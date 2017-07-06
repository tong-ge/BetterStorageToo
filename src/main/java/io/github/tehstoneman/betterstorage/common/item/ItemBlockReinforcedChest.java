package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.BetterStorageAPI;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemBlockReinforcedChest extends ItemBlock
{
	public ItemBlockReinforcedChest( Block block )
	{
		super( block );
		setMaxDamage( 0 );
		setHasSubtypes( true );
	}

	@Override
	public int getMetadata( int metadata )
	{
		return metadata;
	}

	@Override
	public String getItemStackDisplayName( ItemStack stack )
	{
		EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
		//final EnumReinforced material = BetterStorageAPI.materials.getMaterial( stack );
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
		EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
		//return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
		//final EnumReinforced material = BetterStorageAPI.materials.getMaterial( stack );
		if( material != null )
		{
			final String materialName = BetterStorage.proxy.localize( material.getUnlocalizedName() );
			final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
			return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
		}
		return super.getUnlocalizedName();
	}
}
