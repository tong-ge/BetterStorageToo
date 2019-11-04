package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.item.Item;

public abstract class ItemBetterStorage extends Item
{
	public ItemBetterStorage( String name, Properties properties )
	{
		super( properties.group( BetterStorage.ITEM_GROUP ) );
	}
}
