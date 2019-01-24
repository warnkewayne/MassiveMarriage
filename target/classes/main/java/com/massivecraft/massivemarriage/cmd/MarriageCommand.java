package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivecore.command.MassiveCommand;

public class MarriageCommand extends MassiveCommand
{
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public MPlayer msender;
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public MarriageCommand()
	{
		this.setSetupEnabled(true);
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void senderFields(boolean set)
	{
		this.msender = set ? MPlayer.get(sender) : null;
	}
}
