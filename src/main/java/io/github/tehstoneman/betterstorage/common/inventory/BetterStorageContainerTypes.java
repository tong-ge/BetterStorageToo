package io.github.tehstoneman.betterstorage.common.inventory;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class BetterStorageContainerTypes
{
	public static final DeferredRegister< MenuType< ? > >					REGISTERY			= DeferredRegister.create( ForgeRegistries.CONTAINERS,
			ModInfo.MOD_ID );

	//@formatter:off
	public static RegistryObject< MenuType< ContainerCardboardBox > >		CARDBOARD_BOX		= REGISTERY.register( "cardboard_box",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerCardboardBox( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< MenuType< ContainerCrate > >				CRATE				= REGISTERY.register( "crate",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerCrate( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< MenuType< ContainerKeyring > >			KEYRING				= REGISTERY.register( "keyring",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final ItemStack keyring = data.readItem();
											final int protectedIndex = data.readInt();
											return new ContainerKeyring( windowID, inv, keyring, protectedIndex );
										} ) );
	public static RegistryObject< MenuType< ContainerLocker > >			LOCKER				= REGISTERY.register( "locker",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerLocker( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< MenuType< ReinforcedChestContainer > >	REINFORCED_CHEST	= REGISTERY.register( "reinforced_chest",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ReinforcedChestContainer( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< MenuType< ContainerReinforcedLocker > >	REINFORCED_LOCKER	= REGISTERY.register( "reinforced_locker",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ContainerReinforcedLocker( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	public static RegistryObject< MenuType< ConfigContainer > >			CONFIG	= REGISTERY.register( "config_container",
			() -> IForgeMenuType.create( ( windowID, inv, data ) ->
										{
											final BlockPos pos = data.readBlockPos();
											return new ConfigContainer( windowID, inv, BetterStorage.PROXY.getClientWorld(), pos );
										} ) );
	//@formatter:on
}
