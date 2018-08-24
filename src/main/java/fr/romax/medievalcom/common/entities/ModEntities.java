package fr.romax.medievalcom.common.entities;

import java.awt.Color;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.client.renderers.entity.RenderHangingDaggerMessage;
import fr.romax.medievalcom.client.renderers.entity.RenderHangingMessage;
import fr.romax.medievalcom.client.renderers.entity.RenderMessageArrow;
import fr.romax.medievalcom.client.renderers.entity.RenderVillagerMessager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = MedievalCommunications.MODID)
public class ModEntities
{
	
	public static void registerEntities()
	{
		EntityRegistry.registerModEntity(new ResourceLocation(MedievalCommunications.MODID, "message_arrow"), EntityMessageArrow.class, "message_arrow", 0, MedievalCommunications.instance, 64, 20, false);
		EntityRegistry.registerModEntity(new ResourceLocation(MedievalCommunications.MODID, "hanging_message"), EntityHangingMessage.class, "hanging_message", 1, MedievalCommunications.instance, 160, Integer.MAX_VALUE, false);
		EntityRegistry.registerModEntity(new ResourceLocation(MedievalCommunications.MODID, "hanging_dagger_message"), EntityHangingDaggerMessage.class, "hanging_dagger_message", 2, MedievalCommunications.instance, 160, Integer.MAX_VALUE, false);
		EntityRegistry.registerModEntity(new ResourceLocation(MedievalCommunications.MODID, "villager_messager"), EntityVillagerMessager.class, "villager_messager", 3, MedievalCommunications.instance, 60, 20, false, Color.black.getRGB(), Color.white.getRGB());
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMessageArrow.class, RenderMessageArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHangingMessage.class, RenderHangingMessage<EntityHangingMessage>::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHangingDaggerMessage.class, RenderHangingDaggerMessage::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVillagerMessager.class, RenderVillagerMessager::new);
	}
	
}
