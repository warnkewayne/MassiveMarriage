package com.massivecraft.massivemarriage.cmd;

import com.massivecraft.massivecore.command.requirement.RequirementHasPerm;
import com.massivecraft.massivemarriage.Perm;
import com.massivecraft.massivemarriage.entity.MConf;
import com.massivecraft.massivecore.MassiveException;
import com.massivecraft.massivemarriage.entity.MPlayer;
import org.bukkit.ChatColor;

import java.util.List;

public class CmdMarriageShow extends MarriageCommand
{
	// -------------------------------------------- //
	// CONSTRUCT
	// -------------------------------------------- //
	
	public CmdMarriageShow()
	{
		// No parameters
		
		this.addRequirements(RequirementHasPerm.get(Perm.SHOW));
	}
	
	// -------------------------------------------- //
	// OVERRIDE
	// -------------------------------------------- //
	
	@Override
	public void perform() throws MassiveException
	{
		if( msender.getPartnerId() == null ) throw new MassiveException().addMsg("<b>You are not married!"); //.color(ChatColor.RED);
		
		String partnerId = msender.getPartnerId();
		MPlayer partner = MPlayer.get(partnerId);
		
		msg("\247eYou are currently married to <i>\247d" + partner.getName());
		
	}
	
	@Override
	public List<String> getAliases() { return MConf.get().getAliasesMarriageShow; }
}
