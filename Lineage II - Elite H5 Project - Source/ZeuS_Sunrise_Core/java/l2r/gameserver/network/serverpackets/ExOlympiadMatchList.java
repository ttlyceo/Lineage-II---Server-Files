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
package l2r.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import l2r.gameserver.model.entity.olympiad.AbstractOlympiadGame;
import l2r.gameserver.model.entity.olympiad.OlympiadGameClassed;
import l2r.gameserver.model.entity.olympiad.OlympiadGameManager;
import l2r.gameserver.model.entity.olympiad.OlympiadGameNonClassed;
import l2r.gameserver.model.entity.olympiad.OlympiadGameTeams;
import l2r.gameserver.model.entity.olympiad.tasks.OlympiadGameTask;

import ZeuS.ZeuS.ZeuS;

/**
 * @author mrTJO
 */
public class ExOlympiadMatchList extends L2GameServerPacket
{
	private final List<OlympiadGameTask> _games = new ArrayList<>();
	
	public ExOlympiadMatchList()
	{
		OlympiadGameTask task;
		for (int i = 0; i < OlympiadGameManager.getInstance().getNumberOfStadiums(); i++)
		{
			task = OlympiadGameManager.getInstance().getOlympiadTask(i);
			if (task != null)
			{
				if (!task.isGameStarted() || task.isBattleFinished())
				{
					continue; // initial or finished state not shown
				}
				_games.add(task);
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0xD4);
		writeD(0x00); // Type 0 = Match List, 1 = Match Result
		
		writeD(_games.size());
		writeD(0x00);
		
		for (OlympiadGameTask curGame : _games)
		{
			AbstractOlympiadGame game = curGame.getGame();
			if (game != null)
			{
				writeD(game.getStadiumId()); // Stadium Id (Arena 1 = 0)
				
				if (game instanceof OlympiadGameNonClassed)
				{
					writeD(1);
				}
				else if (game instanceof OlympiadGameClassed)
				{
					writeD(2);
				}
				else if (game instanceof OlympiadGameTeams)
				{
					writeD(-1);
				}
				else
				{
					writeD(0);
				}
				
				writeD(curGame.getState()); // (1 = Standby, 2 = Playing)
				writeS(ZeuS.showNameNpcOly() ? game.getPlayerNames()[0] : "unknown"); // Player 1 Name
				writeS(ZeuS.showNameNpcOly() ? game.getPlayerNames()[1] : "unknown"); // Player 2 Name
			}
		}
	}
}
