package io.github.tehstoneman.betterstorage.api;

import net.minecraft.util.IStringSerializable;

public enum EnumLockerType implements IStringSerializable
{
	SINGLE( "single", 0 ), BOTTOM( "bottom", 2 ), TOP( "top", 1 );

	public static final EnumLockerType[]	VALUES	= values();
	private final String					name;
	private final int						opposite;

	private EnumLockerType( String name, int oppositeIn )
	{
		this.name = name;
		opposite = oppositeIn;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public EnumLockerType opposite()
	{
		return VALUES[opposite];
	}
}
