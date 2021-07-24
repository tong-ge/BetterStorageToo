package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.api.IHexKeyConfig;
import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityConfig;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityContainer;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ConfigContainer extends Container
{
	private final IHexKeyConfig			configContainer;
	private final TileEntityContainer	tileContainer;

	private static final int			CONFIG_SLOTS	= 1;

	public int							indexStart, indexPlayer, indexHotbar;

	public ConfigContainer( int windowId, PlayerInventory playerInventory, World world, BlockPos pos )
	{
		super( BetterStorageContainerTypes.CONFIG.get(), windowId );
		tileContainer = (TileEntityContainer)world.getBlockEntity( pos );
		configContainer = tileContainer.getCapability( CapabilityConfig.CONFIG_CAPABILITY, null ).orElse( null );

		indexStart = 0;
		indexHotbar = CONFIG_SLOTS;
		indexPlayer = indexHotbar + 9;

		addSlot( new ConfigSlot( configContainer, HexKeyConfig.SLOT_APPEARANCE, 8, 18 ) );

		// Fill player inventory
		for( int i = 0; i < 27; i++ )
		{
			final int x = i % 9 * 18;
			final int y = i / 9 * 18;
			addSlot( new Slot( playerInventory, i + 9, 8 + x, 50 + y ) );
		}

		for( int i = 0; i < 9; i++ )
		{
			final int x = i % 9 * 18;
			addSlot( new Slot( playerInventory, i, 8 + x, 108 ) );
		}
	}

	@Override
	public ItemStack quickMoveStack( PlayerEntity playerIn, int index )
	{
		final Slot slot = slots.get( index );
		ItemStack returnStack = ItemStack.EMPTY;

		if( slot != null && slot.hasItem() )
		{
			final ItemStack itemStack = slot.getItem();
			returnStack = itemStack.copy();

			if( index < indexHotbar )
			{
				// Try to transfer from container to player
				if( !moveItemStackTo( itemStack, indexHotbar, slots.size(), true ) )
					return ItemStack.EMPTY;
			} // Otherwise try to transfer from player to container
			else if( !moveItemStackTo( itemStack, 0, indexHotbar, false ) )
				return ItemStack.EMPTY;

			if( itemStack.isEmpty() )
				slot.set( ItemStack.EMPTY );
			else
				slot.setChanged();
		}

		return returnStack;
	}

	@Override
	public boolean stillValid( PlayerEntity playerIn )
	{
		final World world = tileContainer.getLevel();
		final BlockPos pos = tileContainer.getBlockPos();
		final BlockState state = world.getBlockState( pos );
		return stillValid( IWorldPosCallable.create( world, pos ), playerIn, state.getBlock() );
	}

	public class ConfigSlot extends SlotItemHandler
	{
		public ConfigSlot( IItemHandler itemHandler, int index, int xPosition, int yPosition )
		{
			super( itemHandler, index, xPosition, yPosition );
		}

		public int getIconX()
		{
			if( isActive() )
				return 176;
			else
				return 194;
		}

		public int getIconY()
		{
			if( getSlotIndex() == HexKeyConfig.SLOT_APPEARANCE )
				return 0;
			else
				return 18;
		}
	}
}
