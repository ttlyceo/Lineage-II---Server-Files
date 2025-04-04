package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.buffcommunity;
import ZeuS.Config.buffcommunitySchemme;
import ZeuS.Config.general;
import ZeuS.Config.premiumPersonalData;
import ZeuS.Config.premiumsystem;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;
import gr.sr.interf.SunriseEvents;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_Buffer_New {
	
	private static Map<Integer, String>TEMPORAL_SELECT_ICON = new HashMap<Integer, String>();
	private static Map<Integer, HashMap<Integer, SchemeChar>> SCHEME_P_PLAYER = new HashMap<Integer, HashMap<Integer, SchemeChar>>();
	private static Map<Integer, SchemeChar>SELECTED_SCHEME_PLAYER = new HashMap<Integer, SchemeChar>();
	private static Map<Integer, Boolean>isTargetChar = new HashMap<Integer, Boolean>();
	private static Logger _log = Logger.getLogger(v_Buffer_New.class.getName());
	private static Map<Integer, Integer> t_ID = new HashMap<Integer, Integer>();
	private static Vector<String> ICON_SKILL = new Vector<String>();
	protected static Map<Integer, _charBuffVariable> CHAR_SETTING = new HashMap<Integer, _charBuffVariable>();	
	
	protected static boolean canUseReuseSkill(L2PcInstance player, int idBuff, int reuseTime) {
		if(CHAR_SETTING != null ) {
			if(CHAR_SETTING.size()>0) {
				if(CHAR_SETTING.containsKey(player.getObjectId())) {
					if(!CHAR_SETTING.get(player.getObjectId()).canUseThisBuff(idBuff, reuseTime)) {
						central.msgbox(opera.getSkillName(idBuff, 1) +  " buff Reuse Time is not ready yet", player);
						return false;
					}
				}else {
					CHAR_SETTING.put(player.getObjectId(), new _charBuffVariable());
					CHAR_SETTING.get(player.getObjectId()).canUseThisBuff(idBuff, reuseTime);
				}
			}else {
				CHAR_SETTING.put(player.getObjectId(), new _charBuffVariable());
				CHAR_SETTING.get(player.getObjectId()).canUseThisBuff(idBuff, reuseTime);						
			}
		}else {
			CHAR_SETTING.put(player.getObjectId(), new _charBuffVariable());
			CHAR_SETTING.get(player.getObjectId()).canUseThisBuff(idBuff, reuseTime);					
		}		
		return true;
	}
	
	public static void setIcons(Vector<String> _Icons){
		try{
			ICON_SKILL.clear();
		}catch(Exception a){
			
		}
		ICON_SKILL = _Icons;
	}
	
	public static void removeAllFromPlayer(L2PcInstance player){
			try{
				Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buff_char_sch WHERE idChar=?");
				ins.setInt(1,player.getObjectId());
				try{
					ins.executeUpdate();
					try{
						SCHEME_P_PLAYER.remove(player.getObjectId());
					}catch(Exception a){
						
					}				
				}catch(SQLException e){
	
				}
				
			}catch(Exception a){
				
			}		
			try{
				Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buff_char_sch_buff WHERE idSch NOT IN ( SELECT id FROM zeus_buff_char_sch)");
				try{
					ins.executeUpdate();
				}catch(SQLException e){
	
				}
				
			}catch(Exception a){
				
			}		
		}	
	
	private static void getWindowsRemoveScheme(L2PcInstance player){
		//String ByPassRemoveIt = "bypass -h ZeuS v_buffer schRemoveThisScheme";
		SchemeChar sch = SELECTED_SCHEME_PLAYER.get(player.getObjectId());
		
		String ByPassRemoveIt = "bypass -h ZeuS removescheme " + sch.getSchemeName().replace(" ", "_");

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/buffer_delete_scheme.html");

		html.replace("%SCHEME_NAME%", sch.getSchemeName());
		html.replace("%ICON%", sch.getSchemeIcon());
		html.replace("%BYPASS%", ByPassRemoveIt);
		
		central.sendHtml(player, html);
	}
	
	private static void getWindowsRemoveBuff(L2PcInstance player, int Pagina){
		SchemeChar T = SELECTED_SCHEME_PLAYER.get(player.getObjectId());
		String colorPremium = "9096C5";
		String colorNormal = "73C685";
		
		int Maximo=8;
		
		int Contador=0;
		
		int desde = Pagina * Maximo;
		int Hasta = desde + Maximo;

		boolean HaveNext=false;
		
		String BypassRemoveBuf = "bypass -h ZeuS v_buffer schRemoveBuff %PAGINA% %SKILL% %LEVEL%";
		
		if(T.getTotalBuffDanceCount()<=0){
			return;
		}
		
		String retorno = "";
		for(int idB : T.getBuffs()){
			if(Contador >= desde && Contador < Hasta){
				int idLevel = T.getLevelFromBuff(idB);
				boolean isPremium = T.isPremiumBuff(idB, idLevel);
				String icono = "Icon.skill" + opera.getIconSkill(idB);
				retorno += "<tr><td fixwidth=40 align=center>"+
				cbFormato.getBotonForm(icono, BypassRemoveBuf.replace("%LEVEL%", String.valueOf(idLevel)) .replace("%SKILL%", String.valueOf(idB)) .replace("%PAGINA%", String.valueOf(Pagina)) ) + "</td><td fixwidth=240 align=LEFT>"+
				"<font color="+ ( isPremium ? colorPremium : colorNormal ) +">"+ opera.getSkillName(idB, idLevel) +"</font></td></tr>";
			}
			Contador++;
			if(Contador > Hasta){
				HaveNext=true;
				break;
			}
		}
		
		if(retorno.length()==0){
			retorno = "<tr><td></td></tr>";
		}

		//String ByPassSelectedIcon = "bypass -h ZeuS v_buffer SelIcOnModif %ICON%";
		
		String Controls = "";
		
		if(HaveNext || Pagina>0){
			String ByPass_Prev = "bypass -h ZeuS v_buffer schRemove " + String.valueOf(Pagina - 1);
	    	String ByPass_Next = "bypass -h ZeuS v_buffer schRemove " + String.valueOf(Pagina + 1);
	    	
	    	String BtnPrev = "<button action=\""+ ByPass_Prev +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
	    	String BtnNext = "<button action=\""+ ByPass_Next +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
	    	
	    	String Controles = "<table width=280><tr>"+
            "<td fixwidth=93 align=RIGHT>" + ( Pagina>0 ? BtnPrev : "" ) +"</td><td width=93 align=CENTER >"+
            "<font name=\"hs12\" color=76F7D1>"+ String.valueOf(Pagina + 1) +"</font></td>"+
            "<td fixwidth=93 align=LEFT>" + (HaveNext ? BtnNext : "") + "</td></tr></table>";
	    	Controls = Controles;
		}

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/buffer_remove_buff_from_scheme.html");
		
		html.replace("%BUFF%", retorno);
		html.replace("%CONTROL%", Controls);
		
		central.sendHtml(player, html);
	}
	
	private static void getWindowsChangeIcon(L2PcInstance player, String IconActual){
		SchemeChar T = SELECTED_SCHEME_PLAYER.get(player.getObjectId());
		String SchemeIcon = T.getSchemeIcon();

		String SelectIcon = T.getSchemeIcon().replace("icon.", "");
		
		if(IconActual.length()>0){
			SelectIcon = IconActual;
			TEMPORAL_SELECT_ICON.put(player.getObjectId(), IconActual);
		}
		
		
		String ByPassSelectedIcon = "bypass -h ZeuS v_buffer SelIcOnModif %ICON%"; 
		
		int Contador = 0;
		
		String retorno = "";
		
		for(String TIconShow : ICON_SKILL){
			if(Contador==0){
				retorno += "<tr>";
			}
			if(SelectIcon.equals(TIconShow)){
				retorno += "<td fixwidth=40 align=center><table width=30 height=31 background=icon."+ TIconShow +" border=0 cellspacing=0 cellpadding=0><tr><td><img src=L2UI_CT1.ItemWindow_DF_Frame_Over width=32 height=31><br></td></tr></table></td>";
			}else{
				retorno += "<td fixwidth=40 align=center>"+ cbFormato.getBotonForm("icon." + TIconShow, ByPassSelectedIcon.replace("%ICON%", TIconShow) ) + "</td>";
			}
			Contador++;
			if(Contador >= 6){
				retorno += "</tr>";
				Contador=0;
			}
		}
		
		if(Contador>0 && Contador<7){
			for(int i=Contador;i<7;i++){
				retorno += "<td fixwidth=40></td>";
			}
			retorno += "</tr>";
		}
		
		String BypassSave = "bypass -h ZeuS v_buffer SavEIcOnModif %ICON%";
		
		String btnChange = "";
		if(IconActual.length()>0){
			btnChange ="<tr><td align=center><br><button value=\"Change It!\" action=\""+ BypassSave.replace("%ICON%", "icon." +IconActual) +"\" width=180 height=32 back=L2UI_ct1.button_df fore=L2UI_ct1.button_df></td></tr>";
		}
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/buffer_change_scheme_icon.html");
	
		html.replace("%SCHEME_ICON%", SchemeIcon);
		html.replace("%DATA%", retorno);
		html.replace("%BUTTON_CHANGE%", btnChange);
		
		central.sendHtml(player, html);
	}
	
	
	
	private static String getWindowsModif(L2PcInstance player, int idScheme){
		if(SCHEME_P_PLAYER==null){
			return "";
		}
		
		if(!SCHEME_P_PLAYER.containsKey(player.getObjectId())){
			return "";
		}
		
		SchemeChar SchData = SCHEME_P_PLAYER.get(player.getObjectId()).get(idScheme);

		if(SchData==null){
			return "";
		}
		
		SELECTED_SCHEME_PLAYER.put(player.getObjectId(), SchData);

		boolean canAddMoreBuff = SchData.canAddMoreBuffDance();
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-buffer-change.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%SCHEME_NAME%", SchData.getSchemeName());
		html.replace("%BUFF_COUNT%", String.valueOf(SchData.getBuffCount()));
		html.replace("%DANCE_COUNT%", String.valueOf(SchData.getDanceCount()));
		html.replace("%ID_SCHEME%", String.valueOf(SchData.getSchemeID()));
		html.replace("%STATUS%", (!canAddMoreBuff ? " (Disabled)" : ""));
		
		return html.getHtml();
		
		
		
	}	
	
	private static String getMainWindowsCreateScheme(L2PcInstance player, String SelectIcon){
		int Contador = 0;
		
		TEMPORAL_SELECT_ICON.put(player.getObjectId(), "icon." + SelectIcon);
		
		String IconGrill = "<table width=544 background=L2UI_CT1.Windows_DF_TooltipBG cellpadding=4>";
		
		String ByPassSelectedIcon = "bypass -h ZeuS v_buffer SelIcOn %ICON%"; 
		String ByPassCreate = "bypass -h ZeuS v_buffer CScheme $txtNewScheme";
		
		for(String T : ICON_SKILL){
			if(Contador==0){
				IconGrill += "<tr>";
			}
			if(SelectIcon.equals(T)){
				IconGrill += "<td width=32><table width=30 height=31 background=icon."+ T +" border=0 cellspacing=0 cellpadding=0><tr><td><img src=L2UI_CT1.ItemWindow_DF_Frame_Over width=32 height=31><br></td></tr></table></td>";
			}else{
				IconGrill += "<td width=32>"+ cbFormato.getBotonForm("icon." + T, ByPassSelectedIcon.replace("%ICON%", T) ) + "</td>";
			}
			Contador++;
			if(Contador >= 15){
				IconGrill += "</tr>";
				Contador=0;
			}
		}
		
		IconGrill += "</table><br><br><br>";
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-buffer-create-scheme.htm");
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%DATA%", IconGrill);
		html.replace("%BYPASS_CREATE%", ByPassCreate);
		
		return html.getHtml();
	}
	
	
	private static void showWindowsModifNameScheme(L2PcInstance player){
		SchemeChar T = SELECTED_SCHEME_PLAYER.get(player.getObjectId());
		String ByPass = "bypass -h ZeuS v_buffer ChangeNameScheme $txtSchemeName";
		String SchemeName = T.getSchemeName();
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/buffer_change_scheme_name.html");
		html.replace("%SCHEME_NAME%", SchemeName);
		html.replace("%BYPASS%", ByPass);
		central.sendHtml(player, html);
	}
	
	@SuppressWarnings("rawtypes")
	private static NpcHtmlMessage getSchemeFromPlayers(L2PcInstance player, NpcHtmlMessage html){
		String ByPassBuffWidthScheme = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer + ";USE_SCH_ASK;%IDSCHEME%;0;0;0;0"; 
		String ByPassChange = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer + ";CHANGESCH;%IDSCHEME%;0;0;0;0";
		int fila = 1;
		if(SCHEME_P_PLAYER!=null){
			if(SCHEME_P_PLAYER.containsKey(player.getObjectId())){
				Iterator itr = SCHEME_P_PLAYER.get(player.getObjectId()).entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry Entrada = (Map.Entry)itr.next();
					int idScheme = (int) Entrada.getKey();
					SchemeChar T1 = (SchemeChar) Entrada.getValue();
					String Icono = T1.getSchemeIcon();
					String SchemeName = T1.getSchemeName();
					int SchemePrice = T1.getPrice();
					html.replace("%PLAYER_SCHEME_ICON_"+ String.valueOf(fila) +"%", cbFormato.getBotonForm(Icono, ByPassBuffWidthScheme.replace("%IDSCHEME%", String.valueOf(idScheme))));
					html.replace("%PLAYER_SCHEME_NAME_"+ String.valueOf(fila) +"%", SchemeName);
					html.replace("%PLAYER_SCHEME_PRICE_"+ String.valueOf(fila) +"%", opera.getFormatNumbers(SchemePrice));
					html.replace("%PLAYER_SCHEME_BYPASS_CHANGE_IT_"+ String.valueOf(fila) +"%", ByPassChange.replace("%IDSCHEME%", String.valueOf(idScheme)));
					fila++;
				}
			}
		}
		
		for(int i=fila; i<= general._BUFFCHAR_SCHEME_MAX; i++){
			html.replace("%PLAYER_SCHEME_ICON_"+ String.valueOf(i) +"%", "");
			html.replace("%PLAYER_SCHEME_NAME_"+ String.valueOf(i) +"%", "");
			html.replace("%PLAYER_SCHEME_PRICE_"+ String.valueOf(i) +"%", "");
			html.replace("%PLAYER_SCHEME_BYPASS_CHANGE_IT_"+ String.valueOf(i) +"%", "");			
		}
		
		
		return html;
		
		
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private static NpcHtmlMessage getSingleBuff(L2PcInstance player, NpcHtmlMessage html){
		String bypass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";BUFF_INDI;%IDCATEGORIA%;0;0;0;0";
		
		String grilla = "<table width=188 background=L2UI_CT1.Windows_DF_Small_Vertical_Bg><tr><td fixwidth=32 align=CENTER>"+
        cbFormato.getBotonForm("%ICONO%", bypass) + "<br><br></td><td fixwidth=156><br1>"+
        "<font color=C1AC67 name=hs12>%CATE_NOM%</font><br1>"+
        "<font color=F7BE81>Price: </font><font color=F5DA81>%PRICE%</font></td></tr></table>";
		
		int fila = 1;
		
		Iterator itr = general.getBuffCommnunityCategorias().entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			buffcommunity T1 = (buffcommunity) Entrada.getValue();
			html.replace("%SINGLE_BUFF_DATA_ICON_"+ String.valueOf(fila) +"%",  cbFormato.getBotonForm(T1.getCategoriaIcon(), bypass.replace("%IDCATEGORIA%", String.valueOf(T1.getIdCategoria()))));
			html.replace("%SINGLE_BUFF_DATA_NAME_"+ String.valueOf(fila) +"%", T1.getCategoriaName());
			html.replace("%SINGLE_BUFF_DATA_PRICE_"+ String.valueOf(fila) +"%", opera.getFormatNumbers(T1.getPrice()));
			fila++;
		}
		
		if(fila < 5){
			for(int i=fila; i<=5; i++){
				html.replace("%SINGLE_BUFF_DATA_ICON_"+ String.valueOf(i) +"%", "");
				html.replace("%SINGLE_BUFF_DATA_NAME_"+ String.valueOf(i) +"%", "");
				html.replace("%SINGLE_BUFF_DATA_PRICE_"+ String.valueOf(i) +"%", "");				
			}
		}
		return html;
	}
	
	@SuppressWarnings({ "rawtypes" })
	private static NpcHtmlMessage getSchemeReadys(L2PcInstance player, NpcHtmlMessage html){
		String bypass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer + ";SCHEMEBUFF_ASK;%IDSCHEME%;0;0;0;0";
		int levelPlayer = player.getLevel();
		Iterator itr = general.getBuffCommnunitySchemeReady().entrySet().iterator();
		int Fila = 1;
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int idScheme = (int) Entrada.getKey();
			buffcommunitySchemme BT = (buffcommunitySchemme)Entrada.getValue();
			int FreeUntilLevel = BT.getFreeUntilLevel();
			boolean isFree = (levelPlayer <= FreeUntilLevel);
			
			html.replace("%READY_SCHEME_ICON_"+ String.valueOf(Fila) + "%",cbFormato.getBotonForm(BT.getIcon(), bypass.replace("%IDSCHEME%", String.valueOf(idScheme))));
			html.replace("%READY_SCHEME_NAME_"+ String.valueOf(Fila) + "%",BT.getName());
			html.replace("%READY_SCHEME_PRICE_"+ String.valueOf(Fila) + "%", ( isFree ? "Free" : opera.getFormatNumbers(BT.getPrice()) ) );
			Fila++;
		}
		
		if(Fila < 5){
			for(int i=Fila; i<=5; i++){
				html.replace("%READY_SCHEME_ICON_"+ String.valueOf(i) + "%","");
				html.replace("%READY_SCHEME_NAME_"+ String.valueOf(i) + "%","");
				html.replace("%READY_SCHEME_PRICE_"+ String.valueOf(i) + "%","" );				
			}
		}
		
		return html;
	}
	
	private static String getStringMainWindows(L2PcInstance player){
		
		
		if(isTargetChar==null || !isTargetChar.containsKey(player.getObjectId())){
			isTargetChar.put(player.getObjectId(), true);
		}
		
		if(!isTargetChar.get(player.getObjectId())){
			if(player.getSummon()==null){
				isTargetChar.put(player.getObjectId(), true);
			}
		}
		
		//String bypass_Heal = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";HEAL;0;0;0;0;0";
		
		String iconHuman = "icon.skill1297";
		String iconPet = "icon.skill1497";
        String selectedTarget = (isTargetChar.get(player.getObjectId()) ? iconHuman : iconPet);

        NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-buffer.htm");
        
		html = getSchemeReadys(player, html);
		html = getSingleBuff(player, html);
		html = getSchemeFromPlayers(player, html);
		
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%HEAL_PRICE%", opera.getFormatNumbers(general.BUFFCHAR_COST_HEAL));
		html.replace("%CANCEL_PRICE%",  opera.getFormatNumbers(general.BUFFCHAR_COST_CANCEL));
		html.replace("%TARGET_ICON%", selectedTarget);
		html.replace("%TARGET%", (isTargetChar.get(player.getObjectId()) ? "Self" : "Pet"));
		return html.getHtml();
	}
	
	
	private static String getBuffforScheme(L2PcInstance player, int pagina){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-buffer-create-scheme-buff.htm");
		String gridFormat = opera.getGridFormatFromHtml(html, 1, "%INDI_BUFF_GRID%");
		
		String BuffLeft = String.valueOf(Config.BUFFS_MAX_AMOUNT - SELECTED_SCHEME_PLAYER.get(player.getObjectId()).getBuffCount());
		String DanceLeft = String.valueOf(Config.DANCES_MAX_AMOUNT - SELECTED_SCHEME_PLAYER.get(player.getObjectId()).getDanceCount());

		String ByPassBuff = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";SAVEBUFFDANCESCH;%IDBUFF%;%LEVELBUFF%;%ISPREMIUM%;"+ String.valueOf(pagina) +";0;0";
		gridFormat = gridFormat.replace("%BUFF_BYPASS_USE%", ByPassBuff);

		int LimitpourPage = 18;
		int Desde = pagina * LimitpourPage;
		int Hasta = Desde + LimitpourPage;
		
		int contador = 0;
		int ContBuff = 1;
		boolean HaveNext= false;
		
		String BuffIndiGrid = "";
		for( buffcommunity T1 : general.getBuffCommnunity_individual()){
			//Map.Entry Entrada = (Map.Entry)itr.next();
			
			if(!T1.isActive()){
				continue;
			}
			
			if(SELECTED_SCHEME_PLAYER.get(player.getObjectId()).hasBuffOnScheme(T1.getBuffID()) ){
				continue;
			}			
			
			if(ContBuff <= Desde){
				ContBuff++;
				continue;
			}

			if(ContBuff <= Hasta){
				if(contador==0){
					BuffIndiGrid += "<tr>";
				}
				
				String colorTexto = "C1AC67";
	
				String EnchantSkill = T1.getEnchantBuff();
				
				if(T1.isPremium()){
					colorTexto = "04B4AE";
				}
				//%IDBUFF%;%LEVEL%;%ISPREMIUM%
				
				BuffIndiGrid += gridFormat.replace("%ISPREMIUM%", T1.isPremium() ? "true" : "false" ).replace("%LEVELBUFF%", String.valueOf(T1.getBuffLevel())) .replace("%IDBUFF%", String.valueOf(T1.getBuffID()));
				BuffIndiGrid = BuffIndiGrid.replace("%BUFF_ENCHANT%", (EnchantSkill.length()>0 ? " +" + EnchantSkill : "" )) .replace("%BUFF_FONTCOLOR%", colorTexto).replace("%BUFF_NAME%", T1.getBuffName()).replace("%BUFF_ICON%", "Icon.skill" + opera.getIconSkill(T1.getBuffID()));			
	
				contador++;
				if(contador >=3){
					BuffIndiGrid += "</tr>";
					contador=0;
				}
			}else if(ContBuff > Hasta){
				HaveNext = true;
				break;
			}
			ContBuff++;
		}
		
		if(contador >0 && contador <3){
			for(int i=contador ; i<3 ;i++){
				BuffIndiGrid += "<td></td>";
			}
			BuffIndiGrid += "</tr>";
		}
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.Buffer.name() + ";SCHEMESELECTBUFF;" + String.valueOf(pagina-1) + ";0;0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";" + Engine.enumBypass.Buffer.name() + ";SCHEMESELECTBUFF;" + String.valueOf(pagina+1) + ";0;0;0;0";		
		
		html.replace("%BYPASS_PREV%", (pagina > 0 ? bypassAntes : ""));
		html.replace("%BYPASS_NEXT%", (HaveNext ? bypassProx : ""));
		html.replace("%PAGE_NUMBER%", String.valueOf(pagina + 1));
		
		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%BUFF_LEFT%", BuffLeft);
		html.replace("%DANCE_LEFT%", DanceLeft);
		html.replace("%INDI_BUFF_GRID%", BuffIndiGrid);
		
		return html.getHtml();
	}	
	
	
	
	
	private static String getBuffIndi(L2PcInstance player, int idCategoria, int Page){
		int LimitpourPage = 18;
		int Desde = Page * LimitpourPage;
		int Hasta = Desde + LimitpourPage;
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-buffer-buff-indi.htm");
		String gridFormat = opera.getGridFormatFromHtml(html, 1, "%INDI_BUFF_GRID%");
		
		String ByPassBuff = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";USEBUFF;%IDBUFF%;%LEVELBUFF%;%ISPREMIUM%;"+ String.valueOf(idCategoria) +";" + String.valueOf(Page);
		gridFormat = gridFormat.replace("%BUFF_BYPASS_USE%", ByPassBuff);
		
		int contador = 0;
		int ContBuff = 1;
		
		boolean HaveNext = false;
		
		String BuffIndiGrid = "";
		
		for( buffcommunity T1 : general.getBuffCommnunity_individual()){
			if(idCategoria != T1.getIdCategoria()){
				continue;
			}
			
			if(!T1.isActive()){
				continue;
			}
			
			if(ContBuff <= Desde){
				ContBuff++;
				continue;
			}
			
			if(ContBuff <= Hasta){
				if(contador==0){
					BuffIndiGrid += "<tr>";
				}
				
				String colorTexto = "C1AC67";
	
				String EnchantSkill = T1.getEnchantBuff();
				
				if(T1.isPremium()){
					colorTexto = "04B4AE";
				}
				//USEBUFF;%IDBUFF%;%LEVELBUFF%;%ISPREMIUM%
				BuffIndiGrid += gridFormat.replace("%ISPREMIUM%", T1.isPremium() ? "true" : "false" ).replace("%LEVELBUFF%", String.valueOf(T1.getBuffLevel())) .replace("%IDBUFF%", String.valueOf(T1.getBuffID()));
				BuffIndiGrid = BuffIndiGrid.replace("%BUFF_ENCHANT%", (EnchantSkill.length()>0 ? " +" + EnchantSkill : "" )) .replace("%BUFF_FONTCOLOR%", colorTexto).replace("%BUFF_NAME%", T1.getBuffName()).replace("%BUFF_ICON%", "Icon.skill" + opera.getIconSkill(T1.getBuffID()));
	
				contador++;
				if(contador >=3){
					BuffIndiGrid += "</tr>";
					contador=0;
				}
			}else if(ContBuff > Hasta){
				HaveNext = true;
				break;
			}
			ContBuff++;
		}
		
		if(contador >0 && contador <3){
			for(int i=contador ; i<3 ;i++){
				BuffIndiGrid += "<td></td>";
			}
			BuffIndiGrid+="</tr>";
		}
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";BUFF_INDI;"+ String.valueOf(idCategoria) +";"+ String.valueOf(Page - 1) +";0;0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";BUFF_INDI;"+ String.valueOf(idCategoria) +";"+ String.valueOf(Page + 1) +";0;0;0";

		html.replace("%BYPASS_PREV%", (Page > 0 ? bypassAntes : ""));
		html.replace("%BYPASS_NEXT%", (HaveNext ? bypassProx : ""));
		html.replace("%PAGE_NUMBER%", String.valueOf(Page + 1));
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%INDI_BUFF_GRID%", BuffIndiGrid);
		return html.getHtml();
	}
	
	private static String getCentralHtml(L2PcInstance player,String Params){
		String retorno = "";

		if(Params.split(";")[2].equals("0")){
			retorno += getStringMainWindows(player);
		}else if(Params.split(";")[2].equals("USEBUFF")){
			boolean usarBuff = true;
			if(player.isInOlympiadMode()){
				usarBuff= false;
			}
			String tId = Params.split(";")[3] + Params.split(";")[4];
			boolean UseCombatMode = general.getBuffCommnunity_individual(Integer.valueOf(tId)).canUseInCombatMode();
			boolean UseFlagMode = general.getBuffCommnunity_individual(Integer.valueOf(tId)).canUseInFlagMode();
			boolean UseKarma = general.getBuffCommnunity_individual(Integer.valueOf(tId)).canUseWithKarma();			
			boolean UseInPeaceZone = general.BUFFCHAR_JUST_ON_PEACE_ZONE;
			if(SunriseEvents.isInEvent(player))
				UseInPeaceZone = false;
			if(UseInPeaceZone){
				if(!player.isInsideZone(ZoneIdType.PEACE )){
					if(player.getInstanceId()>0){
						if(!general.BUFFCHAR_USE_INSTANCE){
							central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_JUST_IN_PEACE_ZONE, player);
							usarBuff= false;							
						}
					}else{
						central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_JUST_IN_PEACE_ZONE, player);
						usarBuff= false;
					}
				}
			}
			
			if(UseFlagMode && player.isCombatFlagEquipped()){
				central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG, player);
				usarBuff= false;
			}
			
			if( UseKarma && (player.getKarma()>0)){
				central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK, player);
				usarBuff= false;			
			}
			
			if(!UseCombatMode && player.isInCombat()){
				central.msgbox(language.getInstance().getMsg(player).CANT_USE_IN_COMBAT_MODE, player);
				usarBuff= false;
			}			
			
			int IdBuff = Integer.valueOf(Params.split(";")[3]);
			int LevelBuff = Integer.valueOf(Params.split(";")[4]);
			boolean isPremiumBuff = Boolean.valueOf(Params.split(";")[5]);
			
			premiumPersonalData U_Pre = null;
			if(isPremiumBuff && opera.isPremium_Player(player)){
				U_Pre = general.getPremiumDataFromPlayerOrClan(player.getAccountName());
			}else if(isPremiumBuff && opera.isPremium_Clan(player)){
				U_Pre = general.getPremiumDataFromPlayerOrClan(String.valueOf(player.getClan().getId()));
			}else if(isPremiumBuff){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_DONT_HAVE_DONATION_COIN, player);
				usarBuff = false;
			}
			
			String IdT = String.valueOf(IdBuff) + String.valueOf(LevelBuff);
			
			if(U_Pre!=null){
				if(U_Pre.isActive()){
					premiumsystem PremiumDa = U_Pre.getPremiumData();
					if(PremiumDa!=null){
						if(PremiumDa.isEnabled()){
							if(!PremiumDa.canUseBuffPremium()){
								central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_PREMIUM_SYSTEM_NO_HAVE_PREMIUM_BUFF, player);
								usarBuff = false;
							}
						}else{
							central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_PREMIUM_SYSTEM_IS_OVER, player);
							usarBuff = false;
						}
					}
				}else{
					usarBuff=false;
					central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_PREMIUM_SYSTEM_IS_OVER, player);
				}
			}

			buffcommunity TempBuffData = general.getBuffCommnunity_individual(Integer.valueOf(IdT));
			
			if(TempBuffData.getReuseTime()>0) {
				if(!canUseReuseSkill(player,TempBuffData.getBuffID(),TempBuffData.getReuseTime())){
					usarBuff = false;
				}
			}
			
			int priceBuff = TempBuffData.getPrice();
			int MinLevel = TempBuffData.getMinLevelToUse();

			if(usarBuff && player.getLevel() < MinLevel){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NEED_A_MINIMUN_LEVEL_TO_USE_THIS.replace("$level",String.valueOf(MinLevel)), player);
				usarBuff=false;
			}
			
			if(usarBuff && !opera.haveItem(player, general.BUFFCHAR_SCHEME_USE_ITEM_ID, priceBuff)){
				usarBuff = false;
			}
			
			boolean TargetPlayer = isTargetChar.get(player.getObjectId());
			
			if(usarBuff && (!TargetPlayer && player.getSummon()==null)){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NOT_HAVE_PET, player);
				usarBuff= false;
			}
			
			if(usarBuff && (!TargetPlayer && player.getSummon().isDead())){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NOT_HAVE_PET, player);
				usarBuff= false;
			}
			
			
			if(usarBuff){
				opera.removeItem(57, priceBuff, player);
				try{
					if(TargetPlayer){
						if (player.isAffectedBySkill(IdBuff))
						{
							player.stopSkillEffects(IdBuff);
						}						
						SkillData.getInstance().getInfo(IdBuff, LevelBuff).getEffects(player, player);
					}else{
						if (player.getSummon().isAffectedBySkill(IdBuff))
						{
							player.getSummon().stopSkillEffects(IdBuff);
						}						
						SkillData.getInstance().getInfo(IdBuff, LevelBuff).getEffects(player.getSummon(), player.getSummon());
					}
				}catch(Exception a){
					
				}
			}
			retorno += getBuffIndi(player,Integer.valueOf(Params.split(";")[6]),Integer.valueOf(Params.split(";")[7]));
			//USEBUFF;%IDBUFF%;%LEVELBUFF%;%ISPREMIUM%
		}else if(Params.split(";")[2].equals("HEAL_ASK")){
			String Msje = language.getInstance().getMsg(player).BUFFERCHAR_YOU_WANT_HEAL.replace("$mount", opera.getFormatNumbers(general.BUFFCHAR_COST_HEAL));
			Dlg.sendDlg(player, Msje, IdDialog.ENGINE_BUFFERCHAR_HEAL);
			retorno += getStringMainWindows(player);
		}else if(Params.split(";")[2].equals("CANCEL_ASK")){
			String Msje = language.getInstance().getMsg(player).BUFFERCHAR_YOU_WANT_CANCEL.replace("$mount", opera.getFormatNumbers(general.BUFFCHAR_COST_HEAL));
			Dlg.sendDlg(player, Msje, IdDialog.ENGINE_BUFFERCHAR_CANCEL);
			retorno += getStringMainWindows(player);			
		}else if(Params.split(";")[2].equals("HEAL") || Params.split(";")[2].equals("CANCEL")){
			if(canUseHealCancel(player)){
				if(Params.split(";")[2].equals("HEAL")){
					healProcess(player);
				}else if(Params.split(";")[2].equals("CANCEL")){
					cancelProcess(player);
				}
			}
			retorno += getStringMainWindows(player);			
		}else if(Params.split(";")[2].equals("CHANGETARGET")){
			if(isTargetChar==null){
				isTargetChar.put(player.getObjectId(), true);
			}else if(!isTargetChar.containsKey(player.getObjectId())){
				isTargetChar.put(player.getObjectId(), true);
			}else if(!isTargetChar.get(player.getObjectId()) && player.getSummon()==null){
				isTargetChar.put(player.getObjectId(), true);
			}else{
				isTargetChar.put(player.getObjectId(), !isTargetChar.get(player.getObjectId()));
			}
			retorno += getStringMainWindows(player);			
		}else if(Params.split(";")[2].equals("BUFF_INDI")){
			retorno += getBuffIndi(player,Integer.valueOf(Params.split(";")[3]),Integer.valueOf(Params.split(";")[4]));
		}else if(Params.split(";")[2].equals("WCREATESCHEME")){
			retorno += getMainWindowsCreateScheme(player, Params.split(";")[3]);
		}else if(Params.split(";")[2].equals("SCHEMESELECTBUFF")){
			retorno += getBuffforScheme(player, Integer.valueOf(Params.split(";")[3]));
		}else if(Params.split(";")[2].equals("SAVEBUFFDANCESCH")){
			int idBuff = Integer.valueOf(Params.split(";")[3]);
			int idBuffLevel = Integer.valueOf(Params.split(";")[4]);
			boolean isPremium = Boolean.valueOf(Params.split(";")[5]);
			int pagina = Integer.valueOf(Params.split(";")[6]);
			SchemeChar T1 = SELECTED_SCHEME_PLAYER.get(player.getObjectId());
			T1.setBuffToScheme(player, idBuff, idBuffLevel, isPremium);
			retorno += getBuffforScheme(player, pagina);
		}else if(Params.split(";")[2].equals("USE_SCH_ASK")){
			int idScheme = Integer.valueOf(Params.split(";")[3]);
			t_ID.put(player.getObjectId(), idScheme);
			SchemeChar schMe = SCHEME_P_PLAYER.get(player.getObjectId()).get(idScheme);
			String msgje = language.getInstance().getMsg(player).BUFFERCHAR_YOU_WANT_TO_USE_YOU_OWN_SCHEME.replace("$schemeName", schMe.getSchemeName()).replace("$Cost", opera.getFormatNumbers(schMe.getPrice()));
			Dlg.sendDlg(player, msgje, IdDialog.ENGINE_BUFFERCHAR_USE_YOU_OWN_SCHEME);
			retorno += getStringMainWindows(player);
		}else if(Params.split(";")[2].equals("USE_SCH")){
			int idScheme = t_ID.get(player.getObjectId());
			SchemeChar schMe = SCHEME_P_PLAYER.get(player.getObjectId()).get(idScheme);
			schMe.useScheme(player, isTargetChar.get(player.getObjectId()));
			retorno += getStringMainWindows(player);
		}else if(Params.split(";")[2].equals("CHANGESCH")){
			int idScheme = Integer.valueOf(Params.split(";")[3]);
		
			retorno += getWindowsModif(player,idScheme);
		}else if(Params.split(";")[2].equals("SHOWNAMECHANGEWIN")){
			showWindowsModifNameScheme(player);
			return "";
		}else if(Params.split(";")[2].equals("SHOWICONCHANGEWIN")){
			getWindowsChangeIcon(player, "");
			return "";
		}else if(Params.split(";")[2].equals("ADDSCHEMESELECTBUFF")){
			int idScheme = Integer.valueOf(Params.split(";")[3]);
			SchemeChar SchemeSe = SCHEME_P_PLAYER.get(player.getObjectId()).get(idScheme);
			if(!SchemeSe.canAddMoreBuffDance()){
				retorno += getStringMainWindows(player);
			}else{
				SELECTED_SCHEME_PLAYER.put(player.getObjectId(), SchemeSe);
				retorno += getBuffforScheme(player, Integer.valueOf(Params.split(";")[4]));				
			}
		}else if(Params.split(";")[2].equals("REMMOVECHANGEWIN")){
			getWindowsRemoveBuff(player, 0);
			return "";
		}else if(Params.split(";")[2].equals("REMOVESCHEME")){
			getWindowsRemoveScheme(player);
			return "";
		}else if(Params.split(";")[2].equals("SCHEMEBUFF_ASK")){
			int idSchemeBuff = Integer.valueOf(Params.split(";")[3]);
			t_ID.put(player.getObjectId(), idSchemeBuff);
			buffcommunitySchemme SchB = general.getBuffCommnunitySchemeReady().get(idSchemeBuff);
			String msgje =  "";
			if(SchB.getFreeUntilLevel() >= player.getLevel()){
				msgje = language.getInstance().getMsg(player).BUFFERCHAR_YOU_WANT_TO_USE_THIS_SCHEME.replace("$schemeName", SchB.getName()).replace("$Cost", "FREE");
			}else{
				msgje = language.getInstance().getMsg(player).BUFFERCHAR_YOU_WANT_TO_USE_THIS_SCHEME.replace("$schemeName", SchB.getName()).replace("$Cost", opera.getFormatNumbers(SchB.getPrice()) + " Adena" );
			}
			Dlg.sendDlg(player, msgje, IdDialog.ENGINE_BUFFERCHAR_USE_SCHEME);
			retorno += getStringMainWindows(player);			
		}else if(Params.split(";")[2].equals("SCHEMEBUFF")){
			int idSchemeBuff = t_ID.get(player.getObjectId());
			buffcommunitySchemme SchB = general.getBuffCommnunitySchemeReady().get(idSchemeBuff);
			SchB.buffChar(player, isTargetChar.get(player.getObjectId()));
			retorno += getStringMainWindows(player);			
		}
		
		//getWindowsRemoveBuff REMMOVECHANGEWIN
		
		
		//"bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";SAVEBUFFDANCESCH;%IDBUFF%;%LEVEL%;%ISPREMIUM%;0;0;0";
		
		//retorno += cbManager.getPieCommunidad() + "</center></body></html>";
		return retorno;
	}
	
	private static boolean canUseHealCancel(L2PcInstance player){
		boolean isInFlag = player.isCombatFlagEquipped();
		boolean isInCombat = player.isInCombat();
		boolean isInFromPeaceZone =  true;
		
		if(general.BUFFCHAR_JUST_ON_PEACE_ZONE){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				if(player.getInstanceId()>0){
					if(!general.BUFFCHAR_USE_INSTANCE){
						isInFromPeaceZone = false;							
					}
				}else{
					isInFromPeaceZone = false;
				}
			}
		}
		
		if( (general.BUFFCHAR_HEAL_AND_CANCEL_JUST_PEACE_ZONE && !player.isInsideZone(ZoneIdType.PEACE)) ||  ( !general.BUFFCHAR_CAN_USE_COMBAT_MODE && isInCombat ) || !isInFromPeaceZone || (!general.BUFFCHAR_HEAL_AND_CANCEL_COMBAT_MODE &&  player.isInCombat()) || (!general.BUFFCHAR_HEAL_AND_CANCEL_FLAG_MODE && isInFlag))  {
			central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
			return false;
		}
		
		if(general.BUFFCHAR_CAN_HEAL_CANCEL_JUST_IN_PEACE_ZONE){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);				
				return false;
			}
		}
		
		return true;
	}
	
	public static void cancelProcess(L2PcInstance player){
		if(!canUseHealCancel(player)){
			return;
		}
		boolean remove = false;
		if(general.BUFFCHAR_COST_CANCEL>0){
			if(opera.haveItem(player, general.BUFFCHAR_SCHEME_USE_ITEM_ID, general.BUFFCHAR_COST_CANCEL)){
				opera.removeItem(general.BUFFCHAR_SCHEME_USE_ITEM_ID, general.BUFFCHAR_COST_CANCEL, player);
				remove=true;
			}
		}else{
			remove = true;
		}
		if(remove){
			if(isTargetChar.get(player.getObjectId())){
				player.stopAllEffects();
			}else{
				if(player.getSummon()!=null){
					player.getSummon().stopAllEffects();								
				}
			}
		}		
	}
	
	public static void healProcess(L2PcInstance player){
		if(!canUseHealCancel(player)){
			return;
		}		
		boolean heal = false;
		if(general.BUFFCHAR_COST_HEAL>0){
			if(opera.haveItem(player, general.BUFFCHAR_SCHEME_USE_ITEM_ID, general.BUFFCHAR_COST_HEAL)){
				opera.removeItem(general.BUFFCHAR_SCHEME_USE_ITEM_ID, general.BUFFCHAR_COST_HEAL, player);
				heal = true;
			}
		}else{
			heal = true;
		}
		if(heal){
			central.healAll(player, false);
			if(player.getSummon()!=null){
				central.healAll(player, true);								
			}						
		}		
	}
	
	
	public static String delegar(L2PcInstance player, String params){
		if(!general.STATUS_BUFFER) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";
		}		
		if(player.isInTownWarEvent() && !general.EVENT_TOWN_WAR_CAN_USE_BUFFER){
			central.msgbox(language.getInstance().getMsg(player).CANT_USE_IN_SIEGE_OR_TOWN_WAR, player);
			cbFormato.cerrarCB(player);
			return "";
		}
		
		String retorno = "";
		
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		switch(parm1){
			case "0":
			case "BUFF_INDI":
			case "WCREATESCHEME":
			case "SCHEMESELECTBUFF":
			case "SAVEBUFFDANCESCH":
			case "CHANGESCH":
			case "SHOWNAMECHANGEWIN":
			case "SHOWICONCHANGEWIN":
			case "ADDSCHEMESELECTBUFF":
			case "REMMOVECHANGEWIN":
			case "REMOVESCHEME":
			case "SCHEMEBUFF":
			case "SCHEMEBUFF_ASK":
			case "CHANGETARGET":
			case "HEAL":
			case "HEAL_ASK":
			case "CANCEL":
			case "CANCEL_ASK":
			case "USEBUFF":
			case "USE_SCH_ASK":
			case "USE_SCH":
				retorno = getCentralHtml(player, params);
		}
		
		
		
		return retorno;
	}
	
	public static void getSchemeFromPlayer(L2PcInstance player){
		try{
			SCHEME_P_PLAYER.remove(player.getObjectId());
		}catch(Exception a){
			
		}
		String Consulta = "SELECT id,NomSch,idIcon FROM zeus_buff_char_sch WHERE idChar=?";
		try{
			Connection conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			psqry.setInt(1, player.getObjectId());
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						SchemeChar T = new SchemeChar(rss.getString("NomSch"), rss.getString("idIcon"), rss.getInt("id"));
						T.loadBuffFromScheme();
						if(SCHEME_P_PLAYER==null){
							SCHEME_P_PLAYER.put(player.getObjectId(), new HashMap<Integer, SchemeChar>());							
						}else{
							if(!SCHEME_P_PLAYER.containsKey(player.getObjectId())){
								SCHEME_P_PLAYER.put(player.getObjectId(), new HashMap<Integer, SchemeChar>());
							}
						}
						SCHEME_P_PLAYER.get(player.getObjectId()).put(rss.getInt("id"), T);						
						
					}catch(Exception e){
						_log.warning("Error loading ZeuS Buffer Scheme Player -> " + e.getMessage());
					}
				}
			conn.close();
		}catch(SQLException e){

		}		
	}

	private static boolean createScheme(L2PcInstance player, String Name){
		boolean retorno = true;
		
		if(SCHEME_P_PLAYER!=null){
			if(SCHEME_P_PLAYER.containsKey(player.getObjectId())){
				if(SCHEME_P_PLAYER.get(player.getObjectId()) !=  null){
					if(SCHEME_P_PLAYER.get(player.getObjectId()).size()>0){
						int SchemeFromPlayer = SCHEME_P_PLAYER.get(player.getObjectId()).size();
						if(SchemeFromPlayer >= general._BUFFCHAR_SCHEME_MAX){
							central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_ADD_MORE_SCHEME.replace("$limit", String.valueOf(general._BUFFCHAR_SCHEME_MAX)) , player);
							central.msgbox("You Have->" + String.valueOf(SchemeFromPlayer), player);
							return false;
						}
					}
				}
			}
		}
		
			String consulta = "INSERT INTO zeus_buff_char_sch(idChar,NomSch,idIcon) VALUES(?,?,?)";
			
			Connection con = null;
			PreparedStatement statement = null;
			try{
				con = L2DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement(consulta,Statement.RETURN_GENERATED_KEYS);
				statement.setInt(1, player.getObjectId());
				statement.setString(2, Name);
				statement.setString(3, TEMPORAL_SELECT_ICON.get(player.getObjectId()) );
				statement.execute();
				try (ResultSet rset = statement.getGeneratedKeys())
				{
					if (rset.next())
					{
						int _id = rset.getInt(1);
						SchemeChar T = new SchemeChar(Name, TEMPORAL_SELECT_ICON.get(player.getObjectId()), _id);
						
						if(SCHEME_P_PLAYER==null){
							SCHEME_P_PLAYER.put(player.getObjectId(), new HashMap<Integer, SchemeChar>());							
						}else{
							if(!SCHEME_P_PLAYER.containsKey(player.getObjectId())){
								SCHEME_P_PLAYER.put(player.getObjectId(), new HashMap<Integer, SchemeChar>());
							}
						}
						SCHEME_P_PLAYER.get(player.getObjectId()).put(_id, T);
						if(SCHEME_P_PLAYER.size()==1){
							TEMPORAL_SELECT_ICON.remove(player.getObjectId());
						}
						SELECTED_SCHEME_PLAYER.put(player.getObjectId(), T);
					}
				}			
			}catch(Exception a){
				_log.warning("Error on Save scheme->" + a.getMessage());
			}			
			
		
		return retorno;
	}

	public static void voiceByPass(L2PcInstance activeChar, String params) {
		if(!general.STATUS_BUFFER) {
			central.msgbox(language.getInstance().getMsg(activeChar).DISABLE_BY_ADMIN, activeChar);
			return;			
		}		
		if(params==null){
			String MainW = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";0;0;0;0;0;0;0";
			cbManager.separateAndSend(delegar(activeChar, MainW), activeChar);
			return;
		}
		
		if(params.trim().length()>0){
			String Comando = params.split(" ")[0];
			switch(Comando){
				case "CScheme":
					if( TEMPORAL_SELECT_ICON ==null  ){
						return;
					}
					if( !TEMPORAL_SELECT_ICON.containsKey(activeChar.getObjectId()) ){
						return;
					}
					
					String NombreSelected = "";
					try{
						NombreSelected = params.substring(8, params.length());
					}catch(Exception a){
						String Send = getCentralHtml(activeChar, general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";WCREATESCHEME;skill0284;0;0;0;0;0");
						cbManager.separateAndSend(Send, activeChar);
						return;
					}
					
					NombreSelected = NombreSelected.length()>10 ? NombreSelected.substring(0,10) : NombreSelected;
					if(createScheme(activeChar,NombreSelected)){
						central.msgbox(language.getInstance().getMsg(activeChar).BUFFERCHAR_SCHEME_CREATED, activeChar);
					}
					String cbSelBuff = delegar(activeChar, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";SCHEMESELECTBUFF;0;0;0;0;0;0");
					cbManager.separateAndSend(cbSelBuff, activeChar);
					break;
				case "SelIcOn":
					String IconId = params.split(" ")[1];
					String cbHt = delegar(activeChar, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";WCREATESCHEME;"+ IconId +";0;0;0;0;0"); 
					cbManager.separateAndSend(cbHt, activeChar);
					break;
				case "ChangeNameScheme":
					SchemeChar T1 = SELECTED_SCHEME_PLAYER.get(activeChar.getObjectId());
					String NombreChange = params.substring(17, params.length());
					T1.setNewNameScheme(NombreChange);
					String cbMain = delegar(activeChar, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";CHANGESCH;"+ String.valueOf(T1.getSchemeID()) +";0;0;0;0;0");
					cbManager.separateAndSend(cbMain, activeChar);
					central.msgbox(language.getInstance().getMsg(activeChar).BUFFERCHAR_SCHEME_NAME_CHANGE, activeChar);
					break;
				case "SelIcOnModif":
					String IconIdChange = params.split(" ")[1];
					getWindowsChangeIcon(activeChar, IconIdChange);
					break;
				case "SavEIcOnModif":
					String IconIdSave = params.split(" ")[1];
					SchemeChar T1a = SELECTED_SCHEME_PLAYER.get(activeChar.getObjectId());
					T1a.setNewIconScheme(IconIdSave);
					String cbMaina = delegar(activeChar, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";CHANGESCH;"+ String.valueOf(T1a.getSchemeID()) +";0;0;0;0;0");
					cbManager.separateAndSend(cbMaina, activeChar);
					central.msgbox(language.getInstance().getMsg(activeChar).BUFFERCHAR_SCHEME_ICON_CHANGE, activeChar);
					break;
				case "schRemove":
					int Pagina = Integer.valueOf(IconIdSave = params.split(" ")[1]); 
					getWindowsRemoveBuff(activeChar, Pagina);
					break;
				case "schRemoveBuff":
					int pagina = Integer.valueOf(IconIdSave = params.split(" ")[1]);
					int idSkill = Integer.valueOf(IconIdSave = params.split(" ")[2]);
					int idLevel = Integer.valueOf(IconIdSave = params.split(" ")[3]);
					SchemeChar T3 = SELECTED_SCHEME_PLAYER.get(activeChar.getObjectId());
					T3.setRemoveBuff(idSkill, idLevel);
					String BypassEn = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";CHANGESCH;" + String.valueOf(T3.getSchemeID()) + ";0;0;0;0;0";
					cbManager.separateAndSend( delegar(activeChar, BypassEn) , activeChar);
					getWindowsRemoveBuff(activeChar, pagina);
					central.msgbox(language.getInstance().getMsg(activeChar).BUFFERCHAR_SCHEME_REMOVE_BUFF, activeChar);
					break;
				case "schRemoveThisScheme":
					int idScheme = SELECTED_SCHEME_PLAYER.get(activeChar.getObjectId()).getSchemeID();
					SELECTED_SCHEME_PLAYER.get(activeChar.getObjectId()).RemoveAll();
					SCHEME_P_PLAYER.get(activeChar.getObjectId()).remove(idScheme);
					SELECTED_SCHEME_PLAYER.remove(activeChar.getObjectId());
					String cbMainW = delegar(activeChar, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Buffer.name() + ";0;0;0;0;0;0");
					cbManager.separateAndSend(cbMainW, activeChar);
					central.msgbox(language.getInstance().getMsg(activeChar).BUFFERCHAR_SCHEME_REMOVED, activeChar);
					return;
					//String ByPass_Next = "bypass -h ZeuS v_buffer schRemove:" + String.valueOf(Pagina + 1);
			}
		}
	}
}
class SchemeChar extends v_Buffer_New{
	private String SCHEME_NAME;
	private String SCHEME_ICON;
	private int SCHEME_ID;
	private Vector<Integer> SCHEME_BUFF = new Vector<Integer>();
	private Map<Integer, Integer> SCHEME_BUFF_LEVEL = new HashMap<Integer, Integer>();
	@SuppressWarnings("unused")
	private boolean isDonate;
	private int PRICETOUSE=0;
	
	private static Logger _log = Logger.getLogger(v_Buffer_New.class.getName());
	
	
	
	private enum Status{
		FLAG,
		KARMA,
		COMBAT
	}
	
	private boolean isValidCondition(buffcommunity BF, L2PcInstance player, Status estado){
		switch(estado){
			case FLAG:
				if(!BF.canUseInFlagMode() && player.isCombatFlagEquipped()){
					return false;
				}				
				break;
			case KARMA:
				if(!BF.canUseWithKarma() && player.getKarma()>0){
					return false;
				}				
				break;
			case COMBAT:
				if(!BF.canUseInCombatMode() && player.isInCombat()){
					return false;
				}				
				break;
		}
		return true;
	}
	
	private void updatePrice(L2PcInstance player){
		this.PRICETOUSE=0;
		for(int IdB : this.SCHEME_BUFF){
			int Bufflvl = SCHEME_BUFF_LEVEL.get(IdB);
			String Ct =String.valueOf(IdB) + String.valueOf(Bufflvl);
			buffcommunity InfoB = general.getBuffCommnunity_individual(Integer.valueOf(Ct));
			boolean canUsePremiumBuff = false;
			premiumPersonalData PremiumData = null;			
			if( ZeuS.isPremium(player) ){
				if(opera.isPremium_Clan(player)){
					PremiumData = general.getPremiumDataFromPlayerOrClan( String.valueOf(player.getClanId()));
				}else if(opera.isPremium_Player(player)){
					PremiumData = general.getPremiumDataFromPlayerOrClan( player.getAccountName());
				}
				if(PremiumData!=null){
					canUsePremiumBuff = PremiumData.getPremiumData().canUseBuffPremium();
				}
			}
			
			if((InfoB.isPremium() && canUsePremiumBuff) || (!InfoB.isPremium())){
				this.PRICETOUSE += InfoB.getPrice();
			}else if(InfoB.isPremium() && !canUsePremiumBuff){
				continue;
			}
		}		
	}
	
	public void useScheme(L2PcInstance player, boolean TargetPlayer){
		boolean ShowErrorMensaje = true;
		boolean removePrice = false;
		
		if(( !general.BUFFCHAR_SCHEME_USE_COMBAT_MODE && player.isInCombat()) || ( !general.BUFFCHAR_SCHEME_USE_FLAG_MODE && player.isCombatFlagEquipped())){
			central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
			return;
		}
		
		if(general.BUFFCHAR_JUST_ON_PEACE_ZONE){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				if(player.getInstanceId()>0){
					if(!general.BUFFCHAR_USE_INSTANCE){
						central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
						return;						
					}
				}else{
					central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
					return;
				}
			}
		}
		
		if(!TargetPlayer){
			if(player.getSummon()==null){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NOT_HAVE_PET, player);
				return;
			}else if(player.getSummon().isDead()){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NOT_HAVE_PET, player);
				return;
			}
		}
		
		updatePrice(player);
		
		if(!opera.haveItem(player, general.BUFFCHAR_SCHEME_USE_ITEM_ID, PRICETOUSE)){
			return;
		}
		
		
		for(int IdB : this.SCHEME_BUFF){
			int Bufflvl = SCHEME_BUFF_LEVEL.get(IdB);
			String Ct =String.valueOf(IdB) + String.valueOf(Bufflvl);
			buffcommunity InfoB = general.getBuffCommnunity_individual(Integer.valueOf(Ct));
			boolean canUsePremiumBuff = false;
			premiumPersonalData PremiumData = null;			
			if( ZeuS.isPremium(player) ){
				if(opera.isPremium_Clan(player)){
					PremiumData = general.getPremiumDataFromPlayerOrClan( String.valueOf(player.getClanId()));
				}else if(opera.isPremium_Player(player)){
					PremiumData = general.getPremiumDataFromPlayerOrClan( player.getAccountName());
				}
				if(PremiumData!=null){
					canUsePremiumBuff = PremiumData.getPremiumData().canUseBuffPremium();
				}
			}
			
			if(InfoB.getReuseTime()>0) {
				if(!canUseReuseSkill(player,InfoB.getBuffID(), InfoB.getReuseTime())){
					continue;
				}
			}
			
			boolean isFlag = isValidCondition(InfoB,player, Status.FLAG);
			boolean isKarma = isValidCondition(InfoB,player, Status.KARMA);
			boolean isCombat = isValidCondition(InfoB,player, Status.COMBAT);
			if(!isFlag || !isKarma || !isCombat){
				if(ShowErrorMensaje){
					central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
					ShowErrorMensaje = false;
				}
				continue;
			}else{
				removePrice=true;
				try{
					int lvlBuff=SCHEME_BUFF_LEVEL.get(IdB);
					if((InfoB.isPremium() && canUsePremiumBuff) || (!InfoB.isPremium())){
						lvlBuff = InfoB.getBuffLevel();
					}else if(InfoB.isPremium() && !canUsePremiumBuff){
						continue;
					}
					
					if(TargetPlayer){
						if (player.isAffectedBySkill(IdB))
						{
							player.stopSkillEffects(IdB);
						}						
						SkillData.getInstance().getInfo(IdB, lvlBuff).getEffects(player, player);
					}else{
						if (player.getSummon().isAffectedBySkill(IdB))
						{
							player.getSummon().stopSkillEffects(IdB);
						}						
						SkillData.getInstance().getInfo(IdB, lvlBuff).getEffects(player.getSummon(), player.getSummon());
					}
				}catch(Exception a){
					
				}				
			}
		}
		if(removePrice){
			opera.removeItem(general.BUFFCHAR_SCHEME_USE_ITEM_ID, this.PRICETOUSE, player);
		}
		
	}	
	
	
	
	
	
	
	protected SchemeChar(){
		
	}
	
	public int getTotalBuffDanceCount(){
		int BuffTotal = getBuffCount();
		int DanceTotal = getDanceCount();
		return BuffTotal + DanceTotal;
	}
	
	public Vector<Integer> getBuffs(){
		return this.SCHEME_BUFF;
	}
	
	public int getLevelFromBuff(int idBuff){
		return this.SCHEME_BUFF_LEVEL.get(idBuff);
	}
	
	
	
	public boolean canAddMoreBuffDance(){
		int BuffC = this.getBuffCount();
		int BuffD = this.getDanceCount();
		
		if(BuffC >= Config.BUFFS_MAX_AMOUNT && BuffD >= Config.DANCES_MAX_AMOUNT){
			return false;
		}
		
		return true;
	}
	
	public int getSchemeID(){
		return this.SCHEME_ID;
	}
	
	public String getSchemeIcon(){
		return this.SCHEME_ICON;
	}
	
	public int getPrice(){
		return this.PRICETOUSE;
	}
	
	public void RemoveAll(){
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buff_char_sch WHERE id=?");
			ins.setInt(1,this.SCHEME_ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}		
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buff_char_sch_buff WHERE idSch=?");
			ins.setInt(1, this.SCHEME_ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}
	}
	
	public void setRemoveBuff(int idBuff, int idLevel){
		try{
			this.SCHEME_BUFF.removeElement(idBuff);
		}catch(Exception a){
			_log.warning("Error removing Buff from Scheme->" + a.getMessage());
		}
		try{
			this.SCHEME_BUFF_LEVEL.remove(idBuff, idLevel);
		}catch(Exception a){
			_log.warning("Error removing BuffLevel from Scheme->" + a.getMessage());
		}
		
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement("DELETE FROM zeus_buff_char_sch_buff WHERE idSch=? AND idBuff=? and lvlBuff=?");
			ins.setInt(1,this.SCHEME_ID);
			ins.setInt(2, idBuff);
			ins.setInt(3, idLevel);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}
		
		int BuffPrice = getPriceFromBuff(idBuff, idLevel);
		if((this.PRICETOUSE -= BuffPrice)>=0){
			this.PRICETOUSE -= BuffPrice;
		}
		
	}
	
	public void setNewIconScheme(String Icono){
		this.SCHEME_ICON = Icono;
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement("UPDATE zeus_buff_char_sch SET idIcon=? WHERE id=?");
			ins.setString(1, Icono);
			ins.setInt(2, this.SCHEME_ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}		
	}
	
	public void setNewNameScheme(String Nombre){
		this.SCHEME_NAME = Nombre;
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement("UPDATE zeus_buff_char_sch SET NomSch=? WHERE id=?");
			ins.setString(1, Nombre);
			ins.setInt(2, this.SCHEME_ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}
	}
	
	public String getSchemeName(){
		return this.SCHEME_NAME;
	}
	
	public boolean hasBuffOnScheme(int idBuff){
		try{
			return this.SCHEME_BUFF.contains(idBuff);
		}catch(Exception a){
			
		}
		return false;
	}
	
	private boolean isBuff(int idBuff, int Level){
		L2Skill skill = SkillData.getInstance().getInfo(idBuff,Level);
		if(skill != null) {
			if(!skill.isDance()){
				return true;
			}
		}
		return false;		
	}
	
	public int getDanceCount(){
		int contador=0;
		for(int idBuff : SCHEME_BUFF){
			if(!isBuff(idBuff,this.SCHEME_BUFF_LEVEL.get(idBuff))){
				contador++;
			}
			
		}
		return contador;
	}
	
	public int getBuffCount(){
		int contador=0;
		for(int idBuff : SCHEME_BUFF){
			if(isBuff(idBuff,this.SCHEME_BUFF_LEVEL.get(idBuff))){
				contador++;
			}
		}
		return contador;		
	}
	
	protected void loadBuffFromScheme(){
		this.PRICETOUSE = 0;
		String Consulta = "SELECT idBuff, lvlBuff,isPremium FROM zeus_buff_char_sch_buff WHERE idSch=?";
		try{
			Connection conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Consulta);
			psqry.setInt(1, this.SCHEME_ID);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						this.SCHEME_BUFF.add(rss.getInt("idBuff"));
						this.SCHEME_BUFF_LEVEL.put(rss.getInt("idBuff"), rss.getInt("lvlBuff"));
						int BuffPrice = getPriceFromBuff(rss.getInt("idBuff"), rss.getInt("lvlBuff"));
						this.PRICETOUSE += BuffPrice;
					}catch(Exception e){
						_log.warning("Error Cargando buff ->" + rss.getInt("idBuff"));
					}
				}
			conn.close();
		}catch(SQLException e){
			_log.warning("Error Cargando buff ->" + e.getMessage());
		}
	}
	
	public boolean isPremiumBuff(int idBuff, int level){
		try{
			String TID = String.valueOf(idBuff) + String.valueOf(level);
			buffcommunity InfoBuff = general.getBuffCommnunity_individual(Integer.valueOf(TID));
			return InfoBuff.isPremium();
		}catch(Exception a){
			_log.warning("Error getting Buff Premium Info->" + a.getMessage());
		}
		return false;		
	}
	
	private int getPriceFromBuff(int idBuff, int level){
		try{
			String TID = String.valueOf(idBuff) + String.valueOf(level);
			buffcommunity InfoBuff = general.getBuffCommnunity_individual(Integer.valueOf(TID));
			return InfoBuff.getPrice();
		}catch(Exception a){
			_log.warning("Error getting Buff price->" + a.getMessage());
		}
		return 0;
	}
	
	protected SchemeChar(String _schemeName, String _schemeIcon, int _schemeID){
		this.SCHEME_NAME = _schemeName;
		this.SCHEME_ICON = _schemeIcon;
		this.SCHEME_ID = _schemeID;
	}
	
	@SuppressWarnings("unused")
	protected void setBuffToScheme(L2PcInstance player, int idSkill, int level, boolean isPremium){
		int MaximoBuff = Config.BUFFS_MAX_AMOUNT;
		int MaximoDances = Config.DANCES_MAX_AMOUNT;
		int SchemeBuffHave = getBuffCount();
		int SchemeDanceHave = getDanceCount();
		if(isBuff(idSkill, level)){
			if(SchemeBuffHave >= MaximoBuff){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_SCHEME_CANT_ADD_MORE_BUFF, player);
				return;
			}
		}else{
			if(SchemeDanceHave >= MaximoDances){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_SCHEME_CANT_ADD_MORE_DANCE, player);
				return;
			}
		}
		
		
		
		
		if(isPremiumBuff(idSkill, level)){
			boolean retorno = true;
			if(opera.isPremium_Clan(player)){
				premiumPersonalData P_info = general.getPremiumDataFromPlayerOrClan(String.valueOf(player.getClanId()));
				if(P_info.isActive()){
					premiumsystem P_Data = P_info.getPremiumData();
					if(!P_Data.canUseBuffPremium()){
						retorno = false;
					}
				}else{
					retorno = false;
				}
			}
			if(retorno = false){
				if(opera.isPremium_Player(player)){
					premiumPersonalData P_info = general.getPremiumDataFromPlayerOrClan(player.getAccountName());
					if(P_info.isActive()){
						int idPremium = P_info.getIdPremiumUse();
						premiumsystem P_Data = general.getPremiumServices().get(idPremium);
						if(!P_Data.canUseBuffPremium()){
							retorno = false;
						}
					}else{
						retorno = false;
					}
				}
			}
			if(retorno=false){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_DONT_HAVE_PREMIUM_SYSTEM, player);
				return;
			}
		}		
		
		if(this.SCHEME_BUFF!=null){
			if(this.SCHEME_BUFF.contains(idSkill)){
				central.msgbox("You have this buff on this scheme, please Check.", player);
				return;
			}
		}
		
		
		
		this.SCHEME_BUFF.add(idSkill);
		this.SCHEME_BUFF_LEVEL.put(idSkill, level);
		
		this.PRICETOUSE += getPriceFromBuff(idSkill, level);
		
		this.isDonate=isPremium;
		
		String consulta = "INSERT INTO zeus_buff_char_sch_buff values (?,?,?,?)";
		
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(consulta);
			statement.setInt(1, this.SCHEME_ID);
			statement.setInt(2, idSkill);
			statement.setInt(3, level);
			statement.setString(4, isPremium ? "true" : "false");
			statement.execute();
		}catch(Exception a){
			_log.warning("Error on Save Buff on Scheme->" + a.getMessage());
		}		
	}
}

class _charBuffVariable{
	public static final Logger _log = Logger.getLogger(_charBuffVariable.class.getName());
	private Map<Integer, Integer> BUFF_DATA = new HashMap<Integer, Integer>();
	
	public boolean canUseThisBuff(int idBuff, int reuseTime) {
		int UnixNow = opera.getUnixTimeNow();
		int UnixToCheck = UnixNow + reuseTime;	
		
		if(this.BUFF_DATA != null) {
			if(this.BUFF_DATA.size()>0) {
				if(this.BUFF_DATA.containsKey(idBuff)) {
					int LastUnix = this.BUFF_DATA.get(idBuff);
					if(LastUnix > UnixNow) {
						return false;
					}else {
						this.BUFF_DATA.put(idBuff, UnixToCheck);
					}
				}else {
					this.BUFF_DATA.put(idBuff, UnixToCheck);					
				}
			}else {
				this.BUFF_DATA.put(idBuff, UnixToCheck);
			}
		}else {
			this.BUFF_DATA.put(idBuff, UnixToCheck);
		}
		return true;
	}
	
}
