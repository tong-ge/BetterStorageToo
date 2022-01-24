package io.github.tehstoneman.betterstorage.common.capabilities;

import io.github.tehstoneman.betterstorage.api.ICrateStorage;
import io.github.tehstoneman.betterstorage.common.world.storage.CrateStorage;
import io.github.tehstoneman.betterstorage.util.BetterStorageResource;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityCrate
{
	// @CapabilityInject( ICrateStorage.class )
	public static final Capability< ICrateStorage >	CRATE_PILE_CAPABILITY	= null;

	public static ResourceLocation					CAPABILITY_RESOURCE		= new BetterStorageResource( "crate_pile" );

	public static void register()
	{
		/*
		 * CapabilityManager.INSTANCE.register( ICrateStorage.class, new IStorage< ICrateStorage >()
		 * {
		 *
		 * @Override
		 * public INBT writeNBT( Capability< ICrateStorage > capability, ICrateStorage instance, Direction side )
		 * {
		 * return instance.serializeNBT();
		 * }
		 *
		 * @Override
		 * public void readNBT( Capability< ICrateStorage > capability, ICrateStorage instance, Direction side, INBT nbt )
		 * {
		 * if( !( instance instanceof CrateStorage ) )
		 * throw new IllegalArgumentException( "Can not deserialize to an instance that isn't the default implementation" );
		 * instance.deserializeNBT( (CompoundTag)nbt );
		 * }
		 * }, () -> new CrateStorage() );
		 */
	}

	public static class Provider implements ICapabilitySerializable< CompoundTag >
	{
		public ICrateStorage						crateStorage;
		private final LazyOptional< ICrateStorage >	capabilityHandler	= LazyOptional.of( () -> crateStorage );

		public Provider()
		{
			crateStorage = new CrateStorage();
			// this.capability = capability;
			// capabilityHandler = LazyOptional.of( () -> this.capability );
		}

		@Override
		public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction side )
		{
			return CRATE_PILE_CAPABILITY.orEmpty( capability, capabilityHandler );
			// return LazyOptional.empty();
		}

		@Override
		public CompoundTag serializeNBT()
		{
			return crateStorage.serializeNBT();
		}

		@Override
		public void deserializeNBT( CompoundTag nbt )
		{
			crateStorage.deserializeNBT( nbt );
		}

	}
}
