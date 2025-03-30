package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class colorNameTitle {
	private static final Logger _log = Logger.getLogger(colorNameTitle.class.getName());
	private static Map<Integer, _color_name_title> PLAYER_COLOR = new HashMap<Integer, _color_name_title>();
	
	public static String showNameTitleColor(L2PcInstance player, boolean showNameColor) {
		NpcHtmlMessage html = null;
		if(showNameColor) {
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-color-name.htm");
		}else {
			html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) +"/communityboard/engine-color-title.htm");
		}
		html.replace("%BYPASS%", general.getCOMMUNITY_BOARD_ENGINE_PART_EXEC());
		return html.getHtml();
	}
	
	public static void getAllData() {
		String Consulta = "SELECT idChar, color_name, color_title FROM zeus_color_name_title";
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(Consulta)){
				ResultSet Resul = statement.executeQuery();
				while(Resul.next()){
					_color_name_title _tempCon = new _color_name_title(Resul.getInt("color_name"), Resul.getInt("color_title"));
					if(PLAYER_COLOR == null) {
						PLAYER_COLOR.put(Resul.getInt("idChar"), _tempCon);
					}else if(PLAYER_COLOR.size() == 0) {
						PLAYER_COLOR.put(Resul.getInt("idChar"), _tempCon);
					}else if(!PLAYER_COLOR.containsKey( Resul.getInt("idChar") )) {
						PLAYER_COLOR.put(Resul.getInt("idChar"), _tempCon);
					}else {
						PLAYER_COLOR.get(Resul.getInt("idChar")).setNameColor(Resul.getInt("color_name"));
						PLAYER_COLOR.get(Resul.getInt("idChar")).setTitleColor(Resul.getInt("color_title"));
					}
				}
		}catch(Exception a){
			
		}	
		
	}
	
	public static int getColors(L2PcInstance player, boolean isName) {
		if(PLAYER_COLOR==null) {
			if(PLAYER_COLOR.size() == 0 || !PLAYER_COLOR.containsKey(player.getObjectId())) {
				_color_name_title temp_color = new _color_name_title(-1,-1);
				PLAYER_COLOR.put(player.getObjectId(), temp_color);
				return -1;
			}
		}		
		if(PLAYER_COLOR.containsKey(player.getObjectId())) {
			_color_name_title _ColorTemp = PLAYER_COLOR.get(player.getObjectId());
			return (isName ? _ColorTemp.getNameColor() : _ColorTemp.getTitleColor());
		}else {
			PLAYER_COLOR.put(player.getObjectId(), new _color_name_title(-1, -1));
		}
		return -1;
	}
	
	public static boolean setColorName(L2PcInstance player, String color, String price) {
		return setColor(player, color, price, true);
	}
	public static boolean setColorTitle(L2PcInstance player, String color, String price) {
		return setColor(player, color, price, false);
	}
	
	private static boolean setColor(L2PcInstance player, String color, String price, boolean isName) {
		if(!opera.haveItem(player, price)) {
			return false;
		}
		
		if(player.isInCombat()) {
			central.msgbox(language.getInstance().getMsg(player).CANT_USE_IN_COMBAT_MODE , player);
			return false;
		}else if(player.isCombatFlagEquipped()) {
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_FLAG , player);
			return false;
		}else if(player.getKarma() > 0) {
			central.msgbox(language.getInstance().getMsg(player).CAN_NOT_USE_THIS_SERVICE_WHILE_YOU_PK, player);
			return false;
		}
		
		opera.removeItem(price, player);
		
		if(isName) {
			player.getAppearance().setNameColor(Integer.decode("0x" + color ));
			PLAYER_COLOR.get(player.getObjectId()).setNameColor(Integer.decode("0x" + color ));
		}else {
			player.getAppearance().setTitleColor(Integer.decode("0x" + color ));
			PLAYER_COLOR.get(player.getObjectId()).setTitleColor(Integer.decode("0x" + color ));
		}
		
		player.broadcastUserInfo();
		player.broadcastInfo();
		player.store(true);
		saveInDB(player);
		
		return true;
	}
	
	private static void saveInDB(L2PcInstance player) {
		
		_color_name_title PlayerColor = PLAYER_COLOR.get(player.getObjectId());
		
		String UpdateScript = "INSERT INTO zeus_color_name_title(idChar, color_name, color_title) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE color_name = ?, color_title = ?";
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement ps = con.prepareStatement(UpdateScript))
			{
				ps.setInt(1, player.getObjectId());
				ps.setInt(2, PlayerColor.getNameColor());
				ps.setInt(3, PlayerColor.getTitleColor());
				ps.setInt(4, PlayerColor.getNameColor());
				ps.setInt(5, PlayerColor.getTitleColor());
				ps.execute();
			}
		catch(Exception a) {
			_log.warning("ZeuS: Error recording the Colors on Database" + a.getMessage());
		}
		
	}
	
}

class _color_name_title{
	private int NAME_COLOR = -1;
	private int TITTLE_COLOR = -1;
	public _color_name_title(int _name, int _title) {
		this.NAME_COLOR = _name;
		this.TITTLE_COLOR = _title;
	}
	
	public int getNameColor() {
		return this.NAME_COLOR; 
	}
	
	public int getTitleColor() {
		return this.TITTLE_COLOR;
	}
	
	public void setNameColor(int Color) {
		this.NAME_COLOR = Color;
	}
	
	public void setTitleColor(int Color) {
		this.TITTLE_COLOR = Color;
	}
	
}
