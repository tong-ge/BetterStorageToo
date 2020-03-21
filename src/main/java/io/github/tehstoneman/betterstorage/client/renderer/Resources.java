package io.github.tehstoneman.betterstorage.client.renderer;

import io.github.tehstoneman.betterstorage.util.BetterStorageResource;
import net.minecraft.util.ResourceLocation;

public class Resources
{
	// Generic textures
	public static final BetterStorageResource	TEXTURE_EMPTY						= new BetterStorageResource( "textures/empty.png" );
	public static final BetterStorageResource	TEXTURE_WHITE						= new BetterStorageResource( "white" );

	// Gui Textures
	public static final BetterStorageResource	CONTAINER_CRATE						= new BetterStorageResource( "textures/gui/crate.png" );
	public static final BetterStorageResource	CONTAINER_EXPANDABLE				= new BetterStorageResource( "textures/gui/reinforcedchest.png" );
	public static final ResourceLocation		CONTAINER_GENERIC					= new ResourceLocation( "textures/gui/container/generic_54.png" );

	// Model textures
	public static final BetterStorageResource	TEXTURE_CHEST_REINFORCED			= new BetterStorageResource( "entity/chest/reinforced" );
	public static final BetterStorageResource	TEXTURE_CHEST_REINFORCED_DOUBLE		= new BetterStorageResource( "entity/chest/reinforced_double" );
	public static final BetterStorageResource	TEXTURE_LOCKER_NORMAL				= new BetterStorageResource( "entity/locker/normal" );
	public static final BetterStorageResource	TEXTURE_LOCKER_NORMAL_DOUBLE		= new BetterStorageResource( "entity/locker/normal_double" );
	public static final BetterStorageResource	TEXTURE_LOCKER_REINFORCED			= new BetterStorageResource( "entity/locker/reinforced" );
	public static final BetterStorageResource	TEXTURE_LOCKER_REINFORCED_DOUBLE	= new BetterStorageResource( "entity/locker/reinforced_double" );

	public static final BetterStorageResource	TEXTURE_CARDBOARD_ARMOR				= new BetterStorageResource(
			"textures/models/armor/cardboard_armor.png" );
	public static final BetterStorageResource	TEXTURE_CARDBOARD_LEGGINGS			= new BetterStorageResource(
			"textures/models/armor/cardboard_armor_leggings.png" );
}
