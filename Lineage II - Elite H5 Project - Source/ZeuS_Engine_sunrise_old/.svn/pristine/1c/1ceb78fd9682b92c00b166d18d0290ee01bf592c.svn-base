package ZeuS.Config;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import ZeuS.procedimientos.EmailSend;
import ZeuS.procedimientos.EmailSend.tipoMensaje;
import ZeuS.procedimientos.opera;

public class _donaGift {
	private int ID;	
	private int ID_CHAR;
	@SuppressWarnings("unused")
	private String BEGIN_DATE;
	private int TIMES;
	private int LASTGIFT_UNIX;
	@SuppressWarnings("unused")
	private String LASTGIFT_DATE;
	@SuppressWarnings("unused")
	private boolean CAN_RECEIVE_MORE;
	private final Logger _log = Logger.getLogger(_donaGift.class.getName());
	
	public _donaGift(int id, int IdPlayer, String BeginDate, int times, int LastUnix, String LastDate, boolean canReceive){
		this.ID = id;
		this.ID_CHAR = IdPlayer;
		this.BEGIN_DATE = BeginDate;
		this.TIMES = times;
		this.LASTGIFT_UNIX = LastUnix;
		this.LASTGIFT_DATE = LastDate;
		this.CAN_RECEIVE_MORE = canReceive;
		this.checkNextGift();
	}
	
	public int getID(){
		return this.ID;
	}
	
	private void checkNextGift(){
		if(!general.DONATION_EXTRA_GIFT_REPEAT){
			this.CAN_RECEIVE_MORE = false;
			this.setNoMore();
			return;
		}
		int TotalSecondWait = (general.DONATION_TEMP_EVERY_TYPE.equalsIgnoreCase("MINUTES") ? general.DONATION_EXTRA_GIFT_REPEAT_EVERY_X * 60 : general.DONATION_EXTRA_GIFT_REPEAT_EVERY_X * 60 * 60);
		int UnixEnd = this.LASTGIFT_UNIX + TotalSecondWait;
		int NowUnix = opera.getUnixTimeNow();
		int SecondLeft = UnixEnd - NowUnix;
		if(SecondLeft <= 0){
			this.SendRewardByEmail();
		}else{
			this.setNewReward(SecondLeft);
		}
	}
	
	public _donaGift(L2PcInstance player){
		this.ID_CHAR = player.getObjectId();
		this.insertIntoDB();
		if(this.ID > 0){
			this.getData();
			this.SendRewardByEmail();
		}else{
			_log.warning("Donation Gift: We got a error on getting ID from the New Row");
		}
	}
	
	@SuppressWarnings("rawtypes")
	private String getReward(){
		String _Return="";
		Iterator itr = general.DONATION_EXTRA_GIFT_ITEMS.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			int IdItem = (int)Entrada.getKey();
			int Cant = (int)Entrada.getValue();
			if(_Return.length()>0){
				_Return += ";";
			}
			_Return += String.valueOf(IdItem) + "," + String.valueOf(Cant);
		}
		
		return _Return;
	}
	
	private void SendRewardByEmail(){
		if(!general.DONATION_EXTRA_GIFT){
			return;
		}
		if(general.DONATION_EXTRA_GIFT_ITEMS==null){
			_log.warning("Donation Extra Gift Reward are Empty");
			return;
		}else if(general.DONATION_EXTRA_GIFT_ITEMS.size()==0){
			_log.warning("Donation Extra Gift Reward are Empty");
			return;
		}
		if(this.TIMES > general.DONATION_EXTRA_GIFT_TIMES){
			this.CAN_RECEIVE_MORE=false;
			return;
		}
		String RepiteType = general.DONATION_TEMP_EVERY_TYPE.equalsIgnoreCase("MINUTES") ? "Minutes" : "Hours";
		String Msgje = "This is a Extra Reward for Your Donation. Sent " + opera.getDateFromUnixTime(opera.getUnixTimeNow()) + ". These will be repeated every " + String.valueOf( general.DONATION_EXTRA_GIFT_REPEAT_EVERY_X + " " + RepiteType + " automatically. The Staff");
		String Name = opera.getPlayerNameById(this.ID_CHAR);
		EmailSend.sendEmail(null, Name, "Extra Reward N " + String.valueOf(this.TIMES + 1) + " from " + String.valueOf(general.DONATION_EXTRA_GIFT_TIMES), Msgje, this.getReward(), tipoMensaje.Donation);
		_log.info("Donation Gift: Sending Gift Number " + String.valueOf(this.TIMES + 1) + " to:" + Name);
		this.setLastTime();
	}
	
	private void setNewReward(int secondLeft){
		if(general.DONATION_EXTRA_GIFT_REPEAT){
			int Repite = general.DONATION_TEMP_EVERY_TYPE.equalsIgnoreCase("MINUTES") ? (general.DONATION_EXTRA_GIFT_REPEAT_EVERY_X * 60) : (general.DONATION_EXTRA_GIFT_REPEAT_EVERY_X * 60 * 60);
			ThreadPoolManager.getInstance().scheduleGeneral( new GiftClock() , ( secondLeft == 0 ? Repite : secondLeft ) * 1000);
		}
	}
	
	private void setLastTime(){
		this.TIMES++;
		String Qry = "";
		int UnixTimes = opera.getUnixTimeNow();
		String DateUnix = opera.getDateFromUnixTime(UnixTimes);
		if(this.TIMES >= general.DONATION_EXTRA_GIFT_TIMES){
			Qry = "UPDATE zeus_dona_gift SET canReceiveMore = 'false', times=?, lastTimeUnix=?, lastTime=? WHERE id=?";
		}else{
			Qry = "UPDATE zeus_dona_gift SET times=?, lastTimeUnix=?, lastTime=? WHERE id=?";
			setNewReward(0);
		}
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Qry);
			ins.setInt(1, this.TIMES);
			ins.setInt(2, UnixTimes);
			ins.setString(3, DateUnix);
			ins.setInt(4, this.ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}		
	}
	
	private void setNoMore(){
		String Qry = "UPDATE zeus_dona_gift SET canReceiveMore='false' WHERE id=?";
		try{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement ins = con.prepareStatement(Qry);
			ins.setInt(1, this.ID);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}		
	}

	private void getData(){
		String Qry = "SELECT * FROM zeus_dona_gift WHERE zeus_dona_gift.id=?";
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement psqry = conn.prepareStatement(Qry);
			psqry.setInt(1, this.ID);
			ResultSet rss = psqry.executeQuery();
			if(rss.next()){
				try{
					this.BEGIN_DATE = rss.getString(3);
					this.TIMES = rss.getInt(4);
					this.LASTGIFT_UNIX = rss.getInt(5);
					this.LASTGIFT_DATE = rss.getString(6);
					this.CAN_RECEIVE_MORE = ( rss.getString(7).equalsIgnoreCase("true") ? true : false );
				}catch(SQLException e){

				}
			}
			conn.close();
			}catch(SQLException e){

			}
		try{
			conn.close();
		}catch (Exception e) {

		}		
		
		
	}
	private void insertIntoDB(){
		int UnixNow = opera.getUnixTimeNow();
		String DateNow = opera.getDateFromUnixTime(UnixNow);		
		String Qr = "INSERT INTO zeus_dona_gift(idChar, lastTimeUnix, lastTime) VALUES(?,?,?)";
		Connection con = null;
		PreparedStatement ins = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(Qr, Statement.RETURN_GENERATED_KEYS);	
			ins.setInt(1, this.ID_CHAR );
			ins.setInt(2, UnixNow);
			ins.setString(3, DateNow);
			try{
				ins.executeUpdate();
				try (ResultSet rset = ins.getGeneratedKeys()){
					if (rset.next()){
						int CodigoDeVuelta = rset.getInt(1);
						this.ID = CodigoDeVuelta;
					}
				}
			}catch(SQLException e){
				
			}
		}catch(Exception a){
			
		}
	}
	
	public class GiftClock implements Runnable{
		public GiftClock(){
		}
		@Override
		public void run(){
			try{
				SendRewardByEmail();
			}catch(Exception a){

			}
		}
	}	
	
}
