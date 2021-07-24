package io.github.tehstoneman.betterstorage.common.item.cardboard;

import java.util.function.Supplier;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;

public enum BetterStorageArmorMaterial implements IArmorMaterial
{
	CARDBOARD( "cardboard", 3, new int[] { 1, 2, 3, 1 }, 12, SoundEvents.ARMOR_EQUIP_LEATHER, 0.0F,
			() -> Ingredient.of( BetterStorageItems.CARDBOARD_SHEET.get() ) ),;

	private static final int[]	MAX_DAMAGE_ARRAY	= new int[] { 13, 15, 16, 11 };
	private String				name;
	private int					maxDamageFactor;
	private int[]				damageReductionAmountArray;
	private int					enchantability;
	private SoundEvent			soundEvent;
	private float				toughness;
	// private LazyLoadBase< Ingredient > repairMaterial;

	BetterStorageArmorMaterial( final String name, final int maxDamageFactor, final int[] damageReductionAmountArray, final int enchantability,
			final SoundEvent soundEvent, final float toughness, final Supplier< Ingredient > repairMaterial )
	{
		this.name = new ResourceLocation( ModInfo.MOD_ID, name ).toString();
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		// this.repairMaterial = new LazyLoadBase<>( repairMaterial );
	}

	@Override
	public int getDurabilityForSlot( EquipmentSlotType slotIn )
	{
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * maxDamageFactor;
	}

	@Override
	public int getDefenseForSlot( EquipmentSlotType slotIn )
	{
		return damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue()
	{
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound()
	{
		return soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient()
	{
		// return repairMaterial.getValue();
		return null;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public float getToughness()
	{
		return toughness;
	}

	// Knockbask resistance
	@Override
	public float getKnockbackResistance()
	{
		return 0;
	}

}
