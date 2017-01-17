package io.github.tehstoneman.betterstorage.item.recipe;

import java.awt.Color;

import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import io.github.tehstoneman.betterstorage.item.ItemBetterStorage;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.ShapelessOreRecipe;

/*
 * Recipe to dye keys
 */
public class RecipeDyeKey extends ShapelessOreRecipe
{
	public RecipeDyeKey( ItemStack result, Object[] recipe )
	{
		super( result, recipe );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		ItemStack itemKey = null;
		int colorCount = 0;
		int colorFull = Color.white.getRGB();
		int color = Color.white.getRGB();

		for( int i = 0; i < inv.getSizeInventory(); ++i )
		{
			final ItemStack testStack = inv.getStackInSlot( i );

			if( testStack != null )
			{
				if( testStack.getItem() == BetterStorageItems.key )
					itemKey = testStack;
				if( testStack.getItem() == Items.DYE )
				{
					colorCount++;
					if( colorCount == 1 )
						colorFull = EnumDyeColor.byDyeDamage( testStack.getMetadata() ).getMapColor().colorValue;
					if( colorCount == 2 )
						color = EnumDyeColor.byDyeDamage( testStack.getMetadata() ).getMapColor().colorValue;
				}
			}
		}

		if( itemKey != null )
		{
			final ItemStack resultStack = new ItemStack( BetterStorageItems.key, 1, colorCount );

			NBTTagCompound tag = itemKey.getTagCompound().copy();
			if( tag == null )
				tag = new NBTTagCompound();
			tag.setInteger( ItemBetterStorage.TAG_FULL_COLOR, colorFull );
			if( colorCount == 2 )
				tag.setInteger( ItemBetterStorage.TAG_COLOR, color );
			else
				if( tag.hasKey( ItemBetterStorage.TAG_COLOR ) )
					tag.removeTag( ItemBetterStorage.TAG_COLOR );

			resultStack.setTagCompound( tag );

			return resultStack;
		}
		return null;
	}
}
