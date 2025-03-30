package l2r.gameserver.model.zone.type;

import java.util.HashMap;
import java.util.Map;

import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.zone.L2ZoneType;

public class L2PvPZoneA extends L2ZoneType
{
	private final Map<String, Integer> Participant = new HashMap<String, Integer>();
	
	protected L2PvPZoneA(int id)
	{
		super(id);
	}
	
	private void addIfNoExist(L2Character player)
	{
		if (player != null)
		{
			if (player instanceof L2PcInstance)
			{
				L2PcInstance temChar = (L2PcInstance) player;
				if (Participant == null)
				{
					Participant.put(temChar.getName(), 0);
				}
				else if ((Participant.size() == 0) || !Participant.containsKey(temChar.getName()))
				{
					Participant.put(temChar.getName(), 0);
				}
			}
		}
	}
	
	@Override
	protected void onEnter(L2Character character)
	{
		addIfNoExist(character);
	}
	
	@Override
	protected void onExit(L2Character character)
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDieInside(L2Character character)
	{
		onExit(character);
	}
	
	@Override
	public void onReviveInside(L2Character character)
	{
		onEnter(character);
	}
	
}