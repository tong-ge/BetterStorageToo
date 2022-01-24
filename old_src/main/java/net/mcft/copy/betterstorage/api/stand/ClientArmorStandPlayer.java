package io.github.tehstoneman.betterstorage.api.stand;

import com.mojang.authlib.GameProfile;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.text.Component;
import net.minecraft.world.Level;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly( Side.CLIENT )
public class ClientArmorStandPlayer extends AbstractClientPlayer
{

	public ClientArmorStandPlayer( Level world )
	{
		super( world, new GameProfile( null, "[ARMOR STAND]" ) );
		setInvisible( true );
	}

	@Override
	public void addChatMessage( Component message )
	{}

	/*
	 * @Override
	 * public ChunkCoordinates getPlayerCoordinates()
	 * {
	 * return null;
	 * }
	 */

	@Override
	public boolean canCommandSenderUseCommand( int permissionLevel, String commandName )
	{
		return false;
	}

}
