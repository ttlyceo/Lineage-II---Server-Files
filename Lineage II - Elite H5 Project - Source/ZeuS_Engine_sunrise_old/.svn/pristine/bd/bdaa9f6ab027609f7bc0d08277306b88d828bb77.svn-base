package ZeuS.interfase;

import java.util.logging.Logger;

import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

import l2r.gameserver.GameServer;
import l2r.gameserver.model.actor.instance.L2PcInstance;

public class pintar {

	private static final Logger _log = Logger.getLogger(pintar.class.getName());

	public static boolean ColorNombre(L2PcInstance st, String Color){
		int outColor = Integer.decode("0x"+Color);
		return ColorNombre(st,outColor);
	}

	public static boolean ColorNombre(L2PcInstance st, int Color){
		boolean Retornar = true;
		try{
			st.getAppearance().setNameColor(Color);
			st.broadcastUserInfo();
		}catch(Exception a){
			_log.warning("Color->" + a.getMessage());
			return false;
		}
		return Retornar;
	}

	public static boolean ColorTitulo(L2PcInstance st, String Color){
		int outColor = Integer.decode("0x"+Color);
		return ColorTitulo(st,outColor);
	}

	public static boolean ColorTitulo(L2PcInstance st, int Color){
		boolean Retornar = true;
		try{
			st.getAppearance().setTitleColor(Color);
			st.broadcastUserInfo();
		}catch(Exception a){
			_log.warning("Color->" + a.getMessage());
			return false;
		}
		return Retornar;
	}



	public static void Colorar(L2PcInstance st, String eventParam1){
		if(eventParam1.equals("1")) {
			;
		}
            st.getAppearance().setNameColor(0x009900);
		if(eventParam1.equals("2")) {
			;
		}
		            st.getAppearance().setNameColor(0xff7f00);
		if(eventParam1.equals("3")) {
			;
		}
		    		st.getAppearance().setNameColor(0x2efe2e);
		if(eventParam1.equals("4")) {
			;
		}
		            st.getAppearance().setNameColor(0x00ffff);
		if(eventParam1.equals("5")) {
			;
		}
		            st.getAppearance().setNameColor(804000);
		if(eventParam1.equals("6")) {
			;
		}
		            st.getAppearance().setNameColor(0x0099ff);
		if(eventParam1.equals("7")) {
			;
		}
		            st.getAppearance().setNameColor(0x70db93);
		if(eventParam1.equals("8")) {
			;
		}
		            st.getAppearance().setNameColor(0x9f9f9f);
		if(eventParam1.equals("9")) {
			;
		}
		            st.getAppearance().setNameColor(0xffff00);
		st.broadcastUserInfo();
		opera.removeItem(general.PINTAR_PRICE, st);
	}
}
