package l2r.gabriel.instances;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import gr.sr.interf.SunriseEvents;
import l2r.gameserver.enums.CtrlEvent;
import l2r.gameserver.enums.CtrlIntention;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.L2Attackable;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.L2Summon;
import l2r.gameserver.model.actor.instance.L2DoorInstance;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.effects.L2EffectType;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.entity.olympiad.OlympiadManager;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.model.skills.L2Skill;
import l2r.gameserver.network.SystemMessageId;
import l2r.gameserver.network.serverpackets.ExSendUIEvent;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;
import l2r.gameserver.network.serverpackets.SystemMessage;
/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */
public class BehemothKing extends Quest
{
    private class DHSWorld extends InstanceWorld
    {
        public Map<L2Npc, Boolean> npcList = new HashMap<>();
        public int monstersAlive = 0;
        public L2Npc BEHEMOTH_BOSS = null;
        public boolean isBossesAttacked = false;
        public long[] storeTime =
                {
                        0,
                        0
                }; // 0: instance start, 1: finish time

        public DHSWorld()
        {
            // InstanceManager.getInstance().super();
        }
    }

    private static final int INSTANCEID = 161; // this is the client number
    private static final boolean debug = false;

    private static int X_EXIT_LOC = 82852;
    private static int Y_EXIT_LOC = 148611;
    private static int Z_EXIT_LOC = -3464;

    private static int ENTRANCE_X = 144768;
    private static int ENTRANCE_Y = 147373;
    private static int ENTRANCE_Z = -12112;

    /*
    HALFDAY = 0;
    ONEDAY = 1;
    TWODAYS = 2;
    HALFWEEK = 3;
    WEEK = 4;
    */
    private static int instanceRefresh = HALFWEEK;

    // NPCs
    private static final int ENTRANCE_NPC = 16160001;
    private static final int FINAL_NPC = 16159999;
    //back teleporter
    private static final int[] FINAL_NPC_SPAWN = {144802,143716,-12808};

    // mobs
    private static final int BEHEMOTH_BOSS = 93944;

    //@formatter:off
    private static final int[] BEHEMOTH_MOBIDS = {
            93804,
            93807,
            93813,
            93811,
            93808,
            93815,
    };
    private static final int[][] BOSS_SPAWN = {{BEHEMOTH_BOSS,145131,143710,-12808, 63943}};

    // Doors/Walls/Zones
    //{id, x, y , z}
    private static final int[][] ROOM_1_MOBS = {
            {93807,142877,146632,-12032},
            {93813,142861,146249,-12032},
            {93811,142640,146338,-12032},
            {93808,142504,146107,-12032},
            {93815,142559,145814,-12032},
            {93804,142193,145658,-12032},
            {93807,142174,145342,-12032},
            {93813,142529,145455,-12032},
            {93811,142879,145803,-12032},
            {93808,143434,146184,-12032},
            {93815,143314,145921,-12032},
            {93804,143831,145500,-12024},
            {93807,143403,145540,-12032},
            {93813,142633,145044,-12024},
            {93811,142291,144796,-11992},
            {93808,142340,144420,-11952},
            {93815,143021,145215,-12032},
            {93804,143321,145037,-12024},
            {93807,143306,144535,-12008},
            {93813,143335,144053,-11976},
            {93811,143202,143535,-11912},
            {93808,143458,143433,-11920},
            {93815,143501,143097,-11904},
            {93804,143325,143004,-11880},
            {93807,143568,142679,-11880},
            {93813,143980,143019,-11888},
            {93811,144299,143465,-11968},
            {93808,144524,143852,-12000},
            {93815,144839,143618,-11976},
            {93804,145246,143641,-11880},
            {93807,145461,143481,-11816},
            {93813,145574,143711,-11808},
            {93811,145447,143922,-11816},
            {93808,145611,144102,-11816},
            {93815,144947,143977,-11968},
            {93804,144015,143923,-12000},
            {93807,143957,143623,-11960},
            {93813,143920,143297,-11920},
            {93811,142403,143593,-11880},
            {93808,142019,143983,-11896},
            {93815,141765,144516,-11952},
            {93804,141909,144881,-12000},
            {93807,142227,143369,-11848},
            {93813,142908,142800,-11872},
            {93811,142741,144267,-11952},
            {93808,145245,144076,-11880},
            {93815,145853,144376,-11864},
            {93804,145979,144235,-11888},
            {93807,146076,144550,-11912},
            {93813,146307,144440,-11976},
            {93811,146442,144781,-12024},
            {93808,146867,144577,-12144},
            {93815,147272,144882,-12224},
            {93804,145742,143248,-11840},
            {93807,145952,143115,-11896},
            {93813,146268,143010,-11968},
            {93811,146408,142768,-12016},
            {93808,146701,142957,-12096},
            {93815,146984,142853,-12168},
            {93804,147291,142687,-12224},
            {93807,147562,142998,-12224},
            {93813,147933,142724,-12224},
            {93811,147937,142970,-12224},
            {93808,147645,143213,-12224},
            {93815,147857,143372,-12224},
            {93804,148303,142867,-12224},
            {93807,148575,142654,-12224},
            {93813,148869,142963,-12232},
            {93811,148537,143012,-12224},
            {93808,148491,143300,-12224},
            {93815,148175,143663,-12224},
            {93804,148944,143667,-12232},
            {93807,149182,143826,-12232},
            {93813,149080,144090,-12232},
            {93811,148858,143977,-12232},
            {93808,149183,144797,-12288},
            {93815,149025,145368,-12304},
            {93804,148270,145174,-12248},
            {93807,148759,145831,-12312},
            {93813,148711,146540,-12320},
            {93811,149131,146583,-12344},
            {93808,148195,146618,-12296},
            {93815,147645,146460,-12264},
            {93804,148102,146169,-12272},
            {93807,148656,146157,-12312},
            {93813,148125,145654,-12256},
            {93811,147639,145663,-12216},
            {93808,147817,144943,-12224},
            {93815,147676,144401,-12224},
            {93804,148178,144177,-12224},
            {93807,148522,143829,-12232},
            {93813,148692,144390,-12240},
            {93811,148621,144994,-12272},
            {93808,149703,145782,-12336},
            {93815,149701,145179,-12280},
            {93804,149991,144699,-12240},
            {93807,147756,143836,-12224},
            {93813,143772,145935,-12032},
            {93811,144102,146265,-12024},
            {93808,143776,146560,-12032}
    };

    //@formatter:on

    // Instance reenter time
    // default: 24h
    private static final int INSTANCEPENALTY = 24;

    public class teleCoord
    {
        int instanceId;
        int x;
        int y;
        int z;
    }

    public BehemothKing()
    {
        super(-1, BehemothKing.class.getSimpleName(), "gracia/instances");

        addStartNpc(ENTRANCE_NPC);
        addTalkId(ENTRANCE_NPC);
        addStartNpc(FINAL_NPC);
        addTalkId(FINAL_NPC);
        addKillId(BEHEMOTH_BOSS);
        addAttackId(BEHEMOTH_BOSS);
        addSkillSeeId(BEHEMOTH_MOBIDS);
        addKillId(BEHEMOTH_MOBIDS);
    }

    private boolean checkConditions(L2PcInstance player)
    {
        if (debug || player.isGM())
        {
            return true;
        }
        L2Party party = player.getParty();
//        if (party == null)
//        {
//            player.sendPacket(SystemMessage.getSystemMessage(2101));
//            return false;
//        }
//        if (party.getLeader() != player)
//        {
//            player.sendPacket(SystemMessage.getSystemMessage(2185));
//            return false;
//        }
        if(party != null){
            player.sendMessage("You cannot enter with party!");
            return false;
        }
//        if (!checkIPs(party))
//            return false;

//        for (L2PcInstance partyMember : party.getMembers())
//        {
            if ((player.getLevel() < 80) || (player.getLevel() > 85))
            {
                SystemMessage sm = SystemMessage.getSystemMessage(2097);
                sm.addPcName(player);
                player.sendMessage("You need to wait for the time to re-enter the instance!");
                return false;
            }
//            if (!Util.checkIfInRange(1000, player, party, true))
//            {
//                SystemMessage sm = SystemMessage.getSystemMessage(2096);
//                sm.addPcName(partyMember);
//                party.broadcastPacket(sm);
//                return false;
//            }
            if(SunriseEvents.isRegistered(player) || SunriseEvents.isInEvent(player)){
                player.sendMessage("You are already registered in another event!!");
                return false;
            }
            if (player.isInDuel() || player.isInOlympiadMode() || OlympiadManager.getInstance().isRegistered(player)) {
                player.sendMessage("You can't enter the instance while in duel/oly");
                return false;
            }
            Long reentertime = InstanceManager.getInstance().getInstanceTime(player.getObjectId(), INSTANCEID);
            if (System.currentTimeMillis() < reentertime)
            {
                SystemMessage sm = SystemMessage.getSystemMessage(2100);
                sm.addPcName(player);
                player.sendMessage("You need to wait for the time to re-enter the instance!");
                return false;
            }
//        }
        return true;
    }

    private void teleportplayer(L2PcInstance player, teleCoord teleto)
    {
        player.getAI().setIntention(CtrlIntention.AI_INTENTION_IDLE);
        player.setInstanceId(teleto.instanceId);
        player.teleToLocation(teleto.x, teleto.y, teleto.z);
    }

    protected void enterInstance(L2PcInstance player, String template, teleCoord teleto)
    {
        // check for existing instances for this player
        InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
        // existing instance

//
        if (checkConditions(player))
        {
            world = new DHSWorld();
            world.setInstanceId(InstanceManager.getInstance().createDynamicInstance(template));
            world.setTemplateId(INSTANCEID);
            world.setStatus(0);
            ((DHSWorld) world).storeTime[0] = System.currentTimeMillis();
            InstanceManager.getInstance().addWorld(world);
            _log.info("Epic Mission as been started " + template + " Instance: " + world.getInstanceId() + " created by player: " + player.getName());
            runTumors((DHSWorld) world);
            // teleport players
            teleto.instanceId = world.getInstanceId();

            L2Party party = player.getParty();
            if (party == null)
            {
                InstanceManager.getInstance().setInstanceTime(player.getObjectId(), INSTANCEID, getNextInstanceTime(instanceRefresh));
                teleportplayer(player, teleto);
                world.addAllowed(player.getObjectId());
            }
//            else
//            {
//                for (L2PcInstance partyMember : party.getMembers())
//                {
//                    InstanceManager.getInstance().setInstanceTime(partyMember.getObjectId(), INSTANCEID, getNextInstanceTime(instanceRefresh));
//                    teleportplayer(partyMember, teleto);
//                    world.addAllowed(partyMember.getObjectId());
//                }
//            }
        }
    }

    protected void exitInstance(L2PcInstance player, teleCoord tele)
    {
        player.setInstanceId(0);
        player.teleToLocation(tele.x, tele.y, tele.z);
        L2Summon pet = player.getSummon();
        if (pet != null)
        {
            pet.setInstanceId(0);
            pet.teleToLocation(tele.x, tele.y, tele.z);
        }
    }

    protected boolean checkKillProgress(DHSWorld world)
    {
//        int deadCounter = 0;
//        if (world.npcList.containsKey(mob))
//        {
//            world.npcList.put(mob, true);
//        }
//        if(!afterMobsLoop) {
//            for (boolean isDead : world.npcList.values()) {
//                if (!isDead) {
//                    return false;
//                }
//            }
//        }else{
//            for (boolean isDead : world.npcList.values()) {
//                if (isDead) {
//                    deadCounter++;
//                }
//            }
//            return world.npcList.size() == (deadCounter -1);
//        }
        return world.monstersAlive <= 1;
    }

    protected int[][] getRoomSpawns(int room)
    {
        switch (room)
        {
            case 0:
                return ROOM_1_MOBS;
        }
        _log.warn("");
        return new int[][] {};
    }

    protected void runTumors(DHSWorld world)
    {
        for (int[] mob : getRoomSpawns(world.getStatus()))
        {
            L2Npc npc = addSpawn(mob[0], mob[1], mob[2], mob[3], 0, false, 0, false, world.getInstanceId());
            npc.setIsNoRndWalk(true);
            world.npcList.put(npc, false);
            world.monstersAlive++;
        }

        world.setStatus(world.getStatus() + 1); // sets to status 1
    }

    protected void runTwins(DHSWorld world)
    {
        world.setStatus(world.getStatus() + 1); //sets to status 2
        L2DoorInstance door = this.getDoor(24220003, world.getInstanceId());
        door.openMe();
        world.BEHEMOTH_BOSS = addSpawn(BOSS_SPAWN[0][0], BOSS_SPAWN[0][1], BOSS_SPAWN[0][2], BOSS_SPAWN[0][3], BOSS_SPAWN[0][4], false, 0, false, world.getInstanceId());
        world.BEHEMOTH_BOSS.setIsNoRndWalk(true);
        world.BEHEMOTH_BOSS.setIsMortal(true);
    }

    protected void bossSimpleDie(L2Npc boss)
    {
        // killing is only possible one time
        synchronized (this)
        {
            if (boss.isDead())
            {
                return;
            }
            // now reset currentHp to zero
            boss.setCurrentHp(0);
            boss.setIsDead(true);
        }

        // Set target to null and cancel Attack or Cast
        boss.setTarget(null);

        // Stop movement
        boss.stopMove(null);

        // Stop HP/MP/CP Regeneration task
        boss.getStatus().stopHpMpRegeneration();

        boss.stopAllEffectsExceptThoseThatLastThroughDeath();

        // Send the Server->Client packet StatusUpdate with current HP and MP to all other L2PcInstance to inform
        boss.broadcastStatusUpdate();

        // Notify L2Character AI
        boss.getAI().notifyEvent(CtrlEvent.EVT_DEAD);

        if (boss.getWorldRegion() != null)
        {
            boss.getWorldRegion().onDeath(boss);
        }
    }

    @Override
    public String onSkillSee(L2Npc npc, L2PcInstance caster, L2Skill skill, L2Object[] targets, boolean isPet)
    {
        if (skill.hasEffectType(L2EffectType.REBALANCE_HP, L2EffectType.HEAL, L2EffectType.HEAL_PERCENT))
        {
            int hate = 2 * skill.getAggroPoints();
            if (hate < 2)
            {
                hate = 1000;
            }
            ((L2Attackable) npc).addDamageHate(caster, 0, hate);
        }
        return super.onSkillSee(npc, caster, skill, targets, isPet);
    }

    @Override
    public String onAttack(L2Npc npc, L2PcInstance attacker, int damage, boolean isPet, L2Skill skill)
    {
        InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
        if (tmpworld instanceof DHSWorld)
        {
            if (!((DHSWorld) tmpworld).isBossesAttacked)
            {
                ((DHSWorld) tmpworld).isBossesAttacked = true;
                Calendar reenter = Calendar.getInstance();
                reenter.add(Calendar.HOUR, INSTANCEPENALTY);

                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.INSTANT_ZONE_FROM_HERE_S1_S_ENTRY_HAS_BEEN_RESTRICTED);
                sm.addString(InstanceManager.getInstance().getInstanceIdName(tmpworld.getTemplateId()));

                // set instance reenter time for all allowed players
                for (int objectId : tmpworld.getAllowed())
                {
                    L2PcInstance player = L2World.getInstance().getPlayer(objectId);
                    if ((player != null) && player.isOnline())
                    {
                        InstanceManager.getInstance().setInstanceTime(objectId, tmpworld.getTemplateId(), reenter.getTimeInMillis());
                        player.sendPacket(sm);
                    }
                }
            }
            else if (damage >= npc.getCurrentHp())
            {
                if (((DHSWorld) tmpworld).BEHEMOTH_BOSS.isDead())
                {
                    ((DHSWorld) tmpworld).BEHEMOTH_BOSS.setIsDead(false);
                    ((DHSWorld) tmpworld).BEHEMOTH_BOSS.doDie(attacker);
                }
            }
        }
        return null;
    }

    private void sendRemaining(DHSWorld world){
        Instance instance = InstanceManager.getInstance().getInstance(world.getInstanceId());

        int alive = world.monstersAlive;

        if(alive <= 0)
            return;
        for (Integer playerobjId : instance.getPlayers()) {
            L2PcInstance player = L2World.getInstance().getPlayer(playerobjId);
            player.sendPacket(new ExShowScreenMessage(1, -1, ExShowScreenMessage.BOTTOM_RIGHT, 0, 1, 0, 0, false, 5000, false, "Kill " + alive + " monsters to next stage!"));

        }
    }

    @Override
    public String onKill(L2Npc npc, L2PcInstance player, boolean isPet)
    {
        InstanceWorld tmpworld = InstanceManager.getInstance().getWorld(npc.getInstanceId());
        if (tmpworld instanceof DHSWorld)
        {
            DHSWorld world = (DHSWorld) tmpworld;
            world.monstersAlive--;
            sendRemaining(world);
            if (world.getStatus() < 1)
            {
                if (checkKillProgress(world))
                {
                    runTumors(world);
                }
            }
            else if (world.getStatus() == 1)
            {
                if (checkKillProgress(world))
                {
                    L2DoorInstance door = this.getDoor(24220003, world.getInstanceId());
                    door.openMe();
                    runTwins(world);
                }
            }
            else if ((world.getStatus() == 2) && ((npc.getId() == BEHEMOTH_BOSS)))
            {
                if (world.BEHEMOTH_BOSS.isDead())
                {
                    world.setStatus(world.getStatus() + 1);
                    // instance end
                    world.storeTime[1] = System.currentTimeMillis();
                    world.BEHEMOTH_BOSS = null;
//                    Broadcast.toAllOnlinePlayers("King Behemoth as been defeated by brave heroes!");
                    addSpawn(FINAL_NPC, FINAL_NPC_SPAWN[0], FINAL_NPC_SPAWN[1], FINAL_NPC_SPAWN[2], 0, false, 0, false, world.getInstanceId());

                    for (Integer pc : world.getAllowed())
                    {
                        L2PcInstance killer = L2World.getInstance().getPlayer(pc);
                        if (killer != null)
                        {
                            killer.sendPacket(new ExSendUIEvent(killer, true, true, 0, 0, ""));
                        }
                    }

                    Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
                    inst.setDuration(5 * 60000);
                    inst.setEmptyDestroyTime(0);

                }
            }
        }
        return "";
    }

    @Override
    public String onTalk(L2Npc npc, L2PcInstance player)
    {
        String htmltext = getNoQuestMsg(player);
        final QuestState st = getQuestState(player, true);
        if (st == null)
        {
            return htmltext;
        }

        int npcId = npc.getId();
        if (npcId == ENTRANCE_NPC)
        {
            teleCoord tele = new teleCoord();
            tele.x = ENTRANCE_X;
            tele.y = ENTRANCE_Y;
            tele.z = ENTRANCE_Z;
            enterInstance(player, "BehemothKing.xml", tele);
            return "";
        }
        else if (npcId == FINAL_NPC)
        {
            InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
            Long finishDiff = ((DHSWorld) world).storeTime[1] - ((DHSWorld) world).storeTime[0];
            if (finishDiff < (10*60*1000)) //10 minutes <
            {
                st.giveItems(49002, 30);
                st.giveItems(6673, 20);
                st.giveItems(6622, 10);
                st.giveItems(9627, 5);
            }
            else if (finishDiff < 15*60*1000) //15 minutes <
            {
                st.giveItems(49002, 20);
                st.giveItems(6673, 15);
                st.giveItems(6622, 7);
                st.giveItems(9627, 3);
            }
            else if (finishDiff < 20*60*1000) // 20 minutes <
            {
                st.giveItems(49002, 15);
                st.giveItems(6673, 10);
                st.giveItems(6622, 5);
                st.giveItems(9627, 2);
            }
            else
            {
                st.giveItems(49002, 10);
                st.giveItems(6673, 7);
                st.giveItems(6622, 3);
                st.giveItems(9627, 1);
            }
            world.removeAllowed(player.getObjectId());
            teleCoord tele = new teleCoord();
            tele.instanceId = 0;
            tele.x = X_EXIT_LOC;
            tele.y = Y_EXIT_LOC;
            tele.z = Z_EXIT_LOC;
            exitInstance(player, tele);
        }
        return "";
    }
}
