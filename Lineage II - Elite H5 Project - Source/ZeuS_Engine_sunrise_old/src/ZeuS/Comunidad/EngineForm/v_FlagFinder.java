package ZeuS.Comunidad.EngineForm;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_FlagFinder{
	
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(v_FlagFinder.class.getName());
	private static Map<Integer, _flagCharInfo> InfoCharTime = new HashMap<Integer, _flagCharInfo>();
	
	
	private static String mainHtml(L2PcInstance player, String Params, String BypassAnterior){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-flag-finder.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%COST%", cbFormato.getCostOfService(general.FLAG_FINDER_PRICE));
		
		Vector<String>Requerimientos = new Vector<String>();
		
		Requerimientos.add("For Nobles Only," + String.valueOf(general.FLAG_FINDER_CAN_NOBLE));
		Requerimientos.add("Minimun Level to Use," + String.valueOf(general.FLAG_FINDER_LVL));
		Requerimientos.add("Minimun Level of victim," + String.valueOf(general.FLAG_PVP_PK_LVL_MIN));
		Requerimientos.add("Minimun PvP/PK of victim," + String.valueOf(general.FLAG_FINDER_MIN_PVP_FROM_TARGET));
		Requerimientos.add("Use in Flag mode," + String.valueOf(general.FLAG_FINDER_CAN_USE_FLAG));
		Requerimientos.add("Use in Pk mode," + String.valueOf(general.FLAG_FINDER_CAN_USE_PK));
		Requerimientos.add("Pk Pririty," + String.valueOf(general.FLAG_FINDER_PK_PRIORITY));

		html.replace("%REQUIREMENTS%", cbFormato.getRequirements(Requerimientos));
		
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_FLAGFINDER) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}			
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,"");
		}else if(parm1.equals("go")){
			if(opera.canUseCBFunction(player)){
				if(canuseFlagFinder(player)){
					if(goFlagFinder(player)){
						opera.removeItem(general.FLAG_FINDER_PRICE, player);
						cbFormato.cerrarCB(player);
					}
				}
			}
		}else if(parm1.equals("FromMain")){
			return mainHtml(player,parm1,"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC());
		}
		return "";
	}
	public static boolean canuseFlagFinder(L2PcInstance player){
		if(!general._activated()){
			return false;
		}
		if(!general.FLAG_FINDER_CAN_USE_FLAG && (player.getPvpFlag()>0)){
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG, player);
			return false;
		}
		if(!general.FLAG_FINDER_CAN_USE_PK && (player.getKarma()>0)){
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK, player);
			return false;
		}
		if(general.FLAG_FINDER_CAN_NOBLE && !player.isNoble()){
			central.msgbox(language.getInstance().getMsg(player).YOU_ARE_NOT_NOBLE, player);
			return false;
		}
		if(general.FLAG_FINDER_LVL > player.getLevel()){
			central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.FLAG_FINDER_LVL)), player);
			return false;
		}
		if(!opera.haveItem(player, general.FLAG_FINDER_PRICE)){
			return false;
		}
		return true;
	}
	@SuppressWarnings("unused")
	public static boolean goFlagFinder(L2PcInstance player){
		if(!general._activated()){
			return false;
		}

		if(!canuseFlagFinder(player)) {
			return false;
		}

		String PreList = "";
		Vector<L2PcInstance> playersOnline = new Vector<L2PcInstance>();
		Vector<L2PcInstance> playersOnlinePK = new Vector<L2PcInstance>();

		//Freya Collection<L2PcInstance> pls = L2World.getInstance().getAllPlayers().values();
		//Vector<L2PcInstance> pls = new Vector<L2PcInstance>();

		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();

		for(L2PcInstance onlinePlayer : pls){
			if(onlinePlayer.isOnline() && !onlinePlayer.getName().equals(player.getName())){
				if((onlinePlayer.getPvpFlag() != 0) || (onlinePlayer.getKarma() > 0)){
					if(onlinePlayer.inObserverMode()){
						continue;
					}
					int insiege = Integer.valueOf(onlinePlayer.getSiegeState());
					if (insiege <=0){
						if(!opera._checkIsEventServer(onlinePlayer)){
							if(!onlinePlayer.isInStance() && !opera.isMaster(onlinePlayer)){
								if(onlinePlayer.getLevel() >= general.FLAG_PVP_PK_LVL_MIN){
									if(!onlinePlayer.isInsideZone(ZoneIdType.CASTLE) || (onlinePlayer.isInsideZone(ZoneIdType.CASTLE) && general.FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE)){
										if(!onlinePlayer.isInsideZone(ZoneIdType.CLAN_HALL) && !onlinePlayer.isInsideZone(ZoneIdType.NO_ZEUS)){
											if(!RaidBossEvent.isPlayerOnRBEvent(onlinePlayer)){
												if(!onlinePlayer.isInsideZone(ZoneIdType.NO_SUMMON_FRIEND) && !onlinePlayer.isInsideZone(ZoneIdType.QUEEN_ANT)){
													if(general.FLAG_FINDER_MIN_PVP_FROM_TARGET==0 || onlinePlayer.getKarma()>0 || (general.FLAG_FINDER_MIN_PVP_FROM_TARGET <= onlinePlayer.getPvpKills())){
														if((general.FLAG_FINDER_CHECK_CLAN && !isInTheSameClan(player,onlinePlayer)) || !general.FLAG_FINDER_CHECK_CLAN){
															if(!onlinePlayer.isGM() && (!onlinePlayer.isInsideZone(ZoneIdType.PEACE) && !onlinePlayer.isInsideZone(ZoneIdType.TOWN))){
																playersOnline.add(onlinePlayer);
																if(onlinePlayer.getKarma()>0){
																	playersOnlinePK.add(onlinePlayer);
																}
															}
														}	
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		if (playersOnline.size() <= 0){
			central.msgbox(language.getInstance().getMsg(player).FLAG_FINDER_NO_PVP, player);
			return false;
		}

		Random aleatorio = new Random();

		int RandCharNormal = 0;
		int RandCharPK = 0;

		L2PcInstance playerElegido = null;

		boolean _HaveIt = false;
		int Buc = 0;
		
		while(!_HaveIt){
			if(general.FLAG_FINDER_PK_PRIORITY && (playersOnlinePK.size()>0) ){
				RandCharPK = aleatorio.nextInt( playersOnlinePK.size() );				
				playerElegido = playersOnlinePK.get(RandCharPK);
				break;
			}else{
				RandCharNormal = aleatorio.nextInt(playersOnline.size());
				playerElegido = playersOnline.get(RandCharNormal);
			}
			
			if(InfoCharTime == null){
				_HaveIt = true;
				_flagCharInfo temp = new _flagCharInfo(playerElegido);
				InfoCharTime.put(playerElegido.getObjectId(), temp);
				break;
			}else if(InfoCharTime.containsKey(playerElegido.getObjectId())){
				if(InfoCharTime.get(playerElegido.getObjectId()).canGo()){
					_HaveIt = true;
					InfoCharTime.get(playerElegido.getObjectId()).reset();
					break;
				}
			}else{
				_HaveIt = true;
				_flagCharInfo temp = new _flagCharInfo(playerElegido);
				InfoCharTime.put(playerElegido.getObjectId(), temp);
				break;
			}
			Buc++;
			if(Buc>=30){
				_HaveIt = true;
				InfoCharTime.get(playerElegido.getObjectId()).reset();				
			}
		}

		player.teleToLocation(playerElegido.getX(),playerElegido.getY(),playerElegido.getZ());
		central.msgbox_Lado(language.getInstance().getMsg(player).FLAG_FINDER_PLAYER_FOUND.replace("$player", playerElegido.getName()), player,"Go PvP");
		if(!opera.isMaster(player)){
			central.msgbox_Lado(language.getInstance().getMsg(playerElegido).FLAG_FINDER_COMMING_FOR_YOU.replace("$player", player.getName()) ,playerElegido,"Go PvP");
		}
		return true;
	}
	
	private static boolean isInTheSameClan(L2PcInstance playerUse, L2PcInstance playerTarget){
		if(playerUse.getClan()!=null){
			if(playerTarget.getClan()!=null){
				if(playerUse.getClanId() == playerTarget.getClanId()){
					return true;
				}
			}
		}
		
		return false;
	}
}

class _flagCharInfo{
	int IDPlayer;
	int LastUnixTime;
	final int SecondToGoAgain = 50;
	public _flagCharInfo(L2PcInstance player){
		this.IDPlayer = player.getObjectId();
		this.LastUnixTime = opera.getUnixTimeNow();
	}
	public boolean canGo(){
		int UnixNow = opera.getUnixTimeNow();
		if( ( UnixNow - LastUnixTime ) >= this.SecondToGoAgain ){
			return true;
		}
		return false;
	}
	public void reset(){
		this.LastUnixTime = opera.getUnixTimeNow();
	}
}