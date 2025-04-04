package ZeuS.Comunidad.EngineForm;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.server.comun;
import l2r.gameserver.model.ClanPrivilege;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.SortedWareHouseWithdrawalList;
import l2r.gameserver.network.serverpackets.WareHouseDepositList;
import l2r.gameserver.network.serverpackets.WareHouseWithdrawalList;
import l2r.gameserver.network.serverpackets.SortedWareHouseWithdrawalList.WarehouseListType;

public class v_Warehouse {

	private static final void showPWithdrawWindow(L2PcInstance player, WarehouseListType itemtype, byte sortorder)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		player.setActiveWarehouse(player.getWarehouse());
		
		if (player.getActiveWarehouse().getSize() == 0)
		{
			player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
			return;
		}
		
		if (itemtype != null)
		{
			player.sendPacket(new SortedWareHouseWithdrawalList(player, WareHouseWithdrawalList.PRIVATE, itemtype, sortorder));
		}
		else
		{
			player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.PRIVATE));
		}
	}
	
	
	private static final void showCWithdrawWindow(L2PcInstance player, WarehouseListType itemtype, byte sortorder)
	{
		player.sendPacket(ActionFailed.STATIC_PACKET);
		
		if (!player.hasClanPrivilege(ClanPrivilege.CL_VIEW_WAREHOUSE))
		{
			player.sendPacket(SystemMessageId.YOU_DO_NOT_HAVE_THE_RIGHT_TO_USE_CLAN_WAREHOUSE);
			return;
		}
		
		player.setActiveWarehouse(player.getClan().getWarehouse());
		
		if (player.getActiveWarehouse().getSize() == 0)
		{
			player.sendPacket(SystemMessageId.NO_ITEM_DEPOSITED_IN_WH);
			return;
		}
		
		if (itemtype != null)
		{
			player.sendPacket(new SortedWareHouseWithdrawalList(player, WareHouseWithdrawalList.CLAN, itemtype, sortorder));
		}
		else
		{
			player.sendPacket(new WareHouseWithdrawalList(player, WareHouseWithdrawalList.CLAN));
		}
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-warehouse.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_WAREHOUSE){
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
		}else if(parm1.equals("depo_ware_per")){
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.setActiveWarehouse(player.getWarehouse());
			if (player.getWarehouse().getSize() == player.getWareHouseLimit())
			{
				player.sendPacket(SystemMessageId.WAREHOUSE_FULL);
			}
			player.setIsUsingAioWh(true);
			player.tempInventoryDisable();
			player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.PRIVATE));		
			
		}else if(parm1.equals("saca_ware_per")){
			player.setIsUsingAioWh(true);
			showPWithdrawWindow(player, WarehouseListType.ALL, SortedWareHouseWithdrawalList.A2Z);
			
		}else if(parm1.equals("depo_ware_clan")){
			if (player.getClan() == null)
			{
				player.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER);
				return "";
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.setActiveWarehouse(player.getClan().getWarehouse());
			
			if (player.getClan().getLevel() == 0)
			{
				player.sendPacket(SystemMessageId.ONLY_LEVEL_1_CLAN_OR_HIGHER_CAN_USE_WAREHOUSE);
				return "";
			}
			player.setIsUsingAioWh(true);
			player.setActiveWarehouse(player.getClan().getWarehouse());
			player.tempInventoryDisable();
			player.sendPacket(new WareHouseDepositList(player, WareHouseDepositList.CLAN));			
		}else if(parm1.equals("saca_ware_clan")){
			player.setIsUsingAioWh(true);
			showCWithdrawWindow(player, WarehouseListType.ALL, SortedWareHouseWithdrawalList.A2Z);
		}
		
		return "";
	}
}
