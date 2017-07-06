package io.github.tehstoneman.betterstorage.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.internal.IMaterialRegistry;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.block.ReinforcedMaterial;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class MaterialRegistry implements IMaterialRegistry
{
	private static Map< String, ReinforcedMaterial > materialMap = new HashMap<>();

	public MaterialRegistry()
	{
		// Vanilla materials
		register( "iron", Items.IRON_INGOT, Blocks.IRON_BLOCK );
		register( "gold", Items.GOLD_INGOT, Blocks.GOLD_BLOCK );
		register( "diamond", Items.DIAMOND, Blocks.DIAMOND_BLOCK );
		register( "emerald", Items.EMERALD, Blocks.EMERALD_BLOCK );

		// Mod materials
		register( "copper", "ingotCopper", "blockCopper" );
		register( "tin", "ingotTin", "blockTin" );
		register( "silver", "ingotSilver", "blockSilver" );
		register( "zinc", "ingotZinc", "blockZinc" );
		register( "steel", "ingotSteel", "blockSteel" );
	}

	public void register( String materialName, Object materialIngot, Object materialBlock )
	{
		this.register( ModInfo.modId, materialName, materialIngot, materialBlock );
	}

	@Override
	public void register( String modID, String materialName, Object materialIngot, Object materialBlock )
	{
		if( !materialMap.containsKey( materialName ) )
			materialMap.put( materialName, new ReinforcedMaterial( modID, materialName, materialIngot, materialBlock ) );
	}

	@Override
	public EnumReinforced get( String name )
	{
		return EnumReinforced.byName( name );
	}

	@Override
	public EnumReinforced getMaterial( ItemStack stack )
	{
		return EnumReinforced.byMetadata( stack.getMetadata() );
		/*if( stack.hasTagCompound() )
		{
			final NBTTagCompound compound = stack.getTagCompound();
			if( compound.hasKey( ReinforcedMaterial.TAG_NAME ) )
				return get( compound.getString( ReinforcedMaterial.TAG_NAME ) );
		}
		return null;*/
	}
	
	@Override
	public Set< String > getMaterialList()
	{
		return materialMap.keySet();
	}
}
