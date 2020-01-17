package io.github.tehstoneman.betterstorage.common.inventory;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidTankHandler implements IFluidHandler
{
	protected Predicate< FluidStack >	validator;
	@Nonnull
	protected FluidStack				fluid	= FluidStack.EMPTY;
	protected int						capacity;

	public FluidTankHandler( int capacity )
	{
		this( capacity, e -> true );
	}

	public FluidTankHandler( int capacity, Predicate< FluidStack > validator )
	{
		this.capacity = capacity;
		this.validator = validator;
	}

	@Override
	public int getTanks()
	{
		return 1;
	}

	@Override
	public FluidStack getFluidInTank( int tank )
	{
		return fluid;
	}

	@Override
	public int getTankCapacity( int tank )
	{
		return capacity;
	}

	@Override
	public boolean isFluidValid( int tank, FluidStack stack )
	{
		return validator.test( stack );
	}

	@Override
	public int fill( FluidStack resource, FluidAction action )
	{
		if( resource.isEmpty() || !isFluidValid( 0, resource ) )
			return 0;
		if( action.simulate() )
		{
			if( fluid.isEmpty() )
				return Math.min( capacity, resource.getAmount() );
			if( !fluid.isFluidEqual( resource ) )
				return 0;
			return Math.min( capacity - fluid.getAmount(), resource.getAmount() );
		}
		if( fluid.isEmpty() )
		{
			fluid = new FluidStack( resource, Math.min( capacity, resource.getAmount() ) );
			onContentsChanged();
			return fluid.getAmount();
		}
		if( !fluid.isEmpty() && !fluid.isFluidEqual( resource ) )
			return 0;
		int filled = capacity - fluid.getAmount();

		if( resource.getAmount() < filled )
		{
			fluid.grow( resource.getAmount() );
			filled = resource.getAmount();
		}
		else
			fluid.setAmount( capacity );
		if( filled > 0 )
			onContentsChanged();
		return filled;
	}

	@Override
	public FluidStack drain( FluidStack resource, FluidAction action )
	{
		if( resource.isEmpty() || !resource.isFluidEqual( fluid ) )
			return FluidStack.EMPTY;
		return drain( resource.getAmount(), action );
	}

	@Override
	public FluidStack drain( int maxDrain, FluidAction action )
	{
		int drained = maxDrain;
		if( fluid.getAmount() < drained )
			drained = fluid.getAmount();
		final FluidStack stack = new FluidStack( fluid, drained );
		if( action.execute() && drained > 0 )
			fluid.shrink( drained );
		if( drained > 0 )
			onContentsChanged();
		return stack;
	}

	protected void onContentsChanged()
	{}

	public void setFluid( FluidStack stack )
	{
		fluid = stack;
	}

	public boolean isEmpty()
	{
		return fluid.isEmpty();
	}

	public int getSpace()
	{
		return Math.max( 0, capacity - fluid.getAmount() );
	}

	public int getFluidAmount()
	{
		return fluid.getAmount();
	}

	public FluidTankHandler readFromNBT( CompoundNBT nbt )
	{

		final FluidStack fluid = FluidStack.loadFluidStackFromNBT( nbt );
		setFluid( fluid );
		return this;
	}

	public CompoundNBT writeToNBT( CompoundNBT nbt )
	{

		fluid.writeToNBT( nbt );

		return nbt;
	}
}
