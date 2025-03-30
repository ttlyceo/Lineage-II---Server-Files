package ZeuS.procedimientos.imagen;

import java.io.File;
import java.util.logging.Logger;

import l2r.gameserver.model.actor.instance.L2PcInstance;

public class _ImageGenerator
{
	public static final Logger _log = Logger.getLogger(_ImageGenerator.class.getName());
	
	public void generateImage(L2PcInstance player, int imgId)
	{
		if(player == null){
			return;
		}
		try
		{
			File image = new File("config/zeus/images/" + String.valueOf(imgId) + ".png");
			_PledgeImage packet = new _PledgeImage(imgId, _DDsConverter.convertToDDS(image).array());
			player.sendPacket(packet);
		}
		catch (Exception e)
		{
			_log.warning("Imagen Generator Error->" + e.getMessage());
		}
	}
	
	public static _ImageGenerator getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final _ImageGenerator _instance = new _ImageGenerator();
	}
}