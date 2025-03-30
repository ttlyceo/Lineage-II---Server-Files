package l2r.gameserver.model.actor.instance;

import l2r.gameserver.communitybbs.BoardsManager;
import l2r.gameserver.model.actor.templates.L2NpcTemplate;
import l2r.gameserver.network.serverpackets.ActionFailed;


/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */
public final class L2CommunityBoardInstance extends L2NpcInstance
{

    public L2CommunityBoardInstance(L2NpcTemplate template)
    {
        super(template);
    }

    @Override
    public void onBypassFeedback(L2PcInstance player, String command)
    {

    }

    @Override
    public void showChatWindow(L2PcInstance player)
    {
        if(this.getId() == 55555){ //Donate Manager
            BoardsManager.getInstance().handleCommands(player.getClient(), "bbs_add_fav");
        }

        // Send a Server->Client ActionFailed to the L2PcInstance in order to avoid that the client wait another packet
        player.sendPacket(ActionFailed.STATIC_PACKET);
    }


}