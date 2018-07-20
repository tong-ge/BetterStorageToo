package io.github.tehstoneman.betterstorage.event;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import io.github.tehstoneman.betterstorage.api.ICardboardItem;
import io.github.tehstoneman.betterstorage.api.IDyeableItem;
import io.github.tehstoneman.betterstorage.client.renderer.block.statemap.SizeStateMap;
import io.github.tehstoneman.betterstorage.common.block.BetterStorageBlocks;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockCrate;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockLocker;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedChest;
import io.github.tehstoneman.betterstorage.common.item.ItemBlockReinforcedLocker;
import io.github.tehstoneman.betterstorage.common.item.ItemBucketSlime;
import io.github.tehstoneman.betterstorage.common.item.cardboard.ItemCardboardSheet;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityCrate;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityLocker;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedChest;
import io.github.tehstoneman.betterstorage.common.tileentity.TileEntityReinforcedLocker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
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
		if( BetterStorage.config.lockerEnabled )
		{
			BetterStorageBlocks.LOCKER.setUnlocalizedName( ModInfo.modId + "." + BetterStorageBlocks.LOCKER.getBlockName() );
			BetterStorageBlocks.LOCKER.setRegistryName( BetterStorageBlocks.LOCKER.getBlockName() );
			registry.register( BetterStorageBlocks.LOCKER );
			GameRegistry.registerTileEntity( TileEntityLocker.class, ModInfo.containerLocker );
			if( BetterStorage.config.reinforcedLockerEnabled )
			{
				BetterStorageBlocks.REINFORCED_LOCKER
						.setUnlocalizedName( ModInfo.modId + "." + BetterStorageBlocks.REINFORCED_LOCKER.getBlockName() );
				BetterStorageBlocks.REINFORCED_LOCKER.setRegistryName( BetterStorageBlocks.REINFORCED_LOCKER.getBlockName() );
				registry.register( BetterStorageBlocks.REINFORCED_LOCKER );
				GameRegistry.registerTileEntity( TileEntityReinforcedLocker.class, ModInfo.containerReinforcedLocker );
			}
		}
		if( BetterStorage.config.flintBlockEnabled )
		{
			BetterStorageBlocks.BLOCK_FLINT.setUnlocalizedName( ModInfo.modId + "." + BetterStorageBlocks.BLOCK_FLINT.getBlockName() );
			BetterStorageBlocks.BLOCK_FLINT.setRegistryName( BetterStorageBlocks.BLOCK_FLINT.getBlockName() );
			registry.register( BetterStorageBlocks.BLOCK_FLINT );
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

		if( BetterStorage.config.lockerEnabled )
		{
			registry.register( new ItemBlockLocker( BetterStorageBlocks.LOCKER ).setRegistryName( BetterStorageBlocks.LOCKER.getRegistryName() ) );
			if( BetterStorage.config.reinforcedLockerEnabled )
				registry.register( new ItemBlockReinforcedLocker( BetterStorageBlocks.REINFORCED_LOCKER )
						.setRegistryName( BetterStorageBlocks.REINFORCED_LOCKER.getRegistryName() ) );
		}
	}

	@SubscribeEvent
	public void onRegisterModels( ModelRegistryEvent event )
	{
		// ModelLoader.setCustomStateMapper( block, mapper );

		final SizeStateMap sizeStateMap = new SizeStateMap();

		if( BetterStorage.config.crateEnabled )
			registerItemModel( BetterStorageBlocks.CRATE );

		if( BetterStorage.config.reinforcedChestEnabled )
		{
			ModelLoader.setCustomStateMapper( BetterStorageBlocks.REINFORCED_CHEST, sizeStateMap );
			for( final EnumReinforced material : EnumReinforced.values() )
				registerItemModel( BetterStorageBlocks.REINFORCED_CHEST, material.getMetadata(),
						BetterStorageBlocks.REINFORCED_CHEST.getRegistryName() + "_" + material.getName() );
		}

		if( BetterStorage.config.lockerEnabled )
		{
			ModelLoader.setCustomStateMapper( BetterStorageBlocks.LOCKER, sizeStateMap );
			registerItemModel( BetterStorageBlocks.LOCKER );
			if( BetterStorage.config.reinforcedLockerEnabled )
			{
				ModelLoader.setCustomStateMapper( BetterStorageBlocks.REINFORCED_LOCKER, sizeStateMap );
				for( final EnumReinforced material : EnumReinforced.values() )
					registerItemModel( BetterStorageBlocks.REINFORCED_LOCKER, material.getMetadata(),
							BetterStorageBlocks.REINFORCED_LOCKER.getRegistryName() + "_" + material.getName() );
			}
		}

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

	/*
	 * =================
	 * Support functions
	 *
	 * Adapted from Choonster's TestMod3
	 * =================
	 */

	/**
	 * A {@link StateMapperBase} used to create property strings.
	 */
	private final StateMapperBase propertyStringMapper = new StateMapperBase()
	{
		@Override
		protected ModelResourceLocation getModelResourceLocation( IBlockState state )
		{
			return new ModelResourceLocation( "minecraft:air" );
		}
	};

	/**
	 * Register a model for a metadata value of the {@link Block}'s {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and the {@link IBlockState} as the variant.
	 *
	 * @param state
	 *            The state to use as the variant
	 * @param metadata
	 *            The item metadata to register the model for
	 */
	private void registerBlockItemModelForMeta( IBlockState state, int metadata )
	{
		final Item item = Item.getItemFromBlock( state.getBlock() );

		if( item != Items.AIR )
			registerItemModel( item, metadata, propertyStringMapper.getPropertyString( state.getProperties() ) );
	}

	private void registerItemModel( Block block )
	{
		final Item item = Item.getItemFromBlock( block );

		if( item != Items.AIR )
			registerItemModel( item );
	}

	private void registerItemModel( Block block, int metadata, String modelLocation )
	{
		final Item item = Item.getItemFromBlock( block );

		if( item != Items.AIR )
			registerItemModel( item, metadata, modelLocation );
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and {@code "inventory"} as the variant.
	 *
	 * @param item
	 *            The Item
	 */
	private void registerItemModel( Item item )
	{
		final ResourceLocation registryName = item.getRegistryName();
		registerItemModel( item, registryName.toString() );
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code modelLocation} as the domain/path and {@link "inventory"} as the variant.
	 *
	 * @param item
	 *            The Item
	 * @param modelLocation
	 *            The model location
	 */
	private void registerItemModel( Item item, String modelLocation )
	{
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation( modelLocation, "inventory" );
		registerItemModel( item, fullModelLocation );
	}

	private void registerItemModel( Item item, int meta, String modelLocation )
	{
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation( modelLocation, "inventory" );
		registerItemModel( item, meta, fullModelLocation );
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code fullModelLocation} as the domain, path and variant.
	 *
	 * @param item
	 *            The Item
	 * @param fullModelLocation
	 *            The full model location
	 */
	private void registerItemModel( Item item, ModelResourceLocation fullModelLocation )
	{
		ModelBakery.registerItemVariants( item, fullModelLocation );
		registerItemModel( item, stack -> fullModelLocation );
	}

	/**
	 * Register an {@link ItemMeshDifinition} for an {@link Item}.
	 *
	 * @param item
	 *            The Item
	 * @param meshDefinition
	 *            The ItemModelDefinition
	 */
	private void registerItemModel( Item item, ItemMeshDefinition meshDefinition )
	{
		ModelLoader.setCustomMeshDefinition( item, meshDefinition );
	}

	/**
	 * Register a model for a metadata value of an {@link Item}.
	 * <p>
	 * Uses {@code modelResourceLocation} as the domain, path and variant.
	 *
	 * @param item
	 *            The Item
	 * @param metadata
	 *            The metadata
	 * @param modelResourceLocation
	 *            The full model location
	 */
	private void registerItemModel( Item item, int metadata, ModelResourceLocation modelResourceLocation )
	{
		ModelLoader.setCustomModelResourceLocation( item, metadata, modelResourceLocation );
	}
}
