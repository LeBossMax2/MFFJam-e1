package fr.romax.medievalcom.client.handlers;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.inventory.ContainerDesk;
import fr.romax.medievalcom.common.inventory.ContainerVillagerMessenger;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = MedievalCommunications.MODID, value = Side.CLIENT)
public class TextureHandler
{
	
	@SubscribeEvent
	public static void onTextureLoaded(TextureStitchEvent.Pre event)
	{
		event.getMap().registerSprite(ContainerDesk.EMPTY_SLOT_ICON);
		event.getMap().registerSprite(ContainerVillagerMessenger.EMPTY_SLOT_EMERALD_ICON);
	}
	
}
