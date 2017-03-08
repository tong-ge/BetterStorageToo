package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityReinforcedChest extends TileEntityLockable
{
	@Override
	@SideOnly( Side.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return WorldUtils.getAABB( this, 0, 0, 0, 1, 1, 1 );
	}

	@SideOnly( Side.CLIENT )
	public ResourceLocation getResource()
	{
		return new ResourceLocation( ModInfo.modId,
				"textures/models/chest" + ( isConnected() ? "_large/" : "/" ) + getMaterial().getName() + ".png" );
	}

	@Override
	public void setAttachmentPosition()
	{
		final double x = !isConnected() ? 8 : getOrientation() == EnumFacing.WEST || getOrientation() == EnumFacing.SOUTH ? 0 : 16;
		lockAttachment.setBox( x, 6.5, 0.5, 7, 7, 1 );
	}

	// TileEntityContainer stuff

	@Override
	public int getColumns()
	{
		return BetterStorage.globalConfig.getInteger( GlobalConfig.reinforcedColumns );
	}

	// TileEntityConnactable stuff

	private static EnumFacing[] neighbors = { EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH };

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerReinforcedChest;
	}

	@Override
	public EnumFacing[] getPossibleNeighbors()
	{
		return neighbors;
	}
}
