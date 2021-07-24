package io.github.tehstoneman.betterstorage.common.item.locking;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMasterKey extends ItemKey
{
	@Override
	public void onCraftedBy( ItemStack stack, World worldIn, PlayerEntity playerIn )
	{}

	@Override
	public boolean isFoil( ItemStack stack )
	{
		return true;
	}

	/*
	 * ====
	 * IKey
	 * ====
	 */

	@Override
	public boolean unlock( ItemStack key, ItemStack lock, boolean useAbility )
	{
		return true;
	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return false;
	}

	@Override
	public boolean isEnchantable( ItemStack stack )
	{
		return false;
	}
}
