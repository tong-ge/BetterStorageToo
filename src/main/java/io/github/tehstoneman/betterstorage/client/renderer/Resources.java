package io.github.tehstoneman.betterstorage.client.renderer;

import io.github.tehstoneman.betterstorage.client.BetterStorageResource;
import net.minecraft.util.ResourceLocation;

//@SideOnly( Side.CLIENT )
public final class Resources
{
	public static final ResourceLocation	textureEmpty				= new BetterStorageResource( "textures/empty.png" );
	public static final ResourceLocation	enchantedEffect				= new ResourceLocation( "textures/misc/enchanted_item_glint.png" );

	public static final ResourceLocation	containerCrate				= new BetterStorageResource( "textures/gui/crate.png" );
	public static final ResourceLocation	containerReinforcedChest	= new BetterStorageResource( "textures/gui/reinforcedchest.png" );
	// public static final ResourceLocation containerCraftingStation = new BetterStorageResource( "textures/gui/craftingStation.png" );

	public static final ResourceLocation	textureLocker				= new BetterStorageResource( "textures/models/locker/wood.png" );
	public static final ResourceLocation	textureLockerLarge			= new BetterStorageResource( "textures/models/locker_large/wood.png" );
	// public static final ResourceLocation textureArmorStand = new BetterStorageResource( "textures/models/armorstand.png" );
	// public static final ResourceLocation textureBackpack = new BetterStorageResource( "textures/models/backpack.png" );
	// public static final ResourceLocation textureBackpackOverlay = new BetterStorageResource( "textures/models/backpack_overlay.png" );
	// public static final ResourceLocation textureEnderBackpack = new BetterStorageResource( "textures/models/enderBackpack.png" );
	public static final ResourceLocation	textureDrinkingHelmet		= new BetterStorageResource( "textures/models/drinkingHelmet.png" );
	public static final ResourceLocation	textureCardboardArmor		= new BetterStorageResource( "textures/models/cardboard_armor.png" );
	public static final ResourceLocation	textureCardboardLeggings	= new BetterStorageResource( "textures/models/cardboard_armor_leggings.png" );
	// public static final ResourceLocation textureCluckOverlay = new BetterStorageResource( "textures/models/cluck.png" );
	public static final ResourceLocation	texturePresentOverlay		= new BetterStorageResource( "textures/models/present_overlay.png" );

	public static final ResourceLocation	modelLocker					= new BetterStorageResource( "models/locker.obj" );
	public static final ResourceLocation	modelLockerLarge			= new BetterStorageResource( "models/locker_large.obj" );

	public static final ResourceLocation	modelPresent				= new BetterStorageResource( "models/present.obj" );

	private Resources()
	{}
}
