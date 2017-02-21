package io.github.tehstoneman.betterstorage.tile.entity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.item.ItemStack;
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
		return getMaterial().getChestResource( isConnected() );
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

	@Override
	public boolean canConnect( TileEntityConnectable connectable )
	{
		return connectable instanceof TileEntityReinforcedChest && super.canConnect( connectable )
				&& ( pos.getX() != connectable.getPos().getX() && ( getOrientation() == EnumFacing.SOUTH || getOrientation() == EnumFacing.NORTH )
						|| pos.getZ() != connectable.getPos().getZ()
								&& ( getOrientation() == EnumFacing.EAST || getOrientation() == EnumFacing.WEST ) );
	}

	@Override
	public ItemStack removeStackFromSlot( int index )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getField( int id )
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField( int id, int value )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public int getFieldCount()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear()
	{
		// TODO Auto-generated method stub
	}
}
