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

import l2r.L2DatabaseFactory;
import l2r.gameserver.enums.ItemLocation;
import l2r.gameserver.model.L2World;
import l2r.gameserver.model.actor.instance.L2PcInstance;
import l2r.gameserver.model.items.instance.L2ItemInstance;

/**
 * @author DS
 */
public class Mail extends ItemContainer
{
	private final int _ownerId;
	private int _messageId;
	
	public Mail(int objectId, int messageId)
	{
		_ownerId = objectId;
		_messageId = messageId;
	}
	
	@Override
	public String getName()
	{
		return "Mail";
	}
	
	@Override
	public L2PcInstance getOwner()
	{
		return null;
	}
	
	@Override
	public ItemLocation getBaseLocation()
	{
		return ItemLocation.MAIL;
	}
	
	public int getMessageId()
	{
		return _messageId;
	}
	
	public void setNewMessageId(int messageId)
	{
		_messageId = messageId;
		for (L2ItemInstance item : _items)
		{
			if (item == null)
			{
				continue;
			}
			
			item.setItemLocation(getBaseLocation(), messageId);
		}
		
		updateDatabase();
	}
	
	public void returnToWh(ItemContainer wh)
	{
		for (L2ItemInstance item : _items)
		{
			if (item == null)
			{
				continue;
			}
			if (wh == null)
			{
				item.setItemLocation(ItemLocation.WAREHOUSE);
				item.updateDatabase(true);
			}
			else
			{
				transferItem("Expire", item.getObjectId(), item.getCount(), wh, null, null);
			}
		}
	}
	
	@Override
	protected void addItem(L2ItemInstance item)
	{
		super.addItem(item);
		item.setItemLocation(getBaseLocation(), _messageId);
	}
	
	@Override
	public void updateDatabase()
	{
		_items.forEach(i -> i.updateDatabase(true));
	}
	
	private void restoreEmailFromZeuS()
	{
		String QQuery = "SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time FROM items WHERE owner_id=(SELECT messages.senderId FROM messages WHERE messages.messageId = items.loc_data AND messages.receiverId=? AND messages.hasAttachments='true' ) AND loc=? AND loc_data=?";
		try
		{
			Connection con = L2DatabaseFactory.getInstance().getConnection();
			Throwable throwable = null;
			try
			{
				PreparedStatement statement = con.prepareStatement(QQuery);
				Throwable throwable2 = null;
				try
				{
					statement.setInt(1, this.getOwnerId());
					statement.setString(2, this.getBaseLocation().name());
					statement.setInt(3, this.getMessageId());
					ResultSet inv = statement.executeQuery();
					Throwable throwable3 = null;
					try
					{
						while (inv.next())
						{
							L2ItemInstance item = L2ItemInstance.restoreFromDb(this.getOwnerId(), inv);
							if (item == null)
							{
								continue;
							}
							L2World.getInstance().storeObject(item);
							if (item.isStackable() && (this.getItemByItemId(item.getId()) != null))
							{
								this.addItem("Restore", item, null, (Object) null);
								continue;
							}
							this.addItem(item);
						}
					}
					catch (Throwable item)
					{
						throwable3 = item;
						throw item;
					}
					finally
					{
						if (inv != null)
						{
							if (throwable3 != null)
							{
								try
								{
									inv.close();
								}
								catch (Throwable item)
								{
									throwable3.addSuppressed(item);
								}
							}
							else
							{
								inv.close();
							}
						}
					}
				}
				catch (Throwable inv)
				{
					throwable2 = inv;
					throw inv;
				}
				finally
				{
					if (statement != null)
					{
						if (throwable2 != null)
						{
							try
							{
								statement.close();
							}
							catch (Throwable inv)
							{
								throwable2.addSuppressed(inv);
							}
						}
						else
						{
							statement.close();
						}
					}
				}
			}
			catch (Throwable statement)
			{
				throwable = statement;
				throw statement;
			}
			finally
			{
				if (con != null)
				{
					if (throwable != null)
					{
						try
						{
							con.close();
						}
						catch (Throwable statement)
						{
							throwable.addSuppressed(statement);
						}
					}
					else
					{
						con.close();
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("could not restore container:", e);
		}
	}
	
	@Override
	public void restore()
	{
		try (Connection con = L2DatabaseFactory.getInstance().getConnection();
			PreparedStatement statement = con.prepareStatement("SELECT object_id, item_id, count, enchant_level, loc, loc_data, custom_type1, custom_type2, mana_left, time FROM items WHERE owner_id=? AND loc=? AND loc_data=?"))
		{
			statement.setInt(1, getOwnerId());
			statement.setString(2, getBaseLocation().name());
			statement.setInt(3, getMessageId());
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
					
					// If stackable item is found just add to current quantity
					if (item.isStackable() && (getItemByItemId(item.getId()) != null))
					{
						addItem("Restore", item, null, null);
					}
					else
					{
						addItem(item);
					}
				}
			}
		}
		catch (Exception e)
		{
			_log.warn("could not restore container:", e);
		}
		try
		{
			this.restoreEmailFromZeuS();
		}
		catch (Exception e)
		{
			_log.warn("could not restore container Email:", e);
		}
	}
	
	@Override
	public int getOwnerId()
	{
		return _ownerId;
	}
}