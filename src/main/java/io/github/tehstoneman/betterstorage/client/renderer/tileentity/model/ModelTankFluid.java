package io.github.tehstoneman.betterstorage.client.renderer.tileentity.model;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;

public class ModelTankFluid extends Model
{
	protected ModelRenderer fluid;

	public ModelTankFluid()
	{
		super( RenderType::entityCutout );
	}

	public void setLevel( float amount, boolean fillTop, boolean fillBottom, boolean isLigterThanAir )
	{
		final float height = ( ( fillBottom ? 2.0f : 0.0f ) + 12.0F + ( fillTop ? 2.0f : 0.0f ) ) * amount;
		final float base = fillBottom ? 0.0f : 2.0f;
		fluid = new ModelRenderer( 64, 32, 0, 0 ).addBox( 1.1F, isLigterThanAir ? 16.0f - ( base + height ) : base, 1.1F, 13.8F, height, 13.8F );
	}

	@Override
	public void renderToBuffer( MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
			float alpha )
	{
		fluid.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
	}
}
