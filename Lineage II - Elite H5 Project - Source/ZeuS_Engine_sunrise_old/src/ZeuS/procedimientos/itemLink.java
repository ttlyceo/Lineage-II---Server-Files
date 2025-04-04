package ZeuS.procedimientos;

import gr.sr.interf.SunriseEvents;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Comunidad;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.C_cmdInfo;
import ZeuS.Comunidad.EngineForm.C_findparty;
import ZeuS.Comunidad.EngineForm.C_gmcommand;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.v_Dressme;
import ZeuS.Config.general;
import ZeuS.Instances.VoteInstance;
import ZeuS.Instances.pvpInstance;
import ZeuS.Instances.pvpInstance._ZONES_TYPE;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.accountRecovery;
import ZeuS.interfase.central;
import ZeuS.interfase.changeCharAccount;
import ZeuS.interfase.changePassword;
import ZeuS.interfase.fixMe;
import ZeuS.interfase.sellAccount;
import ZeuS.interfase.sellClan;
import ZeuS.interfase.votereward;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.tutorial.SecondaryPassword;

public class itemLink {
	private static int ContadorRetroceso = -2099999999;
	private static Vector<Integer> v_findParty = new Vector<Integer>();
	private static Vector<Integer> v_auctionoffline = new Vector<Integer>();
	private static int v_voteLink = 0;
	private static int v_recall_all_ppl = 0;
	private static int v_raid_boss_event = 0;
	private static int v_event = 0;
	private static int v_server_info = 0;
	private static int v_home_cb = 0;
	private static int v_command_info = 0;
	private static int v_donation_windows = 0;
	private static int v_acc_register = 0;
	private static int v_change_password = 0;
	private static int v_change_email = 0;
	private static int v_remove_secondary_password = 0;
	private static int v_acc_recovery = 0;
	private static int v_sell_account = 0;
	private static int v_sell_clan = 0;
	private static int v_dressme = 0;
	private static int v_fixme = 0;
	private static int v_oly_buffer = 0;
	private static int v_move_char = 0;
	private static int v_language = 0 ;
	private static int v_pvp_zone_single = 0;
	private static int v_pvp_zone_party = 0;
	private static int v_pvp_zone_clan = 0;
	private static int v_pvp_zone_FreeToAll = 0;
	private static int v_pvp_zone_Classes = 0;
	private static int v_pvp_zone_Dropzone = 0;
	private static int v_vote_zone_enter = 0;
	private static int v_vote_zone_npc_tele = 0;
	private static Map<Integer, Integer> SurveyWindows = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> AnnoucementLink = new HashMap<Integer, Integer>();
	
	private static Map<Integer, Integer> GM_RECALL = new HashMap<Integer, Integer>();
	
	private static final Logger _log = Logger.getLogger(itemLink.class.getName());
	
	@SuppressWarnings("unused")
	private static boolean setClean = true;
	@SuppressWarnings("unused")
	private static int autoCleanEvery = 64800; // 18 hours
	
	
	public enum sectores{
		V_FIND_PARTY,
		V_AUCTION_OFFLINE,
		V_VOTE,
		V_RECALL,
		V_RAID_BOSS_EVENT,
		V_EVENT,
		V_SERVER_INFO,
		V_HOME_CB,
		V_COMMAND_INFO,
		V_DONATION,
		V_ACCREGISTER,
		V_CHANGEPASSWORD,
		V_CHANGEEMAIL,
		V_REMOVSECONDARYPASSWORD,
		V_ACC_RECOVERY,
		V_SELL_ACCOUNT,
		V_SELL_CLAN,
		V_DRESSME,
		V_FIXME,
		V_OLY_BUFFER,
		V_MOVE_CHAR,
		V_SURVEY,
		V_LANGUAGE,
		V_PVP_ZONE_SINGLE,
		V_PVP_ZONE_PARTY,
		V_PVP_ZONE_CLAN,
		V_PVP_ZONE_FREE_TO_ALL,
		V_PVP_ZONE_CLASSES,
		V_PVP_ZONE_DROPZONE,
		V_VOTE_ZONE_ENTER,
		V_VOTE_ZONE_TELE_NPC
	}
	
	@SuppressWarnings("unused")
	private static void autoClean(){
		int oldCounter = ContadorRetroceso;
		ContadorRetroceso = -2099999999;
		setClean = true;
		_log.warning("Item Link: Autoclean Old ID:" + oldCounter + ", all Clean");
	}
	
	@SuppressWarnings("rawtypes")
	public static boolean isLinkFromZeuS(L2PcInstance charRequestLink, int idLink){
		//_log.warning("ID LINK REQUEST->" + idLink + "   -   IDWINDOWS->" + v_voteLink);

		if(!ZeuS.isActivePIN(charRequestLink)){
			return false;
		}
		
		if(charRequestLink.isInOlympiadMode() || charRequestLink.inObserverMode() || SunriseEvents.isInEvent(charRequestLink) || RaidBossEvent.isPlayerOnRBEvent(charRequestLink) ){
			return false;
		}
		
		if(v_findParty!=null){
			if(v_findParty.contains(idLink)){
				C_findparty.bypass(charRequestLink, idLink);
				return true;
			}
		}
		if(v_auctionoffline!=null){
			if(v_auctionoffline.contains(idLink)){
				return true;
			}
		}
		
		if(idLink == v_voteLink){
			votereward.getVoteWindows(charRequestLink, true);
			return true;
		}else if(idLink == v_recall_all_ppl){
			L2PcInstance player = null;
			try{
				player = opera.getPlayerbyID(GM_RECALL.get(v_recall_all_ppl));
			}catch(Exception a){
				player = null;
			}
			if(player !=null){
				if(player == charRequestLink){
					return false;
				}
				if(general.isGmSummonWisp(player)){
					Location Locacion = player.getLocation();
					charRequestLink.teleToLocation(Locacion, 300);
				}else{
					central.msgbox("Adm/GM is Not ready to Summon by this option.", charRequestLink);
				}
			}
			return true;
		}else if(idLink == v_raid_boss_event){
			if(RaidBossEvent._joining){
				String Questt = language.getInstance().getMsg(charRequestLink).RAIDBOSS_JOIN;
				Dlg.sendDlg(charRequestLink, Questt, IdDialog.ENGINE_RAID_BOSS_QUESTION_TO_JOIN);
			}
			return true;
		}else if(idLink == v_event){
			if(!SunriseEvents.isInEvent(charRequestLink)){
				central.msgbox("To see the event information u can see on the CB, event section", charRequestLink);
			}
			return true;
		}else if(idLink == v_server_info){
			String toSend = Comunidad.getServerInformationRate(charRequestLink, 0);
			cbManager.separateAndSend(toSend, charRequestLink);
			return true;
		}else if(idLink == v_home_cb){
			CBByPass.byPass(charRequestLink, general.getCOMMUNITY_BOARD_PART_EXEC());
			return true;
		}else if(idLink == v_command_info){
			String toSend = C_cmdInfo.bypass(charRequestLink, "0;0;0;0;0");
			cbManager.separateAndSend(toSend, charRequestLink);
			return true;
		}else if(idLink == v_donation_windows){
			String ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";0;0;0;0;0;0";
			CBByPass.IsFromMain.put(charRequestLink.getObjectId(), false);
			CBByPass.byPass(charRequestLink, ByPass);			
			return true;
		}else if(idLink == v_acc_register){
			EmailRegistration.getRegistrationWindos(charRequestLink, true, false);
			return true;
		}else if(idLink == v_change_email){
			EmailRegistration.getRegistrationWindos(charRequestLink, true, true);
			return true;
		}else if(idLink == v_change_password){
			changePassword.bypass(charRequestLink, "");
			return true;
		}else if(idLink == v_remove_secondary_password){
			SecondaryPassword.showRemoveWindos(charRequestLink);
			return true;
		}else if(idLink == v_acc_recovery){
			accountRecovery.showRecoveryWindow(charRequestLink);
			return true;
		}else if(idLink == v_sell_account){
			sellAccount.showSellConfig(charRequestLink);
			return true;			
		}else if(idLink == v_sell_clan){
			sellClan.showSellConfig(charRequestLink);
			return true;	
		}else if(idLink == v_dressme){
			String CBHtml = v_Dressme.bypass(charRequestLink, Engine.enumBypass.Dressme.name()+";0;0;0;0;0;0;0;0");
			cbManager.separateAndSend(CBHtml, charRequestLink);
			return true;			
		}else if(idLink == v_fixme){
			fixMe.delegar(charRequestLink, "");
			return true;			
		}else if(idLink == v_oly_buffer){
			if(!general.OLY_CAN_USE_SCHEME_BUFFER){
				central.msgbox(language.getInstance().getMsg(charRequestLink).DISABLE_BY_ADMIN, charRequestLink);
				return false;
			}
			
			if(!charRequestLink.isNoble()){
				central.msgbox("This command is only for noble player", charRequestLink);
				return true;
			}
			if(opera.canUseCBFunction(charRequestLink)){
				String Enviar = C_oly_buff.bypass(charRequestLink, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0");
				cbManager.separateAndSend(Enviar, charRequestLink);
				System.out.print("itemlink.java");
				System.out.print(Enviar);
			}else{
				central.msgbox("This command are blocked on this Area, Zone or Time", charRequestLink);
			}
		}else if(idLink == v_move_char){
			changeCharAccount.ShowFirtWindows(charRequestLink);
			return true;
		}else if(idLink == v_language){
			language.getInstance().showLanguageWindows(charRequestLink);
			return true;
		}else if(idLink == v_pvp_zone_single){
			pvpInstance.setPlayerToEnter(charRequestLink, _ZONES_TYPE.INDIVIDUAL.name());
			Dlg.sendDlg(charRequestLink, language.getInstance().getMsg(charRequestLink).PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER.replace("$type", _ZONES_TYPE.INDIVIDUAL.name()) , IdDialog.PVP_INSTANCE_ASK_FOR_ENTER, 10);
			//pvpInstance.enterZone(charRequestLink, _ZONES_TYPE.INDIVIDUAL);
			return true;
		}else if(idLink == v_pvp_zone_party){
			pvpInstance.setPlayerToEnter(charRequestLink, _ZONES_TYPE.PARTY.name());
			Dlg.sendDlg(charRequestLink, language.getInstance().getMsg(charRequestLink).PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER.replace("$type", _ZONES_TYPE.PARTY.name()) , IdDialog.PVP_INSTANCE_ASK_FOR_ENTER, 10);			
			//pvpInstance.enterZone(charRequestLink, _ZONES_TYPE.PARTY);
			return true;
		}else if(idLink == v_pvp_zone_clan){
			pvpInstance.setPlayerToEnter(charRequestLink, _ZONES_TYPE.CLAN.name());
			Dlg.sendDlg(charRequestLink, language.getInstance().getMsg(charRequestLink).PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER.replace("$type", _ZONES_TYPE.CLAN.name()) , IdDialog.PVP_INSTANCE_ASK_FOR_ENTER, 10);
			//pvpInstance.enterZone(charRequestLink, _ZONES_TYPE.CLAN);
			return true;
		}else if(idLink == v_pvp_zone_FreeToAll){
			pvpInstance.setPlayerToEnter(charRequestLink, _ZONES_TYPE.FREE_FOR_ALL.name());
			Dlg.sendDlg(charRequestLink, language.getInstance().getMsg(charRequestLink).PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER.replace("$type", _ZONES_TYPE.FREE_FOR_ALL.name()) , IdDialog.PVP_INSTANCE_ASK_FOR_ENTER, 10);
			//pvpInstance.enterZone(charRequestLink, _ZONES_TYPE.FREE_FOR_ALL);
			return true;
		}else if(idLink == v_pvp_zone_Classes){
			pvpInstance.setPlayerToEnter(charRequestLink, _ZONES_TYPE.CLASSES.name());
			Dlg.sendDlg(charRequestLink, language.getInstance().getMsg(charRequestLink).PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER.replace("$type", _ZONES_TYPE.CLASSES.name()) , IdDialog.PVP_INSTANCE_ASK_FOR_ENTER, 10);
			//pvpInstance.enterZone(charRequestLink, _ZONES_TYPE.CLASSES);
			return true;
		}else if(idLink == v_pvp_zone_Dropzone){
			pvpInstance.setPlayerToEnter(charRequestLink, _ZONES_TYPE.DROP_ZONE.name());
			Dlg.sendDlg(charRequestLink, language.getInstance().getMsg(charRequestLink).PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER.replace("$type", _ZONES_TYPE.DROP_ZONE.name()) , IdDialog.PVP_INSTANCE_ASK_FOR_ENTER, 10);
			//pvpInstance.enterZone(charRequestLink, _ZONES_TYPE.CLASSES);
			return true;			
		}else if(idLink == v_vote_zone_enter){
			VoteInstance.enterToZone(charRequestLink);
			return true;
		}else if(idLink == v_vote_zone_npc_tele){
			VoteInstance.teleportPlayerToNPC(charRequestLink);
			return true;
		}
		
		if(SurveyWindows!=null){
			if(SurveyWindows.size()>0){
				Iterator itr = SurveyWindows.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idLinkIterator = (int)Entrada.getKey();
					int idGm = (int)Entrada.getValue();
					if(idLink == idLinkIterator){
						C_gmcommand.showSurveyWindows(charRequestLink, idGm);
						return true;
					}
				}				
			}
		}
		
		if(AnnoucementLink!=null){
			if(AnnoucementLink.size()>0){
				if(AnnoucementLink.containsKey(idLink)){
					int idMensj = AnnoucementLink.get(idLink);
					showMensjWindows(charRequestLink,idMensj);
					return true;
				}
			}
		}

		
		return false;
	}
	
	private static void showMensjWindows(L2PcInstance player, int idToShow){
		Comunidad.showCommunityMessageWindow(player, idToShow);
	}
	
	public static int getIdForRemoveSecondaryPass(){
		if(v_remove_secondary_password == 0){
			getNewIdLink(sectores.V_REMOVSECONDARYPASSWORD);
		}
		return v_remove_secondary_password;
	}
	
	public static int getIdForAccRegister(){
		if(v_acc_register == 0){
			getNewIdLink(sectores.V_ACCREGISTER);
		}
		return v_acc_register;
	}
	
	public static int getIdForEmailChange(){
		if(v_change_email == 0){
			getNewIdLink(sectores.V_CHANGEEMAIL);
		}
		return v_change_email;
	}
	
	public static int getIdForChangePassword(){
		if(v_change_password == 0){
			getNewIdLink(sectores.V_CHANGEPASSWORD);
		}
		return v_change_password;
	}
	
	public static int getIdForRaidBossEvent(){
		if(v_raid_boss_event==0){
			getNewIdLink(sectores.V_RAID_BOSS_EVENT);
		}
		return v_raid_boss_event;
	}
	
	public static int getIdForDonation(){
		if(v_donation_windows == 0){
			getNewIdLink(sectores.V_DONATION);
		}
		return v_donation_windows;
	}
	
	public static int getIdForCommandInfo(){
		if(v_command_info == 0){
			getNewIdLink(sectores.V_COMMAND_INFO);
		}
		return v_command_info;
	}
	
	public static int getIdForHomeCB(){
		if(v_home_cb==0){
			getNewIdLink(sectores.V_HOME_CB);
		}
		return v_home_cb;
	}
	
	public static int getIdForServerInfo(){
		if(v_server_info==0){
			getNewIdLink(sectores.V_SERVER_INFO);
		}
		return v_server_info;
	}
	
	public static int getIdForEvent(){
		if(v_event==0){
			getNewIdLink(sectores.V_EVENT);
		}
		return v_event;
	}
	
	public static int getIdForVoteWindows(){
		if(v_voteLink==0){
			getNewIdLink(sectores.V_VOTE);
		}
		return v_voteLink;
	}
	
	public static int getIdForRecall(L2PcInstance player){
		if(v_recall_all_ppl==0){
			getNewIdLink(sectores.V_RECALL);
			GM_RECALL.put(v_recall_all_ppl, player.getObjectId());
		}
		return v_recall_all_ppl;
	}
	
	public static int getIdForAccRecovery(){
		if(v_acc_recovery==0){
			getNewIdLink(sectores.V_ACC_RECOVERY);
		}
		return v_acc_recovery;
	}

	public static int getIdForSellAccount(){
		if(v_sell_account==0){
			getNewIdLink(sectores.V_SELL_ACCOUNT);
		}
		return v_sell_account;
	}
	
	public static int getIdForSellClan(){
		if(v_sell_clan==0){
			getNewIdLink(sectores.V_SELL_CLAN);
		}
		return v_sell_clan;
	}
	
	public static int getIdForDressme(){
		if(v_dressme==0){
			getNewIdLink(sectores.V_DRESSME);
		}
		return v_dressme;
	}
	
	public static int getIdForFixMe(){
		if(v_fixme==0){
			getNewIdLink(sectores.V_FIXME);
		}
		return v_fixme;
	}
	
	public static int getIdForMoveChar(){
		if(v_move_char==0){
			getNewIdLink(sectores.V_MOVE_CHAR);
		}
		return v_move_char;
	}
	
	public static int getIdForOlyBuffer(){
		if(v_oly_buffer==0){
			getNewIdLink(sectores.V_OLY_BUFFER);
		}
		return v_oly_buffer;
	}
	
	public static int getIdForLanguage(){
		if(v_language==0){
			getNewIdLink(sectores.V_LANGUAGE);
		}
		return v_language;
	}
	
	public static int getIdForPVP_Zone_Single(){
		if(v_pvp_zone_single==0){
			getNewIdLink(sectores.V_PVP_ZONE_SINGLE);
		}
		return v_pvp_zone_single;
	}
	
	public static int getIdForPVP_Zone_Party(){
		if(v_pvp_zone_party==0){
			getNewIdLink(sectores.V_PVP_ZONE_PARTY);
		}
		return v_pvp_zone_party;
	}
	
	public static int getIdForPVP_Zone_Clan(){
		if(v_pvp_zone_clan==0){
			getNewIdLink(sectores.V_PVP_ZONE_CLAN);
		}
		return v_pvp_zone_clan;
	}
	public static int getIdForPVP_Zone_FreeToAll(){
		if(v_pvp_zone_FreeToAll==0){
			getNewIdLink(sectores.V_PVP_ZONE_FREE_TO_ALL);
		}
		return v_pvp_zone_FreeToAll;
	}
	
	public static int getIdForPVP_Zone_Classes(){
		if(v_pvp_zone_Classes==0){
			getNewIdLink(sectores.V_PVP_ZONE_CLASSES);
		}
		return v_pvp_zone_Classes;
	}
	public static int getIdForPVP_Zone_DropZone(){
		if(v_pvp_zone_Dropzone==0){
			getNewIdLink(sectores.V_PVP_ZONE_DROPZONE);
		}
		return v_pvp_zone_Dropzone;
	}	
	
	public static int getIdForVote_Zone_Enter(){
		if(v_vote_zone_enter==0){
			getNewIdLink(sectores.V_VOTE_ZONE_ENTER);
		}
		return v_vote_zone_enter;
	}
	
	public static int getIdForVote_Zone_Tele_Npc(){
		if(v_vote_zone_npc_tele==0){
			getNewIdLink(sectores.V_VOTE_ZONE_TELE_NPC);
		}
		return v_vote_zone_npc_tele;
	}
	
	@SuppressWarnings("rawtypes")
	public static int getIdForSurvey(L2PcInstance player){
		int Retorno = 0;
		if(SurveyWindows!=null){
			if(SurveyWindows.size()>0){
				Iterator itr = SurveyWindows.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idLinkIterator = (int)Entrada.getKey();
					int idGm = (int)Entrada.getValue();
					if(player.getObjectId() == idGm){
						Retorno = idLinkIterator;
						return Retorno;
					}
				}
			}
		}
		Retorno = ContadorRetroceso++;
		SurveyWindows.put(Retorno, player.getObjectId());
		return Retorno;
	}
	
	@SuppressWarnings("rawtypes")
	public static int getIdAnnoucementToShow(L2PcInstance player, int idAnnoucementToShow){
		int Retorno = 0;
		if(AnnoucementLink!=null){
			if(AnnoucementLink.size()>0){
				Iterator itr = AnnoucementLink.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idLinkIterator = (int)Entrada.getKey();
					int idToShow = (int)Entrada.getValue();
					if(idToShow == idAnnoucementToShow){
						Retorno = idLinkIterator;
						return Retorno;
					}
				}
			}
		}
		Retorno = ContadorRetroceso++;
		AnnoucementLink.put(Retorno, idAnnoucementToShow);
		return Retorno;
	}	
	
	
	@SuppressWarnings("incomplete-switch")
	public static int getNewIdLink(sectores Sector){
		/*if(setClean){
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
				@Override
				public void run(){
					autoClean();
				}
			}, autoCleanEvery * 1000); // 18 Hours
			setClean = false;
		}*/
		int retorno =0;
		switch(Sector){
			case V_FIND_PARTY:
				retorno = ContadorRetroceso++;
				v_findParty.add(retorno);
				return retorno;
			case V_AUCTION_OFFLINE:
				retorno = ContadorRetroceso++;
				v_auctionoffline.add(retorno);
				return retorno;
			case V_VOTE:
				v_voteLink = ContadorRetroceso++;
				return v_voteLink;
			case V_RECALL:
				v_recall_all_ppl = ContadorRetroceso++;
				return v_recall_all_ppl;
			case V_RAID_BOSS_EVENT:
				v_raid_boss_event = ContadorRetroceso++;
				return v_raid_boss_event;
			case V_EVENT:
				v_event = ContadorRetroceso++;
				return v_event;
			case V_SERVER_INFO:
				v_server_info = ContadorRetroceso++;
				return v_server_info;
			case V_HOME_CB:
				v_home_cb = ContadorRetroceso++;
				return v_home_cb;
			case V_COMMAND_INFO:
				v_command_info = ContadorRetroceso++;
				return v_command_info;
			case V_DONATION:
				v_donation_windows = ContadorRetroceso++;
				return v_donation_windows;
			case V_ACCREGISTER:
				v_acc_register = ContadorRetroceso++;
				return v_acc_register;
			case V_CHANGEPASSWORD:	
				v_change_password = ContadorRetroceso++;
				return v_change_password;
			case V_CHANGEEMAIL:
				v_change_email = ContadorRetroceso++;
				return v_change_email;
			case V_REMOVSECONDARYPASSWORD:
				v_remove_secondary_password = ContadorRetroceso++;
				return v_remove_secondary_password;
			case V_ACC_RECOVERY:
				v_acc_recovery = ContadorRetroceso++;
				return v_acc_recovery;
			case V_SELL_ACCOUNT:
				v_sell_account = ContadorRetroceso++;
				return v_sell_account;
			case V_SELL_CLAN:
				v_sell_clan = ContadorRetroceso++;
				return v_sell_clan;
			case V_DRESSME:
				v_dressme = ContadorRetroceso++;
				return v_dressme;
			case V_FIXME:
				v_fixme = ContadorRetroceso++;
				return v_fixme;
			case V_OLY_BUFFER:
				v_oly_buffer = ContadorRetroceso++;
				return v_oly_buffer;
			case V_MOVE_CHAR:
				v_move_char = ContadorRetroceso++;
				return v_move_char;
			case V_LANGUAGE:
				v_language = ContadorRetroceso++;
				return v_language;
			case V_PVP_ZONE_SINGLE:
				v_pvp_zone_single = ContadorRetroceso++;
				return v_pvp_zone_single;
			case V_PVP_ZONE_PARTY:
				v_pvp_zone_party = ContadorRetroceso++;
				return v_pvp_zone_party;
			case V_PVP_ZONE_CLAN:
				v_pvp_zone_clan = ContadorRetroceso++;
				return v_pvp_zone_clan;
			case V_PVP_ZONE_FREE_TO_ALL:
				v_pvp_zone_FreeToAll = ContadorRetroceso++;
				return v_pvp_zone_FreeToAll;
			case V_PVP_ZONE_CLASSES:
				v_pvp_zone_Classes = ContadorRetroceso++;
				return v_pvp_zone_Classes;
			case V_PVP_ZONE_DROPZONE:
				v_pvp_zone_Dropzone = ContadorRetroceso++;
				return v_pvp_zone_Dropzone;
			case V_VOTE_ZONE_ENTER:
				v_vote_zone_enter = ContadorRetroceso++;
				return v_vote_zone_enter;
			case V_VOTE_ZONE_TELE_NPC:
				v_vote_zone_npc_tele = ContadorRetroceso++;
				return v_vote_zone_npc_tele;
					
		}
		return ContadorRetroceso;
	}
	
}
