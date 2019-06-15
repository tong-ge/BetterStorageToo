package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;

public class BlockReinforcedLocker extends BlockLocker
{
	public BlockReinforcedLocker()
	{
		super( Block.Properties.create( Material.WOOD ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	/*
	 * @Override
	 * public TileEntity createTileEntity( IBlockState state, IBlockReader world )
	 * {
	 * return new TileEntityReinforcedLocker();
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
	 * float hitY, float hitZ )
	 * {
	 * final EnumFacing facing = state.getValue( BlockHorizontal.FACING );
	 * if( facing == side )
	 * {
	 * if( !world.isRemote )
	 * {
	 * final TileEntity tileentity = world.getTileEntity( pos );
	 * if( tileentity instanceof TileEntityLocker )
	 * {
	 * final TileEntityLocker locker = (TileEntityLocker)tileentity;
	 * return locker.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
	 * }
	 * }
	 * return true;
	 * }
	 * return false;
	 * }
	 */
}
