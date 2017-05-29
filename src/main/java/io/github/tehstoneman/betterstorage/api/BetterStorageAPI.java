package io.github.tehstoneman.betterstorage.api;

import io.github.tehstoneman.betterstorage.api.internal.IMaterialRegistry;
import io.github.tehstoneman.betterstorage.api.internal.IUtils;

/**
 * The central point of access to all BetterStorageToo API calls
 * API version 1.0
 *
 * @author TehStoneMan
 *
 */
public class BetterStorageAPI
{
	/**
	 * Used to call Better Storage utilities
	 */
	public static IUtils utils;
	
	/**
	 * Access reinforced materal registry
	 */
	public static IMaterialRegistry materials;
}
