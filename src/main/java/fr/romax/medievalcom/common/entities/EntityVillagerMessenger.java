package fr.romax.medievalcom.common.entities;

import java.util.UUID;

import fr.romax.medievalcom.common.GuiHandler;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.INpc;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class EntityVillagerMessenger extends EntityAgeable implements INpc
{
	protected UUID targetId;
	protected ItemStack message;
	
	public EntityVillagerMessenger(World worldIn)
	{
		super(worldIn);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
	}

	@Override
	public EntityAgeable createChild(EntityAgeable ageable) {
		return new EntityVillagerMessenger(ageable.world);
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

	public void sendMessage(EntityPlayer addresse, ItemStack message)
	{
		this.targetId = addresse.getUniqueID();
		this.message = message;
	}
	
}
