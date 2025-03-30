package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.jMail;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class changeCharAccount {
//changeCharAccount.html
	private static Map<Integer, String> CodeToChange = new HashMap<Integer, String>();
	private static Map<Integer, String> TempAccount = new HashMap<Integer, String>();
	
	private static final Logger _log = Logger.getLogger(changeCharAccount.class.getName());
	
	private static void cleanPass(L2PcInstance player){
		if(CodeToChange!=null){
			if(CodeToChange.containsKey(player.getObjectId())){
				CodeToChange.remove(player.getObjectId());
			}
		}
		if(TempAccount!=null){
			if(TempAccount.containsKey(player.getObjectId())){
				TempAccount.remove(player.getObjectId());
			}
		}
	}
	
	private static String getIDForChangeChar(){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(999999);
		return String.valueOf(RandChar);
	}
	
	private static int getCountPlayer(String Account){
		String Consulta = "SELECT count(charId) FROM characters WHERE account_name=?";
		int Retorno = 1;
		try{
			Connection conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			psqry.setString(1, Account);			
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					Retorno = rss.getInt(1);
				}catch(Exception e){
					_log.warning("Error Loading char from Account->" + Account + "-->" + e.getMessage());
				}
			}
			conn.close();
		}catch(SQLException e){

		}
		return Retorno;
	}
	
	private static void moveCharToAccount(String CharName, String NewAccount){
		String Consulta = "UPDATE characters SET account_name=? WHERE char_name=?";
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setString(1, NewAccount);
			ins.setString(2, CharName);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}			
	}
	
	public static void checkCodeToChange(L2PcInstance player, String CodeByPlayer){
		if(CodeByPlayer.equals(CodeToChange.get(player.getObjectId()))){
			moveCharToAccount(player.getName(), TempAccount.get(player.getObjectId()));
			player.getClient().closeNow();
		}
	}
	
	public static void checkAccount(L2PcInstance player, String AccountTo){
		if(AccountTo.equalsIgnoreCase(player.getAccountName())){
			central.msgbox(language.getInstance().getMsg(player).CHANGE_ACC_MUST_BE_A_DIFFERENT_ACCOUNT, player);
			ShowFirtWindows(player);
			return;
		}
		
		String EmailToSend = opera.getUserMail(AccountTo);
		if(EmailToSend!=null){
			if(EmailToSend.length()>0){
				int MaxiChar = Config.MAX_CHARACTERS_NUMBER_PER_ACCOUNT;
				int CharInAccount = getCountPlayer(AccountTo);
				if(CharInAccount>=MaxiChar){
					central.msgbox(language.getInstance().getMsg(player).CHANGE_ACC_FULL, player);
					return;
				}
				TempAccount.put(player.getObjectId(), AccountTo);				
				String CodeToSend = getIDForChangeChar();
				CodeToChange.put(player.getObjectId(), CodeToSend);
				jMail.sendCodeForRecovery(player, EmailToSend, CodeToSend);
				ShowSecondWindows(player,EmailToSend);
				return;
			}
		}
		central.msgbox(language.getInstance().getMsg(player).CHANGE_ACC_NO_EXIST_NO_LINKED, player);
	}
	
	private static void ShowSecondWindows(L2PcInstance player, String Email){
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return;
		}		
		String ByPass_CheckCode = "bypass -h ZeuS movechar checkcode $txtCode";
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"changeCharAccount-2.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%EMAIL%", Email);
		html.replace("%BYPASS_CHECK%", ByPass_CheckCode);
		central.sendHtml(player, html.getHtml(), false);
	}	
	
	public static void ShowFirtWindows(L2PcInstance player){
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return;
		}		
		cleanPass(player);
		String ByPass_SendCode = "bypass -h ZeuS movechar checkaccount $txtAccountTo";
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"changeCharAccount.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%LINK%", ByPass_SendCode);
		central.sendHtml(player, html.getHtml(), false);		
	}
}
