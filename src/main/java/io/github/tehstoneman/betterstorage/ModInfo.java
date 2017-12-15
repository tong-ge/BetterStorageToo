package io.github.tehstoneman.betterstorage;

public final class ModInfo
{
	public static final String	modId						= "betterstorage";
	public static final String	modName						= "BetterStorageToo";
	public static final String	modVersion					= "@mod_major_version@.@api_major_version@.@minor_version@.@patch_version@";
	public static final String	dependencies				= "required-after:forge@[13.20.0.2228,); after:jei;";
	public static final String	acceptedMC					= "@minecraft_version@";
	public static final String	guiFactory					= "io.github.tehstoneman.betterstorage.client.gui.BetterStorageGuiFactory";
	public static final String	updateJson					= "http://tehstoneman.github.io/" + modId + ".json";

	public static final String	commonProxy					= "io.github.tehstoneman.betterstorage.proxy.CommonProxy";
	public static final String	clientProxy					= "io.github.tehstoneman.betterstorage.proxy.ClientProxy";

	public static final String	containerCrate				= "container." + modId + ".crate";
	public static final String	containerCapacity			= "container." + modId + ".crate.capacity";
	public static final String	containerReinforcedChest	= "container." + modId + ".reinforced_chest";
	public static final String	containerLocker				= "container." + modId + ".locker";
	public static final String	containerReinforcedLocker	= "container." + modId + ".reinforced_locker";
	public static final String	containerArmorStand			= "container." + modId + ".armorStand";
	public static final String	containerCardboardBox		= "container." + modId + ".cardboard_box";
	public static final String	containerCraftingStation	= "container." + modId + ".crafting_station";
	public static final String	containerPresent			= "container." + modId + ".present";

	public static final String	containerKeyring			= "container." + modId + ".keyring";

	public static final String	lockableDoor				= modId + ".lockableDoor";
}
