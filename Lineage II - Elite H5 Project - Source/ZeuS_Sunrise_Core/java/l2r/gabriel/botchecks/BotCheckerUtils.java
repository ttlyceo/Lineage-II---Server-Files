package l2r.gabriel.botchecks;

import ZeuS.procedimientos.opera;
import gr.sr.interf.SunriseEvents;
import l2r.gameserver.enums.PrivateStoreType;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class BotCheckerUtils {

    public static boolean isOk(L2PcInstance player){

        if(player == null)
            return false;


        if(player.isDead() || player.isAlikeDead())
            return false;

        if(player.isInOfflineMode())
            return false;

        if(!player.isInCombat())
            return false;

        if(player.isInsideZone(ZoneIdType.PEACE) || player.isInsideZone(ZoneIdType.TOWN))
            return false;

        if(player.getPrivateStoreType() != PrivateStoreType.NONE)
            return false;

        if(player.isSitting())
            return false;

        if(SunriseEvents.isInEvent(player) || SunriseEvents.isInMainEvent(player))
            return false;

        if(player.isOlympiadStart() || player.isInOlympiadMode() )
            return false;

        if(player.isGM())
            return false;

        if(opera.isMaster(player))
            return false;

        if(player.isPhantom())
            return false;

        return true;
    }
}
