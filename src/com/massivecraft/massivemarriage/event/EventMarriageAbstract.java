package com.massivecraft.massivemarriage.event;

import com.massivecraft.massivecore.event.EventMassiveCore;

public abstract class EventMarriageAbstract extends EventMassiveCore
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public EventMarriageAbstract()
	{
	
	}
	
	public EventMarriageAbstract(boolean isAsync)
	{
		super(isAsync);
	}
}
