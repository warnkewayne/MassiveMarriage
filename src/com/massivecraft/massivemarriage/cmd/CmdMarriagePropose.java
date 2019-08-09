package com.massivecraft.massivemarriage.cmd;


import com.massivecraft.massivecore.Button;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageProposalChange;


import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.mson.Mson;


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
		String mplayerId = mplayer.getId();
		String senderId = msender.getId();
		
		// Annoy WumosWared
		if ( senderId.equals(MConf.get().jaredsID)) throw new MassiveException().addMsg("<b>Sorry Jared, give Rusty his credit.");
		
		// Check if same IP address (alt abuse avoid)
		if ( msender.getIp().equals(mplayer.getIp()) && ! MConf.get().allowedIPs.contains(msender.getIp()) ) throw new MassiveException().addMsg("<b>Same IP Address!");
		
		// Sender is already married?
		if ( msender.hasPartner() ) { throw new MassiveException().addMsg("<b>You are already married!"); }
		
		// Player is already married?
		if ( mplayer.hasPartner() )
		{
			throw new MassiveException().addMsg(
				MixinDisplayName.get().getDisplayName(mplayer, msender)
					+ " <b>is already married. Don't be a homewrecker!"
			);
		}
			
		// Check if player isn't ignoring the sender
		if ( ! mplayer.isAcknowledging(sender) ) throw new MassiveException().addMsg("<b>Sorry, you cannot propose to this player.");
			
		// Check if player is online
		if ( mplayer.isOffline() ) throw new MassiveException().addMsg("<b>Sorry, you cannot propose to an offline player.");
		
		// Sender sending to themselves?
		if( senderId.equals(mplayerId) ) { throw new MassiveException().addMsg("<b>You cannot marry yourself!"); }
			
			
		// Already proposed?
		if(msender.getProposedPlayerId() != null)
		{
			Button btnRemove = new Button().setName("Remove").setSender(sender).setCommand(CmdMarriage.get().cmdMarriageProposeRemove);

			// Inform
			msender.message(Mson.parse("<i>You already have a pending proposal.").add(btnRemove.render()));

			return;
		}

		// Event
		EventMarriageProposalChange event = new EventMarriageProposalChange(sender, mplayer, false);
		event.run();

		if (event.isCancelled()) return;
				
		// Store
		mplayer.addToSuitors(senderId); msender.setProposedPlayerId(mplayerId);

		Button btnAccept = new Button().setName("Accept").setSender(mplayer.getSender()).setCommand(CmdMarriage.get().cmdMarriageAcceptProposal).setArgs(msender.getName()).setPaddingRight(true);
		Button btnDeny = new Button().setName("Deny").setSender(mplayer.getSender()).setCommand(CmdMarriage.get().cmdMarriageDenyProposal).setArgs(msender.getName());

		// Inform MPlayer
		mplayer.message(Mson.parse("%s <i>has proposed to You! ", MixinDisplayName.get().getDisplayName(msender, mplayer)).add(btnAccept.render()).add(btnDeny.render()));

		// Inform Sender
		msender.msg("<i>You have proposed to %s", MixinDisplayName.get().getDisplayName(mplayer, msender));

	}
	
	@Override
	public List<String> getAliases() { return MConf.get().aliasesMarriagePropose; }
	
}
