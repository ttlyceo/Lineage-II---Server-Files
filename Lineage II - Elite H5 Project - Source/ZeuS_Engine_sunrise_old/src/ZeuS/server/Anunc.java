package ZeuS.server;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.logging.Logger;

import l2r.gameserver.model.L2Object;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.actor.instance.L2RaidBossInstance;
import ZeuS.Comunidad.EngineForm.v_RaidBossInfo;
import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class Anunc{
	private static final Logger _log = Logger.getLogger(Anunc.class.getName());
	public static void AnunciarRaidBoss(L2RaidBossInstance raidboss){
		v_RaidBossInfo.setStatusToRB(raidboss.getId(), true, "");
		if(!general.ANNOUCE_RAID_BOS_STATUS || !general._activated()) {
			return;
		}
		String Mensaje = general.RAID_ANNOUCEMENT_LIFE;
		Mensaje = Mensaje.replace("%RAID_NAME%", raidboss.getName());
		opera.AnunciarTodos(general.RAID_ANNOUCEMENT_ID_ANNOUCEMENT, "RaidBoss:", Mensaje);
	}

	public static void AnunciarRaidBoss(L2RaidBossInstance raidboss, long respawnTime){
		final Calendar time = Calendar.getInstance();		
		time.setTimeInMillis(respawnTime);
		String RespawnHora = time.getTime().toString();		
		v_RaidBossInfo.setStatusToRB(raidboss.getId(), false, RespawnHora);		
		if(!general.ANNOUCE_RAID_BOS_STATUS || !general._activated()) {
			return;
		}
		String Mensaje = general.RAID_ANNOUCEMENT_DIED;
		Mensaje = Mensaje.replace("%RAID_NAME%", raidboss.getName());
		Mensaje = Mensaje.replace("%DATE%", RespawnHora);
		opera.AnunciarTodos(general.RAID_ANNOUCEMENT_ID_ANNOUCEMENT, "RaidBoss:", Mensaje);
	}

	public static boolean _anunciarOponente(int Segundos){
		int Encontro = Arrays.binarySearch(general.OLY_SECOND_SHOW_OPPONET, Segundos);
		if(Encontro>=0){
			return true;
		}
		return false;
	}


	public static boolean _AnunciarEnchant(int Enchant){
		if(!general._activated()){
			if((Enchant == 3) || (Enchant == 7)){
				return true;
			}else
			{
				return false;
			}
		}
		int Encontrado = Arrays.binarySearch(general.ENCHANT_ANNOUCEMENT , Enchant);
		if(Encontrado>=0) {
			return true;
		}
		return false;
	}


	public static void _AnunciarCiclosPvP_PK(L2PcInstance player, boolean _pvp){
		if(!general.SPREE_SYSTEM || player.isGM() || !general._activated()){
			return;
		}

		try{
			L2Object targetPlayer = player.getTarget();
			if(targetPlayer!=null){
				if(targetPlayer instanceof L2PcInstance){
					if((general.charPKCOUNT.get(targetPlayer)==0) || (general.charPVPCOUNT.get(targetPlayer)==0)){
						central.msgbox("PvP/Pk points has been reset", (L2PcInstance)targetPlayer);
					}
					general.charPKCOUNT.put((L2PcInstance)targetPlayer, 0);
					general.charPVPCOUNT.put((L2PcInstance)targetPlayer, 0);
				}
			}
		}catch(Exception a){

		}


		if(_pvp){
			if(!general.charPVPCOUNT.containsKey(player)){
				general.charPVPCOUNT.put(player, 1);
			}else{
				general.charPVPCOUNT.put(player, general.charPVPCOUNT.get(player) + 1);
			}
		} else {
			if(!general.charPKCOUNT.containsKey(player)){
				general.charPKCOUNT.put(player, 1);
			}else{
				general.charPKCOUNT.put(player, general.charPKCOUNT.get(player) + 1);
			}
		}

		String Anunciar ="";
		if(_pvp){
			int PVPPoint = general.charPVPCOUNT.get(player);
			if(general.SPREE_PVP_SYSTEM_MESSAGE!=null){
				if(general.SPREE_PVP_SYSTEM_MESSAGE.size()>0){
					if(general.SPREE_PVP_SYSTEM_MESSAGE.containsKey(PVPPoint)){
						Anunciar = general.SPREE_PVP_SYSTEM_MESSAGE.get(PVPPoint);
					}
				}
			}
			if(Anunciar.length()>0){
				Anunciar = Anunciar.replace("%CHAR_NAME%", player.getName()).replace("%CANT%", String.valueOf(PVPPoint));
				opera.AnunciarTodos("PVP Manager", Anunciar);
			}
		}else{
			int PKPoint = general.charPKCOUNT.get(player);
			if(general.SPREE_PK_SYSTEM_MESSAGE!=null){
				if(general.SPREE_PK_SYSTEM_MESSAGE.size()>0){
					if(general.SPREE_PK_SYSTEM_MESSAGE.containsKey(PKPoint)){
						Anunciar = general.SPREE_PK_SYSTEM_MESSAGE.get(PKPoint);
					}
				}
			}

			if(Anunciar.length()>0){
				Anunciar = Anunciar.replace("%CHAR_NAME%", player.getName()).replace("%CANT%", String.valueOf(PKPoint + 1));
				opera.AnunciarTodos("PK Manager", Anunciar);
			}
		}
	}

	public static void Anunciar_All_Char_IP(L2PcInstance player, String Mensaje, String Titulo){
		Anunciar_All_Char_IP(player,Mensaje,Titulo,true);
	}

	public static void Anunciar_All_Char_IP(L2PcInstance player, String Mensaje, String Titulo, boolean SendToMainPlayer){
		Collection<L2PcInstance> players = opera.getAllPlayerOnWorld();
		if(player==null){
			return;
		}
		if(players == null){
			return;
		}
		String ipPlayer = ZeuS.getIp_Wan(player);
		String ipForPlayer = "";
		for(L2PcInstance chars: players){
			if(chars!=null){
				if(!chars.getClient().isDetached()){
					if(chars.isOnline()){
						try{
							ipForPlayer = ZeuS.getIp_Wan(chars);
							if(ipPlayer.equalsIgnoreCase(ipForPlayer)){
								if((chars != player) || ( (chars==player) && SendToMainPlayer)) {
									central.msgbox_Lado(Mensaje, chars, Titulo);
								}
							}
						}catch(Exception a){
							_log.warning("Error sending Menssages to IP->" + ipForPlayer + " from player:" + player.getName() + "("+ chars.getName() +") ->" + a.getMessage());
						}
					}
				}
			}
		}
	}

}
