package io.github.tehstoneman.betterstorage.attachment;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.lwjgl.opengl.GL11;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class Attachments implements Iterable< Attachment >
{

	public static final ThreadLocal< EntityPlayer >	playerLocal	= new ThreadLocal<>();

	public final TileEntity							tileEntity;
	private final Map< Integer, Attachment >		attachments	= new HashMap<>();

	public Attachments( TileEntity tileEntity )
	{
		this.tileEntity = tileEntity;
	}

	// Called in CommonProxy.onPlayerInteract.
	public boolean interact( RayTraceResult target, EntityPlayer player, EnumAttachmentInteraction interactionType )
	{
		final Attachment attachment = target != null ? get( target.subHit ) : null;
		return attachment != null ? attachment.interact( player, interactionType ) : false;
	}

	// Called in TileEntityContainer.onPickBlock.
	public ItemStack pick( RayTraceResult target )
	{
		final Attachment attachment = target != null ? get( target.subHit ) : null;
		return attachment != null ? attachment.pick() : null;
	}

	// Called in Block.collisionRayTrace.

	public RayTraceResult rayTrace( World world, int x, int y, int z, Vec3d start, Vec3d end )
	{
		/*
		 * final AxisAlignedBB aabb = tileEntity.getBlockType().getCollisionBoundingBox( world.getBlockState( new BlockPos( x, y, z ) ), world,
		 * new BlockPos( x, y, z ) );
		 * RayTraceResult target = aabb.calculateIntercept( start, end );
		 * final EntityPlayer player = playerLocal.get();
		 * 
		 * double distance = target != null ? start.distanceTo( target.hitVec ) : Double.MAX_VALUE;
		 * for( final Attachment attachment : this )
		 * {
		 * if( !attachment.boxVisible( player ) )
		 * continue;
		 * final AxisAlignedBB attachmentBox = attachment.getHighlightBox();
		 * final RayTraceResult attachmentTarget = attachmentBox.calculateIntercept( start, end );
		 * if( attachmentTarget == null )
		 * continue;
		 * final double attachmentDistance = start.distanceTo( attachmentTarget.hitVec );
		 * if( attachmentDistance >= distance )
		 * continue;
		 * distance = attachmentDistance;
		 * target = attachmentTarget;
		 * target.subHit = attachment.subId;
		 * }
		 */

		/*
		 * if (target != null) {
		 * target.blockX = x;
		 * target.blockY = y;
		 * target.blockZ = z;
		 * }
		 */
		// return target;
		return null;

	}

	// Called in TileEntity.updateEntity.
	public void update()
	{
		for( final Attachment attachment : this )
			attachment.update();
	}

	// Called in TileEntityRenderer.renderTileEntityAt.
	// @SideOnly( Side.CLIENT )
	public void render( float partialTicks )
	{
		for( final Attachment attachment : this )
		{
			final float rotation = attachment.getRotation();
			GL11.glPushMatrix();
			GL11.glTranslated( 0.5, 0.5, 0.5 );
			GL11.glPushMatrix();
			GL11.glRotatef( rotation, 0, -1, 0 );
			GL11.glTranslated( 0.5 - attachment.getX(), 0.5 - attachment.getY(), 0.5 - attachment.getZ() );
			attachment.getRenderer().render( attachment, partialTicks );
			GL11.glPopMatrix();
			GL11.glPopMatrix();
		}
	}

	public <T extends Attachment> T add( Class< T > attachmentClass )
	{
		try
		{
			final Constructor< T > constructor = attachmentClass.getConstructor( TileEntity.class, int.class );
			final T attachment = constructor.newInstance( tileEntity, getFreeSubId() );
			attachments.put( attachment.subId, attachment );
			return attachment;
		}
		catch( final Exception e )
		{
			throw new RuntimeException( e );
		}
	}

	public Attachment get( int subId )
	{
		return attachments.get( subId );
	}

	public void remove( Attachment attachment )
	{
		attachments.remove( attachment.subId );
	}

	public boolean has( Attachment attachment )
	{
		return attachments.containsKey( attachment.subId );
	}

	@Override
	public Iterator< Attachment > iterator()
	{
		return attachments.values().iterator();
	}

	private int getFreeSubId()
	{
		int freeSubId = 0;
		final Set< Integer > takenSet = new HashSet<>( attachments.keySet() );
		while( takenSet.remove( ++freeSubId ) )
		{}
		return freeSubId;
	}

}
