package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;

public class ItemCardboardArmor extends ItemArmor implements /*ICardboardItem,*/ ISpecialArmor
{

	private static final String[] armorText = { "Helmet", "Chestplate", "Leggings", "Boots" };

	public ItemCardboardArmor( EntityEquipmentSlot armorType )
	{
		super( ItemCardboardSheet.armorMaterial, 0, armorType );
	}

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type )
	{
		return ( type != null ? Resources.textureEmpty
				: slot == EntityEquipmentSlot.LEGS ? Resources.textureCardboardLeggins : Resources.textureCardboardArmor ).toString();
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

	/*@Override
	public boolean canDye( ItemStack stack )
	{
		return true;
	}*/

	// ISpecialArmor implementation
	// Makes sure cardboard armor doesn't get destroyed,
	// and is ineffective when durability is at 0..
	@Override
	public ArmorProperties getProperties( EntityLivingBase player, ItemStack armor, DamageSource source, double damage, int slot )
	{
		return new ArmorProperties( 0, damageReduceAmount / 25D, armor.getMaxDamage() - armor.getItemDamage() );
	}

	@Override
	public int getArmorDisplay( EntityPlayer player, ItemStack armor, int slot )
	{
		return armor.getItemDamage() < armor.getMaxDamage() ? damageReduceAmount : 0;
	}

	@Override
	public void damageArmor( EntityLivingBase entity, ItemStack stack, DamageSource source, int damage, int slot )
	{
		ItemCardboardSheet.damageItem( stack, damage, entity );
	}

}
