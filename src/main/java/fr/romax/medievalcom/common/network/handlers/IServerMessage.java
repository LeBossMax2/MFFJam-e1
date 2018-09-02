package fr.romax.medievalcom.common.network.handlers;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

// Client -> Server
public interface IServerMessage extends IMessage
{
	void onServerReceive(EntityPlayerMP sender);
	
	default boolean doesServerSynchronize()
	{
		return true;
	}
}
