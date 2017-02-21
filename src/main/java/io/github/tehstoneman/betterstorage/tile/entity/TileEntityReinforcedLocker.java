package io.github.tehstoneman.betterstorage.tile.entity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityReinforcedLocker extends TileEntityLocker
{

	@Override
	@SideOnly( Side.CLIENT )
	public ResourceLocation getResource()
	{
		return getMaterial().getLockerResource( isConnected() );
	}

	@Override
	public boolean canHaveLock()
	{
		return true;
	}

	@Override
	public boolean canHaveMaterial()
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
		return BetterStorage.globalConfig.getInteger( GlobalConfig.reinforcedColumns );
	}

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerReinforcedLocker;
	}
}
