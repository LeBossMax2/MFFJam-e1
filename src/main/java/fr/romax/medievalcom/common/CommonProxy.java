package fr.romax.medievalcom.common;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.blocks.ModBlocks;
import fr.romax.medievalcom.common.entities.ModEntities;
import fr.romax.medievalcom.common.items.ModItems;
import fr.romax.medievalcom.common.world.gen.structure.StructureHandler;
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
		
		NetworkRegistry.INSTANCE.registerGuiHandler(MedievalCommunications.instance, new GuiHandler());
	}
	
	public void init(FMLInitializationEvent event) {}
	
	public void postInit(FMLPostInitializationEvent event) {}

}
