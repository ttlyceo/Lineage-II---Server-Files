package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.server.comun;

public class v_blacksmith extends v_Shop{

	private static Map<Integer, String>ByPassChar = new HashMap<Integer, String>();
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(v_blacksmith.class.getName());
	private static String mainHtml(L2PcInstance player, String Params){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/communityboard/engine-blacksmith.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_BLACKSMITH){
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
			ByPassChar.put(player.getObjectId(), params);
			return mainHtml(player,params);
		}else if(parm1.equals("multi_show")){
			cbManager.separateAndSend(mainHtml(player, ByPassChar.get(player.getObjectId())), player);
			v_Shop.showMultisell(player, Integer.valueOf(parm2));
		}else if(parm1.equals("multiexc_show")){
			cbManager.separateAndSend(mainHtml(player, ByPassChar.get(player.getObjectId())), player);
			v_Shop.showExcMultisell(player, Integer.valueOf(parm2));
		}
		return "";
	}
}
