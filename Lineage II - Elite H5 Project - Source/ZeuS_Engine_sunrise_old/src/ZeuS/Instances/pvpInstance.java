package ZeuS.Instances;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.effects.L2Effect;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.serverpackets.Die;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.util.Rnd;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import gr.sr.interf.SunriseEvents;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Config.general;
import ZeuS.Instances.pvpInstance._ZONES_TYPE;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.server.comun;

public class pvpInstance {
	private static boolean isEnabled;
	public static InstanceWorld world;
	private static Map<_ZONES_TYPE, Integer>InstanceId = new HashMap<_ZONES_TYPE, Integer>();
	private static Map<_ZONES_TYPE, Integer> LastInstanceId = new HashMap<_ZONES_TYPE, Integer>();
	private static Map<_ZONES_TYPE, Integer> SELECTED_ZONE_ID = new HashMap<_ZONES_TYPE, Integer>();
	private static Map<Integer, Integer> INSTANCE_TIME = new HashMap<Integer, Integer>();
	private static Map<_ZONES_TYPE, Vector<Integer>> ZONES_BEFORE = new HashMap<_ZONES_TYPE, Vector<Integer>>();
	private final static Logger _log = Logger.getLogger(pvpInstance.class.getName());
	private static Map<_ZONES_TYPE, HashMap<Integer, _PVP_ZONES>> ZONES_SPAWN = new HashMap<_ZONES_TYPE, HashMap<Integer, _PVP_ZONES>>();
	
	private static Map<Integer, _ZONES_TYPE> PLAYER_WANT_TO_ENTER = new HashMap<Integer, _ZONES_TYPE>();	
	
	public static final boolean _isEnabled() {
		return isEnabled;
	}
	
	public static void setPlayerToEnter(L2PcInstance player, String _type){
		_ZONES_TYPE temp = null;
		
		for(_ZONES_TYPE _names : _ZONES_TYPE.values()){
			if(_names.name().equals(_type)){
				temp = _names;
				PLAYER_WANT_TO_ENTER.put(player.getObjectId(), temp);
				break;
			}
		}
		
		if(temp == null){
			central.msgbox("PvP Instance enter problems", player);
		}
	}
	
	public static void playerEnterFromAsk(L2PcInstance player){
		if(PLAYER_WANT_TO_ENTER != null){
			if(PLAYER_WANT_TO_ENTER.size() != 0){
				if(PLAYER_WANT_TO_ENTER.containsKey(player.getObjectId())){
					enterZone(player, PLAYER_WANT_TO_ENTER.get(player.getObjectId()));
				}
			}
		}
	}	
	
	public static int getInstanceID(_ZONES_TYPE type){
		return InstanceId.get(type);
	}
	public static int getLastInstanceID(_ZONES_TYPE type){
		return LastInstanceId.get(type);
	}
	
	private static void resetAll(){
		for(_ZONES_TYPE _ztype : _ZONES_TYPE.values()){
			InstanceId.put(_ztype , -1);
			LastInstanceId.put(_ztype, -1);
			SELECTED_ZONE_ID.put(_ztype, -1);
			ZONES_BEFORE.put(_ztype, new Vector<Integer>());
			ZONES_BEFORE.get(_ztype).clear();
		}
	}	
	
	public enum _ZONES_TYPE{
		INDIVIDUAL,
		FREE_FOR_ALL,
		PARTY,
		CLAN,
		CLASSES,
		DROP_ZONE
	};
	
	private static String getButton(){
		String Retorno = "";
		if(ZONES_SPAWN==null){
			return "";
		}else if(ZONES_SPAWN.size()==0){
			return "";
		}
		String btnFormat = "<button action=\"bypass -h ZeuS pvpinstanceask %SELECT% \" value=\"%NAME%\" width=200 height=31 back=\"L2UI_CT1.OlympiadWnd_DF_Apply_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_Apply\">";
		if(ZONES_SPAWN.get(_ZONES_TYPE.FREE_FOR_ALL)!=null && isEnabled){
			Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>"+ btnFormat.replace("%NAME%", "Free For All") .replace("%SELECT%", _ZONES_TYPE.FREE_FOR_ALL.name()) +"</td></tr></table><br1>";
		}
		if(ZONES_SPAWN.get(_ZONES_TYPE.INDIVIDUAL)!=null && isEnabled){
			Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>"+ btnFormat.replace("%NAME%", "Individual Zone") .replace("%SELECT%", _ZONES_TYPE.INDIVIDUAL.name() ) +"</td></tr></table><br1>";
		}
		if(ZONES_SPAWN.get(_ZONES_TYPE.CLAN)!=null && isEnabled){
			Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>"+ btnFormat.replace("%NAME%", "Clan Zone") .replace("%SELECT%", _ZONES_TYPE.CLAN.name() ) +"</td></tr></table><br1>";
		}
		if(ZONES_SPAWN.get(_ZONES_TYPE.PARTY)!=null && isEnabled){
			Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>"+ btnFormat.replace("%NAME%", "Party Zone") .replace("%SELECT%", _ZONES_TYPE.PARTY.name() ) +"</td></tr></table><br1>";
		}
		if(ZONES_SPAWN.get(_ZONES_TYPE.DROP_ZONE)!=null && isEnabled){
			Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>"+ btnFormat.replace("%NAME%", "Drop Zone") .replace("%SELECT%", _ZONES_TYPE.DROP_ZONE.name() ) +"</td></tr></table><br1>";
		}		
		if(ZONES_SPAWN.get(_ZONES_TYPE.CLASSES)!=null && isEnabled){
			if(SELECTED_ZONE_ID.get(_ZONES_TYPE.CLASSES) <= 0){
				Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>CLASS ALLOWED<br1> <font color=LEVEL>WAITING</font></td></tr></table><br1>";
			}else{
				int idZoneSelected = SELECTED_ZONE_ID.get(_ZONES_TYPE.CLASSES);
				String ClassAllowed = ZONES_SPAWN.get(_ZONES_TYPE.CLASSES).get(idZoneSelected).getClassNameAllow();
				Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>CLASS ALLOWED<br1> <font color=LEVEL>"+ ClassAllowed +"</font></td></tr></table><br1>";				
			}
			Retorno += "<table width=280><tr><td fixwidth=280 align=CENTER>"+ btnFormat.replace("%NAME%", "Classes Zone") .replace("%SELECT%", _ZONES_TYPE.CLASSES.name() ) +"</td></tr></table><br1>";
		}		
		
		return Retorno;
	}
	
	public static void getHtmlWindow(L2PcInstance player){
		final NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/pvpinstance.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%BTNS%", getButton());
		central.sendHtml(player, html.getHtml(), false);
	}
	
	private static class PvPWorld extends InstanceWorld{
		public PvPWorld(){
		}
	}

	public static _ZONES_TYPE getZoneType(int idInstance){
		if(InstanceId != null){
			for(_ZONES_TYPE _type : _ZONES_TYPE.values()){
				if(InstanceId.containsKey(_type)){
					if(InstanceId.get(_type) == idInstance){
						return _type;
					}
				}
			}
		}
		return null;
	}	
	
	public static boolean setKill(L2PcInstance killer, L2PcInstance death){
		if(killer.getInstanceId() != death.getInstanceId()){
			return false;
		}
		int idInstance = killer.getInstanceId();
		_ZONES_TYPE Type = getZoneType(idInstance);
		if(Type==null){
			return false;
		}
		ZONES_SPAWN.get(Type).get(SELECTED_ZONE_ID.get(Type)).addPvPKill(killer, death);
		return true;
	}	
	
	public static boolean canPartyClan(L2PcInstance player){
		int InstanceID = player.getInstanceId();
		_ZONES_TYPE tempZone = getZoneType(InstanceID);
		if(ZONES_SPAWN.get(tempZone)!=null){
			int idZoneSelected = SELECTED_ZONE_ID.get(tempZone);
			if(idZoneSelected>0){
				if(ZONES_SPAWN.get(tempZone).get(idZoneSelected)!=null){
					return ZONES_SPAWN.get(tempZone).get(idZoneSelected).canParty();
				}
			}
		}
		return true;		
	}	
	
	public static boolean canParty(L2PcInstance player){
		int InstanceID = player.getInstanceId();
		if(InstanceID==0){
			return true;
		}
		_ZONES_TYPE tempZone = getZoneType(InstanceID);
		if(ZONES_SPAWN.get(tempZone)!=null){
			int idZoneSelected = SELECTED_ZONE_ID.get(tempZone);
			if(idZoneSelected>0){
				if(ZONES_SPAWN.get(tempZone).get(idZoneSelected)!=null){
					return ZONES_SPAWN.get(tempZone).get(idZoneSelected).canParty();
				}
			}
		}
		return true;		
	}
	
	public static boolean isInsideZone(L2PcInstance player){
		int InstanceID = player.getInstanceId();
		if(InstanceID==0){
			return false;
		}
		if(ZONES_SPAWN==null){
			return false;
		}
		if(ZONES_SPAWN.size()==0){
			return false;
		}
		boolean isInsideAnyZone = false;
		for(_ZONES_TYPE TypeZ : _ZONES_TYPE.values()){
			if(ZONES_SPAWN.get(TypeZ) != null && !isInsideAnyZone){
				int idZoneSelected = SELECTED_ZONE_ID.get(TypeZ);
				if(idZoneSelected>0){
					if(ZONES_SPAWN.get(TypeZ).get(idZoneSelected)!=null){
						isInsideAnyZone = ZONES_SPAWN.get(TypeZ).get(idZoneSelected).isPlayerInside(player);
					}
				}
			}
		}
		return isInsideAnyZone;
	}	
	
	public static void playerLeave(L2PcInstance player){
		try{
			int idInstance = player.getInstanceId();
	    	try{
		    	if(player.isDead()){
		    		player.doRevive();
		    	}
	    	}catch(Exception a){
	    		
	    	}
	    	if(idInstance!=0){
	    		_ZONES_TYPE TypoCheck = getZoneType(idInstance);
	    		ZONES_SPAWN.get(TypoCheck).get(SELECTED_ZONE_ID.get(TypoCheck)).removeFromInstance(player);
	    		if(player.isDead()){
	    			player.doRevive();
	    		}
	    	}
		}catch(Exception a){
			
		}
		player.startAntifeedProtection(false, false, false, false);
	}	
	
	public static void finishZone(int idInstance){
		int reuseTime = 40;
		_ZONES_TYPE TypoCheck = getZoneType(idInstance);
		ZONES_SPAWN.get(TypoCheck).get(SELECTED_ZONE_ID.get(TypoCheck)).EndZone();
		LastInstanceId.put(TypoCheck, InstanceId.get(TypoCheck));
		InstanceId.put(TypoCheck, -1);
		SELECTED_ZONE_ID.put(TypoCheck, -1);
		INSTANCE_TIME.remove(idInstance);
		try{
			reuseTime = ZONES_SPAWN.get(TypoCheck).get(SELECTED_ZONE_ID.get(TypoCheck)).reuseTime;	
		}catch(Exception a){
			
		}
		ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK(TypoCheck ) , reuseTime * 1000);
	}	
	
	public static boolean isInstancePVP(L2PcInstance player, int idInstance, boolean checkLastInstance){
		if(checkLastInstance){
			for(_ZONES_TYPE _type : _ZONES_TYPE.values()){
				if(LastInstanceId != null){
					try{
						if(LastInstanceId.get(_type) != null){
							if(LastInstanceId.get(_type) == idInstance){
								return true;
							}
						}
					}catch(Exception a){
						
					}
				}
 			}
		}else{
			if(InstanceId != null){
				for(_ZONES_TYPE _type : _ZONES_TYPE.values()){
					if(InstanceId.containsKey(_type)){
						if(InstanceId.get(_type) == idInstance){
							return true;
						}
					}
				}
			}
		}
		return false;		
	}
	
	public static boolean isInSoloClan(int idInstance, boolean checkLastInstance){
		if(checkLastInstance){
			if(LastInstanceId.get(_ZONES_TYPE.CLAN) == idInstance){
				return true;
			}
		}else{
			if(InstanceId.get(_ZONES_TYPE.CLAN) == idInstance){
				return true;
			}
		}		
		return false;
	}	
		
	public static boolean isInSoloParty(int idInstance, boolean checkLastInstance){
		if(checkLastInstance){
			if(LastInstanceId.get(_ZONES_TYPE.PARTY) == idInstance){
				return true;
			}
		}else{
			if(InstanceId.get(_ZONES_TYPE.PARTY) == idInstance){
				return true;
			}
		}		
		return false;
	}
	
	public static void XMLLoad(){
		try{
			if(ZONES_SPAWN.size()>0){
				return;
			}
		}catch(Exception a){
			
		}
		
		resetAll();		
		
		File dir = new File("./config/zeus/zeus_pvp_instances.xml");
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
					if (!"zone".equalsIgnoreCase(d.getNodeName())){
						continue;
					}
					
					_ZONES_TYPE TypeZone = null;
					String TYPE_ZONE = String.valueOf(d.getAttributes().getNamedItem("type").getNodeValue());
					if(TYPE_ZONE.equalsIgnoreCase("INDIVIDUAL")){
						TypeZone = _ZONES_TYPE.INDIVIDUAL;
					}else if(TYPE_ZONE.equalsIgnoreCase("PARTY")){
						TypeZone = _ZONES_TYPE.PARTY;
					}else if(TYPE_ZONE.equalsIgnoreCase("CLAN")){
						TypeZone = _ZONES_TYPE.CLAN;
					}else if(TYPE_ZONE.equalsIgnoreCase("FREE FOR ALL") || TYPE_ZONE.equalsIgnoreCase("FREE_FOR_ALL")){
						TypeZone = _ZONES_TYPE.FREE_FOR_ALL;
					}else if(TYPE_ZONE.equalsIgnoreCase("CLASSES")){
						TypeZone = _ZONES_TYPE.CLASSES;
					}else if(TYPE_ZONE.equalsIgnoreCase("DROP_ZONE") || TYPE_ZONE.equalsIgnoreCase("DROPZONE")){
						TypeZone = _ZONES_TYPE.DROP_ZONE;
					}

					int ID_ZONE = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
					String NAME_ZONE = String.valueOf(d.getAttributes().getNamedItem("name").getNodeValue());
					boolean ZONE_ENABLE = true;
					try{
						ZONE_ENABLE = Boolean.valueOf(d.getAttributes().getNamedItem("enable").getNodeValue());
					}catch(Exception a){
						
					}
					if(!ZONE_ENABLE){
						continue;
					}
					boolean TRANSFORM_ZONE = Boolean.valueOf(d.getAttributes().getNamedItem("transform").getNodeValue());
					boolean BLOCKCHAT_ZONE = Boolean.valueOf(d.getAttributes().getNamedItem("blockchat").getNodeValue());
					boolean PARTYZONE_ZONE = Boolean.valueOf(d.getAttributes().getNamedItem("partys").getNodeValue());
					boolean PARTY_CLAN_ZONE = Boolean.valueOf(d.getAttributes().getNamedItem("clan").getNodeValue());
					boolean PVP_COUNT = Boolean.valueOf(d.getAttributes().getNamedItem("pvpCount").getNodeValue());
					boolean CAN_SHOW_DRESSME = Boolean.valueOf(d.getAttributes().getNamedItem("canSeeDressme").getNodeValue());
					int MINUTES_ZONE =  Integer.valueOf(d.getAttributes().getNamedItem("minutes").getNodeValue());
					int MIN_LEVEL_ZONE = Integer.valueOf(d.getAttributes().getNamedItem("minLevel").getNodeValue());
					int REVIVE_TIME = Integer.valueOf(d.getAttributes().getNamedItem("reviveTime").getNodeValue());
					int REUSE_INSTANCE = Integer.valueOf(d.getAttributes().getNamedItem("reuse").getNodeValue());
					boolean REWARDBYEMAIL = Boolean.valueOf(d.getAttributes().getNamedItem("rewardByEmailAtFinish").getNodeValue());
					boolean DUALBOXCHECK = Boolean.valueOf(d.getAttributes().getNamedItem("dualboxcheck").getNodeValue());
					boolean CAN_DROP = false;
					if(TypeZone == _ZONES_TYPE.DROP_ZONE){
						CAN_DROP = true;
					}
					_PVP_ZONES TemporalZone = new _PVP_ZONES(NAME_ZONE,MINUTES_ZONE,TRANSFORM_ZONE,BLOCKCHAT_ZONE,PARTYZONE_ZONE,PARTY_CLAN_ZONE, MIN_LEVEL_ZONE, REVIVE_TIME, PVP_COUNT, REUSE_INSTANCE, TypeZone,REWARDBYEMAIL, DUALBOXCHECK, CAN_DROP, CAN_SHOW_DRESSME);
					if(ZONES_SPAWN==null){
						ZONES_SPAWN.put(TypeZone, new HashMap<Integer, _PVP_ZONES>());
					}else if(ZONES_SPAWN.size()==0){
						ZONES_SPAWN.put(TypeZone, new HashMap<Integer, _PVP_ZONES>());
					}else if(!ZONES_SPAWN.containsKey(TypeZone)){
						ZONES_SPAWN.put(TypeZone, new HashMap<Integer, _PVP_ZONES>());
					}
					ZONES_SPAWN.get(TypeZone).put(ID_ZONE, TemporalZone);
					
					for (Node b = d.getFirstChild(); b != null; b = b.getNextSibling())
					{
						if (b.getNodeName().equalsIgnoreCase("set")){
							try
							{
								String tSpawn = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
								Location TEMP_LOCA = new Location(Integer.valueOf(tSpawn.split(",")[0]), Integer.valueOf(tSpawn.split(",")[1]), Integer.valueOf(tSpawn.split(",")[2]));
								ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setSpawnLocation(TEMP_LOCA);
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new Zone Spawn for PVP(pvp.java)" + aaa.getMessage());
								ZONES_SPAWN.get(TypeZone).remove(ID_ZONE);
							}
						}else if (b.getNodeName().equalsIgnoreCase("buffs")){
							try
							{
								String BUFF_IDS = String.valueOf(b.getAttributes().getNamedItem("id").getNodeValue().toLowerCase());
								for(String _buff : BUFF_IDS.split(",")){
									ZONES_SPAWN.get(TypeZone).get(ID_ZONE).buffToGive.add(Integer.valueOf(_buff));
								}
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new Buff for PVP(pvp.java)" + aaa.getMessage());
							}
						}else if (b.getNodeName().equalsIgnoreCase("reward_for_death")){
							try
							{
								String REWARDS = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
								ZONES_SPAWN.get(TypeZone).get(ID_ZONE).RewardForDeath = REWARDS;
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new Buff for PVP(pvp.java)" + aaa.getMessage());
							}							
						}else if (b.getNodeName().equalsIgnoreCase("reward_for_kill")){
							try
							{
								String REWARDS = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
								ZONES_SPAWN.get(TypeZone).get(ID_ZONE).RewardForKill = REWARDS;
							}
							catch (Exception aaa)
							{
								_log.warning("Error added new Buff for PVP(pvp.java)" + aaa.getMessage());
							}
						}else if (b.getNodeName().equalsIgnoreCase("prohibited_classes_id")){
							String _prohibitedClases = "";
							try{
								_prohibitedClases = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
							}catch(Exception a){
								_log.warning("Error adding prohibited clases to Zone");
							}
							ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setProhibitedClasses(_prohibitedClases);
						}else if (b.getNodeName().equalsIgnoreCase("allowed_classes_id")){
							String _allowedClases = "";
							try{
								_allowedClases = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
							}catch(Exception a){
								_log.warning("Error adding prohibited clases to Zone");
							}
							ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setAllowedClasses(_allowedClases);
						}else if (b.getNodeName().equalsIgnoreCase("prohibited_item_id")){
							String _prohibitedItems = "";
							try{
								_prohibitedItems = String.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
							}catch(Exception a){
								_log.warning("Error adding prohibited items' to Zone");
							}
							ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setProhibitedItems(_prohibitedItems);
						}else if(b.getNodeName().equalsIgnoreCase("clan_give_reputation_point")){
							boolean giveReputationPoint = false;
							int reputation = 0;
							try{
								giveReputationPoint = Boolean.valueOf(b.getAttributes().getNamedItem("enabled").getNodeValue().toLowerCase());
							}catch(Exception a){
								
							}
							try{
								reputation = Integer.valueOf(b.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
							}catch(Exception a){
								
							}
							ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setClanReputation(giveReputationPoint, reputation);
						}else if ("npc_raid".equalsIgnoreCase(b.getNodeName())){
							boolean rbEnabled = false;
							rbEnabled = Boolean.valueOf(b.getAttributes().getNamedItem("enabled").getNodeValue());
							ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setRaidBoss(rbEnabled);
							for (Node npcRaid = b.getFirstChild(); npcRaid != null; npcRaid = npcRaid.getNextSibling())
							{
								if (npcRaid.getNodeName().equalsIgnoreCase("set")){
									String property = String.valueOf(npcRaid.getAttributes().getNamedItem("name").getNodeValue().toLowerCase());
									String values = "";
									try{
										values = String.valueOf(npcRaid.getAttributes().getNamedItem("value").getNodeValue().toLowerCase());
									}catch(Exception a){
										
									}
									if(property.equalsIgnoreCase("id_raid")){
										if(values.length()>0){
											ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setRbId(values);
										}
									}else if(property.equalsIgnoreCase("spawn")){
										ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setRbSpawn(values);
									}else if(property.equalsIgnoreCase("second_delay_to_show_after_instance_start")){
										if(values.length()==0){
											values = "0";
										}
										ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setRbDelay(Integer.valueOf(values));
									}
								}
							}
						}else if("player_drop".equalsIgnoreCase(b.getNodeName()) && TypeZone == _ZONES_TYPE.DROP_ZONE){
							for (Node dropData = b.getFirstChild(); dropData != null; dropData = dropData.getNextSibling())
							{
								if (dropData.getNodeName().equalsIgnoreCase("drop")){
									String values = "";
									int ItemId = 0;
									long DropPorcent = 0;
									int Minium_Amount_Allowed = 0;
									boolean isRequerided = true;
									try{
										values = String.valueOf(dropData.getAttributes().getNamedItem("item_id").getNodeValue().toLowerCase());
									}catch(Exception a){
										_log.warning("Drop Zone cant load item_id from the properties");
										continue;
									}
									ItemId = Integer.valueOf(values);
									try{
										values = String.valueOf(dropData.getAttributes().getNamedItem("porcent").getNodeValue().toLowerCase());
									}catch(Exception a){
										_log.warning("Drop Zone cant load porcent from the properties");
										continue;
									}
									DropPorcent = Long.parseLong(values);
									try{
										values = String.valueOf(dropData.getAttributes().getNamedItem("minimum_amount_allowed_to_be_inside").getNodeValue().toLowerCase());
									}catch(Exception a){
										_log.warning("Drop Zone cant load minimum_amount_allowed_to_be_inside from the properties");
										continue;
									}
									Minium_Amount_Allowed = Integer.valueOf(values);
									try{
										values = String.valueOf(dropData.getAttributes().getNamedItem("required").getNodeValue().toLowerCase());
									}catch(Exception a){
										_log.warning("Drop Zone cant load required from the properties");
										continue;
									}									
									isRequerided = values.equalsIgnoreCase("true") ? true : false;
									ZONES_SPAWN.get(TypeZone).get(ID_ZONE).setDropItemForDROPZONE(ItemId, DropPorcent, Minium_Amount_Allowed, isRequerided);
								}
							}							
						}
					}
				}
			}
		}
		catch (Exception a)
		{
			_log.warning("	Error loading PVP ZONE ->" + a.getMessage() + "\n");
		}
		_log.warning("	Load " + ZONES_SPAWN.size() + " PvP Instance Zones from The Engine");
		int countIndi =0, countParty=0, countClan=0, countFreeForAll=0, countClasses=0, countDropzone=0;
		try{ countIndi = ZONES_SPAWN.get(_ZONES_TYPE.INDIVIDUAL).size() ; }catch(Exception a){}
		try{ countParty = ZONES_SPAWN.get(_ZONES_TYPE.PARTY).size() ; }catch(Exception a){}
		try{ countClan = ZONES_SPAWN.get(_ZONES_TYPE.CLAN).size() ; }catch(Exception a){}
		try{ countFreeForAll = ZONES_SPAWN.get(_ZONES_TYPE.FREE_FOR_ALL).size() ; }catch(Exception a){}
		try{ countClasses = ZONES_SPAWN.get(_ZONES_TYPE.CLASSES).size() ; }catch(Exception a){}
		try{ countDropzone = ZONES_SPAWN.get(_ZONES_TYPE.DROP_ZONE).size() ; }catch(Exception a){}
		
		_log.warning("         Individual Zones Load-> " + countIndi );
		_log.warning("         Party Zones Load-> " + countParty );
		_log.warning("         Clan Zones Load-> " + countClan );
		_log.warning("         Free for All Zones Load-> " + countFreeForAll );
		_log.warning("         Classes Zones Load-> " + countClasses );
		_log.warning("         Drop Zones Load-> " + countDropzone );
		try{
			if(isEnabled){
				ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK( _ZONES_TYPE.INDIVIDUAL ) , 40 * 1000);
				ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK( _ZONES_TYPE.PARTY ) , 50 * 1000);
				ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK( _ZONES_TYPE.CLAN ) , 60 * 1000);
				ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK( _ZONES_TYPE.FREE_FOR_ALL ) , 70 * 1000);
				ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK( _ZONES_TYPE.CLASSES ) , 80 * 1000);
				ThreadPoolManager.getInstance().scheduleGeneral(new INSTANCE_PVP_CLOCK( _ZONES_TYPE.DROP_ZONE ) , 90 * 1000);
			}else{
				_log.warning("    Instance zone are DISABLED.");
			}
		}catch(Exception a){
			
		}
	}
	
	public static boolean onDieRevive(L2PcInstance character, boolean AskForRevive)
	{
		if(isInsideZone(character)){
			if(AskForRevive){
				_ZONES_TYPE tempZone = getZoneType(character.getInstanceId());
				return ZONES_SPAWN.get(tempZone).get(SELECTED_ZONE_ID.get(tempZone)).onDieReviveAsk(character, tempZone, character.getInstanceId());
			}else{
				_ZONES_TYPE tempZone = getZoneType(character.getInstanceId());
				_PVP_ZONES zoneT = ZONES_SPAWN.get(tempZone).get(SELECTED_ZONE_ID.get(tempZone));
				return zoneT._onDieRevive(character, tempZone, character.getInstanceId(), zoneT);				
			}
		}
		return false;
	}
	
	public static boolean canSeeDressme(L2PcInstance player) {
		if(isInsideZone(player)){
			_ZONES_TYPE tempZone = getZoneType(player.getInstanceId());
			_PVP_ZONES zoneT = ZONES_SPAWN.get(tempZone).get(SELECTED_ZONE_ID.get(tempZone));
			return zoneT.canSeeDressme();				
		}
		return true;		
	}
	
	
	private static void SelectAndBeginZone(_ZONES_TYPE type){
		if(!isEnabled){
			_log.warning("PvP Instance zone are Disabled. Cant initialize");
			return;
		}
		if(ZONES_SPAWN==null){
			_log.warning("PVP Instance zone was cancel. No Zone Data");
			return;
		}else if(ZONES_SPAWN.size()==0){
			_log.warning("PVP Instance zone was cancel. No Zone Data");
			return;
		}else if(ZONES_SPAWN.get(type)==null){
			_log.warning("PVP Instance zone was cancel. No Zone Data for " + type.name());
			return;
		}else if(ZONES_SPAWN.get(type).size()==0){
			_log.warning("PVP Instance zone was cancel. No Zone Data for " + type.name());
			return;
		}
		
		int ZONES_TO_SELECT = ZONES_SPAWN.get(type).size();

		int SELECTED_ZONE = 0;
		
		if(ZONES_TO_SELECT == 1){
			SELECTED_ZONE_ID.put(type, 1);
			SELECTED_ZONE = 1;
		}else{
			if(ZONES_BEFORE.get(type)!=null){
				if(ZONES_BEFORE.get(type).size() == ZONES_SPAWN.get(type).size()){
					ZONES_BEFORE.get(type).clear();
					ZONES_BEFORE.get(type).add(-1);
				}
			}
			Random aleatorio = new Random();
			boolean Selected = false;
			while(!Selected){
				int SelectID = aleatorio.nextInt(ZONES_TO_SELECT+1);
				if(ZONES_BEFORE.get(type).contains(SelectID)){
					continue;
				}
				if(ZONES_SPAWN.get(type).containsKey(SelectID)){
					Selected = true;
					SELECTED_ZONE_ID.put(type, SelectID);
					SELECTED_ZONE = SelectID;
				}
			}			
		}
		
		ZONES_BEFORE.get(type).add(SELECTED_ZONE);
		
		int idInstanceTemp = InstanceManager.getInstance().createDynamicInstance(null);
		InstanceId.put(type, idInstanceTemp);

		world = new PvPWorld();
		world.setInstanceId(InstanceId.get(type));
		world.setStatus(0);
		InstanceManager.getInstance().addWorld(world);
		int Minutes = ZONES_SPAWN.get(type).get(SELECTED_ZONE).Minutes;
		InstanceManager.getInstance().getInstance(world.getInstanceId()).setDuration((Minutes * 60) * 1000);
		InstanceManager.getInstance().getInstance(world.getInstanceId()).setPvPInstance(true);
		INSTANCE_TIME.put(InstanceId.get(type), opera.getUnixTimeNow());
		try{
			ZONES_SPAWN.get(type).get(SELECTED_ZONE).startCount(opera.getUnixTimeNow(), idInstanceTemp);
		}catch(Exception a){
			_log.warning("Error A->" + a.getMessage());
		}
		String Mensaje = "";
		if(type == _ZONES_TYPE.INDIVIDUAL){
			Mensaje = "New PVP Individual Zone. To Enter Click Here [PVPZONE_SINGLE]";
		}else if(type == _ZONES_TYPE.PARTY){
			Mensaje = "New PVP Party Zone. To Enter Click Here [PVPZONE_PARTY]";
		}else if(type == _ZONES_TYPE.CLAN){
			Mensaje = "New PVP Clan Zone. To Enter Click Here [PVPZONE_CLAN]";
		}else if(type == _ZONES_TYPE.FREE_FOR_ALL){
			Mensaje = "New PVP Free For All Zone. To Enter Click Here [PVPZONE_FREEFORALL]";
		}else if(type == _ZONES_TYPE.CLASSES){
			Mensaje = "New PVP Classes Zone. To Enter Click Here [PVPZONE_CLASSES]";
		}else if(type == _ZONES_TYPE.DROP_ZONE){
			Mensaje = "New PVP Drop Zone. To Enter Click Here [PVPZONE_DROPZONE]";
		}
		opera.AnunciarTodos("PVP ZONE", Mensaje);
	}
	
	private static void sendMnsjeToParty(L2PcInstance player, String Mensaje){
		if(player.isInParty()){
			for(L2PcInstance ppl : player.getParty().getMembers()){
				central.msgbox(Mensaje, ppl);
			}
		}
	}
	
	
	
	public static void enterZone(L2PcInstance player, _ZONES_TYPE ZoneType){
		if(player.getInstanceId()>0){
			central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_YOU_NEED_TO_EXIT_TO_ENTER_TO_ANOTHER, player);
			return;
		}
		
		if(RaidBossEvent.isPlayerOnRBEvent(player)){
			central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_YOU_CANT_ENTER_NOW_FOR_EVENT, player);
			return;
		}
		
		if(SunriseEvents.isInEvent(player)){
			central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_YOU_CANT_ENTER_NOW_FOR_EVENT, player);
			return;			
		}
		
		if(!player.isInsideZone(ZoneIdType.PEACE)){
			central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_YOU_NEED_TO_BE_ON_PEACE_ZONE, player);
			return;
		}
		
		if(SELECTED_ZONE_ID.get(ZoneType) <= 0){
			central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_IS_NOT_READY, player);
			return;			
		}
		
		_log.warning("Zone Id To Enter: " + SELECTED_ZONE_ID.get(ZoneType) + " - " + ZoneType.name());
		
		if(ZoneType == _ZONES_TYPE.DROP_ZONE){
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).hasItemDrop()){
				central.msgbox("This Zone have problem.", player);
				return;
			}
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > player.getLevel()){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER.replace("$level",String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel)), player);
				return;
			}
			
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(player)){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER, player);
				return;
			}

			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(player)){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM, player);
				return;
			}
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveItemDropToEnter(player)){
				return;
			}
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).canParty() && player.getParty()!=null){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CANT_ENTER_WITH_PARTY, player);
				return;				
			}
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).canPartyJustClan() && player.getClan()==null){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_CLAN_PARTY, player);
				return;				
			}
			boolean onlyClan = ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).canPartyJustClan();
			
			if(onlyClan && player.getParty()==null){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_CLAN_PARTY, player);
				return;				
			}
			
			if(player.getParty() != null){
				for(L2PcInstance ppl : player.getParty().getMembers()){
					if(ppl != player){
						if(!ppl.isInsideRadius(player.getLocation(), 300, true, true)){
							for(L2PcInstance ppl2 : player.getParty().getMembers()){
								central.msgbox(language.getInstance().getMsg(ppl2).PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER.replace("$player", ppl.getName()), ppl2);							
							}
							return;
						}
					}
				}				
				for(L2PcInstance _ppl : player.getParty().getMembers()){
					if(_ppl == player){
						continue;
					}
					if(onlyClan){
						if(_ppl.getClan()==null){
							central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_CLAN_PARTY, _ppl);
							central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NEED_CLAN.replace("$name", _ppl.getName())  , player);
							return;
						}else if(_ppl.getClan() != player.getClan()){
							central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_CLAN_PARTY, _ppl);
							central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NEED_CLAN.replace("$name", _ppl.getName())  , player);							
						}
						
					}

					if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > _ppl.getLevel()){
						central.msgbox(language.getInstance().getMsg(_ppl).PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER.replace("$level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel)), player);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_NO_HAVE_LEVEL.replace("$player", _ppl.getName()).replace("$min_level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel) ) ,ppla);
						}
						return;
					}
					if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(_ppl)){
						central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER, player);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_NO_HAD_A_CLASS.replace("$player", _ppl.getName() ), ppla);
						}
						return;
					}
					if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(_ppl)){
						central.msgbox( language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM , player);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_HAVE_PROHIBITED_ITEM.replace("$player", _ppl.getName()) , ppla);
						}
						return;
					}
					if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveItemDropToEnter(_ppl)){
						central.msgbox( language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NO_HAVE_ITEM_REQUESTED.replace("$name", _ppl.getName()) , player);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NO_HAVE_ITEM_REQUESTED.replace("$player", _ppl.getName()) , ppla);
						}
						return;
					}					
					
					
				}
			}
			ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).addPlayer(player, InstanceId.get(ZoneType), false);
		}else if(ZoneType == _ZONES_TYPE.INDIVIDUAL){
			if(player.isInParty()){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_PARTY, player);
				return;
			}
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > player.getLevel()){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER.replace("$level",String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel)), player);
				return;
			}
			
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(player)){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER, player);
				return;
			}

			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(player)){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM, player);
				return;
			}			
			ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).addPlayer(player, InstanceId.get(ZoneType), false);			
		}else if(ZoneType == _ZONES_TYPE.CLASSES){
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > player.getLevel()){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER.replace("$level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel) ) , player);
				return;
			}
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(player)){
				String ClasePermitida = ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).getClassNameAllow();
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER_CLASS_ALLOW.replace("$class", ClasePermitida) , player);
				return;
			}
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).canParty() && player.getParty()!=null){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CANT_ENTER_WITH_PARTY, player);
				return;
			}else{
				if(player.isInParty()){
					for(L2PcInstance ppl : player.getParty().getMembers()){
						if(ppl != player){
							if(!ppl.isInsideRadius(player.getLocation(), 300, true, true)){
								for(L2PcInstance pplInParty : player.getParty().getMembers()){
									central.msgbox( language.getInstance().getMsg(pplInParty).PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER.replace("$player", ppl.getName() ) , pplInParty);
								}
								return;
							}
						}
					}
				}
			}
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(player)){
				central.msgbox("You Cant enter. You have a Prohibited Item", player);
				return;
			}
			ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).addPlayer(player, InstanceId.get(ZoneType), false);
		}else if(ZoneType == _ZONES_TYPE.FREE_FOR_ALL){
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > player.getLevel()){
				central.msgbox("You need a minimun level of " + String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel) + " to enter", player);
				return;
			}
			
			if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(player)){
				central.msgbox("You class cant enter to this zone", player);
				return;
			}
			
			if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(player)){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM, player);
				return;
			}						
			ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).addPlayer(player, InstanceId.get(ZoneType), false);			
		}else if(ZoneType == _ZONES_TYPE.PARTY){
			if(!player.isInParty()){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_PARTY, player);
				return;				
			}else if(!player.getParty().isLeader(player)){
				sendMnsjeToParty(player, language.getInstance().getMsg(player).ONLY_PARTY_LEADER_CAN_USE_THIS);
				return;
			}
			
			for(L2PcInstance ppl : player.getParty().getMembers()){
				if(ppl != player){
					if(!ppl.isInsideRadius(player.getLocation(), 300, true, true)){
						for(L2PcInstance ppl2 : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppl2).PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER.replace("$player", ppl.getName()), ppl2);							
						}
						return;
					}
				}
			}
			
			for( L2PcInstance ppl: player.getParty().getMembers() ){
				if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > ppl.getLevel()){
					central.msgbox(language.getInstance().getMsg(ppl).PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER.replace("$level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel)), player);
					for(L2PcInstance ppla : player.getParty().getMembers()){
						central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_NO_HAVE_LEVEL.replace("$player", ppl.getName()).replace("$min_level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel) ) ,ppla);
					}
					return;
				}
				if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(ppl)){
					central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER, player);
					for(L2PcInstance ppla : player.getParty().getMembers()){
						central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_NO_HAD_A_CLASS.replace("$player", ppl.getName() ), ppla);
					}
					return;
				}
				if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(ppl)){
					central.msgbox( language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM , player);
					for(L2PcInstance ppla : player.getParty().getMembers()){
						central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_HAVE_PROHIBITED_ITEM.replace("$player",ppl.getName()) , ppla);
					}
					return;
				}							
			}
			ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).addPlayer(player, InstanceId.get(ZoneType), true);
		}else if(ZoneType == _ZONES_TYPE.CLAN){
			if(!player.isInParty()){
				central.msgbox(language.getInstance().getMsg(player).PVP_INSTANCE_ZONE_JUST_PARTY , player);
				return;
			}else{
				if(!player.getParty().isLeader(player)){
					central.msgbox(language.getInstance().getMsg(player).ONLY_PARTY_LEADER_CAN_USE_THIS , player);
					return;
				}
				
				for(L2PcInstance ppl : player.getParty().getMembers()){
					if(ppl != player){
						if(!ppl.isInsideRadius(player.getLocation(), 300, true, true)){
							for(L2PcInstance ppla : player.getParty().getMembers()){
								central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER.replace("$player", ppl.getName()), ppla);
							}
							return;
						}
					}
				}				
				
				for(L2PcInstance ppl : player.getParty().getMembers()){
					if(ppl.getClan()!=null){
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_JUST_CLAN_PARTY, ppla);
						}
						return;						
					}
					if(ppl.getClanId() != player.getClanId()){
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_JUST_CLAN_PARTY, ppla);
						}
						return;						
					}
					if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel > ppl.getLevel()){
						central.msgbox(language.getInstance().getMsg(ppl).PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER.replace("$level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel)), ppl);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_NO_HAVE_LEVEL.replace("$min_level", String.valueOf(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).minLevel))  , ppla);
						}
						return;
					}
					if(!ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).classCanEnter(ppl)){
						central.msgbox( language.getInstance().getMsg(ppl).PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER  , ppl);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_NO_HAD_A_CLASS, ppla);
						}
						sendMnsjeToParty(player, "The player " + ppl.getName() + " has a prohibited class to enter this zone");
						return;
					}
					if(ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).haveProhibitedItems(ppl)){
						central.msgbox(language.getInstance().getMsg(ppl).PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM , ppl);
						for(L2PcInstance ppla : player.getParty().getMembers()){
							central.msgbox(language.getInstance().getMsg(ppla).PVP_INSTANCE_ZONE_PLAYER_HAVE_PROHIBITED_ITEM.replace("$player",ppl.getName())  , ppla);
						}
						return;
					}	
				}
				ZONES_SPAWN.get(ZoneType).get(SELECTED_ZONE_ID.get(ZoneType)).addPlayer(player, InstanceId.get(ZoneType), true);
			}
		}
	}	
	
	protected static class INSTANCE_PVP_CLOCK implements Runnable{
		private _ZONES_TYPE _Type;
		public INSTANCE_PVP_CLOCK(_ZONES_TYPE Type){
			_Type = Type;
		}
		public void run(){
			SelectAndBeginZone(_Type);
		}
	}
}



class _PVP_ZONES{
	private final Logger _log = Logger.getLogger(pvpInstance.class.getName());
	public int Minutes;
	public String nameZone;
	private boolean canTransform;
	private boolean canBlockAllChat;
	private boolean canParty;
	private boolean canPartyJustClan;
	private boolean canDrop;
	private boolean canShowDressme;
	private boolean pvpCount;
	public Vector<Integer> buffToGive = new Vector<Integer>();
	public String RewardForKill;
	public String RewardForDeath;
	public int minLevel;
	public int reviveTime;
	public int reuseTime;
	private _ZONES_TYPE Type;
	private Vector<Integer>PROHIBITED_CLASSES = new Vector<Integer>();
	private Vector<Integer>ALLOWED_CLASSES = new Vector<Integer>();
	private Vector<Integer>PROHIBITED_ITEMS = new Vector<Integer>();
	private Map<String, _CHAR_INFO_ZONE> PLAYER_INSIDE = new HashMap<String, _CHAR_INFO_ZONE>();
	private Vector<String>CHAR_FOR_REWARD = new Vector<String>();
	private Map<Integer, Location> SpawnLocation = new HashMap<Integer, Location>();
	private boolean isOn = false;
	private boolean rewardByEmail;
	private boolean dualboxcheck;
	private Vector<Integer> RAIDBOSS_IDS = new Vector<Integer>();
	private Vector<String> RAIDBOSS_SPAWN = new Vector<String>();
	private int RAIDBOSS_DELAY=0;
	private boolean RAIDBOSS_ENABLED;
	private boolean CLAN_REPUTATION;
	private int CLAN_REPUTATION_VALUE;
	private int idInstance;
	private L2Spawn _bossSpawn;
	private int ID_ALLOWED_CLASS_ENTER;
	protected int INIT_UNIX_TIME;
	
	private Map<Integer, _DROP_ZONE_VALUES> DROPZONE_ITEM = new HashMap<Integer, _DROP_ZONE_VALUES>();
	
	private static Map<Integer, _ZONES_TYPE>TemporalDataZone = new HashMap<Integer, _ZONES_TYPE>();
	private static Map<Integer, Integer>TemporalDataInstance = new HashMap<Integer, Integer>();	
	
	public boolean canParty(){
		return this.canParty;
	}
	public boolean canPartyJustClan(){
		return this.canPartyJustClan;
	}
	
	
	
	public _PVP_ZONES(String _zoneName, int _minutes, boolean _canTransform, boolean _canBlockAllChat, boolean _canParty, boolean _canPartyJustClan, int _minLevel, int _reviveTime, boolean _pvpCount, int _reuseTime, _ZONES_TYPE _type, boolean _rewardByEmail, boolean _dualboxcheck, boolean _canDrop, boolean _canShowDressme){
		this.Minutes = _minutes;
		this.canTransform = _canTransform;
		this.canBlockAllChat = _canBlockAllChat;
		this.canParty = _canParty;
		this.canPartyJustClan = _canPartyJustClan;
		this.nameZone = _zoneName;
		this.minLevel = _minLevel;
		this.reviveTime = _reviveTime;
		this.pvpCount = _pvpCount;
		this.reuseTime = _reuseTime;
		this.Type = _type;
		this.rewardByEmail = _rewardByEmail;
		this.dualboxcheck = _dualboxcheck;
		this.canDrop = _canDrop;
		this.canShowDressme = _canShowDressme;
	}
	public boolean canSeeDressme() {
		return this.canShowDressme;
	}
	public boolean hasItemDrop(){
		if(this.DROPZONE_ITEM==null){
			return false;
		}else if(this.DROPZONE_ITEM.size()==0){
			return false;
		}
		return true;
	}
	public void setDropItemForDROPZONE(int _idItem, long _porcen, int minimum_amount_allowed, boolean Requerided){
		_DROP_ZONE_VALUES tmp = new _DROP_ZONE_VALUES(_idItem, _porcen, minimum_amount_allowed, Requerided);
		DROPZONE_ITEM.put(_idItem, tmp);
	}
	public void setRbId(String idRaid){
		if(idRaid!=null){
			if(idRaid.length()>0){
				for(String idR : idRaid.split(",")){
					if(opera.isNumeric(idR)){
						this.RAIDBOSS_IDS.add(Integer.valueOf(idR));
					}
				}
			}
		}
	}
	public void setRbSpawn(String location){
		this.RAIDBOSS_SPAWN.add(location);
	}
	public void setRbDelay(int Delay){
		this.RAIDBOSS_DELAY = Delay;
	}
	public void setRaidBoss(boolean isEnabled){
		this.RAIDBOSS_ENABLED = isEnabled;
	}
	
	public boolean haveItemDropToEnter(L2PcInstance ppl){
		if(DROPZONE_ITEM == null){
			central.msgbox("This zone Has Missing the Drop Item's to Enter", ppl);
			return false;
		}
		if(DROPZONE_ITEM.size()==0){
			central.msgbox("This zone Has Missing the Drop Item's to Enter", ppl);
			return false;			
		}
		@SuppressWarnings("rawtypes")
		Iterator itr = DROPZONE_ITEM.entrySet().iterator();
		while(itr.hasNext()){
	    	@SuppressWarnings("rawtypes")
			Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idItem = (int)Entrada.getKey();
	    	_DROP_ZONE_VALUES _var = (_DROP_ZONE_VALUES)Entrada.getValue();
	    	if(!opera.haveItem(ppl, idItem, _var.getMinimumAmountAllowed(), _var.isRequerided()) && _var.isRequerided()){
	    		central.msgbox(language.getInstance().getMsg(ppl).YOU_DONT_HAVE_THE_REQUESTED_ITEM, ppl);
	    		return false;
	    	}
		}
		return true;
	}
	
	public boolean haveProhibitedItems(L2PcInstance ppl){
		if(this.PROHIBITED_ITEMS!=null){
			if(this.PROHIBITED_ITEMS.size()>0){
				for(L2ItemInstance Item : ppl.getInventory().getItems()){
					if(this.PROHIBITED_ITEMS.contains(Item.getItem().getId())){
						central.msgbox("Prohibited Item Found " + Item.getName(), ppl);
						return true;
					}
				}
			}
		}
		return false;
	}
	public void setClanReputation(Boolean isEnabled, int value){
		this.CLAN_REPUTATION = isEnabled;
		this.CLAN_REPUTATION_VALUE = value;
	}
	public void setProhibitedItems(String _prohibitedItems){
		if(_prohibitedItems!=null){
			if(_prohibitedItems.length()>0){
				for(String Classes : _prohibitedItems.split(",")){
					try{
						this.PROHIBITED_ITEMS.add(Integer.valueOf(Classes));
					}catch(Exception a){
						
					}
				}
			}
		}		
	}
	
	public void setAllowedClasses(String _allowedClases){
		if(_allowedClases!=null){
			if(_allowedClases.length()>0){
				if(_allowedClases.indexOf(",")>=0){
					for(String Classes : _allowedClases.split(",")){
						try{
							this.ALLOWED_CLASSES.add(Integer.valueOf(Classes));
						}catch(Exception a){
							_log.warning("Error adding New Class id for Allowed Classes Zone");
						}
					}
				}else{
					try{
						this.ALLOWED_CLASSES.add(Integer.valueOf(_allowedClases));
					}catch(Exception a){
						_log.warning("Error adding New Class id for Allowed Classes Zone");
					}					
				}
			}
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
	
	public boolean isPlayerInside(L2PcInstance player){
		try{
			boolean TisInside = this.PLAYER_INSIDE.containsKey(player.getName());
			if(TisInside){
				if(this.PLAYER_INSIDE.get(player.getName()).isInside){
					return true;
				}
			}
		}catch(Exception a){
			_log.warning("isPlayerInside PvpInstance Error: " + a.getMessage());
		}
		return false;
	}
	
	public void setSpawnLocation(Location loc){
		if(SpawnLocation==null){
			SpawnLocation.put(1, loc);
		}else if(SpawnLocation.size()==0){
			SpawnLocation.put(1, loc);
		}else{
			int Cargo = SpawnLocation.size() + 1;
			SpawnLocation.put(Cargo, loc);
		}
	}
	private void moveChar(L2PcInstance player, boolean isParty, int instanceID){
		Random aleatorio = new Random();
		boolean Selected = false;
		
		if(!this.PLAYER_INSIDE.get(player.getName()).isInside){
			return;
		}
		
		while(!Selected){
			int SelectID = aleatorio.nextInt(SpawnLocation.size()+1);
			if(SpawnLocation.containsKey(SelectID)){
				if(!isParty){
					player.setInstanceId(instanceID);
					giveBuff(player);
					if(!player.isGM() && this.canTransform){
						player.startAntifeedProtection(true, true, this.canBlockAllChat, this.canTransform);
					}
					player.teleToLocation(SpawnLocation.get(SelectID));
				}else{
					for(L2PcInstance ppl : player.getParty().getMembers()){
						ppl.setInstanceId(instanceID);
						giveBuff(player);
						if(!player.isGM() && this.canTransform){
							ppl.startAntifeedProtection(true, true, this.canBlockAllChat, this.canTransform);
						}
						ppl.teleToLocation(SpawnLocation.get(SelectID));
					}
				}
				Selected = true;
			}
		}
	}
	private void giveBuff(L2PcInstance player){
		try{
			for (L2Effect e : player.getAllEffects()){
				if ((e == null) || !e.getSkill().isOffensive() || !e.getSkill().canBeDispeled()){
					continue;
				}
				e.exit();
			}
		}catch(Exception a){
			
		}
		try{
			if(player.getSummon()!=null){
				for (L2Effect e : player.getSummon().getAllEffects()){
					if ((e == null) || !e.getSkill().isOffensive() || !e.getSkill().canBeDispeled()){
						continue;
					}
					e.exit();
				}
			}
		}catch(Exception a){
		}		
		if(buffToGive!=null){
			if(buffToGive.size()>0){
				for(int idBuff : buffToGive){
					int lvlBuff = SkillData.getInstance().getMaxLevel(idBuff);
					if (player.isAffectedBySkill(idBuff)){
						player.stopSkillEffects(idBuff);
					}
					SkillData.getInstance().getInfo(idBuff, lvlBuff).getEffects(player, player);
					if(player.getSummon()!=null){
						if (player.getSummon().isAffectedBySkill(idBuff)){
							player.getSummon().stopSkillEffects(idBuff);
						}						
						SkillData.getInstance().getInfo(idBuff, lvlBuff).getEffects(player.getSummon(), player.getSummon());
					}					
				}				
			}
		}
	}
	
	public boolean onDieReviveAsk(L2PcInstance character, _ZONES_TYPE TypeZone, int InstanceID){
		try{
			TemporalDataZone.remove(character.getObjectId());
		}catch(Exception a){
			
		}
		try{
			TemporalDataInstance.remove(character.getObjectId());
		}catch(Exception a){
			
		}
		TemporalDataZone.put(character.getObjectId(), TypeZone);
		TemporalDataInstance.put(character.getObjectId(), InstanceID);
		Dlg.sendDlg(character, "You want revive in this zone again?", IdDialog.PVP_INSTANCE_REVIVE);
		return true;
	}
	
	public boolean _onDieRevive(L2PcInstance character, _ZONES_TYPE TypeZone, int InstanceID, _PVP_ZONES Zone){
		character.sendMessage("You will revive on " + String.valueOf(this.reviveTime) + " Second's");
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			private int Instance = InstanceID;
			public void run(){
				if(character.isDead() && character.getInstanceId()>0 &&  character.getInstanceId()== Instance ){
					if(isOn){
						if(!PLAYER_INSIDE.get(character.getName()).isInside){
							return;
						}
						boolean canReviveInside = true;
						if(TypeZone == _ZONES_TYPE.DROP_ZONE){
							if(!Zone.haveItemDropToEnter(character)){
								canReviveInside = false;
							}
						}
						character.doRevive();
						if(character.getInstanceId()>0 && canReviveInside){
							moveChar(character,false,Instance);
							if(!character.isGM() && Zone.canTransform){
								character.startAntifeedProtection(true, true, Zone.canBlockAllChat, Zone.canTransform);
							}else if(canBlockAllChat){
								character.startAntifeedProtection(true, true, Zone.canBlockAllChat, Zone.canTransform);
							}
							central.healAll(character, false);
							if(character.getSummon()!=null){
								central.healAll(character, true);
							}
						}else{
							if(!canReviveInside){
								character.doRevive();
							}
							Location LocT = PLAYER_INSIDE.get(character.getName()).getLocation();
							character.teleToLocation(LocT);
							pvpInstance.playerLeave(character);
							Zone.removeFromInstance(character);
						}
					}
				}
			}
		}, reviveTime * 1000);
		return true;
	}
	
	private void dropItems(L2PcInstance player){
		if(this.DROPZONE_ITEM==null){
			return;
		}
		if(this.DROPZONE_ITEM.size()==0){
			return;
		}
		for (L2ItemInstance itemDrop : player.getInventory().getItems()){
			if(this.DROPZONE_ITEM.containsKey(itemDrop.getItem().getId())){
				_DROP_ZONE_VALUES dropData = this.DROPZONE_ITEM.get(itemDrop.getItem().getId());
				long _DropMaxCount = ( itemDrop.getCount() * dropData.getPorcentToDrop() ) / 100l;
				long _CountToDrop = Rnd.get(1l, _DropMaxCount); //aleatorio.nextInt((int) _DropMaxCount);
				int _x = (player.getX() + Rnd.get(50)) - 25;
				int _y = (player.getY() + Rnd.get(50)) - 25;
				int _z = player.getZ() + 20;
				if(opera.haveItem(player, itemDrop.getId(), _CountToDrop)){
					player.dropItem("Drop", itemDrop.getObjectId(), _CountToDrop, _x, _y, _z, null, false, false);
				}
			}
		}
	}
	
	public void addPvPKill(L2PcInstance KILLER, L2PcInstance DEATH){
		if(!this.PLAYER_INSIDE.containsKey(KILLER.getName()) && !this.PLAYER_INSIDE.containsKey(DEATH.getName())){
			return;
		}
		if(this.dualboxcheck){
			try{
				if(ZeuS.isDualBox_pc(KILLER, DEATH)){
					return;
				}
			}catch(Exception a){
				return;
			}
		}
		
		if(this.pvpCount){
			KILLER.setPvpKills( KILLER.getPvpKills() + 1 );
			KILLER.sendUserInfo(true);
		}
		this.PLAYER_INSIDE.get(KILLER.getName()).Setkill();
		this.PLAYER_INSIDE.get(DEATH.getName()).Setdeath();
		
		if(this.canDrop){
			dropItems(DEATH);
		}
		
		if(!this.rewardByEmail){
			opera.giveReward(KILLER, this.RewardForKill);
			opera.giveReward(DEATH, this.RewardForDeath);
			if(this.CLAN_REPUTATION){
				if(KILLER.getClan()!=null){
					KILLER.getClan().setReputationScore( KILLER.getClan().getReputationScore() + this.CLAN_REPUTATION_VALUE , true);
					central.msgbox("You give " + String.valueOf(this.CLAN_REPUTATION_VALUE) + " Reputation for your clan" , KILLER);
				}
			}			
		}
	}

	public boolean classCanEnter(L2PcInstance ppl){
		if(this.PROHIBITED_CLASSES!=null){
			if(this.PROHIBITED_CLASSES.size()>0){
				if(this.PROHIBITED_CLASSES.contains(ppl.getClassId().getId())){
					return false;
				}
			}
		}
		
		if(this.Type == _ZONES_TYPE.CLASSES){
			if(this.ID_ALLOWED_CLASS_ENTER != ppl.getClassId().getId()){
				return false;
			}
		}
		
		return true;
	}
	
	public void addPlayer(L2PcInstance pplayer, int InstanceID, boolean isParty){
		if(this.PROHIBITED_CLASSES!=null){
			if(this.PROHIBITED_CLASSES.size()>0){
				if(this.PROHIBITED_CLASSES.contains(pplayer.getClassId().getId())){
					central.msgbox("You class cant enter to this zone", pplayer);
				}
			}
		}
		
		Vector<L2PcInstance> PlayerToEnter = new Vector<L2PcInstance>();
		
		PlayerToEnter.add(pplayer);
		
		if(!isParty){
			if(!this.canParty && pplayer.getParty()!=null){
				central.msgbox("In this zone you cant enter with party", pplayer);
				return;
			}
			if(!this.canPartyJustClan && pplayer.getParty()!=null){
				central.msgbox("In this zone you cant enter with party", pplayer);
				return;				
			}
		}else{
			if(pplayer.isInParty()){
				for(L2PcInstance tempPla : pplayer.getParty().getMembers()){
					if(tempPla != pplayer){
						PlayerToEnter.add(tempPla);
					}
				}
			}else{
				return;
			}
		}
		
		
		
		for(L2PcInstance ppl : PlayerToEnter){
			if(this.PLAYER_INSIDE==null){
				_CHAR_INFO_ZONE temp = new _CHAR_INFO_ZONE();
				temp.setValue(ppl);
				this.PLAYER_INSIDE.put(ppl.getName(), temp);
			}else if(this.PLAYER_INSIDE.size()==0){
				_CHAR_INFO_ZONE temp = new _CHAR_INFO_ZONE();
				temp.setValue(ppl);
				this.PLAYER_INSIDE.put(ppl.getName(), temp);
			}else if(!this.PLAYER_INSIDE.containsKey(ppl.getName())){
				_CHAR_INFO_ZONE temp = new _CHAR_INFO_ZONE();
				temp.setValue(ppl);
				this.PLAYER_INSIDE.put(ppl.getName(), temp);
			}
			this.PLAYER_INSIDE.get(ppl.getName()).isInside = true;
			if(CHAR_FOR_REWARD!=null){
				if(!CHAR_FOR_REWARD.contains(ppl.getName())){
					CHAR_FOR_REWARD.add(ppl.getName());
				}
			}else{
				CHAR_FOR_REWARD.add(ppl.getName());	
			}
			cbFormato.cerrarCB(ppl);
			cbFormato.cerrarTutorial(ppl);			
		}
		moveChar(pplayer,isParty,InstanceID);
	}
	public void removePlayer(L2PcInstance ppl){
		if(!ppl.isGM()){
			ppl.startAntifeedProtection(false, false, false, false);
		}
		try{
			if(ppl.isDead()){
				ppl.doRevive();
			}
		}catch(Exception a){
			
		}
		try{
			this.PLAYER_INSIDE.get(ppl.getName()).isInside = false;
		}catch(Exception a){
			
		}
	}
	public void resetPlayerInside(){
		CHAR_FOR_REWARD.clear();
		this.PLAYER_INSIDE.clear();
	}
	
	public void removeFromInstance(L2PcInstance player){
		try{
			player.setInstanceId(0);
			this.PLAYER_INSIDE.get(player.getName()).isInside = false;
			player.teleToLocation(this.PLAYER_INSIDE.get(player.getName()).getLocation());
		}catch(Exception a){
			
		}
	}
	private void startReward(){
		if(CHAR_FOR_REWARD!=null){
			if(CHAR_FOR_REWARD.size()>0){
				for(String CharName : this.CHAR_FOR_REWARD){
					try{
						if(!this.PLAYER_INSIDE.containsKey(CharName)){
							continue;
						}
						giveReward(CharName, this.PLAYER_INSIDE.get(CharName).getKill(), this.PLAYER_INSIDE.get(CharName).getDeath());
					}catch(Exception a){
						_log.warning("("+ this.nameZone +") Error in reward Player->" + CharName + ". Error->" + a.getMessage());
					}
				}
			}
		}		
	}
	
	@SuppressWarnings("rawtypes")
	private void moveAllPlayerToOriginalLoc(){
		if(this.PLAYER_INSIDE !=null){
			if(this.PLAYER_INSIDE.size()>0){
				Iterator itr = this.PLAYER_INSIDE.entrySet().iterator();
				while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	String charName = (String)Entrada.getKey();
			    	_CHAR_INFO_ZONE chaInfo = (_CHAR_INFO_ZONE)Entrada.getValue();
			    	if(!chaInfo.isInside){
			    		continue;
			    	}
			    	try{
			    		L2PcInstance player = opera.getPlayerbyName(charName);
			    		if(player!=null){
					    	if(player.getInstanceId()>0){
					    		if(player.getInstanceId() != this.idInstance){
					    			continue;
					    		}
					    		player.setInstanceId(0);
					    		player.startAntifeedProtection(false, false, false, false);
						    	player.teleToLocation(chaInfo.getLocation());
						    	if(player.isDead()){
						    		try{
								    	if(player.isDead()){
								    		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
								    			@Override
								    			public void run(){
								    				if(player.isDead()){
								    					player.setInstanceId(0);
								    					player.sendPacket(new Die(player));
								    					player.startAntifeedProtection(false, false, false,false);
								    				}
								    			}
								    		}, 8 * 1000);
								    	}
							    	}catch(Exception a){
							    		_log.warning("Error reviviendo->" + a.getMessage());
							    	}						    		
						    	}
					    	}
			    		}
			    	}catch(Exception a){
			    		
			    	}
				}							
			}
		}
	}
	
	public void EndZone(){
		this.isOn = false;
		this.ID_ALLOWED_CLASS_ENTER = -1;
		if(this.rewardByEmail){
			startReward();
		}
		saveDataInBD();		
		moveAllPlayerToOriginalLoc();
		this.PLAYER_INSIDE.clear();
		this.CHAR_FOR_REWARD.clear();
		rbFinish();
	}
	
	@SuppressWarnings("rawtypes")
	private void saveDataInBD(){
		if(this.PLAYER_INSIDE !=null){
			if(this.PLAYER_INSIDE.size()>0){
				Iterator itr = this.PLAYER_INSIDE.entrySet().iterator();
				while(itr.hasNext()){
			    	Map.Entry Entrada = (Map.Entry)itr.next();
			    	_CHAR_INFO_ZONE chaInfo = (_CHAR_INFO_ZONE)Entrada.getValue();
			    	chaInfo.saveRecord();
				}
			}
		}
	}
	
	private void setUnixTimeStart(int Start){
		this.INIT_UNIX_TIME = Start;
	}
	
	protected class PERSONAL_PVP_CLOCK implements Runnable{
		private int UnixTimeBegin;
		public PERSONAL_PVP_CLOCK(int UnixTime){
			this.UnixTimeBegin = UnixTime;
		}
		public void run(){
			setUnixTimeStart(this.UnixTimeBegin); 
			startClock();
		}
	}
	
	public void startClock(){
		try{
			long startWaiterTime = System.currentTimeMillis();
			int seconds = this.Minutes * 60;
			int interval = seconds * 1000;
			boolean isOK = true;
			if ((startWaiterTime + interval > System.currentTimeMillis()) && isOK){
				try{
					int tiempoRestante = seconds - ( opera.getUnixTimeNow() - this.INIT_UNIX_TIME );
					int Minutes = tiempoRestante / 60;
					int Seconds = tiempoRestante - (Minutes * 60);
					String Tiempo = ( Minutes <= 9 ? String.valueOf("0" + Minutes) : String.valueOf(Minutes)) + ":" + ( Seconds <=9 ? String.valueOf("0" + Seconds) : String.valueOf(Seconds)) ;
					String Text = Tiempo + "\n Kill: %KILL%  Death: %DEATH%" ;
					AnnounceTime(Text);
					try{
						//Thread.sleep(300);
						ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			    			@Override
			    			public void run(){
			    				startClock();
			    			}
			    		}, 300);
					}catch (Exception ie){
						_log.warning("Error in counter->" + ie.getMessage());
						isOK = false;
					}
				}catch(Exception a ){
					
				}
			}
		}catch(Exception a){
			_log.warning("Error start pvp instance counter->" + a.getMessage());
		}
	}
	
	public String getClassNameAllow(){
		if(this.ID_ALLOWED_CLASS_ENTER>0){
			return general.getClassInfo(this.ID_ALLOWED_CLASS_ENTER).getName();
		}
		return "NO SELECTED";
	}
	
	public void startCount(int UnixTimeBegin, int IDINSTANCE){
		if(this.Type== _ZONES_TYPE.CLASSES){
			Random aleatorio = new Random();
			try{
				this.ID_ALLOWED_CLASS_ENTER = this.ALLOWED_CLASSES.get(aleatorio.nextInt( ALLOWED_CLASSES.size()));
			}catch(Exception a){
				this.ID_ALLOWED_CLASS_ENTER = -1;
				_log.warning("The System cant not create a Allow Class for this Zone. Error->" + a.getMessage());
			}
		}
		this.isOn=true;
		checkRbInZone();
		idInstance = IDINSTANCE;
		ThreadPoolManager.getInstance().scheduleGeneral(new PERSONAL_PVP_CLOCK(UnixTimeBegin) , 2 * 1000);
	}
	
	private void checkRbInZone(){
		if(this.RAIDBOSS_ENABLED){
			if(this.RAIDBOSS_IDS!=null){
				if(this.RAIDBOSS_IDS.size()>0){
					if(this.RAIDBOSS_DELAY>0){
						if(this.RAIDBOSS_SPAWN!=null){
							if(this.RAIDBOSS_SPAWN.size()>0){
								int CantRB = this.RAIDBOSS_IDS.size();
								int CantSpawn = this.RAIDBOSS_SPAWN.size();
								int IDRBSELECTED = 0;
								String SPAWNRBSELECTED = "";
								if(CantRB==1){
									IDRBSELECTED = this.RAIDBOSS_IDS.get(0);
								}else{
									Random aleatorio = new Random();
									IDRBSELECTED = this.RAIDBOSS_IDS.get(aleatorio.nextInt(CantRB));
								}
								if(CantSpawn==1){
									SPAWNRBSELECTED = this.RAIDBOSS_SPAWN.get(0);
								}else{
									Random aleatorio = new Random();
									SPAWNRBSELECTED = this.RAIDBOSS_SPAWN.get(aleatorio.nextInt(CantSpawn));
								}
								spawnRB(IDRBSELECTED,SPAWNRBSELECTED);
							}
						}
					}
				}
			}
		}
	}
	
	@SuppressWarnings("static-access")
	private void createAndSpawnRB(int _bossId, String Location){
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(_bossId);
		try
		{
			int _bossX = Integer.valueOf(Location.split(",")[0]);
			int _bossY = Integer.valueOf(Location.split(",")[1]);
			int _bossZ = Integer.valueOf(Location.split(",")[2]);
			
			L2Spawn _bossSpawn = new L2Spawn(tmpl);
			_bossSpawn.setX(_bossX);
			_bossSpawn.setY(_bossY);
			_bossSpawn.setZ(_bossZ);
			_bossSpawn.setAmount(1);
			_bossSpawn.setHeading(0);
			_bossSpawn.setRespawnDelay(20);
			_bossSpawn.setAmount(1);
			SpawnTable.getInstance().addNewSpawn(_bossSpawn, false);
			_bossSpawn.setInstanceId(this.idInstance);
			_bossSpawn.init();
			_bossSpawn.getLastSpawn().setTitle("PVP ZONE");
			_bossSpawn.getLastSpawn()._isEventMobRaid = false;
			_bossSpawn.getLastSpawn().isAggressive();
			_bossSpawn.getLastSpawn().isVisible();
			_bossSpawn.getLastSpawn().decayMe();
			_bossSpawn.setRespawnDelay(5000000);
			_bossSpawn.getLastSpawn().spawnMe(_bossSpawn.getLastSpawn().getX(), _bossSpawn.getLastSpawn().getY(), _bossSpawn.getLastSpawn().getZ());
			_bossSpawn.getLastSpawn().broadcastPacket(new MagicSkillUse(_bossSpawn.getLastSpawn(), _bossSpawn.getLastSpawn(), 1034, 1, 1, 1));
		}
		catch (Exception e)
		{
			_log.warning("PvP Raid Engine[spawnEventNpc(" + String.valueOf(_bossId) + ")]: exception: " + e.getMessage());
		}		
	}
	
	@SuppressWarnings("rawtypes")
	private void annoucementZone(String Msnje){
		if(this.PLAYER_INSIDE !=null){
			if(this.PLAYER_INSIDE.size()>0){
				try{
					Iterator itr = this.PLAYER_INSIDE.entrySet().iterator();
					while(itr.hasNext()){
				    	Map.Entry Entrada = (Map.Entry)itr.next();
				    	String charName = (String)Entrada.getKey();
				    	_CHAR_INFO_ZONE cT = (_CHAR_INFO_ZONE)Entrada.getValue();
				    	if(!cT.isInside){
				    		continue;
				    	}
				    	try{
				    		L2PcInstance pplT = opera.getPlayerbyName(charName);
				    		if(pplT == null){
				    			continue;
				    		}
				    		central.msgbox_Lado(Msnje, pplT);				    		
				    	}catch(Exception a){
				    		
				    	}
					}
				}catch(Exception a){
					
				}
			}
		}
	}
	
	private void rbFinish(){
		try{
			if (_bossSpawn == null) {
				return;
			}
			try{
				_bossSpawn.getLastSpawn().isInvisible();
			}catch(Exception a){
				
			}
			_bossSpawn.getLastSpawn().deleteMe();
			_bossSpawn.stopRespawn();
			SpawnTable.getInstance().deleteSpawn(_bossSpawn, true);
			_bossSpawn = null;
		}catch(Exception a){
			_log.warning("RaidBoss Event: Error unspawn RB->" + a.getMessage());
		}
	}
	
	private void spawnRB(int idRB, String Spawn){
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			@Override
			public void run(){
				createAndSpawnRB(idRB,Spawn);
				annoucementZone("Raid Boss Has Spawned. Found and Kill it!!!");
			}
		}, this.RAIDBOSS_DELAY * 1000);		
	}
	
	@SuppressWarnings("rawtypes")
	private void AnnounceTime(String Text){
		try{
			if(this.PLAYER_INSIDE==null){
				return;
			}else if(this.PLAYER_INSIDE.size()==0){
				return;
			}
			
			Iterator itr = this.PLAYER_INSIDE.entrySet().iterator();
			while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	String charName = (String)Entrada.getKey();
		    	_CHAR_INFO_ZONE cT = (_CHAR_INFO_ZONE) Entrada.getValue();
		    	if(!cT.isInside){
		    		continue;
		    	}
		    	try{
		    		L2PcInstance pplT = opera.getPlayerbyName(charName);
		    		if(pplT == null){
		    			continue;
		    		}
		    		String SendMensaje = "";
					SendMensaje = Text.replace("%KILL%", String.valueOf(this.PLAYER_INSIDE.get(pplT.getName()).getKill())).replace("%DEATH%", String.valueOf(this.PLAYER_INSIDE.get(pplT.getName()).getDeath()));
					ExShowScreenMessage Mnsje = new ExShowScreenMessage(1, -1,  ExShowScreenMessage.TOP_RIGHT , 1, /*Size*/1, 0, 0, false, 310, false, SendMensaje);				
					pplT.sendPacket(Mnsje);				    		
		    	}catch(Exception a){
		    		
		    	}
			}			
		}catch(Exception a){
			
		}
	}
	@SuppressWarnings({ "null", "rawtypes" })
	private void giveReward(String CharName, int kills, int deaths){
		if(kills == 0 && deaths==0){
			return;
		}
		if(CharName == null){
			if(general.DEBUG_CONSOLA_ENTRADAS){
				_log.warning("Reward PVP Zone, Name = null");
			}
			return;
		}

		if(CharName.length()==0){
			if(general.DEBUG_CONSOLA_ENTRADAS){
				_log.warning("Reward PVP Zone, Name = null");
			}
			return;
		}
		
		Map<Integer, Long>ItemToGive = new HashMap<Integer, Long>();
		String ItemToSend = "";
		
		if(kills>0){
			if(this.RewardForKill.indexOf(";")>=1){
				for(String Items : RewardForKill.split(";")){
					int IdItem = Integer.valueOf(Items.split(",")[0]);
					Long CantItem = Long.valueOf(Items.split(",")[1]) * kills;
					if(ItemToGive!=null){
						if(ItemToGive.size()>0){
							if(ItemToGive.containsKey(IdItem)){
								ItemToGive.put(IdItem, ItemToGive.get(IdItem)+CantItem);
							}else{
								ItemToGive.put(IdItem, CantItem);
							}
						}else{
							ItemToGive.put(IdItem, CantItem);
						}
					}else{
						ItemToGive.put(IdItem, CantItem);
					}
				}
			}else{
				ItemToGive.put(Integer.valueOf(this.RewardForKill.split(",")[0]) , Long.valueOf(this.RewardForKill.split(",")[1]) * kills);
			}
		}
		if(deaths>0){
			if(this.RewardForDeath.indexOf(";")>=0){
				for(String Items : RewardForDeath.split(";")){
					int IdItem = Integer.valueOf(Items.split(",")[0]);
					Long CantItem = Long.valueOf(Items.split(",")[1]) * deaths;
					if(ItemToGive!=null){
						if(ItemToGive.size()>0){
							if(ItemToGive.containsKey(IdItem)){
								ItemToGive.put(IdItem, ItemToGive.get(IdItem)+CantItem);
							}else{
								ItemToGive.put(IdItem, CantItem);
							}
						}else{
							ItemToGive.put(IdItem, CantItem);
						}
					}else{
						ItemToGive.put(IdItem, CantItem);
					}			
				}
			}else{
				boolean addSingle = true;
				int IdItem = Integer.valueOf(this.RewardForDeath.split(",")[0]);
				Long Quantity = Long.valueOf(this.RewardForDeath.split(",")[1]);
				if(ItemToGive!=null){
					if(ItemToGive.size()>0){
						if(ItemToGive.containsKey(IdItem)){
							Long TQuan = ItemToGive.get(IdItem) + (Quantity * deaths);
							ItemToGive.put(IdItem, TQuan);
							addSingle = false;
						}
					}
				}
				if(addSingle){
					ItemToGive.put(IdItem, Quantity * deaths);
				}
			}
		}
		if(ItemToGive.size()>0){
			Iterator itr = ItemToGive.entrySet().iterator();
			while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	int IdItem = (int)Entrada.getKey();
		    	Long Cantidad = (Long)Entrada.getValue();
		    	if(ItemToSend.length()>0){
		    		ItemToSend += ";";
		    	}
		    	ItemToSend+= String.valueOf(IdItem) + "," + String.valueOf(Cantidad);	    	
			}
		}
		
		if(this.CLAN_REPUTATION && kills>0){
			try{
				L2PcInstance tempPlayer = opera.getPlayerbyName(CharName);
				if(tempPlayer!=null){
					if(opera.isCharOnline(tempPlayer)){
						if(tempPlayer.getClan()!=null){
							tempPlayer.getClan().setReputationScore( tempPlayer.getClan().getReputationScore() + (this.CLAN_REPUTATION_VALUE * kills) , true);
							central.msgbox("You give " + String.valueOf(this.CLAN_REPUTATION_VALUE * kills) + " Reputation for your clan" , tempPlayer);
						}
					}
				}
			}catch(Exception a){
				
			}
		}
		EmailSend.sendEmail(null, CharName , "Reward " + nameZone, "This is your Reward (Kills: "+ String.valueOf(kills) +", Death: "+ String.valueOf(deaths) +")", ItemToSend, tipoMensaje.Reward);
	}
}

class _DROP_ZONE_VALUES{
	private final int idItem;
	private final long Porcent;
	private final int Minimum_Amount_Allowed;
	private final boolean Requerided;
	@SuppressWarnings("unused")
	private final static Logger _log = Logger.getLogger(pvpInstance.class.getName());
	public _DROP_ZONE_VALUES(int _idItem, long _porcen, int Minimum_Amount_Allowed, boolean _required){
		this.idItem = _idItem;
		this.Porcent = _porcen;
		this.Minimum_Amount_Allowed = Minimum_Amount_Allowed;
		this.Requerided = _required;
	}
	public int getItem(){
		return this.idItem;
	}
	public int getMinimumAmountAllowed(){
		return this.Minimum_Amount_Allowed;
	}
	public long getPorcentToDrop(){
		return this.Porcent;
	}
	public boolean isRequerided(){
		return this.Requerided;
	}
}

class _CHAR_INFO_ZONE{
	private String Name;
	private int CountKill=0;
	private int CountDeath=0;
	private int PlayerId = 0;
	private String ClassName;
	private Location OriginalLocation;
	public boolean isInside;
	private final Logger _log = Logger.getLogger(_CHAR_INFO_ZONE.class.getName());
	protected _CHAR_INFO_ZONE(){

	}
	public void setValue(L2PcInstance player){
		this.Name = player.getName();
		this.CountKill=0;
		this.CountDeath=0;
		this.PlayerId = player.getObjectId();
		this.ClassName = opera.getClassName(player.getClassId().getId());
		this.OriginalLocation = player.getLocation();
		this.isInside = true;
	}
	public Location getLocation(){
		return this.OriginalLocation;
	}
	public void Setkill(){
		this.CountKill++;
	}
	public void Setdeath(){
		this.CountDeath++;
	}
	public int getKill(){
		return this.CountKill;
	}
	public int getDeath(){
		return this.CountDeath;
	}
	public String getName(){
		return this.Name;
	}
	public String getClassName(){
		return this.ClassName;
	}
	public void saveRecord(){
		Calendar now = Calendar.getInstance();
		int MonthNow = now.get(Calendar.MONTH) + 1;
		String Query = "INSERT INTO zeus_pvp_zone (idChar, month, allKill, allDeath) VALUES (?,?,?,?) " +
		"ON DUPLICATE KEY UPDATE allKill=allKill+?, allDeath=allDeath+?";
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Query);	
			ins.setInt(1, this.PlayerId);
			ins.setInt(2, MonthNow);
			ins.setInt(3, this.CountKill);
			ins.setInt(4, this.CountDeath);
			ins.setInt(5, this.CountKill);
			ins.setInt(6, this.CountDeath);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error Executing the Script on pvp Instance zone BD records" + e.getMessage());
			}
		}catch(Exception a){
			_log.warning("Error Saving Player ("+ this.Name +") PvP Instance Data ->" + a.getMessage());
		}
	}
}