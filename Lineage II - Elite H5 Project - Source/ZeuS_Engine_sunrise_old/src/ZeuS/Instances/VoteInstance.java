package ZeuS.Instances;

import gr.sr.interf.SunriseEvents;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.MobGroup;
import l2r.gameserver.model.MobGroupTable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.server.comun;

public class VoteInstance {

	private static Map<Integer, _VOTE_ZONES> ZONES_SPAWN = new HashMap<Integer, _VOTE_ZONES>();
	private static Vector<String>VOTES_TO_START = new Vector<String>();
	private static Vector<Integer> ZONES_SPAWNED = new Vector<Integer>();
	private static int ID_ZONE = -1;
	private static int LAST_VOTE_HOPZONE = 0;
	private static int LAST_VOTE_TOPZONE = 0;
	private static Vector<Integer> VOTES_SHOWS = new Vector<Integer>();

	private final static Logger _log = Logger.getLogger(VoteInstance.class.getName());
	
	private static boolean isEnabled;
	protected static boolean IS_RUNNING = false;
	
	public static void XMLLoad(){
		XMLLoad(false);
	}
	
	public static void XMLLoad(boolean reset){
		if(!reset){
			try{
				if(ZONES_SPAWN.size()>0){
					return;
				}else{
				}
			}catch(Exception a){
				
			}
		}else{
			try{
				ZONES_SPAWN.clear();
				VOTES_TO_START.clear();
			}catch(Exception a){
				
			}
		}
		
		File dir = new File("./config/zeus/zeus_vote_instance.xml");
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
					if("instance".equalsIgnoreCase(d.getNodeName())){
						try{
							isEnabled  = Boolean.valueOf(d.getAttributes().getNamedItem("enabled").getNodeValue());
						}catch(Exception a){
							isEnabled = false;
						}
						continue;
					}
					if("goal".equalsIgnoreCase(d.getNodeName())){
						try{
							String temp = d.getAttributes().getNamedItem("votes").getNodeValue();
							for(String votes : temp.split(",")){
								VOTES_TO_START.add(votes.trim());
							}
						}catch(Exception a){
							
						}
					}
					if (!"zone".equalsIgnoreCase(d.getNodeName())){
						continue;
					}
					int ID_ZONE = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
					String NAME_ZONE = String.valueOf(d.getAttributes().getNamedItem("name").getNodeValue());
					boolean PARTYZONE_ZONE = Boolean.valueOf(d.getAttributes().getNamedItem("partys").getNodeValue());
					boolean REWARDBYEMAIL = Boolean.valueOf(d.getAttributes().getNamedItem("rewardByEmailAtFinish").getNodeValue());
					boolean DUALBOXCHECK = Boolean.valueOf(d.getAttributes().getNamedItem("dualboxcheck").getNodeValue());
					boolean REWARD_BY_KILL = Boolean.valueOf(d.getAttributes().getNamedItem("killMobReward").getNodeValue());
					boolean PVP_INSTANCE = Boolean.valueOf(d.getAttributes().getNamedItem("pvpinstance").getNodeValue());
					int MINUTES_TO_START = Integer.valueOf(d.getAttributes().getNamedItem("random_minute_to_start_when_goal_is_reach").getNodeValue());
					int MINUTES_DURATION = Integer.valueOf(d.getAttributes().getNamedItem("duration_minutes").getNodeValue());
					int CHANCE_DROP_PORCEN = Integer.valueOf(d.getAttributes().getNamedItem("chanceDrop_porcent").getNodeValue());
					Float RATE_DROP_MULTI = Float.valueOf(d.getAttributes().getNamedItem("rateDropMulti").getNodeValue());
					_VOTE_ZONES TZone = new _VOTE_ZONES(NAME_ZONE, PARTYZONE_ZONE, REWARDBYEMAIL, DUALBOXCHECK,  MINUTES_TO_START, MINUTES_DURATION, REWARD_BY_KILL, CHANCE_DROP_PORCEN, RATE_DROP_MULTI, PVP_INSTANCE);
					
					if(ZONES_SPAWN==null){
						ZONES_SPAWN.put(ID_ZONE, TZone);
					}else if(ZONES_SPAWN.size()==0){
						ZONES_SPAWN.put(ID_ZONE, TZone);
					}else if(!ZONES_SPAWN.containsKey(ID_ZONE)){
						ZONES_SPAWN.put(ID_ZONE, TZone);
					}
					
					for (Node b = d.getFirstChild(); b != null; b = b.getNextSibling())
					{
						if (b.getNodeName().equalsIgnoreCase("set")){
							try
							{
								String tSpawn = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
								int idMob = Integer.valueOf(b.getAttributes().getNamedItem("idMob").getNodeValue().toLowerCase());
								int mobCount = Integer.valueOf(b.getAttributes().getNamedItem("count").getNodeValue().toLowerCase());
								int RangeX = Integer.valueOf(b.getAttributes().getNamedItem("rangeX").getNodeValue().toLowerCase());
								int RangeY = Integer.valueOf(b.getAttributes().getNamedItem("rangeY").getNodeValue().toLowerCase());
								Location TEMP_LOCA = new Location(Integer.valueOf(tSpawn.split(",")[0]), Integer.valueOf(tSpawn.split(",")[1]), Integer.valueOf(tSpawn.split(",")[2]));
								ZONES_SPAWN.get(ID_ZONE).setSpawnLocation(TEMP_LOCA,idMob,mobCount, String.valueOf(RangeX) + "," + String.valueOf(RangeY));
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new Zone Spawn for Vote VoteInstance.java" + aaa.getMessage());
							}
						}else if (b.getNodeName().equalsIgnoreCase("respawn")){
							try
							{
								String tSpawn = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
								Location TEMP_LOCA = new Location(Integer.valueOf(tSpawn.split(",")[0]), Integer.valueOf(tSpawn.split(",")[1]), Integer.valueOf(tSpawn.split(",")[2]));
								ZONES_SPAWN.get(ID_ZONE).setRespawnLocation(TEMP_LOCA);
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new Respawn for VoteInstance.java" + aaa.getMessage());
							}
						}else if (b.getNodeName().equalsIgnoreCase("cost")){
							try
							{
								int idItem = Integer.valueOf(b.getAttributes().getNamedItem("id").getNodeValue().toLowerCase());
								int ItemCount = Integer.valueOf(b.getAttributes().getNamedItem("mount").getNodeValue().toLowerCase());
								if(idItem > 0 && ItemCount > 0){
									ZONES_SPAWN.get(ID_ZONE).setCost(idItem, ItemCount);
								}
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new cost for VoteInstance.java)" + aaa.getMessage());
							}							
						}else if (b.getNodeName().equalsIgnoreCase("npc")){
							try
							{
								String CLUE = String.valueOf(b.getAttributes().getNamedItem("clue").getNodeValue());
								String LOCATION_STR = String.valueOf(b.getAttributes().getNamedItem("location").getNodeValue().toLowerCase());
								boolean ENTER_BY_QUESTION_MARK = Boolean.valueOf(b.getAttributes().getNamedItem("enterByQuestionMark").getNodeValue().toLowerCase());
								boolean TELE_CLOSE_NPC = Boolean.valueOf(b.getAttributes().getNamedItem("teleportToNpcByQuestionMark").getNodeValue().toLowerCase());
								Location TEMP_LOCA = new Location(Integer.valueOf(LOCATION_STR.split(",")[0]), Integer.valueOf(LOCATION_STR.split(",")[1]), Integer.valueOf(LOCATION_STR.split(",")[2]));								
								int IDNPC = Integer.valueOf(b.getAttributes().getNamedItem("idnpc").getNodeValue().toLowerCase());
								ZONES_SPAWN.get(ID_ZONE).setRespawnNpcLocation(IDNPC, CLUE, TEMP_LOCA, ENTER_BY_QUESTION_MARK, TELE_CLOSE_NPC);
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new NPC for VoteInstance.java)" + aaa.getMessage());
							}
						}else if (b.getNodeName().equalsIgnoreCase("prohibited_classes_id")){
							String _prohibitedClases = "";
							try{
								_prohibitedClases = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
							}catch(Exception a){
								_log.warning("Error adding prohibited clases to VoteInstance.java");
							}
							ZONES_SPAWN.get(ID_ZONE).setProhibitedClasses(_prohibitedClases);
						}else if (b.getNodeName().equalsIgnoreCase("reward")){
							try{
								int ID_ITEM = Integer.valueOf(b.getAttributes().getNamedItem("id").getNodeValue().toLowerCase());
								int MOUNT = Integer.valueOf(b.getAttributes().getNamedItem("count").getNodeValue().toLowerCase());
								ZONES_SPAWN.get(ID_ZONE).setKillReward(ID_ITEM, MOUNT);
							}catch(Exception a){
								_log.warning("Error adding prohibited clases to VoteInstance.java");
							}
						}
					}
				}
			}
		}
		catch (Exception a)
		{
			_log.warning("Error loading VOTE INSTANCE ZONE ->" + a.getMessage() + "\n");
		}
		_log.warning("	Loading " + ZONES_SPAWN.size() + " vote zones.");
		
	}
	
	
	public static void checkKillMob(L2PcInstance player){
		if(!isEnabled){
			return;
		}
		if(ID_ZONE <=0){
			return;
		}
		
		if(!playerIsInside(player.getObjectId())){
			return;
		}
		ZONES_SPAWN.get(ID_ZONE).checkDeathMob(player);
	}
	
	public static void removePlayerFromZone(L2PcInstance player){
		if(!isEnabled){
			return;
		}
		
		if(player.getInstanceId()==0){
			return;
		}
		
		if(ID_ZONE > 0){
			if(ZONES_SPAWN.get(ID_ZONE).getInstanceID() == player.getInstanceId()){
				ZONES_SPAWN.get(ID_ZONE).removePlayer(player);
			}
		}		
	}
	
	public static boolean playerIsInside(int idPlayer){
		if(!isEnabled){
			return false;
		}		
		if(ID_ZONE > 0){
			return ZONES_SPAWN.get(ID_ZONE).playerIsInside(idPlayer);
		}
		return false;
	}
	
	public static boolean canParty(){
		if(ID_ZONE >0){
			if(ZONES_SPAWN.containsKey(ID_ZONE)){
				return ZONES_SPAWN.get(ID_ZONE).canParty();
			}
		}
		return true;
	}
	
	public static void enterToZone(L2PcInstance player){
		if(!isEnabled){
			return;
		}
	
		if(player.isInCombat()){
			central.msgbox(language.getInstance().getMsg(player).CANT_USE_IN_COMBAT_MODE, player);
			return;
		}
		
		if(!player.isInsideZone(ZoneIdType.PEACE)){
			central.msgbox(language.getInstance().getMsg(player).ARE_INSIDE_A_CASTLE, player);
			return;
		}
		
		if(player.getInstanceId()!=0){
			central.msgbox(language.getInstance().getMsg(player).YOU_ARE_INSIDE_A_INSTANCE_CANT_ENTER_NOW, player);
			return;
		}
		
		if(SunriseEvents.isInEvent(player) || RaidBossEvent.isPlayerOnRBEvent(player)){
			central.msgbox(language.getInstance().getMsg(player).YOU_OR_TARGET_IS_IN_EVENT, player);
			return;
		}
		if(player.isInOlympiad()){
			central.msgbox(language.getInstance().getMsg(player).YOU_OR_TARGET_IS_IN_OLY, player);
			return;
		}
		
		if(ID_ZONE > 0){
			if(ZONES_SPAWN.get(ID_ZONE).playerIsInside(player.getObjectId())){
				central.msgbox(language.getInstance().getMsg(player).YOU_ARE_INSIDE_A_INSTANCE_CANT_ENTER_NOW, player);
				return;
			}			
			ZONES_SPAWN.get(ID_ZONE).enterZone(player);
		}
	}
	
	public static int getInstanceID(){
		try{
			if(ID_ZONE > 0){
				if(ZONES_SPAWN!=null){
					if(ZONES_SPAWN.containsKey(ID_ZONE)){
						return ZONES_SPAWN.get(ID_ZONE).getInstanceID();
					}
				}
			}
		}
		catch(Exception a){
		}
		return -1; 
	}
	
	public static int getDropChance(){
		try{
			if(ID_ZONE > 0){
				if(ZONES_SPAWN!=null){
					if(ZONES_SPAWN.containsKey(ID_ZONE)){
						return ZONES_SPAWN.get(ID_ZONE).getDropChance();
					}
				}
			}
		}
		catch(Exception a){
		}
		return 0;		
	}
	
	
	public static float getDropRate(){
		try{
			if(ID_ZONE > 0){
				if(ZONES_SPAWN!=null){
					if(ZONES_SPAWN.containsKey(ID_ZONE)){
						return ZONES_SPAWN.get(ID_ZONE).getRateMulti();
					}
				}
			}
		}
		catch(Exception a){
		}
		return 0;		
	}
	
	public static boolean isNpcFromVoteInstance(L2PcInstance player, int idNpc){
		if(!isEnabled){
			return false;
		}
		if(ID_ZONE >0){
			if(ZONES_SPAWN.size()>0){
				if(ZONES_SPAWN.containsKey(ID_ZONE)){
					if(player==null){
						return ZONES_SPAWN.get(ID_ZONE).isNPC_Instance(idNpc);
					}else{
						ZONES_SPAWN.get(ID_ZONE).sendHtmlWindows(player);
						return true;
					}
				}
			}
		}
		return false;
	}
	
	public static void checkVote(int votes, boolean isHopZone){
		if(!isEnabled){
			return;
		}
		if(isHopZone && (votes == LAST_VOTE_HOPZONE)){
			return;
		}else if(!isHopZone && (votes == LAST_VOTE_TOPZONE)){
			return;
		}
		
		if(IS_RUNNING){
			return;
		}
		
		boolean start = false;
		for(String voteToCheck : VOTES_TO_START){
			if(start){
				continue;
			}
			int vote1 = Integer.valueOf(voteToCheck.split("-")[0]);
			int vote2 = Integer.valueOf(voteToCheck.split("-")[1]);
			
			if(VOTES_SHOWS != null){
				if(VOTES_SHOWS.size()>0){
					if(VOTES_SHOWS.contains(vote1) && VOTES_SHOWS.contains(vote2)){
						continue;
					}
				}
			}
			
			if(votes >= vote1 && votes <= vote2){
				start = true;
				VOTES_SHOWS.add(vote1);VOTES_SHOWS.add(vote2);
				_log.info("Loading Vote Instance zone ("+ vote1 + "-" + vote2 +")");
				if(isHopZone){
					LAST_VOTE_HOPZONE = votes;
				}else{
					LAST_VOTE_TOPZONE = votes;
				}
			}
		}
		if(start){
			startZone();
		}
	}
	
	public static void teleportPlayerToNPC(L2PcInstance player){
		if(!isEnabled){
			return;
		}		
		if(ID_ZONE <=0){
			return;
		}
		if(ZONES_SPAWN.get(ID_ZONE).playerIsInside(player.getObjectId())){
			return;
		}
		ZONES_SPAWN.get(ID_ZONE).teleportCloseToNPC(player);
	}
	
	private static void startZone(){
		if(!isEnabled){
			return;
		}	
		
		
		int ZoneCount = ZONES_SPAWN.size();
		if(ZoneCount==0){
			return;
		}
		
		if(ID_ZONE>0){
			if(ZONES_SPAWN.containsKey(ID_ZONE)){
				if(ZONES_SPAWN.get(ID_ZONE).is_On()){
					_log.warning("Can not start another zone.");
					return;
				}
			}
		}
		
		int SelectedZone = -1;
		if(ZoneCount > 1){
			Random aleatorio = new Random();
			boolean Selected = false;
			while(!Selected){
				int SelectID = aleatorio.nextInt(ZoneCount+1);
				if(ZONES_SPAWN.containsKey(SelectID) && !ZONES_SPAWNED.contains(SelectID) ){
					Selected = true;
					SelectedZone = SelectID;
				}
			}			
		}else{
			SelectedZone = 1;
		}
		if(SelectedZone>0){
			ZONES_SPAWN.get(SelectedZone).begin();
			IS_RUNNING = true;
			ZONES_SPAWNED.add(SelectedZone);
			ID_ZONE = SelectedZone;
			if(ZONES_SPAWNED.size()>=ZONES_SPAWN.size()){
				ZONES_SPAWNED.clear();
				ZONES_SPAWNED.add(SelectedZone);
			}
		}
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("Votezone Selected Id ->" + SelectedZone);
		}
	} 
	
}

class _VOTE_ZONES{
	private final Logger _log = Logger.getLogger(VoteInstance.class.getName());
	public String nameZone;
	private boolean canParty;
	private Vector<Integer>PROHIBITED_CLASSES = new Vector<Integer>();
	private Map<Integer, Location> SpawnLocation = new HashMap<Integer, Location>();
	private Map<Integer, String> SpawnRange = new HashMap<Integer, String>();
	private Map<Integer, Location> RespawnLocation = new HashMap<Integer, Location>();
	private Map<Integer, Integer>SpawnMob = new HashMap<Integer, Integer>();
	private Map<Integer, Integer>SpawnMobCount = new HashMap<Integer, Integer>();
	private Vector<String>COST_TO_ENTER = new Vector<String>();
	private Map<Integer, _VOTE_NPC>SPAWN_NPC = new HashMap<Integer, _VOTE_NPC>();
	private Vector<Integer> NPC_ENTER = new Vector<Integer>();
	private boolean isOn = false;
	private boolean rewardByEmail;
	private boolean killMobReward;
	private boolean dualboxcheck;
	private boolean pvpinstance;
	
	int chanceDropPorcent;
	float rateDropMulti;
	
	private int idInstance;
	private int minutesToStart;
	private int Minutes_Duration;
	private int SelectedNPC;
	private L2Spawn NPCEnter;
	private Vector<Integer> PLAYER_INSIDE = new Vector<Integer>();
	private Vector<Integer> MOBMENU_IDS = new Vector<Integer>();
	private Vector<Integer> PLAYER_PAY = new Vector<Integer>();
	private Vector<String> REWARD_KILL = new Vector<String>();
	private Map<String, Integer> MOB_KILL = new HashMap<String, Integer>();
	
	
	private int GONNA_START_IN;
	
	private static InstanceWorld world;
	
	private static class VoteWorld extends InstanceWorld{
		public VoteWorld(){
		}
	}
	
	public boolean is_On(){
		return this.isOn;
	}
	
	private void setModDeath(L2PcInstance player){
		if(this.MOB_KILL==null){
			this.MOB_KILL.put(player.getName(), 1);
		}else if(this.MOB_KILL.size()==0){
			this.MOB_KILL.put(player.getName(), 1);
		}else if(!this.MOB_KILL.containsKey(player.getName())){
			this.MOB_KILL.put(player.getName(), 1);
		}else{
			this.MOB_KILL.put(player.getName(), this.MOB_KILL.get(player.getName()) + 1);
		}
		if(!this.rewardByEmail && this.killMobReward){
			opera.giveReward(player, this.REWARD_KILL);
		}
	}
	
	public void teleportCloseToNPC(L2PcInstance player){
		if(!this.isOn){
			return;
		}
		if(SPAWN_NPC.get(this.SelectedNPC).teleportCloseNPC()){
			Location NPC = SPAWN_NPC.get(this.SelectedNPC).getLocation();
			player.teleToLocation(NPC, 250);
		}
	}
	
	public void checkDeathMob(L2PcInstance player){
		if(!this.isOn){
			return;
		}
		if(!this.killMobReward){
			return;
		}
		L2Object target = player.getTarget();
		if(target instanceof L2Npc){
			L2Npc Mob = (L2Npc)target;
			if(Mob.getInstanceId() == this.idInstance){
				setModDeath(player);
			}
		}
	}
	
	public int getInstanceID(){
		return this.idInstance;
	}
	
	public void setKillReward(int idItem, int Count){
		String ToSave = String.valueOf(idItem) + "," + String.valueOf(Count);
		REWARD_KILL.add(ToSave);
	}
	
	public boolean isNPC_Instance(int idNpc){
		return NPC_ENTER.contains(idNpc);
	}
	
	public void begin(){
		createInstance();
		createMobs();
		createTimer();
	}
	
	@SuppressWarnings("rawtypes")
	private void sendRewardByEmail(){
		if(!this.killMobReward){
			return;
		}
		if(!this.rewardByEmail){
			return;
		}
		Iterator itr = this.MOB_KILL.entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	String PlayerName = (String)Entrada.getKey();
	    	int MountKill = (int)Entrada.getValue();
	    	
			String RewardToSend = "";
			for(String rew : this.REWARD_KILL){
				if(RewardToSend.length()>0){
					RewardToSend += ";";
				}
				int MountReward = Integer.valueOf(rew.split(",")[1]);
				RewardToSend += rew.split(",")[0] + "," + String.valueOf( MountReward * MountKill);
			}	    	
	    	EmailSend.sendEmail(null, PlayerName , "Reward Vote Zone", language.getInstance().getMsg(PlayerName).VOTE_INSTANCE_REWARD_BY_EMAIL_MESSAGE.replace("$mobs",String.valueOf(MountKill)), RewardToSend, tipoMensaje.Reward);	    	
		}		
		
		
	}
	
	private void zoneClose(){
		for(int idGroup : this.MOBMENU_IDS){
			MobGroup group = MobGroupTable.getInstance().getGroup(idGroup);
			try{
				group.unspawnGroup();
			}catch(Exception a){
				
			}
			MobGroupTable.getInstance().removeGroup(idGroup);
		}
		sendRewardByEmail();
		this.NPCEnter.getLastSpawn().deleteMe();
		this.NPCEnter.stopRespawn();
		SpawnTable.getInstance().deleteSpawn(this.NPCEnter, true);
		this.PLAYER_INSIDE.clear();
		this.PLAYER_PAY.clear();
		this.MOB_KILL.clear();
		this.isOn = false;
		VoteInstance.IS_RUNNING = false;
		opera.AnunciarTodos("Vote Instance", "The Vote Instance Zone is now Close. Will be Open again when we reach another Vote Goal!!!");
	}

	public void sendHtmlWindows(L2PcInstance player){
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/voteInstance.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%NEEDS%", "");
		central.sendHtml(player, html.getHtml(), false);		
	}
	
	public void removePlayer(L2PcInstance player){
		if(this.PLAYER_INSIDE!=null){
			if(this.PLAYER_INSIDE.size()>0){
				if(this.PLAYER_INSIDE.contains(player.getObjectId())){
					this.PLAYER_INSIDE.removeElement(player.getObjectId());
				}
			}
		}		
	}
	
	public boolean playerIsInside(int idPlayer){
		if(this.PLAYER_INSIDE!=null){
			if(this.PLAYER_INSIDE.size()>0){
				return this.PLAYER_INSIDE.contains(idPlayer);
			}
		}
		
		return false;
	}
	
	private boolean haveDualBoxInside(L2PcInstance player){
		boolean isDualBox = false;
		if(this.PLAYER_INSIDE!=null){
			if(this.PLAYER_INSIDE.size()>0){
				for(int idPlayer : this.PLAYER_INSIDE){
					if(player.getObjectId() == idPlayer){
						continue;
					}
					if(isDualBox){
						continue;
					}
					try{
						if(opera.isCharInGame(idPlayer)){
							L2PcInstance pplT = opera.getPlayerbyID(idPlayer);
							if(pplT.isGM()){
								continue;
							}
							if(opera.isCharOnline(pplT)){
								if(ZeuS.isDualBox_pc(pplT, player) ){
									isDualBox = true;
								}
							}
						}
					}catch(Exception a){
						
					}
				}
			}
		}
		return isDualBox;
	}
	
	public void enterZone(L2PcInstance player){
		if(!this.isOn){
			player.sendMessage(language.getInstance().getMsg(player).VOTE_INSTANCE_THIS_ZONE_IS_CLOSE_FOR_NOW);
			return;
		}
		
		if(!this.canParty){
			if(player.isInParty()){
				central.msgbox(language.getInstance().getMsg(player).YOU_CANT_USE_THIS_IN_PARTY, player);
				return;
			}
		}
		
		if(this.dualboxcheck && !player.isGM()){
			if(haveDualBoxInside(player)){
				central.msgbox(language.getInstance().getMsg(player).VOTE_INSTANCE_CANT_USE_THIS_IN_DUALBOX, player);
				return;
			}
		}
		
		if(this.PROHIBITED_CLASSES!=null){
			if(this.PROHIBITED_CLASSES.size()>0){
				if(this.PROHIBITED_CLASSES.contains( player.getClassId().getId())){
					central.msgbox(language.getInstance().getMsg(player).VOTE_INSTANCE_YOUR_CLASS_IS_NOT_ALLOWED_TO_ENTER, player);
					return;
				}
			}
		}
		
		boolean playerPay = false;
		boolean removeItem = false;
		
		if(this.PLAYER_PAY!=null){
			if(this.PLAYER_PAY.size()>0){
				playerPay = this.PLAYER_PAY.contains(player.getObjectId());
			}
		}
		
		if(this.COST_TO_ENTER!=null){
			if(this.COST_TO_ENTER.size()>0){
				if(!playerPay){
					if(!opera.haveItem(player, this.COST_TO_ENTER)){
						return;
					}else{
						removeItem = true;
					}
				}
			}
		}
		
		int totalRespawn = this.RespawnLocation.size();
		Random aleatorio = new Random();
		
		int SelectedZone = -1;
		
		if(totalRespawn == 1){
			SelectedZone = 1;
		}else{
			SelectedZone = aleatorio.nextInt(totalRespawn);
			boolean hasZone = false;
			while(!hasZone){
				if(this.RespawnLocation.containsKey(SelectedZone)){
					hasZone = true;
				}else{
					SelectedZone = aleatorio.nextInt(totalRespawn);					
				}
			}
		}
		if(!playerPay && removeItem){
			opera.removeItem(this.COST_TO_ENTER, player);
			this.PLAYER_PAY.add(player.getObjectId());
		}
		this.PLAYER_INSIDE.add(player.getObjectId());		
		player.setInstanceId(this.idInstance);
		player.teleToLocation(this.RespawnLocation.get(SelectedZone), 500);
	}
	
	private void zoneStart(){
		int NpcS = SPAWN_NPC.size();
		int _SelectedNPC = -1;
		if(NpcS==1){
			_SelectedNPC = 1;
		}else{
			Random aleatorio = new Random();
			_SelectedNPC = aleatorio.nextInt(NpcS);
		}
		if(_SelectedNPC>0){
			String ClueToShow = SPAWN_NPC.get(_SelectedNPC).getClue();
			this.isOn = true;
			try{
				L2NpcTemplate template = NpcTable.getInstance().getTemplate(SPAWN_NPC.get(_SelectedNPC).getNPCID());
				final L2Spawn spawn = new L2Spawn(template);
				if (Config.SAVE_GMSPAWN_ON_CUSTOM)
				{
					spawn.setCustom(true);
				}
				spawn.setX(SPAWN_NPC.get(_SelectedNPC).getLocation().getX());
				spawn.setY(SPAWN_NPC.get(_SelectedNPC).getLocation().getY());
				spawn.setZ(SPAWN_NPC.get(_SelectedNPC).getLocation().getZ());
				spawn.setAmount(1);
				spawn.setHeading(1);
				spawn.setRespawnDelay(0);
				spawn.setInstanceId(0);
				SpawnTable.getInstance().addNewSpawn(spawn, false);
				spawn.init();
				this.NPCEnter = spawn;
				this.SelectedNPC = _SelectedNPC;
			}catch(Exception a){
				this.isOn = false;
			}
			
			if(this.isOn){
				opera.AnunciarTodos("Vote Instance", ClueToShow);
				ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
					@Override
					public void run(){
						zoneClose();
					}
				}, (this.Minutes_Duration * 60) * 1000);						
			}
			
		}
	}
	
	private void createTimer(){
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			@Override
			public void run(){
				zoneStart();
			}
		}, (this.GONNA_START_IN * 60) * 1000);		
	}
	
	@SuppressWarnings("rawtypes")
	private void createMobs(){
		Iterator itr = SpawnLocation.entrySet().iterator();
		int idMobMenuGeneral = opera.getUnixTimeNow();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idSpawn = (int)Entrada.getKey();
	    	idMobMenuGeneral = idMobMenuGeneral + idSpawn;
			int templateId = this.SpawnMob.get(idSpawn);
			int mobCount = this.SpawnMobCount.get(idSpawn);
			String getRange = this.SpawnRange.get(idSpawn);
			int RX = Integer.valueOf(getRange.split(",")[0]);
			int RY = Integer.valueOf(getRange.split(",")[1]);
			L2NpcTemplate template = NpcTable.getInstance().getTemplate(templateId);
			MobGroup group = new MobGroup(idMobMenuGeneral, template, mobCount);
			MobGroupTable.getInstance().addGroup(idMobMenuGeneral, group);
			group.spawnGroup(SpawnLocation.get(idSpawn).getX(), SpawnLocation.get(idSpawn).getY(), SpawnLocation.get(idSpawn).getZ(), this.idInstance, RX, RY);
			group.setAttackRandom();
			this.MOBMENU_IDS.add(idMobMenuGeneral);
		}
	}
	
	private void createInstance(){
		int idInstanceTemp = InstanceManager.getInstance().createDynamicInstance(null);
		world = new VoteWorld();
		world.setInstanceId(idInstanceTemp);
		world.setStatus(0);
		InstanceManager.getInstance().addWorld(world);
		this.idInstance = idInstanceTemp;
		Random aleatorio = new Random();
		this.GONNA_START_IN = aleatorio.nextInt(this.minutesToStart); 
		InstanceManager.getInstance().getInstance(world.getInstanceId()).setDuration(( ( this.GONNA_START_IN + this.Minutes_Duration ) * 60) * 1000);
		InstanceManager.getInstance().getInstance(idInstanceTemp).setShowTimer(true);
		InstanceManager.getInstance().getInstance(idInstanceTemp).setTimerIncrease(false);
		InstanceManager.getInstance().getInstance(idInstanceTemp).setPvPInstance(this.pvpinstance);
		_log.warning("VoteInstance - New Instance("+ this.idInstance +"), Begin on ->" + this.GONNA_START_IN + " Minutes.");
	}
	
	public boolean canParty(){
		return this.canParty;
	}
	
	public int getDropChance(){
		return this.chanceDropPorcent;
	}
	public float getRateMulti(){
		return this.rateDropMulti;
	}
	
	public _VOTE_ZONES(String name, boolean party, boolean rewardbyemail, boolean dualbox, int _minutesToStart, int _minutesDuration, boolean _killmobReward, int chance_drop, float _rate_multi, boolean ispvpinstance){
		this.nameZone = name;
		this.canParty = party;
		this.rewardByEmail = rewardbyemail;
		this.dualboxcheck = dualbox;
		this.minutesToStart = _minutesToStart;
		this.Minutes_Duration = _minutesDuration;
		this.killMobReward = _killmobReward;
		
		this.chanceDropPorcent = chance_drop;
		this.rateDropMulti = _rate_multi;
		this.pvpinstance = ispvpinstance;
	}

	public void setCost(int idItem, int mount){
		String Cost = String.valueOf(idItem) + "," + String.valueOf(mount);
		this.COST_TO_ENTER.add(Cost);
	}
	
	public void setSpawnLocation(Location loc, int IdMob, int Count, String Range){
		if(this.SpawnLocation==null){
			this.SpawnLocation.put(1, loc);
			this.SpawnMob.put(1, IdMob);
			this.SpawnMobCount.put(1, Count);
			this.SpawnRange.put(1, Range);
		}else if(this.SpawnLocation.size()==0){
			this.SpawnLocation.put(1, loc);
			this.SpawnMob.put(1, IdMob);
			this.SpawnMobCount.put(1, Count);
			this.SpawnRange.put(1, Range);
		}else{
			int Cargo = this.SpawnLocation.size() + 1;
			this.SpawnLocation.put(Cargo, loc);
			this.SpawnMob.put(Cargo, IdMob);
			this.SpawnMobCount.put(Cargo, Count);
			this.SpawnRange.put(Cargo, Range);
		}
	}
	
	public void setRespawnNpcLocation(int idNpc, String clue, Location loc, boolean enterByQuestionMark, boolean teleCloseNpc){
		if(this.SPAWN_NPC==null){
			this.SPAWN_NPC.put(1, new _VOTE_NPC(idNpc, clue, loc, enterByQuestionMark, teleCloseNpc));
		}else if(this.SPAWN_NPC.size()==0){
			this.SPAWN_NPC.put(1, new _VOTE_NPC(idNpc, clue, loc, enterByQuestionMark, teleCloseNpc));
		}else{
			int Cargo = this.SPAWN_NPC.size() + 1;
			this.SPAWN_NPC.put(Cargo, new _VOTE_NPC(idNpc, clue, loc, enterByQuestionMark, teleCloseNpc));
		}
		NPC_ENTER.add(idNpc);
	}

	public void setRespawnLocation(Location loc){
		if(this.RespawnLocation==null){
			this.RespawnLocation.put(1, loc);
		}else if(this.RespawnLocation.size()==0){
			this.RespawnLocation.put(1, loc);
		}else{
			int Cargo = this.RespawnLocation.size() + 1;
			this.RespawnLocation.put(Cargo, loc);
		}
	}
	
	public void setProhibitedClasses(String _prohibitedClases){
		if(_prohibitedClases!=null){
			if(_prohibitedClases.length()>0){
				if(_prohibitedClases.indexOf(",")>=0){
					for(String Classes : _prohibitedClases.split(",")){
						try{
							this.PROHIBITED_CLASSES.add(Integer.valueOf(Classes));
						}catch(Exception a){
							_log.warning("Error adding New Class id for prohibited Classes Zone");
						}
					}
				}else{
					try{
						this.PROHIBITED_CLASSES.add(Integer.valueOf(_prohibitedClases));
					}catch(Exception a){
						_log.warning("Error adding New Class id for prohibited Classes Zone");
					}					
				}
			}
		}
	}	
}

class _VOTE_NPC {
	@SuppressWarnings("unused")
	private final Logger _log = Logger.getLogger(VoteInstance.class.getName());
	private int ID_NPC;
	private boolean ENTER_BY_QUESTION_MARK;
	private boolean TELEPORT_CLOSE_NPC;
	private String CLUE;
	private Location NPC_LOCATION;
	public _VOTE_NPC(int idnpc, String clue, Location spawn, boolean EnterByQuestionMark, boolean teleportCloseToNpc){
		this.ID_NPC = idnpc;
		this.CLUE = clue;
		this.NPC_LOCATION = spawn;
		this.ENTER_BY_QUESTION_MARK = EnterByQuestionMark;
		this.TELEPORT_CLOSE_NPC = teleportCloseToNpc;
	}
	
	public boolean teleportCloseNPC(){
		return this.TELEPORT_CLOSE_NPC;
	}
	
	public int getNPCID(){
		return this.ID_NPC;
	}
	
	public String getClue(){
		String Clue_temp = this.CLUE;
		if(ENTER_BY_QUESTION_MARK){
			Clue_temp += " To enter into the vote instance press here [VOTEZONE_ENTER]";
		}
		if(TELEPORT_CLOSE_NPC){
			Clue_temp += " To Teleport close to the Vote Instance NPC press here [VOTEZONE_TELE_NPC]";
		}
		return Clue_temp;
	}

	public Location getLocation(){
		return this.NPC_LOCATION;
	}
	
}