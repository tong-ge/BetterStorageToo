package io.github.tehstoneman.betterstorage.common.item.crafting;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public final class Recipes
{
	private Recipes()
	{}

	public static void add()
	{
		registerRecipeSorter();

		//addBlockRecipes();
		//addItemRecipes();
		//addCardboardRecipes();

		// Misc. recipes
		//GameRegistry.addRecipe( new CardboardColorRecipe() );
		//GameRegistry.addRecipe( new KeyColorRecipe() );
		// Addon.addRecipesAll();
	}

	private static void registerRecipeSorter()
	{
		// RecipeSorter.register( "betterstorage:drinkinghelmetrecipe", DrinkingHelmetRecipe.class, Category.SHAPED, "" );

		RecipeSorter.register( "betterstorage:cardboardColorRecipe", CardboardColorRecipe.class, Category.SHAPELESS, "" );
		RecipeSorter.register( "betterstorage:keyColorRecipe", KeyColorRecipe.class, Category.SHAPELESS, "" );
	}

	private static void addBlockRecipes()
	{
		// Cardboard box recipe
		if( BetterStorage.config.cardboardBoxEnabled )
		{
			//@formatter:off
/*			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.CARDBOARD_BOX ),
							new Object[] { false,
										   "ooo",
										   "o o",
										   "ooo",	'o', "sheetCardboard" } ) );
*/			//@formatter:on
		}

		// Crafting Station recipe
		/*if( BetterStorageBlocks.CRAFTING_STATION != null )
			//@formatter:off
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.CRAFTING_STATION ),
							new Object[]{ false,
										  "B-B",
										  "PTP",
										  "WCW",	'B', Blocks.STONEBRICK,
										  			'-', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
										  			'P', Blocks.PISTON,
										  			'T', "workbench",
										  			'W', "plankWood",
										  			'C', BetterStorageBlocks.CRATE != null ? BetterStorageBlocks.CRATE : "chestWood" } ) );
			//@formatter:on
*/
		// Present recipe
		/*
		 * if( BetterStorageBlocks.PRESENT != null && BetterStorageBlocks.CARDBOARD_BOX != null )
		 * {
		 * GameRegistry.addRecipe( new PresentRecipe() );
		 * BetterStorageCrafting.addStationRecipe( new PresentRemoveNametagRecipe() );
		 * }
		 */

		// Flint Block recipe
		if( BetterStorage.config.flintBlockEnabled )
		{
			//@formatter:off
/*			GameRegistry.addShapedRecipe( new ItemStack( BetterStorageBlocks.BLOCK_FLINT ),
					new Object[]{ "ooo",
								  "ooo",
								  "ooo",	'o', Items.FLINT } );
			GameRegistry.addShapelessRecipe( new ItemStack( Items.FLINT, 9 ), BetterStorageBlocks.BLOCK_FLINT );
*/			//@formatter:on
		}

	}

	private static void addItemRecipes()
	{
		// Drinking helmet recipe
		// if( BetterStorageItems.drinkingHelmet != null ) GameRegistry.addRecipe( new DrinkingHelmetRecipe( BetterStorageItems.drinkingHelmet ) );

	}

	private static void addCardboardRecipes()
	{
		//@formatter:off
		// Cardboard sheet recipe
/*		if( BetterStorage.config.cardboardSheetEnabled )
			GameRegistry.addRecipe(
					new ShapelessOreRecipe(  new ItemStack( BetterStorageItems.CARDBOARD_SHEET, 4 ),
							new Object[] { "paper",     "paper", "paper",
									   	   "paper", "slimeball", "paper",
									   	   "paper",     "paper", "paper" } ) );
*/
		// Cardboard helmet recipe
/*		if( BetterStorage.config.cardboardHelmetEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_HELMET ),
							new Object[]{ false,
										  "ooo",
										  "o o",	'o', "sheetCardboard" } ) );
*/
		// Cardboard chestplate recipe
/*		if( BetterStorage.config.cardboardChestplateEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_CHESTPLATE ),
							new Object[]{ false,
										  "o o",
										  "ooo",
										  "ooo",	'o', "sheetCardboard" } ) );
*/
		// Cardboard leggings recipe
/*		if( BetterStorage.config.cardboardLeggingsEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_LEGGINGS ),
							new Object[]{ false,
										  "ooo",
										  "o o",
										  "o o",	'o', "sheetCardboard" } ) );
*/
		// Cardboard boots recipe
/*		if( BetterStorage.config.cardboardBootsEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_BOOTS ),
							new Object[]{ false,
										  "o o",
										  "o o",	'o', "sheetCardboard" } ) );
*/
		// Cardboard sword recipe
/*		if( BetterStorage.config.cardboardSwordEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_SWORD ),
							new Object[]{ false,
										  "o",
										  "o",
										  "/",	'o', "sheetCardboard",
										  		'/', "stickWood" } ) );
*/
		// Cardboard pickaxe recipe
/*		if( BetterStorage.config.cardboardPickaxeEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_PICKAXE ),
							new Object[]{ false,
										  "ooo",
										  " / ",
										  " / ",	'o', "sheetCardboard",
										  			'/', "stickWood" } ) );
*/
		// Cardboard shovel recipe
/*		if( BetterStorage.config.cardboardShovelEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_SHOVEL ),
							new Object[]{ false,
										  "o",
										  "/",
										  "/",	'o', "sheetCardboard",
								  				'/', "stickWood" } ) );
*/
		// Cardboard axe recipe
/*		if( BetterStorage.config.cardboardAxeEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_AXE ),
							new Object[] { "oo",
										   "o/",
										   " /",	'o', "sheetCardboard",
										   			'/', "stickWood" } ) );
*/
		// Cardboard hoe recipe
/*		if( BetterStorage.config.cardboardHoeEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_HOE ),
							new Object[]{ "oo",
									" /",
									" /",	'o', "sheetCardboard",
											'/', "stickWood" } ) );
*/		//@formatter:on

		/*
		 * if( BetterStorageItems.anyCardboardItemsEnabled )
		 * {
		 * // Crafting Station: Add cardboard enchantment recipe
		 * BetterStorageCrafting.addStationRecipe( new CardboardEnchantmentRecipe() );
		 *
		 * // Crafting Station: Add cardboard repair recipe
		 * if( BetterStorageItems.CARDBOARD_SHEET != null )
		 * BetterStorageCrafting.addStationRecipe( new CardboardRepairRecipe() );
		 * }
		 */
	}
}
