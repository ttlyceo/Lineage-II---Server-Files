package ZeuS.interfase;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import ZeuS.server.httpResp;

public class changePassword {
	private static final Logger _log = Logger.getLogger(changePassword.class.getName());
	
	private static Map<L2PcInstance, HashMap<String, String>> CHANGEPASS_CHAR = new HashMap<L2PcInstance, HashMap<String, String>>();
	
	private static void cleanPass(L2PcInstance player){
		if(CHANGEPASS_CHAR!=null){
			if(CHANGEPASS_CHAR.containsKey(player)){
				CHANGEPASS_CHAR.remove(player);
			}
		}
	}
	
	private static String getIDForChangePass(){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(999999);
		return String.valueOf(RandChar);
	}
	
	@SuppressWarnings("unused")
	public static void bypass(L2PcInstance player, String Parametros){
		if(Parametros.length()==0){
			getChangePassWindows(player, "");
			return;
		}
		String params[] = Parametros.split(" ");
		
		if(params.length<4 || params.length>4){
			if(params[0].equals("C1")){
				central.msgbox("Wrog input data", player);
				getChangePassWindows(player, "");
			}else if(params[0].equals("C2")){
				central.msgbox("Wrog input data", player);
			}
		}
		
		//_log.warning("largo=" + String.valueOf(params.length));
		if(params[0].equals("C1")){
			String pass1 = params[1];
			String pass2 = params[2];
			String nomChar = params[3];
			
			if(pass1.length()<4){
				central.msgbox(language.getInstance().getMsg(player).CHANGE_PASS_LENGHT_SHOULD_BE_HIGHER_THANT_4, player);
				getChangePassWindows(player, "");
				return;
			}
			
			if(pass1.equals(pass2)){
				cleanPass(player);
				String ID_EMAIL = getIDForChangePass();
				//central.msgbox(ID_EMAIL, player);
				CHANGEPASS_CHAR.put(player, new HashMap<String, String>());
				CHANGEPASS_CHAR.get(player).put("ID", ID_EMAIL);
				CHANGEPASS_CHAR.get(player).put("PASS", pass1);
				
				httpResp.sendCodeForChangePassword(opera.getUserMail(player.getAccountName()),ID_EMAIL,player);
				
				getChangePassWindows_EnterCode(player,ID_EMAIL,player.getName());
			}else{
				central.msgbox(language.getInstance().getMsg(player).CHANGE_PASS_NO_MATCH, player);
				getChangePassWindows(player, "");
			}
		}else if(params[0].equals("C2")){
			//C2 $txtcode "+ idSet +" "+ NomChar
			String idCodeToEmail = CHANGEPASS_CHAR.get(player).get("ID");
			String NewPass = CHANGEPASS_CHAR.get(player).get("PASS");
			if(params[1].equals(idCodeToEmail)){
				if(setNewPassword(player, NewPass)){
					central.msgbox(language.getInstance().getMsg(player).CHANGE_PASS_NEW_PASSWORD.replace("$account", player.getAccountName()).replace("$newpass", NewPass), player);
				}else{
					central.msgbox(language.getInstance().getMsg(player).CHANGE_PASS_ERROR,player);
				}
			}else{
				getChangePassWindows_EnterCode(player,idCodeToEmail,player.getName());
			}
		}
	}
	
	private static boolean hasUsedThisPasswordBefore(L2PcInstance player, String newPassword){
		boolean asNew = false;
		String Query = "SELECT * FROM zeus_old_password WHERE zeus_old_password.account = ? limit 1";
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Query)){
			statement.setString(1, player.getAccountName());
			ResultSet Resul = statement.executeQuery();
			while(Resul.next()){
				asNew = true;
			}
		}catch(Exception a){
			
		}		
		return asNew;
	}
	
	private static void setNewOldPassword(L2PcInstance player){
		String Query = "INSERT INTO zeus_old_password(account, `old_password`) VALUES (?, (SELECT "+general.LOGINSERVERNAME+".accounts.`password` from "+general.LOGINSERVERNAME+".accounts WHERE "+general.LOGINSERVERNAME+".accounts.login = ? ))";
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Query)){
			statement.setString(1, player.getAccountName());
			statement.setString(2, player.getAccountName());
			statement.execute();
		}catch(Exception a){
			_log.warning("Error add the Old Password to the Database: " + a.getMessage());
		}		
	}	
	
	private static boolean setNewPassword(L2PcInstance player, String newpass){
		if(!general.CAN_USE_A_OLD_PASSWORD_AS_NEW){
			if(hasUsedThisPasswordBefore(player, newpass)){
				central.msgbox(language.getInstance().getMsg(player).CHANGE_PASS_YOU_CANT_USE_OLD_PASS_AS_NEW, player);
				return false;
			}
		}
		int passUpdated=0;
		String EncriptPass = "";
		try{
			MessageDigest md = MessageDigest.getInstance("SHA");		
			byte[] password = newpass.getBytes("UTF-8");
			password = md.digest(password);
			String accountName = player.getAccountName();
			EncriptPass = Base64.getEncoder().encodeToString(password);
			// SQL connection
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement("UPDATE "+general.LOGINSERVERNAME+".accounts SET password=? WHERE login=?"))
			{
				ps.setString(1, EncriptPass);
				ps.setString(2, accountName);
				passUpdated = ps.executeUpdate();
			}
		}catch(Exception e){
			
		}
		if (passUpdated > 0){
			if(!general.CAN_USE_A_OLD_PASSWORD_AS_NEW){
				setNewOldPassword(player);
			}			
			return true;
		}
		return false;
	}
	
	private static void getChangePassWindows_EnterCode(L2PcInstance player, String idSet, String NomChar){
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return;
		}		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/changePassword-validation-code.html");
		String BypassAceptar = "bypass -h ZeuS changepass C2 $txtcode "+ idSet +" "+ NomChar;
		html.replace("%BYPASS_CHANGE%", BypassAceptar);
		opera.enviarHTML(player, html.getHtml());
	}
	
	private static void getChangePassWindows(L2PcInstance player, String params){
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return;
		}		
		if(!EmailRegistration.hasEmailRegister(player)){
			central.msgbox(language.getInstance().getMsg(player).YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL.replace("$command",""), player);
			return;
		}
		String Email = opera.getUserMail(player.getAccountName());
		String ByPassAceptar = "bypass -h ZeuS changepass C1 $txtPass1 $txtPass2 "+ player.getName();
		String Icono = opera.getImageLogo(player);
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/changePassword.html");
		html.replace("%SERVER_LOGO%", Icono);
		html.replace("%EMAIL%", Email);
		html.replace("%BYPASS%", ByPassAceptar);
		opera.enviarHTML(player, html.getHtml());
	}
}
