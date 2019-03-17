package io.github.tehstoneman.betterstorage.client.renderer;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;

//@SideOnly( Side.CLIENT )
public class RenderArmor extends RenderPlayer
{
	public RenderArmor( RenderManager renderManager )
	{
		super( renderManager );
		// setRenderManager(renderManager);
	}

	/*
	 * @Override
	 * protected float handleRotationFloat(EntityLivingBase entity, float partialTicks) { return partialTicks; }
	 */
}
