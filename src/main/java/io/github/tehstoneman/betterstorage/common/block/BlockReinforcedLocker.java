package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.common.block.BlockLocker.EnumLockerPart;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityReinforcedLocker;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReinforcedLocker extends BlockLockable
{
	public BlockReinforcedLocker()
	{
		super( Material.WOOD );

		setHardness( 8.0F );
		setResistance( 20.0F );

		setHarvestLevel( "axe", 2 );
		setSoundType( SoundType.WOOD );

		setDefaultState( blockState.getBaseState().withProperty( FACING, EnumFacing.NORTH ).withProperty( MATERIAL, EnumReinforced.IRON )
				.withProperty( BlockLocker.PART, EnumLockerPart.SINGLE ) );
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
		final IProperty[] listedProperties = new IProperty[] { FACING, MATERIAL, BlockLocker.PART };
		return new BlockStateContainer( this, listedProperties );
	}

	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedLocker )
		{
			final TileEntityReinforcedLocker locker = (TileEntityReinforcedLocker)tileEntity;
			if( !locker.isConnected() )
				state = state.withProperty( BlockLocker.PART, EnumLockerPart.SINGLE );
			else
				if( locker.isMain() )
					state = state.withProperty( BlockLocker.PART, EnumLockerPart.BOTTOM );
				else
					state = state.withProperty( BlockLocker.PART, EnumLockerPart.TOP );
		}

		return super.getActualState( state, world, pos );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityReinforcedLocker();
	}
}
