/*
 * Copyright (C) 2004-2015 L2J DataPack
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
package events.MasterOfEnchanting;

import java.util.Date;

import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.event.LongTimeEvent;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.SystemMessage;

/**
 * Master of Enchanting event AI.
 * @author Gnacik
 */
public final class MasterOfEnchanting extends LongTimeEvent
{
	// NPC
	private static final int MASTER_YOGI = 32599;
	// Items
	private static final int MASTER_YOGI_STAFF = 13539;
	private static final int MASTER_YOGI_SCROLL = 13540;
	// Misc
	private static final int STAFF_PRICE = 1000000;
	private static final int SCROLL_24_PRICE = 5000000;
	private static final int SCROLL_24_TIME = 6;
	private static final int SCROLL_1_PRICE = 500000;
	private static final int SCROLL_10_PRICE = 5000000;
	
	//private static final int[] HAT_SHADOW_REWARD =
	//{
	//	13074,
	//	13075,
	//	13076
	//};
	//private static final int[] HAT_EVENT_REWARD =
	//{
	//	13518,
	//	13519,
	//	13522
	//};
	//
	//private static final int[] CRYSTAL_REWARD =
	//{
	//	9570,
	//	9571,
	//	9572
	//};
	
	@SuppressWarnings("deprecation")
	private static final Date EVENT_START = new Date(2011, 7, 1);
	
	public MasterOfEnchanting()
	{
		super(MasterOfEnchanting.class.getSimpleName(), "events");
		addStartNpc(MASTER_YOGI);
		addFirstTalkId(MASTER_YOGI);
		addTalkId(MASTER_YOGI);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		String htmltext = event;
		if (event.equalsIgnoreCase("buy_staff"))
		{
			if (!hasQuestItems(player, MASTER_YOGI_STAFF) && (getQuestItemsCount(player, Inventory.ADENA_ID) > STAFF_PRICE))
			{
				takeItems(player, Inventory.ADENA_ID, STAFF_PRICE);
				giveItems(player, MASTER_YOGI_STAFF, 1);
				htmltext = "32599-staffbuyed.htm";
			}
			else
			{
				htmltext = "32599-staffcant.htm";
			}
		}
		else if (event.equalsIgnoreCase("buy_scroll_24"))
		{
			long curTime = System.currentTimeMillis();
			String value = loadGlobalQuestVar(player.getAccountName());
			long reuse = value == "" ? 0 : Long.parseLong(value);
			if (player.getCreateDate().after(EVENT_START))
			{
				return "32599-bidth.htm";
			}
			
			if (curTime > reuse)
			{
				if (getQuestItemsCount(player, Inventory.ADENA_ID) > SCROLL_24_PRICE)
				{
					takeItems(player, Inventory.ADENA_ID, SCROLL_24_PRICE);
					giveItems(player, MASTER_YOGI_SCROLL, 24);
					saveGlobalQuestVar(player.getAccountName(), Long.toString(System.currentTimeMillis() + (SCROLL_24_TIME * 3600000)));
					htmltext = "32599-scroll24.htm";
				}
				else
				{
					htmltext = "32599-s24-no.htm";
				}
			}
			else
			{
				long remainingTime = (reuse - curTime) / 1000;
				int hours = (int) remainingTime / 3600;
				int minutes = ((int) remainingTime % 3600) / 60;
				if (hours > 0)
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ITEM_PURCHASABLE_IN_S1_HOURS_S2_MINUTES);
					sm.addInt(hours);
					sm.addInt(minutes);
					player.sendPacket(sm);
					htmltext = "32599-scroll24.htm";
				}
				else if (minutes > 0)
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.ITEM_PURCHASABLE_IN_S1_MINUTES);
					sm.addInt(minutes);
					player.sendPacket(sm);
					htmltext = "32599-scroll24.htm";
				}
				else
				{
					// Little glitch. There is no SystemMessage with seconds only.
					// If time is less than 1 minute player can buy scrolls
					if (getQuestItemsCount(player, Inventory.ADENA_ID) > SCROLL_24_PRICE)
					{
						takeItems(player, Inventory.ADENA_ID, SCROLL_24_PRICE);
						giveItems(player, MASTER_YOGI_SCROLL, 24);
						saveGlobalQuestVar(player.getAccountName(), Long.toString(System.currentTimeMillis() + (SCROLL_24_TIME * 3600000)));
						htmltext = "32599-scroll24.htm";
					}
					else
					{
						htmltext = "32599-s24-no.htm";
					}
				}
			}
		}
		else if (event.equalsIgnoreCase("buy_scroll_1"))
		{
			if (getQuestItemsCount(player, Inventory.ADENA_ID) > SCROLL_1_PRICE)
			{
				takeItems(player, Inventory.ADENA_ID, SCROLL_1_PRICE);
				giveItems(player, MASTER_YOGI_SCROLL, 1);
				htmltext = "32599-scroll-ok.htm";
			}
			else
			{
				htmltext = "32599-s1-no.htm";
			}
		}
		else if (event.equalsIgnoreCase("buy_scroll_10"))
		{
			if (getQuestItemsCount(player, Inventory.ADENA_ID) > SCROLL_10_PRICE)
			{
				takeItems(player, Inventory.ADENA_ID, SCROLL_10_PRICE);
				giveItems(player, MASTER_YOGI_SCROLL, 10);
				htmltext = "32599-scroll-ok.htm";
			}
			else
			{
				htmltext = "32599-s10-no.htm";
			}
		}
		else if (event.equalsIgnoreCase("receive_reward"))
		{
			if ((getItemEquipped(player, Inventory.PAPERDOLL_RHAND) == MASTER_YOGI_STAFF) && (getEnchantLevel(player, MASTER_YOGI_STAFF) > 3))
			{
				switch (getEnchantLevel(player, MASTER_YOGI_STAFF))
				{
					case 4:
						giveItems(player, 6406, 1); // Firework
						break;
					case 5:
						giveItems(player, 6406, 3); // Firework
						giveItems(player, 6407, 1); // Large Firework
						break;
					case 6:
						giveItems(player, 13518, 1); // Event Top Hat
						break;
					case 7:
						giveItems(player, 47038, 1); // Talisman of Conqueror lv 1
						break;
					case 8:
						giveItems(player, 47032, 1); // Talisman of Authority lv 1
						break;
					case 9:
						giveItems(player, 47026, 1); // Talisman of Protection lv 1
						break;
					case 10:
						giveItems(player, 47020, 1); // Talisman of Insanity lv 1
						break;
					case 11:
						giveItems(player, 17071, 1); // Dynasty Weapon Box
						break;
					case 12:
						giveItems(player, 17070, 1); // Icarus Weapon Box
						break;
					case 13:
						giveItems(player, 51211, 1); // Lindvior Aghation
						break;
					case 14:
						giveItems(player, 53026, 1); // Lindvior Maunt
						break;
					case 15:
						giveItems(player, 17072, 1); // Vesper Armor Box
						break;
					case 16:
						giveItems(player, 17069, 1); // Vesper Weapon Box
						break;

					default:
						if (getEnchantLevel(player, MASTER_YOGI_STAFF) > 23)
						{
							giveItems(player, 13988, 1); // S80 Grade Weapon Chest (Event)
						}
						break;
				}
				takeItems(player, MASTER_YOGI_STAFF, 1);
				htmltext = "32599-rewardok.htm";
			}
			else
			{
				htmltext = "32599-rewardnostaff.htm";
			}
		}
		return htmltext;
	}
	
	@Override
	public String onFirstTalk(L2Npc npc, L2PcInstance player)
	{
		return npc.getId() + ".htm";
	}
}
