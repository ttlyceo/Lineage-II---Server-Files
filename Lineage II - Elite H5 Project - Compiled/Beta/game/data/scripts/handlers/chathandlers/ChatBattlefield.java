/*
 * Copyright (C) 2004-2013 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.chathandlers;

import l2r.Config;
import l2r.gameserver.handler.IChatHandler;
import l2r.gameserver.instancemanager.TerritoryWarManager;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.CreatureSay;
import l2r.gameserver.util.Util;

import ZeuS.ZeuS.ZeuS;

/**
 * A chat handler
 * @author Gigiikun
 */
public class ChatBattlefield implements IChatHandler
{
	private static final int[] COMMAND_IDS =
	{
		20
	};
	
	/**
	 * Handle chat type 'battlefield'
	 */
	@Override
	public void handleChat(int type, L2PcInstance activeChar, String target, String text)
	{
		if (!ZeuS.isActivePIN(activeChar))
		{
			return;
		}
		if (activeChar.isInOlympiadMode() && !activeChar.isGM() && Config.ENABLE_OLY_ANTIFEED)
		{
			return;
		}
		
		if (TerritoryWarManager.getInstance().isTWChannelOpen() && (activeChar.getSiegeSide() > 0))
		{
			if (activeChar.isChatBanned() && Util.contains(Config.BAN_CHAT_CHANNELS, type))
			{
				activeChar.sendPacket(SystemMessageId.CHATTING_IS_CURRENTLY_PROHIBITED);
				return;
			}
			if (activeChar.isGM())
			{
				CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getVar("namePrefix", "") + activeChar.getName(), text);
				for (L2PcInstance player : L2World.getInstance().getPlayers())
				{
					if (player.getSiegeSide() == activeChar.getSiegeSide())
					{
						player.sendPacket(cs);
					}
				}
				return;
			}
			
			CreatureSay cs = new CreatureSay(activeChar.getObjectId(), type, activeChar.getName(), text);
			for (L2PcInstance player : L2World.getInstance().getPlayers())
			{
				if (player.getSiegeSide() == activeChar.getSiegeSide())
				{
					player.sendPacket(cs);
				}
			}
		}
	}
	
	/**
	 * Returns the chat types registered to this handler.
	 */
	@Override
	public int[] getChatTypeList()
	{
		return COMMAND_IDS;
	}
}
