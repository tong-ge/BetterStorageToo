package io.github.tehstoneman.betterstorage.client.model;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBlockRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class ModelArmorStand extends ModelBase
{
	private final ModelBlockRenderer	bottom;
	private final ModelBlockRenderer	middle;

	private final ModelBlockRenderer	head;
	private final ModelBlockRenderer	shoulder;
	private final ModelBlockRenderer	legs;

	public ModelArmorStand()
	{

		textureWidth = 64;
		textureHeight = 32;

		bottom = new ModelBlockRenderer( this, 4, 0 );
		bottom.addBox( -6F, 0F, -6F, 12, 1, 12 );
		bottom.setRotationPoint( 0F, 23F, 0F );

		middle = new ModelBlockRenderer( this, 0, 0 );
		middle.addBox( -0.5F, 0.5F, -0.5F, 1, 31, 1 );
		middle.setRotationPoint( 0F, -8F, 0F );

		head = new ModelBlockRenderer( this, 4, 9 );
		head.addBox( -2F, 0F, -1F, 4, 1, 2 );
		head.setRotationPoint( 0F, -8F, 0F );

		shoulder = new ModelBlockRenderer( this, 4, 13 );
		shoulder.addBox( -6F, 0F, -1F, 12, 1, 2 );
		shoulder.setRotationPoint( 0F, 0F, 0F );

		legs = new ModelBlockRenderer( this, 4, 16 );
		legs.addBox( -3F, 0F, -1F, 6, 1, 2 );
		legs.setRotationPoint( 0F, 8F, 0F );

	}

	public void renderAll()
	{
		final float foo = 1 / 16.0F;
		bottom.render( foo );
		middle.render( foo );
		head.render( foo );
		shoulder.render( foo );
		legs.render( foo );
	}

	public void setRotation( float rotation )
	{
		rotation = rotation / 360 * ( (float)Math.PI * 2 );
		middle.rotateAngleY = rotation;
		head.rotateAngleY = rotation;
		shoulder.rotateAngleY = rotation;
		legs.rotateAngleY = rotation;
	}
}
