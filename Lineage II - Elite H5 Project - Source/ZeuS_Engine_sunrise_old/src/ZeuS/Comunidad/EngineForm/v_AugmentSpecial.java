package ZeuS.Comunidad.EngineForm;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.enums.ElementalItems;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.L2Augmentation;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ExAttributeEnchantResult;
import l2r.gameserver.network.serverpackets.ExBrExtraUserInfo;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.MagicSkillUse;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.gameserver.network.serverpackets.UserInfo;
import l2r.gameserver.util.Util;

public class v_AugmentSpecial{
	private final static Logger _log = Logger.getLogger(v_AugmentSpecial.class.getName());
	private static Map<Integer, _augmentData> AUGMENT_DATA = new HashMap<Integer, _augmentData>();
	public static void getAllAugment(){
		
		try{
			AUGMENT_DATA.clear();
		}catch(Exception a){
			
		}
		
		String Consulta = "SELECT * FROM zeus_augment_data ORDER BY AugmentName";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			CallableStatement psqry = conn.prepareCall(Consulta);
			ResultSet rss = psqry.executeQuery();
			try{
				while (rss.next()){
					_augmentData temp = new _augmentData(rss.getInt(1), rss.getInt(5), rss.getString(2), rss.getString(3), rss.getString(4));
					AUGMENT_DATA.put(rss.getInt(1), temp);
				}
			}catch(SQLException a){
				conn.close();
			}
			conn.close();
		}catch(SQLException a){

		}
		_log.warning("	We load " + AUGMENT_DATA.size() + " special augments");
		try{
			conn.close();
		}catch(SQLException a ){
			
		}
	}
	
	public static int getRandomSkill(){
		int returnInt = 23300;
		Random aleatorio = new Random();
		returnInt += aleatorio.nextInt(90);
		return returnInt;
	}
	
	private static void getAugmentWindows(L2PcInstance player, String Tipo, String idAugment_Interno){
		
		String ItemToShow = "";
		if(Tipo.equalsIgnoreCase("Active")){
			ItemToShow  = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
		}else if(Tipo.equalsIgnoreCase("Passive")){
			ItemToShow = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
		}else if(Tipo.equalsIgnoreCase("Chance")){
			ItemToShow = general.AUGMENT_SPECIAL_PRICE_CHANCE;
		}
		
		String ByPassAceptar = "bypass ZeuS selectAugment " + idAugment_Interno + " $MainSymbol";
		
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-special-augment-doit.htm"); 
		_augmentData temp = AUGMENT_DATA.get(Integer.valueOf(idAugment_Interno));
		html.replace("%TYPE_AUGMENT%", temp.getType());
		html.replace("%DESCRIPT_AUGMENT%", temp.getDescript());
		html.replace("%LEVEL_AUGMENT%", temp.getLevel());
		html.replace("%COST_BOX%", central.ItemNeedShowBox(ItemToShow));
		html.replace("%COST_SYMBOL%", central.ItemNeedShowBox(general.AUGMENT_SPECIAL_PRICE_SYMBOL));
		html.replace("%BYPASS_GETIT%", ByPassAceptar);
		
		central.sendHtml(player, html.getHtml());
	}

	@SuppressWarnings("rawtypes")
	private static NpcHtmlMessage getAugmentAll(L2PcInstance player, int pagina, String tipo, NpcHtmlMessage html){
		
		String ByPassMenu = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SelectAugment.name() + ";list;%TIPO%;0;0;0;0";
		String ColorSel = "89F4FF";
		String ColorNoSel = "01A9DB";
		String ByPassCostActive = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.SelectAugment + ";showPriceActive;0;0;0;0;0;0";
		String ByPassCostPassive = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.SelectAugment + ";showPricePassive;0;0;0;0;0;0";
		String ByPassCostChance = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.SelectAugment + ";showPriceChance;0;0;0;0;0;0";

		html.replace("%SA_MENU_ACTIVE_SKILLS_BYPASS%", ByPassMenu.replace("%TIPO%", "Active"));
		html.replace("%SA_MENU_PASSIVE_SKILLS_BYPASS%", ByPassMenu.replace("%TIPO%", "Passive"));
		html.replace("%SA_MENU_CHANCE_SKILLS_BYPASS%", ByPassMenu.replace("%TIPO%", "Chance"));
		html.replace("%SA_MENU_ACTIVE_SKILLS_FONT_COLOR%", (tipo.equalsIgnoreCase("Active") ? ColorSel : ColorNoSel));
		html.replace("%SA_MENU_PASSIVE_SKILLS_FONT_COLOR%", (tipo.equalsIgnoreCase("Passive") ? ColorSel : ColorNoSel));
		html.replace("%SA_MENU_CHANCE_SKILLS_FONT_COLOR%", (tipo.equalsIgnoreCase("Chance") ? ColorSel : ColorNoSel));
		html.replace("%SA_MENU_ACTIVE_SKILLS_SHOW_PRICE%", ByPassCostActive);
		html.replace("%SA_MENU_PASSIVE_SKILLS_SHOW_PRICE%", ByPassCostPassive);
		html.replace("%SA_MENU_CHANCE_SKILLS_SHOW_PRICE%", ByPassCostChance);

		String GridFormatActive = opera.getGridFormatFromHtml(html, 1, "%GRID_ACTIVE%");
		String GridFormatPassive = opera.getGridFormatFromHtml(html, 2, "%GRID_PASSIVE%");
		String GridFormatChance = opera.getGridFormatFromHtml(html, 3, "%GRID_CHANCE%");
		
		String GridFormat = "";
		
		switch (tipo.toLowerCase()) {
			case "active":
				GridFormat = GridFormatActive;
				html.replace("%GRID_PASSIVE%", "");
				html.replace("%GRID_CHANCE%", "");
				break;
			case "passive":
				GridFormat = GridFormatPassive;
				html.replace("%GRID_ACTIVE%", "");
				html.replace("%GRID_CHANCE%", "");				
				break;
			case "chance":
				GridFormat = GridFormatChance;
				html.replace("%GRID_ACTIVE%", "");
				html.replace("%GRID_PASSIVE%", "");				
				break;
		}

		int Maximo = 15;
		int Desde = pagina * Maximo;
		int Hasta = Desde + Maximo;
		
		int Contador = 0;
		int ContadorTR = 0;
		boolean haveNext = false;
		
		String AllAugmentGrid = "<tr>";
	
		Iterator itr = AUGMENT_DATA.entrySet().iterator();
		String ByPass = "bypass "+general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SelectAugment.name() + ";getinfo;%TIPO%;%IDINTERNO%;"+ String.valueOf(pagina) +";0;0";
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	_augmentData temp = (_augmentData)Entrada.getValue();
	    	String SKILL_TYPE = temp.getType();
	    	if(tipo.equalsIgnoreCase(SKILL_TYPE)){
	    		if(Contador >= Desde && Contador < Hasta){
		    		String SKILL_NAME = temp.getName();
	    			AllAugmentGrid += GridFormat.replace("%SA_SKILLS_TO_SEE_NAME%", SKILL_NAME) .replace("%SA_SKILLS_TO_SEE_BYPASS%", ByPass.replace("%TIPO%", tipo).replace("%IDINTERNO%", String.valueOf(temp.getID())));
	    			ContadorTR++;
	    			if(ContadorTR>=3){
	    				AllAugmentGrid += "</tr><tr>";
	    				ContadorTR=0;
	    			}
	    		}else if(Contador >= Hasta){
	    			haveNext = true;
	    		}
    			Contador++;	    		
	    	}
	    }
		
		if(AllAugmentGrid.endsWith("<tr>")){
			AllAugmentGrid = AllAugmentGrid.substring(0,AllAugmentGrid.length()-4);
		}
		
		if(ContadorTR>0 && ContadorTR<3){
			for(int i=ContadorTR;i<3;i++){
				AllAugmentGrid += "<td fixwidth=250></td>";
			}
			AllAugmentGrid += "</tr>";
		}
		
		switch (tipo.toLowerCase()) {
			case "active":
				html.replace("%GRID_ACTIVE%", AllAugmentGrid);
				break;
			case "passive":
				html.replace("%GRID_PASSIVE%", AllAugmentGrid);
				break;
			case "chance":
				html.replace("%GRID_CHANCE%", AllAugmentGrid);					
				break;
		}		
		
		
		
		/*else if(parm1.equals("list")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));*/

		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.SelectAugment.name() + ";list;" + tipo + ";" +String.valueOf(pagina-1)+";0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.SelectAugment.name() + ";list;" + tipo + ";" +String.valueOf(pagina+1)+";0;0;0";

		html.replace("%SA_SKILLS_PREVIEW_BYPASS%", ( pagina>0 ? bypassAntes : ""));
		html.replace("%SA_SKILLS_NEXT_BYPASS%", (haveNext ? bypassProx : ""));
		html.replace("%SA_SKILL_ACTUAL_PAGE%", String.valueOf(pagina+1));

		return html;
	}

	private static String mainHtml(L2PcInstance player, String TipoBusqueda,int Pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-special-augment.htm");
		html = getAugmentAll(player, Pagina, TipoBusqueda, html);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	private static boolean canDoIt(L2PcInstance activeChar){
		L2ItemInstance parmorInstance = activeChar.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if ((parmorInstance == null) || !parmorInstance.isWeapon() || (parmorInstance.isCommonItem() || (parmorInstance.isEtcItem() || (parmorInstance.isTimeLimitedItem()))))
		{
			activeChar.sendPacket(SystemMessageId.THIS_IS_NOT_A_SUITABLE_ITEM);
			return false;
		}
		
		if (parmorInstance.isAugmented())
		{
			activeChar.sendPacket(SystemMessageId.ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN);
			return false;
		}
		return true;
	}
	
	public static void MakeAugment(L2PcInstance player, int idMainSkill, int idAugment, boolean SysmbolRequested){
		
		if(!canDoIt(player)){
			return;
		}
		
		
		//MakeAugment(player, idMainSymbol, idAugment, symbolRequested);
		
		String ItemToRemove = "";
		_augmentData temp = AUGMENT_DATA.get(idAugment);
		
		if(temp.getType().equalsIgnoreCase("Active")){
			ItemToRemove  = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
		}else if(temp.getType().equalsIgnoreCase("Passive")){
			ItemToRemove = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
		}else if(temp.getType().equalsIgnoreCase("Chance")){
			ItemToRemove = general.AUGMENT_SPECIAL_PRICE_CHANCE;
		}
		if(!opera.haveItem(player, ItemToRemove)){
			return;
		}
		if(idMainSkill>0 && SysmbolRequested){
			if(general.AUGMENT_SPECIAL_PRICE_SYMBOL.length()==0){
				central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
				return;
			}
			if(!opera.haveItem(player, general.AUGMENT_SPECIAL_PRICE_SYMBOL)){
				return;
			}
		}
		
		opera.removeItem(ItemToRemove, player);
		if(idMainSkill>0 && SysmbolRequested){
			opera.removeItem(general.AUGMENT_SPECIAL_PRICE_SYMBOL, player);
		}
		//MakeAugment(player, idMainSymbol, idAugment, symbolRequested);
		setAugment(player, idMainSkill, idAugment);
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_AUGMENT_SPECIAL){
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}
		Map<String, String> Precios = new HashMap<String, String>();
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,"Active",0);
		}else if(parm1.equals("list")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("getinfo")){
			getAugmentWindows(player, parm2, parm3);
			return mainHtml(player,parm2,Integer.valueOf(parm4));
		}else if(parm1.equals("setAugment")){
			String ItemToRemove = "";
			if(parm3.equalsIgnoreCase("Active")){
				ItemToRemove  = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
			}else if(parm3.equalsIgnoreCase("Passive")){
				ItemToRemove = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
			}else if(parm3.equalsIgnoreCase("Chance")){
				ItemToRemove = general.AUGMENT_SPECIAL_PRICE_CHANCE;
			}
			if(!opera.haveItem(player, ItemToRemove)){
				return "";			
			}	
		}else if(parm1.equals("showPriceActive")){
			Precios.put("Cost", general.AUGMENT_SPECIAL_PRICE_ACTIVE);
			Precios.put("Symbol Cost", general.AUGMENT_SPECIAL_PRICE_SYMBOL);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.AUGMENT_SPECIAL_LVL) + ";Need Noble:" + ( general.AUGMENT_SPECIAL_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Active Augment");			
		}else if(parm1.equals("showPricePassive")){
			Precios.put("Cost", general.AUGMENT_SPECIAL_PRICE_PASSIVE);
			Precios.put("Symbol Cost", general.AUGMENT_SPECIAL_PRICE_SYMBOL);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.AUGMENT_SPECIAL_LVL) + ";Need Noble:" + ( general.AUGMENT_SPECIAL_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Active Passive");
		}else if(parm1.equals("showPriceChance")){
			Precios.put("Cost", general.AUGMENT_SPECIAL_PRICE_CHANCE);
			Precios.put("Symbol Cost", general.AUGMENT_SPECIAL_PRICE_SYMBOL);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.AUGMENT_SPECIAL_LVL) + ";Need Noble:" + ( general.AUGMENT_SPECIAL_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Active Chance");			
		}
		return "";
	}


	@SuppressWarnings("unused")
	private static boolean AlternativeEnchant(L2PcInstance player, int Obj, int stoneId, boolean AplicarElemento)
	{
		
		boolean success = true;

		L2ItemInstance item = player.getInventory().getItemByObjectId(Obj);
		
		boolean isWeapon = item.isWeapon();

		if (item == null)
		{
			if(AplicarElemento){
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}

		if(!item.getItem().isElementable()){
			if(AplicarElemento){
				player.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.ELEMENTAL_ENHANCE_REQUIREMENT_NOT_SUFFICIENT));
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}


		switch (item.getItemLocation())
		{
			case INVENTORY:
			case PAPERDOLL:
			{
				if (item.getOwnerId() != player.getObjectId())
				{
					if(AplicarElemento){
						player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
					}
					return false;
				}
				break;
			}
			default:
			{
				if(AplicarElemento){
					player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
					Util.handleIllegalPlayerAction(player, "Player " + player.getName() + " tried to use enchant Exploit!", Config.DEFAULT_PUNISH);
				}
				return false;
			}
		}

		byte elementToAdd = Elementals.getItemElement(stoneId);
		// Armors have the opposite element
		if (item.isArmor())
		{
			elementToAdd = Elementals.getOppositeElement(elementToAdd);
		}

		//elementToAdd = Elementals.getOppositeElement(elementToAdd);

		byte opositeElement = Elementals.getOppositeElement(elementToAdd);

		Elementals oldElement = item.getElemental(elementToAdd);

		int elementValue = oldElement == null ? 0 : oldElement.getValue();
		int limit = getLimit(item, stoneId);
		int powerToAdd = getPowerToAdd(stoneId, elementValue, item);

		int idElementalActual = item.getAttackElementType();
		int idElementalPoner = elementToAdd;

		if(  ( item.isWeapon() && ( ((idElementalActual != idElementalPoner) && (idElementalActual != -2)) )    )
			|| (item.isArmor() && (item.getElemental(elementToAdd) == null) && (item.getElementals() != null) && (item.getElementals().length >= 3)))
		{
			if(AplicarElemento){
				player.sendPacket(SystemMessageId.ANOTHER_ELEMENTAL_POWER_ALREADY_ADDED);
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}

		if (item.isArmor() && (item.getElementals() != null))
		{
			// cant add opposite element
			for (Elementals elm : item.getElementals())
			{
				boolean isSameElement = (Elementals.getItemElement(stoneId) == elm.getElement());
				if (!isElementalOK(elm.getElement(), opositeElement) )
				{
					if(!isSameElement){
						player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
						return false;
					}
				}
			}
		}

		int newPower = elementValue + powerToAdd;
		if (newPower > limit)
		{
			newPower = limit;
			powerToAdd = limit - elementValue;
		}
		if(AplicarElemento){
			if(isWeapon){
				if(newPower>general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON){
					central.msgbox(language.getInstance().getMsg(player).ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED, player);
					return false;
				}
			}else{
				if(newPower>general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR){
					central.msgbox(language.getInstance().getMsg(player).ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED, player);
					return false;
				}			
			}
		}

		if (powerToAdd <= 0)
		{
			if(AplicarElemento){
				player.sendPacket(SystemMessageId.ELEMENTAL_ENHANCE_CANCELED);
				player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);
			}
			return false;
		}

		byte realElement = item.isArmor() ? opositeElement : elementToAdd;

		if(!AplicarElemento){
			return true;
		}
		
		SystemMessage sm;
		if (item.getEnchantLevel() == 0)
		{
			if (item.isArmor())
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_S2_ATTRIBUTE_WAS_SUCCESSFULLY_BESTOWED_ON_S1_RES_TO_S3_INCREASED);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.ELEMENTAL_POWER_S2_SUCCESSFULLY_ADDED_TO_S1);
			}
			sm.addItemName(item);
			sm.addElemental(realElement);
			if (item.isArmor())
			{
				sm.addElemental(Elementals.getOppositeElement(realElement));
			}
		}
		else
		{
			if (item.isArmor())
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_S3_ATTRIBUTE_BESTOWED_ON_S1_S2_RESISTANCE_TO_S4_INCREASED);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.ELEMENTAL_POWER_S3_SUCCESSFULLY_ADDED_TO_S1_S2);
			}
			sm.addInt(item.getEnchantLevel());
			sm.addItemName(item);
			sm.addElemental(realElement);
			if (item.isArmor())
			{
				sm.addElemental(Elementals.getOppositeElement(realElement));
			}
		}
		player.sendPacket(sm);
		item.setElementAttr(elementToAdd, newPower);
		if (item.isEquipped())
		{
			item.updateElementAttrBonus(player);
		}

		// send packets
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(item);
		player.sendPacket(iu);

		player.sendPacket(new ExAttributeEnchantResult(powerToAdd));
		player.sendPacket(new UserInfo(player));
		player.sendPacket(new ExBrExtraUserInfo(player));
		player.setActiveEnchantAttrItemId(L2PcInstance.ID_NONE);

		return success;
	}
	private static boolean isElementalOK(int TypeObject, int toCheck){
		if((TypeObject==0 && toCheck==1) || (TypeObject==1 && toCheck==0)){
			return false;
		}else if((TypeObject==2 && toCheck==3) || (TypeObject==3 && toCheck==2)){
			return false;
		}else if((TypeObject==4 && toCheck==5) || (TypeObject==5 && toCheck==4)){
			return false;
		}
		return true;
	}	
	private static int getPowerToAdd(int stoneId, int oldValue, L2ItemInstance item)
	{
		if (Elementals.getItemElement(stoneId) != Elementals.NONE)
		{
			if (item.isWeapon())
			{
				if (oldValue == 0)
				{
					return Elementals.FIRST_WEAPON_BONUS;
				}
				return Elementals.NEXT_WEAPON_BONUS;
			}
			else if (item.isArmor())
			{
				return Elementals.ARMOR_BONUS;
			}
		}
		return 0;
	}	
	
	private static int getLimit(L2ItemInstance item, int sotneId)
	{
		ElementalItems elementItem = Elementals.getItemElemental(sotneId);
		if (elementItem == null)
		{
			return 0;
		}

		if (item.isWeapon())
		{
			return Elementals.WEAPON_VALUES[elementItem._type._maxLevel];
		}
		return Elementals.ARMOR_VALUES[elementItem._type._maxLevel];
	}

	private static void setAugment(L2PcInstance activeChar, int idBaseSkill, int idSecondarySkill){
		
		int secondarySkill = idSecondarySkill + 8358;
		
		L2ItemInstance parmorInstance = activeChar.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND);
		if ((parmorInstance == null) || !parmorInstance.isWeapon() || (parmorInstance.isCommonItem() || (parmorInstance.isEtcItem() || (parmorInstance.isTimeLimitedItem()))))
		{
			activeChar.sendPacket(SystemMessageId.THIS_IS_NOT_A_SUITABLE_ITEM);
			return;
		}
		
		if (parmorInstance.isAugmented())
		{
			activeChar.sendPacket(SystemMessageId.ONCE_AN_ITEM_IS_AUGMENTED_IT_CANNOT_BE_AUGMENTED_AGAIN);
			return;
		}
		
		// set augment skill
		activeChar.getInventory().unEquipItemInSlot(Inventory.PAPERDOLL_RHAND);
		parmorInstance.setAugmentation(new L2Augmentation(((secondarySkill << 16) + idBaseSkill)));
		activeChar.getInventory().equipItem(parmorInstance);
		activeChar.sendPacket(SystemMessageId.THE_ITEM_WAS_SUCCESSFULLY_AUGMENTED);
		//activeChar.sendPacket(new ExShowScreenMessage("You got " + stats[1] + " " + stats[2] + " and " + temp_stat + "", 5000));
		
		activeChar.broadcastPacket(new MagicSkillUse(activeChar, 6463, 1, 1000, 0));
		
		// send packets
		InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(parmorInstance);
		activeChar.sendPacket(iu);
		activeChar.broadcastUserInfo();
	}

	public static boolean CanUseStoneOnThis(L2PcInstance player, int idObj, int idElemental){
		return AlternativeEnchant(player, idObj, idElemental,false);
	}
}

class _augmentData{
	private int ID_AUGMENT;
	private int LEVEL;
	private String TYPE;
	private String NAME;
	private String DESCRIPT;
	public _augmentData(int _ID, int _Level, String _type, String _name, String _descript){
		this.ID_AUGMENT = _ID;
		this.LEVEL = _Level;
		this.TYPE = _type;
		this.NAME = _name;
		this.DESCRIPT = _descript;
	}
	
	public final int getID(){
		return this.ID_AUGMENT;
	}
	public final int getLevel(){
		return this.LEVEL;
	}
	public final String getDescript(){
		return this.DESCRIPT;
	}
	public final String getName(){
		return this.NAME;
	}
	public final String getType(){
		return this.TYPE;
	}
}
