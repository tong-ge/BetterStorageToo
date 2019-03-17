package io.github.tehstoneman.betterstorage.api.crafting;

import java.util.Arrays;
import java.util.List;

import io.github.tehstoneman.betterstorage.api.BetterStorageUtils;
import net.minecraft.item.ItemStack;

public class RecipeInputItemStack extends RecipeInputBase
{
	public final ItemStack stack;

	public RecipeInputItemStack( ItemStack stack )
	{
		this.stack = stack;
	}

	public RecipeInputItemStack( ItemStack stack, boolean nbtSensitive )
	{
		this( stack );
		// If input is NBT sensitive, make sure it has an NBT compound.
		// Empty means it only matches items with no NBT data.
		/*
		 * if( nbtSensitive )
		 * {
		 * if( !stack.hasTagCompound() )
		 * stack.setTagCompound( new NBTTagCompound() );
		 * // Otherwise, always remove the NBT compound,
		 * // because then it will match any item.
		 * }
		 * else
		 * if( stack.hasTagCompound() )
		 * stack.setTagCompound( null );
		 */
	}

	@Override
	public int getAmount()
	{
		return stack.getCount();
	}

	@Override
	public boolean matches( ItemStack stack )
	{
		return BetterStorageUtils.wildcardMatch( this.stack, stack );
	}

	private final List< ItemStack > list = null;

	@Override
	// @SideOnly( Side.CLIENT )
	public List< ItemStack > getPossibleMatches()
	{
		final ItemStack stack = this.stack.copy();
		// if( stack.getItemDamage() == OreDictionary.WILDCARD_VALUE ) stack.setItemDamage( 0 );
		return Arrays.asList( stack );
	}
}
