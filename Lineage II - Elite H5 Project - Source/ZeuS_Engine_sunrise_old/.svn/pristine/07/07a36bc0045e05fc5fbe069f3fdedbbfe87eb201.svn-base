package ZeuS.Config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import ZeuS.interfase.sellBuff;
import ZeuS.procedimientos.opera;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class _buffStoreSkillInfo {
	private Map<Integer, _buffStoreDataSellInfo> idPlayers = new HashMap<Integer, _buffStoreDataSellInfo>(); 

	private void removeMe(L2PcInstance player){
		removeMe(player.getObjectId());
	}
	private void removeMe(int idPlayer){
		if(idPlayers!=null){
			if(idPlayers.size()>0){
				if(idPlayers.containsKey(idPlayer)){
					idPlayers.remove(idPlayer);
				}
			}
		}
	}
	
	
	
	public void checkIsInBuffStore(){
		checkIsInBuffStore(-1);
	}	

	@SuppressWarnings("rawtypes")
	public void checkIsInBuffStore(int idPlayerToRemove){
		if(idPlayers==null){
			return;
		}else if(idPlayers.size()==0){
			return;
		}
		if(idPlayerToRemove<0){
			Iterator itr = idPlayers.entrySet().iterator();
			while(itr.hasNext()){
				Map.Entry Entrada = (Map.Entry)itr.next();
				int idPlayer = (int)Entrada.getKey();
				L2PcInstance ppl = opera.getPlayerbyID(idPlayer);
				if(!opera.isCharOnline(ppl)){
					removeMe(ppl);
				}else if(!sellBuff.isBuffSeller(ppl)){
					removeMe(ppl);
				}
			}
		}else{
			if(idPlayers.containsKey(idPlayerToRemove)){
				removeMe(idPlayerToRemove);
			}
		}
	}
	
	public _buffStoreSkillInfo(L2PcInstance player, int idSkill, int _idItem, long _price, int level){
		setData(player, idSkill, _idItem, _price, level);		
	}
	public void setData(L2PcInstance player, int idSkill, int _idItem, long _price, int level){
		if(idPlayers!=null){
			if(idPlayers.size()>0){
				if(idPlayers.containsKey(player.getObjectId())){
					idPlayers.remove(player.getObjectId());
				}
			}
		}
		_buffStoreDataSellInfo temp = new _buffStoreDataSellInfo(idSkill, _idItem, _price, level, player);
		idPlayers.put(player.getObjectId(), temp);
	}
	
	public final Map<Integer, _buffStoreDataSellInfo> getAllPlayer(){
		return this.idPlayers;
	}
}