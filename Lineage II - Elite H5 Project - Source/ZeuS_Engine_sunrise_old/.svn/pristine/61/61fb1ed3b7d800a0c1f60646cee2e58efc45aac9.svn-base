package ZeuS.procedimientos;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class jMail {
	
	private static Map<Integer, jEmail> EMAILS_DATA = new HashMap<Integer, jEmail>();
	
	private static Map<Integer, String> tempLangFolder = new HashMap<Integer, String>();
	
	private static String SUBJECT_REGISTER;
	private static String SUBJECT_CHANGE_PASSWORD;
	private static String SUBJECT_NOTIFICATION_DONA;
	private static String SUBJECT_READY_DONA;
	private static String SUBJECT_FAIL_SECONDARY_PASS;
	private static String SUBJECT_RECOVERY_ACCOUNT;
	private static String SUBJECT_ACCOUNT_INFORMATION;
	private static int MINUTE_EMAIL_FAIL_SP = 2;
	
	private static String SERVER_NAME;
	private static String ADMIN_EMAIL;
	
	private static String WEB_GET_NAME_TYPE;
	private static String WEB_FOR_VALIDATED;
	private static String WEB_LINK_KEY;
	private static String WEB_GET_NAME_FOR_ID_DONATION;
	private static String WEB_GET_NAME_FOR_KEY;
	
	private final static Logger _log = Logger.getLogger(jMail.class.getName());
	
	public static void Load(){
		try{
			Properties propZ = new Properties();
			propZ.load(new FileInputStream(new File("./config/zeus/zeus_email.properties")));
			SUBJECT_REGISTER = String.valueOf(propZ.getProperty("RegisterSubject"));
			SUBJECT_CHANGE_PASSWORD = String.valueOf(propZ.getProperty("ChangePasswordSubject"));
			SUBJECT_NOTIFICATION_DONA = String.valueOf(propZ.getProperty("DonationNotification"));
			SUBJECT_READY_DONA = String.valueOf(propZ.getProperty("DonationNotificationReady"));
			SUBJECT_FAIL_SECONDARY_PASS = String.valueOf(propZ.get("FailSecondaryPassword"));
			SUBJECT_RECOVERY_ACCOUNT = String.valueOf(propZ.get("RecoveryAccount"));
			SUBJECT_ACCOUNT_INFORMATION = String.valueOf(propZ.get("RecoveryAccountChange"));
			WEB_FOR_VALIDATED = String.valueOf(propZ.getProperty("WebForValidate"));
			WEB_LINK_KEY = String.valueOf(propZ.getProperty("WebDonationLinkKey"));
			WEB_GET_NAME_FOR_ID_DONATION = String.valueOf(propZ.getProperty("WebGetCommandName_for_ID_DONATION"));
			WEB_GET_NAME_FOR_KEY = String.valueOf(propZ.getProperty("WebGetCommandName_for_ID_KEY"));
			WEB_GET_NAME_TYPE = String.valueOf(propZ.getProperty("WebGetNameType"));
			SERVER_NAME = String.valueOf(propZ.getProperty("ServerName","ZeuS"));
			ADMIN_EMAIL = String.valueOf(propZ.getProperty("AdminEmail",""));
			MINUTE_EMAIL_FAIL_SP = Integer.valueOf(propZ.getProperty("MinutesToSend"));
			
		}catch(Exception a){
			_log.warning(":::::::: ERROR EMAIL LOADING ::::::::::");
			_log.warning(a.getMessage());
		}
		
		try{
			getAllEmail();
		}catch(Exception a){
			
		}
	}
	
	public static int MinutesSend(){
		return MINUTE_EMAIL_FAIL_SP;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static void sendFailEntrySecondaryPassword(String Folder, HashMap<Integer, HashMap<String,String>> Failed, String Account, String Email){
		String Formato = "<tr><td width=\"200px\">%ACCOUNT%</td><td width=\"200px\">%DATE%</td><td width=\"200px\"><span style=\"TEXT-TRANSFORM: none;\">%PASS%</span></td></tr>";
		Iterator itr = Failed.entrySet().iterator();
		String Parte = "";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int UnixTime = (int)Entrada.getKey();
	    	Map<String, String>_Data = (Map<String, String>) Entrada.getValue();
	    	Parte += Formato.replace("%PASS%", _Data.get("PASSWORD")) .replace("%DATE%", _Data.get("TIME")) .replace("%ACCOUNT%", _Data.get("ACCOUNT"));
	    }
	    
	    final NpcHtmlMessage html = comun.htmlMaker(null, "config/zeus/mail/"+ Folder +"/sendFailEntrySecondaryPass.html");
    	html.replace("%TABLE%",Parte);	    
    	try{
    		sendFromMail(Email , html.getHtml(), SUBJECT_FAIL_SECONDARY_PASS);
    	}catch(Exception a){
    		_log.warning("Error sending email for Donation Reward to Player Account->" + Account);
    	}	    
	}
	
	private static String getFolderAccount(String Account){
		Connection conn = null;
		String LanguageFolder = "eng";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "SELECT zeus_char_config.language FROM characters INNER JOIN zeus_char_config ON characters.charId = zeus_char_config.idchar WHERE characters.account_name = ?";
			PreparedStatement psqry = conn.prepareStatement(qry);
			psqry.setString(1, Account);
			ResultSet rss = psqry.executeQuery();
				while (rss.next()){
					try{
						LanguageFolder = rss.getString(1);
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
		return LanguageFolder;
	}
	
	public static boolean sendDonationPaypal(L2PcInstance player) {
		String IdForMd5 = String.valueOf(opera.getUnixTimeL2JServer()) + String.valueOf(player.getObjectId());
		String Encript = opera.toMD5(IdForMd5);
		String Encript2 = opera.toMD5(IdForMd5 + player.getName());
		String InsertQry = "INSERT INTO zeus_paypal(idweb, gameEmail, gameAccount, createTime, createTimeLimit) VALUES (?,?,?,?,?)";
		String LinkToSend = general.PAYPAL_LINK_USE.equalsIgnoreCase("WEB") ? general.PAYPAL_LINK_WEB + "?IDW=" + Encript + "&INM=DONATION&INB=" + Encript2 : general.PAYPAL_LINK_EMAIL.replace("%ID_WEB_DONATION%", Encript);
		int TimeBegin = opera.getUnixTimeNow();
		int TimeEnds = TimeBegin + general.PAYPAL_SECONDS_WAITING_TIME;
		Connection con = null;
		PreparedStatement ins = null;
		boolean sendEmail = true;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(InsertQry);
			ins.setString(1, Encript);
			ins.setString(2, opera.getUserMail(player.getAccountName()));
			ins.setString(3, player.getAccountName());
			ins.setInt(4, TimeBegin);
			ins.setInt(5, TimeEnds);
			try{
				ins.executeUpdate();
				sendEmail = true;
			}catch(SQLException e){
				_log.warning("Error on Saving Donation Information from ->" + player.getName() + ": " + e.getMessage());
				sendEmail = false;
			}
		}catch(SQLException a){
			_log.warning("Error on Saving Donation Information (2) from ->" + player.getName() + ": " + a.getMessage());
			sendEmail = false;
		}
		if(!sendEmail) {
			central.msgbox("Cant send the PayPal Link. Please Communicate With the Admin", player);
			return false;
		}
		
	    final NpcHtmlMessage html = comun.htmlMaker(null, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/sendPaypal.html");
    	html.replace("%LINKWEB%", LinkToSend);
    	try{
    		central.msgbox_Lado(language.getInstance().getMsg(player).DONATION_PAYPAL_SENDING_LINK_TO_YOUR_EMAIL.replace("$email", opera.getUserMail(player.getAccountName())), player, "Donation");
    		sendFromMail(opera.getUserMail(player.getAccountName()) , html.getHtml(), SUBJECT_NOTIFICATION_DONA);
    	}catch(Exception a){
    		central.msgbox_Lado("We had an error sending Donation Link. Please Contact to your Server Admin.", player, "Donation");
    		_log.warning("Error sending email for Donation Reward to Player Account->" + player.getAccountName() + " ( " + player.getName() + " )");
    	}		
		
		return true;
	}
	
	public static boolean sendDonationReady(String Email, int Amount, String Account){
		boolean retorno = true;
		String Folder = getFolderAccount(Account);
		final NpcHtmlMessage html = comun.htmlMaker(null, "config/zeus/mail/"+ Folder +"/sendDonationReady.html");
    	html.replace("%SERVER%", SERVER_NAME);
    	html.replace("%MOUNT%", String.valueOf(Amount));
    	html.replace("%ACCOUNT%", Account);
    	try{
    		sendFromMail(Email , html.getHtml(), SUBJECT_READY_DONA);
    	}catch(Exception a){
    		_log.warning("Error sending email for Donation Reward to Player Account->" + Account);
    		retorno = false;
    	}
    	return retorno;
	}	
	
	public static boolean sendDonationInformation(L2PcInstance player, String idDona, String Method, String Amount, String Comment){
		boolean retorno = true;
		
		if(tempLangFolder!=null){
			if(tempLangFolder.size()==0){
				tempLangFolder.put(player.getObjectId(), language.getInstance().getFolder(player));
			}else if(!tempLangFolder.containsKey(player.getObjectId())){
				tempLangFolder.put(player.getObjectId(), language.getInstance().getFolder(player));
			}
		}else{
			tempLangFolder.put(player.getObjectId(), language.getInstance().getFolder(player));
		}
		
		int idServer = opera.getServerID(player);
		
		String LinkForAdmin = WEB_FOR_VALIDATED + "?"+ WEB_GET_NAME_TYPE +"=ACTIVE_DONATION&" + WEB_GET_NAME_FOR_KEY + "=" + WEB_LINK_KEY + "&" + WEB_GET_NAME_FOR_ID_DONATION + "=" + idDona + "&ZEUS_ID_SERVER=" + String.valueOf(idServer);
		
		NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/sendDonationInfo.html");
    	html.replace("%ADMIN_LINK%", "");
    	html.replace("%COMMENT%", Comment);
    	html.replace("%MOUNT%", Amount);
    	html.replace("%DONATION_METHOD%", Method.replace("_", " "));
    	html.replace("%ACCOUNT%", player.getAccountName());
    	html.replace("%SERVER%", SERVER_NAME);
    	try{
    		sendFromMail(opera.getUserMail(player.getAccountName()) , html.getHtml(), SUBJECT_NOTIFICATION_DONA);
    	}catch(Exception a){
    		_log.warning("Error sending email for Donation notification to Player idDona->" + idDona + ", player->" + player.getName());
    		retorno = false;
    	}

    	html = comun.htmlMaker(player, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/sendDonationInfo.html");
    	
    	html.replace("%ADMIN_LINK%", LinkForAdmin);
    	html.replace("%COMMENT%", Comment);
    	html.replace("%MOUNT%", Amount);
    	html.replace("%DONATION_METHOD%", Method.replace("_", " "));
    	html.replace("%ACCOUNT%", player.getAccountName());
    	html.replace("%SERVER%", SERVER_NAME);
    	try{
    		sendFromMail(ADMIN_EMAIL , html.getHtml(), SUBJECT_NOTIFICATION_DONA + " " + opera.getUnixTimeNow());
    	}catch(Exception a){
    		_log.warning("Error sending email for Donation notification to Admin idDOna->" + idDona + ", player->" + player.getName());
    		retorno = false;
    	}    	
    	
    	return retorno;
	}
	
	public static void sendRegisterWindows(L2PcInstance player, String EmailTo, String Code){
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/sendCodeAccount.html");
    	html.replace("%CODE%", Code);
    	try{
    		sendFromMail(EmailTo, html.getHtml(), SUBJECT_REGISTER);
    	}catch(Exception a){
    		_log.warning("Error sending Email Registration to " + player.getName() + "->" + a.getMessage());
    	}
	}
	
	public static void sendCodeForChangePasswordWindows(L2PcInstance player, String EmailTo, String Code){
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/sendCodeForChangePassword.html");
    	html.replace("%CODE%", Code);
    	try{
    		sendFromMail(EmailTo, html.getHtml(), SUBJECT_CHANGE_PASSWORD);
    	}catch(Exception a){
    		_log.warning("Error sending Email Change Password to " + player.getName() + "->" + a.getMessage());
    	}
	}
	
	@SuppressWarnings("rawtypes")
	public static void sendRecoveryInformation(L2PcInstance player, Map<String, HashMap<String, String>> AccountsInfo, String PassNoEncript){
		
		String Email = opera.getUserMail(player.getAccountName());
		String Grilla = "<tr>"+
            	"<td width=\"200px\"><span style=\"text-transform:none;color:#6FC\">%ACCOUNT%</span></td>"+
				"<td width=\"200px\"><span style=\"text-transform:none;color:#6FC\">%COUNT%</span></td>"+
               	"<td width=\"200px\"><span style=\"text-transform:none;color:#6FC\">%NEW_PASSWORD%</span></td></tr>";
		
		String SendTable = "";
		Iterator itr = AccountsInfo.entrySet().iterator();
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	String Account = (String)Entrada.getKey();
	    	int CountChar = Integer.valueOf(AccountsInfo.get(Account).get("CHARS"));
	    	SendTable += Grilla.replace("%NEW_PASSWORD%", PassNoEncript) .replace("%COUNT%", String.valueOf(CountChar)) .replace("%ACCOUNT%", Account);
	    }		
		
	    final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/accountRecovery.html");
    	html.replace("%TABLE%", SendTable);
    	try{
    		sendFromMail(Email, html.getHtml(), SUBJECT_ACCOUNT_INFORMATION);
    	}catch(Exception a){
    		_log.warning("Error sending Email Recovery Info to " + player.getName() + "->" + a.getMessage());
    	}	    
	    
	}
	
	public static void sendCodeForRecovery(L2PcInstance player, String EmailTo, String Code){
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/mail/"+ language.getInstance().getFolder(player) +"/sendCodeRecovery.html");
    	html.replace("%CODE%", Code);
    	try{
    		sendFromMail(EmailTo, html.getHtml(), SUBJECT_RECOVERY_ACCOUNT);
    	}catch(Exception a){
    		_log.warning("Error sending Email Recovery to " + player.getName() + "->" + a.getMessage());
    	}
	}
	
	
	@SuppressWarnings({ "rawtypes", "unused" })
	private static void sendFromMail(String EmailFor, String HTML_BODY, String Subject){
		
		if(EMAILS_DATA ==null){
			_log.warning("Cant Send Email becouse the Email XML is Empty or u dont have any Enabled data, please check zeus_email_data_setting.xml file on zeus config");
			return;
		}else if(EMAILS_DATA.size()==0){
			_log.warning("Cant Send Email becouse the Email XML is Empty or u dont have any Enabled data, please check zeus_email_data_setting.xml file on zeus config");
			return;
		}
		boolean EmailSend = false;
		Iterator itr = EMAILS_DATA.entrySet().iterator();
	    while(itr.hasNext() && !EmailSend){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	int IdEmail = (int)Entrada.getKey();
	    	jEmail DataEmail = (jEmail)Entrada.getValue();
	    	EmailSend = DataEmail.sendEmail(EmailFor, HTML_BODY, Subject);
	    }
	}
	
	public static void getAllEmail(){
		
		try{
			if(EMAILS_DATA != null){
				EMAILS_DATA.clear();
			}else if(EMAILS_DATA.size()>0){
				EMAILS_DATA.clear();
			}
		}catch(Exception a){
			
		}
		
		File dir = new File("./config/zeus/zeus_email_data_setting.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);			
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("email")){
							int ID_EMAIL = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							boolean isEnabled = Boolean.valueOf(d.getAttributes().getNamedItem("enabled").getNodeValue());
							if(isEnabled){
								String smtp_maildress = "", smtp_host = "", smtp_user = "", smtp_pass = "", smtp_socket_class = "";
								String smtp_auth = "false";
								String smtp_port = "0", smtp_socket_port = "0";
								boolean isTTL = false;
								for(Node dd = d.getFirstChild(); dd != null; dd = dd.getNextSibling()){
									if(dd.getNodeName().equalsIgnoreCase("set")){
										String Config = dd.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
										String ValConfig = dd.getAttributes().getNamedItem("value").getNodeValue();
										switch(Config){
											case "istls":
												isTTL = Boolean.valueOf(ValConfig);
												break;
											case "smtp_host":
												try{
													smtp_host = ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_auth":
												try{
													smtp_auth = ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_port":
												try{
													smtp_port = ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_user":
												try{
													smtp_user = ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_pass":
												try{
													smtp_pass= ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_socket_port":
												try{
													smtp_socket_port = ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_socket_class":
												try{
													smtp_socket_class = ValConfig;
												}catch(Exception a){
													
												}
												break;
											case "smtp_maildress":
												try{
													smtp_maildress = ValConfig;
												}catch(Exception a){
													
												}
												break;										
										}
										
									}
								}
								jEmail T1 = new jEmail(ID_EMAIL, smtp_host, smtp_auth, smtp_port, smtp_user, smtp_pass, smtp_socket_port, smtp_socket_class, smtp_maildress, isTTL);
								EMAILS_DATA.put(ID_EMAIL, T1);
							}
						}
					}
				}
			}
			
		}catch(Exception a){
			_log.warning("Error loading Mail Data from zeus_email_data_setting.xml");
		}		
	}
	
	private static class jEmail{
		private String _SMTP_HOST;
		private String _SMTP_AUTH;
		private String _SMTP_PORT;
		private String _SMTP_USER;
		private String _SMTP_PASSWORD;
		private String _SOCKET_PORT;
		private String _SOCKET_CLASS;
		private String _SMTP_EMAIL_ADRESS;
		private boolean USING_TTL;
		private int ID;
		public String getHost(){
			return _SMTP_HOST;
		}
		public String getAuth(){
			return _SMTP_AUTH;
		}
		public String getPort(){
			return _SMTP_PORT;
		}
		public String getUser(){
			return _SMTP_USER;
		}
		public String getPass(){
			return _SMTP_PASSWORD;
		}
		public String getSocketPort(){
			return _SOCKET_PORT;
		}
		public String getSocketClass(){
			return _SOCKET_CLASS;
		}
		public String getEmailAdress(){
			return _SMTP_EMAIL_ADRESS;
		}
		
		@SuppressWarnings("unused")
		public int getID(){
			return ID;
		}
		public jEmail(int _Id, String _host, String _auth, String _port, String _user, String _pass, String _socketPort, String _socketClass, String _emailAdress, boolean TTL){
			ID = _Id;
			_SMTP_HOST = _host;
			_SMTP_AUTH = _auth;
			_SMTP_PORT = _port;
			_SMTP_USER = _user;
			_SMTP_PASSWORD = _pass;
			_SOCKET_PORT = _socketPort;
			_SOCKET_CLASS = _socketClass;
			_SMTP_EMAIL_ADRESS = _emailAdress;
			USING_TTL = TTL;
		}
		
		public boolean sendEmail(String EmailFor, String HTML_BODY, String Subject){
			boolean retorno = true;
			Properties props = System.getProperties();
			if(USING_TTL){
				props.put("mail.smtp.starttls.enable", "true");
		        props.put("mail.smtp.user", getUser());
		        props.put("mail.smtp.password", getPass());
		        props.put("mail.smtp.mail.sender",getEmailAdress());
			}	    	
			props.put("mail.smtp.host", getHost());
			props.put("mail.smtp.socketFactory.port", getSocketPort());
			props.put("mail.smtp.socketFactory.class",getSocketClass() );
			props.put("mail.smtp.auth", getAuth());
			props.put("mail.smtp.port", getPort());
			props.put("mail.smtp.debug", "false");
			props.put("mail.debug", "false");
			
			Session session = Session.getDefaultInstance(props,
					new javax.mail.Authenticator() {
						protected PasswordAuthentication getPasswordAuthentication() {
							return new PasswordAuthentication(getUser(), getPass());
						}
					});
			session.setDebug(false);
			session.setDebugOut(null);
			try {
				Message message = new MimeMessage(session);
				message.setFrom(new InternetAddress( getEmailAdress() ));
				message.setRecipients(Message.RecipientType.TO,
						InternetAddress.parse(EmailFor));
				message.setSubject(Subject);
				message.setContent(HTML_BODY,"text/html");
				Transport.send(message);
				try{
					session = null;
					message = null;
					props = null;
				}catch(Exception a){
					
				}
			}catch (MessagingException e) {
				_log.warning("SMTP ERROR->" + e.getMessage() + "<- Please check the Email Setting ID (" + ID + ")." );
				retorno=false;
			}			
			
			return retorno;
		}
		
	}
	
}


