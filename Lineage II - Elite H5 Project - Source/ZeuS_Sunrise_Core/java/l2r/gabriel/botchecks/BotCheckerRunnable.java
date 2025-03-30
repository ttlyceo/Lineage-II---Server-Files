package l2r.gabriel.botchecks;

import ZeuS.interfase.antibotSystem;
import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.util.Rnd;

public class BotCheckerRunnable implements Runnable {

    private L2PcInstance player;
    private int minMinutes = 30;
    private int maxMinutes = 90;

    public BotCheckerRunnable(L2PcInstance player) {
        this.player = player;
    }

    @Override
    public void run() {
        int delay = Rnd.get(minMinutes*60*1000, maxMinutes*60*1000);

        if(player == null)
            return;

        if(player.isPhantom())
            return;

        if(player.isInOfflineMode())
            return;


        ThreadPoolManager.getInstance().scheduleGeneral(() -> {
            if(BotCheckerUtils.isOk(player))
                antibotSystem.setCheckBot(player, "System", 0);
        }, delay);

        ThreadPoolManager.getInstance().scheduleGeneral(this, Rnd.get(minMinutes*60*1000, maxMinutes*60*1000));

    }






}
