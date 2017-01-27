package io.github.tehstoneman.betterstorage.tile;

import io.github.tehstoneman.betterstorage.item.tile.ItemLockable;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityReinforcedChest;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileReinforcedChest extends TileLockable
{
	public TileReinforcedChest( Material material )
	{
		super( material );

		setHardness( 8.0F );
		setResistance( 20.0F );
		// setStepSound(soundTypeWood);
		// setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);

		setHarvestLevel( "axe", 2 );
	}

	public TileReinforcedChest()
	{
		this( Material.WOOD );
	}

	@Override
	protected void registerBlock()
	{
		GameRegistry.register( this );
		GameRegistry.register( new ItemLockable( this ).setRegistryName( getRegistryName() ) );
	}

	@Override
	public boolean isOpaqueCube( IBlockState state )
	{
		return false;
	}

	@Override
	public boolean isFullCube( IBlockState state )
	{
		return false;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public EnumBlockRenderType getRenderType( IBlockState state )
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;// return ClientProxy.reinforcedChestRenderId;}
	}

	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	{
		final TileEntity tileEntity = worldIn.getTileEntity( pos );
		if( tileEntity instanceof TileEntityReinforcedChest )
		{
			final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileEntity;
			if( chest.getOrientation() != null )
				state = state.withProperty( FACING, chest.getOrientation() );
		}
		return state;
	}

	/*
	 * @Override
	 * public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos )
	 * {
	 * final TileEntityReinforcedChest chest = WorldUtils.get( world, pos.getX(), pos.getY(), pos.getZ(), TileEntityReinforcedChest.class );
	 * if( chest.isConnected() )
	 * {
	 * final EnumFacing connected = chest.getConnected();
	 * if( connected == EnumFacing.NORTH )
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0F, 0.9375F, 0.875F, 0.9375F );
	 * else
	 * if( connected == EnumFacing.SOUTH )
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 1.0F );
	 * else
	 * if( connected == EnumFacing.WEST )
	 * return new AxisAlignedBB( 0.0F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
	 * else
	 * if( connected == EnumFacing.EAST )
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 1.0F, 0.875F, 0.9375F );
	 * }
	 * return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F );
	 * }
	 */

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityReinforcedChest();
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( this ), 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	}
}
