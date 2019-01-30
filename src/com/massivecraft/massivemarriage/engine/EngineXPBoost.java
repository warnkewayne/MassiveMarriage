package com.massivecraft.massivemarriage.engine;

import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.util.MUtil;

import org.bukkit.event.player.PlayerExpChangeEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import org.bukkit.entity.Player;


public class EngineXPBoost extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineXPBoost i = new EngineXPBoost();
	public static EngineXPBoost get() { return i; }
	
	// -------------------------------------------- //
	// XP BOOST
	// -------------------------------------------- //
	@EventHandler(priority = EventPriority.HIGH)
	public void xpBoost(final PlayerExpChangeEvent event)
	{
		if ( ! MConf.get().enableXpBoost ) return;
		
		// Gather info
		final Player player = event.getPlayer();
		if( MUtil.isntPlayer(player) ) return;
		final MPlayer mplayer = MPlayer.get(player);
		
		// If partner is married ...
		if( ! mplayer.hasPartner() ) return;
		final Player pPartner = IdUtil.getPlayer(mplayer.getPartnerId());
		
		if (MUtil.isntPlayer(pPartner)) return;
		
		// ... check if worlds are null ...
		String playerWorld = player.getWorld().toString();
		String partnerWorld = pPartner.getWorld().toString();
		
		if ( playerWorld == null || partnerWorld == null ) return;
		
		// ... and if the players are in the same world
		if ( ! playerWorld.equals(partnerWorld) ) return;
		
		// ... and while the two players are within a certain distance
		double distanceSquared = pPartner.getLocation().distanceSquared(player.getLocation());
		double blockRadius = MConf.get().xpBlockRadius;
		double blockRadiusSquared = blockRadius * blockRadius;
		
		if( distanceSquared > blockRadiusSquared ) return;
		
		// ... then boost the xp
		int vanillaXP = event.getAmount();
		double boost = MConf.get().xpBoostFactor;
		
		double addOn = vanillaXP * boost;
		
		// cast addOn to an int
		int addExp = (int)addOn;
		
		event.setAmount(addExp + vanillaXP);
		mplayer.msg("Your XP was: " + vanillaXP);
		mplayer.msg("Your XP is now: " + addExp);
	}
}


