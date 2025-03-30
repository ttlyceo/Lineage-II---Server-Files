package ZeuS.interfase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.logging.Logger;

import l2r.L2DatabaseFactory;
import l2r.gameserver.GameServer;
import l2r.gameserver.data.sql.ClanTable;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.SystemMessage;
import ZeuS.Config.general;
import ZeuS.language.language;

public class clanNomCambio {

		private static final Logger _log = Logger.getLogger(clanNomCambio.class.getName());

		public static boolean changeNameClan(String newName, L2PcInstance Player)

		{
			if(!general._activated()){
				return false;
			}
			if (Player.getClan() == null)
			{
				Player.sendMessage(language.getInstance().getMsg(Player).ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION);
			}
			else
			{
				try
				{
					if (!Player.isClanLeader())
					{
					Player.sendMessage(language.getInstance().getMsg(Player).ONLY_CLAN_LEADER_CAN_MAKE_THIS_OPERATION);
						return false;
					}
					else if (null != ClanTable.getInstance().getClanByName(newName))
					{
						Player.sendMessage(language.getInstance().getMsg(Player).CLAN_NAME_ALREADY_EXISTS.replace("$name", newName));
						return false;
					}
					else if (!newName.matches("^[a-zA-Z0-9]+$"))
					{
						Player.sendMessage(language.getInstance().getMsg(Player).THE_NAME_ENTERED_IS_NOT_VALID);
					}
					else
					{
						Player.getClan().setName(newName);

						try (Connection con = L2DatabaseFactory.getInstance().getConnection();
							PreparedStatement statement = con.prepareStatement("UPDATE clan_data SET clan_name=? WHERE clan_id=?"))
						{
							statement.setString(1, newName);
							statement.setInt(2, Player.getClan().getId());
							statement.execute();
							statement.close();
						}
						catch (Exception e)
						{
						_log.info("Error updating clan name for player " + Player.getName() + ". Error: " + e);
							return false;
						}
						String Mensaje = language.getInstance().getMsg(Player).CLAN_NAME_CHANGE.replace("$name", newName);
						SystemMessage msg = SystemMessage.sendString(Mensaje);
						Player.sendMessage(Mensaje);
						Player.broadcastUserInfo();
						Player.getClan().broadcastClanStatus();
						Player.getClan().broadcastToOnlineMembers(msg);
					}
				}
			catch (Exception e)
				{
					Player.sendMessage(language.getInstance().getMsg(Player).THE_NAME_ENTERED_IS_NOT_VALID);
					return false;
				}
			}
			return true;
		}
}
