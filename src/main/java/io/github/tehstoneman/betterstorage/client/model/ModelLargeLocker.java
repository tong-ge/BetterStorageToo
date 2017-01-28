package io.github.tehstoneman.betterstorage.client.model;

import io.github.tehstoneman.betterstorage.misc.Resources;
import net.minecraft.util.ResourceLocation;

public class ModelLargeLocker extends ModelLocker
{
	@Override
	protected ResourceLocation modelPath()
	{
		return Resources.modelLockerLarge;
	}
}
