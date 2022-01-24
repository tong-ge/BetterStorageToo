package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.api.lock.KeyLockItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class KeyColorRecipe extends CustomRecipe
{
	public KeyColorRecipe( ResourceLocation idIn )
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
			final ItemStack itemStack = inv.getItem( i );
			if( !itemStack.isEmpty() )
				if( itemStack.getItem() instanceof KeyLockItem )
				{
					if( !resultStack.isEmpty() )
						return false;

					resultStack = itemStack;
				}
				else
					dyeList.add( itemStack );
		}

		return !resultStack.isEmpty() && !dyeList.isEmpty() && dyeList.size() <= 2;
	}

	@Override
	public ItemStack assemble( CraftingContainer inv )
	{
		ItemStack resultStack = ItemStack.EMPTY;

		final CompoundTag tagCompound = new CompoundTag();
		for( int i = 0; i < inv.getContainerSize(); ++i )
		{
			final ItemStack ingredientStack = inv.getItem( i );

			if( !ingredientStack.isEmpty() )
			{
				final Item item = ingredientStack.getItem();
				if( item instanceof KeyLockItem )
				{
					if( !resultStack.isEmpty() )
						return ItemStack.EMPTY;

					KeyLockItem.clearColors( ingredientStack );

					resultStack = ingredientStack.copy();
					if( ingredientStack.hasTag() )
						tagCompound.merge( ingredientStack.getTag() );
				}
				/*
				 * else if( item.is( Tags.Items.DYES ) )
				 * {
				 * final DyeColor dyeColor = DyeColor.getColor( ingredientStack );
				 * if( !tagCompound.contains( KeyLockItem.TAG_COLOR1 ) )
				 * tagCompound.putInt( KeyLockItem.TAG_COLOR1, dyeColor.getFireworkColor() );
				 * else if( !tagCompound.contains( KeyLockItem.TAG_COLOR2 ) )
				 * tagCompound.putInt( KeyLockItem.TAG_COLOR2, dyeColor.getFireworkColor() );
				 * }
				 */
				else
					return ItemStack.EMPTY;
			}
		}

		resultStack.setTag( tagCompound );
		return resultStack;
	}

	@Override
	public boolean canCraftInDimensions( int width, int height )
	{
		return width * height >= 2;
	}

	@Override
	public RecipeSerializer< ? > getSerializer()
	{
		return BetterStorageRecipes.COLOR_KEY;
	}

	@Override
	public boolean isSpecial()
	{
		return true;
	}
}
