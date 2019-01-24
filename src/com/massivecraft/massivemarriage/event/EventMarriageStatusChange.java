package com.massivecraft.massivemarriage.event;

import com.massivecraft.massivemarriage.entity.MPlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.HandlerList;

public class EventMarriageStatusChange extends EventMarriageAbstractSender
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
	
	@Override
	public void setCancelled(boolean cancelled)
	{
		if (!this.reason.isCancellable()) cancelled = false;
		super.setCancelled(cancelled);
	}
	
	private final MPlayer mplayer1;
	public MPlayer getMPlayer1() { return this.mplayer1; }
	
	private final MPlayer mplayer2;
	public MPlayer getMplayer2() { return this.mplayer2; }
	
	private final StatusChangeReason reason;
	public StatusChangeReason getReason() { return this.reason; }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMarriageStatusChange(CommandSender sender, MPlayer mplayer1, MPlayer mplayer2, StatusChangeReason reason)
	{
		super(sender);
		this.mplayer1 = mplayer1;
		this.mplayer2 = mplayer2;
		this.reason = reason;
	}
	
	// -------------------------------------------- //
	// REASON ENUM
	// -------------------------------------------- //
	
	public enum StatusChangeReason
	{
		// Join
		MARRY (true),
		
		// Leave
		DIVORCE (true),
		;
		
		private final boolean cancellable;
		public boolean isCancellable() { return this.cancellable; }
		
		StatusChangeReason(boolean cancellable)
		{
			this.cancellable = cancellable;
		}
	}
}

