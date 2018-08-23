package fr.romax.mffjam.common.world.gen.structure;

import fr.romax.mffjam.MFFJam;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.fml.common.registry.VillagerRegistry;

public class StructureHandler
{
	
	public static void registerStructures()
	{
		VillagerRegistry.instance().registerVillageCreationHandler(SpyVillageHouse.CreationHandler.INSTANCE);

		MapGenStructureIO.registerStructureComponent(SpyVillageHouse.class, MFFJam.MODID + ":spy_house");
	}
	
}
