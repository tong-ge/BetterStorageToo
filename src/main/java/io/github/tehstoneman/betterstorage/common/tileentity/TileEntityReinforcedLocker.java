package io.github.tehstoneman.betterstorage.common.tileentity;

import io.github.tehstoneman.betterstorage.BetterStorage;
import io.github.tehstoneman.betterstorage.ModInfo;
import io.github.tehstoneman.betterstorage.api.EnumReinforced;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;

public class TileEntityReinforcedLocker extends TileEntityLocker
{
	public TileEntityReinforcedLocker()
	{
		super( BetterStorageTileEntityTypes.REINFORCED_LOCKER );
	}

	@Override
	public boolean canHaveLock()
	{
		return true;
	}

	/*@Override
	public void setAttachmentPosition()
	{
		final double x = mirror ? 13.5 : 2.5;
		final double y = isConnected() ? 0 : 8;
		lockAttachment.setBox( x, y, 0.5, 5, 5, 1 );
		lockAttachment.setScale( 0.375F, 1.5F );
	}*/

	@Override
	public int getColumns()
	{
		return 13;
		// return BetterStorage.config.reinforcedColumns;
	}

	@Override
	protected ITextComponent getConnectableName()
	{
		return new TextComponentTranslation( ModInfo.containerReinforcedLocker );
	}

	/*@Override
	public void setMaterial( EnumReinforced reinforcedMaterial )
	{
		material = reinforcedMaterial;
		markDirty();
	}*/
}
