package fr.romax.mffjam.common.entities;

import fr.romax.mffjam.MFFJam;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = MFFJam.MODID)
public class ModEntities
{
	
	public static void registerEntities()
	{
		EntityRegistry.registerModEntity(new ResourceLocation(MFFJam.MODID, "mesage_arrow"), EntityMessageArrow.class, "mesage_arrow", 0, MFFJam.instance, 64, 20, false);
	}
	
	@SideOnly(Side.CLIENT)
	public static void registerRenders()
	{
		//RenderingRegistry.registerEntityRenderingHandler(entityClass, renderFactory);
	}
	
}
