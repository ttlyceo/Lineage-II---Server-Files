package ZeuS.Config;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class _cumulativeSkill {
	private final int SKILL_ID;
	private final int SKILL_LEVEL;
	private final String SKILL_NAME;
	private final boolean ENABLED;
	private final int MIN_LEVEL_TO_USE;
	private final List<Integer> PROHIBITED_FOR_CLASS_ID = new ArrayList<>();
	private final List<Integer> PROHIBITED_FOR_OTHERS_SKILL_ID = new ArrayList<>();
	private final boolean IS_ACTIVE_SKILL;
	private final Logger _log = Logger.getLogger(_cumulativeSkill.class.getName());
	public _cumulativeSkill(int skillID, int skillLEVEL, String skillNAME, boolean skillENABLED, int minLevelToUse, String classIDProhibited, String skillIDProhibitedInBaseClass, String SkillType) {
		this.SKILL_ID = skillID;
		this.SKILL_LEVEL = skillLEVEL;
		this.SKILL_NAME = skillNAME;
		this.ENABLED = skillENABLED;
		this.MIN_LEVEL_TO_USE = minLevelToUse;
		this.IS_ACTIVE_SKILL = (SkillType.equalsIgnoreCase("PASSIVE") ? false : true);
		if(classIDProhibited.length()>0) {
			if(classIDProhibited.indexOf(",")>0) {
				for(String IDCLASS_TEMP : classIDProhibited.split(",")) {
					try {
						int tempClassID = Integer.valueOf(IDCLASS_TEMP);
						this.PROHIBITED_FOR_CLASS_ID.add(tempClassID);
					}catch(Exception a) {
						_log.warning("Prohibited ID Class Wrog in Skill ID: " + skillID + " ("+ a.getMessage() +")");
					}
				}
			}else {
				try {
					int tempClassID = Integer.valueOf(classIDProhibited);
					this.PROHIBITED_FOR_CLASS_ID.add(tempClassID);
				}catch(Exception a) {
					_log.warning("Prohibited ID Class Wrog in Skill ID: " + skillID + " ("+ a.getMessage() +")");
				}
			}
		}
		
		if(skillIDProhibitedInBaseClass.length()>0) {
			if(skillIDProhibitedInBaseClass.indexOf(",")>0) {
				for(String ID_SKILL_TEMP : skillIDProhibitedInBaseClass.split(",")) {
					try {
						int tempSkillID = Integer.valueOf(ID_SKILL_TEMP);
						this.PROHIBITED_FOR_OTHERS_SKILL_ID.add(tempSkillID);
					}catch(Exception a) {
						
					}
				}
			}else{
				try {
					int tempSkillID = Integer.valueOf(skillIDProhibitedInBaseClass);
					this.PROHIBITED_FOR_OTHERS_SKILL_ID.add(tempSkillID);
				}catch(Exception a) {
					
				}
			}
		}
		
	}
	
	public final boolean isActiveSkill() {
		return this.IS_ACTIVE_SKILL;
	}
	
	public final int getSkillID() {
		return this.SKILL_ID;
	}
	public final int getSkillLevel() {
		return this.SKILL_LEVEL;
	}
	public final String getSkillName() {
		return this.SKILL_NAME;
	}
	public final boolean isEnabled() {
		return this.ENABLED;
	}
	public final int getMinLevelToBeUse() {
		return this.MIN_LEVEL_TO_USE;
	}
	public final List<Integer> getProhibitedClassToBeUse(){
		return this.PROHIBITED_FOR_CLASS_ID;
	}
	public final List<Integer> getProhibitedSkillsIdWithOtherSkill(){
		return this.PROHIBITED_FOR_OTHERS_SKILL_ID;
	}
	
	public final boolean hadSkillProhibitedWithOtherSkill() {
		if(this.PROHIBITED_FOR_OTHERS_SKILL_ID!=null) {
			if(this.PROHIBITED_FOR_OTHERS_SKILL_ID.size()>0) {
				return true;
			}
		}
		return false;
	}
}