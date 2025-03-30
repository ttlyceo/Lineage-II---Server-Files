package ZeuS.Config;

public class _classesinfo {
	private String CLASS_NAME;
	private String CLASS_LOGO;
	private int CLASS_ID;
	private boolean CLASS_IS_LAST = false;
	private int CLASS_COUNT = 0;
	private String RACE_NAME;
	private int IdToCheckReset;
	public _classesinfo(String Name, String Logo, int ClassId, boolean isLast, String RaceName){
		this.CLASS_NAME = Name;
		this.CLASS_LOGO = Logo;
		this.CLASS_ID = ClassId;
		this.CLASS_IS_LAST = isLast;
		this.RACE_NAME = RaceName;
	}
	
	public void setIsLast(boolean Value){
		this.CLASS_IS_LAST = Value;
	}
	public final String getName(){
		return this.CLASS_NAME;
	}
	public final String getLogo(){
		return this.CLASS_LOGO;
	}
	public final int getID(){
		return this.CLASS_ID;
	}
	public final boolean isLast(){
		return this.CLASS_IS_LAST;
	}
	public final void setCountByOne(int IdCheck){
		if(this.IdToCheckReset != IdCheck){
			this.CLASS_COUNT = 1;
			IdToCheckReset = IdCheck;
		}else{
			this.CLASS_COUNT += 1;
		}
	}
	public final int getClassCount(){
		return this.CLASS_COUNT;
	}
	public final String getRaceName(){
		return this.RACE_NAME;
	}
}
