package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockBetterStorage extends Block
{
	private final String name;

	public BlockBetterStorage( String name, Material material )
	{
		super( material );

		setCreativeTab( BetterStorage.creativeTab );

		this.name = name;
	}

	/** Returns the name of this tile, for example "craftingStation". */
	public String getTileName()
	{
		return name;
	}

	/** Returns the item class used for this block. */
	protected ItemBlock getItemBlock()
	{
		return new ItemBlock( this );
	}

	/** Registers the block in the GameRegistry. */
	public void registerBlock()
	{
		setUnlocalizedName( ModInfo.modId + "." + name );
		this.setRegistryName( name );
		GameRegistry.register( this );

		final ItemBlock itemBlock = getItemBlock();

		if( itemBlock != null )
			GameRegistry.register( itemBlock.setRegistryName( getRegistryName() ) );
	}

	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( this ), 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	}
}
