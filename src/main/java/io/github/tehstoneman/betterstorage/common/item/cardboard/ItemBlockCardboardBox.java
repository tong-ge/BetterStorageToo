package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.api.IContainerItem;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockBetterStorage;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.ITextComponent;

public class ItemBlockCardboardBox extends ItemBlockBetterStorage implements IContainerItem, ICardboardItem
{
	public ItemBlockCardboardBox( Block block )
	{
		super( block );
		// setMaxStackSize( 1 );
	}

	// Item stuff

	/*
	 * @Override
	 * public boolean showDurabilityBar( ItemStack stack )
	 * {
	 * final int maxUses = getUses();
	 * if( maxUses > 1 )
	 * {
	 * final int uses = maxUses;
	 * if( stack.hasTagCompound() && stack.getTagCompound().hasKey( "uses" ) )
	 * uses = Math.min( maxUses, stack.getTagCompound().getInteger( "uses" ) );
	 * return uses < maxUses;
	 * }
	 * return false;
	 * }
	 */

	/*
	 * @Override
	 * public double getDurabilityForDisplay( ItemStack stack )
	 * {
	 * final int maxUses = getUses();
	 * return 1 - (float)StackUtils.get( stack, maxUses, "uses" ) / maxUses;
	 * }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void addInformation( ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag toolTipFlag )
	 * {
	 * final int maxUses = getUses();
	 * final boolean hasItems = stack.hasTagCompound() && stack.getTagCompound().hasKey( "Inventory" );
	 *
	 * if( !hasItems && BetterStorage.config.enableHelpTooltips )
	 * tooltip.add( BetterStorage.proxy.localize( "tooltip.betterstorage.cardboardBox.useHint" + ( maxUses > 0 ? ".reusable" : 0 ) ) );
	 *
	 * if( maxUses > 1 )
	 * {
	 * int uses = maxUses;
	 * if( stack.hasTagCompound() && stack.getTagCompound().hasKey( "uses" ) )
	 * uses = Math.min( maxUses, stack.getTagCompound().getInteger( "uses" ) );
	 * tooltip.add( TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC
	 * + BetterStorage.proxy.localize( "tooltip.betterstorage.cardboardBox.uses", uses ) );
	 * }
	 *
	 * if( !hasItems )
	 * return;
	 * if( !BetterStorage.config.cardboardBoxShowContents )
	 * {
	 * tooltip.add( BetterStorage.proxy.localize( "tooltip.betterstorage.cardboardBox.containsItems" ) );
	 * return;
	 * }
	 *
	 * final ItemStackHandler contents = new ItemStackHandler( 9 );
	 * contents.deserializeNBT( stack.getTagCompound().getCompoundTag( "Inventory" ) );
	 *
	 * final int limit = toolTipFlag.isAdvanced() || GuiScreen.isShiftKeyDown() ? 6 : 3;
	 *
	 * final List< DisplayNameStack > items = new ArrayList<>();
	 *
	 * for( int i = 0; i < contents.getSlots(); i++ )
	 * {
	 * final ItemStack contentStack = contents.getStackInSlot( i );
	 * if( !contentStack.isEmpty() )
	 * {
	 * boolean added = false;
	 * if( items.size() > 0 )
	 * for( int j = 0; j < items.size(); j++ )
	 * if( items.get( j ).matchAndAdd( contentStack ) )
	 * {
	 * added = true;
	 * break;
	 * }
	 * if( !added )
	 * items.add( new DisplayNameStack( contentStack ) );
	 * }
	 * }
	 *
	 * Collections.sort( items );
	 * for( int i = 0; i < items.size() && i < limit; i++ )
	 * tooltip.add( items.get( i ).toString() );
	 *
	 * if( items.size() <= limit )
	 * return;
	 *
	 * int count = 0;
	 * for( int i = limit; i < items.size(); i++ )
	 * count += items.get( i ).stackSize;
	 * tooltip.add( BetterStorage.proxy.localize( "tooltip.betterstorage.cardboardBox.plusMore", count ) );
	 * }
	 */

	// IContainerItem implementation

	/*
	 * @Override
	 * public ItemStack[] getContainerItemContents( ItemStack container )
	 * {
	 * if( StackUtils.has( container, "Items" ) )
	 * return StackUtils.getStackContents( container, getRows() );
	 * else
	 * return null;
	 * }
	 */

	/*
	 * @Override
	 * public boolean canBeStoredInContainerItem( ItemStack item )
	 * {
	 * return !StackUtils.has( item, "Items" );
	 * }
	 */

	// Helper functions

	/** Returns the amount of rows in a cardboard box. */
	/*public static int getRows()
	{
		return BetterStorage.config.cardboardBoxRows;
	}*/

	/** Returns how many times cardboard boxes can be reused. */
	/*public static int getUses()
	{
		return BetterStorage.config.cardboardBoxUses;
	}*/

	// Cardboard items
	@Override
	public boolean canDye( ItemStack stack )
	{
		return true;
	}

	@Override
	public int getColor( ItemStack itemstack )
	{
		/*
		 * if( hasColor( itemstack ) )
		 * {
		 * final NBTTagCompound compound = itemstack.getTagCompound();
		 * return compound.getInteger( "color" );
		 * }
		 */
		return 0x705030;
	}

	@Override
	public boolean hasColor( ItemStack itemstack )
	{
		/*
		 * if( itemstack.hasTagCompound() )
		 * {
		 * final NBTTagCompound compound = itemstack.getTagCompound();
		 * return compound.hasKey( "color" );
		 * }
		 */
		return false;
	}

	@Override
	public void setColor( ItemStack itemstack, int colorRGB )
	{
		final NBTTagCompound compound;
		/*
		 * if( itemstack.hasTagCompound() )
		 * compound = itemstack.getTagCompound();
		 * else
		 * compound = new NBTTagCompound();
		 * compound.setInteger( "color", colorRGB );
		 * itemstack.setTagCompound( compound );
		 */
	}

	private static class DisplayNameStack implements Comparable< DisplayNameStack >
	{
		public final ITextComponent	name;
		public int					stackSize;

		public DisplayNameStack( ItemStack stack )
		{
			name = stack.getItem().getDisplayName( stack );
			stackSize = stack.getCount();
		}

		public boolean matchAndAdd( ItemStack stack )
		{
			if( name.equals( stack.getItem().getDisplayName( stack ) ) )
			{
				stackSize += stack.getCount();
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

	@Override
	public ItemStack[] getContainerItemContents( ItemStack container )
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean canBeStoredInContainerItem( ItemStack item )
	{
		// TODO Auto-generated method stub
		return false;
	}
}
