package io.github.tehstoneman.betterstorage.attachment;

import org.lwjgl.opengl.GL11;

//@SideOnly( Side.CLIENT )
public class LockAttachmentRenderer extends ItemAttachmentRenderer
{
	public static final LockAttachmentRenderer instance = new LockAttachmentRenderer();

	@Override
	public void render( Attachment attachment, float partialTicks )
	{
		render( (LockAttachment)attachment, partialTicks );
	}

	private void render( LockAttachment attachment, float partialTicks )
	{
		GL11.glPushMatrix();
		final float hit = (float)Math.sin( Math.max( 0, attachment.hit - partialTicks - 4 ) / 6 * Math.PI ) * -20;
		final float wiggle = (float)Math.sin( attachment.wiggle + partialTicks ) * attachment.wiggleStrength;
		GL11.glTranslated( 0.0, -0.05, 0.0 );
		GL11.glRotatef( hit, 1.0F, 0.0F, 0.0F );
		GL11.glRotatef( wiggle, 0.0F, 0.0F, 1.0F );
		GL11.glTranslated( 0.0, 0.05, 0.0 );
		super.render( attachment, partialTicks );
		GL11.glPopMatrix();
	}
}
