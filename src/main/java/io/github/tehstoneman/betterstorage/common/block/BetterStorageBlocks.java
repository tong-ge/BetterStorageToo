package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageBlocks
{
	//@formatter:off
	// public static BlockCrate CRATE = new BlockCrate();
	// public static BlockReinforcedChest REINFORCED_CHEST = new BlockReinforcedChest();
	@ObjectHolder( "locker" )		public static BlockLocker			LOCKER;
	// public static BlockReinforcedLocker REINFORCED_LOCKER = new BlockReinforcedLocker();
	@ObjectHolder( "block_flint" )	public static BlockBetterStorage	BLOCK_FLINT;
	// public static BlockBetterStorage BLOCK_FLINT = new BlockBetterStorage( Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance(
	// 1.5F, 6.0F ) );
	// public static BlockLockableDoor LOCKABLE_DOOR = new BlockLockableDoor();
	// public static BlockCardboardBox CARDBOARD_BOX = new BlockCardboardBox();
	//@formatter:on
}
