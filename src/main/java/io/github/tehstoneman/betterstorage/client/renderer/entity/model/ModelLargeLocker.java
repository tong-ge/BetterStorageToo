package io.github.tehstoneman.betterstorage.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.ModelRenderer;

public class ModelLargeLocker extends ModelLocker
{
	public ModelLargeLocker()
	{
		lockerDoorLeft	= new ModelRenderer( this, 12, 0 ).setTextureSize( 64, 128 );
		lockerDoorLeft.addBox( 0.0F, -16.0F, -2.0F, 16, 32, 2, 0.0F );
		lockerDoorLeft.rotationPointX = 0.0F;
		lockerDoorLeft.rotationPointY = 0.0F;
		lockerDoorLeft.rotationPointZ = 3.0F;

		lockerKnobLeft = new ModelRenderer( this, 0, 0 ).setTextureSize( 64, 128 );
		lockerKnobLeft.addBox( 12.0F, -2.0F, -3.0F, 2, 4, 1, 0.0F );
		lockerKnobLeft.rotationPointX = 0.0F;
		lockerKnobLeft.rotationPointY = 0.0F;
		lockerKnobLeft.rotationPointZ = 3.0F;

		lockerDoorRight	= new ModelRenderer( this, 12, 0 ).setTextureSize( 64, 128 );
		lockerDoorRight.addBox( -16.0F, -16.0F, -2.0F, 16, 32, 2, 0.0F );
		lockerDoorRight.rotationPointX = 16.0F;
		lockerDoorRight.rotationPointY = 0.0F;
		lockerDoorRight.rotationPointZ = 3.0F;

		lockerKnobRight = new ModelRenderer( this, 0, 0 ).setTextureSize( 64, 128 );
		lockerKnobRight.addBox( -14.0F, -2.0F, -3.0F, 2, 4, 1, 0.0F );
		lockerKnobRight.rotationPointX = 16.0F;
		lockerKnobRight.rotationPointY = 0.0F;
		lockerKnobRight.rotationPointZ = 3.0F;

		lockerBody = new ModelRenderer( this, 0, 34 ).setTextureSize( 64, 128 );
		lockerBody.addBox( 0.0F, -16.0F, 0.0F, 16, 32, 14, 0.0F );
		lockerBody.rotationPointX = 0.0F;
		lockerBody.rotationPointY = 0.0F;
		lockerBody.rotationPointZ = 2.0F;
	}
}
