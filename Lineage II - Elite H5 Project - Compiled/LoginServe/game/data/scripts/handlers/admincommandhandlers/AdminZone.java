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
package handlers.admincommandhandlers;

import java.util.StringTokenizer;

import l2r.gameserver.cache.HtmCache;
import l2r.gameserver.enums.TeleportWhereType;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.handler.IAdminCommandHandler;
import l2r.gameserver.instancemanager.MapRegionManager;
import l2r.gameserver.instancemanager.ZoneManager;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.L2WorldRegion;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.zone.L2ZoneType;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import l2r.util.StringUtil;

/**
 * Small typo fix by Zoey76 24/02/2011
 */
public class AdminZone implements IAdminCommandHandler
{
	private static final String[] ADMIN_COMMANDS =
	{
		"admin_zone_check",
		"admin_zone_reload",
		"admin_zone_visual",
		"admin_zone_visual_clear"
	};
	
	@Override
	public boolean useAdminCommand(String command, L2PcInstance activeChar)
	{
		if (activeChar == null)
		{
			return false;
		}
		
		StringTokenizer st = new StringTokenizer(command, " ");
		String actualCommand = st.nextToken(); // Get actual command
		
		// String val = "";
		// if (st.countTokens() >= 1) {val = st.nextToken();}
		
		if (actualCommand.equalsIgnoreCase("admin_zone_check"))
		{
			showHtml(activeChar);
			activeChar.sendMessage("MapRegion: x:" + MapRegionManager.getInstance().getMapRegionX(activeChar.getX()) + " y:" + MapRegionManager.getInstance().getMapRegionY(activeChar.getY()) + " (" + MapRegionManager.getInstance().getMapRegionLocId(activeChar) + ")");
			getGeoRegionXY(activeChar);
			activeChar.sendMessage("Closest Town: " + MapRegionManager.getInstance().getClosestTownName(activeChar));
			
			Location loc;
			
			loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, TeleportWhereType.CASTLE);
			activeChar.sendMessage("TeleToLocation (Castle): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, TeleportWhereType.CLANHALL);
			activeChar.sendMessage("TeleToLocation (ClanHall): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, TeleportWhereType.SIEGEFLAG);
			activeChar.sendMessage("TeleToLocation (SiegeFlag): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
			
			loc = MapRegionManager.getInstance().getTeleToLocation(activeChar, TeleportWhereType.TOWN);
			activeChar.sendMessage("TeleToLocation (Town): x:" + loc.getX() + " y:" + loc.getY() + " z:" + loc.getZ());
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_reload"))
		{
			ZoneManager.getInstance().reload();
			activeChar.sendMessage("All Zones have been reloaded");
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_visual"))
		{
			String next = st.nextToken();
			if (next.equalsIgnoreCase("all"))
			{
				for (L2ZoneType zone : ZoneManager.getInstance().getZones(activeChar))
				{
					zone.visualizeZone(activeChar.getZ());
				}
				showHtml(activeChar);
			}
			else
			{
				int zoneId = Integer.parseInt(next);
				ZoneManager.getInstance().getZoneById(zoneId).visualizeZone(activeChar.getZ());
			}
		}
		else if (actualCommand.equalsIgnoreCase("admin_zone_visual_clear"))
		{
			ZoneManager.getInstance().clearDebugItems();
			showHtml(activeChar);
		}
		return true;
	}
	
	private static void showHtml(L2PcInstance activeChar)
	{
		final String htmContent = HtmCache.getInstance().getHtm(activeChar, activeChar.getHtmlPrefix(), "data/html/admin/zone.htm");
		NpcHtmlMessage adminReply = new NpcHtmlMessage();
		adminReply.setHtml(htmContent);
		adminReply.replace("%PEACE%", (activeChar.isInsideZone(ZoneIdType.PEACE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%PVP%", (activeChar.isInsideZone(ZoneIdType.PVP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%SIEGE%", (activeChar.isInsideZone(ZoneIdType.SIEGE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%TOWN%", (activeChar.isInsideZone(ZoneIdType.TOWN) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%CASTLE%", (activeChar.isInsideZone(ZoneIdType.CASTLE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%FORT%", (activeChar.isInsideZone(ZoneIdType.FORT) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%HQ%", (activeChar.isInsideZone(ZoneIdType.HQ) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%CLANHALL%", (activeChar.isInsideZone(ZoneIdType.CLAN_HALL) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%LAND%", (activeChar.isInsideZone(ZoneIdType.LANDING) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%NOLAND%", (activeChar.isInsideZone(ZoneIdType.NO_LANDING) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%NOSUMMON%", (activeChar.isInsideZone(ZoneIdType.NO_SUMMON_FRIEND) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%WATER%", (activeChar.isInsideZone(ZoneIdType.WATER) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%SWAMP%", (activeChar.isInsideZone(ZoneIdType.SWAMP) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%DANGER%", (activeChar.isInsideZone(ZoneIdType.DANGER_AREA) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%NOSTORE%", (activeChar.isInsideZone(ZoneIdType.NO_STORE) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%SCRIPT%", (activeChar.isInsideZone(ZoneIdType.SCRIPT) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		adminReply.replace("%BUFFSTORE%", (activeChar.isInsideZone(ZoneIdType.PEACEBUFF) ? "<font color=\"LEVEL\">YES</font>" : "NO"));
		
		StringBuilder zones = new StringBuilder(100);
		L2WorldRegion region = L2World.getInstance().getRegion(activeChar.getX(), activeChar.getY());
		for (L2ZoneType zone : region.getZones())
		{
			if (zone.isCharacterInZone(activeChar))
			{
				if (zone.getName() != null)
				{
					StringUtil.append(zones, zone.getName() + "<br1>");
					if (zone.getId() < 300000)
					{
						StringUtil.append(zones, "(", String.valueOf(zone.getId()), ")");
					}
				}
				else
				{
					StringUtil.append(zones, String.valueOf(zone.getId()));
				}
				StringUtil.append(zones, " ");
			}
		}
		adminReply.replace("%ZLIST%", zones.toString());
		activeChar.sendPacket(adminReply);
	}
	
	private static void getGeoRegionXY(L2PcInstance activeChar)
	{
		int worldX = activeChar.getX();
		int worldY = activeChar.getY();
		int geoX = ((((worldX - (-327680)) >> 4) >> 11) + 10);
		int geoY = ((((worldY - (-262144)) >> 4) >> 11) + 10);
		activeChar.sendMessage("GeoRegion: " + geoX + "_" + geoY + "");
	}
	
	@Override
	public String[] getAdminCommandList()
	{
		return ADMIN_COMMANDS;
	}
}
