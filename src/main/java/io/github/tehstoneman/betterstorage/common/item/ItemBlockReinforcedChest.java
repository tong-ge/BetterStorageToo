package io.github.tehstoneman.betterstorage.common.item;

import net.minecraft.block.Block;

public class ItemBlockReinforcedChest extends ItemBlockBetterStorage
{
	public ItemBlockReinforcedChest( Block block )
	{
		super( block );
		// setMaxDamage( 0 );
		// setHasSubtypes( true );
	}

	/*
	 * @Override
	 * public EnumActionResult onItemUse( EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY,
	 * float hitZ )
	 * {
	 * final IBlockState iblockstate = worldIn.getBlockState( pos );
	 * final Block block = iblockstate.getBlock();
	 *
	 * if( !block.isReplaceable( worldIn, pos ) )
	 * pos = pos.offset( facing );
	 *
	 * final ItemStack itemstack = player.getHeldItem( hand );
	 *
	 * if( !itemstack.isEmpty() && player.canPlayerEdit( pos, facing, itemstack )
	 * && worldIn.mayPlace( this.block, pos, false, facing, (Entity)null ) )
	 * {
	 * final IBlockState iblockstate1 = this.block.getStateForPlacement( worldIn, pos, facing, hitX, hitY, hitZ, 0, player, hand );
	 *
	 * if( placeBlockAt( itemstack, player, worldIn, pos, facing, hitX, hitY, hitZ, iblockstate1 ) )
	 * {
	 * final SoundType soundtype = worldIn.getBlockState( pos ).getBlock().getSoundType( worldIn.getBlockState( pos ), worldIn, pos,
	 * player );
	 * worldIn.playSound( player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, ( soundtype.getVolume() + 1.0F ) / 2.0F,
	 * soundtype.getPitch() * 0.8F );
	 * itemstack.shrink( 1 );
	 * }
	 *
	 * return EnumActionResult.SUCCESS;
	 * }
	 * else
	 * return EnumActionResult.FAIL;
	 * }
	 */

	/*
	 * @Override
	 * public int getMetadata( int metadata )
	 * {
	 * return metadata;
	 * }
	 */

	/*
	 * @Override
	 * public String getItemStackDisplayName( ItemStack stack )
	 * {
	 * // final EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
	 * final EnumReinforced material = BetterStorageAPI.materials.get( stack );
	 * if( material != null )
	 * {
	 * final String materialName = BetterStorage.proxy.localize( material.getUnlocalizedName() );
	 * final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
	 * return name.trim();
	 * }
	 * return super.getItemStackDisplayName( stack );
	 * }
	 */

	/*
	 * @Override
	 * public String getUnlocalizedName( ItemStack stack )
	 * {
	 * // final EnumReinforced material = EnumReinforced.byMetadata( stack.getMetadata() );
	 * // return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
	 * final EnumReinforced material = BetterStorageAPI.materials.get( stack );
	 * Logger.getLogger( ModInfo.modId ).info( "Material : " + material );
	 * if( material != null )
	 * {
	 * final String materialName = BetterStorage.proxy.localize( material.getUnlocalizedName() );
	 * final String name = BetterStorage.proxy.localize( getUnlocalizedName() + ".name.full", materialName );
	 * return super.getUnlocalizedName() + "." + material.getUnlocalizedName();
	 * }
	 * return super.getUnlocalizedName();
	 * }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly( Side.CLIENT )
	 * public void getSubItems( CreativeTabs tab, NonNullList< ItemStack > subItems )
	 * {
	 * if( isInCreativeTab( tab ) )
	 * for( final EnumReinforced values : EnumReinforced.values() )
	 * {
	 * final int metadata = values.getMetadata();
	 * final ItemStack subItemStack = new ItemStack( this, 1, metadata );
	 * subItems.add( subItemStack );
	 * }
	 * }
	 */
}
