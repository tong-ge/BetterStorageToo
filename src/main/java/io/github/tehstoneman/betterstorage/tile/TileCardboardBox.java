package io.github.tehstoneman.betterstorage.tile;

import java.util.Random;

import io.github.tehstoneman.betterstorage.item.tile.ItemCardboardBox;
import io.github.tehstoneman.betterstorage.common.block.BlockContainerBetterStorage;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityCardboardBox;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class TileCardboardBox extends TileContainerBetterStorage
{

	// private IIcon sideIcon;

	public TileCardboardBox()
	{
		super( Material.WOOD );

		setHardness( 0.8f );
		// setStepSound(soundTypeWood);
		// setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
	}

	@Override
	protected void registerBlock()
	{
		GameRegistry.register( this );
		GameRegistry.register( new ItemCardboardBox( this ).setRegistryName( getRegistryName() ) );
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public void registerBlockIcons(IIconRegister iconRegister) {
	 * blockIcon = iconRegister.registerIcon(Constants.modId + ":" + getTileName());
	 * sideIcon = iconRegister.registerIcon(Constants.modId + ":" + getTileName() + "_side");
	 * }
	 */

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public IIcon getIcon(int side, int meta) {
	 * return ((side < 2) ? blockIcon : sideIcon);
	 * }
	 */

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public int colorMultiplier(IBlockAccess world, int x, int y, int z) {
	 * TileEntityCardboardBox box = WorldUtils.get(world, x, y, z, TileEntityCardboardBox.class);
	 * return (((box != null) && (box.color >= 0)) ? box.color : 0x705030);
	 * }
	 */

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
	public int quantityDropped( Random rand )
	{
		return 0;
	}

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityCardboardBox();
	}
}
