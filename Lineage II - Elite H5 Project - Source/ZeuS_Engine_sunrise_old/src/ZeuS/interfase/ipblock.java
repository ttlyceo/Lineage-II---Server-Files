package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.GameServer;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.procedimientos.opera;

public class ipblock extends ZeuS{

	private static final Logger _log = Logger.getLogger(ipblock.class.getName());

	public static boolean isBanIP(L2PcInstance player){
		String ipInternet = getIP_Internet(player);
		String ipMaquina = getIPpc(player);

		if(opera.isMaster(player)){
			return false;
		}

		if(general.banIPInterner(ipInternet) || general.banIPMaquina(ipMaquina)){
			return true;
		}

		return false;

	}


	protected static boolean toBD(int tipo, String ipInternet, String ipRed){
		String qry = "call sp_ip(?,?,?)";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(qry);
			psqry.setInt(1, tipo);
			psqry.setString(2, ipInternet);
			psqry.setString(3, ipRed);
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException a){
			_log.warning("IP BLOCK->"+ a.getMessage());
			return false;
		}

		try{
			conn.close();
		}catch(Exception a){

		}
		if(Respu.equals("err")){
			return false;
		}
		general.loadConfigBanIP();
		return true;
	}

	protected static boolean recordIP(L2PcInstance player){
		String ipInternet = getIP_Internet(player);
		String ipMaquina = getIPpc(player);
		return toBD(1,ipInternet,ipMaquina);
	}
	protected static boolean recordIP(String ipInternet){
		return toBD(1,ipInternet,"");
	}
	protected static boolean recordIP(String ipInternet, String ipMaquina){
		return toBD(1,ipInternet,ipMaquina);
	}

	protected static void errorTipo(L2PcInstance player){
		central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
	}

	protected static void banIP(String Command, L2PcInstance player){
		String[]param = Command.split(" ");
		L2PcInstance playerBan = null;
		if(param.length>1){
			playerBan = opera.getPlayerbyName(param[1]);
			if(playerBan==null){
				central.msgbox("Incorrect name ("+param[1]+") or not online", player);
				return;
			}
		}else{
			Object target = player.getTarget();
			if(target instanceof L2PcInstance){
				playerBan = (L2PcInstance)target;
			}else{
				central.msgbox("Incorrect Target", player);
				return;
			}
		}

		if(playerBan!=null){
			recordIP(playerBan);
			playerBan.getClient().closeNow();
			DisconnectOthers(getIP_Internet(playerBan),getIPpc(playerBan),playerBan);
		}
	}

	public static void bypass(String Command, L2PcInstance player){
		//_log.warning(Command);
		if(Command.equals("admin_zeus_ipblock")){
			sendhtmlBLockIP(player, 0);
			return;
		}
		if(Command.startsWith("admin_zeus_banip")){
			banIP(Command, player);
			return;
		}

		String[]Param=Command.split(" ");

		if(Param.length>1){
			if(Param[1].equals("ipblock")){
				if((Param.length < 4) || (Param.length > 4)){
					errorTipo(player);
				}else{
					if(Param[2].equals("add1")){
						if(recordIP(Param[3])){
							central.msgbox("IP banned to: " + Param[3] , player);
							DisconnectOthers(Param[3],"",player);
						}else{
							central.msgbox("Fail IP banned to: " + Param[3] , player);
						}
					}else if(Param[2].equals("suprLan")){
						toBD(3,"",Param[3]);
					}else if(Param[2].equals("suprInt")){
						toBD(2,Param[3],"");
					}else if(Param[2].equals("listaip")){
						sendhtmlBLockIP(player, Integer.valueOf(Param[3]));
						return;
					}

					sendhtmlBLockIP(player, 0);
					return;
				}
			}
		}
	}

	protected static void DisconnectOthers(String IPInternet, String IPLocal, L2PcInstance Player){
		if(!general.BANIP_DISCONNECT_ALL_PLAYER){
			return;
		}

		Collection<L2PcInstance> pls = opera.getAllPlayerOnWorld();
		for(L2PcInstance onlinePlayer : pls){
			if(IPInternet.length()>0){
				if(getIP_Internet(onlinePlayer).equals(IPInternet)){
					try{
						if(!opera.isMaster(onlinePlayer)){
							onlinePlayer.getClient().closeNow();
						}
					}catch(Exception a){

					}
				}
			}
			if(IPLocal.length()>0){
				if(getIp_pc(onlinePlayer).equals(IPLocal) && getIP_Internet(onlinePlayer).equals(IPInternet)){
					try{
						if(!opera.isMaster(onlinePlayer)){
							onlinePlayer.getClient().closeNow();
						}
					}catch(Exception a){

					}
				}
			}
		}

	}

	protected static String getAllIPBanned(int Pagina){
		int MaximoPagina = 30;

		String btnBypassNext = "bypass -h admin_zeus_op ipblock listaip " + String.valueOf(Pagina + 1);
		String btnBypassPreview = "bypass -h admin_zeus_op ipblock listaip " + String.valueOf(Pagina - 1);

		String BtnNext = "<button value=\"NEXT\" action=\""+ btnBypassNext +"\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String BtnPre = "<button value=\"PREV\" action=\""+ btnBypassPreview +"\" width=80 height=20 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String btnBypass = "bypass -h admin_zeus_op ipblock suprInt %IP%";
		String BtnAdd = "<button value=\"W: %IP%\" action=\""+ btnBypass +"\" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String HTML = "<table width=280>";
		int paso =1;

		int desdeContador = Pagina * MaximoPagina;
		int Contador = 0;
		int ContadorIndi = 0;

		boolean Continua = false;


		for(String IPInternet: general.getBANIPINTERNET()){
			if(Contador >= desdeContador){
				if(ContadorIndi< MaximoPagina){
					if(IPInternet.length()>0){
						if(paso == 1){
							HTML += "<tr>";
						}
						HTML += "<td width=140>" + BtnAdd.replace("%IP%", IPInternet) + "</td>";
						if((paso%2)==0){
							paso = 1;
							HTML += "</tr>";
						}else{
							paso++;
						}
					}
					ContadorIndi++;
				}else{
					Continua = true;
				}
			}
			Contador ++;
		}
		btnBypass = "bypass -h admin_zeus_op ipblock suprLan %IP%";
		BtnAdd = "<button value=\"L: %IP%\" action=\""+ btnBypass +"\" width=130 height=22 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		for(String IPLocal: general.getBANIPMAQUINA()){
			if(Contador >= desdeContador){
				if(ContadorIndi< MaximoPagina){
					if(IPLocal.length()>0){
						if(paso == 1){
							HTML += "<tr>";
						}
						HTML += "<td width=140>" + BtnAdd.replace("%IP%", IPLocal) + "</td>";
						if((paso%2)==0){
							paso = 1;
							HTML += "</tr>";
						}else{
							paso++;
						}
					}
					ContadorIndi++;
				}else{
					Continua = true;
				}
			}
			Contador ++;
		}

		if(HTML.endsWith("</td>")){
			HTML +="</tr>";
		}

		HTML +="</table>";

		String grillabtn = "<center><table width=260><tr>";

		if(Pagina>0){
			grillabtn +="<td width=130 align=CENTER>"+BtnPre+"</td>";
		}else{
			grillabtn +="<td width=130 align=CENTER></td>";
		}

		if(Continua){
			grillabtn +="<td width=130 align=CENTER>"+BtnNext+"</td>";
		}else{
			grillabtn +="<td width=130 align=CENTER></td>";
		}

		grillabtn+="</tr></table></center><br>";

		HTML += central.LineaDivisora(1) + central.headFormat(grillabtn) + central.LineaDivisora(1);

		return HTML;
	}

	public static void sendhtmlBLockIP(L2PcInstance player, int pagina){
		if(!opera.isMaster(player)){
			return;
		}

		//action= \"bypass -h admin_zeus_shop
		String btnBypass = "bypass -h admin_zeus_op ipblock add1 $txt_ip";
		String BtnAdd = "<button value=\"Block\" action=\""+ btnBypass +"\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

		String HTML = "<html><title>" + general.TITULO_NPC() + "</title><body><center>";
		HTML += central.LineaDivisora(1) + central.headFormat("IP BLOCK") + central.LineaDivisora(1);
		String txtIPBlock = "<edit var=\"txt_ip\" width=160>";
		HTML += central.LineaDivisora(1) + central.headFormat("Input the IP to blocking"+txtIPBlock+BtnAdd+"<br>","LEVEL") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("Select for Delete IP<br1>L: LAN IP   /   W: WAN IP","WHITE") + central.LineaDivisora(1);
		HTML += getAllIPBanned(pagina);
		HTML += "</center>"+ central.getPieHTML() +"</body></html>";
		opera.enviarHTML(player, HTML);
	}
}
