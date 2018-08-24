package fr.romax.medievalcom.common.world.gen.structure;

import fr.romax.medievalcom.MedievalCommunications;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class StructureHandler
{
	
	public static void registerStructures()
	{
		VillagerRegistry.instance().registerVillageCreationHandler(SpyVillageHouse.CreationHandler.INSTANCE);

		MapGenStructureIO.registerStructureComponent(SpyVillageHouse.class, MedievalCommunications.MODID + ":spy_house");
	}
	
}
