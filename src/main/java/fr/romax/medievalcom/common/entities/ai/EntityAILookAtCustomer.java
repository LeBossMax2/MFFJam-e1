package fr.romax.medievalcom.common.entities.ai;

import fr.romax.medievalcom.common.entities.EntityVillagerMessenger;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAILookAtCustomer extends EntityAIWatchClosest
{
    private final EntityVillagerMessenger villager;

    public EntityAILookAtCustomer(EntityVillagerMessenger villagerIn)
    {
        super(villagerIn, EntityPlayer.class, 8.0F);
        this.villager = villagerIn;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        if (this.villager.isTrading())
        {
            this.closestEntity = this.villager.getCustomer();
            return true;
        }
        else
        {
            return false;
        }
    }
}