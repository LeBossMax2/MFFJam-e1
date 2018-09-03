package fr.romax.medievalcom.common;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.network.MessageSendMessenger;
import fr.romax.medievalcom.common.network.MessageWritePage;
import fr.romax.medievalcom.common.network.handlers.ClientMessageHandler;
import fr.romax.medievalcom.common.network.handlers.IClientMessage;
import fr.romax.medievalcom.common.network.handlers.IServerMessage;
import fr.romax.medievalcom.common.network.handlers.ServerMessageHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class ModNetwork
{
	
	public static final SimpleNetworkWrapper MOD_CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(MedievalCommunications.REDUCED_NAME);
	
	private static final ServerMessageHandler SERVER_MESSAGE_HANDLER = new ServerMessageHandler();
	private static final ClientMessageHandler CLIENT_MESSAGE_HANDLER = new ClientMessageHandler();
	
	public static void registerPackets()
	{
		next();
		registerServer(MessageWritePage.class);
		next();
		registerServer(MessageSendMessenger.class);
	}
	
	private static int lastIndex = 0;
	
	private static void next()
	{
		lastIndex++;
	}
	
	private static void registerServer(Class<? extends IServerMessage> messageClass)
	{
		MOD_CHANNEL.registerMessage(SERVER_MESSAGE_HANDLER, messageClass, lastIndex, Side.SERVER);
	}
	
	private static void registerClient(Class<? extends IClientMessage> messageClass)
	{
		MOD_CHANNEL.registerMessage(CLIENT_MESSAGE_HANDLER, messageClass, lastIndex, Side.CLIENT);
	}
	
}
