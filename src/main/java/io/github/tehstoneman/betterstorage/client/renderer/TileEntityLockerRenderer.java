package io.github.tehstoneman.betterstorage.client.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import io.github.tehstoneman.betterstorage.attachment.LockAttachment;
import io.github.tehstoneman.betterstorage.client.model.ModelLargeLocker;
import io.github.tehstoneman.betterstorage.client.model.ModelLocker;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.utils.DirectionUtils;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class TileEntityLockerRenderer extends TileEntitySpecialRenderer
{
	private final ModelLocker	lockerModel			= new ModelLocker();
	private final ModelLocker	largeLockerModel	= new ModelLargeLocker();

	public void renderTileEntityAt( TileEntityLocker locker, double x, double y, double z, float partialTicks )
	{

		final float scale = 1.0F / 16;

		final boolean large = locker.isConnected();
		if( large && !locker.isMain() )
			return;

		final int index = locker.mirror ? 1 : 0;
		final ModelLocker model = large ? largeLockerModel : lockerModel;
		bindTexture( locker.getResource() );

		GL11.glPushMatrix();
		GL11.glEnable( GL12.GL_RESCALE_NORMAL );

		float angle = locker.prevLidAngle + ( locker.lidAngle - locker.prevLidAngle ) * partialTicks;
		angle = 1.0F - angle;
		angle = 1.0F - angle * angle * angle;
		angle = angle * 90;

		GL11.glTranslated( x + 0.5, y + 0.5, z + 0.5 );
		final int rotation = DirectionUtils.getRotation( locker.getOrientation() );
		GL11.glRotatef( -rotation, 0.0F, 1.0F, 0.0F );

		GL11.glPushMatrix();
		GL11.glScalef( scale, scale, scale );

		model.renderAll( locker.mirror, angle );

		GL11.glPopMatrix();

		if( locker.canHaveLock() )
		{
			if( angle > 0 )
			{
				final double seven = 7 / 16.0D;
				GL11.glTranslated( locker.mirror ? seven : -seven, 0, seven );
				GL11.glRotatef( locker.mirror ? angle : -angle, 0, 1, 0 );
				GL11.glTranslated( locker.mirror ? -seven : seven, 0, -seven );
			}
			final LockAttachment a = locker.lockAttachment;
			GL11.glTranslated( 0.5 - a.getX(), 0.5 - a.getY(), 0.5 - a.getZ() );
			a.getRenderer().render( a, partialTicks );
		}

		GL11.glDisable( GL12.GL_RESCALE_NORMAL );
		GL11.glPopMatrix();

	}

	@Override
	public void renderTileEntityAt( TileEntity entity, double x, double y, double z, float partialTicks, int destroyStage )
	{
		renderTileEntityAt( (TileEntityLocker)entity, x, y, z, partialTicks );
	}
}
