package io.github.tehstoneman.betterstorage.tile;

import java.util.Random;

import io.github.tehstoneman.betterstorage.common.block.BlockContainerBetterStorage;
import io.github.tehstoneman.betterstorage.item.tile.ItemPresent;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityPresent;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TilePresent extends TileContainerBetterStorage
{
	public TilePresent()
	{
		super( Material.CLOTH );
		setCreativeTab( null );

		setHardness( 0.75f );
		// setStepSound(soundTypeCloth);
		// setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public void registerBlockIcons(IIconRegister iconRegister) { }
	 */

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public IIcon getIcon(int side, int meta) {
	 * return Blocks.wool.getIcon(side, meta);
	 * }
	 */

	/*
	 * @Override
	 * public Class<? extends ItemBlock> getItemClass() { return ItemPresent.class; }
	 */

	@Override
	protected void registerBlock()
	{
		GameRegistry.register( this );
		GameRegistry.register( new ItemPresent( this ).setRegistryName( getRegistryName() ) );
	}

	@Override
	public boolean isOpaqueCube( IBlockState state )
	{
		return false;
	}

	@Override
	public boolean isNormalCube( IBlockState state )
	{
		return false;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public EnumBlockRenderType getRenderType( IBlockState state )
	{
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;// return ClientProxy.lockerRenderId;
	}

	@Override
	public int quantityDropped( Random rand )
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityPresent();
	}
}
