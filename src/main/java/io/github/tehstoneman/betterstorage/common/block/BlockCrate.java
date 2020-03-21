package io.github.tehstoneman.betterstorage.common.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

//@Interface( modid = "Botania", iface = "vazkii.botania.api.mana.ILaputaImmobile", striprefs = true )
public class BlockCrate extends BlockConnectableContainer// implements ILaputaImmobile
{
	public static final BooleanProperty						NORTH					= BlockStateProperties.NORTH;
	public static final BooleanProperty						EAST					= BlockStateProperties.EAST;
	public static final BooleanProperty						SOUTH					= BlockStateProperties.SOUTH;
	public static final BooleanProperty						WEST					= BlockStateProperties.WEST;
	public static final BooleanProperty						UP						= BlockStateProperties.UP;
	public static final BooleanProperty						DOWN					= BlockStateProperties.DOWN;
	public static final Map< Direction, BooleanProperty >	FACING_TO_PROPERTY_MAP	= Util.make( Maps.newEnumMap( Direction.class ), ( facing ) ->
																					{
																						facing.put( Direction.NORTH, NORTH );
																						facing.put( Direction.EAST, EAST );
																						facing.put( Direction.SOUTH, SOUTH );
																						facing.put( Direction.WEST, WEST );
																						facing.put( Direction.UP, UP );
																						facing.put( Direction.DOWN, DOWN );
																					} );

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

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityCrate();
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack )
	{
		super.onBlockPlacedBy( worldIn, pos, state, placer, stack );
		if( !worldIn.isRemote )
		{
			final TileEntityCrate crate = TileEntityCrate.getCrateAt( worldIn, pos );
			if( crate != null && !crate.hasID() )
			{
				final CrateStackHandler handler = crate.getCrateStackHandler();
				handler.addCrate( crate );
			}
		}
	}

	@Override
	public BlockState updatePostPlacement( BlockState state, Direction facing, BlockState facingState, IWorld world, BlockPos pos,
			BlockPos facingPos )
	{
		if( !world.isRemote() )
		{
			final TileEntityCrate tileCrate = TileEntityCrate.getCrateAt( world, pos );
			if( tileCrate != null )
			{
				final TileEntityCrate facingCrate = TileEntityCrate.getCrateAt( world, facingPos );
				state = state.with( FACING_TO_PROPERTY_MAP.get( facing ), tileCrate.tryAddCrate( facingCrate ) ).with( TYPE,
						tileCrate.getNumCrates() > 1 ? ConnectedType.PILE : ConnectedType.SINGLE );
			}
			// tileCrate.updateConnections();
		}
		return super.updatePostPlacement( state, facing, facingState, world, pos, facingPos );
	}

	@Override
	public ActionResultType onBlockActivated( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( worldIn.isRemote )
			return ActionResultType.SUCCESS;
		else
		{
			final INamedContainerProvider tileEntityCrate = getContainer( state, worldIn, pos );
			if( tileEntityCrate != null )
				NetworkHooks.openGui( (ServerPlayerEntity)player, tileEntityCrate, pos );
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	@Nullable
	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityCrate )
			return (TileEntityCrate)tileEntity;
		return null;
	}

	@Override
	public void onReplaced( BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( !world.isRemote && state.getBlock() != newState.getBlock() )
		{
			final TileEntityCrate tileCrate = TileEntityCrate.getCrateAt( world, pos );
			if( tileCrate != null )
			{
				final CrateStackHandler handler = tileCrate.getCrateStackHandler();
				final NonNullList< ItemStack > overflow = tileCrate.removeCrate();
				if( !overflow.isEmpty() )
					InventoryHelper.dropItems( world, pos, overflow );
				tileCrate.notifyRegionUpdate( handler.getRegion(), tileCrate.getPileID() );
			}

		}
		super.onReplaced( state, world, pos, newState, isMoving );
	}

	/*
	 * @Nullable
	 * private TileEntityCrate getCrateAt( IBlockReader world, BlockPos pos )
	 * {
	 * final TileEntity tileEntity = world.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityCrate )
	 * return (TileEntityCrate)tileEntity;
	 * return null;
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
