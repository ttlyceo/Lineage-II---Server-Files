package ZeuS.procedimientos;

import java.util.HashMap;
import java.util.Map;

import l2r.gameserver.enums.MessageType;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.L2EffectType;
import l2r.gameserver.network.serverpackets.ObservationMode;

public class ObserveMode{
	
	private static Map<L2PcInstance, Boolean> PlayerIsInObserveMode = new HashMap<L2PcInstance, Boolean>();
	
	public static void setObserveMode(L2PcInstance player,boolean p1){
		PlayerIsInObserveMode.put(player, p1);
	}
	
	public static boolean isInObserveMode(L2PcInstance player){
		if(PlayerIsInObserveMode!=null){
			if(PlayerIsInObserveMode.containsKey(player)){
				return PlayerIsInObserveMode.get(player);
			}
		}
		return false;
	}
	public static void EnterObserveMode(L2PcInstance player, Location loc){
		if (player.hasSummon())
		{
			player.getSummon().unSummon(player);
		}
		
		player.stopEffects(L2EffectType.HIDE);
		
		
		if (!player.getCubics().isEmpty())
		{
			for (int cubic : player.getCubics().keySet() )
			{
				player.getCubics().remove(cubic);
			}
			player.getCubics().clear();
		}
		
		if (player.getParty() != null)
		{
			player.getParty().removePartyMember(player, MessageType.Expelled);
		}
		
		if (player.isSitting())
		{
			player.standUp();
		}
		
		player.setLastLocation();
		player._setObserveMode(true);
		player.setTarget(null);
		player.setIsParalyzed(true);
		player.startParalyze();
		player.setIsInvul(true);
		player.setInvisible(true);
		player.sendPacket(new ObservationMode(loc));
		player.teleToLocation(loc, false);
		player.broadcastUserInfo();
		player._isChatBlock = true;
	}
}
