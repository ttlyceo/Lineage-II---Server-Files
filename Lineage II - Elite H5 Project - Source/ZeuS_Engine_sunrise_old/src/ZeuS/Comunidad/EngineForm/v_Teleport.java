package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._teleportData;
import ZeuS.Config.general;
import ZeuS.Instances.VoteInstance;
import ZeuS.Instances.pvpInstance;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;
import gr.sr.interf.SunriseEvents;
import l2r.L2DatabaseFactory;
import l2r.gameserver.GameTimeController;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.enums.CtrlIntention;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.instancemanager.SiegeManager;
import l2r.gameserver.instancemanager.TownManager;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SetupGauge;
import l2r.gameserver.util.Broadcast;

public class v_Teleport {
	
	private static Logger _log = Logger.getLogger(v_Teleport.class.getName());
	
	private static String TeleportURL = "./data/html/teleporter/ZeuS-GK/";
	@SuppressWarnings("unused")
	private static Map<Integer, Boolean> _FromMain = new HashMap<Integer, Boolean>();
	
	private static Map<Integer, Integer> SHOW_FIRTS_TELEPORT = new HashMap<Integer, Integer>();
	private static Map<Integer, List<L2PcInstance>> PLAYER_INSIDE = new HashMap<Integer, List<L2PcInstance>>();
	private static Map<L2PcInstance, Integer> ID_ZONE_FROM_PLAYER = new HashMap<L2PcInstance, Integer>();
	private static Map<Integer, _teleportData> TELEPORT_DATA = new HashMap<Integer, _teleportData>();
	private static Map<Integer, _teleportData> CHAR_SELECTED_TELEPORT = new HashMap<Integer, _teleportData>();
	
	public static final Map<Integer, _teleportData> getTeleportData(){
		return TELEPORT_DATA;
	}
	
	public static void showHtml(L2PcInstance player, String URL) {
		NpcHtmlMessage htmlToShow = comun.htmlMaker(player, TeleportURL + URL);
		cbManager.separateAndSend(htmlToShow.getHtml(), player);
		
	}
	
	@SuppressWarnings("rawtypes")
	private static NpcHtmlMessage getMenuCentral(L2PcInstance player, NpcHtmlMessage html){
		
		try{
			SHOW_FIRTS_TELEPORT.remove(player.getObjectId());
		}catch(Exception a){
			
		}
		
		int Inicio = 0;
		
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.Teleport.name() +";%TIPO%;%IDTELEPORT%;%NOMBRE%;0;0;0";
		String gridMainButton = opera.getGridFormatFromHtml(html, 1, "%MAIN_BUTTONS%");
		
		//String btnTeleport = "<button value=\"%NOMBRE%\" action=\""+ ByPass +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Fight3None\" fore=\"L2UI_CT1.OlympiadWnd_DF_Fight3None\">";
		
		Iterator itr = TELEPORT_DATA.entrySet().iterator();
		String MainButton = "";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    	  	if(Temporal.getIDSecc() == -1){
    	  		if(Inicio==0){
    	  			Inicio = Temporal.getID();
    	  		}
    	  		String ByPassTemp = ByPass.replace("%NOMBRE%", Temporal.getName()).replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%TIPO%", Temporal.getType());
    	  		MainButton += gridMainButton.replace("%MAIN_TELEPORT_BYPASS%", ByPassTemp) .replace("%MAIN_TELEPORT_NAME%", Temporal.getName()); 
    	  	}
	    }
	    SHOW_FIRTS_TELEPORT.put(player.getObjectId(), Inicio);
	    html.replace("%MAIN_BUTTONS%", MainButton);
	   
	    return html;
	}
	
	@SuppressWarnings({ "rawtypes"})
	public static String getNomSeccion(String IDTele){
		String NomSeccion = "--##--";

		
		Iterator itr = TELEPORT_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
			if(Temporal.getID() == Integer.valueOf(IDTele)){
				return Temporal.getName();
			}
	    }
	    
		return NomSeccion;
	}
	
	@SuppressWarnings({ "rawtypes"})
	private static NpcHtmlMessage getMainTeleportMenu(int _idCategoria, L2PcInstance player, NpcHtmlMessage html){

		int idCategoria = 0;
		if(_idCategoria<-100){
			idCategoria = SHOW_FIRTS_TELEPORT.get(player.getObjectId());
		}else{
			idCategoria = _idCategoria;
		}
		
		html.replace("%SELECTED_ZONE%", getNomSeccion(String.valueOf(idCategoria)));
		
		String GridFormat = opera.getGridFormatFromHtml(html, 2, "%TELEPORT_GO%");
		
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.Teleport.name() +";%TIPO%;%IDTELEPORT%;%NOMBRE%;0;0;0";
		
		Iterator itr = TELEPORT_DATA.entrySet().iterator();
		int contador = 1;
		String TeleportFormat = "";
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		_teleportData Temporal = null;
	    		Temporal = (_teleportData) Entrada.getValue();
	    	  	if(Temporal.getIDSecc() == idCategoria){
	    	  		if(contador==1){
	    	  			TeleportFormat += GridFormat.replace("%USE_TELEPORT_NAME_"+ String.valueOf(contador) +"%", Temporal.getName()) .replace("%BYPASS_USE_TELEPORT_"+ String.valueOf(contador) +"%", ByPass.replace("%NOMBRE%", Temporal.getName()).replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%TIPO%", Temporal.getType()));
	    	  		}else{
	    	  			TeleportFormat = TeleportFormat.replace("%USE_TELEPORT_NAME_"+ String.valueOf(contador) +"%", Temporal.getName()) .replace("%BYPASS_USE_TELEPORT_"+ String.valueOf(contador) +"%", ByPass.replace("%NOMBRE%", Temporal.getName()).replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%TIPO%", Temporal.getType()));
	    	  		}
	    	  		contador++;
	    	  		if(contador==3){
	    	  			contador = 1;
	    	  		}
	    	  	}
	    }

		if(contador==2){
			TeleportFormat = TeleportFormat.replace("%BYPASS_USE_TELEPORT_2%", "") .replace("%USE_TELEPORT_NAME_2%", "");
		}
		
		html.replace("%TELEPORT_GO%", TeleportFormat);
		
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, int idCategoria, String ByPassAnterior){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-teleport.htm");
		html = getMenuCentral(player,html);
		html = getMainTeleportMenu(idCategoria,player, html);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	private static boolean isDualBoxInside(int idZoneIn,L2PcInstance player){
		boolean Retorno = false;

		if(!player.isGM()){
			if(PLAYER_INSIDE.containsKey(idZoneIn)){
				if(PLAYER_INSIDE.get(idZoneIn).size()>0){
					for(L2PcInstance cha : PLAYER_INSIDE.get(idZoneIn)){
						if(ZeuS.isDualBox_pc(cha, player)){
							Retorno = true;
						}
					}
				}
			}
		}

		return Retorno;
	}	
	
	private static void AskTeleportplayer(int idTeleport, L2PcInstance player){
		if(player.isCastingNow()){
			central.msgbox(language.getInstance().getMsg(player).TELEPORT_YOU_CANT_TELEPORT_WHILE_CASTING_OR_IN_TELEPORTATION, player);
			cbFormato.cerrarCB(player);
			return;
		}
		_teleportData temp = TELEPORT_DATA.get(idTeleport);
		CHAR_SELECTED_TELEPORT.put(player.getObjectId(), temp);
		Dlg.sendDlg(player, language.getInstance().getMsg(player).TELEPORT_DO_YOU_WANT_TELEPORT_TO.replace("$zone", temp.getName()), IdDialog.ENGINE_TELEPORT, 9);
	}
	
	public static boolean beginTeleport(L2PcInstance player, Location FromLocation){
		boolean Pass = false;
		boolean playerIsGm = player.isGM();
		_teleportData tempTele = null;
		
		if(general.TELEPORT_BD) {
			tempTele = CHAR_SELECTED_TELEPORT.get(player.getObjectId());
		}
		
		try{
			Pass = true;
		}catch(Exception a){
			Pass = false;
			_log.warning(a.getMessage());
		}
		if(Pass && ((tempTele != null && general.TELEPORT_BD) || FromLocation != null)){
			if(player.isJailed()){
				central.msgbox(language.getInstance().getMsg(player).DISABLED_FOR_PLAYER_IN_JAIL, player);
				return false;
			}			
			if(general.TELEPORT_BD) {
				if(!tempTele.canUseDualPlayer()){
					if(isDualBoxInside(tempTele.getID(), player)){
						central.msgbox(language.getInstance().getMsg(player).TELEPORT_THIS_AREA_DOES_NOT_ALLOW_DUAL_BOX, player);
						return false;
					}
				}
	
				if(tempTele.isJustForNoble() && !player.isNoble()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
					return false;
				}
	
				if(!tempTele.canUseFlagPlayer() && (player.getPvpFlag()>0)){
					central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG, player);
					return false;
				}
	
	
				if(!tempTele.canUsePkPlayer() && (player.getKarma()>0)){
					central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK, player);
					return false;
				}
	
	
				if(tempTele.minLevelToUse() > player.getLevel()){
					central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf( tempTele.minLevelToUse() )), player);
					return false;
				}
	
				if(SiegeManager.getInstance().getSiege(tempTele.getX() , tempTele.getY(), tempTele.getZ()) != null){
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					return false;
				}
	
	
				if(TownManager.townHasCastleInSiege(tempTele.getX(), tempTele.getY()) && player.isInsideZone(ZoneIdType.TOWN)){
					player.sendPacket(SystemMessageId.NO_PORT_THAT_IS_IN_SIGE);
					return false;
				}
			}

			if(player.isAlikeDead()){
				central.msgbox("You can not use teleport while you death", player);
				return false;
			}

			if(!general.TELEPORT_CAN_USE_IN_COMBAT_MODE){
				if(player.isInCombat()){
					central.msgbox(language.getInstance().getMsg(player).TELEPORT_YOU_CAN_NOT_USE_THE_TELEPORT_IN_COMBAT_MODE, player);
					return false;
				}
			}

			boolean isForFree = false;
			
			if(general.TELEPORT_FOR_FREE_UP_TO_LEVEL){
				int charLevel = player.getLevel();
				if(charLevel > general.TELEPORT_FOR_FREE_UP_TO_LEVEL_LV){
					if(!general.FREE_TELEPORT && !opera.haveItem(player, general.TELEPORT_PRICE)){
						return false;
					}					
				}else{
					isForFree=true;
				}
			}else{
				if(!playerIsGm){
					if(!general.FREE_TELEPORT && !opera.haveItem(player, general.TELEPORT_PRICE)){
						return false;
					}
				}
			}

			if(!general.FREE_TELEPORT && !isForFree && !playerIsGm){
				opera.removeItem(general.TELEPORT_PRICE, player);
			}
			
			int unstuckTimer = general.TELEPORT_SECONDS_SKILL_TO_GO * 1000;

			boolean isInInstancePvpZone = pvpInstance.isInsideZone(player);
			Location tempLoc = null;			
			if(player.isGM() || isInInstancePvpZone || player.isInsideZone(ZoneIdType.TOWN) || player.isInsideZone(ZoneIdType.PEACE)){
				boolean isGo = true;
				int XX,YY,ZZ;
				if(general.TELEPORT_BD) {
					isGo = tempTele.getType().equalsIgnoreCase("GO");
					XX = tempTele.getX();
					YY = tempTele.getY();
					ZZ = tempTele.getZ();
				}else {
					XX = FromLocation.getX();
					YY = FromLocation.getY();
					ZZ = FromLocation.getZ();
				}
				if(isGo){
					if(isInInstancePvpZone){
						pvpInstance.playerLeave(player);
						player.setInstanceId(0);
					}else if(VoteInstance.playerIsInside(player.getObjectId())){
						VoteInstance.removePlayerFromZone(player);
						player.setInstanceId(0);
					}
					player.setInstanceId(0);
					player.teleToLocation(XX, YY, ZZ, true);
					if(general.TELEPORT_BD) {
						player.sendMessage("You have been teleport to " + tempTele.getName());
						addPlayerToZone(Integer.valueOf(tempTele.getID()),player);
					}
					cbFormato.cerrarCB(player);
				}
			}else{
				player.forceIsCasting(GameTimeController.getInstance().getGameTicks() + (unstuckTimer / GameTimeController.MILLIS_IN_TICK));
				
				player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
				// SoE Animation section
				player.setTarget(player);
				player.disableAllSkills();
				
				MagicSkillUse msk = new MagicSkillUse(player, 1050, 1, unstuckTimer, 0);
				Broadcast.toSelfAndKnownPlayersInRadius(player, msk, 900);
				SetupGauge sg = new SetupGauge(SetupGauge.BLUE, unstuckTimer);
				player.sendPacket(sg);
				// End SoE Animation section
				
				// continue execution later
				player.setSkillCast(ThreadPoolManager.getInstance().scheduleGeneral(new EscapeFinalizer(player, tempTele, FromLocation), unstuckTimer ));
				cbFormato.cerrarCB(player);
			}
		}else{
			return false;
		}
		return true;
	}
	
	
	private static class EscapeFinalizer implements Runnable
	{
		private final L2PcInstance _activeChar;
		private final _teleportData _townInfo;
		private final Location _Location;
		
		protected EscapeFinalizer(L2PcInstance activeChar, _teleportData towninfo, Location _Loc)
		{
			_activeChar = activeChar;
			_townInfo = towninfo;
			_Location = _Loc;
		}
		
		@Override
		public void run()
		{
			if (_activeChar.isDead())
			{
				return;
			}
			
			_activeChar.setIsIn7sDungeon(false);
			_activeChar.enableAllSkills();
			_activeChar.setIsCastingNow(false);
			_activeChar.setInstanceId(0);
			if(general.TELEPORT_BD) {
				_activeChar.teleToLocation(Integer.valueOf(_townInfo.getX()), _townInfo.getY() , _townInfo.getZ() , true);
				_activeChar.sendMessage("You have been teleport to " + _townInfo.getName());
				addPlayerToZone(Integer.valueOf(_townInfo.getID()),_activeChar);
			}else {
				_activeChar.teleToLocation(_Location);
			}
			cbFormato.cerrarCB(_activeChar);
		}
	}	
	
	
	public static void removePlayerInSide(L2PcInstance player){
		try{
			int idTeleport = ID_ZONE_FROM_PLAYER.get(player);
			if(PLAYER_INSIDE.containsKey(idTeleport)){
				if(PLAYER_INSIDE.get(idTeleport).contains(player)){
					PLAYER_INSIDE.get(idTeleport).remove(player);
				}
			}
		}catch(Exception a){

		}
	}	
	
	protected static void addPlayerToZone(int idTeleport, L2PcInstance player){
		if(player.isGM()){
			return;
		}
		removePlayerInSide(player);
		if(!PLAYER_INSIDE.containsKey(idTeleport)){
			PLAYER_INSIDE.put(idTeleport, new Vector<L2PcInstance>());
		}
		if(!PLAYER_INSIDE.get(idTeleport).contains(player)){
			PLAYER_INSIDE.get(idTeleport).add(player);
		}
		ID_ZONE_FROM_PLAYER.put(player, idTeleport);
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_TELEPORT){
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}
		
		if(!general.TELEPORT_BD) {
			NpcHtmlMessage htmlToShow = comun.htmlMaker(player, TeleportURL + "index.htm");
			return htmlToShow.getHtml();
		}
		
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,-300,"");
		}else if(parm1.equals("secc")){
			return mainHtml(player,parm1,Integer.valueOf(parm2),"");
		}else if(parm1.equals("go")){
			if(SunriseEvents.isInEvent(player)){
				return "";
			}
			if(opera.canUseCBFunction(player, false)){
				AskTeleportplayer(Integer.valueOf(parm2),player);
			}else if(pvpInstance.isInsideZone(player)){
				AskTeleportplayer(Integer.valueOf(parm2),player);				
			}
		}else if(parm1.equals("FromMain")){
			return mainHtml(player,parm1,-300,"");
		}
		return "";
	}
	
	public static void loadTeleportData(){
		TELEPORT_DATA.clear();
		Connection conn = null;
		String qry = "";
		PreparedStatement psqry = null;
		ResultSet rss = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			qry = "SELECT * FROM zeus_teleport ORDER BY zeus_teleport.idsec, zeus_teleport.pos ASC";
			psqry = conn.prepareStatement(qry);
			rss = psqry.executeQuery();
			while(rss.next()){
				_teleportData temp = new _teleportData(rss.getInt("id"), Integer.valueOf(rss.getString("idsec")), rss.getString("nom"), ( rss.getString("fornoble").equalsIgnoreCase("true") ) , ( rss.getString("cangoflag").equalsIgnoreCase("true") )  , ( rss.getString("cangoKarma").equalsIgnoreCase("true")  ) , ( rss.getString("dualbox").equalsIgnoreCase("true")  ) , rss.getInt("lvlup"), rss.getString("tip"), rss.getInt("pos"), rss.getInt("x"), rss.getInt("y"), rss.getInt("z"), "");
				TELEPORT_DATA.put(rss.getInt("id"), temp);
			}
		}catch(SQLException a){
			_log.warning("Telepot Load error->" + a.getMessage());
		}

		try{
			conn.close();
		}catch(Exception a){
			
		}				
	}	
	
}


