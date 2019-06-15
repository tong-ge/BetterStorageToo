package io.github.tehstoneman.betterstorage.common.item;

import net.minecraft.block.Block.Properties;
import net.minecraft.item.IItemTier;

public abstract class ItemBetterStorageSword// extends ItemSword
{
	private final String name;

	public ItemBetterStorageSword( String name, IItemTier tier, int maxDamage, float attackDamage, Properties builder )
	{
		// super( tier, maxDamage, attackDamage, builder );

		this.name = name;
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
