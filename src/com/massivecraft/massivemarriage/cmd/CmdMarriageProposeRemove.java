package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
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
		this.addParameter(TypeMPlayer.get());
		
		this.addRequirements(RequirementHasPerm.get(Perm.UNPROPOSE));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		MPlayer mplayer = this.readArg();
		
		// Check if player has pending proposal
		if ( mplayer.getProposedPlayerId() != null )
		{
			// Event
			EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, true);
			event.run();
			
			if (event.isCancelled()) return;

			// Check if sent name matches with proposed player.
			if ( ! (msender.getPartnerId() == IdUtil.getId(mplayer)) ) throw new MassiveException().addMsg("<b>You did not propose to them.");
			
			// Inform Player
			mplayer.msg("%s<i> has cancelled their proposal.", msender.getName());
			msender.msg("You have cancelled your proposal to %s<i>", mplayer.getName());
			
			// Apply
			msender.setProposedPlayerId(null);
		}
		else
		{
			// Inform player
			throw new MassiveException().addMsg("<b>You do not have a pending proposal."); //.color(ChatColor.RED);
		}
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriageProposeRemove; }
}
