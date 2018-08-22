package fr.romax.mffjam.common.items;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.GuiHandler;
import fr.romax.mffjam.common.entities.EntityHangingMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemSlimyPaper extends ItemWrittenPaper
{
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		ItemStack stack = player.getHeldItem(hand);
		BlockPos hangingPos = pos.offset(facing);
		
		if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(hangingPos, facing, stack))
        {
			EntityHangingMessage entityMessage = new EntityHangingMessage(worldIn, true, hangingPos, facing, stack.getTagCompound());
			if (entityMessage.onValidSurface())
			{
				if (!worldIn.isRemote)
				{
					entityMessage.playPlaceSound();
					worldIn.spawnEntity(entityMessage);
				}
				
				stack.shrink(1);
				return EnumActionResult.SUCCESS;
			}
        }
		return EnumActionResult.FAIL;
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		ItemStack stack = player.getHeldItem(hand);
		
		if (stack.hasTagCompound() && world.isRemote)
		{
			player.openGui(MFFJam.instance, GuiHandler.getHandID(hand), world, 0, 0, 0);
		}
		
		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
	
}
