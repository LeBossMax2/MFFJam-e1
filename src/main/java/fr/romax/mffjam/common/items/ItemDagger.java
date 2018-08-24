package fr.romax.mffjam.common.items;

import java.util.List;

import fr.romax.mffjam.common.entities.EntityHangingDaggerMessage;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
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
				EntityHangingDaggerMessage entityMessage = new EntityHangingDaggerMessage(worldIn, hangingPos, facing, hitX, hitY, hitZ, stack.copy(), otherStack.getTagCompound());
				if (entityMessage.onValidSurface())
				{
					if (!worldIn.isRemote)
					{
						entityMessage.playPlaceSound();
						worldIn.spawnEntity(entityMessage);
					}
					
					player.setHeldItem(hand, ItemStack.EMPTY);
					if (!player.isCreative()) otherStack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
	        }
			return EnumActionResult.FAIL;
		}
		return EnumActionResult.PASS;
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		tooltip.add(I18n.format("item.dagger.tooltip"));
	}
	
}
