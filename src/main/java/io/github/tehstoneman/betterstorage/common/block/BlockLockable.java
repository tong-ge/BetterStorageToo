package io.github.tehstoneman.betterstorage.common.block;

import java.util.List;
import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.attachment.Attachments;
import io.github.tehstoneman.betterstorage.attachment.EnumAttachmentInteraction;
import io.github.tehstoneman.betterstorage.attachment.IHasAttachments;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemLock;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityConnectable;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockable;
import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockLockable extends BlockContainerBetterStorage
{
	public static final PropertyDirection	FACING		= BlockHorizontal.FACING;
	public static final PropertyEnum		MATERIAL	= PropertyEnum.create( "material", EnumReinforced.class );

	protected BlockLockable( Material material )
	{
		super( material );
		setDefaultState( blockState.getBaseState().withProperty( FACING, EnumFacing.NORTH ).withProperty( MATERIAL, EnumReinforced.IRON ) );
	}

	@Override
	public void getSubBlocks( Item item, CreativeTabs tab, List list )
	{
		for( final EnumReinforced material : EnumReinforced.values() )
			if( material != EnumReinforced.SPECIAL )
				list.add( new ItemStack( item, 1, material.getMetadata() ) );
	}

	@Override
	protected BlockStateContainer createBlockState()
	{
		final IProperty[] listedProperties = new IProperty[] { FACING, MATERIAL };
		return new BlockStateContainer( this, listedProperties );
	}

	@Override
	public IBlockState getStateFromMeta( int meta )
	{
		return getDefaultState().withProperty( MATERIAL, EnumReinforced.byMetadata( meta ) );
	}

	@Override
	public int getMetaFromState( IBlockState state )
	{
		return ( (EnumReinforced)state.getValue( MATERIAL ) ).getMetadata();
	}

	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityLockable )
		{
			final TileEntityLockable chest = (TileEntityLockable)tileEntity;
			if( chest.getOrientation() != null )
				state = state.withProperty( FACING, chest.getOrientation() );
		}
		return state;
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityConnectable )
			( (TileEntityConnectable)tileEntity ).onBlockPlaced( placer, stack );
		else
			super.onBlockPlacedBy( worldIn, pos, state, placer, stack );
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
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityLockable )
		{

			final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
			if( lockable != null && lockable.getLock() != null )
				return -1;
		}
		return super.getBlockHardness( blockState, worldIn, pos );
	}

	@Override
	public float getExplosionResistance( World world, BlockPos pos, Entity exploder, Explosion explosion )
	{
		float modifier = 1.0F;
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityLockable )
		{

			final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
			if( lockable != null )
			{
				final int persistance = BetterStorageEnchantment.getLevel( lockable.getLock(), "persistance" );
				if( persistance > 0 )
					modifier += Math.pow( 2, persistance );
			}
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

	public static enum EnumReinforced implements IStringSerializable
	{
		//@formatter:off
		IRON	( 0, "iron", "ingotIron", "blockIron" ),
		GOLD	( 1, "gold", "ingotGold", "blockGold" ),
		DIAMOND	( 2, "diamond", "gemDiamond", "blockDiamond" ),
		EMERALD	( 3, "emerald", "gemEmerald", "blockEmerald" ),
		SPECIAL	( 4, "special", "",""),
		COPPER	( 5, "copper", "ingotCopper", "blockCopper" ),
		TIN		( 6, "tin", "ingotTin", "blockTin" ),
		SILVER	( 7, "silver", "ingotSilver", "blockSilver" ),
		ZINC	( 8, "zinc", "ingotZinc", "blockZinc" ),
		STEEL	( 9, "steel", "ingotSteel", "blockSteel" );
		//@formatter:on

		private final int						meta;
		private final String					name;
		private final String					ingot;
		private final String					block;
		private static final EnumReinforced[]	META_LOOKUP	= new EnumReinforced[values().length];

		private EnumReinforced( int meta, String name, String ingot, String block )
		{
			this.meta = meta;
			this.name = name;
			this.ingot = ingot;
			this.block = block;
		}

		@Override
		public String getName()
		{
			return name;
		}

		public int getMetadata()
		{
			return meta;
		}

		public String getOreDictIngot()
		{
			return ingot;
		}

		public String getOreDictBlock()
		{
			return block;
		}

		public static EnumReinforced byMetadata( int meta )
		{
			if( meta <= 0 || meta >= META_LOOKUP.length )
				meta = 0;
			return META_LOOKUP[meta];
		}

		static
		{
			for( final EnumReinforced material : values() )
				META_LOOKUP[material.getMetadata()] = material;
		}
	}
}
