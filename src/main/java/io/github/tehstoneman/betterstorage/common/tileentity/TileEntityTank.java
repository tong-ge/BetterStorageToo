package io.github.tehstoneman.betterstorage.common.tileentity;

import java.util.ArrayList;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.block.BlockTank;
import io.github.tehstoneman.betterstorage.common.inventory.FluidTankHandler;
import io.github.tehstoneman.betterstorage.common.inventory.StackedTankHandler;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TileEntityTank extends TileEntity
{
	private final FluidTankHandler				fluidTank		= new FluidTankHandler( BetterStorageConfig.COMMON.tankBuckets.get() * 1000 )
																{
																	@Override
																	protected void onContentsChanged()
																	{
																		TileEntityTank.this.markDirty();
																	}
																};
	private final LazyOptional< IFluidHandler >	fluidHandler	= LazyOptional.of( () -> fluidTank );

	private StackedTankHandler					stackedTank;
	private final LazyOptional< IFluidHandler >	stackedHandler	= LazyOptional.of( () -> stackedTank );
	private BlockPos							mainPos			= BlockPos.ZERO;
	private int									tankCount;

	public TileEntityTank()
	{
		super( BetterStorageTileEntityTypes.GLASS_TANK.get() );
		tankCount = 1;
	}

	@Override
	public <T> LazyOptional< T > getCapability( @Nonnull Capability< T > capability, @Nullable Direction side )
	{
		if( capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY )
			if( isMain() )
				if( isStacked() )
				{
					stackedTank = new StackedTankHandler( getStackedTanks() );
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty( capability, stackedHandler );
				}
				else
					return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.orEmpty( capability, fluidHandler );
			else
				return getMain().getCapability( capability, side );
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

	public boolean isMain()
	{
		return !getBlockState().get( BlockTank.DOWN );
	}

	public TileEntityTank getMain()
	{
		if( mainPos.equals( BlockPos.ZERO ) )
		{
			if( isMain() )
				return this;
			final TileEntityTank mainTank = getTankAt( pos.down() ).getMain();
			mainPos = mainTank.getPos();
			return mainTank;
		}
		else
			return getTankAt( mainPos );
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

	public boolean isStacked()
	{
		final BlockState state = getBlockState();
		return state.get( BlockTank.UP ) || state.get( BlockTank.DOWN );
	}

	public FluidTankHandler getHandler()
	{
		return fluidTank;
	}

	public ArrayList< FluidTankHandler > getStackedTanks()
	{
		final ArrayList< FluidTankHandler > tanks = new ArrayList();
		BlockPos tankPos = pos;
		while( world.getBlockState( tankPos ).getBlock() instanceof BlockTank )
		{
			tanks.add( getTankAt( tankPos ).getHandler() );
			tankPos = tankPos.up();
		}
		return tanks;
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

		return nbt;
	}

	@Override
	public void handleUpdateTag( CompoundNBT nbt )
	{
		super.handleUpdateTag( nbt );

		fluidTank.readFromNBT( nbt );
	}

	@Override
	public CompoundNBT write( CompoundNBT nbt )
	{
		fluidTank.writeToNBT( nbt );
		nbt.putInt( "tankCount", tankCount );

		if( !mainPos.equals( BlockPos.ZERO ) )
			nbt.putLong( "mainPos", mainPos.toLong() );

		return super.write( nbt );
	}

	@Override
	public void read( CompoundNBT nbt )
	{
		fluidTank.readFromNBT( nbt );
		tankCount = nbt.getInt( "tankCount" );

		if( nbt.contains( "mainPos" ) )
			mainPos = BlockPos.fromLong( nbt.getLong( "mainPos" ) );

		super.read( nbt );
	}
}
