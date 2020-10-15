package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BetterStorageTileEntityTypes
{
	public static final DeferredRegister< TileEntityType< ? > >						REGISTERY			= DeferredRegister
			.create( ForgeRegistries.TILE_ENTITIES, ModInfo.MOD_ID );

	public static RegistryObject< TileEntityType< TileEntityCardboardBox > >		CARDBOARD_BOX		= REGISTERY.register( "cardboard_box",
			() -> TileEntityType.Builder.create( TileEntityCardboardBox::new, BetterStorageBlocks.CARDBOARD_BOX.get() ).build( null ) );
	public static RegistryObject< TileEntityType< TileEntityCrate > >				CRATE				= REGISTERY.register( "crate",
			() -> TileEntityType.Builder.create( TileEntityCrate::new, BetterStorageBlocks.CRATE.get() ).build( null ) );
	public static RegistryObject< TileEntityType< TileEntityLockableDoor > >		LOCKABLE_DOOR		= REGISTERY.register( "lockable_door",
			() -> TileEntityType.Builder.create( TileEntityLockableDoor::new, BetterStorageBlocks.LOCKABLE_DOOR.get() ).build( null ) );
	public static RegistryObject< TileEntityType< TileEntityLocker > >				LOCKER				= REGISTERY.register( "locker",
			() -> TileEntityType.Builder.create( TileEntityLocker::new, BetterStorageBlocks.LOCKER.get() ).build( null ) );
	public static RegistryObject< TileEntityType< TileEntityReinforcedChest > >		REINFORCED_CHEST	= REGISTERY.register( "reinforced_chest",
			() -> TileEntityType.Builder.create( TileEntityReinforcedChest::new, BetterStorageBlocks.REINFORCED_CHEST.get() ).build( null ) );
	public static RegistryObject< TileEntityType< TileEntityReinforcedLocker > >	REINFORCED_LOCKER	= REGISTERY.register( "reinforced_locker",
			() -> TileEntityType.Builder.create( TileEntityReinforcedLocker::new, BetterStorageBlocks.REINFORCED_LOCKER.get() ).build( null ) );
	public static RegistryObject< TileEntityType< TileEntityTank > >				GLASS_TANK			= REGISTERY.register( "glass_tank",
			() -> TileEntityType.Builder.create( TileEntityTank::new, BetterStorageBlocks.GLASS_TANK.get() ).build( null ) );
}
