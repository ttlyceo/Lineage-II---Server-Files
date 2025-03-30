package ZeuS.interfase;

import java.util.logging.Logger;

import l2r.gameserver.GameServer;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.EngineForm.v_MiscelaniusOption.loadByPass;
import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

public class delevel {

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(delevel.class.getName());

	public static String htmlDelevel(L2PcInstance player){
		String imgLogo = opera.getImageLogo(player);
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.charclanoption.name() + ";"+ loadByPass.Delevel.name() +";APPLY;%LEVEL%;0;0;0;0";

		String Botones = "";
		String BOTON_DISMINUIR = "<center><button value=\"Delevel to %LEVEL%\" action=\""+ ByPass + "\" width=170 height=35 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		
		if(player.getLevel() > 82){
			Botones += BOTON_DISMINUIR.replace("%LEVEL%", String.valueOf(82));
		}
		for(int i=80;i>=general.DELEVEL_LVL_MAX;i-=10){
			if(player.getLevel()>i){
				Botones += BOTON_DISMINUIR.replace("%LEVEL%", String.valueOf(i));
			}
		}
		
		String htmltext = "<html><title>" + general.TITULO_NPC() + "</title><body><center>" + imgLogo + "<br>";
		htmltext += central.LineaDivisora(2) + central.headFormat("DELEVEL MANAGER") + central.LineaDivisora(2);
		htmltext += central.LineaDivisora(2) + central.headFormat( Botones ) + central.LineaDivisora(2);
		htmltext += central.getPieHTML() + "</center></body></html>";
		return htmltext;
	}

}
