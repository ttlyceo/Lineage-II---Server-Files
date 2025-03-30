package ZeuS.event;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ScheduledFuture;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.opera;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.enums.TeleportWhereType;
import l2r.gameserver.instancemanager.MapRegionManager;
import l2r.gameserver.instancemanager.TownManager;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.util.Broadcast;

public class TownWarEvent {
	protected static final Logger _log = Logger.getLogger(TownWarEvent.class.getName());
	@SuppressWarnings("unused")
	private static final int ALL_TOWNS_INT = 17;
	private boolean isInactive = true;
	private boolean isStarting = false;
	private boolean isStarted = false;
	private boolean firtMsnje = true;
	private boolean waitingToAutoStart = false;
	@SuppressWarnings("unused")
	private boolean isManualTW = false;
	private TownWarStartTask _task;
	
	private int SELECTED_TOWN_ID;
	private String SELECTED_TOWN_NAME;
	
	private Map<Integer, Location>spawnHide = new HashMap<Integer, Location>();
	
	private Map<Integer, Integer>PPL_KILLS = new HashMap<Integer, Integer>();
	
	
	
	private Map<String, Integer>CityID = new HashMap<String, Integer>();
	private Map<Integer, String>CityID_Random = new HashMap<Integer, String>();
	
	private final String ZONES_ID = "GLUDIN=5,GLUDIO=7,DION=8,GIRAN=9,OREN=10,H.VILLAGE=11,ADEN=12,"
			+ "GODDARD=13,RUNE=14,HEINE=15,SCHUTTGART=17";
	

	public String getZoneID(){
		return ZONES_ID;
	}
	
	private void loadCities(){
		int contador=0;
		try{
			
		}catch(Exception a){
			CityID.clear();
			CityID_Random.clear();
		}
		for(String Zns : ZONES_ID.split(",")){
			CityID.put(Zns.split("=")[0], Integer.valueOf(Zns.split("=")[1]));
			CityID_Random.put(contador++, Zns.split("=")[0]);
		}
	}
	
	private void setCity(){
		Random r = new Random();
		int RanCity = r.nextInt( CityID_Random.size()-1);
		SELECTED_TOWN_NAME = CityID_Random.get(RanCity);
		SELECTED_TOWN_ID = CityID.get(SELECTED_TOWN_NAME);
	}
	
	@SuppressWarnings("unused")
	private int getIDCity(String Ciudad){
		return CityID.get(Ciudad);
	}
	
		
	public static TownWarEvent getInstance(){
		return SingletonHolder._instance;
	}
	
	
	public void setKills(L2PcInstance player){
		if(PPL_KILLS==null){
			PPL_KILLS.put(player.getObjectId(), 1);
		}else if(PPL_KILLS.containsKey(player.getObjectId())){
			PPL_KILLS.put(player.getObjectId(), PPL_KILLS.get(player.getObjectId()) + 1);
		}else{
			PPL_KILLS.put(player.getObjectId(), 1);
		}
	}

	
	private boolean isOkConfig(){
		if(general.EVENT_TOWN_WAR_CITY_ON_WAR.length()==0){
			return false;
		}
		
		return true;
	}
	
	public void AutoEventStart(){
		AutoEventStart(false);
	}

	public void AutoEventStart(boolean StartServer){
		if(waitingToAutoStart) {
			return;
		}
		if(!isOkConfig()){
			_log.warning("ZeuS -> Error on load Town War Event");
			return;
		}
		
		loadCities();
		
		
		try{
			if(general.EVENT_TOWN_WAR_AUTOEVENT){
				int minutesToBegin;
				if(StartServer){
					minutesToBegin = general.EVENT_TOWN_WAR_MINUTES_START_SERVER*60000;
					_log.warning("Town War Event Begin in " + minutesToBegin / 60000 +" minutes");
					waitingToAutoStart = true;
				}else{
					minutesToBegin = general.EVENT_TOWN_WAR_MINUTES_INTERVAL*60000;
					_log.warning("Town War Event Begin in " + minutesToBegin / 60000 +" minutes");
					waitingToAutoStart = true;
				}
				ThreadPoolManager.getInstance().scheduleGeneral(new TownWarStartTask(minutesToBegin),minutesToBegin);
			}
		}
		catch (Exception e)
		{
			_log.warning("TownWarEngine[TownWarManager.scheduleEventStart()]: Error figuring out a start time. Check TownWarEventInterval in config file.");
		}
	}
			
	public void starting()
	{
		isInactive = false;
		isStarting = true;
		
		if(!general.EVENT_TOWN_WAR_RANDOM_CITY){
			SELECTED_TOWN_ID = CityID.get(general.EVENT_TOWN_WAR_CITY_ON_WAR);
			SELECTED_TOWN_NAME = general.EVENT_TOWN_WAR_CITY_ON_WAR;
		}else{
			setCity();
		}
		
		_log.warning("Town War JOIN TIME:" + general.EVENT_TOWN_WAR_JOIN_TIME + " in " + SELECTED_TOWN_NAME);
		_task = null;
		_task = new TownWarStartTask(System.currentTimeMillis() + 60000 * general.EVENT_TOWN_WAR_JOIN_TIME);
		_task.setStartTime(System.currentTimeMillis() + 60000 * general.EVENT_TOWN_WAR_JOIN_TIME);
		ThreadPoolManager.getInstance().executeGeneral(_task);
	}		
	
	public void startEvent(boolean isAutomatic)
	{
		if(!isAutomatic){
			if(!general.EVENT_TOWN_WAR_RANDOM_CITY){
				SELECTED_TOWN_ID = CityID.get(general.EVENT_TOWN_WAR_CITY_ON_WAR);
				SELECTED_TOWN_NAME = general.EVENT_TOWN_WAR_CITY_ON_WAR;
			}else{
				setCity();
			}			
		}
		
		if(isAutomatic && !isStarting && isStarted){
			endEvent(false);
		}
		
		isStarting = false;
		isStarted = true;
		TownManager.getTown(SELECTED_TOWN_ID).setIsTWZone(true);
		TownManager.getTown(SELECTED_TOWN_ID).updateForCharactersInside();
		
		hideNPC(true);
		
		AnunciarTodos(SELECTED_TOWN_NAME + " is now a War Zone. Good Luck!");
		if(isAutomatic){
			_task.setStartTime(System.currentTimeMillis() + 60000L * general.EVENT_TOWN_WAR_MINUTES_EVENT);
			ThreadPoolManager.getInstance().executeGeneral(_task);
		}
	}		
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private int getTopPPLKill(){
		Iterator itr = PPL_KILLS.entrySet().iterator();
		int Kills=-1, IDJug=0,tKills=0;
		if(PPL_KILLS!=null){
			if(PPL_KILLS.size()>0){
			    while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	tKills = Integer.valueOf(Entrada.getValue().toString());
			    	if(general.DEBUG_CONSOLA_ENTRADAS){
			    		_log.warning("IDCHAR:" + Entrada.getKey() + " pvp:" + Entrada.getValue());
			    	}
			    	if(tKills >  Kills){
			    		Kills = tKills;
			    	}
			    }				
			}
		}
	    return Kills;
	}
	
	@SuppressWarnings("rawtypes")
	private Map<Integer, Integer>getTopKiller(){
		Map<Integer, Integer>TempKiller = new HashMap<Integer, Integer>();
		Iterator itr = PPL_KILLS.entrySet().iterator();
		int maxTop = getTopPPLKill();
		if(maxTop>0){
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	int tKills = Integer.valueOf(Entrada.getValue().toString());
		    	if(tKills == maxTop){
		    		TempKiller.put(Integer.valueOf(Entrada.getKey().toString()), maxTop);
		    	}
		    }
		}
		return TempKiller;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private void giveRewardToTheTop(){
		Map<Integer, Integer> TopKillers = getTopKiller();
		if(TopKillers!=null){
			if(TopKillers.size()>0){
				Iterator itr = TopKillers.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idPpl = Integer.valueOf(Entrada.getKey().toString());
					int tPpl = Integer.valueOf(Entrada.getValue().toString());
					L2PcInstance playerWin = opera.getPlayerbyID(idPpl);
					if(playerWin!=null){
						AnunciarTodos("Top Killer " + playerWin.getName() + " has been rewarded");
						//opera.giveReward(playerWin, general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER);
						EmailSend.sendRewardForTopTownWarEvent(playerWin);
					}
				}
			}
		}
	}
	
	@SuppressWarnings("unused")
	private void MoveCharBegin(){
		Vector<L2PcInstance>charPlayer = new Vector<L2PcInstance>();
		for(L2PcInstance cha : opera.getAllPlayerOnWorld()){
			if(cha.isInTownWarEvent() && !cha.getClient().isDetached() && !cha.isGM()){
				Location locNew = MapRegionManager.getInstance().getTeleToLocation(cha, TeleportWhereType.TOWN);
				cha.teleToLocation(locNew);				
			}
		}
	}
	
	private void hideNPC(boolean Estado){
		
		if(!general.EVENT_TOWN_WAR_HIDE_NPC){
			return;
		}
		
		if(general.EVENT_TOWN_WAR_NPC_ID_HIDE.length()==0){
			return;
		}
		for(String idNpcH : general.EVENT_TOWN_WAR_NPC_ID_HIDE.split(",")){
			if(Estado){
				spawnHide.clear();
				MoveCharBegin();
				for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(Integer.valueOf(idNpcH))){
					L2Npc npcLocal = SpawnLo.getLastSpawn();
					if(npcLocal.isInTownWarEvent()){
						spawnHide.put(npcLocal.getObjectId(), npcLocal.getLocation());
						npcLocal.teleToLocation(-128848, -260358, -15535);
					}
				}
			}else{
				for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(Integer.valueOf(idNpcH))){
					L2Npc npcLocal = SpawnLo.getLastSpawn();
					if(spawnHide.containsKey(npcLocal.getObjectId())){
						npcLocal.teleToLocation(spawnHide.get(npcLocal.getObjectId()));
					}
				}
			}
		}
	}
	
	public void endEvent(boolean isAutomatic)
	{
		isStarted = false;
		isInactive = true;
		firtMsnje = true;
		waitingToAutoStart = false;
		hideNPC(false);
		TownManager.getTown(SELECTED_TOWN_ID).setIsTWZone(false);
		TownManager.getTown(SELECTED_TOWN_ID).updateForCharactersInside();
		if(general.EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER){
			giveRewardToTheTop();
		}
		PPL_KILLS.clear();
		AnunciarTodos(SELECTED_TOWN_NAME + " has return to peace Zone, Congratulations to all Winners!!");
		if(isAutomatic){
			this.AutoEventStart();
		}
	}
	
	public final boolean isOn(){
		return this.isStarted;
	}

	class TownWarStartTask implements Runnable
		{
			private long _startTime;
			public ScheduledFuture<?> nextRun;
			
			public TownWarStartTask(long startTime)
			{
				_startTime = startTime;
			}
			
			public void setStartTime(long startTime)
			{
				_startTime = startTime;
			}
			
			/**
			 * @see java.lang.Runnable#run()
			 */
			@Override
			public void run()
			{
				if(!general.EVENT_TOWN_WAR_AUTOEVENT) {
					waitingToAutoStart = false;
					return;
				}				
				int delay = (int) Math.round((_startTime - System.currentTimeMillis()) / 1000.0);
				
				if (delay > 0)
				{
					this.announce(delay);
				}
				
				int nextMsg = 0;
				if (delay > 3600)
				{
					nextMsg = delay - 3600;
				}
				else if (delay > 1800)
				{
					nextMsg = delay - 1800;
				}
				else if (delay > 900)
				{
					nextMsg = delay - 900;
				}
				else if (delay > 600)
				{
					nextMsg = delay - 600;
				}
				else if (delay > 300)
				{
					nextMsg = delay - 300;
				}
				else if (delay > 60)
				{
					nextMsg = delay - 60;
				}
				else if (delay > 5)
				{
					nextMsg = delay - 5;
				}
				else if (delay > 0)
				{
					nextMsg = delay;
				}
				else
				{
					if (isInactive)
					{
						starting();
					}
					else if (isStarting)
					{
						startEvent(true);
					}
					else
					{
						endEvent(true);
					}
				}
				
				if (delay > 0)
				{
					nextRun = ThreadPoolManager.getInstance().scheduleGeneral(this, nextMsg * 1000);
				}
			}
			
			private void announce(long time)
			{
				if(firtMsnje){
					if (TownWarEvent.this.isStarting)
					{
						AnunciarTodos("Attention: Town War Event will be Start soon in "+ SELECTED_TOWN_NAME  +"!!");
						firtMsnje = false;
					}					
				}
				
				if (time >= 3600 && time % 3600 == 0)
				{
					if (TownWarEvent.this.isStarting)
					{
						AnunciarTodos("Attention: " + String.valueOf((time / 60 / 60)) + " hour(s) until event starts in "+ SELECTED_TOWN_NAME +"!");
					}
					else if (TownWarEvent.this.isStarted)
					{
						AnunciarTodos(String.valueOf((time / 60 / 60)) + " hour(s) until event is finished!");
					}
				}
				else if (time >= 60)
				{
					if (TownWarEvent.this.isStarting)
					{
						AnunciarTodos("Attention: " + String.valueOf((time / 60)) + " minute(s) until event starts in "+ SELECTED_TOWN_NAME +"!");
					}
					else if (TownWarEvent.this.isStarted)
					{
						AnunciarTodos(String.valueOf((time / 60 )) + " minute(s) until event is finished!");
					}
				}
				else
				{
					if (TownWarEvent.this.isStarting)
					{
						AnunciarTodos("Attention: " + String.valueOf(time) + " second(s) until event starts in "+ SELECTED_TOWN_NAME +"!");
					}
					else if (TownWarEvent.this.isStarted)
					{
						AnunciarTodos(String.valueOf(time) + " second(s) until event is finished!");
					}
				}
			}
		}		
		
		
		private static void AnunciarTodos(String Mensaje)
		{
			CreatureSay strMensaje = new CreatureSay(0, 18, "", "[TW Event]" + Mensaje);
			Broadcast.toAllOnlinePlayers(strMensaje);
		}		
		
		@SuppressWarnings("synthetic-access")
		private static class SingletonHolder
		{
			protected static final TownWarEvent _instance = new TownWarEvent();
		}		
}
