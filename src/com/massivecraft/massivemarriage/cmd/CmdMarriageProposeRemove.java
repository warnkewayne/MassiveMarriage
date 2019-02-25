package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;

import com.massivecraft.massivecore.MassiveException;

import java.util.List;

public class CmdMarriageProposeRemove extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	public CmdMarriageProposeRemove()
	{
		this.addRequirements(RequirementHasPerm.get(Perm.UNPROPOSE));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Annoy WumosWared
		if ( IdUtil.getId(sender).equals(MConf.get().jaredsID)) throw new MassiveException().addMsg("<b>Sorry Jared, give Rusty his credit.");
		
		// Check if player has pending proposal
		if ( msender.getProposedPlayerId() != null )
		{
			MPlayer proposedPlayer = MPlayer.get(msender.getProposedPlayerId());
			
			// Event
			EventMarriageProposalChange event = new EventMarriageProposalChange(sender, proposedPlayer, true);
			event.run();
			
			if (event.isCancelled()) return;
			
			// Inform Player
			proposedPlayer.msg("%s<i> has cancelled their proposal.", MixinDisplayName.get().getDisplayName(msender, proposedPlayer));
			message("<i>%s have cancelled your proposal to <white>%s", MixinDisplayName.get().getDisplayName(msender, msender), MixinDisplayName.get().getDisplayName(proposedPlayer, msender));
			
			// Apply
			msender.setProposedPlayerId(null);
			proposedPlayer.removeFromSuitors(msender.getId());
		}
		else
		{
			// Inform player
			throw new MassiveException().addMsg("<b>You do not have a pending proposal.");
		}
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().aliasesMarriageProposeRemove; }
}
