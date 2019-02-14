package com.massivecraft.massivemarriage.entity;

import com.massivecraft.massivecore.collections.ExceptionSet;
import com.massivecraft.massivecore.command.editor.annotation.EditorName;
import com.massivecraft.massivecore.command.editor.annotation.EditorType;
import com.massivecraft.massivecore.command.editor.annotation.EditorTypeInner;
import com.massivecraft.massivecore.command.type.TypeMillisDiff;
import com.massivecraft.massivecore.store.Entity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.TimeUnit;

import java.util.List;
import java.util.Map;

@EditorName("config")
public class MConf extends Entity<MConf>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //
	
	protected static transient MConf i;
	public static MConf get() { return i; }
	
	// -------------------------------------------- //
	// LOGGING
	// -------------------------------------------- //
	
	// Here you can disable logging of certain events to the server console.
	
	public boolean logMarriage = true;
	public boolean logDivorce = true;
	
	// -------------------------------------------- //
	// REMOVE DATA
	// -------------------------------------------- //
	
	// Should players' marriages be removed and their data erased when banned?
	public boolean removePlayerWhenBanned = true;
	
	// After how many milliseconds should players be automatically divorced?
	@EditorType(TypeMillisDiff.class)
	public long cleanInactivityToleranceMillis = 10 * TimeUnit.MILLIS_PER_DAY; //10 Days
	
	// Player Age Bonus
	@EditorTypeInner({TypeMillisDiff.class, TypeMillisDiff.class})
	public Map<Long, Long> cleanInactivityToleranceMillisPlayerAgeToBonus = MUtil.map(
		2 * TimeUnit.MILLIS_PER_WEEK, 10 * TimeUnit.MILLIS_PER_DAY  // +10 days after 2 weeks
	);
	
	// -------------------------------------------- //
	// MARRIAGE COSTS
	// -------------------------------------------- //
	
	// Here you can edit if marriages cost regals
	// and how much it costs for EACH player.
	
	public double marriageCostAmount = 50;
	
	// -------------------------------------------- //
	// MARRIAGE XP BOOST
	// -------------------------------------------- //
	
	// Here you can enable/disable marriage xp boosts and
	// edit the amount marriage boosts xp (Percentage)
	// and the block radius where the boosts are in effect.
	
	public boolean enableXpBoost = true;
	public double xpBoostFactor = .3; // if value is .3, xp boosted 30%
	public double xpBlockRadius = 30;
	
	// -------------------------------------------- //
	// PARTNER KISSES
	// -------------------------------------------- //
	
	// Here you can enable/disable players' ability to
	// kiss their partners by right-clicking them while holding the items within the set
	// and config the cooldown for kisses.
	
	public boolean enableKisses = true;
	
	public int cooldownKisses = 60; //60 seconds
	
	// Set of flowers used in EngineKissPartner
	public ExceptionSet flowers = new ExceptionSet(false,
		"RED_ROSE",
		"YELLOW_FLOWER",
		"DEAD_BUSH",
		"DOUBLE_PLANT"
		);
	
	// -------------------------------------------- //
	// BROADCAST MARRIAGES
	// -------------------------------------------- //
	
	// Here you can disable broadcasts of marriages
	//TODO: Option to broadcast nicknames instead of usernames?
	
	public boolean broadcastMarriages = true;
	
	
	// -------------------------------------------- //
	// COMMAND ALIASES
	// -------------------------------------------- //
	
	public List<String> aliasesMarriage = MUtil.list("marry");
	public List<String> aliasesMarriagePropose = MUtil.list("propose");
	public List<String> aliasesMarriageProposeRemove = MUtil.list("unpropose");
	public List<String> aliasesMarriageAcceptProposal = MUtil.list("accept");
	public List<String> aliasesMarriageDenyProposal = MUtil.list("deny");
	public List<String> aliasesMarriageShow = MUtil.list("show");
	public List<String> aliasesMarriageDivorce = MUtil.list("divorce");
	public List<String> aliasesMarriageConfig = MUtil.list("config");
	public List<String> aliasesMarriageVersion = MUtil.list("v", "version");
}
