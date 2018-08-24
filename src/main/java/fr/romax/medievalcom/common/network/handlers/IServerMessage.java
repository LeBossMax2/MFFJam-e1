package fr.romax.medievalcom.common.network.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

// Client -> Server
public interface IServerMessage extends IMessage
{
	void onServerReceive(EntityPlayer sender);
	
	default boolean doesServerSynchronize()
	{
		return true;
	}
}
