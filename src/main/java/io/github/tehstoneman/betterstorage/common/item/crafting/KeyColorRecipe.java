package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemKeyLock;
import io.github.tehstoneman.betterstorage.utils.DyeUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class KeyColorRecipe implements IRecipe
{
	@Override
	public boolean matches( InventoryCrafting inv, World worldIn )
	{
		ItemStack itemstack = ItemStack.EMPTY;
		final List< ItemStack > list = Lists.<ItemStack> newArrayList();

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack itemstack1 = inv.getStackInSlot( i );

			if( !itemstack1.isEmpty() )
				if( itemstack1.getItem() instanceof ItemKeyLock )
				{
					if( !itemstack.isEmpty() )
						return false;

					itemstack = itemstack1;
				}
				else
				{
					if( !DyeUtils.isDye( itemstack1 ) )
						return false;

					list.add( itemstack1 );
				}
		}

		return !itemstack.isEmpty() && !list.isEmpty() && list.size() <= 2;
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		ItemStack itemIn = ItemStack.EMPTY;
		int color1 = -1;
		int color2 = -1;

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack testStack = inv.getStackInSlot( i );

			if( !testStack.isEmpty() )
			{
				if( testStack.getItem() instanceof ItemBetterStorage )
					itemIn = testStack;
				/*if( DyeUtils.isDye( testStack ) )
					if( color1 == -1 )
						color1 = DyeUtils.getDyeColor( testStack ).getMapColor().colorValue;
					else
						if( color2 == -1 )
							color2 = DyeUtils.getDyeColor( testStack ).getMapColor().colorValue;*/
			}
		}

		if( !itemIn.isEmpty() )
		{
			final ItemStack resultStack = itemIn.copy();

			if( color1 != -1 )
				ItemKeyLock.setKeyColor1( resultStack, color1 );
			if( color2 != -1 )
				ItemKeyLock.setKeyColor2( resultStack, color2 );

			return resultStack;
		}
		return ItemStack.EMPTY;
	}

	/*@Override
	public int getRecipeSize()
	{
		return 10;
	}*/

	@Override
	public ItemStack getRecipeOutput()
	{
		return ItemStack.EMPTY;
	}

	@Override
	public NonNullList< ItemStack > getRemainingItems( InventoryCrafting inv )
	{
		final NonNullList< ItemStack > nonnulllist = NonNullList.<ItemStack> withSize( inv.getSizeInventory(), ItemStack.EMPTY );

		for( int i = 0; i < nonnulllist.size(); ++i )
		{
			final ItemStack itemstack = inv.getStackInSlot( i );
			nonnulllist.set( i, net.minecraftforge.common.ForgeHooks.getContainerItem( itemstack ) );
		}

		return nonnulllist;
	}

	@Override
	public IRecipe setRegistryName( ResourceLocation name )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ResourceLocation getRegistryName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Class< IRecipe > getRegistryType()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canFit( int width, int height )
	{
		// TODO Auto-generated method stub
		return false;
	}
}
