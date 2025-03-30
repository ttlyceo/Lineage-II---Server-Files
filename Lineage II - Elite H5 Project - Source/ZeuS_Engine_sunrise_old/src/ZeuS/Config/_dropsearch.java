package ZeuS.Config;

import java.text.DecimalFormat;
import java.util.logging.Logger;

import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;

public class _dropsearch {
	private String NPC_NAME;
	private String NPC_TYPE;
	private String NPC_TELEPORT;
	private int NPC_LEVEL;
	private int NPC_ID;
	private L2NpcTemplate NPC_MONSTER_TEMPLATE;
	private L2Npc NPC_MONSTER_NPC;
	private boolean NPC_AGGRESIVE;
	
	
	private int DROPS_ITEMS_NORMAL;
	private int DROPS_ITEMS_SPOIL;
	private int DROP_ID;
	private int DROP_MAX;
	private int DROP_MIN;
	private int DROP_CATEGORY;
	private String DROP_CHANCE;
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(_dropsearch.class.getName());
	
	public _dropsearch(String _Name, int _NpcId, int _NpcLvl, boolean isAgressive, int _DropItemsNormal, int _DropItemsSpoil, L2NpcTemplate _npcTemplate, L2Npc _npc){
		this.NPC_NAME = _Name;
		this.NPC_LEVEL = _NpcLvl;
		this.NPC_ID = _NpcId;
		this.NPC_AGGRESIVE = isAgressive;
		this.DROPS_ITEMS_NORMAL = _DropItemsNormal;
		this.DROPS_ITEMS_SPOIL = _DropItemsSpoil;
		this.NPC_MONSTER_TEMPLATE = _npcTemplate;
		this.NPC_MONSTER_NPC = _npc;
	}
	
	public _dropsearch(String _Name, int _NpcId, int _DropID, int _DropMax, int _DropMin, String _Type, int _Level, int _Category, String _Chance, String _Teleport){
		this.NPC_NAME = _Name;
		this.NPC_ID = _NpcId;
		this.DROP_ID = _DropID;
		this.DROP_MAX = _DropMax;
		this.DROP_MIN = _DropMin;
		this.NPC_TYPE = _Type;
		this.NPC_LEVEL = _Level;
		this.DROP_CATEGORY = _Category;
		this.DROP_CHANCE = _Chance;
		this.NPC_TELEPORT = _Teleport;
	}
	public final String getName(){
		return this.NPC_NAME;
	}
	public final String getType(){
		return this.NPC_TYPE;
	}
	public final String getTeleport(){
		return this.NPC_TELEPORT;
	}
	public final int getNpcLevel(){
		return this.NPC_LEVEL;
	}
	public final int getNpcID(){
		return this.NPC_ID;
	}
	public final int getDropID(){
		return this.DROP_ID;
	}
	public final int getDropMax(){
		return this.DROP_MAX;
	}
	public final int getDropMin(){
		return this.DROP_MIN;
	}
	public final int getDropCategory(){
		return this.DROP_CATEGORY;
	}
	public final String _getDropChance(){
		return this.DROP_CHANCE;
	}
	public final String _getDropChancePorcent(){
		DecimalFormat df = new DecimalFormat("#.##");
		double tempDouble = Double.parseDouble(this.DROP_CHANCE);
		return df.format(tempDouble);
	}	
	public final boolean getIsAggre(){
		return this.NPC_AGGRESIVE;
	}
	public final int getDropsNornaml(){
		return this.DROPS_ITEMS_NORMAL;
	}
	public final int getDropsSpoil(){
		return this.DROPS_ITEMS_SPOIL;
	}
	
	public final L2NpcTemplate getNpcTemplace(){
		return this.NPC_MONSTER_TEMPLATE;
	}
	
	public final L2Npc getNpc(){
		return this.NPC_MONSTER_NPC;
	}
}
