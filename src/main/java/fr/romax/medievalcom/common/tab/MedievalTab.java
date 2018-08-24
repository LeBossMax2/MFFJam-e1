package fr.romax.medievalcom.common.tab;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class MedievalTab extends CreativeTabs
{

	public MedievalTab(String label)
	{
		super(label);
	}

	@Override
    @SideOnly(Side.CLIENT)
	public ItemStack createIcon()
	{
		return new ItemStack(Items.ARROW);
	}

}
