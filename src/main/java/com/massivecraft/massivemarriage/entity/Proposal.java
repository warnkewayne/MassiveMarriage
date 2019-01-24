package com.massivecraft.massivemarriage.entity;

import com.massivecraft.massivecore.store.EntityInternal;

public class Proposal extends EntityInternal<Proposal>
{
	// -------------------------------------------- //
	// OVERRIDE: ENTITY
	// -------------------------------------------- //
	
	@Override
	public Proposal load(Proposal that)
	{
		this.inviterId = that.inviterId;
		this.receiverId = that.receiverId;
		this.creationMillis = that.creationMillis;
		
		return this;
	}
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private String inviterId;
	public String getInviterId() { return inviterId; }
	public void setInviterId(String inviterId) { this.inviterId = inviterId; }
	
	private String receiverId;
	public String getReceiverId() { return receiverId; }
	public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
	
	private Long creationMillis;
	public Long getCreationMillis() { return creationMillis; }
	public void setCreationMillis(Long creationMillis) { this.creationMillis = creationMillis; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public Proposal() { this(null, null, null); }
	
	public Proposal(String inviterId, String receiverId, Long creationMillis)
	{
		this.inviterId = inviterId;
		this.receiverId = receiverId;
		this.creationMillis = creationMillis;
	}
	
	// -------------------------------------------- //
	// DESTRUCT
	// -------------------------------------------- //
	
	protected void finalize() throws Throwable
	{
		setCreationMillis(null);
		setReceiverId(null);
		setInviterId(null);
		
		super.finalize();
	}
	
}
