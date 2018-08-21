package fr.romax.mffjam.common;

import fr.romax.mffjam.client.gui.GuiDesk;
import fr.romax.mffjam.client.gui.GuiReadMessage;
import fr.romax.mffjam.common.blocks.TileEntityDesk;
import fr.romax.mffjam.common.inventory.ContainerDesk;
import fr.romax.mffjam.common.items.ItemWritedPaper;
import fr.romax.mffjam.common.items.ModItems;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int TILE_ENTITY = 0, ITEM_MAIN_HAND = 1, ITEM_OFF_HAND = 2;
	
	public static int getHandID(EnumHand hand)
	{
		return hand == EnumHand.OFF_HAND ? ITEM_OFF_HAND : ITEM_MAIN_HAND;
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
			
			if (stack.getItem() == ModItems.writted_paper && stack.hasTagCompound())
			{
				return new GuiReadMessage(ItemWritedPaper.getWritedText(stack));
			}
			break;
		}
		return null;
	}

	@Override
	public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == TILE_ENTITY)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			
			if (tile instanceof TileEntityDesk)
			{
				TileEntityDesk desk = (TileEntityDesk)tile;
				return desk == null ? null : new ContainerDesk(desk, player.inventory);
			}
		}
		return null;
	}
	
}
