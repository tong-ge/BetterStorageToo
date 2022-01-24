package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.api.IDyeableItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.Tags;

public class CardboardColorRecipe extends CustomRecipe
{

	public CardboardColorRecipe( ResourceLocation idIn )
	{
		super( idIn );
	}

	@Override
	public boolean matches( CraftingContainer inv, Level worldIn )
	{
		ItemStack resultStack = ItemStack.EMPTY;
		final List< ItemStack > dyeList = Lists.newArrayList();

		for( int i = 0; i < inv.getContainerSize(); ++i )
		{
			final ItemStack itemstack = inv.getItem( i );

			if( !itemstack.isEmpty() )
				if( itemstack.getItem() instanceof IDyeableItem )
				{
					if( !resultStack.isEmpty() )
						return false;

					resultStack = itemstack;
				}
				else
				{
					/*
					 * if( !itemstack.getItem().is( Tags.Items.DYES ) )
					 * return false;
					 */

					dyeList.add( itemstack );
				}
		}

		return !resultStack.isEmpty() && !dyeList.isEmpty();
	}

	@Override
	public ItemStack assemble( CraftingContainer inv )
	{
		ItemStack resultStack = ItemStack.EMPTY;
		final List< DyeColor > dyeList = Lists.newArrayList();

		for( int i = 0; i < inv.getContainerSize(); ++i )
		{
			final ItemStack ingredientStack = inv.getItem( i );

			if( !ingredientStack.isEmpty() )
			{
				final Item item = ingredientStack.getItem();
				if( item instanceof IDyeableItem )
				{
					if( !resultStack.isEmpty() )
						return ItemStack.EMPTY;

					resultStack = ingredientStack.copy();
				}
				/*
				 * else if( item.is( Tags.Items.DYES ) )
				 * dyeList.add( DyeColor.getColor( ingredientStack ) );
				 */
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
	public boolean canCraftInDimensions( int width, int height )
	{
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer()
	{
		return BetterStorageRecipes.COLOR_CARDBOARD;
	}

	@Override
	public boolean isSpecial()
	{
		return true;
	}
}
