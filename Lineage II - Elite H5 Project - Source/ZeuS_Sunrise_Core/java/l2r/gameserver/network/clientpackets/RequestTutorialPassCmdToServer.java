/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2r.gameserver.network.clientpackets;

import gabriel.phantomEngine.PhantomControl;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;

public class RequestTutorialPassCmdToServer extends L2GameClientPacket
{
	private static final String _C__86_REQUESTTUTORIALPASSCMDTOSERVER = "[C] 86 RequestTutorialPassCmdToServer";
	
	private String _bypass = null;
	
	@Override
	protected void readImpl()
	{
		_bypass = readS();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance player = getClient().getActiveChar();
		
		if (player == null)
		{
			return;
		}
		
		final QuestState qs = player.getQuestState(Quest.TUTORIAL);
		if (qs != null)
		{
			qs.getQuest().notifyEvent(_bypass, null, player);
		}
        PhantomControl.getInstance().onBypass(player, _bypass);

    }
	
	@Override
	public String getType()
	{
		return _C__86_REQUESTTUTORIALPASSCMDTOSERVER;
	}
}
