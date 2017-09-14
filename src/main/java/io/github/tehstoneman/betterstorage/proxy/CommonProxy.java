package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.crafting.Recipes;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLockableDoor;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import io.github.tehstoneman.betterstorage.event.Events;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
	}

	public void initialize()
	{
		Recipes.add();

		MinecraftForge.EVENT_BUS.register( Events.class );
		// FMLCommonHandler.instance().bus().register(this);

		// if (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableChristmasEvent)) new ChristmasEventHandler();

		// registerArmorStandHandlers();
	}

	public void postInit()
	{}

	/*
	 * protected AxisAlignedBB getIronDoorHightlightBox(EntityPlayer player, World world, int x, int y, int z, Vec3 hitVec, Block block) {
	 * if(!StackUtils.isLock(player.getCurrentEquippedItem())) return null;
	 *
	 * int meta = world.getBlockMetadata(x, y, z);
	 * boolean isMirrored;
	 * if(meta >= 8) {
	 * isMirrored = meta == 9;
	 * y -= 1;
	 * meta = world.getBlockMetadata(x, y, z);
	 * }
	 * else isMirrored = world.getBlockMetadata(x, y + 1, z) == 9;
	 *
	 * boolean isOpen = (meta & 4) == 4;
	 * int rotation = (meta & 3);
	 * rotation = isMirrored ? (rotation == 0 ? 3 : rotation == 1 ? 0 : rotation == 2 ? 1 : 2) : rotation;
	 * isOpen = isMirrored ? !isOpen : isOpen;
	 *
	 * AxisAlignedBB box;
	 * switch(rotation) {
	 * case 0 :
	 * if(!isOpen) box = AxisAlignedBB.getBoundingBox(x - 0.005 / 16F, y + 14.5 / 16F, z + 10 / 16F, x + 3.005 / 16F, y + 20.5 / 16F, z + 15 / 16F);
	 * else box = AxisAlignedBB.getBoundingBox(x + 10 / 16F, y + 14.5 / 16F, z - 0.005 / 16F, x + 15 / 16F, y + 20.5 / 16F, z + 3.005 / 16F);
	 * break;
	 * case 1 :
	 * if(!isOpen) box = AxisAlignedBB.getBoundingBox(x + 1 / 16F, y + 14.5 / 16F, z - 0.005 / 16F, x + 6 / 16F, y + 20.5 / 16F, z + 3.005 / 16F);
	 * else box = AxisAlignedBB.getBoundingBox(x + 12.995 / 16F, y + 14.5 / 16F, z + 10 / 16F, x + 16.005 / 16F, y + 20.5 / 16F, z + 15 / 16F);
	 * break;
	 * case 2 :
	 * if(!isOpen) box = AxisAlignedBB.getBoundingBox(x + 12.995 / 16F, y + 14.5 / 16F, z + 1 / 16F, x + 16.005 / 16F, y + 20.5 / 16F, z + 6 / 16F);
	 * else box = AxisAlignedBB.getBoundingBox(x + 1 / 16F, y + 14.5 / 16F, z + 12.995 / 16F, x + 6 / 16F, y + 20.5 / 16F, z + 16.005 / 16F);
	 * break;
	 * default :
	 * if(!isOpen) box = AxisAlignedBB.getBoundingBox(x + 10 / 16F, y + 14.5 / 16F, z + 12.995 / 16F, x + 15 / 16F, y + 20.5 / 16F, z + 16.005 / 16F);
	 * else box = AxisAlignedBB.getBoundingBox(x - 0.005 / 16F, y + 14.5 / 16F, z + 1 / 16F, x + 3.005 / 16F, y + 20.5 / 16F, z + 6 / 16F);
	 * break;
	 * }
	 *
	 * return box.isVecInside(hitVec) ? box : null;
	 * }
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

	/*
	 * @SubscribeEvent
	 * public void onWorldTick(WorldTickEvent event) {
	 * if (event.side == Side.SERVER)
	 * CratePileCollection.getCollection(event.world).onTick();
	 * }
	 */

	/*
	 * @SubscribeEvent
	 * public void onPlayerTick(PlayerTickEvent event) {
	 * if (event.side == Side.SERVER && event.phase == Phase.END) {
	 * //Cleanup in case the backpack is not equipped correctly, due to changing the backpackChestplate setting.
	 * ItemStack stack = event.player.getEquipmentInSlot(EquipmentSlot.CHEST);
	 * if(stack != null && stack.getItem() instanceof ItemBackpack && !BetterStorage.globalConfig.getBoolean(GlobalConfig.backpackChestplate)) {
	 * //First thing that never should happen...
	 * event.player.setCurrentItemOrArmor(EquipmentSlot.CHEST, null);
	 * ItemBackpack.setBackpack(event.player, stack, ItemBackpack.getBackpackData(event.player).contents);
	 * } else if((stack == null || (stack.getItem() != null && !(stack.getItem() instanceof ItemBackpack))) &&
	 * ItemBackpack.getBackpackData(event.player).backpack != null && BetterStorage.globalConfig.getBoolean(GlobalConfig.backpackChestplate)) {
	 * //And that.
	 * ItemStack backpack = ItemBackpack.getBackpack(event.player);
	 * //Not really a good practice, I'd say.
	 * ItemBackpack.getBackpackData(event.player).backpack = null;
	 * if(stack != null) {
	 * //Drop the armor if the player had some and decided to switch the setting anyways.
	 * WorldUtils.dropStackFromEntity(event.player, stack, 4.0F);
	 * }
	 * event.player.setCurrentItemOrArmor(EquipmentSlot.CHEST, backpack);
	 * }
	 * }
	 * }
	 */

	public String localize( String unlocalized, Object... args )
	{
		return I18n.translateToLocalFormatted( unlocalized, args );
	}
}
