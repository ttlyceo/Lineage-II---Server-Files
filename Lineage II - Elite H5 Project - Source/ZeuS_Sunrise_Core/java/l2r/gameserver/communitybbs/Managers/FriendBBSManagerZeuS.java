package l2r.gameserver.communitybbs.Managers;

import java.util.logging.Logger;

import ZeuS.ZeuS.ZeuS;
import ZeuS.language.language;
import l2r.gameserver.cache.HtmCache;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ShowBoard;

public class FriendBBSManagerZeuS
{
	private static Logger _log = Logger.getLogger(FriendBBSManagerZeuS.class.getName());
	
	public void cbByPassZeuS(String command, L2PcInstance activeChar)
	{
		if (ZeuS.cbByPass(activeChar, command))
		{
			return;
		}	
		String path = "./config/zeus/htm/" + language.getInstance().getFolder(activeChar) + "/communityboard/serverMarket.htm";
		String content = "";
		if (command.startsWith("_friendlist"))
		{
			content = HtmCache.getInstance().getHtm("", path);
			this.separateAndSend(content, activeChar);
		}
	}
	
	protected void separateAndSend(String html, L2PcInstance acha)
	{
		if (html == null)
		{
			return;
		}
		if (html.length() < 8180)
		{
			acha.sendPacket(new ShowBoard(html, "101"));
			acha.sendPacket(new ShowBoard(null, "102"));
			acha.sendPacket(new ShowBoard(null, "103"));
		}
		else if (html.length() < 16360)
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 8180), "101"));
			acha.sendPacket(new ShowBoard(html.substring(8180, html.length()), "102"));
			acha.sendPacket(new ShowBoard(null, "103"));
		}
		else if (html.length() < 24540)
		{
			acha.sendPacket(new ShowBoard(html.substring(0, 8180), "101"));
			acha.sendPacket(new ShowBoard(html.substring(8180, 16360), "102"));
			acha.sendPacket(new ShowBoard(html.substring(16360, html.length()), "103"));
		}
	}
	
	public static FriendBBSManagerZeuS getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final FriendBBSManagerZeuS _instance = new FriendBBSManagerZeuS();
	}
}