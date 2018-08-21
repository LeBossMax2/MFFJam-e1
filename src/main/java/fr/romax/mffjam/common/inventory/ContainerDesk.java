package fr.romax.mffjam.common.inventory;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.blocks.TileEntityDesk;
import fr.romax.mffjam.common.items.ItemWritedPaper;
import fr.romax.mffjam.common.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.SlotItemHandler;

public class ContainerDesk extends Container
{
	public static final ResourceLocation EMPTY_SLOT_ICON = new ResourceLocation(MFFJam.MODID, "items/empty_slot_paper");
	
	private final TileEntityDesk desk;
	
	public ContainerDesk(TileEntityDesk desk, InventoryPlayer playerInv)
	{
		this.desk = desk;
		
		//Desk inventory
		Slot paperSlot = new SlotItemHandler(desk.getInventory(), 0, 18, 100)
		{
			@Override
			public boolean isItemValid(ItemStack stack)
			{
				return stack.getItem() == Items.PAPER;
			}
		};
		paperSlot.setBackgroundName(EMPTY_SLOT_ICON.toString());
		this.addSlotToContainer(paperSlot);
		
		//Player inventory
		for (int i = 0; i < 3; ++i)
		{
			for (int j = 0; j < 9; ++j)
			{
				this.addSlotToContainer(new Slot(playerInv, j + i * 9 + 9, 48 + j * 18, 172 + i * 18));
			}
		}
		
		//Player hotbar
		for (int i = 0; i < 9; ++i)
		{
			this.addSlotToContainer(new Slot(playerInv, i, 48 + i * 18, 230));
		}
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		BlockPos pos = this.desk.getPos();
		return player.world.getTileEntity(pos) == this.desk && player.getDistanceSqToCenter(pos) <= 64.0d;
	}
	
	@Override
	public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
	{
		Slot slot = this.inventorySlots.get(index);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack currentStack = slot.getStack();
			ItemStack returnStack = currentStack.copy();
			
			if (index == 0)
			{
				if (!this.mergeItemStack(currentStack, 1, 37, true))
				{
					return ItemStack.EMPTY;
				}
			}
			else if (!this.mergeItemStack(currentStack, 0, 1, false))
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

	public boolean writePage(EntityPlayer sender, String pageContent, String title)
	{
		if (!pageContent.isEmpty() && !title.isEmpty() && pageContent.length() < 512 && title.length() < 16 && this.getSlot(0).getHasStack())
		{
			if (sender.addItemStackToInventory(this.writePageToStack(pageContent, title, sender.getName())))
			{
				this.getSlot(0).decrStackSize(1);
				return true;
			}
		}
		return false;
	}

	protected ItemStack writePageToStack(String pageContent, String title, String author)
	{
		ItemStack stack = new ItemStack(ModItems.writted_paper);
		ItemWritedPaper.setContent(stack, pageContent, title, author);
		return stack;
	}
	
}
