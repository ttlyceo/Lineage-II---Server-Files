package ZeuS.GM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import l2r.L2DatabaseFactory;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.olympiad.Olympiad;
import l2r.gameserver.model.entity.olympiad.Participant;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.L2GameServerPacket;
import l2r.gameserver.network.serverpackets.SystemMessage;
import ZeuS.Config.general;
import ZeuS.interfase.central;
import ZeuS.language.language;



public class olymp {
	protected static final String POINTS = "olympiad_points";

	public static void unbanOlys(L2PcInstance Player, L2PcInstance target){
		if(!target.isNoble()){
			central.msgbox(language.getInstance().getMsg(Player).THE_PLAYER_IS_NOT_NOBLE.replace("$player", target.getName()) , Player);
			return;
		}

		if(!general.getCharConfigBANOLY(target)){
			central.msgbox("This player is not banned for playing Olympics.", Player);
			return;
		}

		if (BD_Ban(target, 0)){
			central.msgbox_Lado(language.getInstance().getMsg(target).OLY_YOU_HAVE_BEEN_UNBANNED_FROM_OLY_BY_$gmName.replace("$gmName", Player.getName()), target);
			central.msgbox("The player "+ target.getName() +" has been unbanned from Olympics", Player);
		}else{
			central.msgbox("Problems unbanned Player", Player);
		}

	}


	protected static void broadcastPacket(L2GameServerPacket packet, L2PcInstance player)
	{
		player.sendPacket(packet);
	}

	public static void point(Participant par, boolean reset){
		if(reset){
			int Puntos = Olympiad.getInstance().getNoblePoints(par.getPlayer().getObjectId());
			par.updateStat(POINTS,-Puntos);
		}
	}

	public static void point(Participant par, int points){
		point(par,points,true);
	}

	public static void point(Participant par, int points, boolean showMensaje)
	{
		par.updateStat(POINTS, points);

		if(showMensaje){
			final SystemMessage sm;
			if(points <= 0){
				sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_LOST_S2_OLYMPIAD_POINTS);
			}else{
				sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_GAINED_S2_OLYMPIAD_POINTS);
			}
			sm.addString(par.getName());
			int PuntoDar = points < 0 ? points * -1 : points;
			sm.addInt(PuntoDar);
			broadcastPacket(sm,par.getPlayer());
		}
	}

	@SuppressWarnings("unused")
	public static void banOlys(L2PcInstance Player, L2PcInstance target){

		if(!target.isNoble()){
			central.msgbox(language.getInstance().getMsg(Player).THE_PLAYER_IS_NOT_NOBLE.replace("$player", target.getName()) , Player);
			return;
		}

		if(general.getCharConfigBANOLY(target)){
			central.msgbox("This player is already banned for playing Olympics.", Player);
			return;
		}
		int Puntos = Olympiad.getInstance().getNoblePoints(target.getObjectId());

		if (BD_Ban(target, 1)){
			central.msgbox_Lado(language.getInstance().getMsg(target).OLY_YOU_HAVE_BEEN_BANNED_FROM_OLY_BY_$gmName.replace("$gmName", Player.getName()), target);
			central.msgbox("The player "+ target.getName() +" has been Olympics banned", Player);
		}else{
			central.msgbox("Problems banned Player", Player);
		}

	}

	private static boolean BD_Ban(L2PcInstance Player, int Estado){
		int idPlayer = Player.getObjectId();
		Connection conn = null;
		try{
			conn = L2DatabaseFactory.getInstance().getConnection();
			String qry = "UPDATE zeus_char_config SET banOly = " + String.valueOf(Estado) + " WHERE idchar = " + String.valueOf(idPlayer);
			PreparedStatement psqry = conn.prepareStatement(qry);
			try{
				psqry.executeUpdate();
				psqry.close();
				conn.close();
			}catch (SQLException e) {
				return false;
			}
		}catch(SQLException e){
			return false;
		}
		general.getInstance().loadCharConfig(Player);
		return true;
	}

	public static int getPuntos(L2PcInstance par){
		if(par == null){
			return -1;
		}

		if(!par.isNoble()){
			return -1;
		}
		return Olympiad.getInstance().getNoblePoints(par.getObjectId());
	}
}
