package ZeuS.admin;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Config.general;
import ZeuS.event.RaidBossEvent;
import ZeuS.interfase.central;
import ZeuS.interfase.htmls;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;


public class menu {
	String[] cmbBoolean = {"true","false"};
	public  final static String bypassNom = "admin_zeus_config";
	public static final Logger _log = Logger.getLogger(menu.class.getName());
	protected final static String[] operadoresNumericos = {"1","2","291","292","293","280","279","271","272","268","254","249","244","221","222","223","224","225",
			"226","227","228","229","230","200","201","202","203","204","205","206","207","208","209","180","181","182","183","184",
			"185","186","187","188","189","175","171","169","166","164","162","160","158","156","154","152","150","148","146","143",
			"146","143","142","139","138","136","134","132","129","128","121","118","116","115","114","113","110","108","103","102","97",
			"90","87","81","80","79","78","77","76","73","4","3","294","295","296","299","308","311","316","317","323","324","350","351",
			"354","356","357","380","382","390","391","392","393","394","395","396","397","398","401","403","405","406","416","417","418","419","458","481",
			"463","464","466","467","468","471","482","484","485","492","493","495","496","497","498","503","504","506","510","516","517","519","523", "524","527","529",
			"543","544","545","546","547","548","549","550","551", "554", "555", "557", "558", "566", "572","573","574","575","576","577","578","579","580","581","582","583","584", "590","591","592", "600","608"};
	
	public final static String[] LeyendaComunidad = {"GM:38","DEATH:45","HERO:83","C.LEADER:83","V.I.P:83","NORMAL:83","IN_PVP:83","P.STORE:83","KARMA (PK):83","JAILED:83"};
	
	private final static Vector<Integer> VectorOnlyNumeros = new Vector<Integer>();


	private static void CreatedFilter(){
		for(String operador: operadoresNumericos){
			VectorOnlyNumeros.add(Integer.valueOf(operador));
		}
	}

	private static String crearBoton(String Nombre, String Link){
		return "<button value=\""+Nombre+"\" action=\"bypass -h "+ bypassNom +" "+Link+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	}

	public static String getBtnbackConfig(){
		return central.LineaDivisora(2) + central.headFormat("<button value=\"Main Config\" action=\"bypass -h "+ bypassNom +" Config 2 0 0\" width=100 height=28 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">","WHITE") + central.LineaDivisora(2);
	}

	public static String getConfigMenu(L2PcInstance player){
		if(!opera.isMaster(player)){
			central.msgbox(language.getInstance().getMsg(player).YOU_CANT_ACCESS_TO_THIS_SECTION, player);
			return "";
		}

		if(VectorOnlyNumeros.size()==0){
			CreatedFilter();
		}

		Vector<String> Botones= new Vector<String>();

		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(3) + central.headFormat("AIO Config") + central.LineaDivisora(3);
		MAIN_HTML += central.LineaDivisora(1) + central.LineaDivisora(2) + central.headFormat("Select Area to Edit") + central.LineaDivisora(2) + central.LineaDivisora(1);

		Botones.add(crearBoton("Config","setConfig1 general 0 0 0 0 0"));
		Botones.add(crearBoton("Engine Buttons","GetHTMConfig 3 0 0 0 0"));
		Botones.add(crearBoton("Engine Status","GetHTMConfig 4 0 0 0 0"));
		Botones.add(crearBoton("Vote","setConfig1 vote 0 0 0 0 0"));
		Botones.add(crearBoton("Teleport","setConfig1 teleport 0 0 0 0 0"));
		Botones.add(crearBoton("Instance Zone","setConfig1 instancezone 0 0 0 0 0"));
		Botones.add(crearBoton("The challenge","setConfig1 desafio 0 0 0 0 0"));
		Botones.add(crearBoton("Drop Search","setConfig1 dropsear 0 0 0 0 0"));
		Botones.add(crearBoton("Go Party Leader","setConfig1 partyfin 0 0 0 0 0"));
		Botones.add(crearBoton("Go Flag","setConfig1 flagfin 0 0 0 0 0"));
		Botones.add(crearBoton("Color Name","setConfig1 colorname 0 0 0 0 0"));
		Botones.add(crearBoton("Augment Special","setConfig1 augmentspe 0 0 0 0 0"));
		Botones.add(crearBoton("Enchant Special","setConfig1 enchantspe 0 0 0 0 0"));
		Botones.add(crearBoton("Elemental Special","setConfig1 elementalspe 0 0 0 0 0"));
		Botones.add(crearBoton("Raid Boss Info","setConfig1 bossinfo 0 0 0 0 0"));
		Botones.add(crearBoton("Miscellaneous","setConfig1 opcvarias 0 0 0 0 0"));
		Botones.add(crearBoton("Change Names","setConfig1 changeName 0 0 0 0 0"));
		Botones.add(crearBoton("PvP Config","setConfig1 pvpConfig 0 0 0 0 0"));
		Botones.add(crearBoton("Auto Pots","setConfig1 autopots 0 0 0 0 0"));
		Botones.add(crearBoton("Raid Annou.","setConfig1 raidconfig 0 0 0 0 0"));
		Botones.add(crearBoton("Cancel Buff","setConfig1 cancelbuff 0 0 0 0 0"));
		Botones.add(crearBoton("Transform","setConfig1 transform 0 0 0 0 0"));
		Botones.add(crearBoton("Olympics","setConfig1 Olymp 0 0 0 0 0"));
		Botones.add(crearBoton("Antibot","setConfig1 Antibot 0 0 0 0 0"));
		Botones.add(crearBoton("IP Ban","setConfig1 banip 0 0 0 0 0"));
		Botones.add(crearBoton("Over Enchant","setConfig1 overenchant 0 0 0 0 0"));
		Botones.add(crearBoton("Dual Box","setConfig1 dualbox 0 0 0 0 0"));
		Botones.add(crearBoton("Chat Config","setConfig1 ChatConfig 0 0 0 0 0"));		
		Botones.add(crearBoton("Castle Manager","setConfig1 castlema 0 0 0 0 0"));
		Botones.add(crearBoton("Dressme","setConfig1 dressme 0 0 0 0 0"));
		Botones.add(crearBoton("Community B.","setConfig1 comunidad 0 0 0 0 0"));
		Botones.add(crearBoton("CB. Party Finder","setConfig1 cbpartyfinder 0 0 0 0 0"));
		Botones.add(crearBoton("Email Register","setConfig1 emailregister 0 0 0 0 0"));
		Botones.add(crearBoton("Raid Boss Event","setConfig1 RaidBossEvent 0 0 0 0 0"));
		Botones.add(crearBoton("Town War Event","setConfig1 TownWarEvent 0 0 0 0 0"));
		Botones.add(crearBoton("Jail Bail System","setConfig1 jailbail 0 0 0 0 0"));
		Botones.add(crearBoton("Load Config","setConfig1 loadConfig 0 0 0 0 0"));
		String Grilla ="<table width=260><tr>";
		int Contador =0;
		for(String btn:Botones){
			Contador++;
			Grilla += "<td width=130 align=CENTER>"+btn+"</td>";
			if(((Contador%2)==0) && (Contador!=0)){
				Grilla+= "</tr><tr>";
			}
		}

		if(!Grilla.endsWith("</tr>")){
			Grilla +="</tr>";
		}

		Grilla += "</table>";

		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(Grilla) + central.LineaDivisora(1);
		MAIN_HTML += central.getPieHTML() + "</body></html>";

		return MAIN_HTML;
	}
	private static String setFilaModif(String titulo, String Valor, String Seccion, String idBox){
		String temp = "<table width=260><tr><td width=200 align=LEFT fixwidth=200>"+titulo+"</td><td width=60><button value=\"Modif\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+" "+idBox+" 0 0 0 0\" width=55 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>" +
				"<tr><td width=260 align=LEFT><font color=A4A4A4>" + (Valor.isEmpty() || (Valor==null) ? "":"Actual: "+Valor)+"</font></td></tr></table><br>";
		return central.LineaDivisora(1) + central.headFormat(temp);
	}

	@SuppressWarnings("unused")
	private static String setFilaModif_SinFormarto(String titulo, String Valor, String Seccion, String idBox){
		String temp = "<tr><td width=200 align=LEFT fixwidth=200>"+titulo+"</td><td width=60><button value=\"Modif\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+" "+idBox+" 0 0 0 0\" width=55 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr>" +
				"<tr><td width=260 align=LEFT><font color=A4A4A4>" + (Valor.isEmpty() || (Valor==null) ? "":"Actual: "+Valor)+"</font></td></tr>";
		return central.LineaDivisora(1) + central.headFormat(temp);
	}

	private static String getEditComboBox(String titulo, String idINBD, String Seccion, String ListaCombo){
		String temp = central.headFormat(titulo,"LEVEL");
		temp += "<center><table width=260><tr>" +
				"<td width=210 align=LEFT><combobox width=210 var=valor list="+ListaCombo+"></td>" +
				"<td align=center width=55><button value=\"OK\" action=\"bypass -h " + bypassNom + " setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\" width=50 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" +
				"</tr></table></center><br>";
		return central.headFormat(temp);
	}

	private static String getEditBigBox(String titulo, String idINBD, String Seccion){
		String temp = central.headFormat(titulo,"LEVEL");
		temp += "<center><table width=260><tr>" +
				"<td width=210 align=LEFT><multiedit var=\"valor\" width=210 ></td>" +
				"<td align=center width=55><button value=\"OK\" action=\"bypass -h " + bypassNom + " setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\" width=50 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>" +
				"</tr></table></center><br>";
		return central.headFormat(temp);
	}


	private static String getEditBox(String titulo, String idINBD, String Seccion){
		String temp = central.headFormat(titulo,"LEVEL");
		temp += "<center><table width=200><tr><td width=145 align=LEFT><edit var=\"valor\" width=110></td><td align=center width=55><button value=\"OK\" action=\"bypass -h " + bypassNom + " setConfig1 setValor "+idINBD+" $valor "+Seccion+" 0 0\" width=50 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center>";
		return central.headFormat(temp);
	}

	private static String getEditLista_StringInteger(String titulo,String ListaStr, String Seccion, String idINBD, boolean _showOnly){
		
		Vector<Integer> Lista = new Vector<Integer>();
		
		if(ListaStr.length()>0){
			for (String SpitL : ListaStr.split(",")){
				Lista.add(Integer.valueOf(SpitL));
			}
		}
		
		return getEditLista_VectorInteger(titulo,Lista,Seccion,idINBD,_showOnly);
	}	
	
	
	private static String getEditLista_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD, boolean _showOnly){
		String btnBack = "<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+ " 0 0 0 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat(titulo, "LEVEL") + central.LineaDivisora(1);
		HTML += getEditLista_VectorInteger(titulo, Lista, Seccion, idINBD,"1");
		HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		HTML += getCerrarTabla();
		return HTML;
	}

	@SuppressWarnings("unused")
	private static String getEditLista_VectorString(String titulo,Vector<String> Lista, String Seccion, String idINBD, boolean _showOnly){
		String btnBack = "<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+ " 0 0 0 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat(titulo, "LEVEL") + central.LineaDivisora(1);
		//HTML += getEditLista_VectorInteger(titulo, Lista, Seccion, idINBD,"1");
		HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		HTML += getCerrarTabla();
		return HTML;
	}
	
	private static String getEditLista_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD){
		return getEditLista_VectorInteger(titulo, Lista, Seccion, idINBD,"0");
	}
	
	private static String getEditLista_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD, String idShow){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>Ingrese Valor</td>" +
				"<td width=120 align=LEFT><edit type=\"number\" var=\"idIngr\" width=110></td>" +
				"</tr></table><center>"+
				"<button value=\"add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $idIngr "+idINBD+" "+Seccion+" 0 "+idShow+"\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp + getListaIndividual_INTEGER(Lista, Seccion, idINBD));
		return MAIN_HTML;
	}

	private static String getEditListaSkill_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>ID Skill</td>" +
				"<td width=80 align=LEFT></td>" +				
				"<td width=120 align=LEFT><edit type=\"number\" var=\"idIngr\" width=110></td>" +
				"</tr></table><center>"+
				"<button value=\"add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $idIngr "+idINBD+" "+Seccion+" 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp + getListaIndividual_NPC_INTEGER(Lista, Seccion, idINBD));
		return MAIN_HTML;
	}

	private static String getEditListaNPC_VectorInteger(String titulo,Vector<Integer> Lista, String Seccion, String idINBD){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>ID NPC</td>" +
				"<td width=80 align=LEFT></td>" +				
				"<td width=120 align=LEFT><edit type=\"number\" var=\"idIngr\" width=110></td>" +
				"</tr></table><center>"+
				"<button value=\"add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $idIngr "+idINBD+" "+Seccion+" 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp + getListaIndividual_NPC_INTEGER(Lista, Seccion, idINBD));
		return MAIN_HTML;
	}

	private static String getColorMenu(String titulo,String ListaColor, String Seccion, String idINBD){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>Ingrese Color (HTML)</td>" +
				"<td width=120 align=LEFT><edit var=\"Color\" width=110></td></tr></table><center>"+
				"<button value=\"Add\" action=\"bypass -h " + bypassNom + " setConfig1 setLista $Color "+idINBD+" "+Seccion+" 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";

		MAIN_HTML = central.headFormat(temp + getColores(ListaColor, Seccion, idINBD));
		return MAIN_HTML;
	}

	/*Aqui*/

	@SuppressWarnings("unused")
	private static String getEditCombo(String titulo,String List, String Seccion, String idINBD){
		String Combo = "<combobox width=120 var=cmbSelecc list="+ List + " >";
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>Select</td>" +
				"<td width=120 align=LEFT>"+ Combo +"</td>" +
				"</tr></table><center><br>"+
				"<button value=\"Save\" action=\"bypass -h " + bypassNom + " setConfig1 setCombo $cmbSelecc 0 "+idINBD+" "+Seccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
		MAIN_HTML = central.headFormat(temp);
		return MAIN_HTML;
	}

	private static String getEdititem(String titulo,String premios, String Seccion, String idINBD, boolean _onlyItem){
		String btnBack = "<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 "+Seccion+ " 0 0 0 0 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String MAIN_HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(titulo,"LEVEL") + central.LineaDivisora(1); 
		MAIN_HTML += getEdititem(titulo,premios,Seccion,idINBD,"1");
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnBack) + central.LineaDivisora(1);
		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getEdititem(String titulo,String premios, String Seccion, String idINBD){
		return getEdititem(titulo,premios,Seccion,idINBD,"0");
	}
	
	private static String getEdititem(String titulo,String premios, String Seccion, String idINBD, String tipoVentana){
		String MAIN_HTML = "";
		String temp = "<center>" +
				"<table width=200>" +
				"<tr>" +
				"<td width=80 align=LEFT>ID Item</td>" +
				"<td width=120 align=LEFT><edit type=\"number\" var=\"idItem\" width=110></td>" +
				"</tr><tr>" +
				"<td width=80 align=LEFT>Amount</td>" +
				"<td width=120 align=LEFT><edit type=\"number\" var=\"Cantidad\" width=110></td></tr></table><center><br>"+
				"<button value=\"Add\" action=\"bypass -h " + bypassNom + " setConfig1 setProducto $idItem $Cantidad "+idINBD+" "+Seccion+" "+tipoVentana+"\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";

		MAIN_HTML = central.headFormat(temp + getPremiosLista(premios, Seccion, idINBD));
		return MAIN_HTML;
	}

	private static String setFilaBoolean(String Titulo, boolean estado, String Seccion, String idINBD){
		String color = (estado ? "94FF8B" : "FF8B8B");
		Titulo = "<font color="+ color +">"+ Titulo +"</font>";
		String Boton = "<button value=\""+ (estado?"YES":"NO") + "\" action=\"bypass -h " + bypassNom + " setConfig1 setstatus "+Seccion+" "+idINBD+" 0 0 0\" width=43 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String HTML = "<table width=270><tr><td width=225 fixwidth=220>"+Titulo+"</td><td width=45>"+Boton+"</td></tr></table>";
		return central.LineaDivisora(1) + central.headFormat(HTML);
	}

	private static String getCerrarTabla(){
		return getBtnbackConfig() + central.getPieHTML() + "</body></html>";
	}


	private static String getConfigRegister(L2PcInstance player,String idBox){
		//('465','REGISTER_EMAIL_ONLINE','false'), ('466','REGISTER_NEW_PLAYER_WAITING_TIME','60'), ('467','REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME','10'), ('468','REGISTER_NEW_PlAYER_TRIES','3')
		String inSeccion = "emailregister";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Email Register")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Email Register Active", general.REGISTER_EMAIL_ONLINE , inSeccion, "465");
		MAIN_HTML += setFilaBoolean("Chat block while data is entered", general.REGISTER_NEW_PLAYER_BLOCK_CHAT , inSeccion, "477");
		MAIN_HTML += setFilaBoolean("Check the email have banned account", general.REGISTER_NEW_PLAYER_CHECK_BANNED_ACCOUNT , inSeccion, "478");

		MAIN_HTML += setFilaModif("Register New Player Waiting Time", String.valueOf(general.REGISTER_NEW_PLAYER_WAITING_TIME ), inSeccion, "466");
		if(idBox.equals("466")){
			MAIN_HTML +=getEditBox("Register New Player Waiting Time (Second's)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Register Re Enter world Waiting Time", String.valueOf(general.REGISTER_NEW_PLAYER_RE_ENTER_WAITING_TIME ), inSeccion, "467");
		if(idBox.equals("467")){
			MAIN_HTML +=getEditBox("Register Re Enter world Waiting Time (Second's)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Register Max. Tries", String.valueOf(general.REGISTER_NEW_PlAYER_TRIES), inSeccion, "468");
		if(idBox.equals("468")){
			MAIN_HTML +=getEditBox("Register Max. Tries", idBox, inSeccion);
		}

		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}




	private static String getConfigIPBan(L2PcInstance player, String idBox){
		String inSeccion = "banip";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("IPBan Config.")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("BanIp Active", general.BANIP_STATUS , inSeccion, "306");
		MAIN_HTML += setFilaBoolean("BanIp Check Wan IP from Cliente", general.BANIP_CHECK_IP_INTERNET , inSeccion, "304");
		MAIN_HTML += setFilaBoolean("BanIP Check Lan IP from Cliente", general.BANIP_CHECK_IP_RED , inSeccion, "305");
		MAIN_HTML += setFilaBoolean("BanIP Disconnect all player with the<br1>same IP Wan/Lan", general.BANIP_DISCONNECT_ALL_PLAYER , inSeccion, "307");

		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getConfigLoad(L2PcInstance player, String idBox){
		String inSeccion = "loadConfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Load Config.")+central.LineaDivisora(2);

		String BTN_load = "<button value=\"Load\" action=\"bypass -h " + bypassNom + " setConfig1 _loadConfig %LOAD% 0 "+idBox+" "+inSeccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";

		MAIN_HTML += central.LineaDivisora(1)+central.headFormat("Load All Config<br>" + BTN_load.replace("%LOAD%", "load_all")+"<br>","LEVEL") + central.LineaDivisora(1);

		MAIN_HTML += central.LineaDivisora(1)+central.headFormat("Load All Dropsearch Data<br>" + BTN_load.replace("%LOAD%", "load_dropsearch")+"<br>","LEVEL") + central.LineaDivisora(1);
		
		MAIN_HTML += central.LineaDivisora(1)+central.headFormat("Load Raidboss Event autoevent<br>" + BTN_load.replace("%LOAD%", "load_raidbossevent")+"<br>","LEVEL") + central.LineaDivisora(1);

		MAIN_HTML +=  getCerrarTabla();

		return MAIN_HTML;
	}
	
	private static String getConfigChat(L2PcInstance player, String idBox){
		String inSeccion = "ChatConfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Chat Config.")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Chat All Block", general.CHAT_GENERAL_BLOCK , inSeccion, "618");
		MAIN_HTML += setFilaModif("Chat All Need Level", String.valueOf(general.CHAT_GENERAL_NEED_LEVEL) , inSeccion, "620");
		if(idBox.equals("620")){
			MAIN_HTML+= getEditBox("Chat All Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat All Need Lifetime", String.valueOf(general.CHAT_GENERAL_NEED_LIFETIME) , inSeccion, "621");
		if(idBox.equals("621")){
			MAIN_HTML+= getEditBox("Chat All Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat All Need PvP", String.valueOf(general.CHAT_GENERAL_NEED_PVP) , inSeccion, "619");
		if(idBox.equals("619")){
			MAIN_HTML+= getEditBox("Chat All Need PvP (-1 = Disabled)", idBox,inSeccion);
		}		
		
		MAIN_HTML += setFilaBoolean("Chat Shout (!) Block", general.CHAT_SHOUT_BLOCK , inSeccion, "540");
		MAIN_HTML += setFilaModif("Chat Shout Need Level", String.valueOf(general.CHAT_SHOUT_NEED_LEVEL) , inSeccion, "544");
		if(idBox.equals("544")){
			MAIN_HTML+= getEditBox("Chat Shout Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Shout Need Lifetime", String.valueOf(general.CHAT_SHOUT_NEED_LIFETIME) , inSeccion, "545");
		if(idBox.equals("545")){
			MAIN_HTML+= getEditBox("Chat Shout Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Shout Need PvP", String.valueOf(general.CHAT_SHOUT_NEED_PVP) , inSeccion, "543");
		if(idBox.equals("543")){
			MAIN_HTML+= getEditBox("Chat Shout Need PvP (-1 = Disabled)", idBox,inSeccion);
		}
		
		
		MAIN_HTML += setFilaBoolean("Chat Trade (+) Block", general.CHAT_TRADE_BLOCK , inSeccion, "541");
		MAIN_HTML += setFilaModif("Chat Trade Need Level", String.valueOf(general.CHAT_TRADE_NEED_LEVEL) , inSeccion, "547");
		if(idBox.equals("547")){
			MAIN_HTML+= getEditBox("Chat Trade Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Trade Need Lifetime", String.valueOf(general.CHAT_TRADE_NEED_LIFETIME) , inSeccion, "548");
		if(idBox.equals("548")){
			MAIN_HTML+= getEditBox("Chat Trade Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Trade Need PvP", String.valueOf(general.CHAT_TRADE_NEED_PVP) , inSeccion, "546");
		if(idBox.equals("546")){
			MAIN_HTML+= getEditBox("Chat Trade Need PvP (-1 = Disabled)", idBox,inSeccion);
		}	

		MAIN_HTML += setFilaBoolean("Chat Wisp (\") Block", general.CHAT_WISP_BLOCK , inSeccion, "542");
		MAIN_HTML += setFilaModif("Chat Wisp Need Level", String.valueOf(general.CHAT_WISP_NEED_LEVEL) , inSeccion, "550");
		if(idBox.equals("550")){
			MAIN_HTML+= getEditBox("Chat Wisp Need Level (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Wisp Need Lifetime", String.valueOf(general.CHAT_WISP_NEED_LIFETIME) , inSeccion, "551");
		if(idBox.equals("551")){
			MAIN_HTML+= getEditBox("Chat Wisp Need Lifetime (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Chat Wisp Need PvP", String.valueOf(general.CHAT_WISP_NEED_PVP) , inSeccion, "549");
		if(idBox.equals("549")){
			MAIN_HTML+= getEditBox("Chat Wisp Need PvP (-1 = Disabled)", idBox,inSeccion);
		}
		MAIN_HTML +=  getCerrarTabla();
		
		return MAIN_HTML;
	}
	

	private static String getConfigDualBox(L2PcInstance player, String idBox) {
		String inSeccion = "dualbox";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Dual Box Config.")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("IP Check", general.MAX_IP_CHECK , inSeccion, "400");
		MAIN_HTML += setFilaModif("Max IP Normal Player allow", String.valueOf(general.MAX_IP_COUNT),inSeccion,"401");
		if(idBox.equals("401")){
			MAIN_HTML+= getEditBox("Max IP Nomal Player allow", idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Max IP Premium Player allow", String.valueOf(general.MAX_IP_VIP_COUNT),inSeccion,"403");
		if(idBox.equals("403")){
			MAIN_HTML+= getEditBox("Max IP Premium Player allow", idBox,inSeccion);
		}
		//MAIN_HTML += setFilaBoolean("IP Check Recording Data", general.MAX_IP_RECORD_DATA , inSeccion, "402");
		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;

	}



	private static String getConfigOly(L2PcInstance player, String idBox){
		String inSeccion = "Olymp";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Olympics Config.")+ central.LineaDivisora(1);
		
		MAIN_HTML += setFilaBoolean("Olympiad is allow", general.OLY_ALLOW , inSeccion, "604");
		
		MAIN_HTML += setFilaBoolean("Olympiad Timer", general.ZEUS_OLY_COUNTER , inSeccion, "900");
		
		MAIN_HTML += setFilaBoolean("Olympiad Show Dmg", general.ZEUS_OLY_SHOW_DMG , inSeccion, "901");
		
		MAIN_HTML += setFilaBoolean("Announce the Battle Result to Averyone", general.OLY_ANNOUNCE_RESULTS , inSeccion, "605");
		
		MAIN_HTML += setFilaBoolean("Announce Opponent Class", general.ANNOUCE_CLASS_OPONENT_OLY , inSeccion, "250");
		MAIN_HTML += setFilaBoolean("Show the Opponent Name", general.OLY_ANTIFEED_SHOW_NAME_OPPO , inSeccion, "285");
		
		MAIN_HTML += setFilaBoolean("Show Scheme Buffer Windows", general.OLY_CAN_USE_SCHEME_BUFFER , inSeccion, "597");
		
		MAIN_HTML += menu.setFilaBoolean("Show Class in Title", general.OLY_ANTIFEED_SHOW_IN_TITLE_CLASS, inSeccion, "610");
        
		MAIN_HTML += menu.setFilaBoolean("Show Class in Name", general.OLY_ANTIFEED_SHOW_IN_NAME_CLASS, inSeccion, "381");
		
		MAIN_HTML += setFilaBoolean("Change template from Char in Olys", general.OLY_ANTIFEED_CHANGE_TEMPLATE , inSeccion, "282");
		MAIN_HTML += setFilaBoolean("Show the Name in NPC", general.OLY_ANTIFEED_SHOW_NAME_NPC , inSeccion, "283");
		
		MAIN_HTML += setFilaBoolean("Block Dualbox IP", general.OLY_DUAL_BOX_CONTROL , inSeccion, "528");
		
		MAIN_HTML += setFilaModif("Seconds to inform Opponent class", "", inSeccion, "284");
		if(idBox.equals("284")){
			MAIN_HTML += getEditLista_VectorInteger("Seconds to inform Opponent class",passVectorFromArray_int(general.OLY_SECOND_SHOW_OPPONET),inSeccion,idBox);
		}
		MAIN_HTML += setFilaModif("ID Access to use Oly Admin Command", "", inSeccion, "286");
		if(idBox.equals("286")){
			MAIN_HTML += getEditLista_VectorInteger("ID Access to use Oly Admin Command<br1>Default: 127, 8",passVectorFromArray_int(general.OLY_ACCESS_ID_MODIFICAR_POINT),inSeccion,idBox);
		}
		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}


	private static String getConfigTransform(L2PcInstance player, String idBox){
		String inSeccion = "transform";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Transformations Config.")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Noble can use it only", general.TRANSFORMATION_NOBLE, inSeccion, "253");

		MAIN_HTML += setFilaBoolean("Special transformations enabled", general.TRANSFORMATION_ESPECIALES, inSeccion, "256");

		MAIN_HTML += setFilaBoolean("RaidBoss transformations enabled", general.TRANSFORMATION_RAIDBOSS, inSeccion, "257");
		
		MAIN_HTML += setFilaBoolean("Special & RaidBoss Trans. with Time", general.TRANSFORM_TIME, inSeccion, "515");

		MAIN_HTML += setFilaModif("Transformation time (Minutes)", String.valueOf(general.TRANSFORM_TIME_MINUTES), inSeccion, "516");
		if(idBox.equals("516")){
			MAIN_HTML += getEditBox("Transformation time (Minutes)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Transformation Reuse time (Minutes)", String.valueOf(general.TRANSFORM_REUSE_TIME_MINUTES), inSeccion, "517");
		if(idBox.equals("517")){
			MAIN_HTML += getEditBox("Transformation Reuse time (Minutes)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.TRANSFORMATION_LVL), inSeccion, "254");
		if(idBox.equals("254")){
			MAIN_HTML += getEditBox("Enter from which level can be used", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Enter the price for normal Transformations", "" , inSeccion, "255");
		if(idBox.equals("255")){
			MAIN_HTML += getEdititem("Enter the cost to use this service", general.TRANSFORMATION_PRICE, inSeccion, "255");
		}

		MAIN_HTML += setFilaModif("Enter the price for Special Transformations", "" , inSeccion, "259");
		if(idBox.equals("259")){
			MAIN_HTML += getEdititem("Enter the cost to use this service", general.TRANSFORMATION_ESPECIALES_PRICE, inSeccion, "259");
		}

		MAIN_HTML += setFilaModif("Enter the price for Raidboss Transformations", "" , inSeccion, "260");
		if(idBox.equals("260")){
			MAIN_HTML += getEdititem("Enter the cost to use this service", general.TRANSFORMATION_RAIDBOSS_PRICE, inSeccion, "260");
		}


		MAIN_HTML +=  getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getConfigTownWarEvent(L2PcInstance player, String idBox){
		String inSeccion = "TownWarEvent";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Town War Event")+ central.LineaDivisora(1);
		
		MAIN_HTML += setFilaBoolean( "Town War Autoevent" ,general.EVENT_TOWN_WAR_AUTOEVENT,inSeccion,"553");
		
		MAIN_HTML += setFilaBoolean( "Give PvP Reward from PVP Sytem" ,general.EVENT_TOWN_WAR_GIVE_PVP_REWARD,inSeccion,"559");
		
		MAIN_HTML += setFilaBoolean( "Give Reward to the Top Killer" ,general.EVENT_TOWN_WAR_GIVE_REWARD_TO_TOP_PPL_KILLER,inSeccion,"560");
		
		MAIN_HTML += setFilaBoolean( "Town War Event on Random City" ,general.EVENT_TOWN_WAR_RANDOM_CITY,inSeccion,"563");
		
		MAIN_HTML += setFilaBoolean( "Check Dual Box" ,general.EVENT_TOWN_WAR_DUAL_BOX_CHECK ,inSeccion,"564");
		
		MAIN_HTML += setFilaBoolean( "Block Selected NPC" ,general.EVENT_TOWN_WAR_HIDE_NPC ,inSeccion,"565");
		
		MAIN_HTML += setFilaBoolean( "Can use Buffer" ,general.EVENT_TOWN_WAR_CAN_USE_BUFFER ,inSeccion,"567");
				 
		//('566','EVENT_TOWN_WAR_NPC_ID_HIDE','')

		
		
		
		MAIN_HTML += setFilaModif("Town for the Event: ", general.EVENT_TOWN_WAR_CITY_ON_WAR, inSeccion, "556");
		if(idBox.equals("556")){
			String ZONESS = "GLUDIN,GLUDIO,DION,GIRAN,OREN,H.VILLAGE,ADEN,"
					+ "GODDARD,RUNE,HEINE,SCHUTTGART";
			String ComboZones = "";
			for(String Zns : ZONESS.split(",")){
				if(!Zns.equalsIgnoreCase(general.EVENT_TOWN_WAR_CITY_ON_WAR)){
					if(ComboZones.length()>0){
						ComboZones+=";";
					}
					ComboZones += Zns;
				}
			}			
			MAIN_HTML += getEditComboBox("Select Town for the event",idBox,inSeccion,ComboZones);
		}
		
		MAIN_HTML += setFilaModif("Minutes to Start After Server R.", String.valueOf(general.EVENT_TOWN_WAR_MINUTES_START_SERVER),inSeccion,"554");
		if(idBox.equals("554")){
			MAIN_HTML += getEditBox("Minutes to Start After Server R.", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Minutes Interval", String.valueOf(general.EVENT_TOWN_WAR_MINUTES_INTERVAL),inSeccion,"555");
		if(idBox.equals("555")){
			MAIN_HTML += getEditBox("Minutes Interval", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Minutes Duration", String.valueOf(general.EVENT_TOWN_WAR_MINUTES_EVENT),inSeccion,"557");
		if(idBox.equals("557")){
			MAIN_HTML += getEditBox("Minutes Duration", idBox, inSeccion);
		}
		
		
		MAIN_HTML += setFilaModif("Minutes To Wait Start", String.valueOf(general.EVENT_TOWN_WAR_JOIN_TIME),inSeccion,"558");
		if(idBox.equals("558")){
			MAIN_HTML += getEditBox("Minutes o Wait Start", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("General Reward", "", inSeccion, "561");
		if(idBox.equals("561")){
			//MAIN_HTML += getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox);
			opera.enviarHTML(player, getEdititem("Town War General Reward",general.EVENT_TOWN_WAR_REWARD_GENERAL,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Top player killing Reward", "", inSeccion, "562");
		if(idBox.equals("562")){
			//MAIN_HTML += getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox);
			opera.enviarHTML(player, getEdititem("Top Player Killing Reward",general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("ID NPC to Block", "", inSeccion, "566");
		if(idBox.equals("566")){
			opera.enviarHTML(player,getEditLista_StringInteger("ID NPC to Block",general.EVENT_TOWN_WAR_NPC_ID_HIDE,inSeccion,idBox,true));
			return "";
			//MAIN_HTML += getEditListaNPC_VectorInteger("NPC For Search.",general.EVENT_RAIDBOSS_RAID_ID ,inSeccion,idBox);
		}
		
		return MAIN_HTML + getCerrarTabla();
	}
	
	
	
	
	//getConfigRaidBossEvent(player,idBox);
	private static String getConfigRaidBossEvent(L2PcInstance player, String idBox){
		String inSeccion = "RaidBossEvent";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Raid Boss Event")+ central.LineaDivisora(1);
		
		MAIN_HTML += setFilaBoolean( "Raid Boss Autoevent" ,general.EVENT_RAIDBOSS_AUTOEVENT,inSeccion,"508");
		
		MAIN_HTML += setFilaModif("Raid Boss Event Winner Reward", "", inSeccion, "490");
		if(idBox.equals("490")){
			//MAIN_HTML += getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox);
			opera.enviarHTML(player, getEdititem("Raid Boss Event Winner Reward",general.EVENT_RAIDBOSS_REWARD_WIN,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Event looser Reward", "", inSeccion, "491");
		if(idBox.equals("491")){
			opera.enviarHTML(player,getEdititem("Raid Boss Event Looser Reward",general.EVENT_RAIDBOSS_REWARD_LOOSER,inSeccion,idBox,true));
			return "";
		}		

		MAIN_HTML += setFilaModif("Raid Boss Spawn Position (x,y,z)",general.EVENT_RAIDBOSS_RAID_POSITION,inSeccion, "487");
		if(idBox.equals("487")){
			MAIN_HTML += getEditBigBox("Raid Boss Spawn Position (x,y,z)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Player Spawn Position (x,y,z)",general.EVENT_RAIDBOSS_PLAYER_POSITION,inSeccion, "488");
		if(idBox.equals("488")){
			MAIN_HTML += getEditBigBox("Player Spawn Position (x,y,z)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Min. Level", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_LEVEL),inSeccion,"492");
		if(idBox.equals("492")){
			MAIN_HTML += getEditBox("Raid Boss Min. Level", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Max. Level", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_LEVEL),inSeccion,"493");
		if(idBox.equals("493")){
			MAIN_HTML += getEditBox("Raid Boss Max. Level", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Min. Register Player to begin", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MIN_REGIS),inSeccion,"494");
		if(idBox.equals("494")){
			MAIN_HTML += getEditBox("Raid Boss Min. Register Player to begin", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Max. Register Player", String.valueOf(general.EVENT_RAIDBOSS_PLAYER_MAX_REGIS),inSeccion,"495");
		if(idBox.equals("495")){
			MAIN_HTML += getEditBox("Raid Boss Max. Register Player", idBox, inSeccion);
		}		
		MAIN_HTML += setFilaModif("Raid Boss Join Time (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_JOINTIME),inSeccion,"497");
		if(idBox.equals("497")){
			MAIN_HTML += getEditBox("Raid Boss Join Time (Minutes)", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Event Time (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_EVENT_TIME),inSeccion,"498");
		if(idBox.equals("498")){
			MAIN_HTML += getEditBox("Raid Boss Event Time (Minutes)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Second Teleport into the RB Event.", String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_TELEPORT_RADIBOSS),inSeccion,"504");
		if(idBox.equals("504")){
			MAIN_HTML += getEditBox("Raid Boss Second Teleport into the RB Event.", idBox, inSeccion);
		}		
				
		MAIN_HTML += setFilaModif("Raid Boss Second to return back to City", String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_BACK),inSeccion,"496");
		if(idBox.equals("496")){
			MAIN_HTML += getEditBox("Raid Boss Second to return back to City", idBox, inSeccion);
		}
		MAIN_HTML += setFilaModif("Raid Boss Second to Revive", String.valueOf(general.EVENT_RAIDBOSS_SECOND_TO_REVIVE),inSeccion,"503");
		if(idBox.equals("503")){
			MAIN_HTML += getEditBox("Raid Boss Second to come back to the village", idBox, inSeccion);
		}		
		MAIN_HTML += setFilaModif("Raid Boss HTML color Name", general.EVENT_RAIDBOSS_COLORNAME ,inSeccion,"499");
		if(idBox.equals("499")){
			MAIN_HTML += getEditBox("Raid Boss HTML color Name", idBox, inSeccion);
		}
		MAIN_HTML += setFilaBoolean( "Raid Boss immobilize when teleport" ,general.EVENT_RAIDBOSS_PLAYER_INMOBIL,inSeccion,"489");
		MAIN_HTML += setFilaBoolean( "Raid Boss Check Dual Box" ,general.EVENT_RAIDBOSS_CHECK_DUALBOX,inSeccion,"500");
		MAIN_HTML += setFilaBoolean( "Raid Boss Cancel Buff" ,general.EVENT_RAIDBOSS_CANCEL_BUFF,inSeccion,"501");
		MAIN_HTML += setFilaBoolean( "Raid Boss Unsummon pet's" ,general.EVENT_RAIDBOSS_UNSUMMON_PET,inSeccion,"502");
		
		MAIN_HTML += setFilaModif("Raid Boss Event ID NPC", "", inSeccion, "486");
		if(idBox.equals("486")){
			opera.enviarHTML(player,getEditLista_VectorInteger("Raid Boss Event ID NPC",general.EVENT_RAIDBOSS_RAID_ID,inSeccion,idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Event Interval (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_MINUTE_INTERVAL),inSeccion,"523");
		if(idBox.equals("523")){
			MAIN_HTML += getEditBox("Raid Boss Event Interval (Minutes)", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Raid Boss Event Interval on Start Server (Minutes)", String.valueOf(general.EVENT_RAIDBOSS_MINUTE_INTERVAL_START_SERVER),inSeccion,"524");
		if(idBox.equals("524")){
			MAIN_HTML += getEditBox("Raid Boss Event Interval (Minutes)", idBox, inSeccion);
		}		
		
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigCancelBuff(L2PcInstance player, String idBox){
		String inSeccion = "cancelbuff";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";

		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Cance Buff Recovery System")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean( "Restore Canceled Buff Activated" ,general.RETURN_BUFF,inSeccion,"353");

		MAIN_HTML += setFilaBoolean( "Restore Canceled Buff In Olys" ,general.RETURN_BUFF_IN_OLY,inSeccion,"355");

		MAIN_HTML += setFilaModif("Seconds it takes to restore Canceled Buff", String.valueOf(general.RETURN_BUFF_SECONDS_TO_RETURN),inSeccion,"354");
		if(idBox.equals("354")){
			MAIN_HTML += getEditBox("Seconds it takes to restore Canceled Buff", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Seconds it takes to restore Canceled Buff on Olys", String.valueOf(general.RETURN_BUFF_IN_OLY_SECONDS_TO_RETURN),inSeccion,"356");
		if(idBox.equals("356")){
			MAIN_HTML += getEditBox("Seconds it takes to restore Canceled Buff on Olys", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("ID Buff that can not be stolen with Steal Divinity", "", inSeccion, "357");
		if(idBox.equals("357")){
			MAIN_HTML += getEditListaSkill_VectorInteger("ID Buff that can not be stolen with Steal Divinity",general.RETURN_BUFF_NOT_STEALING ,inSeccion,idBox);
		}

		//_log.warning(MAIN_HTML + getCerrarTabla());
		return MAIN_HTML + getCerrarTabla();
		
	}

	private static String getConfigRaidAnnoucement(L2PcInstance player, String idBox){
		String inSeccion = "raidconfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Raid Boss Config.")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Announce death and a Raid Boss respawn", general.ANNOUCE_RAID_BOS_STATUS, inSeccion, "241");

		MAIN_HTML += setFilaModif("ID of announcement", String.valueOf(general.RAID_ANNOUCEMENT_ID_ANNOUCEMENT), inSeccion, "244");
		if(idBox.equals("244")){
			MAIN_HTML += getEditBox("Use ID: <br1>Annoucement=10<br1>Party Room Commander=15<br1>Critical Announce=18<br1>Alliance=9", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Message Raid Boss Die", String.valueOf(general.RAID_ANNOUCEMENT_DIED), inSeccion, "242");
		if(idBox.equals("242")){
			MAIN_HTML += getEditBigBox("Input the message when the Raid Boss dies<br1>Use: %RAID_NAME% Raid Boss Name<br1>%DATE% Respawn Date", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Message Raid Boss Respawn", String.valueOf(general.RAID_ANNOUCEMENT_LIFE), inSeccion, "243");
		if(idBox.equals("243")){
			MAIN_HTML += getEditBigBox("Input the Message when Raid Boss Respawn <br1>Use: %RAID_NAME% Raid Boss Name", idBox, inSeccion);
		}


		MAIN_HTML += getCerrarTabla();
		return MAIN_HTML;
	}

	private static String getConfigPKColor(L2PcInstance player, String idBox){
		String inSeccion = "pkColor";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("PK Color System Config.")+ central.LineaDivisora(1);

		for(int i=210;i<=219;i++){
			MAIN_HTML += setFilaModif("Color Title For "+ String.valueOf(i-209), "<font color="+general.TITLE_COLOR_FOR_ALL[i-210]+">THIS("+general.TITLE_COLOR_FOR_ALL[i-210]+")</font>", inSeccion, String.valueOf(i));
			if(idBox.equals(String.valueOf(i))){
				MAIN_HTML += getEditBox("Input the PK Title Color HTML Code " + String.valueOf(i-209), idBox, inSeccion);
			}
		}
		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvppkColor 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigPVPColor(L2PcInstance player, String idBox){
		String inSeccion = "pvpColor";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("PvP Color System Config")+ central.LineaDivisora(1);

		for(int i=190;i<=199;i++){
			MAIN_HTML += setFilaModif("Color Name For "+ String.valueOf(i-189), "<font color="+general.NAME_COLOR_FOR_ALL[i-190]+">THIS("+general.NAME_COLOR_FOR_ALL[i-190]+")</font>", inSeccion, String.valueOf(i));
			if(idBox.equals(String.valueOf(i))){
				MAIN_HTML += getEditBox("Input the PvP Name Color HTML Code " + String.valueOf(i - 189), idBox, inSeccion);
			}
		}
		String BotonAtras = central.LineaDivisora(3) + central.headFormat("<button value=\"Back\" action=\"bypass -h " + bypassNom + " setConfig1 pvppkColor 0 0 0 0 0\" width=85 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">") + central.LineaDivisora(3);
		MAIN_HTML += BotonAtras + getCerrarTabla();
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigPVPPKColor(L2PcInstance player, String idBox){
		String inSeccion ="pvppkColor";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("PVP Color Sytem Config")+ central.LineaDivisora(1);

		String btnCantidadPVP = "<button value=\"PvP Amount\" action=\"bypass -h " + bypassNom + " setConfig1 pvpColorCantidad 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnCantidadPK = "<button value=\"PK Amount\" action=\"bypass -h " + bypassNom + " setConfig1 pkColorCantidad 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnColorPVP = "<button value=\"PvP Color Name\" action=\"bypass -h " + bypassNom + " setConfig1 pvpColor 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnColorPK = "<button value=\"PK Color Title\" action=\"bypass -h " + bypassNom + " setConfig1 pkColor 0 0 0 0 0\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String botonConf = "<table width=260><tr><td width=130 align=center>"+btnCantidadPVP+"</td><td width=130 align=center>"+btnCantidadPK+"</td></tr></table>";
		botonConf += "<table width=260><tr><td width=130 align=center>"+btnColorPVP+"</td><td width=130 align=center>"+btnColorPK+"</td></tr></table>";

		MAIN_HTML += central.LineaDivisora(3) + central.headFormat(botonConf) + central.LineaDivisora(3);

		MAIN_HTML += setFilaBoolean("PvP Color Name System",general.PVP_COLOR_SYSTEM_ENABLED,inSeccion,"178");
		MAIN_HTML += setFilaBoolean("PK Color Title System",general.PK_COLOR_SYSTEM_ENABLED,inSeccion,"179");
		MAIN_HTML += getCerrarTabla();
		return MAIN_HTML;

	}

	private static String getConfigChangeName(L2PcInstance player, String idBox){
		String inSeccion = "changeName";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Name Change Char / Clan")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Change Name Cost", "", inSeccion, "125");
		if(idBox.equals("125")){
			MAIN_HTML += getEdititem("Change Name Cost",general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Only nobles can change Name", general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE, inSeccion, "127");

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL),inSeccion,"128");
		if(idBox.equals("128")){
			MAIN_HTML += getEditBox("Min level to change name", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Change Clan Name Cost", "", inSeccion, "126");
		if(idBox.equals("126")){
			MAIN_HTML += getEdititem("Change Clan Name Cost",general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Min. Clan LvL to Change Name", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL),inSeccion,"129");
		if(idBox.equals("129")){
			MAIN_HTML += getEditBox("Min. Clan LvL to Change Name", idBox, inSeccion);
		}	

		
		return MAIN_HTML + getCerrarTabla();
	}
	

	private static String getConfigVarios(L2PcInstance player, String idBox){
		String inSeccion = "opcvarias";
		//TODO: falta el boton de la segunda configuracion
		String LinkSecondConfig = "setConfig1 opcvarias2 0 0 0 0 0";
		String btnSecondConfig = "<button value=\"Config 2\" action=\"bypass -h " + bypassNom + " "+LinkSecondConfig+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Miscellaneous Config")+ central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnSecondConfig) + central.LineaDivisora(1);		

		MAIN_HTML += setFilaBoolean("Change Gender", general.OPCIONES_CHAR_SEXO, inSeccion, "261");
		MAIN_HTML += setFilaModif("Change Gender Cost", "", inSeccion, "122");
		if(idBox.equals("122")){
			MAIN_HTML += getEdititem("Change Gender Cost",general.OPCIONES_CHAR_SEXO_ITEM_PRICE ,inSeccion,idBox);
		}		

		MAIN_HTML += setFilaBoolean("Noble", general.OPCIONES_CHAR_NOBLE, inSeccion, "262");
		MAIN_HTML += setFilaModif("Noble Cost", "", inSeccion, "123");
		if(idBox.equals("123")){
			MAIN_HTML += getEdititem("Noble Cost",general.OPCIONES_CHAR_NOBLE_ITEM_PRICE ,inSeccion,idBox);
		}		

		MAIN_HTML += setFilaBoolean("LvL 85", general.OPCIONES_CHAR_LVL85, inSeccion, "263");
		MAIN_HTML += setFilaModif("lvl 85 Cost", "", inSeccion, "124");
		if(idBox.equals("124")){
			MAIN_HTML += getEdititem("lvl 85 Cost",general.OPCIONES_CHAR_LVL85_ITEM_PRICE ,inSeccion,idBox);
		}		

		MAIN_HTML += setFilaBoolean("Char AIO Buffer", general.OPCIONES_CHAR_BUFFER_AIO, inSeccion, "264");
		MAIN_HTML += setFilaModif("AIO Buffer Cost", "", inSeccion, "265");
		if(idBox.equals("265")){
			MAIN_HTML += getEdititem("AIO Buffer Cost",general.OPCIONES_CHAR_BUFFER_AIO_PRICE ,inSeccion,idBox);
		}
		
		MAIN_HTML += setFilaBoolean("Char AIO +30 Buffer", general.OPCIONES_CHAR_BUFFER_AIO_30, inSeccion, "585");
		MAIN_HTML += setFilaModif("AIO +30 Buffer Cost", "", inSeccion, "586");
		if(idBox.equals("586")){
			MAIN_HTML += getEdititem("AIO+30 Buffer Cost",general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30 ,inSeccion,idBox);
		}
		
		MAIN_HTML += setFilaModif("Min. LvL to Create AIO Buffer", String.valueOf(general.OPCIONES_CHAR_BUFFER_AIO_LVL),inSeccion,"268");
		if(idBox.equals("268")){
			MAIN_HTML += getEditBox("Min. LvL to Create AIO Buffer", idBox, inSeccion);
		}		

		MAIN_HTML += setFilaBoolean("Fame", general.OPCIONES_CHAR_FAME, inSeccion, "276");
		MAIN_HTML += setFilaModif("Fame Cost", "", inSeccion, "277");
		if(idBox.equals("277")){
			MAIN_HTML += getEdititem("Famce Cost",general.OPCIONES_CHAR_FAME_PRICE ,inSeccion,idBox);
		}		
		MAIN_HTML += setFilaModif("Fame Ammount to Give", String.valueOf(general.OPCIONES_CHAR_FAME_GIVE),inSeccion,"280");
		if(idBox.equals("280")){
			MAIN_HTML += getEditBox("Fame Ammount to Give", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Fame Noble Only", general.OPCIONES_CHAR_FAME_NOBLE, inSeccion, "278");

		MAIN_HTML += setFilaModif("Min. Level to Fame", String.valueOf(general.OPCIONES_CHAR_FAME_LVL),inSeccion,"279");
		if(idBox.equals("279")){
			MAIN_HTML += getEditBox("Min. Level to Fame", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaBoolean("Reduce Pk Counter", general.OPCIONES_CHAR_REDUCE_PK, inSeccion, "602");

		MAIN_HTML += setFilaModif("Reduce Pk Mount to Reduce", String.valueOf(general.OPCIONES_CHAR_REDUCE_AMOUNT),inSeccion,"603");
		if(idBox.equals("603")){
			MAIN_HTML += getEditBox("Reduce Pk Mount to Reduce", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Reduce Pk Cost", "", inSeccion, "601");
		if(idBox.equals("601")){
			MAIN_HTML += getEdititem("Reduce Pk Cost",general.OPCIONES_CHAR_REDUCE_PK_PRICE ,inSeccion,idBox);
		}
		return MAIN_HTML + getCerrarTabla();
	}
	
	private static String getConfigVarios2(L2PcInstance player, String idBox){
		//TODO: aqui para la nueva configuracion
		String inSeccion = "opcvarias2";
		
		String LinkConfig = "setConfig1 opcvarias 0 0 0 0 0";
		String btnConfig = "<button value=\"Config 1\" action=\"bypass -h " + bypassNom + " "+LinkConfig+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Miscelaniuos Config" + btnConfig)+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Event Lv clan & reputation", general.EVENT_REPUTATION_CLAN, inSeccion, "534");

		MAIN_HTML += setFilaModif("Event Lv clan NPC ID", String.valueOf(general.EVENT_REPUTATION_CLAN_ID_NPC), inSeccion, "535");
		if(idBox.equals("535")){
			MAIN_HTML += getEditBox("Event Lv clan NPC ID",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Level to give", String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE), inSeccion, "536");
		if(idBox.equals("536")){
			MAIN_HTML += getEditBox("Event Lv clan Level to give",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Reputation to give", String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE), inSeccion, "537");
		if(idBox.equals("537")){
			MAIN_HTML += getEditBox("Event Lv clan Reputation to give",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Min Player", String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER), inSeccion, "538");
		if(idBox.equals("538")){
			MAIN_HTML += getEditBox("Event Lv clan Min Player",idBox,inSeccion);
		}
		MAIN_HTML += setFilaBoolean("Event Lv Need all member's Onlines", general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE, inSeccion, "539");

		
		MAIN_HTML += setFilaBoolean("Can Change Players Name", general.OPCIONES_CHAR_CAMBIO_NOMBRE, inSeccion, "479");
		
		MAIN_HTML += setFilaModif("Change Name Cost", "", inSeccion, "125");
		if(idBox.equals("125")){
			MAIN_HTML += getEdititem("Change Name Cost",general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Only nobles can change Name", general.OPCIONES_CHAR_CAMBIO_NOMBRE_NOBLE, inSeccion, "127");

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_LVL),inSeccion,"128");
		if(idBox.equals("128")){
			MAIN_HTML += getEditBox("Min level to change name", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Can Change Clan Name", general.OPCIONES_CLAN_CAMBIO_NOMBRE, inSeccion, "480");
		
		MAIN_HTML += setFilaModif("Change Clan Name Cost", "", inSeccion, "126");
		if(idBox.equals("126")){
			MAIN_HTML += getEdititem("Change Clan Name Cost",general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Min. Clan LvL to Change Name", String.valueOf(general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_LVL),inSeccion,"129");
		if(idBox.equals("129")){
			MAIN_HTML += getEditBox("Min. Clan LvL to Change Name", idBox, inSeccion);
		}		
		
		MAIN_HTML += setFilaBoolean("Delevel", general.OPCIONES_CHAR_DELEVEL, inSeccion, "609");
		
		MAIN_HTML += setFilaModif("Delevel Prices", "", inSeccion, "133");
		if(idBox.equals("133")){
			MAIN_HTML += getEdititem("Input Delevel Prices",general.DELEVEL_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Min level can use", String.valueOf(general.DELEVEL_LVL_MAX),inSeccion,"134");
		if(idBox.equals("134")){
			MAIN_HTML += getEditBox("Minimum level that can reach", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Noble can use it only",general.DELEVEL_NOBLE,inSeccion,"135");

		MAIN_HTML += setFilaBoolean("Remove Invalid Skill from Player", general.DELEVEL_CHECK_SKILL, inSeccion, "399");		
		
		
		return MAIN_HTML + getCerrarTabla();
	}		

	private static String getConfigRaidbossInfo(L2PcInstance player, String idBox){
		String inSeccion = "bossinfo";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Raid Boss Info Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("RaidBoss Direct Teleport", general.RAIDBOSS_INFO_TELEPORT , inSeccion, "119");
		
		MAIN_HTML += setFilaBoolean("RaidBoss Observe Mode", general.RAIDBOSS_OBSERVE_MODE , inSeccion, "595");
		
		MAIN_HTML += setFilaBoolean("Just Noble Can use RB Direct Tele.", general.RAIDBOSS_INFO_NOBLE , inSeccion, "120");

		MAIN_HTML += setFilaModif("RB Teleport Cost", "", inSeccion, "117");

		if(idBox.equals("117")){
			MAIN_HTML += getEdititem("RB Teleport Cost",general.RAIDBOSS_INFO_TELEPORT_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Number of RB to display", String.valueOf(general.RAIDBOSS_INFO_LISTA_X_HOJA),inSeccion,"118");
		if(idBox.equals("118")){
			MAIN_HTML += getEditBox("Number of RB to display (Recommends 25)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Min. LvL to use RB Direct Teleport", String.valueOf(general.RAIDBOSS_INFO_LISTA_X_HOJA),inSeccion,"121");
		if(idBox.equals("121")){
			MAIN_HTML += getEditBox("Min. LvL to use RB Direct Teleport", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Raidboss Blacklist (List of Raid that can not be reached from RaidBoss Info teleport)", "", inSeccion, "351");
		if(idBox.equals("351")){
			MAIN_HTML += getEditListaNPC_VectorInteger("RaidBoss Blacklist",general.RAIDBOSS_ID_MOB_NO_TELEPORT ,inSeccion,idBox);
		}

		return MAIN_HTML + getCerrarTabla();
	}




	private static String getConfigColor(L2PcInstance player, String idBox){
		String inSeccion = "colorname";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Color Name Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Color Name Cost", "", inSeccion, "104");
		if(idBox.equals("104")){
			MAIN_HTML += getEdititem("Color Name Cost",general.PINTAR_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Colors", "", inSeccion, "105");
		if(idBox.equals("105")){
			MAIN_HTML += getColorMenu("Input the Name Colors HTML Code", general.PINTAR_MATRIZ, inSeccion, idBox);
		}

		return MAIN_HTML + getCerrarTabla();
	}
	
	private static String getConfigInstanceZone(L2PcInstance player, String idBox){
		String inSeccion = "instancezone";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Instance Zone")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Pay to remove Instance Penalty.", general.INSTANCE_ZONE_CLEAR, inSeccion, "606");
		MAIN_HTML += setFilaModif("Cost of Service (by hour)", "", inSeccion, "607");
		if(idBox.equals("607")){
			MAIN_HTML += getEdititem("Cost (by Hour) to remove Instance Penalty",general.INSTANCE_ZONE_COST ,inSeccion,idBox);
		}
		return MAIN_HTML + getCerrarTabla();
	}


	private static String getConfigDesafios(L2PcInstance player, String idBox){
		String inSeccion = "desafio";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("The Challenge Config")+ central.LineaDivisora(1);

		String BOTON = "<button value=\"Reward and NPC Found\" action=\"bypass -h " + bypassNom + " DESAFIO 88 0 0\" width=155 height=24 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		MAIN_HTML += central.LineaDivisora(2) + central.headFormat(BOTON) + central.LineaDivisora(2);

		/*MAIN_HTML += setFilaBoolean("lvl 85 Challenge", general.DESAFIO_LVL85, inSeccion, "273");

		MAIN_HTML += setFilaModif("Number of lvl 85 to Win", String.valueOf(general.DESAFIO_MAX_LVL85), inSeccion, "271");
		if(idBox.equals("271")){
			MAIN_HTML += getEditBox("Number of LvL 85 to Win",idBox,inSeccion);
		}

		MAIN_HTML += setFilaModif("Reward for the Firts lvl 85", "", inSeccion, "85");
		if(idBox.equals("85")){
			MAIN_HTML += getEdititem("Reward for the Firts lvl 85",general.DESAFIO_85_PREMIO ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Noble Challenge", general.DESAFIO_NOBLE, inSeccion, "274");


		MAIN_HTML += setFilaModif("Number of Noble to Win", String.valueOf(general.DESAFIO_MAX_NOBLE), inSeccion, "272");
		if(idBox.equals("272")){
			MAIN_HTML += getEditBox("Number of Noble to Win",idBox,inSeccion);
		}
		MAIN_HTML += setFilaModif("Reward for the Firts Noble", "", inSeccion, "86");
		if(idBox.equals("86")){
			MAIN_HTML += getEdititem("Reward for the Firts Noble",general.DESAFIO_NOBLE_PREMIO ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("ID NPC for Search", "", inSeccion, "87");
		if(idBox.equals("87")){
			MAIN_HTML += getEditListaNPC_VectorInteger("NPC For Search.",general.DESAFIO_NPC_BUSQUEDAS,inSeccion,"87");
		}
*/
		MAIN_HTML += setFilaBoolean("Event Lv clan & reputation", general.EVENT_REPUTATION_CLAN, inSeccion, "534");

		MAIN_HTML += setFilaModif("Event Lv clan NPC ID", String.valueOf(general.EVENT_REPUTATION_CLAN_ID_NPC), inSeccion, "535");
		if(idBox.equals("535")){
			MAIN_HTML += getEditBox("Event Lv clan NPC ID",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Level to give", String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE), inSeccion, "536");
		if(idBox.equals("536")){
			MAIN_HTML += getEditBox("Event Lv clan Level to give",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Reputation to give", String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE), inSeccion, "537");
		if(idBox.equals("537")){
			MAIN_HTML += getEditBox("Event Lv clan Reputation to give",idBox,inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Event Lv clan Min Player", String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER), inSeccion, "538");
		if(idBox.equals("538")){
			MAIN_HTML += getEditBox("Event Lv clan Min Player",idBox,inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Event Lv Need all member's Onlines", general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE, inSeccion, "539");		
		
		return MAIN_HTML + getCerrarTabla();

	}

	private static String getConfigAugmentEspecial(L2PcInstance player, String idBox){
		String inSeccion = "augmentspe";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Select Augment Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Select Augment Passive Cost", "", inSeccion, "341");
		if(idBox.equals("341")){
			MAIN_HTML += getEdititem("Select Augment Passive Cost",general.AUGMENT_SPECIAL_PRICE_PASSIVE ,inSeccion,idBox);
		}
		
		MAIN_HTML += setFilaModif("Select Augment Symbol Cost", "", inSeccion, "902");
		if(idBox.equals("902")){
			MAIN_HTML += getEdititem("Select Augment Symbol Cost",general.AUGMENT_SPECIAL_PRICE_SYMBOL ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Select Augment Chance Cost", "", inSeccion, "342");
		if(idBox.equals("342")){
			MAIN_HTML += getEdititem("Select Augment Chance Cost",general.AUGMENT_SPECIAL_PRICE_CHANCE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Select Augment Active Cost", "", inSeccion, "343");
		if(idBox.equals("343")){
			MAIN_HTML += getEdititem("Select Augment Active Cost",general.AUGMENT_SPECIAL_PRICE_ACTIVE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Augment Count to Display", String.valueOf(general.AUGMENT_SPECIAL_x_PAGINA), inSeccion, "108");
		if(idBox.equals("108")){
			MAIN_HTML +=getEditBox("Augment Count to Display (Recommends 25)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.AUGMENT_SPECIAL_LVL), inSeccion, "110");
		if(idBox.equals("110")){
			MAIN_HTML +=getEditBox("Minimum level to be used", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Noble can use it only", general.AUGMENT_SPECIAL_NOBLE, inSeccion, "109");


		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigPartyFinder(L2PcInstance player, String idBox){
		//partyfin
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Go Party Leader Config")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaModif("Go Party Leader Cost", "", "partyfin", "91");
		if(idBox.equals("91")){
			MAIN_HTML += getEdititem("Go Party Leader Cost",general.PARTY_FINDER_PRICE,"partyfin",idBox);
		}

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.PARTY_FINDER_CAN_USE_LVL), "partyfin", "97");
		if(idBox.equals("97")){
			MAIN_HTML +=getEditBox("Minimum level to be used", "97", "partyfin");
		}

		
		
		MAIN_HTML += setFilaBoolean("Only for Noble Player's", general.PARTY_FINDER_CAN_USE_ONLY_NOBLE, "partyfin", "530");
		
		MAIN_HTML += setFilaBoolean("Go when Party Leader is Death", general.PARTY_FINDER_GO_LEADER_DEATH, "partyfin", "92");
		MAIN_HTML += setFilaBoolean("Go when Party Leader is Noble", general.PARTY_FINDER_GO_LEADER_NOBLE, "partyfin", "93");
		MAIN_HTML += setFilaBoolean("Go when Party Leader is Flag / PK", general.PARTY_FINDER_GO_LEADER_FLAGPK, "partyfin", "94");
		MAIN_HTML += setFilaBoolean("Go when Party Leader are in Instance", general.PARTY_FINDER_GO_LEADER_WHEN_ARE_INSTANCE, "partyfin", "322");
		MAIN_HTML += setFilaBoolean("Go when Party Leader is on Asedio Zone", general.PARTY_FINDER_GO_LEADER_ON_ASEDIO, "partyfin", "415");
		MAIN_HTML += setFilaBoolean("PK Player Can use it", general.PARTY_FINDER_CAN_USE_PK, "partyfin", "95");
		MAIN_HTML += setFilaBoolean("Flag Player Can use it", general.PARTY_FINDER_CAN_USE_FLAG, "partyfin", "96");
		MAIN_HTML += setFilaBoolean("Use Summon Rulez From Geo D.", general.PARTY_FINDER_USE_NO_SUMMON_RULEZ, "partyfin", "321");

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigFlagFinder(L2PcInstance player, String idBox){
		String inSeccion = "flagfin";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Flag Finder Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Flag finder Cost", "", inSeccion, "98");
		if(idBox.equals("98")){
			MAIN_HTML += getEdititem("Flag Finder Cost",general.FLAG_FINDER_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.FLAG_FINDER_LVL), inSeccion, "102");
		if(idBox.equals("102")){
			MAIN_HTML +=getEditBox("Minimum level to be used", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum Level of the Flag/PK Player to Sort", String.valueOf(general.FLAG_PVP_PK_LVL_MIN), inSeccion, "103");
		if(idBox.equals("103")){
			MAIN_HTML +=getEditBox("Minimum Level of the Flag/PK Player to Sort", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("Minimum PvP of the Flag/PK Player to Sort", String.valueOf(general.FLAG_FINDER_MIN_PVP_FROM_TARGET), inSeccion, "510");
		if(idBox.equals("510")){
			MAIN_HTML +=getEditBox("Minimum PvP of the Flag/PK Player to Sort", idBox, inSeccion);
		}		
		
		MAIN_HTML += setFilaBoolean("Flag player can use It", general.FLAG_FINDER_CAN_USE_FLAG, inSeccion, "99");
		MAIN_HTML += setFilaBoolean("PK Player can use It", general.FLAG_FINDER_CAN_USE_PK, inSeccion, "100");
		MAIN_HTML += setFilaBoolean("Go flag only for Noble Player", general.FLAG_FINDER_CAN_NOBLE, inSeccion, "101");
		MAIN_HTML += setFilaBoolean("When the pk/pvp Player is inside the castle, can go for him?", general.FLAG_FINDER_CAN_GO_IS_INSIDE_CASTLE, inSeccion, "309");
		MAIN_HTML += setFilaBoolean("PK Player Priority?", general.FLAG_FINDER_PK_PRIORITY, inSeccion, "340");
		MAIN_HTML += setFilaBoolean("Check Clan", general.FLAG_FINDER_CHECK_CLAN, inSeccion, "552");
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigAntiBot2(L2PcInstance player, String idBox){
		String inSeccion = "Antibot2";
		
		String LinkConfig = "setConfig1 Antibot 0 0 0 0 0";
		String btnConfig = "<button value=\"Config 1\" action=\"bypass -h " + bypassNom + " "+LinkConfig+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		
		
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Antibot Config" + btnConfig)+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Deaths Mobs to Send Antibot Verification", String.valueOf(general.ANTIBOT_MOB_DEAD_TO_ACTIVATE), inSeccion, "293");
		if(idBox.equals("293")){
			MAIN_HTML +=getEditBox("Deaths Mobs to Send Antibot Verification", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes between Player To Player verification", String.valueOf(general.ANTIBOT_MINUTE_VERIF_AGAIN), inSeccion, "294");
		if(idBox.equals("294")){
			MAIN_HTML +=getEditBox("Minutes between Player To Player verification", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Timeout to wait for the answer", String.valueOf(general.ANTIBOT_MINUTOS_ESPERA), inSeccion, "295");
		if(idBox.equals("295")){
			MAIN_HTML +=getEditBox("Timeout to wait for the answer. When the time is up the player will be sent to jail", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum level of the player verifier", String.valueOf(general.ANTIBOT_MIN_LVL), inSeccion, "296");
		if(idBox.equals("296")){
			MAIN_HTML +=getEditBox("Minimum level of the player verifier. (0 = Disabled)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Id Item to Remove from Player", "" ,inSeccion,"317");
		if(idBox.equals("317")){
			opera.enviarHTML(player,getEditLista_VectorInteger("Id Item to Remove from Inventary player",passVectorFromArray_int(general.ANTIBOT_BORRAR_ITEM_ID),inSeccion,idBox,true));
			return "";
		}
		//'526','ANTIBOT_BLACK_LIST'
		MAIN_HTML += setFilaBoolean("Antibot Black List", general.ANTIBOT_BLACK_LIST, inSeccion, "526");

		MAIN_HTML += setFilaModif("Multiplier minutes for the Blacklisted", String.valueOf(general.ANTIBOT_BLACK_LIST_MULTIPLIER) ,inSeccion,"527");
		if(idBox.equals("527")){
			MAIN_HTML +=getEditBox("Multiplier minutes for the Blacklisted", idBox, inSeccion);
		}



		return MAIN_HTML + getCerrarTabla();
	}		
	
	
	
	
	
	
	
	
	

	private static String getConfigAntiBot(L2PcInstance player, String idBox){
		String inSeccion = "Antibot";
		
		String LinkSecondConfig = "setConfig1 Antibot2 0 0 0 0 0";
		String btnSecondConfig = "<button value=\"Config 2\" action=\"bypass -h " + bypassNom + " "+LinkSecondConfig+"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";		

		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Antibot Config")+ central.LineaDivisora(1);
		MAIN_HTML += central.LineaDivisora(1) + central.headFormat(btnSecondConfig) + central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Antibot command .checkbot activated", general.ANTIBOT_COMANDO_STATUS, inSeccion, "290");
		MAIN_HTML += setFilaBoolean("Reset death mob count when player re login", general.ANTIBOT_RESET_COUNT, inSeccion, "303");
		MAIN_HTML += setFilaBoolean("Automatic Antibot System", general.ANTIBOT_AUTO, inSeccion, "302");
		MAIN_HTML += setFilaBoolean("Command .checkbot Only Noble", general.ANTIBOT_NOBLE_ONLY, inSeccion, "297");
		MAIN_HTML += setFilaBoolean("Command .checkbot Only Hero", general.ANTIBOT_HERO_ONLY, inSeccion, "298");
		MAIN_HTML += setFilaBoolean("Command .checkbot Only GM", general.ANTIBOT_GM_ONLY, inSeccion, "300");
		MAIN_HTML += setFilaBoolean("Annouce Jail Player", general.ANTIBOT_ANNOU_JAIL, inSeccion, "301");
		MAIN_HTML += setFilaBoolean("Send to jail all player on this IP", general.ANTIBOT_SEND_JAIL_ALL_DUAL_BOX, inSeccion, "505");
		MAIN_HTML += setFilaBoolean("Remove Item's from Inventory when player sent to jail", general.ANTIBOT_BORRAR_ITEM, inSeccion, "315");
		MAIN_HTML += setFilaBoolean("Player can send checkbot in peace zone", general.ANTIBOT_CHECK_INPEACE_ZONE, inSeccion, "318");
		MAIN_HTML += setFilaBoolean("Send all player on IP the annoucement", general.ANTIBOT_SEND_ALL_IP, inSeccion, "404");
		MAIN_HTML += setFilaBoolean("Can send check in the Same PC", general.ANTIBOT_CHECK_DUALBOX, inSeccion, "483");
		

		MAIN_HTML += setFilaModif("Total percentage to Remove Items from Bot Player", String.valueOf(general.ANTIBOT_BORRAR_ITEM_PORCENTAJE), inSeccion, "316");
		if(idBox.equals("316")){
			MAIN_HTML +=getEditBox("Total percentage to Remove Items from Bot Player", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Opportunities to make mistakes", String.valueOf(general.ANTIBOT_OPORTUNIDADES), inSeccion, "291");
		if(idBox.equals("291")){
			MAIN_HTML +=getEditBox("Opportunities for entering wrong answer", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Inactive minutes to not receive bot verification", String.valueOf(general.ANTIBOT_INACTIVE_MINUTES), inSeccion, "380");
		if(idBox.equals("380")){
			MAIN_HTML +=getEditBox("Inactive minutes to not receive bot verification", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum lifetime to use this command (Minutes).", String.valueOf(general.ANTIBOT_ANTIGUEDAD_MINUTOS), inSeccion, "299");
		if(idBox.equals("299")){
			MAIN_HTML +=getEditBox("Minimum lifetime to use this command (Minutes, use 0 for Disabled)", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minutes in jail for wrog answer", String.valueOf(general.ANTIBOT_MINUTOS_JAIL), inSeccion, "292");
		if(idBox.equals("292")){
			MAIN_HTML +=getEditBox("Minutes in jail for wrog answer", idBox, inSeccion);
		}
		return MAIN_HTML + getCerrarTabla();
	}


	private static String getConfigElemental(L2PcInstance player, String idBox){
		String inSeccion = "elementalspe";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Select Element Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Select Element Cost", "", inSeccion, "130");
		if(idBox.equals("130")){
			MAIN_HTML += getEdititem("Select Element Cost",general.ELEMENTAL_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Noble can use it only", general.ELEMENTAL_NOBLE, inSeccion, "131");

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.ELEMENTAL_LVL), inSeccion, "132");
		if(idBox.equals("132")){
			MAIN_HTML +=getEditBox("Minimum level to be used", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Elemental Max Power to Weapon", String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_WEAPON), inSeccion, "405");
		if(idBox.equals("405")){
			MAIN_HTML +=getEditBox("Elemental Max Power to Weapon", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Elemental Max Power to Armor", String.valueOf(general.ELEMENTAL_LVL_ENCHANT_MAX_ARMOR), inSeccion, "406");
		if(idBox.equals("406")){
			MAIN_HTML +=getEditBox("Elemental Max Power to Armor", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getPageDisponiblesCommu(){
		
		String paraCombo = "_bbshome;_bbsgetfav;_bbslink;_bbsloc;_bbsclan;_bbsmemo;_maillist_0_1_0_;_friendlist_0_";
		//String paraCombo = "_bbshome;_bbsgetfav;_bbslink;_bbsloc;_bbsclan;_bbsmemo;_maillist_0_1_0_;_friendlist_0_";
		String retorno = "";

		for(String parte : paraCombo.split(";")){
			if(!parte.equals(general.getCOMMUNITY_BOARD_DONATION_PART_EXEC()) && !parte.equals(general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC()) && !parte.equals(general.getCOMMUNITY_BOARD_GRAND_RB_EXEC()) && !parte.equals(general.getCOMMUNITY_BOARD_PART_EXEC()) && !parte.equals(general.getCOMMUNITY_BOARD_REGION_PART_EXEC()) && !parte.equals(general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()) && !parte.equals(general.getCOMMUNITY_BOARD_CLAN_PART_EXEC())){
				if(retorno.length()>0){
					retorno += ";";
				}
				retorno += parte;
			}
		}

		return retorno;
	}


	private static String getConfigOverEnchant(L2PcInstance player,String idBox){

		String paraCombo = "";

		for(String cmbStr :  general._ANTI_OVER_TYPE_PUNISH){
			if(paraCombo.length()>1){
				paraCombo += ";";
			}
			paraCombo += cmbStr;
		}

		String inSeccion = "overenchant";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Over Enchant Protection Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Over Enchant Protection", general.ANTI_OVER_ENCHANT_ACT, inSeccion, "456");
		MAIN_HTML += setFilaBoolean("Over Enchant Check before Attack", general.ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK, inSeccion, "457");
		MAIN_HTML += setFilaBoolean("Over Enchant Annoucement", general.ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL, inSeccion, "461");
		MAIN_HTML += setFilaBoolean("Over Enchant Punish All Ip's", general.ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP, inSeccion, "462");

		MAIN_HTML += setFilaModif("Over Enchant Type of Punishment: ", general.ANTI_OVER_TYPE_PUNISH , inSeccion, "460");
		if(idBox.equals("460")){
			MAIN_HTML += getEditComboBox("Type: " + general.ANTI_OVER_TYPE_PUNISH ,idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaModif("Over Enchant Second Between Attack Check", String.valueOf(general.ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK), inSeccion, "458");
		if(idBox.equals("458")){
			MAIN_HTML +=getEditBox("Second Between Attack Check", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Over Enchant Max. Weapon Enchant", String.valueOf(general.ANTI_OVER_ENCHANT_MAX_WEAPON), inSeccion, "463");
		if(idBox.equals("463")){
			MAIN_HTML +=getEditBox("Max. Weapon Enchant", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Over Enchant Max. Armor Enchant", String.valueOf(general.ANTI_OVER_ENCHANT_MAX_ARMOR), inSeccion, "464");
		if(idBox.equals("464")){
			MAIN_HTML +=getEditBox("Max. Armor Enchant", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Over Enchant Annoucement:", String.valueOf(general.ANTI_OVER_ENCHANT_MESJ_PUNISH),inSeccion,"459");
		if(idBox.equals("459")){
			MAIN_HTML+= getEditBigBox("Over Enchant Punishment Annoucement<br1> Use: %CHARNAME% Player Name<br1>Use: %TYPE_PUNISHMENT% for Punishment Type", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigComunidadPartyFinder(L2PcInstance player,String idBox){
		String inSeccion = "cbpartyfinder";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Community Board Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean(".party command enabled", general.COMMUNITY_BOARD_PARTYFINDER_COMMAND_ENABLE , inSeccion, "593");
		
		MAIN_HTML += setFilaModif("Seconds Between player party message", String.valueOf(general.COMMUNITY_BOARD_PARTYFINDER_SEC_BETWEEN_MSJE), inSeccion, "590");
		if(idBox.equals("590")){
			MAIN_HTML +=getEditBox("Seconds Between player party message", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Seconds duration of the Party Request", String.valueOf(general.COMMUNITY_BOARD_PARTYFINDER_SEC_REQUEST), inSeccion, "591");
		if(idBox.equals("591")){
			MAIN_HTML +=getEditBox("Seconds duration of the Party Request", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Sec. duration of the Party Request on Board", String.valueOf(general.COMMUNITY_BOARD_PARTYFINDER_SEC_ON_BOARD), inSeccion, "592");
		if(idBox.equals("592")){
			MAIN_HTML +=getEditBox("Sec. duration of the Party Request on Board", idBox, inSeccion);
		}
		return MAIN_HTML + getCerrarTabla();
	}




	private static String getConfigComunidad(L2PcInstance player,String idBox){

		String paraCombo = getPageDisponiblesCommu();

		String inSeccion = "comunidad";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Community Board Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("ZeuS Donation Board", general.COMMUNITY_BOARD_DONATION, inSeccion, "938");
		MAIN_HTML += setFilaModif("Donation Page: ", general.getCOMMUNITY_BOARD_DONATION_PART_EXEC(), inSeccion, "939");
		if(idBox.equals("939")){
			MAIN_HTML += getEditComboBox("Donation Page: " + general.getCOMMUNITY_BOARD_PART_EXEC(), idBox,inSeccion,paraCombo);
		}
		
		MAIN_HTML += setFilaBoolean("ZeuS Community Board", general.COMMUNITY_BOARD, inSeccion, "409");
		MAIN_HTML += setFilaModif("Community Page: ", general.getCOMMUNITY_BOARD_PART_EXEC(), inSeccion, "410");
		if(idBox.equals("410")){
			MAIN_HTML += getEditComboBox("Community Page: " + general.getCOMMUNITY_BOARD_PART_EXEC(),idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Region Board", general.COMMUNITY_BOARD_REGION, inSeccion, "411");
		MAIN_HTML += setFilaModif("Region Page: ", general.getCOMMUNITY_BOARD_REGION_PART_EXEC(), inSeccion, "412");
		if(idBox.equals("412")){
			MAIN_HTML += getEditComboBox("Region Page: " + general.getCOMMUNITY_BOARD_REGION_PART_EXEC(),idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("Engine Board", general.COMMUNITY_BOARD_ENGINE, inSeccion, "413");
		MAIN_HTML += setFilaModif("Engine Page: ", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC(), inSeccion, "414");
		if(idBox.equals("414")){
			MAIN_HTML += getEditComboBox("Engine Page: " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC(),idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Clan Board", general.COMMUNITY_BOARD_CLAN, inSeccion, "470");
		MAIN_HTML += setFilaModif("Clan Page: ", general.getCOMMUNITY_BOARD_CLAN_PART_EXEC(), inSeccion, "469");
		if(idBox.equals("469")){
			MAIN_HTML += getEditComboBox("Clan Page: " + general.getCOMMUNITY_BOARD_CLAN_PART_EXEC(),idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Grand Raid Boss Board", general.COMMUNITY_BOARD_GRAND_RB, inSeccion, "569");
		MAIN_HTML += setFilaModif("Grand Raid Boss Page: ", general.getCOMMUNITY_BOARD_GRAND_RB_EXEC(), inSeccion, "570");
		if(idBox.equals("570")){
			MAIN_HTML += getEditComboBox("Grand Raid Boss Page: " + general.getCOMMUNITY_BOARD_GRAND_RB_EXEC(),idBox,inSeccion,paraCombo);
		}

		MAIN_HTML += setFilaBoolean("ZeuS Find party Board", general.COMMUNITY_BOARD_PARTYFINDER, inSeccion, "588");
		MAIN_HTML += setFilaModif("Find Party Page: ", general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC(), inSeccion, "589");
		if(idBox.equals("589")){
			MAIN_HTML += getEditComboBox("Find Party Page: " + general.getCOMMUNITY_BOARD_PARTYFINDER_EXEC(),idBox,inSeccion,paraCombo);
		}
		
		MAIN_HTML += setFilaModif("Rows for Page", String.valueOf(general.COMMUNITY_BOARD_ROWS_FOR_PAGE), inSeccion, "416");
		if(idBox.equals("416")){
			MAIN_HTML +=getEditBox("Rows for Page", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Top Player List Count", String.valueOf(general.COMMUNITY_BOARD_TOPPLAYER_LIST), inSeccion, "417");
		if(idBox.equals("417")){
			MAIN_HTML +=getEditBox("Top Player List Count", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Clan List Count", String.valueOf(general.COMMUNITY_BOARD_CLAN_LIST), inSeccion, "418");
		if(idBox.equals("418")){
			MAIN_HTML +=getEditBox("Clan List Count", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigEnchant(L2PcInstance player, String idBox){
		String inSeccion = "enchantspe";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Special Enchant Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaModif("Special Enchant Cost", "", inSeccion, "111");
		if(idBox.equals("111")){
			MAIN_HTML += getEdititem("Special Enchant Cost",general.ENCHANT_ITEM_PRICE ,inSeccion,idBox);
		}

		MAIN_HTML += setFilaBoolean("Noble can use it only", general.ENCHANT_NOBLE, inSeccion, "112");

		MAIN_HTML += setFilaModif("Minimum level to be used", String.valueOf(general.ENCHANT_LVL), inSeccion, "113");
		if(idBox.equals("113")){
			MAIN_HTML +=getEditBox("Minimum level to be used", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Minimum Enchant Item for Enchanted", String.valueOf(general.ENCHANT_MIN_ENCHANT), inSeccion, "114");
		if(idBox.equals("114")){
			MAIN_HTML +=getEditBox("Minimum Enchant Item for Enchanted", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Maximum Enchant for Enchanted", String.valueOf(general.ENCHANT_MAX_ENCHANT), inSeccion, "115");
		if(idBox.equals("115")){
			MAIN_HTML +=getEditBox("Maximum Enchant for Enchanted", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Variable Summing between Enchant.", String.valueOf(general.ENCHANT_x_VEZ), inSeccion, "116");
		if(idBox.equals("116")){
			MAIN_HTML +=getEditBox("Variable Summing between Enchant", idBox, inSeccion);
		}

		return MAIN_HTML + getCerrarTabla();
	}

	private static String getConfigDropSearch(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Drop Search Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Drop Search Direct Teleport for Free", general.DROP_SEARCH_TELEPORT_FOR_FREE , "dropsear", "88");
		
		MAIN_HTML += setFilaBoolean("Drop Search Observe Mode", general.DROP_SEARCH_OBSERVE_MODE, "dropsear", "596");
		
		MAIN_HTML += setFilaBoolean("Drop Search Can use Teleport to Mob", general.DROP_SEARCH_CAN_USE_TELEPORT , "dropsear", "352");
		
		MAIN_HTML += setFilaBoolean("Drop Search use Shift Mode", general.DROP_SEARCH_SHIFT_NPC_SHOW , "dropsear", "598");
		

		MAIN_HTML += setFilaModif("Drop Search Direct Teleport Cost", "", "dropsear", "89");
		if(idBox.equals("89")){
			MAIN_HTML += getEdititem("Drop Search Direct Teleport Cost",general.DROP_TELEPORT_COST,"dropsear",idBox);
		}

		MAIN_HTML += setFilaModif("Drop Search Count to Display", String.valueOf(general.DROP_SEARCH_MOSTRAR_LISTA), "dropsear", "90");
		if(idBox.equals("90")){
			MAIN_HTML +=getEditBox("Count to Display (recommended 25)", "90", "dropsear");
		}

		MAIN_HTML += setFilaBoolean("Drop Search Show ID Item to Players", general.DROP_SEARCH_SHOW_IDITEM_TO_PLAYER , "dropsear", "338");

		MAIN_HTML += setFilaModif("Monster Blacklist (List of mobs that can not be reached from Dropsearch teleport)", "", "dropsear", "350");
		if(idBox.equals("350")){
			MAIN_HTML += getEditListaNPC_VectorInteger("Mob Blacklist",general.DROP_SEARCH_MOB_BLOCK_TELEPORT ,"dropsear","350");
		}

		return MAIN_HTML + getCerrarTabla();

	}

	private static String getConfigTeleport(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Teleport Config")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Use ZeuS Teleport (From BD)", general.TELEPORT_BD, "teleport", "287");
		MAIN_HTML += central.headFormat("<font color=>If you gonna use htm file, need to create a folder into teleport width the name AIONpc-GK. The index file need to called teleport.htm</font>","WHITE");
		MAIN_HTML += setFilaModif("Teleport Cost", "", "teleport", "83");
		if(idBox.equals("83")){
			MAIN_HTML += getEdititem("Teleport Cost",general.TELEPORT_PRICE,"teleport",idBox);
		}
		MAIN_HTML += setFilaBoolean("Teleport for Free", general.FREE_TELEPORT, "teleport", "84");

		MAIN_HTML += setFilaBoolean("Teleport for Free up to Level", general.TELEPORT_FOR_FREE_UP_TO_LEVEL, "teleport", "531");
		
		MAIN_HTML += setFilaModif("Free up to Level", String.valueOf(general.TELEPORT_FOR_FREE_UP_TO_LEVEL_LV),"teleport","532");
		if(idBox.equals("532")){
			MAIN_HTML+= getEditBox("Free up to Level", idBox, "teleport");
		}
		
		MAIN_HTML += setFilaBoolean("Teleport Can be used in combat mode", general.TELEPORT_CAN_USE_IN_COMBAT_MODE, "teleport", "360");
		return MAIN_HTML + getCerrarTabla();
	}

	/*
	 * 	private static String getEditBigBox(String titulo, String idINBD, String Seccion){
			String temp = central.headFormat(titulo,"LEVEL");
			temp += "<center><table width=200><tr><td width=145 align=LEFT><multiedit var=\"valor\" width=110 ></td><td align=center width=55><button value=\"OK\" action=\"bypass -h ZeuSNPC setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\" width=50 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table></center><br>";
			return central.headFormat(temp);
		}
	 * */
	
	private static String getConfigAutopots(L2PcInstance player, String idBox){
		String inSeccion  = "autopots";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Auto Pots")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("CP Autopots Enabled", general.ZEUS_AUTOPOTS_CP, inSeccion, "903");

		MAIN_HTML += setFilaBoolean("MP Autopots Enabled", general.ZEUS_AUTOPOTS_MP, inSeccion, "904");
		
		MAIN_HTML += setFilaBoolean("HP Autopots Enabled", general.ZEUS_AUTOPOTS_HP , inSeccion, "905");

		MAIN_HTML += setFilaModif("Autopots Reuse Check (milisecond)", String.valueOf(general.ZEUS_AUTOPOTS_CHECK_MILISECOND), inSeccion, "906");
		if(idBox.equals("906")){
			MAIN_HTML += getEditBigBox("Input value greater than 500 for reuse check", idBox, inSeccion);
		}		
		
		return MAIN_HTML + getCerrarTabla();
	}	
	
	private static String getConfigPVP(L2PcInstance player, String idBox){
		String inSeccion  = "pvpConfig";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("PvP / PK Config")+ central.LineaDivisora(1);


		MAIN_HTML += setFilaBoolean("PVP/PK Log", general.LOG_FIGHT_PVP_PK, inSeccion, "173");

		MAIN_HTML += setFilaBoolean("BSOE can use in PVP", general.ALLOW_BLESSED_ESCAPE_PVP, inSeccion, "176");
		
		MAIN_HTML += setFilaBoolean("BSOE can use in PK Mode", general.CAN_USE_BSOE_PK , inSeccion, "509");

		MAIN_HTML += setFilaBoolean("ToP PvP / PK Graphic Effects", general.PVP_PK_GRAFICAL_EFFECT, inSeccion, "177");

		MAIN_HTML += setFilaBoolean("Top PvP / PK Login Annoucement", general.ANNOUCE_TOP_PPVPPK_ENTER, inSeccion, "245");

		MAIN_HTML += setFilaBoolean("PvP Reward", general.PVP_REWARD, inSeccion, "384");
		
		MAIN_HTML += setFilaBoolean("PvP Reward check dualbox", general.PVP_REWARD_CHECK_DUALBOX, inSeccion, "520");
		
		MAIN_HTML += setFilaBoolean("Party PvP Reward", general.PVP_PARTY_REWARD, inSeccion, "385");
		
		MAIN_HTML += setFilaBoolean("Party Count Allow on Sieges Fortress", general.PVP_COUNT_ALLOW_SIEGES_FORTRESS, inSeccion, "617");

		MAIN_HTML += setFilaModif("Top PvP / PK Login Message", String.valueOf(general.MENSAJE_ENTRADA_PJ_TOPPVPPK),inSeccion,"246");
		if(idBox.equals("246")){
			MAIN_HTML+= getEditBigBox("Top PvP / PK Login Message<br1> Use: %CHAR_NAME% Player Name", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Annouce Karma Player Login", general.ANNOUCE_PJ_KARMA, inSeccion, "247");

		MAIN_HTML += setFilaModif("Karma Amount to Announce", String.valueOf(general.ANNOUCE_PJ_KARMA_CANTIDAD),inSeccion,"249");
		if(idBox.equals("249")){
			MAIN_HTML+= getEditBox("karma Amount to Announce", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Karma Player Login Message", String.valueOf(general.MENSAJE_ENTRADA_PJ_KARMA),inSeccion,"248");
		if(idBox.equals("248")){
			MAIN_HTML+= getEditBigBox("Karma Player Login Message<br1> Use: %CHAR_NAME% Player Name<br1>%KARMA% for Karma", idBox, inSeccion);
		}

		MAIN_HTML += setFilaBoolean("Annouce Player When kill and get Karma", general.ANNOUNCE_KARMA_PLAYER_WHEN_KILL , inSeccion, "319");
		MAIN_HTML += setFilaModif("Message when PLayer get Karma", String.valueOf(general.ANNOUNCE_KARMA_PLAYER_WHEN_KILL_MSN ),inSeccion,"320");


		if(idBox.equals("248")){
			MAIN_HTML+= getEditBigBox("Karma Player Login Message<br1> Use: %CHAR_NAME% Player Name<br1>%KARMA% for Karma", idBox, inSeccion);
		}


		MAIN_HTML += setFilaModif("PK Protection (level difference)", String.valueOf(general.PVP_PK_PROTECTION_LVL),inSeccion,"175");
		if(idBox.equals("175")){
			MAIN_HTML+= getEditBox("PK Protection (level difference). 0 = Disabled", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("PK Protection Lifetime (Minutes)", String.valueOf(general.PVP_PK_PROTECTION_LIFETIME_MINUTES),inSeccion,"519");
		if(idBox.equals("519")){
			MAIN_HTML+= getEditBox("PK Protection Lifetime (Minutes). 0 = Disabled", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaModif("PVP/PK Player List", String.valueOf(general.MAX_LISTA_PVP), inSeccion, "3");
		if(idBox.equals("3")){
			MAIN_HTML += getEditBigBox("Input value to Player Display", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("PVP/PK Log Player List", String.valueOf(general.MAX_LISTA_PVP_LOG), inSeccion, "4");
		if(idBox.equals("4")){
			MAIN_HTML += getEditBigBox("Input value to Player Display", idBox, inSeccion);
		}

		MAIN_HTML += setFilaModif("Reward for PvP", "", inSeccion, "386");
		if(idBox.equals("386")){
			opera.enviarHTML(player,getEdititem("Reward for PvP", general.PVP_REWARD_ITEMS , inSeccion, idBox,true));
			return "";
		}

		MAIN_HTML += setFilaModif("Reward for Party PvP", "", inSeccion, "387");
		if(idBox.equals("387")){
			opera.enviarHTML(player,getEdititem("Reward for Party PvP", general.PVP_PARTY_REWARD_ITEMS , inSeccion, idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("PvP Reward party Radius", String.valueOf(general.PVP_REWARD_RANGE), inSeccion, "529");
		if(idBox.equals("529")){
			MAIN_HTML += getEditBigBox("PvP Reward party Radius", idBox, inSeccion);
		}
		
		MAIN_HTML += setFilaBoolean("Clan Reputation reward for Kill War Player", general.PVP_CLAN_REPUTATION_REWARD, inSeccion, "599");
		MAIN_HTML += setFilaModif("Clan reputation amount for Kill War player", String.valueOf(general.PVP_CLAN_REPUTATION_AMOUNT), inSeccion, "600");
		if(idBox.equals("600")){
			MAIN_HTML += getEditBox("Clan reputation amount for Kill War Player", idBox, inSeccion);//getEditBigBox("Clan reputation amount for Kill War Player", idBox, inSeccion);
		}
		
		return MAIN_HTML + getCerrarTabla();
	}

	private static String getArrayIntToString(int[] ArrayIN){
		String Retorno ="";

		for(int Access : ArrayIN) {
			if(Retorno.length()>0){
				Retorno += ", ";
			}
			Retorno += String.valueOf(Access);
		}

		return Retorno;
	}

	private static String getConfigNPCZeuS(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("ZeuS NPC AIO")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaModif("NPC's ZeuS Radius", String.valueOf(general.RADIO_PLAYER_NPC_MAXIMO),"general","382");
		if(idBox.equals("382")){
			MAIN_HTML+= getEditBox("NPC's ZeuS Radius", idBox,"general");
		}

		MAIN_HTML += setFilaModif("ID Access Level", getArrayIntToString(general.get_AccessConfig()), "general", "281");
		if(idBox.equals("281")){
			MAIN_HTML += getEditLista_VectorInteger("Input Access Level (Default: 127 (Freya), 8 (H5)", passVectorFromArray_int(general.get_AccessConfig()), "general", "281");
		}

		MAIN_HTML += setFilaBoolean("Commands .expon & .expoff", general.RATE_EXP_OFF , "general", "172");

		MAIN_HTML += setFilaBoolean("Show my Stat", general.SHOW_MY_STAT, "general", "251");

		MAIN_HTML += setFilaBoolean("Use ZeuS Shop (From BD)",general.SHOP_USE_BD,"general","289");
		MAIN_HTML += central.headFormat("<font color=WHITE>If you gonna use htm file, need to create a folder into merchant width the name AIONPC. The index file need to called 955.htm</font>","WHITE");

		MAIN_HTML += setFilaModif("Enchant Annoucement", "" ,"general","174");
		if(idBox.equals("174")){
			MAIN_HTML += getEditLista_VectorInteger("Ingrese los Enchant a Anunciar",passVectorFromArray_int(general.ENCHANT_ANNOUCEMENT),"general","174");
		}

		MAIN_HTML += setFilaModif("Server Name: ", general.Server_Name,"general","288");
		if(idBox.equals("288")){
			MAIN_HTML+= getEditBigBox("Server Name (Max. 16 characters)", idBox, "general");
		}

		MAIN_HTML += setFilaBoolean("Can trade While are Flag", general.TRADE_WHILE_FLAG , "general", "358");
		MAIN_HTML += setFilaBoolean("Can trade While are PK", general.TRADE_WHILE_PK , "general", "359");

		MAIN_HTML += setFilaBoolean("Show ZeuS new main Windows", general.SHOW_NEW_MAIN_WINDOWS , "general", "339");

		MAIN_HTML += setFilaBoolean("Show ZeuS info when player enter game", general.SHOW_ZEUS_ENTER_GAME , "general", "383");

		MAIN_HTML += setFilaBoolean("User char panel voice command(.charpanel)", general.CHAR_PANEL , "general", "407");

		MAIN_HTML += setFilaBoolean("Fix Char Windows (.fixme)", general.SHOW_FIXME_WINDOWS , "general", "408");

		MAIN_HTML += setFilaModif("Second's between skill enchanting", String.valueOf(general.ANTIFEED_ENCHANT_SKILL_REUSE),"general","506");
		if(idBox.equals("506")){
			MAIN_HTML+= getEditBox("Second's between skill enchanting (0=disabled)", "506","general");
		}

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static Vector<Integer> passVectorFromArray_int(String CadenaPasar){
		Vector<Integer> VectorRetorno = new Vector<Integer>();
		for(String _Paso : CadenaPasar.split(",")){
			VectorRetorno.add(Integer.valueOf(_Paso));
		}
		return VectorRetorno;
	}

	private static Vector<Integer> passVectorFromArray_int(int[] VectorPasar){
		Vector<Integer> VectorRetorno = new Vector<Integer>();
		for(int _Paso : VectorPasar){
			VectorRetorno.add(_Paso);
		}
		return VectorRetorno;
	}

	private static String getConfigCastleManager(L2PcInstance player, String idBox){
		String inSeccion = "castlema";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Castle Manager Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Show Giran Manager",general.GIRAN,inSeccion,"329");
		MAIN_HTML += setFilaBoolean("Show Aden Manager",general.ADEN,inSeccion,"330");
		MAIN_HTML += setFilaBoolean("Show Rune Manager",general.RUNE,inSeccion,"331");
		MAIN_HTML += setFilaBoolean("Show Oren Manager",general.OREN,inSeccion,"332");
		MAIN_HTML += setFilaBoolean("Show Dion Manager",general.DION,inSeccion,"333");
		MAIN_HTML += setFilaBoolean("Show Gludio Manager",general.GLUDIO,inSeccion,"334");
		MAIN_HTML += setFilaBoolean("Show Goddard Manager",general.GODDARD,inSeccion,"335");
		MAIN_HTML += setFilaBoolean("Show Schuttgart Manager",general.SCHUTTGART,inSeccion,"336");
		MAIN_HTML += setFilaBoolean("Show Innadril Manager",general.INNADRIL,inSeccion,"337");

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}

	private static String getConfigDressme(L2PcInstance player, String idBox){
		String inSeccion = "dressme";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Dressme Config")+ central.LineaDivisora(1);

		MAIN_HTML += setFilaBoolean("Dressme can use in olys",general.DRESSME_CAN_USE_IN_OLYS,inSeccion,"345");

		MAIN_HTML += setFilaBoolean("Dressme can change clothes in Olys",general.DRESSME_CAN_CHANGE_IN_OLYS,inSeccion,"346");

		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}
	
	private static String getConfigJailBail(L2PcInstance player, String idBox){
		String inSeccion = "jailbail";
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Jail Bail Config")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaBoolean("Jail Bail Payment Enabled",general.JAIL_BAIL_STATUS ,inSeccion,"613");

		MAIN_HTML += setFilaBoolean("Jail Bail Blacklist Enabled",general.JAIL_BAIL_BLACKLIST_MULTIPLE ,inSeccion,"616");
		
		MAIN_HTML += setFilaModif("Jail Bail Cost", "", inSeccion, "614");
		if(idBox.equals("614")){
			MAIN_HTML += getEdititem("Jail Bail Cost",general.JAIL_BAIL_COST,inSeccion,idBox);
		}
		
		MAIN_HTML += setFilaModif("Jail Bail Multiple Cost Fee", String.valueOf(general.JAIL_BAIL_MULTIPLE_COST ), inSeccion, "615");
		if(idBox.equals("615")){
			MAIN_HTML += getEditBox("Jail Bail Multiple Cost Fee", idBox, inSeccion);
		}
		
		
		
		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}	

	private static String getConfigVote(L2PcInstance player, String idBox){
		String MAIN_HTML = "<html><title>"+general.TITULO_NPC()+"</title><body><center>";
		MAIN_HTML += central.LineaDivisora(2)+central.headFormat("Vote Reward Config")+ central.LineaDivisora(1);
		MAIN_HTML += setFilaModif("TopZone Reward", "", "vote", "69");
		if(idBox.equals("69")){
			opera.enviarHTML(player,getEdititem("TopZone Reward",general.VOTO_REWARD_TOPZONE,"vote",idBox,true));
			return "";
		}
		MAIN_HTML += setFilaModif("HopZone Reward", "", "vote", "70");
		if(idBox.equals("70")){
			opera.enviarHTML(player,getEdititem("HopZone Reward",general.VOTO_REWARD_HOPZONE,"vote",idBox,true));
			return "";
		}

		MAIN_HTML += setFilaBoolean("Topzone Vote",general.VOTO_REWARD_ACTIVO_TOPZONE,"vote","71");
		MAIN_HTML += setFilaBoolean("Hopzone Vote",general.VOTO_REWARD_ACTIVO_HOPZONE,"vote","72");
		
		MAIN_HTML += setFilaModif("Attempts Allowed (0=Disabled)", String.valueOf(general.VOTE_ATTEMPTS_ALLOWED), "vote", "608");
		if(idBox.equals("608")){
			MAIN_HTML += getEditBox("Wait Second to Validates", idBox, "vote");
		}
				
		
		MAIN_HTML += setFilaBoolean("Vote every 12 hrs",general.VOTE_EVERY_12_HOURS,"vote","568");

		MAIN_HTML += setFilaModif("Wait Second to Validated", String.valueOf(general.VOTO_REWARD_SEG_ESPERA), "vote", "73");
		if(idBox.equals("73")){
			MAIN_HTML += getEditBox("Wait Second to Validates", idBox, "vote");
		}
		
		MAIN_HTML += setFilaBoolean("Reward all ppl on the same Network",general.VOTE_REWARD_PERSONAL_VOTE_ALL_IP_PLAYER,"vote","533");

		MAIN_HTML += setFilaBoolean("Auto Vote Reward System", general.VOTO_AUTOREWARD , "vote", "328");
		
		MAIN_HTML += setFilaBoolean("Auto Vote Reward AFK Check", general.VOTO_REWARD_AUTO_AFK_CHECK , "vote", "514");

		MAIN_HTML += setFilaModif("AutoVote Reward minutes between check votes", String.valueOf(general.VOTO_REWARD_AUTO_MINUTE_TIME_TO_CHECK ), "vote", "323");
		if(idBox.equals("323")){
			MAIN_HTML += getEditBox("Minutes between check votes", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("AutoVote Rewarding every X Votes", String.valueOf(general.VOTO_REWARD_AUTO_RANGO_PREMIAR), "vote", "324");
		if(idBox.equals("324")){
			MAIN_HTML += getEditBox("AutoVote Rewarding every X Votes", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Messages to reach the Vote reward", String.valueOf(general.VOTO_REWARD_AUTO_MENSAJE_FALTA), "vote", "325");
		if(idBox.equals("325")){
			MAIN_HTML += getEditBigBox("Input the message<br1>Use: %VOTENEED% Votes Needed<br1>%VOTEACT% current votes<br1>%VOTETOREWARD% to show the Vote to Reward<br1>%SITE% To show the Vote Web Site", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Messages when the Vote reward is reached", String.valueOf(general.VOTO_REWARD_AUTO_MENSAJE_META_COMPLIDA), "vote", "326");
		if(idBox.equals("326")){
			MAIN_HTML += getEditBigBox("Input the message: Used %SITE% To show the Vote Web Site", idBox, "vote");
		}

		MAIN_HTML += setFilaModif("Reward when the Hopzone voting cycle was reached", "", "vote", "512");
		if(idBox.equals("512")){
			opera.enviarHTML(player,getEdititem("Reward when the Hopzone voting cycle was reached", general.VOTO_REWARD_AUTO_REWARD_META_HOPZONE ,"vote",idBox,true));
			return "";
		}
		
		MAIN_HTML += setFilaModif("Reward when the Topzone voting cycle was reached", "", "vote", "513");
		if(idBox.equals("513")){
			opera.enviarHTML(player,getEdititem("Reward when the Topzone voting cycle was reached", general.VOTO_REWARD_AUTO_REWARD_META_TOPZONE ,"vote",idBox,true));
			return "";
		}



		MAIN_HTML += getBtnbackConfig() + central.getPieHTML() + "</body></html>";
		return MAIN_HTML;
	}





	private static String getListaIndividual_INTEGER(Vector<Integer> Lista, String Seccion, String idBox){
		if(Lista.isEmpty()){
			return "";
		}
		String BotonSuprimir ="";
		String Grilla = "";
		String Retorno = "";
		int Contador = 0;
		Grilla = "<table width=260><tr>";
		if(!Lista.isEmpty()){
			for(int _lista : Lista){
				//suprdato
				Contador++;
				BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h "+ bypassNom +" setConfig1 suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				Grilla += "<td width=65 align=center>"+String.valueOf(_lista)+"<br1>"+BotonSuprimir+"</td>";
				if((Contador%4) == 0){
					Grilla += "</tr><tr>" ;
				}
			}
			if(Grilla.endsWith("<tr>") || Grilla.endsWith("</td>")){
				Grilla += "</tr>";
			}
			Grilla += "</table>";
			Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}
		return Retorno;
	}

	private static String getListaIndividual_NPC_INTEGER(Vector<Integer> Lista, String Seccion, String idBox){
		if(Lista.isEmpty()){
			return "";
		}
		String BotonSuprimir ="";
		String Grilla = "";
		String Retorno = "";
		int Contador = 0;
		Grilla = "<table width=260><tr>";
		if(!Lista.isEmpty()){
			for(int _lista : Lista){
				//suprdato
				Contador++;
				BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				Grilla += "<td width=65 align=center>"+String.valueOf(_lista)+"<br1>"+BotonSuprimir+"</td>";
				if((Contador%4) == 0){
					Grilla += "</tr><tr>" ;
				}
			}
			if(Grilla.endsWith("<tr>") || Grilla.endsWith("</td>")){
				Grilla += "</tr>";
			}
			Grilla += "</table>";
			Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}
		return Retorno;
	}

	private static String getColores(String Colores, String Seccion, String idBox){
		try{
			if(Colores.isEmpty()){
				return "";
			}
		}catch(Exception a ){
			_log.warning(a.getMessage() + "->" + Colores);
			return "";
		}

		String Retorno ="";

		String BotonSuprimir ="";
		String Grilla = "<table width=260><tr>";

		int Contador = 0;

		//suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+"
		for(String Color : Colores.split(",")){
			Contador++;
			BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 suprdato "+Seccion+" "+idBox+" "+Color+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
			Grilla += "<td width=65 align=center><font color="+Color+">This Color</font><br1>"+BotonSuprimir+"</td>";
			if((Contador%4)==0){
				Grilla += "</tr><tr>";
			}
		}

		if(!Grilla.endsWith("</tr>")){
			Grilla += "</tr>";
		}

		Grilla += "</table>";
		Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);


		return Retorno;

		//HTML_MIENTRAS += "<tr><td width=230 align=center>" + central.getNombreITEMbyID(ID_ITEM) + " ("+ String.valueOf(rss.getInt(4)) +",["+String.valueOf(rss.getInt(3))+"]) </td><td width=50 align=center>"+BOTON_BORRAR_ITEM_FAMILIA+"</td></tr>";
	}

	private static String getPremiosLista(String premios, String Seccion, String idBox){
		try{
			if(premios.isEmpty()){
				return "";
			}
		}catch(Exception a ){
			_log.warning(a.getMessage() + "->" + premios);
			return "";
		}

		String Retorno ="";

		if(!premios.isEmpty()){
			String BotonSuprimir ="";
			String Grilla = "<table width=260>";

			for(String pre : premios.split(";")){
				String[] pre2 = pre.split(",");
				BotonSuprimir = "<button value=\"Supr.\" action=\"bypass -h " + bypassNom + " setConfig1 supritem "+Seccion+" "+idBox+" "+pre+" 0 0\" width=45 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				Grilla += "<tr><td width=215 align=center>"+central.getNombreITEMbyID(Integer.valueOf(pre2[0]))+" ("+ opera.getFormatNumbers(pre2[1])+")</td><td width=45>"+BotonSuprimir+"</td></tr>";
			}

			Grilla += "</table>";
			Retorno = central.LineaDivisora(1) + central.headFormat(Grilla)+central.LineaDivisora(1);
		}


		return Retorno;

		//HTML_MIENTRAS += "<tr><td width=230 align=center>" + central.getNombreITEMbyID(ID_ITEM) + " ("+ String.valueOf(rss.getInt(4)) +",["+String.valueOf(rss.getInt(3))+"]) </td><td width=50 align=center>"+BOTON_BORRAR_ITEM_FAMILIA+"</td></tr>";
	}
	
	@SuppressWarnings("unused")
	private static String RemoveItemStringInteger(String Listastr, String Remover){
		Vector<Integer> Lista = new Vector<Integer>();
		
		for(String Spi : Listastr.split(",")){
			Lista.add(Integer.valueOf(Spi));
		}
		
		if(Lista == null){
			return "";
		}
		
		if(Lista.isEmpty()){
			return "";
		}
		String Retorno ="";

		if(!Lista.remove(Integer.valueOf(Remover))){
			Retorno = "";
			_log.warning("Error al Remover el ID");
			return Retorno;
		}

		if(Lista.size()>0){
			for(int _IDVector : Lista){
				if(Retorno.length()>0){
					Retorno +=",";
				}
				Retorno += String.valueOf(_IDVector);
			}
		}
		return Retorno;
	}

	private static String RemoveItemVectorInteger(Vector<Integer> Lista, String Remover){
		if(Lista.isEmpty()){
			return "";
		}
		String Retorno ="";

		if(!Lista.remove(Integer.valueOf(Remover))){
			Retorno = "";
			_log.warning("Error al Remover el ID");
			return Retorno;
		}

		if(Lista.size()>0){
			for(int _IDVector : Lista){
				if(Retorno.length()>0){
					Retorno +=",";
				}
				Retorno += String.valueOf(_IDVector);
			}
		}
		return Retorno;
	}

	private static String RemoveListaString(int[] Lista, String Remover){
		if(Lista.length==0){
			return "";
		}
		String Retorno ="";
		try{
			Vector<String> vecPremio = new Vector<String>();
			for(int pre : Lista){
				vecPremio.add(String.valueOf(pre));
			}
			vecPremio.remove(Remover);

			if(vecPremio.size()>0){
				for(String premiosIn : vecPremio){
					if(Retorno.length()>0){
						Retorno +=",";
					}
					Retorno += premiosIn;
				}
			}
		}catch(Exception a){
			Retorno = "";
		}
		return Retorno;
	}

	private static String RemoveListaString(String Lista, String Remover){
		if(Lista.isEmpty()){
			return "";
		}
		String Retorno ="";
		try{
			Vector<String> vecPremio = new Vector<String>();
			for(String pre : Lista.split(",")){
				vecPremio.add(pre);
			}
			vecPremio.remove(Remover);

			if(vecPremio.size()>0){
				for(String premiosIn : vecPremio){
					if(Retorno.length()>0){
						Retorno +=",";
					}
					Retorno += premiosIn;
				}
			}
		}catch(Exception a){
			Retorno = "";
		}
		return Retorno;
	}

	private static String RemovePremio(String Premios, String Remover){
		if(Premios.isEmpty()){
			return "";
		}
		String Retorno ="";
		try{
			Vector<String> vecPremio = new Vector<String>();
			for(String pre : Premios.split(";")){
				vecPremio.add(pre);
			}
			vecPremio.remove(Remover);

			if(vecPremio.size()>0){
				for(String premiosIn : vecPremio){
					if(Retorno.length()>0){
						Retorno +=";";
					}
					Retorno += premiosIn;
				}
			}
		}catch(Exception a){
			Retorno = "";
		}
		return Retorno;
	}

	public static String bypass(L2PcInstance player, String event){

		if(general.DEBUG_CONSOLA_ENTRADAS){
			_log.warning("ADMIN->"+event);
		}
		if(general.DEBUG_CONSOLA_ENTRADAS_TO_USER){
			central.msgbox(event, player);
		}

		String[] eventSplit = event.split(" ");
		String evento = eventSplit[1];
		if(eventSplit.length<6){
			central.msgbox_Lado(language.getInstance().getMsg(player).NEED_ENTER_THE_REQUESTED_DATA, player);
			return htmlShow(player, evento, "0");
		}

		String eventParam1 = "";
		String eventParam2 = "";
		String eventParam3 = "";
		String eventParam4 = "";
		String eventParam5 = "";

		//return htmls.ErrorTipeoEspacio();

		try{
			eventParam1 = eventSplit[2];
			eventParam2 = eventSplit[3];
			eventParam3 = eventSplit[4];
			eventParam4 = eventSplit[5];
			eventParam5 = eventSplit[6];
		}catch(Exception a ){
			return htmls.ErrorTipeoEspacio_Admin();
		}


			switch(evento)/*Determinado despues de setConfig1*/{
				case "general":
					return getConfigNPCZeuS(player, eventParam1);
				case "dressme":
					return getConfigDressme(player, eventParam1);
				case "jailbail":
					return getConfigJailBail(player, eventParam1);
				case "vote":
					return getConfigVote(player, eventParam1);
				case "teleport":
					return getConfigTeleport(player, eventParam1);
				case "autopots":
					return getConfigAutopots(player, eventParam1);
				case "dropsear":
					return getConfigDropSearch(player, eventParam1);
				case "partyfin":
					return getConfigPartyFinder(player, eventParam1);
				case "flagfin":
					return getConfigFlagFinder(player, eventParam1);
				case "augmentspe":
					return getConfigAugmentEspecial(player, eventParam1);
				case "enchantspe":
					return getConfigEnchant(player, eventParam1);
				case "instancezone":
					return getConfigInstanceZone(player, eventParam1);
				case "desafio":
					return getConfigDesafios(player, eventParam1);
				case "colorname":
					return getConfigColor(player, eventParam1);
				case "elementalspe":
					return getConfigElemental(player, eventParam1);
				case "bossinfo":
					return getConfigRaidbossInfo(player, eventParam1);
				case "opcvarias":
					return getConfigVarios(player, eventParam1);
				case "opcvarias2":
					return getConfigVarios2(player, eventParam1);
				case "pvppkColor":
					return getConfigPVPPKColor(player, eventParam1);
				case "pvpColor":
					return getConfigPVPColor(player, eventParam1);
				case "pkColor":
					return getConfigPKColor(player, eventParam1);
				case "pvpConfig":
					return getConfigPVP(player, eventParam1);
				case "raidconfig":
					return getConfigRaidAnnoucement(player, eventParam1);
				case "transform":
					return getConfigTransform(player, eventParam1);
				case "Olymp":
					return getConfigOly(player,eventParam1);
				case "Antibot":
					return getConfigAntiBot(player, eventParam1);
				case "Antibot2":
					return getConfigAntiBot2(player, eventParam1);
				case "castlema":
					return getConfigCastleManager(player,eventParam1);
				case "loadConfig":
					return getConfigLoad(player, eventParam1);
				case "cancelbuff":
					return getConfigCancelBuff(player, eventParam1);
				case "comunidad":
					return getConfigComunidad(player,eventParam1);
				case "cbpartyfinder":
					return getConfigComunidadPartyFinder(player, eventParam1);
				case "overenchant":
					return getConfigOverEnchant(player, eventParam1);
				case "emailregister":
					return getConfigRegister(player,eventParam1);
				case "dualbox":
					return getConfigDualBox(player,eventParam1);
				case "ChatConfig":
					return getConfigChat(player, eventParam1);
				case "RaidBossEvent":
					return getConfigRaidBossEvent(player,eventParam1);
				case "TownWarEvent":
					return getConfigTownWarEvent(player, eventParam1);
				case "changeName":
					return  getConfigChangeName(player, eventParam1);
				case "banip":
					return getConfigIPBan(player,eventParam1);
				case "_loadConfig":
					//String BTN_load = "<button value=\"Add\" action=\"bypass -h " + bypassNom + " setConfig1 _loadConfig %LOAD% 0 "+idBox+" "+inSeccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					if(eventParam1.equals("load_all")){
						general.loadConfigs();
						central.msgbox("Load Complete", player);
					}else if(eventParam1.equals("load_dropsearch")){
						general.loadDropList();
						central.msgbox("Drop search Load Complete", player);
					}else if(eventParam1.equals("load_raidbossevent")){
						general.loadConfigs(false);
						RaidBossEvent.setNewSearch();
						RaidBossEvent.IntervalEventStart();
						central.msgbox("Load Complete", player);
					}else if(eventParam1.equals("create_droplist")){
						String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>" + central.LineaDivisora(1) + central.headFormat("Create Droplist") + central.LineaDivisora(1) + central.headFormat("This process may take several minutes.<br1> Not recommended for live servers","LEVEL")+central.LineaDivisora(1)+central.getPieHTML()+"</body></html>";
						opera.enviarHTML(player, HTML);
						//general.setNewDataTableDropList();
						HTML = "<html><title>" + general.TITULO_NPC() + "</title><body>" + central.LineaDivisora(1) + central.headFormat("Create Droplist") + central.LineaDivisora(1) + central.headFormat("The process is complete","LEVEL")+central.LineaDivisora(1)+central.getPieHTML()+"</body></html>";
						opera.enviarHTML(player, HTML);
					}
					return getConfigLoad(player, "0");
				case "setValor"://eventParam1 => ID EN BD
					if(!opera.isNumeric(eventParam2) && VectorOnlyNumeros.contains(Integer.valueOf(eventParam1))){
						central.msgbox(language.getInstance().getMsg(player).ENTER_NUMBERS_ONLY, player);
						return htmlShow(player, eventParam3, eventParam1);
					}
					if(eventParam1.equals("1")){//ID BD NPC ZEUS
						if(Integer.valueOf(eventParam2)==general.ID_NPC_CH){
							central.msgbox("Wrog NPC ID.", player);
							return getConfigNPCZeuS(player, eventParam1);
						}
					}else if(eventParam1.equals("2")){
						if(Integer.valueOf(eventParam2) == general.ID_NPC){
							central.msgbox("Wrog NPC ID.", player);
							return getConfigNPCZeuS(player, eventParam1);
						}
					}else if(eventParam1.equals("316")){
						int TempoVal = Integer.valueOf(eventParam2);
						if((TempoVal<0) || (TempoVal>100)){
							central.msgbox("Put numbers between 1 and 100", player);
							return getConfigNPCZeuS(player, eventParam1);
						}
					}
					setValue(eventParam1,eventParam2,player);

					
					if(eventParam3.equals("comunidad")){
						general._getAllTopPlayers();
					}
					
					return htmlShow(player,eventParam3,eventParam1);

				case "setValorBig":
					//setConfig1 setValorBig "+idINBD+" 0 "+Seccion+" 0 $valor\"
					if(eventParam5.isEmpty()){
						central.msgbox(language.getInstance().getMsg(player).DO_NOT_LEAVE_EMPTY_FIELD, player);
					}
					String Mensaje = getTextoParam5(event);
					setValue(eventParam1,Mensaje,player);

					return htmlShow(player, eventParam3, eventParam1);
				case "setCombo":
					/*Aqui*/
					//setConfig1 setCombo $cmbSelecc 0 "+idINBD+" "+Seccion+" 0\" width=120 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></center>";
					setValue(eventParam3,eventParam1,player);
					return htmlShow(player, eventParam4 , eventParam3);
				case "setProducto":
					//setReward(String idINBD, String Parametro, L2PcInstance player){
					if(!opera.isNumeric(eventParam1) || !opera.isNumeric(eventParam2))/*id item - cantidad*/{
						central.msgbox("EL id del Item y cantidad debe ser numerico", player);
						return htmlShow(player,eventParam4/*Evento*/,eventParam3/*ID IN BD*/);
					}
					if((central.getNombreITEMbyID(Integer.valueOf(eventParam1))==null) || central.getNombreITEMbyID(Integer.valueOf(eventParam1)).equals("none")){
						central.msgbox("EL id del Item ingresado no existe", player);
						return htmlShow(player,eventParam4/*Evento*/,eventParam3/*ID IN BD*/);
					}
					setReward(eventParam3, eventParam1+","+eventParam2, player);
					return htmlShow(player,eventParam4/*Evento*/,eventParam3/*ID IN BD*/);
				case "setLista":
					if(!opera.isNumeric(eventParam1) && opera.searchInArray(operadoresNumericos,eventParam2)){
						central.msgbox(language.getInstance().getMsg(player).ENTER_NUMBERS_ONLY, player);
						//return getConfigNPCZeuS(player, eventParam1);
						return htmlShow(player, eventParam3, eventParam2);
					}
					if(eventParam2.equals("317")){
						if(general.ANTIBOT_BORRAR_ITEM_ID.split(",").length>14){
							central.msgbox("You can not add more ID", player);
							return htmlShow(player, eventParam3, eventParam2);
						}
					}
					//setLista $idNpc "+idINBD+" "+Seccion+" 0 0
					setLista(eventParam2, eventParam1, player);
					return htmlShow(player, eventParam3, eventParam2);
				case "setstatus":
					status(player,Integer.valueOf(eventParam2));
					if(eventParam2.equals("344")){
						//dressme desactivar
						//dressme.getInstance().setNewConfigDressme();
					}
					return htmlShow(player,eventParam1/*Evento*/,eventParam2/*ID IN BD*/);
				case "suprdato":
					//suprdato "+Seccion+" "+idBox+" "+String.valueOf(_lista)+"
					if(eventParam2.equals("87")){
						setValue(eventParam2, RemoveItemVectorInteger(general.DESAFIO_NPC_BUSQUEDAS,eventParam3),player);
						return htmlShow(player, eventParam1,  eventParam2);
					}else if(eventParam2.equals("105")){
						//setValue(idINBD, Parametro, player);
						setValue(eventParam2,RemoveListaString(general.PINTAR_MATRIZ,eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("174")){
						setValue(eventParam2,RemoveListaString(general.ENCHANT_ANNOUCEMENT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("281")){
						setValue(eventParam2,RemoveListaString(general.get_AccessConfig(), eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("284")){
						setValue(eventParam2,RemoveListaString(general.OLY_SECOND_SHOW_OPPONET, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("286")){
						setValue(eventParam2,RemoveListaString(general.OLY_ACCESS_ID_MODIFICAR_POINT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("317")){
						setValue(eventParam2,RemoveListaString(general.ANTIBOT_BORRAR_ITEM_ID , eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("350")){
						setValue(eventParam2, RemoveItemVectorInteger(general.DROP_SEARCH_MOB_BLOCK_TELEPORT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("351")){
						setValue(eventParam2, RemoveItemVectorInteger(general.RAIDBOSS_ID_MOB_NO_TELEPORT, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("357")){
						setValue(eventParam2, RemoveItemVectorInteger(general.RETURN_BUFF_NOT_STEALING, eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);
					}else if(eventParam2.equals("486")){
						setValue(eventParam2, RemoveItemVectorInteger(general.EVENT_RAIDBOSS_RAID_ID , eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);						
					}else if(eventParam2.equals("566")){
						setValue(eventParam2, RemoveItemStringInteger(general.EVENT_TOWN_WAR_NPC_ID_HIDE , eventParam3),player);
						return htmlShow(player, eventParam1, eventParam2);						
					}
				case "supritem":
					String itemForElim = "";
					//supritem "+Seccion+" "+idBox+" "+pre+"
						if(eventParam2.equals("69")){
							itemForElim = general.VOTO_REWARD_TOPZONE;
						}else if(eventParam2.equals("70")){
							itemForElim = general.VOTO_REWARD_HOPZONE;
						}else if(eventParam2.equals("74")){
							itemForElim = general.VOTO_ITEM_BUFF_ENCHANT_PRICE;
						}else if(eventParam2.equals("83")){
							itemForElim = general.TELEPORT_PRICE;
						}else if(eventParam2.equals("85")){
							itemForElim = general.DESAFIO_85_PREMIO;
						}else if(eventParam2.equals("86")){
							itemForElim = general.DESAFIO_NOBLE_PREMIO;
						}else if(eventParam2.equals("89")){
							itemForElim = general.DROP_TELEPORT_COST;
						}else if(eventParam2.equals("91")){
							itemForElim = general.PARTY_FINDER_PRICE;
						}else if(eventParam2.equals("98")){
							itemForElim = general.FLAG_FINDER_PRICE;
						}else if(eventParam2.equals("104")){
							itemForElim = general.PINTAR_PRICE;
						}else if(eventParam2.equals("106")){
							itemForElim = general.AUGMENT_ITEM_PRICE;
						}else if(eventParam2.equals("107")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE;
						}else if(eventParam2.equals("111")){
							itemForElim = general.ENCHANT_ITEM_PRICE;
						}else if(eventParam2.equals("117")){
							itemForElim = general.RAIDBOSS_INFO_TELEPORT_PRICE;
						}else if(eventParam2.equals("122")){
							itemForElim = general.OPCIONES_CHAR_SEXO_ITEM_PRICE;
						}else if(eventParam2.equals("123")){
							itemForElim = general.OPCIONES_CHAR_NOBLE_ITEM_PRICE;
						}else if(eventParam2.equals("124")){
							itemForElim = general.OPCIONES_CHAR_LVL85_ITEM_PRICE;
						}else if(eventParam2.equals("125")){
							itemForElim = general.OPCIONES_CHAR_CAMBIO_NOMBRE_PJ_ITEM_PRICE;
						}else if(eventParam2.equals("126")){
							itemForElim = general.OPCIONES_CHAR_CAMBIO_NOMBRE_CLAN_ITEM_PRICE;
						}else if(eventParam2.equals("130")){
							itemForElim = general.ELEMENTAL_ITEM_PRICE;
						}else if(eventParam2.equals("133")){
							itemForElim = general.DELEVEL_PRICE;
						}else if(eventParam2.equals("255")){
							itemForElim = general.TRANSFORMATION_PRICE;
						}else if(eventParam2.equals("259")){
							itemForElim = general.TRANSFORMATION_ESPECIALES_PRICE;
						}else if(eventParam2.equals("260")){
							itemForElim = general.TRANSFORMATION_RAIDBOSS_PRICE;
						}else if(eventParam2.equals("265")){
							itemForElim = general.OPCIONES_CHAR_BUFFER_AIO_PRICE;
						}else if(eventParam2.equals("586")){
							itemForElim = general.OPCIONES_CHAR_BUFFER_AIO_PRICE_30;
						}else if(eventParam2.endsWith("277")){
							itemForElim = general.OPCIONES_CHAR_FAME_PRICE;
						}else if(eventParam2.endsWith("314")){
							itemForElim = general.VOTE_SHOW_ZEUS_TEMPORAL_PRICE;
						}else if(eventParam2.endsWith("341")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_PASSIVE;
						}else if(eventParam2.endsWith("342")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_CHANCE;
						}else if(eventParam2.endsWith("343")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_ACTIVE;
						}else if(eventParam2.endsWith("348")){
							itemForElim = general.DRESSME_NEW_DRESS_COST;
						}else if(eventParam2.endsWith("386")){
							itemForElim = general.PVP_REWARD_ITEMS;
						}else if(eventParam2.endsWith("387")){
							itemForElim = general.PVP_PARTY_REWARD_ITEMS;
						}else if(eventParam2.endsWith("490")){
							itemForElim = general.EVENT_RAIDBOSS_REWARD_WIN;
						}else if(eventParam2.endsWith("491")){
							itemForElim = general.EVENT_RAIDBOSS_REWARD_LOOSER;
						}else if(eventParam2.endsWith("512")){
							itemForElim = general.VOTO_REWARD_AUTO_REWARD_META_HOPZONE;
						}else if(eventParam2.endsWith("513")){
							itemForElim = general.VOTO_REWARD_AUTO_REWARD_META_TOPZONE;
						}else if(eventParam2.endsWith("561")){
							itemForElim = general.EVENT_TOWN_WAR_REWARD_GENERAL;
						}else if(eventParam2.endsWith("562")){
							itemForElim = general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER;
						}else if(eventParam2.endsWith("601")){
							itemForElim = general.OPCIONES_CHAR_REDUCE_PK_PRICE;
						}else if(eventParam2.endsWith("607")){
							itemForElim = general.INSTANCE_ZONE_COST;
						}else if(eventParam2.endsWith("614")){
							itemForElim = general.JAIL_BAIL_COST;
						}else if(eventParam2.endsWith("902")){
							itemForElim = general.AUGMENT_SPECIAL_PRICE_SYMBOL;
						}
					if(itemForElim.length()>0){
						saveReward(RemovePremio(itemForElim, eventParam3),eventParam2,player);
					}
					return htmlShow(player,eventParam1,eventParam2);

			}
		return "";
	}

	private static String getTextoParam5(String event){
		int contador = 0;
		String Mensaje = "";
		for (String _Parte : event.split(" ")){
			if(contador>5){
				if(Mensaje.length()>0){
					Mensaje+= " ";
				}
				Mensaje+=_Parte;
			}
			contador++;
		}
		return Mensaje;
	}

	private static void saveReward(String premios, String idINBD, L2PcInstance player){
		setValue(idINBD, premios, player);
	}

	private static String htmlShow(L2PcInstance player, String evento, String idBox){
		return htmlShow(player, evento, idBox,"0");
	}
	
	private static String htmlShow(L2PcInstance player,String evento,String idBox, String showInNewWindows){
		switch(evento){
			case "general":
				return getConfigNPCZeuS(player, idBox);
			case "vote":
				return getConfigVote(player, idBox);
			case "teleport":
				return getConfigTeleport(player, idBox);
			case "autopots":
				return getConfigAutopots(player, idBox);
			case "dropsear":
				return getConfigDropSearch(player, idBox);
			case "partyfin":
				return getConfigPartyFinder(player, idBox);
			case "flagfin":
				return getConfigFlagFinder(player, idBox);
			case "augmentspe":
				return getConfigAugmentEspecial(player, idBox);
			case "enchantspe":
				return getConfigEnchant(player, idBox);
			case "instancezone":
				return getConfigInstanceZone(player, idBox);
			case "desafio":
				return getConfigDesafios(player, idBox);
			case "colorname":
				return getConfigColor(player, idBox);
			case "elementalspe":
				return getConfigElemental(player, idBox);
			case "bossinfo":
				return getConfigRaidbossInfo(player, idBox);
			case "opcvarias":
				return getConfigVarios(player, idBox);
			case "opcvarias2":
				return getConfigVarios2(player, idBox);
			case "pvppkColor":
				return getConfigPVPPKColor(player, "0");
			case "pvpColor":
				return getConfigPVPColor(player, idBox);
			case "pkColor":
				return getConfigPKColor(player, idBox);
			case "pvpConfig":
				return getConfigPVP(player, idBox);
			case "raidconfig":
				return getConfigRaidAnnoucement(player, idBox);
			case "transform":
				return getConfigTransform(player, idBox);
			case "Olymp":
				return getConfigOly(player, idBox);
			case "Antibot":
				return getConfigAntiBot(player, idBox);
			case "Antibot2":
				return getConfigAntiBot2(player, idBox);
			case "banip":
				return getConfigIPBan(player,idBox);
			case "castlema":
				return getConfigCastleManager(player,idBox);
			case "dressme":
				return getConfigDressme(player, idBox);
			case "jailbail":
				return getConfigJailBail(player, idBox);
			case "cancelbuff":
				return getConfigCancelBuff(player, idBox);
			case "comunidad":
				return getConfigComunidad(player,idBox);
			case "cbpartyfinder":
				return getConfigComunidadPartyFinder(player, idBox);
			case "overenchant":
				return getConfigOverEnchant(player, idBox);
			case "emailregister":
				return getConfigRegister(player,idBox);
			case "dualbox":
				return getConfigDualBox(player,idBox);
			case "ChatConfig":
				return getConfigChat(player, idBox);
			case "RaidBossEvent":
				return getConfigRaidBossEvent(player,idBox);
			case "TownWarEvent":
				return getConfigTownWarEvent(player, idBox);
			case "changeName":
				return getConfigChangeName(player, idBox);
		}
		return "";
	}

	private static void status(L2PcInstance player, int idProc){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(1,"+idProc+",'','','')";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET ADMIN BUTTON->"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET ADMIN BUTTON->"+a.getMessage());
		}

		if(Respu.equals("cor")){
			general.loadConfigs();
		}

	}

	private static void setValue(String idINBD, String Parametro, L2PcInstance player){
			if(!opera.isMaster(player)){
				return;
			}
			String qry = "call sp_zeus_config(2,"+idINBD+",?,'','')";
			Connection conn = null;
			CallableStatement psqry;
			String Respu ="";
			try{
				conn = L2DatabaseFactory.getInstance().getConnection();
				psqry = conn.prepareCall(qry);
				psqry.setString(1, Parametro);
				ResultSet rss = psqry.executeQuery();
				rss.next();
				Respu = rss.getString(1);
			}catch(SQLException e){
				_log.warning("SET ADMIN BUTTON->"+e.getMessage());
			}
			try{
				conn.close();
			}catch(SQLException a ){
				_log.warning("SET ADMIN BUTTON->"+a.getMessage());
			}

			if(Respu.equals("cor")){
				general.loadConfigs();
			}
	}

	private static void setLista(String idINBD, String Parametro, L2PcInstance player){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(4,"+idINBD+",?,'','')";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			psqry.setString(1, Parametro);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET ADMIN BUTTON->"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET ADMIN BUTTON->"+a.getMessage());
		}

		if(Respu.equals("cor")){
			general.loadConfigs();
		}
	}


	private static void setReward(String idINBD, String Parametro, L2PcInstance player){
		if(!opera.isMaster(player)){
			return;
		}
		String qry = "call sp_zeus_config(3,"+idINBD+",'"+Parametro+"','','')";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET ADMIN BUTTON->"+e.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET ADMIN BUTTON->"+a.getMessage());
		}

		if(Respu.equals("cor")){
			general.loadConfigs();
		}
	}

}
