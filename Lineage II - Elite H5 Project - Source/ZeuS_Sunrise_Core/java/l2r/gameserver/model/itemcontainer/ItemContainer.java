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
package l2r.gameserver.model.itemcontainer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l2r.Config;
import l2r.L2DatabaseFactory;
import l2r.gameserver.GameTimeController;
import l2r.gameserver.data.xml.impl.ItemData;
import l2r.gameserver.enums.ItemLocation;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.L2Character;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.L2Item;
import l2r.gameserver.model.items.instance.L2ItemInstance;
import l2r.gameserver.network.serverpackets.InventoryUpdate;

import gr.sr.configsEngine.configs.impl.CustomServerConfigs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Advi
 */
public abstract class ItemContainer
{
	protected static final Logger _log = LoggerFactory.getLogger(ItemContainer.class);
	
	protected final List<L2ItemInstance> _items = new CopyOnWriteArrayList<>();
	
	protected ItemContainer()
	{
	}
	
	protected abstract L2Character getOwner();
	
	protected abstract ItemLocation getBaseLocation();
	
	public String getName()
	{
		return "ItemContainer";
	}
	
	/**
	 * @return int the owner object Id
	 */
	public int getOwnerId()
	{
		return getOwner() == null ? 0 : getOwner().getObjectId();
	}
	
	/**
	 * @return the quantity of items in the inventory
	 */
	public int getSize()
	{
		return _items.size();
	}
	
	/**
	 * @return the items in inventory
	 */
	public L2ItemInstance[] getItems()
	{
		return _items.toArray(new L2ItemInstance[_items.size()]);
	}
	
	/**
	 * @return the items in inventory
	 */
	public List<L2ItemInstance> getItemsWithoutQuest()
	{
		List<L2ItemInstance> list = new LinkedList<>();
		for (L2ItemInstance item : _items)
		{
			if (!item.isQuestItem())
			{
				list.add(item);
			}
		}
		return list;
	}
	
	/**
	 * @param itemId the item Id
	 * @return the item from inventory by itemId
	 */
	public L2ItemInstance getItemByItemId(int itemId)
	{
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getId() == itemId))
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * @return true if player got item for self resurrection
	 */
	public final boolean haveItemForSelfResurrection()
	{
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getItem().isAllowSelfResurrection()))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * @param itemId the item Id
	 * @return the items list from inventory by using its itemId
	 */
	public List<L2ItemInstance> getItemsByItemId(int itemId)
	{
		final List<L2ItemInstance> returnList = new LinkedList<>();
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getId() == itemId))
			{
				returnList.add(item);
			}
		}
		return returnList;
	}
	
	/**
	 * @param itemId the item Id
	 * @param itemToIgnore used during the loop, to avoid returning the same item
	 * @return the item from inventory by itemId
	 */
	public L2ItemInstance getItemByItemId(int itemId, L2ItemInstance itemToIgnore)
	{
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getId() == itemId) && !item.equals(itemToIgnore))
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * @param objectId the item object Id
	 * @return item from inventory by objectId
	 */
	public L2ItemInstance getItemByObjectId(int objectId)
	{
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getObjectId() == objectId))
			{
				return item;
			}
		}
		return null;
	}
	
	/**
	 * Gets the inventory item count by item Id and enchant level including equipped items.
	 * @param itemId the item Id
	 * @param enchantLevel the item enchant level, use -1 to match any enchant level
	 * @return the inventory item count
	 */
	public long getInventoryItemCount(int itemId, int enchantLevel)
	{
		return getInventoryItemCount(itemId, enchantLevel, true);
	}
	
	/**
	 * Gets the inventory item count by item Id and enchant level, may include equipped items.
	 * @param itemId the item Id
	 * @param enchantLevel the item enchant level, use -1 to match any enchant level
	 * @param includeEquipped if {@code true} includes equipped items in the result
	 * @return the inventory item count
	 */
	public long getInventoryItemCount(int itemId, int enchantLevel, boolean includeEquipped)
	{
		long count = 0;
		
		for (L2ItemInstance item : _items)
		{
			if ((item.getId() == itemId) && ((item.getEnchantLevel() == enchantLevel) || (enchantLevel < 0)) && (includeEquipped || !item.isEquipped()))
			{
				if (item.isStackable())
				{
					// FIXME: Zoey76: if there are more than one stacks of the same item Id
					// it will return the count of the last one, if is not possible to
					// have more than one stacks of the same item Id,
					// it will continue iterating over all items
					// possible fixes:
					// count += item.getCount();
					// or
					// count = item.getCount();
					// break;
					count = item.getCount();
				}
				else
				{
					count++;
				}
			}
		}
		return count;
	}
	
	public L2ItemInstance addItem(String process, L2ItemInstance item, L2PcInstance actor, Object reference){
			return addItem(process, item, actor, reference, false);
	}	
	
	/**
	 * Adds item to inventory
	 * @param process : String Identifier of process triggering this action
	 * @param item : L2ItemInstance to be added
	 * @param actor : L2PcInstance Player requesting the item add
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new item or the updated item in inventory
	 */
	public L2ItemInstance addItem(String process, L2ItemInstance item, L2PcInstance actor, Object reference, boolean isFromZeuS)
	{
		L2ItemInstance olditem = getItemByItemId(item.getId());
		
		// If stackable item is found in inventory just add to current quantity
		if ((olditem != null) && olditem.isStackable() && !isFromZeuS)
		{
			long count = item.getCount();
			olditem.changeCount(process, count, actor, reference);
			olditem.setLastChange(L2ItemInstance.MODIFIED);
			
			// And destroys the item
			ItemData.getInstance().destroyItem(process, item, actor, reference);
			item.updateDatabase();
			item = olditem;
			
			// Updates database
			float adenaRate = Config.RATE_DROP_ITEMS_ID.getOrDefault(Inventory.ADENA_ID, 1f);
			if ((item.getId() == Inventory.ADENA_ID) && (count < (10000 * adenaRate)))
			{
				// Small adena changes won't be saved to database all the time
				if ((GameTimeController.getInstance().getGameTicks() % 5) == 0)
				{
					item.updateDatabase();
				}
			}
			else
			{
				item.updateDatabase();
			}
		}
		// If item hasn't be found in inventory, create new one
		else
		{
			item.setOwnerId(process, ( isFromZeuS ? item.getZeuSNewOwner() : getOwnerId() ), actor, reference);
			item.setItemLocation(getBaseLocation());
			item.setLastChange((L2ItemInstance.ADDED));
			
			// Add item in inventory
			addItem(item);
			
			// Updates database
			item.updateDatabase();
		}
		
		refreshWeight();
		return item;
	}
	
	/**
	 * Adds item to inventory
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item Identifier of the item to be added
	 * @param count : int Quantity of items to be added
	 * @param actor : L2PcInstance Player requesting the item add
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new item or the updated item in inventory
	 */
	public L2ItemInstance addItem(String process, int itemId, long count, L2PcInstance actor, Object reference)
	{
		L2ItemInstance item = getItemByItemId(itemId);
		
		// If stackable item is found in inventory just add to current quantity
		if ((item != null) && item.isStackable())
		{
			item.changeCount(process, count, actor, reference);
			item.setLastChange(L2ItemInstance.MODIFIED);
			// Updates database
			// If Adena drop rate is not present it will be x1.
			float adenaRate = Config.RATE_DROP_ITEMS_ID.getOrDefault(Inventory.ADENA_ID, 1f);
			if ((itemId == Inventory.ADENA_ID) && (count < (10000 * adenaRate)))
			{
				// Small adena changes won't be saved to database all the time
				if ((GameTimeController.getInstance().getGameTicks() % 5) == 0)
				{
					item.updateDatabase();
				}
			}
			else
			{
				item.updateDatabase();
			}
			
			// Send inventory update packet
			if (actor != null)
			{
				if (!Config.FORCE_INVENTORY_UPDATE)
				{
					final InventoryUpdate iu = new InventoryUpdate();
					iu.addItem(item);
					actor.sendInventoryUpdate(iu);
				}
				else
				{
					actor.sendItemList(false);
				}
			}
		}
		// If item hasn't be found in inventory, create new one
		else
		{
			for (int i = 0; i < count; i++)
			{
				L2Item template = ItemData.getInstance().getTemplate(itemId);
				if (template == null)
				{
					_log.warn((actor != null ? "[" + actor.getName() + "] " : "") + "Invalid ItemId requested: ", itemId);
					return null;
				}
				
				item = ItemData.getInstance().createItem(process, itemId, template.isStackable() ? count : 1, actor, reference);
				item.setOwnerId(getOwnerId());
				item.setItemLocation(getBaseLocation());
				item.setLastChange(L2ItemInstance.ADDED);
				item.setEnchantLevel(template.getDefaultEnchantLevel());
				
				// Add item in inventory
				addItem(item);
				// Updates database
				item.updateDatabase();
				
				// Send inventory update packet
				if (actor != null)
				{
					if (!Config.FORCE_INVENTORY_UPDATE)
					{
						final InventoryUpdate iu = new InventoryUpdate();
						iu.addItem(item);
						actor.sendInventoryUpdate(iu);
					}
					else
					{
						actor.sendItemList(false);
					}
				}
				
				// If stackable, end loop as entire count is included in 1 instance of item
				if ((template.isStackable() || !Config.MULTIPLE_ITEM_DROP))
				{
					break;
				}
			}
		}
		
		refreshWeight();
		return item;
	}
	
	/**
	 * Transfers item to another inventory
	 * @param process string Identifier of process triggering this action
	 * @param objectId Item Identifier of the item to be transfered
	 * @param count Quantity of items to be transfered
	 * @param target the item container where the item will be moved.
	 * @param actor Player requesting the item transfer
	 * @param reference Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the new item or the updated item in inventory
	 */
	
	public L2ItemInstance transferItem(String process, int objectId, long count, ItemContainer target, L2PcInstance actor, Object reference)
	{
		return transferItem(process, objectId, count, target, actor, reference, true);
	}
	
	public L2ItemInstance transferItem(String process, int objectId, long count, ItemContainer target, L2PcInstance actor, Object reference, boolean removeItem)
	{
		if (target == null)
		{
			return null;
		}
		
		L2ItemInstance sourceitem = getItemByObjectId(objectId);
		if (sourceitem == null)
		{
			return null;
		}
		L2ItemInstance targetitem = sourceitem.isStackable() ? target.getItemByItemId(sourceitem.getId()) : null;
		
		synchronized (sourceitem)
		{
			// check if this item still present in this container
			if (getItemByObjectId(objectId) != sourceitem)
			{
				return null;
			}
			
			// Check if requested quantity is available
			if (count > sourceitem.getCount())
			{
				count = sourceitem.getCount();
			}
			
			// If possible, move entire item object
			if ((sourceitem.getCount() == count) && (targetitem == null))
			{
				if (removeItem)
				{
					removeItem(sourceitem);
				}
				target.addItem(process, sourceitem, actor, reference);
				targetitem = sourceitem;
			}
			else
			{
				if (sourceitem.getCount() > count) // If possible, only update counts
				{
					if (removeItem)
					{
						sourceitem.changeCount(process, -count, actor, reference);
					}
				}
				else
				// Otherwise destroy old item
				{
					if (removeItem)
					{
						removeItem(sourceitem);
						ItemData.getInstance().destroyItem(process, sourceitem, actor, reference);
					}
				}
				
				if (targetitem != null) // If possible, only update counts
				{
					targetitem.changeCount(process, count, actor, reference);
				}
				else
				// Otherwise add new item
				{
					targetitem = target.addItem(process, sourceitem.getId(), count, actor, reference);
				}
			}
			
			// Updates database
			sourceitem.updateDatabase(true);
			if ((targetitem != sourceitem) && (targetitem != null))
			{
				targetitem.updateDatabase();
			}
			if (sourceitem.isAugmented())
			{
				sourceitem.getAugmentation().removeBonus(actor);
			}
			refreshWeight();
			target.refreshWeight();
		}
		return targetitem;
	}
	
	/**
	 * Destroy item from inventory and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param item : L2ItemInstance to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItem(String process, L2ItemInstance item, L2PcInstance actor, Object reference)
	{
		return this.destroyItem(process, item, item.getCount(), actor, reference);
	}
	
	/**
	 * Destroy item from inventory and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param item : L2ItemInstance to be destroyed
	 * @param count
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItem(String process, L2ItemInstance item, long count, L2PcInstance actor, Object reference)
	{
		synchronized (item)
		{
			// Adjust item quantity
			if (item.getCount() > count)
			{
				item.changeCount(process, -count, actor, reference);
				item.setLastChange(L2ItemInstance.MODIFIED);
				
				// don't update often for untraced items
				if ((process != null) || ((GameTimeController.getInstance().getGameTicks() % 10) == 0))
				{
					item.updateDatabase();
				}
				
				refreshWeight();
			}
			else
			{
				if (item.getCount() < count)
				{
					return null;
				}
				
				boolean removed = removeItem(item);
				if (!removed)
				{
					return null;
				}
				
				ItemData.getInstance().destroyItem(process, item, actor, reference);
				
				item.updateDatabase();
				refreshWeight();
			}
			item.deleteMe();
		}
		return item;
	}
	
	/**
	 * Destroy item from inventory by using its <B>objectID</B> and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param objectId : int Item Instance identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItem(String process, int objectId, long count, L2PcInstance actor, Object reference)
	{
		L2ItemInstance item = getItemByObjectId(objectId);
		if (item == null)
		{
			return null;
		}
		return this.destroyItem(process, item, count, actor, reference);
	}
	
	/**
	 * Destroy item from inventory by using its <B>itemId</B> and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param itemId : int Item identifier of the item to be destroyed
	 * @param count : int Quantity of items to be destroyed
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 * @return L2ItemInstance corresponding to the destroyed item or the updated item in inventory
	 */
	public L2ItemInstance destroyItemByItemId(String process, int itemId, long count, L2PcInstance actor, Object reference)
	{
		L2ItemInstance item = getItemByItemId(itemId);
		if (item == null)
		{
			return null;
		}
		return destroyItem(process, item, count, actor, reference);
	}
	
	/**
	 * Destroy all items from inventory and updates database
	 * @param process : String Identifier of process triggering this action
	 * @param actor : L2PcInstance Player requesting the item destroy
	 * @param reference : Object Object referencing current action like NPC selling item or previous item in transformation
	 */
	public void destroyAllItems(String process, L2PcInstance actor, Object reference)
	{
		for (L2ItemInstance item : _items)
		{
			if (item != null)
			{
				destroyItem(process, item, actor, reference);
			}
		}
	}
	
	/**
	 * @return warehouse Adena.
	 */
	public long getAdena()
	{
		long count = 0;
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getId() == Inventory.ADENA_ID))
			{
				count = item.getCount();
				return count;
			}
		}
		return count;
	}
	
	public long getFAdena()
	{
		long count = 0;
		
		for (L2ItemInstance item : _items)
		{
			if ((item != null) && (item.getId() == CustomServerConfigs.ALTERNATE_PAYMENT_ID))
			{
				count = item.getCount();
				return count;
			}
		}
		return count;
	}
	
	/**
	 * Adds item to inventory for further adjustments.
	 * @param item : L2ItemInstance to be added from inventory
	 */
	protected void addItem(L2ItemInstance item)
	{
		_items.add(item);
	}
	
	/**
	 * Removes item from inventory for further adjustments.
	 * @param item : L2ItemInstance to be removed from inventory
	 * @return
	 */
	protected boolean removeItem(L2ItemInstance item)
	{
		return _items.remove(item);
	}
	
	/**
	 * Refresh the weight of equipment loaded
	 */
	protected void refreshWeight()
	{
	}
	
	/**
	 * Delete item object from world
	 */
	public void deleteMe()
	{
		if (getOwner() != null)
		{
			for (L2ItemInstance item : _items)
			{
				if (item != null)
				{
					item.updateDatabase(true);
					item.deleteMe();
					L2World.getInstance().removeObject(item);
				}
			}
		}
		_items.clear();
	}
	
	/**
	 * Update database with items in inventory
	 */
	public void updateDatabase()
	{
		if (getOwner() != null)
		{
			for (L2ItemInstance item : _items)
			{
				if (item != null)
				{
					item.updateDatabase(true);
				}
			}
		}
	}
	
	/**
	 * Get back items in container from database
	 */
	public void restore()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time FROM items WHERE owner_id=? AND (loc=?)"))
		{
			statement.setInt(1, getOwnerId());
			statement.setString(2, getBaseLocation().name());
			try (ResultSet inv = statement.executeQuery())
			{
				L2ItemInstance item;
				while (inv.next())
				{
					item = L2ItemInstance.restoreFromDb(getOwnerId(), inv);
					if (item == null)
					{
						continue;
					}
					
					L2World.getInstance().storeObject(item);
					
					L2PcInstance owner = getOwner() == null ? null : getOwner().getActingPlayer();
					
					// If stackable item is found in inventory just add to current quantity
					if (item.isStackable() && (getItemByItemId(item.getId()) != null))
					{
						addItem("Restore", item, owner, null);
					}
					else
					{
						addItem(item);
					}
				}
			}
			refreshWeight();
		}
		catch (Exception e)
		{
			_log.warn("could not restore container:", e);
		}
	}
	
	public boolean validateCapacity(long slots)
	{
		return true;
	}
	
	public boolean validateWeight(long weight)
	{
		return true;
	}
	
	/**
	 * If the item is stackable validates 1 slot, if the item isn't stackable validates the item count.
	 * @param itemId the item Id to verify
	 * @param count amount of item's weight to validate
	 * @return {@code true} if the item doesn't exists or it validates its slot count
	 */
	public boolean validateCapacityByItemId(int itemId, long count)
	{
		final L2Item template = ItemData.getInstance().getTemplate(itemId);
		return (template == null) || (template.isStackable() ? validateCapacity(1) : validateCapacity(count));
	}
	
	/**
	 * @param itemId the item Id to verify
	 * @param count amount of item's weight to validate
	 * @return {@code true} if the item doesn't exists or it validates its weight
	 */
	public boolean validateWeightByItemId(int itemId, long count)
	{
		final L2Item template = ItemData.getInstance().getTemplate(itemId);
		return (template == null) || validateWeight(template.getWeight() * count);
	}

    public boolean destroyItemBoolean(String process, int objectId, long count, L2PcInstance actor, Object reference)
    {
        L2ItemInstance item = getItemByObjectId(objectId);
        if (item == null)
        {
            return false;
        }
        boolean ok = this.destroyItemBoolean(process, item, count, actor, reference);
        return ok;
    }

    public boolean destroyItemBoolean(String process, L2ItemInstance item, long count, L2PcInstance actor, Object reference)
    {
        synchronized (item)
        {
            // Adjust item quantity
            if (item.getCount() > count)
            {
                item.changeCount(process, -count, actor, reference);
                item.setLastChange(L2ItemInstance.MODIFIED);

                // don't update often for untraced items
                if ((process != null) || ((GameTimeController.getInstance().getGameTicks() % 10) == 0))
                {
                    item.updateDatabase();
                }

                refreshWeight();
            }
            else
            {
                if (item.getCount() < count)
                {
                    return false;
                }

                boolean removed = removeItem(item);
                if (!removed)
                {
                    return false;
                }

                ItemData.getInstance().destroyItem(process, item, actor, reference);

                item.updateDatabase();
                refreshWeight();
            }
            item.deleteMe();
        }
        return true;
    }
}
