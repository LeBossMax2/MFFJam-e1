package fr.romax.medievalcom.common.entities;

import fr.romax.medievalcom.common.items.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class EntityHangingDaggerMessage extends EntityHangingMessage
{
	protected float daggerX, daggerY;
	protected ItemStack daggerStack;
	
    public EntityHangingDaggerMessage(World worldIn)
    {
        super(worldIn);
        this.daggerX = 0.5F;
        this.daggerY = 0.5F;
        this.daggerStack = ItemStack.EMPTY;
    }

    public EntityHangingDaggerMessage(World worldIn, BlockPos hangingPos, EnumFacing facing, float hitX, float hitY, float hitZ, ItemStack dagger, NBTTagCompound itemData)
    {
        super(worldIn, false, hangingPos, facing, itemData);
        this.daggerX = MathHelper.clamp(facing.getAxis() == EnumFacing.Axis.X ? 1.0F - hitZ : hitX, 1.0F / 16.0F, 15.0F / 16.0F);
        if (facing.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE)
        {
        	this.daggerX = 1.0F - this.daggerX;
        }
        this.daggerY = MathHelper.clamp(hitY, 0.0F, 1.0F);
        this.daggerStack = dagger;
    }
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setFloat("daggerX", this.daggerX);
		compound.setFloat("daggerY", this.daggerY);
		compound.setTag("daggerItem", this.daggerStack.serializeNBT());
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
        this.daggerX = compound.hasKey("daggerX", NBT.TAG_FLOAT) ? compound.getFloat("daggerX") : 0.5F;
        this.daggerY = compound.hasKey("daggerY", NBT.TAG_FLOAT) ? compound.getFloat("daggerY") : 0.5f;
        this.daggerStack = compound.hasKey("daggerItem", NBT.TAG_COMPOUND) ? new ItemStack(compound.getCompoundTag("daggerItem")) : new ItemStack(ModItems.DAGGER);
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		super.writeSpawnData(buffer);
		buffer.writeFloat(this.daggerX);
		buffer.writeFloat(this.daggerY);
		ByteBufUtils.writeItemStack(buffer, this.daggerStack);
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		super.readSpawnData(buffer);
		this.daggerX = buffer.readFloat();
		this.daggerY = buffer.readFloat();
		this.daggerStack = ByteBufUtils.readItemStack(buffer);
	}

	@Override
	protected void dropItems()
	{
		super.dropItems();
		if (!this.daggerStack.isEmpty())
		{
			this.entityDropItem(this.getDaggerStack(), 0.0F);
		}
	}
	
	
	public float getDaggerX()
	{
		return daggerX;
	}
	
	public float getDaggerY()
	{
		return daggerY;
	}
	
	public ItemStack getDaggerStack()
	{
		return daggerStack;
	}
	
}
