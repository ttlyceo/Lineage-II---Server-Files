package ZeuS.language;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;

public class setting {
	private final Logger _log = Logger.getLogger(setting.class.getName());
	protected String LANGUAGE = "";
	protected int idPlayer = 0 ;
	
	public setting(int IdPlayer){
		idPlayer = IdPlayer;
		loadConfig();
	}
	
	private void loadConfig(){
		loadLanguage();
	}

	public void loadLanguage(){
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
				Statement st = con.createStatement();
				PreparedStatement psqry = con.prepareStatement("SELECT language FROM zeus_char_config WHERE idchar=?")){
				psqry.setInt(1, idPlayer);
				ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					this.LANGUAGE = rss.getString(1);
				}
				rss.close();
		}catch (Exception e){
				_log.warning("" + e.getMessage());
		}
	}
	protected String getLanguage(){
		return this.LANGUAGE;
	}
	public void ChangeLang(String Language){
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement("UPDATE zeus_char_config SET language = ? WHERE idchar = ?");
			ins.setString(1,Language);
			ins.setInt(2, idPlayer);
			try{
				ins.executeUpdate();
				this.LANGUAGE = Language;
			}catch(SQLException e){

			}
		}catch(SQLException a){

		}
		try{
			con.close();
		}catch(SQLException a){

		}		
	}	
}
