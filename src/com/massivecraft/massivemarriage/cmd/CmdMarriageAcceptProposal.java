package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mixin.MixinMessage;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivemarriage.MassiveMarriage;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageStatusChange;
import com.massivecraft.massivemarriage.event.EventMarriageStatusChange.StatusChangeReason;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.money.Money;

import java.util.List;

public class CmdMarriageAcceptProposal extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageAcceptProposal()
	{
		this.addParameter(TypeMPlayer.get());
		
		this.addRequirements(RequirementHasPerm.get(Perm.ACCEPT));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	@Override
	public void perform() throws MassiveException
	{
		// Args
		MPlayer mplayer = this.readArg(); // Player who sent proposal
		String mplayerId = IdUtil.getId(mplayer);
		
		String senderId = IdUtil.getId(sender); // Player who accepts proposal
		
		// Annoy WumosWared
		if ( senderId.equals(MConf.get().jaredsID)) throw new MassiveException().addMsg("<b>Sorry Jared, give Rusty his credit.");
		
		// Check if same IP address (alt abuse avoid)
		if ( msender.getIp().equals(mplayer.getIp()) && ! MConf.get().allowedIPs.contains(msender.getIp()) ) throw new MassiveException().addMsg("<b>Same IP Address!");
		
		// Check Player has proposal request
		// Does mplayer have pending proposal?
		if ( mplayer.getProposedPlayerId() == null ) throw new MassiveException().addMsg("<b>They did not send you a proposal!");
	
		// Is the proposal request to sender?
		String mpPpId = mplayer.getProposedPlayerId();
		if ( ! mpPpId.equals(senderId)) throw new MassiveException().addMsg("<b>They did not send you a proposal!");
			
		// Check Player is married
		if ( mplayer.hasPartner() ) throw new MassiveException().addMsg("<b>They are already married. Don't be a homewrecker!");
		
		// Check if Sender is married
		if ( msender.hasPartner() ) throw new MassiveException().addMsg("<b>You are already married. You cannot accept a proposal.");
		
		// Marriage Costs Regals
		if ( MConf.get().marriageCostAmount > 0 )
		{
			double marriageCost = MConf.get().marriageCostAmount;
			
			if ( Money.enabled() )
			{
				// Check if mplayer has the money
				if ( ! Money.has(mplayer, marriageCost) )
				{
					double moneyPossessed = Money.get(mplayer);
					double moneyMissing = marriageCost - moneyPossessed;
					this.sendCheckFailMessage(mplayer, "money", marriageCost, moneyPossessed, moneyMissing);
					msender.msg("%s <b>did not have enough money to get married!", MixinDisplayName.get().getDisplayName(mplayer, msender));
					return;
				}
				
				// Check if sender has the money
				if ( ! Money.has(msender, marriageCost) )
				{
					double moneyPossesed = Money.get(msender);
					double moneyMissing = marriageCost - moneyPossesed;
					this.sendCheckFailMessage(msender, "money", marriageCost, moneyPossesed, moneyMissing);
					mplayer.msg("%s <b>did not have enough money to get married!", MixinDisplayName.get().getDisplayName(msender, mplayer));
					return;
				}
				
				// Charge the two players regals
				if (! Money.despawn(mplayer, mplayer, marriageCost, "MassiveMarriage") )
				{
					msender.msg("<b>Failed to remove money from %s", MixinDisplayName.get().getDisplayName(mplayer, msender));
					throw new MassiveException().addMsg("<b>Failed to remove money.");
				}
				
				if (! Money.despawn(msender, msender, marriageCost, "MassiveMarriage") )
				{
					mplayer.msg("<b>Money returned. Failed to remove money from %s.", MixinDisplayName.get().getDisplayName(msender, mplayer));
					Money.spawn(mplayer, mplayer, marriageCost);
					throw new MassiveException().addMsg("<b>Failed to remove money.");
				}
			}
		}
		
		// Event
		EventMarriageStatusChange statusChangeEvent = new EventMarriageStatusChange(sender, msender, mplayer, StatusChangeReason.MARRY);
		statusChangeEvent.run();
		if ( statusChangeEvent.isCancelled() ) return;
		
		// Apply
		mplayer.resetMarriageData();
		mplayer.setPartnerId(senderId);
		
		msender.resetMarriageData();
		msender.setPartnerId(mplayerId);

		
		// Inform
		mplayer.msg("<i>You and %s<i> are now married!", MixinDisplayName.get().getDisplayName(msender, mplayer));
		msender.msg("<i>You and %s<i> are now married!", MixinDisplayName.get().getDisplayName(mplayer, msender));
		
		// Logging
		if ( MConf.get().logMarriage )
		{
			MassiveMarriage.get().log(Txt.parse("%s and %s have gotten married.", mplayer.getName(), msender.getName()));
		}
		
		//TODO: Eventually make this into separate chat channel?
		// Server Broadcasts
		if ( MConf.get().broadcastMarriages )
		{
			// Broadcast a message
			MixinMessage.get().messageAll(Mson.parse("<teal>[MassiveMarriage] <white>%s and %s have gotten married!", msender.getName(), mplayer.getName()));
		}
		
	}
	
	// -------------------------------------------- //
	// UTILITIES
	// -------------------------------------------- //
	
	private void sendCheckFailMessage(MPlayer mplayer, String resourceName, Object required, Object possessed, Object missing)
	{
		String notEnough = "<b>Not enough <h>%s<b>.";
		String requirePossessMissing ="<k>Required: <v>%s <k>Possessed: <v>%s <k>Missing: <v>%s";
		
		mplayer.msg(notEnough, resourceName);
		mplayer.msg(requirePossessMissing, required, possessed, missing);
	}
	
	// -------------------------------------------- //
	// COMMAND ALIASES
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases() { return MConf.get().aliasesMarriageAcceptProposal; }
}
