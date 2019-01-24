package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;

import com.massivecraft.massivecore.MassiveException;

import org.bukkit.ChatColor;

import java.util.List;

public class CmdMarriageProposeRemove extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	public CmdMarriageProposeRemove()
	{
		// No Parameters
		this.addParameter(TypeMPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		boolean hasPendingProposal = msender.getPendingProposal();
		MPlayer mplayer = this.readArg();
		
		// Check if player has pending proposal
		if ( hasPendingProposal )
		{
			// Event
			EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, hasPendingProposal);
			event.run();
			
			if (event.isCancelled()) return;

			hasPendingProposal = event.hasNewProposal();
			
			// Inform Player
			mplayer.msg("%s<i> has cancelled their proposal.", msender.getName());
			msender.msg("You have cancelled your proposal to %s<i>", mplayer.getName());
			
			// Apply
			msender.setPendingProposal(false);
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
