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
package l2r.gameserver.model.actor.stat;

import java.util.concurrent.atomic.AtomicInteger;

import l2r.Config;
import l2r.gameserver.data.xml.impl.ExperienceData;
import l2r.gameserver.data.xml.impl.PetData;
import l2r.gameserver.enums.PcCondOverride;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.L2PetLevelData;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2ClassMasterInstance;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.instance.L2PetInstance;
import l2r.gameserver.model.actor.transform.TransformTemplate;
import l2r.gameserver.model.events.EventDispatcher;
import l2r.gameserver.model.events.impl.character.player.OnPlayerLevelChanged;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.model.stats.Formulas;
import l2r.gameserver.model.stats.MoveType;
import l2r.gameserver.model.stats.Stats;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ExVitalityPointInfo;
import l2r.gameserver.network.serverpackets.ExVoteSystemInfo;
import l2r.gameserver.network.serverpackets.PledgeShowMemberListUpdate;
import l2r.gameserver.network.serverpackets.SocialAction;
import l2r.gameserver.network.serverpackets.StatusUpdate;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.gameserver.util.Util;

import gr.sr.configsEngine.configs.impl.FormulasConfigs;

import ZeuS.ZeuS.ZeuS;

public class PcStat extends PlayableStat
{
	private int _oldMaxHp; // stats watch
	private int _oldMaxMp; // stats watch
	private int _oldMaxCp; // stats watch
	private float _vitalityPoints = 1;
	private byte _vitalityLevel = 0;
	private long _startingXp;
	/** Player's maximum cubic count. */
	private int _maxCubicCount = 1;
	/** Player's maximum talisman count. */
	private final AtomicInteger _talismanSlots = new AtomicInteger();
	private boolean _cloakSlot = false;
	
	public static final int VITALITY_LEVELS[] =
	{
		240,
		2000,
		13000,
		17000,
		20000
	};
	public static final int MAX_VITALITY_POINTS = VITALITY_LEVELS[4];
	public static final int MIN_VITALITY_POINTS = 1;
	
	public PcStat(L2PcInstance activeChar)
	{
		super(activeChar);
	}
	
	// vGodFather
	public void decreaseKarma(long value)
	{
		// Set new karma
		final L2PcInstance activeChar = getActiveChar();
		if (!activeChar.isCursedWeaponEquipped() && (activeChar.getKarma() > 0) && (activeChar.isGM() || !activeChar.isInsideZone(ZoneIdType.PVP)))
		{
			int karmaLost = Formulas.calculateKarmaLost(activeChar, value);
			if (karmaLost > 0)
			{
				activeChar.setKarma(activeChar.getKarma() - karmaLost);
				final SystemMessage msg = SystemMessage.getSystemMessage(SystemMessageId.YOUR_KARMA_HAS_BEEN_CHANGED_TO_S1);
				msg.addInt(activeChar.getKarma());
				activeChar.sendPacket(msg);
				
				// vGodFather Fix
				if (activeChar.getKarma() <= 0)
				{
					activeChar.startFlag();
				}
			}
		}
	}
	
	@Override
	public boolean addExp(long value)
	{
		return addExp(value, false);
	}
	
	public boolean addExp(long exp, boolean isRessurect)
	{
		L2PcInstance activeChar = getActiveChar();
		
		// Allowed to gain exp?
		if (!getActiveChar().getAccessLevel().canGainExp())
		{
			return false;
		}
		
		// Exp from resurrect don't remove karma
		if (!super.addExp(exp))
		{
			return false;
		}
		
		if (!isRessurect)
		{
			decreaseKarma(exp);
		}
		
		activeChar.sendUserInfo(true);
		return true;
	}
	
	@Override
	public boolean removeExp(long exp)
	{
		if (!super.removeExp(exp))
		{
			return false;
		}
		decreaseKarma(exp);
		return true;
	}
	
	/**
	 * Add Experience and SP rewards to the L2PcInstance, remove its Karma (if necessary) and Launch increase level task.<br>
	 * <B><U>Actions </U>:</B>
	 * <ul>
	 * <li>Remove Karma when the player kills L2MonsterInstance</li>
	 * <li>Send a Server->Client packet StatusUpdate to the L2PcInstance</li>
	 * <li>Send a Server->Client System Message to the L2PcInstance</li>
	 * <li>If the L2PcInstance increases it's level, send a Server->Client packet SocialAction (broadcast)</li>
	 * <li>If the L2PcInstance increases it's level, manage the increase level task (Max MP, Max MP, Recommendation, Expertise and beginner skills...)</li>
	 * <li>If the L2PcInstance increases it's level, send a Server->Client packet UserInfo to the L2PcInstance</li>
	 * </ul>
	 * @param addToExp The Experience value to add
	 * @param addToSp The SP value to add
	 */
	@Override
	public boolean addExpAndSp(long addToExp, int addToSp)
	{
		return addExpAndSp(addToExp, addToSp, false);
	}
	
	public boolean addExpAndSp(long addToExp, int addToSp, boolean useBonuses)
	{
		L2PcInstance activeChar = getActiveChar();
		
		// Allowed to gain exp/sp?
		if (!activeChar.getAccessLevel().canGainExp())
		{
			return false;
		}
		
		long baseExp = addToExp;
		int baseSp = addToSp;
		
		final double bonusExp = useBonuses ? getExpBonusMultiplier() : 1.;
		final double bonusSp = useBonuses ? getSpBonusMultiplier() : 1.;
		
		double ZeuSPremiumEXP = 1.;
		double ZeuSPremiumSP = 1.;
		
		if (!activeChar.isInsideZone(ZoneIdType.PEACE))
		{
			activeChar.setRecomTimerActive(true);
		}
		
		addToExp *= bonusExp;
		addToSp *= bonusSp;
		
		if (ZeuS.isPremium(activeChar) && !ZeuS.isForJustRate(activeChar))
		{
			ZeuSPremiumEXP = ZeuS.getPremiumExp_Add(activeChar, addToExp);
			ZeuSPremiumSP = ZeuS.getPremiumSp_Add(activeChar, addToSp);
			addToExp += ZeuSPremiumEXP;
			addToSp += ZeuSPremiumSP;
		}
		
		float ratioTakenByPlayer = 0;
		
		// if this player has a pet and it is in his range he takes from the owner's Exp, give the pet Exp now
		if (activeChar.hasPet() && Util.checkIfInShortRadius(Config.ALT_PARTY_RANGE, activeChar, activeChar.getSummon(), false))
		{
			L2PetInstance pet = (L2PetInstance) activeChar.getSummon();
			ratioTakenByPlayer = pet.getPetLevelData().getOwnerExpTaken() / 100f;
			
			// only give exp/sp to the pet by taking from the owner if the pet has a non-zero, positive ratio
			// allow possible customizations that would have the pet earning more than 100% of the owner's exp/sp
			if (ratioTakenByPlayer > 1)
			{
				ratioTakenByPlayer = 1;
			}
			
			if (!pet.isDead())
			{
				pet.addExpAndSp((long) (addToExp * (1 - ratioTakenByPlayer)), (int) (addToSp * (1 - ratioTakenByPlayer)));
			}
			
			// now adjust the max ratio to avoid the owner earning negative exp/sp
			addToExp = (long) (addToExp * ratioTakenByPlayer);
			addToSp = (int) (addToSp * ratioTakenByPlayer);
		}
		
		if (!super.addExpAndSp(addToExp, addToSp))
		{
			return false;
		}
		
		SystemMessage sm = null;
		if ((addToExp == 0) && (addToSp != 0))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.ACQUIRED_S1_SP);
			sm.addInt(addToSp);
		}
		else if ((addToSp == 0) && (addToExp != 0))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.EARNED_S1_EXPERIENCE);
			sm.addLong(addToExp);
		}
		else
		{
			if ((addToExp - baseExp) > 0)
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_EARNED_S1_EXP_BONUS_S2_AND_S3_SP_BONUS_S4);
				sm.addLong(addToExp);
				sm.addLong(addToExp - baseExp);
				sm.addInt(addToSp);
				sm.addInt(addToSp - baseSp);
			}
			else
			{
				sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_EARNED_S1_EXP_AND_S2_SP);
				sm.addLong((int) addToExp);
				sm.addInt(addToSp);
			}
		}
		activeChar.sendPacket(sm);
		return true;
	}
	
	@Override
	public boolean removeExpAndSp(long addToExp, int addToSp)
	{
		return removeExpAndSp(addToExp, addToSp, true);
	}
	
	public boolean removeExpAndSp(long addToExp, int addToSp, boolean sendMessage)
	{
		int level = getLevel();
		if (!super.removeExpAndSp(addToExp, addToSp))
		{
			return false;
		}
		
		if (sendMessage)
		{
			// Send a Server->Client System Message to the L2PcInstance
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.EXP_DECREASED_BY_S1);
			sm.addLong(addToExp);
			getActiveChar().sendPacket(sm);
			sm = SystemMessage.getSystemMessage(SystemMessageId.SP_DECREASED_S1);
			sm.addInt(addToSp);
			getActiveChar().sendPacket(sm);
			if (getLevel() < level)
			{
				getActiveChar().broadcastStatusUpdate();
			}
		}
		return true;
	}
	
	@Override
	public final boolean addLevel(byte value)
	{
		if ((getLevel() + value) > (ExperienceData.getInstance().getMaxLevel() - 1))
		{
			return false;
		}
		
		// Notify to scripts
		EventDispatcher.getInstance().notifyEventAsync(new OnPlayerLevelChanged(getActiveChar(), getLevel(), getLevel() + value), getActiveChar());
		
		boolean levelIncreased = super.addLevel(value);
		if (levelIncreased)
		{
			if (!Config.DISABLE_TUTORIAL)
			{
				final QuestState qs = getActiveChar().getQuestState(Quest.TUTORIAL);
				if (qs != null)
				{
					qs.getQuest().notifyEvent("CE40", null, getActiveChar());
				}
			}
			
			getActiveChar().setCurrentCp(getMaxCp());
			getActiveChar().broadcastPacket(new SocialAction(getActiveChar().getObjectId(), SocialAction.LEVEL_UP));
			getActiveChar().sendPacket(SystemMessageId.YOU_INCREASED_YOUR_LEVEL);
			
			L2ClassMasterInstance.showQuestionMark(getActiveChar());
		}
		
		// Give AutoGet skills and all normal skills if Auto-Learn is activated.
		getActiveChar().rewardSkills();
		
		if (getActiveChar().getClan() != null)
		{
			getActiveChar().getClan().updateClanMember(getActiveChar());
			getActiveChar().getClan().broadcastToOnlineMembers(new PledgeShowMemberListUpdate(getActiveChar()));
		}
		if (getActiveChar().isInParty())
		{
			getActiveChar().getParty().recalculatePartyLevel(); // Recalculate the party level
		}
		
		if (getActiveChar().isTransformed() || getActiveChar().isInStance())
		{
			getActiveChar().getTransformation().onLevelUp(getActiveChar());
		}
		
		// Synchronize level with pet if possible.
		if (getActiveChar().hasPet())
		{
			final L2PetInstance pet = (L2PetInstance) getActiveChar().getSummon();
			if (pet.getPetData().isSynchLevel() && (pet.getLevel() != getLevel()))
			{
				pet.getStat().setLevel(getLevel());
				pet.getStat().getExpForLevel(getActiveChar().getLevel());
				pet.setCurrentHp(pet.getMaxHp());
				pet.setCurrentMp(pet.getMaxMp());
				pet.broadcastPacket(new SocialAction(getActiveChar().getObjectId(), SocialAction.LEVEL_UP));
				pet.updateAndBroadcastStatus(1);
			}
		}
		
		// Update the overloaded status of the L2PcInstance
		getActiveChar().refreshOverloaded(false);
		// Update the expertise status of the L2PcInstance
		getActiveChar().refreshExpertisePenalty();
		// Send a Server->Client packet UserInfo to the L2PcInstance
		getActiveChar().broadcastUserInfo();
		getActiveChar().sendPacket(new ExVoteSystemInfo(getActiveChar()));
		// Nevit Points For Level
		getActiveChar().getNevitSystem().addPoints(1950);
		
		return levelIncreased;
	}
	
	@Override
	public boolean addSp(int value)
	{
		if (!super.addSp(value))
		{
			return false;
		}
		
		StatusUpdate su = getActiveChar().makeStatusUpdate(StatusUpdate.SP);
		getActiveChar().sendPacket(su);
		
		return true;
	}
	
	@Override
	public final long getExpForLevel(int level)
	{
		return ExperienceData.getInstance().getExpForLevel(level);
	}
	
	@Override
	public final L2PcInstance getActiveChar()
	{
		return (L2PcInstance) super.getActiveChar();
	}
	
	@Override
	public final long getExp()
	{
		if (getActiveChar().isSubClassActive())
		{
			return getActiveChar().getSubClasses().get(getActiveChar().getClassIndex()).getExp();
		}
		
		return super.getExp();
	}
	
	public final long getBaseExp()
	{
		return super.getExp();
	}
	
	@Override
	public final void setExp(long value)
	{
		if (getActiveChar().isSubClassActive())
		{
			getActiveChar().getSubClasses().get(getActiveChar().getClassIndex()).setExp(value);
		}
		else
		{
			super.setExp(value);
		}
	}
	
	public void setStartingExp(long value)
	{
		if (Config.BOTREPORT_ENABLE)
		{
			_startingXp = value;
		}
	}
	
	public long getStartingExp()
	{
		return _startingXp;
	}
	
	/**
	 * Gets the maximum cubic count.
	 * @return the maximum cubic count
	 */
	public int getMaxCubicCount()
	{
		return _maxCubicCount;
	}
	
	/**
	 * Sets the maximum cubic count.
	 * @param cubicCount the maximum cubic count
	 */
	public void setMaxCubicCount(int cubicCount)
	{
		_maxCubicCount = cubicCount;
	}
	
	/**
	 * Gets the maximum talisman count.
	 * @return the maximum talisman count
	 */
	public int getTalismanSlots()
	{
		return _talismanSlots.get();
	}
	
	public void addTalismanSlots(int count)
	{
		_talismanSlots.addAndGet(count);
	}
	
	public boolean canEquipCloak()
	{
		return _cloakSlot;
	}
	
	public void setCloakSlotStatus(boolean cloakSlot)
	{
		_cloakSlot = cloakSlot;
	}
	
	@Override
	public final byte getLevel()
	{
		if (getActiveChar().isSubClassActive())
		{
			return getActiveChar().getSubClasses().get(getActiveChar().getClassIndex()).getLevel();
		}
		
		return super.getLevel();
	}
	
	public final byte getBaseLevel()
	{
		return super.getLevel();
	}
	
	@Override
	public final void setLevel(byte value)
	{
		if (value > (ExperienceData.getInstance().getMaxLevel() - 1))
		{
			value = (byte) (ExperienceData.getInstance().getMaxLevel() - 1);
		}
		
		if (getActiveChar().isSubClassActive())
		{
			getActiveChar().getSubClasses().get(getActiveChar().getClassIndex()).setLevel(value);
		}
		else
		{
			super.setLevel(value);
		}
	}
	
	@Override
	public final int getMaxCp()
	{
		// Get the Max CP (base+modifier) of the L2PcInstance
		int val = (getActiveChar() == null) ? 1 : (int) calcStat(Stats.MAX_CP, getActiveChar().getTemplate().getBaseCpMax(getActiveChar().getLevel()));
		if (getActiveChar() instanceof L2PcInstance)
		{
			val += ZeuS.getPremiumCPBonus(getActiveChar());
		}
		if (val != _oldMaxCp)
		{
			_oldMaxCp = val;
			
			// Launch a regen task if the new Max CP is higher than the old one
			if (getActiveChar().getStatus().getCurrentCp() != val)
			{
				getActiveChar().getStatus().setCurrentCp(getActiveChar().getStatus().getCurrentCp()); // trigger start of regeneration
			}
		}
		return val;
	}
	
	@Override
	public final int getMaxHp()
	{
		// Get the Max HP (base+modifier) of the L2PcInstance
		int val = (getActiveChar() == null) ? 1 : (int) calcStat(Stats.MAX_HP, getActiveChar().getTemplate().getBaseHpMax(getActiveChar().getLevel()));
		if (getActiveChar() instanceof L2PcInstance)
		{
			val += ZeuS.getPremiumHPBonus(getActiveChar());
		}
		if (val != _oldMaxHp)
		{
			_oldMaxHp = val;
			
			// Launch a regen task if the new Max HP is higher than the old one
			if (getActiveChar().getStatus().getCurrentHp() != val)
			{
				getActiveChar().getStatus().setCurrentHp(getActiveChar().getStatus().getCurrentHp()); // trigger start of regeneration
			}
		}
		
		return val;
	}
	
	@Override
	public final int getMaxMp()
	{
		// Get the Max MP (base+modifier) of the L2PcInstance
		int val = (getActiveChar() == null) ? 1 : (int) calcStat(Stats.MAX_MP, getActiveChar().getTemplate().getBaseMpMax(getActiveChar().getLevel()));
		if (getActiveChar() instanceof L2PcInstance)
		{
			val += ZeuS.getPremiumMPBonus(getActiveChar());
		}
		if (val != _oldMaxMp)
		{
			_oldMaxMp = val;
			
			// Launch a regen task if the new Max MP is higher than the old one
			if (getActiveChar().getStatus().getCurrentMp() != val)
			{
				getActiveChar().getStatus().setCurrentMp(getActiveChar().getStatus().getCurrentMp()); // trigger start of regeneration
			}
		}
		
		return val;
	}
	
	@Override
	public final int getSp()
	{
		if (getActiveChar().isSubClassActive())
		{
			return getActiveChar().getSubClasses().get(getActiveChar().getClassIndex()).getSp();
		}
		
		return super.getSp();
	}
	
	public final int getBaseSp()
	{
		return super.getSp();
	}
	
	@Override
	public final void setSp(int value)
	{
		if (getActiveChar().isSubClassActive())
		{
			getActiveChar().getSubClasses().get(getActiveChar().getClassIndex()).setSp(value);
		}
		else
		{
			super.setSp(value);
		}
	}
	
	/**
	 * @param type movement type
	 * @return the base move speed of given movement type.
	 */
	@Override
	public double getBaseMoveSpeed(MoveType type)
	{
		final L2PcInstance player = getActiveChar();
		if (player.isTransformed())
		{
			final TransformTemplate template = player.getTransformation().getTemplate(player);
			if (template != null)
			{
				return template.getBaseMoveSpeed(type);
			}
		}
		else if (player.isMounted())
		{
			final L2PetLevelData data = PetData.getInstance().getPetLevelData(player.getMountNpcId(), player.getMountLevel());
			if (data != null)
			{
				return data.getSpeedOnRide(type);
			}
		}
		return super.getBaseMoveSpeed(type);
	}
	
	@Override
	public double getRunSpeed()
	{
		double val = super.getRunSpeed() + Config.RUN_SPD_BOOST;
		
		// Apply max run speed cap.
		if ((val > FormulasConfigs.MAX_RUN_SPEED) && !getActiveChar().canOverrideCond(PcCondOverride.MAX_STATS_VALUE))
		{
			return FormulasConfigs.MAX_RUN_SPEED;
		}
		
		// Check for mount penalties
		if (getActiveChar().isMounted())
		{
			// if level diff with mount >= 10, it decreases move speed by 50%
			if ((getActiveChar().getMountLevel() - getActiveChar().getLevel()) >= 10)
			{
				val /= 2;
			}
			// if mount is hungry, it decreases move speed by 50%
			if (getActiveChar().isHungry())
			{
				val /= 2;
			}
		}
		
		return val;
	}
	
	@Override
	public double getWalkSpeed()
	{
		double val = super.getWalkSpeed() + Config.RUN_SPD_BOOST;
		
		// Apply max run speed cap.
		if ((val > FormulasConfigs.MAX_RUN_SPEED) && !getActiveChar().canOverrideCond(PcCondOverride.MAX_STATS_VALUE))
		{
			return FormulasConfigs.MAX_RUN_SPEED;
		}
		
		if (getActiveChar().isMounted())
		{
			// if level diff with mount >= 10, it decreases move speed by 50%
			if ((getActiveChar().getMountLevel() - getActiveChar().getLevel()) >= 10)
			{
				val /= 2;
			}
			// if mount is hungry, it decreases move speed by 50%
			if (getActiveChar().isHungry())
			{
				val /= 2;
			}
		}
		
		return val;
	}
	
	@Override
	public double getPAtkSpd()
	{
		double val = super.getPAtkSpd();
		
		if ((val > FormulasConfigs.MAX_PATK_SPEED) && !getActiveChar().canOverrideCond(PcCondOverride.MAX_STATS_VALUE))
		{
			return FormulasConfigs.MAX_PATK_SPEED;
		}
		
		return val;
	}
	
	@Override
	public int getEvasionRate(L2Character target)
	{
		int val = super.getEvasionRate(target);
		
		if ((val > FormulasConfigs.MAX_EVASION) && !getActiveChar().canOverrideCond(PcCondOverride.MAX_STATS_VALUE))
		{
			return FormulasConfigs.MAX_EVASION;
		}
		
		return val;
	}
	
	@Override
	public int getMAtkSpd()
	{
		int val = super.getMAtkSpd();
		
		if ((val > FormulasConfigs.MAX_MATK_SPEED) && !getActiveChar().canOverrideCond(PcCondOverride.MAX_STATS_VALUE))
		{
			return FormulasConfigs.MAX_MATK_SPEED;
		}
		
		return val;
	}
	
	private void updateVitalityLevel(boolean quiet)
	{
		final byte level;
		
		if (_vitalityPoints <= VITALITY_LEVELS[0])
		{
			level = 0;
		}
		else if (_vitalityPoints <= VITALITY_LEVELS[1])
		{
			level = 1;
		}
		else if (_vitalityPoints <= VITALITY_LEVELS[2])
		{
			level = 2;
		}
		else if (_vitalityPoints <= VITALITY_LEVELS[3])
		{
			level = 3;
		}
		else
		{
			level = 4;
		}
		
		if (!quiet && (level != _vitalityLevel))
		{
			if (level < _vitalityLevel)
			{
				getActiveChar().sendPacket(SystemMessageId.VITALITY_HAS_DECREASED);
			}
			else
			{
				getActiveChar().sendPacket(SystemMessageId.VITALITY_HAS_INCREASED);
			}
			if (level == 0)
			{
				getActiveChar().sendPacket(SystemMessageId.VITALITY_IS_EXHAUSTED);
			}
			else if (level == 4)
			{
				getActiveChar().sendPacket(SystemMessageId.VITALITY_IS_AT_MAXIMUM);
			}
		}
		
		_vitalityLevel = level;
	}
	
	/*
	 * Return current vitality points in integer format
	 */
	public int getVitalityPoints()
	{
		return (int) _vitalityPoints;
	}
	
	/*
	 * Set current vitality points to this value if quiet = true - does not send system messages
	 */
	public void setVitalityPoints(int points, boolean quiet)
	{
		points = Math.min(Math.max(points, MIN_VITALITY_POINTS), MAX_VITALITY_POINTS);
		if (points == _vitalityPoints)
		{
			return;
		}
		
		_vitalityPoints = points;
		updateVitalityLevel(quiet);
		getActiveChar().sendPacket(new ExVitalityPointInfo(getVitalityPoints()));
	}
	
	public synchronized void updateVitalityPoints(float points, boolean useRates, boolean quiet)
	{
		if ((points == 0) || !Config.ENABLE_VITALITY || ((Config.FREE_VITALITY_TILL_LEVEL > 0) && getActiveChar().isMainClass() && (getActiveChar().getLevel() < Config.FREE_VITALITY_TILL_LEVEL)))
		{
			return;
		}
		
		if (useRates)
		{
			if (getActiveChar().isLucky())
			{
				return;
			}
			
			if (points < 0) // vitality consumed
			{
				int stat = (int) calcStat(Stats.VITALITY_CONSUME_RATE, 1, getActiveChar(), null);
				
				if (getActiveChar().getNevitSystem().isAdventBlessingActive())
				{
					stat = -10; // increase Vitality During Blessing
				}
				
				if (stat == 0)
				{
					return;
				}
				if (stat < 0)
				{
					points = -points;
				}
			}
			
			if (points > 0)
			{
				// vitality increased
				points *= Config.RATE_VITALITY_GAIN;
			}
			else
			{
				// vitality decreased
				points *= Config.RATE_VITALITY_LOST;
			}
		}
		
		if (points > 0)
		{
			points = Math.min(_vitalityPoints + points, MAX_VITALITY_POINTS);
		}
		else
		{
			points = Math.max(_vitalityPoints + points, MIN_VITALITY_POINTS);
		}
		
		if (points == _vitalityPoints)
		{
			return;
		}
		
		_vitalityPoints = points;
		updateVitalityLevel(quiet);
	}
	
	public double getVitalityMultiplier()
	{
		double vitality = 1.0;
		
		if (Config.ENABLE_VITALITY)
		{
			switch (getVitalityLevel())
			{
				case 1:
					vitality = Config.RATE_VITALITY_LEVEL_1;
					break;
				case 2:
					vitality = Config.RATE_VITALITY_LEVEL_2;
					break;
				case 3:
					vitality = Config.RATE_VITALITY_LEVEL_3;
					break;
				case 4:
					vitality = Config.RATE_VITALITY_LEVEL_4;
					break;
			}
		}
		
		return vitality;
	}
	
	/**
	 * @return the _vitalityLevel
	 */
	public byte getVitalityLevel()
	{
		if (getActiveChar().getNevitSystem().isAdventBlessingActive())
		{
			return 4;
		}
		
		if ((Config.FREE_VITALITY_TILL_LEVEL > 0) && getActiveChar().isMainClass() && (getActiveChar().getLevel() < Config.FREE_VITALITY_TILL_LEVEL))
		{
			return 4;
		}
		
		return _vitalityLevel;
	}
	
	public double getExpBonusMultiplier()
	{
		// Bonus from Vitality System
		final double vitality = getVitalityMultiplier();
		
		// Bonus from recommends
		final double reco = getActiveChar().getRecomBonusMul();
		
		// Bonus from Nevit's hunting already handled
		final double nevits = 0.0;
		
		// Bonus exp from skills
		final double bonusExp = calcStat(Stats.BONUS_EXP, 0) / 100;
		
		double bonus = 0.0;
		bonus += Math.max(vitality, 0);
		bonus += Math.max(reco, 0);
		bonus += Math.max(nevits, 0);
		bonus += Math.max(bonusExp, 0);
		
		// Check for abnormal bonuses
		bonus = Math.max(bonus, 1);
		bonus = Math.min(bonus, Config.MAX_BONUS_EXP);
		
		return bonus;
	}
	
	public double getSpBonusMultiplier()
	{
		// Bonus from Vitality System
		final double vitality = getVitalityMultiplier();
		
		// Bonus from recommends
		final double reco = getActiveChar().getRecomBonusMul();
		
		// Bonus from Nevit's hunting already handled
		final double nevits = 0.0;
		
		// Bonus exp from skills
		final double bonusExp = calcStat(Stats.BONUS_SP, 0) / 100;
		
		double bonus = 0.0;
		bonus += Math.max(vitality, 0);
		bonus += Math.max(reco, 0);
		bonus += Math.max(nevits, 0);
		bonus += Math.max(bonusExp, 0);
		
		// Check for abnormal bonuses
		bonus = Math.max(bonus, 1);
		bonus = Math.min(bonus, Config.MAX_BONUS_SP);
		
		return bonus;
	}
}
