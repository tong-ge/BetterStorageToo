package io.github.tehstoneman.betterstorage.common.item.crafting;

import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ColorRecipe extends ShapelessOreRecipe
{
	public ColorRecipe( ItemStack result, Object[] recipe )
	{
		super( result, recipe );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		ItemStack itemIn = null;
		int color1 = -1;
		int color2 = -1;

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack testStack = inv.getStackInSlot( i );

			if( testStack != null )
			{
				if( testStack.getItem() instanceof ItemBetterStorage )
					itemIn = testStack;
				if( testStack.getItem() == Items.DYE )
					if( color1 == -1 )
						color1 = EnumDyeColor.byDyeDamage( testStack.getMetadata() ).getMapColor().colorValue;
					else
						if( color2 == -1 )
							color2 = EnumDyeColor.byDyeDamage( testStack.getMetadata() ).getMapColor().colorValue;
			}
		}

		if( itemIn != null )
		{
			final ItemStack resultStack = itemIn.copy();

			if( color1 != -1 )
				ItemBetterStorage.setKeyColor1( resultStack, color1 );
			if( color2 != -1 )
				ItemBetterStorage.setKeyColor2( resultStack, color2 );

			return resultStack;
		}
		return null;
	}
}
