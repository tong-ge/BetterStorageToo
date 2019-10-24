package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

/** Common base class for locks and keys **/
public abstract class ItemKeyLock extends ItemBetterStorage
{

	public static final String	TAG_COLOR1		= "color1";
	public static final String	TAG_COLOR2		= "color2";
	public static final String	TAG_KEYLOCK_ID	= "keyid";

	public ItemKeyLock( String name )
	{
		super( name, new Item.Properties().group( BetterStorage.ITEM_GROUP ).maxStackSize( 1 ) );
	}

	@Override
	public int getItemEnchantability()
	{
		return 20;
	}

	// NBT helper functions
	// Only used by keys and locks currently.
	/*
	 * public static int getKeyColor1( ItemStack stack )
	 * {
	 * if( stack.hasTagCompound() )
	 * {
	 * final NBTTagCompound tag = stack.getTagCompound();
	 * if( tag.hasKey( TAG_COLOR1 ) )
	 * return tag.getInteger( TAG_COLOR1 );
	 * }
	 * return MapColor.GOLD.colorValue;
	 * }
	 */

	/*
	 * public static void setKeyColor1( ItemStack stack, int color )
	 * {
	 * NBTTagCompound tag = stack.getTagCompound();
	 * if( tag == null )
	 * tag = new NBTTagCompound();
	 *
	 * tag.setInteger( TAG_COLOR1, color );
	 * stack.setTagCompound( tag );
	 * }
	 */

	/*
	 * public static boolean hasKeyColor1( ItemStack stack )
	 * {
	 * return stack.hasTagCompound() && stack.getTagCompound().hasKey( TAG_COLOR1 );
	 * }
	 */

	/*
	 * public static int getKeyColor2( ItemStack stack )
	 * {
	 * if( stack.hasTagCompound() )
	 * {
	 * final NBTTagCompound tag = stack.getTagCompound();
	 * if( tag.hasKey( TAG_COLOR2 ) )
	 * return tag.getInteger( TAG_COLOR2 );
	 * }
	 * return getKeyColor1( stack );
	 * }
	 */

	/*
	 * public static void setKeyColor2( ItemStack stack, int fullColor )
	 * {
	 * NBTTagCompound tag = stack.getTagCompound();
	 * if( tag == null )
	 * tag = new NBTTagCompound();
	 *
	 * tag.setInteger( TAG_COLOR2, fullColor );
	 * stack.setTagCompound( tag );
	 * }
	 */

	/*
	 * public static boolean hasKeyColor2( ItemStack stack )
	 * {
	 * return stack.hasTagCompound() && stack.getTagCompound().hasKey( TAG_COLOR2 );
	 * }
	 */

	/*
	 * public static void clearColors( ItemStack stack )
	 * {
	 * if( stack.hasTagCompound() )
	 * {
	 * NBTTagCompound tag = stack.getTagCompound();
	 * tag.removeTag( TAG_COLOR1 );
	 * tag.removeTag( TAG_COLOR2 );
	 * stack.setTagCompound( tag );
	 * }
	 * }
	 */

	public static UUID getID( ItemStack stack )
	{
		final CompoundNBT tag = stack.getChildTag( TAG_KEYLOCK_ID );
		if( tag == null )
			setID( stack, UUID.randomUUID() );

		return tag.getUniqueId( TAG_KEYLOCK_ID );
	}

	public static void setID( ItemStack stack, UUID uuid )
	{
		final CompoundNBT tag = stack.getOrCreateTag();
		tag.putUniqueId( TAG_KEYLOCK_ID, uuid );
		stack.setTag( tag );
	}

	@Override
	public void addInformation( ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn )
	{
		super.addInformation( stack, worldIn, tooltip, flagIn );
		if( flagIn.isAdvanced() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag != null )
			{
				if( tag.hasUniqueId( TAG_KEYLOCK_ID ) )
					tooltip.add( new TranslationTextComponent( "Keytag : " + tag.getUniqueId( TAG_KEYLOCK_ID ) ) );

				if( tag.contains( TAG_COLOR1 ) )
					tooltip.add( new TranslationTextComponent( "Color 1 : #" + Integer.toHexString( tag.getInt( TAG_COLOR1 ) ).toUpperCase() ) );
				if( tag.contains( TAG_COLOR2 ) )
					tooltip.add( new TranslationTextComponent( "Color 2 : #" + Integer.toHexString( tag.getInt( TAG_COLOR2 ) ).toUpperCase() ) );
			}
		}
	}

}
