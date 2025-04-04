/*
 * Copyright (C) L2J Sunrise
 * This file is part of L2J Sunrise.
 */
package handlers.effecthandlers;

import java.util.List;

import l2r.gameserver.model.effects.EffectTemplate;
import l2r.gameserver.model.effects.L2Effect;
import l2r.gameserver.model.effects.L2EffectType;
import l2r.gameserver.model.stats.Env;
import l2r.gameserver.model.stats.Formulas;
import l2r.gameserver.model.actor.instance.L2PcInstance;

import ZeuS.ZeuS.cancelbuff;
/**
 * Dispel By Category effect implementation.
 * @author vGodFather
 */
public final class DispelByCategory extends L2Effect
{
	private final String _slot;
	private final int _rate;
	private final int _max;
	private final boolean _randomEffects;
	
	public DispelByCategory(Env env, EffectTemplate template)
	{
		super(env, template);
		
		_slot = template.getParameters().getString("slot", null);
		_rate = template.getParameters().getInt("rate", 0);
		_max = template.getParameters().getInt("max", 0);
		_randomEffects = template.getParameters().getInt("randomEffects", 0) == 1 ? true : false;
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.DISPEL;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public boolean onStart()
	{
		if (getEffected().isDead())
		{
			return false;
		}
		boolean isL2PcInstance = false;
		if (getEffected() instanceof L2PcInstance)
		{
			isL2PcInstance = true;
		}
		final List<L2Effect> canceled = Formulas.getCanceledEffects(getEffector(), getEffected(), getSkill(), _slot, _rate, _max, _randomEffects);
		for (L2Effect can : canceled)
		{
			can.exit();
			if (isL2PcInstance)
			{
				cancelbuff.getInstance().setCancel((L2PcInstance) getEffected(), can);
			}
		}
		return true;
	}
}