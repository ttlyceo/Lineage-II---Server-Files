package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import ZeuS.Config.general;

import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class RaidBossInfo {
	public static boolean goRaid(L2PcInstance player, String eventParam1, String eventParam2, String eventParam3){
		if(!general._activated()){
			return false;
		}
		if(general.RAIDBOSS_INFO_NOBLE && !player.isNoble()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
			return false;
		}
		if(general.RAIDBOSS_INFO_LVL >= player.getLevel()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.RAIDBOSS_INFO_LVL)), player);
			return false;
		}
		if(!opera.haveItem(player, general.RAIDBOSS_INFO_TELEPORT_PRICE)){
			return false;
		}

		player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);

		return true;
	}

	private static boolean canUseTeleportBtn(int IDMob){
		if(general.RAIDBOSS_ID_MOB_NO_TELEPORT.contains(IDMob)){
			return false;
		}
		return true;
	}

	public static String getInfo(L2PcInstance player, int Pagina){
		if(!general._activated()){
			return "";
		}
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Raid Boss Info") + central.LineaDivisora(2);

		MAIN_HTML += central.ItemNeedShowBox(general.RAIDBOSS_INFO_TELEPORT_PRICE, "Teleport to Raid Cost");

		String Consulta = "call sp_get_raidboss_info(1,"+String.valueOf(Pagina * general.RAIDBOSS_INFO_LISTA_X_HOJA)+","+ String.valueOf(general.RAIDBOSS_INFO_LISTA_X_HOJA) +")";
		Connection conn = null;
		String BTN_SIGUENTE = "";
		String BTN_ATRAS = "";
		String Temporal ="";
		if(Pagina>0){
			BTN_ATRAS = "<button value=\"<-\" action=\"bypass -h ZeuSNPC RaidBossInf "+String.valueOf(Pagina-1)+" 0 0\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}
		int Contador = 0;
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(Consulta);
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				Temporal = "<center>"+rss.getString(1)+"<br1>";
				if(rss.getString(3).equals("0")){
					Temporal += "<font color=99FF00>Alive</font>";
					if(general.RAIDBOSS_INFO_TELEPORT && canUseTeleportBtn(rss.getInt(2))){
						Temporal += "<br1><button value=\"GO\" action=\"bypass -h ZeuSNPC RaidBossInGo "+String.valueOf(rss.getInt(4))+" "+String.valueOf(rss.getInt(5))+" "+String.valueOf(rss.getInt(6))+" "+ String.valueOf(Pagina) +"\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
					}
				}else{
					Temporal += "<font color=CC0000>Death</font><br1>";
					Temporal += "Spawn: " +rss.getString(3) + "</center>";
				}
				MAIN_HTML += central.LineaDivisora(1) + central.headFormat(Temporal) + central.LineaDivisora(1);
				Contador++;
			}
		}catch (SQLException e) {
			e.printStackTrace();
		}finally{try{conn.close();}catch(SQLException a){}}

		if(Contador >= general.RAIDBOSS_INFO_LISTA_X_HOJA){
			BTN_SIGUENTE = "<button value=\"->\" action=\"bypass -h ZeuSNPC RaidBossInf "+String.valueOf(Pagina+1)+" 0 0\" width=85 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		String BOTONES = "<center><table width=170><tr><td width=85>"+BTN_ATRAS+"</td><td width=85>"+BTN_SIGUENTE+"</td></tr></table>";

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTONES) + central.LineaDivisora(2);
		MAIN_HTML += central.getPieHTML() + "</body></html>";

		return MAIN_HTML;
	}
}

