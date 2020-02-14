package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.Block.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BetterStorageBlocks
{
	public static final DeferredRegister< Block >			BLOCK_REGISTER		= new DeferredRegister<>( ForgeRegistries.BLOCKS, ModInfo.MOD_ID );

	public static RegistryObject< BlockCrate >				CRATE				= BLOCK_REGISTER.register( "crate", () -> new BlockCrate() );
	public static RegistryObject< BlockReinforcedChest >	REINFORCED_CHEST	= BLOCK_REGISTER.register( "reinforced_chest",
			() -> new BlockReinforcedChest() );
	public static RegistryObject< BlockLocker >				LOCKER				= BLOCK_REGISTER.register( "locker", () -> new BlockLocker() );
	public static RegistryObject< BlockReinforcedLocker >	REINFORCED_LOCKER	= BLOCK_REGISTER.register( "reinforced_locker",
			() -> new BlockReinforcedLocker() );
	public static RegistryObject< Block >					BLOCK_FLINT			= BLOCK_REGISTER.register( "block_flint",
			() -> new Block( Properties.create( Material.ROCK, MaterialColor.BLACK ).hardnessAndResistance( 5.0F, 6.0F ) ) );
	public static RegistryObject< BlockLockableDoor >		LOCKABLE_DOOR		= BLOCK_REGISTER.register( "lockable_door",
			() -> new BlockLockableDoor() );
	public static RegistryObject< BlockCardboardBox >		CARDBOARD_BOX		= BLOCK_REGISTER.register( "cardboard_box",
			() -> new BlockCardboardBox() );
	public static RegistryObject< BlockTank >				GLASS_TANK			= BLOCK_REGISTER.register( "glass_tank", () -> new BlockTank() );
}
