package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockReinforcedLocker extends BlockLocker
{
	public BlockReinforcedLocker()
	{
		this( Block.Properties.create( Material.WOOD ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	public BlockReinforcedLocker( Properties properties )
	{
		super( properties );
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityReinforcedLocker();
	}

	@Override
	@Nullable
	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( !( tileentity instanceof TileEntityReinforcedLocker ) )
			return null;
		else
		{
			final TileEntityReinforcedLocker locker = (TileEntityReinforcedLocker)tileentity;
			return locker;
		}
	}

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
