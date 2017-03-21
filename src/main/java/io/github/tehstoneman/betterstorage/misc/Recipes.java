package io.github.tehstoneman.betterstorage.misc;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.api.crafting.BetterStorageCrafting;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.item.cardboard.CardboardEnchantmentRecipe;
import io.github.tehstoneman.betterstorage.item.cardboard.CardboardRepairRecipe;
import io.github.tehstoneman.betterstorage.item.recipe.ColorRecipe;
import io.github.tehstoneman.betterstorage.item.recipe.CopyKeyRecipe;
import io.github.tehstoneman.betterstorage.item.recipe.DyeRecipe;
import io.github.tehstoneman.betterstorage.item.recipe.LockRecipe;
import io.github.tehstoneman.betterstorage.item.recipe.PresentRecipe;
import io.github.tehstoneman.betterstorage.item.recipe.PresentRemoveNametagRecipe;
import io.github.tehstoneman.betterstorage.tile.ContainerMaterial;
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

		addTileRecipes();
		addItemRecipes();
		addCardboardRecipes();

		GameRegistry.addRecipe( new DyeRecipe() );
		Addon.addRecipesAll();
	}

	private static void registerRecipeSorter()
	{
		// RecipeSorter.register( "betterstorage:drinkinghelmetrecipe", DrinkingHelmetRecipe.class, Category.SHAPED, "" );

		RecipeSorter.register( "betterstorage:dyerecipe", DyeRecipe.class, Category.SHAPELESS, "" );
		RecipeSorter.register( "betterstorage:lockcolorrecipe", LockRecipe.class, Category.SHAPELESS, "" );

		RecipeSorter.register( "betterstorage:copykeyrecipe", CopyKeyRecipe.class, Category.SHAPED, "" );
		RecipeSorter.register( "betterstorage:colorkeyrecipe", ColorRecipe.class, Category.SHAPELESS, "" );
		RecipeSorter.register( "betterstorage:lockrecipe", LockRecipe.class, Category.SHAPED, "" );
	}

	private static void addTileRecipes()
	{
		// Crate recipe
		//@formatter:off
		if( BetterStorageBlocks.CRATE != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.CRATE ),
							"o/o",
							"/ /",
							"o/o",	'o', "plankWood",
									'/', "stickWood" ) );
		//@formatter:on

		// Reinforced chest recipes
		if( BetterStorageBlocks.REINFORCED_CHEST != null )
			for( final EnumReinforced material : EnumReinforced.values() )
				if( material != EnumReinforced.SPECIAL )
					//@formatter:off
					GameRegistry.addRecipe(
							new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.REINFORCED_CHEST, 1, material.getMetadata() ),
									"o#o",
									"#C#",
									"oOo",	'C', Blocks.CHEST,
											'#', "logWood",
											'o', material.getOreDictIngot(),
											'O', material.getOreDictBlock() ) );
					//@formatter:on

		// Locker recipe
		if( BetterStorageBlocks.LOCKER != null )
		{
			//@formatter:off
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.LOCKER ),
							"ooo",
							"o |",
							"ooo",	'o', "plankWood",
									'|', Blocks.TRAPDOOR ) );
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.LOCKER ),
							"ooo",
							"| o",
							"ooo",	'o', "plankWood",
									'|', Blocks.TRAPDOOR ) );
			//@formatter:on

			// Reinforced locker recipes
			if( BetterStorageBlocks.reinforcedLocker != null )
				for( final ContainerMaterial material : ContainerMaterial.getMaterials() )
				{
					final IRecipe recipe = material.getReinforcedRecipe( BetterStorageBlocks.LOCKER, BetterStorageBlocks.reinforcedLocker );
					if( recipe != null )
						GameRegistry.addRecipe( recipe );
				}
		}

		// Armor stand recipe
		//@formatter:off
		if( BetterStorageBlocks.armorStand != null )
			GameRegistry.addShapedRecipe( new ItemStack( BetterStorageBlocks.armorStand ),
					" i ",
					"/i/",
					" s ",	's', new ItemStack( Blocks.STONE_SLAB, 1, 0 ),
							'i', Items.IRON_INGOT,
							'/', Items.STICK );

		// Cardboard box recipe
		if( BetterStorageBlocks.cardboardBox != null && BetterStorageItems.cardboardSheet != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.cardboardBox ),
					"ooo",
					"o o",
					"ooo",	'o', "sheetCardboard" ) );

		// Crafting Station recipe
		if( BetterStorageBlocks.craftingStation != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageBlocks.craftingStation ),
					"B-B",
					"PTP",
					"WCW",	'B', Blocks.STONEBRICK,
							'-', Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
							'P', Blocks.PISTON,
							'T', Blocks.CRAFTING_TABLE,
							'W', "plankWood",
							'C', BetterStorageBlocks.CRATE != null ? BetterStorageBlocks.CRATE : Blocks.CHEST ) );
		//@formatter:on

		// Present recipe
		if( BetterStorageBlocks.present != null && BetterStorageBlocks.cardboardBox != null )
		{
			GameRegistry.addRecipe( new PresentRecipe() );
			BetterStorageCrafting.addStationRecipe( new PresentRemoveNametagRecipe() );
		}

		// Flint Block recipe
		if( BetterStorageBlocks.flintBlock != null )
		{
			//@formatter:off
			GameRegistry.addShapedRecipe( new ItemStack( BetterStorageBlocks.flintBlock ),
					"ooo",
					"ooo",
					"ooo",	'o', Items.FLINT );
			GameRegistry.addShapelessRecipe( new ItemStack( Items.FLINT, 9 ), BetterStorageBlocks.flintBlock );
			//@formatter:on
		}

	}

	private static void addItemRecipes()
	{

		if( BetterStorageItems.KEY != null )
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
			IRecipe keyDyeRecipe = new ColorRecipe( new ItemStack( BetterStorageItems.KEY ),
					new Object[] { new ItemStack( BetterStorageItems.KEY ),
								   "dye" } );
			GameRegistry.addRecipe( keyDyeRecipe );
			keyDyeRecipe = new ColorRecipe( new ItemStack( BetterStorageItems.KEY ),
					new Object[] { new ItemStack( BetterStorageItems.KEY ),
								  "dye",
								  "dye" } );
			GameRegistry.addRecipe( keyDyeRecipe );
			//@formatter:on

			if( BetterStorageItems.LOCK != null )
			{
				// Lock recipe
				//@formatter:off
				final IRecipe lockRecipe = new LockRecipe( new ItemStack( BetterStorageItems.LOCK ),
						new Object[] { " g ",
									   "gkg",
									   "gig", 'g', "ingotGold",
									   		  'k', new ItemStack( BetterStorageItems.KEY, 1, OreDictionary.WILDCARD_VALUE ),
									   		  'i', "ingotIron" } );
				GameRegistry.addRecipe( lockRecipe );
				// Lock color recipe
				IRecipe lockDyeRecipe = new ColorRecipe( new ItemStack( BetterStorageItems.LOCK ),
						new Object[] { new ItemStack( BetterStorageItems.LOCK ),
									   "dye" } );
				GameRegistry.addRecipe( lockDyeRecipe );
				lockDyeRecipe = new ColorRecipe( new ItemStack( BetterStorageItems.LOCK ),
						new Object[] { new ItemStack( BetterStorageItems.LOCK ),
									  "dye",
									  "dye" } );
				GameRegistry.addRecipe( lockDyeRecipe );
				//@formatter:on
			}

			// Keyring recipe
			if( BetterStorageItems.KEYRING != null )
			{
				//@formatter:off
				final IRecipe keyRingRecipe = new ShapedOreRecipe( new ItemStack( BetterStorageItems.KEYRING ),
						new Object[] { "...",
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
		// Cardboard sheet recipe
		if( BetterStorageItems.cardboardSheet != null )
		{
			//@formatter:off
			final IRecipe cardboardSheetRecipe = new ShapelessOreRecipe( new ItemStack( BetterStorageItems.cardboardSheet, 4 ),
					new Object[] { "paper",     "paper", "paper",
								   "paper", "slimeball", "paper",
								   "paper",     "paper", "paper" } );
			GameRegistry.addRecipe( cardboardSheetRecipe );
			//@formatter:on
		}

		// Cardboard helmet recipe
		//@formatter:off
		if( BetterStorageItems.cardboardHelmet != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardHelmet ),
					"ooo",
					"o o",	'o', "sheetCardboard" ) );

		// Cardboard chestplate recipe
		if( BetterStorageItems.cardboardChestplate != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardChestplate ),
							"o o",
							"ooo",
							"ooo",	'o', "sheetCardboard" ) );

		// Cardboard leggings recipe
		if( BetterStorageItems.cardboardLeggings != null )
			GameRegistry.addRecipe(
					new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardLeggings ),
							"ooo",
							"o o",
							"o o",	'o', "sheetCardboard" ) );

		// Cardboard boots recipe
		if( BetterStorageItems.cardboardBoots != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardBoots ),
					"o o",
					"o o",	'o', "sheetCardboard" ) );

		// Cardboard sword recipe
		if( BetterStorageItems.cardboardSword != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardSword ),
					"o",
					"o",
					"/",	'o', "sheetCardboard",
							'/', Items.STICK ) );

		// Cardboard pickaxe recipe
		if( BetterStorageItems.cardboardPickaxe != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardPickaxe ),
					"ooo",
					" / ",
					" / ",	'o', "sheetCardboard",
							'/', Items.STICK ) );

		// Cardboard shovel recipe
		if( BetterStorageItems.cardboardShovel != null )
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardShovel ),
					"o",
					"/",
					"/",	'o', "sheetCardboard",
							'/', Items.STICK ) );

		// Cardboard axe recipe
		if( BetterStorageItems.cardboardAxe != null )
		{
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardAxe ),
					"oo",
					"o/",
					" /",	'o', "sheetCardboard",
							'/', Items.STICK ) );
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardAxe ),
					"oo",
					"/o",
					"/ ",	'o', "sheetCardboard",
							'/', Items.STICK ) );
		}

		// Cardboard hoe recipe
		if( BetterStorageItems.cardboardHoe != null )
		{
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardHoe ),
					"oo",
					" /",
					" /",	'o', "sheetCardboard",
							'/', Items.STICK ) );
			GameRegistry.addRecipe( new ShapedOreRecipe( new ItemStack( BetterStorageItems.cardboardHoe ),
					"oo",
					"/ ",
					"/ ",	'o', "sheetCardboard",
							'/', Items.STICK ) );
		}
		//@formatter:on

		if( BetterStorageItems.anyCardboardItemsEnabled )
		{
			// Crafting Station: Add cardboard enchantment recipe
			BetterStorageCrafting.addStationRecipe( new CardboardEnchantmentRecipe() );

			// Crafting Station: Add cardboard repair recipe
			if( BetterStorageItems.cardboardSheet != null )
				BetterStorageCrafting.addStationRecipe( new CardboardRepairRecipe() );
		}
	}
}
