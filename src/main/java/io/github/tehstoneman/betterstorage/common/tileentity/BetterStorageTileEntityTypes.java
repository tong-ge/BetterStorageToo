package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageTileEntityTypes
{
	//@formatter:off
	@ObjectHolder( "crate" )				public static TileEntityType< TileEntityCrate >				CRATE;
	@ObjectHolder( "cardboard_box" )		public static TileEntityType< TileEntityCardboardBox >		CARDBOARD_BOX;
	@ObjectHolder( "reinforced_chest" )		public static TileEntityType< TileEntityReinforcedChest >	REINFORCED_CHEST;
	@ObjectHolder( "locker" )				public static TileEntityType< TileEntityLocker >			LOCKER;
	@ObjectHolder( "reinforced_locker" )	public static TileEntityType< TileEntityReinforcedLocker >	REINFORCED_LOCKER;
	//@formatter:on
}
