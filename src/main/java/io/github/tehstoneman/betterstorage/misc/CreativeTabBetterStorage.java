package io.github.tehstoneman.betterstorage.misc;

import java.util.List;

import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.content.BetterStorageTiles;
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
		super( Constants.modId );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public Item getTabIconItem()
	{

		if( BetterStorageTiles.crate != null )
			return Item.getItemFromBlock( BetterStorageTiles.crate );
		else
			if( BetterStorageTiles.reinforcedChest != null )
				return Item.getItemFromBlock( BetterStorageTiles.reinforcedChest );
			else

				return Item.getItemFromBlock( Blocks.CHEST );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void displayAllRelevantItems( List< ItemStack > list )
	{
		super.displayAllRelevantItems( list );
		addEnchantmentBooksToList( list, BetterStorageEnchantment.getType( "key" ), BetterStorageEnchantment.getType( "lock" ) );
	}
}
