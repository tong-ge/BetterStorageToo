package io.github.tehstoneman.betterstorage.common.block;

import net.minecraft.block.Block;

public class BlockBetterStorage extends Block
{
	public BlockBetterStorage( Properties properties )
	{
		super( properties );
	}

	/** Returns the item class used for this block. */
	/*
	 * protected ItemBlock getItemBlock()
	 * {
	 * return new ItemBlock( this );
	 * }
	 */

	/**
	 * Registers the block in the GameRegistry.
	 *
	 * @param registry
	 */
	/*
	 * public void registerBlock( IForgeRegistry< Block > registry )
	 * {
	 * setUnlocalizedName( ModInfo.modId + "." + name );
	 * this.setRegistryName( name );
	 * registry.register( this );
	 * 
	 * final ItemBlock itemBlock = getItemBlock();
	 * 
	 * if( itemBlock != null )
	 * ForgeRegistries.ITEMS.register( itemBlock.setRegistryName( getRegistryName() ) );
	 * }
	 */

	/*
	 * @SideOnly( Side.CLIENT )
	 * public void registerItemModels( ItemModelMesher mesher )
	 * {
	 * final Item item = Item.getItemFromBlock( this );
	 * final int meta = 0;
	 * final ModelResourceLocation model = new ModelResourceLocation( getRegistryName(), "inventory" );
	 * ModelLoader.setCustomModelResourceLocation( item, meta, model );
	 * mesher.register( item, meta, model );
	 * }
	 */
}
