package io.github.tehstoneman.betterstorage.client.renderer.tileentity.model;

import net.minecraft.client.renderer.entity.model.RendererModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class ModelTankFluid extends Model
{
	protected final RendererModel	LEVEL_02;
	protected final RendererModel	LEVEL_04;
	protected final RendererModel	LEVEL_06;
	protected final RendererModel	LEVEL_08;
	protected final RendererModel	LEVEL_10;
	protected final RendererModel	LEVEL_12;
	protected final RendererModel	LEVEL_14;
	protected final RendererModel	LEVEL_16;

	public ModelTankFluid()
	{
		LEVEL_02 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 2, 12 );
		LEVEL_04 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 4, 12 );
		LEVEL_06 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 6, 12 );
		LEVEL_08 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 8, 12 );
		LEVEL_10 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 10, 12 );
		LEVEL_12 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 12, 12 );
		LEVEL_14 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 14, 12 );
		LEVEL_16 = new RendererModel( this, 0, 0 ).addBox( 0.125f, 0.0f, 0.125f, 12, 16, 12 );
	}
	
	public void render(TextureAtlasSprite fluidStillSprite)
	{
		LEVEL_16.render( 0.0625f );
	}
}
