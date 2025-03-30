package ZeuS.procedimientos;

import gr.sr.interf.SunriseEvents;

import java.util.logging.Logger;

import l2r.Config;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.instancemanager.CastleManager;
import l2r.gameserver.instancemanager.SiegeManager;
import l2r.gameserver.instancemanager.TownManager;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Castle;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SiegeInfo;
import l2r.gameserver.network.serverpackets.WareHouseDepositList;
import ZeuS.Config.general;
import ZeuS.admin.menu;
import ZeuS.interfase.GrandBossInfo;
import ZeuS.interfase.RaidBossInfo;
import ZeuS.interfase.aioChar;
import ZeuS.interfase.cambionombre;
import ZeuS.interfase.central;
import ZeuS.interfase.clan;
import ZeuS.interfase.desafio;
import ZeuS.interfase.desafioBusqueda;
import ZeuS.interfase.htmls;
import ZeuS.interfase.shop;
import ZeuS.interfase.teleport;
import ZeuS.interfase.transform;
import ZeuS.interfase.votereward;
import ZeuS.language.language;

public class delegar{

	private static final Logger _log = Logger.getLogger(delegar.class.getName());

	public static String delegar_desafio(String event, L2PcInstance player){
		if(!general._activated()){
			return "";
		}
		if(event.equals("getPremio")){
			L2Object obj = player.getTarget();
			if (obj == null)
			{
				return "";
			}
			if (obj instanceof L2Npc)
			{
				if(desafioBusqueda.getPremios(player, opera.getIDNPCTarget(player) ) ){
					return desafioBusqueda.premiosOK();
				}
			}
			return "";
		}

		return "";
	}

	public static String _delegar(String event, L2PcInstance player){
		if (!general._activated()){
			return "";
		}
		
		if(SunriseEvents.isInEvent(player)){
			return "";
		}		
		
		if(!Bloqueos(event,player)){
			player.sendMessage("Ingrese los datos Solicitados. Acción Cancelada");
			opera.enviarHTML(player,language.getInstance().getMsg(player).TYPING_ERROR);
			return "";
		}

		String strHtml = Subproces(event,player);
		return strHtml;

	}

	@SuppressWarnings("unused")
	private static String Concatenar(String[] strEvent, int Inicio){
		int contador = 0;
		String strConcatenador ="";
		for(String Eventos : strEvent){
			if(contador >Inicio) {
				strConcatenador += " " + Eventos;
			}
			contador++;
		}
		return strConcatenador;
	}

	private static boolean Bloqueos(String event, L2PcInstance player){
		boolean retorno = true;

		event = event.replace(","," ");
		String Prueba1 = event.replace(" ",":");
		String[] Buscar = Prueba1.split(":");

		if(general.DEBUG_CONSOLA_ENTRADAS) {
			_log.warning("::DEBUGING NPC AIO -> " + Prueba1 + " ->Largo=" + String.valueOf(Buscar.length));
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER && player.isGM()) {
			player.sendMessage("::DEBUGING NPC AIO -> " + Prueba1 + " ->Largo=" + String.valueOf(Buscar.length));
		}

		if(Buscar[0].equals("setConfig1")){
			return true;
		}

		if (Buscar[0].equals("bugReportIN") || Buscar[0].equals("MENUDONA_VARIOS")){
			if (Buscar.length <= 3){
				return false;
			}
		}
		if (Buscar[0].equals("crea")){
			if (Buscar.length <= 4){
				return false;
			}
		}

		if (Buscar[0].equals("DropSearch")){
			if ((Buscar.length >=5) || (Buscar.length<=2)){
				return false;
			}
		}
		if(Buscar[0].equals("DESAFIOADD")){
			if ((Buscar.length <=2) || (Buscar.length > 4)){
				return false;
			}
		}
		if (Buscar[0].equals("logpeleas") || Buscar[0].equals("CHANGEPIN")){
			if ((Buscar.length <= 3) || (Buscar.length >4)){
				return false;
			}
		}
		if(Buscar[0].equals("ENVIAR_NOTIFICACION")){
			if(Buscar.length < 4){
				return false;
			}
		}




		return retorno;
	}


	@SuppressWarnings("unused")
	private static String Subproces(String event, L2PcInstance player){
		if(!general._activated()){
			return "";
		}
		
		if(player.isInOlympiadMode()){
			return "";
		}
		if(player.isOlympiadStart()){
			return "";
		}

		String[] eventSplit;
		String evento = null;
		String eventParam1;
		String eventParam2;
		String eventParam3;

		int currentTime = (int) (System.currentTimeMillis()/1000);

		try{
			eventSplit = event.split(" ");
			evento = eventSplit[0];
			eventParam1 = eventSplit[1];
			eventParam2 = eventSplit[2];
			eventParam3 = eventSplit[3];
		}catch(Exception a){
			return htmls.ErrorTipeoEspacio();
		}

		String[] datiAzione;

		int idSchema;
		int idSkill;
		int livello;
		int livelloSkill;



		int ANNOUCSTR =0;
		int EFECTPVP = 0;
		int SHOWSHIFT = 0;
		int SHOWPINWINDOWS =0;
		int SHOWHERO=0;
		int EXPSP=0;
		int TRADE=0;
		int BADBUFF=0;
		int HIDESTORE=0;
		int REFUSAL=0;
		int PARTYMATCHING=0;
		int OLYSCHEME=0;
		int READOLY = 0;


		String strHtml = "";

		switch(evento){
			case "getRegOlyEven":
				teamEvent.RegisterOnTeam(player);
				break;
			case "shopBD":
				if(eventParam1.equals("0")){
					strHtml = shop.getShopCentral(player);
				}else if(eventParam1.equals("1")){
					strHtml = shop.getShopSeccion(player, eventParam2);
				}
				break;
			case "transform":
				if(eventParam1.equals("0")){ //Main Meni
					strHtml = transform.mainTransform(player,Integer.valueOf(eventParam2));
					break;
				}else if(eventParam1.equals("1")){
					if(transform.setTransformar(player, Integer.valueOf(eventParam2),1)){
						opera.removeItem(general.TRANSFORMATION_ESPECIALES_PRICE, player);
						central.msgbox("Felicidades, ya estós transformado", player);
						break;
					}
				}else if(eventParam1.equals("2")){
					if(transform.setTransformar(player, Integer.valueOf(eventParam2),2)){
						opera.removeItem(general.TRANSFORMATION_RAIDBOSS_PRICE, player);
						central.msgbox("Felicidades, ya estós transformado", player);
						break;
					}
				}else if(eventParam1.equals("3")){
					if(transform.setTransformar(player, Integer.valueOf(eventParam2),3)){
						opera.removeItem(general.TRANSFORMATION_PRICE, player);
						central.msgbox("Felicidades, ya estós transformado", player);
						break;
					}
				}else if(eventParam1.equals("4")){
					strHtml = transform.mainTransform(player,-1);
					break;
				}
				strHtml = transform.mainTransform(player,0);
				break;

			case "teleportMain":
				strHtml = teleport.CentralHTML(player);
				break;
			case "teleportshow":
				strHtml = teleport.getSegmento(player, Integer.valueOf(eventParam1), eventParam2);
				break;
			case "howiam":
				strHtml = htmls.getInfoContacto();
				break;
			case "setConfig1":
				strHtml = menu.bypass(player, event);
				break;
			case "Config":
				if(eventParam1.equals("1")){
					general.loadConfigs();
					central.msgbox("Config Recargada", player);
				}
				if(eventParam1.equals("2")){
					strHtml = menu.getConfigMenu(player);
					break;
				}
				strHtml = htmls.firtsHTML(player, general.npcGlobal(player));
				break;
			case "maincentral":
				strHtml = htmls.firtsHTML(player, general.npcGlobal(player));
				break;
			case "RaidBossInf":
				strHtml = RaidBossInfo.getInfo(player, Integer.valueOf(eventParam1));
				break;
			case "RaidBossInGo":
				if(RaidBossInfo.goRaid(player, eventParam1, eventParam2, eventParam3)){
					opera.removeItem(general.RAIDBOSS_INFO_TELEPORT_PRICE, player);
					central.msgbox("Buena Suerte con tu raid!", player);
					strHtml ="";
				}else{
					String PaginaQuedo ="0";
					try{
						PaginaQuedo = eventSplit[4];
					}catch(Exception a){
						PaginaQuedo = "0";
					}
					strHtml = RaidBossInfo.getInfo(player, Integer.valueOf(PaginaQuedo));
				}
				break;
			case "showGrandBoss":
				strHtml = GrandBossInfo.getInfo();
				break;
			case "reloadscript":
				if(general.IS_USING_NPC.get(player.getObjectId()) && !general.IS_USING_CB.get(player.getObjectId())){
					strHtml = htmls.firtsHTML(player, general.npcGlobal(player));
				}else{
					strHtml = "";
				}
				break;
			case "gototeleport":
				if(SiegeManager.getInstance().getSiege(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3)) != null){
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					strHtml = "";
					break;
				}
				else if(TownManager.townHasCastleInSiege(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2)) && player.isInsideZone(ZoneIdType.TOWN)){
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					strHtml = "";
					break;
				}else if (!Config.ALT_GAME_KARMA_PLAYER_CAN_USE_GK && (player.getKarma() > 0)){
					player.sendMessage("Go away, you're not welcome here.");
					strHtml = "";
					break;
				}else if(player.isAlikeDead()){
					strHtml = "";
					break;
				}

				String retorno ="";
				if(!general.FREE_TELEPORT){
					if(opera.haveItem(player, general.TELEPORT_PRICE)){
						opera.removeItem(general.TELEPORT_PRICE, player);
						player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
						player.sendMessage("Has sido teletransportado a " + eventParam1 +" "+eventParam2+ " "+eventParam3);
						break;
					}
				}else{
					player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
					player.sendMessage("Has sido teletransportado a " + eventParam1 +" "+eventParam2+ " "+eventParam3);
					break;
				}
				break;
	   		case "chat1":
	   			strHtml = htmls.MainHtml1(player);
				break;
	   		case "chat2":
	   			strHtml = htmls.MainHtml2(player);
				break;
	   		case "chat3":
	   			strHtml = htmls.MainHtml3(player);
				break;
	   		case "chat4":
	   			strHtml = htmls.MainHtml4(player);
				break;
	   		case "chat6":
	   			strHtml = htmls.MainHtml6(player);
				break;
	   		case "AUGMENTMNU":
	   			strHtml = htmls.MainMenuAug(eventParam1);
				break;
	   		case "DesafioVerDetaFami":
	   				//Llama a la nueva Forma de Ver las Familias con los premios indicandole el ID de la Familia a ver en param2
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
	   				break;
	   		case "DESAFIOADD":
	   			if (eventParam1.equals("0")){
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,-1);
					break;
	   			}
	   			if(!opera.isNumeric(eventParam2)){
	   				central.msgbox("Solo debes ingresar nómeros en la ID del Item", player);
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
					break;
	   			}
	   			if(!opera.isNumeric(eventParam3)){
	   				central.msgbox("Solo debes ingresar nómeros en la Cantidad", player);
	   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
					break;
	   			}
	   			desafio.AgregarPremios(player, eventParam1, eventParam2, eventParam3);
   				strHtml = desafio.menuConfigAddPremiosDesafio(player,Integer.parseInt(eventParam1));
				break;
	   		case "DESAFIO":
	   			strHtml = desafio.Desafio(player, eventParam1, eventParam2, eventParam3);
				break;
	   		case "OPCIONESVAR":
	   			if(eventParam1.equals("0")){
	   				strHtml = htmls.MainHtmlOpcionesVarias(player);
					break;
	   			}
	   			if(eventParam1.equals("1")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_SEXO_ITEM_PRICE)){
	   					opera.changeSex(player);
	   					opera.removeItem(general.OPCIONES_CHAR_SEXO_ITEM_PRICE, player);
	   				}
	   			}
	   			if(eventParam1.equals("2")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_NOBLE_ITEM_PRICE)){
	   					if(opera.setNoble(player)){
	   						central.msgbox("Usted ahora es Noble", player);
	   						opera.removeItem(general.OPCIONES_CHAR_NOBLE_ITEM_PRICE, player);
	   					}
	   				}
	   			}
	   			if(eventParam1.equals("3")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_LVL85_ITEM_PRICE)){
	   					if(opera.set85(player)){
	   						central.msgbox("Usted tiene ahora lvl 85", player);
	   						opera.removeItem(general.OPCIONES_CHAR_LVL85_ITEM_PRICE, player);
	   					}
	   				}
	   			}

	   			if(eventParam1.equals("4")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_BUFFER_AIO_PRICE)){
	   					if(player.getLevel() < general.OPCIONES_CHAR_BUFFER_AIO_LVL){
	   						central.msgbox("Lo sentimos, debes tener un level superior o igual a " + String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL) + " para ósta operación", player);
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}
	   					if(aioChar.setNewAIO(player,true)){
	   						opera.removeItem(general.OPCIONES_CHAR_BUFFER_AIO_PRICE, player);
	   						strHtml = "";
	   						break;
	   					}else{
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						central.msgbox("No es posible crear AIO Buffer.", player);
	   						break;
	   					}
	   				}
	   			}

	   			if(eventParam1.equals("5")){
	   				if(opera.haveItem(player, general.OPCIONES_CHAR_FAME_PRICE)){
	   					if(general.OPCIONES_CHAR_FAME_NOBLE && !player.isNoble()){
	   						central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}

	   					if(general.OPCIONES_CHAR_FAME_LVL > player.getLevel()){
   							String Mensaje = language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.OPCIONES_CHAR_FAME_LVL));
   							central.msgbox(Mensaje, player);
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}

	   					if(!opera.haveItem(player, general.OPCIONES_CHAR_FAME_PRICE)){
	   						strHtml = htmls.MainHtmlOpcionesVarias(player);
	   						break;
	   					}

						player.setFame(player.getFame() + general.OPCIONES_CHAR_FAME_GIVE);
						opera.removeItem(general.OPCIONES_CHAR_FAME_PRICE, player);
						player.broadcastUserInfo();

	   				}
	   			}

	   			strHtml = htmls.MainHtmlOpcionesVarias(player);
				break;
			case "DellvlMenu":
				strHtml = htmls.DelevelMenu(player);
				break;
			case "ConfigPanel":
				if(eventParam1.equals("1") || eventParam1.equals("2") || eventParam1.equals("3") || eventParam1.equals("4")){
					//if(player.toppvppk_SHOWANNOUCEMENT_ALL()) {
					ANNOUCSTR = (opera.TOP_PVP_PK_ANNOU(player)?1:0);
					EFECTPVP = (opera.TOP_PVP_PK_EFFECT(player)?1:0);
					SHOWSHIFT = (opera.SHOW_MY_STAT_SHIFT(player)?1:0);
					SHOWPINWINDOWS = (opera.SHOW_PIN_WINDOWS(player)?1:0);
					SHOWHERO = (opera.SHOW_HERO_PLAYER(player)?1:0);
					EXPSP = (general.getCharConfigEXPSP(player)?1:0);
					TRADE = (general.getCharConfigTRADE(player)?1:0);
					BADBUFF = (general.getCharConfigBADBUFF(player)?1:0);
					HIDESTORE = (general.getCharConfigHIDESTORE(player)?1:0);
					REFUSAL = ( general.getCharConfigREFUSAL(player) ? 1 : 0 );
					PARTYMATCHING =( general.getCharConfigREFUSAL(player) ? 1 : 0 );
					OLYSCHEME = (general.getCharConfigOlyScheme(player) ? 1 : 0);
					READOLY = (general.getCharConfigReadOlyWinner(player) ? 1: 0);
				}
			case "TELEPORT_TO_FREE":
				player.teleToLocation(Integer.valueOf(eventParam1), Integer.valueOf(eventParam2), Integer.valueOf(eventParam3), true);
				strHtml = "";
				break;
			case "VoteReward":
				if (eventParam1.equals("0")){
					if(eventParam2.equals("0")){
						strHtml = votereward.MainMenuVoteReward(player);
						break;
					}else if(eventParam2.equals("1")){
						strHtml = votereward.MainMenuVoteRewardHopzone(player);
						break;
					}else if(eventParam2.equals("2")){
						strHtml = votereward.MainMenuVoteRewardTopzone(player);
					}else if(eventParam2.equals("3")){
						if(eventParam3.equals("0")){
							strHtml = votereward.MainMenuVoteReward_Pide(player);
						}else if(opera.isNumeric(eventParam3)){
							if(opera.haveItem(player, general.VOTO_ITEM_BUFF_ENCHANT_PRICE)){
								int buffID = Integer.valueOf(eventParam3);
								SkillData.getInstance().getInfo(buffID,315).getEffects(player,player);
								opera.removeItem(general.VOTO_ITEM_BUFF_ENCHANT_PRICE, player);
							}
						}
					}
				}else if(eventParam1.equals("3")){
					strHtml = votereward.HTMLComoVotar(player);
					break;
				}else if(eventParam1.equals("1")){
					if(votereward.checkVoto(Integer.valueOf(eventParam2), eventParam3, Integer.valueOf(eventSplit[4]), player, ( eventParam3.equals("hopzone") ? 1 : 2 )  )){
						if(eventParam3.equals("hopzone")){
							opera.giveReward(player, general.VOTO_REWARD_HOPZONE);
						}else if(eventParam3.equals("topzone")){
							opera.giveReward(player, general.VOTO_REWARD_TOPZONE);
						}
						central.msgbox_Lado("Tu voto fue contabilizado correctamente.", player);
						central.msgbox("Muchas gracias por el voto en " + eventParam3 + ".", player);
					}else{
					}
				}else if(eventParam1.equals("4")){
					if(opera.haveItem(player, general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE)){
						opera.giveReward(player, general.VOTE_SHOW_ZEUS_GIVE_TEMPORAL_ITEM_ID, 1);
						opera.removeItem(general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE, player);
					}
					strHtml = votereward.MainMenuVoteReward(player);
					break;
				}
				htmls.firtsHTML(player, general.npcGlobal(player));
				break;
			case "CastleManagerStr":
				if(eventParam1.equals("0")){
					strHtml = htmls.MainHtmlCastleManager(player);
					break;
				}else{
					int castleId = Integer.valueOf(eventParam1);
				    Castle castle = CastleManager.getInstance().getCastleById(castleId);
				    if(castleId != 0){
				    	player.sendPacket(new SiegeInfo(castle));
				    	strHtml = htmls.MainHtmlCastleManager(player);
						break;
				    }
				}
				break;
	   		case "ReleaseAttribute":
	   			player.sendPacket(new ExShowBaseAttributeCancelWindow(player));
	   			strHtml = "";
				break;
	   		case "SkillList":
	   		case "EnchantSkillList":
	   		case "SafeEnchantSkillList" :
	   		case "UntrainEnchantSkillList" :
	   		case "ChangeEnchantSkillList" :
	   		case "warehouse":
	   			//warehouse DepositC
	   			if(eventParam1.equals("DepositC")){
					player.sendPacket(ActionFailed.STATIC_PACKET);
					player.setActiveWarehouse(player.getClan().getWarehouse());
					player.tempInventoryDisable();
					player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.CLAN));
	   			}else if(eventParam1.equals("WithdrawC")){
					if (Config.L2JMOD_ENABLE_WAREHOUSESORTING_CLAN)
					{
						NpcHtmlMessage msg = new NpcHtmlMessage(Integer.valueOf(general.npcGlobal(player)));
						msg.setFile(player, player.getHtmlPrefix(), "data/html/mods/WhSortedC.htm");
						msg.replace("%objectId%", general.npcGlobal(player));
						player.sendPacket(msg);
					}
					else
					{
						opera.showWithdrawWindow(player, null, (byte) 0);
					}
	   			}
	   			break;
	   		case "increaseclan" ://
	   			strHtml = clan.increaseclan(player);
	   			break;
			case "increase_clan":
				clan.increase_clan(player);
				break;
			case "DisbandClan"://
				strHtml = clan.DisbandClan(player);
				break;
			case "dissolve_clan":
				clan.dissolve_clan(player);
				break;
			case "RestoreClan"://
				strHtml = clan.RestoreClan(player);
				break;
			case "recover_clan":
				clan.recover_clan(player);
				break;
			case "giveclanl"://
				strHtml = clan.giveclanl(player);
				break;
			case "change_clan_leader":
				if(eventParam1.isEmpty()){
					central.msgbox("Ingresa el Nombre del nuevo clan de lider", player);
					strHtml = clan.giveclanl(player);
					break;
				}
				clan.change_clan_leader(player, eventParam1);
				break;
			case "learn_clan_skills"://
				strHtml = clan.learn_clan_skills(player);
				break;
			case "createclan"://
				strHtml = clan.createclan(player);
				break;
			case "create_clan"://
				if(eventParam1.isEmpty()){
					central.msgbox("Debe ingresar el nombre clan", player);
					strHtml= clan.createclan(player);
					break;
				}
				clan.create_clan(player, eventParam1);
				break;
			case "createally"://
				strHtml = clan.createally(player);
				break;
			case "create_ally":
				if(eventParam1.isEmpty()){
					central.msgbox("Dene ingresar el Nombre de ally", player);
					strHtml = clan.createally(player);
					break;
				}
				clan.create_ally(player, eventParam1);
				break;
			case "dissolve_ally"://
				clan.dissolve_clan(player);
				strHtml = "";
				break;
		}
		return strHtml;
	}


}

