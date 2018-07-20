package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.property.Properties;

public class BlockReinforcedChest extends BlockLockable
{
	public BlockReinforcedChest( Material material )
	{
		super( "reinforced_chest", material );

		//@formatter:off
		setDefaultState( blockState.getBaseState().withProperty( BlockHorizontal.FACING, EnumFacing.NORTH )
												  .withProperty( MATERIAL, EnumReinforced.IRON )
												  .withProperty( Properties.StaticProperty, true )
												  .withProperty( CONNECTED, false ) );
		//@formatter:on

		setHardness( 8.0F );
		setResistance( 20.0F );

		setHarvestLevel( "axe", 2 );
		setSoundType( SoundType.WOOD );
	}

	public BlockReinforcedChest()
	{
		this( Material.WOOD );
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
	public EnumBlockRenderType getRenderType( IBlockState state )
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( !( tileEntity instanceof TileEntityReinforcedChest ) )
			return super.getBoundingBox( state, world, pos );

		final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileEntity;

		if( chest.isConnected() )
		{
			final EnumFacing facing = state.getValue( BlockHorizontal.FACING );
			if( chest.isMain() )
				switch( facing )
				{
				case NORTH:
				case SOUTH:
					return new AxisAlignedBB( 1F / 16F, 0F, 1F / 16F, 1F, 14F / 16F, 15F / 16F );
				case EAST:
				case WEST:
					return new AxisAlignedBB( 1F / 16F, 0F, 1F / 16F, 15F / 16F, 14F / 16F, 1F );
				default:
					break;
				}
			else
				switch( facing )
				{
				case NORTH:
				case SOUTH:
					return new AxisAlignedBB( 0F, 0F, 1F / 16F, 15F / 16F, 14F / 16F, 15F / 16F );
				case EAST:
				case WEST:
					return new AxisAlignedBB( 1F / 16F, 0F, 0F, 15F / 16F, 14F / 16F, 15F / 16F );
				default:
					break;
				}
		}

		return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 0.9375F );
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityReinforcedChest();
	}

	@Override
	public boolean onBlockActivated( World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ )
	{
		if( !world.isRemote )
		{
			final TileEntity tileentity = world.getTileEntity( pos );
			if( tileentity instanceof TileEntityReinforcedChest )
			{
				final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileentity;
				return chest.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
			}
		}
		return true;
	}

	@Override
	protected ItemBlock getItemBlock()
	{
		return new ItemBlockReinforcedChest( this );
	}
}
