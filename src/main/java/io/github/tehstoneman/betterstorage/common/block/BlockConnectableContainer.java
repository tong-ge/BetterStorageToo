package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.api.EnumConnectedType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;

public abstract class BlockConnectableContainer extends BlockContainerBetterStorage
{
	public static final EnumProperty< EnumConnectedType > TYPE = EnumProperty.create( "type", EnumConnectedType.class );

	protected BlockConnectableContainer( Block.Properties builder )
	{
		super( builder );

		//@formatter:off
		setDefaultState( stateContainer.getBaseState().with( TYPE, EnumConnectedType.SINGLE ) );
		//@formatter:on
	}

	@Override
	protected void fillStateContainer( StateContainer.Builder< Block, BlockState > builder )
	{
		super.fillStateContainer( builder );
		builder.add( TYPE );
	}

	/*
	 * @Override
	 * public void onBlockExploded(World world, BlockPos pos, Explosion explosion) {
	 * if (hasMaterial())
	 * dropBlockAsItem(world, x, y, z, WorldUtils.get(world, x, y, z, TileEntityLockable.class).material.setMaterial(new ItemStack(this, 1, 0)));
	 * super.onBlockExploded(world, x, y, z, explosion);
	 * }
	 */

	/*
	 * @Override
	 * public int quantityDropped(int meta, int fortune, Random random) { return (hasMaterial() ? 0 : 1); }
	 */

	/*
	 * @Override
	 * public float getBlockHardness( IBlockState blockState, World worldIn, BlockPos pos )
	 * {
	 * final TileEntity tileEntity = worldIn.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityLockable )
	 * {
	 * final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
	 * if( !lockable.getLock().isEmpty() )
	 * return -1;
	 * }
	 * return super.getBlockHardness( blockState, worldIn, pos );
	 * }
	 */

	/*
	 * @Override
	 * public float getExplosionResistance( World world, BlockPos pos, Entity exploder, Explosion explosion )
	 * {
	 * float modifier = 1.0F;
	 * final TileEntity tileEntity = world.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityLockable )
	 * {
	 * final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
	 * final int persistance = BetterStorageEnchantment.getLevel( lockable.getLock(), EnchantmentBetterStorage.persistance );
	 * if( persistance > 0 )
	 * modifier += Math.pow( 2, persistance );
	 * }
	 * return super.getExplosionResistance( exploder ) * modifier;
	 * }
	 */

	/*
	 * @Override
	 * public RayTraceResult collisionRayTrace( IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end )
	 * {
	 * return WorldUtils.get( worldIn, pos.getX(), pos.getY(), pos.getZ(), IHasAttachments.class ).getAttachments().rayTrace( worldIn, pos.getX(),
	 * pos.getY(), pos.getZ(), start, end );
	 * }
	 */

	/*
	 * @Override
	 * public void onBlockClicked( World worldIn, BlockPos pos, EntityPlayer playerIn )
	 * {
	 * // TODO: See if we can make a pull request to Forge to get PlayerInteractEvent to fire for left click on client.
	 * final Attachments attachments = WorldUtils.get( worldIn, pos.getX(), pos.getY(), pos.getZ(), IHasAttachments.class ).getAttachments();
	 * final boolean abort = attachments.interact( WorldUtils.rayTrace( playerIn, 1.0F ), playerIn, EnumAttachmentInteraction.attack );
	 * // TODO: Abort block breaking? playerController.resetBlockBreaking doesn't seem to do the job.
	 * }
	 */

	/*
	 * @Override
	 * public boolean hasComparatorInputOverride( IBlockState state )
	 * {
	 * return true;
	 * }
	 */

	// Trigger enchantment related

	/*
	 * @Override
	 * public boolean canProvidePower( IBlockState state )
	 * {
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public int getWeakPower( IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side )
	 * {
	 * final TileEntity tileEntity = blockAccess.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityLockable )
	 * return ( (TileEntityLockable)tileEntity ).isPowered() ? 15 : 0;
	 * return 0;
	 * }
	 */

	/*
	 * @Override
	 * public int getStrongPower( IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side )
	 * {
	 * return getWeakPower( blockState, blockAccess, pos, side );
	 * }
	 */

	/*
	 * @Override
	 * public void updateTick( World worldIn, BlockPos pos, IBlockState state, Random rand )
	 * {
	 * final TileEntity tileEntity = worldIn.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityLockable )
	 * ( (TileEntityLockable)tileEntity ).setPowered( false );
	 * }
	 */

	/*
	 * @Override
	 * public ItemStack getPickBlock( IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player )
	 * {
	 * final TileEntity tileentity = world.getTileEntity( pos );
	 * EnumReinforced material = EnumReinforced.IRON;
	 *
	 * if( tileentity instanceof TileEntityLockable )
	 * {
	 * final TileEntityLockable lockable = (TileEntityLockable)tileentity;
	 * material = lockable.getMaterial();
	 * }
	 * return new ItemStack( Item.getItemFromBlock( this ), 1, material.getMetadata() );
	 * }
	 */
}
