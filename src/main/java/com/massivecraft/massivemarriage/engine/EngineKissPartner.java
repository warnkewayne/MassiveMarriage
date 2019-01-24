package com.massivecraft.massivemarriage.engine;


import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayerColl;
import com.massivecraft.massivemarriage.entity.MPlayer;

import com.massivecraft.massivecore.Engine;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class EngineKissPartner extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //

	private static EngineKissPartner i = new EngineKissPartner();
	public static EngineKissPartner get() { return i; }

	// -------------------------------------------- //
	// KISS
	// -------------------------------------------- //
	@EventHandler(priority = EventPriority.MONITOR)
	public void kissPartner(final PlayerInteractEntityEvent event)
	{
		if( ! MConf.get().enableKisses ) return;
		
		// Gather info
		final Player player = event.getPlayer();
		final MPlayer mplayer = MPlayer.get(player);
		
		if(! (event.getRightClicked() instanceof Player) ) return;
		final Player cPlayer = (Player)event.getRightClicked();
		final MPlayer cMPlayer = MPlayer.get(cPlayer);
		
		// If the two players are married ...
		if( ! mplayer.checkIfPartner(cMPlayer) ) return;
		
		// ...and the player who clicked is sneaking ...
		if ( ! player.isSneaking() ) return;
		
		// ...and the player has a flower in hand ...
		Material inHand = player.getInventory().getItemInMainHand().getType();
		if ( ! MConf.get().flowers.contains(inHand) ) return;
		
		// ... then get location of player ...
		Location playerLocation = player.getEyeLocation();
		World playerWorld = player.getWorld();
		
		// ... then send kisses <3 <3 <3 ...
		playerWorld.spawnParticle(Particle.HEART, playerLocation, 1);
		
		// ... check cooldown ...
		if( MConf.get().cooldownHandler(player) ) { event.setCancelled(true); return; }
		// ... inform partner of that kiss
		cMPlayer.msg("%s<i> has kissed you <3", mplayer.getName());
		
		// Cancel event to not interfere with MassiveBasic & Others
		event.setCancelled(true);
	}
}
