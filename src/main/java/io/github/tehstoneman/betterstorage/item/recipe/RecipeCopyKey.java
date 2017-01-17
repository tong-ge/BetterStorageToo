package io.github.tehstoneman.betterstorage.item.recipe;

import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

/*
 * Recipe to copy keys
 */
public class RecipeCopyKey extends ShapedOreRecipe
{
	public RecipeCopyKey( ItemStack result, Object[] recipe )
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
				if( testStack.getItem() == BetterStorageItems.key )
					itemStack = testStack;
		}

		if( itemStack != null )
		{
			final ItemStack resultStack = new ItemStack( BetterStorageItems.key, 1, itemStack.getMetadata() );

			if( itemStack.hasTagCompound() )
				resultStack.setTagCompound( itemStack.getTagCompound() );

			return resultStack;
		}
		return null;
	}

	@Override
	public ItemStack[] getRemainingItems( InventoryCrafting inv )
	{
		final ItemStack[] resultStack = new ItemStack[inv.getSizeInventory()];

		for( int i = 0; i < resultStack.length; ++i )
		{
			final ItemStack itemStack = inv.getStackInSlot( i );
			if( itemStack != null )
				if( itemStack.getItem() == BetterStorageItems.key )
				{
					// Return original key to crafting table
					resultStack[i] = itemStack.copy();
					resultStack[i].stackSize = 1;
				}
				else
					resultStack[i] = net.minecraftforge.common.ForgeHooks.getContainerItem( itemStack );
		}

		return resultStack;
	}
}
