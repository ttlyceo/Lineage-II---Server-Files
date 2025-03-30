package ZeuS.Comunidad.EngineForm;

import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.Engine;
import ZeuS.Config._classesinfo;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class v_clasesStadistic {
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(v_clasesStadistic.class.getName());
	
	private static String WidthxPorcentaje(int idClase, boolean porcent, int widhFormat){
		int MaximoWidth = (porcent ? 100 : widhFormat);
		int Cantidad = 0;
		String Race = general.getClassInfo(idClase).getRaceName();

		int UniversoPlayer = 0;
		
		if(general.getRaceCount().containsKey(Race)){
			UniversoPlayer = general.getRaceCount().get(Race);
		}
		
		try{
			Cantidad = general.getClassInfo(idClase).getClassCount();
		}catch(Exception a){

		}
		
		int NuevoWidth = ( UniversoPlayer>0 ? Math.round(( MaximoWidth * Cantidad ) / UniversoPlayer) : 0);
		
		return String.valueOf(NuevoWidth);
		
	}
	
	@SuppressWarnings("rawtypes")
	private static NpcHtmlMessage getAllClases(String strRace, L2PcInstance player, int Page){
		//String []Clases = {"Human:icon.skillhuman","Elf:icon.skillelf","Dark Elf:icon.skilldarkelf","Orc:icon.skillorc","Dwarf:icon.skilldwarf","Kamael:icon.skillkamael"};
		int maxCount = 16;
		int SearchFrom = Page * maxCount;
		int SearchTo = SearchFrom + maxCount;
		
		boolean haveNext = false;
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/index-class-stadistic.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_PART_EXEC());
		
		String[] BackGroundBar = opera.getGridFormatFromHtml(html, 2, "").split(",");
		String[] ActiveBar = opera.getGridFormatFromHtml(html, 3, "").split(",");
		int widthBar = Integer.valueOf(opera.getGridFormatFromHtml(html, 1, ""));
		String ClassGrid = opera.getGridFormatFromHtml(html, 4, "%CLASS_GRID%");
		
		String ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.ClasesStadistic.name() + ";0;%RACE%;0;0;0;0";
		
		html.replace("%HUMAN_SHOW_BYPASS%", ByPass.replace("%RACE%", "Human"));
		html.replace("%ELF_SHOW_BYPASS%", ByPass.replace("%RACE%", "Elf"));
		html.replace("%DARK_ELF_SHOW_BYPASS%", ByPass.replace("%RACE%", "Dark Elf"));
		html.replace("%ORC_SHOW_BYPASS%", ByPass.replace("%RACE%", "Orc"));
		html.replace("%DWARF_SHOW_BYPASS%", ByPass.replace("%RACE%", "Dwarf"));
		html.replace("%KAMAEL_SHOW_BYPASS%", ByPass.replace("%RACE%", "Kamael"));
		int RasesC = 0;
		if(general.getRaceCount().containsKey("Human")){
			RasesC = general.getRaceCount().get("Human");
		}
		html.replace("%HUMAN_COUNT%", String.valueOf(RasesC));
		html.replace("%HUMAN_PORCENT%", String.valueOf( RasesC > 0 ? Math.round(( 100 * RasesC ) / general._getCharInfo().size()) : 0 )  + "%");
		RasesC = 0;
		if(general.getRaceCount().containsKey("Elf")){
			RasesC = general.getRaceCount().get("Elf");
		}		
		html.replace("%ELF_COUNT%", String.valueOf(RasesC));		
		html.replace("%ELF_PORCENT%", String.valueOf( RasesC > 0 ? Math.round(( 100 * RasesC ) / general._getCharInfo().size()) : 0 )  + "%");
		RasesC = 0;
		if(general.getRaceCount().containsKey("Dark Elf")){
			RasesC = general.getRaceCount().get("Dark Elf");
		}		
		html.replace("%DARK_ELF_COUNT%", String.valueOf(RasesC));
		html.replace("%DARK_ELF_PORCENT%", String.valueOf( RasesC > 0 ? Math.round(( 100 * RasesC ) / general._getCharInfo().size()) : 0 )  + "%");
		RasesC = 0;
		if(general.getRaceCount().containsKey("Orc")){
			RasesC = general.getRaceCount().get("Orc");
		}		
		html.replace("%ORC_COUNT%", String.valueOf(RasesC));		
		html.replace("%ORC_PORCENT%", String.valueOf( RasesC > 0 ? Math.round(( 100 * RasesC ) / general._getCharInfo().size()) : 0 )  + "%");
		RasesC = 0;
		if(general.getRaceCount().containsKey("Dwarf")){
			RasesC = general.getRaceCount().get("Dwarf");
		}		
		html.replace("%DWARF_COUNT%", String.valueOf(RasesC));
		html.replace("%DWARF_PORCENT%", String.valueOf( RasesC > 0 ? Math.round(( 100 * RasesC ) / general._getCharInfo().size()) : 0 )  + "%");
		RasesC = 0;
		if(general.getRaceCount().containsKey("Kamael")){
			RasesC = general.getRaceCount().get("Kamael");
		}		
		html.replace("%KAMAEL_COUNT%", String.valueOf(RasesC));
		html.replace("%KAMEL_PORCENT%", String.valueOf( RasesC > 0 ? Math.round(( 100 * RasesC ) / general._getCharInfo().size()) : 0 )  + "%");
		
		switch(strRace){
			case "Human":
				html.replace("%HUMAN_FONT_COLOR%", "0080FF");
				break;
			case "Elf":
				html.replace("%ELF_FONT_COLOR%", "0080FF");
				break;
			case "Dark Elf":
				html.replace("%DARK_ELF_FONT_COLOR%", "0080FF");
				break;
			case "Orc":
				html.replace("%ORC_FONT_COLOR%", "0080FF");
				break;
			case "Dwarf":
				html.replace("%DWARF_FONT_COLOR%", "0080FF");
				break;
			case "Kamael":
				html.replace("%KAMAEL_FONT_COLOR%", "0080FF");
				break;
		}
		
		html.replace("%HUMAN_FONT_COLOR%", "");
		html.replace("%ELF_FONT_COLOR%", "");
		html.replace("%DARK_ELF_FONT_COLOR%", "");
		html.replace("%ORC_FONT_COLOR%", "");
		html.replace("%DWARF_FONT_COLOR%", "");
		html.replace("%KAMAEL_FONT_COLOR%", "");
		html.replace("%ETHERIA_FONT_COLOR%", "");
		
		Iterator itr = general.getClassInfo().entrySet().iterator();
		String DataClass = "";
		
		int contador = 0;
		int ContBarra = 0;
		int contadorFilas = 0;
		
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int idClase = (int) Entrada.getKey();
	    	_classesinfo T1 = (_classesinfo)Entrada.getValue();
	    	if(T1.getRaceName().equalsIgnoreCase(strRace)){
	        	if(contadorFilas >= SearchFrom && contadorFilas < SearchTo){	    		
		    		if(contador==0){
		    			DataClass += "<tr>";
			    	}
		    		String WDTH = WidthxPorcentaje(idClase, false,widthBar);
		    		String Porcent = WidthxPorcentaje(idClase, true,widthBar);
		    		DataClass += ClassGrid.replace("%CLASS_WIDTH_BAR%", WDTH) .replace("%CLASS_COLOR_BAR%", ActiveBar[ContBarra]) .replace("%CLASS_BACKGROUND%", BackGroundBar[ContBarra]) .replace("%CLASS_PORCENT%", Porcent) .replace("%CLASS_NAME%", opera.getClassName(idClase));
			    	ContBarra++;
			    	contador++;
			    	if(contador>=4){
			    		DataClass += "</tr>";
			    		contador =0;
			    	}
			    	if(ContBarra >= ActiveBar.length){
			    		ContBarra=0;
			    	}
			    	contadorFilas++;
	        	}else if(contadorFilas > SearchTo){
	        		haveNext = true;
	        		break;
	        	}else{
	        		contadorFilas++;
	        	}
	    	}
	    	
	    }//end while
	    
	    if(contador>0 && contador <4){
	    	for(int i=contador;i<4;i++){
	    		DataClass += "<td></td>";		    		
	    	}
	    	DataClass +="</tr>";
	    }
	    
	    String ByPassControl = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +";" + Engine.enumBypass.ClasesStadistic.name() + ";0;%RACE%;%PAGE%;0;0;0";
	    String PrevBtn = ByPassControl.replace("%RACE%", strRace).replace("%PAGE%", String.valueOf( Page - 1 )) ;
	    String NextBtn = ByPassControl.replace("%RACE%", strRace).replace("%PAGE%", String.valueOf( Page + 1 )) ;
	    html.replace("%CLASS_GRID%", DataClass);
	    html.replace("%BYPASS_PREV%", ( Page == 0 ? "" : PrevBtn ));
	    html.replace("%PAGE_NUMBER%", String.valueOf(Page + 1));
	    html.replace("%BYPASS_NEXT%", ( haveNext ? NextBtn : "" ) );
		return html;
	}
	
	private static String mainHtml(L2PcInstance player, String Params, String PalabraBuscar, int Page){
		return getAllClases(PalabraBuscar, player, Page).getHtml();
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		if(parm1.equals("0")){
			return mainHtml(player,parm1,parm2, Integer.valueOf(parm3));
		}
		return "";
	}
}
