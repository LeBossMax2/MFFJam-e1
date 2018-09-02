package fr.romax.medievalcom.common.entities.ai;

import fr.romax.medievalcom.common.entities.EntityVillagerMessenger;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIMoveToMessageTarget extends EntityAIBase
{
    private final EntityVillagerMessenger villager;
    private final double movementSpeed;
    
    private int delayCounter;

	public EntityAIMoveToMessageTarget(EntityVillagerMessenger villager, double speedIn)
	{
        this.villager = villager;
        this.movementSpeed = speedIn;
        this.setMutexBits(1);
	}

	@Override
	public boolean shouldExecute()
	{
		return this.villager.hasMessage() && this.villager.hasMessageTarget();
	}
	
	@Override
	public void startExecuting()
	{
		this.delayCounter = 0;
	}
	
	@Override
	public void updateTask()
	{
		--this.delayCounter;
		if (this.delayCounter <= 0)
		{
            this.delayCounter = 4 + this.villager.getRNG().nextInt(7);
            
            EntityPlayer target = this.villager.getMessageTarget();
            double dist = this.villager.getDistanceSq(target);
            
            if (dist > 1024.0D)
            {
                this.delayCounter += 10;
            }
            else if (dist > 256.0D)
            {
                this.delayCounter += 5;
            }
            
			this.villager.getNavigator().tryMoveToEntityLiving(target, this.movementSpeed);
		}
	}
	
}
