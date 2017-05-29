package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBlockCrate extends ItemBlock
{
	public ItemBlockCrate( Block block )
	{
		super( block );
	}

	@Override
	public boolean placeBlockAt( ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ,
			IBlockState newState )
	{
		if( !super.placeBlockAt( stack, player, world, pos, side, hitX, hitY, hitZ, newState ) )
			return false;
		BetterStorageBlocks.CRATE.onBlockPlacedExtended( world, pos, side, hitX, hitY, hitZ, player, stack );
		return true;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void getSubItems( Item itemIn, CreativeTabs tab, NonNullList< ItemStack > subItems )
	{
		block.getSubBlocks( itemIn, tab, subItems );
	}
}
