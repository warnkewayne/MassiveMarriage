package com.massivecraft.massivemarriage.event;

import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.entity.Proposal;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventMarriageProposalChange extends EventMarriageAbstractSender
{
	// -------------------------------------------- //
	// REQUIRED EVENT CODE
	// -------------------------------------------- //
	
	private static final HandlerList handlers = new HandlerList();
	@Override public HandlerList getHandlers() { return handlers; }
	public static HandlerList getHandlerList() { return handlers; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final MPlayer mplayer;
	public MPlayer getMPlayer() { return this.mplayer; }
	
	private boolean newProposal;
	public boolean hasNewProposal() { return this.newProposal; }
	public void setNewProposal(boolean newProposal) { this.newProposal = newProposal; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMarriageProposalChange(CommandSender sender, MPlayer mplayer, boolean newProposal)
	{
		super(sender);
		this.mplayer = mplayer;
		this.newProposal = newProposal;
	}
}
