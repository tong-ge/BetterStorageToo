package io.github.tehstoneman.betterstorage.api;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityCrate;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.world.storage.CrateStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ICrateStorage extends INBTSerializable< CompoundNBT >
{
	/** Get the stack handler for the associated ID */
	public CrateStackHandler getCratePile( UUID pileID );

	/**
	 * Get or create a crate handler for the given UUID
	 * Used for syncing client with server
	 */
	public CrateStackHandler getOrCreateCratePile( UUID pileID );

	/** Creates and adds a new crate to this collection. */
	public CrateStackHandler createCratePile();

	/** Adds a new crate pile to this collection. */
	public CrateStackHandler addCrateToPile( UUID pileID );

	/** Removes the crate pile from the collection, deletes the pile's file. */
	public void removeCratePile( UUID pileID );

	/** Retrive the crate storage for the given world */
	public static ICrateStorage getCrateStorage( World world )
	{
		final LazyOptional< ICrateStorage > capability = world.getCapability( CapabilityCrate.CRATE_PILE_CAPABILITY );
		final ICrateStorage crateStorage = capability.orElse( new CrateStorage() );
		return crateStorage;
	}
}
