package ZeuS.Config;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.event.TownWarEvent;
import ZeuS.procedimientos.opera;
import gr.sr.interf.SunriseEvents;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;


public class _dealy_reward_system {
	private final static Logger _log = Logger.getLogger(_player_dealy_reward.class.getName());
	private static Map<String, _player_dealy_reward> _PLAYERS = new HashMap<String, _player_dealy_reward>();
	private static Map<Integer, _cycles_> CYCLES = new HashMap<Integer, _cycles_>();
	private static boolean isEnabled;
	private static _securityType TYPE_SECURITY;
	
	public static boolean playerRegister(L2PcInstance player) {
		if(!isEnabled) {
			return false;
		}
		String keyPlayer = getSecurityVal(player);

		if(keyPlayer ==null) {
			return false;
		}
		if(keyPlayer.length()==0) {
			return false;
		}
		if(_PLAYERS==null) {
			_player_dealy_reward tmp = new _player_dealy_reward(player, CYCLES, keyPlayer);
			_PLAYERS.put(keyPlayer, tmp);
		}else if(_PLAYERS.size()==0) {
			_player_dealy_reward tmp = new _player_dealy_reward(player, CYCLES, keyPlayer);
			_PLAYERS.put(keyPlayer, tmp);			
		}else if(!_PLAYERS.containsKey(keyPlayer)) {
			_player_dealy_reward tmp = new _player_dealy_reward(player, CYCLES, keyPlayer);
			_PLAYERS.put(keyPlayer, tmp);			
		}else {
			if(_PLAYERS.get(keyPlayer).Is_Done()) {
				return false;
			}
			_PLAYERS.get(keyPlayer).reEnter(player, CYCLES);
		}
		return true;
	}
	
	public static boolean isInDealyLoginQuest(L2PcInstance player) {
		if(!isEnabled) {
			return false;
		}
		String keyPlayer = getSecurityVal(player);

		if(keyPlayer ==null) {
			return false;
		}
		if(keyPlayer.length()==0) {
			return false;
		}
		if(_PLAYERS==null) {
			return true;
		}else if(_PLAYERS.size()==0) {
			return true;			
		}else if(!_PLAYERS.containsKey(keyPlayer)) {
			return true;			
		}else {
			if(_PLAYERS.get(keyPlayer).Is_Done()) {
				return false;
			}
		}
		return true;		
	}
	
	public static String getCycle(L2PcInstance player) {
		String keyPlayer = getSecurityVal(player);

		if(keyPlayer ==null) {
			return "0";
		}
		if(keyPlayer.length()==0) {
			return "0";
		}
		if(_PLAYERS==null) {
			return "0";
		}else if(_PLAYERS.size()==0) {
			return "0";			
		}else if(!_PLAYERS.containsKey(keyPlayer)) {
			return "0";			
		}else {
			if(_PLAYERS.get(keyPlayer).Is_Done()) {
				return "0";
			}
		}
		return String.valueOf(_PLAYERS.get(keyPlayer).getCycle());
	}
	
	private static String getSecurityVal(L2PcInstance player) {
		String keyPlayer = "";
		switch(TYPE_SECURITY) {
			case ACCOUNT:
				keyPlayer = player.getAccountName();
				break;
			case CHAR:
				keyPlayer = String.valueOf(player.getObjectId());
				break;
			case IP:
				keyPlayer = ZeuS.getIp_Wan(player) + "-" + ZeuS.getIp_pc(player);
				break;
			case HWID:
				keyPlayer = player.getClient().getHWID();
				break;
			case NONE:
				keyPlayer = "";
		}
		return keyPlayer;
	}
	
	public static void loadDealy() {
		cleanDataBase();
		File dir = new File("./config/zeus/zeus_dealy_reward.xml");
		try
		{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling()){
				
				if (!"list".equalsIgnoreCase(n.getNodeName())){
					continue;
				}
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
					if("cycles".equalsIgnoreCase(d.getNodeName())){
						try{
							isEnabled  = Boolean.valueOf(d.getAttributes().getNamedItem("enabled").getNodeValue());
						}catch(Exception a){
							isEnabled = false;
						}
						try {
							String tempValue = d.getAttributes().getNamedItem("check_by").getNodeValue();
							if(tempValue != null) {
								switch(tempValue.toUpperCase()) {
									case "IP":
										TYPE_SECURITY = _securityType.IP;
										break;
									case "ACCOUNT":
										TYPE_SECURITY = _securityType.ACCOUNT;
										break;
									case "CHAR":
										TYPE_SECURITY = _securityType.CHAR;
										break;
									case "HWID":
										TYPE_SECURITY = _securityType.HWID;
										break;
									default:
										TYPE_SECURITY = _securityType.NONE;
										isEnabled = false;
										break;
								}
							}
						}catch(Exception a) {
							TYPE_SECURITY = _securityType.NONE;
							isEnabled = false;
						}
						continue;
					}						
					if (!"cycle".equalsIgnoreCase(d.getNodeName())){
						continue;
					}
					int ID = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
					int SECONDS = Integer.valueOf(d.getAttributes().getNamedItem("time_online_seg").getNodeValue());
					String VALCHECK = d.getAttributes().getNamedItem("checkDualBox").getNodeValue();
					boolean CHECK_DUAL_BOX;
					if(VALCHECK.equalsIgnoreCase("true")) {
						CHECK_DUAL_BOX = true;
					}else {
						CHECK_DUAL_BOX = false;
					}
					_cycles_ TMP = new _cycles_(ID, SECONDS, CHECK_DUAL_BOX, TYPE_SECURITY);
					CYCLES.put(ID, TMP);
					for (Node b = d.getFirstChild(); b != null; b = b.getNextSibling()){
						if ("reward".equalsIgnoreCase(b.getNodeName())){
							for (Node Rew = b.getFirstChild(); Rew != null; Rew = Rew.getNextSibling())
							{
								if (Rew.getNodeName().equalsIgnoreCase("item")){
									int IDItem = 0;
									long Count = 0;
									String values = "";
									try{
										values = String.valueOf(Rew.getAttributes().getNamedItem("id").getNodeValue().toLowerCase());										
										IDItem = Integer.valueOf(values);
									}catch(Exception a){
										
									}
									values = "";
									try{
										values = String.valueOf(Rew.getAttributes().getNamedItem("count").getNodeValue().toLowerCase());
										Count = Long.valueOf(Rew.getAttributes().getNamedItem("count").getNodeValue().toLowerCase());
									}catch(Exception a){
										
									}
									if(IDItem>0 && Count >0) {
										CYCLES.get(ID).setReward(IDItem, Count);
									}
								}
							}
						}
					}
				}
			}
		}catch(Exception a) {
			
		}
		createTimeToClean();
	}
	
	private static void createTimeToClean() {
		Calendar CleanProcess;
		CleanProcess = Calendar.getInstance();
		CleanProcess.set(Calendar.HOUR_OF_DAY, 0);
		CleanProcess.set(Calendar.MINUTE, 1);
		CleanProcess.add(Calendar.HOUR_OF_DAY, 24);
		long Seg = CleanProcess.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
		try{
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
    			@Override
    			public void run(){
    				cleanDataBase();
    				_log.warning("Dealy Reward Data has been Cleaning");
    				createTimeToClean();
    			}
    		}, Seg);
		}catch (Exception ie){
			_log.warning("Error Dealy Reward Cleaning Process -> " + ie.getMessage());
		}		
	}
	
	private static void cleanDataBase() {
		String SQL = "DELETE FROM zeus_dealy_reward WHERE zeus_dealy_reward.isDone = ? OR ((UNIX_TIMESTAMP(NOW()) - UNIX_TIMESTAMP(zeus_dealy_reward.startDate)) > ?) OR ( DATEDIFF(NOW(),zeus_dealy_reward.startDate) > 0 )";
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(SQL);
			ins.setString(1, "Y");
			ins.setInt(2, 43200/*12 hours*/);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}		
	}
}

enum _securityType{
	IP,
	ACCOUNT,
	CHAR,
	HWID,
	NONE
}

class _cycles_{
	private final int ID;
	private final int SECONDS;
	private final boolean CHECK_DUAL_BOX;
	private final _securityType TYPE_CHECK;
	private Map<String, Integer> IP_CHECK = new HashMap<String, Integer>();
	private Map<Integer, Long> REWARDS = new HashMap<Integer, Long>();
	private final static Logger _log = Logger.getLogger(_player_dealy_reward.class.getName());
	public _cycles_(int _id, int _seconds, boolean checkDualBox, _securityType _TypeCheck) {
		this.ID = _id;
		this.SECONDS = _seconds;
		this.CHECK_DUAL_BOX = checkDualBox;
		this.TYPE_CHECK = _TypeCheck;
	}
	public final boolean checkDualBox() {
		return this.CHECK_DUAL_BOX;
	}
	public boolean isPlayerInDualBox(L2PcInstance player) {
		if(player.isGM()) {
			return false;
		}
		String IP = ZeuS.getIp_Wan(player) + "-" + ZeuS.getIp_pc(player);
		if(this.IP_CHECK!=null) {
			if(this.IP_CHECK.size()!=0) {
				if(this.IP_CHECK.containsKey(IP)) {
					if(this.IP_CHECK.get(IP) != player.getObjectId()) {
						return true;
					}
				}
			}
		}

		this.IP_CHECK.put(IP, player.getObjectId());
		return false;			
		
	}
	public void setReward(int idItem, long count) {
		this.REWARDS.put(idItem, count);
	}
	public final int getID() {
		return this.ID;
	}
	public final int getSeconds() {
		return this.SECONDS;
	}
	public final Map<Integer, Long> getRewards(){
		return this.REWARDS;
	}
}

class _player_dealy_reward {
	private final static Logger _log = Logger.getLogger(_player_dealy_reward.class.getName());
	private final String _SQLINSERT = "INSERT INTO zeus_dealy_reward(idValue, unixstart, cycle) VALUES(?,?,?) ON DUPLICATE KEY UPDATE unixstart=?, cycle=?";
	private final String _SQLPAUSE = "UPDATE zeus_dealy_reward SET segpaused=? WHERE idValue=?";
	private final String _SQLVIEW = "SELECT idValue, cycle, segpaused FROM zeus_dealy_reward WHERE isDone=? AND idValue=?";
	private final String _SQLDONE = "UPDATE zeus_dealy_reward SET isDone=? WHERE idValue=?";
	private int secondPass=0;
	private _cycles_ CYCLE;
	private int _CYCLE_ID_FROM_DB=1;
	private L2PcInstance PLAYER;
	private boolean giveReward;
	private boolean isEnabled=false;
	private boolean isPaused;
	private boolean isDone = false;
	private String KEY_VALUE;
	public _player_dealy_reward(L2PcInstance _player, Map<Integer, _cycles_> _infoCycles, String keyValues) {
		this.PLAYER = _player;
		this.giveReward = false;
		this.isEnabled = true;
		this.isPaused = false;
		this.KEY_VALUE = keyValues;
		loadData();
		this.CYCLE = _infoCycles.get(this._CYCLE_ID_FROM_DB);
		if(this.CYCLE.checkDualBox()) {
			if(this.CYCLE.isPlayerInDualBox(_player)) {
				return;
			}
		}		
		this.saveInBD();
		this.clockStart();
	}
	public int getCycle() {
		return this._CYCLE_ID_FROM_DB;
	}
	public boolean Is_Done() {
		return this.isDone;
	}
	public void reEnter(L2PcInstance player, Map<Integer, _cycles_> _infoCycles) {
		if(this.isDone) {
			return;
		}
		if(this.giveReward) {
			this.giveRewardToPLayer();
			this.giveReward = false;
			this._CYCLE_ID_FROM_DB++;
			if(_infoCycles.containsKey(this._CYCLE_ID_FROM_DB)) {
				this.isEnabled=false;
				this.isPaused = false;
				this.secondPass=0;
				this.CYCLE = _infoCycles.get(this._CYCLE_ID_FROM_DB);
				this.saveInBD();				
			}else {
				this.done();
				return;
			}
		}
		if(!this.isEnabled || this.isPaused) {
			if(this.CYCLE.checkDualBox()) {
				if(this.CYCLE.isPlayerInDualBox(player)) {
					return;
				}
			}			
			this.PLAYER = player;
			this.isPaused=false;
			this.isEnabled = true;
			this.clockStart();			
		}
	}
	
	private void giveRewardToPLayer() {
		Iterator itr = this.CYCLE.getRewards().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry InfoItem =(Map.Entry)itr.next();
			int ID_ITEM = (int)InfoItem.getKey();
			long Count = (long)InfoItem.getValue();
			try {
				opera.giveReward(this.PLAYER, ID_ITEM, Count);
			}catch(Exception a) {
				
			}
		}
	}
	private boolean loadData() {
		boolean _Return = false;
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement(this._SQLVIEW)){
				ps.setString(1, "N");
				ps.setString(2, this.KEY_VALUE);
				try (ResultSet rs = ps.executeQuery()){
					if(rs.next()){
						this._CYCLE_ID_FROM_DB = rs.getInt("cycle");
						this.secondPass = rs.getInt("segpaused");
						_Return = true;
					}
				}catch(Exception a){
					
				}
		}catch(Exception b) {
			
		}
		return _Return;
	}
	
	private boolean canShowTime() {
		if(this.PLAYER==null) {
			return false;
		}
		if(this.PLAYER.getClient().isDetached()) {
			return false;
		}
		if(this.PLAYER.isInCombat()) {
			return false;
		}
		if(this.PLAYER.isInDuel()) {
			return false;
		}
		if(this.PLAYER.getPvpFlag()>0) {
			if(!this.PLAYER.isInsideZone(ZoneIdType.PEACE)) {
				return false;
			}
		}
		if(this.PLAYER.isInTownWarEvent()) {
			return false;
		}
		if(this.PLAYER.isOnEvent()) {
			return false;
		}
		if(SunriseEvents.isInEvent(this.PLAYER)) {
			return false;
		}
		if(RaidBossEvent.playerIsRaidEventInEvent(this.PLAYER)) {
			return false;
		}
		if(ZeuS.isInPvPInstance(this.PLAYER, false)) {
			return false;
		}
		return true;
	}
	
	private void clockStart(){
		if(!this.isEnabled) {
			return;
		}
		if(this.isPaused) {
			return;
		}
		if(this.PLAYER==null) {
			this.isPaused=true;
			return;
		}
		if(!this.PLAYER.isOnline()) {
			this.isPaused = true;
			this.pause();
			return;
		}
		if(this.PLAYER.getClient().isDetached()) {
			this.isPaused = true;
			this.pause();
			return;
		}
		if(this.secondPass >= this.CYCLE.getSeconds()) {
			this.isPaused = false;
			this.isEnabled = false;
			this.giveReward = true;
			return;
		}
		this.secondPass++;
		String SendMensaje = "DAILY REWARD TIME\n" + getTimeLeft();
		if(canShowTime()) {
			ExShowScreenMessage Mnsje = new ExShowScreenMessage(1, -1,  ExShowScreenMessage.TOP_LEFT , 1, /*Size*/1, 0, 0, false, 1005, false, SendMensaje);
			this.PLAYER.sendPacket(Mnsje);
		}
		createCycle();
	}

	private String getTimeLeft() {
		String _return = "";
		int segLeft = this.CYCLE.getSeconds()  - this.secondPass;
		int hours = ( segLeft > 3600 ? segLeft / 3600 : 0);
		int min = 0;		
		if(hours>0) {
			min = ( segLeft % 3600 ) / 60;
		}else {
			min = ( segLeft>0 ? segLeft / 60 : 0);			
		}
		
		if(hours > 0) {
			_return += ( hours>9 ? String.valueOf(hours) : "0" + String.valueOf(hours)) + ":";
		}
		
		if(min > 0) {
			_return += ( min >= 10 ? String.valueOf(min) : "0" + String.valueOf(min));
		}else {
			_return += "00";
		}

		int seg = 0;
		
		if(hours>0) {
			seg = segLeft - ( 3600 * hours ) - ( min * 60);
		}else {
			seg = ( min > 0 ? segLeft - ( min * 60 )  : segLeft );			
		}
		
		
		if(seg >= 10) {
			_return += ":" + String.valueOf(seg);
		}else {
			_return += ":0" + String.valueOf(seg);			
		}
		return _return;
	}
	private void createCycle() {
		try{
			//Thread.sleep(300);
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
    			@Override
    			public void run(){
    				clockStart();
    			}
    		}, 1000);
		}catch (Exception ie){
			_log.warning("Error in Dealy Reward counter->" + ie.getMessage());
		}		
	}
	private void saveInBD() {
		if(PLAYER!=null) {
			this.insertDB();
		}
	}
	private void done() {
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(_SQLDONE);
			ins.setString(1,"Y");
			ins.setString(2, this.KEY_VALUE);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}
		this.isDone = true;
	}
	private void pause() {
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(_SQLPAUSE);
			ins.setInt(1, this.secondPass);
			ins.setString(2, this.KEY_VALUE);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}		
	}
	private void insertDB() {
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(_SQLINSERT);
			//unixstart=?, cycle=?
			ins.setString(1, this.KEY_VALUE);
			ins.setInt(2, opera.getUnixTimeNow());
			ins.setInt(3, this.CYCLE.getID());
			ins.setInt(4, opera.getUnixTimeNow());
			ins.setInt(5, this.CYCLE.getID());
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}			
	}
}