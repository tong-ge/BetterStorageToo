package io.github.tehstoneman.betterstorage.common.tileentity;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityReinforcedChest extends TileEntity implements IChestLid, ITickable // TileEntityLockable
{
	public ItemStackHandler		inventory;
	protected ITextComponent	customName;

	protected TileEntityReinforcedChest( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
		inventory = new ItemStackHandler( 33 );
	}

	public TileEntityReinforcedChest()
	{
		this( BetterStorageTileEntityTypes.REINFORCED_CHEST );
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB( pos.add( -1, 0, -1 ), pos.add( 2, 2, 2 ) );
	}

	// TileEntityConnactable stuff

	// private static EnumFacing[] neighbors = { EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH };

	/*
	 * @Override
	 * public void setAttachmentPosition()
	 * {
	 * // final double x = !isConnected() ? 8 : getOrientation() == EnumFacing.WEST || getOrientation() == EnumFacing.SOUTH ? 0 : 16;
	 * // lockAttachment.setBox( x, 6.5, 0.5, 7, 7, 1 );
	 * }
	 */

	// TileEntityContainer stuff

	/*
	 * @Override
	 * public int getColumns()
	 * {
	 * return 13;
	 * // return BetterStorage.config.reinforcedColumns;
	 * }
	 */

	// TileEntityConnactable stuff

	/*
	 * @Override
	 * protected String getConnectableName()
	 * {
	 * return ModInfo.containerReinforcedChest;
	 * }
	 */

	/*
	 * @Override
	 * public EnumFacing[] getPossibleNeighbors()
	 * {
	 * // final EnumFacing facing = getOrientation();
	 * // return new EnumFacing[] { facing.rotateY(), facing.rotateYCCW() };
	 * return neighbors;
	 * }
	 */

	/*
	 * public void setCustomInventoryName( String displayName )
	 * {
	 * // TODO Auto-generated method stub
	 * }
	 */

	/*
	 * public boolean canConnect( TileEntityConnectable connectable )
	 * {
	 * if( connectable instanceof TileEntityReinforcedChest )
	 * return connectable != null && // check for null
	 * !isConnected() && !connectable.isConnected() && // Make sure the containers are not already connected.
	 * getBlockType() == connectable.getBlockType() && // check for same block type
	 * getOrientation() == connectable.getOrientation() && // check for same orientation
	 * getMaterial() == ((TileEntityReinforcedChest)connectable).getMaterial(); // check for same material
	 * return super.canConnect( connectable );
	 * }
	 */

	/*
	 * @Override
	 * public void onBlockDestroyed()
	 * {
	 * super.onBlockDestroyed();
	 *
	 * // Don't drop an empty cardboard box in creative.
	 *
	 * if( !brokenInCreative )
	 * {
	 * final ItemStack stack = new ItemStack( BetterStorageBlocks.REINFORCED_CHEST, 1, material.getMetadata() );
	 * final EntityItem entityItem = new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack );
	 * world.spawnEntity( entityItem );
	 * }
	 *
	 * }
	 */

	public boolean hasCustomName()
	{
		return customName != null;
	}

	public void setCustomName( @Nullable ITextComponent name )
	{
		customName = name;
	}

	@Nullable
	public ITextComponent getCustomName()
	{
		return customName;
	}

	// TileEntity synchronization

	/*
	 * @Override
	 * public NBTTagCompound getUpdateTag()
	 * {
	 * final NBTTagCompound compound = super.getUpdateTag();
	 * return compound;
	 * }
	 */

	/*
	 * @Override
	 * public void handleUpdateTag( NBTTagCompound compound )
	 * {
	 * super.handleUpdateTag( compound );
	 * }
	 */

	// Reading from / writing to NBT

	@Override
	public NBTTagCompound write( NBTTagCompound compound )
	{
		compound = super.write( compound );
		compound.setTag( "inventory", inventory.serializeNBT() );

		final ITextComponent itextcomponent = getCustomName();
		if( itextcomponent != null )
			compound.setString( "CustomName", ITextComponent.Serializer.toJson( itextcomponent ) );
		return compound;
	}

	@Override
	public void read( NBTTagCompound compound )
	{
		super.read( compound );
		if( compound.hasKey( "inventory" ) )
			inventory.deserializeNBT( (NBTTagCompound)compound.getTag( "inventory" ) );

		if( compound.contains( "CustomName", 8 ) )
			customName = ITextComponent.Serializer.fromJson( compound.getString( "CustomName" ) );
	}

	@Override
	public void tick()
	{
		// TODO Auto-generated method stub

	}

	@Override
	public float getLidAngle( float partialTicks )
	{
		// TODO Auto-generated method stub
		return 0;
	}
}
