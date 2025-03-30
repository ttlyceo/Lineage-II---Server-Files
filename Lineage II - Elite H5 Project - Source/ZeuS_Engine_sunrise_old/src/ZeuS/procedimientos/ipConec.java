package ZeuS.procedimientos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.server.Anunc;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class ipConec {

	private static Map<String, HashMap<String, L2PcInstance>> IP_ACCOUNT_DATA = new HashMap<String, HashMap<String, L2PcInstance>>();
	private static int SecondToCloseNewAccount = 10;
	private static final Logger _log = Logger.getLogger(IPConection.class.getName());
	private static final String SQL_INSERT = "INSERT INTO zeus_connection() values (?,?,?,?,?,NOW(),?)";	
	
	private static String getID_CONNECT(L2PcInstance player){
		String IP_LAN = ZeuS.getIp_pc(player);
		String IP_WAN = ZeuS.getIp_Wan(player);
		String ID_CONNECT = IP_WAN + IP_LAN;
		return ID_CONNECT;
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public static void cleanBD(L2PcInstance player){
		if(IP_ACCOUNT_DATA!=null){
			String IdCon = getID_CONNECT(player);
			String AccountName ="";
			int Etapa=0;
			if(IP_ACCOUNT_DATA.containsKey(IdCon)){
				Map<String, L2PcInstance> DATA_ACC = IP_ACCOUNT_DATA.get(IdCon);
				Iterator itr = DATA_ACC.entrySet().iterator();
			    while(itr.hasNext()){
			    	try{
				    	Map.Entry Entrada = (Map.Entry)itr.next();
				    	L2PcInstance ppl = (L2PcInstance) Entrada.getValue();
				    	AccountName = (String)Entrada.getKey();
				    	if(ppl==null){
				    		Etapa=1;
				    		DATA_ACC.remove(AccountName);
				    	}else if(ppl.getClient().isDetached()){
				    		Etapa=2;
				    		DATA_ACC.remove(AccountName);
				    	}else if(ppl.isGM()){
				    		Etapa=3;
				    		DATA_ACC.remove(AccountName);
				    	}
					}catch(Exception a){
						//_log.warning("Error on clean->("+ Etapa +")" + a.getMessage() + ", Account Name to Clean->" + AccountName);
					}
			    	//_log.warning("Largo despues de la Limpiada->" + DATA_ACC.size());
			    }				
			}
		}
	}
	
	@SuppressWarnings({ "null", "unused" })
	public static void canLogIn(L2PcInstance player){
		cleanBD(player);
		String IdCon = getID_CONNECT(player);
		if(IP_ACCOUNT_DATA == null){
			IP_ACCOUNT_DATA.put(IdCon, new HashMap<String, L2PcInstance>());
		}else if(IP_ACCOUNT_DATA.size()==0 || !IP_ACCOUNT_DATA.containsKey(IdCon)){
			IP_ACCOUNT_DATA.put(IdCon, new HashMap<String, L2PcInstance>());
		}
		
		Map<String, L2PcInstance> DATA_ACC = new HashMap<String, L2PcInstance>();
		DATA_ACC = IP_ACCOUNT_DATA.get(IdCon);

		if(DATA_ACC == null){
			DATA_ACC.put(player.getAccountName(), player);
		}else if(DATA_ACC.size() == 0 || !DATA_ACC.containsKey(player.getAccountName())){
			DATA_ACC.put(player.getAccountName(), player);
		}
		
		if(DATA_ACC.containsKey(player.getAccountName())){
		    int Cantidad = DATA_ACC.size();
		    int MaxIP = getMaxIp(player);
		    if((Cantidad > general.MAX_IP_COUNT) && general.MAX_IP_CHECK){
		    	central.msgbox("MAX IP ALLOW " + String.valueOf(general.MAX_IP_COUNT), player);
				Anunc.Anunciar_All_Char_IP(player, language.getInstance().getMsg(player).MAXIMUM_ALLOWED_IP_ARE_$countAllow_CHAR_$name_ARE_DISCONNECTED.replace("$countAllow", String.valueOf(general.MAX_IP_COUNT)).replace("$player",player.getName()), "IP CHECK");
				ThreadPoolManager.getInstance().executeGeneral(new closeClientByConnect(SecondToCloseNewAccount * 1000, player));
				insertIntoBD(player,"DISCONNECTED");
				return;		    	
		    }
		}
	    DATA_ACC.put(player.getAccountName(), player);
	    //_log.warning("Largo->" + DATA_ACC.size());
	    insertIntoBD(player,"CONNECTED");		
	    return;		
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	private static int getMaxIp(L2PcInstance player){
		String IdCon = getID_CONNECT(player);
		int Maximo = general.MAX_IP_COUNT;
		boolean Conti = true;
		if(IP_ACCOUNT_DATA!=null){
			String AccountName ="";
			int Etapa=0;
			if(IP_ACCOUNT_DATA.containsKey(IdCon)){
				Map<String, L2PcInstance> DATA_ACC = IP_ACCOUNT_DATA.get(IdCon);
				Iterator itr = DATA_ACC.entrySet().iterator();
			    while(itr.hasNext() && Conti){
			    	try{
				    	Map.Entry Entrada = (Map.Entry)itr.next();
				    	L2PcInstance ppl = (L2PcInstance) Entrada.getValue();
				    	AccountName = (String)Entrada.getKey();
				    	if(ppl!=null){
				    		if(!ppl.getClient().isDetached()){
				    			if(ZeuS.isPremium(player)){
				    				Conti=false;
				    				Maximo = general.MAX_IP_VIP_COUNT;
				    			}
				    		}
				    	}
					}catch(Exception a){
					}
			    }				
			}
		}		
		
		
		return Maximo;
	}
	
	private static void insertIntoBD(L2PcInstance player, String estado){
		String ip_wan = ZeuS.getIp_Wan(player);
		String ip_lan = ZeuS.getIp_pc(player);
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
}
class closeClientByConnect implements Runnable{
	private int _time;
	L2Character _player;

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
