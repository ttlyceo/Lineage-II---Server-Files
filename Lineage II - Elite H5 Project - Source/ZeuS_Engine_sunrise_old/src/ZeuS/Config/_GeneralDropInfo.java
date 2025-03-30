package ZeuS.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import l2r.gameserver.data.sql.NpcTable;
import l2r.gameserver.model.L2DropCategory;
import l2r.gameserver.model.L2DropData;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;

public class _GeneralDropInfo {

	private static Map<Integer, _SelfInfoGeneralMobs> INFO_MOBS_DROP = new HashMap<Integer, _SelfInfoGeneralMobs>();
	private static Map<Integer, Vector<_InfoGeneralDrop>> MOBS_DROPS_ALL = new HashMap<Integer, Vector<_InfoGeneralDrop>>();
	
	
	public static Vector<_InfoGeneralDrop> getAllDropFromMob(int idMob){
		if(MOBS_DROPS_ALL.containsKey(idMob)){
			return MOBS_DROPS_ALL.get(idMob);
		}
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(idMob);
		Vector<_InfoGeneralDrop>vTemporal = new Vector<_InfoGeneralDrop>();
		if (tmpl.getDropData() != null)
		{
			for (L2DropCategory cat : tmpl.getDropData()){
				for(L2DropData DropIn : cat.getAllDrops()){
					//String ttt = String.valueOf(DropIn.getId())+":"+String.valueOf(DropIn.getMinDrop())+":"+String.valueOf(DropIn.getMaxDrop())+":"+String.valueOf(DropIn.getChance())+":"+String.valueOf(cat.getCategoryType());
					_InfoGeneralDrop ttt = new _InfoGeneralDrop(DropIn.getId(), DropIn.getChance(), DropIn.getMinDrop(), DropIn.getMaxDrop(), idMob ,  ( cat.isSweep() ? false : true ), cat.getCategoryType(), DropIn.isQuestDrop());
					vTemporal.add(ttt);
				}
			}
		}
		MOBS_DROPS_ALL.put(idMob, vTemporal);
		return vTemporal;
	}
	
	
	public static _InfoGeneralDrop getDropInfoById(int idMob, int idItem){
		if(INFO_MOBS_DROP.containsKey(idMob)){
			return INFO_MOBS_DROP.get(idMob).getDropData(idItem);
		}
		_SelfInfoGeneralMobs _newMob = new _SelfInfoGeneralMobs(idMob);
		_newMob.getDropData(idItem);
		INFO_MOBS_DROP.put(idMob, _newMob);
		return _newMob.getDropData(idItem);
	}
	
}

class _SelfInfoGeneralMobs{
	private final int ID_MOB;
	private Map<Integer, _InfoGeneralDrop> ITEM_DROP_INFO = new HashMap<Integer, _InfoGeneralDrop>();
	
	public _SelfInfoGeneralMobs(int MobId){
		this.ID_MOB = MobId;
	}
	public _InfoGeneralDrop getDropData(int IdItem){
		_InfoGeneralDrop _returnData = null;
		L2NpcTemplate tmpl = NpcTable.getInstance().getTemplate(this.ID_MOB);
		if(tmpl != null) {
			if(this.ITEM_DROP_INFO!=null){
				if(this.ITEM_DROP_INFO.containsKey(IdItem)){
					return this.ITEM_DROP_INFO.get(IdItem);
				}
			}
			
			if (tmpl.getDropData() != null){
				for (L2DropCategory cat  : tmpl.getDropData()){
					for(L2DropData DropIn : cat.getAllDrops()){
						_InfoGeneralDrop tempData = new _InfoGeneralDrop(DropIn.getId(), DropIn.getChance(), DropIn.getMinDrop(), DropIn.getMaxDrop(), this.ID_MOB, ( cat.isSweep() ? false : true ), cat.getCategoryType(), DropIn.isQuestDrop());
						this.ITEM_DROP_INFO.put(DropIn.getId(), tempData);
					}
				}			
			}
		}
		
		if(this.ITEM_DROP_INFO.containsKey(IdItem)){
			_returnData = this.ITEM_DROP_INFO.get(IdItem);
		}else{
			_InfoGeneralDrop tempData = new _InfoGeneralDrop(IdItem, -1, -1, -1, this.ID_MOB, true, 1, false);
			this.ITEM_DROP_INFO.put(IdItem, tempData);
			_returnData = this.ITEM_DROP_INFO.get(IdItem);
		}
		
		return _returnData;
	}
}


