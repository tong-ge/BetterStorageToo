package io.github.tehstoneman.betterstorage.tile;

import java.util.List;

import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.attachment.Attachments;
import io.github.tehstoneman.betterstorage.attachment.EnumAttachmentInteraction;
import io.github.tehstoneman.betterstorage.attachment.IHasAttachments;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityLockable;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class TileLockable extends TileContainerBetterStorage
{
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	protected TileLockable( Material material )
	{
		super( material );
		setDefaultState( blockState.getBaseState().withProperty( FACING, EnumFacing.NORTH ) );
	}

	public boolean hasMaterial()
	{
		return true;
	}

	@Override
	public void getSubBlocks( Item item, CreativeTabs tab, List list )
	{
		if( !hasMaterial() )
			super.getSubBlocks( item, tab, list );
		else
			for( final ContainerMaterial material : ContainerMaterial.getMaterials() )
				list.add( material.setMaterial( new ItemStack( item, 1, 0 ) ) );
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		//@formatter:off
		final IProperty[] listedProperties = new IProperty[] { FACING };
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

	/*
	 * @Override
	 * public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
	 * if (hasMaterial() && !player.capabilities.isCreativeMode)
	 * dropBlockAsItem(world, pos, WorldUtils.get(world, pos.getX(), pos.getY(), pos.getZ(), TileEntityLockable.class).material.setMaterial(new ItemStack(this,
	 * 1, 0)), 0);
	 * return super.removedByPlayer(world, player, x, y, z);
	 * }
	 */

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

	@Override
	public float getBlockHardness( IBlockState blockState, World worldIn, BlockPos pos )
	{
		final TileEntityLockable lockable = WorldUtils.get( worldIn, pos.getX(), pos.getY(), pos.getZ(), TileEntityLockable.class );
		if( lockable != null && lockable.getLock() != null )
			return -1;
		else
			return super.getBlockHardness( blockState, worldIn, pos );
	}

	@Override
	public float getExplosionResistance( World world, BlockPos pos, Entity exploder, Explosion explosion )
	{
		float modifier = 1.0F;
		final TileEntityLockable lockable = WorldUtils.get( world, pos.getX(), pos.getY(), pos.getZ(), TileEntityLockable.class );
		if( lockable != null )
		{
			final int persistance = BetterStorageEnchantment.getLevel( lockable.getLock(), "persistance" );
			if( persistance > 0 )
				modifier += Math.pow( 2, persistance );
		}
		return super.getExplosionResistance( exploder ) * modifier;
	}

	/*
	 * @Override
	 * public RayTraceResult collisionRayTrace( IBlockState blockState, World worldIn, BlockPos pos, Vec3d start, Vec3d end )
	 * {
	 * return WorldUtils.get( worldIn, pos.getX(), pos.getY(), pos.getZ(), IHasAttachments.class ).getAttachments().rayTrace( worldIn, pos.getX(),
	 * pos.getY(), pos.getZ(), start, end );
	 * }
	 */

	@Override
	public void onBlockClicked( World worldIn, BlockPos pos, EntityPlayer playerIn )
	{
		// TODO: See if we can make a pull request to Forge to get PlayerInteractEvent to fire for left click on client.
		final Attachments attachments = WorldUtils.get( worldIn, pos.getX(), pos.getY(), pos.getZ(), IHasAttachments.class ).getAttachments();
		final boolean abort = attachments.interact( WorldUtils.rayTrace( playerIn, 1.0F ), playerIn, EnumAttachmentInteraction.attack );
		// TODO: Abort block breaking? playerController.resetBlockBreaking doesn't seem to do the job.
	}

	@Override
	public boolean hasComparatorInputOverride( IBlockState state )
	{
		return true;
	}

	// Trigger enchantment related

	@Override
	public boolean canProvidePower( IBlockState state )
	{
		return true;
	}

	/*
	 * @Override
	 * public int isProvidingWeakPower(IBlockAccess world, int x, int y, int z, int side) {
	 * return (WorldUtils.get(world, x, y, z, TileEntityLockable.class).isPowered() ? 15 : 0);
	 * }
	 */

	/*
	 * @Override
	 * public int isProvidingStrongPower(IBlockAccess world, int x, int y, int z, int side) {
	 * return isProvidingWeakPower(world, x, y, z, side);
	 * }
	 */

	/*
	 * @Override
	 * public void updateTick(World world, int x, int y, int z, Random random) {
	 * WorldUtils.get(world, x, y, z, TileEntityLockable.class).setPowered(false);
	 * }
	 */
}
