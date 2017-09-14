package io.github.tehstoneman.betterstorage.common.item;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemBetterStorage extends Item
{
	private String				name;

	public ItemBetterStorage( String name )
	{
		setCreativeTab( BetterStorage.creativeTab );
		
		this.name = name;
	}
	
	public void register()
	{
		setUnlocalizedName( ModInfo.modId + "." + name );
		setRegistryName( name );
		GameRegistry.register( this );
	}

	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( this, 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	}
}
