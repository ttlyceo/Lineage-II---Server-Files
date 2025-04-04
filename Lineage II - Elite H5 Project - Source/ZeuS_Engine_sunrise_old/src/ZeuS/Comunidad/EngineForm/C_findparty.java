package ZeuS.Comunidad.EngineForm;


import gr.sr.interf.SunriseEvents;

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
import ZeuS.procedimientos.itemLink;
import ZeuS.procedimientos.itemLink.sectores;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.enums.PartyDistributionType;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.util.Broadcast;

public class C_findparty {
	
	private static final String    LinkSend = "	Type=1 	ID=%IDSEND% 	Color=0 	Underline=0 	Title= ";

	private static final Logger _log = Logger.getLogger(C_findparty.class.getName());
	
	private static Map<Integer, Integer>PLAYER_ID_PARTYFIND_REQUEST = new HashMap<Integer, Integer>();
	private static Map<Integer, L2PcInstance>PLAYER_PARTYFIND_REQUEST= new HashMap<Integer, L2PcInstance>();
	private static Map<L2PcInstance, String> PLAYER_PARTYFIND_MESSAGE = new HashMap<L2PcInstance, String>();
	private static Map<Integer, Integer> PLAYER_PARTYFIND_TIME_BEGIN = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> PLAYER_PARTYFIND_TIME_MESSAGE = new HashMap<Integer, Integer>();
	
	private static Map<Integer, HashMap<Integer, L2PcInstance>> PLAYER_WAITING_FOR_APPROVAL = new HashMap<Integer, HashMap<Integer, L2PcInstance>>();
	private static Map<Integer, Integer> PLAYER_WAITING_FOR_APPROVAL_BEGIN = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer>ID_LASTR = new HashMap<Integer, Integer>();
	
	private static Map<Integer, Boolean>CerrarVentana = new HashMap<Integer, Boolean>();
	
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
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private static void sendRequestWindows(L2PcInstance pplEmite, int IDpplReceptor,boolean automatic, int Id_To_Close){
		L2PcInstance pplReceptor = null;
		if(opera.isCharInGame(IDpplReceptor)){
			pplReceptor = opera.getPlayerbyID(IDpplReceptor);
		}else{
			return;
		}

		if(pplEmite!=null){
				try{
					if(PLAYER_WAITING_FOR_APPROVAL!=null){
						if(!PLAYER_WAITING_FOR_APPROVAL.containsKey(pplReceptor.getObjectId())){
							cerrarWindows(pplReceptor);
							return;
						}else{
							if(PLAYER_WAITING_FOR_APPROVAL.get(pplReceptor.getObjectId()).size()<=0){
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
						if(pplEmite.isOnline() && automatic){
							central.msgbox(language.getInstance().getMsg(pplEmite).FP_TIME_OUT , pplEmite);
						}
					}
				}catch(Exception a){
					
				}
		}
		
		
		if(pplReceptor.isInOlympiadMode() || SunriseEvents.isInEvent(pplReceptor) || RaidBossEvent.isPlayerOnRBEvent(pplReceptor)){
			return;
		}
		
		
		
		NpcHtmlMessage html = comun.htmlMaker(pplReceptor, "./config/zeus/htm/" + language.getInstance().getFolder(pplReceptor) + "/communityboard/engine-findparty-player-list.htm");
		
		String gridFormat = opera.getGridFormatFromHtml(html, 1, "%GRID_DATA%"); 
		
		
		
		
		String ByPassAceptar = "link zeusC;" + general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";accept;%IDCHAR%;0;0;0;0;0";
		String ByPassRefuse = "link zeusC;" + general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";refuse;%IDCHAR%;0;0;0;0;0";
		
		//String GrillaFormat = opera.getGridFormatFromHtml(html,1);
		String DataRequest = "";
		Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(pplReceptor.getObjectId()).entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry infoR = (Map.Entry)itr.next();
			int IdChar = (int) infoR.getKey();
			if(opera.isCharInGame(IdChar)){
				L2PcInstance pplB = opera.getPlayerbyID(IdChar);
				String Nombre = pplB.getName();
				String Clase = opera.getClassName(pplB.getClassId().getId());
				String Level = String.valueOf(pplB.getLevel());
				String ImagenID = general.getClassInfo(pplB.getClassId().getId()).getLogo();
				int UnixRequest = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(IdChar) +  general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST; /*segundosDuracionRequest;*/
				int UnixAhora = opera.getUnixTimeNow();
				int SegundosFaltan = UnixRequest - UnixAhora;
				DataRequest += gridFormat.replace("%BYPASS_REFUSE%", ByPassRefuse).replace("%BYPASS_ACCEPT%", ByPassAceptar).replace("%IDCHAR%", String.valueOf(IdChar)) .replace("%SECONDS_REMAINING%", String.valueOf(SegundosFaltan)).replace("%IDIMA%", ImagenID).replace("%PLAYER_NAME%", Nombre).replace("%PLAYER_LEVEL%", Level).replace("%PLAYER_CLASS%", Clase);
			}
		}
		
		html.replace("%GRID_DATA%", DataRequest);
		
		String ByPas_RefuseAll = "link zeusC;" + general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";refuseAll;0;0;0;0;0";;
		String ByPass_Disable_P_Matching = "link zeusC;" + general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";disabled;0;0;0;0;0";;
		
		html.replace("%REFUSE_ALL%", ByPas_RefuseAll);
		
		central.sendHtml(pplReceptor, html.getHtml(),true);
		CerrarVentana.put(pplReceptor.getObjectId(), true);
		int LastR = opera.getUnixTimeNow();
		ID_LASTR.put(pplReceptor.getObjectId(), LastR);
		if(!automatic && pplEmite!=null){
			ThreadPoolManager.getInstance().scheduleGeneral(new PMTimerPlayer(pplReceptor,pplEmite,LastR), general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST  * 1000);
			central.msgbox(language.getInstance().getMsg(pplEmite).FP_REQUEST_SENDING_TO_PLAYER_YOU_NEED_TO_WAIT.replace("$player", pplReceptor.getName()),pplEmite);
		}
	}	
	
	
	private static void sendToAll(String Mensaje, int idChat) {
		CreatureSay strMensaje = new CreatureSay(0, idChat, "[Find Party]", Mensaje);
		Broadcast.toAllOnlinePlayers(strMensaje);		
	}
	
	private static void sendToAll(String Mensaje){
		CreatureSay strMensaje = new CreatureSay(0,1,"[Find Party]",Mensaje);
		Broadcast.toAllOnlinePlayers(strMensaje);
	}
	
	private static boolean canSendMessageTime(L2PcInstance player){
		if(PLAYER_PARTYFIND_TIME_MESSAGE!=null){
			if(PLAYER_PARTYFIND_TIME_MESSAGE.containsKey(player.getObjectId())){
				int unixTimePlayer = PLAYER_PARTYFIND_TIME_MESSAGE.get(player.getObjectId()) + general.COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE;
				int UnixAhora = opera.getUnixTimeNow();
				if(UnixAhora>unixTimePlayer){
					return true;
				}else{
					central.msgbox(language.getInstance().getMsg(player).FP_WAITING_TO_SEND_AGAIN.replace("$seconds", String.valueOf( unixTimePlayer - UnixAhora )),player);
					return false;
				}
			}
		}
		return true;
	}
	
	public static void bypass(L2PcInstance playerAsking, int idLink){
		//L2PcInstance charLink = PLAYER_REQUEST.get(idLink);
		requestParty(idLink,playerAsking);
	}
	
	private static boolean canUse(L2PcInstance playerReceptor, L2PcInstance playerEmite){
		if(!playerReceptor.isOnline()){
			central.msgbox(language.getInstance().getMsg(playerEmite).PLAYER_IS_NOT_ONLINE, playerEmite);
			return false;
		}
		
		if(playerEmite == playerReceptor){
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
	
	@SuppressWarnings("rawtypes")
	private static void refreshList(L2PcInstance partyLeader, boolean isAutomatic){
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(PLAYER_WAITING_FOR_APPROVAL.containsKey(partyLeader.getObjectId())){
				Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(partyLeader.getObjectId()).entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry infoR = (Map.Entry)itr.next();
					int idPartyRequest = (int)infoR.getKey();
					if(PLAYER_WAITING_FOR_APPROVAL_BEGIN!=null){
						if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(idPartyRequest)){
							int UnixPeticion = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(idPartyRequest);
							int UnixAhora = opera.getUnixTimeNow();
							if(UnixAhora >= (UnixPeticion + general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST)){
								PLAYER_WAITING_FOR_APPROVAL.get(partyLeader.getObjectId()).remove(idPartyRequest);
								PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(idPartyRequest);
								if(isAutomatic){
									if(opera.isCharInGame(idPartyRequest)){
										L2PcInstance ppl = opera.getPlayerbyID(idPartyRequest);
										central.msgbox(language.getInstance().getMsg(ppl).FP_TIME_OUT, ppl);
									}
								}
							}							
						}else{
							PLAYER_WAITING_FOR_APPROVAL.get(partyLeader.getObjectId()).remove(idPartyRequest);
						}
					}
				}
			}
		}
	}
	
	private static void registerRequest(L2PcInstance partyL, L2PcInstance partyReq){
		//refreshList(partyL);
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(!PLAYER_WAITING_FOR_APPROVAL.containsKey(partyL.getObjectId())){
				PLAYER_WAITING_FOR_APPROVAL.put(partyL.getObjectId(), new HashMap<Integer, L2PcInstance>());				
			}
		}else{
			PLAYER_WAITING_FOR_APPROVAL.put(partyL.getObjectId(), new HashMap<Integer, L2PcInstance>());
		}
		PLAYER_WAITING_FOR_APPROVAL.get(partyL.getObjectId()).put(partyReq.getObjectId(), partyReq);
		PLAYER_WAITING_FOR_APPROVAL_BEGIN.put(partyReq.getObjectId(),opera.getUnixTimeNow());
	}
	
	private static boolean isInTimeToRegisInWaiting(L2PcInstance player){
		if(PLAYER_WAITING_FOR_APPROVAL_BEGIN!=null){
			if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(player.getObjectId())){
				int tiempoComenzo = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(player.getObjectId()) + general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST;
				int ahora = opera.getUnixTimeNow();
				if(ahora < tiempoComenzo){
					central.msgbox(language.getInstance().getMsg(player).FP_WAITING_TO_SEND_AGAIN.replace("$seconds", String.valueOf((tiempoComenzo - ahora))), player);
					return false;
				}
			}
		}
		return true;
	}
	
	private static void refreshPartysFinder(int IdParty){
		if(PLAYER_PARTYFIND_REQUEST==null){
			return;
		}
		
		L2PcInstance pplleader = null;
		if(PLAYER_PARTYFIND_REQUEST.containsKey(IdParty)){
			if(opera.isCharInGame(PLAYER_PARTYFIND_REQUEST.get(IdParty).getObjectId())){
				pplleader = PLAYER_PARTYFIND_REQUEST.get(IdParty);
			}else{
				return;
			}
		}else{
			return;
		}
			
		if(PLAYER_PARTYFIND_TIME_BEGIN!=null){
			if(PLAYER_PARTYFIND_TIME_BEGIN.containsKey(pplleader.getObjectId())){
				int ComenzoPTFinder = PLAYER_PARTYFIND_TIME_BEGIN.get(pplleader.getObjectId());
				int Ahora = opera.getUnixTimeNow();
				int HastaUnix = ComenzoPTFinder + general.COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD;
				if(Ahora > HastaUnix){
					PLAYER_PARTYFIND_TIME_BEGIN.remove(pplleader.getObjectId());
					PLAYER_PARTYFIND_TIME_MESSAGE.remove(pplleader.getObjectId());
					PLAYER_PARTYFIND_REQUEST.remove(pplleader.getObjectId());
				}
			}
		}	
	}
	
	public static void requestParty(int idParty, L2PcInstance playerRequesting){
		refreshPartysFinder(idParty);
		if(PLAYER_PARTYFIND_REQUEST.containsKey(idParty)){
			L2PcInstance PartyLeader = null;
			try{
				if(opera.isCharInGame( PLAYER_PARTYFIND_REQUEST.get(idParty).getObjectId())){
					PartyLeader = PLAYER_PARTYFIND_REQUEST.get(idParty);					
				}
			}catch(Exception a){
				
			}
			if(PartyLeader!=null){
				if(canUse(PartyLeader,playerRequesting)){
					if(isInTimeToRegisInWaiting(playerRequesting)){
						registerRequest(PartyLeader,playerRequesting);
						sendRequestWindows(playerRequesting, PartyLeader.getObjectId(),false,0);
					}else{
						return;						
					}
				}else{
					return;
				}
			}else{
				central.msgbox(language.getInstance().getMsg(playerRequesting).PLAYER_IS_OFFLINE, playerRequesting);
			}
		}else{
			central.msgbox(language.getInstance().getMsg(playerRequesting).FP_END_REQUEST, playerRequesting);
		}
	}
	
	
	
	private static int getIdToPartyFinder(L2PcInstance player){
		if(PLAYER_ID_PARTYFIND_REQUEST!=null){
			if(PLAYER_ID_PARTYFIND_REQUEST.containsKey(player.getObjectId())){
				int idPartyFromPpl = PLAYER_ID_PARTYFIND_REQUEST.get(player.getObjectId());
				return idPartyFromPpl;
			}
		}
		int t = itemLink.getNewIdLink(sectores.V_FIND_PARTY);
		PLAYER_ID_PARTYFIND_REQUEST.put(player.getObjectId(), t);
		return t;
	}
	
	public static void sendRequestToAll(L2PcInstance player, String Mensaje){
		
		if(!general.COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE){
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return;
		}
		
		
		if(player.isJailed()){
			central.msgbox(language.getInstance().getMsg(player).DISABLED_FOR_PLAYER_IN_JAIL, player);
			return;
		}
		
		
		int t = getIdToPartyFinder(player);
		
		
		
		
		int ptCount = 8;
		
		if(player.getParty()!=null){
			ptCount = 9 - player.getParty().getMemberCount();
			if(player.getParty().getMemberCount()==9){
				return;
			}
		}
		
		
		if(!canSendMessageTime(player)){
			return;
		}
		
		if(player.getParty()!=null){
			if(!player.getParty().isLeader(player)){
				central.msgbox(language.getInstance().getMsg(player).ONLY_PARTY_LEADER_CAN_USE_THIS, player);
				return;
			}
		}
		
		
		String MensajeFromPlayer = "";
		
		if(Mensaje==null){
			if(PLAYER_PARTYFIND_MESSAGE!=null){
				if(!PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
					return;
				}else{
					MensajeFromPlayer = PLAYER_PARTYFIND_MESSAGE.get(player);
				}
			}else{
				return;
			}
		}else if(Mensaje.length()==0){
			if(PLAYER_PARTYFIND_MESSAGE!=null){
				if(!PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
					return;
				}else{
					MensajeFromPlayer = PLAYER_PARTYFIND_MESSAGE.get(player);
				}
			}else{
				return;
			}			
		}else if(Mensaje.length()>0){
			MensajeFromPlayer = Mensaje;
		}
		
		cleanRegistroPTFINDER(player,t);
		
		PLAYER_PARTYFIND_MESSAGE.put(player, MensajeFromPlayer);
		PLAYER_PARTYFIND_TIME_MESSAGE.put(player.getObjectId(), opera.getUnixTimeNow());
		PLAYER_PARTYFIND_TIME_BEGIN.put(player.getObjectId(), opera.getUnixTimeNow());
		PLAYER_PARTYFIND_REQUEST.put(t, player);
		
		String strMensaje = LinkSend.replace("%IDSEND%", String.valueOf(t)) + " " + player.getName() + " ("+ String.valueOf(ptCount) +"/9) free slots " + MensajeFromPlayer;
		sendToAll(strMensaje, Say2.PARTY);
	}
	
	private static void cleanRegistroPTFINDER(L2PcInstance player, int idParty){
		if(PLAYER_PARTYFIND_MESSAGE!=null){
			if(PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
				PLAYER_PARTYFIND_MESSAGE.remove(player);
			}
		}
		
		if(PLAYER_PARTYFIND_MESSAGE!=null){
			if(PLAYER_PARTYFIND_MESSAGE.containsKey(player)){
				PLAYER_PARTYFIND_MESSAGE.remove(player);
			}
		}
		
		if(PLAYER_PARTYFIND_TIME_MESSAGE!=null){
			if(PLAYER_PARTYFIND_TIME_MESSAGE.containsKey(player.getObjectId())){
				PLAYER_PARTYFIND_TIME_MESSAGE.remove(player.getObjectId());
			}
		}
		
		if(PLAYER_PARTYFIND_TIME_BEGIN!=null){
			if(PLAYER_PARTYFIND_TIME_BEGIN.containsKey(player.getObjectId())){
				PLAYER_PARTYFIND_TIME_BEGIN.remove(player.getObjectId());
			}
		}
		
		if(PLAYER_PARTYFIND_REQUEST!=null){
			if(PLAYER_PARTYFIND_REQUEST.containsKey(idParty)){
				PLAYER_PARTYFIND_REQUEST.remove(idParty);
			}
		}

	}
	
	@SuppressWarnings("rawtypes")
	private static Vector<Map<String, String>> pplsSolicitando(String Busqueda){
		Vector<Map<String, String>> t = new Vector<Map<String, String>>();
		Iterator itr = PLAYER_PARTYFIND_REQUEST.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry infoR = (Map.Entry)itr.next();
			int idParty = (int)infoR.getKey();
			try{
				L2PcInstance pp = null;
				if(opera.isCharInGame(PLAYER_PARTYFIND_REQUEST.get(idParty).getObjectId())){
					pp = PLAYER_PARTYFIND_REQUEST.get(idParty);
					if(pp!=null){
						refreshList(pp,false);
						int MaxDuracion = PLAYER_PARTYFIND_TIME_BEGIN.get(pp.getObjectId()) + general.COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD;
						int Ahora = opera.getUnixTimeNow();
						if(MaxDuracion>Ahora){
							Map<String, String> F = new HashMap<String, String>();
							F.put("NOMBRE",pp.getName());
							F.put("CLAN", (pp.getClan()!= null ? pp.getClan().getName() : "No Have" ) );
							F.put("MENSAJE", PLAYER_PARTYFIND_MESSAGE.get(pp));
							F.put("COUNT",  String.valueOf(  ( pp.getParty() != null ? pp.getParty().getMemberCount() : 1 )   ) );
							F.put("IDPLAYER", String.valueOf(pp.getObjectId()));
							F.put("IDPARTY", String.valueOf(idParty));
							F.put("CLASS", opera.getClassName(pp.getClassId().getId()));
							F.put("SEGUNDOSESPERA", String.valueOf(MaxDuracion-Ahora) );
							t.add(F);
						}
					}
				}
			}catch(Exception a){
				
			}
		}
		
		return t;
	}
	
	
	private static String getGrilla(L2PcInstance player, String Buscar, int pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-find-party.htm");
		String []bgcolor= {"180606","191919"};
		String []fccolor= {"FAAC58","86FFFE"};
		String []fccolor_2= {"7C5A34","3B6363"};
		
		String ByPassRequest = "bypass " + general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC() + ";" + Engine.enumBypass.partymatching.name()+";sendInvitation;%IDCHAR%;0;0;0;0;0";
		int Contador = 0;
		
		String gridFormat = opera.getGridFormatFromHtml(html, 1, "%GRID_DATA%");
		gridFormat = gridFormat.replace("%BYPASS_REQUEST%", ByPassRequest) ;
		String GridData = "";
		
		for(Map<String, String> g : pplsSolicitando(Buscar)){
			GridData += gridFormat.replace("%IDCHAR%", g.get("IDPARTY")) .replace("%TIME%", g.get("SEGUNDOSESPERA")) .replace("%CLASS%", g.get("CLASS")) .replace("%FONTCOLOR2%",fccolor_2[Contador%2]).replace("%PARTYCOUNT%", g.get("COUNT")) .replace("%CLAN%", g.get("CLAN")) .replace("%MESSAGE%", g.get("MENSAJE")) .replace("%NAME%", g.get("NOMBRE")) .replace("%FONTCOLOR%", fccolor[Contador%2]) .replace("%BGCOLOR%", bgcolor[Contador%2]);
		}
		
		html.replace("%GRID_DATA%", GridData);
		
		return html.getHtml();
	}
	
	
	
	private static String mainHtml(L2PcInstance player, String Parametro, String Busqueda, int pagina){
		return getGrilla(player,Busqueda,pagina);
	}
	
	
	@SuppressWarnings("rawtypes")
	private static void cleanRequest(L2PcInstance playerReceptor){
		if(PLAYER_WAITING_FOR_APPROVAL!=null){
			if(PLAYER_WAITING_FOR_APPROVAL.containsKey(playerReceptor.getObjectId())){
				Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(playerReceptor.getObjectId()).entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry InfoRequest = (Map.Entry)itr.next();
					int IdChar = (int) InfoRequest.getKey();
					if(opera.isCharInGame(IdChar)){
						int InicioUnix = PLAYER_WAITING_FOR_APPROVAL_BEGIN.get(IdChar);
						int ActualUnix = opera.getUnixTimeNow();
						if((InicioUnix + general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST) <= ActualUnix){
							PLAYER_WAITING_FOR_APPROVAL.get(playerReceptor.getObjectId()).remove(IdChar);
						}
					}else{
						PLAYER_WAITING_FOR_APPROVAL.get(playerReceptor.getObjectId()).remove(IdChar);
					}
				}
			}
		}
	}	
	
	@SuppressWarnings({ "unused", "rawtypes" })
	public static String bypass(L2PcInstance player, String params){
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning(params);
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.msgbox(params, player);
		}
		
		if(params.equalsIgnoreCase(general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC())){
			return mainHtml(player,"0","0",0);
		}
		
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"0","0",0);
		}else if(parm1.equals("accept")){
			if(opera.isCharInGame(Integer.valueOf(parm2))){
				L2PcInstance playerEmisor = opera.getPlayerbyID(Integer.valueOf(parm2));
				if(canUse(player, playerEmisor)){
					if(player.getParty()==null){
						player.setParty(new L2Party(player, PartyDistributionType.RANDOM));
					}
					playerEmisor.joinParty(player.getParty());
					if(player.isInParty()){
						player.getParty().setPendingInvitation(false); // if party is null, there is no need of decreasing
					}
					player.setActiveRequester(null);
					player.onTransactionResponse();					
					try{
						if(PLAYER_WAITING_FOR_APPROVAL!=null){
							if(PLAYER_WAITING_FOR_APPROVAL.containsKey(player.getObjectId())){
								if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).containsKey(playerEmisor.getObjectId())){
									PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).remove(playerEmisor.getObjectId());
									try{
										if(PLAYER_WAITING_FOR_APPROVAL_BEGIN.containsKey(playerEmisor.getObjectId())){
											PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(playerEmisor.getObjectId());
										}
									}catch(Exception a){
										
									}
									if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).size()==0){
										cerrarWindows(player);
										CerrarVentana.put(player.getObjectId(), false);
									}else{
										sendRequestWindows(null,player.getObjectId(),false,0);
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
		}else if(parm1.equals("refuse")){
			if(opera.isCharInGame(Integer.valueOf(parm2))){
				L2PcInstance playerEmisor = opera.getPlayerbyID(Integer.valueOf(parm2));
				if(PLAYER_WAITING_FOR_APPROVAL!=null){
					if(PLAYER_WAITING_FOR_APPROVAL.containsKey(player.getObjectId())){
						if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).containsKey(playerEmisor.getObjectId())){
							PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).remove(playerEmisor.getObjectId());
							central.msgbox( language.getInstance().getMsg(playerEmisor).SELECTED_PLAYER_REFUSE_PARTY_REQUEST, playerEmisor);
							if(PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).size()==0){
								cerrarWindows(player);
								CerrarVentana.put(player.getObjectId(), false);
							}else{
								sendRequestWindows(null,player.getObjectId(),false,0);
							}							
						}
					}
				}
			}else{
				cleanRequest(player);
			}
			
		}else if(parm1.equals("refuseAll")){
			if(PLAYER_WAITING_FOR_APPROVAL!=null){
				if(PLAYER_WAITING_FOR_APPROVAL.containsKey(player.getObjectId())){
					//FastMap<Integer,FastMap<Integer,L2PcInstance>> PLAYER_WAITING_FOR_APPROVAL
					Iterator itr = PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).entrySet().iterator();
					while(itr.hasNext()){
						Map.Entry w8 = (Map.Entry)itr.next();
						int idCharToRefuse = (int)w8.getKey();
						if(opera.isCharInGame(idCharToRefuse)){
							central.msgbox(language.getInstance().getMsg(player).FP_PLAYER_REFUSED_PARTY_REQUEST, PLAYER_WAITING_FOR_APPROVAL.get(player.getObjectId()).get(idCharToRefuse));
						}
						try{
							PLAYER_WAITING_FOR_APPROVAL_BEGIN.remove(idCharToRefuse);
						}catch(Exception a){
							
						}
					}
					PLAYER_WAITING_FOR_APPROVAL.remove(player.getObjectId());
					CerrarVentana.put(player.getObjectId(), true);
					cerrarWindows(player);
					CerrarVentana.put(player.getObjectId(), false);
				}
			}
		}else if(parm1.equals("sendInvitation")){
			requestParty(Integer.valueOf(parm2), player);
			return mainHtml(player,"0","0",0);
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
				refreshList(activeChar_Receptor, true);
				
				sendRequestWindows(activeChar_Emite , activeChar_Receptor.getObjectId(),true,LastR_ID);
			}catch(Exception a){

			}

		}
	}
	
}
