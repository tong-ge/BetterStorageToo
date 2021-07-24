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
					if( !itemstack.getItem().is( Tags.Items.DYES ) )
						return false;

					dyeList.add( itemstack );
				}
		}

		return !resultStack.isEmpty() && !dyeList.isEmpty();
	}

	@Override
	public ItemStack assemble( CraftingInventory inv )
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
				else if( item.is( Tags.Items.DYES ) )
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
	public boolean canCraftInDimensions( int width, int height )
	{
		return width * height >= 2;
	}

	@Override
	public IRecipeSerializer< ? > getSerializer()
	{
		return BetterStorageRecipes.COLOR_CARDBOARD;
	}

	@Override
	public boolean isSpecial()
	{
		return true;
	}
}
