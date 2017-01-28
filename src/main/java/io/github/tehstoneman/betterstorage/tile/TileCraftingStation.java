package io.github.tehstoneman.betterstorage.tile;

import io.github.tehstoneman.betterstorage.tile.entity.TileEntityCraftingStation;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileCraftingStation extends TileContainerBetterStorage
{

	// private IIcon topIcon;
	// private IIcon bottomIconDisabled;
	// private IIcon bottomIconEnabled;

	public TileCraftingStation()
	{
		super( Material.IRON );

		setHardness( 1.5f );
		// setStepSound(soundTypeStone);
	}

	@Override
	public boolean isNormalCube( IBlockState state )
	{
		return false;
	}

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public void registerBlockIcons(IIconRegister iconRegister) {
	 * blockIcon = iconRegister.registerIcon(Constants.modId + ":" + getTileName());
	 * topIcon = iconRegister.registerIcon(Constants.modId + ":" + getTileName() + "_top");
	 * bottomIconDisabled = iconRegister.registerIcon(Constants.modId + ":" + getTileName() + "_bottom_0");
	 * bottomIconEnabled = iconRegister.registerIcon(Constants.modId + ":" + getTileName() + "_bottom_1");
	 * }
	 */

	/*
	 * @Override
	 * 
	 * @SideOnly(Side.CLIENT)
	 * public IIcon getIcon(int side, int meta) {
	 * return ((side == 0)
	 * ? (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableStationAutoCrafting)
	 * ? bottomIconEnabled : bottomIconDisabled)
	 * : ((side == 1) ? topIcon : blockIcon));
	 * }
	 */

	@Override
	public TileEntity createNewTileEntity( World world, int metadata )
	{
		return new TileEntityCraftingStation();
	}

}
