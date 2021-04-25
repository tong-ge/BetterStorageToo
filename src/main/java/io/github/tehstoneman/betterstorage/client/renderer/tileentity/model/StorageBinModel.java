package io.github.tehstoneman.betterstorage.client.renderer.tileentity.model;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class StorageBinModel extends Model
{
	protected ModelRenderer storageBin;

	public StorageBinModel()
	{
		super( RenderType::getEntityCutout );
		storageBin = new ModelRenderer( 64, 64, 0, 18 );
		storageBin.addBox( 0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 14.0F, 0.0F );
	}

	public StorageBinModel( Function< ResourceLocation, RenderType > renderTypeIn )
	{
		super( renderTypeIn );
	}

	@Override
	public void render( MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue,
			float alpha )
	{
		storageBin.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
	}
}
