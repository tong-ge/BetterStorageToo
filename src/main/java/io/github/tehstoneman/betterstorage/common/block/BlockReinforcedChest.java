package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReinforcedChest extends BlockLockable
{
	public BlockReinforcedChest( Material material )
	{
		super( material );

		setHardness( 8.0F );
		setResistance( 20.0F );

		setHarvestLevel( "axe", 2 );
		setSoundType( SoundType.WOOD );
	}

	public BlockReinforcedChest()
	{
		this( Material.WOOD );
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
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( !( tileEntity instanceof TileEntityReinforcedChest ) )
			return super.getBoundingBox( state, world, pos );

		final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileEntity;
		if( chest.isConnected() )
		{
			final EnumFacing connected = chest.getConnected();
			if( connected == EnumFacing.NORTH )
				return new AxisAlignedBB( 0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F );
			else
				if( connected == EnumFacing.SOUTH )
					return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F );
				else
					if( connected == EnumFacing.WEST )
						return new AxisAlignedBB( 0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
					else
						if( connected == EnumFacing.EAST )
							return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F );
		}

		return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityReinforcedChest();
	}
}
