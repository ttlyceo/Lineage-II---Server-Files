package ZeuS.tutorial;

import java.util.logging.Logger;

import ZeuS.Comunidad.sendC;
import ZeuS.Config.general;
import ZeuS.Config.levelupspot;
import ZeuS.interfase.central;
import ZeuS.interfase.cumulativeSubclass;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.imagen._ImageGenerator;
import ZeuS.server.comun;
import l2r.Config;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class levelup {
	@SuppressWarnings("unused")
	private static String CLOSE_WINDOWS_ACTION = "link COXX";
	
	public static final Logger _log = Logger.getLogger(levelup.class.getName());
	
	public static void bypass(L2PcInstance player, String params){
		String parm[] = params.split(";");
		String Parametro = parm[1];
		String Par1 = parm[2];
		String Par2 = parm[3];
		String Par3 = parm[4];
		String Par4 = parm[5];
		if(Parametro.equals("TELEPORTME")){
			int x = Integer.valueOf(Par1);
			int y = Integer.valueOf(Par2);
			int z = Integer.valueOf(Par3);
			player.teleToLocation(x, y, z, true);
			central.msgbox("You have been teleport to " + Par4.replace("_", " "), player);
			player.sendPacket(new sendC());
		}
	}
	
	public static void getLevelUpSpotWindows(L2PcInstance player){
		
		try {
			cumulativeSubclass.getInstance().checkSkillByLevelUp(player);
		}catch(Exception a) {
			
		}
		
		if(!general.LEVEL_UP_SPOT){
			return;
		}
		if(player.getInstanceId()>0){
			return;
		}
		levelupspot T1 = general.getLevelUpSpotInfo(player.getLevel());
		if(T1==null){
			return;
		}
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + "/" + language.getInstance().getFolder(player) + "/" +"levelspot.html");
		_ImageGenerator.getInstance().generateImage(player, T1.getImagen());
		
		String ByPassTeleport = "link zeusLS;TELEPORTME;"+String.valueOf(T1.getX())+";"+ String.valueOf(T1.getY())+";"+ String.valueOf(T1.getZ()) + ";" + T1.getName();
		String BtnTeleportMe = "<button value=\"Let me Kill Some Monsters\" action=\""+ ByPassTeleport +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Back\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\">";
		
		String imageLogo = opera.getImageLogo(player);
		String ImagenMonster = "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + String.valueOf(T1.getImagen()) + "\" width=256 height=256>";		
		html.replace("%SERVER_LOGO%", imageLogo);
		html.replace("%IMAGE_MONSTER_BY_LEVEL%", ImagenMonster);
		html.replace("%BTN_TELEPORT%", BtnTeleportMe);
		central.sendHtml(player, html.getHtml(), true);
	}
}
