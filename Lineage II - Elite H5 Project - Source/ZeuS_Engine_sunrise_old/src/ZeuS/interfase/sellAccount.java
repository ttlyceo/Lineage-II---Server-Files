package ZeuS.interfase;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.server.comun;

public class sellAccount {
	private static final Logger _log = Logger.getLogger(sellAccount.class.getName());
	private static Map<Integer, DataSell>SELL_INFO = new HashMap<Integer, DataSell>();
	private static Map<Integer, Integer> SELLER_BUY_INFO = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> TIME_TO_ANSWER= new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> TIME_TO_SEND = new HashMap<Integer, Integer>();
	
	private static final int SECOND_DELAY = 30;
	
	private static String getNewNumericPassword(){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(9999999);
		return String.valueOf(RandChar);
	}
	
	private static boolean setNewPassword(L2PcInstance player, String newpass){
		int passUpdated=0;		
		try{
			MessageDigest md = MessageDigest.getInstance("SHA");		
			byte[] password = newpass.getBytes("UTF-8");
			password = md.digest(password);
			String accountName = player.getAccountName();
			// SQL connection
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE "+general.LOGINSERVERNAME+".accounts SET password=? WHERE login=?"))
			{
				ps.setString(1, Base64.getEncoder().encodeToString(password));
				ps.setString(2, accountName);
				passUpdated = ps.executeUpdate();
			}
		}catch(Exception e){
			
		}
		if (passUpdated > 0){
			return true;
		}
		return false;
	}
	
	private static void removeSecondaryPassword(L2PcInstance player){
		String Qsql = "delete from zeus_secundary_pass where zeus_secundary_pass.account=?";
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Qsql);
			ins.setString(1, player.getAccountName());
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}		
	}	
	
	private static boolean startProcess(L2PcInstance playerBuyer, int idPlayerSeller){
		boolean retorno = false;
		L2PcInstance playerSeller = opera.getPlayerbyID(idPlayerSeller);
		if(general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE){
			if(playerSeller.isOnline()){
				String EmailFromBuyer = opera.getUserMail(playerBuyer.getAccountName());
				String NewPasswor = getNewNumericPassword();
				setNewPassword(playerSeller,NewPasswor);
				opera.setUserEmailtoBD(playerSeller,EmailFromBuyer);
				removeSecondaryPassword(playerSeller);
				EmailSend.sendNewPasswordSellAccount(playerBuyer, NewPasswor, playerSeller.getAccountName());
				retorno = true;
			}
		}else{
			String EmailFromBuyer = opera.getUserMail(playerBuyer.getAccountName());
			String NewPasswor = getNewNumericPassword();
			setNewPassword(playerSeller,NewPasswor);
			opera.setUserEmailtoBD(playerSeller,EmailFromBuyer);
			removeSecondaryPassword(playerSeller);
			EmailSend.sendNewPasswordSellAccount(playerBuyer, NewPasswor, playerSeller.getAccountName());
			retorno = true;			
		}
		return retorno;
	}
	
	public static void checkAndBuyit(L2PcInstance player){
		if(!canAnswer(player)){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_TIME_IS_OVER, player);
			return;
		}
		int IdFromSeller = SELLER_BUY_INFO.get(player.getObjectId());
		boolean haveItem = SELL_INFO.get(IdFromSeller).HaveTheRequestItem(player);
		boolean isReceiverOnline = SELL_INFO.get(IdFromSeller).isOnlineReceiver();
		
		
		if(!isReceiverOnline && general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_RECEIVER_PLAYER_IS_NOT_ONLINE, player);
			return;
		}
		
		if(haveItem){
			if(startProcess(player,IdFromSeller)){
				L2PcInstance pplToClose = opera.getPlayerbyID(IdFromSeller);
				SELL_INFO.get(IdFromSeller).startProcessPay(player);
				SELL_INFO.remove(IdFromSeller);
				pplToClose.getClient().closeNow();
			}
		}
	}
	
	private static boolean canAnswer(L2PcInstance player){
		if(TIME_TO_ANSWER!=null){
			if(TIME_TO_ANSWER.size()>0){
				if(TIME_TO_ANSWER.containsKey(player.getObjectId())){
					int timeNow = opera.getUnixTimeNow();
					int timeSave = TIME_TO_ANSWER.get(player.getObjectId());
					return timeSave >= timeNow;
				}
			}
		}
		return false;
	}
	
	private static boolean canSendWindowsTime(L2PcInstance player){
		if(TIME_TO_SEND!=null){
			if(TIME_TO_SEND.size()>0){
				if(TIME_TO_SEND.containsKey(player.getObjectId())){
					int timeNow = opera.getUnixTimeNow();
					int timeSave = TIME_TO_SEND.get(player.getObjectId());
					return timeSave <= timeNow;
				}
			}
		}
		return true;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static void sendWindowsToTarget(L2PcInstance player){
		
		if(!canSendWindowsTime(player)){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_YOU_NEED_TO_WAIT_TO_RESEND, player);
			return;
		}
		
		if(SELL_INFO==null){
			central.msgbox("Error, you need to do it again. If this a Second time message, please contact the Admin", player);
			_log.warning("Error Sell Account-> SELL_INFO is Null");
			return;
		}else if(SELL_INFO.size()==0){
			central.msgbox("Error, you need to do it again. If this a Second time message, please contact the Admin", player);
			_log.warning("Error Sell Account-> SELL_INFO size is 0");
			return;
		}else if(!SELL_INFO.containsKey(player.getObjectId())){
			central.msgbox("Error, you need to do it again. If this a Second time message, please contact the Admin", player);
			_log.warning("Error Sell Account-> SELL_INFO no contains player info");
			return;
		}else if(!SELL_INFO.get(player.getObjectId()).haveRequestedItem()){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_ENTER_REQUESTED_ITEM_TO_SELL, player);
			return;
		}else if(SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY.length()==0 ){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_ENTER_PLAYER_TO_RECEIVED_PAYMENT, player);
			return;			
		}else if(general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE && !opera.isCharInGame( SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY)){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_YOUR_RECEIVER_CHAR_IS_NOT_ONLINE.replace("$name",SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY), player);
			return;			
		}else if(general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE && !opera.isCharOnline(SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY)){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_YOUR_RECEIVER_CHAR_IS_NOT_ONLINE.replace("$name",SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY), player);
			return;			
		}
		
		L2Object Target = player.getTarget();
		String ByPassAccept = "bypass -h ZeuS sellAccount checkAndBuy";
		String ItemRequest = SELL_INFO.get(player.getObjectId()).getItems(false);		
		
		if(Target instanceof L2PcInstance){
			L2PcInstance Tpl = (L2PcInstance)Target;
			if(Tpl.isOnline() && !Tpl.getClient().isDetached()){
				if(canAnswer(Tpl)){
					central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_TARGET_NOT_READY, player);
					return;
				}
				
				Map<Integer, String> allPlayer = opera.getAllPlayerOnThisAccount(player);
				
				Iterator itr = allPlayer.entrySet().iterator();
				String NombreChar = player.getName();
			    while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	if(NombreChar.length()>0){
			    		NombreChar+=",";
			    	}
			    	NombreChar += (String)Entrada.getValue();
			    }
				
				
				SELLER_BUY_INFO.put(Tpl.getObjectId(), player.getObjectId());
				final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"sellAccount-sender.html");
				html.replace("%SERVER_LOGO%", opera.getImageLogo(Tpl));
				html.replace("%LINK_ACCEPT%", ByPassAccept);
				html.replace("%TABLE_ITEM%", ItemRequest);
				html.replace("%RECEIVER%", SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY);
				html.replace("%CHAR_LIST%", NombreChar);
				html.replace("%SELLER%", player.getName());
				
				
				central.sendHtml(Tpl, html.getHtml(), false);
				TIME_TO_ANSWER.put(Tpl.getObjectId(), opera.getUnixTimeNow() + SECOND_DELAY);
				TIME_TO_SEND.put(player.getObjectId(), opera.getUnixTimeNow() + SECOND_DELAY + 2);
				return;
			}
		}
		central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_TARGET_PLAYER, player);
	}
	
	public static void setRemoveItem(L2PcInstance player, int idItem){
		SELL_INFO.get(player.getObjectId()).removeItem(idItem);
	}
	
	public static void setItem(L2PcInstance player, int idItem, long amount){
		SELL_INFO.get(player.getObjectId()).setItemToRequest(idItem, amount);
	}
	public static void setReceiver(L2PcInstance player, String Name){
		SELL_INFO.get(player.getObjectId()).setMoneySender(Name);
	}
	
	public static void showSellConfig(L2PcInstance player){
		//sellAccount.html
		String imageLogo = opera.getImageLogo(player);
		if(SELL_INFO==null){
			DataSell T1 = new DataSell(player);
			SELL_INFO.put(player.getObjectId(), T1);
		}else if(SELL_INFO.size()==0){
			DataSell T1 = new DataSell(player);
			SELL_INFO.put(player.getObjectId(), T1);
		}else if(SELL_INFO.containsKey(player.getObjectId())){
			if(SELL_INFO.get(player.getObjectId()).getPpl()==null){
				SELL_INFO.get(player.getObjectId()).setPpl(player);
			}else if(SELL_INFO.get(player.getObjectId()).getPpl() != player){
				SELL_INFO.get(player.getObjectId()).setPpl(player);
			}
		}else if(!SELL_INFO.containsKey(player.getObjectId())){
			DataSell T1 = new DataSell(player);
			SELL_INFO.put(player.getObjectId(), T1);
		}
		String ComboRequestItem = getItemForRequest();
		String ByPassAddItem = "bypass -h ZeuS sellAccount addItem $cmbItem $txtAmount";
		String ByPassAddReceiver = "bypass -h ZeuS sellAccount addReceiver $txtReceiver";
		String ByPassSenderWindows = "bypass -h ZeuS sellAccount sendWindowsTarget";//%LINK_SENDER%
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"sellAccount.html");
		html.replace("%SERVER_LOGO%", imageLogo);
		html.replace("%LIST%", ComboRequestItem);
		html.replace("%LINK_ADD_ITEM%", ByPassAddItem);
		html.replace("%RECEIVER_NAME%", SELL_INFO.get(player.getObjectId()).CHAR_TO_SEND_MONEY);
		html.replace("%LINK_ADD_RECEIVER%", ByPassAddReceiver);
		html.replace("%TABLE_ITEM%", SELL_INFO.get(player.getObjectId()).getItems(true));
		html.replace("%LINK_SENDER%", ByPassSenderWindows);
		central.sendHtml(player, html.getHtml(), false);		
	}
	
	public static int getRequestItemID(String ItemFromCmb){
		if(ItemFromCmb.equalsIgnoreCase("adena")){
			return 57;
		}
		
		if(general.ACCOUNT_SELL_ITEMS_REQUEST !=null){
			for(String t : general.ACCOUNT_SELL_ITEMS_REQUEST ){
				if(t.split(":")[1].startsWith(ItemFromCmb)){
					return Integer.valueOf(t.split(":")[0]);
				}
			}
		}		
		
		return 57;
		
	}
	
	private static String getItemForRequest(){
		String retorno = "";
		if(general.ACCOUNT_SELL_ITEMS_REQUEST!=null){
			for(String t : general.ACCOUNT_SELL_ITEMS_REQUEST){
				if(retorno.length()>0){
					retorno += ";";
				}
				String NomItem = t.split(":")[1];
				retorno += NomItem.length()>15 ? NomItem.substring(0,15) : NomItem;
			}
		}
		
		if(retorno.length()==0){
			retorno = "Adena";
		}
		return retorno;
	}
	
	private static class DataSell{
		String CHAR_TO_SEND_MONEY = "";
		Map<Integer, Long>ITEM_REQUEST = new HashMap<Integer, Long>();
		L2PcInstance SELLER;
		public DataSell(L2PcInstance Seller){
			SELLER = Seller;
			CHAR_TO_SEND_MONEY = "";
		}
		public L2PcInstance getPpl(){
			return SELLER;
		}
		public boolean haveRequestedItem(){
			if(ITEM_REQUEST!=null){
				if(ITEM_REQUEST.size()>0){
					return true;
				}
			}
			return false;
		}
		public void setPpl(L2PcInstance player){
			SELLER = player;
		}
		@SuppressWarnings("rawtypes")
		public String getItems(boolean withRemove){
			String[] Color = {"F7BE81","F2F5A9"};
			String Table = "<table width=280>%TDS%</table>";
			if(ITEM_REQUEST!=null){
				if(ITEM_REQUEST.size()>0){
					Iterator itr = ITEM_REQUEST.entrySet().iterator();
					String Retorno = "";
					String TDS_SHOW_BUYER = "<tr><td width=140 align=\"RIGHT\"><font color=%COLOR%>%NAME%</font></td><td width=140 align=\"LEFT\"><font color=%COLOR%>%AMOUNT%</font></td></tr>";
					String TDS = withRemove ? "<tr><td width=120 align=\"CENTER\"><font color=%COLOR%>%NAME%</font></td><td width=120><font color=%COLOR%>%AMOUNT%</font></td><td width=40>%REMOVE%</td></tr>" : TDS_SHOW_BUYER;
					String ByPassRemove = withRemove ? "<button value=\"Del\" action=\"bypass -h ZeuS sellAccount remove %IDITEM%\" width=25 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" : "";
					int cont=0;
				    while(itr.hasNext()){
				    	Map.Entry Entrada = (Map.Entry)itr.next();
				    	int IdItem = (int)Entrada.getKey();
				    	long Count = (long) Entrada.getValue();
				    	Retorno += TDS.replace("%REMOVE%", ByPassRemove.replace("%IDITEM%", String.valueOf(IdItem)) ) .replace("%COLOR%", Color[cont%2]) .replace("%REMOVE%", ByPassRemove).replace("%AMOUNT%", opera.getFormatNumbers(Count)) .replace("%NAME%", central.getNombreITEMbyID(IdItem));
				    	cont++;
				    }
				    return Table.replace("%TDS%", Retorno);
				}
			}
			return "";
		}
		
		@SuppressWarnings("rawtypes")
		private void startProcessPay(L2PcInstance fromPlayer){
			L2PcInstance toPlayer = opera.getPlayerbyName(CHAR_TO_SEND_MONEY);
			
			boolean isOnline = opera.isCharOnline(toPlayer);
			
			if(ITEM_REQUEST!=null){
				if(ITEM_REQUEST.size()>0){
					if(general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE || isOnline){
						Iterator itr = ITEM_REQUEST.entrySet().iterator();
					    while(itr.hasNext()){
					    	Map.Entry Entrada = (Map.Entry)itr.next();
					    	int IdItem = (int)Entrada.getKey();
					    	long Count = (long) Entrada.getValue();
				    		opera.removeItem(IdItem, Count, fromPlayer);
				    		opera.giveReward(toPlayer, IdItem, Count);
					    }
					}else{
						Iterator itr = ITEM_REQUEST.entrySet().iterator();
						String ItemsForEmail = "";
						while(itr.hasNext()){
					    	Map.Entry Entrada = (Map.Entry)itr.next();
					    	int IdItem = (int)Entrada.getKey();
					    	long Count = (long) Entrada.getValue();
					    	if(ItemsForEmail.length()>0){
					    		ItemsForEmail += ";";
					    	}
					    	ItemsForEmail += String.valueOf(IdItem) + "," + String.valueOf(Count);
					    }
						EmailSend.sendEmail(null, CHAR_TO_SEND_MONEY , "Sell Account Payment", "This are u Payment for selling your account", ItemsForEmail, tipoMensaje.Account );
					}
				}
			}			
		}
		
		private void setItemToRequest(int idItem, long count){
			if(ITEM_REQUEST==null){
				ITEM_REQUEST.put(idItem, count);
				return;
			}else if(!ITEM_REQUEST.containsKey(idItem)){
				ITEM_REQUEST.put(idItem, count);
				return;
			}else if(ITEM_REQUEST.size()==0){
				ITEM_REQUEST.put(idItem, count);
				return;
			}else if(ITEM_REQUEST.size()>=8){
				central.msgbox(language.getInstance().getMsg(SELLER).SELLACCOUNT_CANT_ADD_MORE_ITEM, SELLER);
				return;
			}
			ITEM_REQUEST.put(idItem, ITEM_REQUEST.get(idItem) + count);
		}
		
		private void removeItem(int idItem){
			if(ITEM_REQUEST!=null){
				if(ITEM_REQUEST.containsKey(idItem)){
					ITEM_REQUEST.remove(idItem);
				}
			}
		}
		
		private boolean isOnlineReceiver(){
			if(!general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE){
				return true;
			}
			L2PcInstance plRe = opera.getPlayerbyName(CHAR_TO_SEND_MONEY);
			if(plRe == null){
				central.msgbox(language.getInstance().getMsg(SELLER).SELLACCOUNT_RECEIVER_PLAYER_IS_NOT_ONLINE, SELLER);
				return false;
			}
			boolean isOnline = opera.isCharOnline(plRe);
			if(!isOnline){
				central.msgbox(language.getInstance().getMsg(SELLER).SELLACCOUNT_RECEIVER_PLAYER_IS_NOT_ONLINE, SELLER);
			}
			return isOnline;
		}
		
		private boolean setMoneySender(String name){
			L2PcInstance playerS = opera.getPlayerbyName(name);
			if(playerS==null && general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE){
				central.msgbox(language.getInstance().getMsg(SELLER).SELLACCOUNT_NAME_NO_EXISTS.replace("$name", name), SELLER);
				return false;
			}else if(playerS!=null){
				if(!playerS.isOnline() && general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE){
					central.msgbox("The Char is not Online", SELLER);
					return false;					
				}
			}else if(!general.SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE){
				String SqlConsulta = "SELECT characters.charId FROM characters WHERE characters.char_name=?";
				try (Connection con = L2DatabaseFactory.getInstance().getConnection();
						PreparedStatement statement = con.prepareStatement(SqlConsulta))
					{
						statement.setString(1, name);
						try (ResultSet inv = statement.executeQuery())
						{
							if (!inv.next())
							{
								central.msgbox("The Name "+ name + " no exist's", SELLER);
								return false;
							}
						}catch(Exception a){
							_log.warning("Error A ->" + a.getMessage());
						}
				}catch (Exception e){
					_log.warning("Error B ->" + e.getMessage());
				}					
			}
			CHAR_TO_SEND_MONEY = name;
			return true;
		}
		@SuppressWarnings("rawtypes")
		private boolean HaveTheRequestItem(L2PcInstance player){
			if(ITEM_REQUEST!=null){
				if(ITEM_REQUEST.size()>0){
					Iterator itr = ITEM_REQUEST.entrySet().iterator();
				    while(itr.hasNext()){
				    	Map.Entry Entrada = (Map.Entry)itr.next();
				    	int IdItem = (int)Entrada.getKey();
				    	long Count = (long) Entrada.getValue();
				    	if(!opera.haveItem(player, IdItem, Count)){
				    		return false;
				    	}
				    }
				}
			}
			return true;
		}
	}
}
