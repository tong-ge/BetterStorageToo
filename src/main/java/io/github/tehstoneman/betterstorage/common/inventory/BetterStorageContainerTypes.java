package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder( ModInfo.MOD_ID )
public final class BetterStorageContainerTypes
{
	//@formatter:off
	@ObjectHolder( "crate" )				public static ContainerType< ContainerCrate >				CRATE;
	@ObjectHolder( "reinforced_chest" )		public static ContainerType< ContainerReinforcedChest >		REINFORCED_CHEST;
	@ObjectHolder( "locker" )				public static ContainerType< ContainerLocker >				LOCKER;
	@ObjectHolder( "reinforced_locker" )	public static ContainerType< ContainerReinforcedLocker >	REINFORCED_LOCKER;
	@ObjectHolder( "keyring" )				public static ContainerType< ContainerKeyring >				KEYRING;
	@ObjectHolder( "cardboard_box" )		public static ContainerType< ContainerCardboardBox >		CARDBOARD_BOX;
	//@formatter:on

	@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
	private static class Register
	{
		@SubscribeEvent
		public static void onContainerRegistry( final RegistryEvent.Register< ContainerType< ? > > event )
		{
			final IForgeRegistry< ContainerType< ? > > registry = event.getRegistry();

			registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
			{
				final BlockPos pos = data.readBlockPos();
				return new ContainerCrate( windowID, inv, getClientWorld(), pos );
			} ).setRegistryName( BetterStorageBlocks.CRATE.get().getRegistryName() ) );
			registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
			{
				final BlockPos pos = data.readBlockPos();
				return new ContainerLocker( windowID, inv, getClientWorld(), pos );
			} ).setRegistryName( BetterStorageBlocks.LOCKER.get().getRegistryName() ) );
			registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
			{
				final BlockPos pos = data.readBlockPos();
				return new ContainerReinforcedChest( windowID, inv, getClientWorld(), pos );
			} ).setRegistryName( BetterStorageBlocks.REINFORCED_CHEST.get().getRegistryName() ) );
			registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
			{
				final BlockPos pos = data.readBlockPos();
				return new ContainerReinforcedLocker( windowID, inv, getClientWorld(), pos );
			} ).setRegistryName( BetterStorageBlocks.REINFORCED_LOCKER.get().getRegistryName() ) );
			registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
			{
				final BlockPos pos = data.readBlockPos();
				return new ContainerCardboardBox( windowID, inv, getClientWorld(), pos );
			} ).setRegistryName( BetterStorageBlocks.CARDBOARD_BOX.get().getRegistryName() ) );
			registry.register( IForgeContainerType.create( ( windowID, inv, data ) ->
			{
				final ItemStack keyring = data.readItemStack();
				final int protectedIndex = data.readInt();
				return new ContainerKeyring( windowID, inv, keyring, protectedIndex );
			} ).setRegistryName( BetterStorageItems.KEYRING.get().getRegistryName() ) );
		}

		private static World getClientWorld()
		{
			return Minecraft.getInstance().world;
		}
	}
}
