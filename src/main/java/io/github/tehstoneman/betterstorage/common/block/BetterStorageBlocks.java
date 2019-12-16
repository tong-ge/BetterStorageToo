package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
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
	
	@Mod.EventBusSubscriber( bus = Mod.EventBusSubscriber.Bus.MOD )
	private static class Register
	{
		@SubscribeEvent
		public static void onBlocksRegistry( final RegistryEvent.Register< Block > event )
		{
			final IForgeRegistry< Block > registry = event.getRegistry();

			registry.register( new BlockCrate().setRegistryName( ModInfo.MOD_ID, "crate" ) );
			registry.register( new BlockReinforcedChest().setRegistryName( ModInfo.MOD_ID, "reinforced_chest" ) );
			registry.register( new BlockLocker().setRegistryName( ModInfo.MOD_ID, "locker" ) );
			registry.register( new BlockReinforcedLocker().setRegistryName( ModInfo.MOD_ID, "reinforced_locker" ) );
			registry.register( new BlockLockableDoor().setRegistryName( ModInfo.MOD_ID, "lockable_door" ) );
			registry.register( new BlockCardboardBox().setRegistryName( ModInfo.MOD_ID, "cardboard_box" ) );
			registry.register( new BlockBetterStorage( Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 5.0F, 6.0F ) )
					.setRegistryName( ModInfo.MOD_ID, "block_flint" ) );
		}
	}
}
