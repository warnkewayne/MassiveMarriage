package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivecore.mixin.MixinDisplayName;
import com.massivecraft.massivecore.mson.Mson;
import com.massivecraft.massivecore.util.IdUtil;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.cmd.type.TypeMPlayer;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivemarriage.entity.MPlayer;

import java.util.List;

public class CmdMarriageShow extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageShow()
	{
		this.addParameter(TypeMPlayer.get(), "player", "you");
		
		this.addRequirements(RequirementHasPerm.get(Perm.SHOW));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		MPlayer mPlayer = this.readArg(msender); // Player who
		String mPlayerId = IdUtil.getId(mPlayer);
		
		// Annoy WumosWared
		if ( IdUtil.getId(sender).equals(MConf.get().jaredsID)) throw new MassiveException().addMsg("<b>Sorry Jared, give Rusty his credit.");
		
		if( mPlayerId.equals(msender.getId()) )
		{
			msg("<gold>_________.[<teal>MassiveMarriage<gold>].__________");
			
			if( ! msender.hasPartner() )
			{
				msender.msg("<i>You are single and ready to mingle!");
				
				if ( msender.hasSuitors() )
				{
					MassiveSet<String> suitors = msender.getSuitors();
					// Print suitors
					Mson prefix = Mson.parse("<gold>Proposals to You: ");
					Mson list = Mson.mson();
					
					for(String suitorId: suitors)
					{
						String dummy = MixinDisplayName.get().getDisplayName(MPlayer.get(suitorId), msender) + " ";
						
						list = Mson.mson(list).add(dummy)
								   .suggest(CmdMarriage.get().cmdMarriageAcceptProposal.getCommandLine(MPlayer.get(suitorId).getName()));
						
					}
					Mson complete = Mson.mson(prefix).add(list);
					
					message(complete);
					
				}
				if ( msender.getProposedPlayerId() != null )
				{
					String unpropose = CmdMarriage.get().cmdMarriageProposeRemove.getCommandLine();
					
					// Print proposed player
					message(Mson.parse("<gold> Proposals Pending: %s", MixinDisplayName.get().getDisplayName(MPlayer.get(msender.getProposedPlayerId()), msender)).suggest(unpropose));
				}
				return;
			}
			
			String partnerId = msender.getPartnerId();
			MPlayer partner = MPlayer.get(partnerId);
			
			msender.msg("<i>You are currently married to %s.", MixinDisplayName.get().getDisplayName(partner, msender));
			return;
		}
		
		// Player does not have permission to see other marriage info
		if( ! Perm.SHOW_OTHER.has(sender, true) ) return;
		
		msg("<gold>_________.[<teal>MassiveMarriage<gold>].__________");
		
		if( ! mPlayer.hasPartner() ) throw new MassiveException().addMsg("%s <i>is single and ready to mingle!", MixinDisplayName.get().getDisplayName(mPlayer, msender));
		
		String partnerId = mPlayer.getPartnerId();
		MPlayer partner = MPlayer.get(partnerId);
		
		msg(MixinDisplayName.get().getDisplayName(mPlayer, msender) + "<i> and <white>%s <i>are married.", MixinDisplayName.get().getDisplayName(partner, msender));
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().aliasesMarriageShow; }
}
