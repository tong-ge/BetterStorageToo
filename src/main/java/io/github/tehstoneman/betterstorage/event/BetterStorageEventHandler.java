package io.github.tehstoneman.betterstorage.event;

import java.util.Set;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.lock.IKeyLockable;
import io.github.tehstoneman.betterstorage.common.enchantment.EnchantmentBetterStorage;
import io.github.tehstoneman.betterstorage.config.BetterStorageConfig;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber( modid = ModInfo.MOD_ID )
public class BetterStorageEventHandler
{
	@SubscribeEvent
	public static void onPlayerBreakSpeed( PlayerEvent.BreakSpeed event )
	{
		final BlockState blockState = event.getState();
		final BlockPos pos = event.getPos();

		if( blockState.hasTileEntity() )
		{
			final PlayerEntity player = event.getPlayer();
			final World world = player.getEntityWorld();
			final TileEntity tileEntity = world.getTileEntity( event.getPos() );
			if( tileEntity instanceof IKeyLockable )
			{
				final IKeyLockable lockable = (IKeyLockable)tileEntity;
				if( lockable != null && lockable.isLocked() )
					if( BetterStorageConfig.COMMON.lockBreakable.get() )
					{
						final ItemStack tool = player.getHeldItemMainhand();
						final Set< ToolType > toolTypes = tool.getToolTypes();
						float speed = event.getOriginalSpeed() * 0.2f;
						if( !( toolTypes.contains( ToolType.AXE ) || toolTypes.contains( ToolType.PICKAXE ) ) )
							speed *= 0.1f;
						else
						{
							final int level = Math.max( tool.getHarvestLevel( ToolType.AXE, player, blockState ),
									tool.getHarvestLevel( ToolType.PICKAXE, player, blockState ) );
							if( level < EnchantmentHelper.getEnchantmentLevel( EnchantmentBetterStorage.PERSISTANCE.get(), lockable.getLock() ) )
								speed *= 0.1f;
						}
						event.setNewSpeed( speed );
					}
					else if( event.isCancelable() )
						event.setCanceled( true );
			}
		}
	}
}
