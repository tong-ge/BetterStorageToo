package io.github.tehstoneman.betterstorage.common.block;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;

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
		super( Properties.copy( Blocks.OAK_PLANKS ) );

		//@formatter:off
		registerDefaultState( defaultBlockState().setValue( NORTH, Boolean.valueOf( false ) )
												 .setValue( EAST, Boolean.valueOf( false ) )
												 .setValue( SOUTH, Boolean.valueOf( false ) )
												 .setValue( WEST, Boolean.valueOf( false ) )
												 .setValue( UP, Boolean.valueOf( false ) )
												 .setValue( DOWN, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	protected void createBlockStateDefinition( StateDefinition.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( NORTH, EAST, SOUTH, WEST, UP, DOWN );
	}

	@Override
	public BlockEntity newBlockEntity( BlockPos blockPos, BlockState blockState )
	{
		return new TileEntityCrate( blockPos, blockState );
	}

	@Override
	public void setPlacedBy( Level worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack )
	{
		super.setPlacedBy( worldIn, pos, state, placer, stack );
		if( !worldIn.isClientSide() )
		{
			final TileEntityCrate crate = TileEntityCrate.getCrateAt( worldIn, pos );
			if( crate != null && !crate.hasID() )
			{
				final CrateStackHandler handler = crate.getCrateStackHandler();
				handler.addCrate( crate );
			}
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState updateShape( BlockState state, Direction facing, BlockState facingState, LevelAccessor world, BlockPos pos, BlockPos facingPos )
	{
		if( !world.isClientSide() )
		{
			final TileEntityCrate tileCrate = TileEntityCrate.getCrateAt( world, pos );
			if( tileCrate != null )
			{
				final TileEntityCrate facingCrate = TileEntityCrate.getCrateAt( world, facingPos );
				state = state.setValue( FACING_TO_PROPERTY_MAP.get( facing ), tileCrate.tryAddCrate( facingCrate ) ).setValue( TYPE,
						tileCrate.getNumCrates() > 1 ? ConnectedType.PILE : ConnectedType.SINGLE );
			}
			tileCrate.updateConnections();
		}
		return super.updateShape( state, facing, facingState, world, pos, facingPos );
	}

	@Override
	public InteractionResult use( BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
	{
		if( worldIn.isClientSide )
			return InteractionResult.SUCCESS;
		else
		{
			final MenuProvider tileEntityCrate = getMenuProvider( state, worldIn, pos );
			if( tileEntityCrate != null )
				NetworkHooks.openGui( (ServerPlayer)player, tileEntityCrate, pos );
			return InteractionResult.SUCCESS;
		}
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider( BlockState state, Level world, BlockPos pos )
	{
		return TileEntityCrate.getCrateAt( world, pos );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void onRemove( BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( !world.isClientSide && state.getBlock() != newState.getBlock() )
		{
			final TileEntityCrate tileCrate = TileEntityCrate.getCrateAt( world, pos );
			if( tileCrate != null )
			{
				final CrateStackHandler handler = tileCrate.getCrateStackHandler();
				final NonNullList< ItemStack > overflow = tileCrate.removeCrate();
				if( !overflow.isEmpty() )
					Containers.dropContents( world, pos, overflow );
				tileCrate.notifyRegionUpdate( handler.getRegion(), tileCrate.getPileID() );
			}

		}
		super.onRemove( state, world, pos, newState, isMoving );
	}

	/*
	 * @Override
	 * public boolean hasComparatorInputOverride( BlockState state )
	 * {
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public int getComparatorInputOverride( BlockState blockState, Level worldIn, BlockPos pos )
	 * {
	 * final BlockEntity tileEntity = worldIn.getBlockEntity( pos );
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
	 * public boolean canMove( Level world, BlockPos pos )
	 * {
	 * return false;
	 * }
	 */
}
