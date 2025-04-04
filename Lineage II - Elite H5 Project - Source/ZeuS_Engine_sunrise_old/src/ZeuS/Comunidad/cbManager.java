package ZeuS.Comunidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ShowBoard;
import ZeuS.Config.general;
import ZeuS.procedimientos.opera;


public class cbManager {
	private static final Logger _log = Logger.getLogger(cbManager.class.getName());

	private static  String Titulo(){
		return "<center><font color=86AB98>"+ general.TITULO_NPC() + " Community Board" +"</font></center>";
	}

	public static String getPieCommunidad(){
		String retorno = "<table width=720 align=CENTER><tr>" +
							"<td width=720 align=CENTER>" +
							general.PIE_PAGINA_COMMUNIDAD()+
							"</td>" +
							"</tr></table><br>";
		return retorno;
	}

	protected enum enumTipoEscritura{
		Z_ANNOUCEMENT,
		Z_CHANGELOG,
		Z_RULES,
		Z_STAFF
	}

	public enum enumColor{
		Naranjo_Claro,
		Naranjo_Oscuro,
		Verde,
		Gris,
		Blanco,
		COLOR_GM,
		COLOR_HERO,
		COLOR_CLANLEADER,
		COLOR_VIP,
		COLOR_NORMAL,
		COLOR_PVP,
		COLOR_PK,
		COLOR_PRIVATESTORE,
		COLOR_DEATH,
		COLOR_JAILED,
		COLOR_BUFFSTORE,
		Celeste,
		Azul,
		COLOR_CABECERA,
		COLOR_GRILLA_DEFAULT,
		COLOR_GRILLA_VERDE,
		COLOR_GRILLA_NARANJO,
		COLOR_GRILLA_ROJO,
		COLOR_GRILLA_AZUL,
		COLOR_GRILLA_VIOLETA_OSCURO,
		COLOR_GRILLA_VIOLETA_BRILLANTE,
		Rojo,
		celeste_oscuro,
		Amarillo
	}
	private static void setColores(){
		Colores.put(enumColor.Naranjo_Claro.name(), "EBC277");
		Colores.put(enumColor.Naranjo_Oscuro.name(), "EF8514");
		Colores.put(enumColor.Verde.name(),"58C08C");
		Colores.put(enumColor.Gris.name() , "7F7F7F");
		Colores.put(enumColor.Blanco.name(), "ECECEC");
		Colores.put(enumColor.COLOR_GM.name(),"8FDC9D");
		Colores.put(enumColor.COLOR_HERO.name(),"E6E44F");
		Colores.put(enumColor.COLOR_CLANLEADER.name(),"E5C384");
		Colores.put(enumColor.COLOR_VIP.name(),"83D8DE");
		Colores.put(enumColor.COLOR_PVP.name(),"8D699F");
		Colores.put(enumColor.COLOR_PK.name(),"CD4747");
		Colores.put(enumColor.COLOR_PRIVATESTORE.name(),"9F9C9C");
		Colores.put(enumColor.COLOR_DEATH.name(),"877458");
		Colores.put(enumColor.COLOR_JAILED.name(),"EF8514");
		Colores.put(enumColor.COLOR_BUFFSTORE.name(),"7878FA");
		Colores.put(enumColor.Celeste.name(), "2E9AFE");
		Colores.put(enumColor.Azul.name(), "045FB4");
		Colores.put(enumColor.COLOR_CABECERA.name(),"1B1B1B");
		Colores.put(enumColor.COLOR_GRILLA_DEFAULT.name(), "171717");
		Colores.put(enumColor.COLOR_GRILLA_VERDE.name(),"243B0B");
		Colores.put(enumColor.COLOR_GRILLA_ROJO.name(), "8A2908");
		Colores.put(enumColor.COLOR_GRILLA_NARANJO.name(), "B45F04");
		Colores.put(enumColor.COLOR_GRILLA_AZUL.name(), "0B2161");
		Colores.put(enumColor.Rojo.name(), "8A2908");
		Colores.put(enumColor.COLOR_GRILLA_VIOLETA_OSCURO.name(),"201020");
		Colores.put(enumColor.COLOR_GRILLA_VIOLETA_BRILLANTE.name(),"302030");
		Colores.put(enumColor.celeste_oscuro.name(),"0097CD");
		Colores.put(enumColor.Amarillo.name(), "FAEB79");
	}
	private static Map<String,String> Colores = new HashMap<String, String>();

	public void parsecmd(String command, L2PcInstance activeChar) {
	}

	public void parsewrite(String ar1, String ar2, String ar3, String ar4, String ar5, L2PcInstance activeChar) {
	}

	public static void loadColores(){
		boolean cargar = false;
		if(Colores==null){
			cargar = true;
		}else if(Colores.size()==0){
			cargar = true;
		}
		if(cargar){
			setColores();
		}
	}
	
	public static int getNoReadBugReport(){
		int Retorno = 0;
		String Consulta = "SELECT COUNT(*) FROM zeus_bug_report WHERE zeus_bug_report.leido = 'NO'";
		
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						Retorno = rss.getInt("count(*)");
					}catch(SQLException a){
						_log.warning("Error A: " + a.getMessage());
					}
				}
			conn.close();
			}catch(SQLException e){
				_log.warning("Error: E" + e.getMessage());
			}
		try{
			conn.close();
		}catch (Exception e) {

		}		
		
		
		
		return Retorno;
	}

	public static String getFontColor(String color){
		return Colores.get(color);
	}

	protected static String _formLine(int Largo){
		String Retorno = "";
		Retorno = "<img src=\"L2UI.SquareGray\" width="+ String.valueOf(Largo) +" height=2><img src=\"L2UI.SquareBlank\" width="+ String.valueOf(Largo - 70) +" height=3>";
		return Retorno;
	}

	protected static String _formBodyComunidad(String Cuerpo){
		return _formBodyComunidad(Cuerpo, Colores.get(enumColor.COLOR_GRILLA_DEFAULT.name()));
	}

	protected static String _formBodyComunidad(String Cuerpo, String ColorFondoGrilla){
		String retorno ="<center>"+
				"<img src=\"L2UI.SquareGray\" width=790 height=2>"+
				"<img src=\"L2UI.SquareBlank\" width=720 height=3>"+
				"<table width=770 bgcolor="+ ColorFondoGrilla +"><tr><td align=LEFT width=770>"+Cuerpo+"<br></td></tr></table>"+
				"<img src=\"L2UI.SquareBlank\" width=720 height=3>"+
				"<img src=\"L2UI.SquareGray\" width=790 height=2>";
		retorno += "</center>";
		return retorno;
	}

	protected static String _formTituloComunidad(String Cuerpo, Boolean EncabezadoZeuS){
		return _formTituloComunidad(Cuerpo, EncabezadoZeuS, Colores.get(enumColor.COLOR_GRILLA_DEFAULT.name()));
	}

	protected static String _formTituloComunidad(String Cuerpo, String ColorGrillaFondo){
		return _formTituloComunidad(Cuerpo, true, ColorGrillaFondo);
	}

	public static String _formTituloComunidad(String Cuerpo){
		return _formTituloComunidad(Cuerpo, true, Colores.get(enumColor.COLOR_GRILLA_DEFAULT.name()));
	}

	protected static String _formTituloComunidad(String Cuerpo, Boolean EncabezadoZeuS, String ColorgrillaFondo){
		String retorno = "<center>"+ ( EncabezadoZeuS ? " <br>" + Titulo() + "<br1>" : "") +
				"<img src=\"L2UI.SquareGray\" width=620 height=1>"+
				"<img src=\"L2UI.SquareBlank\" width=620 height=2>"+
				"<table width=616 bgcolor="+ColorgrillaFondo+"><tr><td width=16><img src=\"L2UI_CT1.RadarMap_DF_iCN_Target01\" width=16 height=16></td><td align=CENTER width=560>"+Cuerpo+"<br1></td><td width=16><img src=\"L2UI_CT1.RadarMap_DF_iCN_Target01\" width=16 height=16></td></tr></table>"+
				"<img src=\"L2UI.SquareBlank\" width=620 height=3>"+
				"<img src=\"L2UI.SquareGray\" width=620 height=2>";
		retorno += "</center>";
		return retorno;
	}
	
	public static String _formTituloComunidad(){
		String retorno ="<center>"+
				"<img src=\"L2UI.SquareGray\" width=780 height=1>"+
				"<img src=\"L2UI.SquareBlank\" width=780 height=2>"+
				"<table width=616 bgcolor="+Colores.get(enumColor.COLOR_GRILLA_DEFAULT.name())+">"
						+ "<tr>"
						+ "<td width=16><img src=\"L2UI_CT1.RadarMap_DF_iCN_Target01\" width=16 height=16></td>"
						+ "<td align=CENTER width=560>"+Titulo()+"<br1></td>"
						+ "<td width=16><img src=\"L2UI_CT1.RadarMap_DF_iCN_Target01\" width=16 height=16></td>"
						+ "</tr></table>"+
				"<img src=\"L2UI.SquareBlank\" width=780 height=2>"+
				"<img src=\"L2UI.SquareGray\" width=780 height=1>";
		retorno += "</center>";
		return retorno;
	}
	
	/*
	public static void separateAndSend(String html, L2PcInstance acha)
	{
		if (html == null)
		{
			return;
		}
		
		html = opera.cleanHtml(html);
		
		if (html.length() < 4096)
		{
			acha.sendPacket(new ShowBoard(html, "101"));
			acha.sendPacket(new ShowBoard(null, "102"));
			acha.sendPacket(new ShowBoard(null, "103"));

		}
		else if (html.length() < 8192)
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 4096), "101"));
			acha.sendPacket(new ShowBoard(html.substring(4096), "102"));
			acha.sendPacket(new ShowBoard(null, "103"));

		}
		else if (html.length() < 16384)
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 4096), "101"));
			acha.sendPacket(new ShowBoard(html.substring(4096, 8192), "102"));
			acha.sendPacket(new ShowBoard(html.substring(8192), "103"));
		}
	}
	*/
	public static void separateAndSend(String html, L2PcInstance acha)
	{
		if (html == null || html.isEmpty() || html.equals("") || html.length() < 1)
		{
			return;
		}
		
		if (html.length() < 8180)
		{
			acha.sendPacket(new ShowBoard(html, "101"));
			acha.sendPacket(new ShowBoard(null, "102"));
			acha.sendPacket(new ShowBoard(null, "103"));
		}
		else if (html.length() < (8180 * 2))
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 8180), "101"));
			acha.sendPacket(new ShowBoard(html.substring(8180, html.length()), "102"));
			acha.sendPacket(new ShowBoard(null, "103"));
		}
		else if (html.length() < (8180 * 3))
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 8180), "101"));
			acha.sendPacket(new ShowBoard(html.substring(8180, 8180 * 2), "102"));
			acha.sendPacket(new ShowBoard(html.substring(8180 * 2, html.length()), "103"));
		}
	}

	/**
	 * @param html
	 * @param acha
	 */
	public static void send1001(String html, L2PcInstance acha)
	{
		if (html.length() < 8192)
		{
			acha.sendPacket(new ShowBoard(html, "1001"));
		}
	}

	/**
	 * @param acha
	 */
	public static void send1002(L2PcInstance acha)
	{
		send1002(acha, " ", " ", "0");
	}

	/**
	 * @param activeChar
	 * @param string
	 * @param string2
	 * @param string3
	 */
	public static void send1002(L2PcInstance activeChar, String string, String string2, String string3)
	{
		List<String> _arg = new ArrayList<>();
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add("0");
		_arg.add(activeChar.getName());
		_arg.add(Integer.toString(activeChar.getObjectId()));
		_arg.add(activeChar.getAccountName());
		_arg.add("9");
		_arg.add(string2);
		_arg.add(string2);
		_arg.add(string);
		_arg.add(string3);
		_arg.add(string3);
		_arg.add("0");
		_arg.add("0");
		activeChar.sendPacket(new ShowBoard(_arg));
	}
}
