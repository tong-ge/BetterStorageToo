package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.api.BetterStorageEnchantment;
import io.github.tehstoneman.betterstorage.api.lock.EnumLockInteraction;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.api.lock.ILockable;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockable;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockDoor.EnumDoorHalf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemLock extends ItemKeyLock implements ILock
{
	public ItemLock()
	{
		super( "lock" );
		setMaxDamage( 64 );
		setMaxStackSize( 1 );
	}

	@Override
	public boolean isRepairable()
	{
		return true;
	}

	@Override
	public boolean getIsRepairable( ItemStack stack, ItemStack material )
	{
		return material.getItem() == Items.GOLD_INGOT;
	}

	@Override
	public boolean isDamageable()
	{
		return true;
	}

	@Override
	public int getItemEnchantability()
	{
		return 20;
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
	public EnumActionResult onItemUse( EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
			float hitZ )
	{
		if( hand == EnumHand.MAIN_HAND )
		{
			final ItemStack stack = playerIn.getHeldItem( hand );
			IBlockState blockState = worldIn.getBlockState( pos );
			Block block = blockState.getBlock();

			if( block == Blocks.IRON_DOOR )
			{
				if( blockState.getValue( BlockDoor.HALF ) == EnumDoorHalf.UPPER )
				{
					pos = pos.down();
					blockState = worldIn.getBlockState( pos );
					block = blockState.getBlock();
				}

				//@formatter:off
				worldIn.setBlockState( pos, BetterStorageBlocks.LOCKABLE_DOOR.getDefaultState()
						.withProperty( BlockDoor.FACING, blockState.getValue( BlockDoor.FACING ) )
						.withProperty( BlockDoor.OPEN, blockState.getValue( BlockDoor.OPEN ) )
						.withProperty( BlockDoor.HINGE, blockState.getValue( BlockDoor.HINGE ) )
						.withProperty( BlockDoor.HALF, EnumDoorHalf.LOWER ) );
				worldIn.setBlockState( pos.up(), BetterStorageBlocks.LOCKABLE_DOOR.getDefaultState()
						.withProperty( BlockDoor.FACING, blockState.getValue( BlockDoor.FACING ) )
						.withProperty( BlockDoor.OPEN, blockState.getValue( BlockDoor.OPEN ) )
						.withProperty( BlockDoor.HINGE, blockState.getValue( BlockDoor.HINGE ) )
						.withProperty( BlockDoor.HALF, EnumDoorHalf.UPPER ) );
				//@formatter:on

				final TileEntity tileEntity = worldIn.getTileEntity( pos );
				if( tileEntity instanceof TileEntityLockableDoor )
				{
					final TileEntityLockableDoor lockable = (TileEntityLockableDoor)tileEntity;
					if( lockable.isLockValid( stack ) )
					{
						lockable.setLock( stack.copy() );
						if( !playerIn.isCreative() )
							playerIn.setHeldItem( hand, ItemStack.EMPTY );
						return EnumActionResult.SUCCESS;
					}
				}
			}

			final TileEntity tileEntity = worldIn.getTileEntity( pos );
			if( tileEntity instanceof TileEntityLockable )
			{
				final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
				if( lockable.isLockValid( stack ) )
				{
					lockable.setLock( stack.copy() );
					if( !playerIn.isCreative() )
						playerIn.setHeldItem( hand, ItemStack.EMPTY );
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return super.onItemUse( playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
	}

	/**
	 * Gives the lock a random ID if it doesn't have one. <br>
	 * This is usually only when the lock is taken out of creative.
	 */
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

	// ILock implementation

	@Override
	public String getLockType()
	{
		return "normal";
	}

	@Override
	public void onUnlock( ItemStack lock, ItemStack key, ILockable lockable, EntityPlayer player, boolean success )
	{
		if( success )
			return;
		// Power is 2 when a key was used to open the lock, 1 otherwise.
		final EnumLockInteraction interaction = key != null ? EnumLockInteraction.PICK : EnumLockInteraction.OPEN;
		applyEffects( lock, lockable, player, interaction );
	}

	@Override
	public void applyEffects( ItemStack lock, ILockable lockable, EntityPlayer player, EnumLockInteraction interaction )
	{

		final int shock = BetterStorageEnchantment.getLevel( lock, "shock" );
		final int trigger = BetterStorageEnchantment.getLevel( lock, "trigger" );

		if( shock > 0 )
		{
			final boolean open = interaction == EnumLockInteraction.OPEN;
			final boolean pick = interaction == EnumLockInteraction.PICK;
			int damage = shock;
			if( pick )
				damage *= 3;
			player.attackEntityFrom( DamageSource.MAGIC, damage );
			if( shock >= 3 && !open )
				player.setFire( 3 );
		}

		if( trigger > 0 )
			lockable.applyTrigger();

	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return true;
	}
}
