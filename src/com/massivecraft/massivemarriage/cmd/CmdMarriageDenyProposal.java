package com.massivecraft.massivemarriage.cmd;


import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.IdUtil;

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
		
		String senderId = IdUtil.getId(sender);
		
		// Check Player has proposal request
		// Is the proposal request to sender?
		String mpPpId = mplayer.getProposedPlayerId();
		
		if ( mpPpId == null ) throw new MassiveException().addMsg("<b>They did not send you a proposal!");
		if ( ! mpPpId.equals(senderId)) throw new MassiveException().addMsg("<b>They did not send you a proposal!");
		
		// Event
		EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, true);
		event.run();
		
		if ( event.isCancelled() ) return;
		
		// Inform Player
		msender.msg("<i>You have denied the proposal from <white>%s.", mplayer.getName());
		mplayer.msg("%s<i> has denied your proposal.", sender.getName());
		
		// Apply
		mplayer.setProposedPlayerId(null);
		
	}

	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriageDenyProposal; }
}
