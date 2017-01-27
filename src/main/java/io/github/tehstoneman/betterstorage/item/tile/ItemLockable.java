package io.github.tehstoneman.betterstorage.item.tile;

import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.tile.ContainerMaterial;
import io.github.tehstoneman.betterstorage.tile.TileLockable;
import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemLockable extends ItemBlock
{
	public ItemLockable( Block block )
	{
		super( block );
		setHasSubtypes( true );
		setMaxDamage( 0 );
	}

	@Override
	public int getMetadata( int damage )
	{
		return damage;
	}

	@Override
	public String getItemStackDisplayName( ItemStack stack )
	{
		if( !( (TileLockable)Block.getBlockFromItem( stack.getItem() ) ).hasMaterial() )
			return super.getItemStackDisplayName( stack );

		final ContainerMaterial material = ContainerMaterial.getMaterial( stack, ContainerMaterial.iron );

		final String name = I18n.format( getUnlocalizedName( stack ) + ".name.full" );
		final String materialName = I18n.format( "material." + Constants.modId + "." + material.name );
		return name.replace( "%MATERIAL%", materialName );
	}
}
