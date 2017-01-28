package io.github.tehstoneman.betterstorage.api.crafting;

import java.util.List;

import io.github.tehstoneman.betterstorage.api.BetterStorageUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RecipeInputList extends RecipeInputBase
{
	private final List< ItemStack > items;

	public RecipeInputList( List< ItemStack > items )
	{
		this.items = items;
	}

	@Override
	public int getAmount()
	{
		return 1;
	}

	@Override
	public boolean matches( ItemStack stack )
	{
		for( final ItemStack item : items )
			if( BetterStorageUtils.wildcardMatch( item, stack ) )
				return true;
		return false;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public List< ItemStack > getPossibleMatches()
	{
		return items;
	}
}
