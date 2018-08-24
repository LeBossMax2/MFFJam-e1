package fr.romax.medievalcom.common.entities;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.GuiHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityVillagerMessager extends EntityAgeable implements INpc {

	public EntityVillagerMessager(World worldIn) {
		super(worldIn);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityVillagerMessager(ageable.world);
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag)
        {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isChild() && !player.isSneaking())
        {
            if (!this.world.isRemote)
            {
            	GuiHandler.openEntityGui(this.world, player, this);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
	}
	
}
