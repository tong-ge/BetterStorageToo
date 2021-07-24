package io.github.tehstoneman.betterstorage.client.renderer.tileentity.model;

import java.util.function.Function;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.util.ResourceLocation;

public class ModelLocker extends Model
{
	/** The locker door in the locker's model. */
	protected ModelRenderer	lockerDoorLeft;
	protected ModelRenderer	lockerDoorRight;
	/** The model of the body of the locker. */
	protected ModelRenderer	lockerBody;

	/** The locker's knob in the locker model. */
	protected ModelRenderer	lockerKnobLeft;
	protected ModelRenderer	lockerKnobRight;

	public ModelLocker()
	{
		super( RenderType::entityCutout );
		lockerBody = new ModelRenderer( 64, 64, 0, 18 );
		lockerBody.addBox( 0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 14.0F, 0.0F );

		lockerDoorLeft = new ModelRenderer( 64, 64, 12, 0 );
		lockerDoorLeft.addBox( 0.0F, 0.0F, 0.0F, 16.0F, 16.0F, 2.0F, 0.0F );
		lockerDoorLeft.z = 13.0F;

		lockerKnobLeft = new ModelRenderer( 64, 64, 0, 0 );
		lockerKnobLeft.addBox( 12.0F, 6.0F, 2.0F, 2.0F, 4.0F, 1.0F, 0.0F );
		lockerKnobLeft.z = 13.0F;

		lockerDoorRight = new ModelRenderer( 64, 64, 12, 0 );
		lockerDoorRight.addBox( -16.0F, 0.0F, 0.0F, 16.0F, 16.0F, 2.0F, 0.0F );
		lockerDoorRight.x = 16.0F;
		lockerDoorRight.z = 13.0F;

		lockerKnobRight = new ModelRenderer( 64, 64, 0, 0 );
		lockerKnobRight.addBox( -14.0F, 6.0F, 2.0F, 2.0F, 4.0F, 1.0F, 0.0F );
		lockerKnobRight.x = 16.0F;
		lockerKnobRight.z = 13.0F;
	}

	public ModelLocker( Function< ResourceLocation, RenderType > renderTypeIn )
	{
		super( renderTypeIn );
	}

	public void renderToBuffer( MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green,
			float blue, float alpha, boolean flag )
	{
		if( flag )
		{
			lockerDoorLeft.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
			lockerKnobLeft.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
		}
		else
		{
			lockerDoorRight.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
			lockerKnobRight.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
		}
		lockerBody.render( matrixStack, buffer, packedLight, packedOverlay, red, green, blue, alpha );
	}

	public void rotateDoor( float angle, boolean isLeftDoor )
	{
		if( isLeftDoor )
		{
			final float r = -angle * ( (float)Math.PI / 2F );
			lockerDoorLeft.yRot = r;
			lockerKnobLeft.yRot = r;
		}
		else
		{
			final float r = angle * ( (float)Math.PI / 2F );
			lockerDoorRight.yRot = r;
			lockerKnobRight.yRot = r;
		}
	}

	@Override
	public void renderToBuffer( MatrixStack p_225598_1_, IVertexBuilder p_225598_2_, int p_225598_3_, int p_225598_4_, float p_225598_5_,
			float p_225598_6_, float p_225598_7_, float p_225598_8_ )
	{
		// TODO Auto-generated method stub

	}
}
