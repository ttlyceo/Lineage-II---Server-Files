package ZeuS.interfase;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import l2r.gameserver.model.L2ClanMember;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class sellClan {
	private static final Logger _log = Logger.getLogger(sellClan.class.getName());
	private static Map<Integer, DataSellClan>SELL_INFO = new HashMap<Integer, DataSellClan>();
	private static Map<Integer, Integer> SELLER_BUY_INFO = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> TIME_TO_ANSWER= new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> TIME_TO_SEND = new HashMap<Integer, Integer>();
	
	private static final int SECOND_DELAY = 30;
	
	@SuppressWarnings("unused")
	private static String getNewNumericPassword(){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(9999999);
		return String.valueOf(RandChar);
	}
	
	private static boolean startProcess(L2PcInstance playerBuyer, int idPlayerSeller){
		boolean retorno = false;;

		L2PcInstance playerSeller = opera.getPlayerbyID(idPlayerSeller);
		
		if(playerSeller.isOnline()){
			//TODO: Setear al Nuevo clan leader
			
			final L2ClanMember member = playerSeller.getClan().getClanMember( playerBuyer.getName());
			
			if (member.getPlayerInstance().isAcademyMember())
			{
				playerBuyer.sendPacket(SystemMessageId.RIGHT_CANT_TRANSFERRED_TO_ACADEMY_MEMBER);
				playerSeller.sendPacket(SystemMessageId.RIGHT_CANT_TRANSFERRED_TO_ACADEMY_MEMBER);
				return false;
			}
			
			playerSeller.getClan().setNewLeader(member);
			
			playerSeller.getClan().broadcastClanStatus();
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
		
		L2PcInstance playerSeller = opera.getPlayerbyID(IdFromSeller);
		if(playerSeller==null){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_CLAN_LEADER_ERROR, player);
			return;
		}else if(!playerSeller.isOnline()){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_LADER_NOT_ONLINE, player);
			return;			
		}else if(playerSeller.getClan()==null){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_SELLER_NO_HAVE_CLAN_TO_SELL, player);
			return;			
		}else if(!playerSeller.isClanLeader()){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_SELLER_NO_CLAN_LEADER, player);
			return;			
		}else if(player.getClan()==null){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN, player);
			return;			
		}else if(player.getClan() != playerSeller.getClan()){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN, player);
			return;			
		}

		
		if(haveItem){
			if(startProcess(player,IdFromSeller)){
				SELL_INFO.get(IdFromSeller).startProcessPay(player);
				SELL_INFO.remove(IdFromSeller);
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
	
	
	public static void sendWindowsToTarget(L2PcInstance player){
		
		if(!canSendWindowsTime(player)){
			central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_YOU_NEED_TO_WAIT_TO_RESEND, player);
			return;
		}
		
		if(player.getClan()==null){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_YOU_NEED_TO_HAVE_A_CLAN_TO_SELL, player);
			return;
		}else if(!player.isClanLeader()){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_YOU_NEED_TO_HAVE_A_CLAN_TO_SELL, player);
			return;
		}else if(SELL_INFO==null){
			central.msgbox("Error, you need to do it again. If this a Second time message, please contact the Admin", player);
			if(general.DEBUG_CONSOLA_ENTRADAS){
				_log.warning("Error Sell Clan -> SELL_INFO is Null");
			}
			return;
		}else if(SELL_INFO.size()==0){
			central.msgbox("Error, you need to do it again. If this a Second time message, please contact the Admin", player);
			if(general.DEBUG_CONSOLA_ENTRADAS){
				_log.warning("Error Sell Clan-> SELL_INFO size is 0");
			}
			return;
		}else if(!SELL_INFO.containsKey(player.getObjectId())){
			central.msgbox("Error, you need to do it again. If this a Second time message, please contact the Admin", player);
			if(general.DEBUG_CONSOLA_ENTRADAS){
				_log.warning("Error Sell Clan-> SELL_INFO no contains player info");
			}
			return;
		}else if(!SELL_INFO.get(player.getObjectId()).haveRequestedItem()){
			central.msgbox(language.getInstance().getMsg(player).SELLCLAN_ENTER_REQUESTED_ITEM_TO_SELL, player);
			return;
		}
		
		L2Object Target = player.getTarget();
		String ByPassAccept = "bypass -h ZeuS sellClan checkAndBuy";
		String ItemRequest = SELL_INFO.get(player.getObjectId()).getItems(false);		
		
		if(Target instanceof L2PcInstance){
			L2PcInstance Tpl = (L2PcInstance)Target;
			if(Tpl.isOnline() && !Tpl.getClient().isDetached()){
				
				if(Tpl.getClan()==null){
					central.msgbox(language.getInstance().getMsg(player).SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN, player);
					return;
				}else if(Tpl.getClan() != player.getClan()){
					central.msgbox(language.getInstance().getMsg(player).SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN, player);
					return;					
				}
				
				if(canAnswer(Tpl)){
					central.msgbox(language.getInstance().getMsg(player).SELLACCOUNT_TARGET_NOT_READY, player);
					return;
				}
				
				SELLER_BUY_INFO.put(Tpl.getObjectId(), player.getObjectId());
				final NpcHtmlMessage html = comun.htmlMaker(Tpl, general.DIR_HTML + "/" + language.getInstance().getFolder(Tpl) + "/" +"sellClan-sender.html");
				html.replace("%SERVER_LOGO%", opera.getImageLogo(Tpl));
				html.replace("%LINK_ACCEPT%", ByPassAccept);
				html.replace("%TABLE_ITEM%", ItemRequest);
				html.replace("%CLAN_NAME%", player.getClan().getName());
				html.replace("%CLAN_LEVEL%", String.valueOf(player.getClan().getLevel()));
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
	
	public static void showSellConfig(L2PcInstance player){
		//sellAccount.html
		String imageLogo = opera.getImageLogo(player);
		if(SELL_INFO==null){
			DataSellClan T1 = new DataSellClan(player);
			SELL_INFO.put(player.getObjectId(), T1);
		}else if(SELL_INFO.size()==0){
			DataSellClan T1 = new DataSellClan(player);
			SELL_INFO.put(player.getObjectId(), T1);
		}else if(SELL_INFO.containsKey(player.getObjectId())){
			if(SELL_INFO.get(player.getObjectId()).getPpl()==null){
				SELL_INFO.get(player.getObjectId()).setPpl(player);
			}else if(SELL_INFO.get(player.getObjectId()).getPpl() != player){
				SELL_INFO.get(player.getObjectId()).setPpl(player);
			}
		}else if(!SELL_INFO.containsKey(player.getObjectId())){
			DataSellClan T1 = new DataSellClan(player);
			SELL_INFO.put(player.getObjectId(), T1);
		}
		String ComboRequestItem = getItemForRequest();
		String ByPassAddItem = "bypass -h ZeuS sellClan addItem $cmbItem $txtAmount";
		String ByPassSenderWindows = "bypass -h ZeuS sellClan sendWindowsTarget";//%LINK_SENDER%
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"sellClan.html");
		html.replace("%SERVER_LOGO%", imageLogo);
		html.replace("%LIST%", ComboRequestItem);
		html.replace("%LINK_ADD_ITEM%", ByPassAddItem);
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
	
	private static class DataSellClan{
		Map<Integer, Long>ITEM_REQUEST = new HashMap<Integer, Long>();
		L2PcInstance SELLER;
		public DataSellClan(L2PcInstance Seller){
			SELLER = Seller;
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
					String ByPassRemove = withRemove ? "<button value=\"Del\" action=\"bypass -h ZeuS sellClan remove %IDITEM%\" width=25 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">" : "";
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
			if(ITEM_REQUEST!=null){
				if(ITEM_REQUEST.size()>0){
					Iterator itr = ITEM_REQUEST.entrySet().iterator();
				    while(itr.hasNext()){
				    	Map.Entry Entrada = (Map.Entry)itr.next();
				    	int IdItem = (int)Entrada.getKey();
				    	long Count = (long) Entrada.getValue();
				    	opera.removeItem(IdItem, Count, fromPlayer);
				    	opera.giveReward(SELLER, IdItem, Count);
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
