package io.github.tehstoneman.betterstorage.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.AABB;

public final class WorldUtils
{
	private WorldUtils()
	{}

	// @SideOnly( Side.CLIENT )
	/*
	 * public static Level getLocalWorld()
	 * {
	 * return Minecraft.getInstance().level;
	 * }
	 */

	public static AABB getAABB( BlockEntity entity, double minX, double minY, double minZ, double maxX, double maxY, double maxZ )
	{
		final double x = entity.getBlockPos().getX();
		final double y = entity.getBlockPos().getY();
		final double z = entity.getBlockPos().getZ();
		return new AABB( x - minX, y - minY, z - minZ, x + maxX + 1, y + maxY + 1, z + maxZ + 1 );
	}

	public static AABB getAABB( BlockEntity entity, double radius )
	{
		return getAABB( entity, radius, radius, radius, radius, radius, radius );
	}

	// Item spawning related functions

	/** Spawns an ItemStack in the world. */
	/*
	 * public static EntityItem spawnItem( Level world, double x, double y, double z, ItemStack stack )
	 * {
	 * if( stack == null || stack.getCount() <= 0 )
	 * return null;
	 * final EntityItem item = new EntityItem( world, x, y, z, stack );
	 * world.spawnEntity( item );
	 * return item;
	 * }
	 */

	/** Spawns an ItemStack in the world with random motion. */
	/*
	 * public static EntityItem spawnItemWithMotion( Level world, double x, double y, double z, ItemStack stack )
	 * {
	 * final EntityItem item = spawnItem( world, x, y, z, stack );
	 * if( item != null )
	 * {
	 * item.motionX = RandomUtils.getGaussian() * 0.05F;
	 * item.motionY = RandomUtils.getGaussian() * 0.05F + 0.2F;
	 * item.motionZ = RandomUtils.getGaussian() * 0.05F;
	 * }
	 * return item;
	 * }
	 */

	/** Spawn an ItemStack dropping from a destroyed block. */
	/*
	 * public static EntityItem dropStackFromBlock( Level world, int x, int y, int z, ItemStack stack )
	 * {
	 * final float itemX = x + RandomUtils.getFloat( 0.1F, 0.9F );
	 * final float itemY = y + RandomUtils.getFloat( 0.1F, 0.9F );
	 * final float itemZ = z + RandomUtils.getFloat( 0.1F, 0.9F );
	 * return spawnItemWithMotion( world, itemX, itemY, itemZ, stack );
	 * }
	 */

	/** Spawn an ItemStack dropping from a destroyed block. */
	/*
	 * public static EntityItem dropStackFromBlock( BlockEntity te, ItemStack stack )
	 * {
	 * return dropStackFromBlock( te.getLevel(), te.getBlockPos().getX(), te.getBlockPos().getY(), te.getBlockPos().getZ(), stack );
	 * }
	 */

	/** Spawns an ItemStack as if it was dropped from an entity on death. */
	/*
	 * public static EntityItem dropStackFromEntity( Entity entity, ItemStack stack, float speed )
	 * {
	 * final EntityPlayer player = entity instanceof EntityPlayer ? (EntityPlayer)entity : null;
	 * EntityItem item;
	 * if( player == null )
	 * {
	 * final double y = entity.posY + entity.getEyeHeight() - 0.3;
	 * item = spawnItem( entity.world, entity.posX, y, entity.posZ, stack );
	 * if( item == null )
	 * return null;
	 * // item.delayBeforeCanPickup = 40;
	 * final float f1 = RandomUtils.getFloat( 0.5F );
	 * final float f2 = RandomUtils.getFloat( (float)Math.PI * 2.0F );
	 * item.motionX = -Mth.sin( f2 ) * f1;
	 * item.motionY = 0.2;
	 * item.motionZ = Mth.cos( f2 ) * f1;
	 * return item;
	 * }
	 * else
	 * item = player.dropItem( stack, true );
	 * if( item != null )
	 * {
	 * item.motionX *= speed / 4;
	 * item.motionZ *= speed / 4;
	 * }
	 * return item;
	 * }
	 */

	// BlockEntity related functions

	/** Returns whether the BlockEntity at the position is an instance of tileClass. */
	/*
	 * public static <T> boolean is( IBlockAccess world, int x, int y, int z, Class< T > tileClass )
	 * {
	 * return tileClass.isInstance( world.getBlockEntity( new BlockPos( x, y, z ) ) );
	 * }
	 */

	/** Returns the BlockEntity at the position if it's an instance of tileClass, null if not. */
	/*
	 * public static <T> T get( IBlockAccess world, int x, int y, int z, Class< T > tileClass )
	 * {
	 * final BlockEntity t = world.getBlockEntity( new BlockPos( x, y, z ) );
	 * return tileClass.isInstance( t ) ? (T)t : null;
	 * }
	 */

	/** Returns if the BlockEntity can be used by this player. */
	/*
	 * public static boolean isTileEntityUsableByPlayer( BlockEntity entity, EntityPlayer player )
	 * {
	 * return entity.getLevel().getBlockEntity( entity.getBlockPos() ) == entity
	 * && player.getDistanceSq( entity.getBlockPos().getX() + 0.5, entity.getBlockPos().getY() + 0.5, entity.getBlockPos().getZ() + 0.5 ) <= 64.0;
	 * }
	 */

	/** Counts and returns the number of players who're accessing a tile entity. */
	/*
	 * public static int syncPlayersUsing( BlockEntity te, int playersUsing, Inventory playerInventory )
	 * {
	 * if( !te.getLevel().isRemote && playersUsing != 0 )
	 * {
	 * playersUsing = 0;
	 * final List< EntityPlayer > players = te.getLevel().getEntitiesOfClass( EntityPlayer.class, getAABB( te, 5 ) );
	 * for( final EntityPlayer player : players )
	 * if( player.containerMenu instanceof ContainerBetterStorage )
	 * {
	 * final Inventory inventory = ( (ContainerBetterStorage)player.containerMenu ).inventory;
	 * if( inventory == playerInventory )
	 * playersUsing++;
	 * else
	 * if( inventory instanceof InventoryTileEntity )
	 * if( ( (InventoryTileEntity)inventory ).mainTileEntity == te )
	 * playersUsing++;
	 * }
	 * }
	 * return playersUsing;
	 * }
	 */

	/** Counts and returns the number of players who're accessing a tile entity. */
	/*
	 * public static int syncPlayersUsing( TileEntityContainer te, int numUsingPlayers )
	 * {
	 * return syncPlayersUsing( te, numUsingPlayers, te.getPlayerInventory() );
	 * }
	 */

	/**
	 * This will perform a Level.updateNeighborsAt() on every adjacent block including the block at x|y|z.
	 *
	 * @param world
	 *            The world
	 * @param x
	 *            X position
	 * @param y
	 *            Y position
	 * @param z
	 *            Z position
	 */

	public static void notifyBlocksAround( Level world, int x, int y, int z )
	{
		final Block block = world.getBlockState( new BlockPos( x, y, z ) ).getBlock();
		// world.updateNeighborsAt( pos, blockType );
		world.updateNeighborsAt( new BlockPos( x, y, z ), block );
		world.updateNeighborsAt( new BlockPos( x + 1, y, z ), block );
		world.updateNeighborsAt( new BlockPos( x - 1, y, z ), block );
		world.updateNeighborsAt( new BlockPos( x, y + 1, z ), block );
		world.updateNeighborsAt( new BlockPos( x, y - 1, z ), block );
		world.updateNeighborsAt( new BlockPos( x, y, z + 1 ), block );
		world.updateNeighborsAt( new BlockPos( x, y, z - 1 ), block );
	}

	// Misc functions

	/*
	 * public static BlockHitResult rayTrace( EntityPlayer player, float partialTicks )
	 * {
	 * Attachments.playerLocal.set( player );
	 * // final double range = player.worldObj.isRemote ? Minecraft.getMinecraft().playerController.getBlockReachDistance()
	 * // : ( (EntityPlayerMP)player ).theItemInWorldManager.getBlockReachDistance();
	 * final double range = Minecraft.getInstance().playerController.getBlockReachDistance();
	 * final Vec3d start = new Vec3d( player.posX, player.posY + 1.62 - player.getYOffset(), player.posZ );
	 * final Vec3d look = player.getLook( 1.0F );
	 * final Vec3d end = start.addVector( look.x * range, look.y * range, look.z * range );
	 * final BlockHitResult target = player.world.rayTraceBlocks( start, end );
	 * Attachments.playerLocal.remove();
	 * return target;
	 * }
	 */
}
