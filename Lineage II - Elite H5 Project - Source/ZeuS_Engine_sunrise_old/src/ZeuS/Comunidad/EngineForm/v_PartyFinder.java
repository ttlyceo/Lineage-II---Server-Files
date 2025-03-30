package ZeuS.Comunidad.EngineForm;

import gr.sr.interf.SunriseEvents;

import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_PartyFinder{
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(v_PartyFinder.class.getName());
	
	private static String mainHtml(L2PcInstance player, String params, String ByPassAnterior){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-go-party-leader.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%COST%", cbFormato.getCostOfService(general.PARTY_FINDER_PRICE));
		Vector<String>Requerimientos = new Vector<String>();
		Requerimientos.add("For Nobles Only," + String.valueOf(general.PARTY_FINDER_CAN_USE_ONLY_NOBLE));
		Requerimientos.add("Minimun Level to Use," + String.valueOf(general.PARTY_FINDER_CAN_USE_LVL));
		Requerimientos.add("Use in Flag mode," + String.valueOf(general.PARTY_FINDER_CAN_USE_FLAG));
		Requerimientos.add("Party Leader Noble," + String.valueOf(general.PARTY_FINDER_GO_LEADER_NOBLE));
		Requerimientos.add("Party Leader Flag/PK," + String.valueOf(general.PARTY_FINDER_GO_LEADER_FLAGPK));
		html.replace("%REQUIREMENTS%", cbFormato.getRequirements(Requerimientos));

		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_PARTYFINDER) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}			
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,"");
		}else if(parm1.equals("go")){
			if(opera.canUseCBFunction(player)){
				if(canUse(player)){
					opera.removeItem(general.PARTY_FINDER_PRICE, player);
					goPartyLeader(player);
					cbFormato.cerrarCB(player);
				}
			}
		}else if(parm1.equals("FromMain")){
			return mainHtml(player,parm1,"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC());
		}
		return "";
	}
	protected static boolean canUse(L2PcInstance player){
		if(!general._activated()){
			return false;
		}
		
		if(player.inObserverMode()){
			central.msgbox(language.getInstance().getMsg(player).GPL_PARTY_LEADER_IN_OBSERVE_MODE, player);
			return false;
		}
		
		if(SunriseEvents.isInEvent(player)){
			central.msgbox(language.getInstance().getMsg(player).GPL_PARTY_LEADER_IN_EVENT, player);
			return false;
		}
		
		if(!player.isInParty()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_IN_PARTY, player);
			return false;
		}
		
		if(player.getParty().getLeader().getObjectId() == player.getObjectId()){
			central.msgbox(language.getInstance().getMsg(player).GPL_PARTY_LEADER_ARE_YOU, player);
			return false;
		}
		
		
		if(general.PARTY_FINDER_CAN_USE_ONLY_NOBLE && !player.isNoble()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE , player);
			return false;
		}
		
		
		if(!general.PARTY_FINDER_CAN_USE_FLAG && (player.getPvpFlag() > 0) ){
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG, player);
			return false;
		}
		if(general.PARTY_FINDER_CAN_USE_LVL > player.getLevel()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.PARTY_FINDER_CAN_USE_LVL)) , player);
			return false;
		}

		L2PcInstance PartyLeader = player.getParty().getLeader();
		
		if(PartyLeader.isInsideZone(ZoneIdType.NO_ZEUS)){
			central.msgbox(language.getInstance().getMsg(player).GPL_PARTY_LEADER_IN_RESTRICTED_AREA, player);
			return false;			
		}
		
		if(general.PARTY_FINDER_GO_LEADER_NOBLE && !PartyLeader.isNoble()){
			central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_NO_PARTY_LEADER_NOBLE, player);
			return false;
		}

		if(RaidBossEvent.isPlayerOnRBEvent(PartyLeader)){
			central.msgbox(language.getInstance().getMsg(player).GPL_PARTY_LEADER_IN_EVENT, player);
			return false;			
		}
		

		if(!general.PARTY_FINDER_GO_LEADER_ON_ASEDIO){
			if(PartyLeader.isInsideZone(ZoneIdType.SIEGE)){
				central.msgbox(language.getInstance().getMsg(player).GPL_PARTY_LEADER_IS_IN_SIEGE, player);
				return false;
			}
		}

		if(general.PARTY_FINDER_USE_NO_SUMMON_RULEZ){
			if(PartyLeader.isInsideZone(ZoneIdType.NO_SUMMON_FRIEND)){
				central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_NO_PARTY_NO_SUMMON_ZONE, player);
				return false;
			}
		}

		if(!general.PARTY_FINDER_GO_LEADER_DEATH && PartyLeader.isDead()){
			central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_NO_PARTY_LEADER_DEATH, player);
			return false;
		}
		if(general.PARTY_FINDER_GO_LEADER_FLAGPK && ( (PartyLeader.getKarma()>0) || (PartyLeader.getPvpFlag()!=0) )){
			central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_NO_PARTY_LEADER_FLAG, player);
			return false;
		}
		if(general.PARTY_FINDER_GO_LEADER_NOBLE && !PartyLeader.isNoble()){
			central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_NO_PARTY_LEADER_NOBLE, player);
			return false;
		}

		if(!general.PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE && PartyLeader.isIn7sDungeon()){
			central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_NO_PARTY_LEADER_INSTANCE_ZONE, player);
			return false;
		}

		if(!opera.haveItem(player, general.PARTY_FINDER_PRICE)){
			return false;
		}

		return true;
	}
	
	public static boolean goPartyLeader(L2PcInstance player){

		L2PcInstance PartyLeader = player.getParty().getLeader();

		int xx = PartyLeader.getX();
		int yy = PartyLeader.getY();
		int zz = PartyLeader.getZ();

		player.teleToLocation(xx,yy,zz);

		central.msgbox(language.getInstance().getMsg(player).PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER, player);
		central.msgbox(language.getInstance().getMsg(PartyLeader).PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION.replace("$name", player.getName()), PartyLeader);

		return true;
	}	
	
}
