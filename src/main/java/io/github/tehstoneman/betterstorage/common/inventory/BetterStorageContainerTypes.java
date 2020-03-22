package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public final class BetterStorageContainerTypes
{
	public static final DeferredRegister< ContainerType< ? > >					REGISTERY			= new DeferredRegister<>(
			ForgeRegistries.CONTAINERS, ModInfo.MOD_ID );

	//@formatter:off
	public static RegistryObject< ContainerType< ContainerCardboardBox > >		CARDBOARD_BOX		= REGISTERY.register( "cardboard_box",
			() -> IForgeContainerType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerCardboardBox( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< ContainerType< ContainerCrate > >				CRATE				= REGISTERY.register( "crate",
			() -> IForgeContainerType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerCrate( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< ContainerType< ContainerKeyring > >			KEYRING				= REGISTERY.register( "keyring",
			() -> IForgeContainerType.create( ( windowID, inv, data ) ->
										{
											final ItemStack keyring = data.readItemStack();
											final int protectedIndex = data.readInt();
											return new ContainerKeyring( windowID, inv, keyring, protectedIndex );
										} ) );
	public static RegistryObject< ContainerType< ContainerLocker > >			LOCKER				= REGISTERY.register( "locker",
			() -> IForgeContainerType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerLocker( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< ContainerType< ContainerReinforcedChest > >	REINFORCED_CHEST	= REGISTERY.register( "reinforced_chest",
			() -> IForgeContainerType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerReinforcedChest( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< ContainerType< ContainerReinforcedLocker > >	REINFORCED_LOCKER	= REGISTERY.register( "reinforced_locker",
			() -> IForgeContainerType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerReinforcedLocker( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	//@formatter:on
}
