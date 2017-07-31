package io.github.tehstoneman.betterstorage.api;

import java.util.HashMap;
import java.util.Map;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.util.IStringSerializable;

public enum EnumReinforced implements IStringSerializable
{
	//@formatter:off
	IRON	( 0, "iron", "ingotIron", "blockIron" ),
	GOLD	( 1, "gold", "ingotGold", "blockGold" ),
	DIAMOND	( 2, "diamond", "gemDiamond", "blockDiamond" ),
	EMERALD	( 3, "emerald", "gemEmerald", "blockEmerald" ),
	//SPECIAL	( "special", "",""),
	COPPER	( 4, "copper", "ingotCopper", "blockCopper" ),
	TIN		( 5, "tin", "ingotTin", "blockTin" ),
	SILVER	( 6, "silver", "ingotSilver", "blockSilver" ),
	ZINC	( 7, "zinc", "ingotZinc", "blockZinc" ),
	STEEL	( 8, "steel", "ingotSteel", "blockSteel" );
	//@formatter:on

	private final int								meta;
	private final String							name;
	private final String							ingot;
	private final String							block;

	private static EnumReinforced[]					META_LOOKUP	= new EnumReinforced[values().length];
	private static Map< String, EnumReinforced >	NAME_LOOKUP	= new HashMap< >();

	static
	{
		for( final EnumReinforced material : values() )
		{
			META_LOOKUP[material.getMetadata()] = material;
			NAME_LOOKUP.put( material.name, material );
		}
	}

	private EnumReinforced( int meta, String name, String ingot, String block )
	{
		this.meta = meta;
		this.name = name;
		this.ingot = ingot;
		this.block = block;
	}

	@Override
	public String toString()
	{
		return name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	public int getMetadata()
	{
		return meta;
	}

	public String getOreDictIngot()
	{
		return ingot;
	}

	public String getOreDictBlock()
	{
		return block;
	}

	public String getUnlocalizedName()
	{
		return "material." + ModInfo.modId + "." + name;
	}

	public static EnumReinforced byMetadata( int meta )
	{
		return META_LOOKUP[meta];
	}

	public static EnumReinforced byName( String name )
	{
		return NAME_LOOKUP.get( name );
	}
}
