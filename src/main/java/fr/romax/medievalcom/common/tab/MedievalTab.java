package fr.romax.medievalcom.common.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MedievalTab extends CreativeTabs{

	public MedievalTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Items.ARROW);
	}

}
