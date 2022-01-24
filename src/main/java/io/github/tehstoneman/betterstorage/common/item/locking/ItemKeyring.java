package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerKeyring;
import io.github.tehstoneman.betterstorage.common.inventory.KeyringCapabilityProvider;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.network.NetworkHooks;

public class ItemKeyring extends ItemBetterStorage implements IKey, MenuProvider
{
	public ItemKeyring()
	{
		super( new Item.Properties() );
		/*
		 * addPropertyOverride( new ResourceLocation( "full" ), ( itemStack, world,
		 * entityPlayer ) -> { return getFilledCapacity( itemStack ); } );
		 */
	}

	@Override
	public InteractionResultHolder< ItemStack > use( Level world, Player player, InteractionHand hand )
	{
		final ItemStack stack = player.getItemInHand( hand );

		if( !player.isCrouching() )
			return new InteractionResultHolder<>( InteractionResult.PASS, stack );

		if( !world.isClientSide )
			NetworkHooks.openGui( (ServerPlayer)player, BetterStorageItems.KEYRING.get(),
					buf -> buf.writeItem( player.getItemInHand( hand ) ).writeInt( player.getInventory().selected ) );
		return new InteractionResultHolder<>( InteractionResult.SUCCESS, stack );
	}

	/*
	 * ==== IKey ====
	 */

	@Override
	public boolean unlock( ItemStack keyring, ItemStack lock, boolean useAbility )
	{
		// Loop through all the keys in the keyring,
		// returns if any of the keys fit in the lock.

		final IItemHandler inventory = keyring.getCapability( CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null ).orElse( null );
		if( inventory != null )
			for( int i = 0; i < inventory.getSlots(); i++ )
			{
				final ItemStack key = inventory.getStackInSlot( i );
				if( !key.isEmpty() )
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

	@Override
	public ICapabilityProvider initCapabilities( ItemStack stack, @Nullable CompoundTag nbt )
	{
		return new KeyringCapabilityProvider( stack );
	}

	@Override
	public AbstractContainerMenu createMenu( int windowID, Inventory playerInventory, Player player )
	{
		final ItemStack itemStack = player.getMainHandItem();
		final int index = player.getInventory().selected;
		return new ContainerKeyring( windowID, playerInventory, itemStack, index );
	}

	@Override
	public Component getDisplayName()
	{
		return new TranslatableComponent( ModInfo.CONTAINER_KEYRING_NAME );
	}

	public float getFilledCapacity( ItemStack itemStack )
	{
		if( itemStack.hasTag() )
		{
			final CompoundTag tag = itemStack.getTag();
			if( tag.contains( "Occupied" ) )
			{
				final int occupied = tag.getInt( "Occupied" );
				return occupied / 9.0f;
			}
		}
		return 0.0F;
	}

	@Override
	public void appendHoverText( ItemStack stack, @Nullable Level worldIn, List< Component > tooltip, TooltipFlag flagIn )
	{
		super.appendHoverText( stack, worldIn, tooltip, flagIn );
		if( flagIn.isAdvanced() && stack.hasTag() )
		{
			final CompoundTag tag = stack.getTag();
			if( tag.contains( "Occupied" ) )
				tooltip.add( new TranslatableComponent( "Keys : " + tag.getInt( "Occupied" ) ) );
		}
	}
}
