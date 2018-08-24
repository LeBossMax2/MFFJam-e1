package fr.romax.mffjam.common;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.blocks.ModBlocks;
import fr.romax.mffjam.common.entities.ModEntities;
import fr.romax.mffjam.common.items.ModItems;
import fr.romax.mffjam.common.world.gen.structure.StructureHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event){
		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ModEntities.registerEntities();
		ModNetwork.registerPackets();
		StructureHandler.registerStructures();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(MFFJam.instance, new GuiHandler());
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event) {}

}
