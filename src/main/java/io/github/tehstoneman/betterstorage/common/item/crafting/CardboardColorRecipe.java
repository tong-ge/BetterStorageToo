package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.api.IDyeableItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class CardboardColorRecipe extends SpecialRecipe
{

	public CardboardColorRecipe( ResourceLocation idIn )
	{
		super( idIn );
	}

	@Override
	public boolean matches( CraftingInventory inv, World worldIn )
	{
		ItemStack resultStack = ItemStack.EMPTY;
		final List< ItemStack > dyeList = Lists.newArrayList();

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack itemstack = inv.getStackInSlot( i );

			if( !itemstack.isEmpty() )
				if( itemstack.getItem() instanceof IDyeableItem )
				{
					if( !resultStack.isEmpty() )
						return false;

					resultStack = itemstack;
				}
				else
				{
					if( !itemstack.getItem().isIn( Tags.Items.DYES ) )
						return false;

					dyeList.add( itemstack );
				}
		}

		return !resultStack.isEmpty() && !dyeList.isEmpty();
	}

	@Override
	public ItemStack getCraftingResult( CraftingInventory inv )
	{
		ItemStack resultStack = ItemStack.EMPTY;
		final List< DyeColor > dyeList = Lists.newArrayList();

		final int[] aint = new int[3];
		final int h = 0;
		final int j = 0;
		IDyeableItem itemdyable = null;

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack ingredientStack = inv.getStackInSlot( i );

			if( !ingredientStack.isEmpty() )
			{
				final Item item = ingredientStack.getItem();
				if( item instanceof IDyeableItem )
				{
					if( !resultStack.isEmpty() )
						return ItemStack.EMPTY;

					itemdyable = (IDyeableItem)ingredientStack.getItem();

					resultStack = ingredientStack.copy();
				}
				else if( item.isIn( Tags.Items.DYES ) )
					dyeList.add( DyeColor.getColor( ingredientStack ) );
				else
					return ItemStack.EMPTY;
			}
		}

		if( !resultStack.isEmpty() && !dyeList.isEmpty() )
			return IDyeableItem.dyeItem( resultStack, dyeList );
		else
			return ItemStack.EMPTY;
	}

	@Override
	public boolean canFit( int width, int height )
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public IRecipeSerializer< ? > getSerializer()
	{
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * @Override
	 * public ItemStack getCraftingResult( InventoryCrafting inv )
	 * {
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack getRecipeOutput()
	 * {
	 * return ItemStack.EMPTY;
	 * }
	 */

	/*
	 * @Override
	 * public NonNullList< ItemStack > getRemainingItems( InventoryCrafting inv )
	 * {
	 * final NonNullList< ItemStack > nonnulllist = NonNullList.<ItemStack> withSize( inv.getSizeInventory(), ItemStack.EMPTY );
	 *
	 * for( int i = 0; i < nonnulllist.size(); ++i )
	 * {
	 * final ItemStack itemstack = inv.getStackInSlot( i );
	 * nonnulllist.set( i, net.minecraftforge.common.ForgeHooks.getContainerItem( itemstack ) );
	 * }
	 *
	 * return nonnulllist;
	 * }
	 */

	/*
	 * @Override
	 * public boolean isDynamic()
	 * {
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public boolean canFit( int width, int height )
	 * {
	 * return width * height >= 2;
	 * }
	 */
}
