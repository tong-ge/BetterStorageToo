package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public class BlockItemBetterStorage extends BlockItem
{
	public BlockItemBetterStorage( Block blockIn )
	{
		super( blockIn, new Item.Properties().group( BetterStorage.ITEM_GROUP ) );
	}
}
