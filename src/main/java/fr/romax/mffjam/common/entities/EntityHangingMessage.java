package fr.romax.mffjam.common.entities;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.GuiHandler;
import fr.romax.mffjam.common.items.ItemWrittenPaper;
import fr.romax.mffjam.common.items.ModItems;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityHanging;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;

public class EntityHangingMessage extends EntityHanging implements IEntityAdditionalSpawnData
{
	private NBTTagCompound itemData;

    public EntityHangingMessage(World worldIn)
    {
        super(worldIn);
        this.itemData = new NBTTagCompound();
    }

    public EntityHangingMessage(World worldIn, BlockPos hangingPos, EnumFacing facing, NBTTagCompound itemData)
    {
        super(worldIn, hangingPos);
        this.updateFacingWithBoundingBox(facing);
        this.itemData = itemData;
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
		buffer.writeByte(this.facingDirection.getHorizontalIndex());
	}

	@Override
	public void readSpawnData(ByteBuf additionalData)
	{
		String text = ByteBufUtils.readUTF8String(additionalData);
		String title = ByteBufUtils.readUTF8String(additionalData);
		String author = ByteBufUtils.readUTF8String(additionalData);
		ItemWrittenPaper.setContent(this.itemData, text, title, author);
		this.updateFacingWithBoundingBox(EnumFacing.getHorizontal(additionalData.readUnsignedByte()));
	}

	@Override
    public int getWidthPixels()
    {
        return 14;
    }

	@Override
    public int getHeightPixels()
    {
        return 16;
    }
	
	@Override
	public void onBroken(Entity brokenEntity)
	{
		this.playPlaceSound();

		if (this.world.getGameRules().getBoolean("doEntityDrops"))
        {
			if (brokenEntity instanceof EntityPlayer)
            {
                EntityPlayer entityplayer = (EntityPlayer)brokenEntity;

                if (entityplayer.capabilities.isCreativeMode)
                {
                    return;
                }
            }
			
			this.dropItems();
        }
	}
	
	protected ItemStack getPage()
	{
		ItemStack paper = new ItemStack(ModItems.written_paper);
		paper.setTagCompound(this.itemData.copy());
		return paper;
	}
	
	private void dropItems()
	{
		this.entityDropItem(this.getPage(), 0.0F);
	}

	@Override
	public void playPlaceSound()
	{
		BlockPos hangingBlock = this.hangingPosition.offset(this.facingDirection.getOpposite());
		IBlockState state = this.world.getBlockState(hangingBlock);
		this.playSound(state.getBlock().getSoundType(state, this.world, hangingBlock, this).getHitSound(), 1.5F, 1.0F);
	}
	
	@Override
	public boolean processInitialInteract(EntityPlayer player, EnumHand hand)
	{
		if (this.world.isRemote)
		{
			// Show the message in a gui
			player.openGui(MFFJam.MODID, GuiHandler.ENTITY, this.world, this.getEntityId(), 0, 0);
		}
		return true;
	}

	public String pageContent()
	{
		return this.itemData.getString("text");
	}
	
	@Override
	public ItemStack getPickedResult(RayTraceResult target)
	{
		return this.getPage();
	}
	
}
