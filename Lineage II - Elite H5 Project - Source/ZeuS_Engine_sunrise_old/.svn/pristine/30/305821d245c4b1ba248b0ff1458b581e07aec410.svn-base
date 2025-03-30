package ZeuS.procedimientos;

import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.gameserver.GameServer;
import l2r.gameserver.handler.AdminCommandHandler;
import l2r.gameserver.handler.IAdminCommandHandler;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.olympiad.Participant;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import l2r.gameserver.network.serverpackets.SystemMessage;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.C_gmcommand;
import ZeuS.Config.general;
import ZeuS.GM.fakenpc;
import ZeuS.GM.olymp;
import ZeuS.admin.button;
import ZeuS.admin.menu;
import ZeuS.event.TownWarEvent;
import ZeuS.interfase.antibotSystem;
import ZeuS.interfase.central;
import ZeuS.interfase.desafio;
import ZeuS.interfase.ipblock;
import ZeuS.interfase.shop;
import ZeuS.interfase.teleport;
import ZeuS.language.language;

public class adminHandler implements IAdminCommandHandler
{

	public static void registerHandler(){
		AdminCommandHandler.getInstance().registerHandler(new adminHandlerOpera());
	}

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(adminHandler.class.getName());

	private static class adminHandlerOpera implements IAdminCommandHandler{

		private static boolean canUseCommandOlyPoint(L2PcInstance activeChar){
			int AccessLevel = Arrays.binarySearch(general.OLY_ACCESS_ID_MODIFICAR_POINT, activeChar.getAccessLevel().getLevel() );
			if(AccessLevel < 0){
				return false;
			}
			return true;
		}

		private static final String[] ADMIN_COMMANDS =
		{
			"admin_zeus_help",
			"admin_oly_ban",
			"admin_oly_unban",
			"admin_oly_reset_point",
			"admin_oly_point",
			"admin_oly_help",
			"admin_zeus_tele_help",
			"admin_zeus_tele_manual",
			"admin_zeus_tele_auto",
			"admin_zeus_tele_main_manual",
			"admin_zeus_tele_main_auto",
			"admin_zeus_tele",
			"admin_zeus_shop",
			"admin_z_c_c",
			"admin_zeus_op",
			"admin_zeus_ipblock",
			"admin_zeus_banip",
			"admin_zeus_config",
			"admin_zeus_botzone",
			"admin_zeus_recallAll",
			"admin_zeus_recallAllforce",
			"admin_zeus_buff_voice",
			"admin_zeus",
			"admin_zeus_bot_cancel",
			"admin_zeus_dona",
			"admin_zeus_gmpanel",
			"admin_zeus_fake",
			"admin_zeus_fake_clone",
			"admin_zeus_fake_remove",
			"admin_zeus_townwar_start",
			"admin_zeus_townwar_stop",
			"admin_zeus_townwar_check",
			"admin_zeus_gminvis",
			"admin_zeus_gmvis",
		};

		private static Vector<String> admExplicaCommand = new Vector<String>();

		private static void setExplicaCommand(){
			if(admExplicaCommand.size()==0){
				admExplicaCommand.add("//oly_ban: Ban player from Olys");
				admExplicaCommand.add("//oly_unban: Unban player from Olys");
				admExplicaCommand.add("//oly_reset_point: Reset the Player Olympiad Point to 0");
				admExplicaCommand.add("//oly_point: Give or remove Point to Target player. //oly_point -2 = Remove 2 point from target, //oly_point 2 = give 2 point to target");
				admExplicaCommand.add("//zeus_tele: Show the Teleport Config Menu");
				admExplicaCommand.add("//zeus_shop: Show the Shop Config Menu");
				admExplicaCommand.add("//zeus_ipblock: Show the IP Ban Config Windows");
				admExplicaCommand.add("//zeus_banip: IP Ban to the target Player");
				admExplicaCommand.add("//zeus_botzone: Send a boot check to all char in you zone");
				admExplicaCommand.add("//zeus_recallAll: Recall all char to you location whith radio. //zeus_recall 200");
				admExplicaCommand.add("//zeus_recallAllforce: Recall all char (force all to come) to you location whith radio and. //zeus_recall 200");
				admExplicaCommand.add("//zeus_buff_voice: Show buffer windows config");
				admExplicaCommand.add("//zeus_bot_cancel: Remove antibot windows from target name. //zeus_bot_cancel tester");
				admExplicaCommand.add("//zeus_dona: Donation Config section");
				admExplicaCommand.add("//zeus_gmpanel: Show some Admin Panel");
				admExplicaCommand.add("//zeus_townwar_start: This command start the Town War Event. You need to setting in //zeus_config, townwar menu");
				admExplicaCommand.add("//zeus_townwar_stop: This command stop the Town War Event.");
			}
		}

		@Override
		public boolean useAdminCommand(String command, L2PcInstance activeChar)
		{
			setExplicaCommand();
			String strHtml = "";
			if(command.equals("admin_zeus_gminvis")) {
				activeChar.setInvisible(true);
				activeChar.isZeuSGmVis = false;
				activeChar.broadcastUserInfo(true);
				activeChar.broadcastUserInfo();
				activeChar.decayMe();
				activeChar.spawnMe();				
			}else if(command.equals("admin_zeus_gmvis")) {
				activeChar.isZeuSGmVis = true;
				activeChar.broadcastUserInfo(true);
				activeChar.broadcastUserInfo();
				activeChar.decayMe();
				activeChar.spawnMe();				
			}else if(command.equals("admin_zeus_townwar_start")){
				TownWarEvent.getInstance().startEvent(false);
			}else if(command.equals("admin_zeus_townwar_stop")){
				TownWarEvent.getInstance().endEvent(false);
			}else if(command.equals("admin_zeus_fake")){
				central.msgbox("//zeus_fake_clone = Make the Target npc with all your clothes and class", activeChar);
				central.msgbox("//zeus_fake_remove = Clean the Target fake (Just ZeuS Fake) ", activeChar);
				return true;
			}else if(command.equals("admin_zeus_fake_clone")){
				L2Object target = activeChar.getTarget();
				if(target != null){
					if (target instanceof L2Npc){
						L2Npc targetNpc = (L2Npc)target;
						if(targetNpc != null){
							fakenpc.getInstance().createFakeClone(activeChar, targetNpc, false);
						}
					}
				}
			}else if(command.equals("admin_zeus_fake_remove")){
				L2Object target = activeChar.getTarget();
				if(target != null){
					if (target instanceof L2Npc){
						L2Npc targetNpc = (L2Npc)target;
						if(targetNpc != null){
							fakenpc.getInstance().resetFake(activeChar, targetNpc);
						}
					}
				}				
			}else if(command.startsWith("admin_zeus_bot_cancel")){
				if(command.split(" ").length>1){
					antibotSystem.cancelbotCheck(activeChar, command.split(" ")[1]);
				}else{
					antibotSystem.cancelbotCheck(activeChar, null);
				}
				return true;
			}else if(command.equals("admin_zeus")){
				String EnviarBypass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0";
				cbManager.separateAndSend(Engine.delegar(activeChar, EnviarBypass),activeChar);				
				return true;
			}else if(command.startsWith("admin_zeus_recallAll")){
				opera.summonAll(activeChar,command);
				return true;
			}else if(command.startsWith("admin_zeus_recallAllforce")){
				opera.summonAll(true, activeChar,command);
				return true;
			}else if(command.equals("admin_zeus_botzone")){
				antibotSystem.sendCheckBootZone(activeChar);
				return true;
			}else if(command.equals("admin_zeus_config")){
				String html = menu.getConfigMenu(activeChar);
				if(html.length()>0){
					activeChar.sendPacket(new NpcHtmlMessage(0,html));
				}
			}else if(command.startsWith("admin_zeus_config")){
				String html = "";
				if(command.split(" ")[1].equals("statusCBTN")){
					if(!opera.isNumeric( command.split(" ")[2] )){
						central.msgbox("Error al leer parametro 1", activeChar);
						strHtml ="";
						return true;
					}
					button.status(activeChar, Integer.valueOf(command.split(" ")[2]));
					if(command.split(" ")[3].equals("2")){
						strHtml = button.getBtnCBE(activeChar);
					}
					activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.split(" ")[1].equals("statusCBT2")){
					if(!opera.isNumeric( command.split(" ")[2] )){
						central.msgbox("Error al leer parametro 1", activeChar);
						strHtml ="";
						return true;
					}
					button.status(activeChar, Integer.valueOf(command.split(" ")[2]));
					if(command.split(" ")[3].equals("2")){
						strHtml = button.getBtnCBStatus(activeChar);
					}
					activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.split(" ")[1].equals("GetHTMConfig") ){
					if(command.split(" ")[2].equals("3")){
						strHtml = button.getBtnCBE(activeChar);
					}else if(command.split(" ")[2].equals("4")){
						strHtml = button.getBtnCBStatus(activeChar);
					}
					activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.endsWith("admin_zeus_config Config 2 0 0") ){
					String html2 = menu.getConfigMenu(activeChar);
					activeChar.sendPacket(new NpcHtmlMessage(0,html2));
					return true;
				}else if(command.split(" ")[1].equals("DESAFIO") ){
		   			strHtml = desafio.Desafio(activeChar, command.split(" ")[2], command.split(" ")[3], command.split(" ")[4]);
		   			activeChar.sendPacket(new NpcHtmlMessage(0,strHtml));
					return true;
				}else if(command.split(" ")[1].equals("DESAFIOADD")){
		   			if (command.split(" ")[2].equals("0")){
		   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,-1);
		   				return true;
		   			}
		   			if(!opera.isNumeric(command.split(" ")[3])){
		   				central.msgbox("Solo debes ingresar nómeros en la ID del Item", activeChar);
		   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,Integer.parseInt(command.split(" ")[2]));
		   				return true;
		   			}
		   			if(!opera.isNumeric(command.split(" ")[4])){
		   				central.msgbox("Solo debes ingresar nómeros en la Cantidad", activeChar);
		   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,Integer.parseInt(command.split(" ")[2]));
		   				return true;
		   			}
		   			desafio.AgregarPremios(activeChar, command.split(" ")[2], command.split(" ")[3], command.split(" ")[4]);
	   				strHtml = desafio.menuConfigAddPremiosDesafio(activeChar,Integer.parseInt(command.split(" ")[2]));
	   				return true;
				}else if( (command.split(" ")[1].equals("indirizza") && command.split(" ")[2].equals("gestisci_buff")) ||
						command.split(" ")[1].equals("cambia_gruppo") || command.split(" ")[1].equals("edita_gruppi") ||
						command.split(" ")[1].equals("cambia_set") || command.split(" ")[1].equals("edita_lista_buff") ||
						command.split(" ")[1].equals("edita_buff") || command.split(" ")[1].equals("ordina_buff") ||
						command.split(" ")[1].equals("ordina_lista_buff") || command.split(" ")[1].equals("sposta_su") ||
						command.split(" ")[1].equals("sposta_giu")
						){
						String passDelegar = "";
						for(String parte : command.split(" ")){
							if(!parte.equalsIgnoreCase("admin_zeus_config")){
								passDelegar += parte + " ";
							}
						}
						opera.enviarHTML(activeChar, delegar._delegar(passDelegar.trim(), activeChar));
						return true;
				}else{
					int pri = 0;
					String newCommand = "";
					for (String coma  : command.split(" ")){
						if(pri==1){
							newCommand += coma + " ";
						}else{
							pri=1;
						}
					}

					html = menu.bypass(activeChar,newCommand.trim());
				}
				if(html.length()>0){
					activeChar.sendPacket(new NpcHtmlMessage(0,html));
				}
				return true;
			}else if(command.startsWith("admin_zeus_gmpanel")){
				int aLevel = activeChar.getAccessLevel().getLevel();
				if(aLevel!=8){
					central.msgbox("You dont have right to do this.", activeChar);
					return false;
				}
				String Retorno = C_gmcommand.bypass(activeChar, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.admCommand.name() + ";0;PREMIUM;0;0;0;0;0");
				cbManager.separateAndSend(Retorno, activeChar);
			}else if(command.startsWith("admin_zeus_banip")){
				ipblock.bypass(command, activeChar);
			}else if(command.equals("admin_zeus_ipblock")){
				ipblock.sendhtmlBLockIP(activeChar, 0);
			}else if(command.startsWith("admin_zeus_op")){
				indelegar(command,activeChar);
			}else if(command.startsWith("admin_zeus_shop")){
				shop.ShopByPass(activeChar,command);
				return true;
			}else if(command.equalsIgnoreCase("admin_zeus_tele")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.CentralHTML(activeChar,true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_main_manual")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleportMain(activeChar, "");
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_main_auto")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleportMain(activeChar, "",true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));

				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_manual")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleport(activeChar, "");
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.equalsIgnoreCase("admin_zeus_tele_auto")){
				if(opera.isMaster(activeChar)){
					String HTML = teleport.adminTeleport(activeChar, "",true);
					activeChar.sendPacket(new NpcHtmlMessage(0,HTML));
				}
			}else if(command.startsWith("admin_zeus_tele_help")){
				String Concatenacion = "";
				for(String Comando: ADMIN_COMMANDS){
					if(Concatenacion.length()!=0){
						Concatenacion+=", ";
					}
					if(Comando.startsWith("admin_zeus_tele")){
						Concatenacion += Comando.replace("admin_", "//");
					}
				}
				central.msgbox(Concatenacion, activeChar);
			}else if (command.startsWith("admin_oly_ban")){
				StringTokenizer st = new StringTokenizer(command, " ");
				command = st.nextToken();

				if (st.hasMoreTokens())
				{
					L2PcInstance player = null;
					String playername = st.nextToken();
					try
					{
						player = L2World.getInstance().getPlayer(playername);
					}
					catch (Exception e)
					{
					}

					if (player != null)
					{
						olymp.banOlys(activeChar, player);
						return true;
					}
					activeChar.sendMessage("The player " + playername + " is not online");
					return false;
				}
				else if ((activeChar.getTarget() != null) && (activeChar.getTarget() instanceof L2PcInstance))
				{
					olymp.banOlys(activeChar, (L2PcInstance) activeChar.getTarget());
					return true;
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}
			}
			else if (command.startsWith("admin_oly_unban"))
			{
				StringTokenizer st = new StringTokenizer(command, " ");
				command = st.nextToken();

				if (st.hasMoreTokens())
				{
					L2PcInstance player = null;
					String playername = st.nextToken();
					try
					{
						player = L2World.getInstance().getPlayer(playername);
					}
					catch (Exception e)
					{
					}

					if (player != null)
					{
						olymp.unbanOlys(activeChar, player);
						return true;
					}
					activeChar.sendMessage("The player " + playername + " is not online");
					return false;
				}
				else if ((activeChar.getTarget() != null) && (activeChar.getTarget() instanceof L2PcInstance))
				{
					olymp.unbanOlys(activeChar, (L2PcInstance) activeChar.getTarget());
					return true;
				}
				else
				{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}
			}
			else if (command.startsWith("admin_oly_reset_point"))
			{
				if(!canUseCommandOlyPoint(activeChar)){
					central.msgbox("You do not have access to this command", activeChar);
					return false;
				}
				L2Object target = activeChar.getTarget();
				if(target instanceof L2PcInstance ){
					if(((L2PcInstance) target).isNoble()){
						L2PcInstance playerTarget = (L2PcInstance)target;
						Participant par = new Participant(playerTarget,1);
						olymp.point(par, true);
						central.msgbox_Lado("GM " + activeChar.getName() + " as reset your Olympics Points", playerTarget);
						central.msgbox("Points have been reset to " + playerTarget.getName() , activeChar);
					}else{
						activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
						return false;
					}
				}else{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}

			}
			else if (command.startsWith("admin_oly_point"))
			{
				if(!canUseCommandOlyPoint(activeChar)){
					central.msgbox("You do not have access to this command", activeChar);
					return false;
				}
				int pointGive = 0;
				StringTokenizer st = new StringTokenizer(command, " ");
				command = st.nextToken();
				String Puntos = "";
				if (st.hasMoreTokens()){
					Puntos = st.nextToken();
					if(!central.isNumeric(Puntos)){
						central.msgbox(language.getInstance().getMsg(activeChar).ENTER_NUMBERS_ONLY, activeChar);
						return false;
					}
					pointGive = Integer.valueOf(Puntos);
				}else{
					central.msgbox("Need to include the points to give or take", activeChar);
					return false;
				}


				L2Object target = activeChar.getTarget();
				if(target instanceof L2PcInstance ){
					if(((L2PcInstance) target).isNoble()){
						L2PcInstance playerTarget = (L2PcInstance)target;
						Participant par = new Participant(playerTarget,1);
						olymp.point(par, pointGive);
						central.msgbox_Lado("GM " + activeChar.getName() + " has put "+ String.valueOf(pointGive) +" Olympiad points", playerTarget);
						central.msgbox("You put "+ String.valueOf(pointGive) + " Olmpiad points to " + playerTarget.getName(),activeChar);
					}else{
						activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
						return false;
					}
				}else{
					activeChar.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
					return false;
				}
			}else if(command.startsWith("admin_zeus_help")){
				for(String Comando: admExplicaCommand){
					central.msgbox(Comando, activeChar);
				}

			}
			return true;
		}
		@Override
		public String[] getAdminCommandList()
		{
			return ADMIN_COMMANDS;
		}
	}

	protected static void indelegar(String Comando, L2PcInstance player){
		if(Comando.startsWith("admin_zeus_op")){
			String[] Params = Comando.split(" ");
			if(Params.length>1){
				if(Params[1].equals("ipblock")){
					ipblock.bypass(Comando, player);
				}else if(Params[1].equals("teleaddmain1")){
					String HTML = teleport.adminTeleportMain(player, "",true);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("teleaddmain2")){
					String HTML = teleport.adminTeleportMain(player, "");
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("teleadd1")){
					String HTML = teleport.adminTeleport(player, "",true);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("teleadd2")){
					String HTML = teleport.adminTeleport(player, "");
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("telesection1")){
					String HTML = teleport.adminTeleport(player,"",true,false,Params[2]);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("telesection2")){
					String HTML = teleport.adminTeleport(player,"",false,false,Params[2]);
					player.sendPacket(new NpcHtmlMessage(0,HTML));
				}else if(Params[1].equals("tele_update")){
					teleport.bypassMain_edit(Comando, player);
				}else if(Params[1].endsWith("coliseevent")){
					teamEvent.bypass(Comando, player);
				}
			}
		}
	}
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar) {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public String[] getAdminCommandList() {

		return null;
	}

}