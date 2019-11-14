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
	@ObjectHolder( "lockable_door" )		public static BlockLockableDoor		LOCKABLE_DOOR;
	@ObjectHolder( "cardboard_box" )		public static BlockCardboardBox		CARDBOARD_BOX;
	//@formatter:on
}
