package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.collections.MassiveSet;
import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
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
		this.addParameter(TypeMPlayer.get());
		
		this.addRequirements(RequirementHasPerm.get(Perm.SHOW));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		MPlayer mPlayer = this.readArg(); // Player who
		String mPlayerId = IdUtil.getId(mPlayer);
		
		if( mPlayerId.equals(msender.getId()) )
		{
			if( ! msender.hasPartner() )
			{
				msg("<i>You are single and ready to mingle!");
				
				if ( msender.hasSuitors() )
				{
					// Print suitors
					msg("<gold>Proposed to You: <white>%s", msender.getSuitors());
				}
				if ( msender.getProposedPlayerId() != null )
				{
					// Print proposed player id
					msg("<gold>Proposals Pending: <white>%s", msender.getProposedPlayerId());
				}
				return;
			}
			
			String partnerId = msender.getPartnerId();
			MPlayer partner = MPlayer.get(partnerId);
			
			msg("<i>You are currently married to <aqua>" + partner.getName());
			return;
		}
		
		if( ! Perm.SHOW_OTHER.has(sender, true) ) return;
		
		if( ! mPlayer.hasPartner() ) throw new MassiveException().addMsg("%s <i>is single and ready to mingle!", mPlayer.getName());
		
		String partnerId = mPlayer.getPartnerId();
		MPlayer partner = MPlayer.get(partnerId);
		
		msg(mPlayer.getName() + "<i> and <white>%s <i>are married.", partner.getName());
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriageShow; }
}
