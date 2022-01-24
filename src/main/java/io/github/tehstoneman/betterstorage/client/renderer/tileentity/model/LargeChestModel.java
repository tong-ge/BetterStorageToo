package io.github.tehstoneman.betterstorage.client.renderer.tileentity.model;

import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn( Dist.CLIENT )
public class LargeChestModel extends ChestModel
{
	public LargeChestModel()
	{
		super( RenderType::entityCutout );

		/*
		 * chestBody = new ModelBlockRenderer( 128, 64, 0, 19 );
		 * chestBody.addBox( 1.0F, 0.0F, 1.0F, 30.0F, 10.0F, 14.0F, 0.0F );
		 *
		 * chestLid = new ModelBlockRenderer( 128, 64, 0, 0 );
		 * chestLid.addBox( 1.0F, 0.0F, 0.0F, 30.0F, 5.0F, 14.0F, 0.0F );
		 * chestLid.y = 9.0F;
		 * chestLid.z = 1.0F;
		 *
		 * chestLatch = new ModelBlockRenderer( 128, 64, 0, 0 );
		 * chestLatch.addBox( 15.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F );
		 * chestLatch.y = 8.0F;
		 */
	}
}
