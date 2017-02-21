package io.github.tehstoneman.betterstorage.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemArmorBetterStorage extends ItemArmor
{
	private String name;

	public ItemArmorBetterStorage( ArmorMaterial material, int renderSlot, EntityEquipmentSlot slot )
	{
		super( material, renderSlot, slot );

		setCreativeTab( BetterStorage.creativeTab );

		setUnlocalizedName( ModInfo.modId + "." + getItemName() );
		// GameRegistry.registerItem( this, getItemName() );
	}

	/** Returns the name of this item, for example "drinkingHelmet". */
	public String getItemName()
	{
		return name != null ? name : ( name = MiscUtils.getName( this ) );
	}
}
