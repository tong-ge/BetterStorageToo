package io.github.tehstoneman.betterstorage.common.item.crafting;

import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.Tags;

public class CopyKeyRecipe extends ShapedRecipe
{
	public CopyKeyRecipe( ResourceLocation idIn )
	{
		super( idIn, "", 3, 3,
				NonNullList.from( Ingredient.EMPTY, Ingredient.fromTag( Tags.Items.NUGGETS_GOLD ), Ingredient.fromTag( Tags.Items.INGOTS_GOLD ),
						Ingredient.fromTag( Tags.Items.NUGGETS_GOLD ), Ingredient.fromTag( Tags.Items.INGOTS_GOLD ),
						Ingredient.fromItems( BetterStorageItems.KEY ), Ingredient.fromTag( Tags.Items.INGOTS_GOLD ) ),
				new ItemStack( BetterStorageItems.KEY ) );
	}

	@Override
	public ItemStack getCraftingResult( CraftingInventory inv )
	{
		final ItemStack outputStack = super.getCraftingResult( inv );

		if( !outputStack.isEmpty() )
		{
			final CompoundNBT tagCompound = outputStack.getOrCreateTag();

			for( int i = 0; i < inv.getSizeInventory(); ++i )
			{
				final ItemStack ingredientStack = inv.getStackInSlot( i );

				if( !ingredientStack.isEmpty() && ingredientStack.getItem() instanceof IKey )
					if( ingredientStack.hasTag() )
						tagCompound.merge( ingredientStack.getTag() );
			}

			outputStack.setTag( tagCompound );
		}

		return outputStack;
	}

	@Override
	public NonNullList< ItemStack > getRemainingItems( CraftingInventory inv )
	{
		final NonNullList< ItemStack > ret = NonNullList.withSize( inv.getSizeInventory(), ItemStack.EMPTY );

		for( int i = 0; i < ret.size(); ++i )
		{
			final ItemStack itemStack = inv.getStackInSlot( i );
			if( !itemStack.isEmpty() )
				if( itemStack.getItem() instanceof IKey )
					ret.set( i, itemStack.copy() );
				else
					ret.set( i, ForgeHooks.getContainerItem( itemStack ) );
		}

		return ret;
	}

	@Override
	public boolean isDynamic()
	{
		return true;
	}
}
