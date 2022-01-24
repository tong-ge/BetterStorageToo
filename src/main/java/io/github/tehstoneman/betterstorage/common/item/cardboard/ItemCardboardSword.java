package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.cardboard.ICardboardItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;

public class ItemCardboardSword extends SwordItem implements ICardboardItem
{
	public ItemCardboardSword()
	{
		super( Tiers.WOOD, 3, -2.4F, new Item.Properties().tab( BetterStorage.ITEM_GROUP ) );
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
	 * public boolean onBlockDestroyed( ItemStack stack, Level world, IBlockState block, BlockPos pos, EntityLivingBase player )
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
}
