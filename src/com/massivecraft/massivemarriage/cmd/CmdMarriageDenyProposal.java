package com.massivecraft.massivemarriage.cmd;


import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;

import com.massivecraft.massivecore.MassiveException;

import java.util.List;

public class CmdMarriageDenyProposal extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageDenyProposal()
	{
		this.addParameter(TypeMPlayer.get());
		
		this.addRequirements(RequirementHasPerm.get(Perm.DENY));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	@Override
	public void perform() throws MassiveException
	{
		// Args
		MPlayer mplayer = this.readArg();
		
		String senderId = msender.getId();
		
		// Annoy WumosWared
		if ( senderId.equals(MConf.get().jaredsID)) throw new MassiveException().addMsg("<b>Sorry Jared, give Rusty his credit.");
		
		// Check Player has proposal request
		// Is the proposal request to sender?
		String mpPpId = mplayer.getProposedPlayerId();
		if ( mpPpId == null || ! mpPpId.equals(senderId) ) throw new MassiveException().addMsg("<b>They did not send you a proposal!");
		
		// Event
		EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, true);
		event.run();
		
		if ( event.isCancelled() ) return;
		
		// Inform Player
		msender.msg("<i>You have denied the proposal from <white>%s.", MixinDisplayName.get().getDisplayName(mplayer, msender));
		mplayer.msg("%s<i> has denied your proposal.", MixinDisplayName.get().getDisplayName(msender, mplayer));
		
		// Apply
		mplayer.setProposedPlayerId(null);
		msender.removeFromSuitors(mplayer.getId());
		
	}

	@Override
	public List<String> getAliases() { return MConf.get().aliasesMarriageDenyProposal; }
}
