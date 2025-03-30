package ZeuS.Comunidad.EngineForm;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

import java.util.HashMap;
import java.util.Map;

import ZeuS.Config.general;
import ZeuS.Config.premiumsystem;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class v_MyInfo {
	private static String CrearCharInformation(L2PcInstance player){
		String PremiumCharInicio = "NONE", PremiumCharTermino = "NONE", NombrePremium = "NONE";
		String PremiumClanInicio = "NONE", PremiumClanTermino = "NONE", NombreClanPremium = "NONE";
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/MyInfo.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_PART_EXEC());
		if(opera.isPremium_Player(player)){
			premiumsystem Ps = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getPremiumData();
			NombrePremium = Ps.getName();
			int Inicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
			int Termino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd();
			PremiumCharInicio = opera.getDateFromUnixTime(Inicio);
			PremiumCharTermino = opera.getDateFromUnixTime(Termino);			
		}
		
		if(opera.isPremium_Clan(player)){
			int idClan = player.getClan().getId();
			premiumsystem Ps = general.getPremiumDataFromPlayerOrClan(idClan).getPremiumData();
			NombreClanPremium = Ps.getName();
			int Inicio = general.getPremiumDataFromPlayerOrClan(idClan).getBegin();
			int Termino = general.getPremiumDataFromPlayerOrClan(idClan).getEnd();
			PremiumClanInicio = opera.getDateFromUnixTime(Inicio);
			PremiumClanTermino = opera.getDateFromUnixTime(Termino);			
		}
		
		if(general.getPremiumDataFromPlayerOrClan(player.getAccountName()) !=null){
			premiumsystem PS = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getPremiumData();
			NombrePremium = PS.getName();
			int Inicio = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getBegin();
			int Termino = general.getPremiumDataFromPlayerOrClan(player.getAccountName()).getEnd();
			PremiumCharInicio = opera.getDateFromUnixTime(Inicio);
			PremiumCharTermino = opera.getDateFromUnixTime(Termino);
		}
		html.replace("%CHAR_NAME%", player.getName());
		html.replace("%CHAR%", player.getName());
		html.replace("%PROFESION%", central.getClassName(player));
		html.replace("%LEVEL%", String.valueOf(player.getLevel()));
		html.replace("%CLAN%", ( player.getClan() !=null ? player.getClan().getName() : "NO CLAN" ));
		html.replace("%ONLINE%", general.getLifeToday(player));
		html.replace("%LIFETIME%", opera.getTiempoON(player.getOnlineTime()));
		html.replace("%IPLAN%", ZeuS.getIp_pc(player));
		html.replace("%IPWAN%", ZeuS.getIp_Wan(player));
		html.replace("%VIPACCOUNT%", opera.isPremium_Player(player) ? "YES" : "NO" );
		html.replace("%VIPACCOUNT_BEGIN%", PremiumCharInicio);
		html.replace("%VIPACCOUNT_END%", PremiumCharTermino) ;
		html.replace("%VIPACCOUNT_NAME%", NombrePremium ) ;
		html.replace("%VIPACCOUNT_CLAN%", opera.isPremium_Clan(player) ? "YES" : "NO" );
		html.replace("%VIPACCOUNT_BEGIN_CLAN%", PremiumClanInicio);
		html.replace("%VIPACCOUNT_END_CLAN%", PremiumClanTermino) ;
		html.replace("%VIPACCOUNT_NAME_CLAN%", NombreClanPremium ) ;
			

		String Colores[] = {"73B9B3","73B98A"};
		String formatLast = opera.getGridFormatFromHtml(html, 1, "%LAST_CONNECTION%");
		if(general.getLastConnections(player)!=null){
			Map<Integer, HashMap<String, String>>LastCon = general.getLastConnections(player);

			boolean Continuar = true;
			
			String strLastConnection = "";
			
			int Contador = 1;
			
			while(Continuar){
				if(LastCon.containsKey(Contador)){
					strLastConnection += formatLast.replace("%LAST_DATE%", LastCon.get(Contador).get("DATE")) .replace("%LAST_LAN%", LastCon.get(Contador).get("LAN")).replace("%LAST_WAN%", LastCon.get(Contador).get("WAN")).replace("%FONT_COLOR%", Colores[Contador%2]);
				}else{
					Continuar = false;
				}
				Contador++;
			}
			html.replace("%LAST_CONNECTION%", strLastConnection);
		}else{
			html.replace("%LAST_CONNECTION%", "");
		}

		return html.getHtml();
	}
	
	
	
	private static String mainHtml(L2PcInstance player, String Params, int idCategoria, String ByPassAnterior){
		return CrearCharInformation(player);
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"FromMain",0,"bypass "+general.getCOMMUNITY_BOARD_PART_EXEC());
		}
		return "";
	}
	
	
	
}
