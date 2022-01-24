package io.github.tehstoneman.betterstorage.common.block;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;

public class BlockCardboardBox extends BlockContainerBetterStorage implements SimpleWaterloggedBlock
{
	public static final BooleanProperty	WATERLOGGED	= BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape	SHAPE_BOX	= Block.box( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );

	public BlockCardboardBox()
	{
		super( Properties.of( Material.WOOL ).strength( 0.8f ).sound( SoundType.WOOL ) );

		registerDefaultState( defaultBlockState().setValue( WATERLOGGED, Boolean.valueOf( false ) ) );
	}

	@Override
	protected void createBlockStateDefinition( StateDefinition.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( WATERLOGGED );
	}

	/*
	 * ======================
	 * BlockEntity / Rendering
	 * ======================
	 */

	@Override
	public BlockEntity newBlockEntity( BlockPos blockPos, BlockState blockState )
	{
		return new TileEntityCardboardBox( blockPos, blockState );
	}

	@Override
	public VoxelShape getShape( BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context )
	{
		return SHAPE_BOX;
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public FluidState getFluidState( BlockState state )
	{
		return state.getValue( WATERLOGGED ) ? Fluids.WATER.getSource( false ) : super.getFluidState( state );
	}

	/*
	 * =========
	 * Placement
	 * =========
	 */

	@Override
	public BlockState getStateForPlacement( BlockPlaceContext context )
	{
		final FluidState fluidState = context.getLevel().getFluidState( context.getClickedPos() );
		return defaultBlockState().setValue( WATERLOGGED, Boolean.valueOf( fluidState.getType() == Fluids.WATER ) );
	}

	@Override
	public void setPlacedBy( Level worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		final BlockEntity tileentity = worldIn.getBlockEntity( pos );
		if( tileentity instanceof TileEntityCardboardBox )
		{
			final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
			if( stack.hasCustomHoverName() )
				cardboardBox.setCustomName( stack.getDisplayName() );
			if( stack.hasTag() )
			{
				final CompoundTag nbt = stack.getTag();

				if( nbt.contains( "Color" ) )
					cardboardBox.setColor( nbt.getInt( "Color" ) );

				if( nbt.contains( "Inventory" ) )
					cardboardBox.inventory.deserializeNBT( nbt.getCompound( "Inventory" ) );

				int uses = nbt.contains( "Uses" ) ? nbt.getInt( "Uses" ) : ItemBlockCardboardBox.getMaxUses();
				if( !( placer instanceof Player ) || !( (Player)placer ).isCreative() )
					if( !cardboardBox.isEmpty() )
						uses--;
				cardboardBox.setUses( uses );
			}
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState updateShape( BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.getValue( WATERLOGGED ) )
			worldIn.scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickDelay( worldIn ) );

		return super.updateShape( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void onRemove( BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final BlockEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityCardboardBox )
			{
				final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
				if( cardboardBox.uses < 1 )
					cardboardBox.dropInventoryItems();
				worldIn.updateNeighbourForOutputSignal( pos, this );
			}

		}
		super.onRemove( state, worldIn, pos, newState, isMoving );
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public InteractionResult use( BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit )
	{
		if( worldIn.isClientSide )
			return InteractionResult.SUCCESS;
		else
		{
			final MenuProvider box = getMenuProvider( state, worldIn, pos );
			if( box != null )
				NetworkHooks.openGui( (ServerPlayer)player, box, pos );
			return InteractionResult.SUCCESS;
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void playerWillDestroy( Level worldIn, BlockPos pos, BlockState state, Player player )
	{
		if( !worldIn.isClientSide() )
		{
			final BlockEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityCardboardBox )
			{
				final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
				if( player.isCreative() && !cardboardBox.isEmpty() && cardboardBox.uses > 0 )
				{
					final ItemStack itemstack = getCloneItemStack( worldIn, pos, state );
					final CompoundTag nbt = cardboardBox.save( new CompoundTag() );
					if( nbt.contains( "Inventory" ) )
						itemstack.addTagElement( "Inventory", nbt.get( "Inventory" ) );
					if( cardboardBox.hasCustomName() )
						itemstack.setHoverName( cardboardBox.getCustomName() );
					if( nbt.contains( "Color" ) )
						itemstack.addTagElement( "Color", nbt.get( "Color" ) );
					if( nbt.contains( "Uses" ) )
						itemstack.addTagElement( "Uses", nbt.get( "Uses" ) );

					final ItemEntity itementity = new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), itemstack );
					itementity.setDefaultPickUpDelay();
					worldIn.addFreshEntity( itementity );
				}
			}
		}
		super.playerWillDestroy( worldIn, pos, state, player );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public List< ItemStack > getDrops( BlockState state, LootContext.Builder builder )
	{
		final BlockEntity tileentity = builder.getOptionalParameter( LootContextParams.BLOCK_ENTITY );
		if( tileentity instanceof TileEntityCardboardBox )
		{
			final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
			if( cardboardBox.uses == 0 )
				return Collections.emptyList();
		}

		return super.getDrops( state, builder );
	}

	@Override
	@Nullable
	public MenuProvider getMenuProvider( BlockState state, Level worldIn, BlockPos pos )
	{
		final BlockEntity tileentity = worldIn.getBlockEntity( pos );
		if( tileentity instanceof TileEntityCardboardBox )
			return (TileEntityCardboardBox)tileentity;
		return null;
	}

	@Override
	public PushReaction getPistonPushReaction( BlockState state )
	{
		return BetterStorageConfig.COMMON.cardboardBoxPistonBreakable.get() ? PushReaction.DESTROY : PushReaction.BLOCK;
	}
}
