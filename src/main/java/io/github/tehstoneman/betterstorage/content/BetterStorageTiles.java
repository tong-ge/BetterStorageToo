package io.github.tehstoneman.betterstorage.content;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.tile.TileCardboardBox;
import io.github.tehstoneman.betterstorage.tile.TileCraftingStation;
import io.github.tehstoneman.betterstorage.tile.TileFlintBlock;
import io.github.tehstoneman.betterstorage.tile.TileLockableDoor;
import io.github.tehstoneman.betterstorage.tile.TileLocker;
import io.github.tehstoneman.betterstorage.tile.TilePresent;
import io.github.tehstoneman.betterstorage.tile.TileReinforcedChest;
import io.github.tehstoneman.betterstorage.tile.TileReinforcedLocker;
import io.github.tehstoneman.betterstorage.tile.crate.TileCrate;
import io.github.tehstoneman.betterstorage.tile.stand.TileArmorStand;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BetterStorageTiles
{
	public static TileCrate				crate;
	public static TileReinforcedChest	reinforcedChest;
	public static TileLocker			locker;
	public static TileArmorStand		armorStand;

	// Backpacks removed
	// public static TileBackpack backpack;
	// public static TileEnderBackpack enderBackpack;

	public static TileCardboardBox		cardboardBox;
	public static TileReinforcedLocker	reinforcedLocker;
	public static TileCraftingStation	craftingStation;
	public static TileFlintBlock		flintBlock;
	public static TileLockableDoor		lockableDoor;
	public static TilePresent			present;

	private BetterStorageTiles()
	{}

	public static void initialize()
	{
		crate = MiscUtils.conditionalNew( TileCrate.class, GlobalConfig.crateEnabled );
		reinforcedChest = MiscUtils.conditionalNew( TileReinforcedChest.class, GlobalConfig.reinforcedChestEnabled );
		locker = MiscUtils.conditionalNew( TileLocker.class, GlobalConfig.lockerEnabled );
		armorStand = MiscUtils.conditionalNew( TileArmorStand.class, GlobalConfig.armorStandEnabled );
		cardboardBox = MiscUtils.conditionalNew( TileCardboardBox.class, GlobalConfig.cardboardBoxEnabled );
		reinforcedLocker = MiscUtils.conditionalNew( TileReinforcedLocker.class, GlobalConfig.reinforcedLockerEnabled );
		craftingStation = MiscUtils.conditionalNew( TileCraftingStation.class, GlobalConfig.craftingStationEnabled );
		flintBlock = MiscUtils.conditionalNew( TileFlintBlock.class, GlobalConfig.flintBlockEnabled );
		lockableDoor = MiscUtils.conditionalNew( TileLockableDoor.class, GlobalConfig.lockableDoorEnabled );
		present = MiscUtils.conditionalNew( TilePresent.class, GlobalConfig.presentEnabled );

		Addon.initializeTilesAll();
	}

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		crate.registerItemModels();
		reinforcedChest.registerItemModels();
		locker.registerItemModels();
		armorStand.registerItemModels();
		cardboardBox.registerItemModels();
		reinforcedLocker.registerItemModels();
		craftingStation.registerItemModels();
		flintBlock.registerItemModels();
		lockableDoor.registerItemModels();
		present.registerItemModels();
	}
}
