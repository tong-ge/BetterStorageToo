package io.github.tehstoneman.betterstorage.common.block;

import java.util.logging.Logger;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ChunkCache;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockReinforcedChest extends BlockContainerBetterStorage // BlockLockable
{
	public static final PropertyDirection	FACING		= BlockHorizontal.FACING;
	public static final PropertyEnum		MATERIAL	= PropertyEnum.create( "material", EnumReinforced.class );

	public BlockReinforcedChest( Material material )
	{
		super( "reinforced_chest", material );

		setHardness( 8.0F );
		setResistance( 20.0F );

		setHarvestLevel( "axe", 2 );
		setSoundType( SoundType.WOOD );

		setDefaultState( blockState.getBaseState().withProperty( FACING, EnumFacing.NORTH ).withProperty( MATERIAL, EnumReinforced.IRON ) );
	}

	public BlockReinforcedChest()
	{
		this( Material.WOOD );
	}

	@Override
	protected ExtendedBlockState createBlockState()
	{
		return new ExtendedBlockState( this, new IProperty[] { FACING, MATERIAL, Properties.StaticProperty },
											 new IUnlistedProperty[] { Properties.AnimationProperty } );
	}

	@Override
	public void getSubBlocks( Item item, CreativeTabs tab, NonNullList< ItemStack > list )
	{
		for( final EnumReinforced material : EnumReinforced.values() )
		{
			final ItemStack itemstack = new ItemStack( item );
			final NBTTagCompound compound = itemstack.hasTagCompound() ? itemstack.getTagCompound() : new NBTTagCompound();
			compound.setString( "Material", material.getName() );
			itemstack.setTagCompound( compound );
			list.add( itemstack );
		}
	}

	@Override
	public IBlockState getStateFromMeta( int meta )
	{
		EnumFacing enumfacing = EnumFacing.getFront( meta );

		if( enumfacing.getAxis() == EnumFacing.Axis.Y )
			enumfacing = EnumFacing.NORTH;

		return getDefaultState().withProperty( FACING, enumfacing );
	}

	@Override
	public int getMetaFromState( IBlockState state )
	{
		return state.getValue( FACING ).getIndex();
	}

	@Override
	public IBlockState getActualState( IBlockState state, IBlockAccess worldIn, BlockPos pos )
	{
		final TileEntity tileentity = worldIn instanceof ChunkCache ? ( (ChunkCache)worldIn ).getTileEntity( pos, Chunk.EnumCreateEntityType.CHECK )
				: worldIn.getTileEntity( pos );

		if( tileentity instanceof TileEntityReinforcedChest )
		{
			final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileentity;
			if( chest.getMaterial() != null )
				state = state.withProperty( MATERIAL, chest.getMaterial() );
		}
		return state.withProperty( Properties.StaticProperty, true );
	}

	@Override
	public void onBlockPlacedBy( World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack )
	{
		worldIn.setBlockState( pos, state.withProperty( FACING, placer.getHorizontalFacing().getOpposite() ), 2 );

		if( stack.hasDisplayName() )
		{
			final TileEntity tileentity = worldIn.getTileEntity( pos );

			if( tileentity instanceof TileEntityReinforcedChest )
			{
				final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileentity;
				chest.setCustomInventoryName( stack.getDisplayName() );
				chest.setMaterial( EnumReinforced.IRON );
			}
		}
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
	public AxisAlignedBB getBoundingBox( IBlockState state, IBlockAccess world, BlockPos pos )
	{
		final TileEntity tileEntity = world.getTileEntity( pos );
		if( !( tileEntity instanceof TileEntityReinforcedChest ) )
			return super.getBoundingBox( state, world, pos );

		final TileEntityReinforcedChest chest = (TileEntityReinforcedChest)tileEntity;
		if( chest.isConnected() )
		{
			final EnumFacing connected = chest.getConnected();
			if( connected == EnumFacing.NORTH )
				return new AxisAlignedBB( 0.0625F, 0.0F, 0.0F, 0.9375F, (14F/16F), 0.9375F );
			else
				if( connected == EnumFacing.SOUTH )
					return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, (14F/16F), 1.0F );
				else
					if( connected == EnumFacing.WEST )
						return new AxisAlignedBB( 0.0F, 0.0F, 0.0625F, 0.9375F, (14F/16F), 0.9375F );
					else
						if( connected == EnumFacing.EAST )
							return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 1.0F, (14F/16F), 0.9375F );
		}

		return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, (14F/16F), 0.9375F );
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
				chest.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
			}
		}
		return true;
	}

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void registerItemModels()
	 * {
	 * for( final EnumReinforced material : EnumReinforced.values() )
	 * //if( material != EnumReinforced.SPECIAL )
	 * ModelLoader.setCustomModelResourceLocation( Item.getItemFromBlock( this ), material.getMetadata(),
	 * new ModelResourceLocation( getRegistryName() + "_" + material.getName(), "inventory" ) );
	 * }
	 */

	@Override
	protected ItemBlock getItemBlock()
	{
		return new ItemBlockReinforcedChest( this );
	}
}
