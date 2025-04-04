package ZeuS.procedimientos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.sql.CharNameTable;
import l2r.gameserver.idfactory.IdFactory;
import l2r.gameserver.instancemanager.MailManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Message;
import l2r.gameserver.model.events.EventDispatcher;
import l2r.gameserver.model.events.impl.character.player.inventory.OnPlayerItemTransfer;
import l2r.gameserver.model.itemcontainer.ItemContainer;
import l2r.gameserver.model.itemcontainer.Mail;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ExNoticePostSent;

public class EmailSend {
	
	private static final Logger _log = Logger.getLogger(EmailSend.class.getName());
	
	private static EmailSend _instance;	
	
	private static final int EXPIRATION_DAYS = 25;
	
	public enum tipoMensaje{
		Annoucement,
		Donation,
		Event,
		Gift,
		Special,
		Reward,
		Critical,
		Account,
		Password,
		Gathering,
		Promotions,
		Other,
		Survey,
		Vote,
		Auction_house,
		Bid_house,
		Bug_Report_To_Admin,
		Bug_Report_To_User_With_Answer,
		TownWarEventTopReward,
		Wishlist
	}
	
	public static String getMessageById(String idMsn){
		switch (idMsn)
		{
			case "-30000001":
				return "*Annoucement*";
			case "-30000002":
				return "*Donation*";
			case "-30000003":
				return "*Event*";
			case "-30000004":
				return "*Gift*";
			case "-30000005":
				return "*Special*";
			case "-30000006":
				return "*Reward*";
			case "-30000007":
				return "*Critical*";
			case "-30000008":
				return "*Account*";
			case "-30000009":
				return "*Password*";
			case "-30000010":
				return "*Vote*";				
			case "-30000011":
				return "*Gathering*";
			case "-30000012":
				return "*Promotions*";
			case "-30000013":
				return "*Other*";
			case "-30000014":
				return "*Survey*";
			case "-30000015":
				return "*Auction House*";
			case "-30000016":
				return "*Bid House*";
			case "-30000017":
				return "*New Bug Report*";
			case "-30000018":
				return "*Bug Report*";
			case "-30000019":
				return "*Town War Event*";
			case "-30000020":
				return "*Wishlist*";
				
		}
		return idMsn;
	}
	
	private static int getIdMessage(tipoMensaje tipo){
		switch(tipo){
			case Annoucement:
				return -30000001;
			case Donation:
				return -30000002;
			case Event:
				return -30000003;
			case Gift:
				return -30000004;
			case Special:
				return -30000005;
			case Reward:
				return -30000006;
			case Critical:
				return -30000007;
			case Account:
				return -30000008;
			case Password:
				return -30000009;
			case Vote:
				return -30000010;
			case Gathering:
				return -30000011;
			case Promotions:
				return -30000012;
			case Other:
				return -30000013;
			case Survey:
				return -30000014;
			case Auction_house:
				return -30000015;
			case Bid_house:
				return -30000016;
			case Bug_Report_To_Admin:
				return -30000017;
			case Bug_Report_To_User_With_Answer:
				return -30000018;
			case TownWarEventTopReward:
				return -30000019;
			case Wishlist:
				return -30000020;
		}
		return 0;
	}
	
	public static void sendAllPlayerAccountInfoDonation(String AccountPpl, int Amounts){
		String title = "Donation Item";
		String Messege = "Your Donations Pounds are ready, please use command .donate to claim them.";
		String Qry = "select characters.char_name, characters.charId from characters where characters.account_name = ?";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Qry);
			psqry.setString(1, AccountPpl);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						PsendEmail(null,rss.getInt(2),title,Messege,tipoMensaje.Donation,"");
					}catch(SQLException e){
						_log.warning("Error on Game Email sender by Donation->" + e.getMessage());
					}
				}
				conn.close();
			}catch(SQLException a){
				_log.warning("Error on Game Email sender by Donation->->" + a.getMessage());
			}
		try{
			conn.close();
		}catch (Exception e) {

		}		

		Qry = "update zeus_dona_creditos set emailed='true' where cuenta = ?";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Qry);
			psqry.setString(1, AccountPpl);
			psqry.executeUpdate();
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}		
		
		String EmailAccount = opera.getUserMail(AccountPpl);
		jMail.sendDonationReady(EmailAccount, Amounts, AccountPpl);
		
	}
	
	public static void sendBugReportToAdmin(L2PcInstance player){
		String Men = "You have a new Bug Report from " + player.getName() + ".";
		for(String prt : general.STAFF_DATA.split(":")){
			int idChar = Integer.valueOf(prt.split(";")[0]);
			PsendEmail(player, idChar, "Bug Report", Men, tipoMensaje.Bug_Report_To_Admin, "");			
		}
	}
	
	public static void sendBugReportAnswerToPlayer(L2PcInstance playerAdmin, String NamePlayer, String Answer){
		String Men = Answer;
		sendEmail(playerAdmin, NamePlayer, "Bug Report Answer", Men, "", tipoMensaje.Bug_Report_To_User_With_Answer);
	}	
	
	public static void sendPasswordWrogPass(L2PcInstance player, String Pass){
		String Men = language.getInstance().getMsg(player).EMAIL_SOMEONE_WROTE_WRONG_YOUR_SECONDARY_PASSWORD.replace("$pass", Pass);
		PsendEmail(player,player.getObjectId(),"Secundary Password Alert",Men,tipoMensaje.Account,"");		
	}
	
	public static void sendNewPasswordSellAccount(L2PcInstance player, String Pass, String Account){
		String Men = language.getInstance().getMsg(player).EMAIL_SELL_ACCOUNT_INFO.replace("$account",Account).replace("$pass", Pass);
		PsendEmail(null,player.getObjectId(),"Sell account Successful",Men,tipoMensaje.Account,"");		
	}	
	
	public static tipoMensaje getIdMessage(String typeSend) {
		return tipoMensaje.valueOf(typeSend);
	}
	
	public static void sendAnnoucementMail(L2PcInstance player,String Title, String Message, String Items, String ReceiverName){
		int IdReceiver = CharNameTable.getInstance().getIdByName(ReceiverName);
		PsendEmail(player, IdReceiver, Title, Message, tipoMensaje.Annoucement,"");
	}
	
	public static void sendSurveyMail(String Title, String Message, int IdReceiver){
		//int IdReceiver = CharNameTable.getInstance().getIdByName(ReceiverName);
		PsendEmail(null, IdReceiver, Title, Message, tipoMensaje.Survey,"");
	}	
	
	public static void sendEmail(L2PcInstance player, String ReceiverName, String Title, String Message, String Items, tipoMensaje TypeMessage){
		try{
			int IdReceiver = CharNameTable.getInstance().getIdByName(ReceiverName);
			PsendEmail(player,IdReceiver,Title,Message,TypeMessage,Items);
		}catch(Exception a){
			central.msgbox("Error sending email to:" + ReceiverName + " ->", player);
		}
	}
	
	private static int getNewIDObject(){
		int _objectId = IdFactory.getInstance().getNextId();
		return _objectId;
	}	
	
	@SuppressWarnings("unused")
	private static void setCreateNewItemInBD(int ID_OWNER, int IdObject, int itemID, Long Count, int IdEmail){
		String Consulta = "insert into items(owner_id, object_id, item_id, count, enchant_level, loc, loc_data) values (?,?,?,?,?,?,?)";
		
		Connection con = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Consulta);
			ins.setInt(1, ID_OWNER);
			ins.setInt(2, IdObject);
			ins.setInt(3, itemID);
			ins.setLong(4, Count);
			ins.setInt(5, 0);
			ins.setString(6, "MAIL");
			ins.setInt(7, IdEmail);
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
	
	public static void preDonation(L2PcInstance player, String Items, String TextToSend){
		PsendEmail(null, player.getObjectId(), "Pre Donation Reward", TextToSend , tipoMensaje.Donation, Items);
	}
	
	
	@SuppressWarnings("unused")
	private static void PsendEmail(L2PcInstance player, int _Idplayer, String Subject, String Text, tipoMensaje tipomensaje, String Items){
		Message msg = new Message(getIdMessage(tipomensaje), _Idplayer, false, Subject, Text, 0, EXPIRATION_DAYS);
		if(Items.length()>0){
			try{
				Mail attachments = msg.createAttachments();
				int idMensaje = msg.getId();				
				for(String Ite : Items.split(";")){
					int IdItem = Integer.valueOf(Ite.split(",")[0]);
					int IdObject = getNewIDObject();
					long Mount = Long.valueOf(Ite.split(",")[1]);
					attachments.addItem("ZeuS Email",IdItem,Mount,null,null);
				}
			}catch(Exception a){
				_log.warning("Error on ZeuS EMail->" + a.getMessage());
			}
		}
		MailManager.getInstance().sendMessage(msg);
		if(player!=null){
			player.sendPacket(ExNoticePostSent.valueOf(true));
			player.sendPacket(SystemMessageId.MAIL_SUCCESSFULLY_SENT);
		}
	}
	
	public static void sendRewardForTopTownWarEvent(L2PcInstance player){
		if(general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER.length() >0){
			String title = "Top Killer, TownWar Event";
			String Msje = "Congratulations!";
			String Items = "";
			if(general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER.indexOf(";")>0){
				int Contt = 1;
				for(String Caden : general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER.split(";")){
					if(Contt <=5){
						if(Items.length()>0){
							Items += ";";
						}
						Items += Caden.split(",")[0] + "," + Caden.split(",")[1];
					}
					Contt++;
				}
			}else{
				Items = general.EVENT_TOWN_WAR_REWARD_TOP_PLAYER;
			}
			PsendEmail(null,player.getObjectId(),title,Msje,tipoMensaje.TownWarEventTopReward,Items);
		}
	}
	
	public static void bidSendItemToWinner(int idPlayerReceiver, int idObject, L2ItemInstance item, String Title, String Message){
		Message msg = new Message(getIdMessage( tipoMensaje.Bid_house ), idPlayerReceiver, false, Title, Message, 0, 25);
		item.setZeuSBidNewOwner( getIdMessage(tipoMensaje.Bid_house));
		try{
			Mail attachments = msg.createAttachments();
			attachments.addItem("ZeuS Email", item, null, null, true);
		}catch(Exception a){
			_log.warning("Error on ZeuS EMail->" + a.getMessage());
		}
		MailManager.getInstance().sendMessage(msg);
	}
	
	public static L2ItemInstance transferItem(L2PcInstance player, String process, int objectId, long count, ItemContainer target, L2PcInstance actor, Object reference)
	{
		L2ItemInstance item = player.getInventory().getItemByObjectId(objectId);
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerItemTransfer(actor, item, target), item.getItem());
		return item;
	}

	
	
	public static final EmailSend getInstance()
	{
		return _instance;
	}

}
