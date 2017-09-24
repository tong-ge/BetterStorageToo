package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.api.IDyeableItem;
import io.github.tehstoneman.betterstorage.utils.DyeUtils;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class CardboardColorRecipe implements IRecipe
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
				if( itemstack1.getItem() instanceof IDyeableItem )
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

		return !itemstack.isEmpty() && !list.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		ItemStack itemstack = ItemStack.EMPTY;
		final int[] aint = new int[3];
		int i = 0;
		int j = 0;
		IDyeableItem itemdyable = null;

		for( int k = 0; k < inv.getSizeInventory(); ++k )
		{
			final ItemStack itemstack1 = inv.getStackInSlot( k );

			if( !itemstack1.isEmpty() )
				if( itemstack1.getItem() instanceof IDyeableItem )
				{
					itemdyable = (IDyeableItem)itemstack1.getItem();

					if( !itemstack.isEmpty() )
						return ItemStack.EMPTY;

					itemstack = itemstack1.copy();
					itemstack.setCount( 1 );

					if( itemdyable.hasColor( itemstack1 ) )
					{
						final int l = itemdyable.getColor( itemstack );
						final float f = ( l >> 16 & 255 ) / 255.0F;
						final float f1 = ( l >> 8 & 255 ) / 255.0F;
						final float f2 = ( l & 255 ) / 255.0F;
						i = (int)( i + Math.max( f, Math.max( f1, f2 ) ) * 255.0F );
						aint[0] = (int)( aint[0] + f * 255.0F );
						aint[1] = (int)( aint[1] + f1 * 255.0F );
						aint[2] = (int)( aint[2] + f2 * 255.0F );
						++j;
					}
				}
				else
				{
					if( !DyeUtils.isDye( itemstack1 ) )
						return ItemStack.EMPTY;

					final float[] afloat = EntitySheep.getDyeRgb( DyeUtils.getDyeColor( itemstack1 ) );
					final int l1 = (int)( afloat[0] * 255.0F );
					final int i2 = (int)( afloat[1] * 255.0F );
					final int j2 = (int)( afloat[2] * 255.0F );
					i += Math.max( l1, Math.max( i2, j2 ) );
					aint[0] += l1;
					aint[1] += i2;
					aint[2] += j2;
					++j;
				}
		}

		if( itemdyable == null )
			return ItemStack.EMPTY;
		else
		{
			int i1 = aint[0] / j;
			int j1 = aint[1] / j;
			int k1 = aint[2] / j;
			final float f3 = (float)i / (float)j;
			final float f4 = Math.max( i1, Math.max( j1, k1 ) );
			i1 = (int)( i1 * f3 / f4 );
			j1 = (int)( j1 * f3 / f4 );
			k1 = (int)( k1 * f3 / f4 );
			int lvt_12_3_ = ( i1 << 8 ) + j1;
			lvt_12_3_ = ( lvt_12_3_ << 8 ) + k1;
			itemdyable.setColor( itemstack, lvt_12_3_ );
			return itemstack;
		}
	}

	@Override
	public int getRecipeSize()
	{
		return 10;
	}

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
}
