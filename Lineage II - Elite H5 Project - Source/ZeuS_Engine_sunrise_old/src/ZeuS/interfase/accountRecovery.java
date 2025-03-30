package ZeuS.interfase;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.jMail;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class accountRecovery {
	private static final Logger _log = Logger.getLogger(accountRecovery.class.getName());
	private static Map<Integer, HashMap<String, HashMap<String, String>>> ACCOUNT_INFO_FROM_PLAYER = new HashMap<Integer, HashMap<String, HashMap<String, String>>>();
	private static Map<Integer, String> CODE_FOR_CHANGE = new HashMap<Integer, String>();
	private static Map<Integer, Integer> VECES = new HashMap<Integer, Integer>();
	private static Map<Integer, Boolean> HAVE_ACCOUNT = new HashMap<Integer, Boolean>();

	private static String getIDForChangePassword(){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(9999999);
		return String.valueOf(RandChar);
	}
	
	
	
	private static int getAccountPlayer(String Account){
		String qry = "select count(charId) from characters where account_name=?";
		int retorno = 0;				
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(qry))
			{
				statement.setString(1, Account);
				try (ResultSet rset = statement.executeQuery())
				{
					if (rset.next())
					{
						retorno = rset.getInt(1);
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Cannot get the Account for recovery");
			}
		return retorno;
	}
	
	private static String getAllAccountFromPlayer(L2PcInstance player){
		
		boolean haveA = false;
		
		ACCOUNT_INFO_FROM_PLAYER.put(player.getObjectId(), new HashMap<String, HashMap<String, String>>());
		
		String Email = opera.getUserMail(player.getAccountName());
		String qry = "select login from " + general.LOGINSERVERNAME + ".accounts where email=?";
		String retorno = "";				
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(qry))
			{
				statement.setString(1, Email);
				try (ResultSet rset = statement.executeQuery())
				{
					while (rset.next())
					{
						if(rset.getString(1).equals(player.getAccountName()) ){
							continue;
						}
						if(retorno.length()>0){
							retorno +=", ";
						}
						retorno += rset.getString(1);
						int Chars = getAccountPlayer(rset.getString(1));
						ACCOUNT_INFO_FROM_PLAYER.get(player.getObjectId()).put(rset.getString(1), new HashMap<String, String>());
						ACCOUNT_INFO_FROM_PLAYER.get(player.getObjectId()).get(rset.getString(1)).put("ACCOUNT", rset.getString(1));
						ACCOUNT_INFO_FROM_PLAYER.get(player.getObjectId()).get(rset.getString(1)).put("CHARS", String.valueOf(Chars));
						haveA = true;
					}
				}
			}
			catch (Exception e)
			{
				HAVE_ACCOUNT.put(player.getObjectId(), false);
				_log.warning("Cannot get the Account for recovery");
			}
		HAVE_ACCOUNT.put(player.getObjectId(), haveA);
		return retorno.length()==0 ? "NONE" : retorno;
	}
	
	@SuppressWarnings("unused")
	private static boolean updateAllAccount(L2PcInstance player, String NewPassword){
		boolean retorno = true;
		boolean checkPreDonation = false;
		String Email = opera.getUserMail(player.getAccountName());
		String qry = "update " + general.LOGINSERVERNAME + ".accounts set password=? where email=? and login != ?";
		Connection con = null;
		PreparedStatement ins = null;
		try{
			MessageDigest md = MessageDigest.getInstance("SHA");		
			byte[] password = NewPassword.getBytes("UTF-8");
			password = md.digest(password);		
			String PassEncript = Base64.getEncoder().encodeToString(password);			
			
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(qry);
			ins.setString(1, PassEncript);
			ins.setString(2, Email);
			ins.setString(3, player.getAccountName());
			try{
				ins.executeUpdate();
				checkPreDonation = true;
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
				retorno = false;
			}
		}catch(Exception a){
			retorno = false;
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}		
		
		
		return retorno;
	}
	
	public static void checkCodeToProccess(L2PcInstance player, String CodeForCheck){
		if(CODE_FOR_CHANGE==null){
			_log.warning("Error on Recovery Email Proces. No have Code Information");
			central.msgbox(language.getInstance().getMsg(player).RACC_ERROR_IN_CODE, player);
			return;
		}else if(CODE_FOR_CHANGE.size()==0){
			_log.warning("Error on Recovery Email Proces. No have Code Information");
			central.msgbox(language.getInstance().getMsg(player).RACC_ERROR_IN_CODE, player);
			return;			
		}else if(!CODE_FOR_CHANGE.containsKey(player.getObjectId())){
			_log.warning("Error on Recovery Email Proces. No have Player Code Information");
			central.msgbox(language.getInstance().getMsg(player).RACC_ERROR_IN_CODE, player);
			return;
		}
		
		if(CODE_FOR_CHANGE.get(player.getObjectId()).equals(CodeForCheck)){
			String NewPassword = getIDForChangePassword();
			updateAllAccount(player,NewPassword);
			jMail.sendRecoveryInformation( player, ACCOUNT_INFO_FROM_PLAYER.get(player.getObjectId()), NewPassword);
			central.msgbox_Lado(language.getInstance().getMsg(player).RAAC_CHECK_EMAIL, player);
		}else{
			if(VECES.get(player.getObjectId())>3){
				central.msgbox("You have no more opportunities", player);
				return;
			}
			VECES.put(player.getObjectId(), VECES.get(player.getObjectId())+1);
			central.msgbox(language.getInstance().getMsg(player).RAAC_ENTER_WROG_CODE, player);
			getCodeWindows(player);
		}
		
	}
	
	public static void sendCodeToRecovery(L2PcInstance player){
		String idForChange = getIDForChangePassword();
		String Email = opera.getUserMail(player.getAccountName());
		CODE_FOR_CHANGE.put(player.getObjectId(), idForChange);
		central.msgbox(language.getInstance().getMsg(player).SENDING_EMAIL_TO.replace("$email", Email), player);
		jMail.sendCodeForRecovery(player, Email, idForChange);
		getCodeWindows(player);
	}
	
	private static void getCodeWindows(L2PcInstance player){
		String ByPassSendCode = "bypass -h ZeuS accountreco checkCode $txtCode";
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"AccountRecovery-2.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%LINK_SENDER%", ByPassSendCode);
		html.replace("%EMAIL%", opera.getUserMail(player.getAccountName()));
		central.sendHtml(player, html.getHtml(), false);		
	}
	
	public static void showRecoveryWindow(L2PcInstance player){
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return;
		}			
		String Email = opera.getUserMail(player.getAccountName());

		VECES.put(player.getObjectId(), 0);
		
		if(Email==null){
			central.msgbox(language.getInstance().getMsg(player).YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL.replace("$command",""), player);
			return;
		}else if(Email.length()==0){
			central.msgbox(language.getInstance().getMsg(player).YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL.replace("$command",""), player);
			return;			
		}
		
		try{
			ACCOUNT_INFO_FROM_PLAYER.get(player.getObjectId()).clear();
		}catch(Exception a){
			
		}
		
		String AccountsFromEmail = getAllAccountFromPlayer(player);
		
		if(!HAVE_ACCOUNT.get(player.getObjectId())){
			central.msgbox(language.getInstance().getMsg(player).RAAC_YOU_DONT_HAVE_ANY_MORE_ACCOUNT_TO_THIS_EMAIL, player);
			return;
		}

		String ByPassSendCode = "bypass -h ZeuS accountreco sendCode";
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"AccountRecovery.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%ACCOUNT%", AccountsFromEmail);
		html.replace("%LINK_SENDER%", ByPassSendCode);
		central.sendHtml(player, html.getHtml(), false);
	}
}
