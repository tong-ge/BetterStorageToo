package io.github.tehstoneman.betterstorage.item.cardboard;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.api.crafting.BetterStorageCrafting;
import io.github.tehstoneman.betterstorage.api.crafting.IRecipeInput;
import io.github.tehstoneman.betterstorage.api.crafting.IStationRecipe;
import io.github.tehstoneman.betterstorage.api.crafting.RecipeBounds;
import io.github.tehstoneman.betterstorage.api.crafting.RecipeInputItemStack;
import io.github.tehstoneman.betterstorage.api.crafting.StationCrafting;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils.StackEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.init.Items;
import net.minecraft.item.ItemEnchantedBook;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CardboardEnchantmentRecipe implements IStationRecipe
{

	@Override
	public StationCrafting checkMatch( ItemStack[] input, RecipeBounds bounds )
	{

		// Quick check if input matches the recipe.

		boolean hasCardboardItems = false;
		int bookIndex = -1;
		ItemStack book = null;

		for( int i = 0; i < input.length; i++ )
		{
			final ItemStack stack = input[i];
			if( stack == null )
				continue;
			if( stack.getItem() instanceof ICardboardItem )
				hasCardboardItems = true;
			else
				if( book == null && stack.getItem() == Items.ENCHANTED_BOOK )
				{
					bookIndex = i;
					book = stack;
				}
				else
					return null;
		}

		if( book == null || !hasCardboardItems )
			return null;

		// Basic items match the recipe,
		// do more expensive stuff now.

		final ItemStack[] output = new ItemStack[9];
		final int experienceCost = 0;
		final IRecipeInput[] requiredInput = new IRecipeInput[9];

		final Collection< StackEnchantment > bookEnchantments = StackUtils.getEnchantments( book ).values();

		for( int i = 0; i < input.length; i++ )
		{
			final ItemStack stack = input[i];
			if( stack == null || !( stack.getItem() instanceof ICardboardItem ) )
				continue;

			final ItemStack outset = stack.copy();

			final boolean canApply = false;
			final Map< Integer, StackEnchantment > stackEnchants = StackUtils.getEnchantments( outset );
			final int numEnchants = stackEnchants.size();

			/*
			 * for (StackEnchantment bookEnch : bookEnchantments) {
			 * if (!StackUtils.isEnchantmentCompatible(outset, stackEnchants.values(), bookEnch))
			 * continue;
			 * 
			 * StackEnchantment stackEnch = stackEnchants.get(bookEnch.ench.effectId);
			 * 
			 * // Calculate enchantment cost.
			 * int level = (bookEnch.getLevel() - ((stackEnch != null) ? stackEnch.getLevel() : 0));
			 * experienceCost += calculateCost(bookEnch, (stackEnch == null), numEnchants);
			 * 
			 * // Set enchantment level of output item.
			 * if (stackEnch != null) stackEnch.setLevel(bookEnch.getLevel());
			 * else outset.addEnchantment(bookEnch.ench, bookEnch.getLevel());
			 * 
			 * canApply = true;
			 * }
			 */

			// If none of the enchantments on the book can
			// be applied on the item, the recipe is invalid.
			if( !canApply )
				return null;

			output[i] = outset;
			requiredInput[i] = new RecipeInputItemStack( StackUtils.copyStack( stack, 1 ), true );
		}

		requiredInput[bookIndex] = new RecipeInputItemStack( StackUtils.copyStack( book, 0, false ) );

		return new StationCrafting( output, requiredInput, experienceCost );

	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public List<IRecipeInput[]> getSampleInputs() {
	 * List<IRecipeInput[]> sampleInputs = new ArrayList<IRecipeInput[]>();
	 * makeInput(sampleInputs, BetterStorageItems.cardboardHelmet, BetterStorageItems.cardboardChestplate, null,
	 * BetterStorageItems.cardboardLeggings, BetterStorageItems.cardboardBoots,
	 * makeEnchantedBook(Enchantment.protection, 4));
	 * makeInput(sampleInputs, null, BetterStorageItems.cardboardPickaxe, null,
	 * null, BetterStorageItems.cardboardShovel, makeEnchantedBook(Enchantment.efficiency, 5),
	 * null, BetterStorageItems.cardboardAxe);
	 * makeInput(sampleInputs, null, null, null,
	 * BetterStorageItems.cardboardSword,
	 * BetterStorageItems.cardboardAxe,
	 * makeEnchantedBook(Enchantment.sharpness, 5));
	 * makeInput(sampleInputs, BetterStorageItems.cardboardChestplate, BetterStorageItems.cardboardLeggings, null,
	 * BetterStorageItems.cardboardSword, BetterStorageItems.cardboardPickaxe,
	 * makeEnchantedBook(Enchantment.unbreaking, 3),
	 * BetterStorageItems.cardboardShovel, BetterStorageItems.cardboardHoe);
	 * return sampleInputs;
	 * }
	 */

	private void makeInput( List< IRecipeInput[] > sampleInputs, Object... obj )
	{
		final IRecipeInput[] input = new IRecipeInput[9];
		boolean hasCardboardItem = false;
		for( int i = 0; i < obj.length; i++ )
			if( obj[i] != null )
			{
				input[i] = BetterStorageCrafting.makeInput( obj[i] );
				if( obj[i] instanceof ICardboardItem )
					hasCardboardItem = true;
			}
		if( hasCardboardItem )
			sampleInputs.add( input );
	}

	private ItemStack makeEnchantedBook( Enchantment ench, int level )
	{
		final ItemStack book = new ItemStack( Items.ENCHANTED_BOOK );
		( (ItemEnchantedBook)book.getItem() ).addEnchantment( book, new EnchantmentData( ench, level ) );
		return book;
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public List<IRecipeInput> getBlockPossibleInputs() {
	 * return Arrays.<IRecipeInput>asList(
	 * RecipeInputCardboard.instance,
	 * RecipeInputEnchantedBook.instance);
	 * }
	 */

	@Override
	@SideOnly( Side.CLIENT )
	public List< ItemStack > getBlockPossibleOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public List< IRecipeInput[] > getSampleInputs()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List< IRecipeInput > getBlockPossibleInputs()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// Utility functions

	/*
	 * private int calculateCost(StackEnchantment ench, boolean hasEnchantment, int numEnchants) {
	 * int weight = ench.ench.getWeight();
	 * int costPerLevel;
	 * if (weight > 8) costPerLevel = 1;
	 * else if (weight > 6) costPerLevel = 2;
	 * else if (weight > 3) costPerLevel = 3;
	 * else if (weight > 1) costPerLevel = 4;
	 * else costPerLevel = 6;
	 * 
	 * int cost = (costPerLevel * ench.getLevel()) - 1;
	 * cost += CardboardRecipeHelper.getAdditionalEnchantmentCost(ench);
	 * if (hasEnchantment)
	 * cost += (numEnchants + 1) / 2;
	 * return cost;
	 * }
	 */
}
