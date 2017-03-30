package io.github.tehstoneman.betterstorage.attachment;

import org.lwjgl.opengl.GL11;

import io.github.tehstoneman.betterstorage.utils.RenderUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class ItemAttachmentRenderer implements IAttachmentRenderer
{

	public static final ItemAttachmentRenderer instance = new ItemAttachmentRenderer();

	@Override
	public void render( Attachment attachment, float partialTicks )
	{
		render( (ItemAttachment)attachment, partialTicks );
	}

	private void render( ItemAttachment attachment, float partialTicks )
	{
		final ItemStack item = attachment.getItem();
		if( item == null )
			return;
		GL11.glPushMatrix();
		GL11.glScalef( attachment.scale, attachment.scale, attachment.scaleDepth );
		RenderUtils.renderItemIn3d( item );
		GL11.glPopMatrix();
	}

}
