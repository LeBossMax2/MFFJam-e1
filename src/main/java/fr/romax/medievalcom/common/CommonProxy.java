package fr.romax.medievalcom.common;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.blocks.ModBlocks;
import fr.romax.medievalcom.common.entities.EntityMessageArrow;
import fr.romax.medievalcom.common.entities.ModEntities;
import fr.romax.medievalcom.common.items.ModItems;
import fr.romax.medievalcom.common.world.gen.structure.StructureHandler;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent event)
	{
		ModBlocks.registerTiles();
		ModEntities.registerEntities();
		ModNetwork.registerPackets();
		StructureHandler.registerStructures();
		
		NetworkRegistry.INSTANCE.registerGuiHandler(MedievalCommunications.instance, new GuiHandler());
	}
	
	public void init(FMLInitializationEvent event)
	{
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(ModItems.MESSAGE_ARROW, new BehaviorProjectileDispense()
		{
			@Override
			protected IProjectile getProjectileEntity(World world, IPosition position, ItemStack stack)
			{
				EntityMessageArrow entitytippedarrow = new EntityMessageArrow(world, position.getX(), position.getY(), position.getZ(), stack.getTagCompound());
                entitytippedarrow.pickupStatus = EntityArrow.PickupStatus.ALLOWED;
                return entitytippedarrow;
			}
		});
	}
	
	public void postInit(FMLPostInitializationEvent event) {}

}
