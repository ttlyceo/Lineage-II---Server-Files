package ZeuS.event;

import gr.sr.interf.SunriseEvents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.MessageType;
import l2r.gameserver.enums.Team;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.util.Broadcast;
import l2r.util.StringUtil;
import ZeuS.Config._GeneralDropInfo;
import ZeuS.Config._InfoGeneralDrop;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class RaidBossEvent
{
	
	private static class RBWorld extends InstanceWorld
	{
		public RBWorld()
		{
		}
	}	
	
	private static boolean RaidBoss_Auto_Event = false;
	
	private static InstanceWorld world;
	private static int InstanceId = 0;
	private static int SECOND_TO_CHECK_LAST_HIT = 30;
	
	
	
	@SuppressWarnings("unused")
	private final static int searchNextSpawnOnMinute = 30;
	
	@SuppressWarnings("unused")
	private static int _npcX, _npcY, _npcZ, _bossX, _bossY, _bossZ, _startX, _startY, _startZ;
	private static int _bossId;
	private static boolean _playersWon = false;
	
	private static int KeyToStart = 0;
	private static int NextUnixEvent = 0;
	private static boolean isStartServerEvent = true;
	
	private static Map<Integer, String> PLAYER_REGISTER = new HashMap<Integer, String>();
	private static Vector<Integer> PLAYER_TO_HIT_THE_RAID = new Vector<Integer>();
	private static boolean isCheck = false;
	private static Map<Integer, Integer> PLAYER_TIME_HIT = new HashMap<Integer, Integer>();
	private static Map<Integer, HashMap<String, String>> PLAYER_INFO = new HashMap<Integer, HashMap<String, String>>();
	private static Vector<_itemRBEventDrop> DROP_RAID = new Vector<_itemRBEventDrop>();
	private static enum FormatosVaribles{
		TELE,
		COLOR
	}
	
	public static boolean isJoin(){
		return _joining;
	}
	
	public static void isEventModRaid(L2Npc raid,L2PcInstance player){
		if(PLAYER_REGISTER==null){
			return;
		}
		if(PLAYER_REGISTER.size()==0){
			return;
		}
		if(!_started){
			return;
		}
		
		if(raid.getTemplate().getId() != _bossId){
			return;
		}
		
		raidKilled();
	}
	public static void setPlayerHitRaid(L2PcInstance player){
		if(player==null){
			return;
		}
		
		if(isCheck){
			return;
		}
		
		if(!PLAYER_REGISTER.containsKey(player.getObjectId())){
			return;
		}
		
		PLAYER_TIME_HIT.put(player.getObjectId(), opera.getUnixTimeNow());
		
		if(PLAYER_TO_HIT_THE_RAID!=null){
			if(!PLAYER_TO_HIT_THE_RAID.contains(player.getObjectId())){
				PLAYER_TO_HIT_THE_RAID.add(player.getObjectId());
				player.setTeam(Team.RED);
			}
		}else{
			PLAYER_TO_HIT_THE_RAID.add(player.getObjectId());
		}
	}
	
	public static boolean isPlayerOnRBEvent(L2PcInstance player){
		
		if(PLAYER_REGISTER==null){
			return false;
		}
		
		if(PLAYER_REGISTER.size()==0){
			return false;
		}
		
		try{
			return PLAYER_REGISTER.containsKey(player.getObjectId());
		}catch(Exception a){
			return false;
		}
	}
	
	public static boolean onDieRevive(L2PcInstance character)
	{
		if(!_started){
			return false;
		}
		
		if(PLAYER_REGISTER==null){
			return false;
		}
		
		if(PLAYER_REGISTER.size()==0){
			return false;
		}
		
		if(!PLAYER_REGISTER.containsKey(character.getObjectId())){
			return false;
		}
		
		L2PcInstance activeChar = character;
		activeChar.sendMessage(language.getInstance().getMsg(activeChar).YOU_WILL_BE_REVIVE_ON.replace("$second",String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_REVIVE)));
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@Override
			public void run()
			{
				activeChar.doRevive();
				if(PLAYER_TO_HIT_THE_RAID!=null){
					if(PLAYER_REGISTER!=null){
						if(PLAYER_REGISTER.containsKey(activeChar.getObjectId())){
							if(PLAYER_TO_HIT_THE_RAID.contains(activeChar.getObjectId())){
								activeChar.setTeam(Team.RED);
							}else{
								activeChar.setTeam(Team.BLUE);
							}
							try{
								SkillData.getInstance().getInfo(1323,1).getEffects(activeChar,activeChar);
							}catch(Exception a){
								
							}
						}
					}
				}
			}
		}, general.EVENT_RAIDBOSS_SECOND_TO_REVIVE * 1000);
		return true;
	}
	
	public static void setPlayerInfo(L2PcInstance player){
		String LocActual = String.valueOf(player.getLocation().getX()) + "," + String.valueOf(player.getLocation().getY()) + "," + String.valueOf(player.getLocation().getZ());
		String ColorName = String.valueOf(player.getAppearance().getNameColor());
		PLAYER_INFO.put(player.getObjectId(), new HashMap<String, String>());
		PLAYER_INFO.get(player.getObjectId()).put(FormatosVaribles.TELE.name(), LocActual);
		PLAYER_INFO.get(player.getObjectId()).put(FormatosVaribles.COLOR.name(), ColorName);
		player.setTeam(Team.BLUE);
	}
	
	public static String getPlayerTele(L2PcInstance player){
		return PLAYER_INFO.get(player.getObjectId()).get(FormatosVaribles.TELE.name());
	}

	public static String getPlayerColorName(L2PcInstance player){
		return PLAYER_INFO.get(player.getObjectId()).get(FormatosVaribles.COLOR.name());
	}
	
	private final static Logger _log = Logger.getLogger(RaidBossEvent.class.getName());

	public static String _nameRaid = "";
	
	public static boolean _joining = false;
	public static boolean _teleport = false;
	public static boolean _started = false;
	public static L2Spawn _bossSpawn;
	
	public static int _npcHeading = 0;
	public static int _bossHeading = 0;
	
	
	@SuppressWarnings("rawtypes")
	public static Vector<L2PcInstance> getValidPlayer(boolean LimpiarRegistroPlayer){
		Vector<L2PcInstance> retorno = new Vector<L2PcInstance>();
		Map<Integer, String> LimpieadoRegistro = new HashMap<Integer, String>();
		Iterator itr = PLAYER_REGISTER.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	L2PcInstance cha = null;
	    	try{
	    		cha = opera.getPlayerbyName(Entrada.getValue().toString());
	    		if(cha != null){
	    			if(cha.isOnline()){
	    				if(!cha.getClient().isDetached()){
	    					retorno.add(cha);
	    					LimpieadoRegistro.put(cha.getObjectId(), cha.getName());
	    				}
	    			}
	    		}
	    	}catch(Exception a){
	    		
	    	}
	    }
	    if(LimpiarRegistroPlayer){
	    	PLAYER_REGISTER.clear();
	    	PLAYER_REGISTER = LimpieadoRegistro;
	    }
	    return retorno;
	}
	
	
	@SuppressWarnings("rawtypes")
	public static void AnnounceToPlayers(Boolean toall, String announce)
	{
		if (toall) {
			AnunciarTodos(announce);
		}else{
			Iterator itr = PLAYER_REGISTER.entrySet().iterator();
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	L2PcInstance cha = null;
		    	try{
		    		cha = opera.getPlayerbyName(Entrada.getValue().toString());
		    		if(cha != null){
		    			central.msgbox_Lado(announce, cha, "RAIDBOSS EVENT");
		    		}
		    	}catch(Exception a){
		    		
		    	}
		    }
		}
	}
	
	public static void kickPlayerFromRaid(L2PcInstance playerToKick)
	{
		if (playerToKick == null) {
			return;
		}
		
		playerToKick.setInstanceId(0);
		
		if(isPlayerOnRBEvent(playerToKick)){
			PLAYER_REGISTER.remove(playerToKick.getObjectId());
		}
		
		try{
			PLAYER_REGISTER.remove(playerToKick.getObjectId());
		}catch(Exception a){
			
		}
		try{
			PLAYER_INFO.remove(playerToKick.getObjectId());
		}catch(Exception a){
			
		}
		try{
			PLAYER_TIME_HIT.remove(playerToKick.getObjectId());				
		}catch(Exception a){
			
		}
		try{
			PLAYER_TO_HIT_THE_RAID.remove(playerToKick.getObjectId());

		}catch(Exception a){
			
		}		
		
		
		if (_started || _teleport)
		{
			if (playerToKick.isOnline())
			{
				playerToKick.getAppearance().setNameColor( Integer.valueOf(getPlayerColorName(playerToKick)));
				playerToKick.broadcastUserInfo();
				playerToKick.sendMessage("You have been kicked from the Raid.");
				playerToKick.setTeam(Team.NONE);
				playerToKick.setInstanceId(0);				
				String LocationToSend = getPlayerTele(playerToKick);
				playerToKick.teleToLocation(Integer.valueOf(LocationToSend.split(",")[0]), Integer.valueOf(LocationToSend.split(",")[1]), Integer.valueOf(LocationToSend.split(",")[2]), false);
			}
		}
	}
	
	private static void setBossPos(){
		_bossX = Integer.valueOf(general.EVENT_RAIDBOSS_RAID_POSITION.split(",")[0]);
		_bossY = Integer.valueOf(general.EVENT_RAIDBOSS_RAID_POSITION.split(",")[1]);
		_bossZ = Integer.valueOf(general.EVENT_RAIDBOSS_RAID_POSITION.split(",")[2]);
	}
	
	public static boolean checkMaxLevel(int maxlvl)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MIN_LEVEL < maxlvl;
	}
	
	public static boolean checkMinLevel(int minlvl)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MAX_LEVEL > minlvl;
	}
	
	public static boolean checkMinPlayers(int players)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS <= players;
	}
	
	public static boolean checkMaxPlayers(int players)
	{
		return general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS > players;
	}
	
	public static boolean setTeamPos()
	{
		try {
			_startX = Integer.valueOf(general.EVENT_RAIDBOSS_PLAYER_POSITION.split(",")[0]);
			_startY = Integer.valueOf(general.EVENT_RAIDBOSS_PLAYER_POSITION.split(",")[1]);
			_startZ = Integer.valueOf(general.EVENT_RAIDBOSS_PLAYER_POSITION.split(",")[2]);
			return true;
		}catch(Exception a) {
			_log.warning("Error setting the raid boss player spot position: " + general.EVENT_RAIDBOSS_PLAYER_POSITION + " ("+ a.getMessage() +")");
		}
		return false;
	}
	
	public static boolean checkTeamOk()
	{
		return !(_started || _teleport || _joining);
	}
	
	public static boolean startJoin()
	{
		if(!setIdBoss()) {
			return false;
		}
		
		if(!setTeamPos()) {
			return false;
		}
		setBossPos();if(general.DEBUG_CONSOLA_ENTRADAS) {_log.warning("SetBossPos: DONE");}
		spawnEventBoss(); if(general.DEBUG_CONSOLA_ENTRADAS) {_log.warning("EventBoss: DONE");}
		_joining = true;
		MensajeRegistro(true);if(general.DEBUG_CONSOLA_ENTRADAS) {_log.warning("setShowMsjeJoin: DONE");}
		return true;
	}
	
	
	private static void MensajeRegistro(boolean isStart){
		if(isStart){
			AnnounceToPlayers(true, ZeuS.getAnnouncement("Iniciado / Started Use .joinraid[RBEVENT] to register and .leaveraid to unregister! Recruiting Levels "+ String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_LEVEL) +" to " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_LEVEL) + ". Player Needed: " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS) + " to " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS),null));
			AnnounceToPlayers(true, "Our Raid for now is " + _nameRaid + ", register and kill it!!!");
		}else{
			AnnounceToPlayers(true, ZeuS.getAnnouncement("Use .joinraid[RBEVENT] to register and .leaveraid to unregister! Players Registered " + String.valueOf(PLAYER_REGISTER.size()), null));
		}
	}
	
	@SuppressWarnings("static-access")
	private static void spawnEventBoss(){
		
		if(InstanceId <= 0 ){
			InstanceId = InstanceManager.getInstance().createDynamicInstance(null);
		}
		
		if(_joining){
			return;
		}
		
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("RaidBoss Instance ID -> " + InstanceId);
		}
		
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_bossId);
		world = new RBWorld();
		world.setInstanceId(InstanceId);
		world.setStatus(0);
		InstanceManager.getInstance().addWorld(world);
		try
		{
			try{
				DROP_RAID.clear();
			}catch(Exception Clean){
				
			}
			_bossSpawn = new L2Spawn(tmpl);
			_bossSpawn.setX(_bossX);
			_bossSpawn.setY(_bossY);
			_bossSpawn.setZ(_bossZ);
			_bossSpawn.setAmount(1);
			_bossSpawn.setHeading(_bossHeading);
			_bossSpawn.setRespawnDelay(20);
			_bossSpawn.setAmount(1);
			SpawnTable.getInstance().addNewSpawn(_bossSpawn, false);
			_bossSpawn.setInstanceId(InstanceId);
			_bossSpawn.init();
			_bossSpawn.getLastSpawn().setTitle("RAID BOSS EVENT");
			_bossSpawn.getLastSpawn()._isEventMobRaid = true;
			_bossSpawn.getLastSpawn().isAggressive();
			_bossSpawn.getLastSpawn().isInvisible();
			_bossSpawn.getLastSpawn().decayMe();
			_bossSpawn.getLastSpawn().spawnMe(_bossSpawn.getLastSpawn().getX(), _bossSpawn.getLastSpawn().getY(), _bossSpawn.getLastSpawn().getZ());
			_bossSpawn.getLastSpawn().broadcastPacket(new MagicSkillUse(_bossSpawn.getLastSpawn(), _bossSpawn.getLastSpawn(), 1034, 1, 1, 1));
			_nameRaid = tmpl.getName();
			
			try{
				if (!(_bossSpawn.getTemplate().getDropData().isEmpty())){
					for (_InfoGeneralDrop DataD : _GeneralDropInfo.getAllDropFromMob(_bossId))
					{
						final L2Item item = ItemData.getInstance().getTemplate(DataD.getIdItem());
						if (item == null)
						{
							continue;
						}
						_itemRBEventDrop t1 = new _itemRBEventDrop(item.getId(), item.getName(), item.getIcon(), DataD.getMin() , DataD.getMax() , (DataD.isQuest() ? "Quest" : (DataD.isDrop() ? "Drop" : "Sweep")));
						DROP_RAID.add(t1);
					}
				}
			}catch(Exception drops){
				_log.warning("Raid Engine[Drop Show] (" + drops.getMessage() + ")");
			}
		}
		catch (Exception e)
		{
			_log.warning("Raid Engine[spawnEventNpc(" + String.valueOf(_bossId) + ")]: exception: " + e.getMessage());
		}
	}
	
	private static boolean teleportStart(){
		if (!_joining || _started || _teleport) {
			return true;
		}
		
		removeOfflinePlayers();
		if (!checkMinPlayers( PLAYER_REGISTER.size()))
		{
			if(_joining){
				AnnounceToPlayers(true, "Not enough players for RB Event. Min Requested : " + String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS ) + ", Participating : " + PLAYER_REGISTER.size());
				cleanRaid();
			}
			return false;
		}
		
		_joining = false;
		AnnounceToPlayers(false, "Teleport to starting spot in "+ String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS) +" seconds!");
		setUserData();
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			public void run()
			{
				RaidBossEvent.sit(true);
				
				for (L2PcInstance player : getValidPlayer(false)){
					if (player != null)	{
						if (player.getParty() != null)
						{
							L2Party party = player.getParty();
							party.removePartyMember(player,MessageType.Expelled);
						}
						player.setInstanceId(InstanceId);
						player.teleToLocation(_startX, _startY, _startZ, true);
					}
				}
			}
		}, general.EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS * 1000);
		_teleport = true;
		return true;
	}
	
	@SuppressWarnings("unused")
	private static void startEvent(L2PcInstance activeChar)
	{
		if (!startEventOk())
		{
			if (Config.DEBUG) {
				_log.fine("Raid Engine[startEvent(" + activeChar.getName() + ")]: startEventOk() = false");
			}
			return;
		}
		_teleport = false;
		sit(false);
		_bossSpawn.getLastSpawn().isVisible();
		AnnounceToPlayers(false,"Started. Go kill the raid!");
		_started = true;
		ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossCheckLastHit() , SECOND_TO_CHECK_LAST_HIT * 1000);
	}
	
	public static Integer getSeconToStartEvent(){
		if(general.EVENT_RAIDBOSS_HOUR_TO_START==null){
			return -1;
		}
		
		if(general.EVENT_RAIDBOSS_HOUR_TO_START.length()==0){
			return -1;
		}
		
		Calendar currentCal = Calendar.getInstance();
		Integer hour = currentCal.get(Calendar.HOUR_OF_DAY);
		Integer mins = currentCal.get(Calendar.MINUTE);
		Integer sec = currentCal.get(Calendar.SECOND);
		String horaCercana = "";
		Vector<String> Tiempos = new Vector<String>();
		for(String horas : general.EVENT_RAIDBOSS_HOUR_TO_START.split(";")){
			Tiempos.add(horas);
		}
		Collections.sort(Tiempos);
		for(String tiempo : Tiempos){
			if(tiempo.split(":").length == 2){
				if(Integer.valueOf(tiempo.split(":")[0])==hour && Integer.valueOf(tiempo.split(":")[1])>=mins && horaCercana.length()==0){
					horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
				}else if(Integer.valueOf(tiempo.split(":")[0]) > hour && horaCercana.length()==0){
					horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
				}else if(Integer.valueOf(tiempo.split(":")[0])<= hour && Integer.valueOf(tiempo.split(":")[1])>=mins && horaCercana.length()==0){
					horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
				}else if((hour - Integer.valueOf(tiempo.split(":")[0]))>12 && horaCercana.length()==0){
					if(horaCercana.length()==0){
						horaCercana = tiempo.split(":")[0] + ":" + tiempo.split(":")[1] + ":00";
					}
				}
			}
		}
		int SegundosDiferencia = 0;
		if(horaCercana.length()>0){
			int SegundosHoraCerca = 0;
			if( Integer.valueOf(horaCercana.split(":")[0]) >= hour ){
				SegundosHoraCerca = (Integer.valueOf(horaCercana.split(":")[0]) * 3600) + (Integer.valueOf(horaCercana.split(":")[1]) * 60) + 60;
				SegundosDiferencia = SegundosHoraCerca - ((hour * 3600) + (mins * 60) + sec);				
			}else{
				SegundosHoraCerca = (Integer.valueOf(horaCercana.split(":")[0]) * 3600) + (Integer.valueOf(horaCercana.split(":")[1]) * 60) + 60;
				int segundosDias = 86400;
				int SegundosHoraActual = (hour * 3600) + (mins * 60) + sec;
				SegundosDiferencia = (segundosDias - SegundosHoraActual) + SegundosHoraCerca;
			}
			_log.warning("RaidBossEvent: Next event in ->" + opera.getTiempoON(SegundosDiferencia, false) + " sec. at " + horaCercana);
		}
		return SegundosDiferencia ;
	}
	
	
	
	@SuppressWarnings("unused")
	private static class RaidBoss_autoSearch implements Runnable{
		int _CodigoEntrada;
		public RaidBoss_autoSearch(){
			
		}
		@Override
		public void run(){
			try{
				if(!RaidBoss_Auto_Event){
					IntervalEventStart();
				}
			}catch(Exception a){

			}

		}
	}	
	
	
	
	protected static class RaidBossCheckLastHit implements Runnable{
		public RaidBossCheckLastHit(){
			
		}
		@SuppressWarnings("rawtypes")
		public void run(){
			if(!_started){
				isCheck = false;
				return;
			}
			
			if(PLAYER_TIME_HIT==null){
			    isCheck = false;
		    	ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossCheckLastHit() , SECOND_TO_CHECK_LAST_HIT * 1000);				
		    	return;
			}else if(PLAYER_TIME_HIT.size()==0){
			    isCheck = false;
		    	ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossCheckLastHit() , SECOND_TO_CHECK_LAST_HIT * 1000);
		    	return;
			}
			
			Iterator itr = PLAYER_TIME_HIT.entrySet().iterator();
			isCheck = true;
		    try{
		    	while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	L2PcInstance cha = null;
			    	try{
			    		int idPlayerToRemove = Integer.valueOf(Entrada.getKey().toString());
			    		
			    		if(PLAYER_TIME_HIT==null){
			    			continue;
			    		}else if(PLAYER_TIME_HIT.size()==0){
			    			continue;
			    		}else if(!PLAYER_TIME_HIT.containsKey(idPlayerToRemove)){
			    			continue;
			    		}
			    		cha = opera.getPlayerbyID(idPlayerToRemove);
			    		if(cha != null){
			    			if(cha.isOnline()){
		    					if(cha.getClient().isDetached()){
		    						kickPlayerFromRaid(cha);
		    					}else{
		    						int nowUnix = opera.getUnixTimeNow();
		    						int firtsLast = PLAYER_TIME_HIT.get(idPlayerToRemove);
		    						if( (nowUnix - firtsLast) >= SECOND_TO_CHECK_LAST_HIT){
		    							_log.info("[RB EVENT]The player " + cha.getName() + " was removed from the reward list");	
		    							cha.setTeam(Team.BLUE);
		    							PLAYER_TIME_HIT.remove(idPlayerToRemove);
		    							PLAYER_TO_HIT_THE_RAID.removeElement(idPlayerToRemove);
		    							central.msgbox_Lado("You need to attak to get Reward", cha);
		    						}	    						
		    					}
			    			}
			    		}
		    		}catch(Exception a){
		    			_log.warning("ZeuS Rb Event Check AFK Error->" + a.getMessage());
		    		}
			    }		    	
		    }catch(Exception e){
		    	_log.warning("ZeuS Rb Event Check List Error->" + e.getMessage());
		    }
		    isCheck = false;
	    	ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossCheckLastHit() , SECOND_TO_CHECK_LAST_HIT * 1000);
		}
	}
	
	
	public static class RaidBossIntervalRunner implements Runnable{
		int _CodigoEntrada;
		public RaidBossIntervalRunner(int CodEntrada){
			_CodigoEntrada = CodEntrada;
		}
		@Override
		public void run(){
			try{
				if(_CodigoEntrada == KeyToStart){
					if(!_joining && !_started & !_teleport){
						if(general.EVENT_RAIDBOSS_AUTOEVENT){
							KeyToStart = opera.getUnixTimeNow();
							autoEvent();
						}
					}
				}
			}catch(Exception a){

			}

		}
	}	
	
	public static void IntervalEventStart(){

		int IntevarloCorrectoMayor = general.EVENT_RAIDBOSS_JOINTIME + general.EVENT_RAIDBOSS_EVENT_TIME + 10;
		
		NextUnixEvent = 0;
		
		if(general.EVENT_RAIDBOSS_MINUTE_INTERVAL<=0 || general.EVENT_RAIDBOSS_MINUTE_INTERVAL <= IntevarloCorrectoMayor){
			_log.warning("Raidboss event->Problem with Interval : " + general.EVENT_RAIDBOSS_MINUTE_INTERVAL + ". Need to be bigger than "+ String.valueOf(IntevarloCorrectoMayor) +" minutes");
			RaidBoss_Auto_Event = false;
			return;
		}

		if(!general.EVENT_RAIDBOSS_AUTOEVENT){
			return;	
		}
		
		if(general.EVENT_RAIDBOSS_HOUR_TO_START==null){
			return;
		}
		if(general.EVENT_RAIDBOSS_HOUR_TO_START.length()==0){
			return;
		}
		
		
		int minutosParaComenzar = 0;
		
		if(isStartServerEvent){
			isStartServerEvent = false;
			minutosParaComenzar = general.EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER;
		}else{
			minutosParaComenzar = general.EVENT_RAIDBOSS_MINUTE_INTERVAL;
		}
		KeyToStart = opera.getUnixTimeNow();
		NextUnixEvent = KeyToStart + (minutosParaComenzar * 60);
		ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossIntervalRunner(KeyToStart), (minutosParaComenzar * 60) * 1000);
		_log.info("Raiddboss event-> Start: " + minutosParaComenzar + " minutes ("+ opera.getDateFromUnixTime(NextUnixEvent) +")");
		RaidBoss_Auto_Event = true;
	}
	
	public static String getNextEventTimeLess(){
		String retorno = "";
		
		if(general.EVENT_RAIDBOSS_MINUTE_INTERVAL<=0 || !RaidBoss_Auto_Event){
			return "";
		}
		
		int Falta = NextUnixEvent - opera.getUnixTimeNow();
		
		int minutosFalta = Math.round(Falta / 60);
		int Segundos = Falta;
		
		if(minutosFalta>0){
			retorno = "Left "+ String.valueOf(minutosFalta) +" minutes to start";
		}else{
			retorno = "Left "+ String.valueOf(Segundos) +" second to start";
		}
		retorno += "(/time "+ opera.getDateFromUnixTime(NextUnixEvent) +")";
		return retorno;
	}
	
	public static void autoEvent(){
		//INICIO
		if(_joining || _started || _teleport){
			return;
		}
		if (startJoin()){
			if (general.EVENT_RAIDBOSS_JOINTIME > 0) {
				waiter(general.EVENT_RAIDBOSS_JOINTIME * 60 * 1000); // minutes for join event
			}else if (general.EVENT_RAIDBOSS_JOINTIME <= 0){
				abortEvent();
				return;
			}
			if (teleportStart())
			{
				waiter(1 * 40 * 1000); // 30 sec wait time untill start fight after teleported
				if (startAutoEvent())
				{
					waiter(general.EVENT_RAIDBOSS_EVENT_TIME * 60 * 1000); // minutes for event time
					finishEvent();
				}
			}
			else if (!teleportStart())
			{
				abortEvent();
			}
		}
	}
	
	private static void waiter(long interval)
	{
		long startWaiterTime = System.currentTimeMillis();
		int seconds = (int) (interval / 1000);
		
		while (startWaiterTime + interval > System.currentTimeMillis())
		{
			seconds--; // here because we don't want to see two time announce at the same time
			
			if (_joining || _started || _teleport)
			{
				switch (seconds)
				{
					case 3600: // 1 hour left
						if (_joining)
						{
							MensajeRegistro(false);
						}
						else if (_started) {
							AnnounceToPlayers(false, "Raid Event: " + seconds / 60 / 60 + " hour(s) till event ends!");
						}
						
						break;
					case 1800: // 30 minutes left
					case 900: // 15 minutes left
					case 600: // 10 minutes left
					case 300: // 5 minutes left
					case 60: // 1 minute left
						if (_joining)
						{
							removeOfflinePlayers();
							MensajeRegistro(false);
							AnnounceToPlayers(true, seconds / 60 + " minute(s) till registration ends!");
						}
						else if (_started) {
							AnnounceToPlayers(false, seconds / 60 + " minute(s) till event ends!");
						}
						
						break;
					case 30: // 30 seconds left
					case 10: // 10 seconds left
					case 3: // 3 seconds left
					case 2: // 2 seconds left
					case 1: // 1 seconds left
						if (_joining)
						{
							MensajeRegistro(false);
							AnnounceToPlayers(true, seconds + " second(s) till registration ends!");
						}
						else if (_teleport) {
							AnnounceToPlayers(false, seconds + " second(s) till fight starts!");
						} else if (_started) {
							AnnounceToPlayers(false, seconds + " second(s) till event ends!");
						}
						
						break;
				}
			}
			
			long startOneSecondWaiterStartTime = System.currentTimeMillis();

			while (startOneSecondWaiterStartTime + 1000 > System.currentTimeMillis())
			{
				try
				{
					Thread.sleep(1);
				}
				catch (InterruptedException ie)
				{
				}
			}
		}
	}
	
	private static boolean startAutoEvent(){
		if (!startEventOk())
		{
			if (Config.DEBUG) {
				_log.fine("Raid Engine[startEvent]: startEventOk() = false");
			}
			return false;
		}
		_teleport = false;
		sit(false);
		AnnounceToPlayers(false,"Started. Go kill the raid!");
		_started = true;
		ThreadPoolManager.getInstance().scheduleGeneral(new RaidBossCheckLastHit() , SECOND_TO_CHECK_LAST_HIT * 1000);
		return true;
	}	
	
	private static boolean startEventOk()
	{
		if (_joining || !_teleport || _started) {
			return false;
		}
		
		if (PLAYER_REGISTER == null || PLAYER_REGISTER.isEmpty() || PLAYER_REGISTER.size() == 0) {
			return false;
		}
		
		return true;
	}
	
	private static void setUserData()
	{
		for (L2PcInstance player : getValidPlayer(true))
		{
			
			setPlayerInfo(player);
			player.getAppearance().setNameColor(Integer.decode("0x" + "0099ff"));
			player.broadcastUserInfo();
		}
	}
	
	private static void showHTMLReward(L2PcInstance player){
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/" +"RaidBossEvent-KillIt.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		central.sendHtml(player, html.getHtml(), false);
	}	
	private static void showHTMLNoHelp(L2PcInstance player){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/" +"RaidBossEvent-NoHelp.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		central.sendHtml(player, html.getHtml(), false);
	}
	private static void showHTMLNoKill(L2PcInstance player){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/" +"RaidBossEvent-NoKill.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		central.sendHtml(player, html.getHtml(), false);
	}
	
	
	private static void finishEvent()
	{
		if (!finishEventOk())
		{
			if (Config.DEBUG) {
				_log.fine("Raid Engine[finishEvent]: finishEventOk() = false");
			}
			return;
		}
		_started = false;
		
		if (!_playersWon)
		{
			_bossSpawn.getLastSpawn().isInvisible();
			unspawnEventBoss();
			
			AnnounceToPlayers(true, "The event is over and you have failed to kill the raid.");
			for (L2PcInstance player : getValidPlayer(false))
			{
				if (PLAYER_TO_HIT_THE_RAID.contains(player.getObjectId())){
					EntregaPremioLooser(player);
					showHTMLNoKill(player);
				}else{
					showHTMLNoHelp(player);
				}
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
			
		}else{
			ThreadPoolManager.getInstance().scheduleGeneral(new unSpawnRaidBoss(), 5 * 1000);
		}
		teleportFinish();
		
	}
	
	private static boolean finishEventOk()
	{
		return _started;
	}
	
	@SuppressWarnings("unused")
	private static String NombresItemDarlooser()
	{
		String NombresPremios = "";
		for (String rewardTexto : general.EVENT_RAIDBOSS_REWARD_LOOSER.split(";"))
		{
			if(NombresPremios.length()>0){
				NombresPremios +=", ";
			}
			String[] reward = rewardTexto.split(",");
			NombresPremios += reward[1] + " of " + ItemData.getInstance().getTemplate(Integer.valueOf(reward[0])).getName();
		}
		return "Reward(s) if your loose:" + NombresPremios;
	}
	
	@SuppressWarnings("unused")
	private static String NombresItemDar()
	{
		String NombresPremios = "";
		for (String rewardTexto : general.EVENT_RAIDBOSS_REWARD_WIN.split(";"))
		{
			if(NombresPremios.length()>0){
				NombresPremios +=", ";
			}
			String[] reward = rewardTexto.split(",");
			NombresPremios += reward[1] + " of " + ItemData.getInstance().getTemplate(Integer.valueOf(reward[0])).getName();
		}
		return "Reward(s) if your Win:" + NombresPremios;
	}
	
	private static void EntregaPremioLooser(L2PcInstance eventPlayer)
	{
		opera.giveReward(eventPlayer, general.EVENT_RAIDBOSS_REWARD_LOOSER);
	}
	
	private static void EntregaPremio(L2PcInstance eventPlayer)
	{
		opera.giveReward(eventPlayer, general.EVENT_RAIDBOSS_REWARD_WIN);
	}
	
	private static void rewardTeam()
	{
		for (L2PcInstance player : getValidPlayer(false))
		{
			if (player != null && player.isOnline())
			{
				if(PLAYER_TO_HIT_THE_RAID.contains(player.getObjectId())){
					EntregaPremio(player);
					showHTMLReward(player);
				}else{
					showHTMLNoHelp(player);
				}
				player.sendPacket(ActionFailed.STATIC_PACKET);
			}
		}
	}
	
	private static void raidKilled(){
		_playersWon = true;
		rewardTeam();
		AnnounceToPlayers(true, "The players have been killed the raidboss!");
		finishEvent();
	}
	
	public static void abortEvent()
	{
		if (!_joining && !_teleport && !_started) {
			return;
		}
		if (_joining && !_teleport && !_started)
		{
			unspawnEventBoss();
			cleanRaid();
			_joining = false;
			AnnounceToPlayers(true, "Match aborted!");
			return;
		}
		_joining = false;
		_teleport = false;
		_started = false;
		unspawnEventBoss();
		AnnounceToPlayers(true, "Match aborted!");
		teleportFinish();
	}
	
	private static void sit(Boolean Congelar)
	{
		for (L2PcInstance player : getValidPlayer(false)){
			if (player != null){
				
				if(general.EVENT_RAIDBOSS_CANCEL_BUFF){
					player.stopAllEffects();
					if(player.getSummon()!=null){
						player.getSummon().stopAllEffects();
					}
				}
				
				if(general.EVENT_RAIDBOSS_UNSUMMON_PET){
					if(player.getSummon()!=null){
						player.getSummon().unSummon(player);
					}
				}
				
				if (Congelar){
					if(general.EVENT_RAIDBOSS_PLAYER_INMOBIL){
						opera.setimmobilizeChar(player, true);
					}
				}else{
					opera.setimmobilizeChar(player, false);
				}
			}
		}
	}
	
	private static boolean setIdBoss(){
		try {
			Random r = new Random();
			int RanBossSelec = r.nextInt( general.EVENT_RAIDBOSS_RAID_ID.size() );
			_bossId = general.EVENT_RAIDBOSS_RAID_ID.get(RanBossSelec);
			NomRaid();
			return true;
		}catch(Exception a) {
			_log.warning("Error on raid boss Event. Can load the Raid Boss for the Event");
		}
		return false;

	}
	
	private static void NomRaid()
	{
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_bossId);
		_nameRaid = tmpl.getName();
		_log.warning("Selected Raid Boss for the Event: " + _nameRaid);
	}
	
	private static void showJoinWindows(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/RaidBossEvent-joinWindows.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));		
		html.replace("%PLAYER_JOINED%", String.valueOf(PLAYER_REGISTER.size()));
		String GridWinner = opera.getGridFormatFromHtml(html, 1, "%GRID_WINNER%");
		String ItemReward = "";
		for(String PremiosWinner : general.EVENT_RAIDBOSS_REWARD_WIN.split(";")){
			ItemReward += GridWinner.replace("%WINNER_ITEM%", central.getNombreITEMbyID(Integer.valueOf(PremiosWinner.split(",")[0]))) .replace("%WINNER_AMOUNT%", PremiosWinner.split(",")[1]);
		}
		html.replace("%GRID_WINNER%", ItemReward);
		ItemReward = "";
		String GridLoose = opera.getGridFormatFromHtml(html, 2, "%GRID_LOOSER%");
		for(String PremiosLooser : general.EVENT_RAIDBOSS_REWARD_LOOSER.split(";")){
			ItemReward += GridLoose.replace("%LOOSE_ITEM%", central.getNombreITEMbyID(Integer.valueOf(PremiosLooser.split(",")[0]))) .replace("%LOOSE_AMOUNT%", PremiosLooser.split(",")[1]);
		}
		html.replace("%GRID_LOOSER%", ItemReward);		
		//_itemRBEventDrop
		String GridDrop = opera.getGridFormatFromHtml(html, 3, "%GRID_DROP%");
		ItemReward = "";
		if(DROP_RAID!=null){
			if(DROP_RAID.size()>0){
				for(_itemRBEventDrop DropData : DROP_RAID){
					ItemReward += GridDrop.replace("%DROP_ITEM_MAX%", opera.getFormatNumbers(DropData.getMax())).replace("%DROP_ITEM_MIN%", opera.getFormatNumbers(DropData.getMin())).replace("%DROP_ITEM_TYPE%", DropData.getType()) .replace("%DROP_ITEM_NAME%", DropData.getName()) .replace("%DROP_ITEM_ICON%", DropData.getLogo());
				}
			}
		}
		html.replace("%GRID_DROP%", ItemReward);
		central.sendHtml(player, html.getHtml());
	}
	
	public static void addPlayer(L2PcInstance player)
	{
		if (!addPlayerOk(player)) {
			if(isPlayerOnRBEvent(player)){
				showJoinWindows(player);
			}
			return;
		}
		PLAYER_REGISTER.put(player.getObjectId(), player.getName());
		showJoinWindows(player);
	}
	
	private static void removeOfflinePlayers(){
		getValidPlayer(true);
	}
	
	private static boolean addPlayerOk(L2PcInstance eventPlayer)
	{
		if(!_joining){
			String Mensaje = getNextEventTimeLess();
			central.msgbox("Event Inactive. You need to Wait. " + Mensaje, eventPlayer);
			return false;
		}
		if(_started){
			central.msgbox("Event Inactive. Event Started", eventPlayer);
			return false;			
		}
		
		try
		{
			
			if (eventPlayer.getInstanceId() > 0)
			{
				eventPlayer.sendMessage( language.getInstance().getMsg(eventPlayer).YOUR_ON_A_INSTANCE_CANT_PARTICIPATE);
				return false;
			}
			
			
			if (eventPlayer.isOnEvent()) {
				eventPlayer.sendMessage( language.getInstance().getMsg(eventPlayer).YOUR_ARE_IN_OTHER_EVENT );
				return false;
			}
			
			if(SunriseEvents.isInEvent(eventPlayer)){
				eventPlayer.sendMessage(language.getInstance().getMsg(eventPlayer).YOUR_ARE_IN_OTHER_EVENT);
				return false;				
			}
			
			if(PLAYER_REGISTER.containsKey(eventPlayer.getObjectId()))
			{
				eventPlayer.sendMessage(language.getInstance().getMsg(eventPlayer).YOUR_ARE_IN_THIS_EVENT);
				return false;
			}
			
			if (eventPlayer.isInOlympiadMode())
			{
				eventPlayer.sendMessage(language.getInstance().getMsg(eventPlayer).YOUR_CANT_JOIN_THIS_EVENT_BECOUSE_OLYS);
				return false;
			}
			
			for (L2PcInstance player : getValidPlayer(false))
			{
				if (eventPlayer.getAccessLevel().getLevel() <= 0)
				{
					if(general.EVENT_RAIDBOSS_CHECK_DUALBOX){
						if (ZeuS.isDualBox_pc(player, eventPlayer) && (!player.isGM() || !eventPlayer.isGM()))
						{
							eventPlayer.sendMessage(language.getInstance().getMsg(eventPlayer).YOUR_ARE_ALREADY_PARTICIPATING_IN_THE_EVENT_WITH_OTHER_IP);
							return false;
						}
					}
				}
				else if (player.getObjectId() == eventPlayer.getObjectId())
				{
					eventPlayer.sendMessage(language.getInstance().getMsg(eventPlayer).YOUR_ARE_IN_THIS_EVENT);
					return false;
				}
				else if (player.getName() == eventPlayer.getName())
				{
					eventPlayer.sendMessage(language.getInstance().getMsg(eventPlayer).YOUR_ARE_IN_THIS_EVENT);
					return false;
				}
			}
		}
		catch (Exception e)
		{
			_log.warning("Raid Engine exception: " + e.getMessage());
		}
		
		if (PLAYER_REGISTER.size() < general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS) {
			return true;
		}
		
		eventPlayer.sendMessage("Too many players registered");
		return false;
	}
	
	public static void removePlayer(L2PcInstance player)
	{
		if(!_joining){
			return;
		}
		
		if(_started){
			central.msgbox("You can not unregister now", player);
			return;
		}
		
		if(PLAYER_REGISTER==null){
			return;
		}
		if (PLAYER_REGISTER.containsKey(player.getObjectId())){
			if (!_joining){
				player.getAppearance().setNameColor(Integer.valueOf(getPlayerColorName(player)));
				player.broadcastUserInfo();
			}
			PLAYER_REGISTER.remove(player.getObjectId());
			PLAYER_INFO.remove(player.getObjectId());
			
			try{
				PLAYER_TO_HIT_THE_RAID.removeElementAt(player.getObjectId());
				PLAYER_TIME_HIT.remove(player.getObjectId());				
			}catch(Exception a){
				
			}
			NpcHtmlMessage nhm = new NpcHtmlMessage(5);
			final StringBuilder replyMSG = StringUtil.startAppend(1000, "<html><body>");
			replyMSG.append("<html><body>Te has Eliminado del Evento Raid Boss con Exito.</body></html>");
			nhm.setHtml(replyMSG.toString());
			player.sendPacket(nhm);
			PLAYER_REGISTER.remove(player.getObjectId());
		}
	}
	
	private static void cleanRaid()
	{
		InstanceId=0;
		
		unspawnEventBoss();
		_log.warning("Raid: Cleaning players.");
		PLAYER_INFO.clear();
		PLAYER_REGISTER.clear();
		PLAYER_TO_HIT_THE_RAID.clear();
		PLAYER_TIME_HIT.clear();
		isCheck = false;
		_joining = false;
		_started = false;
		_teleport = false;		
		_playersWon = false;
		_log.warning("Cleaning Raid done.");
		if(general.EVENT_RAIDBOSS_AUTOEVENT){
			RaidBoss_Auto_Event = false;
			IntervalEventStart();
		}
	}
	
	public static void setNewSearch(){
		RaidBoss_Auto_Event = false;
	}
	
	private static void unspawnEventBoss()
	{
		try{
			if (_bossSpawn == null) {
				return;
			}
			_bossSpawn.getLastSpawn().deleteMe();
			_bossSpawn.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(_bossSpawn, true);
		}catch(Exception a){
			_log.warning("RaidBoss Event: Error unspawn RB->" + a.getMessage());
		}
	}
	
	public static boolean playerIsRaidEventInEvent(L2PcInstance player){
		if(PLAYER_REGISTER==null){
			return false;
		}
		if(PLAYER_REGISTER.size()==0){
			return false;
		}
		if(!PLAYER_REGISTER.containsKey(player.getObjectId())){
			return false;
		}
		if(!_started){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("static-access")
	public static void atacaAlRaid(L2PcInstance jugador, L2Npc raid)
	{
		if(PLAYER_REGISTER==null){
			return;
		}
		
		if(PLAYER_REGISTER.size()==0){
			return;
		}
		
		if(!_started){
			return;
		}
				
		if (jugador == null || raid == null) {
			return;
		}
		
		if(!_started){
			return;
		}
		
		if (raid._isEventMobRaid)
		{
			setPlayerHitRaid(jugador);
		}
	}
	
	private static void AnunciarTodos(String Mensaje)
	{
		CreatureSay strMensaje = new CreatureSay(0, 18, "", "[RB Event]" + Mensaje);
		Broadcast.toAllOnlinePlayers(strMensaje);
	}
	
	
	public static class unSpawnRaidBoss implements Runnable{
		public unSpawnRaidBoss(){

		}
		@Override
		public void run(){
			try{
				_bossSpawn.getLastSpawn().isInvisible();
			}catch(Exception a){

			}
			try{
				unspawnEventBoss();
			}catch(Exception a){
				
			}
		}
	}	
	
	
	private static void teleportFinish()
	{
		AnnounceToPlayers(false, "Teleport back to participation Spot in " + String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_BACK) + " seconds!");
		
		
		
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable()
		{
			@SuppressWarnings("synthetic-access")
			public void run()
			{
				for (L2PcInstance player : getValidPlayer(false)){
					if (player != null)
					{
						String colorName = "";
						try{
							colorName = getPlayerColorName(player);
							player.getAppearance().setNameColor(Integer.valueOf(colorName));							
						}catch(Exception a){
							player.getAppearance().setNameColor(0);
						}
						player.setTeam(Team.NONE);
						player.setInstanceId(0);
						String[] locacion = getPlayerTele(player).split(",");
						if (player.isOnline()) {
							player.teleToLocation(Integer.valueOf(locacion[0]), Integer.valueOf(locacion[1]), Integer.valueOf(locacion[2]), false);
						}else{
							Connection con = null;
							try
							{
								con = L2DatabaseFactory.getInstance().getConnection();
								
								PreparedStatement statement = con.prepareStatement("UPDATE characters SET x=?, y=?, z=? WHERE char_name=?");
								statement.setInt(1, Integer.valueOf(locacion[0]));
								statement.setInt(2, Integer.valueOf(locacion[1]));
								statement.setInt(3, Integer.valueOf(locacion[2]));
								statement.setString(4, player.getName());
								statement.execute();
								statement.close();
							}
							catch (SQLException se)
							{
								_log.warning("Raid Engine exception: " + se.getMessage());
							}
							finally
							{
								try
								{
									if (con != null) {
										con.close();
									}
								}
								catch (SQLException e)
								{
									e.printStackTrace();
								}
							}
						}
					}
				}
				_log.warning("Raid: Teleport done.");
				cleanRaid();
			}
		}, general.EVENT_RAIDBOSS_SECOND_TO_BACK * 1000);
	}
}

class _itemRBEventDrop{
	private int ID_ITEM;
	private String NAME_ITEM;
	private String LOGO_ITEM;
	private long AMOUNT_MIN_ITEM;
	private long AMOUNT_MAX_ITEM;
	private String DROP_ITEM_TYPE;
	public _itemRBEventDrop(int idItem, String ItemName, String ItemLogo, long AmountMin, long AmountMax, String TypeDrop){
		this.ID_ITEM = idItem;
		this.NAME_ITEM = ItemName;
		this.LOGO_ITEM = ItemLogo;
		this.AMOUNT_MIN_ITEM = AmountMin;
		this.AMOUNT_MAX_ITEM = AmountMax;
		this.DROP_ITEM_TYPE = TypeDrop;
	}
	public final int getID(){
		return this.ID_ITEM;
	}
	public final String getName(){
		return this.NAME_ITEM;
	}
	public final String getLogo(){
		return this.LOGO_ITEM;
	}
	public final String getType(){
		return this.DROP_ITEM_TYPE;
	}
	public final long getMin(){
		return this.AMOUNT_MIN_ITEM;
	}
	public final long getMax(){
		return this.AMOUNT_MAX_ITEM;
	}
}