package io.github.tehstoneman.betterstorage.utils;

import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import io.github.tehstoneman.betterstorage.api.internal.IMaterialRegistry;
import net.minecraft.item.ItemStack;

public class MaterialRegistry implements IMaterialRegistry
{
	@Override
	public EnumReinforced get( String name )
	{
		return EnumReinforced.byName( name );
	}

	@Override
	public EnumReinforced get( ItemStack stack )
	{
		// return EnumReinforced.byMetadata( stack.getMetadata() );
		return null;
	}
}
