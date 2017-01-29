package io.github.tehstoneman.betterstorage.item.recipe;

import java.util.ArrayList;
import java.util.List;

import io.github.tehstoneman.betterstorage.item.IDyeableItem;
import io.github.tehstoneman.betterstorage.utils.DyeUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class DyeRecipe implements IRecipe
{
	@Override
	public int getRecipeSize()
	{
		return 10;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return null;
	}

	@Override
	public boolean matches( InventoryCrafting crafting, World world )
	{
		boolean hasArmor = false;
		boolean hasDyes = false;
		for( int i = 0; i < crafting.getSizeInventory(); i++ )
		{
			final ItemStack stack = crafting.getStackInSlot( i );
			if( stack == null )
				continue;
			final IDyeableItem dyeable = stack.getItem() instanceof IDyeableItem ? (IDyeableItem)stack.getItem() : null;
			if( dyeable != null && dyeable.canDye( stack ) )
			{
				if( hasArmor )
					return false;
				hasArmor = true;
			}
			else
				if( DyeUtils.isDye( stack ) )
					hasDyes = true;
				else
					return false;
		}
		return hasArmor && hasDyes;
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting crafting )
	{
		ItemStack armor = null;
		IDyeableItem dyeable = null;
		final List< ItemStack > dyes = new ArrayList< >();
		for( int i = 0; i < crafting.getSizeInventory(); i++ )
		{
			final ItemStack stack = crafting.getStackInSlot( i );
			if( stack == null )
				continue;
			dyeable = stack.getItem() instanceof IDyeableItem ? (IDyeableItem)stack.getItem() : null;
			if( dyeable != null && dyeable.canDye( stack ) )
			{
				if( armor != null )
					return null;
				armor = stack.copy();
			}
			else
				if( DyeUtils.isDye( stack ) )
					dyes.add( stack );
				else
					return null;
		}
		if( dyes.isEmpty() )
			return null;
		final int oldColor = StackUtils.get( armor, -1, "display", "color" );
		final int newColor = DyeUtils.getColorFromDyes( oldColor, dyes );
		StackUtils.set( armor, newColor, "display", "color" );
		return armor;
	}

	@Override
	public ItemStack[] getRemainingItems( InventoryCrafting inv )
	{
		// TODO Auto-generated method stub
		return null;
	}
}
