package io.github.tehstoneman.betterstorage.item.cardboard;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.api.crafting.BetterStorageCrafting;
import io.github.tehstoneman.betterstorage.api.crafting.IRecipeInput;
import io.github.tehstoneman.betterstorage.api.crafting.IStationRecipe;
import io.github.tehstoneman.betterstorage.api.crafting.RecipeBounds;
import io.github.tehstoneman.betterstorage.api.crafting.RecipeInputItemStack;
import io.github.tehstoneman.betterstorage.api.crafting.RecipeInputOreDict;
import io.github.tehstoneman.betterstorage.api.crafting.StationCrafting;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils.StackEnchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class CardboardRepairRecipe implements IStationRecipe
{
	private static final IRecipeInput	sheetUsed	= new RecipeInputOreDict( "sheetCardboard", 1 );
	private static final IRecipeInput	sheetUnused	= new RecipeInputOreDict( "sheetCardboard", 0 );

	@Override
	public StationCrafting checkMatch( ItemStack[] input, RecipeBounds bounds )
	{

		// Quick check if input matches the recipe.

		boolean hasCardboardItems = false;
		int numSheets = 0;
		int totalDamage = 0;

		for( final ItemStack stack : input )
		{
			if( stack == null )
				continue;
			if( stack.getItem() instanceof ICardboardItem )
			{
				hasCardboardItems = true;
				totalDamage += stack.getItemDamage();
			}
			else
				if( sheetUsed.matches( stack ) )
					numSheets++;
				else
					return null;
		}

		if( !hasCardboardItems || numSheets <= 0 )
			return null;

		// If there's not enough sheets to repair all items, return null.
		int numSheetsNeeded = ( totalDamage + 79 ) / 80;
		if( numSheetsNeeded > numSheets )
			return null;

		// Basic items match the recipe,
		// do more expensive stuff now.

		final ItemStack[] output = new ItemStack[9];
		int experienceCost = 0;
		final IRecipeInput[] requiredInput = new IRecipeInput[9];

		for( int i = 0; i < input.length; i++ )
		{
			final ItemStack stack = input[i];
			if( stack == null )
				continue;
			ItemStack outset = null;

			if( stack.getItem() instanceof ICardboardItem )
			{

				final Collection< StackEnchantment > enchantments = StackUtils.getEnchantments( stack ).values();
				experienceCost += Math.max( enchantments.size() - 1, 0 );
				/*
				 * for (StackEnchantment ench : enchantments)
				 * experienceCost += calculateCost(ench);
				 */

				outset = StackUtils.copyStack( stack, 1 );
				outset.setItemDamage( 0 );

				final ItemStack requiredStack = outset.copy();
				requiredStack.setItemDamage( OreDictionary.WILDCARD_VALUE );
				requiredStack.setTagCompound( null );
				requiredInput[i] = new RecipeInputItemStack( requiredStack );

			}
			else
				requiredInput[i] = numSheetsNeeded-- > 0 ? sheetUsed : sheetUnused;

			output[i] = outset;
		}

		return new StationCrafting( output, requiredInput, experienceCost );

	}

	@Override
	@SideOnly( Side.CLIENT )
	public List< IRecipeInput[] > getSampleInputs()
	{
		final List< IRecipeInput[] > sampleInputs = new ArrayList< >();
		/*
		 * if (BetterStorageItems.cardboardPickaxe != null) {
		 * ItemStack stack = new ItemStack(BetterStorageItems.cardboardPickaxe, 1, 32);
		 * stack.addEnchantment(Enchantment.efficiency, 5);
		 * stack.addEnchantment(Enchantment.unbreaking, 3);
		 * makeInput(sampleInputs, stack, BetterStorageItems.cardboardSheet);
		 * }
		 */
		makeInput( sampleInputs, new ItemStack( BetterStorageItems.CARDBOARD_PICKAXE, 1, 32 ), BetterStorageItems.CARDBOARD_SHEET );
		makeInput( sampleInputs, new ItemStack( BetterStorageItems.cardboardHelmet, 1, 11 ),
				new ItemStack( BetterStorageItems.CARDBOARD_CHESTPLATE, 1, 16 ), BetterStorageItems.CARDBOARD_SHEET,
				new ItemStack( BetterStorageItems.CARDBOARD_LEGGINGS, 1, 15 ), new ItemStack( BetterStorageItems.CARDBOARD_BOOTS, 1, 13 ) );
		makeInput( sampleInputs, new ItemStack( BetterStorageItems.cardboardHelmet, 1, 11 * 2 ),
				new ItemStack( BetterStorageItems.CARDBOARD_CHESTPLATE, 1, 16 * 2 ), BetterStorageItems.CARDBOARD_SHEET,
				new ItemStack( BetterStorageItems.CARDBOARD_LEGGINGS, 1, 15 * 2 ), new ItemStack( BetterStorageItems.CARDBOARD_BOOTS, 1, 13 * 2 ),
				BetterStorageItems.CARDBOARD_SHEET );
		makeInput( sampleInputs, new ItemStack( BetterStorageItems.cardboardHelmet, 1, 11 * 3 ),
				new ItemStack( BetterStorageItems.CARDBOARD_CHESTPLATE, 1, 16 * 3 ), BetterStorageItems.CARDBOARD_SHEET,
				new ItemStack( BetterStorageItems.CARDBOARD_LEGGINGS, 1, 15 * 3 ), new ItemStack( BetterStorageItems.CARDBOARD_BOOTS, 1, 13 * 3 ),
				BetterStorageItems.CARDBOARD_SHEET, new ItemStack( BetterStorageItems.CARDBOARD_SHOVEL, 1, 32 ),
				new ItemStack( BetterStorageItems.CARDBOARD_HOE, 1, 32 ), BetterStorageItems.CARDBOARD_SHEET );
		return sampleInputs;
	}

	private void makeInput( List< IRecipeInput[] > sampleInputs, Object... obj )
	{
		final IRecipeInput[] input = new IRecipeInput[9];
		boolean hasCardboardItem = false;
		for( int i = 0; i < obj.length; i++ )
			if( obj[i] != null )
			{
				if( obj[i] instanceof ItemStack )
				{
					final Item item = ( (ItemStack)obj[i] ).getItem();
					if( item == null )
						continue;
					if( item instanceof ICardboardItem )
						hasCardboardItem = true;
				}
				input[i] = BetterStorageCrafting.makeInput( obj[i] );
			}
		if( hasCardboardItem )
			sampleInputs.add( input );
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public List<IRecipeInput> getBlockPossibleInputs() {
	 * return Arrays.<IRecipeInput>asList(RecipeInputCardboard.instance,
	 * new RecipeInputItemStack(new ItemStack(BetterStorageItems.cardboardSheet)));
	 * }
	 */

	@Override
	@SideOnly( Side.CLIENT )
	public List< ItemStack > getBlockPossibleOutputs()
	{
		return Collections.emptyList();
	}

	@Override
	public List< IRecipeInput > getBlockPossibleInputs()
	{
		// TODO Auto-generated method stub
		return null;
	}

	// Utility functions

	/*
	 * private int calculateCost(StackEnchantment ench) {
	 * int cost = 0;
	 * int weight = ench.ench.getWeight();
	 * int level = ench.getLevel();
	 * if (weight > 8) cost += Math.max(level - 2, 0);
	 * else if (weight > 4) cost += level - 1;
	 * else if (weight > 2) cost += level;
	 * else cost = level * 2;
	 * cost += CardboardRecipeHelper.getAdditionalEnchantmentCost(ench);
	 * return cost;
	 * }
	 */
}
