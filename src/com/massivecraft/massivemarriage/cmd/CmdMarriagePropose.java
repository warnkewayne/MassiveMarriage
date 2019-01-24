package com.massivecraft.massivemarriage.cmd;


import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.entity.Proposal;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;


import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;


import org.bukkit.ChatColor;

import java.util.List;
import java.util.logging.Logger;

import static com.massivecraft.massivecore.mson.Mson.mson;


public class CmdMarriagePropose extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriagePropose()
	{
		// Parameters
		// Grabs args from command
		this.addParameter(TypeMPlayer.get());
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		// Arg
		MPlayer mplayer = this.readArg();
		String mplayerId = IdUtil.getId(mplayer);
		
		String senderId = IdUtil.getId(sender);
		MPlayer sendingPlayer = MPlayer.get(senderId);
		long creationMillis = System.currentTimeMillis();
		
			// Sender sending to themselves?
			if( senderId.equals(mplayerId) )
			{
				throw new MassiveException().addMsg("You cannot marry yourself!"); //.color(ChatColor.RED);
			}
			
			// Sender is already married?
			if ( sendingPlayer.getIsMarried() )
			{
				throw new MassiveException().addMsg("You are already married!"); //.color(ChatColor.RED);
			}
		
			// Player is already married?
			if (mplayer.getIsMarried())
			{
				throw new MassiveException().addMsg(mplayer.getName() + " is already married. Don't be a homewrecker!"); //.color(ChatColor.RED);
			}
			
			// Already proposed?
			boolean hasProposal = sendingPlayer.getPendingProposal();
			if ( ! hasProposal )
			{
				// Event
				EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, hasProposal);
				event.run();
				if (event.isCancelled()) return;
				hasProposal = event.hasNewProposal();
				
				// Mson
				String accept = CmdMarriage.get().cmdMarriageAcceptProposal.getCommandLine(msender.getName());
				String deny = CmdMarriage.get().cmdMarriageDenyProposal.getCommandLine(msender.getName());
				
				Mson mson = Mson.mson(
					mson(sendingPlayer.getName() + " has proposed to you!"),
					mson("<Accept> ").color(ChatColor.GREEN).suggest(accept),
					mson(" <Deny>").color(ChatColor.RED).suggest(deny)
				);
				
				// Inform MPlayer
				mplayer.message(mson);
				
				// Inform Sender
				msg("You have proposed to " + mplayer.getName() + "."); //.color(ChatColor.YELLOW);
				
				// Apply
				Proposal proposal = new Proposal(senderId, mplayerId, creationMillis);
				sendingPlayer.setPendingProposal(true);
				sendingPlayer.setProposedPlayerId(mplayerId);
			}
			
			else
			{
				// Mson
				String command = CmdMarriage.get().cmdMarriageProposeRemove.getCommandLine(mplayer.getName());
				
				Mson remove = mson(
					mson("You already have a pending proposal.").color(ChatColor.YELLOW),
					mson("<Remove>").color(ChatColor.RED).suggest(command)
				);
				
				// Inform
				message(remove);
			}
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriagePropose; }
	
}
