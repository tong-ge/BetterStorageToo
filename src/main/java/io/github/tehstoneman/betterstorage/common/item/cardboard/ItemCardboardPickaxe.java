package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemCardboardPickaxe extends ItemPickaxe implements ICardboardItem
{

	private String name;

	public ItemCardboardPickaxe()
	{
		super( ItemCardboardSheet.toolMaterial );
	}

	@Override
	public boolean canDye( ItemStack stack )
	{
		return true;
	}

	@Override
	public int getColor( ItemStack stack )
	{
		final NBTTagCompound nbttagcompound = stack.getTagCompound();

		if( nbttagcompound != null )
		{
			final NBTTagCompound nbttagcompound1 = nbttagcompound.getCompoundTag( "display" );

			if( nbttagcompound1 != null && nbttagcompound1.hasKey( "color", 3 ) )
				return nbttagcompound1.getInteger( "color" );
		}
		return 0x705030;
	}

	// Makes sure cardboard tools don't get destroyed,
	// and are ineffective when durability is at 0.
	@Override
	public boolean canHarvestBlock( IBlockState block, ItemStack stack )
	{
		return ItemCardboardSheet.canHarvestBlock( stack, super.canHarvestBlock( block, stack ) );
	}

	@Override
	public boolean onLeftClickEntity( ItemStack stack, EntityPlayer player, Entity entity )
	{
		return !ItemCardboardSheet.isEffective( stack );
	}

	/*
	 * @Override
	 * public boolean onBlockDestroyed( ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase player )
	 * {
	 * return ItemCardboardSheet.onBlockDestroyed( stack, world, block, pos, player );
	 * }
	 */

	@Override
	public boolean hitEntity( ItemStack stack, EntityLivingBase target, EntityLivingBase player )
	{
		return ItemCardboardSheet.damageItem( stack, 1, player );
	}
}
