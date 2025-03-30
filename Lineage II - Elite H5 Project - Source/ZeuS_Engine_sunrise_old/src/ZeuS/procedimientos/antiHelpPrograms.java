package ZeuS.procedimientos;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class antiHelpPrograms {
	// <IPWAN / IPLAN , COUNT>
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(antiHelpPrograms.class.getName());
	private static Map<String, _dualdata> DUAL_PLAYERS = new HashMap<String, _dualdata>();
	private static String getIpID(L2PcInstance player){
		String IP_LAN = ZeuS.getIp_pc(player);
		String IP_WAN = ZeuS.getIp_Wan(player);
		return IP_LAN + "-" + IP_WAN;
	}
	public static boolean isProgram(L2PcInstance player){
		return DUAL_PLAYERS.get(getIpID(player)).isProgram(player);
	}
}

class _dualdata{
	private Vector<L2PcInstance> _PLAYERS = new Vector<L2PcInstance>();
	private long _LastActivity = 0;
	@SuppressWarnings("unused")
	private String _IP_LAN = "";
	@SuppressWarnings("unused")
	private String _IP_WAN = "";
	private int lastHitPlayerID = 0;
	public _dualdata(L2PcInstance player){
		this._PLAYERS.add(player);
		this._IP_LAN = ZeuS.getIp_pc(player);
		this._IP_WAN = ZeuS.getIp_Wan(player);
	}
	public void setNewPlayer(L2PcInstance player){
		checkAnRemove();
		this._PLAYERS.add(player);
	}
	public boolean isProgram(L2PcInstance player){
		long NowAct = opera.getUnixTimeL2JServer();
		if(this._LastActivity==0){
			this._LastActivity = NowAct;
			this.lastHitPlayerID = player.getObjectId();
		}
		if((NowAct - this._LastActivity) < 1000){
			if(this.lastHitPlayerID != player.getObjectId()){
				central.msgbox("Dual Box Program Detedted.", player);
				Punish(player);
				return true;
			}
		}else{
			this.lastHitPlayerID = player.getObjectId();
			this._LastActivity = NowAct;
		}
		return false;
	}
	
	private void checkAnRemove(){
		Vector<L2PcInstance> ToRemove = new Vector<L2PcInstance>();
		for(L2PcInstance pp :this._PLAYERS){
			if(pp==null){
				ToRemove.add(pp);
			}else if(!pp.isOnline()){
				ToRemove.add(pp);
			}else if(pp.getClient().isDetached()){
				ToRemove.add(pp);
			}
		}
		for(L2PcInstance toRem : ToRemove){
			this._PLAYERS.remove(toRem);
		}
	}
	
	private void Punish(L2PcInstance player){
		
	}
	
	
}