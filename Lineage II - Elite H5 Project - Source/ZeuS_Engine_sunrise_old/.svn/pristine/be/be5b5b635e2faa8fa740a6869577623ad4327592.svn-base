package ZeuS.Comunidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.sql.ClanTable;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class Clan {
	private static Logger _log = Logger.getLogger(Clan.class.getName());
	public static Map<Integer, Integer> IdPostModif = new HashMap<Integer, Integer>();
	public static Map<Integer, String> MensajeClan = new HashMap<Integer, String>();
	public static Map<Integer, Integer> paginaPreview = new HashMap<Integer, Integer>();

	private static String getPostByID(int idPost){
		String retorno = "None";
		String consulta = "select memo from zeus_cb_clan_foro where id = " + String.valueOf(idPost);
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

	private static String setModifClanMessage(L2PcInstance player, int pagina){
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";SHOW_POST;"+ String.valueOf(pagina) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Clan Annoucement</font>"+btnBack);
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_CLAN_MESSAGE_MOD Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPreview = "<button value=\"Preview\" width=80 height=24 action=\"Write Z_CLAN_NOTICE_PREVIEW Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Clan Annoucement:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=325 align=CENTER>" + btnAgregar + "</td><td width=325 align=LEFT>"+ btnPreview +"</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}

	private static String setModifPost(L2PcInstance player, int pagina){
		String btnBack = "<button value=\"Back\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";SHOW_POST;"+ String.valueOf(pagina) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">Change Post</font>"+btnBack);
		String btnAgregar = "<button value=\"Update\" width=80 height=24 action=\"Write Z_CLAN_POST_MOD Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Post:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	public static String setPageNewPost(L2PcInstance player, int pagina, String Parametros){
		String retorno ="<html><body><center>" + cbManager._formTituloComunidad("<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name()) + ">New Clan Post</font>");
		String btnAgregar = "<button value=\"Save\" width=60 height=24 action=\"Write Z_NEW_CLAN_POST Set _ Content Content Content\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String Caja = "<MultiEdit var =\"Content\" width=610 height=100>";

		String grillaCaja = "<table width=650><tr><td width=650 align=LEFT><font color="+ cbManager.getFontColor(cbManager.enumColor.Verde.name()) +">Post:</font></td></tr><tr><td width=650 align=LEFT>"+ Caja +"</td></tr></table>";
		String grillaBtn = "<table width=650><tr><td width=650 align=CENTER>" + btnAgregar + "</td></tr></table>";

		retorno += cbManager._formBodyComunidad(grillaCaja + "<br1>" + grillaBtn);
		retorno += "</center>"+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}


	private static void setDeletePost(L2PcInstance player, int idPost){

		if(player.getClan()==null){
			central.msgbox(language.getInstance().getMsg(player).YOU_CAN_NOT_REMOVE_THIS_POST, player);
			return;
		}

		String consulta = "delete from zeus_cb_clan_foro where id=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(consulta);
			psqry.setInt(1, idPost);
			psqry.executeUpdate();
			psqry.close();
			central.msgbox(language.getInstance().getMsg(player).POST_REMOVED, player);
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}
	}



	private static void getMensaje(L2PcInstance player, int idMensajeVer){
		String Consulta = "SELECT zeus_cb_clan_foro.id," +
							"(IFNULL((SELECT characters.char_name FROM characters WHERE characters.charId = zeus_cb_clan_foro.idChar),\"NO EXISTS\")),"+
							"zeus_cb_clan_foro.memo,"+
							"zeus_cb_clan_foro.createdate "+
							"FROM zeus_cb_clan_foro WHERE zeus_cb_clan_foro.id = ?";
		Connection conn = null;
		PreparedStatement psqry = null;

		String FechaIngreso = "", NombreChar ="", Memo = "";
		boolean haveInfo = false;

		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			psqry.setInt(1, idMensajeVer);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				FechaIngreso = rss.getString(4);
				NombreChar = rss.getString(2);
				Memo = rss.getString(3);
				haveInfo = true;
			}
		}catch(Exception e){

		}
		try{
			conn.close();
		}catch(Exception a){

		}
		if(!haveInfo){
			return;
		}

		String colorVioBrill = cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name());
		String colorVioOscu = cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name());
		String colorTexto = cbManager.getFontColor(cbManager.enumColor.Blanco.name());
		String colorNombre = cbManager.getFontColor(cbManager.enumColor.Celeste.name());
		String colorNaranjo = cbManager.getFontColor(cbManager.enumColor.Naranjo_Claro.name());

		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Private Forum Post Viewer") + central.LineaDivisora(1);
		html += "<table width=270 align=CENTER bgcolor="+colorVioBrill+">"+
				"<tr>" +
					"<td width=135 align=CENTER><font color="+colorTexto+">Name:</font></td>" +
					"<td width=135 align=CENTER><font color="+colorNombre+">"+ NombreChar +"</font></td>"+
				"</tr>"+
				"<tr>"+
					"<td width=135 align=CENTER><font color="+colorTexto+">Create Date:</font></td>" +
					"<td width=135 align=CENTER><font color="+colorNombre+">"+ FechaIngreso +"</font></td>"+
			"</tr></table><br1>";
		html += central.LineaDivisora(3) + "<table width=270 align=CENTER bgcolor="+colorVioBrill+"><tr>" +
											"<td width=270 align=CENTER>Post</td>" +
											"</tr></table>" + central.LineaDivisora(1);
		html += "<table width=270 align=CENTER bgcolor="+colorVioOscu+"><tr>" +
					"<td width=270 fixwidth=260 align=CENTER><font color="+ colorNaranjo +">"+ Memo +"</font></td>" +
					"</tr></table>"+central.LineaDivisora(1);

		html += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, html);
	}

	private static String getClanForum(L2PcInstance player, int Pagina){
		String retorno = "<table width=750 align=CENTER bgcolor="+cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name())+">";

		boolean isClanLeader = player.isClanLeader();

		int MaximoPorPagina = general.COMMUNITY_BOARD_CLAN_ROWN_LIST;
		int Desde = MaximoPorPagina * Pagina;
		int idClan = player.getClanId();

		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";SHOW_POST;"+ String.valueOf(Pagina + 1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";SHOW_POST;"+ String.valueOf(Pagina - 1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";


		String Consulta = "SELECT zeus_cb_clan_foro.id, zeus_cb_clan_foro.idChar," +
							"(IFNULL((SELECT characters.char_name FROM characters WHERE characters.charId = zeus_cb_clan_foro.idChar),\"NO EXISTS\")),"+
							"zeus_cb_clan_foro.idClan, zeus_cb_clan_foro.memo, zeus_cb_clan_foro.createdate "+
							"FROM zeus_cb_clan_foro WHERE zeus_cb_clan_foro.idClan=? ORDER BY zeus_cb_clan_foro.createdate DESC LIMIT " + String.valueOf(Desde) + ", " + String.valueOf(MaximoPorPagina + 1);
		retorno += "<tr>" +
				"<td width=40><font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name())+">N</font></td>"+
				"<td width=180><font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name())+">Char Name</font></td>"+
				"<td width=490 align=CENTER><font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name())+">Message</font></td>"+
				"<td width=40><font color="+cbManager.getFontColor(cbManager.enumColor.Celeste.name())+"></font></td>"+
			"</tr>";

		String coloresGrilla[] = { cbManager.getFontColor(cbManager.enumColor.COLOR_CLANLEADER.name()), cbManager.getFontColor(cbManager.enumColor.COLOR_VIP.name())};

		Connection conn = null;
		PreparedStatement psqry = null;

		String btnVerNoticia = "<button value=\"See\" width=35 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";MENS_VER;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String btnBorrarPost = "<button value=\"Delete\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";MENS_DELETE;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnModificarPost = "<button value=\"Change\" width=80 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";MENS_MODIF;" + String.valueOf(Pagina) + ";%IDVER%;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";

		String GrillaOpciones = "<table width=300 align=CENTER><tr>" +
								"<td width=150 align=CENTER>"+btnModificarPost+"</td><td width=150 align=CENTER>"+btnBorrarPost+"</td>"+
								"</tr></table>";
		boolean haveNext = false;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			psqry.setInt(1, idClan);
			ResultSet rss = psqry.executeQuery();
			int Contador=1;
			while (rss.next()){
				if(Contador <= MaximoPorPagina ){
					boolean isOwnerPost = rss.getString(3).equals(player.getName());
					String GrillaOpc = "";
					if(isOwnerPost || isClanLeader){
						GrillaOpc = "<br1>"+ GrillaOpciones.replace("%IDVER%", String.valueOf(rss.getString(1))) + "<br>";
					}

					String MensajeShow = rss.getString(5).replace("<br>", " ").replace("<br1>", " ");
					MensajeShow = MensajeShow.substring(0, (MensajeShow.length()>50 ? 50 : MensajeShow.length()));

					retorno += "<tr>" +
									"<td width=40 align=CENTER><font color=" + coloresGrilla[Contador%2] +">"+(Contador + ( MaximoPorPagina * Pagina ) ) +"</font></td>"+
									"<td width=180 align=CENTER><font color=" + coloresGrilla[Contador%2] +">"+rss.getString(3)+"</font></td>"+
									"<td width=490 align=CENTER><font color=" + coloresGrilla[Contador%2] +">"+ MensajeShow +"</font>"+GrillaOpc+"</td>"+
									"<td width=40 align=CENTER><font color=" + coloresGrilla[Contador%2] +">"+ btnVerNoticia.replace("%IDVER%", String.valueOf(rss.getInt(1))) +"</font></td>"+
								"</tr>";
				}else{
					haveNext = true;
				}
				Contador++;
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}
		retorno+="</table>";

		String tablaControles = "<br><table width=700 align=CENTER><tr>" +
				"<td width=60 align=CENTER>"+ ( Pagina > 0 ? btnPrevi : "" ) +"</td>"+
				"<td width=580 align=CENTER>Clan post page "+ String.valueOf(Pagina + 1) +"</td>"+
				"<td width=60 align=CENTER>"+ (haveNext ? btnNext : "") +"</td>"+
				"</tr></table>";

		retorno += tablaControles;

		return retorno;
	}

	public static void getPreviewClanNotice(L2PcInstance player, String Mensaje){
		String FormatoModifMen = setModifClanMessage(player,paginaPreview.get(player.getObjectId()));
		cbManager.send1001(FormatoModifMen, player);
		cbManager.send1002(player, Mensaje , " ", "0");
		String html = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Clan Notice Preview") + central.LineaDivisora(1);
		html += "<table width=280 align=CENTER bgcolor="+ cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()) +"><tr>"+
				"<td width=280 align=CENTER fixwidth=280>"+ Mensaje +"</td>"+
				"</tr></table>";
		html += central.getPieHTML(false) + "</body></html>";
		opera.enviarHTML(player, html);
	}


	private static String getClan(L2PcInstance player, String Params, int pag){
		boolean playerHaveClan = player.getClan() == null ? false : true;
		String titulo = "";

		String btnNuevo = "<button value=\"New Post\" width=120 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";NEW_POST;"+String.valueOf(pag)+";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnClanMessege = "<button value=\"Clan Message\" width=180 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";CLAN_MODIF_MESSAGE;"+String.valueOf(pag)+";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		
		boolean showCreateClanOption = false;
		
		if(playerHaveClan){
			boolean isNoticeOn = player.getClan().isNoticeEnabled();
			String btnNoticeClan = "<button value=\""+ (isNoticeOn ? "Deactivate Notice" : "Active Notice") +"\" width=180 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";NOTICE_BTN;"+ String.valueOf(pag) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
			String grillaBotones = "<br1><table width=580 align=CENTER><tr><td widh=290 align=CENTER>"+ btnClanMessege +"</td><td widh=290 align=CENTER>"+ btnNoticeClan +"</td></tr></table>";
			titulo = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Naranjo_Oscuro.name()) + ">Clan " + player.getClan().getName() + " Private Forum</font>"+btnNuevo + (player.isClanLeader() ? grillaBotones : "");
		}else{
			titulo = "<font color=" + cbManager.getFontColor(cbManager.enumColor.Celeste.name()) + ">You need to join/create a clan to view this section</font>";
			showCreateClanOption = true;
		}
		String retorno ="<html><body>" + cbManager._formTituloComunidad(titulo);
		if(playerHaveClan){
			retorno += cbManager._formBodyComunidad(getClanForum(player,pag), cbManager.getFontColor(cbManager.enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name()));
		}else if(showCreateClanOption){
			String btnCreateClan = "<button value=\"Create Clan\" width=180 height=24 action=\"bypass " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC() + ";SHOW_CREATE_CLAN;0;0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
			retorno += cbManager._formTituloComunidad(btnCreateClan,false);
		}
		retorno += ""+cbManager.getPieCommunidad()+"</body></html>";
		return retorno;
	}
	
	public static void createClan(L2PcInstance player, String ClanName){
		if(isValidName(ClanName)){
			ClanTable.getInstance().createClan(player, ClanName);
			return;
		}
		central.msgbox(language.getInstance().getMsg(player).CLAN_NAME_FAIL, player);
	}
	
	private static boolean isValidName(String name)
	{
		Pattern pattern;
		try
		{
			pattern = Pattern.compile(Config.CLAN_NAME_TEMPLATE);
		}
		catch (PatternSyntaxException e)
		{
			_log.warning("ERROR: Wrong pattern for clan name!");
			pattern = Pattern.compile(".*");
		}
		return pattern.matcher(name).matches();
	}	

	private static void showCreateClanWindows(L2PcInstance player){
		//createClan.html
		String ByPassCreate = "bypass -h ZeuS clan Create $txtClan";
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/createClan.html"); 
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%LINK%", ByPassCreate);
		central.sendHtml(player, html.getHtml(), false);			
	}


	@SuppressWarnings("unused")
	public static String delegar(L2PcInstance player, String params){
		IdPostModif.put(player.getObjectId(), 0);
		String retorno = "";
		if(params == null){
			return getClan(player, "", 0);
		}else if(params.length()==0){
			return getClan(player, "", 0);
		}else if(params.equals(general.getCOMMUNITY_BOARD_CLAN_PART_EXEC())){
			return getClan(player, "", 0);
		}
		if(params.contains(";")){
			String[] Eventos = params.split(";");
			String Event = Eventos[1];
			String parm1 = Eventos[2];
			String parm2 = Eventos[3];
			String parm3 = Eventos[4];
			String parm4 = Eventos[5];
			switch (Event){
				case "SHOW_CREATE_CLAN":
					showCreateClanWindows(player);
					break;
				case "NOTICE_BTN":
					if(player.isClanLeader()){
						player.getClan().setNoticeEnabled(( player.getClan().isNoticeEnabled() ? false :true ));
					}
					return getClan(player, "", Integer.valueOf(parm1));
				case "SHOW_POST":
					return getClan(player, "", Integer.valueOf(parm1));
				case "MENS_VER":
					getMensaje(player,Integer.valueOf(parm2));
					return getClan(player, "", Integer.valueOf(parm1));
				case "NEW_POST":
					return setPageNewPost(player,Integer.valueOf(parm1),"");
				case "MENS_DELETE":
					setDeletePost(player,Integer.valueOf(parm2));
					return getClan(player, "",0);
				case "MENS_MODIF":
					String FormatoModificar = setModifPost(player,Integer.valueOf(parm1));
					int idPost = Integer.valueOf(parm2);
					IdPostModif.put(player.getObjectId(), idPost);
					String strMensaje = getPostByID(idPost);
					cbManager.send1001(FormatoModificar, player);
					cbManager.send1002(player, strMensaje, " ", "0");
					return "";
				case "CLAN_MODIF_MESSAGE":
					paginaPreview.put(player.getObjectId(), Integer.valueOf(parm1));
					String FormatoModifMen = setModifClanMessage(player,Integer.valueOf(parm1));
					cbManager.send1001(FormatoModifMen, player);
					_log.warning("NOTICE: " + player.getClan().getNotice());
					cbManager.send1002(player, (player.getClan().getNotice()==null ? "" : player.getClan().getNotice())  , " ", "0");
					return "";
			}
		}
		return retorno;
	}

}
