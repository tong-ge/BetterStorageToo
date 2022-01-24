package io.github.tehstoneman.betterstorage.misc;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import io.github.tehstoneman.betterstorage.utils.NbtUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

public class ChristmasEventHandler
{

	private static final int	DAYS_BEFORE_CHRISTMAS	= 14;
	private static final int	DAYS_AFTER_CHRISTMAS	= 2;

	public ChristmasEventHandler()
	{
		MinecraftForge.EVENT_BUS.register( this );
		FMLCommonHandler.instance().bus().register( this );
	}

	@SubscribeEvent
	public void onPlayerLogin( PlayerLoggedInEvent event )
	{
		final List< ItemStack > items = getItemsForYear( getYear(), event.player );
		/*
		 * BetterChristmasProperties properties =
		 * EntityUtils.getProperties(event.player, BetterChristmasProperties.class);
		 */

		if( items == null )
			return;

		/*
		 * if (isBeforeChristmas() && (properties.year < getYear())) {
		 * ItemStack book = new ItemStack(BetterStorageItems.presentBook);
		 * 
		 * StackUtils.set(book, getYear(), "year");
		 * StackUtils.set(book, event.player.getUniqueID().toString(), "uuid");
		 * StackUtils.set(book, event.player.getCommandSenderEntity(), "name");
		 * 
		 * event.player.inventory.addItemStackToInventory(book);
		 * properties.year = getYear();
		 * properties.gotPresent = false;
		 * }
		 */

		/*
		 * if (isPresentTime() && !properties.gotPresent) {
		 * Inventory inv = event.player.inventory;
		 * for (int i = 0; i < inv.getContainerSize(); i++) {
		 * ItemStack stack = inv.getItem(i);
		 * if ((stack != null) && (stack.getItem() == BetterStorageItems.presentBook) &&
		 * (StackUtils.get(stack, 9001, "year") == getYear()) &&
		 * event.player.getUniqueID().toString().equals(StackUtils.get(stack, null, "uuid"))) {
		 * ItemStack present = new ItemStack(BetterStorageTiles.present);
		 * 
		 * present.setStackDisplayName("Christmas Present " + getYear());
		 * StackUtils.set(present, event.player.getCommandSenderEntity(), TileEntityPresent.TAG_NAMETAG);
		 * 
		 * int color = DyeUtils.getDyeColor(new ItemStack(Items.dye, 1, 1));
		 * StackUtils.set(present, color, "color");
		 * StackUtils.set(present, (byte)14, TileEntityPresent.TAG_COLOR_INNER);
		 * StackUtils.set(present, (byte)16, TileEntityPresent.TAG_COLOR_OUTER);
		 * 
		 * ItemStack[] contents = new ItemStack[ItemCardboardBox.getRows() * 9];
		 * for (int j = 0; ((j < contents.length) && !items.isEmpty()); j++)
		 * if (RandomUtils.getBoolean((double)items.size() / (contents.length - j)))
		 * contents[j] = items.remove(RandomUtils.getInt(items.size()));
		 * StackUtils.setStackContents(present, contents);
		 * 
		 * if (event.player.getCommandSenderEntity().equalsIgnoreCase("xXxCJxXx")) {
		 * StackUtils.set(present, (byte)1, TileEntityPresent.TAG_SKOJANZA_MODE);
		 * StackUtils.set(present, NbtUtils.createList(
		 * "Just for you!",
		 * "skojanzaMode = true"
		 * ), "display", "Lore");
		 * }
		 * 
		 * inv.setInventorySlotContents(i, present);
		 * properties.gotPresent = true;
		 * break;
		 * }
		 * }
		 * }
		 */
	}

	@SubscribeEvent
	public void onEntityConstructing( EntityConstructing event )
	{
		/*
		 * if (event.getEntity() instanceof EntityPlayerMP)
		 * EntityUtils.createProperties(event.getEntity(), BetterChristmasProperties.class);
		 */
	}

	@SubscribeEvent
	public void onPlayerDeath( LivingDeathEvent event )
	{
		if( !( event.getEntity() instanceof EntityPlayerMP ) )
			return;
		final EntityPlayer player = (EntityPlayer)event.getEntity();
		/*
		 * BetterChristmasProperties properties =
		 * EntityUtils.getProperties(player, BetterChristmasProperties.class);
		 */

		final NBTTagCompound entityData = player.getEntityData();
		final NBTTagCompound persistent = entityData.getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
		entityData.setTag( EntityPlayer.PERSISTED_NBT_TAG, persistent );

		final NBTTagCompound propertiesCompound = new NBTTagCompound();
		/*
		 * properties.saveNBTData(propertiesCompound);
		 * persistent.setTag(BetterChristmasProperties.identifier, propertiesCompound);
		 */
	}

	@SubscribeEvent
	public void onPlayerRespawn( PlayerRespawnEvent event )
	{
		final EntityPlayer player = event.player;
		/*
		 * BetterChristmasProperties properties =
		 * EntityUtils.getProperties(player, BetterChristmasProperties.class);
		 */

		final NBTTagCompound entityData = player.getEntityData();
		final NBTTagCompound persistent = entityData.getCompoundTag( EntityPlayer.PERSISTED_NBT_TAG );
		// NBTTagCompound propertiesCompound = persistent.getCompoundTag(BetterChristmasProperties.identifier);
		// if (propertiesCompound.hasNoTags()) return;

		// properties.loadNBTData(propertiesCompound);

		// persistent.removeTag(BetterChristmasProperties.identifier);
		if( persistent.hasNoTags() )
			entityData.removeTag( EntityPlayer.PERSISTED_NBT_TAG );
	}

	public static int getYear()
	{
		return Calendar.getInstance().get( Calendar.YEAR );
	}

	public static int getMonth()
	{
		return Calendar.getInstance().get( Calendar.MONTH );
	}

	public static int getDay()
	{
		return Calendar.getInstance().get( Calendar.DAY_OF_MONTH );
	}

	public static boolean isBeforeChristmas()
	{
		return getMonth() == Calendar.DECEMBER && getDay() >= 24 - DAYS_BEFORE_CHRISTMAS && getDay() < 24;
	}

	public static boolean isPresentTime()
	{
		return getMonth() == Calendar.DECEMBER && getDay() >= 24 && getDay() <= 24 + DAYS_AFTER_CHRISTMAS;
	}

	private static List< ItemStack > getItemsForYear( int year, EntityPlayer player )
	{
		final List< ItemStack > items = new ArrayList< >();
		switch( year )
		{
		case 2014:
			getItemsFor2014( items, player );
			break;
		default:
			return null;
		}
		return items;
	}

	private static void getItemsFor2014( List< ItemStack > items, EntityPlayer player )
	{

		final boolean vouchers = false;

		/*
		 * ItemStack sword = new ItemStack((BetterStorageItems.cardboardSword != null)
		 * ? BetterStorageItems.cardboardSword : Items.wooden_sword);
		 * sword.addEnchantment(Enchantment.looting, 4);
		 * sword.addEnchantment(Enchantment.knockback, 3);
		 * if (BetterStorageItems.cardboardSword != null) {
		 * sword.setStackDisplayName(player.getCommandSenderEntity() + "'s Magical Sword");
		 * sword.addEnchantment(Enchantment.unbreaking, 2);
		 * int color = DyeUtils.getDyeColor(new ItemStack(Items.dye, 1, 5));
		 * StackUtils.set(sword, color, "display", "color");
		 * StackUtils.set(sword, NbtUtils.createList(
		 * "True strength is not in power,",
		 * "but in overcoming challenges."
		 * ), "display", "Lore");
		 * } else {
		 * sword.setStackDisplayName("Fake Cardboard Sword");
		 * StackUtils.set(sword, NbtUtils.createList(
		 * "Not made of actual cardboard,",
		 * "because some douche disabled it."
		 * ), "display", "Lore");
		 * }
		 * items.add(sword);
		 * 
		 * ItemStack chestplate = new ItemStack((BetterStorageItems.cardboardChestplate != null)
		 * ? BetterStorageItems.cardboardChestplate : Items.leather_chestplate);
		 * chestplate.addEnchantment(Enchantment.protection, 4);
		 * chestplate.addEnchantment(Enchantment.thorns, 3);
		 * if (BetterStorageItems.cardboardChestplate != null) {
		 * chestplate.setStackDisplayName(player.getCommandSenderEntity() + "'s Magical Chestpiece");
		 * chestplate.addEnchantment(Enchantment.unbreaking, 2);
		 * int color = DyeUtils.getDyeColor(new ItemStack(Items.dye, 1, 5));
		 * StackUtils.set(chestplate, color, "display", "color");
		 * StackUtils.set(chestplate, NbtUtils.createList(
		 * "True strength is not in power,",
		 * "but in overcoming challenges."
		 * ), "display", "Lore");
		 * } else {
		 * chestplate.setStackDisplayName("Fake Cardboard Chestplate");
		 * StackUtils.set(chestplate, NbtUtils.createList(
		 * "Not made of actual cardboard,",
		 * "because some douche disabled it."
		 * ), "display", "Lore");
		 * }
		 * items.add(chestplate);
		 * 
		 * if (BetterStorageItems.drinkingHelmet != null) {
		 * ItemStack drinkingHelmet = new ItemStack(BetterStorageItems.drinkingHelmet);
		 * drinkingHelmet.setStackDisplayName("Splash Drinking Helmet");
		 * drinkingHelmet.addEnchantment(Enchantment.protection, 5);
		 * drinkingHelmet.addEnchantment(Enchantment.respiration, 4);
		 * ItemStack speedPotion = new ItemStack(Items.potionitem, 1, 16418);
		 * StackUtils.set(speedPotion, NbtUtils.createList(
		 * NbtUtils.createCompound("Id", 1, "Amplifier", 3, "Duration", 2000),
		 * NbtUtils.createCompound("Id", 9, "Amplifier", 1, "Duration", 1600)
		 * ), "CustomPotionEffects");
		 * ItemStack weaknessPotion = new ItemStack(Items.potionitem, 1, 16424);
		 * StackUtils.set(weaknessPotion, NbtUtils.createList(
		 * NbtUtils.createCompound("Id", 18, "Amplifier", 2, "Duration", 2000)
		 * ), "CustomPotionEffects");
		 * ItemDrinkingHelmet.setPotions(drinkingHelmet, new ItemStack[]{ speedPotion, weaknessPotion });
		 * StackUtils.set(drinkingHelmet, 24, "uses");
		 * items.add(drinkingHelmet);
		 * } else vouchers = createVoucher(items, "Drinking Helmet");
		 * 
		 * ItemStack shears = new ItemStack(Items.shears);
		 * shears.setStackDisplayName("Shears of Destiny");
		 * shears.addEnchantment(Enchantment.silkTouch, 2);
		 * StackUtils.set(shears, NbtUtils.createList("\"Destinyyy!\" -Guude"), "display", "Lore");
		 * items.add(shears);
		 * 
		 * if (BetterStorageItems.slimeBucket != null) {
		 * ItemStack slime = new ItemStack(BetterStorageItems.slimeBucket);
		 * StackUtils.set(slime, "Magma King", "Slime", "name");
		 * StackUtils.set(slime, "LavaSlime", "Slime", "id");
		 * StackUtils.set(slime, NbtUtils.createList(
		 * new PotionEffect(Potion.regeneration.id, 6000, 0).writeCustomPotionEffectToNBT(new NBTTagCompound()),
		 * new PotionEffect(Potion.resistance.id, 6000, 1).writeCustomPotionEffectToNBT(new NBTTagCompound()),
		 * new PotionEffect(Potion.fireResistance.id, 6000, 2).writeCustomPotionEffectToNBT(new NBTTagCompound()),
		 * new PotionEffect(Potion.waterBreathing.id, 6000, 3).writeCustomPotionEffectToNBT(new NBTTagCompound()),
		 * new PotionEffect(Potion.nightVision.id, 6000, 3).writeCustomPotionEffectToNBT(new NBTTagCompound())
		 * ), "Effects");
		 * StackUtils.set(slime, NbtUtils.createList(
		 * "Sneak and use to consume.",
		 * "Don't accidentially release it!"
		 * ), "display", "Lore");
		 * items.add(slime);
		 * } else vouchers = createVoucher(items, "Slime in a Bucket");
		 * 
		 * if (BetterStorageTiles.craftingStation != null)
		 * items.add(new ItemStack(BetterStorageTiles.craftingStation));
		 * else vouchers = createVoucher(items, "Crafting Station");
		 * items.add(new ItemStack(Items.diamond, 8));
		 * items.add(new ItemStack(Items.emerald, 16));
		 * 
		 * if (vouchers) {
		 * ItemStack book = new ItemStack(Items.written_book);
		 * StackUtils.set(book, "Voucher Information", "title");
		 * StackUtils.set(book, "copygirl", "author");
		 * StackUtils.set(book, 3, "generation");
		 * StackUtils.set(book, NbtUtils.createList(
		 * "If you received this book, it means whoever is in charge of " +
		 * "configs decided some items are not worth existing.\n\n" +
		 * "This, of course, is unacceptable. Now it is your job to bug " +
		 * "them about it and demand a way to turn your voucher(s) into " +
		 * "the christmas present items you deserve!"
		 * ), "pages");
		 * items.add(book);
		 * }
		 */

	}

	private static boolean createVoucher( List< ItemStack > items, String itemName )
	{
		final ItemStack voucher = new ItemStack( Items.PAPER );
		voucher.setStackDisplayName( itemName + " Voucher" );
		StackUtils.set( voucher, NbtUtils.createList( "Item: " + itemName, "Redeem at server owner / modpack author." ), "display", "Lore" );
		items.add( voucher );
		return true;
	}

	/*
	 * public static class BetterChristmasProperties implements IExtendedEntityProperties {
	 * 
	 * public static final String identifier = Constants.modId + ".betterChristmas";
	 * 
	 * public int year = 2013;
	 * public boolean gotPresent = false;
	 * 
	 * @Override
	 * public void init(Entity entity, Level world) { }
	 * 
	 * @Override
	 * public void saveNBTData(NBTTagCompound compound) {
	 * NBTTagCompound data = new NBTTagCompound();
	 * data.setShort("year", (short)year);
	 * data.setBoolean("gotPresent", gotPresent);
	 * compound.setTag(identifier, data);
	 * }
	 * 
	 * @Override
	 * public void loadNBTData(NBTTagCompound compound) {
	 * NBTTagCompound data = compound.getCompoundTag(identifier);
	 * if (!data.hasNoTags()) {
	 * year = data.getShort("year");
	 * gotPresent = data.getBoolean("gotPresent");
	 * } else year = compound.getInteger("betterChristmasYear");
	 * }
	 * 
	 * }
	 */

}
