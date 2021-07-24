package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.ConnectedType;
import io.github.tehstoneman.betterstorage.common.block.BlockConnectableContainer;
import io.github.tehstoneman.betterstorage.common.block.BlockLocker;
import io.github.tehstoneman.betterstorage.common.inventory.ContainerLocker;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.IChestLid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( value = Dist.CLIENT, _interface = IChestLid.class )
public class TileEntityLocker extends TileEntityConnectable implements IChestLid, ITickableTileEntity
{
	protected int ticksSinceSync;

	public TileEntityLocker( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
	}

	public TileEntityLocker()
	{
		super( BetterStorageTileEntityTypes.LOCKER.get() );
	}

	@Override
	public AxisAlignedBB getRenderBoundingBox()
	{
		return new AxisAlignedBB( worldPosition.offset( -1, 0, -1 ), worldPosition.offset( 2, 2, 2 ) );
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.CONTAINER_LOCKER_NAME;
	}

	@Override
	public BlockPos getConnected()
	{
		if( isConnected() )
			if( isMain() )
				return worldPosition.above();
			else
				return worldPosition.below();
		return null;
	}

	@Override
	public Container createMenu( int windowID, PlayerInventory playerInventory, PlayerEntity player )
	{
		if( isMain() )
			return new ContainerLocker( windowID, playerInventory, level, worldPosition );
		else
			return getMainTileEntity().createMenu( windowID, playerInventory, player );
	}

	@Override
	public void tick()
	{
		final int x = worldPosition.getX();
		final int y = worldPosition.getY();
		final int z = worldPosition.getZ();
		++ticksSinceSync;
		if( !level.isClientSide && numPlayersUsing != 0 && ( ticksSinceSync + x + y + z ) % 200 == 0 )
		{
			numPlayersUsing = 0;
			for( final PlayerEntity entityplayer : level.getEntitiesOfClass( PlayerEntity.class,
					new AxisAlignedBB( x - 5.0F, y - 5.0F, z - 5.0F, x + 1 + 5.0F, y + 1 + 5.0F, z + 1 + 5.0F ) ) )
				if( entityplayer.containerMenu instanceof ContainerLocker )
					++numPlayersUsing;
		}

		prevLidAngle = lidAngle;
		if( numPlayersUsing > 0 && lidAngle == 0.0F )
			playSound( SoundEvents.CHEST_OPEN );

		if( numPlayersUsing == 0 && lidAngle > 0.0F || numPlayersUsing > 0 && lidAngle < 1.0F )
		{
			final float f2 = lidAngle;
			if( numPlayersUsing > 0 )
				lidAngle += 0.1F;
			else
				lidAngle -= 0.1F;

			if( lidAngle > 1.0F )
				lidAngle = 1.0F;

			if( lidAngle < 0.5F && f2 >= 0.5F )
				playSound( SoundEvents.CHEST_CLOSE );

			if( lidAngle < 0.0F )
				lidAngle = 0.0F;
		}
	}

	protected void playSound( SoundEvent soundIn )
	{
		final ConnectedType chesttype = getBlockState().getValue( BlockConnectableContainer.TYPE );
		double x = worldPosition.getX() + 0.5D;
		final double y = worldPosition.getY() + 0.5D;
		double z = worldPosition.getZ() + 0.5D;
		if( chesttype != ConnectedType.SINGLE )
		{
			final Direction enumfacing = BlockLocker.getDirectionToAttached( getBlockState() );
			x += enumfacing.getStepX() * 0.5D;
			z += enumfacing.getStepZ() * 0.5D;
		}

		level.playSound( (PlayerEntity)null, x, y, z, soundIn, SoundCategory.BLOCKS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F );
	}

	@Override
	public float getOpenNess( float partialTicks )
	{
		return prevLidAngle + ( lidAngle - prevLidAngle ) * partialTicks;
	}
}
