/*
 * Copyright (C) 2004-2014 L2J Server
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
package l2r.gameserver.model.zone.type;

import java.util.Vector;

import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.ZeuS.ZeuS;

/**
 * @author zairus
 */
public class L2RaidBossZone extends L2RespawnZone
{
	public L2RaidBossZone(int id)
	{
		super(id);
	}
	
	private final Vector<L2PcInstance> playerinSide = new Vector<L2PcInstance>();
	
	protected String getGeoRegion(int x, int y)
	{
		int worldX = x;// activeChar.getX();
		int worldY = y;// activeChar.getY();
		int geoX = ((((worldX - (-327680)) >> 4) >> 11) + 10);
		int geoY = ((((worldY - (-262144)) >> 4) >> 11) + 10);
		return geoX + "_" + geoY;
	}
	
	protected boolean isDualBox(L2PcInstance player)
	{
		boolean Retorno = false;
		if (playerinSide.size() > 0)
		{
			for (L2PcInstance cha : playerinSide)
			{
				Retorno = ZeuS.isDualBox_pc(player, cha);
			}
		}
		return Retorno;
	}
	
	protected void addPlayerInside(L2PcInstance player)
	{
		if (!playerinSide.contains(player))
		{
			playerinSide.add(player);
		}
	}
	
	protected void RemovePlayerInside(L2PcInstance player)
	{
		if (!playerinSide.contains(player))
		{
			playerinSide.remove(player);
		}
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		/*
		 * if (character instanceof L2PcInstance) { character.setInsideZone(ZoneId.NO_SUMMON_FRIEND, true); character.setInsideZone(ZoneId.NO_STORE, true); character.setInsideZone ZoneId.NO_BOOKMARK, true); L2PcInstance activeChar = (L2PcInstance) character; if (isDualBox(activeChar) &&
		 * !activeChar.isGM()) { activeChar.teleToLocation(-248372, 250444, 4344, 200); activeChar.sendMessage("You already have a char in there"); RemovePlayerInside(activeChar); return; } if !activeChar.isGM()) { addPlayerInside(activeChar); } }
		 */
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		
		try
		{
			if (character instanceof L2PcInstance)
			{
				// ((L2PcInstance) character).teleToLocation(115210, 74454, -2612, 80);
				ZeuS.RemovePlayerInZone((L2PcInstance) character);
			}
		}
		catch (Exception a)
		{
		}
		
	}
	
	@Override
	public void onDieInside(final L2Character character)
	{
		
	}
	
	@Override
	public void onReviveInside(L2Character character)
	{
		if (character instanceof L2PcInstance)
		{
			heal((L2PcInstance) character);
		}
	}
	
	static void heal(L2PcInstance activeChar)
	{
		activeChar.setCurrentHp(activeChar.getMaxHp());
		activeChar.setCurrentCp(activeChar.getMaxCp());
		activeChar.setCurrentMp(activeChar.getMaxMp());
	}
}
