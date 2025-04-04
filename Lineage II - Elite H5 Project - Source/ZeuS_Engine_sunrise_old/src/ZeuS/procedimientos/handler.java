package ZeuS.procedimientos;

import java.util.logging.Logger;

import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.handler.VoicedCommandHandler;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.util.Broadcast;
import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.EngineForm.C_oly_buff;
import ZeuS.Comunidad.EngineForm.v_Dressme;
import ZeuS.Comunidad.EngineForm.v_donation;
import ZeuS.Comunidad.EngineForm.C_findparty;
import ZeuS.Config._dealy_reward_system;
import ZeuS.Config.general;
import ZeuS.Instances.VoteInstance;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.EmailRegistration;
import ZeuS.interfase.accountRecovery;
import ZeuS.interfase.antibotSystem;
import ZeuS.interfase.borrowAccount;
import ZeuS.interfase.central;
import ZeuS.interfase.changeCharAccount;
import ZeuS.interfase.changePassword;
import ZeuS.interfase.charPanel;
import ZeuS.interfase.fixMe;
import ZeuS.interfase.borrowAccountSystem;
import ZeuS.interfase.sellAccount;
import ZeuS.interfase.sellBuff;
import ZeuS.interfase.sellClan;
import ZeuS.interfase.shop;
import ZeuS.interfase.showMyStat;
import ZeuS.interfase.teleport;
import ZeuS.interfase.votereward;
import ZeuS.interfase.wishList;
import ZeuS.language.language;
import ZeuS.server.comun;
import ZeuS.tutorial.SecondaryPassword;



public class handler implements IVoicedCommandHandler {

	@SuppressWarnings("unused")
	private final Logger _log = Logger.getLogger(handler.class.getName());
	
	public static void registerHandler(){
		VoicedCommandHandler.getInstance().registerHandler(new VoicedHandler());
	}
	public static String []getCommand(){
		return VOICED_COMMANDS;
	}
	
	private static final String[] VOICED_COMMANDS = {"zeus",
		"zeus_buffer",
		"dressme",
		"teamevent",
		"report",
		"cancelreport",
		"zeusBoot",
		"z_c_c",
		"z_t_a",
		"z_t_m",
		"z_t",
		"zeusvote",
		"expon",
		"expoff",
		"exp_on",
		"exp_off",			
		"stat",
		"configpanelchar",
		"RegisEmailCMB",
		"CharPnl",
		"charpanel",
		"fixme",
		"fixmeCharName",
		"myinfo",
		"zeus_raid_start",
		"joinraid",
		"leaveraid",
		"acc_register",
		"changepassword",
		"online",
		"makeancientadena",
		"townwar_check",
		"buffstore",
		"party",
		"obsr",
		"oly_buff",
		"email",
		"tutorial",
		"combinetalisman",
		"changeemail",
		"removesecondarypassword",
		"sellaccount",
		"sellclan",
		"accrecovery",
		"movechar",
		"afk",
		"changelang",
		"votezone",
		"close",
		"autocp",
		"automp",
		"autohp",
		"mywishlist",
		"borrow_account"
		};	


	private static class VoicedHandler implements IVoicedCommandHandler {
		
		
		@SuppressWarnings("unused")
		private static final Logger _log = Logger.getLogger(sellBuff.class.getName());
		
		
		/**
		 *
		 * @see l2r.gameserver.handler.IVoicedCommandHandler#getVoicedCommandList()
		 */
		@Override
		public String[] getVoicedCommandList() {
			return VOICED_COMMANDS;
		}

		/**
		 *
		 * @see l2r.gameserver.handler.IVoicedCommandHandler#useVoicedCommand(java.lang.String,
		 *      l2r.gameserver.model.actor.instance.L2PcInstance,
		 *      java.lang.String)
		 */
		@Override
		public boolean useVoicedCommand(String command,	L2PcInstance activeChar, String params){
			if(activeChar!=null){
				general._byPassVoice.put(activeChar.getObjectId(), opera.getUnixTimeL2JServer());
			}
			
			if(!SecondaryPassword.isValid(activeChar) && !command.equals("RegisEmailCMB")){
				return false;
			}
			
			if(command.equals("dealylogin")) {
				if(activeChar == null) {
					return false;
				}					
				_dealy_reward_system.playerRegister(activeChar);
				CBByPass.byPass(activeChar, general.getCOMMUNITY_BOARD_PART_EXEC());
 				return true;
			}else if(command.equals("borrow_account")) {
				if(borrowAccount.getInstance().isBorrowActice(activeChar.getAccountName())) {
					activeChar.sendMessage(language.getInstance().getMsg(activeChar).BORROW_SYSTEM_BLOCKED);
					return true;
				}				
				borrowAccountSystem.getInstance().sendBorrowWindows(activeChar);
				return true;
			}else if(command.equals("mywishlist")){
				if(!general.STATUS_AUCTIONHOUSE && !general.STATUS_BIDHOUSE) {
					central.msgbox(language.getInstance().getMsg(activeChar).DISABLE_BY_ADMIN , activeChar);
					return true;
				}
				wishList.showMenuWindows(activeChar, "");
				return true;
			}else if(command.equals("autocp")){
				autoPots.setCp(activeChar);
				if(params!=null){
					if(params.equals("Show")){
						charPanel.delegar(activeChar, params);
					}
				}
				return true;
			}else if(command.equals("automp")){
				autoPots.setMana(activeChar);
				if(params!=null){
					if(params.equals("Show")){
						charPanel.delegar(activeChar, params);
					}
				}				
				return true;
			}else if(command.equals("autohp")){
				autoPots.setHp(activeChar);
				if(params!=null){
					if(params.equals("Show")){
						charPanel.delegar(activeChar, params);
					}
				}				
				return true;
			}else if(command.equals("close")){
				return true;
			}else if (command.equals("afk")){
				Afk.setAFK(activeChar);
				return true;
			}else if(command.equals("votezone")){
				VoteInstance.checkVote(Integer.valueOf(params),true);
				return true;
			}else if(command.equals("changelang")){
				language.getInstance().showLanguageWindows(activeChar);
				return true;
			}else if(command.equals("movechar")){
				changeCharAccount.ShowFirtWindows(activeChar);
				return true;
			}else if(command.equals("accrecovery")){
				accountRecovery.showRecoveryWindow(activeChar);
				return true;
			}else if(command.equals("sellclan")){
				sellClan.showSellConfig(activeChar);
				return true;
			}else if(command.equals("sellaccount")){
				sellAccount.showSellConfig(activeChar);
				return true;
			}else if(command.equals("removesecondarypassword")){
				SecondaryPassword.showRemoveWindos(activeChar);
				return true;
			}else if(command.equals("combinetalisman")){
				opera.combineTalismanFromPlayer(activeChar);
				return true;
			}else if(command.equals("tutorial")){
				final NpcHtmlMessage html = comun.htmlMaker(activeChar, general.DIR_HTML + "/" + language.getInstance().getFolder(activeChar) + "/" +"test.html");
				central.sendHtml(activeChar, html.getHtml(), true);
				return true;
			}else if(command.equals("oly_buff")){
				if(!general.OLY_CAN_USE_SCHEME_BUFFER){
					central.msgbox(language.getInstance().getMsg(activeChar).DISABLE_BY_ADMIN, activeChar);
					return false;
				}
				
				if(!activeChar.isNoble()){
					central.msgbox("This command is only for noble player", activeChar);
					return true;
				}
				if(opera.canUseCBFunctionOlyBuff(activeChar)){
					String Enviar = C_oly_buff.bypass(activeChar, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.OlyBuffer.name() + ";0;0;0;0;0;0");
					cbManager.separateAndSend(Enviar, activeChar);
				}else{
					central.msgbox("This command are blocked on this Area, Zone or Time", activeChar);
				}
			}else if(command.equals("party")){
				C_findparty.sendRequestToAll(activeChar,params);
				return true;
			}else if(command.equals("buffstore")){
				if(!general.ALLOW_BUFFSTORE){
					central.msgbox(language.getInstance().getMsg(activeChar).DISABLE_BY_ADMIN, activeChar);
					return true;
				}
				if(sellBuff.isBuffSeller(activeChar)){
					sellBuff.setStopSellBuff(activeChar);
					return true;
				}else{
					//sellBuff.setBuffSell(activeChar, 500L);
					sellBuff.showMainWindows(activeChar, true, "");
					return true;
				}
			}else if(command.equals("makeancientadena")){
				long Ancient_a_Dar = 0l;
				boolean Transformo = false;
				if(opera.haveItem(activeChar, 6360)){
					Transformo=true;
					int BlueSealStone = (int) opera.ContarItem(activeChar, 6360);
					Ancient_a_Dar = ( BlueSealStone * 3 );
					opera.giveReward(activeChar, 5575, (int) Ancient_a_Dar);
					opera.removeItem(6360, BlueSealStone, activeChar);
				}
				if(opera.haveItem(activeChar, 6361)){
					Transformo=true;
					int GreenSealStone = (int) opera.ContarItem(activeChar, 6361);
					Ancient_a_Dar = ( GreenSealStone * 5 );
					opera.giveReward(activeChar, 5575, (int) Ancient_a_Dar);
					opera.removeItem(6361, GreenSealStone, activeChar);					
				}
				if(opera.haveItem(activeChar, 6362)){
					Transformo=true;
					int RedSealStone = (int) opera.ContarItem(activeChar, 6362);
					Ancient_a_Dar = ( RedSealStone * 10 );
					opera.giveReward(activeChar, 5575, (int) Ancient_a_Dar);
					opera.removeItem(6362, RedSealStone, activeChar);					
				}
				if(Transformo){
					central.msgbox("Process Finish", activeChar);
				}else{
					central.msgbox("You don't have any Seal Stones to exchange.", activeChar);
				}
			}else if(command.equals("online")){
				if(!general.COMMUNITY_BOARD_REGION){
					int onlinePlayer = opera.getOnlinePlayer();
					central.msgbox("-----------------------------", activeChar);
					central.msgbox( "Number of Player(s): " + String.valueOf(onlinePlayer + general.PLAYER_BASE_TO_SHOW) , activeChar);
					central.msgbox("-----------------------------", activeChar);
				}else{
					central.msgbox("To see this information please use alt+b Region Option.",activeChar);
				}
			}else if(command.equals("changepassword")){
				changePassword.bypass(activeChar, "");
				return true;
			}else if(command.equals("acc_register")){
				EmailRegistration.getRegistrationWindos(activeChar, true, false);
				return true;
			}else if(command.equals("changeemail")){
				EmailRegistration.getRegistrationWindos(activeChar, true, true);
				return true;				
			}else if(command.equals("leaveraid")){
				RaidBossEvent.removePlayer(activeChar);
				return true;
			}else if(command.equals("joinraid")){
				RaidBossEvent.addPlayer(activeChar);
				return true;
			}else if(command.equals("zeus_raid_start")){
				if(!activeChar.isGM()){
					return false;
				}
				RaidBossEvent.autoEvent();
				return true;
			}else if(command.equals("myinfo")){
				charPanel.getCharInfo(activeChar);
				return true;
			}else if(command.startsWith("fixmeCharName")){
				fixMe.delegar(activeChar, params);
				return true;
			}else if(command.equals("fixme")){
				fixMe.delegar(activeChar, "");
				return true;
			}else if(command.equals("charpanel")){
				if(!general.CHAR_PANEL){
					central.msgbox("Char Panel disabled", activeChar);
					return true;
				}
				charPanel.delegar(activeChar, "");
				return true;
			}else if(command.equals("CharPnl")){
				charPanel.delegar(activeChar, params);
				return true;
			}else if(command.equals("RegisEmailCMB")){
				EmailRegistration.baypass(activeChar, params);
				return true;
			}else if(command.equals("zeus")){
				String EnviarBypass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.commandinfo.name() + ";0;0;0;0;0;0";
				cbManager.separateAndSend(Engine.delegar(activeChar, EnviarBypass),activeChar);
				return true;
			}else if(command.equals("dressme")){
				String CBHtml = v_Dressme.bypass(activeChar, Engine.enumBypass.Dressme.name()+";0;0;0;0;0;0;0;0");
				cbManager.separateAndSend(CBHtml, activeChar);
				return true;
			}else if(command.equals("teamevent")){
				if(opera.isMaster(activeChar)){
					teamEvent.getHTMLAdminConfig(activeChar);
				}
			}else if(command.startsWith("cancelreport")){
				antibotSystem.cancelbotCheck(activeChar, params);
			}else if(command.startsWith("report")){
				antibotSystem.bypass(activeChar, command + " " + params);
			}else if(command.startsWith("zeusBoot")){
				antibotSystem.bypass(activeChar, command + " " + params);
			}else if(command.equalsIgnoreCase("z_c_c")){
				if(opera.isMaster(activeChar)){
					shop.ShopByPass(activeChar, command);
				}
			}else if(command.equalsIgnoreCase("z_t_a")){
				if(opera.isMaster(activeChar)){
					teleport.bypassMain_edit(params, activeChar);
				}
			}else if(command.equalsIgnoreCase("z_t_m")){
				if(opera.isMaster(activeChar)){
					teleport.bypassMain(params, activeChar);
				}
			}else if(command.equalsIgnoreCase("z_t")){
				if(opera.isMaster(activeChar)) {
					teleport.bypassTeleport(params, activeChar);
				}
			}else if (command.equalsIgnoreCase("zeusvote")) {

			}else if (command.equalsIgnoreCase("expon") || command.equalsIgnoreCase("exp_on")) {
				if(!general.RATE_EXP_OFF){
					central.msgbox_Lado("Command disabled by Admin", activeChar);
					return true;
				}
				general.getInstance().setConfigExpSp(activeChar,true);
				central.msgbox_Lado(language.getInstance().getMsg(activeChar).EXPERIENCE_ON, activeChar);
			}else if(command.equalsIgnoreCase("expoff") || command.equalsIgnoreCase("exp_off")){
				if(!general.RATE_EXP_OFF){
					central.msgbox_Lado("Command disabled by Admin", activeChar);
					return true;
				}
				general.getInstance().setConfigExpSp(activeChar,false);
				central.msgbox_Lado(language.getInstance().getMsg(activeChar).EXPERIENCE_OFF, activeChar);
			}else if(command.equals("stat")){
				L2Object target = activeChar.getTarget();
				if(target instanceof L2PcInstance ){
					showMyStat.showMyStats((L2PcInstance) target, activeChar);
				}else{
					central.msgbox(language.getInstance().getMsg(activeChar).INVALID_TARGER_IS_NOT_A_PLAYER, activeChar);
				}
			}
			return true;
		}
	}


	@Override
	public String[] getVoicedCommandList() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean useVoicedCommand(String arg0, L2PcInstance arg1, String arg2) {
		// TODO Auto-generated method stub
		return false;
	}
}
