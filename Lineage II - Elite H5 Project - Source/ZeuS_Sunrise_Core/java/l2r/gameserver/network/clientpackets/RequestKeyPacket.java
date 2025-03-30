package l2r.gameserver.network.clientpackets;

import Interface.impl.KeyPacket;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class RequestKeyPacket extends L2GameClientPacket {
	private static final String _C__D0_83_10_REQUESTKEYPACKET = "[C] D0:83:10 RequestKeyPacket";
	byte[] data = null;
	int data_size;
	
	@Override
	protected void readImpl() {
		if(_buf.remaining() > 2) {
			data_size = readH();
			if(_buf.remaining() >= data_size) {
				data = new byte[data_size];
				readB(data);
			}
		}
	}

	@Override
	protected void runImpl() {
		L2PcInstance activeChar = getClient().getActiveChar();

		if(activeChar == null)
			return;
		activeChar.sendPacket(new KeyPacket().sendKey(data, data_size));
	}

	@Override
	public String getType()
	{
		return _C__D0_83_10_REQUESTKEYPACKET;
	}
}