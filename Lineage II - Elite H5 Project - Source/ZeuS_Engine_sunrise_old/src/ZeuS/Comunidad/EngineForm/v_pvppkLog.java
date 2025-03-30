package ZeuS.Comunidad.EngineForm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._charinfo;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_pvppkLog{
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(v_pvppkLog.class.getName());
	
	@SuppressWarnings("unused")
	private static boolean loadInicio = false;

	
	
	@SuppressWarnings("rawtypes")
	private static NpcHtmlMessage getVentanaBusquedaChar(L2PcInstance player,String palabra, int pagina, NpcHtmlMessage html){
		int contador = 0;
		Iterator itr = general._getCharInfo().entrySet().iterator();
		int maximo = 9;
		Vector<_charinfo> CHAR_INFO_TEMP = new Vector<_charinfo>();
		while(itr.hasNext()){
			Map.Entry Info =(Map.Entry)itr.next();
			String Nombre = (String) Info.getKey();
			if(Nombre.toUpperCase().indexOf(palabra.toUpperCase())>=0){
				_charinfo Temp = (_charinfo)Info.getValue();
				CHAR_INFO_TEMP.add(Temp);
				contador++;
				if(contador>maximo){
					central.msgbox(language.getInstance().getMsg(player).YOU_NEED_TO_BE_MORE_SPECIFIC, player);
					return html;
				}				
			}
		}
		
		String GridData = "";
		contador=0;
		
		String Bypass = "bypass "+general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.pvppklog.name()+";LoadLog;%IDCHAR%;0;0;0;0";
		
		String GridSearchList = opera.getGridFormatFromHtml(html, 1, "%GRID_SEARCH_LIST%");		
		
		
		for(_charinfo T1 : CHAR_INFO_TEMP){
			if(contador==0){
				GridData +="<tr>";
			}
			GridData += GridSearchList.replace("%BYPASS_INFO%", Bypass.replace("%IDCHAR%", String.valueOf(T1.getCharID()))).replace("%CLASS_ICON%", general.getClassInfo(T1.getBase()).getLogo()).replace("%PLAYER_PKS%", opera.getFormatNumbers(T1.getPk())) .replace("%PLAYER_PVPS%", opera.getFormatNumbers(T1.getPvP())).replace("%PLAYER_NAME%", T1.getName()).replace("%PLAYER_CLASS_NAME%", opera.getClassName(T1.getBase()));
			contador++;
			
			if(contador==3){
				GridData +="</tr>";
				contador=0;
			}
		}
		
		if(contador>0 && contador <3){
			for(int i=contador;i<3;i++){
				GridData += "<td fixwidth=240></td>";
			}
			GridData += "</tr>";
		}
		html.replace("%GRID_SEARCH_LIST%", GridData);
		return html;
	}
	
	@SuppressWarnings("unused")
	private static NpcHtmlMessage getLog(String CharID, int Pagina, NpcHtmlMessage html){
		int contador = 0;
		int Maximo = 12;
		boolean continua = false;
		
		String ByPass = "";
		
		String []Botones = {"1B2723","151E1B"};
		
		String GridFormat = opera.getGridFormatFromHtml(html, 1, "%PLAYERS%");
		String GridAllData = "";
		
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "call sp_lista_log_pvp(2,?," + String.valueOf(Maximo + 1) + "," + String.valueOf( Pagina * Maximo ) + ")";
			CallableStatement psqry = conn.prepareCall(qry);
			psqry.setString(1,CharID);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				if (!rss.getString(1).equals("err")){
					String NomWin = rss.getString(1);
					String NomLoo = rss.getString(2);

					if(!rss.getString(5).equalsIgnoreCase("null") && !rss.getString(6).equalsIgnoreCase("null")){
						if(contador < Maximo){
							_charinfo TWin = general._getCharInfo().get(NomWin);
							_charinfo TLoo = general._getCharInfo().get(NomLoo);
							String IconoWin = general.getClassInfo(rss.getInt(5)).getLogo();
							String IconoLoo = general.getClassInfo(rss.getInt(6)).getLogo();

							String IDWin = String.valueOf(TWin.getCharID());
							String IDLoo = String.valueOf(TLoo.getCharID());
							
							String ByPassWin = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+ ";" + Engine.enumBypass.pvppklog.name()+";LoadLog;" + IDWin + ";0;0;0;0";
							String BypassLoo = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+ ";" + Engine.enumBypass.pvppklog.name()+";LoadLog;" + IDLoo + ";0;0;0;0";
							
							String GetNameID = general._getIdToCharName(Integer.valueOf(CharID));
							
							NomWin = (NomWin.equals(GetNameID) ? "<font color=01A9DB>"+NomWin+"</font>" : NomWin);
							NomLoo = (NomLoo.equals(GetNameID) ? "<font color=01A9DB>"+NomLoo+"</font>" : NomLoo);
							
							String BaseWin = "<font color=585858>"+opera.getClassName(rss.getInt(5)) + "</font>";
							String BaseLooser = "<font color=585858>"+opera.getClassName(rss.getInt(6)) + "</font>";
							
							String Tipo = rss.getString(4);
							String Veces = String.valueOf(rss.getInt(3));
							GridAllData += GridFormat.replace("%BGCOLOR%", Botones[contador % 2]).replace("%WINNER_ICON%", cbFormato.getBotonForm(IconoWin, ByPassWin) ).replace("%WINNER_NAME%", NomWin+"<br1>" + BaseWin).replace("%TYPE%", Tipo)
									.replace("%LOOSER_ICON%", cbFormato.getBotonForm(IconoLoo, BypassLoo)).replace("%LOOSER_NAME%", NomLoo + "<br1>" + BaseLooser).replace("%TIMES%", Veces);
						}else{
							continua=true;							
						}
					}
					contador++;
				}
			}
		}
		catch (SQLException e){

		}
		try{
			conn.close();
		}catch(SQLException e){

		}		

		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.pvppklog.name() + ";LoadLog;" + String.valueOf(CharID) + ";" +String.valueOf(Pagina-1)+";0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.pvppklog.name() + ";LoadLog;" + String.valueOf(CharID) + ";" +String.valueOf(Pagina+1)+";0;0;0";
		
		html.replace("%PLAYERS%", GridAllData);
		
		html.replace("%BYPASS_PREV%", ( Pagina>0 ? bypassAntes : ""));
		html.replace("%PAGE%", String.valueOf(Pagina+1));
		html.replace("%BYPASS_NEXT%", (continua ? bypassProx : ""));
		
		
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params,String Buscar, int pagina){
		NpcHtmlMessage html = null; 
		
		if(Params.equals("0")){
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-pvplog.htm");
		}else if(Params.equals("findppl")){
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-pvplog-searching.htm");
			html = getVentanaBusquedaChar(player,Buscar,pagina,html);
		}else if(Params.equals("LoadLog")){
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-pvplog-players-logs.htm");
			html = getLog(Buscar, pagina, html);
		}

		String Bypass = "bypass -h ZeuS pvpLog $txtName";
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%BYPASS_SEARCH%", Bypass);
		
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_LOG_PELEAS) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}			
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,"", Integer.valueOf(parm2));
		}else if(parm1.equals("findppl")){
			return mainHtml(player,parm1,parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("LoadLog")){
			return mainHtml(player,parm1,parm2, Integer.valueOf(parm3));
		}
		return "";
	}
	
	public static void bypassVoice(L2PcInstance player, String Buscar){
		String enviar = "";
		enviar = mainHtml(player, "findppl",Buscar, 0);
		cbManager.separateAndSend(enviar, player);
	}
}
