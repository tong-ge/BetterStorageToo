package io.github.tehstoneman.betterstorage.common.item;

import net.minecraft.block.Block;

public class ItemBlockLockable extends BlockItemBetterStorage
{
	public ItemBlockLockable( Block block )
	{
		super( block );
		// setHasSubtypes( true );
		// setMaxDamage( 0 );
	}

	/*
	 * @Override
	 * public int getMetadata( int metadata )
	 * {
	 * return metadata;
	 * }
	 */

	/*
	 * @Override
	 * public String getItemStackDisplayName( ItemStack stack )
	 * {
	 * final Block block = Block.getBlockFromItem( stack.getItem() );
	 * if( block instanceof TileLockable )
	 * {
	 * if( !( (TileLockable)Block.getBlockFromItem( stack.getItem() ) ).hasMaterial() )
	 * return super.getItemStackDisplayName( stack );
	 *
	 * final ContainerMaterial material = ContainerMaterial.getMaterial( stack, ContainerMaterial.iron );
	 *
	 * final String name = I18n.format( getUnlocalizedName() + ".name.full" );
	 * final String materialName = I18n.format( "material." + ModInfo.modId + "." + material.name );
	 * return name.replace( "%MATERIAL%", materialName );
	 * }
	 *
	 * if( block instanceof BlockLockable )
	 * {
	 * final EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
	 *
	 * final String materialName = BetterStorage.proxy.localize( "material." + ModInfo.modId + "." + material.getName() );
	 * final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
	 * return name.trim();
	 * }
	 * return super.getItemStackDisplayName( stack );
	 * }
	 */

	/*
	 * @Override
	 * public String getUnlocalizedName( ItemStack stack )
	 * {
	 * return super.getUnlocalizedName();// + "." + EnumReinforced.byMetadata( stack.getMetadata() ).getName();
	 * }
	 */
}
