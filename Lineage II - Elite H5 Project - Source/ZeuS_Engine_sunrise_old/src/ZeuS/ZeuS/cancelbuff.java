package ZeuS.ZeuS;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

import l2r.gameserver.GameServer;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.L2Effect;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.SystemMessage;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;

public class cancelbuff {

	@SuppressWarnings("unused")
	private final Logger _log = Logger.getLogger(cancelbuff.class.getName());

	private void addcancelbuffs(L2PcInstance player, L2Effect effect){
		if(!general.BUFF_REMOVED.containsKey(player)){
			general.BUFF_REMOVED.put(player, new ArrayList<>());
		}
		general.BUFF_REMOVED.get(player).add(effect);
	}

		
	public boolean isPlayerWithCancelBuffReturned(L2PcInstance player) {
		if(general.BUFF_REMOVED != null) {
			if(general.BUFF_REMOVED.size()>0) {
				if(general.BUFF_REMOVED.containsKey(player)) {
					return general.BUFF_REMOVED.get(player).size() > 0;
				}
			}
		}		
		return false;
	}
	
	public boolean isBuffInCancelList(L2PcInstance player, L2Effect _buff) {
		if(general.BUFF_REMOVED != null) {
			if(general.BUFF_REMOVED.size()>0) {
				if(general.BUFF_REMOVED.containsKey(player)) {
					return general.BUFF_REMOVED.get(player).contains(_buff);
				}
			}
		}
		return false;
	}	
	
	private void clearcancelbuffs(L2PcInstance player){
		if(general.BUFF_REMOVED.containsKey(player)){
			general.BUFF_REMOVED.remove(player);
		}
	}

	public boolean canStealBuff(L2Effect effect){
		return !general.RETURN_BUFF_NOT_STEALING.contains(effect.getSkill().getId());
	}

	public void setCancel(L2PcInstance player, L2Effect buff){

		if(!general.RETURN_BUFF && !general.RETURN_BUFF_IN_OLY ){
			return;
		}

		int SegundosSeleccionados = 0;

		if(player.isInOlympiadMode() || player.isOlympiadStart()){
			if(general.RETURN_BUFF_IN_OLY){
				SegundosSeleccionados = general.RETURN_BUFF_IN_OLY_SECONDS_TO_RETURN;
			}else{
				return;
			}
		}else{
			SegundosSeleccionados = general.RETURN_BUFF_SECONDS_TO_RETURN;
		}



		if(!general.CANCEL_TASK.containsKey(player)){
			general.CANCEL_TASK.put(player, false);
		}

		addcancelbuffs(player, buff);
		if(!general.CANCEL_TASK.get(player)){
			ThreadPoolManager.getInstance().executeGeneral(new ReturnEffects(SegundosSeleccionados * 1000, player));
			general.CANCEL_TASK.put(player, true);
			central.msgbox_Lado(language.getInstance().getMsg(player).CANCEL_BUFF_RETURNED_IN_SECONDS.replace("$timeSeconds", String.valueOf(SegundosSeleccionados)), player, "Cancel Buff");
		}
	}




	class ReturnEffects implements Runnable{
		private int _time;
		List<L2Effect> _list;
		L2Character _player;

		public ReturnEffects (int time, L2Character player){
			_time = time;
			_list = general.BUFF_REMOVED.get(player);
			_player = player;
		}

		@Override
		public void run(){
			if (_time > 0){
				ThreadPoolManager.getInstance().scheduleGeneral(this, 1000);
				_time-=1000;
			}

			if (_time==0){

				if(_list==null){
					return;
				}

				if(_list.size()==0){
					return;
				}

				if(general.CANCEL_TASK.get(_player)){
					for (L2Effect e : _list){
						L2Skill skill = SkillData.getInstance().getInfo(e.getSkill().getId(), e.getLevel());
						if(skill != null && Arrays.stream(SKILL_ID_BLOCK_CANCEL).noneMatch(y->y == skill.getId())){
							skill.getEffects(_player, _player);
							SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_FEEL_S1_EFFECT);
							sm.addSkillName(e);
							((L2PcInstance)_player).sendPacket(sm);
						}
					}
					try{
						general.CANCEL_TASK.put((L2PcInstance)_player, false);
						central.msgbox_Lado(language.getInstance().getMsg((L2PcInstance)_player).CANCEL_BUFF_YOUR_CANCEL_BUFF_HAVE_RETURNED, (L2PcInstance)_player, "Cancel Buff");
					}catch (Exception e){
						//
					}

				}
				clearcancelbuffs((L2PcInstance)_player);
			}
		}
	}
	private int[] SKILL_ID_BLOCK_CANCEL = {
			279,6090,5145,1422,1169,4108,4689
	};

  	public static cancelbuff getInstance()
	{
	    return SingletonHolder._instance;
	}


	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder{
	    protected static final cancelbuff _instance = new cancelbuff();
	}
}
