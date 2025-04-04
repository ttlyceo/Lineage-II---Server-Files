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
package l2r.gameserver.model.entity.olympiad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import gabriel.GabrielBoards.ClassPathsDAO;
import gabriel.config.ClassPathConfig;
import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.data.SpawnTable;
import l2r.gameserver.enums.audio.Sound;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.AbnormalEffect;
import l2r.gameserver.model.entity.olympiad.enums.CompetitionType;
import l2r.gameserver.model.events.EventDispatcher;
import l2r.gameserver.model.events.impl.olympiad.OnOlympiadMatchResult;
import l2r.gameserver.model.zone.type.L2OlympiadStadiumZone;
import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.ExOlympiadMatchResult;
import l2r.gameserver.network.serverpackets.ExOlympiadUserInfo;
import l2r.gameserver.network.serverpackets.L2GameServerPacket;
import l2r.gameserver.network.serverpackets.NpcSay;
import l2r.gameserver.network.serverpackets.SystemMessage;
import l2r.util.Rnd;

import gr.sr.utils.Tools;

import ZeuS.ZeuS.ZeuS;

/**
 * @author GodKratos, Pere, DS
 */
public abstract class OlympiadGameNormal extends AbstractOlympiadGame
{
	protected int _damageP1 = 0;
	protected int _damageP2 = 0;
	
	protected Participant _playerOne;
	protected Participant _playerTwo;
	
	protected OlympiadGameNormal(int id, Participant[] opponents)
	{
		super(id);
		
		_playerOne = opponents[0];
		_playerTwo = opponents[1];
		
		_playerOne.getPlayer().setOlympiadGameId(id);
		_playerTwo.getPlayer().setOlympiadGameId(id);
	}
	
	protected static final Participant[] createListOfParticipants(List<Integer> list)
	{
		if ((list == null) || list.isEmpty() || (list.size() < 2))
		{
			return null;
		}
		
		int playerOneObjectId = 0;
		L2PcInstance playerOne = null;
		L2PcInstance playerTwo = null;
		
		while (list.size() > 1)
		{
			playerOneObjectId = list.remove(Rnd.nextInt(list.size()));
			playerOne = L2World.getInstance().getPlayer(playerOneObjectId);
			if ((playerOne == null) || !playerOne.isOnline())
			{
				continue;
			}
			
			playerTwo = L2World.getInstance().getPlayer(list.remove(Rnd.nextInt(list.size())));
			if ((playerTwo == null) || !playerTwo.isOnline())
			{
				list.add(playerOneObjectId);
				continue;
			}
			
			Participant[] result = new Participant[2];
			result[0] = new Participant(playerOne, 1);
			result[1] = new Participant(playerTwo, 2);
			
			return result;
		}
		return null;
	}
	
	@Override
	public final boolean containsParticipant(int playerId)
	{
		return ((_playerOne != null) && (_playerOne.getObjectId() == playerId)) || ((_playerTwo != null) && (_playerTwo.getObjectId() == playerId));
	}
	
	@Override
	public final void sendOlympiadInfo(L2Character player)
	{
		player.sendPacket(new ExOlympiadUserInfo(_playerOne));
		player.sendPacket(new ExOlympiadUserInfo(_playerTwo));
	}
	
	@Override
	public final void broadcastOlympiadInfo(L2OlympiadStadiumZone stadium)
	{
		stadium.broadcastPacket(new ExOlympiadUserInfo(_playerOne));
		stadium.broadcastPacket(new ExOlympiadUserInfo(_playerTwo));
	}
	
	@Override
	public final void broadcastPacket(L2GameServerPacket packet)
	{
		if (_playerOne.updatePlayer())
		{
			_playerOne.getPlayer().sendPacket(packet);
		}
		
		if (_playerTwo.updatePlayer())
		{
			_playerTwo.getPlayer().sendPacket(packet);
		}
	}
	
	@Override
	public final boolean portPlayersToArena(List<Location> spawns)
	{
		boolean result = true;
		try
		{
			setOlympiadGame(_playerOne, this);
			setOlympiadGame(_playerTwo, this);
			result &= portPlayerToArena(_playerOne, spawns.get(0), _stadiumID);
			result &= portPlayerToArena(_playerTwo, spawns.get(spawns.size() / 2), _stadiumID);
		}
		catch (Exception e)
		{
			_log.warn(String.valueOf(e));
			return false;
		}
		return result;
	}
	
	@Override
	public boolean needBuffers()
	{
		return true;
	}
	
	@Override
	public final void removals()
	{
		if (_aborted)
		{
			return;
		}
		
		removals(_playerOne.getPlayer(), true);
		removals(_playerTwo.getPlayer(), true);
	}
	
	@Override
	public final boolean makeCompetitionStart()
	{
		// Dual Box Check pcIp based in Olys.
		if (Config.ENABLE_OLY_IP_ANTIFEED && Tools.isDualBox(_playerOne.getPlayer(), _playerTwo.getPlayer()))
		{
			_playerOne.getPlayer().sendMessage("Competiton canceled, Dual Box detected!");
			_playerTwo.getPlayer().sendMessage("Competiton canceled, Dual Box detected!");
			return false;
		}
		
		if (!super.makeCompetitionStart())
		{
			return false;
		}
		
		if ((_playerOne.getPlayer() == null) || (_playerTwo.getPlayer() == null))
		{
			return false;
		}
		
		_playerOne.getPlayer().setIsOlympiadStart(true);
		if(ClassPathConfig.CLASSPATH_REMOVE_OLY)
			ClassPathsDAO.getInstance().removeSkills(_playerOne.getPlayer());
		_playerOne.getPlayer().updateEffectIcons();
		_playerTwo.getPlayer().setIsOlympiadStart(true);
		if(ClassPathConfig.CLASSPATH_REMOVE_OLY)
			ClassPathsDAO.getInstance().removeSkills(_playerTwo.getPlayer());
		_playerTwo.getPlayer().updateEffectIcons();
		ZeuS.setOlyTimer(_playerOne.getPlayer(), _playerTwo.getPlayer());
		return true;
	}
	
	@Override
	public final void restorePlayers()
	{
		if ((_playerOne.getPlayer() != null) && !_playerOne.isDefaulted() && !_playerOne.isDisconnected() && (_playerOne.getPlayer().getOlympiadGameId() == _stadiumID))
		{
			restorePlayer(_playerOne.getPlayer());
		}
		
		if ((_playerTwo.getPlayer() != null) && !_playerTwo.isDefaulted() && !_playerTwo.isDisconnected() && (_playerTwo.getPlayer().getOlympiadGameId() == _stadiumID))
		{
			restorePlayer(_playerTwo.getPlayer());
		}
	}
	
	@Override
	public final void cleanEffects()
	{
		if ((_playerOne.getPlayer() != null) && !_playerOne.isDefaulted() && !_playerOne.isDisconnected() && (_playerOne.getPlayer().getOlympiadGameId() == _stadiumID))
		{
			cleanEffects(_playerOne.getPlayer());
		}
		
		if ((_playerTwo.getPlayer() != null) && !_playerTwo.isDefaulted() && !_playerTwo.isDisconnected() && (_playerTwo.getPlayer().getOlympiadGameId() == _stadiumID))
		{
			cleanEffects(_playerTwo.getPlayer());
		}
	}
	
	@Override
	public final void portPlayersBack()
	{
		if ((_playerOne.getPlayer() != null) && !_playerOne.isDefaulted() && !_playerOne.isDisconnected())
		{
			portPlayerBack(_playerOne.getPlayer());
			
			_playerOne.getPlayer().stopAbnormalEffect(AbnormalEffect.HOLD_1);
			_playerOne.getPlayer().setIsParalyzed(false);
			
			if(_playerOne.getPlayer().getSummon() != null) {
				_playerOne.getPlayer().getSummon().stopAbnormalEffect(AbnormalEffect.HOLD_1);
				_playerOne.getPlayer().getSummon().setIsParalyzed(false);
			}
		}
		if ((_playerTwo.getPlayer() != null) && !_playerTwo.isDefaulted() && !_playerTwo.isDisconnected())
		{
			portPlayerBack(_playerTwo.getPlayer());
			
			_playerTwo.getPlayer().stopAbnormalEffect(AbnormalEffect.HOLD_1);
			_playerTwo.getPlayer().setIsParalyzed(false);
			if(_playerTwo.getPlayer().getSummon() != null) {
				_playerTwo.getPlayer().getSummon().stopAbnormalEffect(AbnormalEffect.HOLD_1);
				_playerTwo.getPlayer().getSummon().setIsParalyzed(false);
			}
			
		}
	}
	
	@Override
	public final void playersStatusBack()
	{
		if ((_playerOne.getPlayer() != null) && !_playerOne.isDefaulted() && !_playerOne.isDisconnected() && (_playerOne.getPlayer().getOlympiadGameId() == _stadiumID))
		{
			playerStatusBack(_playerOne.getPlayer());
		}
		
		if ((_playerTwo.getPlayer() != null) && !_playerTwo.isDefaulted() && !_playerTwo.isDisconnected() && (_playerTwo.getPlayer().getOlympiadGameId() == _stadiumID))
		{
			playerStatusBack(_playerTwo.getPlayer());
		}
	}
	
	@Override
	public final void clearPlayers()
	{
		_playerOne.setPlayer(null);
		_playerOne = null;
		_playerTwo.setPlayer(null);
		_playerTwo = null;
	}
	
	@Override
	public void healPlayers()
	{
		final L2PcInstance playerOne = _playerOne.getPlayer();
		if (playerOne != null)
		{
			playerOne.setCurrentCp(playerOne.getMaxCp());
			playerOne.setCurrentHp(playerOne.getMaxHp());
			playerOne.setCurrentMp(playerOne.getMaxMp());
		}
		
		final L2PcInstance playerTwo = _playerTwo.getPlayer();
		if (playerTwo != null)
		{
			playerTwo.setCurrentCp(playerTwo.getMaxCp());
			playerTwo.setCurrentHp(playerTwo.getMaxHp());
			playerTwo.setCurrentMp(playerTwo.getMaxMp());
		}
	}
	
	@Override
	protected final void handleDisconnect(L2PcInstance player)
	{
		if (player.getObjectId() == _playerOne.getObjectId())
		{
			_playerOne.setDisconnected(true);
		}
		else if (player.getObjectId() == _playerTwo.getObjectId())
		{
			_playerTwo.setDisconnected(true);
		}
	}
	
	@Override
	public final boolean checkBattleStatus()
	{
		if (_aborted)
		{
			return false;
		}
		
		if ((_playerOne.getPlayer() == null) || _playerOne.isDisconnected())
		{
			return false;
		}
		
		if ((_playerTwo.getPlayer() == null) || _playerTwo.isDisconnected())
		{
			return false;
		}
		
		return true;
	}
	
	@Override
	public final boolean haveWinner()
	{
		if (!checkBattleStatus())
		{
			return true;
		}
		
		boolean playerOneLost = true;
		try
		{
			if (_playerOne.getPlayer().getOlympiadGameId() == _stadiumID)
			{
				playerOneLost = _playerOne.getPlayer().isDead();
			}
		}
		catch (Exception e)
		{
			playerOneLost = true;
		}
		
		boolean playerTwoLost = true;
		try
		{
			if (_playerTwo.getPlayer().getOlympiadGameId() == _stadiumID)
			{
				playerTwoLost = _playerTwo.getPlayer().isDead();
			}
		}
		catch (Exception e)
		{
			playerTwoLost = true;
		}
		
		return playerOneLost || playerTwoLost;
	}
	
	@Override
	public void validateWinner(L2OlympiadStadiumZone stadium)
	{
		if (_aborted)
		{
			return;
		}
		
		ExOlympiadMatchResult result = null;
		
		boolean tie = false;
		int winside = 0;
		
		List<OlympiadInfo> list1 = new ArrayList<>(1);
		List<OlympiadInfo> list2 = new ArrayList<>(1);
		
		final boolean _pOneCrash = ((_playerOne.getPlayer() == null) || _playerOne.isDisconnected());
		final boolean _pTwoCrash = ((_playerTwo.getPlayer() == null) || _playerTwo.isDisconnected());
		
		final int playerOnePoints = _playerOne.getStats().getInt(POINTS);
		final int playerTwoPoints = _playerTwo.getStats().getInt(POINTS);
		int pointDiff = Math.min(playerOnePoints, playerTwoPoints) / getDivider();
		if (pointDiff <= 0)
		{
			pointDiff = 1;
		}
		else if (pointDiff > Config.ALT_OLY_MAX_POINTS)
		{
			pointDiff = Config.ALT_OLY_MAX_POINTS;
		}
		
		ZeuS.stopOlyTimer(_playerOne.getPlayer());
		ZeuS.stopOlyTimer(_playerTwo.getPlayer());
		
		if(!_pOneCrash) {
			_playerOne.getPlayer().startAbnormalEffect(AbnormalEffect.HOLD_1);
			_playerOne.getPlayer().setIsParalyzed(true);
			if(_playerOne.getPlayer().getSummon() != null) {
				_playerOne.getPlayer().getSummon().startAbnormalEffect(AbnormalEffect.HOLD_1);
				_playerOne.getPlayer().getSummon().setIsParalyzed(true);
			}
			
		}
		if(!_pTwoCrash) {	
			_playerTwo.getPlayer().startAbnormalEffect(AbnormalEffect.HOLD_1);
			_playerTwo.getPlayer().setIsParalyzed(true);
			if(_playerTwo.getPlayer().getSummon() != null) {
				_playerTwo.getPlayer().getSummon().startAbnormalEffect(AbnormalEffect.HOLD_1);
				_playerTwo.getPlayer().getSummon().setIsParalyzed(true);
			}
		}
		int points;
		SystemMessage sm;
		
		// Check for if a player defaulted before battle started
		if (_playerOne.isDefaulted() || _playerTwo.isDefaulted())
		{
			try
			{
				if (_playerOne.isDefaulted())
				{
					try
					{
						points = Math.min(playerOnePoints / 3, Config.ALT_OLY_MAX_POINTS);
						removePointsFromParticipant(_playerOne, points);
						list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints - points, -points));
						
						winside = 2;
						
						if (Config.ALT_OLY_LOG_FIGHTS)
						{
							LogRecord record = new LogRecord(Level.INFO, _playerOne.getName() + " default");
							record.setParameters(new Object[]
							{
								_playerOne.getName(),
								_playerTwo.getName(),
								0,
								0,
								0,
								0,
								points,
								getType().toString()
							});
							_logResults.log(record);
						}
					}
					catch (Exception e)
					{
						_log.warn("Exception on validateWinner(): " + e.getMessage(), e);
					}
				}
				if (_playerTwo.isDefaulted())
				{
					try
					{
						points = Math.min(playerTwoPoints / 3, Config.ALT_OLY_MAX_POINTS);
						removePointsFromParticipant(_playerTwo, points);
						list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints - points, -points));
						
						if (winside == 2)
						{
							tie = true;
						}
						else
						{
							winside = 1;
						}
						
						if (Config.ALT_OLY_LOG_FIGHTS)
						{
							LogRecord record = new LogRecord(Level.INFO, _playerTwo.getName() + " default");
							record.setParameters(new Object[]
							{
								_playerOne.getName(),
								_playerTwo.getName(),
								0,
								0,
								0,
								0,
								points,
								getType().toString()
							});
							_logResults.log(record);
						}
					}
					catch (Exception e)
					{
						_log.warn("Exception on validateWinner(): " + e.getMessage(), e);
					}
				}
				if (winside == 1)
				{
					result = new ExOlympiadMatchResult(tie, winside, list1, list2);
				}
				else
				{
					result = new ExOlympiadMatchResult(tie, winside, list2, list1);
				}
				stadium.broadcastPacket(result, true);
				return;
			}
			catch (Exception e)
			{
				_log.warn("Exception on validateWinner(): " + e.getMessage(), e);
				return;
			}
		}
		
		// Create results for players if a player crashed
		if (_pOneCrash || _pTwoCrash)
		{
			try
			{
				if (_pTwoCrash && !_pOneCrash)
				{
					try
					{
						ZeuS.Announce_Oly_Winner(_playerOne, _playerTwo);
					}
					catch (Exception a)
					{
						
					}
					
					sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
					sm.addString(_playerOne.getName());
					stadium.broadcastPacket(sm);
					
					_playerOne.updateStat(COMP_WON, 1);
					addPointsToParticipant(_playerOne, pointDiff);
					list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints + pointDiff, pointDiff));
					
					_playerTwo.updateStat(COMP_LOST, 1);
					removePointsFromParticipant(_playerTwo, pointDiff);
					list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints - pointDiff, -pointDiff));
					
					winside = 1;
					
					rewardParticipant(_playerOne.getPlayer(), getReward());
					_playerOne.getPlayer().sendPacket(Sound.ITEMSOUND_QUEST_ITEMGET.getPacket());
					
					if (Config.ALT_OLY_LOG_FIGHTS)
					{
						LogRecord record = new LogRecord(Level.INFO, _playerTwo.getName() + " crash");
						record.setParameters(new Object[]
						{
							_playerOne.getName(),
							_playerTwo.getName(),
							0,
							0,
							0,
							0,
							pointDiff,
							getType().toString()
						});
						_logResults.log(record);
					}
					
					// Notify to scripts
					EventDispatcher.getInstance().notifyEventAsync(new OnOlympiadMatchResult(_playerOne, _playerTwo, getType()), Olympiad.getInstance());
				}
				else if (_pOneCrash && !_pTwoCrash)
				{
					try
					{
						ZeuS.Announce_Oly_Winner(_playerTwo, _playerOne);
					}
					catch (Exception a)
					{
						
					}
					
					sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
					sm.addString(_playerTwo.getName());
					stadium.broadcastPacket(sm);
					
					_playerTwo.updateStat(COMP_WON, 1);
					addPointsToParticipant(_playerTwo, pointDiff);
					list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints + pointDiff, pointDiff));
					
					_playerOne.updateStat(COMP_LOST, 1);
					removePointsFromParticipant(_playerOne, pointDiff);
					list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints - pointDiff, -pointDiff));
					
					winside = 2;
					
					rewardParticipant(_playerTwo.getPlayer(), getReward());
					_playerTwo.getPlayer().sendPacket(Sound.ITEMSOUND_QUEST_ITEMGET.getPacket());
					
					if (Config.ALT_OLY_LOG_FIGHTS)
					{
						LogRecord record = new LogRecord(Level.INFO, _playerOne.getName() + " crash");
						record.setParameters(new Object[]
						{
							_playerOne.getName(),
							_playerTwo.getName(),
							0,
							0,
							0,
							0,
							pointDiff,
							getType().toString()
						});
						_logResults.log(record);
					}
					
					// Notify to scripts
					EventDispatcher.getInstance().notifyEventAsync(new OnOlympiadMatchResult(_playerTwo, _playerOne, getType()), Olympiad.getInstance());
				}
				else if (_pOneCrash && _pTwoCrash)
				{
					stadium.broadcastPacket(SystemMessage.getSystemMessage(SystemMessageId.THE_GAME_ENDED_IN_A_TIE));
					
					_playerOne.updateStat(COMP_LOST, 1);
					removePointsFromParticipant(_playerOne, pointDiff);
					list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints - pointDiff, -pointDiff));
					
					_playerTwo.updateStat(COMP_LOST, 1);
					removePointsFromParticipant(_playerTwo, pointDiff);
					list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints - pointDiff, -pointDiff));
					
					tie = true;
					
					if (Config.ALT_OLY_LOG_FIGHTS)
					{
						LogRecord record = new LogRecord(Level.INFO, "both crash");
						record.setParameters(new Object[]
						{
							_playerOne.getName(),
							_playerTwo.getName(),
							0,
							0,
							0,
							0,
							pointDiff,
							getType().toString()
						});
						_logResults.log(record);
					}
				}
				
				_playerOne.updateStat(COMP_DONE, 1);
				_playerTwo.updateStat(COMP_DONE, 1);
				_playerOne.updateStat(COMP_DONE_WEEK, 1);
				_playerTwo.updateStat(COMP_DONE_WEEK, 1);
				_playerOne.updateStat(getWeeklyMatchType(), 1);
				_playerTwo.updateStat(getWeeklyMatchType(), 1);
				
				if (winside == 1)
				{
					result = new ExOlympiadMatchResult(tie, winside, list1, list2);
				}
				else
				{
					result = new ExOlympiadMatchResult(tie, winside, list2, list1);
				}
				stadium.broadcastPacket(result, true);
				
				// Notify to scripts
				EventDispatcher.getInstance().notifyEventAsync(new OnOlympiadMatchResult(null, _playerOne, getType()), Olympiad.getInstance());
				EventDispatcher.getInstance().notifyEventAsync(new OnOlympiadMatchResult(null, _playerTwo, getType()), Olympiad.getInstance());
				return;
			}
			catch (Exception e)
			{
				_log.warn("Exception on validateWinner(): " + e.getMessage(), e);
				return;
			}
		}
		
		try
		{
			String winner = "draw";
			
			// Calculate Fight time
			long _fightTime = (System.currentTimeMillis() - _startTime);
			
			double playerOneHp = 0;
			if ((_playerOne.getPlayer() != null) && !_playerOne.getPlayer().isDead())
			{
				playerOneHp = _playerOne.getPlayer().getCurrentHp() + _playerOne.getPlayer().getCurrentCp();
				if (playerOneHp < 0.5)
				{
					playerOneHp = 0;
				}
			}
			
			double playerTwoHp = 0;
			if ((_playerTwo.getPlayer() != null) && !_playerTwo.getPlayer().isDead())
			{
				playerTwoHp = _playerTwo.getPlayer().getCurrentHp() + _playerTwo.getPlayer().getCurrentCp();
				if (playerTwoHp < 0.5)
				{
					playerTwoHp = 0;
				}
			}
			
			// if players crashed, search if they've relogged
			_playerOne.updatePlayer();
			_playerTwo.updatePlayer();
			
			if (((_playerOne.getPlayer() == null) || !_playerOne.getPlayer().isOnline()) && ((_playerTwo.getPlayer() == null) || !_playerTwo.getPlayer().isOnline()))
			{
				_playerOne.updateStat(COMP_DRAWN, 1);
				_playerTwo.updateStat(COMP_DRAWN, 1);
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_GAME_ENDED_IN_A_TIE);
				stadium.broadcastPacket(sm);
			}
			else if ((_playerTwo.getPlayer() == null) || !_playerTwo.getPlayer().isOnline() || ((playerTwoHp == 0) && (playerOneHp != 0)) || ((_damageP1 > _damageP2) && (playerTwoHp != 0) && (playerOneHp != 0)))
			{
				try
				{
					ZeuS.Announce_Oly_Winner(_playerOne, _playerTwo);
				}
				catch (Exception a)
				{
					
				}
				sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
				sm.addString(_playerOne.getName());
				stadium.broadcastPacket(sm);
				
				_playerOne.updateStat(COMP_WON, 1);
				_playerTwo.updateStat(COMP_LOST, 1);
				
				addPointsToParticipant(_playerOne, pointDiff);
				list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints + pointDiff, pointDiff));
				
				removePointsFromParticipant(_playerTwo, pointDiff);
				list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints - pointDiff, -pointDiff));
				winner = _playerOne.getName() + " won";
				
				winside = 1;
				
				// Save Fight Result
				if (_startTime != 0) // vGodFather fix
				{
					saveResults(_playerOne, _playerTwo, 1, _startTime, _fightTime, getType());
				}
				rewardParticipant(_playerOne.getPlayer(), getReward());
				_playerOne.getPlayer().sendPacket(Sound.ITEMSOUND_QUEST_ITEMGET.getPacket());
				
				// Notify to scripts
				EventDispatcher.getInstance().notifyEventAsync(new OnOlympiadMatchResult(_playerOne, _playerTwo, getType()), Olympiad.getInstance());
			}
			else if ((_playerOne.getPlayer() == null) || !_playerOne.getPlayer().isOnline() || ((playerOneHp == 0) && (playerTwoHp != 0)) || ((_damageP2 > _damageP1) && (playerOneHp != 0) && (playerTwoHp != 0)))
			{
				try
				{
					ZeuS.Announce_Oly_Winner(_playerTwo, _playerOne);
				}
				catch (Exception a)
				{
					
				}
				sm = SystemMessage.getSystemMessage(SystemMessageId.C1_HAS_WON_THE_GAME);
				sm.addString(_playerTwo.getName());
				stadium.broadcastPacket(sm);
				
				_playerTwo.updateStat(COMP_WON, 1);
				_playerOne.updateStat(COMP_LOST, 1);
				
				addPointsToParticipant(_playerTwo, pointDiff);
				list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints + pointDiff, pointDiff));
				
				removePointsFromParticipant(_playerOne, pointDiff);
				list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints - pointDiff, -pointDiff));
				
				winner = _playerTwo.getName() + " won";
				winside = 2;
				
				// Save Fight Result
				if (_startTime != 0) // vGodFather fix
				{
					saveResults(_playerOne, _playerTwo, 2, _startTime, _fightTime, getType());
				}
				rewardParticipant(_playerTwo.getPlayer(), getReward());
				_playerTwo.getPlayer().sendPacket(Sound.ITEMSOUND_QUEST_ITEMGET.getPacket());
				
				// Notify to scripts
				EventDispatcher.getInstance().notifyEventAsync(new OnOlympiadMatchResult(_playerTwo, _playerOne, getType()), Olympiad.getInstance());
			}
			else
			{
				// Save Fight Result
				if (_startTime != 0) // vGodFather fix
				{
					saveResults(_playerOne, _playerTwo, 0, _startTime, _fightTime, getType());
				}
				
				sm = SystemMessage.getSystemMessage(SystemMessageId.THE_GAME_ENDED_IN_A_TIE);
				stadium.broadcastPacket(sm);
				
				int value = Math.min(playerOnePoints / getDivider(), Config.ALT_OLY_MAX_POINTS);
				
				removePointsFromParticipant(_playerOne, value);
				list1.add(new OlympiadInfo(_playerOne.getName(), _playerOne.getClanName(), _playerOne.getClanId(), _playerOne.getBaseClass(), _damageP1, playerOnePoints - value, -value));
				
				value = Math.min(playerTwoPoints / getDivider(), Config.ALT_OLY_MAX_POINTS);
				removePointsFromParticipant(_playerTwo, value);
				list2.add(new OlympiadInfo(_playerTwo.getName(), _playerTwo.getClanName(), _playerTwo.getClanId(), _playerTwo.getBaseClass(), _damageP2, playerTwoPoints - value, -value));
				
				tie = true;
			}
			
			_playerOne.updateStat(COMP_DONE, 1);
			_playerTwo.updateStat(COMP_DONE, 1);
			_playerOne.updateStat(COMP_DONE_WEEK, 1);
			_playerTwo.updateStat(COMP_DONE_WEEK, 1);
			_playerOne.updateStat(getWeeklyMatchType(), 1);
			_playerTwo.updateStat(getWeeklyMatchType(), 1);
			
			if (winside == 1)
			{
				result = new ExOlympiadMatchResult(tie, winside, list1, list2);
			}
			else
			{
				result = new ExOlympiadMatchResult(tie, winside, list2, list1);
			}
			stadium.broadcastPacket(result, true);
			
			if (Config.ALT_OLY_LOG_FIGHTS)
			{
				LogRecord record = new LogRecord(Level.INFO, winner);
				record.setParameters(new Object[]
				{
					_playerOne.getName(),
					_playerTwo.getName(),
					playerOneHp,
					playerTwoHp,
					_damageP1,
					_damageP2,
					pointDiff,
					getType().toString()
				});
				_logResults.log(record);
			}
		}
		catch (Exception e)
		{
			_log.warn("Exception on validateWinner(): " + e.getMessage(), e);
		}
	}
	
	@Override
	protected final void addDamage(L2PcInstance player, int damage)
	{
		int IdDmg = 0;
		if ((_playerOne.getPlayer() == null) || (_playerTwo.getPlayer() == null))
		{
			return;
		}
		if (player == _playerOne.getPlayer())
		{
			if(_playerTwo.getPlayer().isBlockDamage()){
				return;
			}			
			IdDmg = 1;
			_damageP1 += damage;
		}
		else if (player == _playerTwo.getPlayer())
		{
			if(_playerOne.getPlayer().isBlockDamage()){
				return;
			}
			IdDmg = 2;
			_damageP2 += damage;
		}
		ZeuS.setOlyDmg(_playerOne.getPlayer(), _playerTwo.getPlayer(), IdDmg, damage);
	}
	
	@Override
	public final String[] getPlayerNames()
	{
		return new String[]
		{
			_playerOne.getName(),
			_playerTwo.getName()
		};
	}
	
	@Override
	public boolean checkDefaulted()
	{
		SystemMessage reason;
		_playerOne.updatePlayer();
		_playerTwo.updatePlayer();
		
		reason = checkDefaulted(_playerOne.getPlayer());
		if (reason != null)
		{
			_playerOne.setDefaulted(true);
			if (_playerTwo.getPlayer() != null)
			{
				_playerTwo.getPlayer().sendPacket(reason);
			}
		}
		
		reason = checkDefaulted(_playerTwo.getPlayer());
		if (reason != null)
		{
			_playerTwo.setDefaulted(true);
			if (_playerOne.getPlayer() != null)
			{
				_playerOne.getPlayer().sendPacket(reason);
			}
		}
		
		return _playerOne.isDefaulted() || _playerTwo.isDefaulted();
	}
	
	// vGodFather: better handling olympiad announcements
	@Override
	public void announceGame()
	{
		NpcStringId npcString = null;
		final String arenaId = String.valueOf(getStadiumId() + 1);
		switch (getType())
		{
			case NON_CLASSED:
				npcString = NpcStringId.OLYMPIAD_CLASS_FREE_INDIVIDUAL_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT;
				break;
			case CLASSED:
				npcString = NpcStringId.OLYMPIAD_CLASS_INDIVIDUAL_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT;
				break;
			case TEAMS:
				npcString = NpcStringId.OLYMPIAD_CLASS_FREE_TEAM_MATCH_IS_GOING_TO_BEGIN_IN_ARENA_S1_IN_A_MOMENT;
				break;
			default:
				break;
		}
		
		L2Npc manager;
		NpcSay packet;
		for (L2Spawn spawn : SpawnTable.getInstance().getSpawns(OLY_MANAGER))
		{
			manager = spawn.getLastSpawn();
			if (manager != null)
			{
				packet = new NpcSay(manager.getObjectId(), Say2.NPC_SHOUT, manager.getId(), npcString);
				packet.addStringParameter(arenaId);
				manager.broadcastPacket(packet);
			}
		}
	}
	
	@Override
	public final void resetDamage()
	{
		_damageP1 = 0;
		_damageP2 = 0;
	}
	
	protected static final void saveResults(Participant one, Participant two, int _winner, long _startTime, long _fightTime, CompetitionType type)
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("INSERT INTO olympiad_fights (charOneId, charTwoId, charOneClass, charTwoClass, winner, start, time, classed) values(?,?,?,?,?,?,?,?)"))
		{
			statement.setInt(1, one.getObjectId());
			statement.setInt(2, two.getObjectId());
			statement.setInt(3, one.getBaseClass());
			statement.setInt(4, two.getBaseClass());
			statement.setInt(5, _winner);
			statement.setLong(6, _startTime);
			statement.setLong(7, _fightTime);
			statement.setInt(8, (type == CompetitionType.CLASSED ? 1 : 0));
			statement.execute();
		}
		catch (SQLException e)
		{
			_log.error("SQL exception while saving olympiad fight.", e);
		}
	}
}
