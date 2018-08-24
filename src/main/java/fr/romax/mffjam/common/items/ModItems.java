package fr.romax.mffjam.common.items;

import fr.romax.mffjam.common.entities.EntityMessageArrow;
import fr.romax.mffjam.common.registry.ItemsRegistry;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorProjectileDispense;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ModItems {

	public static ItemDagger dagger = new ItemDagger();
	public static ItemWrittenPaper written_paper = new ItemWrittenPaper();
	public static ItemMessageArrow message_arrow = new ItemMessageArrow();
	public static ItemSlimyPaper slimy_paper = new ItemSlimyPaper();

	
	public static void registerItems() {
		// A utiliser : ItemsRegistry.register(block, name);
		ItemsRegistry.register(dagger, "dagger");
		ItemsRegistry.register(written_paper, "written_paper");
		ItemsRegistry.register(message_arrow, "message_arrow");
		ItemsRegistry.register(slimy_paper, "slimy_paper");
		
		BlockDispenser.DISPENSE_BEHAVIOR_REGISTRY.putObject(message_arrow, new BehaviorProjectileDispense()
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
	
}
