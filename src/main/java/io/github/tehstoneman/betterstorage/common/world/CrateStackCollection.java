package io.github.tehstoneman.betterstorage.common.world;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

/** Holds all CratePileData objects for one world / dimension. */
public class CrateStackCollection extends SavedData
{
	private static final String						filename	= ModInfo.MOD_ID + "_cratepile";

	private final Map< UUID, CrateStackHandler >	pileDataMap	= new HashMap<>();

	/*
	 * public CrateStackCollection()
	 * {
	 * this( filename );
	 * }
	 *
	 * public CrateStackCollection( String filename )
	 * {
	 * super( filename );
	 * }
	 */

	/**
	 * Gets or creates a CratePileCollection for a world.
	 *
	 * @param world
	 *            The world that the collection is stored in
	 * @return The collection for the world, or null if client
	 */
	@Nullable
	public static CrateStackCollection getCollection( Level world )
	{
		if( world.isClientSide )
			return null;

		// final DimensionType dimType = world.getDimension().getType();
		final ResourceKey< Level > dimType = world.dimension();
		final DimensionDataStorage manager = world.getServer().getLevel( dimType ).getDataStorage();
		// final SavedData data = manager.computeIfAbsent( CrateStackCollection::new, filename );

		// return (CrateStackCollection)data;
		return null;
	}

	/**
	 * Get or create a {@link CrateStackHandler} for the given {@link UUID}
	 *
	 * @param pileID
	 *            {@link UUID} for the stack handler we are looking for<BR>
	 *            (Null to create new ID)
	 * @return The {@link CrateStackHandler} for the given {@link UUID}
	 */
	public CrateStackHandler computeIfAbsentCratePile( @Nullable UUID pileID )
	{
		// Check if pileID is null, and create a new ID if so
		if( pileID == null )
			do
				pileID = UUID.randomUUID();
			while( pileDataMap.containsKey( pileID ) );

		// Check if pileID is in collection, and create a new handler if not
		if( !pileDataMap.containsKey( pileID ) )
		{
			final CrateStackHandler cratePile = new CrateStackHandler( 18 );
			pileDataMap.put( pileID, cratePile );
			return addCrateToPile( pileID );
		}

		return pileDataMap.get( pileID );
	}

	/**
	 * Creates and adds a new crate to this collection.
	 *
	 * @return The new {@link CrateStackHandler}
	 */
	public CrateStackHandler createCratePile()
	{
		UUID pileID = UUID.randomUUID();
		while( pileDataMap.containsKey( pileID ) )
			pileID = UUID.randomUUID();
		return addCrateToPile( pileID );
	}

	/**
	 * Adds a new crate pile to this collection.
	 *
	 * @param pileID
	 *            The ID to add to this collection
	 * @return The new {@link CrateStackHandler}
	 */
	public CrateStackHandler addCrateToPile( UUID pileID )
	{
		final CrateStackHandler cratePile = new CrateStackHandler( 18 );
		cratePile.setPileID( pileID );
		pileDataMap.put( pileID, cratePile );
		setDirty();
		return cratePile;
	}

	/**
	 * Removes the crate pile from the collection, deletes the pile's file.
	 *
	 * @param pileID
	 *            The ID to delete
	 */
	public void removeCratePile( UUID pileID )
	{
		pileDataMap.remove( pileID );
		setDirty();
	}

	// @Override
	public void load( CompoundTag nbt )
	{
		if( !nbt.isEmpty() )
			for( final String key : nbt.getAllKeys() )
			{
				final CrateStackHandler crateStackHandler = new CrateStackHandler( 0 );
				crateStackHandler.deserializeNBT( nbt.getCompound( key ) );
				pileDataMap.put( UUID.fromString( key ), crateStackHandler );
			}
	}

	@Override
	public CompoundTag save( CompoundTag nbt )
	{
		if( !pileDataMap.isEmpty() )
			for( final Map.Entry< UUID, CrateStackHandler > entry : pileDataMap.entrySet() )
				nbt.put( entry.getKey().toString(), entry.getValue().serializeNBT() );
		return nbt;
	}
}
