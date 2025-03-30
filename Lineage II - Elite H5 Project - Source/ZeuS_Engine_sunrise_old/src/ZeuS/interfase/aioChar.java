package ZeuS.interfase;

import java.io.File;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import l2r.L2DatabaseFactory;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.L2Skill;
import ZeuS.Config.general;
import ZeuS.language.language;



public class aioChar {

	private static Logger _log = Logger.getLogger(aioChar.class.getName());
	static Connection conn =null;
	static CallableStatement psqry = null;
	static ResultSet rss = null;
	
	private static Map<Integer, _SkillsAIOS> SKILL_FOR_AIO = new HashMap<Integer, _SkillsAIOS>();
	
	private static void loadFromXML(){
		File dir = new File("./config/zeus/zeus_AIO_skill.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")){
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("skill")){
							int idBuff = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							int levelNormal = Integer.valueOf(d.getAttributes().getNamedItem("normal_level").getNodeValue());
							int levelEnchant = Integer.valueOf(d.getAttributes().getNamedItem("enchant_level").getNodeValue());
							String BuffName = d.getAttributes().getNamedItem("name").getNodeValue();
							_SkillsAIOS temp = new _SkillsAIOS(idBuff, levelNormal, levelEnchant, BuffName);
							SKILL_FOR_AIO.put(idBuff, temp);
						}
					}
				}
			}
		}catch(Exception a){
			
		}				
		
	}
	
	public static void loadBuffForAio(){
		try{
			SKILL_FOR_AIO.clear();
		}catch(Exception a){
			
		}
		loadFromXML();
	}

	public static String explicaAIO(L2PcInstance st){
		return "";
	}

	@SuppressWarnings("rawtypes")
	private static void addSkillAIO(L2PcInstance st, boolean Buff0){
		if(!general._activated()){
			return;
		}
		
		Iterator itr = SKILL_FOR_AIO.entrySet().iterator();
		int idBuff = 0;
	    while(itr.hasNext()){
	    	try{
		    	Map.Entry Entrada = (Map.Entry)itr.next();
		    	_SkillsAIOS BuffData = (_SkillsAIOS)Entrada.getValue();
		    	idBuff = (int)Entrada.getKey();
		    	L2Skill newSkill = null;
		    	if(Buff0){
		    		newSkill = SkillData.getInstance().getInfo(BuffData.getSkillID(), BuffData.getSkillEnchantDefault());
		    	}else{
		    		newSkill = SkillData.getInstance().getInfo(BuffData.getSkillID(), BuffData.getSkillEnchantMax());
		    	}
		    	if(newSkill==null) {
		    		continue;
		    	}
				st.addSkill(newSkill,true);
	    	}catch(Exception a){
	    		_log.warning("ZeuS AIOCHAR Error-> IDSKILL->" + idBuff + " Error->" + a.getMessage());
	    	}
	    }
	}

	public static boolean setNewAIO(L2PcInstance st,boolean setSkill_0){
		return setNewAIO(st, setSkill_0, null);
	}
	
	public static boolean setNewAIO(L2PcInstance st,boolean setSkill_0, L2PcInstance GMPLAYER){
		if(!general._activated()){
			return false;
		}
		if(!isNameOK(st)){
			central.msgbox(language.getInstance().getMsg(st).THE_NEW_AIO_NAME_EXIST, st);
			if(GMPLAYER!=null){
				central.msgbox(language.getInstance().getMsg(st).THE_NEW_AIO_NAME_EXIST, GMPLAYER);	
			}
			return false;
		}

		String ActualName = st.getName();
		if(ActualName.length()>14){
			central.msgbox(language.getInstance().getMsg(st).AIO_CHAR_NAME_MUST_HAVE_CHARACTERS.replace("$maximo", "14"), st);
			if(GMPLAYER!=null){
				central.msgbox(language.getInstance().getMsg(st).AIO_CHAR_NAME_MUST_HAVE_CHARACTERS.replace("$maximo", "14"), GMPLAYER);	
			}
			return false;
		}

		central.msgbox_Lado(language.getInstance().getMsg(st).AIO_CHAR_CREATION_STARTED, st);
		central.msgbox(language.getInstance().getMsg(st).THIS_PROCESS_MAY_TAKE_FEW_MINUTES,st);

		if(GMPLAYER!=null){
			central.msgbox_Lado(language.getInstance().getMsg(GMPLAYER).AIO_CHAR_CREATION_STARTED, GMPLAYER);
			central.msgbox(language.getInstance().getMsg(GMPLAYER).THIS_PROCESS_MAY_TAKE_FEW_MINUTES,GMPLAYER);	
		}
		
		for(L2Skill tempSkill : st.getAllSkills()){
			if(tempSkill.isActive() || tempSkill.isToggle()){
				st.removeSkill(tempSkill);
			}
		}		
		
		st.setNoble(true);
		st.addExpAndSp(93836,0);

		String NombreNew = general.AIO_PREFIX + st.getName();
		
		st.setName(NombreNew);
		st.store();		
		
		addSkillAIO(st,setSkill_0);
		
		st.broadcastUserInfo();
		central.msgbox_Lado(language.getInstance().getMsg(st).AIO_CHAR_ENDED_WITHOUT_PROBLEMS,st);
		
		if(GMPLAYER!=null){
			central.msgbox_Lado(language.getInstance().getMsg(st).AIO_CHAR_ENDED_WITHOUT_PROBLEMS,GMPLAYER);	
		}		
		
		return true;
	}

	private static boolean isNameOK(L2PcInstance st){
		boolean _isOK = true;
		if(st.getName().startsWith(general.AIO_PREFIX)){
			central.msgbox(language.getInstance().getMsg(st).MIS_YOU_AIOCHAR_CANT_DO_IT_AGAIN, st);
			return false;
		}
		Connection conn = null;
		PreparedStatement psqry = null;		
		String _qry = "SELECT characters.charId FROM characters WHERE characters.char_name = ?";
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			psqry = conn.prepareStatement(_qry);
			psqry.setString(1, general.AIO_PREFIX + st.getName());
			ResultSet rss = psqry.executeQuery();
			while (rss.next()){
				_isOK = false;
			}
		}catch(Exception e){

		}

		try{
			conn.close();
		}catch(Exception a){

		}
		return _isOK;
	}
}

class _SkillsAIOS{
	int SKILL_ID;
	int SKILL_DEFAULT_ENCHANT_LEVEL;
	int SKILL_MAX_ENCHANT_LEVEL;
	String SKILL_NAME;
	public _SkillsAIOS(int idSkill, int DefaultEnchant, int MaxEnchant, String SkillName){
		this.SKILL_ID = idSkill;
		this.SKILL_DEFAULT_ENCHANT_LEVEL = DefaultEnchant;
		this.SKILL_MAX_ENCHANT_LEVEL = MaxEnchant;
		this.SKILL_NAME = SkillName;
	}
	public final int getSkillID(){
		return this.SKILL_ID;
	}
	public final int getSkillEnchantDefault(){
		return this.SKILL_DEFAULT_ENCHANT_LEVEL;
	}
	public final int getSkillEnchantMax(){
		return this.SKILL_MAX_ENCHANT_LEVEL;
	}
}
