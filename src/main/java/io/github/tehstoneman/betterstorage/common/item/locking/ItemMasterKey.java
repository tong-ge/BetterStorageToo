package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemMasterKey extends ItemKey
{
	public ItemMasterKey()
	{
		super( "master_key" );
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
