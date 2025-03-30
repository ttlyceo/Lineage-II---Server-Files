package ZeuS.Comunidad.EngineForm;

import java.util.Vector;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_EnchantSpecial{
	
	private static NpcHtmlMessage getObject(L2PcInstance player, NpcHtmlMessage html){
		int[] VectorLocaciones = {
			Inventory.PAPERDOLL_HEAD,
			Inventory.PAPERDOLL_CHEST,
			Inventory.PAPERDOLL_LEGS,
			Inventory.PAPERDOLL_GLOVES,
			Inventory.PAPERDOLL_FEET,
			Inventory.PAPERDOLL_RHAND,
			Inventory.PAPERDOLL_LHAND,
			Inventory.PAPERDOLL_UNDER,
			Inventory.PAPERDOLL_BELT,
			Inventory.PAPERDOLL_LEAR,
			Inventory.PAPERDOLL_REAR,
			Inventory.PAPERDOLL_LFINGER,
			Inventory.PAPERDOLL_RFINGER,
			Inventory.PAPERDOLL_NECK,
			Inventory.PAPERDOLL_LBRACELET};

		String Bypass = "bypass -h ZeuS specialOpt " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SelectEnchant.name() + ";Enchant;%POSI%;%IDITEM%;0;0;0";
		
		int Contador = 1;
		
		String NOITEM = ItemData.getInstance().getTemplate(3883).getIcon();
		
		for(int Info : VectorLocaciones){
			if (player.getInventory().getPaperdollItem(Info)!=null) {
				String ImagenItem = player.getInventory().getPaperdollItem(Info).getItem().getIcon();
				String NombreItem = central.getNombreITEMbyID(player.getInventory().getPaperdollItem(Info).getItem().getId());
				L2ItemInstance itemSelecc = player.getInventory().getPaperdollItem(Info);
				String IDObjeto = String.valueOf(itemSelecc.getObjectId()); 				
				html.replace("%ICON_"+ String.valueOf(Contador) +"%", ImagenItem);
				html.replace("%ITEM_NAME_"+ String.valueOf(Contador) +"%", NombreItem);				
				html.replace("%BYPASS_"+ String.valueOf(Contador) +"%", Bypass.replace("%POSI%", String.valueOf(Info)).replace("%IDITEM%", IDObjeto));
			}else{
				html.replace("%ICON_"+ String.valueOf(Contador) +"%", NOITEM);
				html.replace("%ITEM_NAME_"+ String.valueOf(Contador) +"%", "No Item");				
				html.replace("%BYPASS_"+ String.valueOf(Contador) +"%", "");
			}
			Contador++;
		}
		
		return html;
	}	
	
	private static NpcHtmlMessage getMainWindows(L2PcInstance player, NpcHtmlMessage html){
		
		Vector<String> Reque = new Vector<String>();
		Reque.add("Just Noble:," + (general.ENCHANT_NOBLE ? "Yes" : "No"));
		Reque.add("Lv to Use:," + String.valueOf(general.ENCHANT_LVL ));
		Reque.add("Min. Enchant:," + String.valueOf(general.ENCHANT_MIN_ENCHANT));
		Reque.add("Max. Enchant:," + String.valueOf(general.ENCHANT_MAX_ENCHANT));
		Reque.add("Enchant plus:," + String.valueOf(general.ENCHANT_x_VEZ));

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

		
		
		String ByPassRefresh = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.SelectEnchant.name() + ";0;0;0;0;0;0";
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%COST%", CostWindows);
		html.replace("%REQUE%", Requerimientos);
		html.replace("%BYPASS_REFRESH%", ByPassRefresh);
		
		html = getObject(player, html);
		
		return  html;
	}	
	
	
	
	
	private static String mainHtml(L2PcInstance player, String Params){
		NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-special-enchant.htm");
		
		html = getMainWindows(player, html);
		return html.getHtml();
	}
	public static boolean AplicarEnchantItem(L2PcInstance st,String PosicionDelItem, String IDOBJETO_del_Item){
		if(!general._activated()){
			return false;
		}
		int IDPosiInventario = Integer.valueOf(PosicionDelItem);
		int IDObjetoInventario = Integer.valueOf(IDOBJETO_del_Item);

		if(!opera.haveItem(st, general.ENCHANT_ITEM_PRICE)){
			return false;
		}
		
		if (st.getInventory().getPaperdollItem(IDPosiInventario).equals(null)){
			central.msgbox(language.getInstance().getMsg(st).ENCHANT_YOU_MOVE_THE_ITEM,st);
			//return mainHtmlEnchantItem(st)
			return false;
		}

		L2ItemInstance itemSelecc = st.getInventory().getPaperdollItem(IDPosiInventario);
		int IDObjeto = itemSelecc.getObjectId();

		if (IDObjeto != IDObjetoInventario){
			central.msgbox(language.getInstance().getMsg(st).ELEMENTAL_ATRIBUTTE_MAXIMUS_REACHED,st);
			return false;
		}

		L2ItemInstance Item = st.getInventory().getItemByObjectId(IDObjetoInventario);

		if (general.ENCHANT_NOBLE && !st.isNoble()){
			central.msgbox(language.getInstance().getMsg(st).NEED_TO_BE_NOBLE,st);
			return false;
		}

		if(st.getLevel() < general.ENCHANT_LVL){
			central.msgbox(language.getInstance().getMsg(st).NEED_TO_BE_LEVEL_FOR_THIS_OPERATION.replace("$level",  String.valueOf(general.ENCHANT_LVL)),st);
			return false;
		}

		if((Item.getEnchantLevel() < general.ENCHANT_MIN_ENCHANT) || (Item.getEnchantLevel() >= general.ENCHANT_MAX_ENCHANT)){
			central.msgbox(language.getInstance().getMsg(st).ENCHANT_ITEM_NO_MEET_PARAMETERS_TO_ENCHANT.replace("$item",Item.getItem().getName()).replace("$minenchant",String.valueOf(general.ENCHANT_MIN_ENCHANT)).replace("$maxenchant",String.valueOf(general.ENCHANT_MAX_ENCHANT)),st);
			return false;
		}

		int IntEnchant = Item.getEnchantLevel() + general.ENCHANT_x_VEZ;

		if(IntEnchant > general.ENCHANT_MAX_ENCHANT){
			Item.setEnchantLevel(general.ENCHANT_MAX_ENCHANT);
			IntEnchant = general.ENCHANT_MAX_ENCHANT;
		}else{
			Item.setEnchantLevel(IntEnchant);
		}
		final InventoryUpdate iu = new InventoryUpdate();
		iu.addModifiedItem(Item);
		st.sendPacket(iu);
		st.broadcastUserInfo();
		st.getInventory().reloadEquippedItems();
		central.msgbox(language.getInstance().getMsg(st).ENCHANT_CONGRATULATION_ITEM_HAS_NEW_ENCHANT.replace("$item",Item.getItem().getName()).replace("$enchant",String.valueOf(IntEnchant)),st);
		return true;
	}	
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_ENCANTAMIENTO_ITEM) {
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
			return mainHtml(player,params);
		}else if(parm1.equals("Enchant")){
			if(AplicarEnchantItem(player,parm2.trim(),parm3.trim() )){
				opera.removeItem(general.ENCHANT_ITEM_PRICE, player);
			}else{
				central.msgbox("Enchant Error.", player);
			}
			return mainHtml(player,params);
		}
		return "";
	}
}
