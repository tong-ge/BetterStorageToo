package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ItemBlockReinforcedLocker extends ItemBlockLocker
{
	public ItemBlockReinforcedLocker( Block block )
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
		final EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
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
		final EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
		if( material != null )
		{
			final String materialName = BetterStorage.proxy.localize( material.getUnlocalizedName() );
			final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
			return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
		}
		return super.getUnlocalizedName();
	}
}
