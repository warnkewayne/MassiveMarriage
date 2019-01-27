package com.massivecraft.massivemarriage.entity;

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
		this.setLastActivityMillis(that.lastActivityMillis);
		this.setPartnerId(that.partnerId);
		this.setProposedPlayerId(that.proposedPlayerId);
		
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

	// This will hold the player that MPlayer has
	// 	  sent a proposal to.
	// Default is null.
	private String proposedPlayerId = null;
	

	// -------------------------------------------- //
	// CORE UTILITIES
	// -------------------------------------------- //
	
	public void resetMarriageData()
	{
		this.partnerId = null;
		this.proposedPlayerId = null;
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

	//TODO: getPartner() accepts different datatypes
	
	public boolean hasPartner()
	{
		if( this.getPartnerId() != null ) return true;
		else { return false; }
	}
	
	public void setPartnerId(String partnerId)
	{
		// Before
		String beforeId = this.partnerId;
		
		// After
		String afterId = partnerId;
		
		// NoChange
		if (MUtil.equals(beforeId, afterId)) return;
		
		// Apply
		this.partnerId = afterId;
		
		// Mark as changed
		this.changed();
		
	}
	
	public boolean checkIfPartner(MPlayer mPlayer)
	{
		String mPlayerId = IdUtil.getId(mPlayer);
		
		if(this.partnerId.equals(mPlayerId)) return true;
		
		
		return false;
	}

	// -------------------------------------------- //
	// FIELD: proposalPlayerId
	// -------------------------------------------- //
	public String getProposedPlayerId() { return this.proposedPlayerId; }

	public void setProposedPlayerId(String proposedPlayerId) { this.proposedPlayerId = proposedPlayerId; }

	
}
