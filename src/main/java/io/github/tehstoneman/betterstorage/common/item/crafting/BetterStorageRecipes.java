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
			registry.register( new SpecialRecipeSerializer<>( KeyColorRecipe::new ).setRegistryName( "color_key_special" ) );
			registry.register( new SpecialRecipeSerializer<>( CardboardColorRecipe::new ).setRegistryName( "color_cardboard_special" ) );
		}
	}
}
