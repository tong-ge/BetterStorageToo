package io.github.tehstoneman.betterstorage.common.item.locking;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.common.inventory.KeyringCapabilityProvider;
import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class ItemKeyring extends ItemBetterStorage implements IKey
{
	public ItemKeyring()
	{
		super( "keyring", new Item.Properties().group( BetterStorage.ITEM_GROUP ) );
		// setMaxDamage( 0 );
		// setMaxStackSize( 1 );
	}

	@Override
	public ActionResult< ItemStack > onItemRightClick( World world, EntityPlayer player, EnumHand hand )
	{
		final ItemStack stack = player.getHeldItem( hand );

		if( !player.isSneaking() )
			return new ActionResult( EnumActionResult.PASS, stack );

		// player.openGui( BetterStorage.instance, EnumGui.KEYRING.getGuiID(), world, 0, 0, 0 );
		return new ActionResult( EnumActionResult.SUCCESS, stack );
	}

	/*
	 * @Override
	 * public EnumActionResult onItemUse( EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
	 * float hitZ )
	 * {
	 * if( hand == EnumHand.MAIN_HAND )
	 * {
	 * final TileEntity tileEntity = worldIn.getTileEntity( pos );
	 * if( tileEntity instanceof TileEntityLockable )
	 * {
	 * final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
	 * if( unlock( playerIn.getHeldItem( hand ), lockable.getLock(), false ) )
	 * lockable.useUnlocked( playerIn );
	 * else
	 * ( (ILock)lockable.getLock().getItem() ).applyEffects( lockable.getLock(), lockable, playerIn, EnumLockInteraction.PICK );
	 * }
	 * }
	 * return super.onItemUse( playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
	 * }
	 */

	// IKey implementation

	@Override
	public boolean isNormalKey()
	{
		return false;
	}

	@Override
	public boolean unlock( ItemStack keyring, ItemStack lock, boolean useAbility )
	{
		/*
		 * if( keyring.hasCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ) )
		 * {
		 * // Loop through all the keys in the keyring,
		 * // returns if any of the keys fit in the lock.
		 *
		 * final IItemHandler inventory = keyring.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null );
		 * for( int i = 0; i < inventory.getSlots(); i++ )
		 * {
		 * final ItemStack key = inventory.getStackInSlot( i );
		 * if( !key.isEmpty() )
		 * {
		 * final IKey keyType = (IKey)key.getItem();
		 * if( keyType.unlock( key, lock, false ) )
		 * return true;
		 * }
		 * }
		 * }
		 */
		return false;
	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return false;
	}

	@Override
	public ICapabilityProvider initCapabilities( ItemStack stack, @Nullable NBTTagCompound nbt )
	{
		return new KeyringCapabilityProvider( stack );
	}

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void registerItemModels()
	 * {
	 * for( int i = 0; i < 4; i++ )
	 * ModelLoader.setCustomModelResourceLocation( this, i, new ModelResourceLocation( getRegistryName() + "_" + i, "inventory" ) );
	 * }
	 */
}
