package l2r.gabriel.instances;


import gr.sr.interf.SunriseEvents;
import l2r.Config;
import l2r.gameserver.cache.HtmCache;
import l2r.gameserver.instancemanager.InstanceManager;
import l2r.gameserver.model.L2Party;
import l2r.gameserver.model.L2Spawn;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.Location;
import l2r.gameserver.model.actor.L2Npc;
import l2r.gameserver.model.actor.L2Summon;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Instance;
import l2r.gameserver.model.entity.olympiad.OlympiadManager;
import l2r.gameserver.model.instancezone.InstanceWorld;
import l2r.gameserver.model.quest.QuestState;
import l2r.gameserver.network.serverpackets.ExShowScreenMessage;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

import java.util.Set;

/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */
public class TOI extends AbstractInstance {

    //Instance Configuration
    private static String qn = "TOI";
    public static final int INSTANCEID = 2003;
    private static boolean debug = false;
    private static int levelReq = 84;
    private static int pvpReq = 0;
    private static int fameReq = 0;


    /*
     * Time to destroy instance (and eject players away) after boss defeat Default: 5 minutes
     */
    private static final int EXIT_TIME = 5;
    private static int X_EXIT_LOC = 82852;
    private static int Y_EXIT_LOC = 148611;
    private static int Z_EXIT_LOC = -3464;

    private static int ENTRANCE_X= 121186;
    private static int ENTRANCE_Y= 16060;
    private static int ENTRANCE_Z= -4995;
    //NPCs
    private static int MALEFICENT = 200380000;
    private static int TELEPORTER = 200380001;
    private static int TELEPORTER2 = 200380002;
    private static int GK = 200390049;
    private static int BAAL = 200390008;

    //TOI 1 MOBS
    private static final Integer[] MOBS = {200390077, 200390078, 200390080, 200390081, 200390082, 200390083, 200390084,94083,94084,94085}; //1º floor
    private static final Integer[] MOBS01 = {200390079}; //1º floor

    //TOI 2 MOBS
    private static final Integer[] MOBS2 = {200390080, 200390081, 200390082, 200390083, 200390084}; //2º floor
    private static final Integer[] MOBS02 = {200390079, 200390085, 200390086, 200390087}; //2º floor

    //TOI 3 MOBS
    private static final Integer[] MOBS3 = {200390085, 200390082, 200390086, 200390087, 200390083, 200390084}; //3º floor
    private static final Integer[] MOBS03 = {200390088, 200390089, 200390090, 200390091, 200390092};

    //TOI 4 MOBS
    private static final Integer[] MOBS4 = {200390092, 200390091, 200390088, 200390093, 200390090, 200390094, 200390095, 200390096}; //4º floor

    //TOI 5 MOBS
    private static final Integer[] MOBS5 = {200390093, 200390090, 200390094, 200390096, 200390095, 200390097, 200390099}; //5º floor
    private static final Integer[] MOBS05 = {200390098}; //5º floor
    private static final Integer[] MOBS005 = {200390100}; //5º floor

    //TOI 6 MOBS
    private static final Integer[] MOBS6 = {200390101, 200390102, 200390103, 200390104, 200390105, 200390091, 200390092, 200390106}; //6º floor
    private static final Integer[] MOBS06 = {200390107};

    //TOI 7 MOBS
    private static final Integer[] MOBS7 = {200390108, 200390109, 200390110, 200390111}; //7º floor

    //TOI 8 MOBS
    private static final Integer[] MOBS8 = {200390113}; //8º floor
    private static final Integer[] MOBS08 = {200390103, 200390105, 200390091, 200390092, 200390106, 200390101, 200390104};

    //TOI 9 MOBS
    private static final Integer[] MOBS9 = {200390113, 200390114, 200390115, 200390116}; //9º floor
    private static final Integer[] MOBS09 = {200390117, 200390118};
    private static final Integer[] MOBS009 = {200390119, 200390129};

    //TOI 10 MOBS
    private static final Integer[] MOBS10 = {200390114, 200390115, 200390116, 200390117, 200390118}; //10º floor
    private static final Integer[] MOBS010 = {200390120};
    private static final Integer[] MOBS0010 = {2009016};
    private static final Integer[] MOBS00010 = {2009017};

    //TOI 11 MOBS
    private static final Integer[] MOBS11 = {200390117, 200390118, 200390121, 200390122, 200390123, 200390124}; //11º floor
    private static final Integer[] MOBS011 = {200390125}; //11º floor

    //TOI 12 MOBS
    private static final Integer[] MOBS12 = {200390126}; //12º floor
    private static final Integer[] MOBS012 = {2009018};
    private static final Integer[] MOBS0012 = {2009020};

    //Raid Bosses
    private static final Integer[] BOSSES = {93848,93850}; //13º floor

    //Baium
    private static final Integer[] GRAND_BOSSES = {94086}; //14º floor

    private static final int REWARD_ITEM_COUNT = 50;
    private static final int REWARD_ITEM_ID = 49002;
    /*
       HALFDAY = 0;
       ONEDAY = 1;
       TWODAYS = 2;
       HALFWEEK = 3;
       WEEK = 4;
       */
    private static int instanceRefresh = WEEK;

    private class teleCoord {
        int instanceId;
        int x;
        int y;
        int z;
    }

    public class TOIWorld extends InstanceWorld {
        private int stage = 0;
        private int liveMobs = 0;

        public void incStage() {
            stage++;
        }

        public int getStage() {
            return stage;
        }

        public void incLiveMobs() {
            liveMobs++;
        }

        public void decLiveMobs() {
            liveMobs--;

            if (liveMobs < 0) {
                _log.warn("Tower of Insolence declive mobs went into negatives ");
            }
        }

        public int getLiveMobs() {
            return liveMobs;
        }

        public TOIWorld() {
        }
    }

    public TOI(int questId, String name, String descr) {
        super(TOI.class.getSimpleName());
        addStartNpc(MALEFICENT);
        addTalkId(MALEFICENT);
        addTalkId(TELEPORTER);
        addTalkId(TELEPORTER2);
        addTalkId(GK);
        Set<Integer> mergedIds = mergeIds(MOBS, MOBS01, MOBS2, MOBS02, MOBS3, MOBS03, MOBS4, MOBS5, MOBS05, MOBS005, MOBS6, MOBS06, MOBS7, MOBS8, MOBS08, MOBS9, MOBS09, MOBS009, MOBS10, MOBS010, MOBS0010, MOBS00010, MOBS11, MOBS011, MOBS12, MOBS012, MOBS0012, BOSSES, GRAND_BOSSES);
        for (Integer mergedId : mergedIds) {
            addKillId(mergedId);
        }

    }

    public static void main(String[] args) {
        new TOI(-1, qn, "instances");
        System.out.println("Gabriel TOI loaded with success!");
    }

    private boolean checkConditions(L2PcInstance player, boolean single) {
        if (debug || player.isGM())
            return true;
        else {
            final L2Party party = player.getParty();
//
//            if (!single && (party == null || party.getMemberCount() < 8 || party.getMemberCount() > 9)) {
//                player.sendMessage("This is a 8-9 player party instance, so you must have a party of 8-9 people");
//                return false;
//            }
//            if (!single && party.getLeaderObjectId() != player.getObjectId()) {
//                player.sendPacket(SystemMessageId.ONLY_PARTY_LEADER_CAN_ENTER);
//                return false;
//            }

            if(party != null){
                player.sendMessage("You cannot enter with party!");
                return false;
            }
//            Map<Integer, Long> instanceTimes;

//            if (!single) {
//                if (!checkIPs(party))
//                    return false;
//
//                boolean canEnter = true;
//
//                for (L2PcInstance ptm : party.getMembers()) {
//                    if (ptm == null) return false;
//                    instanceTimes = InstanceManager.getInstance().getAllInstanceTimes(ptm.getObjectId());
//                    final String instanceName = InstanceManager.getInstance().getInstanceIdName(INSTANCEID);
//
//                    if (ptm.getLevel() < levelReq) {
//                        ptm.sendMessage("You must be level " + levelReq + " to enter this instance");
//                        canEnter = false;
//                    } else if (ptm.getPvpKills() < pvpReq) {
//                        ptm.sendMessage("You must have " + pvpReq + " PvPs to enter this instance");
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
//                    else if (System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(ptm.getObjectId(), INSTANCEID)) {
//                        ptm.sendMessage("You can only enter this instance once a week. Wait until next Sunday.");
//                        canEnter = false;
//                    } else if (ptm.isInDuel() || ptm.isInOlympiadMode() || OlympiadManager.getInstance().isRegistered(ptm)) {
//                        ptm.sendMessage("You can't enter the instance while in duel/oly");
//                        canEnter = false;
//                    } else if (instanceTimes != null) {
//                        for (int id : instanceTimes.keySet()) {
//                            if (!instanceName.equals(InstanceManager.getInstance().getInstanceIdName(id))) {
//                                continue;
//                            }
//                            // if found instance still can't be reentered - exit
//                            if (System.currentTimeMillis() < instanceTimes.get(id)) {
//                                SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.C1_MAY_NOT_RE_ENTER_YET);
//                                sm.addPcName(ptm);
//                                player.sendPacket(sm);
//                                canEnter = false;
//                            }
//                        }
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
                } else if (player.getPvpKills() < pvpReq) {
                    player.sendMessage("You must have " + pvpReq + " PvPs to enter this instance");
                    return false;
                }
                else if (player.isInDuel() || player.isInOlympiadMode() || OlympiadManager.getInstance().isRegistered(player)) {
                    player.sendMessage("You can't enter the instance while in duel/oly");
                    return false;
                } else if (System.currentTimeMillis() < InstanceManager.getInstance().getInstanceTime(player.getObjectId(), INSTANCEID)) {
                    player.sendMessage("You can enter this instance every 2 times a week. Wait until next Sunday.");
                    return false;
                } else if (player.getFame() < fameReq) {
                    player.sendMessage("You must have " + fameReq + " fame to enter this instance");
                    return false;
                } else if (player.getPvpFlag() != 0 || player.getKarma() > 0) {
                    player.sendMessage("You can't enter the instance while in PVP mode or have karma");
                    return false;
                }else if(SunriseEvents.isRegistered(player) || SunriseEvents.isInEvent(player)){
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
//
            return true;
        }
    }

    private void teleportplayer(L2PcInstance player, teleCoord teleto) {
        teleportPlayer(player, new Location(teleto.x, teleto.y, teleto.z, 0), teleto.instanceId);
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

        instanceId = InstanceManager.getInstance().createDynamicInstance(template);
        world = new TOIWorld();
        world.setInstanceId(instanceId);
        world.setTemplateId(INSTANCEID);

        InstanceManager.getInstance().addWorld(world);
//            _log.info("Tower of Insolence: new " + template + " Instance: " + instanceId + " created by player: " + player.getName());

        final L2Party party = player.getParty();


        // teleport players
        teleto.instanceId = instanceId;
        InstanceManager.getInstance().setInstanceTime(player.getObjectId(), INSTANCEID, getNextInstanceTime(instanceRefresh));
        world.addAllowed(player.getObjectId());
        teleportplayer(player, teleto);


        spawn1stMobs((TOIWorld) world, player);
        showWindows((TOIWorld) world, player);
        GKspawn((TOIWorld) world, player);
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

    @Override
    public String onTalk(L2Npc npc, L2PcInstance player) {
        final int npcId = npc.getId();

        QuestState st = player.getQuestState(qn);

        if (st == null)
            st = newQuestState(player);

        if (npcId == MALEFICENT) {
            //Change Here Instance spawn location.
            teleCoord teleto = new teleCoord();
            teleto.x = ENTRANCE_X;
            teleto.y = ENTRANCE_Y;
            teleto.z = ENTRANCE_Z;
            enterInstance(player, "TOI.xml", teleto);
        } else if (npcId == GK) {
            final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

            if (world == null || !(world instanceof TOIWorld))
                return null;

            teleCoord teleto = new teleCoord();
            teleto.x = player.getX();
            teleto.y = player.getY();
            teleto.z = player.getZ();

            if (player.getParty() == null) {
                startInstance(player, teleto);
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    startInstance(ptm, teleto);
                }
            }

        } else if (npcId == TELEPORTER) {
            final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

            if (world == null || !(world instanceof TOIWorld))
                return null;

            final L2Party party = player.getParty();

            final TOIWorld toiworld = (TOIWorld) world;

            if (toiworld.getStage() == 14) {
                if (party != null) {
                    for (L2PcInstance ptm : party.getMembers()) {
                        if (ptm == null) continue;
                        ptm.teleToLocation(113174, 14595, 10072, false);
                        ptm.sendPacket(new ExShowScreenMessage("The Boss of Tower of Insolence has Appeared!", 8000));
                    }
                } else {
                    player.teleToLocation(113174, 14595, 10072, false);
                    player.sendPacket(new ExShowScreenMessage("The Boss of Tower of Insolence has Appeared!", 8000));
                }
                /*npc.deleteMe();*/
            } else if (toiworld.getStage() == 15) {
                teleCoord teleto = new teleCoord();

                teleto.x = X_EXIT_LOC;
                teleto.y = Y_EXIT_LOC;
                teleto.z = Z_EXIT_LOC;

                if (player.getParty() == null) {
                    exitInstance(player, teleto);
                    player.sendPacket(new ExShowScreenMessage("You have completed the Tower of Insolence instance", 8000));
                } else {
                    for (L2PcInstance ptm : player.getParty().getMembers()) {
                        exitInstance(ptm, teleto);
                        ptm.sendPacket(new ExShowScreenMessage("You have completed the Tower of Insolence instance", 8000));
                    }
                }

                st.exitQuest(true);
            } else {
                _log.warn("Tower of Insolence stage is fucked up!");
            }
        } else if (npcId == TELEPORTER2) {
            final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(player);

            if (world == null || !(world instanceof TOIWorld))
                return null;

            final TOIWorld toiworld = (TOIWorld) world;

            if (toiworld.getStage() == 15) {
                teleCoord teleto = new teleCoord();
                teleto.x = X_EXIT_LOC;
                teleto.y = Y_EXIT_LOC;
                teleto.z = Z_EXIT_LOC;

                if (player.getParty() == null) {
                    exitInstance(player, teleto);
                    player.sendPacket(new ExShowScreenMessage("You have completed the Tower of Insolence instance", 8000));
                } else {
                    for (L2PcInstance ptm : player.getParty().getMembers()) {
                        exitInstance(ptm, teleto);
                        ptm.sendPacket(new ExShowScreenMessage("You have completed the Tower of Insolence instance", 8000));
                    }
                }
            } else {
                _log.warn("Tower of Insolence stage is fucked up!");
            }

            st.exitQuest(true);
        }

        return null;
    }

    private void sendRemainingMobs(TOIWorld dvcWorld, int instanceid){
        Instance instance = InstanceManager.getInstance().getInstance(instanceid);
        int alive = dvcWorld.liveMobs;

        if(alive <= 0)
            return;
        for (Integer playerobjId : instance.getPlayers()) {
            L2PcInstance player = L2World.getInstance().getPlayer(playerobjId);
            player.sendPacket(new ExShowScreenMessage(1, -1, ExShowScreenMessage.BOTTOM_RIGHT, 0, 1, 0, 0, false, 5000, false, "Kill " + alive + " monsters to next stage!"));

        }
    }

    @Override
    public String onKill(L2Npc npc, L2PcInstance killer, boolean isPet) {
        final InstanceWorld world = InstanceManager.getInstance().getPlayerWorld(killer);

        if (world == null || !(world instanceof TOIWorld))
            return null;

        TOIWorld toiworld = (TOIWorld) world;

        toiworld.decLiveMobs();
        sendRemainingMobs(toiworld, toiworld.getInstanceId());
        if (toiworld.getLiveMobs() <= 0) {
            toiworld.liveMobs = 0;

            for (int id : GRAND_BOSSES) {
                if (id == npc.getId()) {
                    toiworld.incStage();
                    rewardItem(toiworld, killer);
                    annunceToAll(npc, killer);
                    addSpawn(TELEPORTER2, 115210, 16622, 10072, 0, false, 0,false, world.getInstanceId(), true);

                    int instanceId = InstanceManager.getInstance().getPlayerInstance(killer.getObjectId());
                    Instance instance = InstanceManager.getInstance().getInstance(instanceId);

                    instance.setDuration(EXIT_TIME * 60000);
                    instance.setEmptyDestroyTime(0);
                    savePlayerReenter(toiworld);

                    return null;
                }
            }

            final int stage = toiworld.getStage();

            switch (stage) {
                case 0:
                    spawn1stMobs(toiworld, killer);
                    break;
                case 1:
                    spawn2ndMobs(toiworld, killer);
                    break;
                case 2:
                    spawn3rdMobs(toiworld, killer);
                    break;
                case 3:
                    spawn4rdMobs(toiworld, killer);
                    break;
                case 4:
                    spawn5thMobs(toiworld, killer);
                    break;
                case 5:
                    spawn6thMobs(toiworld, killer);
                    break;
                case 6:
                    spawn7thMobs(toiworld, killer);
                    break;
                case 7:
                    spawn8thMobs(toiworld, killer);
                    break;
                case 8:
                    spawn9thMobs(toiworld, killer);
                    break;
                case 9:
                    spawn10thMobs(toiworld, killer);
                    break;
                case 10:
                    spawn11thMobs(toiworld, killer);
                    break;
                case 11:
                    spawn12thMobs(toiworld, killer);
                    break;
                case 12:
                    spawnSubBoss(toiworld, killer);
                    break;
                case 13:
                    spawnGrandBoss(toiworld, killer);
                    spawnGK(toiworld, killer);
                    //spawnBAAL(toiworld, killer);
                    break;
            }


        }

        return null;
    }

    public void spawnGK(TOIWorld world, L2PcInstance player) {
        addSpawn(TELEPORTER, 112754, 14190, 10072, 0, false, 0,false, world.getInstanceId(), true);
        if (player.getParty() == null) {
            player.sendPacket(new ExShowScreenMessage("Go to last mission", 8000));
        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                ptm.sendPacket(new ExShowScreenMessage("Go to last mission", 8000));
            }
        }
    }

    public void spawnBAAL(TOIWorld world, L2PcInstance player) {
        addSpawn(BAAL, 113561, 14997, 9560, 0, false, 0,false, world.getInstanceId(), true);
    }

    public void spawn1stMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 0) // primeiro floor
        {
            addSpawn(94084,114633,15342,-5096, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,113243,14854,-5096, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,115986,17342,-5096, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,113267,17402,-5096, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,113441,14776,-5096, 0, false, 0,false, world.getInstanceId(), true);

            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();

            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("First mission, kill all monsters on the 1st floor.", 8000));
                player.sendMessage("Kill all monsters on the 1st floor.");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("First mission, kill all monsters on the 1st floor.", 8000));
                    ptm.sendMessage("Kill all monsters on the 1st floor.");
                }
            }
        }

    }

    public void spawn2ndMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 1) //segundo floor
        {
            addSpawn(94084,113335,15026,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,113459,17077,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,115849,17204,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,115709,14912,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,114405,15650,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94084,114652,16554,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,112773,16005,-3608, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,116553,16079,-3608, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 2nd floor started...", 8000));
                player.sendMessage("Kill all monsters on the 2nd floor.");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 2nd floor started...", 8000));
                    ptm.sendMessage("Kill all monsters on the 2nd floor.");
                }
            }
        }
    }

    public void spawn3rdMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 2) //terceiro floor
        {
            addSpawn(94084,113271,15017,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,113756,14240,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,115609,14424,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,116172,15179,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,116025,16955,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94084,115725,17758,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,114576,18096,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,114004,16511,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,114614,15939,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,115332,16347,-2120, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94084,116339,16009,-2120, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
             world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 3rd floor started...", 8000));
                player.sendMessage("Kill all monsters on the 3rd floor.");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 3rd floor started...", 8000));
                    ptm.sendMessage("Kill all monsters on the 3rd floor.");
                }
            }
        }
    }

    public void noRandomWalkAllMobs(){
    }

    public void spawn4rdMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 3) //quarto floor
        {
            addSpawn(94084,114788,17404,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,114500,17014,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,114006,17608,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,113632,17702,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,113006,17162,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94084,113805,16143,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,112735,16114,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,113138,15084,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,113962,14703,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,114590,14215,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94084,114705,15369,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,115524,14506,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94083,115502,15974,-640, 0, false, 0,false, world.getInstanceId(), true);
            addSpawn(94085,116056,17515,-640, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 4th floor started...", 8000));
                player.sendMessage("Kill all monsters on the 4th floor.");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 4th floor started...", 8000));
                    ptm.sendMessage("Kill all monsters on the 4th floor.");
                }
            }
        }
    }

    public void spawn5thMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 4) //quinto floor
        {
            addSpawn(94084, 116650, 17034, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 117138, 15794, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116443, 14291, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115350, 14026, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112402, 16631, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115337, 14672, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115557, 16097, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115762, 17081, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116563, 17631, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115303, 18133, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114090, 18643, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113085, 17604, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113431, 16670, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113012,15706,928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113965, 14628, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114639, 17019, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114684, 15467, 928, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();

            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 5th floor started...", 8000));
                player.sendMessage("Kill all monsters on the 5th floor.");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 5th floor started...", 8000));
                    ptm.sendMessage("Kill all monsters on the 5th floor.");
                }
            }
        }
    }

    public void spawn6thMobs(TOIWorld world, L2PcInstance player) //aqui tem que por mobs fortes
    {
        if (world.getStage() == 5) //sexto floor
        {
            addSpawn(94084, 116143, 17577, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115961, 18330, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113607, 18439, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113289, 17810, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112267, 17019, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 112572, 15182, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113484, 16230, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113023, 15238, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113727, 14763, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114630, 14479, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115080, 15121, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115809, 14783, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114798, 16191, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114603, 17591, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115912, 17197, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115926, 15947, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116986, 17023, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114515, 13628, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112977, 14369, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113507, 17242, 1944, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 6th floor started...", 8000));
                player.sendMessage("Next mission 6th floor started...");

            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 6th floor started...", 8000));
                    ptm.sendMessage("Next mission 6th floor started...");
                }
            }
        }
    }

    public void spawn7thMobs(TOIWorld world, L2PcInstance player) //boss
    {
        if (world.getStage() == 6) //7º floor
        {
            addSpawn(94084, 113661, 13942, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115615, 13759, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 117134, 15613, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116330,17731,2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113606, 18398, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115970, 17000, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114999, 17444, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113810, 17181, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112979, 17690, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112169, 16042, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 112583, 15211, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112979, 14041, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113729, 14910, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115320, 14694, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116223, 15517, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114605, 16400, 2992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113963, 16101, 2992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114213,15741,2992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115924, 18519, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114626, 18229, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115401, 16925, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114758, 14408, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113213, 14400, 2952, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 7th floor (Medium boss)", 8000));
                player.sendMessage("Next mission 7th floor (Medium boss)");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 7th floor (Medium boss)", 8000));
                    ptm.sendMessage("Next mission 7th floor (Medium boss)");
                }
            }
        }
    }

    public void spawn8thMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 7) //8º floor
        {
            addSpawn(94084, 112667, 14766, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113855, 13978, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112197, 16332, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112270, 17345, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114642, 18262, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113530, 17478, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114894, 17677, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115395, 16907, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116165, 16881, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115718,15858,3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115915, 15206, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114603, 16353, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114814, 15618, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113967, 15293, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114261, 14574, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115121, 14904, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115532, 13997, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114560, 13935, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116423, 14296, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116707, 15380, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 117200, 16074, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116678, 16945, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116177, 18042, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115604,18339,3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112999, 15677, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 117092, 15139, 3960, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 8th floor started...", 8000));
                player.sendMessage("Next mission 8th floor started...");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 8th floor started...", 8000));
                    ptm.sendMessage("Next mission 8th floor started...");
                }
            }
        }
    }

    public void spawn9thMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 8) //9º floor
        {
            addSpawn(94084, 116812, 17727, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115291, 18555, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114534, 18265, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113417, 18252, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112958, 17472, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114426, 17698, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114881, 14638, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113871, 14119, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112826, 14019, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112845, 15083, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 112023, 15502, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112649, 16850, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114771, 13793, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115603, 13700, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115951, 14270, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 117015, 14840, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116741, 15798, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 117090, 16605, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116631, 16922, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115334, 14672, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115744, 15679, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116059, 16339, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115423, 17009, 4992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115153, 17439, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114679, 17447, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113746, 17191, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114099, 16275, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114650, 15366, 4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115291,16120,4976, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 9th floor started...", 8000));
                player.sendMessage("Next mission 9th floor started...");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 9th floor started...", 8000));
                    ptm.sendMessage("Next mission 9th floor started...");
                }
            }
        }
    }

    public void spawn10thMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 9) //10º floor
        {
            addSpawn(94084, 112635, 15116, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112091, 15571, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112435, 16522, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112414, 17374, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113661, 18331, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115125, 18518, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116363, 17562, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 117020, 16785, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116646, 15142, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116481, 14146, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115235, 13965, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114364, 13563, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113407, 13894, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113050, 14407, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113681, 15010, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114057,15522,5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115486, 15464, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114418,15114,5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113791, 14507, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115751, 15062, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 116092, 15938, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115954, 16669, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115335, 16454, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114863, 15967, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115113,16873,5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114036, 16915, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113827,16423,5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113612, 15661, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113144, 15353, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113253, 16429, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113501, 16963, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114999, 17475, 5984, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();

            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 10th floor started...", 8000));
                player.sendMessage("Next mission 10th floor started...");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 10th floor started...", 8000));
                    ptm.sendMessage("Next mission 10th floor started...");
                }
            }
        }
    }

    public void spawn11thMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 10) //11º floor
        {
            addSpawn(94084, 115100, 18252, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115463, 18115, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116301,17578,6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116595, 16873, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116875, 16423, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 116022, 16736, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115216, 17238, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115028, 17783, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114448, 17351, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114011, 17883, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115946, 15913, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116608, 15208, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116694, 15813, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114381, 14333, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115422, 14129, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114818, 14776, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114174, 13840, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112552, 15432, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112339, 14620, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 111943, 16455, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113393, 13700, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114349, 13355, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115092, 13370, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113719, 14710, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113776, 15358, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114408, 15486, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114139, 16489, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114700, 16026, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115133, 15383, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115794, 15121, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115918, 14452, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113324, 17071, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113330, 17510, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114744, 16789, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115452, 16535, 6992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 11th floor started...", 8000));
                player.sendMessage("Next mission 11th floor started...");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 11th floor started...", 8000));
                    ptm.sendMessage("Next mission 11th floor started...");
                }
            }
        }
    }

    public void spawn12thMobs(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 11) //12º floor
        {
            addSpawn(94084, 116823, 16505, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 116292, 17561, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115595, 18025, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114620, 18321, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113759, 18129, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 112974, 17549, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112510, 16756, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 112471, 15883, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 112559, 15207, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113142, 14349, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113836, 14014, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114441, 13870, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115184, 13902, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115892, 14186, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116374, 14622, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 116784, 15553, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115345, 16471, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115645, 15731, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115124,15161,7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114222, 15113, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113935, 14469, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114614, 14576, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115295, 14845, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115889, 15241, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 116118, 15701, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 115996, 16788, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114037, 15471, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114733, 15873, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 114509, 16389, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 115175, 16881, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 114229, 17055, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113784,16339,7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 113876, 15772, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113061, 15149, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94085, 113172, 16214, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94084, 113388, 16819, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 115200, 17368, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            addSpawn(94083, 114180, 17461, 7992, 0, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
            if (player.getParty() == null) {
                player.sendPacket(new ExShowScreenMessage("Next mission 12th floor started...", 8000));
                player.sendMessage("Next mission 12th floor started...");
            } else {
                for (L2PcInstance ptm : player.getParty().getMembers()) {
                    ptm.sendPacket(new ExShowScreenMessage("Next mission 12th floor started...", 8000));
                    ptm.sendMessage("Next mission 12th floor started...");
                }
            }
        }
    }

    public void spawnSubBoss(TOIWorld world, L2PcInstance player) {
        if (world.getStage() == 12) {
            addSpawn(93848, 115323,16739,9000, 7593, true, 0,false, world.getInstanceId(), true);
            addSpawn(93850, 113652,15039,9560, 6839, true, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incLiveMobs();
            world.incStage();
        }
        if (player.getParty() == null) {
            player.sendPacket(new ExShowScreenMessage("Next mission 13th floor, Raid Boss has born!", 8000));
            player.sendMessage("Next mission 13th floor, Raid Boss has born!");

        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                ptm.sendPacket(new ExShowScreenMessage("Next mission 13th floor, Raid Boss has born!", 8000));
                ptm.sendMessage("Next mission 13th floor, Raid Boss has born!");
            }
        }
    }

    public void spawnGrandBoss(TOIWorld world, L2PcInstance player) {
        if (world.getStage() >= 13) {
            addSpawn(94086, 115196,16620,10080, 40789, false, 0,false, world.getInstanceId(), true);
            world.incLiveMobs();
            world.incStage();
        } else {
            _log.warn("Tower of Insolence spawning grand boss w/o stage being >= 13");
        }
        if (player.getParty() == null) {
            player.sendPacket(new ExShowScreenMessage("Go up to the Great Boss's room!", 8000));
            player.sendMessage("Go up to the Great Boss's room!");

        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                ptm.sendPacket(new ExShowScreenMessage("Go up to the Great Boss's room!", 8000));
                ptm.sendMessage("Go up to the Great Boss's room!");
            }
        }
    }

    public void annunceToAll(L2Npc npc, L2PcInstance player) {
//        Broadcast.toAllOnlinePlayers("The leader " + player.getName() + " and your party killed the Great Boss " + npc.getName() + " of Tower of Insolence Instance.");
    }

    public void showWindows(TOIWorld world, L2PcInstance player) {

        if (player.getParty() == null) {
            String filename = HtmCache.getInstance().getHtm(player, "data/html/instance/insolence/insolence.htm");
            NpcHtmlMessage itemReply = new NpcHtmlMessage(1);
            itemReply.setHtml(filename);
            player.sendPacket(itemReply);
        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                String filename = HtmCache.getInstance().getHtm(ptm, "data/html/instance/insolence/insolence.htm");
                NpcHtmlMessage itemReply = new NpcHtmlMessage(1);
                itemReply.setHtml(filename);
                ptm.sendPacket(itemReply);
            }
        }
    }

    public void rewardItem(TOIWorld world, L2PcInstance player) {
        if (player.getParty() == null) {
            player.addItem("TOI Instance: ", REWARD_ITEM_ID, REWARD_ITEM_COUNT, player, true);
        } else {
            for (L2PcInstance ptm : player.getParty().getMembers()) {
                ptm.addItem("TOI Instance: ", REWARD_ITEM_ID, REWARD_ITEM_COUNT, player, true);
            }
        }
    }

    public void GKspawn(TOIWorld world, L2PcInstance player) {
        addSpawn(GK, 119380, 16269, -5120, 0, false, 0,false, world.getInstanceId(), true);
    }

    @Override
    protected void onEnterInstance(L2PcInstance player, InstanceWorld world, boolean firstEntrance) {

    }

    private void savePlayerReenter(InstanceWorld world) {
//        if (world instanceof DVC.DragonValeyWorld)
//        {
//            Calendar reenter = Calendar.getInstance();
//            reenter.set(Calendar.MINUTE, RESET_MIN);
//            // if time is >= RESET_HOUR - roll to the next day
//            if (reenter.get(Calendar.HOUR_OF_DAY) >= RESET_HOUR)
//            {
//                reenter.add(Calendar.DATE, 1);
//            }
//            reenter.set(Calendar.HOUR_OF_DAY, RESET_HOUR);
//
//            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.INSTANT_ZONE_FROM_HERE_S1_S_ENTRY_HAS_BEEN_RESTRICTED);
//            sm.addInstanceName(world.getTemplateId());
//
//            // set instance reenter time for all allowed players
//            for (int objectId : world.getAllowed())
//            {
//                L2PcInstance obj = L2World.getInstance().getPlayer(objectId);
//                if ((obj != null) && obj.isOnline())
//                {
//                    InstanceManager.getInstance().setInstanceTime(objectId, world.getTemplateId(), reenter.getTimeInMillis());
//                    obj.sendPacket(sm);
//                }
//            }
//
//            // destroy instance after EXIT_TIME
//            Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
//            inst.setDuration(EXIT_TIME * 60000);
//            inst.setEmptyDestroyTime(0);
//        }
    }

    /**
     * Called on instance finish and handles reenter time for instance
     *
     * @param world instanceWorld
     */
    @Override
    protected final void finishInstance(InstanceWorld world) {
//        if (world instanceof TOIWorld)
//        {
//            Calendar reenter = Calendar.getInstance();
//            reenter.set(Calendar.MINUTE, RESET_MIN);
//            // if time is >= RESET_HOUR - roll to the next day
//            if (reenter.get(Calendar.HOUR_OF_DAY) >= RESET_HOUR)
//            {
//                reenter.add(Calendar.DATE, 1);
//            }
//            reenter.set(Calendar.HOUR_OF_DAY, RESET_HOUR);
//
//            SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.INSTANT_ZONE_FROM_HERE_S1_S_ENTRY_HAS_BEEN_RESTRICTED);
//            sm.addInstanceName(world.getTemplateId());
//
//            // set instance reenter time for all allowed players
//            for (int objectId : world.getAllowed())
//            {
//                L2PcInstance obj = L2World.getInstance().getPlayer(objectId);
//                if ((obj != null) && obj.isOnline())
//                {
//                    InstanceManager.getInstance().setInstanceTime(objectId, world.getTemplateId(), reenter.getTimeInMillis());
//                    obj.sendPacket(sm);
//                }
//            }
//
//            // destroy instance after EXIT_TIME
//            Instance inst = InstanceManager.getInstance().getInstance(world.getInstanceId());
//            inst.setDuration(EXIT_TIME * 60000);
//            inst.setEmptyDestroyTime(0);
//        }
    }
}