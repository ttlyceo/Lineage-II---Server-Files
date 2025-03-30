package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.data.xml.impl.ClassListData;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SetupGauge;
import l2r.gameserver.network.serverpackets.TutorialShowHtml;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class central {

	private static Logger _log = Logger.getLogger(central.class.getName());
	public static int IntPintarGrilla = -1;

	public static void sendHtml(L2PcInstance player, String ShowHTML) {
		sendHtml(player, ShowHTML, false);
	}

	public static void sendHtml(L2PcInstance player, NpcHtmlMessage ShowHTML) {
		sendHtml(player, ShowHTML.getHtml(), false);
	}
	
	public static void sendHtml(L2PcInstance player, String ShowHTML, boolean tutorial) {
		ShowHTML = opera.cleanHtml(ShowHTML);
		if (!tutorial) {
			player.sendPacket(new NpcHtmlMessage(0, ShowHTML));
		} else {
			player.sendPacket(new TutorialShowHtml(ShowHTML));
		}
	}

	public static String getPieHTML(boolean showLink) {
		String Retorno = "";
		if (showLink) {
			Retorno = LineaDivisora(1)
					+ "<br><center><a action=\"bypass -h ZeuSNPC howiam 0 0 0\" >"
					+ general.PIE_PAGINA() + "</a></center>";
		} else {
			Retorno = LineaDivisora(1) + "<br><center>" + general.PIE_PAGINA()
					+ "</center>";
		}
		return Retorno;
	}

	public static String getPieHTML() {
		return getPieHTML(true);
	}

	public static void msgbox_Lado(String Mensaje, L2PcInstance st, String TITULO_MENSAJE) {
		CreatureSay cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "[" + TITULO_MENSAJE + "]", Mensaje);
		st.sendPacket(cs);
	}

	public static void msgbox_Lado(String Mensaje, L2PcInstance st) {
		CreatureSay cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, "[" + general.NOMBRE_NPC + "]", Mensaje);
		st.sendPacket(cs);
	}

	public static String ItemShowReward_annoucement(String ItemReward) {
		String Retorno = "";

		String[] MatrizPremeios = ItemReward.split(";");
		for (String Premios : MatrizPremeios) {
			String[] indPremio = Premios.split(",");
			if (Retorno.length() > 0) {
				Retorno += ", ";
			}
			Retorno += opera.getFormatNumbers(indPremio[1]) + " "
					+ getNombreITEMbyID(Integer.valueOf(indPremio[0]));
		}

		return Retorno;
	}

	public static String ItemNeedShowBox(String ItemNeed) {
		return ItemNeedShowBox(ItemNeed, "Cost of service");
	}

	public static String ItemNeedShowBox(String ItemNeed, String Titulo) {
		String Retorno = ItemNeedShow(ItemNeed);
		Retorno = headFormat("<font color=FE9A2E>" + Titulo + "</font><br1>"
				+ Retorno);
		return Retorno;
	}

	public static String ItemNeedShow_line(String ItemNeed) {
		String Retorno = "";

		String[] MatrizPremeios = ItemNeed.split(";");
		for (String Premios : MatrizPremeios) {
			String[] indPremio = Premios.split(",");
			if (Retorno.length() > 0) {
				Retorno += ", ";
			}
			try {
				Retorno += "<font color=LEVEL>" + indPremio[1] + " "
						+ getNombreITEMbyID(Integer.valueOf(indPremio[0]))
						+ "</font>";
			} catch (Exception a) {
				_log.warning("CHECK ITEM-> Error. ItemID not exist: "
						+ indPremio[0]);
			}
		}

		return Retorno;
	}

	public static String ItemNeedShow(String ItemNeed) {
		String Retorno = "";

		String[] MatrizPremeios = ItemNeed.split(";");
		for (String Premios : MatrizPremeios) {
			String[] indPremio = Premios.split(",");
			if (Retorno.length() > 0) {
				Retorno += "<br1>";
			}
			try {
				Retorno += "<font color=LEVEL>"
						+ opera.getFormatNumbers(indPremio[1])
						+ "</font> of <font color=FF8000>"
						+ getNombreITEMbyID(Integer.valueOf(indPremio[0]))
						+ "</font>";
			} catch (Exception a) {
				_log.warning("CHECK ITEM-> Error. ItemID not exist: "
						+ indPremio[0]);
			}
		}

		return Retorno;
	}

	public static void msgbox(String Mensaje, L2PcInstance st) {
		st.sendMessage(Mensaje);
	}

	public static int getStatusDesafioBusNPC() {
		Connection conn = null;
		int _estadoDesafio = 0;
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_evento_inicial(-2,-1)";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()) {
				_estadoDesafio = rss.getInt(1);
			}

		} catch (SQLException e) {

		}

		try {
			conn.close();
		} catch (Exception a) {

		}
		return _estadoDesafio;
	}

	public static int[] getStatusDesafio(L2PcInstance st) {
		Connection conn = null;
		int[] _estadoDesafio = new int[800];
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_evento_inicial(-1,-1)";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()) {
				_estadoDesafio[Integer.valueOf(rss.getString(1))] = _estadoDesafio[Integer
						.valueOf(rss.getString(1))] + 1;
			}

		} catch (SQLException e) {

		}

		try {
			conn.close();
		} catch (Exception a) {

		}

		return _estadoDesafio;
	}

	public static String getNombreITEMbyID(int id, boolean showLogError) {
		return getNombreITEMbyID(id, showLogError, false);
	}
	
	public static L2Item getItemData(int idItem){
		return ItemData.getInstance().getTemplate(idItem);
	}

	public static String getNombreITEMbyID(int id, boolean showLogError, boolean forDropSearchFilter) {
		try {
			if (!forDropSearchFilter) {
				return general.ITEM_FOR_SEARCH.get(id).ITEM_NAME;
			}else{
				if(general.ITEM_FOR_SEARCH.get(id).ITEM_FOR_SEARCH) {
					return general.ITEM_FOR_SEARCH.get(id).ITEM_NAME;
				}else{
					return "NULL";
				}
			}
		} catch (Exception a) {
			if (showLogError) {
				_log.warning("CHECK ITEM-> Error. ItemID not exist: " + String.valueOf(id));
			}
		}
		return "NULL";
	}

	public static String getNombreITEMbyID(int id) {
		return getNombreITEMbyID(id, true);
	}

	public static String getTipoITEMbyID(int id) {
		try {
			return ItemData.getInstance().getTemplate(id).getItemType()
					.toString();
		} catch (Exception a) {
			_log.warning("CHECK ITEM-> Error. ItemID not exist: "
					+ String.valueOf(id));
			return "NULL";
		}
	}

	public static String EjecutarDesafio(L2PcInstance st, String opc) {
		String Respuesta = "";
		Connection conn = null;
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_evento_inicial(" + opc + ","
					+ String.valueOf(st.getObjectId()) + ")";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();

			while (rss.next()) {
				Respuesta = rss.getString(1);
			}
		} catch (SQLException e) {

		}

		try {
			conn.close();
		} catch (Exception a) {

		}

		return Respuesta;
	}

	public static boolean isNameDisponibleAIO(L2PcInstance st) {
		boolean Responder = false;
		Connection conn = null;
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_buffaio(2,'" + st.getName() + "')";
			CallableStatement psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()) {
				if (rss.getInt("A") == 0) {
					Responder = true;
				}
			}
		} catch (SQLException e) {

		}
		try {
			conn.close();
		} catch (Exception a) {

		}
		return Responder;
	}

	public static void healAll(L2PcInstance st, boolean isSummon) {
		if (!isSummon) {
			st.getStatus().setCurrentHp(st.getStat().getMaxHp());
			st.getStatus().setCurrentMp(st.getStat().getMaxMp());
			st.getStatus().setCurrentCp(st.getStat().getMaxCp());
		} else {
			if (st.getSummon() != null) {
				st.getSummon().getStatus()
						.setCurrentHp(st.getSummon().getStat().getMaxHp());
				st.getSummon().getStatus()
						.setCurrentMp(st.getSummon().getStat().getMaxMp());

			}
		}
	}

	public static String[] getDateCastleRegEnd(L2PcInstance st) {
		String ListaDate[] = new String[20];
		String Prelist = "";
		int Contador = 0;
		try {
			Connection conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "SELECT FROM_UNIXTIME(castle.regTimeEnd /1000) as FechaLimite FROM castle ORDER BY castle.id ASC";
			PreparedStatement psqry = conn.prepareStatement(qry);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()) {
				Prelist = rss.getString("FechaLimite");
				ListaDate[Contador] = Prelist;
				Contador++;
			}
			conn.close();
		} catch (SQLException e) {

		}
		return ListaDate;
	}

	public static void setBlockPJSeg(L2PcInstance Player, int Segundos) {
		Player.sendPacket(new SetupGauge(3, Integer
				.valueOf((Segundos * 1000) + 300)));
	}

	@SuppressWarnings("unused")
	public static Boolean SetBlockTimeVote(L2PcInstance st) {
		long endtime = (System.currentTimeMillis() / 1000)
				+ general.VOTO_REWARD_SEG_ESPERA;
		st.sendPacket(new SetupGauge(3, Integer
				.valueOf((general.VOTO_REWARD_SEG_ESPERA * 1000) + 300)));
		Boolean val = true;
		return val;
	}

	public static String LineaDivisora(int Ancho) {
		return "<img src=\"L2UI.SquareBlank\" width=280 height="
				+ String.valueOf(Ancho)
				+ "><img src=\"L2UI.SquareGray\" width=280 height="
				+ String.valueOf(Ancho)
				+ "><img src=\"L2UI.SquareBlank\" width=270 height="
				+ String.valueOf(Ancho) + ">";
	}

	public static String ErrorTipeoEspacio(L2PcInstance player) {
		String HTML_STR = "<html><title>" + general.TITULO_NPC()
				+ "</title><body>";
		HTML_STR += LineaDivisora(2) + headFormat("ERROR") + LineaDivisora(2);
		HTML_STR += LineaDivisora(2) + headFormat(language.getInstance().getMsg(player).TYPING_ERROR, "B40404")
				+ LineaDivisora(2);
		HTML_STR += "</body></html>";
		return HTML_STR;
	}

	public static String headFormat(String Titulo) {
		String TITLE_NPC;
		if (Titulo == "") {
			TITLE_NPC = "WELCOME TRAVELER";
		} else {
			TITLE_NPC = Titulo;
		}

		String MAIN_HTM = "<center><table width=280 border=0 bgcolor=151515>";
		MAIN_HTM += "<tr><td width=280 align=center fixwidth=280><font color=EBDF6C>"
				+ TITLE_NPC + "</font></td></tr></table></center>";
		return MAIN_HTM;
	}

	public static String headFormat(String Titulo, String Color) {
		String TITLE_NPC;
		if (Titulo == "") {
			TITLE_NPC = "WELCOME TRAVELER";
		} else {
			TITLE_NPC = Titulo;
		}
		String MAIN_HTM = "<center><table width=280 border=0 bgcolor=151515>";
		MAIN_HTM += "<tr><td width=280 align=center fixwidth=280><font color="
				+ Color + ">" + TITLE_NPC
				+ "</font></td></tr></table></center>";
		return MAIN_HTM;
	}

	public static String BotonClasicCentral(String Boton, String Boton2) {
		String MAIN_HTM = "";
		MAIN_HTM = "<table width=280 border=0 bgcolor=151515><tr>";
		MAIN_HTM += "<td width=12 align=left></td>";
		MAIN_HTM += "<td width=128 align=center>" + Boton + "</td>";
		MAIN_HTM += "<td width=128 align=center>" + Boton2 + "</td>";
		MAIN_HTM += "<td width=12 align=left></td>";
		MAIN_HTM += "</tr>";
		MAIN_HTM += "</table><img src=\"L2UI.SquareBlank\" width=280 height=1>";
		return MAIN_HTM;
	}

	public static String BotonCentral(String Boton, String Mensaje, int Color) {
		String COLORPINTAR;
		if (Color == 0) {
			COLORPINTAR = "C4A23D";
		} else {
			COLORPINTAR = "C4803D";
		}

		String MAIN_HTM = "";

		MAIN_HTM = "<table width=280 border=0 bgcolor=151515><tr>";
		MAIN_HTM += "<td width=2 align=left></td>";
		MAIN_HTM += "<td width=90 align=left>" + Boton + "</td>";
		MAIN_HTM += "<td width=180 align=left><font color=" + COLORPINTAR + ">"
				+ Mensaje + "</font></td>";
		MAIN_HTM += "<td width=2 align=left></td>";
		MAIN_HTM += "</tr>";
		MAIN_HTM += "</table><img src=\"L2UI.SquareBlank\" width=280 height=1>";
		return MAIN_HTM;
	}

	public static String BotonCentral(String Boton, String Mensaje) {
		String MAIN_HTM = "<table width=280 border=0 bgcolor=151515><tr>";
		MAIN_HTM += "<td width=90 align=left>" + Boton + "</td>";
		MAIN_HTM += "<td width=130 align=right>" + Mensaje + "</td>";
		MAIN_HTM += "</tr></table>";
		MAIN_HTM += LineaDivisora(2);
		return MAIN_HTM;
	}

	public static String BotonCentral_sinFormato(String Boton, String Mensaje) {
		String MAIN_HTM = "<tr><td width=2 align=left></td>";
		MAIN_HTM += "<td width=90 align=left>" + Boton + "</td>";
		MAIN_HTM += "<td width=130 align=right>" + Mensaje + "</td>";
		MAIN_HTM += "</tr>";
		MAIN_HTM += LineaDivisora(1);
		return MAIN_HTM;
	}

	@SuppressWarnings("unused")
	public static boolean isNumeric(String s) {
		try {
			double y = Double.parseDouble(s);
			return true;
		} catch (NumberFormatException err) {
			return false;
		}
	}

	public static int INT_PINTAR_GRILLA(int INICIO) {

		if (INICIO > 0) {
			IntPintarGrilla = INICIO;
		}
		if (IntPintarGrilla == 1) {
			IntPintarGrilla = 0;
		} else {
			IntPintarGrilla = 1;
		}

		return IntPintarGrilla;
	}

	/*
	 * Freya public static String getClassName(L2PcInstance st,int idClass){
	 * return CharTemplateTable.getInstance().getClassNameById(idClass); }
	 * 
	 * public static String getClassName(L2PcInstance st){ return
	 * CharTemplateTable
	 * .getInstance().getClassNameById(st.getClassId().getId()); }
	 */

	public static String getClassName(L2PcInstance st, int idClass) {
		return ClassListData.getInstance().getClass(idClass).getClassName();
	}

	public static String getClassName(L2PcInstance st) {
		return ClassListData.getInstance().getClass(st.getClassId().getId())
				.getClassName();
	}

	public static void setAllConfigCharToBD(L2PcInstance player) {
		int _SHOWEFECT = (general.getCharConfigEFFECT(player) ? 1 : 0);
		int _SHOWANNOU = (general.getCharConfigANNOU(player) ? 1 : 0);
		int _SHOWMYSTAT = (general.getCharConfigSHOWSTAT(player) ? 1 : 0);
		int _SHOWPIN = (general.getCharConfigPIN(player) ? 1 : 0);
		int _HERO = (general.getCharConfigHERO(player) ? 1 : 0);
		int _EXPSP = (general.getCharConfigEXPSP(player) ? 1 : 0);
		int _TRADE = (general.getCharConfigTRADE(player) ? 1 : 0);
		int _BADBUFF = (general.getCharConfigBADBUFF(player) ? 1 : 0);
		int _HIDESTORE = (general.getCharConfigHIDESTORE(player) ? 1 : 0);
		int _REFUSAL = (general.getCharConfigREFUSAL(player) ? 1 : 0);
		int _PARTYMATCHING = (general.getCharConfigPartyMatching(player) ? 1: 0);
		int _OLYSCHEME = (general.getCharConfigOlyScheme(player) ? 1:0);
		int _OLYREAD = (general.getCharConfigReadOlyWinner(player) ? 1:0);
		//TODO: aqui
		set_ConfigCHAR(player, _SHOWEFECT, _SHOWANNOU, _SHOWMYSTAT, _SHOWPIN,
				_HERO, _EXPSP, _TRADE, _BADBUFF, _HIDESTORE, _REFUSAL,
				_PARTYMATCHING, _OLYSCHEME,_OLYREAD);
	}

	public static void set_ConfigCHAR(L2PcInstance player, Integer _SHOWEFECT,
			Integer _SHOWANNOU, Integer _SHOWMYSTAT, Integer _SHOWPIN,
			Integer _HERO, Integer _EXPSP, Integer _TRADE, Integer _BADBUFF,
			Integer _HIDESTORE, Integer _REFUSAL, Integer _PARTYMATCHING, Integer _OLYSCHEME, Integer _OLYREAD) {
		String UPDATE_CONFIG_PVPPK = "UPDATE zeus_char_config SET "
				+ "annouc=?,effect=?,statt=?,pin=?,hero=?,"
				+ "expsp=?,trade=?,badbuff=?,hidestore=?,"
				+ "refusal=?,partymatching=?,olyBuffScheme=?,olyReadWinners=?" + " WHERE idchar=?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection()) {
			PreparedStatement statement;
			statement = con.prepareStatement(UPDATE_CONFIG_PVPPK);
			statement.setString(1, _SHOWANNOU.toString());
			statement.setString(2, _SHOWEFECT.toString());
			statement.setString(3, _SHOWMYSTAT.toString());
			statement.setString(4, _SHOWPIN.toString());
			statement.setString(5, _HERO.toString());
			statement.setString(6, _EXPSP.toString());
			statement.setString(7, _TRADE.toString());
			statement.setString(8, _BADBUFF.toString());
			statement.setString(9, _HIDESTORE.toString());
			statement.setString(10, _REFUSAL.toString());
			statement.setString(11, _PARTYMATCHING.toString());
			statement.setString(12, _OLYSCHEME.toString());
			statement.setString(13, _OLYREAD.toString());
			statement.setInt(14, player.getObjectId());
			statement.execute();
			statement.close();
		} catch (SQLException e) {
			_log.warning("ZeuS Config-> Error Save personal Config '"
					+ player.getName() + "': " + e.getMessage());
		}
		general.getInstance().loadCharConfig(player);
	}

}
