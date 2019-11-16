package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.List;

import com.google.common.collect.Lists;

import io.github.tehstoneman.betterstorage.common.item.locking.ItemKeyLock;
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

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack itemStack = inv.getStackInSlot( i );
			if( !itemStack.isEmpty() )
				if( itemStack.getItem() instanceof ItemKeyLock )
				{
					if( !resultStack.isEmpty() )
						return false;

					resultStack = itemStack;
				}
				else
				{
					if( !itemStack.getItem().isIn( Tags.Items.DYES ) )
						return false;

					dyeList.add( itemStack );
				}
		}

		return !resultStack.isEmpty() && !dyeList.isEmpty() && dyeList.size() <= 2;
	}

	@Override
	public ItemStack getCraftingResult( CraftingInventory inv )
	{
		ItemStack resultStack = ItemStack.EMPTY;

		final CompoundNBT tagCompound = new CompoundNBT();
		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack ingredientStack = inv.getStackInSlot( i );

			if( !ingredientStack.isEmpty() )
			{
				final Item item = ingredientStack.getItem();
				if( item instanceof ItemKeyLock )
				{
					if( !resultStack.isEmpty() )
						return ItemStack.EMPTY;

					ItemKeyLock.clearColors( ingredientStack );

					resultStack = ingredientStack.copy();
					if( ingredientStack.hasTag() )
						tagCompound.merge( ingredientStack.getTag() );
				}
				else if( item.isIn( Tags.Items.DYES ) )
				{
					final DyeColor dyeColor = DyeColor.getColor( ingredientStack );
					if( !tagCompound.contains( ItemKeyLock.TAG_COLOR1 ) )
						tagCompound.putInt( ItemKeyLock.TAG_COLOR1, dyeColor.getFireworkColor() );
					else if( !tagCompound.contains( ItemKeyLock.TAG_COLOR2 ) )
						tagCompound.putInt( ItemKeyLock.TAG_COLOR2, dyeColor.getFireworkColor() );
				}
				else
					return ItemStack.EMPTY;
			}
		}

		resultStack.setTag( tagCompound );
		return resultStack;
	}

	@Override
	public boolean canFit( int width, int height )
	{
		return width * height >= 2;
	}

	@Override
	public IRecipeSerializer< ? > getSerializer()
	{
		return BetterStorageRecipes.COLOR_KEY;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}
}
