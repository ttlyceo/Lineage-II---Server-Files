package ZeuS.Config;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l2r.gameserver.ThreadPoolManager;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.itemcontainer.Inventory;
import l2r.gameserver.model.items.L2Item;

public class _dress{
	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(_dress.class.getName());
	public enum BodyPart{
		R_HAND,
		L_HAND,
		CHEST,
		LEGS,
		GLOVES,
		FEET,
		ID_DRESSME,
		CLOAK,
		HEAD_1,
		HEAD_2
	}
	private int ID_Right_Hand;
	private int ID_Left_Hand;
	private int ID_Chest;
	private int ID_Legs;
	private int ID_Gloves;
	private int ID_Feet;
	private int ID_Dressme;
	private int ID_Cloak;
	private int ID_Head_1;
	private int ID_Head_2;
	private boolean isTry = false;
	private boolean isFullArmorDressme = false;
	private boolean isSemiFullArmorDressme = false;
	private boolean isArmorTryDressme = false;
	private int FullArmorDressme_ID = -1;
	private boolean isWeapon = false;
	private boolean isCloak = false;
	private int ID_Deco_Try = -100;
	
	Map<Integer,Integer> ID_Dressme_Try = new HashMap<Integer, Integer>();
	
	public boolean isEmpty(){
		return this.ID_Right_Hand==0 && this.ID_Left_Hand==0 && this.ID_Gloves == 0 && this.ID_Chest == 0 && this.ID_Legs==0 && this.ID_Feet==0 && this.ID_Cloak==0;
	}
	
	public void setNewDressmeUse(int IdDressme){
		this.ID_Dressme = IdDressme;
	}
	
	public void set(int cloak, int rhand,int lhand, int chest,int legs, int gloves, int feet, int idDressme, int Hood1, int Hood2){
		this.ID_Right_Hand=rhand;
		this.ID_Left_Hand=lhand;
		this.ID_Chest=chest;
		this.ID_Legs=legs;
		this.ID_Gloves=gloves;
		this.ID_Feet=feet;
		this.ID_Cloak = cloak;
		this.ID_Dressme=idDressme;
		this.isTry = false;
		this.isFullArmorDressme = false;
		this.isSemiFullArmorDressme = false;
		this.isArmorTryDressme = false;
		this.FullArmorDressme_ID = -1;
		this.isWeapon = false;
		this.isCloak = false;
		this.ID_Head_1 = Hood1;
		this.ID_Head_2 = Hood2;
	}
	
	public _dress(int cloak, int rhand,int lhand, int chest,int legs, int gloves, int feet, int idDressme, int Hood1, int Hood2){
		this.ID_Right_Hand=rhand;
		this.ID_Left_Hand=lhand;
		this.ID_Chest=chest;
		this.ID_Legs=legs;
		this.ID_Gloves=gloves;
		this.ID_Feet=feet;
		this.ID_Cloak = cloak;
		this.ID_Dressme=idDressme;
		this.isTry = false;
		this.isFullArmorDressme = false;
		this.isSemiFullArmorDressme = false;
		this.isArmorTryDressme = false;
		this.FullArmorDressme_ID = -1;
		this.isWeapon = false;
		this.isCloak = false;
		this.ID_Head_1 = Hood1;
		this.ID_Head_2 = Hood2;		
	}
	
	public boolean isFullDress(){
		return ( this.ID_Chest == this.ID_Feet ) && (this.ID_Chest == this.ID_Gloves) && (this.ID_Chest == this.ID_Gloves) && ( this.ID_Chest == this.ID_Legs );
	}
	
	public boolean isFullChest(){
		return (this.ID_Chest == this.ID_Legs) && ( this.ID_Chest != this.ID_Gloves ) && (this.ID_Chest != this.ID_Feet);
	}
	
	public boolean isFullDressWeapon(){
		return (this.ID_Right_Hand == this.ID_Left_Hand);
	}
	
	public boolean isDeco(int idSlot) {
		switch(idSlot) {
			case Inventory.PAPERDOLL_HAIR:
			case Inventory.PAPERDOLL_HAIR2:
			return true;
		}
		return false;
	}
	
	public boolean isArmor(int idSlot){
		switch(idSlot){
			case Inventory.PAPERDOLL_GLOVES:
			case Inventory.PAPERDOLL_CHEST:
			case Inventory.PAPERDOLL_LEGS:
			case Inventory.PAPERDOLL_FEET:
				return true;
		}
		return false;
	}
	
	public boolean isCloak(int idSlot){
		return (Inventory.PAPERDOLL_CLOAK == idSlot);
	}
	
	public boolean setTry(int IDSlot, int IdItem){
		return setTry(IDSlot, IdItem, -100);
	}
	public boolean setTry(int IDSlot, int IdItem, int DecoID){
		if(this.isTry){
			return false;
		}
		this.isTry=true;
		
		try{
			L2Item vItem = ItemData.getInstance().getTemplate(IdItem);
			this.isFullArmorDressme = (vItem.getBodyPart() == L2Item.SLOT_ALLDRESS ? true : false);
			this.isSemiFullArmorDressme = (vItem.getBodyPart() == L2Item.SLOT_FULL_ARMOR ? true : false);
			this.isArmorTryDressme = isArmor(IDSlot);
			this.FullArmorDressme_ID = IdItem;
			if(vItem.getBodyPart() == L2Item.SLOT_R_HAND || vItem.getBodyPart() == L2Item.SLOT_L_HAND || vItem.getBodyPart() == L2Item.SLOT_LR_HAND) {
				this.isWeapon = true;
			}else {
				this.isWeapon = false;
			}
			if(vItem.getBodyPart() == L2Item.SLOT_BACK) {
				this.isCloak = true;
			}else {
				this.isCloak = false;
			}
			
			this.ID_Deco_Try = DecoID;
		}catch(Exception a){
			
		}
		
		ID_Dressme_Try.put(IDSlot, IdItem);
		ThreadPoolManager.getInstance().scheduleGeneral(new DressmeTryClock(this),5000);
		return true;
	}
	
	public boolean isTrySet(){
		return this.isTry;
	}
	
	private boolean isFullArmor(int idChestArmor){
		try{
			if(this.ID_Chest>0){
				L2Item vItem = ItemData.getInstance().getTemplate(idChestArmor);
				if(vItem.getBodyPart() == L2Item.SLOT_ALLDRESS){
					return true;
				}
			}
		}catch(Exception a){
			
		}
		return false;		
	}
	
	private boolean isFullArmor(){
		return isFullArmor(this.ID_Chest);
	}
	
	public int getIds(int idSlot, boolean isFromCharInfo, boolean isFullDress){
		if(isFromCharInfo){
			if(this.isTry){
				if(idSlot == Inventory.PAPERDOLL_HAIR || idSlot == Inventory.PAPERDOLL_HAIR2){
					if(this.ID_Deco_Try<=0 && !(this.isWeapon || this.isCloak)) {
						return -100;
					}
					if((this.ID_Head_1 > 0 && this.ID_Head_2 > 0) && this.ID_Deco_Try <=0) {
						return this.ID_Head_1;
					}
					return this.ID_Deco_Try;
				}				
				if(ID_Dressme_Try!=null){
					if(ID_Dressme_Try.containsKey(idSlot)){
						return ID_Dressme_Try.get(idSlot);
					}else{
						if(idSlot == Inventory.PAPERDOLL_HAIR || idSlot == Inventory.PAPERDOLL_HAIR2){
							return this.ID_Head_1;
						}						
						if(this.isArmorTryDressme){
							if(this.isFullArmorDressme || ( this.isSemiFullArmorDressme && idSlot == Inventory.PAPERDOLL_LEGS) ){
								switch(idSlot){
									case Inventory.PAPERDOLL_GLOVES:
									case Inventory.PAPERDOLL_CHEST:
						  			case Inventory.PAPERDOLL_LEGS:
						  			case Inventory.PAPERDOLL_FEET:
						  				if(isFullDress) {
						  					return this.FullArmorDressme_ID;
						  				}else {
						  					return -100;
						  				}
						  			case Inventory.PAPERDOLL_HAIR:
						  			case Inventory.PAPERDOLL_HAIR2:
						  				return -100;
								}
							}else if(isFullArmor()){
								switch(idSlot){
									case Inventory.PAPERDOLL_GLOVES:
									case Inventory.PAPERDOLL_CHEST:
						  			case Inventory.PAPERDOLL_LEGS:
						  			case Inventory.PAPERDOLL_FEET:
						  			case Inventory.PAPERDOLL_HAIR:
						  			case Inventory.PAPERDOLL_HAIR2:						  				
						  				return -100;
								}
							}
						}
					}
				}
			}
		}
		
		if(isFullArmor()){
			switch(idSlot){
				case Inventory.PAPERDOLL_GLOVES:
				case Inventory.PAPERDOLL_CHEST:
		  		case Inventory.PAPERDOLL_LEGS:
		  		case Inventory.PAPERDOLL_FEET:
		  			return this.ID_Chest;
		  		case Inventory.PAPERDOLL_HAIR:
		  		case Inventory.PAPERDOLL_HAIR2:
	  				if(this.ID_Head_1<=0 && this.ID_Head_2<=0) {
	  					return -100;
	  				}
	  				return this.ID_Head_1;
			}	
		}
		
		switch(idSlot){
			case Inventory.PAPERDOLL_GLOVES:
		  		return this.ID_Gloves;
		  	case Inventory.PAPERDOLL_CHEST:
		  		return this.ID_Chest;
		  	case Inventory.PAPERDOLL_LEGS:
		  		return this.ID_Legs;
		  	case Inventory.PAPERDOLL_FEET:
		  		return this.ID_Feet;
		  	case Inventory.PAPERDOLL_RHAND:
		  		return this.ID_Right_Hand;
		  	case Inventory.PAPERDOLL_LHAND:
		  		return this.ID_Left_Hand;
		  	case Inventory.PAPERDOLL_CLOAK:
		  		return this.ID_Cloak;
		  	case Inventory.PAPERDOLL_HAIR:
		  	case Inventory.PAPERDOLL_HAIR2:
		  		return this.ID_Head_1;
		}
	  return -100;
	}
	
	public int getIds(BodyPart part){
		switch (part) {
			case R_HAND:
				return this.ID_Right_Hand;
			case L_HAND:
				return this.ID_Left_Hand;
			case CHEST:
				return this.ID_Chest;
			case LEGS:
				return this.ID_Legs;
			case GLOVES:
				return this.ID_Gloves;
			case FEET:
				return this.ID_Feet;
			case ID_DRESSME:
				return this.ID_Dressme;
			case CLOAK:
				return this.ID_Cloak;
			case HEAD_1:
			case HEAD_2:				
				return this.ID_Head_1;
		}
		return -100;
	}
	
	public void cleanAllDressme(){
		resetDressme();
		resetDressmeWeapon();
	}
	
	public void SetDress(BodyPart part, int ID){
		switch (part) {
			case R_HAND:
				this.ID_Right_Hand=ID;
				break;
			case L_HAND:
				this.ID_Left_Hand=ID;
				break;
			case CHEST:
				this.ID_Chest=ID;
				break;
			case LEGS:
				this.ID_Legs=ID;
				break;
			case GLOVES:
				this.ID_Gloves=ID;
				break;
			case FEET:
				this.ID_Feet=ID;
				break;
			case ID_DRESSME:
				this.ID_Dressme=ID;
				break;
			case CLOAK:
				this.ID_Cloak = ID;
				break;
			case HEAD_1:
			case HEAD_2:
				this.ID_Head_1 = ID;
				this.ID_Head_2 = ID;
				break;
		}
	}

	public void SetDress(int slot, int idItem) {
		L2Item vItemNew = ItemData.getInstance().getTemplate(idItem);
		L2Item vItemChestUsing = null;
		L2Item vItemWeaponUsing = null;
		if(this.ID_Chest>0){
			vItemChestUsing = ItemData.getInstance().getTemplate(this.ID_Chest);
		}
		if(this.ID_Right_Hand>0){
			vItemWeaponUsing = ItemData.getInstance().getTemplate(this.ID_Right_Hand);
		}
		boolean reset = false;
		switch(slot){
			case Inventory.PAPERDOLL_RHAND:
				if(idItem==0){
					if(vItemWeaponUsing!=null){
						if(vItemWeaponUsing.getBodyPart() == L2Item.SLOT_LR_HAND){
							reset = true;
						}
					}
				}else{
					if(vItemNew !=null){
						if(vItemNew.getBodyPart() == L2Item.SLOT_LR_HAND){
							reset = true;
						}else if(vItemWeaponUsing!=null){
							if(vItemWeaponUsing.getBodyPart() == L2Item.SLOT_LR_HAND){
								this.ID_Left_Hand = 0;
								this.ID_Right_Hand = 0;
							}
						}
					}
				}
				if(reset){
					this.ID_Right_Hand = idItem;
					this.ID_Left_Hand = idItem;
				}else{
					this.ID_Right_Hand = idItem;
				}
				break;
			case Inventory.PAPERDOLL_LHAND:
				if(vItemWeaponUsing!=null){
					if(vItemWeaponUsing.getBodyPart() == L2Item.SLOT_LR_HAND){
						resetDressmeWeapon();
					}
				}				
				this.ID_Left_Hand = idItem;
				break;		
			case Inventory.PAPERDOLL_CHEST:
				if(idItem==0){
					if(vItemChestUsing!=null){
						if(vItemChestUsing.getBodyPart() == L2Item.SLOT_ALLDRESS){
							reset = true;
						}else if(vItemChestUsing.getBodyPart() == L2Item.SLOT_FULL_ARMOR){
							this.ID_Legs = 0;
						}
					}
				}else{
					if(vItemNew!=null){
						if(vItemNew.getBodyPart() == L2Item.SLOT_ALLDRESS){
							reset = true;
						}else if(vItemNew.getBodyPart() == L2Item.SLOT_FULL_ARMOR){
							this.ID_Legs = idItem;
						}else if(vItemChestUsing!=null){
							if(vItemChestUsing.getBodyPart() == L2Item.SLOT_ALLDRESS){
								resetDressme();
							}else if(vItemChestUsing.getBodyPart() == L2Item.SLOT_FULL_ARMOR){
								resetDressme(false);
							}
						}
					}
				}
				if(reset){
					this.ID_Chest = idItem;
					this.ID_Feet = idItem;
					this.ID_Gloves = idItem;
					this.ID_Legs = idItem;					
				}else{
					this.ID_Chest = idItem;
				}
				break;
			case Inventory.PAPERDOLL_LEGS:
				if(vItemChestUsing!=null){
					if(vItemChestUsing.getBodyPart() == L2Item.SLOT_ALLDRESS){
						resetDressme();
					}else if(vItemChestUsing.getBodyPart() == L2Item.SLOT_FULL_ARMOR){
						this.ID_Chest=0;
						this.ID_Legs=0;
					}
				}
				this.ID_Legs = idItem;
				break;
			case Inventory.PAPERDOLL_GLOVES:
				if(vItemChestUsing!=null){
					if(vItemChestUsing.getBodyPart() == L2Item.SLOT_ALLDRESS){
						resetDressme();
					}
				}				
				this.ID_Gloves = idItem;
				break;
			case Inventory.PAPERDOLL_FEET:
				if(vItemChestUsing!=null){
					if(vItemChestUsing.getBodyPart() == L2Item.SLOT_ALLDRESS){
						resetDressme();
					}
				}				
				this.ID_Feet = idItem;
				break;
			case Inventory.PAPERDOLL_CLOAK:
				this.ID_Cloak = idItem;
				break;
			case Inventory.PAPERDOLL_HAIR:
			case Inventory.PAPERDOLL_HAIR2:
				this.ID_Head_1 = idItem;
				this.ID_Head_2 = idItem;
				break;
		}
	}
	
	public void setDecos(int idItem) {
		this.ID_Head_1 = idItem;
		this.ID_Head_2 = idItem;
	}
	
	private void resetDressme(){
		resetDressme(true);
	}
	
	private void resetDressme(boolean All){
		this.ID_Chest=0;
		this.ID_Legs=0;
		if(All){
			this.ID_Feet=0;
			this.ID_Gloves=0;
			this.ID_Cloak=0;
			this.ID_Head_1 = -100;
			this.ID_Head_2 = -100;		
		}
	}
	private void resetDressmeWeapon(){
		this.ID_Right_Hand=0;
		this.ID_Left_Hand=0;
	}
	
	public static class DressmeTryClock implements Runnable{
		L2PcInstance activeChar;
		int CodigoEntrada;
		_dress _Dressme;
		public DressmeTryClock(_dress Dressme){
			_Dressme = Dressme;
		}
		@Override
		public void run(){
			try{
				this._Dressme.isTry=false;
				this._Dressme.ID_Dressme_Try.clear();
				this._Dressme.isFullArmorDressme = false;
				this._Dressme.isSemiFullArmorDressme = false;
				this._Dressme.FullArmorDressme_ID = -1;
			}catch(Exception a){

			}

		}
	}
	
	
}
