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
package l2r.gameserver.network.clientpackets;

import l2r.Config;
import l2r.gameserver.enums.PartyDistributionType;
import l2r.gameserver.model.BlockList;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ActionFailed;
import l2r.gameserver.network.serverpackets.AskJoinParty;
import l2r.gameserver.network.serverpackets.SystemMessage;

import gr.sr.interf.SunriseEvents;

import ZeuS.ZeuS.ZeuS;

/**
 * sample 29 42 00 00 10 01 00 00 00 format cdd
 * @version $Revision: 1.7.4.4 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestJoinParty extends L2GameClientPacket
{
	private static final String _C__42_REQUESTJOINPARTY = "[C] 42 RequestJoinParty";
	
	private String _name;
	private int _partyDistributionTypeId;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
		_partyDistributionTypeId = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance requestor = getClient().getActiveChar();
		L2PcInstance target = L2World.getInstance().getPlayer(_name);
		
		if (requestor == null)
		{
			return;
		}
		
		if (target == null)
		{
			requestor.sendPacket(SystemMessageId.FIRST_SELECT_USER_TO_INVITE_TO_PARTY);
			return;
		}
		
		if(target.isGM() && target.getAccessLevel().getLevel()==8 && !target.isZeuSGmVis) {
			requestor.sendPacket(SystemMessageId.FIRST_SELECT_USER_TO_INVITE_TO_PARTY);
			return;		
		}
		
		if(target.isGM() && !requestor.isGM()) {
			requestor.sendPacket(SystemMessageId.FIRST_SELECT_USER_TO_INVITE_TO_PARTY);
			return;			
		}

        if(target.isPhantom()){
            if(target.isInStoreMode()){
                requestor.sendMessage("Player is in offline mode.");
                return;
            }
        }else{
            if ((target.getClient() == null) || (target.getClient().isDetached()))
            {
                requestor.sendMessage("Player is in offline mode.");
                return;
            }
        }
		
		if (requestor.isPartyBanned())
		{
			requestor.sendPacket(SystemMessageId.YOU_HAVE_BEEN_REPORTED_SO_PARTY_NOT_ALLOWED);
			requestor.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (target.isPartyBanned())
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_REPORTED_AND_CANNOT_PARTY);
			sm.addCharName(target);
			requestor.sendPacket(sm);
			return;
		}
		
		if (!target.isVisibleFor(requestor))
		{
			requestor.sendPacket(SystemMessageId.TARGET_IS_INCORRECT);
			return;
		}
		
		SystemMessage sm;
		if (target.isInParty())
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ALREADY_IN_PARTY);
			sm.addString(target.getName());
			requestor.sendPacket(sm);
			return;
		}
		
		if (BlockList.isBlocked(target, requestor))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_ADDED_YOU_TO_IGNORE_LIST);
			sm.addCharName(target);
			requestor.sendPacket(sm);
			return;
		}
		
		if (target == requestor)
		{
			requestor.sendPacket(SystemMessageId.YOU_HAVE_INVITED_THE_WRONG_TARGET);
			return;
		}
		
		if (target.isCursedWeaponEquipped() || requestor.isCursedWeaponEquipped())
		{
			requestor.sendPacket(SystemMessageId.INCORRECT_TARGET);
			return;
		}
		
		if (target.isJailed() || requestor.isJailed())
		{
			requestor.sendMessage("You cannot invite a player while is in Jail.");
			return;
		}
		
		if (SunriseEvents.isInEvent(requestor) && !SunriseEvents.canInviteToParty(requestor, target))
		{
			requestor.sendMessage("You may not invite this player to the party.");
			return;
		}
		
		if (target.isInOlympiadMode() || requestor.isInOlympiadMode())
		{
			if ((target.isInOlympiadMode() != requestor.isInOlympiadMode()) || (target.getOlympiadGameId() != requestor.getOlympiadGameId()) || (target.getOlympiadSide() != requestor.getOlympiadSide()))
			{
				requestor.sendPacket(SystemMessageId.A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS);
				return;
			}
		}
		
		if(ZeuS.isInPvPInstance(requestor, false) || ZeuS.isInPvPInstance(target, false)){
			if (!ZeuS.canPvPInstanceParty(requestor, target))
			{
				requestor.sendMessage("You may not invite this player to the party. Him or you are in PvP Instance Zone.");
				return;
			}
			
			if(ZeuS.isInPvPInstanceSoloClan(target)){
				if(requestor.getClan() != target.getClan()){
					requestor.sendMessage("Only players from the same clan can be added to the party in this area");
					return;
				}
			}
		}
		
		if (!ZeuS.canVoteInstanceParty(requestor, target))
		{
			requestor.sendMessage("You may not invite this player to the party. Him or you are in PvP Instance Zone");
			return;
		}
		
		sm = SystemMessage.getSystemMessage(SystemMessageId.C1_INVITED_TO_PARTY);
		sm.addCharName(target);
		requestor.sendPacket(sm);
		
		if (!requestor.isInParty())
		{
			createNewParty(target, requestor);
		}
		else
		{
			if (requestor.getParty().isInDimensionalRift())
			{
				requestor.sendMessage("You cannot invite a player when you are in the Dimensional Rift.");
			}
			else
			{
				addTargetToParty(target, requestor);
			}
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void addTargetToParty(L2PcInstance target, L2PcInstance requestor)
	{
		final L2Party party = requestor.getParty();
		// summary of ppl already in party and ppl that get invitation
		if (!party.isLeader(requestor))
		{
			requestor.sendPacket(SystemMessageId.ONLY_LEADER_CAN_INVITE);
			return;
		}
		if (party.getMemberCount() >= 9)
		{
			requestor.sendPacket(SystemMessageId.PARTY_FULL);
			return;
		}
		if (party.getPendingInvitation() && !party.isInvitationRequestExpired())
		{
			requestor.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			return;
		}
		
		if (!target.isProcessingRequest())
		{
			requestor.onTransactionRequest(target);
			// in case a leader change has happened, use party's mode
			target.sendPacket(new AskJoinParty(requestor.getName(), party.getDistributionType()));
			party.setPendingInvitation(true);
			
			if (Config.DEBUG)
			{
				_log.info("sent out a party invitation to:" + target.getName());
			}
			
		}
		else
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_BUSY_TRY_LATER);
			sm.addString(target.getName());
			requestor.sendPacket(sm);
			
			if (Config.DEBUG)
			{
				_log.warn(requestor.getName() + " already received a party invitation");
			}
		}
	}
	
	/**
	 * @param target
	 * @param requestor
	 */
	private void createNewParty(L2PcInstance target, L2PcInstance requestor)
	{
		PartyDistributionType partyDistributionType = PartyDistributionType.findById(_partyDistributionTypeId);
		if (partyDistributionType == null)
		{
			return;
		}
		
		if (!target.isProcessingRequest())
		{
			target.sendPacket(new AskJoinParty(requestor.getName(), partyDistributionType));
			target.setActiveRequester(requestor);
			requestor.onTransactionRequest(target);
			requestor.setPartyDistributionType(partyDistributionType);
			
			if (Config.DEBUG)
			{
				_log.info("sent out a party invitation to:" + target.getName());
			}
			
		}
		else
		{
			requestor.sendPacket(SystemMessageId.WAITING_FOR_ANOTHER_REPLY);
			
			if (Config.DEBUG)
			{
				_log.warn(requestor.getName() + " already received a party invitation");
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__42_REQUESTJOINPARTY;
	}
}
