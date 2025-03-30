package ZeuS.Config;

public class _topplayer {
	private String NAME;
	private int LEVEL;
	private int PVP;
	private int PK;
	private String BASE_CLASS;
	private int ID_PLAYER;
	public _topplayer(String _name, int _level, int _pvp, int _pk, String _base, int iddplayer, int _rank){
		this.NAME = _name;
		this.LEVEL = _level;
		this.PVP = _pvp;
		this.PK = _pk;
		this.BASE_CLASS = _base;
		this.ID_PLAYER = iddplayer;
	}
	
	public final String getName(){
		return this.NAME;
	}
	public final int getLevel(){
		return this.LEVEL;
	}
	public final int getPVP(){
		return this.PVP;
	}
	public final int getPK(){
		return this.PK;
	}
	public final String getBaseClass(){
		return this.BASE_CLASS;
	}
	public final int getID(){
		return this.ID_PLAYER;
	}
	
}
