package fr.romax.mffjam.common.items;

import fr.romax.mffjam.common.entities.EntityHangingMessage;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDagger extends ItemSword
{
	
	public ItemDagger()
	{
		super(ToolMaterial.IRON);
	}
	
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		EnumHand otherHand = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack otherStack = player.getHeldItem(otherHand);
		
		if (!otherStack.isEmpty() && otherStack.hasTagCompound() && otherStack.getItem() == ModItems.written_paper)
		{
	        ItemStack stack = player.getHeldItem(hand);
			BlockPos hangingPos = pos.offset(facing);
			
			if (facing != EnumFacing.DOWN && facing != EnumFacing.UP && player.canPlayerEdit(hangingPos, facing, stack))
	        {
				EntityHangingMessage entityMessage = new EntityHangingMessage(worldIn, false, hangingPos, facing, otherStack.getTagCompound());
				if (entityMessage.onValidSurface())
				{
					if (!worldIn.isRemote)
					{
						entityMessage.playPlaceSound();
						worldIn.spawnEntity(entityMessage);
					}
					
					stack.shrink(1);
					if (!player.isCreative()) otherStack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
	        }
			return EnumActionResult.FAIL;
		}
		return EnumActionResult.PASS;
	}
	
}
