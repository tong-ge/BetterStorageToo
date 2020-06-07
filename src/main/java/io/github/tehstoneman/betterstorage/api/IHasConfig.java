package io.github.tehstoneman.betterstorage.api;

import io.github.tehstoneman.betterstorage.common.item.HexKeyItem;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.tileentity.TileEntity;

/**
 * Interface for a {@link TileEntity} that can be configured through the use of a {@link HexKeyItem}
 *
 * @author TehStoneMan
 *
 */
public interface IHasConfig
{
	/**
	 * Get the configuration of this {@link TileEntity}
	 *
	 * @return the {@link HexKeyConfig} of this {@link TileEntity}
	 */
	public HexKeyConfig getConfig();

	/**
	 * Sets the {@link HexKeyConfig} of this {@link TileEntity}
	 *
	 * @param config
	 *            The {@link HexKeyConfig} to set.
	 */
	public void setConfig( HexKeyConfig config );
}
