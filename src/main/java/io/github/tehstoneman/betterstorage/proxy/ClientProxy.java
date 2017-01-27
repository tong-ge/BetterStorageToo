package io.github.tehstoneman.betterstorage.proxy;

import java.util.HashMap;
import java.util.Map;

import io.github.tehstoneman.betterstorage.addon.Addon;
import io.github.tehstoneman.betterstorage.client.renderer.BetterStorageRenderingHandler;
import io.github.tehstoneman.betterstorage.client.renderer.TileEntityReinforcedChestRenderer;
import io.github.tehstoneman.betterstorage.content.BetterStorageItems;
import io.github.tehstoneman.betterstorage.content.BetterStorageTiles;
import io.github.tehstoneman.betterstorage.item.locking.KeyColor;
import io.github.tehstoneman.betterstorage.tile.entity.TileEntityReinforcedChest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class ClientProxy extends CommonProxy
{
	// public static int lockerRenderId;
	// public static int armorStandRenderId;
	// public static int backpackRenderId;
	// public static int reinforcedLockerRenderId;
	// public static int lockableDoorRenderId;
	// public static int presentRenderId;

	public static final Map< Class< ? extends TileEntity >, BetterStorageRenderingHandler > renderingHandlers = new HashMap<>();

	@Override
	public void preInit()
	{
		super.preInit();

		BetterStorageTiles.registerItemModels();
		BetterStorageItems.registerItemModels();
	}

	@Override
	public void initialize()
	{
		super.initialize();

		// new KeyBindingHandler();

		registerRenderers();
	}

	@Override
	public void postInit()
	{
		super.postInit();

		Minecraft.getMinecraft().getItemColors().registerItemColorHandler( new KeyColor(), BetterStorageItems.key, BetterStorageItems.lock );
	}

	@Override
	protected void registerArmorStandHandlers()
	{
		super.registerArmorStandHandlers();
		// BetterStorageArmorStand.registerRenderHandler(new VanillaArmorStandRenderHandler());
	}

	private void registerRenderers()
	{

		// registerItemRenderer(BetterStorageItems.itemBackpack, new ItemRendererBackpack(TileEntityBackpack.class));
		// registerItemRenderer(BetterStorageItems.itemEnderBackpack, new ItemRendererBackpack(TileEntityBackpack.class));

		// registerItemRenderer(BetterStorageTiles.reinforcedChest, new ItemRendererContainer(TileEntityReinforcedChest.class));
		// registerItemRenderer(BetterStorageTiles.reinforcedLocker, new ItemRendererContainer(TileEntityReinforcedLocker.class));

		// registerItemRenderer(BetterStorageTiles.cardboardBox, new ItemRendererCardboardBox(BetterStorageTiles.cardboardBox));
		// registerItemRenderer(BetterStorageTiles.present, new ItemRendererContainer(TileEntityPresent.class));

		// RenderingRegistry.registerEntityRenderingHandler(EntityFrienderman.class, new RenderFrienderman());
		// RenderingRegistry.registerEntityRenderingHandler(EntityCluckington.class, new RenderChicken(new ModelCluckington(), 0.4F));

		ClientRegistry.bindTileEntitySpecialRenderer( TileEntityReinforcedChest.class, new TileEntityReinforcedChestRenderer() );
		// lockerRenderId = registerTileEntityRenderer(TileEntityLocker.class, new TileEntityLockerRenderer());
		// armorStandRenderId = registerTileEntityRenderer(TileEntityArmorStand.class, new TileEntityArmorStandRenderer(), false, 0, 1, 0);
		// backpackRenderId = registerTileEntityRenderer(TileEntityBackpack.class, new TileEntityBackpackRenderer(), true, -160, 1.5F, 0.14F);
		// reinforcedLockerRenderId = registerTileEntityRenderer(TileEntityReinforcedLocker.class, new TileEntityLockerRenderer());
		// lockableDoorRenderId = registerTileEntityRenderer(TileEntityLockableDoor.class, new TileEntityLockableDoorRenderer());
		// presentRenderId = registerTileEntityRenderer(TileEntityPresent.class, new TileEntityPresentRenderer());
		// RenderingRegistry.registerBlockHandler(new TileLockableDoorRenderingHandler());
		Addon.registerRenderersAll();

	}

	/*
	 * public static void registerItemRenderer(Block block, IItemRenderer renderer) {
	 * if (block != null)
	 * MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(block), renderer);
	 * }
	 */

	/*
	 * public static void registerItemRenderer(Item item, IItemRenderer renderer) {
	 * if (item != null)
	 * MinecraftForgeClient.registerItemRenderer(item, renderer);
	 * }
	 */

	public static int registerTileEntityRenderer( Class< ? extends TileEntity > tileEntityClass, TileEntitySpecialRenderer renderer,
			boolean render3dInInventory, float rotation, float scale, float yOffset )
	{
		ClientRegistry.bindTileEntitySpecialRenderer( tileEntityClass, renderer );
		final BetterStorageRenderingHandler renderingHandler = new BetterStorageRenderingHandler( tileEntityClass, renderer, render3dInInventory,
				rotation, scale, yOffset );
		// RenderingRegistry.registerBlockHandler(renderingHandler);
		renderingHandlers.put( tileEntityClass, renderingHandler );
		return renderingHandler.getRenderId();
	}

	public static int registerTileEntityRenderer( Class< ? extends TileEntity > tileEntityClass, TileEntitySpecialRenderer renderer )
	{
		return registerTileEntityRenderer( tileEntityClass, renderer, true, 90, 1, 0 );
	}

	/*
	 * @SubscribeEvent
	 * public void drawBlockHighlight(DrawBlockHighlightEvent event) {
	 *
	 * EntityPlayer player = event.player;
	 * World world = player.worldObj;
	 * MovingObjectPosition target = WorldUtils.rayTrace(player, event.partialTicks);
	 *
	 * if ((target == null) || (target.typeOfHit != MovingObjectType.BLOCK)) return;
	 * int x = target.blockX;
	 * int y = target.blockY;
	 * int z = target.blockZ;
	 *
	 * AxisAlignedBB box = null;
	 * Block block = world.getBlock(x, y, z);
	 * TileEntity tileEntity = world.getTileEntity(x, y, z);
	 *
	 * if (block instanceof TileArmorStand)
	 * box = getArmorStandHighlightBox(player, world, x, y, z, target.hitVec);
	 * else if (block == Blocks.iron_door)
	 * box = getIronDoorHightlightBox(player, world, x, y, z, target.hitVec, block);
	 * else if (tileEntity instanceof IHasAttachments)
	 * box = getAttachmentPointsHighlightBox(player, tileEntity, target);
	 *
	 * if (box == null) return;
	 *
	 * double xOff = player.lastTickPosX + (player.posX - player.lastTickPosX) * event.partialTicks;
	 * double yOff = player.lastTickPosY + (player.posY - player.lastTickPosY) * event.partialTicks;
	 * double zOff = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * event.partialTicks;
	 * box.offset(-xOff, -yOff, -zOff);
	 *
	 * GL11.glEnable(GL11.GL_BLEND);
	 * GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	 * GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.4F);
	 * GL11.glLineWidth(2.0F);
	 * GL11.glDisable(GL11.GL_TEXTURE_2D);
	 * GL11.glDepthMask(false);
	 *
	 * RenderGlobal.drawOutlinedBoundingBox(box, -1);
	 *
	 * GL11.glDepthMask(true);
	 * GL11.glEnable(GL11.GL_TEXTURE_2D);
	 * GL11.glDisable(GL11.GL_BLEND);
	 *
	 * event.setCanceled(true);
	 *
	 * }
	 */

	/*
	 * private AxisAlignedBB getArmorStandHighlightBox(EntityPlayer player, World world, int x, int y, int z, Vec3 hitVec) {
	 *
	 * int metadata = world.getBlockMetadata(x, y, z);
	 * if (metadata > 0) y -= 1;
	 *
	 * TileEntityArmorStand armorStand = WorldUtils.get(world, x, y, z, TileEntityArmorStand.class);
	 * if (armorStand == null) return null;
	 *
	 * int slot = Math.max(0, Math.min(3, (int)((hitVec.yCoord - y) * 2)));
	 *
	 * double minX = x + 2 / 16.0;
	 * double minY = y + slot / 2.0;
	 * double minZ = z + 2 / 16.0;
	 * double maxX = x + 14 / 16.0;
	 * double maxY = y + slot / 2.0 + 0.5;
	 * double maxZ = z + 14 / 16.0;
	 *
	 * EnumArmorStandRegion region = EnumArmorStandRegion.values()[slot];
	 * for (ArmorStandEquipHandler handler : BetterStorageArmorStand.getEquipHandlers(region)) {
	 * ItemStack item = armorStand.getItem(handler);
	 * if (player.isSneaking()) {
	 * // Check if we can swap the player's equipped armor with armor stand's.
	 * ItemStack equipped = handler.getEquipment(player);
	 * if (((item == null) && (equipped == null)) ||
	 * ((item != null) && !handler.isValidItem(player, item)) ||
	 * ((equipped != null) && !handler.isValidItem(player, equipped)) ||
	 * !handler.canSetEquipment(player, item)) continue;
	 * return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	 * } else {
	 * // Check if we can swap the player's held item with armor stand's.
	 * ItemStack holding = player.getCurrentEquippedItem();
	 * if (((item == null) && (holding == null)) ||
	 * ((holding != null) && !handler.isValidItem(player, holding))) continue;
	 * return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	 * }
	 * }
	 *
	 * return AxisAlignedBB.getBoundingBox(minX, y, minZ, maxX, y + 2, maxZ);
	 *
	 * }
	 */

	/*
	 * private AxisAlignedBB getAttachmentPointsHighlightBox(EntityPlayer player, TileEntity tileEntity,
	 * MovingObjectPosition target) {
	 * Attachments attachments = ((IHasAttachments)tileEntity).getAttachments();
	 * Attachment attachment = attachments.get(target.subHit);
	 * if (attachment == null) return null;
	 * return attachment.getHighlightBox();
	 * }
	 */

	/*
	 * @SubscribeEvent
	 * public void onRenderPlayerSpecialsPre(RenderPlayerEvent.Specials.Pre event) {
	 * ItemStack backpack = ItemBackpack.getBackpackData(event.entityPlayer).backpack;
	 * if (backpack != null) {
	 *
	 * EntityPlayer player = event.entityPlayer;
	 * float partial = event.partialRenderTick;
	 * ItemBackpack backpackType = (ItemBackpack)backpack.getItem();
	 * int color = backpackType.getColor(backpack);
	 * ModelBackpackArmor model = (ModelBackpackArmor)backpackType.getArmorModel(player, backpack, 0);
	 *
	 * model.onGround = ReflectionUtils.invoke(
	 * RendererLivingEntity.class, event.renderer, "func_77040_d", "renderSwingProgress",
	 * EntityLivingBase.class, float.class, player, partial);
	 * model.setLivingAnimations(player, 0, 0, partial);
	 *
	 * RenderUtils.bindTexture(new ResourceLocation(backpackType.getArmorTexture(backpack, player, 0, null)));
	 * RenderUtils.setColorFromInt((color >= 0) ? color : 0xFFFFFF);
	 * model.render(player, 0, 0, 0, 0, 0, 0);
	 *
	 * if (color >= 0) {
	 * RenderUtils.bindTexture(new ResourceLocation(backpackType.getArmorTexture(backpack, player, 0, "overlay")));
	 * GL11.glColor3f(1.0F, 1.0F, 1.0F);
	 * model.render(player, 0, 0, 0, 0, 0, 0);
	 * }
	 *
	 * if (backpack.isItemEnchanted()) {
	 * float f9 = player.ticksExisted + partial;
	 *
	 * RenderUtils.bindTexture(Resources.enchantedEffect);
	 *
	 * GL11.glEnable(GL11.GL_BLEND);
	 * GL11.glColor4f(0.5F, 0.5F, 0.5F, 1.0F);
	 * GL11.glDepthFunc(GL11.GL_EQUAL);
	 * GL11.glDepthMask(false);
	 * for (int k = 0; k < 2; ++k) {
	 * GL11.glDisable(GL11.GL_LIGHTING);
	 * float f11 = 0.76F;
	 * GL11.glColor4f(0.5F * f11, 0.25F * f11, 0.8F * f11, 1.0F);
	 * GL11.glBlendFunc(GL11.GL_SRC_COLOR, GL11.GL_ONE);
	 * GL11.glMatrixMode(GL11.GL_TEXTURE);
	 * GL11.glLoadIdentity();
	 * float f12 = f9 * (0.001F + k * 0.003F) * 20.0F;
	 * float f13 = 0.33333334F;
	 * GL11.glScalef(f13, f13, f13);
	 * GL11.glRotatef(30.0F - k * 60.0F, 0.0F, 0.0F, 1.0F);
	 * GL11.glTranslatef(0.0F, f12, 0.0F);
	 * GL11.glMatrixMode(GL11.GL_MODELVIEW);
	 * model.render(player, 0, 0, 0, 0, 0, 0);
	 * }
	 * GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	 * GL11.glMatrixMode(GL11.GL_TEXTURE);
	 * GL11.glDepthMask(true);
	 * GL11.glLoadIdentity();
	 * GL11.glMatrixMode(GL11.GL_MODELVIEW);
	 * GL11.glEnable(GL11.GL_LIGHTING);
	 * GL11.glDisable(GL11.GL_BLEND);
	 * GL11.glDepthFunc(GL11.GL_LEQUAL);
	 * }
	 *
	 * } else backpack = ItemBackpack.getBackpack(event.entityPlayer);
	 * if (backpack != null) event.renderCape = false;
	 * }
	 */
}
