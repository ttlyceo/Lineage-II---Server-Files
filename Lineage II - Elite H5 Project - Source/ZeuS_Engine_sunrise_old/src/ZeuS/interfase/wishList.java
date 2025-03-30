package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Config._itemInfo;
import ZeuS.language.language;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class wishList {
	private static Map<Integer, Vector<_WishItem>> Wish_list = new HashMap<Integer, Vector<_WishItem>>();
	private static final String SELECT_SQL = "SELECT idChar, idItem, beginDate, endDate FROM zeus_wish_list";
	private static final int Days = 7;
	private static final int MaxList = 6;
	private static Map<Integer, Vector<_itemInfo>> LAST_SEARCH = new HashMap<Integer, Vector<_itemInfo>>();
	public static final Logger _log = Logger.getLogger(wishList.class.getName());
	
	
	public static void showMenuWindows(L2PcInstance player, String Params) {
		String ByPassFoundItem = "bypass -h ZeuS wishlist found $txtItem";
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/wishlist-main.html");
		String GridItemToAdd = opera.getGridFormatFromHtml(html, 1, "%ITEM_TO_ADD%");
		String GridItemMyList = opera.getGridFormatFromHtml(html, 2, "%MY_LIST_ITEM%");
		html.replace("%BYPASS_FOUND%", ByPassFoundItem);
		if(Params.length()>0) {
			String Action = Params.split(" ")[0];
			switch (Action) {
				case "found":
					String ItemName = Params.substring(6, Params.length());
					Vector<_itemInfo> ItemSeleccionados = opera.getAllItemByName(ItemName,true);
					if(ItemSeleccionados!=null) {
						if(ItemSeleccionados.size()>0) {
							if(ItemSeleccionados.size()>6) {
								central.msgbox(language.getInstance().getMsg(player).WISH_LIST_BE_MORE_PRECISE_FOR_SEARCH , player);
							}else {
								LAST_SEARCH.put(player.getObjectId(), ItemSeleccionados);
							}
						}
					}
					break;
				case "addItem":
					int idItem = Integer.valueOf(Params.split(" ")[1]);
					if(!playerHaveWishListWithThisItem(player, idItem) ) {
						if(canAddMoreList(player)) {
							saveItemToWishList(player, idItem);
							central.msgbox(language.getInstance().getMsg(player).WISH_LIST_WAS_CREATED_WITH_DURATION_SEVEN_DAYS.replace("$itemname", central.getNombreITEMbyID(idItem)) , player);
						}else {
							central.msgbox(language.getInstance().getMsg(player).WISH_LIST_CANT_ADD_MORE_TO_YOUR_LIST.replace("$slot", String.valueOf(MaxList)), player);
						}
					}else {
						central.msgbox(language.getInstance().getMsg(player).WISH_LIST_YOU_HAVE_THIS_ITEM_IN_LIST, player);
					}
					break;
				case "delItem":
					int idItemRemove = Integer.valueOf(Params.split(" ")[1]);
					removeFromWishList(player, idItemRemove);
					break;
			}
		}else {
			if(LAST_SEARCH!=null) {
				if(LAST_SEARCH.size()>0) {
					if(LAST_SEARCH.containsKey(player.getObjectId())) {
						LAST_SEARCH.remove(player.getObjectId());
					}
				}
			}
		}
		
		Vector<_WishItem> myList = getAllWishlistFromPlayer(player);
		if(myList != null) {
			if(myList.size()>0) {
				String _data = "";
				for(_WishItem TT: myList) {
					String ByPassAdd = "bypass ZeuS wishlist delItem " + String.valueOf(TT.getIdItem());
					_data += GridItemMyList.replace("%UNTIL_DATE%", TT.getEndDate()) .replace("%REMOVE_TO_WISHLIST_BYPASS%", ByPassAdd) .replace("%ITEM_NAME%", TT.getName() ).replace("%ITEM_ICON%", TT.getIcon());
				}
				html.replace("%MY_LIST_ITEM%", _data);				
			}
		}
		
		if(LAST_SEARCH!=null) {
			if(LAST_SEARCH.size()>0) {
				if(LAST_SEARCH.containsKey(player.getObjectId())) {
					String _data = "";
					for(_itemInfo TT: LAST_SEARCH.get(player.getObjectId()) ) {
						String ByPassAdd = "bypass ZeuS wishlist addItem " + String.valueOf(TT.ITEM_ID);
						_data += GridItemToAdd.replace("%ADD_TO_WISHLIST_BYPASS%", ByPassAdd) .replace("%ITEM_NAME%", TT.ITEM_NAME).replace("%ITEM_ICON%", TT.ITEM_ICON);
					}
					html.replace("%ITEM_TO_ADD%", _data);					
				}
			}
		}
		
		html.replace("%ITEM_TO_ADD%", "<tr><td> </td></tr>");
		html.replace("%MY_LIST_ITEM%", "<tr><td> </td></tr>");
		central.sendHtml(player, html.getHtml());
	}
	
	public static void loadWishList() {
		try {
			Wish_list.clear();
		}catch(Exception a) {
			
		}
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement(SELECT_SQL)){
			try (ResultSet rs = ps.executeQuery()){
				while (rs.next()){
					try{
						_WishItem TItem = new _WishItem(rs.getInt("idItem"), rs.getInt("idChar"), rs.getInt("beginDate"), rs.getInt("endDate"));
						if(Wish_list==null) {
							Wish_list.put(rs.getInt("idItem"), new Vector<_WishItem>());
						}else if(Wish_list.size()==0) {
							Wish_list.put(rs.getInt("idItem"), new Vector<_WishItem>());
						}else if(!Wish_list.containsKey(rs.getInt("idItem"))) {
							Wish_list.put(rs.getInt("idItem"), new Vector<_WishItem>());
						}
						Wish_list.get(rs.getInt("idItem")).add(TItem);
					}catch(Exception e){
						_log.warning("");
						continue;
					}
				}
			}
		}
		catch (SQLException e)
		{
			_log.severe("");
		}		
	}
	
	public static void AnnounceItem(int idItem, String FromSection, String playerName) {
		if(Wish_list != null) {
			if(Wish_list.size()>0) {
				if(Wish_list.containsKey(idItem)) {
					Vector<_WishItem> AllData = Wish_list.get(idItem);
					for(_WishItem InfoData : AllData) {
						InfoData.sendEmail(FromSection, playerName);
					}
				}
			}
		}
	}
	public static void removeFromWishList(L2PcInstance player, int IdItem) {
		if(Wish_list != null) {
			if(Wish_list.size()>0) {
				if(Wish_list.containsKey(IdItem)) {
					_WishItem ToRemove = null;
					for(_WishItem tItem : Wish_list.get(IdItem)) {
						ToRemove = tItem;
						break;
					}
					if(ToRemove!=null) {
						ToRemove.removeMeFromBD();
						Wish_list.get(IdItem).remove(ToRemove);
					}
				}
			}
		}
	}
	
	public static boolean canAddMoreList(L2PcInstance player) {
		Vector<_WishItem> temp = getAllWishlistFromPlayer(player);
		if(temp!=null) {
			if(temp.size()>0) {
				if(temp.size()>= MaxList) {
					return false;
				}
			}
		}
		return true;
	}
	
	public static Vector<_WishItem> getAllWishlistFromPlayer(L2PcInstance player){
		Vector<_WishItem> retorno = new Vector<_WishItem>();
		Iterator itr = Wish_list.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry InfoWish =(Map.Entry)itr.next();
			Vector<_WishItem> tt = (Vector<_WishItem>)InfoWish.getValue();
			for(_WishItem IndiWish : tt) {
				if(IndiWish.getIdChar() == player.getObjectId()) {
					retorno.add(IndiWish);
				}				
			}
		}
		return retorno;
	}
	public static boolean playerHaveWishListWithThisItem(L2PcInstance Player, int IdItem) {
		if(Wish_list!=null) {
			if(Wish_list.size()>0) {
				if(Wish_list.containsKey(IdItem)) {
					for(_WishItem wlItem : Wish_list.get(IdItem)) {
						if(wlItem.getIdChar() == Player.getObjectId()){
							return true;
						}
					}
				}
			}
		}
		return false;
	}
	public static void saveItemToWishList(L2PcInstance player, int idItem) {
		int UnixNow = opera.getUnixTimeNow();
		int UnixEnd = UnixNow + ( Days * 86400 );
		_WishItem TItem = new _WishItem(idItem, player.getObjectId(), UnixNow, UnixEnd);			
		if(Wish_list==null) {
			Wish_list.put(idItem, new Vector<_WishItem>());
		}else if(Wish_list.size()==0) {
			Wish_list.put(idItem, new Vector<_WishItem>());
		}else if(!Wish_list.containsKey(idItem)) {
			Wish_list.put(idItem, new Vector<_WishItem>());
		}
		Wish_list.get(idItem).add(TItem);
		TItem.saveInDB();
	}
}
class _WishItem{
	private int ID_ITEM;
	private int ID_CHAR;
	private int BEGIN_DATE;
	private int END_DATE;
	private final String DELETE_SQL = "DELETE FROM zeus_wish_list WHERE idChar=? AND idItem=?" ;
	private final String INSERT_SQL = "INSERT INTO zeus_wish_list(idChar, idItem, beginDate, endDate) VALUES (?,?,?,?)";
	public final int getIdChar() {
		return this.ID_CHAR;
	}
	public _WishItem(int idItem, int idChar, int beginDate, int endDate) {
		this.ID_ITEM = idItem;
		this.ID_CHAR = idChar;
		this.BEGIN_DATE = beginDate;
		this.END_DATE = endDate;
	}
	public final int getRemainingSeconds() {
		return this.END_DATE - this.BEGIN_DATE;
	}
	public final String getEndDate() {
		return opera.getDateFromUnixTime(this.END_DATE);
	}
	public final int getIdItem() {
		return this.ID_ITEM;
	}
	public final String getName() {
		return central.getNombreITEMbyID(this.ID_ITEM);
	}
	public final String getIcon() {
		return opera.getIconImgFromItem(this.ID_ITEM, true);
	}
	public void sendEmail(String From, String playerName) {
		if(opera.getPlayerNameById(this.ID_CHAR).equalsIgnoreCase(playerName)) {
			return;
		}
		if(opera.getUnixTimeNow() < this.END_DATE) {
			String ItemName = central.getNombreITEMbyID(this.ID_ITEM);			
			String Message = language.getInstance().getMsg(this.ID_CHAR).WISH_LIST_NEW_AUCTION_BID_HAS_BEEN_CREATED.replace("$playername", playerName).replace("$Item_Name", ItemName).replace("$Section", From);
			EmailSend.sendEmail(null, opera.getPlayerNameById(this.ID_CHAR), ItemName + " from Your Wish List", Message, "", tipoMensaje.Wishlist);
		}else {
			removeMeFromBD();
		}
	}
	public void removeMeFromBD() {
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(DELETE_SQL)){
				statement.setInt(1, this.ID_CHAR);
				statement.setInt(2, this.ID_ITEM);
				statement.execute();
			}catch(Exception a){
				
			}		
	}
	public void saveInDB() {
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(INSERT_SQL)){
			statement.setInt(1, this.ID_CHAR);
			statement.setInt(2, this.ID_ITEM);
			statement.setInt(3, this.BEGIN_DATE);
			statement.setInt(4, this.END_DATE);
			statement.execute();
		}catch(Exception a){
			
		}
	}
}