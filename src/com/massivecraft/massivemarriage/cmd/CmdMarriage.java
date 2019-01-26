package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.MassiveCommand;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.entity.MConf;

import java.util.List;

public class CmdMarriage extends MassiveCommand
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	private static CmdMarriage i = new CmdMarriage();
	public static CmdMarriage get() { return i; }
	
	// -------------------------------------------- //
	// FIELDS
	// -------------------------------------------- //
	
	public CmdMarriagePropose cmdMarriagePropose = new CmdMarriagePropose();
	public CmdMarriageProposeRemove cmdMarriageProposeRemove = new CmdMarriageProposeRemove();
	public CmdMarriageAcceptProposal cmdMarriageAcceptProposal = new CmdMarriageAcceptProposal();
	public CmdMarriageDenyProposal cmdMarriageDenyProposal = new CmdMarriageDenyProposal();
	public CmdMarriageShow cmdMarriageShow = new CmdMarriageShow();
	public CmdMarriageDivorce cmdMarriageDivorce = new CmdMarriageDivorce();
	public CmdMarriageConfig cmdMarriageConfig = new CmdMarriageConfig();
	public CmdMarriageVersion cmdMarriageVersion = new CmdMarriageVersion();
	
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriage()
	{
		//Children
		this.addChild(this.cmdMarriagePropose);
		this.addChild(this.cmdMarriageProposeRemove);
		this.addChild(this.cmdMarriageAcceptProposal);
		this.addChild(this.cmdMarriageDenyProposal);
		this.addChild(this.cmdMarriageShow);
		this.addChild(this.cmdMarriageDivorce);
		this.addChild(this.cmdMarriageConfig);
		this.addChild(this.cmdMarriageVersion);
		
		this.addRequirements(RequirementHasPerm.get(Perm.BASECOMMAND));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	@Override
	public List<String> getAliases() { return MConf.get().aliasesMarriage; }
	
}
