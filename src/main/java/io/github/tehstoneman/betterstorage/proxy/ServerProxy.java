package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.api.IProxy;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy
{
	@Override
	public void setup( FMLCommonSetupEvent event )
	{
		// TODO Auto-generated method stub
	}

	// private final boolean preventSlimeBucketUse = false;

	/*
	 * public void preInit()
	 * {
	 * // NetworkRegistry.INSTANCE.registerGuiHandler( BetterStorage.instance, new BetterStorageGUIHandler() );
	 *
	 *
	 * if( BetterStorage.config.cardboardSheetEnabled )
	 * {
	 * BetterStorageItems.CARDBOARD_SHEET.register();
	 * OreDictionary.registerOre( "sheetCardboard", BetterStorageItems.CARDBOARD_SHEET );
	 * }
	 *
	 * if( BetterStorage.config.cardboardBoxEnabled )
	 * {
	 * BetterStorageBlocks.CARDBOARD_BOX.registerBlock();
	 * GameRegistry.registerTileEntity( TileEntityCardboardBox.class, ModInfo.containerCardboardBox );
	 * }
	 *
	 * if( BetterStorage.config.cardboardSwordEnabled )
	 * BetterStorageItems.CARDBOARD_SWORD.register();
	 * if( BetterStorage.config.cardboardShovelEnabled )
	 * BetterStorageItems.CARDBOARD_SHOVEL.register();
	 * if( BetterStorage.config.cardboardPickaxeEnabled )
	 * BetterStorageItems.CARDBOARD_PICKAXE.register();
	 * if( BetterStorage.config.cardboardAxeEnabled )
	 * BetterStorageItems.CARDBOARD_AXE.register();
	 * if( BetterStorage.config.cardboardHoeEnabled )
	 * BetterStorageItems.CARDBOARD_HOE.register();
	 *
	 * if( BetterStorage.config.cardboardHelmetEnabled )
	 * BetterStorageItems.CARDBOARD_HELMET.register( "cardboard_helmet" );
	 * if( BetterStorage.config.cardboardChestplateEnabled )
	 * BetterStorageItems.CARDBOARD_CHESTPLATE.register( "cardboard_chestplate" );
	 * if( BetterStorage.config.cardboardLeggingsEnabled )
	 * BetterStorageItems.CARDBOARD_LEGGINGS.register( "cardboard_leggings" );
	 * if( BetterStorage.config.cardboardBootsEnabled )
	 * BetterStorageItems.CARDBOARD_BOOTS.register( "cardboard_boots" );
	 *
	 * if( BetterStorage.config.slimeBucketEnabled )
	 * BetterStorageItems.SLIME_BUCKET.register();
	 *
	 * }
	 */

	/*
	 * public void initialize()
	 * {
	 * Recipes.add();
	 *
	 * // FMLCommonHandler.instance().bus().register(this);
	 *
	 * // if (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableChristmasEvent)) new ChristmasEventHandler();
	 *
	 * // registerArmorStandHandlers();
	 * }
	 */

	/*
	 * public void postInit()
	 * {}
	 */

	/*
	 * @SubscribeEvent
	 * public void onBreakSpeed(BreakSpeed event) {
	 * // Stupid Forge not firing PlayerInteractEvent for left-clicks!
	 * // This is a workaround to instead make blocks appear unbreakable.
	 * EntityPlayer player = event.entityPlayer;
	 * ItemStack holding = player.getCurrentEquippedItem();
	 * if ((holding != null) && (holding.getItem() instanceof ICardboardItem) &&
	 * !ItemCardboardSheet.isEffective(holding))
	 * event.newSpeed = -1;
	 * }
	 */

	/*
	 * public String localize( String unlocalized, Object... args )
	 * {
	 * // return I18n.translateToLocalFormatted( unlocalized, args );
	 * return unlocalized;
	 * }
	 */
}
