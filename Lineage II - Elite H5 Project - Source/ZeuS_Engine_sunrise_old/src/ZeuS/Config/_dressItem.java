package ZeuS.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.sql.ClanTable;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;

public class _dressItem {
	private int ID_ITEM=0;
	private int ID_HOOD=-100;
	private String COST="";
	private L2Item _ITEMDATA;
	private L2Item _ITEMDECODATA;
	
	private boolean userJustHero;
	private boolean userJustNoble;
	private boolean userJustTopPVPPK;
	private boolean char_exclusive;
	private String ExclusiveCharCost="";	
	private int CHAR_OWNER= 0;
	private int CHAR_END_DATE = 0;
	private int CHAR_DAYS = 0;
	private int CHAR_MIN_LEVEL = 0;
	
	private boolean clan_exclusive;	
	private String ExclusiveClanCost="";	
	private int CLAN_OWNER= 0;
	private int CLAN_END_DATE = 0;
	private int CLAN_DAYS = 0;
	private int CLAN_MIN_LEVEL = 0;
	
	private boolean IS_ENABLED;
	
	private Vector<Integer> CLASS_ID_RESTRICTION = new Vector<Integer>();
	
	private boolean IS_VISIBLE;
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(_dressItem.class.getName());
	public _dressItem(int id, String cost, boolean justForHero, boolean justForNoble, boolean justTopPVPPK, boolean clanExclusive, int clanExclusive_Days, String clanExclusive_Cost, boolean charExclusive, int charExclusive_Days, String charExclusive_Cost, int idHood, boolean isVisible, Vector<Integer>_classIdRestriction, int _charexclusiveMinLevel, int _clanexclusiveMinLevel, boolean _IsEnable){
		this.ID_ITEM = id;
		this.ID_HOOD = idHood;
		this.COST = cost;
		try{
			this._ITEMDATA = ItemData.getInstance().getTemplate(id);
		}catch(Exception a){
			_log.warning("Dressme: Item ID: " + id + " no exist.");
		}
		if(idHood>0) {
			try{
				this._ITEMDECODATA = ItemData.getInstance().getTemplate(idHood);
			}catch(Exception a) {
				this._ITEMDECODATA = null;
				this.ID_HOOD = -100;
				_log.warning("Dressme: Item Deco ID: " + id + " no exist.");
			}
		}
		this.userJustHero = justForHero;
		this.userJustNoble = justForNoble;
		this.userJustTopPVPPK = justTopPVPPK;
		this.clan_exclusive = clanExclusive;
		this.CLAN_DAYS = clanExclusive_Days;
		this.ExclusiveClanCost = clanExclusive_Cost;
		this.char_exclusive = charExclusive;
		this.ExclusiveCharCost = charExclusive_Cost;
		this.CHAR_DAYS = charExclusive_Days;
		this.IS_VISIBLE = isVisible;
		this.CLASS_ID_RESTRICTION = _classIdRestriction;
		this.CHAR_MIN_LEVEL = _charexclusiveMinLevel;
		this.CLAN_MIN_LEVEL = _clanexclusiveMinLevel;
		this.IS_ENABLED = _IsEnable;
		loadExclusiveData();
	}
	
	public int getClanExclusiveMinLevel() {
		return this.CLAN_MIN_LEVEL;
	}
	public int getCharExclusiveMinLevel() {
		return this.CHAR_MIN_LEVEL;
	}
	
	public boolean isVisible() {
		return this.IS_VISIBLE;
	}
	
	public boolean isEnabled() {
		if(this.IS_ENABLED) {
			if(this.COST.trim().length()==0) {
				return false;
			}
		}
		return this.IS_ENABLED;
	}
	
	public boolean isClassProhibited(int idClass) {
		if(this.CLASS_ID_RESTRICTION!=null) {
			if(this.CLASS_ID_RESTRICTION.size()>0) {
				return this.CLASS_ID_RESTRICTION.contains(idClass);
			}
		}
		return false;
	}
	
	private void removeFromDB(boolean Force){
		
		
		if(haveOwnerChar()){
			
		}else if(haveOwnerClan()){
			
		}
		
		
		String query = "DELETE FROM zeus_dressme_exclusive WHERE itemid = ?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statementt = conn.prepareStatement(query))
		{
			statementt.setInt(1, this.ID_ITEM);
			statementt.execute();
		}
		catch (Exception e)
		{
			
		}		
	}	
	public void loadExclusiveData(){
		this.CLAN_OWNER = 0;
		this.CLAN_END_DATE = 0;
		this.CHAR_OWNER = 0;
		this.CHAR_END_DATE = 0;
		String query = "SELECT clanid, charid, end_date FROM zeus_dressme_exclusive WHERE itemid = ?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(query))
			{
			statementt.setInt(1, this.ID_ITEM);
			ResultSet rss = statementt.executeQuery();
			while (rss.next()){
				this.CLAN_OWNER = rss.getInt(1);
				this.CHAR_OWNER = rss.getInt(2);
				this.CLAN_END_DATE = rss.getInt(3);
				this.CHAR_END_DATE = rss.getInt(3);
			}
		}catch(Exception e){

		}
	}
	public void buyDays(L2PcInstance player, boolean isChar){
		if(player.isGM()){
			return;
		}
		int idClan = 0;
		int idChar = player.getObjectId();
		if(!isChar){
			idClan = player.getClan().getId();
			idChar = 0;
		}
		int secondsGive = (isChar ? this.CHAR_DAYS : this.CLAN_DAYS) * 86400;
		
		String query = "INSERT INTO zeus_dressme_exclusive (itemid, clanid, charid, begin_date, end_date) VALUES (?,?,?,?,?) ON DUPLICATE KEY UPDATE end_date = end_date + ?";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statementt = conn.prepareStatement(query))
			{
				statementt.setInt(1, this.ID_ITEM);
				statementt.setInt(2, idClan);
				statementt.setInt(3, idChar);
				statementt.setInt(4, opera.getUnixTimeNow());
				statementt.setInt(5, opera.getUnixTimeNow() + secondsGive);
				statementt.setInt(6, secondsGive);
				
				statementt.execute();
				
			}
			catch (Exception e)
			{
				_log.warning("Error saving Dressme exclusive -> " + e.getMessage());
			}
		loadExclusiveData();
	}
	public String _getEndDate(){
		if(this.CLAN_OWNER > 0){
			return opera.getDateFromUnixTime(this.CLAN_END_DATE);
		}else{
			return opera.getDateFromUnixTime(this.CHAR_END_DATE);
		}
	}
	
	private boolean _isCharActive(){
		if(getOwnerCharName().length()==0){
			return false;
		}
		return true;
	}
	
	private boolean _isClanActive(){
		L2Clan _Clan = ClanTable.getInstance().getClan(this.CLAN_OWNER);
		if(_Clan == null){
			return false;
		}
		return true;
	}
	public String getOwnerClanName(){
		L2Clan _Clan = ClanTable.getInstance().getClan(this.CLAN_OWNER);
		if(_Clan != null){
			return _Clan.getName();
		}
		return "";
	}
	public String getOwnerCharName(){
		String qry = "SELECT char_name FROM characters WHERE charId = ?";
		String charName = "";
		try (Connection conn = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statementt = conn.prepareStatement(qry))
			{
			statementt.setInt(1, this.CHAR_OWNER);
			ResultSet rss = statementt.executeQuery();
			while (rss.next()){
				charName = rss.getString("char_name");
			}
		}catch(Exception e){

		}
		return charName;
	}
	
	public boolean haveOwnerClan(){
		if(this.CLAN_OWNER <= 0){
			return false;
		}
		if(this.CHAR_OWNER > 0){
			return false;
		}
		
		if(!this._isClanActive()){
			this.CLAN_END_DATE=0;
			this.CLAN_OWNER=0;
			removeFromDB(true);
			return false;
		}
		if(this.CLAN_END_DATE > 0){
			if(this.CLAN_END_DATE <= opera.getUnixTimeNow()){
				this.CLAN_OWNER = 0;
				removeFromDB(true);
				return false;
			}
		}
		return true;
	}
	
	public boolean haveOwnerChar(){
		if(this.CHAR_OWNER <= 0){
			return false;
		}
		if(this.CLAN_OWNER > 0){
			return false;
		}
		if(!this._isCharActive()){
			this.CHAR_END_DATE=0;
			this.CHAR_OWNER=0;
			removeFromDB(true);
			return false;
		}
		if(CHAR_END_DATE > 0){
			if(CHAR_END_DATE <= opera.getUnixTimeNow()){
				this.CHAR_OWNER = 0;
				removeFromDB(true);
				return false;
			}
		}
		return true;
	}	
	
	public String getItemName(){
		return this._ITEMDATA.getName();
	}
	
	public boolean _isOwnerClan(int idClan){
		return idClan == this.CLAN_OWNER;
	}
	public boolean _isOwnerChar(int idChar){
		return idChar == this.CHAR_OWNER;
	}
	
	public int _getOwnerClanId(){
		if(!this.clan_exclusive){
			return 0;
		}
		if(this.CLAN_END_DATE>0){
			if(this.CLAN_END_DATE <= opera.getUnixTimeNow()){
				this.CLAN_OWNER = 0;
				this.CLAN_END_DATE = 0;
				this.haveOwnerClan();
				return 0;
			}
		}
		if(this.CLAN_OWNER>0){
			return this.CLAN_OWNER;
		}
		return 0;
	}
	
	public int _getOwnerCharId(){
		if(!this.char_exclusive){
			return 0;
		}
		if(this.CHAR_END_DATE>0){
			if(this.CHAR_END_DATE <= opera.getUnixTimeNow()){
				this.CHAR_OWNER = 0;
				this.CHAR_END_DATE = 0;
				this.haveOwnerChar();
			}
		}
		if(this.CHAR_OWNER>0){
			return CHAR_OWNER;
		}
		return 0;
	}	
	public boolean _isClan_exclusive(){
		if(this.CLAN_END_DATE>0){
			if(this.CLAN_END_DATE <= opera.getUnixTimeNow()){
				this.CLAN_OWNER = 0;
				this.CLAN_END_DATE = 0;
				this.haveOwnerClan();
			}
		}		
		return this.clan_exclusive;
	}
	public boolean _isChar_exclusive(){
		if(this.CHAR_END_DATE>0){
			if(this.CHAR_END_DATE <= opera.getUnixTimeNow()){
				this.CHAR_OWNER = 0;
				this.CHAR_END_DATE = 0;
				this.haveOwnerChar();
			}
		}		
		return this.char_exclusive;
	}	
	public boolean isHeroItem(){
		return this.userJustHero;
	}
	public boolean isNobleItem(){
		return this.userJustNoble;
	}
	public boolean isTopPvPPKItem(){
		return this.userJustTopPVPPK;
	}
	public L2Item getItem(){
		return this._ITEMDATA;
	}
	public int getIdItem(){
		return this.ID_ITEM;
	}
	public L2Item getItemDeco() {
		return this._ITEMDECODATA;
	}
	public int getIdDeco(){
		return this.ID_HOOD;
	}
	public String getCost(){
		return this.COST;
	}
	public int getClanDays(){
		return this.CLAN_DAYS;
	}
	public int getAccountDays(){
		return this.CHAR_DAYS;
	}	
	public String getExclusiveClanCost(){
		return this.ExclusiveClanCost;
	}
	public String getExclusiveCharCost(){
		return this.ExclusiveCharCost;
	}	
	
	public String getCostBox(){
		String Retorno = "";
		if(this.COST.indexOf(";")>0){
			for(String Itm : this.COST.split(";")){
				String Cantidad = Itm.split(",")[1];
				int IdItem = Integer.valueOf(Itm.split(",")[0]);
				if(Retorno.length()>0){
					Retorno += "<br1>";
				}
				Retorno += opera.getFormatNumbers(Cantidad) + " " + central.getNombreITEMbyID(IdItem) + "<br1>";
			}			
		}else{
			String Cantidad = this.COST.split(",")[1];
			int IdItem = Integer.valueOf(this.COST.split(",")[0]);
			Retorno = opera.getFormatNumbers(Cantidad) + " " + central.getNombreITEMbyID(IdItem) + "<br1>";			
		}
		return Retorno;
	}
	
	public String getClanCostBox(){
		String Retorno = "";
			if(this.ExclusiveClanCost.indexOf(";")>0){
				for(String Itm : this.ExclusiveClanCost.split(";")){
					String Cantidad = Itm.split(",")[1];
					int IdItem = Integer.valueOf(Itm.split(",")[0]);
					if(Retorno.length()>0){
						Retorno += "<br1>";
					}
					Retorno += opera.getFormatNumbers(Cantidad) + " " + central.getNombreITEMbyID(IdItem) + "<br1>";
				}
			}else{
				String Cantidad = ExclusiveClanCost.split(",")[1];
				int IdItem = Integer.valueOf(ExclusiveClanCost.split(",")[0]);
				Retorno = opera.getFormatNumbers(Cantidad) + " " + central.getNombreITEMbyID(IdItem) + "<br1>";					
			}
		return Retorno;
	}
	public String getCharCostBox(){
		String Retorno = "";
			if(this.ExclusiveCharCost.indexOf(";")>0){
				for(String Itm : this.ExclusiveCharCost.split(";")){
					String Cantidad = Itm.split(",")[1];
					int IdItem = Integer.valueOf(Itm.split(",")[0]);
					if(Retorno.length()>0){
						Retorno += "<br1>";
					}
					Retorno += opera.getFormatNumbers(Cantidad) + " " + central.getNombreITEMbyID(IdItem) + "<br1>";
				}
			}else{
				String Cantidad = ExclusiveCharCost.split(",")[1];
				int IdItem = Integer.valueOf(ExclusiveCharCost.split(",")[0]);
				Retorno = opera.getFormatNumbers(Cantidad) + " " + central.getNombreITEMbyID(IdItem) + "<br1>";				
			}
		return Retorno;
	}	
}
