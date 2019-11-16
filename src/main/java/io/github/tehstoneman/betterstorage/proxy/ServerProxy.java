package io.github.tehstoneman.betterstorage.proxy;

import io.github.tehstoneman.betterstorage.api.IProxy;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import net.minecraft.block.DispenserBlock;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.OptionalDispenseBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DirectionalPlaceContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.DeferredWorkQueue;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class ServerProxy implements IProxy
{
	@Override
	public void setup( FMLCommonSetupEvent event )
	{
		DeferredWorkQueue.runLater( () ->
		{
			DispenserBlock.registerDispenseBehavior( BetterStorageBlocks.CARDBOARD_BOX, new OptionalDispenseBehavior()
			{
				/**
				 * Dispense the specified stack, play the dispense sound and spawn particles.
				 */
				@Override
				protected ItemStack dispenseStack( IBlockSource source, ItemStack stack )
				{
					successful = false;
					final Item item = stack.getItem();
					if( item instanceof BlockItem )
					{
						final Direction direction = source.getBlockState().get( DispenserBlock.FACING );
						final BlockPos blockpos = source.getBlockPos().offset( direction );
						final Direction direction1 = source.getWorld().isAirBlock( blockpos.down() ) ? direction : Direction.UP;
						successful = ( (BlockItem)item ).tryPlace( new DirectionalPlaceContext( source.getWorld(), blockpos, direction, stack,
								direction1 ) ) == ActionResultType.SUCCESS;
					}

					return stack;
				}
			} );
		} );
	}

	@Override
	public World getClientWorld()
	{
		throw new IllegalStateException( "Only run this on the client!" );
	}
	// private final boolean preventSlimeBucketUse = false;

	/*
	 * public void preInit()
	 * {
	 * // NetworkRegistry.INSTANCE.registerGuiHandler( BetterStorage.instance, new BetterStorageGUIHandler() );
	 *
	 *
	 * if( BetterStorage.config.cardboardSheetEnabled )
	 * {
	 * BetterStorageItems.CARDBOARD_SHEET.register();
	 * OreDictionary.registerOre( "sheetCardboard", BetterStorageItems.CARDBOARD_SHEET );
	 * }
	 *
	 * if( BetterStorage.config.cardboardBoxEnabled )
	 * {
	 * BetterStorageBlocks.CARDBOARD_BOX.registerBlock();
	 * GameRegistry.registerTileEntity( TileEntityCardboardBox.class, ModInfo.containerCardboardBox );
	 * }
	 *
	 * if( BetterStorage.config.cardboardSwordEnabled )
	 * BetterStorageItems.CARDBOARD_SWORD.register();
	 * if( BetterStorage.config.cardboardShovelEnabled )
	 * BetterStorageItems.CARDBOARD_SHOVEL.register();
	 * if( BetterStorage.config.cardboardPickaxeEnabled )
	 * BetterStorageItems.CARDBOARD_PICKAXE.register();
	 * if( BetterStorage.config.cardboardAxeEnabled )
	 * BetterStorageItems.CARDBOARD_AXE.register();
	 * if( BetterStorage.config.cardboardHoeEnabled )
	 * BetterStorageItems.CARDBOARD_HOE.register();
	 *
	 * if( BetterStorage.config.cardboardHelmetEnabled )
	 * BetterStorageItems.CARDBOARD_HELMET.register( "cardboard_helmet" );
	 * if( BetterStorage.config.cardboardChestplateEnabled )
	 * BetterStorageItems.CARDBOARD_CHESTPLATE.register( "cardboard_chestplate" );
	 * if( BetterStorage.config.cardboardLeggingsEnabled )
	 * BetterStorageItems.CARDBOARD_LEGGINGS.register( "cardboard_leggings" );
	 * if( BetterStorage.config.cardboardBootsEnabled )
	 * BetterStorageItems.CARDBOARD_BOOTS.register( "cardboard_boots" );
	 *
	 * if( BetterStorage.config.slimeBucketEnabled )
	 * BetterStorageItems.SLIME_BUCKET.register();
	 *
	 * }
	 */

	/*
	 * public void initialize()
	 * {
	 * Recipes.add();
	 *
	 * // FMLCommonHandler.instance().bus().register(this);
	 *
	 * // if (BetterStorage.globalConfig.getBoolean(GlobalConfig.enableChristmasEvent)) new ChristmasEventHandler();
	 *
	 * // registerArmorStandHandlers();
	 * }
	 */

	/*
	 * public void postInit()
	 * {}
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
	 * public String localize( String unlocalized, Object... args )
	 * {
	 * // return I18n.translateToLocalFormatted( unlocalized, args );
	 * return unlocalized;
	 * }
	 */
}
