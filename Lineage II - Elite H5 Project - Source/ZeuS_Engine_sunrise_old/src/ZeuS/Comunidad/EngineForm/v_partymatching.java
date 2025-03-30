package ZeuS.Comunidad.EngineForm;

import gr.sr.interf.SunriseEvents;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.sendC;
import ZeuS.Config.general;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.enums.PartyDistributionType;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_partymatching {
	
	private static final Logger _log = Logger.getLogger(v_partymatching.class.getName());
	private static Map<Integer,Vector<String>> NombresListados = new HashMap<Integer, Vector<String>>();
	private static Map<Integer, HashMap<String, L2PcInstance>> NombreListadosConInstance = new HashMap<Integer, HashMap<String, L2PcInstance>>();
	
	private static HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>> REQUEST_PARTY = new HashMap<Integer, HashMap<Integer, HashMap<String, Integer>>>();
	
	private static Map<Integer, Boolean>CerrarVentana = new HashMap<Integer, Boolean>();
	private static Map<Integer, Integer>ID_LASTR = new HashMap<Integer, Integer>();
	
	private static int SegundoEspera = 80;
	
	private static Integer[] ClassBuffer = {136,135,116,115,112,105,98,52,51,43,30,17};
	private static Integer[] ClassBladeDancer = {107,34};
	private static Integer[] ClassSwordMuse = {100,21};
	private static Integer[] Healers = {136,135,115,112,105,97,51,43,30,16};
	private static Integer[] Tanks = {106,99,91,90,32,20,6,4,5};
	private static Integer[] MageDamage = {133,132,129,128,126,111,110,104,103,96,94,95,40,41,39,28,27,13,12,11};
	private static Integer[] FighterDamege = {106,2,3,9,8,7,14,22,24,23131,134,130,127,125,118,117,114,113,109,108,107,101,102,92,93,91,89,88,53,54,55,56,57,48,47,46,45,44,37,36,35,33,18,6};
	
	private static Map<Integer,Integer[]> TipoBusqueda = new HashMap<Integer, Integer[]>();
	
	public static void setTipoBusquedas(){
		TipoBusqueda.put(0, null);
		TipoBusqueda.put(1, ClassBuffer);
		TipoBusqueda.put(2, ClassBladeDancer);
		TipoBusqueda.put(3, ClassSwordMuse);
		TipoBusqueda.put(4, Healers);
		TipoBusqueda.put(5, Tanks);
		TipoBusqueda.put(6, MageDamage);
		TipoBusqueda.put(7, FighterDamege);
	}
	
	private static boolean canUse(L2PcInstance playerReceptor, L2PcInstance playerEmite){
		if(!playerReceptor.isOnline()){
			central.msgbox(language.getInstance().getMsg(playerEmite).PLAYER_IS_NOT_ONLINE, playerEmite);
			return false;
		}
		
		if(playerReceptor.isJailed()){
			 central.msgbox(language.getInstance().getMsg(playerEmite).PLAYER_IS_IN_JAIL, playerEmite);
			 return false;
		}

		if(playerEmite.isJailed()){
			 central.msgbox(language.getInstance().getMsg(playerEmite).I_IN_JAIL, playerEmite);
			 return false;
		}
		
		if(playerEmite == playerReceptor){
			return false;
		}
		
		if(playerReceptor.getBlockList().isInBlockList(playerEmite)){
			central.msgbox(language.getInstance().getMsg(playerEmite).PLAYER_HAS_YOU_BLOCK_LIST, playerEmite);
			return false;
		}
		
		if(playerReceptor.isInDuel()){
			central.msgbox(language.getInstance().getMsg(playerEmite).PLAYER_IS_IN_COMBAT_DUEL, playerEmite);
			return false;
		}
		
		if(playerReceptor.isInOlympiadMode() || playerEmite.isInOlympiadMode()){
			central.msgbox(language.getInstance().getMsg(playerEmite).YOU_OR_TARGET_IS_IN_OLY, playerEmite);
			return false;
		}
		
		if(playerReceptor.isInBoat() || playerEmite.isInBoat()){
			central.msgbox(language.getInstance().getMsg(playerEmite).YOU_OR_TARGET_IS_IN_BOAT, playerEmite);
			return false;
		}
		
		if(playerReceptor.inObserverMode() || playerEmite.inObserverMode()){
			central.msgbox(language.getInstance().getMsg(playerEmite).YOU_OR_TARGET_IS_IN_OLY_OBSERVER, playerEmite);
			return false;
		}
		
		if(playerReceptor.isOnEvent() || playerEmite.isOnEvent()){
			central.msgbox(language.getInstance().getMsg(playerEmite).YOU_OR_TARGET_IS_IN_EVENT, playerEmite);
			return false;
		}
		
		if(SunriseEvents.isInEvent(playerReceptor) || SunriseEvents.isInEvent(playerEmite)){
			central.msgbox(language.getInstance().getMsg(playerEmite).YOU_OR_TARGET_IS_IN_EVENT, playerEmite);
			return false;
		}
		
		if(RaidBossEvent.isPlayerOnRBEvent(playerReceptor) || RaidBossEvent.isPlayerOnRBEvent(playerEmite)){
			central.msgbox(language.getInstance().getMsg(playerEmite).YOU_OR_TARGET_IS_IN_EVENT, playerEmite);
			return false;
		}
		
		if(playerEmite.isInParty()){
			if(!playerEmite.getParty().isLeader(playerEmite)){
				return false;
			}
		}
		
		if(playerReceptor.isInParty()){
			if(!playerReceptor.getParty().isLeader(playerReceptor)){
				central.msgbox(language.getInstance().getMsg(playerEmite).TARGET_IS_NOT_PARTY_LEADER, playerEmite);
				return false;
			}
			if(playerReceptor.getParty().getMemberCount()==9){
				central.msgbox(language.getInstance().getMsg(playerEmite).FP_NO_MORE_SLOT, playerEmite);
				return false;
			}
		}
		
		
		
		return true;
	}
	
	private static boolean setNewRequest(L2PcInstance playerEmite, L2PcInstance playerReceptor){
		cleanRequest(playerReceptor);
		
		if(!canUse(playerReceptor,playerEmite)){
			return false;
		}
		
		if(REQUEST_PARTY==null){
			REQUEST_PARTY.put(playerReceptor.getObjectId(), new HashMap<Integer, HashMap<String, Integer>>());
		}else{
			if(!REQUEST_PARTY.containsKey(playerReceptor.getObjectId())){
				REQUEST_PARTY.put(playerReceptor.getObjectId(), new HashMap<Integer, HashMap<String, Integer>>());				
			}
		}
		if(REQUEST_PARTY.get(playerReceptor.getObjectId()).containsKey(playerEmite.getObjectId())){
			central.msgbox("You Need to Wait the answer.", playerEmite);
			return false;
		}else{
			REQUEST_PARTY.get(playerReceptor.getObjectId()).put(playerEmite.getObjectId(), new HashMap<String, Integer>());
			REQUEST_PARTY.get(playerReceptor.getObjectId()).get(playerEmite.getObjectId()).put("TIME", opera.getUnixTimeNow());
		}
		return true;
	}
	
	private static void cerrarWindows(L2PcInstance player){
		try{
			if(CerrarVentana!=null){
				if(CerrarVentana.containsKey(player.getObjectId())){
					if(!CerrarVentana.get(player.getObjectId())){
						return;
					}
				}
			}
			player.sendPacket(new sendC());
		}catch(Exception a){
			
		}
	}
	
	
	private static void sendRequest(L2PcInstance pplEmite, int IDpplReceptor,boolean automatic){
		sendRequest(pplEmite,IDpplReceptor,automatic,-1);
	}
	
	@SuppressWarnings("rawtypes")
	private static void sendRequest(L2PcInstance pplEmite, int IDpplReceptor,boolean automatic, int Id_To_Close){
		L2PcInstance pplReceptor = null;
		if(opera.isCharInGame(IDpplReceptor)){
			pplReceptor = opera.getPlayerbyID(IDpplReceptor);
		}else{
			return;
		}

		if(pplEmite!=null){
			if(!automatic){
				if(general.getCharConfigPartyMatching(pplReceptor)){
					central.msgbox("Player " + pplReceptor.getName() + " is on Party Matching Refusal.", pplEmite);
					return;
				}
				
				if(!setNewRequest(pplEmite, pplReceptor)){
					central.msgbox(language.getInstance().getMsg(pplEmite).FP_YOU_CANT_SEND_PARTY_REQUEST_NOW, pplEmite);
					//central.msgbox(language.getInstance().getMsg(pplEmite).FP_REQUEST_SENDING_TO_PLAYER_YOU_NEED_TO_WAIT.replace("$player", pplReceptor.getName()), pplEmite);
					return;
				}
			}else{
				try{
					if(REQUEST_PARTY!=null){
						if(!REQUEST_PARTY.containsKey(pplReceptor.getObjectId())){
							cerrarWindows(pplReceptor);
							return;
						}else{
							if(REQUEST_PARTY.get(pplReceptor.getObjectId()).size()<=0){
								cerrarWindows(pplReceptor);
								return;
							}
						}
					}else{
						cerrarWindows(pplReceptor);
						return;
					}
				}catch(Exception a){
					cerrarWindows(pplReceptor);
				}
				try{
					if(pplEmite!=null){
						if(pplEmite.isOnline()){
							central.msgbox(language.getInstance().getMsg(pplEmite).FP_TIME_OUT , pplEmite);
						}
					}
				}catch(Exception a){
					
				}
			}
		}
		
		NpcHtmlMessage html = comun.htmlMaker(pplReceptor, general.DIR_HTML + "/" + language.getInstance().getFolder(pplReceptor) + "/communityboard/engine-party-matching-invitation-windows.htm");
		String ByPassAceptar = "link zeusC;" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";accept;%IDCHAR%;0;0;0;0;0";
		String ByPassRefuse = "link zeusC;" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";refuse;%IDCHAR%;0;0;0;0;0";

		
		String Grilla = opera.getGridFormatFromHtml(html, 1, "%PARTY_DATA%");
		Grilla = Grilla.replace("%BYPASS_ACCESS_INDI%", ByPassAceptar).replace("%BYPASS_REFUSE_INDI%", ByPassRefuse);
		
		String DataGrid = "";
		/*"<table width=280 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=0 cellpadding=2><tr><td fixwidth=3></td><td fixwidth=32>"+
        "<img src=\"%IDIMA%\" width=32 height=32><br><br><br><br></td><td fixwidth=240>"+
        "<font color=09FF95>%NOMBRE%</font> <font color=626262>Lv %LEVEL%</font><br1><font color=677C97>%CLASS%</font><font color=626262>  Sec. Decide %SECONDS%</font<table width=240><tr><td fixwidth=120 align=right>"+
        "<button value=\"Acept\" width=55 height=20 action=\""+ ByPassAceptar +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td><td fixwidth=120>"+
        "<button value=\"Refuse\" width=55 height=20 action=\""+ ByPassRefuse +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td></tr></table></td></tr></table>";
		*/
		Iterator itr = REQUEST_PARTY.get(pplReceptor.getObjectId()).entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry infoR = (Map.Entry)itr.next();
			int IdChar = (int) infoR.getKey();
			if(opera.isCharInGame(IdChar)){
				L2PcInstance pplB = opera.getPlayerbyID(IdChar);
				String Nombre = pplB.getName();
				String Clase = opera.getClassName(pplB.getClassId().getId());
				String Level = String.valueOf(pplB.getLevel());
				String ImagenID = general.getClassInfo(pplB.getClassId().getId()).getLogo();
				int UnixRequest = REQUEST_PARTY.get(pplReceptor.getObjectId()).get(IdChar).get("TIME") + SegundoEspera;
				int UnixAhora = opera.getUnixTimeNow();
				int SegundosFaltan = UnixRequest - UnixAhora;
				DataGrid += Grilla.replace("%IDCHAR%", String.valueOf(IdChar)) .replace("%SECONDS%", String.valueOf(SegundosFaltan)).replace("%IDIMA%", ImagenID).replace("%NOMBRE%", Nombre).replace("%LEVEL%", Level).replace("%CLASS%", Clase);
			}
		}
		
		html.replace("%PARTY_DATA%", DataGrid);
		
		String ByPas_RefuseAll = "link zeusC;" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";refuseAll;0;0;0;0;0";;
		String ByPass_Disable_P_Matching = "link zeusC;" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";disabled;0;0;0;0;0";;
		
		html.replace("%BYPASS_REFUSE_ALL%", ByPas_RefuseAll);
		html.replace("%BYPASS_DISABLE_PARTY_MATCHING%", ByPass_Disable_P_Matching);

		central.sendHtml(pplReceptor, html.getHtml(), true);
		CerrarVentana.put(pplReceptor.getObjectId(), true);
		int LastR = opera.getUnixTimeNow();
		ID_LASTR.put(pplReceptor.getObjectId(), LastR);
		if(!automatic && pplEmite!=null){
			ThreadPoolManager.getInstance().scheduleGeneral(new PMTimerPlayer(pplReceptor,pplEmite,LastR), SegundoEspera  * 1000);
			central.msgbox(			language.getInstance().getMsg(pplEmite).FP_REQUEST_SENDING_TO_PLAYER_YOU_NEED_TO_WAIT.replace("$player", pplReceptor.getName()),pplEmite);
		}
		
	}
	
	@SuppressWarnings("rawtypes")
	private static void cleanRequest(L2PcInstance playerReceptor){
		if(REQUEST_PARTY!=null){
			if(REQUEST_PARTY.containsKey(playerReceptor.getObjectId())){
				Iterator itr = REQUEST_PARTY.get(playerReceptor.getObjectId()).entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry InfoRequest = (Map.Entry)itr.next();
					int IdChar = (int) InfoRequest.getKey();
					if(opera.isCharInGame(IdChar)){
						int InicioUnix = REQUEST_PARTY.get(playerReceptor.getObjectId()).get(IdChar).get("TIME");
						int ActualUnix = opera.getUnixTimeNow();
						if((InicioUnix + SegundoEspera) <= ActualUnix){
							REQUEST_PARTY.get(playerReceptor.getObjectId()).remove(IdChar);
						}
					}else{
						REQUEST_PARTY.get(playerReceptor.getObjectId()).remove(IdChar);
					}
				}
			}
		}
	}
	
	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	private static void cleanRequestAll(){
		Iterator itr = REQUEST_PARTY.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Infore =(Map.Entry)itr.next();
			int IdChar = (int) Infore.getKey();
			if(opera.isCharInGame(IdChar)){
				HashMap<Integer, HashMap<String, Integer>> dc = (HashMap<Integer, HashMap<String, Integer>>) Infore.getValue();
				Iterator itr2 = dc.entrySet().iterator();
				while(itr2.hasNext()){
					Map.Entry ppl = (Map.Entry)itr2.next();
					int idSolicitante = (int) ppl.getKey();
					if(opera.isCharInGame(idSolicitante)){
						int InicioUnix = REQUEST_PARTY.get(IdChar).get(idSolicitante).get("TIME");
						int ActualUnix = opera.getUnixTimeNow();
						if((InicioUnix + SegundoEspera) > ActualUnix){
							REQUEST_PARTY.get(IdChar).remove(idSolicitante);		
						}						
					}else{
						REQUEST_PARTY.get(IdChar).remove(idSolicitante);
					}
				}
			}else{
				REQUEST_PARTY.remove(IdChar);
			}
		}		
	}
	
	
	@SuppressWarnings("unused")
	private static String getMenuFiltro(int idFiltro){
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name() + ";cargarfiltro;%IDFILTRO%;0;0;0;0";
		String btnCheckActivo = "<img src=\"L2UI.CheckBox_checked\" width=16 height=16><br>";
		String btnCheckEsperando= " <button width=16 height=16 action=\""+ ByPass +"\" back=L2UI.CheckBox_unable fore=L2UI.CheckBox_unable>";
		
		String Retorno = "<center><table width=100 background=L2UI_CT1.Windows_DF_TooltipBG border=0><tr><td><table width=30 border=0><tr><td fixwidth=16>"+
        (idFiltro==0 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "0") ) + "</td><td fixwidth=54>"+
        "<font color=EAFF86>All</font><br></td></tr></table></td><td><table width=55 border=0><tr><td fixwidth=16>"+
        (idFiltro==1 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "1")) + "</td><td fixwidth=55>"+
        "<font color=EAFF86>Buffer</font><br></td></tr></table></td><td><table width=50 border=0><tr><td fixwidth=16>"+
        (idFiltro==2 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "2")) + "</td><td fixwidth=30>"+
        "<font color=EAFF86>BDs</font><br></td></tr></table></td><td><table width=50 border=0><tr><td fixwidth=16>"+
        (idFiltro==3 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "3")) + "</td><td fixwidth=48>"+
        "<font color=EAFF86>SwSs</font><br></td></tr></table></td><td><table width=60 border=0><tr><td fixwidth=16>"+
        (idFiltro==4 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "4")) + "</td><td fixwidth=55>"+
        "<font color=EAFF86>Healer</font><br></td></tr></table></td><td><table width=55 border=0><tr><td fixwidth=16>"+
        (idFiltro==5 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "5")) + "</td><td fixwidth=50>"+
        "<font color=EAFF86>Tanks</font><br></td></tr></table></td><td><table width=130 border=0><tr><td fixwidth=16>"+
        (idFiltro==6 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "6")) + "</td><td fixwidth=135>"+
        "<font color=EAFF86>Mage Dmg. Deslers</font><br></td></tr></table></td><td><table width=140 border=0><tr><td fixwidth=16>"+
        (idFiltro==7 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "7")) + "</td><td fixwidth=145>"+
        "<font color=EAFF86>Fighter Dmg. Deslers</font><br></td></tr></table></td></tr></table></center>";
		
		return Retorno;
	}
	
	
	private static Integer[] getTipoBusqueda(int id){
		return TipoBusqueda.get(id);
	}
	
	
	public static void getAllMemberOnLine(L2PcInstance player){
		Vector<String> PplForList = new Vector<String>();
		Map<String, L2PcInstance> pplInstance = new HashMap<String, L2PcInstance>();		
		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();//L2World.getInstance().getPlayers();//;
		
		if(NombresListados!=null){
			if(NombresListados.containsKey(player.getObjectId())){
				NombresListados.remove(player.getObjectId());
			}
			if(NombreListadosConInstance.containsKey(player.getObjectId())){
				NombreListadosConInstance.remove(player.getObjectId());
			}
		}
		
		for(L2PcInstance p : pls){
			if(p!=null){
				
				if(p.isGM()){
					continue;
				}
				
				if((p.isOnline() && !p.isJailed())){
					if(!p.getClient().isDetached()){
						PplForList.add(p.getName());
						pplInstance.put(p.getName(), p);
						if(NombresListados!=null){
							if(!NombresListados.containsKey(player.getObjectId())){
								NombresListados.put(player.getObjectId(),new Vector<String>());
								NombreListadosConInstance.put(player.getObjectId(), new HashMap<String, L2PcInstance>());
							}
						}else{
							NombresListados.put(player.getObjectId(),new Vector<String>());
							NombreListadosConInstance.put(player.getObjectId(), new HashMap<String, L2PcInstance>());
						}
						NombresListados.get(player.getObjectId()).add(p.getName());
						NombreListadosConInstance.get(player.getObjectId()).put(p.getName(), p) ;
					}
				}
			}
		}
		if(NombresListados!=null){
			if(NombresListados.containsKey(player.getObjectId())){
				Collections.sort(NombresListados.get(player.getObjectId()));
			}
		}
	}
	
	
	
	private static String getGrilla(L2PcInstance player, int Tipo, int pagina){
		String Retorno = "<table cellspacing=0 cellpadding=0 border=0><tr><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=160 align=center><button value=\"Player Name\" width=160 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=160 align=center><button value=\"Class name\" width=160 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=45 align=center><button value=\"Level\" width=45 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=52 align=center><button value=\"M. Class\" width=52 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=160 align=center><button value=\"Clan\" width=160 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=70 align=center><button value=\"P. Members\" width=70 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td>"+
                   "<td fixwidth=55 align=center><button value=\"\" width=55 height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter></td><td><img src=\"L2UI_CH3.HorizontalSliderBarRight\" width=2 height=16><br></td><td><img src=\"L2UI_CH3.HorizontalSliderBarLeft\" width=2 height=16><br></td</tr></table>";
		
		String bgColor[]={"180606","191919"};
		String fontColor[]={"86FFFE","86FFA7"};
		
		String BypassRequest = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name() + ";sendpartyinvi;" + Tipo + ";" + String.valueOf(pagina)+ ";%IDPPL%;0;0;0";
		String Infogrilla = "<table cellspacing=0 cellpadding=0 border=0 bgcolor=%BGCOLOR%><tr>"+
		"<td fixwidth=164 align=center><font color=%FONTCOLOR%>%NOMBRE%</font></td>"+
        "<td fixwidth=164 align=center><font color=%FONTCOLOR%>%CLASSNAME%</font></td>"+
        "<td fixwidth=49 align=center><font color=%FONTCOLOR%>%LEVEL%</font></td>"+
        "<td fixwidth=56 align=center><font color=%FONTCOLOR%>%ISBASECLASS%</font></td>"+
        "<td fixwidth=164 align=center><font color=%FONTCOLOR%>%CLAN%</font></td>"+
        "<td fixwidth=74 align=center><font color=%FONTCOLOR%>%PARTYMEMBER%</font></td>"+
        "<td fixwidth=59 align=center><button value=\"Request\" width=55 height=30 action=\""+ BypassRequest +"\" back=L2UI_CT1.Button_DF_Calculator_Over fore=L2UI_CT1.Button_DF_Calculator></td></tr></table>";

		Integer[] TipoListado = getTipoBusqueda(Tipo);
		Vector<Integer> BuffTipLis = new Vector<Integer>();
		if(TipoListado!=null){
			for(int CC: TipoListado){
				BuffTipLis.add(CC);
			}			
		}
		
		int MaximoLista = 10;
		
		int desde = pagina * MaximoLista;
		int cont=0,contF=0;
		boolean record=true;
		boolean havenext = false;
		
		if(NombresListados!=null){
			if(NombresListados.containsKey(player.getObjectId())){
				for(String n : NombresListados.get(player.getObjectId())){
					try{
						L2PcInstance p1 = opera.getPlayerbyName(n);
						if(p1!=null){
							if(cont>=desde){
								if(general.getCharConfigPartyMatching(p1)){
									continue;
								}

								if(p1.getParty()!=null){
									if(!p1.getParty().isLeader(p1)){
										continue;
									}
								}

								if(BuffTipLis!=null){
									if(BuffTipLis.size()>0){
										record=false;
										int ClassID = p1.getClassId().getId();
										if(BuffTipLis.contains(ClassID)){
											record=true;	
										}
									}
								}

								if(record){
									if(p1.isOnline()){
										if(contF<MaximoLista){
											Retorno += Infogrilla.replace("%IDPPL%", String.valueOf(p1.getObjectId())).replace("%PARTYMEMBER%", String.valueOf(( p1.getParty()!=null ? p1.getParty().getMemberCount() : 0 ))) .replace("%CLAN%",  ( p1.getClan()!=null ? p1.getClan().getName() : "No Clan" ) ) .replace("%ISBASECLASS%", ( p1.getBaseClass() == p1.getClassId().getId() ? "Yes" : "No" )  ) .replace("%LEVEL%", String.valueOf(p1.getLevel())).replace("%CLASSNAME%", opera.getClassName(p1.getClassId().getId())).replace("%NOMBRE%",p1.getName()). replace("%FONTCOLOR%", fontColor[contF%2]).replace("%BGCOLOR%", bgColor[contF%2]);
										}else if(contF>MaximoLista){
											havenext = true;
										}
										contF++;
									}
								}
							}
							cont++;
						}
					}catch(Exception a){
						_log.warning(a.getMessage());
					}
				}				
			}
		}
		
		if(havenext || pagina>0 ){
			
			String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.partymatching.name() + ";cargarfiltro;" + Tipo + ";" + String.valueOf(pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.partymatching.name() + ";cargarfiltro;" + Tipo + ";" +String.valueOf(pagina+1)+";0;0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";				
			
			String Grilla = "<table width=730 cellspacing=0 cellpadding=0 border=0 bgcolor=191919  height = 30><tr>"+
            "<td fixwidth=320 align=RIGHT><br>"+
            (pagina>0 ? btnAntes : "")+ "</td>"+
            "<td fixwidth=89 align=CENTER><font name=hs12 color=8886FF>"+ String.valueOf(pagina+1) +"</font></td>"+
            "<td fixwidth=320 align=LEFT><br>"+
            (havenext ? btnProx : "")+  "</td>"+
            "</tr></table>";
			
			Retorno += Grilla;
		}
		
		
		Retorno += "";
		
		
		return Retorno;
	}
	
	
	
	private static String mainHtml(L2PcInstance player,String tipoBusqueda, int pagina){
		final NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-party-matching.htm");
		
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.partymatching.name() + ";cargarfiltro;%IDFILTRO%;0;0;0;0";
		String btnCheckActivo = "<img src=\"L2UI.CheckBox_checked\" width=16 height=16><br>";
		String btnCheckEsperando= " <button width=16 height=16 action=\""+ ByPass +"\" back=L2UI.CheckBox_unable fore=L2UI.CheckBox_unable>";
		
		int idFiltro = Integer.valueOf(tipoBusqueda);
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		
		html.replace("%OPTION_ALL%", idFiltro==0 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "0"));
        html.replace("%OPTION_BUFFER%", idFiltro==1 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "1"));
        html.replace("%OPTION_BDS%", idFiltro==2 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "2"));
        html.replace("%OPTION_SWSS%", idFiltro==3 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "3"));
        html.replace("%OPTION_HEALER%", idFiltro==4 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "4"));
        html.replace("%OPTION_TANKS%", idFiltro==5 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "5"));
        html.replace("%OPTION_MAGE_DMG%", idFiltro==6 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "6"));
        html.replace("%OPTION_FIGHTER_DMG%", idFiltro==7 ? btnCheckActivo : btnCheckEsperando.replace("%IDFILTRO%", "7"));

        
        
		html.replace("%DATA%", getGrilla(player,Integer.valueOf(tipoBusqueda),pagina));
		
		return html.getHtml();
	}
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			getAllMemberOnLine(player);
			return mainHtml(player,"0",0);
		}else if(parm1.equals("cargarfiltro")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("sendpartyinvi")){
			sendRequest(player,Integer.valueOf(parm4),false);
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("refuseAll")){
			if(REQUEST_PARTY!=null){
				if(REQUEST_PARTY.containsKey(player.getObjectId())){
					Iterator itr = REQUEST_PARTY.get(player.getObjectId()).entrySet().iterator();
					while(itr.hasNext()){
						Map.Entry w8 = (Map.Entry)itr.next();
						int idCharToRefuse = (int)w8.getKey();
						if(opera.isCharInGame(idCharToRefuse)){
							L2PcInstance ppl = opera.getPlayerbyID(idCharToRefuse);
							central.msgbox(language.getInstance().getMsg(player).FP_PLAYER_REFUSED_PARTY_REQUEST, ppl);
						}
					}
					
					REQUEST_PARTY.remove(player.getObjectId());
					CerrarVentana.put(player.getObjectId(), true);
					cerrarWindows(player);
					CerrarVentana.put(player.getObjectId(), false);
				}
			}
		}else if(parm1.equals("accept")){
			if(opera.isCharInGame(Integer.valueOf(parm2))){
				L2PcInstance playerEmisor = opera.getPlayerbyID(Integer.valueOf(parm2));
				if(canUse(player, playerEmisor)){
					if(player.getParty()==null){
						player.setParty(new L2Party(player, PartyDistributionType.RANDOM));
					}
					playerEmisor.joinParty(player.getParty());
					try{
						if(REQUEST_PARTY!=null){
							if(REQUEST_PARTY.containsKey(player.getObjectId())){
								if(REQUEST_PARTY.get(player.getObjectId()).containsKey(playerEmisor.getObjectId())){
									REQUEST_PARTY.get(player.getObjectId()).remove(playerEmisor.getObjectId());
									if(REQUEST_PARTY.get(player.getObjectId()).size()==0){
										cerrarWindows(player);
										CerrarVentana.put(player.getObjectId(), false);
									}else{
										sendRequest(null,player.getObjectId(),false);
									}
								}
							}
						}
					}catch(Exception a){
						
					}
				}
			}else{
				central.msgbox(language.getInstance().getMsg(player).PLAYER_IS_OFFLINE, player);
			}
		}else if(parm1.equals("disable")){
			opera.setPlayerConfig("partymatching", player);
			cerrarWindows(player);
			if(REQUEST_PARTY!=null){
				if(REQUEST_PARTY.containsKey(player.getObjectId())){
					REQUEST_PARTY.remove(player.getObjectId());
				}
			}
			CerrarVentana.put(player.getObjectId(), false);
		}else if(parm1.equals("refuse")){
			if(opera.isCharInGame(Integer.valueOf(parm2))){
				L2PcInstance playerEmisor = opera.getPlayerbyID(Integer.valueOf(parm2));
				if(REQUEST_PARTY!=null){
					if(REQUEST_PARTY.containsKey(player.getObjectId())){
						if(REQUEST_PARTY.get(player.getObjectId()).containsKey(playerEmisor.getObjectId())){
							REQUEST_PARTY.get(player.getObjectId()).remove(playerEmisor.getObjectId());
							central.msgbox("Selected player refuse you Party Request.", playerEmisor);
							if(REQUEST_PARTY.get(player.getObjectId()).size()==0){
								cerrarWindows(player);
								CerrarVentana.put(player.getObjectId(), false);
							}else{
								sendRequest(null,player.getObjectId(),false);
							}							
						}
					}
				}
			}else{
				cleanRequest(player);
			}
		}
		return "";
	}
	
	
	public static class PMTimerPlayer implements Runnable{
		L2PcInstance activeChar_Receptor;
		L2PcInstance activeChar_Emite;
		int LastR_ID=0;
		public PMTimerPlayer(L2PcInstance player_Receptor, L2PcInstance player_Emite, int LastID_R){
			activeChar_Receptor = player_Receptor;
			activeChar_Emite = player_Emite;
			LastR_ID = LastID_R;
		}
		@Override
		public void run(){
			try{
				cleanRequest(activeChar_Receptor);
				sendRequest(activeChar_Emite , activeChar_Receptor.getObjectId(),true,LastR_ID);
			}catch(Exception a){

			}

		}
	}
	
	
} 
