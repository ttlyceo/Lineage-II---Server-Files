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
package ai.npc.MonumentOfHeroes;

import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.util.Util;

import ai.npc.AbstractNpcAI;

/**
 * Monument of Heroes AI.
 * @author Adry_85
 */
public class MonumentOfHeroes extends AbstractNpcAI
{
	// NPCs
	private static final int[] MONUMENTS =
	{
		31690,
		31769,
		31770,
		31771,
		31772
	};
	// Items
	private static final int WINGS_OF_DESTINY_CIRCLET = 6842;
	private static final int CLOAK_OF_THE_GODS = 61723;
	private static final int[] WEAPONS =
	{
		42640, // Infinity Blade
		42641, // Infinity Cleaver
		42642, // Infinity Axe
		42648, // Infinity Rod
		42653, // Infinity Crusher
		42649, // Infinity Scepter
		42639, // Infinity Stinger
		42651, // Infinity Dual Stingers
		42643, // Infinity Fang
		42645, // Infinity Bow
		42650, // Infinity Wing
		42644, // Infinity Spear
		42647, // Infinity Rapier
		42652, // Infinity Sword
		42646, // Infinity Shooter
	};
	
	
	public MonumentOfHeroes()
	{
		super(MonumentOfHeroes.class.getSimpleName(), "ai/npc");
		addStartNpc(MONUMENTS);
		addTalkId(MONUMENTS);
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		switch (event)
		{
			case "HeroWeapon":
			{
				if (player.isHero())
				{
					return hasAtLeastOneQuestItem(player, WEAPONS) ? "already_have_weapon.htm" : "weapon_list.htm";
				}
				return "no_hero_weapon.htm";
			}
			case "HeroCirclet":
			{
				if (player.isHero())
				{
					if (!hasQuestItems(player, WINGS_OF_DESTINY_CIRCLET))
					{
						giveItems(player, WINGS_OF_DESTINY_CIRCLET, 1);
					}
					else
					{
						return "already_have_circlet.htm";
					}
				}
				else
				{
					return "no_hero_circlet.htm";
				}
				break;
			}
			default:
			{
				int weaponId = Integer.parseInt(event);
				if (Util.contains(WEAPONS, weaponId))
				{
					giveItems(player, weaponId, 1);
				}
				break;
			}
			case "HeroCloak":
			{
				if (player.isHero())
				{
					if (!hasQuestItems(player, CLOAK_OF_THE_GODS))
					{
						giveItems(player, CLOAK_OF_THE_GODS, 1);
					}
					else
					{
						return "already_have_cloak.htm";
					}
				}
				else
				{
					return "no_hero_cloak.htm";
				}
				break;
			}
		}
		return super.onAdvEvent(event, npc, player);
	}
}
