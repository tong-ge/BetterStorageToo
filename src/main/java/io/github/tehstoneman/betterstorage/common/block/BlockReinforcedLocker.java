package io.github.tehstoneman.betterstorage.common.block;

import javax.annotation.Nullable;

import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class BlockReinforcedLocker extends BlockLocker
{
	public BlockReinforcedLocker()
	{
		this( Block.Properties.create( Material.WOOD ).hardnessAndResistance( 5.0F, 6.0F ).sound( SoundType.WOOD ) );
	}

	public BlockReinforcedLocker( Properties properties )
	{
		super( properties );
	}

	@Override
	public TileEntity createTileEntity( BlockState state, IBlockReader world )
	{
		return new TileEntityReinforcedLocker();
	}

	@Override
	@Nullable
	public INamedContainerProvider getContainer( BlockState state, World worldIn, BlockPos pos )
	{
		final TileEntity tileentity = worldIn.getTileEntity( pos );
		if( !( tileentity instanceof TileEntityReinforcedLocker ) )
			return null;
		else
		{
			final TileEntityReinforcedLocker locker = (TileEntityReinforcedLocker)tileentity;
			return locker;
		}
	}

	@Override
	public boolean onBlockActivated( BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit )
	{
		if( hit.getFace() == state.get( FACING ) )
		{
			if( !worldIn.isRemote )
			{
				final TileEntityReinforcedLocker tileChest = getChestAt( worldIn, pos );
				if( tileChest != null && tileChest.isLocked() )
				{
					if( !tileChest.unlockWith( player.getHeldItem( hand ) ) )
						return false;
					if( player.isSneaking() )
					{
						worldIn.addEntity( new ItemEntity( worldIn, pos.getX(), pos.getY(), pos.getZ(), tileChest.getLock().copy() ) );
						tileChest.setLock( ItemStack.EMPTY );
						return true;
					}
				}
				return super.onBlockActivated( state, worldIn, pos, player, hand, hit );
			}
			return true;
		}
		return false;
	}

	@Nullable
	public static TileEntityReinforcedLocker getChestAt( World world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedLocker )
			return (TileEntityReinforcedLocker)tileEntity;
		return null;
	}
}
