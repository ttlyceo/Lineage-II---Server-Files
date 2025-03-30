package ZeuS.Comunidad.EngineForm;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.ExShowBaseAttributeCancelWindow;

public class v_RemoveAttribute {
	@SuppressWarnings("unused")
	private static String mainHtml(L2PcInstance player, String Params){
		String par = Engine.enumBypass.RemoveAttri.name();
		String retorno = "<html><body><center>" +cbFormato.getTituloEngine();
		String Icono = Engine.getIcono(par);
		String Explica = Engine.getExplica(par);
		String Nombre = Engine.getNom(par);
		
		String bypass_Draw = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + par + ";add;0,0;0;0;0";
		String bypass_Delete = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + par + ";remove;0,0;0;0;0";
		
		retorno += cbFormato.getTituloCentral(Icono, Nombre, Explica,player);
		
		retorno += "<table width=760 background=L2UI_CT1.Windows_DF_Drawer_Bg><tr><td></td></tr><tr><td width=760 align=center ><center>";
		
		retorno += cbFormato.getBarra1("icon.skill3631", "Augment Item", "Add more Power and become Stronger", bypass_Draw, "Augment Item") + "<br><br><br>";
		
		retorno += cbFormato.getBarra1("icon.skill3630", "Remove Augment", "Remove augment", bypass_Delete, "Remove Augment") + "<br><br>";
		
		retorno += "</td></tr></table></center>";				
		
		retorno += "</body></html>";
		return retorno;
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_REMOVE_ATRIBUTE) {
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
			player.sendPacket(new ExShowBaseAttributeCancelWindow(player));
			return "";
		}else if(parm1.equals("add")){
			
		}else if(parm1.equals("remove")){
			
		}
		return "";
	}
}
