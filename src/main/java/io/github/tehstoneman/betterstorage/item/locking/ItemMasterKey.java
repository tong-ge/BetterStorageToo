package io.github.tehstoneman.betterstorage.item.locking;

import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.item.ItemBetterStorage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMasterKey extends ItemBetterStorage implements IKey
{
	public ItemMasterKey()
	{
		setMaxDamage( 0 );
		setMaxStackSize( 1 );
	}

	@Override
	public void onCreated( ItemStack stack, World world, EntityPlayer player )
	{}

	@Override
	public void onUpdate( ItemStack stack, World world, Entity entity, int slot, boolean isBeingHeld )
	{}

	@Override
	@SideOnly( Side.CLIENT )
	public boolean hasEffect( ItemStack stack )
	{
		return true;
	}

	// IKey implementation
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
	public boolean isNormalKey()
	{
		return true;
	}
}
