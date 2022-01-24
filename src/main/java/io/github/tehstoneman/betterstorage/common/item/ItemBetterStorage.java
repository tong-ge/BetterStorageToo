package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.world.item.Item;

public abstract class ItemBetterStorage extends Item
{
	public ItemBetterStorage( Properties properties )
	{
		super( properties.tab( BetterStorage.ITEM_GROUP ) );
	}
}
