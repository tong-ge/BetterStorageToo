package io.github.tehstoneman.betterstorage.common.tileentity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public class TileEntityTank extends TileEntity
{
	private final FluidTank						fluidTank		= new FluidTank( 1000 )
																{
																	@Override
																	protected void onContentsChanged()
																	{
																		TileEntityTank.this.markDirty();
																	}
																};
	private final LazyOptional< IFluidHandler >	fluidHandler	= LazyOptional.of( () -> fluidTank );

	public TileEntityTank()
	{
		super( BetterStorageTileEntityTypes.GLASS_TANK );
	}

	@Override
	public boolean hasFastRenderer()
	{
		return true;
	}

	@Override
	public <T> LazyOptional< T > getCapability( @Nonnull Capability< T > capability, @Nullable Direction side )
	{
		if( capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY )
			return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty( capability, fluidHandler );
		return super.getCapability( capability, side );
	}

	public FluidStack getFluid()
	{
		return fluidTank.getFluidInTank( 0 );
	}

	public int getCapacity()
	{
		return fluidTank.getTankCapacity( 0 );
	}

	public TileEntityTank getTankAt( BlockPos pos )
	{
		return (TileEntityTank)world.getTileEntity( pos );
	}

	public int getFluidAmountAbove()
	{
		final TileEntityTank tank = getTankAt( pos.up() );
		return tank != null ? tank.getFluid().getAmount() : 0;
	}

	public int getFluidAmountBelow()
	{
		final TileEntityTank tank = getTankAt( pos.down() );
		return tank != null ? tank.getFluid().getAmount() : 0;
	}

	/*
	 * ==========================
	 * TileEntity synchronization
	 * ==========================
	 */

	@Override
	public CompoundNBT getUpdateTag()
	{
		final CompoundNBT nbt = super.getUpdateTag();

		fluidTank.writeToNBT( nbt );

		/*
		 * if( hasCustomName() )
		 * nbt.putString( "CustomName", ITextComponent.Serializer.toJson( getCustomName() ) );
		 */

		return nbt;
	}

	@Override
	public void handleUpdateTag( CompoundNBT nbt )
	{
		super.handleUpdateTag( nbt );

		fluidTank.readFromNBT( nbt );

		/*
		 * if( nbt.contains( "CustomName" ) )
		 * setCustomName( ITextComponent.Serializer.fromJson( nbt.getString( "CustomName" ) ) );
		 */
	}

	@Override
	public CompoundNBT write( CompoundNBT nbt )
	{
		fluidTank.writeToNBT( nbt );

		/*
		 * if( hasCustomName() )
		 * nbt.putString( "CustomName", ITextComponent.Serializer.toJson( getCustomName() ) );
		 */

		return super.write( nbt );
	}

	@Override
	public void read( CompoundNBT nbt )
	{
		fluidTank.readFromNBT( nbt );

		/*
		 * if( nbt.contains( "CustomName" ) )
		 * setCustomName( ITextComponent.Serializer.fromJson( nbt.getString( "CustomName" ) ) );
		 */

		super.read( nbt );
	}
}
