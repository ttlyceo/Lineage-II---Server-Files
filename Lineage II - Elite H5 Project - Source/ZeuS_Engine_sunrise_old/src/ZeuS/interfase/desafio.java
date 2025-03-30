package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.GameServer;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Config.general;

import ZeuS.ZeuS.ZeuS;
import ZeuS.admin.menu;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class desafio {

	private static final Logger _log = Logger.getLogger(desafio.class.getName());
	
	private static Map<String, String> lvl85 = new HashMap<String, String>();
	private static Map<String, String> Noble = new HashMap<String, String>();
	private static Vector<String> Namelvl85 = new Vector<String>();
	private static Vector<String> NameNoble = new Vector<String>();	
	
	private static boolean isWinnerDualBox_85(L2PcInstance player){
		String IPInternet = ZeuS.getIp_Wan(player);
		String IPLan = ZeuS.getIp_pc(player);
		if(lvl85!=null){
			if(lvl85.containsKey(IPInternet)){
				if(lvl85.get(IPInternet).equals(IPLan)){
					Namelvl85.add(player.getName());
					return false;
				}
			}
		}
		
		if(Namelvl85!=null){
			if(Namelvl85.contains(player.getName())){
				return false;
			}
		}
		
		lvl85.put(IPInternet, IPLan);
		return true;		
	}
	
	private static boolean isWinnerDualBox_Noble(L2PcInstance player){
		String IPInternet = ZeuS.getIp_Wan(player);
		String IPLan = ZeuS.getIp_pc(player);
		if(Noble!=null){
			if(Noble.containsKey(IPInternet)){
				if(Noble.get(IPInternet).equals(IPLan)){
					NameNoble.add(player.getName());
					return false;
				}
			}
		}
		if(NameNoble!=null){
			if(NameNoble.contains(player.getName())){
				return false;
			}
		}		
		
		Noble.put(IPInternet, IPLan);
		return true;		
	}	

	private static class SingletonHolder
	{
		protected static final desafio _instance = new desafio();
	}

	public static desafio getInstance()
	{
		return SingletonHolder._instance;
	}

	static Connection conn;
	static CallableStatement psqry;
	static ResultSet rss;

	public static void AgregarPremios(L2PcInstance st, String eventParam1,String eventParam2, String eventParam3){
		if(!general._activated()){
			return;
		}

		String qry = "call sp_npc_evento_operaciones(4,"+ eventParam2 +","+ eventParam3 +"," + eventParam1 + ")";
		try{
		Connection conn = L2DatabaseFactory.getInstance().getConnection();
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();
		while (rss.next()) {
			central.msgbox(rss.getString(2),st);
		}
		try{
			conn.close();
		}catch(SQLException e1){

		}
		}catch(SQLException e){

		}
	}

	private static void desafioCheck(L2PcInstance st, String opc){
		if(!general._activated()){
			return ;
		}
			if(opc.equals("1")){
				if(st.getLevel() == 85){
					int[] estaDis = central.getStatusDesafio(st);
					if(estaDis[1]< general.DESAFIO_MAX_LVL85){
						if(!isWinnerDualBox_85(st)){
							central.msgbox("You've won with another character", st);
							return;
						}
						if(central.EjecutarDesafio(st, "1").equals("cor")){
							central.msgbox(language.getInstance().getMsg(st).CHALLENGE_WIN,st);
							opera.giveReward(st, general.DESAFIO_85_PREMIO);
							opera.AnunciarTodos("Challenge", language.getInstance().getMsg(st).CHALLENGE_LV85_WIN.replace("$player",st.getName()).replace("$posi",String.valueOf(estaDis[1] + 1)).replace("$total", String.valueOf(general.DESAFIO_MAX_LVL85)) );
							opera.AnunciarTodos("Challenge","Reward: "+central.ItemShowReward_annoucement(general.DESAFIO_85_PREMIO));
						}else{
							central.msgbox("You've already won",st);
						}
					}else{
						central.msgbox(language.getInstance().getMsg(st).CHALLENGE_LOOSE,st);
					}
				}else{
					central.msgbox(language.getInstance().getMsg(st).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", "85"),st);
				}
			}
			if(opc.equals("2")){
				if(st.isNoble()){
					int[] estaDis = central.getStatusDesafio(st);
					if(estaDis[2] < general.DESAFIO_MAX_NOBLE) {
						if(!isWinnerDualBox_Noble(st)){
							central.msgbox("You've won with another character", st);
							return;
						}						
						if(central.EjecutarDesafio(st, "2").equals("cor")){
							central.msgbox(language.getInstance().getMsg(st).CHALLENGE_WIN,st);
							opera.giveReward(st, general.DESAFIO_NOBLE_PREMIO);
							opera.AnunciarTodos("Challenge", language.getInstance().getMsg(st).CHALLENGE_NOBLE_WIN.replace("$player",st.getName()).replace("$posi",String.valueOf(estaDis[2] + 1)).replace("$total", String.valueOf(general.DESAFIO_MAX_NOBLE)) );
							opera.AnunciarTodos("Challenge","Reward: "+central.ItemShowReward_annoucement(general.DESAFIO_NOBLE_PREMIO));
						}else{
							central.msgbox("You've already won",st);
						}
					}else{
						central.msgbox(language.getInstance().getMsg(st).CHALLENGE_LOOSE,st);
					}
				}else{
					central.msgbox(language.getInstance().getMsg(st).NEED_TO_BE_NOBLE,st);
				}
			}
	}
	private static void DeleteNpcDesafio(L2PcInstance st, String eventParam2, String eventParam3){

		String qry = "call sp_npc_evento_operaciones(1,0,0," + eventParam2 + ")";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			while (rss.next()) {
				central.msgbox(rss.getString(2),st);
			}
			try{
				conn.close();
			}catch(SQLException e1){

			}
		}catch(SQLException e){

		}

		try{
			for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(Integer.valueOf(eventParam3))){
				L2Npc npcLocal = SpawnLo.getLastSpawn();
				npcLocal.deleteMe();
				SpawnLo.stopRespawn();
				SpawnTable.getInstance().deleteSpawn(SpawnLo, true);
			}
		}catch(Exception a){
			_log.warning(a.getMessage());
		}



	}

	private static void borraItemDesafio(L2PcInstance st, String eventParam3){
		String qry = "call sp_npc_evento_operaciones(3,0,0," + eventParam3 + ")";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			while (rss.next()) {
				central.msgbox(rss.getString(2),st);
			}
			try{
				conn.close();
			}catch (SQLException e) {

			}
		}catch (SQLException e) {

		}
	}

	private static void borraItemFamilia(L2PcInstance st, String eventParam2, String eventParam3){
		String qry = "call sp_npc_evento_operaciones(6,"+eventParam2+",0," + eventParam3 + ")";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			while (rss.next()) {
				central.msgbox(rss.getString(2),st);
			}
			try{
				conn.close();
			}catch (SQLException e2) {

			}
		}catch(SQLException e1){

		}
	}

	public static String Desafio(L2PcInstance st,String eventParam1, String eventParam2, String eventParam3){
		if(!general._activated()){
			return "";
		}
		if(eventParam1.equals("0")) {
			return DesafioMenu(st);
		}
		if(eventParam1.equals("1")){
			desafioCheck(st,eventParam2);
			return DesafioMenu(st);
		}
		if (eventParam1.equals("2")) {
			return ResultadosDesafio(st);
		}
		if (eventParam1.equals("3")) {
			return htmls.DesafioVerPremios(st);
		}
		if(eventParam1.equals("88")){
			if(eventParam2.equals("0")) {
				return menuConfigDesafio(st);
			} else{
				DeleteNpcDesafio(st,eventParam2,eventParam3);
				return menuConfigDesafio(st);
			}
		}
		if(eventParam1.equals("89")){
			if(eventParam2.equals("0")) {
				return menuConfigDesafioPremios(st,-1);
			}
			if(eventParam2.equals("1")){
				borraItemDesafio(st,eventParam3);
				return menuConfigDesafioPremios(st,-1);
			}
		}
		if(eventParam1.equals("90")){
			borraItemFamilia(st,eventParam2,eventParam3);
			return menuConfigDesafioPremios(st,-1);
		}
		if(eventParam1.equals("91")){
			borraItemFamilia(st,eventParam2,eventParam3);
			return menuConfigAddPremiosDesafio(st,Integer.valueOf(eventParam3));
		}
		return "";
	}



	public static String menuConfigDesafioGetFamilias(L2PcInstance st){
		String MAIN_HTML ="";
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Configuración de Busqueda de NPC (Familias)") + central.LineaDivisora(2);
		String strBotonAgregar = "<center><button value=\"Add Family and/or reward\" action=\"bypass -h ZeuSNPC DESAFIOADD 0 0 0\" width=185 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		String strAtras = "<center><button value=\"Back\" action=\"bypass -h ZeuSNPC DESAFIO 88 0 0\" width=85 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat(strBotonAgregar) + central.LineaDivisora(2) + central.LineaDivisora(1);
		String BotonVerDetalleFamilia = "";
		try{
			Connection conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_npc_evento_operaciones(7,-1,-1,-1)";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				BotonVerDetalleFamilia = "<center><button value=\"See\" action=\"bypass -h ZeuSNPC DesafioVerDetaFami "+ String.valueOf(rss.getInt(1)) + " 0 0\" width=65 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat("Family " + String.valueOf(rss.getInt(1)) + " with " + String.valueOf(rss.getInt(2)) +" reward "+ BotonVerDetalleFamilia );
			}
			conn.close();
			}
			catch(SQLException e){

			}

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(strAtras) + central.LineaDivisora(1) + central.getPieHTML() + "</body></html>";

		return MAIN_HTML;
	}


	public static String menuConfigDesafioPremios(L2PcInstance st, int ListaCarga){
		int Cargando;
		if (ListaCarga <=0) {
			//Cargando = 2;
			return menuConfigDesafioGetFamilias(st);
		} else {
			Cargando = 5;
		}


		if (!opera.isMaster(st)) {
			return "";
		}
		String BOTON_ACTUALIZAR = "<button value=\"update\" action=\"bypass -h ZeuSNPC DESAFIO 89 0 0\" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTON_AGREGAR = "<button value=\"add\" action=\"bypass -h ZeuSNPC DESAFIOADD 0 0 0\" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTON_ATRAS = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DESAFIO 88 0 0\" width=50 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BOTON = "<table  width=280><tr><td align=left width=100>"+BOTON_ACTUALIZAR+"</td><td align=CENTER width=50>"+BOTON_ATRAS+"</td><td align=right width=100>"+BOTON_AGREGAR+"</td></tr></table>";
		String MAIN_HTML = "";
		if (ListaCarga <= 0){
			MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>";
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config. NPC Search (Reward)") + central.LineaDivisora(2);
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON,"LEVEL") + central.LineaDivisora(2);
		}
		int IDPremioCAP = -1;
		String HTML_MIENTRAS = "";
		String COLORES_FONDO[] = {"4B610B","5E610B"};
		int ID_ITEM =0;
		String COLOR_TITULO = "0A2229";
		try{
		Connection conn = L2DatabaseFactory.getInstance().getConnection();
		String qry = "call sp_npc_evento_operaciones("+String.valueOf(Cargando)+",1,1,"+String.valueOf(ListaCarga)+")";
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();

		while (rss.next()){
			if(!rss.getString(1).equals("err")){
				if(IDPremioCAP != rss.getInt(2)){
					String BOTON_BORRAR_FAMILIA;
					if(ListaCarga <= 0) {
						BOTON_BORRAR_FAMILIA = "<button value=\"Supr F.\" action=\"bypass -h ZeuSNPC DESAFIO 89 1 " + String.valueOf(rss.getInt(2)) + "\" width=65 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
					} else {
						BOTON_BORRAR_FAMILIA = "";
					}

					IDPremioCAP = rss.getInt(2);
					String INFOFAMILIA = "<table width = 280 bgcolor="+COLOR_TITULO+"><tr><td align=CENTER width = 200>FAMILY ID: "+String.valueOf(rss.getInt(2))+" - Given: " + rss.getString(5) + "</td><td align=CENTER width = 80>"+ BOTON_BORRAR_FAMILIA +"</td></tr></table>";
					if(HTML_MIENTRAS.length()==0) {
						HTML_MIENTRAS = INFOFAMILIA + "<table width = 280 bgcolor="+COLORES_FONDO[central.INT_PINTAR_GRILLA(-1)]+">";
					} else {
						HTML_MIENTRAS += "</table>"+central.LineaDivisora(2)+"<br>"+INFOFAMILIA+"<table width = 280 bgcolor="+COLORES_FONDO[central.INT_PINTAR_GRILLA(-1)]+">";
					}
				}
				ID_ITEM = rss.getInt(3);
				String BOTON_BORRAR_ITEM_FAMILIA;
				if (ListaCarga <=0) {
					BOTON_BORRAR_ITEM_FAMILIA = "<button value=\"Supr.\" action=\"bypass -h ZeuSNPC DESAFIO 90 " + String.valueOf(rss.getInt(3)) + " " + String.valueOf(rss.getInt(2)) + "\" width=45 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				} else {
					BOTON_BORRAR_ITEM_FAMILIA = "<button value=\"Supr.\" action=\"bypass -h ZeuSNPC DESAFIO 91 " + String.valueOf(rss.getInt(3)) + " " + String.valueOf(rss.getInt(2)) + "\" width=45 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				}
				HTML_MIENTRAS += "<tr><td width=230 align=center>" + central.getNombreITEMbyID(ID_ITEM) + " ("+ String.valueOf(rss.getInt(4)) +",["+String.valueOf(rss.getInt(3))+"]) </td><td width=50 align=center>"+BOTON_BORRAR_ITEM_FAMILIA+"</td></tr>";
			}
		}
		conn.close();
		}
		catch(SQLException e){

		}
		if(HTML_MIENTRAS.length()>0) {
			MAIN_HTML += HTML_MIENTRAS + "</table>" + central.LineaDivisora(2);
		}
		if(ListaCarga <= 0) {
			MAIN_HTML += central.getPieHTML() + "</body></html>";
		}

		return MAIN_HTML;
	}

	public static String menuConfigDesafio(L2PcInstance st){
		String MAIN_HTML="";
		if (!opera.isMaster(st)) {
			return
		MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		}
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config NPC Search") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("NPC Found","00FFFF") + central.LineaDivisora(2);
		String BOTON = "<table width=260><tr>";
		BOTON += "<td width=105 align=left><button value=\"UPDATE\" action=\"bypass -h " + menu.bypassNom +" DESAFIO 88 0 0\" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		BOTON += "<td width=50 align=center><button value=\"BACK\" action=\"bypass -h "+ menu.bypassNom +" setConfig1 desafio 0 0 0 0 0\" width=50 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		BOTON += "<td width=105 align=right><button value=\"REWARD\" action=\"bypass -h "+ menu.bypassNom +" DESAFIO 89 0 0\" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		BOTON +="</tr></table";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON,"00FFFF") + central.LineaDivisora(2);
		try{
		Connection conn;
		conn = L2DatabaseFactory.getInstance().getConnection();
		String qry = "call sp_npc_busqueda_resul(1," + String.valueOf(st.getObjectId()) + ")";
		CallableStatement psqry = conn.prepareCall(qry);
		ResultSet rss = psqry.executeQuery();
		String BotonIRNPC ="";
		String Direccion = "";
		String[] DireccionMa;
		while (rss.next()){
			if(!rss.getString(1).equals("err")){
				BotonIRNPC = "<table><tr>";
				if(!rss.getString(3).equals("err")){
					Direccion = rss.getString(3);
					DireccionMa = Direccion.split(",");
					BotonIRNPC += "<td><button value=\"where was it?\" action=\"bypass -h admin_move_to " + DireccionMa[0] + " " + DireccionMa[1] + " " + DireccionMa[2] + "\" width=100 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
				} else {
					BotonIRNPC += "<td></td>";
				}
				BotonIRNPC += "<td><button value=\"Supr Info\" action=\"bypass -h "+ menu.bypassNom +" DESAFIO 88 "+String.valueOf(rss.getInt(5))+" "+String.valueOf(rss.getInt(4))+"\" width=100 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
				MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Player: " + rss.getString(1) + " <br1>ID NPC: " + String.valueOf(rss.getInt(4)) + "<br1>" + BotonIRNPC + "<br1>Fecha: " + rss.getString(2) ,"LEVEL") + central.LineaDivisora(2);
			}
		}
		conn.close();
		}catch(SQLException e){

		}
		MAIN_HTML += central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}


	public static String DesafioMenu(L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		int[] estaDis = central.getStatusDesafio(st);
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("The Challenge - " + st.getName()) + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("The Challenge Event","00FFFF") + central.LineaDivisora(2);

		if(general.DESAFIO_LVL85){
			MAIN_HTML += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTML += "<td align=left width=220>Level 85, Remain: <font color=\"FF6308\">"+ String.valueOf( general.DESAFIO_MAX_LVL85 - estaDis[1] ) +"</font> of <font color=\"LEVEL\">"+ String.valueOf(general.DESAFIO_MAX_LVL85) +"</font>.</td>";
			MAIN_HTML += "<td width=80 align=left><button value=\"Check\" action=\"bypass -h ZeuSNPC DESAFIO 1 1 0\" width=45 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			MAIN_HTML += "</tr></table>";
			MAIN_HTML += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTML += "<td align=center width=280 fixwidth=270>"+ "".replace("$total85", String.valueOf(general.DESAFIO_MAX_LVL85)) +"</td></tr>";
			if(general.DESAFIO_85_PREMIO!=null){
				if(general.DESAFIO_85_PREMIO.length()>0){
					String temporal = "<table width=250 align=CENTER>";
					temporal+= "<tr><td width=190 fixwidth=190 align=CENTER><font color=\"58C08C\">Reward</font></td></tr>";
					for(String Items : general.DESAFIO_85_PREMIO.split(";")){
						temporal+= "<tr><td width=250 fixwidth=240 align=CENTER><font color=\"E5C384\"> "+ opera.getFormatNumbers(Items.split(",")[1]) +" " + central.getNombreITEMbyID(Integer.valueOf( Items.split(",")[0] ))  + "</font></td></tr>";
					}
					temporal += "</table><br>";
					MAIN_HTML += "<tr><td align=CENTER width=280 fixwidth=270>"+ temporal +"</td></tr>";
				}
			}
			MAIN_HTML += "</table>";
			MAIN_HTML += central.LineaDivisora(2);
		}

		if(general.DESAFIO_NOBLE){
			MAIN_HTML += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTML += "<td align=left width=220>Nobles, Remain: <font color=\"FF6308\">"+ String.valueOf(general.DESAFIO_MAX_NOBLE - estaDis[2]) +"</font> of <font color=\"LEVEL\">"+ String.valueOf(general.DESAFIO_MAX_NOBLE) +"</font>.</td>";
			MAIN_HTML += "<td width=80 align=left><button value=\"Check\" action=\"bypass -h ZeuSNPC DESAFIO 1 2 0\" width=45 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			MAIN_HTML += "</tr></table>";
			MAIN_HTML += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTML += "<td align=center width=280>"+ "".replace("$totalnoble", String.valueOf(general.DESAFIO_MAX_NOBLE)) +"</td></tr>";
			if(general.DESAFIO_NOBLE_PREMIO!=null){
				if(general.DESAFIO_NOBLE_PREMIO.length()>0){
					String temporal = "<table width=250 align=CENTER>";
					temporal+= "<tr><td width=190 fixwidth=190 align=CENTER><font color=\"58C08C\">Reward</font></td></tr>";
					for(String Items : general.DESAFIO_NOBLE_PREMIO.split(";")){
						temporal+= "<tr><td width=250 fixwidth=240 align=CENTER><font color=\"E5C384\"> "+ opera.getFormatNumbers(Items.split(",")[1]) +" " + central.getNombreITEMbyID(Integer.valueOf( Items.split(",")[0] ))  + "</font></td></tr>";
					}
					temporal += "</table><br>";
					MAIN_HTML += "<tr><td align=CENTER width=280 fixwidth=270>"+ temporal +"</td></tr>";
				}
			}
			MAIN_HTML += "</table>";
			MAIN_HTML += "</center>";
			MAIN_HTML += central.LineaDivisora(2);
		}

		if(!general.DESAFIO_NPC || (general.DESAFIO_NPC_BUSQUEDAS.size()>0)){
			String BOTON_VER_PREMIO = "<table width=270><tr><td width=270 align=CENTER><button value=\"Ver premios\" action=\"bypass -h ZeuSNPC DESAFIO 3 0 0\" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
			MAIN_HTML += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTML += "<td align=left width=220>Hidden NPC, Remain: <font color=\"FF6308\">"+ String.valueOf( general.DESAFIO_NPC_BUSQUEDAS.size() - central.getStatusDesafioBusNPC() ) +"</font> of <font color=\"LEVEL\">"+ String.valueOf(general.DESAFIO_NPC_BUSQUEDAS.size()) +"</font>.</td>";
			MAIN_HTML += "<td width=80 align=left><button value=\"View\" action=\"bypass -h ZeuSNPC DESAFIO 2 0 0\" width=45 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>";
			MAIN_HTML += "</tr></table>";
			MAIN_HTML += "<table width=280 border=0 bgcolor=151515><tr>";
			MAIN_HTML += "<td align=center width=260>"+"".replace("$total", String.valueOf(general.DESAFIO_NPC_BUSQUEDAS.size()) ) +BOTON_VER_PREMIO+"</td></tr>";
			MAIN_HTML += "</table><br>";
			MAIN_HTML += "</center>";
		}
		MAIN_HTML += "<br>"+central.getPieHTML()+"</body></html>";
		return MAIN_HTML;
	}



	public static String ResultadosDesafio(L2PcInstance st){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Result Search Hidden NPC") + central.LineaDivisora(2);
		String BOTON_ATRAS = "<table width=260><tr><td width=260 align=CENTER><button value=\"Back\" action=\"bypass -h ZeuSNPC DESAFIO 0 0 0\" width=50 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
		try{
			Connection conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_npc_busqueda_resul(1," + String.valueOf(st.getObjectId()) + ")";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			String Direccion ="";
			String DireccionMa[];
			String BotonIRNPC ="";
			while (rss.next()){
				if (!rss.getString(1).equals("err")){
					if(!rss.getString(3).equals("err")){
						Direccion = rss.getString(3);
						DireccionMa = Direccion.split(",");
						BotonIRNPC = "<button value=\"Where was It?\" action=\"bypass -h ZeuSNPC TELEPORT_TO_FREE " + DireccionMa[0] + " " + DireccionMa[1] + " " + DireccionMa[2] + "\" width=100 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
					} else {
						BotonIRNPC = "";
					}
					MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Player: " + rss.getString(1) + " " + BotonIRNPC + "<br1>Date: " + rss.getString(2) ,"LEVEL") + central.LineaDivisora(2);
				}
			}
			try{
				conn.close();
			}catch(SQLException a){

			}
		}catch(SQLException e){

		}


		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_ATRAS,"LEVEL") + central.LineaDivisora(2);
		MAIN_HTML += "</body></html><br>";
		return MAIN_HTML;
	}

	public static String menuConfigAddPremiosDesafio(L2PcInstance st, int InListaMirando){
		if(!general._activated()){
			return "";
		}
		if(!opera.isMaster(st)) {
			return "";
		}

		int[] forFamilia = {1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20};

		String strComboFamilia = "";
		if(InListaMirando>0){
			strComboFamilia = String.valueOf(InListaMirando);
		}

		for(int _famiInterna: forFamilia){
			if(_famiInterna != InListaMirando){
				if(strComboFamilia.length()>0){
					strComboFamilia += ";";
				}
				strComboFamilia += String.valueOf(_famiInterna);
			}
		}

		String COMBO_FAMILIAS = "<combobox width=38 var=cmbFamilia list="+strComboFamilia+">";
		String TXTIDITEM = "<edit var=\"txtIDItem\" width=50>";
		String TXTCANTIDAD = "<edit var=\"txtCantidad\" width=120>";

		String BOTON_RETROCEDER = "<button value=\"Back\" action=\"bypass -h ZeuSNPC DESAFIO 89 0 0\" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String BOTON_ACEPTAR = "<button value=\"Add\" action=\"bypass -h ZeuSNPC DESAFIOADD $cmbFamilia $txtIDItem $txtCantidad \" width=95 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String MAIN_HTML = "<html><title>" + general.TITULO_NPC() + "(Premios)</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Config. NPC Search (Add Reward)") + central.LineaDivisora(2);
		MAIN_HTML += central.headFormat("<table width = 280><tr><td width=140 align=right>Select Family:</td><td width=140>" + COMBO_FAMILIAS + "</td></tr><tr><td><br></td></tr></table>","LEVEL");
		MAIN_HTML += central.headFormat("<table width = 280><tr><td width=140 align=right>Item ID:</td><td width=140>" + TXTIDITEM + "</td></tr><tr><td><br></td></tr></table>","LEVEL");
		MAIN_HTML += central.headFormat("<table width = 280><tr><td width=140 align=right>Amount:</td><td width=140>" + TXTCANTIDAD + "</td></tr><tr><td><br></td></tr></table>","LEVEL");
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON_ACEPTAR+BOTON_RETROCEDER,"LEVEL") + central.LineaDivisora(2) + central.LineaDivisora(2);

		if(InListaMirando > 0) {
			MAIN_HTML += menuConfigDesafioPremios(st, InListaMirando);
		}

		MAIN_HTML += central.getPieHTML()+"</body></html>";

		return MAIN_HTML;
	}


}
