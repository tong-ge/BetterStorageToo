package io.github.tehstoneman.betterstorage.common.tileentity;

import java.util.logging.Logger;

import com.google.common.collect.ImmutableMap;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.common.block.BlockLockable.EnumReinforced;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.model.animation.Animation;
import net.minecraftforge.common.animation.TimeValues.VariableValue;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityReinforcedChest extends TileEntityLockable
{
	private final IAnimationStateMachine	asm;
	private final VariableValue				clickTime	= new VariableValue( Float.NEGATIVE_INFINITY );

	public TileEntityReinforcedChest()
	{
		asm = BetterStorage.proxy.load( new ResourceLocation( ModInfo.modId, "asms/block/chest.json" ), ImmutableMap.of( "click_time", clickTime ) );
	}

	@Override
	public boolean hasCapability( Capability< ? > capability, EnumFacing facing )
	{
		return capability == CapabilityAnimation.ANIMATION_CAPABILITY || super.hasCapability( capability, facing );
	}

	@Override
	public <T> T getCapability( Capability< T > capability, EnumFacing facing )
	{
		if( capability == CapabilityAnimation.ANIMATION_CAPABILITY )
		{
			Logger.getLogger( ModInfo.modId ).info( "Calling animation capability" );
			return CapabilityAnimation.ANIMATION_CAPABILITY.cast( asm );
		}
		return super.getCapability( capability, facing );
	}

	@Override
	public boolean hasFastRenderer()
	{
		Logger.getLogger( ModInfo.modId ).info( "Checking fast renderer" );
		return true;
	}

	@Override
	@SideOnly( Side.CLIENT )
	public AxisAlignedBB getRenderBoundingBox()
	{
		if( isConnected() )
		{
			final EnumFacing connected = getConnected();
			if( connected == EnumFacing.NORTH )
				return new AxisAlignedBB( 0.0625F, 0.0F, 0.0F, 0.9375F, 14F / 16F, 0.9375F );
			else
				if( connected == EnumFacing.SOUTH )
					return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 1.0F );
				else
					if( connected == EnumFacing.WEST )
						return new AxisAlignedBB( 0.0F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 0.9375F );
					else
						if( connected == EnumFacing.EAST )
							return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 1.0F, 14F / 16F, 0.9375F );
		}

		return new AxisAlignedBB( 0.0625F, 0.0F, 0.0625F, 0.9375F, 14F / 16F, 0.9375F );
	}

	@Override
	public void setAttachmentPosition()
	{
		final double x = !isConnected() ? 8 : getOrientation() == EnumFacing.WEST || getOrientation() == EnumFacing.SOUTH ? 0 : 16;
		lockAttachment.setBox( x, 6.5, 0.5, 7, 7, 1 );
	}

	// TileEntityContainer stuff

	@Override
	public int getColumns()
	{
		return BetterStorage.config.reinforcedColumns;
	}

	// TileEntityConnactable stuff

	private static EnumFacing[] neighbors = { EnumFacing.EAST, EnumFacing.NORTH, EnumFacing.WEST, EnumFacing.SOUTH };

	@Override
	protected String getConnectableName()
	{
		return ModInfo.containerReinforcedChest;
	}

	@Override
	public EnumFacing[] getPossibleNeighbors()
	{
		return neighbors;
	}

	@Override
	public boolean onBlockActivated( BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing side, float hitX, float hitY,
			float hitZ )
	{
		if( asm != null )
		{
			Logger.getLogger( ModInfo.modId ).info( asm.currentState() );
			if( asm.currentState().equals( "open" ) )
			{
				final float time = Animation.getWorldTime( getWorld(), Animation.getPartialTickTime() );
				clickTime.setValue( time );
				asm.transition( "closing" );
			}
			else
				if( asm.currentState().equals( "closed" ) )
				{
					final float time = Animation.getWorldTime( getWorld(), Animation.getPartialTickTime() );
					clickTime.setValue( time );
					asm.transition( "opening" );
				}
		}
		return super.onBlockActivated( pos, state, player, hand, side, hitX, hitY, hitZ );
	}

	public void setCustomInventoryName( String displayName )
	{
		// TODO Auto-generated method stub

	}

	public void setMaterial( EnumReinforced material )
	{
		this.material = material;
	}
}
