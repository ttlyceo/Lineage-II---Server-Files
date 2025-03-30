package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;

import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class desafioBusqueda {

	private static final Logger _log = Logger.getLogger(desafioBusqueda.class.getName());

	public static String getBienvenida(L2PcInstance player, int IdNpc){
		String MAIN_HTML = "<html><head><title>"+general.TITULO_NPC()+"</title></head><body>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("Welcome") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat("NPC Hidden Event") + central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("","") + central.LineaDivisora(2);

		MAIN_HTML += checkPlayer(IdNpc,player);
		return MAIN_HTML;
	}

	public static String premiosOK(){
		String Retorno ="<html><title>"+general.TITULO_NPC()+"</title><body>";
		Retorno += central.LineaDivisora(3) + central.headFormat("NPC Hidden Event") + central.LineaDivisora(3);
		Retorno += central.LineaDivisora(1) + central.LineaDivisora(2) + central.LineaDivisora(3) + central.headFormat("","31B404") + central.LineaDivisora(3) + central.LineaDivisora(2) + central.LineaDivisora(1);
		Retorno += central.getPieHTML() + "</body></html>";
		return Retorno;
	}

	public static boolean getPremios(L2PcInstance player, int IdNpc){

		String Entrando ="<html><title>" + general.TITULO_NPC() + "</title><body>";
		Entrando += central.LineaDivisora(2) + central.headFormat("Desafio") + central.LineaDivisora(2);
		Entrando += central.LineaDivisora(1) + central.headFormat("Check Reward, Wait") + central.LineaDivisora(1);
		Entrando += central.getPieHTML() + "</body></html>";

		player.sendPacket(new NpcHtmlMessage(0,Entrando));

		Vector<String> Premios_Otorgar = new Vector<String>();

		Connection conn = null;
		String qry = "";
		CallableStatement psqry = null;
		ResultSet rss = null;

		String Premios = "";

		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "call sp_npc_evento(2,"+ String.valueOf(IdNpc) +"," + String.valueOf(player.getObjectId()) +","+ String.valueOf(general.DESAFIO_NPC_BUSQUEDAS.size()) +")";
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				if(rss.getString(1).equals("cor")){
					Premios = String.valueOf(rss.getInt(2)) + "," + String.valueOf(rss.getInt(3));
					Premios_Otorgar.add(Premios);
				}else{
					central.msgbox("Error en Desafio." + rss.getString(2), player);
				}
			}
		}catch(SQLException a){
			_log.warning("GET PREMIO DESAFIO->" + a.getMessage());
		}finally{

		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}
		

		if(Premios_Otorgar.size()>0){
			opera.AnunciarTodos(language.getInstance().getMsg(player).CHALLENGE_ANNOUCEMENT_PLAYER_FOUND_NPC.replace("$player",player.getName()));
		}else{
			return false;
		}

		int i=1;

		for(String Premios_split : Premios_Otorgar){
			String[] Premio = Premios_split.split(",");
			opera.giveReward(player, Integer.valueOf(Premio[0]), Integer.valueOf(Premio[1]));
			opera.AnunciarTodos(String.valueOf(i), opera.getFormatNumbers(Premio[1]) + " " + central.getNombreITEMbyID(Integer.valueOf(Premio[0])));
			i++;
		}
		return true;
	}



	private static String checkPlayer(int IDNpc, L2PcInstance player){
		String retorno = "";

		Connection conn = null;
		String qry = "";
		CallableStatement psqry = null;
		ResultSet rss = null;

		String tipoError = "", descError = "",nomChar = "";

		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "call sp_npc_evento(3,"+ String.valueOf(IDNpc) +"," + String.valueOf(player.getObjectId()) +",-1)";
			psqry = conn.prepareCall(qry);
			rss = psqry.executeQuery();
			if(rss.next()){
				tipoError = rss.getString(1);
				descError= rss.getString(2);
				nomChar = rss.getString(3);
			}
		}catch(SQLException a){
			_log.warning(a.getMessage());
		}finally{
			
		}
		
		try{
			conn.close();
		}catch(Exception a){
			
		}

		if(tipoError.equals("err")){
			if(descError.equals("NPC")){
				retorno = central.LineaDivisora(2) + central.headFormat("".replace("$player", nomChar),"LEVEL") + central.LineaDivisora(2);
			}
			if(descError.equals("CHAR")){
				retorno = central.LineaDivisora(2) + central.headFormat("","LEVEL") + central.LineaDivisora(2);
			}
		}
		if(tipoError.equals("cor")){
			retorno = central.LineaDivisora(1) + central.LineaDivisora(2)
					+ central.headFormat(""+
					"<button value=\"Reward me\" action=\"bypass ZeuSNPC getPremio\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">")
					+ central.LineaDivisora(2) + central.LineaDivisora(1);
		}

		return retorno;
	}

}
