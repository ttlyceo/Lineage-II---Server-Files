package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.sendC;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.Anunc;
import ZeuS.server.comun;

public class antibotSystem {
	private static final Logger _log = Logger.getLogger(antibotSystem.class.getName());
	private static Map<L2PcInstance, Integer> Player_UnixTime = new HashMap<L2PcInstance, Integer>();
	private static Map<L2PcInstance, Integer> PlayerCodigToJail = new HashMap<L2PcInstance, Integer>();
	private static Map<L2PcInstance, Integer> PlayerVecesEquivocado = new HashMap<L2PcInstance, Integer>();
	
	private static Map<Integer, String> ICON_CAPTCHA = new HashMap<Integer, String>();
	private static Map<Integer, Integer> ID_ICON_CAPTCHA_PLAYER = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> ID_SELECT = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> ATTEMPT_NUMBERS = new HashMap<Integer, Integer>();
	
	private static Map<L2PcInstance, _autoClock> PLAYER_AUTOCLOCK = new HashMap<L2PcInstance, _autoClock>();
	
	private static Vector<String> ALL_ICON_CAPTCHA = new Vector<String>(); 
	
	public static void setIcons(Vector<String>AllIcons){
		ALL_ICON_CAPTCHA = AllIcons;
	}
	
	private static void CreateFastIcon(){
		if(ICON_CAPTCHA.size()>0){
			return;
		}
		int contador = 0;
		for(String Icon : ALL_ICON_CAPTCHA){
			ICON_CAPTCHA.put(contador, Icon);
			contador++;
		}
	}
	
	private static int getUnixTime(){
		int unixTime = (int) (System.currentTimeMillis()/1000);
		return unixTime;
	}
	
	private static int getBlackListCounter(L2PcInstance player){
		int idPlayer = player.getObjectId();
		int Retorno = 0;
		String Query = "SELECT zeus_antibot_blacklist.veces FROM zeus_antibot_blacklist WHERE zeus_antibot_blacklist.idchar = ?";
		try {
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Query);
			statement.setInt(1, idPlayer);
			ResultSet rset = statement.executeQuery();
			if(rset.next()){
				Retorno = rset.getInt(1);
			}
			rset.close();
		}catch (Exception e){
			Retorno = 0;
			_log.warning("Oly_Monuments Error: Error while Load Top Heroes: " + e.getMessage());
		}
		return Retorno;
	}

	private static int blackListedChar(L2PcInstance cha){
		int retorno = 1;

		String NombreChar = cha.getName();
		int IdChar = cha.getObjectId();
		
		String qry = "call sp_antibot_blacklist(?,?)";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			psqry.setInt(1, IdChar);
			psqry.setString(2, NombreChar);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("-->Antibot blacklist error:"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("-->Antibot blacklist error:"+a.getMessage());
		}

		if(Respu.trim().length()>0){
			if (opera.isNumeric(Respu)){
				retorno = Integer.valueOf(Respu);
			}
		}
		
		return retorno;
	}
	
	private static String getAllPlayerNameOnThisRed(L2PcInstance player){
		String Nombres = "";
		if(!general.ANTIBOT_SEND_JAIL_ALL_DUAL_BOX){
			return "";
		}
		
		Vector<L2PcInstance> PJ = opera.getAllPlayerOnThisIp(player);
		
		if(PJ==null){
			return "";
		}
		
		if(PJ.size()==0){
			return "";
		}
		
		for(L2PcInstance cha : PJ){
			try{
				if(cha != null){
					if(cha.isOnline()){
						if(!cha.getClient().isDetached()){
							if(Nombres.length()>0){
								Nombres += ", " + cha.getName();
							}
						}
					}
				}
			}catch(Exception a){
				
			}
		}
		
		return Nombres;
	}
	

	public static void sendAllPlayerJailByIP(L2PcInstance player, int MinutesInJail){
		if(!general.ANTIBOT_SEND_JAIL_ALL_DUAL_BOX){
			return;
		}
		Vector<L2PcInstance> PJ = opera.getAllPlayerOnThisIp(player);
		if(PJ==null){
			return;
		}
		
		if(PJ.size()==0){
			return;
		}
		
		for(L2PcInstance cha : PJ){
			try{
				if(cha != null){
					if(cha.isOnline() && !cha.isGM()){
						if(!cha.getClient().isDetached()){
							_log.warning("Antibot System has Jailed "+cha.getName()+" becouse the main " + player.getName()+" are Using Bot.");
							opera.sendToJail(cha, MinutesInJail);
						}
					}
				}
			}catch(Exception a){
				
			}
		}
	}
	
	public static void setLastKillTime(L2PcInstance player){
		try{
			Player_UnixTime.put(player, getUnixTime());
		}catch(Exception a){
			_log.warning("ZeuS Error getting last time kill -> " + a.getMessage());
		}
	}

	public static void cancelbotCheck(L2PcInstance player, String parametros){
		if(!player.isGM()){
			return;
		}

		L2PcInstance chaTarget = null;

		if(parametros==null){
			L2Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				chaTarget = (L2PcInstance)target;
			}
		}else{
			try{
				chaTarget = L2World.getInstance().getPlayer(parametros);
			}catch (Exception e){
			}

			if (chaTarget == null){
				player.sendMessage("The player " + parametros + " is not online or not exist");
			}
		}

		if(chaTarget!=null){
			if(chaTarget != player){
				if(general.isBotCheckPlayer(chaTarget)){
					cleanPlayer(chaTarget);
					central.msgbox("GM has remove the antibot check from you", chaTarget);
					central.msgbox("You remove the antibot check from " + chaTarget.getName(), player);
					String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
					HTML += central.LineaDivisora(1) + central.headFormat("Antibot System") + central.LineaDivisora(1);
					HTML += central.LineaDivisora(1) + central.headFormat("You antibot check was removed By GM.","LEVEL") + central.LineaDivisora(1);
					HTML += central.LineaDivisora(1) + central.getPieHTML() + "</body></html>";
					central.sendHtml(chaTarget, HTML);
				}
			}
		}

	}

	private static void sendAllCharInZone(L2PcInstance player){
		//opera.isInGeoZone
		int contador = 0;
		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();
		for(L2PcInstance onlinePlayer : pls){
			if(opera.isInGeoZone(onlinePlayer, player)){
				if(!onlinePlayer.isGM()){
					if(isPlayerValid(onlinePlayer)){
						PlayerVecesEquivocado.put(player, 0);
						contador++;
						setCheckBot(onlinePlayer, player.getName(),0);
						central.msgbox(language.getInstance().getMsg(player).BOT_VERIFICATION_SEND_TO_$player.replace("$player",onlinePlayer.getName()), player);
					}
				}
			}
		}
		central.msgbox("report send to " + String.valueOf(contador) + " player in you zone." , player);
	}

	public static void sendCheckBootZone(L2PcInstance player){
		sendAllCharInZone(player);
	}


	public static void checkSendAntibot(L2PcInstance player){
		if(player.isPhantom())
			return;
		int getMobKill = general.getMobKillAntibot(player);
		if(getMobKill >= general.ANTIBOT_MOB_DEAD_TO_ACTIVATE){
			PlayerVecesEquivocado.put(player, 0);
			checkboot(player,true);
		}
	}

	protected static boolean isPlayerValid(L2PcInstance player){
		return isPlayerValid(player, true, true);
	}


	protected static boolean isPlayerValid(L2PcInstance player, boolean ShowMensaje,boolean respetarConfigZeuS){
		Object target = player.getTarget();


		if(!(target instanceof L2PcInstance)){
			central.msgbox(language.getInstance().getMsg(player).INVALID_TARGER_IS_NOT_A_PLAYER, player);
			return false;
		}
		L2PcInstance targetChar = (L2PcInstance)target;

		if(!player.isGM() || !player.isPhantom()){
			if(Player_UnixTime!=null){
				if(Player_UnixTime.containsKey(target)){
					int ActualUnix = getUnixTime();
					int TiempoTranscurrido = ActualUnix - Player_UnixTime.get(target);
					if(TiempoTranscurrido >= ( general.ANTIBOT_INACTIVE_MINUTES * 60 ) ){
						if(ShowMensaje){
							central.msgbox(language.getInstance().getMsg(player).BOT_INACTIVE_X_THAN_$minutes.replace("$minutes", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES)), player);
						}
						return false;
					}
				}else{
					if(ShowMensaje){
						central.msgbox(language.getInstance().getMsg(player).BOT_INACTIVE_X_THAN_$minutes.replace("$minutes", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES)), player);
					}
					return false;
				}
			}else{
				if(ShowMensaje){
					central.msgbox(language.getInstance().getMsg(player).BOT_INACTIVE_X_THAN_$minutes.replace("$minutes", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES)), player);
				}
				return false;
			}
		}

		if(targetChar.isSitting()){
			central.msgbox("Player is Sitting", player);
			return false;
		}
		
		if(targetChar.isOlympiadStart() || player.isOlympiadStart() || targetChar.isInOlympiadMode() || player.isInOlympiadMode()){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_CAN_NOT_SEND_IN_OLY, player);
			}
			return false;
		}

		if(targetChar.equals(player)){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_CAN_NOT_SEND_IN_YOUR_SELF, player);
			}
			return false;
		}

		if(targetChar.isGM()){
			if(ShowMensaje){
				central.msgbox("This command not work on adm/gm", player);
			}
			return false;
		}

		if(opera.isMaster(player)){
			return true;
		}



		if(general.ANTIBOT_CHECK_DUALBOX){
			if(ZeuS.isDualBox_pc(player, targetChar)){
				if(ShowMensaje){
					central.msgbox(language.getInstance().getMsg(player).BOT_CAN_NOT_SEND_IN_YOUR_SELF, player);
				}
				return false;
			}
		}

		if(!general.ANTIBOT_COMANDO_STATUS && !player.isGM()){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			}
			return false;
		}

		if(targetChar.isGM()){
			if(ShowMensaje){
				central.msgbox("This command not work on adm/gm", player);
			}
			return false;
		}

		if(respetarConfigZeuS){
			if(general.getTime_antibot(targetChar) > opera.getUnixTimeNow()){
				if(ShowMensaje){
					int NextCheck = general.getTime_antibot(targetChar) - opera.getUnixTimeNow();
					int Minutos = NextCheck/60;
					String strNextCheck = String.valueOf(Minutos) + ":" + String.valueOf( NextCheck - (Minutos * 60) );
					central.msgbox(language.getInstance().getMsg(player).BOT_RECENTLY_BEEN_SENT_TO_THIS_PLAYER_VERIFICATION_EVERY_$timeEvery_NEXT_CHECK_IN_$timeNextCheck.replace("$timeEvery", String.valueOf(general.ANTIBOT_MINUTE_VERIF_AGAIN).replace("$timeNextCheck", strNextCheck) ), player);
				}
				return false;
			}
			if(general.ANTIBOT_NOBLE_ONLY && !player.isNoble()){
				if(ShowMensaje){
					central.msgbox(language.getInstance().getMsg(player).BOT_THIS_COMMAND_ONLY_NOBLE, player);
				}
				return false;
			}

			if(general.ANTIBOT_HERO_ONLY && !player.isHero()){
				if(ShowMensaje){
					central.msgbox(language.getInstance().getMsg(player).BOT_THIS_COMMAND_ONLY_HERO, player);
				}
				return false;
			}

			if(general.ANTIBOT_GM_ONLY && !player.isGM()){
				if(ShowMensaje){
					central.msgbox("This command is GM Only", player);
				}
				return false;
			}

			if(general.ANTIBOT_MIN_LVL>player.getLevel()){
				if(ShowMensaje){
					central.msgbox(language.getInstance().getMsg(player).BOT_COMMAND_IS_ONLY_FOR_PLAYER_WHITH_$level.replace("$level", String.valueOf(general.ANTIBOT_MIN_LVL)), player);
				}
				return false;
			}

			if(general.ANTIBOT_ANTIGUEDAD_MINUTOS!=0){
				if (opera.getMinutosVida(player) < general.ANTIBOT_ANTIGUEDAD_MINUTOS){
					if(ShowMensaje){
						central.msgbox(language.getInstance().getMsg(player).BOT_COMMAND_IS_ONLY_WITH_LIFETIME_OVER_$lifetime.replace("$lifetime", String.valueOf(general.ANTIBOT_ANTIGUEDAD_MINUTOS)), player);
					}
					return false;
				}
			}
		}

		Object targetFromTarget = targetChar.getTarget();

		if(targetFromTarget instanceof L2Npc){
			L2Npc tfTarget = (L2Npc)targetFromTarget;
			if(tfTarget.isRaid()){
				if(ShowMensaje){
					central.msgbox(language.getInstance().getMsg(player).BOT_THIS_PLAYER_ARE_KILLING_A_RAID_BOSS, player);
				}
				return false;
			}
		}


		if(targetChar.getKarma()>0){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_THIS_PLAYER_HAVE_KARMA, player);
			}
			return false;
		}

		if(targetChar.isInSiege()){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK, player);
			}
			return false;
		}

		if (targetChar.isInsideZone(ZoneIdType.TOWN)){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK, player);
			}
			return false;
		}

		if(targetChar.isInsideZone(ZoneIdType.PEACE) && !general.ANTIBOT_CHECK_INPEACE_ZONE){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK, player);
			}
			return false;
		}

		if((targetChar.getPvpFlag() != 0) || targetChar.isCombatFlagEquipped()){
			if(ShowMensaje){
				central.msgbox(language.getInstance().getMsg(player).BOT_TARGET_PLAYER_CAN_RECEIVE_BOT_CHECK, player);
			}
			return false;
		}

		return true;
	}

	public static void checkbootenterW(L2PcInstance player){
		if(!isPlayerValid(player)){
			return;
		}
		sendJail(player);
		central.msgbox(language.getInstance().getMsg(player).BOT_SYSTEM_SEND_TO_JAIL_FOR_$time_MINUTES_FOR_LOGOUT.replace("$time", String.valueOf(general.ANTIBOT_MINUTOS_JAIL)), player);
	}

	public static void checkboot(L2PcInstance player){
		checkboot(player,false);
	}


	public static void checkboot(L2PcInstance player, boolean isAuto){
		if(!isPlayerValid(player) && !isAuto){
			return;
		}

		L2PcInstance PlayerCheck = null;

		if(isAuto){
			PlayerCheck = player;
		}else{
			L2Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				PlayerCheck = (L2PcInstance)target;
			}else{
				return;
			}
		}
		PlayerVecesEquivocado.put(player, 0);
		setCheckBot(PlayerCheck, player.getName(),0);
		if(!isAuto){
			central.msgbox(language.getInstance().getMsg(player).BOT_VERIFICATION_SEND_TO_$player.replace("$player", PlayerCheck.getName()), player);
		}else{
			general.resetKillAntibot(PlayerCheck);
		}
	}

	protected static int getPosi_ID(String idPregunta){
		int retorno = 0;
		int Contador = 0;
		for(String[] Pregunta : general.PREGUNTAS_BOT){
			if(Pregunta[0]!=null){
				if(Pregunta[0].equals(idPregunta)){
					return Contador;
				}
				Contador++;
			}
		}

		return retorno;
	}

	public static void bypass(L2PcInstance player, String command){

		if(PlayerVecesEquivocado ==null){
			PlayerVecesEquivocado.put(player, 0);
		}else if(!PlayerVecesEquivocado.containsKey(player)){
			PlayerVecesEquivocado.put(player, 0);
		}

		if(command.startsWith("report")){
			if(!isPlayerValid(player)){
				return;
			}
			L2Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				PlayerVecesEquivocado.put(player, 0);
				setCheckBot((L2PcInstance)target, player.getName(),0);
			}
			central.msgbox(language.getInstance().getMsg(player).BOT_VERIFICATION_SEND_TO_$player.replace("$player", ((L2PcInstance)target).getName()), player);
		}else if(command.startsWith("zeusBootAsk")){
			String Comando[] = command.split(" ");
			if(Comando.length==4){
				try{
					ID_SELECT.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				int idImagenClick = Integer.valueOf(Comando[3]);
				ID_SELECT.put(player.getObjectId(), idImagenClick);
				Dlg.sendDlg(player, language.getInstance().getMsg(player).BOT_MSGBOX_ARE_YOU_SURE , IdDialog.ENGINE_ANTIBOT_ASK, 5);
			}		
		}else if(command.startsWith("zeusBoot")) {

		}
	}

	public static void checkAnswerByBox(L2PcInstance player){
		if(ID_ICON_CAPTCHA_PLAYER.get(player.getObjectId()) == ID_SELECT.get(player.getObjectId())){
			cleanPlayer(player);
			central.msgbox(language.getInstance().getMsg(player).BOT_THE_PLAYER_IS_NOT_A_BOT, player);
			player.sendPacket(new sendC());			
		}else{
			PlayerVecesEquivocado.put(player, PlayerVecesEquivocado.get(player) + 1);
			setCheckBot(player, "Server_Engine" , PlayerVecesEquivocado.get(player)) ;			
		}
	}
	
	public static void setCheckBot(L2PcInstance targetPlayer, String playerCheck, int VecesIntento){
		//central.msgbox("If you close this window, it will appear again on "+ String.valueOf(general.ANTIBOT_SECONDS_TO_RESEND_ANTIBOT) +" seconds.", targetPlayer);
		opera.setimmobilizeChar(targetPlayer, true);
		Random aleatorio = new Random();
	
		CreateFastIcon();
		
		try{
			ID_ICON_CAPTCHA_PLAYER.remove(targetPlayer.getObjectId());
		}catch(Exception a){
			
		}
		
		int RandImageSelected = aleatorio.nextInt(ICON_CAPTCHA.size()-1);
		
		ID_ICON_CAPTCHA_PLAYER.put(targetPlayer.getObjectId(), RandImageSelected);
		
		int ColumToShow[] = {12,18,24,30};
		int intColumToShow = ColumToShow[aleatorio.nextInt(3)];
		
		int PositionRanDom = aleatorio.nextInt(intColumToShow);
	 

		String btnBypass = "link zeusBC;zeusBootAsk;"+String.valueOf(VecesIntento) + ";" + playerCheck + ";%IDSELECTED_IMAGE%";

		boolean createCaptcha = true;
		Vector<Integer>T_Icon = new Vector<Integer>();
		int Contador = 0;
		T_Icon.add(RandImageSelected);
		String RetornoGrillaSelected = "";
		
		while(createCaptcha){
			if(Contador>=intColumToShow){
				createCaptcha=false;
				break;
			}			
			int idTemporal = aleatorio.nextInt(ICON_CAPTCHA.size());
			if(!T_Icon.contains(idTemporal)){
				T_Icon.add(idTemporal);
				Contador++;
			}
		}
		RetornoGrillaSelected = "";
		Contador = 0;
		for(int idIcon : T_Icon){
			if(Contador == 0 || Contador == 6 || Contador == 12 || Contador == 18 || Contador == 24){
				RetornoGrillaSelected+="<table width=280 align=center><tr>";
			}
			if(Contador==PositionRanDom){
				RetornoGrillaSelected += "<td fixwidth=32>"+ cbFormato.getBotonForm(ICON_CAPTCHA.get(RandImageSelected), btnBypass.replace("%IDSELECTED_IMAGE%", String.valueOf(RandImageSelected))) +"</td>";
				Contador++;
			}else if(idIcon != RandImageSelected){
				RetornoGrillaSelected += "<td fixwidth=32>"+ cbFormato.getBotonForm(ICON_CAPTCHA.get(idIcon), btnBypass.replace("%IDSELECTED_IMAGE%", String.valueOf(idIcon))) +"</td>";
				Contador++;
			}
			if(Contador == 6 || Contador == 12 || Contador == 18 || Contador == 24 || Contador == 30){
				RetornoGrillaSelected+= "</tr></table><br>";
				if(Contador==intColumToShow){
					break;
				}
			}
		}
		
		String GrillaImagenToSee = "<table width=280 border=0 cellpadding=0><tr><td fixwidth=280 align=center><table width=148 background=L2UI_CH3.refinegrade1_01 height=130><tr><td align=center>"+
        "<img src=\""+ ICON_CAPTCHA.get(RandImageSelected) +"\" width=32 height=32><br></td></tr></table><font name=hs12>Click on the Same Imagen Bellow</font><br></td></tr><tr><td fixwidth=280 align=center>";
		
		GrillaImagenToSee += RetornoGrillaSelected + "</td></tr></table><br>";
		
		int Times = getBlackListCounter(targetPlayer) + 1;
		int MinutesInJail = general.ANTIBOT_MINUTOS_JAIL * ( Times * general.ANTIBOT_BLACK_LIST_MULTIPLIER );
		if(Times>0){
			
		}
		
		NpcHtmlMessage html = comun.htmlMaker(targetPlayer, "./config/zeus/htm/" + language.getInstance().getFolder(targetPlayer) + "/Antibot.html");
				
		html.replace("%NAME_SENDER%", playerCheck.replace("_", ""));
		html.replace("%DATA%", GrillaImagenToSee);
		html.replace("%TRIES%", String.valueOf(general.ANTIBOT_OPORTUNIDADES));
		html.replace("%TIME%", String.valueOf(general.ANTIBOT_MINUTOS_ESPERA));
		html.replace("%ATTEMPT%", String.valueOf(VecesIntento));
		html.replace("%MINUTES%", String.valueOf(MinutesInJail));
		html.replace("%REMOVE%", (general.ANTIBOT_BORRAR_ITEM ? "YES" : "NO" ));
		
		central.sendHtml(targetPlayer, html.getHtml() ,true);
		ATTEMPT_NUMBERS.put(targetPlayer.getObjectId(), VecesIntento);		
		general.setTime_antibot(targetPlayer);		
		
		if(VecesIntento >= general.ANTIBOT_OPORTUNIDADES){
			sendJail(targetPlayer);
			general.removeBotPlayer(targetPlayer);
			return;
		}

		if(general.ANTIBOT_SEND_ALL_IP){
			Anunc.Anunciar_All_Char_IP(targetPlayer,"Antibot check was send to " + targetPlayer.getName(),"ANTIBOT",false);
		}

		if(!general.isBotCheckPlayer(targetPlayer)){
			central.msgbox(language.getInstance().getMsg(targetPlayer).BOT_$player_HAVE_SENT_YOU_A_ANTIBOT_VERIFICATION.replace("$player", playerCheck) , targetPlayer);
			general.addBotPlayer(targetPlayer);
			PlayerCodigToJail.put(targetPlayer, getUnixTime());
			ThreadPoolManager.getInstance().scheduleGeneral(new CaptchaTimerPlayer(targetPlayer,PlayerCodigToJail.get(targetPlayer)), (general.ANTIBOT_MINUTOS_ESPERA * 60) * 1000);
			_autoClock temp = new _autoClock(targetPlayer, opera.getUnixTimeNow());
			PLAYER_AUTOCLOCK.put(targetPlayer, temp);
		}
	}
	
	protected static int getAttemptNumber(L2PcInstance player){
		return ATTEMPT_NUMBERS.get(player.getObjectId());
	}
	
	protected static void cleanPlayer(L2PcInstance activeChar){
		opera.setimmobilizeChar(activeChar, false);
		general.removeBotPlayer(activeChar);
		general.resetKillAntibot(activeChar);
		PlayerVecesEquivocado.put(activeChar, 0);
		PlayerCodigToJail.put(activeChar, 0);
		try{
			PlayerCodigToJail.remove(activeChar);
		}catch(Exception a){
			
		}
		try{
			PLAYER_AUTOCLOCK.get(activeChar).end();
			PLAYER_AUTOCLOCK.remove(activeChar);
		}catch(Exception a){
			
		}
	}

	protected static void sendJail(L2PcInstance activeChar){

		if(!general.isBotCheckPlayer(activeChar)){
			return;
		}
		
		activeChar.sendPacket(new sendC());

		general.removeBotPlayer(activeChar);

		try{
			opera.setimmobilizeChar(activeChar, false);
		}catch(Exception a ){

		}
		central.msgbox(language.getInstance().getMsg(activeChar).BOT_YOU_HAVE_BEEN_SEND_TO_JAIL_FOR_NOT_ENTER_THE_RIGHT_PASS_INTIME_$time.replace("$time", String.valueOf(general.ANTIBOT_MINUTOS_ESPERA)), activeChar);
		if (activeChar.isTransformed()) {
			activeChar.untransform();
		}
		
		int tiempoInJail = general.ANTIBOT_MINUTOS_JAIL;
		if(general.ANTIBOT_BLACK_LIST){
			try{
				int vecesRepetidas = blackListedChar( activeChar );
				tiempoInJail = (tiempoInJail * vecesRepetidas) * (general.ANTIBOT_BLACK_LIST_MULTIPLIER <= 0 ? 1 : general.ANTIBOT_BLACK_LIST_MULTIPLIER) ;
			}catch(Exception a){
				tiempoInJail = general.ANTIBOT_MINUTOS_JAIL;
			}
		}
		opera.sendToJail(activeChar, tiempoInJail);
		sendAllPlayerJailByIP(activeChar, tiempoInJail);
		String PlayersName = getAllPlayerNameOnThisRed(activeChar);
		PlayersName = PlayersName.length()>0 ? activeChar.getName() +", "+PlayersName : activeChar.getName();
		if(general.ANTIBOT_ANNOU_JAIL){
			String Mensaje = language.getInstance().getMsg(activeChar).BOT_ANNOUCEMENT_WHEN_$player_IS_SEND_TO_JAIL_FOR_$time_MINUTER.replace("$player", PlayersName).replace("$time", opera.getFormatNumbers(tiempoInJail)) + ( general.ANTIBOT_BORRAR_ITEM ? " " + language.getInstance().getMsg(activeChar).BOT_ANNOUCEMENT_REMOVE_ITEM : "" ) ;
			opera.AnunciarTodos("Antibot", Mensaje);
		}
		RemoveItemFromInventary(activeChar);
		cleanPlayer(activeChar);
	}

	protected static void RemoveItemFromInventary(L2PcInstance player){
		if((general.ANTIBOT_BORRAR_ITEM_ID.length()==0) || !general.ANTIBOT_BORRAR_ITEM){
			return;
		}

		for(String IdItem : general.ANTIBOT_BORRAR_ITEM_ID.split(",")){
			try{
				long IteminChar = player.getInventory().getInventoryItemCount(Integer.valueOf(IdItem),-1);
				if(IteminChar>0){
					long CantidadRemover = Porcent( IteminChar );
					if(CantidadRemover>0){
						opera.removeItem(Integer.valueOf(IdItem), CantidadRemover, player);
					}
				}
			}catch(Exception a ){

			}
		}
	}

	private static long Porcent(long valor){
		double retorno = 0;
		double multi = valor * (general.ANTIBOT_BORRAR_ITEM_PORCENTAJE / 100.0);
		retorno = multi;
		return (long) retorno;
	}

	public static class CaptchaSendBackHTML implements Runnable{
		L2PcInstance activeChar;
		int _VecesIntentado;
		public CaptchaSendBackHTML(L2PcInstance player, int VecesIntentado){
			activeChar = player;
			_VecesIntentado = VecesIntentado;
		}
		@Override
		public void run(){
			try{
				if(_VecesIntentado == PlayerVecesEquivocado.get(activeChar)){
					if(general.isBotCheckPlayer(activeChar) && !activeChar.isJailed()){
						central.msgbox("RESEND ANTIBOT CHECK WINDOWS", activeChar);
						setCheckBot(activeChar,"Auto_System",_VecesIntentado);
					}
				}
			}catch(Exception a){

			}
		}
	}
	
	private static class CaptchaTimerPlayer implements Runnable{
			L2PcInstance activeChar;
			int CodigoEntrada;
			public CaptchaTimerPlayer(L2PcInstance player, int CodEntrada){
				activeChar = player;
				this.CodigoEntrada = CodEntrada;
			}
			@Override
			public void run(){
				try{
					if(general.isBotCheckPlayer(activeChar) && !activeChar.isJailed()){
						if(PlayerCodigToJail.get(activeChar) == this.CodigoEntrada){
							sendJail(activeChar);
						}
					}
				}catch(Exception a){

				}

			}
		}
}

class _autoClock{
	private final L2PcInstance player;
	private final int iniTime;
	private final int totalSecond;
	private boolean continueClock;
	private final Logger _log = Logger.getLogger(_autoClock.class.getName());
	public _autoClock(L2PcInstance _player,int _iniTime){
		this.player = _player;
		this.iniTime = _iniTime;
		this.totalSecond = general.ANTIBOT_MINUTOS_ESPERA * 60;
		continueClock = true;
		this.showTime();
	}
	
	public void end(){
		this.continueClock = false;
	}
	
	private void showTime(){
		if(!continueClock){
			return;
		}
		
		try{
			int unixNow = opera.getUnixTimeNow();
			int RestTime = this.totalSecond - ( unixNow - this.iniTime );
			if(RestTime <=0){
				this.continueClock = false;
				return;
			}
			if(this.player.isJailed()){
				this.continueClock = false;
				return;
			}
			int Attempt = antibotSystem.getAttemptNumber(this.player);
			String Message = language.getInstance().getMsg(this.player).BOT_SECONDS_LEFT_TO_ANSWER.replace("$max_attempt", String.valueOf(general.ANTIBOT_OPORTUNIDADES)).replace("$attempt", String.valueOf(Attempt)).replace("$seconds",String.valueOf(RestTime));
			ExShowScreenMessage Mnsje = new ExShowScreenMessage(1, -1,  ExShowScreenMessage.TOP_LEFT , 1, 1, 0, 0, false, 905, false, Message);
			this.player.sendPacket(Mnsje);
			ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
				public void run(){
					showTime();
				}
			}, 900);		
		}catch(Exception a){
			_log.warning("Error on showTime Antibot System ->" + a.getMessage());
			this.continueClock = false;
		}
	}
	
}
