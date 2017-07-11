package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import net.minecraft.util.EnumFacing;

public class TileEntityReinforcedChest extends TileEntityLockable
{
	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public AxisAlignedBB getRenderBoundingBox()
	 * {
	 * if( isConnected() )
	 * {
	 * final EnumFacing connected = getConnected();
	 * if( connected == EnumFacing.NORTH )
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0F, 0.9375F, 14F / 16F, 0.9375F );
	 * else
	 * if( connected == EnumFacing.SOUTH )
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 1.0F );
	 * else
	 * if( connected == EnumFacing.WEST )
	 * return new AxisAlignedBB( 0.0F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 0.9375F );
	 * else
	 * if( connected == EnumFacing.EAST )
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 1.0F, 14F / 16F, 0.9375F );
	 * }
	 *
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 0.9375F );
	 * }
	 */

	// TileEntityConnactable stuff
	
	private static EnumFacing[] neighbors = { EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH };

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
		return BetterStorage.config.reinforcedColumns;
	}

	// TileEntityConnactable stuff

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

	public void setCustomInventoryName( String displayName )
	{
		// TODO Auto-generated method stub
	}

	public void setMaterial( EnumReinforced reinforcedMaterial )
	{
		material = reinforcedMaterial;
		markDirty();
	}
}
