/*
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.voicedcommandhandlers;

import l2r.Config;
import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.serverpackets.InventoryUpdate;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

import ZeuS.language.language;
import ZeuS.procedimientos.opera;

/**
 * This class trades Gold Bars for Adena and vice versa.
 * @author Ahmed
 */
public class Banking implements IVoicedCommandHandler
{
	private static String[] _voicedCommands =
	{
		"bank",
		"withdraw",
		"deposit",
		"withdrawDO",
		"depositDO"
	};
	
	@Override
	public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
	{
		if (command.equalsIgnoreCase("bank"))
		{
			String ByPassDeposit = "bypass -h voice .deposit";
			String ByPassWithDraw = "bypass -h voice .withdraw";
			final NpcHtmlMessage html = new NpcHtmlMessage();
			html.setFile(activeChar, activeChar.getHtmlPrefix(), "config/zeus/htm/" + language.getInstance().getFolder(activeChar) + "/bank.html");
			html.replace("%SERVER_LOGO%", opera.getImageLogo(activeChar));
			html.replace("%LINK_DEPOSIT%", ByPassDeposit);
			html.replace("%LINK_WITHDRAW%", ByPassWithDraw);
			
			html.replace("%ADENA%", String.valueOf(Config.BANKING_SYSTEM_ADENA));
			html.replace("%GOLD_BAR%", String.valueOf(Config.BANKING_SYSTEM_GOLDBARS));
			
			activeChar.sendPacket(html);
			activeChar.sendMessage(".deposit (" + Config.BANKING_SYSTEM_ADENA + " Adena = " + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar) / .withdraw (" + Config.BANKING_SYSTEM_GOLDBARS + " Goldbar = " + Config.BANKING_SYSTEM_ADENA + " Adena)");
		}
		else if (command.equalsIgnoreCase("deposit"))
		{
			String ByPass = "bypass -h voice .depositDO $txtGoldBar";
			// deposit.html
			final NpcHtmlMessage html = new NpcHtmlMessage();
			html.setFile(activeChar, activeChar.getHtmlPrefix(), "config/zeus/htm/" + language.getInstance().getFolder(activeChar) + "/deposit.html");
			html.replace("%SERVER_LOGO%", opera.getImageLogo(activeChar));
			html.replace("%LINK_SEND%", ByPass);
			activeChar.sendPacket(html);
		}
		else if (command.equalsIgnoreCase("withdraw"))
		{
			String ByPass = "bypass -h voice .withdrawDO $txtAdena";
			// deposit.html
			final NpcHtmlMessage html = new NpcHtmlMessage();
			html.setFile(activeChar, activeChar.getHtmlPrefix(), "config/zeus/htm/" + language.getInstance().getFolder(activeChar) + "/withdraw.html");
			html.replace("%SERVER_LOGO%", opera.getImageLogo(activeChar));
			html.replace("%LINK_SEND%", ByPass);
			activeChar.sendPacket(html);
		}
		else if (command.equals("depositDO"))
		{
			if (target == null)
			{
				activeChar.sendMessage("Please enter all Requested Data.");
				return true;
			}
			
			if (target.length() == 0)
			{
				activeChar.sendMessage("Please enter all Requested Data.");
				return true;
			}
			
			int Cantidad = Integer.valueOf(target);
			
			if (Cantidad <= 0)
			{
				activeChar.sendMessage("Please enter all Requested Data and Number.");
				return true;
			}
			
			long L_Cantidad_Crear = Cantidad;
			long L_Cantidad_Adena_Crear = Config.BANKING_SYSTEM_ADENA;
			long L_Reducir = L_Cantidad_Crear * L_Cantidad_Adena_Crear;
			
			if (activeChar.getInventory().getInventoryItemCount(57, 0) >= L_Reducir)
			{
				InventoryUpdate iu = new InventoryUpdate();
				activeChar.getInventory().reduceAdena("BanbingAdenaDestroy", L_Reducir, activeChar, null);
				activeChar.getInventory().addItem("BanbingGoldbarCreate", 3470, Config.BANKING_SYSTEM_GOLDBARS * Cantidad, activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendPacket(iu);
				activeChar.sendMessage("Thank you, you now have " + (Config.BANKING_SYSTEM_GOLDBARS * Cantidad) + " Goldbar(s), and " + L_Reducir + " less adena.");
			}
			else
			{
				activeChar.sendMessage("You do not have enough Adena to convert to Goldbar(s), you need " + L_Reducir + " Adena.");
			}
		}
		else if (command.equalsIgnoreCase("withdrawDO"))
		{
			if (target == null)
			{
				activeChar.sendMessage("Please enter all Requested Data.");
				return true;
			}
			
			if (target.length() == 0)
			{
				activeChar.sendMessage("Please enter all Requested Data.");
				return true;
			}
			
			int Cantidad = Integer.valueOf(target);
			
			if (Cantidad <= 0)
			{
				activeChar.sendMessage("Please enter all Requested Data and Number.");
				return true;
			}
			
			long L_Cantidad_Crear = Cantidad;
			long L_Cantidad_Adena_Crear = Config.BANKING_SYSTEM_ADENA;
			long L_Otorgar = L_Cantidad_Crear * L_Cantidad_Adena_Crear;
			
			if (activeChar.getInventory().getInventoryItemCount(3470, 0) >= (Config.BANKING_SYSTEM_GOLDBARS * Cantidad))
			{
				InventoryUpdate iu = new InventoryUpdate();
				activeChar.getInventory().destroyItemByItemId("BankingGoldbarDestroy", 3470, Config.BANKING_SYSTEM_GOLDBARS * Cantidad, activeChar, null);
				activeChar.getInventory().addAdena("BankingAdenaCreate", L_Otorgar, activeChar, null);
				activeChar.getInventory().updateDatabase();
				activeChar.sendPacket(iu);
				activeChar.sendMessage("Thank you, you now have " + L_Otorgar + " Adena, and " + (Config.BANKING_SYSTEM_GOLDBARS * Cantidad) + " less Goldbar(s).");
			}
			else
			{
				activeChar.sendMessage("You do not have any Goldbars to turn into " + L_Otorgar + " Adena.");
			}
		}
		return true;
	}
	
	@Override
	public String[] getVoicedCommandList()
	{
		return _voicedCommands;
	}
}