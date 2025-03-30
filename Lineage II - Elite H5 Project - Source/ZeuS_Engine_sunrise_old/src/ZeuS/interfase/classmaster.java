package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.logging.Logger;

import ZeuS.procedimientos.opera;
import l2r.Config;
import l2r.gameserver.GameServer;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.ClassId;

public class classmaster {

	private static final Logger _log = Logger.getLogger(classmaster.class.getName());


	@SuppressWarnings("unused")
	private static final int[] ParentProfesion ={0,0,1,1,0,4,4,0,7,7,10,11,11,11,10,15,15,18,19,19,18,22,22,25,26,26,25,29,31,32,33,31,35,35,38,39,39,
38,42,44,45,44,47,49,50,50,53,54,53,56,2,3,5,6,9,8,12,13,14,16,17,20,21,23,24,27,26,30,33,34,36,37,40,41,43,46,48,51,52,55,123,124,125,
125,126,126,127,128,129,130,126,135};


	Connection conn;
	String ConsultaMYSQL;
	PreparedStatement act;
	ResultSet rs;

	public static boolean AddProfesion(L2PcInstance player, int idProfecion, int level){
		boolean removerItem = false;
		if(Config.CLASS_MASTER_SETTINGS != null){
			if (Config.CLASS_MASTER_SETTINGS.getRequireItems(level) != null){
				if(!Config.CLASS_MASTER_SETTINGS.getRequireItems(level).isEmpty()){
					for (int _itemId : Config.CLASS_MASTER_SETTINGS.getRequireItems(level).keySet())
					{
						int _count = Config.CLASS_MASTER_SETTINGS.getRequireItems(level).get(_itemId);
						if(!opera.haveItem(player, _itemId, _count)){
							return false;
						}
					}
					removerItem = true;
				}
			}
		}
		
		
		final ClassId currentClassId = player.getClassId();
		
		if(currentClassId.level()==level){
			_log.warning("Error when the player " + player.getName() + " try to choose Class");
			return false;
		}
		
		
		try{
			player.setClassId(idProfecion);
			if(player.isSubClassActive()){
				player.getSubClasses().get(player.getClassIndex()).setClassId(player.getActiveClass());
			}else{
	    		player.setBaseClass(player.getActiveClass());
			}
			player.broadcastUserInfo();
			
			if(removerItem){
				for (int _itemId : Config.CLASS_MASTER_SETTINGS.getRequireItems(level).keySet())
				{
					int _count = Config.CLASS_MASTER_SETTINGS.getRequireItems(level).get(_itemId);
					opera.removeItem(_itemId, _count, player);
				}				
			}
			
			return true;
		}catch(Exception a){
			_log.warning("CLASS MASTER->"+a.getMessage());
			return false;
		}
	}
}
