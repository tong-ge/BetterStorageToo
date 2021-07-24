package io.github.tehstoneman.betterstorage.client.renderer.tileentity.model;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ChestModel extends Model
{
	protected ModelRenderer	chestBody;
	protected ModelRenderer	chestLid;
	protected ModelRenderer	chestLatch;

	public ChestModel()
	{
		super( RenderType::entityCutout );

		chestBody = new ModelRenderer( 64, 64, 0, 19 );
		chestBody.addBox( 1.0F, 0.0F, 1.0F, 14.0F, 10.0F, 14.0F, 0.0F );

		chestLid = new ModelRenderer( 64, 64, 0, 0 );
		chestLid.addBox( 1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F );
		chestLid.y = 9.0F;
		chestLid.z = 1.0F;

		chestLatch = new ModelRenderer( 64, 64, 0, 0 );
		chestLatch.addBox( 7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F );
		chestLatch.y = 8.0F;
	}

	public ChestModel( Function< ResourceLocation, RenderType > renderTypeIn )
	{
		super( renderTypeIn );
	}

	@Override
	public void renderToBuffer( MatrixStack matrixStackIn, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green,
			float blue, float alpha )
	{
		chestBody.render( matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha );
		chestLid.render( matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha );
		chestLatch.render( matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha );
	}

	public void rotateLid( float angle )
	{
		final float r = -angle * ( (float)Math.PI / 2F );
		chestLid.xRot = r;
		chestLatch.xRot = r;
	}
}
