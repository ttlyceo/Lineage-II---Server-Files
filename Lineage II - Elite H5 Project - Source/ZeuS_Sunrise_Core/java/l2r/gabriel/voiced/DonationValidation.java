package l2r.gabriel.voiced;

import ZeuS.Comunidad.EngineForm.v_donation;
import l2r.gameserver.handler.IVoicedCommandHandler;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class DonationValidation implements IVoicedCommandHandler {

    private static final String[] VOICED_COMMANDS =
            {
                    "donate",
            };

    @Override
    public boolean useVoicedCommand(String command, L2PcInstance activeChar, String target)
    {
        if (command.startsWith("donate"))
        {
            v_donation.checkDonationPoint(activeChar);
        }
        return true;
    }


    @Override
    public String[] getVoicedCommandList()
    {
        return VOICED_COMMANDS;
    }
}
