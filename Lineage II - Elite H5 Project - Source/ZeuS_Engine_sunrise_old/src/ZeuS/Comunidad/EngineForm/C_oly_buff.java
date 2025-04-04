package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.sql.NpcBufferTable;
import l2r.gameserver.data.sql.NpcBufferTable.NpcBufferData;
import l2r.gameserver.model.actor.L2Summon;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class C_oly_buff {
	private static Map<Integer, HashMap<Integer, String>> BUFF_SCH_NAME = new HashMap<Integer, HashMap<Integer, String>>();
	private static HashMap<Integer, Vector<Integer>>BUFF_SCH_BUFF = new HashMap<Integer, Vector<Integer>>();
	private static Map<Integer, Integer> SELECTED_SCHEME = new HashMap<Integer, Integer>();
	private static int MAXIMO_BUFF = Config.ALT_OLY_MAX_BUFFS;
	private static int MAXIMO_SCH = 5;
	
	private static final Logger _log = Logger.getLogger(C_oly_buff.class.getName());
	
	private static final int[] BUFFS =
		{
			4357, // Haste Lv2
			4342, // Wind Walk Lv2
			4356, // Empower Lv3
			4355, // Acumen Lv3
			4351, // Concentration Lv6
			4345, // Might Lv3
			4358, // Guidance Lv3
			4359, // Focus Lv3
			4360, // Death Whisper Lv3
			4352, // Berserker Spirit Lv2
		};
	
	private static final String[]ICON_BUFF={
		"icon.skill1086",
		"icon.skill1204",
		"icon.skill1059",
		"icon.skill1085",
		"icon.skill1078",
		"icon.skill1068",
		"icon.skill1240",
		"icon.skill1077",
		"icon.skill1242",
		"icon.skill1062"
	};
	
	private static final String[]NOM_BUFF={
		"Haste Lv2",
		"Wind Walk Lv2",
		"Empower Lv3",
		"Acumen Lv3",
		"Concentration Lv6",
		"Might Lv3",
		"Guidance Lv3",
		"Focus Lv3",
		"Death Whisper Lv3",
		"Berserker Spirit Lv2"
	};
	
	public static void removeAllScheme(L2PcInstance player){
		String Consulta = "delete from zeus_oly_sch where idChar=?";
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.setInt(1, player.getObjectId());
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}
		Consulta = "delete from zeus_oly_sch_buff where idsch not in (select zeus_oly_sch.id from zeus_oly_sch )";
		con = null;
		statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
		
		//BUFF_SCH_NAME.get(player.getObjectId()).remove(idSch);
		BUFF_SCH_NAME.remove(player.getObjectId());
		SELECTED_SCHEME.put(player.getObjectId(), 0);
	}
	
	private static void removeBuffScheme(L2PcInstance player, int idSch){
		String Consulta = "delete from zeus_oly_sch where id=?";
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.setInt(1, idSch);
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
		
		Consulta = "delete from zeus_oly_sch_buff where idsch=?";
		con = null;
		statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.setInt(1, idSch);
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
		
		BUFF_SCH_NAME.get(player.getObjectId()).remove(idSch);
		BUFF_SCH_BUFF.remove(idSch);
		
		if(SELECTED_SCHEME.get(player.getObjectId())==idSch){
			SELECTED_SCHEME.put(player.getObjectId(), 0);
		}
		
	}
	
	private static void removeBuffFromScheme(L2PcInstance player, int idBuff){
		int Sche=0;
		
		if(SELECTED_SCHEME!=null){
			if(SELECTED_SCHEME.containsKey(player.getObjectId())){
				Sche = SELECTED_SCHEME.get(player.getObjectId());
			}
		}
		
		if(Sche==0){
			central.msgbox(language.getInstance().getMsg(player).OLY_BUFFER_NEED_TO_ACTIVE_CREATE_SCHEME_FOR_USE, player);
			return;
		}
		
		String Consulta = "delete from zeus_oly_sch_buff where idsch=? and idbuff=?";
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.setInt(1, Sche);
			statement.setInt(2, idBuff);
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
		BUFF_SCH_BUFF.get(Sche).removeElement(idBuff);
	}
	
	
	private static void putBuffIntoScheme(L2PcInstance player, int idBuff){
		
		int Sche=0;
		
		if(SELECTED_SCHEME!=null){
			if(SELECTED_SCHEME.containsKey(player.getObjectId())){
				Sche = SELECTED_SCHEME.get(player.getObjectId());
			}
		}
		
		if(Sche==0){
			central.msgbox(language.getInstance().getMsg(player).OLY_BUFFER_NEED_TO_ACTIVE_CREATE_SCHEME_FOR_USE, player);
			return;
		}
		
		int BuffHave = BUFF_SCH_BUFF.get(Sche).size();
		
		if(BuffHave>=MAXIMO_BUFF){
			central.msgbox(language.getInstance().getMsg(player).OLY_BUFFER_YOU_CANT_ADD_MORE_BUFF_MAXIMUM.replace("$max", String.valueOf(MAXIMO_BUFF)), player);
			return;
		}
		
		String Consulta = "INSERT INTO zeus_oly_sch_buff() VALUES(?,?)";
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.setInt(1, Sche);
			statement.setInt(2, idBuff);
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
		BUFF_SCH_BUFF.get(Sche).add(idBuff);
	}
	
	private static void createScheme(L2PcInstance player, String Nombre){
		
		if(BUFF_SCH_NAME!=null){
			if(BUFF_SCH_NAME.containsKey(player.getObjectId())){
				int Schs = BUFF_SCH_NAME.get( player.getObjectId() ).size();
				if(Schs >= MAXIMO_SCH){
					central.msgbox(language.getInstance().getMsg(player).OLY_BUFFER_YOU_CANT_ADD_MORE_SCHEMES_MAXIMUM.replace("$max", String.valueOf(MAXIMO_SCH)), player);
					return;
				}
			}
		}
		
		int MaximoPermitido = 14;
		
		if(Nombre.trim().length()>MaximoPermitido){
			Nombre = Nombre.substring(0,MaximoPermitido);
		}
		
		if(BUFF_SCH_NAME!=null){
			if(BUFF_SCH_NAME.containsKey(player.getObjectId())){
				if(BUFF_SCH_NAME.get(player.getObjectId()).containsValue(Nombre)){
					central.msgbox(language.getInstance().getMsg(player).OLY_BUFFER_THE_SCHEME_NAME_ALREADY_EXIST, player);
					return;
				}
			}
		}
		
		String Consulta = "insert into zeus_oly_sch(idChar,nombre) values(?,?)";
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta,Statement.RETURN_GENERATED_KEYS);
			statement.setInt(1, player.getObjectId());
			statement.setString(2, Nombre);
			statement.execute();
			try (ResultSet rset = statement.getGeneratedKeys())
			{
				if (rset.next())
				{
					int _id = rset.getInt(1);
					
					if(!BUFF_SCH_NAME.containsKey(player.getObjectId())){
						BUFF_SCH_NAME.put(player.getObjectId(), new HashMap<Integer, String>());
					}
					BUFF_SCH_NAME.get(player.getObjectId()).put(_id, Nombre);
					BUFF_SCH_BUFF.put(_id, new Vector<Integer>());
					if(BUFF_SCH_NAME.get(player.getObjectId()).size()==1){
						SELECTED_SCHEME.put(player.getObjectId(), _id);
					}
				}
			}			
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
	}
	
	private static void getBuffFromSchOly(int playeID, int idSch){
		String Consulta = "SELECT idbuff, idsch FROM zeus_oly_sch_buff WHERE idsch=?";
		Connection con = null;
		PreparedStatement statement = null;
		Vector<Integer>T = new Vector<Integer>();		
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			statement.setInt(1, idSch);
			ResultSet inv = statement.executeQuery();
			while (inv.next()){
				T.add(inv.getInt("idbuff"));
			}			
		}catch(Exception a){
			_log.warning("Error A2->" + a.getMessage());
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
		BUFF_SCH_BUFF.put(idSch, T);
	}
	
	public static void getSchFromOlys(){
		String Consulta = "SELECT * FROM zeus_oly_sch";
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(Consulta);
			ResultSet inv = statement.executeQuery();
			while (inv.next()){
				if(!BUFF_SCH_NAME.containsKey(inv.getInt("idChar"))){
					BUFF_SCH_NAME.put(inv.getInt("idChar"), new HashMap<Integer, String>());
				}
				BUFF_SCH_NAME.get(inv.getInt("idChar")).put(inv.getInt("id"), inv.getString("nombre"));
				getBuffFromSchOly(inv.getInt("idChar"),inv.getInt("id"));
			}			
		}catch(Exception a){
			_log.warning("Error A1->" + a.getMessage());
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
	}
	
	private static boolean haveBuffInSch(int idBuff, int idSch){
		if(idSch==0){
			return false;
		}
		if(BUFF_SCH_BUFF!=null){
			if(BUFF_SCH_BUFF.containsKey(idSch)){
				return BUFF_SCH_BUFF.get(idSch).contains(idBuff);
			}
		}
		return false;
	}
	
	private static NpcHtmlMessage getMainBuff(L2PcInstance player, NpcHtmlMessage html){
		int SelSch = 0;
		String bypass_add = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.OlyBuffer.name() + ";ADD_BUFF;%IDBUFF%;0;0;0;0";
		String bypass_remover = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.OlyBuffer.name() + ";REMOVE_BUFF;%IDBUFF%;0;0;0;0" ;
		String BtnAgregar = "<button action=\""+ bypass_add +"\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_PlusBtn_Red\" fore=\"L2UI_CT1.MiniMap_DF_PlusBtn_Red\" value=\" \">";
		String BtnRemover = "<button action=\""+ bypass_remover +"\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_MinusBtn_Red\" fore=\"L2UI_CT1.MiniMap_DF_MinusBtn_Red\">";
		
		if(SELECTED_SCHEME!=null){
			if(SELECTED_SCHEME.containsKey(player.getObjectId())){
				SelSch = SELECTED_SCHEME.get(player.getObjectId());
			}
		}
		
		boolean addBtn = false;
		int cont=0;
		if(SelSch>0){
			addBtn = true;
		}		
		
		String Buff_Grid = opera.getGridFormatFromHtml(html, 1, "%MAIN_BUFF%");
		String BuffList = "";
		for(int idBuff : BUFFS){
			boolean haveSkillInSch = haveBuffInSch(idBuff, SelSch);
			String ByPass_UseBuff = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.OlyBuffer.name() + ";BUFF;%IDBUFF%;0;0;0;0" ;
			
			BuffList += Buff_Grid
					.replace("%BUTTON_ADD_REMOVE%", ( addBtn ? ( haveSkillInSch ? BtnRemover.replace("%IDBUFF%", String.valueOf(idBuff)) : BtnAgregar.replace("%IDBUFF%", String.valueOf(idBuff)) ) : "" ))
					.replace("%BUFF_NAME%", NOM_BUFF[cont])  
					.replace("%BUFF_BYPASS%", ByPass_UseBuff.replace("%IDBUFF%", String.valueOf(idBuff)))
					.replace("%ICON_SKILL%", ICON_BUFF[cont]);
			cont++;
		}
		
		html.replace("%MAIN_BUFF%", BuffList);
		
		return html;
	}
	
	@SuppressWarnings("rawtypes")
	private static NpcHtmlMessage getSchemeSection(L2PcInstance player, NpcHtmlMessage html){
		String ByPass_CreateScheme = "bypass -h ZeuS olyscheme $txtNomSch";
		
		html.replace("%BYPASS_SCHEME_CREATE%", ByPass_CreateScheme);
		String MyScheme = opera.getGridFormatFromHtml(html, 2, "%MY_SCHEME%");
		String AllScheme = "";
		
		if(BUFF_SCH_NAME!=null){
			if(BUFF_SCH_NAME.containsKey(player.getObjectId())){
				String Bypass_UseScheme = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.OlyBuffer.name() + ";USE_SCHEME;%ID_SCH%;0;0;0;0";
				String Bypass_SelectScheme = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.OlyBuffer.name() + ";SELECT_SCHEME;%ID_SCH%;0;0;0;0";
				String Bypass_deleteScheme = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.OlyBuffer.name() + ";REMOVE_SCHEME;%ID_SCH%;0;0;0;0";
				Iterator itr = BUFF_SCH_NAME.get(player.getObjectId()).entrySet().iterator();
				String ColorSelected = "FE9A2E";
				String ColorNoSelected = "F6E3CE";
				int idSchSelected = SELECTED_SCHEME.get(player.getObjectId());
				while(itr.hasNext()){
					Map.Entry inf = (Map.Entry)itr.next();
					int IdScheme = (int) inf.getKey();
					int CantiBuffScheme = BUFF_SCH_BUFF.get(IdScheme).size();
					String NombreScheme = (String) inf.getValue();
					AllScheme += MyScheme.replace("%MY_SCHEME_SELECTED_REMOVE_ME%", Bypass_deleteScheme.replace("%ID_SCH%", String.valueOf(IdScheme))).replace("%MY_SCHEME_SELECTED_QUANTITY_BUFF%", String.valueOf(CantiBuffScheme)) .replace("%MY_SCHEME_SELECTED_COLOR%", ( idSchSelected == IdScheme ? ColorSelected : ColorNoSelected )) .replace("%MY_SCHEME_USE_ME_SELECTED%", Bypass_UseScheme.replace("%ID_SCH%", String.valueOf(IdScheme))).replace("%MY_SCHEME_SELECTED_BYPASS%", Bypass_SelectScheme.replace("%ID_SCH%", String.valueOf(IdScheme))) .replace("%MY_SCHEME_NAME_SELECTED%", NombreScheme);
				}
			}
		}

		html.replace("%MY_SCHEME%", AllScheme);
		
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-oly-buffer_scheme.htm");
		html = getMainBuff(player, html);
		html = getSchemeSection(player, html);
		return html.getHtml();
	}
	
	private static boolean isOly(L2PcInstance player){
		if(player.isInOlympiadMode()){
			return true;
		}
		return false;
	}
	
	private static boolean canBuff_count(L2PcInstance player){
		
		if(!isOly(player)){
			return true;
		}
		
		int buffCount = player.getOlympiadBuffCount();
		if (buffCount <= 0)
		{
			central.msgbox(language.getInstance().getMsg(player).OLYMPIAD_SF_NO_MORE_BUFF_TO_USE , player);
			cbFormato.cerrarCB(player);
			return false;
		}
		player.setOlympiadBuffCount(--buffCount);
		return true;
	}
	
	private static void useSchPlayer(L2PcInstance player, int idSch){
		if(player.isOlympiadStart()){
			return;
		}
		int cont = 0;
		for(int idBuff : BUFF_SCH_BUFF.get(idSch)){
			if(cont > MAXIMO_BUFF){
				continue;
			}
			BuffPlayer(player, idBuff);
			cont++;
		}
	}
	
	private static void BuffPlayer(L2PcInstance player, int IdBuff){
		final NpcBufferData npcBuffGroupInfo = NpcBufferTable.getInstance().getSkillInfo(36402, IdBuff);
		if (npcBuffGroupInfo == null)
		{
			_log.warning("Olympiad Buffer Warning");
		}
		
		if(!canBuff_count(player)){
			return;
		}
		
		final L2Skill skill = npcBuffGroupInfo.getSkill().getSkill();
		if (skill != null)
		{
			player.broadcastPacket(new MagicSkillUse(player, player, skill.getId(), skill.getLevel(), 0, 0));
			skill.getEffects(player, player);
			final L2Summon summon = player.getSummon();
			if (summon != null)
			{
				player.broadcastPacket(new MagicSkillUse(player, summon, skill.getId(), skill.getLevel(), 0, 0));
				skill.getEffects(summon, summon);
			}
		}
					
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(player.isInTownWarEvent()){
			central.msgbox(language.getInstance().getMsg(player).OLYMPIAD_SF_CAN_USE_BUFFER_TOWN_EVENT,player);
			cbFormato.cerrarCB(player);
			return "";
		}
			
		int buffCount = Config.ALT_OLY_MAX_BUFFS;
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];		
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			SELECTED_SCHEME.put(player.getObjectId(),0);
			return mainHtml(player,params);
		}else if(parm1.equals("BUFF")){
			int idBuff = Integer.valueOf(parm2);
			BuffPlayer(player, idBuff);
			buffCount = player.getOlympiadBuffCount();
		}else if(parm1.equals("ADD_BUFF")){
			int idBuff = Integer.valueOf(parm2);
			putBuffIntoScheme(player,idBuff);
			buffCount = player.getOlympiadBuffCount();
		}else if(parm1.equals("REMOVE_BUFF")){
			int idBuff = Integer.valueOf(parm2);
			removeBuffFromScheme(player,idBuff);
			buffCount = player.getOlympiadBuffCount();
		}else if(parm1.equals("CREATE_SCHEME")){
			String NombreScheme = parm2;
			createScheme(player,NombreScheme);
			buffCount = player.getOlympiadBuffCount();
		}else if(parm1.equals("SELECT_SCHEME")){
			int idScheme = Integer.valueOf(parm2);
			SELECTED_SCHEME.put(player.getObjectId(), idScheme);
			buffCount = player.getOlympiadBuffCount();
		}else if(parm1.equals("USE_SCHEME")){
			int idScheme = Integer.valueOf(parm2);
			useSchPlayer(player,idScheme);
			buffCount = player.getOlympiadBuffCount();
		}else if(parm1.equals("REMOVE_SCHEME")){
			int idScheme = Integer.valueOf(parm2);
			removeBuffScheme(player, idScheme);
			buffCount = player.getOlympiadBuffCount();
		}
		
		if(buffCount<=0 && player.isInOlympiadMode()){
			central.msgbox(language.getInstance().getMsg(player).OLYMPIAD_SF_NO_MORE_BUFF_TO_USE , player);
			cbFormato.cerrarCB(player);
			return "";
		}	
		
		return mainHtml(player,params);
	}

	public static String delegar(L2PcInstance player, String linkComunidad) {
		return mainHtml(player, linkComunidad);
	}
}
