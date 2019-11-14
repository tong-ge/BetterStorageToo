package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.nbt.CompoundNBT;

public class ItemCardboardSword extends SwordItem implements ICardboardItem
{
	public ItemCardboardSword()
	{
		super( ItemTier.WOOD, 3, -2.4F, new Item.Properties().group( ItemGroup.COMBAT ) );
	}

	// Makes sure cardboard tools don't get destroyed,
	// and are ineffective when durability is at 0.
	/*
	 * @Override
	 * public boolean canHarvestBlock( IBlockState block, ItemStack stack )
	 * {
	 * return ItemCardboardSheet.canHarvestBlock( stack, super.canHarvestBlock( block, stack ) );
	 * }
	 */

	/*
	 * @Override
	 * public boolean onLeftClickEntity( ItemStack stack, EntityPlayer player, Entity entity )
	 * {
	 * return !ItemCardboardSheet.isEffective( stack );
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockDestroyed( ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase player )
	 * {
	 * //return ItemCardboardSheet.onBlockDestroyed( stack, world, block, pos, player );
	 * return block.getBlockHardness( world, pos ) > 0 ? ItemCardboardSheet.damageItem( stack, 1, player ) : true;
	 * }
	 */

	/*
	 * @Override
	 * public boolean hitEntity( ItemStack stack, EntityLivingBase target, EntityLivingBase player )
	 * {
	 * return ItemCardboardSheet.damageItem( stack, 1, player );
	 * }
	 */

	// Cardboard items
	@Override
	public boolean canDye( ItemStack stack )
	{
		return true;
	}

	@Override
	public int getColor( ItemStack itemstack )
	{
		if( hasColor( itemstack ) )
		{
			final CompoundNBT compound = itemstack.getTag();
			return compound.getInt( "color" );
		}
		return 0xA08060;
	}

	@Override
	public boolean hasColor( ItemStack itemstack )
	{
		if( itemstack.hasTag() )
		{
			final CompoundNBT compound = itemstack.getTag();
			return compound.contains( "color" );
		}
		return false;
	}

	@Override
	public void setColor( ItemStack itemstack, int colorRGB )
	{
		final CompoundNBT compound = itemstack.getOrCreateTag();
		compound.putInt( "color", colorRGB );
		itemstack.setTag( compound );
	}
}
