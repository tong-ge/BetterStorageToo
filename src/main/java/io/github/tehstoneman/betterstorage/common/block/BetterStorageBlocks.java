package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class BetterStorageBlocks
{
	public static BlockCrate			CRATE				= new BlockCrate();
	public static BlockReinforcedChest	REINFORCED_CHEST	= new BlockReinforcedChest();
	public static BlockLocker			LOCKER				= new BlockLocker();
	public static BlockReinforcedLocker	REINFORCED_LOCKER	= new BlockReinforcedLocker();
	public static BlockFlintBlock		BLOCK_FLINT			= new BlockFlintBlock();
	public static BlockLockableDoor		LOCKABLE_DOOR		= new BlockLockableDoor();
	public static BlockCardboardBox		CARDBOARD_BOX		= new BlockCardboardBox();

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		final ItemModelMesher mesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		if( BetterStorage.config.crateEnabled )
			CRATE.registerItemModels( mesher );
		if( BetterStorage.config.reinforcedChestEnabled )
			REINFORCED_CHEST.registerItemModels( mesher );
		/*
		 * if( BetterStorage.config.lockerEnabled )
		 * {
		 * LOCKER.registerItemModels();
		 * if( BetterStorage.config.reinforcedLockerEnabled )
		 * REINFORCED_LOCKER.registerItemModels();
		 * }
		 * if( BetterStorage.config.flintBlockEnabled )
		 * BLOCK_FLINT.registerItemModels();
		 * if( BetterStorage.config.cardboardBoxEnabled )
		 * CARDBOARD_BOX.registerItemModels();
		 */
	}
}
