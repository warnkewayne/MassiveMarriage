package com.massivecraft.massivemarriage.event;

import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivecore.event.EventMassiveCore;
import org.bukkit.command.CommandSender;

public abstract class EventMarriageAbstractSender extends EventMassiveCore
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	private final CommandSender sender;
	public CommandSender getSender() { return this.sender; }
	public MPlayer getMPlayer() { return this.sender == null ? null : MPlayer.get(this.sender); }
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMarriageAbstractSender(CommandSender sender)
	{
		this.sender = sender;
	}
	
	public EventMarriageAbstractSender(boolean async, CommandSender sender)
	{
		super(async);
		this.sender = sender;
	}
	
	
}
