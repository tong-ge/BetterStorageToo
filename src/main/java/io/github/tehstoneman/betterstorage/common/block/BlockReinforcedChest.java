package io.github.tehstoneman.betterstorage.common.block;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.inventory.ContainerBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.Block;
import net.minecraft.block.IBucketPickupHandler;
import net.minecraft.block.ILiquidContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.init.Fluids;
import net.minecraft.inventory.Container;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.state.properties.ChestType;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IInteractionObject;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

public class BlockReinforcedChest extends BlockLockable implements IBucketPickupHandler, ILiquidContainer
{
	public static final EnumProperty< ChestType >	TYPE			= BlockStateProperties.CHEST_TYPE;
	public static final BooleanProperty				WATERLOGGED		= BlockStateProperties.WATERLOGGED;
	protected static final VoxelShape				SHAPE_NORTH		= Block.makeCuboidShape( 1.0D, 0.0D, 0.0D, 15.0D, 14.0D, 15.0D );
	protected static final VoxelShape				SHAPE_SOUTH		= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 16.0D );
	protected static final VoxelShape				SHAPE_WEST		= Block.makeCuboidShape( 0.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );
	protected static final VoxelShape				SHAPE_EAST		= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 16.0D, 14.0D, 15.0D );
	protected static final VoxelShape				SHAPE_SINGLE	= Block.makeCuboidShape( 1.0D, 0.0D, 1.0D, 15.0D, 14.0D, 15.0D );

	public BlockReinforcedChest()
	{
		super( Block.Properties.create( Material.WOOD ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.WOOD ) );

		//@formatter:off
		setDefaultState( stateContainer.getBaseState().with( FACING, EnumFacing.NORTH )
													  .with( TYPE, ChestType.SINGLE )
													  .with( WATERLOGGED, Boolean.valueOf( false ) ) );
		//@formatter:on
	}

	@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}

	@Override
	public IBlockState updatePostPlacement( IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos,
			BlockPos facingPos )
	{
		if( stateIn.get( WATERLOGGED ) )
			worldIn.getPendingFluidTicks().scheduleTick( currentPos, Fluids.WATER, Fluids.WATER.getTickRate( worldIn ) );

		if( facingState.getBlock() == this && facing.getAxis().isHorizontal() )
		{
			final ChestType chesttype = facingState.get( TYPE );
			if( stateIn.get( TYPE ) == ChestType.SINGLE && chesttype != ChestType.SINGLE && stateIn.get( FACING ) == facingState.get( FACING )
					&& getDirectionToAttached( facingState ) == facing.getOpposite() )
				return stateIn.with( TYPE, chesttype.opposite() );
		}
		else if( getDirectionToAttached( stateIn ) == facing )
			return stateIn.with( TYPE, ChestType.SINGLE );

		return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	}

	@Override
	public VoxelShape getShape( IBlockState state, IBlockReader worldIn, BlockPos pos )
	{
		if( state.get( TYPE ) == ChestType.SINGLE )
			return SHAPE_SINGLE;
		else
			switch( getDirectionToAttached( state ) )
			{
			case NORTH:
			default:
				return SHAPE_NORTH;
			case SOUTH:
				return SHAPE_SOUTH;
			case WEST:
				return SHAPE_WEST;
			case EAST:
				return SHAPE_EAST;
			}

	}

	/**
	 * Returns a facing pointing from the given state to its attached double chest
	 */
	public static EnumFacing getDirectionToAttached( IBlockState state )
	{
		final EnumFacing enumfacing = state.get( FACING );
		return state.get( TYPE ) == ChestType.LEFT ? enumfacing.rotateY() : enumfacing.rotateYCCW();
	}

	@Override
	public IBlockState getStateForPlacement( BlockItemUseContext context )
	{
		ChestType chesttype = ChestType.SINGLE;
		EnumFacing enumfacing = context.getPlacementHorizontalFacing().getOpposite();
		final IFluidState ifluidstate = context.getWorld().getFluidState( context.getPos() );
		final boolean flag = context.isPlacerSneaking();
		final EnumFacing enumfacing1 = context.getFace();
		if( enumfacing1.getAxis().isHorizontal() && flag )
		{
			final EnumFacing enumfacing2 = getDirectionToAttach( context, enumfacing1.getOpposite() );
			if( enumfacing2 != null && enumfacing2.getAxis() != enumfacing1.getAxis() )
			{
				enumfacing = enumfacing2;
				chesttype = enumfacing2.rotateYCCW() == enumfacing1.getOpposite() ? ChestType.RIGHT : ChestType.LEFT;
			}
		}

		if( chesttype == ChestType.SINGLE && !flag )
			if( enumfacing == getDirectionToAttach( context, enumfacing.rotateY() ) )
				chesttype = ChestType.LEFT;
			else if( enumfacing == getDirectionToAttach( context, enumfacing.rotateYCCW() ) )
				chesttype = ChestType.RIGHT;

		return getDefaultState().with( FACING, enumfacing ).with( TYPE, chesttype ).with( WATERLOGGED,
				Boolean.valueOf( ifluidstate.getFluid() == Fluids.WATER ) );
	}

	@Override
	public Fluid pickupFluid( IWorld worldIn, BlockPos pos, IBlockState state )
	{
		if( state.get( WATERLOGGED ) )
		{
			worldIn.setBlockState( pos, state.with( WATERLOGGED, Boolean.valueOf( false ) ), 3 );
			return Fluids.WATER;
		}
		else
			return Fluids.EMPTY;
	}

	@Override
	public IFluidState getFluidState( IBlockState state )
	{
		return state.get( WATERLOGGED ) ? Fluids.WATER.getStillFluidState( false ) : super.getFluidState( state );
	}

	@Override
	public boolean canContainFluid( IBlockReader worldIn, BlockPos pos, IBlockState state, Fluid fluidIn )
	{
		return !state.get( WATERLOGGED ) && fluidIn == Fluids.WATER;
	}

	@Override
	public boolean receiveFluid( IWorld worldIn, BlockPos pos, IBlockState state, IFluidState fluidStateIn )
	{
		if( !state.get( WATERLOGGED ) && fluidStateIn.getFluid() == Fluids.WATER )
		{
			if( !worldIn.isRemote() )
			{
				worldIn.setBlockState( pos, state.with( WATERLOGGED, Boolean.valueOf( true ) ), 3 );
				worldIn.getPendingFluidTicks().scheduleTick( pos, Fluids.WATER, Fluids.WATER.getTickRate( worldIn ) );
			}

			return true;
		}
		else
			return false;
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack )
	{
		if( stack.hasDisplayName() )
		{
			final TileEntity tileentity = worldIn.getTileEntity( pos );
			if( tileentity instanceof TileEntityReinforcedChest )
				( (TileEntityReinforcedChest)tileentity ).setCustomName( stack.getDisplayName() );
		}

	}

	/**
	 * Returns facing pointing to a chest to form a double chest with, null otherwise
	 */
	@Nullable
	private EnumFacing getDirectionToAttach( BlockItemUseContext context, EnumFacing facing )
	{
		final IBlockState iblockstate = context.getWorld().getBlockState( context.getPos().offset( facing ) );
		return iblockstate.getBlock() == this && iblockstate.get( TYPE ) == ChestType.SINGLE ? iblockstate.get( FACING ) : null;
	}

	@Override
	public boolean onBlockActivated( IBlockState state, World worldIn, BlockPos pos, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ )
	{
		if( worldIn.isRemote )
			return true;
		else
		{
			final TileEntityReinforcedChest ilockablecontainer = getContainer( state, worldIn, pos, false );
			if( ilockablecontainer != null )
			{
				//player.displayGui( new Interface( ilockablecontainer ) );
				//player.addStat( getOpenStat() );
			}

			return true;
		}
	}

	@Override
	protected Stat< ResourceLocation > getOpenStat()
	{
		return StatList.CUSTOM.get( StatList.OPEN_CHEST );
	}

	/**
	 * Gets the chest inventory at the given location, returning null if there is no chest at that location or optionally
	 * if the chest is blocked. Handles large chests.
	 *
	 * @param state
	 *            The current state
	 * @param worldIn
	 *            The world
	 * @param pos
	 *            The position to check
	 * @param allowBlockedChest
	 *            If false, then if the chest is blocked then <code>null</code> will be returned. If true,
	 *            then the chest can still be blocked (used by hoppers).
	 */
	@Nullable
	public TileEntityReinforcedChest getContainer( IBlockState state, World worldIn, BlockPos pos, boolean allowBlockedChest )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( !( tileentity instanceof TileEntityReinforcedChest ) )
			return null;
		else if( !allowBlockedChest && isBlocked( worldIn, pos ) )
			return null;
		else
		{
			final TileEntityReinforcedChest ilockablecontainer = (TileEntityReinforcedChest)tileentity;
			final ChestType chesttype = state.get( TYPE );
			if( chesttype == ChestType.SINGLE )
				return ilockablecontainer;
			else
			{
				final BlockPos blockpos = pos.offset( getDirectionToAttached( state ) );
				final IBlockState iblockstate = worldIn.getBlockState( blockpos );
				if( iblockstate.getBlock() == this )
				{
					final ChestType chesttype1 = iblockstate.get( TYPE );
					if( chesttype1 != ChestType.SINGLE && chesttype != chesttype1 && iblockstate.get( FACING ) == state.get( FACING ) )
					{
						if( !allowBlockedChest && isBlocked( worldIn, blockpos ) )
							return null;

						final TileEntity tileentity1 = worldIn.getTileEntity( blockpos );
						/*
						 * if( tileentity1 instanceof TileEntityReinforcedChest )
						 * {
						 * final TileEntityReinforcedChest ilockablecontainer1 = chesttype == ChestType.RIGHT ? ilockablecontainer
						 * : (TileEntityReinforcedChest)tileentity1;
						 * final TileEntityReinforcedChest ilockablecontainer2 = chesttype == ChestType.RIGHT
						 * ? (TileEntityReinforcedChest)tileentity1
						 * : ilockablecontainer;
						 * ilockablecontainer = new TileEntityReinforcedChest( new TextComponentTranslation( "container.chestDouble" ),
						 * ilockablecontainer1, ilockablecontainer2 );
						 * }
						 */
					}
				}

				return ilockablecontainer;
			}
		}
	}

	@Override
	public TileEntity createTileEntity( IBlockState state, IBlockReader world )
	{
		return new TileEntityReinforcedChest();
	}

	private boolean isBlocked( World worldIn, BlockPos pos )
	{
		return isBelowSolidBlock( worldIn, pos ) || isOcelotSittingOnChest( worldIn, pos );
	}

	private boolean isBelowSolidBlock( IBlockReader worldIn, BlockPos pos )
	{
		return worldIn.getBlockState( pos.up() ).doesSideBlockChestOpening( worldIn, pos.up(), EnumFacing.DOWN );
	}

	private boolean isOcelotSittingOnChest( World worldIn, BlockPos pos )
	{
		final List< EntityOcelot > list = worldIn.getEntitiesWithinAABB( EntityOcelot.class,
				new AxisAlignedBB( pos.getX(), pos.getY() + 1, pos.getZ(), pos.getX() + 1, pos.getY() + 2, pos.getZ() + 1 ) );
		if( !list.isEmpty() )
			for( final EntityOcelot entityocelot : list )
				if( entityocelot.isSitting() )
					return true;

		return false;
	}

	@Override
	protected void fillStateContainer( StateContainer.Builder< Block, IBlockState > builder )
	{
		super.fillStateContainer( builder );
		builder.add( TYPE, WATERLOGGED );
	}

	/**
	 * Get the geometry of the queried face at the given position and state. This is used to decide whether things like
	 * buttons are allowed to be placed on the face, or how glass panes connect to the face, among other things.
	 * <p>
	 * Common values are {@code SOLID}, which is the default, and {@code UNDEFINED}, which represents something that does
	 * not fit the other descriptions and will generally cause other things not to connect to the face.
	 *
	 * @return an approximation of the form of the given face
	 */
	@Override
	public BlockFaceShape getBlockFaceShape( IBlockReader worldIn, IBlockState state, BlockPos pos, EnumFacing face )
	{
		return BlockFaceShape.UNDEFINED;
	}

	@Override
	public boolean allowsMovement( IBlockState state, IBlockReader worldIn, BlockPos pos, PathType type )
	{
		return false;
	}

	public class Interface implements IInteractionObject
	{
		private TileEntityReinforcedChest chestLeft;
		private TileEntityReinforcedChest chestRight;

		public Interface( TileEntityReinforcedChest tileEntityReinforcedChest )
		{
			chestLeft = tileEntityReinforcedChest;
		}

		public Interface( TileEntityReinforcedChest tileEntityReinforcedChestL, TileEntityReinforcedChest tileEntityReinforcedChestR )
		{
			chestLeft = tileEntityReinforcedChestL;
			chestRight = tileEntityReinforcedChestR;
		}

		@Override
		public ITextComponent getName()
		{
			return new TextComponentTranslation( BetterStorageBlocks.REINFORCED_CHEST.getTranslationKey() + ".name" );
		}

		@Override
		public boolean hasCustomName()
		{
			return chestLeft.hasCustomName() || chestRight != null && chestRight.hasCustomName();
		}

		@Override
		public ITextComponent getCustomName()
		{
			return chestLeft.hasCustomName() ? chestLeft.getCustomName() : chestRight.getCustomName();
		}

		@Override
		public Container createContainer( InventoryPlayer playerInventory, EntityPlayer playerIn )
		{
			return new ContainerBetterStorage( playerIn, null );
		}

		@Override
		public String getGuiID()
		{
			return "betterstorage:reinforced_chest";
		}
	}
}
