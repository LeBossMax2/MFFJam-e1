package fr.romax.mffjam.common.entities;

import fr.romax.mffjam.common.items.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityMessageArrow extends EntityArrow
{
	private NBTTagCompound itemData;
	
	public EntityMessageArrow(World worldIn)
	{
		super(worldIn);
	}
	
	public EntityMessageArrow(World worldIn, double x, double y, double z, NBTTagCompound itemData)
	{
		super(worldIn, x, y, z);
		this.itemData = itemData == null ? new NBTTagCompound() : itemData.copy();
	}
	
	public EntityMessageArrow(World worldIn, EntityLivingBase shooter, NBTTagCompound itemData)
	{
		super(worldIn, shooter);
		this.itemData = itemData == null ? new NBTTagCompound() : itemData.copy();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setTag("itemData", this.itemData);
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.itemData = compound.getCompoundTag("itemData");
	}

	@Override
	protected ItemStack getArrowStack()
	{
		ItemStack stack = new ItemStack(ModItems.message_arrow);
		stack.setTagCompound(this.itemData.copy());
		return stack;
	}
	
	@Override
	protected void arrowHit(EntityLivingBase living)
	{
		ItemStack dropStack = new ItemStack(ModItems.writted_paper);
		dropStack.setTagCompound(this.itemData.copy());
		
		this.entityDropItem(dropStack, 0.1F);
	}
	
}
