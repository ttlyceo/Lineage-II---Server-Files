package ZeuS.Config;

import l2r.gameserver.model.Location;

public class _teleportData {
	private String NAME;
	private boolean FOR_NOBLE;
	private boolean CAN_GO_FLAG;
	private boolean CAN_GO_KARMA;
	private boolean CAN_USE_DUALBOX;
	private int MIN_LEVEL;
	private int POSITION;
	private String TYPE;
	private String DESCRIPT;
	private int ID;
	private int ID_SECC;
	private int X;
	private int Y;
	private int Z;

	public _teleportData(int id, int idsec, String TeleName, boolean isForNoble, boolean cangoFlag, boolean cangoKarma, boolean canuseDualbox, int minLevel, String type, int Position, int _x, int _y, int _z, String Description){
		this.NAME = TeleName;
		this.FOR_NOBLE = isForNoble;
		this.CAN_GO_FLAG = cangoFlag;
		this.CAN_GO_KARMA = cangoKarma;
		this.CAN_USE_DUALBOX = canuseDualbox;
		this.MIN_LEVEL = minLevel;
		this.TYPE = type;
		this.POSITION = Position;
		this.ID = id;
		this.X = _x;
		this.Y = _y;
		this.Z = _z;
		this.ID_SECC = idsec;
		this.DESCRIPT = Description;
	}
	public final String getDescription(){
		return this.DESCRIPT;
	}
	public final String getStrCoordenates(){
		return String.valueOf( this.X ) + " " + String.valueOf( this.Y ) + " " + String.valueOf( this.Z ) + " ";
	}
	public final String getName(){
		return this.NAME;
	}
	public final String getDescript(){
		return this.getDescript();
	}
	public final boolean isJustForNoble(){
		return this.FOR_NOBLE;
	}
	public final boolean canUseFlagPlayer(){
		return this.CAN_GO_FLAG;
	}
	public final boolean canUsePkPlayer(){
		return this.CAN_GO_KARMA;
	}
	public final boolean canUseDualPlayer(){
		return this.CAN_USE_DUALBOX;
	}
	public final int minLevelToUse(){
		return this.MIN_LEVEL;
	}
	public final String getType(){
		return this.TYPE;
	}
	public final int getPosition(){
		return this.POSITION;
	}
	public final int getID(){
		return this.ID;
	}
	public final int getIDSecc(){
		return this.ID_SECC;
	}
	public final Location getLocation(){
		Location temp = new Location(this.X, this.Y, this.Z);
		return temp;
	}
	public final int getX(){
		return this.X;
	}
	public final int getY(){
		return this.Y;
	}
	public final int getZ(){
		return this.Z;
	}
}
