package fr.romax.medievalcom.common.blocks;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TileEntityDeskMain extends TileEntityDesk
{
	private final ItemStackHandler inventory = new ItemStackHandler()
	{
		@Override
		public boolean isItemValid(int slot, ItemStack stack)
		{
			return stack.getItem() == Items.PAPER;
		}
		
		public ItemStack insertItem(int slot, ItemStack stack, boolean simulate)
		{
			if (stack.isEmpty() || !this.isItemValid(slot, stack))
			{
				return stack;
			}
			else
			{
				return super.insertItem(slot, stack, simulate);
			}
		}
	};
	
	@Override
	public IItemHandler getInventory()
	{
		return this.inventory;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound)
	{
		super.readFromNBT(compound);
		this.inventory.deserializeNBT(compound);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound)
	{
		compound.merge(this.inventory.serializeNBT());
		return super.writeToNBT(compound);
	}
}
