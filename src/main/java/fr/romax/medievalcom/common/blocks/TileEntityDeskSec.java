package fr.romax.medievalcom.common.blocks;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.items.IItemHandler;

public class TileEntityDeskSec extends TileEntityDesk
{
	private IItemHandler inventory;
	private boolean inventoryInit = false;

	@Override
	public IItemHandler getInventory()
	{
		if (!this.inventoryInit)
		{
			EnumFacing dir = EnumFacing.getHorizontal(this.getBlockMetadata() + 1);
			BlockPos mainPos = this.pos.offset(dir);
			if (this.world.isBlockLoaded(mainPos))
			{
				TileEntity mainTile = this.world.getTileEntity(mainPos);
				if (mainTile instanceof TileEntityDeskMain)
				{
					this.inventory = ((TileEntityDeskMain) mainTile).getInventory();
				}
				this.inventoryInit = true;
			}
		}
		return this.inventory;
	}
	
}
