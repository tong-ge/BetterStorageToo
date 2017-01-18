package io.github.tehstoneman.betterstorage.item;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.misc.Constants;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class ItemBetterStorage extends Item
{

	public static final String	TAG_COLOR1		= "color1";
	public static final String	TAG_COLOR2		= "color2";
	public static final String	TAG_KEYLOCK_ID	= "keyid";

	private String				name;

	public ItemBetterStorage()
	{
		setMaxStackSize( 1 );
		setCreativeTab( BetterStorage.creativeTab );

		setUnlocalizedName( Constants.modId + "." + getItemName() );
		setRegistryName( getItemName() );
		GameRegistry.register( this );
	}

	/** Returns the name of this item, for example "drinkingHelmet". */

	public String getItemName()
	{
		return name != null ? name : ( name = MiscUtils.getName( this ) );
	}

	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( this, 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
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
}
