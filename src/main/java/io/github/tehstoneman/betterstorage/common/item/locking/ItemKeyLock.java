package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.List;
import java.util.UUID;

import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/** Common base class for locks and keys **/
public abstract class ItemKeyLock extends ItemBetterStorage
{

	public static final String	TAG_COLOR1		= "color1";
	public static final String	TAG_COLOR2		= "color2";
	public static final String	TAG_KEYLOCK_ID	= "keyid";

	public ItemKeyLock( String name )
	{
		super( name );
		setMaxStackSize( 1 );
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
		if( stack.hasTagCompound() )
		{
			final NBTTagCompound tag = stack.getTagCompound();
			if( tag.hasKey( TAG_COLOR1 ) )
				return tag.getInteger( TAG_COLOR1 );
		}
		return MapColor.GOLD.colorValue;
	}

	public static void setKeyColor1( ItemStack stack, int color )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if( tag == null )
			tag = new NBTTagCompound();

		tag.setInteger( TAG_COLOR1, color );
		stack.setTagCompound( tag );
	}

	public static int getKeyColor2( ItemStack stack )
	{
		if( stack.hasTagCompound() )
		{
			final NBTTagCompound tag = stack.getTagCompound();
			if( tag.hasKey( TAG_COLOR2 ) )
				return tag.getInteger( TAG_COLOR2 );
		}
		return getKeyColor1( stack );
	}

	public static void setKeyColor2( ItemStack stack, int fullColor )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if( tag == null )
			tag = new NBTTagCompound();

		tag.setInteger( TAG_COLOR2, fullColor );
		stack.setTagCompound( tag );
	}

	public static UUID getID( ItemStack stack )
	{
		if( !stack.hasTagCompound() || !stack.getTagCompound().hasUniqueId( TAG_KEYLOCK_ID ) )
			setID( stack, UUID.randomUUID() );

		return stack.getTagCompound().getUniqueId( TAG_KEYLOCK_ID );
	}

	public static void setID( ItemStack stack, UUID uuid )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if( tag == null )
			tag = new NBTTagCompound();

		tag.setUniqueId( TAG_KEYLOCK_ID, uuid );
		stack.setTagCompound( tag );
	}

	@SideOnly( Side.CLIENT )
	@SuppressWarnings( "unchecked" )
	@Override
	public void addInformation( ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag toolTipFlag )
	{
		final NBTTagCompound tag = stack.getTagCompound();
		if( tag != null )
		{
			/*
			 * if( tag.hasUniqueId( TAG_KEYLOCK_ID ) )
			 * tooltip.add( "Keytag : " + tag.getUniqueId( TAG_KEYLOCK_ID ) );
			 */
			if( tag.hasKey( TAG_COLOR1 ) )
				tooltip.add( "Color 1 : #" + Integer.toHexString( tag.getInteger( TAG_COLOR1 ) ).toUpperCase() );
			if( tag.hasKey( TAG_COLOR2 ) )
				tooltip.add( "Color 2 : #" + Integer.toHexString( tag.getInteger( TAG_COLOR2 ) ).toUpperCase() );
		}
	}
}
