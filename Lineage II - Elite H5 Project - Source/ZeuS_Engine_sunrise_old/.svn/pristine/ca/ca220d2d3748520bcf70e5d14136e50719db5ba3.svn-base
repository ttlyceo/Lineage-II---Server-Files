package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Comunidad.EngineForm.C_gmcommand;
import ZeuS.Config._cumulativeData;
import ZeuS.Config._cumulativeSkill;
import ZeuS.procedimientos.opera;
import l2r.L2DatabaseFactory;
import l2r.gameserver.dao.factory.impl.DAOFactory;
import l2r.gameserver.data.xml.impl.SkillData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.base.SubClass;
import l2r.gameserver.model.skills.L2Skill;

public class cumulativeSubclass {
	private String insertQuery = "REPLACE INTO zeus_cumulative_subclass() values(?,?,?,?,?,?)";
	private String updateCumulativeSubClass = "UPDATE zeus_cumulative_subclass SET classIndex=? WHERE idchar=? AND idclass=?";
	private String deleteQuery = "DELETE FROM zeus_cumulative_subclass WHERE idchar=? AND classIndex=? and idskill=?";
	private String deleteQueryCumulative = "DELETE FROM zeus_cumulative_subclass WHERE idchar=? AND classIndex=?";
	private String selectAllSkillFromDB = "SELECT idskill, levelskill, classIndex FROM zeus_cumulative_subclass WHERE idchar=?";

	private cumulativeSubclass _instance;
	
	private static Logger _log = Logger.getLogger(cumulativeSubclass.class.getName());
	
	public void checkSkillByLevelUp(L2PcInstance player) {
		if(!_cumulativeData.isEnabled()) {
			return;
		}
		if(player.getSubClasses()!=null){
			if(player.getSubClasses().size()>0) {
				for(SubClass SubClassToCheck : player.getSubClasses().values()) {
					List<_cumulativeSkill> skillFromSubClass = _cumulativeData.getAllSkillToGive(player, SubClassToCheck.getClassId() , player.getTotalSubClasses());
					if(skillFromSubClass==null) {
						return;
					}
					if(skillFromSubClass.size()==0) {
						return;
					}
					for(_cumulativeSkill skillGive : skillFromSubClass) {
						L2Skill skillG = SkillData.getInstance().getSkill(skillGive.getSkillID() , skillGive.getSkillLevel());
						if(skillG != null) {
							player.addSkill(skillG, true);
						}
					}
				}
			}
		}
	}
	
	public void createSkillBase(L2PcInstance player) {
		if(_cumulativeData.isEnabled()) {
			Connection con = null;
			PreparedStatement statement = null;			
			boolean HaveData = true;
			String Consulta = "SELECT zeus_cumulative_subclass.idchar FROM zeus_cumulative_subclass WHERE idchar=? and classIndex=0";
			try{
				con = L2DatabaseFactory.getInstance().getConnection();
				statement = con.prepareStatement(Consulta);
				statement.setInt(1, player.getObjectId());
				try (ResultSet rset = statement.executeQuery()){
					if(rset.next()){
						HaveData = false;						
					}
				}catch(Exception a) {
					_log.warning("(1) Error getting Skill Cumulative Subclass Base Info : " + a.getMessage());
				}
			}catch(Exception e) {
				_log.warning("(2) Error getting Skill Cumulative Subclass Base Info : " + e.getMessage());
			}
			try {
				con.close();
			}catch(Exception a) {
				
			}
			if(HaveData) {
				for(L2Skill SkillData : player.getSkills().values()) {
					insertData(player.getObjectId(), player.getBaseClass(), SkillData.getId(), SkillData.getLevel(), 0);
				}
			}
		}
	}
	
	public void deleteSubClass(L2PcInstance player, int IdClassToRemove, int IdClassIndex) {
		Vector<SubClass>classes_temp = new Vector<SubClass>();
		if(player.getSubClasses()!=null) {
			if(player.getSubClasses().size()>0) {
				Vector<Integer> SkillToRemove = new Vector<Integer>();
				SkillToRemove = getAllSkillToRemove(player, IdClassIndex);
				
				DAOFactory.getInstance().getSubclassDAO().delete(player, IdClassIndex);
				player.getSubClasses().remove(IdClassIndex);
				deletesubclassInformation(player, IdClassIndex);
				
				if(player.getSubClasses()!=null) {
					if(player.getSubClasses().size()>0) {
						for(SubClass temSub : player.getSubClasses().values()) {
							if(temSub.getClassIndex()>IdClassIndex) {
								temSub.setClassIndex(temSub.getClassIndex() - 1);
							}
							classes_temp.add(temSub);			
						}
						player.getSubClasses().clear();
						for (SubClass ttp: classes_temp) {
							player.getSubClasses().put(ttp.getClassIndex(), ttp);
							_updateCumulativeSubClass(player, ttp.getClassId(), ttp.getClassIndex());
						}
					}
				}
				
				
				if(SkillToRemove!=null) {
					if(SkillToRemove.size()>0) {
						for(int IdSkill : SkillToRemove) {
							try {
								L2Skill SkillDataRemove = player.getSkills().get(IdSkill);
								player.removeSkill(SkillDataRemove);
							}catch(Exception a) {
								_log.warning("Cumulative Delete Skill Error: " + a.getMessage() + " - " + IdSkill);
							}
						}
					}
				}
				player.store();
				player.broadcastUserInfo(true);				
				
			}
		}
	}
	
	private Vector<Integer> getAllSkillToRemove(L2PcInstance player, int IdClassIndexToRemove){
		Vector<Integer>SKILL_TO_REMOVE = new Vector<Integer>();
		Vector<Integer>ALL_SKILL_FROM_OTHER_CLASS = new Vector<Integer>();
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(selectAllSkillFromDB);
			statement.setInt(1, player.getObjectId());
			try (ResultSet rset = statement.executeQuery()){
				while (rset.next()){
					if(rset.getInt("classIndex") == IdClassIndexToRemove) {
						SKILL_TO_REMOVE.add(rset.getInt("idskill"));
					}else {
						ALL_SKILL_FROM_OTHER_CLASS.add(rset.getInt("idskill"));
					}
				}
			}catch(Exception a) {
				_log.warning("(1) Error getting Skill to Remove From Cumulative Subclass : " + a.getMessage());
			}
		}catch(Exception e) {
			_log.warning("(2) Error getting Skill to Remove From Cumulative Subclass : " + e.getMessage());
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}
		
		if(ALL_SKILL_FROM_OTHER_CLASS !=null) {
			for(int IdSKillCheck : ALL_SKILL_FROM_OTHER_CLASS) {
				if(SKILL_TO_REMOVE.contains(IdSKillCheck)) {
					SKILL_TO_REMOVE.removeElement(IdSKillCheck) ;
				}
			}
		}
		
		return SKILL_TO_REMOVE;
	}
	
	private void deletesubclassInformation(L2PcInstance player, int IdClassIndex) {
		Connection con = null;
		PreparedStatement statement = null;		
		//deleteQueryCumulative = "DELETE FROM zeus_cumulative_subclass WHERE idchar=? AND classIndex=?";
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(deleteQueryCumulative);
			statement.setInt(1, player.getObjectId());
			statement.setInt(2, IdClassIndex);
			statement.execute();
		}catch(Exception a){
			_log.warning("character_subclass Update Info Fail: " + a.getMessage());
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}
	}
	
	private void _updateCumulativeSubClass(L2PcInstance player, int IdClass, int IdClassIndex) {
		Connection con = null;
		PreparedStatement statement = null;			
		// private String updateCumulativeSubClass = "UPDATE zeus_cumulative_subclass SET classIndex=? WHERE idchar=? AND idclass=?";
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(updateCumulativeSubClass);
			statement.setInt(1, IdClassIndex);
			statement.setInt(2, player.getObjectId());
			statement.setInt(3, IdClass);
			statement.execute();
		}catch(Exception a){
			_log.warning("updateCumulativeSubClass Update Info Fail: " + a.getMessage());
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}
	}
	
	
	public void insertData(int IdPlayer, int IdClass, int IdSkill, int LevelSkill, int ClassIndex) {
		Connection con = null;
		PreparedStatement statement = null;
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			statement = con.prepareStatement(insertQuery);
			statement.setInt(1, IdPlayer);
			statement.setInt(2, IdClass);
			statement.setInt(3, IdSkill);
			statement.setInt(4, LevelSkill);
			statement.setString(5, opera.getClassName(IdClass));
			statement.setInt(6, ClassIndex);
			statement.execute();
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
		
		}		
	}
	
	private void deleteSkills(int IdPlayer, int IdSkill, int IdClassIndex) {
		Connection con = null;
		PreparedStatement ins = null;
		//deleteQuery = "DELETE FROM zeus_cumulative_subclass WHERE idchar=? AND classIndex=? and idskill=?";
		try{
			con = L2DatabaseFactory.getInstance().getConnection();
			ins = con.prepareStatement(deleteQuery);
			ins.setInt(1, IdPlayer);
			ins.setInt(2, IdClassIndex);
			ins.setInt(3, IdSkill);
			try{
				ins.executeUpdate();
			}catch(SQLException e){

			}
			
		}catch(Exception a){
			
		}
		try {
			con.close();
		}catch(Exception a) {
			
		}
	}
	
	public static cumulativeSubclass getInstance() {
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder{
		protected static final cumulativeSubclass _instance = new cumulativeSubclass();
	}
	
}
