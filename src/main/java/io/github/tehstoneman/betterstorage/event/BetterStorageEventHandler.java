package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.ModInfo;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( modid = ModInfo.MOD_ID )
public class BetterStorageEventHandler
{
	/*
	 * @SubscribeEvent
	 * public static void onPlayerBreakSpeed( PlayerEvent.BreakSpeed event )
	 * {
	 * final BlockState blockState = event.getState();
	 * if( blockState.hasBlockEntity() )
	 * {
	 * final Player player = event.getPlayer();
	 * final Level world = player.level;
	 * final BlockEntity tileEntity = world.getBlockEntity( event.getPos() );
	 * if( tileEntity instanceof IKeyLockable )
	 * {
	 * final IKeyLockable lockable = (IKeyLockable)tileEntity;
	 * if( lockable != null && lockable.isLocked() )
	 * if( BetterStorageConfig.COMMON.lockBreakable.get() )
	 * {
	 * final ItemStack tool = player.getMainHandItem();
	 * final Set< ToolType > toolTypes = tool.getToolTypes();
	 * float speed = event.getOriginalSpeed() * 0.2f;
	 * if( !( toolTypes.contains( ToolType.AXE ) || toolTypes.contains( ToolType.PICKAXE ) ) )
	 * speed *= 0.1f;
	 * else
	 * {
	 * final int level = Math.max( tool.getHarvestLevel( ToolType.AXE, player, blockState ),
	 * tool.getHarvestLevel( ToolType.PICKAXE, player, blockState ) );
	 * if( level < EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), lockable.getLock() ) )
	 * speed *= 0.1f;
	 * }
	 * event.setNewSpeed( speed );
	 * }
	 * else if( event.isCancelable() )
	 * event.setCanceled( true );
	 * }
	 * }
	 * }
	 */

	/*
	 * @SubscribeEvent
	 * public static void onPlayerRightClickBlock( PlayerInteractEvent.RightClickBlock event )
	 * {
	 * final ItemStack heldItem = event.getItemStack();
	 * if( heldItem.getItem() == Items.MILK_BUCKET && BetterStorageConfig.COMMON.useFluidMilk.get() )
	 * {
	 * final Level world = event.getLevel();
	 * final BlockPos pos = event.getPos();
	 * final Direction face = event.getFace();
	 * final Player player = event.getPlayer();
	 * final InteractionHand hand = event.getHand();
	 * 
	 * // Try to place milk into tank
	 * Boolean result = FluidUtil.getFluidHandler( world, pos, face ).map( handler -> FluidMilk.interactFluidHandler( player, hand, handler ) )
	 * .orElse( false );
	 * 
	 * // If no tank available, try to place milk in world
	 * if( !result )
	 * {
	 * final FluidActionResult fluidActionResult = FluidUtil.tryPlaceFluid( player, world, hand, pos.offset( face.getNormal() ),
	 * new ItemStack( BetterStorageItems.MILK_BUCKET.get() ), new FluidStack( BetterStorageFluids.MILK.get(), 1000 ) );
	 * result = fluidActionResult.isSuccess();
	 * }
	 * 
	 * // If successful, cancel event and swap milk bucket for empty bucket
	 * if( result )
	 * {
	 * event.setCancellationResult( InteractionResult.SUCCESS );
	 * if( event.isCancelable() )
	 * event.setCanceled( true );
	 * if( !event.getPlayer().isCreative() )
	 * event.getPlayer().setItemInHand( hand, new ItemStack( Items.BUCKET ) );
	 * }
	 * }
	 * }
	 */
}
