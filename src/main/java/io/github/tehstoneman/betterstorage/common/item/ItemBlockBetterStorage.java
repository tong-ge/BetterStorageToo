package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class ItemBlockBetterStorage extends ItemBlock
{
	public ItemBlockBetterStorage( Block blockIn )
	{
		super( blockIn, new Item.Properties().group( BetterStorage.ITEM_GROUP ) );
	}
}
