package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.instancemanager.PunishmentManager;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.punishment.PunishmentAffect;
import l2r.gameserver.model.punishment.PunishmentType;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class fixMe {

	private static final Logger _log = Logger.getLogger(fixMe.class.getName());

	public static void delegar(L2PcInstance player, String params){
		if(params == null){
			getShowWindowsFix(player);
		}else{
			if(params.length()==0){
				getShowWindowsFix(player);
			}else{
				fixIt(params,player);
			}
		}
	}

	@SuppressWarnings("unused")
	private static int getCountItemINBD(String NombreChar){
		int retorno = 0;
		String sqlConsulta = "select count(*) from items where items.owner_id = (select characters.charId from characters where characters.char_name = ?)";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(sqlConsulta);
			psqry.setString(1, NombreChar);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			retorno = rss.getInt(0);
			conn.close();
		}catch(SQLException e){

		}
		try{
			conn.close();
		}catch(Exception a){

		}
		return retorno;
	}

	private static void fixIt(String NomChar, L2PcInstance player){
		String sqlUpdateLocation = "update characters set characters.x=17527, characters.y=170013, characters.z=-3504 where characters.char_name = ?";
		String sqlUpdateInventario_1 = "update items set items.loc='WAREHOUSE' where items.loc='PAPERDOLL' and items.owner_id = (select characters.charId from characters where characters.char_name = ?)";
		String sqlUpdateInventario_2 = "update items set items.loc='WAREHOUSE' where items.count >= 1000 and items.owner_id = (select characters.charId from characters where characters.char_name = ?)";
		String sqlRemoveSC = "DELETE FROM character_shortcuts WHERE charId= (select characters.charId from characters where characters.char_name = ?)";
		
		
		central.msgbox("Moving "+NomChar+" to a save zone", player);
		if(!ejecSQL(NomChar, sqlUpdateLocation)){
			central.msgbox("Moving "+NomChar+" was wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Moving "+NomChar+" Done!", player);
		}

		central.msgbox("Moving the used clothes to his warehouse", player);
		if(!ejecSQL(NomChar, sqlUpdateInventario_1)){
			central.msgbox("Moving "+NomChar+" clothes wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Moving "+NomChar+" clothes Done!", player);
		}

		central.msgbox("Moving ramdon item to his warehouse", player);
		if(!ejecSQL(NomChar, sqlUpdateInventario_2)){
			central.msgbox("Moving "+NomChar+" ramdon item's wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Moving "+NomChar+" ramdon item's Done!", player);
		}

		central.msgbox("Removing Shorcuts.", player);
		if(!ejecSQL(NomChar, sqlRemoveSC)){
			central.msgbox("Removing "+NomChar+" Short Cuts wrog, please contact the GM/ADM if you char " + NomChar + " it not right", player);
		}else{
			central.msgbox("Removing "+NomChar+" Short Cuts's Done!", player);
		}
		
		ejecSQL(NomChar, "DELETE FROM character_ui_actions WHERE charId=(select characters.charId from characters where characters.char_name = ?)");
		ejecSQL(NomChar, "DELETE FROM character_ui_categories WHERE charId=(select characters.charId from characters where characters.char_name = ?)");
		
		central.msgbox("Please try to enter you char.", player);

	}

	private static boolean ejecSQL(String NomChar, String strSql){
		Connection conn = null;
		PreparedStatement psqry = null;
		boolean retorno = false;
		try{
			conn  = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strSql);
			psqry.setString(1, NomChar);
			psqry.executeUpdate();
			retorno = true;
		}catch(SQLException e){
			_log.warning("ZeuS fix Char " + NomChar + " error: " + e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){

		}
		return retorno;
	}

	@SuppressWarnings({ "unused", "rawtypes" })
	private static void getShowWindowsFix(L2PcInstance player){
		String imgLogo = "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + String.valueOf(general.ID_LOGO_SERVER) + "\" width=256 height=64>";
		NpcHtmlMessage html = null;
		
		String fixmeURL_NoChar = "fixme_noaccount.html";
		String fixmeURL = "fixme.html";
		Map<Integer, String> allPlayer = opera.getAllPlayerOnThisAccount(player);
		String playerToCombo = "";
		String btnDoIt = "<button value=\"Fix\" action=\"bypass -h voice .fixmeCharName $cmbCharacters\" width=75 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnClose = "<button value=\"Cancel\" action=\"bypass -h voice .closedlg\" width=\"75\" height=\"26\" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		Iterator itr = allPlayer.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	
	    	String NombreChar = (String)Entrada.getValue();
	    	int idPlayer = (int)Entrada.getKey();
	    	
	    	L2PcInstance ppl = L2World.getInstance().getPlayer(NombreChar);
	    	
	    	final PunishmentAffect affect = PunishmentAffect.getByName("CHARACTER");
			final PunishmentType type = PunishmentType.getByName("JAIL");
			
			if (PunishmentManager.getInstance().hasPunishment(idPlayer, affect, type))
			{
				continue;
			}
	    	
			if(playerToCombo.length()>0){
				playerToCombo += ";";
			}
			playerToCombo += Entrada.getValue();
	    }
	   
	    if(playerToCombo.length()>0){
	    	html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/" + fixmeURL);
	    	html.replace("%BTN_FIX%", btnDoIt);
	    	html.replace("%CHAR_LIST%", playerToCombo);
	    }else{
	    	html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/" + fixmeURL_NoChar);
	    	html.replace("%BTN_FIX%", "");
	    }
	    html.replace("%SERVER_LOGO%", imgLogo);
	    
		opera.enviarHTML(player, html.getHtml());
	}
}
