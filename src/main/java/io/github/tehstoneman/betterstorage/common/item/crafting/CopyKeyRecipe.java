package io.github.tehstoneman.betterstorage.common.item.crafting;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.SpecialRecipe;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;

public class CopyKeyRecipe extends SpecialRecipe
{
	static int							MAX_WIDTH	= 3;
	static int							MAX_HEIGHT	= 3;

	private int							recipeWidth;
	private int							recipeHeight;
	private NonNullList< Ingredient >	recipeItems;
	private ItemStack					recipeOutput;

	public CopyKeyRecipe( ResourceLocation idIn )
	{
		super( idIn );
	}

	public CopyKeyRecipe( ResourceLocation idIn, String groupIn, int recipeWidthIn, int recipeHeightIn, NonNullList< Ingredient > recipeItemsIn,
			ItemStack recipeOutputIn )
	{
		super( idIn );
		recipeWidth = recipeWidthIn;
		recipeHeight = recipeHeightIn;
		recipeItems = recipeItemsIn;
		recipeOutput = recipeOutputIn;
	}

	@Override
	public boolean matches( CraftingInventory inv, World worldIn )
	{
		for( int i = 0; i <= inv.getWidth() - recipeWidth; ++i )
			for( int j = 0; j <= inv.getHeight() - recipeHeight; ++j )
			{
				if( checkMatch( inv, i, j, true ) )
					return true;

				if( checkMatch( inv, i, j, false ) )
					return true;
			}

		return false;
	}

	/**
	 * Checks if the region of a crafting inventory is match for the recipe.
	 */
	private boolean checkMatch( CraftingInventory craftingInventory, int width, int height, boolean p_77573_4_ )
	{
		for( int i = 0; i < craftingInventory.getWidth(); ++i )
			for( int j = 0; j < craftingInventory.getHeight(); ++j )
			{
				final int k = i - width;
				final int l = j - height;
				Ingredient ingredient = Ingredient.EMPTY;
				if( k >= 0 && l >= 0 && k < recipeWidth && l < recipeHeight )
					if( p_77573_4_ )
						ingredient = recipeItems.get( recipeWidth - k - 1 + l * recipeWidth );
					else
						ingredient = recipeItems.get( k + l * recipeWidth );

				if( !ingredient.test( craftingInventory.getItem( i + j * craftingInventory.getWidth() ) ) )
					return false;
			}

		return true;
	}

	@Override
	public ItemStack getResultItem()
	{
		return recipeOutput;
	}

	@Override
	public ItemStack assemble( CraftingInventory inv )
	{
		// final ItemStack outset = ItemStack.EMPTY;
		final ItemStack outset = getResultItem().copy();

		if( !outset.isEmpty() )
		{
			final CompoundNBT tagCompound = outset.getOrCreateTag();

			for( int i = 0; i < inv.getContainerSize(); ++i )
			{
				final ItemStack ingredientStack = inv.getItem( i );

				if( !ingredientStack.isEmpty() && ingredientStack.getItem() instanceof IKey )
					if( ingredientStack.hasTag() )
						tagCompound.merge( ingredientStack.getTag() );
			}

			outset.setTag( tagCompound );
		}

		return outset;
	}

	@Override
	public NonNullList< ItemStack > getRemainingItems( CraftingInventory inv )
	{
		final NonNullList< ItemStack > ret = NonNullList.withSize( inv.getContainerSize(), ItemStack.EMPTY );

		for( int i = 0; i < ret.size(); ++i )
		{
			final ItemStack itemStack = inv.getItem( i );
			if( !itemStack.isEmpty() )
				if( itemStack.getItem() instanceof IKey )
					ret.set( i, itemStack.copy() );
				else
					ret.set( i, ForgeHooks.getContainerItem( itemStack ) );
		}

		return ret;
	}

	@Override
	public boolean canCraftInDimensions( int width, int height )
	{
		return width >= recipeWidth && height >= recipeHeight;
	}

	@Override
	public IRecipeSerializer< ? > getSerializer()
	{
		return BetterStorageRecipes.COPY_KEY;
	}

	@Override
	public boolean isSpecial()
	{
		return false;
	}

	/**
	 * Returns a key json object as a Java HashMap.
	 */
	private static Map< String, Ingredient > deserializeKey( JsonObject json )
	{
		final Map< String, Ingredient > map = Maps.newHashMap();

		for( final Entry< String, JsonElement > entry : json.entrySet() )
		{
			if( entry.getKey().length() != 1 )
				throw new JsonSyntaxException( "Invalid key entry: '" + entry.getKey() + "' is an invalid symbol (must be 1 character only)." );

			if( " ".equals( entry.getKey() ) )
				throw new JsonSyntaxException( "Invalid key entry: ' ' is a reserved symbol." );

			map.put( entry.getKey(), Ingredient.fromJson( entry.getValue() ) );
		}

		map.put( " ", Ingredient.EMPTY );
		return map;
	}

	@VisibleForTesting
	static String[] shrink( String... toShrink )
	{
		int i = Integer.MAX_VALUE;
		int j = 0;
		int k = 0;
		int l = 0;

		for( int i1 = 0; i1 < toShrink.length; ++i1 )
		{
			final String s = toShrink[i1];
			i = Math.min( i, firstNonSpace( s ) );
			final int j1 = lastNonSpace( s );
			j = Math.max( j, j1 );
			if( j1 < 0 )
			{
				if( k == i1 )
					++k;

				++l;
			}
			else
				l = 0;
		}

		if( toShrink.length == l )
			return new String[0];
		else
		{
			final String[] astring = new String[toShrink.length - l - k];

			for( int k1 = 0; k1 < astring.length; ++k1 )
				astring[k1] = toShrink[k1 + k].substring( i, j + 1 );

			return astring;
		}
	}

	private static int firstNonSpace( String str )
	{
		int i;
		for( i = 0; i < str.length() && str.charAt( i ) == ' '; ++i );

		return i;
	}

	private static int lastNonSpace( String str )
	{
		int i;
		for( i = str.length() - 1; i >= 0 && str.charAt( i ) == ' '; --i );

		return i;
	}

	private static String[] patternFromJson( JsonArray jsonArr )
	{
		final String[] astring = new String[jsonArr.size()];
		if( astring.length > MAX_HEIGHT )
			throw new JsonSyntaxException( "Invalid pattern: too many rows, " + MAX_HEIGHT + " is maximum" );
		else if( astring.length == 0 )
			throw new JsonSyntaxException( "Invalid pattern: empty pattern not allowed" );
		else
		{
			for( int i = 0; i < astring.length; ++i )
			{
				final String s = JSONUtils.convertToString( jsonArr.get( i ), "pattern[" + i + "]" );
				if( s.length() > MAX_WIDTH )
					throw new JsonSyntaxException( "Invalid pattern: too many columns, " + MAX_WIDTH + " is maximum" );

				if( i > 0 && astring[0].length() != s.length() )
					throw new JsonSyntaxException( "Invalid pattern: each row must be the same width" );

				astring[i] = s;
			}

			return astring;
		}
	}

	private static NonNullList< Ingredient > deserializeIngredients( String[] pattern, Map< String, Ingredient > keys, int patternWidth,
			int patternHeight )
	{
		final NonNullList< Ingredient > nonnulllist = NonNullList.withSize( patternWidth * patternHeight, Ingredient.EMPTY );
		final Set< String > set = Sets.newHashSet( keys.keySet() );
		set.remove( " " );

		for( int i = 0; i < pattern.length; ++i )
			for( int j = 0; j < pattern[i].length(); ++j )
			{
				final String s = pattern[i].substring( j, j + 1 );
				final Ingredient ingredient = keys.get( s );
				if( ingredient == null )
					throw new JsonSyntaxException( "Pattern references symbol '" + s + "' but it's not defined in the key" );

				set.remove( s );
				nonnulllist.set( j + patternWidth * i, ingredient );
			}

		if( !set.isEmpty() )
			throw new JsonSyntaxException( "Key defines symbols that aren't used in pattern: " + set );
		else
			return nonnulllist;
	}

	public static class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry< IRecipeSerializer< ? > >
			implements IRecipeSerializer< CopyKeyRecipe >
	{
		@Override
		public CopyKeyRecipe fromJson( ResourceLocation recipeId, JsonObject json )
		{
			final String s = JSONUtils.getAsString( json, "group", "" );
			final Map< String, Ingredient > map = deserializeKey( JSONUtils.getAsJsonObject( json, "key" ) );
			final String[] astring = shrink( patternFromJson( JSONUtils.getAsJsonArray( json, "pattern" ) ) );
			final int i = astring[0].length();
			final int j = astring.length;
			final NonNullList< Ingredient > nonnulllist = deserializeIngredients( astring, map, i, j );
			final ItemStack itemstack = ShapedRecipe.itemFromJson( JSONUtils.getAsJsonObject( json, "result" ) );
			return new CopyKeyRecipe( recipeId, s, i, j, nonnulllist, itemstack );
		}

		@Override
		public CopyKeyRecipe fromNetwork( ResourceLocation recipeId, PacketBuffer buffer )
		{
			final int i = buffer.readVarInt();
			final int j = buffer.readVarInt();
			final String s = buffer.readUtf( 32767 );
			final NonNullList< Ingredient > nonnulllist = NonNullList.withSize( i * j, Ingredient.EMPTY );

			for( int k = 0; k < nonnulllist.size(); ++k )
				nonnulllist.set( k, Ingredient.fromNetwork( buffer ) );

			final ItemStack itemstack = buffer.readItem();
			return new CopyKeyRecipe( recipeId, s, i, j, nonnulllist, itemstack );
		}

		@Override
		public void toNetwork( PacketBuffer buffer, CopyKeyRecipe recipe )
		{
			buffer.writeVarInt( recipe.recipeWidth );
			buffer.writeVarInt( recipe.recipeHeight );
			buffer.writeUtf( recipe.getGroup() );

			for( final Ingredient ingredient : recipe.recipeItems )
				ingredient.toNetwork( buffer );

			buffer.writeItem( recipe.getResultItem() );
		}
	}
}
