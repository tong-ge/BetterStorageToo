package io.github.tehstoneman.betterstorage.api;

import net.minecraft.util.IStringSerializable;

public enum EnumConnectedType implements IStringSerializable
{
	SINGLE( "single", 0 ), MASTER( "master", 2 ), SLAVE( "slave", 1 );

	public static final EnumConnectedType[]	VALUES	= values();
	private final String					name;
	private final int						opposite;

	private EnumConnectedType( String name, int oppositeIn )
	{
		this.name = name;
		opposite = oppositeIn;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public EnumConnectedType opposite()
	{
		return VALUES[opposite];
	}
}
