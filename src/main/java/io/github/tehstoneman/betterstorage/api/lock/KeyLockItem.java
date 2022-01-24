package io.github.tehstoneman.betterstorage.api.lock;

import java.awt.Color;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.MaterialColor;

/** Common base class for locks and keys **/
public abstract class KeyLockItem extends Item
{

	public static final String	TAG_COLOR1		= "color1";
	public static final String	TAG_COLOR2		= "color2";
	public static final String	TAG_KEYLOCK_ID	= "keyid";

	public KeyLockItem( Properties properties )
	{
		super( properties.stacksTo( 1 ) );
	}

	@Override
	public int getEnchantmentValue()
	{
		return 20;
	}

	/**
	 * Get the {@link UUID} of this lock/key
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to get the ID for.
	 * @return The {@link UUID} of this item or null if not a lock or key.
	 */
	@Nullable
	public UUID getID( ItemStack itemStack )
	{
		if( itemStack.getItem() instanceof KeyLockItem )
		{
			final CompoundTag tag = itemStack.getOrCreateTag();
			if( !tag.hasUUID( TAG_KEYLOCK_ID ) )
			{
				final UUID uuid = UUID.randomUUID();
				setID( itemStack, uuid );
				return uuid;
			}

			return tag.getUUID( TAG_KEYLOCK_ID );
		}
		return null;
	}

	/**
	 * Sets the {@link UUID} of this lock/key
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to set the ID of.
	 * @param uuid
	 *            The {@link UUID} to set the ID to.
	 */
	public void setID( ItemStack itemStack, UUID uuid )
	{
		if( itemStack.getItem() instanceof KeyLockItem )
		{
			final CompoundTag tag = itemStack.getOrCreateTag();
			tag.putUUID( TAG_KEYLOCK_ID, uuid );
			itemStack.setTag( tag );
		}
	}

	@Override
	public void onCraftedBy( ItemStack stack, Level worldIn, Player playerIn )
	{
		if( !worldIn.isClientSide )
			ensureHasID( stack );
	}

	/**
	 * Gives the key/lock a random ID if it doesn't have one already.
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to check.
	 */
	protected void ensureHasID( ItemStack itemStack )
	{
		if( itemStack.getItem() instanceof KeyLockItem )
		{
			CompoundTag tag = itemStack.getTag();
			if( tag == null )
				tag = new CompoundTag();
			if( !tag.hasUUID( TAG_KEYLOCK_ID ) )
			{
				tag.putUUID( TAG_KEYLOCK_ID, UUID.randomUUID() );
				itemStack.setTag( tag );
			}
		}
	}

	/**
	 * Get the first of two colors.
	 *
	 * @param itemStack
	 *            {@link ItemStack} to get the color for.
	 * @return The color represented as an int.
	 */
	public static int getKeyColor1( ItemStack itemStack )
	{
		if( itemStack.getItem() instanceof KeyLockItem )
		{
			if( itemStack.hasTag() )
			{
				final CompoundTag tag = itemStack.getTag();
				if( tag.contains( TAG_COLOR1 ) )
					return tag.getInt( TAG_COLOR1 );
			}
			return MaterialColor.GOLD.col;
		}
		return Color.WHITE.getRGB();
	}

	/**
	 * Set the first of two colors.
	 *
	 * @param itemStack
	 *            {@link ItemStack} to set the color for.
	 * @param color
	 *            The color represented as an int.
	 */
	public static void setKeyColor1( ItemStack itemStack, int color )
	{
		if( itemStack.getItem() instanceof KeyLockItem )
		{
			CompoundTag tag = itemStack.getTag();
			if( tag == null )
				tag = new CompoundTag();

			tag.putInt( TAG_COLOR1, color );
			itemStack.setTag( tag );
		}
	}

	/**
	 * Check for the first of two colors.
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to check.
	 * @return True if the first color is present.
	 */
	public static boolean hasKeyColor1( ItemStack itemStack )
	{
		return itemStack.getItem() instanceof KeyLockItem && itemStack.hasTag() && itemStack.getTag().contains( TAG_COLOR1 );
	}

	/**
	 * Get the second of two colors.
	 *
	 * @param itemStack
	 *            {@link ItemStack} to get the color for.
	 * @return The color represented as an int.
	 */
	public static int getKeyColor2( ItemStack itemStack )
	{
		if( itemStack.getItem() instanceof KeyLockItem && itemStack.hasTag() )
		{
			final CompoundTag tag = itemStack.getTag();
			if( tag.contains( TAG_COLOR2 ) )
				return tag.getInt( TAG_COLOR2 );
		}
		return getKeyColor1( itemStack );
	}

	/**
	 * Set the second of two colors.
	 *
	 * @param itemStack
	 *            {@link ItemStack} to set the color for.
	 * @param color
	 *            The color represented as an int.
	 */
	public static void setKeyColor2( ItemStack itemStack, int color )
	{
		if( itemStack.getItem() instanceof KeyLockItem )
		{
			CompoundTag tag = itemStack.getTag();
			if( tag == null )
				tag = new CompoundTag();

			tag.putInt( TAG_COLOR2, color );
			itemStack.setTag( tag );
		}
	}

	/**
	 * Check for the second of two colors.
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to check.
	 * @return True if the first color is present.
	 */
	public static boolean hasKeyColor2( ItemStack itemStack )
	{
		return itemStack.getItem() instanceof KeyLockItem && itemStack.hasTag() && itemStack.getTag().contains( TAG_COLOR2 );
	}

	/**
	 * Clear the colors of a dyed key/lock
	 *
	 * @param itemStack
	 *            The {@link ItemStack} to clear.
	 */
	public static void clearColors( ItemStack itemStack )
	{
		if( itemStack.hasTag() )
		{
			final CompoundTag tag = itemStack.getTag();
			tag.remove( TAG_COLOR1 );
			tag.remove( TAG_COLOR2 );
			itemStack.setTag( tag );
		}
	}

	@Override
	public void appendHoverText( ItemStack stack, @Nullable Level worldIn, List< Component > tooltip, TooltipFlag flagIn )
	{
		super.appendHoverText( stack, worldIn, tooltip, flagIn );
		if( flagIn.isAdvanced() && stack.hasTag() )
		{
			final CompoundTag tag = stack.getTag();
			if( tag.hasUUID( TAG_KEYLOCK_ID ) )
				tooltip.add( new TranslatableComponent( "Keytag : " + tag.getUUID( TAG_KEYLOCK_ID ) ) );
			if( tag.contains( TAG_COLOR1 ) )
				tooltip.add( new TranslatableComponent( "Color 1 : #" + Integer.toHexString( tag.getInt( TAG_COLOR1 ) ).toUpperCase() ) );
			if( tag.contains( TAG_COLOR2 ) )
				tooltip.add( new TranslatableComponent( "Color 2 : #" + Integer.toHexString( tag.getInt( TAG_COLOR2 ) ).toUpperCase() ) );
		}
	}
}
