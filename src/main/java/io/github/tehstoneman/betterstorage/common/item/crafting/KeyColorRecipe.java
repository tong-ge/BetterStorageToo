package io.github.tehstoneman.betterstorage.common.item.crafting;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import io.github.tehstoneman.betterstorage.common.item.locking.ItemKeyLock;
import io.github.tehstoneman.betterstorage.utils.DyeUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class KeyColorRecipe extends ShapelessOreRecipe
{
	public KeyColorRecipe( @Nullable final ResourceLocation group, final NonNullList< Ingredient > input, final ItemStack result )
	{
		super( group, result, result );
	}

	@Override
	public ItemStack getCraftingResult( InventoryCrafting inv )
	{
		final ItemStack outputStack = super.getCraftingResult( inv );

		if( !outputStack.isEmpty() )
		{
			final NBTTagCompound tagCompound = outputStack.hasTagCompound() ? outputStack.getTagCompound() : new NBTTagCompound();

			for( int i = 0; i < inv.getSizeInventory(); ++i )
			{
				final ItemStack ingredientStack = inv.getStackInSlot( i );

				if( !ingredientStack.isEmpty() )
				{
					ItemKeyLock.clearColors( ingredientStack );
					if( ingredientStack.getItem() == outputStack.getItem() )
						if( ingredientStack.hasTagCompound() )
							tagCompound.merge( ingredientStack.getTagCompound() );

					if( DyeUtils.isDye( ingredientStack ) )
						if( !tagCompound.hasKey( ItemKeyLock.TAG_COLOR1 ) )
							tagCompound.setInteger( ItemKeyLock.TAG_COLOR1, DyeUtils.getDyeColor( ingredientStack ).getColorValue() );
						else
							if( !tagCompound.hasKey( ItemKeyLock.TAG_COLOR2 ) )
								tagCompound.setInteger( ItemKeyLock.TAG_COLOR2, DyeUtils.getDyeColor( ingredientStack ).getColorValue() );

				}
			}

			outputStack.setTagCompound( tagCompound );
		}

		return outputStack;
	}

	public static class Factory implements IRecipeFactory
	{
		@Override
		public IRecipe parse( JsonContext context, JsonObject json )
		{
			final String group = JsonUtils.getString( json, "group", "" );
			final NonNullList< Ingredient > ingredients = parseShapeless( context, json );
			final ItemStack result = CraftingHelper.getItemStack( JsonUtils.getJsonObject( json, "result" ), context );

			return new KeyColorRecipe( group.isEmpty() ? null : new ResourceLocation( group ), ingredients, result );
		}

		public static NonNullList< Ingredient > parseShapeless( final JsonContext context, final JsonObject json )
		{
			final NonNullList< Ingredient > ingredients = NonNullList.create();
			for( final JsonElement element : JsonUtils.getJsonArray( json, "ingredients" ) )
				ingredients.add( CraftingHelper.getIngredient( element, context ) );

			if( ingredients.isEmpty() )
				throw new JsonParseException( "No ingredients for shapeless recipe" );

			return ingredients;
		}
	}
}
