package fr.romax.mffjam.client.handlers;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.inventory.ContainerDesk;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
@EventBusSubscriber(modid = MFFJam.MODID, value = Side.CLIENT)
public class TextureHandler
{
	
	@SubscribeEvent
	public static void onTextureLoaded(TextureStitchEvent.Pre event)
	{
		event.getMap().registerSprite(ContainerDesk.EMPTY_SLOT_ICON);
	}
	
}
