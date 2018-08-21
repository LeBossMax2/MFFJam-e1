package fr.romax.mffjam.common.network.handlers;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

// Server -> Client
public interface IClientMessage extends IMessage
{
	@SideOnly(Side.CLIENT)
	void onClientReceive();

	@SideOnly(Side.CLIENT)
	default boolean doesClientSynchronize()
	{
		return true;
	}
}
