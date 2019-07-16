package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
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

	/*
	 * ===========
	 * Interaction
	 * ===========
	 */

	/*@Override
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
	}*/

	/*
	 * @Override
	 * public IBlockState updatePostPlacement( IBlockState stateIn, EnumFacing facing, IBlockState facingState, IWorld worldIn, BlockPos currentPos,
	 * BlockPos facingPos )
	 * {
	 * if( facingState.getBlock() == this )
	 * {
	 *
	 * }
	 * return super.updatePostPlacement( stateIn, facing, facingState, worldIn, currentPos, facingPos );
	 * }
	 */

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

	/**
	 * Checks if this block can connect with a neighboring block
	 *
	 * @param side
	 *            Side facing toward neighbor
	 */
	/*
	 * public boolean canConnect( IWorld worldIn, BlockPos pos, EnumFacing side )
	 * {
	 * final TileEntityCrate thisCrate = getCrateAt( worldIn, pos );
	 * final TileEntityCrate connectedCrate = getCrateAt( worldIn, pos.add( side.getDirectionVec() ) );
	 * if( thisCrate != null && connectedCrate != null )
	 * return thisCrate.getPileID().equals( connectedCrate.getPileID() );
	 * return false;
	 * }
	 */

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

	/*
	 * public class Interface implements IInteractionObject
	 * {
	 * private final TileEntityCrate crate;
	 *
	 * public Interface( TileEntityCrate tileEntityCrate )
	 * {
	 * crate = tileEntityCrate;
	 * }
	 *
	 * @Override
	 * public ITextComponent getName()
	 * {
	 * return new TextComponentTranslation( BetterStorageBlocks.CRATE.getTranslationKey() + ".name" );
	 * }
	 *
	 * @Override
	 * public boolean hasCustomName()
	 * {
	 * return crate.hasCustomName();
	 * }
	 *
	 * @Override
	 * public ITextComponent getCustomName()
	 * {
	 * return crate.getCustomName();
	 * }
	 *
	 * @Override
	 * public Container createContainer( InventoryPlayer playerInventory, EntityPlayer playerIn )
	 * {
	 * return new ContainerCrate( crate, playerIn );
	 * }
	 *
	 * @Override
	 * public String getGuiID()
	 * {
	 * return "betterstorage:crate";
	 * }
	 * }
	 */
}
