package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityConnectable;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLocker extends BlockContainerBetterStorage
{
	public static final PropertyDirection	FACING	= PropertyDirection.create( "facing", EnumFacing.Plane.HORIZONTAL );
	public static final PropertyEnum		PART	= PropertyEnum.create( "part", EnumLockerPart.class );

	public BlockLocker()
	{
		super( Material.WOOD );

		setHardness( 2.5f );

		setHarvestLevel( "axe", 0 );
		setSoundType( SoundType.WOOD );

		setDefaultState( blockState.getBaseState().withProperty( FACING, EnumFacing.NORTH ).withProperty( PART, EnumLockerPart.SINGLE ) );
	}

	@Override
	public boolean isOpaqueCube( IBlockState state )
	{
		return false;
	}

	@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public EnumBlockRenderType getRenderType( IBlockState state )
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public BlockStateContainer createBlockState()
	{
		final IProperty[] listedProperties = new IProperty[] { FACING, PART };
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
	public IBlockState getActualState( IBlockState state, IBlockAccess world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityLocker )
		{
			final TileEntityLocker locker = (TileEntityLocker)tileEntity;
			state = state.withProperty( FACING, locker.getOrientation() );
			if( !locker.isConnected() )
				state = state.withProperty( PART, EnumLockerPart.SINGLE );
			else
				if( locker.isMain() )
					state = state.withProperty( PART, EnumLockerPart.BOTTOM );
				else
					state = state.withProperty( PART, EnumLockerPart.TOP );
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

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityLocker();
	}

	@Override
	public boolean hasComparatorInputOverride( IBlockState state )
	{
		return true;
	}

	public static enum EnumLockerPart implements IStringSerializable
	{
		//@formatter:off
		SINGLE	( 0, "single" ),
		BOTTOM	( 1, "bottom" ),
		TOP		( 2, "top" );
		//@formatter:on

		private final int						meta;
		private final String					name;
		private static final EnumLockerPart[]	META_LOOKUP	= new EnumLockerPart[values().length];

		private EnumLockerPart( int meta, String name )
		{
			this.meta = meta;
			this.name = name;
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

		public static EnumLockerPart byMetadata( int meta )
		{
			if( meta <= 0 || meta >= META_LOOKUP.length )
				meta = 0;
			return META_LOOKUP[meta];
		}

		static
		{
			for( final EnumLockerPart part : values() )
				META_LOOKUP[part.getMetadata()] = part;
		}
	}
}
