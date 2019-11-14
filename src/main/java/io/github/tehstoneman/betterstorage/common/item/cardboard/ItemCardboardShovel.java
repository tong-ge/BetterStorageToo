package io.github.tehstoneman.betterstorage.common.item.cardboard;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.ShovelItem;
import net.minecraft.nbt.CompoundNBT;

public class ItemCardboardShovel extends ShovelItem implements ICardboardItem
{

	public ItemCardboardShovel()
	{
		super( ItemTier.WOOD, 1.5F, -3.0F, new Item.Properties().group( ItemGroup.TOOLS ) );
	}

	/*
	 * public ItemCardboardShovel()
	 * {
	 * super( ItemCardboardSheet.toolMaterial );
	 * this.name = "cardboard_shovel";
	 * }
	 */

	/*
	 * public void register()
	 * {
	 * setUnlocalizedName( ModInfo.modId + "." + name );
	 * setRegistryName( name );
	 * //GameRegistry.register( this );
	 * }
	 */

	/*
	 * @SideOnly( Side.CLIENT )
	 * public void registerItemModels()
	 * {
	 * ModelLoader.setCustomModelResourceLocation( this, 0, new ModelResourceLocation( getRegistryName(), "inventory" ) );
	 * }
	 */

	// Makes sure cardboard tools don't get destroyed,
	// and are ineffective when durability is at 0.
	/*
	 * @Override
	 * public boolean canHarvestBlock( IBlockState block, ItemStack stack )
	 * {
	 * return ItemCardboardSheet.canHarvestBlock( stack, super.canHarvestBlock( block, stack ) );
	 * }
	 */

	/*
	 * @Override
	 * public boolean onLeftClickEntity( ItemStack stack, EntityPlayer player, Entity entity )
	 * {
	 * return !ItemCardboardSheet.isEffective( stack );
	 * }
	 */

	/*
	 * @Override
	 * public boolean onBlockDestroyed( ItemStack stack, World world, IBlockState block, BlockPos pos, EntityLivingBase player )
	 * {
	 * //return ItemCardboardSheet.onBlockDestroyed( stack, world, block, pos, player );
	 * return block.getBlockHardness( world, pos ) > 0 ? ItemCardboardSheet.damageItem( stack, 1, player ) : true;
	 * }
	 */

	/*
	 * @Override
	 * public boolean hitEntity( ItemStack stack, EntityLivingBase target, EntityLivingBase player )
	 * {
	 * return ItemCardboardSheet.damageItem( stack, 1, player );
	 * }
	 */

	// Cardboard items
	@Override
	public boolean canDye( ItemStack stack )
	{
		return true;
	}

	@Override
	public int getColor( ItemStack itemstack )
	{
		if( hasColor( itemstack ) )
		{
			final CompoundNBT compound = itemstack.getTag();
			return compound.getInt( "color" );
		}
		return 0xA08060;
	}

	@Override
	public boolean hasColor( ItemStack itemstack )
	{
		if( itemstack.hasTag() )
		{
			final CompoundNBT compound = itemstack.getTag();
			return compound.contains( "color" );
		}
		return false;
	}

	@Override
	public void setColor( ItemStack itemstack, int colorRGB )
	{
		final CompoundNBT compound = itemstack.getOrCreateTag();
		compound.putInt( "color", colorRGB );
		itemstack.setTag( compound );
	}
}
