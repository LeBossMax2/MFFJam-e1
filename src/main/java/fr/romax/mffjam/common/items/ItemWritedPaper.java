package fr.romax.mffjam.common.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemWritedPaper extends Item{

	public ItemWritedPaper() {
		this.setMaxStackSize(1);
	}
	
	public void setWritedText(ItemStack stack, String text) {
		this.addNBTTagCompound(stack);
		
		stack.getTagCompound().setString("WritedText", text);
	}
	
	public String getWritedText(ItemStack stack) {
		this.addNBTTagCompound(stack);
		String text = stack.getTagCompound().getString("WritedText");
		if(text == null) {
			return "Empty";
		}
		return text;
	}
	
	private void addNBTTagCompound(ItemStack stack) {
		if(!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
	}
	
}
