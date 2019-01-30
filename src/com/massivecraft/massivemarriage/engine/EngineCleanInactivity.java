package com.massivecraft.massivemarriage.engine;

import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayerColl;

import com.massivecraft.massivecore.Engine;
import com.massivecraft.massivecore.event.EventMassiveCorePlayerCleanInactivityToleranceMillis;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

import java.util.Map;
import java.util.Map.Entry;

public class EngineCleanInactivity extends Engine
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static EngineCleanInactivity i = new EngineCleanInactivity();
	public static EngineCleanInactivity get() { return i; }
	
	// -------------------------------------------- //
	// REMOVE PLAYER MILLIS
	// -------------------------------------------- //
	
	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
	public void ageBonus(EventMassiveCorePlayerCleanInactivityToleranceMillis event)
	{
		if( event.getColl() != MPlayerColl.get() ) return;
		
		applyPlayerAgeBonus(event);
	}
	
	private void applyPlayerAgeBonus(EventMassiveCorePlayerCleanInactivityToleranceMillis event)
	{
		// Calculate First played
		Long firstPlayed = event.getEntity().getFirstPlayed();
		Long age = 0L;
		if( firstPlayed != null )
		{
			age = System.currentTimeMillis() - firstPlayed;
		}
		
		// Calculate age Bonus
		Long bonus = calculateBonus(age, MConf.get().cleanInactivityToleranceMillisPlayerAgeToBonus);
		if (bonus == null) return;
		
		// Apply
		event.getToleranceCauseMillis().put("Player Age Bonus", bonus);
	}
	
	private Long calculateBonus(long age, Map<Long, Long> ageToBonus)
	{
		if( ageToBonus.isEmpty() ) return null;
		
		Long bonus = 0L;
		for(Entry<Long, Long> entry: ageToBonus.entrySet())
		{
			Long key = entry.getKey();
			if( key == null ) continue;
			
			Long value = entry.getValue();
			if( value == null) continue;
			
			if( age >= key ) { bonus = value; break; }
		}
		
		return bonus;
	}
}
