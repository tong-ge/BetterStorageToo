package io.github.tehstoneman.betterstorage.common.inventory;

import net.minecraftforge.items.ItemStackHandler;

public class ExpandableStackHandler extends ItemStackHandler
{
	private final int	columns;
	private final int	rows;

	public ExpandableStackHandler( int columns, int rows )
	{
		super( columns * rows );
		this.columns = columns;
		this.rows = rows;
	}

	public int getColumns()
	{
		return columns;
	}

	public int getRows()
	{
		return rows;
	}
}
