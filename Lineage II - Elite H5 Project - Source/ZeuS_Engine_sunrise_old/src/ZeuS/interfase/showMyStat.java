package ZeuS.interfase;

import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.stats.BaseStats;
import l2r.gameserver.model.stats.Stats;
import l2r.gameserver.network.serverpackets.NpcHtmlMessage;
import ZeuS.Config.general;

public class showMyStat {

	private static String getFormat(){
		if(!general._activated()){
			return "";
		}
		String Retorno = "<html><title>%CHAR_NAME%</title>" + central.LineaDivisora(2) + central.headFormat("%CHAR_NAME% Stat Info","63AA1C") + central.LineaDivisora(2);
		Retorno += "<table border=0 width=\"280\" bgcolor=151515>" +
				"<tr><td width=140 align=CENTER>Name:</td><td><font color=\"FAAC58\">%CHAR_NAME%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>Clan:</td><td><font color=\"FAAC58\">%CLAN_NAME%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>Level: </td><td><font color=\"FAAC58\">%CHAR_LEVEL%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>Actual Class: </td><td><font color=\"FAAC58\">%CLASS_NAME%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>Base Class: </td><td><font color=\"FAAC58\">%BASE_CLASS%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>CP: </td><td><font color=\"FAAC58\">%CP%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>HP: </td><td><font color=\"FAAC58\">%HP%</font></td></tr>" +
				"<tr><td width=140 align=CENTER>MP: </td><td><font color=\"FAAC58\">%MP%</font></td></tr>" +
				"</table>";

		Retorno += central.LineaDivisora(2) +central.headFormat("PvP / Pk Stat","DF0101") + central.LineaDivisora(2);

		Retorno += "<table border=0 width=\"280\" bgcolor=151515>" +
				"<tr>" +
				"<td width=70 align=CENTER>PvP Kills: </td>" +
				"<td width=70 align=CENTER><font color=\"FAAC58\">%PVP_KILL%</font></td>" +
				"<td width=70 align=CENTER>PvP Flag: </td>" +
				"<td width=70 align=CENTER><font color=\"FAAC58\">%PVP_FLAG%</font></td>" +
				"</tr>" +
				"<tr>" +
				"<td width=70 align=CENTER>PK Kills: </td>" +
				"<td width=70 align=CENTER><font color=\"FAAC58\">%PK_KILL%</font</td>" +
				"<td width=70 align=CENTER>Karma: </td>" +
				"<td width=70 align=CENTER><font color=\"FAAC58\">%KARMA%</font></td>" +
				"</tr>" +
				"</table>";

		Retorno += central.LineaDivisora(2) + central.headFormat("Accessories, Armors, Weapons and Jewels","63AA1C") + central.LineaDivisora(2);

		Retorno += "<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_RHAND% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_RHAND% <font color=F4FA58>+%ENCH_RHAND%</font></td></tr</table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_LHAND% width=32 height=32</td><td width=255 height=39><table><tr><td>%NOM_LHAND% <font color=F4FA58>+%ENCH_LHAND%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_HEAD% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_HEAD% <font color=F4FA58>+%ENCH_HEAD%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_CHEST% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_CHEST% <font color=F4FA58>+%ENCH_CHEST%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_LEGS% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_LEGS% <font color=F4FA58>+%ENCH_LEGS%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_GLOVES% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_GLOVES% <font color=F4FA58>+%ENCH_GLOVES%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_FEET% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_FEET% <font color=F4FA58>+%ENCH_FEET%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_CLOAK% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_CLOAK% <font color=F4FA58>+%ENCH_CLOAK%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_UNDER% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_UNDER% <font color=F4FA58>+%ENCH_UNDER%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_BELT% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_BELT% <font color=F4FA58>+%ENCH_BELT%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_HAIR% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_HAIR% <font color=F4FA58>+%ENCH_HAIR%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_HAIR2% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_HAIR2% <font color=F4FA58>+%ENCH_HAIR2%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_LEAR% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_LEAR% <font color=F4FA58>+%ENCH_LEAR%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_REAR% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_REAR% <font color=F4FA58>+%ENCH_REAR%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_NECK% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_NECK% <font color=F4FA58>+%ENCH_NECK%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_LFINGER% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_LFINGER% <font color=F4FA58>+%ENCH_LFINGER%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_RFINGER% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_RFINGER% <font color=F4FA58>+%ENCH_RFINGER%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>" +
				"<table bgcolor=151515 width=280><tr><td height=39 width=45><img src=%ICON_LBRACELET% width=32 height=32></td><td width=255 height=39><table><tr><td>%NOM_LBRACELET% <font color=F4FA58>+%ENCH_LBRACELET%</font></td></tr></table></td></tr></table>" +
				"<img src=\"L2UI.SquareBlank\" width=280 height=1><img src=\"L2UI.SquareGray\" width=280 height=1><img src=\"L2UI.SquareBlank\" width=270 height=1>";
			Retorno += central.getPieHTML() + "</body></html>";

		return Retorno;
	}

	public static void showMyStats(L2PcInstance target, L2PcInstance activeChar){
		if(!general._activated()){
			return;
		}


			if(!general.getCharConfigSHOWSTAT(target)){
					activeChar.sendMessage(target.getName() + " has its stats in private mode.");
					return;
				}

				activeChar.setTarget(target);
				int hpMul = Math.round((float) (((L2Character) target).getStat().calcStat(Stats.MAX_HP, 1, target, null) / BaseStats.CON.calcBonus(target)));
				if (hpMul == 0)
				{
					hpMul = 1;
				}
				central.msgbox_Lado(activeChar.getName()+ " is seeing your stats.", target);

				final NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
				//adminReply.setFile(activeChar.getHtmlPrefix(), "data/html/admin/charinfostatpublic.htm");
				adminReply.setHtml(getFormat());
				adminReply.replace("%CHAR_NAME%", target.getName());
				adminReply.replace("%CLAN_NAME%", String.valueOf(target.getClan() != null ? target.getClan().getName() : "No Clan"));
				adminReply.replace("%CHAR_LEVEL%", String.valueOf(((L2Character) target).getLevel()));
				//CharTemplateTable.getInstance().getClassNameById(st.getClassId().getId());
				int idClassSub = target.getClassId().getId();
				adminReply.replace("%CLASS_NAME%", central.getClassName(activeChar, idClassSub));

				//adminReply.replace("%BASE_CLASS%", CharTemplateTable.getInstance().getTemplate(((L2PcInstance) target).getBaseClass()).getClass().getName());
				adminReply.replace("%BASE_CLASS%", central.getClassName(activeChar, target.getBaseClass()) );

				adminReply.replace("%CP%", String.valueOf((int) target.getCurrentCp()) + "/" + String.valueOf(target.getMaxCp()));
				adminReply.replace("%HP%", String.valueOf((int) target.getCurrentHp()) + "/" + String.valueOf(target.getMaxHp()));
				adminReply.replace("%MP%", String.valueOf((int) target.getCurrentMp()) + "/" + String.valueOf(target.getMaxMp()));
				adminReply.replace("%PVP_KILL%", String.valueOf(target.getPvpKills()));
				adminReply.replace("%PVP_FLAG%", String.valueOf(target.getPvpFlag() == 0 ? "False" : "True"));
				adminReply.replace("%PK_KILL%", String.valueOf(target.getPkKills()));
				adminReply.replace("%KARMA%", String.valueOf(target.getKarma()));

				String strParte = "RHAND";
				int SlotPedir = Inventory.PAPERDOLL_RHAND;


				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "LHAND";
				SlotPedir = Inventory.PAPERDOLL_LHAND;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "HEAD";
				SlotPedir = Inventory.PAPERDOLL_HEAD;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "CHEST";
				SlotPedir = Inventory.PAPERDOLL_CHEST;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));


				strParte = "LEGS";
				SlotPedir = Inventory.PAPERDOLL_LEGS;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));


				strParte = "GLOVES";
				SlotPedir = Inventory.PAPERDOLL_GLOVES;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "FEET";
				SlotPedir = Inventory.PAPERDOLL_FEET;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "CLOAK";
				SlotPedir = Inventory.PAPERDOLL_CLOAK;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "UNDER";
				SlotPedir = Inventory.PAPERDOLL_UNDER;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "BELT";
				SlotPedir = Inventory.PAPERDOLL_BELT;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "HAIR";
				SlotPedir = Inventory.PAPERDOLL_HAIR;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "HAIR2";
				SlotPedir = Inventory.PAPERDOLL_HAIR2;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "LEAR";
				SlotPedir = Inventory.PAPERDOLL_LEAR;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "REAR";
				SlotPedir = Inventory.PAPERDOLL_REAR;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "NECK";
				SlotPedir = Inventory.PAPERDOLL_NECK;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "LFINGER";
				SlotPedir = Inventory.PAPERDOLL_LFINGER;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "RFINGER";
				SlotPedir = Inventory.PAPERDOLL_RFINGER;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				strParte = "LBRACELET";
				SlotPedir = Inventory.PAPERDOLL_LBRACELET;
				adminReply.replace("%ICON_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getIcon(): ItemData.getInstance().getTemplate(3883).getIcon()));
				adminReply.replace("%NOM_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getItem().getName() : "No Item Equipped"));
				adminReply.replace("%ENCH_" + strParte + "%", String.valueOf(target.getInventory().getPaperdollItem(SlotPedir) != null ? target.getInventory().getPaperdollItem(SlotPedir).getEnchantLevel() : "0"));

				activeChar.sendPacket(adminReply);
	}

}
