package io.github.tehstoneman.betterstorage.client.renderer.entity.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;

public class ModelLocker extends Model
{
	/** The locker door in the locker's model. */
	protected RendererModel	lockerDoorLeft;
	protected RendererModel	lockerDoorRight;
	/** The model of the body of the locker. */
	protected RendererModel	lockerBody;
	/** The locker's knob in the locker model. */
	protected RendererModel	lockerKnobLeft;
	protected RendererModel	lockerKnobRight;

	public ModelLocker()
	{
		lockerDoorLeft = new RendererModel( this, 12, 0 ).setTextureSize( 64, 64 );
		lockerDoorLeft.addBox( 0.0F, 0.0F, -2.0F, 16, 16, 2, 0.0F );
		lockerDoorLeft.rotationPointX = 0.0F;
		lockerDoorLeft.rotationPointY = 0.0F;
		lockerDoorLeft.rotationPointZ = 3.0F;

		lockerKnobLeft = new RendererModel( this, 0, 0 ).setTextureSize( 64, 64 );
		lockerKnobLeft.addBox( 12.0F, 6.0F, -3.0F, 2, 4, 1, 0.0F );
		lockerKnobLeft.rotationPointX = 0.0F;
		lockerKnobLeft.rotationPointY = 0.0F;
		lockerKnobLeft.rotationPointZ = 3.0F;

		lockerDoorRight = new RendererModel( this, 12, 0 ).setTextureSize( 64, 64 );
		lockerDoorRight.addBox( -16.0F, 0.0F, -2.0F, 16, 16, 2, 0.0F );
		lockerDoorRight.rotationPointX = 16.0F;
		lockerDoorRight.rotationPointY = 0.0F;
		lockerDoorRight.rotationPointZ = 3.0F;

		lockerKnobRight = new RendererModel( this, 0, 0 ).setTextureSize( 64, 64 );
		lockerKnobRight.addBox( -14.0F, 6.0F, -3.0F, 2, 4, 1, 0.0F );
		lockerKnobRight.rotationPointX = 16.0F;
		lockerKnobRight.rotationPointY = 0.0F;
		lockerKnobRight.rotationPointZ = 3.0F;

		lockerBody = new RendererModel( this, 0, 18 ).setTextureSize( 64, 64 );
		lockerBody.addBox( 0.0F, 0.0F, 0.0F, 16, 16, 14, 0.0F );
		lockerBody.rotationPointX = 0.0F;
		lockerBody.rotationPointY = 0.0F;
		lockerBody.rotationPointZ = 2.0F;
	}

	/**
	 * This method renders out all parts of the locker model.
	 */

	public void renderAll( boolean flag )
	{
		if( flag )
		{
			lockerKnobLeft.rotateAngleY = lockerDoorLeft.rotateAngleY;
			lockerDoorLeft.render( 0.0625F );
			lockerKnobLeft.render( 0.0625F );
		}
		else
		{
			lockerKnobRight.rotateAngleY = lockerDoorRight.rotateAngleY;
			lockerDoorRight.render( 0.0625F );
			lockerKnobRight.render( 0.0625F );
		}
		lockerBody.render( 0.0625F );
	}

	public RendererModel getDoor( boolean isLeftDoor )
	{
		return isLeftDoor ? lockerDoorLeft : lockerDoorRight;
	}
}
