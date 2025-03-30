package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;

public class v_HeroList {

	@SuppressWarnings({ "unused", "rawtypes" })
	private static String getAllClases(L2PcInstance player, String strRace){
		//String []Clases = {"Human:icon.skillhuman","Elf:icon.skillelf","Dark Elf:icon.skilldarkelf","Orc:icon.skillorc","Dwarf:icon.skilldwarf","Kamael:icon.skillkamael"};
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-herolist.htm");
		String DataExtra = opera.getGridFormatFromHtml(html, 1, "");
		
		String IconFirts = DataExtra.split(",")[0].split(":")[1];
		String FontColorFirts = DataExtra.split(",")[1].split(":")[1];
		String FontColorOther = DataExtra.split(",")[2].split(":")[1];
		
		String DataGridFormat = opera.getGridFormatFromHtml(html, 2, "%HERO_DATA%");
		
		int contador = 0;
		int ContBarra = 0;
	
		String btnMaestro = "<button value=\"%NOMBRE%\" width=%TAM% height=16 action=\"\" back=L2UI_CH3.HorizontalSliderBarCenter fore=L2UI_CH3.HorizontalSliderBarCenter>";

		String btnPvP = btnMaestro.replace("%NOMBRE%", "PvP").replace("%TAM%", "58");
		String btnPk = btnMaestro.replace("%NOMBRE%", "Pk").replace("%TAM%", "58");
		String retorno = "";
		String TipoConecion = "";
		if(general.getPlayerHeroes(strRace)!=null){
			Iterator itr = general.getPlayerHeroes(strRace).entrySet().iterator();
			
			int ContFila=0;
			String Colores[] = {"232323","141313"};
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	int idClase = (int) Entrada.getKey();
		    	Map<Integer, String> Base = new HashMap<Integer, String>();
		    	Base = general.getPlayerHeroes(strRace).get(idClase);
				L2PcInstance playerBus = null;
				try{
					try{
						playerBus = opera.getPlayerbyName(Base.get(1));
						if(playerBus == null){
							TipoConecion = "<font color=676767>Offline</font>";
						}else{
							if(playerBus.getClient().isDetached()){
								if(playerBus.isInCraftMode()){
									TipoConecion = "<font color=676767>Off. Cr</font>";
								}else if(playerBus.isInStoreMode()){
									TipoConecion = "<font color=676767>Off. St</font>";;
								}
							}else{
								TipoConecion =  "<font color=5EFDA6>Online</font>";
							}
						}
					}catch(Exception a){
						TipoConecion = "<font color=676767>Offline</font>";
					}
					String ByPassInfo = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.HeroList.name()+ ";showcharinfo;" + strRace + ";"+Base.get(1)+";0;0;0";
					retorno += DataGridFormat.replace("%BG_COLOR%", Colores[ContFila % 2]);
					retorno = retorno.replace("%PLACE_DATA%", (ContFila==0 ? IconFirts : "<font color="+ FontColorOther +">"+ String.valueOf(ContFila + 1)) +"</font>");
					retorno = retorno.replace("%HERO_NAME%", (ContFila==0 ? "<font name=\"hs12\" color="+ FontColorFirts +">"+ Base.get(1) + "</font>" : "<font color="+ FontColorOther +">"+ Base.get(1) + "</font>"));
					retorno = retorno.replace("%HERO_LEVEL%", String.valueOf(Base.get(2)));
					retorno = retorno.replace("%HERO_PVPS%", Base.get(4));
					retorno = retorno.replace("%HERO_PKS%", Base.get(5));
					retorno = retorno.replace("%HERO_CLASS%", opera.getClassName(Integer.valueOf(Base.get(3))));
					retorno = retorno.replace("%HERO_ONLINE_STATUS%", TipoConecion);
					retorno = retorno.replace("%BYPASS_CHAR_INFO%", ByPassInfo);
				}catch(Exception a){
					
				}	    	
		    }
		}
		html.replace("%HUMAN%", ( strRace.equals("Human") ? "color=0080FF" : "" ));
		html.replace("%ELF%", ( strRace.equals("Elf") ? "color=0080FF" : "" ));
		html.replace("%DARK_ELF%", ( strRace.equals("Dark Elf") ? "color=0080FF" : "" ));
		html.replace("%ORC%", ( strRace.equals("Orc") ? "color=0080FF" : "" ));
		html.replace("%DWARF%", ( strRace.equals("Dwarf") ? "color=0080FF" : "" ));
		html.replace("%KAMAEL%", ( strRace.equals("Kamael") ? "color=0080FF" : "" ));
		html.replace("%HERO_DATA%", retorno);

		return html.getHtml();
	}
	
	private static String mainHtml(L2PcInstance player, String Params, String PalabraBuscar){
		String retorno = getAllClases(player, PalabraBuscar);
		return retorno;
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
			return mainHtml(player,parm1,parm2);
		}else if(parm1.equals("showcharinfo")){
			opera.getInfoPlayer(parm3, player);
			return mainHtml(player,"0",parm2);
		}
		return "";
	}
}
