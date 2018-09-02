package fr.romax.medievalcom.common.entities;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.client.renderers.entity.RenderHangingDaggerMessage;
import fr.romax.medievalcom.client.renderers.entity.RenderHangingMessage;
import fr.romax.medievalcom.client.renderers.entity.RenderMessageArrow;
import fr.romax.medievalcom.client.renderers.entity.RenderVillagerMessager;
import net.minecraft.entity.Entity;
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
		registerEntity(EntityMessageArrow.class, "message_arrow", 64, 20, false);
		registerEntity(EntityHangingMessage.class, "hanging_message", 160, Integer.MAX_VALUE, false);
		registerEntity(EntityHangingDaggerMessage.class, "hanging_dagger_message", 160, Integer.MAX_VALUE, false);
		registerEntity(EntityVillagerMessenger.class, "villager_messenger", 80, 3, true, 0x111122, 0xBD8B72);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		RenderingRegistry.registerEntityRenderingHandler(EntityMessageArrow.class, RenderMessageArrow::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHangingMessage.class, RenderHangingMessage<EntityHangingMessage>::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityHangingDaggerMessage.class, RenderHangingDaggerMessage::new);
		RenderingRegistry.registerEntityRenderingHandler(EntityVillagerMessenger.class, RenderVillagerMessager::new);
	}
	
	private static int nextId = 0;
	
	protected static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(MedievalCommunications.MODID, entityName), entityClass, entityName, nextId++, MedievalCommunications.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	protected static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary)
	{
		EntityRegistry.registerModEntity(new ResourceLocation(MedievalCommunications.MODID, entityName), entityClass, entityName, nextId++, MedievalCommunications.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
	}
	
}
