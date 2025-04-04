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
 * this program.
 */
package ai.npc.BloodAltars;

import java.util.ArrayList;
import java.util.List;

import l2r.Config;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.util.Rnd;

import ai.npc.AbstractNpcAI;

/**
 * Author: vGodFather
 */
public class AdenBloodAltar extends AbstractNpcAI
{
	private static final long delay = Config.CHANGE_STATUS * 60 * 1000;
	protected static boolean bossesSpawned = false;
	
	private final List<L2Npc> deadnpcs = new ArrayList<>();
	private final List<L2Npc> alivenpcs = new ArrayList<>();
	private final List<L2Npc> bosses = new ArrayList<>();
	
	protected boolean progress1 = false;
	protected boolean progress2 = false;
	protected boolean progress3 = false;
	
	protected static final int[][] bossGroups =
	{
		// {
			// 25793,
			// 152936,
			// 25016,
			// -2154,
			// 28318
		// },
		// {
			// 25794,
			// 153144,
			// 24664,
			// -2139,
			// 32767
		// },
		// {
			// 25797,
			// 152392,
			// 24712,
			// -2158,
			// 24575
		// }
	};
	
	protected static final int[][] BLOODALTARS_DEAD_NPC =
	{
		{
			4327,
			152728,
			24920,
			-2120,
			16383
		},
		{
			4328,
			152856,
			24840,
			-2128,
			8191
		},
		{
			4328,
			152584,
			24840,
			-2120,
			24575
		}
	};
	
	protected static final int[][] BLOODALTARS_ALIVE_NPC =
	{
		{
			4324,
			152728,
			24920,
			-2120,
			16383
		},
		{
			4325,
			152856,
			24840,
			-2128,
			8191
		},
		{
			4325,
			152584,
			24840,
			-2120,
			24575
		}
	};
	
	public AdenBloodAltar()
	{
		super(AdenBloodAltar.class.getSimpleName(), "ai/npc");
		
		manageNpcs(true);
		
		// addKillId(25793);
		// addKillId(25794);
		// addKillId(25797);
		
		ThreadPoolManager.getInstance().scheduleGeneral(() -> changestatus(), delay);
	}
	
	protected void manageNpcs(boolean spawnAlive)
	{
		if (spawnAlive)
		{
			for (int[] spawn : BLOODALTARS_ALIVE_NPC)
			{
				L2Npc npc = addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0, false);
				if (npc != null)
				{
					alivenpcs.add(npc);
				}
			}
			
			if (!deadnpcs.isEmpty())
			{
				for (L2Npc npc : deadnpcs)
				{
					if (npc != null)
					{
						npc.deleteMe();
					}
				}
			}
			deadnpcs.clear();
		}
		else
		{
			for (int[] spawn : BLOODALTARS_DEAD_NPC)
			{
				L2Npc npc = addSpawn(spawn[0], spawn[1], spawn[2], spawn[3], spawn[4], false, 0, false);
				if (npc != null)
				{
					deadnpcs.add(npc);
				}
			}
			
			if (!alivenpcs.isEmpty())
			{
				for (L2Npc npc : alivenpcs)
				{
					if (npc != null)
					{
						npc.deleteMe();
					}
				}
			}
			alivenpcs.clear();
		}
	}
	
	protected void manageBosses(boolean spawn)
	{
		if (spawn)
		{
			for (int[] bossspawn : bossGroups)
			{
				L2Npc boss = addSpawn(bossspawn[0], bossspawn[1], bossspawn[2], bossspawn[3], bossspawn[4], false, 0, false);
				if (boss != null)
				{
					bosses.add(boss);
				}
			}
		}
		else
		{
			if (!bosses.isEmpty())
			{
				for (L2Npc boss : bosses)
				{
					if (boss != null)
					{
						boss.deleteMe();
					}
				}
			}
		}
	}
	
	protected void changestatus()
	{
		ThreadPoolManager.getInstance().scheduleGeneral(() ->
		{
			if (Rnd.chance(Config.CHANCE_SPAWN))
			{
				if (!bossesSpawned)
				{
					manageNpcs(false);
					manageBosses(true);
					bossesSpawned = true;
				}
				else
				{
					manageBosses(false);
					manageNpcs(true);
					bossesSpawned = false;
					ThreadPoolManager.getInstance().scheduleGeneral(() -> changestatus(), Config.RESPAWN_TIME * 60 * 1000);
				}
			}
		} , 10000);
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance player, boolean isSummon)
	{
		// final int npcId = npc.getId();
		
		// if (npcId == 25793)
		// {
			// progress1 = true;
		// }
		
		// if (npcId == 25794)
		// {
			// progress2 = true;
		// }
		
		// if (npcId == 25797)
		// {
			// progress3 = true;
		// }
		
		if (progress1 && progress2 && progress3)
		{
			ThreadPoolManager.getInstance().scheduleGeneral(() ->
			{
				progress1 = false;
				progress2 = false;
				progress3 = false;
				
				manageBosses(false);
				manageNpcs(true);
				ThreadPoolManager.getInstance().scheduleGeneral(() -> changestatus(), Config.RESPAWN_TIME * 60 * 1000);
			} , 30000);
		}
		return super.onKill(npc, player, isSummon);
	}
}