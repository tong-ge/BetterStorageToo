package io.github.tehstoneman.betterstorage.common.capabilities;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.IHexKeyConfig;
import io.github.tehstoneman.betterstorage.common.world.storage.HexKeyConfig;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class CapabilityConfig
{
	@CapabilityInject( IHexKeyConfig.class )
	public static final Capability< IHexKeyConfig >	CONFIG_CAPABILITY	= null;

	public static ResourceLocation					CAPABILITY_RESOURCE	= new ResourceLocation( ModInfo.MOD_ID, "config" );

	public static void register()
	{
		CapabilityManager.INSTANCE.register( IHexKeyConfig.class, new Capability.IStorage< IHexKeyConfig >()
		{
			@Override
			public INBT writeNBT( Capability< IHexKeyConfig > capability, IHexKeyConfig instance, Direction side )
			{
				final ListNBT nbtTagList = new ListNBT();
				final int size = instance.getSlots();
				for( int i = 0; i < size; i++ )
				{
					final ItemStack stack = instance.getStackInSlot( i );
					if( !stack.isEmpty() )
					{
						final CompoundNBT itemTag = new CompoundNBT();
						itemTag.putInt( "Slot", i );
						stack.save( itemTag );
						nbtTagList.add( itemTag );
					}
				}
				return nbtTagList;
			}

			@Override
			public void readNBT( Capability< IHexKeyConfig > capability, IHexKeyConfig instance, Direction side, INBT nbt )
			{
				final ListNBT tagList = (ListNBT)nbt;
				for( int i = 0; i < tagList.size(); i++ )
				{
					final CompoundNBT itemTags = tagList.getCompound( i );
					final int j = itemTags.getInt( "Slot" );

					if( j >= 0 && j < instance.getSlots() )
						instance.setStackInSlot( j, ItemStack.of( itemTags ) );
				}
			}
		}, HexKeyConfig::new );
	}

	public static class Provider implements ICapabilitySerializable< CompoundNBT >
	{
		public IHexKeyConfig hexKeyConfig;
		private final LazyOptional< IHexKeyConfig >	capabilityHandler = LazyOptional.of( ()->hexKeyConfig );

		public Provider()
		{
			hexKeyConfig = new HexKeyConfig();
		}

		@Override
		public <T> LazyOptional< T > getCapability( Capability< T > capability, Direction side )
		{
			return CONFIG_CAPABILITY.orEmpty( capability, capabilityHandler );
		}

		@Override
		public CompoundNBT serializeNBT()
		{
			return hexKeyConfig.serializeNBT();
		}

		@Override
		public void deserializeNBT( CompoundNBT nbt )
		{
			hexKeyConfig.deserializeNBT( nbt );
		}
	}
}
