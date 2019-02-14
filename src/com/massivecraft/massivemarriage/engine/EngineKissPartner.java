package com.massivecraft.massivemarriage.engine;


import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;

import com.massivecraft.massivecore.Engine;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.entity.Player;
import org.bukkit.World;
import org.bukkit.Location;
import org.bukkit.Particle;
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
	@EventHandler(priority = EventPriority.NORMAL) 		//WorldGuard onPlayerInteraction() Listener is Priority HIGH
	public void kissPartner(final PlayerInteractEntityEvent event)
	{
		// Check if event is cancelled, or feature is disabled.
		if ( event.isCancelled() ) return;
		if( ! MConf.get().enableKisses ) return;
		
		// Gather info
		final Player player = event.getPlayer();
		final MPlayer mplayer = MPlayer.get(player);
		
		// If the player is sneaking return
		if ( player.isSneaking() ) return;
		
		// If entity that is right click is a player...
		if(! (event.getRightClicked() instanceof Player) ) return;
		
		final Player cPlayer = (Player)event.getRightClicked();
		final MPlayer cMPlayer = MPlayer.get(cPlayer);
		
		// If the two players are married ...
		if( ! mplayer.isPartner(cMPlayer) ) return;
		
		// ...and the player has a flower in hand ...
		Material inHand = player.getInventory().getItemInMainHand().getType();
		if ( ! MConf.get().flowers.contains(inHand) ) return;
		
		// ... then get location of player ...
		Location playerLocation = player.getEyeLocation();
		Location partnerLocation = cPlayer.getEyeLocation();
		World playerWorld = player.getWorld();
		World partnerWorld = cPlayer.getWorld();
		
		// ... check cooldown ...
		if( checkCooldown(player) ) { event.setCancelled(true); return; }
		
		// ... then send kisses <3 <3 <3 ...
		playerWorld.spawnParticle(Particle.HEART, playerLocation, 1);
		partnerWorld.spawnParticle(Particle.HEART, partnerLocation, 1);
		
		// ... inform partner of that kiss
		mplayer.msg("<i>You have kissed %s<red> <3", MixinDisplayName.get().getDisplayName(cMPlayer, mplayer));
		cMPlayer.msg("%s<i> has kissed you<red> <3", MixinDisplayName.get().getDisplayName(mplayer, cMPlayer));
		
		// Cancel event to not interfere with MassiveBasic & Others
		event.setCancelled(true);
	}
	
	// Handles Message Sending Cooldown in EngineKissPartner
	private HashMap<String, Long> cooldowns = new HashMap<>();
	
	private boolean checkCooldown(Player player)
	{
		if(cooldowns.containsKey(player.getName()))
		{
			//We divide the cooldown in milliseconds to seconds
			long secondsLeft = ((cooldowns.get(player.getName())/1000)+ MConf.get().cooldownKisses) - (System.currentTimeMillis()/1000);
			
			if( secondsLeft > 0 ) return true; // cooldown has time left
		}
		
		// No cooldown found or cooldown has expired, save new cooldown
		cooldowns.put(player.getName(), System.currentTimeMillis());
		return false;
	}
}
