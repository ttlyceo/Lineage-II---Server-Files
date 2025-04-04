package ZeuS.Comunidad.EngineForm;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._GeneralDropInfo;
import ZeuS.Config._InfoGeneralDrop;
import ZeuS.Config._dropsearch;
import ZeuS.Config._itemInfo;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.ObserveMode;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;
import javafx.scene.input.DataFormat;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.log.filter.Log;

public class v_dropsearch{
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(v_dropsearch.class.getName());
	//private static Map<Integer, HashMap<Integer, String>> INFO_FOR_SEARCH = new FastMap<Integer, Map<Integer, String>>();
	private static Map<Integer, _dropCache> INFO_SEARCH = new HashMap<Integer, _dropCache>();
	private static Map<Integer, String> LastWordToSave = new HashMap<Integer, String>();
	private static Map<Integer, Boolean> isDropEngine = new HashMap<Integer, Boolean>();
	private static Map<Integer, String> TELEPORT_TO = new HashMap<Integer, String>();
	private static Map<Integer, String> TYPE_SORT = new HashMap<Integer, String>();
	private static Map<Integer, Vector<_dropsearch>> PERSONAL_SORT = new HashMap<Integer, Vector<_dropsearch>>();
	
	private static Vector<Integer> DROP_LIST_ITEM = new Vector<Integer>();
	
	private static String []IconosTop = {"icon.skill0930","icon.skill_transform_buff","icon.skill_transform_debuff","icon.skill_transform_etc","icon.skill_transform_s_attack"};
	
	private static Vector<_dropsearch> ALL_NPC = new Vector<_dropsearch>();

	private static boolean isSorted = false;
	
	public static void Load(){
		for(_dropsearch D1: general.GET_DROP_LIST_CLASS()){
			DROP_LIST_ITEM.add(D1.getDropID());
		}
	}
	
	protected static boolean ItemisInDropList(int idItem){
		if(DROP_LIST_ITEM==null){
			return false;
		}
		if(!DROP_LIST_ITEM.contains(idItem)){
			return false;
		}
		return true;
	}

	protected static boolean canUseTeleportBtn(int IDMob){
		if(general.DROP_SEARCH_MOB_BLOCK_TELEPORT.contains(IDMob)){
			return false;
		}
		return true;
	}
	
	public static void showInfoNpxFromShift(L2PcInstance player, int idNpc){
		isDropEngine.put(player.getObjectId(), false);
		String ByPass = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";showmonsterinfo;" + String.valueOf(idNpc) + ";" + "0;0;0;0";
		cbManager.separateAndSend(bypass(player,ByPass), player);
	}
	
	private static String getMonsterDropList(int idMonster, int pagina, L2PcInstance player){
		String URLHTML = "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch-droplist.htm";
		NpcHtmlMessage html = comun.htmlMaker(player, URLHTML);
		String LinkBack = "bypass _bbslink;DropSearch;showmonsterinfo;"+ String.valueOf(idMonster) +";0;0;0;0;0";
		String DropData = "";
		String GrillaDropBox = opera.getGridFormatFromHtml(html, 1, "%DATA_GRID%");

		if(GrillaDropBox == "") {
			_log.warning("Error trying found Grid Information on: " + URLHTML);
		}
		
		Vector<_InfoGeneralDrop>vTemporal = _GeneralDropInfo.getAllDropFromMob(idMonster);		
		
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
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina-1)+";0;0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch.name() + ";showDrop;" + String.valueOf(idMonster) + ";" +String.valueOf(pagina+1)+";0;0;0;0";

		html.replace("%PAG_NUMBERS%", String.valueOf(pagina+1));
		html.replace("%BYPASS_PREV%", (pagina <=0 ? "" : bypassAntes));
		html.replace("%BYPASS_NEXT%", (haveNext ? bypassProx : ""));			

		html.replace("%DATA_GRID%", DropData);
		html.replace("%LINK_BACK%", LinkBack);
		
		return html.getHtml();
	}
	
	
	
	
	public static void setNPCInfoForSearch(String NPCINFO){
		if(isSorted){
			return;
		}
		getAllNPCForSearch(true);
		isSorted=true;
	}
	
	@SuppressWarnings("unused")
	private static boolean isValidNPC(int idNpc){
		try{
			L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(idNpc);
			L2Npc Monster = null;
			for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(idNpc)){
				if(Monster==null){
					Monster = SpawnLo.getLastSpawn();
				}
			}
		}catch(Exception a){
			return false;
		}
		
		return true;
	}
	
	@SuppressWarnings("unused")
	private static boolean canAddNpcByLevel(L2NpcTemplate npcc){
		try{
			int npccLv = npcc.getLevel();
			return true;
		}catch(Exception a){
			
		}
		return false;
	}
	
	public static void getAllNPCForSearch(boolean valid){
		String []tipos = {"L2Monster","L2SepulcherMonster","L2FlyMonster"};
		String temp = "";
		
		Vector<String> InfTemporal = new Vector<String>();
		
		for(String _type: tipos){
			for(L2NpcTemplate Npcc : NpcTable.getInstance().getAllNpcOfClassType(_type)){
				if( !canAddNpcByLevel(Npcc) ){
					continue;
				}
				temp = Npcc.getName() + ":" + String.valueOf(Npcc.getLevel()) + ":" + String.valueOf(Npcc.getId());
				if(!InfTemporal.contains(temp)){
					try {
						if(isValidNPC(Npcc.getId())){
							int DropCount = getDrop(Npcc,false);
							int DropCount_spoil = getDrop(Npcc,true);
							L2Npc Monster = null;
							for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(Npcc._id)){
								if(Monster==null){
									Monster = SpawnLo.getLastSpawn();
									break;
								}
							}
							_dropsearch T1 = new _dropsearch(Npcc.getName(), Npcc.getId(), Npcc.getLevel(), Npcc.isAggressive(), DropCount, DropCount_spoil, Npcc, Monster);
							ALL_NPC.add(T1);
							InfTemporal.add(temp);
						}
					} catch (Exception e){
						
					} 
				}
			}
		}
	}
	
	
	@SuppressWarnings("unused")
	private static L2Npc getL2NpcMonster(int IdNpc){
		L2Npc Monster = null;
		for(_dropsearch npcM : general.GET_DROP_LIST_CLASS()){
			if(npcM.getNpcID() != IdNpc){
				continue;
			}
			Monster = npcM.getNpc();
			break;
		}
		return Monster;
	}
	
	@SuppressWarnings("unused")
	private static L2NpcTemplate getTemplateMonster(int IdNpc){
		L2NpcTemplate Monster = null;
		for(_dropsearch npcM : general.GET_DROP_LIST_CLASS()){
			if(npcM.getNpcID() != IdNpc){
				continue;
			}
			Monster = npcM.getNpcTemplace();
			break;
		}
		return Monster;		
	}
	
	private static String getInfoNPC(L2PcInstance player, int idMonster){
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch-npc-info.htm");
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(idMonster);
		L2Npc Monster = null;
		for(L2Spawn SpawnLo : SpawnTable.getInstance().getSpawns(idMonster)){
			if(Monster==null){
				Monster = SpawnLo.getLastSpawn();
				int LevelMonster=0;
				try {
					LevelMonster = Monster.getTemplate().getLevel();
					if(Monster.getInstanceId()>0){
						Monster=null;						
					}
				}catch(Exception a) {
					Monster=null;
				}
			}
		}
		
		if(Monster == null){
			central.msgbox("This NPC Have problem. We cant load the NPC / Monster Information.", player);
			cbFormato.cerrarCB(player);
			return "";
		}
		
		int DropCount = 0;
		for (_InfoGeneralDrop Dat : _GeneralDropInfo.getAllDropFromMob(idMonster)){
			DropCount++;
		}
		int LevelMonster = Monster.getTemplate().getLevel();
		String Nombre = Monster.getTemplate().getName();
		int SpawnCount = SpawnTable.getInstance().getSpawns(idMonster).size();
		
		String x = String.valueOf(Monster.getLocation().getX());
		String y = String.valueOf(Monster.getLocation().getY());
		String z = String.valueOf(Monster.getLocation().getZ());
		byte attackAttribute = Monster.getAttackElement();
		
		String ByPassBack = "";
		
		boolean isTopItemTop = false;
		boolean isTopMobTop = false;
		
		isTopMobTop = INFO_SEARCH.get(player.getObjectId()).isFromMobTop;
		isTopItemTop = INFO_SEARCH.get(player.getObjectId()).isFromItemTop;

		boolean isFromItemSearch = INFO_SEARCH.get(player.getObjectId()).isFromItemSearch;
		
		if(isFromItemSearch){
			if(isTopItemTop){
				ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";showMonster;"+ INFO_SEARCH.get(player.getObjectId()).ID_ITEM +";"+ INFO_SEARCH.get(player.getObjectId()).PAG_MOB +";0;0;0";
			}else{
				ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";showMonster;"+ INFO_SEARCH.get(player.getObjectId()).ID_ITEM +";"+ INFO_SEARCH.get(player.getObjectId()).PAG_MOB +";true;0;0";
			}
		}else{
			if(isTopMobTop){
				ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";0;0;0;0;0;0";
			}else{
				ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";showMonster;" + INFO_SEARCH.get(player.getObjectId()).ID_ITEM + ";"+ INFO_SEARCH.get(player.getObjectId()).PAG_MOB +";0;0;0";
			}
		}
		DecimalFormat df = new DecimalFormat("#.##");
		
		html.replace("%BYPASS_BACK%", ByPassBack);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%NPC_NAME%", Nombre);
		html.replace("%NPC_LEVEL%", String.valueOf(LevelMonster));
		html.replace("%SPAWNED_COUNT%", String.valueOf(SpawnCount));
		html.replace("%MINIONS_COUNT%", String.valueOf( tmpl.getMinionData().size()));
		html.replace("%EXP_REWARD%", String.valueOf( tmpl.getRewardExp()));
		html.replace("%SP_REWARD%", String.valueOf(tmpl.getRewardSp()));
		html.replace("%MAX_HP%", String.valueOf(Monster.getMaxHp()));
		html.replace("%MAX_MP%", String.valueOf(Monster.getMaxMp()));
		html.replace("%P_ATTACK%", String.valueOf(df.format(Monster.getPAtk(null))));
		html.replace("%M_ATTACK%", String.valueOf(df.format(Monster.getMAtk(null, null))));
		html.replace("%P_DEF%", String.valueOf(df.format(Monster.getPDef(null))));
		html.replace("%M_DEF%", String.valueOf(df.format(Monster.getMDef(null,null))));
		html.replace("%ATTACK_SPEED%", String.valueOf(df.format(Monster.getPAtkSpd())));
		html.replace("%CASTING_SPEED%", String.valueOf(Monster.getMAtkSpd()));
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

		boolean isFromEngine = isDropEngine.get(player.getObjectId());
		
		String bypass_ShowDrop = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" +Engine.enumBypass.DropSearch.name()+";showDrop;"+ String.valueOf(idMonster) +";0;0;0;0;0" ;
		String bypass_ShowSkill = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+ ";ShowNPCSkill;" + String.valueOf(idMonster)+";0;0;0;0;0";
		String bypass_TeleportMe = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name()+";teleportAsk;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		String bypass_Observe = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name()+";ObserveAsk;" + idMonster +";" + x + ";" + y + ";" + z + ";0";
		
		html.replace("%BYPASS_SHOW_DROP%", bypass_ShowDrop);
		html.replace("%BYPASS_SHOW_SKILL%", bypass_ShowSkill);
		html.replace("%BYPASS_TELEPORT_ME%", (isFromEngine ? bypass_TeleportMe : ""));
		html.replace("%BYPASS_OBSERVER%", (isFromEngine ? bypass_Observe : ""));
		
		return html.getHtml();
	}
	
	private static int getDrop(L2NpcTemplate nnpc, boolean JustSpoil){
		int DropCount = 0;
		int DropCountSpoil = 0;

		Vector<_InfoGeneralDrop> vTem = _GeneralDropInfo.getAllDropFromMob(nnpc._id);
		
		for(_InfoGeneralDrop  dato : vTem){
			if(dato.isDrop()){
				DropCount++;
			}else{
				DropCountSpoil++;
			}
		}

		if(!JustSpoil){
			return DropCount;
		}else{
			return DropCountSpoil;
		}
	}
	
	private static String getTablaMonsterByName(L2PcInstance player, String Nom, Integer pagina){
		if(PERSONAL_SORT == null){
			setPlayerSortMonster(player, "nameAZ", Nom);
		}else if(!PERSONAL_SORT.containsKey(player.getObjectId())){
			setPlayerSortMonster(player, "nameAZ", Nom);
		}
		Nom = Nom.replace(" ", "_");
		String ByPassForName = "bypass ZeuS dropsearch2 nameAZ " + Nom;
		String ByPassForLvL = "bypass ZeuS dropsearch2 levelAZ " + Nom;
		String ByPassForAggre = "bypass ZeuS dropsearch2 aggreAZ " + Nom;
		String ByPassForDrops = "bypass ZeuS dropsearch2 dropnormalAZ " + Nom;
		String ByPassForSpoil = "bypass ZeuS dropsearch2 dropspoilAZ " + Nom;
		
		switch(TYPE_SORT.get(player.getObjectId())){
			case "nameAZ":
				ByPassForName = "bypass ZeuS dropsearch2 nameZA " + Nom;
				break;
			case "levelAZ":
				ByPassForLvL = "bypass ZeuS dropsearch2 levelZA " + Nom;
				break;
			case "aggreAZ":
				ByPassForAggre = "bypass ZeuS dropsearch2 aggreZA " + Nom;
				break;
			case "dropnormalAZ":
				ByPassForDrops = "bypass ZeuS dropsearch2 dropnormalZA " + Nom;
				break;
			case "dropspoilAZ":
				ByPassForSpoil = "bypass ZeuS dropsearch2 dropspoilZA " + Nom;
				break;
		}
		
		Nom = Nom.replace("_", " ");
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch-npc-found.htm");
		
		html.replace("%NAME_LINK%", ByPassForName);
		html.replace("%LEVEL_LINK%", ByPassForLvL);
		html.replace("%AGGRE_LINK%", ByPassForAggre);
		html.replace("%DROP_LINK%", ByPassForDrops);
		html.replace("%SPOIL_LINK%", ByPassForSpoil);
		
		int maximoLista = 16;int contador = 0;
		int Desde = pagina * maximoLista; int Hasta = (pagina * maximoLista) + maximoLista;
		boolean haveMore = true;
		
		String GrillaInfo = "<table fixwidth=767 cellspacing=-4 cellpadding=-1 height=25 bgcolor=%BGCOLOR%><tr><td fixwidth=320 align=CENTER>"+
        "%NOMBRE%</td><td fixwidth=42 align=CENTER>%LEVEL%</td><td fixwidth=75 align=CENTER>"+
        "%AGGRE%</td><td fixwidth=120 align=CENTER>%DROPITEM%</td><td fixwidth=120 align=CENTER>"+
        "%DROPSPOIL%</td><td fixwidth=40 align=CENTER><br>"+
        "<button action=\"%BYPASS%\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">"+
        "</td></tr></table>";
		
		String GridNpc = "";
		String Controls = "";
		
		String []Color = {"13270C","0E180B"};
		
		for(_dropsearch Ind : ALL_NPC){
			if(Ind.getName().toUpperCase().indexOf(Nom.toUpperCase())>=0){
				if(contador>=Desde && contador < Hasta){
					String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch+";showmonsterinfo;%NPCID%;0;0;0;0";
					GridNpc+= GrillaInfo.replace("%BYPASS%",ByPass.replace("%NPCID%", String.valueOf(Ind.getNpcID())) ).replace("%DROPSPOIL%", String.valueOf(Ind.getDropsSpoil() )).replace("%DROPITEM%", String.valueOf(Ind.getDropsNornaml())).replace("%AGGRE%", Ind.getIsAggre() ? "Yes":"No") .replace("%LEVEL%", String.valueOf(Ind.getNpcLevel())) .replace("%NOMBRE%", Ind.getName()).replace("%BGCOLOR%",Color[contador % 2]);
				}else if(contador>Hasta){
					haveMore=false;
				}
				contador++;				
			}
		}
		
		if(!haveMore || pagina>0 ){
			
			String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch + ";SearchMonster;" + Nom + ";" +String.valueOf(pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch + ";SearchMonster;" + Nom + ";" +String.valueOf(pagina+1)+";0;0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";				
			
			String Grilla = "<table fixwidth=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
            "<tr><td width=32>"+ ( pagina>0 ? btnAntes : "") +"</td>"+
                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(pagina+1) +"</font></td>"+
                "<td width=32>"+ (!haveMore ? btnProx : "") +"</td></tr></table></td></tr></table>";
			
			Controls = Grilla;
		}		
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%DATA%", GridNpc);
		html.replace("%CONTROL%", Controls);
		
		
		return html.getHtml();
	}	
	
	private static final Vector<_dropsearch> getMosterList(){
		return ALL_NPC;
	}
	
	public static void setPlayerSortMonster(L2PcInstance player, String sort, String NameNPC){
		TYPE_SORT.put(player.getObjectId(), sort);
		
		Comparator<_dropsearch> NameAZ = (p1,p2) -> p1.getName().compareToIgnoreCase(p2.getName());
		Comparator<_dropsearch> NameZA = (p1,p2) -> p2.getName().compareToIgnoreCase(p1.getName());
		
		Comparator<_dropsearch> LevelAZ = (p1,p2) -> Integer.compare(p1.getNpcLevel(),p2.getNpcLevel());
		Comparator<_dropsearch> LevelZA = (p1,p2) -> Integer.compare(p2.getNpcLevel(),p1.getNpcLevel());
		
		Comparator<_dropsearch> AggreAZ = (p1,p2) -> Boolean.compare(p1.getIsAggre(), p2.getIsAggre());
		Comparator<_dropsearch> AggreZA = (p1,p2) -> Boolean.compare(p2.getIsAggre(), p1.getIsAggre());
		
		Comparator<_dropsearch> dropNormalAZ = (p1,p2) -> Integer.compare(p1.getDropsNornaml(),p2.getDropsNornaml());
		Comparator<_dropsearch> dropNormalZA = (p1,p2) -> Integer.compare(p2.getDropsNornaml(),p1.getDropsNornaml());
		
		Comparator<_dropsearch> dropSpoilAZ = (p1,p2) -> Integer.compare(p1.getDropsSpoil(),p2.getDropsSpoil());
		Comparator<_dropsearch> dropSpoilZA = (p1,p2) -> Integer.compare(p2.getDropsSpoil(),p1.getDropsSpoil());		
		
		Vector<_dropsearch> DROP_TO_SORT = getMosterList();
		
		Collections.sort(DROP_TO_SORT,NameAZ);
		
		switch(sort){
			/*case "nameAZ":
				Collections.sort(DROP_TO_SORT,NameAZ);
				break;*/
			case "nameZA":
				Collections.sort(DROP_TO_SORT,NameZA);
				break;
			case "levelAZ":
				Collections.sort(DROP_TO_SORT,LevelAZ);
				break;
			case "levelZA":
				Collections.sort(DROP_TO_SORT,LevelZA);
				break;
			case "aggreAZ":
				Collections.sort(DROP_TO_SORT,AggreAZ);
				break;
			case "aggreZA":
				Collections.sort(DROP_TO_SORT,AggreZA);
				break;
			case "dropnormalAZ":
				Collections.sort(DROP_TO_SORT,dropNormalAZ);
				break;
			case "dropnormalZA":
				Collections.sort(DROP_TO_SORT,dropNormalZA);
				break;
			case "dropspoilAZ":
				Collections.sort(DROP_TO_SORT,dropSpoilAZ);
				break;
			case "dropspoilZA":
				Collections.sort(DROP_TO_SORT,dropSpoilZA);
				break;				
		}
		
		PERSONAL_SORT.put(player.getObjectId(), DROP_TO_SORT);

		//INFO_SEARCH.put(player.getObjectId(), NameNPC + ";0");
		
		String SendHtml = getTablaMonsterByName(player, NameNPC, 0);
		cbManager.separateAndSend(SendHtml, player);		
		
	}	
	
	
	
	public static void setPlayerSortMonsterByItemDrop(L2PcInstance player, String sort, Integer idItem){
		TYPE_SORT.put(player.getObjectId(), sort);
		
		Comparator<_dropsearch> NameAZ = (p1,p2) -> p1.getName().compareToIgnoreCase(p2.getName());
		Comparator<_dropsearch> NameZA = (p1,p2) -> p2.getName().compareToIgnoreCase(p1.getName());
		
		Comparator<_dropsearch> LevelAZ = (p1,p2) -> Integer.compare(p1.getNpcLevel(),p2.getNpcLevel());
		Comparator<_dropsearch> LevelZA = (p1,p2) -> Integer.compare(p2.getNpcLevel(),p1.getNpcLevel());
		
		Comparator<_dropsearch> TypeAZ = (p1,p2) -> p1.getType().compareToIgnoreCase(p2.getType());
		Comparator<_dropsearch> TypeZA = (p1,p2) -> p2.getType().compareToIgnoreCase(p1.getType());
		
		Comparator<_dropsearch> MountAZ = (p1,p2) -> Integer.compare(p1.getDropMax(),p2.getDropMax());
		Comparator<_dropsearch> MountZA = (p1,p2) -> Integer.compare(p2.getDropMax(),p1.getDropMax());
		
		Comparator<_dropsearch> ChanceAZ = (p1,p2) -> Long.compare(Long.parseLong(p1._getDropChance()),Long.parseLong(p2._getDropChance()));
		Comparator<_dropsearch> ChanceZA = (p1,p2) -> Long.compare(Long.parseLong(p2._getDropChance()),Long.parseLong(p1._getDropChance()));
		
		Vector<_dropsearch> DROP_TO_SORT = general.GET_DROP_LIST_CLASS();
		
		Collections.sort(DROP_TO_SORT,NameAZ);
		
		switch(sort){
			/*case "nameAZ":
				Collections.sort(DROP_TO_SORT,NameAZ);
				break;*/
			case "nameZA":
				Collections.sort(DROP_TO_SORT,NameZA);
				break;
			case "levelAZ":
				Collections.sort(DROP_TO_SORT,LevelAZ);
				break;
			case "levelZA":
				Collections.sort(DROP_TO_SORT,LevelZA);
				break;
			case "typeAZ":
				Collections.sort(DROP_TO_SORT,TypeAZ);
				break;
			case "typeZA":
				Collections.sort(DROP_TO_SORT,TypeZA);
				break;
			case "mountAZ":
				Collections.sort(DROP_TO_SORT,MountAZ);
				break;
			case "mountZA":
				Collections.sort(DROP_TO_SORT,MountZA);
				break;
			case "chanceAZ":
				Collections.sort(DROP_TO_SORT,ChanceAZ);
				break;
			case "chanceZA":
				Collections.sort(DROP_TO_SORT,ChanceZA);
				break;				
		}
		
		PERSONAL_SORT.put(player.getObjectId(), DROP_TO_SORT);

		String SendHtml = getTablaMonsterByItemDrop(player, idItem, 0);
		cbManager.separateAndSend(SendHtml, player);		
		
	}
	
	public static void setFromItem(L2PcInstance player, String Buscando){
		CheckIt(player);
		INFO_SEARCH.get(player.getObjectId()).isFromItemSearch = true;
		INFO_SEARCH.get(player.getObjectId()).isFromItemTop = false;
		INFO_SEARCH.get(player.getObjectId()).isFromMobTop = false;
		INFO_SEARCH.get(player.getObjectId()).ItemNameSearching = Buscando;
		INFO_SEARCH.get(player.getObjectId()).MobNameSearhing = "";
	}
	
	public static void setFromMob(L2PcInstance player, String Buscando){
		CheckIt(player);
		INFO_SEARCH.get(player.getObjectId()).isFromItemSearch = false;
		INFO_SEARCH.get(player.getObjectId()).isFromItemTop = false;
		INFO_SEARCH.get(player.getObjectId()).isFromMobTop = false;
		INFO_SEARCH.get(player.getObjectId()).ItemNameSearching = "";
		INFO_SEARCH.get(player.getObjectId()).MobNameSearhing = Buscando;
	}
	
	private static String getTablaMonsterByItemDrop(L2PcInstance player, Integer idItem, Integer pagina){
		
		if(PERSONAL_SORT == null){
			setPlayerSortMonsterByItemDrop(player, "nameAZ", idItem);
		}else if(!PERSONAL_SORT.containsKey(player.getObjectId())){
			setPlayerSortMonsterByItemDrop(player, "nameAZ", idItem);
		}		
		
		boolean isFromTop = INFO_SEARCH.get(player.getObjectId()).isFromItemTop;
		
		String ByPassBack = "";
		
		if(isFromTop){
			ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";0;0;0;0;0;0;0";
		}else{
			if(INFO_SEARCH.get(player.getObjectId()).isFromItemSearch){
				ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";buscarDrop;"+ INFO_SEARCH.get(player.getObjectId()).ItemNameSearching +";"+ INFO_SEARCH.get(player.getObjectId()).PAG_ITEM +";0;0;0;0";	
			}else{
				ByPassBack = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";buscarDrop;0;0;0;0;0;0";
			}
		}
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch-npc-list.htm");
		
		String ByPassForName = "bypass ZeuS dropsearch nameAZ " + idItem;
		String ByPassForLvL = "bypass ZeuS dropsearch levelAZ " + idItem;
		String ByPassForType = "bypass ZeuS dropsearch typeAZ " + idItem;
		String ByPassForMount = "bypass ZeuS dropsearch mountAZ " + idItem;
		String ByPassForChance = "bypass ZeuS dropsearch chanceAZ " + idItem;
		
		switch(TYPE_SORT.get(player.getObjectId())){
			case "nameAZ":
				ByPassForName = "bypass ZeuS dropsearch nameZA " + idItem;
				break;
			case "levelAZ":
				ByPassForLvL = "bypass ZeuS dropsearch levelZA " + idItem;
				break;
			case "typeAZ":
				ByPassForType = "bypass ZeuS dropsearch typeZA " + idItem;
				break;
			case "mountAZ":
				ByPassForMount = "bypass ZeuS dropsearch mountZA " + idItem;
				break;
			case "chanceAZ":
				ByPassForChance = "bypass ZeuS dropsearch chanceZA " + idItem;
				break;
		}
		
		
		String NPCLIST = "<center><table fixwidth=767 cellspacing=-4 cellpadding=-1><tr><td align=CENTER><button value=\"Monster Name\" action=\""+ ByPassForName +"\" width=290 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
        "</td><td align=CENTER><button value=\"Lv\" action=\""+ ByPassForLvL + "\" width=42 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\"></td><td align=CENTER>"+
        "<button value=\"Type\" action=\""+ByPassForType+"\" width=65 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\"></td><td align=CENTER><button value=\"Amount\" action=\""+ByPassForMount+"\" width=270 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
        "</td><td align=CENTER><button value=\"Chance\" action=\""+ByPassForChance+"\" width=60 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\"></td><td align=CENTER>"+
        "<button value=\"\" action=\"\" width=40 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\"></td></tr></table><br>";

		int maximoLista = 16;int contador = 0;
		int Desde = pagina * maximoLista; int Hasta = (pagina * maximoLista) + maximoLista;
		boolean haveMore = false;
		
		String byPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch + ";showmonsterinfo;%IDMONSTER%;0;0;0;0";
		
		String GrillaInfo = "<table fixwidth=767 cellspacing=-4 cellpadding=-1 height=25 bgcolor=%BGCOLOR%><tr><td fixwidth=290 align=CENTER>"+
        "<font color=\"CCFFCC\">%NOMBRE%</font></td><td fixwidth=42 align=CENTER>%LEVEL%</td><td fixwidth=65 align=CENTER>%SPOIL%</td><td fixwidth=270 align=CENTER>"+
        "<font color=\"A6CAF0\">%CANT_MIN%</font> | <font color=\"5DA2EC\">%CANT_MAX%</font></td><td fixwidth=60 align=CENTER><font color=\"CCFFFF\">%CHANCE%</font></td><td fixwidth=40 align=CENTER><br><button action=\""+ byPass +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">"+
        "</td></tr></table>";
		
		String []Color = {"13270C","0E180B"};
		
		boolean saveInfo = true;
		
		String Controls = "";
		
		Vector<_dropsearch> SortedData = PERSONAL_SORT.get(player.getObjectId());
		
		if(ItemisInDropList(idItem)){
			
			for(_dropsearch Data : SortedData){
				if(Data.getDropID() != idItem){
					continue;
				}
				if(saveInfo){
					recordInBD(player,String.valueOf(idItem),Engine.sRank.SEARCH_DROP.name());
					saveInfo=false;
				}
				
				if(contador>=Desde && contador < Hasta){
					NPCLIST += GrillaInfo.replace("%NAME_LINK%", Data.getName().replace(" ", "_")) .replace("%IDMONSTER%", String.valueOf( Data.getNpcID() )).replace("%BGCOLOR%",Color[contador % 2]).replace("%NOMBRE%", Data.getName() ).replace("%LEVEL%",  String.valueOf(Data.getNpcLevel()) ).replace("%SPOIL%", Data.getDropCategory() == -1 ? "SPOIL" : "DROP").replace("%CANT_MIN%", opera.getFormatNumbers(Data.getDropMin() )).replace("%CANT_MAX%", opera.getFormatNumbers( Data.getDropMax() )).replace("%CHANCE%", String.valueOf(Data._getDropChancePorcent()) + "%");
				}else if(contador>Hasta){
					haveMore = true;
					contador++;					
					break;
				}
				contador++;
			}
			
			if(haveMore || pagina>0 ){
				
				String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch + ";showMonster;" + String.valueOf(idItem) + ";" +String.valueOf(pagina-1)+";"+ ( INFO_SEARCH.get(player.getObjectId()).isFromItemTop ? "true" : "0" ) + ";0;0"; 
				String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch + ";showMonster;" + String.valueOf(idItem) + ";" +String.valueOf(pagina+1)+";"+ ( INFO_SEARCH.get(player.getObjectId()).isFromItemTop ? "true" : "0" ) + ";0;0";
				
				String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
				String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";				
				
				String Grilla = "<table fixwidth=767 cellspacing=-4 cellpadding=-1 height=38 bgcolor=0E180B><tr><td fixwidth=767 align=CENTER><br><table with=96>"+
	            "<tr><td width=32>"+ ( pagina>0 ? btnAntes : "") +"</td>"+
	                "<td width=32 align=CENTER><font name=\"hs12\">"+ String.valueOf(pagina+1) +"</font></td>"+
	                "<td width=32>"+ (haveMore ? btnProx : "") +"</td></tr></table></td></tr></table>";
				
				Controls = Grilla;
			}
		}
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%ITEM_TO_SEARCH%", ByPassBack);
		html.replace("%NPC_DATA%", NPCLIST);
		html.replace("%CONTROL%", Controls);
		return html.getHtml();
	}
	
	
	private static String getBoxItem(L2PcInstance player, String Buscar, Integer Pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch-item-list.htm");
		
		
		String ItemGrid = "<table fixwidth=620 cellspacing=-4 cellpadding=-1>"+
              "<tr>"+
                  "<td fixwidth=60 align=CENTER>"+
                      "<button value=\"Icon\" action=\"\" width=70 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
                  "</td>"+
                  "<td fiwidth=580 align=CENTER>"+
                      "<button value=\"Item Name\" action=\"\" width=580 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
                  "</td>"+
                  "<td width=100 fixwidth=100 align=CENTER>"+
                      "<button value=\"\" action=\"\" width=100 height=24 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
                  "</td>"+
              "</tr>"+
       "</table><br>";


		int Limite = 16;
		
		int desde = Pagina * Limite; int Hasta = Limite+(Pagina * Limite);
		int contador=0;
		boolean continuidad = false;
		
		String Controls = "";
		
		Vector<_itemInfo> ItemSeleccionados = opera.getAllItemByName(Buscar,true);

		String[] Colores = {"0A0A2A","050517"};
		
		String Bypass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.DropSearch.name()+";showMonster;%IDITEM%;0;0;0;0";
		
		if((ItemSeleccionados.size()>0)){
			for(_itemInfo _ItemSelect : ItemSeleccionados){
				int idItem = _ItemSelect.ITEM_ID;
					if(ItemisInDropList(idItem)){
						if(!continuidad){
							if(contador>=desde && contador < Hasta){
								ItemGrid += "<table fixwidth=620 cellspacing=-1 cellpadding=-1 bgcolor=\""+ Colores[contador % 2] +"\" height=35><tr>"+
			                  "<td width=70 fixwidth=70 align=CENTER>"+
			                   opera.getIconImgFromItem(idItem) +
			                  "</td>"+
			                  "<td width=570 fiwidth=570 align=CENTER>"+
			                  _ItemSelect.ITEM_NAME +
			                  "</td>"+
			                  "<td width=100 fixwidth=100 align=RIGHT>"+
			                      "<button  value \"Show Monsters\" action=\""+ Bypass.replace("%IDITEM%", String.valueOf(idItem)) +"\" width=100 height=35 back=\"L2UI_CT1.Button_DF_Calculator\" fore=\"L2UI_CT1.Button_DF_Calculator\">"+
			                  "</td>"+
			                  "</tr></table>";
							}else if(contador>Hasta){
								continuidad=true;
							}
							contador++;
						}
					}
			}
			
			String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch + ";buscarDrop;" + Buscar + ";" +String.valueOf(Pagina-1)+";0;0;0"; 
			String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.DropSearch + ";buscarDrop;" + Buscar + ";" +String.valueOf(Pagina+1)+";0;0;0";
			
			String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
			String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
			
			if(continuidad || Pagina>0){
				Controls += "<table fixwidth=620 cellspacing=-1 cellpadding=-1 bgcolor=\""+Colores[contador % 2]+"\" height=35><tr><td width=70 fixwidth=70 align=CENTER></td><td width=570 fiwidth=570 align=CENTER>"+
                "<table><tr><td>"+
				(Pagina > 0 ? btnAntes : "") +
                "</td><td width=50 align=CENTER><font name=\"hs12\"> "+ String.valueOf(Pagina + 1) +" </font></td><td>"+
                (continuidad ? btnProx : "")+
                "</td></tr></table></td><td width=100 fixwidth=100 height=32></td></tr></table>";
			}
		}else{
			ItemGrid += "<table fixwidth=620 cellspacing=-1 cellpadding=-1 bgcolor=\""+ Colores[contador % 2] +"\" height=35><tr><td>No Item</td></tr></table>";
		}
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%ITEM_DATA%", ItemGrid);
		html.replace("%CONTROLS%", Controls);
		
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	private static String getBoxSearch(String tituloCentral, String tituloBoton, String CajaID){
		return "<table width=360 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=0 height=100><tr><td width=360 align=CENTER>"+
        "<font name=\"hs12\" color=\"CCFFCC\">"+ tituloCentral +"</font></td></tr><tr><td align=CENTER>"+
        "<edit var=\""+ CajaID +"\" width=150></td></tr><tr><td align=CENTER><button value=\""+ tituloBoton +"\" action=\"%BYPASS%\" width=100 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">"+
        "</td></tr></table>";
	}
	
	@SuppressWarnings("unused")
	private static NpcHtmlMessage getBoxMostSearch_ItemAndMobs(NpcHtmlMessage html){
		String EmptyData = opera.getGridFormatFromHtml(html, 1, "");
		String EmptyIconLogo = EmptyData.split(",")[0].split(":")[1];
		String EmptyTitle = EmptyData.split(",")[1].split(":")[1];
		
		int contador=0;
		String NombreItem = "";
		String ImagenItem = "";
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";showMonster_top;%IDITEM%;0;true;0;0";
		
		String NoItem = ItemData.getInstance().getTemplate(3883).getIcon();
		
		String GridItem = "";
		
		for(Integer IdItem : general.getTopSearchItem()){
			NombreItem = central.getNombreITEMbyID(IdItem);
			ImagenItem = opera.getIconImgFromItem(IdItem,true);
			String ByPassItem = ByPass.replace("%IDITEM%", String.valueOf(IdItem));
			html.replace("%ITEM_NAME_"+ String.valueOf(contador+1) +"%", NombreItem);
			html.replace("%ITEM_BYPASS_"+ String.valueOf(contador+1) +"%", ByPassItem);
			html.replace("%ITEM_ICO_IMG_"+ String.valueOf(contador+1) +"%", ImagenItem);
			contador++;
		}
		
		if(contador<5){
			for(int i=contador;i<5;i++){
				html.replace("%ITEM_NAME_"+ String.valueOf(i+1) +"%", EmptyTitle);
				html.replace("%ITEM_BYPASS_"+ String.valueOf(i+1) +"%", "");
				html.replace("%ITEM_ICO_IMG_"+ String.valueOf(i+1) +"%", EmptyIconLogo);
			}
		}
		
		
		contador=0;
		String NombreMonster = "";
		ImagenItem = "";
		ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.DropSearch.name() + ";showmonsterinfo_top;%IDNPC%;0;0;0;0";
		
		for(Integer IdMonster : general.getTopSearchMonster()){
			L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(IdMonster);
			NombreMonster = tmpl.getName();
			ImagenItem = IconosTop[contador];
			String ByPassMobs = ByPass.replace("%IDNPC%", String.valueOf(IdMonster));
			html.replace("%MOB_NAME_"+ String.valueOf(contador+1) +"%", NombreMonster);
			html.replace("%MOB_BYPASS_"+ String.valueOf(contador+1) +"%", ByPassMobs);
			html.replace("%MOB_ICO_IMG_"+ String.valueOf(contador+1) +"%", ImagenItem); 
			contador++;
		}
		
		if(contador<5){
			for(int i=contador;i<5;i++){
				html.replace("%MOB_NAME_"+ String.valueOf(i+1) +"%", EmptyTitle);
				html.replace("%MOB_BYPASS_"+ String.valueOf(i+1) +"%", "");
				html.replace("%MOB_ICO_IMG_"+ String.valueOf(i+1) +"%", EmptyIconLogo);				
			}
		}		
		
		
		return html;
	}
	
	private static String getMainForm(L2PcInstance player){
		String ByPass_Item = "bypass -h ZeuS dropsearch_search Item $txtItem";
		String ByPass_Monster = "bypass -h ZeuS dropsearch_search Npc $txtMonster";
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-dropsearch.htm");
		html = getBoxMostSearch_ItemAndMobs(html);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%BYPASS_ITEM%", ByPass_Item);
		html.replace("%BYPASS_NPC%", ByPass_Monster);
		return html.getHtml();
	}
	
	public static void showByAction(L2PcInstance player, String Bypass, String Buscar){
		String ByPassToSend = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.DropSearch.name() + ";" + Bypass + ";" + Buscar + ";0;0;0;0";
		String retornoStr = v_dropsearch.bypass(player, ByPassToSend);
		cbManager.separateAndSend(retornoStr,player);
	}
	
	private static void recordInBD(L2PcInstance player, String Busqueda, String tipo){
		if(LastWordToSave==null){
			LastWordToSave.put(player.getObjectId(), Busqueda);
		}
		
		if(LastWordToSave.containsKey(player.getObjectId())){
			if(LastWordToSave.get(player.getObjectId()).equals(Busqueda)){
				return;
			}
		}
		
		Engine.createRank(Integer.parseInt(Busqueda),tipo);
	}	
	
	@SuppressWarnings("unused")
	public static String mainHtml(L2PcInstance player, String Params, String PalabraBuscar,int pagina){
		String par = Engine.enumBypass.DropSearch.name();
		String retorno = "";
		CheckIt(player);
		if(Params.equals("0")){
			retorno = getMainForm(player);
		}else if(Params.equals("buscarDrop")){
			INFO_SEARCH.get(player.getObjectId()).PAG_ITEM = String.valueOf(pagina);
			retorno = getBoxItem(player, PalabraBuscar,pagina);
		}else if(Params.startsWith("showMonster")){
			if(Params.endsWith("_top")){
				INFO_SEARCH.get(player.getObjectId()).isFromItemTop = true;
				INFO_SEARCH.get(player.getObjectId()).isFromMobTop = false;
			}
			INFO_SEARCH.get(player.getObjectId()).ID_ITEM = PalabraBuscar;
			INFO_SEARCH.get(player.getObjectId()).PAG_MOB = String.valueOf(pagina);
			retorno = getTablaMonsterByItemDrop(player,Integer.parseInt(PalabraBuscar),pagina);
		}else if(Params.equals("showmonsterinfo")){
			retorno = getInfoNPC(player,Integer.valueOf(PalabraBuscar));
			INFO_SEARCH.get(player.getObjectId()).ID_MOB = PalabraBuscar;
		}else if(Params.equals("SearchMonster")){
			retorno = getTablaMonsterByName(player,PalabraBuscar,pagina);
		}else if(Params.equals("showDrop")){
			retorno = getMonsterDropList(Integer.valueOf(PalabraBuscar), pagina, player);
		}else if(Params.equals("showmonsterinfo_top")){
			INFO_SEARCH.get(player.getObjectId()).isFromItemTop = false;
			INFO_SEARCH.get(player.getObjectId()).isFromMobTop = true;	
			retorno = getInfoNPC(player,Integer.valueOf(PalabraBuscar));
		}
		return retorno;
	}

	private static void CheckIt(L2PcInstance player){
		if(INFO_SEARCH == null){
			_dropCache temp = new _dropCache();
			INFO_SEARCH.put(player.getObjectId(), temp);
		}else if(INFO_SEARCH.size()==0){
			_dropCache temp = new _dropCache();
			INFO_SEARCH.put(player.getObjectId(), temp);
		}else if(!INFO_SEARCH.containsKey(player.getObjectId())){
			_dropCache temp = new _dropCache();
			INFO_SEARCH.put(player.getObjectId(), temp);			
		}		
	}
	
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_DROP_SEARCH) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}		
		CheckIt(player);
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			if(!isSorted){
				isSorted=true;
			}
			isDropEngine.put(player.getObjectId(), true);
			return mainHtml(player,parm1,"",0);
		}else if(parm1.equals("buscarDrop")){
			return mainHtml(player,parm1,parm2,Integer.valueOf(parm3));
		}else if(parm1.startsWith("showMonster")){
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("showmonsterinfo")){
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("SearchMonster")){
			INFO_SEARCH.get(player.getObjectId()).MobNameSearhing = parm2;
			INFO_SEARCH.get(player.getObjectId()).PAG_MOB = parm3;
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("showDrop")){
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("showmonsterinfo_top")){
			INFO_SEARCH.get(player.getObjectId()).isFromItemSearch = false;
			return mainHtml(player, parm1, parm2, Integer.valueOf(parm3));
		}else if(parm1.equals("ObserveAsk")){
			if(!general.DROP_SEARCH_OBSERVE_MODE){
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
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DS_YOU_WANT_TO_OBSERVER_THIS, IdDialog.ENGINE_DROPSEARCH_OBSERVE);
		}else if(parm1.equals("Observe")){
			Location loc = new Location(Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[0]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[1]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[2]));
			ObserveMode.EnterObserveMode(player, loc);
			return "";
		}else if(parm1.equals("teleportAsk")){
			String idMonster = parm2;			
			if(!general.DROP_SEARCH_CAN_USE_TELEPORT){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return "";
			}
			
			if(general.DROP_SEARCH_MOB_BLOCK_TELEPORT.contains(Integer.valueOf(idMonster))){
				central.msgbox(language.getInstance().getMsg(player).DS_MOB_OR_RB_BLOCKED_TELEPORT, player);
				return "";
			}
			Integer x =  Integer.valueOf(parm3);
			Integer y = Integer.valueOf(parm4);
			Integer z = Integer.valueOf(parm5);
			
			TELEPORT_TO.put(player.getObjectId(), x + "," + y + "," + z);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DS_YOU_WANT_TO_TELEPORT_THIS, IdDialog.ENGINE_DROPSEARCH_TELEPORT);
		}else if(parm1.equals("teleport")){
			if(!general.DROP_SEARCH_TELEPORT_FOR_FREE){
				if(!opera.haveItem(player, general.DROP_TELEPORT_COST)){
					return "";
				}
				opera.removeItem(general.DROP_TELEPORT_COST, player);
			}
			player.teleToLocation(Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[0]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[1]), Integer.valueOf(TELEPORT_TO.get(player.getObjectId()).split(",")[2]), true);
			player.setInstanceId(0);
			cbFormato.cerrarCB(player);
		}
		return "";
	}
}

class _dropCache{
	public String PAG_ITEM;
	public String PAG_MOB;
	public String ID_ITEM;
	public String ID_MOB;
	public String ItemNameSearching;
	public String MobNameSearhing;
	public boolean isFromItemSearch;
	public boolean isFromItemTop;
	public boolean isFromMobTop;
	public _dropCache(){
		this.PAG_ITEM = "0";
		this.PAG_MOB = "0";
		this.ID_ITEM = "0";
		this.ID_MOB = "0";
		this.ItemNameSearching = "0";
		this.MobNameSearhing = "0";
		this.isFromItemSearch = false;
		this.isFromItemTop = false;
		this.isFromMobTop = false;
	}
}
