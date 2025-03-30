package ZeuS.ZeuS;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import ZeuS.Config.general;

public class BD {

	private final String MISSING_$table$ = "	ZeuS-> $table$ table missing. Creating table";

	private final Logger _log = Logger.getLogger(BD.class.getName());

	/***
	 * Create table data
	 */
	
	private static String SQL_zeus_color_name_title = "CREATE TABLE `zeus_color_name_title`  ("+
			  "`idChar` int(11) NOT NULL,"+
			  "`color_name` int(11) NULL DEFAULT 0,"+
			  "`color_title` int(11) NULL DEFAULT 0,"+
			  "PRIMARY KEY (`idChar`) USING BTREE"+
			") ENGINE = InnoDB CHARACTER SET = latin1 COLLATE = latin1_swedish_ci ROW_FORMAT = Compact;";
	
	private static String SQL_zeus_Borrow_System = "CREATE TABLE `zeus_borrow_account` (" + 
			"  `account` varchar(100) NOT NULL," + 
			"  `borrowPass` varchar(255) NOT NULL," + 
			"  `isInUse` enum('Y','N','GM') DEFAULT 'N'," + 
			"  PRIMARY KEY (`account`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_cumulative_subclass = "CREATE TABLE `zeus_cumulative_subclass` (" + 
			"  `idchar` int(11) NOT NULL," + 
			"  `idclass` smallint(11) DEFAULT NULL," + 
			"  `idskill` int(11) NOT NULL," + 
			"  `levelskill` int(11) NOT NULL," + 
			"  `subclassName` varchar(200) DEFAULT NULL," + 
			"  `classIndex` smallint(11) DEFAULT NULL" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_dressme_items_char = "CREATE TABLE `zeus_dressme_items_char` (" + 
			"  `idChar` int(11) NOT NULL," + 
			"  `idItem` int(11) NOT NULL" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_dealy_reward = "CREATE TABLE `zeus_dealy_reward` (" + 
			"  `idValue` varchar(80) NOT NULL," +
			"  `unixstart` int(11) NOT NULL," + 
			"  `cycle` int(11) NOT NULL," + 
			"  `segpaused` int(11) NOT NULL DEFAULT '0' COMMENT '0'," + 
			"  `isDone` enum('Y','N') NOT NULL DEFAULT 'N'," + 
			"  PRIMARY KEY (`idValue`)" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_wishlist = "CREATE TABLE `zeus_wish_list` (" + 
			"  `idChar` int(11) NOT NULL," + 
			"  `idItem` int(11) NOT NULL," + 
			"  `beginDate` int(11) DEFAULT NULL," + 
			"  `endDate` int(11) DEFAULT NULL" + 
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_old_password = "CREATE TABLE `zeus_old_password` ("+
			"`account` varchar(80) NOT NULL,"+
			"`old_password` varchar(180) NOT NULL,"+
			"`change_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_dressme_exclusive = "CREATE TABLE `zeus_dressme_exclusive` ("+
			"`itemid` int(11) NOT NULL,"+
			"`clanid` int(11) NOT NULL,"+
			"`charid` int(11) NOT NULL,"+
			"`begin_date` int(11) NOT NULL,"+
			"`end_date` int(11) NOT NULL,"+
			"PRIMARY KEY (`itemid`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_bid_house = "CREATE TABLE `zeus_bid_house` ("+
			  "`idObjeto` int(11) DEFAULT NULL,"+
			  "`idOwner` int(11) DEFAULT NULL,"+
			  "`idItemRequest` int(11) DEFAULT NULL,"+
			  "`ItemRequestQuantity` bigint(11) DEFAULT NULL,"+
			  "`ItemQuantityToSell` bigint(11) DEFAULT '1',"+
			  "`startUnix` int(11) DEFAULT NULL,"+
			  "`endUnix` int(11) DEFAULT NULL,"+
			  "`feed` int(11) DEFAULT NULL"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_bid_house_offline = "CREATE TABLE `zeus_bid_house_offline` ("+
			  "`idChar` int(11) NOT NULL,"+
			  "`idItemVendido` int(11) NOT NULL DEFAULT '0',"+
			  "`idItemSolicitado` int(11) DEFAULT NULL,"+
			  "`totalItemEntregar` bigint(20) DEFAULT NULL,"+
			  "`totalItemVendidos` bigint(20) DEFAULT NULL,"+
			  "PRIMARY KEY (`idChar`,`idItemVendido`)"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_bid_players_bid = "CREATE TABLE `zeus_bid_players_bid` ("+
			  "`idObjeto` int(11) NOT NULL,"+
			  "`id_player_bid` int(11) DEFAULT NULL,"+
			  "`name_player_bid` varchar(80) DEFAULT NULL,"+
			  "`player_bid` int(11) DEFAULT NULL,"+
			  "`bid_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	
	private static String SQL_zeus_bid_players_winners = "CREATE TABLE `zeus_bid_players_winners` ("+
			  "`idObjeto` int(11) NOT NULL,"+
			  "`id_player_bid` int(11) DEFAULT NULL,"+
			  "`name_player_bid` varchar(80) DEFAULT NULL,"+
			  "`bid_date` timestamp NULL DEFAULT CURRENT_TIMESTAMP"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=utf8;";
	
	private static String SQL_zeus_Annoucement = "CREATE TABLE `zeus_annoucement` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`strtitle` varchar(80) DEFAULT '',"+
			  "`strmensaje` text CHARACTER SET utf8 COLLATE utf8_spanish_ci,"+
			  "`strgmnombre` varchar(80) DEFAULT '',"+
			  "`fecha` datetime DEFAULT NULL,"+
			  "`tipo` enum('ANNOUCEMENT','CHANGELOG','RULES','EVENTS','FEATURES','PLAYGAME') DEFAULT 'ANNOUCEMENT',"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_antibot_blacklist = "CREATE TABLE `zeus_antibot_blacklist`("+
			"`id` int(11) NOT NULL AUTO_INCREMENT,"+
			"`idchar` int(11) DEFAULT NULL,"+
			"`veces` int(11) DEFAULT '1',"+
			"`nomChar` varchar(30) DEFAULT '',"+
			"PRIMARY KEY(`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_antibot = "CREATE TABLE `zeus_antibot`("+
			"`id` int(11) NOT NULL AUTO_INCREMENT,"+
			"`ask` varchar(300) DEFAULT NULL,"+
			"`answer` varchar(16) DEFAULT NULL,"+
			"PRIMARY KEY(`id`)) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_auction_house = "CREATE TABLE `zeus_auctions_house` ("+
			  "`idObjeto` int(11) DEFAULT NULL,"+
			  "`idOwner` int(11) DEFAULT NULL,"+
			  "`idItemRequest` int(11) DEFAULT NULL,"+
			  "`ItemRequestQuantity` bigint(11) DEFAULT NULL,"+
			  "`ItemQuantityToSell` bigint(11) DEFAULT NULL,"+
			  "`startUnix` bigint(11) DEFAULT NULL,"+
			  "`feed` int(11) DEFAULT NULL"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_auction_house_offline = "CREATE TABLE `zeus_auctions_house_offline` ("+
			  "`idChar` int(11) NOT NULL,"+
			  "`idItemVendido` int(11) NOT NULL DEFAULT '0',"+
			  "`idItemSolicitado` int(11) DEFAULT NULL,"+
			  "`totalItemEntregar` bigint(20) DEFAULT NULL,"+
			  "`totalItemVendidos` bigint(20) DEFAULT NULL,"+
			  "PRIMARY KEY (`idChar`,`idItemVendido`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_augment_data = "CREATE TABLE `zeus_augment_data` ("+
			  "`id` int(11) NOT NULL,"+
			  "`Type` enum('ACTIVE','CHANCE','PASSIVE') DEFAULT NULL,"+
			  "`AugmentName` varchar(80) DEFAULT NULL,"+
			  "`AugmentDescript` varchar(255) DEFAULT NULL,"+
			  "`AugmentLevel` int(11) DEFAULT NULL,"+
			  "`AugmentSkillId` int(11) DEFAULT NULL,"+
			  "PRIMARY KEY (`id`)"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_buffer_list = "CREATE TABLE `zeus_buffer_buff_list` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`buffClass` int(2) DEFAULT NULL,"+
			  "`buffType` varchar(10) DEFAULT NULL,"+
			  "`buffOrder` int(2) NOT NULL DEFAULT '100',"+
			  "`buffId` int(5) DEFAULT '0',"+
			  "`buffLevel` int(5) DEFAULT '0',"+
			  "`buffDesc` text NOT NULL,"+
			  "`forClass` tinyint(1) DEFAULT '0',"+
			  "`canUse` tinyint(1) DEFAULT '1',"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=16341 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_buffer_scheme_contents = "CREATE TABLE `zeus_buffer_scheme_contents` ("+
			  "`id` bigint(20) NOT NULL AUTO_INCREMENT,"+
			  "`schemeId` int(11) DEFAULT NULL,"+
			  "`buffId` int(5) DEFAULT NULL,"+
			  "`buffLevel` int(5) DEFAULT NULL,"+
			  "`buffClass` int(2) DEFAULT NULL,"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_buffer_scheme_list = "CREATE TABLE `zeus_buffer_scheme_list` ("+
			  "`id` bigint(20) NOT NULL AUTO_INCREMENT,"+
			  "`playerId` varchar(40) DEFAULT NULL,"+
			  "`buffPlayer` tinyint(1) NOT NULL DEFAULT '0',"+
			  "`schemeName` varchar(36) DEFAULT NULL,"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_buffstore = "CREATE TABLE `zeus_buffstore` ("+
			  "`idChar` int(10) DEFAULT NULL,"+
			  "`idRequest` int(11) DEFAULT NULL,"+
			  "`price` double DEFAULT NULL,"+
			  "`title` varchar(50) DEFAULT '',"+
			  "`clan` enum('true','false') DEFAULT NULL,"+
			  "`wan` enum('true','false') DEFAULT NULL,"+
			  "`friend` enum('true','false') DEFAULT NULL,"+
			  "`email` enum('true','false') DEFAULT NULL,"+
			  "`x` int(11) DEFAULT NULL,"+
			  "`y` int(11) DEFAULT NULL,"+
			  "`z` int(11) DEFAULT NULL,"+
			  "`time` bigint(13) DEFAULT NULL,"+
			  "`ipwan` varchar(80) DEFAULT NULL"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_buffstore_scheme = "CREATE TABLE `zeus_buffstore_scheme` ("+
			  "`id` bigint(20) NOT NULL AUTO_INCREMENT,"+
			  "`idplayer` int(11) DEFAULT NULL,"+
			  "`id_ppl_seller` int(11) DEFAULT NULL,"+
			  "`scheme_name` varchar(30) DEFAULT NULL,"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_buffstore_scheme_buff = "CREATE TABLE `zeus_buffstore_scheme_buff` ("+
			"`idscheme` bigint(20) NOT NULL,"+
			"`idbuff` int(11) DEFAULT NULL"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private static String SQL_Buff_Char_sch = "CREATE TABLE `zeus_buff_char_sch` ("+
			  "`id` bigint(20) NOT NULL AUTO_INCREMENT,"+
			  "`idChar` int(11) NOT NULL,"+
			  "`NomSch` varchar(15) NOT NULL,"+
			  "`idIcon` varchar(20) NOT NULL DEFAULT '0' ,"+
			  "PRIMARY KEY (`id`)" +
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";
	
	private static String SQL_buff_char_sch_buff = "CREATE TABLE `zeus_buff_char_sch_buff` (" +
			  "`idSch` bigint(20) NOT NULL,"+
			  "`idBuff` int(11) NOT NULL,"+
			  "`lvlBuff` smallint(6) DEFAULT NULL,"+
			  "`isPremium` enum('true','false') DEFAULT 'false'"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_buffer_aio = "CREATE TABLE `zeus_buff_for_aio` ("+
			  "`nombreBuff` varchar(80) DEFAULT NULL,"+
			  "`idBuff` int(11) DEFAULT NULL,"+
			  "`buffLevel` int(11) DEFAULT NULL,"+
			  "`buffEnchant` int(11) DEFAULT NULL"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_bug_report = "CREATE TABLE `zeus_bug_report` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`tipo` varchar(50) DEFAULT NULL,"+
			  "`mensaje` varchar(350) DEFAULT NULL,"+
			  "`fechaIngreso` datetime DEFAULT NULL,"+
			  "`PlayerNom` varchar(80) DEFAULT NULL,"+
			  "`leido` enum('SI','NO') DEFAULT 'NO',"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private static String SQL_zeus_cb_clan_foro = "CREATE TABLE `zeus_cb_clan_foro` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`idChar` int(11) DEFAULT NULL,"+
			  "`idClan` int(11) DEFAULT NULL,"+
			  "`memo` text,"+
			  "`createdate` datetime DEFAULT NULL,"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";	
	
	private final String SQL_zeus_certification = "CREATE TABLE `zeus_certification` ("+
			  "`idChar` int(11) NOT NULL,"+
			  "`idClass` int(11) NOT NULL,"+
			  "`EmergentAbility` int(11) DEFAULT '0',"+
			  "`Skill` int(11) DEFAULT '0',"+
			  "`Transform` int(11) DEFAULT '0',"+
			  "`SkillIds` varchar(120) DEFAULT ''"+
			") ENGINE=InnoDB DEFAULT CHARSET=utf8;";

	private static String SQL_zeus_char_config = "CREATE TABLE `zeus_char_config` ("+
			  "`idchar` int(10) NOT NULL,"+
			  "`annouc` tinyint(4) DEFAULT '0',"+
			  "`effect` tinyint(4) DEFAULT '0',"+
			  "`statt` tinyint(4) DEFAULT '0',"+
			  "`pin` tinyint(4) DEFAULT '0',"+
			  "`pinCode` varchar(60) DEFAULT '',"+
			  "`banOly` tinyint(4) DEFAULT '0',"+
			  "`hero` tinyint(4) DEFAULT '0',"+
			  "`expsp` tinyint(4) DEFAULT '1',"+
			  "`trade` tinyint(4) DEFAULT '0',"+
			  "`badbuff` tinyint(4) DEFAULT '0',"+
			  "`hidestore` tinyint(4) DEFAULT '0',"+
			  "`refusal` tinyint(4) DEFAULT '0',"+
			  "`partymatching` tinyint(4) DEFAULT '0',"+
			  "`olyBuffScheme` tinyint(4) DEFAULT '0',"+
			  "`olyReadWinners` tinyint(4) DEFAULT '0',"+
			  "`language` char(3) DEFAULT 'eng',"+
			  "PRIMARY KEY (`idchar`)" +
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_config = "CREATE TABLE `zeus_config_seccion` ("+
			  "`id` smallint(6) NOT NULL AUTO_INCREMENT,"+
			  "`seccion` varchar(80) NOT NULL DEFAULT '',"+
			  "`param` varchar(550) DEFAULT NULL,"+
			  "PRIMARY KEY (`id`,`seccion`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_connection = "CREATE TABLE `zeus_connection` ("+
			  "`ipWan` varchar(20) DEFAULT NULL,"+
			  "`ipLan` varchar(20) DEFAULT NULL,"+
			  "`account` varchar(80) DEFAULT NULL,"+
			  "`char` varchar(80) DEFAULT NULL,"+
			  "`status` enum('CONNECTED','DISCONNECTED') DEFAULT NULL,"+
			  "`date` datetime DEFAULT NULL,"+
			  "`charID` int(11) DEFAULT 0"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_dona_creditos = "CREATE TABLE `zeus_dona_creditos` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`cuenta` varchar(180) DEFAULT NULL,"+
			  "`creditos` int(11) DEFAULT '0',"+
			  "`entregados` enum('NO','SI') DEFAULT 'NO',"+
			  "`fechaDeposit` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"+
			  "`fechaEntregado` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'datetime',"+
			  "`emailed` enum('true','false') DEFAULT 'false',"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private static String SQL_zeus_dona_espera = "CREATE TABLE `zeus_dona_espera` ("+
			  "`id` varchar(250) NOT NULL,"+
			  "`dona_monto` varchar(20) DEFAULT NULL,"+
			  "`dona_char` varchar(80) DEFAULT NULL,"+
			  "`dona_medio` varchar(80) DEFAULT NULL,"+
			  "`dona_email` varchar(250) DEFAULT NULL,"+
			  "`dona_obser` text,"+
			  "`dona_activa` enum('true','false') DEFAULT 'true',"+
			  "`dona_fecha` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_zeus_dona_gift = "CREATE TABLE `zeus_dona_gift` ("+
	  "`id` bigint(11) NOT NULL AUTO_INCREMENT,"+
	  "`idChar` int(11) NOT NULL,"+
	  "`beginDate` timestamp NULL DEFAULT CURRENT_TIMESTAMP,"+
	  "`times` smallint(6) DEFAULT '0',"+
	  "`lastTimeUnix` int(11) DEFAULT NULL,"+
	  "`lastTime` varchar(30) DEFAULT NULL,"+
	  "`canReceiveMore` enum('true','false') DEFAULT 'true',"+
	  "PRIMARY KEY (`id`),"+
	  "KEY `index_canReceiveMore` (`canReceiveMore`) USING BTREE"+
	") ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_dona_shop = "CREATE TABLE `zeus_dona_shop` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`nom` varchar(80) DEFAULT NULL,"+
			  "`tipoAccion` enum('NOBLE','HEROE','FAMA','CLAN_REPUTATION','LVL_85','SEC','MULTISELL','EXEC_MULTISELL','HTML','BUYLIST','CLAN_LVL') DEFAULT 'SEC',"+
			  "`param1` varchar(80) DEFAULT '',"+
			  "`param2` varchar(80) DEFAULT '',"+
			  "`DC_Count` int(11) DEFAULT '-1',"+
			  "`idSec` int(11) DEFAULT '-1',"+
			  "`posi` int(11) DEFAULT '0',"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=latin1;";
	
	private static String SQL_dressme = "CREATE TABLE `zeus_dressme` ("+
			  "`id` bigint(20) NOT NULL AUTO_INCREMENT,"+
			  "`idChar` int(11) DEFAULT NULL,"+
			  "`d1` varchar(300) DEFAULT '',"+
			  "`d2` varchar(300) DEFAULT '',"+
			  "`d3` varchar(300) DEFAULT '',"+
			  "`d4` varchar(300) DEFAULT '',"+
			  "`d5` varchar(300) DEFAULT '',"+
			  "`d6` varchar(300) DEFAULT '',"+
			  "`d7` varchar(300) DEFAULT '',"+
			  "`d8` varchar(300) DEFAULT '',"+
			  "`d9` varchar(300) DEFAULT '',"+
			  "`d10` varchar(300) DEFAULT '',"+
			  "`used` smallint(6) DEFAULT NULL,"+
			  "`seeOtherDressme` enum('no','yes') DEFAULT 'yes',"+
			  "PRIMARY KEY (`id`),"+
			  "KEY `dressme_idchar` (`idChar`) USING BTREE,"+
			  "KEY `dressme_id` (`id`) USING BTREE"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8;";

	private static String SQL_Evento = "CREATE TABLE `zeus_evento` ("+
			  "`idChar` int(11) DEFAULT NULL,"+
			  "`Accion` varchar(10) DEFAULT NULL,"+
			  "`fecha` varchar(30) DEFAULT NULL"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_Evento_IN = "CREATE TABLE `zeus_evento_in` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`idNPC` int(11) DEFAULT NULL,"+
			  "`idCHAR` bigint(11) DEFAULT NULL,"+
			  "`fechahora` datetime DEFAULT NULL,"+
			  "`idPremioDado` int(11) DEFAULT NULL,"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_EventoPremios = "CREATE TABLE `zeus_evento_premios` ("+
			  "`idPremio` smallint(6) DEFAULT NULL,"+
			  "`idItem` int(11) DEFAULT NULL,"+
			  "`Cantidad` int(6) DEFAULT NULL,"+
			  "`estado` enum('SI','NO') DEFAULT 'NO',"+
			  "`npcID` int(11) DEFAULT '0'"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_FakeHeroes = "CREATE TABLE `zeus_fake_heroes` ("+
			  "`idPlayer` int(11) NOT NULL,"+
			  "`id` int(11) NOT NULL,"+
			  "`race` int(11) DEFAULT NULL,"+
			  "`sex` int(11) DEFAULT NULL,"+
			  "`plClass` int(11) DEFAULT NULL,"+
			  "`plTitle` varchar(50) DEFAULT NULL,"+
			  "`plName` varchar(50) DEFAULT NULL,"+
			  "`plTitleColor` varchar(11) DEFAULT NULL,"+
			  "`plNameColor` varchar(11) DEFAULT NULL,"+
			  "`hairStyle` int(11) DEFAULT NULL,"+
			  "`hairColor` int(11) DEFAULT NULL,"+
			  "`face` int(11) DEFAULT NULL,"+
			  "`RHand` int(11) DEFAULT NULL,"+
			  "`LHand` int(11) DEFAULT NULL,"+
			  "`Gloves` int(11) DEFAULT NULL,"+
			  "`Chest` int(11) DEFAULT NULL,"+
			  "`Legs` int(11) DEFAULT NULL,"+
			  "`Feet` int(11) DEFAULT NULL,"+
			  "`LRHand` int(11) DEFAULT NULL,"+
			  "`Hair1` int(11) DEFAULT NULL,"+
			  "`Hair2` int(11) DEFAULT NULL,"+
			  "`enchantEffect` int(11) DEFAULT NULL,"+
			  "`hero` tinyint(4) DEFAULT '0',"+			  
			  "`cloak` int(11) DEFAULT NULL,"+			  
			  "`idClan` int(11) DEFAULT '0',"+
			  "`idAlly` int(11) DEFAULT '0',"+			  
			  "`idCrestClan` int(11) DEFAULT '0',"+
			  "`idCrestAlly` int(11) DEFAULT '0',"+			  
			  "PRIMARY KEY (`id`)"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_fake_npc = "CREATE TABLE `zeus_fake_npc` ("+
	  "`idPlayer` int(11) NOT NULL,"+
	  "`id` int(11) NOT NULL,"+
	  "`race` int(11) DEFAULT NULL,"+
	  "`sex` int(11) DEFAULT NULL,"+
	  "`plClass` int(11) DEFAULT NULL,"+
	  "`plTitle` varchar(50) DEFAULT NULL,"+
	  "`plName` varchar(50) DEFAULT NULL,"+
	  "`plTitleColor` varchar(11) DEFAULT NULL,"+
	  "`plNameColor` varchar(11) DEFAULT NULL,"+
	  "`hairStyle` int(11) DEFAULT NULL,"+
	  "`hairColor` int(11) DEFAULT NULL,"+
	  "`face` int(11) DEFAULT NULL,"+
	  "`RHand` int(11) DEFAULT NULL,"+
	  "`LHand` int(11) DEFAULT NULL,"+
	  "`Gloves` int(11) DEFAULT NULL,"+
	  "`Chest` int(11) DEFAULT NULL,"+
	  "`Legs` int(11) DEFAULT NULL,"+
	  "`Feet` int(11) DEFAULT NULL,"+
	  "`LRHand` int(11) DEFAULT NULL,"+
	  "`Hair1` int(11) DEFAULT NULL,"+
	  "`Hair2` int(11) DEFAULT NULL,"+
	  "`enchantEffect` int(11) DEFAULT NULL,"+
	  "`hero` tinyint(4) DEFAULT '0',"+
	  "`cloak` int(11) DEFAULT NULL,"+
	  "`idClan` int(11) DEFAULT '0',"+
	  "`idAlly` int(11) DEFAULT '0',"+			  
	  "`idCrestClan` int(11) DEFAULT '0',"+
	  "`idCrestAlly` int(11) DEFAULT '0',"+		  
	  "PRIMARY KEY (`id`)"+
	") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_ipblock = "CREATE TABLE `zeus_ipblock` ("+
			  "`ipWAN` varchar(20) NOT NULL DEFAULT '',"+
			  "`ipRED` varchar(20) NOT NULL DEFAULT ''"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	
	
	private final String SQL_jail_bail_blacklist = "CREATE TABLE `zeus_jail_bail_blacklist` ("+
			"`idchar` int(11) NOT NULL,"+
			"`veces` int(11) NOT NULL DEFAULT '1',"+
			"`nomChar` varchar(30) NOT NULL DEFAULT '',"+
			"PRIMARY KEY (`idchar`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_logFight = "CREATE TABLE `zeus_log_fight` ("+
			  "`Atacante` varchar(80) DEFAULT NULL,"+
			  "`idAtacante` int(11) DEFAULT NULL,"+
			  "`Asesinado` varchar(80) DEFAULT NULL,"+
			  "`idAsesinado` int(11) DEFAULT NULL,"+
			  "`tipPelea` enum('PK','PVP') DEFAULT NULL,"+
			  "`veces` int(11) DEFAULT '1'"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_oly_sch = "CREATE TABLE `zeus_oly_sch` ("+
			  "`id` bigint(20) NOT NULL AUTO_INCREMENT,"+
			  "`idChar` int(11) NOT NULL,"+
			  "`nombre` varchar(20) NOT NULL,"+
			  "PRIMARY KEY (`id`),"+
			  "KEY `Z_O_SCH_1` (`id`) USING BTREE"+
			") ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=latin1;";
	
	private final String SQL_oly_sch_buff = "CREATE TABLE `zeus_oly_sch_buff` ("+
			  "`idsch` bigint(20) NOT NULL,"+
			  "`idbuff` smallint(6) DEFAULT NULL,"+
			  "KEY `Z_O_S_BUFF_1` (`idsch`) USING BTREE,"+
			  "KEY `Z_O_S_BUFF_2` (`idsch`,`idbuff`) USING BTREE"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_premium = "CREATE TABLE `zeus_premium` ("+
			  "`id` varchar(50) NOT NULL DEFAULT '0',"+
			  "`start_date` int(11) DEFAULT NULL,"+
			  "`end_date` int(11) DEFAULT NULL,"+
			  "`tip` enum('ACCOUNT','CLAN') DEFAULT 'ACCOUNT',"+
			  "`idPremium` int(11) NOT NULL DEFAULT '0',"+
			  "PRIMARY KEY (`id`,`tip`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private static String SQL_zeus_pre_donation = "CREATE TABLE `zeus_pre_donation` ("+
	  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
	  "`from` varchar(120) DEFAULT '',"+
	  "`email` varchar(255) DEFAULT '',"+
	  "`donationMount` int(11) DEFAULT '0',"+
	  "`donationToGive` int(11) DEFAULT '0',"+
	  "`given` enum('YES','NO') DEFAULT 'NO',"+
	  "PRIMARY KEY (`id`)"+
	") ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;";
	
	private final String SQL_zeus_pvp_zone = "CREATE TABLE `zeus_pvp_zone` ("+
			  "`idChar` int(11) NOT NULL,"+
			  "`month` int(11) NOT NULL,"+
			  "`allKill` int(11) DEFAULT NULL,"+
			  "`allDeath` int(11) DEFAULT NULL,"+
			  "PRIMARY KEY (`idChar`,`month`)"+
			  ") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_Rank = "CREATE TABLE `zeus_rank_acc` ("+
			"`id` int(11) NOT NULL,"+
			"`tip` enum('SHOP','TELEPORT','SEARCH_DROP','SEARCH_NPC') NOT NULL,"+
			"`cant` bigint(20) DEFAULT NULL,"+
			"`seccion` varchar(50) DEFAULT 'null',"+
			"PRIMARY KEY (`id`,`tip`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=latin1;";
	
	private final String SQL_secundary_pass = "CREATE TABLE `zeus_secundary_pass` ("+
			"`account` varchar(120) NOT NULL,"+
			"`sec_pass` varchar(90) NOT NULL,"+
			"PRIMARY KEY (`account`)"+
			") ENGINE=InnoDB DEFAULT CHARSET=utf8;";		
	
	private static String SQL_shop = "CREATE TABLE `zeus_shop` ("+
			  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
			  "`nom` varchar(80) NOT NULL DEFAULT '',"+
			  "`descrip` varchar(80) DEFAULT '',"+
			  "`ima` varchar(300) DEFAULT '',"+
			  "`tip` enum('buylist','secc','multisell','exec_multisell','html') DEFAULT 'secc',"+
			  "`idarch` varchar(30) DEFAULT '0',"+
			  "`idsec` int(11) DEFAULT '-1',"+
			  "`pos` int(11) DEFAULT NULL,"+
			  "`idItemShow` int(11) DEFAULT '0',"+
			  "PRIMARY KEY (`id`)"+
			") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

	private static String SQL_teleport = "CREATE TABLE `zeus_teleport` ("+
				  "`id` int(11) NOT NULL AUTO_INCREMENT,"+
				  "`nom` varchar(80) NOT NULL,"+
				  "`descrip` varchar(80) DEFAULT '',"+
				  "`tip` enum('go','secc') DEFAULT 'secc',"+
				  "`x` int(11) DEFAULT '0',"+
				  "`y` int(11) DEFAULT '0',"+
				  "`z` int(11) DEFAULT '0',"+
				  "`idsec` int(11) DEFAULT '-1',"+
				  "`forNoble` enum('false','true') DEFAULT 'false',"+
				  "`cangoFlag` enum('true','false') DEFAULT 'true',"+
				  "`cangoKarma` enum('true','false') DEFAULT 'true',"+
				  "`lvlup` smallint(6) DEFAULT '1',"+
				  "`pos` int(11) DEFAULT NULL,"+
				  "`dualbox` enum('false','true') DEFAULT 'true',"+
				  "PRIMARY KEY (`id`)"+
				") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;";

	private static String SQL_votos ="CREATE TABLE `zeus_votos` ("+
		  "`idChar` int(18) NOT NULL,"+
		  "`voto` smallint(6) DEFAULT NULL,"+
		  "`fecha` int(18) DEFAULT NULL,"+
		  "`web` smallint(6) DEFAULT NULL"+
		") ENGINE=InnoDB DEFAULT CHARSET=latin1;";	

	

	
	
	

	
	
	
	

	










	
	
	
	

	


/**Septiembre 2014*/

	/**
	 *
	 * Arriba las tablas para crear
	 *
	 *
	 * Desde abajo los Insert viejos sin actualizaciones
	 *
	 * **/

	private final String SQL_antibot_data = "INSERT IGNORE INTO zeus_antibot(ask,answer) VALUES ('1+1','2'),('2+2',4),('2*2','4'),('10+20','30')";

	private final String SQL_AUGMENT_1 = "INSERT IGNORE INTO `zeus_augment_data` VALUES ('16163','ACTIVE','Mystery Skill','Increases your head size.','1','3203'), ('16164','ACTIVE','Kiss of Eva','Increases Lung Capacity temporarily.','10','3143'), ('16165','ACTIVE','Acrobatics','Increases the height from which you can jump without sustaining damage temporarily.','10','3144'), ('16166','ACTIVE','Iron Body','Raises resistance to damage from falling.','10','3145'), ('16167','ACTIVE','Firework','Ignites a Firework.','1','3156'), ('16168','ACTIVE','Music','Plays music.','10','3206'), ('16169','ACTIVE','Large Firework','Ignites a Large Firework.','1','3157'), ('16170','CHANCE','Charm','Decreases a target\\'s urge to attack during a general physical attack. Power 219.','10','3081'), ('16171','CHANCE','Aggression','Provokes a target to attack during an ordinary physical attack. Power 438.','10','3080'), ('16172','CHANCE','Slow','Momentarily decreases a target\\'s speed during an ordinary physical attack. Effect 3.','10','3083'), ('16173','CHANCE','Aggression Down','Decreases a target\\'s urge to attack during a critical attack. Power 219.','10','3109'), ('16174','CHANCE','Aggression Up','Increases a target\\'s urge to attack during a critical attack. Power 438.','10','3108'), ('16175','CHANCE','Slow','Momentarily decreases the target\\'s speed during a critical attack. Effect 3.','10','3111'), ('16176','CHANCE','Slow','Momentarily decreases the target\\'s speed during magic use. Effect 3.','10','3096'), ('16177','PASSIVE','Kiss of Eva','Increases lung capacity when equipped.','10','3252'), ('16178','PASSIVE','Acrobatics','Increases the height from which you can jump without sustaining damage when equipped.','10','3253'), ('16179','PASSIVE','Iron Body','Raises resistance to damage from falling when equipped.','10','3254'), ('16180','ACTIVE','Winter','Temporarily decreases a target\\'s Atk. Spd.','10','3188'), ('16181','ACTIVE','Agility','Increases Dodge temporarily.','10','3139'), ('16182','ACTIVE','Bleed','Temporarily causes a target to bleed heavily. Effect 5.','10','3196'), ('16183','ACTIVE','Ritual','Regenerates CP. Power 264.','10','3130'), ('16185','ACTIVE','Fear','Momentarily throws the target into a state of fear and causes him to flee.','10','3194'), ('16186','ACTIVE','Prominence','Unleashes a flaming attack against the enemies near a target. Power 31.','10','3173'), ('16188','ACTIVE','Charm','Decreases a target\\'s urge to attack. Power 219.','10','3150'), ('16189','ACTIVE','Aggression','Increases the target\\'s urge to attack. Power 438.','10','3149'), ('16190','ACTIVE','Guidance','Increases Accuracy temporarily.','10','3140'), ('16191','ACTIVE','Hold','Temporarily throws the target into a state of hold. The target cannot be affected by any additional hold attacks while the effect lasts.','10','3190'), ('16193','ACTIVE','Heal Empower','Increases the power of HP recovery magic temporarily.','10','3138'), ('16194','ACTIVE','Prayer','Increases the effectiveness of HP recovery magic temporarily.','10','3126'), ('16195','ACTIVE','Heal','Immediately recovers your HP. Power 308.','10','3123'), ('16196','ACTIVE','Empower','Increases M. Atk. temporarily.','10','3133'), ('16197','ACTIVE','Cheer','Increases the Max. CP by 30 temporarily.','10','3131'), ('16198','ACTIVE','Battle Roar','Increases the Max. HP temporarily and restores HP by the increased amount.','10','3125'), ('16199','ACTIVE','Blessed Body','Increases the Max. HP by 30 temporarily.','10','3124'), ('16200','ACTIVE','Blessed Soul','Increases the maximum MP by 20 temporarily.','10','3128'), ('16201','ACTIVE','Magic Barrier','Increases M. Def. temporarily.','10','3136'), ('16202','ACTIVE','Mana Burn','Burns up the enemy\\'s MP. Power 68.','10','3154'), ('16203','ACTIVE','Mana Gain','Increases the recharge recover rate of MP.','10','3129'), ('16204','ACTIVE','Recharge','Regenerates MP. Power 41.','10','3127'), ('16206','ACTIVE','Might','Increases P. Atk. temporarily.','10','3132'), ('16207','ACTIVE','Paralyze','Temporarily throws the target into a state of paralysis.','10','3192'), ('16208','ACTIVE','Shield','Increases P. Def. temporarily.','10','3135'), ('16209','ACTIVE','Poison','Temporarily poisons a target. Effect 5.','10','3195'), ('16210','ACTIVE','Duel Weakness','Decreases the opponent\\'s PVP P. Atk. temporarily.','10','3137'), ('16211','ACTIVE','Duel Might','Increases PVP P. Atk. temporarily.','10','3134'), ('16212','ACTIVE','Recall','Teleports the caster to a village. Cannot be used in special areas, such as the GM Consultation Room.','1','3146'), ('16213','ACTIVE','Resurrection','Resurrects a corpse.','9','3160'), ('16214','ACTIVE','Stone','Unleashes an earthen attack against nearby enemies. Power 31.','10','3183'), ('16215','ACTIVE','Prominence','Unleashes a flaming attack against nearby enemies. Power 31.','10','3180'), ('16216','ACTIVE','Solar Flare','Unleashes a sacred attack against nearby enemies. Power 31.','10','3184'), ('16217','ACTIVE','Aura Flare','Unleashes an elemental attack against nearby enemies. Power 31.','10','3186'), ('16218','ACTIVE','Shadow Flare','Unleashes a dark attack against nearby enemies. Power 31.','10','3185'), ('16219','ACTIVE','Hydro Blast','Unleashes a powerful liquidy attack against nearby enemies. Power 31.','10','3181'), ('16220','ACTIVE','Hurricane','Unleashes a powerful gusting attack against nearby enemies. Power 31.','10','3182'), ('16221','ACTIVE','Sleep','Instantly puts a target into sleep. Additional chance to be put into sleep greatly decreases while the effect lasts.','10','3191'), ('16222','ACTIVE','Slow','Temporarily decreases a target\\'s speed.','10','3187'), ('16223','ACTIVE','Stun','Temporarily throws the target into a state of shock.','10','3189'), ('16224','ACTIVE','Stone','Attacks the target with a stone boulder. Power 61.','10','3169'), ('16225','ACTIVE','Prominence','Detonates a fireball by compressing the air around the caster. Power 61.','10','3165'), ('16226','ACTIVE','Solar Flare','Unleashes a sacred attack. Power 61.','10','3170'), ('16227','ACTIVE','Aura Flare','NO DESCRIPTION','10','3172'), ('16231','ACTIVE','Trick','Cancels the target\\'s status.','10','3152'), ('16232','ACTIVE','Medusa','Temporarily throws the target into a petrified state.','10','3193'), ('16233','ACTIVE','Shadow Flare','Unleashes a dark attack. Power 61.','10','3171'), ('16234','ACTIVE','Unlock','Opens level 1 doors with 100% probability, level 2 doors with 30% probability, and chests below level 36 with 90% probability. Requires 4 Keys of a Thief.','10','3155'), ('16235','ACTIVE','Vampiric Touch','Absorbs HP. Power 49.','10','3153'), ('16236','ACTIVE','Hydro Blast','Unleashes a spray of highly pressurized water. Power 61.','10','3167'), ('16237','ACTIVE','Hurricane','Creates a whirlwind of destruction. Power 61.','10','3168'), ('16238','CHANCE','Winter','Momentarily decreases a target\\'s Atk. Spd. during an ordinary physical attack. Effect 3.','10','3084'), ('16239','CHANCE','Bleed','Momentarily throws the target into a bleeding state during a general physical attack. Effect 5.','10','3092'), ('16240','CHANCE','Fear','Momentarily throws the target into a state of fear and causes him to flee during a general physical attack.','10','3090'), ('16241','CHANCE','Hold','Momentarily throws the target into a state of hold during an ordinary physical attack. The target cannot be affected by any additional hold attacks while the effect lasts.','10','3086'), ('16242','CHANCE','Poison','Momentarily throws the target into a poisoned state during a general physical attack. Effect 5.','10','3091'), ('16243','CHANCE','Medusa','Momentarily throws the target into a petrified state during a general physical attack.','10','3089'), ('16244','CHANCE','Winter','Momentarily decreases the target\\'s Atk. Spd. during a critical attack. Effect 3.','10','3112'), ('16245','CHANCE','Bleed','Momentarily throws the target into a bleeding state during a critical attack. Effect 5.','10','3120'), ('16246','CHANCE','Fear','Momentarily throws the target into a state of fear and causes him to flee during a critical attack.','10','3118'), ('16247','CHANCE','Hold','Momentarily throws the target into a state of hold during a critical attack. The target cannot be affected by any additional hold attacks while the effect lasts.','10','3114'), ('16248','CHANCE','Poison','Momentarily throws the target into a poisoned state during a critical attack. Effect 5.','10','3119'), ('16249','CHANCE','Medusa','Momentarily throws the target into a petrified state during a critical attack.','10','3117'), ('16250','CHANCE','Winter','Momentarily decreases a target\\'s Atk. Spd. when you are under attack.','10','3227'), ('16251','CHANCE','Agility','Temporarily increases Evasion when under attack.','10','3221'), ('16252','CHANCE','Bleed','Momentarily causes the target to bleed when you are under attack. Effect 5.','10','3235'), ('16253','CHANCE','Ritual','Restores CP when under attack.','10','3213'), ('16254','CHANCE','Focus','Temporarily increases the critical attack rate when under attack.','10','3223'), ('16255','CHANCE','Charm','Decreases the enemy\\'s urge to attack when you are under attack.','10','3225'), ('16256','CHANCE','Guidance','Temporarily increases Accuracy when under attack.','10','3222'), ('16257','CHANCE','Hold','Momentarily holds the target when you are under attack. Additional chance to be put into hold greatly decreases while the effect lasts.','10','3229'), ('16258','CHANCE','Prayer','Increases the effect of HP recovery magic by using attack rate for a certain amount of time.','10','3209'), ('16259','CHANCE','Heal','Restores your HP by using attack rate.','10','3207'), ('16260','CHANCE','Empower','Temporarily increases PVP M. Atk. when under attack.','10','3216'), ('16261','CHANCE','Wild Magic','Temporarilty increases the critical attack rate of magic attacks when under attack.','10','3224'), ('16262','CHANCE','Cheer','Increases Max. CP when under attack for a certain amount of time.','10','3214'), ('16263','CHANCE','Blessed Body','Increases Max. HP by using attack rate for a certain amount of time.','10','3208'), ('16264','CHANCE','Blessed Soul','Increases maximum MP when under attack for a certain amount of time.','10','3211'), ('16265','CHANCE','Magic Barrier','Temporarily increases M. Def. when under attack.','10','3219'), ('16266','CHANCE','Might','Temporarily increases P. Atk. when under attack.','10','3215'), ('16267','CHANCE','Shield','Temporarily increases P. Def. when under attack.','10','3218'), ('16268','CHANCE','Poison','Momentarily poisons the target when you are under attack. Effect 5.','10','3234'), ('16269','CHANCE','Duel Weakness','Temporarily decreases the opponent\\'s PVP P. Atk. when you are under attack.','10','3220')";
	private final String SQL_AUGMENT_2 = "INSERT IGNORE INTO `zeus_augment_data` VALUES ('16270','CHANCE','Duel Might','Temporarily increases PVP P. Atk. when under attack.','10','3217'), ('16271','CHANCE','Sleep','Momentarily causes the target to sleep when you are under attack. Additional chance to be put into sleep greatly decreases while the effect lasts.','10','3230'), ('16272','CHANCE','Slow','Momentarily decreases a target\\'s Speed when you are under attack.','10','3226'), ('16273','CHANCE','Winter','Momentarily decreases the target\\'s Atk. Spd. during magic use. Effect 3.','10','3097'), ('16274','CHANCE','Bleed','Momentarily throws the target into a bleeding state during magic use. Effect 5.','10','3105'), ('16275','CHANCE','Fear','Momentarily throws the target into a state of fear and causes him to flee during magic use.','10','3103'), ('16276','CHANCE','Hold','Momentarily throws the target into a state of hold during magic use. The target cannot be affected by any additional hold attacks while the effect lasts.','10','3099'), ('16277','CHANCE','Poison','Momentarily throws the target into a poisoned state during magic use. Effect 5.','10','3104'), ('16278','CHANCE','Medusa','Momentarily throws the target into a petrified state during magic use.','10','3102'), ('16279','PASSIVE','Heal Empower','Increases the power of HP recovery magic when equipped.','10','3246'), ('16280','PASSIVE','Prayer','Increases the effect of HP recovery magic when equipped.','10','3238'), ('16281','PASSIVE','Empower','Increases M. Atk. when equipped.','10','3241'), ('16282','PASSIVE','Magic Barrier','Increases M. Def. when equipped.','10','3245'), ('16283','PASSIVE','Might','Increases P. Atk. when equipped','10','3240'), ('16284','PASSIVE','Shield','Increases P. Def. when equipped.','10','3244'), ('16285','PASSIVE','Duel Might','Increases PVP P. Atk. when equipped.','10','3243'), ('16286','PASSIVE','Weight Limit','Increases the weapon weight limit by 2 times when equipped.','10','3251'), ('16287','ACTIVE','Refresh','Temporarily decreases the re-use times for all skills.','3','3202'), ('16288','ACTIVE','Clarity','Temporarily decreases the MP consumption rates for all skills.','3','3164'), ('16289','ACTIVE','Focus','Increases the chance of a critical attack temporarily.','10','3141'), ('16290','ACTIVE','Reflect Damage','Allows you to reflect some of the damage you incurred back to the enemy for a certain amount of time. Excludes damage from skill or remote attacks.','3','3204'), ('16291','ACTIVE','Doom','Temporarily blocks all of the target\\'s physical/magic skills.','10','3198'), ('16292','ACTIVE','Recall','Teleports the caster to a village. Cannot be used in special areas, such as the GM Consultation Room.','1','3147'), ('16293','ACTIVE','Lesser Celestial Shield','Bestows temporary invincibility.','1','3158'), ('16294','ACTIVE','Wild Magic','Increases the critical attack rate of magic attacks temporarily.','10','3142'), ('16295','ACTIVE','Party Recall','Teleports party members to a village. Cannot be used in a specially designated place such as the GM Consultation Service.','2','3205'), ('16296','ACTIVE','Silence','Temporarily blocks the target\\'s magic skills.','10','3197'), ('16297','ACTIVE','Skill Refresh','Temporarily decreases the re-use time for physical skills.','3','3199'), ('16298','ACTIVE','Skill Clarity','Temporarily decreases the MP consumption rate for physical skills.','3','3161'), ('16299','ACTIVE','Music Refresh','Temporarily decreases the re-use time for song/dance skills.','3','3201'), ('16300','ACTIVE','Music Clarity','Temporarily decreases the MP consumption rate for song/dance skills.','3','3163'), ('16301','ACTIVE','Spell Refresh','Temporarily decreases the re-use time for magic skills.','3','3200'), ('16302','ACTIVE','Spell Clarity','Temporarily decreases the MP consumption rate for magical skills.','3','3162'), ('16303','ACTIVE','Stealth','Temporarily blocks a monster\\'s pre-emptive attack. Fighting ability significantly decreases while in effect.','3','3159'), ('16304','ACTIVE','Vampiric Rage','Increases the ability to restore some HP from the damage inflicted on an enemy temporarily. Excludes damage by skill or long-range attacks.','10','3148'), ('16305','CHANCE','Doom','Momentarily blocks all of the target\\'s physical and magic skills during a general physical attack.','10','3094'), ('16306','CHANCE','Mana Burn','Burns up a target\\'s MP during an ordinary physical attack. Power 49.','10','3082'), ('16307','CHANCE','Paralyze','Momentarily throws the target into a state of paralysis during an ordinary physical attack.','10','3088'), ('16308','CHANCE','Silence','Momentarily blocks the target\\'s magic skill during a general physical attack.','10','3093'), ('16309','CHANCE','Sleep','Momentarily throws the target into a state of sleep during a physical attack. Additional chance to be put into sleep greatly decreases while the effect lasts.','10','3087'), ('16310','CHANCE','Stun','Momentarily throws the target into a state of shock during an ordinary physical attack.','10','3085'), ('16311','CHANCE','Doom','Momentarily blocks all of the target\\'s physical and magic skills during a critical attack.','10','3122'), ('16312','CHANCE','Mana Burn','Burns up a target\\'s MP during a critical attack. Power 49.','10','3110'), ('16313','CHANCE','Paralyze','Momentarily throws the target into a state of paralysis during a critical attack.','10','3116'), ('16314','CHANCE','Silence','Momentarily blocks the target\\'s magic skill during a critical attack.','10','3121'), ('16315','CHANCE','Sleep','Momentarily throws the target into a state of sleep during a critical attack. Additional chance to be put into sleep greatly decreases while the effect lasts.','10','3115'), ('16316','CHANCE','Stun','Momentarily throws the target into a state of shock during a critical attack.','10','3113'), ('16317','CHANCE','Doom','Momentarily blocks all of the target\\'s physical and magic skills when you are under attack.','10','3237'), ('16318','CHANCE','Fear','Momentarily instills a feeling of fear on the target that causes it to flee when you are under attack.','10','3233'), ('16319','CHANCE','Mana Gain','Increases the recharge recovery rate of MP when under attack.','10','3212'), ('16320','CHANCE','Recharge','Restores your MP by using attack rate.','10','3210'), ('16321','CHANCE','Paralyze','Momentarily paralyzes the target when you are under attack.','10','3231'), ('16322','CHANCE','Silence','Momentarily blocks the target\\'s magic skills when you are under attack.','10','3236'), ('16323','CHANCE','Stun','Momentarily stuns the target when you are under attack.','10','3228'), ('16324','CHANCE','Medusa','Momentarily petrifies the target when you are under attack.','10','3232'), ('16325','CHANCE','Doom','Momentarily blocks all of the target\\'s physical and magic skills during magic use.','10','3107'), ('16326','CHANCE','Mana Burn','Burns up a target\\'s MP during magic use. Power 49.','10','3095'), ('16327','CHANCE','Paralyze','Momentarily throws the target into a state of paralysis during magic use.','10','3101'), ('16328','CHANCE','Silence','Momentarily blocks the target\\'s magic skill during magic use.','10','3106'), ('16329','CHANCE','Sleep','Momentarily throws the target into a state of sleep during magic use. Additional chance to be put into sleep greatly decreases while the effect lasts.','10','3100'), ('16330','CHANCE','Stun','Momentarily throws the target into a state of shock during magic use.','10','3098'), ('16331','PASSIVE','Clarity','Decreases the MP consumption rate for all skills when equipped.','3','3258'), ('16332','PASSIVE','Agility','Increases evasion when equipped.','10','3247'), ('16333','PASSIVE','Focus','Increases critical attack rate when equipped.','10','3249'), ('16334','PASSIVE','Reflect Damage','Increases the ability to reflect some of the damage you incur back to the enemy when equipped. Excludes damage by skill or long-range attacks.','3','3259'), ('16335','PASSIVE','Guidance','Increases accuracy when equipped.','10','3248'), ('16336','PASSIVE','Wild Magic','Increases the critical attack rate of magic attacks when equipped.','10','3250'), ('16337','PASSIVE','Mana Gain','Increases the recharge recovery rate of MP when equipped.','10','3239'), ('16338','PASSIVE','Skill Clarity','Decreases the MP consumption rate for physical skills when equipped.','3','3255'), ('16339','PASSIVE','Music Clarity','Decreases the MP consumption rate for song/dance skills when equipped.','3','3257'), ('16340','PASSIVE','Spell Clarity','Decreases the MP consumption rate for magic skills when equipped.','3','3256')";
	
	private final String SQL_Buffer_buff_list_1 = "INSERT IGNORE INTO `zeus_buffer_buff_list` VALUES ('1','1','buff','26','4','1','Increases Speed by 40 for 15 seconds.','0','0'), ('2','5','cubic','1','10','8','Summons a storm cubic that inflicts damage on the enemy with magic. Consumes 4 Spirit Ore when summoning.','0','0'), ('3','5','cubic','2','22','7','Summons a Vampiric Cubic. A Vampiric Cubic uses magic that absorbs the target\\'s HP and with it regenerates its master\\'s HP. Consumes 5 Spirit Ore.','0','0'), ('4','5','cubic','3','33','8','Summons Phantom Cubic. A Phantom Cubic uses magic that decreases the target\\'s P. Atk., P. Def., and Atk. Spd. Requires 2 Spirit Ore.','0','0'), ('5','5','cubic','4','67','7','Summons a Life Cubic. The Life Cubic uses magic to regenerate HP to its owner and party members. Requires 5 Spirit Ore.','0','0'), ('6','1','buff','6','77','1','Increases P. Atk. by 8% for 20 minutes.','0','0'), ('7','1','buff','9','78','1','Increases P. Atk. by 20% for 1 minute.','0','0'), ('8','1','buff','31','82','1','Increases P. Def. by 7% and decreases Evasion by 2 for 5 minutes.','0','0'), ('9','1','buff','30','91','1','Increases P. Def. by 8% for 20 minutes.','0','0'), ('10','2','resist','2','112','1','Increases Resistance to bow attacks by 16% and Resistance to crossbow attacks by 8% for 20 minutes.','0','0'), ('11','1','buff','40','121','1','Regenerates 10% of the user\\'s original HP and increases the user\\'s Max HP by 10% for 10 minutes.','0','0'), ('12','9','others','15','123','1','Increases the user\\'s M. Def. by 15% for 20 minutes.','0','0'), ('13','1','buff','15','131','1','Decreases user\\'s P. Def. by 10% and increases Accuracy by 6 for 5 minutes.','0','0'), ('14','1','buff','24','230','1','Increases Speed by 22 for 20 minutes.','0','0'), ('15','7','song','6','264','1','Increases P. Def. of all party members by 25% for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','4','1'), ('16','7','song','8','265','1','Increases HP Regeneration of all party members by 20% for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','0','1'), ('17','7','song','2','266','1','Increases Evasion of all party members by 3 for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','4','1'), ('18','7','song','5','267','1','Increases M. Def. of all party members by 30% for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','0','1'), ('19','7','song','4','268','1','Increases Speed of all party members by 20 for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','4','1'), ('20','7','song','1','269','1','Increases Critical Rate of all party members by 100% for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','2','1'), ('21','7','song','16','270','1','Increases all party members\\'s Resistance to Dark by 20 for 2 minutes. Increases MP consumption when singing while song/dance effect lasts.','0','1'), ('22','6','dance','5','271','1','Increases P. Atk. of all party members by 12% for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','2','1'), ('23','6','dance','7','272','1','Increases Accuracy of all party members by 4 for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('24','6','dance','2','273','1','Increases M. Atk. of all party members by 20% for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','1','1'), ('25','6','dance','6','274','1','Increases Critical Damage of all party members by 35% for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','2','1'), ('26','6','dance','4','275','1','Increases Atk. Spd. of all party members by 15% for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','2','1'), ('27','6','dance','1','276','1','Decreases all party members\\'s magic cancel damage by 40 and increases Casting Spd. by 30% for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','1','1'), ('28','6','dance','11','277','1','Increases holy P. Atk. of all party members by 20 for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('29','5','cubic','5','278','6','Summons a Viper Cubic. The Viper Cubic uses magic that poisons a targeted enemy. Consumes 2 Spirit Ore when summoning.','0','0'), ('30','2','resist','12','287','1','Increases Resistance to Paralysis, Hold, Sleep, Stun and buff-canceling attacks by 40% for 1 minute.','0','0'), ('31','9','others','24','297','1','Increases dualsword weapon Atk. Spd. by 8% for 1 minute and ordinary/skill attack damage by 5% during PvP.','0','0'), ('32','1','buff','38','303','1','Increases the user\\'s Max MP by 10% for 20 minutes.','0','0'), ('33','7','song','10','304','1','Increases Max HP of all party members by 30% for 2 minutes. Additionally increases MP consumption when singing while song/dance is in effect.','4','1'), ('34','7','song','3','305','1','For 2 minutes, gives a party member the ability to transfer 20% of received standard short-range damage back to the enemy. Additionally increases MP consumption when singing while song/dance is in effect.','0','1'), ('35','7','song','14','306','1','Increases party members\\'s Resistance to Fire by 30 for 2 minutes. Additionally increases MP consumption when singing while song/dance is in effect.','0','1'), ('36','6','dance','12','307','1','Increases party members\\'s Resistance to Water by 30 for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('37','7','song','15','308','1','Increases party members\\'s Resistance to Wind by 30 for 2 minutes. Additionally increases MP consumption when singing while song/dance is in effect.','0','1'), ('38','6','dance','13','309','1','Increases party members\\'s Resistance to Earth by 30 for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('39','6','dance','8','310','1','For 2 minutes, gives all party members the ability to recover as HP 8% of any standard melee damage inflicted on the enemy. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','2','1'), ('40','6','dance','10','311','1','For 2 minutes, gives all party members the ability to decrease by 30 any environment-related damage received. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('41','7','song','11','349','1','For 2 minutes, decreases all party members\\'s physical/magic skill MP consumption by 5% and re-use time by 20%. Additionally increases MP consumption when singing while song/dance is in effect.','0','1'), ('42','7','song','7','363','1','For 2 minutes, increases all party members\\'s MP Recovery Bonus by 20%, and decreases magic skill use MP consumption by 10%. Additionally increases MP consumption when singing while song/dance is in effect.','0','1'), ('43','7','song','12','364','1','For 2 minutes, decreases all party members\\'s MP consumption by 20% and reuse time by 10% for physical/sing/dance skill use. The MP consumption increases additionally when singing while sing/dance effect lasts.','0','1'), ('44','6','dance','3','365','1','For 2 minutes, increases all party members\\'s magic damage by 100%. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('45','6','dance','16','366','1','For 2 minutes, decreases all party members\\'s Speed by 50% and prevents them from being pre-emptively attacked by monsters. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('46','8','special','3','395','1','For 30 seconds, increases P. Def. by 5400, M. Def. by 4050, Resistance to buff-canceling attacks by 80, and Speed by 5. Consumes 40 Soul Ore.','0','0'), ('47','8','special','14','396','1','For 2 minutes, increases the user\\'s Accuracy by 8, P. Atk. by 500, M. Atk. by 500, Atk. Spd. by 100, Casting Spd. by 100, Speed by 20, Resistance to buff-canceling attacks by 80, and the effect of received HP recovery magic by 100%. Bestows complete Resistance to de-buff attacks. Decreases P. Def. by 25%, M. Def. by 25%, and Evasion by 8. Consumes 40 Soul Ore.','0','0'), ('48','1','buff','43','415','1','Decreases physical skill MP consumption by 10% for 20 minutes.','0','0'), ('49','1','buff','44','416','1','Decreases physical skill re-use time by 10% for 20 minutes.','0','0'), ('50','1','buff','20','439','1','For 5 minutes, returns damage from close range physical attacks and skills back to the opponent at a fixed rate.','0','0'), ('51','5','cubic','6','449','4','Summons Attractive Cubic. Attractive Cubic can continuously use hate and hold magic on the enemy. Requires 8 Spirit Ore.','0','0'), ('52','1','buff','12','482','1','For 1 minute 30 seconds, increases the user\\'s Critical Rate by 20% and Critical Damage by 25%. Decreases P. Def. by 5%.','0','0'), ('53','7','song','17','529','1','Increases all party members\\'s Resistance to Fire, Water, Wind and Earth attacks by 30 for 2 minutes. Additionally increases MP consumption when singing while song/dance is in effect.','0','1'), ('54','6','dance','14','530','1','Increases all party members\\'s Resistance to Holy or Dark attacks by 30 for 2 minutes. Additionally increases MP consumption when dancing while song/dance is in effect. Requires a dualsword.','0','1'), ('55','1','buff','5','761','1','One sows a seed of wrath himself. There is a chance that the Seed of Wrath will grow when being attacked. P. Atk. increases with growth.','0','0'), ('56','7','song','13','764','1','For 2 minutes, increases a party member\\'s Resistance to bows by 10 and Resistance to crossbows by 5. Increases the additional MP consumption when singing while sing/dance effect lasts.','0','1'), ('57','6','dance','9','765','1','For 15 seconds, increases a party member\\'s Resistance to bows by 45 and Resistance to crossbows by 25. Decreases M. Atk. by 99%. Additionally increases MP consumption when singing while song/dance is in effect. Requires a dualsword.','0','0'), ('58','5','cubic','7','779','1','Summons Smart Cubic. Cancels all the bad abnormal conditions which were cast on the master, and uses useful skills additionally. Consumes 38 Spirit Ore when summoning.','0','0'), ('59','9','others','4','825','1','Sharpens a bladed weapon to increase P. Atk. by 5% and Critical Rate by 20% for 20 minutes.','0','0'), ('60','9','others','5','826','1','Adds a spike to a blunt weapon to increase P. Atk. by 5% and its weight for shock attacks by 8% for 20 minute(s).','0','0'), ('61','9','others','6','827','1','Enhances the string of a bow or crossbow to increase P. Atk. by 5% and range by 100 for 20 minute(s).','0','0'), ('62','9','others','27','828','1','Enhances the armor surface to increase P. Def. by 10% for 20 minute(s).','0','0'), ('63','9','others','16','829','1','Tans armor to increase P. Def. by 5% and Evasion by 2 for 20 minutes. Works only on light armor users and can\\'t be used on pets.','0','0'), ('64','9','others','17','830','1','Embroiders a robe to increase P. Def. by 5% and MP Recovery Bonus by 2 for 20 minutes.','0','0'), ('65','9','others','18','834','1','Through a blood contract, increases party members\\'s maximum HP by 10% and HP Recovery Bonus by 10 for 2 minutes.','0','0'), ('66','2','resist','1','913','1','Increases Resistance to magic attacks by 100% for 20 minutes.','0','0'), ('67','7','song','9','914','1','Provides an 80% chance of removing a party member\\'s de-buffs. And for the next 2 minutes, increases Resistance to de-buff attacks by 30% and increases the power of received HP recovery magic by 30%.','0','0'), ('68','6','dance','15','915','1','Decreases a party member\\'s P. Def., M. Def. and Evasion, and increases their P. Atk., M. Atk., Atk. Spd., Casting Spd. and Speed for 2 minutes.','0','0'), ('69','1','buff','23','916','1','For 8 seconds, transfers magical damage back to the enemy caster.','0','0'), ('70','9','others','26','917','1','For 30 seconds, you call upon your hidden reserves to increase your skills power and ordinary attack damage by 30% during PvP.','0','0'), ('71','1','buff','46','982','1','Increases party member\\'s P. Atk. by 3% and Atk. Spd. by 3% for 20 minutes.','0','1'), ('72','4','chant','1','1002','3','Increases Casting Spd. of all party members by 15% for 20 minutes.','0','1'), ('73','4','chant','5','1003','3','Increases P. Atk. of nearby clan members by 8% for 20 minutes.','0','0'), ('74','4','chant','2','1004','3','Increases Casting Spd. of nearby clan members by 15% for 20 minutes.','0','0'), ('75','4','chant','18','1005','3','Increases P. Def. of nearby clan members by 8% for 20 minutes.','0','0'), ('76','4','chant','16','1006','3','Increases M. Def. of all party members by 15% for 20 minutes.','0','1'), ('77','4','chant','6','1007','3','Increases P. Atk. of all party members by 8% for 20 minutes.','0','1'), ('78','4','chant','17','1008','3','Increases M. Def. of nearby clan members by 15% for 20 minutes.','0','0'), ('79','4','chant','19','1009','3','Increases P. Def. of all party members by 8% for 20 minutes.','0','1'), ('80','4','chant','20','1010','3','Increases P. Def. by 8% for 20 minutes.','0','1'), ('81','2','resist','3','1032','3','Increases Resistance to Bleed attacks by 30% for 20 minutes.','0','1'), ('82','2','resist','11','1033','3','Increases Resistance to Poison by 30% for 20 minutes.','0','1'), ('83','1','buff','53','1035','4','Increases Resistance to Hold/Sleep/Mental attaks by 20 for 20 minutes.','1','1'), ('84','1','buff','28','1036','2','Increases M. Def. by 23% for 20 minutes.','2','1'), ('85','1','buff','32','1040','3','Increases P. Def. by 8% for 20 minutes.','4','1'), ('86','1','buff','55','1043','1','Increases a party member\\'s holy P. Atk. by 20 for 20 minutes.','0','1'), ('87','1','buff','35','1044','3','Increases HP Regeneration by 10% for 20 minutes.','0','1'), ('88','1','buff','41','1045','6','Increases Max HP by 10% for 20 minutes.','4','1'), ('89','1','buff','33','1047','4','Increases the user\\'s MP Recovery Bonus by 1.72 for 20 minutes. Consumes 7 Spirit Ore.','0','1'), ('90','1','buff','39','1048','6','Increases Max MP by 10% for 20 minutes.','1','1'), ('91','1','buff','2','1059','3','Increases M. Atk. by 55% for 20 minutes.','0','1'), ('92','1','buff','45','1062','2','Decreases a party member\\'s P. Def. by 5%, M. Def. by 10% and Evasion by 2, and increases P. Atk. by 5%, M. Atk. by 10%, Atk. Spd. by 5%, Casting Spd. by 5% and Speed by 5 for 20 minutes.','4','1'), ('93','1','buff','8','1068','3','Increases P. Atk. by 8% for 20 minutes.','2','1'), ('94','1','buff','17','1073','1','Increases lung capacity by 400% for 20 minutes.','0','0'), ('95','1','buff','11','1077','3','Increases Critical Rate by 20% for 20 minutes.','2','1'), ('96','1','buff','47','1078','6','Decreases magic cancel damage by 18 for 20 minutes.','0','1'), ('97','1','buff','1','1085','3','Increases Casting Spd. by 15% for 20 minutes.','1','1'), ('98','1','buff','4','1086','2','Increases Atk. Spd. by 15% for 20 minutes.','2','1'), ('99','1','buff','16','1087','3','Increases Evasion by 2 for 20 minutes.','0','1'), ('100','2','resist','4','1182','3','Increases Resistance to Water by 10 for 20 minutes.','0','1')";
	private final String SQL_Buffer_buff_list_2 = "INSERT IGNORE INTO `zeus_buffer_buff_list` VALUES ('101','2','resist','5','1189','3','Increases Resistance to Wind by 10 for 20 minutes.','0','1'), ('102','2','resist','6','1191','3','Increases Resistance to Fire by 10 for 20 minutes.','0','1'), ('103','1','buff','25','1204','2','Increases Speed by 20 for 20 minutes.','0','1'), ('104','1','buff','21','1232','3','For 20 minutes, transfers 10% of the target\\'s received standard short-range damage back to the enemy.','0','0'), ('105','1','buff','22','1238','3','For 20 minutes, transfers 10% of received standard short-range damage back to the enemy.','0','0'), ('106','1','buff','14','1240','3','Increases Accuracy by 2 for 20 minutes.','4','1'), ('107','1','buff','13','1242','3','Increases Critical Damage by 25% for 20 minutes.','2','1'), ('108','1','buff','18','1243','6','Increases Shield Defense by 5% for 20 minutes.','0','1'), ('109','4','chant','9','1249','3','Increases Accuracy of nearby clan members by 2 for 20 minutes.','0','0'), ('110','4','chant','13','1250','3','Increases Shield Defense of nearby clan members by 30% for 20 minutes. Effect 1.','0','0'), ('111','4','chant','3','1251','2','Increases Atk. Spd. of all party members by 15% for 20 minutes.','0','1'), ('112','4','chant','11','1252','3','Increases Evasion of all party members by 2 for 20 minutes.','0','1'), ('113','4','chant','8','1253','3','Increases Critical Damage of all party members by 25% for 20 minutes.','0','1'), ('114','1','buff','48','1257','3','Increases the weight penalty interval by 3000 for 20 minutes.','0','1'), ('115','2','resist','10','1259','4','Increases Resistance to Stun attacks by 15% for 20 minutes.','0','1'), ('116','4','chant','12','1260','3','Increases Evasion of nearby clan members by 2 for 20 minutes.','0','0'), ('117','4','chant','30','1261','2','Decreases nearby clan members\\'s P. Def. by 5%, M. Def. by 10%, and Evasion by 2, and increases P. Atk. by 5%, M. Atk. by 10%, Atk. Spd. by 5%, Casting Spd. by 5%, and Speed by 5 for 20 minutes.','0','0'), ('118','1','buff','37','1268','4','For 20 minutes, 6% of the standard melee damage inflicted on the enemy is recovered as HP.','0','1'), ('119','5','cubic','8','1279','9','Summons Binding Cubic. Binding Cubic uses magic that paralyzes a targeted enemy. Requires 4 Spirit Ore.','0','0'), ('120','5','cubic','9','1280','9','Summons Aqua Cubic. Aqua Cubic uses attack magic that inflicts continuous damage to the enemy. Requires 2 Spirit Ore.','0','0'), ('121','5','cubic','10','1281','9','Summons Spark Cubic. Spark Cubic uses magic that stuns a targeted enemy. Requires 4 Spirit Ore.','0','0'), ('122','4','chant','15','1282','2','Increases Speed of nearby clan members by 20 for 20 minutes.','0','0'), ('123','4','chant','14','1284','3','For 20 minutes, gives a party member the ability to transfer 10% of received standard short-range damage back to the enemy.','0','1'), ('124','9','others','7','1285','1','Uses Fire energy to increase user\\'s Fire attack level by 20.','0','0'), ('125','9','others','8','1286','1','Uses Water energy to increase user\\'s Water attack level by 20.','0','0'), ('126','9','others','9','1287','1','Uses Wind energy to increase user\\'s Wind attack level by 20.','0','0'), ('127','1','buff','3','1303','2','For 20 minutes, increases by 1 the damage rate of magic.','0','1'), ('128','1','buff','19','1304','3','Increases Shield Defense by 30% for 20 minutes.','0','0'), ('129','1','buff','36','1307','3','Increases the power of HP recovery magic received by all party members by 8% for 20 minutes.','0','0'), ('130','4','chant','7','1308','3','Increases Critical Rate of all party members by 20% for 20 minutes.','0','1'), ('131','4','chant','10','1309','3','Increases Accuracy of all party members by 2 for 20 minutes.','0','1'), ('132','4','chant','22','1310','4','For 20 minutes, gives all party members the ability to recover as HP 6% of any standard melee damage inflicted on the enemy.','0','1'), ('133','9','others','19','1311','6','Restores HP of all party members by 10% and increases Max HP by 10% for 20 minutes.','0','0'), ('134','8','special','15','1323','1','Maintains target\\'s buff/de-buff condition even following death and resurrection. The Blessing of Noblesse and the Amulet of Luck disappear, however. Consumes 5 Spirit Ore.','0','1'), ('135','1','buff','50','1352','1','For 20 minutes, increases Resistance to Fire, Water, Wind and Earth attacks.','0','1'), ('136','1','buff','49','1353','1','For 20 minutes, increases Resistance to Dark by 30 and Resistance to Holy by 20.','0','1'), ('137','1','buff','54','1354','1','For 20 minutes, increases Resistance to buff-canceling attacks by 30 and Resistance to de-buff attacks by 20.','0','1'), ('138','3','prophecy','2','1355','1','For 5 minutes, a powerful spirit acts to increase the damage caused by the targeted party member\\'s magic damage by 2, MP Recovery Bonus by 20%, P. Atk. by 10%, P. Def. by 20%, Atk. Spd. by 20%, M. Atk. by 20%, M. Def. by 20%, Casting Spd. by 20%, and Resistance to de-buffs by 10%. Decreases Speed by 20% and MP consumption for skill use by 5%. Consumes 10 Spirit Ore.','0','1'), ('139','3','prophecy','1','1356','1','For 5 minutes, a powerful spirit acts to increase a party member\\'s Max MP by 20%, HP Recovery Bonus by 20%, magic damage by 2, Critical Damages by 20%, P. Atk. by 10%, P. Def. by 20%, Atk. Spd. by 20%, M. Atk. by 20%, M. Def. by 20%, Casting Spd. by 20%, and Resistance to de-buffs by 10%. Decreases Speed by 20%. Consumes 10 Spirit Ore.','0','1'), ('140','3','prophecy','3','1357','1','For 5 minutes, a powerful spirit acts to increase a party member\\'s Max HP by 20%, Critical Rate by 20%, magic damage by 20%, P. Atk. by 10%, P. Def. by 20%, Atk. Spd. by 20%, M. Atk. by 20%, M. Def. by 20%, Casting Spd. by 20%, and Resistance to de-buff by 10%. Decreases Speed by 20%. Bestows the ability to recover as HP 5% of the standard melee damage inflicted on the enemy. Consumes 10 Spirit Ore.','0','1'), ('141','4','chant','25','1362','1','For 20 minutes, increases all party members\\'s Resistance to buff-canceling attacks by 30 and Resistance to de-buff attacks by 20.','0','1'), ('142','3','prophecy','5','1363','1','Recovers all party members\\'s HP by 20%, and for 5 minutes, receives help from a great spirit to increase Max HP by 20%, magic damage Critical Damage by 2, Critical Damage by 20%, P. Atk. by 10%, P. Def. by 20%, Atk. Spd. by 20%, M. Atk. by 20%, M. Def. by 20%, Casting Spd. by 20%, Resistance to de-buffs by 10%, and Accuracy by 4. Decreases Speed by 20%. Consumes 40 Spirit Ore.','0','1'), ('143','4','chant','27','1364','1','For 20 minutes, increases nearby clan members\\'s Accuracy by 4 and decreases the rate of being hit by a critical attack by 30%.','0','0'), ('144','4','chant','29','1365','1','For 20 minutes, increases nearby clan members\\'s M. Atk. by 75% and M. Def. by 30%.','0','0'), ('145','1','buff','51','1388','3','Increases P. Atk. by 4% for 20 minutes. Consumes 1 Spirit Ore.','0','1'), ('146','1','buff','52','1389','3','Increases P. Def. by 5% for 20 minutes. Consumes 1 Spirit Ore.','0','1'), ('147','4','chant','4','1390','3','Increases P. Atk. of all party members by 4% for 20 minutes. Consumes 4 Spirit Ore.','0','1'), ('148','4','chant','21','1391','3','Increases P. Def. of all party members by 5% for 20 minutes. Consumes 4 Spirit Ore.','0','1'), ('149','2','resist','7','1392','3','Increases Resistance to Holy by 15 for 20 minutes.','0','1'), ('150','2','resist','8','1393','3','Increases Resistance to Dark by 15 for 20 minutes.','0','1'), ('151','1','buff','42','1397','3','For 20 minutes, decreases physical skill MP consumption by 10%, magic skill MP consumption by 4%, and song/dance skill MP consumption by 10%. Consumes 1 Spirit Ore.','0','1'), ('152','2','resist','14','1411','1','Makes one invincible to various debuffs for a certain period of time. Usable only on party members.','0','0'), ('153','4','chant','28','1413','1','For 5 minutes, a powerful spirit acts to increase Max MP of all party members by 15%, MP Recovery Bonus by 1.5 when equipped with light or heavy armor, MP Recovery Bonus by 4 when equipped with a robe, M. Def. by 30%, M. Atk. by 30%, Casting Spd. by 20%, Resistance to Fire, Water, Wind and Earth damage by 10, Resistance to de-buff attacks by 25, and Resistance to buff-canceling attacks by 40. Consumes 40 Spirit Ore.','0','1'), ('154','4','chant','31','1414','1','For 5 minutes, a powerful spirit acts to increase nearby clan members\\'s Max CP by 20%, CP recovery bonus by 20%, Max MP by 20%, Critical Rate by 20%, the power of Prominent Damage through magic damage by 20%, P. Atk. by 10%, P. Def. by 20%, Atk. Spd. by 20%, M. Atk. by 20%, M. Def. by 20%, Casting Spd. by 20%, and Resistance to de-buff by 10%. Decreases Speed by 20%. Consumes 40 Spirit Ore.','0','0'), ('155','4','chant','26','1415','1','For 5 minutes, increases nearby clan members\\'s Resistance to buff-canceling attacks by 30% and Resistance to de-buff attacks by 20%.','0','0'), ('156','4','chant','23','1416','1','Regenerates nearby clan members\\'s CP by 800 and increases Max CP by 800 for 5 minutes. Consumes 20 Spirit Ore.','0','0'), ('157','1','buff','7','1432','1','Increases the user\\'s P. Atk. by 8% for 20 minutes.','0','0'), ('158','2','resist','9','1442','1','Increases the target\\'s Resistance to Dark by 15 for 20 minutes.','0','1'), ('159','9','others','14','1443','1','Increases the target\\'s Dark attack level by 20 for 20 minutes. Can be used on party members.','0','0'), ('160','9','others','2','1444','1','When using a Kamael-exclusive weapon, P. Atk., M. Atk. and lethal attack rate are increased by 10% for 3 minutes. May be applied to Kamael party members.','0','0'), ('161','9','others','1','1457','1','For 20 minutes, increases the user\\'s magic MP consumption by 35% and M. Atk. by 25%.','0','0'), ('162','1','buff','34','1460','1','For 20 minutes, increases the recharge power received by the target by 85.','0','0'), ('163','4','chant','24','1461','1','For 20 minutes, decreases Critical Damage received by a party member by 30%.','0','1'), ('164','9','others','10','1463','1','Adds Fire damage to a P. Atk.','0','0'), ('165','9','others','11','1464','1','Adds Water damage to a P. Atk.','0','0'), ('166','9','others','12','1465','1','Adds Wind damage to a P. Atk.','0','0'), ('167','9','others','13','1466','1','Bestows the Earth elemental to a P. Atk.','0','0'), ('168','1','buff','27','1470','1','Increases M. Def. of all party members by 3000 for 30 seconds.','0','0'), ('169','8','special','2','1476','1','For 15 seconds, awakens a party member\\'s destructive instincts and increases P. Atk., Critical Rate and Critical Damage by 30%.','0','0'), ('170','1','buff','29','1478','1','Stirs up the defense instinct to increase P. Def. by 1200 and M. Def. by 900 for 15 seconds.','0','0'), ('171','9','others','20','1492','1','Engulfs the user with a protective coat of fire. For 20 minutes, increases Resistance to fire attacks by 60 and causes burn damage on the attacking enemy.','0','0'), ('172','9','others','21','1493','1','Engulfs the user with a protective glacier barrier. For 20 minutes, increases Resistance to water attacks by 60 and slows down the attacking enemy.','0','0'), ('173','9','others','22','1494','1','Engulfs the body with a storm barrier. For 20 minutes, increases Resistance to wind attacks by 60 and slows down the attacking enemy\\'s Atk. Spd.','0','0'), ('174','0','improved','1','1499','1','Combines P. Atk. increase and P. Def. increase to have more advanced combat power increase effect. For 40 minutes, increases P. Atk. by 15% and P. Def. by 15%.','0','1'), ('175','0','improved','13','1500','1','Increases both M. Atk. and M. Def. to have more advanced magic ability increase effect. For 40 minutes, increases M. Atk. by 75% and M. Def. by 30%.','0','1'), ('176','0','improved','9','1501','1','Combines maximum HP increase and Max MP increase to have more advanced mental and physical power. For 40 minutes, increases Max HP by 35% and Max MP by 35%.','0','1'), ('177','0','improved','4','1502','1','Combines Critical Rate increase and Critical Damage increase to have more advanced critical increase effect. For 40 minutes, increases Critical Rate by 30% and Critical Damage by 35%.','0','1'), ('178','0','improved','6','1503','1','Combines shield Def. rate increase and Shield Defense. increase to have more advanced shield ability increase effect. For 40 minutes, increases Shield Defense by 30% and Shield Defense by 50%.','0','1'), ('179','0','improved','12','1504','1','Combines Spd. increase and Evasion increase to have more advanced movement increase effect. For 40 minutes, increases Speed by 33 and Evasion by 4.','0','1'), ('180','9','others','23','1514','1','Spreads the soul\\'s defensive barrier to increase your Resistance to bows by 10, Resistance to crossbows by 6, and M. Def. by 100% for 10 seconds.','0','0'), ('181','0','improved','2','1517','1','Combines party members\\'s P. Atk. increase and P. Def. increase to have more advanced combat power increase effect. For 40 minutes, increases P. Atk. by 15% and P. Def. by 15%.','0','0'), ('182','0','improved','5','1518','1','Combines party members\\'s Critical Rate increase and Critical Damage increase to have more advanced critical increase effect. Increases Critical Rate by 30% and Critical Damage by 35% for 40 minutes.','0','0'), ('183','0','improved','10','1519','1','Combines party members\\'s general attack damage absorption and Atk. Spd. increase to have a more advanced blood awakening effect. For 40 minutes, increases Atk. Spd. by 33% and bestows the ability to recover as HP 9% of the standard melee damage inflicted on the enemy.','0','1'), ('184','8','special','1','1532','1','User receives mystical enlightenment for 20 seconds, increasing M. Atk. by 40%, Casting Spd. by 50% and magic Critical Rate by 50%, and decreasing MP consumption by 90%.','0','0'), ('185','0','improved','11','1535','1','Combines a party member\\'s Speed increase and Evasion increase effects for a more advanced movement increase effect. For 40 minutes, increases Speed by 33 and Evasion by 4.','0','0'), ('186','0','improved','3','1536','1','Combines a party/clan member\\'s P. Atk. increase and P. Def. increase to have more advanced combat power increase effect. For 40 minutes, increases P. Atk. by 15% and P. Def. by 15%.','0','0'), ('187','0','improved','7','1537','1','Combines a party/clan member\\'s Critical Rate increase and Critical Damage increase effects for a more advanced critical increase effect. For 40 minutes, increases Critical Rate by 30% and Critical Damage by 35%.','0','0'), ('188','0','improved','8','1538','1','Combines a party/clan member\\'s maximum HP increase and Max MP increase effects for more advanced mental and physical power. For 40 minutes, increases Max HP by 35% and Max MP by 35%.','0','0'), ('189','8','special','4','1542','1','For 20 minutes, increases the target\\'s P. Def. against Critical by 30%. When the target receives an attack above a certain amount of damage, Critical Damage of General Short-Range P. Atk. is increased for 8 seconds.','0','0'), ('190','9','others','3','1547','1','For 20 minutes, increases the user\\'s P. Atk. by 3%, M. Atk. by 3%, skill P. Atk. by 5%, Atk. Spd. by 2%, Casting Spd. by 1%, and Critical Rate by 5%. The master\\'s abilities are extended to the servitor.','0','0'), ('191','8','special','5','2076','1','Increase lung capacity. Effect 1.','0','0'), ('192','8','special','6','4551','1','Infected with Hot Springs Rheumatism. While afflicted, your critical attack success rate is increased. Effect 1.','0','0'), ('193','8','special','7','4552','1','Infected with Hot Springs Cholera. While afflicted, your Accuracy is increased. Effect  1.','0','0'), ('194','8','special','8','4553','1','Infected with Hot Springs Flu. While afflicted, your Atk. Spd. is increased. Effect 1.','0','0'), ('195','8','special','9','4554','1','Infected with Hot Springs Malaria. While afflicted, your Casting Spd. is increased. Effect 1.','0','0'), ('196','8','special','10','4699','13','+30% Critical Rate, +25% Critical Pow.','0','1'), ('197','8','special','11','4700','13','+10% P.Atk., +3 Accuracy.','0','1'), ('198','8','special','12','4702','13','Buff magic used by the Unicorn Seraphim. Party members\\'s MP regeneration bonus temporarily increased. Effect 1.','0','1'), ('199','8','special','13','4703','13','u,Unicorn Seraphim\\'s buff magic temporarily reduces party members\\'s magic skill recovery time. 1.','0','1'), ('200','1','buff','10','6049','1','Utters a battle cry that increases one\\'s own P. Atk., P. Def. and M. Def.','0','0')";

	private final String SQL_Buff_for_aio_1 = "INSERT IGNORE INTO `zeus_buff_for_aio` VALUES ('Song of Champion','364','1','1'), ('Shield','1040','3','3'), ('Holy Weapon','1043','1','1'), ('Regeneration','1044','3','3'), ('Magnus\\'s Chant','1413','1','1'), ('Magician\\'s Movement','118','1','1'), ('Cubic Mastery','143','2','2'), ('Dual Weapon Mastery','144','37','37'), ('Anti Magic','146','45','45'), ('Spellcraft','163','1','1'), ('Lucky','194','1','1'), ('Mana Recovery','214','1','1'), ('Fast Spell Casting','228','3','3'), ('Fast Mana Recovery','229','7','7'), ('Light Armor Mastery','236','41','41'), ('Song of Earth','264','1','1'), ('Song of Life','265','1','1'), ('Song of Water','266','1','1'), ('Song of Warding','267','1','1'), ('Song of Wind','268','1','1'), ('Song of Hunter','269','1','1'), ('Song of Invocation','270','1','1'), ('Dance of the Warrior','271','1','1'), ('Dance of Inspiration','272','1','1'), ('Dance of the Mystic','273','1','1'), ('Dance of Fire','274','1','1'), ('Dance of Fury','275','1','1'), ('Dance of Concentration','276','1','1'), ('Dance of Light','277','1','1'), ('Song of Vitality','304','1','1'), ('Song of Vengeance','305','1','1'), ('Song of Flame Guard','306','1','1'), ('Dance of Aqua Guard','307','1','1'), ('Song of Storm Guard','308','1','1'), ('Dance of Earth Guard','309','1','1'), ('Dance of the Vampire','310','1','1'), ('Dance of Protection','311','1','1'), ('Wisdom','328','1','1'), ('Song of Renewal','349','1','1'), ('Song of Meditation','363','1','1'), ('Song of Champion','364','1','1'), ('Dance of Siren','365','1','1'), ('Summon Lore','435','1','1'), ('Divine Lore','436','1','1'), ('Song of Elemental','529','1','1'), ('Dance of Alignment','530','1','1'), ('Flame Chant','1002','3','3'), ('Pa\\'agrian Gift','1003','1','1'), ('Blessings of Pa\\'agrio','1005','1','1'), ('Chant of Fire','1006','3','3'), ('Chant of Battle','1007','3','3'), ('Chant of Shielding','1009','3','3'), ('Soul Shield','1010','3','3'), ('Invigor','1032','330','330'), ('Resist Poison','1033','315','315'), ('Mental Shield','1035','4','4'), ('Magic Barrier','1036','2','2'), ('Shield','1040','3','3'), ('Holy Weapon','1043','1','1'), ('Regeneration','1044','3','3'), ('Bless the Body','1045','6','6'), ('Bless the Soul','1048','6','6'), ('Empower','1059','3','3'), ('Berserker Spirit','1062','2','2'), ('Focus','1077','3','3'), ('Concentration','1078','6','6'), ('Acumen','1085','3','3'), ('Haste','1086','2','2'), ('Agility','1087','3','3'), ('Resist Aqua','1182','330','330'), ('Resist Wind','1189','330','330'), ('Resist Fire','1191','330','330'), ('Resist Earth','1548','330','330'), ('Resist Holy','1392','130','130'), ('Resist Dark','1393','130','130'), ('Wind Walk','1204','2','2'), ('Self Heal','1216','1','1'), ('Guidance','1240','3','3'), ('Death Whisper','1242','3','3'), ('Bless Shield','1243','6','6'), ('Chant of Fury','1251','2','2'), ('Chant of Evasion','1252','3','3'), ('Chant of Rage','1253','3','3'), ('Resist Shock','1259','330','330'), ('Vampiric Rage','1268','4','4'), ('Chant of Revenge','1284','3','3'), ('Wild Magic','1303','2','2'), ('Advanced Block','1304','3','3'), ('Chant of Predator','1308','3','3'), ('Chant of Eagle','1309','3','3'), ('Chant of Vampire','1310','4','4'), ('Mass Summon Storm Cubic','1328','8','8'), ('Mass Summon Aqua Cubic','1329','9','9'), ('Summon Feline Queen','1331','10','10'), ('Summon Seraphim the Unicorn','1332','10','10'), ('Elemental Protection','1352','1','1'), ('Divine Protection','1353','1','1'), ('Arcane Protection','1354','1','1'), ('Prophecy of Water','1355','315','315'), ('Prophecy of Fire','1356','315','315')";
	private final String SQL_Buff_for_aio_2 = "INSERT IGNORE INTO `zeus_buff_for_aio` VALUES ('Prophecy of Wind','1357','315','315'), ('Chant of Spirit','1362','1','1'), ('Chant of Victory','1363','315','315'), ('Greater Might','1388','3','3'), ('Greater Shield','1389','3','3'), ('War Chant','1390','3','3'), ('Earth Chant','1391','3','3'), ('Erase','1395','10','10'), ('Clarity','1397','230','230'), ('Summon Friend','1403','1','1'), ('Cleanse','1409','1','1'), ('Salvation','1410','1','1'), ('Magnus\\'s Chant','1413','1','1'), ('Gate Chant','1429','1','1'), ('Invocation','1430','5','5'), ('Mana Gain','1460','1','1'), ('Chant of Protection','1461','1','1'), ('Steal Mana','1526','3','3'), ('Song of Wind Storm','764','1','1'), ('Dance of Blade Storm','765','1','1'), ('Magician\\'s Will','945','1','1'), ('Improved Combat','1499','1','1'), ('Improved Condition','1501','1','1'), ('Blessing of Eva','1506','1','1'), ('Counter Critical','1542','1','1'), ('The Wisdom of Pa\\'agrio','1004','3','3'), ('The Glory of Pa\\'agrio','1008','3','3'), ('The Vision of Pa\\'agrio','1249','3','3'), ('Shield of Pa\\'agrio','1250','3','3'), ('Condition of Pa\\'agrio','1538','3','3'), ('Chant of Movement','1535','3','3'), ('The Tact of Pa\\'agrio','1260','3','3'), ('Rage of Pa\\'agrio','1261','2','2'), ('Pa\\'agrian Haste','1282','2','2'), ('Eye of Pa\\'agrio','1364','1','1'), ('Soul of Pa\\'agrio','1365','1','1'), ('Victory of Pa\\'agrio','1414','1','1'), ('Pa\\'agrio\\'s Emblem','1415','1','1'), ('Pa\\'agrio\\'s Fist','1416','115','115'), ('Improved Movement','1504','1','1'), ('Sharp Edge','825','1','1'), ('Spike','826','1','1'), ('Restring','827','1','1'), ('Case Harden','828','1','1'), ('Hard Tanning','829','1','1'), ('Embroider','830','1','1'), ('Protection from Darkness','1442','1','1'), ('Dark Weapon','1443','130','130'), ('Blazing Skin','1232','330','330'), ('Combat Aura','982','3','3'), ('Improved Magic','1500','1','1'), ('Improved Critical Attack','1502','1','1'), ('Improved Shield Defense','1503','1','1'), ('Chant of Blood Awakening','1519','1','1'), ('Blessing of Vitality','23179','1','1'), ('Super Haste','7029','3','3'), ('Chant of Elements','1549','1','1'), ('Chant of Berserker','1562','2','2'), ('Boost Mana AIO','10003','130','130'), ('Mana Recovery AIO','10004','1','1')";

	private final String SQL_config_1 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('1','ID_NPC','955'), ('2','ID_NPC_CH','956'), ('3','MAX_LISTA_PVP','30'), ('4','MAX_LISTA_PVP_LOG','30'), ('5','DEBUG_CONSOLA_ENTRADAS','false'), ('6','DEBUG_CONSOLA_ENTRADAS_TO_USER','false'), ('7','BTN_SHOW_VOTE','true'), ('8','BTN_SHOW_BUFFER','true'), ('9','BTN_SHOW_TELEPORT','true'), ('10','BTN_SHOW_SHOP','true'), ('11','BTN_SHOW_WAREHOUSE','true'), ('12','BTN_SHOW_AUGMENT','true'), ('13','BTN_SHOW_SUBCLASES','true'), ('14','BTN_SHOW_CLASS_TRANSFER','true'), ('15','BTN_SHOW_CONFIG_PANEL','true'), ('16','BTN_SHOW_DROP_SEARCH','true'), ('17','BTN_SHOW_PVPPK_LIST','true'), ('18','BTN_SHOW_LOG_PELEAS','true'), ('19','BTN_SHOW_CASTLE_MANAGER','true'), ('20','BTN_SHOW_DESAFIO','true'), ('21','BTN_SHOW_SYMBOL_MARKET','true'), ('22','BTN_SHOW_CLANALLY','true'), ('23','BTN_SHOW_PARTYFINDER','true'), ('24','BTN_SHOW_FLAGFINDER','true'), ('25','BTN_SHOW_COLORNAME','true'), ('26','BTN_SHOW_DELEVEL','true'), ('27','BTN_SHOW_REMOVE_ATRIBUTE','true'), ('28','BTN_SHOW_BUG_REPORT','true'), ('29','BTN_SHOW_DONATION','true'), ('30','BTN_SHOW_CAMBIO_NOMBRE_PJ','true'), ('31','BTN_SHOW_CAMBIO_NOMBRE_CLAN','false'), ('32','BTN_SHOW_VARIAS_OPCIONES','true'), ('33','BTN_SHOW_ELEMENT_ENHANCED','false'), ('34','BTN_SHOW_ENCANTAMIENTO_ITEM','true'), ('35','BTN_SHOW_AUGMENT_SPECIAL','false'), ('36','BTN_SHOW_GRAND_BOSS_STATUS','true'), ('37','BTN_SHOW_RAIDBOSS_INFO','true'), ('38','BTN_SHOW_VOTE_CH','true'), ('39','BTN_SHOW_BUFFER_CH','true'), ('40','BTN_SHOW_TELEPORT_CH','true'), ('41','BTN_SHOW_SHOP_CH','true'), ('42','BTN_SHOW_WAREHOUSE_CH','true'), ('43','BTN_SHOW_AUGMENT_CH','false'), ('44','BTN_SHOW_SUBCLASES_CH','true'), ('45','BTN_SHOW_CLASS_TRANSFER_CH','false'), ('46','BTN_SHOW_CONFIG_PANEL_CH','false'), ('47','BTN_SHOW_DROP_SEARCH_CH','false'), ('48','BTN_SHOW_PVPPK_LIST_CH','true'), ('49','BTN_SHOW_LOG_PELEAS_CH','false'), ('50','BTN_SHOW_CASTLE_MANAGER_CH','false'), ('51','BTN_SHOW_DESAFIO_CH','false'), ('52','BTN_SHOW_SYMBOL_MARKET_CH','false'), ('53','BTN_SHOW_CLANALLY_CH','false'), ('54','BTN_SHOW_PARTYFINDER_CH','false'), ('55','BTN_SHOW_FLAGFINDER_CH','false'), ('56','BTN_SHOW_COLORNAME_CH','true'), ('57','BTN_SHOW_DELEVEL_CH','false'), ('58','BTN_SHOW_REMOVE_ATRIBUTE_CH','true'), ('59','BTN_SHOW_BUG_REPORT_CH','true'), ('60','BTN_SHOW_DONATION_CH','true'), ('61','BTN_SHOW_CAMBIO_NOMBRE_PJ_CH','false'), ('62','BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH','false'), ('63','BTN_SHOW_VARIAS_OPCIONES_CH','false'), ('64','BTN_SHOW_ELEMENT_ENHANCED_CH','false'), ('65','BTN_SHOW_ENCANTAMIENTO_ITEM_CH','false'), ('66','BTN_SHOW_AUGMENT_SPECIAL_CH','false'), ('67','BTN_SHOW_GRAND_BOSS_STATUS_CH','true'), ('68','BTN_SHOW_RAIDBOSS_INFO_CH','true'), ('69','VOTO_REWARD_TOPZONE','57,8000000'), ('70','VOTO_REWARD_HOPZONE','3470,5'), ('71','VOTO_REWARD_ACTIVO_TOPZONE','true'), ('72','VOTO_REWARD_ACTIVO_HOPZONE','true'), ('73','VOTO_REWARD_SEG_ESPERA','30'), ('74','VOTO_ITEM_BUFF_ENCHANT_PRICE','3470,2'), ('75','VOTO_WEB_GET_VOTOS','http://p-venta.cl/pv/getvotos.php'),('82','DONA_WEB_SEND_NOTIFICACION','http://p-venta.cl/l2nacion/NacionNPCZEUS.php?TIP_PARAM=REG_DONA_NPC&IDD='), ('83','TELEPORT_PRICE','57,85000'), ('84','FREE_TELEPORT','false'), ('85','DESAFIO_85_PREMIO','3470,10;57,547874'), ('86','DESAFIO_NOBLE_PREMIO','3470,20;57,87878787'), ('87','DESAFIO_NPC_BUSQUEDAS','8,70030,70033'), ('88','DROP_SEARCH_COBRAR_TELEPORT','true'), ('89','DROP_TELEPORT_COST','57,800000'), ('90','DROP_SEARCH_MOSTRAR_LISTA','18'), ('91','PARTY_FINDER_PRICE','57,8000000;3470,1000'), ('92','PARTY_FINDER_GO_LEADER_DEATH','true'), ('93','PARTY_FINDER_GO_LEADER_NOBLE','false'), ('94','PARTY_FINDER_GO_LEADER_FLAGPK','false'), ('95','PARTY_FINDER_CAN_USE_PK','false'), ('96','PARTY_FINDER_CAN_USE_FLAG','true'), ('97','PARTY_FINDER_CAN_USE_LVL','40'), ('98','FLAG_FINDER_PRICE','57,800000;3470,8'), ('99','FLAG_FINDER_CAN_USE_FLAG','true'), ('100','FLAG_FINDER_CAN_USE_PK','false')";
	private final String SQL_config_2 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('101','FLAG_FINDER_CAN_NOBLE','false'), ('102','FLAG_FINDER_LVL','40'), ('103','FLAG_PVP_PK_LVL_MIN','60'), ('104','PINTAR_PRICE','57,8000;3470,300'), ('105','PINTAR_COLORS','0000FF,00FF00,FFFF00,CCEEFF,81DAF5'), ('106','AUGMENT_ITEM_PRICE','57,585858'), ('107','AUGMENT_SPECIAL_PRICE','3470,10'), ('108','AUGMENT_SPECIAL_x_PAGINA','20'), ('109','AUGMENT_SPECIAL_NOBLE','false'), ('110','AUGMENT_SPECIAL_LVL','80'), ('111','ENCHANT_ITEM_PRICE','3470,80'), ('112','ENCHANT_NOBLE','false'), ('113','ENCHANT_LVL','80'), ('114','ENCHANT_MIN_ENCHANT','10'), ('115','ENCHANT_MAX_ENCHANT','18'), ('116','ENCHANT_x_VEZ','3'), ('117','RAIDBOSS_INFO_TELEPORT_PRICE','57,85000;3470,1'), ('118','RAIDBOSS_INFO_LISTA_X_HOJA','20'), ('119','RAIDBOSS_INFO_TELEPORT','true'), ('120','RAIDBOSS_INFO_NOBLE','false'), ('121','RAIDBOSS_INFO_LVL','80'), ('122','OPCIONES_CHAR_SEXO_ITEM_PRICE','3470,2'), ('123','OPCIONES_CHAR_NOBLE_ITEM_PRICE','3470,2;57,80000'), ('124','OPCIONES_CHAR_LVL85_ITEM_PRICE','3470,2'), ('125','OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE','3470,8'), ('126','OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE','57,8788555;3470,10'), ('127','OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE','true'), ('128','OPCIONES_CHAR_CAMBIO_NOMBRE_LVL','40'), ('129','OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL','4'), ('130','ELEMENTAL_ITEM_PRICE','3470,5'), ('131','ELEMENTAL_NOBLE','false'), ('132','ELEMENTAL_LVL','40'), ('133','DELEVEL_PRICE','57,800000'), ('134','DELEVEL_LVL_MAX','50'), ('135','DELEVEL_NOBLE','false'), ('136','BUFFER_ID_ITEM','57'), ('137','BUFFER_CON_KARMA','false'), ('138','BUFFER_LVL_MIN','1'), ('139','BUFFER_TIME_WAIT','10'), ('140','BUFF_GRATIS','False'), ('141','BUFFER_GM_ONLY','False'), ('142','BUFFER_ID_ACCESO_GM','1'), ('143','BUFFER_ID_ACCESO_ADMIN','8'), ('144','BUFFER_SINGLE_BUFF_CHOICE','true'), ('145','BUFFER_SCHEME_SYSTEM','True'), ('146','BUFFER_SCHEMA_X_PLAYER','4'), ('147','BUFFER_IMPROVED_SECTION','true'), ('148','BUFFER_IMPROVED_VALOR','1000'), ('149','BUFFER_BUFF_SECTION','true'), ('150','BUFFER_BUFF_VALOR','1000'), ('151','BUFFER_CHANT_SECTION','true'), ('152','BUFFER_CHANT_VALOR','1000'), ('153','BUFFER_DANCE_SECTION','true'), ('154','BUFFER_DANCE_VALOR','1000'), ('155','BUFFER_SONG_SECTION','True'), ('156','BUFFER_SONG_VALOR','1000'), ('157','BUFFER_RESIST_SECTION','True'), ('158','BUFFER_RESIST_VALOR','1000'), ('159','BUFFER_CUBIC_SECTION','True'), ('160','BUFFER_CUBIC_VALOR','1000'), ('161','BUFFER_PROPHECY_SECTION','True'), ('162','BUFFER_PROHECY_VALOR','1000'), ('163','BUFFER_SPECIAL_SECTION','True'), ('164','BUFFER_SPECIAL_VALOR','1000'), ('165','BUFFER_OTROS_SECTION','True'), ('166','BUFFER_OTROS_VALOR','1000'), ('167','BUFFER_AUTOBUFF','True'), ('168','BUFFER_HEAL','True'), ('169','BUFFER_HEAL_VALOR','1000'), ('170','BUFFER_REMOVE_BUFF','True'), ('171','BUFFER_REMOVE_BUFF_VALOR','1000'), ('172','RATE_EXP_OFF','true'), ('173','LOG_FIGHT_PVP_PK','True'), ('174','ENCHANT_ANNOUCEMENT','4,5,6,7,9,11'), ('175','PVP_PK_PROTECTION_LVL','30'), ('176','ALLOW_BLESSED_ESCAPE_PVP','False'), ('177','PVP_PK_GRAFICAL_EFFECT','True')";
	private final String SQL_config_3 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('241','ANNOUCE_RAID_BOS_STATUS','true'), ('242','RAID_ANNOUCEMENT_DIED','Raid Boss %RAID_NAME% a muerto, Renacera el %DATE%'), ('243','RAID_ANNOUCEMENT_LIFE','Raid Boss %RAID_NAME% a revivido'), ('244','RAID_ANNOUCEMENT_ID_ANNOUCEMENT','9'), ('245','ANNOUCE_TOP_PPVPPK_ENTER','true'), ('246','MENSAJE_ENTRADA_PJ_TOPPVPPK','%CHAR_NAME% Asesino en Serie'), ('247','ANNOUCE_PJ_KARMA','true'), ('248','MENSAJE_ENTRADA_PJ_KARMA','%CHAR_NAME% have %KARMA% karma'), ('249','ANNOUCE_PJ_KARMA_CANTIDAD','200'), ('250','ANNOUCE_CLASS_OPONENT_OLY','true'), ('251','SHOW_MY_STAT','true'), ('252','BTN_SHOW_TRANSFORMACION','true'), ('253','TRANSFORM_NOBLE','true'), ('254','TRANSFORM_LVL','20'), ('255','TRANSFORM_PRICE','57,85555555'), ('256','TRANSFORM_ESPECIALES','true'), ('257','TRANSFORM_RAIDBOSS','true'), ('258','BTN_SHOW_TRANSFORMACION_CH','false'), ('259','TRANSFORM_ESPECIALES_PRICE','57,8000'), ('260','TRANSFORM_RAIDBOSS_PRICE','57,855555;3470,2'), ('261','OPCIONES_CHAR_SEXO','true'), ('262','OPCIONES_CHAR_NOBLE','true'), ('263','OPCIONES_CHAR_LVL85','true'), ('264','OPCIONES_CHAR_BUFFER_AIO','false'), ('265','OPCIONES_CHAR_BUFFER_AIO_PRICE','3470,80000'), ('268','OPCIONES_CHAR_BUFFER_AIO_LVL','70'), ('269','VOTO_HOPZONE_IDENTIFICACION',''), ('270','VOTO_TOPZONE_IDENTIFICACION',''), ('271','DESAFIO_MAX_LVL85','8'), ('272','DESAFIO_MAX_NOBLE','3'), ('273','DESAFIO_LVL85','false'), ('274','DESAFIO_NOBLE','false'), ('275','DESAFIO_NPC','true'), ('276','OPCIONES_CHAR_FAME','true'), ('277','OPCIONES_CHAR_FAME_PRICE','57,800000'), ('278','OPCIONES_CHAR_FAME_NOBLE','false'), ('279','OPCIONES_CHAR_FAME_LVL','84'), ('280','OPCIONES_CHAR_FAME_GIVE','250'), ('281','ACCESS_ID','8,127'), ('282','OLY_ANTIFEED_CHANGE_TEMPLATE','true'), ('283','OLY_ANTIFEED_NO_SHOW_NAME_NPC','false'), ('284','OLY_SECOND_SHOW_OPONENTES','20,40,60'), ('285','OLY_SHOW_NAME_OPONENTES','false'), ('286','OLY_ID_ACCESS_POINT_MODIF','8,127'), ('287','TELEPORT_BD','true'), ('288','SERVER_NAME','ZeuS'), ('289','SHOP_USE_BD','true'), ('290','ANTIBOT_COMANDO_STATUS','true'), ('291','ANTIBOT_OPORTUNIDADES','3'), ('292','ANTIBOT_MINUTOS_JAIL','1'), ('293','ANTIBOT_MOB_DEAD_TO_ACTIVATE','90'), ('294','ANTIBOT_MINUTE_VERIF_AGAIN','30'), ('295','ANTIBOT_MINUTOS_ESPERA','2'), ('296','ANTIBOT_MIN_LVL','0'), ('297','ANTIBOT_ONLY_NOBLE','false'), ('298','ANTIBOT_ONLY_HERO','false'), ('299','ANTIBOT_ANTIGUEDAD_MINUTOS_MIN','1'), ('300','ANTIBOT_ONLY_GM','false'), ('301','ANTIBOT_ANNOU_JAIL','true'), ('302','ANTIBOT_AUTO','false')";
	private final String SQL_config_4 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('303','ANTIBOT_RESET_COUNT','true'), ('304','BANIP_CHECK_IP_INTERNET','true'), ('305','BANIP_CHECK_IP_RED','true'), ('306','BANIP_STATUS','true'), ('307','BANIP_DISCONNECT_ALL_PLAYER','true'), ('308','EVENT_COLISEUM_NPC_ID','957'), ('309','FLAG_FINDER_CAN_GO_CASTLE','true'), ('310','VOTE_SHOW_ONLY_ZEUS_ITEM','true'), ('311','VOTE_SHOW_ONLY_ZEUS_ITEM_ID','57'), ('312','VOTE_SHOW_ONLY_ZEUS_ITEM_GIVE_TEMPORAL','true'), ('313','VOTE_SHOW_ONLY_ZEUS_ITEM_ID_TEMPORTAL','57'), ('314','VOTE_SHOW_ONLY_ZEUS_ITEM_TEMPORAL_PRICE','3470,1'), ('315','ANTIBOT_BORRAR_ITEM','true'), ('316','ANTIBOT_PORCENTAJE','50'), ('317','ANTIBOT_ID_BORRAR','57'), ('318','ANTIBOT_CHECK_INPEACE_ZONE','true'), ('319','ANNOUNCE_KARMA_PLAYER_WHEN_KILL','true'), ('320','ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN','%CHAR_NAME% es un Asesino, tiene %KARMA% de karma'), ('321','PARTY_FINDER_USE_NO_SUMMON_RULEZ','true'), ('322','PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE','false'), ('323','VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK','30'), ('324','VOTO_REWARD_AUTO_RANGO_PREMIAR','3'), ('325','VOTO_REWARD_AUTO_MENSAJE_FALTA','Nos Faltan %VOTENEED% votos en %SITE% para la meta de %VOTETOREWARD% votos.'), ('326','VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA','Felicidades, han alcanzado la meta en %SITE%'), ('327','VOTO_REWARD_AUTO_REWARD','57,80000'), ('328','VOTE_AUTOREWARD','false'), ('329','CASTLE_MANAGER_SHOW_GIRAN','true'), ('330','CASTLE_MANAGER_SHOW_ADEN','true'), ('331','CASTLE_MANAGER_SHOW_RUNE','true'), ('332','CASTLE_MANAGER_SHOW_OREN','true'), ('333','CASTLE_MANAGER_SHOW_DION','true'), ('334','CASTLE_MANAGER_SHOW_GLUDIO','true'), ('335','CASTLE_MANAGER_SHOW_GODDARD','true'), ('336','CASTLE_MANAGER_SHOW_SCHUTTGART','true'), ('337','CASTLE_MANAGER_SHOW_INNADRIL','true'), ('338','DROP_SEARCH_SHOW_IDITEM_TO_PLAYER','false'), ('339','SHOW_NEW_MAIN_WINDOWS','true'), ('340','FLAG_FINDER_PK_PRIORITY','false'), ('341','AUGMENT_SPECIAL_PRICE_PAS','3470,10;57,1'), ('342','AUGMENT_SPECIAL_PRICE_CHA','3470,10;57,2'), ('343','AUGMENT_SPECIAL_PRICE_ACT','3470,10;57,3'), ('344','DRESSME','true'), ('345','DRESSME_CAN_USE_IN_OLYS','false'), ('346','DRESSME_CAN_CHANGE_DRESS_IN_OLY','false'), ('347','DRESSME_NEW_DRESS_IS_FREE','true'), ('348','DRESSME_NEW_DRESS_COST','57,2;3470,88888888'), ('349','DRESSME_TARGET','true'), ('350','DROP_SEARCH_ID_MOB_NO_TELEPORT','59068,59069,59070,59071,59072,59073,59074,59075,59076,59077,59079,59080'), ('351','RAIDBOSS_ID_MOB_NO_TELEPORT','59068,59069,59070,59072,59073,59074,59075,59076,59077,59079,59080,59071'), ('352','DROP_SEARCH_CAN_USE_TELEPORT','true'), ('353','RETURN_BUFF','true'), ('354','RETURN_BUFF_MINUTES','1'), ('355','RETURN_BUFF_IN_OLY','false'), ('356','RETURN_BUFF_IN_OLY_MINUTES_TO_RETURN','1'), ('357','RETURN_CANCEL_BUFF_NOT_STEALING',''), ('358','TRADE_WHILE_FLAG','true'), ('359','TRADE_WHILE_PK','true'), ('360','TELEPORT_CAN_USE_IN_COMBAT_MODE','true'), ('361','BUFFCHAR_ACT','true'), ('362','BUFFCHAR_CAN_USE_FLAG','true'), ('363','BUFFCHAR_CAN_USE_PK','false'), ('364','BUFFCHAR_CAN_USE_COMBAT_MODE','false'), ('365','BUFFCHAR_CAN_USE_SIEGE_ZONE','false'), ('366','BUFFCHAR_CAN_USE_INDIVIDUAL_BUFF','true'), ('367','BUFFCHAR_FOR_FREE','true'), ('368','BUFFCHAR_HEAL_FOR_FREE','false'), ('369','BUFFCHAR_CANCEL_FOR_FREE','false'), ('370','BUFFCHAR_COST_USE_SCHEME','22222'), ('371','BUFFCHAR_COST_HEAL','55555'), ('372','BUFFCHAR_COST_CANCEL','88888'), ('373','BUFFCHAR_COST_INDIVIDUAL','99999'), ('374','BUFFCHAR_INDIVIDUAL_FOR_FREE','true'), ('375','BUFFCHAR_DONATION_SECCION',''), ('376','BUFFCHAR_DONATION_SECCION_COST','80000'), ('377','BUFFCHAR_DONATION_SECCION_REMOVE_ITEM','true'), ('378','BUFFCHAR_DONATION_SECCION_ACT','true'), ('379','BUFFCHAR_PET','true'), ('380','ANTIBOT_INACTIVE_MINUTES','5'), ('381','OLY_ANTIFEED_SHOW_IN_NAME_CLASS','false'), ('382','RADIO_PLAYER_NPC_MAXIMO','100'), ('383','SHOW_ZEUS_ENTER_GAME','true'), ('384','PVP_REWARD','false'), ('385','PVP_PARTY_REWARD','false'), ('386','PVP_REWARD_ITEMS','57,1'), ('387','PVP_PARTY_REWARD_ITEMS','57,2'), ('388','PREMIUM_CHAR','true'), ('389','PREMIUM_CLAN','true'), ('390','PREMIUM_CHAR_EXP_PORCEN','20'), ('391','PREMIUM_CHAR_SP_PORCEN','20'), ('392','PREMIUM_CLAN_EXP_PORCEN','20'), ('393','PREMIUM_CLAN_SP_PORCEN','20'), ('394','PREMIUM_CHAR_DROP_PORCEN','20'), ('395','PREMIUM_CLAN_DROP_PORCEN','20'), ('396','PREMIUM_DAYS_GIVE','30'), ('397','PREMIUM_CHAR_COST_DONATION','2'), ('398','PREMIUM_CLAN_COST_DONATION','2'), ('399','DELEVEL_CHECK_SKILL','true'), ('400','MAX_IP_CHECK','false'), ('401','MAX_IP_COUNT','3'), ('402','MAX_IP_RECORD_DATA','false')";
	private final String SQL_config_5 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('403','MAX_IP_VIP_COUNT','5'), ('404','ANTIBOT_SEND_ALL_IP','true'), ('405','ELEMENTAL_LVL_ENCHANT_MAX_WEAPON','300'), ('406','ELEMENTAL_LVL_ENCHANT_MAX_ARMOR','120'), ('407','CHAR_PANEL','true'), ('408','SHOW_FIXME_WINDOWS','true'), ('409','COMMUNITY_BOARD','true'), ('410','COMMUNITY_BOARD_PART_EXEC','_bbsgetfav'), ('411','COMMUNITY_BOARD_REGION','true'), ('412','COMMUNITY_BOARD_REGION_PART_EXEC','_bbsloc'), ('413','COMMUNITY_BOARD_ENGINE','true'), ('414','COMMUNITY_BOARD_ENGINE_PART_EXEC','_bbslink'), ('415','PARTY_FINDER_GO_LEADER_ON_ASEDIO','false'), ('416','COMMUNITY_BOARD_ROWS_FOR_PAGE','18'), ('417','COMMUNITY_BOARD_TOPPLAYER_LIST','15'), ('418','COMMUNITY_BOARD_CLAN_LIST','10'), ('419','COMMUNITY_BOARD_MERCHANT_LIST','5'), ('420','BTN_SHOW_VOTE_CBE','true'), ('421','BTN_SHOW_BUFFER_CBE','true'), ('422','BTN_SHOW_TELEPORT_CBE','true'), ('423','BTN_SHOW_SHOP_CBE','true'), ('424','BTN_SHOW_WAREHOUSE_CBE','true'), ('425','BTN_SHOW_AUGMENT_CBE','false'), ('426','BTN_SHOW_SUBCLASES_CBE','true'), ('427','BTN_SHOW_CLASS_TRANSFER_CBE','false'), ('428','BTN_SHOW_CONFIG_PANEL_CBE','false'), ('429','BTN_SHOW_DROP_SEARCH_CBE','false'), ('430','BTN_SHOW_PVPPK_LIST_CBE','true'), ('431','BTN_SHOW_LOG_PELEAS_CBE','false'), ('432','BTN_SHOW_CASTLE_MANAGER_CBE','false'), ('433','BTN_SHOW_DESAFIO_CBE','false'), ('434','BTN_SHOW_SYMBOL_MARKET_CBE','false'), ('435','BTN_SHOW_CLANALLY_CBE','false'), ('436','BTN_SHOW_PARTYFINDER_CBE','false'), ('437','BTN_SHOW_FLAGFINDER_CBE','false'), ('438','BTN_SHOW_COLORNAME_CBE','true'), ('439','BTN_SHOW_DELEVEL_CBE','false'), ('440','BTN_SHOW_REMOVE_ATRIBUTE_CBE','true'), ('441','BTN_SHOW_BUG_REPORT_CBE','true'), ('442','BTN_SHOW_DONATION_CBE','true'), ('443','BTN_SHOW_CAMBIO_NOMBRE_PJ_CBE','false'), ('444','BTN_SHOW_CAMBIO_NOMBRE_CLAN_CBE','false'), ('445','BTN_SHOW_VARIAS_OPCIONES_CBE','false'), ('446','BTN_SHOW_ELEMENT_ENHANCED_CBE','false'), ('447','BTN_SHOW_ENCANTAMIENTO_ITEM_CBE','false'), ('448','BTN_SHOW_AUGMENT_SPECIAL_CBE','false'), ('449','BTN_SHOW_GRAND_BOSS_STATUS_CBE','true'), ('450','BTN_SHOW_RAIDBOSS_INFO_CBE','true'), ('451','BTN_SHOW_TRANSFORMACION_CBE','true'), ('452','PREMIUM_CHAR_DROP_SPOIL','1'), ('453','PREMIUM_CLAN_DROP_SPOIL','1'), ('454','PREMIUM_CHAR_DROP_RAID','1'), ('455','PREMIUM_CLAN_DROP_RAID','1'), ('456','ANTI_OVER_ENCHANT_ACT','false'), ('457','ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK','false'), ('458','ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK','20'), ('459','ANTI_OVER_ENCHANT_MESJ_PUNISH','Tipo Castigo %TYPE_PUNISHMENT%. Nombre PJ %CHARNAME%'), ('460','ANTI_OVER_TYPE_PUNISH','JAIL'), ('461','ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL','false'), ('462','ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP','false'), ('463','ANTI_OVER_ENCHANT_MAX_WEAPON','18'), ('464','ANTI_OVER_ENCHANT_MAX_ARMOR','18'), ('465','REGISTER_EMAIL_ONLINE','false'), ('466','REGISTER_NEW_PLAYER_WAITING_TIME','60'), ('467','REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME','10'), ('468','REGISTER_NEW_PlAYER_TRIES','3'), ('469','COMMUNITY_BOARD_CLAN_PART_EXEC','_bbsclan'), ('470','COMMUNITY_BOARD_CLAN','true'), ('471','COMMUNITY_BOARD_CLAN_ROWN_LIST','6'), ('472','COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT','true'), ('473','COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE','false'), ('474','COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE','false'), ('475','COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE','true'), ('476','COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN','false'), ('477','REGISTER_NEW_PLAYER_BLOCK_CHAT','true'), ('478','REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT','true'), ('479','OPCIONES_CHAR_CAMBIO_NOMBRE','false'), ('480','OPCIONES_CLAN_CAMBIO_NOMBRE','false'), ('481','COMMUNITY_BOARD_REGION_PLAYER_ON_LIST','50'), ('482','ANTIBOT_SECONDS_TO_RESEND_ANTIBOT','40'), ('483','ANTIBOT_CHECK_DUALBOX','true'), ('484','PREMIUM_CHAR_DROP_ITEM','1'), ('485','PREMIUM_CLAN_DROP_ITEM','1'), ('486','EVENT_RAIDBOSS_RAID_ID',''), ('487','EVENT_RAIDBOSS_RAID_POSITION',''), ('488','EVENT_RAIDBOSS_PLAYER_POSITION',''), ('489','EVENT_RAIDBOSS_PLAYER_INMOBIL','true'), ('490','EVENT_RAIDBOSS_REWARD_WIN','57,18000'), ('491','EVENT_RAIDBOSS_REWARD_LOOSER','57,2'), ('492','EVENT_RAIDBOSS_PLAYER_MIN_LEVEL','1'), ('493','EVENT_RAIDBOSS_PLAYER_MAX_LEVEL','85'), ('494','EVENT_RAIDBOSS_PLAYER_MIN_REGIS','1'), ('495','EVENT_RAIDBOSS_PLAYER_MAX_REGIS','20'), ('496','EVENT_RAIDBOSS_SECOND_TO_BACK','10'), ('497','EVENT_RAIDBOSS_JOINTIME','10'), ('498','EVENT_RAIDBOSS_EVENT_TIME','15'), ('499','EVENT_RAIDBOSS_COLORNAME','FF8000'), ('500','EVENT_RAIDBOSS_CHECK_DUALBOX','false'), ('501','EVENT_RAIDBOSS_CANCEL_BUFF','false'), ('502','EVENT_RAIDBOSS_UNSUMMON_PET','false')";
	private final String SQL_config_6 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('503','EVENT_RAIDBOSS_SECOND_TO_REVIVE','10'), ('504','EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS','5'), ('505','ANTIBOT_SEND_JAIL_ALL_DUAL_BOX','false'), ('506','ANTIFEED_ENCHANT_SKILL_REUSE','3'), ('507','EVENT_RAIDBOSS_HOUR_TO_START','18:00'), ('508','EVENT_RAIDBOSS_AUTOEVENT','true'), ('509','CAN_USE_BSOE_PK','true'), ('510','FLAG_FINDER_MIN_PVP_FROM_TARGET','0'), ('511','PREMIUM_MESSAGE','true'), ('512','VOTO_REWARD_AUTO_REWARD_META_HOPZONE','57,1'), ('513','VOTO_REWARD_AUTO_REWARD_META_TOPZONE','57,2'), ('514','VOTO_REWARD_AUTO_AFK_CHECK','true'), ('515','TRANSFORM_TIME','false'), ('516','TRANSFORM_TIME_MINUTES','10'), ('517','TRANSFORM_REUSE_TIME_MINUTES','20'), ('518','COMMUNITY_REGION_LEGEND',''), ('519','PVP_PK_PROTECTION_LIFETIME_MINUTES','0'), ('520','PVP_REWARD_CHECK_DUALBOX','true'), ('521','BUFFER_HEAL_CAN_FLAG','false'), ('522','BUFFER_HEAL_CAN_IN_COMBAT','false'), ('523','EVENT_RAIDBOSS_MINUTE_INTERVAL','180'), ('524','EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER','10'), ('525','BUFFCHAR_EDIT_SCHEME_ON_MAIN_WINDOWS_BUFFER','true'), ('526','ANTIBOT_BLACK_LIST','true'), ('527','ANTIBOT_BLACK_LIST_MULTIPLIER','10'), ('528','OLY_DUAL_BOX_CONTROL','true'), ('529','PVP_REWARD_RANGE','800'), ('530','PARTY_FINDER_CAN_USE_ONLY_NOBLE','false'), ('531','TELEPORT_FOR_FREE_UP_TO_LEVEL','true'), ('532','TELEPORT_FOR_FREE_UP_TO_LEVEL_LV','40'), ('533','VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER','false'), ('534','EVENT_REPUTATION_CLAN','false'), ('535','EVENT_REPUTATION_CLAN_ID_NPC','958'), ('536','EVENT_REPUTATION_LVL_TO_GIVE','5'), ('537','EVENT_REPUTATION_REPU_TO_GIVE','50000'), ('538','EVENT_REPUTATION_MIN_PLAYER','5'), ('539','EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE','true'), ('540','CHAT_SHOUT_BLOCK','false'), ('541','CHAT_TRADE_BLOCK','false'), ('542','CHAT_WISP_BLOCK','false'), ('543','CHAT_SHOUT_NEED_PVP','-1'), ('544','CHAT_SHOUT_NEED_LEVEL','-1'), ('545','CHAT_SHOUT_NEED_LIFETIME','-1'), ('546','CHAT_TRADE_NEED_PVP','-1'), ('547','CHAT_TRADE_NEED_LEVEL','-1'), ('548','CHAT_TRADE_NEED_LIFETIME','-1'), ('549','CHAT_WISP_NEED_PVP','-1'), ('550','CHAT_WISP_NEED_LEVEL','-1'), ('551','CHAT_WISP_NEED_LIFETIME','-1'), ('552','FLAG_FINDER_CHECK_CLAN','true'), ('553','EVENT_TOWN_WAR_AUTOEVENT','false'), ('554','EVENT_TOWN_WAR_MINUTES_START_SERVER','2'), ('555','EVENT_TOWN_WAR_MINUTES_INTERVAL','120'), ('556','EVENT_TOWN_WAR_CITY_ON_WAR','ADEN'), ('557','EVENT_TOWN_WAR_MINUTES_EVENT','10'), ('558','EVENT_TOWN_WAR_JOIN_TIME','15'), ('559','EVENT_TOWN_WAR_GIVE_PVP_REWARD','false'), ('560','EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER','false'), ('561','EVENT_TOWN_WAR_REWARD_GENERAL',''), ('562','EVENT_TOWN_WAR_REWARD_TOP_PLAYER',''), ('563','EVENT_TOWN_WAR_RANDOM_CITY','false'), ('564','EVENT_TOWN_WAR_DUAL_BOX_CHECK','false'), ('565','EVENT_TOWN_WAR_HIDE_NPC','true'), ('566','EVENT_TOWN_WAR_NPC_ID_HIDE',''), ('567','EVENT_TOWN_WAR_CAN_USE_BUFFER','false'), ('568','VOTE_EVERY_12_HOURS','true'), ('569','COMMUNITY_BOARD_GRAND_RB','false'), ('570','COMMUNITY_BOARD_GRAND_RB_EXEC','_bbsfriends'), ('571','BTN_SHOW_BLACKSMITH_CBE','true')";	
	private final String SQL_config_7 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('572','DONATION_LV_85_COST','0'),('573','DONATION_NOBLE_COST','0'),('574','DONATION_FAME_COST','0'), ('575','DONATION_FAME_AMOUNT','200'),('576','DONATION_CLAN_LV_COST','0'),('577','DONATION_CLAN_LV_LV','5'),('578','DONATION_REDUCE_PK_COST','0'),('579','DONATION_CHANGE_SEX_COST','0'),('580','DONATION_AIO_CHAR_SIMPLE_COSTO','0'), ('581','DONATION_AIO_CHAR_30_COSTO','0'),('582','DONATION_AIO_CHAR_LV_REQUEST','72'),('583','DONATION_CHANGE_CHAR_NAME_COST','0'),('584','DONATION_CHANGE_CLAN_NAME_COST','0'),('585','OPCIONES_CHAR_BUFFER_AIO_30','false'), ('586','OPCIONES_CHAR_BUFFER_AIO_PRICE_30','3470,800'),('587','BTN_SHOW_PARTYMATCHING_CBE','false'), ('588','COMMUNITY_BOARD_PARTYFINDER','false'),('589','COMMUNITY_BOARD_PARTYFINDER_EXEC','_bbsfriends'), ('590','COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE','40'), ('591','COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST','60'), ('592','COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD','360'), ('593','COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE','true'), ('594','BTN_SHOW_AUCTIONHOUSE_CBE','true'), ('595','RAIDBOSS_OBSERVE_MODE','false'), ('596','DROP_SEARCH_OBSERVE_MODE','false'), ('597','OLY_CAN_USE_SCHEME_BUFFER','false'), ('598','DROP_SEARCH_SHIFT_NPC_SHOW','true'), ('599','PVP_CLAN_REPUTATION_REWARD','true'),('600','PVP_CLAN_REPUTATION_AMOUNT','10'), ('601','OPCIONES_CHAR_REDUCE_PK_PRICE','57,1000'), ('602','OPCIONES_CHAR_REDUCE_PK','true'), ('603','OPCIONES_CHAR_REDUCE_AMOUNT','1'), ('604','OLY_ALLOW', 'true'), ('605','OLY_ANNOUNCE_RESULTS','true'), ('606','INSTANCE_ZONE_CLEAR','true'), ('607','INSTANCE_ZONE_COST','57,50000'), ('608','VOTE_ATTEMPTS_ALLOWED','3'), ('609','DELEVEL','false'), ('610','OLY_ANTIFEED_SHOW_IN_TITLE_CLASS','true'), ('611','BUFFER_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM','false'), ('612','SHOP_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM','false'), ('613','JAIL_BAIL_STATUS','false'), ('614','JAIL_BAIL_COST',''), ('615','JAIL_BAIL_MULTIPLE_COST','0'), ('616','JAIL_BAIL_BLACKLIST_MULTIPLE','false'), ('617','PVP_COUNT_ALLOW_SIEGES_FORTRESS','true'), ('618','CHAT_GENERAL_BLOCK','false'), ('619','CHAT_GENERAL_NEED_PVP','-1'), ('620','CHAT_GENERAL_NEED_LEVEL','-1'), ('621','CHAT_GENERAL_NEED_LIFETIME','-1'), ('622','BTN_SHOW_BIDHOUSE_CBE','false')";
	private final String SQL_config_20 = "INSERT IGNORE INTO `zeus_config_seccion` VALUES ('900','ZEUS_OLY_SHOW_TIMER','true'), ('901','ZEUS_OLY_SHOW_DMG','true'), ('902','AUGMENT_SPECIAL_PRICE_SYMBOL',''), ('903','ZEUS_AUTOPOTS_CP','false'), ('904','ZEUS_AUTOPOTS_MP','false'), ('905','ZEUS_AUTOPOTS_HP','false'), ('906','ZEUS_AUTOPOTS_CHECK_MILISECOND','600'), ('907','BTN_SHOW_DRESSME','false'), ('908','STATUS_BUFFER','true'), ('909','STATUS_TELEPORT','true'), ('910','STATUS_SHOP','true'), ('911','STATUS_WAREHOUSE','false'), ('912','STATUS_AUGMENT','true'), ('913','STATUS_SUBCLASES','false'), ('914','STATUS_CLASS_TRANSFER','false'), ('915','STATUS_CONFIG_PANEL','false'), ('916','STATUS_DROP_SEARCH','true'), ('917','STATUS_PVPPK_LIST','true'), ('918','STATUS_LOG_PELEAS','true'), ('919','STATUS_CASTLE_MANAGER','true'), ('920','STATUS_SYMBOL_MARKET','true'), ('921','STATUS_PARTYFINDER','false'), ('922','STATUS_FLAGFINDER','false'), ('923','STATUS_DELEVEL','false'), ('924','STATUS_REMOVE_ATRIBUTE','false'), ('925','STATUS_BUG_REPORT','false'), ('926','STATUS_VARIAS_OPCIONES','false'), ('927','STATUS_ELEMENT_ENHANCED','false'), ('928','STATUS_ENCANTAMIENTO_ITEM','false'), ('929','STATUS_AUGMENT_SPECIAL','false'), ('930','STATUS_GRAND_BOSS_STATUS','false'), ('931','STATUS_RAIDBOSS_INFO','false'), ('932','STATUS_TRANSFORMACION','false'), ('933','STATUS_BLACKSMITH','false'), ('934','STATUS_PARTYMATCHING','false'), ('935','STATUS_AUCTIONHOUSE','false'), ('936','STATUS_BIDHOUSE','false'), ('937','STATUS_DRESSME','false'), ('938', 'COMMUNITY_BOARD_DONATION', 'false'), ('939','COMMUNITY_BOARD_DONATION_PART_EXEC','')";
	
	
	private final String SQL_shop_1 = "INSERT IGNORE INTO `zeus_shop` VALUES ('1','Armors','Venta de Armaduras','icon.armor_t95_u_i03','secc','0','-1','1','0'), ('2','Weapons','Venta de Armas','icon.weapon_sacredumors_i00','secc','0','-1','2','0'), ('3','Jewels','Venta de Joyas','icon.accessary_bluelycan_necklace_i00','secc','0','-1','3','0')";

	private final String SQL_teleport_1 = "INSERT IGNORE INTO `zeus_teleport` VALUES ('1','Towns','Reinados','secc','0','0','0','-1','false','true','true','1','1','true'), ('2','Town Areas','Pueblos y Ciudades','secc','0','0','0','-1','false','true','true','1','3','true'), ('3','Seven Signs','Siete Signos','secc','0','0','0','-1','false','true','true','1','10','true'), ('4','Arenas','Arenas para PvP','secc','0','0','0','-1','false','true','true','1','9','true'), ('6','Hellbound','Area','secc','0','0','0','-1','false','true','true','1','11','true'), ('7','Gracia Area','Gracia','secc','0','0','0','-1','false','true','true','1','13','true'), ('8','Leveling Zone','Zonas de Leveo','secc','0','0','0','-1','false','true','true','1','5','true'), ('9','Matrimonios','Casamientos','go','149369','173351','-949','-1','false','true','true','1','14','true'), ('17','Giran','','go','82625','148605','-3468','1','false','true','true','1','2','true'), ('18','Aden','','go','147439','26933','-2204','1','false','true','true','1','1','true'), ('21','Goddard','','go','147736','-56434','-2780','1','false','true','true','1','3','true'), ('22','Rune','','go','45832','-47995','-796','1','true','true','true','1','5','true'), ('25','Dion','','go','16490','143913','-2935','1','false','true','true','1','6','true'), ('26','Gludio','','go','-14155','122135','-2988','1','false','true','true','1','7','true'), ('27','Gludin','','go','-80986','150232','-3043','1','false','true','true','1','8','true'), ('30','Schuttgart','','go','87358','-141982','-1336','1','false','true','true','1','4','true'), ('31','Heine','','go','105411','218149','-3488','1','false','true','true','1','9','true'), ('32','Hunters Village','','go','115210','74454','-2612','1','false','true','true','1','12','true'), ('33','Floran','','go','16722','169982','-3497','1','false','true','true','1','10','true'), ('34','Oren','','go','81006','53089','-1559','1','false','true','true','1','11','true'), ('36','LVL 1-10','','secc','147545','26647','-2203','8','false','true','true','1','1','true'), ('37','LVL 11-20','','secc','147545','26647','-2203','8','false','true','true','1','2','true'), ('38','LVL 21-40','','secc','147545','26647','-2203','8','false','true','true','1','3','true'), ('39','LVL 41-60','','secc','147545','26647','-2203','8','false','true','true','1','4','true'), ('40','LVL 61-85','','secc','147545','26647','-2203','8','false','true','true','1','5','true'), ('41','Talking E T.','none','go','-3553','236990','-3553','36','false','true','true','1','1','true'), ('42','Talking Northern T.','','go','-101728','213557','-3112','36','false','true','true','1','2','true'), ('43','Shadow Mother T.','','go','48823','40146','-3446','36','false','true','true','1','3','true'), ('44','Elven Forest','','go','25254','41416','-3653','36','false','true','true','1','4','true'), ('45','Elven Ruins','','go','-112869','234938','-3689','36','false','true','true','1','5','true'), ('46','Elven Fortress','','go','29046','74919','-3800','36','false','true','true','1','6','true'), ('47','Shilens Garden','','go','21618','8929','-3644','36','false','true','true','1','7','true'), ('48','The Dark Forest','','go','-14759','22163','-3662','36','false','true','true','1','8','true'), ('49','School of Dark Arts','','go','-47122','59674','-3326','36','false','true','true','1','9','true'), ('50','Valley Of Heroes','','go','-42896','-106538','-1414','36','false','true','true','1','10','true'), ('51','Immortal Plateau','','go','-13731','-122018','-2432','36','false','true','true','1','11','true'), ('52','Cave of Trials','','go','9227','-112544','-2534','36','false','true','true','1','12','true'), ('53','Frozen Valley','','go','113192','-174477','-631','36','false','true','true','1','13','true'), ('54','Western Mining Zone','','go','131909','-209046','-3606','36','false','true','true','1','14','true'), ('55','Abandoned Coal Mines','','go','152377','-179906','854','36','false','true','true','1','15','true'), ('56','Isle Of Souls','','go','-120653','54175','-1322','36','false','true','true','1','16','true'), ('57','Ruins Of Despair','','go','-20122','137438','-3893','37','false','true','true','1','1','true'), ('58','Ruins Of Agony','','go','-56096','106857','-3744','37','false','true','true','1','2','true'), ('59','The Neutral Zone','','go','-10767','75789','-3609','37','false','true','true','1','3','true'), ('60','Windmill Hill','','go','-74921','168620','-3545','37','false','true','true','1','4','true'), ('61','Dion Hills','','go','27433','139190','-3093','37','false','true','true','1','5','true'), ('62','Plains Of Dions','','go','185','172679','-2952','38','false','true','true','1','1','true'), ('63','Execution Grounds','','go','51074','141978','-2859','38','false','true','true','1','2','true'), ('64','Cruma Marshlands','','go','5124','126917','-3654','38','false','true','true','1','3','true'), ('65','Wasteland','','go','-16497','209395','-3685','38','false','true','true','1','4','true'), ('66','Death Pass','','go','70060','127265','-3799','38','false','true','true','1','5','true'), ('67','Cruma Tower','','go','17190','114171','-3429','38','false','true','true','1','6','true'), ('68','Ivory Tower Crater','','go','79285','13641','-4550','38','false','true','true','1','7','true'), ('69','Enchanted Valley','','go','124920','61990','-3910','39','false','true','true','1','1','true'), ('70','TOI -Floor 1','','go','115122','16003','-5114','39','false','true','true','1','2','true'), ('71','The Giant\\'s Cave','','go','174513','52673','-4359','39','false','true','true','1','3','true'), ('72','The Cementery','','go','172152','20344','-3316','39','false','true','true','1','4','true'), ('73','Dragon Valley Cave','','go','131357','114464','-3713','39','false','true','true','1','5','true'), ('74','Monastery Of Silence','','go','109103','-80115','-1375','40','false','true','true','1','1','true'), ('75','Ketra Orc Outpost','','go','130493','-72310','-3528','40','false','true','true','1','2','true'), ('76','Blazing Swamp','','go','145351','-12973','-4436','40','false','true','true','1','3','true'), ('77','Wall of Argos','','go','176895','-50792','-3384','40','false','true','true','1','4','true'), ('78','Imperial Tomb','','go','181077','-82368','-6590','40','false','true','true','1','5','true'), ('79','Forge Of The Gods','','go','173611','-115473','-3758','40','false','true','true','1','6','true'), ('80','Aden Areas','','secc','166244','91514','-3183','2','false','true','true','1','1','true'), ('81','Anghel Waterfall','','go','166244','91514','-3183','80','false','true','true','1','1','true'), ('82','The Blazing Swamp','','go','146828','-12856','-4455','80','false','true','true','1','2','true'), ('83','Border Outpost(East Side)','','go','109699','-7908','-2902','80','false','true','true','1','3','true'), ('84','Giran Areas','','secc','166244','91514','-3183','2','false','true','true','1','2','true'), ('85','Death Pass','','go','70000','126636','-3804','84','false','true','true','1','1','true'), ('87','Goddar Areas','','secc','147765','-56009','-2775','2','false','true','true','1','3','true'), ('88','Ketra Orc Outpost','','go','143885','-68972','-3757','87','false','true','true','1','1','true'), ('89','Rune','','secc','43866','-49135','-797','2','false','true','true','1','4','true'), ('91','Raid','','secc','-20155','137426','-3888','38','false','true','true','1','8','true'), ('93','Raid (20)','','secc','-53477','84313','-3545','37','false','true','true','1','6','true'), ('95','Madness Beast','','go','-53597','84358','-3545','93','false','true','true','1','1','true'), ('96','Zombie Lord Fekel','','go','22189','79990','-3162','93','false','true','true','1','2','true'), ('98','Princess Molrang 25','','go','-60653','128059','-2997','91','false','true','true','1','6','true'), ('99','Greyclaw Kutus 23','','go','-54771','146201','-2880','91','false','true','true','1','1','true'), ('101','Tracker Leader S. 23','','go','-56250','186308','-3194','91','false','true','true','1','3','true'), ('102','Kuroboros Priest 23','','go','-61941','179714','-3522','91','false','true','true','1','4','true'), ('103','Unrequited Kael 24','','go','-60032','188262','-4515','91','false','true','true','1','5','true'), ('104','Zombie Lord C. 25','','go','-12082','138567','-3596','91','false','true','true','1','7','true'), ('105','Ikuntai 25','','go','-21470','152243','-3036','91','false','true','true','1','8','true'), ('106','Soul Scavenger 25','','go','-45274','111368','-3807','91','false','true','true','1','9','true'), ('107','Betrayer of Urutu F. 25','','go','-18007','-100787','-2110','91','false','true','true','1','10','true'), ('108','Mammon Collector Talos 25','','go','172311','-214407','-3550','91','false','true','true','1','11','true'), ('109','Tiger Hornet 26','','go','28647','179119','-3613','91','false','true','true','1','12','true'), ('111','Battered lands','','go','218','235035','-3267','6','false','true','true','1','1','true'), ('112','Hellbound','','go','-11737','236236','-3270','6','false','true','true','1','2','true'), ('113','Enchanted Megaliths','','go','-24285','247610','-3110','6','false','true','true','1','3','true'), ('114','Sand Swept Dunes','','go','-13810','255911','-3297','6','false','true','true','1','4','true'), ('116','Zonas Farm','Zonas Farm','secc','0','0','0','-1','false','true','true','1','7','true'), ('117','Farm Zone PVP','','go','20114','253375','-2032','116','false','true','true','1','2','true'), ('118','Forest of the Dead','','go','52100','-54280','-3158','89','false','true','true','1','1','true'), ('119','Fortress of the Dead','','go','57944','-30993','356','89','false','true','true','1','2','true'), ('120','Stakato Nest','','go','89841','-44586','-2136','89','false','true','true','1','3','true'), ('121','Beast Farm','','go','52074','-80821','-2814','89','false','true','true','1','4','true'), ('122','Swamp Fortress','','go','60375','-64112','-16357','89','false','true','true','1','5','true'), ('123','Swamp of Screams','','go','72520','-49116','-3200','89','false','true','true','1','6','true')";
	private final String SQL_teleport_2 = "INSERT IGNORE INTO `zeus_teleport` VALUES ('124','Swamp Fortress','','go','72361','-56711','-3098','89','false','true','true','1','7','true'), ('125','Valley of Saints','','go','74193','-74493','-3465','89','false','true','true','1','8','true'), ('126','Primaveral Isle','','go','10478','-25029','-3675','89','false','true','true','1','9','true'), ('127','Monastery of Silence','','go','108674','-87817','-2883','89','false','true','true','1','10','true'), ('128','Giran Harbor','','go','47399','185579','-3486','84','false','true','true','1','2','true'), ('129','Hardin Private Academy','','go','105922','109684','-3217','84','false','true','true','1','3','true'), ('130','Devil Isle','','go','42107','206491','-3757','84','false','true','true','1','4','true'), ('131','Dragon Valley','','go','75785','117542','-3775','84','false','true','true','1','5','true'), ('132','Antharas Fortress','','go','84912','90226','-3301','84','false','true','true','1','6','true'), ('133','Antharas Lair','','go','130856','114447','-3729','84','false','true','true','1','7','true'), ('134','Ivory Tower','','go','85342','17818','-3516','80','false','true','true','1','4','true'), ('135','Soul Harbor','','go','-73977','51924','-3675','80','false','true','true','1','5','true'), ('136','Coliseum','','go','146762','46720','-3425','80','false','true','true','1','6','true'), ('137','Forsaken Plains And Fortress','','go','176124','39740','-4124','80','false','true','true','1','7','true'), ('138','The Cementary','','go','185126','20353','-3174','80','false','true','true','1','8','true'), ('139','The Forest of Mirrors','','go','141973','81183','-3005','80','false','true','true','1','9','true'), ('140','Bleazing Swamp','','go','151305','-13963','-4494','80','false','true','true','1','10','true'), ('141','Devastated Castle','','go','183555','-15014','-2771','80','false','true','true','1','11','true'), ('142','Ancient Battleground','','go','108310','-2772','-3439','80','false','true','true','1','12','true'), ('143','Silent Valley','','go','172112','55521','-5922','80','false','true','true','1','13','true'), ('144','Tower of Insolence','','go','114637','13415','-5100','80','false','true','true','1','14','true'), ('145','The Giant Cave','','go','174474','52808','-4370','80','false','true','true','1','15','true'), ('146','Varka Silenos','','go','125797','-40908','-3747','87','false','true','true','1','2','true'), ('147','Wall of Argos','','go','165044','-47896','-3555','87','false','true','true','1','3','true'), ('148','Forge of the Gods (FOG)','','go','170146','-116253','-2092','87','false','true','true','1','4','true'), ('149','Dion Areas','','secc','15651','143012','-2706','2','false','false','true','1','5','true'), ('150','Cruma Marshlands','','go','5066','126961','-3659','149','false','true','true','1','1','true'), ('152','Cruma Tower','','secc','17168','114176','-3440','149','false','true','true','1','2','true'), ('153','Cruma Tower 1','','go','17168','114176','-3440','152','false','true','true','1','1','true'), ('154','Cruma Tower 2','','go','17724','114004','-11667','152','false','true','true','1','2','true'), ('155','Cruma Tower 3','','go','17757','108283','-9058','152','false','true','true','1','3','true'), ('156','Fortress of Resistance','','go','47347','109873','-2044','149','false','true','true','1','3','true'), ('157','Plains of Dion','','go','582','179173','-3715','149','false','true','true','1','4','true'), ('158','Floran Village','','go','15792','169195','-3530','149','false','true','true','1','5','true'), ('159','Bee Hive','','go','34515','188124','-2971','149','false','true','true','1','6','true'), ('160','Hive Fortress','','go','20009','187650','-3444','149','false','true','true','1','7','true'), ('161','Tanor Canyon','','go','60103','163609','-2836','149','false','true','true','1','8','true'), ('162','Oren Areas','','secc','83029','53173','-1496','2','false','true','true','1','6','true'), ('163','Hunters Village','','go','117059','76949','-2683','162','false','true','true','1','1','true'), ('164','Sel Mahum Training Ground (West Gate)','','go','76874','63880','-3643','162','false','true','true','1','2','true'), ('165','Sel Mahum Trining Ground (South Gate)','','go','79460','71475','-3443','162','false','true','true','1','3','true'), ('166','Sel Mahum Training Ground (Center)','','go','87412','61478','-3659','162','false','true','true','1','4','true'), ('167','Plains of the Lizardmen','','go','86692','85821','-2921','162','false','true','true','1','5','true'), ('168','Outlaw Forest','','go','91528','-12221','-2435','162','false','true','true','1','6','true'), ('169','Sea of Spores','','go','63450','26302','-3755','162','false','true','true','1','7','true'), ('170','Gludio Areas','','secc','-12766','122800','-3117','2','false','true','true','1','7','true'), ('171','Isle of Soul : Distopya','','go','-73933','51910','-3675','170','false','true','true','1','1','true'), ('172','Ruins of Agony','','go','-42686','119517','-3531','170','false','true','true','1','2','true'), ('173','Ruins of Despair','','go','-19882','137974','-3877','170','false','true','true','1','3','true'), ('174','Waestland','','go','-10035','175299','-4158','170','false','true','true','1','4','true'), ('175','The Ant Nest','','go','-14517','176862','-5311','170','false','true','true','1','5','true'), ('176','Windawood Manor','','go','-28332','155104','-3491','170','false','true','true','1','6','true'), ('178','Gludin Areas','','secc','-80723','149728','-3044','2','false','true','true','1','8','true'), ('179','Langk Lizardmen Dwellings','','go','-44764','203544','-3587','178','false','true','true','1','1','true'), ('180','Windmill Hill','','go','-76596','169227','-3719','178','false','true','true','1','2','true'), ('181','Forgotten Temple','','go','-52832','190599','-3489','178','false','true','true','1','3','true'), ('182','Abandoned Camp','','go','-50235','146482','-2789','178','false','true','true','1','4','true'), ('183','Red Rock Ridge','','go','-42291','198339','-2795','178','false','true','true','1','5','true'), ('184','Windy Hill','','go','-88235','83916','-3019','178','false','true','true','1','6','true'), ('185','Orc Barracks','','go','-89804','105336','-3571','178','false','true','true','1','7','true'), ('186','Schuttgart Areas','','secc','87051','-143399','-1293','2','false','true','true','1','9','true'), ('187','Den of Evil','','go','68660','-110485','-1899','186','false','true','true','1','1','true'), ('188','Planderous Plains','','go','111984','-154190','-1523','186','false','true','true','1','2','true'), ('189','Icemanós Hout','','go','113830','-109275','-843','186','false','true','true','1','3','true'), ('190','Crypts of Disgrace','','go','46452','-115939','-3762','186','false','true','true','1','4','true')";

	/***
	 *
	 *Arriba Insert de datos a la tablas
	 *
	 *
	 *Abajo creación de procedimientos Almacenados
	 *
	 *
	 */

	private final String SQL_SP_randon_rango = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` FUNCTION `func_randon_rango`(`hasta`  int) RETURNS int(11) DETERMINISTIC\n"+
									"BEGIN\n"+
										"DECLARE IDDAR INT;\n"+
										"DECLARE Paso SMALLINT;\n"+
										"SET Paso = 0;\n"+
											"WHILE(Paso = 0) DO\n"+
													"SET IDDAR = (FLOOR( 0 + RAND( ) * hasta ));\n"+
														"IF NOT EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idPremioDado = IDDAR) THEN\n"+
															"SET Paso = 1;\n"+
														"END IF;\n"+
											"END WHILE;\n"+
										"RETURN IDDAR;\n"+
									"END";
	private final String SQL_SP_antibot_blacklist = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_antibot_blacklist`(IN idCharIN INTEGER, IN NombreChar VARCHAR(50))\n"+
								"BEGIN\n"+
									"IF(SELECT zeus_antibot_blacklist.id FROM zeus_antibot_blacklist WHERE zeus_antibot_blacklist.idchar = idCharIN) THEN\n"+
										"UPDATE zeus_antibot_blacklist SET zeus_antibot_blacklist.veces = zeus_antibot_blacklist.veces + 1 WHERE zeus_antibot_blacklist.idchar = idCharIN;\n"+
										"SELECT zeus_antibot_blacklist.veces FROM zeus_antibot_blacklist WHERE zeus_antibot_blacklist.idchar = idCharIN;\n"+
									"ELSE\n"+
										"INSERT INTO zeus_antibot_blacklist(zeus_antibot_blacklist.idchar, zeus_antibot_blacklist.nomChar) VALUES (idCharIN, NombreChar);\n"+
										"SELECT 1;\n"+
									"END IF;\n"+
								"END";
	
	
	private final String SQL_SP_augment = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_augment`(IN tipo SMALLINT, IN Seccion VARCHAR(20), IN idSkill INTEGER, IN Pagina SMALLINT, IN Limite SMALLINT)\n"+
								"BEGIN\n"+
									"IF tipo = 1 THEN\n"+
									"SET @SQLC=CONCAT('\n"+
										"SELECT\n"+
											"zeus_augment_data.id,\n"+
											"zeus_augment_data.idaugmentGame,\n"+
											"zeus_augment_data.tipo,\n"+
											"zeus_augment_data.aug_descrip,\n"+
											"zeus_augment_data.aug_skill,\n"+
											"zeus_augment_data.skill_descrip,\n"+
											"zeus_augment_data.skill_level\n"+
										"FROM\n"+
											"zeus_augment_data\n"+
										"WHERE\n"+
											"zeus_augment_data.tipo = \\'',Seccion,'\\' \n"+
										"GROUP BY\n"+
											"zeus_augment_data.aug_descrip\n"+
										"LIMIT ',Pagina,',',Limite);\n"+
										"PREPARE STMP FROM @SQLC;\n"+
										"EXECUTE STMP;\n"+
									"END IF;\n"+
									"IF tipo = 2 THEN\n"+
										"SELECT\n"+
											"zeus_augment_data.id,\n"+
											"zeus_augment_data.idaugmentGame,\n"+
											"zeus_augment_data.tipo,\n"+
											"zeus_augment_data.aug_descrip,\n"+
											"zeus_augment_data.aug_skill,\n"+
											"zeus_augment_data.skill_descrip,\n"+
											"zeus_augment_data.skill_level\n"+
										"FROM\n"+
											"zeus_augment_data\n"+
										"WHERE\n"+
											"zeus_augment_data.aug_skill = idSkill\n"+
										"ORDER BY zeus_augment_data.skill_level DESC\n"+
										"LIMIT 1;\n"+
									"END IF;\n"+
									"IF tipo = 3 THEN\n"+
										"SELECT\n"+
											"zeus_augment_data.id,\n"+
											"zeus_augment_data.idaugmentGame,\n"+
											"zeus_augment_data.tipo,\n"+
											"zeus_augment_data.aug_descrip,\n"+
											"zeus_augment_data.aug_skill,\n"+
											"zeus_augment_data.skill_descrip,\n"+
											"zeus_augment_data.skill_level\n"+
										"FROM\n"+
											"zeus_augment_data\n"+
										"WHERE\n"+
											"zeus_augment_data.idaugmentGame = Seccion\n"+
										"ORDER BY zeus_augment_data.skill_level DESC\n"+
										"LIMIT 1;\n"+
									"END IF;\n"+
								"END";

	private final String SQL_SP_buffAIO = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_buffAIO`(IN tipo SMALLINT, IN Nombre VARCHAR(30))\n"+
			"BEGIN\n"+
				"IF tipo = 1 THEN\n"+
					"SELECT zeus_buff_for_aio.idBuff, zeus_buff_for_aio.buffLevel, zeus_buff_for_aio.buffEnchant FROM zeus_buff_for_aio;\n"+
				"END IF;\n"+
				"IF tipo = 2 THEN\n"+
					"IF MID(Nombre,1,1) != \"~\" THEN\n"+
						"SET @NuevoNombre = CONCAT(\"[BUFF]\",Nombre);\n"+
						"IF EXISTS(SELECT characters.charId FROM characters WHERE characters.char_name = @NuevoNombre) THEN\n"+
							"SELECT 1 as \"A\";\n"+
						"ELSE\n"+
							"SELECT 0 as \"A\";\n"+
						"END IF;\n"+
					"ELSE\n"+
						"SELECT 1 as \"A\";\n"+
					"END IF;\n"+
				"END IF;\n"+
			"END";

	private final String SQL_SP_bug_ingreso = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_bugg_ingreso`(IN tipo SMALLINT, IN tipoBug VARCHAR(20), IN Mensaje TEXT, IN NombrePlayer VARCHAR(30))\n"+
								"BEGIN\n"+
									"IF tipo = 1 THEN\n"+
										"INSERT INTO zeus_bug_report(\n"+
											"zeus_bug_report.tipo,\n"+
											"zeus_bug_report.mensaje,\n"+
											"zeus_bug_report.fechaIngreso,\n"+
											"zeus_bug_report.PlayerNom\n"+
										")VALUE(\n"+
											"tipoBug,\n"+
											"Mensaje,\n"+
											"NOW(),\n"+
											"NombrePlayer\n"+
										");\n"+
										"SELECT 'INGRESO CORRECTO. GRACIAS POR TU AYUDA!' as \"A\";\n"+
									"END IF;\n"+
								"END";

	private final String SQL_SP_creditosNPC = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_creditosNPC`(IN tipo SMALLINT, IN AccNom VARCHAR(80), IN Creditos INTEGER)\n"+
									"BEGIN\n"+
										"IF tipo = 2 THEN\n"+
											"IF EXISTS(SELECT zeus_dona_creditos.id FROM zeus_dona_creditos WHERE zeus_dona_creditos.cuenta = AccNom AND zeus_dona_creditos.entregados= 'NO') THEN\n"+
												"SELECT SUM(zeus_dona_creditos.creditos) AS 'A' FROM zeus_dona_creditos WHERE zeus_dona_creditos.cuenta = AccNom AND zeus_dona_creditos.entregados= 'NO';\n"+
												"IF Creditos = 0 THEN\n"+
													"UPDATE zeus_dona_creditos SET zeus_dona_creditos.entregados = 'SI',zeus_dona_creditos.fechaEntregado = NOW() WHERE zeus_dona_creditos.cuenta = AccNom AND zeus_dona_creditos.entregados= 'NO' ;\n"+
												"END IF;\n"+
											"ELSE\n"+
												"SELECT -1 as 'A';\n"+
											"END IF;\n"+
										"END IF;\n"+
									"END";


	private final String SQL_SP_dressme = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_dressme`(IN tipo SMALLINT, IN idCharIN INT, IN DressPosition SMALLINT, IN sqlScript VARCHAR(600))\n"+
								"BEGIN\n"+
									"IF tipo = 1 THEN\n"+
										"SET @ScripSQLConcat = \"\";\n"+
										"IF EXISTS(SELECT zeus_dressme.id FROM zeus_dressme WHERE zeus_dressme.idChar = idCharIN) THEN\n"+
											"IF (DressPosition = 9) THEN\n"+
												"SET @ScriptSQLConcat = CONCAT(\"UPDATE zeus_dressme SET zeus_dressme.used = \", 0 ,\" WHERE zeus_dressme.idChar = \", idCharIN);\n"+
											"ELSE\n"+
												"SET @ScriptSQLConcat = CONCAT(\"UPDATE zeus_dressme SET zeus_dressme.d\",DressPosition,\" = '\",sqlScript,\"', zeus_dressme.used = \", DressPosition ,\" WHERE zeus_dressme.idChar = \", idCharIN);\n"+
											"END IF;\n"+
										"ELSE\n"+
											"SET @ScriptSQLConcat = CONCAT(\"INSERT INTO zeus_dressme(zeus_dressme.idChar, zeus_dressme.d\",DressPosition,\",zeus_dressme.used) VALUES (\",idCharIN,\", '\",sqlScript,\"',\",DressPosition,\")\");\n"+
										"END IF;\n"+
										"PREPARE SMTP FROM @ScriptSQLConcat;\n"+
										"EXECUTE SMTP;\n"+
										"SELECT 'cor';\n"+
									"END IF;\n"+
									"IF tipo = 2 THEN\n"+
										"IF(SELECT zeus_dressme.id FROM zeus_dressme WHERE zeus_dressme.idChar = idCharIN) THEN\n"+
											"SELECT\n"+
												"zeus_dressme.d1,\n"+
												"zeus_dressme.d2,\n"+
												"zeus_dressme.d3,\n"+
												"zeus_dressme.d4,\n"+
												"zeus_dressme.d5,\n"+
												"zeus_dressme.d6,\n"+
												"zeus_dressme.d7,\n"+
												"zeus_dressme.d8,\n"+
												"zeus_dressme.d9,\n"+
												"zeus_dressme.d10,\n"+
												"zeus_dressme.used,\n"+
												"zeus_dressme.seeOtherDressme\n"+
											"FROM\n"+
												"zeus_dressme\n"+
											"WHERE\n"+
												"zeus_dressme.idChar = idCharIN;\n"+
										"ELSE\n"+
											"INSERT INTO zeus_dressme(zeus_dressme.idChar, zeus_dressme.used) VALUES ( idCharIN,0);\n"+
											"SELECT '' as d1, '' as d2, '' as d3, '' as d4, '' as d5, '' as d6, '' as d7, '' as d8, '' as d9, '' as d10, 0 as used, 'yes' as seeOtherDressme;\n"+
										"END IF;\n"+
									"END IF;\n"+
									"IF tipo = 3 THEN\n"+
										"IF EXISTS(SELECT zeus_dressme.id FROM zeus_dressme WHERE zeus_dressme.idChar = idCharIN) THEN\n"+
											"UPDATE zeus_dressme SET zeus_dressme.used = sqlScript WHERE zeus_dressme.idChar = idCharIN;\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+
								"END";

	private final String SQL_SP_Evento = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_evento_inicial`(IN tipo SMALLINT, IN idCharIN BIGINT)\n"+
								"BEGIN\n"+
									"#1.- Level 85\n"+
									"#2.- Noble\n"+
									"#3.- Recomendaciones\n"+
									"#4.- Fama\n"+
									"#5.- Raid Boss EVENT\n"+
									"#6.- Raid Point\n"+
									
									"IF tipo = -2 THEN\n"+
										"SELECT count(*) FROM zeus_evento_in;\n"+
									"END IF;\n"+
										
									"IF tipo = -1 THEN\n"+
										"SELECT zeus_evento.Accion FROM zeus_evento;\n"+
									"END IF;\n"+

									"IF tipo = 1 THEN\n"+
										"IF NOT EXISTS(SELECT zeus_evento.idChar FROM zeus_evento WHERE zeus_evento.idChar = idCharIN AND zeus_evento.Accion = tipo) THEN\n"+
											"INSERT INTO zeus_evento() VALUES (idCharIN,tipo, NOW());\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+

									"IF tipo = 2 THEN\n"+
										"IF NOT EXISTS(SELECT zeus_evento.idChar FROM zeus_evento WHERE zeus_evento.idChar = idCharIN AND zeus_evento.Accion = tipo) THEN\n"+
											"INSERT INTO zeus_evento() VALUES (idCharIN,tipo, NOW());\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+

									"IF tipo = 3 THEN\n"+
										"IF NOT EXISTS(SELECT zeus_evento.idChar FROM zeus_evento WHERE zeus_evento.idChar = idCharIN AND zeus_evento.Accion = tipo) THEN\n"+
											"INSERT INTO zeus_evento() VALUES (idCharIN,tipo, NOW());\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+

									"IF tipo = 4 THEN\n"+
										"IF NOT EXISTS(SELECT zeus_evento.idChar FROM zeus_evento WHERE zeus_evento.idChar = idCharIN AND zeus_evento.Accion = tipo) THEN\n"+
											"INSERT INTO zeus_evento() VALUES (idCharIN,tipo, NOW());\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+

									"IF tipo = 5 THEN\n"+
										"IF NOT EXISTS(SELECT zeus_evento.idChar FROM zeus_evento WHERE zeus_evento.idChar = idCharIN AND zeus_evento.Accion = tipo) THEN\n"+
											"INSERT INTO zeus_evento() VALUES (idCharIN,tipo, NOW());\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+

									"IF tipo = 6 THEN\n"+
										"IF NOT EXISTS(SELECT zeus_evento.idChar FROM zeus_evento WHERE zeus_evento.idChar = idCharIN AND zeus_evento.Accion = tipo) THEN\n"+
											"INSERT INTO zeus_evento() VALUES (idCharIN,tipo, NOW());\n"+
											"SELECT 'cor';\n"+
										"ELSE\n"+
											"SELECT 'err';\n"+
										"END IF;\n"+
									"END IF;\n"+

								"END";

	private final String SQL_SP_existe_nombre = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_existe_nombre`(IN tipo SMALLINT, IN NombreNuevo VARCHAR(80), IN NombreActual VARCHAR(80))\n"+
		"BEGIN\n"+
			"IF tipo = 1 THEN\n"+
				"IF LENGTH(NombreNuevo) <= 16 THEN\n"+
					"IF EXISTS(SELECT characters.accesslevel FROM characters WHERE characters.char_name = NombreNuevo) THEN\n"+
						"SELECT 'err',CONCAT('El Nombre ', NombreNuevo,' ya existe.');\n"+
					"ELSE\n"+
						"IF INSTR(NombreActual,\"[BUFF]\")>0 THEN\n"+
							"IF LENGTH(NombreNuevo)>10 THEN\n"+
								"SELECT 'err','El Nombre elegido para la Buffer no puede ser otorgado por el Largo de este. Un Buffer debe tener móximo un largo de 10 Caracteres.';\n"+
							"ELSE\n"+
								"SET @NuevoNombreBuffer = concat(\"[BUFF]\",NombreNuevo);\n"+
								"IF EXISTS(SELECT characters.accesslevel FROM characters WHERE characters.char_name = @NuevoNombreBuffer) THEN\n"+
									"SELECT 'err','El nombre para el Buffer ya estó ocupado. Eliga otro.';\n"+
								"ELSE\n"+
									"SELECT 'cor1',@NuevoNombreBuffer;\n"+
								"END IF;\n"+
							"END IF;\n"+
						"ELSE\n"+
							"SELECT 'cor','';\n"+
						"END IF;\n"+
					"END IF;\n"+
				"ELSE\n"+
					"SELECT 'err','El nombre Ingresado tiene mós de 16 Carócteres';\n"+
				"END IF;\n"+
			"END IF;\n"+
		"END";

	@SuppressWarnings("unused")
	private final String SQL_SP_can_vote = "";//Pendiente para reprogramar


	private final String SQL_SP_get_augment = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_get_augment`(IN tipo SMALLINT, IN idAugment VARCHAR(40))\n"+
		"BEGIN\n"+
			"IF tipo = 1 THEN\n"+
				"IF EXISTS(SELECT zeus_augment_data.id FROM zeus_augment_data WHERE zeus_augment_data.idaugmentGame = idAugment) THEN\n"+
					"SELECT zeus_augment_data.aug_skill,zeus_augment_data.skill_level FROM zeus_augment_data WHERE zeus_augment_data.idaugmentGame = idAugment;\n"+
				"ELSE\n"+
					"SELECT 'err';\n"+
				"END IF;\n"+
			"END IF;\n"+
		"END";

	private final String SQL_SP_get_raidboss ="CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_get_raidboss_info`(IN tipo SMALLINT, IN Desde INTEGER, IN Hasta INTEGER, IN Buscar VARCHAR(200))"+
			"BEGIN\n"+
		"IF tipo = 1 THEN\n"+
			"SET @Consulta = CONCAT('\n"+
			"SELECT\n"+
				"IFNULL((SELECT CONCAT(npc.`name`,\"-\",npc.`level`) FROM npc WHERE npc.id = raidboss_spawnlist.boss_id),(SELECT CONCAT(custom_npc.`name`,\"-\",custom_npc.`level`) FROM custom_npc WHERE custom_npc.id = raidboss_spawnlist.boss_id)) as Nombre,\n"+
				"raidboss_spawnlist.boss_id,\n"+
				"IF(raidboss_spawnlist.respawn_time !=0,FROM_UNIXTIME(raidboss_spawnlist.respawn_time/1000),\"0\"),\n"+
				"raidboss_spawnlist.loc_x,\n"+
				"raidboss_spawnlist.loc_y,\n"+
				"raidboss_spawnlist.loc_z\n"+
			"FROM\n"+
				"raidboss_spawnlist\n"+
			"ORDER BY Nombre ASC\n"+
				"LIMIT ',Desde,',',Hasta);\n"+
		"END IF;\n"+
		"IF tipo = 2 THEN\n"+
		"SET @Consulta = CONCAT('\n"+
		"SELECT\n"+
			"IFNULL((SELECT CONCAT(npc.`name`,\"-\",npc.`level`) FROM npc WHERE npc.id = raidboss_spawnlist.boss_id),(SELECT CONCAT(custom_npc.`name`,\"-\",custom_npc.`level`) FROM custom_npc WHERE custom_npc.id = raidboss_spawnlist.boss_id)) as Nombre,\n"+
			"raidboss_spawnlist.boss_id,\n"+
			"IF(raidboss_spawnlist.respawn_time !=0,FROM_UNIXTIME(raidboss_spawnlist.respawn_time/1000),\"0\"),\n"+
			"raidboss_spawnlist.loc_x,\n"+
			"raidboss_spawnlist.loc_y,\n"+
			"raidboss_spawnlist.loc_z\n"+
		"FROM\n"+
			"raidboss_spawnlist\n"+
		"WHERE\n"+
		"raidboss_spawnlist.boss_id IN (SELECT npc.id FROM npc WHERE npc.`name` LIKE \\'%',Buscar,'%\\')\n"+
		"ORDER BY Nombre ASC\n"+
			"LIMIT ',Desde,',',Hasta);\n"+
	"END IF;\n"+
		"PREPARE STMP FROM @Consulta;\n"+
		"EXECUTE STMP;\n"+				
		"END";

	private final String SQL_FUNC_func_id_zeus_dona_espera = "CREATE DEFINER=`"+ general.getUser() +"`@`"+ general.getHost() +"` FUNCTION `func_id_dona_espera`(`idCharIN` integer) RETURNS varchar(60) CHARSET latin1 DETERMINISTIC\n"+
		"BEGIN\n"+
			"DECLARE Retorno VARCHAR(80);\n"+
			"SET Retorno = MD5(MD5(CONCAT(NOW(),NOW(),idCharIN)));\n"+
			"RETURN Retorno;\n"+
		"END";

	private final String SQL_SP_Ingresa_Dona = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_ingresa_dona`(IN tipo SMALLINT, IN idDonacion VARCHAR(200), IN Monto VARCHAR(10), IN CHARIN VARCHAR(80), IN MEDIO VARCHAR(80), IN EMAILDONA VARCHAR(200), IN OBSERVA VARCHAR(380))\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"SET @IDWEB = func_id_dona_espera(CHARIN);\n"+
		"SELECT characters.account_name INTO @Cuenta FROM characters WHERE characters.charId = CHARIN;\n"+
		"INSERT INTO zeus_dona_espera(\n"+
			"zeus_dona_espera.id,\n"+
			"zeus_dona_espera.dona_monto,\n"+
			"zeus_dona_espera.dona_char,\n"+
			"zeus_dona_espera.dona_medio,\n"+
			"zeus_dona_espera.dona_email,\n"+
			"zeus_dona_espera.dona_obser,\n"+
			"zeus_dona_espera.dona_fecha)\n"+
			"VALUES(\n"+
				"@IDWEB,\n"+
				"Monto,\n"+
				"CHARIN,\n"+
				"MEDIO,\n"+
				"EMAILDONA,\n"+
				"OBSERVA,\n"+
				"NOW()\n"+
			");\n"+
		"SELECT 'cor','Registro de Donación Cursada. Para mós Información revice su correo.',@IDWEB, @Cuenta;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"IF EXISTS(SELECT zeus_dona_espera.id FROM zeus_dona_espera WHERE zeus_dona_espera.id = idDonacion) THEN\n"+
			"SELECT\n"+
				"zeus_dona_espera.dona_email,\n"+
				"zeus_dona_espera.dona_medio,\n"+
				"zeus_dona_espera.dona_obser,\n"+
				"(SELECT characters.account_name FROM characters WHERE characters.charId = zeus_dona_espera.dona_char),\n"+
				"zeus_dona_espera.id,\n"+
				"zeus_dona_espera.dona_monto,\n"+
				"zeus_dona_espera.dona_activa \n"+
			"FROM\n"+
				"zeus_dona_espera\n"+
			"WHERE\n"+
				"zeus_dona_espera.id = idDonacion;\n"+
		"ELSE\n"+
				"SELECT 'err','Error al Enviar Infomación por Correo de la Donación';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"IF EXISTS(SELECT zeus_dona_espera.id FROM zeus_dona_espera WHERE zeus_dona_espera.id=idDonacion AND zeus_dona_espera.dona_activa='true') THEN\n"+
			"SELECT characters.account_name INTO @CUENTACHAR FROM characters WHERE characters.charId = (SELECT zeus_dona_espera.dona_char FROM zeus_dona_espera WHERE zeus_dona_espera.id = idDonacion);\n"+
			"SELECT zeus_dona_espera.dona_email INTO @EMAILCHAR FROM zeus_dona_espera WHERE zeus_dona_espera.id = idDonacion;\n"+
			"UPDATE zeus_dona_espera SET zeus_dona_espera.dona_activa='false' WHERE zeus_dona_espera.id = idDonacion;\n"+
			"IF EXISTS(SELECT zeus_dona_creditos.id FROM zeus_dona_creditos WHERE zeus_dona_creditos.cuenta = @CUENTACHAR AND zeus_dona_creditos.entregados = 'NO') THEN\n"+
				"UPDATE zeus_dona_creditos SET zeus_dona_creditos.creditos = zeus_dona_creditos.creditos + Monto WHERE zeus_dona_creditos.cuenta = @CUENTACHAR AND zeus_dona_creditos.entregados = 'NO';\n"+
			"ELSE\n"+
				"INSERT INTO zeus_dona_creditos(\n"+
					"zeus_dona_creditos.creditos,\n"+
					"zeus_dona_creditos.cuenta,\n"+
					"zeus_dona_creditos.fechaDeposit\n"+
				")VALUES(\n"+
					"Monto,\n"+
					"@CUENTACHAR,\n"+
					"NOW()\n"+
				");\n"+
			"END IF;\n"+
			"SELECT zeus_dona_creditos.creditos INTO @MONTOTOTAL FROM zeus_dona_creditos WHERE zeus_dona_creditos.cuenta = @CUENTACHAR AND zeus_dona_creditos.entregados = 'NO';\n"+
			"SELECT 'cor','Donacion Activada.',Monto,@CUENTACHAR, @MONTOTOTAL, @EMAILCHAR;\n"+
		"ELSE\n"+
			"SELECT 'err','Donación Invalida, ya fue activada';\n"+
		"END IF;\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_ip ="CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_ip`(IN tipo SMALLINT, IN ipIN VARCHAR(20), IN ipMACHI VARCHAR(20))\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"IF EXISTS(SELECT zeus_ipblock.ipRED FROM zeus_ipblock WHERE zeus_ipblock.ipWAN = ipIN AND zeus_ipblock.ipRED = ipMACHI) THEN\n"+
			"SELECT 'err';\n"+
		"ELSE\n"+
			"INSERT INTO zeus_ipblock(\n"+
				"zeus_ipblock.ipWAN,\n"+
				"zeus_ipblock.ipRED\n"+
			")VALUES(\n"+
				"ipIN,\n"+
				"ipMACHI\n"+
			");\n"+
			"SELECT 'cor';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"DELETE FROM zeus_ipblock WHERE zeus_ipblock.ipWAN = ipIN;\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
	"IF TIPO = 3 THEN\n"+
		"DELETE FROM zeus_ipblock WHERE zeus_ipblock.ipRED = ipMACHI;\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
"END";


	protected final String SQL_SP_lista_log_pvp = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_lista_log_pvp`(IN tipo SMALLINT, IN NomCharIN VARCHAR(80), IN ListaMaxima INTEGER, IN Desde INTEGER)\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"SELECT characters.charId INTO @IDCHAR FROM characters WHERE characters.char_name = NomCharIN;\n"+
		"IF EXISTS(SELECT zeus_log_fight.Asesinado FROM zeus_log_fight WHERE zeus_log_fight.idAtacante = @IDCHAR OR zeus_log_fight.idAsesinado = @IDCHAR) THEN\n"+
			"SELECT COUNT(*) INTO @Cantidad FROM zeus_log_fight WHERE zeus_log_fight.idAtacante = @IDCHAR OR zeus_log_fight.idAsesinado = @IDCHAR;\n"+
			"SET @ParaEjecutar = CONCAT('\n"+
			"SELECT\n"+
				"zeus_log_fight.Atacante,\n"+
				"zeus_log_fight.Asesinado,\n"+
				"zeus_log_fight.veces,\n"+
				"zeus_log_fight.tipPelea,\n"+
				"@Cantidad\n"+
			"FROM\n"+
				"zeus_log_fight\n"+
			"WHERE\n"+
				"zeus_log_fight.idAtacante = @IDCHAR\n"+
				"OR\n"+
				"zeus_log_fight.idAsesinado = @IDCHAR\n"+
			"LIMIT ',Desde,' , ', ListaMaxima);\n"+
			"PREPARE SMTP FROM @ParaEjecutar;\n"+
			"EXECUTE SMTP;\n"+
		"ELSE\n"+
			"SELECT 'err',CONCAT('No hay información de pvp o PK del Char<br1>', NomCharIN);\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
	"SET @IDCHAR = NomCharIN;\n"+
	"IF EXISTS(SELECT zeus_log_fight.Asesinado FROM zeus_log_fight WHERE zeus_log_fight.idAtacante = @IDCHAR OR zeus_log_fight.idAsesinado = @IDCHAR) THEN\n"+
		"SELECT COUNT(*) INTO @Cantidad FROM zeus_log_fight WHERE zeus_log_fight.idAtacante = @IDCHAR OR zeus_log_fight.idAsesinado = @IDCHAR;\n"+
		"SET @ParaEjecutar = CONCAT('\n"+
		"SELECT\n"+
			"IFNULL((SELECT characters.char_name FROM characters WHERE characters.charId = zeus_log_fight.idAtacante),\"NO EXIST\"),\n"+
			"IFNULL((SELECT characters.char_name FROM characters WHERE characters.charId = zeus_log_fight.idAsesinado),\"NO EXIST\"),\n"+
			"zeus_log_fight.veces,\n"+
			"zeus_log_fight.tipPelea,\n"+
			"IFNULL((SELECT characters.base_class FROM characters WHERE characters.charId = zeus_log_fight.idAtacante),\"NULL\"),\n"+
			"IFNULL((SELECT characters.base_class FROM characters WHERE characters.charId = zeus_log_fight.idAsesinado),\"NULL\"),\n"+
			"@Cantidad\n"+
		"FROM\n"+
			"zeus_log_fight\n"+
		"WHERE\n"+
			"(zeus_log_fight.idAtacante = @IDCHAR\n"+
			"OR\n"+
			"zeus_log_fight.idAsesinado = @IDCHAR)\n"+
			"AND(\n"+
			"zeus_log_fight.idAtacante IN (SELECT characters.charId FROM characters WHERE characters.accesslevel=0)\n"+
			"AND\n"+
			"zeus_log_fight.idAsesinado IN (SELECT characters.charId FROM characters WHERE characters.accesslevel=0)\n"+
			")\n"+
		"LIMIT ',Desde,' , ', ListaMaxima);\n"+
		"PREPARE SMTP FROM @ParaEjecutar;\n"+
		"EXECUTE SMTP;\n"+
	"ELSE\n"+
		"SELECT 'err','No hay información de pvp o PK del Char';\n"+
	"END IF;\n"+
"END IF;\n"+
"END";

	private final String SQL_SP_log_fight = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_log_fight`(IN idAtaca INTEGER, IN idAsesi INTEGER, IN TipoPelea VARCHAR(80))\n"+
"BEGIN\n"+
	"IF idAtaca >0 THEN\n"+
		"IF EXISTS(SELECT zeus_log_fight.veces FROM zeus_log_fight WHERE zeus_log_fight.idAtacante = idAtaca AND zeus_log_fight.idAsesinado = idAsesi AND zeus_log_fight.tipPelea = TipoPelea ) THEN\n"+
			"UPDATE zeus_log_fight SET zeus_log_fight.veces = zeus_log_fight.veces + 1 WHERE zeus_log_fight.idAtacante = idAtaca AND zeus_log_fight.idAsesinado = idAsesi AND zeus_log_fight.tipPelea = TipoPelea;\n"+
			"SELECT 'cor';\n"+
		"ELSE\n"+
			"INSERT INTO zeus_log_fight(\n"+
				"zeus_log_fight.Atacante,\n"+
				"zeus_log_fight.idAtacante,\n"+
				"zeus_log_fight.Asesinado,\n"+
				"zeus_log_fight.idAsesinado,\n"+
				"zeus_log_fight.tipPelea\n"+
			")VALUES(\n"+
				"(SELECT characters.char_name FROM characters WHERE characters.charId = idAtaca),\n"+
				"idAtaca,\n"+
				"(SELECT characters.char_name FROM characters WHERE characters.charId = idAsesi),\n"+
				"idAsesi,\n"+
				"TipoPelea\n"+
			");\n"+
			"SELECT 'cor';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF idAtaca = -100 THEN\n"+
		"SET @Consulta = CONCAT('SELECT COUNT(*) INTO @Cantidad FROM characters WHERE characters.char_name LIKE \\'',TipoPelea,'\\'');\n"+
		"PREPARE SMTP FROM @Consulta;\n"+
		"EXECUTE SMTP;\n"+
		"IF @Cantidad >0 THEN\n"+
			"IF @Cantidad > 10 THEN\n"+
				"SELECT 'err',CONCAT('Sea mós especifico');\n"+
			"ELSE\n"+
				"SELECT characters.char_name, characters.charId FROM characters WHERE characters.char_name LIKE TipoPelea;\n"+
			"END IF;\n"+
		"ELSE\n"+
			"SELECT 'err','No hay Resultados en la Busqueda';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF idAtaca = -101 THEN\n"+
		"SELECT characters.charId INTO @IDCHAR FROM characters WHERE characters.char_name = TipoPelea;\n"+
		"IF EXISTS(SELECT zeus_log_fight.Asesinado FROM zeus_log_fight WHERE zeus_log_fight.idAtacante = @IDCHAR OR zeus_log_fight.idAsesinado = @IDCHAR) THEN\n"+
			"SELECT\n"+
				"zeus_log_fight.Atacante,\n"+
				"zeus_log_fight.Asesinado,\n"+
				"zeus_log_fight.veces,\n"+
				"zeus_log_fight.tipPelea\n"+
			"FROM\n"+
				"zeus_log_fight\n"+
			"WHERE\n"+
				"zeus_log_fight.idAtacante = @IDCHAR\n"+
				"OR\n"+
				"zeus_log_fight.idAsesinado = @IDCHAR;\n"+
		"ELSE\n"+
			"SELECT 'err',CONCAT('No hay información de pvp o PK del Char ', TipoPelea);\n"+
		"END IF;\n"+
	"END IF;" + "END";

	private final String SQL_SP_npc_busqueda_resul = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_NPC_BUSQUEDA_RESUL`(IN tipo SMALLINT, IN IDChar BIGINT)\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"IF EXISTS(SELECT * FROM zeus_evento_in) THEN\n"+
			"SELECT\n"+
				"IFNULL((SELECT characters.char_name FROM characters WHERE characters.charId = zeus_evento_in.idCHAR),'unknown') as 'Nombre',\n"+
				"zeus_evento_in.fechahora,\n"+
				"get_concatenaciones(2,zeus_evento_in.idNPC),\n"+
				"zeus_evento_in.idNPC,\n"+
				"zeus_evento_in.id\n"+
			"FROM\n"+
				"zeus_evento_in;\n"+
		"ELSE\n"+
			"SELECT 'err';\n"+
		"END IF;\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_NPC_Evento = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_NPC_EVENTO`(IN tipo SMALLINT, IN idNPCIN INTEGER, IN idCHARIN BIGINT, IN CantidadNPC SMALLINT)\n"+
"BEGIN\n"+
	"DECLARE IDDAR INTEGER;\n"+
	"SET IDDAR = -1;\n"+
	"IF tipo = 1 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idNPC = idNPCIN) THEN\n"+
			"SELECT 'err','Que haces! Tratas de Robarme? Andate de Aqui, no tengo nada para ti';\n"+
		"ELSE\n"+
			"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idCHAR = idCHARIN) THEN\n"+
				"SELECT 'err','Asó que eres tu el Ladron que asalto a mi Hermano!! Andate de aqui antes que te mate';\n"+
			"ELSE\n"+
				"SET IDDAR = func_randon_Rango(CantidadNPC);\n"+
				"INSERT INTO zeus_evento_in(\n"+
					"zeus_evento_in.idNPC,\n"+
					"zeus_evento_in.idCHAR,\n"+
					"zeus_evento_in.fechahora,\n"+
					"zeus_evento_in.idPremioDado\n"+
				")VALUES(\n"+
					"idNPCIN,\n"+
					"idCHARIN,\n"+
					"NOW(),\n"+
					"IDDAR\n"+
				");\n"+
				"SELECT 'cor',IDDAR;\n"+
			"END IF;\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_premios.idItem FROM zeus_evento_premios) THEN\n"+
			"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idNPC = idNPCIN) THEN\n"+
				"SELECT 'err','Que haces! Tratas de Robarme? Andate de Aqui, no tengo nada para ti';\n"+
			"ELSE\n"+
				"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idCHAR = idCHARIN) THEN\n"+
					"SELECT 'err','Asó que eres tu el Ladron que asalto a mi Hermano!! Andate de aqui antes que te mate';\n"+
				"ELSE\n"+
					"IF EXISTS(SELECT zeus_evento_premios.idItem FROM zeus_evento_premios WHERE zeus_evento_premios.estado = 'NO') THEN\n"+
									"WHILE IDDAR = -1 DO\n"+
										"SET @IDTemp = func_randon_Rango(CantidadNPC + 1);\n"+
										"IF EXISTS(SELECT zeus_evento_premios.idPremio FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = @IDTemp AND zeus_evento_premios.estado = 'NO') THEN\n"+
											"SET IDDAR = @IDTemp;\n"+
											"END IF;\n"+
									"END WHILE;\n"+
										"INSERT INTO zeus_evento_in(\n"+
											"zeus_evento_in.idNPC,\n"+
											"zeus_evento_in.idCHAR,\n"+
											"zeus_evento_in.fechahora,\n"+
											"zeus_evento_in.idPremioDado\n"+
										")VALUES(\n"+
											"idNPCIN,\n"+
											"idCHARIN,\n"+
											"NOW(),\n"+
											"IDDAR\n"+
										");\n"+
									"SELECT 'cor',zeus_evento_premios.idItem, zeus_evento_premios.Cantidad FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = IDDAR;\n"+
									"UPDATE zeus_evento_premios SET zeus_evento_premios.estado = \"SI\", zeus_evento_premios.npcID = idNPCIN WHERE zeus_evento_premios.idPremio = IDDAR;\n"+
					"ELSE\n"+
						"SELECT 'err','Error de Configuración. No hay Premios Libres.';\n"+
					"END IF;\n"+
				"END IF;\n"+
			"END IF;\n"+
		"ELSE\n"+
			"SELECT 'err','Error de Configuración. No hay Premios Definidos.';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_premios.idItem FROM zeus_evento_premios) THEN\n"+
			"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idNPC = idNPCIN) THEN\n"+
				"SELECT characters.char_name INTO @NOMBRECHAR FROM characters WHERE characters.charId = (SELECT zeus_evento_in.idCHAR FROM zeus_evento_in WHERE zeus_evento_in.idNPC = idNPCIN);\n"+
				"SELECT 'err','NPC',@NOMBRECHAR;\n"+
			"ELSE\n"+
				"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.idCHAR = idCHARIN) THEN\n"+
					"SELECT 'err','CHAR','';\n"+
				"ELSE\n"+
					"SELECT 'cor','','';\n"+
				"END IF;\n"+
			"END IF;\n"+
		"ELSE\n"+
			"SELECT 'err','Error de Configuración. No hay Premios Definidos.';\n"+
		"END IF;\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_Evento_operacion = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_NPC_EVENTO_OPERACIONES`(IN tipo SMALLINT, IN IDItemIN INTEGER, IN CantidadIN INTEGER, IN idOpera INTEGER)\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_in.id FROM zeus_evento_in WHERE zeus_evento_in.id = idOpera) THEN\n"+
			"SELECT zeus_evento_in.idNPC INTO @IDNPC FROM zeus_evento_in WHERE zeus_evento_in.id = idOpera;\n"+
			"DELETE FROM zeus_evento_in WHERE zeus_evento_in.id = idOpera;\n"+
			"UPDATE zeus_evento_premios SET zeus_evento_premios.estado = \"NO\", zeus_evento_premios.npcID = 0 WHERE zeus_evento_premios.npcID = @IDNPC;\n"+
			"SELECT 'cor','Información de Premio Borrada con Exito',@IDNPC;\n"+
		"ELSE\n"+
			"SELECT 'err','No se pudo encontrar la Información Solicitada';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_premios.Cantidad FROM zeus_evento_premios) THEN\n"+
			"SELECT\n"+
				"'cor',\n"+
				"zeus_evento_premios.idPremio,\n"+
				"zeus_evento_premios.idItem,\n"+
				"zeus_evento_premios.Cantidad,\n"+
				"zeus_evento_premios.estado,\n"+
				"zeus_evento_premios.npcID\n"+
			"FROM\n"+
				"zeus_evento_premios\n"+
			"ORDER BY zeus_evento_premios.idPremio ASC;\n"+
		"ELSE\n"+
			"SELECT 'err';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_premios.idItem FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = idOpera) THEN\n"+
			"DELETE FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = idOpera;\n"+
			"SELECT 'cor',CONCAT('Familia Eliminada con ID=',idOpera);\n"+
		"ELSE\n"+
			"SELECT 'err','La familia no Existe.';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 4 THEN\n"+
			"IF EXISTS(SELECT zeus_evento_premios.idItem FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = idOpera AND zeus_evento_premios.idItem = IDItemIN) THEN\n"+
				"UPDATE zeus_evento_premios SET zeus_evento_premios.Cantidad = CantidadIN WHERE zeus_evento_premios.idPremio = idOpera AND zeus_evento_premios.idItem = IDItemIN;\n"+
				"SELECT 'cor','Premio modificado con óxito';\n"+
			"ELSE\n"+
				"INSERT INTO zeus_evento_premios(\n"+
					"zeus_evento_premios.idPremio,\n"+
					"zeus_evento_premios.Cantidad,\n"+
					"zeus_evento_premios.idItem)\n"+
				"VALUES(\n"+
					"idOpera,\n"+
					"CantidadIN,\n"+
					"IDItemIN\n"+
				");\n"+
				"SELECT 'cor','Premio ingresado con óxito';\n"+
			"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 5 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_premios.Cantidad FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = idOpera ) THEN\n"+
			"SELECT\n"+
				"'cor',\n"+
				"zeus_evento_premios.idPremio,\n"+
				"zeus_evento_premios.idItem,\n"+
				"zeus_evento_premios.Cantidad,\n"+
				"zeus_evento_premios.estado,\n"+
				"zeus_evento_premios.npcID\n"+
			"FROM\n"+
				"zeus_evento_premios\n"+
			"WHERE\n"+
				"zeus_evento_premios.idPremio = idOpera\n"+
			"ORDER BY zeus_evento_premios.idPremio ASC;\n"+
		"ELSE\n"+
			"SELECT 'err';\n"+
		"END IF;\n"+
	"END IF;\n"+

	"IF tipo = 6 THEN\n"+
		"IF EXISTS(SELECT zeus_evento_premios.idItem FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = idOpera AND zeus_evento_premios.idItem = IDItemIN) THEN\n"+
			"DELETE FROM zeus_evento_premios WHERE zeus_evento_premios.idPremio = idOpera AND zeus_evento_premios.idItem = IDItemIN;\n"+
			"SELECT 'cor','El item de la Familia seleccionada a sido Eliminado con óxito';\n"+
		"ELSE\n"+
			"SELECT 'err','El item de la Familia no pudo ser localizado';\n"+
		"END IF;\n"+
	"END IF;\n"+

	"IF tipo = 7 THEN\n"+
		"SELECT b.idPremio, (SELECT COUNT(a.idPremio) FROM zeus_evento_premios a WHERE a.idPremio = b.idPremio) FROM zeus_evento_premios b GROUP BY b.idPremio;\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_pin = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_PIN`(IN tipo SMALLINT, IN idChar BIGINT, IN PININ VARCHAR(8), IN NUEVOPIN VARCHAR(8))\n"+
"BEGIN\n"+
	"DECLARE INTVECES INTEGER;\n"+
	"SET INTVECES = 3;\n"+
	"IF tipo = 1 THEN\n"+
		"IF EXISTS(SELECT zeus_char_config.idchar FROM zeus_char_config WHERE zeus_char_config.idchar = idChar AND zeus_char_config.pinCode = PININ) THEN\n"+
			"SELECT 'cor';\n"+
		"ELSE\n"+
			"SELECT 'err','Código PIN Incorrecto';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"IF EXISTS(SELECT zeus_char_config.idchar FROM zeus_char_config WHERE zeus_char_config.idchar = idChar AND zeus_char_config.pinCode = PININ) THEN\n"+
			"UPDATE zeus_char_config SET zeus_char_config.pinCode = NUEVOPIN WHERE zeus_char_config.idchar = idChar AND zeus_char_config.pinCode = PININ;\n"+
			"SELECT 'cor','PIN Cambiado con Exito';\n"+
		"ELSE\n"+
			"SELECT 'err','No Ingreso Correctamente su PIN actual';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"UPDATE zeus_char_config SET zeus_char_config.idchar = \"9876\" WHERE zeus_char_config.idchar = idChar;\n"+
		"SELECT 'cor','';\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_shop = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_shop`(IN tipo SMALLINT, IN idShop INTEGER, IN idSeccion INTEGER, IN Nombre VARCHAR(80), IN Descripc VARCHAR(80), IN Imagen VARCHAR(80), IN tipoLink VARCHAR(30), IN idArchivo VARCHAR(50), IN Posicion INTEGER, IN idItemtoShow INTEGER)\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"SET @Posicion = 0;\n"+
		"IF EXISTS(SELECT zeus_shop.id FROM zeus_shop WHERE zeus_shop.idsec = idSeccion) THEN\n"+
			"SELECT MAX(zeus_shop.pos) INTO @Posicion FROM zeus_shop WHERE zeus_shop.idsec = idSeccion LIMIT 1;\n"+
		"ELSE\n"+
			"SET @Posicion = 0;\n"+
		"END IF;\n"+

		"INSERT INTO zeus_shop(\n"+
			"zeus_shop.nom,\n"+
			"zeus_shop.descrip,\n"+
			"zeus_shop.ima,\n"+
			"zeus_shop.tip,\n"+
			"zeus_shop.idarch,\n"+
			"zeus_shop.idsec,\n"+
			"zeus_shop.pos,\n"+
			"zeus_shop.idItemShow\n"+
		")VALUES(\n"+
			"Nombre,\n"+
			"Descripc,\n"+
			"Imagen,\n"+
			"tipoLink,\n"+
			"idArchivo,\n"+
			"idSeccion,\n"+
			"@Posicion + 1,\n"+
			"idItemtoShow\n"+
		");\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"SELECT zeus_shop.pos, zeus_shop.idsec, zeus_shop.id INTO @ActPos, @IDSec, @IDShop FROM zeus_shop WHERE zeus_shop.id = idShop;\n"+
		"IF Nombre = 'UP' THEN\n"+
			"IF @ActPos = 1 THEN\n"+
				"SELECT 'err';\n"+
			"ELSE\n"+
				"IF(SELECT zeus_shop.id FROM zeus_shop WHERE zeus_shop.pos < @ActPos AND zeus_shop.idsec = @IDSec ORDER BY zeus_shop.pos DESC LIMIT 1) THEN\n"+
					"SELECT zeus_shop.id, zeus_shop.pos INTO @IDMODIF, @POSIMODIF FROM zeus_shop WHERE zeus_shop.pos < @ActPos AND zeus_shop.idsec = @IDSec ORDER BY zeus_shop.pos DESC LIMIT 1;\n"+
					"UPDATE zeus_shop SET zeus_shop.pos = @POSIMODIF WHERE zeus_shop.id = @IDShop;\n"+
					"UPDATE zeus_shop SET zeus_shop.pos = @ActPos WHERE zeus_shop.id = @IDMODIF;\n"+
					"SELECT 'cor';\n"+
				"ELSE\n"+
					"SELECT 'err';\n"+
				"END IF;\n"+
			"END IF;\n"+
		"END IF;\n"+
		"IF Nombre = 'DOWN' THEN\n"+
			"IF(SELECT zeus_shop.id FROM zeus_shop WHERE zeus_shop.pos > @ActPos AND zeus_shop.idsec = @IDSec ORDER BY zeus_shop.pos ASC LIMIT 1) THEN\n"+
				"SELECT zeus_shop.id, zeus_shop.pos INTO @IDMODIF, @POSIMODIF FROM zeus_shop WHERE zeus_shop.pos > @ActPos AND zeus_shop.idsec = @IDSec ORDER BY zeus_shop.pos ASC LIMIT 1;\n"+
					"UPDATE zeus_shop SET zeus_shop.pos = @POSIMODIF WHERE zeus_shop.id = @IDShop;\n"+
					"UPDATE zeus_shop SET zeus_shop.pos = @ActPos WHERE zeus_shop.id = @IDMODIF;\n"+
					"SELECT 'cor';\n"+
			"ELSE\n"+
				"SELECT 'err';\n"+
			"END IF;\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"DELETE FROM zeus_shop WHERE zeus_shop.id = idShop;\n"+
		"DELETE FROM zeus_shop WHERE zeus_shop.idsec = idShop;\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_teleport = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_teleport`(IN tipo SMALLINT, IN idSeccion INTEGER, IN NOM_IN VARCHAR(80), IN SeccionIN VARCHAR(20), IN DescripIN VARCHAR(30), IN C_X INTEGER, IN C_Y INTEGER, IN C_Z INTEGER, IN CAN_FLAG VARCHAR(8), IN CAN_KARMA VARCHAR(8), IN LVL INTEGER, IN ONLY_NOBLE VARCHAR(8), IN idTeleport INTEGER, IN canDualBox VARCHAR(6))\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"SELECT\n"+
			"zeus_teleport.id,\n"+
			"zeus_teleport.nom,\n"+
			"zeus_teleport.descrip,\n"+
			"zeus_teleport.idsec,\n"+
			"zeus_teleport.x,\n"+
			"zeus_teleport.y,\n"+
			"zeus_teleport.z,\n"+
			"zeus_teleport.cangoFlag,\n"+
			"zeus_teleport.cangoKarma,\n"+
			"zeus_teleport.lvlup\n"+
		"FROM\n"+
			"zeus_teleport\n"+
		"ORDER BY zeus_teleport.idsec, zeus_teleport.pos ASC;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"IF EXISTS(SELECT zeus_teleport.id FROM zeus_teleport WHERE zeus_teleport.idsec = idSeccion) THEN\n"+
			"SELECT MAX(zeus_teleport.pos) INTO @Posicion FROM zeus_teleport WHERE zeus_teleport.idsec = idSeccion LIMIT 1;\n"+
		"ELSE\n"+
			"SET @Posicion = 0;\n"+
		"END IF;\n"+
		"SET @TipoCorrecion = \"\";\n"+
		"IF(SeccionIN = \"SECTION\")THEN\n"+
		"SET @TipoCorrecion = \"secc\";\n"+
		"ELSE\n"+
		"SET @TipoCorrecion = SeccionIN;\n"+
		"END IF;\n"+
		"INSERT INTO zeus_teleport(\n"+
			"zeus_teleport.nom,\n"+
			"zeus_teleport.descrip,\n"+
			"zeus_teleport.tip,\n"+
			"zeus_teleport.x,\n"+
			"zeus_teleport.y,\n"+
			"zeus_teleport.z,\n"+
			"zeus_teleport.cangoFlag,\n"+
			"zeus_teleport.cangoKarma,\n"+
			"zeus_teleport.lvlup,\n"+
			"zeus_teleport.idsec,\n"+
			"zeus_teleport.forNoble,\n"+
			"zeus_teleport.pos,\n"+
			"zeus_teleport.dualbox)\n"+
		"VALUES(\n"+
			"NOM_IN,\n"+
			"DescripIN,\n"+
			"@TipoCorrecion,\n"+
			"C_X,\n"+
			"C_Y,\n"+
			"C_Z,\n"+
			"CAN_FLAG,\n"+
			"CAN_KARMA,\n"+
			"LVL,\n"+
			"idSeccion,\n"+
			"ONLY_NOBLE,\n"+
			"(@Posicion + 1),\n"+
			"canDualBox\n"+
			");\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"IF EXISTS(SELECT zeus_teleport.id FROM zeus_teleport WHERE zeus_teleport.id = idSeccion) THEN\n"+
			"DELETE FROM zeus_teleport WHERE zeus_teleport.id = idSeccion;\n"+
			"DELETE FROM zeus_teleport WHERE zeus_teleport.idsec = idSeccion;\n"+
		"ELSE\n"+
			"SELECT 'err';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 4 THEN\n"+
		"UPDATE zeus_teleport SET\n"+
			"zeus_teleport.nom = NOM_IN,\n"+
			"zeus_teleport.descrip = DescripIN,\n"+
			"zeus_teleport.tip = SeccionIN,\n"+
			"zeus_teleport.x = if(C_X=0, zeus_teleport.x,C_X),\n"+
			"zeus_teleport.y = if(C_Y=0, zeus_teleport.y,C_Y),\n"+
			"zeus_teleport.z = if(C_z=0, zeus_teleport.z,C_Z),\n"+
			"zeus_teleport.cangoFlag = CAN_FLAG,\n"+
			"zeus_teleport.cangoKarma = CAN_KARMA,\n"+
			"zeus_teleport.lvlup = LVL,\n"+
			"zeus_teleport.forNoble = ONLY_NOBLE,\n"+
			"zeus_teleport.dualbox = canDualBox\n"+
		"WHERE\n"+
			"zeus_teleport.id = idTeleport;\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_teleport_opera = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_teleport_opera`(IN tipo SMALLINT, IN idTele INTEGER, IN TipS VARCHAR(10))\n\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"SELECT zeus_teleport.pos, zeus_teleport.idsec, zeus_teleport.id INTO @ActPos, @IDSec, @IDTeleport FROM zeus_teleport WHERE zeus_teleport.id = idTele;\n"+
		"IF TipS = 'UP' THEN\n"+
			"IF @ActPos = 1 THEN\n"+
				"SELECT 'err';\n"+
			"ELSE\n"+
				"IF(SELECT zeus_teleport.id FROM zeus_teleport WHERE zeus_teleport.pos < @ActPos AND zeus_teleport.idsec = @IDSec ORDER BY zeus_teleport.pos DESC LIMIT 1) THEN\n"+
					"SELECT zeus_teleport.id, zeus_teleport.pos INTO @IDMODIF, @POSIMODIF FROM zeus_teleport WHERE zeus_teleport.pos < @ActPos AND zeus_teleport.idsec = @IDSec ORDER BY zeus_teleport.pos DESC LIMIT 1;\n"+
					"UPDATE zeus_teleport SET zeus_teleport.pos = @POSIMODIF WHERE zeus_teleport.id = @IDTeleport;\n"+
					"UPDATE zeus_teleport SET zeus_teleport.pos = @ActPos WHERE zeus_teleport.id = @IDMODIF;\n"+
					"SELECT 'cor';\n"+
				"ELSE\n"+
					"SELECT 'err';\n"+
				"END IF;\n"+
			"END IF;\n"+
		"END IF;\n"+

		"IF TipS = 'DOWN' THEN\n"+
			"IF(SELECT zeus_teleport.id FROM zeus_teleport WHERE zeus_teleport.pos > @ActPos AND zeus_teleport.idsec = @IDSec ORDER BY zeus_teleport.pos ASC LIMIT 1) THEN\n"+
				"SELECT zeus_teleport.id, zeus_teleport.pos INTO @IDMODIF, @POSIMODIF FROM zeus_teleport WHERE zeus_teleport.pos > @ActPos AND zeus_teleport.idsec = @IDSec ORDER BY zeus_teleport.pos ASC LIMIT 1;\n"+
					"UPDATE zeus_teleport SET zeus_teleport.pos = @POSIMODIF WHERE zeus_teleport.id = @IDTeleport;\n"+
					"UPDATE zeus_teleport SET zeus_teleport.pos = @ActPos WHERE zeus_teleport.id = @IDMODIF;\n"+
					"SELECT 'cor';\n"+
			"ELSE\n"+
				"SELECT 'err';\n"+
			"END IF;\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"DELETE FROM zeus_teleport WHERE zeus_teleport.id = idTele;\n"+
		"SELECT 'cor';\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_top_pvppk = "CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_top_pvppk`(IN tipo SMALLINT, IN charId VARCHAR(250), IN TopBus INTEGER)\n"+
"BEGIN\n"+
"DECLARE PVP, PK, POSIPVP, POSIPK, MAXIMO, POSICION, ESTADO INTEGER;\n"+
"DECLARE idChar BIGINT;\n"+
"SET @FechaActual = CURRENT_DATE();\n"+
"SELECT COUNT(characters.`online`) INTO @CantidadOnline FROM characters WHERE characters.`online` = 1;\n"+
	"SET MAXIMO = TopBus;\n"+
	"SELECT characters.charId INTO idChar FROM characters WHERE characters.char_name = charId;\n"+
	"SELECT characters.pkkills, characters.pvpkills INTO PK, PVP FROM characters WHERE characters.charId = idChar;\n"+
	"SELECT count(*) INTO POSIPK FROM characters WHERE characters.pkkills >= PK AND characters.accesslevel = 0 ;\n"+
	"SELECT count(*) INTO POSIPVP FROM characters WHERE characters.pvpkills >= PVP AND characters.accesslevel = 0 ;\n"+

	"IF POSIPK < POSIPVP THEN\n"+
		"SET POSICION = POSIPK;\n"+
	"ELSE\n"+
		"SET POSICION = POSIPVP;\n"+
	"END IF;\n"+

	"IF(POSICION <= MAXIMO) THEN\n"+
		"IF (PK = 0 AND PVP = 0) THEN\n"+
			"SET ESTADO =0;\n"+
		"ELSE\n"+
			"SET ESTADO = 1;\n"+
		"END IF;\n"+
	"ELSE\n"+
		"SET ESTADO = 0;\n"+
	"END IF;\n"+

	"IF EXISTS(SELECT characters.charId FROM characters WHERE characters.accesslevel >0 AND characters.charId = idChar ) THEN\n"+
		"SET ESTADO = 0;\n"+
	"END IF;\n"+

	"SET POSICION = POSICION;\n"+

	"SELECT ESTADO as 'resp' , POSICION as 'posi';\n"+
"END";

	private final String SQL_SP_zeus_config="CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_zeus_config`(IN tipo SMALLINT, IN idProce INTEGER, IN Param1 VARCHAR(350), IN Param2 VARCHAR(350), IN Param3 VARCHAR(350))\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"UPDATE zeus_config_seccion SET zeus_config_seccion.param = (IF(zeus_config_seccion.param = 'true','false','true')) WHERE zeus_config_seccion.id = idProce;\n"+
		"SELECT 'cor','';\n"+
	"END IF;\n"+
	"IF tipo = 2 THEN\n"+
		"UPDATE zeus_config_seccion SET zeus_config_seccion.param = Param1 WHERE zeus_config_seccion.id = idProce;\n"+
		"SELECT 'cor','';\n"+
	"END IF;\n"+
	"IF tipo = 3 THEN\n"+
		"UPDATE zeus_config_seccion SET zeus_config_seccion.param = CONCAT(zeus_config_seccion.param , IF(LENGTH(zeus_config_seccion.param)>0,\";\",\"\") ,Param1) WHERE zeus_config_seccion.id = idProce;\n"+
		"SELECT 'cor','';\n"+
	"END IF;\n"+
	"IF tipo = 4 THEN\n"+
		"UPDATE zeus_config_seccion SET zeus_config_seccion.param = CONCAT(zeus_config_seccion.param , IF(LENGTH(zeus_config_seccion.param)>0,\",\",\"\") ,Param1) WHERE zeus_config_seccion.id = idProce;\n"+
		"SELECT 'cor','';\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_zeus_voto ="CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` PROCEDURE `sp_zeus_voto`(IN idCharIN INT, IN votoIN SMALLINT, IN WebIN SMALLINT)\n"+
"BEGIN\n"+
	"DECLARE ActualUnix, UnixLimite INTEGER;\n"+
	"SET ActualUnix = UNIX_TIMESTAMP();\n"+
	"SET UnixLimite = (2 * 24 * 60 * 60) + ActualUnix; #172800\n"+

	"IF EXISTS(SELECT zeus_votos.idChar FROM zeus_votos WHERE zeus_votos.web = WebIN AND zeus_votos.voto = votoIN AND zeus_votos.fecha < ActualUnix) OR NOT EXISTS(SELECT zeus_votos.idChar FROM zeus_votos WHERE zeus_votos.voto = votoIN AND zeus_votos.web = WebIN) THEN\n"+
		"IF EXISTS(SELECT zeus_votos.idChar FROM zeus_votos WHERE zeus_votos.idChar = idCharIN AND zeus_votos.web = WebIN) THEN\n"+
			"UPDATE zeus_votos SET zeus_votos.fecha = UnixLimite, zeus_votos.voto = votoIN WHERE zeus_votos.idChar = idCharIN AND zeus_votos.web = WebIN;\n"+
		"ELSE\n"+
			"INSERT INTO zeus_votos(zeus_votos.fecha,zeus_votos.idChar,zeus_votos.voto, zeus_votos.web) VALUES (UnixLimite,idCharIN,votoIN, WebIN);\n"+
		"END IF;\n"+
		"SELECT 'cor';\n"+
	"ELSE\n"+
		"SELECT 'err';\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_get_concatenaciones ="CREATE DEFINER=`"+general.getUser()+"`@`"+general.getHost()+"` FUNCTION `get_concatenaciones`(`tipo` smallint,`buscar` varchar(200)) RETURNS varchar(280) CHARSET latin1 DETERMINISTIC\n"+
"BEGIN\n"+
	"IF tipo = 2 THEN\n"+
		"IF EXISTS(SELECT custom_spawnlist.count FROM custom_spawnlist WHERE custom_spawnlist.npc_templateid = buscar) THEN\n"+
			"SELECT CONCAT(custom_spawnlist.locx, \",\" , custom_spawnlist.locy, \",\" , custom_spawnlist.locz) INTO @Retorno FROM custom_spawnlist WHERE custom_spawnlist.npc_templateid = buscar;\n"+
		"ELSE\n"+
			"SET @Retorno = 'err';\n"+
		"END IF;\n"+
	"END IF;\n"+
	"IF tipo = 1 THEN\n"+
		"SET @contatenando = \"\";\n"+
	"END IF;\n"+

	"IF tipo = 3 THEN\n"+
		"IF EXISTS(SELECT spawnlist.count FROM spawnlist WHERE spawnlist.npc_templateid = buscar) THEN\n"+
			"SELECT CONCAT(spawnlist.locx,\",\",spawnlist.locy,\",\",spawnlist.locz) INTO @Retorno FROM spawnlist WHERE spawnlist.npc_templateid = buscar LIMIT 1;\n"+
		"ELSE\n"+
			"IF EXISTS(SELECT custom_spawnlist.count FROM custom_spawnlist WHERE custom_spawnlist.npc_templateid = buscar) THEN\n"+
				"SELECT CONCAT(custom_spawnlist.locx,\",\",custom_spawnlist.locy,\",\",custom_spawnlist.locz) INTO @Retorno FROM custom_spawnlist WHERE custom_spawnlist.npc_templateid = buscar LIMIT 1;\n"+
			"ELSE\n"+
				"IF EXISTS(SELECT raidboss_spawnlist.boss_id FROM raidboss_spawnlist WHERE raidboss_spawnlist.boss_id = buscar) THEN\n"+
					"SELECT CONCAT(raidboss_spawnlist.loc_x,\",\",raidboss_spawnlist.loc_y,\",\",raidboss_spawnlist.loc_z) INTO @Retorno FROM raidboss_spawnlist WHERE raidboss_spawnlist.boss_id = buscar;\n"+
				"ELSE\n"+
					"SET @Retorno = \"err\";\n"+
				"END IF;\n"+
			"END IF;\n"+
		"END IF;\n"+
	"END IF;\n"+

	"RETURN @Retorno;\n"+
"END";



	/**Febrero 2014***/

	private final String SQL_SP_zeus_dona_shop = "CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_dona_shop`(IN tipo SMALLINT, IN nombre VARCHAR(80), IN tipoAccionIN VARCHAR(20), IN P1 VARCHAR(100), IN P2 VARCHAR(100), IN idSeccion INTEGER)\n"+
"BEGIN\n"+
	"IF tipo = 1 THEN\n"+
		"SELECT\n"+
			"zeus_dona_shop.nom,\n"+
			"zeus_dona_shop.tipoAccion,\n"+
			"zeus_dona_shop.param1,\n"+
			"zeus_dona_shop.param2,\n"+
			"zeus_dona_shop.DC_Count,\n"+
			"zeus_dona_shop.idSec,\n"+
			"zeus_dona_shop.id\n,"+
			"zeus_dona_shop.posi \n"+
		"FROM\n"+
			"zeus_dona_shop\n"+
		"ORDER BY\n"+
			 "zeus_dona_shop.nom ASC;\n"+
	"END IF;\n"+
"END";

	private final String SQL_SP_char_config = "CREATE DEFINER = `"+ general.getUser() +"`@`"+ general.getHost() + "` PROCEDURE `sp_char_config`(IN tipo SMALLINT, IN SECIN INTEGER, IN idCharIN INTEGER, IN ValueIN VARCHAR(20))\n"+
					"BEGIN\n"+
						"IF tipo = 1 THEN\n"+
							"IF NOT EXISTS(SELECT zeus_char_config.idchar FROM zeus_char_config WHERE zeus_char_config.idchar = idCharIN) THEN\n"+
								"INSERT INTO zeus_char_config(zeus_char_config.idchar) VALUES ( idCharIN );\n"+
							"END IF;\n"+
							"SELECT * FROM zeus_char_config WHERE zeus_char_config.idchar = idCharIN;\n"+
						"END IF;\n"+
					"END";

	/**
	 *
	 * Arriba termina la Creación de procedimientos Almacenados.
	 *
	 * */

	private void crearSP(){
		sendSp("DROP PROCEDURE IF EXISTS `sp_augment`",false);
		sendSp(SQL_SP_augment);
		
		sendSp("DROP PROCEDURE IF EXISTS `sp_antibot_blacklist`",false);
		sendSp(SQL_SP_antibot_blacklist);

		sendSp("DROP PROCEDURE IF EXISTS `sp_buffAIO`",false);
		sendSp(SQL_SP_buffAIO);

		sendSp("DROP PROCEDURE IF EXISTS `sp_bugg_ingreso`",false);
		sendSp(SQL_SP_bug_ingreso);

		sendSp("DROP PROCEDURE IF EXISTS `sp_creditosNPC`",false);
		sendSp(SQL_SP_creditosNPC);

		sendSp("DROP PROCEDURE IF EXISTS `sp_dressme`",false);
		sendSp(SQL_SP_dressme);

		sendSp("DROP PROCEDURE IF EXISTS `sp_evento_inicial`",false);
		sendSp(SQL_SP_Evento);

		sendSp("DROP PROCEDURE IF EXISTS `sp_existe_nombre`",false);
		sendSp(SQL_SP_existe_nombre);

		sendSp("DROP FUNCTION IF EXISTS `func_randon_rango`",false);
		sendSp(SQL_SP_randon_rango);

		sendSp("DROP FUNCTION IF EXISTS `func_id_dona_espera`",false);
		sendSp(SQL_FUNC_func_id_zeus_dona_espera);

		sendSp("DROP PROCEDURE IF EXISTS `sp_get_augment`",false);
		sendSp(SQL_SP_get_augment);

		sendSp("DROP PROCEDURE IF EXISTS `sp_get_raidboss_info`",false);
		sendSp(SQL_SP_get_raidboss);

		sendSp("DROP PROCEDURE IF EXISTS `sp_ingresa_dona`",false);
		sendSp(SQL_SP_Ingresa_Dona);

		sendSp("DROP PROCEDURE IF EXISTS `sp_ip`",false);
		sendSp(SQL_SP_ip);

		sendSp("DROP PROCEDURE IF EXISTS `sp_lista_log_pvp`",false);
		sendSp(SQL_SP_lista_log_pvp);

		sendSp("DROP PROCEDURE IF EXISTS `sp_log_fight`",false);
		sendSp(SQL_SP_log_fight);

		sendSp("DROP PROCEDURE IF EXISTS `sp_NPC_BUSQUEDA_RESUL`",false);
		sendSp(SQL_SP_npc_busqueda_resul);

		sendSp("DROP PROCEDURE IF EXISTS `sp_NPC_EVENTO`",false);
		sendSp(SQL_SP_NPC_Evento);

		sendSp("DROP PROCEDURE IF EXISTS `sp_NPC_EVENTO_OPERACIONES`",false);
		sendSp(SQL_SP_Evento_operacion);

		sendSp("DROP PROCEDURE IF EXISTS `sp_PIN`",false);
		sendSp(SQL_SP_pin);

		sendSp("DROP PROCEDURE IF EXISTS `sp_shop`",false);
		sendSp(SQL_SP_shop);

		sendSp("DROP PROCEDURE IF EXISTS `sp_teleport`",false);
		sendSp(SQL_SP_teleport);

		sendSp("DROP PROCEDURE IF EXISTS `sp_teleport_opera`",false);
		sendSp(SQL_SP_teleport_opera);

		sendSp("DROP PROCEDURE IF EXISTS `sp_top_pvppk`",false);
		sendSp(SQL_SP_top_pvppk);

		sendSp("DROP PROCEDURE IF EXISTS `sp_zeus_config`",false);
		sendSp(SQL_SP_zeus_config);

		sendSp("DROP PROCEDURE IF EXISTS `sp_zeus_voto`",false);
		sendSp(SQL_SP_zeus_voto);

		sendSp("DROP FUNCTION IF EXISTS `get_concatenaciones`",false);
		sendSp(SQL_SP_get_concatenaciones);

		/*Febrero 2014*/

		sendSp("DROP PROCEDURE IF EXISTS `sp_char_config`",false);
		sendSp(SQL_SP_char_config);

		sendSp("DROP PROCEDURE IF EXISTS `sp_dona_shop`",false);
		sendSp(SQL_SP_zeus_dona_shop);
		/*Febrero 2014*/
	}

	private void sendSp(String Consulta){
		sendSp(Consulta, true);
	}

	private void sendSp(String Consulta, boolean sendError){
		createTable(Consulta,sendError,true);
	}

	private void checkDonaShop(){
		String Consulta = "SELECT * from zeus_dona_shop limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Zeus Dona Shop"));
			createTable(SQL_zeus_dona_shop);
		}
	}

	private void checkConnection(){
		String Consulta = "Select * from zeus_connection limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Zeus Connection"));
			createTable(SQL_zeus_connection);
		}

	}

	private void checkAugmentData(){
		String Consulta = "select * from zeus_augment_data limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Augment Data"));
			createTable(SQL_augment_data);
		}
		try{
			createTable(SQL_AUGMENT_1,false,false);
		}catch(Exception a){
			
		}
		try{
			createTable(SQL_AUGMENT_2,false,false);
		}catch(Exception a){
			
		}
	}

	private void checkAntibot(){
		String Consulta = "select * from zeus_antibot limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Antibot"));
			createTable(SQL_antibot);
			createTable(SQL_antibot_data);
		}
	}
	
	private void checkRank(){
		String Consulta = "select * from zeus_rank_acc limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "ZeuS Rank"));
			createTable(SQL_Rank);
		}
	}

	private void checkCumulativeSubclasses() {
		String Consulta = "select * from zeus_cumulative_subclass limit 1";
		if(!sqlScript(Consulta)) {
			_log.warning(MISSING_$table$.replace("$table$", "zeus_cumulative_subclass"));
			createTable(SQL_zeus_cumulative_subclass);
		}
	}
	
	private void checkBorrowSystem() {
		String Consulta = "select * from zeus_borrow_account limit 1";
		if(!sqlScript(Consulta)) {
			_log.warning(MISSING_$table$.replace("$table$", "zeus_borrow_system"));
			createTable(SQL_zeus_Borrow_System);
		}
	}
	
	private void checkColorNameTitle(){
		String Consulta = "select * from zeus_color_name_title limit 1";
		if(!sqlScript(Consulta)) {
			_log.warning(MISSING_$table$.replace("$table$", "zeus_color_name_title"));
			createTable(SQL_zeus_color_name_title);
		}
	}
	
	private void checkDressmeItemPlayers() {
		String Consulta = "select * from zeus_dressme_items_char limit 1";
		if(!sqlScript(Consulta)) {
			_log.warning(MISSING_$table$.replace("$table$", "zeus_dressme_items_char"));
			createTable(SQL_zeus_dressme_items_char);			
		}		
	}
	
	private void checkDealyReward() {
		String Consulta = "select * from zeus_dealy_reward limit 1";
		if(!sqlScript(Consulta)) {
			_log.warning(MISSING_$table$.replace("$table$", "zeus_dealy_reward"));
			createTable(SQL_zeus_dealy_reward);			
		}		
	}
	
	private void checkWishlist() {
		//SQL_zeus_wishlist
		String Consulta = "select * from zeus_wish_list limit 1";
		if(!sqlScript(Consulta)) {
			_log.warning(MISSING_$table$.replace("$table$", "zeus_wish_list"));
			createTable(SQL_zeus_wishlist);			
		}
	}
	private void checkOldPassword(){
		String Consulta = "select * from zeus_old_password limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_old_password"));
			createTable(SQL_zeus_old_password);
		}
	}
	
	private void checkSecundaryPass(){
		String Consulta = "select * from zeus_secundary_pass limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "ZeuS Secundary pass"));
			createTable(SQL_secundary_pass);
		}
	}
	
	private void checkAntibotBlackList(){
		String Consulta = "select zeus_antibot_blacklist.id from zeus_antibot_blacklist limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "ZeuS Antibot Blacklist"));
			createTable(SQL_antibot_blacklist);
		}
	}
	
	private void checkJailBailBlackList(){
		String Consulta = "SELECT zeus_jail_bail_blacklist.idchar FROM zeus_jail_bail_blacklist limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "ZeuS Jail Bail Blacklist"));
			createTable(SQL_jail_bail_blacklist);;
		}
	}

	private void checkBuffer_buff_list(){
		String Consulta = "select * from zeus_buffer_buff_list limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Buffer List Buff"));
			createTable(SQL_buffer_list);
			createTable(SQL_Buffer_buff_list_1);
			createTable(SQL_Buffer_buff_list_2);
		}
	}
	private void checkBuffer_scheme_contents(){
		String Consulta = "select * from zeus_buffer_scheme_contents limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Buffer scheme Contents"));
			createTable(SQL_buffer_scheme_contents);
		}
	}

	private void checkBuffer_scheme_list(){
		String Consulta = "select * from zeus_buffer_scheme_list limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Buffer scheme list"));
			createTable(SQL_buffer_scheme_list);
		}
	}
	
	private void chechBuffstore(){
		String Consulta = "select * from zeus_buffstore limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "buffstore"));
			createTable(SQL_buffstore);
		}
	}
	
	private void checkAuctionsHouse(){
		String Consulta = "select idOwner from zeus_auctions_house limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "auctions_house"));
			createTable(SQL_auction_house);
		}
	}
	
	private void checkAuctionsHouse_offline(){
		String Consulta = "select idChar from zeus_auctions_house_offline limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "auctions_house_offline"));
			createTable(SQL_auction_house_offline);
		}
	}
	
	private void checkCertification(){
		String Consulta = "select idChar from zeus_certification limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_certification"));
			createTable(SQL_zeus_certification);
		}
	}
	
	private void checkOly_sch(){
		String Consulta = "select idChar from zeus_oly_sch limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "oly_sch"));
			createTable(SQL_oly_sch,false,false);
		}
	}
	
	
	private void checkOly_sch_buff(){
		String Consulta = "select idsch from zeus_oly_sch_buff limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "oly_sch_buff"));
			createTable(SQL_oly_sch_buff,false,false);
		}
	}

	
	private void checkBuff_for_aio(){
		String Consulta = "select * from zeus_buff_for_aio limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Aio"));
			createTable(SQL_buffer_aio);
			createTable(SQL_Buff_for_aio_1);
			createTable(SQL_Buff_for_aio_2);
		}
	}

	private void checkBug_Report(){
		String Consulta = "select * from zeus_bug_report limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Bug report"));
			createTable(SQL_bug_report);
		}
	}

	private void checkConfig_Seccion(){
		String Consulta = "select * from zeus_config_seccion limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Config"));
			createTable(SQL_config);
		}
		createTable(SQL_config_1,false, false);
		createTable(SQL_config_2,false, false);
		createTable(SQL_config_3,false, false);
		createTable(SQL_config_4,false, false);
		createTable(SQL_config_5,false, false);
		createTable(SQL_config_6,false, false);
		createTable(SQL_config_7,false, false);
		createTable(SQL_config_20,false, false);
	}
	
	
	private void checkBuffstoreScheme(){
		String Consulta = "select * from zeus_buffstore_scheme limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "buffstore scheme"));
			createTable(SQL_buffstore_scheme);
		}
		
		Consulta = "select * from zeus_buffstore_scheme_buff limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "buffstore scheme buff"));
			createTable(SQL_buffstore_scheme_buff);
		}
	}

	private void checkDona_Credito(){
		String Consulta = "select * from zeus_dona_creditos limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Donation"));
			createTable(SQL_dona_creditos);
		}
	}

	private void checkDressme(){
		String Consulta = "select * from zeus_dressme limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Dressme"));
			createTable(SQL_dressme);
		}
	}

	private void checkEvento(){
		String Consulta = "select * from zeus_evento limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Event"));
			createTable(SQL_Evento);
		}
		Consulta = "select * from zeus_evento_in limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Event"));
			createTable(SQL_Evento_IN);
		}
		Consulta = "select * from zeus_evento_premios limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Event"));
			createTable(SQL_EventoPremios);
		}

	}
	
	public void checkFakeHeroes(){
		String Consulta = "select * from zeus_fake_heroes limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_fake_heroes"));
			createTable(SQL_FakeHeroes);
		}		
	}
	
	public void checkFakeNpc(){
		String Consulta = "select * from zeus_fake_npc limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_fake_npc"));
			createTable(SQL_fake_npc);
		}		
	}

	public void checkIP_Block(){
		String Consulta = "select * from zeus_ipblock limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Block IP"));
			createTable(SQL_ipblock);
		}
	}

	private void checkLog_Fight(){
		String Consulta = "select * from zeus_ipblock limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Log Fight"));
			createTable(SQL_logFight);
		}
	}

	private void checkDressmeExclusiveClan(){
		//SQL_zeus_dressme_exclusive
		String Consulta = "select * from zeus_dressme_exclusive limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_dressme_exclusive"));
			createTable(SQL_zeus_dressme_exclusive);
		}		
	}
	
	private void checkbid_house(){
		String Consulta = "select * from zeus_bid_house limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_bid_house"));
			createTable(SQL_zeus_bid_house);
		}
	}
	
	private void checkbid_house_offline(){
		String Consulta = "select * from zeus_bid_house_offline limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_bid_house_offline"));
			createTable(SQL_zeus_bid_house_offline);
		}
	}
	
	private void checkbid_house_players_bids(){
		String Consulta = "select * from zeus_bid_players_bid limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_bid_players_bid"));
			createTable(SQL_zeus_bid_players_bid);
		}
	}
	
	private void checkbid_house_players_Winners(){
		String Consulta = "select * from zeus_bid_players_winners limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_bid_players_winners"));
			createTable(SQL_zeus_bid_players_winners);
		}
	}
	
	
	private void checkShop(){
		String Consulta = "select * from zeus_shop limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Shop BD"));
			createTable(SQL_shop);
			createTable(SQL_shop_1);
		}
	}

	private void checkTeleport(){
		String Consulta = "select * from zeus_teleport limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Teleport BD"));
			createTable(SQL_teleport);
			createTable(SQL_teleport_1);
			createTable(SQL_teleport_2);
		}
	}

	private void checkVotos(){
		String Consulta = "select * from zeus_votos limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "Votos Check"));
			createTable(SQL_votos);
		}
	}

	private void checkPremium(){
		String Consulta = "select * from zeus_premium limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "ZeuS Premium"));
			createTable(SQL_zeus_premium);
		}
	}

	private void checkPreDonation(){
		String Consulta = "select * from zeus_pre_donation limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_pre_donation"));
			createTable(SQL_zeus_pre_donation);
		}
	}

	private void checkBuffer(){
		String Consulta = "";
		Consulta = "select * from zeus_buff_char_sch limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_buff_char_sch"));
			createTable(SQL_Buff_Char_sch);
		}

		Consulta = "select * from zeus_buff_char_sch_buff limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_buff_char_sch_buff"));
			createTable(SQL_buff_char_sch_buff);
		}

	}

	private void checkConfigChar(){
		String Consulta = "select * from zeus_char_config limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_char_config"));
			createTable(SQL_zeus_char_config);
		}
	}
	
	private void checkPvPZone(){
		String Consulta = "select * from zeus_pvp_zone limit 1";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "pvp zone records"));
			createTable(SQL_zeus_pvp_zone);
		}		
	}

	public void checkZeuSAnnoucement(){
		String Consulta = "select * from zeus_annoucement";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "ZeuS Annoucement"));
			createTable(SQL_zeus_Annoucement);
		}
	}

	public void checkZeuSClanForo(){
		String Consulta = "select * from zeus_cb_clan_foro";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_cb_clan_foro"));
			createTable(SQL_zeus_cb_clan_foro);
		}
	}

	private void checkZeuSDonaEspera() {
		String Consulta = "select * from zeus_dona_espera";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_dona_espera"));
			createTable(SQL_zeus_dona_espera);
		}
	}

	private void checkZeuSDonaGift() {
		String Consulta = "select * from zeus_dona_gift";
		if(!sqlScript(Consulta)){
			_log.warning(MISSING_$table$.replace("$table$", "zeus_dona_gift"));
			createTable(SQL_zeus_dona_gift);
		}
	}

	public void checkBD(){
		crearSP();
		checkRank();
		checkSecundaryPass();
		checkWishlist();
		checkDealyReward();
		checkDressmeItemPlayers();
		checkCumulativeSubclasses();
		checkBorrowSystem();
		checkColorNameTitle();
		checkOldPassword();
		checkAntibot();
		checkAugmentData();
		checkZeuSDonaGift();
		checkBuffer_buff_list();
		checkBuffer_scheme_contents();
		checkBuffer_scheme_list();
		chechBuffstore();
		checkAuctionsHouse();
		checkAuctionsHouse_offline();
		checkCertification();
		checkOly_sch();
		checkOly_sch_buff();
		checkBuff_for_aio();
		checkBug_Report();
		checkConfig_Seccion();
		checkDona_Credito();
		checkDressme();
		checkDressmeExclusiveClan();
		checkEvento();
		checkLog_Fight();
		checkShop();
		checkTeleport();
		checkVotos();
		checkIP_Block();
		checkFakeHeroes();
		checkFakeNpc();
		checkBuffer();
		checkConfigChar();
		checkPvPZone();
		checkDonaShop();
		checkConnection();
		checkPremium();
		checkPreDonation();
		checkZeuSAnnoucement();
		checkZeuSClanForo();
		checkZeuSDonaEspera();
		checkAntibotBlackList();
		checkJailBailBlackList();
		checkBuffstoreScheme();
		checkbid_house();
		checkbid_house_offline();
		checkbid_house_players_bids();
		checkbid_house_players_Winners();
	}

	
	
	@SuppressWarnings("unused")
	private void sqlInsert(String Consulta){
		Connection conn = null;
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			psqry.executeUpdate();
		}catch(SQLException a){
			try{
				conn.close();
			}catch(SQLException b){

			}
		}

		try{
			conn.close();
		}catch(Exception a){

		}
	}

	@SuppressWarnings("unused")
	private boolean sqlScript(String Consulta){
		Connection conn = null;
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			psqry.executeQuery();
		}catch(SQLException a){
			try{
				conn.close();
			}catch(SQLException b){

			}
			if(a.getErrorCode()==1146){
				return false;
			}
		}

		try{
			conn.close();
		}catch(Exception a ){

		}

		return true;
	}

	private void createTable(String Sql){
		createTable(Sql,true,true);
	}

	private String getNameTable(String SQLQuery){
		return getNameTable(SQLQuery, false);
	}
	
	private String getNameTable(String SQLQuery, boolean isProcedure){
		String retorno = "";
		if(!isProcedure){
			String[]Pedaso = SQLQuery.split(" ");
			retorno = Pedaso[3].replace("`", "");
		}else{
			String[]Pedaso = SQLQuery.split(" ");
			retorno = Pedaso[3];
		}
		return retorno;
	}
	
	

	private void createTable(String Sql, boolean sendError, boolean showMensaje){
		Connection conn = null;
		PreparedStatement psqry = null;

		if(Sql.length()==0){
			return;
		}
		
		if(Sql.startsWith("INSERT IGNORE")){
			if(showMensaje){
				_log.warning("ZeuS - Create Table " +getNameTable(Sql) );
			}
		}
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Sql);
			psqry.execute();
		}catch(SQLException a){
			if(sendError && (a.getErrorCode()!= 1050) ){
				if(Sql.indexOf("CREATE TABLE")>0){
					_log.warning("ZeuS - Error create table: "+ a.getMessage() + " " + String.valueOf(a.getErrorCode()) + " ("+ getNameTable(Sql) +")");
				}else{
					_log.warning("ZeuS - Error create procedures: "+ a.getMessage() + " " + String.valueOf(a.getErrorCode()) + " ("+ getNameTable(Sql, true) +")");					
				}
			}
		}

		try{
			conn.close();
		}catch(Exception a){

		}
	}



  	public static BD getInstance()
	{
	    return SingletonHolder._instance;
	}


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder{
	    protected static final BD _instance = new BD();
	}

}
