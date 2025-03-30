package ZeuS.Config;

import ZeuS.procedimientos.opera;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class _buffStoreDataSellInfo{
	private int idItem;
	private long price;
	private int Bufflevel;
	private int BuffEnchant;
	private String PlayerName;
	private String PlayerClass;
	private String PlayerClanName;
	private String buffPathName;
	
	public _buffStoreDataSellInfo(int idSkill, int _iditem, long _price, int level, L2PcInstance _player){
		this.idItem = _iditem;
		this.price = _price;
		this.Bufflevel = level;
		this.PlayerName = _player.getName();
		this.PlayerClass = opera.getClassName(_player.getClassId().getId());
		if(_player.getClan()!=null){
			this.PlayerClanName = _player.getClan().getName();
		}else{
			this.PlayerClanName = "None";
		}
		
		if(level>=100 && level<200){
			BuffEnchant = level - 100;
		}else if(level>200 && level<300){
			BuffEnchant = level - 200;
		}else if(level>300 && level<400){
			BuffEnchant = level - 300;
		}else if(level>400 && level<500){
			BuffEnchant = level - 400;
		}else if(level>500 && level<600){
			BuffEnchant = level - 500;
		}else if(level>600 && level<700){
			BuffEnchant = level - 600;
		}
		
		buffPathName = enum_skill_path_name.getPathName(idSkill, level);
		
	}
	public final int getBuffEnchant(){
		return this.BuffEnchant;
	}
	public final String getBuffPath(){
		return this.buffPathName;
	}
	public final String getPlayerName(){
		return this.PlayerName;
	}
	public final String getPlayerClanName(){
		return this.PlayerClanName;
	}
	public final String getPlayerClassName(){
		return this.PlayerClass;
	}
	public final int getIdItem(){
		return this.idItem;
	}
	public final long getPrice(){
		return this.price;
	}
	public final int getLevel(){
		return this.Bufflevel;
	}
}