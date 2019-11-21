package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumConnectedType;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerReinforcedChest;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class TileEntityReinforcedChest extends TileEntityConnectable implements IChestLid, ITickableTileEntity, IKeyLockable
{
	private boolean		powered;
	protected int		ticksSinceSync;
	private ItemStack	lock	= ItemStack.EMPTY.copy();

	public TileEntityReinforcedChest()
	{
		super( BetterStorageTileEntityTypes.REINFORCED_CHEST );
	}

	@Override
	@OnlyIn( Dist.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB( pos.add( -1, 0, -1 ), pos.add( 2, 2, 2 ) );
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

	/*
	 * public EnumConnectedType getChestType()
	 * {
	 * final IBlockState blockState = hasWorld() ? getBlockState() : BetterStorageBlocks.REINFORCED_CHEST.getDefaultState();
	 * return blockState.has( BlockLockable.TYPE ) ? blockState.get( BlockLockable.TYPE ) : EnumConnectedType.SINGLE;
	 * }
	 */

	@Override
	public BlockPos getConnected()
	{
		if( isConnected() )
			return getPos().offset( BlockReinforcedChest.getDirectionToAttached( getBlockState() ) );
		return pos;
	}

	/*
	 * ==================
	 * TileEntityLockable
	 * ==================
	 */

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		if( isMain() )
			return new ContainerReinforcedChest( windowID, playerInventory, world, pos );
		else
			return getMainTileEntity().createMenu( windowID, playerInventory, player );
	}

	/*
	 * =========
	 * ITickable
	 * =========
	 */

	@Override
	public void tick()
	{
		final int i = pos.getX();
		final int j = pos.getY();
		final int k = pos.getZ();
		++ticksSinceSync;
		if( !world.isRemote && numPlayersUsing != 0 && ( ticksSinceSync + i + j + k ) % 200 == 0 )
		{
			numPlayersUsing = 0;
			final float f = 5.0F;

			for( final PlayerEntity entityplayer : world.getEntitiesWithinAABB( PlayerEntity.class,
					new AxisAlignedBB( i - 5.0F, j - 5.0F, k - 5.0F, i + 1 + 5.0F, j + 1 + 5.0F, k + 1 + 5.0F ) ) )
				if( entityplayer.openContainer instanceof ContainerReinforcedChest )
					++numPlayersUsing;
		}

		prevLidAngle = lidAngle;
		final float f1 = 0.1F;
		if( numPlayersUsing > 0 && lidAngle == 0.0F )
			playSound( SoundEvents.BLOCK_CHEST_OPEN );

		if( numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F )
		{
			final float f2 = lidAngle;
			if( numPlayersUsing > 0 )
				lidAngle += 0.1F;
			else
				lidAngle -= 0.1F;

			if( lidAngle > 1.0F )
				lidAngle = 1.0F;

			final float f3 = 0.5F;
			if( lidAngle < 0.5F && f2 >= 0.5F )
				playSound( SoundEvents.BLOCK_CHEST_CLOSE );

			if( lidAngle < 0.0F )
				lidAngle = 0.0F;
		}
	}

	/*
	 * @Override
	 * public void setAttachmentPosition()
	 * {
	 * // final double x = !isConnected() ? 8 : getOrientation() == EnumFacing.WEST || getOrientation() == EnumFacing.SOUTH ? 0 : 16;
	 * // lockAttachment.setBox( x, 6.5, 0.5, 7, 7, 1 );
	 * }
	 */

	/*
	 * =========
	 * ITickable
	 * =========
	 */

	/*
	 * =========
	 * IChestLid
	 * =========
	 */

	protected void playSound( SoundEvent soundIn )
	{
		final EnumConnectedType chesttype = getBlockState().get( BlockConnectableContainer.TYPE );
		double x = pos.getX() + 0.5D;
		final double y = pos.getY() + 0.5D;
		double z = pos.getZ() + 0.5D;
		if( chesttype != EnumConnectedType.SINGLE )
		{
			final Direction enumfacing = BlockReinforcedChest.getDirectionToAttached( getBlockState() );
			x += enumfacing.getXOffset() * 0.5D;
			z += enumfacing.getZOffset() * 0.5D;
		}

		world.playSound( (PlayerEntity)null, x, y, z, soundIn, SoundCategory.BLOCKS, 0.5F, world.rand.nextFloat() * 0.1F + 0.9F );
	}

	@Override
	public float getLidAngle( float partialTicks )
	{
		return prevLidAngle + ( lidAngle - prevLidAngle ) * partialTicks;
	}

	@Override
	public ItemStack getLock()
	{
		if( isMain() )
			return lock;
		else
			return ( (IKeyLockable)getMainTileEntity() ).getLock();
	}

	@Override
	public boolean isLockValid( ItemStack lock )
	{
		return lock.isEmpty() || lock.getItem() instanceof ILock;
	}

	@Override
	public void setLock( ItemStack lock )
	{
		if( isMain() )
		{
			if( isLockValid( lock ) )
			{
				this.lock = lock;
				getWorld().notifyBlockUpdate( pos, getBlockState(), getBlockState(), 3 );
				setPowered( EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.TRIGGER, lock ) > 0 );
				markDirty();
			}
		}
		else
			( (IKeyLockable)getMainTileEntity() ).setLock( lock );
	}

	@Override
	public void applyTrigger()
	{
		setPowered( true );
	}

	// Trigger enchantment related

	/** Returns if the chest is emitting redstone. */
	public boolean isPowered()
	{
		if( isMain() )
			return EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.TRIGGER, getLock() ) > 0;
		// return powered;
		return ( (TileEntityReinforcedChest)getMainTileEntity() ).isPowered();
	}

	/*
	 * @Override
	 * public void closeInventory( PlayerEntity player )
	 * {
	 * super.closeInventory( player );
	 * if( isPowered() )
	 * setPowered( numPlayersUsing > 0 );
	 * }
	 */

	/**
	 * Sets if the chest is emitting redstone.
	 * Updates all nearby blocks to make sure they notice it.
	 */
	public void setPowered( boolean powered )
	{
		if( !isMain() )
		{
			( (TileEntityReinforcedChest)getMainTileEntity() ).setPowered( powered );
			return;
		}

		this.powered = powered;

		final Block block = getBlockState().getBlock();
		// Schedule a block update to turn the redstone signal back off.
		// if( powered ) getWorld().scheduleBlockUpdate( pos, block, 10, 1 );

		// Notify nearby blocks
		getWorld().notifyNeighborsOfStateChange( pos, block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( -1, 0, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 1, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, -1, 0 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 0, 1 ), block );
		// this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 0, -1 ), block );

		// Notify nearby blocks of adjacent chest
		if( isConnected() )
			getWorld().notifyNeighborsOfStateChange( getConnected(), block );

		/*
		 * if( isConnected() && getConnected() == EnumFacing.EAST )
		 * {
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 2, 0, 0 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 1, 0 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, -1, 0 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, -1 ), block );
		 * }
		 * if( isConnected() && getConnected() == EnumFacing.SOUTH )
		 * {
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 0, 2 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 1, 0, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( -1, 0, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, 1, 1 ), block );
		 * this.getWorld().notifyNeighborsOfStateChange( pos.add( 0, -1, 1 ), block );
		 * }
		 */
	}

	/*
	 * ==========================
	 * TileEntity synchronization
	 * ==========================
	 */

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
		final CompoundNBT nbt = packet.getNbtCompound();
		if( nbt.contains( "lock" ) )
		{
			final CompoundNBT lockNBT = (CompoundNBT)nbt.get( "lock" );
			lock = ItemStack.read( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;
	}

	@Override
	public SUpdateTileEntityPacket getUpdatePacket()
	{
		return new SUpdateTileEntityPacket( pos, 1, getUpdateTag() );
	}

	@Override
	public void handleUpdateTag( CompoundNBT nbt )
	{
		super.handleUpdateTag( nbt );

		if( nbt.contains( "lock" ) )
		{
			final CompoundNBT lockNBT = (CompoundNBT)nbt.get( "lock" );
			lock = ItemStack.read( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;
	}

	@Override
	public CompoundNBT write( CompoundNBT nbt )
	{
		if( !lock.isEmpty() )
		{
			final CompoundNBT lockNBT = new CompoundNBT();
			lock.write( lockNBT );
			nbt.put( "lock", lockNBT );
		}

		return super.write( nbt );
	}

	@Override
	public void read( CompoundNBT nbt )
	{
		if( nbt.contains( "lock" ) )
		{
			final CompoundNBT lockNBT = (CompoundNBT)nbt.get( "lock" );
			lock = ItemStack.read( lockNBT );
		}
		else
			lock = ItemStack.EMPTY;

		super.read( nbt );
	}

	@Override
	public void useUnlocked( PlayerEntity player )
	{}

	@Override
	public boolean canUse( PlayerEntity player )
	{
		return getLock().isEmpty() || getMainTileEntity().getPlayersUsing() > 0;
	}

	@Override
	public boolean isLocked()
	{
		if( isMain() )
			return getPlayersUsing() > 0 || !getLock().isEmpty();
		else
			return ( (IKeyLockable)getMainTileEntity() ).isLocked();
	}

	@Override
	public boolean unlockWith( ItemStack heldItem )
	{
		final Item item = heldItem.getItem();
		return item instanceof IKey ? ( (IKey)item ).unlock( heldItem, getLock(), false ) : false;
	}
}
