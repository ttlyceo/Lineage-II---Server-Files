package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._cumulativeData;
import ZeuS.Config._cumulativeSkill;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.cumulativeSubclass;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.procedimientos.ZeuSConditions;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.ClassId;
import l2r.gameserver.model.base.PlayerClass;
import l2r.gameserver.model.base.SubClass;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SkillCoolTime;
import l2r.gameserver.network.serverpackets.SocialAction;

public class v_subclass{
	
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(v_subclass.class.getName());
	private static Map<L2PcInstance, Boolean> Working_with = new HashMap<L2PcInstance, Boolean>();
	private static Map<L2PcInstance, _tempData_subclass> SUBCLASS_TEMP_DATA = new HashMap<L2PcInstance, _tempData_subclass>();
	private static final Iterator<SubClass> iterSubClasses(L2PcInstance player)
	{
		return player.getSubClasses().values().iterator();
	}
	
	private static String getBtnChooseSubClassToRemove(L2PcInstance player){
		String MAIN_HTML = "<tr><td width=500>";
		
		String byPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SubClass.name() + ";deleteSubClass;%IDSUBCLASS%;%IDCLASS%;0;0;0";
		String btnChange = "<button action=\""+ byPass +"\" width=64 height=64 back=\"L2UI_CT1.ICON_DF_CHARACTERTURN_ZOOMOUT\" fore=\"L2UI_CT1.ICON_DF_CHARACTERTURN_ZOOMOUT\" value=\" \">";
		
		String GrillaCambio = "<table width=500 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td width=32>"+
        "<img src=\"%IMAGEN%\" width=32 height=32></td><td width=404><font name=\"hs12\" color=\"CCFFCC\">%NOMBRE% (%TIPO%)</font><br1><font color=\"A6CAF0\">%RAZA%</font></td>"+
        "<td width=64>%BOTON%</td></tr></table><br>";
		
		String Nombre = "";
		String Raza = "";
		String Imagen = "";
		
		String MAIN_HTML_2 = "";
		int i=1;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			SubClass subClass = subList.next();
			
			Nombre = opera.getClassName(subClass.getClassId());
			Raza = general.getClassInfo(subClass.getClassId()).getRaceName();
			Imagen = general.getClassInfo(subClass.getClassId()).getLogo();
			
			if(subClass.getClassId() != player.getClassId().getId()){
				MAIN_HTML_2 += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass N " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", btnChange.replace("%IDCLASS%", String.valueOf(subClass.getClassId())).replace("%IDSUBCLASS%",String.valueOf(subClass.getClassIndex())));
			}else{
				MAIN_HTML_2 += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass N " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", "");
			}
			i++;
		}
		
		if(MAIN_HTML_2.length()==0){
			MAIN_HTML_2 = "<center>No Have Sub Class</center>";
		}
		
		MAIN_HTML += MAIN_HTML_2 + "</td></tr>";

		return MAIN_HTML;
	}
	
	@SuppressWarnings("unused")
	private static String getBtnChooseSubClass(L2PcInstance player){
		String MAIN_HTML = "<tr><td width=500>";
		
		int actualClass = player.getClassId().getId();
		
		boolean isBaseClass = (player.getClassId().getId() == player.getBaseClass());
		
		String byPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SubClass.name() + ";changeSubC;%IDCLASS%;0;0;0;0";
		String btnChange = "<button action=\""+ byPass +"\" width=64 height=64 back=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\" fore=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\">";
		
		String GrillaCambio = "<table width=500 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td width=32>"+
        "<img src=\"%IMAGEN%\" width=32 height=32></td><td width=404><font name=\"hs12\" color=\"CCFFCC\">%NOMBRE% (%TIPO%)</font><br1><font color=\"A6CAF0\">%RAZA%</font></td>"+
        "<td width=64>%BOTON%</td></tr></table><br>";
		
		String Nombre = opera.getClassName(player.getBaseClass());
		String Raza = general.getClassInfo(player.getBaseClass()).getRaceName();
		String Imagen = general.getClassInfo(player.getBaseClass()).getLogo();
		
		
		MAIN_HTML += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Main").replace("%RAZA%", Raza).replace("%BOTON%", ( isBaseClass ? "" : btnChange.replace("%IDCLASS%", String.valueOf(0)) ) );
			
		int i=1;
		for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
		{
			SubClass subClass = subList.next();
			
			Nombre = opera.getClassName(subClass.getClassId());
			Raza = general.getClassInfo(subClass.getClassId()).getRaceName();
			Imagen = general.getClassInfo(subClass.getClassId()).getLogo();
			
			if(subClass.getClassId() != player.getClassId().getId()){
				MAIN_HTML += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass N " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", btnChange.replace("%IDCLASS%",String.valueOf(subClass.getClassIndex())));
			}else{
				MAIN_HTML += GrillaCambio.replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%TIPO%", "Subclass N " + String.valueOf(i)).replace("%RAZA%", Raza).replace("%BOTON%", "");
			}
			i++;
		}
		
		MAIN_HTML += "</td></tr>";

		return MAIN_HTML;
	}
	
	public final static Set<PlayerClass> getAvailableSubClasses(L2PcInstance player)
	{
		// get player base class
		final int currentBaseId = player.getBaseClass();
		final ClassId baseCID = ClassId.getClassId(currentBaseId);
		
		// we need 2nd occupation ID
		final int baseClassId;
		if (baseCID.level() > 2)
		{
			baseClassId = baseCID.getParent().ordinal();
		}
		else
		{
			baseClassId = currentBaseId;
		}
		
		/**
		 * If the race of your main class is Elf or Dark Elf, you may not select each class as a subclass to the other class. If the race of your main class is Kamael, you may not subclass any other race If the race of your main class is NOT Kamael, you may not subclass any Kamael class You may not
		 * select Overlord and Warsmith class as a subclass. You may not select a similar class as the subclass. The occupations classified as similar classes are as follows: Treasure Hunter, Plainswalker and Abyss Walker Hawkeye, Silver Ranger and Phantom Ranger Paladin, Dark Avenger, Temple Knight
		 * and Shillien Knight Warlocks, Elemental Summoner and Phantom Summoner Elder and Shillien Elder Swordsinger and Bladedancer Sorcerer, Spellsinger and Spellhowler Also, Kamael have a special, hidden 4 subclass, the inspector, which can only be taken if you have already completed the other
		 * two Kamael subclasses
		 */
		Set<PlayerClass> availSubs = PlayerClass.values()[baseClassId].getAvailableSubclasses(player);
		if ((availSubs != null) && !availSubs.isEmpty())
		{
			for (Iterator<PlayerClass> availSub = availSubs.iterator(); availSub.hasNext();)
			{
				PlayerClass pclass = availSub.next();
				
				// check for the village master
				if (!checkVillageMaster(pclass))
				{
					availSub.remove();
					continue;
				}
				
				// scan for already used subclasses
				int availClassId = pclass.ordinal();
				ClassId cid = ClassId.getClassId(availClassId);
				SubClass prevSubClass;
				ClassId subClassId;
				for (Iterator<SubClass> subList = iterSubClasses(player); subList.hasNext();)
				{
					prevSubClass = subList.next();
					subClassId = ClassId.getClassId(prevSubClass.getClassId());
					
					if (subClassId.equalsOrChildOf(cid))
					{
						availSub.remove();
						break;
					}
				}
			}
		}
		
		return availSubs;
	}
	
	public final boolean checkVillageMaster(int classId)
	{
		return checkVillageMaster(PlayerClass.values()[classId]);
	}
	
	/**
	 * Returns true if this PlayerClass is allowed for master
	 * @param pclass
	 * @return
	 */
	public final static boolean checkVillageMaster(PlayerClass pclass)
	{
		if (Config.ALT_GAME_SUBCLASS_EVERYWHERE)
		{
			return true;
		}
		return checkVillageMasterRace(pclass) && checkVillageMasterTeachType(pclass);
	}
	
	protected static boolean checkVillageMasterRace(PlayerClass pclass)
	{
		return true;
	}
	
	protected static boolean checkVillageMasterTeachType(PlayerClass pclass)
	{
		return true;
	}	
	
	
	private static String getBtn(L2PcInstance player, boolean isRemove, String idSubClass, String idClassFromSub, int pagina, NpcHtmlMessage html){
		
		Set<PlayerClass> subsAvailable =  ZeuSConditions.getAvailableSubClasses(player);  //getAvailableSubClasses(player);
		
		String tipoByPass = isRemove ? "apliRemoveC":"apliSubC";
		
		String ByPass =  "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SubClass.name() + ";"+tipoByPass+";%IDCLASS%;"+ String.valueOf(idSubClass) +";0;0;0";
		String btn = "<button action=\""+ ByPass + "\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_PlusBtn_Red\" fore=\"L2UI_CT1.MiniMap_DF_PlusBtn_Red\" value=\" \">";
		
		String retorno = "";
		
		String btnClases = "<td><table width=240 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=1 cellpadding=3 height=60><tr><td wdith=32><img src=\"%IMAGEN%\" width=32 height=32>"+
        "</td><td width=180><font name=\"hs12\" color=\"CCFFCC\">%NOMBRE%</font><br1><font color=\"A6CAF0\">%RAZA%</font></td><td width=32>"+
        btn + "</td></tr></table></td>";
		
		int contador = 1;
		
		if(subsAvailable==null){
			central.msgbox("You dont have any subclass to add.", player);
			return "";
		}
		int limite = 10;
		int Desde = pagina * limite;
		int Hasta = Desde + limite;
		
		int contLimite = 0;
		
		boolean haveNext = false;
		
		Vector<Integer> SubClassIdFromPlayer = new Vector<Integer>();
		if(player.getSubClasses()!=null) {
			for(SubClass ClassPlayer : player.getSubClasses().values()) {
				SubClassIdFromPlayer.add(ClassPlayer.getClassId());
			}
		}
		
		for(PlayerClass ClassA : subsAvailable){
			if(contLimite >= Desde && contLimite < Hasta) {
				if(ClassA.ordinal() == player.getBaseClass()) {
					continue;
				}
				
				boolean canShowData = true;
				if(player.getSubClasses()!=null) {
					if(player.getSubClasses().containsKey(ClassA.ordinal())) {
						continue;
					}
					if(_cumulativeData.isEnabled()) {
						for(Integer idClass : SubClassIdFromPlayer) {
							if(_cumulativeData.isBlockClass( ClassA.ordinal(), idClass) ) {
								canShowData = false;							
							}
						}
					}
				}
				
				if(_cumulativeData.isEnabled()) {
					if(!canShowData) {
						continue;
					}
					
					if(_cumulativeData.isBlockClass(ClassA.ordinal(), player.getBaseClass())) {
						continue;
					}
				}
				
				if(contador == 1){
					retorno += "<tr>";
				}
				int idClass = ClassA.ordinal();
				if(Integer.valueOf(idClassFromSub)!=idClass){
					String Nombre = opera.getClassName(idClass);
					String Raza = general.getClassInfo(idClass).getRaceName();
					String Imagen = general.getClassInfo(idClass).getLogo();
					retorno += btnClases.replace("%IDCLASS%", String.valueOf(idClass)).replace("%IMAGEN%", Imagen).replace("%NOMBRE%", Nombre).replace("%RAZA%", Raza);
					
					contador++;
					
					if(contador == 3){
						retorno += "</tr>";
						contador = 1;
					}
				}
			}else if(contLimite >= Hasta) {
				haveNext = true;
			}
			contLimite++;
		}
		
		if(contador==2){
			retorno += "<td width=240></td></tr>";
		}
		
		//%SUBCLASS_NEXT_PAGE_BYPASS%
		//%SUBCLASS_PREV_PAGE_BYPASS%
		
		String ByPassPrev = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SubClass.name() + ";add;0;"+ String.valueOf( pagina-1 ) +";0;0;0";
		String ByPassNext = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SubClass.name() + ";add;0;"+ String.valueOf( pagina+1 ) +";0;0;0";
		
		if(haveNext) {
			html.replace("%SUBCLASS_NEXT_PAGE_BYPASS%", ByPassNext);
		}else {
			html.replace("%SUBCLASS_NEXT_PAGE_BYPASS%", "");
		}
		
		if(pagina>0) {
			html.replace("%SUBCLASS_PREV_PAGE_BYPASS%", ByPassPrev);
		}else {
			html.replace("%SUBCLASS_PREV_PAGE_BYPASS%", "");
		}
		
		return retorno;
	}
	
	public static void deleteProcess(L2PcInstance player) {
		_tempData_subclass DataClassTemp = SUBCLASS_TEMP_DATA.get(player);
		String DatoEnviar = "";
		if(_cumulativeData.isEnabled()) {
			Working_with.put(player, true);
			sendWaitWindows(player);
			int IdClassToRemove = DataClassTemp.getClassID();
			int IdClassIndex = DataClassTemp.getClassIndex();
			cumulativeSubclass.getInstance().deleteSubClass(player, IdClassToRemove, IdClassIndex);
			player.sendSkillList();
			player.sendPacket(SystemMessageId.SUBCLASS_TRANSFER_COMPLETED);
			Working_with.put(player, false);
			DatoEnviar = mainHtml(player, "remove", 0);
		}else {
			sendWaitWindows(player);
			Working_with.put(player, false);
			//String ID_subclassINDEX, String idClass
			DatoEnviar = mainHtml(player,"add", true, String.valueOf( DataClassTemp.getClassIndex() ), String.valueOf( DataClassTemp.getClassID() ), DataClassTemp.getPagina());
		}
		cbManager.separateAndSend(DatoEnviar, player);
	}
	
	public static void addNewSubClassProcess(L2PcInstance player) {
		_tempData_subclass DataClassTemp = SUBCLASS_TEMP_DATA.get(player);
		String DatoEnviar = "";
		sendWaitWindows(player);
		if(!_cumulativeData.isEnabled()) {
			if(player.getLevel() < 75){
           		central.msgbox(language.getInstance().getMsg(player).SUBCLASS_YOU_LEVEL_IS_LOW_TO_MAKE_ANOTHER_SUBCLASS, player);
           		Working_with.put(player, false);
           		DatoEnviar = mainHtml(player,"0", DataClassTemp.getPagina());
           		cbManager.separateAndSend(DatoEnviar, player);
           		return;
			}
			
			if (!player.addSubClass(DataClassTemp.getClassID() ,player.getTotalSubClasses() + 1)){
				player.sendMessage(language.getInstance().getMsg(player).SUBCLASS_YOU_SUBCLASS_CANT_BE_ADDED);
				Working_with.put(player, false);
				DatoEnviar = mainHtml(player,"0", DataClassTemp.getPagina());
				cbManager.separateAndSend(DatoEnviar, player);
				return;
			}
			player.setActiveClass(player.getTotalSubClasses());
			player.sendPacket(SystemMessageId.CLASS_TRANSFER);
			cbFormato.cerrarCB(player); 
			Working_with.put(player, false);
		}else {
			if(!_cumulativeData.haveRequestedItems(player)) {
				central.msgbox(language.getInstance().getMsg(player).YOU_DONT_HAVE_THE_REQUESTED_ITEM , player);
				Working_with.put(player, false);
				DatoEnviar = mainHtml(player,"0", DataClassTemp.getPagina());
				cbManager.separateAndSend(DatoEnviar, player);
				return;
			}
			boolean CanAddNewSubclass = _cumulativeData.canAddSubClass(player, DataClassTemp.getClassID());
			sendWaitWindows(player);
			if (!player.addSubClass(DataClassTemp.getClassID(), player.getTotalSubClasses() + 1)){
				player.sendMessage(language.getInstance().getMsg(player).SUBCLASS_YOU_SUBCLASS_CANT_BE_ADDED);
				Working_with.put(player, false);
				DatoEnviar = mainHtml(player,"0", DataClassTemp.getPagina());
				cbManager.separateAndSend(DatoEnviar, player);
				return;
			}
			if(CanAddNewSubclass) {
				_cumulativeData.deleteRequestedItem(player);
				player.sendPacket(SystemMessageId.CLASS_TRANSFER);						
				List<_cumulativeSkill> skillFromSubClass = _cumulativeData.getAllSkillToGive(player, DataClassTemp.getClassID(), player.getTotalSubClasses());
				for(_cumulativeSkill skillGive : skillFromSubClass) {
					L2Skill skillG = SkillData.getInstance().getSkill(skillGive.getSkillID() , skillGive.getSkillLevel());
					if(skillG != null) {
						player.addSkill(skillG, true);
					}
				}
				player.broadcastPacket(new SocialAction(player.getObjectId(), SocialAction.LEVEL_UP));
				player.sendSkillList();
				Working_with.put(player, false);
				cbFormato.cerrarCB(player);
				return;
			}
		}
		Working_with.put(player, false);
		DatoEnviar = mainHtml(player, "add", DataClassTemp.getPagina());
		cbManager.separateAndSend(DatoEnviar, player);
	}
	
	private static String mainHtml(L2PcInstance player, String Seccion, int pagina){
		return mainHtml(player, Seccion, false, "0","0", pagina);
	}
	
	private static String mainHtml(L2PcInstance player, String Seccion, boolean isRemove,String ID_subclassINDEX, String idClass, int pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-subclass.htm");		
		String Data = "";
		if(Seccion.equals("add")){
			Data = getBtn(player,isRemove,ID_subclassINDEX, idClass, pagina, html);
		}else if(Seccion.equals("change")){
			Data = getBtnChooseSubClass(player);
		}else if(Seccion.equals("remove")){
			Data = getBtnChooseSubClassToRemove(player);
		}
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%DATA%", Data);
		html.replace("%SUBCLASS_PAGE_NUM%", String.valueOf(pagina+1));
		return html.getHtml();
	}
	
	private static void sendWaitWindows(L2PcInstance player) {
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-subclass-wait.htm");		
		cbManager.separateAndSend(html.getHtml(), player);		
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(Working_with==null) {
			Working_with.put(player, false);
		}else if(!Working_with.containsKey(player)){
			Working_with.put(player, false);
		}
		if(!general.STATUS_SUBCLASES) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}
		
		if(Working_with.get(player) ==true) {
			return "";
		}
		
		Working_with.put(player, false);
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		
		if(!parm1.equals("0")){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				central.msgbox(language.getInstance().getMsg(player).ARE_NOT_IN_PEACE_ZONE, player);
				Working_with.put(player, false);
				return mainHtml(player,"0", Integer.valueOf(parm3)); 
			}
		}
		
		if(parm1.equals("0")){
			Working_with.put(player, false);
			return mainHtml(player,"0",Integer.valueOf(parm3));
		}else if(parm1.equals("add")){
			if(SUBCLASS_TEMP_DATA!=null) {
				if(SUBCLASS_TEMP_DATA.containsKey(player)) {
					SUBCLASS_TEMP_DATA.get(player).setPagina(Integer.valueOf(parm3));
					if(SUBCLASS_TEMP_DATA.get(player).getRemoveSubclass()) {
						return mainHtml(player, "add", true, String.valueOf(SUBCLASS_TEMP_DATA.get(player).getClassIndex() ), String.valueOf( SUBCLASS_TEMP_DATA.get(player).getClassID() ), SUBCLASS_TEMP_DATA.get(player).getPagina());
					}
				}
			}
			
			Working_with.put(player, false);
			return mainHtml(player, parm1, Integer.valueOf(parm3));
		}else if(parm1.equals("apliSubC")){
			//SUBCLASS_YOU_WANT_TO_ADD_THIS_SUBCLASS_$name  = String.valueOf(propZ.getProperty("SUBCLASS_YOU_WANT_TO_ADD_THIS_SUBCLASS_$name"));
			if(opera.canUseCBFunction(player)){
				if(player.getTotalSubClasses() >= Config.MAX_SUBCLASS){
               		central.msgbox(language.getInstance().getMsg(player).SUBCLASS_YOU_CANT_ADD_MORE_SUBCLASS, player);
               		Working_with.put(player, false);
               		return mainHtml(player,"add",Integer.valueOf(parm3));
				}
				_tempData_subclass dataClass = new _tempData_subclass(Integer.valueOf(parm2), 0, Integer.valueOf(parm3), false);
				SUBCLASS_TEMP_DATA.put(player, dataClass);
				String MnsjeAddSubclass = language.getInstance().getMsg(player).SUBCLASS_YOU_WANT_TO_ADD_THIS_SUBCLASS_$name.replace("$name", opera.getClassName(Integer.valueOf(parm2)));
				Dlg.sendDlg(player, MnsjeAddSubclass, IdDialog.ENGINE_SUBCLASS_ADD, 45);
			}
		}else if(parm1.equals("change")){
			Working_with.put(player, false);
			return mainHtml(player, parm1, Integer.valueOf(parm3));
			
		}else if(parm1.equals("changeSubC")){
			if(_cumulativeData.isEnabled()) {
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				Working_with.put(player, false);
				return mainHtml(player, parm1, Integer.valueOf(parm3));
			}			
			if(Config.ZEUS_SUBCLASS_CHANGE_ONLY_IN_PEACE_ZONE) {
				if(!player.isInsideZone(ZoneIdType.PEACE)) {
					player.sendMessage("Cant change subclass in this place");
					Working_with.put(player, false);
					return mainHtml(player, parm1, Integer.valueOf(parm3));
				}
			}
			if(player.isCastingNow()){
				central.msgbox(language.getInstance().getMsg(player).SUBCLASS_YOU_CANT_CHANGE_SUBCLASS_WHILE_CASTING, player);
				Working_with.put(player, false);
				return mainHtml(player, parm1, Integer.valueOf(parm3));	
			}
			
			
			
			if(!player.setActiveClass(Integer.valueOf(parm2))){
				player.sendMessage(language.getInstance().getMsg(player).SUBCLASS_YOU_CLASS_CANT_BE_CHANGED);
				Working_with.put(player, false);
				return mainHtml(player,"0", Integer.valueOf(parm3));
			}
			player.sendPacket(SystemMessageId.SUBCLASS_TRANSFER_COMPLETED);
			Working_with.put(player, false);
			return mainHtml(player,"change", Integer.valueOf(parm3));
			
		}else if(parm1.equals("remove")){
			Working_with.put(player, false);
			return mainHtml(player, parm1, Integer.valueOf(parm3));
		}else if(parm1.equals("deleteSubClass")){
			Working_with.put(player, false);
			_tempData_subclass tmDataSubclass = new _tempData_subclass(Integer.valueOf(parm3), Integer.valueOf(parm2), Integer.valueOf(parm4), true);
			SUBCLASS_TEMP_DATA.put(player, tmDataSubclass);
			String Question = language.getInstance().getMsg(player).SUBCLASS_YOU_WANT_TO_REMOVE_THIS_SUBCLASS_$name.replace("$name", opera.getClassName(Integer.valueOf(parm3)));
			Dlg.sendDlg(player, Question, IdDialog.ENGINE_SUBCLASS_REMOVE, 40);
		}else if(parm1.equals("apliRemoveC")){
			if(opera.canUseCBFunction(player)){
				sendWaitWindows(player);
				if(player.modifySubClass(Integer.valueOf(parm3),Integer.valueOf(parm2))){
					player.stopAllEffects();
					player.setActiveClass(Integer.valueOf(parm3));
					player.sendPacket(SystemMessageId.ADD_NEW_SUBCLASS);
					Working_with.put(player, false);
					return mainHtml(player,"remove", Integer.valueOf(parm3));
				}else{
	                player.sendMessage("The sub class could not be added, you have been reverted to your base class.");
	                Working_with.put(player, false);
				}
				Working_with.put(player, false);
				return mainHtml(player,"remove", Integer.valueOf(parm3));
			}
		}
		Working_with.put(player, false);
		return "";
	}
}

class _tempData_subclass{
	private final int CLASS_ID;
	private final int CLASS_INDEX;
	private int CLASS_PAGINA;
	private final boolean CLASS_REMOVE;
	public _tempData_subclass(int classID, int classIndex, int classPag, boolean removeSubClass) {
		this.CLASS_ID = classID;
		this.CLASS_INDEX = classIndex;
		this.CLASS_PAGINA = classPag;
		this.CLASS_REMOVE = removeSubClass;
	}
	public int getClassID() {
		return this.CLASS_ID;
	}
	public int getClassIndex() {
		return this.CLASS_INDEX;
	}
	public int getPagina() {
		return this.CLASS_PAGINA;
	}
	public boolean getRemoveSubclass() {
		return this.CLASS_REMOVE;
	}
	public void setPagina(int Pagina) {
		this.CLASS_PAGINA = Pagina;
	
	}
}