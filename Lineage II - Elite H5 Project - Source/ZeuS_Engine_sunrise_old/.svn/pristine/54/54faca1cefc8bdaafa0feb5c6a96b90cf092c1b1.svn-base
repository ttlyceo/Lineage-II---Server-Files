package ZeuS.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;


public class button {
	private static String[] estado = {"<font color=DF3A01>DISABLED</font>","<font color=00FF40>ENABLED</font>"};

	private static final Logger _log = Logger.getLogger(Config.class.getName());

	public static String getBtnCBE(L2PcInstance player){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Control Show Features<br1>ZeuS CB Engine<br1> This Option work only with USE_AUTOMATIC_HTML = true")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += "<table width=280 border=0 bgcolor=151515>";
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Buffer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 421 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_BUFFER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Dressme\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 907 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_DRESSME ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Teleport\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 422 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_TELEPORT_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Shop\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 423 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_SHOP_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Warehouse\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 424 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general._BTN_SHOW_WAREHOUSE_CBE ? estado[1]: estado[0] );
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment/Deaugment\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 425 2 0\" width=130 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_AUGMENT_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Sub Class\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 426 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_SUBCLASES_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Class Transfer\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 427 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_CLASS_TRANSFER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Drop S.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 429 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_DROP_SEARCH_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 430 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_PVPPK_LIST_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Log PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 431 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_LOG_PELEAS_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Castle Manager\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 432 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_CASTLE_MANAGER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Simbol Maker\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 434 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_SYMBOL_MARKET_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Clan & Ally\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 435 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_CLANALLY_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Party L.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 436 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_PARTYFINDER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Flag\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 437 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_FLAGFINDER_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Color Name\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 438 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_COLORNAME_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Delevel\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 439 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_DELEVEL_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"G. Boss Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 449 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_GRAND_BOSS_STATUS_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Raid. B. Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 450 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_RAIDBOSS_INFO_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"S. Atribute\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 440 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_REMOVE_ATRIBUTE_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bug Report\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 441 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_BUG_REPORT_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Miscellaneous\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 445 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_VARIAS_OPCIONES_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Transform\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 451 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_TRANSFORMATION_CBE? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Elemental It.\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 446 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_ELEMENT_ENHANCED_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Enchant Item\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 447 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_ENCANTAMIENTO_ITEM_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment Special\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 448 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_AUGMENT_SPECIAL_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Blacksmith\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 571 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_BLACKSMITH_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Party Matching\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 587 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_PARTYMATCHING_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Auction House\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 594 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_AUCTIONHOUSE_CBE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bid House\" action=\"bypass -h " + menu.bypassNom + " statusCBTN 622 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general._BTN_SHOW_BIDHOUSE_CBE ? estado[1]: estado[0]);
		MAIN_HTML += "</table>";
		MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + menu.getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}
	
	public static String getBtnCBStatus(L2PcInstance player){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(1)+central.LineaDivisora(2)+central.headFormat("Control Access<br1>ZeuS CB Engine")+central.LineaDivisora(2) + central.LineaDivisora(1);
		MAIN_HTML += "<table width=280 border=0 bgcolor=151515>";
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Buffer\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 908 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_BUFFER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Dressme\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 937 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_DRESSME ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Teleport\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 909 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_TELEPORT ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Shop\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 910 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_SHOP ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Warehouse\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 911 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.STATUS_WAREHOUSE ? estado[1]: estado[0] );
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment/Deaugment\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 912 2 0\" width=130 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_AUGMENT ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Sub Class\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 913 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_SUBCLASES ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Class Transfer\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 914 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_CLASS_TRANSFER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Drop S.\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 916 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_DROP_SEARCH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 917 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_PVPPK_LIST ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Log PvP / PK\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 918 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_LOG_PELEAS ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Castle Manager\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 919 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_CASTLE_MANAGER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Simbol Maker\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 920 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_SYMBOL_MARKET ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Party L.\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 921 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_PARTYFINDER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Go Flag\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 922 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_FLAGFINDER ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Raid. B. Spawn\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 931 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_RAIDBOSS_INFO ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"S. Atribute\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 924 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_REMOVE_ATRIBUTE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bug Report\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 925 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_BUG_REPORT ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Miscellaneous\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 926 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_VARIAS_OPCIONES ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Transform\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 932 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_TRANSFORMACION ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Elemental It.\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 927 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_ELEMENT_ENHANCED ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Enchant Item\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 928 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_ENCANTAMIENTO_ITEM ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Augment Special\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 929 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_AUGMENT_SPECIAL ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Blacksmith\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 933 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_BLACKSMITH ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Auction House\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 935 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_AUCTIONHOUSE ? estado[1]: estado[0]);
		MAIN_HTML += central.BotonCentral_sinFormato("<button value=\"Bid House\" action=\"bypass -h " + menu.bypassNom + " statusCBT2 936 2 0\" width=98 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.STATUS_BIDHOUSE ? estado[1]: estado[0]);
		MAIN_HTML += "</table>";
		MAIN_HTML += central.LineaDivisora(2) + central.LineaDivisora(1) + menu.getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}	
	
	public static void status(L2PcInstance player, int idProc){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(1,"+idProc+",'','','')";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET ADMIN BUTTON->"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET ADMIN BUTTON->"+a.getMessage());
		}

		if(Respu.equals("cor")){
			general.loadConfigs();
		}

	}

}
