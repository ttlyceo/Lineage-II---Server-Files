package ZeuS.event;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.Dice;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.gameserver.util.Broadcast;
import l2r.util.Rnd;

public class DiceEvent {
	private static Map<L2PcInstance, Integer> LastNumber = new HashMap<L2PcInstance, Integer>();
	private Vector<Integer> KILL_NUMBERS = new Vector<Integer>();
	
	public static void diceLaunch(L2PcInstance player) {
		if (!player.isPlayer())
		{
			return;
		}
		if (player.isInOlympiadMode())
		{
			return;
		}
		if(player.isInStoreMode()) {
			return;
		}
		
		int number = rollDice(player);
		if (number == 0){
			return;
		}
		
		Broadcast.toSelfAndKnownPlayers(player, new Dice(player.getObjectId(), 4628, number, player.getX() - 30, player.getY() - 30, player.getZ()));
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_ROLLED_S2);
		sm.addString(player.getName());
		sm.addInt(number);
		
		player.sendPacket(sm);
		Broadcast.toKnownPlayers(player, sm);
		
		if(isKillNumber(number)) {
			killPlayer(player);
		}
		
	}
	
	private static void killPlayer(L2PcInstance player) {
		player.stopAllEffects();
		player.reduceCurrentHp(player.getMaxHp() + player.getMaxCp() + 1, null, null);		
	}
	
	private static boolean isKillNumber(int Number) {
		if(Number == 4) {
			return true;
		}else {
			return false;
		}
	}
	
	private static int rollDice(L2PcInstance player){
		boolean IsOK = false;
		int rndNumber = 0;
		while(!IsOK) {
			rndNumber = Rnd.get(1,6);
			if(LastNumber!=null) {
				if(LastNumber.size()>0) {
					if(LastNumber.containsKey(player)) {
						if(LastNumber.get(player) != rndNumber) {
							IsOK = true;
						}
					}else {
						IsOK = true;						
					}
				}else {
					IsOK = true;
				}
			}else {
				IsOK = true;
			}
		}
		LastNumber.put(player, rndNumber);
		return rndNumber;
	}
	
}
