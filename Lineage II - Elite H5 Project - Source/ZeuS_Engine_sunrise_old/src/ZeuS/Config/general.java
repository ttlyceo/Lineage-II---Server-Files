package ZeuS.Config;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.effects.L2Effect;
import l2r.gameserver.model.items.L2Item;
import ZeuS.Comunidad.Comunidad;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.Region;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.v_AugmentSpecial;
import ZeuS.Comunidad.EngineForm.v_Buffer_New;
import ZeuS.Comunidad.EngineForm.v_Dressme;
import ZeuS.Comunidad.EngineForm.v_RaidBossInfo;
import ZeuS.Comunidad.EngineForm.v_Teleport;
import ZeuS.Comunidad.EngineForm.v_auction_house;
import ZeuS.Comunidad.EngineForm.v_bid_house;
import ZeuS.Comunidad.EngineForm.v_dropsearch;
import ZeuS.Comunidad.EngineForm.v_partymatching;
import ZeuS.Instances.VoteInstance;
import ZeuS.Instances.oly_monument;
import ZeuS.Instances.pvpInstance;
import ZeuS.ZeuS.BD;
import ZeuS.dressme.dressme;
import ZeuS.event.TownWarEvent;
import ZeuS.interfase.ManagerAIONpc;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.antibotSystem;
import ZeuS.interfase.central;
import ZeuS.interfase.colorNameTitle;
import ZeuS.interfase.cumulativeSubclass;
import ZeuS.interfase.custom_drop_system;
import ZeuS.interfase.potionSystem;
import ZeuS.interfase.sellBuff;
import ZeuS.interfase.votereward;
import ZeuS.interfase.wishList;
import ZeuS.language.language;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.adminHandler;
import ZeuS.procedimientos.handler;
import ZeuS.procedimientos.jMail;
import ZeuS.procedimientos.opera;
import ZeuS.server.httpResp;

public final class general {
	private static boolean _isValid=false;
	private static Map<Integer, _classesinfo> CLASS_LIST = new HashMap<Integer, _classesinfo>();
	private static String SCRIPT_BORRADO ="DELETE FROM zeus_connection WHERE NOW() > DATE_ADD(zeus_connection.date,INTERVAL 10 DAY)";

	public static int ID_LOGO_SERVER = 130;
	public static final String DIR_IMAGES = "./config/zeus/images/";
	public static final String DIR_HTML = "./config/zeus/htm/";
	
	private static Map<Integer, Boolean>IsSummonWisp = new HashMap<Integer, Boolean>();
	public static Map<Integer, _itemInfo> ITEM_FOR_SEARCH = new HashMap<Integer, _itemInfo>();
	
	private static Vector<_topplayer> TOP_PLAYERS = new Vector<_topplayer>();
	
	public static void GmSummonWispChange(L2PcInstance player){
		IsSummonWisp.put(player.getObjectId(), !IsSummonWisp.get(player.getObjectId()) );
	}
	
	public static _classesinfo getClassInfo(int IdClass){
		return CLASS_LIST.get(IdClass);
	}
	public static Map<Integer, _classesinfo> getClassInfo(){
		return CLASS_LIST;
	}
	
	public static boolean isGmSummonWisp(L2PcInstance player){
		try{
			return IsSummonWisp.get(player.getObjectId());
		}catch(Exception a){
			IsSummonWisp.put(player.getObjectId(), false);
		}
		return false;
	}
	
	public static void GmSummonWisp(L2PcInstance PlayerGM, L2PcInstance Player){
		try{
			if(PlayerGM.isGM()){
				if(!IsSummonWisp.containsKey(PlayerGM.getObjectId())){
					IsSummonWisp.put(PlayerGM.getObjectId(), false);
				}
			}
		}catch(Exception a){
			IsSummonWisp.put(PlayerGM.getObjectId(), false);
		}
		if(PlayerGM.isGM()){
			if(IsSummonWisp.get(PlayerGM.getObjectId())){
				Location LocGM = PlayerGM.getLocation();
				Player.teleToLocation(LocGM, true);
			}
		}
	}
	
	private static Map<Integer,premiumsystem> PREMIUM_SERVICES = new HashMap<Integer, premiumsystem>();
	private static Map<String,premiumPersonalData> PREMIUM_SERVICES_CLAN_CHAR = new HashMap<String, premiumPersonalData>();
	
	
	public static boolean BUFFER_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM;
	public static boolean SHOP_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM;
	public static boolean SHOW_SERVER_STATUS_TO_THE_PLAYER;
	
	
	private static Map<Integer, String> NPC_ID_TO_COMMUNITY = new HashMap<Integer, String>();
	public static int ZEUS_ID_NPC_EXCLUSIVE_SHOP;
	public static int ZEUS_ID_NPC_INSTANCE_PVP;
	public static int ZEUS_ID_NPC_BETA;
	public static Map<Integer, String> getnpcIdToCommunity(){
		return NPC_ID_TO_COMMUNITY;
	}
	
	
	private static Map<Integer,levelupspot> LEVEL_UP_SPOT_WINDOWS = new HashMap<Integer, levelupspot>();
	public static boolean LEVEL_UP_SPOT;
	
	public static levelupspot getLevelUpSpotInfo(int level){
		try{
			return LEVEL_UP_SPOT_WINDOWS.get(level);
		}catch(Exception a){
			
		}
		return null;
	}
	
	public static Map<Integer, Advclassmaster> ADVANCED_CLASS_MASTER = new HashMap<Integer, Advclassmaster>();
	
	public static Advclassmaster getAdvClassMasterData(int idIndex){
		try{
			return ADVANCED_CLASS_MASTER.get(idIndex);
		}catch(Exception a){
			
		}
		return null;
	}
	
	private static Map<Integer,buffcommunitySchemme> BUFF_COMMUNITY_SCHEMME = new HashMap<Integer,buffcommunitySchemme>();
	private static Map<Integer, buffcommunity> BUFF_COMMUNITY_CATEGORIA = new HashMap<Integer,buffcommunity>();
	private static Vector<buffcommunity> ALL_BUFF_COMMUNITY = new Vector<buffcommunity>();
	private static Vector<Integer> BUFF_SINGLE_ALL = new Vector<Integer>();
	
		public static boolean isBuffInZeuS(int idBuff){
				try{
					return BUFF_SINGLE_ALL.contains(idBuff);
				}catch(Exception a){
					
				}
				return false;
			}
	
	public static buffcommunity getBuffCommnunity_individual(int idBuff){
		for(buffcommunity T1 : getBuffCommnunity_individual()){
			if(T1.IdIterno() == idBuff){
				return T1;
			}
		}
		return null;
	}
	
	
	public static Map<Integer,buffcommunitySchemme> getBuffCommnunitySchemeReady(){
		return BUFF_COMMUNITY_SCHEMME;
	}
	
	public static Map<Integer, buffcommunity> getBuffCommnunityCategorias(){
		return BUFF_COMMUNITY_CATEGORIA;
	}
	
	
	public static void setNewPremiumPersonalData(String DataToSave, premiumPersonalData Pm_data){
		if(PREMIUM_SERVICES_CLAN_CHAR!=null){
			if(PREMIUM_SERVICES_CLAN_CHAR.containsKey(DataToSave)){
				PREMIUM_SERVICES_CLAN_CHAR.remove(DataToSave);
			}
		}
		PREMIUM_SERVICES_CLAN_CHAR.put(DataToSave, Pm_data);
	}
	
	public static Vector<Integer> COMMUNITY_MAIN_ACCESS = new Vector<Integer>();
	
	public static boolean SECONDAY_PASSWORD;
	public static boolean SECONDARY_PASSWORD_NEED_EMAIL;
	public static boolean SECONDARY_PASSWORD_BLOCK_CHAR;
	public static String SECONDARY_PASSWORD_TEMPLATE;
	public static int SECONDARY_PASSWORD_MIN_LENGHT;
	public static int SECONDARY_PASSWORD_MAX_LENGHT;
	
	public static boolean SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE;
	
	public static Map<Integer, Long>_byPassVoice = new HashMap<Integer, Long>();
	
	private static Map<Integer, Boolean> BLOCKSAVEBD = new HashMap<Integer, Boolean>();
	
	public static void setStatusCanSave(L2PcInstance player, boolean b){
		BLOCKSAVEBD.put(player.getObjectId(), b);
	}
	
	public static boolean canSaveInBD(L2PcInstance player){
		if(BLOCKSAVEBD!=null){
			if(BLOCKSAVEBD.containsKey(player.getObjectId())){
				return BLOCKSAVEBD.get(player.getObjectId()); 
			}
		}
		BLOCKSAVEBD.put(player.getObjectId(), true);
		return true;
	}
	
	
	private static boolean LOAD_START = false;
	private static boolean LOAD_START_ONE_TIME = false;
	private static Map<String, _charinfo> CHAR_INFO = new HashMap<String, _charinfo>();
	private static Map<String, Integer>RACE_COUNT = new HashMap<String, Integer>();
	
	private static Vector<Integer> TopSearch_Item = new Vector<Integer>();
	private static Vector<Integer> TopSearch_Monster = new Vector<Integer>();
	private static Map<String, HashMap<Integer, HashMap<Integer, String>>> HeroesList = new HashMap<String, HashMap<Integer, HashMap<Integer, String>>>();
	
	//private static FastMap<Integer, FastMap<Integer, Map<Integer, String>>> TopPlayersToShow = new FastMap<Integer, Map<Integer, Map<Integer, String>>>();
	
	private static String TITULO_ENGINE = "Engine";
	
	public static String STAFF_DATA;
	
	public static String getTituloEngine(){
		return TITULO_ENGINE;
	}
	

	protected static String HOST_ ="";
	protected static String USER_ ="";
	
	private static String BeginTime = "";
	private static int unixTimeBeginServer = 0;

	private static int CB_IDObjectMainZeuSNPC = 0;

	public static String getHost(){
		return HOST_;
	}

	public static String getUser(){
		return USER_;
	}

	public static boolean _activated(){
		return _isValid;
	}

	protected static void checkValidate(){
		_isValid = true;
		return;
	}

	protected static int[] AccessConfig;



	public static final Logger _log = Logger.getLogger(general.class.getName());
	private static final String ZEUS_CONFIG = "./config/zeus/zeus.properties";
	private static final String ZEUS_PAYPAL_CONFIG = "./config/zeus/zeus_paypal.properties";
	private static final String ZEUS_CONFIG_NPC_COMMUNITY = "./config/zeus/zeus_community_npc.properties";
	private static final String ZEUS_DONATION_CONFIG = "./config/zeus/zeus_donation.properties";
	@SuppressWarnings("unused")
	private static final String ZEUS_CONFIG_LEN = "./config/zeus/zeus_leng.txt";
	public static int ServerStartUnixTime = 0;

	public static final String VERSION = "2017-0.1";
	public static final String EMAIL = "adm.jabberwock@gmail.com";
	
	private static Map<Integer, HashMap<Integer, HashMap<String, String>>> LastAccess = new HashMap<Integer, HashMap<Integer, HashMap<String,String>>>();


	public static int PLAYER_BASE_TO_SHOW = 0;

	public static boolean DEBUG_CONSOLA_ENTRADAS;
	public static boolean DEBUG_CONSOLA_ENTRADAS_TO_USER;

	public static boolean onLine = false;

	public static final int NOBLESS_TIARA = 7694;

	public static boolean FREE_TELEPORT;
	public static String TELEPORT_PRICE;
	public static Boolean TELEPORT_BD;
	public static Boolean TELEPORT_CAN_USE_IN_COMBAT_MODE;
	public static Boolean TELEPORT_FOR_FREE_UP_TO_LEVEL;
	public static int TELEPORT_FOR_FREE_UP_TO_LEVEL_LV;

	public static boolean DROP_SEARCH_TELEPORT_FOR_FREE;
	public static boolean DROP_SEARCH_OBSERVE_MODE;
	public static boolean DROP_SEARCH_SHOW_IDITEM_TO_PLAYER;
	public static String DROP_TELEPORT_COST;
	public static int DROP_SEARCH_MOSTRAR_LISTA;
	public static Vector<Integer> DROP_SEARCH_MOB_BLOCK_TELEPORT = new Vector<Integer>();
	public static boolean DROP_SEARCH_CAN_USE_TELEPORT;
	public static boolean DROP_SEARCH_SHIFT_NPC_SHOW;

	public static String Server_Name = "";

	public static int RADIO_PLAYER_NPC_MAXIMO;
	
	
	public static Vector<Integer> BUFF_STORE_BUFFPROHIBITED = new Vector<Integer>();
	public static Vector<String> BUFFSTORE_ITEMS_REQUEST = new Vector<String>();
	public static Vector<String> ACCOUNT_SELL_ITEMS_REQUEST = new Vector<String>();
	
	
	public static Vector<String> AUCTIONSHOUSE_ITEM_REQUEST = new Vector<String>();
	public static Vector<String> BIDHOUSE_ITEM_REQUEST = new Vector<String>();
	
	public static int AUCTIONSHOUSE_PERCENT_FEED;
	public static int AUCTIONSHOUSE_FEED_MASTER;
	public static boolean AUCTIONSHOUSE_ONLY_IN_PEACE_ZONE;
	public static int BIDHOUSE_PERCENT_FEED;
	public static int BIDHOUSE_FEED_MASTER;	
	public static int BIDHOUSE_DAYS;
	public static int BIDHOUSE_CANCEL_TAX_FOR_SELLER;
	public static int BIDHOUSE_CANCEL_TAX_FOR_BUYER;
	public static boolean BIDHOUSE_ONLY_IN_PEACE_ZONE;
	
	public static int TELEPORT_SECONDS_SKILL_TO_GO;
	public static String AIO_PREFIX;
	public static boolean ALLOW_BUFFSTORE;
	public static boolean CAN_USE_SHOP_ONLY_IN_PEACE_ZONE;

	//public static Map<Integer, HashMap<String, String>> TELEPORT_DATA = new HashMap<Integer, HashMap<String, String>>();
	public static Map<Integer, HashMap<String, String>> SHOP_DATA = new HashMap<Integer, HashMap<String, String>>();	
	public static Map<Integer, Boolean> isSellMerchant = new HashMap<Integer, Boolean>();
	public static Map<Integer, Boolean> IS_USING_NPC = new HashMap<Integer, Boolean>();
	public static Map<Integer, Boolean> IS_USING_CB = new HashMap<Integer, Boolean>();

	public static String PARTY_FINDER_PRICE;
	public static boolean PARTY_FINDER_GO_LEADER_NOBLE;
	public static boolean PARTY_FINDER_GO_LEADER_DEATH;
	public static boolean PARTY_FINDER_GO_LEADER_FLAGPK;
	public static boolean PARTY_FINDER_CAN_USE_FLAG;
	public static boolean PARTY_FINDER_CAN_USE_PK;
	public static int PARTY_FINDER_CAN_USE_LVL;
	public static boolean PARTY_FINDER_USE_NO_SUMMON_RULEZ;
	public static boolean PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE;
	public static boolean PARTY_FINDER_GO_LEADER_ON_ASEDIO;
	public static boolean PARTY_FINDER_CAN_USE_ONLY_NOBLE;

	public static boolean FLAG_FINDER_CAN_USE_FLAG;
	public static boolean FLAG_FINDER_CAN_USE_PK;
	public static boolean FLAG_FINDER_CAN_NOBLE;
	public static String FLAG_FINDER_PRICE;
	public static int FLAG_FINDER_LVL;
	public static int FLAG_PVP_PK_LVL_MIN;
	public static int FLAG_FINDER_MIN_PVP_FROM_TARGET;
	public static boolean FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE;
	public static boolean FLAG_FINDER_PK_PRIORITY;
	public static boolean FLAG_FINDER_CHECK_CLAN;

	public static String PINTAR_PRICE;
	public static String PINTAR_MATRIZ;

	public static String DELEVEL_PRICE;
	public static int DELEVEL_LVL_MAX;
	public static boolean DELEVEL_NOBLE;
	public static boolean DELEVEL_CHECK_SKILL;
	//public static boolean DELEVEL;

	public static boolean DRESSME_CAN_USE_IN_OLYS;
	public static boolean DRESSME_CAN_CHANGE_IN_OLYS;
	public static boolean DRESSME_NEW_DRESS_IS_FREE;
	public static String DRESSME_NEW_DRESS_COST;
	
	public static boolean JAIL_BAIL_STATUS;
	public static String JAIL_BAIL_COST;
	public static int JAIL_BAIL_MULTIPLE_COST;
	public static boolean JAIL_BAIL_BLACKLIST_MULTIPLE;
	public static boolean PVP_COUNT_ALLOW_SIEGES_FORTRESS;
	
	public static String AUGMENT_ITEM_PRICE;
	public static int AUGMENT_SPECIAL_x_PAGINA;
	public static boolean AUGMENT_SPECIAL_NOBLE;
	public static int AUGMENT_SPECIAL_LVL;
	public static String AUGMENT_SPECIAL_PRICE;
	public static String AUGMENT_SPECIAL_PRICE_ACTIVE;
	public static String AUGMENT_SPECIAL_PRICE_PASSIVE;
	public static String AUGMENT_SPECIAL_PRICE_CHANCE;
	public static String AUGMENT_SPECIAL_PRICE_SYMBOL;
	
	public static int DONA_ID_ITEM;
	public static String LOGINSERVERNAME;
	public static boolean USE_AUTOMATIC_HTML;

	public static String VOTO_REWARD_TOPZONE;
	public static String VOTO_REWARD_HOPZONE;
	public static boolean VOTO_REWARD_ACTIVO_TOPZONE;
	public static boolean VOTO_REWARD_ACTIVO_HOPZONE;
	public static int VOTO_REWARD_SEG_ESPERA;
	public static String VOTO_ITEM_BUFF_ENCHANT_PRICE;

	public static int VOTE_ATTEMPTS_ALLOWED = 0;
	public static String VOTO_HOPZONE_IDENTIFICACION = "";
	public static String VOTO_TOPZONE_IDENTIFICACION = "";
	

	public static boolean VOTO_AUTOREWARD;
	public static int VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK;
	public static int VOTO_REWARD_AUTO_RANGO_PREMIAR;
	public static String VOTO_REWARD_AUTO_MENSAJE_FALTA;
	public static String VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA;
	public static String VOTO_REWARD_AUTO_REWARD_META_HOPZONE;
	public static String VOTO_REWARD_AUTO_REWARD_META_TOPZONE;
	public static boolean VOTO_REWARD_AUTO_AFK_CHECK;

	public static boolean ANTI_OVER_ENCHANT_ACT;
	public static boolean ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK;
	public static int ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK;
	public static String ANTI_OVER_ENCHANT_MESJ_PUNISH;
	public static String ANTI_OVER_TYPE_PUNISH;
	public static String[] _ANTI_OVER_TYPE_PUNISH = {"JAIL","BAN_CHAR","BAN_ACC"};
	public static boolean ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL;
	public static boolean ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP;
	public static int ANTI_OVER_ENCHANT_MAX_WEAPON;
	public static int ANTI_OVER_ENCHANT_MAX_ARMOR;


	public static boolean ENCHANT_NOBLE;
	public static int ENCHANT_LVL;
	public static String ENCHANT_ITEM_PRICE;
	public static int ENCHANT_MIN_ENCHANT;
	public static int ENCHANT_MAX_ENCHANT;
	public static int ENCHANT_x_VEZ;

	public static boolean ELEMENTAL_NOBLE;
	public static int ELEMENTAL_LVL;
	public static String ELEMENTAL_ITEM_PRICE;
	public static int ELEMENTAL_LVL_ENCHANT_MAX_WEAPON;
	public static int ELEMENTAL_LVL_ENCHANT_MAX_ARMOR;


	public static String OPCIONES_CHAR_SEXO_ITEM_PRICE;
	public static String OPCIONES_CHAR_NOBLE_ITEM_PRICE;
	public static String OPCIONES_CHAR_LVL85_ITEM_PRICE;
	public static String OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE;
	public static String OPCIONES_CHAR_BUFFER_AIO_PRICE;
	public static String OPCIONES_CHAR_BUFFER_AIO_PRICE_30;
	public static int OPCIONES_CHAR_BUFFER_AIO_LVL;
	public static boolean OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE;
	public static int OPCIONES_CHAR_CAMBIO_NOMBRE_LVL;
	public static boolean OPCIONES_CHAR_CAMBIO_NOMBRE;
	public static boolean OPCIONES_CLAN_CAMBIO_NOMBRE;
	public static boolean OPCIONES_CHAR_BUFFER_AIO_30;
	
	public static int MAX_IP_COUNT;
	public static boolean MAX_IP_CHECK;
	public static boolean MAX_IP_RECORD_DATA;
	public static int MAX_IP_VIP_COUNT;


	public static boolean OPCIONES_CHAR_SEXO;
	public static boolean OPCIONES_CHAR_NOBLE;
	public static boolean OPCIONES_CHAR_LVL85;
	public static boolean OPCIONES_CHAR_BUFFER_AIO;
	public static boolean OPCIONES_CHAR_FAME;
	public static boolean OPCIONES_CHAR_DELEVEL;

	public static String OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE;
	public static int OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL;

	public static String OPCIONES_CHAR_FAME_PRICE;
	public static boolean OPCIONES_CHAR_FAME_NOBLE;
	public static int OPCIONES_CHAR_FAME_LVL;
	public static int OPCIONES_CHAR_FAME_GIVE;
	
	public static String OPCIONES_CHAR_REDUCE_PK_PRICE;
	public static boolean OPCIONES_CHAR_REDUCE_PK;
	public static int OPCIONES_CHAR_REDUCE_AMOUNT;

	public static int RAIDBOSS_INFO_LISTA_X_HOJA;
	public static boolean RAIDBOSS_INFO_TELEPORT;
	public static String RAIDBOSS_INFO_TELEPORT_PRICE;
	public static boolean RAIDBOSS_INFO_NOBLE;
	public static int RAIDBOSS_INFO_LVL;
	public static Vector<Integer> RAIDBOSS_ID_MOB_NO_TELEPORT = new Vector<Integer>();
	public static boolean RAIDBOSS_OBSERVE_MODE;

	public static boolean RETURN_BUFF;
	public static int RETURN_BUFF_SECONDS_TO_RETURN;
	public static boolean RETURN_BUFF_IN_OLY;
	public static int RETURN_BUFF_IN_OLY_SECONDS_TO_RETURN;
	public static Vector<Integer> RETURN_BUFF_NOT_STEALING = new Vector<Integer>();


	public static String GET_NAME_VAR_TYPE;
	public static String GET_NAME_VAR_EMAIL;
	public static String GET_NAME_VAR_CODE;
	public static String GET_NAME_VAR_DIR_WEB;
	public static String GET_NAME_VAR_IDDONACION;
	public static String GET_NAME_VAR_SERVER_ID;	
	public static String WEB_HOP_ZONE_SERVER;
	public static String WEB_TOP_ZONE_SERVER;
	
	public static String PAYPAL_LINK_WEB;
	public static String PAYPAL_LINK_EMAIL;
	public static int PAYPAL_SECONDS_WAITING_TIME;
	public static String PAYPAL_LINK_USE = "WEB";


	public static Map<L2PcInstance, List<L2Effect>> BUFF_REMOVED = new HashMap<L2PcInstance, List<L2Effect>>();
	public static Map<L2PcInstance, Boolean> CANCEL_TASK = new HashMap<L2PcInstance, Boolean>();

	public static void resetBuffRemoved(L2PcInstance player){
		if(BUFF_REMOVED.containsKey(player)){
			BUFF_REMOVED.remove(player);
			CANCEL_TASK.put(player, false);
		}
	}


	public static boolean TRADE_WHILE_FLAG;
	public static boolean TRADE_WHILE_PK;

	public static boolean BTN_SHOW_VOTE;
	public static boolean BTN_SHOW_BUFFER;
	public static boolean BTN_SHOW_TELEPORT;
	public static boolean BTN_SHOW_SHOP;
	public static boolean BTN_SHOW_WAREHOUSE;
	public static boolean BTN_SHOW_AUGMENT;
	public static boolean BTN_SHOW_SUBCLASES;
	public static boolean BTN_SHOW_CLASS_TRANSFER;
	public static boolean BTN_SHOW_CONFIG_PANEL;
	public static boolean BTN_SHOW_DROP_SEARCH;
	public static boolean BTN_SHOW_PVPPK_LIST;
	public static boolean BTN_SHOW_LOG_PELEAS;
	public static boolean BTN_SHOW_CASTLE_MANAGER;
	public static boolean BTN_SHOW_DESAFIO;
	public static boolean BTN_SHOW_SYMBOL_MARKET;
	public static boolean BTN_SHOW_CLANALLY;
	public static boolean BTN_SHOW_PARTYFINDER;
	public static boolean BTN_SHOW_FLAGFINDER;
	public static boolean BTN_SHOW_COLORNAME;
	public static boolean BTN_SHOW_DELEVEL;
	public static boolean BTN_SHOW_REMOVE_ATRIBUTE;
	public static boolean BTN_SHOW_BUG_REPORT;
	public static boolean BTN_SHOW_DONATION;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_PJ;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_CLAN;
	public static boolean BTN_SHOW_VARIAS_OPCIONES;
	public static boolean BTN_SHOW_ELEMENT_ENHANCED;
	public static boolean BTN_SHOW_ENCANTAMIENTO_ITEM;
	public static boolean BTN_SHOW_AUGMENT_SPECIAL;
	public static boolean BTN_SHOW_GRAND_BOSS_STATUS;
	public static boolean BTN_SHOW_RAIDBOSS_INFO;
	public static boolean BTN_SHOW_TRANSFORMATION;


	public static boolean BTN_SHOW_VOTE_CH;
	public static boolean BTN_SHOW_BUFFER_CH;
	public static boolean BTN_SHOW_TELEPORT_CH;
	public static boolean BTN_SHOW_SHOP_CH;
	public static boolean BTN_SHOW_WAREHOUSE_CH;
	public static boolean BTN_SHOW_AUGMENT_CH;
	public static boolean BTN_SHOW_SUBCLASES_CH;
	public static boolean BTN_SHOW_CLASS_TRANSFER_CH;
	public static boolean BTN_SHOW_CONFIG_PANEL_CH;
	public static boolean BTN_SHOW_DROP_SEARCH_CH;
	public static boolean BTN_SHOW_PVPPK_LIST_CH;
	public static boolean BTN_SHOW_LOG_PELEAS_CH;
	public static boolean BTN_SHOW_CASTLE_MANAGER_CH;
	public static boolean BTN_SHOW_DESAFIO_CH;
	public static boolean BTN_SHOW_SYMBOL_MARKET_CH;
	public static boolean BTN_SHOW_CLANALLY_CH;
	public static boolean BTN_SHOW_PARTYFINDER_CH;
	public static boolean BTN_SHOW_FLAGFINDER_CH;
	public static boolean BTN_SHOW_COLORNAME_CH;
	public static boolean BTN_SHOW_DELEVEL_CH;
	public static boolean BTN_SHOW_REMOVE_ATRIBUTE_CH;
	public static boolean BTN_SHOW_BUG_REPORT_CH;
	public static boolean BTN_SHOW_DONATION_CH;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_PJ_CH;
	public static boolean BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH;
	public static boolean BTN_SHOW_VARIAS_OPCIONES_CH;
	public static boolean BTN_SHOW_ELEMENT_ENHANCED_CH;
	public static boolean BTN_SHOW_ENCANTAMIENTO_ITEM_CH;
	public static boolean BTN_SHOW_AUGMENT_SPECIAL_CH;
	public static boolean BTN_SHOW_GRAND_BOSS_STATUS_CH;
	public static boolean BTN_SHOW_RAIDBOSS_INFO_CH;
	public static boolean BTN_SHOW_TRANSFORMATION_CH;


	public static boolean _BTN_SHOW_VOTE_CBE;
	public static boolean _BTN_SHOW_BUFFER_CBE;
	public static boolean _BTN_SHOW_TELEPORT_CBE;
	public static boolean _BTN_SHOW_SHOP_CBE;
	public static boolean _BTN_SHOW_WAREHOUSE_CBE;
	public static boolean _BTN_SHOW_AUGMENT_CBE;
	public static boolean _BTN_SHOW_SUBCLASES_CBE;
	public static boolean _BTN_SHOW_CLASS_TRANSFER_CBE;
	public static boolean _BTN_SHOW_CONFIG_PANEL_CBE;
	public static boolean _BTN_SHOW_DROP_SEARCH_CBE;
	public static boolean _BTN_SHOW_PVPPK_LIST_CBE;
	public static boolean _BTN_SHOW_LOG_PELEAS_CBE;
	public static boolean _BTN_SHOW_CASTLE_MANAGER_CBE;
	public static boolean _BTN_SHOW_DRESSME;
	public static boolean _BTN_SHOW_DESAFIO_CBE;
	public static boolean _BTN_SHOW_SYMBOL_MARKET_CBE;
	public static boolean _BTN_SHOW_CLANALLY_CBE;
	public static boolean _BTN_SHOW_PARTYFINDER_CBE;
	public static boolean _BTN_SHOW_FLAGFINDER_CBE;
	public static boolean _BTN_SHOW_COLORNAME_CBE;
	public static boolean _BTN_SHOW_DELEVEL_CBE;
	public static boolean _BTN_SHOW_REMOVE_ATRIBUTE_CBE;
	public static boolean _BTN_SHOW_BUG_REPORT_CBE;
	public static boolean _BTN_SHOW_DONATION_CBE;
	public static boolean _BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE;
	public static boolean _BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE;
	public static boolean _BTN_SHOW_VARIAS_OPCIONES_CBE;
	public static boolean _BTN_SHOW_ELEMENT_ENHANCED_CBE;
	public static boolean _BTN_SHOW_ENCANTAMIENTO_ITEM_CBE;
	public static boolean _BTN_SHOW_AUGMENT_SPECIAL_CBE;
	public static boolean _BTN_SHOW_GRAND_BOSS_STATUS_CBE;
	public static boolean _BTN_SHOW_RAIDBOSS_INFO_CBE;
	public static boolean _BTN_SHOW_TRANSFORMATION_CBE;
	public static boolean _BTN_SHOW_BLACKSMITH_CBE;
	public static boolean _BTN_SHOW_PARTYMATCHING_CBE;
	public static boolean _BTN_SHOW_AUCTIONHOUSE_CBE;
	public static boolean _BTN_SHOW_BIDHOUSE_CBE;

	public static boolean STATUS_BUFFER;
	public static boolean STATUS_TELEPORT;
	public static boolean STATUS_SHOP;
	public static boolean STATUS_WAREHOUSE;
	public static boolean STATUS_AUGMENT;
	public static boolean STATUS_SUBCLASES;
	public static boolean STATUS_CLASS_TRANSFER;
	public static boolean STATUS_CONFIG_PANEL;
	public static boolean STATUS_DROP_SEARCH;
	public static boolean STATUS_PVPPK_LIST;
	public static boolean STATUS_LOG_PELEAS;
	public static boolean STATUS_CASTLE_MANAGER;
	public static boolean STATUS_SYMBOL_MARKET;
	public static boolean STATUS_PARTYFINDER;
	public static boolean STATUS_FLAGFINDER;
	public static boolean STATUS_DELEVEL;
	public static boolean STATUS_REMOVE_ATRIBUTE;
	public static boolean STATUS_BUG_REPORT;
	public static boolean STATUS_VARIAS_OPCIONES;
	public static boolean STATUS_ELEMENT_ENHANCED;
	public static boolean STATUS_ENCANTAMIENTO_ITEM;
	public static boolean STATUS_AUGMENT_SPECIAL;
	public static boolean STATUS_GRAND_BOSS_STATUS;
	public static boolean STATUS_RAIDBOSS_INFO;
	public static boolean STATUS_TRANSFORMACION;
	public static boolean STATUS_BLACKSMITH;
	public static boolean STATUS_PARTYMATCHING;
	public static boolean STATUS_AUCTIONHOUSE;
	public static boolean STATUS_BIDHOUSE;
	public static boolean STATUS_DRESSME;

	public static String BTN_SHOW_EXPLICA_VOTE;
	public static String BTN_SHOW_EXPLICA_BUFFER;
	public static String BTN_SHOW_EXPLICA_TELEPORT;
	public static String BTN_SHOW_EXPLICA_SHOP;
	public static String BTN_SHOW_EXPLICA_WAREHOUSE;
	public static String BTN_SHOW_EXPLICA_AUGMENT;
	public static String BTN_SHOW_EXPLICA_SUBCLASES;
	public static String BTN_SHOW_EXPLICA_CLASS_TRANSFER;
	public static String BTN_SHOW_EXPLICA_CONFIG_PANEL;
	public static String BTN_SHOW_EXPLICA_DROP_SEARCH;
	public static String BTN_SHOW_EXPLICA_PVPPK_LIST;
	public static String BTN_SHOW_EXPLICA_LOG_PELEAS;
	public static String BTN_SHOW_EXPLICA_CASTLE_MANAGER;
	public static String BTN_SHOW_EXPLICA_DESAFIO;
	public static String BTN_SHOW_EXPLICA_SYMBOL_MARKET;
	public static String BTN_SHOW_EXPLICA_CLANALLY;
	public static String BTN_SHOW_EXPLICA_PARTYFINDER;
	public static String BTN_SHOW_EXPLICA_FLAGFINDER;
	public static String BTN_SHOW_EXPLICA_COLORNAME;
	public static String BTN_SHOW_EXPLICA_DELEVEL;
	public static String BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE;
	public static String BTN_SHOW_EXPLICA_BUG_REPORT;
	public static String BTN_SHOW_EXPLICA_DONATION;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN;
	public static String BTN_SHOW_EXPLICA_VARIAS_OPCIONES;
	public static String BTN_SHOW_EXPLICA_ELEMENT_ENHANCED;
	public static String BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM;
	public static String BTN_SHOW_EXPLICA_AUGMENT_SPECIAL;
	public static String BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS;
	public static String BTN_SHOW_EXPLICA_RAIDBOSS_INFO;
	public static String BTN_SHOW_EXPLICA_TRANSFORMATION;
	public static String BTN_SHOW_EXPLICA_VOTE_CB;
	public static String BTN_SHOW_EXPLICA_BUFFER_CB;
	public static String BTN_SHOW_EXPLICA_TELEPORT_CB;
	public static String BTN_SHOW_EXPLICA_SHOP_CB;
	public static String BTN_SHOW_EXPLICA_WAREHOUSE_CB;
	public static String BTN_SHOW_EXPLICA_AUGMENT_CB;
	public static String BTN_SHOW_EXPLICA_SUBCLASES_CB;
	public static String BTN_SHOW_EXPLICA_CLASS_TRANSFER_CB;
	public static String BTN_SHOW_EXPLICA_CONFIG_PANEL_CB;
	public static String BTN_SHOW_EXPLICA_DROP_SEARCH_CB;
	public static String BTN_SHOW_EXPLICA_PVPPK_LIST_CB;
	public static String BTN_SHOW_EXPLICA_LOG_PELEAS_CB;
	public static String BTN_SHOW_EXPLICA_CASTLE_MANAGER_CB;
	public static String BTN_SHOW_EXPLICA_DESAFIO_CB;
	public static String BTN_SHOW_EXPLICA_SYMBOL_MARKET_CB;
	public static String BTN_SHOW_EXPLICA_CLANALLY_CB;
	public static String BTN_SHOW_EXPLICA_PARTYFINDER_CB;
	public static String BTN_SHOW_EXPLICA_FLAGFINDER_CB;
	public static String BTN_SHOW_EXPLICA_COLORNAME_CB;
	public static String BTN_SHOW_EXPLICA_DELEVEL_CB;
	public static String BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE_CB;
	public static String BTN_SHOW_EXPLICA_BUG_REPORT_CB;
	public static String BTN_SHOW_EXPLICA_DONATION_CB;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ_CB;
	public static String BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN_CB;
	public static String BTN_SHOW_EXPLICA_VARIAS_OPCIONES_CB;
	public static String BTN_SHOW_EXPLICA_ELEMENT_ENHANCED_CB;
	public static String BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM_CB;
	public static String BTN_SHOW_EXPLICA_AUGMENT_SPECIAL_CB;
	public static String BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS_CB;
	public static String BTN_SHOW_EXPLICA_RAIDBOSS_INFO_CB;
	public static String BTN_SHOW_EXPLICA_TRANSFORMATION_CB;
	public static String BTN_SHOW_EXPLICA_PARTYMATCHING_CB;
	public static String BTN_SHOW_EXPLICA_AUCTIONSHOUSE_CB;
	public static String BTN_SHOW_EXPLICA_BIDSHOUSE_CB;

	/**/
	public static String DESAFIO_85_PREMIO;
	public static String DESAFIO_NOBLE_PREMIO;
	public static Vector<Integer> DESAFIO_NPC_BUSQUEDAS = new Vector<Integer>();
	public static int DESAFIO_MAX_LVL85;
	public static int DESAFIO_MAX_NOBLE;
	public static boolean DESAFIO_LVL85;
	public static boolean DESAFIO_NOBLE;
	public static boolean DESAFIO_NPC;

	public static boolean VOTO_SERVER_HOPZONE;
	public static boolean VOTO_SERVER_TOPZONE;



	public static boolean GIRAN;
	public static boolean ADEN;
	public static boolean RUNE;
	public static boolean OREN;
	public static boolean DION;
	public static boolean GLUDIO;
	public static boolean GODDARD;
	public static boolean SCHUTTGART;
	public static boolean INNADRIL;

		/*ZeuS SERVER*/
		
		public static boolean CHAT_GENERAL_BLOCK;
		public static boolean CHAT_SHOUT_BLOCK;
		public static boolean CHAT_TRADE_BLOCK;
		public static boolean CHAT_WISP_BLOCK;
		
		public static int CHAT_GENERAL_NEED_PVP;
		public static int CHAT_GENERAL_NEED_LEVEL;
		public static int CHAT_GENERAL_NEED_LIFETIME;
		
		public static int CHAT_SHOUT_NEED_PVP;
		public static int CHAT_SHOUT_NEED_LEVEL;
		public static int CHAT_SHOUT_NEED_LIFETIME;
		
		public static int CHAT_TRADE_NEED_PVP;
		public static int CHAT_TRADE_NEED_LEVEL;
		public static int CHAT_TRADE_NEED_LIFETIME;

		public static int CHAT_WISP_NEED_PVP;
		public static int CHAT_WISP_NEED_LEVEL;
		public static int CHAT_WISP_NEED_LIFETIME;
		
		
		public static boolean PVP_COLOR_SYSTEM_ENABLED;
		public static boolean PK_COLOR_SYSTEM_ENABLED;
		public static boolean ANNOUCE_RAID_BOS_STATUS;

		public static boolean SPREE_SYSTEM;
		public static Map<Integer, String> SPREE_PVP_SYSTEM_MESSAGE = new HashMap<Integer, String>();
		public static Map<Integer, String> SPREE_PK_SYSTEM_MESSAGE = new HashMap<Integer, String>();

		public static boolean ANNOUCE_TOP_PPVPPK_ENTER;
		public static boolean ANNOUCE_PJ_KARMA;
		public static boolean ANNOUCE_CLASS_OPONENT_OLY;

		public static boolean ALLOW_BLESSED_ESCAPE_PVP;
		public static boolean CAN_USE_BSOE_PK;
		public static boolean RATE_EXP_OFF;
		public static boolean SHOW_MY_STAT;
		public static boolean LOG_FIGHT_PVP_PK;
		public static boolean PVP_PK_GRAFICAL_EFFECT;

		public static String MENSAJE_ENTRADA_PJ_KARMA;
		public static String MENSAJE_ENTRADA_PJ_TOPPVPPK;
		
		
		public static HashMap<Integer, HashMap<Integer, String>> COLOR_SYSTEM_PVP_NAME = new HashMap<Integer, HashMap<Integer, String>>(); 
		public static Map<Integer, HashMap<Integer, String>> COLOR_SYSTEM_PK_NAME = new HashMap<Integer, HashMap<Integer, String>>();
		
		public static String[] NAME_COLOR_FOR_ALL = new String[10];

		public static int TITLE_COLOR_FOR_PK_AMOUNT_1;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_2;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_3;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_4;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_5;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_6;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_7;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_8;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_9;
		public static int TITLE_COLOR_FOR_PK_AMOUNT_10;
		public static String[] TITLE_COLOR_FOR_ALL = new String[10];
		public static String RAID_ANNOUCEMENT_DIED;
		public static String RAID_ANNOUCEMENT_LIFE;
		public static int RAID_ANNOUCEMENT_ID_ANNOUCEMENT;

		public static boolean TRANSFORMATION_NOBLE;
		public static int TRANSFORMATION_LVL;
		public static String TRANSFORMATION_PRICE;
		public static boolean TRANSFORMATION_ESPECIALES;
		public static boolean TRANSFORMATION_RAIDBOSS;
		public static String TRANSFORMATION_ESPECIALES_PRICE;
		public static String TRANSFORMATION_RAIDBOSS_PRICE;
		public static boolean TRANSFORM_TIME;
		public static int TRANSFORM_TIME_MINUTES;
		public static int TRANSFORM_REUSE_TIME_MINUTES;

		public static boolean OLY_ALLOW;
		public static boolean OLY_ANNOUNCE_RESULTS;
		public static boolean OLY_ANTIFEED_CHANGE_TEMPLATE;
		public static boolean OLY_ANTIFEED_SHOW_NAME_NPC;
		public static boolean OLY_ANTIFEED_SHOW_NAME_OPPO;
		public static boolean OLY_ANTIFEED_SHOW_IN_NAME_CLASS;
		public static boolean OLY_ANTIFEED_SHOW_IN_TITLE_CLASS;

		public static int[] OLY_SECOND_SHOW_OPPONET;
		public static int[] OLY_ACCESS_ID_MODIFICAR_POINT;
		
		public static boolean OLY_DUAL_BOX_CONTROL;
		
		public static boolean OLY_CAN_USE_SCHEME_BUFFER;
		
		public static boolean INSTANCE_ZONE_CLEAR;
		public static String INSTANCE_ZONE_COST;
		
		
		public static int[] ENCHANT_ANNOUCEMENT;
		public static int PVP_PK_PROTECTION_LVL;
		public static int PVP_PK_PROTECTION_LIFETIME_MINUTES;
		
		
		public static int ANNOUCE_PJ_KARMA_CANTIDAD;
		public static boolean SHOP_USE_BD;

		public static boolean PVP_REWARD;
		public static boolean PVP_PARTY_REWARD;
		public static String PVP_REWARD_ITEMS;
		public static String PVP_PARTY_REWARD_ITEMS;
		public static boolean PVP_REWARD_CHECK_DUALBOX;
		public static int PVP_REWARD_RANGE;
		
		public static boolean PVP_CLAN_REPUTATION_REWARD;
		public static int PVP_CLAN_REPUTATION_AMOUNT;
		

		public static boolean ANNOUNCE_KARMA_PLAYER_WHEN_KILL;
		public static String ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN;

		protected static Vector<Integer> BOT_PLAYER = new Vector<Integer>();

		public static boolean SHOW_ZEUS_ENTER_GAME;


		public static String[][] PREGUNTAS_BOT = new String[800][2];
		public static int PREGUNTAS_BOT_CANT = 0;

		public static boolean ANTIBOT_COMANDO_STATUS;
		public static int ANTIBOT_MINUTOS_ESPERA;
		public static int ANTIBOT_OPORTUNIDADES;
		public static int ANTIBOT_MINUTOS_JAIL;
		public static int ANTIBOT_MOB_DEAD_TO_ACTIVATE;
		public static int ANTIBOT_MINUTE_VERIF_AGAIN;
		public static int ANTIBOT_MIN_LVL;
		public static int ANTIBOT_ANTIGUEDAD_MINUTOS;
		public static boolean ANTIBOT_NOBLE_ONLY;
		public static boolean ANTIBOT_HERO_ONLY;
		public static boolean ANTIBOT_GM_ONLY;
		public static boolean ANTIBOT_ANNOU_JAIL;
		public static boolean ANTIBOT_AUTO;
		public static boolean ANTIBOT_RESET_COUNT;
		public static boolean ANTIBOT_BORRAR_ITEM;
		public static int ANTIBOT_BORRAR_ITEM_PORCENTAJE;
		public static String ANTIBOT_BORRAR_ITEM_ID;
		public static boolean ANTIBOT_CHECK_INPEACE_ZONE;
		public static int ANTIBOT_INACTIVE_MINUTES;
		public static boolean ANTIBOT_SEND_ALL_IP;
		public static int ANTIBOT_SECONDS_TO_RESEND_ANTIBOT;
		public static boolean ANTIBOT_CHECK_DUALBOX;
		public static boolean ANTIBOT_SEND_JAIL_ALL_DUAL_BOX;
		public static boolean ANTIBOT_BLACK_LIST;
		public static int ANTIBOT_BLACK_LIST_MULTIPLIER;


		public static boolean BANIP_CHECK_IP_INTERNET;
		public static boolean BANIP_CHECK_IP_RED;
		public static boolean BANIP_STATUS;
		public static boolean BANIP_DISCONNECT_ALL_PLAYER;

		public static int BUFFCHAR_COST_HEAL;
		public static int BUFFCHAR_COST_CANCEL;
		public static boolean BUFFCHAR_USE_INSTANCE;
		public static boolean BUFFCHAR_JUST_ON_PEACE_ZONE;
		public static boolean BUFFCHAR_CAN_USE_COMBAT_MODE;
		public static boolean BUFFCHAR_CAN_HEAL_CANCEL_JUST_IN_PEACE_ZONE;
		
		public static boolean BUFFCHAR_HEAL_AND_CANCEL_COMBAT_MODE;
		public static boolean BUFFCHAR_HEAL_AND_CANCEL_FLAG_MODE;
		public static boolean BUFFCHAR_HEAL_AND_CANCEL_JUST_PEACE_ZONE;
		public static boolean BUFFCHAR_SCHEME_USE_COMBAT_MODE;
		public static boolean BUFFCHAR_SCHEME_USE_FLAG_MODE;
		public static int BUFFCHAR_SCHEME_USE_ITEM_ID;
		public static int _BUFFCHAR_SCHEME_MAX = 6;
		

		/*public static FastMap<Integer, Float> PREMIUM_ITEM_CHAR_DROP_LIST_RATE = new FastMap<Integer, Float>();
		public static FastMap<Integer, Float> PREMIUM_ITEM_CHAR_DROP_LIST_CHANCE = new FastMap<Integer, Float>();
		public static FastMap<Integer, Float> PREMIUM_ITEM_CLAN_DROP_LIST_RATE = new FastMap<Integer, Float>();
		public static FastMap<Integer, Float> PREMIUM_ITEM_CLAN_DROP_LIST_CHANCE = new FastMap<Integer, Float>();*/
		
		public static boolean PREMIUM_MESSAGE;
		
		
		public static String DONATION_TYPE_LIST;
		


		public static boolean CHAR_PANEL;

		public static boolean COMMUNITY_BOARD;
		public static boolean COMMUNITY_BOARD_REGION;
		public static boolean COMMUNITY_BOARD_ENGINE;
		public static boolean COMMUNITY_BOARD_CLAN;
		public static boolean COMMUNITY_BOARD_GRAND_RB;
		public static boolean COMMUNITY_BOARD_PARTYFINDER;
		public static boolean COMMUNITY_BOARD_DONATION;
		
		
		public static String _COMMUNITY_BOARD_PART_EXEC;
		public static String _COMMUNITY_BOARD_REGION_PART_EXEC;
		public static String _COMMUNITY_BOARD_ENGINE_PART_EXEC;
		public static String _COMMUNITY_BOARD_CLAN_PART_EXEC;
		public static String _COMMUNITY_BOARD_GRAND_RB_EXEC;
		public static String _COMMUNITY_BOARD_PARTYFINDER_EXEC;
		public static String _COMMUNITY_BOARD_DONATION_PART_EXEC;
		
		public static int COMMUNITY_BOARD_ROWS_FOR_PAGE;
		public static int COMMUNITY_BOARD_TOPPLAYER_LIST;
		public static int COMMUNITY_BOARD_CLAN_LIST;
		public static int COMMUNITY_BOARD_MERCHANT_LIST;
		public static int COMMUNITY_BOARD_CLAN_ROWN_LIST;

		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE;
		public static boolean COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN;
		public static int COMMUNITY_BOARD_REGION_PLAYER_ON_LIST ; //481
		public static Vector<String> COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION = new Vector<String>();
		
		public static Vector<String>COMMUNITY_REGION_LEGEND = new Vector<String>();

		private static Vector<String> COMMUNITY_BOARD_SV_CONFIG = new Vector<String>();

		public static int COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE;
		public static int COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST;
		public static int COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD;
		public static boolean COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE;
		
		public static int DONATION_NOBLE_COST;
		public static int DONATION_CHANGE_SEX_COST;
		public static int DONATION_AIO_CHAR_SIMPLE_COSTO;
		public static int DONATION_AIO_CHAR_30_COSTO;
		public static int DONATION_AIO_CHAR_LV_REQUEST;
		public static int DONATION_CHANGE_CHAR_NAME_COST;
		public static int DONATION_CHANGE_CLAN_NAME_COST;
		public static String DONATION_EXPLAIN_HOW_DO_IT;
		public static int DONATION_255_RECOMMENDS;
		public static Map<Integer, Integer> DONATION_CHARACTERS_LEVEL = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_CLAN_REPUTATION = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_CLAN_SKILL = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_CLAN_LEVEL = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_CHARACTERS_FAME_POINT = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_CHARACTERS_PK_POINT = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_ENCHANT_ITEM_ARMOR = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_ENCHANT_ITEM_WEAPON = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_ELEMENTAL_ITEM_ARMOR = new HashMap<Integer, Integer>();
		public static Map<Integer, Integer> DONATION_ELEMENTAL_ITEM_WEAPON = new HashMap<Integer, Integer>();

		public static boolean DONATION_EXTRA_GIFT;
		public static Map<Integer, Integer>DONATION_EXTRA_GIFT_ITEMS = new HashMap<Integer, Integer>(); 
		public static Vector<Integer> DONATION_BLACK_LIST = new Vector<Integer>();
		public static boolean DONATION_EXTRA_GIFT_REPEAT;
		public static int DONATION_EXTRA_GIFT_TIMES;
		public static String DONATION_TEMP_EVERY_TYPE;
		public static int DONATION_EXTRA_GIFT_REPEAT_EVERY_X;
		public static int DONATION_EXTRA_GIFT_FROM_DONATION_COIN;
		private static Map<Integer, _donaGift> DONATION_EXTRA_DATA = new HashMap<Integer, _donaGift>();
		
		public static boolean PREMIUM_CHAR;
		public static boolean PREMIUM_CLAN;

		
		public static boolean EVENT_TOWN_WAR_AUTOEVENT;
		public static int EVENT_TOWN_WAR_MINUTES_START_SERVER;
		public static int EVENT_TOWN_WAR_MINUTES_INTERVAL;
		public static String EVENT_TOWN_WAR_CITY_ON_WAR;
		public static int EVENT_TOWN_WAR_MINUTES_EVENT;
		public static int EVENT_TOWN_WAR_JOIN_TIME;
		public static boolean EVENT_TOWN_WAR_DUAL_BOX_CHECK;
		
		public static boolean EVENT_TOWN_WAR_GIVE_PVP_REWARD;
		public static boolean EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER;
		public static String EVENT_TOWN_WAR_REWARD_GENERAL;
		public static String EVENT_TOWN_WAR_REWARD_TOP_PLAYER;
		public static boolean EVENT_TOWN_WAR_HIDE_NPC;
		public static boolean EVENT_TOWN_WAR_RANDOM_CITY;
		public static String EVENT_TOWN_WAR_NPC_ID_HIDE;
		public static boolean EVENT_TOWN_WAR_CAN_USE_BUFFER;
		
		
		
		public static boolean EVENT_RAIDBOSS_AUTOEVENT;
		public static Vector<Integer> EVENT_RAIDBOSS_RAID_ID = new Vector<Integer>();
		public static String EVENT_RAIDBOSS_RAID_POSITION;
		public static String EVENT_RAIDBOSS_PLAYER_POSITION;
		public static boolean EVENT_RAIDBOSS_PLAYER_INMOBIL;
		public static boolean EVENT_RAIDBOSS_CHECK_DUALBOX;
		public static String EVENT_RAIDBOSS_REWARD_WIN;
		public static String EVENT_RAIDBOSS_REWARD_LOOSER;
		public static int EVENT_RAIDBOSS_PLAYER_MIN_LEVEL;
		public static int EVENT_RAIDBOSS_PLAYER_MAX_LEVEL;
		public static int EVENT_RAIDBOSS_PLAYER_MIN_REGIS;
		public static int EVENT_RAIDBOSS_PLAYER_MAX_REGIS;
		public static int EVENT_RAIDBOSS_SECOND_TO_BACK;
		public static int EVENT_RAIDBOSS_JOINTIME = 0;
		public static int EVENT_RAIDBOSS_EVENT_TIME = 0;
		public static String EVENT_RAIDBOSS_COLORNAME;
		public static boolean EVENT_RAIDBOSS_CANCEL_BUFF;
		public static boolean EVENT_RAIDBOSS_UNSUMMON_PET;
		public static int EVENT_RAIDBOSS_SECOND_TO_REVIVE;
		public static int EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS;
		public static String EVENT_RAIDBOSS_HOUR_TO_START;
		public static int EVENT_RAIDBOSS_MINUTE_INTERVAL;
		public static int EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER;		

		public static boolean REGISTER_EMAIL_ONLINE;
		public static int REGISTER_NEW_PLAYER_WAITING_TIME;
		public static int REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME;
		public static int REGISTER_NEW_PlAYER_TRIES;
		public static boolean REGISTER_NEW_PLAYER_BLOCK_CHAT;
		public static boolean REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT;
		
		
		public static boolean EVENT_REPUTATION_CLAN;		
		public static int EVENT_REPUTATION_CLAN_ID_NPC;
		public static int EVENT_REPUTATION_LVL_TO_GIVE;
		public static int EVENT_REPUTATION_REPU_TO_GIVE;
		public static int EVENT_REPUTATION_MIN_PLAYER;
		public static boolean EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE;

		public static boolean VOTE_SHOW_ZEUS_ONLY_BY_ITEM;
		public static int VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID;

		public static boolean VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM;
		public static int VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID;
		public static String VOTE_SHOW_ZEUS_TEMPORAL_PRICE;
		public static boolean VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER;
		public static boolean VOTE_EVERY_12_HOURS;

		public static int EVENT_COLISEUM_NPC_ID;
		
		public static Integer ANTIFEED_ENCHANT_SKILL_REUSE;

		public static boolean SHOW_NEW_MAIN_WINDOWS;
		public static boolean SHOW_FIXME_WINDOWS;

		protected static Map<Integer, Integer> ANTIBOT_PLAYER_MOB_KILL = new HashMap<Integer, Integer>();
		protected static Map<Integer, Integer> ANTIBOT_PLAYER_TIME = new HashMap<Integer, Integer>();

		protected static Map<L2PcInstance, HashMap<Integer, String>> CHAR_CONEX = new HashMap<L2PcInstance, HashMap<Integer, String>>();

		protected static Vector<String> IPBAN_INTERNET = new Vector<String>();
		protected static Vector<String> IPBAN_MAQUINA = new Vector<String>();
		
		public static boolean ZEUS_OLY_COUNTER;
		public static boolean ZEUS_OLY_SHOW_DMG;
		
		
		public static boolean ZEUS_AUTOPOTS_CP;
		public static boolean ZEUS_AUTOPOTS_MP;
		public static boolean ZEUS_AUTOPOTS_HP;
		public static int ZEUS_AUTOPOTS_CHECK_MILISECOND;
		
		public static boolean CAN_USE_A_OLD_PASSWORD_AS_NEW;
		
		public static Vector<_dropsearch> DROP_LIST_CLASS = new Vector<_dropsearch>();

		public static Map<Integer, Boolean> DRESSME_SHARE = new HashMap<Integer, Boolean>();


		private static Map<L2PcInstance, HashMap<String, Boolean>> playerConfig = new HashMap<L2PcInstance, HashMap<String, Boolean>>();
		private static Map<L2PcInstance, Integer> countPinCode = new HashMap<L2PcInstance, Integer>();
		public static Map<L2PcInstance, Integer> charPVPCOUNT = new HashMap<L2PcInstance, Integer>();
		public static Map<L2PcInstance, Integer> charPKCOUNT = new HashMap<L2PcInstance, Integer>();
		public static Map<L2PcInstance, Integer> charBufferTime = new HashMap<L2PcInstance, Integer>();
		public static Map<L2PcInstance, Integer> havePetSum = new HashMap<L2PcInstance, Integer>();
		public static Map<L2PcInstance, Integer> blockUntilTime = new HashMap<L2PcInstance, Integer>();
		public static Map<L2PcInstance, Integer> BLOQUEO_ACCION = new HashMap<L2PcInstance, Integer>();


		private static Map<L2PcInstance, Integer> OnlineTimeToday = new HashMap<L2PcInstance, Integer>();
		
		public static void setNewTimeLife(L2PcInstance player){
			OnlineTimeToday.put(player, opera.getUnixTimeNow());
		}
		
		public static String getLifeToday(L2PcInstance player){
			if(OnlineTimeToday!=null){
				if(!OnlineTimeToday.containsKey(player)){
					OnlineTimeToday.put(player,unixTimeBeginServer);
				}
			}
			int unixNow = opera.getUnixTimeNow();
			int Resultado = unixNow - OnlineTimeToday.get(player);
			String RetornoStr = opera.getTiempoON(Resultado);
			return RetornoStr;
		}
//public static Map<String, Map<Integer, Map<String, String>>> BUFF_CHAR_DATA
		public static Map<String, HashMap<Integer, HashMap<String, String>>> BUFF_CHAR_DATA = new HashMap<String, HashMap<Integer, HashMap<String, String>>>();

		private static Map<L2PcInstance, Vector<String>> CHAR_BUFF_SCH = new HashMap<L2PcInstance, Vector<String>>();

		public static void deleteSchemme(L2PcInstance player, String nomSch){
			CHAR_BUFF_SCH.get(player).remove(nomSch);
		}

		public static Vector<String> getNameBuffSch (L2PcInstance player){
			if(!CHAR_BUFF_SCH.containsKey(player)){
				return null;
			}
			return CHAR_BUFF_SCH.get(player);
		}


		public static void setCharVariables(L2PcInstance cha){
			general.getInstance().loadCharConfig(cha);
			charPVPCOUNT.put(cha, 0);
			charPKCOUNT.put(cha, 0);

			general.getInstance().setConfigExpSp(cha, true);

			if(!charBufferTime.containsKey(cha)){
				charBufferTime.put(cha, 0);
			}
			if(!havePetSum.containsKey(cha)){
				havePetSum.put(cha, 0);
			}
			if(!blockUntilTime.containsKey(cha)){
				blockUntilTime.put(cha, 0);
			}
			if(!BLOQUEO_ACCION.containsKey(cha)){
				BLOQUEO_ACCION.put(cha, 0);
			}
		}

		public static int getPinCountChar(L2PcInstance cha){
			if(countPinCode.containsKey(cha)){
				return countPinCode.get(cha);
			}
			return 0;
		}

		public static void setPinCountChar(L2PcInstance cha,int contador){
			if(!countPinCode.containsKey(cha)){
				countPinCode.put(cha, contador);
				return;
			}
			countPinCode.put(cha, contador);
		}

		public static void setPinCountChar(L2PcInstance cha){
			if(!countPinCode.containsKey(cha)){
				countPinCode.put(cha, 1);
				return;
			}
			countPinCode.put(cha, countPinCode.get(cha) + 1);
		}

		public static void loadDropList(){
			loadAllDropList();
		}

		public void setConfigREFUSAL(L2PcInstance player,boolean value){
			setConfigPlayer(player,"REFUSAL",value);
		}
		public static boolean getCharConfigREFUSAL(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("REFUSAL");
		}
		
		public void setConfigReadOlyWinner(L2PcInstance player,boolean value){
			setConfigPlayer(player,"READ_OLY_WINNER",value);
		}
		public static boolean getCharConfigReadOlyWinner(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("READ_OLY_WINNER");
		}		
		
		public void setConfigOlyScheme(L2PcInstance player,boolean value){
			setConfigPlayer(player,"OLY_SCHEME",value);
		}
		public static boolean getCharConfigOlyScheme(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("OLY_SCHEME");
		}
		
		
		public void setConfigPartyMatching(L2PcInstance player,boolean value){
			setConfigPlayer(player,"PARTYMATCHING",value);
		}
		public static boolean getCharConfigPartyMatching(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("PARTYMATCHING");
		}
		
		public void setConfigHideStore(L2PcInstance player,boolean value){
			setConfigPlayer(player,"HIDESTORE",value);
		}
		public static boolean getCharConfigHIDESTORE(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("HIDESTORE");
		}

		public void setConfigBadBuff(L2PcInstance player,boolean value){
			setConfigPlayer(player,"BADBUFF",value);
		}
		public static boolean getCharConfigBADBUFF(L2PcInstance player){
			try{
				if(!playerConfig.containsKey(player)){
					return false;
				}
				return playerConfig.get(player).get("BADBUFF");
			}catch(Exception a){
				return false;
			}
		}

		public void setConfigTrade(L2PcInstance player,boolean value){
			setConfigPlayer(player,"TRADE",value);
		}
		public static boolean getCharConfigTRADE(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("TRADE");
		}

		public void setConfigExpSp(L2PcInstance player, boolean value){
			setConfigPlayer(player,"EXPSP",value);
		}
		public static boolean getCharConfigEXPSP(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("EXPSP");
		}

		public void setConfigHeroPlayer(L2PcInstance player, boolean value){
			setConfigPlayer(player,"HERO",value);
		}

		public static boolean getCharConfigHERO(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("HERO");
		}

		public void setConfigPIN(L2PcInstance player, boolean value){
			setConfigPlayer(player, "PIN", value);
		}

		public static boolean getCharConfigPIN(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("PIN");
		}

		public void setConfigEFFECT(L2PcInstance player, boolean value){
			setConfigPlayer(player, "EFFECT", value);
		}

		public static boolean getCharConfigEFFECT(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("EFFECT");
		}

		public void setConfigANNOU(L2PcInstance player, boolean value){
			setConfigPlayer(player, "ANNOU", value);
		}

		public static boolean getCharConfigANNOU(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("ANNOU");
		}

		public void setConfigSHOWSTAT(L2PcInstance player, boolean value){
			setConfigPlayer(player,"SHOWSTAT",value);
		}

		public static boolean getCharConfigSHOWSTAT(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("SHOWSTAT");
		}

		public void setConfigBANOLY(L2PcInstance player, boolean value){
			setConfigPlayer(player, "BANOLY", value);
		}

		public static boolean getCharConfigBANOLY(L2PcInstance player){
			if(!playerConfig.containsKey(player)){
				return false;
			}
			return playerConfig.get(player).get("BANOLY");
		}

		public static boolean havePremium(L2PcInstance player, boolean checkplayer){
			if(player==null){
				_log.warning("Error Null player");
			}
			if(!checkplayer){
				if(player.getClan()==null){
					return false;
				}
			}
			String idBusqueda = checkplayer ? player.getAccountName() : String.valueOf(player.getClan().getId());
			if(PREMIUM_SERVICES_CLAN_CHAR != null){
				if(PREMIUM_SERVICES_CLAN_CHAR.containsKey(idBusqueda)){
					if(checkplayer && PREMIUM_SERVICES_CLAN_CHAR.get(idBusqueda).ISACTIVE && general.PREMIUM_CHAR){
						return true;
					}
					
					if(!checkplayer && PREMIUM_SERVICES_CLAN_CHAR.get(idBusqueda).ISACTIVE && general.PREMIUM_CLAN){
						return true;
					}
				}
			}

			return false;
		}

		public static boolean isPremium(L2PcInstance player, boolean checkplayer){
			return havePremium(player,checkplayer);
		}

		private static void updatePremiumTable(){
			String DeleteInfo = "DELETE FROM zeus_premium WHERE end_date <= ?";
			try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statementt = conn.prepareStatement(DeleteInfo))
				{
					// Remove or update a L2PcInstance skill from the character_skills table of the database
					statementt.setInt(1, opera.getUnixTimeNow());
					statementt.execute();
				}
				catch (Exception e)
				{
					
				}	
		}
		
		public static premiumPersonalData getPremiumDataFromPlayerOrClan(int DataBuscar){
			return PREMIUM_SERVICES_CLAN_CHAR.get(String.valueOf(DataBuscar));
		}
		
		public static premiumPersonalData getPremiumDataFromPlayerOrClan(String DataBuscar){
			return PREMIUM_SERVICES_CLAN_CHAR.get(DataBuscar);
		}
		
		public static void loadZeuSPremium(){
			updatePremiumTable();
			if(PREMIUM_SERVICES_CLAN_CHAR!=null){
				PREMIUM_SERVICES_CLAN_CHAR.clear();
			}
			String Consulta ="Select * from zeus_premium";
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement(Consulta))
					{
					try (ResultSet rs = ps.executeQuery())
					{
						while (rs.next())
						{
							try{
								premiumsystem T1 = null;
								T1 = general.getPremiumServices().get(rs.getInt("idPremium"));
								premiumPersonalData t = new premiumPersonalData(rs.getString("ID"),rs.getInt("start_date"),rs.getInt("end_date"), ( rs.getString("tip").equals("ACCOUNT") ? true : false ), rs.getInt("idPremium"), T1);
								PREMIUM_SERVICES_CLAN_CHAR.put(rs.getString("ID"), t);								
							}catch(Exception e){
								_log.warning("ZeuS Premium cant load the premiun data ID->" + rs.getInt("idPremium") + " for player Account or Clan ID ->" + rs.getString("ID"));
								continue;
							}
						}
					}
					}
				catch (SQLException e)
				{
					_log.severe("Error Premium Load: " + e.getMessage());
				}
		}

		public void loadCharConfig(L2PcInstance player){
			String Consulta = "call sp_char_config(?,?,?,?)";
			try (Connection con = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement ps = con.prepareStatement(Consulta))
					{
					ps.setInt(1, 1);
					ps.setInt(2, 1);
					ps.setInt(3, player.getObjectId());
					ps.setString(4, "");
					try (ResultSet rs = ps.executeQuery())
					{
						while (rs.next())
						{
							setConfigANNOU(player, rs.getInt(2)==0?false:true);
							setConfigEFFECT(player, rs.getInt(3)==0?false:true);
							setConfigSHOWSTAT(player, rs.getInt(4)==0?false:true);
							setConfigPIN(player, rs.getInt(5)==0?false:true);
							setConfigBANOLY(player, rs.getInt(7)==0?false:true);
							setConfigHeroPlayer(player, rs.getInt(8)==0?false:true);
							setConfigExpSp(player, rs.getInt(9)==0?false:true);
							setConfigTrade(player, rs.getInt(10)==0?false:true);
							setConfigBadBuff(player, rs.getInt(11)==0?false:true);
							setConfigHideStore(player, rs.getInt(12)==0?false:true);
							setConfigREFUSAL(player, rs.getInt(13)==0?false:true);
							setConfigPartyMatching(player, rs.getInt(14)==0?false:true);
							setConfigOlyScheme(player, rs.getInt(15)==0?false:true);
							setConfigReadOlyWinner(player, rs.getInt(16)==0?false:true);
						}
					}
					}
				catch (SQLException e)
				{
					_log.severe("ZeuS Error-> Load Personal Config '"+ player.getName() +"': " + e.getMessage());
				}
		}

		private void setConfigPlayer(L2PcInstance player,String Parametro, boolean valor){
			if(!playerConfig.containsKey(player)){
				playerConfig.put(player, new HashMap<String,Boolean>());
			}
			playerConfig.get(player).put(Parametro, valor);
		}

		public static Map<L2PcInstance, HashMap<String, Boolean>> getConfigChar(){
			return playerConfig;
		}

		private static void getAllItemForSearch() {
			try {
		    	ITEM_FOR_SEARCH.clear();
		    }catch(Exception a) {
		    	
		    }
		    try {
		    	for(L2Item ItemSelec : ItemData.getInstance().getAllItems()){
					try{
						if(ItemSelec != null){
							_itemInfo ttInfoItem = new _itemInfo(ItemSelec);
							ITEM_FOR_SEARCH.put(ItemSelec.getId(), ttInfoItem);
						}
					}catch(Exception a){

					}

				}		    	
		    }catch(Exception a) {
		    	
		    }				
		}
		

		@SuppressWarnings({ "rawtypes", "unchecked" })
		private static boolean loadAllDropList(){
			Vector<String> TiposNPC = new Vector<String>();
			TiposNPC.add("L2FlyMonster");
			TiposNPC.add("L2FlyRaidBoss");
			TiposNPC.add("L2Monster");
			TiposNPC.add("L2RaidBoss");
			int IdDrop = 0;
			int IdMob = 0;
			
			Map<Integer, HashMap<Integer, HashMap<String, String>>> DROP_LIST_ITEM = new HashMap<Integer, HashMap<Integer, HashMap<String,String>>>();
			
			Comparator<L2NpcTemplate> NPC_NAME_COMPARATOR_ = (p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName());
			
			for(String TipoClaseBuscando: TiposNPC){
				List<L2NpcTemplate> L2NpcTemplateSort = NpcTable.getInstance().getAllNpcOfClassType(TipoClaseBuscando);
				L2NpcTemplateSort.sort(NPC_NAME_COMPARATOR_);
				for(L2NpcTemplate tmpl : L2NpcTemplateSort){
					int IdNpc = tmpl.getId();
					if(tmpl.getDropData() != null){
						Set<L2Spawn> _NpcSpawn = SpawnTable.getInstance().getSpawns(IdNpc);
						if(_NpcSpawn==null) {
							continue;
						}
						for(L2Spawn NpcSpawn : _NpcSpawn){
							Vector<_InfoGeneralDrop> DropDataVector = _GeneralDropInfo.getAllDropFromMob(IdNpc);
		    				for(_InfoGeneralDrop DropIn : DropDataVector){
				    			if(!DROP_LIST_ITEM.containsKey(DropIn.getIdItem())){
				    				DROP_LIST_ITEM.put(DropIn.getIdItem(), new HashMap<Integer, HashMap<String,String>>());
				    			}
				    			if(!DROP_LIST_ITEM.get(DropIn.getIdItem()).containsKey(tmpl.getId())){
				    				DROP_LIST_ITEM.get(DropIn.getIdItem()).put(tmpl.getId(), new HashMap<String, String>());
				    			}				    			
				    			IdDrop = DropIn.getIdItem();
				    			IdMob = tmpl.getId();
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("NAME", tmpl.getName());
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TYPE", tmpl.getType());
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("LEVEL", String.valueOf(tmpl.getLevel()));
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CATEGORY", DropIn.getCategoryTypeSTR());
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MIN", String.valueOf(DropIn.getMin()));
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("MAX", String.valueOf(DropIn.getMax()));
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("CHANCE", String.valueOf(DropIn.getChance()));
				    			DROP_LIST_ITEM.get(IdDrop).get(IdMob).put("TELEPORT", String.valueOf(NpcSpawn.getLocation().getX()) + ","+String.valueOf(NpcSpawn.getLocation().getY())+","+String.valueOf(NpcSpawn.getLocation().getZ()));
				    			String NpcForSearch = tmpl.getName() + ":" + String.valueOf(tmpl.getLevel()) + ":" + String.valueOf(IdMob);
								v_dropsearch.setNPCInfoForSearch(NpcForSearch);
		    				}
						}
					}
				}//for
			}
			
			List<Long> FSave = new CopyOnWriteArrayList<>();
		
			try{
				DROP_LIST_CLASS.clear();
			}catch(Exception a){
				
			}
			
			Iterator itr = DROP_LIST_ITEM.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry InfoItem =(Map.Entry)itr.next();
				int ID_ITEM = (int)InfoItem.getKey();
				Map<Integer, HashMap<String, String>> Inf1 = new HashMap<Integer, HashMap<String, String>>();
				Inf1 = (Map<Integer, HashMap<String, String>>) InfoItem.getValue();
				Iterator itr_mob = Inf1.entrySet().iterator();
				while(itr_mob.hasNext()){
					Map.Entry infoMobb = (Map.Entry) itr_mob.next();
					int ID_MOB = (int) infoMobb.getKey();
					String NombreMob = DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("NAME");
					int MAXDROP = Integer.valueOf(DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("MAX"));
					int MINDROP = Integer.valueOf(DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("MIN"));
					String TYPE = DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("TYPE");
					int LEVELNPC = Integer.valueOf(DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("LEVEL"));
					int CATEGORIA = Integer.valueOf(DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("CATEGORY"));
					String CHANCEE =  DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("CHANCE");
					String TELEPORT = DROP_LIST_ITEM.get(ID_ITEM).get(ID_MOB).get("TELEPORT");
					Long Temp = Long.parseLong(String.valueOf(ID_ITEM) + String.valueOf(ID_MOB) + String.valueOf(MAXDROP));
					if(!FSave.contains(Temp)){
						FSave.add(Temp);
						_dropsearch TempData = new _dropsearch(NombreMob, ID_MOB, ID_ITEM , MAXDROP , MINDROP,TYPE,LEVELNPC,CATEGORIA,CHANCEE,TELEPORT);
						DROP_LIST_CLASS.add(TempData);
					}
				}
			}
			
			Comparator<_dropsearch> NameAZ = (p1,p2) -> p1.getName().compareToIgnoreCase(p2.getName());
			Collections.sort(DROP_LIST_CLASS, NameAZ);
			
			v_dropsearch.Load();
		    _log.warning("	--- ZeuS Drop List Has been loaded "+ String.valueOf(DROP_LIST_ITEM.size()) +" items from Drop List ---");
		    return true;
		}

		public static final Vector<_dropsearch> GET_DROP_LIST_CLASS() {
			return DROP_LIST_CLASS;
		}

		private static void cleanBD(){
			Vector<String> SQL_Clean = new Vector<String>();
			SQL_Clean.add("DELETE FROM zeus_buffer_scheme_list WHERE playerId NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buffer_scheme_contents WHERE schemeId NOT IN (SELECT zeus_buffer_scheme_list.playerId FROM zeus_buffer_scheme_list)");
			SQL_Clean.add("DELETE FROM zeus_buff_char_sch WHERE idChar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buff_char_sch_buff WHERE zeus_buff_char_sch_buff.idSch NOT IN (SELECT zeus_buff_char_sch.id FROM zeus_buff_char_sch)");
			SQL_Clean.add("DELETE FROM zeus_char_config WHERE zeus_char_config.idchar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_secundary_pass WHERE zeus_secundary_pass.account NOT IN (SELECT characters.account_name FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_auctions_house WHERE zeus_auctions_house.idOwner NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buffstore WHERE zeus_buffstore.idChar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buffstore_scheme WHERE zeus_buffstore_scheme.idplayer NOT IN (SELECT characters.charId FROM characters) OR zeus_buffstore_scheme.id_ppl_seller NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_buffstore_scheme_buff WHERE zeus_buffstore_scheme_buff.idscheme NOT IN (SELECT zeus_buffstore_scheme.id FROM zeus_buffstore_scheme)");			
			SQL_Clean.add("DELETE FROM zeus_connection WHERE zeus_connection.charID NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_dressme WHERE zeus_dressme.idChar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_log_fight WHERE zeus_log_fight.idAtacante NOT IN (SELECT characters.charId FROM characters) OR zeus_log_fight.idAsesinado NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_dressme WHERE zeus_dressme.idChar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_oly_sch WHERE zeus_oly_sch.idChar NOT IN (SELECT characters.charId FROM characters)");
			SQL_Clean.add("DELETE FROM zeus_oly_sch_buff WHERE zeus_oly_sch_buff.idsch NOT IN (SELECT zeus_oly_sch.id FROM zeus_oly_sch)");
			SQL_Clean.add("DELETE FROM zeus_premium WHERE zeus_premium.id NOT IN (SELECT characters.account_name FROM characters) AND tip='ACCOUNT'");
			SQL_Clean.add("DELETE FROM zeus_premium WHERE zeus_premium.id NOT IN (SELECT clan_data.clan_id FROM clan_data) AND tip='CLAN'");
			SQL_Clean.add("DELETE FROM zeus_cb_clan_foro WHERE zeus_cb_clan_foro.idClan NOT IN (SELECT clan_data.clan_id FROM clan_data) AND zeus_cb_clan_foro.idChar NOT IN (SELECT characters.charId FROM characters) ");
			SQL_Clean.add("DELETE FROM zeus_secundary_pass WHERE zeus_secundary_pass NOT IN ( SELECT " + general.LOGINSERVERNAME + ".accounts.login FROM accounts)");
			//SQL_Clean.add("DELETE FROM items WHERE items.object_id NOT IN ( SELECT zeus_auctions_house.idObjeto FROM zeus_auctions_house) AND items.owner_id = -300");
			for(String consulta : SQL_Clean){
				Connection conn = null;
				PreparedStatement psqry = null;
				try{
					if(Config.DEBUG){
						_log.warning("   Begin to clean ->" + consulta);
					}
					conn = L2DatabaseFactory.getInstance().getConnection();
					psqry = conn.prepareStatement(consulta);
					psqry.executeUpdate();
					psqry.close();
					if(Config.DEBUG){
						_log.warning("   Clean ->" + consulta);
					}
				}catch(Exception e){
 
				}
				try{
					conn.close();
				}catch(Exception a){

				}
			}
		}


		public static void RegistrarPlayerIPs(L2PcInstance player, String IPChar, int idSeccion){
			
			if(CHAR_CONEX == null){
				CHAR_CONEX.put(player,new HashMap<Integer, String>());
			}else if(!CHAR_CONEX.containsKey(player)){
				CHAR_CONEX.put(player,new HashMap<Integer, String>());
			}
			CHAR_CONEX.get(player).put(idSeccion, IPChar);
		}

		public static String getIPPlayer(L2PcInstance player, int idSeccion){
			return CHAR_CONEX.get(player).get(idSeccion);
		}


		public static void setTime_antibot(L2PcInstance player){
			int nextCheck = opera.getUnixTimeNow() + (ANTIBOT_MINUTE_VERIF_AGAIN * 60);
			ANTIBOT_PLAYER_TIME.put(player.getObjectId(), nextCheck);
		}

		public static int getTime_antibot(L2PcInstance player){
			if(ANTIBOT_PLAYER_TIME.containsKey(player.getObjectId())){
				return ANTIBOT_PLAYER_TIME.get(player.getObjectId());
			}
			return 0;
		}

		public static Map<Integer, Integer> getANTIBOT_PLAYER(){
			return ANTIBOT_PLAYER_MOB_KILL;
		}

		public static int getMobKillAntibot(L2PcInstance player){
			return ANTIBOT_PLAYER_MOB_KILL.get(player.getObjectId());
		}

		public static void resetKillAntibot(L2PcInstance player){
			try{
				ANTIBOT_PLAYER_MOB_KILL.put(player.getObjectId(), 0);
			}catch(Exception a){

			}
		}

		public static void addKillAntibot(L2PcInstance player){
			if(player.isInSiege()){
				if(!player.isInsideZone(ZoneIdType.SIEGE)){
					return;
				}
			}
			ANTIBOT_PLAYER_MOB_KILL.put(player.getObjectId(), ANTIBOT_PLAYER_MOB_KILL.get(player.getObjectId()) + 1);
			antibotSystem.setLastKillTime(player);
		}

		public static void addPlayerAntibot(L2PcInstance player){
			if(!general.ANTIBOT_RESET_COUNT){
				if(ANTIBOT_PLAYER_MOB_KILL.containsKey(player.getObjectId())){
					return;
				}
			}
			ANTIBOT_PLAYER_MOB_KILL.put(player.getObjectId(), 0);
		}


		public static Vector<Integer> getVectorBotPlayer(){
			return BOT_PLAYER;
		}

		public static void addBotPlayer(L2PcInstance player){
			BOT_PLAYER.add(player.getObjectId());
		}
		public static void removeBotPlayer(L2PcInstance player){
			if(BOT_PLAYER.contains(player.getObjectId())){
				BOT_PLAYER.removeElement(player.getObjectId());
			}
		}
		public static boolean isBotCheckPlayer(L2PcInstance player){
			return BOT_PLAYER.contains(player.getObjectId());
		}


		/*ZeuS SERVER*/


	public static int[] get_AccessConfig(){
		return AccessConfig;
	}



	protected static final String TITULO_NPC = "";


	public static String TITULO_NPC(){
		return Server_Name;
	}

	public static String PIE_PAGINA_COMMUNIDAD(){
		return "";
	}
	public static String PIE_PAGINA(){
		return "";
	}

	public static String QUEST_INFO = "";

	public static String []BUFF_ENCHANT_PIDE;
	public static String PIE_DE_PAGINA = "";
	public static String NOMBRE_NPC = "ZeuS";
	public static int ID_NPC;
	public static int ID_NPC_CH;

	public static int MAX_LISTA_PVP;
	public static int MAX_LISTA_PVP_LOG;

	public static int IntPintarGrilla;
	//public static String npcGlobal="";

	public static String npcGlobal(L2PcInstance player){

		boolean buscar = false;

		if(general.IS_USING_NPC.get(player.getObjectId()) && !general.IS_USING_CB.get(player.getObjectId())){
			buscar = false;
		}else if(!general.IS_USING_NPC.get(player.getObjectId()) && general.IS_USING_CB.get(player.getObjectId())){
			buscar = true;
		}

		return npcGlobal(player,buscar);
	}

	public static L2Npc getNpcGlobal(){
		for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(ID_NPC)){
			L2Npc npcLocal = SpawnLo.getLastSpawn();
			CB_IDObjectMainZeuSNPC = npcLocal.getObjectId();
			return npcLocal;
		}
		
		return null;
	}
	
	public static String npcGlobal(L2PcInstance player,boolean isComunidad){
		if(!_isValid){
			return "";
		}
		if(!isComunidad){
			if(player.getTarget() instanceof L2Npc){
				L2Npc npc = (L2Npc) player.getTarget();
				if(!ManagerAIONpc.isNpcFromZeus(opera.getIDNPCTarget( player ))){
					return "0";
				}
				String idObjeto = String.valueOf(npc.getObjectId());
				return idObjeto;
			}
		}else{
			if(CB_IDObjectMainZeuSNPC==0){
				for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(ID_NPC)){
					if(CB_IDObjectMainZeuSNPC==0){
						L2Npc npcLocal = SpawnLo.getLastSpawn();
						CB_IDObjectMainZeuSNPC = npcLocal.getObjectId();
						return String.valueOf(CB_IDObjectMainZeuSNPC);
					}
				}

			}else{
				return String.valueOf(CB_IDObjectMainZeuSNPC);
			}
		}
		return "";
	}


	public static void loadConfigs(){
		loadConfigs(true);
	}

	public static void loadConfigs(boolean showmessage){
		if(!onLine){
			loadSerial();
			checkValidate();
			if(httpResp.isConect){
				if(!_isValid){
					_log.warning(":::::: Wrog ZeuS Serial ::::::");
					return;
				}

				_log.info("::::::::::::::::: LOAD PROCESS :::::::::::::::::");
			}else{
				return;
			}
			BD.getInstance().checkBD();
		}
		getAllItemForSearch();
		getAllClases();
		getAllDressmeItem();
		wishList.loadWishList();
		if(general.ServerStartUnixTime <= 0){
			general.ServerStartUnixTime = opera.getUnixTimeNow();
		}

		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "SELECT zeus_config_seccion.seccion, zeus_config_seccion.param FROM zeus_config_seccion";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				switch(rss.getString(1)){
				case "ID_NPC":
					ID_NPC = Integer.valueOf(rss.getString(2));
					break;
				case "ID_NPC_CH":
					ID_NPC_CH = Integer.valueOf(rss.getString(2));
					break;
				case "MAX_LISTA_PVP":
					MAX_LISTA_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "MAX_LISTA_PVP_LOG":
					MAX_LISTA_PVP_LOG = Integer.valueOf(rss.getString(2));
					break;
				case "DEBUG_CONSOLA_ENTRADAS":
					DEBUG_CONSOLA_ENTRADAS = Boolean.valueOf(rss.getString(2));
					break;
				case "DEBUG_CONSOLA_ENTRADAS_TO_USER":
					DEBUG_CONSOLA_ENTRADAS_TO_USER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VOTE":
					BTN_SHOW_VOTE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUFFER":
					BTN_SHOW_BUFFER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TELEPORT":
					BTN_SHOW_TELEPORT =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SHOP":
					BTN_SHOW_SHOP =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_WAREHOUSE":
					BTN_SHOW_WAREHOUSE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT":
					BTN_SHOW_AUGMENT =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SUBCLASES":
					BTN_SHOW_SUBCLASES =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLASS_TRANSFER":
					BTN_SHOW_CLASS_TRANSFER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CONFIG_PANEL":
					BTN_SHOW_CONFIG_PANEL =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DROP_SEARCH":
					BTN_SHOW_DROP_SEARCH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PVPPK_LIST":
					BTN_SHOW_PVPPK_LIST =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_LOG_PELEAS":
					BTN_SHOW_LOG_PELEAS =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CASTLE_MANAGER":
					BTN_SHOW_CASTLE_MANAGER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DESAFIO":
					BTN_SHOW_DESAFIO =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SYMBOL_MARKET":
					BTN_SHOW_SYMBOL_MARKET =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLANALLY":
					BTN_SHOW_CLANALLY =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PARTYFINDER":
					BTN_SHOW_PARTYFINDER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_FLAGFINDER":
					BTN_SHOW_FLAGFINDER =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_COLORNAME":
					BTN_SHOW_COLORNAME =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DELEVEL":
					BTN_SHOW_DELEVEL =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_REMOVE_ATRIBUTE":
					BTN_SHOW_REMOVE_ATRIBUTE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUG_REPORT":
					BTN_SHOW_BUG_REPORT =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DONATION":
					BTN_SHOW_DONATION =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_PJ":
					BTN_SHOW_CAMBIO_NOMBRE_PJ =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_CLAN":
					BTN_SHOW_CAMBIO_NOMBRE_CLAN =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VARIAS_OPCIONES":
					BTN_SHOW_VARIAS_OPCIONES =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ELEMENT_ENHANCED":
					BTN_SHOW_ELEMENT_ENHANCED =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ENCANTAMIENTO_ITEM":
					BTN_SHOW_ENCANTAMIENTO_ITEM =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_SPECIAL":
					BTN_SHOW_AUGMENT_SPECIAL =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_GRAND_BOSS_STATUS":
					BTN_SHOW_GRAND_BOSS_STATUS =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_RAIDBOSS_INFO":
					BTN_SHOW_RAIDBOSS_INFO =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TRANSFORMACION":
					BTN_SHOW_TRANSFORMATION = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VOTE_CH":
					BTN_SHOW_VOTE_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUFFER_CH":
					BTN_SHOW_BUFFER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TELEPORT_CH":
					BTN_SHOW_TELEPORT_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SHOP_CH":
					BTN_SHOW_SHOP_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_WAREHOUSE_CH":
					BTN_SHOW_WAREHOUSE_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_CH":
					BTN_SHOW_AUGMENT_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SUBCLASES_CH":
					BTN_SHOW_SUBCLASES_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLASS_TRANSFER_CH":
					BTN_SHOW_CLASS_TRANSFER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CONFIG_PANEL_CH":
					BTN_SHOW_CONFIG_PANEL_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DROP_SEARCH_CH":
					BTN_SHOW_DROP_SEARCH_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PVPPK_LIST_CH":
					BTN_SHOW_PVPPK_LIST_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_LOG_PELEAS_CH":
					BTN_SHOW_LOG_PELEAS_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CASTLE_MANAGER_CH":
					BTN_SHOW_CASTLE_MANAGER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DESAFIO_CH":
					BTN_SHOW_DESAFIO_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SYMBOL_MARKET_CH":
					BTN_SHOW_SYMBOL_MARKET_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLANALLY_CH":
					BTN_SHOW_CLANALLY_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PARTYFINDER_CH":
					BTN_SHOW_PARTYFINDER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_FLAGFINDER_CH":
					BTN_SHOW_FLAGFINDER_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_COLORNAME_CH":
					BTN_SHOW_COLORNAME_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DELEVEL_CH":
					BTN_SHOW_DELEVEL_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_REMOVE_ATRIBUTE_CH":
					BTN_SHOW_REMOVE_ATRIBUTE_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUG_REPORT_CH":
					BTN_SHOW_BUG_REPORT_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DONATION_CH":
					BTN_SHOW_DONATION_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_PJ_CH":
					BTN_SHOW_CAMBIO_NOMBRE_PJ_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH":
					BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VARIAS_OPCIONES_CH":
					BTN_SHOW_VARIAS_OPCIONES_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ELEMENT_ENHANCED_CH":
					BTN_SHOW_ELEMENT_ENHANCED_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ENCANTAMIENTO_ITEM_CH":
					BTN_SHOW_ENCANTAMIENTO_ITEM_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_SPECIAL_CH":
					BTN_SHOW_AUGMENT_SPECIAL_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_GRAND_BOSS_STATUS_CH":
					BTN_SHOW_GRAND_BOSS_STATUS_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_RAIDBOSS_INFO_CH":
					BTN_SHOW_RAIDBOSS_INFO_CH =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TRANSFORMACION_CH":
					BTN_SHOW_TRANSFORMATION_CH = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_TOPZONE":
					VOTO_REWARD_TOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_HOPZONE":
					VOTO_REWARD_HOPZONE = rss.getString(2);
					break;
				case "VOTE_ATTEMPTS_ALLOWED":
					VOTE_ATTEMPTS_ALLOWED = Integer.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_ACTIVO_TOPZONE":
					VOTO_REWARD_ACTIVO_TOPZONE =Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_ACTIVO_HOPZONE":
					VOTO_REWARD_ACTIVO_HOPZONE =Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_SEG_ESPERA":
					VOTO_REWARD_SEG_ESPERA =Integer.valueOf(rss.getString(2));
					break;
				case "VOTO_ITEM_BUFF_ENCHANT_PRICE":
					VOTO_ITEM_BUFF_ENCHANT_PRICE =rss.getString(2);
					break;
				case "TELEPORT_PRICE":
					TELEPORT_PRICE =rss.getString(2);
					break;
				case "FREE_TELEPORT":
					FREE_TELEPORT =Boolean.valueOf(rss.getString(2));
					break;
				case "DESAFIO_85_PREMIO":
					DESAFIO_85_PREMIO =rss.getString(2);
					break;
				case "DESAFIO_NOBLE_PREMIO":
					DESAFIO_NOBLE_PREMIO =rss.getString(2);
					break;
				case "DESAFIO_NPC_BUSQUEDAS":
					String[] npcBusq = rss.getString(2).split(",");
					DESAFIO_NPC_BUSQUEDAS.clear();
					for (String NpcID : npcBusq)
					{
						if(opera.isNumeric(NpcID)) {
							DESAFIO_NPC_BUSQUEDAS.add(Integer.parseInt(NpcID));
						}
					}
					Collections.sort(DESAFIO_NPC_BUSQUEDAS);
					break;
				case "DESAFIO_MAX_LVL85":
					DESAFIO_MAX_LVL85 = Integer.valueOf(rss.getString(2));
					break;
				case "DESAFIO_MAX_NOBLE":
					DESAFIO_MAX_NOBLE = Integer.valueOf(rss.getString(2));
					break;
				case "DESAFIO_LVL85":
					DESAFIO_LVL85 = Boolean.valueOf(rss.getString(2));
					break;
				case "DESAFIO_NOBLE":
					DESAFIO_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "DESAFIO_NPC":
					DESAFIO_NPC = Boolean.valueOf(rss.getString(2));
					break;
				case "DROP_SEARCH_COBRAR_TELEPORT":
					DROP_SEARCH_TELEPORT_FOR_FREE = Boolean.valueOf(rss.getString(2));
					break;
				case "DROP_TELEPORT_COST":
					DROP_TELEPORT_COST =rss.getString(2);
					break;
				case "DROP_SEARCH_MOSTRAR_LISTA":
					DROP_SEARCH_MOSTRAR_LISTA = Integer.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_PRICE":
					PARTY_FINDER_PRICE =rss.getString(2);
					break;
				case "PARTY_FINDER_GO_LEADER_DEATH":
					PARTY_FINDER_GO_LEADER_DEATH =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_NOBLE":
					PARTY_FINDER_GO_LEADER_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_FLAGPK":
					PARTY_FINDER_GO_LEADER_FLAGPK =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_PK":
					PARTY_FINDER_CAN_USE_PK =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_FLAG":
					PARTY_FINDER_CAN_USE_FLAG =Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_LVL":
					PARTY_FINDER_CAN_USE_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_PRICE":
					FLAG_FINDER_PRICE =rss.getString(2);
					break;
				case "FLAG_FINDER_CAN_USE_FLAG":
					FLAG_FINDER_CAN_USE_FLAG =Boolean.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CAN_USE_PK":
					FLAG_FINDER_CAN_USE_PK =Boolean.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CAN_NOBLE":
					FLAG_FINDER_CAN_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_LVL":
					FLAG_FINDER_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_PVP_PK_LVL_MIN":
					FLAG_PVP_PK_LVL_MIN =Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CAN_GO_CASTLE":
					FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE = Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_ON_ASEDIO":
					PARTY_FINDER_GO_LEADER_ON_ASEDIO = Boolean.valueOf(rss.getString(2));
					break;
				case "PINTAR_PRICE":
					PINTAR_PRICE =rss.getString(2);
					break;
				case "PINTAR_COLORS":
					PINTAR_MATRIZ = rss.getString(2);
					break;
				case "AUGMENT_ITEM_PRICE":
					AUGMENT_ITEM_PRICE =rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_PRICE":
					AUGMENT_SPECIAL_PRICE =rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_x_PAGINA":
					AUGMENT_SPECIAL_x_PAGINA =Integer.valueOf(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_NOBLE":
					AUGMENT_SPECIAL_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_LVL":
					AUGMENT_SPECIAL_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_ITEM_PRICE":
					ENCHANT_ITEM_PRICE =rss.getString(2);
					break;
				case "ENCHANT_NOBLE":
					ENCHANT_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "ENCHANT_LVL":
					ENCHANT_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_MIN_ENCHANT":
					ENCHANT_MIN_ENCHANT =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_MAX_ENCHANT":
					ENCHANT_MAX_ENCHANT =Integer.valueOf(rss.getString(2));
					break;
				case "ENCHANT_x_VEZ":
					ENCHANT_x_VEZ =Integer.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_TELEPORT_PRICE":
					RAIDBOSS_INFO_TELEPORT_PRICE =rss.getString(2);
					break;
				case "RAIDBOSS_INFO_LISTA_X_HOJA":
					RAIDBOSS_INFO_LISTA_X_HOJA =Integer.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_TELEPORT":
					RAIDBOSS_INFO_TELEPORT = Boolean.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_NOBLE":
					RAIDBOSS_INFO_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_INFO_LVL":
					RAIDBOSS_INFO_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_SEXO_ITEM_PRICE":
					OPCIONES_CHAR_SEXO_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_NOBLE_ITEM_PRICE":
					OPCIONES_CHAR_NOBLE_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_LVL85_ITEM_PRICE":
					OPCIONES_CHAR_LVL85_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE":
					OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE":
					OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE":
					OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_LVL":
					OPCIONES_CHAR_CAMBIO_NOMBRE_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL":
					OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_SEXO":
					OPCIONES_CHAR_SEXO = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_NOBLE":
					OPCIONES_CHAR_NOBLE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_LVL85":
					OPCIONES_CHAR_LVL85 = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO":
					OPCIONES_CHAR_BUFFER_AIO = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_PRICE":
					OPCIONES_CHAR_BUFFER_AIO_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_LVL":
					OPCIONES_CHAR_BUFFER_AIO_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "ELEMENTAL_ITEM_PRICE":
					ELEMENTAL_ITEM_PRICE = rss.getString(2);
					break;
				case "ELEMENTAL_NOBLE":
					ELEMENTAL_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "ELEMENTAL_LVL":
					ELEMENTAL_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "DELEVEL":
					OPCIONES_CHAR_DELEVEL = Boolean.valueOf(rss.getString(2));
					break;
				case "DELEVEL_PRICE":
					DELEVEL_PRICE = rss.getString(2);
					break;
				case "DELEVEL_LVL_MAX":
					DELEVEL_LVL_MAX =Integer.valueOf(rss.getString(2));
					break;
				case "DELEVEL_NOBLE":
					DELEVEL_NOBLE =Boolean.valueOf(rss.getString(2));
					break;
				case "RATE_EXP_OFF":
					RATE_EXP_OFF =Boolean.valueOf(rss.getString(2));
					break;
				case "SHOW_MY_STAT":
					SHOW_MY_STAT = Boolean.valueOf(rss.getString(2));
					break;
				case "LOG_FIGHT_PVP_PK":
					LOG_FIGHT_PVP_PK =Boolean.valueOf(rss.getString(2));
					break;
				case "ENCHANT_ANNOUCEMENT":
					final String[] EnchantForAnnoucement = rss.getString(2).split(",");
					ENCHANT_ANNOUCEMENT = null;
					ENCHANT_ANNOUCEMENT = new int[EnchantForAnnoucement.length];
					for (int i = 0; i < EnchantForAnnoucement.length; i++)
					{
						if(opera.isNumeric(EnchantForAnnoucement[i])){
							ENCHANT_ANNOUCEMENT[i] = Integer.parseInt(EnchantForAnnoucement[i]);
						}else{
							_log.warning("ZEUS-> Please Check the Enchant Annoucement. This value  " + EnchantForAnnoucement[i] + " is not Numeric.");
						}
					}
					Arrays.sort(ENCHANT_ANNOUCEMENT);
					break;
				case "PVP_PK_PROTECTION_LVL":
					PVP_PK_PROTECTION_LVL =Integer.valueOf(rss.getString(2));
					break;
				case "ALLOW_BLESSED_ESCAPE_PVP":
					ALLOW_BLESSED_ESCAPE_PVP = Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_PK_GRAFICAL_EFFECT":
					PVP_PK_GRAFICAL_EFFECT =Boolean.valueOf(rss.getString(2));
					break;
				case "ANNOUCE_RAID_BOS_STATUS":
					ANNOUCE_RAID_BOS_STATUS =Boolean.valueOf(rss.getString(2));
					break;
				case "RAID_ANNOUCEMENT_DIED":
					RAID_ANNOUCEMENT_DIED =rss.getString(2);
					break;
				case "RAID_ANNOUCEMENT_LIFE":
					RAID_ANNOUCEMENT_LIFE =rss.getString(2);
					break;
				case "RAID_ANNOUCEMENT_ID_ANNOUCEMENT":
					RAID_ANNOUCEMENT_ID_ANNOUCEMENT = Integer.valueOf(rss.getString(2));
					break;
				case "ANNOUCE_TOP_PPVPPK_ENTER":
					ANNOUCE_TOP_PPVPPK_ENTER =Boolean.valueOf(rss.getString(2));
					break;
				case "MENSAJE_ENTRADA_PJ_TOPPVPPK":
					MENSAJE_ENTRADA_PJ_TOPPVPPK =rss.getString(2);
					break;
				case "ANNOUCE_PJ_KARMA":
					ANNOUCE_PJ_KARMA =Boolean.valueOf(rss.getString(2));
					break;
				case "MENSAJE_ENTRADA_PJ_KARMA":
					MENSAJE_ENTRADA_PJ_KARMA =rss.getString(2);
					break;
				case "ANNOUCE_PJ_KARMA_CANTIDAD":
					ANNOUCE_PJ_KARMA_CANTIDAD =Integer.valueOf(rss.getString(2));
					break;
				case "ANNOUCE_CLASS_OPONENT_OLY":
					ANNOUCE_CLASS_OPONENT_OLY =Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_NOBLE":
					TRANSFORMATION_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_LVL":
					TRANSFORMATION_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_PRICE":
					TRANSFORMATION_PRICE = rss.getString(2);
					break;
				case "TRANSFORM_ESPECIALES":
					TRANSFORMATION_ESPECIALES = Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_RAIDBOSS":
					TRANSFORMATION_RAIDBOSS = Boolean.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_ESPECIALES_PRICE":
					TRANSFORMATION_ESPECIALES_PRICE = rss.getString(2);
					break;
				case "TRANSFORM_RAIDBOSS_PRICE":
					TRANSFORMATION_RAIDBOSS_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_FAME":
					OPCIONES_CHAR_FAME = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_FAME_PRICE":
					OPCIONES_CHAR_FAME_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_FAME_NOBLE":
					OPCIONES_CHAR_FAME_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_FAME_LVL":
					OPCIONES_CHAR_FAME_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_FAME_GIVE":
					OPCIONES_CHAR_FAME_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "OLY_ANTIFEED_CHANGE_TEMPLATE":
					OLY_ANTIFEED_CHANGE_TEMPLATE = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_ANTIFEED_NO_SHOW_NAME_NPC":
					OLY_ANTIFEED_SHOW_NAME_NPC = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_SECOND_SHOW_OPONENTES":
					final String[] SegundosOpoentes = rss.getString(2).split(",");
					OLY_SECOND_SHOW_OPPONET = null;
					OLY_SECOND_SHOW_OPPONET = new int[SegundosOpoentes.length];
					for (int i = 0; i < SegundosOpoentes.length; i++)
					{
						if(opera.isNumeric(SegundosOpoentes[i])){
							OLY_SECOND_SHOW_OPPONET[i] = Integer.parseInt(SegundosOpoentes[i]);
						}else{
							_log.warning("ZEUS-> Please check the Seconds value on the Olympiad show Oponent.  " + SegundosOpoentes[i] + " Is not Numeric");
						}
					}
					Arrays.sort(OLY_SECOND_SHOW_OPPONET);
					break;
				case "OLY_SHOW_NAME_OPONENTES":
					OLY_ANTIFEED_SHOW_NAME_OPPO = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_ALLOW":
					OLY_ALLOW = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_ANNOUNCE_RESULTS":
					OLY_ANNOUNCE_RESULTS = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_ID_ACCESS_POINT_MODIF":
					final String[] ID_ACCESS = rss.getString(2).split(",");
					OLY_ACCESS_ID_MODIFICAR_POINT = null;
					OLY_ACCESS_ID_MODIFICAR_POINT = new int[ID_ACCESS.length];
					for (int i = 0; i < ID_ACCESS.length; i++)
					{
						if(opera.isNumeric(ID_ACCESS[i])){
							OLY_ACCESS_ID_MODIFICAR_POINT[i] = Integer.parseInt(ID_ACCESS[i]);
						}
					}
					Arrays.sort(OLY_ACCESS_ID_MODIFICAR_POINT);
					break;

				case "ACCESS_ID":
					String [] AccSplit =  rss.getString(2).split(",");
					AccessConfig = new int[AccSplit.length];
					int Cont = 0;
					for (String Accs : AccSplit){
						AccessConfig[Cont] = Integer.valueOf(Accs);
						Cont++;
					}
					break;
				case "TELEPORT_BD":
					TELEPORT_BD = Boolean.valueOf(rss.getString(2));
					break;
				case "SERVER_NAME":
					Server_Name = rss.getString(2);
					if(Server_Name.indexOf(" ")>0){
						NOMBRE_NPC = Server_Name; //Server_Name.split(" ")[0];
					}else{
						NOMBRE_NPC = Server_Name;
					}
					break;
				case "SHOP_USE_BD":
					SHOP_USE_BD = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_COMANDO_STATUS":
					ANTIBOT_COMANDO_STATUS = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_OPORTUNIDADES":
					ANTIBOT_OPORTUNIDADES = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MINUTOS_JAIL":
					ANTIBOT_MINUTOS_JAIL = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MOB_DEAD_TO_ACTIVATE":
					ANTIBOT_MOB_DEAD_TO_ACTIVATE = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MINUTE_VERIF_AGAIN":
					ANTIBOT_MINUTE_VERIF_AGAIN = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MINUTOS_ESPERA":
					ANTIBOT_MINUTOS_ESPERA = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_MIN_LVL":
					ANTIBOT_MIN_LVL = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ONLY_NOBLE":
					ANTIBOT_NOBLE_ONLY = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ONLY_HERO":
					ANTIBOT_HERO_ONLY = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ONLY_GM":
					ANTIBOT_GM_ONLY = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ANTIGUEDAD_MINUTOS_MIN":
					ANTIBOT_ANTIGUEDAD_MINUTOS = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ANNOU_JAIL":
					ANTIBOT_ANNOU_JAIL = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_AUTO":
					ANTIBOT_AUTO = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_RESET_COUNT":
					ANTIBOT_RESET_COUNT = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_CHECK_IP_INTERNET":
					BANIP_CHECK_IP_INTERNET = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_CHECK_IP_RED":
					BANIP_CHECK_IP_RED = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_STATUS":
					BANIP_STATUS = Boolean.valueOf(rss.getString(2));
					break;
				case "BANIP_DISCONNECT_ALL_PLAYER":
					BANIP_DISCONNECT_ALL_PLAYER = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_COLISEUM_NPC_ID":
					EVENT_COLISEUM_NPC_ID = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM":
					VOTE_SHOW_ZEUS_ONLY_BY_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_ID":
					VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_GIVE_TEMPORAL":
					VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_ID_TEMPORTAL":
					VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_SHOW_ONLY_ZEUS_ITEM_TEMPORAL_PRICE":
					VOTE_SHOW_ZEUS_TEMPORAL_PRICE = rss.getString(2);
					break;
				case "ANTIBOT_BORRAR_ITEM":
					ANTIBOT_BORRAR_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_PORCENTAJE":
					ANTIBOT_BORRAR_ITEM_PORCENTAJE = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_ID_BORRAR":
					ANTIBOT_BORRAR_ITEM_ID = rss.getString(2);
					break;
				case "ANTIBOT_CHECK_INPEACE_ZONE":
					ANTIBOT_CHECK_INPEACE_ZONE = Boolean.valueOf(rss.getString(2));
					break;
				case "ANNOUNCE_KARMA_PLAYER_WHEN_KILL":
					ANNOUNCE_KARMA_PLAYER_WHEN_KILL = Boolean.valueOf(rss.getString(2));
					break;
				case "ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN":
					ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN = rss.getString(2);
					break;
				case "PARTY_FINDER_USE_NO_SUMMON_RULEZ":
					PARTY_FINDER_USE_NO_SUMMON_RULEZ = Boolean.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE":
					PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK":
					if(!rss.getString(2).equals("")){
						VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK = Integer.valueOf(rss.getString(2));
					}else{
						VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK = 10;
					}
					break;
				case "VOTO_REWARD_AUTO_RANGO_PREMIAR":
					if(rss.getString(2).equals("")){
						VOTO_REWARD_AUTO_RANGO_PREMIAR = 10;
					}else{
						VOTO_REWARD_AUTO_RANGO_PREMIAR = Integer.valueOf(rss.getString(2));
					}
					break;
				case "VOTO_REWARD_AUTO_MENSAJE_FALTA":
					VOTO_REWARD_AUTO_MENSAJE_FALTA = rss.getString(2);
					break;
				case "VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA":
					VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA = rss.getString(2);
					break;
				case "VOTE_AUTOREWARD":
					if(onLine){
						boolean tempo = Boolean.parseBoolean(rss.getString(2));
						if(!VOTO_AUTOREWARD && tempo){
							VOTO_AUTOREWARD = Boolean.parseBoolean(rss.getString(2));
							votereward.getInstance().inicializar();
							break;
						}
					}
					VOTO_AUTOREWARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_GIRAN":
					GIRAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_ADEN":
					ADEN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_RUNE":
					RUNE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_OREN":
					OREN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_DION":
					DION = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_GLUDIO":
					GLUDIO = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_GODDARD":
					GODDARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_SCHUTTGART":
					SCHUTTGART = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CASTLE_MANAGER_SHOW_INNADRIL":
					INNADRIL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DROP_SEARCH_SHOW_IDITEM_TO_PLAYER":
					DROP_SEARCH_SHOW_IDITEM_TO_PLAYER =Boolean.parseBoolean(rss.getString(2));
					break;
				case "SHOW_NEW_MAIN_WINDOWS":
					SHOW_NEW_MAIN_WINDOWS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "FLAG_FINDER_PK_PRIORITY":
					FLAG_FINDER_PK_PRIORITY = Boolean.parseBoolean(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_PRICE_PAS":
					AUGMENT_SPECIAL_PRICE_PASSIVE = rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_PRICE_CHA":
					AUGMENT_SPECIAL_PRICE_CHANCE = rss.getString(2);
					break;
				case "AUGMENT_SPECIAL_PRICE_ACT":
					AUGMENT_SPECIAL_PRICE_ACTIVE = rss.getString(2);
					break;
				case "DRESSME_CAN_USE_IN_OLYS":
					DRESSME_CAN_USE_IN_OLYS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_CAN_CHANGE_DRESS_IN_OLY":
					DRESSME_CAN_CHANGE_IN_OLYS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_NEW_DRESS_IS_FREE":
					DRESSME_NEW_DRESS_IS_FREE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "DRESSME_NEW_DRESS_COST":
					DRESSME_NEW_DRESS_COST = rss.getString(2);
					break;
				case "DROP_SEARCH_ID_MOB_NO_TELEPORT":
					DROP_SEARCH_MOB_BLOCK_TELEPORT.clear();
					if(rss.getString(2).length()>0){
						for(String blockNPC : rss.getString(2).split(",")){
							if(opera.isNumeric(blockNPC)){
								DROP_SEARCH_MOB_BLOCK_TELEPORT.add(Integer.valueOf(blockNPC));
							}else{
								_log.warning("Error loading NPC Block to used Teleport in Drop Search. Data: " + blockNPC);
							}
						}
						Collections.sort(DROP_SEARCH_MOB_BLOCK_TELEPORT);
					}
					break;
				case "RAIDBOSS_ID_MOB_NO_TELEPORT":
					RAIDBOSS_ID_MOB_NO_TELEPORT.clear();
					if(rss.getString(2).length()>0){
						for(String blockNPC : rss.getString(2).split(",")){
							if(opera.isNumeric(blockNPC)){
								RAIDBOSS_ID_MOB_NO_TELEPORT.add(Integer.valueOf(blockNPC));
							}else{
								_log.warning("Error loading NPC Block to used Teleport in Raidboss Info. Data: " + blockNPC);
							}
						}
						Collections.sort(RAIDBOSS_ID_MOB_NO_TELEPORT);
					}
					break;
				case "DROP_SEARCH_CAN_USE_TELEPORT":
					DROP_SEARCH_CAN_USE_TELEPORT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RETURN_BUFF":
					RETURN_BUFF = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RETURN_BUFF_MINUTES":
					RETURN_BUFF_SECONDS_TO_RETURN = Integer.valueOf(rss.getString(2));
					break;
				case "RETURN_BUFF_IN_OLY":
					RETURN_BUFF_IN_OLY = Boolean.parseBoolean(rss.getString(2));
					break;
				case "RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN":
					RETURN_BUFF_IN_OLY_SECONDS_TO_RETURN = Integer.valueOf(rss.getString(2));
					break;
				case "RETURN_CANCEL_BUFF_NOT_STEALING":
					String NoRobar = rss.getString(2);
					if(RETURN_BUFF_NOT_STEALING.size()>0){
						RETURN_BUFF_NOT_STEALING.clear();
					}
					if(NoRobar.length()<=0){
						break;
					}
					for(String NoRobaSplit : NoRobar.split(",")){
						if(opera.isNumeric(NoRobaSplit)){
							RETURN_BUFF_NOT_STEALING.add(Integer.valueOf(NoRobaSplit));
						}
					}
					break;
				case "TRADE_WHILE_FLAG":
					TRADE_WHILE_FLAG = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TRADE_WHILE_PK":
					TRADE_WHILE_PK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TELEPORT_CAN_USE_IN_COMBAT_MODE":
					TELEPORT_CAN_USE_IN_COMBAT_MODE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "ANTIBOT_INACTIVE_MINUTES":
					ANTIBOT_INACTIVE_MINUTES = Integer.parseInt(rss.getString(2));
					break;
				case "OLY_ANTIFEED_SHOW_IN_NAME_CLASS":
					OLY_ANTIFEED_SHOW_IN_NAME_CLASS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OLY_ANTIFEED_SHOW_IN_TITLE_CLASS":
					OLY_ANTIFEED_SHOW_IN_TITLE_CLASS = Boolean.parseBoolean(rss.getString(2));
					break;					
				case "RADIO_PLAYER_NPC_MAXIMO":
					RADIO_PLAYER_NPC_MAXIMO = Integer.valueOf(rss.getString(2));
					break;
				case "SHOW_ZEUS_ENTER_GAME":
					SHOW_ZEUS_ENTER_GAME = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PVP_REWARD":
					PVP_REWARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PVP_PARTY_REWARD":
					PVP_PARTY_REWARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "PVP_REWARD_ITEMS":
					PVP_REWARD_ITEMS = rss.getString(2);
					break;
				case "PVP_PARTY_REWARD_ITEMS":
					PVP_PARTY_REWARD_ITEMS = rss.getString(2);
					break;
				case "DELEVEL_CHECK_SKILL":
					DELEVEL_CHECK_SKILL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "MAX_IP_CHECK":
					MAX_IP_CHECK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "MAX_IP_COUNT":
					MAX_IP_COUNT = Integer.valueOf(rss.getString(2));
					break;
				case "MAX_IP_RECORD_DATA":
					MAX_IP_RECORD_DATA = Boolean.parseBoolean(rss.getString(2));
					break;
				case "MAX_IP_VIP_COUNT":
					MAX_IP_VIP_COUNT = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_SEND_ALL_IP":
					ANTIBOT_SEND_ALL_IP = Boolean.parseBoolean(rss.getString(2));
					break;
				case "ELEMENTAL_LVL_ENCHANT_MAX_WEAPON":
					ELEMENTAL_LVL_ENCHANT_MAX_WEAPON = Integer.valueOf(rss.getString(2));
					break;
				case "ELEMENTAL_LVL_ENCHANT_MAX_ARMOR":
					ELEMENTAL_LVL_ENCHANT_MAX_ARMOR = Integer.valueOf(rss.getString(2));
					break;
				case "CHAR_PANEL":
					CHAR_PANEL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "SHOW_FIXME_WINDOWS":
					SHOW_FIXME_WINDOWS = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD":
					COMMUNITY_BOARD = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PART_EXEC":
					_COMMUNITY_BOARD_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_REGION":
					general.COMMUNITY_BOARD_REGION= Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_REGION_PART_EXEC":
					_COMMUNITY_BOARD_REGION_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_ENGINE":
					general.COMMUNITY_BOARD_ENGINE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_ENGINE_PART_EXEC":
					_COMMUNITY_BOARD_ENGINE_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_ROWS_FOR_PAGE":
					general.COMMUNITY_BOARD_ROWS_FOR_PAGE = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_TOPPLAYER_LIST":
					general.COMMUNITY_BOARD_TOPPLAYER_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_CLAN_LIST":
					general.COMMUNITY_BOARD_CLAN_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_MERCHANT_LIST":
					general.COMMUNITY_BOARD_MERCHANT_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VOTE_CBE":
					_BTN_SHOW_VOTE_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUFFER_CBE":
					_BTN_SHOW_BUFFER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TELEPORT_CBE":
					_BTN_SHOW_TELEPORT_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SHOP_CBE":
					_BTN_SHOW_SHOP_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_WAREHOUSE_CBE":
					_BTN_SHOW_WAREHOUSE_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_CBE":
					_BTN_SHOW_AUGMENT_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SUBCLASES_CBE":
					_BTN_SHOW_SUBCLASES_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLASS_TRANSFER_CBE":
					_BTN_SHOW_CLASS_TRANSFER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CONFIG_PANEL_CBE":
					_BTN_SHOW_CONFIG_PANEL_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DROP_SEARCH_CBE":
					_BTN_SHOW_DROP_SEARCH_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PVPPK_LIST_CBE":
					_BTN_SHOW_PVPPK_LIST_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_LOG_PELEAS_CBE":
					_BTN_SHOW_LOG_PELEAS_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CASTLE_MANAGER_CBE":
					_BTN_SHOW_CASTLE_MANAGER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DRESSME":
					_BTN_SHOW_DRESSME = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DESAFIO_CBE":
					_BTN_SHOW_DESAFIO_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_SYMBOL_MARKET_CBE":
					_BTN_SHOW_SYMBOL_MARKET_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CLANALLY_CBE":
					_BTN_SHOW_CLANALLY_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_PARTYFINDER_CBE":
					_BTN_SHOW_PARTYFINDER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_FLAGFINDER_CBE":
					_BTN_SHOW_FLAGFINDER_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_COLORNAME_CBE":
					_BTN_SHOW_COLORNAME_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DELEVEL_CBE":
					_BTN_SHOW_DELEVEL_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_REMOVE_ATRIBUTE_CBE":
					_BTN_SHOW_REMOVE_ATRIBUTE_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BUG_REPORT_CBE":
					_BTN_SHOW_BUG_REPORT_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_DONATION_CBE":
					_BTN_SHOW_DONATION_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE":
					_BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE":
					_BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_VARIAS_OPCIONES_CBE":
					_BTN_SHOW_VARIAS_OPCIONES_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ELEMENT_ENHANCED_CBE":
					_BTN_SHOW_ELEMENT_ENHANCED_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_ENCANTAMIENTO_ITEM_CBE":
					_BTN_SHOW_ENCANTAMIENTO_ITEM_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUGMENT_SPECIAL_CBE":
					_BTN_SHOW_AUGMENT_SPECIAL_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_GRAND_BOSS_STATUS_CBE":
					_BTN_SHOW_GRAND_BOSS_STATUS_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_RAIDBOSS_INFO_CBE":
					_BTN_SHOW_RAIDBOSS_INFO_CBE =Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_TRANSFORMACION_CBE":
					_BTN_SHOW_TRANSFORMATION_CBE = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_ACT":
					ANTI_OVER_ENCHANT_ACT = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK":
					ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK":
					ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK = Integer.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_MESJ_PUNISH":
					ANTI_OVER_ENCHANT_MESJ_PUNISH = rss.getString(2);
					break;
				case "ANTI_OVER_TYPE_PUNISH":
					ANTI_OVER_TYPE_PUNISH = rss.getString(2);
					break;
				case "ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL":
					ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP":
					ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_MAX_WEAPON":
					ANTI_OVER_ENCHANT_MAX_WEAPON = Integer.valueOf(rss.getString(2));
					break;
				case "ANTI_OVER_ENCHANT_MAX_ARMOR":
					ANTI_OVER_ENCHANT_MAX_ARMOR = Integer.valueOf(rss.getString(2));
					break;
				case "REGISTER_EMAIL_ONLINE":
					REGISTER_EMAIL_ONLINE = Boolean.valueOf(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_WAITING_TIME":
					REGISTER_NEW_PLAYER_WAITING_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME":
					REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "REGISTER_NEW_PlAYER_TRIES":
					REGISTER_NEW_PlAYER_TRIES = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_CLAN_PART_EXEC":
					_COMMUNITY_BOARD_CLAN_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_CLAN":
					COMMUNITY_BOARD_CLAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_CLAN_ROWN_LIST":
					COMMUNITY_BOARD_CLAN_ROWN_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN":
					COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN = Boolean.parseBoolean(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_BLOCK_CHAT":
					REGISTER_NEW_PLAYER_BLOCK_CHAT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT":
					REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CHAR_CAMBIO_NOMBRE":
					OPCIONES_CHAR_CAMBIO_NOMBRE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "OPCIONES_CLAN_CAMBIO_NOMBRE":
					OPCIONES_CLAN_CAMBIO_NOMBRE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_REGION_PLAYER_ON_LIST":
					COMMUNITY_BOARD_REGION_PLAYER_ON_LIST = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_SECONDS_TO_RESEND_ANTIBOT":
					ANTIBOT_SECONDS_TO_RESEND_ANTIBOT = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_CHECK_DUALBOX":
					ANTIBOT_CHECK_DUALBOX = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_RAID_ID":
					String tempEVENT_RAIDBOSS_RAID_ID = rss.getString(2);
					if(tempEVENT_RAIDBOSS_RAID_ID.length()>0){
						EVENT_RAIDBOSS_RAID_ID.clear();
						for(String strNpcId : tempEVENT_RAIDBOSS_RAID_ID.split(",")){
							try{
								EVENT_RAIDBOSS_RAID_ID.add(Integer.valueOf(strNpcId));
							}catch(Exception a){
								
							}
						}
					}
					break;
				case "EVENT_RAIDBOSS_RAID_POSITION":
					EVENT_RAIDBOSS_RAID_POSITION = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_PLAYER_POSITION":
					EVENT_RAIDBOSS_PLAYER_POSITION = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_PLAYER_INMOBIL":
					EVENT_RAIDBOSS_PLAYER_INMOBIL = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_REWARD_WIN":
					EVENT_RAIDBOSS_REWARD_WIN = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_REWARD_LOOSER":
					EVENT_RAIDBOSS_REWARD_LOOSER = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_PLAYER_MIN_LEVEL":
					EVENT_RAIDBOSS_PLAYER_MIN_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_PLAYER_MAX_LEVEL":
					EVENT_RAIDBOSS_PLAYER_MAX_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_PLAYER_MIN_REGIS":
					EVENT_RAIDBOSS_PLAYER_MIN_REGIS = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_PLAYER_MAX_REGIS":
					EVENT_RAIDBOSS_PLAYER_MAX_REGIS = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_SECOND_TO_BACK":
					EVENT_RAIDBOSS_SECOND_TO_BACK = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_JOINTIME":
					EVENT_RAIDBOSS_JOINTIME = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_EVENT_TIME":
					EVENT_RAIDBOSS_EVENT_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_COLORNAME":
					EVENT_RAIDBOSS_COLORNAME = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_CHECK_DUALBOX":
					EVENT_RAIDBOSS_CHECK_DUALBOX = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_CANCEL_BUFF":
					EVENT_RAIDBOSS_CANCEL_BUFF = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_UNSUMMON_PET":
					EVENT_RAIDBOSS_UNSUMMON_PET = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_SECOND_TO_REVIVE":
					EVENT_RAIDBOSS_SECOND_TO_REVIVE = Integer.parseInt(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS":
					EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS = Integer.parseInt(rss.getString(2));
					break;
				case "ANTIBOT_SEND_JAIL_ALL_DUAL_BOX":
					ANTIBOT_SEND_JAIL_ALL_DUAL_BOX = Boolean.parseBoolean(rss.getString(2));
					break;
				case "ANTIFEED_ENCHANT_SKILL_REUSE":
					ANTIFEED_ENCHANT_SKILL_REUSE = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_HOUR_TO_START":
					EVENT_RAIDBOSS_HOUR_TO_START = rss.getString(2);
					break;
				case "EVENT_RAIDBOSS_AUTOEVENT":
					EVENT_RAIDBOSS_AUTOEVENT = Boolean.parseBoolean(rss.getString(2));
					break;
				case "CAN_USE_BSOE_PK":
					CAN_USE_BSOE_PK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "FLAG_FINDER_MIN_PVP_FROM_TARGET":
					FLAG_FINDER_MIN_PVP_FROM_TARGET = Integer.valueOf(rss.getString(2));
					break;
				case "PREMIUM_MESSAGE":
					PREMIUM_MESSAGE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "VOTO_REWARD_AUTO_REWARD_META_HOPZONE":
					VOTO_REWARD_AUTO_REWARD_META_HOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_AUTO_REWARD_META_TOPZONE":
					VOTO_REWARD_AUTO_REWARD_META_TOPZONE = rss.getString(2);
					break;
				case "VOTO_REWARD_AUTO_AFK_CHECK":
					VOTO_REWARD_AUTO_AFK_CHECK = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TRANSFORM_TIME":
					TRANSFORM_TIME = Boolean.parseBoolean(rss.getString(2));
					break;
				case "TRANSFORM_TIME_MINUTES":
					TRANSFORM_TIME_MINUTES = Integer.valueOf(rss.getString(2));
					break;
				case "TRANSFORM_REUSE_TIME_MINUTES":
					TRANSFORM_REUSE_TIME_MINUTES = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_REGION_LEGEND":
					COMMUNITY_REGION_LEGEND.clear();
					String RegionLegend = rss.getString(2);
					for(String parte : RegionLegend.split(",")){
						COMMUNITY_REGION_LEGEND.add(parte.split(":")[0]);
					}
					break;
				case "PVP_PK_PROTECTION_LIFETIME_MINUTES":
					PVP_PK_PROTECTION_LIFETIME_MINUTES = Integer.valueOf(rss.getString(2));
					break;
				case "PVP_REWARD_CHECK_DUALBOX":
					PVP_REWARD_CHECK_DUALBOX = Boolean.parseBoolean(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_MINUTE_INTERVAL":
					EVENT_RAIDBOSS_MINUTE_INTERVAL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER":
					EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER = Integer.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_BLACK_LIST":
					ANTIBOT_BLACK_LIST = Boolean.valueOf(rss.getString(2));
					break;
				case "ANTIBOT_BLACK_LIST_MULTIPLIER":
					ANTIBOT_BLACK_LIST_MULTIPLIER = Integer.valueOf(rss.getString(2));
					break;
				case "OLY_DUAL_BOX_CONTROL":
					OLY_DUAL_BOX_CONTROL = Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_REWARD_RANGE":
					PVP_REWARD_RANGE = Integer.valueOf(rss.getString(2));
					break;
				case "PARTY_FINDER_CAN_USE_ONLY_NOBLE":
					PARTY_FINDER_CAN_USE_ONLY_NOBLE = Boolean.valueOf(rss.getString(2));
					break;
				case "TELEPORT_FOR_FREE_UP_TO_LEVEL":
					TELEPORT_FOR_FREE_UP_TO_LEVEL = Boolean.valueOf(rss.getString(2));
					break;
				case "TELEPORT_FOR_FREE_UP_TO_LEVEL_LV":
					TELEPORT_FOR_FREE_UP_TO_LEVEL_LV = Integer.valueOf(rss.getString(2));
					break;
				case "VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER":
					VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_CLAN":
					EVENT_REPUTATION_CLAN = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_CLAN_ID_NPC":
					EVENT_REPUTATION_CLAN_ID_NPC = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_LVL_TO_GIVE":
					EVENT_REPUTATION_LVL_TO_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_MIN_PLAYER":
					EVENT_REPUTATION_MIN_PLAYER = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE":
					EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_REPUTATION_REPU_TO_GIVE":
					EVENT_REPUTATION_REPU_TO_GIVE = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_GENERAL_BLOCK":
					CHAT_GENERAL_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_SHOUT_BLOCK":
					CHAT_SHOUT_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_BLOCK":
					CHAT_TRADE_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_BLOCK":
					CHAT_WISP_BLOCK = Boolean.valueOf(rss.getString(2));
					break;
				case "CHAT_GENERAL_NEED_PVP":
					CHAT_GENERAL_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;					
				case "CHAT_SHOUT_NEED_PVP":
					CHAT_SHOUT_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_GENERAL_NEED_LEVEL":
					CHAT_GENERAL_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;					
				case "CHAT_SHOUT_NEED_LEVEL":
					CHAT_SHOUT_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_GENERAL_NEED_LIFETIME":
					CHAT_GENERAL_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;					
				case "CHAT_SHOUT_NEED_LIFETIME":
					CHAT_SHOUT_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_NEED_PVP":
					CHAT_TRADE_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_NEED_LEVEL":
					CHAT_TRADE_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_TRADE_NEED_LIFETIME":
					CHAT_TRADE_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_NEED_PVP":
					CHAT_WISP_NEED_PVP = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_NEED_LEVEL":
					CHAT_WISP_NEED_LEVEL = Integer.valueOf(rss.getString(2));
					break;
				case "CHAT_WISP_NEED_LIFETIME":
					CHAT_WISP_NEED_LIFETIME = Integer.valueOf(rss.getString(2));
					break;
				case "FLAG_FINDER_CHECK_CLAN":
					FLAG_FINDER_CHECK_CLAN = Boolean.valueOf(rss.getBoolean(2));
					break;
				case "EVENT_TOWN_WAR_AUTOEVENT":
					EVENT_TOWN_WAR_AUTOEVENT = Boolean.valueOf(rss.getBoolean(2));
					break;
				case "EVENT_TOWN_WAR_MINUTES_START_SERVER":
					EVENT_TOWN_WAR_MINUTES_START_SERVER = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_MINUTES_INTERVAL":
					EVENT_TOWN_WAR_MINUTES_INTERVAL = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_CITY_ON_WAR":
					EVENT_TOWN_WAR_CITY_ON_WAR = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_MINUTES_EVENT":
					EVENT_TOWN_WAR_MINUTES_EVENT = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_JOIN_TIME":
					EVENT_TOWN_WAR_JOIN_TIME = Integer.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_GIVE_PVP_REWARD":
					EVENT_TOWN_WAR_GIVE_PVP_REWARD = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER":
					EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_REWARD_GENERAL":
					EVENT_TOWN_WAR_REWARD_GENERAL = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_REWARD_TOP_PLAYER":
					EVENT_TOWN_WAR_REWARD_TOP_PLAYER = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_RANDOM_CITY":
					EVENT_TOWN_WAR_RANDOM_CITY = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_DUAL_BOX_CHECK":
					EVENT_TOWN_WAR_DUAL_BOX_CHECK = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_HIDE_NPC":
					EVENT_TOWN_WAR_HIDE_NPC = Boolean.valueOf(rss.getString(2));
					break;
				case "EVENT_TOWN_WAR_NPC_ID_HIDE":
					EVENT_TOWN_WAR_NPC_ID_HIDE = rss.getString(2);
					break;
				case "EVENT_TOWN_WAR_CAN_USE_BUFFER":
					EVENT_TOWN_WAR_CAN_USE_BUFFER = Boolean.valueOf(rss.getString(2));
					break;
				case "VOTE_EVERY_12_HOURS":
					VOTE_EVERY_12_HOURS = Boolean.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_GRAND_RB_EXEC":
					_COMMUNITY_BOARD_GRAND_RB_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_GRAND_RB":
					COMMUNITY_BOARD_GRAND_RB = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BLACKSMITH_CBE":
					_BTN_SHOW_BLACKSMITH_CBE = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_30":
					OPCIONES_CHAR_BUFFER_AIO_30 = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_BUFFER_AIO_PRICE_30":
					OPCIONES_CHAR_BUFFER_AIO_PRICE_30 = rss.getString(2);
					break;
				case "BTN_SHOW_PARTYMATCHING_CBE":
					_BTN_SHOW_PARTYMATCHING_CBE = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER":
					COMMUNITY_BOARD_PARTYFINDER = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_DONATION":
					COMMUNITY_BOARD_DONATION = Boolean.parseBoolean(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_EXEC":
					_COMMUNITY_BOARD_PARTYFINDER_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_DONATION_PART_EXEC":
					_COMMUNITY_BOARD_DONATION_PART_EXEC = rss.getString(2);
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE":
					COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST":
					COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD":
					COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD = Integer.valueOf(rss.getString(2));
					break;
				case "COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE":
					COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_AUCTIONHOUSE_CBE":
					_BTN_SHOW_AUCTIONHOUSE_CBE = Boolean.valueOf(rss.getString(2));
					break;
				case "BTN_SHOW_BIDHOUSE_CBE":
					_BTN_SHOW_BIDHOUSE_CBE = Boolean.valueOf(rss.getString(2));
					break;
				case "RAIDBOSS_OBSERVE_MODE":
					RAIDBOSS_OBSERVE_MODE = Boolean.valueOf(rss.getString(2));
					break;
				case "DROP_SEARCH_OBSERVE_MODE":
					DROP_SEARCH_OBSERVE_MODE = Boolean.valueOf(rss.getString(2));
					break;
				case "OLY_CAN_USE_SCHEME_BUFFER":
					OLY_CAN_USE_SCHEME_BUFFER = Boolean.valueOf(rss.getString(2));
					break;
				case "DROP_SEARCH_SHIFT_NPC_SHOW":
					DROP_SEARCH_SHIFT_NPC_SHOW = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_REDUCE_PK_PRICE":
					OPCIONES_CHAR_REDUCE_PK_PRICE = rss.getString(2);
					break;
				case "OPCIONES_CHAR_REDUCE_PK":
					OPCIONES_CHAR_REDUCE_PK = Boolean.valueOf(rss.getString(2));
					break;
				case "OPCIONES_CHAR_REDUCE_AMOUNT":
					OPCIONES_CHAR_REDUCE_AMOUNT = Integer.valueOf(rss.getString(2));
					break;
				case "INSTANCE_ZONE_CLEAR":
					INSTANCE_ZONE_CLEAR = Boolean.valueOf(rss.getString(2));
					break;
				case "INSTANCE_ZONE_COST":
					INSTANCE_ZONE_COST = rss.getString(2);
					break;
				case "PVP_CLAN_REPUTATION_REWARD":
					PVP_CLAN_REPUTATION_REWARD = Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_CLAN_REPUTATION_AMOUNT":
					PVP_CLAN_REPUTATION_AMOUNT = Integer.valueOf(rss.getString(2));
					break;
				case "JAIL_BAIL_STATUS":
					JAIL_BAIL_STATUS = Boolean.valueOf(rss.getString(2));
					break;
				case "JAIL_BAIL_COST":
					JAIL_BAIL_COST = String.valueOf(rss.getString(2));
					break;
				case "JAIL_BAIL_MULTIPLE_COST":
					JAIL_BAIL_MULTIPLE_COST = Integer.valueOf(rss.getString(2));
					break;
				case "JAIL_BAIL_BLACKLIST_MULTIPLE":
					JAIL_BAIL_BLACKLIST_MULTIPLE = Boolean.valueOf(rss.getString(2));
					break;
				case "PVP_COUNT_ALLOW_SIEGES_FORTRESS":
					PVP_COUNT_ALLOW_SIEGES_FORTRESS = Boolean.valueOf(rss.getString(2));
					break;
				case "ZEUS_OLY_SHOW_TIMER":
					ZEUS_OLY_COUNTER = Boolean.valueOf(rss.getString(2));
					break;
				case "ZEUS_OLY_SHOW_DMG":
					ZEUS_OLY_SHOW_DMG = Boolean.valueOf(rss.getString(2));
					break;
				case "AUGMENT_SPECIAL_PRICE_SYMBOL":
					AUGMENT_SPECIAL_PRICE_SYMBOL = String.valueOf(rss.getString(2));
					break;
				case "ZEUS_AUTOPOTS_CP":
					ZEUS_AUTOPOTS_CP = Boolean.valueOf(rss.getString(2));
					break;
				case "ZEUS_AUTOPOTS_MP":
					ZEUS_AUTOPOTS_MP = Boolean.valueOf(rss.getString(2));
					break;
				case "ZEUS_AUTOPOTS_HP":
					ZEUS_AUTOPOTS_HP = Boolean.valueOf(rss.getString(2));
					break;
				case "ZEUS_AUTOPOTS_CHECK_MILISECOND":
					int Temp = Integer.valueOf( Integer.valueOf(rss.getInt(2)));
					if(Temp <500){
						ZEUS_AUTOPOTS_CHECK_MILISECOND = 500;
					}else{
						ZEUS_AUTOPOTS_CHECK_MILISECOND = Temp;
					}
					break;
				case "STATUS_BUFFER":
					STATUS_BUFFER = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_TELEPORT":
					STATUS_TELEPORT = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_SHOP":
					STATUS_SHOP = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_WAREHOUSE":
					STATUS_WAREHOUSE = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_AUGMENT":
					STATUS_AUGMENT = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_SUBCLASES":
					STATUS_SUBCLASES = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_CLASS_TRANSFER":
					STATUS_CLASS_TRANSFER = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_CONFIG_PANEL":
					STATUS_CONFIG_PANEL = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_DROP_SEARCH":
					STATUS_DROP_SEARCH = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_PVPPK_LIST":
					STATUS_PVPPK_LIST = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_LOG_PELEAS":
					STATUS_LOG_PELEAS = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_CASTLE_MANAGER":
					STATUS_CASTLE_MANAGER = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_SYMBOL_MARKET":
					STATUS_SYMBOL_MARKET = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_PARTYFINDER":
					STATUS_PARTYFINDER = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_FLAGFINDER":
					STATUS_FLAGFINDER = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_DELEVEL":
					STATUS_DELEVEL = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_REMOVE_ATRIBUTE":
					STATUS_REMOVE_ATRIBUTE = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_BUG_REPORT":
					STATUS_BUG_REPORT = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_VARIAS_OPCIONES":
					STATUS_VARIAS_OPCIONES = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_ELEMENT_ENHANCED":
					STATUS_ELEMENT_ENHANCED = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_ENCANTAMIENTO_ITEM":
					STATUS_ENCANTAMIENTO_ITEM = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_AUGMENT_SPECIAL":
					STATUS_AUGMENT_SPECIAL = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_GRAND_BOSS_STATUS":
					STATUS_GRAND_BOSS_STATUS = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_RAIDBOSS_INFO":
					STATUS_RAIDBOSS_INFO = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_TRANSFORMACION":
					STATUS_TRANSFORMACION = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_BLACKSMITH":
					STATUS_BLACKSMITH = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_PARTYMATCHING":
					STATUS_PARTYMATCHING = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_AUCTIONHOUSE":
					STATUS_AUCTIONHOUSE = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_BIDHOUSE":
					STATUS_BIDHOUSE = Boolean.valueOf(rss.getString(2));
					break;
				case "STATUS_DRESSME":
					STATUS_DRESSME = Boolean.valueOf(rss.getString(2));
					break;
				}
			}
		}catch(SQLException a){
			_log.warning("ZeuS Error CARGADO->"+a.getMessage());
		}finally{
			
		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}
		
		try{
			language.getInstance().loadCentralLang();
		}catch(Exception a){
			_log.warning("Error loading HTM language system");
		}
		
		loadPropertyWeb();
		if(!onLine){
			loadAllDropList();
			try{
				TownWarEvent.getInstance().AutoEventStart(true);
			}catch(Exception e){
				
			}
		}
		
		try {
			loadPaypal();
		}catch(Exception a) {
			
		}
		
		try{
			loadNpcCommunity();
		}catch(Exception a){
			
		}
		
		try{
			jMail.Load();
		}catch(Exception a){
			
		}
		
		try{
			loadPremiumServices();
		}catch(Exception a){
			_log.warning("Error loading premium data->" + a.getMessage());
		}		
		
		try{
			loadDonationConfig();
		}catch(Exception a){
			
		}
		
		try{
			v_Teleport.loadTeleportData();
		}catch(Exception a){

		}
		try{
			loadShopData();
		}catch(Exception a){

		}
		try{
			loadPreguntasBot();
		}catch(Exception a){

		}

		try{
			loadIPBLOCK();
		}catch(Exception a){

		}

		try{
			loadZeuSPremium();
		}catch(Exception a){

		}

		try{
			checkDualBoxData();
		}catch(Exception a){

		}
		
		try{
			custom_drop_system.loadCustomDrop();
		}catch(Exception a){
			
		}
		
		try{
			loadBuffCB_Scheme();
		}catch(Exception a){
			_log.warning("Error loading Scheme Community Buff->" + a.getMessage());
		}
		
		try{
			loadPvPPkColor();
		}catch(Exception b){
			_log.warning("Error loading pvp/pk Color System ->" + b.getMessage());
		}
		
		try{
			loadKillingSpree();
		}catch(Exception b){
			_log.warning("Error loading Spree System ->" + b.getMessage());
		}
		
		try{
			aioChar.loadBuffForAio();
		}catch(Exception a){
			_log.warning("Error loading Aio Buff Char Creation System ->" + a.getMessage());
		}
		
		try{
			loadAntibotIcons();
		}catch(Exception a){
			
		}
		
		try{
			loadBuffCB_buffs();
		}catch(Exception a){
			_log.warning("Error loading Community Buff->" + a.getMessage());
		}
		
		try{
			loadLevelUpSpotWindows();
		}catch(Exception a){
			_log.warning("Error loading Level Up Spot Windows->" + a.getMessage());
		}
		
		try{
			loadAdvencedClassMaster();
		}catch(Exception a){
			_log.warning("Error loading Advanced Class Master->" + a.getMessage());
		}
		
		try{
			Comunidad.loadAllMsjeCommunity();
		}catch(Exception a){
			_log.warning("Error loading Community Message -> " + a.getMessage());
		}
		
		try{
			pvpInstance.XMLLoad();
		}catch(Exception a){
			_log.warning("Error loading PVP ZONE INSTANCES->" + a.getMessage());
		}
		
		try{
			VoteInstance.XMLLoad();
		}catch(Exception a){
			_log.warning("Error loading VOTE ZONE INSTANCE->" + a.getMessage());
		}
		
		try{
			potionSystem.loadPotions();
		}catch(Exception a){
			_log.warning("Error loading potions_system.xml");
		}
		
		if(!onLine){
			try{
				handler.registerHandler();
			}catch(Exception a){
				_log.warning("Error al cargar los ZeuS Voice Handler");
			}

			try{
				adminHandler.registerHandler();
			}catch(Exception a){
				_log.warning("Error al cargar los ZeuS Voide Handler");
			}
			
			onLine = true;
			
			BeginTime = opera.getFechaActual();
			unixTimeBeginServer = opera.getUnixTimeNow();
			
		}else{
			try{
				loadPremiumServices();
			}catch(Exception a){
				_log.warning("Error loading premium data->" + a.getMessage());
			}			
			if(showmessage){
				System.out.println("---------ZeuS Config Change ---------------");
			}
		}
		
		try{
			getAllAutoUpdate();
		}catch(Exception a){
			
		}		


	}
	
	public static Map<String, _charinfo> _getCharInfo(){
		return CHAR_INFO;
	}
	
	@SuppressWarnings("rawtypes")
	public static String _getIdToCharName(int idChar){
		Iterator itr = CHAR_INFO.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Info =(Map.Entry)itr.next();
			String Nombre = (String) Info.getKey();
			_charinfo temp = (_charinfo) Info.getValue();
			if(temp.getCharID() == idChar){
				return Nombre;
			}
		}
		//return ID_TO_CHAR_NAME;
		return "";
	}
	
	public static Vector<Integer> getTopSearchItem(){
		return TopSearch_Item;
	} 
	public static Vector<Integer> getTopSearchMonster(){
		return TopSearch_Monster;
	} 
	
	private static void _getMostSearch(){
		
		if(TopSearch_Item!=null){
			TopSearch_Item.clear();
		}
		if(TopSearch_Monster!=null){
			TopSearch_Monster.clear();
		}
		String consulta = "SELECT * FROM zeus_rank_acc WHERE zeus_rank_acc.tip=\"SEARCH_DROP\" ORDER BY zeus_rank_acc.cant DESC LIMIT 5";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						TopSearch_Item.add(rss.getInt("id"));
					}catch(SQLException e){

					}
				}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}
		
		consulta = "SELECT * FROM zeus_rank_acc WHERE zeus_rank_acc.tip=\"SEARCH_NPC\" ORDER BY zeus_rank_acc.cant DESC LIMIT 5";
		conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						TopSearch_Monster.add(rss.getInt("id"));
					}catch(SQLException e){

					}
				}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}
	}
	
	
	
	
	
	public static Map<String, Integer> getRaceCount(){
		return RACE_COUNT;
	}
	
	
	
	public static void _getAllChar(){
		try{
			CHAR_INFO.clear();
		}catch(Exception a){
			
		}
		
		try{
			RACE_COUNT.clear();
		}catch(Exception a){
			
		}
		
		int IdCheck = opera.getUnixTimeNow();
		
		String Consulta = "SELECT characters.char_name, characters.charId, characters.`level`, characters.race, characters.base_class, characters.pvpkills, characters.pkkills, characters.account_name FROM characters WHERE characters.accesslevel >= 0 ORDER BY characters.char_name ASC";
		
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						_classesinfo T1 = CLASS_LIST.get(rss.getInt("base_class"));
						_charinfo temp = new _charinfo(rss.getString(1), rss.getString(8), rss.getInt("level"), rss.getInt("pvpkills"), rss.getInt("pkkills"), rss.getInt("base_class"), rss.getInt("charId"));
						CHAR_INFO.put(rss.getString(1), temp);
						T1.setCountByOne(IdCheck);
						
						if(RACE_COUNT.containsKey(T1.getRaceName())){
							RACE_COUNT.put(T1.getRaceName(), RACE_COUNT.get(T1.getRaceName())+1);
						}else{
							RACE_COUNT.put(T1.getRaceName(), 1);
						}
						
					}catch(SQLException e){

					}
				}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}
	}	
	
	public static Map<Integer, premiumsystem> getPremiumServices(){
		return PREMIUM_SERVICES;
	}
	
	private static void loadAdvencedClassMaster(){
		try{
			ADVANCED_CLASS_MASTER.clear();
		}catch(Exception a){
			
		}
		
		boolean isActive=false, giveReward=false, giveArmor=false, giveWeapon=false, giveJustOneTime=false;
		File dir = new File("./config/zeus/zeus_advanced_class_master.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("config")){
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String Config = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ValConfig = dd.getAttributes().getNamedItem("value").getNodeValue().toLowerCase();
									switch(Config){
										case "enable":
											try{
												isActive = Boolean.parseBoolean(ValConfig);
											}catch(Exception a){
												
											}
											break;
										case "give_reward":
											try{
												giveReward = Boolean.parseBoolean(ValConfig);
											}catch(Exception a){
												
											}
											break;
										case "give_armor":
											try{
												giveArmor = Boolean.parseBoolean(ValConfig);
											}catch(Exception a){
												
											}
											break;
										case "give_weapon":
											try{
												giveWeapon = Boolean.parseBoolean(ValConfig);
											}catch(Exception a){
												
											}
											break;
									}
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("reward")){
							int Index = Integer.valueOf(d.getAttributes().getNamedItem("occupation_index").getNodeValue());
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									if(ADVANCED_CLASS_MASTER==null){
										Advclassmaster T1 = new Advclassmaster(Index,giveWeapon,giveArmor,giveReward,isActive,0,giveJustOneTime);
										ADVANCED_CLASS_MASTER.put(Index, T1);
									}else if(!ADVANCED_CLASS_MASTER.containsKey(Index)){
										Advclassmaster T1 = new Advclassmaster(Index,giveWeapon,giveArmor,giveReward,isActive,0,giveJustOneTime);
										ADVANCED_CLASS_MASTER.put(Index, T1);										
									}
									String IdItem = dd.getAttributes().getNamedItem("id").getNodeValue().toLowerCase();
									String Quantity = dd.getAttributes().getNamedItem("value").getNodeValue().toLowerCase();
									ADVANCED_CLASS_MASTER.get(Index).setReward(IdItem+","+Quantity);
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("armor")){
							int Index = Integer.valueOf(d.getAttributes().getNamedItem("occupation_index").getNodeValue());
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									if(ADVANCED_CLASS_MASTER==null){
										Advclassmaster T1 = new Advclassmaster(Index,giveWeapon,giveArmor,giveReward,isActive,0,giveJustOneTime);
										ADVANCED_CLASS_MASTER.put(Index, T1);
									}else if(!ADVANCED_CLASS_MASTER.containsKey(Index)){
										Advclassmaster T1 = new Advclassmaster(Index,giveWeapon,giveArmor,giveReward,isActive,0,giveJustOneTime);
										ADVANCED_CLASS_MASTER.put(Index, T1);										
									}
									String SetName = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String SetIds = dd.getAttributes().getNamedItem("ids").getNodeValue().toLowerCase();
									ADVANCED_CLASS_MASTER.get(Index).setArmor(SetName, SetIds);
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("weapon")){
							int Index = Integer.valueOf(d.getAttributes().getNamedItem("occupation_index").getNodeValue());
							int WeaponToGive_T = Integer.valueOf(d.getAttributes().getNamedItem("weapon_to_give").getNodeValue());
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									if(ADVANCED_CLASS_MASTER==null){
										Advclassmaster T1 = new Advclassmaster(Index,giveWeapon,giveArmor,giveReward,isActive,0,giveJustOneTime);
										ADVANCED_CLASS_MASTER.put(Index, T1);
									}else if(!ADVANCED_CLASS_MASTER.containsKey(Index)){
										Advclassmaster T1 = new Advclassmaster(Index,giveWeapon,giveArmor,giveReward,isActive,0,giveJustOneTime);
										ADVANCED_CLASS_MASTER.put(Index, T1);										
									}
									String SetName = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String SetIds = dd.getAttributes().getNamedItem("ids").getNodeValue().toLowerCase();
									ADVANCED_CLASS_MASTER.get(Index).setWeapon(SetName, SetIds, WeaponToGive_T);
								}
							}							
						}
					}
				}
			}
		}catch(Exception a){
			
		}
	}
	
	private static void loadLevelUpSpotWindows(){
		try{
			LEVEL_UP_SPOT_WINDOWS.clear();
		}catch(Exception a){
			
		}
		File dir = new File("./config/zeus/zeus_levelup_spot_setting.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);			
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("config")){
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String Config = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ValConfig = dd.getAttributes().getNamedItem("value").getNodeValue().toLowerCase();
									switch(Config){
										case "enabled":
											try{
												LEVEL_UP_SPOT = Boolean.parseBoolean(ValConfig);
											}catch(Exception a){
												
											}
											break;
									}
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("levelspotwindows")){
							int Level = Integer.valueOf(d.getAttributes().getNamedItem("level").getNodeValue());
							String PlaceName = "", X = "", Y = "", Z = "";
							int IDImagen = 0;
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String Config = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String ValConfig = dd.getAttributes().getNamedItem("value").getNodeValue().toLowerCase();
									switch(Config){
										case "place_name":
											try{
												PlaceName = ValConfig;
											}catch(Exception a){
												
											}
											break;
										case "id_imagen":
											try{
												IDImagen = Integer.valueOf(ValConfig);
											}catch(Exception a){
												
											}
											break;
										case "location_x":
											try{
												X = ValConfig;
											}catch(Exception a){
												
											}
											break;
										case "location_y":
											try{
												Y = ValConfig;
											}catch(Exception a){
												
											}
											break;
										case "location_z":
											try{
												Z = ValConfig;
											}catch(Exception a){
												
											}
											break;
									}
									
								}
							}
							levelupspot T1 = new levelupspot(Level, PlaceName, IDImagen, Integer.valueOf(X), Integer.valueOf(Y), Integer.valueOf(Z));
							LEVEL_UP_SPOT_WINDOWS.put(Integer.valueOf(Level), T1);
						}
					}
				}
			}
			
		}catch(Exception a){
			_log.warning("Error loading Level Up Spot Windows");
		}
		
	}
	
	
	private static void loadAntibotIcons(){
		File dir = new File("./config/zeus/zeus_antibot_icons.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("icons")){
							Vector<String>Icons = new Vector<String>();
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String IconName = dd.getAttributes().getNamedItem("icon").getNodeValue().toLowerCase();
									Icons.add(IconName);
								}
							}
							antibotSystem.setIcons(Icons);
						}
					}
				}
			}
		}catch(Exception a){
			
		}
	}
	
	
	
	
	@SuppressWarnings({ "null", "rawtypes", "unused", "unchecked" })
	private static void loadBuffCB_buffs(){
		BUFF_COMMUNITY_CATEGORIA.clear();
		HashMap<String, Vector<buffcommunity>> BUFF_COMMUNITY_FORT_SORT = new HashMap<String, Vector<buffcommunity>>();
		ALL_BUFF_COMMUNITY.clear();
		BUFF_SINGLE_ALL.clear();
		File dir = new File("./config/zeus/zeus_buffer.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("config")){
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String NameConfig = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									String Value = dd.getAttributes().getNamedItem("value").getNodeValue().toLowerCase();
									switch(NameConfig.toLowerCase()){
										case "use_on_instances":
											try{
												BUFFCHAR_USE_INSTANCE = Boolean.valueOf(Value);
											}catch(Exception a){
												_log.warning("Buff Config bad Setting on use on instance zone: Value most be Boolean and get this->" + Value + ". Default Setting on (false)");
												BUFFCHAR_USE_INSTANCE = false;
											}											
											break;
										case "heal_and_cancel_peace_zone_only":
											try{
												BUFFCHAR_CAN_HEAL_CANCEL_JUST_IN_PEACE_ZONE = Boolean.valueOf(Value);
											}catch(Exception a){
												_log.warning("Buff Config bad Setting on use on Heal and Cancel Just in Peace zone: Value most be Boolean and get this->" + Value + ". Default Setting on (false)");
												BUFFCHAR_CAN_HEAL_CANCEL_JUST_IN_PEACE_ZONE = false;
											}
											break;
										case "use_on_peace_zone_only":
											try{
												BUFFCHAR_JUST_ON_PEACE_ZONE = Boolean.valueOf(Value);
											}catch(Exception a){
												_log.warning("Buff Config bad Setting on use on peace zone: Value most be Boolean and get this->" + Value + ". Default Setting on (true)");
												BUFFCHAR_JUST_ON_PEACE_ZONE = true;
											}
											break;
										case "heal_price":
											try{
												BUFFCHAR_COST_HEAL = Integer.valueOf(Value);
											}catch (Exception e) {
												_log.warning("Buff Config bad Setting on Heal price: Value most be Integer and get this->" + Value + ". Default Setting on (50000)");
												BUFFCHAR_COST_HEAL = 50000;
											}
											break;
										case "cancel_price":
											try{
												BUFFCHAR_COST_CANCEL = Integer.valueOf(Value);
											}catch (Exception e) {
												_log.warning("Buff Config bad Setting on Cancel price: Value most be Integer and get this->" + Value + ". Default Setting on (60000)");
												BUFFCHAR_COST_CANCEL = 60000;
											}
											break;
										case "heal_and_cancel_in_combat_mode":
											try{
												BUFFCHAR_HEAL_AND_CANCEL_COMBAT_MODE = Boolean.valueOf(Value);
											}catch(Exception a){
												BUFFCHAR_HEAL_AND_CANCEL_COMBAT_MODE = false;
											}
											break;
										case "heal_and_cancel_in_flag_mode":
											try{
												BUFFCHAR_HEAL_AND_CANCEL_FLAG_MODE = Boolean.valueOf(Value);
											}catch(Exception a){
												BUFFCHAR_HEAL_AND_CANCEL_FLAG_MODE = false;
											}
											break;
										case "heal_just_peace_zone":
											try{
												BUFFCHAR_HEAL_AND_CANCEL_JUST_PEACE_ZONE = Boolean.valueOf(Value);
											}catch(Exception a){
												BUFFCHAR_HEAL_AND_CANCEL_JUST_PEACE_ZONE = false;
											}
											break;
										case "use_scheme_combat_mode":
											try{
												BUFFCHAR_SCHEME_USE_COMBAT_MODE = Boolean.valueOf(Value);
											}catch(Exception a){
												BUFFCHAR_SCHEME_USE_COMBAT_MODE = false;
											}
											break;
										case "use_scheme_flag_mode":
											try{
												BUFFCHAR_SCHEME_USE_FLAG_MODE = Boolean.valueOf(Value);
											}catch(Exception a){
												BUFFCHAR_SCHEME_USE_FLAG_MODE = false;
											}
											break;
										case "id_item_requested":
											try{
												BUFFCHAR_SCHEME_USE_ITEM_ID = Integer.valueOf(Value);
											}catch(Exception a){
												BUFFCHAR_SCHEME_USE_ITEM_ID = 57;
											}
											break;
										case "max_scheme_pour_player":
											break;
									}
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("buffer")){
							int idBuffCategoria = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							String BuffCategoriaName = d.getAttributes().getNamedItem("name").getNodeValue();
							int PriceSingle = Integer.valueOf(d.getAttributes().getNamedItem("pricesingle").getNodeValue());
							int MinLevel = Integer.valueOf(d.getAttributes().getNamedItem("minlvl").getNodeValue());
							String Icon = d.getAttributes().getNamedItem("icon").getNodeValue();
							
							if(BUFF_COMMUNITY_FORT_SORT==null){
								BUFF_COMMUNITY_FORT_SORT.put(BuffCategoriaName, new Vector<buffcommunity>());
							}else if(!BUFF_COMMUNITY_FORT_SORT.containsKey(BuffCategoriaName)){
								BUFF_COMMUNITY_FORT_SORT.put(BuffCategoriaName, new Vector<buffcommunity>());
							}
							
							boolean Activo = Boolean.valueOf(d.getAttributes().getNamedItem("active").getNodeValue());
							boolean FLAGMODE = Boolean.valueOf(d.getAttributes().getNamedItem("use_in_flag_mode").getNodeValue());
							boolean PKMODE = Boolean.valueOf(d.getAttributes().getNamedItem("use_with_karma").getNodeValue());
							boolean COMBATMODE = Boolean.valueOf(d.getAttributes().getNamedItem("use_in_combat_mode").getNodeValue());
							
							buffcommunity T1 = new buffcommunity(idBuffCategoria, BuffCategoriaName, Icon, MinLevel, Activo, PriceSingle,FLAGMODE,PKMODE,COMBATMODE);
							BUFF_COMMUNITY_CATEGORIA.put(idBuffCategoria, T1);
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									int Buffs = Integer.valueOf(dd.getAttributes().getNamedItem("id").getNodeValue().toLowerCase());
									try{
										String NameBuff = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
										String DescripBuff = dd.getAttributes().getNamedItem("descript").getNodeValue().toLowerCase();
										int LevelBuff = Integer.valueOf(dd.getAttributes().getNamedItem("level").getNodeValue().toLowerCase());
										boolean isActive = Boolean.valueOf(dd.getAttributes().getNamedItem("active").getNodeValue().toLowerCase());
										boolean isForPremium = Boolean.valueOf(dd.getAttributes().getNamedItem("forpremium").getNodeValue().toLowerCase());
										int reuseTime = -1;
										try {
											reuseTime = Integer.valueOf(dd.getAttributes().getNamedItem("reuse").getNodeValue());
										}catch(Exception a) {
											reuseTime = -1;
										}
										String TID = String.valueOf(Buffs) + String.valueOf(LevelBuff);										
										buffcommunity T2 = new buffcommunity(TID,Buffs,NameBuff,DescripBuff, MinLevel, LevelBuff,PriceSingle,idBuffCategoria,BuffCategoriaName,isActive,isForPremium,FLAGMODE,PKMODE,COMBATMODE, reuseTime);
										BUFF_SINGLE_ALL.add(Buffs);
										BUFF_COMMUNITY_FORT_SORT.get(BuffCategoriaName).add(T2);
									}catch(Exception a){
										_log.warning("Error Loading Single Buff->Cat:" + BuffCategoriaName+"ID Buff->"+ Buffs +" ->"+a.getMessage());
									}
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("icons")){
							Vector<String>Icons = new Vector<String>();
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("set")){
									String IconName = dd.getAttributes().getNamedItem("icon").getNodeValue().toLowerCase();
									Icons.add(IconName);
								}
							}
							v_Buffer_New.setIcons(Icons);
						}
					}
				}
			}
		}catch(Exception a){
			
		}
		Comparator<buffcommunity> NameAZ = (p1,p2) -> p1.getBuffName() .compareToIgnoreCase(p2.getBuffName());

		Iterator itr = BUFF_COMMUNITY_FORT_SORT.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			String Categoria = (String)Entrada.getKey();
			Vector<buffcommunity> Buffs= (Vector<buffcommunity>)Entrada.getValue();
			try{
				Collections.sort(Buffs,NameAZ);
			}catch(Exception a){
				
			}
			for(buffcommunity Temp : Buffs){
				ALL_BUFF_COMMUNITY.add(Temp);
			}
		}
	}	
	
	private static void loadBuffCB_Scheme(){
		BUFF_COMMUNITY_SCHEMME.clear();
		File dir = new File("./config/zeus/zeus_buffer_scheme.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("scheme")){
							int IDScheme = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							int Price = Integer.valueOf(d.getAttributes().getNamedItem("price").getNodeValue());
							int MinLevel = Integer.valueOf(d.getAttributes().getNamedItem("minlvl").getNodeValue());
							int IdItemRequested = Integer.valueOf(d.getAttributes().getNamedItem("minlvl").getNodeValue());
							String Icon = d.getAttributes().getNamedItem("icon").getNodeValue();
							String Name = d.getAttributes().getNamedItem("name").getNodeValue();
							boolean Activo = Boolean.valueOf(d.getAttributes().getNamedItem("active").getNodeValue());
							boolean flagMode = Boolean.valueOf(d.getAttributes().getNamedItem("use_in_flag_mode").getNodeValue());
							boolean PKMode = Boolean.valueOf(d.getAttributes().getNamedItem("use_with_karma").getNodeValue());
							boolean combatMode = Boolean.valueOf(d.getAttributes().getNamedItem("use_in_combat_mode").getNodeValue());
							for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
								if(dd.getNodeName().equalsIgnoreCase("buff")){
									String Buffs = dd.getAttributes().getNamedItem("id").getNodeValue().toLowerCase();
									int freeUntilLevel = Integer.valueOf(d.getAttributes().getNamedItem("free_until_level").getNodeValue());
									IdItemRequested = Integer.valueOf(d.getAttributes().getNamedItem("id_item_requested").getNodeValue());
									buffcommunitySchemme T = new buffcommunitySchemme(IDScheme,Buffs,Name,Icon,Price,MinLevel,Activo,flagMode,PKMode,combatMode, freeUntilLevel, IdItemRequested);
									BUFF_COMMUNITY_SCHEMME.put(IDScheme, T);
								}
							}
						}
					}
				}
			}
		}catch(Exception a){
			
		}		
	}
	
	private static void loadKillingSpree(){
		File dir = new File("./config/zeus/zeus_pvp_pk_spree.xml");

		SPREE_PK_SYSTEM_MESSAGE.clear();
		SPREE_PVP_SYSTEM_MESSAGE.clear();
		
		int pvpcount=0, pkcount=0;
		
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for(Node n = doc.getFirstChild(); n!=null;n=n.getNextSibling()){
				if("list".equalsIgnoreCase(n.getNodeName())){
					for(Node d = n.getFirstChild(); d !=null; d = d.getNextSibling()){
						if ("config".equalsIgnoreCase(d.getNodeName())){
							for(Node b = d.getFirstChild(); b != null ; b=b.getNextSibling() ){
								if ("set".equalsIgnoreCase(b.getNodeName())){
									boolean bTemp = Boolean.parseBoolean(b.getAttributes().getNamedItem("value").getNodeValue() );
									SPREE_SYSTEM = bTemp;
								}
							}
						}else if("pvp".equalsIgnoreCase(d.getNodeName())){
							int iniS = Integer.valueOf(d.getAttributes().getNamedItem("count").getNodeValue());
							String MSN = d.getAttributes().getNamedItem("messages").getNodeValue();
							SPREE_PVP_SYSTEM_MESSAGE.put(iniS, MSN);
							pvpcount++;
						}else if("pk".equalsIgnoreCase(d.getNodeName())){
							int iniS = Integer.valueOf(d.getAttributes().getNamedItem("count").getNodeValue());
							String MSN = d.getAttributes().getNamedItem("messages").getNodeValue();
							SPREE_PK_SYSTEM_MESSAGE.put(iniS, MSN);
							pkcount++;
						}
					}
				}
			}
		}catch(Exception a){
			_log.warning("Error loading ZeuS Spree System->" + a.getMessage());			
		}
		_log.info("	ZeuS Spree System: load " + String.valueOf(pvpcount) + " for PVP spree and " + String.valueOf(pkcount) + " for PK spree");		
		
		
	}
	
	
	private static void loadPvPPkColor(){
		COLOR_SYSTEM_PK_NAME.clear();
		COLOR_SYSTEM_PVP_NAME.clear();
		
		int pvpcount=0, pkcount=0;
		
		
		File dir = new File("./config/zeus/zeus_pvp_pk_color.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for(Node n = doc.getFirstChild(); n!=null;n=n.getNextSibling()){
				if("list".equalsIgnoreCase(n.getNodeName())){
					for(Node d = n.getFirstChild(); d !=null; d = d.getNextSibling()){
						if ("config".equalsIgnoreCase(d.getNodeName())){
							for(Node b = d.getFirstChild(); b != null ; b=b.getNextSibling() ){
								if ("set".equalsIgnoreCase(b.getNodeName())){
									boolean bTemp = Boolean.parseBoolean(b.getAttributes().getNamedItem("value").getNodeValue() );
									if(b.getAttributes().getNamedItem("name").getNodeValue().equals("enabled_pvp_name_color")){
										general.PVP_COLOR_SYSTEM_ENABLED = bTemp;
									}else if(b.getAttributes().getNamedItem("name").getNodeValue().equals("enabled_pk_title_color")){
										general.PK_COLOR_SYSTEM_ENABLED = bTemp;
									}
								}
							}
						}else if("pvpcolor".equalsIgnoreCase(d.getNodeName())){
							int iniS = Integer.valueOf(d.getAttributes().getNamedItem("count_start").getNodeValue());
							int iniE = Integer.valueOf(d.getAttributes().getNamedItem("count_end").getNodeValue());
							String color = d.getAttributes().getNamedItem("color").getNodeValue();
							COLOR_SYSTEM_PVP_NAME.put(iniS, new HashMap<Integer, String>());
							COLOR_SYSTEM_PVP_NAME.get(iniS).put(iniE, color);
							pvpcount++;
						}else if("pkcolor".equalsIgnoreCase(d.getNodeName())){
							int iniS = Integer.valueOf(d.getAttributes().getNamedItem("count_start").getNodeValue());
							int iniE = Integer.valueOf(d.getAttributes().getNamedItem("count_end").getNodeValue());
							String color = d.getAttributes().getNamedItem("color").getNodeValue();
							COLOR_SYSTEM_PK_NAME.put(iniS, new HashMap<Integer, String>());
							COLOR_SYSTEM_PK_NAME.get(iniS).put(iniE, color);
							pkcount++;
						}
					}
				}
			}
		}catch(Exception a){
			_log.warning("Error loading ZeuS PVP PK Color System->" + a.getMessage());			
		}
		_log.info("	ZeuS PVP Color System: load " + String.valueOf(pvpcount) + " color for PVP and " + String.valueOf(pkcount) + " color for PK");
	}
	
	
	@SuppressWarnings("unused")
	private static void loadPremiumServices(){
		boolean USE_VALUE_NEW_RATE = false;
		PREMIUM_SERVICES.clear();
		try{
				PREMIUM_SERVICES.clear();
			}catch(Exception a){
				
			}
		
		File dir = new File("./config/zeus/zeus_premium.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
			{
				if ("list".equalsIgnoreCase(n.getNodeName()))
				{
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if("setting".equalsIgnoreCase(d.getNodeName())){
							String _value = d.getAttributes().getNamedItem("use_value_like_new_rate").getNodeValue();
							if(_value != null) {
								if(_value.length()>0) {
									try {
										USE_VALUE_NEW_RATE = Boolean.parseBoolean(_value);								
									}
									catch(Exception a)
									{
										USE_VALUE_NEW_RATE = false;
									}
								}
							}
						}
						else if ("premium".equalsIgnoreCase(d.getNodeName()))
						{
							int IDPremiun = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							String PremiumChanceItem = "", NombrePremiun = d.getAttributes().getNamedItem("name").getNodeValue();
							NamedNodeMap n2 = d.getAttributes();
							Node first = d.getFirstChild();
							int exp=0, sp=0, adenachance=0, spoilchance=0, dropchance=0, epaulette=0,craft=0,mwcraft=0,soulcrystal=0,days=0,cost=0,buffDuration=0,Weight=0, cpbonus=0, hpbonus=0, mpbonus=0;
							int adenarate=0, spoilrate=0, droprate=0;
							int chatPremiunIDCHAT = -1;
							int chatPremiunREUSE = -1;
							String PremiumRateItem = "";
							boolean isAcc=true;
							boolean isEnabled= false;
							boolean canUseBuffPremium = false;
							boolean isPremiumChance = false;
							boolean isPremiumRate = false;
							boolean forTest = false;
							boolean showInPlayerList = true;
							boolean heroStatus = true;
							boolean tele_immediately = true;
							boolean cpOlyRestric = true;
							boolean hpOlyRestric = true;
							boolean mpOlyRestric = true;
							boolean chatPremiunENABLE = false;
							String Icono="";							
							Vector<String> DataToShow = new Vector<String>();
							for (Node dd = first; dd != null; dd = dd.getNextSibling())
							{
								if ("set".equalsIgnoreCase(dd.getNodeName()))
								{
									String valor = dd.getAttributes().getNamedItem("val").getNodeValue();
									boolean tmpBoolean;
									boolean grabar = true;
									switch(dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase()){
										case "exp":
											exp = Integer.valueOf(valor);
											break;
										case "sp":
											sp = Integer.valueOf(valor);
											break;
										case "mp_bonus":
											try {
												tmpBoolean = Boolean.valueOf( dd.getAttributes().getNamedItem("oly_restriction").getNodeValue() );
											}catch(Exception a) {
												tmpBoolean = true;
											}
											mpOlyRestric = tmpBoolean;
											mpbonus = Integer.valueOf(valor);
											break;
										case "hp_bonus":
											try {
												tmpBoolean = Boolean.valueOf( dd.getAttributes().getNamedItem("oly_restriction").getNodeValue() );
											}catch(Exception a) {
												tmpBoolean = true;
											}
											hpOlyRestric = tmpBoolean;
											hpbonus = Integer.valueOf(valor);
											break;
										case "cp_bonus":
											try {
												tmpBoolean = Boolean.valueOf( dd.getAttributes().getNamedItem("oly_restriction").getNodeValue() );
											}catch(Exception a) {
												tmpBoolean = true;
											}
											cpOlyRestric = tmpBoolean;
											cpbonus = Integer.valueOf(valor);
											break;
										case "chat":
											chatPremiunENABLE = Boolean.valueOf(valor);
											try {
												int TempIdChat = Integer.valueOf( dd.getAttributes().getNamedItem("idChat").getNodeValue() );
												int TempReuse = Integer.valueOf( dd.getAttributes().getNamedItem("reuse").getNodeValue() );
												chatPremiunIDCHAT = TempIdChat;
												chatPremiunREUSE = TempReuse;
											}catch(Exception a) {
												
											}
											break;
										case "adena_chance":
											adenachance = Integer.valueOf(valor);
											break;
										case "adena_rate":
											adenarate = Integer.valueOf(valor);
											break;											
										case "spoil_chance":
											spoilchance = Integer.valueOf(valor);
											break;
										case "spoil_rate":
											spoilrate = Integer.valueOf(valor);
											break;											
										case "drop_chance":
											dropchance = Integer.valueOf(valor);
											break;
										case "drop_rate":
											droprate = Integer.valueOf(valor);
											break;											
										case "epaulette":
											epaulette = Integer.valueOf(valor);
											break;
										case "craft":
											craft = Integer.valueOf(valor);
											break;
										case "mw_craft":
											mwcraft = Integer.valueOf(valor);
											break;
										case "soul_crystal":
											soulcrystal = Integer.valueOf(valor);
											break;
										case "buff_premium":
											canUseBuffPremium = Boolean.parseBoolean(valor);
											break;
										case "buff_duration":
											buffDuration = Integer.valueOf(valor);
											break;
										case "weight_limit":
											Weight = Integer.valueOf(valor);
											break;
										case "item_drop_chance":
											isPremiumChance = Boolean.parseBoolean(valor);
											break;
										case "item_drop_rate":
											isPremiumRate = Boolean.parseBoolean(valor);
											break;
										case "hero_status":
											heroStatus = Boolean.parseBoolean(valor);
											break;
										case "days":
											grabar=false;
											days = Integer.valueOf(valor);
											break;
										case "premium_item_chance":
											PremiumChanceItem = valor;
											grabar = false;
											break;
										case "premium_item_rate":
											PremiumRateItem = valor;
											grabar = false;
											break;											
										case "cost":
											grabar=false;
											cost = Integer.valueOf(valor);
											break;
										case "apply_to":
											grabar=false;
											isAcc = valor.equalsIgnoreCase("account");
											break;
										case "ima":
											grabar=false;
											Icono = valor;
											break;
										case "active":
											isEnabled = Boolean.parseBoolean(valor);
											grabar=false;
											break;
										case "just_for_test":
										case "JustForTest":
											forTest = Boolean.parseBoolean(valor);
											grabar=false;
											break;
										case "show_in_player_list":
											showInPlayerList = Boolean.parseBoolean(valor);
											grabar = false;
											break;
										case "tele_immediately":
											tele_immediately = Boolean.parseBoolean(valor);
											grabar = false;
											break;
										
											
									}
									if(grabar){
										DataToShow.add(dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase() + ":" + valor);
									}
								}
							}
							premiumsystem V = new premiumsystem(IDPremiun,exp,sp,dropchance,adenachance,spoilchance,epaulette,craft,mwcraft,soulcrystal,Weight,days,cost,isAcc,NombrePremiun,Icono,DataToShow,isEnabled,canUseBuffPremium,buffDuration,PremiumChanceItem,isPremiumChance,forTest,showInPlayerList,heroStatus,tele_immediately,adenarate,droprate,spoilrate,isPremiumRate,PremiumRateItem,USE_VALUE_NEW_RATE, cpbonus, hpbonus, mpbonus, cpOlyRestric, hpOlyRestric, mpOlyRestric, chatPremiunENABLE, chatPremiunIDCHAT, chatPremiunREUSE);
							PREMIUM_SERVICES.put(IDPremiun, V);
						}
					}
				}
			}
		}catch(Exception a){
			//System.out.print(a.getMessage());
		}	
		
	}
	

	protected static boolean loadSerial(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_CONFIG)));
			HOST_ = String.valueOf(propZ.getProperty("HOST"));
			USER_ = String.valueOf(propZ.getProperty("USER"));
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR SERIAL DE ZEUS ::::::::::");
			_log.warning(a.getMessage());
			_isValid=false;
			return false;
		}
	}
	
	protected static boolean loadPaypal() {
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_PAYPAL_CONFIG)));
			PAYPAL_LINK_WEB = String.valueOf(propZ.getProperty("PAYPAL_LINK_WEB"));
			PAYPAL_LINK_EMAIL = String.valueOf(propZ.getProperty("PAYPAL_LINK_EMAIL"));
			PAYPAL_LINK_USE = String.valueOf(propZ.getProperty("PAYPAL_LINK_USE"));
			PAYPAL_SECONDS_WAITING_TIME = Integer.valueOf(propZ.getProperty("PAYPAL_SECONDS_WAITING_TIME"));
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR LOADING PAYPAL INFORMATION ::::::::::");
			_log.warning(a.getMessage());
			return false;
		}		
	}
	
	protected static boolean loadNpcCommunity(){
		try{
			NPC_ID_TO_COMMUNITY.clear();
		}catch(Exception a){
			
		}
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_CONFIG_NPC_COMMUNITY)));
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("MAIN_COMMUNITY_WINDOWS","-1")), general.getCOMMUNITY_BOARD_PART_EXEC());
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("ENGINE_COMMUNITY_WINDOWS","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("REGION_COMMUNITY_WINDOWS","-1")), general.getCOMMUNITY_BOARD_REGION_PART_EXEC());
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("GRAND_RAID_BOSS_WINDOWS","-1")), general.getCOMMUNITY_BOARD_GRAND_RB_EXEC());
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("PARTY_FINDER_WINDOWS","-1")), general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC());
			
			
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("BUFFER_NPC_ID","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Buffer;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("GO_PARTY_LEADER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Gopartyleader;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("FLAG_FINDER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Flagfinder;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("TELEPORT","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Teleport;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SHOP","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Shop;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("WHAREHOUSE","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Warehouse;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("AUGMENT_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";AugmentManager;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SUBCLASS_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SubClass;add;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("PROFESSION_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Profession;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("DROP_SEARCH_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";DropSearch;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("PVP_PK_LOG_SYSTEM","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";pvppklog;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SYMBOL_MAKER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Symbolmaker;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("BUG_REPORT_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";BugReport;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("TRANSFORMATIONS_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Transformation;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("REMOVE_ATTRIBUTE_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";RemoveAttri;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SELECT_AUGMENT_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectAugment;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SELECT_ENCHANT_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectEnchant;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SELECT_ELEMENTAL_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectElemental;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SELECT_RAID_BOSS_INFO_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";RaidBossInfo;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("BLACKSMITH_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.blacksmith.name() +";0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("MISCELANIUS","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";charclanoption;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("DRESSME","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.Dressme.name() + ";0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("PARTY_MATCHING","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.partymatching.name() + ";0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("AUCTION_HOUSE","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("BID_HOUSE","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.BidHouse.name() + ";1;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("CASTLE_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.castleManager.name() + ";0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("DONATION_MAMANGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("CLASES_STADISTIC_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.ClasesStadistic.name() + ";0;Human;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("COMMANDS_MANAGER","-1")), general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("SERVER_CONFIG","-1")), general.getCOMMUNITY_BOARD_PART_EXEC() + ";RATE;0;0;0;0;0;0");
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("JAIL_BAIL_NPC","-1")), general.getCOMMUNITY_BOARD_PART_EXEC() + ";BAIL;0;0;0;0;0;0");			
			NPC_ID_TO_COMMUNITY.put(Integer.valueOf(propZ.getProperty("BUFFER_BUFFSTORE_SEARCH_BUFF","-1")), general.getCOMMUNITY_BOARD_PART_EXEC() + ";BSSS;0;0;0;0;0;0");						
			ZEUS_ID_NPC_EXCLUSIVE_SHOP = Integer.valueOf(propZ.getProperty("EXCLUSIVE_SHOP_FROM_COMMUNITY_BOARD", "-1"));
			ZEUS_ID_NPC_INSTANCE_PVP = Integer.valueOf(propZ.getProperty("PVP_INSTANCE", "-1"));
			ZEUS_ID_NPC_BETA = Integer.valueOf(propZ.getProperty("BETA_NPC", "-1"));

			
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR SERIAL DE ZEUS ::::::::::");
			_log.warning(a.getMessage());
			_isValid=false;
			return false;
		}
	}
	
	
	protected static boolean loadDonationConfig(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_DONATION_CONFIG)));
			DONA_ID_ITEM = Integer.valueOf(propZ.getProperty("ID_DONATION_COIN"));
			DONATION_EXPLAIN_HOW_DO_IT = propZ.getProperty("DONATION_EXPLAIN_ABOUT_IT","");
			DONATION_CHANGE_CHAR_NAME_COST = Integer.valueOf(propZ.getProperty("DONATION_CHARACTER_RENAME","0"));
			DONATION_CHANGE_CLAN_NAME_COST = Integer.valueOf(propZ.getProperty("DONATION_CLAN_RENAME","0"));
			DONATION_255_RECOMMENDS = Integer.valueOf(propZ.getProperty("DONATION_255_RECOMMENDS","0"));
			DONATION_NOBLE_COST = Integer.valueOf(propZ.getProperty("DONATION_NOBLE","0"));
			DONATION_CHANGE_SEX_COST = Integer.valueOf(propZ.getProperty("DONATION_SEX_CHANGE","0"));
			DONATION_AIO_CHAR_SIMPLE_COSTO = Integer.valueOf(propZ.getProperty("DONATION_AIO_NORMAL","0"));
			DONATION_AIO_CHAR_30_COSTO = Integer.valueOf(propZ.getProperty("DONATION_AIO_30","0"));
			DONATION_AIO_CHAR_LV_REQUEST = Integer.valueOf(propZ.getProperty("DONATION_MIN_LVL_REQUEST","0"));
			
			BUFFER_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM = Boolean.valueOf(propZ.getProperty("BUFFER_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM","false"));
			SHOP_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM = Boolean.valueOf(propZ.getProperty("SHOP_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM","false"));			
			
			PREMIUM_CHAR = Boolean.valueOf(propZ.getProperty("PREMIUM_CHAR","false"));
			PREMIUM_CLAN = Boolean.valueOf(propZ.getProperty("PREMIUM_CLAN","false"));
			
			try{
				String ch_lv = propZ.getProperty("DONATION_CHARACTER_LEVEL","0,0");
				DONATION_CHARACTERS_LEVEL.clear();
				for(String p : ch_lv.split(";")){
					DONATION_CHARACTERS_LEVEL.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Character Level " + a.getMessage());
			}

			try{
				String c_repu = propZ.getProperty("DONATION_CLAN_REPUTATION","0,0");
				DONATION_CLAN_REPUTATION.clear();
				for(String p : c_repu.split(";")){
					DONATION_CLAN_REPUTATION.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Clan Reputation " + a.getMessage());
			}
			
			try{
				String c_skill = propZ.getProperty("DONATION_CLAN_SKILL","0,0");
				DONATION_CLAN_SKILL.clear();
				for(String p : c_skill.split(";")){
					DONATION_CLAN_SKILL.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Clan Skill " + a.getMessage());
			}
			
			try{
				String c_skill = propZ.getProperty("DONATION_CLAN_LEVEL","0,0");
				DONATION_CLAN_LEVEL.clear();
				for(String p : c_skill.split(";")){
					DONATION_CLAN_LEVEL.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Clan Level " + a.getMessage());
			}
			
			try{
				String c_fame = propZ.getProperty("DONATION_FAME_POINT","0,0");
				DONATION_CHARACTERS_FAME_POINT.clear();
				for(String p : c_fame.split(";")){
					DONATION_CHARACTERS_FAME_POINT.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Fame Point " + a.getMessage());
			}				
			
			try{
				String c_pk_point = propZ.getProperty("DONATION_PK_POINT","0,0");
				DONATION_CHARACTERS_PK_POINT.clear();
				for(String p : c_pk_point.split(";")){
					DONATION_CHARACTERS_PK_POINT.put(Integer.valueOf(p.split(",")[0]),Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Pk Point" + a.getMessage());
			}
			
			try{
				String enchantItem = propZ.getProperty("DONATION_ENCHANT_ITEM_ARMOR","0,0");
				DONATION_ENCHANT_ITEM_ARMOR.clear();
				for(String p : enchantItem.split(";")){
					DONATION_ENCHANT_ITEM_ARMOR.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Enchant Item" + a.getMessage());
			}
			
			try{
				String enchantItemW = propZ.getProperty("DONATION_ENCHANT_ITEM_WEAPON","0,0");
				DONATION_ENCHANT_ITEM_WEAPON.clear();
				for(String p : enchantItemW.split(";")){
					DONATION_ENCHANT_ITEM_WEAPON.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Enchant Item" + a.getMessage());
			}			

			try{
				String enchantItem_E_A = propZ.getProperty("DONATION_ELEMENTAL_ITEM_ARMOR","0,0");
				DONATION_ELEMENTAL_ITEM_ARMOR.clear();
				for(String p : enchantItem_E_A.split(";")){
					DONATION_ELEMENTAL_ITEM_ARMOR.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Elemental Item Armor" + a.getMessage());
			}
			
			try{
				String enchantItem_E_W = propZ.getProperty("DONATION_ELEMENTAL_ITEM_WEAPON","0,0");
				DONATION_ELEMENTAL_ITEM_WEAPON.clear();
				for(String p : enchantItem_E_W.split(";")){
					DONATION_ELEMENTAL_ITEM_WEAPON.put(Integer.valueOf(p.split(",")[0]), Integer.valueOf(p.split(",")[1]));
				}
			}catch(Exception a){
				_log.warning("Error Donation Load -> Elemental Item Armor" + a.getMessage());
			}
			try {
				String itemBlackList = propZ.getProperty("DONATION_BLACK_LIST", "");
				if(itemBlackList != "") {
					DONATION_BLACK_LIST.clear();
					if(itemBlackList.indexOf(",") > 0) {
						for(String p : itemBlackList.split(",")) {
							try {
								DONATION_BLACK_LIST.add(Integer.valueOf(p));
							}catch(Exception a) {
								_log.warning("ZeuS. Donation Enchant Blacklist error: This is not a Number: " + p);
							}
						}
					}else {
						try {
							DONATION_BLACK_LIST.add( Integer.valueOf(itemBlackList) );
						}catch(Exception a) {
							_log.warning("ZeuS. Donation Enchat Blacklist error: All Parameters need to be Numeric: " + itemBlackList);
						}
					}
				}
			}catch(Exception a) {
				
			}
			
			
			/*String itemCharPremium = String.valueOf(propZ.getProperty("PREMIUM_ITEM_DROP_CHAR"));
			if(itemCharPremium!=null){
				if(itemCharPremium.length()>0){
					for(String itemId : itemCharPremium.split(";")){
						String itemChance[] = itemId.split(":");
						if(opera.isNumeric(itemChance[0])){
							try{
								PREMIUM_ITEM_CHAR_DROP_LIST_RATE.put(Integer.valueOf(itemChance[0]), Float.parseFloat(itemChance[1]));
							}catch(Exception a ){
								_log.warning("ZeuS-> Error load Premium Char Item = " + itemId);
							}
						}
					}
				}
			}*/
			/*
			String itemClanPremium = String.valueOf(propZ.getProperty("PREMIUM_ITEM_DROP_CLAN"));
			if(itemClanPremium!=null){
				if(itemClanPremium.length()>0){
					for(String itemId : itemCharPremium.split(";")){
						String itemChance[] = itemId.split(":");
						if(opera.isNumeric(itemChance[0])){
							try{
								PREMIUM_ITEM_CLAN_DROP_LIST_RATE.put(Integer.valueOf(itemChance[0]), Float.parseFloat(itemChance[1]));
							}catch(Exception a ){
								_log.warning("ZeuS-> Error load Premium Clan Item = " + itemId);
							}
						}
					}
				}
			}*/
			
			DONATION_TYPE_LIST = propZ.getProperty("DONATION_TYPE_LIST","");
			
			
			DONATION_EXTRA_GIFT = Boolean.valueOf(propZ.getProperty("DONATION_EXTRA_GIFT","false"));
			DONATION_EXTRA_GIFT_REPEAT = Boolean.valueOf(propZ.getProperty("DONATION_EXTRA_GIFT_REPEAT","false"));
			DONATION_EXTRA_GIFT_TIMES = Integer.valueOf(propZ.getProperty("DONATION_EXTRA_GIFT_TIMES","3"));
			DONATION_TEMP_EVERY_TYPE = propZ.getProperty("DONATION_EXTRA_GIFT_REPEAT_TYPE","HOURS");
			DONATION_EXTRA_GIFT_REPEAT_EVERY_X = Integer.valueOf(propZ.getProperty("DONATION_EXTRA_GIFT_REPEAT_EVERY_X", "24"));
			
			DONATION_EXTRA_GIFT_FROM_DONATION_COIN = Integer.valueOf(propZ.getProperty("DONATION_EXTRA_GIFT_FROM_DONATION_COIN", "12"));
			
		
			if(DONATION_EXTRA_GIFT_ITEMS!=null){
				DONATION_EXTRA_GIFT_ITEMS.clear();
			}
			
			String _DONATION_EXTRA_GIFT_ITEMS = propZ.getProperty("DONATION_EXTRA_GIFT_ITEMS","");
			if(_DONATION_EXTRA_GIFT_ITEMS.length()>0){
				if(_DONATION_EXTRA_GIFT_ITEMS.indexOf(";")>0){
					for(String Items : _DONATION_EXTRA_GIFT_ITEMS.split(";")){
						DONATION_EXTRA_GIFT_ITEMS.put(Integer.valueOf(Items.split(",")[0]) , Integer.valueOf(Items.split(",")[1]));
					}
				}else{
					DONATION_EXTRA_GIFT_ITEMS.put(Integer.valueOf(_DONATION_EXTRA_GIFT_ITEMS.split(",")[0]) , Integer.valueOf(_DONATION_EXTRA_GIFT_ITEMS.split(",")[1]));
				}
			}
			return true;
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR Donation Info ::::::::::");
			_log.warning(a.getMessage());
			return false;
		}
	}

	public static boolean banIPInterner(String ip){
		return IPBAN_INTERNET.contains(ip);
	}

	public static boolean banIPMaquina(String ip){
		return IPBAN_MAQUINA.contains(ip);
	}

	public static Vector<String> getBANIPINTERNET(){
		return IPBAN_INTERNET;
	}

	public static Vector<String> getBANIPMAQUINA(){
		return IPBAN_MAQUINA;
	}

	protected static void loadIPBLOCK(){
		IPBAN_INTERNET.clear();
		IPBAN_MAQUINA.clear();

		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_ipblock";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				if(!banIPInterner(rss.getString("ipWAN"))) {
					IPBAN_INTERNET.add(rss.getString("ipWAN"));
				}

				if(!banIPMaquina(rss.getString("ipRED"))) {
					IPBAN_MAQUINA.add(rss.getString("ipRED"));
				}
			}
		}catch(SQLException a){
			_log.warning("ipblock Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}

	}

	protected static void loadPreguntasBot(){
		PREGUNTAS_BOT = null;
		PREGUNTAS_BOT = new String[800][3];
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_antibot";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			int Contador = 0;
			while(rss.next()){
				PREGUNTAS_BOT_CANT = Contador;
				PREGUNTAS_BOT[Contador][0] = String.valueOf(rss.getInt("id"));
				PREGUNTAS_BOT[Contador][1] = rss.getString("ask");
				PREGUNTAS_BOT[Contador][2] = rss.getString("answer");
				Contador++;
			}
		}catch(SQLException a){
			_log.warning("AntiBot Question Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}
	}
	
	public static enum shopData{
		ID,NOM,DESCRIP,IMA,TIPO,ID_ARCHI,ID_SEC,POS,ID_ITEMSHOW
	}
	
	protected static void loadShopData(){
		SHOP_DATA.clear();
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_shop ORDER BY zeus_shop.idsec, zeus_shop.pos ASC";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				SHOP_DATA.put(rss.getInt("id"), new HashMap<String,String>());
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID.name() , String.valueOf(rss.getInt("id")));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.NOM.name() , rss.getString("nom"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.DESCRIP.name() , rss.getString("descrip"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.IMA.name() , rss.getString("ima"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.TIPO.name() , rss.getString("tip"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID_ARCHI.name() , rss.getString("idarch"));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID_SEC.name() , String.valueOf(rss.getInt("idsec")));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.POS.name() , String.valueOf(rss.getInt("pos")));
				SHOP_DATA.get(rss.getInt("id")).put(shopData.ID_ITEMSHOW.name() , String.valueOf(rss.getInt("idItemShow")));
			}
		}catch(SQLException a){
			_log.warning("Shop Load Error->"+a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}		
	}


	public enum teleportType{
		ID,
		NOM,
		DESCRIP,
		TIP,
		COORDE,
		ID_SECC,
		NOBLE_ONLY,
		USE_FLAG,
		USE_KARMA,
		LEVEL,
		POSI,
		USE_DUALBOX
	}
	

	@SuppressWarnings("rawtypes")
	private static void getAllClases(){
		if(CLASS_LIST != null){
			if(CLASS_LIST.size()>0){
				return;				
			}
		}
		
		Map<Integer, String> classList = new HashMap<Integer, String>();
		classList.put(0, "Fighter");
		classList.put(1, "Warrior");
		classList.put(2, "Gladiator");
		classList.put(3, "Warlord");
		classList.put(4, "Knight");
		classList.put(5, "Paladin");
		classList.put(6, "Dark Avenger");
		classList.put(7, "Rogue");
		classList.put(8, "Treasure Hunter");
		classList.put(9, "Hawkeye");
		classList.put(10, "Mage");
		classList.put(11, "Wizard");
		classList.put(12, "Sorcerer");
		classList.put(13, "Necromancer");
		classList.put(14, "Warlock");
		classList.put(15, "Cleric");
		classList.put(16, "Bishop");
		classList.put(17, "Prophet");
		classList.put(18, "Elven Fighter");
		classList.put(19, "Elven Knight");
		classList.put(20, "Temple Knight");
		classList.put(21, "Swordsinger");
		classList.put(22, "Elven Scout");
		classList.put(23, "Plains Walker");
		classList.put(24, "Silver Ranger");
		classList.put(25, "Elven Mage");
		classList.put(26, "Elven Wizard");
		classList.put(27, "Spellsinger");
		classList.put(28, "Elemental Summoner");
		classList.put(29, "Oracle");
		classList.put(30, "Elder");
		classList.put(31, "Dark Fighter");
		classList.put(32, "Palus Knightr");
		classList.put(33, "Shillien Knight");
		classList.put(34, "Bladedancer");
		classList.put(35, "Assasin");
		classList.put(36, "Abyss Walker");
		classList.put(37, "Phantom Ranger");
		classList.put(38, "Dark Mage");
		classList.put(39, "Dark Wizard");
		classList.put(40, "Spellhowler");
		classList.put(41, "Phantom Summoner");
		classList.put(42, "Shillien Oracle");
		classList.put(43, "Shilien Elder");
		classList.put(44, "Orc Fighter");
		classList.put(45, "Orc Raider");
		classList.put(46, "Destroyer");
		classList.put(47, "Orc Monk");
		classList.put(48, "Tyrant");
		classList.put(49, "Orc Mage");
		classList.put(50, "Orc Shaman");
		classList.put(51, "Overlord");
		classList.put(52, "Warcryer");
		classList.put(53, "Dwarven Fighter");
		classList.put(54, "Scavenger");
		classList.put(55, "Bounty Hunter");
		classList.put(56, "Artisan");
		classList.put(57, "Warsmith");
		classList.put(88, "Duelist");
		classList.put(89, "Dreadnought");
		classList.put(90, "Phoenix Knight");
		classList.put(91, "Hell Knight");
		classList.put(92, "Sagittarius");
		classList.put(93, "Adventurer");
		classList.put(94, "Archmage");
		classList.put(95, "Soultaker");
		classList.put(96, "Arcana Lord");
		classList.put(97, "Cardinal");
		classList.put(98, "Hierophant");
		classList.put(99, "Evas Templar");
		classList.put(100, "Sword Muse");
		classList.put(101, "Wind Rider");
		classList.put(102, "Moonlight Sentinel");
		classList.put(103, "Mystic Muse");
		classList.put(104, "Elemental Master");
		classList.put(105, "Evas Saint");
		classList.put(106, "Shillien Templar");
		classList.put(107, "Spectral Dancer");
		classList.put(108, "Ghost Hunter");
		classList.put(109, "Ghost Sentinel");
		classList.put(110, "Storm Screamer");
		classList.put(111, "Spectral Master");
		classList.put(112, "Shillien Saint");
		classList.put(113, "Titan");
		classList.put(114, "Grand Khavatari");
		classList.put(115, "Dominator");
		classList.put(116, "Doomcryer");
		classList.put(117, "Fortune Seeker");
		classList.put(118, "Maestro");
		classList.put(123, "Male Soldier");
		classList.put(124, "Female Soldier");
		classList.put(125, "Trooper");
		classList.put(126, "Warder");
		classList.put(127, "Berserker");
		classList.put(128, "Male Soulbreaker");
		classList.put(129, "Female Soulbreaker");
		classList.put(130, "Arbalester");
		classList.put(131, "Doombringer");
		classList.put(132, "Male Soulhound");
		classList.put(133, "Female Soulhound");
		classList.put(134, "Trickster");
		classList.put(135, "Inspector");
		classList.put(136, "Judicator");
		
		Map<Integer, String> newMapSortedByKey = classList.entrySet().stream()
                .sorted((e1,e2) -> e1.getValue().compareTo(e2.getValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		Map<Integer, HashMap<String, String>> classData = new HashMap<Integer, HashMap<String, String>>();		
		for(int i=0;i<=136;i++){
			classData.put(i, new HashMap<String, String>());	
		}
		classData.get(0).put("CLASSBASE", "Human");classData.get(0).put("IMAGEN", "icon.skillhuman");
		classData.get(1).put("CLASSBASE", "Human");classData.get(1).put("IMAGEN", "icon.skillhuman");
		classData.get(2).put("CLASSBASE", "Human");classData.get(2).put("IMAGEN", "icon.skillhuman");
		classData.get(3).put("CLASSBASE", "Human");classData.get(3).put("IMAGEN", "icon.skillhuman");
		classData.get(4).put("CLASSBASE", "Human");classData.get(4).put("IMAGEN", "icon.skillhuman");
		classData.get(5).put("CLASSBASE", "Human");classData.get(5).put("IMAGEN", "icon.skillhuman");
		classData.get(6).put("CLASSBASE", "Human");classData.get(6).put("IMAGEN", "icon.skillhuman");
		classData.get(7).put("CLASSBASE", "Human");classData.get(7).put("IMAGEN", "icon.skillhuman");
		classData.get(8).put("CLASSBASE", "Human");classData.get(8).put("IMAGEN", "icon.skillhuman");
		classData.get(9).put("CLASSBASE", "Human");classData.get(9).put("IMAGEN", "icon.skillhuman");
		classData.get(10).put("CLASSBASE", "Human");classData.get(10).put("IMAGEN", "icon.skillhuman");
		classData.get(11).put("CLASSBASE", "Human");classData.get(11).put("IMAGEN", "icon.skillhuman");
		classData.get(12).put("CLASSBASE", "Human");classData.get(12).put("IMAGEN", "icon.skillhuman");
		classData.get(13).put("CLASSBASE", "Human");classData.get(13).put("IMAGEN", "icon.skillhuman");
		classData.get(14).put("CLASSBASE", "Human");classData.get(14).put("IMAGEN", "icon.skillhuman");
		classData.get(15).put("CLASSBASE", "Human");classData.get(15).put("IMAGEN", "icon.skillhuman");
		classData.get(16).put("CLASSBASE", "Human");classData.get(16).put("IMAGEN", "icon.skillhuman");
		classData.get(17).put("CLASSBASE", "Human");classData.get(17).put("IMAGEN", "icon.skillhuman");
		classData.get(88).put("CLASSBASE", "Human");classData.get(88).put("IMAGEN", "icon.skillhuman");
		classData.get(89).put("CLASSBASE", "Human");classData.get(89).put("IMAGEN", "icon.skillhuman");
		classData.get(90).put("CLASSBASE", "Human");classData.get(90).put("IMAGEN", "icon.skillhuman");
		classData.get(91).put("CLASSBASE", "Human");classData.get(91).put("IMAGEN", "icon.skillhuman");
		classData.get(92).put("CLASSBASE", "Human");classData.get(92).put("IMAGEN", "icon.skillhuman");
		classData.get(93).put("CLASSBASE", "Human");classData.get(93).put("IMAGEN", "icon.skillhuman");
		classData.get(94).put("CLASSBASE", "Human");classData.get(94).put("IMAGEN", "icon.skillhuman");
		classData.get(95).put("CLASSBASE", "Human");classData.get(95).put("IMAGEN", "icon.skillhuman");
		classData.get(96).put("CLASSBASE", "Human");classData.get(96).put("IMAGEN", "icon.skillhuman");
		classData.get(97).put("CLASSBASE", "Human");classData.get(97).put("IMAGEN", "icon.skillhuman");
		classData.get(98).put("CLASSBASE", "Human");classData.get(98).put("IMAGEN", "icon.skillhuman");

		classData.get(18).put("CLASSBASE", "Elf");classData.get(18).put("IMAGEN", "icon.skillelf");
		classData.get(19).put("CLASSBASE", "Elf");classData.get(19).put("IMAGEN", "icon.skillelf");
		classData.get(20).put("CLASSBASE", "Elf");classData.get(20).put("IMAGEN", "icon.skillelf");
		classData.get(21).put("CLASSBASE", "Elf");classData.get(21).put("IMAGEN", "icon.skillelf");
		classData.get(22).put("CLASSBASE", "Elf");classData.get(22).put("IMAGEN", "icon.skillelf");
		classData.get(23).put("CLASSBASE", "Elf");classData.get(23).put("IMAGEN", "icon.skillelf");
		classData.get(24).put("CLASSBASE", "Elf");classData.get(24).put("IMAGEN", "icon.skillelf");
		classData.get(25).put("CLASSBASE", "Elf");classData.get(25).put("IMAGEN", "icon.skillelf");
		classData.get(26).put("CLASSBASE", "Elf");classData.get(26).put("IMAGEN", "icon.skillelf");
		classData.get(27).put("CLASSBASE", "Elf");classData.get(27).put("IMAGEN", "icon.skillelf");
		classData.get(28).put("CLASSBASE", "Elf");classData.get(28).put("IMAGEN", "icon.skillelf");
		classData.get(29).put("CLASSBASE", "Elf");classData.get(29).put("IMAGEN", "icon.skillelf");
		classData.get(30).put("CLASSBASE", "Elf");classData.get(30).put("IMAGEN", "icon.skillelf");
		classData.get(99).put("CLASSBASE", "Elf");classData.get(99).put("IMAGEN", "icon.skillelf");
		classData.get(100).put("CLASSBASE", "Elf");classData.get(100).put("IMAGEN", "icon.skillelf");
		classData.get(101).put("CLASSBASE", "Elf");classData.get(101).put("IMAGEN", "icon.skillelf");
		classData.get(102).put("CLASSBASE", "Elf");classData.get(102).put("IMAGEN", "icon.skillelf");
		classData.get(103).put("CLASSBASE", "Elf");classData.get(103).put("IMAGEN", "icon.skillelf");
		classData.get(104).put("CLASSBASE", "Elf");classData.get(104).put("IMAGEN", "icon.skillelf");
		classData.get(105).put("CLASSBASE", "Elf");classData.get(105).put("IMAGEN", "icon.skillelf");

		classData.get(31).put("CLASSBASE", "Dark Elf");classData.get(31).put("IMAGEN", "icon.skilldarkelf");
		classData.get(32).put("CLASSBASE", "Dark Elf");classData.get(32).put("IMAGEN", "icon.skilldarkelf");
		classData.get(33).put("CLASSBASE", "Dark Elf");classData.get(33).put("IMAGEN", "icon.skilldarkelf");
		classData.get(34).put("CLASSBASE", "Dark Elf");classData.get(34).put("IMAGEN", "icon.skilldarkelf");
		classData.get(35).put("CLASSBASE", "Dark Elf");classData.get(35).put("IMAGEN", "icon.skilldarkelf");
		classData.get(36).put("CLASSBASE", "Dark Elf");classData.get(36).put("IMAGEN", "icon.skilldarkelf");
		classData.get(37).put("CLASSBASE", "Dark Elf");classData.get(37).put("IMAGEN", "icon.skilldarkelf");
		classData.get(38).put("CLASSBASE", "Dark Elf");classData.get(38).put("IMAGEN", "icon.skilldarkelf");
		classData.get(39).put("CLASSBASE", "Dark Elf");classData.get(39).put("IMAGEN", "icon.skilldarkelf");
		classData.get(40).put("CLASSBASE", "Dark Elf");classData.get(40).put("IMAGEN", "icon.skilldarkelf");
		classData.get(41).put("CLASSBASE", "Dark Elf");classData.get(41).put("IMAGEN", "icon.skilldarkelf");
		classData.get(42).put("CLASSBASE", "Dark Elf");classData.get(42).put("IMAGEN", "icon.skilldarkelf");
		classData.get(43).put("CLASSBASE", "Dark Elf");classData.get(43).put("IMAGEN", "icon.skilldarkelf");
		classData.get(106).put("CLASSBASE", "Dark Elf");classData.get(106).put("IMAGEN", "icon.skilldarkelf");
		classData.get(107).put("CLASSBASE", "Dark Elf");classData.get(107).put("IMAGEN", "icon.skilldarkelf");
		classData.get(108).put("CLASSBASE", "Dark Elf");classData.get(108).put("IMAGEN", "icon.skilldarkelf");
		classData.get(109).put("CLASSBASE", "Dark Elf");classData.get(109).put("IMAGEN", "icon.skilldarkelf");
		classData.get(110).put("CLASSBASE", "Dark Elf");classData.get(110).put("IMAGEN", "icon.skilldarkelf");
		classData.get(111).put("CLASSBASE", "Dark Elf");classData.get(111).put("IMAGEN", "icon.skilldarkelf");
		classData.get(112).put("CLASSBASE", "Dark Elf");classData.get(112).put("IMAGEN", "icon.skilldarkelf");

		classData.get(44).put("CLASSBASE", "Orc");classData.get(44).put("IMAGEN", "icon.skillorc");
		classData.get(45).put("CLASSBASE", "Orc");classData.get(45).put("IMAGEN", "icon.skillorc");
		classData.get(46).put("CLASSBASE", "Orc");classData.get(46).put("IMAGEN", "icon.skillorc");
		classData.get(47).put("CLASSBASE", "Orc");classData.get(47).put("IMAGEN", "icon.skillorc");
		classData.get(48).put("CLASSBASE", "Orc");classData.get(48).put("IMAGEN", "icon.skillorc");
		classData.get(49).put("CLASSBASE", "Orc");classData.get(49).put("IMAGEN", "icon.skillorc");
		classData.get(50).put("CLASSBASE", "Orc");classData.get(50).put("IMAGEN", "icon.skillorc");
		classData.get(51).put("CLASSBASE", "Orc");classData.get(51).put("IMAGEN", "icon.skillorc");
		classData.get(52).put("CLASSBASE", "Orc");classData.get(52).put("IMAGEN", "icon.skillorc");
		classData.get(113).put("CLASSBASE", "Orc");classData.get(113).put("IMAGEN", "icon.skillorc");
		classData.get(114).put("CLASSBASE", "Orc");classData.get(114).put("IMAGEN", "icon.skillorc");
		classData.get(115).put("CLASSBASE", "Orc");classData.get(115).put("IMAGEN", "icon.skillorc");
		classData.get(116).put("CLASSBASE", "Orc");classData.get(116).put("IMAGEN", "icon.skillorc");
		
		
		classData.get(53).put("CLASSBASE", "Dwarf");classData.get(53).put("IMAGEN", "icon.skilldwarf");
		classData.get(54).put("CLASSBASE", "Dwarf");classData.get(54).put("IMAGEN", "icon.skilldwarf");
		classData.get(55).put("CLASSBASE", "Dwarf");classData.get(55).put("IMAGEN", "icon.skilldwarf");
		classData.get(56).put("CLASSBASE", "Dwarf");classData.get(56).put("IMAGEN", "icon.skilldwarf");
		classData.get(57).put("CLASSBASE", "Dwarf");classData.get(57).put("IMAGEN", "icon.skilldwarf");
		classData.get(117).put("CLASSBASE", "Dwarf");classData.get(117).put("IMAGEN", "icon.skilldwarf");
		classData.get(118).put("CLASSBASE", "Dwarf");classData.get(118).put("IMAGEN", "icon.skilldwarf");

		classData.get(123).put("CLASSBASE", "Kamael");classData.get(123).put("IMAGEN", "icon.skillkamael");
		classData.get(124).put("CLASSBASE", "Kamael");classData.get(124).put("IMAGEN", "icon.skillkamael");
		classData.get(125).put("CLASSBASE", "Kamael");classData.get(125).put("IMAGEN", "icon.skillkamael");
		classData.get(126).put("CLASSBASE", "Kamael");classData.get(126).put("IMAGEN", "icon.skillkamael");
		classData.get(127).put("CLASSBASE", "Kamael");classData.get(127).put("IMAGEN", "icon.skillkamael");
		classData.get(128).put("CLASSBASE", "Kamael");classData.get(128).put("IMAGEN", "icon.skillkamael");
		classData.get(129).put("CLASSBASE", "Kamael");classData.get(129).put("IMAGEN", "icon.skillkamael");
		classData.get(130).put("CLASSBASE", "Kamael");classData.get(130).put("IMAGEN", "icon.skillkamael");
		classData.get(131).put("CLASSBASE", "Kamael");classData.get(131).put("IMAGEN", "icon.skillkamael");
		classData.get(132).put("CLASSBASE", "Kamael");classData.get(132).put("IMAGEN", "icon.skillkamael");
		classData.get(133).put("CLASSBASE", "Kamael");classData.get(133).put("IMAGEN", "icon.skillkamael");
		classData.get(134).put("CLASSBASE", "Kamael");classData.get(134).put("IMAGEN", "icon.skillkamael");
		classData.get(135).put("CLASSBASE", "Kamael");classData.get(135).put("IMAGEN", "icon.skillkamael");
		classData.get(136).put("CLASSBASE", "Kamael");classData.get(136).put("IMAGEN", "icon.skillkamael");
		
		Vector<Integer> ClassUltimate = new Vector<Integer>();
		
		ClassUltimate.add(88); //Duelist
		ClassUltimate.add(89); //Dreadnought
		ClassUltimate.add(90); //Phoenix Knight
		ClassUltimate.add(91); //Hell Knight
		ClassUltimate.add(92); //Sagittarius
		ClassUltimate.add(93); //Adventurer
		ClassUltimate.add(94); //Archmage
		ClassUltimate.add(95); //Soultaker
		ClassUltimate.add(96); //Arcana Lord
		ClassUltimate.add(97); //Cardinal
		ClassUltimate.add(98); //Hierophant
		ClassUltimate.add(99); //Eva's Templar
		ClassUltimate.add(100); //Sword Muse
		ClassUltimate.add(101); //Wind Rider
		ClassUltimate.add(102); //Moonlight Sentinel
		ClassUltimate.add(103); //Mystic Muse
		ClassUltimate.add(104); //Elemental Master
		ClassUltimate.add(105); //Eva's Saint
		ClassUltimate.add(106); //Shilien Templar
		ClassUltimate.add(107); //Spectral Dancer
		ClassUltimate.add(108); //Ghost Hunter
		ClassUltimate.add(109); //Ghost Sentinel
		ClassUltimate.add(110); //Storm Screamer
		ClassUltimate.add(111); //Spectral Master
		ClassUltimate.add(112); //Shilien Saint
		ClassUltimate.add(113); //Titan
		ClassUltimate.add(114); //Grand Khavatari
		ClassUltimate.add(115); //Dominator
		ClassUltimate.add(116); //Doomcryer
		ClassUltimate.add(117); //Fortune Seeker
		ClassUltimate.add(118); //Maestro
		ClassUltimate.add(131); //Doombringer
		ClassUltimate.add(132); //Soul Hound
		ClassUltimate.add(134); //Trickster
		
		
		Iterator itr = newMapSortedByKey.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Info =(Map.Entry)itr.next();
			String Nombre = (String) Info.getValue();
			int IdClass = (int)Info.getKey();
			String Race = classData.get(IdClass).get("CLASSBASE");
			String Logo = classData.get(IdClass).get("IMAGEN");
			boolean isLastClass = ( ClassUltimate.contains(IdClass));
			_classesinfo Temp = new _classesinfo(Nombre, Logo, IdClass, isLastClass, Race);
			CLASS_LIST.put(IdClass, Temp);
		}
		
		_log.warning("	Loading " + CLASS_LIST.size() + " Classes Information");
		

	}

	public static HashMap<Integer, HashMap<String, String>> getLastConnections(L2PcInstance player){
		return LastAccess.get(player.getObjectId());
	}
	
	public static void loadConnections(L2PcInstance player){
		String consulta = "SELECT * FROM zeus_connection WHERE zeus_connection.charID = ? ORDER BY zeus_connection.date DESC LIMIT 0,10";
		Connection conn = null;
		PreparedStatement psqry = null;
		int Contador = 1;
		if(LastAccess != null){
			if(LastAccess.containsKey(player.getObjectId())){
				LastAccess.get(player.getObjectId()).clear();
			}
		}
		
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, player.getObjectId());
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if(LastAccess==null){
					LastAccess.put(player.getObjectId(), new HashMap<Integer, HashMap<String, String>>());
				}else if(!LastAccess.containsKey(player.getObjectId())){
					LastAccess.put(player.getObjectId(), new HashMap<Integer, HashMap<String, String>>());
				}
				LastAccess.get(player.getObjectId()).put(Contador, new HashMap<String, String>());
				LastAccess.get(player.getObjectId()).get(Contador).put("WAN", rss.getString(1));
				LastAccess.get(player.getObjectId()).get(Contador).put("LAN", rss.getString(2));
				LastAccess.get(player.getObjectId()).get(Contador).put("DATE", rss.getString(6).replace(".0", ""));
				Contador++;
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}		
		
	}
	

	public static void loadPropertyWeb(){
		COMMUNITY_BOARD_SV_CONFIG.clear();
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File(ZEUS_CONFIG)));
			LOGINSERVERNAME = String.valueOf(propZ.getProperty("L2JLOGINSERVER_NAME"));
			USE_AUTOMATIC_HTML = Boolean.valueOf(propZ.getProperty("USE_AUTOMATIC_HTML"));
			WEB_HOP_ZONE_SERVER = String.valueOf(propZ.getProperty("WEB_HOPZONE_SERVER"));
			WEB_TOP_ZONE_SERVER = String.valueOf(propZ.getProperty("WEB_TOPZONE_SERVER"));
			GET_NAME_VAR_TYPE = String.valueOf(propZ.getProperty("GET_NAME_VAR_TYPE"));
			GET_NAME_VAR_EMAIL = String.valueOf(propZ.getProperty("GET_NAME_VAR_EMAIL"));
			GET_NAME_VAR_CODE = String.valueOf(propZ.getProperty("GET_NAME_VAR_CODE"));
			GET_NAME_VAR_DIR_WEB = String.valueOf(propZ.getProperty("GET_NAME_VAR_DIR_WEB"));
			GET_NAME_VAR_IDDONACION = String.valueOf(propZ.getProperty("GET_NAME_VAR_IDDONACION"));
			GET_NAME_VAR_SERVER_ID = String.valueOf(propZ.getProperty("GET_NAME_VAR_SERVER_ID"));
			String prop_desde_txt = String.valueOf(propZ.getProperty("COMMUNITY_SV_CONFIG_MANUAL_INPUT"));
			if(prop_desde_txt!=null){
				if(prop_desde_txt.length()>0){
					for(String proP_Indi : prop_desde_txt.split(";")){
						if(proP_Indi.split(":").length>1){
							COMMUNITY_BOARD_SV_CONFIG.add(proP_Indi);
						}else{
							_log.warning("ZeuS error loading Server Config for CB->" + proP_Indi);
						}
					}
				}
			}

			SECONDAY_PASSWORD = Boolean.valueOf(propZ.getProperty("SECUNDARY_PASSWORD","false"));
			SECONDARY_PASSWORD_NEED_EMAIL = Boolean.valueOf(propZ.getProperty("NEED_TO_HAVE_EMAIL","false"));
			SECONDARY_PASSWORD_TEMPLATE = propZ.getProperty("PASSWORD_TEMPLATE",".*");
			SECONDARY_PASSWORD_BLOCK_CHAR = Boolean.valueOf(propZ.getProperty("BLOCK_USER_TO_SET_SECONDARY_PASSWORD","True"));
			
			SECONDARY_PASSWORD_MIN_LENGHT = Integer.valueOf(propZ.getProperty("MIN_LENGHT","4"));
			SECONDARY_PASSWORD_MAX_LENGHT = Integer.valueOf(propZ.getProperty("MAX_LENGHT","10"));
			
			CAN_USE_A_OLD_PASSWORD_AS_NEW = Boolean.valueOf(propZ.getProperty("CAN_USE_A_OLD_PASSWORD_AS_NEW", "true"));
			
			SHOW_SERVER_STATUS_TO_THE_PLAYER = Boolean.valueOf(propZ.getProperty("SHOW_SERVER_STATUS_TO_THE_PLAYER","true"));
			
			SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE = Boolean.valueOf(propZ.getProperty("SELL_ACCOUNT_RECEIVER_MUST_BE_ONLINE","True"));
			
			try{
				PLAYER_BASE_TO_SHOW = Integer.valueOf(propZ.getProperty("ONLINE_BASE_PLAYER"));	
			}catch(Exception Numero){
				_log.warning("Error in load Property ONLINE_BASE_PLAYER");
				PLAYER_BASE_TO_SHOW = 20;
			}
			
			String NoShow = propZ.getProperty("NO_SHOW_ON_PLAYER_INFO","");

			COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.clear();
			
			if(NoShow.length()>0){
				for(String PartesNosShow : NoShow.split(",") ){
					COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.add(PartesNosShow + ":");
				}
			}else{
				COMMUNITY_BOARD_REGION_PLAYER_NO_SHOW_ON_INFORMATION.add("NONE");
			}
			
			if(BUFF_STORE_BUFFPROHIBITED!=null){
				BUFF_STORE_BUFFPROHIBITED.clear();
			}
			
			String BuffProhibited = propZ.getProperty("BUFFSTORE_BUFF_PROHIBITED");
			for(String Parte : BuffProhibited.split(",")){
				if(opera.isNumeric(Parte)){
					BUFF_STORE_BUFFPROHIBITED.add(Integer.valueOf(Parte));
				}
			}
			
			String ITEMS_REQUEST = propZ.getProperty("BUFFSTORE_ITEMS_REQUEST","57");
			
			
			try{
				BUFFSTORE_ITEMS_REQUEST.clear();
			}catch(Exception a){
				
			}
			//ID:NOMBRE_ITEM
			
			for(String t : ITEMS_REQUEST.split(",")){
				if(opera.exitIDItem(Integer.valueOf(t))){
					BUFFSTORE_ITEMS_REQUEST.add(t+":"+central.getNombreITEMbyID(Integer.valueOf(t)).replace(" ", "_") );
				}
			}
			
			
			ITEMS_REQUEST = propZ.getProperty("ACCOUNT_SELL_ITEMS_REQUEST","57");
			
			
			try{
				ACCOUNT_SELL_ITEMS_REQUEST.clear();
			}catch(Exception a){
				
			}
			for(String t : ITEMS_REQUEST.split(",")){
				if(opera.exitIDItem(Integer.valueOf(t))){
					ACCOUNT_SELL_ITEMS_REQUEST.add(t+":"+central.getNombreITEMbyID(Integer.valueOf(t)).replace(" ", "_") );
				}
			}
			
			
			
			
			ITEMS_REQUEST = propZ.getProperty("AUCTIONSHOUSE_ITEMS_REQUEST","57");
			
			try{
				AUCTIONSHOUSE_ITEM_REQUEST.clear();
			}catch(Exception a){
				
			}
			
			for(String t : ITEMS_REQUEST.split(",")){
				if(opera.exitIDItem(Integer.valueOf(t))){
					AUCTIONSHOUSE_ITEM_REQUEST.add(t+":"+central.getNombreITEMbyID(Integer.valueOf(t)).replace(" ", "_") );
				}
			}
			
			ITEMS_REQUEST = propZ.getProperty("BIDHOUSE_ITEMS_REQUEST","57");

			try{
				BIDHOUSE_ITEM_REQUEST.clear();
			}catch(Exception a){
				
			}
			
			for(String t : ITEMS_REQUEST.split(",")){
				if(opera.exitIDItem(Integer.valueOf(t))){
					BIDHOUSE_ITEM_REQUEST.add(t+":"+central.getNombreITEMbyID(Integer.valueOf(t)).replace(" ", "_") );
				}
			}
			
			BIDHOUSE_PERCENT_FEED = Integer.valueOf(propZ.getProperty("BIDHOUSE_PERCENT_FEED","5"));
			BIDHOUSE_FEED_MASTER = Integer.valueOf(propZ.getProperty("BIDHOUSE_FEED_MASTER","10000"));
			BIDHOUSE_DAYS = Integer.valueOf(propZ.getProperty("BIDHOUSE_DAYS","3"));
			BIDHOUSE_CANCEL_TAX_FOR_SELLER = Integer.valueOf(propZ.getProperty("BIDHOUSE_CANCELLATION_TAX_FOR_SELLER","5"));
			BIDHOUSE_CANCEL_TAX_FOR_BUYER = Integer.valueOf(propZ.getProperty("BIDHOUSE_CANCELLATION_TAX_FOR_BUYER","5"));
			BIDHOUSE_ONLY_IN_PEACE_ZONE = Boolean.valueOf(propZ.getProperty("BIDHOUSE_ONLY_IN_PEACE_ZONE", "true"));
			
			AUCTIONSHOUSE_PERCENT_FEED = Integer.valueOf(propZ.getProperty("AUCTIONSHOUSE_PERCENT_FEED","5"));
			AUCTIONSHOUSE_FEED_MASTER = Integer.valueOf(propZ.getProperty("AUCTIONSHOUSE_FEED_MASTER","10000"));
			AUCTIONSHOUSE_ONLY_IN_PEACE_ZONE = Boolean.valueOf(propZ.getProperty("AUCTIONSHOUSE_ONLY_IN_PEACE_ZONE","true"));
			
			TELEPORT_SECONDS_SKILL_TO_GO = Integer.valueOf(propZ.getProperty("TELEPORT_CASTING_SKILL","20"));
			
			AIO_PREFIX = propZ.getProperty("AIO_PREFIX", "[BUFF]");
			ALLOW_BUFFSTORE = Boolean.parseBoolean(propZ.getProperty("ALLOW_BUFFSTORE", "true"));
			CAN_USE_SHOP_ONLY_IN_PEACE_ZONE = Boolean.parseBoolean(propZ.getProperty("CAN_USE_SHOP_ONLY_IN_PEACE_ZONE", "true"));
			
			STAFF_DATA = propZ.getProperty("STAFF_DATA");
			
			String strCBAccess = propZ.getProperty("COMMUNITY_MAIN_ACCESS","0,1,2,3,4,5,6");
			
			COMMUNITY_MAIN_ACCESS.clear();
			
			ID_LOGO_SERVER = Integer.valueOf(propZ.getProperty("SERVER_LOGO_ID", "0"));
			
			int ContadorMaximo = 0;
			
			for(String M : strCBAccess.split(",")){
				if(ContadorMaximo>7){
					break;
				}
				try{
					int ID = Integer.valueOf(M);
					COMMUNITY_MAIN_ACCESS.add(ID);
				}catch(Exception a){
					_log.warning("Wrog input on Community main Acess->" + a.getMessage());
					_log.warning("Try tro create a Random Access");
					try{
						int RandInt = -1;
						int RandQ = 0;
						Random aleatorio = new Random();
						while(RandInt<0){
							RandQ = aleatorio.nextInt(Comunidad.getSizeMainOption());
							if(COMMUNITY_MAIN_ACCESS==null){
								COMMUNITY_MAIN_ACCESS.add(RandQ);
								RandInt = RandQ;
							}else{
								if(!COMMUNITY_MAIN_ACCESS.contains(RandQ)){
									COMMUNITY_MAIN_ACCESS.add(RandQ);
									RandInt = RandQ;								
								}
							}
						}
					}catch(Exception b){
						_log.warning("Error on get a Random Option -> " + b.getMessage());
					}
				}
			}
			
		}catch(Exception a){
			_log.warning("Error loading ZeuS.properties: -> " + a.getMessage());
		}
	}

	public static Vector<String> getCommunityServerInfo() {
		return COMMUNITY_BOARD_SV_CONFIG;
	}


	public static void loadConfigsTele() {
		try{
			v_Teleport.loadTeleportData();
		}catch(Exception a){
			_log.warning("Load ZeuS Teleport->"+a.getMessage());
		}
	}

	public static void loadConfigsShop() {
		try{
			loadShopData();
		}catch(Exception a){
			_log.warning("Load ZeuS Shop->"+a.getMessage());
		}

	}

	public static void loadConfigBanIP() {
		try{
			loadIPBLOCK();
		}catch(Exception a){

		}

	}

	public static void loadAntiBotQuestion() {
		try{
			loadPreguntasBot();
		}catch(Exception a){

		}
	}

  	public static general getInstance()
	{
	    return SingletonHolder._instance;
	}

  	private static void checkDualBoxData(){
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(SCRIPT_BORRADO);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}
	}

  	public static Map<Integer, HashMap<Integer, String>> getPlayerHeroes(String Raza){
  		return HeroesList.get(Raza);
  	}
  	
  	private static void _getAllHeroesPlayers(){
  		//HeroesList
  		
  		String Consulta = "SELECT characters.char_name," +
				"characters.level," +
				"characters.base_class," +
				"characters.pvpkills," +
				"characters.pkkills,"+
				"IFNULL((SELECT clan_data.clan_name FROM clan_data WHERE clan_data.clan_id = characters.clanid),\"No Clan\"),"+
				"characters.online,"+
				"characters.onlinetime"+
				" FROM characters WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.char_name";

  		Connection conn = null;
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				//classData.get(136).put("CLASSBASE",
				int IdClass = rss.getInt(3);
				String BaseClase = CLASS_LIST.get(IdClass).getRaceName();
				if(!HeroesList.containsKey(BaseClase)){
					HeroesList.put(BaseClase, new HashMap<Integer, HashMap<Integer, String>>());
				}
				HeroesList.get(BaseClase).put(IdClass, new HashMap<Integer, String>());
				HeroesList.get(BaseClase).get(IdClass).put(1, rss.getString(1));
				HeroesList.get(BaseClase).get(IdClass).put(2, String.valueOf(rss.getInt(2)));
				HeroesList.get(BaseClase).get(IdClass).put(3, String.valueOf(rss.getInt(3)));
				HeroesList.get(BaseClase).get(IdClass).put(4, String.valueOf(rss.getInt(4)));
				HeroesList.get(BaseClase).get(IdClass).put(5, String.valueOf(rss.getInt(5)));
				HeroesList.get(BaseClase).get(IdClass).put(6, rss.getString(6));
				HeroesList.get(BaseClase).get(IdClass).put(7, String.valueOf(rss.getInt(7)));
				HeroesList.get(BaseClase).get(IdClass).put(8, String.valueOf(rss.getInt(8)));
			}			
		} catch (SQLException e) {

		}
		try{
			conn.close();
		}catch(SQLException a){

		}		
  	}
  	
  	public static final Vector<_topplayer> GET_TOPS_PLAYERS(){
  		return TOP_PLAYERS;
  	}
  	
  	
  	public static void _getAllTopPlayers(){
  		try{
  			TOP_PLAYERS.clear();
  		}catch(Exception a){
  			
  		}
		
  		String Consulta = "SELECT characters.char_name," +
				"characters.level," +
				"characters.base_class," +
				"characters.pvpkills," +
				"characters.pkkills,"+
				"IFNULL((SELECT clan_data.clan_name FROM clan_data WHERE clan_data.clan_id = characters.clanid),\"No Clan\"),"+
				"characters.online,"+
				"characters.onlinetime,"+
				"charId"+
				" FROM characters "+
				"WHERE characters.accesslevel = 0";
		Connection conn = null;
		String strConsulta = Consulta;
		try{
			conn  = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(strConsulta);
			ResultSet rss = psqry.executeQuery();
			int contador = 0;
			while (rss.next()){
				_topplayer TT = new _topplayer(rss.getString(1), rss.getInt(2), rss.getInt(4), rss.getInt(5), opera.getClassName(rss.getInt(3)) , rss.getInt(9), contador);
				TOP_PLAYERS.add(TT);
				contador++;
			}
		}catch(Exception a){
			
		}
		try{
			conn.close();
		}catch(SQLException a){

		}		
	}
  	
  	public static String getBeginDate(){
  		return BeginTime;
  	}
  	

	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder{
	    protected static final general _instance = new general();
	}
	
	public static void getAllDressmeItem(){
		dressme.getInstance().getDressmePart();
	}
	
	private static void getAllDonationForAnnoucement(){
		String qry = "select cuenta,creditos from zeus_dona_creditos where entregados='NO' and emailed='false'";
		Vector<String> Cuentas = new Vector<>();
		Map<String, Integer> Creditos = new HashMap<String, Integer>();
		Connection conn=null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						Cuentas.add(rss.getString(1));
						Creditos.put(rss.getString(1), rss.getInt(2));
					}catch(SQLException e){

					}
				}
				conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}
		
		if(Cuentas!=null){
			if(Cuentas.size()>0){
				for(String Account : Cuentas){
					EmailSend.sendAllPlayerAccountInfoDonation(Account, Creditos.get(Account));
				}
			}
		}
		
	}

	public static void setDonationGift(_donaGift temp){
		DONATION_EXTRA_DATA.put(temp.getID(), temp);
	}
	
	private static void getDonationGift(){
		String Qry = "SELECT * FROM zeus_dona_gift WHERE zeus_dona_gift.canReceiveMore = 'true'";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Qry);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					_donaGift _temp = new _donaGift(rss.getInt(1), rss.getInt(2), rss.getString(3), rss.getInt(4), rss.getInt(5), rss.getString(6), ( rss.getString(7).equalsIgnoreCase("true") ? true : false ) );
					DONATION_EXTRA_DATA.put(rss.getInt(1), _temp);
				}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}		
	}
	
	private static boolean justOne = true;
	
	private static void getAllAutoUpdate(){
		if(LOAD_START){
			return;
		}
		_getMostSearch();
		getAllDonationForAnnoucement();
		if(!LOAD_START_ONE_TIME){
			cleanBD();
			v_auction_house.getAllItem();
			v_bid_house.getAllItem();
			Engine._Load();
			v_AugmentSpecial.getAllAugment();
			cbManager.loadColores();
			sellBuff.restoreOffline();
			v_partymatching.setTipoBusquedas();
			v_RaidBossInfo._loadRaidInfo();
			Region.loadColors();
			oly_monument.load();
			getDonationGift();
			LOAD_START_ONE_TIME=true;
			_dealy_reward_system.loadDealy();
			v_Dressme.getAllItemPlayer();
			_cumulativeData.load();
			colorNameTitle.getAllData();
		}
		_getAllChar();
		_getAllTopPlayers();
		_getAllHeroesPlayers();
		C_oly_buff.getSchFromOlys();
		int MinutosParaCargar = 5;
		ThreadPoolManager.getInstance().scheduleGeneral(new ReloadTopInfo(), (MinutosParaCargar * 60) * 1000);
		LOAD_START=true;
		_log.info("AutoLoad ZeuS");
		if(justOne){
			_log.warning("::::::::::::::::: LOAD PROCESS DONE :::::::::::::::::");
			justOne = false;
		}
	}
	
	
	public static class ReloadTopInfo implements Runnable{
		public ReloadTopInfo(){

		}
		@Override
		public void run(){
			try{
				LOAD_START=false;
				getAllAutoUpdate();
			}catch(Exception a){

			}

		}
	}

	public static Vector<buffcommunity> getBuffCommnunity_individual() {
		return ALL_BUFF_COMMUNITY;
	}
	
	public static final String getCOMMUNITY_BOARD_PART_EXEC(){
		return _COMMUNITY_BOARD_PART_EXEC;
	}

	public static final String getCOMMUNITY_BOARD_CLAN_PART_EXEC(){
		return _COMMUNITY_BOARD_CLAN_PART_EXEC;
	}
	
	public static final String getCOMMUNITY_BOARD_ENGINE_PART_EXEC(){
		return _COMMUNITY_BOARD_ENGINE_PART_EXEC;
	}
	
	public static final String getCOMMUNITY_BOARD_GRAND_RB_EXEC(){
		return _COMMUNITY_BOARD_GRAND_RB_EXEC;
	}
	
	public static final String getCOMMUNITY_BOARD_PARTYFINDER_EXEC(){
		return _COMMUNITY_BOARD_PARTYFINDER_EXEC;
	}
	
	public static final String getCOMMUNITY_BOARD_DONATION_PART_EXEC() {
		return _COMMUNITY_BOARD_DONATION_PART_EXEC;
	}
	
	public static final String getCOMMUNITY_BOARD_REGION_PART_EXEC(){
		return _COMMUNITY_BOARD_REGION_PART_EXEC;
	}
	
}


