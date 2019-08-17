package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

//@Interface( modid = "Botania", iface = "vazkii.botania.api.mana.ILaputaImmobile", striprefs = true )
public class BlockCrate extends BlockConnectableContainer// implements ILaputaImmobile
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
	protected void fillStateContainer( StateContainer.Builder< Block, BlockState > builder )
	{
		super.fillStateContainer( builder );
		builder.add( NORTH, EAST, SOUTH, WEST, UP, DOWN );
	}

	/*
	 * ======================
	 * TileEntity / Rendering
	 * ======================
	 */

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityCrate();
	}

	/*
	 * =========
	 * Placement
	 * =========
	 */

	@Override
	public BlockState updatePostPlacement( BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		final TileEntityCrate tileCrate = getCrateAt( worldIn, currentPos );
		final TileEntityCrate facingCrate = getCrateAt( worldIn, facingPos );
		if( tileCrate != null )
			tileCrate.onBlockPlaced( facingCrate );
		return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public boolean onBlockActivated( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( worldIn.isRemote )
			return true;
		else
		{
			final INamedContainerProvider tileEntityCrate = getContainer( state, worldIn, pos );
			if( tileEntityCrate != null )
				NetworkHooks.openGui( (ServerPlayerEntity)player, tileEntityCrate, pos );
			return true;
		}
	}

	@Override
	@Nullable
	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( !( tileentity instanceof TileEntityCrate ) )
			return null;
		else
		{
			final TileEntityCrate crate = (TileEntityCrate)tileentity;
			return crate;
		}
	}

	private TileEntityCrate getCrateAt( IBlockReader world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityCrate )
			return (TileEntityCrate)tileEntity;
		return null;
	}

	@Override
	public void onReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final TileEntityCrate tileCrate = getCrateAt( world, pos );
			if( !world.isRemote && tileCrate != null )
			{
				final CrateStackHandler handler = tileCrate.getCrateStackHandler();
				final NonNullList< ItemStack > overflow = handler.removeCrate( tileCrate );
				tileCrate.notifyRegionUpdate( handler.getRegion(), tileCrate.getPileID() );
				// if( !overflow.isEmpty() )
				// for( final ItemStack stack : overflow )
				// if( !stack.isEmpty() )
				// world.spawnEntity( new ItemEntity( world, pos.getX(), pos.getY(), pos.getZ(), stack ) );
			}

		}
		super.onReplaced( state, world, pos, newState, isMoving );
	}

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
