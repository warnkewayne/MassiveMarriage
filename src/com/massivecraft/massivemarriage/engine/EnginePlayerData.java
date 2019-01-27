package com.massivecraft.massivemarriage.engine;

import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.entity.MPlayerColl;

import com.massivecraft.massivecore.Engine;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerKickEvent;

public class EnginePlayerData extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EnginePlayerData i = new EnginePlayerData();
	public static EnginePlayerData get() { return i; }

	// -------------------------------------------- //
	// REMOVE PLAYER DATA WHEN BANNED
	// -------------------------------------------- //

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerKick(PlayerKickEvent event)
	{
		// If a player was kicked from the server ...
		Player player = event.getPlayer();

		// ... and if the if player was banned (not just kicked) ...
		//if (!event.getReason().equals("Banned by admin.")) return;
		if ( ! player.isBanned() ) return;

		// ... and we remove player data when banned ...
		if ( ! MConf.get().removePlayerWhenBanned ) return;
		
		// ... get rid of their stored info.
		MPlayer mplayer = MPlayerColl.get().get(player, false);
		if ( mplayer == null ) return;
		
		// ... if banned player is married
		if ( mplayer.getPartnerId() != null )
		{
			// ... get there partner info
			MPlayer partner = MPlayer.get(mplayer.getPartnerId());
			
			// ... reset partner's marriage data
			partner.resetMarriageData();
			
			// ... inform the partner
			partner.msg("Your partner has been banned. You are now single and ready to mingle!");
		}

		mplayer.resetMarriageData();
		mplayer.detach();
	}
}
