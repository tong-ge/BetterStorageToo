package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.cardboard.ICardboardItem;
import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.DyeableArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemCardboardArmor extends DyeableArmorItem implements ICardboardItem
{
	public ItemCardboardArmor( EquipmentSlotType armorSlot )
	{
		super( BetterStorageArmorMaterial.CARDBOARD, armorSlot, new Item.Properties().group( BetterStorage.ITEM_GROUP ) );
	}

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, EquipmentSlotType slot, String type )
	{
		return ( type != null ? Resources.TEXTURE_EMPTY
				: slot == EquipmentSlotType.LEGS ? Resources.TEXTURE_CARDBOARD_LEGGINGS : Resources.TEXTURE_CARDBOARD_ARMOR ).toString();
	}

	@Override
	public boolean hasColor( ItemStack stack )
	{
		return ICardboardItem.super.hasColor( stack );
	}

	@Override
	public int getColor( ItemStack stack )
	{
		return ICardboardItem.super.getColor( stack );
	}

	@Override
	public void setColor( ItemStack itemStack, int colorRGB )
	{
		ICardboardItem.super.setColor( itemStack, colorRGB );
	}
}
