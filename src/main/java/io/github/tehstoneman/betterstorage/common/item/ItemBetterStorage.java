package io.github.tehstoneman.betterstorage.common.item;

import io.github.tehstoneman.betterstorage.BetterStorage;
import net.minecraft.item.Item;

public abstract class ItemBetterStorage extends Item
{
	public ItemBetterStorage( String name, Properties properties )
	{
		super( new Item.Properties().group( BetterStorage.ITEM_GROUP ) );
		// setCreativeTab( BetterStorage.creativeTab );
	}

	/*
	 * public void register()
	 * {
	 * setUnlocalizedName( ModInfo.modId + "." + name );
	 * setRegistryName( name );
	 * //GameRegistry.register( this );
	 * }
	 */

	/*
	 * @SideOnly( Side.CLIENT )
	 * public void registerItemModels()
	 * {
	 * ModelLoader.setCustomModelResourceLocation( this, 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	 * }
	 */
}
