package io.github.tehstoneman.betterstorage.content;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.config.GlobalConfig;
import io.github.tehstoneman.betterstorage.item.cardboard.ItemCardboardSheet;
import io.github.tehstoneman.betterstorage.item.locking.ItemKey;
import io.github.tehstoneman.betterstorage.item.locking.ItemKeyring;
import io.github.tehstoneman.betterstorage.item.locking.ItemLock;
import io.github.tehstoneman.betterstorage.item.locking.ItemMasterKey;
import io.github.tehstoneman.betterstorage.utils.MiscUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public final class BetterStorageItems
{
	public static ItemKey	key;
	public static ItemLock	lock;
	public static ItemKeyring keyring;
	public static ItemMasterKey masterKey;
	public static ItemCardboardSheet cardboardSheet;
	// public static ItemDrinkingHelmet drinkingHelmet;
	// public static ItemBucketSlime slimeBucket;
	// public static ItemPresentBook presentBook;

	// public static ItemBackpack itemBackpack;
	// public static ItemEnderBackpack itemEnderBackpack;

	// public static ItemCardboardArmor cardboardHelmet;
	// public static ItemCardboardArmor cardboardChestplate;
	// public static ItemCardboardArmor cardboardLeggings;
	// public static ItemCardboardArmor cardboardBoots;

	// public static ItemCardboardSword cardboardSword;
	// public static ItemCardboardPickaxe cardboardPickaxe;
	// public static ItemCardboardShovel cardboardShovel;
	// public static ItemCardboardAxe cardboardAxe;
	// public static ItemCardboardHoe cardboardHoe;

	public static boolean	anyCardboardItemsEnabled;

	private BetterStorageItems()
	{}

	public static void initialize()
	{
		key = MiscUtils.conditionalNew( ItemKey.class, GlobalConfig.keyEnabled );
		lock = MiscUtils.conditionalNew( ItemLock.class, GlobalConfig.lockEnabled );
		keyring = MiscUtils.conditionalNew(ItemKeyring.class, GlobalConfig.keyringEnabled);
		masterKey = MiscUtils.conditionalNew(ItemMasterKey.class, GlobalConfig.masterKeyEnabled);
		cardboardSheet = MiscUtils.conditionalNew(ItemCardboardSheet.class, GlobalConfig.cardboardSheetEnabled);
		// drinkingHelmet = MiscUtils.conditionalNew(ItemDrinkingHelmet.class, GlobalConfig.drinkingHelmetEnabled);
		// slimeBucket = MiscUtils.conditionalNew(ItemBucketSlime.class, GlobalConfig.slimeBucketEnabled);
		// presentBook = new ItemPresentBook();

		// itemBackpack = MiscUtils.conditionalNew(ItemBackpack.class, GlobalConfig.backpackEnabled);
		// itemEnderBackpack = MiscUtils.conditionalNew(ItemEnderBackpack.class, GlobalConfig.enderBackpackEnabled);

		// cardboardHelmet = conditionalNewArmor(GlobalConfig.cardboardHelmetEnabled, 0);
		// cardboardChestplate = conditionalNewArmor(GlobalConfig.cardboardChestplateEnabled, 1);
		// cardboardLeggings = conditionalNewArmor(GlobalConfig.cardboardLeggingsEnabled, 2);
		// cardboardBoots = conditionalNewArmor(GlobalConfig.cardboardBootsEnabled, 3);

		// cardboardSword = MiscUtils.conditionalNew(ItemCardboardSword.class, GlobalConfig.cardboardSwordEnabled);
		// cardboardPickaxe = MiscUtils.conditionalNew(ItemCardboardPickaxe.class, GlobalConfig.cardboardPickaxeEnabled);
		// cardboardShovel = MiscUtils.conditionalNew(ItemCardboardShovel.class, GlobalConfig.cardboardShovelEnabled);
		// cardboardAxe = MiscUtils.conditionalNew(ItemCardboardAxe.class, GlobalConfig.cardboardAxeEnabled);
		// cardboardHoe = MiscUtils.conditionalNew(ItemCardboardHoe.class, GlobalConfig.cardboardHoeEnabled);

		/*
		 * anyCardboardItemsEnabled = ((BetterStorageItems.cardboardHelmet != null) ||
		 * (BetterStorageItems.cardboardChestplate != null) ||
		 * (BetterStorageItems.cardboardLeggings != null) ||
		 * (BetterStorageItems.cardboardBoots != null) ||
		 * (BetterStorageItems.cardboardSword != null) ||
		 * (BetterStorageItems.cardboardPickaxe != null) ||
		 * (BetterStorageItems.cardboardAxe != null) ||
		 * (BetterStorageItems.cardboardShovel != null) ||
		 * (BetterStorageItems.cardboardHoe != null));
		 */

		if (cardboardSheet != null) OreDictionary.registerOre("sheetCardboard", cardboardSheet);

		Addon.initializeItemsAll();
	}

	/*
	 * private static ItemCardboardArmor conditionalNewArmor( String configName, int armorType )
	 * {
	 * if( !BetterStorage.globalConfig.getBoolean( configName ) )
	 * return null;
	 * return new ItemCardboardArmor( armorType );
	 * }
	 */

	@SideOnly( Side.CLIENT )
	public static void registerItemModels()
	{
		key.registerItemModels();
		lock.registerItemModels();
		keyring.registerItemModels();
		masterKey.registerItemModels();
		cardboardSheet.registerItemModels();
	}
}
