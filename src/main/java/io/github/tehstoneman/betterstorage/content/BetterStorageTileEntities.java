package io.github.tehstoneman.betterstorage.content;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityCardboardBox;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityCraftingStation;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityLockableDoor;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityPresent;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityReinforcedLocker;
import io.github.tehstoneman.betterstorage.tile.stand.TileEntityArmorStand;
import net.minecraftforge.fml.common.registry.GameRegistry;

public final class BetterStorageTileEntities
{
	private BetterStorageTileEntities()
	{}

	public static void register()
	{
		GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
		GameRegistry.registerTileEntity( TileEntityReinforcedChest.class, ModInfo.containerReinforcedChest );
		GameRegistry.registerTileEntity( TileEntityLocker.class, ModInfo.containerLocker );
		GameRegistry.registerTileEntity( TileEntityArmorStand.class, ModInfo.containerArmorStand );
		GameRegistry.registerTileEntity( TileEntityCardboardBox.class, ModInfo.containerCardboardBox );
		GameRegistry.registerTileEntity( TileEntityReinforcedLocker.class, ModInfo.containerReinforcedLocker );
		GameRegistry.registerTileEntity( TileEntityCraftingStation.class, ModInfo.containerCraftingStation );
		GameRegistry.registerTileEntity( TileEntityLockableDoor.class, ModInfo.lockableDoor );
		GameRegistry.registerTileEntity( TileEntityPresent.class, ModInfo.containerPresent );

		Addon.registerTileEntitesAll();
	}
}
