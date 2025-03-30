package ZeuS.Comunidad.EngineForm;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.interfase.transform;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_Transformations extends transform{
	
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(v_Transformations.class.getName());
	
	private static String mainHtml(L2PcInstance player, String Tipo, int Pagina){
		return getTransformationAll(player, Pagina, Tipo);
	}
	
	private static String getTransformationAll(L2PcInstance player, int pagina, String tipo) {
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-transformation-manager.htm");
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		if(tipo.equals("0")){
			tipo = "Special";
		}
		
		String ByPassMenu = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Transformation.name() + ";list;%TIPO%;0;0;0;0";
		
		String ColorSel = opera.getGridFormatFromHtml(html, 2, "");
		ColorSel = ColorSel.split(":")[1].trim();
		String ColorNoSel = opera.getGridFormatFromHtml(html, 1, "");
		ColorNoSel = ColorNoSel.split(":")[1].trim();
		
		String ByPassShowPrice_Special = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Transformation.name() + ";showPriceSpecial;0;0;0;0;0;0";
		String ByPassShowPrice_RaidBoss = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Transformation.name() + ";showPriceRaidBoss;0;0;0;0;0;0";
		String ByPassShowPrice_Normal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Transformation.name() + ";showPriceNormal;0;0;0;0;0;0";
		String ByPassDispell = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Transformation.name() + ";dispell;0;0;0;0;0;0";
		
		html.replace("%BYPASS_DISPELL_TRANSFORMATION%", ByPassDispell);
		
		html.replace("%BYPASS_SPECIAL%", ByPassMenu.replace("%TIPO%", "Special") );
		html.replace("%BYPASS_RAIDBOSS%", ByPassMenu.replace("%TIPO%", "RaidBoss") );
		html.replace("%BYPASS_NORMAL%", ByPassMenu.replace("%TIPO%", "Normal") );
		
		switch(tipo) {
			case "Special":
				html.replace("%FONT_COLOR_SPECIAL%", ColorSel);
				break;
			case "RaidBoss":
				html.replace("%FONT_COLOR_RAIDBOSS%", ColorSel);
				break;
			case "Normal":
				html.replace("%FONT_COLOR_NORMAL%", ColorSel);
				break;
		}
		
		html.replace("%FONT_COLOR_SPECIAL%", ColorNoSel);
		html.replace("%FONT_COLOR_RAIDBOSS%", ColorNoSel);
		html.replace("%FONT_COLOR_NORMAL%", ColorNoSel);
		
		html.replace("%BYPASS_SPECIAL_PRICE%", ByPassShowPrice_Special);
		html.replace("%BYPASS_RAIDBOSS_PRICE%", ByPassShowPrice_RaidBoss);
		html.replace("%BYPASS_NORMAL_PRICE%", ByPassShowPrice_Normal);
		
		String []StrTransforSelect = {};
		
		String GridFormat = "";
		
		if(tipo.equals("Special")){
			StrTransforSelect = Transformaciones_especiales;
			GridFormat = opera.getGridFormatFromHtml(html, 1, "%DATA_GRID%");
			GridFormat = opera.getGridFormatFromHtml(html, 2, "");
			GridFormat = opera.getGridFormatFromHtml(html, 3, "");
		}else if(tipo.equals("RaidBoss")){
			StrTransforSelect = Transformaciones_RaidBoss;
			GridFormat = opera.getGridFormatFromHtml(html, 1, "");
			GridFormat = opera.getGridFormatFromHtml(html, 2, "%DATA_GRID%");
			GridFormat = opera.getGridFormatFromHtml(html, 3, "");
		}else if(tipo.equals("Normal")){
			StrTransforSelect = Transformaciones_Varias;
			GridFormat = opera.getGridFormatFromHtml(html, 1, "");
			GridFormat = opera.getGridFormatFromHtml(html, 2, "");
			GridFormat = opera.getGridFormatFromHtml(html, 3, "%DATA_GRID%");			
		}

		int Maximo = 12;
		int Desde = pagina * Maximo;
		int Hasta = Desde + Maximo;
		
		int Contador = 0;
		int ContadorTR = 0;
		boolean haveNext = false;
		String _retorno = "";
		for(String pT : StrTransforSelect){
			if(Contador >= Desde && Contador < Hasta){
    			if(ContadorTR == 0){
    				_retorno += "<tr>";
    			}
    			String _ByPass = "bypass "+general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Transformation.name() + ";setTrans;"+tipo+";"+ String.valueOf(pT.split(",")[1]) +";"+ String.valueOf(pagina) +";0;0";
    			_retorno += GridFormat.replace("%TRANSFORMATION_NAME%", pT.split(",")[0]) .replace("%BYPASS_SELECT_TRANSFORM%", _ByPass);
    			ContadorTR++;
    			if(ContadorTR>=3){
    				_retorno += "</tr>";
    				ContadorTR=0;
    			}
    		}else if(Contador > Hasta){
    			haveNext = true;
    		}
			Contador++;			
		}
		
		
		if(ContadorTR>0 && ContadorTR<3){
			for(int i=ContadorTR;i<3;i++){
				_retorno += GridFormat.replace("%TRANSFORMATION_NAME%", "NONE") .replace("%BYPASS_SELECT_TRANSFORM%", "");
			}
			_retorno += "</tr>";
		}
		
		html.replace("%DATA_GRID%", _retorno);
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.Transformation.name() + ";list;" + tipo + ";" +String.valueOf(pagina-1)+";0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.Transformation.name() + ";list;" + tipo + ";" +String.valueOf(pagina+1)+";0;0;0";
			
		html.replace("%PAGE%", String.valueOf(pagina+1));
		html.replace("%BYPASS_PREV%", ( pagina>0 ? bypassAntes : "" ));
		html.replace("%BYPASS_NEXT%", ( haveNext ? bypassProx : "" ));
		
		return html.getHtml();
	}

	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_TRANSFORMACION) {
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
		
		Map<String, String> Precios = new HashMap<String, String>();
		
		if(parm1.equals("0")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("list")){
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}else if(parm1.equals("setTrans")){
			
			if(player.isTransformed() || player.isTransformarCHAR()) {
				central.msgbox("You need to dispell you transformation status firts.", player);
				return mainHtml(player, parm2, Integer.valueOf(parm4));
			}
			
			int idTransForm = Integer.valueOf(parm3);
			int Pagina = Integer.valueOf(parm4);
			
			String IdItemPedir = "";
			if(parm2.equals("Special")){
				if(!general.TRANSFORMATION_ESPECIALES){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,parm2,Pagina);
				}
				IdItemPedir = general.TRANSFORMATION_ESPECIALES_PRICE;
			}else if(parm2.equals("RaidBoss")){
				if(!general.TRANSFORMATION_RAIDBOSS){
					central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
					return mainHtml(player,parm2,Pagina);
				}
				IdItemPedir = general.TRANSFORMATION_RAIDBOSS_PRICE;
			}else if(parm2.equals("Normal")){
				IdItemPedir = general.TRANSFORMATION_PRICE;
			}
			
			if(!opera.haveItem(player, IdItemPedir)){
				return mainHtml(player,parm2,Pagina);
			}
			
			if(transformPlayer(player, idTransForm)){
				opera.removeItem(IdItemPedir, player);
			}
			return mainHtml(player,parm2,Pagina);//mainHtml(player,parm2,Integer.valueOf(parm4));
		}else if(parm1.equals("showPriceSpecial")){
			Precios.put("Cost", general.TRANSFORMATION_ESPECIALES_PRICE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.TRANSFORMATION_LVL) + ";Need Noble:" + ( general.TRANSFORMATION_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Special Transformation");
		}else if(parm1.equals("showPriceRaidBoss")){
			Precios.put("Cost", general.TRANSFORMATION_RAIDBOSS_PRICE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.TRANSFORMATION_LVL) + ";Need Noble:" + ( general.TRANSFORMATION_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Raid Boss Transformation");			
		}else if(parm1.equals("showPriceNormal")){
			Precios.put("Cost", general.TRANSFORMATION_PRICE);
			String Requerimientos = "Min Level Request:" + String.valueOf(general.TRANSFORMATION_LVL) + ";Need Noble:" + ( general.TRANSFORMATION_NOBLE ? "Yes" : "No" );
			Precios.put("Requirements", Requerimientos);
			cbFormato.showItemRequestWindows(player, Precios, "Normal Transformation");			
		}else if(parm1.equals("dispell")){
			transform.untransform(player);
			return mainHtml(player,parm2,Integer.valueOf(parm3));
		}
		return "";
	}
}
