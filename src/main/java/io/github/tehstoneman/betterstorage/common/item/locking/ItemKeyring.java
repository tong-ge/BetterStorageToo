package io.github.tehstoneman.betterstorage.common.item.locking;

import java.util.List;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerKeyring;
import io.github.tehstoneman.betterstorage.common.inventory.KeyringCapabilityProvider;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.ItemBetterStorage;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class ItemKeyring extends ItemBetterStorage implements IKey, INamedContainerProvider
{
	public ItemKeyring()
	{
		super( "keyring", new Item.Properties().group( BetterStorage.ITEM_GROUP ) );
		addPropertyOverride( new ResourceLocation( "full" ), ( itemStack, world, entityPlayer ) ->
		{
			return getFilledCapacity( itemStack );
		} );
	}

	@Override
	public ActionResult< ItemStack > onItemRightClick( World world, PlayerEntity player, Hand hand )
	{
		final ItemStack stack = player.getHeldItem( hand );

		if( !player.isSneaking() )
			return new ActionResult( ActionResultType.PASS, stack );

		if( !world.isRemote )
			NetworkHooks.openGui( (ServerPlayerEntity)player, BetterStorageItems.KEYRING,
					buf -> buf.writeItemStack( player.getHeldItem( hand ) ).writeInt( player.inventory.currentItem ) );
		return new ActionResult( ActionResultType.SUCCESS, stack );
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
	public ICapabilityProvider initCapabilities( ItemStack stack, @Nullable CompoundNBT nbt )
	{
		return new KeyringCapabilityProvider( stack );
	}

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		final ItemStack itemStack = player.getHeldItemMainhand();
		final int index = player.inventory.currentItem;
		return new ContainerKeyring( windowID, playerInventory, itemStack, index );
	}

	@Override
	public ITextComponent getDisplayName()
	{
		return new TranslationTextComponent( ModInfo.CONTAINER_KEYRING_NAME );
	}

	public float getFilledCapacity( ItemStack itemStack )
	{
		if( itemStack.hasTag() )
		{
			final CompoundNBT tag = itemStack.getTag();
			if( tag.contains( "Occupied" ) )
			{
				final int occupied = tag.getInt( "Occupied" );
				return occupied / 9.0f;
			}
		}
		return 0.0F;
	}

	@Override
	public void addInformation( ItemStack stack, @Nullable World worldIn, List< ITextComponent > tooltip, ITooltipFlag flagIn )
	{
		super.addInformation( stack, worldIn, tooltip, flagIn );
		if( flagIn.isAdvanced() && stack.hasTag() )
		{
			final CompoundNBT tag = stack.getTag();
			if( tag.contains( "Occupied" ) )
				tooltip.add( new TranslationTextComponent( "Keys : " + tag.getInt( "Occupied" ) ) );
		}
	}
}
