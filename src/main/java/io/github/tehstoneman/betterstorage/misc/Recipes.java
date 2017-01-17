package io.github.tehstoneman.betterstorage.misc;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import io.github.tehstoneman.betterstorage.item.recipe.RecipeCopyKey;
import io.github.tehstoneman.betterstorage.item.recipe.RecipeDyeKey;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.RecipeSorter;
import net.minecraftforge.oredict.RecipeSorter.Category;
import net.minecraftforge.oredict.ShapedOreRecipe;

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

		// GameRegistry.addRecipe(new DyeRecipe());
		Addon.addRecipesAll();
	}

	private static void registerRecipeSorter()
	{
		// RecipeSorter.register("betterstorage:drinkinghelmetrecipe", DrinkingHelmetRecipe.class, Category.SHAPED, "");
		// RecipeSorter.register("betterstorage:keyrecipe", KeyRecipe.class, Category.SHAPED, "");
		// RecipeSorter.register("betterstorage:lockrecipe", LockRecipe.class, Category.SHAPED, "");

		// RecipeSorter.register("betterstorage:dyerecipe", DyeRecipe.class, Category.SHAPELESS, "");
		// RecipeSorter.register("betterstorage:lockcolorrecipe", LockColorRecipe.class, Category.SHAPELESS, "");

		RecipeSorter.register( "betterstorage:copykeyrecipe", RecipeCopyKey.class, Category.SHAPED, "" );
	}

	private static void addTileRecipes()
	{
		// Crate recipe
		/*
		 * if (BetterStorageTiles.crate != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageTiles.crate),
		 * "o/o",
		 * "/ /",
		 * "o/o", 'o', "plankWood",
		 * '/', "stickWood"));
		 */

		// Reinforced chest recipes
		/*
		 * if (BetterStorageTiles.reinforcedChest != null)
		 * for (ContainerMaterial material : ContainerMaterial.getMaterials()) {
		 * IRecipe recipe = material.getReinforcedRecipe(Blocks.chest, BetterStorageTiles.reinforcedChest);
		 * if (recipe != null) GameRegistry.addRecipe(recipe);
		 * }
		 */

		// Locker recipe
		/*
		 * if (BetterStorageTiles.locker != null) {
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageTiles.locker),
		 * "ooo",
		 * "o |",
		 * "ooo", 'o', "plankWood",
		 * '|', Blocks.trapdoor));
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageTiles.locker),
		 * "ooo",
		 * "| o",
		 * "ooo", 'o', "plankWood",
		 * '|', Blocks.trapdoor));
		 *
		 * // Reinforced locker recipes
		 * if (BetterStorageTiles.reinforcedLocker != null)
		 * for (ContainerMaterial material : ContainerMaterial.getMaterials()) {
		 * IRecipe recipe = material.getReinforcedRecipe(BetterStorageTiles.locker, BetterStorageTiles.reinforcedLocker);
		 * if (recipe != null) GameRegistry.addRecipe(recipe);
		 * }
		 * }
		 */

		// Armor stand recipe
		/*
		 * if (BetterStorageTiles.armorStand != null)
		 * GameRegistry.addShapedRecipe(new ItemStack(BetterStorageTiles.armorStand),
		 * " i ",
		 * "/i/",
		 * " s ", 's', new ItemStack(Blocks.stone_slab, 1, 0),
		 * 'i', Items.iron_ingot,
		 * '/', Items.stick);
		 */

		// Backpack recipe
		/*
		 * if (BetterStorageTiles.backpack != null)
		 * GameRegistry.addShapedRecipe(new ItemStack(BetterStorageItems.itemBackpack),
		 * "#i#",
		 * "#O#",
		 * "###", '#', Items.leather,
		 * 'O', Blocks.wool,
		 * 'i', Items.gold_ingot);
		 */

		// Cardboard box recipe
		/*
		 * if ((BetterStorageTiles.cardboardBox != null) &&
		 * (BetterStorageItems.cardboardSheet != null))
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageTiles.cardboardBox),
		 * "ooo",
		 * "o o",
		 * "ooo", 'o', "sheetCardboard"));
		 */

		// Crafting Station recipe
		/*
		 * if (BetterStorageTiles.craftingStation != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageTiles.craftingStation),
		 * "B-B",
		 * "PTP",
		 * "WCW", 'B', Blocks.stonebrick,
		 * '-', Blocks.light_weighted_pressure_plate,
		 * 'P', Blocks.piston,
		 * 'T', Blocks.crafting_table,
		 * 'W', "plankWood",
		 * 'C', ((BetterStorageTiles.crate != null) ? BetterStorageTiles.crate : Blocks.chest)));
		 */

		// Present recipe
		/*
		 * if ((BetterStorageTiles.present != null) &&
		 * (BetterStorageTiles.cardboardBox != null)) {
		 * GameRegistry.addRecipe(new PresentRecipe());
		 * BetterStorageCrafting.addStationRecipe(new PresentRemoveNametagRecipe());
		 * }
		 */

		// Flint Block recipe
		/*
		 * if (BetterStorageTiles.flintBlock != null) {
		 * GameRegistry.addShapedRecipe(new ItemStack(BetterStorageTiles.flintBlock),
		 * "ooo",
		 * "ooo",
		 * "ooo", 'o', Items.flint);
		 * GameRegistry.addShapelessRecipe(new ItemStack(Items.flint, 9), BetterStorageTiles.flintBlock);
		 * }
		 */
	}

	private static void addItemRecipes()
	{

		if( BetterStorageItems.key != null )
		{
			// Key recipes
			//formatter:off
			IRecipe keyRecipe = new ShapedOreRecipe( new ItemStack( BetterStorageItems.key ),
					new Object[] { ".o",
								   ".o",
								   " o", 'o', "ingotGold",
								   		 '.', "nuggetGold" } );
			GameRegistry.addRecipe( keyRecipe );
			IRecipe keyCopyRecipe = new RecipeCopyKey( new ItemStack( BetterStorageItems.key ),
					new Object[] { ".o",
								   ".o",
								   "ko", 'o', "ingotGold",
								   		 '.', "nuggetGold",
								   		 'k', new ItemStack( BetterStorageItems.key, 1, OreDictionary.WILDCARD_VALUE ) } );
			GameRegistry.addRecipe( keyCopyRecipe );
			IRecipe keyDyeRecipe = new RecipeDyeKey( new ItemStack( BetterStorageItems.key, 1, 1 ),
					new Object[] { new ItemStack( BetterStorageItems.key, 1, OreDictionary.WILDCARD_VALUE ),
								   "dye" } );
			GameRegistry.addRecipe( keyDyeRecipe );
			keyDyeRecipe = new RecipeDyeKey( new ItemStack( BetterStorageItems.key, 1, 2 ),
					new Object[] { new ItemStack( BetterStorageItems.key, 1, OreDictionary.WILDCARD_VALUE ),
								  "dye",
								  "dye" } );
			GameRegistry.addRecipe( keyDyeRecipe );
			//formatter:on
		}

		/*
		 * if (BetterStorageItems.lock != null) {
		 * // Lock recipe
		 * if (BetterStorageItems.key != null)
		 * GameRegistry.addRecipe(LockRecipe.createLockRecipe());
		 * // Lock color recipe
		 * GameRegistry.addRecipe(LockColorRecipe.createLockColorRecipe());
		 * }
		 */

		// Keyring recipe
		/*
		 * if (BetterStorageItems.keyring != null)
		 * GameRegistry.addShapedRecipe(new ItemStack(BetterStorageItems.keyring),
		 * "...",
		 * ". .",
		 * "...", '.', Items.gold_nugget);
		 */

		// Drinking helmet recipe
		/*
		 * if (BetterStorageItems.drinkingHelmet != null)
		 * GameRegistry.addRecipe(new DrinkingHelmetRecipe(BetterStorageItems.drinkingHelmet));
		 */
	}

	private static void addCardboardRecipes()
	{
		// Cardboard sheet recipe
		/*
		 * if (BetterStorageItems.cardboardSheet != null) {
		 * GameRegistry.addShapelessRecipe(new ItemStack(BetterStorageItems.cardboardSheet, 4),
		 * Items.paper, Items.paper, Items.paper,
		 * Items.paper, Items.paper, Items.paper,
		 * Items.paper, Items.paper, Items.slime_ball);
		 * }
		 */

		// Cardboard helmet recipe
		/*
		 * if (BetterStorageItems.cardboardHelmet != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardHelmet),
		 * "ooo",
		 * "o o", 'o', "sheetCardboard"));
		 */
		// Cardboard chestplate recipe
		/*
		 * if (BetterStorageItems.cardboardChestplate != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardChestplate),
		 * "o o",
		 * "ooo",
		 * "ooo", 'o', "sheetCardboard"));
		 */
		// Cardboard leggings recipe
		/*
		 * if (BetterStorageItems.cardboardLeggings != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardLeggings),
		 * "ooo",
		 * "o o",
		 * "o o", 'o', "sheetCardboard"));
		 */
		// Cardboard boots recipe
		/*
		 * if (BetterStorageItems.cardboardBoots != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardBoots),
		 * "o o",
		 * "o o", 'o', "sheetCardboard"));
		 */

		// Cardboard sword recipe
		/*
		 * if (BetterStorageItems.cardboardSword != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardSword),
		 * "o",
		 * "o",
		 * "/", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 */
		// Cardboard pickaxe recipe
		/*
		 * if (BetterStorageItems.cardboardPickaxe != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardPickaxe),
		 * "ooo",
		 * " / ",
		 * " / ", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 */
		// Cardboard shovel recipe
		/*
		 * if (BetterStorageItems.cardboardShovel != null)
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardShovel),
		 * "o",
		 * "/",
		 * "/", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 */

		// Cardboard axe recipe
		/*
		 * if (BetterStorageItems.cardboardAxe != null) {
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardAxe),
		 * "oo",
		 * "o/",
		 * " /", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardAxe),
		 * "oo",
		 * "/o",
		 * "/ ", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 * }
		 */

		// Cardboard hoe recipe
		/*
		 * if (BetterStorageItems.cardboardHoe != null) {
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardHoe),
		 * "oo",
		 * " /",
		 * " /", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 * GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(BetterStorageItems.cardboardHoe),
		 * "oo",
		 * "/ ",
		 * "/ ", 'o', "sheetCardboard",
		 * '/', Items.stick));
		 * }
		 */

		/*
		 * if (BetterStorageItems.anyCardboardItemsEnabled) {
		 * // Crafting Station: Add cardboard enchantment recipe
		 * BetterStorageCrafting.addStationRecipe(new CardboardEnchantmentRecipe());
		 *
		 * // Crafting Station: Add cardboard repair recipe
		 * if (BetterStorageItems.cardboardSheet != null)
		 * BetterStorageCrafting.addStationRecipe(new CardboardRepairRecipe());
		 * }
		 */
	}
}