package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Events
{
	@SubscribeEvent
	public void onPlayerInteract( PlayerInteractEvent event )
	{
		final World world = event.getEntity().world;
		final BlockPos pos = event.getPos();
		final EntityPlayer player = event.getEntityPlayer();
		final ItemStack holding = player.getHeldItemMainhand();
		final IBlockState state = world.getBlockState( pos );
		final Block block = state.getBlock();
		// final boolean leftClick = event.action == Action.LEFT_CLICK_BLOCK;
		// final boolean rightClick = event.action == Action.RIGHT_CLICK_BLOCK;
		final EnumHand hand = event.getHand();

		// Interact with attachments.
		/*
		 * if( leftClick || rightClick )
		 * {
		 * final IHasAttachments hasAttachments = WorldUtils.get( world, x, y, z, IHasAttachments.class );
		 * if( hasAttachments != null )
		 * {
		 * final EnumAttachmentInteraction interactionType = event.action == Action.LEFT_CLICK_BLOCK ? EnumAttachmentInteraction.attack
		 * : EnumAttachmentInteraction.use;
		 * if( hasAttachments.getAttachments().interact( WorldUtils.rayTrace( player, 1.0F ), player, interactionType ) )
		 * {
		 * event.useBlock = Result.DENY;
		 * event.useItem = Result.DENY;
		 * }
		 * }
		 * }
		 */

		// Use cauldron to remove color from dyable items
		/*
		 * if( rightClick && block == Blocks.CAULDRON )
		 * {
		 * final int metadata = world.getBlockMetadata( x, y, z );
		 * if( metadata > 0 )
		 * {
		 * final IDyeableItem dyeable = holding != null && holding.getItem() instanceof IDyeableItem ? (IDyeableItem)holding.getItem() : null;
		 * if( dyeable != null && dyeable.canDye( holding ) )
		 * {
		 * StackUtils.remove( holding, "display", "color" );
		 * world.setBlockMetadataWithNotify( x, y, z, metadata - 1, 2 );
		 * world.func_147453_f( x, y, z, block );
		 *
		 * event.useBlock = Result.DENY;
		 * event.useItem = Result.DENY;
		 * }
		 * }
		 * }
		 */

		// Prevent players from breaking blocks with broken cardboard items.
		/*
		 * if( leftClick && holding != null && holding.getItem() instanceof ICardboardItem && !ItemCardboardSheet.isEffective( holding ) )
		 * event.useItem = Result.DENY;
		 */

		// Attach locks to iron doors.
		if( !world.isRemote && BetterStorage.config.lockableDoorEnabled && hand == EnumHand.MAIN_HAND && block == Blocks.IRON_DOOR && holding.getItem() == BetterStorageItems.LOCK )
		{
			/*final MovingObjectPosition target = WorldUtils.rayTrace( player, 1F );
			if( target != null && getIronDoorHightlightBox( player, world, x, y, z, target.hitVec, block ) != null )
			{

				int meta = world.getBlockMetadata( x, y, z );
				boolean isMirrored;
				if( meta >= 8 )
				{
					isMirrored = meta == 9;
					y -= 1;
					meta = world.getBlockMetadata( x, y, z );
				}
				else
					isMirrored = world.getBlockMetadata( x, y + 1, z ) == 9;

				final int rotation = meta & 3;
				ForgeDirection orientation = rotation == 0 ? ForgeDirection.WEST
						: rotation == 1 ? ForgeDirection.NORTH : rotation == 2 ? ForgeDirection.EAST : ForgeDirection.SOUTH;
				orientation = isMirrored ? orientation == ForgeDirection.WEST ? ForgeDirection.SOUTH
						: orientation == ForgeDirection.NORTH ? ForgeDirection.WEST
								: orientation == ForgeDirection.EAST ? ForgeDirection.NORTH : ForgeDirection.EAST
						: orientation;

				world.setBlock( x, y, z, BetterStorageTiles.lockableDoor, 0, SetBlockFlag.SEND_TO_CLIENT );
				world.setBlock( x, y + 1, z, BetterStorageTiles.lockableDoor, 8, SetBlockFlag.SEND_TO_CLIENT );

				final TileEntityLockableDoor te = WorldUtils.get( world, x, y, z, TileEntityLockableDoor.class );
				te.orientation = orientation;
				te.isOpen = isMirrored;
				te.isMirrored = isMirrored;
				te.setLock( holding );*/

				player.inventory.setInventorySlotContents( player.inventory.currentItem, ItemStack.EMPTY );
			//}
		}

		// Prevent eating of slime buckets after capturing them.
		/*
		 * if( preventSlimeBucketUse )
		 * {
		 * event.setCanceled( true );
		 * preventSlimeBucketUse = false;
		 * }
		 */

	}
}
