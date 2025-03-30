package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

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
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.idfactory.IdFactory;
import l2r.gameserver.model.Elementals;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.TradeItem;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.model.items.type.ArmorType;
import l2r.gameserver.model.items.type.ItemType;
import l2r.gameserver.model.items.type.WeaponType;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.gameserver.network.serverpackets.PrivateStoreListBuy;
import l2r.gameserver.network.serverpackets.PrivateStoreListSell;

public class v_auction_house {
	private static final Logger _log = Logger.getLogger(v_auction_house.class.getName());
	
	private static Vector<InfoAuction> ITEMS_ON_AUCTION = new Vector<InfoAuction>(); 
	
	private static int IDS_FROM_SERVER = -2099999999;
	private static Map<Integer, Boolean> PLAYERS_ID_FROM_SERVER = new HashMap<Integer, Boolean>();

	private static Map<Integer, _variableAuctionHouseCreate> PLAYER_VARIABLES = new HashMap<Integer, _variableAuctionHouseCreate>();
	
	static final int ID_OWNER_AUCTION = -300;
	
	private static final int[]ID_DUAL_SWORD = {52,10004,10415,11251,11300,14570,16150,16154,16158,20295};
	
	private static Vector<Integer>VECTOR_ID_DUALSWORD = new Vector<Integer>();
	
	private static Map<Integer, HashMap<Integer, HashMap<String, String>>> SELLOFFLINER= new HashMap<Integer, HashMap<Integer, HashMap<String,String>>>(); 
	
	private static int getIdFromServer(){
		IDS_FROM_SERVER++;
		return IDS_FROM_SERVER; 
	}
	
	private static boolean TypeSearch_addToList(InfoAuction ItemSelected, String tipoBusqueda){
		/***JEWEL_RING
			JEWEL_EARRING
			JEWEL_NECKLACE
			
			ARMOR_HELMET
			
			WEAPON_SWORD
		*/
		
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
						int FeedToBack = ( ReferentePrice * general.AUCTIONSHOUSE_PERCENT_FEED ) / 100 ;
						
						if(FeedToBack > general.AUCTIONSHOUSE_FEED_MASTER){
							return FeedToBack;
						}else{
							return general.AUCTIONSHOUSE_FEED_MASTER + FeedToBack;							
						}
					}
				}
			}catch(Exception a){
				_log.warning("Error ZeuS A ->" + a.getMessage());
				return general.AUCTIONSHOUSE_FEED_MASTER;
			}
		}
		return general.AUCTIONSHOUSE_FEED_MASTER;
	}
	
	private static InfoAuction getInfoAction(int idObject){
		for(InfoAuction T2 : ITEMS_ON_AUCTION){
			if(T2.getObjectID() == idObject){
				return T2;
			}
		}
		return null;
	}
	
	private static void getInfoItemWindows(L2PcInstance player, int idObjecto){
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-auctionhouse-show-item-info.htm");
		
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
		
		InfoAuction p = getInfoAction(idObjecto);
		
		if(p==null){
			central.msgbox(language.getInstance().getMsg(player).AH_ITEM_IS_NOT_FOR_SELL_NOW, player);
			return;
		}
		
		String fontColorName = ( opera.isMasterWorkItem(p.getIdItem()) ? "LEVEL" : "A4FFA9" );
		
		html.replace("%ITEM_ICO%", p.getIconImgID());
		html.replace("%ITEM_NAME_COLOR%", fontColorName);
		html.replace("%ITEM_NAME%", p.getItemName());
		html.replace("%ITEM_ENCHANT%", (p.getItemEnchant()> 0 ? "+"+String.valueOf(p.getItemEnchant()) : "NONE"));
		
		InfoAuction itemSel = getInfoAction(idObjecto);
		
		if(itemSel==null){
			central.msgbox(language.getInstance().getMsg(player).AH_ITEM_IS_NOT_FOR_SELL_NOW, player);
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

		if(ElementalData.length()>0){
			ElementalData = "<table width=257 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2>"+ ElementalData +"</table>";
		}
		
		html.replace("%ELEMENTAL_DATA%", ElementalData);
		html.replace("%QUANTITY_TO_SELL%", opera.getFormatNumbers(itemSel.getQuantityItemToSell()));
		html.replace("%SELLER_NAME%", itemSel.getSellerName());
		html.replace("%REQUESTED_ITEM_TO_SELL_IT%", itemSel.getNameItemToRequest());
		html.replace("%QUANTITY_REQUESTED_ITEM_TO_SELL_IT%", opera.getFormatNumbers(itemSel.getQuantityRequest()));
		html.replace("%ONLINE_COLOR%", (itemSel.isPlayerOnline() ? "59FF85" : "FF5959"));
		html.replace("%IS_ONLINE%", (itemSel.isPlayerOnline() ? "YES" : "NO"));
		
		
		boolean isOwner = false;
		String CancelMyAuction = "";
		if(player.getObjectId() == itemSel.getPlayerID()){
			String BypassCancel = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";cancelauction;" + String.valueOf(idObjecto) + ";0;0;0;0";
			CancelMyAuction += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
            "<button value=\"Cancel Auction\" action=\""+ BypassCancel +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";
			
			isOwner= true;
		}
		
		html.replace("%CANCEL_MY_AUCTION_SYSTEM%", CancelMyAuction);
		
		String OwnerData = "";
		if(!isOwner){
			if(opera.haveItem(player, itemSel.getIDItemRequested(), itemSel.getQuantityRequest(), false)){
				Long cantidad = itemSel.getQuantityItemToSell();
				
				String ByPassBuyItNow = "bypass ZeuS ah_lin BuyItNow " + idObjecto + " $txtQuantity";
				String ByPassBuyItNow_1 = "bypass ZeuS ah_lin BuyItNow " + idObjecto + " 1";
				OwnerData+= "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
	            "<font name=hs12 color=LEVEL>Quantity for Purchase</font>"+ (cantidad==1 ? "<br><font name=hs12>1</font>" : "<edit type=number var=\"txtQuantity\" width=120>") +"<br>"+
				"<button value=\"Buy It Now\" action=\""+ (cantidad==1 ? ByPassBuyItNow_1 : ByPassBuyItNow ) +"\" width=150 height=34 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\"></td></tr></table>";			
				
				String itemHave = opera.getFormatNumbers( String.valueOf(player.getInventory().getItemByItemId( itemSel.getIDItemRequested()).getCount() ) );
				OwnerData += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
				"<font name=hs12 color=FAF991>You item's</font><br>"+
	            "<font name=hs12 color=B8FA91>"+ itemSel.getNameItemToRequest() +"</font><br1>"+
	            "<font name=hs12 color=91DEFA>"+ itemHave +"</font><br>"+
	           "</td></tr></table>";
			}else{
				OwnerData += "<table width=264 background=L2UI_CT1.Windows_DF_TooltipBG cellspacing=2 cellpadding=2 border=0><tr><td fixwidth=264 align=CENTER>"+
	            "<font name=hs12 color=FAF991>You item's</font><br>You dont have the Requested Item</td></tr></table>";
			}
		}
		
		html.replace("%OWNER_DATA%", OwnerData);

		opera.enviarHTML(player, html.getHtml());
	}
	
	
	private static void setCreateNewItemInBD(L2PcInstance player, int IdObject, int itemID, Long Count){
		String Consulta = "insert into items(owner_id, object_id, item_id, count, enchant_level, loc, loc_data) values (?,?,?,?,?,?,?)";
		
		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, ID_OWNER_AUCTION);
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
		
		String Consulta = "INSERT INTO zeus_auctions_house("+
		"idObjeto,idOwner,idItemRequest,ItemRequestQuantity,ItemQuantityToSell,startUnix,feed) VALUES(?,?,?,?,?,?,?)";
		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, idObject);/**/
			ins.setInt(2, player.getObjectId());/**/
			ins.setInt(3, idItemRequest);/**/
			ins.setLong(4, ItemRequestQuantity);
			ins.setLong(5, SellItem_Quantity);
			ins.setFloat(6, opera.getUnixTimeL2JServer());
			ins.setInt(7, FeedIN);
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
		for(InfoAuction T1 : ITEMS_ON_AUCTION){
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
				central.msgbox(language.getInstance().getMsg(player).AH_ITEM_IS_EQUIPPED.replace("$item", item.getName()), player);
				return;
			}
			
			if(item.getOwnerId() != player.getObjectId()){
				central.msgbox(language.getInstance().getMsg(player).AH_TRY_TO_CHEAT, player);
				return;
			}
			
			int FeedRequested = getFeedFromItem(idObject);
			
			if(!opera.haveItem(player, 57, FeedRequested )){
				central.msgbox(language.getInstance().getMsg(player).AH_NO_HAVE_FEED.replace("$feed",String.valueOf(FeedRequested)), player);
				return;
			}
			
			
			try{
				L2ItemInstance itemW = player.getWarehouse().getItemByObjectId(idObject);
				if(itemW!=null){
					central.msgbox(language.getInstance().getMsg(player).AH_TRY_TO_CHEAT, player);
					return;					
				}
			}catch(Exception a){
				
			}
			
			try{
				L2ItemInstance itemIn = player.getInventory().getItemByObjectId(idObject);
				if(itemIn==null){
					central.msgbox(language.getInstance().getMsg(player).AH_TRY_TO_CHEAT, player);
					return;					
				}
			}catch(Exception a){
				
			}
			
			
			if(!item.isStackable() && Cantidad_a_Vender>1){
				central.msgbox(language.getInstance().getMsg(player).AH_NO_STACKABLE_ITEM_CHANGE_TO_ONE.replace("$item",item.getName()), player);
				Cantidad_a_Vender = 1L;
			}
			
			
			if(haveItemAlready(player,item.getItem().getId())){
				central.msgbox(language.getInstance().getMsg(player).AH_CANCEL_THE_ITEM_TO_SELL_AGAIN, player);
				return;
			}
			
			
			int IdObjectNew = idObject;
			
			int FeedIN = getFeedFromItem(idObject);
			
			if(Cantidad_a_Vender > item.getCount()){
				central.msgbox(language.getInstance().getMsg(player).AH_CREATE_PROCESS_FAIL_CHECK_QUANTITY, player);
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
				item.setOwnerId(ID_OWNER_AUCTION);
				item.updateDatabase(true);
				player.destroyItemWithoutTrace("Auction_house", item.getObjectId(), 1, null, false);
			}
			player.sendPacket(ActionFailed.STATIC_PACKET);
			player.broadcastStatusUpdate();
			central.msgbox("Auction created!", player);
			getAllItem(false,player,idObjeToS);
			opera.removeItem(57, FeedRequested, player);
			wishList.AnnounceItem(item.getId(), "Auction House", player.getName());
		}else{
			central.msgbox("Auctions create process fail. Please Check.", player);
		}
	}
	//itemId + ", cantidad->" + cnt + ", precio->" + price
	public static void setNewSellBuyFromServer(L2PcInstance player, int idItem, long Quantity, long Price, boolean isSell, int enchant){
		InfoAuction temp = new InfoAuction(player, idItem, Price, Quantity, isSell, enchant, getIdFromServer());
		ITEMS_ON_AUCTION.add(temp);
		PLAYERS_ID_FROM_SERVER.put(player.getObjectId(), true);
		if(isSell && !player.getClient().isDetached()) {
			L2ItemInstance tempItem = player.getInventory().getItemByObjectId(idItem);
			wishList.AnnounceItem(tempItem.getId(), "Auction House (Private Store)", player.getName());
		}
	} 
	
	public static void setCancelAuctionFromServer(L2PcInstance player){
		if(PLAYERS_ID_FROM_SERVER==null){
			return;
		}
		if(PLAYERS_ID_FROM_SERVER.size()==0){
			return;
		}
		if(!PLAYERS_ID_FROM_SERVER.containsKey(player.getObjectId())){
			return;
		}
		Vector<InfoAuction> toRemove = new Vector<InfoAuction>();
		for(InfoAuction tmp : ITEMS_ON_AUCTION){
			if(!tmp.isFromZeuS() && (tmp.getPlayerID() == player.getObjectId())){
				toRemove.add(tmp);
			}
		}
		if(toRemove!=null){
			if(toRemove.size()>0){
				for(InfoAuction tmp : toRemove){
					ITEMS_ON_AUCTION.remove(tmp);
				}
			}
		}
		PLAYERS_ID_FROM_SERVER.remove(player.getObjectId());
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
		
		String ByPass_Arriba_Top = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;0;"+ String.valueOf(idObjectItem) +";0;0;0";
		String ByPass_Arriba_1 = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;" + String.valueOf(  pagina>0 ? pagina - 1 : 0    ) + ";"+ String.valueOf(idObjectItem) +";0;0;0";
		String ByPass_Abajo_Top = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;%PAGLAST%;"+ String.valueOf(idObjectItem) +";0;0;0";
		String ByPass_Abajo_1 = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;%PAGNEXT%;"+ String.valueOf(idObjectItem) +";0;0;0";
		html.replace("%BYPASS_AUCTION_UP_ALL%", ByPass_Arriba_Top);
		html.replace("%BYPASS_AUCTION_UP_ONE%", ByPass_Arriba_1);
		html.replace("%BYPASS_AUCTION_DOWN_ONE%", ByPass_Abajo_1);
		html.replace("%BYPASS_AUCTION_DOWN_ALL%", ByPass_Abajo_Top);
		
		int indexBotton = (getTotalInventario(player) / 5);
		indexBotton = ( (indexBotton%5)==0 ? indexBotton-3 : indexBotton-2 );
		int ContadorColumnas=0;
		boolean Continua = true;
		String ItemByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";0;" + String.valueOf(pagina) + ";%IDOBJETO%;0;0;0";
		
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
				html.replace("%MY_AUCTION_MY_INV_ITEM_"+ String.valueOf(RowCounter) +"%", cbFormato.getBotonForm(opera.getIconImgFromItem(items.getItem().getId(), true), ItemByPass.replace("%IDOBJETO%", String.valueOf(items.getObjectId()))));
				RowCounter++;
				if(Continua){
					if(ContadorColumnas == 2){
						Continua=false;
					}
				}
			}
			ContadorElementos++;
		}

		if(RowCounter<15){
			for(int i=RowCounter; i<=15; i++){
				html.replace("%MY_AUCTION_MY_INV_ITEM_"+ String.valueOf(i) +"%", cbFormato.getBotonForm("etc_alphabet_5_i00",""));
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

			html.replace("%MY_AUCTION_SELECTED_ITEM_ICON%", opera.getIconImgFromItem(item.getItem().getId() , true));
			html.replace("%MY_AUCTION_SELECTED_ITEM_QUANTITY%", opera.getFormatNumbers(Quantity));
			html.replace("%MY_AUCTION_SELECTED_ITEM_NAME_AND_ENCHANT%", item.getName() + ( getEnchant>0 ? " (+" + String.valueOf(getEnchant) + ")" : "" ));
		}else{
			html.replace("%MY_AUCTION_SELECTED_ITEM_ICON%", "");
			html.replace("%MY_AUCTION_SELECTED_ITEM_QUANTITY%", "0");
			html.replace("%MY_AUCTION_SELECTED_ITEM_NAME_AND_ENCHANT%", "");			
		}		
		
		return html;
	}
	
	
	private static int getIdFromItemRequest(String NombreItem){
		if(general.AUCTIONSHOUSE_ITEM_REQUEST.size()>0){
			for(String t : general.AUCTIONSHOUSE_ITEM_REQUEST){
				if(t.split(":")[1].equalsIgnoreCase(NombreItem)){
					return Integer.valueOf(t.split(":")[0]);
				}
			}
		}
		return 57;
	}
	
	private static String getCmbItemRequest(){
		//ID:NOMBRE_ITEM
		String Retorno = "";
		if(general.AUCTIONSHOUSE_ITEM_REQUEST.size()>0){
			for(String t : general.AUCTIONSHOUSE_ITEM_REQUEST){
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
		String ByPassCreateAuction = "bypass ZeuS ah_lin createAsk " + String.valueOf(Pagina) + " " + String.valueOf(idObjeto) + " $txtQuantityToSell $cmbItemRequest $txtPrice";
		String comboList = getCmbItemRequest();
		
		html.replace("%MY_AUCTION_ITEM_REQUESTED_LIST%", comboList);
		html.replace("%MY_AUCTION_CREATE_NEW_AUCTION_BYPASS%", ByPassCreateAuction);
		return html;
	}
	
	private static NpcHtmlMessage getFeeAndYouAdena(L2PcInstance player, int IdObjectItem, NpcHtmlMessage html){
		int Feed = getFeedFromItem(IdObjectItem);
		String YouAdena = opera.getFormatNumbers(String.valueOf(player.getInventory().getAdena()));
		html.replace("%MY_AUCTION_MY_ADENA_FEE%", opera.getFormatNumbers(Feed));
		html.replace("%MY_AUCTION_MY_ADENA%", YouAdena);
		return html;
	}

	public static void setSortedData(L2PcInstance player, String Sort, boolean isGeneral){
		if(isGeneral){
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSortGeneralList(Sort);
		}else{
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSortMyList(Sort);
		}
 	}
	
	private static Vector<InfoAuction> SortedData(L2PcInstance player){
		switch( PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSortGeneralList() ){
			case "nameAZ":
				Comparator<InfoAuction> T1 = (p1,p2) -> p1.getItemName().compareToIgnoreCase(p2.getItemName());
				Collections.sort(ITEMS_ON_AUCTION,T1);
				break;
			case "nameZA":
				Comparator<InfoAuction> T2 = (p1,p2) -> p2.getItemName().compareToIgnoreCase(p1.getItemName());
				Collections.sort(ITEMS_ON_AUCTION,T2);
				break;
			case "gradeAZ":
				Comparator<InfoAuction> T3 = (p1,p2) -> p1.getItemGrade().compareToIgnoreCase(p2.getItemGrade());
				Collections.sort(ITEMS_ON_AUCTION,T3);
				break;
			case "gradeZA":
				Comparator<InfoAuction> T4 = (p1,p2) -> p2.getItemGrade().compareToIgnoreCase(p1.getItemGrade());
				Collections.sort(ITEMS_ON_AUCTION,T4);
				break;
			case "quantityAZ":
				Comparator<InfoAuction> T5 = (p1,p2) -> Long.compare(p1.getQuantityItemToSell(), p2.getQuantityItemToSell());
				Collections.sort(ITEMS_ON_AUCTION,T5);
				break;
			case "quantityZA":
				Comparator<InfoAuction> T6 = (p1,p2) -> Long.compare(p2.getQuantityItemToSell(), p1.getQuantityItemToSell());				
				Collections.sort(ITEMS_ON_AUCTION,T6);
				break;
			case "itemrequestAZ":
				Comparator<InfoAuction> T7 = (p1,p2) -> p1.getNameItemRequested().compareToIgnoreCase(p2.getNameItemRequested());
				Collections.sort(ITEMS_ON_AUCTION,T7);
				break;
			case "itemrequestZA":
				Comparator<InfoAuction> T8 = (p1,p2) -> p2.getNameItemRequested().compareToIgnoreCase(p1.getNameItemRequested());				
				Collections.sort(ITEMS_ON_AUCTION,T8);
				break;
			case "priceAZ":
				Comparator<InfoAuction> T9 = (p1,p2) -> Long.compare(p1.getQuantityRequest(), p2.getQuantityRequest());
				Collections.sort(ITEMS_ON_AUCTION,T9);
				break;
			case "priceZA":
				Comparator<InfoAuction> T10 = (p1,p2) -> Long.compare(p2.getQuantityRequest(), p1.getQuantityRequest());				
				Collections.sort(ITEMS_ON_AUCTION,T10);
				break;
		}
		return ITEMS_ON_AUCTION;
	}
	
	private static NpcHtmlMessage getMyAuctionList(L2PcInstance player, int PaginaMisVentas, int PaginaMisItemInventario, int idObjecto, NpcHtmlMessage html){
		
		String ByPassItemToSell = "bypass ZeuS ah_sort_personal nameAZ";
		String ByPassGradeToSell = "bypass ZeuS ah_sort_personal gradeAZ";
		String ByPassQuantityToSell = "bypass ZeuS ah_sort_personal quantityAZ";
		String ByPassItemRequestToSell = "bypass ZeuS ah_sort_personal itemrequestAZ";
		String ByPassSalePrice = "bypass ZeuS ah_sort_personal priceAZ";
		
		switch(PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSortMyList()){
			case "nameAZ":
				ByPassItemToSell = "bypass ZeuS ah_sort_personal nameZA";
				break;
			case "gradeAZ":
				ByPassGradeToSell = "bypass ZeuS ah_sort_personal gradeZA";
				break;
			case "quantityAZ":
				ByPassQuantityToSell = "bypass ZeuS ah_sort_personal quantityZA";
				break;
			case "itemrequestAZ":
				ByPassItemRequestToSell = "bypass ZeuS ah_sort_personal itemrequestZA";
				break;
			case "priceAZ":
				ByPassSalePrice = "bypass ZeuS ah_sort_personal priceZA";
				break;
		}
		
		html.replace("%MY_AUCTION_SORT_BY_ITEM_NAME%", ByPassItemToSell );
		html.replace("%MY_AUCTION_SORT_BY_GRADE%", ByPassGradeToSell);
		html.replace("%MY_AUCTION_SORT_BY_QUANTITY%", ByPassQuantityToSell);
		html.replace("%MY_AUCTION_SORT_BY_ITEM_REQUESTED%", ByPassItemRequestToSell);
		html.replace("%MY_AUCTION_SORT_BY_ITEM_PRICE%", ByPassSalePrice);
		
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
		
		String BypassItemSee = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";showItemWindows;%IDOBJECTO%;0;0;0;0";
		
		int Limite = 6;
		
		int desde = Limite * PaginaMisVentas;
		int hasta = desde + Limite;
		boolean havenext = false;
		

		for(InfoAuction p : SortedData(player)){
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
					
					MyItemList += GridItemFormat.replace("%MY_AUCTION_MY_ITEM_LIST_SALE_PRICE%", opera.getFormatNumbers(p.getQuantityRequest())) .replace("%MY_AUCTION_MY_ITEM_LIST_REQUEST_ITEM_NAME%", p.getNameItemToRequest()) .replace("%MY_AUCTION_MY_ITEM_LIST_QUANTITY%", opera.getFormatNumbers(p.getQuantityItemToSell())).replace("%MY_AUCTION_MY_ITEM_LIST_GRADE%", Grados).replace("%MY_AUCTION_MY_ITEM_LIST_ITEM_NAME%", Enchant + " " + NombreItem) .replace("%MY_AUCTION_MY_ITEM_LIST_BTN_ITEM%", cbFormato.getBotonForm(opera.getIconImgFromItem(p.getIdItem() , true), BypassItemSee.replace("%IDOBJECTO%", String.valueOf(idObject)))).replace("%MY_AUCTION_MY_ITEM_LIST_GRID_COLOR%", ColorBG[contador%2]);
					
				}else if(contador >= hasta){
					havenext = true;
				}
				contador ++;
			}
		}
		
		String PagControl = opera.getGridFormatFromHtml(html, 2, "%PAG_CONTROL%");
		
		String strPagControl = "";
		
		String bypassAntes = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";"+ Engine.enumBypass.AuctionHouse.name() +";0;"+ String.valueOf(PaginaMisItemInventario) +";"+ String.valueOf(idObjecto) +";"+ String.valueOf(PaginaMisVentas - 1) +";0;0"; 
		String bypassProx = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC()+";"+ Engine.enumBypass.AuctionHouse.name() +";0;"+ String.valueOf(PaginaMisItemInventario) +";"+ String.valueOf(idObjecto) +";"+ String.valueOf(PaginaMisVentas + 1) +";0;0";
		strPagControl = PagControl.replace("%MY_ACTION_PAGINE_CONTROL_ACTUAL_PAGINE%", String.valueOf((PaginaMisVentas + 1))) .replace("%MY_ACTION_PAGINE_CONTROL_LEFT%", havenext ? bypassProx : "").replace("%MY_ACTION_PAGINE_CONTROL_RIGHT%", (PaginaMisVentas>=1 ? bypassAntes : ""));
				
		html.replace("%MY_ITEMS_LIST%", MyItemList);
		html.replace("%PAG_CONTROL%", strPagControl);
		
		return html;
	}
	
	private static void removeAuctionFromBD(L2PcInstance player, int IdObject, boolean fromItemTable, int IdPlayerOwner){
		String consulta = "delete from zeus_auctions_house where idObjeto=? and idOwner=?";
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
					statementt.setInt(1, ID_OWNER_AUCTION);
					statementt.setInt(2, IdObject);
					statementt.execute();
				}
				catch (Exception e)
				{
					
				}
		}
	}

	private static InfoAuction getAuctionInformationIsExist(int idObject){
		for(InfoAuction T1 : ITEMS_ON_AUCTION){
			if(T1.getObjectID() == idObject){
				return T1;
			}
		}
		return null;
	}
	
	private static boolean buyitProcess(L2PcInstance player, int idObject, Long Quantity){
		if(ITEMS_ON_AUCTION != null){
			InfoAuction ItemAuc = getAuctionInformationIsExist(idObject);
			if(ItemAuc!=null){
				int idItemRequeridos = ItemAuc.getIDItemRequested();
				Long CantidadRequeridos = ItemAuc.getQuantityRequest();
				Long CantidadAVender = ItemAuc.getQuantityItemToSell();
				long TotalRequestItem = Quantity * CantidadRequeridos;
				if(opera.haveItem(player, idItemRequeridos, TotalRequestItem)){
					if(!ItemAuc.getItemInstance().isStackable()){
						opera.removeItem(idItemRequeridos, CantidadRequeridos, player);
						L2ItemInstance ItemToGive = ItemAuc.getItemInstance();
						player.getInventory().addItem("Auction House", ItemToGive, player, null);
						sendEmailToOwner(ItemAuc, Quantity);
						removeAuctionFromBD(player, idObject,true,ItemAuc.getPlayerID());
						ITEMS_ON_AUCTION.remove(ItemAuc);
						return true;
					}else{
						if(CantidadAVender >= Quantity){
							opera.removeItem( idItemRequeridos, TotalRequestItem, player);
							opera.giveReward(player, ItemAuc.getItemInstance().getItem().getId(), Quantity);
							if(Quantity!=CantidadAVender){
								ItemAuc.RemoveQuantity((long) Quantity);
							}else{
								removeAuctionFromBD(player, idObject,true,ItemAuc.getPlayerID());
								ITEMS_ON_AUCTION.remove(ItemAuc);
							}
						}else{
							central.msgbox(language.getInstance().getMsg(player).AH_WROG_QUANTITY_TO_BUY, player);
							return false;
						}
						sendEmailToOwner(ItemAuc,Quantity);
						return true;
					}
				}
			}else{
				central.msgbox(language.getInstance().getMsg(player).AH_ITEM_IS_NOT_FOR_SELL_NOW, player);
				return false;
			}
		}
		
		return false;
	}
	
	private static boolean removeAuction(L2PcInstance player, int idObject){
		InfoAuction p = getAuctionInformationIsExist(idObject); //AUCTION_ITEM.get(idObject);
		int feePay = p.getFee();
		long Cantidad = p.getQuantityItemToSell();
		int idItem = p.getIdItem();
		L2ItemInstance item = p.getItemInstance();
		
		if(item.isStackable()){
			opera.giveReward(player, idItem, Cantidad);
			opera.giveReward(player, 57, feePay);
			removeAuctionFromBD(player, idObject,true,player.getObjectId());
		}else if(!item.isStackable()){
			player.getInventory().addItem("Auction House", item, player, null);
			opera.giveReward(player, 57, feePay);
			removeAuctionFromBD(player, idObject,false,player.getObjectId());
		}
		try{
			//AUCTION_ITEM.remove(idObject);
			ITEMS_ON_AUCTION.remove(p);
		}catch(Exception a){
			return false;
		}
		
		String Actualizar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + "0;0;0;0;0;0;0";
		cbManager.separateAndSend(bypass(player, Actualizar),player);
		return true;
		
	}
	
	private static String getMainWindows_List(L2PcInstance player, int Pagina, String _Grado, String Tipo, String Busqueda){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-auction-house-all-auction.htm");
		String ByPassList = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0;0";
		String ByPassMy = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.AuctionHouse.name() + ";0;0;0;0;0;0;0";
		html.replace("%ALL_AUCTION_LIST_BYPASS%", ByPassList);
		html.replace("%MY_AUCTION_LIST%", ByPassMy);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		
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
		
		String GradosLista = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerGradeList();
		String GradoAqui = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerGradeList();
		
		for (String GR : GradosV){
			if(!GR.equalsIgnoreCase(GradoAqui)){
				GradosLista += ";" + GR;
			}
		}
		
		String BypassBusGrado = "bypass ZeuS ah_lin gradelist $cmbItemRequest " + Tipo + " " + (Busqueda.equals("0") ? "-1" : Busqueda);
		String BypassBusWord = "bypass ZeuS ah_lin searchword $cmbItemRequest " + Tipo + " $txtWord";
		String BypassRefresh = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() +";1;0;0;0;0;0";
		
		html.replace("%ALL_AUCTION_GRADE_LIST%", GradosLista);
		html.replace("%ALL_AUCTION_APPLY_GRADE_BYPASS%", BypassBusGrado);
		html.replace("%ALL_AUCTION_SEARCH_WORD_BYPASS%", BypassBusWord);
		html.replace("%ALL_AUCTION_REFRESH_BYPASS%", BypassRefresh);

		html = auction_Search_getTypes(player, Tipo, GradoAqui, html);
		html = auction_Search_getAllAuction(player, Tipo, GradoAqui, Busqueda, Pagina, html);
		
		return html.getHtml();
	}
	
	
	private static String getMainWindows(L2PcInstance player, int Pagina, int idObjectItem, int paginaMisItemAuction){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-auction-house-my-auction.htm");
		String ByPassList = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0;0";
		String ByPassMy = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.AuctionHouse.name() + ";0;0;0;0;0;0;0";
	
		html.replace("%ALL_AUCTION_LIST_BYPASS%", ByPassList);
		html.replace("%MY_AUCTION_LIST%", ByPassMy);		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		
		html = getInventaryWindows(player,Pagina,idObjectItem,html);
		html = getSelectedItemToAuction(player,idObjectItem, html);
		html = getSellBox(player,Pagina,idObjectItem, html);
		html = getFeeAndYouAdena(player, idObjectItem, html);
		html = getMyAuctionList(player,paginaMisItemAuction,Pagina,idObjectItem, html);
		return html.getHtml();
	}
	
	
	public static void getAllItem(){
		getAllItem(true, null, 0);
	}
	
	
	public static void getAllItem(boolean isAutomatic, L2PcInstance player, int idItem){
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement("SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time, owner_id FROM items WHERE owner_id=? ORDER BY loc_data"))
			{
				statement.setInt(1, ID_OWNER_AUCTION);
				try (ResultSet inv = statement.executeQuery())
				{
					L2ItemInstance item;
					while (inv.next())
					{
						if(isAutomatic || (!isAutomatic && idItem == inv.getInt(1))){
							item = L2ItemInstance.restoreFromDb(ID_OWNER_AUCTION, inv);
							if (item == null)
							{
								continue;
							}
							try{
								L2World.getInstance().removeObject(item);
							}catch(Exception a){
							}
							
							L2World.getInstance().storeObject(item);
							
							InfoAuction V = new InfoAuction(item.getObjectId(),item, true, true);
							
							if(ITEMS_ON_AUCTION!=null){
								InfoAuction T1 = getAuctionInformationIsExist(item.getObjectId());
								if(T1 != null){
									ITEMS_ON_AUCTION.remove(V);
								}
							}
							ITEMS_ON_AUCTION.add(V);
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
		if(general.AUCTIONSHOUSE_ONLY_IN_PEACE_ZONE){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				central.msgbox(language.getInstance().getMsg(player).AH_ONLY_IN_PEACE_ZONE , player);
				return Comunidad.getComunidad(player, "", 0);
			}
		}
		String ByPassList = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.AuctionHouse.name() + ";1;0;0;0;0;0;0";
		String ByPassMy = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + enumBypass.AuctionHouse.name() + ";0;0;0;0;0;0;0";
		
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/communityboard/engine-auction-house-all-auction.htm");
		
		html.replace("%ALL_AUCTION_LIST_BYPASS%", ByPassList);
		html.replace("%MY_AUCTION_LIST%", ByPassMy);
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		
		String retorno = "";
		
		if(Params.split(";").length>1){
			if(Params.split(";")[2].equals("0")){
				retorno += getMainWindows(player,PaginaInventario,idObjectItem, PaginaMisItem);
			}else if(Params.split(";")[2].equals("1")){
				String Grado = Params.split(";")[3];
				String Tipo = Params.split(";")[4];
				String Pagina = Params.split(";")[5];
				String Busqueda = Params.split(";")[6];
				retorno += getMainWindows_List(player, Integer.valueOf(Pagina), Grado, Tipo, Busqueda);
			}
		}else{
			retorno += getMainWindows(player,PaginaInventario,idObjectItem, PaginaMisItem);
		}

		return retorno;
	}
	
	private static int getIdItemFromItemRequest(String ItemRequest){
		return getIdFromItemRequest(ItemRequest);
	}
	
	public static void setCreationAuction(L2PcInstance player){
		_variableAuctionHouseCreate temp = PLAYER_VARIABLES.get(player.getObjectId());
		createAuctionWithItem(player, temp.getCreateItemIdObject(), temp.getCreateIdItemRequested(), temp.getCreateQuantityToGive(), temp.getCreatePrice());
		String htmlCB = mainHtml(player,"",0,0,0);
		cbManager.separateAndSend(htmlCB, player);		
	}	
	
	//public static void voiceByPass(L2PcInstance player, String params){
	public static void _ByPass(L2PcInstance player, String params){
		
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
			Long CantidadComprar = Long.valueOf(cm[2]);
			if(buyitProcess(player, idItem, CantidadComprar)){
				String Retornar = bypass(player, "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() +";1;0;0;0;0;0");
				cbManager.separateAndSend(Retornar, player);
			}
		}else if(cm[0].equals("createAsk")){
			int idObjeto = Integer.valueOf(cm[2]);
			long Cantidad_A_Vender = Long.valueOf(cm[3]);
			int TipoMonedaPide = getIdItemFromItemRequest(cm[4]);
			Long ValorPorCadaItemA_Vender = Long.parseLong(cm[5]);
			L2ItemInstance itemTemporal = player.getInventory().getItemByObjectId(idObjeto);
			PLAYER_VARIABLES.get(player.getObjectId()).setVariableNewAuction(idObjeto,TipoMonedaPide,Cantidad_A_Vender, ValorPorCadaItemA_Vender);
			String Msje = language.getInstance().getMsg(player).AH_QUESTION_YOU_WANT_CREATE_THIS_AUCTION.replace("$ItemRequest", central.getNombreITEMbyID(TipoMonedaPide)).replace("$QuantityRequest", opera.getFormatNumbers(ValorPorCadaItemA_Vender)) .replace("$ItemNameSell", itemTemporal.getName()) .replace("$QuantitySell", opera.getFormatNumbers(Cantidad_A_Vender));
			Dlg.sendDlg(player, Msje, IdDialog.ENGINE_AUCTION_HOUSE_CREATE_NEW_AUCTION, 80);
		}else if(cm[0].equals("create")){
			int Pagina = Integer.valueOf(cm[1]);
			int idObjeto = Integer.valueOf(cm[2]);
			long Cantidad_A_Vender = Long.valueOf(cm[3]);
			int TipoMonedaPide = getIdItemFromItemRequest(cm[4]);
			Long ValorPorCadaItemA_Vender = Long.parseLong(cm[5]);
			createAuctionWithItem(player, idObjeto, TipoMonedaPide, Cantidad_A_Vender, ValorPorCadaItemA_Vender );
			String htmlCB = mainHtml(player,"",Pagina,0,0);
			cbManager.separateAndSend(htmlCB, player);
		}else if(cm[0].equals("gradelist")){
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerGradeList(cm[1]);
			String TipoMenu = cm[2];
			String ByPassEnviar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +  ";" + Engine.enumBypass.AuctionHouse.name() + ";1;"+ cm[1] +";"+ TipoMenu +";0;0;0";
			String retorno = bypass(player, ByPassEnviar);
			cbManager.separateAndSend(retorno, player);
		}else if(cm[0].equals("searchword")){
			String Grado_ = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerGradeList();
			String _BString = "";
			for (int cont=3 ; cont <= cm.length - 1 ; cont++ ){
				_BString += " " + cm[cont];
			}
			_BString =  _BString.trim(); 
			PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingWord(_BString);
			String TipoMenu = cm[2];
			String ByPassEnviar = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() +  ";" + Engine.enumBypass.AuctionHouse.name() + ";1;"+ Grado_ +";"+ TipoMenu +";0;"+ _BString +";0";
			String retorno = bypass(player, ByPassEnviar);
			cbManager.separateAndSend(retorno, player);
		}
	}
	
	public static String getBypassCreate(L2PcInstance player){
		_variableAuctionHouseCreate _Var = PLAYER_VARIABLES.get(player.getObjectId());
		String GradeIN = _Var.getPlayerGradeList();
		String _strBuscar = _Var.getPlayerSearchingWord();
		String tipo = _Var.getPlayerSearchingTypeItem();
		String retorno = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";0;" + _strBuscar + ";0";
		return retorno;
	}
	
	private static NpcHtmlMessage auction_Search_getAllAuction(L2PcInstance player, String tipo, String _Grade_, String Buscar, int Pagina, NpcHtmlMessage html){
		String ByPassItemToSell = "bypass ZeuS ah_sort nameAZ";
		String ByPassGradeToSell = "bypass ZeuS ah_sort gradeAZ";
		String ByPassQuantityToSell = "bypass ZeuS ah_sort quantityAZ";
		String ByPassItemRequestToSell = "bypass ZeuS ah_sort itemrequestAZ";
		String ByPassSalePrice = "bypass ZeuS ah_sort priceAZ";
		
		switch( PLAYER_VARIABLES.get(player.getObjectId()).getPlayerSortGeneralList()){
			case "nameAZ":
				ByPassItemToSell = "bypass ZeuS ah_sort nameZA";
				break;
			case "gradeAZ":
				ByPassGradeToSell = "bypass ZeuS ah_sort gradeZA";
				break;
			case "quantityAZ":
				ByPassQuantityToSell = "bypass ZeuS ah_sort quantityZA";
				break;
			case "itemrequestAZ":
				ByPassItemRequestToSell = "bypass ZeuS ah_sort itemrequestZA";
				break;
			case "priceAZ":
				ByPassSalePrice = "bypass ZeuS ah_sort priceZA";
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
				
		html.replace("%ALL_AUCTION_SORT_BY_ITEM_NAME%", ByPassItemToSell);
		html.replace("%ALL_AUCTION_SORT_BY_GRADE%", ByPassGradeToSell);
		html.replace("%ALL_AUCTION_SORT_BY_QUANTITY%", ByPassQuantityToSell);
		html.replace("%ALL_AUCTION_SORT_ITEM_REQUESTED%", ByPassItemRequestToSell);
		html.replace("%ALL_AUCTION_SORT_ITEM_PRICE%", ByPassSalePrice);

		
		
		String AllItemGridFormat = opera.getGridFormatFromHtml(html, 4, "%ALL_FIND_ITEM%");
		String AllItemGrid = "";
		
		
		String []BGColor = {"031C1C","011313"};
		
		int Contador=0;
		int MaximoLista = 7;
		boolean haveNext = false;
		int desde = MaximoLista * Pagina;
		int hasta = desde + MaximoLista;

		_variableAuctionHouseCreate _Var = PLAYER_VARIABLES.get(player.getObjectId());
		
		String _strBuscar = _Var.getPlayerSearchingWord();
		String tipoIndex = _Var.getPlayerSearchingTypeItemIndex();
		String GradeIN = _Var.getPlayerGradeList();		

		String ItemByPass = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";showItemWindows;%IDOBJETO%;0;0;0;0";

		Vector<InfoAuction> ToRemove = new Vector<InfoAuction>();
		
		for( InfoAuction p : SortedData(player)){
			if(haveNext){
				break;
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
			
			//PalabraBuscar
			
			if(!p.checkItem()){
				ToRemove.add(p);
				continue;
			}
			
			if(Grabar){
				String Grados = "";
				Grados = GradeItem.get(p.getItemGrade());
				if(Grados==null){
					Grados="";
				}else if(Grados.isEmpty()){
					Grados = "";
				}
				
				
				if(Contador >= desde && Contador <hasta){
					Integer[]Atri = new Integer[6];
					
					Atri[5] = p.getDivineAtributte();
					Atri[4] = p.getDarkAtributte();
					Atri[2] = p.getEarthAtributte();
					Atri[3] = p.getWindAtributte();
					Atri[1] = p.getWaterAtributte();
					Atri[0] = p.getFireAtributte();
					
					if(p.getItemInstance()!=null){
						if(p.getItemInstance().isWeapon()){
							if(p.getItemInstance().getAttackElementType()>=0){
								Atri[p.getItemInstance().getAttackElementType()] = p.getItemInstance().getAttackElementPower();
							}
						}
					}
					
					
                    String A_Fire = Atri[0]>0 ? "<font color=AB3737>Fire:"+ String.valueOf(Atri[0]) +"</font> ": "";
                    String A_Water = Atri[1]>0 ? "<font color=4965CB>Water:"+ String.valueOf(Atri[1]) +"</font> ": "";
                    String A_Earth = Atri[2]>0 ? "<font color=A9803F>Earth:"+ String.valueOf(Atri[2]) +"</font> ": "";
                    String A_Wind = Atri[3]>0 ? "<font color=4C9961>Wind:"+ String.valueOf(Atri[3]) +"</font> ": "";
                    String A_Dark = Atri[4]>0 ? "<font color=8E3FAD>Dark:"+ String.valueOf(Atri[4]) +"</font> ": "";
                    String A_Divine = Atri[5]>0 ? "<font color=808080>Divine:"+ String.valueOf(Atri[5]) +"</font> ": "";
					
                    String ElementalItem = (A_Fire + A_Water + A_Earth + A_Wind + A_Dark + A_Divine).trim();

					String Nombre = (p.isSell() ? "(SELL) " : "(BUY) ") +   (p.getItemEnchant()>0 ? "<font color=5D6C5E>+"+ String.valueOf( p.getItemEnchant() ) + "</font> " : "" ) + ( opera.isMasterWorkItem(p.getIdItem()) ? "<font color=LEVEL>" +  p.getItemName() + "</font>" : p.getItemName());
					String idObjectLink = ( !p.isFromZeuS() ? String.valueOf(p.getObjectID()) : String.valueOf(p.getItemInstance().getObjectId()));
					Nombre += "<br1>" + ElementalItem;
					AllItemGrid += AllItemGridFormat.replace("%ALL_AUCTION_FIND_ITEM_PRICE%", opera.getFormatNumbers(p.getQuantityRequest()))
							.replace("%ALL_AUCTION_FIND_ITEM_REQUESTED%", p.getNameItemToRequest())
							.replace("%ALL_AUCTION_FIND_ITEM_QUANTITY_TO_SELL%", opera.getFormatNumbers(p.getQuantityItemToSell()))
							.replace("%ALL_AUCTION_FIND_ITEM_GRADE%", Grados).replace("%ALL_AUCTION_FIND_ITEM_NAME%", Nombre)
							.replace("%ALL_AUCTION_FIND_ITEM_ICONO_LINK%", cbFormato.getBotonForm(opera.getIconImgFromItem(p.getIdItem(), true),
									ItemByPass.replace("%IDOBJETO%",idObjectLink)))
							.replace("%ALL_AUCTION_FIND_ITEM_BGCOLOR%", BGColor[Contador%2]);
				}else{
					if(Contador>=hasta){
						haveNext=true;
					}
				}
				Contador ++;
			}else{
				
			}
		}
		
		if(ToRemove!=null){
			if(ToRemove.size()>0){
				for(InfoAuction tmp : ToRemove){
					ITEMS_ON_AUCTION.remove(tmp);
				}
			}
		}
		
		html.replace("%ALL_FIND_ITEM%", AllItemGrid);
		
		
		String ByPass_Prev = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";" + String.valueOf( Pagina - 1 ) + ";" + _strBuscar + ";0";
		String ByPass_Next = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + GradeIN + ";" + tipo + ";" + String.valueOf( Pagina + 1 ) + ";" + _strBuscar + ";0";
		
		html.replace("%ALL_AUCTION_CONTROL_PAGE_PREVIEW_BYPASS%", ( Pagina>0 ? ByPass_Prev : "" ));
		html.replace("%ALL_AUCTION_CONTROL_PAGE_NEXT_BYPASS%", ( haveNext ? ByPass_Next : "" ));
		html.replace("%ALL_AUCTION_CONTROL_PAGE%", String.valueOf(Pagina+ 1));
		
		return html;
	}
	
	private static NpcHtmlMessage auction_Search_getTypes(L2PcInstance player, String Tipo, String _Grade, NpcHtmlMessage html){
		String Grade = PLAYER_VARIABLES.get(player.getObjectId()).getPlayerGradeList();
		
		String ByPass_TYPE_JEWEL = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + Grade + ";J;0;0;0";
		String ByPass_TYPE_ARMOR = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + Grade + ";A;0;0;0";
		String ByPass_TYPE_WEAPON = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + Grade + ";W;0;0;0";
		
		html.replace("%ALL_AUCTION_OPEN_JEWEL%", ByPass_TYPE_JEWEL);
		html.replace("%ALL_AUCTION_OPEN_ARMOR%", ByPass_TYPE_ARMOR);
		html.replace("%ALL_AUCTION_OPEN_WEAPON%", ByPass_TYPE_WEAPON);

		String ByPass_RING= "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;JEWEL_RING;" + Tipo + ";0;0;0";
		String ByPass_EARRING= "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;JEWEL_EARRING;" + Tipo + ";0;0;0";
		String ByPass_NECKLACE= "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;JEWEL_NECKLACE;" + Tipo + ";0;0;0";
		html.replace("%ALL_AUCTION_JEWEL_RING%", ByPass_RING);
		html.replace("%ALL_AUCTION_JEWEL_EARRING%", ByPass_EARRING);
		html.replace("%ALL_AUCTION_JEWEL_NECKLACE%", ByPass_NECKLACE);
		
		String ByPassArmor = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;ARMOR_%TIP%;" + Tipo + ";0;0;0";
		html.replace("%ALL_AUCTION_ARMOR_HELMET%", ByPassArmor.replace("%TIP%", "HELMET"));
		html.replace("%ALL_AUCTION_ARMOR_CHEST%", ByPassArmor.replace("%TIP%", "CHEST"));
		html.replace("%ALL_AUCTION_ARMOR_LEGS%", ByPassArmor.replace("%TIP%", "LEGS"));
		html.replace("%ALL_AUCTION_ARMOR_GLOVES%", ByPassArmor.replace("%TIP%", "GLOVES"));
		html.replace("%ALL_AUCTION_ARMOR_SHOES%", ByPassArmor.replace("%TIP%", "SHOES"));
		html.replace("%ALL_AUCTION_ARMOR_CLOAK%", ByPassArmor.replace("%TIP%", "CLOAK"));
		html.replace("%ALL_AUCTION_ARMOR_SHIRT%", ByPassArmor.replace("%TIP%", "SHIRT"));
		html.replace("%ALL_AUCTION_ARMOR_BELT%", ByPassArmor.replace("%TIP%", "BELT"));
		html.replace("%ALL_AUCTION_ARMOR_SIGIL%", ByPassArmor.replace("%TIP%", "SIGIL"));
		html.replace("%ALL_AUCTION_ARMOR_SHIELD%", ByPassArmor.replace("%TIP%", "SHIELD"));
		
		String ByPassWaepon = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";-1;WEAPON_%TIP%;" + Tipo + ";0;0;0";
		html.replace("%ALL_AUCTION_WEAPON_SWORD%", ByPassWaepon.replace("%TIP%", "SWORD"));
		html.replace("%ALL_AUCTION_WEAPON_DUAL_SWORD%", ByPassWaepon.replace("%TIP%", "DUALSWORD"));
		html.replace("%ALL_AUCTION_WEAPON_BIG_SWORD%", ByPassWaepon.replace("%TIP%", "BIGSWORD"));
		html.replace("%ALL_AUCTION_WEAPON_ANCIENT_SWORD%", ByPassWaepon.replace("%TIP%", "ANCIENTSWORD"));
		html.replace("%ALL_AUCTION_WEAPON_BLUNT%", ByPassWaepon.replace("%TIP%", "BLUNT"));
		html.replace("%ALL_AUCTION_BIG_BLUNT%", ByPassWaepon.replace("%TIP%", "BIGBLUNT"));
		html.replace("%ALL_AUCTION_DAGGER%", ByPassWaepon.replace("%TIP%", "DAGGER"));
		html.replace("%ALL_AUCTION_DUAL_DAGGER%", ByPassWaepon.replace("%TIP%", "DUALDAGGER"));
		html.replace("%ALL_AUCTION_BOW%", ByPassWaepon.replace("%TIP%", "BOW"));
		html.replace("%ALL_AUCTION_CROSSBOW%", ByPassWaepon.replace("%TIP%", "CROSSBOW"));
		html.replace("%ALL_AUCTION_POLE%", ByPassWaepon.replace("%TIP%", "POLE"));
		html.replace("%ALL_AUCTION_FISTS%", ByPassWaepon.replace("%TIP%", "FISTS"));
		html.replace("%ALL_AUCTION_RAPIER%", ByPassWaepon.replace("%TIP%", "RAPIER"));
		html.replace("%ALL_AUCTION_OTHER%", ByPassWaepon.replace("%TIP%", "OTHER"));
		
		PLAYER_VARIABLES.get(player.getObjectId()).setPlayerSearchingTypeItem(Tipo);
		
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
	
	private static void giveItemOffline(L2PcInstance player, int idInterno){
		if(SELLOFFLINER!=null){
			if(SELLOFFLINER.containsKey(player.getObjectId())){
				if(SELLOFFLINER.get(player.getObjectId())!=null){
					if(SELLOFFLINER.get(player.getObjectId()).size()>0){
						int idItemToGive = Integer.valueOf(SELLOFFLINER.get(player.getObjectId()).get(idInterno).get("ID_ITEM_REQUEST")); 
						Long CantidadToGive = Long.valueOf(SELLOFFLINER.get(player.getObjectId()).get(idInterno).get("TOTAL_ITEM_A_ENTREGAR").replace(".0", ""));
						int idItemVendido = Integer.valueOf(SELLOFFLINER.get(player.getObjectId()).get(idInterno).get("ID_ITEM_SOLD"));

						String Consulta = "delete from zeus_auctions_house_offline where idChar=? and idItemVendido=?";

						try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
								PreparedStatement statementt = conn.prepareStatement(Consulta))
							{
								statementt.setInt(1, player.getObjectId());
								statementt.setInt(2, idItemVendido);
								statementt.execute();
							}
							catch (Exception e)
							{
								_log.warning("Error ZeuS E->" + e.getMessage());
							}
						opera.giveReward(player, idItemToGive, CantidadToGive);

						SELLOFFLINER.get(player.getObjectId()).remove(idInterno);
					}
				}
			}
		}
		
	}
	
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		
		if(borrowAccount.getInstance().isBorrowActice(player.getAccountName())) {
			central.msgbox(language.getInstance().getMsg(player).BORROW_SYSTEM_BLOCKED, player);
			return "";
		}
		
		if(PLAYER_VARIABLES==null){
			_variableAuctionHouseCreate tem = new _variableAuctionHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tem);
		}else if(PLAYER_VARIABLES.size()==0 ){
			_variableAuctionHouseCreate tem = new _variableAuctionHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tem);			
		}else if(!PLAYER_VARIABLES.containsKey(player.getObjectId())){
			_variableAuctionHouseCreate tem = new _variableAuctionHouseCreate();
			PLAYER_VARIABLES.put(player.getObjectId(), tem);
		}		
		
		if(!general.STATUS_AUCTIONHOUSE) {
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
		
		_variableAuctionHouseCreate _Var = PLAYER_VARIABLES.get(player.getObjectId());
		
		if(parm1.equals("GiveItem")){
			giveItemOffline(player, Integer.valueOf(parm2));
		}else if(parm1.equals("-1")){
			_Var.setPlayerSearchingTypeItemIndex(parm2);
			String ByPassIn = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.AuctionHouse.name() + ";1;" + _Var.getPlayerGradeList() + ";" + parm3 + ";0;0;0";
			return mainHtml(player,ByPassIn,0,0,0);
		}else if(parm1.equals("0")){
			return mainHtml(player,params,Integer.valueOf(parm2),Integer.valueOf(parm3), Integer.valueOf(parm4));
		}else if(parm1.equals("1")){
			if(parm2.equals("0")){
				_Var.setPlayerGradeList("0");
				_Var.setPlayerSearchingWord("");
				_Var.setPlayerSearchingTypeItemIndex("");
			}
			return mainHtml(player,params,0,0,0);
		}else if(parm1.equals("showItemWindows")){
			if( Integer.valueOf(parm2)<0){
				InfoAuction tmpData = getInfoAction(Integer.valueOf(parm2));
				tmpData.setBuySellWindows(player);
				return "";
			}
			getInfoItemWindows(player, Integer.valueOf(parm2));
		}else if(parm1.equals("cancelauction")){
			if(!removeAuction(player,Integer.valueOf(parm2))){
				getInfoItemWindows(player, Integer.valueOf(parm2));
			}else{
				NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-auctionhouse-create-auction-cancel-auction-system.htm");
				central.sendHtml(player, html.getHtml());
				mainHtml(player,params,0,0,0);				
			}
		}else if(parm1.equals("buyItNow")){
			buyitProcess(player,Integer.valueOf(parm2),Long.valueOf(parm3));
		}
		return "";
	}
	
	private static void sendEmailToOwner(InfoAuction Item, long ItemSold){
			long QuantityToSend = Item.getQuantityRequest() * ItemSold;
			String ItemToSend = String.valueOf(Item.getIDItemRequested() ) + "," + String.valueOf(QuantityToSend);
			String titleM = language.getInstance().getMsg(Item.getPlayerID()).AH_EMAIL_TITLE_NEW_SELL.replace("$nameItem", Item.getItemName());
			String Messag = language.getInstance().getMsg(Item.getPlayerID()).AH_EMAIL_MESSAGE_NEW_SELL.replace("$nameItem", Item.getItemName()).replace("$mountSold", opera.getFormatNumbers(ItemSold)).replace("$mountEarned", opera.getFormatNumbers(QuantityToSend)).replace("$ItemEarned", Item.getNameItemToRequest()) ;
			EmailSend.sendEmail(null, Item.getSellerName() ,  titleM , Messag    , ItemToSend, tipoMensaje.Auction_house);
	}
	
	
}

class InfoAuction{
	private String ITEM_NAME;
	private String ITEM_GRADE;
	private int ITEM_ID_OBJECT;
	private int ITEM_ID;
	private int ITEM_ENCHANT;
	
	private L2ItemInstance ITEM_INSTANCE;
	
	private Long SELL_QUANTITY_ITEM_TO_SELL;
	private Long SELL_PRICE_ITEM_BY_1;
	private int SELL_ITEM_REQUEST_ID;
	private String SELL_ITEM_REQUEST_NAME;
	private String PLAYER_NAME;
	private int PLAYER_ID;
	private int SELL_FEE;
	
	private boolean IS_SELL;
	private boolean IS_FROM_ZEUS;
	
	private final Logger _log = Logger.getLogger(v_auction_house.class.getName());
	
	private void saveInBD(){
		
		String Consulta = "update zeus_auctions_house set ItemQuantityToSell=? where idObjeto=? and idOwner=?";
		
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
	
	public boolean checkItem(){
		if(this.IS_FROM_ZEUS){
			return true;
		}
		if(this.IS_SELL){
			
		}
		
		L2PcInstance thisPlayer = opera.getPlayerbyID(this.PLAYER_ID);
		if(!(thisPlayer instanceof L2PcInstance)) {
			return false;
		}
		if(thisPlayer.getSellList()==null) {
			return false;
		}
		TradeItem[] _items = null;
		if(this.IS_SELL){
			_items = thisPlayer.getSellList().getItems();
		}else{
			_items = thisPlayer.getBuyList().getItems();
		}
		for(TradeItem itm : _items){
			if(itm.getItem().getId() == this.ITEM_ID){
				return true;
			}
		}		
		return false;
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
		if(!this.IS_FROM_ZEUS){
			L2PcInstance thisPlayer = opera.getPlayerbyID(this.PLAYER_ID);
			TradeItem[] _items = null;
			if(this.IS_SELL){
				_items = thisPlayer.getSellList().getItems();
			}else{
				_items = thisPlayer.getBuyList().getItems();
			}
			for(TradeItem itm : _items){
				if(itm.getItem().getId() == this.ITEM_ID){
					return itm.getCount();
				}
			}			
		}
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
		int idItemToReturn = 0;
		
		try{
			if(this.ITEM_INSTANCE!=null){
				idItemToReturn = this.ITEM_INSTANCE.getItem().getId();
			}else{
				idItemToReturn = this.ITEM_ID;				
			}
		}catch(Exception a){
			idItemToReturn = this.ITEM_ID;
		}
		return idItemToReturn;
	}
	
	public final int getFee(){
		return this.SELL_FEE;
	}
	
	private void getInfo(int idObjectitem){
		String Consulta = "SELECT zeus_auctions_house.idObjeto, zeus_auctions_house.idOwner, zeus_auctions_house.idItemRequest, zeus_auctions_house.ItemRequestQuantity, zeus_auctions_house.ItemQuantityToSell, zeus_auctions_house.startUnix, characters.char_name, zeus_auctions_house.feed FROM zeus_auctions_house INNER JOIN characters ON zeus_auctions_house.idOwner = characters.charId WHERE zeus_auctions_house.idObjeto = ?";

		
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
					}
				}
			}
			catch (Exception e)
			{
				_log.warning("Erro ZeuS E->" + e.getMessage());
			}
		
		
		
	}
	
	public final Boolean isSell(){
		return this.IS_SELL;
	}
	
	public final int getObjectID(){
		return this.ITEM_ID_OBJECT;
	}
	
	public final boolean isFromZeuS(){
		return this.IS_FROM_ZEUS;
	}
	
	public InfoAuction(L2PcInstance player, int idItem, long price, long QuantityToSell, boolean isSell, int enchant, int idFromServer){
		this.IS_SELL = isSell;
		this.IS_FROM_ZEUS = false;
		L2Item temp = null;
		if(!isSell){
			temp = central.getItemData(idItem);
			this.ITEM_ID = idItem;
			this.ITEM_ENCHANT = enchant;
			this.ITEM_INSTANCE = null;
		}else{
			L2Object object = L2World.getInstance().findObject(idItem);
			L2ItemInstance itemInst = null;
			if(object instanceof L2ItemInstance){
				itemInst = (L2ItemInstance)object;
				temp = itemInst.getItem();
			}
			this.ITEM_ID = temp.getId();
			this.ITEM_ENCHANT = itemInst.getEnchantLevel();
			this.ITEM_INSTANCE = itemInst;
		}
		this.ITEM_NAME = temp.getName();
		this.ITEM_GRADE = temp.getItemGrade().name(); 
		this.ITEM_ID_OBJECT = idFromServer;
		this.PLAYER_ID = player.getObjectId();
		this.PLAYER_NAME = player.getName();
		this.SELL_PRICE_ITEM_BY_1 = price;
		this.SELL_QUANTITY_ITEM_TO_SELL = QuantityToSell;
		this.SELL_ITEM_REQUEST_ID = 57;
		this.SELL_ITEM_REQUEST_NAME = "Adena";
		this.SELL_FEE = 0;	
	}
	
	public void setBuySellWindows(L2PcInstance targetPlayer){
		L2PcInstance thisPlayer = opera.getPlayerbyID(this.PLAYER_ID);
		targetPlayer.setIsUsingBuySellStore(true);
		if(this.isSell()){
			targetPlayer.sendPacket(new PrivateStoreListSell(targetPlayer,thisPlayer));	
		}else{
			targetPlayer.sendPacket(new PrivateStoreListBuy(targetPlayer,thisPlayer));
		}
	}
	
	
	public InfoAuction() {
	}
	public InfoAuction(int idObjectItem, L2ItemInstance item, boolean isSell, boolean isFromZeuS){
		this.ITEM_ID_OBJECT = idObjectItem;
		this.ITEM_INSTANCE = item;
		this.ITEM_NAME = item.getName();
		this.ITEM_GRADE = item.getItem().getItemGrade().name(); 
		this.ITEM_ENCHANT = item.getEnchantLevel();
		this.IS_SELL = isSell;
		this.IS_FROM_ZEUS = isFromZeuS;
		getInfo(idObjectItem);
	}	
}
class _variableAuctionHouseCreate{
	private String SORT_GENERAL;
	private String SORT_MY_LIST;
	private String SEARCHING_WORD;
	private String SEARCHING_TYPE_ITEM;
	private String SEARCHING_TYPE_ITEM_INDEX;
	private String ALL_LIST_GRADE;
	private String ALL_CTRL_PAGINE;
	private int CREATION_PROCESS_IDOBJECT;
	private int CREATION_PROCESS_ID_ITEM_REQUESTED;
	private long CREATION_PROCESS_MOUNT_TO_GIVE;
	private long CREATION_PROCESS_PRICE_TO_SELL;
	
	
	public _variableAuctionHouseCreate(){
		this.SORT_GENERAL = "nameAZ";
		this.SORT_MY_LIST = "nameAZ";
		this.ALL_LIST_GRADE = "All";
		this.ALL_CTRL_PAGINE = "";
		this.SEARCHING_WORD = "";
		this.SEARCHING_TYPE_ITEM = "0";
		this.SEARCHING_TYPE_ITEM_INDEX = "";
	}
	

	public void setVariableNewAuction(int idObecjt, int idItemRequested, long mountToSell, long priceForEach){
		this.CREATION_PROCESS_IDOBJECT = idObecjt;
		this.CREATION_PROCESS_ID_ITEM_REQUESTED = idItemRequested;
		this.CREATION_PROCESS_MOUNT_TO_GIVE = mountToSell;
		this.CREATION_PROCESS_PRICE_TO_SELL = priceForEach;
	}
	
	public void setPlayerSearchingTypeItemIndex(String Data){
		this.SEARCHING_TYPE_ITEM_INDEX = Data;
	}
	public final String getPlayerSearchingTypeItemIndex(){
		return this.SEARCHING_TYPE_ITEM_INDEX;
	}
	
	public void setPlayerSearchingTypeItem(String Data){
		this.SEARCHING_TYPE_ITEM = Data;
	}

	public final String getPlayerSearchingTypeItem(){
		return this.SEARCHING_TYPE_ITEM;
	}
	
	public void setPlayerSearchingWord(String Data){
		this.SEARCHING_WORD = Data;
	}
	public final String getPlayerSearchingWord(){
		return this.SEARCHING_WORD;
	}
	
	public final long getCreatePrice(){
		return this.CREATION_PROCESS_PRICE_TO_SELL;
	}
	public void setCreatePrice(long Data){
		this.CREATION_PROCESS_PRICE_TO_SELL = Data;
	}	
	
	public final long getCreateQuantityToGive(){
		return this.CREATION_PROCESS_MOUNT_TO_GIVE;
	}
	public void setCreateQuantityToGive(long Data){
		this.CREATION_PROCESS_MOUNT_TO_GIVE = Data;
	}
	
	public final int getCreateIdItemRequested(){
		return CREATION_PROCESS_ID_ITEM_REQUESTED;
	}
	public void setCreateIdItemRequested(int Data){
		this.CREATION_PROCESS_ID_ITEM_REQUESTED = Data;
	}	
	
	public final int getCreateItemIdObject(){
		return this.CREATION_PROCESS_IDOBJECT;
	}
	public void setCreateItemIdObject(int Data){
		this.CREATION_PROCESS_IDOBJECT = Data;
	}
	
	public void setControlPagine(String Data){
		this.ALL_CTRL_PAGINE = Data;
	}
	public final String getControlPagine(){
		return this.ALL_CTRL_PAGINE;
	}	
	
	public void setPlayerGradeList(String Data){
		this.ALL_LIST_GRADE = Data;
	}
	public final String getPlayerGradeList(){
		if(this.ALL_LIST_GRADE.equals("0")){
			this.ALL_LIST_GRADE = "All";
		}
		return this.ALL_LIST_GRADE;
	}
	
	public void setPlayerSortGeneralList(String Data){
		this.SORT_GENERAL = Data;
	}
	public final String getPlayerSortGeneralList(){
		return this.SORT_GENERAL;
	}
	public void setPlayerSortMyList(String Data){
		this.SORT_MY_LIST = Data;
	}
	public final String getPlayerSortMyList(){
		return this.SORT_MY_LIST;
	}
}