package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BetterStorageBlocks
{
	public static final DeferredRegister< Block >			REGISTERY			= DeferredRegister.create( ForgeRegistries.BLOCKS, ModInfo.MOD_ID );

	public static RegistryObject< BlockCrate >				CRATE				= REGISTERY.register( "crate", () -> new BlockCrate() );
	public static RegistryObject< BlockReinforcedChest >	REINFORCED_CHEST	= REGISTERY.register( "reinforced_chest",
			() -> new BlockReinforcedChest() );
	public static RegistryObject< BlockLocker >				LOCKER				= REGISTERY.register( "locker", () -> new BlockLocker() );
	public static RegistryObject< BlockReinforcedLocker >	REINFORCED_LOCKER	= REGISTERY.register( "reinforced_locker",
			() -> new BlockReinforcedLocker() );
	public static RegistryObject< Block >					BLOCK_FLINT			= REGISTERY.register( "block_flint",
			() -> new Block( BlockBehaviour.Properties.of( Material.STONE, MaterialColor.COLOR_BLACK ).strength( 5.0F, 6.0F ) ) );
	public static RegistryObject< BlockLockableDoor >		LOCKABLE_DOOR		= REGISTERY.register( "lockable_door",
			() -> new BlockLockableDoor() );
	public static RegistryObject< BlockCardboardBox >		CARDBOARD_BOX		= REGISTERY.register( "cardboard_box",
			() -> new BlockCardboardBox() );
	public static RegistryObject< BlockTank >				GLASS_TANK			= REGISTERY.register( "glass_tank", () -> new BlockTank() );
	/*
	 * public static RegistryObject< FlowingFluidBlock > MILK = REGISTERY.register( "milk",
	 * () -> new FlowingFluidBlock( BetterStorageFluids.MILK, AbstractBlock.Properties.of( FluidMilk.MATERIAL_MILK, DyeColor.WHITE ) ) );
	 */
}
