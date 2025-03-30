package ZeuS.interfase;

import gr.sr.interf.SunriseEvents;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import ZeuS.Config.general;
import ZeuS.ZeuS.ZeuS;
import ZeuS.event.RaidBossEvent;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.procedimientos.imagen._ImageGenerator;
import ZeuS.server.comun;
import l2r.Config;
import l2r.gameserver.enums.ZoneIdType;
import l2r.gameserver.model.L2Clan;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.entity.Fort;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;



public class fortress {

	private static Map<String, Integer>IP_REWARDER = new HashMap<String, Integer>();
	private static Map<Integer, Integer> PlayerClose = new HashMap<Integer, Integer>();
	private enum FORTRESS_LOCA {
		
		_101("-63437,159686,-3676","Shanty"),
		_102("-31893,211754,-3657","Souhern"),
		_103("20922,176935,-3575","Hive"),
		_104("125010,118303,-3061","Valley"),
		_105("75844,15963,-3765","Ivory"),
		_106("166643,51077,-4118","Narsell"),
		_107("178721,36344,-4088","Bayou"),
		_108("106254,199618,-3726","White Sands"),
		_109("146159,-68899,-3731","Borderland"),
		_110("67231,-72608,-3760","Swamp"),
		_111("121242,-141171,-1521","Archaic"),
		_112("14692,149933,-3353","Floran"),
		_113("-59042,80877,-3651","Cloud Mountain"),
		_114("50579,135446,-2908","Tanor"),
		_115("8413,103166,-3688","Dragonspine"),
		_116("77803,82048,-3594","Antharas"),
		_117("125377,-21749,-3923","Western"),
		_118("124893,84524,-2462","Hunters"),
		_119("68880,179212,-3166","Aaru"),
		_120("92320,-58036,-2915","Demon"),
		_121("69653,-82613,-3505","Monastic");
		public String _Location;
		@SuppressWarnings("unused")
		public String _Name;

		private FORTRESS_LOCA(String Location, String Name)
		{
			_Location = Location;
			_Name = Name;
		}
	}
	
	public static void sendHtmReady(L2PcInstance player){
		final NpcHtmlMessage html = comun.htmlMaker(player, "config/zeus/htm/" + language.getInstance().getFolder(player) + "/fortress_tele_ok.html");
		String imgLogo = "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + String.valueOf(general.ID_LOGO_SERVER) + "\" width=256 height=64>";
		html.replace("%SERVER_LOGO%", imgLogo);
		central.sendHtml(player, html.getHtml(),false);
	}
	
	public static void giveRewardFortress(L2PcInstance player){
		String IPPLAYER = ZeuS.getIp_pc(player) + ZeuS.getIp_Wan(player);
		if(IP_REWARDER!=null){
			if(IP_REWARDER.containsKey(IPPLAYER)){
				int UnixSave = IP_REWARDER.get(IPPLAYER);
				int FortressAuto = Config.FORTRESS_ZONE_FAME_TASK_FREQUENCY;
				if( (opera.getUnixTimeNow() - UnixSave) < FortressAuto){
					return;
				}
			}
			IP_REWARDER.put(IPPLAYER, opera.getUnixTimeNow());
		}else{
			IP_REWARDER.put(IPPLAYER, opera.getUnixTimeNow());
		}
		
		if (player.isInsideZone(ZoneIdType.FORT))
		{
			
		}		
	}

	public static void closeWindows(L2PcInstance player, int idFort){
		PlayerClose.put(player.getObjectId(), idFort);
	}
	
	private static boolean isPlayerClose(L2PcInstance player, int idFort){
		if(PlayerClose!=null){
			if(PlayerClose.containsKey(player.getObjectId())){
				if(PlayerClose.get(player.getObjectId()) == idFort){
					return true;
				}
			}
		}
		closeWindows(player,idFort);
		return false;
	}
	
	public static void sendWindowsFortress(Vector<Integer> ClanF, Fort FortressIns){
		
		NpcHtmlMessage html = null;
		String HTML = "";
		
		for (L2PcInstance ppl : L2World.getInstance().getPlayers())
		{
			if (ppl.getClient().isDetached())
			{
				continue;
			}else if(ppl.isJailed()){
				continue;
			}else if(ppl.getInstanceId()>0){
				continue;
			}else if(SunriseEvents.isInEvent(ppl)){
				continue;
			}else if(RaidBossEvent._started && RaidBossEvent.isPlayerOnRBEvent(ppl)){
				continue;
			}else if(ppl.isInOlympiadMode()){
				continue;
			}else if(!ZeuS.isActivePIN(ppl)){
				continue;
			}
			
			L2Clan clanP = ppl.getClan();
			if (clanP != null)
			{
				if (ClanF.contains(clanP.getId()))
				{
					continue;
				}
				getImageToSend(ppl,FortressIns);
				if(HTML.length()==0){
					html = comun.htmlMaker(ppl, "config/zeus/htm/" + language.getInstance().getFolder(ppl) + "/fortress.html");
					HTML = getWindowsFortress(FortressIns, html.getHtml());
				}
				if(!isPlayerClose(ppl, FortressIns.getResidenceId())){
					central.sendHtml(ppl, HTML, false);
				}
			}
		}		
	}
	
	private static void getImageToSend(L2PcInstance player, Fort FortressIns)
	{
		int idFort = FortressIns.getResidenceId();
		_ImageGenerator.getInstance().generateImage(player, idFort);
		opera.getImageLogo(player);
		
	}
	
	private static String getLocationTele(int idFortress){
		String retorno ="";
		switch(idFortress){
			case 101:
				return FORTRESS_LOCA._101._Location;
			case 102:
				return FORTRESS_LOCA._102._Location;
			case 103:
				return FORTRESS_LOCA._103._Location;
			case 104:
				return FORTRESS_LOCA._104._Location;
			case 105:
				return FORTRESS_LOCA._105._Location;
			case 106:
				return FORTRESS_LOCA._106._Location;
			case 107:
				return FORTRESS_LOCA._107._Location;
			case 108:
				return FORTRESS_LOCA._108._Location;
			case 109:
				return FORTRESS_LOCA._109._Location;
			case 110:
				return FORTRESS_LOCA._110._Location;
			case 111:
				return FORTRESS_LOCA._111._Location;
			case 112:
				return FORTRESS_LOCA._112._Location;
			case 113:
				return FORTRESS_LOCA._113._Location;
			case 114:
				return FORTRESS_LOCA._114._Location;
			case 115:
				return FORTRESS_LOCA._115._Location;
			case 116:
				return FORTRESS_LOCA._116._Location;
			case 117:
				return FORTRESS_LOCA._117._Location;
			case 118:
				return FORTRESS_LOCA._118._Location;
			case 119:
				return FORTRESS_LOCA._119._Location;
			case 120:
				return FORTRESS_LOCA._120._Location;
			case 121:
				return FORTRESS_LOCA._121._Location;
		}
		
		return retorno;
	}
	
	@SuppressWarnings("unused")
	private static String getWindowsFortress(Fort FortressIns, String html){
		//getImageToSend(player,FortressIns);
		int idFort = FortressIns.getResidenceId();
		
		String Loc[] = getLocationTele(idFort).split(",");
		
		String imgFort = "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + String.valueOf(idFort) + "\" width=256 height=64>";
		String imgLogo = "<img src=\"Crest.crest_" + Config.SERVER_ID + "_" + String.valueOf(general.ID_LOGO_SERVER) + "\" width=256 height=64>";
		L2Clan ClanOwner = FortressIns.getOwnerClan();
		String ClanName = ClanOwner == null ? "Free" : ClanOwner.getName();
		String ClanLeader = ClanOwner == null ? "No One" : ClanOwner.getLeaderName();
		Calendar currentCal = FortressIns.getSiegeDate();
		String SiegeDate = currentCal.getTime().toString();
		String RewardEvery = String.valueOf(Config.FORTRESS_ZONE_FAME_TASK_FREQUENCY / 60);
		String RewardFame = String.valueOf(Config.FORTRESS_ZONE_FAME_AQUIRE_POINTS);
		String otherReward = "";
		
		if(Config.ZEUS_FORTRESS_ZONE_REWARD.indexOf(";")>0){
			for(String Items : Config.ZEUS_FORTRESS_ZONE_REWARD.split(";")){
				if(otherReward.length()>0){
					otherReward += ", ";
				}
				otherReward += Items.split(",")[1] + " " + central.getNombreITEMbyID(Integer.valueOf(Items.split(",")[0]));
			}
		}else{
			otherReward = Config.ZEUS_FORTRESS_ZONE_REWARD.split(",")[1] + " " + central.getNombreITEMbyID(Integer.valueOf(Config.ZEUS_FORTRESS_ZONE_REWARD.split(",")[0]));
		}
		
		String ByPassTeleport = "bypass ZeuS moveto " + String.valueOf(Loc[0]) + ";" + String.valueOf(Loc[1]) + ";" + String.valueOf(Loc[2]);
		String BtnTeleportMe = "<button value=\"Teleport me near Fortress\" action=\"" + ByPassTeleport + "\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Back\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\">";
		
		String BtnClose = "<button value=\"Close\" action=\bypass ZeuS close_fortress "+ String.valueOf(idFort) +"\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Back\" fore=\"L2UI_CT1.OlympiadWnd_DF_Back\">";
		
		String HTML = html.replace("%BTN_CLOSE%", BtnClose) .replace("%BTN_GO%", BtnTeleportMe). replace("%REWARD%", otherReward).replace("%MINUTES%", RewardEvery).replace("%SIEGE_DATE%", SiegeDate).replace("%CLAN_LEADER%", ClanLeader).replace("%CLAN_NAME%", ClanName).replace("%FORTRESS_IMAGE%", imgFort).replace("%FORTRESS_NAME%", FortressIns.getName()).replace("%SERVER_LOGO%", imgLogo);
		
		return HTML;
	}
	
}
