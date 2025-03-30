package l2r.gabriel.utils;

import L2Neptune.pvpZone;
import ZeuS.Instances.pvpInstance;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.MagicSkillUse;

/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */

public class GabrielUtils {
	public static void handleLeaveCatacombInstance(L2PcInstance player) {	
		if(player.getInstanceId() != 0) {
			player.setInstanceId(0);
			Location loc = new Location(83195,147683,-3464, 0,0);
			//player.setInstanceId(0);
			//player.teleToLocation(83195,147683,-3464);
			player.setXYZ(loc);
			pvpZone.removePlayerInside(player);
			pvpInstance.playerLeave(player);
		}
	}

	public static void handlePackage(L2PcInstance activeChar, int skillId){
		for(L2Character object : activeChar.getKnownList().getKnownPlayers().values()) {
			object.broadcastPacket(new MagicSkillUse(activeChar, skillId, 1, 3000, 0));
		}
	}

}
