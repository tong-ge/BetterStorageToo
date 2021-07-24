package io.github.tehstoneman.betterstorage.common.block;

import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.IWaterLoggable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCardboardBox extends BlockContainerBetterStorage implements IWaterLoggable
{
	public static final BooleanProperty	WATERLOGGED	= BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape	SHAPE_BOX	= Block.box( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );

	public BlockCardboardBox()
	{
		super( Properties.of( Material.WOOL ).strength( 0.8f ).sound( SoundType.WOOL ) );

		registerDefaultState( defaultBlockState().getBlockState().setValue( WATERLOGGED, Boolean.valueOf( false ) ) );
	}

	@Override
	protected void createBlockStateDefinition( StateContainer.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( WATERLOGGED );
	}

	/*
	 * ======================
	 * TileEntity / Rendering
	 * ======================
	 */

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityCardboardBox();
	}

	@Override
	public VoxelShape getShape( BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context )
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
	public BlockState getStateForPlacement( BlockItemUseContext context )
	{
		final FluidState fluidState = context.getLevel().getFluidState( context.getClickedPos());
		return defaultBlockState().setValue( WATERLOGGED, Boolean.valueOf( fluidState.getType() == Fluids.WATER ) );
	}

	@Override
	public void setPlacedBy( World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		final TileEntity tileentity = worldIn.getBlockEntity( pos );
		if( tileentity instanceof TileEntityCardboardBox )
		{
			final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
			if( stack.hasCustomHoverName() )
				cardboardBox.setCustomName( stack.getDisplayName() );
			if( stack.hasTag() )
			{
				final CompoundNBT nbt = stack.getTag();

				if( nbt.contains( "Color" ) )
					cardboardBox.setColor( nbt.getInt( "Color" ) );

				if( nbt.contains( "Inventory" ) )
					cardboardBox.inventory.deserializeNBT( nbt.getCompound( "Inventory" ) );

				int uses = nbt.contains( "Uses" ) ? nbt.getInt( "Uses" ) : ItemBlockCardboardBox.getMaxUses();
				if( !( placer instanceof PlayerEntity ) || !( (PlayerEntity)placer ).isCreative() )
					if( !cardboardBox.isEmpty() )
						uses--;
				cardboardBox.setUses( uses );
			}
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public BlockState updateShape( BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.getValue( WATERLOGGED ) )
			worldIn.getLiquidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickDelay( worldIn ) );

		return super.updateShape( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void onRemove( BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final TileEntity tileentity = worldIn.getBlockEntity( pos );
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
	public ActionResultType use( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( worldIn.isClientSide )
			return ActionResultType.SUCCESS;
		else
		{
			final INamedContainerProvider box = getMenuProvider( state, worldIn, pos );
			if( box != null )
				NetworkHooks.openGui( (ServerPlayerEntity)player, box, pos );
			return ActionResultType.SUCCESS;
		}
	}

	@SuppressWarnings( "deprecation" )
	@Override
	public void playerWillDestroy( World worldIn, BlockPos pos, BlockState state, PlayerEntity player )
	{
		if( !worldIn.isClientSide() )
		{
			final TileEntity tileentity = worldIn.getBlockEntity( pos );
			if( tileentity instanceof TileEntityCardboardBox )
			{
				final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
				if( player.isCreative() && !cardboardBox.isEmpty() && cardboardBox.uses > 0 )
				{
					final ItemStack itemstack = getCloneItemStack( worldIn, pos, state );
					final CompoundNBT nbt = cardboardBox.save( new CompoundNBT() );
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
		final TileEntity tileentity = builder.getOptionalParameter( LootParameters.BLOCK_ENTITY );
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
	public INamedContainerProvider getMenuProvider( BlockState state, World worldIn, BlockPos pos )
	{
		final TileEntity tileentity = worldIn.getBlockEntity( pos );
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
