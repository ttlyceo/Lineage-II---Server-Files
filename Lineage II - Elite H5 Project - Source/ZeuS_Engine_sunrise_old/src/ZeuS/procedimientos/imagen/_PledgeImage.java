package ZeuS.procedimientos.imagen;

import l2r.gameserver.network.serverpackets.L2GameServerPacket;

public class _PledgeImage extends L2GameServerPacket
{
	private final int _crestId;
	private final byte[] _data;
	
	public _PledgeImage(int crestId, byte[] data)
	{
		_crestId = crestId;
		_data = data;
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0x6a); // IL client: 0x6c, H5 client: 0x6a
		writeD(_crestId);
		
		if (_data != null)
		{
			writeD(_data.length);
			writeB(_data);
		}
		else
		{
			writeD(0);
		}
	}
}
