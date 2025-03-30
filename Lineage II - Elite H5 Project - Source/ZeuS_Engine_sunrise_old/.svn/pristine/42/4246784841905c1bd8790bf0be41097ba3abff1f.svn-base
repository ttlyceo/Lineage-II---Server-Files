package ZeuS.Comunidad;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.gameserver.data.xml.impl.AdminData;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;
import ZeuS.interfase.sellBuff;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;


public class Region {
	private static final int Columnas = 5;
	private static Map<Integer, Boolean> ShowAllGlobePlayer = new HashMap<Integer, Boolean>();
	private final static Logger _log = Logger.getLogger(Region.class.getName());
	private static String COLOR_GM = "";
	private static String COLOR_DEATH = "";
	private static String COLOR_HERO = "";
	private static String COLOR_CLAN_LEADER = "";
	private static String COLOR_NORMAL = "";
	private static String COLOR_FLAG = "";
	private static String COLOR_PRIVATE_STORE = "";
	private static String COLOR_BUFF_STORE = "";
	private static String COLOR_KARMA = "";
	private static String COLOR_JAILED = "";

	public static void loadColors(){
		Properties propZ = new Properties();
		try{
			propZ.load(new FileInputStream(new File("./config/zeus/zeus_color_region_setting.properties")));
			COLOR_GM = propZ.getProperty("GM_COLOR");
			COLOR_DEATH = propZ.getProperty("DEATH_COLOR");
			COLOR_HERO = propZ.getProperty("HERO_COLOR");
			COLOR_CLAN_LEADER = propZ.getProperty("CLAN_LEADER_COLOR");
			COLOR_NORMAL = propZ.getProperty("NORMAL_PLAYER_COLOR");
			COLOR_FLAG = propZ.getProperty("FLAG_COLOR");
			COLOR_PRIVATE_STORE = propZ.getProperty("PRIVATE_STORE_COLOR");
			COLOR_BUFF_STORE = propZ.getProperty("BUFF_STORE_COLOR");
			COLOR_KARMA = propZ.getProperty("KARMA_COLOR");
			COLOR_JAILED = propZ.getProperty("JAILED_COLOR");
		}catch(Exception a){
			_log.warning("Error loading Color for Region Community");
		}
	}
	
	private static boolean canAddToList(L2PcInstance player){
		if(!player.isGM()){
			return true;
		}
		return opera.isGmAllVisible(player);
	}

	private static String getServerInfo(int paginaVer, L2PcInstance playerOnCB){
		final NpcHtmlMessage html = comun.htmlMaker(playerOnCB, general.DIR_HTML + language.getInstance().getFolder(playerOnCB) + "/communityboard/region.htm");
		boolean isGm = playerOnCB.isGM();
		int onlinePlayer = L2World.getInstance().getAllPlayersCount();
		int onlineGM = AdminData.getInstance().getAllGms(isGm).size();
		int onlineGMInvi = AdminData.getInstance().getAllGms(true).size();

		String Mirando = "";

		if(ShowAllGlobePlayer.get(playerOnCB.getObjectId())){
			Mirando = "ALL";
		}else{
			Mirando = "REGION";
		}

		if(!playerOnCB.isGM()){
			onlinePlayer = onlinePlayer - onlineGMInvi;
		}

		String btnNext = "<button value=\"Next\" width=60 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC() + ";"+Mirando+";"+ String.valueOf(paginaVer+1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";
		String btnPrevi = "<button value=\"Prev.\" width=60 height=23 action=\"bypass " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC() + ";"+Mirando+";"+ String.valueOf(paginaVer-1) +";0;0;0\" back=L2UI_ct1.button_df fore=L2UI_ct1.button_df>";


		int onlineShop = 0;
		int onlineInThisRegion = 0;
		int contador = 0;
		int desde = paginaVer * general.COMMUNITY_BOARD_REGION_PLAYER_ON_LIST;
		int hasta = desde + general.COMMUNITY_BOARD_REGION_PLAYER_ON_LIST;
		int contadorMini = 0;
		boolean haveNext = false;
		String GrillaPJ = "<tr>";

		if(ShowAllGlobePlayer.get(playerOnCB.getObjectId())){
			Vector<String> PlayerNameOnlineWorld = opera.getAllNamePlayerOnWorld();
			Collections.sort(PlayerNameOnlineWorld);
			for(String playerName : PlayerNameOnlineWorld){
				L2PcInstance player = opera.getPlayerbyName(playerName);
				if(player!=null){
					if((player.isGM() && opera.isGmAllVisible(player,playerOnCB)) || !player.isGM()){
						if(player.isInCraftMode() || player.isInStoreMode() || player.getClient().isDetached()){
							onlineShop++;
						}
						if(opera.isInGeoZone(player, playerOnCB)){
							onlineInThisRegion++;
						}
						if(!haveNext){
							if(canAddToList(player) || playerOnCB.isGM()){
								if(contador >= desde){
									if(contador < hasta){
										if(contadorMini == Columnas){
											GrillaPJ += "</tr><tr>";
											contadorMini=0;
										}
										GrillaPJ += "<td width=70 align=CENTER><font color="+ getColorForPlayer(player) +"><a action=\"bypass " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC() + ";CHARINFO;"+ String.valueOf(paginaVer) +";"+ player.getName() +";0;0\">"+ player.getName() +"</a></font></td>";
										contadorMini++;
									}else if(contador>=hasta){
										haveNext = true;
									}
								}
								contador++;
							}
						}
					}
				}
			}
		}else{
			Vector<String> PlayerNameOnlineRegion = opera.getAllNamePlayerOnRegion(playerOnCB,true);
			Collections.sort(PlayerNameOnlineRegion);
			for(String playerName : PlayerNameOnlineRegion){
				L2PcInstance player = opera.getPlayerbyName(playerName);
				if(player!=null){
					if((player.isGM() && opera.isGmAllVisible(player)) || !player.isGM()){
						if(player.isInCraftMode() || player.isInStoreMode() || player.getClient().isDetached()){
							onlineShop++;
						}
						if(opera.isInGeoZone(player, playerOnCB)){
							onlineInThisRegion++;
						}
						if(canAddToList(player) || playerOnCB.isGM()){
							if(contador >= desde){
								if(contador < hasta){
									if(contadorMini == Columnas){
										GrillaPJ += "</tr><tr>";
										contadorMini=0;
									}
									GrillaPJ += "<td width=70 align=CENTER><font color="+ getColorForPlayer(player) +"><a action=\"bypass " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC() + ";CHARINFO;"+ String.valueOf(paginaVer) +";"+ player.getName() +";0;0\">"+ player.getName() +"</a></font></td>";
									contadorMini++;
								}else if(contador>=hasta){
									haveNext = true;
								}
							}
							contador++;
						}
					}
				}
			}
		}

		if(contadorMini<5){
			for(int i=contadorMini;i<=5;i++){
				GrillaPJ += "<td width=70></td>";
			}
		}

		if(!GrillaPJ.endsWith("</tr>")){
			GrillaPJ += "</tr>";
		}

		GrillaPJ = "<table width=750 align=CENTER>" + GrillaPJ + "</table>";
		
		String btnAllRegion = "bypass " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC() + ";ALL;0;0;0;0";
		String btnThisRegion = "bypass " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC() + ";REGION;0;0;0;0";		
		
		html.replace("%BYPASS_ALL_WORLD%", btnAllRegion);
		html.replace("%BYPASS_THIS_ZONE%", btnThisRegion);
		html.replace("%COLOR_GM%", COLOR_GM);
		html.replace("%COLOR_DEATH%", COLOR_DEATH);
		html.replace("%COLOR_HERO%", COLOR_HERO);
		html.replace("%COLOR_CLAN_LEADER%", COLOR_CLAN_LEADER);
		html.replace("%COLOR_NORMAL%", COLOR_NORMAL);
		html.replace("%COLOR_FLAG%", COLOR_FLAG);
		html.replace("%COLOR_PRIVATE_STORE%", COLOR_PRIVATE_STORE);
		html.replace("%COLOR_BUFF_STORE%", COLOR_BUFF_STORE);
		html.replace("%COLOR_KARMA%", COLOR_KARMA);
		html.replace("%COLOR_JAILED%", COLOR_JAILED);
		
		html.replace("%INFO_PLAYER%", GrillaPJ);
		html.replace("%PLAYER_ONLINE%", String.valueOf(onlinePlayer));
		html.replace("%GM_ONLINE%", String.valueOf(onlineGM));
		html.replace("%OFFLINERS%", String.valueOf(onlineShop));
		html.replace("%AREA%", String.valueOf(onlineInThisRegion));
		html.replace("%ONLINE%", String.valueOf( opera.getTiempoON(opera.getUnixTimeNow() - general.ServerStartUnixTime) ));
		html.replace("%LINK_PRE%", (paginaVer > 0 ? btnPrevi : ""));
		html.replace("%PAGE%", String.valueOf(paginaVer + 1));
		html.replace("%LINK_NEXT%", ( haveNext ? btnNext : "" ));

		return html.getHtml();
	}

	private static String getColorForPlayer(L2PcInstance player){
		if(sellBuff.isBuffSeller(player)){
			return COLOR_BUFF_STORE;
		}else if(player.isInStoreMode() || player.isInCraftMode()){
			return COLOR_PRIVATE_STORE;
		}else if(player.getKarma()>0){
			return COLOR_KARMA;
		}else if(player.isJailed()){
			return COLOR_JAILED;
		}else if(player.getPvpFlag()>0){
			return COLOR_FLAG;
		}else if(player.isDead()){
			return COLOR_DEATH;
		}else if(player.isGM()){
			return COLOR_GM;
		}else if(player.isHero()){
			return COLOR_HERO;
		}
		return COLOR_NORMAL;
	}	
	
	public static String getRegion(L2PcInstance player, String Accion, int Pagina){

		if(ShowAllGlobePlayer==null){
			ShowAllGlobePlayer.put(player.getObjectId(), true);
		}else{
			if(!ShowAllGlobePlayer.containsKey(player.getObjectId())){
				ShowAllGlobePlayer.put(player.getObjectId(), true);
			}
		}

		if(Accion!=null){
			if(Accion.length()>0){
				String[]param = Accion.split(";");
				if(param.length>1){
					if(param[1].equals("REGION")){
						ShowAllGlobePlayer.put(player.getObjectId(), false);
					}else if(param[1].equals("ALL")){
						ShowAllGlobePlayer.put(player.getObjectId(), true);
					}else if(param[1].equals("CHARINFO")){
						opera.getInfoPlayer(param[3], player);
					}
					Pagina = Integer.valueOf(param[2]);
				}
			}
		}

		String parte = ShowAllGlobePlayer.get(player.getObjectId()) ? "World View" : "Region View" ;
		String retorno = getServerInfo(Pagina,player).replace("%VIEW%", parte);
		return retorno;
	}

}
