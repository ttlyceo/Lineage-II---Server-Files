package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.Anunc;

public class ipGuard {
	private final static Map<String, Vector<String>> IPConecc = new HashMap<String, Vector<String>>();
	private static final String SQL_INSERT = "INSERT INTO zeus_connection() values (?,?,?,?,?,NOW(),?)";
	private static Logger _log = Logger.getLogger(ipGuard.class.getName());

	private static String getHashCode (L2PcInstance player){
		String ipWAN = ZeuS.getIp_Wan(player);
		String ipLAN = ZeuS.getIp_pc(player);
		String HashCod = ipWAN + "-" + ipLAN;
		return HashCod;
	}

	public static boolean setRegIPGuard(L2PcInstance player){

		insertIntoBD(player,"CONNECTED");

		if(!general.MAX_IP_CHECK){
			return true;
		}
		boolean retorno = true;
		checkCleanIPs();
		int countLog = getAllCharOnConecc(player);
		int MaxIpPermitidas = general.MAX_IP_COUNT;
		if(general.isPremium(player, true) || general.isPremium(player, false)){
			MaxIpPermitidas = general.MAX_IP_VIP_COUNT;
		}

		if(countLog > MaxIpPermitidas){
			central.msgbox("MAX IP ALLOW " + String.valueOf(MaxIpPermitidas), player);
			Anunc.Anunciar_All_Char_IP(player, language.getInstance().getMsg(player).MAXIMUM_ALLOWED_IP_ARE_$countAllow_CHAR_$name_ARE_DISCONNECTED.replace("$countAllow", String.valueOf(general.MAX_IP_COUNT)).replace("$player",player.getName()), "IP CHECK");
			ThreadPoolManager.getInstance().executeGeneral(new closeClient(10 * 1000, player));
			insertIntoBD(player,"DISCONNECTED");
			return false;
		}

		String hashCode = getHashCode(player);
		
		checkCleanIPFromPlayer(hashCode);
		
		if(IPConecc == null){
			IPConecc.put(hashCode, new Vector<String>());
		}else{
			if(IPConecc.containsKey(hashCode)){
				IPConecc.get(hashCode).add(player.getName());
			}else{
				IPConecc.put(hashCode, new Vector<String>());
				IPConecc.get(hashCode).add(player.getName());
			}
		}

		insertIntoBD(player,"CONNECTED");
		return retorno;
	}

	private static void insertIntoBD(L2PcInstance player, String estado){

	/*	if(!general.MAX_IP_RECORD_DATA){
			return;
		}
*/
		String ip_wan = ZeuS.getIp_Wan(player);
		String ip_lan = ZeuS.getIp_pc(player);
		Connection conn = null;
		PreparedStatement psqry = null;
		//INSERT INTO zeus_connection() values (?,?,?,?,?,NOW(),?)
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

	private static int getAllCharOnConecc (L2PcInstance player){
		int retorno = 0;
		checkCleanIPs();
		if(IPConecc != null){
			String hashBus = getHashCode(player);
			if(IPConecc.containsKey(hashBus)){
				retorno = IPConecc.get(hashBus).size();
			}
		}

		return retorno;
	}
	
	private static void checkCleanIPFromPlayer(String HashLink){
		if(IPConecc!=null){
			if(IPConecc.containsKey(HashLink)){
				try{
					for(String NomChar : IPConecc.get(HashLink)){
						try{
							L2PcInstance playerTemp = opera.getPlayerbyName(NomChar);
							if(playerTemp==null){
								IPConecc.get(HashLink).remove(NomChar);
							}else if(!playerTemp.isOnline()){
								IPConecc.get(HashLink).remove(NomChar);
							}else if(playerTemp.isOnline() && playerTemp.getClient().isDetached()){
								IPConecc.get(HashLink).remove(NomChar);
							}
						}catch(Exception e){
							
						}
					}
				}catch(Exception a){
					
				}
			}
			try{
				if(IPConecc.containsKey(HashLink)){
					if(IPConecc.get(HashLink).size()<=0){
						IPConecc.remove(HashLink);
					}
				}
			}catch(Exception a){
				
			}
		}
	}

	@SuppressWarnings("rawtypes")
	private static void checkCleanIPs(){
		try{
			if(IPConecc!=null){
				Iterator itr = IPConecc.entrySet().iterator();
			    while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	Vector<String>NomChar = IPConecc.get(Entrada.getKey());
			    	if(NomChar != null){
			    		try{
				    		for(String nomChars : NomChar){
				    			L2PcInstance chaTemp = opera.getPlayerbyName(nomChars);
				    			if(chaTemp!=null){
				    				if(chaTemp.isOnline() && !chaTemp.getClient().isDetached()){
				    					if(!getHashCode(chaTemp).equals(Entrada.getKey().toString())){
				    						IPConecc.get(Entrada.getKey()).remove(nomChars);
				    					}
				    				}else{
				    					IPConecc.get(Entrada.getKey()).remove(nomChars);
				    				}
				    			}else{
				    				IPConecc.get(Entrada.getKey()).remove(nomChars);
				    			}
				    			if(IPConecc.containsKey(Entrada.getKey())){
				    				if(IPConecc.get(Entrada.getKey()).size()<=0){
				    					try{
				    						IPConecc.remove(Entrada.getKey());
				    					}catch(Exception e){
				    						
				    					}
				    				}
				    			}
				    		}
			    		}catch(Exception a){
			    			
			    		}
			    	}
			    }
			}
		}catch(Exception a){

		}
	}
}
class closeClient implements Runnable{
	private int _time;
	L2Character _player;

	public closeClient (int time, L2PcInstance player){
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
