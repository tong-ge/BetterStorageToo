package io.github.tehstoneman.betterstorage.common.item.crafting;

import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.ShapedOreRecipe;

/*
 * Recipe to copy keys
 */
public class CopyKeyRecipe extends ShapedOreRecipe
{
	public CopyKeyRecipe( ItemStack result, Object[] recipe )
	{
		super( result, recipe );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		ItemStack itemStack = null;

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack testStack = inv.getStackInSlot( i );

			if( testStack != null )
				if( testStack.getItem() == BetterStorageItems.KEY )
					itemStack = testStack;
		}

		if( itemStack != null )
		{
			final ItemStack resultStack = new ItemStack( BetterStorageItems.KEY, 1, itemStack.getMetadata() );

			if( itemStack.hasTagCompound() )
				resultStack.setTagCompound( itemStack.getTagCompound() );

			return resultStack;
		}
		return null;
	}

	@Override
	public NonNullList< ItemStack > getRemainingItems( InventoryCrafting inv )
	{
		final NonNullList< ItemStack > ret = NonNullList.withSize( inv.getSizeInventory(), ItemStack.EMPTY );

		for( int i = 0; i < ret.size(); ++i )
		{
			final ItemStack itemStack = inv.getStackInSlot( i );
			if( itemStack != null )
				if( itemStack.getItem() == BetterStorageItems.KEY )
					// resultStack[i].setCount( 1 );
					ret.set( i, itemStack.copy() );
				else
					ret.set( i, ForgeHooks.getContainerItem( itemStack ) );
		}

		return ret;
	}
}
