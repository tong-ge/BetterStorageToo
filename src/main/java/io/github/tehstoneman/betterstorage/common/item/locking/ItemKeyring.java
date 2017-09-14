package io.github.tehstoneman.betterstorage.common.item.locking;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import io.github.tehstoneman.betterstorage.common.inventory.KeyringCapabilityProvider;
import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockable;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemKeyring extends ItemBetterStorage implements IKey
{
	public ItemKeyring()
	{
		super( "keyring" );
		setMaxStackSize( 1 );
	}

	@Override
	public ActionResult< ItemStack > onItemRightClick( World world, EntityPlayer player, EnumHand hand )
	{
		final ItemStack stack = player.getHeldItem( hand );

		if( !player.isSneaking() )
			return new ActionResult( EnumActionResult.PASS, stack );

		player.openGui( BetterStorage.instance, EnumGui.KEYRING.getGuiID(), world, 0, 0, 0 );
		return new ActionResult( EnumActionResult.SUCCESS, stack );
	}

	@Override
	public EnumActionResult onItemUse( EntityPlayer playerIn, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
			float hitZ )
	{
		if( hand == EnumHand.MAIN_HAND )
		{
			final TileEntity tileEntity = worldIn.getTileEntity( pos );
			if( tileEntity instanceof TileEntityLockable )
			{
				final TileEntityLockable lockable = (TileEntityLockable)tileEntity;
				if( unlock( playerIn.getHeldItem( hand ), lockable.getLock(), false ) )
					lockable.useUnlocked( playerIn );
			}
		}
		return super.onItemUse( playerIn, worldIn, pos, hand, facing, hitX, hitY, hitZ );
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
		if( keyring.hasCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ) )
		{
			// Loop through all the keys in the keyring,
			// returns if any of the keys fit in the lock.

			final IItemHandler inventory = keyring.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null );
			for( int i = 0; i < inventory.getSlots(); i++ )
			{
				final ItemStack key = inventory.getStackInSlot( i );
				if( key != null )
				{
					final IKey keyType = (IKey)key.getItem();
					if( keyType.unlock( key, lock, false ) )
						return true;
				}
			}
		}
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

	@Override
	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		for( int i = 0; i < 4; i++ )
			ModelLoader.setCustomModelResourceLocation( this, i, new ModelResourceLocation( getRegistryName() + "_" + i, "inventory" ) );
	}
}
