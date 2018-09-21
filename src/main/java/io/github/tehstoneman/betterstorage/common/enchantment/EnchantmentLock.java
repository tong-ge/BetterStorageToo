package io.github.tehstoneman.betterstorage.common.enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemLock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.EnumHelper;

public class EnchantmentLock extends Enchantment
{
	final static EnumEnchantmentType	lockType		= EnumHelper.addEnchantmentType( "lock", ( item ) -> ( item instanceof ItemLock ) );

	private final int					maxLevel;
	private final int					minBase, minScaling;
	private final int					maxBase, maxScaling;

	private List< Enchantment >			incompatible	= new ArrayList<>( 0 );

	protected EnchantmentLock( String name, Rarity rarityIn, int maxLevel, int minBase, int minScaling, int maxBase, int maxScaling )
	{
		super( rarityIn, lockType, new EntityEquipmentSlot[] { EntityEquipmentSlot.MAINHAND } );
		setName( ModInfo.modId + ".lock." + name );

		this.maxLevel = maxLevel;
		this.minBase = minBase;
		this.minScaling = minScaling;
		this.maxBase = maxBase;
		this.maxScaling = maxScaling;
	}

	public void setIncompatible( Enchantment... incompatible )
	{
		this.incompatible = Arrays.asList( incompatible );
	}

	@Override
	public int getMaxLevel()
	{
		return maxLevel;
	}

	@Override
	public int getMinEnchantability( int level )
	{
		return minBase + ( level - 1 ) * minScaling;
	}

	@Override
	public int getMaxEnchantability( int level )
	{
		return getMinEnchantability( level ) + maxBase + ( level - 1 ) * maxScaling;
	}

	@Override
	public boolean canApplyAtEnchantingTable( ItemStack stack )
	{
		if( type == lockType && stack.getItem() instanceof ILock )
		{
			final ILock lock = (ILock)stack.getItem();
			return lock.canApplyEnchantment( stack, this );
		}
		return false;
	}

	@Override
	public boolean canApply( ItemStack stack )
	{
		return canApplyAtEnchantingTable( stack );
	}

	@Override
	public boolean canApplyTogether( Enchantment other )
	{
		return super.canApplyTogether( other ) && !incompatible.contains( other );
	}

	@Override
	public boolean isAllowedOnBooks()
	{
		return false;
	}
}
