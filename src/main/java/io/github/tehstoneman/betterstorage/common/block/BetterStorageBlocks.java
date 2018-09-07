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
}
