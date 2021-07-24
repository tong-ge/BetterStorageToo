package io.github.tehstoneman.betterstorage.item;

import java.util.ArrayList;
import java.util.List;

import io.github.tehstoneman.betterstorage.common.item.ItemArmorBetterStorage;
import io.github.tehstoneman.betterstorage.misc.Resources;
import io.github.tehstoneman.betterstorage.misc.handlers.KeyBindingHandler;
import io.github.tehstoneman.betterstorage.utils.LanguageUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDrinkingHelmet extends ItemArmorBetterStorage
{
	// private IIcon iconPotions;

	public static final int				maxUses		= 12;

	public static final ArmorMaterial	material	= EnumHelper.addArmorMaterial( "drinkingHelmet", "drinkingHelmet", 11, new int[] { 3, 0, 0, 0 },
			15, SoundEvents.ITEM_ARMOR_EQUIP_GENERIC, 1.0F );
	static
	{
		material.customCraftingMaterial = Item.getItemFromBlock( Blocks.REDSTONE_BLOCK );
	}

	public ItemDrinkingHelmet()
	{
		super( material, 0, EntityEquipmentSlot.HEAD );
	}

	/*
	 * @Override
	 *
	 * @SideOnly(Side.CLIENT)
	 * public void registerIcons(IIconRegister iconRegister) {
	 * itemIcon = iconRegister.registerIcon(Constants.modId + ":" + getItemName());
	 * iconPotions = iconRegister.registerIcon(Constants.modId + ":" + getItemName() + "_potions");
	 * }
	 */

	/*
	 * @Override
	 * public IIcon getIcon(ItemStack stack, int pass) {
	 * return ((StackUtils.get(stack, 0, "uses") > 0) ? iconPotions : itemIcon);
	 * }
	 */

	/*
	 * @Override
	 *
	 * @SideOnly(Side.CLIENT)
	 * public IIcon getIconIndex(ItemStack stack) {
	 * return getIcon(stack, 0);
	 * }
	 */

	@Override
	public String getArmorTexture( ItemStack stack, Entity entity, EntityEquipmentSlot slot, String type )
	{
		return Resources.textureDrinkingHelmet.toString();
	}

	/*
	 * @Override
	 *
	 * @SideOnly(Side.CLIENT)
	 * public ModelBiped getArmorModel(EntityLivingBase entity, ItemStack stack, int slot) {
	 * return ModelDrinkingHelmet.instance;
	 * }
	 */

	@Override
	@SideOnly( Side.CLIENT )
	public EnumRarity getRarity( ItemStack stack )
	{
		return StackUtils.has( stack, "display", "Lore" ) ? EnumRarity.EPIC : EnumRarity.RARE;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void appendHoverText( ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips )
	{
		final int uses = StackUtils.get( stack, 0, "uses" );
		if( uses > 0 && getDrinkingHelmet( player ) == stack )
		{
			final String str = GameSettings.getKeyDisplayString( KeyBindingHandler.drinkingHelmet.getKeyCode() );
			LanguageUtils.translateTooltip( list, "drinkingHelmet.useHint", "%KEY%", str );
		}
		/*
		 * for (ItemStack potion : getPotions(stack)) {
		 * if (potion == null) continue;
		 * String name = potion.getDisplayName();
		 * if ((list.size() > 0) && (list.get(list.size() - 1).equals(EnumChatFormatting.DARK_AQUA + name)))
		 * list.set(list.size() - 1, EnumChatFormatting.DARK_AQUA + "2x " + name);
		 * else list.add(EnumChatFormatting.DARK_AQUA + name);
		 * }
		 */
		/*
		 * if (uses > 0)
		 * list.add(EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC +
		 * LanguageUtils.translateTooltip("drinkingHelmet.uses", "%USES%", Integer.toString(uses)));
		 * // TODO: Remove crafting hint?
		 * else if (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableHelpTooltips))
		 * list.add(LanguageUtils.translateTooltip("drinkingHelmet.craftHint"));
		 */
	}

	public static ItemStack[] getPotions( ItemStack drinkingHelmet )
	{
		return StackUtils.getStackContents( drinkingHelmet, 2 );
	}

	public static void setPotions( ItemStack drinkingHelmet, ItemStack[] potions )
	{
		if( potions != null )
		{
			StackUtils.setStackContents( drinkingHelmet, potions );
			StackUtils.set( drinkingHelmet, maxUses, "uses" );
		}
		else
		{
			StackUtils.remove( drinkingHelmet, "Items" );
			StackUtils.remove( drinkingHelmet, "uses" );
		}
	}

	public static ItemStack getDrinkingHelmet( EntityLivingBase entity )
	{
		/*
		 * ItemStack drinkingHelmet = entity.getEquipmentInSlot(EquipmentSlot.HEAD);
		 * if ((drinkingHelmet != null) && (drinkingHelmet.getItem() instanceof ItemDrinkingHelmet))
		 * return drinkingHelmet;
		 * else
		 */ return null;
	}

	public static void use( EntityPlayer player )
	{

		final ItemStack drinkingHelmet = getDrinkingHelmet( player );
		if( drinkingHelmet == null )
			return;
		int uses = StackUtils.get( drinkingHelmet, 0, "uses" );
		if( uses <= 0 )
			return;

		final ItemStack[] potions = StackUtils.getStackContents( drinkingHelmet, 2 );
		final List< PotionEffect > potionEffects = new ArrayList<>();
		/*
		 * for (ItemStack item : potions)
		 * if (item.getItem() instanceof ItemPotion) {
		 * List<PotionEffect> effects = ((ItemPotion)item.getItem()).getEffects(item);
		 * if (effects != null)
		 * potionEffects.addAll(effects);
		 * }
		 */

		/*
		 * for (PotionEffect effect : potionEffects) {
		 * Potion potion = Potion.potionTypes[effect.getPotionID()];
		 * PotionEffect active = player.getActivePotionEffect(potion);
		 * effect = new SmallPotionEffect(effect, active);
		 * player.addPotionEffect(effect);
		 * }
		 */

		// player.worldObj.playSoundAtEntity(player, "random.drink", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.1F);
		if( --uses <= 0 )
			setPotions( drinkingHelmet, null );
		else
			StackUtils.set( drinkingHelmet, uses, "uses" );

	}
}
