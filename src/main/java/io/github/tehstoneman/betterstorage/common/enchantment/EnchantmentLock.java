package io.github.tehstoneman.betterstorage.common.enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.lock.ILock;
import io.github.tehstoneman.betterstorage.common.item.locking.ItemLock;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;

public class EnchantmentLock extends Enchantment
{
	public final static EnchantmentType	LOCK			= EnchantmentType.create( "lock", ( item ) -> ( item instanceof ItemLock ) );

	private final int					maxLevel;
	private final int					minBase, minScaling;
	private final int					maxBase, maxScaling;

	private List< Enchantment >			incompatible	= new ArrayList<>( 0 );

	public EnchantmentLock( Rarity rarityIn, int maxLevel, int minBase, int minScaling, int maxBase, int maxScaling )
	{
		super( rarityIn, LOCK, new EquipmentSlotType[] { EquipmentSlotType.MAINHAND } );

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
	public boolean canApplyTogether( Enchantment other )
	{
		return super.canApplyTogether( other ) && !incompatible.contains( other );
	}

	@Override
	public boolean canApply( ItemStack stack )
	{
		return stack.getItem() instanceof ILock ? true : super.canApply( stack );
	}

	/*@Override
	public boolean canApplyAtEnchantingTable( ItemStack stack )
	{
		if( stack.getItem() instanceof ILock )
		{
			final ILock lock = (ILock)stack.getItem();
			return lock.canApplyEnchantment( stack, this );
		}
		return stack.getItem() instanceof ILock;
	}*/
}
