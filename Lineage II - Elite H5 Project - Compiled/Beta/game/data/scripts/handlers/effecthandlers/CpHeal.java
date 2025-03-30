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
package handlers.effecthandlers;

import ZeuS.interfase.potionSystem;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.EffectTemplate;
import l2r.gameserver.model.effects.L2Effect;
import l2r.gameserver.model.effects.L2EffectType;
import l2r.gameserver.model.stats.Env;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.SystemMessage;

/**
 * @author UnAfraid
 */
public class CpHeal extends L2Effect
{
	public CpHeal(Env env, EffectTemplate template)
	{
		super(env, template);
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.CPHEAL;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public boolean onStart()
	{
		L2Character target = getEffected();
		if ((target == null) || target.isDead() || target.isDoor())
		{
			return false;
		}
		
		// vGodFather: herb effect must override invul check
		if ((target.isInvul() || target.isHpBlocked()) && !getSkill().isHerb())
		{
			return false;
		}
		
		double amount = getValue();
		
		// Prevents overheal and negative amount
		amount = Math.max(Math.min(amount, target.getMaxRecoverableCp() - target.getCurrentCp()), 0);
		int t_amount = 0;
		int oly_t_amount = 0;
		if(target instanceof L2PcInstance){
			if(potionSystem.isEnabled()){
				t_amount = potionSystem.getCP(((L2PcInstance)target).getClassId().getId(), getSkill().getId(), ((L2PcInstance)target));
				oly_t_amount = potionSystem.getOlyCP(((L2PcInstance)target).getClassId().getId(), getSkill().getId());
				if( (t_amount > 0) && !((L2PcInstance)target).isInOlympiad()){
					amount = t_amount;
				}else if(oly_t_amount >0 && ((L2PcInstance)target).isInOlympiad()){
					amount = oly_t_amount;
				}
			}
		}		
		if (amount != 0)
		{
			target.setCurrentCp(amount + target.getCurrentCp());
		}
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_CP_WILL_BE_RESTORED);
		sm.addInt((int) amount);
		target.sendPacket(sm);
		return true;
	}
}
