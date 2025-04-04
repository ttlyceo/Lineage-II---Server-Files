package ZeuS.Comunidad;


import gr.sr.interf.SunriseEvents;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.EngineForm.C_cmdInfo;
import ZeuS.Comunidad.EngineForm.C_gmcommand;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Comunidad.EngineForm.v_AugmentSpecial;
import ZeuS.Comunidad.EngineForm.v_Buffer_New;
import ZeuS.Comunidad.EngineForm.v_BugReport;
import ZeuS.Comunidad.EngineForm.v_Dressme;
import ZeuS.Comunidad.EngineForm.v_ElementalSpecial;
import ZeuS.Comunidad.EngineForm.v_EnchantSpecial;
import ZeuS.Comunidad.EngineForm.v_FlagFinder;
import ZeuS.Comunidad.EngineForm.v_HeroList;
import ZeuS.Comunidad.EngineForm.v_MyInfo;
import ZeuS.Comunidad.EngineForm.v_PartyFinder;
import ZeuS.Comunidad.EngineForm.v_RaidBossInfo;
import ZeuS.Comunidad.EngineForm.v_RemoveAttribute;
import ZeuS.Comunidad.EngineForm.v_Shop;
import ZeuS.Comunidad.EngineForm.v_Teleport;
import ZeuS.Comunidad.EngineForm.v_Transformations;
import ZeuS.Comunidad.EngineForm.v_Warehouse;
import ZeuS.Comunidad.EngineForm.v_auction_house;
import ZeuS.Comunidad.EngineForm.v_augmentManager;
import ZeuS.Comunidad.EngineForm.v_bid_house;
import ZeuS.Comunidad.EngineForm.v_blacksmith;
import ZeuS.Comunidad.EngineForm.v_clasesStadistic;
import ZeuS.Comunidad.EngineForm.v_donation;
import ZeuS.Comunidad.EngineForm.v_dropsearch;
import ZeuS.Comunidad.EngineForm.C_gmlist;
import ZeuS.Comunidad.EngineForm.v_partymatching;
import ZeuS.Comunidad.EngineForm.v_profession;
import ZeuS.Comunidad.EngineForm.v_pvppkLog;
import ZeuS.Comunidad.EngineForm.v_subclass;
import ZeuS.Comunidad.EngineForm.v_symbolMaker;
import ZeuS.Comunidad.EngineForm.v_MiscelaniusOption;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.interfase.colorNameTitle;
import ZeuS.interfase.htmls;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;


public class Engine {

	private static Logger _log = Logger.getLogger(Engine.class.getName());
	
	private static Vector<String> btnImaLink = new Vector<String>();
	
	public static Vector<String> getBtnOption(){
		return btnImaLink;
	}
	
	public enum sRank{
		SHOP,
		TELEPORT,
		SEARCH_DROP,
		SEARCH_NPC
	}
	
	public static void createRank(int id, String Tipo){
		String Consulta = "INSERT INTO zeus_rank_acc (zeus_rank_acc.id , zeus_rank_acc.cant , zeus_rank_acc.tip) VALUES (?, 1, ?) "+
		"ON DUPLICATE KEY UPDATE zeus_rank_acc.cant=zeus_rank_acc.cant+1";
		
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setInt(1, id);
			ins.setString(2,Tipo);
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
	
	
	public enum enumBypass{
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
		colornametitle
	}

	public static void _Load(){
		Vector<String> _btnImaLink = new Vector<String>();
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_BUFFER_CBE)+":icon.skill1297:Buffer:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Buffer;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_BUFFER_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_PARTYFINDER_CBE)+":icon.skill1411:Go Party Leader:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Gopartyleader;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_PARTYFINDER_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_FLAGFINDER_CBE)+":icon.skill5661:Flag Finder:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Flagfinder;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_FLAGFINDER_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_TELEPORT_CBE)+":icon.skillelf:Teleport:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Teleport;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_TELEPORT_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_SHOP_CBE)+":icon.skill5970:Shop:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Shop;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_SHOP_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_WAREHOUSE_CBE)+":icon.pouch_i00:Warehouse:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Warehouse;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_WAREHOUSE_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_AUGMENT_CBE)+":icon.skill4285:Augment Manager:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";AugmentManager;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_AUGMENT_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_SUBCLASES_CBE)+":icon.skill0500:Subclass:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SubClass;add;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_SUBCLASES_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_CLASS_TRANSFER_CBE)+":icon.skill0517:Profession:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Profession;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_CLASS_TRANSFER_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_DROP_SEARCH_CBE)+":icon.skill1369:Drop Search:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";DropSearch;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_DROP_SEARCH_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_LOG_PELEAS_CBE)+":icon.skill4416_mercenary:PvP/PK Log:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";pvppklog;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_PVPPK_LIST_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_SYMBOL_MARKET_CBE)+":icon.skill5739:Symbol Maker:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Symbolmaker;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_SYMBOL_MARKET_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_BUG_REPORT_CBE)+":icon.skill6439:Bug Report:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";BugReport;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_BUG_REPORT_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_TRANSFORMATION_CBE)+":icon.skill1520:Transformations:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Transformation;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_TRANSFORMATION_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_REMOVE_ATRIBUTE_CBE)+":icon.skill0462:Remove Attribute:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";RemoveAttri;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_AUGMENT_SPECIAL_CBE)+":icon.skill4428:Select Augment:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectAugment;0;0;0;0;0;0:" +  general.BTN_SHOW_EXPLICA_AUGMENT_SPECIAL_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_ENCANTAMIENTO_ITEM_CBE)+":icon.skill4449:Select Enchant:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectEnchant;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_ELEMENT_ENHANCED_CBE)+":icon.skill6302:Select Elemental:"+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectElemental;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_ELEMENT_ENHANCED_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_RAIDBOSS_INFO_CBE)+":icon.skillboss:Raid Boss Info:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";RaidBossInfo;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_RAIDBOSS_INFO_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_BLACKSMITH_CBE)+":icon.skill1564:Blacksmith:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.blacksmith.name() +";0;0;0;0;0;0:" + "Exchange, Craft, Reseal, and other Options");
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_VARIAS_OPCIONES_CBE)+":icon.skill6319:Miscelanius:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";charclanoption;0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_VARIAS_OPCIONES_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_PARTYMATCHING_CBE)+":icon.skill6320:Party Matching:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.partymatching.name() + ";0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_PARTYMATCHING_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_AUCTIONHOUSE_CBE)+":icon.etc_ssq_i00:Auctions House:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_AUCTIONSHOUSE_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_BIDHOUSE_CBE)+":icon.skill0818:Bid House:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.BidHouse.name() + ";1;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_BIDSHOUSE_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_CASTLE_MANAGER_CBE)+":icon.etc_bloodpledge_point_i00:Castle Manager:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.castleManager.name() + ";0;0;0;0;0;0:" + general.BTN_SHOW_EXPLICA_CASTLE_MANAGER_CB);
		_btnImaLink.add(String.valueOf(general._BTN_SHOW_DRESSME)+":icon.skill0395:Dressme:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";0;0;0;0;0;0:Change your Armor / Weapon Textures for other");
		_btnImaLink.add(String.valueOf(true)+":icon.skill1381:Color Name Title:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.colornametitle.name() + ";0;0;0;0;0;0:Change Your Name / Title Color");
		

		/***/
			/*** Solo Falses */
		_btnImaLink.add("False:icon.skill6280:Class Statistic:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.ClasesStadistic.name() + ";0;Human;0;0;0;0:null");
		_btnImaLink.add("False:icon.skill0390:Donation:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";0;0;0;0;0;0:null");
		_btnImaLink.add("False:icon.skill5662:GM List:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.gmlist.name() + ";0;0;0;0;0;0:null");
		_btnImaLink.add("False:icon.skill5081:Oly Buff:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0:null");
		_btnImaLink.add("False:icon.skill6274:Commands:" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0:null");
		/***/
		btnImaLink = setSort(_btnImaLink);
	}
	
	private static Vector<String> setSort(Vector<String> menuToSort){
		Vector<String>Names = new Vector<String>();
		Map<String, String>btnSort = new HashMap<String, String>();
		Vector<String>Arreglo = new Vector<String>();
		for(String lista : menuToSort){
			Names.add( lista.split(":")[2]);
			btnSort.put(lista.split(":")[2], lista);
		}
		Collections.sort(Names);
		
		for(String lista : Names){
			Arreglo.add(btnSort.get(lista));
		}
		return Arreglo;
		
	}	
	
	public static String getExplica(String Nom){
		return getInfo(Nom).split(":")[4];
	}
	
	public static String getIcono(String Nom){
		return getInfo(Nom).split(":")[1];
	}
	
	public static String getNom(String Nom){
		return getInfo(Nom).split(":")[2];
	}
	
	public static String getLink(String Nom){
		return getInfo(Nom).split(":")[3];
	}
	
	public static String getInfo(String Nom){
		String retorno = "";
		for(String Parte : btnImaLink){
			try{
				if(Parte.split(":")[3].split(";")[1].equalsIgnoreCase(Nom)){
					retorno = Parte;
				}
			}catch(Exception a){
				
			}
		}
		return retorno;
	}
	
	private static String getEngineHTML(L2PcInstance player, String param, int Pagina){
		_Load();
		NpcHtmlMessage html = null;
		if(!general.USE_AUTOMATIC_HTML) {
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-index-manual.htm");
			html.replace("%BYPASS_ENGINE%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		}else{
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-index-auto.htm");		
			String GridColors = opera.getGridFormatFromHtml(html, 1, "");
			String Colores[] = {GridColors.split(":")[1].split(",")[0],GridColors.split(":")[1].split(",")[1]};
			String GridAllData = opera.getGridFormatFromHtml(html, 2, "%GRID_DATA_ALL%");
			
			int Contador = 0;
			int forColor = 0;
			
			String _retorno = "";
			
			for(String btnP : btnImaLink){
				boolean canUse = Boolean.parseBoolean(btnP.split(":")[0]);
				if(canUse){
					if(Contador==0){
						_retorno += GridAllData.replace("%BG_GRID_COLOR%", Colores[forColor % 2]);
					}
					_retorno = _retorno.replace("%SECTION_LINK_"+ String.valueOf(Contador+1) +"%", btnP.split(":")[3]).replace("%SECTION_DESCRIPTION_"+ String.valueOf(Contador+1) +"%", language.getInstance().getExplains(player, btnP.split(":")[2])).replace("%SECTION_NAME_"+ String.valueOf(Contador+1) +"%", btnP.split(":")[2]) .replace("%SECTION_ICON_"+ String.valueOf(Contador+1) +"%", btnP.split(":")[1]); //getBotonera(player, btnP.split(":")[1], btnP.split(":")[2],btnP.split(":")[4], btnP.split(":")[3]);
					Contador++;
					if(Contador>=3){
						Contador=0;
						forColor++;
					}
				}
			}		
			
			if(Contador>0 && Contador <3){
				for(int i=Contador;i<3;i++){
					_retorno = _retorno.replace("%SECTION_LINK_"+ String.valueOf(i+1) +"%", "").replace("%SECTION_DESCRIPTION_"+ String.valueOf(i+1) +"%", "").replace("%SECTION_NAME_"+ String.valueOf(i+1) +"%", "") .replace("%SECTION_ICON_"+ String.valueOf(i+1) +"%", "");
				}
			}
			
			html.replace("%GRID_DATA_ALL%", _retorno);
		}
		
		return html.getHtml();
	}
	
	private static void getAllSkillFromNPC(L2PcInstance player, int idMonster){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/npc_skill.html");
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(idMonster);
		Map<Integer, L2Skill> skills = new HashMap<>(tmpl.getSkills());
		Iterator<L2Skill> skillite = skills.values().iterator();
		L2Skill sk = null;
		Vector<String> MonsterSkill_ACTIVE = new Vector<String>();
		Vector<String> MonsterSkill_PASSIVE = new Vector<String>();
		
		while(skillite.hasNext()){
			sk = skillite.next();
			if(sk.isPassive()){
				MonsterSkill_PASSIVE.add(sk.getName() + ":" + String.valueOf(sk.getLevel()));
			}else if(sk.isPhysical()){
				MonsterSkill_ACTIVE.add(sk.getName() + ":" + String.valueOf(sk.getLevel()));
			}
		}
		
		html.replace("%MOB_NAME%", tmpl.getName());
		html.replace("%MOB_LEVEL%", String.valueOf(tmpl.getLevel()));
		
		String ActiveSkill = "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		boolean Empty = true;
		if(MonsterSkill_ACTIVE!=null){
			if(MonsterSkill_ACTIVE.size()>0){
				for(String PtSk : MonsterSkill_ACTIVE){
					ActiveSkill += "<table with=270 border=0 bgcolor=1C1C1C>"+
                              "<tr>"+
                                  "<td fixwidth=270 align= LEFT>"+
                                      "<font color=FFFABB>"+ PtSk.split(":")[0] +"</font> <font color=6B6B6B>lv "+ PtSk.split(":")[1] +"</font><br1>"+
                                  "</td>"+
                              "</tr>"+
                             "</table>";
				}
				Empty = false;
			}
		}
		if(Empty){
			ActiveSkill += "<table with=270 border=0 bgcolor=1C1C1C>"+
                    "<tr>"+
                        "<td fixwidth=270 align= LEFT>"+
                            "<font color=FFFABB>EMPTY</font><br1>"+
                        "</td>"+
                    "</tr>"+
                   "</table>";			
		}
		Empty = true;
		ActiveSkill += "<br></td></tr></table><br>";
		
		html.replace("%ACTIVE_SKILL%", ActiveSkill);
		
		
		String PassiveSkill = "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT>"+
				"<font name=hs12 color=8BECFF>Passive</font></td></tr></table>";

		PassiveSkill += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		if(MonsterSkill_PASSIVE!=null){
			if(MonsterSkill_PASSIVE.size()>0){
				for(String PtSk : MonsterSkill_PASSIVE){
					PassiveSkill += "<table with=270 border=0 bgcolor=1C1C1C>"+
                              "<tr>"+
                                  "<td fixwidth=270 align= LEFT>"+
                                      "<font color=FFFABB>"+ PtSk.split(":")[0] +"</font> <font color=6B6B6B>lv "+ PtSk.split(":")[1] +"</font><br1>"+
                                  "</td>"+
                              "</tr>"+
                             "</table>";
				}
				Empty = false;
			}
		}
		if(Empty){
			PassiveSkill += "<table with=270 border=0 bgcolor=1C1C1C>"+
                    "<tr>"+
                        "<td fixwidth=270 align= LEFT>"+
                            "<font color=FFFABB>EMPTY</font><br1>"+
                        "</td>"+
                    "</tr>"+
                   "</table>";			
		}
		
		PassiveSkill += "<br></td></tr></table><br>";
		
		html.replace("%PASSIVE_DATA%", PassiveSkill);
		central.sendHtml(player, html);
	}
	
	public static void openCBLink(L2PcInstance player, String toOpen){
		String _return = "";
		switch(toOpen.toLowerCase()){
			case "class_statistic":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.ClasesStadistic.name() + ";0;Human;0;0;0;0");
				break;
			case "buffer":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Buffer;0;0;0;0;0;0");
				break;
			case "go_party_leader":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Gopartyleader;0;0;0;0;0;0");
				break;
			case "flag_finder":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Flagfinder;0;0;0;0;0;0");
				break;
			case "teleport":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Teleport;0;0;0;0;0;0");
				break;
			case "shop":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Shop;0;0;0;0;0;0");
				break;
			case "warehouse":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Warehouse;0;0;0;0;0;0");
				break;
			case "augment":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";AugmentManager;0;0;0;0;0;0");
				break;
			case "subclass_manager":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SubClass;add;0;0;0;0;0");
				break;
			case "class_transfer":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Profession;0;0;0;0;0;0");
				break;
			case "drop_search":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";DropSearch;0;0;0;0;0;0");
				break;
			case "pvp_pk_log_system":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";pvppklog;0;0;0;0;0;0");
				break;
			case "symbol_market":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Symbolmaker;0;0;0;0;0;0");
				break;
			case "bug_report":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";BugReport;0;0;0;0;0;0");
				break;
			case "transformation_manager":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";Transformation;0;0;0;0;0;0");
				break;
			case "remove_Attribute":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";RemoveAttri;0;0;0;0;0;0");
				break;
			case "manual_augment":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectAugment;0;0;0;0;0;0");
				break;
			case "manual_enchant":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectEnchant;0;0;0;0;0;0");
				break;
			case "manual_enchant_element":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";SelectElemental;0;0;0;0;0;0");
				break;
			case "raidboss_info":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";RaidBossInfo;0;0;0;0;0;0");
				break;
			case "blacksmith":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.blacksmith.name() +";0;0;0;0;0;0");
				break;
			case "miscelanius":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";charclanoption;0;0;0;0;0;0");
				break;
			case "party_matching":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.partymatching.name() + ";0;0;0;0;0;0");
				break;
			case "auction_house":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0");
				break;
			case "bid_house":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.BidHouse.name() + ";1;0;0;0;0;0");
				break;
			case "dressme":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";0;0;0;0;0;0");
				break;
			case "classes_stadistic":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.ClasesStadistic.name() + ";0;Human;0;0;0;0;0");
				break;
			case "donation_manager":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.donation.name() + ";0;0;0;0;0;0");
				break;
			case "gm_list":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.gmlist.name() + ";0;0;0;0;0;0");
				break;
			case "oly_buff_scheme":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0");
				break;
			case "all_commands":
				_return = delegar(player, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0");
				break;
		}
		
		if(_return.isEmpty() || _return.equals("") || _return.length() < 1) {
			return;
		}
	
		cbManager.separateAndSend(_return,player);		
	}

	@SuppressWarnings("unused")
	public static String delegar(L2PcInstance player, String params){
		String Retorno = "";
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning(params);
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.msgbox(params, player);
		}
		general.IS_USING_NPC.put(player.getObjectId(),false);
		general.IS_USING_CB.put(player.getObjectId(),true);
		if(player.isJailed()){
			return Comunidad.getComunidad(player, "", 0);
		}
		
		
		if(params == null){
			return getEngineHTML(player, "", 0);
		}else if(params.length()<=0){
			return getEngineHTML(player, "", 0);
		}else if(params.equals(general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC())){
			return getEngineHTML(player, "", 0);
		}

		if(params.contains(";")){
			String[] Eventos = params.split(";");
			String Event = Eventos[1];
			String parm1 = Eventos[2];
			String parm2 = Eventos[3];
			String parm3 = Eventos[4];
			String parm4 = Eventos[5];
			String parm5 = Eventos[6];
			String parm6 = Eventos[7];
						
			/** Enter Filter*/


			boolean canUseCB = opera.canUseCBFunction(player, true);
			boolean canUseCB_withoutInstance = opera.canUseCBFunction(player, false);
			if(Event.equals(enumBypass.Buffer.name()) && SunriseEvents.isInEvent(player)){
				canUseCB = true;
				canUseCB_withoutInstance = true;
			}
			boolean canUseTeleport = true;
			
			if(player.isOlympiadStart() || SunriseEvents.isInEvent(player) || player.isJailed() || (RaidBossEvent.isPlayerOnRBEvent(player) && RaidBossEvent._started)){
				canUseTeleport = false;
			}
				
			
			
			if(!canUseCB){
				if(!Event.equals(enumBypass.OlyBuffer.name())){
					if(Event.equals(enumBypass.Buffer.name())){
						if(canUseCB_withoutInstance){
							if(!general.BUFFCHAR_USE_INSTANCE){
								central.msgbox(language.getInstance().getMsg(player).COMMUNITY_SYSTEM_DISABLE, player);
								return Comunidad.getComunidad(player, "", 0);								
							}
						}else{
							return Comunidad.getComunidad(player, "", 0);
						}
					}else if(Event.equals(enumBypass.Teleport.name())){
						if(player.isOlympiadStart() || !canUseTeleport){
							central.msgbox(language.getInstance().getMsg(player).COMMUNITY_SYSTEM_DISABLE, player);
							return Comunidad.getComunidad(player, "", 0);							
						}
					}else{					
						central.msgbox(language.getInstance().getMsg(player).COMMUNITY_SYSTEM_DISABLE, player);
						return Comunidad.getComunidad(player, "", 0);
					}
				}else{
					if(player.isOlympiadStart()){
						central.msgbox(language.getInstance().getMsg(player).OLY_BUFFER_MANAGER_TIME_OUT, player);
						cbFormato.cerrarCB(player);
					}
				}
			}
			
			/** Enter Filter*/
			
			if(Event.equals(enumBypass.Buffer.name())){

				if(SunriseEvents.isInEvent(player)){
					return v_Buffer_New.delegar(player, params);
				}

				if(general.BUFFER_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM && !ZeuS.isPremium(player) && !player.isGM() ){
					if(!player.isInsideZone(ZoneIdType.PEACE)){
						return Comunidad.getComunidad(player, "", 0);
					}
				}
				
				if(general.BUFFCHAR_JUST_ON_PEACE_ZONE && !player.isInsideZone(ZoneIdType.PEACE)){
					if(player.getInstanceId()>0){
						if(!general.BUFFCHAR_USE_INSTANCE){
							central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_JUST_IN_PEACE_ZONE, player);
							Retorno = Comunidad.getComunidad(player, "", 0);							
						}else{
							Retorno = v_Buffer_New.delegar(player, params);							
						}
					}else{
						central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_JUST_IN_PEACE_ZONE, player);
						Retorno = Comunidad.getComunidad(player, "", 0);
					}
				}else{
					Retorno = v_Buffer_New.delegar(player, params);
				}
			}else if(SunriseEvents.isInEvent(player)){
				player.sendMessage("You cannot use this service inside Event;");
				Retorno = "";
				return Retorno;
			}else if(Event.equals(enumBypass.Gopartyleader.name())){
				Retorno = v_PartyFinder.bypass(player, params);
			}else if(Event.equals(enumBypass.Flagfinder.name())){
				Retorno = v_FlagFinder.bypass(player, params);
			}else if(Event.equals(enumBypass.Teleport.name())){
				Retorno = v_Teleport.bypass(player, params);
			}else if(Event.equals(enumBypass.Shop.name())){
				if(general.SHOP_PLAYER_ACCESS_EVERYWHERE_JUST_FOR_PREMIUM && !ZeuS.isPremium(player) && !player.isGM() ){
					if(!player.isInsideZone(ZoneIdType.PEACE)){
						return Comunidad.getComunidad(player, "", 0);
					}
				}			
				Retorno = v_Shop.bypass(player, params);
			}else if(Event.equals(enumBypass.Warehouse.name())){
				Retorno = v_Warehouse.bypass(player, params);
			}else if(Event.equals(enumBypass.AugmentManager.name())){
				Retorno = v_augmentManager.bypass(player, params);
			}else if(Event.equals(enumBypass.SubClass.name())){
				Retorno = v_subclass.bypass(player, params);
			}else if(Event.equals(enumBypass.Profession.name())){
				Retorno = v_profession.bypass(player, params);
			}else if(Event.equals(enumBypass.Symbolmaker.name())){
				Retorno = v_symbolMaker.bypass(player, params);
			}else if(Event.equals(enumBypass.RemoveAttri.name())){
				Retorno = v_RemoveAttribute.bypass(player, params);
			}else if(Event.equals(enumBypass.DropSearch.name())){
				Retorno = v_dropsearch.bypass(player, params);
			}else if(Event.equals(enumBypass.pvppklog.name())){
				Retorno = v_pvppkLog.bypass(player, params);
			}else if(Event.equals(enumBypass.ClasesStadistic.name())){
				Retorno = v_clasesStadistic.bypass(player, params);
			}else if(Event.equals(enumBypass.RaidBossInfo.name())){
				Retorno = v_RaidBossInfo.bypass(player, params);
			}else if(Event.equals(enumBypass.HeroList.name())){
				Retorno = v_HeroList.bypass(player, params);
			}else if(Event.equals(enumBypass.MyInfo.name())){
				Retorno = v_MyInfo.bypass(player, params);
			}else if(Event.equals(enumBypass.BugReport.name())){
				Retorno = v_BugReport.bypass(player, params);
			}else if(Event.equals(enumBypass.SelectElemental.name())){
				Retorno = v_ElementalSpecial.bypass(player, params);
			}else if(Event.equals(enumBypass.SelectEnchant.name())){
				Retorno = v_EnchantSpecial.bypass(player, params);
			}else if(Event.equals(enumBypass.SelectAugment.name())){
				Retorno = v_AugmentSpecial.bypass(player, params);
			}else if(Event.equals(enumBypass.blacksmith.name())){
				Retorno = v_blacksmith.bypass(player, params);
			}else if(Event.equals(enumBypass.charclanoption.name())){
				Retorno = v_MiscelaniusOption.bypass(player,params);
			}else if(Event.equals(enumBypass.Transformation.name())){
				Retorno = v_Transformations.bypass(player,params);
			}else if(Event.equals(enumBypass.partymatching.name())){
				Retorno = v_partymatching.bypass(player, params);
			}else if(Event.equals(enumBypass.AuctionHouse.name())){
				Retorno = v_auction_house.bypass(player, params);
			}else if(Event.equals(enumBypass.BidHouse.name())){
				Retorno = v_bid_house.bypass(player, params);
			}else if(Event.equals(enumBypass.donation.name())){
				Retorno = v_donation.bypass(player, params);
			}else if(Event.equals(enumBypass.admCommand.name())){
				Retorno = C_gmcommand.bypass(player, params);
			}else if(Event.equals(Engine.enumBypass.castleManager.name())){
				if(!general.STATUS_CASTLE_MANAGER) {
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return "";
				}
				String Enviar = htmls.MainHtmlCastleManager(player);
				central.sendHtml(player, Enviar);
				Retorno ="";
			}else if(Event.equals(enumBypass.OlyBuffer.name())){				
				Retorno = C_oly_buff.bypass(player, params);
				if(Retorno.length()==0){
					cbFormato.cerrarCB(player);
				}
			}else if(Event.equals(Engine.enumBypass.commandinfo.name())){
				Retorno= C_cmdInfo.bypass(player, params);
			}else if(Event.equals("ShowNPCSkill")){
				getAllSkillFromNPC(player,Integer.valueOf(parm1));
			}else if(Event.equals("gmlist")){
				C_gmlist.bypass(player, params);
			}else if(Event.equals(Engine.enumBypass.Dressme.name())){
				Retorno = v_Dressme.bypass(player, params);
			}else if(Event.equals(Engine.enumBypass.colornametitle.name())) {
				Retorno = colorNameTitle.showNameTitleColor(player, true);
			}
		}

		return Retorno;
	}
}
