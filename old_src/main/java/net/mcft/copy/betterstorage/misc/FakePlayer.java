package io.github.tehstoneman.betterstorage.misc;

import com.mojang.authlib.GameProfile;

import io.github.tehstoneman.betterstorage.utils.WorldUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.BlockEntity;
import net.minecraft.world.Level;

public class FakePlayer extends EntityPlayer
{
	private static final GameProfile	profile	= new GameProfile( null, "[BETTERSTORAGE]" );
	private static FakePlayer			player;

	private FakePlayer( Level world )
	{
		super( world, profile );
	}

	/*
	 * @Override
	 * public void addChatMessage(IChatComponent message) { }
	 */

	/*
	 * @Override
	 * public ChunkCoordinates getPlayerCoordinates() { return null; }
	 */

	@Override
	public boolean canCommandSenderUseCommand( int permissionLevel, String commandName )
	{
		return false;
	}

	public static FakePlayer get( Level world )
	{
		if( player == null )
			player = new FakePlayer( world );
		else
			player.setWorld( world );
		return player;
	}

	public static FakePlayer get( BlockEntity entity )
	{
		return get( entity != null ? entity.getLevel() : WorldUtils.getLocalWorld() );
	}

	public static void unset()
	{
		player.setWorld( null );
	}

	@Override
	public boolean isSpectator()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCreative()
	{
		// TODO Auto-generated method stub
		return false;
	}
}
