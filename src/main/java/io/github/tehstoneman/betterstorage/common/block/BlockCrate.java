package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;

//@Interface( modid = "Botania", iface = "vazkii.botania.api.mana.ILaputaImmobile", striprefs = true )
public class BlockCrate extends BlockContainerBetterStorage// implements ITileEntityProvider//, ILaputaImmobile
{
	public static final BooleanProperty	NORTH	= BlockStateProperties.NORTH;
	public static final BooleanProperty	EAST	= BlockStateProperties.EAST;
	public static final BooleanProperty	SOUTH	= BlockStateProperties.SOUTH;
	public static final BooleanProperty	WEST	= BlockStateProperties.WEST;
	public static final BooleanProperty	UP		= BlockStateProperties.UP;
	public static final BooleanProperty	DOWN	= BlockStateProperties.DOWN;

	public BlockCrate()
	{
		super( Properties.from( Blocks.OAK_PLANKS ) );

		//@formatter:off
		setDefaultState( stateContainer.getBaseState().with( NORTH, Boolean.valueOf( false ) )
													  .with( EAST, Boolean.valueOf( false ) )
													  .with( SOUTH, Boolean.valueOf( false ) )
													  .with( WEST, Boolean.valueOf( false ) )
													  .with( UP, Boolean.valueOf( false ) )
													  .with( DOWN, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void fillStateContainer( StateContainer.Builder< Block, IBlockState > builder )
	{
		builder.add( NORTH, EAST, SOUTH, WEST, UP, DOWN );
	}

	@Override
	public IBlockState updatePostPlacement( IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	private TileEntityCrate getCrateAt( IBlockReader world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityCrate )
			return (TileEntityCrate)tileEntity;
		return null;
	}

	/**
	 * Checks if this block can connect with a neighboring block
	 *
	 * @param side
	 *            Side facing toward neighbor
	 */
	/*
	 * public boolean canConnect( IBlockAccess worldIn, BlockPos pos, EnumFacing side )
	 * {
	 * final TileEntityCrate thisCrate = (TileEntityCrate)worldIn.getTileEntity( pos );
	 * final TileEntity tileEntity = worldIn.getTileEntity( pos.add( side.getDirectionVec() ) );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate connectedCrate = (TileEntityCrate)tileEntity;
	 * return thisCrate.getPileID().equals( connectedCrate.getPileID() );
	 * }
	 * return false;
	 * }
	 */

	@Override
	public TileEntity createTileEntity( IBlockState state, IBlockReader world )
	{
		return new TileEntityCrate();
	}

	/**
	 * Called to test special placement conditions
	 */
	/*
	 * public void onBlockPlacedExtended( World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity,
	 * ItemStack stack )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate crate = (TileEntityCrate)tileEntity;
	 * if( stack.hasDisplayName() )
	 * crate.setCustomTitle( stack.getDisplayName() );
	 *
	 * crate.attemptConnect( side.getOpposite() );
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack )
	 * {
	 * final TileEntity tileEntity = worldIn.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityCrate )
	 * ( (TileEntityCrate)tileEntity ).onBlockPlaced( placer, stack );
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockActivated( World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing,
	 * float hitX, float hitY, float hitZ )
	 * {
	 * if( worldIn.isRemote )
	 * return true;
	 * final TileEntityCrate tileEntityCrate = (TileEntityCrate)worldIn.getTileEntity( pos );
	 * playerIn.openGui( ModInfo.modId, EnumGui.CRATE.getGuiID(), worldIn, pos.getX(), pos.getY(), pos.getZ() );
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public boolean removedByPlayer( IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( pos );
	 * if( !world.isRemote && tileEntity instanceof TileEntityCrate )
	 * {
	 * final TileEntityCrate tileCrate = (TileEntityCrate)tileEntity;
	 * final CrateStackHandler handler = tileCrate.getCrateStackHandler();
	 * final NonNullList< ItemStack > overflow = handler.removeCrate( tileCrate );
	 * tileCrate.notifyRegionUpdate( handler.getRegion(), tileCrate.getPileID() );
	 * if( !overflow.isEmpty() )
	 * for( final ItemStack stack : overflow )
	 * if( !stack.isEmpty() )
	 * world.spawnEntity( new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack ) );
	 * }
	 * return super.removedByPlayer( state, world, pos, player, willHarvest );
	 * }
	 */

	/*
	 * @Override
	 * public boolean hasComparatorInputOverride( IBlockState state )
	 * {
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public int getComparatorInputOverride( IBlockState blockState, World worldIn, BlockPos pos )
	 * {
	 * final TileEntity tileEntity = worldIn.getTileEntity( pos );
	 * if( !( tileEntity instanceof TileEntityCrate ) )
	 * return 0;
	 *
	 * final TileEntityCrate tileCrate = (TileEntityCrate)tileEntity;
	 * return tileCrate.getComparatorSignalStrength();
	 * }
	 */

	/*
	 * @Method( modid = "Botania" )
	 *
	 * @Override
	 * public boolean canMove( World world, BlockPos pos )
	 * {
	 * return false;
	 * }
	 */
}
