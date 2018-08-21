package fr.romax.mffjam.common.items;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.entities.EntityMessageArrow;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemMessageArrow extends ItemArrow
{
	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
	{
		return new EntityMessageArrow(worldIn, shooter, stack.getTagCompound());
	}
	
	@Override
	public CreativeTabs getCreativeTab()
	{
		return CreativeTabs.COMBAT;
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] {CreativeTabs.COMBAT, MFFJam.TAB};
	}
	
}
