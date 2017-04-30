package io.github.tehstoneman.betterstorage.addon;

import java.util.ArrayList;
import java.util.List;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class Addon
{
	private static final List< Addon > addons = new ArrayList<>();

	public static void initialize()
	{
		// if (Loader.isModLoaded("Thaumcraft")) new ThaumcraftAddon();
		// if (Loader.isModLoaded("NotEnoughItems")) new NEIAddon();
		// if (Loader.isModLoaded("armourersWorkshop")) new AWAddon();
		// if (Loader.isModLoaded("MineTweaker3")) new MineTweakerAddon();
	}

	public static void setupConfigsAll()
	{
		for( final Addon addon : addons )
			addon.setupConfig();
	}

	public static void initializeTilesAll()
	{
		for( final Addon addon : addons )
			addon.initializeBlocks();
	}

	public static void initializeItemsAll()
	{
		for( final Addon addon : addons )
			addon.initializeItems();
	}

	public static void addRecipesAll()
	{
		for( final Addon addon : addons )
			addon.addRecipes();
	}

	public static void registerEntitesAll()
	{
		for( final Addon addon : addons )
			addon.registerEntities();
	}

	public static void registerTileEntitesAll()
	{
		for( final Addon addon : addons )
			addon.registerTileEntities();
	}

	@SideOnly( Side.CLIENT )
	public static void registerRenderersAll()
	{
		for( final Addon addon : addons )
			addon.registerRenderers();
	}

	public static void postInitializeAll()
	{
		for( final Addon addon : addons )
			addon.postInitialize();
	}

	public final String modName;

	public Addon( String modName )
	{
		this.modName = modName;
		addons.add( this );
	}

	public void setupConfig()
	{}

	public void initializeItems()
	{}

	public void initializeBlocks()
	{}

	public void addRecipes()
	{}

	public void registerEntities()
	{}

	public void registerTileEntities()
	{}

	@SideOnly( Side.CLIENT )
	public void registerRenderers()
	{}

	public void postInitialize()
	{}
}
