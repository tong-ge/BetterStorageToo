package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
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
	@ObjectHolder( "lockable_door" )		public static TileEntityType< TileEntityLockableDoor >		LOCKABLE_DOOR;
	//@formatter:on

	@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
	private static class Register
	{
		@SubscribeEvent
		public static void onTileEntityRegistry( final RegistryEvent.Register< TileEntityType< ? > > event )
		{
			final IForgeRegistry< TileEntityType< ? > > registry = event.getRegistry();

			registry.register( TileEntityType.Builder.create( TileEntityCrate::new, BetterStorageBlocks.CRATE ).build( null )
					.setRegistryName( BetterStorageBlocks.CRATE.getRegistryName() ) );
			registry.register( TileEntityType.Builder.create( TileEntityReinforcedChest::new, BetterStorageBlocks.REINFORCED_CHEST ).build( null )
					.setRegistryName( BetterStorageBlocks.REINFORCED_CHEST.getRegistryName() ) );
			registry.register( TileEntityType.Builder.create( TileEntityLocker::new, BetterStorageBlocks.LOCKER ).build( null )
					.setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
			registry.register( TileEntityType.Builder.create( TileEntityReinforcedLocker::new, BetterStorageBlocks.REINFORCED_LOCKER ).build( null )
					.setRegistryName( BetterStorageBlocks.REINFORCED_LOCKER.getRegistryName() ) );
			registry.register( TileEntityType.Builder.create( TileEntityLockableDoor::new, BetterStorageBlocks.LOCKABLE_DOOR ).build( null )
					.setRegistryName( BetterStorageBlocks.LOCKABLE_DOOR.getRegistryName() ) );
			registry.register( TileEntityType.Builder.create( TileEntityCardboardBox::new, BetterStorageBlocks.CARDBOARD_BOX ).build( null )
					.setRegistryName( BetterStorageBlocks.CARDBOARD_BOX.getRegistryName() ) );
		}
	}
}
