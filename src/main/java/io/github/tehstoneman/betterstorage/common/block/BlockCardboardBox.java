package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class BlockCardboardBox extends BlockContainerBetterStorage
{
	public BlockCardboardBox( String name, Properties properties )
	{
		super( name, Material.WOOD, properties );

		// setHardness( 0.8f );
		// setSoundType( SoundType.CLOTH );
	}

	public BlockCardboardBox()
	{
		this( "cardboard_box", Properties.from( Blocks.OAK_PLANKS ) );
	}

	/*
	 * @Override
	 * public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos )
	 * {
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
	 * }
	 */

	/*
	 * @Override
	 * public boolean isOpaqueCube( IBlockState state )
	 * {
	 * return false;
	 * }
	 */

	@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}

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
