package io.github.tehstoneman.betterstorage.common.world.storage;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import io.github.tehstoneman.betterstorage.api.ICrateStorage;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import net.minecraft.nbt.CompoundNBT;

/** Holds all CratePile objects for one world / dimension. */
public class CrateStorage implements ICrateStorage
{
	private final Map< UUID, CrateStackHandler > pileDataMap = new HashMap<>();

	public CrateStorage()
	{}

	/** Get the stack handler for the associated ID */
	@Override
	public CrateStackHandler getCratePile( UUID pileID )
	{
		if( pileID != null && pileDataMap.containsKey( pileID ) )
			return pileDataMap.get( pileID );
		return createCratePile();
	}

	@Override
	public CrateStackHandler getOrCreateCratePile( UUID pileID )
	{
		if( !pileDataMap.containsKey( pileID ) )
		{
			final CrateStackHandler cratePile = new CrateStackHandler( 18 );
			pileDataMap.put( pileID, cratePile );
			// cratePile.setPileID( pileID );
		}
		return pileDataMap.get( pileID );
	}

	@Override
	public CrateStackHandler createCratePile()
	{
		UUID pileID = UUID.randomUUID();
		while( pileDataMap.containsKey( pileID ) )
			pileID = UUID.randomUUID();
		return addCrateToPile( pileID );
	}

	@Override
	public CrateStackHandler addCrateToPile( UUID pileID )
	{
		final CrateStackHandler cratePile = new CrateStackHandler( 18 );
		pileDataMap.put( pileID, cratePile );
		// cratePile.setPileID( pileID );
		// markDirty();
		return cratePile;
	}

	@Override
	public void removeCratePile( UUID pileID )
	{
		pileDataMap.remove( pileID );
		// markDirty();
	}

	@Override
	public CompoundNBT serializeNBT()
	{
		final CompoundNBT compound = new CompoundNBT();
		if( !pileDataMap.isEmpty() )
			for( final Map.Entry< UUID, CrateStackHandler > entry : pileDataMap.entrySet() )
				compound.put( entry.getKey().toString(), entry.getValue().serializeNBT() );
		return compound;
	}

	@Override
	public void deserializeNBT( CompoundNBT compound )
	{
		if( !compound.isEmpty() )
			for( final String key : compound.keySet() )
			{
				final CrateStackHandler crateStackHandler = new CrateStackHandler( 18 );
				crateStackHandler.deserializeNBT( compound.getCompound( key ) );
				pileDataMap.put( UUID.fromString( key ), crateStackHandler );
			}
	}
}
