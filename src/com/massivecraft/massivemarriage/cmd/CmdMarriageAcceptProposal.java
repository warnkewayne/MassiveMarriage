package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivemarriage.MassiveMarriage;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.event.EventMarriageStatusChange;
import com.massivecraft.massivemarriage.event.EventMarriageStatusChange.StatusChangeReason;

import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivecore.util.Txt;
import com.massivecraft.massivecore.money.Money;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.List;

public class CmdMarriageAcceptProposal extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageAcceptProposal()
	{
		this.addParameter(TypeMPlayer.get());
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
		
		// Check Player has proposal request
		// Does mplayer have pending proposal?
		if ( ! mplayer.getPendingProposal() ) throw new MassiveException().addMsg("<b>They did not send you a proposal!"); //.color(ChatColor.RED);
	
		// Is the proposal request to sender?
		String mpPpId = mplayer.getProposedPlayerId();
		if ( ! mpPpId.equals(senderId)) throw new MassiveException().addMsg("<b>They did not send you a proposal!"); //.color(ChatColor.RED);
			
		// Check Player is married
		if ( mplayer.getIsMarried() ) throw new MassiveException().addMsg("<b>They are already married. Don't be a homewrecker!"); //.color(ChatColor.RED);
		
		// Check if Sender is married
		if (msender.getIsMarried() ) throw new MassiveException().addMsg("<b>You are already married. You cannot accept a proposal."); //.color(ChatColor.RED);
		
		// Marriage Costs Regals
		if ( MConf.get().marriageCostRegals )
		{
			double marriageCost = MConf.get().marriageCostAmount;
			
			if ( Money.enabled() )
			{
				// Check if mplayer has the money
				if ( ! Money.has(mplayer, marriageCost) )
				{
					double moneyPossesed = Money.get(mplayer);
					double moneyMissing = marriageCost - moneyPossesed;
					this.sendCheckFailMessage(mplayer, "money", marriageCost, moneyPossesed, moneyMissing);
					return;
				}
				
				// Check if sender has the money
				if ( ! Money.has(msender, marriageCost) )
				{
					double moneyPossesed = Money.get(msender);
					double moneyMissing = marriageCost - moneyPossesed;
					this.sendCheckFailMessage(msender, "money", marriageCost, moneyPossesed, moneyMissing);
					return;
				}
				
				// Charge the two players regals
				if (! Money.despawn(mplayer, mplayer, marriageCost, "MassiveMarriage") )
				{
					throw new MassiveException().addMsg("<b>Failed to remove money."); //.color(ChatColor.RED);
				}
				
				if (! Money.despawn(msender, msender, marriageCost, "MassiveMarriage") )
				{
					throw new MassiveException().addMsg("<b>Failed to remove money."); //.color(ChatColor.RED);
				}
			}
		}
		
		// Event
		EventMarriageStatusChange statusChangeEvent = new EventMarriageStatusChange(sender, msender, mplayer, StatusChangeReason.MARRY);
		statusChangeEvent.run();
		if ( statusChangeEvent.isCancelled() ) return;
		
		// Apply
		mplayer.setPartnerId(senderId);
		mplayer.setIsMarried(true);
		mplayer.setPendingProposal(false);
		mplayer.setProposedPlayerId(null);
		
		msender.setPartnerId(mplayerId);
		msender.setIsMarried(true);
		msender.setPendingProposal(false);
		msender.setProposedPlayerId(null);

		// Inform
		mplayer.msg("You and <i>%s<i> are now married!", msender.getName());
		msender.msg("You and <i>%s<i> are now married!", mplayer.getName());
		
		// Logging
		if ( MConf.get().logMarriage )
		{
			MassiveMarriage.get().log(Txt.parse("%s and %s have gotten married.", mplayer.getName(), msender.getName()));
		}
		
		// Server Broadcasts
		if ( MConf.get().broadcastMarriages )
		{
			// Broadcast a message
			Bukkit.broadcastMessage(
				ChatColor.DARK_AQUA + "[MassiveMarriage] " +
					ChatColor.WHITE + msender.getName() +
					" and " + mplayer.getName() + " have gotten married!"
			);
		}
		
	}
	
	
	// -------------------------------------------- //
	// CHECK UTILITIES
	// -------------------------------------------- //
	
	public void sendCheckFailMessage(MPlayer mplayer, String resourceName, Object required, Object possessed, Object missing)
	{
		String notEnoughMessage = String.format("<b>Not enough <h>%s<b>.", resourceName);
		mplayer.message(notEnoughMessage);
		String reportMessage = String.format("<k>Required: <v>%s <k>Possessed: <v>%s <k>Missing: <v>%s", required, possessed, missing);
		mplayer.message(reportMessage);
	}
	
	
	// -------------------------------------------- //
	// COMMAND ALIASES
	// -------------------------------------------- //
	
	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriageAcceptProposal; }
}
