package ZeuS.event;

import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import l2r.gameserver.model.L2ClanMember;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.skills.CommonSkill;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.serverpackets.MagicSkillUse;

public class ClanReputationEvent {

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(ClanReputationEvent.class.getName());
	
	public static void mainWindows(L2PcInstance player){
		String main = "<html><title>"+general.TITULO_NPC()+"</title><body>";
		
		main += central.LineaDivisora(1) + central.headFormat("Clan Reward","FF8000") + central.LineaDivisora(1);
		
		if(!general.EVENT_REPUTATION_CLAN){
			main = central.LineaDivisora(1) + central.headFormat("Clan Reward Disabled")+central.LineaDivisora(1);
			opera.enviarHTML(player, main);
			return;
		}

		
		String requerimientos = "<table with=280>";
		requerimientos += "<tr><td with=280 align=CENTER>Min Player: <font color=088A08>"+ String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER) +"</font</td></tr>";
		requerimientos += "<tr><td with=280 align=CENTER>All Member's online: <font color=088A08>"+ (general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE ? "YES" : "NO") +"</font></td></tr>";
		requerimientos += "</table>";
		
		main += central.LineaDivisora(1) + central.headFormat("<font color=\"FF8000\">Requirements</font><br1>" + requerimientos,"LEVEL") + central.LineaDivisora(1);

		requerimientos = "<table with=280>";
		requerimientos += "<tr><td with=280 align=CENTER>Clan Level: <font color=088A08>"+ String.valueOf(general.EVENT_REPUTATION_LVL_TO_GIVE) +"</font></td></tr>";
		requerimientos += "<tr><td with=280 align=CENTER>Clan Reputation: <font color=088A08>"+ String.valueOf(general.EVENT_REPUTATION_REPU_TO_GIVE) +"</font></td></tr>";		
		requerimientos += "</table>";
		
		main += central.LineaDivisora(1) + central.headFormat("<font color=\"FF8000\">Reward</font><br1>" + requerimientos,"LEVEL") + central.LineaDivisora(1);
	
		
		String btnPremiar = "<button value=\"Reward me\" action=\"bypass ZeuSNPC ClanReward\" width=120 height=25 back=\"L2UI_ct1.button_df\" fore=\"L2UI_ct1.button_df\">";
		
		main += central.LineaDivisora(1) + central.headFormat(btnPremiar) + central.LineaDivisora(1);
		
		
		
		main += central.getPieHTML(true) + "</body></html>";
		
		opera.enviarHTML(player, main);
		
	}
	
	public static String delegar(L2PcInstance player, String params){

		String retorno = "";
		
		if(params.equals("ClanReward")){
			if(GiveReward(player)){
				retorno = "<html><title>"+ general.TITULO_NPC()  + "</title><body>";
				retorno += central.LineaDivisora(1) + central.headFormat("Clan Reward","FF8000") + central.LineaDivisora(1);
				retorno += central.LineaDivisora(1) + central.headFormat("DONE!!!!!","LEVEL") + central.LineaDivisora(1);
				retorno += central.getPieHTML(true) + "</body></html>";
			}
		}
		
		return retorno;
	}
	
	
	public static boolean GiveReward(L2PcInstance player){
		if(!isClanLeader(player)){
			central.msgbox(language.getInstance().getMsg(player).ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION, player);
			return false;
		}
		
		if(!isAllMembersOnline(player)){
			return false;
		}
		
		if(getCountMember(player) < general.EVENT_REPUTATION_MIN_PLAYER){
			central.msgbox(language.getInstance().getMsg(player).CR_YOU_NEED_AT_LEAT_COUNT_CLAN_MEMBERS_ON.replace("$count",String.valueOf(general.EVENT_REPUTATION_MIN_PLAYER)), player);
			return false;
		}
		
		int lvlClan = player.getClan().getLevel();
		
		if(lvlClan >= general.EVENT_REPUTATION_LVL_TO_GIVE){
			central.msgbox(language.getInstance().getMsg(player).CR_YOU_CLAN_IS_INVALID, player);
			return false;
		}
		
		player.getClan().setLevel(general.EVENT_REPUTATION_LVL_TO_GIVE);
		player.getClan().changeLevel(general.EVENT_REPUTATION_LVL_TO_GIVE);
		player.getClan().setReputationScore(player.getClan().getReputationScore() + general.EVENT_REPUTATION_REPU_TO_GIVE, true);
		player.getClan().updateClanInDB();
		player.getClan().broadcastClanStatus();
		
		RewardEfect(player);
		central.msgbox(language.getInstance().getMsg(player).CR_CONGRATULATION_YOU_CLAN_HAS_BEEN_REWARDED, player);
		
		return true;
	}
	
	public static void RewardEfect(L2PcInstance player){
		for(L2ClanMember cha : player.getClan().getMembers()){
			try{
				L2PcInstance activeChar = cha.getPlayerInstance();
				L2Skill skill = CommonSkill.LARGE_FIREWORK.getSkill();
				if (skill != null)
				{
					activeChar.broadcastPacket(new MagicSkillUse(activeChar, activeChar, skill.getId(), skill.getLevel(), skill.getHitTime(), skill.getReuseDelay()));
				}			
			}catch(Exception a){
				
			}
		}
	}
	

	public static boolean isClanLeader(L2PcInstance player){
		if(player.getClan()==null){
			return false;
		}
		return player.isClanLeader();
	}
	
	@SuppressWarnings("unused")
	public static boolean isAllMembersOnline(L2PcInstance player){
		Vector<L2ClanMember> Playerss = new Vector<L2ClanMember>();
		
		if(!general.EVENT_REPUTATION_NEED_ALL_MEMBER_ONLINE){
			return true;
		}
		for(L2ClanMember cha : player.getClan().getMembers()){
			if(cha!=null){
				if(cha.isOnline()){
					if(!cha.getPlayerInstance().getClient().isDetached()){
						Playerss.add(cha);
					}
				}
			}
		}
		if(Playerss == null){
			central.msgbox(language.getInstance().getMsg(player).CR_YOU_NEED_ALL_MEMBERS_ONLINE, player);
			return false;
		}else{
			if(Playerss.size() < general.EVENT_REPUTATION_MIN_PLAYER){
				central.msgbox(language.getInstance().getMsg(player).CR_YOU_NEED_ALL_MEMBERS_ONLINE, player);
				return false;
			}
		}
		return true;
	}
	
	private static int getCountMember(L2PcInstance player){
		return player.getClan().getMembersCount();
	}
	
	@SuppressWarnings("unused")
	private static void UpLevelToClan(L2PcInstance player){
		
	}
	
}
