package ZeuS.GM;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.EmailSend;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class preDonation {
	private static final Logger _log = Logger.getLogger(preDonation.class.getName());
	
	public static void checkPreDonation(L2PcInstance player, String Email){
		havePreDonation(player, Email);
	}
	private static void havePreDonation(L2PcInstance player, String Email){
		String Consulta = "SELECT SUM(donationToGive) FROM zeus_pre_donation WHERE email = ? AND given = ?";
		boolean sendDonationCoin = false;
		int DonationToGive = 0;
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement st = con.prepareStatement(Consulta);
			st.setString(1, Email);
			st.setString(2, "NO");
			ResultSet rset = st.executeQuery();
			if(rset.next()){
				DonationToGive = rset.getInt(1);
				sendDonationCoin = true;
			}
			rset.close();
		}catch (Exception e){
				_log.warning("pre Donation Error: Error while Check the Predonation: " + e.getMessage() + ", email->" + Email);
		}
		
		boolean removeit = false;
		if(sendDonationCoin){
			if(DonationToGive>0){
				central.msgbox("Your email Have a Pre Donation Data. You will receive a Donation Email very Soon", player);
				String ItemToSend = String.valueOf(general.DONA_ID_ITEM) + "," + String.valueOf(DonationToGive);
				EmailSend.preDonation(player, ItemToSend, language.getInstance().getMsg(player).PRE_DONATION_TEXT);
				removeit = true;
			}else{
				_log.warning("Error on pre donation ->" + Email + " ("+ DonationToGive +")" );
			}
		}
		if(removeit){
			String SqlDelete = "UPDATE zeus_pre_donation SET given='YES' WHERE email=?";
			Connection con = null;
			PreparedStatement ins = null;
			try{
				con = L2DatabaseFactory.getInstance().getConnection();
				ins = con.prepareStatement(SqlDelete);
				ins.setString(1, Email);
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
	}
}