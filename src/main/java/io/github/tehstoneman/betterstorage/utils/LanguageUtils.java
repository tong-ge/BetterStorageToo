package io.github.tehstoneman.betterstorage.utils;

import java.util.Arrays;
import java.util.List;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.client.resources.I18n;

public final class LanguageUtils
{
	private LanguageUtils()
	{}

	public static String translateTooltip( String thing, String... replacements )
	{
		if( thing == null )
			return null;
		if( replacements.length % 2 != 0 )
			throw new IllegalArgumentException( "replacements must contain an even number of elements." );
		String translated = I18n.format( "tooltip." + ModInfo.modId + "." + thing );
		for( int i = 0; i < replacements.length; i += 2 )
			translated = translated.replace( replacements[i], replacements[i + 1] );
		return translated;
	}

	public static void translateTooltip( List list, String thing, String... replacements )
	{
		if( thing == null )
			return;
		final String translated = translateTooltip( thing, replacements );
		list.addAll( Arrays.asList( translated.split( "#" ) ) );
	}
}
