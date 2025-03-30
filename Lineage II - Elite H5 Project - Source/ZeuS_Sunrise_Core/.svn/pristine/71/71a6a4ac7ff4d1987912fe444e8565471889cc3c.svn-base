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

import java.util.List;

import l2r.gameserver.instancemanager.MailManager;
import l2r.gameserver.model.entity.Message;

/**
 * @author Migi, DS
 */
public class ExShowReceivedPostList extends L2GameServerPacket
{
	private final List<Message> _inbox;
	
	public ExShowReceivedPostList(int objectId)
	{
		_inbox = MailManager.getInstance().getInbox(objectId);
	}
	
	private String getName(Message _msg)
	{
		switch (String.valueOf(_msg.getSenderId()))
		{
			case "-30000001":
				return "*Annoucement*";
			case "-30000002":
				return "*Donation*";
			case "-30000003":
				return "*Event*";
			case "-30000004":
				return "*Gift*";
			case "-30000005":
				return "*Special*";
			case "-30000006":
				return "*Reward*";
			case "-30000007":
				return "*Critical*";
			case "-30000008":
				return "*Account*";
			case "-30000009":
				return "*Password*";
			case "-30000010":
				return "*Vote*";
			case "-30000011":
				return "*Gathering*";
			case "-30000012":
				return "*Promotions*";
			case "-30000013":
				return "*Other*";
			case "-30000014":
				return "*Survey*";
			case "-30000015":
				return "*Auction House*";
			case "-30000016":
				return "*Bid House*";
			case "-30000017":
				return "*New Bug Report*";
			case "-30000018":
				return "*Bug Report*";
			case "-30000019":
				return "*Town War Event*";
			case "-30000020":
				return "*Wish List*";
		}
		return _msg.getSenderName();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0xAA);
		writeD((int) (System.currentTimeMillis() / 1000));
		if ((_inbox != null) && (_inbox.size() > 0))
		{
			writeD(_inbox.size());
			for (Message msg : _inbox)
			{
				writeD(msg.getId());
				writeS(msg.getSubject());
				writeS(getName(msg));
				writeD(msg.isLocked() ? 0x01 : 0x00);
				writeD(msg.getExpirationSeconds());
				writeD(msg.isUnread() ? 0x01 : 0x00);
				writeD(0x01);
				writeD(msg.hasAttachments() ? 0x01 : 0x00);
				writeD(msg.isReturned() ? 0x01 : 0x00);
				writeD(msg.getSendBySystem());
				writeD(0x00);
			}
		}
		else
		{
			writeD(0x00);
		}
	}
}
