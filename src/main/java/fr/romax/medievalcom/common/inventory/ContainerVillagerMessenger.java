package fr.romax.medievalcom.common.inventory;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.entities.EntityVillagerMessenger;
import fr.romax.medievalcom.common.items.ItemWrittenPaper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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
	
	private final EntityVillagerMessenger entity;
	
	public ContainerVillagerMessenger(EntityVillagerMessenger entity, InventoryPlayer playerInv)
	{
		this.entity = entity;
		
		IItemHandler villagerInv = new ItemStackHandler(2)
		{
			@Override
			public boolean isItemValid(int slot, ItemStack stack)
			{
				return slot == 0 ? stack.getItem() instanceof ItemWrittenPaper : stack.getItem() == Items.EMERALD;
			}
			
			@Override
			public int getSlotLimit(int slot)
			{
				return 1;
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
	public void onContainerClosed(EntityPlayer player)
	{
		super.onContainerClosed(player);

		if (!player.world.isRemote)
		{
			if (!player.isEntityAlive() || player instanceof EntityPlayerMP && ((EntityPlayerMP) player).hasDisconnected())
			{
				if (this.getSlot(0).getHasStack()) player.dropItem(this.getSlot(0).getStack(), false);
				if (this.getSlot(1).getHasStack()) player.dropItem(this.getSlot(1).getStack(), false);
			}
			else
			{
				player.inventory.placeItemBackInInventory(player.world, this.getSlot(0).getStack());
				player.inventory.placeItemBackInInventory(player.world, this.getSlot(1).getStack());
			}
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

	public boolean sendMessage(EntityPlayerMP sender, String addressee)
	{
		if (!addressee.isEmpty() && addressee.length() <= 16 && this.getSlot(0).getHasStack() && this.getSlot(1).getHasStack())
		{
			EntityPlayer addressePlayer = sender.world.getPlayerEntityByName(addressee);
			
			if (addressePlayer != null)
			{
				this.entity.sendMessage(addressePlayer, this.getSlot(0).decrStackSize(1));
				this.getSlot(1).decrStackSize(1);
				
				sender.closeScreen();
				
				return true;
			}
		}
		return false;
	}
	
}
