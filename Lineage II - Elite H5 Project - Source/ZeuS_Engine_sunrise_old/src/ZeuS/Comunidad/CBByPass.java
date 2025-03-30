package ZeuS.Comunidad;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Comunidad.EngineForm.C_GrandRaidBoss;
import ZeuS.Comunidad.EngineForm.C_findparty;
import ZeuS.Comunidad.EngineForm.C_gmcommand;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Comunidad.EngineForm.v_MiscelaniusOption;
import ZeuS.Comunidad.EngineForm.v_Shop;
import ZeuS.Comunidad.EngineForm.v_donation;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.procedimientos.EmailSend;
import ZeuS.tutorial.SecondaryPassword;
import gr.sr.events.engine.EventManagement;
import gr.sr.events.engine.EventManager;
import gr.sr.events.engine.base.Event;
import gr.sr.interf.SunriseEvents;


public class CBByPass{
	private static final Logger _log = Logger.getLogger(CBByPass.class.getName());
	
	public static Map<Integer, Boolean>IsFromMain = new HashMap<Integer, Boolean>();
	
	private static enum TipoComunidad{
		_bbshome,
		_bbsgetfav,
		_bbslink,
		_bbsloc,
		_bbsclan,
		_bbsmemo,
		_bbsmail,
		_bbsfriends
	}

	private static boolean updateInBD(String SaveIN, L2PcInstance player, String titulo, String Mensaje, int IdMensaje){
		String MySqlStr = "";
		boolean retorno = false;
		boolean updateAnnou = false;
		switch (SaveIN){
			case "Z_ANNOUCEMENT_MOD":
			case "Z_CHANGELOG_MOD":
			case "Z_RULES_MOD":
			case "Z_FEATURES_MOD":
			case "Z_EVENTS_MOD":
			case "Z_PLAYGAME_MOD":
				MySqlStr = "update zeus_annoucement set strtitle=?, strmensaje=? where id=?";
				updateAnnou = true;
				break;
			case "Z_CLAN_POST_MOD":
				MySqlStr = "update zeus_cb_clan_foro set memo=? where id=?";
				break;
		}
		if(MySqlStr.length()<=0){
			return false;
		}

		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(MySqlStr);
			if(!SaveIN.equals("Z_CLAN_POST_MOD")){
				ins.setString(1, titulo);
				ins.setString(2, Mensaje);
				ins.setInt(3, IdMensaje);
			}else{
				ins.setString(1, Mensaje);
				ins.setInt(2, IdMensaje);
			}
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
				retorno = true;
			}catch(SQLException e){
				_log.warning("ZeuS CB error ->"+e.getMessage());
			}

		}catch(SQLException e1 ){
			_log.warning("ZeuS CB error ->" + e1.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){

		}
		if(updateAnnou){
			Comunidad.loadAllMsjeCommunity();
		}
		return retorno;
	}


	private static boolean saveInBD(String SaveIN, L2PcInstance player, String titulo, String Mensaje){
		String MySqlStr = "";
		boolean retorno = false;
		boolean incluirSubtitulos = false;
		boolean updateAnnou = true;
		switch (SaveIN){
			case "Z_FEATURES":
			case "Z_EVENTS":
			case "Z_PLAYGAME":
				MySqlStr = "insert into zeus_annoucement(strtitle,strmensaje,strgmnombre,fecha,tipo) values(?,?,?,NOW(),'"+ SaveIN.replace("Z_", "") +"')";
				incluirSubtitulos = true;
				break;
			case "Z_ANNOUCEMENT":
				MySqlStr = "insert into zeus_annoucement(strtitle,strmensaje,strgmnombre,fecha,tipo) values(?,?,?,NOW(),'ANNOUCEMENT')";
				incluirSubtitulos = true;
				break;
			case "Z_CHANGELOG":
				MySqlStr = "insert into zeus_annoucement(strtitle,strmensaje,strgmnombre,fecha,tipo) values('',?,?,NOW(),'CHANGELOG')";
				break;
			case "Z_RULES":
				MySqlStr = "insert into zeus_annoucement(strtitle,strmensaje,strgmnombre,fecha,tipo) values('',?,?,NOW(),'RULES')";
				break;
			case "Z_NEW_CLAN_POST":
				MySqlStr = "insert into zeus_cb_clan_foro(idChar,idClan,memo,createdate) values(?,?,?,NOW())";
				updateAnnou = false;
				break;
		}
		if(MySqlStr.length()<=0){
			return false;
		}

		Connection con = null;
		try{
			int idIns = 1;
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(MySqlStr);
			if(!SaveIN.equals("Z_NEW_CLAN_POST")){
				if(incluirSubtitulos){
					ins.setString(idIns++, titulo);
				}
				ins.setString(idIns++, Mensaje);
				ins.setString(idIns++, player.getName());
			}else{
				
				if(player.getClan()==null){
					return false;
				}
				
				ins.setInt(1, player.getObjectId());
				ins.setInt(2, player.getClanId());
				ins.setString(3, Mensaje);
			}
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
				retorno = true;
			}catch(SQLException e){
				_log.warning("ZeuS CB error ->"+e.getMessage());
			}

		}catch(SQLException e1 ){
			_log.warning("ZeuS CB error ->" + e1.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){

		}
		if(updateAnnou){
			Comunidad.loadAllMsjeCommunity();
		}
		return retorno;
	}
	
	
	private static boolean saveBugReport(String Tipo, String Mensaje,L2PcInstance player){
		boolean retorno = false;
		Connection conn = null;
		try{
		conn = L2DatabaseFactory.getInstance().getConnection();
		String qry = "call sp_bugg_ingreso(1,?,?,?)";
		
		CallableStatement psqry = conn.prepareCall(qry);
		psqry.setString(1, Tipo);
		psqry.setString(2, Mensaje);
		psqry.setString(3, player.getName());
		ResultSet rss = psqry.executeQuery();
		while (rss.next()) {
			retorno=true;
		}
		}catch(SQLException e){

		}

		try{
			conn.close();
		}catch(SQLException a ){

		}		
		return retorno;
	}
	

	public static boolean byPassWrite (L2PcInstance client, String url, String arg1, String arg2, String arg3, String arg4, String arg5){
		boolean retorno = false;
		//_log.warning("url->" + url + " - arg1->" + arg1 + " - arg2->" + arg2 + " - arg3->" + arg3 + " - arg4->" + arg4 + " - arg5->" + arg5);		
		switch(url){
			case "Z_SEND_SECOND_REG":
				SecondaryPassword.showWindowsSecondaryPass(client, arg3, arg4);
				return true;
			case "Z_SEND_SECOND_CHECK":
				SecondaryPassword.checkSecPass(client,arg3,false);
				return true;
			case "Z_SEND_SECOND_CHECK_REMOVE":
				SecondaryPassword.checkSecPass(client,arg3,true);
				return true;
			case "Z_SURVEY_SET_QUESTION":
				C_gmcommand.setSurveyQuestion(client, arg3);
				byPass(client, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;SURVEY;0;0;0;0;0");
				return true;
			case "Z_SURVEY_SET_ANSWER":
				C_gmcommand.setSurveyAnswer(client, arg3);
				byPass(client, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;SURVEY;0;0;0;0;0");				
				return true;				
			case "Z_SEND_EMAIL":
				String tipoEnvio = arg2;
				String Titulo = arg4;
				String TipoMensaje = arg3;
				String Mensaje = arg5;
				C_gmcommand.gmPanelSend(client, Titulo, Mensaje, TipoMensaje, tipoEnvio);
				return true;
			case "Z_CHANGE_CLAN_NAME_DONA":
				if(arg3!=null){
					if(arg3.length()>0){
						if(arg3.split(" ").length==1){
							String ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";" + v_MiscelaniusOption.loadByPass.ChangeClanName.name() + ";dona;" + arg3 + ";0;0;0";
							v_MiscelaniusOption.bypass(client, ByPass);
						}
					}
				}
				return true;		
			case "Z_CHANGE_CLAN_NAME_NORMAL":
				if(arg3!=null){
					if(arg3.length()>0){
						if(arg3.split(" ").length==1){
							v_MiscelaniusOption.TEMPORALDATA.put(client.getObjectId(), arg3);
							Dlg.sendDlg(client, "You want to Change your Clan Name to "+ arg3 +"?", IdDialog.MISCELANIUS_CHANGE_CLAN_NAME);
							v_MiscelaniusOption.bypass(client, "");							
						}
					}
				}
				return true;
			case "Z_CHANGE_CHAR_NAME_DONA":
				if(arg3!=null){
					if(arg3.length()>0){
						if(arg3.split(" ").length==1){
							String ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";" + v_MiscelaniusOption.loadByPass.ChangeCharName.name() + ";dona;" + arg3 + ";0;0;0";
							v_MiscelaniusOption.bypass(client, ByPass);
						}
					}
				}
				return true;			
			case "Z_CHANGE_CHAR_NAME_NORMAL":
				if(arg3!=null){
					if(arg3.length()>0){
						if(arg3.split(" ").length==1){
							v_MiscelaniusOption.TEMPORALDATA.put(client.getObjectId(), arg3);
							Dlg.sendDlg(client, "You want to Change your Name to "+ arg3 +"?", IdDialog.MISCELANIUS_CHANGE_CHAR_NAME);
							v_MiscelaniusOption.bypass(client, "");
						}
					}
				}
				return true;
			case "Z_BUG_REPORT_NEW":
				if(saveBugReport(arg3,arg5,client)){
					central.msgbox(language.getInstance().getMsg(client).BUG_REPORT_SENT, client);
					EmailSend.sendBugReportToAdmin(client);
					cbFormato.cerrarCB(client);					
				}else{
					central.msgbox(language.getInstance().getMsg(client).DO_NOT_LEAVE_EMPTY_FIELD, client);
				}
				return true;
			case "Z_SEARCH_ITEM":
				Comunidad.textToSearch.put(client.getObjectId(), arg5);
				String enviarParam = general.getCOMMUNITY_BOARD_PART_EXEC()+";STORE_SEARCH_1;0;"+arg5+";0;0";
				cbManager.separateAndSend(Comunidad.delegar(client, enviarParam),client);
				return true;
			case "Z_NEW_CLAN_POST":
				if(client.getClan()==null){
					central.msgbox(language.getInstance().getMsg(client).YOU_DONT_HAVE_CLAN, client);
					return true;
				}
				if(saveInBD(url,client,"",arg5)){
					central.msgbox(language.getInstance().getMsg(client).DATA_SAVED, client);
					cbManager.separateAndSend(Clan.delegar(client, general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() ), client);
				}
				retorno = true;
				break;
			case "Z_CLAN_POST_MOD":
				if(updateInBD(url,client,"",arg5, Clan.IdPostModif.get(client.getObjectId()))){
					central.msgbox(language.getInstance().getMsg(client).DATA_UPDATE, client);
					cbManager.separateAndSend(Clan.delegar(client, general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() ), client);
				}
				retorno = true;
				break;
			case "Z_CLAN_MESSAGE_MOD":
				if(client.isClanLeader()){
					client.getClan().setNotice(arg5);
				}
				if(general.COMMUNITY_BOARD_CLAN){
					retorno = true;
					cbManager.separateAndSend(Clan.delegar(client, ""),client);
				}else{
					retorno = false;
				}
				break;
			case "Z_CLAN_NOTICE_PREVIEW":
				String PreviewMensaje = "";
				try{
					PreviewMensaje = arg5;
				}catch(Exception a){

				}
				Clan.getPreviewClanNotice(client, PreviewMensaje);
				retorno=true;
				break;
			case "Z_FEATURES_MOD":
			case "Z_EVENTS_MOD":
			case "Z_PLAYGAME_MOD":
				updateInBD(url,client,arg3,arg5,Comunidad.LastIdModifMensaje.get(client));
				String strComunidad2 = Comunidad.delegar(client, "");
				cbManager.separateAndSend(strComunidad2,client);
				retorno = true;
				break;
			case "Z_FEATURES":
			case "Z_EVENTS":
			case "Z_PLAYGAME":
				if(saveInBD(url,client,arg3,arg5)){
					central.msgbox(language.getInstance().getMsg(client).DATA_SAVED, client);
				}
				String strComunidad0 = Comunidad.delegar(client, "");
				cbManager.separateAndSend(strComunidad0,client);
				retorno = true;
				break;
			case "Z_ANNOUCEMENT":
				if(saveInBD(url,client,arg3,arg5)){
					central.msgbox(language.getInstance().getMsg(client).DATA_SAVED, client);
				}
				String strComunidad = Comunidad.delegar(client, "");
				cbManager.separateAndSend(strComunidad,client);
				retorno = true;
				break;
			case "Z_ANNOUCEMENT_MOD":
				updateInBD(url,client,arg3,arg5,Comunidad.LastIdModifMensaje.get(client));
				String strComunidad5 = Comunidad.delegar(client, "");
				cbManager.separateAndSend(strComunidad5,client);
				retorno = true;
				break;
			case "Z_CHANGELOG":
				if(saveInBD(url,client,arg3,arg5)){
					central.msgbox(language.getInstance().getMsg(client).DATA_SAVED, client);
				}
				String stChangeLog = Comunidad.delegar(client, "");
				cbManager.separateAndSend(stChangeLog,client);
				retorno = true;
				break;
			case "Z_CHANGELOG_MOD":
				updateInBD(url,client,"none",arg5,Comunidad.LastIdModifMensaje.get(client));
				String strComunidad3 = Comunidad.delegar(client, "");
				cbManager.separateAndSend(strComunidad3,client);
				retorno = true;
				break;
			case "Z_RULES":
				if(saveInBD(url,client,arg3,arg5)){
					central.msgbox(language.getInstance().getMsg(client).DATA_SAVED, client);
				}
				String stChangeRules = Comunidad.delegar(client, "");
				cbManager.separateAndSend(stChangeRules,client);
				retorno = true;
				break;
			case "Z_RULES_MOD":
				updateInBD(url,client,"none",arg5,Comunidad.LastIdModifMensaje.get(client));
				String strComunidad4 = Comunidad.delegar(client, "");
				cbManager.separateAndSend(strComunidad4,client);
				retorno = true;
				break;
			case "Z_STAFF":
				retorno = true;
				break;
		}
		return retorno;
	}

	public static boolean byPass(L2PcInstance player, String command){
		if(command == null) {
			return false;
		}		
		String strComunidad = "";
		if(!general.onLine || (!general.COMMUNITY_BOARD_GRAND_RB && !general.COMMUNITY_BOARD && !general.COMMUNITY_BOARD_REGION && !general.COMMUNITY_BOARD_ENGINE)){
			return false;
		}
		player.setIsUsingBuySellStore(false);
		String LinkComunidad = command;


		if(player.isInStoreMode() || player.isInCraftMode()){
			return true;
		}
		
		String Retorno = "";
		
		boolean NeptuneMenu = true;
		
		if(NeptuneMenu) {
			if(player.isInOlympiad() || player.isInOlympiadMode() || player.isOnEvent() || RaidBossEvent.isPlayerOnRBEvent(player) ) {
				return true;
			}

			if(LinkComunidad.startsWith("_bbsloc")) {
				ZeuS.isCMDFromZeuS(player, "ZeuS openCB buffer");
				return true;
			}


			if(player.isInOlympiad() || player.isInOlympiadMode() || player.isOnEvent() || SunriseEvents.isInEvent(player) || RaidBossEvent.isPlayerOnRBEvent(player) ) {
				return true;
			}
			if(LinkComunidad.startsWith( "_bbsgetfav" )) {
				if(general.SHOP_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM && !ZeuS.isPremium(player) && !player.isGM() ){
					if(!player.isInsideZone(ZoneIdType.PEACE)){
						ZeuS.isCMDFromZeuS(player, "ZeuS openCB shop");
						return true;
					}else {
						return true;
					}
				}			
				ZeuS.isCMDFromZeuS(player, "ZeuS openCB shop");
				return true;
			}else if(LinkComunidad.startsWith("_bbslink")) {
				ZeuS.isCMDFromZeuS(player, "ZeuS openCB teleport");
				return true;			
			}else if(LinkComunidad.startsWith("_bbsloc")) {
				ZeuS.isCMDFromZeuS(player, "ZeuS openCB buffer");
				return true;			
			}else if(LinkComunidad.startsWith("bbs_add_fav")) {
				ZeuS.isCMDFromZeuS(player, "ZeuS openCB donation_manager");
				return true;			
			}else if(LinkComunidad.startsWith("_bbsclan;partymatching;accept")) {
				C_findparty.bypass(player, LinkComunidad);
				return true;
			}else if(LinkComunidad.startsWith("_bbsclan;partymatching;refuse")) {
				C_findparty.bypass(player, LinkComunidad);
				return true;
			}else if(LinkComunidad.startsWith("_bbsclan;partymatching;refuseAll")) {
				C_findparty.bypass(player, LinkComunidad);
				return true;
			}
		}
		
		
		
		
		cbManager.separateAndSend(Retorno, player);
		
		cbManager.loadColores();
		if(LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_PART_EXEC()) && general.COMMUNITY_BOARD){
			strComunidad = Comunidad.delegar(player, LinkComunidad);
			IsFromMain.put(player.getObjectId(), true);
		}else if(LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_REGION_PART_EXEC()) && general.COMMUNITY_BOARD_REGION){
			strComunidad = Region.getRegion(player,LinkComunidad,0);
			IsFromMain.put(player.getObjectId(), false);
		}else if(LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()) && general.COMMUNITY_BOARD_ENGINE){
			if(player.isJailed()){
				strComunidad = Comunidad.getComunidad(player, "", 0);
			}else{
				strComunidad = Engine.delegar(player, LinkComunidad);
				IsFromMain.put(player.getObjectId(), false);
			}
		}else if(LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_CLAN_PART_EXEC()) && general.COMMUNITY_BOARD_CLAN){
			strComunidad = Clan.delegar(player, LinkComunidad);
			IsFromMain.put(player.getObjectId(), false);
		}else if(LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_GRAND_RB_EXEC()) && general.COMMUNITY_BOARD_GRAND_RB){
			strComunidad = C_GrandRaidBoss.delegar(player, LinkComunidad);
			IsFromMain.put(player.getObjectId(), false);
		}else if(LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC()) && general.COMMUNITY_BOARD_PARTYFINDER){
			if(player.isJailed()){
				strComunidad = Comunidad.getComunidad(player, "", 0);				
			}else{
				strComunidad = C_findparty.bypass(player, LinkComunidad);
				IsFromMain.put(player.getObjectId(), false);
			}
		}else if( LinkComunidad.startsWith(general.getCOMMUNITY_BOARD_DONATION_PART_EXEC()) && general.COMMUNITY_BOARD_DONATION) {
			if(player.isJailed()) {
				strComunidad = Comunidad.getComunidad(player, "", 0);
			}else {
				strComunidad = v_donation.bypass(player, "0;0;0;0;0;0;0;0");
				IsFromMain.put(player.getObjectId(), false);
			}
		}else{
			return false;
		}

		if(strComunidad.length()>0){
			cbManager.separateAndSend(strComunidad,player);
		}
		return true;
	}
}
