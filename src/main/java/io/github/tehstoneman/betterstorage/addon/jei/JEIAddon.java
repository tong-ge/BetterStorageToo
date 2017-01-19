package io.github.tehstoneman.betterstorage.addon.jei;

import io.github.tehstoneman.betterstorage.addon.Addon;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

@JEIPlugin
public class JEIAddon extends Addon implements IModPlugin
{
	public JEIAddon()
	{
		super( "JustEnoughItems" );
	}

	@Override
	public void register( IModRegistry registry )
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onRuntimeAvailable( IJeiRuntime jeiRuntime )
	{
		// TODO Auto-generated method stub
	}
}
