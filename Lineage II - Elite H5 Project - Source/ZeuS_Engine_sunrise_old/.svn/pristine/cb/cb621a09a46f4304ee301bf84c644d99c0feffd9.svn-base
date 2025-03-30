package ZeuS.interfase;

import java.util.HashMap;
import java.util.Map;

import l2r.gameserver.model.actor.instance.L2PcInstance;

public class pkHunt {
	private static Map<L2PcInstance, Integer> PLAYER_WAIT = new HashMap<L2PcInstance, Integer>();
	private static Map<String, Integer> PLAYER_WAIT_NAME = new HashMap<String, Integer>();
	public static boolean isInWaitToHunt(String player){
		boolean retorno = false;
		if(PLAYER_WAIT_NAME != null){
			return PLAYER_WAIT_NAME.containsKey(player);
		}
		return retorno;
	}
	
	public static int getKeyPlayer(L2PcInstance player){
		if(PLAYER_WAIT==null){
			return 0;
		}
		if(PLAYER_WAIT.containsKey(player)){
			return PLAYER_WAIT.get(player);
		}
		if(PLAYER_WAIT_NAME==null){
			return 0;
		}
		if(PLAYER_WAIT_NAME.containsKey(player.getName())){
			PLAYER_WAIT.put(player,PLAYER_WAIT_NAME.get(player.getName()));
			return PLAYER_WAIT.get(player);
		}
		return 0;
		
	}
	
	public static boolean isInWaitToHunt(L2PcInstance player){
		boolean retorno = false;
		if(PLAYER_WAIT != null){
			return PLAYER_WAIT.containsKey(player);
		}
		return retorno;
	}
	
	public static void setPKHunt(L2PcInstance player){
		if(!isInWaitToHunt(player) && !isInWaitToHunt(player.getName())){
			
		}
	}
	
	public static class CountDown_PK_HUNT implements Runnable{
		L2PcInstance activeChar;
		int activeKey;
		public CountDown_PK_HUNT(L2PcInstance player, int Key){
			activeChar = player;
			activeKey = Key;
		}
		@Override
		public void run(){
			int KeyActual = getKeyPlayer(activeChar);
			if(KeyActual == activeKey){
				
			}
		}
	}	
	
}
