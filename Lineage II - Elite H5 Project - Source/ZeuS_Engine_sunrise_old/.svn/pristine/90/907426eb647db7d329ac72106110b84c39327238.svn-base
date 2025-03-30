package ZeuS.interfase;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.data.xml.impl.BuyListData;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.data.xml.impl.MultisellData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.buylist.L2BuyList;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.ExBuyList;
import l2r.gameserver.network.serverpackets.ExBuySellList;
import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

public class shop {
	private static Logger _log = Logger.getLogger(shop.class.getName());

	protected final static String cmbTipo = "<combobox width=80 var=cmb_tipo list=SECC;BUYLIST;MULTISELL;EXEC_MULTISELL;HTML>";
	protected final static String txtNombre = "<edit var=\"txt_name\" width=160>";
	protected final static String txtDescrip = "<edit var=\"txt_Descrip\" width=160>";
	protected final static String txtLogo = "<multiedit var=\"txt_Logo\" width=150>";
	protected final static String txtidArch = "<edit var=\"txt_idArch\" width=150>";
	protected final static String txtiditemtoShow = "<edit var=\"txt_idItem\" width=150>";


	protected final static String btnUp = "<button value=\"\" action= \"bypass -h admin_zeus_shop z_c_c move up %IDSHOP% %Posi% %TS% %IDS%\" width=16 height=16 back=\"L2UI_CH3.shortcut_nextv_over\" fore=\"L2UI_CH3.shortcut_nextv_over\">";
	protected final static String btnDown = "<button value=\"\" action= \"bypass -h admin_zeus_shop z_c_c move down %IDSHOP% %Posi% %TS% %IDS%\" width=16 height=16 back=\"L2UI_CH3.shortcut_prevv_down\" fore=\"L2UI_CH3.shortcut_prevv_down\">";
	protected final static String btnSupri = "<button value=\"SUPR\" action= \"bypass -h admin_zeus_shop z_c_c supr %IDSHOP% %TS% %IDS%\" width=48 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
	protected final static String btnAdd = "<button value=\"ADD\" action= \"bypass -h admin_zeus_shop z_c_c add %IDSHOP% %TS% %IDS%\" width=48 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static String getHTMLCentral(L2PcInstance player, String NombreSeccion, String idSeccion, boolean inCentral){
		String HTML ="<html><title>" + general.TITULO_NPC() + "</title><body><center>" ;

		HTML += central.LineaDivisora(1) + central.headFormat("GM SHOP EDIT - " + NombreSeccion) + central.LineaDivisora(1);

		if(idSeccion.equals("-1")){
			HTML += central.LineaDivisora(1) + central.headFormat(btnAdd.replace("%IDSHOP%", "-1").replace("%Posi%", "0").replace("%TS%", "secc").replace("%IDS%", "-1").replace("width=48", "width=120")) + central.LineaDivisora(1);
		}

		HTML += "<table width=280>";
		//String btnShopFormato = "<button value=\"%NOM%\" action=\"bypass -h %BYPASS%\" width=200 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String Boton="";
		String ByPass ="";
		String btnAtras = "";
		
		
		
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    	  	//Entrada.getKey().toString()
    	  	//Entrada.getValue().toString()
    		Map<String, String>Temporal = new HashMap<String, String>();
    		Temporal = (Map<String, String>) Entrada.getValue();

    		if(Temporal.get(general.shopData.ID_SEC.name()).equals(idSeccion)){
				//ByPass ="voice .z_c_c " + Shop[0] + " " + Shop[7] + " " + Shop[4] + " " + Shop[6];
				if(Temporal.get(general.shopData.TIPO.name()).equals("secc")){
					ByPass ="admin_zeus_shop z_c_c " + Temporal.get(general.shopData.ID.name()) + " " + Temporal.get(general.shopData.POS.name()) + " " + Temporal.get(general.shopData.TIPO.name()) + " " + Temporal.get(general.shopData.ID_SEC.name());
				}else{
					ByPass ="admin_zeus_shop z_c_c " + Temporal.get(general.shopData.ID.name()) + " " + idSeccion + " " + Temporal.get(general.shopData.TIPO.name()) + " " + Temporal.get(general.shopData.ID_SEC.name());
				}
				Boton = "<button value=\"%NOM%\" action=\"bypass -h %BYPASS%\" width=120 height=18 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
				HTML += "<tr><td width=120>" + Boton.replace("%NOM%", Temporal.get(general.shopData.NOM.name())).replace("%BYPASS%", ByPass)+"</td>";
				HTML += "<td width=16>" + btnUp.replace("%IDSHOP%", Temporal.get(general.shopData.ID.name())).replace("%Posi%", Temporal.get(general.shopData.POS.name())).replace("%TS%", Temporal.get(general.shopData.TIPO.name())).replace("%IDS%", Temporal.get(general.shopData.ID_SEC.name())) +"</td>";
				HTML += "<td width=16>" + btnDown.replace("%IDSHOP%", Temporal.get(general.shopData.ID.name())).replace("%Posi%", Temporal.get(general.shopData.POS.name())).replace("%TS%", Temporal.get(general.shopData.TIPO.name())).replace("%IDS%", Temporal.get(general.shopData.ID_SEC.name())) +"</td>";
				if(Temporal.get(general.shopData.TIPO.name()).equals("secc")){
					HTML += "<td width=16>" + btnAdd.replace("%IDSHOP%", Temporal.get(general.shopData.ID.name())).replace("%Posi%", Temporal.get(general.shopData.POS.name())).replace("%TS%", Temporal.get(general.shopData.TIPO.name())).replace("%IDS%", Temporal.get(general.shopData.ID_SEC.name())) +"</td>";
				}else{
					HTML += "<td width=16></td>";
				}
				HTML += "<td width=16>" + btnSupri.replace("%IDSHOP%", Temporal.get(general.shopData.ID.name())).replace("%Posi%", Temporal.get(general.shopData.POS.name())).replace("%TS%", Temporal.get(general.shopData.TIPO.name())).replace("%IDS%", Temporal.get(general.shopData.ID_SEC.name())) +"</td>";
				HTML += "</tr>";
			}	    		
	    }
		

		HTML += "</table>";
		String btnNuevo = "";
		if(!inCentral && (btnAtras.length()==0) && !idSeccion.equals("-1")){
			ByPass ="admin_zeus_shop z_c_b 0 "+ getidSubcate(idSeccion) +" secc 0";
			btnAtras = "<button value=\"Atras\" action=\"bypass -h %BYPASS%\" width=120 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">".replace("%BYPASS%", ByPass);
			btnNuevo = "<button value=\"ADD\" action= \"bypass -h admin_zeus_shop z_c_c add "+idSeccion+" secc -1\" width=120 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}
		if(btnAtras.length()>0) {
			HTML += central.LineaDivisora(1) + central.headFormat(btnAtras+btnNuevo) + central.LineaDivisora(1);
		}
		HTML += "</center></body></html>";
		return HTML;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static String getidSubcate(String idCategoria){
		String retorno ="";
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    	  	//Entrada.getKey().toString()
    	  	//Entrada.getValue().toString()
    		Map<String, String>Temporal = new HashMap<String, String>();
    		Temporal = (Map<String, String>) Entrada.getValue();
			if(Temporal.get(general.shopData.ID.name()).equals(idCategoria)){
				return Temporal.get(general.shopData.ID_SEC.name());
			}

	    }		
		return retorno;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static String getSecciones(){
		String Retorno = "";
		
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    	  	//Entrada.getKey().toString()
    	  	//Entrada.getValue().toString()
    		Map<String, String>Temporal = new HashMap<String, String>();
    		Temporal = (Map<String, String>) Entrada.getValue();
    		if(Temporal.get(general.shopData.TIPO.name()).equals("secc")){
				if(Retorno.length()>0){
					Retorno += ";";
				}
				Retorno += Temporal.get(general.shopData.NOM.name());
			}

	    }		
		return Retorno;
	}

	public static boolean deleteShop(L2PcInstance player, String idShop){
		return setNewShop(player, 3, Integer.valueOf(idShop), -1, "", "", "", "", "-1", "-1","0");
	}

	public static boolean changePosition(L2PcInstance player,String idShop, String tipo){
		return setNewShop(player, 2, Integer.valueOf(idShop), -1, tipo, "", "", "", "-1", "-1","0");
	}

	public static boolean setNewShop(L2PcInstance player, int tipo, int idseccion, String Nombre, String tipoLink, String idArchivo, String idItemToShow){
		return setNewShop(player, tipo, -1, idseccion, Nombre, "", "", tipoLink, idArchivo, "-1",idItemToShow);
	}

	public static boolean setNewShop(L2PcInstance player, int tipo, int idshop, int idseccion, String Nombre, String Descrip, String Imagen, String tipoLink, String idArchivo, String Posicion, String idShoptoShow){
		String Consulta = "call sp_shop(?,?,?,?,?,?,?,?,?,?)";
		Connection conn = null;
		CallableStatement psqry;
		String Respu ="";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareCall(Consulta);
			psqry.setInt(1, tipo);
			psqry.setInt(2, idshop);
			psqry.setInt(3, idseccion);
			psqry.setString(4, Nombre.replace("_", " ").replace("'", ""));
			psqry.setString(5, Descrip.replace("_", " ").replace("'", ""));
			psqry.setString(6, Imagen);
			psqry.setString(7, tipoLink.toLowerCase() );
			psqry.setString(8,idArchivo);
			psqry.setString(9,Posicion);
			psqry.setInt(10, Integer.valueOf(idShoptoShow));
			ResultSet rss = psqry.executeQuery();
			rss.next();
			Respu = rss.getString(1);
		}catch(SQLException e){
			_log.warning("SET Shop->"+e.getMessage());
			return false;
		}
		try{
			conn.close();
		}catch(SQLException a ){
			_log.warning("SET Shop->"+a.getMessage());

		}
		if(Respu.equals("cor")){
			central.msgbox("Shop Operation OK", player);
			general.loadConfigsShop();
		}else{
			central.msgbox("Shop Operation FAIL", player);
			return false;
		}
		return true;
	}

	public static void ShopByPass(L2PcInstance player, String param){
		String Retorno ="";
		String Parametros[] = param.split(" ");
		if(Parametros.length>1){
			if(Parametros[4].equals("secc")){
				if(Parametros[1].equals("z_c_c")){
					if(Parametros[2].equals("add")){
						Retorno = getShop_Admin_SHOP_ADD(player, getSeccionName(Parametros[3]) ,Parametros[3]);
					}else if(Parametros[2].equals("supr")){
						deleteShop(player,Parametros[3]);
						if(!Parametros[5].equals("-1")){
							Retorno = getHTMLCentral(player, getSeccionName(Parametros[5]), Parametros[5],false);
						}
					}else{
						Retorno = getHTMLCentral(player, getSeccionName(Parametros[2]), Parametros[2],false);
					}
				}else if(Parametros[1].equals("z_c_b")){
					Retorno = getHTMLCentral(player, getSeccionName(Parametros[3]), Parametros[3],false);
				}
			}else if(Parametros[1].equals("z_c_g")){
				if((Parametros.length<8) || (Parametros.length>8)){
					central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
					return;
				}else{
					if(!opera.isNumeric(Parametros[7])){
						central.msgbox("Only number can put on Item Show", player);
						central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
						return;
					}
					if(setNewShop(player, 1, Integer.valueOf(Parametros[5]), Parametros[2].replace("_", " ").replace("'",""), Parametros[3], Parametros[4],Parametros[7])){
						Retorno = getHTMLCentral(player, getSeccionName(Parametros[5]), Parametros[5],false);
					}
				}
			}else if(Parametros[1].equals("z_c_c")){
				if (Parametros[2].equals("move")){
					changePosition(player, Parametros[4], Parametros[3].toUpperCase());
					Retorno = getHTMLCentral(player, getSeccionName(Parametros[7]), Parametros[7],false);
				}else if(Parametros[2].equals("supr")){
					deleteShop(player,Parametros[3]);
					if(!Parametros[5].equals("-1")){
						Retorno = getHTMLCentral(player, getSeccionName(Parametros[5]), Parametros[5],false);
					}
				}
			}else if(Parametros[1].equals("z_c_gc")){
				if((Parametros.length < 9) || (Parametros.length > 9)){
					central.sendHtml(player, htmls.ErrorTipeoEspacio(false));
					return;
				}else{
					//(L2PcInstance player, int tipo, int idshop, int idseccion, String Nombre, String Descrip, String Imagen, String tipoLink, String idArchivo, String Posicion){
					setNewShop(player, 1, Integer.valueOf(Parametros[5]), Integer.valueOf(Parametros[5]), Parametros[2].replace("_", " ").replace("'",""), Parametros[8].replace("_", " ").replace("'",""), Parametros[7], Parametros[3], Parametros[4], "-1","0");
				}
			}
		}

		if(Retorno.length()==0){
			Retorno = getHTMLCentral(player,"Central","-1",true);
		}

		opera.enviarHTML(player, Retorno);
	}




	public static String getShop_Admin_SHOP_ADD(L2PcInstance Player, String nomSeccion, String idSeccion){
		String HTML = "<html><title>" +general.TITULO_NPC() + "</title><body>";

		String ByPass = "";


		ByPass ="admin_zeus_shop z_c_b 0 "+ idSeccion +" secc 0";
		String btnAtras = "<button value=\"Back\" action=\"bypass -h %BYPASS%\" width=120 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">".replace("%BYPASS%", ByPass);

		HTML += central.LineaDivisora(1) + central.headFormat("ZEUS GM SHOP EDIT") + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.headFormat("NAME <br1>Replace spaces for _" + txtNombre + "<br>");
		if(idSeccion.equals("-1")){
			HTML += central.LineaDivisora(1) + central.headFormat("<table width=280><tr><td width=120>DESCRIPTION</td><td width=160>"+txtDescrip+"</td></tr></table><br>");
			HTML += central.LineaDivisora(1) + central.headFormat("<table width=280><tr><td width=120>LOGO</td><td width=160>"+txtLogo+"</td></tr></table><br>");
			ByPass = "admin_zeus_shop z_c_gc $txt_name $cmb_tipo $txt_idArch "+idSeccion+" "+nomSeccion.replace(" ", "_") + " $txt_Logo $txt_Descrip";
		}else{
			ByPass = "admin_zeus_shop z_c_g $txt_name $cmb_tipo $txt_idArch "+idSeccion+" "+nomSeccion.replace(" ", "_") + " $txt_idItem";
		}
		String btnGuardar = "<button value=\"Save\" action=\"bypass -h %BYPASS%\" width=120 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">".replace("%BYPASS%", ByPass);
		HTML += central.LineaDivisora(1) + central.headFormat("<table width=280><tr><td width=120>ID ITEM TO SHOW EX.</td><td width=160>"+txtiditemtoShow+"</td></tr></table><br>");
		HTML += central.LineaDivisora(1) + central.headFormat("<table width=280><tr><td width=120>SECTION</td><td width=160>"+nomSeccion+"</td></tr></table><br>");
		HTML += central.LineaDivisora(1) + central.headFormat("<table width=280><tr><td width=120>TYPE</td><td width=160>"+cmbTipo+"</td></tr></table><br>");
		HTML += central.LineaDivisora(1) + central.headFormat("<table width=280><tr><td width=120>ID ARCH.</td><td width=160>"+txtidArch+"</td></tr></table><br>");
		HTML += central.LineaDivisora(1) + central.headFormat(btnGuardar+btnAtras) + central.LineaDivisora(1);
		HTML += "</body></html>";
		return HTML;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static String getShopCentral(L2PcInstance player){
		String html ="<html><title>"+ general.TITULO_NPC() +"</title><body>";
		html += central.LineaDivisora(1) + central.headFormat("Central Shop") + central.LineaDivisora(1);

		
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		Map<String, String>Temporal = new HashMap<String, String>();
    		Temporal = (Map<String, String>) Entrada.getValue();
			if(Temporal.get(general.shopData.ID_SEC.name()) .equals("-1")){
				html += getBtnCentrales(Temporal,player);
			}

	    }	
		html += central.getPieHTML() + "</body></html>";
		return html;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected static String getSeccionName(String idSeccion){
		String Name ="";
			if(idSeccion.equals("-1")){
				return "MAIN_INDEX";
			}
			
			
			Iterator itr = general.SHOP_DATA.entrySet().iterator();
		    while(itr.hasNext()){
		    	Map.Entry Entrada = (Map.Entry)itr.next();
	    		Map<String, String>Temporal = new HashMap<String, String>();
	    		Temporal = (Map<String, String>) Entrada.getValue();
				if(Temporal.get(general.shopData.ID.name()).equals(idSeccion)){
					return Temporal.get(general.shopData.NOM.name());
				}
		    }
		return Name;
	}
	
	
	
	protected static void showBuylist(L2PcInstance player, int idBuylist){
			player.setIsUsingAioMultisell(true);
			final L2BuyList buyList = BuyListData.getInstance().getBuyList(idBuylist);
			if (buyList == null)
			{
				_log.warning("BuyList not found! BuyListId:" + idBuylist);
				player.sendPacket(ActionFailed.STATIC_PACKET);
				return;
			}
			
			
			final double taxRate = 0;
			
			player.tempInventoryDisable();
			
			player.sendPacket(new ExBuyList(buyList, player, taxRate));
			player.sendPacket(new ExBuySellList(player, taxRate, false));
			player.sendPacket(ActionFailed.STATIC_PACKET);	
	}

	protected static String getImagenItem(int idItem){
		String Retorno ="";
			try{
				Retorno = ItemData.getInstance().getTemplate(idItem).getIcon();
			}catch(Exception a){
				Retorno ="";
			}

		return Retorno;
	}

	@SuppressWarnings({ "unused", "rawtypes", "unchecked" })
	public static String getShopSeccion(L2PcInstance player, String idSeccion){
		String btnShopFormato = "<button value=\"%NOM%\" action=\"bypass -h %BYPASS%\" width=200 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		String btnShop ="";
		String HTML = "<html><title>"+ general.TITULO_NPC() +"</title><body>";
		HTML += central.LineaDivisora(1) + central.headFormat("Central Shop - " + getSeccionName(idSeccion)) + central.LineaDivisora(1);
		HTML += "<table width=280>";
		String btnAtras = "";
		String IconoImagen = "";
		String[] ColorFondo = {" bgcolor=151515",""};
		int cont = 0;
		
		
		
		Iterator itr = general.SHOP_DATA.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
    		Map<String, String>Temporal = new HashMap<String, String>();
    		Temporal = (Map<String, String>) Entrada.getValue();

    		if(Temporal.get(general.shopData.ID_SEC.name()).equals(idSeccion)){
				IconoImagen = getImagenItem(Integer.valueOf(Temporal.get(general.shopData.ID_ITEMSHOW.name())));
				btnShop = btnShopFormato.replace("%NOM%", Temporal.get(general.shopData.NOM.name())).replace("%BYPASS%", getByPass(Temporal, general.npcGlobal(player)));
				HTML += "<tr><td><table width=270"+ ColorFondo[cont%2] +"><tr>"+
				"<td width=40><img src=\""+ IconoImagen +"\" width=32 height=32></td>"+
				"<td width=220>"+
				"<table width=180>"+
				"<tr><td width=180><font color=\"EBDF6C\">"+ Temporal.get(general.shopData.NOM.name()) +"</font></td></tr>"+
				"<tr><td width=180><font color=b0bccc></font></td></tr>"+
				"</table>"+
				"</td>"+
				"<td width=40><button action=\"bypass -h "+ getByPass(Temporal, general.npcGlobal(player)) +"\" width=32 height=32 back=\"L2UI_CT1.MiniMap_DF_PlusBtn_Blue_Down\" fore=\"L2UI_CT1.MiniMap_DF_PlusBtn_Blue\"></td>"+
				"</tr></table></td></tr>";
				cont++;
			}    		
    		
	    }
		
		String idSub = getidSubcate(idSeccion);

		if(idSub.equals("-1")){
			btnAtras = "<button value=\"Back\" action=\"bypass -h ZeuSNPC shopBD 0 0 0\" width=98 height="+26+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}else{
			btnAtras = "<button value=\"Back\" action=\"bypass -h ZeuSNPC shopBD 1 "+ idSub +" 0\" width=98 height="+26+" back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		}

		HTML += "</table><br>";
		HTML += central.LineaDivisora(1) + central.headFormat(btnAtras) + central.LineaDivisora(1);
		HTML += central.LineaDivisora(1) + central.getPieHTML() + "</body></html>";
		return HTML;
	}
	
	protected static void showMultisell(int multisell, L2PcInstance player, boolean normal){
		try
		{
			player.setIsUsingAioMultisell(true);
			if(normal)
				MultisellData.getInstance().separateAndSend(multisell, player, null, false);
			else
				MultisellData.getInstance().separateAndSend(multisell, player, null, true);
		}
		catch (Exception e)
		{
			_log.warning("Error CB SHOP -> " + e.getMessage());
		}
	}	
	
	protected static String getByPass(Map<String, String> Shop, String idObject){
		return getByPass(Shop, idObject,true);
	}

	protected static String getByPass(Map<String, String> Shop, String idObject, boolean npc){
		String ByPass="";
		if(Shop.get(general.shopData.TIPO.name()).equals("secc")){
			if(npc)
				ByPass = "ZeuSNPC shopBD 1 "+ Shop.get(general.shopData.ID.name()) +" 0";
			else
				ByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";Shop;secc;"+Shop.get(general.shopData.ID.name())+";0;0;0;0";
		}else if(Shop.get(general.shopData.TIPO.name()).equals("buylist")){
			if(npc)
				ByPass = "npc_%objectId%_Buy "+Shop.get(general.shopData.ID_ARCHI.name());
			else
				ByPass = "bypass" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";Shop;buy;"+Shop.get(general.shopData.ID_ARCHI.name())+";0;0;0;0";
		}else if(Shop.get(general.shopData.TIPO.name()).equals("multisell")){
			if(npc)
				ByPass = "npc_%objectId%_multisell " + Shop.get(general.shopData.ID_ARCHI.name());
			else
				ByPass = "bypass" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";Shop;multisell;"+Shop.get(general.shopData.ID_ARCHI.name())+";0;0;0;0";
		}else if(Shop.get(general.shopData.TIPO.name()).equals("exec_multisell")){
			if(npc)
				ByPass = "npc_%objectId%_exc_multisell " + Shop.get(general.shopData.ID_ARCHI.name());
			else
				ByPass = "bypass" + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";Shop;exec_multisell;"+Shop.get(general.shopData.ID_ARCHI.name())+";0;0;0;0";
		}else if(Shop.get(general.shopData.TIPO.name()).equals("html")){
			if(npc){
				if(Shop.get(general.shopData.ID_ARCHI.name()).endsWith(".htm")){
					ByPass = "npc_%objectId%_Link merchant/AIONPC/" + Shop.get(general.shopData.ID_ARCHI.name());
				}else{
					ByPass = "npc_%objectId%_Link merchant/AIONPC/" + Shop.get(general.shopData.ID_ARCHI.name()) + ".htm";
				}
			}else{
				if(Shop.get(general.shopData.ID_ARCHI.name()).endsWith(".htm")){
					ByPass = "bypass npc_%objectId%_Link merchant/AIONPC/" + Shop.get(general.shopData.ID_ARCHI.name());
				}else{
					ByPass = "bypass npc_%objectId%_Link merchant/AIONPC/" + Shop.get(general.shopData.ID_ARCHI.name()) + ".htm";
				}				
			}
		}
		ByPass = ByPass.replace("%objectId%",idObject);
		return ByPass;
	}

	protected static String getBtnCentrales(Map<String, String>Shop, L2PcInstance player){
		String[] C_back = {"C4A23D","C4803D"};
		String idObject = general.npcGlobal(player);
		String formato = "<table width=280 border=0 bgcolor=151515><tr><td width=2 align=left></td>";
		String ByPass = getByPass(Shop, idObject);
		formato += "<td width=90 align=left><button value=\"" + Shop.get(general.shopData.NOM.name()) + "\" action=\"bypass -h "+ByPass+"\" width=85 height=26 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td>";
		formato += "<td width=32 align=left><img src=\""+ Shop.get(general.shopData.IMA.name()) +"\" width=32 height=28></td>";
		formato += "<td width=130 align=right><font color="+C_back[central.INT_PINTAR_GRILLA(0)]+">"+Shop.get(general.shopData.DESCRIP.name())+"</font></td>";
		formato += "</tr></table>";
		formato += central.LineaDivisora(1);
		return formato;
	}

}
