package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import ZeuS.Comunidad.Comunidad;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.Engine.enumBypass;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config.general;
import ZeuS.interfase.borrowAccount;
import ZeuS.interfase.central;
import ZeuS.interfase.wishList;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.idfactory.IdFactory;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.model.items.type.ArmorType;
import l2r.gameserver.model.items.type.ItemType;
import l2r.gameserver.model.items.type.WeaponType;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class v_bid_house {
	private static final Logger _log = Logger.getLogger(v_bid_house.class.getName());
	private static Vector<InfoBid> ITEMS_ON_BIDS = new Vector<InfoBid>(); 
	private static Map<Integer, _variableBidHouseCreate> PLAYER_VARIABLES = new HashMap<Integer, _variableBidHouseCreate>();
	static final int ID_OWNER_BID = -301;
	private static final int[]ID_DUAL_SWORD = {52,10004,10415,11251,11300,14570,16150,16154,16158,20295};
	private static Vector<Integer>VECTOR_ID_DUALSWORD = new Vector<Integer>();
	private static boolean TypeSearch_addToList(InfoBid ItemSelected, String tipoBusqueda){
		
		if(VECTOR_ID_DUALSWORD!=null){
			for(int i : ID_DUAL_SWORD){
				VECTOR_ID_DUALSWORD.add(i);
			}
		}
		
		String tipo = tipoBusqueda.split("_")[1];
		L2ItemInstance item = ItemSelected.getItemInstance();
		int BodyPart = item.getItem().getBodyPart();
		ItemType WType = item.getItem().getItemType();
		ItemType ArmorT = item.getItemType();
		switch(tipoBusqueda.split("_")[0]){
			case "JEWEL":
				switch (tipo) {
					case "EARRING":
						if(BodyPart == L2Item.SLOT_R_EAR || BodyPart == L2Item.SLOT_L_EAR || BodyPart == L2Item.SLOT_LR_EAR){
							return true;
						}
						break;
					case "RING":
						if(BodyPart == L2Item.SLOT_R_FINGER || BodyPart == L2Item.SLOT_L_FINGER || BodyPart == L2Item.SLOT_LR_FINGER){
							return true;
						}
						break;
					case "NECKLACE":
						if(BodyPart == L2Item.SLOT_NECK ){
							return true;
						}		
						break;
				}
				break;
			case "ARMOR":
				if(tipo.equals("HELMET") && BodyPart == L2Item.SLOT_HEAD){
					return true;
				}else if(tipo.equals("CHEST") && BodyPart == L2Item.SLOT_CHEST){
					return true;
				}else if(tipo.equals("LEGS") && BodyPart == L2Item.SLOT_LEGS){
					return true;
				}else if(tipo.equals("GLOVES") && BodyPart == L2Item.SLOT_GLOVES){
					return true;
				}else if(tipo.equals("SHOES") && BodyPart == L2Item.SLOT_FEET){
					return true;
				}else if(tipo.equals("CLOAK") && BodyPart == L2Item.SLOT_BACK){
					return true;
				}else if(tipo.equals("SHIRT") && BodyPart == L2Item.SLOT_UNDERWEAR){
					return true;
				}else if(tipo.equals("BELT") && BodyPart == L2Item.SLOT_BELT){
					return true;
				}else if(tipo.equals("SIGIL") && ( BodyPart == L2Item.SLOT_R_HAND && ArmorT == ArmorType.SIGIL ) ){
					return true;
				}else if(tipo.equals("SHIELD") && ( BodyPart == L2Item.SLOT_R_HAND && ArmorT == ArmorType.SHIELD ) ){
					return true;
				}
				break;
			case "WEAPON":
				if(tipo.equals("SWORD") && WType == WeaponType.SWORD){
					return true;
				}else if(tipo.equals("BIGSWORD") && (WType == WeaponType.SWORD && BodyPart == L2Item.SLOT_LR_HAND && !VECTOR_ID_DUALSWORD.contains(item.getItem().getId()))){
					return true;
				}else if(tipo.equals("ANCIENTSWORD") && WType == WeaponType.ANCIENTSWORD){
					return true;
				}else if(tipo.equals("BLUNT") && WType == WeaponType.BLUNT){
					return true;
				}else if(tipo.equals("BIGBLUNT") && (WType == WeaponType.BLUNT && BodyPart == L2Item.SLOT_LR_HAND)){
					return true;
				}else if(tipo.equals("DAGGER") && WType == WeaponType.DAGGER){
					return true;
				}else if(tipo.equals("DUALDAGGER") && WType == WeaponType.DUALDAGGER){
					return true;
				}else if(tipo.equals("BOW") && WType == WeaponType.BOW){
					return true;
				}else if(tipo.equals("CROSSBOW") && WType == WeaponType.CROSSBOW){
					return true;
				}else if(tipo.equals("POLE") && WType == WeaponType.POLE){
					return true;
				}else if(tipo.equals("FISTS") && (WType == WeaponType.FIST || WType == WeaponType.DUALFIST)){
					return true;
				}else if(tipo.equals("RAPIER") && WType == WeaponType.RAPIER){
					return true;
				}else if(tipo.equals("OTHER") && (WType == WeaponType.ETC || WType == WeaponType.FISHINGROD|| WType == WeaponType.NONE || WType == WeaponType.OWNTHING)){
					return true;
				}else if(tipo.equals("DUALSWORD") && VECTOR_ID_DUALSWORD.contains(item.getItem().getId())){
					return true;
				}
				break;
		}
		
		return false;
	}
	
	private static int getFeedFromItem(int idObjectItem){
		if(idObjectItem>0){
			try{
				L2Object object = L2World.getInstance().findObject(idObjectItem);
				if (object instanceof L2ItemInstance){
					L2ItemInstance item = (L2ItemInstance) object;
					int ReferentePrice = item.getReferencePrice();
					if(ReferentePrice>0){
						int FeedToBack = ( ReferentePrice * general.BIDHOUSE_PERCENT_FEED ) / 100 ;
						
						if(FeedToBack > general.BIDHOUSE_FEED_MASTER){
							return FeedToBack;
						}else{
							return general.BIDHOUSE_FEED_MASTER + FeedToBack;							
						}
					}
				}
			}catch(Exception a){
				_log.warning("Error ZeuS A ->" + a.getMessage());
				return general.BIDHOUSE_FEED_MASTER;
			}
		}
		return general.BIDHOUSE_FEED_MASTER;
	}
	
	private static InfoBid getInfoBids(int idObject){
		for(InfoBid T2 : ITEMS_ON_BIDS){
			if((T2.getObjectID() == idObject) && T2.isActive()){
				return T2;
			}
		}
		return null;
	}

	public static void setCancelMyBid(L2PcInstance player, boolean firtsTime, int _idObject){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bidhouse-cancel-my-offert-done.htm");
		String Actualizar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";"+ String.valueOf( PLAYER_VARIABLES.get(player.getObjectId()).getLastSection()) +";0;0;0;0;0;0";		
		int idObject = -1;
		if(_idObject>0){
			PLAYER_VARIABLES.get(player.getObjectId()).setRemoveObjectSetIDObject(_idObject);
			idObject = _idObject;
		}else{
			idObject = PLAYER_VARIABLES.get(player.getObjectId()).getRemoveObjectSetIDObject();
		}
		InfoBid _tempData = null;
		for(InfoBid tD : ITEMS_ON_BIDS){
			if((tD.getObjectID() == idObject) && tD.isActive()){
				_tempData = tD;
				break;
			}
		}
		boolean isBidder = false;
		boolean isTopBidder = false;
		if(_tempData != null){
			if(_tempData.getAllPlayersBids()!=null){
				if(_tempData.getAllPlayersBids().size()>0){
					if(_tempData.getAllPlayersBids().containsKey(player.getObjectId())){
						isBidder = true;
						isTopBidder = (_tempData.getActualBidPlayerID() == player.getObjectId());
					}
				}
			}
			
			if(isBidder && general.BIDHOUSE_CANCEL_TAX_FOR_BUYER <= 0){
				_tempData.cancelBidsByPlayer(player.getObjectId(), false);
				return;
			}
			
			if(isBidder && !isTopBidder && firtsTime){
				_tempData.cancelBidsByPlayer(player.getObjectId(), false);
				cbManager.separateAndSend(bypass(player, Actualizar),player);
				central.sendHtml(player, html.getHtml());
				return;
			}else if(isBidder && isTopBidder && firtsTime){
				long _tax = Long.valueOf( (_tempData.getActualBid() * general.BIDHOUSE_CANCEL_TAX_FOR_BUYER) / 100l);
				String _msge = language.getInstance().getMsg(player).BH_SORRY_YOU_HAVE_THE_FIRTS_PLACE_IN_THE_BID_NEED_PAY_PENALTY.replace("$Item", _tempData.getNameItemRequested()) .replace("$Quantity", opera.getFormatNumbers(_tax));
				cbManager.separateAndSend(bypass(player, Actualizar),player);					
				Dlg.sendDlg(player, _msge, IdDialog.ENGINE_BID_HOUSE_CANCEL_MY_OFFERT, 90);
			}else if(isBidder && isTopBidder && !firtsTime){
				isTopBidder = (_tempData.getActualBidPlayerID() == player.getObjectId());
				long _tax = Long.valueOf( (_tempData.getActualBid() * general.BIDHOUSE_CANCEL_TAX_FOR_BUYER) / 100l);
				int idItemRemove = _tempData.getIDItemRequested();
				if(!opera.haveItem(player, idItemRemove, _tax)){
					cbManager.separateAndSend(bypass(player, Actualizar),player);
					central.msgbox(language.getInstance().getMsg(player).BH_SORRY_BUT_YOU_DONT_HAVE_ITEMS_TO_PAY_THE_PENALTY.replace("$Item", _tempData.getNameItemRequested()) .replace("$Amount", opera.getFormatNumbers(_tax)) , player);
					return;
				}
				opera.removeItem(idItemRemove, _tax, player);
				_tempData.cancelBidsByPlayer(player.getObjectId(), false);
				cbManager.separateAndSend(bypass(player, Actualizar),player);
				central.sendHtml(player, html.getHtml());
			}
		}
	}
	
	private static void getInfoItemWindows(L2PcInstance player, int idObjecto, int seccion){

		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bidhouse-show-item-info.htm");
		
		String Elemental[] = new String[6];
		
		Elemental[0] = "<td fixwidth=257>Fire Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Fire_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
        "<img src=\"L2UI_CT1.Gauge_DF_Attribute_Fire\" width=%WIDTH% height=10></td></tr></table></td>";
		
		Elemental[1] = "<td fixwidth=257>Water Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Water_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Water\" width=%WIDTH% height=10></td></tr></table></td>";

		Elemental[2] = "<td fixwidth=257>Earth Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Earth_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
       "<img src=\"L2UI_CT1.Gauge_DF_Attribute_Earth\" width=%WIDTH% height=10></td></tr></table></td>";		
		
		Elemental[3] = "<td fixwidth=257>Wind Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Wind_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Wind\" width=%WIDTH% height=10></td></tr></table></td>";
		
		Elemental[4] = "<td fixwidth=257>Dark Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Dark_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Dark\" width=%WIDTH% height=10></td></tr></table></td>";
		
		Elemental[5] = "<td fixwidth=257>Holy Atribute (%POWER%)<br1><table width=257 background=L2UI_CT1.Gauge_DF_Attribute_Divine_bg cellspacing=0 cellpadding=0><tr><td fixwidth=257 height=18>"+
		"<img src=\"L2UI_CT1.Gauge_DF_Attribute_Divine\" width=%WIDTH% height=10></td></tr></table></td>";		
		
		InfoBid p = getInfoBids(idObjecto);
		
		if(p==null){
			central.msgbox(language.getInstance().getMsg(player).BH_ITEM_IS_NOT_FOR_SELL_NOW, player);
			return;
		}else if(!p.isActive()){
			central.msgbox(language.getInstance().getMsg(player).BH_ITEM_IS_NOT_FOR_SELL_NOW, player);
			return;			
		}
		
		String fontColorName = ( opera.isMasterWorkItem(p.getIdItem()) ? "LEVEL" : "A4FFA9" );
		
		html.replace("%ITEM_ICO%", p.getIconImgID());
		html.replace("%ITEM_NAME_COLOR%", fontColorName);
		html.replace("%ITEM_NAME%", p.getItemName());
		html.replace("%ITEM_ENCHANT%", ( p.getItemEnchant() > 0 ? "+"+String.valueOf(p.getItemEnchant()) : "NONE" ) );
		
		InfoBid itemSel = getInfoBids(idObjecto);
		
		if(itemSel==null){
			central.msgbox(language.getInstance().getMsg(player).BH_ITEM_IS_NOT_FOR_SELL_NOW, player);
			return;
		}
		
		String ElementalData = "";
		
		if(itemSel.getItemInstance().isWeapon()){
			int idAtri = itemSel.getItemInstance().getAttackElementType();
			int Atributo = itemSel.getItemInstance().getAttackElementPower();
			int maximo = 40;
			int pWidth = (257 * Atributo) / maximo;
			if(pWidth>257){
				pWidth=257;
			}
			for(int i=0 ;i<=5 ;i++){
				if(i==idAtri){
					ElementalData += "<tr>"+ Elemental[i].replace("%WIDTH%", String.valueOf(pWidth)).replace("%POWER%", String.valueOf(Atributo)) +"</tr>";
				}
			}
		}else if(itemSel.getItemInstance().isArmor()){
			Integer[]Atri = new Integer[6];
			Atri[5] = itemSel.getDivineAtributte();
			Atri[4] = itemSel.getDarkAtributte();
			Atri[2] = itemSel.getEarthAtributte();
			Atri[3] = itemSel.getWindAtributte();
			Atri[1] = itemSel.getWaterAtributte();
			Atri[0] = itemSel.getFireAtributte();
			for(int i=0 ;i<=5 ;i++){
				int pWidth = ( 257 * Atri[i] ) / 180;
				int Atributo = Atri[i];
				if(pWidth > 257){
					pWidth = 257;
				}
				if(Atributo>0){
					ElementalData += "<tr>"+ Elemental[i].replace("%WIDTH%", String.valueOf(pWidth)).replace("%POWER%", String.valueOf(Atributo)) +"</tr>";
				}
			}			
		}

		if(ElementalData.length() > 0){
			ElementalData = "<table width=257 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2>"+ ElementalData +"</table>";
		}
		
		html.replace("%ELEMENTAL_DATA%", ElementalData);
		
		boolean istheFirtsBidder = (itemSel.getActualBidPlayerID() == player.getObjectId());
		boolean isBidder = itemSel.getAllPlayersBids().containsKey(player.getObjectId());
		
		html.replace("%QUANTITY_TO_SELL%", opera.getFormatNumbers(itemSel.getQuantityItemToSell()));
		html.replace("%SELLER_NAME%", itemSel.getSellerName());
		html.replace("%REMAINING_TIME%", itemSel.getRemainingTimeFormat());
		html.replace("%REQUESTED_ITEM_TO_MAKE_BID%", itemSel.getNameItemToRequest());
		html.replace("%QUANTITY_REQUESTED_ITEM_TO_BID%", opera.getFormatNumbers(itemSel.getQuantityRequest()));
		html.replace("%LAST_BID%", opera.getFormatNumbers(itemSel.getActualBid()));
		html.replace("%ONLINE_COLOR%", (itemSel.isPlayerOnline() ? "59FF85" : "FF5959"));
		html.replace("%IS_ONLINE%", (itemSel.isPlayerOnline() ? "YES" : "NO"));

		String CancelMyOffert = "";
		if(istheFirtsBidder || isBidder){
			String ByPassCancel = "bypass ZeuS bh_cancel_by_bid %ID_OBJECT%";
			CancelMyOffert = "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
			"My Last Offert: " + opera.getFormatNumbers( itemSel.getAllBiddersOrderByBid().get(player.getObjectId()).getBid()) + "<br1>" +
            "<button value=\"Cancel My Bid\" action=\""+ ByPassCancel.replace("%ID_OBJECT%", String.valueOf(idObjecto)) +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table><br>";
			PLAYER_VARIABLES.get(player.getObjectId()).setRemoveObjectSetIDObject(idObjecto);
		}
		
		html.replace("%CANCEL_MY_OFFERT%", CancelMyOffert);

		boolean isOwner = false;
		String CancelMyBidSystem = "";
		if(player.getObjectId() == itemSel.getPlayerID()){
			String linkRemove = "";
			if(general.BIDHOUSE_CANCEL_TAX_FOR_SELLER>0 && itemSel.haveBidders()){
				linkRemove = "cancelbidAsk";
			}else{
				linkRemove = "cancelbid";
			}
			String BypassCancel = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";"+ linkRemove +";" + String.valueOf(idObjecto) + ";0;0;0;0";
			CancelMyBidSystem = "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
            "<button value=\"Cancel Bid\" action=\""+ BypassCancel +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
			isOwner= true;
		}
		
		html.replace("%CANCEL_MY_BID_SYSTEM%", CancelMyBidSystem);
		
		String OwnerData = "";
		if(!isOwner){
			if(opera.haveItem(player, itemSel.getIDItemRequested(), itemSel.getQuantityRequest(), false)){
				String ByPassBuyItNow = "bypass ZeuS bh_lin BuyItNow " + idObjecto + " $txtBid " + String.valueOf(seccion);
				OwnerData = "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
	            "<font name=hs12 color=LEVEL>Your Bid<br1>(Actual Bid: "+ opera.getFormatNumbers(itemSel.getActualBid() ) +")</font>"+ "<edit type=number var=\"txtBid\" width=120><br>"+
				"<button value=\"Place Bid\" action=\""+ ByPassBuyItNow +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";			
				
				String itemHave = opera.getFormatNumbers( String.valueOf(player.getInventory().getItemByItemId( itemSel.getIDItemRequested()).getCount() ) );
				OwnerData += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
				"<font name=hs12 color=FAF991>Your item's</font><br>"+
	            "<font name=hs12 color=B8FA91>"+ itemSel.getNameItemToRequest() +"</font><br1>"+
	            "<font name=hs12 color=91DEFA>"+ itemHave +"</font><br>"+
	           "</td></tr></table>";
			}else{
				OwnerData += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
	            "<font name=hs12 color=FAF991>You item's</font><br>You dont have the Requested Item</td></tr></table>";
			}
		}
		
		html.replace("%OWNER_DATA%", OwnerData);
		int LastSection = PLAYER_VARIABLES.get(player.getObjectId()).getLastSection();
		String byPassComm = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";"+ String.valueOf(LastSection) +";0;0;0;0;0;0";
		cbManager.separateAndSend( bypass(player, byPassComm) , player);			
		
		
		opera.enviarHTML(player, html.getHtml());
	}
	
	
	private static void setCreateNewItemInBD(L2PcInstance player, int IdObject, int itemID, Long Count){
		String Consulta = "insert into items(owner_id, object_id, item_id, count, enchant_level, loc, loc_data) values (?,?,?,?,?,?,?)";
		
		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, ID_OWNER_BID);
			ins.setInt(2, IdObject);
			ins.setInt(3, itemID);
			ins.setLong(4, Count);
			ins.setInt(5, 0);
			ins.setString(6, "INVENTORY");
			ins.setInt(7, 22);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			_log.warning("Error ZeuS A->" + a.getMessage());
		}		
		
	}
	
	
	private static boolean setNewAuction_INBD(L2PcInstance player, int idObject, int idItemRequest, Long SellItem_Quantity, Long ItemRequestQuantity, int FeedIN){
		
		boolean retorno = false;
		
		String Consulta = "INSERT INTO zeus_bid_house("+
		"idObjeto,idOwner,idItemRequest,ItemRequestQuantity,ItemQuantityToSell,startUnix,endUnix,feed) VALUES(?,?,?,?,?,?,?,?)";
		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, idObject);/**/
			ins.setInt(2, player.getObjectId());/**/
			ins.setInt(3, idItemRequest);/**/
			ins.setLong(4, ItemRequestQuantity);
			ins.setLong(5, SellItem_Quantity);
			ins.setInt(6, opera.getUnixTimeNow());
			ins.setInt(7, opera.getUnixTimeNow() + ( general.BIDHOUSE_DAYS *  ( 24 * 60 * 60 )  ));
			ins.setInt(8, FeedIN);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
				retorno = true;
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			_log.warning("Error ZeuS A->" + a.getMessage());
		}

		return retorno;
	}

	private static boolean haveItemAlready(L2PcInstance player, int idItem){
		for(InfoBid T1 : ITEMS_ON_BIDS){
			if(!T1.isActive()){
				continue;
			}
			if(T1.getIdItem() == idItem && T1.getPlayerID() == player.getObjectId()){
				return true;
			}
		}
		return false;
	}

	private static void createAuctionWithItem(L2PcInstance player, int idObject, int idItemRequest, Long Cantidad_a_Vender, Long ValorPorCadaItem_A_Vender){
		L2Object object = L2World.getInstance().findObject(idObject);
		if (object instanceof L2ItemInstance){
			L2ItemInstance item = (L2ItemInstance) object;
			if(item.isEquipped()){
				central.msgbox(language.getInstance().getMsg(player).BH_ITEM_IS_EQUIPPED.replace("$item", item.getName()), player);
				return;
			}
			
			if(item.getOwnerId() != player.getObjectId()){
				central.msgbox(language.getInstance().getMsg(player).BH_TRY_TO_CHEAT, player);
				return;
			}
			
			int FeedRequested = getFeedFromItem(idObject);
			
			if(!opera.haveItem(player, 57, FeedRequested )){
				central.msgbox(language.getInstance().getMsg(player).BH_NO_HAVE_FEED.replace("$feed",String.valueOf(FeedRequested)), player);
				return;
			}
			
			
			try{
				L2ItemInstance itemW = player.getWarehouse().getItemByObjectId(idObject);
				if(itemW!=null){
					central.msgbox(language.getInstance().getMsg(player).BH_TRY_TO_CHEAT, player);
					return;					
				}
			}catch(Exception a){
				
			}
			
			try{
				L2ItemInstance itemIn = player.getInventory().getItemByObjectId(idObject);
				if(itemIn==null){
					central.msgbox(language.getInstance().getMsg(player).BH_TRY_TO_CHEAT, player);
					return;					
				}
			}catch(Exception a){
				
			}
			
			
			if(!item.isStackable() && Cantidad_a_Vender>1){
				central.msgbox(language.getInstance().getMsg(player).BH_NO_STACKABLE_ITEM_CHANGE_TO_ONE.replace("$item",item.getName()), player);
				Cantidad_a_Vender = 1L;
			}
			
			
			if(haveItemAlready(player,item.getItem().getId())){
				central.msgbox(language.getInstance().getMsg(player).BH_CANCEL_THE_ITEM_TO_SELL_AGAIN, player);
				return;
			}
			
			
			int IdObjectNew = idObject;
			
			int FeedIN = getFeedFromItem(idObject);
			
			if(Cantidad_a_Vender > item.getCount()){
				central.msgbox(language.getInstance().getMsg(player).BH_CREATE_PROCESS_FAIL_CHECK_QUANTITY, player);
				return;
			}else if(Cantidad_a_Vender < item.getCount()){
				IdObjectNew = getNewIDObject();
			}
			
			int idObjeToS = item.getObjectId();
			
			setNewAuction_INBD(player,IdObjectNew,idItemRequest,Cantidad_a_Vender,ValorPorCadaItem_A_Vender,FeedIN);
			if(item.isStackable()){
				opera.removeItem(item.getItem().getId(), Cantidad_a_Vender, player);
				setCreateNewItemInBD(player, IdObjectNew, item.getItem().getId(),Cantidad_a_Vender);
				idObjeToS = IdObjectNew;
			}else{
				item.setOwnerId(ID_OWNER_BID);
				item.updateDatabase(true);
				player.destroyItemWithoutTrace("bid_house", item.getObjectId(), 1, null, false);
			}
			
			player.sendPacket(ActionFailed.STATIC_PACKET);

			player.broadcastStatusUpdate();
			player.getInventory().refreshWeight();			
			
			central.msgbox("Bid system created!", player);
			getAllItem(false,player,idObjeToS);
			opera.removeItem(57, FeedRequested, player);
			wishList.AnnounceItem(item.getId(), "Bid House", player.getName());
		}else{
			central.msgbox("Bid system create process fail. Please Check.", player);
		}
	}
	
	private static int getNewIDObject(){
		int _objectId = IdFactory.getInstance().getNextId();
		return _objectId;
	}
	
	private static int getTotalInventario(L2PcInstance player){
		int Contador = 0;
		for(L2ItemInstance items : player.getInventory().getItems()){
			if(items.getItem().getId()==57){
				continue;
			}
			if(items.isEquipped()){
				continue;
			}
			
			if(!items.isTradeable()){
				continue;
			}
			Contador++;
		}
		return Contador;
	}
	
	
	private static NpcHtmlMessage getInventaryWindows(L2PcInstance player, int pagina, int idObjectItem, NpcHtmlMessage html){
		String ByPass_Arriba_Top = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";0;0;"+ String.valueOf(idObjectItem) +";0;0;0";
		String ByPass_Arriba_1 = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";0;" + String.valueOf(  pagina>0 ? pagina - 1 : 0    ) + ";"+ String.valueOf(idObjectItem) +";0;0;0";
		String ByPass_Abajo_Top = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";0;%PAGLAST%;"+ String.valueOf(idObjectItem) +";0;0;0";
		String ByPass_Abajo_1 = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";0;%PAGNEXT%;"+ String.valueOf(idObjectItem) +";0;0;0";
		html.replace("%BYPASS_BID_UP_ALL%", ByPass_Arriba_Top);
		html.replace("%BYPASS_BID_UP_ONE%", ByPass_Arriba_1);
		html.replace("%BYPASS_BID_DOWN_ONE%", ByPass_Abajo_1);
		html.replace("%BYPASS_BID_DOWN_ALL%", ByPass_Abajo_Top);

		int indexBotton = (getTotalInventario(player) / 5);
		indexBotton = ( (indexBotton%5)==0 ? indexBotton-3 : indexBotton-2 );
		int ContadorColumnas=0;
		boolean Continua = true;
		String ItemByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";0;" + String.valueOf(pagina) + ";%IDOBJETO%;0;0;0";
		
		int ItemLementos = 5;
		int desde = ItemLementos * pagina;
		int ContadorElementos =1;
		
		int RowCounter = 1;
		
		for(L2ItemInstance items : player.getInventory().getItems()){
			if(items.getItem().getId()==57){
				continue;
			}
			if(items.isEquipped()){
				continue;
			}
			
			if(!items.isTradeable()){
				continue;
			}
			
			if(ContadorElementos>desde){
				html.replace("%MY_BID_MY_INV_ITEM_"+ String.valueOf(RowCounter) +"%", cbFormato.getBotonForm(opera.getIconImgFromItem(items.getItem().getId(), true), ItemByPass.replace("%IDOBJETO%", String.valueOf(items.getObjectId()))));
				RowCounter++;
				if(Continua){
					if(ContadorColumnas == 2){
						Continua=false;
					}
				}
			}
			ContadorElementos++;
		}
		
		if(RowCounter<=15){
			for(int i=RowCounter; i<=15; i++){
				html.replace("%MY_BID_MY_INV_ITEM_"+ String.valueOf(i) +"%", cbFormato.getBotonForm("etc_alphabet_5_i00",""));
			}
		}
		
		if(pagina<=indexBotton){
			html.replace("%PAGLAST%", String.valueOf(indexBotton));
			html.replace("%PAGNEXT%", String.valueOf(pagina + 1));
		}else{
			html.replace("%PAGLAST%", "0");
			html.replace("%PAGNEXT%", String.valueOf(pagina-1));
		}
		return html;
	}
	
	private static NpcHtmlMessage getSelectedItemToAuction(L2PcInstance player, int idObjectItem, NpcHtmlMessage html){
		L2Object object = L2World.getInstance().findObject(idObjectItem);
		if (object instanceof L2ItemInstance){
			L2ItemInstance item = (L2ItemInstance) object;
			
			int getEnchant = 0;
			
			try{
				getEnchant = item.getEnchantLevel();
			}catch(Exception a){
				getEnchant = 0;
			}
			
			long Quantity = 0l;
			try{
				Quantity = player.getInventory().getItemByObjectId(idObjectItem).getCount();
			}catch(Exception a){
				
			}

			html.replace("%MY_BID_SELECTED_ITEM_ICON%", opera.getIconImgFromItem(item.getItem().getId() , true));
			html.replace("%MY_BID_SELECTED_ITEM_QUANTITY%", opera.getFormatNumbers(Quantity));
			html.replace("%MY_BID_SELECTED_ITEM_NAME_AND_ENCHANT%", item.getName() + ( getEnchant>0 ? " (+" + String.valueOf(getEnchant) + ")" : "" ));
		}else{
			html.replace("%MY_BID_SELECTED_ITEM_ICON%", "");
			html.replace("%MY_BID_SELECTED_ITEM_QUANTITY%", "0");
			html.replace("%MY_BID_SELECTED_ITEM_NAME_AND_ENCHANT%", "");			
		}		
		
		return html;
	}
	
	
	private static int getIdFromItemRequest(String NombreItem){
		if(general.BIDHOUSE_ITEM_REQUEST.size()>0){
			for(String t : general.BIDHOUSE_ITEM_REQUEST){
				if(t.split(":")[1].equalsIgnoreCase(NombreItem)){
					return Integer.valueOf(t.split(":")[0]);
				}
			}
		}
		return 57;
	}
	
	private static String getCmbItemRequest(){
		String Retorno = "";
		if(general.BIDHOUSE_ITEM_REQUEST.size()>0){
			for(String t : general.BIDHOUSE_ITEM_REQUEST){
				if(Retorno.length()>0){
					Retorno += ";";
				}
				Retorno += t.split(":")[1];
			}
			return Retorno;
		}
		return "Adena";
	}
	
	
	private static NpcHtmlMessage getSellBox(L2PcInstance player, int Pagina, int idObjeto, NpcHtmlMessage html){
		
		String ByPassCreateAuction = "bypass ZeuS bh_lin create " + String.valueOf(Pagina) + " " + String.valueOf(idObjeto) + " $txtQuantity $cmbItemRequest $txtPrice";
		String comboList = getCmbItemRequest();
		
		html.replace("%MY_BID_ITEM_REQUESTED_LIST%", comboList);
		html.replace("%MY_BID_CREATE_NEW_BID_BYPASS%", ByPassCreateAuction);
		return html;
	}
	
	private static NpcHtmlMessage getFeeAndYouAdena(L2PcInstance player, int IdObjectItem, NpcHtmlMessage html){
		int Feed = getFeedFromItem(IdObjectItem);
		String YouAdena = opera.getFormatNumbers(String.valueOf(player.getInventory().getAdena()));
		html.replace("%MY_BID_MY_ADENA_FEE%", opera.getFormatNumbers(Feed));
		html.replace("%MY_BID_MY_ADENA%", YouAdena);
		return html;
	}

	public static void setSortedData(L2PcInstance player, String Sort){
		PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSortGeneralList(Sort);
	}
	public static void setSortedDataMyItems(L2PcInstance player, String Sort){
		PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSortMyItemList(Sort);
	}
	public static void setSortedDataAllBidsData(L2PcInstance player, String Sort, String Section){
		if(Section.equals("Active")){
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerOrderAllActiveBidsData(Sort);
		}else{
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerOrderAllPlacedBidsData(Sort);
		}
	}

	private static Vector<InfoBid> SortedData(L2PcInstance player){
		return SortedData(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSortGeneralList());
	}
	
	private static Vector<InfoBid> SortedData(String SortBy){
		switch(SortBy){
			case "nameAZ":
				Comparator<InfoBid> T1 = (p1,p2) -> p1.getItemName().compareToIgnoreCase(p2.getItemName());
				Collections.sort(ITEMS_ON_BIDS,T1);
				break;
			case "nameZA":
				Comparator<InfoBid> T2 = (p1,p2) -> p2.getItemName().compareToIgnoreCase(p1.getItemName());
				Collections.sort(ITEMS_ON_BIDS,T2);
				break;
			case "gradeAZ":
				Comparator<InfoBid> T3 = (p1,p2) -> p1.getItemGrade().compareToIgnoreCase(p2.getItemGrade());
				Collections.sort(ITEMS_ON_BIDS,T3);
				break;
			case "gradeZA":
				Comparator<InfoBid> T4 = (p1,p2) -> p2.getItemGrade().compareToIgnoreCase(p1.getItemGrade());
				Collections.sort(ITEMS_ON_BIDS,T4);
				break;
			case "quantityAZ":
				Comparator<InfoBid> T5 = (p1,p2) -> Long.compare(p1.getQuantityItemToSell(), p2.getQuantityItemToSell());
				Collections.sort(ITEMS_ON_BIDS,T5);
				break;
			case "quantityZA":
				Comparator<InfoBid> T6 = (p1,p2) -> Long.compare(p2.getQuantityItemToSell(), p1.getQuantityItemToSell());				
				Collections.sort(ITEMS_ON_BIDS,T6);
				break;
			case "itemrequestAZ":
				Comparator<InfoBid> T7 = (p1,p2) -> p1.getNameItemRequested().compareToIgnoreCase(p2.getNameItemRequested());
				Collections.sort(ITEMS_ON_BIDS,T7);
				break;
			case "itemrequestZA":
				Comparator<InfoBid> T8 = (p1,p2) -> p2.getNameItemRequested().compareToIgnoreCase(p1.getNameItemRequested());				
				Collections.sort(ITEMS_ON_BIDS,T8);
				break;
			case "priceAZ":
				Comparator<InfoBid> T9 = (p1,p2) -> Long.compare(p1.getQuantityRequest(), p2.getQuantityRequest());
				Collections.sort(ITEMS_ON_BIDS,T9);
				break;
			case "priceZA":
				Comparator<InfoBid> T10 = (p1,p2) -> Long.compare(p2.getQuantityRequest(), p1.getQuantityRequest());				
				Collections.sort(ITEMS_ON_BIDS,T10);
				break;
			case "bidAZ":
				Comparator<InfoBid> T11 = (p1,p2) -> Long.compare(p1.getActualBid() , p2.getActualBid());				
				Collections.sort(ITEMS_ON_BIDS,T11);
				break;
			case "bidZA":
				Comparator<InfoBid> T12 = (p1,p2) -> Long.compare(p2.getActualBid() , p1.getActualBid());				
				Collections.sort(ITEMS_ON_BIDS,T12);
				break;
			case "timeAZ":
				Comparator<InfoBid> T13 = (p1,p2) -> Integer.compare(p1.getRemainingTime() , p2.getRemainingTime());				
				Collections.sort(ITEMS_ON_BIDS,T13);
				break;
			case "timeZA":
				Comparator<InfoBid> T14 = (p1,p2) -> Integer.compare(p2.getRemainingTime() , p1.getRemainingTime());				
				Collections.sort(ITEMS_ON_BIDS,T14);
				break;
		}
		return ITEMS_ON_BIDS;
	}
	
	public static void showActiveBiddingWindows(L2PcInstance player, int idObject, int idSeller){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bidhouse-show-active-biddings-list.htm");
		InfoBid tempData = null;
		for(InfoBid tD : ITEMS_ON_BIDS){
			if((tD.getObjectID() == idObject) && tD.isActive()){
				tempData = tD;
				break;
			}
		}
		
		if(tempData==null){
			return;
		}
		
		String F_N = "", S_N = "", T_N = "";
		String F_B = "", S_B = "", T_B = "";
		
		if(tempData!=null){
			if(tempData.getAllPlayersBids()!=null){
				if(tempData.getAllPlayersBids().size()>0){
					int cO = 1;
					Map<Integer, _bidInformation> tmpDat = tempData.getAllBiddersOrderByBid();
					@SuppressWarnings("rawtypes")
					Iterator itr = tmpDat.entrySet().iterator();
					while(itr.hasNext()){
						if(cO>3){
							break;
						}
						@SuppressWarnings("rawtypes")
						Map.Entry Entrada = (Map.Entry)itr.next();
						_bidInformation bidInfo = (_bidInformation) Entrada.getValue();
						switch (cO) {
							case 1:
								F_N = bidInfo.getPlayerName();
								F_B = String.valueOf(bidInfo.getBid());
								cO++;
								break;
							case 2:
								S_N = bidInfo.getPlayerName();
								S_B = String.valueOf(bidInfo.getBid());
								cO++;
								break;
							case 3:
								T_N = bidInfo.getPlayerName();
								T_B = String.valueOf(bidInfo.getBid());
								cO++;
								break;
						}
					}
				}
			}
		}
		
		html.replace("%FIRT_NAME%", F_N);
		html.replace("%FIRT_BID%", F_B);
		html.replace("%SECOND_NAME%", S_N);
		html.replace("%SECOND_BID%", S_B);
		html.replace("%THIRD_NAME%", T_N);
		html.replace("%THIRD_BID%", T_B);
		html.replace("%ITEM_NAME%", tempData.getItemName());
		String byPassComm = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";2;0;0;0;0;0;0";
		cbManager.separateAndSend( bypass(player, byPassComm) , player);
		central.sendHtml(player, html.getHtml());
	}
	
	public static void setPageBiddingInfo(L2PcInstance player, int Page, String Seccion){
		if(Seccion.equals("Active")){
			PLAYER_VARIABLES.get(player.getObjectId()).setActiveBiddingPage(Page);
		}else if(Seccion.equals("Placed")){
			PLAYER_VARIABLES.get(player.getObjectId()).setPlacedBidsPage(Page);
		}
	}
	
	private static NpcHtmlMessage getAllPersonalBids(L2PcInstance player){
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bid-house-bids.htm");
		
		String []ColorBG = {"001818","010B0B"};
		
		String ByPassGoActiveBidding = "bypass ZeuS bh_see_bid_data Act_Bid %ID_OBJECT% %ID_SELLER%";
		String ByPassGoPlacedBids = "bypass ZeuS bh_see_bid_data Pla_Bid %ID_OBJECT% %ID_SELLER%";
		
		String btnSeeActiveBidding = "<button action=\""+ ByPassGoActiveBidding +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
		String btnSeePlacedBids = "<button action=\""+ ByPassGoPlacedBids +"\" width=16 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";
		
		String ActiveItem = "bypass ZeuS bh_sort_all_bids_info Active nameAZ";
		String ActiveQuantity = "bypass ZeuS bh_sort_all_bids_info Active quantityAZ";
		String ActiveItemRequest = "bypass ZeuS bh_sort_all_bids_info Active itemrequestAZ";
		String ActiveBids = "bypass ZeuS bh_sort_all_bids_info Active bidAZ";
		String ActiveRemain = "bypass ZeuS bh_sort_all_bids_info Active timeAZ";

		switch(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerOrderAllActiveBidsData()){
			case "nameAZ":
				ActiveItem = "bypass ZeuS bh_sort_all_bids_info Active nameZA";
				break;
			case "quantityAZ":
				ActiveQuantity = "bypass ZeuS bh_sort_all_bids_info Active quantityZA";
				break;
			case "itemrequestAZ":
				ActiveItemRequest = "bypass ZeuS bh_sort_all_bids_info Active itemrequestZA";
				break;
			case "bidAZ":
				ActiveBids = "bypass ZeuS bh_sort_all_bids_info Active bidZA";
				break;
			case "timeAZ":
				ActiveRemain = "bypass ZeuS bh_sort_all_bids_info Active timeZA";
				break;
		}
		

		html.replace("%YOUR_BIDS_ORDER_BY_NAME%", ActiveItem);
		html.replace("%YOUR_BIDS_ORDER_BY_QUANTITY%", ActiveQuantity);
		html.replace("%YOUR_BIDS_ORDER_BY_ITEM_REQUESTED%", ActiveItemRequest);
		html.replace("%YOUR_BIDS_ORDER_BY_CURRENT_BID_DATA%", ActiveBids);
		html.replace("%YOUR_BIDS_ORDER_BY_REMAINING_TIME%", ActiveRemain);

		String gridFormatMyBids = opera.getGridFormatFromHtml(html, 1, "%MY_BIDS_DATA%");
		
		String PlacedItem = "bypass ZeuS bh_sort_all_bids_info Placed nameAZ";
		String PlacedQuantity = "bypass ZeuS bh_sort_all_bids_info Placed quantityAZ";
		String PlacedItemRequest = "bypass ZeuS bh_sort_all_bids_info Placed itemrequestAZ";
		String PlacedBids = "bypass ZeuS bh_sort_all_bids_info Placed bidAZ";
		String PlacedRemain = "bypass ZeuS bh_sort_all_bids_info Placed timeAZ";

		switch(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerOrderAllPlacedBidsData()){
			case "nameAZ":
				PlacedItem = "bypass ZeuS bh_sort_all_bids_info Placed nameZA";
				break;
			case "quantityAZ":
				PlacedQuantity = "bypass ZeuS bh_sort_all_bids_info Placed quantityZA";
				break;
			case "itemrequestAZ":
				PlacedItemRequest = "bypass ZeuS bh_sort_all_bids_info Placed itemrequestZA";
				break;
			case "bidAZ":
				PlacedBids = "bypass ZeuS bh_sort_all_bids_info Placed bidZA";
				break;
			case "timeAZ":
				PlacedRemain = "bypass ZeuS bh_sort_all_bids_info Placed timeZA";
				break;
		}		
		
		html.replace("%PLACED_BIDS_ORDER_BY_ITEM_NAME%", PlacedItem);
		html.replace("%PLACED_BIDS_ORDER_BY_QUANTITY%", PlacedQuantity);
		html.replace("%PLACED_BIDS_ORDER_BY_ITEM_REQUESTED%", PlacedItemRequest);
		html.replace("%PLACED_BIDS_ORDER_BY_BID_DATA%", PlacedBids);
		html.replace("%PLACED_BIDS_ORDER_BY_REMAINING_TIME%", PlacedRemain);
		
		int Cont=0;
		
		int ItemToShow = 4;
		
		boolean haveNext = false;
		
		int activeDesde = PLAYER_VARIABLES.get(player.getObjectId()).getActiveBiddingPage() * ItemToShow;
		int activeHasta = activeDesde + ItemToShow;
		int activeCont = 0;
		String MyGridBidData = "";
		for(InfoBid tempV : SortedData(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerOrderAllActiveBidsData())){
			if((player.getObjectId() == tempV.getPlayerID()) && tempV.isActive()){
				if(activeCont >= activeDesde && activeCont < activeHasta ){
					String BypassItemSee = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";showItemWindows;%IDOBJECTO%;1;0;0;0";
					String btnImg = cbFormato.getBotonForm(opera.getIconImgFromItem(tempV.getIdItem() , true), BypassItemSee.replace("%IDOBJECTO%", String.valueOf(tempV.getObjectID())));				
					String tempB = btnSeeActiveBidding.replace("%ID_SELLER%", String.valueOf(tempV.getPlayerID())).replace("%ID_OBJECT%", String.valueOf(tempV.getObjectID()));
					MyGridBidData += gridFormatMyBids.replace("%INI_BID%", opera.getFormatNumbers(tempV.getQuantityRequest()) ) .replace("%BTN_GO%", tempB) .replace("%COLOR%", "\"\"") .replace("%BGCOLOR%", ColorBG[Cont++ % 2]) .replace("%REMAINING_TIME%", tempV.getRemainingTimeFormat() ) .replace("%CURRENT_BID%", opera.getFormatNumbers(tempV.getActualBid()) ) .replace("%ITEM_REQUESTED%", tempV.getNameItemRequested() ) .replace("%QUANTITY%", opera.getFormatNumbers(tempV.getQuantityItemToSell())) .replace("%ITEM_NAME%", tempV.getItemName())  .replace("%IMG%", btnImg);
				}else if(activeCont >= activeHasta){
					haveNext = true;
					break;
				}
				activeCont++;
			}
		}
		
		html.replace("%MY_BIDS_DATA%", MyGridBidData);
		
		String ByPassMove = "bypass ZeuS bh_personal_bids_page %SECTION% %PAG%";
		/*String btnAntes = "<button  action=\""+ ByPassMove +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Left\" fore=\"L2UI_CT1.Button_DF_Left\" value=\" \">";
		String btnProx = "<button  action=\""+ ByPassMove +"\" width=32 height=16 back=\"L2UI_CT1.Button_DF_Right\" fore=\"L2UI_CT1.Button_DF_Right\" value=\" \">";*/		
		int PagActiveBidding = PLAYER_VARIABLES.get(player.getObjectId()).getActiveBiddingPage();
		
		html.replace("%MY_BIDS_PAGE_NUM%", String.valueOf(PagActiveBidding + 1));
		html.replace("%MY_BIDS_PREV_PAGE_BYPASS%", (PagActiveBidding > 0 ? ByPassMove.replace("%PAG%", String.valueOf( PagActiveBidding - 1 )  )  .replace("%SECTION%", "Active") : ""));
		html.replace("%MY_BIDS_NEXT_PAGE_BYPASS%", (haveNext ? ByPassMove.replace("%PAG%", String.valueOf( PagActiveBidding + 1 )  )  .replace("%SECTION%", "Active") : ""));

		Cont=0;
		
		String GridActiveBidding = opera.getGridFormatFromHtml(html, 2, "%PLACED_BID_DATA%");
		
		int PlacedDesde = PLAYER_VARIABLES.get(player.getObjectId()).getPlacedBidsPage() * ItemToShow;
		int PlacedHasta = PlacedDesde + ItemToShow;
		int PlacedCont = 0;		
		haveNext = false;
		String GridPlacedData = "";
		for(InfoBid tempV : SortedData(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerOrderAllPlacedBidsData())){
			if(!tempV.isActive()){
				continue;
			}
			if(tempV.getAllPlayersBids()==null){
				continue;
			}
			if(tempV.getAllPlayersBids().size()==0){
				continue;
			}
			if(tempV.getAllPlayersBids().containsKey(player.getObjectId())){
				if(PlacedCont >= PlacedDesde && PlacedCont < PlacedHasta ){
					String BypassItemSee = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";showItemWindows;%IDOBJECTO%;1;0;0;0";
					String btnImg = cbFormato.getBotonForm(opera.getIconImgFromItem(tempV.getIdItem() , true), BypassItemSee.replace("%IDOBJECTO%", String.valueOf(tempV.getObjectID())));
					boolean isFirts = tempV.getActualBidPlayerName().equals(player.getName());
					String BidColor = (isFirts ? "2ECC71" : "FF5733");
					String tempB = btnSeePlacedBids.replace("%ID_SELLER%", String.valueOf(tempV.getPlayerID())).replace("%ID_OBJECT%", String.valueOf(tempV.getObjectID()));
					GridPlacedData += GridActiveBidding.replace("%INI_BID%", opera.getFormatNumbers(tempV.getQuantityRequest())) .replace("%BTN_GO%", tempB).replace("%COLOR%", BidColor) .replace("%BGCOLOR%", ColorBG[Cont++ % 2]) .replace("%REMAINING_TIME%", tempV.getRemainingTimeFormat() ) .replace("%CURRENT_BID%", opera.getFormatNumbers(tempV.getActualBid()) ) .replace("%ITEM_REQUESTED%", tempV.getNameItemRequested() ) .replace("%QUANTITY%", opera.getFormatNumbers(tempV.getQuantityItemToSell())) .replace("%ITEM_NAME%", tempV.getItemName())  .replace("%IMG%", btnImg);
				}else if(PlacedCont >= PlacedHasta){
					haveNext = true;
					break;
				}
				PlacedCont++;
			}
		}
		int pagPlaced = PLAYER_VARIABLES.get(player.getObjectId()).getPlacedBidsPage();
		html.replace("%PLACED_BID_DATA%", GridPlacedData);
		html.replace("%PLACED_BIDS_PAGE_NUM%", String.valueOf(pagPlaced + 1));
		html.replace("%PLACED_BIDS_PREV_PAGE_BYPASS%", (pagPlaced > 0 ? ByPassMove.replace("%PAG%", String.valueOf( pagPlaced - 1  )).replace("%SECTION%", "Placed") : ""));
		html.replace("%PLACED_BIDS_NEXT_PAGE_BYPASS%", (haveNext ? ByPassMove.replace("%PAG%", String.valueOf( pagPlaced + 1  )).replace("%SECTION%", "Placed") : ""));		
		
		return html;		
	}
	
	private static NpcHtmlMessage getMyBidList(L2PcInstance player, int PaginaMisVentas, int PaginaMisItemInventario, int idObjecto, NpcHtmlMessage html){
		
		String ByPassItemToSell = "bypass ZeuS bh_sort_personal nameAZ";
		String ByPassQuantityToSell = "bypass ZeuS bh_sort_personal quantityAZ";
		String ByPassItemRequestToSell = "bypass ZeuS bh_sort_personal itemrequestAZ";
		String ByPassSalePrice = "bypass ZeuS bh_sort_personal priceAZ";
		
		switch(PLAYER_VARIABLES.get( player.getObjectId() ).getPlayerSortMyItemList()){
			case "nameAZ":
				ByPassItemToSell = "bypass ZeuS bh_sort_personal nameZA";
				break;
			case "quantityAZ":
				ByPassQuantityToSell = "bypass ZeuS bh_sort_personal quantityZA";
				break;
			case "itemrequestAZ":
				ByPassItemRequestToSell = "bypass ZeuS bh_sort_personal itemrequestZA";
				break;
			case "priceAZ":
				ByPassSalePrice = "bypass ZeuS bh_sort_personal priceZA";
				break;
		}
		
		html.replace("%MY_BID_SORT_BY_ITEM_NAME%", ByPassItemToSell);
		html.replace("%MY_BID_SORT_BY_QUANTITY%", ByPassQuantityToSell);
		html.replace("%MY_BID_SORT_BY_ITEM_REQUEST%", ByPassItemRequestToSell);
		html.replace("%MY_BID_SORT_BY_BID%", ByPassSalePrice);
		
		String []ColorBG = {"001818","010B0B"};

		Map<String, String> GradeItem = new HashMap<String, String>();
		GradeItem.put("S84", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>");
		GradeItem.put("S80", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>");
		GradeItem.put("S", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>");
		GradeItem.put("A", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>");
		GradeItem.put("B", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>");
		GradeItem.put("C", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>");
		GradeItem.put("D", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>");
		GradeItem.put("", "");

		String GridItemFormat = opera.getGridFormatFromHtml(html, 1, "%MY_ITEMS_LIST%");
		String MyItemList = "";		
		
		int contador = 0;
		
		String BypassItemSee = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";showItemWindows;%IDOBJECTO%;0;0;0;0";
		
		int Limite = 6;
		
		int desde = Limite * PaginaMisVentas;
		int hasta = desde + Limite;
		boolean havenext = false;
		

		for(InfoBid p : SortedData(PLAYER_VARIABLES.get( player.getObjectId() ).getPlayerSortMyItemList())){
			if(!p.isActive()){
				continue;
			}
			if(havenext){
				break;
			}
			int idObject = p.getObjectID();
			if(p.getPlayerID() == player.getObjectId()){
				if(contador >= desde && contador < hasta){
					String Grados = "";
					try{
						Grados = GradeItem.get(p.getItemGrade());
					}catch(Exception a){
						
					}
					if(Grados==null){
						Grados="";
					}else if(Grados.isEmpty()){
						Grados = "";
					}
					
					
					
					String Enchant = "<font color=6E6E6E>+%ENCHANT%</font> ";
					if(p.getItemEnchant()>0){
						Enchant = Enchant.replace("%ENCHANT%", String.valueOf(p.getItemEnchant()));
					}else{
						Enchant = "";
					}
					
					String NombreItem = ( opera.isMasterWorkItem(p.getIdItem()) ? "<font color=LEVEL>"+ p.getItemName() +"</font>" : p.getItemName());
					MyItemList += GridItemFormat.replace("%BGCOLOR%", ColorBG[contador%2]) .replace("%SALE_PRICE%", opera.getFormatNumbers(p.getQuantityRequest())) .replace("%REQUEST_ITEM_NAME%", p.getNameItemToRequest()) .replace("%QUANTITY%", opera.getFormatNumbers(p.getQuantityItemToSell())).replace("%REMAINING_TIME%", p.getRemainingTimeFormat()).replace("%ITEM_NAME%", Enchant + " " + NombreItem).replace("%BTN_ITEM%", cbFormato.getBotonForm(opera.getIconImgFromItem(p.getIdItem() , true), BypassItemSee.replace("%IDOBJECTO%", String.valueOf(idObject))));
				}else if(contador >= hasta){
					havenext = true;
					break;
				}
				contador ++;
			}
		}
		
		String PagControl = opera.getGridFormatFromHtml(html, 2, "%PAG_CONTROL%");
		String strPagControl = "";
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";"+ Engine.enumBypass.BidHouse.name() +";0;"+ String.valueOf(PaginaMisItemInventario) +";"+ String.valueOf(idObjecto) +";"+ String.valueOf(PaginaMisVentas - 1) +";0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";"+ Engine.enumBypass.BidHouse.name() +";0;"+ String.valueOf(PaginaMisItemInventario) +";"+ String.valueOf(idObjecto) +";"+ String.valueOf(PaginaMisVentas + 1) +";0;0";
		strPagControl = PagControl.replace("%MY_ACTION_PAGINE_CONTROL_ACTUAL_PAGINE%", String.valueOf((PaginaMisVentas + 1))) .replace("%MY_ACTION_PAGINE_CONTROL_LEFT%", havenext ? bypassProx : "").replace("%MY_ACTION_PAGINE_CONTROL_RIGHT%", (PaginaMisVentas>=1 ? bypassAntes : ""));
		html.replace("%MY_ITEMS_LIST%", MyItemList);
		html.replace("%PAG_CONTROL%", strPagControl);

		return html;
	}
	
	private static void removeAuctionFromBD(L2PcInstance player, int IdObject, boolean fromItemTable, int IdPlayerOwner){
		String consulta = "delete from zeus_bid_house where idObjeto=? and idOwner=?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(consulta))
			{
				statementt.setInt(1, IdObject);
				statementt.setInt(2, IdPlayerOwner);
				statementt.execute();
			}
			catch (Exception e)
			{
				
			}
		
		if(fromItemTable){
			consulta = "delete from items where owner_id=? and object_id=?";
			try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
					PreparedStatement statementt = conn.prepareStatement(consulta))
				{
					statementt.setInt(1, ID_OWNER_BID);
					statementt.setInt(2, IdObject);
					statementt.execute();
				}
				catch (Exception e)
				{
					
				}
		}
	}

	private static InfoBid getAuctionInformationIfExist(int idObject){
		for(InfoBid T1 : ITEMS_ON_BIDS){
			if((T1.getObjectID() == idObject) && T1.isActive()){
				return T1;
			}
		}
		return null;
	}
	
	private static boolean biditProcess(L2PcInstance player, int idObject, Long Bid){
		if(ITEMS_ON_BIDS!=null){
			InfoBid ItemAuc = getAuctionInformationIfExist(idObject);
			if(ItemAuc!=null && ItemAuc.isActive()){
				return ItemAuc.setNewBid(player, Bid);
			}else{
				central.msgbox(language.getInstance().getMsg(player).BH_ITEM_IS_NOT_FOR_SELL_NOW, player);
				return false;				
			}
		}
		return false;
	}

	public static void removeBid(L2PcInstance player){
		removeBid(player, PLAYER_VARIABLES.get(player.getObjectId()).getRemoveObjectSetIDObject());
	}
	
	private static void removeBid(L2PcInstance player, int idObject){
		
		String TaxToRemove = "";
		
		if(general.BIDHOUSE_CANCEL_TAX_FOR_SELLER!=0){
			InfoBid _Temp = getInfoBids(idObject);
			if(_Temp != null){
				if(_Temp.haveBidders()){
					long _tax = Long.valueOf( (_Temp.getActualBid() * general.BIDHOUSE_CANCEL_TAX_FOR_SELLER) / 100l);
					if(opera.haveItem(player, _Temp.getIDItemRequested(), _tax)){
						TaxToRemove = String.valueOf(_Temp.getIDItemRequested()) + "," + String.valueOf(_tax);
					}else{
						central.msgbox(language.getInstance().getMsg(player).BH_SORRY_YOUR_BID_HAS_AN_OFFER_NO_ITEM_PENALTY.replace("$Item_Tax", _Temp.getNameItemRequested())  .replace("$Quantity_Tax", opera.getFormatNumbers(_tax)) , player);
						return;
					}
				}
			}			
		}
		
		
		InfoBid p = getAuctionInformationIfExist(idObject);
		int feePay = p.getFee();
		long Cantidad = p.getQuantityItemToSell();
		int idItem = p.getIdItem();
		L2ItemInstance item = p.getItemInstance();
		
		if(item.isStackable()){
			opera.giveReward(player, idItem, Cantidad);
			opera.giveReward(player, 57, feePay);
			removeAuctionFromBD(player, idObject,true,player.getObjectId());
		}else if(!item.isStackable()){
			player.getInventory().addItem("bid House", item, player, null);
			opera.giveReward(player, 57, feePay);
			removeAuctionFromBD(player, idObject,false,player.getObjectId());
		}
		try{
			p.cancelAllBids();
			ITEMS_ON_BIDS.remove(p);
			if(!TaxToRemove.equals("")){
				opera.removeItem(TaxToRemove, player);
			}
		}catch(Exception a){
			getInfoItemWindows(player, idObject, PLAYER_VARIABLES.get(player.getObjectId()).getMakeBidSection());
			return;
		}
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bidhouse-create-bid-cancel-bid-system.htm");
		central.sendHtml(player, html.getHtml());
		String Actualizar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";"+ String.valueOf( PLAYER_VARIABLES.get(player.getObjectId()).getMakeBidSection()) +";0;0;0;0;0;0";
		cbManager.separateAndSend(bypass(player, Actualizar),player);
	}
	
	private static NpcHtmlMessage getMainWindows_List(L2PcInstance player, int Pagina, String _Grado, String Tipo, String Busqueda){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-bid-house-all-bid.htm");		
		Vector<String> GradosV = new Vector<String>();
		GradosV.add("All");
		GradosV.add("S84");
		GradosV.add("S80");
		GradosV.add("S");
		GradosV.add("A");
		GradosV.add("B");
		GradosV.add("C");
		GradosV.add("D");
		GradosV.add("NONE");
		String GradoAqui = "";
		GradoAqui = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingGrade();
		if(GradoAqui.equals("0")){
			GradoAqui = "All";
		}
		String GradosLista = GradoAqui;
		for (String GR : GradosV){
			if(!GR.equalsIgnoreCase(GradoAqui)){
				GradosLista += ";" + GR;
			}
		}
		html.replace("%ALL_BID_GRADE_LIST%", GradosLista);
		String BypassBusGrado = "bypass ZeuS bh_lin gradelist $cmbItemRequest " + Tipo + " " + (Busqueda.equals("0") ? "-1" : Busqueda);
		String BypassBusWord = "bypass ZeuS bh_lin searchword $cmbItemRequest " + Tipo + " $txtWord";
		String BypassRefresh = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() +";1;0;0;0;0;0";
		
		html.replace("%ALL_BID_APPLY_GRADE_BYPASS%", BypassBusGrado);
		html.replace("%ALL_BID_SEARCH_WORD_BYPASS%", BypassBusWord);
		html.replace("%ALL_BID_REFRESH_BYPASS%", BypassRefresh);

		html = auction_Search_getTypes(player, Tipo, GradoAqui, html);
		
		html = bid_Search_getAllBids(player, Tipo, GradoAqui, Busqueda, Pagina, html);

		return html;
	}

	private static NpcHtmlMessage getMainWindows(L2PcInstance player, int Pagina, int idObjectItem, int paginaMisItemAuction){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bid-house-bid-creation.htm");

		html = getInventaryWindows(player,Pagina,idObjectItem, html);
		html = getSelectedItemToAuction(player,idObjectItem, html);
		html = getSellBox(player,Pagina,idObjectItem, html);
		html = getFeeAndYouAdena(player, idObjectItem, html);
		html = getMyBidList(player,paginaMisItemAuction,Pagina,idObjectItem, html);
           
		return html;
	}
	
	
	public static void getAllItem(){
		getAllItem(true, null, 0);
	}
	
	
	public static void getAllItem(boolean isAutomatic, L2PcInstance player, int idItem){
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time, owner_id FROM items WHERE owner_id=? ORDER BY loc_data"))
			{
				statement.setInt(1, ID_OWNER_BID);
				try (ResultSet inv = statement.executeQuery())
				{
					L2ItemInstance item;
					while (inv.next())
					{
						if(isAutomatic || (!isAutomatic && idItem == inv.getInt(1))){
							item = L2ItemInstance.restoreFromDb(ID_OWNER_BID, inv);
							if (item == null)
							{
								continue;
							}
							try{
								L2World.getInstance().removeObject(item);
							}catch(Exception a){
							}
							L2World.getInstance().storeObject(item);
							
							InfoBid V = new InfoBid(item.getObjectId(),item);
							if(ITEMS_ON_BIDS!=null){
								InfoBid T1 = getAuctionInformationIfExist(item.getObjectId());
								if(T1 != null){
									ITEMS_ON_BIDS.remove(V);
								}
							}
							ITEMS_ON_BIDS.add(V);
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Could not restore inventory: " + e.getMessage());
			}
	}
	
	
	private static String mainHtml(L2PcInstance player, String Params, int PaginaInventario, int idObjectItem, int PaginaMisItem){
		if(PLAYER_VARIABLES==null){
			_variableBidHouseCreate tem = new _variableBidHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tem);
		}else if(PLAYER_VARIABLES.size()==0 ){
			_variableBidHouseCreate tem = new _variableBidHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tem);			
		}else if(!PLAYER_VARIABLES.containsKey(player.getObjectId())){
			_variableBidHouseCreate tem = new _variableBidHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tem);
		}
		NpcHtmlMessage html = null;
		if(Params.split(";").length>1){
			if(Params.split(";")[2].equals("0")){
				html = getMainWindows(player,PaginaInventario,idObjectItem, PaginaMisItem);
			}else if(Params.split(";")[2].equals("1")){
				String Grado = Params.split(";")[3];
				String Tipo = Params.split(";")[4];
				String Pagina = Params.split(";")[5];
				String Busqueda = Params.split(";")[6];
				html = getMainWindows_List(player, Integer.valueOf(Pagina), Grado, Tipo, Busqueda);
			}else if(Params.split(";")[2].equals("2")){
				html = getAllPersonalBids(player);
			}
		}else{
			html = getMainWindows(player,PaginaInventario,idObjectItem, PaginaMisItem);
		}

		String ByPassList = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.BidHouse.name() + ";1;0;0;0;0;0;0";
		String ByPassMy = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.BidHouse.name() + ";0;0;0;0;0;0;0";
		String ByPassMyBids = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.BidHouse.name() + ";2;0;0;0;0;0;0";
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%ALL_BID_LIST_BYPASS%", ByPassList);
		html.replace("%MY_BID_CREATE_LIST%", ByPassMy);
		html.replace("%MY_BIDS_LIST%", ByPassMyBids);		
		
		return html.getHtml();
	}
	
	private static int getIdItemFromItemRequest(String ItemRequest){
		return getIdFromItemRequest(ItemRequest);
	}
	
	public static void setCreationBid(L2PcInstance player){
		_variableBidHouseCreate temp = PLAYER_VARIABLES.get(player.getObjectId());
		createAuctionWithItem(player, temp.getCreateItemIdObject() , temp.getCreateIdItemRequested() , temp.getCreateQuantityToGive() , temp.getCreateMinBid() );
		String htmlCB = mainHtml(player,"",temp.getCreateBidPagine(),0,0);
		cbManager.separateAndSend(htmlCB, player);
	}
	
	public static void setMakeaBid(L2PcInstance player){
		_variableBidHouseCreate temp = PLAYER_VARIABLES.get(player.getObjectId());
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-bidhouse-create-bid-done.htm");
		if(biditProcess(player, temp.getMakeBidIdObject() , temp.getMakeBidAmountOffert())){
			String Retornar = "";
			int ActualSection = PLAYER_VARIABLES.get(player.getObjectId()).getLastSection();
			Retornar = bypass(player, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() +";"+ String.valueOf(ActualSection) +";0;0;0;0;0");
			cbManager.separateAndSend(Retornar, player);
			central.sendHtml(player, html.getHtml());
		}else{
			central.msgbox("Wrog Data.", player);
		}		
	}
	
	public static void _ByPass(L2PcInstance player, String params){
		if(!general.STATUS_BIDHOUSE) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return;			
		}
		
		if(params==null){
			central.msgbox("Input error, Try Again.", player);
			return;
		}
		
		if(params.length()==0){
			central.msgbox("Input error, Try Again.", player);
			return;			
		}
		String []cm = params.split(" ");
		if(cm[0].equals("BuyItNow")){
			int idItem = Integer.valueOf(cm[1]);
			Long BidValue = Long.valueOf(cm[2]);
			int idSection = Integer.valueOf(cm[3]);
			InfoBid DataBidTemp = getInfoBids(idItem);
			Long LeftToRemove = 0l;
			if(DataBidTemp != null) {
				LeftToRemove = DataBidTemp.getActualItemToRemove(player, BidValue);
			}
			if(LeftToRemove <= 0l) {
				LeftToRemove = BidValue;
			}
			PLAYER_VARIABLES.get(player.getObjectId()).setVariableNewOfert(idItem, BidValue, idSection);
			String _Question = language.getInstance().getMsg(player).BH_QUESTION_YOU_HAVE_MAKE_NEW_OFFER.replace("$AmountToRemove", String.valueOf(LeftToRemove)) .replace("$Item", getInfoBids(idItem).getNameItemRequested()).replace("$Amount", opera.getFormatNumbers(BidValue));
			Dlg.sendDlg(player, _Question, IdDialog.ENGINE_BID_HOUSE_CREATE_MAKE_NEW_BID);
		}else if(cm[0].equals("create")){
			int Pagina = Integer.valueOf(cm[1]);
			int idObjeto = Integer.valueOf(cm[2]);
			Long Cantidad_A_Vender = Long.valueOf(cm[3]);
			int TipoMonedaPide = getIdItemFromItemRequest(cm[4]);
			Long ValorPorCadaItemA_Vender = Long.parseLong(cm[5]);
			PLAYER_VARIABLES.get(player.getObjectId()).setVariableNewBid(Pagina, idObjeto, TipoMonedaPide, Cantidad_A_Vender, ValorPorCadaItemA_Vender);
			String htmlCB = mainHtml(player,"",Pagina,0,0);
			cbManager.separateAndSend(htmlCB, player);
			String __Question = language.getInstance().getMsg(player).BH_QUESTION_YOU_GONNA_CREATE_NEW_BID.replace("$Offer", opera.getFormatNumbers(ValorPorCadaItemA_Vender))  .replace("$Item", PLAYER_VARIABLES.get(player.getObjectId()).getItemName()).replace("$Amount", opera.getFormatNumbers(Cantidad_A_Vender));
			Dlg.sendDlg(player, __Question, IdDialog.ENGINE_BID_HOUSE_CREATE_NEW_BID, 80);
		}else if(cm[0].equals("gradelist")){
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingGrade(cm[1]);
			String TipoMenu = cm[2];
			String ByPassEnviar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +  ";" + Engine.enumBypass.BidHouse.name() + ";1;"+ cm[1] +";"+ TipoMenu +";0;0;0";
			String retorno = bypass(player, ByPassEnviar);
			cbManager.separateAndSend(retorno, player);
		}else if(cm[0].equals("searchword")){
			String Grado_ = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingGrade();
			String _BString = "";
			for (int cont=3 ; cont <= cm.length - 1 ; cont++ ){
				_BString += " " + cm[cont];
			}
			_BString =  _BString.trim(); 
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingWords(_BString);
			String TipoMenu = cm[2];
			String ByPassEnviar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +  ";" + Engine.enumBypass.BidHouse.name() + ";1;"+ Grado_ +";"+ TipoMenu +";0;"+ _BString +";0";
			String retorno = bypass(player, ByPassEnviar);
			cbManager.separateAndSend(retorno, player);
		}
	}
	
	public static String getBypassCreate(L2PcInstance player){
		String GradeIN = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingGrade();
		String _strBuscar = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingWords();
		
		String tipo = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingItemType();
		
		String retorno = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";1;" + GradeIN + ";" + tipo + ";0;" + _strBuscar + ";0";
		return retorno;
	}
	
	private static NpcHtmlMessage bid_Search_getAllBids(L2PcInstance player, String tipo, String _Grade_, String Buscar, int Pagina, NpcHtmlMessage html){
		String ByPassItemToSell = "bypass ZeuS bh_sort nameAZ";
		String ByPassQuantityToSell = "bypass ZeuS bh_sort quantityAZ";
		String ByPassItemRequestToSell = "bypass ZeuS bh_sort itemrequestAZ";
		String ByPassSalePrice = "bypass ZeuS bh_sort priceAZ";
		
		switch(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSortGeneralList()){
			case "nameAZ":
				ByPassItemToSell = "bypass ZeuS bh_sort nameZA";
				break;
			case "quantityAZ":
				ByPassQuantityToSell = "bypass ZeuS bh_sort quantityZA";
				break;
			case "itemrequestAZ":
				ByPassItemRequestToSell = "bypass ZeuS bh_sort itemrequestZA";
				break;
			case "priceAZ":
				ByPassSalePrice = "bypass ZeuS bh_sort priceZA";
				break;
		}
		
		
		Map<String, String> GradeItem = new HashMap<String, String>();
		GradeItem.put("S84", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_84\" width=16 height=16>");
		GradeItem.put("S80", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_80\" width=16 height=16>");
		GradeItem.put("S", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_S\" width=16 height=16>");
		GradeItem.put("A", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_A\" width=16 height=16>");
		GradeItem.put("B", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_B\" width=16 height=16>");
		GradeItem.put("C", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_C\" width=16 height=16>");
		GradeItem.put("D", "<img src=\"L2UI_CT1.Icon_DF_ItemGrade_D\" width=16 height=16>");
		GradeItem.put("", "");		
		
		html.replace("%ALL_BID_SORT_BY_ITEM_NAME%", ByPassItemToSell);
		html.replace("%ALL_BID_SORT_BY_QUANTITY%", ByPassQuantityToSell);
		html.replace("%ALL_BID_SORT_ITEM_REQUESTED%", ByPassItemRequestToSell);
		html.replace("%ALL_BID_SORT_CURRENT_BID%", ByPassSalePrice);

		String AllItemGridFormat = opera.getGridFormatFromHtml(html, 4, "%ALL_FIND_ITEM%");
		String AllItemGrid = "";
		
		
		String []BGColor = {"031C1C","011313"};
		
		int Contador=0;
		int MaximoLista = 7;
		boolean haveNext = false;
		int desde = MaximoLista * Pagina;
		int hasta = desde + MaximoLista;
		
		String _strBuscar = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingWords();
		
		String tipoIndex = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingItemTypeIndex();

		String ItemByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";showItemWindows;%IDOBJETO%;0;0;0;0";

		String GradeIN = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingGrade();
		
		for( InfoBid p : SortedData(player)){
			if(haveNext){
				break;
			}
			if(!p.isActive()){
				continue;
			}
			boolean Grabar = true;
			if(!GradeIN.equals("0") && !GradeIN.equalsIgnoreCase("All")){
				if(!p.getItemGrade().equalsIgnoreCase(GradeIN)){
					Grabar = false;
					continue;
				}
			}
			if( _strBuscar.length()>0 ){
				if( p.getItemName().toUpperCase().indexOf( _strBuscar.toUpperCase() )<0 ){
					Grabar=false;
					continue;
				}
			}
			if(tipoIndex.length()>0){
				if(!TypeSearch_addToList(p,tipoIndex)){
					continue;
				}
			}
			
			if(Grabar){
				String Grados = "";
				Grados = GradeItem.get(p.getItemGrade());
				if(Grados==null){
					Grados="";
				}else if(Grados.isEmpty()){
					Grados = "";
				}
				
				
				if(Contador >= desde && Contador < hasta){
					Integer[]Atri = new Integer[6];
					
					Atri[5] = p.getDivineAtributte();
					Atri[4] = p.getDarkAtributte();
					Atri[2] = p.getEarthAtributte();
					Atri[3] = p.getWindAtributte();
					Atri[1] = p.getWaterAtributte();
					Atri[0] = p.getFireAtributte();
					
					if(p.getItemInstance().isWeapon()){
						if(p.getItemInstance().getAttackElementType()>=0){
							Atri[p.getItemInstance().getAttackElementType()] = p.getItemInstance().getAttackElementPower();
						}
					}
					
					
                    String A_Fire = Atri[0]>0 ? "<font color=AB3737>Fire:"+ String.valueOf(Atri[0]) +"</font> ": "";
                    String A_Water = Atri[1]>0 ? "<font color=4965CB>Water:"+ String.valueOf(Atri[1]) +"</font> ": "";
                    String A_Earth = Atri[2]>0 ? "<font color=A9803F>Earth:"+ String.valueOf(Atri[2]) +"</font> ": "";
                    String A_Wind = Atri[3]>0 ? "<font color=4C9961>Wind:"+ String.valueOf(Atri[3]) +"</font> ": "";
                    String A_Dark = Atri[4]>0 ? "<font color=8E3FAD>Dark:"+ String.valueOf(Atri[4]) +"</font> ": "";
                    String A_Divine = Atri[5]>0 ? "<font color=808080>Divine:"+ String.valueOf(Atri[5]) +"</font> ": "";
					
                    String ElementalItem = (A_Fire + A_Water + A_Earth + A_Wind + A_Dark + A_Divine).trim();
                    
                    boolean isOwner = (p.getPlayerID() == player.getObjectId());
                    
					String Nombre = (p.getItemEnchant()>0 ? "<font color=5D6C5E>+"+ String.valueOf( p.getItemEnchant() ) + "</font> " : "" ) + ( opera.isMasterWorkItem(p.getIdItem()) ? "<font color=\"LEVEL\">" + (isOwner ? "**" : "")  +  p.getItemName() + (isOwner ? "**" : "") + "</font>" : (isOwner ? "**" : "") +  p.getItemName() + (isOwner ? "**" : ""));
					Nombre += "<br1>" + ElementalItem;
					
					AllItemGrid += AllItemGridFormat.replace("%REMAINING_TIME%", p.getRemainingTimeFormat()).replace("%SALE_PRICE%", opera.getFormatNumbers(p.getActualBid())).replace("%ITEM_REQUEST_NAME%", p.getNameItemToRequest()) .replace("%CANTIDA_VENDER%", opera.getFormatNumbers(p.getQuantityItemToSell())) .replace("%GRADO%", Grados).replace("%NOMBRE%", Nombre) .replace("%ICONO_LINK%", cbFormato.getBotonForm(opera.getIconImgFromItem(p.getIdItem(), true), ItemByPass.replace("%IDOBJETO%", String.valueOf(p.getItemInstance().getObjectId()))) ) .replace("%BGCOLOR%", BGColor[Contador%2]);
				}else if(Contador >= hasta){
						haveNext=true;
						break;
				}
				Contador ++;
			}
		}
		
		html.replace("%ALL_FIND_ITEM%", AllItemGrid);
		
		
		String ByPass_Prev = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";" + String.valueOf( Pagina - 1 ) + ";" + _strBuscar + ";0";
		String ByPass_Next = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";" + String.valueOf( Pagina + 1 ) + ";" + _strBuscar + ";0";
		
		html.replace("%ALL_BID_CONTROL_PAGE_PREVIEW_BYPASS%", ( Pagina>0 ? ByPass_Prev : "" ));
		html.replace("%ALL_BID_CONTROL_PAGE_NEXT_BYPASS%", ( haveNext ? ByPass_Next : "" ));
		html.replace("%ALL_BID_CONTROL_PAGE%", String.valueOf(Pagina+ 1));		
		
		return html;
	}
	
	private static NpcHtmlMessage auction_Search_getTypes(L2PcInstance player, String Tipo, String _Grade, NpcHtmlMessage html){
		String Grade = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingGrade();
		
		String ByPass_TYPE_JEWEL = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";1;" + Grade + ";J;0;0;0";
		String ByPass_TYPE_ARMOR = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";1;" + Grade + ";A;0;0;0";
		String ByPass_TYPE_WEAPON = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";1;" + Grade + ";W;0;0;0";
		
		html.replace("%ALL_BID_OPEN_JEWEL%", ByPass_TYPE_JEWEL);
		html.replace("%ALL_BID_OPEN_ARMOR%", ByPass_TYPE_ARMOR);
		html.replace("%ALL_BID_OPEN_WEAPON%", ByPass_TYPE_WEAPON);

		String ByPass_RING= "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";-1;JEWEL_RING;" + Tipo + ";0;0;0";
		String ByPass_EARRING= "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";-1;JEWEL_EARRING;" + Tipo + ";0;0;0";
		String ByPass_NECKLACE= "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";-1;JEWEL_NECKLACE;" + Tipo + ";0;0;0";
		html.replace("%ALL_BID_JEWEL_RING%", ByPass_RING);
		html.replace("%ALL_BID_JEWEL_EARRING%", ByPass_EARRING);
		html.replace("%ALL_BID_JEWEL_NECKLACE%", ByPass_NECKLACE);
		
		String ByPassArmor = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";-1;ARMOR_%TIP%;" + Tipo + ";0;0;0";
		html.replace("%ALL_BID_ARMOR_HELMET%", ByPassArmor.replace("%TIP%", "HELMET"));
		html.replace("%ALL_BID_ARMOR_CHEST%", ByPassArmor.replace("%TIP%", "CHEST"));
		html.replace("%ALL_BID_ARMOR_LEGS%", ByPassArmor.replace("%TIP%", "LEGS"));
		html.replace("%ALL_BID_ARMOR_GLOVES%", ByPassArmor.replace("%TIP%", "GLOVES"));
		html.replace("%ALL_BID_ARMOR_SHOES%", ByPassArmor.replace("%TIP%", "SHOES"));
		html.replace("%ALL_BID_ARMOR_CLOAK%", ByPassArmor.replace("%TIP%", "CLOAK"));
		html.replace("%ALL_BID_ARMOR_SHIRT%", ByPassArmor.replace("%TIP%", "SHIRT"));
		html.replace("%ALL_BID_ARMOR_BELT%", ByPassArmor.replace("%TIP%", "BELT"));
		html.replace("%ALL_BID_ARMOR_SIGIL%", ByPassArmor.replace("%TIP%", "SIGIL"));
		html.replace("%ALL_BID_ARMOR_SHIELD%", ByPassArmor.replace("%TIP%", "SHIELD"));
		
		String ByPassWaepon = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";-1;WEAPON_%TIP%;" + Tipo + ";0;0;0";
		html.replace("%ALL_BID_WEAPON_SWORD%", ByPassWaepon.replace("%TIP%", "SWORD"));
		html.replace("%ALL_BID_WEAPON_DUAL_SWORD%", ByPassWaepon.replace("%TIP%", "DUALSWORD"));
		html.replace("%ALL_BID_WEAPON_BIG_SWORD%", ByPassWaepon.replace("%TIP%", "BIGSWORD"));
		html.replace("%ALL_BID_WEAPON_ANCIENT_SWORD%", ByPassWaepon.replace("%TIP%", "ANCIENTSWORD"));
		html.replace("%ALL_BID_WEAPON_BLUNT%", ByPassWaepon.replace("%TIP%", "BLUNT"));
		html.replace("%ALL_BID_BIG_BLUNT%", ByPassWaepon.replace("%TIP%", "BIGBLUNT"));
		html.replace("%ALL_BID_DAGGER%", ByPassWaepon.replace("%TIP%", "DAGGER"));
		html.replace("%ALL_BID_DUAL_DAGGER%", ByPassWaepon.replace("%TIP%", "DUALDAGGER"));
		html.replace("%ALL_BID_BOW%", ByPassWaepon.replace("%TIP%", "BOW"));
		html.replace("%ALL_BID_CROSSBOW%", ByPassWaepon.replace("%TIP%", "CROSSBOW"));
		html.replace("%ALL_BID_POLE%", ByPassWaepon.replace("%TIP%", "POLE"));
		html.replace("%ALL_BID_FISTS%", ByPassWaepon.replace("%TIP%", "FISTS"));
		html.replace("%ALL_BID_RAPIER%", ByPassWaepon.replace("%TIP%", "RAPIER"));
		html.replace("%ALL_BID_OTHER%", ByPassWaepon.replace("%TIP%", "OTHER"));
		
		//PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingTypeItem(Tipo);
		
		String GridJewel = opera.getGridFormatFromHtml(html, 1, "%GRID_JEWEL%");
		String GridArmor = opera.getGridFormatFromHtml(html, 2, "%GRID_ARMOR%");
		String GridWeapon = opera.getGridFormatFromHtml(html, 3, "%GRID_WEAPON%");
		
		
		if(Tipo.equalsIgnoreCase("J")){
			html.replace("%GRID_JEWEL%", GridJewel);
			html.replace("%GRID_ARMOR%", "");
			html.replace("%GRID_WEAPON%", "");
		}else if(Tipo.equalsIgnoreCase("A")){
			html.replace("%GRID_JEWEL%", "");
			html.replace("%GRID_ARMOR%", GridArmor);
			html.replace("%GRID_WEAPON%", "");
		}else if(Tipo.equalsIgnoreCase("W")){
			html.replace("%GRID_JEWEL%", "");
			html.replace("%GRID_ARMOR%", "");
			html.replace("%GRID_WEAPON%", GridWeapon);
		}else if(Tipo.equalsIgnoreCase("0")){
			html.replace("%GRID_JEWEL%", "");
			html.replace("%GRID_ARMOR%", "");
			html.replace("%GRID_WEAPON%", "");
		}
		return html;
	}

	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(!general.STATUS_BIDHOUSE) {
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return "";			
		}
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return "";
		}		
		if(general.BIDHOUSE_ONLY_IN_PEACE_ZONE){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				central.msgbox(language.getInstance().getMsg(player).BH_ONLY_IN_PEACE_ZONE, player);
				return Comunidad.getComunidad(player, "", 0);
			}
		}
		if(PLAYER_VARIABLES==null){
			_variableBidHouseCreate tmp = new _variableBidHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tmp);
		}else if(PLAYER_VARIABLES.size()==0){
			_variableBidHouseCreate tmp = new _variableBidHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tmp);
		}else if(!PLAYER_VARIABLES.containsKey(player.getObjectId())){
			_variableBidHouseCreate tmp = new _variableBidHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tmp);
		}
		String[] Eventos = params.split(";");
		String parm1 = Eventos[2];
		String parm2 = Eventos[3];
		String parm3 = Eventos[4];
		String parm4 = Eventos[5];
		String parm5 = Eventos[6];
		String parm6 = Eventos[7];
		
		if(parm1.equals("-1")){
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingItemTypeIndex(parm2);
			String ByPassIn = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.BidHouse.name() + ";1;" + PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSearchingGrade() + ";" + parm3 + ";0;0;0";
			return mainHtml(player,ByPassIn,0,0,0);
		}else if(parm1.equals("0")){
			PLAYER_VARIABLES.get(player.getObjectId()).setLastSection(0);
			return mainHtml(player,params,Integer.valueOf(parm2),Integer.valueOf(parm3), Integer.valueOf(parm4));
		}else if(parm1.equals("1")){
			PLAYER_VARIABLES.get(player.getObjectId()).setLastSection(1);
			if(parm2.equals("0")){
				PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingGrade("All");
				PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingItemTypeIndex("");
			}
			
			return mainHtml(player,params,0,0,0);
		}else if(parm1.equals("2")){
			PLAYER_VARIABLES.get(player.getObjectId()).setLastSection(2);
			return mainHtml(player, params, 0, 0, 0);
		}else if(parm1.equals("showItemWindows")){
			getInfoItemWindows(player, Integer.valueOf(parm2), Integer.valueOf(parm3));
		}else if(parm1.equals("cancelbidAsk")){
			int _idoBjectToCancel = Integer.valueOf(parm2);
			PLAYER_VARIABLES.get(player.getObjectId()).setRemoveObjectSetIDObject(_idoBjectToCancel);
			InfoBid _Temp = getInfoBids(_idoBjectToCancel);
			if(_Temp != null){
				if(_Temp.haveBidders()){
					long _tax = Long.valueOf( (_Temp.getActualBid() * general.BIDHOUSE_CANCEL_TAX_FOR_SELLER) / 100l);
					if(opera.haveItem(player, _Temp.getIDItemRequested(), _tax)){
						String _msgeSend = language.getInstance().getMsg(player).BH_SORRY_YOUR_BID_HAS_AN_OFFER_QUESTION.replace("$Item_Tax", _Temp.getNameItemRequested()) .replace("$Quantity_Tax", opera.getFormatNumbers(_tax)).replace("$Item", _Temp.getNameItemRequested()).replace("$Quantity", opera.getFormatNumbers(_Temp.getActualBid())) ;
						Dlg.sendDlg(player, _msgeSend, IdDialog.ENGINE_BID_HOUSE_CANCEL_BID_WITH_BIDDER,90);
					}else{
						central.msgbox(language.getInstance().getMsg(player).BH_SORRY_YOUR_BID_HAS_AN_OFFER_NO_ITEM_PENALTY.replace("$Item_Tax", _Temp.getNameItemRequested())  .replace("$Quantity_Tax", opera.getFormatNumbers(_tax)) , player);
					}
				}else{
					
				}
			}
		}else if(parm1.equals("cancelbid")){
			removeBid(player, Integer.valueOf(parm2));
		}else if(parm1.equals("buyItNow")){
			biditProcess(player, Integer.valueOf(parm2),Long.valueOf(parm3));
		}
		return "";
	}
}

class InfoBid{
	private boolean IS_ACTIVE;
	private String ITEM_NAME;
	private String ITEM_GRADE;
	private int ITEM_ID_OBJECT;
	private int ITEM_ENCHANT;
	
	private final int SECOND_SEND_EMAIL_AT_START = 60;
	
	private L2ItemInstance ITEM_INSTANCE;
	
	private Long SELL_QUANTITY_ITEM_TO_SELL;
	private Long SELL_PRICE_ITEM_BY_1;
	private int SELL_ITEM_REQUEST_ID;
	private String SELL_ITEM_REQUEST_NAME;
	private String PLAYER_NAME;
	private int PLAYER_ID;
	private int SELL_FEE;
	private int BEGIN_TIME;
	private int FINISH_TIME;
	private Long ACTUAL_BID = 0l;
	private String ACTUAL_BIDDER_NAME = "";
	private int ACTUAL_BIDDER_ID = -1;
	private Map<Integer, _bidInformation> BIDS_PLAYER = new HashMap<Integer, _bidInformation>();
	
	private final Logger _log = Logger.getLogger(v_auction_house.class.getName());

	public final Map<Integer, _bidInformation> getAllBiddersOrderByBid(){
		Map<Integer, _bidInformation> DataOrder = null;		
		Map<Integer, _bidInformation> newMapSortedByKey = this.BIDS_PLAYER.entrySet().stream()
                .sorted((e1,e2) -> Long.compare(e2.getValue().getBid() , e1.getValue().getBid()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1,e2) -> e1, LinkedHashMap::new));
		DataOrder = newMapSortedByKey;
		return DataOrder; 
	}
	
	public final int getBeginTime(){
		return this.BEGIN_TIME;
	}
	
	public final boolean haveBidders(){
		if(this.BIDS_PLAYER==null){
			return false;
		}else if(this.BIDS_PLAYER.size()==0){
			return false;
		}
		return true;
	}
	
	public final Long getActualBid(){
		if(this.ACTUAL_BID == 0){
			this.ACTUAL_BID = this.SELL_PRICE_ITEM_BY_1;
		}
		return this.ACTUAL_BID;
	}
	public final String getActualBidPlayerName(){
		return this.ACTUAL_BIDDER_NAME;
	}
	public final int getActualBidPlayerID(){
		return this.ACTUAL_BIDDER_ID;
	}
	
	public Long getActualItemToRemove(L2PcInstance player, Long Bid) {
		if(this.BIDS_PLAYER != null){
			if(this.BIDS_PLAYER.size() > 0){
				if(this.BIDS_PLAYER.containsKey(player.getObjectId())){
					return Bid - this.BIDS_PLAYER.get(player.getObjectId()).getBid();
				}
			}
		}
		return 0l;
	}
	
	public boolean setNewBid(L2PcInstance player, Long Bid){
		if(Bid > this.ACTUAL_BID){
			String tempData = opera.getDateFromUnixTime( opera.getUnixTimeNow() );
			Long ItemToRemove = 0l;
			boolean isNewBid = false;
			if(this.BIDS_PLAYER != null){
				if(this.BIDS_PLAYER.size() > 0){
					if(this.BIDS_PLAYER.containsKey(player.getObjectId())){
						ItemToRemove = Bid - this.BIDS_PLAYER.get(player.getObjectId()).getBid();
					}else{
						isNewBid = true;	
					}
				}else{
					isNewBid = true;
				}
			}else{
				isNewBid = true;
			}
			
			if(isNewBid){
				
				if(!opera.haveItem(player, this.SELL_ITEM_REQUEST_ID, Bid)) {
					return false;
				}
				
				_bidInformation tmp = new _bidInformation(Bid, tempData,player.getObjectId(), player.getName());
				this.BIDS_PLAYER.put(player.getObjectId(), tmp);
				opera.removeItem(this.SELL_ITEM_REQUEST_ID, Bid, player);
			}else{
				if(!opera.haveItem(player, this.SELL_ITEM_REQUEST_ID, ItemToRemove)) {
					return false;
				}				
				this.BIDS_PLAYER.get(player.getObjectId()).setNewBid(Bid);
				opera.removeItem(this.SELL_ITEM_REQUEST_ID, ItemToRemove, player);
			}
			
			if(!this.ACTUAL_BIDDER_NAME.equalsIgnoreCase(player.getName()) && this.ACTUAL_BIDDER_NAME.length()>0){
				sendEmailLostPlace(this.ACTUAL_BIDDER_NAME,Bid);
			}				
			
			this.ACTUAL_BID = Bid;
			this.ACTUAL_BIDDER_NAME = player.getName();
			this.ACTUAL_BIDDER_ID = player.getObjectId();
		
			this.saveBidInDb(player.getObjectId(), Bid, player.getName());
			return true;
		}else{
			central.msgbox("Worg Bid Data Amount", player);
		}
		return false;
	}
	
	@SuppressWarnings("rawtypes")
	public void checkTheWinner(){
		int IDPlayerWinner = -1;
		String NamePlayerWinner = "";
		Long MayorBid = this.SELL_PRICE_ITEM_BY_1;
		boolean areWinner = false;
		if(BIDS_PLAYER!=null){
			if(BIDS_PLAYER.size()>0){
				Iterator itr = this.BIDS_PLAYER.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry inf = (Map.Entry)itr.next();
					int idPlayer = (int)inf.getKey();
					_bidInformation tmp = (_bidInformation)inf.getValue();
					if(tmp.getBid()>MayorBid){
						IDPlayerWinner = idPlayer;
						NamePlayerWinner = tmp.getPlayerName();
						MayorBid = tmp.getBid();
						areWinner = true;
					}
				}
			}
		}
		if(areWinner){
			String Query = "INSERT INTO zeus_bid_players_winners(idObjeto, id_player_bid, name_player_bid) VALUES (?,?,?)";
			Connection con = null;
			PreparedStatement ins = null;
			try{
				con = L2DatabaseFactory.getInstance().getConnection();
				ins = con.prepareStatement(Query);
				ins.setInt(1, this.ITEM_INSTANCE.getObjectId());
				ins.setInt(2, IDPlayerWinner);
				ins.setString(3, NamePlayerWinner);
				try{
					ins.executeUpdate();
					ins.close();
				}catch(SQLException e){
					_log.warning("Error ZeuS E ->" + e.getMessage());
				}
			}catch(SQLException a){
				_log.warning("Error ZeuS A ->" + a.getMessage());
			}
			try{
				con.close();
			}catch(SQLException a){
				_log.warning("Error ZeuS A2 ->" + a.getMessage());
			}
			
			removeBid();
			removeBidderFromBD( IDPlayerWinner );
			sendEmailToWinner(IDPlayerWinner);
			sendEmailToSeller(NamePlayerWinner);
			
			Iterator itr = this.BIDS_PLAYER.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry inf = (Map.Entry)itr.next();
				int idPlayer = (int)inf.getKey();
				if(IDPlayerWinner != idPlayer){
					_bidInformation infoBid = (_bidInformation)inf.getValue();
					String BidderName = infoBid.getPlayerName();
					String ItemToReturn = String.valueOf(this.SELL_ITEM_REQUEST_ID) + "," + String.valueOf(infoBid.getBid());
					String _title = language.getInstance().getMsg(idPlayer).BH_EMAIL_TITLE_YOU_LOOSE_THE_ITEM_BID_BY_TIME.replace("$Item", this.ITEM_NAME);
					String _msge = language.getInstance().getMsg(idPlayer).BH_EMAIL_MESSAGE_YOU_LOOSE_THE_ITEM_BID_BY_TIME.replace("$BidItem", this.SELL_ITEM_REQUEST_NAME).replace("$BidWinner", opera.getFormatNumbers(MayorBid)) .replace("$winner_name", NamePlayerWinner) .replace("$Item", this.ITEM_NAME);
					EmailSend.sendEmail(null, BidderName, _title,  _msge, ItemToReturn, tipoMensaje.Bid_house);
					removeBidderFromBD(idPlayer);
				}
			}			
		}else{
			removeBid();
			sendEmailToOwnerNoBids();
		}
		this.IS_ACTIVE = false;
	}
	
	private void removeBid(){
		String consulta = "delete from zeus_bid_house where idObjeto=? and idOwner=?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(consulta))
		{
			statementt.setInt(1, this.ITEM_ID_OBJECT );
			statementt.setInt(2, this.PLAYER_ID);
			statementt.execute();
		}
		catch (Exception e)
		{
			
		}
	}
	
	private void sendEmailToOwnerNoBids(){
		EmailSend.bidSendItemToWinner(this.PLAYER_ID, this.ITEM_ID_OBJECT, this.ITEM_INSTANCE, "Bid timeover.","Bid Canceled for not having offers");		
	}
	
	private void sendEmailToWinner(int idPlayer){
		EmailSend.bidSendItemToWinner(idPlayer, this.ITEM_ID_OBJECT,this.ITEM_INSTANCE,"Congratulation, you won the Bid","Congratulation. You won the Bid of " + this.getItemName() + " for " + opera.getFormatNumbers(this.SELL_PRICE_ITEM_BY_1) + " " + this.SELL_ITEM_REQUEST_NAME);
	}
	
	private void sendEmailToSeller(String WinnerName){
		String ItemsToGive = String.valueOf(this.SELL_ITEM_REQUEST_ID) + "," + String.valueOf(this.ACTUAL_BID);
		String _title = language.getInstance().getMsg(this.PLAYER_ID).BH_EMAIL_TITLE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT.replace("$Item", this.ITEM_NAME);
		String _msge = language.getInstance().getMsg(this.PLAYER_ID).BH_EMAIL_MESSAGE_YOUR_BID_IS_OVER_AND_YOU_SOLD_IT.replace("$WinnerName", WinnerName).replace("$ItemRequested", this.SELL_ITEM_REQUEST_NAME).replace("$MayorBid", opera.getFormatNumbers(this.ACTUAL_BID)).replace("$Item", this.ITEM_NAME).replace("$Quantity", opera.getFormatNumbers(this.SELL_QUANTITY_ITEM_TO_SELL));
		EmailSend.sendEmail(null, this.PLAYER_NAME, _title, _msge, ItemsToGive, tipoMensaje.Bid_house);
	}
	
	@SuppressWarnings("rawtypes")
	public void cancelAllBids(){
		if(this.BIDS_PLAYER==null){
			return;
		}
		if(this.BIDS_PLAYER.size()<=0){
			return;
		}
		Iterator itr = this.BIDS_PLAYER.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry inf = (Map.Entry)itr.next();
			int idPlayer = (int)inf.getKey();
			this.cancelBidsByPlayer(idPlayer, true);
		}
	}
	
	public void cancelBidsByPlayer(int IdPlayer, boolean isFromOwnerBid){
		if(this.BIDS_PLAYER==null){
			return;
		}
		if(this.BIDS_PLAYER.size()<=0){
			return;
		}
		_bidInformation infoBid = this.BIDS_PLAYER.get(IdPlayer);
		String BidderName = infoBid.getPlayerName();
		String ItemToReturn = String.valueOf(this.SELL_ITEM_REQUEST_ID) + "," + String.valueOf(infoBid.getBid());
		String _title = "";
		String _msge = "";
		if(isFromOwnerBid){
			_title = language.getInstance().getMsg(IdPlayer).BH_EMAIL_TITLE_BID_WAS_CANCEL_BY_OWNER.replace("$Item", this.ITEM_NAME);
			_msge = language.getInstance().getMsg(IdPlayer).BH_EMAIL_MESSAGE_BID_WAS_CANCEL_BY_OWNER.replace("$Item", this.ITEM_NAME).replace("$playerName", this.PLAYER_NAME);
		}else{
			_title = language.getInstance().getMsg(IdPlayer).BH_EMAIL_TITLE_CANCEL_MY_OFFERT;
			_msge = language.getInstance().getMsg(IdPlayer).BH_EMAIL_MESSAGE_CANCEL_MY_OFFERT;
		}
		EmailSend.sendEmail(null, BidderName, _title, _msge, ItemToReturn, tipoMensaje.Bid_house);
		removeBidderFromBD(IdPlayer);
		boolean isFirts = (this.ACTUAL_BIDDER_ID == IdPlayer); 
		this.BIDS_PLAYER.remove(IdPlayer);
		if(isFirts){
			this.updatePlaces();
		}
	}
	
	@SuppressWarnings("rawtypes")
	private void updatePlaces(){
		long tempBid = this.getQuantityRequest();
		int idTemp = -1;
		String nameTemp = "";
		Iterator itr = this.BIDS_PLAYER.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int IdPlayer = (int) Entrada.getKey();
			_bidInformation bidInfo = (_bidInformation) Entrada.getValue();
			if(bidInfo.getBid() > tempBid){
				idTemp = IdPlayer;
				nameTemp = bidInfo.getPlayerName();
				tempBid = bidInfo.getBid();
			}
		}
		this.ACTUAL_BID = tempBid;
		this.ACTUAL_BIDDER_NAME = nameTemp;
		this.ACTUAL_BIDDER_ID = idTemp;
	}

	
	private void removeBidderFromBD(int idPlayer){
		String Query = "DELETE FROM zeus_bid_players_bid WHERE id_player_bid = ? AND idObjeto = ?";
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Query);
			ins.setInt(1, idPlayer);
			ins.setInt(2, this.ITEM_ID_OBJECT);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}
	}
	
	
	private void sendEmailLostPlace(String charName, Long newBid){
		String _Title = language.getInstance().getMsg(charName).BH_EMAIL_TITLE_YOUR_LOST_YOUR_BID_PLACE;
		String _Msge = language.getInstance().getMsg(charName).BH_EMAIL_MESSAGE_YOUR_LOST_YOUR_BID_PLACE.replace("$Item", this.ITEM_NAME).replace("$Bid", opera.getFormatNumbers(newBid));
		EmailSend.sendEmail(null, charName, _Title, _Msge, "", tipoMensaje.Bid_house);
	}
	public final Map<Integer, _bidInformation> getAllPlayersBids(){
		return this.BIDS_PLAYER;
	}
	
	private void saveBidInDb(int idBidder, Long Bid, String playerName){
		
		String QueryRemove = "DELETE FROM zeus_bid_players_bid WHERE idObjeto=? AND id_player_bid = ?";
		
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(QueryRemove);
			ins.setInt(1, this.ITEM_INSTANCE.getObjectId());
			ins.setInt(2, idBidder);
			try{
				ins.executeUpdate();
				ins.close();
			}catch(SQLException e){
				_log.warning("Error ZeuS E ->" + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error ZeuS A ->" + a.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){
			_log.warning("Error ZeuS A2 ->" + a.getMessage());
		}		
		
		
		String Query = "INSERT INTO zeus_bid_players_bid( player_bid, idObjeto, id_player_bid, name_player_bid) VALUES(?,?,?,?)";
	
		con = null;
		ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Query);
			ins.setLong(1, Bid);
			ins.setInt(2, this.ITEM_INSTANCE.getObjectId());
			ins.setInt(3, idBidder);
			ins.setString(4, playerName);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error ZeuS E ->" + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error ZeuS A ->" + a.getMessage());
		}
		try{
			con.close();
		}catch(SQLException a){
			_log.warning("Error ZeuS A2 ->" + a.getMessage());
		}		
				
	}
	
	private void saveInBD(){
		
		String Consulta = "update zeus_bid_house set ItemQuantityToSell=? where idObjeto=? and idOwner=?";
		
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setLong(1, this.SELL_QUANTITY_ITEM_TO_SELL);
			ins.setInt(2, this.ITEM_ID_OBJECT );
			ins.setInt(3, this.PLAYER_ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			
		}
		try{
			ins.close();
			con.close();			
		}catch(Exception a){
			
		}
		
		Consulta = "update items set count=? where owner_id=? and object_id=?";
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Consulta);
			ins.setLong(1, this.SELL_QUANTITY_ITEM_TO_SELL);
			ins.setInt(2, v_auction_house.ID_OWNER_AUCTION);
			ins.setInt(3, this.ITEM_ID_OBJECT);
			try{
				ins.executeUpdate();
				ins.close();
				con.close();
			}catch(SQLException e){
				_log.warning("Error ZeuS E->" + e.getMessage());
			}
		}catch(Exception a){
			
		}
		
		
	}
	
	public void RemoveQuantity(Long Quantity){
		this.SELL_QUANTITY_ITEM_TO_SELL = this.SELL_QUANTITY_ITEM_TO_SELL - Quantity;
		saveInBD();
	}
	
	private String ElementalType[] = {"Fire","Water","Earth","Wind","Dark","Holy"};
	
	public final String getIconImgID(){
		return opera.getIconImgFromItem(this.ITEM_INSTANCE.getItem().getId(), true);
	}
	
	public final int getIDItemRequested(){
		return this.SELL_ITEM_REQUEST_ID;
	}
	
	public final String getNameItemRequested(){
		return central.getNombreITEMbyID(this.SELL_ITEM_REQUEST_ID);
	}
	
	public final L2ItemInstance getItemInstance(){
		return this.ITEM_INSTANCE;
	}
	
	public final int getDivineAtributte(){
		try{
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.HOLY);
		}catch(Exception a){
			return 0;
		}
	}
	
	public final boolean isChaosItem(){
		int idItem = this.ITEM_INSTANCE.getItem().getId();
		return opera.isMasterWorkItem(idItem);
	}
	
	public final int getDarkAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.DARK).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.DARK);
		}catch(Exception a){
			return 0;
		}
	}
	
	public final int getFireAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.FIRE).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.FIRE);
		}catch(Exception a){
			return 0;
		}
	}
	
	public final int getWaterAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.WATER).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.WATER);
		}catch(Exception a){
			return 0;
		}
	}
	
	public final int getEarthAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.EARTH).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.EARTH);
		}catch(Exception a){
			return 0;
		}
	}
	public final int getWindAtributte(){
		try{
			//return this.ITEM_INSTANCE.getArmorItem().getElemental(Elementals.WIND).getValue();
			return this.ITEM_INSTANCE.getElementDefAttr(Elementals.WIND);
		}catch(Exception a){
			return 0;
		}
	}
	
	public final String getAtributteElementalType_weapon(){
		return ElementalType[this.ITEM_INSTANCE.getAttackElementType()];
	}
	
	public final int getAtributteElementalPower_weapon(){
		return this.ITEM_INSTANCE.getAttackElementPower();
	}
	
	public final String getElementalTypeFromWeapon(){
		return ElementalType[this.ITEM_INSTANCE.getAttackElementType()];
	}
	
	public final Long getQuantityRequest(){
		return this.SELL_PRICE_ITEM_BY_1;
	}
	public final String getNameItemToRequest(){
		return this.SELL_ITEM_REQUEST_NAME;
	}
	
	public final Long getQuantityItemToSell(){
		return this.SELL_QUANTITY_ITEM_TO_SELL;
	}
	
	public final String getItemName(){
		return this.ITEM_NAME;
	}
	
	public final String getItemGrade(){
		return this.ITEM_GRADE;
	}
	
	public final boolean isPlayerOnline(){
		try{
			return opera.isCharInGame(this.PLAYER_ID);
		}catch(Exception a){
			return false;
		}
	}
	
	public final String getSellerName(){
		return this.PLAYER_NAME;
	}
	
	public final int getItemEnchant(){
		return this.ITEM_ENCHANT;
	}
	
	public final int getPlayerID(){
		return this.PLAYER_ID;
	}
	
	public final int getIdItem(){
		return this.ITEM_INSTANCE.getItem().getId();
	}
	
	public final int getFee(){
		return this.SELL_FEE;
	}
	
	private void getInfo(int idObjectitem){
		String Consulta = "SELECT zeus_bid_house.idObjeto, zeus_bid_house.idOwner, zeus_bid_house.idItemRequest, zeus_bid_house.ItemRequestQuantity, zeus_bid_house.ItemQuantityToSell, zeus_bid_house.startUnix, zeus_bid_house.endUnix, characters.char_name, zeus_bid_house.feed FROM zeus_bid_house INNER JOIN characters ON zeus_bid_house.idOwner = characters.charId WHERE zeus_bid_house.idObjeto = ?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(Consulta))
			{
				statement.setInt(1, idObjectitem);
				try (ResultSet inv = statement.executeQuery())
				{
					if (inv.next())
					{
						this.PLAYER_ID = inv.getInt("idOwner");
						this.PLAYER_NAME = inv.getString("char_name");
						this.SELL_PRICE_ITEM_BY_1 = inv.getLong("ItemRequestQuantity");
						this.SELL_QUANTITY_ITEM_TO_SELL = inv.getLong("ItemQuantityToSell");
						this.SELL_ITEM_REQUEST_ID = inv.getInt("idItemRequest");
						this.SELL_ITEM_REQUEST_NAME = central.getNombreITEMbyID(inv.getInt("idItemRequest"));
						this.SELL_FEE = inv.getInt("feed");
						this.BEGIN_TIME = inv.getInt("startUnix");
						this.FINISH_TIME = inv.getInt("endUnix");
						if(opera.getUnixTimeNow() < this.FINISH_TIME){
							this.IS_ACTIVE = true;
							this.CreateFisnishClock();
						}else{
							this.IS_ACTIVE = false;
							this.CreateFisnishClock(SECOND_SEND_EMAIL_AT_START);
						}
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Erro ZeuS E->" + e.getMessage());
			}
	}
	
	private void CreateFisnishClock(int seconds){
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			@Override
			public void run(){
				checkTheWinner();
			}
		}, seconds * 1000);
	}
	
	private void CreateFisnishClock(){
		int SecondLeft = this.FINISH_TIME - opera.getUnixTimeNow();
		CreateFisnishClock(SecondLeft);
	}
	
	private void getBidInfo(int idObjectitem){
		String Consulta = "SELECT id_player_bid, player_bid, bid_date, name_player_bid FROM zeus_bid_players_bid WHERE idObjeto=?"; 
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(Consulta))
			{
				statement.setInt(1, idObjectitem);
				try (ResultSet inv = statement.executeQuery())
				{
					while (inv.next())
					{
						if(inv.getLong("player_bid") > this.ACTUAL_BID){
							this.ACTUAL_BID = inv.getLong("player_bid");
							this.ACTUAL_BIDDER_NAME = inv.getString("name_player_bid");
							this.ACTUAL_BIDDER_ID = inv.getInt("id_player_bid");							
						}
						_bidInformation tmp = new _bidInformation(inv.getLong("player_bid"), inv.getString("bid_date"), inv.getInt("id_player_bid"), inv.getString("name_player_bid"));
						this.BIDS_PLAYER.put(inv.getInt("id_player_bid"), tmp);
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Erro ZeuS E->" + e.getMessage());
			}
	}	
	
	public final int getRemainingTime(){
		int UnixNow = opera.getUnixTimeNow();
		return this.FINISH_TIME - UnixNow;
	}
	
	public final String getRemainingTimeFormat(){
		int UnixNow = opera.getUnixTimeNow();
		int LeftSeconds = this.FINISH_TIME - UnixNow;
		
		return opera.getTiempoON(LeftSeconds);
	}
	
	public final int getObjectID(){
		return this.ITEM_ID_OBJECT;
	}
	
	public final boolean isActive(){
		return this.IS_ACTIVE;
	}
	
	public InfoBid() {
	}
	public InfoBid(int idObjectItem, L2ItemInstance item){
		this.ITEM_ID_OBJECT = idObjectItem;
		this.ITEM_INSTANCE = item;
		this.ITEM_NAME = item.getName();
		this.ITEM_GRADE = item.getItem().getItemGrade().name(); 
		this.ITEM_ENCHANT = item.getEnchantLevel();
		this.getInfo(idObjectItem);
		this.getBidInfo(idObjectItem);
	}	
}

class _bidInformation{
	private Long BID;
	private String BID_DATE;
	private String BID_BIDDER_NAME;
	private int BID_BIDDER_ID;
	public _bidInformation(Long Bid, String BidDate, int idPlayer, String namePlayer){
		this.BID = Bid;
		this.BID_DATE = BidDate;
		this.BID_BIDDER_ID = idPlayer;
		this.BID_BIDDER_NAME = namePlayer;
	}
	
	public final String getPlayerName(){
		return this.BID_BIDDER_NAME;
	}
	
	public final int getPlayerID(){
		return this.BID_BIDDER_ID;
	}
	public final Long getBid(){
		return this.BID;
	}
	public final String getDate(){
		return this.BID_DATE;
	}
	public void setNewBid(Long Bid){
		this.BID = Bid;
	}
}
class _variableBidHouseCreate{
	private int BID_PAGINE;
	private int BID_ID_OBJECT;
	private Long BID_QUANTITY;
	private int BID_ITEM_REQUESTED;
	private Long BID_MIN_BID;
	private int BID_ACTIVE_BIDDINGS_PAGE;
	private int BID_PLACED_BIDS_PAGE;
	
	private int MAKE_BID_IDOBJECT;
	private Long MAKE_BID_BID;
	private int MAKE_BID_SECTION;
	
	private String LIST_GENERAL_PLAYER_SORT;
	private String LIST_ALL_MY_ITEM_SORT;
	private String LIST_GENERAL_SEARCHING_WORD;
	private String LIST_GENERAL_GRADE;
	private String LIST_GENERAL_ITEM_TYPE;
	private String LIST_GENERAL_ITEM_TYPE_INDEX;
	private String LIST_GENERAL_ALL_BIDS_ACTIVE_DATA;
	private String LIST_GENERAL_ALL_BIDS_PLACED_DATA;
	
	private String PAG_CONTROL;
	private String ITEM_NAME;
	
	private int ID_OBJECT_TO_REMOVE;
	
	private int LAST_SECTION;
	
	private String ALL_LIST_GRADE;
	
	public _variableBidHouseCreate(){
		this.BID_PAGINE = 0;
		this.BID_ID_OBJECT = -1;
		this.BID_QUANTITY = 1l;
		this.BID_ITEM_REQUESTED = -1;
		this.BID_MIN_BID = 1l;
		this.BID_ACTIVE_BIDDINGS_PAGE = 0;
		this.BID_PLACED_BIDS_PAGE = 0;
		this.LIST_GENERAL_PLAYER_SORT = "nameAZ";
		this.LIST_ALL_MY_ITEM_SORT = "nameAZ";
		this.LIST_GENERAL_SEARCHING_WORD = "";
		this.LIST_GENERAL_GRADE = "All";
		this.LIST_GENERAL_ITEM_TYPE = "0";
		this.LIST_GENERAL_ITEM_TYPE_INDEX = "";
		this.PAG_CONTROL = "";
		this.LIST_GENERAL_ALL_BIDS_ACTIVE_DATA = "nameAZ";
		this.LIST_GENERAL_ALL_BIDS_PLACED_DATA = "nameAZ";
		this.ITEM_NAME = "";
		this.LAST_SECTION = 0;
		this.ALL_LIST_GRADE = "All";
	}
	
	public final String getAllListGrade(){
		return this.ALL_LIST_GRADE;
	}
	
	/*public void setPlayerGradeList(String Data){
		this.ALL_LIST_GRADE = Data;
	}
	public final String getPlayerGradeList(){
		if(this.ALL_LIST_GRADE.equals("0")){
			this.ALL_LIST_GRADE = "All";
		}
		return this.ALL_LIST_GRADE;
	}*/	
	
	public void setLastSection(int lastSection){
		this.LAST_SECTION = lastSection;
	}
	public final int getLastSection(){
		return this.LAST_SECTION;
	}
	
	public void setRemoveObjectSetIDObject(int idObject){
		this.ID_OBJECT_TO_REMOVE = idObject;
	}
	public final int getRemoveObjectSetIDObject(){
		return this.ID_OBJECT_TO_REMOVE;
	}
	
	public void setPlayerOrderAllActiveBidsData(String Sort){
		this.LIST_GENERAL_ALL_BIDS_ACTIVE_DATA = Sort;
	}
	public final String getPlayerOrderAllActiveBidsData(){
		return this.LIST_GENERAL_ALL_BIDS_ACTIVE_DATA;
	}
	
	public void setPlayerOrderAllPlacedBidsData(String Sort){
		this.LIST_GENERAL_ALL_BIDS_PLACED_DATA = Sort;
	}
	public final String getPlayerOrderAllPlacedBidsData(){
		return this.LIST_GENERAL_ALL_BIDS_PLACED_DATA;
	}
	
	public void setPlayerPagControl(String ControlData){
		this.PAG_CONTROL = ControlData;
	}
	public final String getPlayerPagControl(){
		return this.PAG_CONTROL;
	}
	
	public void setPlayerSearchingItemTypeIndex(String TypeIndex){
		this.LIST_GENERAL_ITEM_TYPE_INDEX = TypeIndex;
	}
	public final String getPlayerSearchingItemTypeIndex(){
		return this.LIST_GENERAL_ITEM_TYPE_INDEX;
	}	
	
	public void setPlayerSearchingItemType(String Type){
		this.LIST_GENERAL_ITEM_TYPE = Type;
	}
	public final String getPlayerSearchingItemType(){
		return this.LIST_GENERAL_ITEM_TYPE;
	}
	
	public void setPlayerSearchingGrade(String Grade){
		this.LIST_GENERAL_GRADE = Grade;
	}
	
	public final String getPlayerSearchingGrade(){
		return this.LIST_GENERAL_GRADE;
	}
	
	public void setPlayerSearchingWords(String Words){
		this.LIST_GENERAL_SEARCHING_WORD = Words;
	}
	public final String getPlayerSearchingWords(){
		return this.LIST_GENERAL_SEARCHING_WORD;
	}
	
	public void setPlayerSortMyItemList(String Sort){
		this.LIST_ALL_MY_ITEM_SORT = Sort;
	}
	public final String getPlayerSortMyItemList(){
		return this.LIST_ALL_MY_ITEM_SORT;
	}	
	
	public void setPlayerSortGeneralList(String Sort){
		this.LIST_GENERAL_PLAYER_SORT = Sort;
	}
	public final String getPlayerSortGeneralList(){
		return this.LIST_GENERAL_PLAYER_SORT;
	}
	
	public void setActiveBiddingPage(int Page){
		this.BID_ACTIVE_BIDDINGS_PAGE = Page;
	}
	public final int getActiveBiddingPage(){
		return this.BID_ACTIVE_BIDDINGS_PAGE;
	}
	
	public void setPlacedBidsPage(int Page){
		this.BID_PLACED_BIDS_PAGE = Page;
	}
	public final int getPlacedBidsPage(){
		return this.BID_PLACED_BIDS_PAGE;
	}
	
	public void setVariableNewBid( int _pagine, int _idobjec, int _itemRequest, Long _quantity, Long _minBid){
		this.BID_PAGINE = _pagine;
		this.BID_ID_OBJECT = _idobjec;
		this.BID_QUANTITY = _quantity;
		this.BID_ITEM_REQUESTED = _itemRequest;
		this.BID_MIN_BID = _minBid;
		L2Object temp = L2World.getInstance().findObject(_idobjec);
		if(temp instanceof L2ItemInstance){
			L2ItemInstance ItemForName = (L2ItemInstance)temp;
			this.ITEM_NAME = ItemForName.getName();
		}else{
			this.ITEM_NAME = "No Data";
		}
	}
	
	public final String getItemName(){
		return this.ITEM_NAME;
	}
	
	public void setVariableNewOfert(int idObject, Long Mount, int idSeccion){
		this.MAKE_BID_IDOBJECT = idObject;
		this.MAKE_BID_BID = Mount;
		this.MAKE_BID_SECTION = idSeccion;
	}
	
	public final int getCreateBidPagine(){
		return this.BID_PAGINE;
	}
	public final int getCreateItemIdObject(){
		return this.BID_ID_OBJECT;
	}
	public final Long getCreateQuantityToGive(){
		return this.BID_QUANTITY;
	}
	public final int getCreateIdItemRequested(){
		return this.BID_ITEM_REQUESTED;
	}
	public final Long getCreateMinBid(){
		return this.BID_MIN_BID;
	}
	
	public final int getMakeBidIdObject(){
		return this.MAKE_BID_IDOBJECT;
	}
	
	public final Long getMakeBidAmountOffert(){
		return this.MAKE_BID_BID;
	}
	public final int getMakeBidSection(){
		return this.MAKE_BID_SECTION;
	}
}