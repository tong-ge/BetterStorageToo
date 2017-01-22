package io.github.tehstoneman.betterstorage.content;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.tile.crate.TileEntityCrate;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BetterStorageTileEntities
{
	private BetterStorageTileEntities()
	{}

	public static void register()
	{
		GameRegistry.registerTileEntity(TileEntityCrate.class, Constants.containerCrate);
		// GameRegistry.registerTileEntity(TileEntityReinforcedChest.class, Constants.containerReinforcedChest);
		// GameRegistry.registerTileEntity(TileEntityLocker.class, Constants.containerLocker);
		// GameRegistry.registerTileEntity(TileEntityArmorStand.class, Constants.containerArmorStand);
		// GameRegistry.registerTileEntity(TileEntityBackpack.class, Constants.containerBackpack);
		// GameRegistry.registerTileEntity(TileEntityCardboardBox.class, Constants.containerCardboardBox);
		// GameRegistry.registerTileEntity(TileEntityReinforcedLocker.class, Constants.containerReinforcedLocker);
		// GameRegistry.registerTileEntity(TileEntityCraftingStation.class, Constants.containerCraftingStation);
		// GameRegistry.registerTileEntity(TileEntityPresent.class, Constants.containerPresent);
		// GameRegistry.registerTileEntity(TileEntityLockableDoor.class, Constants.lockableDoor);

		Addon.registerTileEntitesAll();
	}
}
