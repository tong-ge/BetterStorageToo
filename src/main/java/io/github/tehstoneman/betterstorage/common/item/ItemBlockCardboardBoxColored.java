package io.github.tehstoneman.betterstorage.common.item;

import net.minecraft.block.Block;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;

public class ItemBlockCardboardBoxColored extends ItemBlockCardboardBox
{
	public ItemBlockCardboardBoxColored( Block block )
	{
		super( block );
		setMaxDamage( 0 );
		setHasSubtypes( true );
	}

	@Override
	public int getMetadata( int meta )
	{
		return meta;
	}

	@Override
	public String getUnlocalizedName( ItemStack stack )
	{
		final EnumDyeColor color = EnumDyeColor.byMetadata( stack.getMetadata() );
		return super.getUnlocalizedName() + "." + color.getUnlocalizedName();
	}
}
