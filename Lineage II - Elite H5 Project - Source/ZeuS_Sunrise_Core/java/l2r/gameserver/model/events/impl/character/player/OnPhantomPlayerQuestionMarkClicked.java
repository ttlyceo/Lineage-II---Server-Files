package l2r.gameserver.model.events.impl.character.player;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.events.EventType;
import l2r.gameserver.model.events.impl.IBaseEvent;

/**
 * @author Gabriel Costa Souza
 * Discord: Gabriel 'GCS'#2589
 * Skype - email: gabriel_costa25@hotmail.com
 */
public class OnPhantomPlayerQuestionMarkClicked implements IBaseEvent
{
    private final L2PcInstance _activeChar;
    private final int _targetId;

    public OnPhantomPlayerQuestionMarkClicked(L2PcInstance activeChar, int targetId)
    {
        _activeChar = activeChar;
        _targetId = targetId;
    }

    public L2PcInstance getActiveChar()
    {
        return _activeChar;
    }

    public int getTargetId()
    {
        return _targetId;
    }

    @Override
    public EventType getType()
    {
        return EventType.ON_QUESTION_MARK_CLICKED_PHANTOM;
    }
}