package io.github.tehstoneman.betterstorage.api.lock;

import java.awt.Color;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/** Common base class for locks and keys **/
public abstract class KeyLockItem extends Item
{

	public static final String	TAG_COLOR1		= "color1";
	public static final String	TAG_COLOR2		= "color2";
	public static final String	TAG_KEYLOCK_ID	= "keyid";

	public KeyLockItem( Properties properties )
	{
		super( properties.maxStackSize( 1 ) );
	}

	@Override
	public int getItemEnchantability()
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
			final CompoundNBT tag = itemStack.getOrCreateTag();
			if( !tag.hasUniqueId( TAG_KEYLOCK_ID ) )
			{
				final UUID uuid = UUID.randomUUID();
				setID( itemStack, uuid );
				return uuid;
			}

			return tag.getUniqueId( TAG_KEYLOCK_ID );
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
			final CompoundNBT tag = itemStack.getOrCreateTag();
			tag.putUniqueId( TAG_KEYLOCK_ID, uuid );
			itemStack.setTag( tag );
		}
	}

	@Override
	public void onCreated( ItemStack stack, World worldIn, PlayerEntity playerIn )
	{
		if( !worldIn.isRemote )
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
			CompoundNBT tag = itemStack.getTag();
			if( tag == null )
				tag = new CompoundNBT();
			if( !tag.hasUniqueId( TAG_KEYLOCK_ID ) )
			{
				tag.putUniqueId( TAG_KEYLOCK_ID, UUID.randomUUID() );
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
				final CompoundNBT tag = itemStack.getTag();
				if( tag.contains( TAG_COLOR1 ) )
					return tag.getInt( TAG_COLOR1 );
			}
			return MaterialColor.GOLD.colorValue;
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
			CompoundNBT tag = itemStack.getTag();
			if( tag == null )
				tag = new CompoundNBT();

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
			final CompoundNBT tag = itemStack.getTag();
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
			CompoundNBT tag = itemStack.getTag();
			if( tag == null )
				tag = new CompoundNBT();

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
			final CompoundNBT tag = itemStack.getTag();
			tag.remove( TAG_COLOR1 );
			tag.remove( TAG_COLOR2 );
			itemStack.setTag( tag );
		}
	}

	@Override
	public void addInformation( ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn )
	{
		super.addInformation( stack, worldIn, tooltip, flagIn );
		if( flagIn.isAdvanced() && stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag.hasUniqueId( TAG_KEYLOCK_ID ) )
				tooltip.add( new TranslationTextComponent( "Keytag : " + tag.getUniqueId( TAG_KEYLOCK_ID ) ) );
			if( tag.contains( TAG_COLOR1 ) )
				tooltip.add( new TranslationTextComponent( "Color 1 : #" + Integer.toHexString( tag.getInt( TAG_COLOR1 ) ).toUpperCase() ) );
			if( tag.contains( TAG_COLOR2 ) )
				tooltip.add( new TranslationTextComponent( "Color 2 : #" + Integer.toHexString( tag.getInt( TAG_COLOR2 ) ).toUpperCase() ) );
		}
	}
}
