package fr.romax.medievalcom.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.IItemHandler;

public abstract class TileEntityDesk extends TileEntity
{
	
	public abstract IItemHandler getInventory();
	
	public static TileEntityDesk createTile(int meta)
	{
		return meta > 3 ? new TileEntityDeskMain() : new TileEntityDeskSec();
	}
	
}
