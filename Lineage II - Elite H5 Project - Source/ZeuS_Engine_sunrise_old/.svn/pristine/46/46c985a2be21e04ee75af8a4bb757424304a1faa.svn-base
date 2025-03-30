package ZeuS.Config;

import l2r.gameserver.model.items.L2Item;

public class _itemInfo {
	public final int ITEM_ID;
	public final String ITEM_NAME;
	public final String ITEM_ICON;
	public final L2Item ITEM;
	public final boolean ITEM_FOR_SEARCH;
	public _itemInfo(L2Item _item) {
		this.ITEM_ID = _item.getId();
		this.ITEM_NAME = _item.getName();
		this.ITEM_ICON = _item.getIcon();
		this.ITEM = _item;
		if ((this.ITEM_NAME.toUpperCase().indexOf("FOR EVENTS") > 0)
				|| (this.ITEM_NAME.toUpperCase().indexOf("LIMITED PERIOD") > 0)
				|| (this.ITEM_NAME.toUpperCase().indexOf("(EVENT)") > 0)
				|| (this.ITEM_NAME.toUpperCase().indexOf("DAY PACK") > 0)
				|| (this.ITEM_NAME.toUpperCase().indexOf("- EVENT") > 0)
				|| (this.ITEM_NAME.toUpperCase().indexOf("{PVP}") > 0)) {
			this.ITEM_FOR_SEARCH = false;
		}else {
			this.ITEM_FOR_SEARCH = true;
		}
	}
}
