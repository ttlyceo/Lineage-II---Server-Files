package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.GameServer;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class C_gmlist {
	
	private static Map<Integer, HashMap<Integer, Integer>> PlayerTime = new HashMap<Integer, HashMap<Integer, Integer>>();
	private static Map<Integer, HashMap<Integer, Integer>> PlayerTry = new HashMap<Integer, HashMap<Integer, Integer>>();
	private static final Logger _log = Logger.getLogger(C_gmlist.class.getName());
	private static NpcHtmlMessage getGMList(L2PcInstance player, String ByPass, String GmID, NpcHtmlMessage html){
		
		String Status_ON = "<font color=6EDC4A>Online</font";
		String Status_OFF = "<font color=FE5858>Offline</font";
		
		String GridFormart = opera.getGridFormatFromHtml(html, 1, "%GM_DATA%");
		String GmListData = "";
		for(String prt : general.STAFF_DATA.split(":")){
			String idChar = prt.split(";")[0];
			String Funsion = prt.split(";")[1];
			String Nombre = opera.getPlayerNameById(Integer.valueOf(idChar));
			String Status = "";
			L2PcInstance cha = opera.getPlayerByID(Integer.valueOf(idChar));
			if(cha!=null){
				if(cha.isGM()){
					if(opera.isGmAllVisible(cha)){
						Status = Status_ON;
					}else{
						Status = Status_OFF;
					}
				}else{
					Status = Status_OFF;
				}
			}else{
				Status = Status_OFF;
			}
			
			String Bypass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.gmlist.name() + ";poke;" + idChar + ";0;0;0;0;0";
			
			GmListData += GridFormart.replace("%GMLIST_POKE_BYPASS%", Bypass) .replace("%GMLIST_GM_POSITION_IN_GAME%", Funsion).replace("%GMLIST_GM_STATUS%", Status) .replace("%GMLIST_GM_NAME%", Nombre);
		}
		
		
		html.replace("%GM_DATA%", GmListData);
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/gmlist.htm");
		
		if(Params.equals("0")){
			html = getGMList(player, "", "0", html);
		}
		return html.getHtml();
	}
	
	private static int getTry(L2PcInstance player,int IdGM, boolean erase){
		int Intentos = 1;
		if(PlayerTry!=null){
			if(PlayerTry.containsKey(player.getObjectId())){
				if(PlayerTry.get(player.getObjectId()).containsKey(IdGM)){
					if(erase){
						PlayerTry.get(player.getObjectId()).remove(IdGM);
						return 0;
					}
					Intentos = PlayerTry.get(player.getObjectId()).get(IdGM) + 1;					
				}
			}
		}
		
		if(!PlayerTry.containsKey(player.getObjectId())){
			PlayerTry.put(player.getObjectId(), new HashMap<Integer, Integer>());
		}
		
		PlayerTry.get(player.getObjectId()).put(IdGM, Intentos);
		return PlayerTry.get(player.getObjectId()).get(IdGM) ;
	}
	
	private static void pokeGM(L2PcInstance player, int idGM){
		int MinIntentos = 3;
		int Intentos = getTry(player,idGM,false);
		int MinutosBase = 3;
		int MinutosEspera = Intentos>MinIntentos ? Intentos * MinutosBase : MinutosBase ;
		L2PcInstance GM4Poke = opera.getPlayerbyID(idGM);
		if(GM4Poke!=null){
			if(GM4Poke.isGM()){
				boolean makePoke = false;
				if(PlayerTime!=null){
					int unixTimeNow = opera.getUnixTimeNow();
					int UnixTimeFromPlayer = 0;
					if(PlayerTime.containsKey(player.getObjectId())){
						if(PlayerTime.get(player.getObjectId()).containsKey(idGM)){
							UnixTimeFromPlayer = PlayerTime.get(player.getObjectId()).get(idGM);
							if(unixTimeNow < ( UnixTimeFromPlayer + ( MinutosEspera * 60 ) ) ){
								central.msgbox(language.getInstance().getMsg(player).WAIT_TO_SEND_ANOTHER_POKE.replace("$minutes", String.valueOf(MinutosEspera)), player);
								return;
							}
						}
					}
				}
				makePoke = true;
				if(makePoke){
					central.msgbox_Lado("EY! Wake up! " + player.getName() + " try to speak with you.", GM4Poke, "GM");
				}
			}
		}
		
		central.msgbox("You poke was send, but if GM/ADM need to be connected to answer.", player);
		getTry(player,idGM,true);
		
		if(!PlayerTime.containsKey(player.getObjectId())){
			PlayerTime.put(player.getObjectId(), new HashMap<Integer, Integer>());			
		}
		PlayerTime.get(player.getObjectId()).put(idGM, opera.getUnixTimeNow()) ;
	}
	
	
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		if(parm1.equals("0")){
			return mainHtml(player,parm1);
		}else if(parm1.equals("poke")){
			pokeGM(player, Integer.valueOf(parm2));
		}
		return "";
	}
	
}
