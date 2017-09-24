package io.github.tehstoneman.betterstorage.common.block;

import java.util.Random;

import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemBlockCardboardBox;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockCardboardBox extends BlockContainerBetterStorage
{
	public BlockCardboardBox( String name )
	{
		super( name, Material.WOOD );

		setHardness( 0.8f );
		setSoundType( SoundType.CLOTH );
	}

	public BlockCardboardBox()
	{
		this( "cardboard_box" );
	}

	@Override
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos )
	{
		return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
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
	public int quantityDropped( Random rand )
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityCardboardBox();
	}

	@Override
	protected ItemBlock getItemBlock()
	{
		return new ItemBlockCardboardBox( this );
	}
}
