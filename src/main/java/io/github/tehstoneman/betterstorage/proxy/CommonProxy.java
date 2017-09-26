package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.crafting.Recipes;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCardboardBox;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

public class CommonProxy
{

	private final boolean preventSlimeBucketUse = false;

	public void preInit()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler( BetterStorage.instance, new BetterStorageGUIHandler() );

		// Register blocks and items
		if( BetterStorage.config.crateEnabled )
		{
			BetterStorageBlocks.CRATE.registerBlock();
			GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
		}
		if( BetterStorage.config.reinforcedChestEnabled )
		{
			BetterStorageBlocks.REINFORCED_CHEST.registerBlock();
			GameRegistry.registerTileEntity( TileEntityReinforcedChest.class, ModInfo.containerReinforcedChest );
		}
		if( BetterStorage.config.lockerEnabled )
		{
			BetterStorageBlocks.LOCKER.registerBlock();
			GameRegistry.registerTileEntity( TileEntityLocker.class, ModInfo.containerLocker );
			if( BetterStorage.config.reinforcedLockerEnabled )
			{
				BetterStorageBlocks.REINFORCED_LOCKER.registerBlock();
				GameRegistry.registerTileEntity( TileEntityReinforcedLocker.class, ModInfo.containerReinforcedLocker );
			}
		}
		if( BetterStorage.config.flintBlockEnabled )
			BetterStorageBlocks.BLOCK_FLINT.registerBlock();

		if( BetterStorage.config.keyEnabled )
		{
			BetterStorageItems.KEY.register();
			if( BetterStorage.config.masterKeyEnabled )
				BetterStorageItems.MASTER_KEY.register();
			if( BetterStorage.config.keyringEnabled )
				BetterStorageItems.KEYRING.register();
			if( BetterStorage.config.lockEnabled )
				BetterStorageItems.LOCK.register();
			if( BetterStorage.config.lockableDoorEnabled )
			{
				BetterStorageBlocks.LOCKABLE_DOOR.registerBlock();
				GameRegistry.registerTileEntity( TileEntityLockableDoor.class, ModInfo.lockableDoor );
			}
		}

		if( BetterStorage.config.cardboardSheetEnabled )
		{
			BetterStorageItems.CARDBOARD_SHEET.register();
			OreDictionary.registerOre( "sheetCardboard", BetterStorageItems.CARDBOARD_SHEET );
		}

		if( BetterStorage.config.cardboardBoxEnabled )
		{
			BetterStorageBlocks.CARDBOARD_BOX.registerBlock();
			GameRegistry.registerTileEntity( TileEntityCardboardBox.class, ModInfo.containerCardboardBox );
		}

		if( BetterStorage.config.cardboardSwordEnabled )
			BetterStorageItems.CARDBOARD_SWORD.register();
		if( BetterStorage.config.cardboardShovelEnabled )
			BetterStorageItems.CARDBOARD_SHOVEL.register();
		if( BetterStorage.config.cardboardPickaxeEnabled )
			BetterStorageItems.CARDBOARD_PICKAXE.register();
		if( BetterStorage.config.cardboardAxeEnabled )
			BetterStorageItems.CARDBOARD_AXE.register();
		if( BetterStorage.config.cardboardHoeEnabled )
			BetterStorageItems.CARDBOARD_HOE.register();

		if( BetterStorage.config.cardboardHelmetEnabled )
			BetterStorageItems.CARDBOARD_HELMET.register( "cardboard_helmet" );
		if( BetterStorage.config.cardboardChestplateEnabled )
			BetterStorageItems.CARDBOARD_CHESTPLATE.register( "cardboard_chestplate" );
		if( BetterStorage.config.cardboardLeggingsEnabled )
			BetterStorageItems.CARDBOARD_LEGGINGS.register( "cardboard_leggings" );
		if( BetterStorage.config.cardboardBootsEnabled )
			BetterStorageItems.CARDBOARD_BOOTS.register( "cardboard_boots" );
	}

	public void initialize()
	{
		Recipes.add();

		// FMLCommonHandler.instance().bus().register(this);

		// if (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableChristmasEvent)) new ChristmasEventHandler();

		// registerArmorStandHandlers();
	}

	public void postInit()
	{}

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
	 * @SubscribeEvent
	 * public void onEntityInteract(EntityInteractEvent event) {
	 *
	 * if (event.entity.worldObj.isRemote || event.isCanceled()) return;
	 *
	 * EntityPlayer player = event.entityPlayer;
	 * Entity target = event.target;
	 * ItemStack holding = player.getCurrentEquippedItem();
	 *
	 * if ((target.getClass() == EntityChicken.class) &&
	 * (holding != null) && (holding.getItem() == Items.name_tag)) {
	 *
	 * EntityChicken chicken = (EntityChicken)target;
	 * if (!chicken.isDead && !chicken.isChild() &&
	 * "Cluckington".equals(holding.getDisplayName()))
	 * EntityCluckington.spawn(chicken);
	 *
	 * }
	 *
	 * if ((BetterStorageItems.slimeBucket != null) && (target instanceof EntityLiving) &&
	 * (holding != null) && (holding.getItem() == Items.bucket)) {
	 * ItemBucketSlime.pickUpSlime(player, (EntityLiving)target);
	 * if (player.getCurrentEquippedItem().getItem() instanceof ItemBucketSlime)
	 * preventSlimeBucketUse = true;
	 * }
	 *
	 * }
	 */

	public String localize( String unlocalized, Object... args )
	{
		return I18n.translateToLocalFormatted( unlocalized, args );
	}
}
