package ZeuS.Comunidad.EngineForm;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.server.comun;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class C_cmdInfo {
	
	private static Vector<String>cmd_char = new Vector<String>();
	private static Vector<String>cmd_ZeuS_Adm = new Vector<String>();
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(C_cmdInfo.class.getName());
	
	@SuppressWarnings("unchecked")
	private static void getAllCommands(L2PcInstance player){
		cmd_ZeuS_Adm = language.getInstance().getAdmCmd(player);
		cmd_char = language.getInstance().getUserCmd(player);
		if(cmd_ZeuS_Adm==null || cmd_ZeuS_Adm.size()==0){
		}
		if(cmd_char==null || cmd_char.size()==0){
			
		}
	}
	
	private static Vector<String> getAllCommandToShow(L2PcInstance player){
		Vector<String> T = new Vector<String>();
		Comparator<String> NameAZ = (p1,p2) -> p1.compareToIgnoreCase(p2);
		Collections.sort(cmd_ZeuS_Adm, NameAZ);
		Collections.sort(cmd_char, NameAZ);
		if(player.isGM()){
			for(String g : cmd_ZeuS_Adm){
				T.add(g);
			}
		}
		
		for(String CM : cmd_char){
			T.add(CM);
		}
		

		return T;
	}
	
	private static NpcHtmlMessage getAllCommand(L2PcInstance player, int pagina, NpcHtmlMessage html){
		getAllCommands(player);
		
		int maximo = 15;
		int desde = pagina * maximo;
		int hasta = desde + maximo;
		int contador = 0;
		boolean haveNext = false;
		int Fila = 1;
		for(String t : getAllCommandToShow(player)){
			if(contador >= desde && contador < hasta){
				String NameWithLink = "";
				if(t.split(":")[0].startsWith(".")){
					NameWithLink = "<a action=\"bypass -h voice "+ t.split(":")[0] +"\">"+ t.split(":")[0] +"</a>";
				}else{
					NameWithLink = t.split(":")[0];
				}
				
				html.replace("%NAME_"+ String.valueOf(Fila) +"%", NameWithLink);
				html.replace("%DOTS_"+ String.valueOf(Fila) +"%", ":");
				html.replace("%DESCRIP_"+ String.valueOf(Fila) +"%", t.split(":")[1]);
				Fila++;
			}else if(contador > hasta){
				haveNext = true;
			}
			contador++;
		}
		
		if(Fila < 15){
			for(int i=Fila;i<=maximo;i++){
				html.replace("%NAME_"+ String.valueOf(i) +"%", "");
				html.replace("%DOTS_"+ String.valueOf(i) +"%", "");
				html.replace("%DESCRIP_"+ String.valueOf(i) +"%", "");				
			}
		}
		
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.commandinfo.name() + ";0;"+ String.valueOf(pagina - 1) +";0;0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.commandinfo.name() + ";0;"+ String.valueOf(pagina + 1) +";0;0;0;0";
		
		String btnAntes = "<button  action=\""+ bypassAntes +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
		String btnProx = "<button  action=\""+ bypassProx +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
		
		String pagControl = "<br><table width=730 border=0 cellpadding=1><tr>"+
           "<td fixwidth=315 align=RIGHT>"+ (pagina > 0 ? btnAntes : "") +"</td>"+
           "<td fixwidth=100 align=CENTER><font color=04B4AE name=hs12>"+ String.valueOf(pagina + 1) +"</font></td>"+
           "<td fixwidth=315 align=LEFT>"+ (haveNext ? btnProx : "") +"</td></tr></table>";

		html.replace("%PAG_CONTROL%", pagControl);
		
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, int Pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-commands.htm");
		html = getAllCommand(player, Pagina, html);
		html.replace("%BYPASS%",general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		
		return html.getHtml();
	}
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,Integer.valueOf(parm2));
		}
		return "";
	}
}
