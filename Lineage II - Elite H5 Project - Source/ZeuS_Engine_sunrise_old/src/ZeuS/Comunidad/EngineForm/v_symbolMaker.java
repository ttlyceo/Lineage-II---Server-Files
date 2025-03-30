package ZeuS.Comunidad.EngineForm;


import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.data.xml.impl.HennaData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Henna;
import l2r.gameserver.network.serverpackets.HennaEquipList;
import l2r.gameserver.network.serverpackets.HennaItemRemoveInfo;
import l2r.gameserver.network.serverpackets.HennaRemoveList;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_symbolMaker {
	@SuppressWarnings("unused")
	private static String mainHtml(L2PcInstance player, String Params){

		String bypass_Draw = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Symbolmaker.name() + ";draw;0;0;0;0;0";
		String bypass_Delete = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Symbolmaker.name() + ";delete;0;0;0;0;0";		
		
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-symbol-maker.htm");
		String imageLogo = opera.getImageLogo(player);
		html.replace("%BYPASS_DRAW%", bypass_Draw);
		html.replace("%BYPASS_DELETE%", bypass_Delete);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_SYMBOL_MARKET) {
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
		}else if(parm1.equals("draw")){
			HennaEquipList hel = new HennaEquipList(player);
			player.sendPacket(hel);			
		}else if(parm1.equals("delete")){
			boolean hasHennas = false;
			int num = 0;
			for (int i = 1; i <= 3; i++)
			{
				L2Henna henna = player.getHennaEx().getHenna(i);
				if (henna != null)
				{
					hasHennas = true;
				}
			}
			if (hasHennas)
			{
				player.sendPacket(new HennaRemoveList(player));
			}
			else
			{
				player.sendMessage("You do not have dyes.");
			}			
		}
		return mainHtml(player,params);
	}
}
