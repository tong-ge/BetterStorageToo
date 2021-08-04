package io.github.tehstoneman.betterstorage.event;

import java.util.Set;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.common.fluid.BetterStorageFluids;
import io.github.tehstoneman.betterstorage.common.fluid.FluidMilk;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( modid = ModInfo.MOD_ID )
public class BetterStorageEventHandler
{
	@SubscribeEvent
	public static void onPlayerBreakSpeed( PlayerEvent.BreakSpeed event )
	{
		final BlockState blockState = event.getState();
		if( blockState.hasTileEntity() )
		{
			final PlayerEntity player = event.getPlayer();
			final World world = player.level;
			final TileEntity tileEntity = world.getBlockEntity( event.getPos() );
			if( tileEntity instanceof IKeyLockable )
			{
				final IKeyLockable lockable = (IKeyLockable)tileEntity;
				if( lockable != null && lockable.isLocked() )
					if( BetterStorageConfig.COMMON.lockBreakable.get() )
					{
						final ItemStack tool = player.getMainHandItem();
						final Set< ToolType > toolTypes = tool.getToolTypes();
						float speed = event.getOriginalSpeed() * 0.2f;
						if( !( toolTypes.contains( ToolType.AXE ) || toolTypes.contains( ToolType.PICKAXE ) ) )
							speed *= 0.1f;
						else
						{
							final int level = Math.max( tool.getHarvestLevel( ToolType.AXE, player, blockState ),
									tool.getHarvestLevel( ToolType.PICKAXE, player, blockState ) );
							if( level < EnchantmentHelper.getItemEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), lockable.getLock() ) )
								speed *= 0.1f;
						}
						event.setNewSpeed( speed );
					}
					else if( event.isCancelable() )
						event.setCanceled( true );
			}
		}
	}

	/*@SubscribeEvent
	public static void onPlayerRightClickBlock( PlayerInteractEvent.RightClickBlock event )
	{
		final ItemStack heldItem = event.getItemStack();
		if( heldItem.getItem() == Items.MILK_BUCKET && BetterStorageConfig.COMMON.useFluidMilk.get() )
		{
			final World world = event.getWorld();
			final BlockPos pos = event.getPos();
			final Direction face = event.getFace();
			final PlayerEntity player = event.getPlayer();
			final Hand hand = event.getHand();

			// Try to place milk into tank
			Boolean result = FluidUtil.getFluidHandler( world, pos, face ).map( handler -> FluidMilk.interactFluidHandler( player, hand, handler ) )
					.orElse( false );

			// If no tank available, try to place milk in world
			if( !result )
			{
				final FluidActionResult fluidActionResult = FluidUtil.tryPlaceFluid( player, world, hand, pos.offset( face.getNormal() ),
						new ItemStack( BetterStorageItems.MILK_BUCKET.get() ), new FluidStack( BetterStorageFluids.MILK.get(), 1000 ) );
				result = fluidActionResult.isSuccess();
			}

			// If successful, cancel event and swap milk bucket for empty bucket
			if( result )
			{
				event.setCancellationResult( ActionResultType.SUCCESS );
				if( event.isCancelable() )
					event.setCanceled( true );
				if( !event.getPlayer().isCreative() )
					event.getPlayer().setItemInHand( hand, new ItemStack( Items.BUCKET ) );
			}
		}
	}*/
}
