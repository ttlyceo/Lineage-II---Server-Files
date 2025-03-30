package ZeuS.interfase;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import ZeuS.language.language;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class borrowAccount {
	private final Logger _log = Logger.getLogger(borrowAccount.class.getName());
	private Map<String, String> ACCOUNT_DATA_BORROW = new HashMap<String, String>();
	private Map<String, Boolean> BORROW_ON = new HashMap<String, Boolean>();
	private Map<String, Boolean> BORROW_ON_GM = new HashMap<String, Boolean>();
	
	public boolean isBorrowAccount(String _Account) {
		String Password = "";
		boolean isBorrowAccount = false;
		String Qry = "SELECT * FROM zeus_borrow_account WHERE account=?";
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Qry)){
			statement.setString(1, _Account);
			ResultSet Resul = statement.executeQuery();
			while(Resul.next()){
				isBorrowAccount = true;
				Password = Resul.getString("borrowPass");
			}
		}catch(Exception a){
			
		}
		
		ACCOUNT_DATA_BORROW.put(_Account, Password);
		
		return isBorrowAccount;
	}
	
	private boolean isBorrowAccount_GM(String _Account) {
		boolean _isBorrowAccountGM = false;
		String Qry = "SELECT isInUse FROM zeus_borrow_account WHERE account=?";
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Qry)){
			statement.setString(1, _Account);
			ResultSet Resul = statement.executeQuery();
			while(Resul.next()){
				if(Resul.getString("isInUse").equalsIgnoreCase("GM")) {
					_isBorrowAccountGM = true;
				}
				
			}
		}catch(Exception a){
			_log.warning("Error on Borrow Account GM -> " + a.getMessage());
		}
		return _isBorrowAccountGM;
	}	
	
	private void setBorrowON(String _account) {
		if(isBorrowAccount_GM(_account)) {
			return;
		}
		String Qry = "UPDATE zeus_borrow_account SET isInUse = ? WHERE account=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(Qry);
			psqry.setString(1, "Y");
			psqry.setString(2, _account);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
				BORROW_ON.put(_account, true);
			}catch (SQLException e){
				_log.warning("Error Setting Borrow is ON, Account (" + _account + ") in BD ->" + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error Setting Borrow is ON, Account (" + _account + ") in BD ->" + a.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a){

		}		
	}
	
	public boolean isborrowAccountWithPassword(String _Account, String _Password) {
		boolean loadDataAccount = false;
		boolean isBorrowAccount = false;
		if(ACCOUNT_DATA_BORROW == null) {
			loadDataAccount = true;
		}else if(ACCOUNT_DATA_BORROW.size()==0) {
			loadDataAccount = true;
		}else if(ACCOUNT_DATA_BORROW.containsKey(_Account)) {
			loadDataAccount = true;
		}
		String PassEncript = getEncriptedPass(_Password);
		boolean Retorno = false;
		if(loadDataAccount){
			Retorno = ACCOUNT_DATA_BORROW.get(_Account).equals(PassEncript);
		}else {
			String Qry = "SELECT * FROM zeus_borrow_account WHERE account=?";
			String PasswordBD = "";
			try(Connection con = L2DatabaseFactory.getInstance().getConnection();
				PreparedStatement statement = con.prepareStatement(Qry)){
				statement.setString(1, _Account);
				ResultSet Resul = statement.executeQuery();
				while(Resul.next()){
					isBorrowAccount = true;
					PasswordBD = Resul.getString("borrowPass");
				}
			}catch(Exception a){
				
			}
			if(isBorrowAccount) {
				Retorno = PasswordBD.equals(PassEncript);
			}
		}
		if(Retorno) {
			setBorrowON(_Account);			
		}
		return Retorno;
	}
	
	public boolean isBorrowActice(String _Account) {
		try {
			return BORROW_ON.get(_Account);
		}catch(Exception a) {
			
		}
		return false;
	}
	
	public void getBorrowData(L2PcInstance player) {
		boolean hadData = false;
		String Qry = "SELECT isInUse, borrowPass, isInUse FROM zeus_borrow_account WHERE account=?";
		try(Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement(Qry)){
			statement.setString(1, player.getAccountName());
			ResultSet Resul = statement.executeQuery();
			while(Resul.next()){
				BORROW_ON.put(player.getAccountName(), ( Resul.getString("isInUse").equalsIgnoreCase("y") ? true : false));
				ACCOUNT_DATA_BORROW.put(player.getAccountName(), Resul.getString("borrowPass"));
				BORROW_ON_GM.put(player.getAccountName(), ( Resul.getString("isInUse").equalsIgnoreCase("GM") ? true : false ));
				hadData = true;
			}
		}catch(Exception a){
			
		}			

		if(!hadData) {
			try {
				ACCOUNT_DATA_BORROW.remove(player.getAccountName());
			}catch(Exception a) {
				
			}
			
			try {
				BORROW_ON.remove(player.getAccountName());
			}catch(Exception a) {
				
			}			
		}
	}
	
	public void setNewBorrowPassword(L2PcInstance player, String PasswordLoan) {
		try {
			String accountName = player.getAccountName();
			String EncriptPass = getEncriptedPass(PasswordLoan);
			recordDataInDB(accountName, EncriptPass, null);
		}catch(Exception a) {
			_log.warning("New Borrow Password Error: " + a.getMessage());
		}
	}
	
	public void setNewBorrowPassword(L2PcInstance playerGM, String PasswordLoan, L2PcInstance TargetPlayer) {
		try {
			String accountName = TargetPlayer.getAccountName();
			String EncriptPass = getEncriptedPass(PasswordLoan);
			recordDataInDB(accountName, EncriptPass, playerGM);
			central.msgbox(language.getInstance().getMsg(TargetPlayer).BORROW_SYSTEM_ADM_$name_HAS_SET_BORROWED_PASSWORD.replace("$name", playerGM.getName()), TargetPlayer);
			NpcHtmlMessage html = comun.htmlMaker(playerGM, "./config/zeus/htm/" + language.getInstance().getFolder(playerGM) + "/borrowSystem-done.html");
			html.replace("%PASSWORD%", PasswordLoan);
			central.sendHtml(playerGM, html);				
		}catch(Exception a) {
			_log.warning("New Borrow Password Error: " + a.getMessage());
		}
	}	
	
	private String getEncriptedPass(String _Pass) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA");
			byte[] password = _Pass.getBytes("UTF-8");
			password = md.digest(password);
			String EncriptPass = Base64.getEncoder().encodeToString(password);
			return EncriptPass;
		}catch(Exception a) {
			
		}
		return "";
	}
	
	private void recordDataInDB(String _Account, String _PasswordEncript, L2PcInstance player) {
		boolean isGM = false;
		if(player != null) {
			isGM = player.isGM();
		}
		String InsertQry = "INSERT INTO zeus_borrow_account(account, borrowPass, isInUse) VALUES(?,?,?) ON DUPLICATE KEY UPDATE borrowPass=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		try{
			conn=L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(InsertQry);
			psqry.setString(1, _Account);
			psqry.setString(2, _PasswordEncript);
			psqry.setString(3, (isGM ? "GM" : "N"));
			psqry.setString(4, _PasswordEncript);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e){
				_log.warning("Error saving Borrow Account in BD ->" + e.getMessage());
			}
		}catch(SQLException a){
			_log.warning("Error saving Borrow Account in BD ->" + a.getMessage());
		}
		try{
			conn.close();
		}catch(SQLException a){

		}		
	}
	
	public void removeBorrow(String Account) {
		try {
			ACCOUNT_DATA_BORROW.remove(Account);
		}catch(Exception a) {
			
		}
		
		try {
			BORROW_ON.remove(Account);
		}catch(Exception a) {
			
		}
		
		String consulta = "DELETE FROM zeus_borrow_account WHERE account = ?";
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(consulta);
			ins.setString(1, Account);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}		
		
	}

	public static borrowAccount getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final borrowAccount _instance = new borrowAccount();
	}
	
}
