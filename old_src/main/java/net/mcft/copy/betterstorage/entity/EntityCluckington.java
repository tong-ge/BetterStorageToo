package io.github.tehstoneman.betterstorage.entity;

import io.github.tehstoneman.betterstorage.utils.StackUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.world.Level;

public class EntityCluckington extends EntityChicken
{
	private int healTimer = 0;

	public EntityCluckington( Level world )
	{
		super( world );
		setSize( 0.6F, 0.8F );
		// EntityAITaskEntry panic = (EntityAITaskEntry)tasks.taskEntries.get(1);
		// EntityAITaskEntry mate = (EntityAITaskEntry)tasks.taskEntries.get(2);
		// EntityAITaskEntry tempt = (EntityAITaskEntry)tasks.taskEntries.get(3);
		// EntityAITaskEntry follow = (EntityAITaskEntry)tasks.taskEntries.get(4);
		// tasks.removeTask(panic.action);
		// tasks.removeTask(mate.action);
		// tasks.removeTask(tempt.action);
		// tasks.removeTask(follow.action);
		// tasks.addTask(2, new EntityAIAttackMelee(this, EntityZombie.class, 1.0D, false));
		// tasks.addTask(2, new EntityAIAttackOnCollide(this, EntityPlayer.class, 1.0D, false));
		// targetTasks.addTask(1, new EntityAIHurtByTarget(this, true));
		// targetTasks.addTask(2, new EntityAINearestAttackableTarget(this, EntityZombie.class, 0, true));
	}

	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
		getEntityAttribute( SharedMonsterAttributes.MAX_HEALTH ).setBaseValue( 30.0 );
		getEntityAttribute( SharedMonsterAttributes.MOVEMENT_SPEED ).setBaseValue( 0.35 );
		getAttributeMap().registerAttribute( SharedMonsterAttributes.ATTACK_DAMAGE ).setBaseValue( 2.0 );
	}

	@Override
	public boolean hasCustomName()
	{
		return true;
	}

	@Override
	public String getCustomNameTag()
	{
		return "Cluckington";
	}

	@Override
	public boolean isBreedingItem( ItemStack stack )
	{
		return stack.getItem() == Items.SKULL && "wyld".equalsIgnoreCase( StackUtils.get( stack, "", "SkullOwner" ) );
	}

	@Override
	public void onLivingUpdate()
	{
		super.onLivingUpdate();
		if( ++healTimer > 40 )
		{
			healTimer = 0;
			heal( 1.0F );
		}
	}

	@Override
	public boolean attackEntityAsMob( Entity target )
	{
		return target.hurt( DamageSource.causeMobDamage( this ),
				(float)getEntityAttribute( SharedMonsterAttributes.ATTACK_DAMAGE ).getBaseValue() );
	}

	public static void spawn( EntityChicken target )
	{
		target.setHealth( 0 );
		target.setDead();
		final EntityCluckington cluck = new EntityCluckington( target.worldObj );
		cluck.setPositionAndRotation( target.posX, target.posY, target.posZ, target.rotationYaw, target.rotationPitch );
		cluck.worldObj.spawnEntityInWorld( cluck );
		cluck.worldObj.createExplosion( cluck, cluck.posX, cluck.posY, cluck.posZ, 1, true );
	}
}
