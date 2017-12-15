package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.api.IDyeableItem;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.item.ItemBucketSlime;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardSheet;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.EntityInteract;
import net.minecraftforge.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class BetterStorageEventHandler
{
	private boolean preventSlimeBucketUse;

	@SubscribeEvent
	public void registerBlocks( Register< Block > event )
	{
		final IForgeRegistry< Block > registry = event.getRegistry();

		if( BetterStorage.config.crateEnabled )
		{
			BetterStorageBlocks.CRATE.setUnlocalizedName( ModInfo.modId + "." + BetterStorageBlocks.CRATE.getBlockName() );
			BetterStorageBlocks.CRATE.setRegistryName( BetterStorageBlocks.CRATE.getBlockName() );
			registry.register( BetterStorageBlocks.CRATE );
			GameRegistry.registerTileEntity( TileEntityCrate.class, ModInfo.containerCrate );
		}
		if( BetterStorage.config.reinforcedChestEnabled )
		{
			BetterStorageBlocks.REINFORCED_CHEST.setUnlocalizedName( ModInfo.modId + "." + BetterStorageBlocks.REINFORCED_CHEST.getBlockName() );
			BetterStorageBlocks.REINFORCED_CHEST.setRegistryName( BetterStorageBlocks.REINFORCED_CHEST.getBlockName() );
			registry.register( BetterStorageBlocks.REINFORCED_CHEST );
			GameRegistry.registerTileEntity( TileEntityReinforcedChest.class, ModInfo.containerReinforcedChest );
		}
	}

	@SubscribeEvent
	public void registerItems( Register< Item > event )
	{
		final IForgeRegistry< Item > registry = event.getRegistry();

		if( BetterStorage.config.crateEnabled )
			registry.register( new ItemBlockCrate( BetterStorageBlocks.CRATE ).setRegistryName( BetterStorageBlocks.CRATE.getRegistryName() ) );

		if( BetterStorage.config.reinforcedChestEnabled )
			registry.register( new ItemBlockReinforcedChest( BetterStorageBlocks.REINFORCED_CHEST )
					.setRegistryName( BetterStorageBlocks.REINFORCED_CHEST.getRegistryName() ) );
	}

	@SubscribeEvent
	public void onConfigChangeEvent( OnConfigChangedEvent event )
	{
		if( event.getModID().equals( ModInfo.modId ) && !event.isWorldRunning() )
			BetterStorage.config.syncFromGUI();
	}

	@SubscribeEvent
	public void onPlayerInteract( PlayerInteractEvent event )
	{
		final World world = event.getEntity().world;
		final BlockPos pos = event.getPos();
		final EntityPlayer player = event.getEntityPlayer();
		final ItemStack holding = player.getHeldItemMainhand();
		final IBlockState state = world.getBlockState( pos );
		final Block block = state.getBlock();
		// final boolean leftClick = event.action == Action.LEFT_CLICK_BLOCK;
		// final boolean rightClick = event.action == Action.RIGHT_CLICK_BLOCK;
		final EnumHand hand = event.getHand();

		// Interact with attachments.
		/*
		 * if( leftClick || rightClick )
		 * {
		 * final IHasAttachments hasAttachments = WorldUtils.get( world, x, y, z, IHasAttachments.class );
		 * if( hasAttachments != null )
		 * {
		 * final EnumAttachmentInteraction interactionType = event.action == Action.LEFT_CLICK_BLOCK ? EnumAttachmentInteraction.attack
		 * : EnumAttachmentInteraction.use;
		 * if( hasAttachments.getAttachments().interact( WorldUtils.rayTrace( player, 1.0F ), player, interactionType ) )
		 * {
		 * event.useBlock = Result.DENY;
		 * event.useItem = Result.DENY;
		 * }
		 * }
		 * }
		 */

		// Use cauldron to remove color from dyable items
		if( hand == EnumHand.MAIN_HAND && block == Blocks.CAULDRON && holding.getItem() instanceof IDyeableItem )
		{
			final int level = state.getValue( BlockCauldron.LEVEL );
			if( level > 0 )
			{
				final IDyeableItem dyeable = (IDyeableItem)holding.getItem();
				if( dyeable.canDye( holding ) && holding.hasTagCompound() )
				{
					final NBTTagCompound compound = holding.getTagCompound();
					if( compound.hasKey( "color" ) )
					{
						compound.removeTag( "color" );
						holding.setTagCompound( compound );
						Blocks.CAULDRON.setWaterLevel( world, pos, state, level - 1 );

						event.setResult( Result.DENY );
					}
				}
			}
		}

		// Prevent players from breaking blocks with broken cardboard items.
		if( hand == EnumHand.MAIN_HAND && holding.getItem() instanceof ICardboardItem && !ItemCardboardSheet.isEffective( holding ) )
			event.setResult( Result.DENY );

		// Attach locks to iron doors.
		if( !world.isRemote && BetterStorage.config.lockableDoorEnabled && hand == EnumHand.MAIN_HAND && block == Blocks.IRON_DOOR
				&& holding.getItem() == BetterStorageItems.LOCK )
			player.inventory.setInventorySlotContents( player.inventory.currentItem, ItemStack.EMPTY );

		// Prevent eating of slime buckets after capturing them.
		if( preventSlimeBucketUse )
		{
			event.setCanceled( true );
			preventSlimeBucketUse = false;
		}
	}

	@SubscribeEvent
	public void onEntityInteract( EntityInteract event )
	{

		if( event.getEntity().world.isRemote || event.isCanceled() )
			return;

		final EntityPlayer player = event.getEntityPlayer();
		final Entity target = event.getTarget();
		final ItemStack holding = player.getHeldItemMainhand();

		/*
		 * if( target.getClass() == EntityChicken.class && holding != null && holding.getItem() == Items.NAME_TAG )
		 * {
		 *
		 * final EntityChicken chicken = (EntityChicken)target;
		 * if( !chicken.isDead && !chicken.isChild() && "Cluckington".equals( holding.getDisplayName() ) )
		 * EntityCluckington.spawn( chicken );
		 *
		 * }
		 */

		if( BetterStorage.config.slimeBucketEnabled && target instanceof EntityLiving && holding != null && holding.getItem() == Items.BUCKET )
		{
			ItemBucketSlime.pickUpSlime( player, (EntityLiving)target );
			if( player.getHeldItemMainhand().getItem() instanceof ItemBucketSlime )
				preventSlimeBucketUse = true;
		}
	}
}
