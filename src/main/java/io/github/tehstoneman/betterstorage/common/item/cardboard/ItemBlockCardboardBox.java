package io.github.tehstoneman.betterstorage.common.item.cardboard;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.api.cardboard.ICardboardItem;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BlockItemBetterStorage;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemStackHandler;

public class ItemBlockCardboardBox extends BlockItemBetterStorage implements ICardboardItem
{
	public ItemBlockCardboardBox()
	{
		super( BetterStorageBlocks.CARDBOARD_BOX.get(), new Item.Properties() );
	}

	// Item stuff

	@Override
	public boolean showDurabilityBar( ItemStack stack )
	{
		final int maxUses = getMaxUses();
		if( maxUses > 1 && stack.hasTag() )
		{
			int uses = maxUses;
			final CompoundNBT nbt = stack.getTag();
			if( nbt.contains( "Uses" ) )
				uses = Math.min( maxUses, nbt.getInt( "Uses" ) );
			return uses < maxUses;
		}
		return false;
	}

	@Override
	public double getDurabilityForDisplay( ItemStack stack )
	{
		final int maxUses = getMaxUses();
		int uses = maxUses;
		if( stack.hasTag() )
		{
			final CompoundNBT nbt = stack.getTag();
			if( nbt.contains( "Uses" ) )
				uses = Math.min( maxUses, nbt.getInt( "Uses" ) );
		}
		return 1d - (float)uses / maxUses;
	}

	@Override
	public void appendHoverText( ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn )
	{
		super.appendHoverText( stack, worldIn, tooltip, flagIn );
		if( stack.hasTag() )
		{
			if( flagIn.isAdvanced() )
			{
				final int maxUses = getMaxUses();
				final boolean hasItems = stack.hasTag() && stack.getTag().contains( "Inventory" );

				if( !hasItems )
					tooltip.add( new TranslationTextComponent( "tooltip.betterstorage.cardboard_box.use_hint" + ( maxUses > 0 ? ".reusable" : 0 ) ) );

				if( maxUses > 1 )
				{
					int uses = maxUses;
					if( stack.getTag().contains( "Uses" ) )
						uses = Math.min( maxUses, stack.getTag().getInt( "Uses" ) );
					tooltip.add( new TranslationTextComponent( "tooltip.betterstorage.cardboard_box.uses", uses ) );// TextFormatting.DARK_GRAY.toString() +
																													// TextFormatting.ITALIC +
				}
			}
			if( BetterStorageConfig.CLIENT.cardboardBoxShowContents.get() && stack.hasTag() && stack.getTag().contains( "Inventory" ) )
			{
				final ItemStackHandler contents = new ItemStackHandler( 9 );
				contents.deserializeNBT( (CompoundNBT)stack.getTag().get( "Inventory" ) );

				final int limit = 4;// flagIn.isAdvanced() || GuiScreen.isShiftKeyDown() ? 6 : 3;
				int count = 0;
				int more = 0;

				for( int i = 0; i < contents.getSlots(); i++ )
				{
					final ItemStack contentStack = contents.getStackInSlot( i );
					if( !contentStack.isEmpty() )
						if( count < limit )
						{
							count++;
							final IFormattableTextComponent text = contentStack.getDisplayName().copy();
							text.append( " x" ).append( String.valueOf( contentStack.getCount() ) );
							tooltip.add( text );
						}
						else
							more++;
				}
				if( more > 0 )
					tooltip.add( new TranslationTextComponent( "tooltip.betterstorage.cardboard_box.plus_more", more )
							.withStyle( TextFormatting.ITALIC ) );
			}
		}
	}

	/**
	 * Returns how many times cardboard boxes can be reused.
	 *
	 * @return Total number of uses.
	 */
	public static int getMaxUses()
	{
		return BetterStorageConfig.COMMON.cardboardBoxUses.get();
	}
}
