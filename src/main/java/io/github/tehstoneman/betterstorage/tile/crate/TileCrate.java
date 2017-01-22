package io.github.tehstoneman.betterstorage.tile.crate;

import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.item.tile.ItemTileBetterStorage;
import io.github.tehstoneman.betterstorage.misc.ConnectedTexture;
import io.github.tehstoneman.betterstorage.misc.ConnectedTexture.EnumConnected;
import io.github.tehstoneman.betterstorage.tile.TileContainerBetterStorage;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import mezz.jei.config.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;

//@Interface(modid = "Botania", iface = "vazkii.botania.api.mana.ILaputaImmobile", striprefs = true)
public class TileCrate extends TileContainerBetterStorage // implements ILaputaImmobile
{
	private final ConnectedTexture							texture		= new ConnectedTextureCrate();

	//public static final IUnlistedProperty< EnumConnected >	FACE_DOWN	= new Properties.PropertyAdapter< EnumConnected >(PropertyEnum.create( "face_down", EnumConnected.class ) );
	//public static final PropertyEnum	FACE_DOWN	= PropertyEnum.create( "face_down", EnumConnected.class ) );

	public static final PropertyBool CONNECTED_DOWN = PropertyBool.create( "down" );
	public static final PropertyBool CONNECTED_UP = PropertyBool.create( "up" );
	public static final PropertyBool CONNECTED_NORTH = PropertyBool.create( "north" );
	public static final PropertyBool CONNECTED_SOUTH = PropertyBool.create( "south" );
	public static final PropertyBool CONNECTED_EAST = PropertyBool.create( "east" );
	public static final PropertyBool CONNECTED_WEST = PropertyBool.create( "west" );
	
	public TileCrate()
	{
		super( Material.WOOD );

		setHardness( 2.0f );
		// setStepSound(soundTypeWood);

		setHarvestLevel( "axe", 0 );
		
		this.setDefaultState( this.blockState.getBaseState().withProperty( CONNECTED_DOWN, false )
				.withProperty( CONNECTED_UP, false )
				.withProperty( CONNECTED_NORTH, false )
				.withProperty( CONNECTED_SOUTH, false )
				.withProperty( CONNECTED_EAST, false )
				.withProperty( CONNECTED_WEST, false ));
	}

	@Override
	public Class< ? extends ItemBlock > getItemClass()
	{
		return ItemTileBetterStorage.class;
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		final IProperty[] listedProperties = new IProperty[]{CONNECTED_DOWN,CONNECTED_UP,CONNECTED_NORTH,CONNECTED_SOUTH,CONNECTED_EAST,CONNECTED_WEST};
		//final IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { FACE_DOWN };
		//return new ExtendedBlockState( this, listedProperties, unlistedProperties );
		return new BlockStateContainer(this,listedProperties );
	}

	/*@Override
	public IBlockState getExtendedState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	{
		if( state instanceof IExtendedBlockState )
		{
			IExtendedBlockState extendedState = (IExtendedBlockState)state;

			extendedState = extendedState.withProperty( FACE_DOWN, EnumConnected.ALL );
			Logger.getLogger( Constants.MOD_ID ).info( extendedState.toString() );
			return extendedState;
		}
		return state;
	}*/
	
	@Override
	public IBlockState getStateFromMeta( int meta )
	{
		return this.getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state)
	{
		return 0;
	}
	
	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	{
		state = state.withProperty( CONNECTED_DOWN, canConnectTo( worldIn, pos.down() ) );
		state = state.withProperty( CONNECTED_UP, canConnectTo( worldIn, pos.up() ) );
		state = state.withProperty( CONNECTED_NORTH, canConnectTo( worldIn, pos.north() ) );
		state = state.withProperty( CONNECTED_SOUTH, canConnectTo( worldIn, pos.south() ) );
		state = state.withProperty( CONNECTED_EAST, canConnectTo( worldIn, pos.east() ) );
		state = state.withProperty( CONNECTED_WEST, canConnectTo( worldIn, pos.west() ) );
		/*if( state instanceof IExtendedBlockState )
		{
			IExtendedBlockState extendedState = (IExtendedBlockState)state;

			int connections = 0;
			if( !canConnectTo( worldIn, pos.north() ) )
				connections += EnumConnected.T.getMetadata();
			if( !canConnectTo( worldIn, pos.south() ) )
				connections += EnumConnected.B.getMetadata();
			if( !canConnectTo( worldIn, pos.east() ) )
				connections += EnumConnected.L.getMetadata();
			if( !canConnectTo( worldIn, pos.west() ) )
				connections += EnumConnected.R.getMetadata();
			extendedState = extendedState.withProperty( FACE_DOWN, EnumConnected.byMetadata( connections ) );

			Logger.getLogger( Constants.MOD_ID ).info( EnumConnected.byMetadata( connections ).toString() );
			Logger.getLogger( Constants.MOD_ID ).info( extendedState.getUnlistedNames().toString() );

			return extendedState;
		}*/
		return state;
	}

	public boolean canConnectTo( IBlockAccess worldIn, BlockPos pos )
	{
		final IBlockState iblockstate = worldIn.getBlockState( pos );
		final Block block = iblockstate.getBlock();
		return block instanceof TileCrate;
	}

	/*
	 * @Override
	 *
	 * @SideOnly(Side.CLIENT)
	 * public void registerBlockIcons(IIconRegister iconRegister) {
	 * texture.registerIcons(iconRegister, Constants.modId + ":crate/%s");
	 * }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly(Side.CLIENT)
	 * public IIcon getIcon(int side, int meta) { return texture.getIcon("all"); }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly(Side.CLIENT)
	 * public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
	 * return texture.getConnectedIcon(world, x, y, z, ForgeDirection.getOrientation(side));
	 * }
	 */

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityCrate();
	}

	/*
	 * public void onBlockPlacedExtended(World world, int x, int y, int z,
	 * int side, float hitX, float hitY, float hitZ,
	 * EntityLivingBase entity, ItemStack stack) {
	 * TileEntityCrate crate = WorldUtils.get(world, x, y, z, TileEntityCrate.class);
	 * if (stack.hasDisplayName())
	 * crate.setCustomTitle(stack.getDisplayName());
	 * crate.attemptConnect(ForgeDirection.getOrientation(side).getOpposite());
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, ItemStack heldItem,
	 * EnumFacing side, float hitX, float hitY, float hitZ )
	 * {
	 * if( world.isRemote )
	 * return true;
	 * //WorldUtils.get( world, pos.getX(), pos.getY(), pos.getZ(), TileEntityCrate.class ).openGui( player );
	 * //TileEntityCrate tileEntity = (TileEntityCrate)world.getTileEntity( pos );
	 * //tileEntity.openGui( player );
	 * //player.openGui( Constants.modId, EnumGui.CRATE.getGuiID(), world, pos.getX(), pos.getY(), pos.getZ() );
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public boolean hasComparatorInputOverride() { return true; }
	 */

	private class ConnectedTextureCrate extends ConnectedTexture
	{
		@Override
		public boolean canConnect( IBlockAccess world, BlockPos pos, EnumFacing side, EnumFacing connected )
		{
			if( world.getBlockState( pos ) != TileCrate.this )
				return false;
			final int offX = pos.getX() + connected.getFrontOffsetX();
			final int offY = pos.getY() + connected.getFrontOffsetY();
			final int offZ = pos.getZ() + connected.getFrontOffsetZ();

			if( offY <= 0 )
				return false;

			final TileEntityCrate connectedCrate = WorldUtils.get( world, offX, offY, offZ, TileEntityCrate.class );
			if( connectedCrate == null )
				return false;
			final TileEntityCrate crate = WorldUtils.get( world, pos.getX(), pos.getY(), pos.getZ(), TileEntityCrate.class );
			return crate.getID() == connectedCrate.getID() && !crate.equals( connectedCrate );
		}
	}

	/*
	 * @Override
	 * public boolean canMove(World world, int x, int y, int z) {
	 * return false;
	 * }
	 */
}
