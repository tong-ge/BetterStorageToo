package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.UUID;
import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.ILockable;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockable;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemKey extends ItemKeyLock implements IKey
{
	public ItemKey()
	{
		this( "key" );
	}

	public ItemKey( String name )
	{
		super( name );

		setMaxDamage( 0 );
		setMaxStackSize( 1 );
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

	@Override
	public EnumActionResult onItemUse( EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ )
	{
		if( !worldIn.isRemote && hand == EnumHand.MAIN_HAND )
		{
			ItemStack stack = playerIn.getHeldItem( hand );
			TileEntity tileEntity = worldIn.getTileEntity( pos );
			if( tileEntity == null )
			{
				IBlockState state = worldIn.getBlockState( pos );
				if( state.getValue( BlockDoor.HALF ) == EnumDoorHalf.UPPER)
				{
					pos = pos.down();
					tileEntity = worldIn.getTileEntity( pos );
				}
			}
			
			if( tileEntity instanceof ILockable )
			{
				final ILockable lockable = (ILockable)tileEntity;
				if( unlock( stack, lockable.getLock(), false ) )
				{
					if( playerIn.isSneaking() )
					{
						worldIn.spawnEntity( new EntityItem( worldIn, pos.getX(), pos.getY(), pos.getZ(), lockable.getLock().copy() ) );
						lockable.setLock( ItemStack.EMPTY );
					}
					else
						lockable.useUnlocked( playerIn );
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return super.onItemUse(playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
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
		if( lockId.equals( keyId ) )
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
}
