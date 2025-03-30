package ZeuS.interfase;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import ZeuS.server.httpResp;
import ZeuS.tutorial.SecondaryPassword;

public class EmailRegistration {
	private static Map<Integer, String> charCode = new HashMap<Integer, String>();
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(EmailRegistration.class.getName());

	private static Vector<Integer> CheckingList = new Vector<Integer>();
	private static Map<Integer, Boolean> OnEmailWindow = new HashMap<Integer, Boolean>();
	private static Map<Integer, String> EmailForAccount = new HashMap<Integer, String>();
	private static Map<Integer, Boolean>IsChangeEmail = new HashMap<Integer, Boolean>();

	private static Map<Integer, Integer> IntentosDePlayer = new HashMap<Integer, Integer>();

	private static String getIDForEmailRegis(L2PcInstance player){
		Random aleatorio = new Random();
		int RandChar = 0;
		RandChar = aleatorio.nextInt(999999);
		charCode.put(player.getObjectId(), String.valueOf(RandChar));
		return String.valueOf(RandChar);
	}


	public static boolean hasEmailRegister(L2PcInstance player){
		boolean retorno = true;
		String EmailAccount = opera.getUserMail(player.getAccountName());
		if(EmailAccount==null){
			retorno = false;
		}else{
			if(EmailAccount.length()<=0){
				retorno = false;
			}
		}
		return retorno;
	}
	
	public static void baypass(L2PcInstance player, String Params){
		
		if(IsChangeEmail==null){
			IsChangeEmail.put(player.getObjectId(), false);
		}else if(!IsChangeEmail.containsKey(player.getObjectId())){
			IsChangeEmail.put(player.getObjectId(), false);
		}
		
		if(Params==null){
			getRegistrationWindos(player, true,IsChangeEmail.get(player.getObjectId()));
		}else{
			if(Params.split(" ").length==3){
				if(Params.split(" ")[0].equals("SEND_EMAIL_CODE")){
					String Email1 = Params.split(" ")[1], Email2 = Params.split(" ")[2];
					if(!Email1.equals(Email2)){
						getRegistrationWindos(player,true,IsChangeEmail.get(player.getObjectId()));
						central.msgbox(language.getInstance().getMsg(player).REGI_EMAIL_NOT_MATCH, player);
						return;
					}

					if(general.REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT){
						if(opera.emailHasBannedAccount(player, Email1)){
							opera.setBanToAccChar(player, "Register Email Check");
							return;
						}
					}

					if(httpResp.sendCodeForRegistration(Params.split(" ")[1], getIDForEmailRegis(player), player)){
						EmailForAccount.put(player.getObjectId(),Params.split(" ")[1]);
						getRegistrationWindos(player,false,IsChangeEmail.get(player.getObjectId()));
					}
				}else if(Params.split(" ")[0].equals("CHECK_EMAIL_CODE")){
					String CodeFromWeb = Params.split(" ")[1];
					if(charCode.get(player.getObjectId()).equals(CodeFromWeb)){
						if(opera.setUserEmailtoBD(player, EmailForAccount.get(player.getObjectId()))){
							EmailForAccount.remove(player.getObjectId());
							charCode.remove(player.getObjectId());
							opera.setimmobilizeChar(player, false);
							player._isChatBlock = false;
							central.msgbox(language.getInstance().getMsg(player).ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED, player);
							SecondaryPassword.showWindowsSecondaryPass( player,"","" );
						}
					}else{
						central.msgbox(language.getInstance().getMsg(player).RAAC_ENTER_WROG_CODE, player);
						getRegistrationWindos(player,false,IsChangeEmail.get(player.getObjectId()));
					}
				}
			}else{
				central.msgbox("Wrog Input Data", player);
				getRegistrationWindos(player, true,IsChangeEmail.get(player.getObjectId()));
			}
		}
	}

	public static void sendEmailToChar(L2PcInstance player, String Email){

	}

	public static void isRegisterUser(L2PcInstance player){

		if(!general.REGISTER_EMAIL_ONLINE){
			return;
		}

		if(player != null){
			if(!player.getClient().isDetached()){
				if(!hasEmailRegister(player)){
					OnEmailWindow.put(player.getObjectId(), false);
					if(CheckingList!=null){
						if(CheckingList.contains(player.getObjectId())){
							OnEmailWindow.put(player.getObjectId(), false);
							ThreadPoolManager.getInstance().scheduleGeneral(new CheckAutomatic(player), general.REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME * 1000);
							return;
						}
					}
					ThreadPoolManager.getInstance().scheduleGeneral(new CheckAutomatic(player), general.REGISTER_NEW_PLAYER_WAITING_TIME * 1000);
					CheckingList.add(player.getObjectId());
				}
			}
		}
	}
	

	public static void getRegistrationWindos(L2PcInstance player, boolean emailSeccion, boolean isChangeEmail){
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return;
		}		
		IsChangeEmail.put(player.getObjectId(), isChangeEmail);
		String ActualEmail = "";
		if(!isChangeEmail && hasEmailRegister(player)){
			central.msgbox(language.getInstance().getMsg(player).ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email.replace("$email", opera.getUserMail(player.getAccountName())),player);
			return;
		}else if(isChangeEmail && !hasEmailRegister(player)){
			central.msgbox("You dont have an email", player);
			return;
		}
		if(isChangeEmail){
			ActualEmail = opera.getUserMail(player.getAccountName());
		}
		
		NpcHtmlMessage html = null;
		//DDSConverter.sendImage(player, general.IDLOGOSERVER);
		String imageLogo = "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + general.ID_LOGO_SERVER + "\" width=256 height=64>";		
		
		if(emailSeccion){
			IntentosDePlayer.put(player.getObjectId(), 0);
			String cajaTexto = "<multiedit var=\"txtEmail\" width=260 height=24>";
			String cajaTexto2 = "<multiedit var=\"txtEmail2\" width=260 height=24>";
			String ByPassAceptar = "\"bypass -h voice .RegisEmailCMB SEND_EMAIL_CODE $txtEmail $txtEmail2\"";
			html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +(isChangeEmail ? "emailRegistrationWindows_change.html" : "emailRegistrationWindows.html"));
			html.replace("%SERVER_LOGO%", imageLogo);
			html.replace("%EMAIL_1%", cajaTexto);
			html.replace("%EMAIL_2%", cajaTexto2);
			html.replace("%BYPASS_SEND%", ByPassAceptar);
			if(ActualEmail.length()>0){
				html.replace("%USING_EMAIL%", ActualEmail);
			}
			central.sendHtml(player, html.getHtml(), false);
		}else{
			IntentosDePlayer.put(player.getObjectId(), IntentosDePlayer.get(player.getObjectId()) + 1);
			if(IntentosDePlayer.get(player.getObjectId()) <= general.REGISTER_NEW_PlAYER_TRIES){
				String cajaClave = "<edit type=\"text\" var=\"txtEmailPass\" width=260>";
				String ByPassAceptar = "\"bypass -h voice .RegisEmailCMB CHECK_EMAIL_CODE $txtEmailPass 0\"";
				html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"emailRegistrationWindows-2.html");
				html.replace("%SERVER_LOGO%", imageLogo);
				html.replace("%VALIDATION_CODE%", cajaClave);
				html.replace("%BYPASS_SEND%", ByPassAceptar);
				central.sendHtml(player, html.getHtml(), false);
			}else{
				IntentosDePlayer.put(player.getObjectId(), 0);
				try{
					EmailForAccount.remove(player.getObjectId());
				}catch(Exception a){

				}
				try{
					charCode.remove(player.getObjectId());
				}catch(Exception a){

				}
				player.getClient().closeNow();
				return;
			}
		}
	}

	public static class CheckAutomatic implements Runnable{
		L2PcInstance activeChar;
		public CheckAutomatic(L2PcInstance player){
			activeChar = player;
		}
		@Override
		public void run(){
			if(!OnEmailWindow.get(activeChar.getObjectId())){
				if(general.REGISTER_EMAIL_ONLINE){
					getRegistrationWindos(activeChar, true, true);
					opera.setimmobilizeChar(activeChar, true);
					if(general.REGISTER_NEW_PLAYER_BLOCK_CHAT){
						activeChar._isChatBlock = true;
					}
				}
			}
		}
	}

}
