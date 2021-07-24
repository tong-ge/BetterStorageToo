package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.DoorBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.state.properties.DoubleBlockHalf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityLockableDoor extends TileEntity implements IKeyLockable
{

	private ItemStack lock = ItemStack.EMPTY.copy();

	public TileEntityLockableDoor()
	{
		super( BetterStorageTileEntityTypes.LOCKABLE_DOOR.get() );
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return WorldUtils.getAABB( this, 0, 0, 0, 0, 1, 0 );
	}

	public boolean isMain()
	{
		return getBlockState().getValue( DoorBlock.HALF ) == DoubleBlockHalf.LOWER;
	}

	public TileEntity getMain()
	{
		if( isMain() )
			return this;
		return level.getBlockEntity( worldPosition.below() );
	}

	public boolean isPowered()
	{
		if( isMain() )
			return EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.TRIGGER.get(), getLock() ) > 0;
		return ( (TileEntityLockableDoor)getMain() ).isPowered();
	}

	// TileEntity synchronization

	@Override
	public CompoundNBT getUpdateTag()
	{
		final CompoundNBT nbt = super.getUpdateTag();

		if( !lock.isEmpty() )
			nbt.put( "lock", lock.serializeNBT() );

		return nbt;
	}

	@Override
	public void onDataPacket( NetworkManager network, SUpdateTileEntityPacket packet )
	{
		final CompoundNBT nbt = packet.getTag();
		if( nbt.contains( "lock" ) )
		{
			final CompoundNBT lockNBT = (CompoundNBT)nbt.get( "lock" );
			lock = ItemStack.of( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;
	}

	/*
	 * @Override
	 * public void handleUpdateTag( CompoundNBT nbt )
	 * {
	 * super.handleUpdateTag( nbt );
	 *
	 * if( nbt.contains( "lock" ) )
	 * {
	 * final CompoundNBT lockNBT = (CompoundNBT)nbt.get( "lock" );
	 * lock = ItemStack.of( lockNBT );
	 * }
	 * else
	 * lock = ItemStack.EMPTY;
	 * }
	 */

	// Reading from / writing to NBT

	@Override
	public CompoundNBT save( CompoundNBT nbt )
	{
		if( !lock.isEmpty() )
		{
			final CompoundNBT lockNBT = new CompoundNBT();
			lock.save( lockNBT );
			nbt.put( "lock", lockNBT );
		}

		return super.save( nbt );
	}

	/*
	 * @Override
	 * public void read( CompoundNBT nbt )
	 * {
	 * if( nbt.contains( "lock" ) )
	 * {
	 * final CompoundNBT lockNBT = (CompoundNBT)nbt.get( "lock" );
	 * lock = ItemStack.of( lockNBT );
	 * }
	 * else
	 * lock = ItemStack.EMPTY;
	 *
	 * super.read( nbt );
	 * }
	 */

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
		return ( (TileEntityLockableDoor)getMain() ).getLock();
	}

	@Override
	public void setLock( ItemStack lock )
	{
		// Turn it back into a normal iron door
		if( lock.isEmpty() )
		{
			this.lock = ItemStack.EMPTY;
			final BlockState blockState = level.getBlockState( worldPosition );

			//@formatter:off
				level.setBlockAndUpdate( worldPosition, Blocks.IRON_DOOR.defaultBlockState()
						.setValue( DoorBlock.FACING, blockState.getValue( DoorBlock.FACING ) )
						.setValue( DoorBlock.OPEN, blockState.getValue( DoorBlock.OPEN ) )
						.setValue( DoorBlock.HINGE, blockState.getValue( DoorBlock.HINGE ) )
						.setValue( DoorBlock.HALF, DoubleBlockHalf.LOWER ) );
				level.setBlockAndUpdate( worldPosition.above(), Blocks.IRON_DOOR.defaultBlockState()
						.setValue( DoorBlock.FACING, blockState.getValue( DoorBlock.FACING ) )
						.setValue( DoorBlock.OPEN, blockState.getValue( DoorBlock.OPEN ) )
						.setValue( DoorBlock.HINGE, blockState.getValue( DoorBlock.HINGE ) )
						.setValue( DoorBlock.HALF, DoubleBlockHalf.UPPER ) );
				//@formatter:on

			level.sendBlockUpdated( worldPosition, blockState, blockState, 3 );
		}
		else if( isLockValid( lock ) )
			setLockWithUpdate( lock );

	}

	public void setLockWithUpdate( ItemStack lock )
	{
		this.lock = lock;
		final BlockState blockState = level.getBlockState( worldPosition );
		level.sendBlockUpdated( worldPosition, blockState, blockState, 3 );
		setChanged();
	}

	@Override
	public boolean canUse( PlayerEntity player )
	{
		return false;
	}

	@Override
	public void applyTrigger()
	{}

	@Override
	public boolean unlockWith( ItemStack heldItem )
	{
		final Item item = heldItem.getItem();
		return item instanceof IKey ? ( (IKey)item ).unlock( heldItem, getLock(), false ) : false;
	}
}
