package fr.romax.medievalcom.common;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.client.gui.GuiDesk;
import fr.romax.medievalcom.client.gui.GuiReadMessage;
import fr.romax.medievalcom.client.gui.GuiVillagerMessenger;
import fr.romax.medievalcom.common.blocks.TileEntityDesk;
import fr.romax.medievalcom.common.entities.EntityHangingMessage;
import fr.romax.medievalcom.common.entities.EntityVillagerMessager;
import fr.romax.medievalcom.common.inventory.ContainerDesk;
import fr.romax.medievalcom.common.inventory.ContainerVillagerMessenger;
import fr.romax.medievalcom.common.items.ItemWrittenPaper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int TILE_ENTITY = 0, ITEM_MAIN_HAND = 1, ITEM_OFF_HAND = 2, ENTITY = 3;
	
	public static int getHandID(EnumHand hand)
	{
		return hand == EnumHand.OFF_HAND ? ITEM_OFF_HAND : ITEM_MAIN_HAND;
	}
	
	public static void openEntityGui(World world, EntityPlayer player, Entity entityIn)
	{
		player.openGui(MedievalCommunications.instance, ENTITY, world, entityIn.getEntityId(), 0, 0);
	}
	
	public static void openTileGui(World world, EntityPlayer player, BlockPos pos)
	{
		player.openGui(MedievalCommunications.instance, TILE_ENTITY, world, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public static void openItemGui(World world, EntityPlayer player, EnumHand hand)
	{
		player.openGui(MedievalCommunications.instance, getHandID(hand), world, 0, 0, 0);
	}
	
	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (id)
		{
		case TILE_ENTITY:
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			
			if (tile instanceof TileEntityDesk)
			{
				TileEntityDesk desk = (TileEntityDesk)tile;
				return desk == null ? null : new GuiDesk(desk, player.inventory);
			}
			break;
		case ITEM_MAIN_HAND:
		case ITEM_OFF_HAND:
			ItemStack stack = player.getHeldItem(id == ITEM_OFF_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND);
			
			if (stack.getItem() instanceof ItemWrittenPaper && stack.hasTagCompound())
			{
				return new GuiReadMessage(ItemWrittenPaper.getWritedText(stack));
			}
			break;
		case ENTITY:
			Entity targetEntity = world.getEntityByID(x);
			
			if (targetEntity instanceof EntityHangingMessage)
			{
				return new GuiReadMessage(((EntityHangingMessage)targetEntity).pageContent());
			}
			else if (targetEntity instanceof EntityVillagerMessager)
			{
				return new GuiVillagerMessenger((EntityVillagerMessager)targetEntity, player.inventory);
			}
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (id)
		{
		case TILE_ENTITY:
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			
			if (tile instanceof TileEntityDesk)
			{
				TileEntityDesk desk = (TileEntityDesk)tile;
				return desk == null ? null : new ContainerDesk(desk, player.inventory);
			}
			break;
		case ENTITY:
			Entity targetEntity = world.getEntityByID(x);
			
			if (targetEntity instanceof EntityVillagerMessager)
			{
				return new ContainerVillagerMessenger((EntityVillagerMessager)targetEntity, player.inventory);
			}
		}
		return null;
	}
	
}
