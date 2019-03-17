package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumBlockRenderType;

public class BlockReinforcedLocker extends BlockLockable
{
	public BlockReinforcedLocker( Material material )
	{
		super( "reinforced_locker", material );

		// setHardness( 8.0F );
		// setResistance( 20.0F );

		// setHarvestLevel( "axe", 2 );
		// setSoundType( SoundType.WOOD );

		//@formatter:off
		/*setDefaultState( blockState.getBaseState().withProperty( BlockHorizontal.FACING, EnumFacing.NORTH )
												  .withProperty( BlockLockable.MATERIAL, EnumReinforced.IRON )
												  .withProperty( Properties.StaticProperty, true )
												  .withProperty( BlockDoor.HINGE, BlockDoor.EnumHingePosition.LEFT )
												  .withProperty( BlockLockable.CONNECTED, false ) );*/
		//@formatter:on
	}

	public BlockReinforcedLocker()
	{
		this( Material.WOOD );
	}

	/*
	 * @Override
	 * public boolean isOpaqueCube( IBlockState state )
	 * {
	 * return false;
	 * }
	 */

	@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}

	@Override
	// @SideOnly( Side.CLIENT )
	public EnumBlockRenderType getRenderType( IBlockState state )
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	/*
	 * @Override
	 * public BlockStateContainer createBlockState()
	 * {
	 * return new BlockStateContainer( this, new IProperty[] { BlockHorizontal.FACING, BlockLockable.MATERIAL, Properties.StaticProperty, BlockDoor.HINGE,
	 * CONNECTED } );
	 * }
	 */

	/*
	 * @Override
	 * public void getSubBlocks( CreativeTabs tab, NonNullList< ItemStack > list )
	 * {
	 * for( final EnumReinforced material : EnumReinforced.values() )
	 * {
	 * final ItemStack itemstack = new ItemStack( this, 1, material.getMetadata() );
	 * list.add( itemstack );
	 * }
	 * }
	 */

	/*
	 * @Override
	 * public IBlockState getStateFromMeta( int meta )
	 * {
	 * EnumFacing enumfacing = EnumFacing.getFront( (meta & 3) + 2 );
	 * 
	 * final Boolean mirrored = ( meta & 8 ) > 0;
	 * 
	 * return getDefaultState().withProperty( BlockHorizontal.FACING, enumfacing ).withProperty( BlockDoor.HINGE,
	 * mirrored ? BlockDoor.EnumHingePosition.RIGHT : BlockDoor.EnumHingePosition.LEFT );
	 * }
	 */

	/*
	 * @Override
	 * public int getMetaFromState( IBlockState state )
	 * {
	 * int meta = ( state.getValue( BlockHorizontal.FACING ).getIndex() - 2 ) & 3;
	 * if( state.getValue( BlockDoor.HINGE ) == BlockDoor.EnumHingePosition.RIGHT )
	 * meta |= 8;
	 * return meta;
	 * }
	 */

	/*
	 * @Override
	 * public IBlockState getActualState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	 * {
	 * final TileEntity tileentity = worldIn instanceof ChunkCache ? ( (ChunkCache)worldIn ).getTileEntity( pos, Chunk.EnumCreateEntityType.CHECK )
	 * : worldIn.getTileEntity( pos );
	 * 
	 * if( tileentity instanceof TileEntityReinforcedLocker )
	 * {
	 * final TileEntityReinforcedLocker locker = (TileEntityReinforcedLocker)tileentity;
	 * if( locker.getMaterial() != null )
	 * state = state.withProperty( MATERIAL, locker.getMaterial() );
	 * }
	 * 
	 * return state.withProperty( Properties.StaticProperty, true );
	 * }
	 */

	/*
	 * @Override
	 * public TileEntity createNewTileEntity( World world, int metadata )
	 * {
	 * return new TileEntityReinforcedLocker();
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
	 * float hitY, float hitZ )
	 * {
	 * final EnumFacing facing = state.getValue( BlockHorizontal.FACING );
	 * if( facing == side )
	 * {
	 * if( !world.isRemote )
	 * {
	 * final TileEntity tileentity = world.getTileEntity( pos );
	 * if( tileentity instanceof TileEntityLocker )
	 * {
	 * final TileEntityLocker locker = (TileEntityLocker)tileentity;
	 * return locker.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
	 * }
	 * }
	 * return true;
	 * }
	 * return false;
	 * }
	 */

	@Override
	public boolean hasComparatorInputOverride( IBlockState state )
	{
		return true;
	}

	/*
	 * @Override
	 * 
	 * @SideOnly( Side.CLIENT )
	 * public void registerItemModels()
	 * {
	 * for( final EnumReinforced material : EnumReinforced.values() )
	 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( this ), material.getMetadata(),
	 * new ModelResourceLocation( getRegistryName() + "_" + material.getName(), "inventory" ) );
	 * }
	 */

	/*
	 * @Override
	 * protected ItemBlock getItemBlock()
	 * {
	 * return new ItemBlockReinforcedLocker( this );
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack getPickBlock( IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player )
	 * {
	 * final TileEntity tileentity = world.getTileEntity( pos );
	 * EnumReinforced material = EnumReinforced.IRON;
	 * 
	 * if( tileentity instanceof TileEntityReinforcedLocker )
	 * {
	 * final TileEntityReinforcedLocker clocker = (TileEntityReinforcedLocker)tileentity;
	 * material = clocker.getMaterial();
	 * }
	 * return new ItemStack( Item.getItemFromBlock( this ), 1, material.getMetadata() );
	 * }
	 */
}
