package ZeuS.interfase;

import java.util.Vector;

import ZeuS.Config.general;
import ZeuS.language.language;
import ZeuS.procedimientos.opera;
import ZeuS.server.comun;
import l2r.gameserver.instancemanager.PunishmentManager;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.punishment.PunishmentAffect;
import l2r.gameserver.model.punishment.PunishmentType;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;

public class jailBail {
	public static void showMain(L2PcInstance player){
		String Link = "bypass ZeuS bail_me";
		final NpcHtmlMessage html = comun.htmlMaker(player, "./config/zeus/htm/" + language.getInstance().getFolder(player) + "/jail_bail.html");
		html.replace("%SERVER_LOGO%", opera.getImageLogo(player));
		html.replace("%LINK%", Link);
		html.replace("%PRICE%", central.ItemNeedShowBox(general.JAIL_BAIL_COST));
		central.sendHtml(player, html.getHtml(), false);	
	}
	public static void checkit(L2PcInstance player){
		if(!general.JAIL_BAIL_STATUS){
			central.msgbox(language.getInstance().getMsg(player).DISABLE_BY_ADMIN, player);
			return;
		}
		if(!player.isJailed()){
			return;
		}
		int multiple = opera.getBlackListJailBail(player, false);
		Vector<String>ItemQuitar = new Vector<String>();
		if(general.JAIL_BAIL_COST.indexOf(";")>0){
			for(String Items : general.JAIL_BAIL_COST.split(";")){
				int cantidad = Integer.valueOf(Items.split(",")[1]) * multiple;
				ItemQuitar.add(Items.split(",")[0] + "," + String.valueOf(cantidad));
			}
		}else{
			int cantidad = Integer.valueOf(general.JAIL_BAIL_COST.split(",")[1]) * multiple;
			ItemQuitar.add(general.JAIL_BAIL_COST.split(",")[0] +","+ String.valueOf(cantidad));
		}
		
		if(opera.haveItem(player, ItemQuitar)){
			opera.removeItem(ItemQuitar, player);
			central.msgbox(language.getInstance().getMsg(player).JAIL_BAIL_COMPLETE, player);
			PunishmentManager.getInstance().stopPunishment(player.getObjectId(), PunishmentAffect.CHARACTER, PunishmentType.JAIL);
		}
	}
}
