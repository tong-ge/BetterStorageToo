package io.github.tehstoneman.betterstorage.item;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.common.item.BetterStorageItems;
import io.github.tehstoneman.betterstorage.utils.LanguageUtils;
import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBucketSlime extends Item
{
	private static Map< String, Handler > handlers = new HashMap<>();

	public ItemBucketSlime()
	{
		setContainerItem( Items.BUCKET );
		setMaxStackSize( 0 );
		setMaxDamage( 0 );
		setHasSubtypes( true );
	}

	@Override
	public String getUnlocalizedName( ItemStack stack )
	{
		final String id = getSlimeId( stack );
		final Handler handler = getHandler( id );
		final String name = StackUtils.get( stack, (String)null, "Slime", "name" );
		if( name != null )
			return super.getUnlocalizedName() + "." + name;
		return super.getUnlocalizedName();
	}

	@Override
	@SideOnly( Side.CLIENT )
	public boolean hasEffect( ItemStack stack )
	{
		return stack.isItemEnchanted() || StackUtils.has( stack, "Effects" );
	}

	@Override
	@SideOnly( Side.CLIENT )
	public void addInformation( ItemStack stack, EntityPlayer player, List list, boolean advancedTooltips )
	{

		final String id = getSlimeId( stack );
		final Handler handler = getHandler( id );
		final String name = StackUtils.get( stack, (String)null, "Slime", "name" );
		if( name != null || advancedTooltips )
			list.add( "Contains: " + ( name != null ? "\"" + name + "\"" + ( advancedTooltips ? " (" + id + ")" : "" ) : id ) );

		final NBTTagList effectList = (NBTTagList)StackUtils.getTag( stack, "Effects" );
		if( effectList != null && handler != null )
		{
			final int max = advancedTooltips || GuiScreen.isShiftKeyDown() ? 6 : 3;

			for( int i = 0; i < Math.min( effectList.tagCount(), max ); i++ )
			{
				final PotionEffect effect = PotionEffect.readCustomPotionEffectFromNBT( effectList.getCompoundTagAt( i ) );
				final Potion potion = effect.getPotion();
				final int duration = (int)( effect.getDuration() * handler.durationMultiplier() );

				final StringBuilder str = new StringBuilder().append( potion.isBadEffect() ? TextFormatting.RED : TextFormatting.GRAY )
						.append( BetterStorage.proxy.localize( effect.getEffectName() ) );

				if( effect.getAmplifier() > 0 )
					str.append( " " ).append( BetterStorage.proxy.localize( "potion.potency." + effect.getAmplifier() ) );

				str.append( " (" ).append( StringUtils.ticksToElapsedTime( duration ) ).append( ")" );

				list.add( str.toString() );
			}

			final int more = effectList.tagCount() - max;

			if( more > 0 )
				list.add( TextFormatting.DARK_GRAY.toString() + TextFormatting.ITALIC
						+ LanguageUtils.translateTooltip( "bucketSlime.more." + ( more == 1 ? "1" : "x" ), "%X%", Integer.toString( more ) ) );

		}

	}

	@Override
	public EnumActionResult onItemUse( ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing side, float hitX,
			float hitY, float hitZ )
	{
		// If player is sneaking, eat slime
		// instead of placing it in the world.
		if( player.isSneaking() )
			return EnumActionResult.FAIL;

		if( world.isRemote )
			return EnumActionResult.PASS;

		pos.add( side.getFrontOffsetX(), side.getFrontOffsetY(), side.getFrontOffsetZ() );

		final String id = getSlimeId( stack );
		final String name = StackUtils.get( stack, (String)null, "Slime", "name" );
		final Entity entity = EntityList.createEntityByName( id, world );
		final Handler handler = getHandler( id );

		if( entity != null && handler != null && entity instanceof EntityLiving )
		{
			final EntityLiving slime = (EntityLiving)entity;

			final float rotation = MathHelper.wrapDegrees( world.rand.nextFloat() * 360.0F );
			slime.setLocationAndAngles( pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, rotation, 0.0F );
			slime.rotationYawHead = slime.renderYawOffset = rotation;

			if( name != null )
				slime.setCustomNameTag( name );
			handler.setSize( slime, 1 );

			final NBTTagList effectList = (NBTTagList)StackUtils.getTag( stack, "Effects" );
			if( effectList != null )
				for( int i = 0; i < effectList.tagCount(); i++ )
					slime.addPotionEffect( PotionEffect.readCustomPotionEffectFromNBT( effectList.getCompoundTagAt( i ) ) );

			world.spawnEntityInWorld( slime );
			slime.playSound( SoundEvents.ENTITY_SLIME_JUMP, 1.2F, 0.6F );

			player.setHeldItem( EnumHand.MAIN_HAND, new ItemStack( Items.BUCKET ) );
		}

		return EnumActionResult.SUCCESS;
	}

	// Eating slime

	@Override
	public EnumAction getItemUseAction( ItemStack stack )
	{
		return EnumAction.DRINK;
	}

	@Override
	public int getMaxItemUseDuration( ItemStack stack )
	{
		return 48;
	}

	@Override
	public ActionResult< ItemStack > onItemRightClick( ItemStack stack, World world, EntityPlayer player, EnumHand hand )
	{
		/*
		 * if( hand == EnumHand.MAIN_HAND)
		 * {
		 * player.setItemInUse(stack, getMaxItemUseDuration(stack));
		 * return stack;
		 * }
		 */
		return super.onItemRightClick( stack, world, player, hand );
	}

	/*
	 * @Override
	 * public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player) {
	 * Handler handler = getHandler(stack);
	 * if (handler != null) {
	 * player.getFoodStats().addStats(handler.foodAmount(), handler.saturationAmount());
	 * NBTTagList effectList = (NBTTagList)StackUtils.getTag(stack, "Effects");
	 * if (effectList != null) {
	 * for (int i = 0; i < effectList.tagCount(); i++) {
	 * PotionEffect effect = PotionEffect.readCustomPotionEffectFromNBT(effectList.getCompoundTagAt(i));
	 * int duration = (int)(effect.getDuration() * handler.durationMultiplier());
	 * effect = new PotionEffect(effect.getPotionID(), duration, effect.getAmplifier());
	 * player.addPotionEffect(effect);
	 * }
	 * }
	 * handler.onEaten(player, false);
	 * }
	 * return new ItemStack(Items.BUCKET);
	 * }
	 */

	// Helper functions

	/**
	 * Called when a player right clicks an entity with an empty bucket.
	 * If the entity clicked is a small slime, attempts to pick it up and
	 * sets the held item to a Slime in a Bucket containing that slime.
	 */
	public static void pickUpSlime( EntityPlayer player, EntityLiving slime )
	{
		final Handler handler = getHandler( slime );

		if( slime.isDead || handler == null || handler.getSize( slime ) != 1 )
			return;

		final ItemStack stack = new ItemStack( BetterStorageItems.SLIME_BUCKET );

		final String entityId = EntityList.getEntityString( slime );
		if( !entityId.equals( "Slime" ) )
			StackUtils.set( stack, entityId, "Slime", "id" );
		if( slime.hasCustomName() )
			StackUtils.set( stack, slime.getCustomNameTag(), "Slime", "name" );

		final Collection< PotionEffect > effects = slime.getActivePotionEffects();
		if( !effects.isEmpty() )
		{
			final NBTTagList effectList = new NBTTagList();
			for( final PotionEffect effect : effects )
				effectList.appendTag( effect.writeCustomPotionEffectToNBT( new NBTTagCompound() ) );
			StackUtils.set( stack, effectList, "Effects" );
		}

		if( --player.getHeldItemMainhand().stackSize <= 0 )
			player.setHeldItem( EnumHand.MAIN_HAND, stack );
		/*
		 * else if (!player.inventory.addItemStackToInventory(stack))
		 * player.dropPlayerItemWithRandomChoice(stack, true);
		 */
		else
			( (EntityPlayerMP)player ).inventoryContainer.detectAndSendChanges();

		slime.playSound( SoundEvents.ENTITY_SLIME_JUMP, 1.2F, 0.8F );
		slime.isDead = true;
	}

	/** Returns the slime ID from a slime bucket. */
	public static String getSlimeId( ItemStack stack )
	{
		return StackUtils.get( stack, "Slime", "Slime", "id" );
	}

	/** Registers a slime handler. */
	public static void registerHandler( Handler handler )
	{
		handlers.put( handler.entityName, handler );
	}

	/** Returns the handler for an entity id, null if none. */
	public static Handler getHandler( String id )
	{
		return handlers.get( id );
	}

	/** Returns the handler for an entity, null if none. */
	public static Handler getHandler( EntityLiving slime )
	{
		return getHandler( EntityList.getEntityString( slime ) );
	}

	/** Returns the handler for slime bucket, null if none. */
	public static Handler getHandler( ItemStack stack )
	{
		return getHandler( getSlimeId( stack ) );
	}

	// Slime handlers

	static
	{
		registerHandler( new Handler( "slime", "Slime" )
		{
			@Override
			public int foodAmount()
			{
				return 3;
			}

			@Override
			public float saturationAmount()
			{
				return 0.2F;
			}
		} );
		registerHandler( new Handler( "magmaCube", "LavaSlime" )
		{
			@Override
			public float durationMultiplier()
			{
				return 0.4F;
			}
			/*
			 * @Override public void onEaten(EntityPlayer player, boolean potionEffects) {
			 * player.setFire(2);
			 * player.addPotionEffect(new PotionEffect(Potion.jump.id, (potionEffects ? 10 : 20) * 20,
			 * (potionEffects ? 2 : 3)));
			 * player.addPotionEffect(new PotionEffect(Potion.damageBoost.id, (potionEffects ? 24 : 32) * 20, 0));
			 * }
			 */} );
		registerHandler( new Handler( "mazeSlime", "TwilightForest.Maze Slime" )
		{
			/*
			 * @Override public void onEaten(EntityPlayer player, boolean potionEffects) {
			 * player.attackEntityFrom(DamageSource.magic, 3.0F);
			 * player.addPotionEffect(new PotionEffect(Potion.jump.id, (potionEffects ? 4 : 8) * 20, 0));
			 * player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id, (potionEffects ? 4 : 8) * 20, 1));
			 * player.addPotionEffect(new PotionEffect(Potion.resistance.id, 30 * 20, 1));
			 * }
			 */} );
		registerHandler( new Handler( "pinkSlime", "MineFactoryReloaded.mfrEntityPinkSlime" )
		{
			@Override
			public int foodAmount()
			{
				return 6;
			}

			@Override
			public float saturationAmount()
			{
				return 0.75F;
			}
			/*
			 * @Override public void onEaten(EntityPlayer player, boolean potionEffects) {
			 * super.onEaten(player, potionEffects);
			 * player.addPotionEffect(new PotionEffect(Potion.field_76434_w.id, (potionEffects ? 40 : 60) * 20, 4));
			 * }
			 */
		} );
		registerHandler( new Handler( "thaumicSlime", "Thaumcraft.ThaumSlime" )
		{
			private final int potionFluxId = -1;

			@Override
			public float durationMultiplier()
			{
				return 1.0F;
			}
			/*
			 * @Override public void onEaten(EntityPlayer player, boolean potionEffects) {
			 * if (potionFluxId == -1) {
			 * // Look for flux potion effect.
			 * for (Potion potion : Potion.potionTypes)
			 * if (potion != null)
			 * if (potion.getName() == "potion.fluxtaint") {
			 * potionFluxId = potion.id; break; }
			 * // If not found, just use wither.
			 * if (potionFluxId == -1)
			 * potionFluxId = Potion.wither.id;
			 * }
			 * super.onEaten(player, potionEffects);
			 * player.addPotionEffect(new PotionEffect(potionFluxId, 8 * 20, 0));
			 * }
			 */
		} );
		registerHandler( new Handler( "blueSlime", "TConstruct.EdibleSlime" )
		{
			@Override
			public float durationMultiplier()
			{
				return 0.2F;
			}
		} );
	}

	public static class Handler
	{

		public final String	name;
		public final String	entityName;

		// public IIcon icon;

		public Handler( String name, String entityName )
		{
			this.name = name;
			this.entityName = entityName;
		}

		/** Returns the icon location to be used in registerIcons. */
		/*
		 * public void registerIcon(IIconRegister iconRegister) {
		 * icon = iconRegister.registerIcon(Constants.modId + ":bucketSlime_" + name);
		 * }
		 */

		/** Returns the size of the slime. */
		public int getSize( EntityLiving slime )
		{
			if( slime instanceof EntitySlime )
				return ( (EntitySlime)slime ).getSlimeSize();
			else
				return 0;
		}

		/** Sets the size of the slime. */
		public void setSize( EntityLiving slime, int size )
		{
			final NBTTagCompound compound = new NBTTagCompound();
			slime.writeToNBT( compound );
			compound.setInteger( "Size", size - 1 );
			slime.readFromNBT( compound );
		}

		/** How much food is restored when eating this slime. */
		public int foodAmount()
		{
			return 4;
		}

		/** The satuation amount added when eating this slime. */
		public float saturationAmount()
		{
			return 0.3F;
		}

		/** Duration will get multiplied by this value. */
		public float durationMultiplier()
		{
			return 0.25F;
		}

		/**
		 * Called when this slime is eaten, allows adding effects to the player
		 * in addition to the potion effects of the slime itself.
		 */
		/*
		 * public void onEaten(EntityPlayer player, boolean potionEffects) {
		 * player.addPotionEffect(new PotionEffect(Potion.jump.id, (potionEffects ? 6 : 16) * 20, 1));
		 * }
		 */

	}
}
