package io.github.tehstoneman.betterstorage.common.block;

import io.github.tehstoneman.betterstorage.api.ConnectedType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public abstract class BlockConnectableContainer extends BlockContainerBetterStorage
{
	public static final EnumProperty< ConnectedType > TYPE = EnumProperty.create( "type", ConnectedType.class );

	protected BlockConnectableContainer( Properties builder )
	{
		super( builder );

		registerDefaultState( defaultBlockState().setValue( TYPE, ConnectedType.SINGLE ) );
	}

	@Override
	protected void createBlockStateDefinition( StateDefinition.Builder< Block, BlockState > builder )
	{
		super.createBlockStateDefinition( builder );
		builder.add( TYPE );
	}
}
