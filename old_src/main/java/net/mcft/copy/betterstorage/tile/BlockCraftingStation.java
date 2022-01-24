package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCraftingStation;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.Level;

public class BlockCraftingStation extends Block implements ITileEntityProvider
{
	public BlockCraftingStation()
	{
		super( Material.IRON );

		setHardness( 1.5f );
		setSoundType(SoundType.STONE);
	}

	@Override
	public boolean isNormalCube( IBlockState state )
	{
		return false;
	}

	@Override
	public BlockEntity createNewTileEntity( Level world, int metadata )
	{
		return new TileEntityCraftingStation();
	}

	@Override
	public boolean onBlockActivated( Level worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem,
			EnumFacing side, float hitX, float hitY, float hitZ )
	{
		final BlockEntity tileEntity = worldIn.getBlockEntity( pos );
		if( tileEntity instanceof TileEntityCraftingStation )
			return ( (TileEntityCraftingStation)tileEntity ).onBlockActivated( playerIn, side.getIndex(), hitX, hitY, hitZ );

		return false;
	}
}
