package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivemarriage.MassiveMarriage;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageStatusChange;
import com.massivecraft.massivemarriage.event.EventMarriageStatusChange.StatusChangeReason;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;

import java.util.List;

public class CmdMarriageDivorce extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageDivorce()
	{
		// No parameters
		
		this.addRequirements(RequirementHasPerm.get(Perm.DIVORCE));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Annoy WumosWared
		if ( IdUtil.getId(sender).equals(MConf.get().jaredsID)) throw new MassiveException().addMsg("<b>Sorry Jared, give Rusty his credit.");
		
		// Check if sender is married
		if( ! msender.hasPartner() ) throw new MassiveException().addMsg("<b>You are not married!");
		
		// Get msender info
		String partnerId = msender.getPartnerId();
		MPlayer partner = MPlayer.get(partnerId);
		
		// Event
		EventMarriageStatusChange statusChangeEvent = new EventMarriageStatusChange(sender, msender, partner, StatusChangeReason.DIVORCE);
		statusChangeEvent.run();
		
		if ( statusChangeEvent.isCancelled() ) return;
		
		// Inform
		partner.msg("%s<i> has divorced %s.", MixinDisplayName.get().getDisplayName(msender, partner), MixinDisplayName.get().getDisplayName(partner, partner));
		msender.msg("<i>%s have divorced <white>%s.", MixinDisplayName.get().getDisplayName(msender, msender), MixinDisplayName.get().getDisplayName(partner, msender));
		
		// Apply
		partner.resetMarriageData();
		msender.resetMarriageData();
		
		// Logging
		if( MConf.get().logDivorce )
		{
			MassiveMarriage.get().log(Txt.parse("%s and %s have gotten divorced.", partner.getName(), msender.getName()));
		}
		
	}
		
		// -------------------------------------------- //
		// COMMAND ALIASES
		// -------------------------------------------- //
		
		@Override
		public List<String> getAliases() { return MConf.get().aliasesMarriageDivorce; }
}
