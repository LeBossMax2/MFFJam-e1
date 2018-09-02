package fr.romax.medievalcom.common.network;

import fr.romax.medievalcom.common.inventory.ContainerVillagerMessenger;
import fr.romax.medievalcom.common.network.handlers.IServerMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageSendMessenger implements IServerMessage
{
	private int windowId;
	private String addressee;

	public MessageSendMessenger()
	{ }
	
	public MessageSendMessenger(int windowId, String addressee)
	{
		this.windowId = windowId;
		this.addressee = addressee;
	}
	
	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.windowId);
		ByteBufUtils.writeUTF8String(buf, this.addressee);
	}

	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = buf.readInt();
		this.addressee = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void onServerReceive(EntityPlayerMP sender)
	{
		if (sender.openContainer.windowId == this.windowId && sender.openContainer instanceof ContainerVillagerMessenger)
		{
			ContainerVillagerMessenger desk = (ContainerVillagerMessenger)sender.openContainer;
			
			desk.sendMessage(sender, this.addressee);
		}
	}
	
}
