package fr.romax.mffjam.common.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class MFFJamTab extends CreativeTabs{

	public MFFJamTab(String label) {
		super(label);
	}

	@Override
	public ItemStack getTabIconItem() {
		return new ItemStack(Items.ARROW);
	}

}
