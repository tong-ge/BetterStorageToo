package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.addon.Addon;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BetterStorageTileEntities
{
	public static void register()
	{
		GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
		//GameRegistry.registerTileEntity( TileEntityReinforcedChest.class, ModInfo.containerReinforcedChest );
		//GameRegistry.registerTileEntity( TileEntityLocker.class, ModInfo.containerLocker );
		//GameRegistry.registerTileEntity( TileEntityCardboardBox.class, ModInfo.containerCardboardBox );
		//GameRegistry.registerTileEntity( TileEntityReinforcedLocker.class, ModInfo.containerReinforcedLocker );
		// GameRegistry.registerTileEntity( TileEntityCraftingStation.class, ModInfo.containerCraftingStation );
		// GameRegistry.registerTileEntity( TileEntityLockableDoor.class, ModInfo.lockableDoor );
		// GameRegistry.registerTileEntity( TileEntityPresent.class, ModInfo.containerPresent );

		Addon.registerTileEntitesAll();
	}
}
