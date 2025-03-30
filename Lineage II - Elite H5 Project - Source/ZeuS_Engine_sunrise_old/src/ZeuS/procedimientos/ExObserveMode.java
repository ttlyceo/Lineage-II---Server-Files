package ZeuS.procedimientos;

import l2r.gameserver.network.serverpackets.L2GameServerPacket;

public class ExObserveMode extends L2GameServerPacket{
	private final int _mode;
	
	/**
	 * @param mode (0 = return, 3 = spectate)
	 */
	public ExObserveMode(int mode)
	{
		_mode = mode;
	}
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0x7C);
		writeC(_mode);
	}
}
