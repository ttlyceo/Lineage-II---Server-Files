package ZeuS.Comunidad.EngineForm;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Comunidad.CBByPass;
import ZeuS.Comunidad.cbManager;
import ZeuS.Comunidad.sendC;
import ZeuS.Comunidad.sendH;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class cbFormato {
	public static String getBotonForm(String imagen, String ByPass){
		String BtnGrilla = "<table width=34 height=33 background=\""+imagen+"\" border=0 cellspacing=0 cellpadding=-2><tr>"+
        "<td><button action=\""+ByPass+"\" back=L2UI_CT1.ItemWindow_DF_Frame_Down fore=L2UI_CT1.ItemWindow_DF_Frame width=\"35\" height=\"34\"></td></tr></table>";
		return BtnGrilla;
	}
	
	public static void cerrarCB(L2PcInstance player){
		player.sendPacket(new sendH());
	}
	
	public static void cerrarTutorial(L2PcInstance player){
		player.sendPacket(new sendC());
	}
	
	
	
	
	@SuppressWarnings("rawtypes")
	protected static void showItemRequestWindows(L2PcInstance player, Map<String, String> Request, String Titulo){
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=CENTER>"+
		"<font color=70FFB3>"+ Titulo + " Item Request</font></td></tr></table><br>";
		
		Iterator itr = Request.entrySet().iterator();
		
		String cHave = "6FFF9D";
		String cNoHave = "FF6F6F";
		String []BGColor = {"1C1C1C","353535"};
		int Contador = 0;		
		
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT>"+
	    			"<font name=hs12 color=8BECFF>"+ (String)Entrada.getKey() +"</font></td></tr></table>";
	    	
	    	HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
	    	
	    	String ItemServerRequest = (String) Entrada.getValue();
	    	
			if(ItemServerRequest.length()>0){
				//3470,10;57,1
				for(String RequestItem : ItemServerRequest.split(";")){
					if(RequestItem.indexOf(",")>=0){
						String Nombre = central.getNombreITEMbyID(Integer.valueOf(RequestItem.split(",")[0]));
						String Cantidad = RequestItem.split(",")[1];
						boolean HaveRequest = opera.haveItem(player, Integer.valueOf(RequestItem.split(",")[0]), Integer.valueOf(Cantidad), false);
						HTML += "<table with=270 border=0 bgcolor="+ BGColor[Contador++ % 2] +"><tr><td fixwidth=270 align=LEFT>"+
		                "<font color="+ ( HaveRequest ? cHave : cNoHave ) +">"+ Nombre + "("+ opera.getFormatNumbers(Cantidad) +")" +"</font><br></td></tr></table>";
					}else if(RequestItem.indexOf(":")>=0){
						String Nombre = RequestItem.split(":")[0];
						String estado = RequestItem.split(":")[1];
						HTML += "<table with=270 border=0 bgcolor="+ BGColor[Contador++ % 2] +"><tr><td fixwidth=270 align=LEFT>"+
		                "<font color="+ cHave + ">"+ Nombre + " : " + estado + "</font><br></td></tr></table>";						
					}
				}
			}else{
				HTML += "<table with=270 border=0 bgcolor=1C1C1C><tr><td fixwidth=270 align= CENTER><font color=FFFABB name=hs12>No Item's</font><br1></td></tr></table>";
			}	    	
	    	
			HTML += "</td></tr></table><br>";
			
	    }
	    HTML += central.getPieHTML(false) + "</body></html>";
	    central.sendHtml(player, HTML);
	}
	
	
	
	protected static void showItemRequestWindows(L2PcInstance player, String ItemServerRequest, String ItemDonationRequest, String Titulo){
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=CENTER>"+
		"<font color=70FFB3>"+ Titulo + " Item Request</font></td></tr></table>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT>"+
		"<font name=hs12 color=8BECFF>Server Item's</font></td></tr></table>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		
		String cHave = "6FFF9D";
		String cNoHave = "FF6F6F";
		String []BGColor = {"1C1C1C","353535"};
		int Contador = 0;
		
		if(ItemServerRequest.length()>0){
			//3470,10;57,1
			for(String Request : ItemServerRequest.split(";")){
				String Nombre = central.getNombreITEMbyID(Integer.valueOf(Request.split(",")[0]));
				String Cantidad = Request.split(",")[1];
				boolean HaveRequest = opera.haveItem(player, Integer.valueOf(Request.split(",")[0]), Integer.valueOf(Cantidad), false);
				
				HTML += "<table with=270 border=0 bgcolor="+ BGColor[Contador++ % 2] +"><tr><td fixwidth=270 align=LEFT>"+
                "<font color="+ ( HaveRequest ? cHave : cNoHave ) +">"+ Nombre + "("+ opera.getFormatNumbers(Cantidad) +")" +"</font><br1></td></tr></table>";
			}
			
		}else{
			HTML += "<table with=270 border=0 bgcolor=1C1C1C><tr><td fixwidth=270 align= CENTER><font color=FFFABB name=hs12>No Item's</font><br1></td></tr></table>";
		}
		
		HTML += "<br></td></tr></table>";
		
		HTML += "<br><table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270 align=RIGHT><font name=hs12 color=8BECFF>Donation Item's</font></td></tr></table>";
		
		HTML += "<table width=270 background=L2UI_CT1.Windows_DF_TooltipBG><tr><td fixwidth=270>";
		
		if(ItemDonationRequest.length()==0 || ItemDonationRequest.equals("0")){
            HTML += "<table with=270 border=0 bgcolor=1C1C1C><tr><td fixwidth=270 align= CENTER><font color=FFFABB name=hs12>No Item's</font><br1></td></tr></table><br>";			
		}else{
			String Nombre = central.getNombreITEMbyID(general.DONA_ID_ITEM);
			boolean HaveRequest = opera.haveItem(player, general.DONA_ID_ITEM , Integer.valueOf(ItemDonationRequest), false);
			HTML += "<table with=270 border=0 bgcolor="+ BGColor[Contador++ % 2] +"><tr><td fixwidth=270 align=LEFT>"+
	                "<font color="+ ( HaveRequest ? cHave : cNoHave ) +">"+ Nombre + "("+ opera.getFormatNumbers(ItemDonationRequest) +")" +"</font><br1></td></tr></table><br>";			
		}
		
		HTML += "<br></td></tr></table>";
		
		HTML +="</body></html>";
		central.sendHtml(player, HTML);
	}

	public static String getCostOfService(String Valores){
		String retorno = "<table width=220 cellspacing=1 cellpadding=1 bgcolor=1C1C1C><tr><td height=25><center><font color=\"339966\">The cost of this service</font></center></td></tr></table><br>";

		if(Valores.length()>0){
			retorno += "<table width=220 fixwidth=220 cellspacing=0 cellpadding=2>";
			for(String p1 : Valores.split(";")){
				String Item = p1.split(",")[0], Cantidad = p1.split(",")[1];
				retorno += "<tr><td align=CENTER>"+ opera.getFormatNumbers(Cantidad) +" of " + central.getNombreITEMbyID(Integer.valueOf(Item)) + "</td></tr>"; 
			}
			retorno += "</table><br><br>";
		}
		return retorno;
	}
	
	public static String getRequirements(Vector<String> Requerimientos){
		String retorno ="<table width=220 cellspacing=1 cellpadding=1 bgcolor=1C1C1C><tr><td height=25><center><font color=\"339966\">Requirements</font></center></td></tr></table><br>";
		if(Requerimientos.size() >0){
			retorno += "<table width=220 fixwidth=220 cellspacing=0 cellpadding=2>";
			for(String p1 : Requerimientos){
				String Reque = p1.split(",")[0], Valor = p1.split(",")[1];
				retorno += "<tr><td align=CENTER width = 160>"+ Reque + "</td><td align=LEFT width = 60><font color=\"FFCC99\">"+ Valor +"</font></td></tr>"; 
			}
			retorno += "</table><br><br>";
		}
		return retorno;
	}
	
	public static String getBarra1(String Icono, String Titulo, String Explica, String Bypass, String Btntitulo){
		String retorno = "<table width=700 background=\"L2UI_CT1.Windows_DF_TooltipBG\" height=50 cellpadding=5><tr><td width=32 alignt=CENTER>"+
                         "<img src=\""+ Icono +"\" width=32 height=32><br></td><td width=500><table width=500><tr><td align=\"LEFT\"><font name=\"hs12\" color=\"FFCC00\">"+ Titulo +"</font></td>"+
                         "</tr><tr><td><font color=\"99CCFF\">"+ Explica +"</font></td></tr></table></td><td width=\"168\" align=RIGHT>"+
                         "<button value=\""+ Btntitulo +"\" action=\""+ Bypass +"\" width=188 height=32 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td></tr></table>";
		return retorno;
	}
	
	public static String getBoxSearch(String Titulo, String Explica, String ByPass, String NametextBox, String ImaID){
		String retorno = "<table width=350 border=0><tr><td width=32><img src=\""+  ImaID +"\" width=32 height=32></td><td width=310><table width=310><tr><td>"+
		"<font name=\"hs12\" color=\"FA8258\">"+ Titulo +"</font></td></tr><tr><td>"+
		"<font>"+ Explica +"</font></td></tr></table></td><td><table><tr><td><edit var=\""+NametextBox+"\" width=150></td></tr><tr>"+
		"<td><button value=\"Search\" action=\""+ ByPass +"\" width=55 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></td></tr></table>";
		
		return retorno;
	}
	
	public static String getTituloEngine(){
		String retorno ="<center><br>" + cbManager._formTituloComunidad() + "</center>";
		return retorno;
	}
	
	public static String getTituloCentral(String Icono, String Nombre, String Explica,boolean showBtnBack){
		return getTituloCentral(Icono,  Nombre, Explica, "",true,"",showBtnBack);
	}
	
	
	public static String getTituloCentral(String Icono, String Nombre, String Explica, boolean VolverEngine, String ByPassVolverOtro){
		return getTituloCentral(Icono,  Nombre, Explica, "",VolverEngine,ByPassVolverOtro,true);
	}
	
	public static String getTituloCentral(String Icono, String Nombre, String Explica, String ByPassVolverAtras ){
		return getTituloCentral(Icono,  Nombre, Explica, ByPassVolverAtras,true,"",true);
	}
	
	public static String getTituloCentral(String Icono, String Nombre, String Explica,L2PcInstance player){
		//getTituloCentral(Icono, Nombre, Explica,false,"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC())
		try{
			if(CBByPass.IsFromMain.get(player.getObjectId())){
				return getTituloCentral(Icono, Nombre, Explica,false,"bypass " + general.getCOMMUNITY_BOARD_PART_EXEC());
			}
		}catch(Exception a){
			
		}
		return getTituloCentral(Icono,  Nombre, Explica, "",true,"",true);
	}
	
	
	public static String getTituloCentral(String Icono, String Nombre, String Explica, String ByPassVolverAtras, boolean VolverEngine, String ByPassVolverOtro, boolean ShowBackBtn ){
		String ByPassVolverInicio = "";
		if(VolverEngine){
			ByPassVolverInicio = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC();
		}else{
			ByPassVolverInicio= ByPassVolverOtro;
		}
		
		String retorno = "<img src=\"L2UI.SquareGray\" width=\"756\" height=\"2\"><table width=730 border=0 cellpadding=0><tr><td width=720 fixwidth=720><img src=\"L2UI.SquareGray\" width=744 height=2><table width=760 bgcolor=\"1F1F1F\" height=48>"+
						"<tr><td width=32><table width=32><tr><td width=32><img src=\""+ Icono + "\" width=32 height=32></td></tr></table></td><td width=644><table width=604 border=0><tr><td><table cellpadding=0 cellspacing=0 border=0><tr><td><font name=\"hs12\" color=\"FF9900\">"+ Nombre +"</font></td>"+
						"<td>"+(ShowBackBtn ? "<button action=\""+ ByPassVolverInicio +"\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_MinusBtn_Blue\" fore=\"L2UI_CT1.MiniMap_DF_MinusBtn_Blue\" value = \" \">" : "" ) + "</td></tr></table></td></tr><tr><td><font color=\"F8F9B7\">"+ Explica +"</font></td></tr></table></td><td width=64 align=\"RIGHT\">"+
						(ByPassVolverAtras.length()>0 ? "<button action=\""+ ByPassVolverAtras +"\" width=64 height=64 back=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\" fore=\"L2UI_CT1.ICON_DF_CHARACTERTURN_RIGHT\" value=\" \">" : "") + "</td></tr></table><img src=\"L2UI.SquareGray\" width=\"756\" height=2></td></tr></table><br>";
		return retorno;
	}
	
}
