package ZeuS.Config;

import ZeuS.interfase.central;
import ZeuS.procedimientos.opera;

public class _InfoGeneralDrop{
	private final int ITEM_ID;
	private final double ITEM_CHANCE;
	private String ITEM_CHANCE_STR;
	private final int ITEM_MIN;
	private final int ITEM_MAX;
	private final boolean ITEM_IS_DROP;
	private final boolean ITEM_IS_QUEST;
	private final int MOD_ID;
	private final int CATEGORY_TYPE;
	public _InfoGeneralDrop(int IdItem, double ChanceItem, int MinItem, int MaxItem, int IdMob, boolean isDrop, int CategoryType, boolean isQuest){
		this.ITEM_ID = IdItem;
		this.ITEM_CHANCE = ChanceItem;
		this.ITEM_MIN = MinItem;
		this.ITEM_MAX = MaxItem;
		this.ITEM_IS_DROP = isDrop;
		this.MOD_ID = IdMob;
		String Chance = opera.getFormatNumbers(ChanceItem / 10000);
		this.ITEM_CHANCE_STR =  Chance;
		this.CATEGORY_TYPE = CategoryType;
		this.ITEM_IS_QUEST = isQuest;
	}
	
	public final boolean isQuest(){
		return this.ITEM_IS_QUEST;
	}
	
	public final String getCategoryTypeSTR(){
		return String.valueOf(this.CATEGORY_TYPE);
	}
	
	public final int getIdItem(){
		return this.ITEM_ID;
	}
	
	public final double getChance(){
		return (this.ITEM_CHANCE/1000000)*100;
	}
	
	public final String getChanceSTR(){
		return this.ITEM_CHANCE_STR;
	}
	
	public final int getMin(){
		return this.ITEM_MIN;
	}

	public final int getMax(){
		return this.ITEM_MAX;
	}
	
	public final boolean isDrop(){
		return this.ITEM_IS_DROP;
	}
	
	public final String getItemName(){
		return central.getNombreITEMbyID(this.ITEM_ID);		
	}
	
	public final String getItemIcon(){
		return opera.getTemplateItem(this.ITEM_ID);		
	}
}