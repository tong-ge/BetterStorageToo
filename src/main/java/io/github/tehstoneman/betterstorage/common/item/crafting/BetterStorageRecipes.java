package io.github.tehstoneman.betterstorage.common.item.crafting;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.SpecialRecipeSerializer;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageRecipes
{
	//@formatter:off
	@ObjectHolder( "copy_key_shaped" )			public static IRecipeSerializer< CopyKeyRecipe >		COPY_KEY;
	@ObjectHolder( "lock_shaped" )				public static IRecipeSerializer< KeyLockRecipe >		LOCK;
	@ObjectHolder( "color_key_special" )		public static IRecipeSerializer< KeyColorRecipe >		COLOR_KEY;
	@ObjectHolder( "color_cardboard_special" )	public static IRecipeSerializer< CardboardColorRecipe >	COLOR_CARDBOARD;
	//@formatter:on

	@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
	public static class Registerstration
	{
		@SubscribeEvent
		public static void RegisterRecipies( Register< IRecipeSerializer< ? > > event )
		{
			final IForgeRegistry< IRecipeSerializer< ? > > registry = event.getRegistry();

			registry.register( new CopyKeyRecipe.Serializer().setRegistryName( "copy_key_shaped" ) );
			registry.register( new SpecialRecipeSerializer<>( KeyLockRecipe::new ).setRegistryName( "lock_shaped" ) );
			registry.register( new SpecialRecipeSerializer<>( KeyColorRecipe::new ).setRegistryName( "color_key_special" ) );
			registry.register( new SpecialRecipeSerializer<>( CardboardColorRecipe::new ).setRegistryName( "color_cardboard_special" ) );
		}
	}

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
