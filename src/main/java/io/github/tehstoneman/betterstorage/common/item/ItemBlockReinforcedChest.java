package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.BetterStorageAPI;
import io.github.tehstoneman.betterstorage.common.block.ReinforcedMaterial;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemBlockReinforcedChest extends ItemBlock
{
	public ItemBlockReinforcedChest( Block block )
	{
		super( block );
	}

	@Override
	public String getItemStackDisplayName( ItemStack stack )
	{
		final ReinforcedMaterial material = BetterStorageAPI.materials.getMaterial( stack );
		if( material != null )
		{
			final String materialName = BetterStorage.proxy.localize( material.getUnlocalizedName() );
			final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
			return name.trim();
		}
		return super.getItemStackDisplayName( stack );
	}

	@Override
	public String getUnlocalizedName( ItemStack stack )
	{
		final ReinforcedMaterial material = BetterStorageAPI.materials.getMaterial( stack );
		if( material != null )
		{
			final String materialName = BetterStorage.proxy.localize( material.getUnlocalizedName() );
			final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
			return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
		}
		return super.getUnlocalizedName();
	}
}
