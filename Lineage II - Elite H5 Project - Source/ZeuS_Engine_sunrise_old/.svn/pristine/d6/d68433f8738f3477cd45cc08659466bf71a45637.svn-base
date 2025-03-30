package ZeuS.language;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ZeuS.interfase.central;
import ZeuS.language.Lang.BUTTON;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class language {
	private final Logger _log = Logger.getLogger(language.class.getName());
	private Map<Integer, setting> SETT = new HashMap<Integer, setting>();
	private Map<String, Lang> FOLDER_LANG = new HashMap<String, Lang>();
	
	public void loadCentralLang(){
		try{
			FOLDER_LANG.clear();
		}catch(Exception a){
			
		}
		
		File dir = new File("./config/zeus/htm/language_setting.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("language")){
							String NameLang = d.getAttributes().getNamedItem("name").getNodeValue().toString();
							String FolderLang = d.getAttributes().getNamedItem("folder").getNodeValue().toString();
							String Abre = d.getAttributes().getNamedItem("abbreviation").getNodeValue().toString();
							Lang temp = new Lang(NameLang, FolderLang, Abre);
							FOLDER_LANG.put(Abre, temp);
							_log.warning("	Language HTML System Add -> " + NameLang);
						}
					}
				}
			}
		}catch(Exception a){
			_log.warning("Error loading HTML Language System->" + a.getMessage());
		}
	}
	
	public void setNewLanguage(L2PcInstance player, String Lang){
		SETT.get(player.getObjectId()).ChangeLang(Lang);
		central.msgbox("New Language ->" + FOLDER_LANG.get(Lang).getLanguage() , player);
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public void showLanguageWindows(L2PcInstance player){
		NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/language.html");
		String Dato = "";
		Iterator itr = FOLDER_LANG.entrySet().iterator();
		while(itr.hasNext()){
			Map.Entry Entrada = (Map.Entry)itr.next();
			String Abrevacion = (String)Entrada.getKey();
			Lang DatoLang = (Lang)Entrada.getValue();
			Dato += "<button value=\""+ DatoLang.getLanguage() +"\" action=\"bypass -h ZeuS changelanguage "+ DatoLang.getAbbreviation() +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Back\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\">";
		}
		html.replace("%DATA%", Dato);
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		central.sendHtml(player, html);
	}
	
	public String getExplains(L2PcInstance player, String _Button){
		String MeLang = SETT.get(player.getObjectId()).getLanguage();
		Lang Temp = FOLDER_LANG.get(MeLang);
		
		switch (_Button) {
			case "Buffer":
				return Temp.getExplains(player, BUTTON.Buffer);
			case "Go Party Leader":
				return Temp.getExplains(player, BUTTON.Gopartyleader);
			case "Flag Finder":
				return Temp.getExplains(player, BUTTON.Flagfinder);
			case "Teleport":
				return Temp.getExplains(player, BUTTON.Teleport);
			case "Shop":
				return Temp.getExplains(player, BUTTON.Shop);
			case "Warehouse":
				return Temp.getExplains(player, BUTTON.Warehouse);
			case "Augment Manager":
				return Temp.getExplains(player, BUTTON.AugmentManager);
			case "Subclass":
				return Temp.getExplains(player, BUTTON.SubClass);
			case "Profession":
				return Temp.getExplains(player, BUTTON.Profession);
			case "Drop Search":
				return Temp.getExplains(player, BUTTON.DropSearch);
			case "PvP/PK Log":
				return Temp.getExplains(player, BUTTON.pvppklog);
			case "Symbol Maker":
				return Temp.getExplains(player, BUTTON.Symbolmaker);
			case "Bug Report":
				return Temp.getExplains(player, BUTTON.BugReport);
			case "Transformations":
				return Temp.getExplains(player, BUTTON.Transformation);
			case "Remove Attribute":
				return Temp.getExplains(player, BUTTON.RemoveAttri);
			case "Select Augment":
				return Temp.getExplains(player, BUTTON.SelectAugment);
			case "Select Enchant":
				return Temp.getExplains(player, BUTTON.SelectEnchant);
			case "Select Elemental":
				return Temp.getExplains(player, BUTTON.SelectElemental);
			case "Raid Boss Info":
				return Temp.getExplains(player, BUTTON.RaidBossInfo);
			case "Blacksmith":
				return Temp.getExplains(player, BUTTON.blacksmith);	
			case "Miscelanius":
				return Temp.getExplains(player, BUTTON.charclanoption);
			case "Party Matching":
				return Temp.getExplains(player, BUTTON.partymatching);
			case "Auctions House":
				return Temp.getExplains(player, BUTTON.AuctionHouse);
			case "Castle Manager":
				return Temp.getExplains(player, BUTTON.castleManager);	
			case "Dressme":
				return Temp.getExplains(player, BUTTON.Dressme);
			case "Bid House":
				return Temp.getExplains(player, BUTTON.BidHouse);
			}
		return "";
	}
	
	public Lang getMsg(String playerName){
		L2PcInstance ppl = opera.getPlayerbyName(playerName);
		if(ppl != null){
			String MeLang = SETT.get(ppl.getObjectId()).getLanguage();
			Lang Temp = FOLDER_LANG.get(MeLang);
			return Temp;
		}
		int getIDPlayer = opera.getPlayerIDbyName(playerName);
		String MeLang = SETT.get(getIDPlayer).getLanguage();
		Lang Temp = FOLDER_LANG.get(MeLang);
		return Temp;		
	}	
	
	public Lang getMsg(L2PcInstance player){
		String MeLang = SETT.get(player.getObjectId()).getLanguage();
		Lang Temp = FOLDER_LANG.get(MeLang);
		return Temp;
	}
	
	public Lang getMsg(int idPlayer){
		String MeLang = getFolderFromDB(idPlayer);
		Lang Temp = FOLDER_LANG.get(MeLang);
		return Temp;
	}	
	
	public String getFolder(L2PcInstance player){
		String MeLang = SETT.get(player.getObjectId()).getLanguage();
		String Folder = FOLDER_LANG.get(MeLang).getFolder();
		return (Folder.length()==0 ? "eng" : Folder);
	}
	
	public String getFolder(int idPlayer){
		String Folder = getFolderFromDB(idPlayer);
		return (Folder.length()==0 ? "eng" : Folder);
	}	
	
	private String getFolderFromDB(int idPlayer){
		String strMySql = "SELECT language FROM zeus_char_config WHERE idchar=?";
		Connection conn = null;
		PreparedStatement psqry = null;
		String Retornar = "";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(strMySql);
			psqry.setInt(1, idPlayer);
			ResultSet rss = psqry.executeQuery();
			if (rss.next()){
				try{
					Retornar = rss.getString("language");
				}catch(Exception a){
				
				}
			}
			rss.close();
			psqry.close();
			conn.close();
		}catch(Exception e){
			
		}
		return Retornar;
	}
	
	public void loadLanguage(L2PcInstance player){
		if(SETT == null){
			setting temp = new setting(player.getObjectId());
			SETT.put(player.getObjectId(), temp);
		}else if(SETT.size()==0){
			setting temp = new setting(player.getObjectId());
			SETT.put(player.getObjectId(), temp);			
		}else if(!SETT.containsKey(player.getObjectId())){
			setting temp = new setting(player.getObjectId());
			SETT.put(player.getObjectId(), temp);			
		}
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getAdmCmd(L2PcInstance player){
		String MeLang = SETT.get(player.getObjectId()).getLanguage();
		return FOLDER_LANG.get(MeLang).getAdminCommand();
	}
	
	@SuppressWarnings("rawtypes")
	public Vector getUserCmd(L2PcInstance player){
		String MeLang = SETT.get(player.getObjectId()).getLanguage();
		return FOLDER_LANG.get(MeLang).getUserCommand();
	}
	
	public static language getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final language _instance = new language();
	}
	
}