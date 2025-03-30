package ZeuS.Comunidad.EngineForm;

import java.util.Vector;
import java.util.logging.Logger;

import l2r.Config;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.enums.ElementalItems;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ExAttributeEnchantResult;
import l2r.gameserver.network.serverpackets.ExBrExtraUserInfo;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.gameserver.network.serverpackets.UserInfo;
import l2r.gameserver.util.Util;
import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.ZeuSConditions;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class v_ElementalSpecial{
	
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(v_ElementalSpecial.class.getName());
	
	private static NpcHtmlMessage getObject(L2PcInstance player, NpcHtmlMessage html){
		int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND };
		String Bypass = "bypass -h ZeuS specialOpt " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SelectElemental + ";Elemental;%TIPO%;%LUGAR%;0;0;0";
		int Contador = 1;
		String NOITEM = ItemData.getInstance().getTemplate(3883).getIcon();
		
		for(int Info : VectorLocaciones){
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				String ImagenItem = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String NombreItem = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				
				html.replace("%ICON_"+ String.valueOf(Contador) +"%", ImagenItem);
				html.replace("%ITEM_NAME_"+ String.valueOf(Contador) +"%", NombreItem);
				html.replace("%BYPASS_"+ String.valueOf(Contador) +"%", Bypass.replace("%TIPO%", " $cmbElemental ").replace("%LUGAR%", String.valueOf(Info)));
			}else{
				html.replace("%ICON_"+ String.valueOf(Contador) +"%", NOITEM);
				html.replace("%ITEM_NAME_"+ String.valueOf(Contador) +"%", "NO ITEM");
				html.replace("%BYPASS_"+ String.valueOf(Contador) +"%", "");
			}
			Contador++;
		}
		
		return html;
	}
	
	private static String getMainWindows(L2PcInstance player, NpcHtmlMessage html){
		
		Vector<String> Reque = new Vector<String>();
		Reque.add("Just Noble:," + (general.ELEMENTAL_NOBLE ? "Yes" : "No"));
		Reque.add("Lv to Use:," + String.valueOf(general.ELEMENTAL_LVL));
		Reque.add("Max Armor Power:," + String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR));
		Reque.add("Max Weapon Power:," + String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON));
		
		String GridFormatcost = opera.getGridFormatFromHtml(html, 1, "%COST%");
		String GridFormatreque = opera.getGridFormatFromHtml(html, 2, "%REQUE%");
		
		String CostWindows = "";
				for(String p1 : general.ELEMENTAL_ITEM_PRICE.split(";")){
			String Item = p1.split(",")[0], Cantidad = p1.split(",")[1];
			CostWindows += GridFormatcost.replace("%COST_ITEM%", central.getNombreITEMbyID(Integer.valueOf(Item))) .replace("%COST_QUANTITY%", opera.getFormatNumbers(Cantidad)); 
		}
		
		String Requerimientos = "";
		for(String p1 : Reque){
			String RequeSTR = p1.split(",")[0], Valor = p1.split(",")[1];
			Requerimientos += GridFormatreque.replace("%REQUERIMENTS_VALUE%", Valor).replace("%REQUERIMENTS_NAME%", RequeSTR); 
		}
		String ByPassRefresh = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SelectElemental.name() + ";0;0;0;0;0;0";
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%COST%", CostWindows);
		html.replace("%REQUE%", Requerimientos);
		html.replace("%BYPASS_REFRESH%", ByPassRefresh);
		html = getObject(player,html);
		return  html.getHtml();
	}
	
	private static String mainHtml(L2PcInstance player, String Params, String ElementalChoose){
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-special-element.htm");
		String strElemental = "";
		String CadenaString = "Fire;Water;Wind;Earth;Dark;Holy";//"%ATTRIBUTE_ELEMENTALS%"
		if(ElementalChoose.trim().length() > 2 ){
			strElemental = ElementalChoose.trim();
		}
		for(String attr : CadenaString.split(";")){
			if(attr.equalsIgnoreCase(ElementalChoose)){
				continue;
			}
			if(strElemental.length()>0){
				strElemental += ";";
			}
			strElemental += attr;
		}
		html.replace("%ATTRIBUTE_ELEMENTALS%", strElemental);
		return getMainWindows(player,html);
	}
	
	public static boolean AplicarElemental(L2PcInstance st, String ElementalType, String ID_objectPlace){
		
		if(!general._activated()){
			return false;
		}

		if(general.ELEMENTAL_NOBLE && !st.isNoble()){
			central.msgbox (language.getInstance().getMsg(st).NEED_TO_BE_NOBLE,st);
			return false;
		}
		
		if(!opera.haveItem(st, general.ELEMENTAL_ITEM_PRICE)){
			return false;
		}

		if(st.getLevel() < general.ELEMENTAL_LVL){
			central.msgbox( language.getInstance().getMsg(st).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level", String.valueOf(general.ELEMENTAL_LVL)),st);
			return false;
		}

		int IDItemEnchant =0;

		switch(ElementalType.trim()){
			case "Fire":
				IDItemEnchant = 9558;
				break;
			case "Water":
				IDItemEnchant = 9559;
				break;
			case "Wind":
				IDItemEnchant = 9561;
				break;
			case "Earth":
				IDItemEnchant = 9560;
				break;
			case "Dark":
				IDItemEnchant = 9562;
				break;
			case "Holy":
				IDItemEnchant = 9563;
				break;
		}
		int IDLugar = Integer.valueOf(ID_objectPlace);
		L2ItemInstance itemSelecc = st.getInventory().getPaperdollItem(IDLugar);
		int IDObjeto = itemSelecc.getObjectId();


		if(AlternativeEnchant(st,IDObjeto,IDItemEnchant)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean CanUseStoneOnThis(L2PcInstance player, int idObj, int idElemental){
		return AlternativeEnchant(player, idObj, idElemental,false);
	}
	
	private static boolean AlternativeEnchant(L2PcInstance player, int Obj, int stoneId){
		return AlternativeEnchant(player, Obj, stoneId,true);
	}

	@SuppressWarnings("unused")
	private static boolean AlternativeEnchant(L2PcInstance player, int Obj, int stoneId, boolean AplicarElemento)
	{
		
		boolean success = true;
		L2Item itemStone = ItemData.getInstance().getTemplate(stoneId);
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
	
		if(item.isWeapon() && item.getAttackElementPower() > 0){
			int idAttribute = item.getAttackElementType();
			int idStoneInWeapon = ZeuSConditions.getStoneIdToID(idAttribute);
			if(idStoneInWeapon != stoneId){
				player.sendPacket(SystemMessageId.ANOTHER_ELEMENTAL_POWER_ALREADY_ADDED);
				return false;
			}
		}else if(item.isArmor() && item.getElementals() != null){
			Elementals[] idAttribute = item.getElementals();
			if(idAttribute != null){
				for(Elementals AttributeTypes : idAttribute){
					int idArmorAttribute = AttributeTypes.getElement(); //.getType().getClientId();
					int idArmorStoneInWeapon = ZeuSConditions.getStoneIdToID(idArmorAttribute);
					switch(idArmorStoneInWeapon){
						case 9558:
							if(stoneId == 9558){
								return false;
							}
							break;
						case 9559:
							if(stoneId == 9559){
								return false;
							}
							break;
						case 9560:
							if(stoneId == 9560){
								return false;
							}
							break;
						case 9561:
							if(stoneId == 9561){
								return false;
							}
							break;		
						case 9562:
							if(stoneId == 9562){
								return false;
							}
							break;		
						case 9563:
							if(stoneId == 9563){
								return false;
							}
							break;						
					}
				}
			}
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
	
		/*if (item.isArmor() && (item.getElementals() != null))
		{
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
		}*/
		
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
		
		if(item.isWeapon()){
			if(newPower > general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON){
				central.msgbox(language.getInstance().getMsg(player).ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED, player);
				return false;
			}
		}else if(item.isArmor()){
			if(newPower > general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR){
				central.msgbox(language.getInstance().getMsg(player).ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED, player);
				return false;
			}			
		}else{
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
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_ELEMENT_ENHANCED) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}		
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3].trim();
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player, params, parm2);
		}else if(parm1.equals("Elemental")){
			if(AplicarElemental(player, parm2.trim(), parm3.trim())){
				opera.removeItem(general.ELEMENTAL_ITEM_PRICE, player);
			}else{
				central.msgbox("Elemental Error.", player);
			}
			return mainHtml(player, params, parm2);
		}
		return "";
	}
}
