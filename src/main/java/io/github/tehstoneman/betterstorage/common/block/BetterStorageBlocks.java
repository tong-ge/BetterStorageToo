package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageBlocks
{
	//@formatter:off
	@ObjectHolder( "crate" )				public static BlockCrate			CRATE;
	@ObjectHolder( "reinforced_chest" )		public static BlockReinforcedChest	REINFORCED_CHEST;
	@ObjectHolder( "locker" )				public static BlockLocker			LOCKER;
	@ObjectHolder( "reinforced_locker" )	public static BlockReinforcedLocker REINFORCED_LOCKER;
	@ObjectHolder( "block_flint" )			public static BlockBetterStorage	BLOCK_FLINT;
	// public static BlockLockableDoor LOCKABLE_DOOR = new BlockLockableDoor();
	// public static BlockCardboardBox CARDBOARD_BOX = new BlockCardboardBox();
	//@formatter:on
}
