package io.github.tehstoneman.betterstorage.client.model;

import io.github.tehstoneman.betterstorage.client.renderer.Resources;
import net.minecraft.util.ResourceLocation;

public class ModelLargeLocker extends ModelLocker
{
	@Override
	protected ResourceLocation modelPath()
	{
		return Resources.modelLockerLarge;
	}
}
