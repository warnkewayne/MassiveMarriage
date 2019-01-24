package com.massivecraft.massivemarriage;

import com.massivecraft.massivecore.MassivePlugin;
import com.massivecraft.massivemarriage.cmd.CmdMarriage;
import com.massivecraft.massivemarriage.engine.EngineCleanInactivity;
import com.massivecraft.massivemarriage.engine.EngineKissPartner;
import com.massivecraft.massivemarriage.engine.EngineLastActivity;
import com.massivecraft.massivemarriage.engine.EnginePlayerData;
import com.massivecraft.massivemarriage.engine.EngineXPBoost;
import com.massivecraft.massivemarriage.entity.MConfColl;
import com.massivecraft.massivemarriage.entity.MPlayerColl;

public class MassiveMarriage extends MassivePlugin
{
	// -------------------------------------------- //
	// INSTANCE & CONSTRUCT
	// -------------------------------------------- //
	
	private static MassiveMarriage i;
	public static MassiveMarriage get() { return i; }
	public MassiveMarriage() { MassiveMarriage.i = this; }
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void onEnableInner()
	{
		//Activate
		this.activate(
			//Coll
			MConfColl.class,
			MPlayerColl.class,
			
			//Engine
			EngineLastActivity.class,
			EnginePlayerData.class,
			EngineCleanInactivity.class,
			EngineKissPartner.class,
			EngineXPBoost.class,
			
			//Command
			CmdMarriage.class
			
		);
		
	}

}