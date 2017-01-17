package io.github.tehstoneman.betterstorage.item.locking;

import java.util.List;
import java.util.UUID;

import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.item.ItemBetterStorage;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKey extends ItemBetterStorage implements IKey
{
	public ItemKey()
	{
		setMaxDamage( 0 );
		setHasSubtypes( true );
		setMaxStackSize( 1 );
	}

	@Override
	public int getMetadata( int damage )
	{
		return damage;
	}

	@Override
	public int getItemEnchantability()
	{
		return 20;
	}

	@Override
	public ItemStack getContainerItem( ItemStack stack )
	{
		return stack;
	}

	@Override
	public void onCreated( ItemStack stack, World world, EntityPlayer player )
	{
		if( !world.isRemote )
			ensureHasID( stack );
	}

	@Override
	public void onUpdate( ItemStack stack, World world, Entity entity, int slot, boolean isBeingHeld )
	{
		if( !world.isRemote )
			ensureHasID( stack );
	}

	/** Gives the key a random ID if it doesn't have one already. */
	public static void ensureHasID( ItemStack stack )
	{
		NBTTagCompound tag = stack.getTagCompound();
		if( tag == null )
			tag = new NBTTagCompound();
		if( !tag.hasUniqueId( TAG_KEYLOCK_ID ) )
		{
			tag.setUniqueId( TAG_KEYLOCK_ID, UUID.randomUUID() );
			stack.setTagCompound( tag );
		}
	}

	// IKey implementation
	@Override
	public boolean isNormalKey()
	{
		return true;
	}

	@Override
	public boolean unlock( ItemStack key, ItemStack lock, boolean useAbility )
	{

		final ILock lockType = (ILock)lock.getItem();
		// If the lock type isn't normal, the key can't unlock it.
		if( lockType.getLockType() != "normal" )
			return false;

		final UUID lockId = getID( lock );
		final UUID keyId = getID( key );

		// If the lock and key IDs match, return true.
		if( lockId == keyId )
			return true;

		final int lockSecurity = BetterStorageEnchantment.getLevel( lock, "security" );
		final int unlocking = BetterStorageEnchantment.getLevel( key, "unlocking" );
		final int lockpicking = BetterStorageEnchantment.getLevel( key, "lockpicking" );
		final int morphing = BetterStorageEnchantment.getLevel( key, "morphing" );

		final int effectiveUnlocking = Math.max( 0, unlocking - lockSecurity );
		final int effectiveLockpicking = Math.max( 0, lockpicking - lockSecurity );
		final int effectiveMorphing = Math.max( 0, morphing - lockSecurity );

		/*
		 * if( effectiveUnlocking > 0 )
		 * {
		 * final int div = (int)Math.pow( 2, 10 + effectiveUnlocking * 1 );
		 * if( lockId / div == keyId / div )
		 * return true;
		 * }
		 */
		if( useAbility && effectiveLockpicking > 0 )
		{
			final NBTTagList list = key.getEnchantmentTagList();
			for( int i = 0; i < list.tagCount(); i++ )
			{
				final NBTTagCompound compound = list.getCompoundTagAt( i );
				if( compound.getShort( "id" ) != Enchantment.getEnchantmentID( BetterStorageEnchantment.get( "lockpicking" ) ) )
					continue;
				final int level = compound.getShort( "lvl" ) - 1;
				if( level == 0 )
				{
					list.removeTag( i );
					if( list.tagCount() == 0 )
						key.getTagCompound().removeTag( "ench" );
				}
				else
					compound.setShort( "lvl", (short)level );
				break;
			}
			return true;
		}
		/*
		 * if( useAbility && effectiveMorphing > 0 )
		 * {
		 * key.setItemDamage( lockId );
		 * final NBTTagList list = key.getEnchantmentTagList();
		 * for( int i = 0; i < list.tagCount(); i++ )
		 * {
		 * final NBTTagCompound compound = list.getCompoundTagAt( i );
		 * if( compound.getShort( "id" ) != Enchantment.getEnchantmentID( BetterStorageEnchantment.get( "morphing" ) ) )
		 * continue;
		 * list.removeTag( i );
		 * // Morphed keys keep their enchanted look, it looks sweet.
		 * // if (list.tagCount() == 0)
		 * // key.getTagCompound().removeTag("ench");
		 * }
		 * return true;
		 * }
		 */

		return false;

	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return true;
	}

	@SideOnly( Side.CLIENT )
	@SuppressWarnings( "unchecked" )
	@Override
	public void addInformation( ItemStack stack, EntityPlayer playerin, List tooltip, boolean advanced )
	{
		final NBTTagCompound tag = stack.getTagCompound();
		if( tag == null )
			tooltip.add( "Keytag not set" );
		else
			if( tag.hasUniqueId( TAG_KEYLOCK_ID ) )
				tooltip.add( "Keytag : " + tag.getUniqueId( TAG_KEYLOCK_ID ) );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( this, 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
		ModelLoader.setCustomModelResourceLocation( this, 1, new ModelResourceLocation( getRegistryName() + "_with_color", "inventory" ) );
		ModelLoader.setCustomModelResourceLocation( this, 2, new ModelResourceLocation( getRegistryName() + "_with_overlay", "inventory" ) );
	}
}
