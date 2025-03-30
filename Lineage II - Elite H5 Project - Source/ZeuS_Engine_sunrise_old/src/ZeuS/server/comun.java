package ZeuS.server;

import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.imagen._ImageGenerator;
import l2r.Config;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class comun {
	public static NpcHtmlMessage htmlMaker(L2PcInstance player, String filename)
	{
		return htmlMaker(0, player, filename);
	}
	public static NpcHtmlMessage htmlMaker(L2Npc npc, L2PcInstance player, String filename)
	{
		return htmlMaker(npc.getObjectId(), player, filename);
	}
	public static NpcHtmlMessage htmlMaker(int npcObjectId, L2PcInstance player, String filename)
	{
		NpcHtmlMessage html = null;
		if(npcObjectId > 0){
			html = new NpcHtmlMessage(npcObjectId);
		}else{
			html = new NpcHtmlMessage();
		}
		html.setFile(player, (player != null ? player.getHtmlPrefix() : ""), filename);
		html.replace("%objectId%", String.valueOf(npcObjectId));
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%L2SOW_LOGO%", getImageLogoSOW(player));
		return html;
	}
	
	/*L2SOW*/
	public static String getImageLogoSOW(L2PcInstance player){
		_ImageGenerator.getInstance().generateImage(player, 150);
    	String imgLogo = "Crest.crest_" + Config.SERVER_ID + "_150";
    	return imgLogo;
	}
}
