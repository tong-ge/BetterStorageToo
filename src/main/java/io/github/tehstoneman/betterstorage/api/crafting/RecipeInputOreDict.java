package io.github.tehstoneman.betterstorage.api.crafting;

import java.util.List;

import net.minecraft.item.ItemStack;

public class RecipeInputOreDict extends RecipeInputBase
{
	public final String	name;
	private final int	amount;

	public RecipeInputOreDict( String name, int amount )
	{
		this.name = name;
		this.amount = amount;
	}

	@Override
	public int getAmount()
	{
		return amount;
	}

	@Override
	public boolean matches( ItemStack stack )
	{
		/*
		 * for( final ItemStack oreStack : OreDictionary.getOres( name ) )
		 * if( BetterStorageUtils.wildcardMatch( oreStack, stack ) )
		 * return true;
		 */ return false;
	}

	@Override
	// @SideOnly( Side.CLIENT )
	public List< ItemStack > getPossibleMatches()
	{
		// return OreDictionary.getOres( name );
		return null;
	}
}
