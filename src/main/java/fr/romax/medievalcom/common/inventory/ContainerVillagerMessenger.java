package fr.romax.medievalcom.common.inventory;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.entities.EntityVillagerMessager;
import fr.romax.medievalcom.common.items.ItemWrittenPaper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerVillagerMessenger extends Container
{
	public static final ResourceLocation EMPTY_SLOT_EMERALD_ICON = new ResourceLocation(MedievalCommunications.MODID, "items/empty_slot_emerald");
	
	private final EntityVillagerMessager entity;
	
	public ContainerVillagerMessenger(EntityVillagerMessager entity, InventoryPlayer playerInv)
	{
		this.entity = entity;
		
		IItemHandler villagerInv = new ItemStackHandler(2)
		{
			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				return slot == 0 ? stack.getItem() instanceof ItemWrittenPaper : stack.getItem() == Items.EMERALD;
			}
		};
		
		//Villager inventory
		Slot pageSlot = new SlotItemHandler(villagerInv, 0, 36, 53);
		pageSlot.setBackgroundName(ContainerDesk.EMPTY_SLOT_ICON.toString());
		this.addSlotToContainer(pageSlot);
		
		Slot emeraldSlot = new SlotItemHandler(villagerInv, 1, 62, 53);
		emeraldSlot.setBackgroundName(EMPTY_SLOT_EMERALD_ICON.toString());
		this.addSlotToContainer(emeraldSlot);
		
		//Player inventory
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
			}
		}
		
		//Player hotbar
		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(playerInv, i, 8 + i * 18, 142));
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.entity.isEntityAlive() && player.getDistanceSq(this.entity) <= 64.0D;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack currentStack = slot.getStack();
			ItemStack returnStack = currentStack.copy();
			
			if (index  < 2)
			{
				if (!this.mergeItemStack(currentStack, 2, 38, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(currentStack, 0, 2, false))
			{
				return ItemStack.EMPTY;
			}
			
			if (currentStack.isEmpty())
			{
                slot.putStack(ItemStack.EMPTY);
            }
			else slot.onSlotChanged();
			
			return returnStack;
		}
		return ItemStack.EMPTY;
	}
	
}
