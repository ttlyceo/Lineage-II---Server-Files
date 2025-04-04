package l2r.gabriel.instances;


import l2r.gameserver.enums.CtrlIntention;
import l2r.gameserver.model.actor.L2Attackable;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.L2Playable;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.holders.SkillHolder;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.network.NpcStringId;
import l2r.gameserver.network.serverpackets.NpcSay;
import l2r.gameserver.network.serverpackets.SocialAction;
import l2r.gameserver.util.Broadcast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract NPC AI class for datapack based AIs.
 * @author UnAfraid, Zoey76
 */
public abstract class AbstractNpcAI extends Quest
{
    public Logger _log = LoggerFactory.getLogger(getClass());

    public AbstractNpcAI(String name, String descr)
    {
        super(-1, name, descr);
    }

    /**
     * Simple on first talk event handler.
     */
    @Override
    public String onFirstTalk(L2Npc npc, L2PcInstance player)
    {
        return npc.getId() + ".html";
    }

    /**
     * Registers the following events to the current script:<br>
     * <ul>
     * <li>ON_ATTACK</li>
     * <li>ON_KILL</li>
     * <li>ON_SPAWN</li>
     * <li>ON_SPELL_FINISHED</li>
     * <li>ON_SKILL_SEE</li>
     * <li>ON_FACTION_CALL</li>
     * <li>ON_AGGR_RANGE_ENTER</li>
     * </ul>
     * @param mobs
     */
    public void registerMobs(int... mobs)
    {
        addAttackId(mobs);
        addKillId(mobs);
        addSpawnId(mobs);
        addSpellFinishedId(mobs);
        addSkillSeeId(mobs);
        addAggroRangeEnterId(mobs);
        addFactionCallId(mobs);
    }

    /**
     * Broadcasts NpcSay packet to all known players with custom string.
     * @param npc
     * @param type
     * @param text
     */
    protected void broadcastNpcSay(L2Npc npc, int type, String text)
    {
        Broadcast.toKnownPlayers(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getDisplayId(), text));
    }

    /**
     * Broadcasts NpcSay packet to all known players with npc string id.
     * @param npc
     * @param type
     * @param stringId
     */
    protected void broadcastNpcSay(L2Npc npc, int type, NpcStringId stringId)
    {
        Broadcast.toKnownPlayers(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getDisplayId(), stringId));
    }

    /**
     * Broadcasts NpcSay packet to all known players with npc string id.
     * @param npc
     * @param type
     * @param stringId
     * @param parameters
     */
    protected void broadcastNpcSay(L2Npc npc, int type, NpcStringId stringId, String... parameters)
    {
        final NpcSay say = new NpcSay(npc.getObjectId(), type, npc.getTemplate().getDisplayId(), stringId);
        if (parameters != null)
        {
            for (String parameter : parameters)
            {
                say.addStringParameter(parameter);
            }
        }
        Broadcast.toKnownPlayers(npc, say);
    }

    /**
     * Broadcasts NpcSay packet to all known players with custom string in specific radius.
     * @param npc
     * @param type
     * @param text
     * @param radius
     */
    protected void broadcastNpcSay(L2Npc npc, int type, String text, int radius)
    {
        Broadcast.toKnownPlayersInRadius(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getDisplayId(), text), radius);
    }

    /**
     * Broadcasts NpcSay packet to all known players with npc string id in specific radius.
     * @param npc
     * @param type
     * @param stringId
     * @param radius
     */
    protected void broadcastNpcSay(L2Npc npc, int type, NpcStringId stringId, int radius)
    {
        Broadcast.toKnownPlayersInRadius(npc, new NpcSay(npc.getObjectId(), type, npc.getTemplate().getDisplayId(), stringId), radius);
    }

    /**
     * Broadcasts SocialAction packet to self and known players.
     * @param character
     * @param actionId
     */
    protected void broadcastSocialAction(L2Character character, int actionId)
    {
        Broadcast.toSelfAndKnownPlayers(character, new SocialAction(character.getObjectId(), actionId));
    }

    /**
     * Broadcasts SocialAction packet to self and known players in specific radius.
     * @param character
     * @param actionId
     * @param radius
     */
    protected void broadcastSocialAction(L2Character character, int actionId, int radius)
    {
        Broadcast.toSelfAndKnownPlayersInRadius(character, new SocialAction(character.getObjectId(), actionId), radius);
    }

    /**
     * Monster is running and attacking the playable.
     * @param npc
     * @param playable
     */
    protected void attackPlayer(L2Attackable npc, L2Playable playable)
    {
        attackPlayer(npc, playable, 999);
    }

    /**
     * Monster is running and attacking the target.
     * @param npc the NPC that performs the attack
     * @param target the target of the attack
     * @param desire the desire to perform the attack
     */
    protected void attackPlayer(L2Npc npc, L2Playable target, int desire)
    {
        if (npc instanceof L2Attackable)
        {
            ((L2Attackable) npc).addDamageHate(target, 0, desire);
        }
        npc.setIsRunning(true);
        npc.getAI().setIntention(CtrlIntention.AI_INTENTION_ATTACK, target);
    }

    /**
     * Monster cast an skill to the character.
     * @param npc the NPC whom cast the skill
     * @param target the skill target
     * @param skill the skill to cast
     * @param desire the desire to cast the skill
     */
    protected void castSkill(L2Npc npc, L2Character target, SkillHolder skill, int desire)
    {
        if (npc instanceof L2Attackable)
        {
            ((L2Attackable) npc).addDamageHate(target, 0, desire);
        }
        npc.setTarget(target);
        npc.getAI().setIntention(CtrlIntention.AI_INTENTION_CAST, skill.getSkill(), target);
    }
}