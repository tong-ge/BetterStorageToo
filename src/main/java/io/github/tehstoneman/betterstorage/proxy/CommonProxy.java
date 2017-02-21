package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy
{

	private final boolean preventSlimeBucketUse = false;

	public void preInit()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler( BetterStorage.instance, new BetterStorageGUIHandler() );
		BetterStorageBlocks.registerBlocks();
		BetterStorageItems.initialize();
	}

	public void initialize()
	{

		MinecraftForge.EVENT_BUS.register( this );
		// FMLCommonHandler.instance().bus().register(this);

		// if (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableChristmasEvent)) new ChristmasEventHandler();

		// registerArmorStandHandlers();
	}

	public void postInit()
	{
		// TODO Auto-generated method stub
	}

	protected void registerArmorStandHandlers()
	{

		// BetterStorageArmorStand.helmet = new VanillaArmorStandEquipHandler(EnumArmorStandRegion.HEAD);
		// BetterStorageArmorStand.chestplate = new VanillaArmorStandEquipHandler(EnumArmorStandRegion.CHEST);
		// BetterStorageArmorStand.leggins = new VanillaArmorStandEquipHandler(EnumArmorStandRegion.LEGS);
		// BetterStorageArmorStand.boots = new VanillaArmorStandEquipHandler(EnumArmorStandRegion.FEET);

		// BetterStorageArmorStand.registerEquipHandler(BetterStorageArmorStand.helmet);
		// BetterStorageArmorStand.registerEquipHandler(BetterStorageArmorStand.chestplate);
		// BetterStorageArmorStand.registerEquipHandler(BetterStorageArmorStand.leggins);
		// BetterStorageArmorStand.registerEquipHandler(BetterStorageArmorStand.boots);

	}

	/*
	 * @SubscribeEvent
	 * public void onWorldUnload(Unload event) {
	 * CratePileCollection.unload(event.world);
	 * }
	 */

	/*
	 * @SubscribeEvent
	 * public void onPlayerInteract(PlayerInteractEvent event) {
	 *
	 * World world = event.entity.worldObj;
	 * int x = event.x;
	 * int y = event.y;
	 * int z = event.z;
	 * EntityPlayer player = event.entityPlayer;
	 * ItemStack holding = player.getCurrentEquippedItem();
	 * Block block = world.getBlock(x, y, z);
	 * boolean leftClick = (event.action == Action.LEFT_CLICK_BLOCK);
	 * boolean rightClick = (event.action == Action.RIGHT_CLICK_BLOCK);
	 *
	 * // Interact with attachments.
	 * if (leftClick || rightClick) {
	 * IHasAttachments hasAttachments =
	 * WorldUtils.get(world, x, y, z, IHasAttachments.class);
	 * if (hasAttachments != null) {
	 * EnumAttachmentInteraction interactionType =
	 * ((event.action == Action.LEFT_CLICK_BLOCK)
	 * ? EnumAttachmentInteraction.attack
	 * : EnumAttachmentInteraction.use);
	 * if (hasAttachments.getAttachments().interact(WorldUtils.rayTrace(player, 1.0F),
	 * player, interactionType)) {
	 * event.useBlock = Result.DENY;
	 * event.useItem = Result.DENY;
	 * }
	 * }
	 * }
	 *
	 * // Use cauldron to remove color from dyable items
	 * if (rightClick && (block == Blocks.cauldron)) {
	 * int metadata = world.getBlockMetadata(x, y, z);
	 * if (metadata > 0) {
	 * IDyeableItem dyeable = (((holding != null) && (holding.getItem() instanceof IDyeableItem))
	 * ? (IDyeableItem)holding.getItem() : null);
	 * if ((dyeable != null) && (dyeable.canDye(holding))) {
	 * StackUtils.remove(holding, "display", "color");
	 * world.setBlockMetadataWithNotify(x, y, z, metadata - 1, 2);
	 * world.func_147453_f(x, y, z, block);
	 *
	 * event.useBlock = Result.DENY;
	 * event.useItem = Result.DENY;
	 * }
	 * }
	 * }
	 *
	 * // Prevent players from breaking blocks with broken cardboard items.
	 * if (leftClick && (holding != null) &&
	 * (holding.getItem() instanceof ICardboardItem) &&
	 * !ItemCardboardSheet.isEffective(holding))
	 * event.useItem = Result.DENY;
	 *
	 * // Attach locks to iron doors.
	 * if (!world.isRemote && BetterStorageTiles.lockableDoor != null && rightClick && block == Blocks.iron_door) {
	 * MovingObjectPosition target = WorldUtils.rayTrace(player, 1F);
	 * if(target != null && getIronDoorHightlightBox(player, world, x, y, z, target.hitVec, block) != null) {
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
	 * int rotation = meta & 3;
	 * ForgeDirection orientation = rotation == 0 ? ForgeDirection.WEST : rotation == 1 ? ForgeDirection.NORTH : rotation == 2 ? ForgeDirection.EAST :
	 * ForgeDirection.SOUTH;
	 * orientation = isMirrored ? (
	 * orientation == ForgeDirection.WEST ? ForgeDirection.SOUTH :
	 * orientation == ForgeDirection.NORTH ? ForgeDirection.WEST :
	 * orientation == ForgeDirection.EAST ? ForgeDirection.NORTH :
	 * ForgeDirection.EAST) : orientation;
	 *
	 * world.setBlock(x, y, z, BetterStorageTiles.lockableDoor, 0, SetBlockFlag.SEND_TO_CLIENT);
	 * world.setBlock(x, y + 1, z, BetterStorageTiles.lockableDoor, 8, SetBlockFlag.SEND_TO_CLIENT);
	 *
	 * TileEntityLockableDoor te = WorldUtils.get(world, x, y, z, TileEntityLockableDoor.class);
	 * te.orientation = orientation;
	 * te.isOpen = isMirrored;
	 * te.isMirrored = isMirrored;
	 * te.setLock(holding);
	 *
	 * player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
	 * }
	 * }
	 *
	 * // Prevent eating of slime buckets after capturing them.
	 * if (preventSlimeBucketUse) {
	 * event.setCanceled(true);
	 * preventSlimeBucketUse = false;
	 * }
	 *
	 * }
	 */

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
	
	public String localize(String unlocalized, Object... args )
	{
		return I18n.translateToLocalFormatted( unlocalized, args );
	}
}
