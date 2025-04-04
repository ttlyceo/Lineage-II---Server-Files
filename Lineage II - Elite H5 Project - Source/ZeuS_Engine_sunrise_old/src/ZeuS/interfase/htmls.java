package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.admin.menu;
import ZeuS.procedimientos.opera;

public class htmls extends ManagerAIONpc{

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(htmls.class.getName());

	@SuppressWarnings("unused")
	private static String getPlayerByIP(L2PcInstance player){
		String ipNet = ZeuS.getIp_Wan(player);
		String NombrePlayerIP = "";
		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();
		Vector<String> IPSGM = new Vector<String>();
		for(L2PcInstance onlinePlayer : pls){
			try{
				if(onlinePlayer.isOnline()){
					if(ipNet.equals(ZeuS.getIp_Wan(onlinePlayer))) {
						if(NombrePlayerIP.length()>0){
							NombrePlayerIP += ",";
						}
						NombrePlayerIP += onlinePlayer.getName();
					}
				}
			}catch(Exception a){

			}
		}
		return player.getName() + ":" + NombrePlayerIP;
	}

	public static String getInfoContacto(){
		String MAIN_HTML ="<html><title>" + general.TITULO_NPC() + "</title><body>";

		String Caracteristicas = "The best engine for your L2Jserver, with more than 50 options and unique features. For more information contact the developer Email adove or follow us on our facebook<br1>http://facebook.com/groups/ZeuSAIO";

		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("About ZeuS AIO NPC")  + central.LineaDivisora(3);

		MAIN_HTML += central.LineaDivisora(3) + central.LineaDivisora(2) + central.LineaDivisora(1);

		MAIN_HTML += central.headFormat("Created by Jabberwock 2013<br1>"+general.EMAIL+"<br1>" + Caracteristicas + "<br>v.: " + general.VERSION);

		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.LineaDivisora(3);

		MAIN_HTML += central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	public static String ErrorTipeoEspacio(boolean ShowBackBtn){
		String HTML_STR = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error") + central.LineaDivisora(2);
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error de Tipeo. Ingrese los Datos Solicitados<br1>Correctamente.<br1>Typing error. Enter <br1> Properly Requested Data.<br>Vuelva a intentar<br>Try Again","B40404") + central.LineaDivisora(2);
		HTML_STR += central.getPieHTML() +  "</body></html>";
		return HTML_STR;
	}

	public static String ErrorTipeoEspacio(){
		return ErrorTipeoEspacio(true);
	}

	public static String ErrorTipeoEspacio_Admin(){
		String HTML_STR = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error") + central.LineaDivisora(2);
		HTML_STR += central.LineaDivisora(2) + central.headFormat("Error de Tipeo. Ingrese los Datos Solicitados<br1>Correctamente.<br1>Typing error. Enter <br1> Properly Requested Data.<br>Vuelva a intentar<br>Try Again","B40404") + central.LineaDivisora(2);
		HTML_STR += menu.getBtnbackConfig() + central.getPieHTML() +  "</body></html>";
		return HTML_STR;
	}




	public static String CrearFilaDonacion(String Boton1IN, String Boton2IN, String npcid){
		String Boton1[] = Boton1IN.split(",");
		String Boton2[] = Boton2IN.split(",");

		int IDPINTAR = central.INT_PINTAR_GRILLA(0);

		String COLOR_PINTA[] = {"LEVEL","00FFFF"};

		String BOTON_GENE_1,BOTON_GENE_2;
		if( central.isNumeric(Boton1[2])) {
			BOTON_GENE_1 = "<button value=\""+Boton1[1]+"\" action=\"bypass -h npc_"+npcid+"_multisell "+Boton1[2] + " \" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else {
			BOTON_GENE_1 = "<button value=\""+Boton1[1]+"\" action=\"bypass -h ZeuSNPC "+ Boton1[2] + " \" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		if (central.isNumeric(Boton2[2])) {
			BOTON_GENE_2 = "<button value=\""+Boton2[1]+"\" action=\"bypass -h npc_"+npcid+"_multisell "+Boton2[2] + "\" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else {
			BOTON_GENE_2 = "<button value=\""+Boton2[1]+"\" action=\"bypass -h ZeuSNPC "+ Boton2[2] + " \" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		String RETORNO = "<table width = 280 bgcolor=151515>";
		RETORNO += "<tr><td width=140 align=CENTER><font color=\""+COLOR_PINTA[IDPINTAR]+"\">"+Boton1[0]+" D. Coin</font></td><td width=140 align=CENTER><font color=\""+COLOR_PINTA[IDPINTAR]+"\">"+Boton2[0]+" D. Coin</font></td></tr>";
		RETORNO += "<tr><td width=140 align=CENTER>"+BOTON_GENE_1+"</td><td width=140 align=CENTER>"+BOTON_GENE_2+"</td></tr>";
		RETORNO += "</table>"+central.LineaDivisora(1);
		return RETORNO;
	}

	private static String showNewMenu(boolean cn, boolean ch, String npcid){

		String btnHeight = "21";

		String MAIN_HTML = "";
		if ((general.BTN_SHOW_BUFFER && cn) || (general.BTN_SHOW_BUFFER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Buffer\" action=\"bypass -h ZeuSNPC deluxbuffer 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_BUFFER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_TELEPORT && cn) || (general.BTN_SHOW_TELEPORT_CH && ch)) {
			if(!general.TELEPORT_BD){
				MAIN_HTML += central.BotonCentral("<button value=\"Teleport\" action=\"bypass -h npc_"+npcid+"_Link teleporter/AIONpc-GK/teleports.htm\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_TELEPORT,central.INT_PINTAR_GRILLA(0));
			}else{
				MAIN_HTML += central.BotonCentral("<button value=\"Teleport\" action=\"bypass -h ZeuSNPC teleportMain 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_TELEPORT,central.INT_PINTAR_GRILLA(0));
			}
		}
		if ((general.BTN_SHOW_SHOP && cn) || (general.BTN_SHOW_SHOP_CH && ch)) {
			if(!general.SHOP_USE_BD) {
				MAIN_HTML += central.BotonCentral("<button value=\"Shop\" action=\"bypass -h npc_"+npcid+"_Link merchant/AIONPC/955.htm\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_SHOP ,central.INT_PINTAR_GRILLA(0));
			} else {
				MAIN_HTML += central.BotonCentral("<button value=\"Shop\" action=\"bypass -h ZeuSNPC shopBD 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_SHOP ,central.INT_PINTAR_GRILLA(0));
			}
		}
		if ((general.BTN_SHOW_WAREHOUSE && cn) || (general.BTN_SHOW_WAREHOUSE_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Warehouse\" action=\"bypass -h ZeuSNPC chat1 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_WAREHOUSE ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_AUGMENT && cn) || (general.BTN_SHOW_AUGMENT_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Augment\" action=\"bypass -h ZeuSNPC AUGMENTMNU "+npcid+" 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_AUGMENT ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_SUBCLASES && cn) || (general.BTN_SHOW_SUBCLASES_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Sub Class\" action=\"bypass -h ZeuSNPC chat3 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_SUBCLASES,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CLASS_TRANSFER && cn) || (general.BTN_SHOW_CLASS_TRANSFER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Class Transfer\" action=\"bypass -h ZeuSNPC TranferMenu 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_CLASS_TRANSFER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CONFIG_PANEL && cn) || (general.BTN_SHOW_CONFIG_PANEL_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Config Panel\" action=\"bypass -h ZeuSNPC ConfigPanel 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_CONFIG_PANEL ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DROP_SEARCH && cn) || (general.BTN_SHOW_DROP_SEARCH_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Drop S.\" action=\"bypass -h ZeuSNPC DropSearch 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_DROP_SEARCH,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_PVPPK_LIST && cn) || (general.BTN_SHOW_PVPPK_LIST_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"PvP / PK\" action=\"bypass -h ZeuSNPC PKlistoption 1 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_PVPPK_LIST,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_LOG_PELEAS && cn) || (general.BTN_SHOW_LOG_PELEAS_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Log PvP / PK\" action=\"bypass -h ZeuSNPC logpeleas 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_LOG_PELEAS,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CASTLE_MANAGER && cn) || (general.BTN_SHOW_CASTLE_MANAGER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Castle Manager\" action=\"bypass -h ZeuSNPC CastleManagerStr 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_CASTLE_MANAGER,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DESAFIO && cn) || (general.BTN_SHOW_DESAFIO_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"The Challenge\" action=\"bypass -h ZeuSNPC DESAFIO 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_DESAFIO ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_SYMBOL_MARKET && cn) || (general.BTN_SHOW_SYMBOL_MARKET_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Symbol Maker\" action=\"bypass -h ZeuSNPC chat6 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_SYMBOL_MARKET ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_CLANALLY && cn) || (general.BTN_SHOW_CLANALLY_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Clan & Ally\" action=\"bypass -h ZeuSNPC chat4 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_VARIAS_OPCIONES ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_PARTYFINDER && cn) || (general.BTN_SHOW_PARTYFINDER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Go Party L.\" action=\"bypass -h ZeuSNPC ptfinder 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_PARTYFINDER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_FLAGFINDER && cn) || (general.BTN_SHOW_FLAGFINDER_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Go Flag\" action=\"bypass -h ZeuSNPC fgfinder 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_FLAGFINDER ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_COLORNAME && cn) || (general.BTN_SHOW_COLORNAME_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Color Name\" action=\"bypass -h ZeuSNPC Colormenuu 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_COLORNAME ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DELEVEL && cn) || (general.BTN_SHOW_DELEVEL_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Delevel\" action=\"bypass -h ZeuSNPC DellvlMenu 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_DELEVEL,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_GRAND_BOSS_STATUS && cn) || (general.BTN_SHOW_GRAND_BOSS_STATUS_CH && ch)){
			MAIN_HTML += central.BotonCentral("<button value=\"G. Boss Spawn\" action=\"bypass -h ZeuSNPC showGrandBoss 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_GRAND_BOSS_STATUS ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_RAIDBOSS_INFO && cn) || (general.BTN_SHOW_RAIDBOSS_INFO_CH && ch)){
			MAIN_HTML += central.BotonCentral("<button value=\"Raid. B. Spawn\" action=\"bypass -h ZeuSNPC RaidBossInf 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_RAIDBOSS_INFO,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_REMOVE_ATRIBUTE && cn) || (general.BTN_SHOW_REMOVE_ATRIBUTE_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"S. Attribute\" action=\"bypass -h ZeuSNPC ReleaseAttribute 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_REMOVE_ATRIBUTE,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_BUG_REPORT && cn) || (general.BTN_SHOW_BUG_REPORT_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Bug Report\" action=\"bypass -h ZeuSNPC MENUBUFREPORT 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_BUG_REPORT,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_CAMBIO_NOMBRE_PJ && cn) || (general.BTN_SHOW_CAMBIO_NOMBRE_PJ_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"C.Nombre pj\" action=\"bypass -h ZeuSNPC USER_CHANGE_NAME 1 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_PJ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_CAMBIO_NOMBRE_CLAN && cn) || (general.BTN_SHOW_CAMBIO_NOMBRE_CLAN_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"C. Nom. Clan\" action=\"bypass -h ZeuSNPC USER_CHANGE_NAME 2 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_CAMBIO_NOMBRE_CLAN,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_VARIAS_OPCIONES && cn) || (general.BTN_SHOW_VARIAS_OPCIONES_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Miscellaneous\" action=\"bypass -h ZeuSNPC OPCIONESVAR 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_VARIAS_OPCIONES ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_TRANSFORMATION && cn) || (general.BTN_SHOW_TRANSFORMATION_CH && ch)){
			MAIN_HTML += central.BotonCentral("<button value=\"Transfor\" action=\"bypass -h ZeuSNPC transform 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_TRANSFORMATION ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_ELEMENT_ENHANCED && cn) || (general.BTN_SHOW_ELEMENT_ENHANCED_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Elemental It.\" action=\"bypass -h ZeuSNPC ELEMENTAL 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_ELEMENT_ENHANCED,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_ENCANTAMIENTO_ITEM && cn) || (general.BTN_SHOW_ENCANTAMIENTO_ITEM_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Enchant Item\" action=\"bypass -h ZeuSNPC ENCHANTITEM 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">", general.BTN_SHOW_EXPLICA_ENCANTAMIENTO_ITEM ,central.INT_PINTAR_GRILLA(0));
		}
		if((general.BTN_SHOW_AUGMENT_SPECIAL && cn) || (general.BTN_SHOW_AUGMENT_SPECIAL_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Augment Special\" action=\"bypass -h ZeuSNPC AUGMENTSP 0 -1 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_AUGMENT_SPECIAL ,central.INT_PINTAR_GRILLA(0));
		}
		if ((general.BTN_SHOW_DONATION && cn) || (general.BTN_SHOW_DONATION_CH && ch)) {
			MAIN_HTML += central.BotonCentral("<button value=\"Donation\" action=\"bypass -h ZeuSNPC MenuDonation "+npcid+" 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">",general.BTN_SHOW_EXPLICA_DONATION ,central.INT_PINTAR_GRILLA(0));
		}

		return MAIN_HTML;
	}


	/*public static String get_CB_Main_Btn(L2PcInstance cha, String params){
		return showClasicMenu(false, false, String.valueOf(general.npcGlobal(cha,true)), true, cha);
	}*/


	public static String firtsHTML(L2PcInstance st, String npcid){
		general.IS_USING_NPC.put(st.getObjectId(),true);
		general.IS_USING_CB.put(st.getObjectId(),false);
		String MAIN_HTML;
		if(!general._activated()){
			return "NO LEGAL";
		}
		String BtnAdmin ="";
		/*
		if(opera.isMaster(st)){
			BtnAdmin = "<table width=280><tr><td width=85>"
					+ "<button value=\"Load\" action=\"bypass -h ZeuSNPC Config 1 0 0\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"
					+ "</td><td width=110 align=center>"+msg.MENSAJE_BIENVENIDA+"</td>"
					+"<td width=85><button value=\"Config\" action=\"bypass -h ZeuSNPC Config 2 0 0\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		}*/
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2);
		MAIN_HTML += central.headFormat(BtnAdmin) + central.LineaDivisora(2);

		boolean cn, ch;
		cn = isZeuSALL(st);
		ch = isZeuSCH(st);

		//MAIN_HTML += "<table width=280 border=0 bgcolor=151515>";

		String btnHeight = "21";

		boolean canShowOther = true;

		if(!opera.isMaster(st) && general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM){
			if(!opera.haveItem(st, general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID, 1,false)){
				if(!opera.haveItem(st, general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID,1,false)){
					canShowOther = false;
				}
			}
		}


		if ((general.BTN_SHOW_VOTE && cn) || (general.BTN_SHOW_VOTE_CH && ch)) {
			String btnVoto = "<button value=\"Vote\" action=\"bypass -h ZeuSNPC VoteReward 0 0 0\" width=98 height="+btnHeight+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			if(general.SHOW_NEW_MAIN_WINDOWS){
				MAIN_HTML += central.BotonCentral(btnVoto,general.BTN_SHOW_EXPLICA_VOTE ,central.INT_PINTAR_GRILLA(0));
			}else{
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnVoto) + central.LineaDivisora(1);
			}
		}
		if(canShowOther){
			if(general.SHOW_NEW_MAIN_WINDOWS){
				MAIN_HTML += showNewMenu(cn,ch,npcid);
			}else{
				//MAIN_HTML += showClasicMenu(cn,ch,npcid);
			}
		}else{
			String Mensaje = "To use ZeuS, you must have " + central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_ONLY_BY_ITEM_ID) + ", which you get Voting";

			if(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM){
				Mensaje += " or with " + central.getNombreITEMbyID(general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID) + " that you can buy for: " + central.ItemNeedShow_line(general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE) ;
			}else{
				Mensaje +=".";
			}
			//+ central.ItemNeedShowBox(general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE)
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat(Mensaje,"LEVEL") + central.LineaDivisora(1);
		}
		MAIN_HTML += central.getPieHTML();
		MAIN_HTML += "</center></body></html>";
		return MAIN_HTML;
	}


	public static String DesafioVerPremios(L2PcInstance st){
		String BOTON_ATRAS = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DESAFIO 0 0 0\" width=50 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Reward Hidden NPC") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_ATRAS,"") + central.LineaDivisora(2);
		int IDPremioCAP = -1;
		String HTML_MIENTRAS = "";
		String COLORES_FONDO[] = {"4B610B","5E610B"};
		String COLOR_TITULO = "0A2229";
		String INFOFAMILIA ="";
		try{
		Connection conn = L2DatabaseFactory.getInstance().getConnection();
		String qry = "call sp_npc_evento_operaciones(2,1,1,1)";
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();
		int ID_ITEM;
		while (rss.next()){
			if (!rss.getString(1).equals("err")){
				if (IDPremioCAP != rss.getInt(2)){
					IDPremioCAP = rss.getInt(2);
					INFOFAMILIA = "<table width = 280 bgcolor="+COLOR_TITULO+"><tr><td align=CENTER width = 200>Family: "+String.valueOf(rss.getInt(2))+" - Given: " + rss.getString(5) + "</td></tr></table>";
					if (HTML_MIENTRAS.length()==0) {
						HTML_MIENTRAS = INFOFAMILIA + "<table width=280 bgcolor="+COLORES_FONDO[central.INT_PINTAR_GRILLA(-1)]+">";
					} else {
						HTML_MIENTRAS += "</table>"+central.LineaDivisora(2)+"<br>"+INFOFAMILIA+"<table width = 280 bgcolor="+COLORES_FONDO[central.INT_PINTAR_GRILLA(-1)]+">";
					}
				}
				ID_ITEM = rss.getInt(3);
				HTML_MIENTRAS += "<tr><td width=280 align=center>" + central.getNombreITEMbyID(ID_ITEM) + "("+ String.valueOf(rss.getInt(4)) +")</td></tr>";
			}
		}
		conn.close();
		}catch(SQLException e){

		}
		if(HTML_MIENTRAS.length()>0) {
			MAIN_HTML += HTML_MIENTRAS + "</table>" + central.LineaDivisora(2);
		}
		MAIN_HTML += central.getPieHTML() + "</body></html>";
		return MAIN_HTML;

	}













	public static String MainMenuAug(String npcid){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Augment / Deaugment") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Augment\" action=\"bypass -h npc_"+npcid+"_Augment 1\" width=192 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Remove Augment\" action=\"bypass -h npc_"+npcid+"_Augment 2\"  width=192 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>" + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}




	public static String MainHtmlCastleManager(L2PcInstance st){
		boolean ShowEndReg = false;
		String[] Fee = central.getDateCastleRegEnd(st);

		String MAIN_HTM = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTM += central.LineaDivisora(2) + central.headFormat("Castle Manager") + central.LineaDivisora(2);
		MAIN_HTM += central.LineaDivisora(2) + central.headFormat("Choose the Castle","LEVEL") + central.LineaDivisora(2);

		if (general.GIRAN){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Giran Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 3 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[2]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.ADEN){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Aden Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 5 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[4]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.RUNE){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Rune Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 8 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[7]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.OREN){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Oren Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 4 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[3]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.DION){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Dion Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 2 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[1]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.GLUDIO){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Gludio Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 1 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[0]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.GODDARD){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Goddard Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 7 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[6]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.SCHUTTGART){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Schuttgart Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 9 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[8]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		if(general.INNADRIL){
			MAIN_HTM += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTM += "<td width=280 align=center><button value=\"Innadril Castle\" action=\"bypass -h ZeuSNPC CastleManagerStr 6 0 0\" width=161 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
			MAIN_HTM += "</tr></table>";
			if(ShowEndReg)
				MAIN_HTM += central.headFormat("End Reg: " + String.valueOf(Fee[5]),"7A7A7A");
			MAIN_HTM += central.LineaDivisora(2);
		}

		MAIN_HTM += central.getPieHTML() + "</center><br></body></html>";
		return MAIN_HTM	;
	}


	public static String MainHtml1(L2PcInstance st){
		String idObjetoNPC = general.npcGlobal(st);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Warehouse") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Private Warehouse","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Deposit Item\" action=\"bypass -h npc_"+idObjetoNPC+"_DepositP\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Withdraw Item\" action=\"bypass -h npc_"+idObjetoNPC+"_WithdrawP\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += central.LineaDivisora(2) + "<br1>" + central.LineaDivisora(2) + central.headFormat("Clan Warehouse","LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Deposit Item\" action=\"bypass -h ZeuSNPC warehouse DepositC 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Withdraw Item\" action=\"bypass -h ZeuSNPC warehouse WithdrawC 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<br><br><br>" + "<br><font color=\"303030\"></font>";
		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml2(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += "<img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br>";
		MAIN_HTML += "<font color=\"FF0000\">.::Skill Enchant Options::.</font><br><br1>";
		MAIN_HTML += "<button value=\"Learn Skill\" action=\"bypass -h ZeuSNPC SkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Skill enchant\" action=\"bypass -h ZeuSNPC EnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Safe enchant\" action=\"bypass -h ZeuSNPC SafeEnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Untrain skills\" action=\"bypass -h ZeuSNPC UntrainEnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"Change routes\" action=\"bypass -h ZeuSNPC ChangeEnchantSkillList 0 0 0\" width=120 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<br><font color=\"303030\"></font>";
		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml3(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("SubClass Master Options") + central.LineaDivisora(2);
		if (st.getTotalSubClasses() < Config.MAX_SUBCLASS) {
			MAIN_HTML += "<button value=\"Add Subclass\" action=\"bypass -h ZeuSNPC subclass addsub 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		}
		MAIN_HTML += "<button value=\"Change Subclass\" action=\"bypass -h ZeuSNPC subclass changesub 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<button value=\"Remove Subclass\" action=\"bypass -h ZeuSNPC subclass deletesub 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><font color=\"303030\"></font>"	;
		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml4(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Clan") + central.LineaDivisora(2);
		if(st.getClan() == null) {
			MAIN_HTML += "<button value=\"Create New Clan\" action=\"bypass -h ZeuSNPC createclan 0 0 0\" width=150 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		} else{
			MAIN_HTML += "<button value=\"Delegate Clan Leader\" action=\"bypass -h ZeuSNPC giveclanl 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Increase Clan Level\" action=\"bypass -h ZeuSNPC increaseclan 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Disband Clan\" action=\"bypass -h ZeuSNPC DisbandClan 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Restore Clan\" action=\"bypass -h ZeuSNPC RestoreClan 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += "<button value=\"Acquire Clan Skill\" action=\"bypass -h ZeuSNPC learn_clan_skills 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Alliance Options") + central.LineaDivisora(2);
			L2Clan clan = st.getClan();
			if(clan!=null){
				if (clan.getAllyId() == 0) {
					MAIN_HTML += "<button value=\"Create a Alliance\" action=\"bypass -h ZeuSNPC createally 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
				} else {
					MAIN_HTML += "<button value=\"Dissolve Alliance\" action=\"bypass -h ZeuSNPC dissolve_ally 0 0 0\" width=161 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr><br1>";
				}
			}
		}
		MAIN_HTML += "<br><font color=\"303030\"></font>";
		MAIN_HTML += "<br><br>" + central.getPieHTML() + "</center></body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml5(L2PcInstance st, String npcid){
		String MAIN_HTML = "<html><head><title>" + general.TITULO_NPC() + "</title></head><body><center><img src=\"L2UI_CH3.herotower_deco\" width=256 height=32><br><br>";
		MAIN_HTML += "<font color=\"303030\"></font><br>";
		MAIN_HTML += "<table width=260 border=0 bgcolor=444444>";
		MAIN_HTML += "<tr><td align=\"center\"><font color=\"LEVEL\">Confirmation</font></td></tr></table><br>";
		MAIN_HTML += "<img src=\"L2UI.SquareGray\" width=250 height=1><br>";
		MAIN_HTML += "<table width=260 border=0 bgcolor=444444>";
		MAIN_HTML += "<tr><td><br></td></tr>";
		MAIN_HTML += "<tr><td align=\"center\"><font color=\"FF0000\">This option can be seen by GMs only and it<br1>allow to update any changes made in the<br1>script. You can disable this option in<br1>the settings section within the Script.<br><font color=\"LEVEL\">Do you want to update the SCRIPT?</font></font></td></tr>";
		MAIN_HTML += "<tr><td></td></tr></table><br>";
		MAIN_HTML += "<img src=\"L2UI.SquareGray\" width=250 height=1><br><br>";
		MAIN_HTML += "<button value=\"Yes\" action=\"bypass -h ZeuSNPC reloadscript 1 "+npcid+" 0\" width=50 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "<button value=\"No\" action=\"bypass -h ZeuSNPC reloadscript 0 "+npcid+" 0\" width=50 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		MAIN_HTML += "</center></body></html>";
		return MAIN_HTML;
	}

	public static String MainHtml6(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Symbol Maker") + central.LineaDivisora(2);
		MAIN_HTML += "<button value=\"Draw a Symbol\" action=\"bypass -h ZeuSNPC symbol draws 0 0\" width=200 height=31 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\">";
		MAIN_HTML += "<button value=\"Delete a Symbol\" action=\"bypass -h ZeuSNPC symbol deletes 0 0\" width=200 height=31 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm_Down\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\">";
		MAIN_HTML += "<br><font color=\"303030\"></font>";
		MAIN_HTML += "<br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br><br>" + central.getPieHTML()+"</center></body></html>";
		return MAIN_HTML;
	}


	public static String MainHtmlOpcionesVarias(L2PcInstance st){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() +"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Opciones Varias") + central.LineaDivisora(2);

		if(!general.OPCIONES_CHAR_BUFFER_AIO && !general.OPCIONES_CHAR_SEXO && !general.OPCIONES_CHAR_NOBLE && !general.OPCIONES_CHAR_LVL85){
			MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Sorry, but there is no Options Enabled") + central.LineaDivisora(3);
			MAIN_HTML += "<br><br><br><br>" + central.getPieHTML() + "</body></html>";
			return MAIN_HTML;
		}

		if(general.OPCIONES_CHAR_SEXO){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Sex Change\" action=\"bypass -h ZeuSNPC OPCIONESVAR 1 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_SEXO_ITEM_PRICE, "Cost Sex Change");
		}
		if(general.OPCIONES_CHAR_NOBLE){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Noble\" action=\"bypass -h ZeuSNPC OPCIONESVAR 2 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE, "Cost Noble");
		}
		if(general.OPCIONES_CHAR_LVL85){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Level 85\" action=\"bypass -h ZeuSNPC OPCIONESVAR 3 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_LVL85_ITEM_PRICE, "Cost lvl 85");
		}
		if(general.OPCIONES_CHAR_BUFFER_AIO){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\"Char Aio Buffer\" action=\"bypass -h ZeuSNPC OPCIONESVAR 4 0 0\" width=128 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_BUFFER_AIO_PRICE, "Cost AIO Buffer");
		}
		if(general.OPCIONES_CHAR_FAME){
			MAIN_HTML += central.LineaDivisora(1) + central.headFormat("<button value=\""+ String.valueOf(general.OPCIONES_CHAR_FAME_GIVE) +" of Fame\" action=\"bypass -h ZeuSNPC OPCIONESVAR 5 0 0\" width=188 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","LEVEL");
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_FAME_PRICE, "Cost To Add " + String.valueOf(general.OPCIONES_CHAR_FAME_GIVE) + " of fame" );
		}

		MAIN_HTML += "</center>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}


	public static String _Menu_CHANGE_NAME_PJ(L2PcInstance st,String seccion){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Player Change Name") + central.LineaDivisora(2);
		String TEXTO_NUEVO_NOM = "<edit type=\"seguridad\" var=\"NEW_NOM\" width=150>";
		String BOTON_ACEPTAR = "<button value=\"Check and Change\" action=\"bypass -h ZeuSNPC "+seccion+" 1 1 $NEW_NOM \" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		if(seccion.equals("USER_CHANGE_NAME")){
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE);
		}
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Enter new Name" + TEXTO_NUEVO_NOM + "<br>" + BOTON_ACEPTAR +"<br>","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	public static String _Menu_CHANGE_NAME_CLAN(L2PcInstance st, String seccion){
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Clan Change Name") + central.LineaDivisora(2);
		String TEXTO_NUEVO_NOM = "<edit type=\"seguridad\" var=\"NEW_NOM\" width=150>";
		String BOTON_ACEPTAR = "<button value=\"Check and Change\" action=\"bypass -h ZeuSNPC "+general.QUEST_INFO+" "+seccion+" 2 1 $NEW_NOM \" width=180 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		if(seccion.equals("USER_CHANGE_NAME")){
			MAIN_HTML += central.ItemNeedShowBox(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE);
		}
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Enter new Name" + TEXTO_NUEVO_NOM + "<br>" + BOTON_ACEPTAR +"<br>","WHITE") + central.LineaDivisora(2);
		MAIN_HTML += central.getPieHTML()+ "</body></html>";
		return MAIN_HTML;
	}



	public static String DelevelMenu(L2PcInstance st){
		return "";
	}

	public static void showWindowsKnowZeus(L2PcInstance cha){
		
	}

	public static void showWindowsKnowZeusAdmin(L2PcInstance cha){

		
	}





}
