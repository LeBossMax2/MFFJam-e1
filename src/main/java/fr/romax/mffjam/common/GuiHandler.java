package fr.romax.mffjam.common;

import fr.romax.mffjam.client.gui.GuiDesk;
import fr.romax.mffjam.common.blocks.TileEntityDesk;
import fr.romax.mffjam.common.inventory.ContainerDesk;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int TILE_ENTITY = 0;

	@Override
	public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z)
	{
		if (id == TILE_ENTITY)
		{
			TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
			
			if (tile instanceof TileEntityDesk)
			{
				TileEntityDesk desk = (TileEntityDesk)tile;
				return desk == null ? null : new GuiDesk(desk, player.inventory);
			}
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
