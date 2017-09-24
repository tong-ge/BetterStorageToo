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

		addBlockRecipes();
		addItemRecipes();
		addCardboardRecipes();

		// Misc. recipes
		GameRegistry.addRecipe( new CardboardColorRecipe() );
		GameRegistry.addRecipe( new KeyColorRecipe() );
		// Addon.addRecipesAll();
	}

	private static void registerRecipeSorter()
	{
		// RecipeSorter.register( "betterstorage:drinkinghelmetrecipe", DrinkingHelmetRecipe.class, Category.SHAPED, "" );

		//RecipeSorter.register( "betterstorage:copykeyrecipe", CopyKeyRecipe.class, Category.SHAPED, "" );
		//RecipeSorter.register( "betterstorage:lockrecipe", LockRecipe.class, Category.SHAPED, "" );
	}

	private static void addBlockRecipes()
	{
		// Crate recipe
		//@formatter:off
		if( BetterStorage.config.crateEnabled )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.CRATE ),
							new Object[] { false,
										   "o/o",
										   "/ /",
										   "o/o",	'o', "plankWood",
										   			'/', "stickWood" } ) );
		//@formatter:on

		// Reinforced chest recipes
		if( BetterStorage.config.reinforcedChestEnabled )
			for( final EnumReinforced material : EnumReinforced.values() )
				//@formatter:off
				GameRegistry.addRecipe(
						new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.REINFORCED_CHEST, 1, material.getMetadata() ),
								new Object[] { false,
											   "o#o",
											   "#C#",
											   "oOo",	'C', Blocks.CHEST,
											   			'#', "logWood",
											   			'o', material.getOreDictIngot(),
											   			'O', material.getOreDictBlock() } ) );
					//@formatter:on

		// Locker recipe
		if( BetterStorage.config.lockerEnabled )
		{
			//@formatter:off
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.LOCKER ),
							new Object[] { "ooo",
										   "o |",
										   "ooo",	'o', "plankWood",
										   			'|', Blocks.TRAPDOOR } ) );
			//@formatter:on

			// Reinforced locker recipes
			if( BetterStorage.config.reinforcedLockerEnabled )
				for( final EnumReinforced material : EnumReinforced.values() )
					//@formatter:off
					GameRegistry.addRecipe(
							new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.REINFORCED_LOCKER, 1, material.getMetadata() ),
									new Object[] { false,
												   "o#o",
												   "#C#",
												   "oOo",	'C', BetterStorageBlocks.LOCKER,
												   			'#', "logWood",
												   			'o', material.getOreDictIngot(),
												   			'O', material.getOreDictBlock() } ) );
						//@formatter:on
		}

		// Cardboard box recipe
		if( BetterStorage.config.cardboardBoxEnabled )
		{
			//@formatter:off
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.CARDBOARD_BOX ),
							new Object[] { false,
										   "ooo",
										   "o o",
										   "ooo",	'o', "sheetCardboard" } ) );
			//@formatter:on
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
			GameRegistry.addShapedRecipe( new ItemStack( BetterStorageBlocks.BLOCK_FLINT ),
					new Object[]{ "ooo",
								  "ooo",
								  "ooo",	'o', Items.FLINT } );
			GameRegistry.addShapelessRecipe( new ItemStack( Items.FLINT, 9 ), BetterStorageBlocks.BLOCK_FLINT );
			//@formatter:on
		}

	}

	private static void addItemRecipes()
	{

		if( BetterStorage.config.keyEnabled )
		{
			// Key recipes
			//@formatter:off
			final IRecipe keyRecipe = new ShapedOreRecipe( new ItemStack( BetterStorageItems.KEY ),
					new Object[] { ".o",
								   ".o",
								   " o", 'o', "ingotGold",
								   		 '.', "nuggetGold" } );
			GameRegistry.addRecipe( keyRecipe );

			final IRecipe keyCopyRecipe = new CopyKeyRecipe( new ItemStack( BetterStorageItems.KEY ),
					new Object[] { ".o",
								   ".o",
								   "ko", 'o', "ingotGold",
								   		 '.', "nuggetGold",
								   		 'k', new ItemStack( BetterStorageItems.KEY ) } );
			GameRegistry.addRecipe( keyCopyRecipe );

			if( BetterStorage.config.lockEnabled )
			{
				// Lock recipe
				//@formatter:off
				final IRecipe lockRecipe = new LockRecipe( new ItemStack( BetterStorageItems.LOCK ),
						new Object[] { " g ",
									   "gkg",
									   "gig", 'g', "ingotGold",
									   		  'k', new ItemStack( BetterStorageItems.KEY ),
									   		  'i', "ingotIron" } );
				GameRegistry.addRecipe( lockRecipe );
			}

			// Keyring recipe
			if( BetterStorage.config.keyringEnabled )
			{
				//@formatter:off
				final IRecipe keyRingRecipe = new ShapedOreRecipe( new ItemStack( BetterStorageItems.KEYRING ),
						new Object[] { false,
									   "...",
									   ". .",
									   "...", '.', "nuggetGold" } );
				GameRegistry.addRecipe( keyRingRecipe );
				//@formatter:on
			}
		}

		// Drinking helmet recipe
		// if( BetterStorageItems.drinkingHelmet != null ) GameRegistry.addRecipe( new DrinkingHelmetRecipe( BetterStorageItems.drinkingHelmet ) );

	}

	private static void addCardboardRecipes()
	{
		//@formatter:off
		// Cardboard sheet recipe
		if( BetterStorage.config.cardboardSheetEnabled )
			GameRegistry.addRecipe(
					new ShapelessOreRecipe(  new ItemStack( BetterStorageItems.CARDBOARD_SHEET, 4 ),
							new Object[] { "paper",     "paper", "paper",
									   	   "paper", "slimeball", "paper",
									   	   "paper",     "paper", "paper" } ) );

		// Cardboard helmet recipe
		if( BetterStorageItems.CARDBOARD_HELMET != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_HELMET ),
							new Object[]{ false,
										  "ooo",
										  "o o",	'o', "sheetCardboard" } ) );

		// Cardboard chestplate recipe
		if( BetterStorageItems.CARDBOARD_CHESTPLATE != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_CHESTPLATE ),
							new Object[]{ false,
										  "o o",
										  "ooo",
										  "ooo",	'o', "sheetCardboard" } ) );

		// Cardboard leggings recipe
		if( BetterStorageItems.CARDBOARD_LEGGINGS != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_LEGGINGS ),
							new Object[]{ false,
										  "ooo",
										  "o o",
										  "o o",	'o', "sheetCardboard" } ) );

		// Cardboard boots recipe
		if( BetterStorageItems.CARDBOARD_BOOTS != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_BOOTS ),
							new Object[]{ false,
										  "o o",
										  "o o",	'o', "sheetCardboard" } ) );

		// Cardboard sword recipe
		if( BetterStorageItems.CARDBOARD_SWORD != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_SWORD ),
							new Object[]{ false,
										  "o",
										  "o",
										  "/",	'o', "sheetCardboard",
										  		'/', "stickWood" } ) );

		// Cardboard pickaxe recipe
		if( BetterStorageItems.CARDBOARD_PICKAXE != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_PICKAXE ),
							new Object[]{ false,
										  "ooo",
										  " / ",
										  " / ",	'o', "sheetCardboard",
										  			'/', "stickWood" } ) );

		// Cardboard shovel recipe
		if( BetterStorageItems.CARDBOARD_SHOVEL != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_SHOVEL ),
							new Object[]{ false,
										  "o",
										  "/",
										  "/",	'o', "sheetCardboard",
								  				'/', "stickWood" } ) );

		// Cardboard axe recipe
		if( BetterStorageItems.CARDBOARD_AXE != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_AXE ),
							new Object[] { "oo",
										   "o/",
										   " /",	'o', "sheetCardboard",
										   			'/', "stickWood" } ) );

		// Cardboard hoe recipe
		if( BetterStorageItems.CARDBOARD_HOE != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.CARDBOARD_HOE ),
							new Object[]{ "oo",
									" /",
									" /",	'o', "sheetCardboard",
											'/', "stickWood" } ) );
		//@formatter:on

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
