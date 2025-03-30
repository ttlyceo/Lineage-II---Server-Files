package ZeuS.Config;

import java.util.logging.Logger;

public class buffcommunity {

	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger( buffcommunity.class.getName());
	
	private String BUFF_NAME;
	private int BUFF_ID;
	private int BUFF_LVL;
	private String BUFF_DESCRIPT;
	private int PRICE;
	private int ID_CATEGORIA;
	private String CATEGORIA_NAME;
	private String CATEGORIA_ICON;
	private boolean ISACTIVE;
	private boolean ISDONATE;
	private int MIN_LEVEL_TO_USE;
	private int ID_INTERNO;
	private int REUSE_TIME;
	
	private boolean USE_FLAG;
	private boolean USE_KARMA;
	private boolean USE_COMBAT_MODE;
	private boolean USE_JUST_PEACE_ZONE;
	
	public buffcommunity(String _IDINTERNO, int _ID, String _NAME, String _DESCRIP, int _MIN_LEVEL_TO_USE, int _LEVEL, int _PRICE, int _ID_CATEGORIA, String _NAME_CATEGORIA, boolean _isActive, boolean _isDonate, boolean _USE_FLAG, boolean _USE_KARMA, boolean _USE_COMBAT_MODE, int _REUSE_TIME){
		this.ID_INTERNO = Integer.valueOf(_IDINTERNO);
		this.BUFF_ID = _ID;
		this.BUFF_NAME = _NAME;
		this.BUFF_LVL = _LEVEL;
		this.BUFF_DESCRIPT = _DESCRIP;
		this.PRICE = _PRICE;
		this.ID_CATEGORIA = _ID_CATEGORIA;
		this.CATEGORIA_NAME = _NAME_CATEGORIA;
		this.ISACTIVE = _isActive;
		this.ISDONATE = _isDonate;
		this.MIN_LEVEL_TO_USE = _MIN_LEVEL_TO_USE;
		this.USE_FLAG = _USE_FLAG;
		this.USE_KARMA = _USE_KARMA;
		this.USE_COMBAT_MODE = _USE_COMBAT_MODE;
		this.REUSE_TIME = _REUSE_TIME;
	}
	
	public final int getReuseTime() {
		return this.REUSE_TIME;
	}
	
	public final int IdIterno(){
		return this.ID_INTERNO;
	}
	
	public boolean canUseJustPeaceZone(){
		return this.USE_JUST_PEACE_ZONE;
	}
	
	public boolean canUseInFlagMode(){
		return this.USE_FLAG;
	}
	
	public boolean canUseWithKarma(){
		return this.USE_KARMA;
	}
	
	public boolean canUseInCombatMode(){
		return this.USE_COMBAT_MODE;
	}
	
	public int getMinLevelToUse(){
		return this.MIN_LEVEL_TO_USE;
	}
	
	public boolean isActive(){
		return this.ISACTIVE;
	}
	
	public int getIdCategoria(){
		return this.ID_CATEGORIA;
	}
	public int getBuffID(){
		return this.BUFF_ID;
	}
	public String getBuffName(){
		return this.BUFF_NAME;
	}
	public String getCategoriaIcon(){
		return this.CATEGORIA_ICON;
	}
	public int getBuffLevel(){
		return this.BUFF_LVL;
	}
	public String getBuffDescript(){
		return this.BUFF_DESCRIPT;
	}
	public int getPrice(){
		return this.PRICE;
	}
	public int idCategoria(){
		return this.ID_CATEGORIA;
	}
	public String getCategoriaName(){
		return this.CATEGORIA_NAME;
	}
	
	public buffcommunity(int _ID, String _NAME_CATEGORIA, String _ICON_CATEGORIA, int _MIN_LEVEL_TO_USE, boolean isActive, int _PRICE, boolean _USE_FLAG, boolean _USE_KARMA, boolean _USE_COMBAT_MODE){
		this.ID_CATEGORIA = _ID;
		this.CATEGORIA_NAME = _NAME_CATEGORIA;
		this.CATEGORIA_ICON = _ICON_CATEGORIA;
		this.MIN_LEVEL_TO_USE = _MIN_LEVEL_TO_USE;
		this.ISACTIVE = isActive;
		this.PRICE = _PRICE;
		this.USE_FLAG = _USE_FLAG;
		this.USE_KARMA = _USE_KARMA;
		this.USE_COMBAT_MODE = _USE_COMBAT_MODE;
	}
	
	public buffcommunity(){
		
	}

	public String getEnchantBuff(){
		if( String.valueOf(this.BUFF_LVL).length()>1){
			return String.valueOf(this.BUFF_LVL).substring(1, String.valueOf(this.BUFF_LVL).length());
		}
		return "";
	}
	
	public boolean isPremium() {
		return this.ISDONATE;
	}
	
	
}
