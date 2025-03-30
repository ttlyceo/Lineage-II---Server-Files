package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Comunidad.EngineForm.v_Teleport;
import ZeuS.Config._teleportData;
import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class teleport {

	private static final Logger _log = Logger.getLogger(teleport.class.getName());

	public static String getTeleportConfigMain(L2PcInstance player, String Param){
		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("Config Teleport","LEVEL") + central.LineaDivisora(1);
		return HTML;
	}

	@SuppressWarnings("rawtypes")
	protected static int getIDSeccion(String Nombre){
		if(Nombre.equals("MAIN_TELEPORT")){
			return -1;
		}
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    	  	if(Temporal.getName().equalsIgnoreCase(Nombre.replace("_", " "))){
    	  		return Integer.valueOf(Entrada.getKey().toString());
    	  	}
	    }

		return -1;
	}

	@SuppressWarnings("rawtypes")
	protected static int getIDSeccion(String Nombre, String tipo){
		if(tipo.equals("MAIN_TELEPORT")){
			return -1;
		}
		
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    		
    		if(Temporal.getName().equalsIgnoreCase(Nombre.replace("_", " ")) && (Temporal.getType().equals(tipo) || ( tipo.equalsIgnoreCase("SECTION") && Temporal.getType().equalsIgnoreCase("secc") )  )  ){
				return Integer.valueOf(Entrada.getKey().toString());
			}
	    }
		
		return -1;
	}

	protected static boolean record(L2PcInstance player, int Tipo,int idSeccion, String Nombre, String TipoSeccion, String Descrip, String CanFlag, String CanKarma, int levelInter, String OnlyNoble, String dualbox){
		return record(player, Tipo, idSeccion, Nombre, TipoSeccion, Descrip, 0,0,0, CanFlag, CanKarma, levelInter, OnlyNoble,-1,dualbox);
	}

	protected static boolean record(L2PcInstance player, int Tipo,int idSeccion, String Nombre, String TipoSeccion, String Descrip, int P_X, int P_Y, int P_Z, String CanFlag, String CanKarma, int levelInter, String OnlyNoble, int IDTeleport, String dualbox){
		String Consulta = "call sp_teleport(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			psqry.setInt(1, Tipo);
			psqry.setInt(2, idSeccion);
			psqry.setString(3, Nombre.replace("_", " ").replace("'", ""));
			psqry.setString(4, TipoSeccion);
			psqry.setString(5, Descrip);
			psqry.setInt(6, P_X);
			psqry.setInt(7, P_Y);
			psqry.setInt(8, P_Z);
			psqry.setString(9, CanFlag.toLowerCase());
			psqry.setString(10, CanKarma.toLowerCase());
			psqry.setInt(11, levelInter);
			psqry.setString(12, OnlyNoble);
			psqry.setInt(13, IDTeleport);
			psqry.setString(14, dualbox);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET Teleport->"+e.getMessage());
			return false;
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET Teleport->"+a.getMessage());

		}
		if(Respu.equals("cor")){
			if(IDTeleport==-1){
				central.msgbox("Add Teleport OK", player);
			}else{
				central.msgbox("Update Teleport OK", player);
			}
			general.loadConfigsTele();
		}else{
			if(IDTeleport == -1){
				central.msgbox("Add Teleport FAIL", player);
			}else{
				central.msgbox("Update Teleport FAIL", player);
			}
			return false;
		}
		return true;
	}

	public static void bypassMain(String Parametros, L2PcInstance player){
		String Temp = Parametros.replace(" ", "#");
		int Cantidad = Temp.split("#").length;

		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("TELEPORT ZEUS ->"+Parametros+"->Largo->"+String.valueOf(Cantidad));
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.sendHtml(player, Parametros+"->Largo->"+String.valueOf(Cantidad));
		}
		if((Cantidad < 7) || (Cantidad > 7)){
			central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
			return;
		}

		String []EventParam = Parametros.split(" ");
		record(player,2,getIDSeccion(EventParam[1].replace("_", " ") ), EventParam[0],EventParam[6],"",Integer.valueOf(EventParam[2]),Integer.valueOf(EventParam[3]),Integer.valueOf(EventParam[4]),EventParam[7],EventParam[8],Integer.valueOf(EventParam[5]),EventParam[9],-1,EventParam[10]);
	}

	public static void bypassTeleport(String Parametros, L2PcInstance player){
		String Temp = Parametros.replace(" ", "#");
		int Cantidad = Temp.split("#").length;

		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("TELEPORT ZEUS ->"+Parametros+"->Largo->"+String.valueOf(Cantidad));
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.sendHtml(player, Parametros+"->Largo->"+String.valueOf(Cantidad));
		}

		String Descript ="";

		if((Cantidad < 11) || (Cantidad > 11) ){
			if(Cantidad == 12){
				if(!Temp.split("#")[1].equals("MAIN_TELEPORT")){
					central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
					return;
				}else{
					Descript = Temp.split("#")[11].replace("_", " ");
				}
			}else{
				central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
				return;
			}
		}
		String []EventParam = Parametros.split(" ");
		record(player,2,getIDSeccion(EventParam[1],"SECTION"), EventParam[0],EventParam[6],Descript,Integer.valueOf(EventParam[2]),Integer.valueOf(EventParam[3]),Integer.valueOf(EventParam[4]),EventParam[8],EventParam[7],Integer.valueOf(EventParam[5]),EventParam[9],-1,EventParam[10]);
	}

	@SuppressWarnings("rawtypes")
	public static String getDescript(String Nombre){
		String NomSeccion = "--##--";
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    		if(Temporal.getName().equalsIgnoreCase(Nombre.replace("_", " "))){
				return Temporal.getDescript();
			}
	    }
		return NomSeccion;
	}

	@SuppressWarnings("rawtypes")
	public static String getSegmento(L2PcInstance player, int idSeccion, String nomSeccion){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>" ;
		String MAIN_DESCRIPTION = getDescript(nomSeccion);

		String Cabezera = nomSeccion.replace("_", " ");

		if(!Cabezera.equalsIgnoreCase(MAIN_DESCRIPTION)){
			Cabezera += "<br1><font color=\"FFBF00\">" + MAIN_DESCRIPTION + "</font>" ;
		}

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Teleport") + central.LineaDivisora(2);
		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(Cabezera.toUpperCase() ,"LEVEL") + central.LineaDivisora(2);

		String HTML_BOTONES = "<table width=272>";

		String Bypass;
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    		boolean Registrar = true;
			if(Registrar){
				if(Temporal.getIDSecc()== idSeccion){
					if(Temporal.getType().equalsIgnoreCase("GO")){
						Bypass = "gototeleportBD " + String.valueOf(Temporal.getID()) + " " + String.valueOf(idSeccion) + " " + nomSeccion;
					}else if(Temporal.getType().equalsIgnoreCase("SECTION") || Temporal.getType().equalsIgnoreCase("SECC")){
						Bypass = "teleportshow " + String.valueOf(Temporal.getID()) + " " + Temporal.getName().replace(" ","_") + " 0";
					}else{
						Bypass = "";
					}
					HTML_BOTONES += "<tr><td width=272 align=center><button action=\"bypass -h ZeuSNPC "+ Bypass +"\" value=\""+Temporal.getName()+"\" width=225 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1></td></tr>";
				}
			}
	    }
		
		HTML_BOTONES += "</table>";
		MAIN_HTML += central.headFormat(HTML_BOTONES,"");

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat("<button action=\"bypass -h ZeuSNPC teleportMain 0 0 0\" value=\"Teleport Menu\" width=150 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(2);

		return MAIN_HTML + central.getPieHTML() + "</body></html>";
	}


	public static String adminTeleportMain(L2PcInstance player, String eventParam, boolean locAuto){
		return adminTeleport(player,eventParam,locAuto,true);
	}

	public static String adminTeleportMain(L2PcInstance player, String eventParam){
		return adminTeleport(player,eventParam,false,true);
	}


	public static String adminTeleport(L2PcInstance player, String eventParam, boolean locAuto){
		return adminTeleport(player,eventParam,locAuto,false);
	}

	public static String adminTeleport(L2PcInstance player, String eventParam){
		return adminTeleport(player,eventParam,false,false);
	}

	protected static String adminTeleport(L2PcInstance player, String eventParam, boolean LocAuto, boolean MainSection){
		return adminTeleport( player, eventParam, LocAuto, MainSection, "");
	}

	@SuppressWarnings("rawtypes")
	protected static int getIDbyName(String Nombre){
		int Retorno = -100;
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    	  	if(Temporal.getName().equalsIgnoreCase(Nombre.replace("_", " "))){
    	  		return Integer.valueOf(Entrada.getKey().toString());
    	  	}
	    }
		return Retorno;
	}

	@SuppressWarnings("rawtypes")
	public static String adminTeleport(L2PcInstance player, String eventParam, boolean LocAuto, boolean MainSection, String Section){
		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("Teleport Admin" + (MainSection? " - Main Teleport":""),"LEVEL") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("Replace spaces for _","FF8000") + central.LineaDivisora(1);

		String btnBypass1 = "bypass -h admin_zeus_op teleaddmain1";
		String BtnAdd1 = "<button value=\"REFRESH\" action=\""+ btnBypass1 +"\" width=80 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBypass2 = "bypass -h admin_zeus_op teleadd1";
		String BtnAdd2 = "<button value=\"REFRESH\" action=\""+ btnBypass2 +"\" width=80 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		if(LocAuto && MainSection){
			HTML += central.LineaDivisora(1) + central.headFormat(BtnAdd1) + central.LineaDivisora(1);
		}

		if(LocAuto && !MainSection){
			HTML += central.LineaDivisora(1) + central.headFormat(BtnAdd2) + central.LineaDivisora(1);
		}


		//admin_zeus_tele_main_auto

		String strComboLvL ="";
		String strComboBoolean = "TRUE;FALSE";

		strComboLvL = "1";

		for(int i=10;i<90;i+=10){
			if(strComboLvL.length()>0){
				strComboLvL += ";";
			}
			strComboLvL += String.valueOf(i);
		}

		String STR_SECCION_LISTA ="";

		if(MainSection){
			STR_SECCION_LISTA = "MAIN_TELEPORT";
		}


		if(!MainSection){
		    Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
			while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		_teleportData Temporal = null;
	    		Temporal = (_teleportData) Entrada.getValue();
	    		if(Temporal.getType().equals("SECTION")){
					if(STR_SECCION_LISTA.length() > 0){
						STR_SECCION_LISTA += ";";
					}
					STR_SECCION_LISTA += Temporal.getName().replace("'","").replace(" ","_");
				}
		    }
		}

		String COMBO_SECCION = Section.length()==0 ? "<combobox width=130 var=cmbseccion list="+STR_SECCION_LISTA+">" : Section;
		String COMBO_LVL = "<combobox width=40 var=cmblvl list="+strComboLvL+">";
		String COMBO_CAN_USE_FLAG = "<combobox width=76 var=cmbflag list="+strComboBoolean+">";
		String COMBO_CAN_USE_KARMA = "<combobox width=76 var=cmbkarma list="+strComboBoolean+">";
		String COMBO_CAN_USE_DUALBOX = "<combobox width=76 var=cmbdualB list="+strComboBoolean+">";
		String COMBO_ONLY_NOBLE = "<combobox width=76 var=cmbnoble list=FALSE;TRUE>";

		String COMBO_TIPO = "<combobox width=80 var=cmbtipo list=SECTION;GO;PVP_INDI;PVP_PARTY>";

		String ValX = "";
		String ValY = "";
		String ValZ = "";

		if(LocAuto){
			ValX = String.valueOf(player.getLocation().getX());
			ValY = String.valueOf(player.getLocation().getY());
			ValZ = String.valueOf(player.getLocation().getZ());
		}else{
			ValX = "<edit var=\"val_x\" width=80>";
			ValY = "<edit var=\"val_y\" width=80>";
			ValZ = "<edit var=\"val_z\" width=80>";
		}

		String ValNombre =  "<multiedit var=\"val_name\" width=160 height=24>"; //"<edit var=\"val_name\" width=160>";
		String ValDescrip = "<multiedit var=\"val_desc\" width=160 height=24>";//"<edit var=\"val_desc\" width=160>";

		String forInputCoo = "<table width=280>";
		forInputCoo += "<tr><td width=93 align=CENTER>X</td><td width=93 align=CENTER>Y</td><td width=93 align=CENTER>Z</td></tr>";
		forInputCoo += "<tr><td width=93 align=CENTER>"+ValX+"</td><td width=93 align=CENTER>"+ValY+"</td><td width=93 align=CENTER>"+ValZ+"</td></tr>";
		forInputCoo += "</table>";

		String FormatoTabla = "<table width=270><tr><td width=135>%NOM%</td><td>%PARAM%<br></td></tr></table>";

		HTML += central.LineaDivisora(1) + central.headFormat("NAME"+ValNombre+"<br>") + central.LineaDivisora(1);
		if(MainSection){
			HTML += central.LineaDivisora(1) + central.headFormat("DESCRIPT(Max. 16 Characters)"+ValDescrip+"<br>") + central.LineaDivisora(1);
		}
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "SECTION").replace("%PARAM%", MainSection ? "MAIN_TELEPORT" : COMBO_SECCION) ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "LEVEL").replace("%PARAM%", COMBO_LVL) ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "FLAG PLAYER CAN USE").replace("%PARAM%", COMBO_CAN_USE_FLAG) ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "CAN USE KARMA PLAYER").replace("%PARAM%", COMBO_CAN_USE_KARMA)  ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "TYPE").replace("%PARAM%", COMBO_TIPO)) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "ONLY NOBLE").replace("%PARAM%", COMBO_ONLY_NOBLE)) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(FormatoTabla.replace("%NOM%", "CAN ENTER DUALBOX").replace("%PARAM%", COMBO_CAN_USE_DUALBOX)) + central.LineaDivisora(1);

		HTML += central.LineaDivisora(1) + central.headFormat(forInputCoo+"<br>") + central.LineaDivisora(1);

		String Locationn = "$val_x $val_y $val_z";

		if(LocAuto){
			Locationn = ValX + " " + ValY +" " + ValZ;
		}

		String tipoSeccion = "";

		if(MainSection){
			tipoSeccion = "MAIN_TELEPORT";
		}else if(Section.length()>0){
			tipoSeccion = Section;
		}else if(Section.length()==0){
			tipoSeccion = "$cmbseccion";
		}

		String BotonAgregar_bypass = "bypass -h voice .z_t $val_name "+tipoSeccion+" "+Locationn+" $cmblvl $cmbtipo $cmbkarma $cmbflag $cmbnoble $cmbdualB" + (MainSection ? " $val_desc":"") ;

		String BtnAdd = "<button value=\"ADD\" action=\""+ BotonAgregar_bypass +"\" width=180 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		HTML += central.LineaDivisora(2) + central.headFormat(BtnAdd) + central.LineaDivisora(2);
		//z_t
		//action=\"bypass -h voice .z_t $PIN_INGRESO\"

		if(Section.length()>0){
			String Bypass = "secc " + getIDbyName(Section) + " " + Section.replace(" ","_") + " 0";
			String btnBack = "<button action=\"bypass -h voice .z_t_a "+ Bypass +"\" value=\"GO BACK\" width=125 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		}

		HTML += central.getPieHTML() + "</body></html>";

		return HTML;
	}

	public static String CentralHTML(L2PcInstance player){
		return CentralHTML(player, false);
	}
	

	@SuppressWarnings("rawtypes")
	public static String CentralHTML(L2PcInstance player, boolean ForEdit){
		String btnBypass1 = "bypass -h admin_zeus_op teleaddmain1";
		String BtnAdd1 = "<button value=\"ADD TO MAIN WINDOWS (Auto)\" action=\""+ btnBypass1 +"\" width=220 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnBypass2 = "bypass -h admin_zeus_op teleaddmain2";
		String BtnAdd2 = "<button value=\"ADD TO MAIN WINDOWS (Manual)\" action=\""+ btnBypass2 +"\" width=220 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>" ;
		if(ForEdit){
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Teleport Main Config"+BtnAdd1+BtnAdd2) + central.LineaDivisora(2);
		}else{
			MAIN_HTML += central.LineaDivisora(2) + central.headFormat("Teleport") + central.LineaDivisora(2);
		}

		boolean Registrar;
		
		
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
		while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    		Registrar = true;
			if(Temporal.isJustForNoble() && !player.isNoble()){
				Registrar = false;
			}
			if(Temporal.canUseFlagPlayer() && (player.getPvpFlag()>0)){
				Registrar = false;
			}
			if(Temporal.canUsePkPlayer() && (player.getKarma()>0 ) ){
				Registrar = false;
			}
			if(Registrar || ForEdit){
				if(Temporal.getIDSecc() == -1  && (player.getLevel() >= Temporal.minLevelToUse())){
					if(Temporal.getType().equalsIgnoreCase("go")){
						if(!ForEdit){
							MAIN_HTML += GeneraHTML_Portada(Temporal.getName(), "gototeleport " + Temporal.getStrCoordenates() ,Temporal.getDescript());
						}else{
							MAIN_HTML += GeneraHTML_Portada_Edit(Temporal.getName() ,"go", Temporal.getID() ,Temporal.getPosition(), Temporal.getType() , String.valueOf(Temporal.getIDSecc()));
						}
					}else{
						if(!ForEdit){
							MAIN_HTML += GeneraHTML_Portada(Temporal.getName(), "teleportshow " + String.valueOf(Temporal.getID()) + " " + Temporal.getName().replace(" ","_") + " 0" , Temporal.getDescript());
						}else{
							MAIN_HTML += GeneraHTML_Portada_Edit(Temporal.getName(),"secc " + String.valueOf(Temporal.getID()) + " " + Temporal.getName().replace(" ","_") + " 0", Temporal.getID(), Temporal.getPosition(), Temporal.getType(), String.valueOf(Temporal.getIDSecc()));
						}
					}
				}
			}
	    }
		MAIN_HTML += central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	protected static String GeneraHTML_Portada_Edit(String Nombre, String bypass, int IDTeleport, int Posi, String tipoSeccion, String IDSeccion){
		String btnUp = "<button value=\"\" action= \"bypass -h voice .z_t_a move up %IDTELEPORT% %Posi% %TS% %IDS%\" width=16 height=16 back=\"L2UI_CH3.shortcut_nextv_over\" fore=\"L2UI_CH3.shortcut_nextv_over\">";
		String btnDown = "<button value=\"\" action= \"bypass -h voice .z_t_a move down %IDTELEPORT% %Posi% %TS% %IDS%\" width=16 height=16 back=\"L2UI_CH3.shortcut_prevv_down\" fore=\"L2UI_CH3.shortcut_prevv_down\">";
		String btnSupri = "<button value=\"SUPR\" action= \"bypass -h voice .z_t_a suprshow %IDTELEPORT% %TS% %IDS%\" width=50 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnModif = "<button value=\"MODIF\" action= \"bypass -h voice .z_t_a forEdit %IDTELEPORT% true %TS% %IDS%\" width=50 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String Retorno = "<table bgcolor=151515 width=270><tr>";
		Retorno += "<td width=120 align=LEFT><button action=\"bypass -h voice .z_t_a "+ bypass +"\" value=\""+Nombre+"\" width=120 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1></td>";
		Retorno += "<td width=50 align=rigth>"+btnModif.replace("%IDTELEPORT%", String.valueOf(IDTeleport)).replace("%Posi%", String.valueOf(Posi)).replace("%TS%", tipoSeccion).replace("%IDS%", IDSeccion) +"</td>";
		Retorno += "<td width=16 align=rigth>"+btnUp.replace("%IDTELEPORT%", String.valueOf(IDTeleport)).replace("%Posi%", String.valueOf(Posi)).replace("%TS%", tipoSeccion).replace("%IDS%", IDSeccion) +"</td>";
		Retorno += "<td width=16 align=rigth>"+btnDown.replace("%IDTELEPORT%", String.valueOf(IDTeleport)).replace("%Posi%", String.valueOf(Posi)).replace("%TS%", tipoSeccion).replace("%IDS%", IDSeccion) +"</td>";
		Retorno += "<td width=50 align=rigth>"+btnSupri.replace("%IDTELEPORT%", String.valueOf(IDTeleport)).replace("%Posi%", String.valueOf(Posi)).replace("%TS%", tipoSeccion).replace("%IDS%", IDSeccion) +"</td>";
		Retorno += "</tr></table>";
		return Retorno;
	}


	protected static String GeneraHTML_Portada(String Nombre, String bypass, String Descrip){
		String[]ColorFondo = {"63AA1C","AB7B1C"};
		//INT_PINTAR_GRILLA
		String Retorno = "";
		Retorno += "<table bgcolor=151515 width=280><tr><td width=120 align=LEFT>";
		Retorno += "<button action=\"bypass -h ZeuSNPC "+ bypass +"\" value=\""+Nombre+"\" width=120 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1>";
		Retorno += "</td><td width=160 align=rigth><font color="+ ColorFondo[central.INT_PINTAR_GRILLA(0)] +">"+Descrip+"</font></td></tr></table>";
		return Retorno;
	}

	protected static boolean exeModifTeleport(String tipo, String idTeleport, String Accion){
		String Consulta = "call sp_teleport_opera(?,?,?)";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			psqry.setInt(1,Integer.valueOf(tipo));
			psqry.setInt(2,Integer.valueOf(idTeleport));
			psqry.setString(3, Accion);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET Teleport->"+e.getMessage());
			return false;
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET Teleport->"+a.getMessage());

		}
		if(Respu.equals("cor")){
			general.loadConfigsTele();
		}else{
			return false;
		}
		return true;
	}

	public static void bypassMain_edit(String params, L2PcInstance activeChar) {
		//central.msgbox(params, activeChar);
		String[] eventParam = params.split(" ");
		String Opera ="";
		if(eventParam[0].equals("move")){
			if(eventParam[1].equals("down")){
				Opera = "DOWN";
			}else if(eventParam[1].equals("up")){
				Opera = "UP";
			}
			if(exeModifTeleport("1", eventParam[2] , Opera)){
				central.msgbox("Changed Teleport Button Position", activeChar);
			}else{
				central.msgbox("Error Changed Teleport Button Position", activeChar);
			}
			String HTML = "";
			if(eventParam[5].equals("-1")){
				HTML = CentralHTML(activeChar,true);
			}else{
				HTML  = EditSeccion(eventParam,eventParam[5], v_Teleport.getNomSeccion(eventParam[5]));
				opera.enviarHTML(activeChar, HTML);
			}

			opera.enviarHTML(activeChar, HTML);
		}else if(eventParam[0].equals("secc")){
			String HTML  = EditSeccion(eventParam,"","");
			opera.enviarHTML(activeChar, HTML);
		}else if(eventParam[0].equals("go")){
			String HTML = teleport.CentralHTML(activeChar,true);
			opera.enviarHTML(activeChar, HTML);
		}else if(eventParam[0].equals("central")){
			String HTML = CentralHTML(activeChar,true);
			opera.enviarHTML(activeChar, HTML);
		}else if(eventParam[0].equals("supr")){
			//action= \"bypass -h voice .z_t_a supr %IDTELEPORT% %TS% %IDS%\"
			setSuprimirTele(eventParam[1]);
			String HTML = CentralHTML(activeChar,true);
			opera.enviarHTML(activeChar, HTML);
		}else if(eventParam[0].equals("suprshow")){
			showDeleteWindows(eventParam[1],activeChar);
		}else if(eventParam[0].equals("main")){
			String HTML = CentralHTML(activeChar,true);
			opera.enviarHTML(activeChar, HTML);
		}else if(eventParam[0].equals("forEdit")){
			getWindowsModif(activeChar,eventParam[1],Boolean.parseBoolean(eventParam[2]));
		}else if(eventParam[0].equals("tele_update")){
			if((eventParam.length>15) || (eventParam.length<15)){
				opera.enviarHTML(activeChar, language.getInstance().getMsg(activeChar).TYPING_ERROR);
			}else{
				//record(L2PcInstance player, int Tipo,int idSeccion, String Nombre, String TipoSeccion, String Descrip, int P_X, int P_Y, int P_Z,
				//String CanFlag, String CanKarma, int levelInter, String OnlyNoble, int IDTeleport){
				record(activeChar, 4, -1, eventParam[2].replace("_", " "), eventParam[8].toLowerCase(), eventParam[12].replace("_", " "),
						Integer.valueOf(eventParam[4]), Integer.valueOf(eventParam[5]), Integer.valueOf(eventParam[6]), eventParam[10],
						eventParam[9], Integer.valueOf(eventParam[7]), eventParam[11], Integer.valueOf(eventParam[1]),eventParam[14]);
				/*AQUI*/
			}
		}
	}

	@SuppressWarnings("rawtypes")
	protected static void showDeleteWindows(String idTeleport,L2PcInstance player){
		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat("DELETE TELEPORT") + central.LineaDivisora(1);
		String idTele="",nom="",descrip="",tipo="",coordenadas="",forNoble="",canGOFlag="", canGOKarma="",lvlUP="";
		Iterator itr = v_Teleport.getTeleportData() .entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		_teleportData Temporal = null;
    		Temporal = (_teleportData) Entrada.getValue();
    		if(Entrada.getKey().toString().equals(idTeleport)){
				idTele = Entrada.getKey().toString();
				nom = Temporal.getName();
				descrip = Temporal.getDescript();
				tipo = Temporal.getType();
				coordenadas = Temporal.getStrCoordenates();
				forNoble = String.valueOf(Temporal.isJustForNoble());
				canGOFlag = String.valueOf(Temporal.canUseFlagPlayer());
				canGOKarma = String.valueOf(Temporal.canUsePkPlayer());
				lvlUP = String.valueOf(Temporal.minLevelToUse());
			}
	    }
		

		HTML += central.LineaDivisora(1) + central.headFormat("<font color=WHITE>NAME: </font>" + nom + "<font color=WHITE>   Type: </font>" + tipo.toUpperCase(),"LEVEL");
		HTML += central.headFormat("<font color=WHITE>Descrip: </font>" + descrip + "<font color=WHITE>   LVL MIN: </font>" + lvlUP,"LEVEL");
		HTML += central.headFormat("<font color=WHITE>Location: </font>" + coordenadas,"LEVEL");
		HTML += central.headFormat("<font color=WHITE>Noble: </font>" + forNoble.toUpperCase() + "<font color=WHITE>  Flag: </font>" + canGOFlag.toUpperCase() + "<font color=WHITE>  Karma: </font>"+canGOKarma.toUpperCase(),"LEVEL");
		String btnSupri = "<button value=\"SUPRIMIR\" action= \"bypass -h voice .z_t_a supr " + idTele +" 0 0\" width=150 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnMain = "<button value=\"MAIN\" action= \"bypass -h voice .z_t_a supr " + idTele +" 0 0\" width=150 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		HTML += central.LineaDivisora(1) + central.headFormat(btnSupri+btnMain) + central.LineaDivisora(1);
		HTML += central.getPieHTML() + "</center></body></html>";
		opera.enviarHTML(player, HTML);
	}

	protected static void setSuprimirTele(String idTeleport){
		exeModifTeleport("2",idTeleport,"");
	}

	protected static String EditSeccion(String[] eventParam, String idSeccion, String NombreSec){
		if(NombreSec.length()==0){
			NombreSec = eventParam[2].replace("_", " ");
		}

		String btnBypass1 = "bypass -h admin_zeus_op telesection1 " + NombreSec.toUpperCase().replace(" ", "_");
		String BtnAdd1 = "<button value=\"ADD TELEPORT (Auto)\" action=\""+ btnBypass1 +"\" width=220 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBypass2 = "bypass -h admin_zeus_op telesection2 " + NombreSec.toUpperCase().replace(" ", "_");
		String BtnAdd2 = "<button value=\"ADD TELEPORT (Manual)\" action=\""+ btnBypass2 +"\" width=220 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";


		String btnMain = "<button action=\"bypass -h voice .z_t_a central \" value=\" Go Back \" width=125 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String HTML = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("Config Teleport - " + NombreSec) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat(BtnAdd1 +  BtnAdd2) + central.LineaDivisora(1);
		HTML += getBtnSub_modif( idSeccion.length()==0?eventParam[1]:idSeccion) + central.LineaDivisora(1);
		HTML += central.headFormat(btnMain) + central.getPieHTML() + "</body></html>";
		return HTML;
	}

	@SuppressWarnings("rawtypes")
	protected static String getBtnSub_modif(String idSeccion){
		String HTML_BOTONES = "<table width=280>";
		String Bypass;
		String btnUp = "<button value=\"\" action= \"bypass -h voice .z_t_a move up %IDTELEPORT% %Posi% %TS% %IDS%\" width=16 height=16 back=\"L2UI_CH3.shortcut_nextv_over\" fore=\"L2UI_CH3.shortcut_nextv_over\">";
		String btnDown = "<button value=\"\" action= \"bypass -h voice .z_t_a move down %IDTELEPORT% %Posi% %TS% %IDS%\" width=16 height=16 back=\"L2UI_CH3.shortcut_prevv_down\" fore=\"L2UI_CH3.shortcut_prevv_down\">";
		String btnSupri = "<button value=\"SUPR\" action= \"bypass -h voice .z_t_a suprshow %IDTELEPORT% %TS% %IDS%\" width=50 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnModif = "<button value=\"MODIF\" action= \"bypass -h voice .z_t_a forEdit %IDTELEPORT% false %TS% %IDS%\" width=50 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		
		
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		_teleportData Temporal = null;
	    		Temporal = (_teleportData) Entrada.getValue();
	    		if(Temporal.getIDSecc() == Integer.valueOf(idSeccion)){
					if(Temporal.getType().equals("go")){
						Bypass = "go ";
					}else{
						Bypass = "secc " + Entrada.getKey().toString() + " " + Temporal.getName().replace(" ","_") + " 0";
					}
					HTML_BOTONES += "<tr>";
					HTML_BOTONES += "<td width=130><button action=\"bypass -h voice .z_t_a "+ Bypass +"\" value=\""+ Temporal.getName() +"\" width=125 height=21 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"><br1></td>";
					HTML_BOTONES += "<td width=50>"+ btnModif.replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%Posi%", Temporal.getStrCoordenates()).replace("%TS%", Temporal.getType()).replace("%IDS%", String.valueOf(Temporal.getIDSecc())) +"</td>";
					HTML_BOTONES += "<td width=16>"+ btnUp.replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%Posi%", Temporal.getStrCoordenates()).replace("%TS%", Temporal.getType()).replace("%IDS%", String.valueOf(Temporal.getIDSecc())) +"</td>";
					HTML_BOTONES += "<td width=16>"+ btnDown.replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%Posi%", Temporal.getStrCoordenates()).replace("%TS%", Temporal.getType()).replace("%IDS%", String.valueOf(Temporal.getIDSecc())) +"</td>";
					HTML_BOTONES += "<td width=50>"+ btnSupri.replace("%IDTELEPORT%", String.valueOf(Temporal.getID())).replace("%Posi%", Temporal.getStrCoordenates()).replace("%TS%", Temporal.getType()).replace("%IDS%", String.valueOf(Temporal.getIDSecc())) +"</td>";
					HTML_BOTONES += "</tr>";
				}
	    }		
		
		
		
		HTML_BOTONES += "</table>";
		return HTML_BOTONES;
	}

	@SuppressWarnings("rawtypes")
	protected static void getWindowsModif(L2PcInstance player, String idTeleport, boolean isMainWindows){

		_teleportData TeleSelec = null;

		
		
		Iterator itr = v_Teleport.getTeleportData().entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		_teleportData Temporal = null;
	    		Temporal = (_teleportData) Entrada.getValue();
				if(Temporal.getID() == Integer.valueOf(idTeleport)){
					TeleSelec = Temporal;
				}
	    }	
		
		
		String strComboLvL ="";
		String strComboTrueFalse = "TRUE;FALSE";
		String strComboFalseTrue = "FALSE;TRUE";
/*		String strComboSectionGo = "SECTION;GO;PVP_INDI;PVP_PARTY";
		
		String strComboSectionShow = TeleSelec.getType().toUpperCase();
		
		for(String partes : strComboSectionGo.split(";")){
			if(!partes.equalsIgnoreCase(TeleSelec.getType())){
				strComboSectionShow += ";" + partes;	
			}
		}*/
		

		if(TeleSelec.minLevelToUse() == 1){
			strComboLvL = "1";
		}else{
			strComboLvL = String.valueOf(TeleSelec.minLevelToUse()) + ";1";
		}

		for(int i=10;i<90;i+=10){
			if(!(TeleSelec.minLevelToUse() == i)){
				if(strComboLvL.length()>0){
					strComboLvL += ";";
				}
				strComboLvL += String.valueOf(i);
			}
		}

		String COMBO_LVL = "<combobox width=40 var=cmblvl list="+strComboLvL+">";
		String COMBO_CAN_USE_FLAG = "<combobox width=76 var=cmbflag list="+ (TeleSelec.canUseFlagPlayer() ? strComboTrueFalse : strComboFalseTrue) +">";
		String COMBO_CAN_USE_KARMA = "<combobox width=76 var=cmbkarma list="+(TeleSelec.canUsePkPlayer() ? strComboTrueFalse : strComboFalseTrue)+">";
		String COMBO_ONLY_NOBLE = "<combobox width=76 var=cmbnoble list="+ ( TeleSelec.isJustForNoble() ? strComboTrueFalse : strComboFalseTrue ) +">";
		String COMBO_CAN_USE_DUALBOX = "<combobox width=76 var=cmbdualB list="+ ( TeleSelec.canUseDualPlayer() ? strComboTrueFalse : strComboFalseTrue ) +">";

		String ValX = "<edit var=\"val_x\" width=80>";
		String ValY = "<edit var=\"val_y\" width=80>";
		String ValZ = "<edit var=\"val_z\" width=80>";

		String ValNombre =  "<multiedit var=\"val_name\" width=160 height=24>"; //"<edit var=\"val_name\" width=160>";
		String ValDescrip = "<multiedit var=\"val_desc\" width=160 height=24>";//"<edit var=\"val_desc\" width=160>";

		String[] Posi = TeleSelec.getStrCoordenates().split(" ");

		String forInputCoo = "<table width=280>";
		forInputCoo += "<tr><td width=93 align=CENTER>X="+Posi[0]+"</td><td width=93 align=CENTER>Y="+Posi[1]+"</td><td width=93 align=CENTER>Z="+Posi[2]+"</td></tr>";
		forInputCoo += "<tr><td width=93 align=CENTER>"+ValX+"</td><td width=93 align=CENTER>"+ValY+"</td><td width=93 align=CENTER>"+ValZ+"</td></tr>";
		forInputCoo += "</table>";

		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat("MODIF TELEPORT " + TeleSelec.getName().replace("_", " ").toUpperCase())+central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("NAME: " + TeleSelec.getName().replace(" ", "_") +ValNombre ) + central.LineaDivisora(1);
		if(isMainWindows){
			HTML += central.LineaDivisora(1) + central.headFormat("DESCRIPTION:<br1> " + TeleSelec.getDescript().replace(" ", "_") +ValDescrip) + central.LineaDivisora(1);
		}
		HTML += central.LineaDivisora(1) + central.headFormat("LEVEL: " + String.valueOf(TeleSelec.minLevelToUse()) + COMBO_LVL ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("FLAG PLAYER CAN USE: " + String.valueOf(TeleSelec.canUseFlagPlayer()) + COMBO_CAN_USE_FLAG ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("KARMA PLAYER CAN USE: " + String.valueOf(TeleSelec.canUsePkPlayer()) + COMBO_CAN_USE_KARMA ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("TYPE: " + TeleSelec.getType()) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("ONLY NOBLE: " + String.valueOf(TeleSelec.isJustForNoble()) + COMBO_ONLY_NOBLE ) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("CAN ENTER DUALBOX: " + String.valueOf(TeleSelec.canUseDualPlayer()) + COMBO_CAN_USE_DUALBOX ) + central.LineaDivisora(1);

		String Locationn ="";
		if(TeleSelec.getType().equals("go")){
			HTML += central.LineaDivisora(1) + central.headFormat("Let 0 if you dont change coordinates.<br1>" + forInputCoo) + central.LineaDivisora(1);
			Locationn = "$val_x $val_y $val_z";
		}


		String descrip = "";
		if(isMainWindows){
			descrip = "$val_desc";
		}else{
			descrip = "none";
		}
		String BotonAgregar_bypass = "bypass -h voice .z_t_a tele_update "+ String.valueOf(TeleSelec.getID()) +" $val_name "+ TeleSelec.getType().replace(" ", "_") + " "+Locationn+" $cmblvl $cmbtipo $cmbkarma $cmbflag $cmbnoble "+ descrip + " " + (isMainWindows ? "TRUE":"FALSE") + " $cmbdualB";
		String BtnAdd = "<button value=\"UPDATE\" action=\""+ BotonAgregar_bypass +"\" width=180 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		HTML += central.LineaDivisora(1) + central.headFormat(BtnAdd) + central.LineaDivisora(1);
		HTML += central.getPieHTML() + "</center></body></html>";
		opera.enviarHTML(player, HTML);

	}

}
