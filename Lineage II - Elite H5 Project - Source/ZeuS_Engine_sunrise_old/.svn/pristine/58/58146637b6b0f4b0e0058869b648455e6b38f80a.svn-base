package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.Config.premiumPersonalData;
import ZeuS.Config.premiumsystem;
import ZeuS.ZeuS.ZeuS;
import ZeuS.dressme.dressme;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.borrowAccount;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.itemLink;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;
import ZeuS.server.httpResp;
import ZeuS.tutorial.SecondaryPassword;

public class C_gmcommand {

	private static Map<Integer, Integer> SELECTED_PREMIUM = new HashMap<Integer, Integer>();
	private static Logger _log = Logger.getLogger(C_gmcommand.class.getName());
	
	private static Map<Integer, HashMap<Integer, Long>> ITEM_GATHERING = new HashMap<Integer, HashMap<Integer, Long>>();
	private static Map<Integer, HashMap<Integer, Long>> ITEM_EMAILS = new HashMap<Integer, HashMap<Integer, Long>>();	
	private static Map<Integer, SurveyClass> SURVEYS = new HashMap<Integer, SurveyClass>();
	private static Map<Integer, _gmTempData> TEMP_DATA = new HashMap<Integer, _gmTempData>();
	
	private static Location GmLocation = null;
	
	public static void ConcludeSurvey(L2PcInstance player){
		SURVEYS.get(player.getObjectId()).endSurvey(true);
	}
	public static void changeSurveyStatus(L2PcInstance player){
		SURVEYS.get(player.getObjectId()).chanceStatus();
	}
	
	public static void showSurveyWindows(L2PcInstance player, int idGmSurvey){
		if(SURVEYS!=null){
			if(SURVEYS.containsKey(idGmSurvey)){
				SURVEYS.get(idGmSurvey).sendWindowsSurveyToPlayer(player);
				return;
			}
		}
	}
	
	public static void setSurveyQuestion(L2PcInstance player, String Question){
		if(SURVEYS==null){
			SurveyClass tempSur = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), tempSur);
		}else if(SURVEYS.size()==0){
			SurveyClass tempSur = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), tempSur);			
		}else if(!SURVEYS.containsKey(player.getObjectId())){
			SurveyClass tempSur = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), tempSur);			
		}
		SURVEYS.get(player.getObjectId()).setQuestion(Question);
	}
	
	public static void removeSurveyAnswer(L2PcInstance player, int idAnswer){
		if(SURVEYS!=null){
			if(SURVEYS.containsKey(player.getObjectId())){
				SURVEYS.get(player.getObjectId()).removeAnwer(idAnswer);				
			}
		}
	}
	
	public static void setSurveyAnswer(L2PcInstance player, String Answer){
		if(SURVEYS==null){
			SurveyClass tempSur = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), tempSur);
		}else if(SURVEYS.size()==0){
			SurveyClass tempSur = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), tempSur);			
		}else if(!SURVEYS.containsKey(player.getObjectId())){
			SurveyClass tempSur = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), tempSur);			
		}
		SURVEYS.get(player.getObjectId()).setAnswer(Answer);
	}
	
	public static void setSurveyAnswerPlayer(L2PcInstance player, String AnswerPlayer, int idGmSurvey){
		if(SURVEYS!=null){
			if(SURVEYS.containsKey(idGmSurvey)){
				SURVEYS.get(idGmSurvey).setAnswer(player, Integer.valueOf(AnswerPlayer));
			}
		}
	}	
	
	public static void showSurveyAnnoucement(L2PcInstance player){
		if(SURVEYS!=null){
			if(SURVEYS.containsKey(player.getObjectId())){
				if(!SURVEYS.get(player.getObjectId()).canAnnoucement()){
					central.msgbox("Annoucement was cancel. Please Check your Survey Config", player);
					return;
				}
				int idSurvey = itemLink.getIdForSurvey(player);
				String idAnnounce = "[SURVEY="+ String.valueOf(idSurvey) +"]";
				String forAnnoucement = SURVEYS.get(player.getObjectId()).getQuestion();
				forAnnoucement += " " + idAnnounce;
				opera.AnunciarTodos("SURVEY", ZeuS.getAnnouncement(forAnnoucement, null)); 
			}
		}
	}
	
	public static void SendMeToGmConfirm(L2PcInstance player){
		if(GmLocation!=null){
			player.teleToLocation(GmLocation, 300);
		}
	}
	
	private static void sendConfirmButton(L2PcInstance player){
		int Contador = 0;
		if(!general.isGmSummonWisp(player)){
			central.msgbox("We Send 0 invitations", player);			
			return;
		}
		GmLocation = player.getLocation();
		for(L2PcInstance players : opera.getAllPlayerOnWorld(false)){
			if(!players.isInCombat()){
				if(player != players){
					Dlg.sendDlg(players, language.getInstance().getMsg(players).ADMIN_GMPANEL_GATHERING_RECALL_YOU , IdDialog.ENGINE_GM_PANEL, 60);
					Contador++;
				}
			}
		}
		central.msgbox("We Send " + String.valueOf(Contador) + " invitations", player);
	}
	
	private static String getSurveyWindows(L2PcInstance player){
		//TODO: SURVEY WINDOWS
		//cbManager.send1002(player, Mensaje , " ", "0");
		
		if(SURVEYS==null){
			SurveyClass T1 = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), T1);
		}else if(SURVEYS.size()==0){
			SurveyClass T1 = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), T1);			
		}else if(!SURVEYS.containsKey(player.getObjectId())){
			SurveyClass T1 = new SurveyClass(player);
			SURVEYS.put(player.getObjectId(), T1);			
		}
		
		String ByPassSaveQuestion = "Write Z_SURVEY_SET_QUESTION Set Z txtQuestion txtQuestion txtQuestion";
		String ByPassSaveAnswer = "Write Z_SURVEY_SET_ANSWER Set Z txtAnswerNew txtAnswerNew txtAnswerNew";
		String ByPassSendSurvey = "bypass ZeuS surveys sendSurvey";
		String ByPassCancelSurvey = "bypass ZeuS surveys CancelSurvey";
		String ByPassActiveSurvey = "bypass ZeuS surveys ActiveSurvey";
		String ByPassConcludeSurvey = "bypass ZeuS surveys ConcludeSurvey";
				
		String Retorno = "<center><table width=760>";
		
		String SurveyAnswer = "";
		String YourSurveyQuestion = "";
		
		if(SURVEYS!=null){
			if(SURVEYS.containsKey(player.getObjectId())){
				SurveyAnswer += SURVEYS.get(player.getObjectId()).getSurveyAnswers();
				YourSurveyQuestion = SURVEYS.get(player.getObjectId()).getQuestion();
			}
		}
		
		boolean isSurveyActive = SURVEYS.get(player.getObjectId()).isActive();
		
		String BtnFormal = "<table><tr><td width=152>"+
		"<button value=\"Send Announcement\" action=\""+ ByPassSendSurvey +"\" width=152 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>"+
		"<td width=152>"+
		
		(isSurveyActive ? "<button value=\"Disabled Survey\" action=\""+ ByPassCancelSurvey +"\" width=152 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">" : "<button value=\"Enabled Survey\" action=\""+ ByPassActiveSurvey +"\" width=152 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">") +

		
		"</td><td width=152>"+
		
		"<button value=\"Conclude Survey\" action=\""+ ByPassConcludeSurvey +"\" width=152 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\">" +
		
		"</td></tr></table>";
		
		
		Retorno += "<tr><td width=740 align=center><img src=\"L2UI.SquareGray\" width=740 height=2><table fixwidth=740 background=L2UI_CT1.Windows_DF_TooltipBG border=0 cellspacing=1 cellpadding=5><tr><td fixwidth=740 align=center>"+
			"<font name=hs12 color=B4CBFA>Survet Question</font></td></tr><tr><td fixwidth=740 align=center>"+
			"<MultiEdit var =\"txtQuestion\" width=680 height=42></td></tr><tr><td fixwidth=740 align=center><button value=\"Save Question\" action=\""+ ByPassSaveQuestion +"\" width=152 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table><img src=\"L2UI.SquareGray\" width=740 height=2></td></tr>";

		Retorno += "<tr><td width = 760 align=CENTER><br><table fixwidth=760 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=40 cellspacing=1 cellpadding=5><tr><td fixwidth=760 align=\"CENTER\">"+
			"<font name=hs12 color=B4CBFA>Your Survey Question ( "+ (isSurveyActive ? "<font color=A9F5BC>Active</font>" :  "<font color=F79F81>Disabled</font>") +" )</font></td></tr><tr><td fixwidth=760 align=\"CENTER\">"+
            "<font name=hs12 color=DF7401>"+ YourSurveyQuestion +"</font></td></tr><tr><td fixwidth=760 align=\"CENTER\">"+
            BtnFormal + "</td></tr></table></td></tr>";
		
		Retorno += SurveyAnswer;
		

		
		Retorno += "<tr><td width = 765 align=CENTER><br1><table fixwidth=760 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=40 cellspacing=1 cellpadding=5><tr><td fixwidth=290>"+
		"<font color=DF7401>Set New Answer</font></td><td fixwidth=400><edit var=\"txtAnswerNew\" width=350></td>"+
		"<td fixwidth=50><button value=\"Add\" action=\""+ ByPassSaveAnswer +"\" width=52 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table></td></tr>";
		
		Retorno += "</table></center>";
		return Retorno;
	} 
	
	public static void sendCode(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}
		
		if(httpResp.sendCodeForChangePassword( Data.getEmail() , Data.getCode(), Data.getPlayer())){
			central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_SEND_YOU_THE_CODE_TO_YOUR_EMAIL.replace("$email", Data.getEmail()) , Data.getPlayer());
		}		
	}
	
	public static void resetSecondaryPass(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}		
		central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_SECONDARY_PASSWORD_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER  , Data.getPlayer());
		if(SecondaryPassword.removeSecondaryPassword(Data.getPlayer())){
			central.msgbox("The target secondary password was reset", player);
			Data.getPlayer().getClient().closeNow();
		}else{
			central.msgbox("Fail to reset the secondary password.", player);
		}		
	}
	
	public static void resetEmail(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}		
		if(!opera.setUserEmailtoBD(Data.getPlayer(), "")){
			central.msgbox("We have a problem with the Email Reset on the account player", player);
			return;					
		}else{
			central.msgbox("Reset Account Email Complete", player);
			central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_ACCOUNT_EMAIL_RESET, Data.getPlayer());
		}
	}
	
	public static void setAioChar(L2PcInstance player, boolean is_0_Skill){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}			
		if(aioChar.setNewAIO(Data.getPlayer(), is_0_Skill, player)){
			
		}
	}
	
	public static void setResetOlySchemeBuff(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}
		
		C_oly_buff.removeAllScheme(Data.getPlayer());
		cbFormato.cerrarCB(Data.getPlayer());
		central.msgbox("Reset Oly Scheme from target Done!", player);
		central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_OLY_SCHEME_REMOVED, Data.getPlayer());		
	}
	
	public static void setResetBuffSchemeConfig(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}
				
		central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_BUFF_SCHEME_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER , Data.getPlayer());
		v_Buffer_New.removeAllFromPlayer(Data.getPlayer());
		Data.getPlayer().getClient().closeNow();
		central.msgbox("Target buff scheme removed", player);		
	}
	
	public static void setResetCharConfig(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}
		central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_PERSONAL_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER, Data.getPlayer());
		removeCharConfig(Data.getPlayer().getObjectId());
		Data.getPlayer().getClient().closeNow();
		central.msgbox("Target Personal Setting removed", player);	
	}
	
	public static void setResetDressmeSetting(L2PcInstance player){
		_gmTempData Data = TEMP_DATA.get(player.getObjectId());
		if(Data == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(Data.getPlayer() == null){
			central.msgbox("Target data is Empty. Please Target the player and send the code Again", player);
			return;
		}else if(!Data.getPlayer().isOnline()){
			central.msgbox("Target is Offline. Please Target the player and send the code Again", player);
			return;
		}
		central.msgbox(language.getInstance().getMsg(Data.getPlayer()).ADMIN_DRESSME_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER, Data.getPlayer());
		removeCharDressme(Data.getPlayer().getObjectId());
		Data.getPlayer().getClient().closeNow();
		central.msgbox("Target Dressme Setting removed", player);				
	}
	
	public static void otherPanel(L2PcInstance player, String Command, String Value){
		if(TEMP_DATA==null){
			_gmTempData temp = new _gmTempData();
			TEMP_DATA.put(player.getObjectId(), temp);
		}else if(!TEMP_DATA.containsKey(player.getObjectId())){
			_gmTempData temp = new _gmTempData();
			TEMP_DATA.put(player.getObjectId(), temp);			
		}
		L2Object TOPlayer = player.getTarget();
		
		if(!(TOPlayer instanceof L2PcInstance)){
			central.msgbox("You need to target a player", player);
			return;
		}
		L2PcInstance TargetPlayer = (L2PcInstance)TOPlayer;
		
		if(player == TargetPlayer){
			central.msgbox("You need to select another player, not you", player);
			return;
		}
		
		String EmailT = opera.getUserMail(TargetPlayer.getAccountName());
		String msgje = "";
		boolean haveEmailTarget = EmailT == null ? false : ( EmailT.length() == 0 ? false : true ); 
		switch(Command){
			case "sendBorrowPass":
				//setNewBorrowPassword(L2PcInstance playerGM, String PasswordLoan, L2PcInstance TargetPlayer) {
				borrowAccount.getInstance().setNewBorrowPassword(player, Value, TargetPlayer);
				break;
			case "surveys":
				
				break;
			case "sendCodeASK":
				if(!haveEmailTarget){
					central.msgbox("The target no have an Email.", player);
					return;
				}else if(Value.length()==0){
					central.msgbox("You need to put a Code for Send by Email", player);
				}
				
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, Value);
				String msje = "You will send this code " + Value + " to " + TargetPlayer.getName() + " email " + EmailT;
				Dlg.sendDlg(player, msje, IdDialog.ENGINE_GMCOMMAND_SEND_CODE);
				if(httpResp.sendCodeForChangePassword( EmailT , Value, player)){
					central.msgbox(language.getInstance().getMsg(TargetPlayer).ADMIN_SEND_YOU_THE_CODE_TO_YOUR_EMAIL.replace("$email",EmailT) , TargetPlayer);
				}
				break;
			case "ResetSecondPassASK":
				if(!SecondaryPassword.haveSP(TargetPlayer.getObjectId())){
					central.msgbox("The target player no have Secondary Password", player);
					return;
				}
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will Reset the Secondary Password from " + TargetPlayer.getName() + ", do you wish to continue?";				
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_RESET_SECONDARY_PASS);				
				break;
			case "ResetEmailASK":
				if(!haveEmailTarget){
					central.msgbox("The target no have an Email.", player);
					return;					
				}
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will Reset the Email from " + TargetPlayer.getName() + ", do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_RESET_EMAIL);
				break;
			case "SetAioASK":
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will create the target player ("+ TargetPlayer.getName() +") into AIO Char Without Enchant Skills, do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_CREATE_AIO_CHAR);
				break;
			case "SetAio30ASK":
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will create the target player ("+ TargetPlayer.getName() +") into AIO Char With Enchant Skills +15/+30, do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_CREATE_AIO_CHAR_30);
				break;
			case "ResetOlyBuffASK":
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will Reset all the Olympiad Buff Scheme from " + TargetPlayer.getName() + ", do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_RESET_OLY_SCHEME);
				break;
			case "ResetSchBuffASK":
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will Reset all the Buff Scheme from " + TargetPlayer.getName() + ", do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_RESET_BUFF_SCHEME);
				break;
			case "ResetCharCoASK":
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will Reset all the Char Config from " + TargetPlayer.getName() + ", do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_RESET_CHAR_CONFIG);			
				break;
			case "ResetCharDrASK":
				TEMP_DATA.get(player.getObjectId()).set(TargetPlayer, EmailT, "");
				msgje = "You will Reset all the Dressme Setting from " + TargetPlayer.getName() + ", do you wish to continue?";
				Dlg.sendDlg(player, msgje, IdDialog.ENGINE_GMCOMMAND_RESET_CHAR_DRESSME);
				break;
		}
	}
	
	private static void removeCharConfig(int idChar){
		String consulta = "delete from zeus_char_config where idchar=?";
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(consulta);
			ins.setInt(1,idChar);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}			
	}
	
	private static void removeCharDressme(int idChar){
		String consulta = "delete from zeus_dressme where idChar=?";
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(consulta);
			ins.setInt(1,idChar);
			try{
				ins.executeUpdate();
				dressme.getInstance().removeDressmeFromPlayer(idChar);
			}catch(SQLException e){

			}
		}catch(Exception a){
			
		}			
	}	
	
	private static String BoxShow(String Titulos, String Explica, String ByPass){
		return "<tr><td width = 760 align=CENTER><br><table fixwidth=760 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=40 cellspacing=1 cellpadding=5><tr><td fixwidth=280>"+
		"<font name=hs12 color=DF7401>"+ Titulos + "</font></td><td fixwidth=400>"+
		"<font color=CACACA>"+ Explica +"</font></td><td fixwidth=50>"+
		"<button value=\"Do It\" action=\""+ ByPass +"\" width=52 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table></td></tr>";
	}
	
	private static String gerMainWindowOther(L2PcInstance player){
		String ByPassSendCode = "bypass -h ZeuS adminPanel sendCodeASK $txtCode";
		String ByPassResetSecondaryPassword = "bypass -h ZeuS adminPanel ResetSecondPassASK";
		String ByPassResetTargetEmail = "bypass -h ZeuS adminPanel ResetEmailASK";
		String ByPassSetAio = "bypass -h ZeuS adminPanel SetAioASK";
		String ByPassSetAio30 = "bypass -h ZeuS adminPanel SetAio30ASK";
		String ByPassResetOlyBuff = "bypass -h ZeuS adminPanel ResetOlyBuffASK";
		String ByPassResetSchemeBuffer = "bypass -h ZeuS adminPanel ResetSchBuffASK";
		String ByPassResetAllCharConfig = "bypass -h ZeuS adminPanel ResetCharCoASK";
		String ByPassResetAllCharDressme = "bypass -h ZeuS adminPanel ResetCharDrASK";
		String ByPassSendSurveys = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;SURVEY;0;0;0;0;0";
		String ByPassGmShopRemove = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;SHOPREMOVE;0;0;0;0;0";
		String ByPassSendPasswordToCharADMIN = "bypass -h ZeuS adminPanel sendBorrowPass $txtPassB";
		
		String Retorno = "<center><table width=760>";
		
		Retorno += BoxShow("Send Code To Target Email", "<table fixwidth=400><tr><td fixwidth=150><font color=CACACA>Input the Code to Send</font></td><td fixwidth=250><edit var=\"txtCode\" width=100></td></tr></table>", ByPassSendCode);
		Retorno += BoxShow("Set Temporaly Password", "<table fixwidth=400><tr><td fixwidth=150><font color=CACACA>Input a Temporaly Password to Enter</font></td><td fixwidth=250><edit var=\"txtPassB\" width=100></td></tr></table>", ByPassSendPasswordToCharADMIN);		
		Retorno += BoxShow("Reset Target Secondary Password", "You need to target the ppl who need to reset the Secondary Password", ByPassResetSecondaryPassword);
		Retorno += BoxShow("Reset Target Email", "You need to target the ppl who need to reset the Email", ByPassResetTargetEmail);
		Retorno += BoxShow("Set AIO", "You need to target the ppl who want be to AIO Buff Char", ByPassSetAio);
		Retorno += BoxShow("Set AIO +30", "You need to target the ppl who want be to AIO Buff +30 Char", ByPassSetAio30);
		Retorno += BoxShow("Reset Oly Buff", "You need to target the ppl who want Reset his Oly Scheme", ByPassResetOlyBuff);
		Retorno += BoxShow("Reset Scheme Buffer", "You need to target the ppl who want Reset his Scheme Buff", ByPassResetSchemeBuffer);
		Retorno += BoxShow("Reset Char Config", "You need to target the ppl who want Reset All Char Config", ByPassResetAllCharConfig);
		Retorno += BoxShow("Reset Char Dressme", "You need to target the ppl who want Reset All Dressme", ByPassResetAllCharDressme);
		Retorno += BoxShow("Surveys", "You can Create Survey and send it to all Char Online", ByPassSendSurveys);
		
		//FIX IT Retorno += BoxShow("Remove Item From Shop", "You can remove Item from the Target", ByPassGmShopRemove);
		
		Retorno += "</center></table>";
		
		return Retorno;
		
	}
	
	@SuppressWarnings("rawtypes")
	private static String getRewardWindowsEmail(L2PcInstance player){
		Map<Integer, Long> Items = null;
		String Retorno = "";
		
		String ByPassRemoveItem = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";REMOVE_ITEM_EMAIL;%IDITEM%;0;0;0;0";
		String Grilla = "<table width=300 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
			cbFormato.getBotonForm("%ICON%", ByPassRemoveItem) + "<br></td><td fixwidth=268 align=CENTER>"+
			"<font color=LEVEL>(%AMOUNT%)</font> %NAME%</td></tr></table>";
		
		try{
			Items = ITEM_EMAILS.get(player.getObjectId());
		}catch(Exception a){
			return "";
		}
		
		if(Items==null){
			return "";
		}
		
		Iterator itr = Items.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idItem = (int)Entrada.getKey();
			long Amount = (long)Entrada.getValue();
			String IconImagen = opera.getIconImgFromItem(idItem, true);
			String NombreItem = central.getNombreITEMbyID(idItem);
			Retorno += Grilla.replace("%IDITEM%",String.valueOf(idItem)).replace("%NAME%", NombreItem) .replace("%AMOUNT%", opera.getFormatNumbers(Amount)) .replace("%ICON%", IconImagen);
		}
		
		return Retorno;		
	}
	
	@SuppressWarnings("rawtypes")
	private static String getItemFromEmail(L2PcInstance player){
		Map<Integer, Long> Items = null;
		try{
			Items = ITEM_EMAILS.get(player.getObjectId());
		}catch(Exception a){
			return "";
		}
		
		if(Items==null){
			return "";
		}
		
		Iterator itr = Items.entrySet().iterator();
		String Retorno = "";
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			if(Retorno.length()>0){
				Retorno+=";";
			}
			Retorno += String.valueOf((int) Entrada.getKey() ) + "," + String.valueOf((long) Entrada.getValue() );
		}
		
		return Retorno;
	}
	
	
	private static void removeItemToEmail(L2PcInstance player, int idItem){
		try{
			ITEM_EMAILS.get(player.getObjectId()).remove(idItem);
		}catch(Exception a){
			
		}
	}
	
	private static void setNewItemToEmail(L2PcInstance player, int idItem, long Amount){
		if(ITEM_EMAILS==null){
			ITEM_EMAILS.put(player.getObjectId(), new HashMap<Integer, Long>());
		}else if(!ITEM_EMAILS.containsKey(player.getObjectId())){
			ITEM_EMAILS.put(player.getObjectId(), new HashMap<Integer, Long>());
		}
		if(ITEM_EMAILS.get(player.getObjectId()).size()>0){
			
			if(ITEM_EMAILS.get(player.getObjectId()).size()>=8){
				central.msgbox("You cant not add more Item to Email.", player);
				return;
			}
			
			if(ITEM_EMAILS.get(player.getObjectId()).containsKey(idItem)){
				long CC = ITEM_EMAILS.get(player.getObjectId()).get(idItem);
				ITEM_EMAILS.get(player.getObjectId()).remove(idItem);
				ITEM_EMAILS.get(player.getObjectId()).put(idItem, Amount + CC);
			}else{
				ITEM_EMAILS.get(player.getObjectId()).put(idItem, Amount);
			}
		}else{
			ITEM_EMAILS.get(player.getObjectId()).put(idItem, Amount);
		}
	}	
	
	@SuppressWarnings("rawtypes")
	private static String getRewardWindowsGathering(L2PcInstance player){
		Map<Integer, Long> Items = null;
		String Retorno = "";
		
		String ByPassRemoveItem = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";REMOVE_ITEM;%IDITEM%;0;0;0;0";
		String Grilla = "<table width=300 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
			cbFormato.getBotonForm("%ICON%", ByPassRemoveItem) + "<br></td><td fixwidth=268 align=CENTER>"+
			"<font color=LEVEL>(%AMOUNT%)</font> %NAME%</td></tr></table>";
		
		try{
			Items = ITEM_GATHERING.get(player.getObjectId());
		}catch(Exception a){
			return "";
		}
		
		if(Items==null){
			return "";
		}
		
		Iterator itr = Items.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idItem = (int)Entrada.getKey();
			long Amount = (long)Entrada.getValue();
			String IconImagen = opera.getIconImgFromItem(idItem, true);
			String NombreItem = central.getNombreITEMbyID(idItem);
			Retorno += Grilla.replace("%IDITEM%",String.valueOf(idItem)).replace("%NAME%", NombreItem) .replace("%AMOUNT%", opera.getFormatNumbers(Amount)) .replace("%ICON%", IconImagen);
		}
		
		return Retorno;		
	}
	
	@SuppressWarnings("rawtypes")
	private static String getItemFromGathering(L2PcInstance player){
		Map<Integer, Long> Items = null;
		try{
			Items = ITEM_GATHERING.get(player.getObjectId());
		}catch(Exception a){
			return "";
		}
		Iterator itr = Items.entrySet().iterator();
		String Retorno = "";
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			if(Retorno.length()>0){
				Retorno+=";";
			}
			Retorno += String.valueOf((int) Entrada.getKey() ) + "," + String.valueOf((long) Entrada.getValue() );
		}
		
		return Retorno;
	}
	
	
	private static void removeItemToGathering(L2PcInstance player, int idItem){
		try{
			ITEM_GATHERING.get(player.getObjectId()).remove(idItem);
		}catch(Exception a){
			
		}
	}
	
	private static void setNewItemToGathering(L2PcInstance player, int idItem, long Amount){
		if(ITEM_GATHERING==null){
			ITEM_GATHERING.put(player.getObjectId(), new HashMap<Integer, Long>());
		}else if(!ITEM_GATHERING.containsKey(player.getObjectId())){
			ITEM_GATHERING.put(player.getObjectId(), new HashMap<Integer, Long>());
		}
		if(ITEM_GATHERING.get(player.getObjectId()).size()>0){
			if(ITEM_GATHERING.get(player.getObjectId()).containsKey(idItem)){
				long CC = ITEM_GATHERING.get(player.getObjectId()).get(idItem);
				ITEM_GATHERING.get(player.getObjectId()).remove(idItem);
				ITEM_GATHERING.get(player.getObjectId()).put(idItem, Amount + CC);
			}else{
				ITEM_GATHERING.get(player.getObjectId()).put(idItem, Amount);
			}
		}else{
			ITEM_GATHERING.get(player.getObjectId()).put(idItem, Amount);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static String getColPremium(){
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";PREMIUM_SEE_GM;%IDPRE%;0;0;0;0;0";
		String ByPassSelect = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";SELECTED_PREMIUM;%IDPRE%;0;0;0;0;0";
		String grilla = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
				cbFormato.getBotonForm("%IMAGEN%", ByPassSelect) + "<br><br></td><td fixwidth=196>"+
		        "<font color=819FF7>%NAME% - </font><font color=81BEF7>%DAYS% days</font><br1>"+
		        "<font color=F7BE81>- Applicable to: </font><font color=F5DA81>%APPLI%</font><br>"+
		        "</td><td fixwidth=52 align=CENTER>"+
		        "<br><br><button action=\""+ByPass+"\" width=16 height=16 back=L2UI_CT1.PostWnd_DF_Btn_List fore=L2UI_CT1.PostWnd_DF_Btn_List></td></tr></table><img src=\"L2UI.SquareGray\" width=\"280\" height=2>";
		String Retorno = "";
		Iterator itr = general.getPremiumServices().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idPremium = (int) Entrada.getKey();
			premiumsystem PremiumInfo = (premiumsystem) Entrada.getValue();
			if(!PremiumInfo.isEnabled()){
				continue;
			}
			Retorno += grilla.replace("%IDPRE%", String.valueOf(idPremium)).replace("%APPLI%", PremiumInfo.getAplicableA()) .replace("%DC%", String.valueOf(PremiumInfo.getCost())) .replace("%DAYS%", String.valueOf(PremiumInfo.getDays())) .replace("%NAME%", PremiumInfo.getName()) .replace("%IMAGEN%", PremiumInfo.getIcono());
		}
		return Retorno;
	}
	
	private static String getMainMenu(L2PcInstance player, String parte){
		String retorno ="";
		String MenuCol = "";
		String CenterCol = "";
		
		int idShowPremium = -1;
		
		try{
			idShowPremium = SELECTED_PREMIUM.get(player.getObjectId());
		}catch(Exception a){
			idShowPremium = -1;
		}
		
		premiumsystem T1 = null;
		
		String PremiumInfoStr = "No have";
		
		if(idShowPremium>=0){
			T1 = general.getPremiumServices().get(idShowPremium);
			if(T1!=null){
				if(T1.isEnabled()){
					PremiumInfoStr = "<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=32 align=CENTER>"+
							"<img src=\""+ T1.getIcono() +"\" width=32 height=32><br><br></td><td fixwidth=196>"+
					        "<font color=819FF7>"+ T1.getName() +" - </font><font color=81BEF7>"+ String.valueOf(T1.getDays()) +" days</font><br1>"+
					        "<font color=F7BE81>- Applicable to: </font><font color=F5DA81>"+ T1.getAplicableA() +"</font><br>"+
					        "</td></tr></table><br>";
				}
			}
		}
		
		switch(parte){
			case "PREMIUM":
				String ByPassTarget = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";PREMIUM_GIVE_TARGET;"+ String.valueOf(idShowPremium) +";0;0;0;0;0";
				String ByPassZone = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";PREMIUM_GIVE_ZONE;"+ String.valueOf(idShowPremium) +";0;0;0;0;0";
				String ByPassZoneDC = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";PREMIUM_GIVE_ZONE_CHECK_DUALBOX;"+ String.valueOf(idShowPremium) +";0;0;0;0;0";
				String ByPassToAll = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";PREMIUM_GIVE_ALL_WORLD;"+ String.valueOf(idShowPremium) +";0;0;0;0;0";
				String ByPassToAllDC = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";PREMIUM_GIVE_ALL_WORLD_CHECK_DUALBOX;"+ String.valueOf(idShowPremium) +";0;0;0;0;0";
				MenuCol = getColPremium();
				CenterCol = "<table width=482 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=482 align=CENTER><font name=HS12>Selected Premium</font><br></td></tr><tr><td fixwidth=482 align=CENTER>"+
						PremiumInfoStr+
                        "</td></tr></table><br>"+
                        "<button value=\"Target\" action=\""+ ByPassTarget +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                        "<button value=\"This Zone\" action=\""+ ByPassZone +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                        "<button value=\"This Zone (D. Check)\" action=\""+ ByPassZoneDC +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                        "<button value=\"To All\" action=\""+ ByPassToAll +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                        "<button value=\"To All (Dual Check)\" action=\""+ ByPassToAllDC +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>";
				retorno = "<center><table width=762 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=48>"+
				        "<tr><td fixwidth=280>"+ MenuCol +"</td><td fixwidth=482 align=center>"+ CenterCol + "</td></tr></table></center>";				
				break;
			case "GATHERING":
				String ByPassGatheringWisp = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";CHANGE_WISP;0;0;0;0;0;0";
				String ByPassGatheringSendConfirm = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";SEND_CONFIRM;0;0;0;0;0;0";
				String ByPassAddItem = "bypass -h ZeuS zeusgmpanel AddItem $txtIdItem $txtAmount";

				Boolean isSummonByWisp = general.isGmSummonWisp(player);
				
				String btnSendConfirmaButton = "<button value=\"Send Confirm Window\" action=\""+ ByPassGatheringSendConfirm +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>";
				
				retorno = "<center><font name=HS12 color=F7D358>Gathering Event Option</font><br><table width=762 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=482 align=CENTER>"+
                "<font name=HS12>Auto recall player by Wisp</font><br>"+
                "<button value=\""+ ( isSummonByWisp ? "Is Active" : "Is Deactivated" ) +"\" action=\""+ ByPassGatheringWisp +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+ btnSendConfirmaButton +"</td></tr></table><br>";
				
				String ByPassThisZone = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";GATHERING_GIVE_ZONE;0;0;0;0;0;0";
				String ByPassThisZoneDC = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";GATHERING_GIVE_ZONE_DUALCHECK;0;0;0;0;0;0";
				String ByPassToAll_Player = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";GATHERING_GIVE_ALL;0;0;0;0;0;0";
				String ByPassToAllDC_Player = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";GATHERING_GIVE_ALL_DUALCHECK;0;0;0;0;0;0";
				
				retorno += "<table width=762 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=CENTER><table width=381 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=CENTER>"+
					"<font name=HS12>Reward's</font></td></tr><tr><td width=381 align=center><table width=381><tr>"+
					"<td fixwidth=190 align=CENTER>ID Item<br1><edit type=number var=\"txtIdItem\" width=120></td>"+
					"<td fixwidth=190 align=CENTER>Amount<br1><edit type=number var=\"txtAmount\" width=120></td></tr></table><br>"+
					"<button value=\"Add\" action=\""+ ByPassAddItem + "\" width=120 height=28 back=L2UI_CT1.Button_DF_Large fore=L2UI_CT1.Button_DF_Large></td></tr><tr>"+
					"<td width=381 align=CENTER>"+
					getRewardWindowsGathering(player) +"<br></td></tr></table><br></td><td fixwidth=381 align=CENTER><table width=381 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=CENTER>"+
                    "<button value=\"This Zone\" action=\""+ ByPassThisZone +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                    "<button value=\"This Zone (D. Check)\" action=\""+ ByPassThisZoneDC +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                    "<button value=\"To All\" action=\""+ ByPassToAll_Player +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                    "<button value=\"To All (Dual Check)\" action=\""+ ByPassToAllDC_Player +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm></td></tr></table><br></td></tr></table><br>";
				
				break;
			case "EMAILSYSTEM":
				String ByPassSendAddItem = "bypass -h ZeuS zeusgmpanel AddItemMail $txtIdItem $txtAmount";
				String ByPassSendThisZone = "Write Z_SEND_EMAIL Set Z cmbType txtTitle txtContent";
				String ByPassSendThisZone_DC = "Write Z_SEND_EMAIL Set Z_DC cmbType txtTitle txtContent";
				String ByPassSendThisTOALL = "Write Z_SEND_EMAIL Set A cmbType txtTitle txtContent";
				String ByPassSendThisTOALL_DC = "Write Z_SEND_EMAIL Set A_DC cmbType txtTitle txtContent";
				String ByPassSendThisTOALL_I_OFFLINE = "Write Z_SEND_EMAIL Set ALL_I_OFF cmbType txtTitle txtContent";
				retorno = "<center><font name=HS12 color=F7D358>Email System</font><br><table width=762 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=LEFT><font name=HS12>Title</font><br></td><td fixwidth=381 align=LEFT><font name=HS12>Message</font><br></td></tr><tr><td fixwidth=381 align=LEFT>"+
				"<edit var=\"txtTitle\" width=350><br><font name=HS12>Type</font><br><combobox width=138 var=cmbType list=\"Annoucement;Donation;Event;Gift;Special;Reward;Critical;Account;Password;Gathering;Promotions;Vote;Other\"></td><td fixwidth=381 align=LEFT>"+
				"<MultiEdit var =\"txtContent\" width=340 height=100><br></td></tr></table><br><table width=762 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=CENTER><table width=381 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=CENTER><font name=HS12>Item's</font></td></tr><tr><td width=381 align=center><table width=381><tr><td fixwidth=190 align=CENTER>"+
                "ID Item<br1><edit type=number var=\"txtIdItem\" width=120></td><td fixwidth=190 align=CENTER>"+
                "Amount<br1><edit type=number var=\"txtAmount\" width=120></td></tr></table><br>"+
                "<button value=\"Add\" action=\""+ ByPassSendAddItem +"\" width=120 height=28 back=L2UI_CT1.Button_DF_Large fore=L2UI_CT1.Button_DF_Large></td></tr><tr><td width=381 align=CENTER>"+
                getRewardWindowsEmail(player)+ "<br></td></tr></table><br></td><td fixwidth=381 align=CENTER><table width=381 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=381 align=CENTER>"+
                "<button value=\"This Zone\" action=\""+ ByPassSendThisZone + "\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                "<button value=\"This Zone (D. Check)\" action=\""+ ByPassSendThisZone_DC +"\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                "<button value=\"To All\" action=\""+ByPassSendThisTOALL + "\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
                "<button value=\"To All (Dual Check)\" action=\""+ ByPassSendThisTOALL_DC + "\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm>"+
				"<button value=\"To All (I. Offline)\" action=\""+ ByPassSendThisTOALL_I_OFFLINE + "\" width=200 height=32 back=L2UI_CT1.OlympiadWnd_DF_HeroConfirm fore=L2UI_CT1.OlympiadWnd_DF_HeroConfirm></td></tr></table><br></td></tr></table><br></center>";
				break;
			case "SURVEY":
				retorno = getSurveyWindows(player);
				break;
			case "PANELOTHER":
				retorno = gerMainWindowOther(player);
				break;
		}
		return retorno;
	}
	
	public static void gmPanelSend(L2PcInstance player, String title, String Message, String TypeSend, String TypeTarget){
		String ItemToGive = getItemFromEmail(player);
		int Contador = 0;
		switch (TypeTarget) {
			case "Z":
				for(L2PcInstance pla : opera.getAllPlayersOnThisRegion(player, false)){
					EmailSend.sendEmail(player, pla.getName(), title, Message, ItemToGive, EmailSend.getIdMessage(TypeSend));
				}
				break;
			case "Z_DC":
				Vector<String> Z_DBC = new Vector<String>();
				for(L2PcInstance pla : opera.getAllPlayersOnThisRegion(player, false)){
					String IpToCheck = ZeuS.getIp_pc(player) + "-" + ZeuS.getIp_Wan(player);
					if(Z_DBC.contains(IpToCheck) && !pla.isGM()){
						continue;
					}
					if(!pla.isGM()){
						Z_DBC.add(IpToCheck);
					}
					EmailSend.sendEmail(player, pla.getName(), title, Message, ItemToGive, EmailSend.getIdMessage(TypeSend));
					Contador++;
				}				
				break;
			case "A":
				for(L2PcInstance pla : opera.getAllPlayerOnWorld(false)){
					EmailSend.sendEmail(player, pla.getName(), title, Message, ItemToGive, EmailSend.getIdMessage(TypeSend));
					Contador++;					
				}
				break;
			case "A_DC":
				Vector<String> A_DBC = new Vector<String>();
				for(L2PcInstance pla : opera.getAllPlayerOnWorld(false)){
					String IpToCheck = ZeuS.getIp_pc(player) + "-" + ZeuS.getIp_Wan(player);
					if(A_DBC.contains(IpToCheck) && !pla.isGM()){
						continue;
					}
					if(!pla.isGM()){
						A_DBC.add(IpToCheck);
					}
					EmailSend.sendEmail(player, pla.getName(), title, Message, ItemToGive, EmailSend.getIdMessage(TypeSend));
					Contador++;					
				}				
				break;
			case "ALL_I_OFF":
				int MinLevel = 40;
				String Query = "SELECT characters.char_name FROM characters WHERE characters.level >=?";
				Connection con = null;
				try{
					con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statement = con.prepareStatement(Query);
					statement.setInt(1, MinLevel);
					ResultSet Resul = statement.executeQuery();
					while(Resul.next()){
						EmailSend.sendEmail(player, Resul.getString(1), title, Message, ItemToGive, EmailSend.getIdMessage(TypeSend));
						Contador++;						
					}
				}catch(Exception a){
					
				}
		}
		central.msgbox("You Sent " + String.valueOf(Contador) + " emails.", player);
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String retorno = "<html><title>ZeuS GM Command</title><body><center>";
		String Icono = "icon.skill0395";
		String Explica = "<br>ZeuS Gm Command Menu";
		String Nombre = "GM Command";
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,false);
		
		String ByPassPremium = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;PREMIUM;0;0;0;0;0";
		String ByPassGathering = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;GATHERING;0;0;0;0;0";
		String ByPassEmailSystem = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;EMAILSYSTEM;0;0;0;0;0";
		String ByPassOther = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;PANELOTHER;0;0;0;0;0";
		
		retorno += "<center><table width=762 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=48><tr><td fixwidth=182 align=LEFT><table width=182 bgcolor=3E3E3E border=0 height=43><tr><td fixwidth=32 align=CENTER>"+
        cbFormato.getBotonForm("icon.skill5041", ByPassPremium) + "<br></td><td fixwidth=156><font color=819FF7 name=hs12>Premium System</font><br1><font color=F5DA81>Give Premium Access</font><br1></td></tr></table></td><td fixwidth=182 align=LEFT><table width=182 bgcolor=3E3E3E border=0 height=43><tr><td fixwidth=32 align=CENTER>"+
        cbFormato.getBotonForm("icon.skill5970", ByPassGathering) + "<br></td><td fixwidth=146><font color=819FF7 name=hs12>Gathering Event</font><br1><font color=F5DA81>Gathering Event Option</font><br1></td></tr></table></td><td fixwidth=182 align=LEFT><table width=182 bgcolor=3E3E3E border=0 height=43><tr><td fixwidth=32 align=CENTER>"+
        cbFormato.getBotonForm("icon.skill1427", ByPassEmailSystem)+ "<br></td><td fixwidth=146><font color=819FF7 name=hs12>Email System</font><br1><font color=F5DA81>Email System</font><br1></td></tr></table></td><td fixwidth=168 align=LEFT><table width=168 bgcolor=3E3E3E border=0 height=43><tr><td fixwidth=32 align=CENTER>"+
        cbFormato.getBotonForm("icon.skill1429", ByPassOther)+ "<br></td><td fixwidth=146><font color=819FF7 name=hs12>Others</font><br1><font color=F5DA81></font><br1></td></tr></table></td></tr></table><br><br></center>";
		
		
		retorno += getMainMenu(player, Params);
		
		retorno += cbManager.getPieCommunidad() + "</body></html>";
		return retorno;
	}
	
	
	public static void voiceHandler(L2PcInstance player, String Param){
		if(!player.isGM()){
			return;
		}
		if(Param == null){
			return;
		}
		
		
		String SplitParams[] = Param.split(" ");
		String Retorno = ""; 
		if(SplitParams[0].equals("AddItem")){
			if(SplitParams.length==3){
				int IdItem = Integer.valueOf( SplitParams[1] );
				long Cantidad = Long.valueOf( SplitParams[2] );
				setNewItemToGathering(player, IdItem, Cantidad);
			}
			Retorno = bypass(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;GATHERING;0;0;0;0;0");			
		}else if(SplitParams[0].equals("AddItemMail")){
			if(SplitParams.length==3){
				int IdItem = Integer.valueOf( SplitParams[1] );
				long Cantidad = Long.valueOf( SplitParams[2] );
				setNewItemToEmail(player, IdItem, Cantidad);
			}
			Retorno = bypass(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;EMAILSYSTEM;0;0;0;0;0");			
		}

		cbManager.separateAndSend(Retorno,player);
	}
	
	private static void giveRewardAllWorld(L2PcInstance player, boolean dualboxCheck){
		String getStrReward = getItemFromGathering(player);
		if(getStrReward.length()==0){
			central.msgbox("You need to add item to give", player);
			return;
		}
		Collection<L2PcInstance> Pplayers = null;
		Vector<L2PcInstance> PlayerCheck = new Vector<L2PcInstance>();
		if(dualboxCheck){
			String IpCheck = "";
			Vector<String>ForDualBox = new Vector<String>();
			for(L2PcInstance playerInWorld : opera.getAllPlayerOnWorld()){
				try{
					if(!playerInWorld.isOnline()){
						continue;
					}
					if(playerInWorld.getClient().isDetached()){
						continue;
					}				
					IpCheck = ZeuS.getIp_pc(playerInWorld) + "-" + ZeuS.getIp_Wan(playerInWorld);
					if(ForDualBox.contains(IpCheck)){
						continue;
					}
					PlayerCheck.add(playerInWorld);
					if(!playerInWorld.isGM()){
						ForDualBox.add(IpCheck);
					}
				}catch(Exception a){
					_log.warning("Error on Give Reward by All World->" + a.getMessage());
				}
			}			
		}else{
			Pplayers = opera.getAllPlayerOnWorld(false);
		}
		
		if(Pplayers==null && (PlayerCheck==null || PlayerCheck.size()==0)){
			central.msgbox("Not player's found to reward it", player);
			return;
		}

		for(L2PcInstance ppl : ( dualboxCheck ? PlayerCheck : Pplayers)){
			opera.giveReward(ppl, getStrReward);
		}
		
		central.msgbox("Has been rewarded " + String.valueOf( ( dualboxCheck ? PlayerCheck.size() : Pplayers.size()) ) + " players on World", player);
		
	}	
	
	
	
	private static void giveRewardZone(L2PcInstance player, boolean dualboxCheck){
		
		String getStrReward = getItemFromGathering(player);
		
		if(getStrReward.length()==0){
			central.msgbox("You need to add item to give", player);
			return;
		}
		
		Vector<L2PcInstance> Pplayers = new Vector<L2PcInstance>();
		if(!dualboxCheck){
			Pplayers = opera.getAllPlayersOnThisRegion(player,false);
		}else{
			String IpCheck = "";
			Vector<String>ForDualBox = new Vector<String>();
			for(L2PcInstance playerInZone : opera.getAllPlayersOnThisRegion(player,false) ){
				try{
					if(!playerInZone.isOnline()){
						continue;
					}
					if(playerInZone.getClient().isDetached()){
						continue;
					}
					IpCheck = ZeuS.getIp_pc(playerInZone) + "-" + ZeuS.getIp_Wan(playerInZone);
					if(ForDualBox.contains(IpCheck)){
						continue;
					}
					Pplayers.add(playerInZone);
					if(!playerInZone.isGM()){
						ForDualBox.add(IpCheck);
					}
				}catch(Exception a){
					_log.warning("Error on Give reward by All Zone->" + a.getMessage());
				}
			}			
		}

		if(Pplayers==null){
			central.msgbox("Not player's found to reward it", player);
			return;
		}
		
		for(L2PcInstance ppl : Pplayers){
			opera.giveReward(ppl, getStrReward);
		}
		
		central.msgbox("Has been rewarded " + String.valueOf( Pplayers.size() ) + " players on this Region", player);
	}
	
	//String ByPassCCLName_dona = "Write Z_CHANGE_CLAN_NAME_DONA Set _ txtCCLName txtCCLName txtCCLName";
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		int buffCount = Config.ALT_OLY_MAX_BUFFS;
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,parm2);
		}else if(parm1.equals("SELECTED_PREMIUM")){
			SELECTED_PREMIUM.put(player.getObjectId(), Integer.valueOf(parm2));
			return mainHtml(player,"PREMIUM");
		}else if(parm1.equals("PREMIUM_GIVE_TARGET")){
			int idPremiumToGive = Integer.valueOf(parm2);
			if(idPremiumToGive>=0){
				L2Object Target = player.getTarget();
				if(Target instanceof L2PcInstance){
					v_donation.setPremium(idPremiumToGive, (L2PcInstance)Target, false, player,false);
				}else{
					central.msgbox("Invalid Target.", player);
				}
			}else{
				central.msgbox("You need to select the premium System to Give", player);
			}
			return mainHtml(player,"PREMIUM");
		}else if(parm1.endsWith("PREMIUM_GIVE_ALL_WORLD")){
			int idPremiumToGive = Integer.valueOf(parm2);
			Vector<String>ForDualBox = new Vector<String>();
			String IpCheck = "";
			for(L2PcInstance playerInZone : opera.getAllPlayerOnWorld()){
				try{
					if(!playerInZone.isOnline()){
						continue;
					}
					if(playerInZone.getClient().isDetached()){
						continue;
					}				
					IpCheck = ZeuS.getIp_pc(playerInZone) + "-" + ZeuS.getIp_Wan(playerInZone);
					
					if(ForDualBox.contains(IpCheck)){
						continue;
					}
					if(ZeuS.isPremium(playerInZone)){
						premiumPersonalData T1 = null;
						if(opera.isPremium_Player(playerInZone)){
							T1 = general.getPremiumDataFromPlayerOrClan(playerInZone.getAccountName());
						}else if(opera.isPremium_Clan(playerInZone)){
							T1 = general.getPremiumDataFromPlayerOrClan(String.valueOf(playerInZone.getClanId()));
						}
						if(T1!=null){
							if(T1.getIdPremiumUse() != idPremiumToGive){
								continue;
							}
						}
					}
					v_donation.setPremium(idPremiumToGive, playerInZone, false, player,false);
					ForDualBox.add(IpCheck);
				}catch(Exception a){
					_log.warning("Error on Give Premium Account by All World->" + a.getMessage());
				}
			}			
		}else if(parm1.endsWith("PREMIUM_GIVE_ALL_WORLD_CHECK_DUALBOX")){
			int idPremiumToGive = Integer.valueOf(parm2);
			for(L2PcInstance playerInZone : opera.getAllPlayerOnWorld()){
				if(!playerInZone.isOnline()){
					continue;
				}
				if(playerInZone.getClient().isDetached()){
					continue;
				}
				v_donation.setPremium(idPremiumToGive, playerInZone, false, player,false);
			}			
		}else if(parm1.endsWith("PREMIUM_GIVE_ZONE")){
			int idPremiumToGive = Integer.valueOf(parm2);
			for(L2PcInstance playerInZone : opera.getAllPlayersOnThisRegion(player,false)){
				if(!playerInZone.isOnline()){
					continue;
				}
				if(playerInZone.getClient().isDetached()){
					continue;
				}
				v_donation.setPremium(idPremiumToGive, playerInZone, false, player,false);
			}
		}else if(parm1.endsWith("PREMIUM_GIVE_ZONE_CHECK_DUALBOX")){
			int idPremiumToGive = Integer.valueOf(parm2);
			Vector<String>ForDualBox = new Vector<String>();
			String IpCheck = "";
			for(L2PcInstance playerInZone : opera.getAllPlayersOnThisRegion(player,false)){
				try{
					if(!playerInZone.isOnline()){
						continue;
					}
					if(playerInZone.getClient().isDetached()){
						continue;
					}				
					IpCheck = ZeuS.getIp_pc(playerInZone) + "-" + ZeuS.getIp_Wan(playerInZone);
					
					if(ForDualBox.contains(IpCheck)){
						continue;
					}
					if(ZeuS.isPremium(playerInZone)){
						premiumPersonalData T1 = null;
						if(opera.isPremium_Player(playerInZone)){
							T1 = general.getPremiumDataFromPlayerOrClan(playerInZone.getAccountName());
						}else if(opera.isPremium_Clan(playerInZone)){
							T1 = general.getPremiumDataFromPlayerOrClan(String.valueOf(playerInZone.getClanId()));
						}
						if(T1!=null){
							if(T1.getIdPremiumUse() != idPremiumToGive){
								continue;
							}
						}
					}
					v_donation.setPremium(idPremiumToGive, playerInZone, false, player,false);
					if(!playerInZone.isGM()){
						ForDualBox.add(IpCheck);
					}
				}catch(Exception a){
					_log.warning("Error on Give Premium Account by Zone->" + a.getMessage());
				}
			}
		}else if(parm1.equals("CHANGE_WISP")){
			general.GmSummonWispChange(player);
			return mainHtml(player,"GATHERING");
		}else if(parm1.equals("SEND_CONFIRM")){
			sendConfirmButton(player);
			return mainHtml(player,"GATHERING");
		}else if(parm1.equals("REMOVE_ITEM")){
			int idItemToRemove = Integer.valueOf(parm2);
			removeItemToGathering(player, idItemToRemove);
			return mainHtml(player,"GATHERING");
		}else if(parm1.equals("REMOVE_ITEM_EMAIL")){
			int idItemToRemove = Integer.valueOf(parm2);
			removeItemToEmail(player, idItemToRemove);
			return mainHtml(player,"EMAILSYSTEM");			
		}else if(parm1.equals("GATHERING_GIVE_ZONE")){
			giveRewardZone(player, false);
			return mainHtml(player,"GATHERING");
		}else if(parm1.equals("GATHERING_GIVE_ZONE_DUALCHECK")){
			giveRewardZone(player,true);
			return mainHtml(player,"GATHERING");
		}else if(parm1.equals("GATHERING_GIVE_ALL")){
			giveRewardAllWorld(player, false);
			return mainHtml(player,"GATHERING");
		}else if(parm1.equals("GATHERING_GIVE_ALL_DUALCHECK")){
			giveRewardAllWorld(player, true);
			return mainHtml(player,"GATHERING");
		}
		return mainHtml(player,params);
	}
	
	public static class SurveyClass{
		private String Asking;
		private Map<Integer, String> Answer_Question = new HashMap<Integer, String>();
		private Map<Integer, Integer> Answers = new HashMap<Integer, Integer>();
		private Map<Integer, Integer> Answers_Player = new HashMap<Integer, Integer>();
		
		private int idGmSurvey;
		private int idFor;
		private boolean _isActive = false;
		public SurveyClass(L2PcInstance player){
			this.idFor=0;
			this.idGmSurvey = player.getObjectId();
			this._isActive = true;
			this.Asking = "";
		}
		
		
		public boolean canAnnoucement(){
			if(Answer_Question==null){
				return false;
			}else if(Answer_Question.size()==0){
				return false;
			}else if(this.Asking.length()==0 || this.Asking.equals("") ){
				return false;
			}else if(!this._isActive){
				return false;
			}
			return true;
		}
		
		@SuppressWarnings("rawtypes")
		public void endSurvey(boolean sendEmailToPlayer){
			int idGanador = 0;
			int tempVotes = 0;
			Iterator itr = Answers.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int idSurvey = (int)Entrada.getKey();
				int Votes = (int)Entrada.getValue();
				if(Votes>tempVotes){
					tempVotes=Votes;
					idGanador = idSurvey;
				}
			}
			String EmailTitle = "***Survey Result***";
			for(L2PcInstance player : opera.getAllPlayerOnWorld(false)){
				String EmailSendMessage = language.getInstance().getMsg(player).ADMIN_SURVEY_ENDED_MESSAGE.replace("$votes", String.valueOf(Answers.get(idGanador))).replace("$question",Asking).replace("$winneranswer", Answer_Question.get(idGanador));
				EmailSend.sendSurveyMail(EmailTitle, EmailSendMessage, player.getObjectId());
			}
			
			this.Asking = "";
			this.Answer_Question.clear();
			this.Answers.clear();
			this.Answers_Player.clear();
			this._isActive = true;
		}
		
		public void enabled(){
			this._isActive=true;
		}
		public void disabled(){
			this._isActive=false;
		}
		public boolean isActive(){
			return this._isActive;
		}
		
		public void setQuestion (String Question){
			this.Asking = Question;
		}
		public String getQuestion(){
			return this.Asking;
		}
		public void setAnswer(String Answer){
			this.Answer_Question.put(idFor++, Answer);
		}
		public void removeAnwer(int idRemove){
			this.Answer_Question.remove(idRemove);
		}
		
		private int getVotes(int idAnswer){
			try{
				return Answers.get(idAnswer);
			}catch(Exception a){
				
			}
			return 0;
		}
		
		@SuppressWarnings("rawtypes")
		public String getSurveyAnswers(){
			String retorno ="";
			
			if(this.Answer_Question!=null){
				if(this.Answer_Question.size()>0){
					Iterator itr = Answer_Question.entrySet().iterator();
					while(itr.hasNext()){
						Map.Entry Entrada = (Map.Entry)itr.next();
						int idSurvey = (int)Entrada.getKey();
						String Answer = (String)Entrada.getValue();
						String ByPassRemover = "bypass ZeuS surveys removeAnswer " + String.valueOf(idSurvey);
						retorno += "<tr><td width = 770 align=CENTER><br1><table fixwidth=770 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=40 cellspacing=1 cellpadding=5><tr><td fixwidth=690>"+
							"<font color=DF7401>"+ Answer +" (Votes="+ String.valueOf(getVotes(idSurvey)) +")</font></td><td fixwidth=50>"+
							"<button value=\"Remove\" action=\""+ ByPassRemover +"\" width=52 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr></table></td></tr>";
					}
				}
			}
			
			return retorno;
		}
		
		private boolean canAnswer(L2PcInstance player){
			if(this.Answers_Player!=null){
				if(this.Answers_Player.containsKey(player.getObjectId())){
					return false;
				}
			}
			return true;
		}
		
		public void chanceStatus(){
			this._isActive = this._isActive ? false : true;			
		}
		
		public void setAnswer(L2PcInstance player, int idAnswer){
			
			if(!canAnswer(player)){
				central.msgbox(language.getInstance().getMsg(player).ADMIN_YOU_CAN_ANSWER_AGAIN, player);
				return;
			}
			
			if(this.Answers==null){
				this.Answers.put(idAnswer, 1);
			}else if(this.Answers.size()==0){
				this.Answers.put(idAnswer, 1);
			}else if(!this.Answers.containsKey(idAnswer)){
				this.Answers.put(idAnswer, 1);
			}else{
				this.Answers.put(idAnswer, this.Answers.get(idAnswer)+1);				
			}
			Answers_Player.put(player.getObjectId(), idAnswer);
			sendWindowsSurveyToPlayer(player);
		}
		
		@SuppressWarnings("rawtypes")
		private String getBoxAnswer(){
			String retorno ="";
			if(this.Answer_Question!=null){
				if(this.Answer_Question.size()>0){
					Iterator itr = Answer_Question.entrySet().iterator();
					while(itr.hasNext()){
						Map.Entry Entrada = (Map.Entry)itr.next();
						int idSurvey = (int)Entrada.getKey();
						String Answer = (String)Entrada.getValue();
						String ByPassAnswer = "bypass ZeuS surveys AnswerQuestion " + idGmSurvey + " " + String.valueOf(idSurvey);
						retorno += "<button value=\""+ Answer +"\" action=\""+ ByPassAnswer +"\" width=220 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"><br1>";
					}
				}
			}			
			
			return retorno;
		}
		
		@SuppressWarnings("rawtypes")
		private String getBoxAnswerVotes(){
			String retorno ="<center><table fixwidth=280>";
			if(this.Answer_Question!=null){
				if(this.Answer_Question.size()>0){
					Iterator itr = Answer_Question.entrySet().iterator();
					while(itr.hasNext()){
						Map.Entry Entrada = (Map.Entry)itr.next();
						int idSurvey = (int)Entrada.getKey();
						String Answer = (String)Entrada.getValue();
						int Votes = getVotes(idSurvey);
						//retorno += "<button value=\""+ Answer +"\" action=\""+ ByPassAnswer +"\" width=220 height=28 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"><br1>";
						retorno += "<tr><td width = 260 align=CENTER><br1><table fixwidth=260 background=L2UI_CT1.Windows_DF_TooltipBG border=0 height=40 cellspacing=1 cellpadding=5><tr><td fixwidth=260 align=\"CENTER\">"+
						"<font color=DF7401>"+ Answer +" (Votes="+ String.valueOf(Votes) +")</font></td></tr></table></td></tr>";						
					}
				}
			}			
			retorno += "</center></table>";
			return retorno;
		}		
		
		public void sendWindowsSurveyToPlayer(L2PcInstance player){
			String Answer = "";
			
			if(!this._isActive){
				central.msgbox("The Current Survey are Disabled.", player);
				return;
			}
			
			if(this.Answer_Question==null){
				central.msgbox("The Current Survey is Over or Empty.", player);
				return;				
			}
			
			if(this.Asking.length()==0 || this.Answer_Question.size()==0 || this.Asking.equals("")  ){
				central.msgbox("The Current Survey is Over or Empty.", player);
				return;				
			}
			
			if(canAnswer(player)){
				Answer = getBoxAnswer();
			}else{
				Answer = getBoxAnswerVotes();
			}
			
			final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/survey.html");
			html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
			html.replace("%QUESTION%", this.Asking);
			html.replace("%ANSWER_BOX%", Answer);
			central.sendHtml(player, html.getHtml(), false);
		}
		
	}
}
class _gmTempData{
	L2PcInstance PLAYER;
	String EMAIL;
	String CODE;
	public _gmTempData(){
		this.PLAYER = null;
		this.EMAIL = "";
		this.CODE = "";
	}
	public void set(L2PcInstance Target, String TargetEmail, String CodeSend){
		this.PLAYER = Target;
		this.EMAIL = TargetEmail;
		this.CODE = CodeSend;
	}
	public final L2PcInstance getPlayer(){
		return this.PLAYER;
	}
	public final String getEmail(){
		return this.EMAIL;
	}
	public final String getCode(){
		return this.CODE;
	}
}
