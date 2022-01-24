package io.github.tehstoneman.betterstorage.api;

import java.util.UUID;

import io.github.tehstoneman.betterstorage.common.capabilities.CapabilityCrate;
import io.github.tehstoneman.betterstorage.common.inventory.CrateStackHandler;
import io.github.tehstoneman.betterstorage.common.world.storage.CrateStorage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

/**
 * Interface that describes the storage of crate piles.
 *
 * @author TehStoneMan
 *
 */
public interface ICrateStorage extends INBTSerializable< CompoundTag >
{
	/**
	 * Get the stack handler for the associated ID.
	 *
	 * @param pileID
	 *            The {@link UUID} of the pile to get.
	 * @return the {@link CrateStackHandler} for the associated crate pile.
	 */
	public CrateStackHandler getCratePile( UUID pileID );

	/**
	 * Get or create a crate handler for the given {@link UUID}. Used for syncing client with server.
	 *
	 * @param pileID
	 *            The {@link UUID} of the pile to get.
	 * @return the {@link CrateStackHandler} for the associated crate pile.
	 */
	public CrateStackHandler computeIfAbsentCratePile( UUID pileID );

	/**
	 * Creates and adds a new crate to this collection.
	 *
	 * @return a new {@link CrateStackHandler}.
	 */
	public CrateStackHandler createCratePile();

	/**
	 * Adds a new crate pile to this collection.
	 *
	 * @param pileID
	 *            The {@link UUID} of the pile to add.
	 * @return the {@link CrateStackHandler} associated with the new crate pile.
	 */
	public CrateStackHandler addCrateToPile( UUID pileID );

	/**
	 * Removes the crate pile from the collection, deletes the pile's file.
	 *
	 * @param pileID
	 *            The {@link UUID} of the pile to remove.
	 */
	public void removeCratePile( UUID pileID );

	/**
	 * Retrieve the crate storage for the given {@link Level}.
	 *
	 * @param world
	 *            The {@link Level} to get the storage for.
	 * @return the {@link ICrateStorage} for the given {@link Level}.
	 */
	public static ICrateStorage getCrateStorage( Level world )
	{
		final LazyOptional< ICrateStorage > capability = world.getCapability( CapabilityCrate.CRATE_PILE_CAPABILITY );
		final ICrateStorage crateStorage = capability.orElse( new CrateStorage() );
		return crateStorage;
	}
}
