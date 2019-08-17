package io.github.tehstoneman.betterstorage.api;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;

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
}
