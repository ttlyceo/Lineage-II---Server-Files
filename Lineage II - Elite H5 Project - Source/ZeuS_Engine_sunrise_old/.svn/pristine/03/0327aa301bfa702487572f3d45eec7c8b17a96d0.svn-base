package ZeuS.language;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.gameserver.model.actor.instance.L2PcInstance;

public class Lang {
	private final static Logger _log = Logger.getLogger(Lang.class.getName());
	private String _LANGUAGE;
	private String _FOLDER;
	private String _ABBREVIATION;
	
	private Vector<String>cmd_char = new Vector<String>();
	private Vector<String>cmd_ZeuS_Adm = new Vector<String>();
	
	public enum BUTTON{
		Buffer,
		Gopartyleader,
		Flagfinder,
		Teleport,
		Shop,
		Warehouse,
		AugmentManager,
		SubClass,
		Profession,
		DropSearch,
		pvppklog,
		Symbolmaker,
		BugReport,
		Transformation,
		RemoveAttri,
		SelectAugment,
		SelectEnchant,
		SelectElemental,
		ClasesStadistic,
		RaidBossInfo,
		Dressme,
		HeroList,
		MyInfo,
		blacksmith,
		gmlist,
		charclanoption,
		partymatching,
		AuctionHouse,
		BidHouse,
		castleManager,
		commandinfo,
		OlyBuffer,
		donation,
		admCommand,
		vote,
		configPanel,
		challange,
		clanAndAlly
	}
	
	//COMMANDS
	public String		ZEUS_CONFIG 	 = "";
	public String		ZEUS_TELE 	 = "";
	public String		ZEUS_SHOP 	 = "";
	public String		OLY_BAN 	 = "";
	public String		OLY_UNBAN 	 = "";
	public String		OLY_RESET_POINT 	 = "";
	public String		OLY_POINT 	 = "";
	public String		ZEUS_BANIP 	 = "";
	public String		ZEUS_IPBLOCK 	 = "";
	public String		ZEUS_BOTZONE 	 = "";
	public String		ZEUS_RECALLALL 	 = "";
	public String		ZEUS_BOT_CANCEL 	 = "";
	public String		ZEUS_GMPANEL 	 = "";
	public String		ZEUS_FAKE		= "";
	public String		ZEUS_FAKE_CLONE	= "";
	public String		ZEUS_FAKE_REMOVE	= "";
	public String		ZEUS 	 = "";
	public String		OLY_BUFF 	 = "";
	public String		CHANGELANG	= "";
	public String		CHARPANEL 	 = "";
	public String		VOTE 	 = "";
	public String		ACC_REGISTER 	 = "";
	public String		CHANGEEMAIL 	 = "";
	public String		ACCRECOVERY 	 = "";
	public String		CHANGEPASSWORD 	 = "";
	public String		REMOVESECONDARYPASSWORD 	 = "";
	public String		MOVECHAR 	 = "";
	public String		COMBINETALISMAN 	 = "";
	public String		EXP_STATUS 	 = "";
	public String		DRESSME 	 = "";
	public String		CHECKBOT 	 = "";
	public String		STAT 	 = "";
	public String		FIXME 	 = "";
	public String		MYINFO 	 = "";
	public String		MAKEANCIENTADENA 	 = "";
	public String		PARTY 	 = "";
	public String		BUFFSTORE 	 = "";
	public String		SELLACCOUNT 	 = "";
	public String		SELLCLAN 	 = "";
	public String		JOINRAID 	 = "";
	public String		LEAVERAID 	 = "";
	public String		ENGANGE 	 = "";
	public String		GOTOLOVE 	 = "";
	public String		DIVORCE 	 = "";
	public String		DEPOSIT 	 = "";
	public String		WITHDRAW 	 = "";
	public String		DUEL 	 = "";
	public String		AUTO_CP		= "";
	public String		AUTO_MP		= "";
	public String		AUTO_HP		= "";
	public String		WISHLIST	= "";
	public String		CLANPENALTY 	 = "";
	public String		ATTACKLIST 	 = "";
	public String		UNDERATTACKLIST 	 = "";
	public String		WARLIST 	 = "";
	public String		INSTANCEZONE 	 = "";
	public String		OLYMPIADSTAT 	 = "";
	public String		MYBIRTHDAY 	 = "";
	public String		UNSTUCK 	 = "";
	public String		CHANNELCREATE 	 = "";
	public String		CHANNELDELETE 	 = "";
	public String		CHANNELINVITE 	 = "";
	public String		CHANNELKICK 	 = "";
	public String		CHANNELLEAVE 	 = "";
	public String		CHANNELINFO 	 = "";
	public String		FRIENDINVITE 	 = "";
	public String		FRIENDLIST 	 = "";
	public String		FRIENDDEL 	 = "";
	public String		BLOCK 	 = "";
	public String		UNBLOCK 	 = "";
	public String		BLOCKLIST 	 = "";
	public String		GMLIST 	 = "";
	public String		GM 	 = "";
	public String		GMCANCEL 	 = "";
	public String		DEALY_LOGIN	= "";
	
	//GENERAL	
	public String	YOU_CANT_ACCESS_TO_THIS_SECTION 	 = "";
	public String	ENTER_NUMBERS_ONLY 	 = "";
	public String	DO_NOT_LEAVE_EMPTY_FIELD 	 = "";
	public String	THIS_PROCESS_MAY_TAKE_FEW_MINUTES 	 = "";
	public String	ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION 	 = "";
	public String	CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_DEATH 	 = "";
	public String	CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG 	 = "";
	public String	CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK 	 = "";
	public String	YOU_ARE_ALREADY_NOBLE 	 = "";
	public String	YOU_ARE_ALREADY_LEVEL_85 	 = "";
	public String	THE_NAME_ENTERED_IS_NOT_VALID 	 = "";
	public String	THE_NAME_CHANGE_WAS_SUCCESSFUL 	 = "";
	public String	ITEM_ID_NOT_FOUND 	 = "";
	public String	INVALID_TARGER_IS_NOT_A_PLAYER 	 = "";
	public String	THE_PLAYER_IS_NOBLE 	 = "";
	public String	THE_PLAYER_IS_NOT_NOBLE_ANYMORE 	 = "";
	public String	THE_PLAYER_IS_NOT_NOBLE	= "";
	public String	THE_PLAYER_IS_HERO	= "";
	public String	YOU_ARE_NOBLE_NOW 	 = "";
	public String	YOU_ARE_NOT_NOBLE 	 = "";
	public String	AUGMENT_WEAPON_REMOVE_IT 	 = "";
	public String	AUGMENT_SPECIAL_REMOVE_AUGMENT 	 = "";
	public String	YOU_CAN_CHANGE_YOUR_TITLE 	 = "";
	public String	YOU_CAN_NOT_TRADE_WHILE_FLAG 	 = "";
	public String	YOU_CAN_NOT_TRADE_WHILE_PK 	 = "";
	public String	DISABLE_BY_ADMIN	= "";
	public String	YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL	= "";
	public String	CANT_USE_IN_COMBAT_MODE	= "";
	public String	CANT_USE_IN_SIEGE_OR_TOWN_WAR	= "";
	public String	YOU_ARE_OLY_BANNED	= "";
	public String	WELCOME	= "";
	public String	MAXIMUM_ALLOWED_IP_ARE_$countAllow_CHAR_$name_ARE_DISCONNECTED	= "";
	public String	DATA_SAVED = "";
	public String	DATA_UPDATE = "";
	public String	YOU_CANT_USE_MY_SERVICES_RIGHT_NOW = "";
	public String	ARE_INSIDE_A_NON_SUMMON_ARE = "";
	public String	ARE_INSIDE_A_SIEGE_ZONE = "";
	public String	ARE_INSIDE_A_CASTLE = "";
	public String	YOU_ARE_INSIDE_A_INSTANCE_CANT_ENTER_NOW = "";
	public String	ARE_INSIDE_A_CH = "";
	public String	ARE_NOT_IN_PEACE_ZONE = "";
	public String	PLAYER_IS_NOT_ONLINE = "";
	public String	TARGET_IS_NOT_PARTY_LEADER = "";
	public String	PLAYER_IS_OFFLINE = "";
	public String	DISABLED_FOR_PLAYER_IN_JAIL ="";
	public String	PLAYER_IS_IN_JAIL = "";
	public String	I_IN_JAIL = "";
	public String	PLAYER_IN_BLOCK_LIST = "";
	public String	PLAYER_HAS_YOU_BLOCK_LIST = "";
	public String	PLAYER_IS_IN_COMBAT_DUEL = "";
	public String	YOU_OR_TARGET_IS_IN_BOAT = "";
	public String	YOU_OR_TARGET_IS_IN_OLY_OBSERVER = "";
	public String	YOU_OR_TARGET_IS_IN_OLY = "";
	public String	YOU_OR_TARGET_IS_IN_EVENT = "";
	public String	ONLY_PARTY_LEADER_CAN_USE_THIS = "";
	public String	TRY_AGAIN_MAYBE_THE_ADMINGM_CHANGE_SOMETHING = "";
	public String	YOU_NEED_TO_HAVE_A_CLAN = "";
	public String	YOU_NEED_TO_BE_MORE_SPECIFIC	= "";
	public String	YOU_WILL_BE_REVIVE_ON	= "";
	public String	SENDING_EMAIL_TO	= "";
	public String	YOU_CANT_USE_THIS_IN_PARTY	= "";
	public String	JAIL_BAIL_COMPLETE	= "";
	public String	SELECTED_PLAYER_REFUSE_PARTY_REQUEST	= "";
	public String	WAIT_TO_SEND_ANOTHER_POKE	= "";
	public String	RAIDBOSS_JOIN	= "";
	public String	YOUR_ON_A_INSTANCE_CANT_PARTICIPATE	= "";
	public String	YOUR_ARE_IN_OTHER_EVENT	= "";
	public String	YOUR_ARE_IN_THIS_EVENT	= "";
	public String	YOUR_CANT_JOIN_THIS_EVENT_BECOUSE_OLYS	= "";
	public String	YOUR_ARE_ALREADY_PARTICIPATING_IN_THE_EVENT_WITH_OTHER_IP	= "";
	public String	YOU_CANT_USE_THE_SHOP_IN_THIS_ZONE	= "";
	public String	YOU_NAME_HAD_MORE_THAN_16_CHARACTERS = "";
	public String	THE_NEW_NAME_ALREADY_EXISTS = "";
	//OLYMPIAD_SCHEME
	public String OLYMPIAD_SF_NO_MORE_BUFF_TO_USE	= "";
	public String OLYMPIAD_SF_CAN_USE_BUFFER_TOWN_EVENT = "";
	public String OLY_INSTANCE_ENTER = "";
	
	//GENERAL NEED
	public String	NEED_TO_BE_NOBLE 	 = "";
	public String	NEED_TO_HAVE_A_LEVEL_GREATER_THAN 	 = "";
	public String	NEED_TO_BE_LEVEL_FOR_THIS_OPERATION 	 = "";
	public String	NEED_TO_HAVE_A_CLAN_EQUAL_OR_GREATER_THAN 	 = "";
	public String	NEED_TO_BE_IN_PARTY 	 = "";
	public String	NEED_ENTER_THE_REQUESTED_DATA 	 = "";

	//AUCTION HOUSE
	public String	AH_ITEM_IS_NOT_FOR_SELL_NOW 	 = "" ;
	public String	AH_ITEM_IS_EQUIPPED 	 = "" ;
	public String	AH_TRY_TO_CHEAT 	 = "" ;
	public String	AH_NO_HAVE_FEED 	 = "" ;
	public String	AH_NO_STACKABLE_ITEM_CHANGE_TO_ONE 	 = "" ;
	public String	AH_CANCEL_THE_ITEM_TO_SELL_AGAIN 	 = "" ;
	public String	AH_CREATE_PROCESS_FAIL_CHECK_QUANTITY 	 = "" ;
	public String	AH_WROG_QUANTITY_TO_BUY 	 = "" ;
	public String	AH_EMAIL_TITLE_NEW_SELL	= "";
	public String	AH_EMAIL_MESSAGE_NEW_SELL = "";
	public String	AH_QUESTION_YOU_WANT_CREATE_THIS_AUCTION	= "";
	public String	AH_ONLY_IN_PEACE_ZONE	= "";
	
	//BID HOUSE
	public String	BH_ITEM_IS_NOT_FOR_SELL_NOW 	 = "" ;
	public String	BH_ITEM_IS_EQUIPPED 	 = "" ;
	public String	BH_TRY_TO_CHEAT 	 = "" ;
	public String	BH_NO_HAVE_FEED 	 = "" ;
	public String	BH_NO_STACKABLE_ITEM_CHANGE_TO_ONE 	 = "" ;
	public String	BH_CANCEL_THE_ITEM_TO_SELL_AGAIN 	 = "" ;
	public String	BH_CREATE_PROCESS_FAIL_CHECK_QUANTITY 	 = "" ;
	public String	BH_PLAYER_BUY_YOU_THE_ITEM 	 = "" ;
	public String	BH_PLAYER_BUY_YOU_THE_QUANTITY_ITEM 	 = "" ;
	public String	BH_WROG_QUANTITY_TO_BUY 	 = "" ;
	public String	BH_QUESTION_YOU_HAVE_MAKE_NEW_OFFER	= "";
	public String	BH_QUESTION_YOU_GONNA_CREATE_NEW_BID	="";
	public String	BH_EMAIL_TITLE_YOU_LOOSE_THE_ITEM_BID_BY_TIME	="";
	public String	BH_EMAIL_MESSAGE_YOU_LOOSE_THE_ITEM_BID_BY_TIME	="";
	public String	BH_EMAIL_TITLE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT = "";
	public String	BH_EMAIL_MESSAGE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT = "";
	public String	BH_EMAIL_TITLE_BID_WAS_CANCEL_BY_OWNER = "";
	public String	BH_EMAIL_MESSAGE_BID_WAS_CANCEL_BY_OWNER = "";
	public String	BH_EMAIL_TITLE_YOUR_LOST_YOUR_BID_PLACE = "";
	public String	BH_EMAIL_MESSAGE_YOUR_LOST_YOUR_BID_PLACE = "";
	public String	BH_SORRY_YOUR_BID_HAS_AN_OFFER_QUESTION = "";
	public String	BH_SORRY_YOUR_BID_HAS_AN_OFFER_NO_ITEM_PENALTY = "";
	public String	BH_SORRY_YOU_HAVE_THE_FIRTS_PLACE_IN_THE_BID_NEED_PAY_PENALTY	= "";
	public String	BH_EMAIL_TITLE_CANCEL_MY_OFFERT = "";
	public String	BH_EMAIL_MESSAGE_CANCEL_MY_OFFERT = "";
	public String	BH_SORRY_BUT_YOU_DONT_HAVE_ITEMS_TO_PAY_THE_PENALTY = "";
	public String	BH_ONLY_IN_PEACE_ZONE	= "";
	
	//WISH LIST
	public String 	WISH_LIST_NEW_AUCTION_BID_HAS_BEEN_CREATED	= "";
	public String	WISH_LIST_BE_MORE_PRECISE_FOR_SEARCH	= "";
	public String	WISH_LIST_YOU_HAVE_THIS_ITEM_IN_LIST	= "";
	public String	WISH_LIST_WAS_CREATED_WITH_DURATION_SEVEN_DAYS	= "";
	public String	WISH_LIST_CANT_ADD_MORE_TO_YOUR_LIST	= "";
	
	//GENERAL ITEM
	public String	YOU_DONT_HAVE_THE_REQUESTED_ITEM 	 = "";
	// VOTE SYSTEM
	public String	YOU_CANT_VOTE_RIGHT_NOW 	 = "";
	public String	YOU_VOTE_WAS_NOT_TAKEN 	 = "";
	public String	VOTEREWARD_REMEMBER_VOTE_12 	 = "";
	public String	VOTEREWARD_WAIT_TIME_IS_OVER 	 = "";
	public String	VOTEREWARD_WAIT_MESSAGE 	 = "";
	public String	VOTEREWARD_ERROR_MESSAGE 	 = "";
	public String	VOTOREWARD_SCAM_MESSAGE 	 = "";
	public String	VOTOREWARD_GETTING_VOTES 	 = "";
	public String	VOTEREWARD_ASK	= "";
	//AIO SYSTEM 
	public String	THE_NEW_AIO_NAME_EXIST 	 = "";
	public String	AIO_CHAR_CREATION_STARTED 	 = "";
	public String	AIO_CHAR_ENDED_WITHOUT_PROBLEMS 	 = "";
	public String	AIO_CHAR_NAME_MUST_HAVE_CHARACTERS	= "";
	
	//STATUS
	public String	BUTTON_ENABLE 	 = "";
	public String	BUTTON_DISABLE 	 = "";
	public String	BUTTON_BACK 	 = "";
	public String	BUTTON_NEXT 	 = "";
	public String	OPTION_ALIVE = "";
	public String	OPTION_WAITING = "";
	public String	OPTION_IN_FIGHT = "";
	public String	OPTION_DEAD = "";
	
	//DROP SEARCH
	public String	DS_YOU_NEED_TO_CLOSE_OBSERVER_MODE_TO_OBSERVER_ANOTHER	= "";
	public String	DS_YOU_WANT_TO_OBSERVER_THIS = "";
	public String	DS_MOB_OR_RB_BLOCKED_TELEPORT = "";
	public String	DS_YOU_WANT_TO_TELEPORT_THIS	= "";
	
	//OLYMPIAD
	public String	OLY_BUFFER_MANAGER_TIME_OUT	= "";
	public String	OLY_BUFFER_NEED_TO_ACTIVE_CREATE_SCHEME_FOR_USE = "";
	public String	OLY_BUFFER_YOU_CANT_ADD_MORE_BUFF_MAXIMUM = "";
	public String	OLY_BUFFER_YOU_CANT_ADD_MORE_SCHEMES_MAXIMUM = "";
	public String	OLY_BUFFER_THE_SCHEME_NAME_ALREADY_EXIST = "";
	
	//ENGINE
	public String	ERROR_IN_THIS_PROCESS 	 = "";
	public String	TYPING_ERROR 	 = "";
	public String	WELLCOME_TITLE 	 = "";
	public String	OLY_YOU_HAVE_BEEN_UNBANNED_FROM_OLY_BY_$gmName 	 = "";
	public String	OLY_YOU_HAVE_BEEN_BANNED_FROM_OLY_BY_$gmName 	 = "";
	
	//BUG REPORT
	public String	BUG_REPORT_LIST_OF_TYPE = "";
	public String	BUG_REPORT_SENT = "";

	//CLAN
	public String	CLAN_REMOVAL_PROCESS_CANCELED 	 = "";
	public String	ENTER_NAME_NEW_OWNER_CLAN 	 = "";
	public String	NO_MORE_CLAN_SKILL_TO_LEARN 	 = "";
	public String	ENTER_NAME_OF_THE_ALLIANCE 	 = "";
	public String	YOU_DONT_HAVE_CLAN 	 = "";
	public String	CLAN_NAME_ALREADY_EXISTS 	 = "";
	public String	CLAN_NAME_CHANGE 	 = "";
	public String	YOU_CAN_NOT_REMOVE_THIS_POST	= "";
	public String 	POST_REMOVED	= "";
	public String	CLAN_NAME_FAIL	= "";
	public String	COMMUNITY_SYSTEM_DISABLE	= "";
	
	//FIND PARTY REQUEST SYSTEM
	public String	FP_TIME_OUT = "";
	public String	FP_REQUEST_SENDING = "";
	public String	FP_WAITING_TO_SEND_AGAIN = "";
	public String	FP_NO_MORE_SLOT = "";
	public String	FP_END_REQUEST = "";
	public String	FP_PLAYER_REFUSED_PARTY_REQUEST = "";
	public String	FP_REQUEST_SENDING_TO_PLAYER_YOU_NEED_TO_WAIT = "";
	public String	FP_YOU_CANT_SEND_PARTY_REQUEST_NOW	= "";
	
	//DONATION
	public String	SENDING_DONATION_VOUCHER_TO_ADMINISTRATOR 	 = "";
	public String	VOUCHER_SENT_SUCCESSFULLY 	 = "";
	public String	VOUCHER_SENT_ERROR	= "";
	public String	DONATION_YOU_HAVE_$donationCount_ON_YOU_ACCOUNT 	 = "";
	public String	DONATION_GIVE_DC_BUTTON 	 = "Give me my DC";
	public String	DONATION_TYPE	= "";
	public String	DONATION_NO_PREMIUM_DATA_ADDED 	 = "" ;
	public String	DONATION_NO_HAVE_CLAN_FOR_PREMIUM_DATA 	 = "" ;
	public String	DONATION_YOU_NEED_TO_BE_CLAN_LEADER_TO_GET_PREMIUM_DATA 	 = "" ;
	public String	DONATION_YOU_GONNA_BUY_255_RECOMMENDS 	 = "" ;
	public String	DONATION_YOU_WANT_TO_BE_NOBLE 	 = "" ;
	public String	DONATION_YOU_wANT_TO_CHANGE_YOUR_SEX 	 = "" ;
	public String	DONATION_YOU_GONNA_TRANSFORM_THIS_CHAR_INTO_A_AIO_BUFFER 	 = "" ;
	public String	DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT 	 = "" ;
	public String	DONATION_PREMIUM_DATA_IS_NOT_AVAIBLE_NOW 	 = "" ;
	public String	DONATION_CONGRATULATION_YOU_GET_DONATION_ITEMS 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_HAS_MORE_DAYS 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_ITS_NOW_ON 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ELEMENTAL_POWER 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ENCHANT 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_NEW_LEVEL_IS 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_ADQUIERE_FAME 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_PK_HAS_REDUCE_TO 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_CLAN_LVL_HAS_INCREASE 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_CLAN_REPUTATION_HAS_INCREASE 	 = "" ;
	public String	DONATION_CONGRATULATION_YOUR_RECEIVED_CLAN_SKILLS 	 = "" ;
	public String	DONATION_ONLY_CLAN_LEADER_CAN_LVL_UP 	 = "" ;
	public String	DONATION_YOU_HAVE_THE_SAME_ENCHANT_ON_YOUR_ITEM 	 = "" ;
	public String	DONATION_THIS_ITEM_CANT_ENCHANT_CHANGE 	 = "" ;	
	public String	DONATION_PREMIUM_ACCOUNT_IS_OVER	= "";
	public String	DONATION_PREMIUM_CLAN_IS_OVER	= "";
	public String	DONATION_YOU_WANT_CHANGE_YOUR_NAME = "";
	public String	DONATION_YOU_WANT_CHANGE_YOUR_CLAN_NAME = "";
	public String	DONATION_ERROR_CREATING_PREMIUM	= "";

	public String	DONATION_ASK_CHAR_LEVEL = "";
	public String	DONATION_ASK_CHAR_FAME = "";
	public String	DONATION_ASK_CHAR_REDUCE_PK = "";
	public String	DONATION_ASK_CLAN_LEVEL = "";
	public String	DONATION_ASK_CLAN_REPUTATION = "";
	public String	DONATION_ASK_CLAN_SKILL = "";	
	public String	DONATION_ASK_ENCHANT_YOU_WANT_TO_ENCHANT_THE_ITEM_$item_TO_$enchant_$cost = "";
	public String	DONATION_ASK_ENCHANT_YOU_WANT_TO_ELEMENTAL_THE_ITEM_$item_TO_$enchant_$elemental_$cost = "";
	public String	DONATION_PAYPAL_YOU_NEED_TO_WAIT_X_SECOND_FOR_OTHER_EMAIL_PETITION = "";
	public String	DONATION_PAYPAL_SENDING_LINK_TO_YOUR_EMAIL = "";
	
	//CHALLENGE
	public String	CHALLENGE_WIN 	 = "";
	public String	CHALLENGE_LOOSE 	 = "";
	public String	CHALLENGE_THE_PLAYER_GET_LEVEL_85 	 = "";
	public String	CHALLENGE_THE_PLAYER_GET_NOBLE 	 = "";
	public String	CHALLENGE_WON_MESSAGE 	 = "";
	public String	CHALLENGE_ANNOUCEMENT_PLAYER_FOUND_NPC 	 = "";
	public String	CHALLENGE_NOBLE_WIN	= "";
	public String	CHALLENGE_LV85_WIN	= "";
	//FLAG FINDER
	public String	FLAG_FINDER_NO_PVP 	 = "";
	public String	FLAG_FINDER_PLAYER_FOUND 	 = "";
	public String	FLAG_FINDER_COMMING_FOR_YOU 	 = "";
	//CHAT
	public String	CHAT_LEVEL	= "";
	public String	CHAT_PVP 	= "";
	public String	CHAT_LIFETIME	= "";
	//PARTY FINDER
	public String	PARTY_FINDER_NO_PARTY_LEADER_DEATH 	 = "";
	public String	PARTY_FINDER_NO_PARTY_LEADER_FLAG 	 = "";
	public String	PARTY_FINDER_NO_PARTY_LEADER_NOBLE 	 = "";
	public String	PARTY_FINDER_NO_PARTY_LEADER_INSTANCE_ZONE 	 = "";
	public String	PARTY_FINDER_NO_PARTY_NO_SUMMON_ZONE 	 = "";
	public String	PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER 	 = "";
	public String	PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION 	 = "";
	//BOT SYSTEM
	public String	BOT_VERIFICATION_SEND_TO_$player 	 = "";
	public String	BOT_CAN_NOT_SEND_IN_OLY 	 = "";
	public String	BOT_CAN_NOT_SEND_IN_YOUR_SELF 	 = "";
	public String	BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$timeEvery_NEXT_CHECK_IN_$timeNextCheck 	 = "";
	public String	BOT_THIS_COMMAND_ONLY_NOBLE 	 = "";
	public String	BOT_THIS_COMMAND_ONLY_HERO 	 = "";
	public String	BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level 	 = "";
	public String	BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime 	 = "";
	public String	BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS 	 = "";
	public String	BOT_THIS_PLAYER_HAVE_KARMA 	 = "";
	public String	BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT 	 = "";
	public String	BOT_THE_PLAYER_IS_NOT_A_BOT 	 = "";
	public String	BOT_$player_HAVE_SENT_YOU_A_ANTIBOT_VERIFICATION 	 = "";
	public String	BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time 	 = "";
	public String	BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER 	 = "";
	public String	BOT_ANNOUCEMENT_REMOVE_ITEM 	 = "";
	public String	BOT_INACTIVE_X_THAN_$minutes 	 = "";
	public String	BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK	= "";
	public String	BOT_MSGBOX_ARE_YOU_SURE = "";
	public String	BOT_SECONDS_LEFT_TO_ANSWER = "";
	//DRESSME
	public String	DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME 	 = "";
	public String	DRESSME_ONLY_HAVE_TO_CHOOSE 	 = "";
	public String	DRESSME_CHOOSE_WRONG 	 = "";
	public String	DRESSME_DISABLED 	 = "";
	public String	DRESSME_YOU_DONT_HAVE_DRESSME_CONFIG 	 = "";
	public String	DRESSME_THE_COST_FOR_NEW_DRESSME 	 = "";
	public String	DRESSME_YOU_NEED_TO_HAVE_ANY_DRESSME_ADDED 	 = "";
	public String	DRESSME_YOU_NEED_TO_PUT_ON_A_DRESSME 	 = "";
	public String	DRESSME_SERVER_CONFIG_EMPTY	= "";
	public String	DRESSME_YOU_NEED_TO_WAIT_TO_TRY_ITEM	= "";
	public String	DRESSME_YOU_NEED_TO_SET_A_DRESSME_BEFORE	= "";
	public String	DRESSME_YOU_CANT_USE_THIS_DRESSME_IS_EMPTY	= "";
	public String	DRESSME_JUST_FOR_HERO = "";
	public String	DRESSME_JUST_FOR_NOBLE = "";
	public String	DRESSME_JUST_FOR_TOP_PVP_PK = "";
	public String	DRESSME_WANT_TO_BUY_THIS_DRESSME = "";
	public String	DRESSME_WANT_TO_USE_THIS_DRESSME = "";
	public String	DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED = "";
	public String	DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CLAN_EXCLUSIVE = "";
	public String	DRESSME_ARE_YOU_SURE_YOU_DELETE_ALL_DRESSME_ITEM_FROM_DRESSME_ID = "";
	public String	DRESSME_ARE_YOU_SURE_YOU_DELETE_THIS_ITEM_FROM_DRESSME_ID = "";
	public String	DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CHAR_EXCLUSIVE = "";
	public String	DRESSME_CLAN_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL = "";
	public String	DRESSME_CHAR_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL = "";	
	//CANCEL BUFF SYSTEM
	public String	CANCEL_BUFF_RETURNED_IN_SECONDS 	 = "";
	public String	CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED 	 = "";
	//BUFFER SYSTEM
	public String	BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN 	 = "";
	public String	BUFFERCHAR_LENGTH_OF_THE_NAME_MUST_BE_LESS_THAN_$size 	 = "";
	public String	BUFFERCHAR_NAME_ALREADY_EXISTS_IN_YOU_SCHEMES 	 = "";
	public String	BUFFERCHAR_SCHEME_NAME_SAVED 	 = "";
	public String	BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET 	 = "";
	public String	BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET 	 = "";
	public String	BUFFERCHAR_YOU_CAN_NOT_CANCEL_YOU_PET_BUFF 	 = "";
	public String	BUFFERCHAR_WROG_SCHEME_NAME 	 = "";
	public String	BUFFERCHAR_YOU_NOT_HAVE_PET 	 = "";
	public String	BUFFERCHAR_JUST_IN_PEACE_ZONE	= "";
	public String	BUFFERCHAR_YOU_DONT_HAVE_PREMIUM_SYSTEM	= "";
	public String	BUFFERCHAR_YOU_PREMIUM_SYSTEM_NO_HAVE_PREMIUM_BUFF 	 = "" ;
	public String	BUFFERCHAR_YOU_PREMIUM_SYSTEM_IS_OVER 	 = "" ;
	public String	BUFFERCHAR_YOU_NEED_A_MINIMUN_LEVEL_TO_USE_THIS 	 = "" ;
	public String	BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE 	 = "" ;
	public String	BUFFERCHAR_SCHEME_CREATED 	 = "" ;
	public String	BUFFERCHAR_SCHEME_NAME_CHANGE 	 = "" ;
	public String	BUFFERCHAR_SCHEME_ICON_CHANGE 	 = "" ;
	public String	BUFFERCHAR_SCHEME_REMOVE_BUFF 	 = "" ;
	public String	BUFFERCHAR_SCHEME_REMOVED 	 = "" ;
	public String	BUFFERCHAR_SCHEME_CANT_ADD_MORE_BUFF 	 = "" ;
	public String	BUFFERCHAR_SCHEME_CANT_ADD_MORE_DANCE	 = "" ;
	public String	BUFFERCHAR_YOU_NEED_TO_BE_ALIVE	= "";
	public String	BUFFERCHAR_ARE_YOU_SURE_ABOUT_REMOVE_SCHEME	= "";
	public String	BUFFERCHAR_YOU_CANT_ADD_MORE_SCHEME = "";
	public String	BUFFERCHAR_YOU_WANT_HEAL = "";
	public String	BUFFERCHAR_YOU_WANT_CANCEL = "";
	public String	BUFFERCHAR_YOU_WANT_TO_USE_THIS_SCHEME = "";
	public String	BUFFERCHAR_YOU_WANT_TO_USE_YOU_OWN_SCHEME = "";
	
	public String	BUFFER_BUFFSTORE_WROG_ZONE_TO_CREATE_SPOT = "";
	public String	BUFFER_BUFFSTORE_ENTER_PRICE_CORRECTLY = "";
	public String	BUFFER_BUFFSTORE_MAX_SCHEME = "";
	public String	BUFFER_BUFFSTORE_MAX_BUFF_POUR_SCHEME = "";
	public String	BUFFER_BUFFSTORE_YOU_HAVE_THIS_SCHEME_NAME_ON_THIS_BUFFSTORE_SELLER = "";
	
	//TELEPORT
	public String	TELEPORT_YOU_CAN_NOT_USE_THE_TELEPORT_IN_COMBAT_MODE 	 = "";
	public String	TELEPORT_THIS_AREA_DOES_NOT_ALLOW_DUAL_BOX 	 = "";
	public String	TELEPORT_DO_YOU_WANT_TELEPORT_TO = "";
	public String	TELEPORT_YOU_CANT_TELEPORT_WHILE_CASTING_OR_IN_TELEPORTATION = "";
	//ACCOUNT
	public String	ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED 	 = "";
	public String	ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email 	 = "";
	public String	ACCOUNT_BUTTON_SEND_ME_A_VALIDATION_CODE 	 = "";
	public String	ACCOUNT_BUTTON_CHECK_BUTTON 	 = "";
	public String	ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_AGAIN 	 = "";
	//DELEVEL
	public String	DELEVEL_REMOVE_INVALID_SKILL 	 = "";
	
	//ELEMENTAL AND ENCHANT
	public String	ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED	= "";
	public String	ENCHANT_YOU_MOVE_THE_ITEM	= "";
	public String	ENCHANT_ITEM_NO_MEET_PARAMETERS_TO_ENCHANT	= "";
	public String	ENCHANT_CONGRATULATION_ITEM_HAS_NEW_ENCHANT = "";

	
	//GO PARTY LEADER
	
	public String	GPL_PARTY_LEADER_IN_OBSERVE_MODE 	 = "" ;
	public String	GPL_PARTY_LEADER_IN_EVENT 	 = "" ;
	public String	GPL_PARTY_LEADER_ARE_YOU 	 = "" ;
	public String	GPL_PARTY_LEADER_IN_RESTRICTED_AREA 	 = "" ;
	public String	GPL_PARTY_LEADER_IS_IN_SIEGE 	 = "" ;
	
	//PROFESSION AND SUBCLASS SYSTEM
	
	public String	PRO_COME_BACK_LV20 	 = "" ;
	public String	PRO_COME_BACK_LV40 	 = "" ;
	public String	PRO_COME_BACK_LV76 	 = "" ;
	public String	SUBCLASS_NO_SUBCLASS_AVAILABLE 	 = "" ;
	public String	SUBCLASS_YOU_CANT_ADD_MORE_SUBCLASS 	 = "" ;
	public String	SUBCLASS_YOU_LEVEL_IS_LOW_TO_MAKE_ANOTHER_SUBCLASS 	 = "" ;
	public String	SUBCLASS_YOU_SUBCLASS_CANT_BE_ADDED 	 = "" ;
	public String	SUBCLASS_YOU_CANT_CHANGE_SUBCLASS_WHILE_CASTING 	 = "" ;
	public String	SUBCLASS_YOU_CLASS_CANT_BE_CHANGED 	 = "" ;
	public String	SUBCLASS_YOU_WANT_TO_ADD_THIS_SUBCLASS_$name	=	"";
	public String	SUBCLASS_YOU_WANT_TO_REMOVE_THIS_SUBCLASS_$name	=	"";
	
	//MISCELANIUS
	
	public String	MIS_YOU_WANT_TO_INSCREISE_YOU_LVL_TO_85 	 = "" ;
	public String	MIS_YOU_WANT_TO_GET_NOBLE_STATUS 	 = "" ;
	public String	MIS_YOU_WANT_TO_ACQUIRE_FAME 	 = "" ;
	public String	MIS_YOU_WANT_CLAN_REWARD 	 = "" ;
	public String	MIS_YOU_WANT_TO_REDUCE_PK_KILL 	 = "" ;
	public String	MIS_YOU_WANT_TO_CHANGE_YOUR_SEX 	 = "" ;
	public String	MIS_YOU_WANT_TO_BE_A_AIOCHAR_TYPE 	 = "" ;
	public String	MIS_YOU_WANT_TO_CHANGE_YOUR_NAME 	 = "" ;
	public String	MIS_YOU_DONT_HAVE_PK_KILL 	 = "" ;
	public String	MIS_YOU_ACQUIRED_FAME 	 = "" ;
	public String	MIS_YOU_AIOCHAR_CANT_DO_IT_AGAIN 	 = "" ;

	//CLAN REPUTATION EVENT
	public String	CR_YOU_NEED_AT_LEAT_COUNT_CLAN_MEMBERS_ON 	 = "" ;
	public String	CR_YOU_NEED_ALL_MEMBERS_ONLINE 	 = "" ;
	public String	CR_YOU_CLAN_IS_INVALID 	 = "" ;
	public String	CR_CONGRATULATION_YOU_CLAN_HAS_BEEN_REWARDED 	 = "" ;
	
	//ACCOUNT RECOVERY, EMAIL REGISTRATION
	public String	RACC_ERROR_IN_CODE 	 = "" ;
	public String	RAAC_CHECK_EMAIL 	 = "" ;
	public String	RAAC_ENTER_WROG_CODE 	 = "" ;
	public String	RAAC_YOU_DONT_HAVE_ANY_MORE_ACCOUNT_TO_THIS_EMAIL 	 = "" ;
	public String	REGI_EMAIL_NOT_MATCH 	 = "" ;

	//CHANGE CHAR ACCOUNT, CHANGE PASSWORD
	public String	CHANGE_ACC_MUST_BE_A_DIFFERENT_ACCOUNT 	 = "" ;
	public String	CHANGE_ACC_FULL 	 = "" ;
	public String	CHANGE_ACC_NO_EXIST_NO_LINKED 	 = "" ;
	public String	CHANGE_PASS_LENGHT_SHOULD_BE_HIGHER_THANT_4 	 = "" ;
	public String	CHANGE_PASS_NO_MATCH 	 = "" ;
	public String	CHANGE_PASS_NEW_PASSWORD 	 = "" ;
	public String	CHANGE_PASS_ERROR 	 = "" ;
	public String	CHANGE_PASS_YOU_CANT_USE_OLD_PASS_AS_NEW	= "";

	//SELL ACCOUNT
	public String	SELLACCOUNT_TIME_IS_OVER 	 = "" ;
	public String	SELLACCOUNT_RECEIVER_PLAYER_IS_NOT_ONLINE 	 = "" ;
	public String	SELLACCOUNT_YOU_NEED_TO_WAIT_TO_RESEND 	 = "" ;
	public String	SELLACCOUNT_ENTER_REQUESTED_ITEM_TO_SELL 	 = "" ;
	public String	SELLACCOUNT_ENTER_PLAYER_TO_RECEIVED_PAYMENT 	 = "" ;
	public String	SELLACCOUNT_YOUR_RECEIVER_CHAR_IS_NOT_ONLINE 	 = "" ;
	public String	SELLACCOUNT_TARGET_NOT_READY 	 = "" ;
	public String	SELLACCOUNT_TARGET_PLAYER 	 = "" ;
	public String	SELLACCOUNT_CANT_ADD_MORE_ITEM 	 = "" ;
	public String	SELLACCOUNT_NAME_NO_EXISTS 	 = "" ;

	//SELL CLAN
	public String	SELLCLAN_CLAN_LEADER_ERROR 	 = "" ;
	public String	SELLCLAN_LADER_NOT_ONLINE 	 = "" ;
	public String	SELLCLAN_SELLER_NO_HAVE_CLAN_TO_SELL 	 = "" ;
	public String	SELLCLAN_SELLER_NO_CLAN_LEADER 	 = "" ;
	public String	SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN 	 = "" ;
	public String	SELLCLAN_YOU_NEED_TO_HAVE_A_CLAN_TO_SELL 	 = "" ;
	public String	SELLCLAN_ENTER_REQUESTED_ITEM_TO_SELL 	 = "" ;

	
	//SECONDAY PASSWORD
	public String	SP_MOST_HAVE_LENGHT 	 = "" ;
	public String	SP_CHAR_TEMPLATE_WROG 	 = "" ;
	public String	SP_WAS_CORRECT 	 = "" ;
	public String	SP_WAS_INCORRECT 	 = "" ;
	public String	SP_WAS_INCORRECT_DISCONNECT 	 = "" ;
	public String	SP_WAS_CORRECT_AND_REMOVE_FROM_ACCOUNT 	 = "" ;
	public String	SP_WAS_INCORRECT_AND_REMOVE_PROCESS_WAS_CANCEL 	 = "" ;
	public String	SP_WAS_BEEN_SET 	 = "" ;
	public String	SP_PASSWORD_NEED_TO_BE_EQUALS 	 = "" ;
	
	
	//EMAIL
	public String	EMAIL_SOMEONE_WROTE_WRONG_YOUR_SECONDARY_PASSWORD 	 = "" ;
	public String	EMAIL_SELL_ACCOUNT_INFO 	 = "" ;
	
	//*AFK*//
	public String	AFK_ALREADY_AFK	= "";
	public String	AFK_YOU_CAN_AFK_WHILE_DEATH	= "";
	public String	AFK_YOU_CAN_AFK_WHILE_COMBAT_MODE	= "";
	public String	AFK_YOU_CAN_AFK_WHILE_FLAG	= "";
	public String	AFK_YOU_CAN_AFK_WHILE_PK	= ""; 
	public String	AFK_YOU_CAN_AFK_WHILE_SIEGE_CASTLE	= ""; 
	public String	AFK_YOU_CAN_AFK_WHILE_EVENT_PVP_ZONE	= "";
	
	public String	CHARPANEL_PANEL_TO_SET_VARIOUS_SETTING_FOR_YOUR_CHARACTER 	 = "";
	public String	FIXME_SELECT_THE_CHAR_HOW_NEED_TO_BE_FIX_IT 	 = "";
	public String	FIXME_EXPLAIN 	 = "";
	public String	CB_BTN_ANNOUCEMENT 	 = "";
	public String	CB_BTN_CHANGE_LOG 	 = "";
	public String	CB_BTN_RULES 	 = "";
	public String	CB_BTN_STAFF 	 = "";
	public String	CB_BTN_TOP_PLAYER 	 = "";
	public String	CB_BTN_HEROES 	 = "";
	public String	CB_BTN_CLAN 	 = "";
	public String	CB_BTN_CASTLE 	 = "";
	public String	CB_BTN_PRIVATE_STORE 	 = "";
	public String	CB_BTN_SV_CONFIG 	 = "";
	public String	CB_BTN_FEATURES 	 = "";
	public String	CB_BTN_EVENTS 	 = "";
	public String	CB_BTN_PLAYGAME 	 = "";
	public String	BUTTON_EXPLAINS_VOTE_CB 	 = "";
	public String	BUTTON_EXPLAINS_BUFFER_CB 	 = "";
	public String	BUTTON_EXPLAINS_TELEPORT_CB 	 = "";
	public String	BUTTON_EXPLAINS_SHOP_CB 	 = "";
	public String	BUTTON_EXPLAINS_WAREHOUSE_CB 	 = "";
	public String	BUTTON_EXPLAINS_AUGMENT_CB 	 = "";
	public String	BUTTON_EXPLAINS_SUBCLASES_CB 	 = "";
	public String	BUTTON_EXPLAINS_PROFESSION_CB 	 = "";
	public String	BUTTON_EXPLAINS_CONFIG_PANEL_CB 	 = "";
	public String	BUTTON_EXPLAINS_DROP_SEARCH_CB 	 = "";
	public String	BUTTON_EXPLAINS_PVPPK_LIST_CB 	 = "";
	public String	BUTTON_EXPLAINS_LOG_PELEAS_CB 	 = "";
	public String	BUTTON_EXPLAINS_CASTLE_MANAGER_CB 	 = "";
	public String	BUTTON_EXPLAINS_CHALLENGE_CB 	 = "";
	public String	BUTTON_EXPLAINS_SYMBOL_MARKET_CB 	 = "";
	public String	BUTTON_EXPLAINS_CLANALLY_CB 	 = "";
	public String	BUTTON_EXPLAINS_GO_PARTY_LEADER_CB 	 = "";
	public String	BUTTON_EXPLAINS_FLAGFINDER_CB 	 = "";
	public String	BUTTON_EXPLAINS_COLORNAME_CB 	 = "";
	public String	BUTTON_EXPLAINS_DELEVEL_CB 	 = "";
	public String	BUTTON_EXPLAINS_REMOVE_ATRIBUTE_CB 	 = "";
	public String	BUTTON_EXPLAINS_BUG_REPORT_CB 	 = "";
	public String	BUTTON_EXPLAINS_DONATION_CB 	 = "";
	public String	BUTTON_EXPLAINS_CAMBIO_NOMBRE_PJ_CB 	 = "";
	public String	BUTTON_EXPLAINS_CAMBIO_NOMBRE_CLAN_CB 	 = "";
	public String	BUTTON_EXPLAINS_MISCELLANEOUS_CB 	 = "";
	public String	BUTTON_EXPLAINS_ELEMENT_ENHANCED_CB 	 = "";
	public String	BUTTON_EXPLAINS_ENCHANT_ITEM_CB 	 = "";
	public String	BUTTON_EXPLAINS_AUGMENT_SPECIAL_CB 	 = "";
	public String	BUTTON_EXPLAINS_GRAND_BOSS_STATUS_CB 	 = "";
	public String	BUTTON_EXPLAINS_RAIDBOSS_INFO_CB 	 = "";
	public String	BUTTON_EXPLAINS_TRANSFORMATION_CB 	 = "";
	public String	BUTTON_EXPLAINS_AUCTIONSHOUSE_CB 	 = "";
	public String	BUTTON_EXPLAINS_DRESSME_CB = "";
	public String	BUTTON_EXPLAINS_BLACKSMITH_CB = "";
	public String	BUTTON_EXPLAINS_PARTY_MATCHING_CB = "";
	public String	BUTTON_EXPLAINS_BIDHOUSE_CB = "";
	
	public String	ADMIN_SEND_YOU_THE_CODE_TO_YOUR_EMAIL = "";
	public String	ADMIN_SECONDARY_PASSWORD_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = "";
	public String	ADMIN_ACCOUNT_EMAIL_RESET = "";
	public String	ADMIN_OLY_SCHEME_REMOVED = "";
	public String	ADMIN_BUFF_SCHEME_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = "";
	public String	ADMIN_PERSONAL_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = "";
	public String	ADMIN_DRESSME_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = "";
	public String	ADMIN_SURVEY_ENDED_MESSAGE = "";
	public String	ADMIN_YOU_CAN_ANSWER_AGAIN = "";
	public String	ADMIN_GMPANEL_GATHERING_RECALL_YOU = "";
	
	public String	VOTE_INSTANCE_THIS_ZONE_IS_CLOSE_FOR_NOW = "";
	public String	VOTE_INSTANCE_CANT_USE_THIS_IN_DUALBOX = "";
	public String	VOTE_INSTANCE_YOUR_CLASS_IS_NOT_ALLOWED_TO_ENTER = "";
	public String	VOTE_INSTANCE_REWARD_BY_EMAIL_MESSAGE	= "";
	public String	PVP_INSTANCE_YOU_NEED_TO_EXIT_TO_ENTER_TO_ANOTHER = "";
	public String	PVP_INSTANCE_YOU_CANT_ENTER_NOW_FOR_EVENT = "";
	public String	PVP_INSTANCE_YOU_NEED_TO_BE_ON_PEACE_ZONE = "";
	public String	PVP_INSTANCE_ZONE_IS_NOT_READY = "";
	public String	PVP_INSTANCE_ZONE_JUST_WITHOUT_PARTY = "";
	public String	PVP_INSTANCE_ZONE_JUST_PARTY = "";
	public String	PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER = "";
	public String	PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER = "";
	public String	PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM = "";
	public String	PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER_CLASS_ALLOW = "";
	public String	PVP_INSTANCE_ZONE_YOUR_CANT_ENTER_WITH_PARTY = "";
	public String	PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER = "";
	public String	PVP_INSTANCE_ZONE_PLAYER_NO_HAVE_LEVEL = "";
	public String	PVP_INSTANCE_ZONE_PLAYER_NO_HAD_A_CLASS = "";
	public String	PVP_INSTANCE_ZONE_PLAYER_HAVE_PROHIBITED_ITEM = "";
	public String	PVP_INSTANCE_ZONE_JUST_CLAN_PARTY = "";
	public String	PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER = "";
	public String	PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NEED_CLAN = "";
	public String	PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NO_HAVE_ITEM_REQUESTED = "";
	
	public String	BORROW_SYSTEM_PASSWORD_NOT_EQUALS = "";
	public String	BORROW_SYSTEM_CONFIRM_PASSWORD_$password = "";
	public String	BORROW_SYSTEM_PASSWORD_LENGHT_WROG = "";
	public String	BORROW_SYSTEM_BLOCKED = "";
	public String	BORROW_SYSTEM_THE_TARGET_$name_CANT_MAKE_TRADE = "";
	public String	BORROW_SYSTEM_CAN_NOT_BE_USE_BY_GM_AND_ADMINS = "";
	public String	BORROW_SYSTEM_ADM_$name_HAS_SET_BORROWED_PASSWORD = "";
	public String	EXPERIENCE_ON = "";
	public String	EXPERIENCE_OFF = "";
	
	public String	PRE_DONATION_TEXT = "";
	
	public Lang(String Language, String Folder, String Abbreviation){
		this._LANGUAGE = Language;
		this._FOLDER = Folder;
		this._ABBREVIATION = Abbreviation;
		loadMsg();
	}
	public String getFolder(){
		return this._FOLDER;
	}
	public String getLanguage(){
		return this._LANGUAGE;
	}
	public String getAbbreviation(){
		return this._ABBREVIATION;
	}
	private void loadMsg(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File("./config/zeus/htm/"+ this._FOLDER +"/Language.ini")));
			
			//COMMANDS
			ZEUS_CONFIG = String.valueOf(propZ.getProperty("ZEUS_CONFIG"));
			ZEUS_TELE = String.valueOf(propZ.getProperty("ZEUS_TELE"));
			ZEUS_SHOP = String.valueOf(propZ.getProperty("ZEUS_SHOP"));
			OLY_BAN = String.valueOf(propZ.getProperty("OLY_BAN"));
			OLY_UNBAN = String.valueOf(propZ.getProperty("OLY_UNBAN"));
			OLY_RESET_POINT = String.valueOf(propZ.getProperty("OLY_RESET_POINT"));
			OLY_POINT = String.valueOf(propZ.getProperty("OLY_POINT"));
			ZEUS_BANIP = String.valueOf(propZ.getProperty("ZEUS_BANIP"));
			ZEUS_IPBLOCK = String.valueOf(propZ.getProperty("ZEUS_IPBLOCK"));
			ZEUS_BOTZONE = String.valueOf(propZ.getProperty("ZEUS_BOTZONE"));
			ZEUS_RECALLALL = String.valueOf(propZ.getProperty("ZEUS_RECALLALL"));
			ZEUS_BOT_CANCEL = String.valueOf(propZ.getProperty("ZEUS_BOT_CANCEL"));
			ZEUS_GMPANEL = String.valueOf(propZ.getProperty("ZEUS_GMPANEL"));
			ZEUS_FAKE = String.valueOf(propZ.getProperty("ZEUS_FAKE"));
			ZEUS_FAKE_CLONE = String.valueOf(propZ.getProperty("ZEUS_FAKE_CLONE"));
			ZEUS_FAKE_REMOVE = String.valueOf(propZ.getProperty("ZEUS_FAKE_REMOVE"));
			ZEUS = String.valueOf(propZ.getProperty("ZEUS"));
			OLY_BUFF = String.valueOf(propZ.getProperty("OLY_BUFF"));
			CHANGELANG = String.valueOf(propZ.getProperty("CHANGELANG"));
			CHARPANEL = String.valueOf(propZ.getProperty("CHARPANEL"));
			VOTE = String.valueOf(propZ.getProperty("VOTE"));
			ACC_REGISTER = String.valueOf(propZ.getProperty("ACC_REGISTER"));
			CHANGEEMAIL = String.valueOf(propZ.getProperty("CHANGEEMAIL"));
			ACCRECOVERY = String.valueOf(propZ.getProperty("ACCRECOVERY"));
			CHANGEPASSWORD = String.valueOf(propZ.getProperty("CHANGEPASSWORD"));
			REMOVESECONDARYPASSWORD = String.valueOf(propZ.getProperty("REMOVESECONDARYPASSWORD"));
			MOVECHAR = String.valueOf(propZ.getProperty("MOVECHAR"));
			COMBINETALISMAN = String.valueOf(propZ.getProperty("COMBINETALISMAN"));
			EXP_STATUS = String.valueOf(propZ.getProperty("EXP_STATUS"));
			DRESSME = String.valueOf(propZ.getProperty("DRESSME"));
			CHECKBOT = String.valueOf(propZ.getProperty("CHECKBOT"));
			STAT = String.valueOf(propZ.getProperty("STAT"));
			FIXME = String.valueOf(propZ.getProperty("FIXME"));
			MYINFO = String.valueOf(propZ.getProperty("MYINFO"));
			MAKEANCIENTADENA = String.valueOf(propZ.getProperty("MAKEANCIENTADENA"));
			PARTY = String.valueOf(propZ.getProperty("PARTY"));
			BUFFSTORE = String.valueOf(propZ.getProperty("BUFFSTORE"));
			SELLACCOUNT = String.valueOf(propZ.getProperty("SELLACCOUNT"));
			SELLCLAN = String.valueOf(propZ.getProperty("SELLCLAN"));
			JOINRAID = String.valueOf(propZ.getProperty("JOINRAID"));
			LEAVERAID = String.valueOf(propZ.getProperty("LEAVERAID"));
			ENGANGE = String.valueOf(propZ.getProperty("ENGANGE"));
			GOTOLOVE = String.valueOf(propZ.getProperty("GOTOLOVE"));
			DIVORCE = String.valueOf(propZ.getProperty("DIVORCE"));
			DEPOSIT = String.valueOf(propZ.getProperty("DEPOSIT"));
			WITHDRAW = String.valueOf(propZ.getProperty("WITHDRAW"));
			DUEL = String.valueOf(propZ.getProperty("DUEL"));
			AUTO_CP = String.valueOf(propZ.getProperty("AUTO_CP"));
			AUTO_MP = String.valueOf(propZ.getProperty("AUTO_MP"));
			AUTO_HP = String.valueOf(propZ.getProperty("AUTO_HP"));
			WISHLIST = String.valueOf(propZ.getProperty("WISHLIST"));
			DEALY_LOGIN = String.valueOf(propZ.getProperty("DEALY_LOGIN"));
			CLANPENALTY = String.valueOf(propZ.getProperty("CLANPENALTY"));
			ATTACKLIST = String.valueOf(propZ.getProperty("ATTACKLIST"));
			UNDERATTACKLIST = String.valueOf(propZ.getProperty("UNDERATTACKLIST"));
			WARLIST = String.valueOf(propZ.getProperty("WARLIST"));
			INSTANCEZONE = String.valueOf(propZ.getProperty("INSTANCEZONE"));
			OLYMPIADSTAT = String.valueOf(propZ.getProperty("OLYMPIADSTAT"));
			MYBIRTHDAY = String.valueOf(propZ.getProperty("MYBIRTHDAY"));
			UNSTUCK = String.valueOf(propZ.getProperty("UNSTUCK"));
			CHANNELCREATE = String.valueOf(propZ.getProperty("CHANNELCREATE"));
			CHANNELDELETE = String.valueOf(propZ.getProperty("CHANNELDELETE"));
			CHANNELINVITE = String.valueOf(propZ.getProperty("CHANNELINVITE"));
			CHANNELKICK = String.valueOf(propZ.getProperty("CHANNELKICK"));
			CHANNELLEAVE = String.valueOf(propZ.getProperty("CHANNELLEAVE"));
			CHANNELINFO = String.valueOf(propZ.getProperty("CHANNELINFO"));
			FRIENDINVITE = String.valueOf(propZ.getProperty("FRIENDINVITE"));
			FRIENDLIST = String.valueOf(propZ.getProperty("FRIENDLIST"));
			FRIENDDEL = String.valueOf(propZ.getProperty("FRIENDDEL"));
			BLOCK = String.valueOf(propZ.getProperty("BLOCK"));
			UNBLOCK = String.valueOf(propZ.getProperty("UNBLOCK"));
			BLOCKLIST = String.valueOf(propZ.getProperty("BLOCKLIST"));
			GMLIST = String.valueOf(propZ.getProperty("GMLIST"));
			GM = String.valueOf(propZ.getProperty("GM"));
			GMCANCEL = String.valueOf(propZ.getProperty("GMCANCEL"));
			
			//GENERAL
			YOU_CANT_ACCESS_TO_THIS_SECTION = String.valueOf(propZ.getProperty("YOU_CANT_ACCESS_TO_THIS_SECTION"));
			ENTER_NUMBERS_ONLY = String.valueOf(propZ.getProperty("ENTER_NUMBERS_ONLY"));
			DO_NOT_LEAVE_EMPTY_FIELD = String.valueOf(propZ.getProperty("DO_NOT_LEAVE_EMPTY_FIELD"));
			THIS_PROCESS_MAY_TAKE_FEW_MINUTES = String.valueOf(propZ.getProperty("THIS_PROCESS_MAY_TAKE_FEW_MINUTES"));
			ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION = String.valueOf(propZ.getProperty("ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION"));
			CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_DEATH = String.valueOf(propZ.getProperty("CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_DEATH"));
			CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG = String.valueOf(propZ.getProperty("CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG"));
			CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK = String.valueOf(propZ.getProperty("CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK"));
			YOU_ARE_ALREADY_NOBLE = String.valueOf(propZ.getProperty("YOU_ARE_ALREADY_NOBLE"));
			YOU_ARE_ALREADY_LEVEL_85 = String.valueOf(propZ.getProperty("YOU_ARE_ALREADY_LEVEL_85"));
			THE_NAME_ENTERED_IS_NOT_VALID = String.valueOf(propZ.getProperty("THE_NAME_ENTERED_IS_NOT_VALID"));
			THE_NAME_CHANGE_WAS_SUCCESSFUL = String.valueOf(propZ.getProperty("THE_NAME_CHANGE_WAS_SUCCESSFUL"));
			ITEM_ID_NOT_FOUND = String.valueOf(propZ.getProperty("ITEM_ID_NOT_FOUND"));
			INVALID_TARGER_IS_NOT_A_PLAYER = String.valueOf(propZ.getProperty("INVALID_TARGER_IS_NOT_A_PLAYER"));
			THE_PLAYER_IS_NOBLE = String.valueOf(propZ.getProperty("THE_PLAYER_IS_NOBLE"));
			THE_PLAYER_IS_NOT_NOBLE_ANYMORE = String.valueOf(propZ.getProperty("THE_PLAYER_IS_NOT_NOBLE_ANYMORE"));
			THE_PLAYER_IS_NOT_NOBLE = String.valueOf(propZ.getProperty("THE_PLAYER_IS_NOT_NOBLE"));
			THE_PLAYER_IS_HERO = String.valueOf(propZ.getProperty("THE_PLAYER_IS_HERO"));
			YOU_ARE_NOBLE_NOW = String.valueOf(propZ.getProperty("YOU_ARE_NOBLE_NOW"));
			YOU_ARE_NOT_NOBLE = String.valueOf(propZ.getProperty("YOU_ARE_NOT_NOBLE"));
			AUGMENT_WEAPON_REMOVE_IT = String.valueOf(propZ.getProperty("AUGMENT_WEAPON_REMOVE_IT"));
			AUGMENT_SPECIAL_REMOVE_AUGMENT = String.valueOf(propZ.getProperty("AUGMENT_SPECIAL_REMOVE_AUGMENT"));
			YOU_CAN_CHANGE_YOUR_TITLE = String.valueOf(propZ.getProperty("YOU_CAN_CHANGE_YOUR_TITLE"));
			YOU_CAN_NOT_TRADE_WHILE_FLAG = String.valueOf(propZ.getProperty("YOU_CAN_NOT_TRADE_WHILE_FLAG"));
			YOU_CAN_NOT_TRADE_WHILE_PK = String.valueOf(propZ.getProperty("YOU_CAN_NOT_TRADE_WHILE_PK"));
			DISABLE_BY_ADMIN = String.valueOf(propZ.getProperty("DISABLE_BY_ADMIN"));
			YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL = String.valueOf(propZ.getProperty("YOU_NEED_TO_LINK_YOUR_ACCOUNT_TO_EMAIL"));
			CANT_USE_IN_COMBAT_MODE = String.valueOf(propZ.getProperty("CANT_USE_IN_COMBAT_MODE"));
			CANT_USE_IN_SIEGE_OR_TOWN_WAR = String.valueOf(propZ.getProperty("CANT_USE_IN_SIEGE_OR_TOWN_WAR"));
			YOU_ARE_OLY_BANNED = String.valueOf(propZ.getProperty("YOU_ARE_OLY_BANNED"));
			WELCOME = String.valueOf(propZ.getProperty("WELCOME"));
			MAXIMUM_ALLOWED_IP_ARE_$countAllow_CHAR_$name_ARE_DISCONNECTED = String.valueOf(propZ.getProperty("MAXIMUM_ALLOWED_IP_ARE_$countAllow_CHAR_$name_ARE_DISCONNECTED"));
			DATA_SAVED = String.valueOf(propZ.getProperty("DATA_SAVED"));
			DATA_UPDATE = String.valueOf(propZ.getProperty("DATA_UPDATE"));
			PLAYER_IS_IN_JAIL = String.valueOf(propZ.getProperty("PLAYER_IS_IN_JAIL"));
			I_IN_JAIL = String.valueOf(propZ.getProperty("I_IN_JAIL"));
			PLAYER_IN_BLOCK_LIST = String.valueOf(propZ.getProperty("PLAYER_IN_BLOCK_LIST"));
			PLAYER_HAS_YOU_BLOCK_LIST = String.valueOf(propZ.getProperty("PLAYER_HAS_YOU_BLOCK_LIST"));
			PLAYER_IS_IN_COMBAT_DUEL = String.valueOf(propZ.getProperty("PLAYER_IS_IN_COMBAT_DUEL"));
			YOU_OR_TARGET_IS_IN_BOAT = String.valueOf(propZ.getProperty("YOU_OR_TARGET_IS_IN_BOAT"));
			YOU_OR_TARGET_IS_IN_OLY_OBSERVER = String.valueOf(propZ.getProperty("YOU_OR_TARGET_IS_IN_OLY_OBSERVER"));
			YOU_OR_TARGET_IS_IN_OLY = String.valueOf(propZ.getProperty("YOU_OR_TARGET_IS_IN_OLY"));
			YOU_OR_TARGET_IS_IN_EVENT = String.valueOf(propZ.getProperty("YOU_OR_TARGET_IS_IN_EVENT"));
			YOU_CANT_USE_MY_SERVICES_RIGHT_NOW = String.valueOf(propZ.getProperty("YOU_CANT_USE_MY_SERVICES_RIGHT_NOW"));
			ARE_INSIDE_A_NON_SUMMON_ARE = String.valueOf(propZ.getProperty("ARE_INSIDE_A_NON_SUMMON_ARE"));
			ARE_INSIDE_A_SIEGE_ZONE = String.valueOf(propZ.getProperty("ARE_INSIDE_A_SIEGE_ZONE"));
			ARE_INSIDE_A_CASTLE = String.valueOf(propZ.getProperty("ARE_INSIDE_A_CASTLE"));
			YOU_ARE_INSIDE_A_INSTANCE_CANT_ENTER_NOW = String.valueOf(propZ.getProperty("YOU_ARE_INSIDE_A_INSTANCE_CANT_ENTER_NOW"));
			ARE_INSIDE_A_CH = String.valueOf(propZ.getProperty("ARE_INSIDE_A_CH"));
			ARE_NOT_IN_PEACE_ZONE = String.valueOf(propZ.getProperty("ARE_NOT_IN_PEACE_ZONE"));
			PLAYER_IS_NOT_ONLINE = String.valueOf(propZ.getProperty("PLAYER_IS_NOT_ONLINE"));
			TARGET_IS_NOT_PARTY_LEADER = String.valueOf(propZ.getProperty("TARGET_IS_NOT_PARTY_LEADER"));
			PLAYER_IS_OFFLINE = String.valueOf(propZ.getProperty("PLAYER_IS_OFFLINE"));
			DISABLED_FOR_PLAYER_IN_JAIL = String.valueOf(propZ.getProperty("DISABLED_FOR_PLAYER_IN_JAIL"));
			ONLY_PARTY_LEADER_CAN_USE_THIS = String.valueOf(propZ.getProperty("ONLY_PARTY_LEADER_CAN_USE_THIS"));
			TRY_AGAIN_MAYBE_THE_ADMINGM_CHANGE_SOMETHING = String.valueOf(propZ.getProperty("TRY_AGAIN_MAYBE_THE_ADMINGM_CHANGE_SOMETHING"));
			YOU_NEED_TO_HAVE_A_CLAN = String.valueOf(propZ.getProperty("YOU_NEED_TO_HAVE_A_CLAN"));
			YOU_NEED_TO_BE_MORE_SPECIFIC = String.valueOf(propZ.getProperty("YOU_NEED_TO_BE_MORE_SPECIFIC"));
			YOU_WILL_BE_REVIVE_ON = String.valueOf(propZ.getProperty("YOU_WILL_BE_REVIVE_ON"));
			SENDING_EMAIL_TO = String.valueOf(propZ.getProperty("SENDING_EMAIL_TO"));
			YOU_CANT_USE_THIS_IN_PARTY = String.valueOf(propZ.getProperty("YOU_CANT_USE_THIS_IN_PARTY"));
			JAIL_BAIL_COMPLETE = String.valueOf(propZ.getProperty("JAIL_BAIL_COMPLETE"));
			SELECTED_PLAYER_REFUSE_PARTY_REQUEST = String.valueOf(propZ.getProperty("SELECTED_PLAYER_REFUSE_PARTY_REQUEST"));
			WAIT_TO_SEND_ANOTHER_POKE = String.valueOf(propZ.getProperty("WAIT_TO_SEND_ANOTHER_POKE"));
			RAIDBOSS_JOIN = String.valueOf(propZ.getProperty("RAIDBOSS_JOIN"));
			YOUR_ON_A_INSTANCE_CANT_PARTICIPATE = String.valueOf(propZ.getProperty("YOUR_ON_A_INSTANCE_CANT_PARTICIPATE"));
			YOUR_ARE_IN_OTHER_EVENT = String.valueOf(propZ.getProperty("YOUR_ARE_IN_OTHER_EVENT"));
			YOUR_ARE_IN_THIS_EVENT = String.valueOf(propZ.getProperty("YOUR_ARE_IN_THIS_EVENT"));
			YOUR_CANT_JOIN_THIS_EVENT_BECOUSE_OLYS = String.valueOf(propZ.getProperty("YOUR_CANT_JOIN_THIS_EVENT_BECOUSE_OLYS"));
			YOUR_ARE_ALREADY_PARTICIPATING_IN_THE_EVENT_WITH_OTHER_IP = String.valueOf(propZ.getProperty("YOUR_ARE_ALREADY_PARTICIPATING_IN_THE_EVENT_WITH_OTHER_IP"));
			YOU_CANT_USE_THE_SHOP_IN_THIS_ZONE = String.valueOf(propZ.getProperty("YOU_CANT_USE_THE_SHOP_IN_THIS_ZONE"));
			YOU_NAME_HAD_MORE_THAN_16_CHARACTERS = String.valueOf(propZ.getProperty("YOU_NAME_HAD_MORE_THAN_16_CHARACTERS"));
			THE_NEW_NAME_ALREADY_EXISTS = String.valueOf(propZ.getProperty("THE_NEW_NAME_ALREADY_EXISTS"));
			
			
			//OLYMPIAD SCHEME
			OLYMPIAD_SF_NO_MORE_BUFF_TO_USE = String.valueOf(propZ.getProperty("OLYMPIAD_SF_NO_MORE_BUFF_TO_USE"));
			OLYMPIAD_SF_CAN_USE_BUFFER_TOWN_EVENT = String.valueOf(propZ.getProperty("OLYMPIAD_SF_CAN_USE_BUFFER_TOWN_EVENT"));
			OLY_INSTANCE_ENTER = String.valueOf(propZ.getProperty("OLY_INSTANCE_ENTER"));
			//GENERAL NEED
			NEED_TO_BE_NOBLE = String.valueOf(propZ.getProperty("NEED_TO_BE_NOBLE"));
			NEED_TO_HAVE_A_LEVEL_GREATER_THAN = String.valueOf(propZ.getProperty("NEED_TO_HAVE_A_LEVEL_GREATER_THAN"));
			NEED_TO_BE_LEVEL_FOR_THIS_OPERATION = String.valueOf(propZ.getProperty("NEED_TO_BE_LEVEL_FOR_THIS_OPERATION"));
			NEED_TO_HAVE_A_CLAN_EQUAL_OR_GREATER_THAN = String.valueOf(propZ.getProperty("NEED_TO_HAVE_A_CLAN_EQUAL_OR_GREATER_THAN"));
			NEED_TO_BE_IN_PARTY = String.valueOf(propZ.getProperty("NEED_TO_BE_IN_PARTY"));
			NEED_ENTER_THE_REQUESTED_DATA = String.valueOf(propZ.getProperty("NEED_ENTER_THE_REQUESTED_DATA"));
			//GENERALITEM = String.valueOf(propZ.getProperty("//GENERALITEM"));
			YOU_DONT_HAVE_THE_REQUESTED_ITEM = String.valueOf(propZ.getProperty("YOU_DONT_HAVE_THE_REQUESTED_ITEM"));
			//VOTESYSTEM
			YOU_CANT_VOTE_RIGHT_NOW = String.valueOf(propZ.getProperty("YOU_CANT_VOTE_RIGHT_NOW"));
			YOU_VOTE_WAS_NOT_TAKEN = String.valueOf(propZ.getProperty("YOU_VOTE_WAS_NOT_TAKEN"));
			VOTEREWARD_REMEMBER_VOTE_12 = String.valueOf(propZ.getProperty("VOTEREWARD_REMEMBER_VOTE_12"));
			VOTEREWARD_WAIT_TIME_IS_OVER = String.valueOf(propZ.getProperty("VOTEREWARD_WAIT_TIME_IS_OVER"));
			VOTEREWARD_WAIT_MESSAGE = String.valueOf(propZ.getProperty("VOTEREWARD_WAIT_MESSAGE"));
			VOTEREWARD_ERROR_MESSAGE = String.valueOf(propZ.getProperty("VOTEREWARD_ERROR_MESSAGE"));
			VOTOREWARD_SCAM_MESSAGE = String.valueOf(propZ.getProperty("VOTOREWARD_SCAM_MESSAGE"));
			VOTOREWARD_GETTING_VOTES = String.valueOf(propZ.getProperty("VOTOREWARD_GETTING_VOTES"));
			VOTEREWARD_ASK = String.valueOf(propZ.getProperty("VOTEREWARD_ASK"));
			//AIOSYSTEM
			THE_NEW_AIO_NAME_EXIST = String.valueOf(propZ.getProperty("THE_NEW_AIO_NAME_EXIST"));
			AIO_CHAR_CREATION_STARTED = String.valueOf(propZ.getProperty("AIO_CHAR_CREATION_STARTED"));
			AIO_CHAR_ENDED_WITHOUT_PROBLEMS = String.valueOf(propZ.getProperty("AIO_CHAR_ENDED_WITHOUT_PROBLEMS"));
			AIO_CHAR_NAME_MUST_HAVE_CHARACTERS = String.valueOf(propZ.getProperty("AIO_CHAR_NAME_MUST_HAVE_CHARACTERS"));
			//STATUS
			BUTTON_ENABLE = String.valueOf(propZ.getProperty("BUTTON_ENABLE"));
			BUTTON_DISABLE = String.valueOf(propZ.getProperty("BUTTON_DISABLE"));
			BUTTON_BACK = String.valueOf(propZ.getProperty("BUTTON_BACK"));
			BUTTON_NEXT = String.valueOf(propZ.getProperty("BUTTON_NEXT"));
			OPTION_ALIVE = String.valueOf(propZ.getProperty("OPTION_ALIVE"));
			OPTION_WAITING = String.valueOf(propZ.getProperty("OPTION_WAITING"));
			OPTION_IN_FIGHT = String.valueOf(propZ.getProperty("OPTION_IN_FIGHT"));
			OPTION_DEAD = String.valueOf(propZ.getProperty("OPTION_DEAD"));
			//DROP SEARCH
			DS_YOU_NEED_TO_CLOSE_OBSERVER_MODE_TO_OBSERVER_ANOTHER = String.valueOf(propZ.getProperty("DS_YOU_NEED_TO_CLOSE_OBSERVER_MODE_TO_OBSERVER_ANOTHER"));
			DS_YOU_WANT_TO_OBSERVER_THIS = String.valueOf(propZ.getProperty("DS_YOU_WANT_TO_OBSERVER_THIS"));
			DS_MOB_OR_RB_BLOCKED_TELEPORT = String.valueOf(propZ.getProperty("DS_MOB_OR_RB_BLOCKED_TELEPORT"));
			DS_YOU_WANT_TO_TELEPORT_THIS = String.valueOf(propZ.getProperty("DS_YOU_WANT_TO_TELEPORT_THIS"));
			//OLYMPIAD
			OLY_BUFFER_MANAGER_TIME_OUT = String.valueOf(propZ.getProperty("OLY_BUFFER_MANAGER_TIME_OUT"));
			OLY_BUFFER_NEED_TO_ACTIVE_CREATE_SCHEME_FOR_USE = String.valueOf(propZ.getProperty("OLY_BUFFER_NEED_TO_ACTIVE_CREATE_SCHEME_FOR_USE"));
			OLY_BUFFER_YOU_CANT_ADD_MORE_BUFF_MAXIMUM = String.valueOf(propZ.getProperty("OLY_BUFFER_YOU_CANT_ADD_MORE_BUFF_MAXIMUM"));
			OLY_BUFFER_YOU_CANT_ADD_MORE_SCHEMES_MAXIMUM = String.valueOf(propZ.getProperty("OLY_BUFFER_YOU_CANT_ADD_MORE_SCHEMES_MAXIMUM"));
			OLY_BUFFER_THE_SCHEME_NAME_ALREADY_EXIST = String.valueOf(propZ.getProperty("OLY_BUFFER_THE_SCHEME_NAME_ALREADY_EXIST"));
			//ENGINE
			ERROR_IN_THIS_PROCESS = String.valueOf(propZ.getProperty("ERROR_IN_THIS_PROCESS"));
			TYPING_ERROR = String.valueOf(propZ.getProperty("TYPING_ERROR"));
			WELLCOME_TITLE = String.valueOf(propZ.getProperty("WELLCOME_TITLE"));
			BUG_REPORT_LIST_OF_TYPE = String.valueOf(propZ.getProperty("BUG_REPORT_LIST_OF_TYPE"));
			BUG_REPORT_SENT = String.valueOf(propZ.getProperty("BUG_REPORT_SENT"));
			OLY_YOU_HAVE_BEEN_UNBANNED_FROM_OLY_BY_$gmName = String.valueOf(propZ.getProperty("OLY_YOU_HAVE_BEEN_UNBANNED_FROM_OLY_BY_$gmName"));
			OLY_YOU_HAVE_BEEN_BANNED_FROM_OLY_BY_$gmName = String.valueOf(propZ.getProperty("OLY_YOU_HAVE_BEEN_BANNED_FROM_OLY_BY_$gmName"));
			//CLAN
			CLAN_REMOVAL_PROCESS_CANCELED = String.valueOf(propZ.getProperty("CLAN_REMOVAL_PROCESS_CANCELED"));
			ENTER_NAME_NEW_OWNER_CLAN = String.valueOf(propZ.getProperty("ENTER_NAME_NEW_OWNER_CLAN"));
			NO_MORE_CLAN_SKILL_TO_LEARN = String.valueOf(propZ.getProperty("NO_MORE_CLAN_SKILL_TO_LEARN"));
			ENTER_NAME_OF_THE_ALLIANCE = String.valueOf(propZ.getProperty("ENTER_NAME_OF_THE_ALLIANCE"));
			YOU_DONT_HAVE_CLAN = String.valueOf(propZ.getProperty("YOU_DONT_HAVE_CLAN"));
			CLAN_NAME_ALREADY_EXISTS = String.valueOf(propZ.getProperty("CLAN_NAME_ALREADY_EXISTS"));
			CLAN_NAME_CHANGE = String.valueOf(propZ.getProperty("CLAN_NAME_CHANGE"));
			YOU_CAN_NOT_REMOVE_THIS_POST = String.valueOf(propZ.getProperty("YOU_CAN_NOT_REMOVE_THIS_POST"));
			POST_REMOVED = String.valueOf(propZ.getProperty("POST_REMOVED"));
			CLAN_NAME_FAIL = String.valueOf(propZ.getProperty("CLAN_NAME_FAIL"));
			COMMUNITY_SYSTEM_DISABLE = String.valueOf(propZ.getProperty("COMMUNITY_SYSTEM_DISABLE"));
			
			//FIND_PARTY_REQUEST_SYSTEM
			FP_TIME_OUT = String.valueOf(propZ.getProperty("FP_TIME_OUT"));
			FP_REQUEST_SENDING = String.valueOf(propZ.getProperty("FP_REQUEST_SENDING"));
			FP_WAITING_TO_SEND_AGAIN = String.valueOf(propZ.getProperty("FP_WAITING_TO_SEND_AGAIN"));
			FP_NO_MORE_SLOT = String.valueOf(propZ.getProperty("FP_NO_MORE_SLOT"));
			FP_END_REQUEST = String.valueOf(propZ.getProperty("FP_END_REQUEST"));
			FP_PLAYER_REFUSED_PARTY_REQUEST = String.valueOf(propZ.getProperty("FP_PLAYER_REFUSED_PARTY_REQUEST"));
			FP_REQUEST_SENDING_TO_PLAYER_YOU_NEED_TO_WAIT = String.valueOf(propZ.getProperty("FP_REQUEST_SENDING_TO_PLAYER_YOU_NEED_TO_WAIT")); 
			FP_YOU_CANT_SEND_PARTY_REQUEST_NOW = String.valueOf(propZ.getProperty("FP_YOU_CANT_SEND_PARTY_REQUEST_NOW"));
			//DONATION
			SENDING_DONATION_VOUCHER_TO_ADMINISTRATOR = String.valueOf(propZ.getProperty("SENDING_DONATION_VOUCHER_TO_ADMINISTRATOR"));
			VOUCHER_SENT_SUCCESSFULLY = String.valueOf(propZ.getProperty("VOUCHER_SENT_SUCCESSFULLY"));
			VOUCHER_SENT_ERROR = String.valueOf(propZ.getProperty("VOUCHER_SENT_ERROR"));
			DONATION_YOU_HAVE_$donationCount_ON_YOU_ACCOUNT = String.valueOf(propZ.getProperty("DONATION_YOU_HAVE_$donationCount_ON_YOU_ACCOUNT"));
			DONATION_GIVE_DC_BUTTON = String.valueOf(propZ.getProperty("DONATION_GIVE_DC_BUTTON"));
			DONATION_TYPE = String.valueOf(propZ.getProperty("DONATION_TYPE"));
			DONATION_NO_PREMIUM_DATA_ADDED = String.valueOf(propZ.getProperty("DONATION_NO_PREMIUM_DATA_ADDED"));
			DONATION_NO_HAVE_CLAN_FOR_PREMIUM_DATA = String.valueOf(propZ.getProperty("DONATION_NO_HAVE_CLAN_FOR_PREMIUM_DATA"));
			DONATION_YOU_NEED_TO_BE_CLAN_LEADER_TO_GET_PREMIUM_DATA = String.valueOf(propZ.getProperty("DONATION_YOU_NEED_TO_BE_CLAN_LEADER_TO_GET_PREMIUM_DATA"));
			DONATION_YOU_GONNA_BUY_255_RECOMMENDS = String.valueOf(propZ.getProperty("DONATION_YOU_GONNA_BUY_255_RECOMMENDS"));
			DONATION_YOU_WANT_TO_BE_NOBLE = String.valueOf(propZ.getProperty("DONATION_YOU_WANT_TO_BE_NOBLE"));
			DONATION_YOU_wANT_TO_CHANGE_YOUR_SEX = String.valueOf(propZ.getProperty("DONATION_YOU_wANT_TO_CHANGE_YOUR_SEX"));
			DONATION_YOU_GONNA_TRANSFORM_THIS_CHAR_INTO_A_AIO_BUFFER = String.valueOf(propZ.getProperty("DONATION_YOU_GONNA_TRANSFORM_THIS_CHAR_INTO_A_AIO_BUFFER"));
			DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT = String.valueOf(propZ.getProperty("DONATION_YOU_CANT_BUY_ANOTHER_PREMIUM_ACCOUNT"));
			DONATION_PREMIUM_DATA_IS_NOT_AVAIBLE_NOW = String.valueOf(propZ.getProperty("DONATION_PREMIUM_DATA_IS_NOT_AVAIBLE_NOW"));
			DONATION_CONGRATULATION_YOU_GET_DONATION_ITEMS = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOU_GET_DONATION_ITEMS"));
			DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_HAS_MORE_DAYS = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_HAS_MORE_DAYS"));
			DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_ITS_NOW_ON = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_PREMIUM_DATA_ITS_NOW_ON"));
			DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ELEMENTAL_POWER = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ELEMENTAL_POWER"));
			DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ENCHANT = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_ITEM_HAS_A_NEW_ENCHANT"));
			DONATION_CONGRATULATION_YOUR_NEW_LEVEL_IS = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_NEW_LEVEL_IS"));
			DONATION_CONGRATULATION_YOUR_ADQUIERE_FAME = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_ADQUIERE_FAME"));
			DONATION_CONGRATULATION_YOUR_PK_HAS_REDUCE_TO = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_PK_HAS_REDUCE_TO"));
			DONATION_CONGRATULATION_YOUR_CLAN_LVL_HAS_INCREASE = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_CLAN_LVL_HAS_INCREASE"));
			DONATION_CONGRATULATION_YOUR_CLAN_REPUTATION_HAS_INCREASE = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_CLAN_REPUTATION_HAS_INCREASE"));
			DONATION_CONGRATULATION_YOUR_RECEIVED_CLAN_SKILLS = String.valueOf(propZ.getProperty("DONATION_CONGRATULATION_YOUR_RECEIVED_CLAN_SKILLS"));
			DONATION_ONLY_CLAN_LEADER_CAN_LVL_UP = String.valueOf(propZ.getProperty("DONATION_ONLY_CLAN_LEADER_CAN_LVL_UP"));
			DONATION_YOU_HAVE_THE_SAME_ENCHANT_ON_YOUR_ITEM = String.valueOf(propZ.getProperty("DONATION_YOU_HAVE_THE_SAME_ENCHANT_ON_YOUR_ITEM"));
			DONATION_PREMIUM_ACCOUNT_IS_OVER = String.valueOf(propZ.getProperty("DONATION_PREMIUM_ACCOUNT_IS_OVER"));
			DONATION_PREMIUM_CLAN_IS_OVER = String.valueOf(propZ.getProperty("DONATION_PREMIUM_CLAN_IS_OVER"));
			DONATION_YOU_WANT_CHANGE_YOUR_NAME = String.valueOf(propZ.getProperty("DONATION_YOU_WANT_CHANGE_YOUR_NAME"));
			DONATION_YOU_WANT_CHANGE_YOUR_CLAN_NAME = String.valueOf(propZ.getProperty("DONATION_YOU_WANT_CHANGE_YOUR_CLAN_NAME"));
			DONATION_ERROR_CREATING_PREMIUM = String.valueOf(propZ.getProperty("DONATION_ERROR_CREATING_PREMIUM"));
			DONATION_ASK_CHAR_LEVEL = String.valueOf(propZ.getProperty("DONATION_ASK_CHAR_LEVEL"));
			DONATION_ASK_CHAR_FAME = String.valueOf(propZ.getProperty("DONATION_ASK_CHAR_FAME"));
			DONATION_ASK_CHAR_REDUCE_PK = String.valueOf(propZ.getProperty("DONATION_ASK_CHAR_REDUCE_PK"));
			DONATION_ASK_CLAN_LEVEL = String.valueOf(propZ.getProperty("DONATION_ASK_CLAN_LEVEL"));
			DONATION_ASK_CLAN_REPUTATION = String.valueOf(propZ.getProperty("DONATION_ASK_CLAN_REPUTATION"));
			DONATION_ASK_CLAN_SKILL = String.valueOf(propZ.getProperty("DONATION_ASK_CLAN_SKILL"));
			DONATION_ASK_ENCHANT_YOU_WANT_TO_ENCHANT_THE_ITEM_$item_TO_$enchant_$cost = String.valueOf(propZ.getProperty("DONATION_ASK_ENCHANT_YOU_WANT_TO_ENCHANT_THE_ITEM_$item_TO_$enchant_$cost"));
			DONATION_ASK_ENCHANT_YOU_WANT_TO_ELEMENTAL_THE_ITEM_$item_TO_$enchant_$elemental_$cost = String.valueOf(propZ.getProperty("DONATION_ASK_ENCHANT_YOU_WANT_TO_ELEMENTAL_THE_ITEM_$item_TO_$enchant_$elemental_$cost"));
			DONATION_PAYPAL_YOU_NEED_TO_WAIT_X_SECOND_FOR_OTHER_EMAIL_PETITION = String.valueOf(propZ.getProperty("DONATION_PAYPAL_YOU_NEED_TO_WAIT_X_SECOND_FOR_OTHER_EMAIL_PETITION"));
			DONATION_PAYPAL_SENDING_LINK_TO_YOUR_EMAIL = String.valueOf(propZ.getProperty("DONATION_PAYPAL_SENDING_LINK_TO_YOUR_EMAIL"));
			//CHALLENGE
			CHALLENGE_WIN = String.valueOf(propZ.getProperty("CHALLENGE_WIN"));
			CHALLENGE_LOOSE = String.valueOf(propZ.getProperty("CHALLENGE"));
			CHALLENGE_THE_PLAYER_GET_LEVEL_85 = String.valueOf(propZ.getProperty("CHALLENGE_THE_PLAYER_GET_LEVEL_85"));
			CHALLENGE_THE_PLAYER_GET_NOBLE = String.valueOf(propZ.getProperty("CHALLENGE_THE_PLAYER_GET_NOBLE"));
			CHALLENGE_WON_MESSAGE = String.valueOf(propZ.getProperty("CHALLENGE_WON_MESSAGE"));
			CHALLENGE_ANNOUCEMENT_PLAYER_FOUND_NPC = String.valueOf(propZ.getProperty("CHALLENGE_ANNOUCEMENT_PLAYER_FOUND_NPC"));
			CHALLENGE_NOBLE_WIN = String.valueOf(propZ.getProperty("CHALLENGE_NOBLE_WIN"));
			CHALLENGE_LV85_WIN = String.valueOf(propZ.getProperty("CHALLENGE_LV85_WIN"));
			//FLAGFINDER
			FLAG_FINDER_NO_PVP = String.valueOf(propZ.getProperty("FLAG_FINDER_NO_PVP"));
			FLAG_FINDER_PLAYER_FOUND = String.valueOf(propZ.getProperty("FLAG_FINDER_PLAYER_FOUND"));
			FLAG_FINDER_COMMING_FOR_YOU = String.valueOf(propZ.getProperty("FLAG_FINDER_COMMING_FOR_YOU"));
			//CHAT
			CHAT_LEVEL = String.valueOf(propZ.getProperty("CHAT_LEVEL"));
			CHAT_PVP = String.valueOf(propZ.getProperty("CHAT_PVP"));
			CHAT_LIFETIME = String.valueOf(propZ.getProperty("CHAT_LIFETIME"));
			
			//PARTYFINDER
			PARTY_FINDER_NO_PARTY_LEADER_DEATH = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_DEATH"));
			PARTY_FINDER_NO_PARTY_LEADER_FLAG = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_FLAG"));
			PARTY_FINDER_NO_PARTY_LEADER_NOBLE = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_NOBLE"));
			PARTY_FINDER_NO_PARTY_LEADER_INSTANCE_ZONE = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_LEADER_INSTANCE_ZONE"));
			PARTY_FINDER_NO_PARTY_NO_SUMMON_ZONE = String.valueOf(propZ.getProperty("PARTY_FINDER_NO_PARTY_NO_SUMMON_ZONE"));
			PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER = String.valueOf(propZ.getProperty("PARTY_FINDER_YOU_HAVE_BEEN_SENT_TO_YOUR_PARTY_LEADER"));
			PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION = String.valueOf(propZ.getProperty("PARTY_FINDER_THE_PLAYER_$name_HAS_MOVE_TO_YOU_POSITION"));
			//BOTSYSTEM
			BOT_VERIFICATION_SEND_TO_$player = String.valueOf(propZ.getProperty("BOT_VERIFICATION_SEND_TO_$player"));
			BOT_CAN_NOT_SEND_IN_OLY = String.valueOf(propZ.getProperty("BOT_CAN_NOT_SEND_IN_OLY"));
			BOT_CAN_NOT_SEND_IN_YOUR_SELF = String.valueOf(propZ.getProperty("BOT_CAN_NOT_SEND_IN_YOUR_SELF"));
			BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$timeEvery_NEXT_CHECK_IN_$timeNextCheck = String.valueOf(propZ.getProperty("BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$timeEvery_NEXT_CHECK_IN_$timeNextCheck"));
			BOT_THIS_COMMAND_ONLY_NOBLE = String.valueOf(propZ.getProperty("BOT_THIS_COMMAND_ONLY_NOBLE"));
			BOT_THIS_COMMAND_ONLY_HERO = String.valueOf(propZ.getProperty("BOT_THIS_COMMAND_ONLY_HERO"));
			BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level = String.valueOf(propZ.getProperty("BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level"));
			BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime = String.valueOf(propZ.getProperty("BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime"));
			BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS = String.valueOf(propZ.getProperty("BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS"));
			BOT_THIS_PLAYER_HAVE_KARMA = String.valueOf(propZ.getProperty("BOT_THIS_PLAYER_HAVE_KARMA"));
			BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT = String.valueOf(propZ.getProperty("BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT"));
			BOT_THE_PLAYER_IS_NOT_A_BOT = String.valueOf(propZ.getProperty("BOT_THE_PLAYER_IS_NOT_A_BOT"));
			BOT_$player_HAVE_SENT_YOU_A_ANTIBOT_VERIFICATION = String.valueOf(propZ.getProperty("BOT_$player_HAVE_SENT_YOU_A_ANTIBOT_VERIFICATION"));
			BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time = String.valueOf(propZ.getProperty("BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time"));
			BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER = String.valueOf(propZ.getProperty("BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER"));
			BOT_ANNOUCEMENT_REMOVE_ITEM = String.valueOf(propZ.getProperty("BOT_ANNOUCEMENT_REMOVE_ITEM"));
			BOT_INACTIVE_X_THAN_$minutes = String.valueOf(propZ.getProperty("BOT_INACTIVE_X_THAN_$minutes"));
			BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK = String.valueOf(propZ.getProperty("BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK"));
			BOT_MSGBOX_ARE_YOU_SURE = String.valueOf(propZ.getProperty("BOT_MSGBOX_ARE_YOU_SURE"));
			BOT_SECONDS_LEFT_TO_ANSWER = String.valueOf(propZ.getProperty("BOT_SECONDS_LEFT_TO_ANSWER"));
			//DRESSME
			DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME = String.valueOf(propZ.getProperty("DRESSME_ONLY_NUMERIC_TO_SHOW_DRESSME"));
			DRESSME_ONLY_HAVE_TO_CHOOSE = String.valueOf(propZ.getProperty("DRESSME_ONLY_HAVE_TO_CHOOSE"));
			DRESSME_CHOOSE_WRONG = String.valueOf(propZ.getProperty("DRESSME_CHOOSE_WRONG"));
			DRESSME_DISABLED = String.valueOf(propZ.getProperty("DRESSME_DISABLED"));
			DRESSME_YOU_DONT_HAVE_DRESSME_CONFIG = String.valueOf(propZ.getProperty("DRESSME_YOU_DONT_HAVE_DRESSME_CONFIG"));
			DRESSME_THE_COST_FOR_NEW_DRESSME = String.valueOf(propZ.getProperty("DRESSME_THE_COST_FOR_NEW_DRESSME"));
			DRESSME_YOU_NEED_TO_HAVE_ANY_DRESSME_ADDED = String.valueOf(propZ.getProperty("DRESSME_YOU_NEED_TO_HAVE_ANY_DRESSME_ADDED"));
			DRESSME_YOU_NEED_TO_PUT_ON_A_DRESSME = String.valueOf(propZ.getProperty("DRESSME_YOU_NEED_TO_PUT_ON_A_DRESSME"));
			DRESSME_SERVER_CONFIG_EMPTY = String.valueOf(propZ.getProperty("DRESSME_SERVER_CONFIG_EMPTY"));
			DRESSME_YOU_NEED_TO_WAIT_TO_TRY_ITEM = String.valueOf(propZ.getProperty("DRESSME_YOU_NEED_TO_WAIT_TO_TRY_ITEM"));
			DRESSME_YOU_NEED_TO_SET_A_DRESSME_BEFORE = String.valueOf(propZ.getProperty("DRESSME_YOU_NEED_TO_SET_A_DRESSME_BEFORE"));
			DRESSME_YOU_CANT_USE_THIS_DRESSME_IS_EMPTY = String.valueOf(propZ.getProperty("DRESSME_YOU_CANT_USE_THIS_DRESSME_IS_EMPTY"));
			DRESSME_JUST_FOR_HERO = String.valueOf(propZ.getProperty("DRESSME_JUST_FOR_HERO"));
			DRESSME_JUST_FOR_NOBLE = String.valueOf(propZ.getProperty("DRESSME_JUST_FOR_NOBLE"));
			DRESSME_JUST_FOR_TOP_PVP_PK = String.valueOf(propZ.getProperty("DRESSME_JUST_FOR_TOP_PVP_PK"));
			DRESSME_WANT_TO_BUY_THIS_DRESSME = String.valueOf(propZ.getProperty("DRESSME_WANT_TO_BUY_THIS_DRESSME"));
			DRESSME_WANT_TO_USE_THIS_DRESSME = String.valueOf(propZ.getProperty("DRESSME_WANT_TO_USE_THIS_DRESSME"));
			DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED = String.valueOf(propZ.getProperty("DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED"));
			DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CLAN_EXCLUSIVE = String.valueOf(propZ.getProperty("DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CLAN_EXCLUSIVE"));
			DRESSME_ARE_YOU_SURE_YOU_DELETE_ALL_DRESSME_ITEM_FROM_DRESSME_ID = String.valueOf(propZ.getProperty("DRESSME_ARE_YOU_SURE_YOU_DELETE_ALL_DRESSME_ITEM_FROM_DRESSME_ID"));
			DRESSME_ARE_YOU_SURE_YOU_DELETE_THIS_ITEM_FROM_DRESSME_ID = propZ.getProperty("DRESSME_ARE_YOU_SURE_YOU_DELETE_THIS_ITEM_FROM_DRESSME_ID", "");
			DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CHAR_EXCLUSIVE = propZ.getProperty("DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CHAR_EXCLUSIVE", "");
			DRESSME_CLAN_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL = propZ.getProperty("DRESSME_CLAN_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL", "");
			DRESSME_CHAR_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL = propZ.getProperty("DRESSME_CHAR_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL", "");
			//CANCELBUFFSYSTEM
			CANCEL_BUFF_RETURNED_IN_SECONDS = String.valueOf(propZ.getProperty("CANCEL_BUFF_RETURNED_IN_SECONDS"));
			CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED = String.valueOf(propZ.getProperty("CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED"));
			//BUFFERSYSTEM
			BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN"));
			BUFFERCHAR_LENGTH_OF_THE_NAME_MUST_BE_LESS_THAN_$size = String.valueOf(propZ.getProperty("BUFFERCHAR_LENGTH_OF_THE_NAME_MUST_BE_LESS_THAN_$size"));
			BUFFERCHAR_NAME_ALREADY_EXISTS_IN_YOU_SCHEMES = String.valueOf(propZ.getProperty("BUFFERCHAR_NAME_ALREADY_EXISTS_IN_YOU_SCHEMES"));
			BUFFERCHAR_SCHEME_NAME_SAVED = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_NAME_SAVED"));
			BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CAN_NOT_BUFF_YOU_PET"));
			BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CAN_NOT_HEAL_YOU_PET"));
			BUFFERCHAR_YOU_CAN_NOT_CANCEL_YOU_PET_BUFF = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CAN_NOT_CANCEL_YOU_PET_BUFF"));
			BUFFERCHAR_WROG_SCHEME_NAME = String.valueOf(propZ.getProperty("BUFFERCHAR_WROG_SCHEME_NAME"));
			BUFFERCHAR_YOU_NOT_HAVE_PET = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_NOT_HAVE_PET"));
			BUFFERCHAR_JUST_IN_PEACE_ZONE = String.valueOf(propZ.getProperty("BUFFERCHAR_JUST_IN_PEACE_ZONE"));
			BUFFERCHAR_YOU_DONT_HAVE_PREMIUM_SYSTEM = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_DONT_HAVE_PREMIUM_SYSTEM"));
			BUFFERCHAR_YOU_PREMIUM_SYSTEM_NO_HAVE_PREMIUM_BUFF = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_PREMIUM_SYSTEM_NO_HAVE_PREMIUM_BUFF"));
			BUFFERCHAR_YOU_PREMIUM_SYSTEM_IS_OVER = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_PREMIUM_SYSTEM_IS_OVER"));
			BUFFERCHAR_YOU_NEED_A_MINIMUN_LEVEL_TO_USE_THIS = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_NEED_A_MINIMUN_LEVEL_TO_USE_THIS"));
			BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE"));
			BUFFERCHAR_SCHEME_CREATED = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_CREATED"));
			BUFFERCHAR_SCHEME_NAME_CHANGE = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_NAME_CHANGE"));
			BUFFERCHAR_SCHEME_ICON_CHANGE = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_ICON_CHANGE"));
			BUFFERCHAR_SCHEME_REMOVE_BUFF = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_REMOVE_BUFF"));
			BUFFERCHAR_SCHEME_REMOVED = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_REMOVED"));
			BUFFERCHAR_SCHEME_CANT_ADD_MORE_BUFF = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_CANT_ADD_MORE_BUFF"));
			BUFFERCHAR_SCHEME_CANT_ADD_MORE_DANCE = String.valueOf(propZ.getProperty("BUFFERCHAR_SCHEME_CANT_ADD_MORE_DANCE"));
			BUFFERCHAR_YOU_NEED_TO_BE_ALIVE = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_NEED_TO_BE_ALIVE"));
			BUFFERCHAR_ARE_YOU_SURE_ABOUT_REMOVE_SCHEME = String.valueOf(propZ.getProperty("BUFFERCHAR_ARE_YOU_SURE_ABOUT_REMOVE_SCHEME"));
			BUFFERCHAR_YOU_CANT_ADD_MORE_SCHEME = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_CANT_ADD_MORE_SCHEME"));
			BUFFERCHAR_YOU_WANT_HEAL = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_WANT_HEAL"));
			BUFFERCHAR_YOU_WANT_CANCEL = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_WANT_CANCEL"));
			BUFFERCHAR_YOU_WANT_TO_USE_THIS_SCHEME = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_WANT_TO_USE_THIS_SCHEME"));
			BUFFERCHAR_YOU_WANT_TO_USE_YOU_OWN_SCHEME = String.valueOf(propZ.getProperty("BUFFERCHAR_YOU_WANT_TO_USE_YOU_OWN_SCHEME"));
			
			BUFFER_BUFFSTORE_WROG_ZONE_TO_CREATE_SPOT = String.valueOf(propZ.getProperty("BUFFER_BUFFSTORE_WROG_ZONE_TO_CREATE_SPOT"));
			BUFFER_BUFFSTORE_ENTER_PRICE_CORRECTLY = String.valueOf(propZ.getProperty("BUFFER_BUFFSTORE_ENTER_PRICE_CORRECTLY"));
			BUFFER_BUFFSTORE_MAX_SCHEME = String.valueOf(propZ.getProperty("BUFFER_BUFFSTORE_MAX_SCHEME"));
			BUFFER_BUFFSTORE_MAX_BUFF_POUR_SCHEME = String.valueOf(propZ.getProperty("BUFFER_BUFFSTORE_MAX_BUFF_POUR_SCHEME"));
			BUFFER_BUFFSTORE_YOU_HAVE_THIS_SCHEME_NAME_ON_THIS_BUFFSTORE_SELLER = String.valueOf(propZ.getProperty("BUFFER_BUFFSTORE_YOU_HAVE_THIS_SCHEME_NAME_ON_THIS_BUFFSTORE_SELLER"));	
			
			//TELEPORT
			TELEPORT_YOU_CAN_NOT_USE_THE_TELEPORT_IN_COMBAT_MODE = String.valueOf(propZ.getProperty("TELEPORT_YOU_CAN_NOT_USE_THE_TELEPORT_IN_COMBAT_MODE"));
			TELEPORT_THIS_AREA_DOES_NOT_ALLOW_DUAL_BOX = String.valueOf(propZ.getProperty("TELEPORT_THIS_AREA_DOES_NOT_ALLOW_DUAL_BOX"));
			TELEPORT_DO_YOU_WANT_TELEPORT_TO = String.valueOf(propZ.getProperty("TELEPORT_DO_YOU_WANT_TELEPORT_TO"));
			TELEPORT_YOU_CANT_TELEPORT_WHILE_CASTING_OR_IN_TELEPORTATION = String.valueOf(propZ.getProperty("TELEPORT_YOU_CANT_TELEPORT_WHILE_CASTING_OR_IN_TELEPORTATION"));
			//ACCOUNT
			ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED = String.valueOf(propZ.getProperty("ACCOUNT_THE_EMAIL_HAS_BEEN_SUCCESFULLY_UPDATED"));
			ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email = String.valueOf(propZ.getProperty("ACCOUNT_YOUR_ACCOUNT_IS_ALREADY_ASOCIATED_TO_AN_EMAIL_$email"));
			ACCOUNT_BUTTON_SEND_ME_A_VALIDATION_CODE = String.valueOf(propZ.getProperty("ACCOUNT_BUTTON_SEND_ME_A_VALIDATION_CODE"));
			ACCOUNT_BUTTON_CHECK_BUTTON = String.valueOf(propZ.getProperty("ACCOUNT_BUTTON_CHECK_BUTTON"));
			ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_AGAIN = String.valueOf(propZ.getProperty("ACCOUNT_REGISTER_EXPLAIN_INPUT_EMAIL_AGAIN"));
			//AUCTION HOUSE
			AH_ITEM_IS_NOT_FOR_SELL_NOW = String.valueOf(propZ.getProperty("AH_ITEM_IS_NOT_FOR_SELL_NOW"));
			AH_ITEM_IS_EQUIPPED = String.valueOf(propZ.getProperty("AH_ITEM_IS_EQUIPPED"));
			AH_TRY_TO_CHEAT = String.valueOf(propZ.getProperty("AH_TRY_TO_CHEAT"));
			AH_NO_HAVE_FEED = String.valueOf(propZ.getProperty("AH_NO_HAVE_FEED"));
			AH_NO_STACKABLE_ITEM_CHANGE_TO_ONE = String.valueOf(propZ.getProperty("AH_NO_STACKABLE_ITEM_CHANGE_TO_ONE"));
			AH_CANCEL_THE_ITEM_TO_SELL_AGAIN = String.valueOf(propZ.getProperty("AH_CANCEL_THE_ITEM_TO_SELL_AGAIN"));
			AH_CREATE_PROCESS_FAIL_CHECK_QUANTITY = String.valueOf(propZ.getProperty("AH_CREATE_PROCESS_FAIL_CHECK_QUANTITY"));
			AH_WROG_QUANTITY_TO_BUY = String.valueOf(propZ.getProperty("AH_WROG_QUANTITY_TO_BUY"));
			AH_EMAIL_TITLE_NEW_SELL = String.valueOf(propZ.getProperty("AH_EMAIL_TITLE_NEW_SELL"));
			AH_EMAIL_MESSAGE_NEW_SELL = String.valueOf(propZ.getProperty("AH_EMAIL_MESSAGE_NEW_SELL"));
			AH_QUESTION_YOU_WANT_CREATE_THIS_AUCTION = String.valueOf(propZ.getProperty("AH_QUESTION_YOU_WANT_CREATE_THIS_AUCTION"));
			AH_ONLY_IN_PEACE_ZONE = String.valueOf(propZ.getProperty("AH_ONLY_IN_PEACE_ZONE"));
			//BID HOUSE
			BH_ITEM_IS_NOT_FOR_SELL_NOW = String.valueOf(propZ.getProperty("BH_ITEM_IS_NOT_FOR_SELL_NOW"));
			BH_ITEM_IS_EQUIPPED = String.valueOf(propZ.getProperty("BH_ITEM_IS_EQUIPPED"));
			BH_TRY_TO_CHEAT = String.valueOf(propZ.getProperty("BH_TRY_TO_CHEAT"));
			BH_NO_HAVE_FEED = String.valueOf(propZ.getProperty("BH_NO_HAVE_FEED"));
			BH_NO_STACKABLE_ITEM_CHANGE_TO_ONE = String.valueOf(propZ.getProperty("BH_NO_STACKABLE_ITEM_CHANGE_TO_ONE"));
			BH_CANCEL_THE_ITEM_TO_SELL_AGAIN = String.valueOf(propZ.getProperty("BH_CANCEL_THE_ITEM_TO_SELL_AGAIN"));
			BH_CREATE_PROCESS_FAIL_CHECK_QUANTITY = String.valueOf(propZ.getProperty("BH_CREATE_PROCESS_FAIL_CHECK_QUANTITY"));
			BH_PLAYER_BUY_YOU_THE_ITEM = String.valueOf(propZ.getProperty("BH_PLAYER_BUY_YOU_THE_ITEM"));
			BH_PLAYER_BUY_YOU_THE_QUANTITY_ITEM = String.valueOf(propZ.getProperty("BH_PLAYER_BUY_YOU_THE_QUANTITY_ITEM"));
			BH_WROG_QUANTITY_TO_BUY = String.valueOf(propZ.getProperty("BH_WROG_QUANTITY_TO_BUY"));
			BH_QUESTION_YOU_HAVE_MAKE_NEW_OFFER = String.valueOf(propZ.getProperty("BH_QUESTION_YOU_HAVE_MAKE_NEW_OFFER"));
			BH_QUESTION_YOU_GONNA_CREATE_NEW_BID = String.valueOf(propZ.getProperty("BH_QUESTION_YOU_GONNA_CREATE_NEW_BID"));
			BH_EMAIL_TITLE_YOU_LOOSE_THE_ITEM_BID_BY_TIME = String.valueOf(propZ.getProperty("BH_EMAIL_TITLE_YOU_LOOSE_THE_ITEM_BID_BY_TIME"));
			BH_EMAIL_MESSAGE_YOU_LOOSE_THE_ITEM_BID_BY_TIME = String.valueOf(propZ.getProperty("BH_EMAIL_MESSAGE_YOU_LOOSE_THE_ITEM_BID_BY_TIME"));
			BH_EMAIL_TITLE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT = String.valueOf(propZ.getProperty("BH_EMAIL_TITLE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT"));
			BH_EMAIL_MESSAGE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT = String.valueOf(propZ.getProperty("BH_EMAIL_MESSAGE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT"));
			BH_EMAIL_TITLE_BID_WAS_CANCEL_BY_OWNER = String.valueOf(propZ.getProperty("BH_EMAIL_TITLE_BID_WAS_CANCEL_BY_OWNER"));
			BH_EMAIL_MESSAGE_BID_WAS_CANCEL_BY_OWNER = String.valueOf(propZ.getProperty("BH_EMAIL_MESSAGE_BID_WAS_CANCEL_BY_OWNER"));
			BH_EMAIL_TITLE_YOUR_LOST_YOUR_BID_PLACE = String.valueOf(propZ.getProperty("BH_EMAIL_TITLE_YOUR_LOST_YOUR_BID_PLACE"));
			BH_EMAIL_MESSAGE_YOUR_LOST_YOUR_BID_PLACE = String.valueOf(propZ.getProperty("BH_EMAIL_MESSAGE_YOUR_LOST_YOUR_BID_PLACE"));
			BH_SORRY_YOUR_BID_HAS_AN_OFFER_QUESTION = String.valueOf(propZ.getProperty("BH_SORRY_YOUR_BID_HAS_AN_OFFER_QUESTION"));
			BH_SORRY_YOUR_BID_HAS_AN_OFFER_NO_ITEM_PENALTY = String.valueOf(propZ.getProperty("BH_SORRY_YOUR_BID_HAS_AN_OFFER_NO_ITEM_PENALTY"));
			BH_SORRY_YOU_HAVE_THE_FIRTS_PLACE_IN_THE_BID_NEED_PAY_PENALTY = String.valueOf(propZ.getProperty("BH_SORRY_YOU_HAVE_THE_FIRTS_PLACE_IN_THE_BID_NEED_PAY_PENALTY"));
			BH_EMAIL_TITLE_CANCEL_MY_OFFERT = String.valueOf(propZ.getProperty("BH_EMAIL_TITLE_CANCEL_MY_OFFERT"));
			BH_EMAIL_MESSAGE_CANCEL_MY_OFFERT = String.valueOf(propZ.getProperty("BH_EMAIL_MESSAGE_CANCEL_MY_OFFERT"));
			BH_SORRY_BUT_YOU_DONT_HAVE_ITEMS_TO_PAY_THE_PENALTY = String.valueOf(propZ.getProperty("BH_SORRY_BUT_YOU_DONT_HAVE_ITEMS_TO_PAY_THE_PENALTY"));
			BH_ONLY_IN_PEACE_ZONE = String.valueOf(propZ.getProperty("BH_ONLY_IN_PEACE_ZONE"));
			
			WISH_LIST_NEW_AUCTION_BID_HAS_BEEN_CREATED = String.valueOf(propZ.getProperty("WISH_LIST_NEW_AUCTION_BID_HAS_BEEN_CREATED"));
			WISH_LIST_BE_MORE_PRECISE_FOR_SEARCH = String.valueOf(propZ.getProperty("WISH_LIST_BE_MORE_PRECISE_FOR_SEARCH"));
			WISH_LIST_YOU_HAVE_THIS_ITEM_IN_LIST = String.valueOf(propZ.getProperty("WISH_LIST_YOU_HAVE_THIS_ITEM_IN_LIST"));
			WISH_LIST_WAS_CREATED_WITH_DURATION_SEVEN_DAYS = String.valueOf(propZ.getProperty("WISH_LIST_WAS_CREATED_WITH_DURATION_SEVEN_DAYS"));
			WISH_LIST_CANT_ADD_MORE_TO_YOUR_LIST = String.valueOf(propZ.getProperty("WISH_LIST_CANT_ADD_MORE_TO_YOUR_LIST"));
			
			DELEVEL_REMOVE_INVALID_SKILL = String.valueOf(propZ.getProperty("DELEVEL_REMOVE_INVALID_SKILL"));
			
			ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED = String.valueOf(propZ.getProperty("ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED"));
			ENCHANT_YOU_MOVE_THE_ITEM = String.valueOf(propZ.getProperty("ENCHANT_YOU_MOVE_THE_ITEM"));
			ENCHANT_ITEM_NO_MEET_PARAMETERS_TO_ENCHANT	= String.valueOf(propZ.getProperty("ENCHANT_ITEM_NO_MEET_PARAMETERS_TO_ENCHANT"));
			ENCHANT_CONGRATULATION_ITEM_HAS_NEW_ENCHANT = String.valueOf(propZ.getProperty("ENCHANT_CONGRATULATION_ITEM_HAS_NEW_ENCHANT"));
			
			CHARPANEL_PANEL_TO_SET_VARIOUS_SETTING_FOR_YOUR_CHARACTER = String.valueOf(propZ.getProperty("CHARPANEL_PANEL_TO_SET_VARIOUS_SETTING_FOR_YOUR_CHARACTER"));
			FIXME_SELECT_THE_CHAR_HOW_NEED_TO_BE_FIX_IT = String.valueOf(propZ.getProperty("FIXME_SELECT_THE_CHAR_HOW_NEED_TO_BE_FIX_IT"));
			FIXME_EXPLAIN = String.valueOf(propZ.getProperty("FIXME_EXPLAIN"));

			CB_BTN_ANNOUCEMENT = String.valueOf(propZ.getProperty("CB_BTN_ANNOUCEMENT"));
			CB_BTN_CHANGE_LOG = String.valueOf(propZ.getProperty("CB_BTN_CHANGE_LOG"));
			CB_BTN_RULES = String.valueOf(propZ.getProperty("CB_BTN_RULES"));
			CB_BTN_STAFF = String.valueOf(propZ.getProperty("CB_BTN_STAFF"));
			CB_BTN_TOP_PLAYER = String.valueOf(propZ.getProperty("CB_BTN_TOP_PLAYER"));
			CB_BTN_HEROES = String.valueOf(propZ.getProperty("CB_BTN_HEROES"));
			CB_BTN_CLAN = String.valueOf(propZ.getProperty("CB_BTN_CLAN"));
			CB_BTN_CASTLE = String.valueOf(propZ.getProperty("CB_BTN_CASTLE"));
			CB_BTN_PRIVATE_STORE = String.valueOf(propZ.getProperty("CB_BTN_PRIVATE_STORE"));
			CB_BTN_SV_CONFIG = String.valueOf(propZ.getProperty("CB_BTN_SV_CONFIG"));
			CB_BTN_FEATURES = String.valueOf(propZ.getProperty("CB_BTN_FEATURES"));
			CB_BTN_EVENTS = String.valueOf(propZ.getProperty("CB_BTN_EVENTS"));
			CB_BTN_PLAYGAME = String.valueOf(propZ.getProperty("CB_BTN_PLAYGAME"));


			BUTTON_EXPLAINS_VOTE_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_VOTE_CB"));
			BUTTON_EXPLAINS_BUFFER_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_BUFFER_CB"));
			BUTTON_EXPLAINS_TELEPORT_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_TELEPORT_CB"));
			BUTTON_EXPLAINS_SHOP_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_SHOP_CB"));
			BUTTON_EXPLAINS_WAREHOUSE_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_WAREHOUSE_CB"));
			BUTTON_EXPLAINS_AUGMENT_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_AUGMENT_CB"));
			BUTTON_EXPLAINS_SUBCLASES_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_SUBCLASES_CB"));
			BUTTON_EXPLAINS_PROFESSION_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CLASS_TRANSFER_CB"));
			BUTTON_EXPLAINS_CONFIG_PANEL_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CONFIG_PANEL_CB"));
			BUTTON_EXPLAINS_DROP_SEARCH_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_DROP_SEARCH_CB"));
			BUTTON_EXPLAINS_PVPPK_LIST_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_PVPPK_LIST_CB"));
			BUTTON_EXPLAINS_LOG_PELEAS_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_LOG_PELEAS_CB"));
			BUTTON_EXPLAINS_CASTLE_MANAGER_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CASTLE_MANAGER_CB"));
			BUTTON_EXPLAINS_CHALLENGE_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CHALLENGE_CB"));
			BUTTON_EXPLAINS_SYMBOL_MARKET_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_SYMBOL_MARKET_CB"));
			BUTTON_EXPLAINS_CLANALLY_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CLANALLY_CB"));
			BUTTON_EXPLAINS_GO_PARTY_LEADER_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_GO_PARTY_LEADER_CB"));
			BUTTON_EXPLAINS_FLAGFINDER_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_FLAGFINDER_CB"));
			BUTTON_EXPLAINS_COLORNAME_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_COLORNAME_CB"));
			BUTTON_EXPLAINS_DELEVEL_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_DELEVEL_CB"));
			BUTTON_EXPLAINS_REMOVE_ATRIBUTE_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_REMOVE_ATRIBUTE_CB"));
			BUTTON_EXPLAINS_BUG_REPORT_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_BUG_REPORT_CB"));
			BUTTON_EXPLAINS_DONATION_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_DONATION_CB"));
			BUTTON_EXPLAINS_CAMBIO_NOMBRE_PJ_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CAMBIO_NOMBRE_PJ_CB"));
			BUTTON_EXPLAINS_CAMBIO_NOMBRE_CLAN_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_CAMBIO_NOMBRE_CLAN_CB"));
			BUTTON_EXPLAINS_MISCELLANEOUS_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_MISCELLANEOUS_CB"));
			BUTTON_EXPLAINS_ELEMENT_ENHANCED_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_ELEMENT_ENHANCED_CB"));
			BUTTON_EXPLAINS_ENCHANT_ITEM_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_ENCHANT_ITEM_CB"));
			BUTTON_EXPLAINS_AUGMENT_SPECIAL_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_AUGMENT_SPECIAL_CB"));
			BUTTON_EXPLAINS_GRAND_BOSS_STATUS_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_GRAND_BOSS_STATUS_CB"));
			BUTTON_EXPLAINS_RAIDBOSS_INFO_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_RAIDBOSS_INFO_CB"));
			BUTTON_EXPLAINS_TRANSFORMATION_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_TRANSFORMATION_CB"));
			BUTTON_EXPLAINS_AUCTIONSHOUSE_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_AUCTIONSHOUSE_CB"));
			
			BUTTON_EXPLAINS_DRESSME_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_DRESSME_CB"));
			BUTTON_EXPLAINS_BLACKSMITH_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_BLACKSMITH_CB"));
			BUTTON_EXPLAINS_PARTY_MATCHING_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_PARTY_MATCHING_CB"));
			BUTTON_EXPLAINS_BIDHOUSE_CB = String.valueOf(propZ.getProperty("BUTTON_EXPLAINS_BIDHOUSE_CB"));
			
			//GO PARTY LEADER
			GPL_PARTY_LEADER_IN_OBSERVE_MODE  = String.valueOf(propZ.getProperty("GPL_PARTY_LEADER_IN_OBSERVE_MODE"));
			GPL_PARTY_LEADER_IN_EVENT  = String.valueOf(propZ.getProperty("GPL_PARTY_LEADER_IN_EVENT"));
			GPL_PARTY_LEADER_ARE_YOU  = String.valueOf(propZ.getProperty("GPL_PARTY_LEADER_ARE_YOU"));
			GPL_PARTY_LEADER_IN_RESTRICTED_AREA  = String.valueOf(propZ.getProperty("GPL_PARTY_LEADER_IN_RESTRICTED_AREA"));
			GPL_PARTY_LEADER_IS_IN_SIEGE  = String.valueOf(propZ.getProperty("GPL_PARTY_LEADER_IS_IN_SIEGE"));

			//PROFESSION AND SUBCLASS SYSTEM
			PRO_COME_BACK_LV20  = String.valueOf(propZ.getProperty("PRO_COME_BACK_LV20"));
			PRO_COME_BACK_LV40  = String.valueOf(propZ.getProperty("PRO_COME_BACK_LV40"));
			PRO_COME_BACK_LV76  = String.valueOf(propZ.getProperty("PRO_COME_BACK_LV76"));
			SUBCLASS_NO_SUBCLASS_AVAILABLE  = String.valueOf(propZ.getProperty("SUBCLASS_NO_SUBCLASS_AVAILABLE"));
			SUBCLASS_YOU_CANT_ADD_MORE_SUBCLASS  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_CANT_ADD_MORE_SUBCLASS"));
			SUBCLASS_YOU_LEVEL_IS_LOW_TO_MAKE_ANOTHER_SUBCLASS  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_LEVEL_IS_LOW_TO_MAKE_ANOTHER_SUBCLASS"));
			SUBCLASS_YOU_SUBCLASS_CANT_BE_ADDED  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_SUBCLASS_CANT_BE_ADDED"));
			SUBCLASS_YOU_CANT_CHANGE_SUBCLASS_WHILE_CASTING  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_CANT_CHANGE_SUBCLASS_WHILE_CASTING"));
			SUBCLASS_YOU_CLASS_CANT_BE_CHANGED  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_CLASS_CANT_BE_CHANGED"));
			SUBCLASS_YOU_WANT_TO_ADD_THIS_SUBCLASS_$name  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_WANT_TO_ADD_THIS_SUBCLASS_$name"));
			SUBCLASS_YOU_WANT_TO_REMOVE_THIS_SUBCLASS_$name  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_WANT_TO_REMOVE_THIS_SUBCLASS_$name"));

			//MISCELANIUS
			MIS_YOU_WANT_TO_INSCREISE_YOU_LVL_TO_85  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_INSCREISE_YOU_LVL_TO_85"));
			MIS_YOU_WANT_TO_GET_NOBLE_STATUS  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_GET_NOBLE_STATUS"));
			MIS_YOU_WANT_TO_ACQUIRE_FAME  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_ACQUIRE_FAME"));
			MIS_YOU_WANT_CLAN_REWARD  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_CLAN_REWARD"));
			MIS_YOU_WANT_TO_REDUCE_PK_KILL  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_REDUCE_PK_KILL"));
			MIS_YOU_WANT_TO_CHANGE_YOUR_SEX  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_CHANGE_YOUR_SEX"));
			MIS_YOU_WANT_TO_BE_A_AIOCHAR_TYPE  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_BE_A_AIOCHAR_TYPE"));
			MIS_YOU_WANT_TO_CHANGE_YOUR_NAME  = String.valueOf(propZ.getProperty("MIS_YOU_WANT_TO_CHANGE_YOUR_NAME"));
			MIS_YOU_DONT_HAVE_PK_KILL  = String.valueOf(propZ.getProperty("MIS_YOU_DONT_HAVE_PK_KILL"));
			MIS_YOU_ACQUIRED_FAME  = String.valueOf(propZ.getProperty("MIS_YOU_ACQUIRED_FAME"));
			MIS_YOU_AIOCHAR_CANT_DO_IT_AGAIN  = String.valueOf(propZ.getProperty("MIS_YOU_AIOCHAR_CANT_DO_IT_AGAIN"));

			//CLAN REPUTATION EVENT
			CR_YOU_NEED_AT_LEAT_COUNT_CLAN_MEMBERS_ON  = String.valueOf(propZ.getProperty("CR_YOU_NEED_AT_LEAT_COUNT_CLAN_MEMBERS_ON"));
			CR_YOU_NEED_ALL_MEMBERS_ONLINE  = String.valueOf(propZ.getProperty("CR_YOU_NEED_ALL_MEMBERS_ONLINE"));
			CR_YOU_CLAN_IS_INVALID  = String.valueOf(propZ.getProperty("CR_YOU_CLAN_IS_INVALID"));
			CR_CONGRATULATION_YOU_CLAN_HAS_BEEN_REWARDED  = String.valueOf(propZ.getProperty("CR_CONGRATULATION_YOU_CLAN_HAS_BEEN_REWARDED"));

			//ACCOUNT RECOVERY, EMAIL REGISTRATION
			RACC_ERROR_IN_CODE  = String.valueOf(propZ.getProperty("RACC_ERROR_IN_CODE"));
			RAAC_CHECK_EMAIL  = String.valueOf(propZ.getProperty("RAAC_CHECK_EMAIL"));
			RAAC_ENTER_WROG_CODE  = String.valueOf(propZ.getProperty("RAAC_ENTER_WROG_CODE"));
			RAAC_YOU_DONT_HAVE_ANY_MORE_ACCOUNT_TO_THIS_EMAIL  = String.valueOf(propZ.getProperty("RAAC_YOU_DONT_HAVE_ANY_MORE_ACCOUNT_TO_THIS_EMAIL"));
			REGI_EMAIL_NOT_MATCH  = String.valueOf(propZ.getProperty("REGI_EMAIL_NOT_MATCH"));

			//CHANGE CHAR ACCOUNT, CHANGE PASSWORD
			CHANGE_ACC_MUST_BE_A_DIFFERENT_ACCOUNT  = String.valueOf(propZ.getProperty("CHANGE_ACC_MUST_BE_A_DIFFERENT_ACCOUNT"));
			CHANGE_ACC_FULL  = String.valueOf(propZ.getProperty("CHANGE_ACC_FULL"));
			CHANGE_ACC_NO_EXIST_NO_LINKED  = String.valueOf(propZ.getProperty("CHANGE_ACC_NO_EXIST_NO_LINKED"));
			CHANGE_PASS_LENGHT_SHOULD_BE_HIGHER_THANT_4  = String.valueOf(propZ.getProperty("CHANGE_PASS_LENGHT_SHOULD_BE_HIGHER_THANT_4"));
			CHANGE_PASS_NO_MATCH  = String.valueOf(propZ.getProperty("CHANGE_PASS_NO_MATCH"));
			CHANGE_PASS_NEW_PASSWORD  = String.valueOf(propZ.getProperty("CHANGE_PASS_NEW_PASSWORD"));
			CHANGE_PASS_ERROR  = String.valueOf(propZ.getProperty("CHANGE_PASS_ERROR"));
			CHANGE_PASS_YOU_CANT_USE_OLD_PASS_AS_NEW = String.valueOf(propZ.getProperty("CHANGE_PASS_YOU_CANT_USE_OLD_PASS_AS_NEW")); 

			//SELL ACCOUNT
			SELLACCOUNT_TIME_IS_OVER  = String.valueOf(propZ.getProperty("SELLACCOUNT_TIME_IS_OVER"));
			SELLACCOUNT_RECEIVER_PLAYER_IS_NOT_ONLINE  = String.valueOf(propZ.getProperty("SELLACCOUNT_RECEIVER_PLAYER_IS_NOT_ONLINE"));
			SELLACCOUNT_YOU_NEED_TO_WAIT_TO_RESEND  = String.valueOf(propZ.getProperty("SELLACCOUNT_YOU_NEED_TO_WAIT_TO_RESEND"));
			SELLACCOUNT_ENTER_REQUESTED_ITEM_TO_SELL  = String.valueOf(propZ.getProperty("SELLACCOUNT_ENTER_REQUESTED_ITEM_TO_SELL"));
			SELLACCOUNT_ENTER_PLAYER_TO_RECEIVED_PAYMENT  = String.valueOf(propZ.getProperty("SELLACCOUNT_ENTER_PLAYER_TO_RECEIVED_PAYMENT"));
			SELLACCOUNT_YOUR_RECEIVER_CHAR_IS_NOT_ONLINE  = String.valueOf(propZ.getProperty("SELLACCOUNT_YOUR_RECEIVER_CHAR_IS_NOT_ONLINE"));
			SELLACCOUNT_TARGET_NOT_READY  = String.valueOf(propZ.getProperty("SELLACCOUNT_TARGET_NOT_READY"));
			SELLACCOUNT_TARGET_PLAYER  = String.valueOf(propZ.getProperty("SELLACCOUNT_TARGET_PLAYER"));
			SELLACCOUNT_CANT_ADD_MORE_ITEM  = String.valueOf(propZ.getProperty("SELLACCOUNT_CANT_ADD_MORE_ITEM"));
			SELLACCOUNT_NAME_NO_EXISTS  = String.valueOf(propZ.getProperty("SELLACCOUNT_NAME_NO_EXISTS"));

			//SELL CLAN
			SELLCLAN_CLAN_LEADER_ERROR  = String.valueOf(propZ.getProperty("SELLCLAN_CLAN_LEADER_ERROR"));
			SELLCLAN_LADER_NOT_ONLINE  = String.valueOf(propZ.getProperty("SELLCLAN_LADER_NOT_ONLINE"));
			SELLCLAN_SELLER_NO_HAVE_CLAN_TO_SELL  = String.valueOf(propZ.getProperty("SELLCLAN_SELLER_NO_HAVE_CLAN_TO_SELL"));
			SELLCLAN_SELLER_NO_CLAN_LEADER  = String.valueOf(propZ.getProperty("SELLCLAN_SELLER_NO_CLAN_LEADER"));
			SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN  = String.valueOf(propZ.getProperty("SELLCLAN_NEED_TO_BE_IN_THE_SAME_CLAN"));
			SELLCLAN_YOU_NEED_TO_HAVE_A_CLAN_TO_SELL  = String.valueOf(propZ.getProperty("SELLCLAN_YOU_NEED_TO_HAVE_A_CLAN_TO_SELL"));
			SELLCLAN_ENTER_REQUESTED_ITEM_TO_SELL  = String.valueOf(propZ.getProperty("SELLCLAN_ENTER_REQUESTED_ITEM_TO_SELL"));

			//SECONDAY PASSWORD
			SP_MOST_HAVE_LENGHT  = String.valueOf(propZ.getProperty("SP_MOST_HAVE_LENGHT"));
			SP_CHAR_TEMPLATE_WROG  = String.valueOf(propZ.getProperty("SP_CHAR_TEMPLATE_WROG"));
			SP_WAS_CORRECT  = String.valueOf(propZ.getProperty("SP_WAS_CORRECT"));
			SP_WAS_INCORRECT  = String.valueOf(propZ.getProperty("SP_WAS_INCORRECT"));
			SP_WAS_INCORRECT_DISCONNECT  = String.valueOf(propZ.getProperty("SP_WAS_INCORRECT_DISCONNECT"));
			SP_WAS_CORRECT_AND_REMOVE_FROM_ACCOUNT  = String.valueOf(propZ.getProperty("SP_WAS_CORRECT_AND_REMOVE_FROM_ACCOUNT"));
			SP_WAS_INCORRECT_AND_REMOVE_PROCESS_WAS_CANCEL  = String.valueOf(propZ.getProperty("SP_WAS_INCORRECT_AND_REMOVE_PROCESS_WAS_CANCEL"));
			SP_WAS_BEEN_SET  = String.valueOf(propZ.getProperty("SP_WAS_BEEN_SET"));
			SP_PASSWORD_NEED_TO_BE_EQUALS  = String.valueOf(propZ.getProperty("SP_PASSWORD_NEED_TO_BE_EQUALS"));

			//EMAIL
			EMAIL_SOMEONE_WROTE_WRONG_YOUR_SECONDARY_PASSWORD  = String.valueOf(propZ.getProperty("EMAIL_SOMEONE_WROTE_WRONG_YOUR_SECONDARY_PASSWORD"));
			EMAIL_SELL_ACCOUNT_INFO  = String.valueOf(propZ.getProperty("EMAIL_SELL_ACCOUNT_INFO"));			
			
			
			//*AFK*//
			
			AFK_ALREADY_AFK = String.valueOf(propZ.getProperty("AFK_ALREADY_AFK"));
			AFK_YOU_CAN_AFK_WHILE_DEATH = String.valueOf(propZ.getProperty("AFK_YOU_CAN_AFK_WHILE_DEATH"));
			AFK_YOU_CAN_AFK_WHILE_COMBAT_MODE = String.valueOf(propZ.getProperty("AFK_YOU_CAN_AFK_WHILE_COMBAT_MODE"));
			AFK_YOU_CAN_AFK_WHILE_FLAG = String.valueOf(propZ.getProperty("AFK_YOU_CAN_AFK_WHILE_FLAG"));
			AFK_YOU_CAN_AFK_WHILE_PK = String.valueOf(propZ.getProperty("AFK_YOU_CAN_AFK_WHILE_PK")); 
			AFK_YOU_CAN_AFK_WHILE_SIEGE_CASTLE = String.valueOf(propZ.getProperty("AFK_YOU_CAN_AFK_WHILE_SIEGE_CASTLE")); 
			AFK_YOU_CAN_AFK_WHILE_EVENT_PVP_ZONE = String.valueOf(propZ.getProperty("AFK_YOU_CAN_AFK_WHILE_EVENT_PVP_ZONE"));
			
			//*ADMIN*//
			
			ADMIN_SEND_YOU_THE_CODE_TO_YOUR_EMAIL = String.valueOf(propZ.getProperty("ADMIN_SEND_YOU_THE_CODE_TO_YOUR_EMAIL"));
			ADMIN_SECONDARY_PASSWORD_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = String.valueOf(propZ.getProperty("ADMIN_SECONDARY_PASSWORD_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER"));
			ADMIN_ACCOUNT_EMAIL_RESET = String.valueOf(propZ.getProperty("ADMIN_ACCOUNT_EMAIL_RESET"));
			ADMIN_OLY_SCHEME_REMOVED = String.valueOf(propZ.getProperty("ADMIN_OLY_SCHEME_REMOVED"));
			ADMIN_BUFF_SCHEME_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = String.valueOf(propZ.getProperty("ADMIN_BUFF_SCHEME_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER"));
			ADMIN_PERSONAL_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = String.valueOf(propZ.getProperty("ADMIN_PERSONAL_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER"));
			ADMIN_DRESSME_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER = String.valueOf(propZ.getProperty("ADMIN_DRESSME_SETTING_WILL_BE_REMOVE_AND_DISCONNECT_FROM_SERVER"));
			ADMIN_SURVEY_ENDED_MESSAGE = String.valueOf(propZ.getProperty("ADMIN_SURVEY_ENDED_MESSAGE"));
			ADMIN_YOU_CAN_ANSWER_AGAIN = String.valueOf(propZ.getProperty("ADMIN_YOU_CAN_ANSWER_AGAIN"));
			ADMIN_GMPANEL_GATHERING_RECALL_YOU = String.valueOf(propZ.getProperty("ADMIN_GMPANEL_GATHERING_RECALL_YOU"));
			
			VOTE_INSTANCE_THIS_ZONE_IS_CLOSE_FOR_NOW = String.valueOf(propZ.getProperty("VOTE_INSTANCE_THIS_ZONE_IS_CLOSE_FOR_NOW"));
			VOTE_INSTANCE_CANT_USE_THIS_IN_DUALBOX = String.valueOf(propZ.getProperty("VOTE_INSTANCE_CANT_USE_THIS_IN_DUALBOX"));
			VOTE_INSTANCE_YOUR_CLASS_IS_NOT_ALLOWED_TO_ENTER = String.valueOf(propZ.getProperty("VOTE_INSTANCE_YOUR_CLASS_IS_NOT_ALLOWED_TO_ENTER"));
			VOTE_INSTANCE_REWARD_BY_EMAIL_MESSAGE = String.valueOf(propZ.getProperty("VOTE_INSTANCE_REWARD_BY_EMAIL_MESSAGE"));
			
			PVP_INSTANCE_YOU_NEED_TO_EXIT_TO_ENTER_TO_ANOTHER = String.valueOf(propZ.getProperty("PVP_INSTANCE_YOU_NEED_TO_EXIT_TO_ENTER_TO_ANOTHER"));
			PVP_INSTANCE_YOU_CANT_ENTER_NOW_FOR_EVENT = String.valueOf(propZ.getProperty("PVP_INSTANCE_YOU_CANT_ENTER_NOW_FOR_EVENT"));
			PVP_INSTANCE_YOU_NEED_TO_BE_ON_PEACE_ZONE = String.valueOf(propZ.getProperty("PVP_INSTANCE_YOU_NEED_TO_BE_ON_PEACE_ZONE"));
			PVP_INSTANCE_ZONE_IS_NOT_READY = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_IS_NOT_READY"));
			PVP_INSTANCE_ZONE_JUST_WITHOUT_PARTY = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_JUST_WITHOUT_PARTY"));
			PVP_INSTANCE_ZONE_JUST_PARTY = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_JUST_PARTY"));
			PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_NEED_MINIMUN_LEVEL_TO_ENTER"));
			PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER"));
			PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_HAVE_PROHIBITED_ITEM"));
			PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER_CLASS_ALLOW = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_YOUR_CLASS_CANT_ENTER_CLASS_ALLOW"));
			PVP_INSTANCE_ZONE_YOUR_CANT_ENTER_WITH_PARTY = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_YOUR_CANT_ENTER_WITH_PARTY"));
			PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_PLAYER_IS_NOT_CLOSE_FROM_PARTY_LEADER"));
			PVP_INSTANCE_ZONE_PLAYER_NO_HAVE_LEVEL = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_PLAYER_NO_HAVE_LEVEL"));
			PVP_INSTANCE_ZONE_PLAYER_NO_HAD_A_CLASS = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_PLAYER_NO_HAD_A_CLASS"));
			PVP_INSTANCE_ZONE_PLAYER_HAVE_PROHIBITED_ITEM = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_PLAYER_HAVE_PROHIBITED_ITEM"));
			PVP_INSTANCE_ZONE_JUST_CLAN_PARTY = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_JUST_CLAN_PARTY"));		
			PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_YOU_WANT_TO_ENTER"));
			
			PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NEED_CLAN = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NEED_CLAN"));
			PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NO_HAVE_ITEM_REQUESTED = String.valueOf(propZ.getProperty("PVP_INSTANCE_ZONE_THE_PLAYER_$name_CAN_ENTER_NO_HAVE_ITEM_REQUESTED"));
			
			BORROW_SYSTEM_PASSWORD_NOT_EQUALS = String.valueOf(propZ.getProperty("BORROW_SYSTEM_PASSWORD_NOT_EQUALS"));
			BORROW_SYSTEM_CONFIRM_PASSWORD_$password = String.valueOf(propZ.getProperty("BORROW_SYSTEM_CONFIRM_PASSWORD_$password"));
			BORROW_SYSTEM_PASSWORD_LENGHT_WROG = String.valueOf(propZ.getProperty("BORROW_SYSTEM_PASSWORD_LENGHT_WROG"));
			BORROW_SYSTEM_BLOCKED = String.valueOf(propZ.getProperty("BORROW_SYSTEM_BLOCKED"));
			BORROW_SYSTEM_THE_TARGET_$name_CANT_MAKE_TRADE = String.valueOf(propZ.getProperty("BORROW_SYSTEM_THE_TARGET_$name_CANT_MAKE_TRADE"));
			BORROW_SYSTEM_CAN_NOT_BE_USE_BY_GM_AND_ADMINS = String.valueOf(propZ.getProperty("BORROW_SYSTEM_CAN_NOT_BE_USE_BY_GM_AND_ADMINS"));
			BORROW_SYSTEM_ADM_$name_HAS_SET_BORROWED_PASSWORD = String.valueOf(propZ.getProperty("BORROW_SYSTEM_ADM_$name_HAS_SET_BORROWED_PASSWORD"));
			
			EXPERIENCE_ON = String.valueOf(propZ.getProperty("EXP_ON"));
			EXPERIENCE_OFF = String.valueOf(propZ.getProperty("EXP_OFF"));
			
			PRE_DONATION_TEXT = String.valueOf(propZ.getProperty("PRE_DONATION_TEXT"));
			
			load_commands();
			_log.warning("	Language Load ("+ _ABBREVIATION +")");
		}catch(Exception a){
			_log.warning(":::::::: ERROR AL CARGAR LENGUAJE ::::::::::");
		}
	}

	@SuppressWarnings("rawtypes")
	protected Vector getAdminCommand(){
		return cmd_ZeuS_Adm;
	}

	@SuppressWarnings("rawtypes")
	protected Vector getUserCommand(){
		return cmd_char;
	}
	
	private void load_commands(){
		try{
			cmd_ZeuS_Adm.clear();
		}catch(Exception a){
			
		}
		try{
			cmd_char.clear();
		}catch(Exception a){
			
		}
		
		cmd_ZeuS_Adm.add("//zeus_config : " + ZEUS_CONFIG);
		cmd_ZeuS_Adm.add("//zeus_tele : " + ZEUS_TELE);
		cmd_ZeuS_Adm.add("//zeus_shop : " + ZEUS_SHOP);
		cmd_ZeuS_Adm.add("//oly_ban [name] : " + OLY_BAN);
		cmd_ZeuS_Adm.add("//oly_unban [name] : " + OLY_UNBAN);
		cmd_ZeuS_Adm.add("//oly_reset_point [name] : " + OLY_RESET_POINT);
		cmd_ZeuS_Adm.add("//oly_point [name] : " + OLY_POINT);
		cmd_ZeuS_Adm.add("//zeus_banip [name] : " + ZEUS_BANIP);
		cmd_ZeuS_Adm.add("//zeus_ipblock : " + ZEUS_IPBLOCK);
		cmd_ZeuS_Adm.add("//zeus_botzone : " + ZEUS_BOTZONE);			
		cmd_ZeuS_Adm.add("//zeus_recallAll : " + ZEUS_RECALLALL);
		cmd_ZeuS_Adm.add("//zeus_bot_cancel [name] : " + ZEUS_BOT_CANCEL);
		cmd_ZeuS_Adm.add("//zeus_gmpanel : " + ZEUS_GMPANEL);
		cmd_ZeuS_Adm.add("//zeus_fake : " + ZEUS_FAKE);
		cmd_ZeuS_Adm.add("//zeus_fake_clone [target] : " + ZEUS_FAKE_CLONE);
		cmd_ZeuS_Adm.add("//zeus_fake_remove [target] : " + ZEUS_FAKE_REMOVE);
		cmd_ZeuS_Adm.add("//zeus_gminvis : Make ADM's invisible for GM's Staff");
		cmd_ZeuS_Adm.add("//zeus_gmvis : Make ADM's visible for GM's Staff");		
		
		cmd_char.add(".zeus : " + ZEUS);
		cmd_char.add(".changelang : " + CHANGELANG);
		cmd_char.add(".oly_buff : " + OLY_BUFF);
		cmd_char.add(".charpanel : " + CHARPANEL);
		cmd_char.add(".vote : " + VOTE);
		cmd_char.add(".acc_register : " + ACC_REGISTER);
		cmd_char.add(".changeemail : " + CHANGEEMAIL);
		cmd_char.add(".accrecovery : " + ACCRECOVERY);
		cmd_char.add(".changepassword : " + CHANGEPASSWORD);
		cmd_char.add(".removesecondarypassword : " + REMOVESECONDARYPASSWORD);
		cmd_char.add(".movechar : " + MOVECHAR);
		cmd_char.add(".combinetalisman : " + COMBINETALISMAN);
		cmd_char.add(".exp_on & .exp_off : " + EXP_STATUS);
		cmd_char.add(".dressme : " + DRESSME);
		cmd_char.add(".checkbot : " + CHECKBOT);
		cmd_char.add(".stat : " + STAT);
		cmd_char.add(".fixme : " + FIXME);
		cmd_char.add(".myinfo : " + MYINFO);
		cmd_char.add(".makeancientadena : " + MAKEANCIENTADENA);
		cmd_char.add(".party[Message] : " + PARTY);
		cmd_char.add(".buffstore : " + BUFFSTORE);
		cmd_char.add(".sellaccount : " + SELLACCOUNT);
		cmd_char.add(".sellclan : " + SELLCLAN);
		cmd_char.add(".joinraid : " + JOINRAID);
		cmd_char.add(".leaveraid : " + LEAVERAID);
		cmd_char.add(".engage : " + ENGANGE);
		cmd_char.add(".gotolove : " + GOTOLOVE);
		cmd_char.add(".divorce : " + DIVORCE);
		cmd_char.add(".deposit : " + DEPOSIT);
		cmd_char.add(".withdraw : " + WITHDRAW);
		cmd_char.add("/duel : " + DUEL);
		cmd_char.add(".autocp : " + AUTO_CP);
		cmd_char.add(".automp : " + AUTO_MP);
		cmd_char.add(".autohp : " + AUTO_HP);
		cmd_char.add(".mywishlist : " + WISHLIST);
		cmd_char.add(".dealylogin : " +  DEALY_LOGIN);
		cmd_char.add("/clanpenalty : " + CLANPENALTY);
		cmd_char.add("/attacklist : " + ATTACKLIST);
		cmd_char.add("/underattacklist : " + UNDERATTACKLIST);
		cmd_char.add("/warlist : " + WARLIST);
		cmd_char.add("/instancezone : " + INSTANCEZONE);
		cmd_char.add("/olympiadstat : " + OLYMPIADSTAT);
		cmd_char.add("/mybirthday : " + MYBIRTHDAY);
		cmd_char.add("/unstuck : " + UNSTUCK);
		cmd_char.add("/channelcreate : " + CHANNELCREATE);
		cmd_char.add("/channeldelete : " + CHANNELDELETE);
		cmd_char.add("/channelinvite : " + CHANNELINVITE);
		cmd_char.add("/channelkick : " +  CHANNELKICK);
		cmd_char.add("/channelleave : " + CHANNELLEAVE);
		cmd_char.add("/channelinfo : " + CHANNELINFO);
		cmd_char.add("/friendinvite : " + FRIENDINVITE);
		cmd_char.add("/friendlist : " + FRIENDLIST);
		cmd_char.add("/frienddel : " + FRIENDDEL);
		cmd_char.add("/block : " + BLOCK);
		cmd_char.add("/unblock : " + UNBLOCK);
		cmd_char.add("/blocklist : " +  BLOCKLIST);
		cmd_char.add("/gmlist : " + GMLIST);
		cmd_char.add("/gm : " + GM);
		cmd_char.add("/gmcancel : " + GMCANCEL);
		
	}
	
	protected String getExplains(L2PcInstance player, BUTTON _Button){
		switch (_Button) {
			case vote:
				return BUTTON_EXPLAINS_VOTE_CB;
			case Buffer:
				return BUTTON_EXPLAINS_BUFFER_CB;
			case Teleport:
				return BUTTON_EXPLAINS_TELEPORT_CB;
			case Shop:
				return BUTTON_EXPLAINS_SHOP_CB;
			case Warehouse:
				return BUTTON_EXPLAINS_WAREHOUSE_CB;
			case AugmentManager:
				return BUTTON_EXPLAINS_AUGMENT_CB;
			case SubClass:
				return BUTTON_EXPLAINS_SUBCLASES_CB;
			case Profession:
				return BUTTON_EXPLAINS_PROFESSION_CB;
			case configPanel:
				return BUTTON_EXPLAINS_CONFIG_PANEL_CB;
			case DropSearch:
				return BUTTON_EXPLAINS_DROP_SEARCH_CB;
			case pvppklog:
				return BUTTON_EXPLAINS_PVPPK_LIST_CB;
			case castleManager:
				return BUTTON_EXPLAINS_CASTLE_MANAGER_CB;
			case challange:
				return BUTTON_EXPLAINS_CHALLENGE_CB;
			case Symbolmaker:
				return BUTTON_EXPLAINS_SYMBOL_MARKET_CB;
			case clanAndAlly:
				return BUTTON_EXPLAINS_CLANALLY_CB;
			case Gopartyleader:
				return BUTTON_EXPLAINS_GO_PARTY_LEADER_CB;
			case Flagfinder:
				return BUTTON_EXPLAINS_FLAGFINDER_CB;
			case RemoveAttri:
				return BUTTON_EXPLAINS_REMOVE_ATRIBUTE_CB;
			case BugReport:
				return BUTTON_EXPLAINS_BUG_REPORT_CB;
			case donation:
				return BUTTON_EXPLAINS_DONATION_CB;
			case charclanoption:
				return BUTTON_EXPLAINS_MISCELLANEOUS_CB;
			case SelectElemental:
				return BUTTON_EXPLAINS_ELEMENT_ENHANCED_CB;
			case SelectEnchant:
				return BUTTON_EXPLAINS_ENCHANT_ITEM_CB;
			case SelectAugment:
				return BUTTON_EXPLAINS_AUGMENT_SPECIAL_CB;
			case RaidBossInfo:
				return BUTTON_EXPLAINS_RAIDBOSS_INFO_CB;
			case Transformation:
				return BUTTON_EXPLAINS_TRANSFORMATION_CB;
			case AuctionHouse:
				return BUTTON_EXPLAINS_AUCTIONSHOUSE_CB;
			case Dressme:
				return BUTTON_EXPLAINS_DRESSME_CB;
			case blacksmith:
				return BUTTON_EXPLAINS_BLACKSMITH_CB;
			case partymatching:
				return BUTTON_EXPLAINS_PARTY_MATCHING_CB;
			case BidHouse:
				return BUTTON_EXPLAINS_BIDHOUSE_CB;
		default:
			break;
		}
		return "";
	}
	
	
}
