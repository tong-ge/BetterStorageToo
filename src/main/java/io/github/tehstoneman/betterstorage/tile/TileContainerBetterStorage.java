package io.github.tehstoneman.betterstorage.tile;

import io.github.tehstoneman.betterstorage.tile.entity.TileEntityContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class TileContainerBetterStorage extends TileBetterStorage implements ITileEntityProvider
{
	protected TileContainerBetterStorage( Material material )
	{
		super( material );
		isBlockContainer = true;
	}

	@Override
	public boolean hasTileEntity( IBlockState state )
	{
		return true;
	}

	/*
	 * @Override
	 * public boolean eventReceived( IBlockState state, World worldIn, BlockPos pos, int id, int param )
	 * {
	 * final TileEntity te = worldIn.getTileEntity( pos );
	 * return te != null ? te.receiveClientEvent( id, param ) : false;
	 * }
	 */

	// Pass actions to TileEntityContainer

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityContainer )
			( (TileEntityContainer)tileEntity ).onBlockPlaced( placer, stack );
	}

	/*
	 * @Override
	 * public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player,
	 * int side, float hitX, float hitY, float hitZ) {
	 * return getContainer(world, x, y, z).onBlockActivated(player, side, hitX, hitY, hitZ);
	 * }
	 */

	/*
	 * @Override
	 * public boolean removedByPlayer(World world, EntityPlayer player, int x, int y, int z) {
	 * if (!getContainer(world, x, y, z).onBlockBreak(player)) return false;
	 * return super.removedByPlayer(world, player, x, y, z);
	 * }
	 */

	/*
	 * @Override
	 * public void onBlockPreDestroy(World world, int x, int y, int z, int meta) {
	 * TileEntityContainer container = getContainer(world, x, y, z);
	 * if (container != null) container.onBlockDestroyed();
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack getPickBlock(MovingObjectPosition target, World world, int x, int y, int z) {
	 * TileEntityContainer container = getContainer(world, x, y, z);
	 * if (container instanceof IHasAttachments) {
	 * ItemStack pick = ((IHasAttachments)container).getAttachments().pick(target);
	 * if (pick != null) return pick;
	 * }
	 * ItemStack pick = super.getPickBlock(target, world, x, y, z);
	 * return container.onPickBlock(pick, target);
	 * }
	 */

	/*
	 * @Override
	 * public int getComparatorInputOverride(World world, int x, int y, int z, int direction) {
	 * return TileEntityContainer.getContainerComparatorSignalStrength(world, x, y, z);
	 * }
	 */

	@Override
	public void onNeighborChange( IBlockAccess world, BlockPos pos, BlockPos neighbor )
	{
		final TileEntity tileEntity = world.getTileEntity( neighbor );
		if( tileEntity instanceof TileEntityContainer )
			( (TileEntityContainer)tileEntity ).onNeighborUpdate( world.getBlockState( neighbor ).getBlock() );
	}

}
