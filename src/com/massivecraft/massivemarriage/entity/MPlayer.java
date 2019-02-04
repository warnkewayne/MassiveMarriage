package com.massivecraft.massivemarriage.entity;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.store.SenderEntity;
import com.massivecraft.massivecore.util.MUtil;
import com.massivecraft.massivecore.util.IdUtil;

public class MPlayer extends SenderEntity<MPlayer>
{
	// -------------------------------------------- //
	// META
	// -------------------------------------------- //

	public static MPlayer get(Object oid) { return MPlayerColl.get().get(oid); }
	
	//----------------------------------------------//
	// OVERRIDE
	//----------------------------------------------//
	
	@Override
	public MPlayer load(MPlayer that)
	{
		this.lastActivityMillis = that.lastActivityMillis;
		this.partnerId = that.partnerId;
		this.proposedPlayerId = that.proposedPlayerId;
		this.suitors = that.suitors;
		
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS: RAW
	// -------------------------------------------- //
	
	// The last known time of player activity (login OR logout)
	// This is for removing cleanable players.
	// Default is set to the current time.
	private long lastActivityMillis = System.currentTimeMillis();
	
	// This is a foreign key
	// Each player may be married to one other player
	// Null is default (No marriage partner)
	private String partnerId = null;
	
	// This is a foreign key
	// This will hold the player that MPlayer has
	// 	  sent a proposal to.
	// Default is null.
	private String proposedPlayerId = null;
	
	// This is a set of playerIds that proposed to
	// MPlayer.
	// Default is set to empty.
	private MassiveSet<String> suitors = new MassiveSet<>();
	
	

	// -------------------------------------------- //
	// CORE UTILITIES
	// -------------------------------------------- //
	
	public void resetMarriageData()
	{
		setPartnerId(null);
		setProposedPlayerId(null);
		emptySuitors();
	}
	
	// -------------------------------------------- //
	// FIELD: lastActivityMillis
	// -------------------------------------------- //
	
	public long getLastActivityMillis() { return this.lastActivityMillis; }
	
	public void setLastActivityMillis( long lastActivityMillis )
	{
		this.lastActivityMillis = convertSet(lastActivityMillis, this.lastActivityMillis, null);
	}
	
	public void setLastActivityMillis() { this.setLastActivityMillis(System.currentTimeMillis()); }
	
	@Override
	public boolean shouldBeCleaned(long now) { return this.shouldBeCleaned(now, this.lastActivityMillis);}
	
	// -------------------------------------------- //
	// FIELD: partnerId
	// -------------------------------------------- //
	
	public String getPartnerId() { return this.partnerId; }

	public boolean hasPartner()
	{
		return this.getPartnerId() != null;
	}
	
	public void setPartnerId(String partnerId)
	{
		// Before
		String beforeId = this.partnerId;
		
		// NoChange
		if (MUtil.equals(beforeId, partnerId)) return;
		
		// Apply
		this.partnerId = partnerId;
		
		// Mark as changed
		this.changed();
		
	}
	
	public boolean isPartner(MPlayer mPlayer)
	{
		String mPlayerId = IdUtil.getId(mPlayer);
		
		if ( this.partnerId == null ) return false;
		
		return this.partnerId.equals(mPlayerId);
	}

	// -------------------------------------------- //
	// FIELD: proposalPlayerId
	// -------------------------------------------- //
	public String getProposedPlayerId() { return this.proposedPlayerId; }

	public void setProposedPlayerId(String proposedPlayerId) { this.proposedPlayerId = proposedPlayerId; this.changed(); }
	
	// -------------------------------------------- //
	// FIELD: suitors
	// -------------------------------------------- //
	public void addToSuitors(String playerId) { suitors.add(playerId); this.changed(); }
	
	public void removeFromSuitors(String playerId) { suitors.remove(playerId); this.changed(); }
	
	public boolean isSuitor(String playerId) { return suitors.contains(playerId); }
	
	public MassiveSet<String> getSuitors() { return suitors; }
	
	public void setSuitors(MassiveSet<String> suitors) { this.suitors = suitors; this.changed(); }
	
	public void emptySuitors()
	{
		MassiveSet<String> suitors = this.getSuitors();
		
		for (String ms: suitors)
		{
			MPlayer.get(ms).setProposedPlayerId(null);
		}
		
		suitors.clear();
		
		this.changed();
	}
	
	public boolean hasSuitors() { return ! (this.suitors.isEmpty()); }
}
