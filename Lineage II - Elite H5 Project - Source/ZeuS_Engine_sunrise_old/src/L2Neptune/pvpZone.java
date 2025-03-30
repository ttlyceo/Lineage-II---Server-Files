package L2Neptune;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import gr.sr.interf.SunriseEvents;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.network.serverpackets.ExAirShipInfo;

public class pvpZone {
	private static final Logger _log = Logger.getLogger(pvpZone.class.getName());
	private static int ID_ZONE = -1;
	private static Instance pvpZoneData=null;
	private static List<Integer> PLAYER_INSIDE = new ArrayList<Integer>();
	
	public static int getIdZone() {
		return ID_ZONE;
	}
	

	public static Instance getPvPZoneData() {
		return pvpZoneData;
	}
	
	public static void createZoneInstance(){
		if(pvpZoneData != null)
			return;
		try{
			int IdTemp = InstanceManager.getInstance().createDynamicInstance("zeus/l2neptune-zone.xml");
			ID_ZONE = IdTemp;
			pvpZoneData = InstanceManager.getInstance().getInstance(ID_ZONE);

		}catch(Exception a){
			ID_ZONE = -1;
			_log.warning("ZeuS Error -> olympiad-zone.xml File not Exist("+ a.getMessage() +")");
		}
	}
	
	public static boolean wasInside(L2PcInstance player) {
		if(PLAYER_INSIDE != null) {
			if(PLAYER_INSIDE.size() > 0) {
				return PLAYER_INSIDE.contains(player.getObjectId());
			}
		}
		return false;
	}
	
	public static void removePlayerInside(L2PcInstance player) {
		if(PLAYER_INSIDE != null) {
			if(PLAYER_INSIDE.size() > 0) {
				PLAYER_INSIDE.remove(Integer.valueOf(player.getObjectId()));
			}
		}
	}
	
	public static void revive(L2PcInstance player) {
		player.updatePvPFlag(1);
		PLAYER_INSIDE.add(player.getObjectId());		
	}
	
	public static boolean isInZone(L2PcInstance player) {
		return player.getInstanceId() == ID_ZONE;
	}
	
	public static void onEnterInstance(L2PcInstance player, Location loc) {
		loc.setInstanceId(ID_ZONE);
		player.teleToLocation(loc);
		player.updatePvPFlag(1);
		PLAYER_INSIDE.add(player.getObjectId());
	}
	
	public static void onExitInstance(L2PcInstance player) {
		player.updatePvPFlag(0);
		player.setInstanceId(0);
	}

	public static void enterProcess(L2PcInstance player, String Parametros) {
		createZoneInstance();
		int locX;
		int locY;
		int locZ;
		try {
			locX = Integer.valueOf(Parametros.split(" ")[0]);
		}catch(Exception a) {
			_log.warning("ZeuS: L2Neptune Zone: Cant pass this Param to Numeric (X): " + Parametros.split(" ")[0]);
			return;
		}
		try {
			locY = Integer.valueOf(Parametros.split(" ")[1]);
		}catch(Exception a) {
			_log.warning("ZeuS: L2Neptune Zone: Cant pass this Param to Numeric (Y): " + Parametros.split(" ")[1]);
			return;
		}
		
		try {
			locZ = Integer.valueOf(Parametros.split(" ")[2]);
		}catch(Exception a) {
			_log.warning("ZeuS: L2Neptune Zone: Cant pass this Param to Numeric (Y): " + Parametros.split(" ")[1]);
			return;
		}
		
		Location LocEnter = new Location(locX, locY, locZ);
		LocEnter.setInstanceId(ID_ZONE);
		
		if(player.isInOlympiadMode() || player.isInOlympiad()) {
			central.msgbox(language.getInstance().getMsg(player).YOUR_CANT_JOIN_THIS_EVENT_BECOUSE_OLYS, player);
			return;
		}
		if(player.isOnEvent() || SunriseEvents.isInEvent(player) || RaidBossEvent.isPlayerOnRBEvent(player)) {
			central.msgbox(language.getInstance().getMsg(player).YOUR_ARE_IN_OTHER_EVENT , player);
			return;
		}
		if(player.isJailed()) {
			return;
		}
		player.teleToLocation(LocEnter,ID_ZONE,0);
		player.updatePvPFlag(1);
		player.setInstanceId(ID_ZONE);
		PLAYER_INSIDE.add(player.getObjectId());
	}
	
}
