package io.github.tehstoneman.betterstorage.tile.crate;

import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import io.github.tehstoneman.betterstorage.item.tile.ItemTileBetterStorage;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.tile.TileContainerBetterStorage;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

//@Interface(modid = "Botania", iface = "vazkii.botania.api.mana.ILaputaImmobile", striprefs = true)
public class TileCrate extends TileContainerBetterStorage // implements ILaputaImmobile
{
	public static final PropertyBool	CONNECTED_DOWN	= PropertyBool.create( "down" );
	public static final PropertyBool	CONNECTED_UP	= PropertyBool.create( "up" );
	public static final PropertyBool	CONNECTED_NORTH	= PropertyBool.create( "north" );
	public static final PropertyBool	CONNECTED_SOUTH	= PropertyBool.create( "south" );
	public static final PropertyBool	CONNECTED_EAST	= PropertyBool.create( "east" );
	public static final PropertyBool	CONNECTED_WEST	= PropertyBool.create( "west" );

	public TileCrate()
	{
		super( Material.WOOD );

		setHardness( 2.0f );
		// setStepSound(soundTypeWood);

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
	protected void registerBlock()
	{
		GameRegistry.register( this );
		GameRegistry.register( new ItemTileBetterStorage( this ).setRegistryName( getRegistryName() ) );
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		//@formatter:off
		final IProperty[] listedProperties = new IProperty[]
				{ CONNECTED_DOWN, CONNECTED_UP, CONNECTED_NORTH, CONNECTED_SOUTH, CONNECTED_EAST, CONNECTED_WEST };
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
		//Logger.getLogger( Constants.modId ).info( "state " + state );
		return state;
	}

	public boolean canConnect( IBlockAccess worldIn, BlockPos pos, EnumFacing side )
	{
		final TileEntityCrate thisCrate = (TileEntityCrate)worldIn.getTileEntity( pos );
		//Logger.getLogger( Constants.modId ).info( "thisCrate.getID() " + thisCrate.getID() );
		if( thisCrate.getID() == -1 )
			return false;
		final TileEntity tileEntity = worldIn.getTileEntity( pos.add( side.getDirectionVec() ) );
		if( tileEntity instanceof TileEntityCrate )
		{
			final TileEntityCrate connectedCrate = (TileEntityCrate)tileEntity;
			return thisCrate.getID() == connectedCrate.getID();
		}
		return false;
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityCrate();
	}

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
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem,
			EnumFacing side, float hitX, float hitY, float hitZ )
	{
		if( world.isRemote )
			return true;
		player.openGui( Constants.modId, EnumGui.CRATE.getGuiID(), world, pos.getX(), pos.getY(), pos.getZ() );
		return true;
	}

	@Override
	public boolean hasComparatorInputOverride( IBlockState state )
	{
		return true;
	}

	/*
	 * private class ConnectedTextureCrate extends ConnectedTexture
	 * {
	 *
	 * @Override
	 * public boolean canConnect( IBlockAccess world, BlockPos pos, EnumFacing side, EnumFacing connected )
	 * {
	 * if( world.getBlockState( pos ) != TileCrate.this )
	 * return false;
	 * final int offX = pos.getX() + connected.getFrontOffsetX();
	 * final int offY = pos.getY() + connected.getFrontOffsetY();
	 * final int offZ = pos.getZ() + connected.getFrontOffsetZ();
	 *
	 * if( offY <= 0 )
	 * return false;
	 *
	 * final TileEntityCrate connectedCrate = WorldUtils.get( world, offX, offY, offZ, TileEntityCrate.class );
	 * if( connectedCrate == null )
	 * return false;
	 * final TileEntityCrate crate = WorldUtils.get( world, pos.getX(), pos.getY(), pos.getZ(), TileEntityCrate.class );
	 * return crate.getID() == connectedCrate.getID() && !crate.equals( connectedCrate );
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public boolean canMove(World world, int x, int y, int z) {
	 * return false;
	 * }
	 */

	@Override
	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( this ), 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	}
}
