package io.github.tehstoneman.betterstorage.common.item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.IContainerItem;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.utils.LanguageUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.block.Block;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.ItemStackHandler;

public class ItemBlockCardboardBox extends ItemBlock implements IContainerItem
{
	public ItemBlockCardboardBox( Block block )
	{
		super( block );
		setMaxStackSize( 1 );
	}

	// Item stuff

	@Override
	public boolean showDurabilityBar( ItemStack stack )
	{
		final int maxUses = getUses();
		return maxUses > 1 && StackUtils.get( stack, maxUses, "uses" ) < maxUses;
	}

	@Override
	public double getDurabilityForDisplay( ItemStack stack )
	{
		final int maxUses = getUses();
		return 1 - (float)StackUtils.get( stack, maxUses, "uses" ) / maxUses;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips )
	{
		final int maxUses = getUses();
		final boolean hasItems = stack.hasTagCompound() && stack.getTagCompound().hasKey( "Inventory" );

		if( !hasItems && BetterStorage.globalConfig.getBoolean( GlobalConfig.enableHelpTooltips ) )
			list.add( LanguageUtils.translateTooltip( "cardboardBox.useHint" + ( maxUses > 0 ? ".reusable" : 0 ) ) );

		if( maxUses > 1 )
		{
			int uses = maxUses;
			if( stack.hasTagCompound() && stack.getTagCompound().hasKey( "uses" ) )
				uses = Math.min( maxUses, stack.getTagCompound().getInteger( "uses" ) );
			list.add( TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC
					+ BetterStorage.proxy.localize( "tooltip.betterstorage.cardboardBox.uses", uses ) );
		}

		if( !hasItems )
			return;
		if( !BetterStorage.globalConfig.getBoolean( GlobalConfig.cardboardBoxShowContents ) )
		{
			list.add( BetterStorage.proxy.localize( "tooltip.betterstorage.cardboardBox.containsItems" ) );
			return;
		}

		final ItemStackHandler contents = new ItemStackHandler( 9 );
		contents.deserializeNBT( stack.getTagCompound().getCompoundTag( "Inventory" ) );

		final int limit = advancedTooltips || GuiScreen.isShiftKeyDown() ? 6 : 3;

		final List< DisplayNameStack > items = new ArrayList<>();

		for( int i = 0; i < contents.getSlots(); i++ )
		{
			final ItemStack contentStack = contents.getStackInSlot( i );
			if( contentStack != null )
			{
				boolean added = false;
				if( items.size() > 0 )
					for( int j = 0; j < items.size(); j++ )
						if( items.get( j ).matchAndAdd( contentStack ) )
						{
							added = true;
							break;
						}
				if( !added )
					items.add( new DisplayNameStack( contentStack ) );
			}
		}

		Collections.sort( items );
		for( int i = 0; i < items.size() && i < limit; i++ )
			list.add( items.get( i ).toString() );

		if( items.size() <= limit )
			return;

		int count = 0;
		for( int i = limit; i < items.size(); i++ )
			count += items.get( i ).stackSize;
		list.add( count + " more item" + ( count > 1 ? "s" : "" ) + " ..." );
	}

	// IContainerItem implementation

	@Override
	public ItemStack[] getContainerItemContents( ItemStack container )
	{
		if( StackUtils.has( container, "Items" ) )
			return StackUtils.getStackContents( container, getRows() );
		else
			return null;
	}

	@Override
	public boolean canBeStoredInContainerItem( ItemStack item )
	{
		return !StackUtils.has( item, "Items" );
	}

	// Helper functions

	/** Returns the amount of rows in a cardboard box. */
	public static int getRows()
	{
		return BetterStorage.globalConfig.getInteger( GlobalConfig.cardboardBoxRows );
	}

	/** Returns how many times cardboard boxes can be reused. */
	public static int getUses()
	{
		return BetterStorage.globalConfig.getInteger( GlobalConfig.cardboardBoxUses );
	}

	private static class DisplayNameStack implements Comparable< DisplayNameStack >
	{
		public final String	name;
		public int			stackSize;

		public DisplayNameStack( ItemStack stack )
		{
			name = stack.getItem().getItemStackDisplayName( stack );
			stackSize = stack.stackSize;
		}

		public boolean matchAndAdd( ItemStack stack )
		{
			if( name.equals( stack.getItem().getItemStackDisplayName( stack ) ) )
			{
				stackSize += stack.stackSize;
				return true;
			}
			else
				return false;
		}

		@Override
		public String toString()
		{
			return stackSize + "x " + name;
		}

		@Override
		public int compareTo( DisplayNameStack other )
		{
			return other.stackSize - stackSize;
		}
	}
}
