package ZeuS.Comunidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.ZeuS.ZeuS;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.TradeItem;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.instance.PcInstance.L2PcFishing;
import l2r.gameserver.model.itemcontainer.PcInventory;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.ShowBoard;
import ZeuS.Comunidad.cbManager.enumColor;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Config._dealy_reward_system;
import ZeuS.Config._topplayer;
import ZeuS.Config.general;
import ZeuS.dressme.dressme;
import ZeuS.interfase.central;
import ZeuS.interfase.jailBail;
import ZeuS.interfase.sellBuff;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import gr.sr.main.ClassNamesHolder;
import gr.sr.utils.Tools;

public class Comunidad {
	
	
	
	private static Logger _log = Logger.getLogger(Comunidad.class.getName());
	private final static String btnMain = "<button value=\"MAIN\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + "\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
	public static Map<L2PcInstance, Integer> LastIdModifMensaje = new HashMap<L2PcInstance, Integer>();
	public static Map<L2PcInstance, Integer> PaginaVerAllPlayer = new HashMap<L2PcInstance, Integer>();

	public static Map<Integer, String> textToSearch = new HashMap<Integer, String>();
	public static Map<Integer, Vector<String>> BotonesBusqueda = new HashMap<Integer, Vector<String>>();
	public static Map<Integer, Integer> idBusquedaPS = new HashMap<Integer, Integer>();
	private static Map<Integer, _infoAnnou> INFO_MSJE_COMMUNITY = new HashMap<Integer, _infoAnnou>();
	private static Map<String, Vector<_infoAnnou>> INFO_MSJE_COMMUNITY_BY_TYPE = new HashMap<String, Vector<_infoAnnou>>();
	private static _infoAnnou LastAnnoucement;
	private static String getStatus(L2PcInstance player, boolean estado){
		return estado ? language.getInstance().getMsg(player).BUTTON_ENABLE : language.getInstance().getMsg(player).BUTTON_DISABLE;
	}
	
	private static String[]CENTRAL_ACCESS = {
			Engine.enumBypass.ClasesStadistic.name(),
			Engine.enumBypass.RaidBossInfo.name(),
			Engine.enumBypass.donation.name(),
			Engine.enumBypass.Buffer.name(),
			Engine.enumBypass.Gopartyleader.name(),
			Engine.enumBypass.Flagfinder.name(),
			Engine.enumBypass.Teleport.name(),
			Engine.enumBypass.Shop.name(),
			Engine.enumBypass.Warehouse.name(),
			Engine.enumBypass.AugmentManager.name(),
			Engine.enumBypass.SubClass.name(),
			Engine.enumBypass.Profession.name(),
			Engine.enumBypass.DropSearch.name(),
			Engine.enumBypass.pvppklog.name(),
			Engine.enumBypass.Symbolmaker.name(),
			Engine.enumBypass.BugReport.name(),
			Engine.enumBypass.Transformation.name(),
			Engine.enumBypass.RemoveAttri.name(),
			Engine.enumBypass.SelectAugment.name(),
			Engine.enumBypass.SelectEnchant.name(),
			Engine.enumBypass.SelectElemental.name(),
			Engine.enumBypass.blacksmith.name(),
			Engine.enumBypass.charclanoption.name(),
			Engine.enumBypass.Dressme.name(),
			Engine.enumBypass.partymatching.name(),
			Engine.enumBypass.AuctionHouse.name(),
			Engine.enumBypass.castleManager.name(),
			Engine.enumBypass.gmlist.name(),
			Engine.enumBypass.OlyBuffer.name(),
			Engine.enumBypass.commandinfo.name(),
			Engine.enumBypass.colornametitle.name()
	};
	
	public static int getSizeMainOption(){
		return CENTRAL_ACCESS.length;
	}


	private static Map<String, String> getAllProperties(L2PcInstance player){
		Map<String, String> propiedades = new HashMap<String, String>();
		propiedades.put("Exp:", String.valueOf(Config.RATE_XP));
		propiedades.put("Sp:", String.valueOf(Config.RATE_SP));
		propiedades.put("Party Exp:", String.valueOf(Config.RATE_PARTY_XP));
		propiedades.put("Party Sp:", String.valueOf(Config.RATE_PARTY_SP));
		propiedades.put("Adena:", String.valueOf(Config.RATE_DROP_ITEMS_ID.get(PcInventory.ADENA_ID)));

		propiedades.put("Drop:", String.valueOf(Config.RATE_DROP_ITEMS));
		propiedades.put("Spoil:", String.valueOf(Config.RATE_DROP_SPOIL));
		propiedades.put("Manor:", String.valueOf(Config.RATE_DROP_MANOR));
		propiedades.put("Quest Drop:", String.valueOf(Config.RATE_QUEST_DROP));
		propiedades.put("Quest Reward:", String.valueOf(Config.RATE_QUEST_REWARD));
		propiedades.put("Quest Adena:", String.valueOf(Config.RATE_QUEST_REWARD_ADENA));
		propiedades.put("Karma Exp Lost:", String.valueOf(Config.RATE_KARMA_EXP_LOST));
		propiedades.put("Weight Limit:", String.valueOf(Config.ALT_WEIGHT_LIMIT));
		propiedades.put("Maximum Slot for No Dwarf:", String.valueOf(Config.INVENTORY_MAXIMUM_NO_DWARF));
		propiedades.put("Maximum Slot for Dwarf:", String.valueOf(Config.INVENTORY_MAXIMUM_DWARF));
		propiedades.put("Maximum Slot WH for No Dwarf:", String.valueOf(Config.WAREHOUSE_SLOTS_NO_DWARF));
		propiedades.put("Maximum Slot WH for Dwarf:", String.valueOf(Config.WAREHOUSE_SLOTS_DWARF));

		propiedades.put("F. to get's Fame Fortress:(Min)", String.valueOf(Config.FORTRESS_ZONE_FAME_TASK_FREQUENCY / 60));
		propiedades.put("F. to get's Fame Castle:(Min)", String.valueOf(Config.CASTLE_ZONE_FAME_TASK_FREQUENCY / 60));

		propiedades.put("Fame Fortress Siege Zone:", String.valueOf(Config.FORTRESS_ZONE_FAME_AQUIRE_POINTS));
		propiedades.put("Fame Castle Zone:", String.valueOf(Config.CASTLE_ZONE_FAME_AQUIRE_POINTS));

		propiedades.put("Starting Adena:", String.valueOf(Config.STARTING_ADENA));
		propiedades.put("Buff:", String.valueOf(Config.BUFFS_MAX_AMOUNT) +" Buff's & " + String.valueOf(Config.DANCES_MAX_AMOUNT)+ " Dances");
		propiedades.put("Max Alliance:", String.valueOf(Config.ALT_MAX_NUM_OF_CLANS_IN_ALLY));
		propiedades.put("Max Subclases:", String.valueOf(Config.MAX_SUBCLASS));
		propiedades.put("Subclases base Level:", String.valueOf(Config.BASE_SUBCLASS_LEVEL));
		propiedades.put("Subclases Max Level:", String.valueOf(Config.MAX_SUBCLASS_LEVEL));
		propiedades.put("Unstuck Seg:", String.valueOf(Config.UNSTUCK_INTERVAL));
		propiedades.put("Day Before Join a Clan:", String.valueOf(Config.ALT_CLAN_JOIN_DAYS));
		propiedades.put("Delete Char After Days:", String.valueOf(Config.DELETE_DAYS));
		propiedades.put("Antibot Target:", getStatus(player, general.ANTIBOT_COMANDO_STATUS));
		propiedades.put("Antibot Automatic:", getStatus(player, general.ANTIBOT_AUTO));
		propiedades.put("Dressme:", getStatus(player, general.STATUS_DRESSME));
		propiedades.put("Char panel:", getStatus(player, general.CHAR_PANEL));
		propiedades.put("Premium Char:", getStatus(player, general.PREMIUM_CHAR));
		propiedades.put("Premium Clan:", getStatus(player, general.PREMIUM_CLAN));

		if(general.getCommunityServerInfo()!=null){
			if(general.getCommunityServerInfo().size()>0){
				for(String p_in : general.getCommunityServerInfo()){
					propiedades.put(p_in.split(":")[0]+":" , p_in.split(":")[1]);
				}
			}
		}

		return propiedades;
	}

	@SuppressWarnings("unused")
	private static String getAllPlayerFromClan(L2PcInstance player, int Pagina, int idClan, String NombreClan, String lvlClan){

		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_ALL_MORE;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ lvlClan +";"+ String.valueOf(idClan) +"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_ALL_MENUS;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ lvlClan +";"+ String.valueOf(idClan) +"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnNombreClan = "<button value=\"Back\" width=100 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_SEE;" + String.valueOf(Pagina) + ";"+ String.valueOf(idClan) +";"+NombreClan+";"+lvlClan+"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int MaximoPlayer = 50;

		int Inicio = MaximoPlayer * PaginaVerAllPlayer.get(player);
		int Termino = MaximoPlayer * (PaginaVerAllPlayer.get(player) + 1);

		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">All Player's from "+ NombreClan +" Clan</font>" + btnMain + btnNombreClan);
		String Tabla = "<table width=750 align=CENTER bgColor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) +">" ;
		String consulta = "SELECT characters.char_name FROM characters WHERE characters.clanid = ? ORDER BY characters.char_name ASC LIMIT " + String.valueOf(Inicio) + " , " + String.valueOf(MaximoPlayer + 2);

		String[] Colores = { cbManager.getFontColor(enumColor.Celeste.name()), cbManager.getFontColor(enumColor.Naranjo_Claro.name()), cbManager.getFontColor(enumColor.Verde.name())};
		int contadorColores = 0;

		Connection conn = null;
		PreparedStatement psqry = null;
		int Contador = 1;
		int MaximoColumnas = 5;
		String tamanoColumnas = String.valueOf(750 / MaximoColumnas);
		boolean haveNext = false;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idClan);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if(contadorColores <= MaximoPlayer){
					if(Contador == 1){
						Tabla += "<tr>";
					}
					Tabla += "<td align=CENTER width="+tamanoColumnas+"><a action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHARINFO;"+ String.valueOf(Pagina) +";"+ rss.getString(1) +";"+String.valueOf(idClan)+";"+ NombreClan + "-" + lvlClan +"\">"+
					"<font color="+ Colores[contadorColores%3] +">"+rss.getString(1) +"</font></a></td>";
					if(Contador == MaximoColumnas){
						Tabla += "</tr>";
						Contador = 0;
					}
					Contador++;
				}else{
					haveNext = true;
				}
				contadorColores++;
			}
			if(Contador==0){
				Tabla +="</table>";
			}else{
				for(int i=Contador;i<=MaximoColumnas;i++){
					Tabla += "<td width="+tamanoColumnas+"></td>";
				}
				Tabla += "</tr></table>";
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}


		String tablaBotones = "<table width=720><tr><td width=23>"+( PaginaVerAllPlayer.get(player)>0 ? btnPrevi : "") + "</td><td width=674 align=CENTER>Page "+ String.valueOf(PaginaVerAllPlayer.get(player) + 1)  +"</td><td width=23>"+ (haveNext ? btnNext : "") +"</td></tr></table>";


		retorno += cbManager._formBodyComunidad(Tabla + tablaBotones, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()));

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static void setDeleteReportWindows(L2PcInstance player, int idBugReport){
		String strMySql = "DELETE FROM zeus_bug_report WHERE id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			psqry.setInt(1, idBugReport);
			psqry.executeUpdate();
			psqry.close();
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
	}
	
	private static void setReadBugReport(int idBugReport){
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=L2DatabaseFactory.getInstance().getConnection();
			psqry=conn.prepareStatement("UPDATE zeus_bug_report SET zeus_bug_report.leido = 'SI' WHERE zeus_bug_report.ID = ? ");
			try{
				psqry.setInt(1, idBugReport);
				psqry.executeUpdate();
				psqry.close();
			}catch(SQLException e){
				_log.warning("ERROR E1:" + e.getMessage());
			}
		}catch (SQLException ee) {
			_log.warning("ERROR EE:" + ee.getMessage());
		}
		try{
			conn.close();
		}catch (SQLException e2) {
			_log.warning("ERROR E2:" + e2.getMessage());
		}		
	}

	private static void getBugReportWindows(L2PcInstance player, int idBugReport){
		String retorno = "<html><title>"+ general.NOMBRE_NPC +" Bug Report</title><body>";
		retorno += central.LineaDivisora(1)+central.headFormat("Bug Report",cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name())) + central.LineaDivisora(1) + central.LineaDivisora(3);
		String formatoCabezera = "<table width=270 align=center bgColor=%COLOR%><tr><td width=135 align=CENTER>%TITULO%</td><td width=135 align=CENTER>%DATO%</td></table>" + central.LineaDivisora(1);
		String formatoMensaje = "<table width=270 align=center bgColor=%COLOR%><tr><td width=270 align=CENTER fixwidth=270>%DATO%</td></table>" + central.LineaDivisora(1);
		String[] ColorG = { cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name()), cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) };
		String strMySql = "SELECT * FROM zeus_bug_report WHERE id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			psqry.setInt(1, idBugReport);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					L2PcInstance chaReport = opera.getPlayerbyName(rss.getString(5));
					boolean playerOnline = false;
					if(chaReport!=null){
						if(chaReport.isOnline()){
							if(!chaReport.getClient().isDetached()){
								playerOnline = true;
							}
						}
					}
					retorno += formatoCabezera.replace("%DATO%", rss.getString(5)).replace("%COLOR%", ColorG[0]).replace("%TITULO%", "CHAR NAME:");
					retorno += formatoCabezera.replace("%DATO%", ( playerOnline ? "YES" : "NO" ) ).replace("%COLOR%", ColorG[1]).replace("%TITULO%", "ONLINE:");
					retorno += formatoCabezera.replace("%DATO%", rss.getString(4)).replace("%COLOR%", ColorG[0]).replace("%TITULO%", "DATE:");
					retorno += formatoMensaje.replace("%DATO%", "REPORT").replace("%COLOR%", ColorG[1]);
					retorno += formatoMensaje.replace("%DATO%", rss.getString(3)).replace("%COLOR%", ColorG[0]);
					retorno += formatoMensaje.replace("%DATO%", "ANSWER").replace("%COLOR%", ColorG[1]);
					retorno += formatoMensaje.replace("%DATO%", "<MultiEdit var =\"Content\" width=260 height=100><br><button value=\"Send\" width=80 height=24 action=\"bypass -h ZeuS bugreport "+ rss.getString(5) +" $Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>").replace("%COLOR%", ColorG[0]);
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		setReadBugReport(idBugReport);

		retorno += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, retorno);
	}

	private static String getBugReport(L2PcInstance player, int Pagina){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Bug Report's</font>" + btnMain);

		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";BUG_REPORT;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";BUG_REPORT;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String colorHead = cbManager.getFontColor(cbManager.enumColor.Verde.name());

		String Tabla = "<table width=720 align=center bgcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name())+">" +
						"<tr>"+
							"<td width=18 align=CENTER><font color="+colorHead+">N</font></td>"+
							"<td width=170 align=CENTER><font color="+colorHead+">DATE</font></td>"+
							"<td width=110 align=CENTER><font color="+colorHead+">TYPE</font></td>"+
							"<td width=130 align=CENTER><font color="+colorHead+">PLAYER</font></td>"+
							"<td width=262 align=CENTER><font color="+colorHead+">REPORT</font></td>"+
							"<td width=30 align=CENTER><font color="+colorHead+">SEE</font></td>"+
						"</tr>";

		int MaximoLineas = 10;
		int Desde = MaximoLineas * Pagina;
		boolean haveNext = false;
		int Contador = 1;

		String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";BUG_REPORT_SEE;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnBorrarNoticia = "<button value=\"Spr\" width=35 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";BUG_REPORT_SUPRIMIR;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		boolean isMasterAccess = opera.isMaster(player);

		String[] ColoresFilas = { cbManager.getFontColor(cbManager.enumColor.Celeste.name()),cbManager.getFontColor(cbManager.enumColor.Gris.name()) };
		String Consulta = "select * from zeus_bug_report order by fechaIngreso DESC limit " + String.valueOf(Desde) + ", " + String.valueOf(MaximoLineas + 1);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(Contador <= MaximoLineas){
						String DescripTemporal = rss.getString(3).substring(0, ( rss.getString(3).length()>40 ? 40 : rss.getString(3).length() ) );
						Tabla += "<tr>" +
									"<td width=18 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+String.valueOf(Contador + Desde)+"</font></td>"+
									"<td width=170 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+rss.getString(4)+"</font></td>"+
									"<td width=110 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+rss.getString(2)+"</font></td>"+
									"<td width=130 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+rss.getString(5)+"</font></td>"+
									"<td width=262 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+DescripTemporal+"</font></td>"+
									"<td width=30 align=CENTER><font color="+ColoresFilas[Contador%2]+">"+btnVerNoticia.replace("%IDVER%", String.valueOf(rss.getString(1))) + ( isMasterAccess ? btnBorrarNoticia.replace("%IDVER%", String.valueOf(rss.getInt(1))) : "" ) +"</font></td>"+
								"</tr>";
					}else{
						haveNext = true;
					}
					Contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		Tabla += "</table><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Bug Report Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		Tabla += TablaControles;

		retorno += cbManager._formBodyComunidad(Tabla, cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()));

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}








	@SuppressWarnings("rawtypes")
	public static String getServerInformationRate(L2PcInstance player, int pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-info-server.htm");
		
		String DataFormat = opera.getGridFormatFromHtml(html, 1, "%DATA_INFO%");
		
		Map<String, String> propiedades = getAllProperties(player);
		String bypassNext = general.getCOMMUNITY_BOARD_PART_EXEC() + ";RATE;"+ String.valueOf(pagina+1) +";0;0;0";
		String bypassPrev = general.getCOMMUNITY_BOARD_PART_EXEC() + ";RATE;"+ String.valueOf(pagina-1) +";0;0;0";
		int propiedadesSorportadas = 19 * 3;
		int desde = pagina * propiedadesSorportadas;
		int hasta = desde + propiedadesSorportadas;
		boolean haveNext = false;
		

		
		

		String Tabla = "<tr>";
		Iterator itr = propiedades.entrySet().iterator();
		int Contador = 0;
		int Separador = 3;
		int ContadorPasadas = 0;
	    while(itr.hasNext() && !haveNext){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	if(ContadorPasadas>=desde){
		    	if((Contador%Separador) == 0){
		    		Contador = 0;
		    		Tabla+= "</tr><tr>";
		    	}
		    	Tabla += DataFormat.replace("%VALUE%", Entrada.getValue().toString()).replace("%PROPERTY%", Entrada.getKey().toString()); // "<td width=255 align=CENTER fixwidth=258><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">"+ Entrada.getKey().toString() +"</font> <font color="+ cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) +">"+ Entrada.getValue().toString() +"</font></td>";
		    	Contador++;
	    	}
	    	ContadorPasadas++;
	    	if(ContadorPasadas>=hasta){
	    		haveNext = true;
	    	}
	    }
	    
		html.replace("%PAG_PREV%", (pagina <=0 ? "" : bypassPrev));
		html.replace("%PAG_NEXT%", (haveNext ? bypassNext : ""));	    
	    html.replace("%DATA_INFO%", Tabla);
	    html.replace("%PAGE%", pagina + 1);
	    
		return html.getHtml();
	}

	//AQUI

	private static String getExplica(L2PcInstance player, String parte, int PaginaVer){
		int MaximoVer = 4;
		String Titulo = "";
		int Desde = PaginaVer*MaximoVer;
		int Hasta = Desde + MaximoVer;
		
		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";"+parte+"_MOD;0;%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";"+parte+"_DEL;0;%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnNew = "<button value=\"New\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";"+parte+"_NEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnSendLinkToAll = "<button value=\"Share to World\" width=90 height=24 action=\"bypass ZeuS annouceShow %IDMENSAJE%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		String grilla = "<table width=500><tr><td width=100 align=CENTER>"+ btnModificar +"</td><td width=100 align=CENTER>"+ btnEliminar +"</td><td width=100>"+ btnSendLinkToAll +"</td></tr></table>";

		String ParteBuscar = "";

		String TablaInformacion = "<table width=750 align=CENTER bgcolor="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) +"><tr><td width=20></td><td width=730 align=LEFT><font color="+cbManager.getFontColor(cbManager.enumColor.Verde.name())+">%TITULO%</font></td></tr></table>";
		TablaInformacion += "<table width=750 bgcolor="+cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name())+"><tr><td width=80></td><td width=670><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">%MENSAJE%</font></td></tr></table>";

		if(parte.equalsIgnoreCase("FEATURES")){
			ParteBuscar = "FEATURES";
			Titulo = "Features";
		}else if(parte.equalsIgnoreCase("EVENTS")){
			ParteBuscar = "EVENTS";
			Titulo = "Events";
		}else if(parte.equalsIgnoreCase("PLAYGAME")){
			ParteBuscar = "PLAYGAME";
			Titulo = "Playgame";
		}
		
		String retorno="";
		boolean haveNext = false;

		int Contador = 0;
		if(INFO_MSJE_COMMUNITY_BY_TYPE!=null){
			if(INFO_MSJE_COMMUNITY_BY_TYPE.containsKey(ParteBuscar)){
				for(_infoAnnou temp : INFO_MSJE_COMMUNITY_BY_TYPE.get(ParteBuscar)){
					if(Contador >= Desde && Contador < Hasta){
						String Grilla2 = grilla.replace("%IDMENSAJE%", String.valueOf(temp.getID()));
						retorno += TablaInformacion.replace("%TITULO%", temp.getTitle()).replace("%MENSAJE%", temp.getMessage() + ( player.isGM() ? Grilla2 + "<br>" : "" ) );			    		
			    	}else if(Contador >= Hasta){
			    		haveNext = true;
			    		break;
			    	}
			    	Contador++;					
				}
			}
		}		

		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";"+ParteBuscar+";" + String.valueOf(PaginaVer - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";"+ ParteBuscar +";" + String.valueOf(PaginaVer + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";		
		
		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
		"<tr>"+
			"<td width=80 align=CENTER>"+ (PaginaVer > 0 ? btnAtras : "") +"</td>"+
			"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">"+ Titulo +" Page "+ String.valueOf(PaginaVer + 1) +"</font></td>"+
			"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
		"</tr>"+
		"</table>";		
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-FeaturesEventsPlaygame.htm");
		
		html.replace("%NAME%", ParteBuscar);
		html.replace("%BTN_CREATE_NEW%", (player.isGM() ? btnNew : ""));
		html.replace("%DATA%", retorno + "<br>" + TablaControles);
		
		return html.getHtml();
	}

	private static String getMensajeParaLeer(L2PcInstance player, int IdMensaje, int IdPagina){

		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_MODIF;" + String.valueOf(IdPagina-1) + ";"+ String.valueOf(IdMensaje) +";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_DELETE;" + String.valueOf(IdPagina-1) + ";"+String.valueOf(IdMensaje)+";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSendLinkToAll = "<button value=\"Share to World\" width=90 height=24 action=\"bypass ZeuS annouceShow " + String.valueOf(IdMensaje) + "\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String strTableConfig = "<table width=270><tr><td width=90>"+btnModificar+"</td><td width=90>" + btnSendLinkToAll + "</td><td width=90>"+btnEliminar+"</td></tr></table>";

		String btnMainBack = "<button value=\"Back\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO;" + String.valueOf(IdPagina-1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno = "<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Annoucement Reading</font>" + btnMainBack + ( player.isGM() ? strTableConfig : "" ));

		String NombreGM ="NO INFO", Titulo="NO INFO", Mensaje="NO INFO", Fecha="NO INFO";

		if(INFO_MSJE_COMMUNITY != null){
			if(INFO_MSJE_COMMUNITY.containsKey(IdMensaje)){
				_infoAnnou DataMessage = INFO_MSJE_COMMUNITY.get(IdMensaje);
				NombreGM = DataMessage.getName();
				Titulo = DataMessage.getTitle();
				Mensaje = DataMessage.getMessage();
				Fecha = DataMessage.getDate();
			}
		}

		String Grilla = "<table width=700 align=CENTER>" +
						"<tr><td width=720 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">" + NombreGM + "</font></td></tr>" +
						"<tr><td width=720 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">" + Fecha + "</font></td></tr>" +
						"<tr><td width=720 align=CENTER fixwidth=700><font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">" + Titulo + "</font></td></tr>" +
						"<tr><td width=720 align=CENTER fixwidth=700></td></tr>" +
						"<tr><td width=720 align=CENTER fixwidth=700><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">" + Mensaje + "</font></td></tr>" +
						"</table>";

		retorno += cbManager._formBodyComunidad(Grilla);

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getAllRules(L2PcInstance player, int Pagina){

		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";RULES_MODIF;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";RULES_DELETE;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		String btnSendLinkToAll = "<button value=\"Share to World\" width=90 height=24 action=\"bypass ZeuS annouceShow %IDMENSAJE%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		String btnNewRules = "<button value=\"New\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";RULES_NEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		String strTableConfig = "<table width=270><tr><td width=90>"+btnModificar+"</td><td width=90>"+btnSendLinkToAll+"</td><td width=90>"+btnEliminar+"</td></tr></table>";

		String Retorno = "";
		int PorPagina = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";RULES;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";RULES;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String TablaSTR = "<table width=750 align=CENTER>";

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		boolean haveNext = false;
		int Contador = 0;
		int Desde = Pagina * PorPagina;
		int Hasta = Desde + PorPagina;
		
		if(INFO_MSJE_COMMUNITY_BY_TYPE!=null){
			if(INFO_MSJE_COMMUNITY_BY_TYPE.containsKey("RULES")){
				for(_infoAnnou temp : INFO_MSJE_COMMUNITY_BY_TYPE.get("RULES")){
					if(Contador >= Desde && Contador < Hasta ){
						String GrillaModif = strTableConfig.replace("%IDMENSAJE%", String.valueOf(temp.getID()));
						if(!player.isGM()){
							GrillaModif = "";
						}
						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + (Contador + 1 ) +"</font></td>" +
								"<td width=700 align=CENTER fixwidth=690><font color="+ ColoresGrilla[Contador%2] +">" + temp.getMessage() +"</font><br1>"+GrillaModif+"</td>" +
								"</tr>";						
					}else if(Contador > Hasta){
						haveNext = true;
						break;
					}
					Contador++;
				}
			}
		}
		

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Rules Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-rules.htm");

		html.replace("%BTN_CREATE_NEW%", (player.isGM() ? btnNewRules : ""));
		html.replace("%RULES%", TablaSTR);
		html.replace("%CONTROLS%", TablaControles);
		
		Retorno = html.getHtml();

		return Retorno;
	}
	
	private static Vector<_topplayer> getSortTop(){
		return general.GET_TOPS_PLAYERS();
	}

	@SuppressWarnings("unused")
	private static String getAllTopPlayer(L2PcInstance player, int pagina, String Parametros){
		Map<Integer, String> TipoBusquedas = new HashMap<Integer, String>();
		String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=16 action=\"%ACCION%\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter>";
		String ByPassPvP = general.getCOMMUNITY_BOARD_PART_EXEC() + ";TOPPLAYER;pvpAZ;0;0;0";
		String ByPassPK = general.getCOMMUNITY_BOARD_PART_EXEC() + ";TOPPLAYER;pkAZ;0;0;0";

		String btnPvP = btnMaestro.replace("%NOMBRE%", "PvP").replace("%TAM%", "58").replace("%ACCION%", ByPassPvP );
		String btnPk = btnMaestro.replace("%NOMBRE%", "Pk").replace("%TAM%", "58").replace("%ACCION%", ByPassPK );
		
		String retorno = "<center><table cellpadding=0 cellspacing=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Rank\" width=34 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Player Name\" width=188 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Lv\" width=22 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            btnPvP+"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            btnPk+"</td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Base Class\" width=138 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"Status\" width=52 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td><td>"+
            "<button value=\"\" width=62 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td></tr></table><br>";

		String Colores[] = {"232323","141313"};
		int contador = 1;
		int ContFila = 0;
		
		for(_topplayer TT : getSortTop()){
			if(contador >= general.COMMUNITY_BOARD_TOPPLAYER_LIST){
				break;
			}
			String TipoConecion = "";
			L2PcInstance playerBus = opera.getPlayerbyID(TT.getID());
			if(playerBus == null){
				TipoConecion = "<font color=676767>Offline</font>";
			}else if(playerBus.getClient().isDetached()){
				if(sellBuff.isBuffSeller(player)){
					TipoConecion = "<font color=676767>Off. Bf</font>";
				}else if(playerBus.isInCraftMode()){
					TipoConecion = "<font color=676767>Off. Cr</font>";
				}else if(playerBus.isInStoreMode()){
					TipoConecion = "<font color=676767>Off. St</font>";;
				}	
			}else if(playerBus.isOnline()){
				TipoConecion =  "<font color=5EFDA6>Online</font>";
			}
			String btnInfoChar = "<button value=\"Char Info\" width=60 height=26 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";TOPPLAYER_SHOW_PPL;"+ String.valueOf(pagina) +";"+ TT.getName() +";0;0\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator>";
			
			retorno += "<table cellpadding=0 cellspacing=0 border=0 bgcolor="+Colores[ContFila % 2]+"><tr>"+
			"<td fixwidth=38 align=CENTER>" + (ContFila==0 ? "<img src=\"L2UI_CT1.clan_DF_clanwaricon_bluecrownleader\" width=32 height=32>" : String.valueOf(ContFila + 1)) + "</td>"+
			"<td fixwidth=192 align=CENTER>" + (ContFila==0 ? "<font name=\"hs12\" color=54CFFF>"+ TT.getName() + "</font>" : "<font color=E9BF85>"+ TT.getName() + "</font>") + "</td>"+
			"<td fixwidth=26 align=CENTER>" + String.valueOf(TT.getLevel()) + "</td>"+
			"<td fixwidth=59 align=CENTER><font color=FD5EFB>"+ String.valueOf(TT.getPVP()) +"</font></td>"+
			"<td fixwidth=65 align=CENTER><font color=FE7A7A>"+ String.valueOf(TT.getPK())  +"</font></td>"+
			"<td fixwidth=140 align=CENTER><font color=E9BF85>"+ TT.getBaseClass() +"</font></td>"+
			"<td fixwidth=58 align=CENTER>"+ TipoConecion + "</td>"+
			"<td fixwidth=62>"+ btnInfoChar + "</td></tr></table>";
			ContFila++;
			contador++;
		}

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-topplayer.htm");
		html.replace("%DATA%", retorno);

		return html.getHtml();
	}

	private static Vector<String>getClanFromAlly(int idAlly){
		Vector<String> ClanesSave = new Vector<String>();
		String consulta = "select clan_data.clan_name, clan_data.clan_id, clan_data.clan_level from clan_data where clan_data.ally_id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idAlly);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					ClanesSave.add(rss.getString(1)+";"+String.valueOf(rss.getInt(2))+";"+String.valueOf(rss.getString(3)));
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return ClanesSave;
	}

	private static int getIdAllyFromClan(int clanId){
		int retorno = 0;
		String consulta = "select ally_id from clan_data where clan_data.clan_id = " + String.valueOf(clanId);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getInt(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static String getFortressFromClan(int idClan){
		String retorno = "None";
		String consulta = "select fort.name from fort where fort.owner = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getString(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static String getCastleFromClan(int idClan){
		String retorno = "None";
		String consulta = "SELECT castle.name FROM castle INNER JOIN clan_data ON clan_data.hasCastle = castle.id where clan_data.clan_id = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getString(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static int getCountAllPplFromClan(int idClan){
		int retorno = 0;
		String consulta = "SELECT count(*) FROM characters WHERE characters.clanid = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getInt(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static int getCountHeroesFromClan(int idClan){
		int retorno = 0;
		String consulta = "SELECT count(*) FROM characters WHERE characters.charId IN (SELECT heroes.charId FROM heroes WHERE playerd = 1 ) AND characters.clanid = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getInt(1);
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}


	private static String getAnnoucementByID(int idAnnou, boolean getMensaje){
		String retorno = "None";
		if(INFO_MSJE_COMMUNITY!=null){
			if(INFO_MSJE_COMMUNITY.containsKey(idAnnou)){
				_infoAnnou temp = INFO_MSJE_COMMUNITY.get(idAnnou);
				if(getMensaje){
					retorno = temp.getMessage();
				}else{
					retorno = temp.getTitle();
				}
			}
		}
		
		return retorno;
	}

	private static void setDeleteAnnoucement(int idMensaje){
		String consulta = "delete from zeus_annoucement where id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idMensaje);
			psqry.executeUpdate();
			psqry.close();
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}
		loadAllMsjeCommunity();
	}

	private static String getClanHallFromClan(int idClan){
		String retorno = "None";
		String consulta = "select chanhall.name, clanhall.location from chanhall where chanhall.ownerId = " + String.valueOf(idClan);
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					retorno = rss.getString(1) + "("+ rss.getString(2) +")";
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}

	private static Vector<String> getAllClanWar(int idClan){
		Vector<String> retorno = new Vector<String>();
		Vector<Integer> ClanesAgregados = new Vector<Integer>();
		ClanesAgregados.add(-1);
		String consulta = "SELECT clan_wars.clan1,"+
							"clan_wars.clan2,"+
							"(SELECT CONCAT(clan_data.clan_name, \" (Lv\", clan_data.clan_level ,\")\") FROM clan_data WHERE clan_data.clan_id = clan_wars.clan1),"+
							"(SELECT CONCAT(clan_data.clan_name, \" (Lv\", clan_data.clan_level ,\")\") FROM clan_data WHERE clan_data.clan_id = clan_wars.clan2),"+
							"(SELECT CONCAT(clan_data.clan_name,\";\",clan_data.clan_level) FROM clan_data WHERE clan_data.clan_id = clan_wars.clan1),"+
							"(SELECT CONCAT(clan_data.clan_name,\";\",clan_data.clan_level) FROM clan_data WHERE clan_data.clan_id = clan_wars.clan2)"+
							" FROM clan_wars WHERE (clan_wars.wantspeace1 = 0 AND clan_wars.wantspeace2 = 0) AND clan_wars.clan1 = "+ String.valueOf(idClan) +" OR clan_wars.clan2 = "+String.valueOf(idClan);

		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					if(!ClanesAgregados.contains(rss.getInt(1)) && !ClanesAgregados.contains(rss.getInt(2))){
						if(rss.getInt(1)!=idClan){
							retorno.add(String.valueOf(rss.getInt(1))+";"+rss.getString(3)+";"+rss.getString(5));
							ClanesAgregados.add(rss.getInt(1));
						}else{
							retorno.add(String.valueOf(rss.getInt(2))+";"+rss.getString(4)+";"+rss.getString(6));
							ClanesAgregados.add(rss.getInt(2));
						}
					}
				}catch(Exception a){
				}
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}

		return retorno;
	}


	private static String getInfoClan(L2PcInstance player, int Pagina, String Parametros, int IdClan, String NombreClan, int lvClan){

		String isVipClan = opera.isClanPremium_BD(String.valueOf(IdClan)) ? "<br1><font color="+ cbManager.getFontColor(cbManager.enumColor.Azul.name()) +">--VIP Clan--</font>" : "";

		String btnRetroceder = "<button value=\"Back\" width=120 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN;"+ String.valueOf(Pagina + 1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">"+NombreClan+"("+ String.valueOf(lvClan) +") Clan Information</font>" + isVipClan + btnRetroceder);
		String btnNombreClan = "<button value=\"%NOMBRE%\" width=150 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_SEE;" + String.valueOf(Pagina) + ";%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnGetAllPlayers = "<button value=\"Gets all Player\" width=155 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLANALLPLAYER;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ String.valueOf(lvClan) +";"+ String.valueOf(IdClan) +"\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		//CLANALLPLAYER

		int idAlly = getIdAllyFromClan(IdClan);

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Friends Alliance</font>",false);

		if(idAlly>0){
			Vector<String> ClanesFromAlly = getClanFromAlly(idAlly);
			if((ClanesFromAlly !=null) && (ClanesFromAlly.size()>1)){
				String grillaTemporal = "<table with=650 align=center>";
				int contadorFilas = 0;
				for(String clann : ClanesFromAlly){
					String[] infoClan = clann.split(";");
					if(contadorFilas==0){
						grillaTemporal += "<tr>";
					}
					if(IdClan != Integer.valueOf(infoClan[1])){
						grillaTemporal += "<td width=216 align=CENTER>"+ btnNombreClan.replace("%NOMBRE%", infoClan[0]).replace("%IDCLAN%", infoClan[1]).replace("%LVLCLAN%", infoClan[2]) +"</td>";
					}
					contadorFilas++;
					if(contadorFilas == 3){
						contadorFilas = 0;
						grillaTemporal += "</tr>";
					}
				}
				if(contadorFilas!=0){
					for(int i=contadorFilas;i<=3;i++){
						grillaTemporal += "<td></td>";
					}
					grillaTemporal += "</tr>";
				}
				grillaTemporal += "</table>";
				retorno += cbManager._formBodyComunidad(grillaTemporal,cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name()));
			}else{
				retorno += cbManager._formBodyComunidad("Without Friends Alliance",cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name()));
			}
		}else{
			retorno += cbManager._formBodyComunidad("Without Friends Alliance",cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name()));
		}

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Residences</font>",false);

		String grillaResidence = "<table width=750 align=center><tr>" +
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Clan Hall:</font> "+ getClanHallFromClan(IdClan) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Castle:</font> "+ getCastleFromClan(IdClan) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Fortress:</font> "+ getFortressFromClan(IdClan) +"</td>"+
								"</tr></table>";

		retorno += cbManager._formBodyComunidad(grillaResidence,cbManager.getFontColor(enumColor.COLOR_GRILLA_AZUL.name()));

		int allMembers = getCountAllPplFromClan(IdClan), OnLineMembers = 0, HeroesMember = getCountHeroesFromClan(IdClan);

		for(L2PcInstance ppl : opera.getAllPlayerOnWorld()){
			if(ppl.getClan()!=null){
				if(ppl.getClan().getId() == IdClan){
					OnLineMembers++;
				}
			}
		}

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Members</font>",false);

		grillaResidence = "<table width=750 align=center><tr>" +
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">All Members:</font> "+ String.valueOf(allMembers) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Online Members:</font> "+ String.valueOf(OnLineMembers) +"</td>"+
								"<td align=center width=250><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Heroes Members:</font> "+ String.valueOf(HeroesMember) +"</td></tr>"+
								"<tr><td width=250></td><td width=250 align=CENTER>"+btnGetAllPlayers+"</td><td width=250></td></tr>"+
								"</table>";

		retorno += cbManager._formBodyComunidad(grillaResidence,cbManager.getFontColor(enumColor.COLOR_GRILLA_VERDE.name()));

		Vector<String> ClanesWar = getAllClanWar(IdClan);

		retorno += cbManager._formTituloComunidad("<font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Clan Wars</font>",false);

		if(ClanesWar != null){
			if(ClanesWar.size()>0){
				String btnNombreClan2 = "<button value=\"%TITULO%\" width=150 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_SEE;" + String.valueOf(Pagina) + ";%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
				String grillaTemporal = "<table with=650 align=center>";
				int contadorFilas = 0;
				for(String clanInfo:ClanesWar){
					String[] infoClan = clanInfo.split(";");
					if(contadorFilas==0){
						grillaTemporal += "<tr>";
					}
					//IDCLAN;TITULO_BTN;CLAN_NAME;CLAN_LEVEL
					grillaTemporal += "<td width=216 align=CENTER>"+ btnNombreClan2.replace("%TITULO%", infoClan[1]).replace("%NOMBRE%", infoClan[2]).replace("%IDCLAN%", infoClan[0]).replace("%LVLCLAN%", infoClan[3]) +"</td>";
					contadorFilas++;
					if(contadorFilas == 3){
						contadorFilas = 0;
						grillaTemporal += "</tr>";
					}
				}
				if(contadorFilas!=0){
					for(int i=contadorFilas;i<=3;i++){
						grillaTemporal += "<td></td>";
					}
					grillaTemporal += "</tr>";
				}
				grillaTemporal += "</table>";
				retorno += cbManager._formBodyComunidad(grillaTemporal,cbManager.getFontColor(enumColor.COLOR_GRILLA_ROJO.name()));
			}else{
				retorno += cbManager._formBodyComunidad("In peace",cbManager.getFontColor(enumColor.COLOR_GRILLA_ROJO.name()));
			}
		}else{
			retorno += cbManager._formBodyComunidad("In peace",cbManager.getFontColor(enumColor.COLOR_GRILLA_ROJO.name()));
		}

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getAllClan(L2PcInstance player, int Pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Clans</font>" + btnMain);
		String btnNombreClan = "<button value=\"%NOMBRE%\" width=150 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_SEE;" + String.valueOf(Pagina - 1) + ";%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int PorPagina = general.COMMUNITY_BOARD_CLAN_LIST;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
						"<tr>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Lv</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Name</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Alliance</font></td>" +
						"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Leader</font></td>" +
						"<td width=80 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Members</font></td>" +
						"<td width=30 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Heroes</font></td>" +
						"</tr></table>";
		TablaSTR += "<table width=750 align=CENTER>";

		String strMySql = "SELECT clan_data.clan_id, clan_data.clan_level, clan_data.clan_name, IFNULL(clan_data.ally_name,\"\"), clan_data.leader_id, clan_data.reputation_score,"+
				"(SELECT count(*) FROM characters WHERE characters.clanid = clan_data.clan_id),"+
				"(SELECT characters.char_name FROM characters WHERE characters.charId = clan_data.leader_id),"+
				"(SELECT count(*) FROM characters WHERE characters.charId IN (SELECT heroes.charId FROM heroes) AND characters.clanid = clan_data.clan_id) "+
				"FROM clan_data ORDER BY clan_data.clan_level DESC LIMIT " + String.valueOf(Pagina * PorPagina) + "," + String.valueOf(((Pagina+1) * PorPagina) + 1);

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean haveNext = false;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;

			while (rss.next() && !haveNext){
				try{
					if(contador <= PorPagina){
						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(2)) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + btnNombreClan.replace("%NOMBRE%", rss.getString(3)).replace("%IDCLAN%", String.valueOf(rss.getInt(1))).replace("%LVLCLAN%", String.valueOf(rss.getInt(2))) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(4) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(8) +"</font></td>" +
								"<td width=80 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(7)) +"</font></td>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(9)) +"</font></td>" +
								"</tr>";
					}else{
						haveNext = true;
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Clan Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(TablaSTR + TablaControles);

		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getAllTopPlayerHeroes(L2PcInstance player, int pagina, String Parametros){

		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";HEROES;" + String.valueOf(pagina - 1) + ";"+Parametros+";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";HEROES;" + String.valueOf(pagina + 1) + ";"+Parametros+";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		int MaximoVer = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		String desde = String.valueOf(pagina * general.COMMUNITY_BOARD_ROWS_FOR_PAGE);

		String retorno = "<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Heroes</font>" + btnMain);
		Map<Integer, String> TipoBusquedas = new HashMap<Integer, String>();
		TipoBusquedas.put(0, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.onlinetime DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(1, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.onlinetime ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Menor a Mayor
		TipoBusquedas.put(2, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pkkills DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(3, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pkkills ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Menor a Mayor
		TipoBusquedas.put(4, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pvpkills DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(5, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.pvpkills ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Menor a Mayor
		TipoBusquedas.put(6, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.char_name DESC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor
		TipoBusquedas.put(7, " WHERE characters.accesslevel = 0 AND characters.charId IN (SELECT heroes.charId FROM heroes WHERE heroes.played = 1) ORDER BY characters.char_name ASC LIMIT "+ desde + ", " + String.valueOf(MaximoVer + 1));//Mayor a Menor

		String Consulta = "SELECT characters.char_name," +
				"characters.level," +
				"characters.base_class," +
				"characters.pvpkills," +
				"characters.pkkills,"+
				"IFNULL((SELECT CONCAT(clan_data.clan_name, \"(Lv \", clan_data.clan_level ,\")\") FROM clan_data WHERE clan_data.clan_id = characters.clanid),\"\"),"+
				"characters.online,"+
				"characters.onlinetime,"+
				"(SELECT heroes.count FROM heroes WHERE heroes.charId = characters.charId),"+
				"characters.classid"+
				" FROM characters " + TipoBusquedas.get(Integer.valueOf(Parametros));

		String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";HEROES;0;%ACCION%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnPvP = btnMaestro.replace("%NOMBRE%", "PvP").replace("%TAM%", "70").replace("%ACCION%", ( Integer.valueOf(Parametros)==4 ? "5" : "4"));
		String btnPk = btnMaestro.replace("%NOMBRE%", "Pk").replace("%TAM%", "70").replace("%ACCION%", ( Integer.valueOf(Parametros)==2 ? "3" : "2"));
		String btnOnline = btnMaestro.replace("%NOMBRE%", "Online").replace("%TAM%", "110").replace("%ACCION%", (Integer.valueOf(Parametros)==0 ? "1" : "0"));
		String btnNombre = btnMaestro.replace("%NOMBRE%", "Name").replace("%TAM%", "170").replace("%ACCION%", (Integer.valueOf(Parametros)==6 ? "7" : "6"));



		String grillaMostrar = "<table width=705 align=CENTER border=0><tr>"+
								"<td align=center width=170>"+btnNombre+"</td>"+
								"<td align=center width=25>"+btnMaestro.replace("%NOMBRE%", "Lv").replace("%TAM%", "25").replace("%ACCION%", "0")+"</td>"+
								"<td align=center width=70>"+btnPvP+"</td>"+
								"<td align=center width=70>"+btnPk+"</td>"+
								"<td align=center width=160>"+btnMaestro.replace("%NOMBRE%", "Clan").replace("%TAM%", "160").replace("%ACCION%", "0")+"</td>"+
								"<td align=center width=110>"+btnOnline+"</td>"+
								"<td align=center width=100>"+btnMaestro.replace("%NOMBRE%", "Status").replace("%TAM%", "100").replace("%ACCION%", "0")+"</td></tr>";

		Connection conn = null;

		boolean haveNext = false;

		int contador = 1;

		try{
			conn  = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			String []ColoresGrilla = new String[2];
			ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
			ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());

			String TipoConecion = "";

			while (rss.next()){
				try{
					L2PcInstance playerBus = null;
					try{
						playerBus = opera.getPlayerbyName(rss.getString(1));
						if(playerBus == null){
							TipoConecion = "OFFLINE";
						}else{
							if(playerBus.getClient().isDetached()){
								if(playerBus.isInCraftMode()){
									TipoConecion = "OFF. CRAFT";
								}else if(playerBus.isInStoreMode()){
									TipoConecion = "OFF. STORE";
								}
							}else{
								TipoConecion = "ONLINE";
							}
						}
					}catch(Exception a){
						TipoConecion = "OFFLINE";
					}

					if(contador <= general.COMMUNITY_BOARD_ROWS_FOR_PAGE){

						//";HEROES;0;%ACCION%;0;0\"

						String NombreCharConLink = "<a action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";HEROES_SHOW_PPL;"+ String.valueOf(pagina) +";" + Parametros + ";" + rss.getString(1) +";0\"><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(1) + " ("+ String.valueOf(rss.getInt(9)) +")</font></a>";

						grillaMostrar += "<tr>" +
								"<td width=170 align=CENTER>"+ NombreCharConLink +"<br><font color="+ ColoresGrilla[contador%2] +">" + opera.getClassName(rss.getInt(3))  +"</font><br></td>" +
								"<td width=25 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(2)) +"</font><br></td>" +
								"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(4)) +"</font><br></td>" +
								"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(5)) +"</font><br></td>" +
								"<td width=160 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(6) +"</font><br></td>" +
								"<td width=110 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getTiempoON(rss.getInt(8)) +"</font><br></td>" +
								"<td width=100 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + TipoConecion +"</font><br></td>" +
								"</tr>";
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		grillaMostrar += "</table>";

		if(contador>general.COMMUNITY_BOARD_ROWS_FOR_PAGE){
			haveNext = true;
		}


		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Heroes Page "+ String.valueOf(pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";



		retorno += cbManager._formBodyComunidad(grillaMostrar + TablaControles);
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	private static String getAllChangeLog(L2PcInstance player, int Pagina){
		String btnNewChangeLog = "<button value=\"New\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHANGELOGNEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnModificar = "<button value=\"Change\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHANGELOG_MODIF;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnEliminar = "<button value=\"Delete\" width=90 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHANGELOG_DELETE;" + String.valueOf(Pagina-1) + ";%IDMENSAJE%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSendLinkToAll = "<button value=\"Share to World\" width=90 height=24 action=\"bypass ZeuS annouceShow %IDMENSAJE%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		

		String strTableConfig = "<table width=270><tr><td width=90>"+btnModificar+"</td><td width=90>"+btnSendLinkToAll+"</td><td width=90>"+btnEliminar+"</td></tr></table>";

		int PorPagina = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		int Desde = Pagina * PorPagina;
		int Hasta = Desde + PorPagina;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHANGELOG;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHANGELOG;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String TablaSTR = "<table width=750 align=CENTER>";

		//String []ColoresGrilla = {cbManager.getFontColor(cbManager.enumColor.Gris.name()), cbManager.getFontColor(cbManager.enumColor.Verde.name()) };
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		boolean haveNext = false;
		int Contador=0;
		
		if(INFO_MSJE_COMMUNITY_BY_TYPE!=null){
			if(INFO_MSJE_COMMUNITY_BY_TYPE.containsKey("CHANGELOG")){
				for(_infoAnnou temp : INFO_MSJE_COMMUNITY_BY_TYPE.get("CHANGELOG")){
					if(Contador >= Desde && Contador < Hasta){
						String GrillaModifMensaje = "<br1>" + strTableConfig.replace("%IDMENSAJE%", String.valueOf(temp.getID()));
						if(!player.isGM()){
							GrillaModifMensaje = "";
						}

						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + (Contador + 1)  +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + temp.getDate() +"</font></td>" +
								"<td width=585 align=CENTER fixwidth=580><font color="+ ColoresGrilla[Contador%2] +">" + temp.getMessage() +"</font>"+GrillaModifMensaje+"</td>" +
								"</tr>";						
					}else if(Contador > Hasta){
						haveNext = true;
						break;
					}
					Contador++;
				}
			}
		}

		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table><br><br>";

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change Log Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-changelog.htm");
		
		html.replace("%BTN_CREATE_NEW%", (player.isGM() ? btnNewChangeLog : ""));
		html.replace("%CHANGE_LOG%", TablaSTR);
		html.replace("%CONTROLS%", TablaControles);
		return html.getHtml();
	}

	private static String getCraftStoresOnLine(L2PcInstance player, int pagina, String parametros, boolean isForSearch){


		int desde = pagina * general.COMMUNITY_BOARD_MERCHANT_LIST;
		int Hasta = (pagina * general.COMMUNITY_BOARD_MERCHANT_LIST) + general.COMMUNITY_BOARD_MERCHANT_LIST;

		String btnBackToSearch = "";
		if(isForSearch){
			desde = Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST;
			Hasta = (Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST) + general.COMMUNITY_BOARD_MERCHANT_LIST;
			btnBackToSearch = "<button value=\"Back\" width=100 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH_1;" + parametros.split(";")[1] + ";"+ textToSearch.get(player.getObjectId()) +";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		}

		String btnSearch = "<button value=\"Search\" width=100 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH;" + String.valueOf(pagina) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnAtras = "";
		String btnSiguente = "";


		/*		case "STORE_SEARCH_IN_PS":
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm3),parm2+";"+parm1,true);
		*/

		if(!isForSearch){
			btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CRAFTERPRIVATE;" + String.valueOf(pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
			btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CRAFTERPRIVATE;" + String.valueOf(pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		}else{
			//Integer.valueOf(parm3) parm2+";"+parm1
			btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH_IN_PS;" + parametros.split(";")[1] + ";"+parametros.split(";")[0]+";"+ String.valueOf(pagina - 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
			btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH_IN_PS;" + parametros.split(";")[1] + ";"+parametros.split(";")[0]+";"+ String.valueOf(pagina + 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		}

		String btnNameChar = "<button value=\"%NAME%\" width=133 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CRAFTERPRIVATE_GO;" + String.valueOf(pagina) + ";%NAME%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores</font>" + btnMain + btnSearch + btnBackToSearch);
		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
				"<tr>" +
				"<td width=60 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Type</font></td>" +
				"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Name</font></td>" +
				"<td width=555 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">List</font></td>" +
				"</tr></table>";
		String[] Colores = {cbManager.getFontColor(enumColor.COLOR_GRILLA_AZUL.name()), cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name())};
		int Contador = 0;
		int ContadorPagina = 1;

		int contadorBusqueda = 1;

		boolean HaveNext = false;
		boolean CreateTableItem = !isForSearch ? true : false;
		boolean continuarPorJunta = isForSearch;
		for(L2PcInstance cha: opera.getAllPlayerOnWorld()){
			if(cha.isInStoreMode()){
				if((ContadorPagina > desde) || continuarPorJunta){
					if((ContadorPagina <= Hasta) || continuarPorJunta){
										boolean isOnline = true;
										if(cha.getClient().isDetached()){
											isOnline = false;
										}
										TablaSTR = "<table width=750 align=CENTER>";
										Contador++;
										TradeItem[] itemSell = cha.getSellList().getItems();
										TradeItem[] itemBuy = cha.getBuyList().getItems();
										TradeItem[] itemSelected = null;
										String tipoVS = "";
										boolean isPackageSell = false;
										String Productos = "<table width=555>";

										boolean isSellSelected = true;

										if(general.isSellMerchant.containsKey(cha.getObjectId())){
											if(!general.isSellMerchant.get(cha.getObjectId())){
												isSellSelected = false;
											}
										}

										if(isSellSelected){
											if(itemSell !=null ){
												if(itemSell.length>0){
													itemSelected = itemSell;
													if(!cha.getSellList().isPackaged()){
														tipoVS = "Sell";
													}else{
														tipoVS = "P. Sell";
														isPackageSell = true;
													}
												}
											}
										}else{
											if(itemBuy != null){
												if(itemBuy.length>0){
													itemSelected = itemBuy;
													tipoVS = "Buy";
												}
											}
										}

										if(itemSelected==null){
											if(itemSell==null){
												if(itemBuy.length>0){
													itemSelected = itemBuy;
													isPackageSell = false;
													tipoVS = "Buy";
												}
											}else{
												if(itemSell.length==0){
													tipoVS = "Buy";
													itemSelected = itemBuy;
												}else{
													if(!cha.getSellList().isPackaged()){
														tipoVS = "Sell";
														isPackageSell = false;
													}else{
														tipoVS = "P. Sell";
														isPackageSell = true;
													}
													itemSelected = itemSell;
												}
											}
										}

										Long total = 0L;

										if(isForSearch){
											//CreateTableItem
										}
										if(isForSearch){
											CreateTableItem = false;
										}
										for(TradeItem item : itemSelected){
											boolean isEnchantable = (item.getItem().isEnchantable()==1 ? true : false);
											int EnchatItem = item.getEnchant();
											if(!isEnchantable){
												EnchatItem = 0;
											}
											if(isForSearch){
												if(item.getItem().getId()== Integer.valueOf(parametros.split(";")[0])){
													CreateTableItem = true;
												}
											}

											String itemPrice = opera.getFormatNumbers(item.getPrice());
											Productos += "<tr><td width=32>" + opera.getIconImgFromItem(item.getItem().getId()) + "</td>"+
														"<td width=500 align=LEFT>"+item.getItem().getName() + ( EnchatItem>0 ? " +" + String.valueOf(EnchatItem) : "" ) + " (Amount " + String.valueOf(item.getCount()) + (!isPackageSell ? " / $" + itemPrice : "") +")</td></tr>";
											total += item.getPrice();
										}


										Productos += "</table>";
										String strOnline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">Online</font>";
										String strOffline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Rojo.name()) + ">Offline</font>";
										String seccionNombre = "";
										if(general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT){
											seccionNombre = btnNameChar.replace("%NAME%", cha.getName()) +"<br1>"+ ( isOnline ? strOnline : strOffline );
										}else{
											seccionNombre = "<font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">"+cha.getName()+"</font>" +"<br1>" + ( isOnline ? strOnline : strOffline );
										}
										TablaSTR += "<tr>" +
												"<td width=60 align=CENTER>"+ tipoVS +"</td>" +
												"<td width=135 align=CENTER>"+ seccionNombre + "</td>" +
												"<td width=555 align=CENTER>"+ Productos +"</td>" +
												"</tr>";
										if(isPackageSell){
											TablaSTR += "<tr><td width=60></td><td width=135></td><td width=555 align=CENTER>TOTAL: "+ opera.getFormatNumbers(total) + "</td></tr>";
										}
										TablaSTR += "</table><br>";
										if(CreateTableItem){
											retorno += cbManager._formBodyComunidad(TablaSTR,Colores[Contador%2]);
											contadorBusqueda++;
											if(contadorBusqueda>=general.COMMUNITY_BOARD_MERCHANT_LIST){
												continuarPorJunta=false;
											}
										}
					}else if(ContadorPagina>Hasta){
						HaveNext = true;
					}
				}
				if(CreateTableItem){
					ContadorPagina++;
				}
			}
		}


		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores Page "+ String.valueOf(pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( HaveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(TablaControles);

		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}



	private static String getCraftStoresOnLineSearch(L2PcInstance player, int pagina, String parametros, boolean buscar){


		int desde = Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST;
		int Hasta = (Integer.valueOf(parametros.split(";")[1]) * general.COMMUNITY_BOARD_MERCHANT_LIST) + general.COMMUNITY_BOARD_MERCHANT_LIST;
		String btnBackToSearch = "<button value=\"Back\" width=100 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH_1;" + parametros.split(";")[1] + ";"+ textToSearch.get(player.getObjectId()) +";0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSearch = "<button value=\"Search\" width=100 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH;" + String.valueOf(pagina) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnAtras = "";
		String btnSiguente = "";

		btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH_IN_PS;" + String.valueOf(pagina) + ";"+parametros.split(";")[0]+";"+ String.valueOf(Integer.valueOf(parametros.split(";")[1]) - 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";STORE_SEARCH_IN_PS;" + String.valueOf(pagina) + ";"+parametros.split(";")[0]+";"+ String.valueOf(Integer.valueOf(parametros.split(";")[1]) + 1) +";0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnNameChar = "<button value=\"%NAME%\" width=133 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CRAFTERPRIVATE_GO;" + String.valueOf(pagina) + ";%NAME%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores</font>" + btnMain + btnSearch + btnBackToSearch);
		String TablaSTR = "<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) + ">"+
				"<tr>" +
				"<td width=60 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Type</font></td>" +
				"<td width=135 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">Name</font></td>" +
				"<td width=555 align=CENTER><font color="+ cbManager.getFontColor(cbManager.enumColor.Celeste.name()) +">List</font></td>" +
				"</tr></table>";
		String[] Colores = {cbManager.getFontColor(enumColor.COLOR_GRILLA_AZUL.name()), cbManager.getFontColor(enumColor.COLOR_GRILLA_NARANJO.name())};
		int Contador = 0;
		int ContadorPagina = 1;
		boolean HaveNext = false;
		boolean CreateTableItem = false;
		boolean seguirBuscando = true;
		for(L2PcInstance cha: opera.getAllPlayerOnWorld()){
			if(cha.isInStoreMode()){
				if(seguirBuscando){
						boolean isOnline = true;
						if(cha.getClient().isDetached()){
							isOnline = false;
						}
						TablaSTR = "<table width=750 align=CENTER>";
						TradeItem[] itemSell = cha.getSellList().getItems();
						TradeItem[] itemBuy = cha.getBuyList().getItems();
						TradeItem[] itemSelected = null;
						String tipoVS = "";
						boolean isPackageSell = false;
						String Productos = "<table width=555>";

						boolean isSellSelected = true;

						if(general.isSellMerchant.containsKey(cha.getObjectId())){
							if(!general.isSellMerchant.get(cha.getObjectId())){
								isSellSelected = false;
							}
						}

						if(isSellSelected){
							if(itemSell !=null ){
								if(itemSell.length>0){
									itemSelected = itemSell;
									if(!cha.getSellList().isPackaged()){
										tipoVS = "Sell";
									}else{
										tipoVS = "P. Sell";
										isPackageSell = true;
									}
								}
							}
						}else{
							if(itemBuy != null){
								if(itemBuy.length>0){
									itemSelected = itemBuy;
									tipoVS = "Buy";
								}
							}
						}

						if(itemSelected==null){
							if(itemSell==null){
								if(itemBuy.length>0){
									itemSelected = itemBuy;
									isPackageSell = false;
									tipoVS = "Buy";
								}
							}else{
								if(itemSell.length==0){
									tipoVS = "Buy";
									itemSelected = itemBuy;
								}else{
									if(!cha.getSellList().isPackaged()){
										tipoVS = "Sell";
										isPackageSell = false;
									}else{
										tipoVS = "P. Sell";
										isPackageSell = true;
									}
									itemSelected = itemSell;
								}
							}
						}

						Long total = 0L;
						CreateTableItem = false;
						for(TradeItem item : itemSelected){
							boolean isEnchantable = (item.getItem().isEnchantable()==1 ? true : false);
							int EnchatItem = item.getEnchant();
							if(!isEnchantable){
								EnchatItem = 0;
							}
							if(item.getItem().getId()== Integer.valueOf(parametros.split(";")[0])){
								CreateTableItem = true;
							}
							String itemPrice = opera.getFormatNumbers(item.getPrice());
							Productos += "<tr><td width=32>" + opera.getIconImgFromItem(item.getItem().getId()) + "</td>"+
										"<td width=500 align=LEFT>"+item.getItem().getName() + ( EnchatItem>0 ? " +" + String.valueOf(EnchatItem) : "" ) + " (Amount " + String.valueOf(item.getCount()) + (!isPackageSell ? " / $" + itemPrice : "") +")</td></tr>";
							total += item.getPrice();
						}


						Productos += "</table>";
						String strOnline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Verde.name()) + ">Online</font>";
						String strOffline = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Rojo.name()) + ">Offline</font>";
						String seccionNombre = "";
						if(general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT){
							seccionNombre = btnNameChar.replace("%NAME%", cha.getName()) +"<br1>"+ ( isOnline ? strOnline : strOffline );
						}else{
							seccionNombre = "<font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">"+cha.getName()+"</font>" +"<br1>" + ( isOnline ? strOnline : strOffline );
						}
						TablaSTR += "<tr>" +
								"<td width=60 align=CENTER>"+ tipoVS +"</td>" +
								"<td width=135 align=CENTER>"+ seccionNombre + "</td>" +
								"<td width=555 align=CENTER>"+ Productos +"</td>" +
								"</tr>";
						if(isPackageSell){
							TablaSTR += "<tr><td width=60></td><td width=135></td><td width=555 align=CENTER>TOTAL: "+ opera.getFormatNumbers(total) + "</td></tr>";
						}
						TablaSTR += "</table><br>";
						if(CreateTableItem){
							if((ContadorPagina>desde) && (ContadorPagina<=Hasta)){
								retorno += cbManager._formBodyComunidad(TablaSTR,Colores[Contador%2]);
								Contador++;
							}else if(ContadorPagina>Hasta){
								seguirBuscando = false;
								HaveNext=true;
							}
							ContadorPagina++;
						}
				}
			}
		}


		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Integer.valueOf(parametros.split(";")[1]) > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Private Stores Page "+ String.valueOf((Integer.valueOf(parametros.split(";")[1])) + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( HaveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";

		retorno += cbManager._formBodyComunidad(TablaControles);

		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String getCastillos(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Castles</font>" + btnMain);

		String Consulta = "SELECT "+
						"castle.`name`,"+
						"castle.siegeDate,"+
						"castle.regTimeOver,"+
						"castle.regTimeEnd,"+
						"castle.taxPercent,"+
						"IFNULL((SELECT clan_data.clan_name FROM clan_data WHERE clan_data.hasCastle = castle.id),\"Empty\"),"+
						"IFNULL((SELECT clan_data.clan_level FROM clan_data WHERE clan_data.hasCastle = castle.id),0),"+
						"IFNULL((SELECT clan_data.ally_name FROM clan_data WHERE clan_data.hasCastle = castle.id),\"None\"),"+
						"IFNULL((SELECT clan_data.clan_id FROM clan_data WHERE clan_data.hasCastle = castle.id),0) "+
						"FROM "+
						"castle";

		String btnNombreClan = "<button value=\"%TITULO%\" width=135 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLAN_SEE;-99;%IDCLAN%;%NOMBRE%;%LVLCLAN%\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String grillaMostrar = "<table width=727 align=CENTER border=0>" +
								"<tr>"+
								"<td align=center width=70><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Castle</font></td>"+
								"<td align=center width=135><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Clan</font></td>"+
								"<td align=center width=135><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Ally Name</font></td>"+
								"<td align=center width=100><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Siege Date</font></td>"+
								"<td align=center width=70><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Reg. Period</font></td>"+
								"<td align=center width=100><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Reg. Time Over</font></td>"+
								"<td align=center width=50><font color="+ cbManager.getFontColor(cbManager.enumColor.Gris.name()) +">Tax</font></td>"+
								"</tr>";


		Connection conn = null;
		try{
			conn  = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;

			String []ColoresGrilla = new String[2];
			ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
			ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
			while (rss.next()){
				try{
					int idClan = rss.getInt(9);
					String btnClan = "None";
					if(idClan>0){
						btnClan = btnNombreClan.replace("%TITULO%", rss.getString(6) + "(lv"+ String.valueOf(rss.getInt(7)) +")" ).replace("%NOMBRE%", rss.getString(1)).replace("%IDCLAN%", String.valueOf(rss.getInt(9))).replace("%LVLCLAN%", String.valueOf(rss.getInt(7)));
					}
					grillaMostrar += "<tr>" +
							"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(1) +"</font></td>" +
							"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + btnClan  +"</font></td>" +
							"<td width=135 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (idClan>0 ? rss.getString(8) : "None") +"</font></td>" +
							"<td width=100 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getDateFromUnixTime(rss.getLong(2)) +"</font></td>" +
							"<td width=70 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (rss.getString(3).equals("false") ? "Over" : "Started") +"</font></td>" +
							"<td width=100 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getDateFromUnixTime(rss.getLong(4)) +"</font></td>" +
							"<td width=50 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + String.valueOf(rss.getInt(5)) +"</font></td>" +
							"</tr>";
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}

		grillaMostrar += "</table>";

		retorno += cbManager._formBodyComunidad(grillaMostrar);
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;

	}

	@SuppressWarnings("unused")
	private static String getTitulosMensaje(L2PcInstance player, int Pagina){
		String Retorno = "";
		int PorPagina = general.COMMUNITY_BOARD_ROWS_FOR_PAGE;
		int Desde = Pagina * PorPagina;
		int Hasta = Desde + PorPagina;
		String btnAtras = "<button value=\"<- Prev.\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO;" + String.valueOf(Pagina - 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnSiguente = "<button value=\"Next ->\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO;" + String.valueOf(Pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		//String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		String btnVerNoticia = "<button width=32 height=32 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_CT1.MiniMap_DF_PlusBtn_Blue_Down fore=L2UI_CT1.MiniMap_DF_PlusBtn_Blue>";
		
		String TablaSTR = "<table width=750 align=CENTER>";
		String []ColoresGrilla = new String[2];
		ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
		ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
		boolean haveNext = false;
		int Contador = 0;
		if(INFO_MSJE_COMMUNITY_BY_TYPE!=null){
			if(INFO_MSJE_COMMUNITY_BY_TYPE.containsKey("ANNOUCEMENT")){
				for(_infoAnnou temp : INFO_MSJE_COMMUNITY_BY_TYPE.get("ANNOUCEMENT")){
					if(Contador >= Desde && Contador <= Hasta){
						TablaSTR += "<tr>" +
								"<td width=30 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + (Contador + 1 ) +"</font></td>" +
								"<td width=135 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + temp.getName() +"</font></td>" +
								"<td width=128 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + temp.getTitle().substring(0, ( temp.getTitle().length()>35 ? 35 : temp.getTitle().length() ) ) +"</font></td>" +
								"<td width=395 align=CENTER><font color="+ ColoresGrilla[Contador%2] +">" + temp.getMessage().substring(0, ( temp.getMessage().length()>50 ? 50 : temp.getMessage().length() ) ) +"</font></td>" +
								"<td width=36 align=LEFT>"+ btnVerNoticia.replace("%IDVER%", String.valueOf(temp.getID())) +"</td>" +
								"</tr>";						
					}else if(Contador > Hasta){
						haveNext = true;
						break;
					}
				}
			}
		}
		if(!TablaSTR.endsWith("</tr>")){
			TablaSTR += "</tr>";
		}

		TablaSTR += "</table>";
		
	

		String TablaControles ="<table width=750 align=CENTER gbcolor=" + cbManager.getFontColor(cbManager.enumColor.COLOR_CABECERA.name()) +">"+
				"<tr>"+
					"<td width=80 align=CENTER>"+ (Pagina > 0 ? btnAtras : "") +"</td>"+
					"<td width=590 align=CENTER><font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Annoucement Page "+ String.valueOf(Pagina + 1) +"</font></td>"+
					"<td width=80 align=CENTER>"+ ( haveNext ? btnSiguente : "" ) +"</td>"+
				"</tr>"+
				"</table>";
		
		String btnNuevo = "<button value=\"New\" width=60 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNONEW;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-annoucement.htm");
		html.replace("%ANNOUCEMENTS%", TablaSTR);
		html.replace("%CONTROLS%", TablaControles);
		html.replace("%BTN_CREATE_NEW%", (player.isGM() ? btnNuevo : ""));
		return html.getHtml();
	}

	public static String getRules(L2PcInstance player, int pagina, String Parametros){
		String retorno = getAllRules(player, pagina);
		return retorno;
	}

	public static String getChangeLog(L2PcInstance player, int pagina, String Parametros){
		String retorno =getAllChangeLog(player,pagina);
		return retorno;
	}

	public static String setPageNewMensajesChangeLog(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Change Log</font>");
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_CHANGELOG Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Change:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setPageNewRules(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Rules</font>");
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_RULES Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Rules:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setModifNewRules(L2PcInstance player, int pagina, String Parametros){
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";RULES;" + String.valueOf(pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Update Rules</font>"+btnBack);
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_RULES_MOD Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Rules:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setModifMensajesChangeLog(L2PcInstance player, int pagina, String Parametros){
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHANGELOG;" + String.valueOf(pagina + 1) + ";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Update Change Log</font>"+btnBack);
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_CHANGELOG_MOD Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Change:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String setModifMensajesVarios(L2PcInstance player, int pagina, String Parametros, String Lugar){
		//String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";"+Lugar+";0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change "+ Lugar +"</font>"+btnBack);
		String Titulo = "<edit var=\"txtTitle\" width=150>";;
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_"+Lugar+"_MOD Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:(%TITULO%)</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	public static String setPageModifMensajesAnnou(L2PcInstance player, int pagina, String Parametros){
		//String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_VER;" + String.valueOf(Pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_VER;" + String.valueOf(pagina + 1) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change Annoucement</font>"+btnBack);
		String Titulo = "<edit var=\"txtTitle\" width=150>";;
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_ANNOUCEMENT_MOD Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:(%TITULO%)</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	//AQUI
	public static String setPageNewVariosMensajes(L2PcInstance player, int pagina, String Parametros, String Seccion){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">"+Seccion+"</font>");
		String Titulo = "<edit var=\"txtTitle\" width=150>";;
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_"+Seccion+" Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Data:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	public static String setPageNewMensajesAnnou(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Annoucement</font>");
		String Titulo = "<edit var=\"txtTitle\" width=150>";
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_ANNOUCEMENT Set _ txtTitle Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaTitulo = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Title:</font></td></tr><tr><td width=650 align=LEFT>"+ Titulo +"</td></tr></table>";
		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad("<center>" + grillaTitulo + "<br1>" + grillaCaja + "<br1>" + grillaBtn + "</center>");
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	public static String getPageStaff(L2PcInstance player, int pagina, String Parametros){
		String grillaStaff = "<table width=460 align=CENTER>";
		String Consulta = "SELECT characters.charId, characters.char_name, characters.`level`, characters.classid, characters.base_class, characters.`online` FROM characters WHERE characters.accesslevel > 0";

		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
			int contador = 1;
			String []ColoresGrilla = new String[2];
			ColoresGrilla[0] = cbManager.getFontColor(cbManager.enumColor.Gris.name());
			ColoresGrilla[1] = cbManager.getFontColor(cbManager.enumColor.Verde.name());
			boolean showGM = true;
			while (rss.next()){
				if(rss.getInt(6)==1){
					L2PcInstance playerByName = opera.getPlayerbyName(rss.getString(2));
					if(playerByName!=null){
						showGM = opera.isGmAllVisible(playerByName, player);
					}else{
						showGM = false;
					}
				}else{
					showGM = false;
				}

				try{
					grillaStaff += "<tr>" +
							"<td width=15 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + contador +"</font></td>" +
							"<td width=180 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + rss.getString(2) +"</font></td>" +
							"<td width=180 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + opera.getClassName(rss.getInt(5)) +"</font></td>" +
							"<td width=90 align=CENTER><font color="+ ColoresGrilla[contador%2] +">" + (showGM ? "ONLINE" : "OFFLINE")  + "</font></td>" +
							"</tr>";
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}


		grillaStaff += "</table>";

		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-staff.htm");
		html.replace("%STAFF%", grillaStaff);
		
		return html.getHtml();
	}


	private static boolean canUseTeleporPrivateStore(L2PcInstance privateStore, L2PcInstance player){

		if(player.getKarma()>0){
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK, player);
			return false;
		}
		if(player.isDead()){
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_DEATH, player);
			return false;
		}
		L2PcFishing fish = player.getFishingEx();
		if(player.isInCombat() || fish.isFishing() || player.isFlying() || player.isInOlympiadMode() || ((player.getPvpFlag()>0)
				|| player.isInDuel() || player.isJailed() || player.isInCraftMode() || player.isInStoreMode() || player.isIn7sDungeon()
				|| player.isInStance() || player.isOnEvent() || player.isInsideZone(ZoneIdType.SIEGE) )){
			central.msgbox(language.getInstance().getMsg(player).YOU_CANT_USE_MY_SERVICES_RIGHT_NOW, player);
			return false;
		}

		if(privateStore.isInsideZone(ZoneIdType.NO_SUMMON_FRIEND)){
			central.msgbox(privateStore.getName()+ " " + language.getInstance().getMsg(player).ARE_INSIDE_A_NON_SUMMON_ARE, player);
			return false;
		}

		if(privateStore.isInsideZone(ZoneIdType.SIEGE) && !general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_SIEGE){
			central.msgbox( privateStore.getName()+ " " + language.getInstance().getMsg(player).ARE_INSIDE_A_SIEGE_ZONE , player);
			return false;
		}

		if(privateStore.isInsideZone(ZoneIdType.CASTLE) && !general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CASTLE){
			if(player.getClan()==null){
				central.msgbox( privateStore.getName()+ " " + language.getInstance().getMsg(player).ARE_INSIDE_A_CASTLE , player);
				return false;
			}
			if(player.getClan().getCastleId()<=0){
				central.msgbox( privateStore.getName()+ " " + language.getInstance().getMsg(player).ARE_INSIDE_A_CASTLE , player);
				return false;
			}
			if(privateStore.getClan()!=null){
				if(player.getClan().getCastleId() != privateStore.getClan().getCastleId()){
					central.msgbox( privateStore.getName()+ " " + language.getInstance().getMsg(player).ARE_INSIDE_A_CASTLE , player);
					return false;
				}
			}else{
				central.msgbox( privateStore.getName()+ " " + language.getInstance().getMsg(player).ARE_INSIDE_A_CASTLE , player);
				return false;
			}
		}

		if(privateStore.isInsideZone(ZoneIdType.CLAN_HALL) && !general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_INSIDE_CLANHALL_WITH_ANOTHER_CLAN){
			if((privateStore.getClan()!=null) && (player.getClan() !=null)){
				if(privateStore.getClan()!= player.getClan()){
					central.msgbox( privateStore.getName()  + " " + language.getInstance().getMsg(player).ARE_INSIDE_A_CH , player);
					return false;
				}
			}else{
				central.msgbox( privateStore.getName()  + " " + language.getInstance().getMsg(player).ARE_INSIDE_A_CH , player);
				return false;
			}
		}

		if(!privateStore.isInsideZone(ZoneIdType.PEACE) && general.COMMUNITY_BOARD_PRIVATE_STORE_TELEPORT_ONLY_PEACEZONE){
			central.msgbox(privateStore.getName()  + " " + language.getInstance().getMsg(player).ARE_NOT_IN_PEACE_ZONE , player);
			return false;
		}

		return true;
	}




	public static String getPageMensajes(L2PcInstance player, int pagina, String Parametros){
		String retorno = getTitulosMensaje(player,pagina);
		return retorno;
	}
	
	private static String getInfoAccess(String NombreSeccion){
		for(String t : Engine.getBtnOption()){
			if(t.indexOf(NombreSeccion)>0){
				return t;
			}
		}
		return "";
	}

	private static NpcHtmlMessage getFastMenu(L2PcInstance player, NpcHtmlMessage html){

		
		
		return html;
	}
	
	private static NpcHtmlMessage getLastAnnoucement(NpcHtmlMessage html){
		
		if(LastAnnoucement==null){
			html.replace("%LAST_MESSAGE%", "---");
			html.replace("%LAST_MESSAGE_DATE%", "---");
			html.replace("%LAST_MESSAGE_WRITER_NAME%", "---");
		}else{
			if(LastAnnoucement.getMessage().length()==0){
				html.replace("%LAST_MESSAGE%", "---");
				html.replace("%LAST_MESSAGE_DATE%", "---");
				html.replace("%LAST_MESSAGE_WRITER_NAME%", "---");
			}else{
				int LargoPermitido = 350;
				String Mensaje = LastAnnoucement.getMessage();
				if(Mensaje.length()==0){
					html.replace("%LAST_MESSAGE%", "---");
					html.replace("%LAST_MESSAGE_DATE%", "---");
					html.replace("%LAST_MESSAGE_WRITER_NAME%", "---");
				}else{
					if(LastAnnoucement.getMessage().length()>LargoPermitido){
						Mensaje = Mensaje.substring(0,LargoPermitido-31);
					}
					
					String ByPass = "bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";ANNO_VER;1;"+ String.valueOf(LastAnnoucement.getID()) +";0;0";
					String showmoreLink = "<a action=\""+ ByPass +"\"><font color=\"377AFF\">...Show More</font></a>";		
	
					Mensaje += "<br>" + showmoreLink;
					
					html.replace("%LAST_MESSAGE%", Mensaje);
					html.replace("%LAST_MESSAGE_DATE%", LastAnnoucement.getDate());
					html.replace("%LAST_MESSAGE_WRITER_NAME%", LastAnnoucement.getName());
				}
			}
		}
		return html;
	}
	
	public static String delegar(L2PcInstance player, String params){
		String Retorno = "";

		if(params == null){
			return getComunidad(player, "", 0);
		}

		if(params.contains(";")){
			if(general.DEBUG_CONSOLA_ENTRADAS){
				_log.warning("Community-> " + params);
			}
			if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
				central.msgbox(params, player);
			}
			String[] Eventos = params.split(";");
			String Event = Eventos[1];
			String parm1 = Eventos[2];
			String parm2 = Eventos[3];
			String parm3 = Eventos[4];
			String parm4 = Eventos[5];
			switch (Event){
				case "CHANG":
					opera.setPlayerConfig(parm1, player);
					Retorno = getComunidad(player, "", 0); 
					break;
				case "ANNO":
					Retorno = getPageMensajes(player,Integer.valueOf(parm1),"");
					break;
				case "ANNONEW":
					Retorno = setPageNewMensajesAnnou(player, 0 , "");
					break;
				case "ANNO_VER":
					Retorno = getMensajeParaLeer(player, Integer.valueOf(parm2),Integer.valueOf(parm1));
					break;
				case "ANNO_MODIF":
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String AnnModif = setPageModifMensajesAnnou(player, Integer.valueOf(parm1),"");
					String Annuncio = getAnnoucementByID(Integer.valueOf(parm2),true);
					String Titulo = getAnnoucementByID(Integer.valueOf(parm2),false);
					AnnModif = AnnModif.replace("%TITULO%", Titulo).replace("%IDVER%", parm2);
					cbManager.send1001(AnnModif, player);
					cbManager.send1002(player, Annuncio, " ", "0");
					Retorno = "";
					break;
				case "ANNO_DELETE":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getPageMensajes(player,0,"");
					break;
				case "CHANGELOG":
					Retorno = getChangeLog(player, Integer.valueOf(parm1), "");
					break;
				case "CHANGELOG_MODIF":
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String ChangeLogMensaje = getAnnoucementByID(Integer.valueOf(parm2),true);
					String ChangeLogModif = setModifMensajesChangeLog(player, Integer.valueOf(parm1),"");
					cbManager.send1001(ChangeLogModif, player);
					cbManager.send1002(player, ChangeLogMensaje, " ", "0");
					break;
				case "CHANGELOG_DELETE":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getChangeLog(player, 0, "");
					break;
				case "CHANGELOGNEW":
					Retorno = setPageNewMensajesChangeLog(player,Integer.valueOf(parm1),"");
					break;
				case "RULES":
					Retorno = getRules(player,Integer.valueOf(parm1),"");
					break;
				case "RULES_NEW":
					Retorno = setPageNewRules(player, Integer.valueOf(parm1), "");
					break;
				case "RULES_MODIF":
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String RulesMensaje = getAnnoucementByID(Integer.valueOf(parm2),true);
					String RulesModif = setModifNewRules(player, Integer.valueOf(parm1),"");
					cbManager.send1001(RulesModif, player);
					cbManager.send1002(player, RulesMensaje, " ", "0");
					break;
				case "RULES_DELETE":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getRules(player,0,"");
					break;
				case "STAFF":
					Retorno = getPageStaff(player,Integer.valueOf(parm1),"");
					break;
				case "TOPPLAYER_SHOW_PPL":
					opera.getInfoPlayer(parm2, player);
					break;
				case "TOPPLAYER":
					Retorno = getAllTopPlayer(player,0,parm1);
					break;
				case "HEROES_SHOW_PPL":
					opera.getInfoPlayer(parm3, player);
				case "HEROES":
					Retorno = getAllTopPlayerHeroes(player,Integer.valueOf(parm1),parm2);
					break;
				case "CLAN":
					PaginaVerAllPlayer.put(player, 0);
					if(Integer.valueOf(parm1)>=0){
						Retorno = getAllClan(player,Integer.valueOf(parm1),"");
					}else{
						Retorno = getCastillos(player,Integer.valueOf(parm1),"");
					}
					break;
				case "CLAN_SEE":
					PaginaVerAllPlayer.put(player, 0);
					Retorno = getInfoClan(player, Integer.valueOf(parm1), "", Integer.valueOf(parm2), parm3, Integer.valueOf(parm4));
					break;
				case "CLAN_ALL_MORE":
					PaginaVerAllPlayer.put(player, PaginaVerAllPlayer.get(player) + 1);
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm4), parm2, parm3);
					break;
				case "CLAN_ALL_MENUS":
					PaginaVerAllPlayer.put(player, PaginaVerAllPlayer.get(player) - 1);
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm4), parm2, parm3);
					break;
				case "CLANALLPLAYER":
					//action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CLANALLPLAYER;"+ String.valueOf(Pagina) +";"+ NombreClan +";"+ String.valueOf(lvClan) +";"+ String.valueOf(IdClan) +"\"
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm4), parm2, parm3);
					break;
				case "CHARINFO":
					opera.getInfoPlayer(parm2, player);
					//<a action=\"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";CHARINFO;"+ String.valueOf(Pagina) +";"+ player.getName() +";"+String.valueOf(idClan)+";"+ NombreClan + "-" + lvlClan +"\">"
					//getAllPlayerFromClan(player, Pagina, idClan, NombreClan, lvlClan)
					Retorno = getAllPlayerFromClan(player, Integer.valueOf(parm1), Integer.valueOf(parm3), parm4.split("-")[0], parm4.split("-")[1]);
					break;
				case "CASTLE":
					Retorno = getCastillos(player,Integer.valueOf(parm1),"");
					break;
				case "CRAFTERPRIVATE":
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm1),"",false);
					break;
				case "CRAFTERPRIVATE_GO":
					L2PcInstance playerToGo = opera.getPlayerbyName(parm2);
					if(playerToGo!=null){
						if(playerToGo != player){
							if(canUseTeleporPrivateStore(playerToGo,player)){
								Location gotoDir = playerToGo.getLocation();
								player.teleToLocation(gotoDir, true);
							}
						}
					}
					Retorno = getCraftStoresOnLine(player,Integer.valueOf(parm1),"",false);
					break;
				case "RATE":
					Retorno = getServerInformationRate(player, Integer.valueOf(parm1));
					break;
				case "BAIL":
					jailBail.showMain(player);				
					break;
				case "BSSS":
					sellBuff.setBuffForSearchPagine(player, 0, false);
					sellBuff.searchBuffSystem(player);
					break;
				case "FEATURES_NEW":
				case "EVENTS_NEW":
				case "PLAYGAME_NEW":
					Retorno = setPageNewVariosMensajes( player,0,"", Event.replace("_NEW", "") );
					break;
				case "FEATURES":
				case "EVENTS":
				case "PLAYGAME":
					Retorno = getExplica(player,Event,Integer.valueOf(parm1));
					break;
				case "FEATURES_MOD":
				case "EVENTS_MOD":
				case "PLAYGAME_MOD":
					String MensajesVarios = setModifMensajesVarios(player,0,"",Event.replace("_MOD", ""));
					LastIdModifMensaje.put(player, Integer.valueOf(parm2));
					String strMensaje = getAnnoucementByID(Integer.valueOf(parm2),true);
					String strTitulo = getAnnoucementByID(Integer.valueOf(parm2),false);
					String strAnnModif = MensajesVarios.replace("%TITULO%", strTitulo).replace("%IDVER%", parm2);
					cbManager.send1001(strAnnModif, player);
					cbManager.send1002(player, strMensaje, " ", "0");
					Retorno = "";
					break;
				case "FEATURES_DEL":
				case "EVENTS_DEL":
				case "PLAYGAME_DEL":
					setDeleteAnnoucement(Integer.valueOf(parm2));
					Retorno = getExplica(player, Event.replace("_DEL", ""),0);
				case "STORE_SEARCH_IN_PS":
					Retorno = getCraftStoresOnLineSearch(player,Integer.valueOf(parm1),parm2+";"+parm3,true);
					break;
				case "BUG_REPORT_SEE":
					getBugReportWindows(player,Integer.valueOf(parm2));
				case "BUG_REPORT":
					Retorno = getBugReport(player,Integer.valueOf(parm1));
					break;
				case "BUG_REPORT_SUPRIMIR":
					setDeleteReportWindows(player, Integer.valueOf(parm2));
					Retorno = getBugReport(player,Integer.valueOf(parm1));
					break;
			}
		}else{
			Retorno = Comunidad.getComunidad(player, params, 0);
		}
		return Retorno;
	}
	
	public static NpcHtmlMessage getMemoryStatusServer(L2PcInstance cha, NpcHtmlMessage html){
		double max = Runtime.getRuntime().maxMemory() / 1048576; // maxMemory is the upper
		double allocated = Runtime.getRuntime().totalMemory() / 1048576; // totalMemory the
		double cached = Runtime.getRuntime().freeMemory() / 1048576; // freeMemory the
		double used = allocated - cached; // really used memory
		double useable = max - used; // allocated, but non-used and non-allocated memory
		
		DecimalFormat df = new DecimalFormat(" (0.0000'%')");
		DecimalFormat df2 = new DecimalFormat(" # 'MB'");
		
		html.replace("%TOTAL_SERVER_RAM%", String.valueOf(df2.format(max)));
		html.replace("%USED_SERVER_MEMORY_RAM%", String.valueOf(df2.format(allocated) + df.format((allocated / max) * 100) ));
		html.replace("%FREE_SERVER_MEMORY_RAM%", String.valueOf(df2.format(useable) + df.format((useable / max) * 100)));

		
		return html;
	}

	public static String getComunidad_original(L2PcInstance player, String accion, int pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_PART_EXEC());
		html.replace("%BYPASS_2%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html = getCharMenu(player, html);
		html = getLastAnnoucement(html);
		html = getOnlineStatusBar(html);
		html = getFastMenu(player, html);
		html = getMemoryStatusServer(player, html);
		html = getBugReport(player, html);
		String btnDealyLoginQuest = opera.getGridFormatFromHtml(html, 7, "%DEALY_QUEST%");
		btnDealyLoginQuest = btnDealyLoginQuest.replace("%CYCLE%", _dealy_reward_system.getCycle(player)); 
		html.replace("%DEALY_QUEST%", _dealy_reward_system.isInDealyLoginQuest(player) ? btnDealyLoginQuest : "" );
		return html.getHtml();
	}	
	
	public static String getComunidad(L2PcInstance player, String accion, int pagina){
		if(!opera.canUseCBFunction(player, false)) {
			return "";
		}
				
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_PART_EXEC());
		html.replace("%BYPASS_2%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html = getCharMenu(player, html);
		html = getLastAnnoucement(html);
		html = getOnlineStatusBar(html);
		
		html = getMemoryStatusServer(player, html);
		html = getBugReport(player, html);
		String btnDealyLoginQuest = opera.getGridFormatFromHtml(html, 7, "%DEALY_QUEST%");
		btnDealyLoginQuest = btnDealyLoginQuest.replace("%CYCLE%", _dealy_reward_system.getCycle(player)); 
		html.replace("%DEALY_QUEST%", _dealy_reward_system.isInDealyLoginQuest(player) ? btnDealyLoginQuest : "" );
		return html.getHtml();
	}
	
	
	

	private static NpcHtmlMessage getBugReport(L2PcInstance player, NpcHtmlMessage html){
		String NoReadingBug = "0";
		String gridFormat = opera.getGridFormatFromHtml(html, 6, "%BUG_REPORT_GRID%");
		if(player.isGM()){
			NoReadingBug = String.valueOf( cbManager.getNoReadBugReport());
			html.replace("%BUG_REPORT_GRID%", gridFormat.replace("%BYPASS_BUG_REPORT%", "bypass " + general.getCOMMUNITY_BOARD_PART_EXEC() + ";BUG_REPORT;0;0;0;0").replace("%NO_READING_COUNT%", opera.getFormatNumbers(NoReadingBug)));
		}else{
			html.replace("%BUG_REPORT_GRID%", "");
		}
		
		return html;
	}
	
	private static NpcHtmlMessage getOnlineStatusBar(NpcHtmlMessage html){
		html.replace("%ONLINE_PLAYERS_COUNT%", opera.getFormatNumbers(opera.getOnlinePlayer()));
		html.replace("%OFFLINE_TRADERS%", opera.getFormatNumbers(opera.getOffliners()));
		html.replace("%LAST_RESTART%", general.getBeginDate());
		html.replace("%SERVER_TIME%", opera.getHoraActual());
		return html;
	}
	
	private static NpcHtmlMessage getCharMenu(L2PcInstance player, NpcHtmlMessage html){
	
		boolean isExpSpBlock = general.getCharConfigEXPSP(player);
		boolean isTradeRefusal = !general.getCharConfigTRADE(player);
		boolean isBadBuffProtec = general.getCharConfigBADBUFF(player);
		boolean isHideStore = general.getCharConfigHIDESTORE(player);
		boolean isPublicStat = general.getCharConfigSHOWSTAT(player);
		boolean isRefusal = general.getCharConfigREFUSAL(player);
		boolean isPartyMatching = general.getCharConfigPartyMatching(player);
		boolean isOlyScheme = general.getCharConfigOlyScheme(player);
		boolean isReadOlyWinner = general.getCharConfigReadOlyWinner(player);
		
		boolean seeOtherDressme = dressme.getInstance().getDressme(player.getObjectId()).canSeeOthersDressme();
		
		String Activo = opera.getGridFormatFromHtml(html, 1, "");
		String Desactivado = opera.getGridFormatFromHtml(html, 2, "");
		String OlyBuffMenu = opera.getGridFormatFromHtml(html, 3, "%OLY_BUFF_GRID%");

		html.replace("%name%", player.getName());
        html.replace("%class%", ClassNamesHolder.getClassName(player.getActiveClass()));
        html.replace("%level%", String.valueOf(player.getLevel()));
        html.replace("%clan%", String.valueOf(player.getClan() != null ? player.getClan().getName() : "No"));
        html.replace("%ally%", String.valueOf(player.getClan() != null && player.getClan().getAllyId() > 0 ? player.getClan().getAllyName() : "No"));
        html.replace("%noble%", String.valueOf(player.isNoble() ? "Yes" : "No"));
        html.replace("%online_time%", Tools.convertMinuteToString(player.getCurrentOnlineTime()));
//        html.replace("%ip%", player.getIPAddress());
        html.replace("%premium%", ZeuS.isPremium(player)? "Yes" : "No");

		
		html.replace("%expsp_STATUS%", (!isExpSpBlock ? Activo : Desactivado));
		html.replace("%tradec_STATUS%", (!isTradeRefusal ? Activo : Desactivado));
		html.replace("%noBuff_STATUS%", (isBadBuffProtec ? Activo : Desactivado));
		html.replace("%hideStore_STATUS%", (isHideStore ? Activo : Desactivado));
		html.replace("%showmystat_STATUS%", (isPublicStat ? Activo : Desactivado));
		html.replace("%Refusal_STATUS%", (isRefusal ? Activo : Desactivado));
		html.replace("%partymatching_STATUS%", (isPartyMatching ? Activo : Desactivado));
		
		html.replace("%DRESSME_SEE_OTHERS%", "ZeuS see_others_dressme " + ( seeOtherDressme ? "0" : "1" ) );
		html.replace("%seeOtherDressme_STATUS%", (seeOtherDressme ? Activo : Desactivado));
		
		if(!player.isNoble()){
			html.replace("%OLY_BUFF_GRID%", "");
		}else{
			html.replace("%OLY_BUFF_GRID%", OlyBuffMenu.replace("%olyBuffScheme_STATUS%", (isOlyScheme ? Activo : Desactivado)));
		}
		html.replace("%readolywinner_STATUS%", (isReadOlyWinner ? Activo : Desactivado));
		
		String EmailAccount = opera.getUserMail(player.getAccountName());

		String EmailRegistrationGrid = opera.getGridFormatFromHtml(html, 4, "%REGISTRATION_GRID%");
		String EmailRegisterGrid = opera.getGridFormatFromHtml(html, 5, "%REGISTER_EMAIL%");
		
		if(EmailAccount==null){
			html.replace("%REGISTRATION_GRID%", EmailRegistrationGrid);
			html.replace("%REGISTER_EMAIL%", "");
		}else if(EmailAccount.length()<=0){
			html.replace("%REGISTRATION_GRID%", EmailRegistrationGrid);
			html.replace("%REGISTER_EMAIL%", "");
		}else{
			html.replace("%REGISTRATION_GRID%", "");
			html.replace("%REGISTER_EMAIL%", EmailRegisterGrid.replace("%ACCOUNT_EMAIL%", EmailAccount));
		}
		return html;
	}
	public static void loadAllMsjeCommunity(){
		try{
			INFO_MSJE_COMMUNITY.clear();
		}catch(Exception a){
			
		}
		try{
			INFO_MSJE_COMMUNITY_BY_TYPE.clear();
		}catch(Exception a){
			
		}
		
		try{
			LastAnnoucement = null;
		}catch(Exception a){
			
		}
		
		String strMySql = "SELECT * FROM zeus_annoucement ORDER BY zeus_annoucement.fecha DESC";
		Connection conn = null;
		PreparedStatement psqry = null;
		int contador = 0;		
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				try{
					int idMens = rss.getInt(1);
					String strTitulo = rss.getString(2);
					String strMensaje = rss.getString(3);
					String strOrigen = rss.getString(4);
					String strFecha = String.valueOf(rss.getDate(5));
					String strTipo = rss.getString(6);
					_infoAnnou temp = new _infoAnnou(idMens, strTitulo, strMensaje, strOrigen, strFecha, strTipo);
					INFO_MSJE_COMMUNITY.put(idMens, temp);
					if(INFO_MSJE_COMMUNITY_BY_TYPE == null){
						INFO_MSJE_COMMUNITY_BY_TYPE.put(strTipo, new Vector<_infoAnnou>());
					}else if(!INFO_MSJE_COMMUNITY_BY_TYPE.containsKey(strTipo)){
						INFO_MSJE_COMMUNITY_BY_TYPE.put(strTipo, new Vector<_infoAnnou>());
					}
					INFO_MSJE_COMMUNITY_BY_TYPE.get(strTipo).add(temp);
					if(strTipo.equalsIgnoreCase("ANNOUCEMENT") && LastAnnoucement == null){
						LastAnnoucement = temp;
					}
					contador++;
				}catch(SQLException e){
					_log.warning("ZeuS cb Error -> " + e.getMessage());
				}
			}
		}catch (SQLException e){
			_log.warning("ZeuS cb Error -> " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}		
		_log.warning("	Community Message loaded " + contador + " Messages.");
	}
	
	public static void showCommunityMessageWindow(L2PcInstance player, int idMnsj){
		if(INFO_MSJE_COMMUNITY == null){
			central.msgbox("NOTHING TO SHOW", player);
			return;
		}else if(INFO_MSJE_COMMUNITY.size()==0){
			central.msgbox("NOTHING TO SHOW", player);
			return;
		}else if(!INFO_MSJE_COMMUNITY.containsKey(idMnsj)){
			central.msgbox("NOTHING TO SHOW", player);
			return;			
		}
		_infoAnnou Temp = INFO_MSJE_COMMUNITY.get(idMnsj);
		if(Temp == null){
			central.msgbox("NOTHING TO SHOW", player);
			return;
		}
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityAnnoucement.html"); 
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%TITLE%", Temp.getTitle());
		html.replace("%DATE%", Temp.getDate());
		html.replace("%NAME%", Temp.getName());
		html.replace("%MESSAGE%", Temp.getMessage());
		central.sendHtml(player, html.getHtml());
	}
	
}
class _infoAnnou{
	private int ID;
	private String TITLE;
	private String MSJE;
	private String NAME;
	private String DATE;
	private String TYPE;
	public _infoAnnou(int _id, String _title, String _msje, String _name, String _date, String _type){
		this.ID = _id;
		this.TITLE = _title;
		this.MSJE = _msje;
		this.NAME = _name;
		this.DATE = _date;
		this.TYPE = _type;
	}
	public final int getID(){
		return this.ID;
	}
	public final String getTitle(){
		return this.TITLE;
	}
	public final String getName(){
		return this.NAME;
	}
	public final String getMessage(){
		return this.MSJE;
	}
	public final String getDate(){
		return this.DATE;
	}
	public final String getType(){
		return this.TYPE;
	}
}