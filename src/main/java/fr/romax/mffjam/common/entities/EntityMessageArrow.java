package fr.romax.mffjam.common.entities;

import fr.romax.mffjam.common.items.ItemWritedPaper;
import fr.romax.mffjam.common.items.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.common.registry.IThrowableEntity;

public class EntityMessageArrow extends EntityArrow implements IThrowableEntity, IEntityAdditionalSpawnData
{
	private NBTTagCompound itemData;
	
	public EntityMessageArrow(World worldIn)
	{
		super(worldIn);
		this.itemData = new NBTTagCompound();
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
	public void setThrower(Entity entity)
	{
		this.shootingEntity = entity;
	}

	@Override
	public Entity getThrower()
	{
		return this.shootingEntity;
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
	public void writeSpawnData(ByteBuf buffer)
	{
		ByteBufUtils.writeUTF8String(buffer, this.itemData.getString("text"));
		ByteBufUtils.writeUTF8String(buffer, this.itemData.getString("title"));
		ByteBufUtils.writeUTF8String(buffer, this.itemData.getString("author"));
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		String text = ByteBufUtils.readUTF8String(additionalData);
		String title = ByteBufUtils.readUTF8String(additionalData);
		String author = ByteBufUtils.readUTF8String(additionalData);
		ItemWritedPaper.setContent(this.itemData, text, title, author);
	}

	@Override
	protected ItemStack getArrowStack()
	{
		ItemStack stack = new ItemStack(ModItems.message_arrow);
		stack.setTagCompound(this.itemData.copy());
		return stack;
	}
	
	protected void dropPage()
	{
		if (!this.world.isRemote && this.world.getGameRules().getBoolean("doMobLoot"))
        {
			ItemStack dropStack = new ItemStack(ModItems.writted_paper);
			dropStack.setTagCompound(this.itemData.copy());
			
			this.entityDropItem(dropStack, 0.1F);
        }
	}
	
	@Override
	protected void arrowHit(EntityLivingBase living)
	{
		this.dropPage();
	}
	
}
