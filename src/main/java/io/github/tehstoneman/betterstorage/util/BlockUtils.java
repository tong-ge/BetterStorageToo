package io.github.tehstoneman.betterstorage.util;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import io.github.tehstoneman.betterstorage.common.inventory.Region;
import net.minecraft.util.math.BlockPos;

public class BlockUtils
{
	/**
	 * Returns a list that contains all the {@link BlockPos} positions within the given box
	 *
	 * @param min
	 *            Minimum box corner
	 * @param max
	 *            Maximum box corner
	 * @return {@link BlockPos} list
	 */
	public static List< BlockPos > getAllInBox( BlockPos min, BlockPos max )
	{
		return StreamSupport.stream( BlockPos.betweenClosed( min, max ).spliterator(), true ).collect( Collectors.toList() );
		// return BlockPos.betweenClosed( min, max ).map( BlockPos::toImmutable ).collect( Collectors.toList() );
	}

	/**
	 * Returns a list that contains all the {@link BlockPos} positions within the given {@link Region}
	 *
	 * @param region
	 *            The {@link Region} to search in
	 * @return {@link BlockPos} list
	 */
	public static List< BlockPos > getAllInBox( Region region )
	{
		return getAllInBox( region.posMin, region.posMax );
	}
}
