package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockCardboardBox extends Block// BlockContainerBetterStorage
{
	protected static final VoxelShape	SHAPE_BOX	= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );

	private static Properties			properties	= Properties.create( Material.WOOL ).hardnessAndResistance( 0.8f ).sound( SoundType.CLOTH );

	public BlockCardboardBox()
	{
		super( properties );
	}

	/*@Override
	public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos )
	{
		return SHAPE_BOX;
	}*/

	/*
	 * @Override
	 * public boolean isOpaqueCube( IBlockState state )
	 * {
	 * return false;
	 * }
	 */

	/*@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}*/

	/*
	 * @Override
	 * public int quantityDropped( Random rand )
	 * {
	 * return 0;
	 * }
	 */

	/*
	 * @Override
	 * public TileEntity createNewTileEntity( World world, int metadata )
	 * {
	 * return new TileEntityCardboardBox();
	 * }
	 */

	/*
	 * @Override
	 * protected ItemBlock getItemBlock()
	 * {
	 * return new ItemBlockCardboardBox( this );
	 * }
	 */
}
