package io.github.tehstoneman.betterstorage.misc;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class Region
{
	public BlockPos	posMin;
	public BlockPos	posMax;

	public Region( BlockPos posMin, BlockPos posMax )
	{
		set( posMin, posMax );
	}

	public Region( TileEntity entity )
	{
		this( entity.getPos(), entity.getPos() );
	}

	public int width()
	{
		return posMax.getX() - posMin.getX() + 1;
	}

	public int depth()
	{
		return posMax.getY() - posMin.getY() + 1;
	}

	public int height()
	{
		return posMax.getZ() - posMin.getZ() + 1;
	}

	public int volume()
	{
		return width() * depth() * height();
	}

	public void set( BlockPos posMin, BlockPos posMax )
	{
		this.posMin = posMin;
		this.posMax = posMax;
	}

	public void expand( BlockPos posMin, BlockPos posMax )
	{
		this.posMin.subtract( posMin );
		this.posMax.add( posMax );
	}

	public void expand( int size )
	{
		expand( new BlockPos( size, size, size ), new BlockPos( size, size, size ) );
	}

	public boolean contains( BlockPos pos )
	{
		return pos.getX() >= posMin.getX() && pos.getY() >= posMin.getY() && pos.getZ() >= posMin.getZ() && pos.getX() <= posMax.getX()
				&& pos.getY() <= posMax.getY() && pos.getZ() <= posMax.getZ();
	}

	public boolean contains( TileEntity entity )
	{
		return contains( entity.getPos() );
	}

	public void expandToContain( BlockPos pos )
	{
		posMin = new BlockPos( Math.min( posMin.getX(), pos.getX() ), Math.min( posMin.getY(), pos.getY() ), Math.min( posMin.getZ(), pos.getZ() ) );
		posMax = new BlockPos( Math.max( posMax.getX(), pos.getX() ), Math.max( posMax.getY(), pos.getY() ), Math.max( posMax.getZ(), pos.getZ() ) );
	}

	public void expandToContain( TileEntity entity )
	{
		expandToContain( entity.getPos() );
	}

	public NBTTagCompound toCompound()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setLong( "posMin", posMin.toLong() );
		compound.setLong( "posMax", posMax.toLong() );
		return compound;
	}

	public static Region fromCompound( NBTTagCompound compound )
	{
		final BlockPos posMin = BlockPos.fromLong( compound.getInteger( "posMin.getX()" ) );
		final BlockPos posMax = BlockPos.fromLong( compound.getInteger( "posMax.getX()" ) );
		return new Region( posMin, posMax );
	}

	@Override
	public Region clone()
	{
		return new Region( posMin, posMax );
	}

	@Override
	public String toString()
	{
		return "[ " + posMin.getX() + "," + posMin.getY() + "," + posMin.getZ() + " : " + posMax.getX() + "," + posMax.getY() + "," + posMax.getZ()
				+ " ]";
	}
}
