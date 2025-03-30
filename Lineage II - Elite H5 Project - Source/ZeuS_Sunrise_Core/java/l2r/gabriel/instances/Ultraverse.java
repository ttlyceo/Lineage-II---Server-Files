package l2r.gabriel.instances;


import gr.sr.interf.SunriseEvents;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.instancemanager.RaidBossSpawnManager;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.L2Summon;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.entity.olympiad.OlympiadManager;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.quest.Quest;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.network.serverpackets.*;

import java.util.Arrays;
/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */
public class Ultraverse extends Quest {
    //NPCs
    private static int CAHIRA = 200290010;
    private static int TELEPORTER = 200290022;
    private static int EXIT_TELEPORTER = 200290014;

    private static String qn = "Ultraverse";
    public static final int INSTANCEID = 2002;

    /*
       HALFDAY = 0;
       ONEDAY = 1;
       TWODAYS = 2;
       HALFWEEK = 3;
       WEEK = 4;
       */
    private static int instanceRefresh = ONEDAY;

    public static int[] ALLOWEDBOSSES = {94042, 23473, 94169, 93932, 23472};
    //bosses id, x, y, z, heading
    public static int[][] bossses = {
        {94042,164497,-85336,-3000,26433},
        {23473,-248731,250088,4336,10747},
        {94169,-73721,52401,-3712,63236},
        {93932,189849,-105569,-784,49151},
        {23472,-250397,207156,-11952,14587},
    };


    //REQUIREMENTS
    private static boolean debug = false;
    private static int levelReq = 84;
    private static int pvpReq = 0;
    private static int fameReq = 0;
    private static int pkReq = 0;

    private static int X_EXIT_LOC = 82852;
    private static int Y_EXIT_LOC = 148611;
    private static int Z_EXIT_LOC = -3464;

    private static final int REWARD_ITEM_COUNT = 10;
    private static final int REWARD_ITEM_ID = 49002;

    private class teleCoord {
        int instanceId;
        int x;
        int y;
        int z;
    }

    private class freeRBWorld extends InstanceWorld {
        public freeRBWorld() {
            super();
        }
    }

    public Ultraverse(int questId, String name, String descr) {
        super(questId, name, descr);

        addStartNpc(CAHIRA);
        addTalkId(CAHIRA);
        addTalkId(TELEPORTER);
        addTalkId(EXIT_TELEPORTER);

        for (L2Spawn boss : RaidBossSpawnManager.getInstance().getSpawns().values())
            addKillId(boss.getId());

        for (int allowedboss : ALLOWEDBOSSES) {
            addKillId(allowedboss);
        }
    }

    public static void main(String[] args) {
        new Ultraverse(-1, qn, "instances");
    }

    private boolean checkConditions(L2PcInstance player, boolean single) {
        if (debug || player.isGM())
            return true;
        else {
            final L2Party party = player.getParty();

//            if (!single && (party == null || party.getMemberCount() < 6 || party.getMemberCount() > 9)) {
//                player.sendMessage("This is a 6-9 player party instance, so you must have a party of 6-9 people");
//                return false;
//            }
//            if (!single && party.getLeader().getObjectId() != player.getObjectId()) {
//                player.sendPacket(SystemMessage.getSystemMessage(2185));
//                return false;
//            }

            if(party != null){
                player.sendMessage("You cannot enter with a party.");
                return false;
            }

//            if (!single) {
//                if (!checkIPs(party))
//                    return false;
//
//                boolean canEnter = true;
//
//                for (L2PcInstance ptm : party.getMembers()) {
//                    if (ptm == null) return false;
//
//                    if (ptm.getLevel() < levelReq) {
//                        ptm.sendMessage("You must be level " + levelReq + " to enter this instance");
//                        canEnter = false;
//                    } else if (ptm.getPvpKills() < pvpReq) {
//                        ptm.sendMessage("You must have " + pvpReq + " PvPs to enter this instance");
//                        canEnter = false;
//                    } else if (ptm.getPkKills() < pkReq) {
//                        ptm.sendMessage("You must have " + pkReq + " PKs to enter this instance");
//                        canEnter = false;
//                    } else if (ptm.getFame() < fameReq) {
//                        ptm.sendMessage("You must have " + fameReq + " fame to enter this instance");
//                        canEnter = false;
//                    } else if (ptm.getPvpFlag() != 0 || ptm.getKarma() > 0) {
//                        ptm.sendMessage("You can't enter the instance while in PVP mode or have karma");
//                        canEnter = false;
//                    }else if(SunriseEvents.isRegistered(ptm) || SunriseEvents.isInEvent(ptm)){
//                        ptm.sendMessage("You are already registered in another event!!");
//                        canEnter = false;
//                    }
////                    else if (ptm.isInFunEvent()) {
////                        ptm.sendMessage("You can't enter the instance while in an event");
////                        canEnter = false;
////                    }
//                    else if (ptm.isInDuel() || ptm.isInOlympiadMode()  || OlympiadManager.getInstance().isRegistered(player)) {
//                        ptm.sendMessage("You can't enter the instance while in duel/oly");
//                        canEnter = false;
//                    }
//                    else if (System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(ptm.getObjectId(), INSTANCEID)) {
//                        ptm.sendMessage("You can only enter this instance once a week. Wait until next Sunday.");
//                        canEnter = false;
//                    } else if (!ptm.isInsideRadius(player, 500, true, false)) {
//                        ptm.sendMessage("You're too far away from your party leader");
//                        player.sendMessage("One of your party members is too far away");
//                        canEnter = false;
//                    } else {
//                        final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);
//
//                        if (world != null) {
//                            ptm.sendMessage("You can't enter because you have entered into another instance that hasn't expired yet, try waiting 5 min");
//                            canEnter = false;
//                        }
//                    }
//
//                    if (!canEnter) {
//                        ptm.sendMessage("You're preventing your party from entering an instance");
//                        if (ptm != player)
//                            player.sendMessage(ptm.getName() + " is preventing you from entering the instance");
//                        return false;
//                    }
//                }
//            } else {
                if (player.getLevel() < levelReq) {
                    player.sendMessage("You must be level " + levelReq + " to enter this instance");
                    return false;
                }else if (player.isInDuel() || player.isInOlympiadMode() || OlympiadManager.getInstance().isRegistered(player)) {
                    player.sendMessage("You can't enter the instance while in duel/oly");
                    return false;
                }else if (System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(player.getObjectId(), INSTANCEID)) {
                    player.sendMessage("You can enter this instance every 2 times a week. Wait until next Sunday.");
                    return false;
                }
                else if (player.getPvpKills() < pvpReq) {
                    player.sendMessage("You must have " + pvpReq + " PvPs to enter this instance");
                    return false;
                } else if (player.getFame() < fameReq) {
                    player.sendMessage("You must have " + fameReq + " fame to enter this instance");
                    return false;
                } else if (player.getPvpFlag() != 0 || player.getKarma() > 0) {
                    player.sendMessage("You can't enter the instance while in PVP mode or have karma");
                    return false;
                } else if(SunriseEvents.isRegistered(player) || SunriseEvents.isInEvent(player)){
                    player.sendMessage("You are already registered in another event!!");
                    return false;
                }
//                else if (player.isInFunEvent()) {
//                    player.sendMessage("You can't enter the instance while in an event");
//                    return false;
//                }
                else if (player.isInDuel() || player.isInOlympiadMode() || OlympiadManager.getInstance().isRegistered(player)) {
                    player.sendMessage("You can't enter the instance while in duel/oly");
                    return false;
                }
//            }

            return true;
        }
    }

    private void teleportplayer(L2PcInstance player, teleCoord teleto) {
        player.setInstanceId(teleto.instanceId);
        player.teleToLocation(teleto.x, teleto.y, teleto.z);
        L2Summon pet = player.getSummon();
        if (pet != null) {
            pet.setInstanceId(teleto.instanceId);
            pet.teleToLocation(teleto.x, teleto.y, teleto.z);
        }
    }

    protected int enterInstance(L2PcInstance player, String template, teleCoord teleto) {
        int instanceId = 0;

        //check for existing instances for this player
        InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

        if (!checkConditions(player, false))
            return 0;

        L2Party party = player.getParty();
        instanceId = InstanceManager.getInstance().createDynamicInstance(template);
        world = new freeRBWorld();
        world.setInstanceId(instanceId);
        world.setTemplateId(INSTANCEID);
        InstanceManager.getInstance().addWorld(world);
//            _log.info("Ultraverse: new " + template + " Instance: " + instanceId + " created by player: " + player.getName());
        // teleport players
        teleto.instanceId = instanceId;

        if (party == null) {
            // this can happen only if debug is true
            InstanceManager.getInstance().setInstanceTime(player.getObjectId(), INSTANCEID, getNextInstanceTime(instanceRefresh));
            world.addAllowed(player.getObjectId());
            teleportplayer(player, teleto);
        }
//        else {
//            for (L2PcInstance partyMember : party.getMembers()) {
//                if (partyMember == null) continue;
//                partyMember.sendMessage("You have entered the alternate dimension of raidbosses");
//                InstanceManager.getInstance().setInstanceTime(partyMember.getObjectId(), INSTANCEID, getNextInstanceTime(WEEK));
//                world.addAllowed(partyMember.getObjectId());
//                teleportplayer(partyMember, teleto);
//            }
//        }

        for (int[] bosss : bossses) {
            if(Arrays.stream(ALLOWEDBOSSES).anyMatch(e-> e == bosss[0])) {
                addSpawn(bosss[0], bosss[1], bosss[2], bosss[3], bosss[4], false, 120 * 60 * 1000, false, instanceId);
            }
        }
        spawnGK((freeRBWorld) world, player);
        showWindows((freeRBWorld) world, player);
        return instanceId;


    }

    protected void exitInstance(L2PcInstance player, teleCoord tele) {
        player.setInstanceId(0);
        player.teleToLocation(tele.x, tele.y, tele.z);

        L2Summon pet = player.getSummon();
        if (pet != null) {
            pet.setInstanceId(0);
            pet.teleToLocation(tele.x, tele.y, tele.z);
        }
    }

    protected void startInstance(L2PcInstance player, teleCoord tele) {
        player.setInstanceId(0);
        player.teleToLocation(tele.x, tele.y, tele.z);

        L2Summon pet = player.getSummon();
        if (pet != null) {
            pet.setInstanceId(0);
            pet.teleToLocation(tele.x, tele.y, tele.z);
        }
    }

    public void spawnGK(freeRBWorld world, L2PcInstance player) {
        addSpawn(TELEPORTER, -213473,244899,2016, 0, true, 0, false, world.getInstanceId());
    }

    @Override
    public String onTalk(L2Npc npc, L2PcInstance player) {
        final int npcId = npc.getId();

        QuestState st = player.getQuestState(qn);

        if (st == null)
            st = newQuestState(player);

        if (npcId == CAHIRA) {
            teleCoord teleto = new teleCoord();
            teleto.x = -213304;
            teleto.y = 245061;
            teleto.z = 2016;
            enterInstance(player, "Ultraverse.xml", teleto);
        } else if (npcId == TELEPORTER) {
            final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

            if (world == null || !(world instanceof freeRBWorld))
                return null;

            teleCoord teleto = new teleCoord();
            teleto.x = -213304;
            teleto.y = 245061;
            teleto.z = 2016;

            if (player.getParty() == null) {
                startInstance(player, teleto);
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    startInstance(ptm, teleto);
                }
            }

        } else if (npcId == EXIT_TELEPORTER) {
            final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

            if (world == null || !(world instanceof freeRBWorld))
                return null;

            teleCoord teleto = new teleCoord();
            teleto.x = X_EXIT_LOC;
            teleto.y = Y_EXIT_LOC;
            teleto.z = Z_EXIT_LOC;


            if (player.getParty() == null) {
                exitInstance(player, teleto);
                player.sendPacket(new ExShowScreenMessage("You have completed the Ultraverse instance", 6000));
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    exitInstance(ptm, teleto);
                    player.sendPacket(new ExShowScreenMessage("You have completed the Ultraverse instance", 6000));
                }
            }

            st.exitQuest(true);
        }

        return null;
    }

    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet) {
        if (npc.getInstanceId() < 1000)
            return null;

        final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(killer);

        if (world == null || !(world instanceof freeRBWorld)) {
            QuestState st = killer.getQuestState(qn);

            if (st != null)
                st.exitQuest(true);

            return null;
        }

        final freeRBWorld rbWorld = (freeRBWorld) world;

        int instId = InstanceManager.getInstance().getPlayerInstance(killer.getObjectId());
        Instance instance = InstanceManager.getInstance().getInstance(instId);

        instance.removeNpcs();

        for (Integer player : instance.getPlayers()) {
            L2World.getInstance().getPlayer(player).sendPacket(new ExShowScreenMessage("You have killed " + npc.getName() + ". Now you're free to leave", 6000));
        }

        annunceToAll(npc, killer);

        rewardItem(rbWorld, killer);

        spawnExitGK(rbWorld, killer);

        return null;
    }

    public void displayCongrats(L2PcInstance player) {
        player.broadcastPacket(new SocialAction(player.getObjectId(), 3));//Victory Social Action.
        MagicSkillUse MSU = new MagicSkillUse(player, player, 2024, 1, 1, 0);//Fireworks Display
        player.broadcastPacket(MSU);
    }

    public void annunceToAll(L2Npc npc, L2PcInstance player) {
//        Broadcast.toAllOnlinePlayers(player.getName() + " and your party killed the boss " + npc.getName() + " of Ultraverse Instance.");
    }

    public void rewardItem(freeRBWorld world, L2PcInstance player) {
        if (player.getParty() == null) {
            player.addItem("Ultraverse Instance: ", REWARD_ITEM_ID, REWARD_ITEM_COUNT, player, true);
        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                ptm.addItem("Ultraverse Instance: ", REWARD_ITEM_ID, REWARD_ITEM_COUNT, player, true);
            }
        }
    }

    public void showWindows(freeRBWorld world, L2PcInstance player) {
        if (player.getParty() == null) {
            String filename = "data/html/instance/ultraverse/ultraverse.htm";
            NpcHtmlMessage itemReply = new NpcHtmlMessage(1);
            itemReply.setFile(player, player.getHtmlPrefix(), filename);
            player.sendPacket(itemReply);
        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                String filename = "data/html/instance/ultraverse/ultraverse.htm";
                NpcHtmlMessage itemReply = new NpcHtmlMessage(1);
                itemReply.setFile(player, player.getHtmlPrefix(), filename);
                ptm.sendPacket(itemReply);
            }
        }
    }

    public void spawnExitGK(freeRBWorld world, L2PcInstance player) {
        addSpawn(EXIT_TELEPORTER, player.getX(), player.getY(), player.getZ(), 0, true, 0, false, world.getInstanceId());
        displayCongrats(player);
    }
}
