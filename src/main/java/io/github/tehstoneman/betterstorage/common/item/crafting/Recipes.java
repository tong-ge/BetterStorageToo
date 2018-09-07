package io.github.tehstoneman.betterstorage.common.item.crafting;

public final class Recipes
{
	private Recipes()
	{}

	public static void add()
	{
		registerRecipeSorter();

		// addBlockRecipes();
		// addItemRecipes();
		// addCardboardRecipes();

		// Misc. recipes
		// GameRegistry.addRecipe( new CardboardColorRecipe() );
		// GameRegistry.addRecipe( new KeyColorRecipe() );
		// Addon.addRecipesAll();
	}

	private static void registerRecipeSorter()
	{
		// RecipeSorter.register( "betterstorage:drinkinghelmetrecipe", DrinkingHelmetRecipe.class, Category.SHAPED, "" );

		// RecipeSorter.register( "betterstorage:cardboardColorRecipe", CardboardColorRecipe.class, Category.SHAPELESS, "" );
		// RecipeSorter.register( "betterstorage:keyColorRecipe", KeyColorRecipe.class, Category.SHAPELESS, "" );
	}

	private static void addBlockRecipes()
	{
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
	}

	private static void addItemRecipes()
	{
		// Drinking helmet recipe
		// if( BetterStorageItems.drinkingHelmet != null ) GameRegistry.addRecipe( new DrinkingHelmetRecipe( BetterStorageItems.drinkingHelmet ) );

	}
}
