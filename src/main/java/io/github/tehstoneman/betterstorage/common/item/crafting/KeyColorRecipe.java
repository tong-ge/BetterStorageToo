package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.api.lock.KeyLockItem;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.Tags;

public class KeyColorRecipe extends SpecialRecipe
{
	public KeyColorRecipe( ResourceLocation idIn )
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
			final ItemStack itemStack = inv.getItem( i );
			if( !itemStack.isEmpty() )
				if( itemStack.getItem() instanceof KeyLockItem )
				{
					if( !resultStack.isEmpty() )
						return false;

					resultStack = itemStack;
				}
				else
				{
					if( !itemStack.getItem().is( Tags.Items.DYES ) )
						return false;

					dyeList.add( itemStack );
				}
		}

		return !resultStack.isEmpty() && !dyeList.isEmpty() && dyeList.size() <= 2;
	}

	@Override
	public ItemStack assemble( CraftingInventory inv )
	{
		ItemStack resultStack = ItemStack.EMPTY;

		final CompoundNBT tagCompound = new CompoundNBT();
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
				else if( item.is( Tags.Items.DYES ) )
				{
					final DyeColor dyeColor = DyeColor.getColor( ingredientStack );
					if( !tagCompound.contains( KeyLockItem.TAG_COLOR1 ) )
						tagCompound.putInt( KeyLockItem.TAG_COLOR1, dyeColor.getFireworkColor() );
					else if( !tagCompound.contains( KeyLockItem.TAG_COLOR2 ) )
						tagCompound.putInt( KeyLockItem.TAG_COLOR2, dyeColor.getFireworkColor() );
				}
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
	public IRecipeSerializer< ? > getSerializer()
	{
		return BetterStorageRecipes.COLOR_KEY;
	}

	@Override
	public boolean isSpecial()
	{
		return true;
	}
}
