package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.MobGroup;
import l2r.gameserver.model.MobGroupTable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._GeneralDropInfo;
import ZeuS.Config._InfoGeneralDrop;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.ObserveMode;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;

public class v_RaidBossInfo{
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(v_RaidBossInfo.class.getName());

	private static Map<Integer, String>PALABRA_BUSCAR = new HashMap<Integer, String>();
	private static Map<Integer, Boolean>FROM_MENU = new HashMap<Integer, Boolean>();
	
	private static Map<Integer, String>PaginaAntes = new HashMap<Integer, String>();
	
	private static Vector<raidInfoClass> INFO_RAIDBOSS = new Vector<raidInfoClass>();
	private static Map<Integer, String> TELEPORT_TO = new HashMap<Integer, String>();
	
	private static String getInfoNPC(L2PcInstance player, int idMonster, String Sort, int idPagina){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-raidboss-info-npc.htm");
		
		L2NpcTemplate tmpl = null;
		L2Npc Monster = null;
		raidInfoClass RBIn = null;
		
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId()!= idMonster){
				continue;
			}
			RBIn = TT;
			break;
		}
		
		tmpl = RBIn.getL2NpcTemplate();
		Monster = RBIn.getL2Npc(true);
		String Nombre = "";
		
		String x = "";
		String y = "";
		String z = "";
		
		raidInfoClass C_Monster = null;
		
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId()!= idMonster){
				continue;
			}
			C_Monster = TT;
			x = String.valueOf(TT.getRB_X());
			y = String.valueOf(TT.getRB_Y());
			z = String.valueOf(TT.getRB_Z());
		}
		
		if(Monster == null){
			central.msgbox("This NPC Have problem. We cant load the NPC / Monster Information.", player);
			cbFormato.cerrarCB(player);
			return "";
		}
		
		int DropCount = 0;
		for (_InfoGeneralDrop TemD : C_Monster.getAllDrop()){
			DropCount++;
		}
				
		Nombre = C_Monster.getName();
		int SpawnCount = SpawnTable.getInstance().getSpawns(idMonster).size();
		
		byte attackAttribute = Monster.getAttackElement();
		
		String ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name() + ";Listar;"+ String.valueOf(idPagina) +";0;" + Sort + ";0;0";
		
		html.replace("%BYPASS_BACK%", ByPassBack);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%NPC_NAME%", Nombre);
		html.replace("%NPC_LEVEL%", String.valueOf(RBIn.getLevel()));
		html.replace("%SPAWNED_COUNT%", String.valueOf(SpawnCount));
		html.replace("%MINIONS_COUNT%", String.valueOf( tmpl.getMinionData().size()));
		html.replace("%EXP_REWARD%", String.valueOf( tmpl.getRewardExp()));
		html.replace("%SP_REWARD%", String.valueOf( tmpl.getRewardSp()));
		html.replace("%MAX_HP%", String.valueOf(Monster.getMaxHp()));
		html.replace("%MAX_MP%", String.valueOf(Monster.getMaxMp()));
		html.replace("%P_ATTACK%", String.valueOf(Monster.getPAtk(null)));
		html.replace("%M_ATTACK%", String.valueOf(Monster.getMAtk(null, null)));
		html.replace("%P_DEF%", String.valueOf( Monster.getPDef(null)));
		html.replace("%M_DEF%", String.valueOf( Monster.getMDef(null,null)));
		html.replace("%ATTACK_SPEED%", String.valueOf( Monster.getPAtkSpd()));
		html.replace("%CASTING_SPEED%", String.valueOf( Monster.getMAtkSpd()));
		html.replace("%RUN_SPEED%", String.valueOf( Math.round(Monster.getRunSpeed())));
		html.replace("%DROP_AMOUNT%", String.valueOf(DropCount));
		html.replace("%STR%", String.valueOf(Monster.getSTR()));
		html.replace("%DEX%", String.valueOf(Monster.getDEX()));
		html.replace("%CON%", String.valueOf(Monster.getCON()));
		html.replace("%INT%", String.valueOf(Monster.getINT()));
		html.replace("%WIT%", String.valueOf(Monster.getWIT()));
		html.replace("%MEN%", String.valueOf(Monster.getMEN()));
		html.replace("%ATTACK_ELEMENTAL%", Elementals.getElementName(attackAttribute));
		html.replace("%ATTACK_ELEMENTAL_VALUE%", String.valueOf(Monster.getAttackElementValue(attackAttribute)));
		html.replace("%FIRE_DEF%", String.valueOf(Monster.getDefenseElementValue(Elementals.FIRE)));
		html.replace("%WATER_DEF%", String.valueOf(Monster.getDefenseElementValue(Elementals.WATER)));
		html.replace("%WIND_DEF%", String.valueOf(Monster.getDefenseElementValue(Elementals.WIND)));
		html.replace("%EARTH_DEF%", String.valueOf(Monster.getDefenseElementValue(Elementals.EARTH)));
		html.replace("%HOLY_DEF%", String.valueOf(Monster.getDefenseElementValue(Elementals.HOLY)));
		html.replace("%DARK_DEF%", String.valueOf(Monster.getDefenseElementValue(Elementals.DARK)));
		html.replace("%IDMONSTER%", String.valueOf(Monster.getId()));

		String bypass_ShowDrop = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" +Engine.enumBypass.RaidBossInfo.name()+";showDrop;"+ String.valueOf(idMonster) +";0;"+ String.valueOf(idPagina) +";0;0;0" ;
		String bypass_ShowSkill = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+ ";ShowNPCSkill;" + String.valueOf(idMonster)+";0;0;0;0;0";
		String bypass_TeleportMe = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name()+";teleportAsk;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		String bypass_Observe = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name()+";ObserveAsk;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		
		html.replace("%BYPASS_SHOW_DROP%", bypass_ShowDrop);
		html.replace("%BYPASS_SHOW_SKILL%", bypass_ShowSkill);
		html.replace("%BYPASS_TELEPORT_ME%", bypass_TeleportMe);
		html.replace("%BYPASS_OBSERVER%", bypass_Observe);
		
		return html.getHtml();
	}	
	
	
	@SuppressWarnings("unused")
	private static String _getInfoNPC(L2PcInstance player, int idMonster, String Sort){
		String retorno = "";
		int LevelMonster = 0;
		String Nombre = "";
		
		String x = "";
		String y = "";
		String z = "";
		
		raidInfoClass C_Monster = null;
		
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId()!= idMonster){
				continue;
			}
			C_Monster = TT;
			LevelMonster = TT.getLevel();
			Nombre = TT.getName();
			x = String.valueOf(TT.getRB_X());
			y = String.valueOf(TT.getRB_Y());
			z = String.valueOf(TT.getRB_Z());
		}
		
		retorno = "<table width=820 background=L2UI_CT1.Windows_DF_TooltipBG  cellpadding=3 cellspacing=3><tr><td>"+
		        "Npc Name: <font name=\"hs12\" color=CCFFCC>"+Nombre+"</font> <font color=5B5B5B>Lv: "+ String.valueOf(LevelMonster) +"</font></td></tr></table>";

		retorno += "<center><br><br><table width=720><tr><td fixwidth=360><table fixwidth=360 background=\"L2UI_CT1.Windows_DF_Drawer_Bg\" cellpadding=5><tr>"+
				"<td><table fixwidth=390 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr><td fixwidth=390 align=RIGHT><font name=\"hs12\"color=\"D1D296\">Basic Information</font></td></tr></table>"+
				"<table fixwidth=390 cellspacing=2 cellpadding=0 bgcolor=\"0C0D29\" height=20>";
		
		int contador = 0;
		
		
		
		for(String Datos : getMonsterInfo(idMonster)){
			if(contador==0){
				retorno += "<tr>";
			}
			retorno += "<td fixwidth=115 align=LEFT>"+Datos.split(":")[0]+":</td>"+
            "<td fixwidth=100 align=RIGHT><font color=A36947>"+ opera.getFormatNumbers(Datos.split(":")[1]) +"</font></td>";

			contador++;
			
			if(contador==2){
				contador=0;
				retorno += "</tr>";
			}
		}
		
		if(contador==1){
			retorno += "<td></td></tr>";
		}
		
		retorno += "</table><br>";
		
		retorno += "<table fixwidth=390 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr><td fixwidth=390 align=RIGHT><font name=\"hs12\"color=\"D1D296\">Stats</font></td></tr>"+
        "</table><table fixwidth=360 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr>";
		
		contador = 0;
		
		for(String Datos : getMonsterDyes(idMonster)){
			if(contador==0){
				retorno += "<tr>";
			}
			retorno += "<td fixwidth=140 align=LEFT>"+Datos.split(":")[0]+":</td>"+
            "<td fixwidth=40 align=RIGHT><font color=A36947>"+ Datos.split(":")[1] +"</font></td>";

			contador++;
			
			if(contador==2){
				contador=0;
				retorno += "</tr>";
			}
		}
		
		if(contador==1){
			retorno += "<td></td></tr>";
		}
		
		retorno += "</table><br>";
		
		retorno += "<table fixwidth=390 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr><td fixwidth=390 align=RIGHT><font name=\"hs12\"color=\"D1D296\">Attribute Value Def</font></td></tr>"+
		        "</table><table fixwidth=360 cellspacing=8 cellpadding=0 bgcolor=\"0C0D29\" height=20><tr>";
				
		contador = 0;
		
		for(String Datos : getMonsterElements(idMonster)){
			if(contador==0){
				retorno += "<tr>";
			}
			retorno += "<td fixwidth=140 align=LEFT>"+Datos.split(":")[0]+":</td>"+
            "<td fixwidth=40 align=RIGHT><font color=A36947>"+ Datos.split(":")[1] +"</font></td>";

			contador++;
			
			if(contador==2){
				contador=0;
				retorno += "</tr>";
			}
		}
		
		if(contador==1){
			retorno += "<td></td></tr>";
		}
		retorno += "</table><br>";

		retorno += "</td>";
		
		String bypass_ShowDrop = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" +Engine.enumBypass.RaidBossInfo.name()+";showDrop;"+ String.valueOf(idMonster) +";0;0;"+ Sort +";0;0" ;
		String bypass_ShowSkill = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+ ";ShowNPCSkill;" + String.valueOf(idMonster)+";0;0;0;0;0";
		String bypass_TeleportMe = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name()+";teleport;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		String bypass_ObserveMode = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name()+";Observe;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		
		retorno += "<td fixwidth=360><table width=360 background=\"L2UI_CT1.Windows_DF_Drawer_Bg\"><tr><td><table fixwidth=360 cellpadding=5><tr><td fixwidth=180 align=CENTER>"+
        "<button value=\"Show Drop\" action=\""+ bypass_ShowDrop +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td><td fixwidth=180 align=CENTER>"+
        "<button value=\"Show Skill\" action=\""+ bypass_ShowSkill +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><table fixwidth=360 cellpadding=5><tr><td fixwidth=360 align=CENTER>"+
        "<button value=\"Teleport Me\" action=\""+ bypass_TeleportMe +"\" width=250 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br>"+
        "<button value=\"I Want to Observe\" action=\""+ bypass_ObserveMode +"\" width=250 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br></td></tr></table></td>";
		
		retorno +="</tr></table><br><br></td></tr></table></center>";
		
		
		return retorno;
	}
	
	@SuppressWarnings("unused")
	private static String getMonsterDropList(int idMonster, int pagina, L2PcInstance player, int PaginaIndex){

		Vector<_InfoGeneralDrop>vTemporal = _GeneralDropInfo.getAllDropFromMob(idMonster);
		
		String DropData = "<center><table fixwidth=690 border=0 cellpadding=0 cellspacing=0>";
		
		String GrillaDrop = "<td width=230><table width=230 background=L2UI_CT1.Windows_DF_TooltipBG height=75><tr><td fixwidth=32>"+
			"<table width=32 cellspacing=0><tr><td><img src=\"%IDIMG%\" width=32 height=32></td></tr></table></td><td>"+
			"<table fixwidth=198 border=0 cellspacing=1><tr><td fixwidth=218><font color=B3C624>%NOMBRE%</font></td></tr>"+
			"<tr><td fixwidth=218><font color=2ECCFA>%DROPMIN%</font> | <font color=2E9AFE>%DROPMAX%</font></td></tr>"+
			"<tr><td fixwidth=218><font color=C6D6DA>Chance: </font><font color=8BC7D9>%PORCENTAJE% %TIPODROP%</font></td></tr></table><br></td></tr></table></td>";
		
		
		int max=15;
		int contador=0;
		int contDrop = 0;
		int Desde = pagina * max;
		int Hasta = Desde + max;
		boolean haveNext = false;
		
		for(_InfoGeneralDrop Dat : vTemporal){
			if(contDrop>=Desde && contDrop<Hasta){
				String Nombre = Dat.getItemName();
				String IMAGEN_ID = Dat.getItemIcon();
				String MIN = opera.getFormatNumbers( Dat.getMin() );
				String MAX = opera.getFormatNumbers( Dat.getMax() );
				String CHANCE = Dat.getChanceSTR();
				String CATE = Dat.isDrop() ? "DROP" : "SWEEP";
				if(contador==0){
					DropData +="<tr>";
				}
				
				DropData += GrillaDrop.replace("%IDIMG%", IMAGEN_ID).replace("%NOMBRE%", Nombre).replace("%DROPMIN%", MIN).replace("%DROPMAX%", MAX).replace("%PORCENTAJE%", CHANCE).replace("%TIPODROP%", CATE);
				contador++;
				if(contador==3){
					DropData += "</tr>";
					contador=0;
				}
			}
			contDrop++;
			if(contDrop>Hasta){
				haveNext=true;
			}			
		}
		
		if(contador>0 && contador<3){
			for(int i=contador;i<3;i++){
				DropData += "<td fixwidth=32><table width=32 cellspacing=0><tr><td></td></tr></table></td><td>"+
						"<table fixwidth=198 border=0 cellspacing=1><tr><td fixwidth=218><font color=B3C624></font></td></tr><tr><td fixwidth=218><font color=2ECCFA></font><font color=2E9AFE></font></td></tr>"+
						"<tr><td fixwidth=218><font color=C6D6DA></font><font color=8BC7D9></font></td></tr></table><br></td>";
			}
			DropData += "</tr>";
		}
		
		DropData += "</table></center>";
		
		
		String Controls = "";
		
		if(haveNext || pagina>0 ){
			
			String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina-1)+";"+ String.valueOf(PaginaIndex) +";0;0"; 
			String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina+1)+";"+ String.valueOf(PaginaIndex) +";0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";				
			
			String Grilla = "<center><table fixwidth=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
            "<tr><td width=32>"+ ( pagina>0 ? btnAntes : "") +"</td>"+
                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(pagina+1) +"</font></td>"+
                "<td width=32>"+ (haveNext ? btnProx : "") +"</td></tr></table></td></tr></table></center>";
			
			DropData += Grilla;
		}		
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch-droplist.htm");

		html.replace("%DATA%", DropData);
		html.replace("%CONTROLS%", Controls);
		
		return html.getHtml();
	}	
	
	
	private static Vector<String>getMonsterDyes(int IdNpc){
		
		raidInfoClass ClassRB = null;
		
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId() != IdNpc){
				continue;
			}
			ClassRB = TT;
			break;
		}		
		
		L2Npc Monster = ClassRB.getL2Npc(true);
		Vector<String>monsterInfoSymbol = new Vector<String>();
		monsterInfoSymbol.add("STR:" + String.valueOf(Monster.getSTR()));
		monsterInfoSymbol.add("DEX:" + String.valueOf(Monster.getDEX()));
		monsterInfoSymbol.add("CON:" + String.valueOf(Monster.getCON()));
		monsterInfoSymbol.add("INT:" + String.valueOf(Monster.getINT()));
		monsterInfoSymbol.add("WIT:" + String.valueOf(Monster.getWIT()));
		monsterInfoSymbol.add("MEN:" + String.valueOf(Monster.getMEN()));
		ClassRB.getL2Npc(false);
		return monsterInfoSymbol;
	}
	
	private static Vector<String>getMonsterElements(int IdNpc){
		raidInfoClass ClassRB = null;
		
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId() != IdNpc){
				continue;
			}
			ClassRB = TT;
			break;
		}		
		
		L2Npc Monster = ClassRB.getL2Npc(true);
		byte attackAttribute = Monster.getAttackElement();
		Vector<String>monsterInfoElemental = new Vector<String>();
		monsterInfoElemental.add("Atk. Element:" + Elementals.getElementName(attackAttribute));
		monsterInfoElemental.add("Atk. Element Value:" + Monster.getAttackElementValue(attackAttribute));
		monsterInfoElemental.add("Fire Def.:" + Monster.getDefenseElementValue(Elementals.FIRE));
		monsterInfoElemental.add("Water Def.:" + Monster.getDefenseElementValue(Elementals.WATER));
		monsterInfoElemental.add("Wind Def.:" + Monster.getDefenseElementValue(Elementals.WIND));
		monsterInfoElemental.add("Earth Def.:" + Monster.getDefenseElementValue(Elementals.EARTH));
		monsterInfoElemental.add("Holy Def.:" + Monster.getDefenseElementValue(Elementals.HOLY));
		monsterInfoElemental.add("Dark Def.:" + Monster.getDefenseElementValue(Elementals.DARK));
		ClassRB.getL2Npc(false);
		return monsterInfoElemental;
	}	
	
	
	private static Vector<String>getMonsterInfo(int idMonster){
		Vector<String> monsterInfo = new Vector<String>();
		int SpawnCount = SpawnTable.getInstance().getSpawns(idMonster).size();
		
		int DropCount = 0;
		
		raidInfoClass ClassRB = null;
		
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId() != idMonster){
				continue;
			}
			ClassRB = TT;
			break;
		}
		
		
		L2NpcTemplate tmpl = ClassRB.getL2NpcTemplate();
		L2Npc Monster = ClassRB.getL2Npc(true);
		
		
		for (_InfoGeneralDrop tt : ClassRB.getAllDrop()){
			DropCount++;
		}
		
		monsterInfo.add("Spawned Count:" + String.valueOf(SpawnCount));
		monsterInfo.add("Minions Count:" + String.valueOf( tmpl.getMinionData().size()));
		monsterInfo.add("Exp Reward:" + String.valueOf( tmpl.getRewardExp()));
		monsterInfo.add("SP Reward:" + String.valueOf( tmpl.getRewardSp()));
		monsterInfo.add("Max HP:" + String.valueOf( Monster.getMaxHp() ));
		monsterInfo.add("Max MP:" + String.valueOf( Monster.getMaxMp() ));
		monsterInfo.add("P. Atack:" + String.valueOf( Monster.getPAtk(null)));
		monsterInfo.add("M. Atack:" + String.valueOf( Monster.getMAtk(null, null)));
		monsterInfo.add("P. Def:" + String.valueOf( Monster.getPDef(null)));
		monsterInfo.add("M. Def:" + String.valueOf( Monster.getMDef(null,null)));
		monsterInfo.add("Attack Speed:" + String.valueOf( Monster.getPAtkSpd()));
		monsterInfo.add("Casting Speed:" + String.valueOf( Monster.getMAtkSpd()));
		monsterInfo.add("Run Speed:" + String.valueOf( Math.round(Monster.getRunSpeed())));
		monsterInfo.add("Drop Amount:" + String.valueOf( DropCount));
		ClassRB.getL2Npc(false);
		return monsterInfo;
	}	
	
	
	
	private static String getRbData(int id){
		L2NpcTemplate temp = NpcTable.getInstance().getTemplate(id);
		return temp.getName() + " - ("+ String.valueOf( temp.getLevel() ) + ")";
	}
	private static int getRbLevel(int id){
		L2NpcTemplate temp = NpcTable.getInstance().getTemplate(id);
		return temp.getLevel();
	}	
	
	public static void _loadRaidInfo(){
		try{
			INFO_RAIDBOSS.clear();
		}catch(Exception a){
			
		}

		String Qry = "SELECT boss_id, loc_x, loc_y, loc_z, IF(raidboss_spawnlist.respawn_time !=0,FROM_UNIXTIME(ROUND(raidboss_spawnlist.respawn_time/1000, 0)),\"0\") FROM raidboss_spawnlist";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Qry);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						String Nombre = getRbData(rss.getInt(1)); 
						int Level = getRbLevel(rss.getInt(1));
						int XX = rss.getInt(2);
						int YY = rss.getInt(3);
						int ZZ = rss.getInt(4);
						int IDD = rss.getInt(1);
						String Respawn = rss.getString(5);
						raidInfoClass t = new raidInfoClass(Nombre,Level,XX,YY,ZZ,IDD,Respawn);
						INFO_RAIDBOSS.add(t);
					}catch(SQLException e){
						
					}
				}
				conn.close();
			}catch(SQLException a){
				_log.warning("Error loading RaidBoss info -> " + a.getMessage());
			}
		try{
			conn.close();
		}catch (Exception e) {

		}		
	}
	
	
	private static final Vector<raidInfoClass> getRaidBossInfo(String Sort){
		Comparator<raidInfoClass> NameAZ = (p1,p2) -> p1.getName().compareToIgnoreCase(p2.getName());
		Comparator<raidInfoClass> NameZA = (p1,p2) -> p2.getName().compareToIgnoreCase(p1.getName());
		
		Comparator<raidInfoClass> LevelAZ = (p1,p2) -> Integer.compare(p1.getLevel(),p2.getLevel());
		Comparator<raidInfoClass> LevelZA = (p1,p2) -> Integer.compare(p2.getLevel(),p1.getLevel());

		Comparator<raidInfoClass> StatusAZ = (p1,p2) -> p1.isAliveInt().compareToIgnoreCase(p2.isAliveInt()); 
		Comparator<raidInfoClass> StatusZA = (p1,p2) -> p2.isAliveInt().compareToIgnoreCase(p1.isAliveInt());
		
		switch(Sort){
			case "0":
			case "nameAZ":
				Collections.sort(INFO_RAIDBOSS,NameAZ);
				break;
			case "nameZA":
				Collections.sort(INFO_RAIDBOSS,NameZA);
				break;
			case "levelAZ":
				Collections.sort(INFO_RAIDBOSS,LevelAZ);
				break;
			case "levelZA":
				Collections.sort(INFO_RAIDBOSS,LevelZA);
				break;
			case "statusAZ":
				Collections.sort(INFO_RAIDBOSS,StatusAZ);
				break;
			case "statusZA":
				Collections.sort(INFO_RAIDBOSS,StatusZA);
				break;
		}
		
		return INFO_RAIDBOSS;
		
	}
	
	
	@SuppressWarnings("unused")
	private static String getGrillaRb(int pagina,String buscar, String sortType){
		int Maximo = 15;
		
		String ByPass_NAME = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo + ";Listar;0;0;nameAZ;0;0";
		String ByPass_LVL = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo + ";Listar;0;0;levelAZ;0;0";
		String ByPass_STATUS = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo + ";Listar;0;0;statusAZ;0;0";

		switch(sortType){
			case "nameAZ":
				ByPass_NAME = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo + ";Listar;0;0;nameZA;0;0";
				break;
			case "levelAZ":
				ByPass_LVL = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo + ";Listar;0;0;levelZA;0;0";
				break;
			case "statusAZ":
				ByPass_STATUS = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo + ";Listar;0;0;statusZA;0;0";
				break;
		}
		
		String retorno = "<table width=640 border=0 cellpadding=0 cellspacing=0><tr><td>"+
				"<button value=\"Raid Boss Name\" action=\""+ ByPass_NAME +"\" width=340 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td><td>"+
				"<button value=\"Level\" action=\""+ ByPass_LVL +"\" width=60 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td><td>"+
				"<button value=\"Status\" action=\""+ ByPass_STATUS +"\" width=150 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\">"+
				"</td><td><button value=\"\" action=\"\" width=90 height=16 back=\"L2UI_CH3.HorizontalSliderBarCenter\" fore=\"L2UI_CH3.HorizontalSliderBarCenter\"></td></tr></table>";
		
		String []Colores = {"3B240B","3B170B"};
		
		String Bypass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name() + ";shownpcinfo;%IDNPC%;" + String.valueOf(pagina) + ";"+ sortType +";0;0" ;
		int Contador = 1;
		int Desde = Maximo * pagina;
		int Hasta = Desde + Maximo + 1;
		boolean HaveMore = false;
		
		for(raidInfoClass TT : getRaidBossInfo(sortType)){
			if(buscar.trim().length()>0){
				if(TT.getName().toUpperCase().indexOf(buscar.toUpperCase())<0){
					continue;
				}
			}
			if(Contador >= Desde && Contador < Hasta){
				String Nombre = TT.getName();
				String Level = String.valueOf(TT.getLevel());
				String X = String.valueOf(TT.getRB_X());
				String Y = String.valueOf(TT.getRB_Y());
				String Z = String.valueOf(TT.getRB_Z());
				String BOSSID = String.valueOf(TT.getId());
				String RESPAWN = (TT.isAlive() ? "0" : TT.getRespawnTime());
				
				String EstadoRb = RESPAWN.equalsIgnoreCase("0") ? "<font color=A9F5A9>Alive</font>" :  "<font color=F78181>Death</font>" ; 
				
				retorno += "<table width=640 border=0 cellpadding=0 cellspacing=0 bgcolor="+ Colores[Contador % 2] +" height=43><tr><td fixwidth=340 align=CENTER>"+
	            "<font color=81F7F3>"+ Nombre +"</font>"+ ( !RESPAWN.equalsIgnoreCase("0") ? "<br1>"+RESPAWN : "" ) + "</td>"+
	            "<td fixwidth=60 align=CENTER>"+ Level +"</td><td fixwidth=150 align=CENTER>"+
	            "<font color=A9F5A9>"+ EstadoRb +"</font></td>"+
	            "<td fixwidth=90 align=CENTER><button value=\"Information\" action=\""+ Bypass.replace("%IDNPC%", BOSSID) +"\" width=90 height=38 back=\"L2UI_CT1.Button_DF_Calculator_Over\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
	            "</td></tr></table>";				
			}
			
			if(Contador > Maximo){
				HaveMore = true;
			}
			Contador++;
		}
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo + ";Listar;" +String.valueOf(pagina-1)+";0;"+sortType+";0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo + ";Listar;" +String.valueOf(pagina+1)+";0;"+sortType+";0;0";
		
		String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
		String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
		
		if(HaveMore || pagina>0){
			retorno += "<table fixwidth=620 cellspacing=-1 cellpadding=-1 bgcolor=\""+Colores[Contador % 2]+"\" height=35><tr><td width=70 fixwidth=70 align=CENTER></td><td width=570 fiwidth=570 align=CENTER>"+
            "<table><tr><td>"+
			(pagina > 0 ? btnAntes : "") +
            "</td><td width=50 align=CENTER><font name=\"hs12\"> "+ String.valueOf(pagina + 1) +" </font></td><td>"+
            (HaveMore ? btnProx : "")+
            "</td></tr></table></td><td width=100 fixwidth=100 height=32></td></tr></table>";
		}
		
		
		retorno += "</center>";
		
		return retorno;
	}
	
	private static String getMonsterDropList(int idMonster, int pagina, String Sort){

		Vector<_InfoGeneralDrop>vTemporal = _GeneralDropInfo.getAllDropFromMob(idMonster);
		
		String Retorno = "<center><table fixwidth=690 border=0 cellpadding=0 cellspacing=0>";
		
		String GrillaDrop = "<td width=230><table width=230 background=L2UI_CT1.Windows_DF_TooltipBG height=75><tr><td fixwidth=32>"+
			"<table width=32 cellspacing=0><tr><td><img src=\"%IDIMG%\" width=32 height=32></td></tr></table></td><td>"+
			"<table fixwidth=198 border=0 cellspacing=1><tr><td fixwidth=218><font color=B3C624>%NOMBRE%</font></td></tr>"+
			"<tr><td fixwidth=218><font color=2ECCFA>%DROPMIN%</font> | <font color=2E9AFE>%DROPMAX%</font></td></tr>"+
			"<tr><td fixwidth=218><font color=C6D6DA>Chance: </font><font color=8BC7D9>%PORCENTAJE% %TIPODROP%</font></td></tr></table><br></td></tr></table></td>";
		
		
		int max=15;
		int contador=0;
		int contDrop = 0;
		int Desde = pagina * max;
		int Hasta = Desde + max;
		boolean haveNext = false;
		
		for(_InfoGeneralDrop Dat : vTemporal){
			if(contDrop>=Desde && contDrop<Hasta){
				String Nombre = Dat.getItemName();
				String IMAGEN_ID = Dat.getItemIcon();
				String MIN = opera.getFormatNumbers(Dat.getMin());
				String MAX = opera.getFormatNumbers(Dat.getMax());
				String CHANCE = Dat.getChanceSTR();
				String CATE = Dat.isDrop() ? "DROP" : "SWEEP";
				if(contador==0){
					Retorno +="<tr>";
				}
				
				Retorno += GrillaDrop.replace("%IDIMG%", IMAGEN_ID).replace("%NOMBRE%", Nombre).replace("%DROPMIN%", MIN).replace("%DROPMAX%", MAX).replace("%PORCENTAJE%", CHANCE).replace("%TIPODROP%", CATE);
				contador++;
				if(contador==3){
					Retorno += "</tr>";
					contador=0;
				}
			}
			contDrop++;
			if(contDrop>Hasta){
				haveNext=true;
			}			
		}
		
		if(contador>0 && contador<3){
			for(int i=contador;i<3;i++){
				Retorno += "<td fixwidth=32><table width=32 cellspacing=0><tr><td></td></tr></table></td><td>"+
						"<table fixwidth=198 border=0 cellspacing=1><tr><td fixwidth=218><font color=B3C624></font></td></tr><tr><td fixwidth=218><font color=2ECCFA></font><font color=2E9AFE></font></td></tr>"+
						"<tr><td fixwidth=218><font color=C6D6DA></font><font color=8BC7D9></font></td></tr></table><br></td>";
			}
			Retorno += "</tr>";
		}
		
		Retorno += "</table></center>";
		
		
		if(haveNext || pagina>0 ){
			
			String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina-1)+";"+Sort+";0;0"; 
			String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina+1)+";"+Sort+";0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";				
			
			String Grilla = "<center><table fixwidth=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
            "<tr><td width=32>"+ ( pagina>0 ? btnAntes : "") +"</td>"+
                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(pagina+1) +"</font></td>"+
                "<td width=32>"+ (haveNext ? btnProx : "") +"</td></tr></table></td></tr></table></center>";
			
			Retorno += Grilla;
		}		
		return Retorno;
	}
	
	@SuppressWarnings("unused")
	private static String getBackBtn(String ByPass,int Pagina,L2PcInstance player,String Busqueda, String Sort){
		if(ByPass.equals("shownpcinfo")){
			String ByPassWhere = FROM_MENU.get(player.getObjectId()) ? "FromMain" : "Listar";
			return "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name() + ";" + ByPassWhere + ";" +  String.valueOf(Pagina) + ";0;"+ Sort +";0;0";
		}else if(ByPass.equals("showDrop")){
			return "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.RaidBossInfo.name() + ";shownpcinfo;" + Busqueda  + ";" + PaginaAntes.get(player.getObjectId()) + ";0;"+Sort+";0;0";
		}
		return "";
	}
	
	private static String mainHtml(L2PcInstance player, String Params, String Buscar, int pagina, String SortType){
		NpcHtmlMessage html = null;
		String ByPassVoice = "bypass -h ZeuS raidbossSearch $txtRaidName";
		String Retorno = "";
		if(Params.equals("0") || Params.equals("FromMain")){
			Retorno = getGrillaRb(pagina,"", SortType);
			html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-raidboss-index.htm");
			html.replace("%DATA%", Retorno);
			html.replace("%BYPASS_SEARCH%", ByPassVoice);
			Retorno = html.getHtml();
		}else if(Params.equals("Listar")){
			Retorno = getGrillaRb(pagina,PALABRA_BUSCAR.get(player.getObjectId()), SortType);
			html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-raidboss-index.htm");
			html.replace("%DATA%", Retorno);
			html.replace("%BYPASS_SEARCH%", ByPassVoice);
		}else if(Params.equals("shownpcinfo")){
			PaginaAntes.put(player.getObjectId(), String.valueOf(pagina));
			Retorno = getInfoNPC(player, Integer.parseInt(Buscar), SortType, pagina);
			return Retorno;
		}else if(Params.equals("showDrop")){
			/*String PagCen = PaginaAntes.get(player.getObjectId());
			Retorno = getMonsterDropList(Integer.valueOf(Buscar), pagina, SortType);
			html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-raidboss-drop-list.htm");
			String BypassBackInfo = "bypass "+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.RaidBossInfo +";shownpcinfo;"+ Integer.valueOf(Buscar) +";"+ PagCen +";0;0;0;0";
			html.replace("%DATA%", Retorno);
			html.replace("%BYPASS_DROP_BACK%", BypassBackInfo);*/

			Retorno = getDroplistWindows(player, pagina, Integer.parseInt(Buscar), SortType);
//			Retorno = getMonsterDropList(Integer.parseInt(Buscar), pagina, player, pagina);
			return Retorno;
		}
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	private static String getDroplistWindows(L2PcInstance player, int pagina, int idRB, String SortType) {
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-raidboss-drop-list.htm");
		String LinkBack = "bypass "+ general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";"+ Engine.enumBypass.RaidBossInfo +";shownpcinfo;"+ Integer.valueOf(idRB) +";"+ pagina +";0;0;0;0";
		String DropData = "";
		String GrillaDropBox = opera.getGridFormatFromHtml(html, 1, "%DATA_GRID%");

		//Vector<_InfoGeneralDrop>vTemporal = _GeneralDropInfo.getAllDropFromMob(idMonster, player);
		Vector<_InfoGeneralDrop>vTemporal = _GeneralDropInfo.getAllDropFromMob(idRB);
		
		int max=15;
		int contador=0;
		int contDrop = 0;
		int Desde = pagina * max;
		int Hasta = Desde + max;
		boolean haveNext = false;
		
		for(_InfoGeneralDrop Dat : vTemporal){
			if(contDrop>=Desde && contDrop<Hasta){
				String Nombre = Dat.getItemName();
				String IMAGEN_ID = Dat.getItemIcon() == null ? "icon.Magic13" : Dat.getItemIcon() ;
				String MIN = opera.getFormatNumbers(Dat.getMin() );
				String MAX = opera.getFormatNumbers(Dat.getMax() );
				String CHANCE = Dat.getChanceSTR();
				String CATE = Dat.isDrop() ? "DROP" : "SWEEP";
				if(contador==0){
					DropData +="<tr>";
				}
				DropData += GrillaDropBox.replace("%IDIMG%", IMAGEN_ID).replace("%NOMBRE%", Nombre).replace("%DROPMIN%", MIN).replace("%DROPMAX%", MAX).replace("%PORCENTAJE%", CHANCE).replace("%TIPODROP%", CATE);
				contador++;
				if(contador==3){
					DropData += "</tr>";
					contador=0;
				}
			}
			contDrop++;
			if(contDrop>Hasta){
				haveNext=true;
			}			
		}
		
		if(contador>0 && contador<3){
			for(int i=contador;i<3;i++){
				DropData += GrillaDropBox.replace("%IDIMG%", "").replace("%NOMBRE%", "").replace("%DROPMIN%", "").replace("%DROPMAX%", "").replace("%PORCENTAJE%", "").replace("%TIPODROP%", "");
			}
			DropData += "</tr>";
		}
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo.name() + ";showDrop;" + String.valueOf(idRB) + ";" +String.valueOf(pagina-1)+";"+ SortType +";0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.RaidBossInfo.name() + ";showDrop;" + String.valueOf(idRB) + ";" +String.valueOf(pagina+1)+";"+ SortType +";0;0;0";

		html.replace("%PAG_NUMBERS%", String.valueOf(pagina+1));
		html.replace("%BYPASS_PREV%", (pagina <=0 ? "" : bypassAntes));
		html.replace("%BYPASS_NEXT%", (haveNext ? bypassProx : ""));			

		html.replace("%DATA_GRID%", DropData);
		html.replace("%LINK_BACK%", LinkBack);
		
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_RAIDBOSS_INFO) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}					
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			FROM_MENU.put(player.getObjectId(), false);
			PALABRA_BUSCAR.put(player.getObjectId(), "");
			return mainHtml(player,parm1,"", Integer.valueOf(parm2), parm4);
		}else if(parm1.equals("Listar")){
			return mainHtml(player,parm1,"",Integer.valueOf(parm2), parm4);
		}else if(parm1.equals("FromMain")){
			FROM_MENU.put(player.getObjectId(), true);
			PALABRA_BUSCAR.put(player.getObjectId(), "");
			return mainHtml(player,parm1,"", Integer.valueOf(parm2), parm4);
		}else if(parm1.equals("shownpcinfo")){
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3), parm4) ;
		}else if(parm1.equals("showDrop")){
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3), parm4);
		}else if(parm1.equals("ObserveAsk")){
			if(!general.RAIDBOSS_OBSERVE_MODE){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";
			}
			if(player.inObserverMode()){
				central.msgbox(language.getInstance().getMsg(player).DS_YOU_NEED_TO_CLOSE_OBSERVER_MODE_TO_OBSERVER_ANOTHER, player);
				return "";
			}
			Integer x =  Integer.valueOf(parm3);
			Integer y = Integer.valueOf(parm4);
			Integer z = Integer.valueOf(parm5);
			TELEPORT_TO.put(player.getObjectId(), x + "," + y + "," + z);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DS_YOU_WANT_TO_OBSERVER_THIS, IdDialog.ENGINE_RAIDBOSSSEARCH_OBSERVE);			
		}else if(parm1.equals("Observe")){
			Location loc = new Location(Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[0]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[1]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[2]));
			ObserveMode.EnterObserveMode(player, loc);
			return "";
		}else if(parm1.equals("teleportAsk")){
			String idMonster = parm2;			
			if(!general.RAIDBOSS_INFO_TELEPORT){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";
			}
			
			if(general.RAIDBOSS_ID_MOB_NO_TELEPORT.contains(Integer.valueOf(idMonster))){
				central.msgbox(language.getInstance().getMsg(player).DS_MOB_OR_RB_BLOCKED_TELEPORT, player);
				return "";
			}
			
			if(!general.RAIDBOSS_INFO_TELEPORT){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";
			}
			
			if(general.RAIDBOSS_INFO_NOBLE && !player.isNoble()){
				central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_NOBLE, player);
				return "";
			}
			
			if(player.getLevel() < general.RAIDBOSS_INFO_LVL){
				central.msgbox(language.getInstance().getMsg(player).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.RAIDBOSS_INFO_LVL)), player);
				return "";
			}
			
			if(general.RAIDBOSS_ID_MOB_NO_TELEPORT.contains(Integer.valueOf(idMonster))){
				central.msgbox(language.getInstance().getMsg(player).DS_MOB_OR_RB_BLOCKED_TELEPORT, player);
				return "";
			}			
			
			Integer x =  Integer.valueOf(parm3);
			Integer y = Integer.valueOf(parm4);
			Integer z = Integer.valueOf(parm5);
			
			TELEPORT_TO.put(player.getObjectId(), x + "," + y + "," + z);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DS_YOU_WANT_TO_TELEPORT_THIS, IdDialog.ENGINE_RAIDBOSSSEARCH_TELEPORT);			
		}else if(parm1.equals("teleport")){
			if(general.RAIDBOSS_INFO_TELEPORT_PRICE != ""){
				if(!opera.haveItem(player, general.RAIDBOSS_INFO_TELEPORT_PRICE)){
					return "";
				}
				opera.removeItem(general.RAIDBOSS_INFO_TELEPORT_PRICE, player);
			}
			player.teleToLocation(Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[0]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[1]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[2]), true);
			player.setInstanceId(0);
			cbFormato.cerrarCB(player);			
		}
		return "";
	}
	
	public static void setStatusToRB(int idRB, boolean status, String SpawnTime){
		for(raidInfoClass TT : INFO_RAIDBOSS){
			if(TT.getId() == idRB){
				TT.setStatus(status,SpawnTime);
				break;
			}
		}
	}
	
	public static void bypassVoice(L2PcInstance player, String Buscar){
		String enviar = "";
		PALABRA_BUSCAR.put(player.getObjectId(), Buscar);
		enviar = mainHtml(player, "Listar", Buscar, 0, "nameAZ");
		cbManager.separateAndSend(enviar, player);
	}
}
class raidInfoClass{
	@SuppressWarnings("unused")
	private Logger _log = Logger.getLogger(v_RaidBossInfo.class.getName());
	private String RB_NAME;
	private int RB_LEVEL;
	private int LOC_X, LOC_Y, LOC_Z;
	private int RB_ID;
	private int ID_MOB_GROUP;
	private String RB_RESPAWN;
	private boolean RB_ALIVE;
	private L2Npc NPC_class;
	private Vector<_InfoGeneralDrop> RAID_DROP = new Vector<_InfoGeneralDrop>();
	@SuppressWarnings("unused")
	private L2NpcTemplate NPC_TEMPLATE_class;
	public raidInfoClass(String Name, int Level, int X, int Y, int Z, int BossID, String Respawn){
		this.RB_NAME = Name;
		this.LOC_X = X;
		this.LOC_Y = Y;
		this.LOC_Z = Z;
		this.RB_ID = BossID;
		this.RB_LEVEL = Level;
		this.RB_RESPAWN = Respawn;
		if(Respawn == "0"){
			this.RB_ALIVE = true;
		}else{
			this.RB_ALIVE = false;
		}
		this.RAID_DROP = _GeneralDropInfo.getAllDropFromMob(BossID);
	}
	public final String getName(){
		return this.RB_NAME;
	}
	
	public final Vector<_InfoGeneralDrop> getAllDrop(){
		return this.RAID_DROP;
	}
	
	public final int getLevel(){
		return this.RB_LEVEL;
	}
	
	public final int getId(){
		return this.RB_ID;
	}
	
	public void setStatus(boolean status, String respawnTime){
		this.RB_ALIVE = status;
		if(!status){
			_getRespawnTime();
		}
	}
	
	public final L2NpcTemplate getL2NpcTemplate(){
		L2NpcTemplate template = NpcTable.getInstance().getTemplate(this.RB_ID);
		return template;
	}
	
	public final L2Npc getL2Npc(boolean load){
		if(load){
			int idMobMenuGeneral = opera.getUnixTimeNow();
			idMobMenuGeneral = idMobMenuGeneral + this.RB_ID;
			L2NpcTemplate template = NpcTable.getInstance().getTemplate(this.RB_ID);
			MobGroup group = new MobGroup(idMobMenuGeneral, template, 1);
			MobGroupTable.getInstance().addGroup(idMobMenuGeneral, group);
			group.spawnGroup(100, 100, 100, 0);
			L2Npc temp = group.getMobs().get(0);
			this.NPC_class = temp;
			this.ID_MOB_GROUP = idMobMenuGeneral;
		}else{
			this.NPC_class = null;
			MobGroup group = MobGroupTable.getInstance().getGroup(this.ID_MOB_GROUP);
			try{
				group.unspawnGroup();
			}catch(Exception a){
				
			}
			MobGroupTable.getInstance().removeGroup(this.ID_MOB_GROUP);
		}
		return this.NPC_class;
		
	}
	
	public final String isAliveInt(){
		if(!this.RB_ALIVE){
			return "Death";
		}
		return "Alive";
	}
	
	public final boolean isAlive(){
		return this.RB_ALIVE;
	}
	
	public final String getRespawnTime(){
		return this.RB_RESPAWN;
	}
	
	@SuppressWarnings("unused")
	private final void _getRespawnTime(){
		String Retorno = "";
		String Consulta = "SELECT IF(raidboss_spawnlist.respawn_time !=0,FROM_UNIXTIME(ROUND(raidboss_spawnlist.respawn_time/1000, 0)),\"0\") FROM raidboss_spawnlist WHERE boss_id=?";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			psqry.setInt(1, this.RB_ID);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					this.RB_RESPAWN = rss.getString(1);
				}
		}catch(Exception a){
			
		}		
	}
	
	public final int getRB_X(){
		return LOC_X;
	}
	
	public final int getRB_Y(){
		return LOC_Y;
	}
	
	public final int getRB_Z(){
		return LOC_Z;
	}
}