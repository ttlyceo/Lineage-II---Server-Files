package ZeuS.interfase;


import java.util.HashMap;
import java.util.Map;

import ZeuS.Config.general;
import ZeuS.procedimientos.opera;

import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.instance.L2ItemInstance;

public class overenchant {
	private static Map<Integer, Integer>UnixTimeCheck = new HashMap<Integer, Integer>();

	private static boolean canCheck_time(L2PcInstance player){
		boolean Retorno = false;
		if(UnixTimeCheck!=null){
			if(UnixTimeCheck.containsKey(player.getObjectId())){
				int SegundosPasado = opera.getUnixTimeNow() - UnixTimeCheck.get(player.getObjectId());
				if(SegundosPasado >= general.ANTI_OVER_ENCHANT_SECOND_BETWEEN_CHECK_BEFORE_ATTACK){
					Retorno = true;
				}else{
					Retorno = false;
				}
			}else{
				Retorno = true;
				UnixTimeCheck.put(player.getObjectId(), opera.getUnixTimeNow());
			}
		}else{
			Retorno = true;
			UnixTimeCheck.put(player.getObjectId(), opera.getUnixTimeNow());
		}

		return Retorno;
	}

	private static void checkToPunishment(L2PcInstance player){
		if(!general.ANTI_OVER_ENCHANT_PUNISH_ALL_SAME_IP){
			setPunishment(player);
		}else{
			setPunishment(player);
			for(L2PcInstance cha : opera.getAllPlayerOnThisIp(player)){
				setPunishment(cha);
			}
		}
		if(general.ANTI_OVER_ENCHANT_ANNOUCEMENT_ALL){
			//%TYPE_PUNISHMENT%. Nombre PJ %CHARNAME%
			opera.AnunciarTodos("Over Enchant", general.ANTI_OVER_ENCHANT_MESJ_PUNISH.replace("%CHARNAME%", player.getName()).replace("%TYPE_PUNISHMENT%", general.ANTI_OVER_TYPE_PUNISH));
		}
	}

	private static void setPunishment(L2PcInstance player){
		//"JAIL","BAN_CHAR","BAN_ACC"
		switch(general.ANTI_OVER_TYPE_PUNISH){
			case "JAIL":
				opera.sendToJail(player, 0);
				break;
			case "BAN_CHAR":
				opera.setBanToChar(player, "Over Enchant System");
				break;
			case "BAN_ACC":
				opera.setBanToAccChar(player, "Over Enchant System");
				break;
		}
	}

	private static void checkAll(L2PcInstance player){
		if(player.isGM()){
			return;
		}
		int[] VectorLocaciones = {Inventory.PAPERDOLL_HEAD, Inventory.PAPERDOLL_CHEST, Inventory.PAPERDOLL_LEGS, Inventory.PAPERDOLL_GLOVES, Inventory.PAPERDOLL_FEET, Inventory.PAPERDOLL_RHAND, Inventory.PAPERDOLL_LHAND, Inventory.PAPERDOLL_UNDER, Inventory.PAPERDOLL_BELT, Inventory.PAPERDOLL_LEAR,Inventory.PAPERDOLL_REAR, Inventory.PAPERDOLL_LFINGER, Inventory.PAPERDOLL_RFINGER, Inventory.PAPERDOLL_NECK, Inventory.PAPERDOLL_LBRACELET };
		for(int Posi : VectorLocaciones){
			if(player.getInventory().getPaperdollItem(Posi) != null){
				L2ItemInstance itemVer = player.getInventory().getPaperdollItem(Posi);
				if(itemVer.isArmor()){
					if(itemVer.getEnchantLevel() > general.ANTI_OVER_ENCHANT_MAX_ARMOR){
						checkToPunishment(player);
						return;
					}
				}else if(itemVer.isWeapon()){
					if(itemVer.getEnchantLevel()> general.ANTI_OVER_ENCHANT_MAX_WEAPON){
						checkToPunishment(player);
						return;
					}
				}
			}
		}
	}

	public static void check(L2PcInstance player, boolean isAttack){
		if(!general.ANTI_OVER_ENCHANT_ACT){
			return;
		}
		if(isAttack && general.ANTI_OVER_ENCHANT_CHECK_BEFORE_ATTACK){
			if(canCheck_time(player)){
				checkAll(player);
			}
		}else if(!isAttack){
			checkAll(player);
		}
	}
}
