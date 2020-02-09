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
import net.minecraft.fluid.Fluids;
import net.minecraft.fluid.IFluidState;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraftforge.fml.network.NetworkHooks;

public class BlockCardboardBox extends BlockContainerBetterStorage implements IWaterLoggable
{
	public static final BooleanProperty		WATERLOGGED	= BlockStateProperties.WATERLOGGED;

	protected static final VoxelShape		SHAPE_BOX	= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );

	public BlockCardboardBox()
	{
		super( Properties.create( Material.WOOL ).hardnessAndResistance( 0.8f ).sound( SoundType.CLOTH ) );

		setDefaultState( stateContainer.getBaseState().with( WATERLOGGED, Boolean.valueOf( false ) ) );
	}

	@Override
	protected void fillStateContainer( StateContainer.Builder< Block, BlockState > builder )
	{
		super.fillStateContainer( builder );
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

	@Override
	public IFluidState getFluidState( BlockState state )
	{
		return state.get( WATERLOGGED ) ? Fluids.WATER.getStillFluidState( false ) : super.getFluidState( state );
	}

	/*
	 * =========
	 * Placement
	 * =========
	 */

	@Override
	public BlockState getStateForPlacement( BlockItemUseContext context )
	{
		final IFluidState fluidState = context.getWorld().getFluidState( context.getPos() );
		return getDefaultState().with( WATERLOGGED, Boolean.valueOf( fluidState.getFluid() == Fluids.WATER ) );
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( tileentity instanceof TileEntityCardboardBox )
		{
			final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
			if( stack.hasDisplayName() )
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

	@Override
	public BlockState updatePostPlacement( BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.get( WATERLOGGED ) )
			worldIn.getPendingFluidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickRate( worldIn ) );

		return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	@Override
	public void onReplaced( BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving )
	{
		if( state.getBlock() != newState.getBlock() )
		{
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof TileEntityCardboardBox )
			{
				final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
				if( cardboardBox.uses < 1 )
					cardboardBox.dropInventoryItems();
				worldIn.updateComparatorOutputLevel( pos, this );
			}

		}
		super.onReplaced( state, worldIn, pos, newState, isMoving );
	}

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	@Override
	public ActionResultType onBlockActivated( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( worldIn.isRemote )
			return ActionResultType.SUCCESS;
		else
		{
			final INamedContainerProvider box = getContainer( state, worldIn, pos );
			if( box != null )
				NetworkHooks.openGui( (ServerPlayerEntity)player, box, pos );
			return ActionResultType.SUCCESS;
		}
	}

	@Override
	public void onBlockHarvested( World worldIn, BlockPos pos, BlockState state, PlayerEntity player )
	{
		if( !worldIn.isRemote() )
		{
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof TileEntityCardboardBox )
			{
				final TileEntityCardboardBox cardboardBox = (TileEntityCardboardBox)tileentity;
				if( player.isCreative() && !cardboardBox.isEmpty() && cardboardBox.uses > 0 )
				{
					final ItemStack itemstack = getItem( worldIn, pos, state );
					final CompoundNBT nbt = cardboardBox.write( new CompoundNBT() );
					if( nbt.contains( "Inventory" ) )
						itemstack.setTagInfo( "Inventory", nbt.get( "Inventory" ) );
					if( cardboardBox.hasCustomName() )
						itemstack.setDisplayName( cardboardBox.getCustomName() );
					if( nbt.contains( "Color" ) )
						itemstack.setTagInfo( "Color", nbt.get( "Color" ) );
					if( nbt.contains( "Uses" ) )
						itemstack.setTagInfo( "Uses", nbt.get( "Uses" ) );

					final ItemEntity itementity = new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), itemstack );
					itementity.setDefaultPickupDelay();
					worldIn.addEntity( itementity );
				}
			}
		}
		super.onBlockHarvested( worldIn, pos, state, player );
	}

	@Override
	public List< ItemStack > getDrops( BlockState state, LootContext.Builder builder )
	{
		final TileEntity tileentity = builder.get( LootParameters.BLOCK_ENTITY );
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
	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( tileentity instanceof TileEntityCardboardBox )
			return (TileEntityCardboardBox)tileentity;
		return null;
	}

	@Override
	public PushReaction getPushReaction( BlockState state )
	{
		return BetterStorageConfig.COMMON.cardboardBoxPistonBreakable.get() ? PushReaction.DESTROY : PushReaction.BLOCK;
	}
}
