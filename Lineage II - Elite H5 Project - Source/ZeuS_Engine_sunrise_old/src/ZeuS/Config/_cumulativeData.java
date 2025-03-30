package ZeuS.Config;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import ZeuS.interfase.cumulativeSubclass;
import ZeuS.procedimientos.opera;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.SubClass;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.Earthquake;

public class _cumulativeData {
	private static final Logger _log = Logger.getLogger(_cumulativeData.class.getName());
	private static Map<Integer, _cumulativeClass> CLASS_DATA_CUMULATIVE = new HashMap<Integer, _cumulativeClass>();

	private static boolean is_Enabled;
	private static boolean ADD_PASSIVE_SKILL;
	private static Map<Integer, Long> REQUESTED_ITEM = new HashMap<Integer, Long>();
	
	public static void load() {
		File dir = new File("./config/zeus/zeus_cumulative_class_skill.xml");
		try{
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(dir);
			for (Node n = doc.getFirstChild();n!=null;n=n.getNextSibling()){
				if(n.getNodeName().equalsIgnoreCase("list")) {
					for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling()){
						if(d.getNodeName().equalsIgnoreCase("config")){
							for(Node ConfigData = d.getFirstChild() ; ConfigData != null; ConfigData = ConfigData.getNextSibling()) {
								if(ConfigData.getNodeName().equalsIgnoreCase("enabled")){
									try {
										String tempBoolean = ConfigData.getAttributes().getNamedItem("value").getNodeValue().toLowerCase().trim();
										is_Enabled = tempBoolean.equalsIgnoreCase("true");
									}catch (Exception e) {
										is_Enabled = false;
									}
									_log.warning("Subclass Cumulative: " + String.valueOf(isEnabled()));
								}else if(ConfigData.getNodeName().equalsIgnoreCase("add_passive_skill")) {
									try {
										String tempBoolean = ConfigData.getAttributes().getNamedItem("value").getNodeValue().toLowerCase().trim();
										ADD_PASSIVE_SKILL = tempBoolean.equalsIgnoreCase("true");
									}catch (Exception e) {
										ADD_PASSIVE_SKILL = true;
									}									
								}else if(ConfigData.getNodeName().equalsIgnoreCase("requested_items")) {
									String tempRequestItem = "";
									try {
										tempRequestItem = ConfigData.getAttributes().getNamedItem("value").getNodeValue().toLowerCase().trim();
										if(tempRequestItem.indexOf(";")>0) {
											for(String Part : tempRequestItem.split(";")) {
												int IdItem = Integer.valueOf(Part.split(",")[0]);
												long Cantidad = Long.valueOf(Part.split(",")[1]);
												REQUESTED_ITEM.put(IdItem, Cantidad);												
											}
										}else {
											int IdItem = Integer.valueOf(tempRequestItem.split(",")[0]);
											long Cantidad = Long.valueOf(tempRequestItem.split(",")[1]);
											REQUESTED_ITEM.put(IdItem, Cantidad);
										}
									}catch(Exception a) {
										tempRequestItem = "";
									}
									
								}
							}
						}else if(d.getNodeName().equalsIgnoreCase("class")){
							String ClassName = d.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
							int ClassId = Integer.valueOf(d.getAttributes().getNamedItem("id").getNodeValue());
							int minLevelToUse = Integer.valueOf(d.getAttributes().getNamedItem("minlevel_to_use").getNodeValue());
							String prohibitedUseInClassesWith = "";
							try {
								prohibitedUseInClassesWith = d.getAttributes().getNamedItem("cannot_be_used_with_these_classes_id").getNodeValue().toLowerCase();
							}catch(Exception a) {
								_log.warning("Error getting Prohibited Use In Class With: " + a.getMessage());
							}
							
							boolean Enabled = ( d.getAttributes().getNamedItem("enabled").getNodeValue().equalsIgnoreCase("true") );
							_cumulativeClass tempClassSetting = new _cumulativeClass(ClassId, minLevelToUse, ClassName, Enabled, prohibitedUseInClassesWith);
							for(Node skills = d.getFirstChild() ; skills != null; skills = skills.getNextSibling()) {
								if(skills.getNodeName().equalsIgnoreCase("skill")){
									int IdSkill = Integer.valueOf(skills.getAttributes().getNamedItem("id").getNodeValue());
									int LevelSkill = Integer.valueOf(skills.getAttributes().getNamedItem("level").getNodeValue());
									boolean skillEnable = Boolean.valueOf( skills.getAttributes().getNamedItem("enabled").getNodeValue().toLowerCase().equalsIgnoreCase("true"));
									int minSkillLevelToBeUse = Integer.valueOf(skills.getAttributes().getNamedItem("minlevel_to_use").getNodeValue());
									String ProhibitedSkillUseInClassWith = "";
									try {
										ProhibitedSkillUseInClassWith = skills.getAttributes().getNamedItem("prohibited_use_with_class_id").getNodeValue().toLowerCase();
									}catch(Exception a) {
										
									}
									String ProhibitedSKillNotUseWithOtherSkill = "";
									try {
										ProhibitedSKillNotUseWithOtherSkill = skills.getAttributes().getNamedItem("prohibited_use_if_have_skill_id").getNodeValue().toLowerCase();
									}catch(Exception a) {
										
									}
									
									
									String TypeSkill = "";
									try {
										TypeSkill = skills.getAttributes().getNamedItem("type").getNodeValue().toLowerCase();
									}catch(Exception a) {
										TypeSkill = "ACTIVE";
									}
									
									String SkillName = skills.getAttributes().getNamedItem("name").getNodeValue().toLowerCase();
									_cumulativeSkill temSkillSetting = new _cumulativeSkill(IdSkill, LevelSkill, SkillName, skillEnable, minSkillLevelToBeUse, ProhibitedSkillUseInClassWith, ProhibitedSKillNotUseWithOtherSkill, TypeSkill);
									tempClassSetting.setNewSkillData(temSkillSetting);
								}							
							}
							CLASS_DATA_CUMULATIVE.put(ClassId, tempClassSetting);
						}
					}
				}
			}
		}catch(Exception a) {
			_log.warning("Error loading zeus_cumulative_class_skill.xml file (" + a.getMessage() + ")");
		}		
	}
	
	public static final boolean isEnabled() {
		return is_Enabled;
	}
	
	public static final boolean canAddPassiveSkill() {
		return ADD_PASSIVE_SKILL;
	}
	
	public static boolean isBlockClass(int idClassCheck, int IdClassNew) {
		try {
			_cumulativeClass ClassInfo = CLASS_DATA_CUMULATIVE.get(idClassCheck);
			return ClassInfo.isClassIdBlock(IdClassNew);
		}catch(Exception a) {
			_log.warning("Error isBlockClass->" + a.getMessage());
		}
		return true;
	}
	
	public static boolean canAddSubClass(L2PcInstance player, int IdClassNew) {
		if(player.getSubClasses()!=null) {
			if(player.getSubClasses().size()>0) {
				for(SubClass SubClassSelect : player.getSubClasses().values()) {
					_cumulativeClass ClassInfo = CLASS_DATA_CUMULATIVE.get( SubClassSelect.getClassId());
					boolean IsBlockClassID = ClassInfo.isClassIdBlock(IdClassNew);
					if(IsBlockClassID) {
						return false;
					}
				}
				return true;
			}
		}
		_cumulativeClass ClassInfo = CLASS_DATA_CUMULATIVE.get( player.getBaseClass() );
		boolean IsBlockClassID = ClassInfo.isClassIdBlock(IdClassNew);
		return IsBlockClassID ? false : true;		
	}
	
	public static List<_cumulativeSkill> getAllSkillToGive(L2PcInstance player, int idClass, int IndexClassIndex) {
		return CLASS_DATA_CUMULATIVE.get(idClass).getSkillsData(player, idClass, IndexClassIndex);
	}
	
	public static List<_cumulativeSkill> getSkillToGive(L2PcInstance player, int idClass, int IndexClassIndex){
		List<_cumulativeSkill> Retorno = new ArrayList<_cumulativeSkill>();
		List<_cumulativeSkill> AllSkillFromNewClass = CLASS_DATA_CUMULATIVE.get(idClass).getSkillsData(player, idClass, IndexClassIndex);
		Collection<L2Skill> AllSkillFromPlayer = player.getAllSkills();
		List<Integer> SKillIdFromPlayer = new ArrayList<>();
		for(L2Skill skillArray : AllSkillFromPlayer) {
			SKillIdFromPlayer.add(skillArray.getId());
		}
		
		for(_cumulativeSkill SkillToGive: AllSkillFromNewClass) {
			if(SkillToGive.hadSkillProhibitedWithOtherSkill()) {
				for( Integer ID_PROHIBITED :SkillToGive.getProhibitedSkillsIdWithOtherSkill() ) {
					if(SKillIdFromPlayer.contains(ID_PROHIBITED)) {
						continue;
					}
				}
				Retorno.add(SkillToGive);
			}else {
				Retorno.add(SkillToGive);
			}
		}
		
		return Retorno;
	}

	public static boolean haveRequestedItems(L2PcInstance player) {
		if(REQUESTED_ITEM != null) {
			if(REQUESTED_ITEM.size()>0) {
				Iterator itr = REQUESTED_ITEM.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry infoR = (Map.Entry)itr.next();
					int idItem = (int)infoR.getKey();
					long Cantidad = (long)infoR.getValue();
					if(!opera.haveItem(player, idItem, Cantidad)) {
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public static void deleteRequestedItem(L2PcInstance player) {
		if(REQUESTED_ITEM != null) {
			if(REQUESTED_ITEM.size()>0) {
				Iterator itr = REQUESTED_ITEM.entrySet().iterator();
				while(itr.hasNext()){
					Map.Entry infoR = (Map.Entry)itr.next();
					int idItem = (int)infoR.getKey();
					long Cantidad = (long)infoR.getValue();
					opera.removeItem(idItem, Cantidad, player);
				}
			}
		}		
	}
}

class _cumulativeClass {
	private final int ID_CLASS;
	private final int MIN_LEVEL;
	private final String NAME_CLASS;
	private final boolean ENABLED;
	private final List<Integer> PROHIBITED_CLASS = new ArrayList<>();
	private final Logger _log = Logger.getLogger(_cumulativeClass.class.getName());
	private List<_cumulativeSkill> SKILLS = new ArrayList<>();
	public _cumulativeClass(int idClass, int minLevel, String ClassName, boolean Enabled, String ProhibitedClassID) {
		this.ID_CLASS = idClass;
		this.MIN_LEVEL = minLevel;
		this.NAME_CLASS = ClassName;
		this.ENABLED = Enabled;
		if(ProhibitedClassID.length()>0) {
			if(ProhibitedClassID.indexOf(",")>0) {
				for(String ClassID : ProhibitedClassID.split(",")) {
					try {
						int IdClassTemp = Integer.valueOf(ClassID);
						this.PROHIBITED_CLASS.add(IdClassTemp);
					}catch(Exception a) {
						_log.warning("Prohibited Use ID Class Wrog in Class ID: " + idClass + "("+ a.getMessage() +")");
					}
				}
			}else {
				try {
					int IdClassTemp = Integer.valueOf(ProhibitedClassID);
					this.PROHIBITED_CLASS.add(IdClassTemp);
				}catch(Exception a) {
					_log.warning("Prohibited Use ID Class Wrog in Class ID: " + idClass + "("+ a.getMessage() +")");
				}			
			}
		}
	}
	
	public final List<_cumulativeSkill> getSkillsData(L2PcInstance player, int IdClass, int ClassIndexID) {
		List<_cumulativeSkill> tmpData = new ArrayList<_cumulativeSkill>();
		List<Integer> SkillFromPlayer = new ArrayList<Integer>();
		List<Integer> PlayerSubClassID = new ArrayList<Integer>();
		
		if(player.getSubClasses() != null) {
			if(player.getSubClasses().size()>0) {
				for(SubClass PplayerSubClass : player.getSubClasses().values()) {
					PlayerSubClassID.add(PplayerSubClass.getClassId());
				}
			}
		}
		
		for(L2Skill SkillPlayer : player.getSkills().values()) {
			SkillFromPlayer.add(SkillPlayer.getId());
		}
		
		
		for(_cumulativeSkill TskillData : this.SKILLS) {
			
			if(!TskillData.isEnabled()) {
				continue;
			}
			
			if( player.getLevel() < TskillData.getMinLevelToBeUse() ) {
				continue;
			}
			
			if(!_cumulativeData.canAddPassiveSkill()) {
				if(!TskillData.isActiveSkill()) {
					continue;
				}
			}			
			
			if(TskillData.getProhibitedClassToBeUse()!=null) {
				if(TskillData.getProhibitedClassToBeUse().size()>0) {
					if(PlayerSubClassID!=null) {
						boolean BreakSkill = false;
						for(int IdPlayerSubClassCheck : PlayerSubClassID) {
							if(TskillData.getProhibitedClassToBeUse().contains(IdPlayerSubClassCheck)) {
								BreakSkill = true;
							}
						}
						if(BreakSkill) {
							continue;
						}
					}
					
					if(TskillData.getProhibitedClassToBeUse().contains(player.getBaseClass())) {
						continue;
					}
					
					
				}
			}
			
			if(TskillData.getProhibitedSkillsIdWithOtherSkill()!=null) {
				if(TskillData.getProhibitedSkillsIdWithOtherSkill().size()>0) {
					boolean BreakSKill = false;
					
					for(int idSkillFromPlayer : SkillFromPlayer) {
						if(TskillData.getProhibitedSkillsIdWithOtherSkill().contains(idSkillFromPlayer)) {
							BreakSKill = true;
						}
					}
					
					if(BreakSKill) {
						continue;
					}
				}
			}
			
			tmpData.add(TskillData);
			cumulativeSubclass.getInstance().insertData(player.getObjectId(), IdClass, TskillData.getSkillID(), TskillData.getSkillLevel(), ClassIndexID);			
		}
		return tmpData;
	}
	
	public boolean isClassIdBlock(int IdNewClass) {
		if(this.PROHIBITED_CLASS != null) {
			if(this.PROHIBITED_CLASS.size()>0) {
				return this.PROHIBITED_CLASS.contains(IdNewClass);
			}
		}
		return false;
	}
	
	public void setNewSkillData( _cumulativeSkill SkillToSave) {
		this.SKILLS.add(SkillToSave);
	}
	
	public final int getIdClass() {
		return this.ID_CLASS;
	}
	
	public final int getMinMainLevelToUse() {
		return this.MIN_LEVEL;
	}
	
	public final String getClassName() {
		return this.NAME_CLASS;
	}
	
	public final boolean isEnabled() {
		return this.ENABLED;
	}
	
	public List<Integer> getProhibitedMainClassToUse(){
		return this.PROHIBITED_CLASS;
	}
	
}


