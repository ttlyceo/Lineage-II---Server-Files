package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class cambionombre {

	private static boolean freeName(L2PcInstance player, String NewName) {
		boolean retorno = true;
		
		if(NewName.trim().length()>16) {
			central.msgbox(language.getInstance().getMsg(player).YOU_NAME_HAD_MORE_THAN_16_CHARACTERS.replace("$new_name", NewName) , player);
			return false;
		}
		
		String Consulta = "SELECT characters.accesslevel FROM characters WHERE characters.char_name = ?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try {
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Consulta);
			psqry.setString(1, NewName.trim());
			ResultSet rs = psqry.executeQuery();
			if (rs.next()){
				retorno = false;
			}
		}catch(Exception a) {
			
		}
		
		try {
			conn.close();
		}catch(Exception a) {
			
		}
		
		return retorno;
	}
	
	public static boolean changeName_Char(L2PcInstance st,String NombreNuevo){
		if(!opera.isValidName(NombreNuevo)){
			central.msgbox(language.getInstance().getMsg(st).THE_NAME_ENTERED_IS_NOT_VALID,st);
			return false;
		}
		
		if(freeName(st, NombreNuevo)){
			st.setName(NombreNuevo);
			st.store();
			central.msgbox (language.getInstance().getMsg(st).THE_NAME_CHANGE_WAS_SUCCESSFUL,st);
			st.broadcastUserInfo();
			return true;
		}
		central.msgbox(language.getInstance().getMsg(st).THE_NEW_NAME_ALREADY_EXISTS, st);
		return false;
	}
}
