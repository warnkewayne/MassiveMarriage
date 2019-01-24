package com.massivecraft.massivemarriage.cmd.type;

import com.massivecraft.massivecore.command.type.Type;
import com.massivecraft.massivemarriage.entity.MPlayer;
import com.massivecraft.massivemarriage.entity.MPlayerColl;

public class TypeMPlayer
{
	// -------------------------------------------- //
	// INSTANCE
	// -------------------------------------------- //
	
	public static Type<MPlayer> get() { return MPlayerColl.get().getTypeEntity(); }
	
	
}
