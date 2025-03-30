package ZeuS.tutorial;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.sendC;
import ZeuS.Comunidad.EngineForm.cbFormato;
import ZeuS.Config.Advclassmaster;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.interfase.classmaster;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.gameserver.data.xml.impl.ClassListData;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.ClassId;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class profession {
	//		<button value="Yeah, i need better Weapon" action="link zeusPF;Weapon;0;0;0;0" width=200 height=32 back="L2UI_CT1.OlympiadWnd_DF_Apply" fore="L2UI_CT1.OlympiadWnd_DF_Apply">
	@SuppressWarnings("unused")
	private static String CLOSE_WINDOWS_ACTION = "link COXX";
	public static final Logger _log = Logger.getLogger(profession.class.getName());
	private static Map<Integer, Integer> INDEX_CLASS_BASE = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> WEAPON_GIVEN = new HashMap<Integer, Integer>();
	private static Map<Integer, Integer> ARMOR_GIVEN = new HashMap<Integer, Integer>();
	private static Vector<Integer> JUST_1_ARMOR = new Vector<Integer>();
	private static Vector<Integer> JUST_1_WEAPON = new Vector<Integer>();
	
	@SuppressWarnings("unused")
	public static void bypass(L2PcInstance player, String params){
		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning(params);
		}
		String parm[] = params.split(";");
		String Parametro = parm[1];
		String Par1 = parm[2];
		String Par2 = parm[3];
		String Par3 = parm[4];
		String Par4 = parm[5];
		if(Parametro.equals("CLASSMASTER")){
			int IDCLASS = Integer.valueOf(Par1);
			if(!classmaster.AddProfesion(player, IDCLASS, Integer.valueOf(Par2) + 1)){
				player.sendPacket(new sendC());
				return;
			}
			general.getAdvClassMasterData(Integer.valueOf(Par2) + 1).giveReward(player);
			if(general.getAdvClassMasterData(Integer.valueOf(Par2) + 1).showArmorWindows()){
				try{
					WEAPON_GIVEN.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				
				try{
					ARMOR_GIVEN.remove(player.getObjectId());
				}catch(Exception a){
					
				}
				
				WEAPON_GIVEN.put(player.getObjectId(), 1);
				ARMOR_GIVEN.put(player.getObjectId(), 1);
				getWindowsWeaponQuestion(player);
			}else {
				cbFormato.cerrarTutorial(player);
			}
		}else if(Parametro.endsWith("Weapon")){
			getWindowsWeaponSelection(player);
		}else if(Parametro.endsWith("Weapon_sel")){
			cbFormato.cerrarTutorial(player);
			int WeaponToGive = general.getAdvClassMasterData(INDEX_CLASS_BASE.get(player.getObjectId() )).getWeaponToGive();
			int GivenWeapon = WEAPON_GIVEN.get(player.getObjectId());
			if(GivenWeapon >= WeaponToGive){
				JUST_1_WEAPON.add(player.getObjectId());
				WEAPON_GIVEN.put(player.getObjectId(), WEAPON_GIVEN.get(player.getObjectId())+1);				
				general.getAdvClassMasterData(INDEX_CLASS_BASE.get(player.getObjectId())).giveWeapon(player, Par1);				
				getWindowsArmorSelection(player);
			}else{
				WEAPON_GIVEN.put(player.getObjectId(), WEAPON_GIVEN.get(player.getObjectId())+1);
				general.getAdvClassMasterData(INDEX_CLASS_BASE.get(player.getObjectId())).giveWeapon(player, Par1);
				getWindowsWeaponSelection(player);
			}
		}else if(Parametro.endsWith("Armor_sel")){
			cbFormato.cerrarTutorial(player);
			int ArmorGiven = ARMOR_GIVEN.get(player.getObjectId());
			if(ArmorGiven==1){
				ARMOR_GIVEN.put(player.getObjectId(), ARMOR_GIVEN.get(player.getObjectId())+1);				
				general.getAdvClassMasterData(INDEX_CLASS_BASE.get( player.getObjectId() )).giveArmor(player, Par1);
			}
			player.sendPacket(new sendC());
			if(isAdvancedClassMaster(player)){
				showProfesionWindows(player);
			}else{			
				ZeuS.showLevelUpSpotWindows(player);
			}
		}
	}
	
	private static void getWindowsArmorSelection(L2PcInstance player){
		int idIndex = INDEX_CLASS_BASE.get(player.getObjectId());
		Advclassmaster T1 = general.ADVANCED_CLASS_MASTER.get(idIndex);

		if(T1.canJust_1_Item_at_time()){
			if(JUST_1_ARMOR != null){
				if(JUST_1_ARMOR.size()>0){
					if(JUST_1_ARMOR.contains(player.getObjectId())){
						//return;
					}
				}
			}
		}
		
		if(!T1.isActive() || !T1.showArmorWindows()){
			return;
		}
		String Armors = general.getAdvClassMasterData(INDEX_CLASS_BASE.get(player.getObjectId())).getArmorBtn();
		
		if(Armors==null){
			return;
		}else if(Armors.length()==0){
			return;
		}
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"classMaster-Armors.html");
		String imageLogo = opera.getImageLogo(player);
		html.replace("%SERVER_LOGO%", imageLogo);
		html.replace("%BTN_ARMORS%", Armors);
		central.sendHtml(player, html.getHtml(), true);
		JUST_1_ARMOR.add(player.getObjectId());
	}
	
	private static void getWindowsWeaponSelection(L2PcInstance player){
		int idIndex = INDEX_CLASS_BASE.get(player.getObjectId());
		Advclassmaster T1 = general.ADVANCED_CLASS_MASTER.get(idIndex);
		
		if(!T1.isActive() || !T1.showWeaponWindows()){
			return;
		}
		
		if(T1.canJust_1_Item_at_time()){
			if(JUST_1_WEAPON!=null){
				if(JUST_1_WEAPON.size()>0){
					if(JUST_1_WEAPON.contains(player.getObjectId())){
						//return;
					}
				}
			}
		}
		
		String Weapons = general.getAdvClassMasterData(INDEX_CLASS_BASE.get(player.getObjectId())).getWeaponBtn();
		int WeaponsToChoose = general.getAdvClassMasterData( INDEX_CLASS_BASE.get(player.getObjectId() ) ).getWeaponToGive();
		int TotalWeaponGiven = 0;
		try{
			TotalWeaponGiven = WEAPON_GIVEN.get(player.getObjectId());
		}catch(Exception a){
			
		}
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"classMaster-Weapons.html");
		String imageLogo = opera.getImageLogo(player);
		String HTML_R = html.getHtml();
		HTML_R = HTML_R.replace("%QUANTITY_WEAPONS%", String.valueOf( (WeaponsToChoose - TotalWeaponGiven)+1 ) ) .replace("%SERVER_LOGO%", imageLogo).replace("%BTN_WEAPONS%", Weapons);
		central.sendHtml(player, HTML_R, true);
		if(TotalWeaponGiven >= WeaponsToChoose){
			JUST_1_WEAPON.add(player.getObjectId());
		}
	}
	
	
	
	
	private static void getWindowsWeaponQuestion(L2PcInstance player){
		
		player.sendPacket(new sendC());
		
		Advclassmaster T1 = general.getAdvClassMasterData(INDEX_CLASS_BASE.get(player.getObjectId()));
		
		if(!T1.isActive() || !T1.showWeaponWindows()){
			return;
		}
		
		if(T1.getWeaponToGive()==0){
			getWindowsArmorSelection(player);
			return;
		}
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"classMaster-WeaponQuestion.html");
		String imageLogo = opera.getImageLogo(player);
		String HTML_R = html.getHtml();
		HTML_R = HTML_R.replace("%SERVER_LOGO%", imageLogo).replace("%CLASSNAME%", player.getTemplate().getClassId().name());
		central.sendHtml(player, HTML_R, true);			
	}
	
	private static final int getMinLevel(int level)
	{
		switch (level)
		{
			case 0:
				return 20;
			case 1:
				return 40;
			case 2:
				return 76;
			default:
				return Integer.MAX_VALUE;
		}
	}
	
	public static boolean isAdvancedClassMaster(L2PcInstance player){
		final ClassId currentClassId = player.getClassId();
		if ((getMinLevel(currentClassId.level()) > player.getLevel()) && !Config.ALLOW_ENTIRE_TREE)
		{
			return false;
		}
		int idIndex = currentClassId.level();
		Advclassmaster T1 = general.getAdvClassMasterData(idIndex+1);
		if(T1==null){
			return false;
		}
		if(!T1.isActive()){
			return false;
		}
		return true;
	}
	
	private static final boolean validateClassId(ClassId oldCID, ClassId newCID)
	{
		if ((newCID == null) || (newCID.getRace() == null))
		{
			return false;
		}
		
		if (oldCID.equals(newCID.getParent()))
		{
			return true;
		}
		
		if (Config.ALLOW_ENTIRE_TREE && newCID.childOf(oldCID))
		{
			return true;
		}
		
		return false;
	}
	
	private static String getRequiredItems(int level)
	{
		if ((Config.CLASS_MASTER_SETTINGS.getRequireItems(level) == null) || Config.CLASS_MASTER_SETTINGS.getRequireItems(level).isEmpty())
		{
			return "";
		}
		String Retorno = "";
		for (int _itemId : Config.CLASS_MASTER_SETTINGS.getRequireItems(level).keySet())
		{
			int _count = Config.CLASS_MASTER_SETTINGS.getRequireItems(level).get(_itemId);
			if(Retorno.length()>0){
				Retorno += ", ";
			}
			Retorno += String.valueOf(_count) + " " + ItemData.getInstance().getTemplate(_itemId).getName();
		}
		return Retorno;
	}

	@SuppressWarnings("unused")
	private static String getClasesBtn(L2PcInstance player, ClassId currentClassId){
		String retorno = "";
		if ((getMinLevel(currentClassId.level()) > player.getLevel()))
		{
			return "";
		}
		
		final StringBuilder menu = new StringBuilder(100);
		for (ClassId cid : ClassId.values())
		{
			if ((cid == ClassId.inspector) && (player.getTotalSubClasses() < 2))
			{
				continue;
			}
			if (validateClassId(currentClassId, cid))
			{
				String ByPass = "link zeusPF;CLASSMASTER;" + String.valueOf(cid.getId()) + ";"+ String.valueOf(currentClassId.level()) +";0;0;0";
				//ClassListData.getInstance().getClass(cid).getEscapedClientCode()
				String BtnSelect = "<button value=\""+ ClassListData.getInstance().getClass(cid).getClassName() +"\" action=\""+ ByPass +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\" fore=\"L2UI_CT1.OlympiadWnd_DF_HeroConfirm\"><br>";
				retorno += BtnSelect;
			}
		}
		return retorno;
	}
	
	public static void showProfesionWindows(L2PcInstance player){
		
		try{
			INDEX_CLASS_BASE.remove(player.getObjectId());
		}catch(Exception a){
			
		}
		final ClassId currentClassId = player.getClassId();
		INDEX_CLASS_BASE.put(player.getObjectId(), currentClassId.level() + 1);
		String Clases = getClasesBtn(player, currentClassId);
		String Cost = getRequiredItems(currentClassId.level() + 1);
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"classMaster.html");
		String imageLogo = opera.getImageLogo(player);
		String HTML_R = html.getHtml();
		HTML_R = HTML_R.replace("%SERVER_LOGO%", imageLogo).replace("%CLASSES%", Clases).replace("%COST%", (Cost.length()>0 ? "It will cost<br1>" + Cost : ""));
		central.sendHtml(player, HTML_R, true);	
	}
}
