package ZeuS.dressme;

import ZeuS.Config.general;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.type.ArmorType;
import l2r.gameserver.model.items.type.WeaponType;

public class newDressme {
	private final int totalWeaponTypes = WeaponType.values().length;
	private final int totalArmorTypes = ArmorType.values().length;
	private final boolean[][] weaponMapping = new boolean[totalWeaponTypes][totalWeaponTypes];
	private final boolean[][] armorMapping = new boolean[totalArmorTypes][totalArmorTypes];
	private boolean isLoad=false;
	protected void loadConfig(){
		if(isLoad){
			return;
		}
		for(int i =0; i< weaponMapping.length; i++){
			  for(int j = 0; j< weaponMapping.length; j++) {
				  weaponMapping[i][j]=false;
			  }
		}
		for(int i =0; i< armorMapping.length; i++){
			for(int j = 0; j< armorMapping.length; j++) {
				armorMapping[i][j]=false;
			}
		}
		weaponMapping[WeaponType.SWORD.ordinal()][WeaponType.BLUNT.ordinal()] = true;
		weaponMapping[WeaponType.BLUNT.ordinal()][WeaponType.SWORD.ordinal()] = true;

		armorMapping[ArmorType.SIGIL.ordinal()][ArmorType.SHIELD.ordinal()] = true;
		armorMapping[ArmorType.SHIELD.ordinal()][ArmorType.SIGIL.ordinal()] = true;

		armorMapping[ArmorType.HEAVY.ordinal()][ArmorType.LIGHT.ordinal()] = true;
		armorMapping[ArmorType.HEAVY.ordinal()][ArmorType.MAGIC.ordinal()] = true;

		armorMapping[ArmorType.LIGHT.ordinal()][ArmorType.HEAVY.ordinal()] = true;
		armorMapping[ArmorType.LIGHT.ordinal()][ArmorType.MAGIC.ordinal()] = true;

		armorMapping[ArmorType.MAGIC.ordinal()][ArmorType.LIGHT.ordinal()] = true;
		armorMapping[ArmorType.MAGIC.ordinal()][ArmorType.HEAVY.ordinal()] = true;

		isLoad=true;
	}
	
	protected int armorMatching(L2PcInstance player, ArmorType virtual, ArmorType equiped, int matchId , int noMatchId){
    	if(!general.DRESSME_CAN_USE_IN_OLYS){
    		if(player.isNoble()){
    			if(player.isOlympiadStart() || player.isInOlympiadMode()){
    				return noMatchId;
    			}
    		}
    	}
      if(virtual == equiped){
    	  return matchId;
      }
      
      return matchId;
      /*
      if(armorMapping[virtual.ordinal()][equiped.ordinal()] == true){
    	  return matchId;
      }
      return noMatchId;*/
	}
	
	protected int weaponMatching(L2PcInstance player, WeaponType virtual, WeaponType equiped, int matchId, int noMatchId){
		  if(!general.DRESSME_CAN_USE_IN_OLYS || !general.STATUS_DRESSME){
			  if(player.isNoble()){
				  if(player.isOlympiadStart() || player.isInOlympiadMode()){
					  return noMatchId;
				  }
			  }
		  }
		  if(virtual == equiped){
			  return matchId;
		  }

		  if(weaponMapping[virtual.ordinal()][equiped.ordinal()] == true){
			  L2Item V_Item = ItemData.getInstance().getTemplate(matchId);
			  L2Item R_Item = ItemData.getInstance().getTemplate(noMatchId);
			  if(V_Item.getBodyPart() == R_Item.getBodyPart()){
				  return matchId;
			  }
		  }
		  return noMatchId;
	}
	
	
	
}
