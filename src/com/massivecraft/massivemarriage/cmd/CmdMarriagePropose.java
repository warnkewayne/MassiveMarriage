package com.massivecraft.massivemarriage.cmd;


import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.entity.Proposal;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;


import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;


import org.bukkit.ChatColor;

import java.util.List;

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
		
		this.addRequirements(RequirementHasPerm.get(Perm.PROPOSE));
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
		
			// Check if player isn't ignoring the sender
			if ( ! mplayer.isAcknowledging(sender) ) throw new MassiveException().addMsg("<b>Sorry, you cannot propose to this player.");
			
			// Check if player is online
			if ( mplayer.isOffline() ) throw new MassiveException().addMsg("<b>Sorry, the player must be online to be proposed to.");
		
			// Sender sending to themselves?
			if( senderId.equals(mplayerId) )
			{
				throw new MassiveException().addMsg("<b>You cannot marry yourself!");
			}
			
			// Sender is already married?
			if ( sendingPlayer.hasPartner() )
			{
				throw new MassiveException().addMsg("<b>You are already married!");
			}
		
			// Player is already married?
			if ( mplayer.hasPartner() )
			{
				throw new MassiveException().addMsg(mplayer.getName() + " <b>is already married. Don't be a homewrecker!");
			}
			
			// Already proposed?
			if ( mplayer.getProposedPlayerId() == null )
			{
				// Event
				EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, false);
				event.run();
				if (event.isCancelled()) return;
				
				// Mson
				String accept = CmdMarriage.get().cmdMarriageAcceptProposal.getCommandLine(msender.getName());
				String deny = CmdMarriage.get().cmdMarriageDenyProposal.getCommandLine(msender.getName());
				
				Mson mson = Mson.mson(
					mson(sendingPlayer.getName() + " has proposed to you!").color(ChatColor.YELLOW),
					mson(" <Accept> ").color(ChatColor.GREEN).suggest(accept),
					mson(" <Deny>").color(ChatColor.RED).suggest(deny)
				);
				
				// Inform MPlayer
				mplayer.message(mson);
				
				// Inform Sender
				msg("<i>You have proposed to <white>" + mplayer.getName() + ".");
				
				// Apply
				Proposal proposal = new Proposal(senderId, mplayerId, creationMillis);
				sendingPlayer.setProposedPlayerId(mplayerId);
			}
			
			else
			{
				// Mson
				String command = CmdMarriage.get().cmdMarriageProposeRemove.getCommandLine(mplayer.getName());
				
				Mson remove = mson(
					mson("You already have a pending proposal.").color(ChatColor.YELLOW),
					mson(" <Remove>").color(ChatColor.RED).suggest(command)
				);
				
				// Inform
				message(remove);
			}
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriagePropose; }
	
}
