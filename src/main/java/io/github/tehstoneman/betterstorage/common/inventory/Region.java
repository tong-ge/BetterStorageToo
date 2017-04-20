package io.github.tehstoneman.betterstorage.common.inventory;

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

	/** Return the width (x-direction) of this region */
	public int width()
	{
		return posMax.getX() - posMin.getX() + 1;
	}

	/** Return the depth (z-direction) of this region */
	public int depth()
	{
		return posMax.getZ() - posMin.getZ() + 1;
	}

	/** Return the height (y-direction) of this region */
	public int height()
	{
		return posMax.getY() - posMin.getY() + 1;
	}

	/** Return the volume of this region */
	public int volume()
	{
		return width() * depth() * height();
	}

	/** Sets the area of this region */
	public void set( BlockPos posMin, BlockPos posMax )
	{
		//@formatter:off
		this.posMin = new BlockPos( Math.min( posMin.getX(), posMax.getX() ),
									Math.min( posMin.getY(), posMax.getY() ),
									Math.min( posMin.getZ(), posMax.getZ() ) );
		this.posMax = new BlockPos( Math.max( posMin.getX(), posMax.getX() ),
									Math.max( posMin.getY(), posMax.getY() ),
									Math.max( posMin.getZ(), posMax.getZ() ) );
		//@formatter:on
	}

	/** Expand the region by the given size */
	public void expand( BlockPos posMin, BlockPos posMax )
	{
		//@formatter:off
		this.posMin.subtract( new BlockPos( Math.min( posMin.getX(), posMax.getX() ),
											Math.min( posMin.getY(), posMax.getY() ),
											Math.min( posMin.getZ(), posMax.getZ() ) ) );
		this.posMax.add( new BlockPos(	Math.max( posMin.getX(), posMax.getX() ),
										Math.max( posMin.getY(), posMax.getY() ),
										Math.max( posMin.getZ(), posMax.getZ() ) ) );
		//@formatter:on
	}

	/** Expand the region by the given size */
	public void expand( int size )
	{
		expand( new BlockPos( size, size, size ), new BlockPos( size, size, size ) );
	}

	/** Expand the region to contain the given position */
	public void expandToContain( BlockPos pos )
	{
		posMin = new BlockPos( Math.min( posMin.getX(), pos.getX() ), Math.min( posMin.getY(), pos.getY() ), Math.min( posMin.getZ(), pos.getZ() ) );
		posMax = new BlockPos( Math.max( posMax.getX(), pos.getX() ), Math.max( posMax.getY(), pos.getY() ), Math.max( posMax.getZ(), pos.getZ() ) );
	}

	/** Expand the region to contain the given tile entity */
	public void expandToContain( TileEntity entity )
	{
		expandToContain( entity.getPos() );
	}

	/** Checks if the region contains the giving position */
	public boolean contains( BlockPos pos )
	{
		return pos.getX() >= posMin.getX() && pos.getY() >= posMin.getY() && pos.getZ() >= posMin.getZ() && pos.getX() <= posMax.getX()
				&& pos.getY() <= posMax.getY() && pos.getZ() <= posMax.getZ();
	}

	/** Checks if the region contains the giving tile entity */
	public boolean contains( TileEntity entity )
	{
		return contains( entity.getPos() );
	}

	/** Convert region to NBT data for serialization */
	public NBTTagCompound toCompound()
	{
		final NBTTagCompound compound = new NBTTagCompound();
		compound.setLong( "posMin", posMin.toLong() );
		compound.setLong( "posMax", posMax.toLong() );
		return compound;
	}

	/** Convert NBT data to region for deserialization */
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
