package ZeuS.Config;

import java.util.Vector;
import java.util.logging.Logger;

import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.L2Skill;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;

public class buffcommunitySchemme {
	
	private Logger _log = Logger.getLogger(buffcommunitySchemme.class.getName());
	
	private Vector<Integer>BUFF = new Vector<Integer>();
	private String NAME;
	private String ICON;
	private int PRICE;
	private int MIN_LEVEL;
	private int IDSCHEME;
	private boolean ACTIVE;
	private boolean USING_FLAG;
	private boolean USING_KARMA;
	private boolean USING_IN_COMBAT_MODE;
	private int FREE_UNTIL_LEVEL;
	private int ID_ITEM_REQUEST;
	
	public int getFreeUntilLevel(){
		return this.FREE_UNTIL_LEVEL;
	}
	
	public boolean isActive(){
		return this.ACTIVE;
	}
	
	public String getIcon(){
		return this.ICON;
	}
	
	public String getName(){
		return this.NAME;
	}
	
	public int getPrice(){
		return this.PRICE;
	}
	
	public int getMinLevel(){
		return this.MIN_LEVEL;
	}
	
	public int getIDScheme(){
		return this.IDSCHEME;
	}
	
	public buffcommunitySchemme(int _ID, String _BUFF, String _NAME, String _ICON, int _PRICE, int _MIN_LEVEL, boolean _ACTIVE, boolean _use_flag, boolean _use_karma, boolean _combat_mode, int _freeUntilLevel, int _ID_ITEM_REQUEST){
		this.IDSCHEME = _ID;
		getBuffFromString(_BUFF);
		this.NAME = _NAME;
		this.ICON = _ICON;
		this.PRICE = _PRICE;
		this.MIN_LEVEL = _MIN_LEVEL;
		this.ACTIVE = _ACTIVE;
		this.USING_FLAG = _use_flag;
		this.USING_KARMA = _use_karma;
		this.USING_IN_COMBAT_MODE = _combat_mode;
		this.FREE_UNTIL_LEVEL = _freeUntilLevel;
		this.ID_ITEM_REQUEST = _ID_ITEM_REQUEST;
	}
	
	@SuppressWarnings("unused")
	public boolean buffChar(L2PcInstance player, boolean BuffChar){
		boolean retorno = true;
		
		int LevelPlayer = player.getLevel();
		
		boolean isFree = (LevelPlayer <= this.FREE_UNTIL_LEVEL);
		
		if(!isFree){
			if(!opera.haveItem(player, this.ID_ITEM_REQUEST, this.PRICE)){
				return false;
			}
		}
		
		if(!BuffChar){
			if(player.getSummon()==null){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NOT_HAVE_PET, player);
				return false;
			}else if(player.getSummon().isDead()){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NOT_HAVE_PET, player);
				return false;				
			}
		}else{
			if(player.isDead()){
				central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NEED_TO_BE_ALIVE, player);
				return false;
			}
		}
		
		if(this.MIN_LEVEL > player.getLevel()){
			central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_NEED_A_MINIMUN_LEVEL_TO_USE_THIS.replace("$level",String.valueOf(this.MIN_LEVEL)), player);
			return false;
		}
		
		if(!this.USING_FLAG && player.isCombatFlagEquipped()){
			central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
			return false;
		}
		
		if(!this.USING_KARMA && player.getKarma()>0){
			central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
			return false;			
		}
		
		if(!this.USING_IN_COMBAT_MODE && player.isInCombat()){
			central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_YOU_CANT_USE_ME_IN_THIS_STATE_OR_ZONE, player);
			return false;
		}
		
		
		if(general.BUFFCHAR_JUST_ON_PEACE_ZONE){
			if(!player.isInsideZone(ZoneIdType.PEACE)){
				if(player.getInstanceId()>0){
					if(!general.BUFFCHAR_USE_INSTANCE){
						central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_JUST_IN_PEACE_ZONE, player);
						return false;						
					}
				}else{
					central.msgbox(language.getInstance().getMsg(player).BUFFERCHAR_JUST_IN_PEACE_ZONE, player);
					return false;
				}
			}
		}
		if(!isFree){
			opera.removeItem(this.ID_ITEM_REQUEST, this.PRICE, player);
		}
		for(int idBuff : BUFF){
			try{
				int lvlBuff = SkillData.getInstance().getMaxLevel(idBuff);
				L2Skill BuffDar = SkillData.getInstance().getInfo(idBuff, lvlBuff);
				if(BuffDar == null) {
					continue;
				}
				if(BuffChar){
					SkillData.getInstance().getInfo(idBuff, lvlBuff).getEffects(player, player);
				}else{
					SkillData.getInstance().getInfo(idBuff, lvlBuff).getEffects(player.getSummon(), player.getSummon());
				}
			}catch(Exception a){
				
			}
		}
		
		return retorno;
	}
	
	private void getBuffFromString(String Buff){
		this.BUFF.clear();
		for(String BB : Buff.split(",")){
			try{
				BUFF.add(Integer.valueOf(BB));
			}catch(Exception a){
				_log.warning("Cant add this value on Scheme->" + BB  + " ->" + a.getMessage());
			}
		}
	}
	public buffcommunitySchemme(){
		
	}
}
