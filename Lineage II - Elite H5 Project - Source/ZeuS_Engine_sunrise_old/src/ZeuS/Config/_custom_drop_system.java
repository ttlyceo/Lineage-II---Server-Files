package ZeuS.Config;

import java.util.Vector;
import java.util.logging.Logger;


public class _custom_drop_system {
	private final static Logger _log = Logger.getLogger(_custom_drop_system.class.getName());	
	private enum _types{
		BY_LEVEL,
		BY_NPC
	};
	private final int _ITEM_ID;
	private final _types _TYPE;
	private final long _CHANCE_TO_ADD;
	private final long _RATE_TO_ADD;
	private Vector<_levels> _LEVELS = new Vector<_levels>();
	private Vector<Integer> _NPC_ID = new Vector<Integer>();
	public int getItemId(){
		return this._ITEM_ID;
	}
	public long getChancePorcent(int level, int npcId){
		if(_TYPE == _types.BY_NPC && npcId<=0){
			return 0;
		}
		if(_TYPE == _types.BY_LEVEL && level <=0){
			return 0;
		}
		if(npcId > 0 && _TYPE == _types.BY_NPC){
			if(_NPC_ID != null){
				if(_NPC_ID.size()>0){
					if(_NPC_ID.contains(npcId)){
						return this._CHANCE_TO_ADD;
					}
				}
			}
		}
		if(level > 0 && _TYPE == _types.BY_LEVEL){
			if(_LEVELS != null){
				if(_LEVELS.size()>0){
					for(_levels tmp : _LEVELS){
						if(tmp.isBetween(level)){
							return this._CHANCE_TO_ADD;
						}
					}
				}
			}
		}
		return 0;
	}
	public long getRatePorcent(int level, int npcId){
		if(_TYPE == _types.BY_NPC && npcId<=0){
			return 0;
		}
		if(_TYPE == _types.BY_LEVEL && level <=0){
			return 0;
		}
		if(npcId > 0 && _TYPE == _types.BY_NPC){
			if(_NPC_ID != null){
				if(_NPC_ID.size()>0){
					if(_NPC_ID.contains(npcId)){
						return this._RATE_TO_ADD;
					}
				}
			}
		}
		if(level > 0 && _TYPE == _types.BY_LEVEL){
			if(_LEVELS != null){
				if(_LEVELS.size()>0){
					for(_levels tmp : _LEVELS){
						if(tmp.isBetween(level)){
							return this._RATE_TO_ADD;
						}
					}
				}
			}
		}
		return 0;
	}	
	public _custom_drop_system(int idItem, String type, long chance, long rate){
		this._ITEM_ID = idItem;
		this._CHANCE_TO_ADD = chance;
		this._RATE_TO_ADD = rate;		
		if(type.equalsIgnoreCase("level")){
			_TYPE = _types.BY_LEVEL;
		}else if(type.equalsIgnoreCase("npc_id")){
			_TYPE = _types.BY_NPC;
		}else{
			_TYPE = null;
			return;
		}
	}
	public void setIdNpc(int _idNpc){
		this._NPC_ID.add(_idNpc);
	}
	public void setLevels(int _min, int _max){
		_levels tmp = new _levels(_min, _max);
		_LEVELS.add(tmp);
	}
}
class _levels{
	private final int MIN;
	private final int MAX;
	private final static Logger _log = Logger.getLogger(_custom_drop_system.class.getName());	
	public _levels(int _min, int _max){
		this.MIN = _min;
		this.MAX = _max;
	}
	public boolean isBetween(int _level){
		return this.MIN <= _level && this.MAX >= _level;
	}
}