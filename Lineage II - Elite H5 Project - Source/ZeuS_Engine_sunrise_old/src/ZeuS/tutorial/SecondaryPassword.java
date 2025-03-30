package ZeuS.tutorial;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.sendC;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.jMail;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import gabriel.community.communityDailyLogin.DailyLoginManager;
import gabriel.community.communityDailyLogin.DailyLoginPremiumManager;
import gabriel.config.DailyLoginConfig;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SocialAction;

public class SecondaryPassword {
	private static Map<Integer, String> SECUNDARY_PASSWORD = new HashMap<Integer, String>();
	private static final Logger _log = Logger.getLogger(SecondaryPassword.class.getName());
	private static Map<Integer, Boolean> isOk = new HashMap<Integer, Boolean>();
	private static HashMap<String, HashMap<Integer, HashMap<String, String>>> FAIL_ENTRY_FOR_EMAIL = new HashMap<String, HashMap<Integer, HashMap<String, String>>>();
	
	private static Map<Integer, Integer> FAIL_REMOVE_IT = new HashMap<Integer, Integer>();
	
	public static boolean isValid(L2PcInstance player){
		if(!general.SECONDAY_PASSWORD){
			return true;
		}
		
		try{
			boolean Retorno = isOk.get(player.getObjectId());
			if(!Retorno){
				central.msgbox_Lado("Enter the Secondary password Firt's.", player);
			}
			return Retorno;
		}catch(Exception a){
			
		}
		
		return false;
	}
	
	private static boolean isThisSecundaryPass(L2PcInstance player, String PassIn){
		if(SECUNDARY_PASSWORD.containsKey(player.getObjectId())){
			return SECUNDARY_PASSWORD.get(player.getObjectId()).equals(PassIn);
		}
		return false;
	}
	
	private static String getEncrytedPass(String Pass){
		try{
			MessageDigest md = MessageDigest.getInstance("SHA");		
			byte[] password = Pass.getBytes("UTF-8");
			password = md.digest(password);
			String PassEncript = Base64.getEncoder().encodeToString(password);
			return PassEncript;
		}catch(Exception a){
			_log.warning("Error creating Secundary pass Encript-> Pass->" + Pass + ", Error->" + a.getMessage());
			return "";
		}
	}
	
	private static boolean setSecundaryPass(L2PcInstance player, String Pass){
		boolean retorno = false;
		boolean isTemplatePassOK = opera.isValidSecondaryPassword(Pass);
		if(isTemplatePassOK){
			
			if(Pass.length()>= general.SECONDARY_PASSWORD_MIN_LENGHT && Pass.length() <= general.SECONDARY_PASSWORD_MAX_LENGHT){
				String PassEncript = getEncrytedPass(Pass);
				String Query = "INSERT INTO zeus_secundary_pass(account,sec_pass) VALUES (?,?)  ON DUPLICATE KEY UPDATE sec_pass=?";
				Connection con = null;
				PreparedStatement ins = null;
				try{
					con = L2DatabaseFactory.getInstance().getConnection();
					ins = con.prepareStatement(Query);
					ins.setString(1, player.getAccountName());
					ins.setString(2, PassEncript);
					ins.setString(3, PassEncript);
					try{
						ins.executeUpdate();
						retorno = true;
						isOk.put(player.getObjectId(), true);
						paraPlayer(player, false);
					}catch(SQLException e){
						_log.warning("Error ZeuS E->" + e.getMessage());
					}
				}catch(Exception a){
					_log.warning("Error Zeus A-> " + a.getMessage());
				}
				try{
					ins.close();
					con.close();			
				}catch(Exception a){
					
				}
			}else{
				central.msgbox_Lado(language.getInstance().getMsg(player).SP_MOST_HAVE_LENGHT.replace("$min",String.valueOf(general.SECONDARY_PASSWORD_MIN_LENGHT)).replace("$max",String.valueOf(general.SECONDARY_PASSWORD_MAX_LENGHT)),player , "S. PASS");
			}
		}else{
			central.msgbox(language.getInstance().getMsg(player).SP_CHAR_TEMPLATE_WROG, player);
		}
		
		return retorno;
	}
	
	private static boolean hasSecundaryPass(L2PcInstance player){
		try{
			SECUNDARY_PASSWORD.remove(player.getObjectId());
		}catch(Exception a){
			
		}
		String Consulta = "SELECT zeus_secundary_pass.sec_pass FROM zeus_secundary_pass WHERE zeus_secundary_pass.account = ?";
		String Secundary = "";
		boolean hasSecundary = false;
		
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			psqry.setString(1, player.getAccountName());
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						Secundary = rss.getString("sec_pass");
						if(Secundary!=null){
							if(Secundary.trim().length()>0){
								SECUNDARY_PASSWORD.put(player.getObjectId(), Secundary);
								hasSecundary = true;
								isOk.put(player.getObjectId(), false);
							}
						}
					}catch(SQLException e){

					}
				}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}		
		
		return hasSecundary;
	}
	
	private static boolean performSocial(int action, L2PcInstance player)
	{
		try
		{
			if (((action < 2) || ((action > 18) && (action != SocialAction.LEVEL_UP))))
			{
				return false;
			}
			L2Character character = (L2Character) player;
			character.broadcastPacket(new SocialAction(character.getObjectId(), action));
		}
		catch (Exception e)
		{
		}
		return true;
	}
	
	public static boolean haveSP(int idPlayer){
		boolean haveSe = true;
		if(SECUNDARY_PASSWORD==null){
			haveSe = false;
		}else if(SECUNDARY_PASSWORD.size()==0){
			haveSe = false;
		}else if(!SECUNDARY_PASSWORD.containsKey(idPlayer)){
			haveSe = false;			
		}
		
		if(!haveSe){
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unused")
	public static void showRemoveWindos(L2PcInstance player){
		
		if(!haveSP(player.getObjectId())){
			central.msgbox("You dont have Secondary Password", player);
			return;
		}
		
		
		String imageLogo = opera.getImageLogo(player);
		String ByPass_set = "Write Z_SEND_SECOND_REG Set _ txtPass1 txtPass2 txtPass2";
		String ByPass_remove = "Write Z_SEND_SECOND_CHECK_REMOVE Set _ txtPass1 txtPass1 txtPass1";
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"removeSecondaryPass.html");
		html.replace("%SERVER_LOGO%", imageLogo);
		html.replace("%LINK%", ByPass_remove);
		central.sendHtml(player, html.getHtml(), true);		
	}
	
	private static void showRegister(L2PcInstance player, boolean Register){
		String imageLogo = opera.getImageLogo(player);
		String ByPass_set = "Write Z_SEND_SECOND_REG Set _ txtPass1 txtPass2 txtPass2";
		String ByPass_check = "Write Z_SEND_SECOND_CHECK Set _ txtPass1 txtPass1 txtPass1";
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +( Register ? "SpasswordRegister.html" : "SpasswordRegister-2.html"));
		html.replace("%SERVER_LOGO%", imageLogo);
		html.replace("%LINK%", ( Register ? ByPass_set : ByPass_check));
		central.sendHtml(player, html.getHtml(), true);				
	}
	
	private static void ActionWait(L2PcInstance player, boolean isRegister){
		if(!general.SECONDARY_PASSWORD_BLOCK_CHAR && isRegister){
			try{
				isOk.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			isOk.put(player.getObjectId(), true);
			return;
		}
		opera.setimmobilizeChar(player, true);
		if(!player.isGM()){
			player._isChatBlock=true;
		}
		int idSkill = 5441;
		player.broadcastPacket(new MagicSkillUse(player, player, idSkill, 1, 1, 0));		
	}

	private static void paraPlayer(L2PcInstance player, boolean para){
		opera.setimmobilizeChar(player, para);
		if(!player.isGM()){
			player._isChatBlock=para;
		}
		int idSkill = 5441;
		player.broadcastPacket(new MagicSkillUse(player, player, idSkill, 1, 1, 0));
	}

	private static void ActionCorrectCreateSecunday(L2PcInstance player){
		int idSkill = 5441;
		player.sendPacket(new sendC());
		opera.setimmobilizeChar(player, false);
		player._isChatBlock=false;
		player.broadcastPacket(new MagicSkillUse(player, player, idSkill, 1, 1, 0));
		//player.broadcastPacket(new MagicSkillUse(player, 6463, 1, 1000, 0));
		performSocial(4,player);
		CBByPass.byPass(player, general.getCOMMUNITY_BOARD_PART_EXEC());
	}	
	
	private static void ActionCorrectPassword(L2PcInstance player){
		int idSkill = 1034;
		player.sendPacket(new sendC());
		opera.setimmobilizeChar(player, false);
		player._isChatBlock=false;
		player.broadcastPacket(new MagicSkillUse(player, idSkill, 1, 1000, 0));
		player.broadcastPacket(new MagicSkillUse(player, 6463, 1, 1000, 0));
		performSocial(3,player);
		CBByPass.byPass(player, general.getCOMMUNITY_BOARD_PART_EXEC());
		ZeuS.isAdvensedClassMaster(player);
//		ShowNotices(player);
		if(DailyLoginConfig.DAILY_LOGIN_ENABLE) {
			if(ZeuS.isPremium(player)){
				DailyLoginPremiumManager.getInstance().parseHtm(player);
			}else {
				DailyLoginManager.getInstance().parseHtm(player);
			}
		}
	}
	
	private static void ActionIncorrect(L2PcInstance player, String PassWorg){
		String CuentaPlayer = player.getAccountName();
		player.getClient().closeNow();
		EmailSend.sendPasswordWrogPass(player,PassWorg);
		String EmailAccount = opera.getUserMail(CuentaPlayer);
		boolean haveEmail = true;
		if(EmailAccount==null){
			haveEmail = false;
		}else{
			if(EmailAccount.length()<=0){
				haveEmail = false;
			}
		}
		boolean isNew = false;
		if(haveEmail){
			//FastMap<String,Map<Integer, Map<String,String>>>
			if(FAIL_ENTRY_FOR_EMAIL==null){
				FAIL_ENTRY_FOR_EMAIL.put(CuentaPlayer, new HashMap<Integer, HashMap<String, String>>());
				isNew = true;
			}else if(!FAIL_ENTRY_FOR_EMAIL.containsKey(CuentaPlayer)){
				FAIL_ENTRY_FOR_EMAIL.put(CuentaPlayer, new HashMap<Integer, HashMap<String, String>>());
				isNew = true;
			}
			int UnixTimeNow = opera.getUnixTimeNow();
			FAIL_ENTRY_FOR_EMAIL.get(CuentaPlayer).put(UnixTimeNow, new HashMap<String, String>());
			FAIL_ENTRY_FOR_EMAIL.get(CuentaPlayer).get(UnixTimeNow).put("ACCOUNT", CuentaPlayer);
			FAIL_ENTRY_FOR_EMAIL.get(CuentaPlayer).get(UnixTimeNow).put("UNIX", String.valueOf(UnixTimeNow));
			FAIL_ENTRY_FOR_EMAIL.get(CuentaPlayer).get(UnixTimeNow).put("PASSWORD", PassWorg);
			FAIL_ENTRY_FOR_EMAIL.get(CuentaPlayer).get(UnixTimeNow).put("TIME", opera.getDateFromUnixTime(UnixTimeNow));
			if(isNew){
				int Minutos = jMail.MinutesSend();
				ThreadPoolManager.getInstance().scheduleGeneral(new SenderSPTimer(player, CuentaPlayer,EmailAccount), (Minutos * 60) * 1000);
			}
		}
	}
	
	public static void checkSecPass(L2PcInstance player, String PassIn, boolean isRemove){
		String EncripPass = getEncrytedPass(PassIn);
		boolean isValidIn = isThisSecundaryPass(player,EncripPass);
		if(!isRemove){
			if(isValidIn){
				central.msgbox_Lado(language.getInstance().getMsg(player).SP_WAS_CORRECT, player);
				isOk.put(player.getObjectId(), true);
				ActionCorrectPassword(player);
			}else{
				central.msgbox_Lado(language.getInstance().getMsg(player).SP_WAS_INCORRECT, player);
				ActionIncorrect(player,PassIn);
			}
		}else{
			
			if(FAIL_REMOVE_IT==null){
				FAIL_REMOVE_IT.put(player.getObjectId(), 1);
			}else if(!FAIL_REMOVE_IT.containsKey(player.getObjectId())){
				FAIL_REMOVE_IT.put(player.getObjectId(), 1);
			}else if(FAIL_REMOVE_IT.containsKey(player.getObjectId())){
				int Veces = FAIL_REMOVE_IT.get(player.getObjectId());
				FAIL_REMOVE_IT.put(player.getObjectId(), Veces + 1);
			}
			
			if(FAIL_REMOVE_IT.get(player.getObjectId()) >3){
				FAIL_REMOVE_IT.remove(player.getObjectId());
				central.msgbox(language.getInstance().getMsg(player).SP_WAS_INCORRECT_DISCONNECT, player);
				player.getClient().closeNow();
			}
			
			if(isValidIn){
				removeSecondaryPassword(player);
				central.msgbox(language.getInstance().getMsg(player).SP_WAS_CORRECT_AND_REMOVE_FROM_ACCOUNT, player);
				player.getClient().closeNow();
			}else{
				central.msgbox_Lado(language.getInstance().getMsg(player).SP_WAS_INCORRECT_AND_REMOVE_PROCESS_WAS_CANCEL, player);
				player.sendPacket(new sendC());
			}
		}
	}
	
	public static boolean removeSecondaryPassword(L2PcInstance player){
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
				return false;
			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}		
		return true;
	}
	
	private static void CleanPlayerInformation(L2PcInstance player){
		//TODO: Aqui se debe incorporar la limpieza de todas las variables
		try{
			isOk.remove(player.getObjectId());
		}catch(Exception a){
			
		}
	}
	
	private static void ShowNotices(L2PcInstance player){
		boolean showClanNotice = false;
		if(player.getClan()!=null){
			showClanNotice =  player.getClan().isNoticeEnabled();
		}
		
		
		if (showClanNotice)
		{
			NpcHtmlMessage notice = new NpcHtmlMessage(1);
			notice = comun.htmlMaker(0, player, "data/html/clanNotice.htm");
			notice.replace("%clan_name%", player.getClan().getName());
			notice.replace("%notice_text%", player.getClan().getNotice());
			notice.replace("%name%", player.getName());
			notice.disableValidation();
			player.sendPacket(notice);
		}
		if (Config.SERVER_NEWS && !showClanNotice)
		{
			NpcHtmlMessage serverNews = new NpcHtmlMessage(1);
			serverNews = comun.htmlMaker(0, player, "data/html/servnews.htm");
			serverNews.disableValidation();
			if (serverNews != null)
			{
				player.sendPacket(serverNews);
			}
		}
	}
	
	public static void showWindowsSecondaryPass(L2PcInstance player, String Pass1, String Pass2){
		CleanPlayerInformation(player);
		if(ZeuS.isActivePIN(player) && hasSecundaryPass(player)){
			return;
		}
		try{
			isOk.remove(player.getObjectId());
		}catch(Exception a){
			
		}
		if(!general.SECONDAY_PASSWORD){
			isOk.put(player.getObjectId(), true);
			ShowNotices(player);
			return;
		}
		if(general.SECONDARY_PASSWORD_NEED_EMAIL && !EmailRegistration.hasEmailRegister(player)){
			return;
		}
		paraPlayer(player, true);

		if(hasSecundaryPass(player)){
			ActionWait(player,false);
			showRegister(player,false);
		}else{
			if(Pass1.length()>0 || Pass2.length()>0){
				if(Pass1.equals(Pass2)){
					if(setSecundaryPass(player, Pass1)){
						central.msgbox_Lado(language.getInstance().getMsg(player).SP_WAS_BEEN_SET, player, "S. PASS");
						player.sendPacket(new sendC());
						ActionCorrectCreateSecunday(player);
						try{
							isOk.put(player.getObjectId(), true);
							ActionCorrectPassword(player);
						}catch(Exception a){
							
						}
						return;
					}
				}else{
					central.msgbox_Lado(language.getInstance().getMsg(player).SP_PASSWORD_NEED_TO_BE_EQUALS, player);
				}
			}
			if(general.SECONDARY_PASSWORD_NEED_EMAIL && !general.REGISTER_EMAIL_ONLINE && !EmailRegistration.hasEmailRegister(player)){
				EmailRegistration.getRegistrationWindos(player, true, false);
				return;
			}
			ActionWait(player,true);
			showRegister(player,true);
			ShowNotices(player);
		}
		
		
	}
	
	
	public static class SenderSPTimer implements Runnable{
		String Account = "", Email = "", Folder = "";
		public SenderSPTimer(L2PcInstance player, String _Account, String _Email){
			Account = _Account;
			Email = _Email;
			Folder = language.getInstance().getFolder(player);
		}
		@Override
		public void run(){
			try{
				if(FAIL_ENTRY_FOR_EMAIL!=null){
					if(FAIL_ENTRY_FOR_EMAIL.size()>0){
						if(FAIL_ENTRY_FOR_EMAIL.containsKey(Account)){
							jMail.sendFailEntrySecondaryPassword(Folder, FAIL_ENTRY_FOR_EMAIL.get(Account),Account,Email);
							FAIL_ENTRY_FOR_EMAIL.remove(Account);
						}
					}
				}
			}catch(Exception a){

			}

		}
	}

	

}
