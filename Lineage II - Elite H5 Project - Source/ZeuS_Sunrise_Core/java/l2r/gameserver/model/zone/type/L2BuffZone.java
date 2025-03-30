package l2r.gameserver.model.zone.type;

import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.zone.L2ZoneType;

public class L2BuffZone extends L2ZoneType {

	protected L2BuffZone(int id) {
		super(id);
	}

	@Override
	protected void onEnter(L2Character character) {
		character.setInsideZone(ZoneIdType.BUFF, true);
	}

	@Override
	protected void onExit(L2Character character) {
		character.setInsideZone(ZoneIdType.BUFF, false);
	}

}
