package io.github.tehstoneman.betterstorage.common.fluid;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraft.fluid.Fluid;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BetterStorageFluids
{
	public static final DeferredRegister< Fluid >		REGISTERY		= DeferredRegister.create( ForgeRegistries.FLUIDS, ModInfo.MOD_ID );

	/*public static RegistryObject< ForgeFlowingFluid >	MILK			= REGISTERY.register( "milk",
			() -> new ForgeFlowingFluid.Source( FluidMilk.PROPERTIES ) );
	public static RegistryObject< ForgeFlowingFluid >	FLOWING_MILK	= REGISTERY.register( "flowing_milk",
			() -> new ForgeFlowingFluid.Flowing( FluidMilk.PROPERTIES ) );*/
}
