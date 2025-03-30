package l2r.gabriel;

import l2r.gabriel.instances.BehemothKing;
import l2r.gabriel.instances.TOI;
import l2r.gabriel.instances.Ultraverse;
import l2r.gabriel.voiced.DonationValidation;
import l2r.gameserver.handler.VoicedCommandHandler;

public class GabInits {

    public GabInits(){

        VoicedCommandHandler.getInstance().registerHandler(new DonationValidation());
        TOI.main(null);
        Ultraverse.main(null);
        new BehemothKing();

    }

    protected static GabInits instance;

    public static GabInits getInstance() {
        if (instance == null)
            instance = new GabInits();
        return instance;
    }
}
