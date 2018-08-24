package fr.romax.medievalcom.common.entities;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = MedievalCommunications.MODID)
public class EntityHandler
{
	private static final float
		PI = (float)Math.PI,
		TWO_PI = PI * 2.0F,
		PI_QUARTER = PI / 4.0F;
	
	@SubscribeEvent
	public static void getCriticalModifier(CriticalHitEvent event)
	{
		EntityPlayer attacker = event.getEntityPlayer();
		ItemStack weapon = attacker.getHeldItemMainhand();
		
		if (weapon.getItem() == ModItems.DAGGER)
		{
			float difYaw = Math.abs((attacker.rotationYawHead - event.getTarget().rotationYaw) % TWO_PI);
			if (difYaw > PI) difYaw = TWO_PI - difYaw;
			
			if (difYaw < PI_QUARTER) // If back stab
			{
				event.setDamageModifier(event.getOldDamageModifier() + 1.0F);
				event.setResult(Result.ALLOW);
			}
		}
	}
}
