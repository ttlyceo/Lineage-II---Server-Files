package ZeuS.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.Anunc;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class _dualBox {
	private static Map<String, _dualBoxClass> PLAYERS_CONNECTIONS = new HashMap<String, _dualBoxClass>();
	private static final Logger _log = Logger.getLogger(_dualBox.class.getName());
	
	
	public static boolean isSecondProgram(L2PcInstance player){
		boolean _return = false;
		if(PLAYERS_CONNECTIONS != null){
			if(PLAYERS_CONNECTIONS.size() > 1){
				String IdConex = getConnectionID(player);
				if(PLAYERS_CONNECTIONS.containsKey(IdConex)){
					return PLAYERS_CONNECTIONS.get(IdConex).isProgram(player);
				}
			}
		}
		return _return;
	}
	
	
	public static void RegisterPlayer(L2PcInstance player){
		if(general.MAX_IP_COUNT<=0 || !general.MAX_IP_CHECK){
			if(!player.getClient().isDetached()) {
				_insertIntoBD(player, "CONNECTED");
			}
			return;
		}
		
		if(player.getClient().isDetached()){
			return;
		}
		
		if(player.isGM()){
			return;
		}
		
		if(canAddMorePlayer(player)){
			AddData(player);
		}else{
			removeFromGame(player);
		}
	}
	
	private static void removeFromGame(L2PcInstance player){
		int SecondToCloseNewAccount = 10;
		central.msgbox_Lado("MAX IP ALLOW " + String.valueOf(general.MAX_IP_COUNT), player);
		String Mensaje = language.getInstance().getMsg(player).MAXIMUM_ALLOWED_IP_ARE_$countAllow_CHAR_$name_ARE_DISCONNECTED.replace("$countAllow", String.valueOf(general.MAX_IP_COUNT)).replace("$name",player.getName());
		Anunc.Anunciar_All_Char_IP(player, Mensaje , "IP CHECK");
		ThreadPoolManager.getInstance().executeGeneral(new closeClientByConnect(SecondToCloseNewAccount * 1000, player));
		_insertIntoBD(player,"DISCONNECTED");
	}
	
	private static void _insertIntoBD(L2PcInstance player, String estado){
		String ip_wan = ZeuS.getIp_Wan(player);
		String ip_lan = ZeuS.getIp_pc(player);
		String SQL_INSERT = "INSERT INTO zeus_connection() values (?,?,?,?,?,NOW(),?)";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(SQL_INSERT);
			psqry.setString(1, ip_wan);
			psqry.setString(2, ip_lan);
			psqry.setString(3, player.getAccountName());
			psqry.setString(4, player.getName());
			psqry.setString(5, estado);
			psqry.setInt(6, player.getObjectId());
			psqry.execute();
		}catch(SQLException a){
			_log.warning("ZeuS - IP Insert Error: "+ a.getMessage() + " " + String.valueOf(a.getErrorCode()));
		}

		try{
			conn.close();
		}catch(Exception a){

		}
	}	
	
	private static String getConnectionID(L2PcInstance player){
		return ZeuS.getIp_Wan(player) + "-" + ZeuS.getIp_pc(player);
	}
	
	private static void addPlayer(L2PcInstance player, String idConnection){
		if(PLAYERS_CONNECTIONS == null) {
			_dualBoxClass temp = new _dualBoxClass(player, idConnection);
			PLAYERS_CONNECTIONS.put(idConnection, temp);
		}else if(PLAYERS_CONNECTIONS.size()==0) {
			_dualBoxClass temp = new _dualBoxClass(player, idConnection);
			PLAYERS_CONNECTIONS.put(idConnection, temp);			
		}else if(PLAYERS_CONNECTIONS.containsKey(idConnection)){
			PLAYERS_CONNECTIONS.get(idConnection).setNewPlayer(player);
		}
		_insertIntoBD(player,"CONNECTED");
	}
	
	private static boolean canAddMorePlayer(L2PcInstance player){
		if(player.isGM() && !general.MAX_IP_CHECK){
			return true;
		}
		
		String idLogIn = getConnectionID(player);
		if(PLAYERS_CONNECTIONS!=null){
			if(PLAYERS_CONNECTIONS.size()>0){
				if(PLAYERS_CONNECTIONS.containsKey(idLogIn)){
					int PlayersIn = PLAYERS_CONNECTIONS.get(idLogIn).getAllPlayer();
					if(PlayersIn >= general.MAX_IP_COUNT){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	private static void AddData(L2PcInstance player){
		String IdConnect = getConnectionID(player);
		addPlayer(player, IdConnect);
	}
}
class _dualBoxClass{
	private final int times_check = 20;
	private final long MILISECOND_TO_CHECK = 800l;
	private Vector<L2PcInstance> PLAYERS_IN = new Vector<L2PcInstance>();
	private Map<Integer, Long> LAST_MOVING = new HashMap<Integer, Long>();
	private Map<String, Integer> TIME_MOVING = new HashMap<String, Integer>(); 
	String ConnectionIDD;
	private static final Logger _log = Logger.getLogger(_dualBoxClass.class.getName());
	
	private int setTimeMoving(boolean reset){
		if(reset){
			try{
				this.TIME_MOVING.remove(this.ConnectionIDD);
				return 0;
			}catch(Exception a){
				
			}
		}
		if(this.TIME_MOVING==null){
			this.TIME_MOVING.put(this.ConnectionIDD, 1);
		}else{
			int Times = TIME_MOVING.get(this.ConnectionIDD)+1;
			this.TIME_MOVING.put(this.ConnectionIDD, Times);
		}
		return this.TIME_MOVING.get(this.ConnectionIDD);
	}

	@SuppressWarnings("rawtypes")
	private boolean HaveSecondProgram(){
		boolean _return = false;
		Iterator itr = this.LAST_MOVING.entrySet().iterator();
		Long TimeCheck = 0l;
		while(itr.hasNext()){
			Map.Entry _Info =(Map.Entry)itr.next();
			long checking = (long)_Info.getValue();
			long Diff = checking - TimeCheck;
			if(Diff < this.MILISECOND_TO_CHECK && Diff > 0){
				if(this.setTimeMoving(false) >= times_check){
					this.setTimeMoving(true);
					_return = true;
				}
			}
		}
		return _return;
	}
	
	public boolean isProgram(L2PcInstance ppl){
		boolean _return = false;
		long RealUnix = opera.getUnixTimeL2JServer();
		
		if(LAST_MOVING == null){
			LAST_MOVING.put(ppl.getObjectId(), RealUnix);
		}else{
			LAST_MOVING.put(ppl.getObjectId(), RealUnix);
			if(LAST_MOVING.size()>1){
				if(HaveSecondProgram()){
					_log.warning("Second Program Detected... Need new Command");
				}
			}
		}
		return _return;
	}
	
	
	private String getConnectionID(L2PcInstance player){
		return ZeuS.getIp_Wan(player) + "-" + ZeuS.getIp_pc(player);
	}
	
	public _dualBoxClass(L2PcInstance player, String ConexID){
		this.ConnectionIDD = ConexID;
		this.PLAYERS_IN.add(player);
	}
	public void setNewPlayer(L2PcInstance player){
		if(this.PLAYERS_IN != null){
			if(!this.PLAYERS_IN.contains(player)){
				this.PLAYERS_IN.add(player);
			}
		}else{
			try{
				this.checkAll();
			}catch(Exception a){
				
			}
			this.PLAYERS_IN.add(player);
		}
	}
	
	public int getAllPlayer(){
		try{
			this.checkAll();
		}catch(Exception a){
			
		}
		try{
			return this.PLAYERS_IN.size();
		}catch(Exception a){
			
		}
		return 0;
	}
	
	public void checkAll(){
		if(this.PLAYERS_IN==null){
			return;
		}
		
		if(this.PLAYERS_IN.size()==0){
			return;
		}
		
		Vector<L2PcInstance>ToRemove = new Vector<L2PcInstance>();
		
		for(L2PcInstance ppl : this.PLAYERS_IN){
			if(!opera.isCharOnline(ppl)){
				ToRemove.add(ppl);
			}else{
				String ConexID = this.getConnectionID(ppl);
				if(!ConexID.equalsIgnoreCase(this.ConnectionIDD)){
					ToRemove.add(ppl);
				}
			}
		}
		
		if(ToRemove!=null){
			if(ToRemove.size()>0){
				for(L2PcInstance ppl: ToRemove){
					this.PLAYERS_IN.remove(ppl);
				}
			}
		}
		
	}
}

class closeClientByConnect implements Runnable{
	private int _time;
	L2Character _player;
	private final Logger _log = Logger.getLogger(closeClientByConnect.class.getName());
	public closeClientByConnect (int time, L2PcInstance player){
		_time = time;
		_player = player;
	}

	@Override
	public void run(){
		if (_time > 0){
			ThreadPoolManager.getInstance().scheduleGeneral(this, 1000);
			_time-=1000;
		}

		if (_time==0){
			if(_player!=null){
				L2PcInstance cha = (L2PcInstance)_player;
				if(cha.isOnline()){
					try{
						_log.info("Player " + _player.getName() + " is disconected by the Dualbox Check System" );
					}catch(Exception a){
						
					}
					try{
						cha.logout();
					}catch(Exception a){

					}
					try{
						cha.getClient().closeNow();
					}catch(Exception a){

					}
				}
			}
		}
	}	
}
