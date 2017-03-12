package io.github.tehstoneman.betterstorage.common.item.locking;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.IContainerItem;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import io.github.tehstoneman.betterstorage.common.inventory.InventoryKeyring;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockable;
import io.github.tehstoneman.betterstorage.utils.NbtUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;

public class ItemKeyring extends Item implements IKey, IContainerItem
{
	public ItemKeyring()
	{
		setMaxStackSize( 1 );
	}

	@Override
	public ActionResult< ItemStack > onItemRightClick( ItemStack stack, World world, EntityPlayer player, EnumHand hand )
	{
		if( !player.isSneaking() )
			return new ActionResult( EnumActionResult.PASS, stack );

		final String title = StackUtils.get( stack, "", "display", "Name" );
		final int protectedSlot = player.inventory.currentItem;
		// final Container container = new ContainerKeyring( player, title, protectedSlot );
		player.openGui( BetterStorage.instance, EnumGui.KEYRING.getGuiID(), world, 0, 0, 0 );
		return new ActionResult( EnumActionResult.SUCCESS, stack );
	}

	@Override
	public EnumActionResult onItemUse( ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing,
			float hitX, float hitY, float hitZ )
	{
		if( hand == EnumHand.MAIN_HAND )
		{
			final TileEntity tileEntity = worldIn.getTileEntity( pos );
			if( tileEntity instanceof TileEntityLockable )
			{
				final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
				if( unlock( stack, lockable.getLock(), false ) )
					lockable.useUnlocked( playerIn );
			}
		}
		return super.onItemUse( stack, playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
	}

	// IKey implementation

	@Override
	public boolean isNormalKey()
	{
		return false;
	}

	@Override
	public boolean unlock( ItemStack keyring, ItemStack lock, boolean useAbility )
	{

		// Goes through all the keys in the keyring,
		// returns if any of the keys fit in the lock.

		final InventoryKeyring inventory = new InventoryKeyring( keyring );
		for( int i = 0; i < inventory.getSizeInventory(); i++ )
		{
			final ItemStack key = inventory.getStackInSlot( i );
			if( key != null )
			{
				final IKey keyType = (IKey)key.getItem();
				if( keyType.unlock( key, lock, false ) )
					return true;
			}
		}

		return false;

	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return false;
	}

	// IContainerItem implementation

	@Override
	public ItemStack[] getContainerItemContents( ItemStack container )
	{
		final ItemStack[] contents = new ItemStack[9];
		final NBTTagCompound compound = container.getTagCompound();
		if( compound != null && compound.hasKey( "Items" ) )
			NbtUtils.readItems( contents, compound.getTagList( "Items", NBT.TAG_COMPOUND ) );
		return contents;
		// return StackUtils.getStackContents( container, 9 );
	}

	@Override
	public boolean canBeStoredInContainerItem( ItemStack item )
	{
		return true;
	}
}
