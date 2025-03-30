/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package l2r.gameserver.model.multisell;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import l2r.Config;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.model.StatsSet;
import l2r.gameserver.model.items.L2Armor;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.L2Weapon;
import l2r.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author DS
 */
public class Ingredient
{
	private int _itemId;
	private final int _EnchantmentLevel;
	private long _itemCount;
	private boolean _isTaxIngredient;
	private boolean _maintainIngredient;
	private L2Item _template = null;
	private ItemInfo _itemInfo = null;
	
	protected static final Logger _log = LoggerFactory.getLogger(Ingredient.class);
	
	private final boolean _updateAdena;
	
	public Ingredient(StatsSet set)
	{
		this(set.getInt("id"), set.getLong("count"), set.getInt("enchantmentLevel", 0), set.getBoolean("isTaxIngredient", false), set.getBoolean("maintainIngredient", false), set.getBoolean("updateAdena", true));
	}
	
	// public Ingredient(int itemId, long itemCount, boolean isTaxIngredient, boolean maintainIngredient)
	public Ingredient(int itemId, long itemCount, int EnchantmentLevel, boolean isTaxIngredient, boolean maintainIngredient, boolean updateAdena)
	{
		int ValorAgregado = 0;
		_itemId = itemId;
		_EnchantmentLevel = EnchantmentLevel;
		_isTaxIngredient = isTaxIngredient;
		_maintainIngredient = maintainIngredient;
		_updateAdena = updateAdena;
		if (itemId == 57)
		{
			try{
				if (Config.ZEUS_VALOR_AUMENTADO > 0)
				{
					Long Price = Long.valueOf(itemCount);
					Long Interes = Long.valueOf((Price.longValue() * Config.ZEUS_VALOR_AUMENTADO) / 100L);
					ValorAgregado = Interes.intValue();
				}
			}catch(Exception a){
				_log.warn("Ingredient ZeuS Engine Value plus Wrog for ->" + itemId + ", error: " + a.getMessage());
			}
		}
		_itemCount = (ValorAgregado <= 0 ? itemCount : itemCount + ValorAgregado);
		if (_itemId > 0)
		{
			_template = ItemData.getInstance().getTemplate(_itemId);
		}
	}
	
	/**
	 * @return a new Ingredient instance with the same values as this.
	 */
	public Ingredient getCopy()
	{
		// return new Ingredient(_itemId, _itemCount, _isTaxIngredient, _maintainIngredient);
		return new Ingredient(_itemId, _itemCount, _EnchantmentLevel, _isTaxIngredient, _maintainIngredient, _updateAdena);
	}
	
	public final L2Item getTemplate()
	{
		return _template;
	}
	
	public final void setItemInfo(L2ItemInstance item)
	{
		_itemInfo = new ItemInfo(item);
	}
	
	public final void setItemInfo(ItemInfo info)
	{
		_itemInfo = info;
	}
	
	public final ItemInfo getItemInfo()
	{
		return _itemInfo;
	}
	
	public final int getEnchantLevel()
	{
		// return _itemInfo != null ? _itemInfo.getEnchantLevel() : 0;
		return _itemInfo == null ? _EnchantmentLevel : _itemInfo.getEnchantLevel();
	}
	
	public final void setItemId(int itemId)
	{
		_itemId = itemId;
	}
	
	public final int getId()
	{
		return _itemId;
	}
	
	public final void setItemCount(long itemCount)
	{
		_itemCount = itemCount;
	}
	
	public final long getItemCount()
	{
		return _itemCount;
	}
	
	public final void setIsTaxIngredient(boolean isTaxIngredient)
	{
		_isTaxIngredient = isTaxIngredient;
	}
	
	public final boolean isTaxIngredient()
	{
		return _isTaxIngredient;
	}
	
	public final void setMaintainIngredient(boolean maintainIngredient)
	{
		_maintainIngredient = maintainIngredient;
	}
	
	public final boolean getMaintainIngredient()
	{
		return _maintainIngredient;
	}
	
	public final boolean isStackable()
	{
		return _template == null ? true : _template.isStackable();
	}
	
	public final boolean isArmorOrWeapon()
	{
		return _template == null ? false : (_template instanceof L2Armor) || (_template instanceof L2Weapon);
	}
	
	public final int getWeight()
	{
		return _template == null ? 0 : _template.getWeight();
	}
}