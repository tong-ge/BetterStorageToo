package io.github.tehstoneman.betterstorage.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import io.github.tehstoneman.betterstorage.api.internal.IMaterialRegistry;
import io.github.tehstoneman.betterstorage.common.block.ReinforcedMaterial;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
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
		return EnumReinforced.byMetadata( stack.getMetadata() );
	}
}
