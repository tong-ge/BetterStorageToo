package io.github.tehstoneman.betterstorage.api;

import io.github.tehstoneman.betterstorage.common.item.HexKeyItem;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * Interface for a {@link BlockEntity} that can be configured through the use of a {@link HexKeyItem}
 *
 * @author TehStoneMan
 *
 */
public interface IHasConfig
{
	/**
	 * Get the configuration of this {@link BlockEntity}
	 *
	 * @return the {@link HexKeyConfig} of this {@link BlockEntity}
	 */
	public HexKeyConfig getConfig();

	/**
	 * Sets the {@link HexKeyConfig} of this {@link BlockEntity}
	 *
	 * @param config
	 *            The {@link HexKeyConfig} to set.
	 */
	public void setConfig( HexKeyConfig config );
}
