package fr.romax.mffjam.common.network;

import fr.romax.mffjam.common.inventory.ContainerDesk;
import fr.romax.mffjam.common.network.handlers.IServerMessage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class MessageWritePage implements IServerMessage
{
	private int windowId;
	private String pageContent;
	private String title;
	private String author;
	
	public MessageWritePage()
	{ }
	
	public MessageWritePage(int windowId, String pageContent, String title, String author)
	{
		this.windowId = windowId;
		this.pageContent = pageContent;
		this.title = title;
		this.author = author;
	}

	@Override
	public void toBytes(ByteBuf buf)
	{
		buf.writeInt(this.windowId);
		ByteBufUtils.writeUTF8String(buf, this.pageContent);
		ByteBufUtils.writeUTF8String(buf, this.title);
		ByteBufUtils.writeUTF8String(buf, this.author);
	}
	
	@Override
	public void fromBytes(ByteBuf buf)
	{
		this.windowId = buf.readInt();
		this.pageContent = ByteBufUtils.readUTF8String(buf);
		this.title = ByteBufUtils.readUTF8String(buf);
		this.author = ByteBufUtils.readUTF8String(buf);
	}

	@Override
	public void onServerReceive(EntityPlayer sender)
	{
		if (sender.openContainer.windowId == this.windowId && sender.openContainer instanceof ContainerDesk)
		{
			ContainerDesk desk = (ContainerDesk)sender.openContainer;
			
			desk.writePage(sender, this.pageContent, this.title, this.author);
		}
	}
	
}
