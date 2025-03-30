package ZeuS.Comunidad.EngineForm;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Comunidad.Engine;
import ZeuS.Comunidad.cbManager;
import ZeuS.Config._dressItem;
import ZeuS.Config.general;
import ZeuS.dressme.dressme;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.Dlg;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.Dlg.IdDialog;
import ZeuS.server.comun;

public class v_Dressme extends dressme{

	private static Map<Integer, Integer>SEE_DRESSME_SHOW = new HashMap<Integer, Integer>();
	private final static Logger _log = Logger.getLogger(v_Dressme.class.getName());
	private static Map<Integer, v_dressme_buy_var> BUYING_VAR = new HashMap<Integer, v_dressme_buy_var>();
	private static Map<Integer, Boolean> SORT_TYPE_LIKE_MY_WEAPON = new HashMap<Integer, Boolean>();
	private static Map<Integer, Boolean> PURCHASED_ITEM = new HashMap<Integer, Boolean>();
	private static Map<Integer, List<Integer>> ITEM_PLAYERS = new HashMap<Integer, List<Integer>>();
	private static Map<Integer, String> LAST_BYPASS = new HashMap<Integer, String>();
	public static void setNewItemToPlayer(int IdPlayer, int IdItem) {
		if(ITEM_PLAYERS == null) {
			ITEM_PLAYERS.put(IdPlayer, new ArrayList<Integer>());
			ITEM_PLAYERS.get(IdPlayer).add(IdItem);
		}else if(ITEM_PLAYERS.size()==0) {
			ITEM_PLAYERS.put(IdPlayer, new ArrayList<Integer>());
			ITEM_PLAYERS.get(IdPlayer).add(IdItem);			
		}else if(!ITEM_PLAYERS.containsKey(IdPlayer)) {
			ITEM_PLAYERS.put(IdPlayer, new ArrayList<Integer>());
			ITEM_PLAYERS.get(IdPlayer).add(IdItem);			
		}else {
			ITEM_PLAYERS.get(IdPlayer).add(IdItem);			
		}
	}
	
	public static void getAllItemPlayer() {
		String QryClean = "DELETE FROM zeus_dressme_items_char WHERE idChar NOT IN (SELECT charId FROM characters)";
		Connection con = null;		
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(QryClean);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}
		try{
			con.close();
		}catch(SQLException a){

		}		
		String Qry = "SELECT idChar, idItem FROM zeus_dressme_items_char";
		con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Qry);
			ResultSet Resul = statement.executeQuery();
			while(Resul.next()){
				setNewItemToPlayer(Resul.getInt("idChar"), Resul.getInt("idItem"));
			}
		}catch(Exception a){
			
		}
		try{
			con.close();
		}catch(SQLException a){

		}		
	}
	
	private static boolean isInItemPlayer(int IdPlayer, int IdItem) {
		if(ITEM_PLAYERS!=null) {
			if(ITEM_PLAYERS.size()>0) {
				if(ITEM_PLAYERS.containsKey(IdPlayer)) {
					return ITEM_PLAYERS.get(IdPlayer).contains(IdItem);
				}
			}
		}
		return false;
	}
	
	private static String getMain(L2PcInstance player){
		final NpcHtmlMessage html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-dressme.htm") ;

		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		
		int IDSLOT[] = {Inventory.PAPERDOLL_LHAND,Inventory.PAPERDOLL_RHAND,Inventory.PAPERDOLL_CHEST,Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_CLOAK};
		
		int idDressmeUsingNow = DRESSME_PLAYER.get(player.getObjectId()).getIdDressmeUsing();
		
		html.replace("%DRESSME_ID_USING%", String.valueOf(idDressmeUsingNow));

		for(int i=1; i<=10;i++){
			if(i == idDressmeUsingNow){
				html.replace("%USING_" + String.valueOf(i) + "%", " (IN USE)");
			}else{
				html.replace("%USING_" + String.valueOf(i) + "%", "");
			}
		}
		//html.replace("Dressme " + String.valueOf(idDressmeUsingNow), "Dressme " + String.valueOf(idDressmeUsingNow) + " (IN USE)");
		
		html.replace("%DRESSME_ID_USE_REMOVE%", String.valueOf(SEE_DRESSME_SHOW.get(player.getObjectId())));
		
		html.replace("%DRESSME_USING_THIS%", (SEE_DRESSME_SHOW.get(player.getObjectId()) ==  idDressmeUsingNow ? "YES" : "NO"));
        
		String ICO_NOITEM = ItemData.getInstance().getTemplate(3883).getIcon();
		for(int i=0; i<IDSLOT.length; i++){
			try{
				int idVirtual = DRESSME_PLAYER.get(player.getObjectId()).getIds(IDSLOT[i],SEE_DRESSME_SHOW.get(player.getObjectId()),false, true);
				if(idVirtual<=0){
					String NameItem = "NONE";
					String Icono = ICO_NOITEM;
					html.replace("%ICON_" + String.valueOf(IDSLOT[i]) + "%", Icono);
					html.replace("%NAME_"+ String.valueOf(IDSLOT[i]) +"%", NameItem) ;
				}else{
					L2Item ItemDressme = ItemData.getInstance().getTemplate(idVirtual);
					if(ItemDressme!=null){
						String Icono = ItemDressme.getIcon();
						String NameItem = ItemDressme.getName();
						html.replace("%ICON_" + String.valueOf(IDSLOT[i]) + "%", Icono);
						html.replace("%NAME_"+ String.valueOf(IDSLOT[i]) +"%", NameItem) ;
					}else {
						html.replace("%ICON_" + String.valueOf(IDSLOT[i]) + "%", "");
						html.replace("%NAME_"+ String.valueOf(IDSLOT[i]) +"%", "") ;						
					}
				}
			}catch(Exception a){
				html.replace("%ICON_" + String.valueOf(IDSLOT[i]) + "%", "");
				html.replace("%NAME_"+ String.valueOf(IDSLOT[i]) +"%", "") ;					
			}
		}
		return html.getHtml();
	}
	
	private static String mainHtml(L2PcInstance player, String Params){
		String retorno = getMain(player);
		return retorno;
	}
	
	private static void getHTML_DressmeItem(L2PcInstance player, String Part, int Page, int IdSlot, boolean sortLikeMyWeapon){
		//engine-dressme-items.htm
		NpcHtmlMessage HTML = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-dressme-items.htm") ;
		HTML.replace("%DRESSME_SECTION%", Part);
		
		String BoughtItMessage = opera.getGridFormatFromHtml(HTML, 11, "");
		String ExclusiveBtnHTML_CHAR = opera.getGridFormatFromHtml(HTML, 12, "");
		String ExclusiveBtnHTML_CLAN = opera.getGridFormatFromHtml(HTML, 13, "");
		
		String MESSAGE_REMOVE_OTHER = opera.getGridFormatFromHtml(HTML, 1, "");
		String REMOVE_BTN = opera.getGridFormatFromHtml(HTML, 2, "");
		String TRY_BTN = opera.getGridFormatFromHtml(HTML, 3, "");
		String GridFormarItems = opera.getGridFormatFromHtml(HTML, 4, "%ITEM_GRID%");
		
		if(DRESSME_ITEMS==null){
			central.msgbox(language.getInstance().getMsg(player).DRESSME_SERVER_CONFIG_EMPTY, player);
			return;
		}else if(DRESSME_ITEMS.size()==0){
			central.msgbox(language.getInstance().getMsg(player).DRESSME_SERVER_CONFIG_EMPTY, player);
			return;
		}
		int Maximo = 5;
		int _Desde = Page * 5;
		int _Hasta = _Desde + Maximo;
		
    	String ByPass_Prev = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SHOW_PARTS;"+ String.valueOf(IdSlot) +";"+ String.valueOf(Page - 1) +";0;0;0;0";
    	String ByPass_Next = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SHOW_PARTS;"+ String.valueOf(IdSlot) +";"+ String.valueOf(Page + 1) +";0;0;0;0";
		String ByPassTryOnMe = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";TRY_DRESSME_ITEM;%ID_SLOT%;"+ Part +";%ID_ITEM%;0;0;0";
		String ByPassBuyExclusiveClan = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";BUY_EXCLUSIVE_CLAN;%ID_ITEM%;%ID_SLOT%;%PART%;%CLAN_DAYS_USE%;0;0";
		String ByPassBuyExclusiveChar = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";BUY_EXCLUSIVE_CHAR;%ID_ITEM%;%ID_SLOT%;%PART%;%CHAR_DAYS_USE%;0;0";
		String _BtnBuyExclusiveClan = opera.getGridFormatFromHtml(HTML, 7, "");
		_BtnBuyExclusiveClan = _BtnBuyExclusiveClan.replace("%BYPASS_BUY_CLAN_EXCLUSIVE%", ByPassBuyExclusiveClan.replace("%ID_SLOT%", String.valueOf(IdSlot)));
		String _BtnBuyExclusiveChar = opera.getGridFormatFromHtml(HTML, 8, "");
		_BtnBuyExclusiveChar = _BtnBuyExclusiveChar.replace("%BYPASS_BUY_CHAR_EXCLUSIVE%", ByPassBuyExclusiveChar.replace("%ID_SLOT%", String.valueOf(IdSlot)));
		String _BtnBuyMoreDaysExclusive = opera.getGridFormatFromHtml(HTML, 9, "");
		
		
		if(DRESSME_PLAYER.get(player.getObjectId()).getDressme(SEE_DRESSME_SHOW.get(player.getObjectId())).getIds(IdSlot,false, true)>0 ){
			String ByPassBtnRemove = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";REMOVE_DRESSME_ITEM_ASK;%ID_SLOT%;"+ Part +";0;0;0;0";
			String BtnCancelar = REMOVE_BTN.replace("%BYPASS_REMOVE%", ByPassBtnRemove.replace("%ID_SLOT%", String.valueOf(IdSlot)) );
			boolean isFullDress = DRESSME_PLAYER.get(player.getObjectId()).getDressme( SEE_DRESSME_SHOW.get(player.getObjectId()) ).isFullDress();
			boolean isFullDressWeapon = DRESSME_PLAYER.get(player.getObjectId()).getDressme( SEE_DRESSME_SHOW.get(player.getObjectId()) ).isFullDressWeapon();
			boolean isFullDressArmor = DRESSME_PLAYER.get(player.getObjectId()).getDressme( SEE_DRESSME_SHOW.get(player.getObjectId()) ).isFullChest();
			
			boolean CanRemoveIt = true;
			
			if(IdSlot == 23){
				CanRemoveIt = true;
			}else if(IdSlot != 7 && IdSlot != 5){
				if(((IdSlot != 6) && isFullDress) || ((IdSlot != 6) && isFullDressArmor) ){
					CanRemoveIt=false;
				}
			}else if ((IdSlot != 5) && isFullDressWeapon){
					CanRemoveIt=false;
			}			

			if(!CanRemoveIt){
				HTML.replace("%ACTION_TO_REMOVE%", MESSAGE_REMOVE_OTHER);
			}else{
				HTML.replace("%ACTION_TO_REMOVE%", BtnCancelar);			
			}
		}
		
		HTML.replace("%ACTION_TO_REMOVE%", "");
		
		String ByPassTypeLikeMyWeapon = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SHOW_PARTS;"+ String.valueOf(IdSlot) +";0;"+ (sortLikeMyWeapon ? "FALSE" : "TRUE") +";0;0;0";
		
		String ByPassSortPurchasedItem = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SHOW_PARTS;"+ String.valueOf(IdSlot) +";0;0;"+  ( PURCHASED_ITEM.get(player.getObjectId()) ? "FALSE" : "TRUE" )  +";0;0";
		
		HTML.replace("%BYPASS_PURCHASED_ITEM%", ByPassSortPurchasedItem);
		HTML.replace("%STATUS_PURCHASED%", ( PURCHASED_ITEM.get(player.getObjectId()) ? "Yes" : "No" ));
		
		String SortTypeLikeMyWeaponButton = opera.getGridFormatFromHtml(HTML, 10, "").replace("%STATUS_SORT%", sortLikeMyWeapon ? "Yes" : "No") .replace("%BYPASS_SORT_LIKE_MY_WEAPON%", ByPassTypeLikeMyWeapon);
		
		if(Part.equals("WEAPON")) {
			HTML.replace("%SORT_OPTION%", SortTypeLikeMyWeaponButton);
		}else {
			HTML.replace("%SORT_OPTION%", "");
		}		
		
		boolean haveNext = false;
		int Contador = 0;
		String _html = "";
		for(_dressItem ItemDressme : DRESSME_ITEMS.get(Part)){
			String BtnBuyExclusive = "";
			
			if(!ItemDressme.isVisible()) {
				continue;
			}
			if(!ItemDressme.isEnabled()) {
				continue;
			}
			if(sortLikeMyWeapon) {
				
				if(player.getInventory().getPaperdollItem(Inventory.PAPERDOLL_RHAND) == null) {
					continue;
				}
				
				 L2Item _Weapon = player.getInventory().getPaperdollItem( Inventory.PAPERDOLL_RHAND ).getItem(); //.unEquipItemInBodySlot(L2Item.SLOT_R_HAND);
				if(_Weapon != null) {
					if(_Weapon.getItemType() != ItemDressme.getItem().getItemType()) {
						continue;
					}
				}
			}
			if(ItemDressme.isClassProhibited(player.getClassId().getId())) {
				continue;
			}
			boolean isExclusiveItem = false;
			_dressItem ItD = ItemDressme;
			if(ItD.getItem()==null){
				try{
					_log.warning("Dressme Item is Null ->" + ItD.getIdItem());
				}catch(Exception a){
					
				}
				continue;
			}
			
			boolean IsOwner = isInItemPlayer(player.getObjectId(), ItD.getIdItem());
			
			if(PURCHASED_ITEM.get(player.getObjectId())) {
				if(!IsOwner) {
					continue;
				}
			}
			
			boolean ItemHasClan_Owner = false;
			if(ItD._isClan_exclusive()){
				if(ItD.haveOwnerClan()){
					String ClanName = ItD.getOwnerClanName();					
					if(player.getClan()==null){
						BtnBuyExclusive = ExclusiveBtnHTML_CLAN.replace("%CLAN_CHAR_NAME%", ClanName) + "<br1>End Date: " + ItD._getEndDate();
					}						
					if(!ItD._isOwnerClan( player.getClanId())){
						BtnBuyExclusive = ExclusiveBtnHTML_CLAN.replace("%CLAN_CHAR_NAME%", ClanName) + "<br1>End Date: " + ItD._getEndDate();
					}else{
						BtnBuyExclusive = _BtnBuyMoreDaysExclusive.replace("%BYPASS_BUY_MORE_DAYS%", ByPassBuyExclusiveClan).replace("%DAYS%", String.valueOf(ItD.getClanDays())) + "<br1>End Date: " + ItD._getEndDate();
					}
					ItemHasClan_Owner = true;
					BtnBuyExclusive = BtnBuyExclusive.replace("%CLAN_DAYS_USE%", String.valueOf(ItD.getClanDays())).replace("%CHAR_DAYS_USE%", String.valueOf(ItD.getAccountDays()));					
				}else{
					BtnBuyExclusive = _BtnBuyExclusiveClan;
				}
				isExclusiveItem = true;
			}
			
			if(ItD._isChar_exclusive()){
				if(!ItemHasClan_Owner) {
					if(ItD.haveOwnerChar()){
						String CharName = ItD.getOwnerCharName();					
						if(!ItD._isOwnerChar( player.getObjectId())){
							BtnBuyExclusive = ExclusiveBtnHTML_CHAR.replace("%CLAN_CHAR_NAME%", CharName) + "<br1>End Date: " + ItD._getEndDate();
						}else{
							BtnBuyExclusive = _BtnBuyMoreDaysExclusive.replace("%BYPASS_BUY_MORE_DAYS%", ByPassBuyExclusiveChar).replace("%DAYS%", String.valueOf(ItD.getAccountDays())) + "<br1>End Date: " + ItD._getEndDate();
						}
					}else{
						BtnBuyExclusive += _BtnBuyExclusiveChar;
					}
				}
				isExclusiveItem = true;
				BtnBuyExclusive = BtnBuyExclusive.replace("%CLAN_DAYS_USE%", String.valueOf(ItD.getClanDays())).replace("%CHAR_DAYS_USE%", String.valueOf(ItD.getAccountDays()));
			}
			
			switch(IdSlot){
				case 7:
					if(L2Item.SLOT_L_HAND != ItD.getItem().getBodyPart()){continue;}
					break;
				case 5:
					try{
						if(L2Item.SLOT_R_HAND != ItD.getItem().getBodyPart()){
							if(L2Item.SLOT_LR_HAND != ItD.getItem().getBodyPart()){
								continue;
							}
						}
					}catch(Exception a){
						//_log.warning("Error->"  + a.getMessage() + "->" + ItD.getIdItem());
					}
					break;
				case 6:
					if(L2Item.SLOT_CHEST != ItD.getItem().getBodyPart()){
						if(L2Item.SLOT_ALLDRESS != ItD.getItem().getBodyPart()){
							if(L2Item.SLOT_FULL_ARMOR != ItD.getItem().getBodyPart()){
								continue;
							}
						}
					}
					break;
				case 11:
					if(L2Item.SLOT_LEGS != ItD.getItem().getBodyPart()){continue;}
					break;
				case 10:
					if(L2Item.SLOT_GLOVES != ItD.getItem().getBodyPart()){continue;}
					break;
				case 12:
					if(L2Item.SLOT_FEET != ItD.getItem().getBodyPart()){continue;}
					break;						
			}
			if( Contador >= _Desde && Contador < _Hasta ){
				String ByPassSelectItemToDressme = "";
				if(IsOwner) {
					ByPassSelectItemToDressme = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";USE_DRESSME_PART_ASK;"+String.valueOf(ItD.getIdItem())+";"+String.valueOf(IdSlot)+";"+Part+";0;0;0";
				}else {
					ByPassSelectItemToDressme = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SET_DRESSME_PART_ASK;"+String.valueOf(ItD.getIdItem())+";"+String.valueOf(IdSlot)+";"+Part+";0;0;0";
				}
	            String TryItemInfo = TRY_BTN.replace("%BYPASS_TRY%", ByPassTryOnMe);
	            String tempBoughtItMessage = BoughtItMessage + BtnBuyExclusive.replace("%PART%", Part) .replace("%ID_SLOT%", String.valueOf(IdSlot)) .replace("%ID_ITEM%", String.valueOf( ItD.getIdItem() ));
	            TryItemInfo = TryItemInfo.replace("%ID_SLOT%", String.valueOf(IdSlot)).replace("%ID_ITEM%", String.valueOf(ItD.getIdItem())) + ( isExclusiveItem ? "<br1>" + BtnBuyExclusive.replace("%PART%", Part) .replace("%ID_SLOT%", String.valueOf(IdSlot)) .replace("%ID_ITEM%", String.valueOf( ItD.getIdItem() )) : "" );
				_html += GridFormarItems.replace("%TRY_ITEM_INFO%", (IsOwner ? "" : TryItemInfo)) .replace("%ITEM_COST%", IsOwner ? tempBoughtItMessage : ItD.getCostBox()) .replace("%ITEM_NAME%", ItD.getItem().getName()).replace("%BYPASS_BUY_ITEM%", ByPassSelectItemToDressme) .replace("%ITEM_ICON%", ItD.getItem().getIcon());
			}
			Contador++;
			if(Contador > _Hasta){
				haveNext = true;
			}			
		}
		
		HTML.replace("%ITEM_GRID%", _html);
		
		String gridContentPages = opera.getGridFormatFromHtml(HTML, 6, "%GRID_PAGES%");
		String gridContentAll = opera.getGridFormatFromHtml(HTML, 5, "%GRID_PAGES_ALL%");
		String _BtnMenus = gridContentAll;
		String _BtnPages = "";

		int _Cont = 0;
		int _Pages = 0;
		for(int i=1 ; i <= Contador ; i++ ){
			_Cont++;
			if(_Cont < Maximo) {
				continue;
			}
			_Pages++;
			_Cont=0;
			String BB = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SHOW_PARTS;"+ String.valueOf(IdSlot) +";"+ String.valueOf(_Pages - 1) +";0;0;0;0";
			if((_Pages%10)==0){
				_BtnMenus = _BtnMenus.replace("%GRID_PAGES%", _BtnPages);
				_BtnPages = "";
				_BtnMenus += gridContentAll;
			}
			_BtnPages += gridContentPages.replace("%PAG_NUMBER%", String.valueOf(_Pages)).replace("%BYPASS_PAGE%", BB);
		}
		if((Contador % Maximo)>0) {
			if(_Pages>0) {
				_Pages++;
				String BB = "bypass " + general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";SHOW_PARTS;"+ String.valueOf(IdSlot) +";"+ String.valueOf(_Pages - 1) +";0;0;0;0";
				if((_Pages%10)==0){
					_BtnMenus = _BtnMenus.replace("%GRID_PAGES%", _BtnPages);
					_BtnPages = "";
					_BtnMenus += gridContentAll;
				}				
				_BtnPages += gridContentPages.replace("%PAG_NUMBER%", String.valueOf(_Pages)).replace("%BYPASS_PAGE%", BB);				
			}
		}
		_BtnMenus = _BtnMenus.replace("%GRID_PAGES%", _BtnPages);
		_BtnPages = "";		
		HTML.replace("%GRID_PAGES_ALL%", _BtnMenus);
		HTML.replace("%BYPASS_PREV%", ( Page==0 ? "" : ByPass_Prev ));
		HTML.replace("%BYPASS_NEXT%", ( haveNext ? ByPass_Next : "" ));
		HTML.replace("%PAGE%", String.valueOf(Page + 1));
		opera.enviarHTML(player, HTML.getHtml());
	}
	
	private static _dressItem getData(String Parte, int idItem){
		for(_dressItem Temm : DRESSME_ITEMS.get(Parte)){
			if(Temm.getIdItem() == idItem){
				return Temm;
			}
		}
		return null;
	}
	
	public static void removeDressmeID(L2PcInstance player){
		if(BUYING_VAR==null){
			return;
		}
		if(!BUYING_VAR.containsKey(player.getObjectId())){
			return;
		}
		v_dressme_buy_var tmp = BUYING_VAR.get(player.getObjectId());
		int idDressmeSee = tmp.getLoc();
		DRESSME_PLAYER.get(player.getObjectId()).setCleanDressme(idDressmeSee);
		cbManager.separateAndSend(mainHtml(player, tmp.getPart()), player);
		BUYING_VAR.remove(player.getObjectId());
	}
	
	@SuppressWarnings("unused")
	public static String bypass(L2PcInstance player, String params){
		if(SORT_TYPE_LIKE_MY_WEAPON==null) {
			SORT_TYPE_LIKE_MY_WEAPON.put(player.getObjectId(), false);
		}else if(!SORT_TYPE_LIKE_MY_WEAPON.containsKey(player.getObjectId())) {
			SORT_TYPE_LIKE_MY_WEAPON.put(player.getObjectId(), false);
		}
		if(PURCHASED_ITEM==null) {
			PURCHASED_ITEM.put(player.getObjectId(), false);
		}else if(!PURCHASED_ITEM.containsKey(player.getObjectId())) {
			PURCHASED_ITEM.put(player.getObjectId(), false);
		}
		
		if(!general.STATUS_DRESSME) {
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
		if(parm1.equals("0")){
			SEE_DRESSME_SHOW.put(player.getObjectId(), 1);
			return mainHtml(player,params);
		}else if(parm1.equals("TOO_SEE")){
			int idDressmeSee = Integer.valueOf(parm2);
			try{
				SEE_DRESSME_SHOW.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			SEE_DRESSME_SHOW.put(player.getObjectId(), idDressmeSee);
			return mainHtml(player,params);
		}else if(parm1.equals("TOO_USE")){
			int idDressmeSee = Integer.valueOf(parm2);
			DRESSME_PLAYER.get(player.getObjectId()).useDressme(idDressmeSee);
			return mainHtml(player,params);
		}else if(parm1.equals("REMOVE_ALL_ASK")){
			int idDressmeSee = Integer.valueOf(parm2);
			try{
				BUYING_VAR.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			v_dressme_buy_var Vtmp = new v_dressme_buy_var(0, idDressmeSee, params, "", 0, 0);
			BUYING_VAR.put(player.getObjectId(), Vtmp);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DRESSME_ARE_YOU_SURE_YOU_DELETE_ALL_DRESSME_ITEM_FROM_DRESSME_ID.replace("$id", parm2), IdDialog.ENGINE_DRESSME_REMOVE_DRESSME, 70);
			return mainHtml(player,params);
		}else if(parm1.equals("REMOVE_ALL")){
			int idDressmeSee = Integer.valueOf(parm2);
			DRESSME_PLAYER.get(player.getObjectId()).setCleanDressme(idDressmeSee);
			return mainHtml(player,params);
		}else if(parm1.equals("SHOW_PARTS")){
			LAST_BYPASS.put(player.getObjectId(), params);
			int idSlot = Integer.valueOf(parm2);
			int Pag = Integer.valueOf(parm3);
			cbManager.separateAndSend(mainHtml(player, params), player);
			if(parm5.equalsIgnoreCase("false")) {
				PURCHASED_ITEM.put(player.getObjectId(), false);
			}else if(parm5.equalsIgnoreCase("true")) {
				PURCHASED_ITEM.put(player.getObjectId(), true);
			}
			switch (idSlot) {
				case Inventory.PAPERDOLL_LHAND:
					getHTML_DressmeItem(player, "SHIELD", Pag, idSlot, false);
					break;
				case Inventory.PAPERDOLL_RHAND:
					boolean ShowMyType = SORT_TYPE_LIKE_MY_WEAPON.get(player.getObjectId());
					try {
						if(!parm4.equalsIgnoreCase("0")) {
							ShowMyType = Boolean.parseBoolean(parm4);
							SORT_TYPE_LIKE_MY_WEAPON.put(player.getObjectId(), ShowMyType);
						}
					}catch(Exception a) {
						ShowMyType = false;
					}
					getHTML_DressmeItem(player, "WEAPON", Pag, idSlot, ShowMyType);
					break;
				case Inventory.PAPERDOLL_CLOAK:
					getHTML_DressmeItem(player, "CLOAK", Pag, idSlot, false);
					break;					
				default:
					getHTML_DressmeItem(player, "ARMOR", Pag, idSlot, false);
					break;
			}
		}else if(parm1.equals("TRY_DRESSME_ITEM")){
			int IdLoc = Integer.valueOf(parm2);
			int IdItem = Integer.valueOf(parm4);
			_dressItem _dressmeItem = getData(parm3, IdItem);
			if(!DRESSME_PLAYER.get(player.getObjectId()).getDressme().setTry(IdLoc, IdItem, _dressmeItem.getIdDeco())){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_YOU_NEED_TO_WAIT_TO_TRY_ITEM, player);
			}else{
				ThreadPoolManager.getInstance().scheduleGeneral(new StartDressme(player) ,100);
				ThreadPoolManager.getInstance().scheduleGeneral(new StartDressme(player) ,5100);
			}
		}else if(parm1.equals("SET_DRESSME_PART_ASK")){
			try{
				BUYING_VAR.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			int IdItem = Integer.valueOf(parm2);
			int IdLoc = Integer.valueOf(parm3);
			String Parte = String.valueOf(parm4);
			String Cost =  getData(Parte, IdItem).getCost();
			_dressItem _dressmeItem = getData(Parte, IdItem);
			
			boolean isTopPvP = opera.isTopPvPPk(player);
			
			if(_dressmeItem.isHeroItem() && !player.isHero() && !player.isGM()){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_JUST_FOR_HERO, player);
				return "";
			}
			if(_dressmeItem.isNobleItem() && !player.isNoble() && !player.isGM()){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_JUST_FOR_NOBLE, player);
				return "";
			}
			if(_dressmeItem.isTopPvPPKItem() && !isTopPvP && !player.isGM()){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_JUST_FOR_TOP_PVP_PK, player);
				return "";
			}
			
			v_dressme_buy_var Vtmp = new v_dressme_buy_var(IdItem, IdLoc, Parte, Cost, 0, 0);
			BUYING_VAR.put(player.getObjectId(), Vtmp);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DRESSME_WANT_TO_BUY_THIS_DRESSME.replace("$item_name", getData(Parte, IdItem).getItemName()), IdDialog.ENGINE_DRESSME_PERSONAL_BUY);			
		}else if(parm1.equals("USE_DRESSME_PART_ASK")){
			try{
				BUYING_VAR.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			int IdItem = Integer.valueOf(parm2);
			int IdLoc = Integer.valueOf(parm3);
			String Parte = String.valueOf(parm4);
			String Cost =  getData(Parte, IdItem).getCost();
			_dressItem _dressmeItem = getData(Parte, IdItem);
			
			boolean isTopPvP = opera.isTopPvPPk(player);
			
			if(_dressmeItem.isHeroItem() && !player.isHero() && !player.isGM()){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_JUST_FOR_HERO, player);
				return "";
			}
			if(_dressmeItem.isNobleItem() && !player.isNoble() && !player.isGM()){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_JUST_FOR_NOBLE, player);
				return "";
			}
			if(_dressmeItem.isTopPvPPKItem() && !isTopPvP && !player.isGM()){
				central.msgbox(language.getInstance().getMsg(player).DRESSME_JUST_FOR_TOP_PVP_PK, player);
				return "";
			}
			
			v_dressme_buy_var Vtmp = new v_dressme_buy_var(IdItem, IdLoc, Parte, Cost, 0, 0);
			BUYING_VAR.put(player.getObjectId(), Vtmp);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DRESSME_WANT_TO_USE_THIS_DRESSME.replace("$item_name", getData(Parte, IdItem).getItemName()), IdDialog.ENGINE_DRESSME_PERSONAL_BUY);
		}else if(parm1.equals("SET_DRESSME_PART")){
		}else if(parm1.equals("BUY_EXCLUSIVE_CLAN")){
			try{
				BUYING_VAR.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			int IdItem = Integer.valueOf(parm2);
			int IdLoc = Integer.valueOf(parm3);
			String Parte = String.valueOf(parm4);
			String Cost =  getData(Parte, IdItem).getExclusiveClanCost();
			int DaysClan = getData(Parte, IdItem).getClanDays();
			int MinLevelClan = getData(Parte, IdItem).getClanExclusiveMinLevel();
			v_dressme_buy_var Vtmp = new v_dressme_buy_var(IdItem, IdLoc, Parte, Cost, DaysClan, MinLevelClan);
			BUYING_VAR.put(player.getObjectId(), Vtmp);
			showExclusiveCharClanExplain(player, false);
		}else if(parm1.equals("BUY_EXCLUSIVE_CHAR")){
			try{
				BUYING_VAR.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			int IdItem = Integer.valueOf(parm2);
			int IdLoc = Integer.valueOf(parm3);
			String Parte = String.valueOf(parm4);
			String Cost =  getData(Parte, IdItem).getExclusiveCharCost();
			int DaysAccount = getData(Parte, IdItem).getAccountDays();
			int MinLevelChar = getData(Parte, IdItem).getCharExclusiveMinLevel();
			v_dressme_buy_var Vtmp = new v_dressme_buy_var(IdItem, IdLoc, Parte, Cost, DaysAccount, MinLevelChar);
			BUYING_VAR.put(player.getObjectId(), Vtmp);
			showExclusiveCharClanExplain(player, true);			
		}else if(parm1.equals("REMOVE_DRESSME_ITEM_ASK")){
			try{
				BUYING_VAR.remove(player.getObjectId());
			}catch(Exception a){
				
			}
			int IdItem = 0;
			int IdLoc = Integer.valueOf(parm2);
			String Parte = parm3;
			String Cost =  params;
			v_dressme_buy_var Vtmp = new v_dressme_buy_var(IdItem, IdLoc, Parte, Cost, 0, 0);
			BUYING_VAR.put(player.getObjectId(), Vtmp);
			cbManager.separateAndSend(mainHtml(player, params), player);
			Dlg.sendDlg(player, language.getInstance().getMsg(player).DRESSME_ARE_YOU_SURE_YOU_DELETE_THIS_ITEM_FROM_DRESSME_ID.replace("id",  String.valueOf(SEE_DRESSME_SHOW.get(player.getObjectId()))),IdDialog.ENGINE_DRESSME_REMOVE_ITEM_FROM_DRESSME, 60);
		}else if(parm1.equals("REMOVE_DRESSME_ITEM")){
			int idSlot = Integer.valueOf(parm2);
			DRESSME_PLAYER.get(player.getObjectId()).setIdDressmeToPart(idSlot, 0, SEE_DRESSME_SHOW.get(player.getObjectId()) );
			cbManager.separateAndSend(mainHtml(player, params), player);			
		}
		return "";
	}
	public static void removeItemFromDressme(L2PcInstance player){
		if(BUYING_VAR == null){
			return;
		}
		if(!BUYING_VAR.containsKey(player.getObjectId())){
			return;
		}
		v_dressme_buy_var _tmp = BUYING_VAR.get(player.getObjectId());
		DRESSME_PLAYER.get(player.getObjectId()).setIdDressmeToPart(_tmp.getLoc(), 0, SEE_DRESSME_SHOW.get(player.getObjectId()) );
		cbManager.separateAndSend(mainHtml(player, _tmp.getPrice()), player);
		BUYING_VAR.remove(player.getObjectId());
	}
	public static void ApliDressmeFromDlg(L2PcInstance player){
		String params = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";TOO_SEE;" + String.valueOf(SEE_DRESSME_SHOW.get(player.getObjectId())) + ";0;0;0;0;0";
		if(BUYING_VAR==null){
			return;
		}
		if(!BUYING_VAR.containsKey(player.getObjectId())){
			return;
		}
		int IdItem = BUYING_VAR.get(player.getObjectId()).getIdItem();
		int IdLoc = BUYING_VAR.get(player.getObjectId()).getLoc();
		String Parte = BUYING_VAR.get(player.getObjectId()).getPart();
		String Cost =  BUYING_VAR.get(player.getObjectId()).getPrice();
		
		_dressItem _itemData = getData(Parte, IdItem);
		
		if(_itemData._isChar_exclusive()){
			if(_itemData.haveOwnerChar()) {
				if(!_itemData._isOwnerChar(player.getObjectId())) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, player);
					return;
				}
			}
		}
		
		
		if(_itemData._isClan_exclusive()){
			if(_itemData.haveOwnerClan()) {
				if(player.getClan()==null) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, player);
					return;				
				}else if(!_itemData._isOwnerClan(player.getClanId())) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, player);
					return;					
				}				
			}
		}
		
		boolean HaveThisItem = isInItemPlayer(player.getObjectId(), IdItem);
		
		boolean removeItem = false;
		
		if(!HaveThisItem) {
			if(Cost.length()>0){
				if(opera.haveItem(player, Cost)){
					removeItem = true;
				}else{
					cbManager.separateAndSend(mainHtml(player, params), player);
					return;
				}
			}
		}
		
		int idDressmeSelect = SEE_DRESSME_SHOW.get(player.getObjectId());
		if(idDressmeSelect<=0){
			central.msgbox(language.getInstance().getMsg(player).DRESSME_YOU_NEED_TO_SET_A_DRESSME_BEFORE, player);
			cbManager.separateAndSend(mainHtml(player, params), player);
			return;
		}
		
		DRESSME_PLAYER.get(player.getObjectId()).setIdDressmeToPart(IdLoc, IdItem, idDressmeSelect);
		if(removeItem && !HaveThisItem){
			opera.removeItem(Cost, player);
		}
		cbManager.separateAndSend(mainHtml(player, params), player);
		BUYING_VAR.remove(player.getObjectId());
		if(LAST_BYPASS!=null) {
			if(LAST_BYPASS.containsKey(player.getObjectId())) {
				bypass(player, LAST_BYPASS.get(player.getObjectId()));
			}
		}
		LAST_BYPASS.put(player.getObjectId(), params);
	}
	
	public static void makeQuestionAboutExclusiveClan(L2PcInstance player){
		if(BUYING_VAR==null){
			return;
		}
		if(!BUYING_VAR.containsKey(player.getObjectId())){
			return;
		}
		String ItemName = central.getNombreITEMbyID(BUYING_VAR.get(player.getObjectId()).getIdItem());
		Dlg.sendDlg(player, language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CLAN_EXCLUSIVE.replace("$Item", ItemName), IdDialog.ENGINE_DRESSME_EXCLUSIVE_BUY_CLAN, 40);
	}
	
	public static void makeQuestionAboutExclusiveChar(L2PcInstance player){
		if(BUYING_VAR==null){
			return;
		}
		if(!BUYING_VAR.containsKey(player.getObjectId())){
			return;
		}
		String ItemName = central.getNombreITEMbyID(BUYING_VAR.get(player.getObjectId()).getIdItem());
		Dlg.sendDlg(player, language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_ARE_YOU_SURE_WANT_THIS_ITEM_FOR_CHAR_EXCLUSIVE.replace("$Item", ItemName), IdDialog.ENGINE_DRESSME_EXCLUSIVE_BUY_CHAR, 40);
	}	
	
	public static void ApliDressmeFromExclusive(L2PcInstance player, boolean isChar){
		String params = general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC() + ";" + Engine.enumBypass.Dressme.name() + ";TOO_SEE;" + String.valueOf(SEE_DRESSME_SHOW.get(player.getObjectId())) + ";0;0;0;0;0";
		
		if(player.getClan()==null && !isChar){
			central.msgbox(language.getInstance().getMsg(player).YOU_DONT_HAVE_CLAN, player);
			return;
		}
		if(BUYING_VAR==null){
			return;
		}
		if(!BUYING_VAR.containsKey(player.getObjectId())){
			return;
		}
		int IdItem = BUYING_VAR.get(player.getObjectId()).getIdItem();
		int IdLoc = BUYING_VAR.get(player.getObjectId()).getLoc();
		String Cost =  BUYING_VAR.get(player.getObjectId()).getPrice();
		
		boolean removeItem = false;
		
		_dressItem _dressmeItem = getData(BUYING_VAR.get(player.getObjectId()).getPart(), BUYING_VAR.get(player.getObjectId()).getIdItem());

		if(Cost.length()>0){
			if(opera.haveItem(player, Cost)){
				removeItem = true;
			}else{
				cbManager.separateAndSend(mainHtml(player, params), player);
				return;
			}
		}
		
		int idDressmeSelect = SEE_DRESSME_SHOW.get(player.getObjectId());
		if(idDressmeSelect<=0){
			central.msgbox(language.getInstance().getMsg(player).DRESSME_YOU_NEED_TO_SET_A_DRESSME_BEFORE, player);
			cbManager.separateAndSend(mainHtml(player, params), player);
			return;
		}
		
		_dressmeItem.loadExclusiveData();

		if(_dressmeItem._isClan_exclusive()) {
			if(_dressmeItem.haveOwnerClan()) {
				if(player.getClan() == null) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, player);
					return;
				}else if(!_dressmeItem._isOwnerClan(player.getClanId())) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, player);
					return;					
				}else if(_dressmeItem.getClanExclusiveMinLevel() > player.getClan().getLevel()) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_CLAN_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL.replace("$minLevel", String.valueOf(_dressmeItem.getClanExclusiveMinLevel())), player);
					return;
				}
			}
		}
		
		if(_dressmeItem._isChar_exclusive()) {
			if(_dressmeItem.haveOwnerChar()) {
				if(!_dressmeItem._isOwnerChar(player.getObjectId())) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_EXCLUSIVE_DRESSME_ITEM_HAS_ALREADY_BEEN_ACQUIRED, player);
					return;						
				}else if(_dressmeItem.getCharExclusiveMinLevel()> player.getLevel()) {
					central.msgbox(language.getInstance().getMsg(player).DRESSME_CHAR_YOU_CANT_BUY_THE_EXCLUSIVE_USE_OF_THIS_ITEM_NO_LEVEL.replace("$minLevel", String.valueOf(_dressmeItem.getClanExclusiveMinLevel())), player);
					return;					
				}
			}
		}
		
		_dressmeItem.buyDays(player, isChar);
		DRESSME_PLAYER.get(player.getObjectId()).setIdDressmeToPart(IdLoc, IdItem, idDressmeSelect);
		if(removeItem){
			opera.removeItem(Cost, player);
		}
		cbManager.separateAndSend(mainHtml(player, params), player);
		cbFormato.cerrarTutorial(player);
	}
	
	private static void showExclusiveCharClanExplain(L2PcInstance player, boolean isChar){
		String bypassAceptClan = "link zeusDRCL";
		String bypassAceptChar = "link zeusDRCH";
		NpcHtmlMessage html = null;
		String gridPrice = "";
		String Price = "";
		int Days = 0;
		if(isChar){
			html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-dressme-buy-exclusive-char.htm") ;
			html.replace("%LINK_BUY%", bypassAceptChar);
		}else{
			html = comun.htmlMaker(player, general.DIR_HTML + language.getInstance().getFolder(player) + "/communityboard/engine-dressme-buy-exclusive-clan.htm") ;
			html.replace("%LINK_BUY%", bypassAceptClan);
		}
		String gridFormat = opera.getGridFormatFromHtml(html, 1, "%GRID_COST%");		
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		html.replace("%DRESSME_ITEM_NAME%", central.getNombreITEMbyID( BUYING_VAR.get(player.getObjectId()).getIdItem() ) );
		Price = BUYING_VAR.get(player.getObjectId()).getPrice();
		Days = BUYING_VAR.get(player.getObjectId()).getDays();
		if(Price.indexOf(";")>0){
			for(String _pPrice : Price.split(";")){
				gridPrice += gridFormat.replace("%DRESSME_COST_ITEM_COUNT%", opera.getFormatNumbers(_pPrice.split(",")[1])).replace("%DRESSME_COST_ITEM_NAME%", central.getNombreITEMbyID( Integer.valueOf( _pPrice.split(",")[0])));	
			}
		}else{
			gridPrice = gridFormat.replace("%DRESSME_COST_ITEM_COUNT%", opera.getFormatNumbers(Price.split(",")[1])).replace("%DRESSME_COST_ITEM_NAME%", central.getNombreITEMbyID( Integer.valueOf( Price.split(",")[0])));
		}
		html.replace("%GRID_COST%", gridPrice);
		html.replace("%DAYS%", String.valueOf(BUYING_VAR.get(player.getObjectId()).getDays()));
		html.replace("%MIN_LEVEL%", String.valueOf( BUYING_VAR.get(player.getObjectId()).getMinLevel() ) );		
		central.sendHtml(player, html.getHtml(), true);
	}
	
	public static class StartDressme implements Runnable{
		L2PcInstance activeChar;
		public StartDressme(L2PcInstance _Char){
			activeChar = _Char;
		}
		@Override
		public void run(){
			try{
				InventoryUpdate iu = new InventoryUpdate();
				activeChar.sendPacket(iu);
				activeChar.broadcastUserInfo();
				InventoryUpdate iu2 = new InventoryUpdate();
				activeChar.sendPacket(iu2);
				activeChar.broadcastUserInfo();
			}catch(Exception a){

			}

		}
	}	
}
class v_dressme_buy_var{
	private final int ID_ITEM;
	private final int ID_LOC;
	private final String STR_PART;
	private final String COST;
	private final int DAYS;
	private final int MIN_LEVEL;
	public v_dressme_buy_var(int _idItem, int _idLoc, String _part, String _cost, int _days, int MinLevel){
		this.ID_ITEM = _idItem;
		this.ID_LOC = _idLoc;
		this.STR_PART = _part;
		this.COST = _cost;
		this.DAYS = _days;
		this.MIN_LEVEL = MinLevel;
	}
	public int getMinLevel(){
		return this.MIN_LEVEL;
	}
	public int getIdItem(){
		return this.ID_ITEM;
	}
	public int getLoc(){
		return this.ID_LOC;
	}
	public String getPart(){
		return this.STR_PART;
	}
	public String getPrice(){
		return this.COST;
	}
	public int getDays() {
		return this.DAYS;
	}
}
