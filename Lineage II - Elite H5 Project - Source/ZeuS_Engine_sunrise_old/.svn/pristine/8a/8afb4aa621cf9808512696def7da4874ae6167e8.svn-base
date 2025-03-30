package ZeuS.procedimientos;

import gr.sr.interf.SunriseEvents;

import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class Afk {
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(Afk.class.getName());
	private static Vector<L2PcInstance> AFK_PLAYER = new Vector<L2PcInstance>();
	
	public static boolean isAFK(L2PcInstance player){
		if(AFK_PLAYER!=null){
			return AFK_PLAYER.contains(player);
		}
		return false;
	}
	public static boolean setAFK(L2PcInstance player){
		boolean isAFK = false;
		if(AFK_PLAYER != null){
			if(AFK_PLAYER.contains(player)){
				isAFK = true;
			}
		}
		
		if(isAFK){
			player.standUp();
			if(!player.isGM()){
				player.setIsInvul(false);
			}
			AFK_PLAYER.remove(player);
			return false;
		}
		
		if(player.isDead()){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_DEATH, player);
			return false;
		}
		
		if(player.isInCombat()){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_COMBAT_MODE, player);
			return false;
		}
		
		if(player.getPvpFlag()>0){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_FLAG, player);
			return false;
		}
		
		if(player.getKarma()>0){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_FLAG, player);
			return false;			
		}
		
		if(player.isInSiege() || player.isInsideZone(ZoneIdType.CASTLE) || player.isInsideZone(ZoneIdType.SIEGE) || player.isInsideZone(ZoneIdType.FORT) ){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_SIEGE_CASTLE, player);
			return false;
		}
		
		if(player.getInstanceId()>0 || player.isInsideZone(ZoneIdType.PVP) || player.isInsideZone(ZoneIdType.JAIL) || player.isInsideZone(ZoneIdType.QUEEN_ANT) || RaidBossEvent.isPlayerOnRBEvent(player) || SunriseEvents.isInEvent(player)  ){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_EVENT_PVP_ZONE, player);
			return false;
		}
		
		if(player.isInBoat() || player.isInCraftMode() || player.isInDuel() || player.isInvisible() || player.isInStoreMode()){
			central.msgbox(language.getInstance().getMsg(player).AFK_YOU_CAN_AFK_WHILE_EVENT_PVP_ZONE, player);
			return false;
		}
		
		return true;
	}
}
