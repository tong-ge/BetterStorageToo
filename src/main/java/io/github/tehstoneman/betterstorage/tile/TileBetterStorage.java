package io.github.tehstoneman.betterstorage.tile;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileBetterStorage extends Block
{
	private String name;

	public TileBetterStorage( Material material )
	{
		super( material );

		setCreativeTab( BetterStorage.creativeTab );

		this.setUnlocalizedName( Constants.modId + "." + getTileName() );
		this.setRegistryName( getTileName() );
		registerBlock();
	}

	/** Returns the name of this tile, for example "craftingStation". */
	public String getTileName()
	{
		return name != null ? name : ( name = MiscUtils.getName( this ) );
	}

	/** Returns the item class used for this block. */
	protected Class< ? extends ItemBlock > getItemClass()
	{
		return ItemBlock.class;
	}

	/** Registers the block in the GameRegistry. */
	protected void registerBlock()
	{
		GameRegistry.register( this );
		GameRegistry.register( new ItemBlock( this ).setRegistryName( this.getRegistryName() ) );
		/*final Class< ? extends Item > itemClass = getItemClass();

		if( itemClass != null )
			GameRegistry.registerBlock( this, (Class< ? extends ItemBlock >)itemClass, getTileName(), Constants.modId );
		else
			GameRegistry.registerBlock( this, null, getTileName(), Constants.modId );*/
	}

	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( ItemBlock.getItemFromBlock( this ), 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	}
}
