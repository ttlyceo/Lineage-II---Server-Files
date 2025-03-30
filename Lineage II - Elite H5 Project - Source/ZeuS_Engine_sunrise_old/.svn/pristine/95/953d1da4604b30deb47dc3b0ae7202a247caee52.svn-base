package ZeuS.Config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Logger;

import ZeuS.procedimientos.opera;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;

public class Advclassmaster {
	private static final Logger _log = Logger.getLogger(Advclassmaster.class.getName());
	private Vector<String> REWARD = new Vector<String>();
	private Map<String, String> ARMOR = new HashMap<String, String>();
	private Map<String, String> WEAPON = new HashMap<String, String>();

	private Vector<Integer> REWARDED_PLAYER = new Vector<Integer>();
	
	private boolean ENABLE;
	private boolean GIVE_WEAPON;
	private boolean GIVE_ARMOR;
	private boolean GIVE_REWARD;
	private boolean GIVE_JUST_1_TIME;
	
	private int WEAPON_TO_GIVE;
	@SuppressWarnings("unused")
	private int IDCLASSINDEX;
	
	public Advclassmaster(){
		
	}
	
	
	
	public int getWeaponToGive(){
		return this.WEAPON_TO_GIVE; 
	}
	
	public boolean showArmorWindows(){
		return this.GIVE_ARMOR;
	}
	
	public boolean showWeaponWindows(){
		return this.GIVE_WEAPON;
	}	
	
	public boolean canJust_1_Item_at_time(){
		return this.GIVE_JUST_1_TIME;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public String getArmorBtn(){
		String retorno = "";
		Iterator itr = this.ARMOR.entrySet().iterator();
		String ByPass = "%ARMOR_SET%";
		String btnArmor = "";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	//retorno += btnArmor.replace("%ARMOR_SET%", ((String)Entrada.getKey()).replace(" ", "_"));
	    	retorno += "<button value=\""+ ((String)Entrada.getKey()) + "\" action=\"link zeusPF;Armor_sel;"+ ((String)Entrada.getKey()).replace(" ", "_") +";0;0;0\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Apply\" fore=\"L2UI_CT1.OlympiadWnd_DF_Apply\">";
	    }
	    return retorno;
	}
	
	@SuppressWarnings({ "rawtypes", "unused" })
	public String getWeaponBtn(){
		String retorno = "";
		Iterator itr = this.WEAPON.entrySet().iterator();
		String ByPass = "%WEAPON_SET%";
		String btnArmor = "";
	    while(itr.hasNext()){
	    	Map.Entry Entrada = (Map.Entry)itr.next();
	    	String Cadena = "<button value=\""+ ((String)Entrada.getKey()) + "\" action=\"link zeusPF;Weapon_sel;"+ ((String)Entrada.getKey()).replace(" ", "_") +";0;0;0\" width=200 height=32 back=\"L2UI_CT1.OlympiadWnd_DF_Apply\" fore=\"L2UI_CT1.OlympiadWnd_DF_Apply\">";
	    	retorno += Cadena;
	    }
	    return retorno;
	}
	
	
	public void setNakedChar(L2PcInstance player, boolean justArmor){
		int[] PartToNaked_Armor = {L2Item.SLOT_L_EAR, L2Item.SLOT_L_FINGER, L2Item.SLOT_R_EAR, L2Item.SLOT_R_FINGER, L2Item.SLOT_NECK,  L2Item.SLOT_HEAD, L2Item.SLOT_CHEST, L2Item.SLOT_LEGS, L2Item.SLOT_FEET, L2Item.SLOT_GLOVES } ;
		int[] PartToNaked_WEAPON = { L2Item.SLOT_L_HAND, L2Item.SLOT_R_HAND } ;
		if(justArmor){
			for (int part : PartToNaked_Armor){
				player.getInventory().unEquipItemInBodySlot(part);
			}
		}else{
			for (int part : PartToNaked_WEAPON){
				player.getInventory().unEquipItemInBodySlot(part);
			}			
		}
	}
	
	
	public void giveArmor(L2PcInstance player,String armorToGive){
		String Armors = this.ARMOR.get(armorToGive.replace("_", " "));
		boolean disarmArmor = false;
		boolean disarmJewelsEar = false;
		boolean disarmJewelsFinger = false;
		boolean disarmWeapon = false;
		for(String PA : Armors.split(";")){
			int idItem = Integer.valueOf(PA.split(",")[0]);
			int Cantidad = Integer.valueOf(PA.split(",")[1]);
			final L2ItemInstance It = player.getInventory().addItem("Init", idItem, Cantidad, player, null);
			int BodyPart = It.getItem().getBodyPart();
			try{
				if(It.isEquipable()){
					if((BodyPart == L2Item.SLOT_R_HAND) && (player.getInventory().getPaperdollItemId(L2Item.SLOT_R_HAND)<=0)){
						if(!disarmWeapon){
							disarmWeapon = true;
							player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_R_HAND);
							player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_L_HAND);
							try{
								if(player.getInventory().getPaperdollItem(L2Item.SLOT_LR_HAND)!=null){
									player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_LR_HAND);
								}
							}catch(Exception a){
								
							}
						}
						player.getInventory().unEquipItemInBodySlot(BodyPart);
						player.getInventory().equipItem(It);
					}else if(BodyPart == L2Item.SLOT_L_HAND){
						L2Item bodyAct = player.getInventory().getPaperdollItem(L2Item.SLOT_R_HAND).getItem();
						if(!disarmWeapon){
							disarmWeapon = true;
							player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_R_HAND);
							player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_L_HAND);
							try{
								if(player.getInventory().getPaperdollItem(L2Item.SLOT_LR_HAND)!=null){
									player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_LR_HAND);
								}
							}catch(Exception a){
								
							}
						}						
						if(bodyAct.getBodyPart() != L2Item.SLOT_LR_HAND){
							player.getInventory().equipItem(It);						
						}
					}else if( BodyPart != L2Item.SLOT_R_HAND ){
						if(BodyPart == L2Item.SLOT_LR_EAR){
							if(!disarmJewelsEar){
								player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_R_EAR);
								player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_L_EAR);
								try{
									if(player.getInventory().getPaperdollItem(L2Item.SLOT_LR_EAR)!=null){
										player.getInventory().unEquipItemInBodySlot(L2Item.SLOT_LR_EAR);
									}
								}catch(Exception a){
									
								}
								disarmJewelsEar = true;
							}
						}else if(BodyPart == L2Item.SLOT_LR_FINGER){
							if(!disarmJewelsFinger){
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_R_FINGER);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_L_FINGER);
								try{
									if(player.getInventory().getPaperdollItem(L2Item.SLOT_LR_FINGER)!=null){
										player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_LR_FINGER);
									}
								}catch(Exception a){
									
								}
								disarmJewelsFinger=true;
							}
						}else{
							if(!disarmArmor){
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_FULL_ARMOR);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_ALLDRESS);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_HEAD);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_CHEST);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_FEET);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_GLOVES);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_LEGS);
								player.getInventory().unEquipItemInBodySlot( L2Item.SLOT_FEET);
								disarmArmor = true;
							}
							player.getInventory().unEquipItemInBodySlot(BodyPart);
						}
						player.getInventory().equipItem(It);
					}
				}else if(It.isEtcItem()){
					//agregar soulshot y spirit shot
				}
			}catch(Exception a){
				_log.warning("Error Class Master Choose Armor->" + a.getMessage());
			}
			player.broadcastInfo();
		}		
	}
	
	public void giveWeapon(L2PcInstance player,String WeaponToGive){
		String Weap = this.WEAPON.get(WeaponToGive.replace("_", " "));
		for(String PA : Weap.split(";")){
			int idItem = Integer.valueOf(PA.split(",")[0]);
			int Cantidad = Integer.valueOf(PA.split(",")[1]);
			opera.giveReward(player, idItem, Cantidad);
			L2ItemInstance It = player.getInventory().getItemByItemId(idItem);
			int BodyPart = It.getItem().getBodyPart();
			if(It.isEquipable()){
				player.getInventory().unEquipItemInBodySlot(BodyPart);
				player.getInventory().equipItem(It);
			}
		}
		player.broadcastInfo();
	}	
	
	public void giveReward(L2PcInstance player){
		if(!this.ENABLE){
			return;
		}
		
		if(!this.GIVE_REWARD){
			return;
		}
		
		if(this.REWARD==null){
			return;
		}else if(this.REWARD.size()==0){
			return;
		}
		if(REWARDED_PLAYER!=null){
			if(REWARDED_PLAYER.contains(player.getObjectId())){
				return;
			}
		}
		
		for(String Rew : this.REWARD){
			int idItem = Integer.valueOf(Rew.split(",")[0]);
			int Cantidad = Integer.valueOf(Rew.split(",")[1]);
			opera.giveReward(player, idItem, Cantidad);
		}
		REWARDED_PLAYER.add(player.getObjectId());
	}
	
	public boolean isActive(){
		return ENABLE;
	}
	
	public Advclassmaster(int idClassIndex, boolean G_W, boolean G_A, boolean G_R, boolean isActive, int WeaponToGive, boolean giveJust1Time){
		this.IDCLASSINDEX = idClassIndex;
		this.GIVE_ARMOR = G_A;
		this.GIVE_REWARD = G_R;
		this.GIVE_WEAPON = G_W;
		this.ENABLE = isActive;
		this.WEAPON_TO_GIVE = WeaponToGive;
		this.GIVE_JUST_1_TIME = giveJust1Time;
	}
	
	public void setArmor(String Name, String Items){
		this.ARMOR.put(Name, Items);
		
	}
	
	public void setWeapon(String Name, String Items, int WeaponToGive){
		this.WEAPON_TO_GIVE = WeaponToGive;
		this.WEAPON.put(Name, Items);
		
	}
	
	public void setReward(String Rewards){
		this.REWARD.add(Rewards);
		
	}
	
}
