package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Optional.Interface;
import net.minecraftforge.fml.common.Optional.Method;
import vazkii.botania.api.mana.ILaputaImmobile;

@Interface( modid = "Botania", iface = "vazkii.botania.api.mana.ILaputaImmobile", striprefs = true )
public class BlockCrate extends Block implements ITileEntityProvider, ILaputaImmobile
{
	public static final PropertyBool	CONNECTED_DOWN	= PropertyBool.create( "down" );
	public static final PropertyBool	CONNECTED_UP	= PropertyBool.create( "up" );
	public static final PropertyBool	CONNECTED_NORTH	= PropertyBool.create( "north" );
	public static final PropertyBool	CONNECTED_SOUTH	= PropertyBool.create( "south" );
	public static final PropertyBool	CONNECTED_EAST	= PropertyBool.create( "east" );
	public static final PropertyBool	CONNECTED_WEST	= PropertyBool.create( "west" );

	public BlockCrate()
	{
		super( Material.WOOD );
		setHardness( 2.0f );
		setHarvestLevel( "axe", 0 );

		//@formatter:off
		setDefaultState( blockState.getBaseState().withProperty( CONNECTED_DOWN, false )
												  .withProperty( CONNECTED_UP, false )
												  .withProperty( CONNECTED_NORTH, false )
												  .withProperty( CONNECTED_SOUTH, false )
												  .withProperty( CONNECTED_EAST, false )
												  .withProperty( CONNECTED_WEST, false ) );
		//@formatter:on
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		//@formatter:off
		final IProperty[] listedProperties = new IProperty[] { CONNECTED_DOWN,
															   CONNECTED_UP,
															   CONNECTED_NORTH,
															   CONNECTED_SOUTH,
															   CONNECTED_EAST,
															   CONNECTED_WEST };
		//@formatter:on
		return new BlockStateContainer( this, listedProperties );
	}

	@Override
	public IBlockState getStateFromMeta( int meta )
	{
		return getDefaultState();
	}

	@Override
	public int getMetaFromState( IBlockState state )
	{
		return 0;
	}

	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	{
		state = state.withProperty( CONNECTED_DOWN, canConnect( worldIn, pos, EnumFacing.DOWN ) );
		state = state.withProperty( CONNECTED_UP, canConnect( worldIn, pos, EnumFacing.UP ) );
		state = state.withProperty( CONNECTED_NORTH, canConnect( worldIn, pos, EnumFacing.NORTH ) );
		state = state.withProperty( CONNECTED_SOUTH, canConnect( worldIn, pos, EnumFacing.SOUTH ) );
		state = state.withProperty( CONNECTED_EAST, canConnect( worldIn, pos, EnumFacing.EAST ) );
		state = state.withProperty( CONNECTED_WEST, canConnect( worldIn, pos, EnumFacing.WEST ) );
		return state;
	}

	/**
	 * Checks if this block can connect with a neighboring block
	 * 
	 * @param side
	 *            Side facing toward neighbor
	 */
	public boolean canConnect( IBlockAccess worldIn, BlockPos pos, EnumFacing side )
	{
		final TileEntityCrate thisCrate = (TileEntityCrate)worldIn.getTileEntity( pos );
		final TileEntity tileEntity = worldIn.getTileEntity( pos.add( side.getDirectionVec() ) );
		if( tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate connectedCrate = (TileEntityCrate)tileEntity;
			return thisCrate.getPileID().equals( connectedCrate.getPileID() );
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityCrate();
	}

	/**
	 * Called to test special placement conditions
	 */
	public void onBlockPlacedExtended( World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EntityLivingBase entity,
			ItemStack stack )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate crate = (TileEntityCrate)tileEntity;
			if( stack.hasDisplayName() )
				crate.setCustomTitle( stack.getDisplayName() );

			crate.attemptConnect( side.getOpposite() );
		}
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityCrate )
			( (TileEntityCrate)tileEntity ).onBlockPlaced( placer, stack );
	}

	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			EnumFacing side, float hitX, float hitY, float hitZ )
	{
		if( world.isRemote )
			return true;
		final TileEntityCrate tileEntityCrate = (TileEntityCrate)world.getTileEntity( pos );
		player.openGui( ModInfo.modId, EnumGui.CRATE.getGuiID(), world, pos.getX(), pos.getY(), pos.getZ() );
		return true;
	}

	@Override
	public boolean removedByPlayer( IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( !world.isRemote && tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate tileCrate = (TileEntityCrate)tileEntity;
			final CrateStackHandler handler = tileCrate.getCrateStackHandler();
			final ItemStack[] overflow = handler.removeCrate( tileCrate );
			if( overflow != null )
				for( final ItemStack stack : overflow )
					if( stack != null && stack.stackSize > 0 )
						world.spawnEntityInWorld( new EntityItem( world, pos.getX(), pos.getY(), pos.getZ(), stack ) );
		}
		return super.removedByPlayer( state, world, pos, player, willHarvest );
	}

	@Override
    public void onNeighborChange(IBlockAccess world, BlockPos pos, BlockPos neighbor)
	{
		//world.getTileEntity( pos ).markDirty();
	}

	@Override
	public boolean hasComparatorInputOverride( IBlockState state )
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride( IBlockState blockState, World worldIn, BlockPos pos )
	{
		TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( !(tileEntity instanceof TileEntityCrate))
			return 0;
		
		TileEntityCrate tileCrate = (TileEntityCrate)tileEntity;
		return tileCrate.getComparatorSignalStrength();
	}

	@Method( modid = "Botania" )
	@Override
	public boolean canMove( World world, BlockPos pos )
	{
		return false;
	}
}
