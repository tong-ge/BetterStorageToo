package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.api.IHasConfig;
import io.github.tehstoneman.betterstorage.api.IHexKeyConfig;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityConfig;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.inventory.ConfigContainer;
import io.github.tehstoneman.betterstorage.common.inventory.ReinforcedChestContainer;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.LidBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;

@OnlyIn( value = Dist.CLIENT, _interface = LidBlockEntity.class )
public class TileEntityReinforcedChest extends TileEntityConnectable implements LidBlockEntity, IKeyLockable, IHasConfig// , TickableBlockEntity
{
	protected int								ticksSinceSync;
	private ItemStack							lock			= ItemStack.EMPTY.copy();
	public HexKeyConfig							config;
	private final LazyOptional< IHexKeyConfig >	configHandler	= LazyOptional.of( () -> config );

	public TileEntityReinforcedChest( BlockPos blockPos, BlockState blockState )
	{
		super( BetterStorageTileEntityTypes.REINFORCED_CHEST.get(), blockPos, blockState );
		config = new HexKeyConfig()
		{
			@Override
			protected void onContentsChanged( int slot )
			{
				TileEntityReinforcedChest.this.setChanged();
			}
		};
	}

	@Override
	public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction facing )
	{
		if( isMain() )
		{
			if( capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY && isLocked() && facing != null )
				return LazyOptional.empty();
			if( capability == CapabilityConfig.CONFIG_CAPABILITY && !isLocked() )
				return CapabilityConfig.CONFIG_CAPABILITY.orEmpty( capability, configHandler );
			return super.getCapability( capability, facing );
		}
		else
			return getMainTileEntity().getCapability( capability, facing );
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public AABB getRenderBoundingBox()
	{
		return new AABB( worldPosition.offset( -1, 0, -1 ), worldPosition.offset( 2, 2, 2 ) );
	}

	@Override
	public int getColumns()
	{
		return BetterStorageConfig.COMMON.reinforcedColumns.get();
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.CONTAINER_REINFORCED_CHEST_NAME;
	}

	/*
	 * =====================
	 * TileEntityConnectable
	 * =====================
	 */

	@Override
	public BlockPos getConnected()
	{
		if( isConnected() )
			return getBlockPos().relative( BlockReinforcedChest.getDirectionToAttached( getBlockState() ) );
		return worldPosition;
	}

	@Override
	public void dropInventoryItems()
	{
		super.dropInventoryItems();
		if( !config.isEmpty() )
			if( isConnected() && isMain() )
				( (TileEntityReinforcedChest)getConnectedTileEntity() ).setConfig( config );
			else
				for( int i = 0; i < config.getSlots(); i++ )
				{
					final ItemStack stack = config.getStackInSlot( i );
					if( !stack.isEmpty() )
						Containers.dropItemStack( getLevel(), worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), stack );
				}
	}

	/*
	 * ==================
	 * TileEntityLockable
	 * ==================
	 */

	@Override
	public AbstractContainerMenu createMenu( int windowID, Inventory playerInventory, Player player )
	{
		if( isMain() )
		{
			if( player.getMainHandItem().getItem() == BetterStorageItems.HEX_KEY.get() )
				return new ConfigContainer( windowID, playerInventory, getLevel(), getBlockPos() );
			return new ReinforcedChestContainer( windowID, playerInventory, getLevel(), getBlockPos() );
		}
		else
			return getMainTileEntity().createMenu( windowID, playerInventory, player );
	}

	/*
	 * =========
	 * ITickable
	 * =========
	 */

	/*
	 * @Override
	 * public void tick()
	 * {
	 * final int x = worldPosition.getX();
	 * final int y = worldPosition.getY();
	 * final int z = worldPosition.getZ();
	 * ++ticksSinceSync;
	 * if( !level.isClientSide && numPlayersUsing != 0 && ( ticksSinceSync + x + y + z ) % 200 == 0 )
	 * {
	 * numPlayersUsing = 0;
	 * for( final Player entityplayer : level.getEntitiesOfClass( Player.class,
	 * new AABB( x - 5.0F, y - 5.0F, z - 5.0F, x + 1 + 5.0F, y + 1 + 5.0F, z + 1 + 5.0F ) ) )
	 * if( entityplayer.containerMenu instanceof ReinforcedChestContainer )
	 * ++numPlayersUsing;
	 * }
	 *
	 * prevLidAngle = lidAngle;
	 * if( numPlayersUsing > 0 && lidAngle == 0.0F )
	 * playSound( SoundEvents.CHEST_OPEN );
	 *
	 * if( numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F )
	 * {
	 * final float f2 = lidAngle;
	 * if( numPlayersUsing > 0 )
	 * lidAngle += 0.1F;
	 * else
	 * lidAngle -= 0.1F;
	 *
	 * if( lidAngle > 1.0F )
	 * lidAngle = 1.0F;
	 *
	 * if( lidAngle < 0.5F && f2 >= 0.5F )
	 * playSound( SoundEvents.CHEST_CLOSE );
	 *
	 * if( lidAngle < 0.0F )
	 * lidAngle = 0.0F;
	 * }
	 * }
	 */

	/*
	 * =========
	 * LidBlockEntity
	 * =========
	 */

	protected void playSound( SoundEvent soundIn )
	{
		final ConnectedType chesttype = getBlockState().getValue( BlockConnectableContainer.TYPE );
		double x = worldPosition.getX() + 0.5D;
		final double y = worldPosition.getY() + 0.5D;
		double z = worldPosition.getZ() + 0.5D;
		if( chesttype != ConnectedType.SINGLE )
		{
			final Direction enumfacing = BlockReinforcedChest.getDirectionToAttached( getBlockState() );
			x += enumfacing.getStepX() * 0.5D;
			z += enumfacing.getStepZ() * 0.5D;
		}

		level.playSound( (Player)null, x, y, z, soundIn, SoundSource.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F );
	}

	@Override
	public float getOpenNess( float partialTicks )
	{
		return prevLidAngle + ( lidAngle - prevLidAngle ) * partialTicks;
	}

	/*
	 * ============
	 * IKeyLockable
	 * ============
	 */

	@Override
	public ItemStack getLock()
	{
		if( isMain() )
			return lock;
		else
			return ( (IKeyLockable)getMainTileEntity() ).getLock();
	}

	@Override
	public void setLock( ItemStack lock )
	{
		if( isMain() )
		{
			if( lock.isEmpty() || isLockValid( lock ) )
			{
				this.lock = lock;
				getLevel().sendBlockUpdated( worldPosition, getBlockState(), getBlockState(), 3 );
				setPowered( EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.TRIGGER.get(), lock ) > 0 );
				setChanged();
			}
		}
		else
			( (IKeyLockable)getMainTileEntity() ).setLock( lock );
	}

	@Override
	public boolean canUse( Player player )
	{
		return !isLocked() || getMainTileEntity().getPlayersUsing() > 0;
	}

	@Override
	public void applyTrigger()
	{
		setPowered( true );
	}

	@Override
	public boolean unlockWith( ItemStack heldItem )
	{
		final Item item = heldItem.getItem();
		return item instanceof IKey ? ( (IKey)item ).unlock( heldItem, getLock(), false ) : false;
	}

	// Trigger enchantment related

	/**
	 * Returns if the chest is emitting redstone.
	 *
	 * @return True if powered
	 */
	public boolean isPowered()
	{
		if( isMain() )
			return EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.TRIGGER.get(), getLock() ) > 0;
		return ( (TileEntityReinforcedChest)getMainTileEntity() ).isPowered();
	}

	/**
	 * Sets if the chest is emitting redstone.
	 * <p>
	 * Updates all nearby blocks to make sure they notice it.
	 *
	 * @param powered
	 *            True if powered
	 */
	public void setPowered( boolean powered )
	{
		if( !isMain() )
		{
			( (TileEntityReinforcedChest)getMainTileEntity() ).setPowered( powered );
			return;
		}

		final Block block = getBlockState().getBlock();

		// Notify nearby blocks
		getLevel().updateNeighborsAt( worldPosition, block );

		// Notify nearby blocks of adjacent chest
		if( isConnected() )
			getLevel().updateNeighborsAt( getConnected(), block );
	}

	public void renderUpdate()
	{
		if( isMain() )
		{
			final BlockState state = getBlockState();

			// Notify nearby blocks
			getLevel().setBlocksDirty( worldPosition, state, state );
		}
		else
			( (TileEntityReinforcedChest)getMainTileEntity() ).renderUpdate();
	}

	@Override
	public HexKeyConfig getConfig()
	{
		return config;
	}

	@Override
	public void setConfig( HexKeyConfig config )
	{
		this.config = config;
		setChanged();
	}

	/*
	 * ==========================
	 * BlockEntity synchronization
	 * ==========================
	 */

	@Override
	public CompoundTag getUpdateTag()
	{
		final CompoundTag nbt = super.getUpdateTag();

		if( !config.isEmpty() )
			nbt.put( "Config", config.serializeNBT() );
		if( !lock.isEmpty() )
			nbt.put( "lock", lock.serializeNBT() );

		return nbt;
	}

	@Override
	public void onDataPacket( Connection network, ClientboundBlockEntityDataPacket packet )
	{
		final CompoundTag nbt = packet.getTag();
		if( nbt.contains( "Config" ) )
			config.deserializeNBT( nbt.getCompound( "Config" ) );
		else
			config = new HexKeyConfig();
		if( nbt.contains( "lock" ) )
		{
			final CompoundTag lockNBT = (CompoundTag)nbt.get( "lock" );
			lock = ItemStack.of( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;
	}

	/*
	 * @Override
	 * public ClientboundBlockEntityDataPacket getUpdatePacket()
	 * {
	 * return new ClientboundBlockEntityDataPacket( worldPosition, 1, getUpdateTag() );
	 * }
	 */

	@Override
	public void handleUpdateTag( CompoundTag nbt )
	{
		super.handleUpdateTag( nbt );

		if( nbt.contains( "Config" ) )
			config.deserializeNBT( nbt.getCompound( "Config" ) );
		else
			config = new HexKeyConfig();
		if( nbt.contains( "lock" ) )
		{
			final CompoundTag lockNBT = (CompoundTag)nbt.get( "lock" );
			lock = ItemStack.of( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;
	}

	@Override
	public CompoundTag save( CompoundTag nbt )
	{
		if( !config.isEmpty() )
			nbt.put( "Config", config.serializeNBT() );
		if( !lock.isEmpty() )
		{
			final CompoundTag lockNBT = new CompoundTag();
			lock.save( lockNBT );
			nbt.put( "lock", lockNBT );
		}

		return super.save( nbt );
	}

	@Override
	public void load( CompoundTag nbt )
	{
		if( nbt.contains( "Config" ) )
			config.deserializeNBT( nbt.getCompound( "Config" ) );
		if( nbt.contains( "lock" ) )
		{
			final CompoundTag lockNBT = (CompoundTag)nbt.get( "lock" );
			lock = ItemStack.of( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;

		super.load( nbt );
	}
}
