package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.MassiveCommandVersion;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivemarriage.MassiveMarriage;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.entity.MConf;

import java.util.List;

public class CmdMarriageVersion extends MassiveCommandVersion
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageVersion()
	{
		super(MassiveMarriage.get());
		this.addRequirements(RequirementHasPerm.get(Perm.VERSION));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases()
	{
		return MConf.get().aliasesMarriageVersion;
	}
	
}
