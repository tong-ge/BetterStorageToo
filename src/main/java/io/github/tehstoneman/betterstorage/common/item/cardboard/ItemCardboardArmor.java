package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;

public class ItemCardboardArmor extends DyeableArmorItem implements ICardboardItem
{
	private static final String[] armorText = { "Helmet", "Chestplate", "Leggings", "Boots" };

	public ItemCardboardArmor( EquipmentSlotType armorSlot )
	{
		super( BetterStorageArmorMaterial.CARDBOARD, armorSlot, new Item.Properties().group( ItemGroup.COMBAT ) );
	}

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

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, EquipmentSlotType slot, String type )
	{
		return ( type != null ? Resources.TEXTURE_EMPTY
				: slot == EquipmentSlotType.LEGS ? Resources.TEXTURE_CARDBOARD_LEGGINGS : Resources.TEXTURE_CARDBOARD_ARMOR ).toString();
	}
}
