package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageContainerTypes
{
	//@formatter:off
	@ObjectHolder( "crate" )				public static ContainerType< ContainerCrate >				CRATE;
	@ObjectHolder( "reinforced_chest" )		public static ContainerType< ContainerReinforcedChest >		REINFORCED_CHEST;
	@ObjectHolder( "locker" )				public static ContainerType< ContainerBetterStorage >		LOCKER;
	@ObjectHolder( "reinforced_locker" )	public static ContainerType< ContainerReinforcedLocker >	REINFORCED_LOCKER;
	//@formatter:on
}
