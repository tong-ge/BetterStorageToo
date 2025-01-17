package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentKey;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentLock;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class BetterStorageItemGroup extends CreativeModeTab
{
	public BetterStorageItemGroup()
	{
		super( "better_storage_too" );
		setEnchantmentCategories( EnchantmentKey.KEY, EnchantmentLock.LOCK );
	}

	@Override
	public ItemStack makeIcon()
	{
		return new ItemStack( BetterStorageBlocks.CRATE.get() );
	}
}
