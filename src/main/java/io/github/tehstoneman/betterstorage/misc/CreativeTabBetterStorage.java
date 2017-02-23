package io.github.tehstoneman.betterstorage.misc;

import java.util.List;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CreativeTabBetterStorage extends CreativeTabs
{
	public CreativeTabBetterStorage()
	{
		super( ModInfo.modId );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public Item getTabIconItem()
	{
		if( BetterStorageBlocks.CRATE != null )
			return Item.getItemFromBlock( BetterStorageBlocks.CRATE );
		else
			if( BetterStorageBlocks.reinforcedChest != null )
				return Item.getItemFromBlock( BetterStorageBlocks.reinforcedChest );
			else
				return Item.getItemFromBlock( Blocks.CHEST );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void displayAllRelevantItems( List< ItemStack > list )
	{
		// super.displayAllRelevantItems( list );
		for( final Item item : Item.REGISTRY )
			if( item != null )
				if( item.getUnlocalizedName().contains( ModInfo.modId ) )
					item.getSubItems( item, this, list );
		addEnchantmentBooksToList( list, BetterStorageEnchantment.getType( "key" ), BetterStorageEnchantment.getType( "lock" ) );
	}
}
