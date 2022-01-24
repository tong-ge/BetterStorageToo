package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BetterStorageTileEntityTypes
{
	public static final DeferredRegister< BlockEntityType< ? > >					REGISTERY			= DeferredRegister
			.create( ForgeRegistries.BLOCK_ENTITIES, ModInfo.MOD_ID );

	public static RegistryObject< BlockEntityType< TileEntityCardboardBox > >		CARDBOARD_BOX		= REGISTERY.register( "cardboard_box",
			() -> BlockEntityType.Builder.of( TileEntityCardboardBox::new, BetterStorageBlocks.CARDBOARD_BOX.get() ).build( null ) );
	public static RegistryObject< BlockEntityType< TileEntityCrate > >				CRATE				= REGISTERY.register( "crate",
			() -> BlockEntityType.Builder.of( TileEntityCrate::new, BetterStorageBlocks.CRATE.get() ).build( null ) );
	public static RegistryObject< BlockEntityType< TileEntityLockableDoor > >		LOCKABLE_DOOR		= REGISTERY.register( "lockable_door",
			() -> BlockEntityType.Builder.of( TileEntityLockableDoor::new, BetterStorageBlocks.LOCKABLE_DOOR.get() ).build( null ) );
	public static RegistryObject< BlockEntityType< TileEntityLocker > >				LOCKER				= REGISTERY.register( "locker",
			() -> BlockEntityType.Builder.of( TileEntityLocker::new, BetterStorageBlocks.LOCKER.get() ).build( null ) );
	public static RegistryObject< BlockEntityType< TileEntityReinforcedChest > >	REINFORCED_CHEST	= REGISTERY.register( "reinforced_chest",
			() -> BlockEntityType.Builder.of( TileEntityReinforcedChest::new, BetterStorageBlocks.REINFORCED_CHEST.get() ).build( null ) );
	public static RegistryObject< BlockEntityType< TileEntityReinforcedLocker > >	REINFORCED_LOCKER	= REGISTERY.register( "reinforced_locker",
			() -> BlockEntityType.Builder.of( TileEntityReinforcedLocker::new, BetterStorageBlocks.REINFORCED_LOCKER.get() ).build( null ) );
	public static RegistryObject< BlockEntityType< TileEntityTank > >				GLASS_TANK			= REGISTERY.register( "glass_tank",
			() -> BlockEntityType.Builder.of( TileEntityTank::new, BetterStorageBlocks.GLASS_TANK.get() ).build( null ) );
}
