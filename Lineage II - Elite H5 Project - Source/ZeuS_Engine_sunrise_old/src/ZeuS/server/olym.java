package ZeuS.server;

import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.interfase.central;

import l2r.gameserver.GameServer;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.network.clientpackets.Say2;
import l2r.gameserver.network.serverpackets.CreatureSay;

public class olym {
	private static olym _instance;

	private static final Logger _log = Logger.getLogger(olym.class.getName());
	public static void _Anun_oponent(L2PcInstance _playerOne, L2PcInstance _playerTwo){
		String TitleMensajeClase = "[Opp. Class]";
		String TitleMensajeNom = "[Opp. Name]";
		if(!general._activated()){
			return;
		}
		if(!general.ANNOUCE_CLASS_OPONENT_OLY && !general.OLY_ANTIFEED_SHOW_NAME_OPPO){
			return;
		}
		CreatureSay cs = null;

		String C_O_1 = central.getClassName(_playerOne, _playerOne.getBaseClass());
		String C_O_2 = central.getClassName(_playerOne, _playerTwo.getBaseClass());

		try{
			if(general.ANNOUCE_CLASS_OPONENT_OLY){
				cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, TitleMensajeClase, C_O_2);
				_playerOne.sendPacket(cs);
			}
			if(general.OLY_ANTIFEED_SHOW_NAME_OPPO){
				cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, TitleMensajeNom, _playerTwo.getName());
				_playerOne.sendPacket(cs);
			}
		}catch(Exception a){
			_log.warning("ANNOUCEMENTE OPONENTE->Error anunciar el Oponente del Player" + String.valueOf(_playerTwo.getObjectId()));
		}

		try{
			if (general.ANNOUCE_CLASS_OPONENT_OLY){
				cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, TitleMensajeClase, C_O_1);
				_playerTwo.sendPacket(cs);
			}

			if(general.OLY_ANTIFEED_SHOW_NAME_OPPO){
				cs = new CreatureSay(0, Say2.PARTYROOM_COMMANDER, TitleMensajeNom, _playerOne.getName());
				_playerTwo.sendPacket(cs);
			}
		}catch(Exception a){
			_log.warning("ANNOUCEMENTE OPONENTE->Error anunciar el Oponente del Player" + String.valueOf(_playerOne.getObjectId()));
		}
	}
	public static final olym getInstance()
	{
		return _instance;
	}

}
