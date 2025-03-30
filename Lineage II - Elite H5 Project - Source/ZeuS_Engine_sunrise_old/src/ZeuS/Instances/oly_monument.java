package ZeuS.Instances;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import ZeuS.GM.fakenpc;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class oly_monument {
	private static final Logger _log = Logger.getLogger(oly_monument.class.getName());

	private static int OLY_TOP_NPC_1 = -1;
	private static int OLY_TOP_NPC_2 = -1;
	private static int OLY_TOP_NPC_3 = -1;
	private static int OLY_TOP_NPC_4 = -1;
	
	private static int DUELIST_MONUMENT = -1;
	private static int PHOENIX_KNIGHT_MONUMENT = -1;
	private static int SAGITTARIUS_MONUMENT = -1;
	private static int ARCHMAGE_MONUMENT = -1;
	private static int ARCANA_LORD_MONUMENT = -1;
	private static int HIEROPLANT_MONUMENT = -1;
	private static int SWORD_MUSE_MONUMENT = -1;
	private static int MOONLIGHT_SENTINEL_MONUMENT = -1;
	private static int ELEMENTAL_MASTER_MONUMENT = -1;
	private static int SHILLIEN_TEMPLAR_MONUMENT = -1;
	private static int GHOST_HUNTER_MONUMENT = -1;
	private static int STORM_SCREAMER_MONUMENT = -1;
	private static int SHILLIEN_SAINT_MONUMENT = -1;
	private static int GRAND_KHAVATARI_MONUMENT = -1;
	private static int DOOMCRYER_MONUMENT = -1;
	private static int MAESTRO_MONUMENT = -1;
	private static int SOUL_HOUND_MONUMENT = -1;
	private static int DREADNOUGHT_MONUMENT = -1;
	private static int HELL_KNIGHT_MONUMENT = -1;
	private static int ADVENTURER_MONUMENT = -1;
	private static int SOULTAKER_MONUMENT = -1;
	private static int CARDINAL_MONUMENT = -1;
	private static int EVAS_TEMPLAR_MONUMENT = -1;
	private static int WIND_RIDER_MONUMENT = -1;
	private static int MYSTIC_MUSE_MONUMENT = -1;
	private static int EVAS_SAINT_MONUMENT = -1;
	private static int SPECTRAL_DANCER_MONUMENT = -1;
	private static int GHOST_SENTINEL_MONUMENT = -1;
	private static int SPECTRAL_MASTER_MONUMENT = -1;
	private static int TITAN_MONUMENT = -1;
	private static int DOMINATOR_MONUMENT = -1;
	private static int FORTUNE_SEEKER_MONUMENT = -1;
	private static int DOOMBRINGER_MONUMENT = -1;
	private static int TRICKSTER_MONUMENT = -1;
	private static int OLYMPIAD_ZONE_INSTANCE = -1;
	private static Map<Integer, Integer> CLASS_NPC = new HashMap<Integer, Integer>();
	private static boolean ZoneCreated = false;
	
	//						ID CLASS, DATA
	private static Map<Integer, _monumentData> HERO_MONUMENT = new HashMap<Integer, _monumentData>();
	private static Map<Integer, olyMonument_player_respawn> RESPAWN_PLAYERS = new HashMap<Integer, olyMonument_player_respawn>();
	
	public static void load(){
		try{
			CLASS_NPC.clear();
		}catch(Exception a){
			
		}
		
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File("./config/zeus/zeus_oly_monument.properties")));
			OLY_TOP_NPC_1 = Integer.valueOf(propZ.getProperty("ID_NPC_TOP_HERO_1"));
			OLY_TOP_NPC_2 = Integer.valueOf(propZ.getProperty("ID_NPC_TOP_HERO_2"));
			OLY_TOP_NPC_3 = Integer.valueOf(propZ.getProperty("ID_NPC_TOP_HERO_3"));
			OLY_TOP_NPC_4 = Integer.valueOf(propZ.getProperty("ID_NPC_TOP_HERO_4"));
			
			DUELIST_MONUMENT = Integer.valueOf(propZ.getProperty("DUELIST_MONUMENT"));
			PHOENIX_KNIGHT_MONUMENT = Integer.valueOf(propZ.getProperty("PHOENIX_KNIGHT_MONUMENT"));
			SAGITTARIUS_MONUMENT = Integer.valueOf(propZ.getProperty("SAGITTARIUS_MONUMENT"));
			ARCHMAGE_MONUMENT = Integer.valueOf(propZ.getProperty("ARCHMAGE_MONUMENT"));
			ARCANA_LORD_MONUMENT = Integer.valueOf(propZ.getProperty("ARCANA_LORD_MONUMENT"));
			HIEROPLANT_MONUMENT = Integer.valueOf(propZ.getProperty("HIEROPLANT_MONUMENT"));
			SWORD_MUSE_MONUMENT = Integer.valueOf(propZ.getProperty("SWORD_MUSE_MONUMENT"));
			MOONLIGHT_SENTINEL_MONUMENT = Integer.valueOf(propZ.getProperty("MOONLIGHT_SENTINEL_MONUMENT"));
			ELEMENTAL_MASTER_MONUMENT = Integer.valueOf(propZ.getProperty("ELEMENTAL_MASTER_MONUMENT"));
			SHILLIEN_TEMPLAR_MONUMENT = Integer.valueOf(propZ.getProperty("SHILLIEN_TEMPLAR_MONUMENT"));
			GHOST_HUNTER_MONUMENT = Integer.valueOf(propZ.getProperty("GHOST_HUNTER_MONUMENT"));
			STORM_SCREAMER_MONUMENT = Integer.valueOf(propZ.getProperty("STORM_SCREAMER_MONUMENT"));
			SHILLIEN_SAINT_MONUMENT = Integer.valueOf(propZ.getProperty("SHILLIEN_SAINT_MONUMENT"));
			GRAND_KHAVATARI_MONUMENT = Integer.valueOf(propZ.getProperty("GRAND_KHAVATARI_MONUMENT"));
			DOOMCRYER_MONUMENT = Integer.valueOf(propZ.getProperty("DOOMCRYER_MONUMENT"));
			MAESTRO_MONUMENT = Integer.valueOf(propZ.getProperty("MAESTRO_MONUMENT"));
			SOUL_HOUND_MONUMENT = Integer.valueOf(propZ.getProperty("SOUL_HOUND_MONUMENT"));
			DREADNOUGHT_MONUMENT = Integer.valueOf(propZ.getProperty("DREADNOUGHT_MONUMENT"));
			HELL_KNIGHT_MONUMENT = Integer.valueOf(propZ.getProperty("HELL_KNIGHT_MONUMENT"));
			ADVENTURER_MONUMENT = Integer.valueOf(propZ.getProperty("ADVENTURER_MONUMENT"));
			SOULTAKER_MONUMENT = Integer.valueOf(propZ.getProperty("SOULTAKER_MONUMENT"));
			CARDINAL_MONUMENT = Integer.valueOf(propZ.getProperty("CARDINAL_MONUMENT"));
			EVAS_TEMPLAR_MONUMENT = Integer.valueOf(propZ.getProperty("EVAS_TEMPLAR_MONUMENT"));
			WIND_RIDER_MONUMENT = Integer.valueOf(propZ.getProperty("WIND_RIDER_MONUMENT"));
			MYSTIC_MUSE_MONUMENT = Integer.valueOf(propZ.getProperty("MYSTIC_MUSE_MONUMENT"));
			EVAS_SAINT_MONUMENT = Integer.valueOf(propZ.getProperty("EVAS_SAINT_MONUMENT"));
			SPECTRAL_DANCER_MONUMENT = Integer.valueOf(propZ.getProperty("SPECTRAL_DANCER_MONUMENT"));
			GHOST_SENTINEL_MONUMENT = Integer.valueOf(propZ.getProperty("GHOST_SENTINEL_MONUMENT"));
			SPECTRAL_MASTER_MONUMENT = Integer.valueOf(propZ.getProperty("SPECTRAL_MASTER_MONUMENT"));
			TITAN_MONUMENT = Integer.valueOf(propZ.getProperty("TITAN_MONUMENT"));
			DOMINATOR_MONUMENT = Integer.valueOf(propZ.getProperty("DOMINATOR_MONUMENT"));
			FORTUNE_SEEKER_MONUMENT = Integer.valueOf(propZ.getProperty("FORTUNE_SEEKER_MONUMENT"));
			DOOMBRINGER_MONUMENT = Integer.valueOf(propZ.getProperty("DOOMBRINGER_MONUMENT"));
			TRICKSTER_MONUMENT = Integer.valueOf(propZ.getProperty("TRICKSTER_MONUMENT"));
			
			CLASS_NPC.put(88, DUELIST_MONUMENT);
			CLASS_NPC.put(90, PHOENIX_KNIGHT_MONUMENT);
			CLASS_NPC.put(92, SAGITTARIUS_MONUMENT);
			CLASS_NPC.put(94, ARCHMAGE_MONUMENT);
			CLASS_NPC.put(96, ARCANA_LORD_MONUMENT);
			CLASS_NPC.put(98, HIEROPLANT_MONUMENT);
			CLASS_NPC.put(100, SWORD_MUSE_MONUMENT);
			CLASS_NPC.put(102, MOONLIGHT_SENTINEL_MONUMENT);
			CLASS_NPC.put(104, ELEMENTAL_MASTER_MONUMENT);
			CLASS_NPC.put(106, SHILLIEN_TEMPLAR_MONUMENT);
			CLASS_NPC.put(108, GHOST_HUNTER_MONUMENT);
			CLASS_NPC.put(110, STORM_SCREAMER_MONUMENT);
			CLASS_NPC.put(112, SHILLIEN_SAINT_MONUMENT);
			CLASS_NPC.put(114, GRAND_KHAVATARI_MONUMENT);
			CLASS_NPC.put(116, DOOMCRYER_MONUMENT);
			CLASS_NPC.put(118, MAESTRO_MONUMENT);
			CLASS_NPC.put(132, SOUL_HOUND_MONUMENT);
			CLASS_NPC.put(133, SOUL_HOUND_MONUMENT);
			CLASS_NPC.put(89, DREADNOUGHT_MONUMENT);
			CLASS_NPC.put(91, HELL_KNIGHT_MONUMENT);
			CLASS_NPC.put(93, ADVENTURER_MONUMENT);
			CLASS_NPC.put(95, SOULTAKER_MONUMENT);
			CLASS_NPC.put(97, CARDINAL_MONUMENT);
			CLASS_NPC.put(99, EVAS_TEMPLAR_MONUMENT);
			CLASS_NPC.put(101, WIND_RIDER_MONUMENT);
			CLASS_NPC.put(103, MYSTIC_MUSE_MONUMENT);
			CLASS_NPC.put(105, EVAS_SAINT_MONUMENT);
			CLASS_NPC.put(107, SPECTRAL_DANCER_MONUMENT);
			CLASS_NPC.put(109, GHOST_SENTINEL_MONUMENT);
			CLASS_NPC.put(111, SPECTRAL_MASTER_MONUMENT);
			CLASS_NPC.put(113, TITAN_MONUMENT);
			CLASS_NPC.put(115, DOMINATOR_MONUMENT);
			CLASS_NPC.put(117, FORTUNE_SEEKER_MONUMENT);
			CLASS_NPC.put(131, DOOMBRINGER_MONUMENT);
			CLASS_NPC.put(134, TRICKSTER_MONUMENT);
			loadNewInformation();			
			if(!ZoneCreated){
				createOlympiadZoneInstance();
				ZoneCreated = true;
			}
		}catch(Exception a){
			_log.warning("Olympiad Npc Monument, wrog on loading->" + a.getMessage());
		}		
	}
	
	@SuppressWarnings("unused")
	private static String getClassNameNPC(int idNpc){
		int idClass = getRaceFromNpcId(idNpc);
   		return opera.getClassName(idClass);
	}
	
	@SuppressWarnings("unused")
	private static String getCharNameFromNpc(int idClass){
		try{
			return HERO_MONUMENT.get(idClass).getPlayerName();
		}catch(Exception a){
			
		}
		return "NONE";
	}
	
	@SuppressWarnings("rawtypes")
	private static boolean isNpcFromMonument(int IdNpc, boolean checkAll){
		Iterator itr = HERO_MONUMENT.entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	_monumentData Temp = (_monumentData)Entrada.getValue();
	    	if(Temp.getIdNpc() == IdNpc){
	    		return true;
	    	}
		}
		if(checkAll){
			if(isFromTopMonument(IdNpc)){
				return true;
			}
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	private static int getRaceFromNpcId(int idNpc){
		int Retorno = -1;
		//HERO_MONUMENT
		Iterator itr = HERO_MONUMENT.entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int ID_RACE = (int)Entrada.getKey();
	    	_monumentData Temp = (_monumentData)Entrada.getValue();
	    	if(Temp.getIdNpc() == idNpc || ( Temp.getIsTop() && Temp.getIdNpcTop() == idNpc)){
	    		return ID_RACE;
	    	}
		}
		return Retorno;
	}
	
	public static void loadNewInformation(boolean onload){
		loadNpcInfo();
		loadHeroes();
		loadTopHeroes();
	}
	
	public static void loadNewInformation(){
		loadNewInformation(true);
	}
	
	@SuppressWarnings("rawtypes")
	private static void loadNpcInfo(){
		//ID CLASS, NPC OWNER
		try{
			HERO_MONUMENT.clear();
		}catch(Exception a){
			
		}
		Iterator itr = CLASS_NPC.entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int ID_CLASS = (int)Entrada.getKey();
	    	int ID_NPC = (int)Entrada.getValue();
	    	_monumentData Tem = new _monumentData(ID_NPC, ID_CLASS);
	    	HERO_MONUMENT.put(ID_CLASS, Tem);
		}
	}
	
	private static void loadTopHeroes(){
		int Contador = 1;
		int idNpcTop = -1;
		String Consulta = "SELECT heroes.class_id, heroes.charId, heroes.count FROM heroes WHERE heroes.played = 1 GROUP BY heroes.class_id ORDER BY heroes.points DESC LIMIT 4";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement st = con.createStatement();
				ResultSet rset = st.executeQuery(Consulta)){
				while (rset.next()){
					if(Contador == 1){
						idNpcTop = OLY_TOP_NPC_1;
						Contador++;
					}else if(Contador == 2){
						idNpcTop = OLY_TOP_NPC_2;
						Contador++;
					}else if(Contador == 3){
						idNpcTop = OLY_TOP_NPC_3;
						Contador++;
					}else if(Contador == 4){
						idNpcTop = OLY_TOP_NPC_4;
						Contador++;
					}
					int idClass = rset.getInt(1);
					HERO_MONUMENT.get(idClass).setTop(true, idNpcTop);
				}
				rset.close();
		}catch (Exception e){
				_log.warning("Oly_Monuments Error: Error while Load Top Heroes: " + e.getMessage());
		}		
	}
	
	
	
	private static void loadHeroes(){
		//HEROE_CLASS ID_CLASS, ID_PLAYER
		String Consulta = "SELECT class_id, charId FROM heroes";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement st = con.createStatement();
				ResultSet rset = st.executeQuery(Consulta)){
				while (rset.next()){
					int idClass = rset.getInt(1);
					int idPlayer = rset.getInt(2);
					HERO_MONUMENT.get(idClass).setPlayer(idPlayer, false);
				}
				rset.close();
		}catch (Exception e){
				_log.warning("Oly_Monuments Error: Error while Load Heroes: " + e.getMessage());
		}		
	}
	

	public static void resetAllHeroes(boolean ResetAll, boolean newHeroes){

	}
	
	public static boolean isFromTopMonument(int idNpc){
		if(OLY_TOP_NPC_1 == idNpc){
			return true;
		}else if(OLY_TOP_NPC_2 == idNpc){
			return true;
		}else if(OLY_TOP_NPC_3 == idNpc){
			return true;
		}else if(OLY_TOP_NPC_4 == idNpc){
			return true;
		}
		return false;
	}
	
	private static String getPlace(int idNpc){
		if(OLY_TOP_NPC_1 == idNpc){
			return "1ft Place";
		}else if(OLY_TOP_NPC_2 == idNpc){
			return "2nd Place";
		}else if(OLY_TOP_NPC_3 == idNpc){
			return "3rd Place";
		}else if(OLY_TOP_NPC_4 == idNpc){
			return "4th Place";
		}
		return "";
	}
	
	private static String getClassTitle(L2PcInstance player){
		int idBaseClass = player.getBaseClass();
		String Class = opera.getClassName(idBaseClass);
		return Class;
	}
	
	public static final int getInstanceId(){
		return OLYMPIAD_ZONE_INSTANCE;
	}
	
	private static void createOlympiadZoneInstance(){
		OLYMPIAD_ZONE_INSTANCE = -1;
		try{
			int IdTemp = InstanceManager.getInstance().createDynamicInstance("zeus/olympiad-zone.xml");
			OLYMPIAD_ZONE_INSTANCE = IdTemp;
			Instance instance = InstanceManager.getInstance().getInstance(OLYMPIAD_ZONE_INSTANCE);
			int locCont = 0;
			for(Location tempLoc : instance.getEnterLocs()){
				olyMonument_player_respawn tloc = new olyMonument_player_respawn(tempLoc.getX(),tempLoc.getY(), tempLoc.getZ());
				RESPAWN_PLAYERS.put(locCont, tloc);
				locCont++;
			}
		}catch(Exception a){
			OLYMPIAD_ZONE_INSTANCE = -1;
			_log.warning("ZeuS Error -> olympiad-zone.xml File not Exist("+ a.getMessage() +")");
		}
	}
	
	public static void goToOlympiadZone(L2PcInstance player){
		if(player.getInstanceId() == OLYMPIAD_ZONE_INSTANCE){
			return;
		}
		if(OLYMPIAD_ZONE_INSTANCE>0){
			Random aleatorio = new Random();
			int RandChar = 0;
			if(RESPAWN_PLAYERS.size()>1){
				RandChar = aleatorio.nextInt( RESPAWN_PLAYERS.size() );
			}
			if(RandChar < RESPAWN_PLAYERS.size() || RandChar > RESPAWN_PLAYERS.size()){
				RandChar = 0;
			}
			player.teleToLocation(RESPAWN_PLAYERS.get(RandChar).get_X(), RESPAWN_PLAYERS.get(RandChar).get_Y(), RESPAWN_PLAYERS.get(RandChar).get_Z(), 0, OLYMPIAD_ZONE_INSTANCE, 50);
			
			NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/oly-monument-windows-arrived.html");
			
			html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
			central.sendHtml(player, html.getHtml());
		}else{
			central.msgbox("Sorry. The Instance was not create", player);
		}
	}
	
	private static boolean isTop(L2PcInstance player, int idNpc){
		if(player.isGM()){
			return true;
		}
		if(player.isNoble()){
			if(HERO_MONUMENT!=null){
				if(HERO_MONUMENT.size()>0){
					if(HERO_MONUMENT.containsKey(player.getBaseClass())){
						_monumentData Temp = HERO_MONUMENT.get(player.getBaseClass());
						if((Temp.getIdPlayer() == player.getObjectId()) && Temp.getIsTop() && Temp.getIdNpcTop() == idNpc){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	
	public static boolean isFromMonuments(int IdNpc){
		if(isNpcFromMonument(IdNpc, true)){
			return true;
		}
		return false;
	}
	
	
	private static boolean isNpcOwner(L2PcInstance player, int idNpc){
		if(player.isGM()){
			return true;
		}
		
		if(isFromTopMonument(idNpc)){
			if(isTop(player, idNpc)){
				return true;
			}
		}else{
			if(player.isNoble()){
				if(HERO_MONUMENT != null){
					if(HERO_MONUMENT.size()>0){
						if(HERO_MONUMENT.containsKey(player.getBaseClass())){
							_monumentData Temp = HERO_MONUMENT.get(player.getBaseClass());
							if(Temp.getIdPlayer()  == player.getObjectId() && Temp.getIdNpc() == idNpc){
								return true;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public static void createAndUpdate(L2PcInstance player, int idNpc){
		if(isNpcFromMonument(idNpc,false)){
			int idBaseClass = player.getBaseClass();
			if(!player.isGM() && HERO_MONUMENT.get(idBaseClass).getIdNpc() != idNpc ){
				return;
			}
			String Title = getClassTitle(player);
			fakenpc.getInstance().createFakeClone(player, idNpc, true, Title, true);
		}else{
			fakenpc.getInstance().createFakeClone(player, idNpc, true, getPlace(idNpc), true);
		}
	}
	
	public static void resetMonument(L2PcInstance player, int idNpc){
		fakenpc.getInstance().resetFake(player, idNpc);
	}
	
	public static void showHtmlWindows(L2PcInstance player, int idNpc){
		NpcHtmlMessage html = null;
		String NameChar = "";
		String NameClass = "";
		String ByPassToInstance = "bypass ZeuS oly_monu gotoinstan 0";
		if(isNpcOwner(player, idNpc)){
			String ByPass = "bypass ZeuS oly_monu create " + String.valueOf(idNpc);
			String ByPassReset = "bypass ZeuS oly_monu reset " + String.valueOf(idNpc);			
			html = comun.htmlMaker(player, "./config/zeus/htm/"+ language.getInstance().getFolder(player) + "/oly-monument-windows-main.html");
			html.replace("%BYPASS_CREATE%", ByPass);
			html.replace("%BYPASS_RESET%", ByPassReset);
		}else{
			if(isFromTopMonument(idNpc)){
				int IdClass = getRaceFromNpcId(idNpc);
				if(IdClass>0){
					NameChar = HERO_MONUMENT.get(IdClass).getPlayerName();
					NameClass = HERO_MONUMENT.get(IdClass).getClassName();
				}else{
					NameChar = "No Info";
					NameClass = "No Info";
				}
			}
			html = comun.htmlMaker(player, "./config/zeus/htm/"+ language.getInstance().getFolder(player) +"/oly-monument-windows.html");
		}
		html.replace("%NPC_CLASS%", NameClass);
		html.replace("%PLAYER_NAME%", NameChar);
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%BYPASS_GOTO%", ByPassToInstance);
		opera.enviarHTML(player, html.getHtml());
	}
}

class _monumentData{
	private int ID_NPC_MONUMENT = -1;
	private int ID_NPC_MONUMENT_TOP=-1;
	private int ID_PLAYER = -1;
	private int ID_RACE = -1;
	private boolean IS_TOP = false;
	
	public final String getPlayerName(){
		if(this.ID_PLAYER < 0){
			return "NO ONE";
		}
		return opera.getPlayerNameById(this.ID_PLAYER);
	}
	
	public final String getClassName(){
		if(this.ID_RACE<0){
			return "NO CLASS";
		}
		return opera.getClassName(this.ID_RACE);
	}
	
	public void setTop(boolean isTop, int idNpcTop){
		this.IS_TOP = isTop;
		this.ID_NPC_MONUMENT_TOP = idNpcTop;
	}
	
	public void setPlayer(int idPlayer, boolean isTop){
		this.ID_PLAYER = idPlayer;
		this.IS_TOP = isTop;
	}
	
	public _monumentData(int idNpc, int idRace){
		this.ID_NPC_MONUMENT = idNpc;
		this.ID_RACE = idRace;
	}
	public _monumentData(int idNpc, int idPlayer, int idRace, boolean isTop){
		this.ID_NPC_MONUMENT = idNpc;
		this.ID_PLAYER = idPlayer;
		this.ID_RACE = idRace;
		this.IS_TOP = isTop;
	}
	
	public final int getIdNpcTop(){
		return this.ID_NPC_MONUMENT_TOP;
	}
	
	public final int getIdNpc(){
		return this.ID_NPC_MONUMENT;
	}
	
	public final int getIdPlayer(){
		return this.ID_PLAYER;
	}
	
	public final int getIdRace(){
		return this.ID_RACE;
	}
	
	public final boolean getIsTop(){
		return this.IS_TOP;
	}
	
	public final String getRaceName(){
		return opera.getClassName(this.ID_RACE);
	}
}
class olyMonument_player_respawn{
	private int LOC_X;
	private int LOC_Y;
	private int LOC_Z;
	public olyMonument_player_respawn(int X, int Y, int Z){
		this.LOC_X = X;
		this.LOC_Y = Y;
		this.LOC_Z = Z;
	}
	public final int get_X(){
		return this.LOC_X;
	}
	public final int get_Y(){
		return this.LOC_Y;
	}
	public final int get_Z(){
		return this.LOC_Z;
	}	
}