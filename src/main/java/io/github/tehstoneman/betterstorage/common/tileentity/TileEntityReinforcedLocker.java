package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import net.minecraft.tileentity.TileEntityType;

public class TileEntityReinforcedLocker extends TileEntityLocker
{
	public TileEntityReinforcedLocker( TileEntityType< ? > tileEntityTypeIn )
	{
		super( tileEntityTypeIn );
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean canHaveLock()
	{
		return true;
	}

	@Override
	public void setAttachmentPosition()
	{
		final double x = mirror ? 13.5 : 2.5;
		final double y = isConnected() ? 0 : 8;
		lockAttachment.setBox( x, y, 0.5, 5, 5, 1 );
		lockAttachment.setScale( 0.375F, 1.5F );
	}

	@Override
	public int getColumns()
	{
		return BetterStorage.config.reinforcedColumns;
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerReinforcedLocker;
	}

	@Override
	public void setMaterial( EnumReinforced reinforcedMaterial )
	{
		material = reinforcedMaterial;
		markDirty();
	}
}
