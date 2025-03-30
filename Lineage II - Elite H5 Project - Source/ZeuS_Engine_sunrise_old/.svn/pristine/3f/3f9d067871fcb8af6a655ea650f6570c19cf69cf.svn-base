package ZeuS.procedimientos;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.handler.ItemHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.holders.SkillHolder;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.model.skills.L2Skill;

public class autoPots {
	private static Map<L2PcInstance, _autosP> PLAYER_AUTOPOTS = new HashMap<L2PcInstance, _autosP>();

	public static void setMana(L2PcInstance player){
		PLAYER_AUTOPOTS.get(player).MP();
	}
	
	public static void setCp(L2PcInstance player){
		PLAYER_AUTOPOTS.get(player).CP();
	}
	
	public static void setHp(L2PcInstance player){
		PLAYER_AUTOPOTS.get(player).HP();
	}
	
	public static void addPlayer(L2PcInstance player){
		if(PLAYER_AUTOPOTS != null){
			if(PLAYER_AUTOPOTS.size()>0){
				if(PLAYER_AUTOPOTS.containsKey(player)){
					PLAYER_AUTOPOTS.remove(player);
					_autosP Temp = new _autosP(player);
					PLAYER_AUTOPOTS.put(player, Temp);					
				}else{
					_autosP Temp = new _autosP(player);
					PLAYER_AUTOPOTS.put(player, Temp);
				}
			}else{
				_autosP Temp = new _autosP(player);
				PLAYER_AUTOPOTS.put(player, Temp);				
			}
		}else{
			_autosP Temp = new _autosP(player);
			PLAYER_AUTOPOTS.put(player, Temp);
		}
	}
	
	
	public static boolean isAutoCp(L2PcInstance player) {
		return isAutoCp(player, null, null);
	}
	
	public static boolean isAutoMp(L2PcInstance player) {
		return isAutoMp(player, null, null);
	}
	
	public static boolean isAutoHp(L2PcInstance player) {
		return isAutoHp(player, null, null);
	}
	
	public static boolean isAutoCp(L2PcInstance player, L2ItemInstance item, L2Skill skill){
		if(item != null) {
			if(item.getId() != 5592) {
				return false;
			}
		}
		if(skill != null) {
			L2Item itemCP = ItemData.getInstance().getTemplate(5592);
			if(itemCP.hasSkills()) {
				Vector<Integer> Skills = new Vector<Integer>();
				for (SkillHolder skillInfo : itemCP.getSkills()) {
					Skills.add(skillInfo.getSkill().getId());
				}
				if(!Skills.contains(skill.getId())) {
					return false;
				}
			}
		}	
		if(PLAYER_AUTOPOTS != null){
			if(PLAYER_AUTOPOTS.size()>0){
				if(PLAYER_AUTOPOTS.containsKey(player)){
					return PLAYER_AUTOPOTS.get(player).isAutoCP();
				}
			}
		}
		return false;
	}
	
	public static boolean isAutoMp(L2PcInstance player, L2ItemInstance item, L2Skill skill){
		if(item != null) {
			if(item.getId() != 728) {
				return false;
			}
		}
		if(skill != null) {
			L2Item itemCP = ItemData.getInstance().getTemplate(728);
			if(itemCP.hasSkills()) {
				Vector<Integer> Skills = new Vector<Integer>();
				for (SkillHolder skillInfo : itemCP.getSkills()) {
					Skills.add(skillInfo.getSkill().getId());
				}
				if(!Skills.contains(skill.getId())) {
					return false;
				}
			}
		}		
		if(PLAYER_AUTOPOTS != null){
			if(PLAYER_AUTOPOTS.size()>0){
				if(PLAYER_AUTOPOTS.containsKey(player)){
					return PLAYER_AUTOPOTS.get(player).isAutoMP();
				}
			}
		}
		return false;
	}
	public static boolean isAutoHp(L2PcInstance player, L2ItemInstance item, L2Skill skill){
		if(item != null) {
			if(item.getId() != 1539) {
				return false;
			}
		}
		if(skill != null) {
			L2Item itemCP = ItemData.getInstance().getTemplate(1539);
			if(itemCP.hasSkills()) {
				Vector<Integer> Skills = new Vector<Integer>();
				for (SkillHolder skillInfo : itemCP.getSkills()) {
					Skills.add(skillInfo.getSkill().getId());
				}
				if(!Skills.contains(skill.getId())) {
					return false;
				}
			}
		}		
		if(PLAYER_AUTOPOTS != null){
			if(PLAYER_AUTOPOTS.size()>0){
				if(PLAYER_AUTOPOTS.containsKey(player)){
					return PLAYER_AUTOPOTS.get(player).isAutoHP();
				}
			}
		}
		return false;
	}	
	
}

class _autosP{
	private L2PcInstance _PLAYER;
	private final int _ID_MANA_ITEM = 728;
	private final int _ID_CP_ITEM = 5592;
	private final int _ID_HEAL_ITEM = 1539;
	
	private boolean IS_HP = false;
	private boolean IS_CP = false;
	private boolean IS_MP = false;

	protected int HP_ID_WAIT = 0;
	public int CP_ID_WAIT = 0;
	public int MP_ID_WAIT = 0;
	
	@SuppressWarnings("unused")
	private final Logger _log = Logger.getLogger(_autosP.class.getName());
	
	public _autosP(L2PcInstance player){
		this._PLAYER = player;
	}

	public boolean isAutoCP(){
		return this.IS_CP;
	}
	
	public boolean isAutoMP(){
		return this.IS_MP;
	}
	
	public boolean isAutoHP(){
		return this.IS_HP;
	}
	
	public boolean isPlayerOK(){
		try{
			if(this._PLAYER != null){
				if(opera.isCharOnline(this._PLAYER)){
					return true;
				}				
			}
		}catch(Exception a){
			
		}
		return false;
	}
	
	public void HP(){
		if(!general.ZEUS_AUTOPOTS_HP){
			central.msgbox(language.getInstance().getMsg(this._PLAYER).DISABLE_BY_ADMIN, this._PLAYER);
			return;
		}
		if(this.IS_HP){
			this.IS_HP = false;
			central.msgbox_Lado("AUTO HP: DISABLED", this._PLAYER, "AUTO POTS");
		}else{
			try{
				this.HP_ID_WAIT = opera.getUnixTimeNow();
				this.IS_HP = true;				
				healHP(this.HP_ID_WAIT);
				central.msgbox_Lado("AUTO HP: ENABLED", this._PLAYER, "AUTO POTS");
			}catch(Exception a){
				
			}
		}
	}
	
	public void MP(){
		if(!general.ZEUS_AUTOPOTS_MP){
			central.msgbox(language.getInstance().getMsg(this._PLAYER).DISABLE_BY_ADMIN, this._PLAYER);
			return;
		}		
		if(this.IS_MP){
			this.IS_MP = false;
			central.msgbox_Lado("AUTO MP: DISABLED", this._PLAYER, "AUTO POTS");
		}else{
			try{
				this.MP_ID_WAIT = opera.getUnixTimeNow();
				this.IS_MP = true;				
				this.healMana(this.MP_ID_WAIT);
				central.msgbox_Lado("AUTO MP: ENABLED", this._PLAYER, "AUTO POTS");
			}catch(Exception a){
				
			}
		}
	}
	
	public void CP(){
		if(!general.ZEUS_AUTOPOTS_CP){
			central.msgbox(language.getInstance().getMsg(this._PLAYER).DISABLE_BY_ADMIN, this._PLAYER);
			return;
		}		
		if(this.IS_CP){
			this.IS_CP = false;
			central.msgbox_Lado("AUTO CP: DISABLED", this._PLAYER, "AUTO POTS");
		}else{
			try{
				this.CP_ID_WAIT = opera.getUnixTimeNow();
				this.IS_CP = true;				
				healCP(this.CP_ID_WAIT);
				central.msgbox_Lado("AUTO CP: ENABLED", this._PLAYER, "AUTO POTS");
			}catch(Exception a){
				
			}
		}
	}
	
	private boolean isPlayerReady(){
		if(this._PLAYER.isInvisible()){
			return false;
		}else if(this._PLAYER.isStunned()){
			return false;
		}else if(this._PLAYER.isInvul()){
			return false;
		}else if(this._PLAYER.isDead()){
			return false;
		}else if(this._PLAYER.isFakeDeath()){
			return false;
		}
		return true;
	}
	
	private void healHP(int IdForUse){
		if(!general.ZEUS_AUTOPOTS_HP){
			this.IS_HP = false;
			return;			
		}
		
		if(IdForUse != this.HP_ID_WAIT){
			this.IS_HP = false;
			return;
		}
		
		if(!opera.isCharOnline(this._PLAYER)){
			this.IS_HP = false;
			return;
		}		
		
		if(!this.IS_HP){
			return;
		}
		
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			@Override
			public void run(){
				healHP(HP_ID_WAIT);
			}
		}, general.ZEUS_AUTOPOTS_CHECK_MILISECOND + 100);
		
		if(!isPlayerReady() || (this._PLAYER.getMaxHp() == this._PLAYER.getStatus().getCurrentHp())){
			return;
		}		
		
		L2ItemInstance item = this._PLAYER.getInventory().getItemByItemId(this._ID_HEAL_ITEM);
		if(item == null){
			this.IS_HP = false;
			central.msgbox_Lado("AUTO HP: DISABLED, NO ITEMS", this._PLAYER, "AUTO POTS");
			return;			
		}
		if(item.getCount()<=0){
			this.IS_HP = false;
			central.msgbox_Lado("AUTO HP: DISABLED, NO ITEMS", this._PLAYER, "AUTO POTS");
			return;
		}
		if(this._PLAYER.isInOlympiad() || this._PLAYER.isInOlympiadMode()){
			central.msgbox_Lado("AUTO HP: DISABLED, FOR OLYS", this._PLAYER, "AUTO POTS");
			this.IS_HP = false;
			return;
		}
		useItem(item, true);		
	}
	
	private void healMana(int IdForUse){
		if(!general.ZEUS_AUTOPOTS_MP){
			this.IS_MP = false;
			return;			
		}		
		
		if(IdForUse != this.MP_ID_WAIT){
			this.IS_MP = false;
			return;
		}

		if(!opera.isCharOnline(this._PLAYER)){
			this.IS_MP = false;
			return;
		}
		
		if(!this.IS_MP){
			return;
		}
		
		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			@Override
			public void run(){
				healMana(MP_ID_WAIT);
			}
		}, general.ZEUS_AUTOPOTS_CHECK_MILISECOND+100);
		
		
		if(!isPlayerReady() || (this._PLAYER.getMaxMp() == this._PLAYER.getStatus().getCurrentMp())){
			return;
		}		
		
		L2ItemInstance item = this._PLAYER.getInventory().getItemByItemId(this._ID_MANA_ITEM);
		if(item == null){
			this.IS_MP = false;
			central.msgbox_Lado("AUTO MP: DISABLED, NO ITEMS", this._PLAYER, "AUTO POTS");
			return;			
		}
		if(item.getCount()<=0){
			this.IS_MP = false;
			central.msgbox_Lado("AUTO MP: DISABLED, NO ITEMS", this._PLAYER, "AUTO POTS");
			return;
		}
	
		if(this._PLAYER.isInOlympiad() || this._PLAYER.isInOlympiadMode()){
			this.IS_MP = false;
			central.msgbox_Lado("AUTO MP: DISABLED, FOR OLYS", this._PLAYER, "AUTO POTS");
			return;
		}
		
		useItem(item, true);
	}
	
	private void healCP(int IdForUse){
		if(!general.ZEUS_AUTOPOTS_CP){
			this.IS_CP = false;
			return;			
		}
		
		if(IdForUse != this.CP_ID_WAIT){
			this.IS_CP = false;
			return;
		}
	
		if(!opera.isCharOnline(this._PLAYER)){
			this.IS_CP = false;
			return;
		}
		
		if(!this.IS_CP){
			return;
		}

		ThreadPoolManager.getInstance().scheduleGeneral(new Runnable(){
			@Override
			public void run(){
				healCP(IdForUse);
			}
		}, general.ZEUS_AUTOPOTS_CHECK_MILISECOND + 100);		
		
		if(!isPlayerReady() || (this._PLAYER.getMaxCp() == this._PLAYER.getStatus().getCurrentCp())){
			return;
		}
		
		L2ItemInstance item = this._PLAYER.getInventory().getItemByItemId(this._ID_CP_ITEM);
		if(item == null){
			this.IS_CP = false;
			central.msgbox_Lado("AUTO CP: DISABLED, NO ITEMS", this._PLAYER, "AUTO POTS");
			return;			
		}
		if(item.getCount()<=0){
			this.IS_CP = false;
			central.msgbox_Lado("AUTO CP: DISABLED, NO ITEMS", this._PLAYER, "AUTO POTS");
			return;
		}
		
		if(this._PLAYER.isInOlympiad() || this._PLAYER.isInOlympiadMode()){
			this.IS_CP = false;
			central.msgbox_Lado("AUTO CP: DISABLED, FOR OLYS", this._PLAYER, "AUTO POTS");
			return;
		}
		useItem(item, true);
	}
	
	private boolean useItem(L2ItemInstance item, boolean force){
		return ItemHandler.getInstance().getHandler(item.getEtcItem()).useItem(_PLAYER, item, force);
	}
}