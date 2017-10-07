package io.github.tehstoneman.betterstorage.client.gui;

import java.util.ArrayList;
import java.util.Set;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.DummyConfigElement.DummyCategoryElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

public class BetterStorageGuiFactory implements IModGuiFactory
{
	@Override
	public void initialize( Minecraft minecraftInstance )
	{
		BetterStorageGuiConfig.initialize( minecraftInstance );
	}

	@Override
	public Class< ? extends GuiScreen > mainConfigGuiClass()
	{
		return BetterStorageGuiConfig.class;
	}

	@Override
	public Set< RuntimeOptionCategoryElement > runtimeGuiCategories()
	{
		return null;
	}

	@Override
	public RuntimeOptionGuiHandler getHandlerFor( RuntimeOptionCategoryElement element )
	{
		return null;
	}

	public static class BetterStorageGuiConfig extends GuiConfig
	{
		private static ArrayList< IConfigElement > configElements;

		public BetterStorageGuiConfig( GuiScreen parentScreen )
		{
			super( parentScreen, configElements, ModInfo.modId, false, false, ModInfo.modName );
		}

		public static void initialize( Minecraft minecraftInstance )
		{
			configElements = new ArrayList<>();

			final Configuration config = BetterStorage.config.getConfig();
			final ConfigElement generalCategory = new ConfigElement( config.getCategory( Configuration.CATEGORY_GENERAL ) );
			final ConfigElement blockCategory = new ConfigElement( config.getCategory( BetterStorageConfig.CATEGORY_BLOCKS ) );
			final ConfigElement itemCategory = new ConfigElement( config.getCategory( BetterStorageConfig.CATEGORY_ITEMS ) );
			final ConfigElement enchantCategory = new ConfigElement( config.getCategory( BetterStorageConfig.CATEGORY_ENCHANTMENTS ) );

			configElements.addAll( generalCategory.getChildElements() );
			configElements.add( new DummyCategoryElement( "block", "config.betterstorage.category.block", blockCategory.getChildElements() ) );
			configElements.add( new DummyCategoryElement( "item", "config.betterstorage.category.item", itemCategory.getChildElements() ) );
			configElements.add( new DummyCategoryElement( "enchant", "config.betterstorage.category.enchantment", enchantCategory.getChildElements() ) );
		}
	}
}
