package io.github.tehstoneman.betterstorage.item.locking;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.api.IContainerItem;
import io.github.tehstoneman.betterstorage.api.lock.IKey;
import io.github.tehstoneman.betterstorage.client.gui.BetterStorageGUIHandler.EnumGui;
import io.github.tehstoneman.betterstorage.container.ContainerKeyring;
import io.github.tehstoneman.betterstorage.item.ItemBetterStorage;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemKeyring extends ItemBetterStorage implements IKey, IContainerItem
{
	@Override
	public ActionResult< ItemStack > onItemRightClick( ItemStack stack, World world, EntityPlayer player, EnumHand hand )
	{
		if( !player.isSneaking() )
			return new ActionResult( EnumActionResult.PASS, stack );

		final String title = StackUtils.get( stack, "", "display", "Name" );
		final int protectedSlot = player.inventory.currentItem;
		final Container container = new ContainerKeyring( player, title, protectedSlot );
		player.openGui( BetterStorage.instance, EnumGui.KEYRING.getGuiID(), world, 0, 0, 0 );
		return new ActionResult( EnumActionResult.SUCCESS, stack );
	}

	// IKey implementation

	@Override
	public boolean isNormalKey()
	{
		return false;
	}

	@Override
	public boolean unlock( ItemStack keyring, ItemStack lock, boolean useAbility )
	{

		// Goes through all the keys in the keyring,
		// returns if any of the keys fit in the lock.

		final ItemStack[] items = StackUtils.getStackContents( keyring, 9 );
		for( final ItemStack key : items )
		{
			if( key == null )
				continue;
			final IKey keyType = (IKey)key.getItem();
			if( keyType.unlock( key, lock, false ) )
				return true;
		}

		return false;

	}

	@Override
	public boolean canApplyEnchantment( ItemStack key, Enchantment enchantment )
	{
		return false;
	}

	// IContainerItem implementation

	@Override
	public ItemStack[] getContainerItemContents( ItemStack container )
	{
		return StackUtils.getStackContents( container, 9 );
	}

	@Override
	public boolean canBeStoredInContainerItem( ItemStack item )
	{
		return true;
	}

	@SideOnly( Side.CLIENT )
	public void registerItemModels()
	{
		for (int i = 0; i < 4; i++)
			ModelLoader.setCustomModelResourceLocation( this, i, new ModelResourceLocation( getRegistryName() + "_" + i, "inventory" ) );
	}
}
