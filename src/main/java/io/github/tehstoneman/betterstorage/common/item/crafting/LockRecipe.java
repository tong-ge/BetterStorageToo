package io.github.tehstoneman.betterstorage.common.item.crafting;

import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class LockRecipe extends ShapedOreRecipe
{

	public LockRecipe( ItemStack result, Object[] recipe )
	{
		super( null, result, recipe );
		mirrored = false;
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
			final ItemStack resultStack = new ItemStack( BetterStorageItems.LOCK );

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
		final ItemStack[] resultStack = new ItemStack[inv.getSizeInventory()];

		for( int i = 0; i < resultStack.length; ++i )
		{
			final ItemStack itemStack = inv.getStackInSlot( i );
			if( itemStack != null )
				if( itemStack.getItem() == BetterStorageItems.KEY )
					// Return original key to crafting table
					ret.set( i, itemStack.copy() );
				else
					ret.set( i, ForgeHooks.getContainerItem( itemStack ) );
		}

		return ret;
	}

	public static LockRecipe createLockRecipe()
	{
		final ItemStack gold = new ItemStack( Items.GOLD_INGOT );
		final ItemStack iron = new ItemStack( Items.IRON_INGOT );
		final ItemStack key = new ItemStack( BetterStorageItems.KEY, 1, OreDictionary.WILDCARD_VALUE );
		final ItemStack lock = new ItemStack( BetterStorageItems.LOCK );
		final ItemStack[] items = new ItemStack[] { null, gold, null, gold, key, gold, gold, iron, gold };
		return new LockRecipe( lock, items );
	}

}
