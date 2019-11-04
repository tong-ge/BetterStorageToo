package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.block.material.MaterialColor;
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
		super( name, new Item.Properties().maxStackSize( 1 ) );
	}

	@Override
	public int getItemEnchantability()
	{
		return 20;
	}

	// NBT helper functions
	// Only used by keys and locks currently.
	public static int getKeyColor1( ItemStack stack )
	{
		if( stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag.contains( TAG_COLOR1 ) )
				return tag.getInt( TAG_COLOR1 );
		}
		return MaterialColor.GOLD.colorValue;
	}

	public static void setKeyColor1( ItemStack stack, int color )
	{
		CompoundNBT tag = stack.getTag();
		if( tag == null )
			tag = new CompoundNBT();

		tag.putInt( TAG_COLOR1, color );
		stack.setTag( tag );
	}

	public static boolean hasKeyColor1( ItemStack stack )
	{
		return stack.hasTag() && stack.getTag().contains( TAG_COLOR1 );
	}

	public static int getKeyColor2( ItemStack stack )
	{
		if( stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag.contains( TAG_COLOR2 ) )
				return tag.getInt( TAG_COLOR2 );
		}
		return getKeyColor1( stack );
	}

	public static void setKeyColor2( ItemStack stack, int fullColor )
	{
		CompoundNBT tag = stack.getTag();
		if( tag == null )
			tag = new CompoundNBT();

		tag.putInt( TAG_COLOR2, fullColor );
		stack.setTag( tag );
	}

	public static boolean hasKeyColor2( ItemStack stack )
	{
		return stack.hasTag() && stack.getTag().contains( TAG_COLOR2 );
	}

	public static void clearColors( ItemStack stack )
	{
		if( stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			tag.remove( TAG_COLOR1 );
			tag.remove( TAG_COLOR2 );
			stack.setTag( tag );
		}
	}

	public static UUID getID( ItemStack stack )
	{
		final CompoundNBT tag = stack.getOrCreateTag();
		if( !tag.hasUniqueId( TAG_KEYLOCK_ID ) )
		{
			final UUID uuid = UUID.randomUUID();
			setID( stack, uuid );
			return uuid;
		}

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
