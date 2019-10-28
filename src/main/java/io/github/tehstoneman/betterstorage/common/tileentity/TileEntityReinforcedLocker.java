package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerReinforcedLocker;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;

public class TileEntityReinforcedLocker extends TileEntityLocker
{
	public TileEntityReinforcedLocker()
	{
		super( BetterStorageTileEntityTypes.REINFORCED_LOCKER );
	}

	/*
	 * @Override
	 * public boolean canHaveLock()
	 * {
	 * return true;
	 * }
	 */

	/*
	 * @Override
	 * public void setAttachmentPosition()
	 * {
	 * final double x = mirror ? 13.5 : 2.5;
	 * final double y = isConnected() ? 0 : 8;
	 * lockAttachment.setBox( x, y, 0.5, 5, 5, 1 );
	 * lockAttachment.setScale( 0.375F, 1.5F );
	 * }
	 */

	@Override
	public int getColumns()
	{
		return BetterStorageConfig.COMMON.reinforcedColumns.get();
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerReinforcedLocker;
	}

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		if( isMain() )
			return new ContainerReinforcedLocker( windowID, playerInventory, world, pos );
		else
			return getMainTileEntity().createMenu( windowID, playerInventory, player );
	}

	@Override
	public void tick()
	{
		final int x = pos.getX();
		final int y = pos.getY();
		final int z = pos.getZ();
		++ticksSinceSync;
		if( !world.isRemote && numPlayersUsing != 0 && ( ticksSinceSync + x + y + z ) % 200 == 0 )
		{
			numPlayersUsing = 0;
			final float f = 5.0F;

			for( final PlayerEntity entityplayer : world.getEntitiesWithinAABB( PlayerEntity.class,
					new AxisAlignedBB( x - 5.0F, y - 5.0F, z - 5.0F, x + 1 + 5.0F, y + 1 + 5.0F, z + 1 + 5.0F ) ) )
				if( entityplayer.openContainer instanceof ContainerReinforcedLocker )
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
	 * public void setMaterial( EnumReinforced reinforcedMaterial )
	 * {
	 * material = reinforcedMaterial;
	 * markDirty();
	 * }
	 */
}
