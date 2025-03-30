package l2r.gameserver.network.serverpackets;

import l2r.gameserver.model.entity.Message;
import l2r.gameserver.model.itemcontainer.ItemContainer;
import l2r.gameserver.model.items.instance.L2ItemInstance;

public class ExReplyReceivedPost extends AbstractItemPacket
{
	private final Message _msg;
	private L2ItemInstance[] _items = null;
	
	public ExReplyReceivedPost(Message msg)
	{
		_msg = msg;
		if (msg.hasAttachments())
		{
			final ItemContainer attachments = msg.getAttachments();
			if ((attachments != null) && (attachments.getSize() > 0))
			{
				_items = attachments.getItems();
			}
			else
			{
				_log.warn("Message " + msg.getId() + " has attachments but itemcontainer is empty.");
			}
		}
	}
	
	private String getName()
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
		writeH(0xAB);
		
		writeD(_msg.getId());
		writeD(_msg.isLocked() ? 1 : 0);
		writeD(0x00); // Unknown
		writeS(getName());
		writeS(_msg.getSubject());
		writeS(_msg.getContent());
		
		if ((_items != null) && (_items.length > 0))
		{
			writeD(_items.length);
			for (L2ItemInstance item : _items)
			{
				writeItem(item);
				writeD(item.getObjectId());
			}
		}
		else
		{
			writeD(0x00);
		}
		
		writeQ(_msg.getReqAdena());
		writeD(_msg.hasAttachments() ? 1 : 0);
		writeD(_msg.getSendBySystem());
	}
}
