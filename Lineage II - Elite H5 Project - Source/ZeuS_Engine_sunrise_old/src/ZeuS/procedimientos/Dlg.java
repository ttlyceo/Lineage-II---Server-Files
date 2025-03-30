package ZeuS.procedimientos;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.EngineForm.C_gmcommand;
import ZeuS.Comunidad.EngineForm.v_Buffer_New;
import ZeuS.Comunidad.EngineForm.v_Dressme;
import ZeuS.Comunidad.EngineForm.v_RaidBossInfo;
import ZeuS.Comunidad.EngineForm.v_Teleport;
import ZeuS.Comunidad.EngineForm.v_auction_house;
import ZeuS.Comunidad.EngineForm.v_bid_house;
import ZeuS.Comunidad.EngineForm.v_donation;
import ZeuS.Comunidad.EngineForm.v_dropsearch;
import ZeuS.Comunidad.EngineForm.v_subclass;
import ZeuS.Comunidad.EngineForm.v_MiscelaniusOption;
import ZeuS.Comunidad.EngineForm.v_MiscelaniusOption.loadByPass;
import ZeuS.Config.general;
import ZeuS.Instances.oly_monument;
import ZeuS.Instances.pvpInstance;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.antibotSystem;
import ZeuS.interfase.borrowAccountSystem;
import ZeuS.interfase.central;
import ZeuS.interfase.instanceZone;
import ZeuS.interfase.sellBuff;
import ZeuS.interfase.votereward;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ConfirmDlg;

public class Dlg {
	
	private static Dlg _instance;
	
	private static final Logger _log = Logger.getLogger(Dlg.class.getName());
	private static Map<Integer, Integer> ANNOUCEMENT_LINK = new HashMap<Integer, Integer>();
	
	public static enum IdDialog {
		MISCELANIUS_lv85(4000,"",""),
		MISCELANIUS_NOBLE(4001,"",""),
		MISCELANIUS_FAME(4002,"",""),
		MISCELANIUS_CLAN(4003,"",""),
		MISCELANIUS_REDUCEPK(4004,"",""),
		MISCELANIUS_SEX(4005,"",""),
		MISCELANIUS_AIONORMAL(4006,"",""),
		MISCELANIUS_AIO30(4007,"",""),
		MISCELANIUS_CHANGE_CHAR_NAME(4008,"",""),
		MISCELANIUS_CHANGE_CLAN_NAME(4009,"",""),
		PVP_INSTANCE_REVIVE(5000,"",""),
		PVP_INSTANCE_PARTY_GO(5001,"",""),
		PVP_INSTANCE_ASK_FOR_ENTER(5002, "", ""),
		ENGINE_REMOVE_SCHEME(5100,"",""),
		ENGINE_DROPSEARCH_TELEPORT(5101,"",""),
		ENGINE_DROPSEARCH_OBSERVE(5102,"",""),
		ENGINE_VOTE(5103,"",""),
		ENGINE_RAIDBOSSSEARCH_TELEPORT(5104,"",""),
		ENGINE_GM_PANEL(5105,"",""),
		ENGINE_ANTIBOT_ASK(5106,"",""),
		ENGINE_LINK_ANNOUCEMENT_TO_WORLD(5107,"",""),
		ENGINE_TELEPORT(5108,"",""),
		ENFINE_BUFFSTORESEARCH_TELEPORT_PLAYER(5109,"",""),
		ENGINE_BID_HOUSE_CREATE_NEW_BID(5110,"",""),
		ENGINE_BID_HOUSE_CREATE_MAKE_NEW_BID(5111,"",""),
		ENGINE_AUCTION_HOUSE_CREATE_NEW_AUCTION(5112,"",""),
		ENGINE_BID_HOUSE_CANCEL_BID_WITH_BIDDER(5113,"",""),
		ENGINE_BID_HOUSE_CANCEL_MY_OFFERT(5114,"",""),
		ENGINE_DONATION_PART_1(5115,"",""),
		ENGINE_BUFFERCHAR_HEAL(5116,"",""),
		ENGINE_BUFFERCHAR_CANCEL(5117,"",""),
		ENGINE_RAID_BOSS_QUESTION_TO_JOIN(5118,"",""),
		ENGINE_GMCOMMAND_SEND_CODE(5119,"",""),
		ENGINE_GMCOMMAND_RESET_SECONDARY_PASS(5120,"",""),
		ENGINE_GMCOMMAND_RESET_EMAIL(5121,"",""),
		ENGINE_GMCOMMAND_CREATE_AIO_CHAR(5122,"",""),
		ENGINE_GMCOMMAND_CREATE_AIO_CHAR_30(5123,"",""),
		ENGINE_GMCOMMAND_RESET_OLY_SCHEME(5124,"",""),
		ENGINE_GMCOMMAND_RESET_BUFF_SCHEME(5125,"",""),
		ENGINE_GMCOMMAND_RESET_CHAR_CONFIG(5126,"",""),
		ENGINE_GMCOMMAND_RESET_CHAR_DRESSME(5127,"",""),
		ENGINE_RAIDBOSSSEARCH_OBSERVE(5128,"",""),
		ENGINE_DRESSME_PERSONAL_BUY(5129, "", ""),
		ENGINE_DRESSME_EXCLUSIVE_BUY_CLAN(5130, "", ""),
		ENGINE_DRESSME_REMOVE_DRESSME(5131, "", ""),
		ENGINE_DRESSME_REMOVE_ITEM_FROM_DRESSME(5132, "", ""),
		ENGINE_DRESSME_EXCLUSIVE_BUY_CHAR(5133, "", ""),
		ENGINE_BUFFERCHAR_USE_SCHEME(5134, "", ""),
		ENGINE_BUFFERCHAR_USE_YOU_OWN_SCHEME(5135, "", ""),
		ENGINE_DRESSME_PERSONAL_USE(5136, "", ""),
		ENGINE_SUBCLASS_ADD(5137, "", ""),
		ENGINE_SUBCLASS_REMOVE(5138, "", ""),
		ENGINE_BORROW_QUESTION(5139, "", ""),
		INSTANCE_REMOVE_BY_PAY(6000,"",""),
		INSTANCE_OLYMPIAD_ZONE(6001,"",""),
		MAKE_DONATION_CHANGE_CHAR_NAME(7001,"",""),
		MAKE_DONATION_CHANGE_CLAN_NAME(7002,"",""),
		MAKE_DONATION_AIO_CHAR_SIMPLE(8001,"",""),
		MAKE_DONATION_AIO_CHAR_30(8002,"",""),
		MAKE_DONATION_NOBLE_CHAR(8004,"",""),
		MAKE_DONATION_Lv80_CHAR(8005,"",""),
		MAKE_DONATION_CHANGE_SEX(8006,"",""),
		MAKE_DONATION_RECOS(8007,"",""),
		MAKE_DONATION_ENCHANT_ITEM(8008,"",""),
		MAKE_DONATION_ELEMENTAL_ITEM(8009,"","")
		;
		public int _ID;
		public String _ByPass;
		public String _Name;
		private IdDialog(int ID, String ByPass, String Nombre)
		{
			_ID = ID;
			_ByPass = ByPass;
			_Name = Nombre;
		}
		public int getId(){
			return _ID;
		}
	}

	
	@SuppressWarnings("unused")
	private static void makeAction(int IDDialog, int Answer, L2PcInstance player){
		
	}
	
	public static boolean isFromZeuS(int IDDialog, int Answer, L2PcInstance player){
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("From Dlg->IDDialog"+IDDialog+"-Answer->"+Answer);
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.msgbox("From Dlg->IDDialog"+IDDialog+"-Answer->"+Answer, player);
		}
		boolean _isFromThis = isFromThis(IDDialog);
		String ByPass = "";
		if(_isFromThis && Answer== 1){
			switch (IDDialog) {
				case 4000:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Lv85.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4001:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Noble.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4002:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Fame.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4003:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ClanLv.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4004:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.ReducePK.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4005:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.SexChange.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4006:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4007:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.AIONormal30.name() +";normal;0;0;0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 4008:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";" + v_MiscelaniusOption.loadByPass.ChangeCharName.name() + ";normal;" + v_MiscelaniusOption.TEMPORALDATA.get(player.getObjectId()) + ";0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;					
				case 4009:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";" + v_MiscelaniusOption.loadByPass.ChangeClanName.name() + ";normal;" + v_MiscelaniusOption.TEMPORALDATA.get(player.getObjectId()) + ";0;0;0";
					v_MiscelaniusOption.bypass(player, ByPass);
					break;
				case 5000:
					pvpInstance.onDieRevive(player, false);
					break;
				case 5001:
					break;
				case 5002:
					pvpInstance.playerEnterFromAsk(player);
					break;
				case 5100:
					v_Buffer_New.voiceByPass(player, "schRemoveThisScheme 0 0 0 ");
					break;
				case 5101:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name()+";teleport;0;0;0;0;0";
					v_dropsearch.bypass(player, ByPass);
					break;
				case 5102:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name()+";Observe;0;0;0;0;0";
					v_dropsearch.bypass(player, ByPass);
					break;
				case 5103:
					votereward.getVoteWindows(player,false);
					break;
				case 5104:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name()+";teleport;0;0;0;0;0";
					v_RaidBossInfo.bypass(player, ByPass);
					break;
				case 5105:
					C_gmcommand.SendMeToGmConfirm(player);
					break;
				case 5106:
					antibotSystem.checkAnswerByBox(player);
					break;
				case 5107:
					int idToShow = itemLink.getIdAnnoucementToShow(player, ANNOUCEMENT_LINK.get(player.getObjectId()));
					String Msnje = "The Staff Member " + player.getName() + " want to show you this Annoucement/Rulz/Other. Please click this [ANNOUNCE_LINK=" + String.valueOf(idToShow) + "]";
					opera.AnunciarTodos(Msnje);
					break;
				case 5108:
					v_Teleport.beginTeleport(player, null);
					break;
				case 5109:
					sellBuff.sendBuyerToSeller(player);
					break;
				case 5110:
					v_bid_house.setCreationBid(player);
					break;
				case 5111:
					v_bid_house.setMakeaBid(player);
					break;
				case 5112:
					v_auction_house.setCreationAuction(player);
					break;
				case 5113:
					v_bid_house.removeBid(player);
					break;
				case 5114:
					v_bid_house.setCancelMyBid(player, false, -1);
					break;
				case 5115:
					v_donation.ByPassFromDlg(player);
					break;
				case 5116:
					v_Buffer_New.healProcess(player);
					break;
				case 5117:
					v_Buffer_New.cancelProcess(player);
					break;
				case 5118:
					RaidBossEvent.addPlayer(player);
					break;
				case 5119:
					C_gmcommand.sendCode(player);
					break;
				case 5120:
					C_gmcommand.resetSecondaryPass(player);
					break;
				case 5121:
					C_gmcommand.resetEmail(player);
					break;
				case 5122:
					C_gmcommand.setAioChar(player, true);
					break;
				case 5123:
					C_gmcommand.setAioChar(player, false);
					break;
				case 5124:
					C_gmcommand.setResetOlySchemeBuff(player);
					break;
				case 5125:
					C_gmcommand.setResetBuffSchemeConfig(player);
					break;
				case 5126:
					C_gmcommand.setResetCharConfig(player);
					break;
				case 5127:
					C_gmcommand.setResetDressmeSetting(player);
					break;
				case 5128:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name()+";Observe;0;0;0;0;0";
					v_RaidBossInfo.bypass(player, ByPass);
					break;
				case 5129:
					v_Dressme.ApliDressmeFromDlg(player);
					break;
				case 5130:
					v_Dressme.ApliDressmeFromExclusive(player, false);
					break;
				case 5131:
					v_Dressme.removeDressmeID(player);
					break;
				case 5132:
					v_Dressme.removeItemFromDressme(player);
					break;
				case 5133:
					v_Dressme.ApliDressmeFromExclusive(player, true);
					break;
				case 5134:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";SCHEMEBUFF;0;0;0;0;0;0";
					v_Buffer_New.delegar(player, ByPass);
					break;
				case 5135:
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";USE_SCH;0;0;0;0;0;0";
					v_Buffer_New.delegar(player, ByPass);					
					break;
				case 5136:
					v_Dressme.ApliDressmeFromDlg(player);
					break;
				case 5137:
					//ENGINE_SUBCLASS_ADD(5137, "", ""),
					v_subclass.addNewSubClassProcess(player);
					break;
				case 5138:
					//ENGINE_SUBCLASS_REMOVE(5138, "", ""),
					v_subclass.deleteProcess(player);
					break;
				case 5139:
					borrowAccountSystem.getInstance().setPasswordFromDLG(player);
					break;
				case 6000:
					ByPass = "ZeuS instancezone Remove " + String.valueOf(instanceZone.TemporalData.get(player.getObjectId()));
					instanceZone.bypass(player, ByPass);
					break;
				case 6001:
					oly_monument.goToOlympiadZone(player);
					break;
				case 7001://DONATION CHANGE CHAR NAME
					ByPass = "CHANGE_CHAR_NAME " +  v_donation.getTemporalData(player);
					v_donation.bypass_voice(player, ByPass);
					break;
				case 7002://CLAN NAME
					ByPass = "CHANGE_CLAN_NAME " +  v_donation.getTemporalData(player);
					v_donation.bypass_voice(player, ByPass);
					break;					
				case 8007://DONATION RECO
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";donation;255_R;0;0;0;0;0";
					v_donation.bypass(player, ByPass);
					break;
				case 8004://NOBLE
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";donation;NOBLE;0;0;0;0;0";
					v_donation.bypass(player, ByPass);
					break;
				case 8006://SEX
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";donation;SEX;0;0;0;0;0";
					v_donation.bypass(player, ByPass);
					break;
				case 8001://AIO NORMAL
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";donation;AIO_NORMAL;0;0;0;0;0";
					v_donation.bypass(player, ByPass);
					break;
				case 8002://AIO +30
					ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";donation;AIO_30;0;0;0;0;0";
					v_donation.bypass(player, ByPass);
					break;
				case 8008:
					//MAKE_DONATION_ENCHANT_ITEM(8008,"","");
					v_donation.dlgEnchantItem(player);
					break;
				case 8009:
					v_donation.dlgElementalItem(player);
					break;
			}
			
		}else if(_isFromThis && Answer==0){
			switch (IDDialog) {
				case 5000:
					pvpInstance.playerLeave(player);
					break;
				default:
					central.msgbox("Operation was Cancel", player);
			}
		}
		
		return _isFromThis;
	}
	
	private static boolean isFromThis(int ID){
		for(IdDialog EnumDialog : IdDialog.values()){
			if( EnumDialog._ID == ID ){
				return true;
			}
		}
		return false;
	}
	
	public static final Dlg getInstance()
	{
		return _instance;
	}
	
	public static void sendDlg_linkAnnoucement(L2Character player, String Question, int idToLink){
		ANNOUCEMENT_LINK.put(player.getObjectId(), idToLink);
		sendDlg(player, Question, IdDialog.ENGINE_LINK_ANNOUCEMENT_TO_WORLD);
	}
	
	public static void sendDlg(L2Character player, String Question, IdDialog iDD){
		sendDlg(player, Question, iDD, 15);
	}
	public static void sendDlg(L2Character player, String Question, IdDialog iDD, int Seconds){
		final ConfirmDlg confirm = new ConfirmDlg(Question);
		confirm.addTime(Seconds * 1000);
		confirm.addRequesterId(iDD._ID);
		player.sendPacket(confirm);
	}	
}
