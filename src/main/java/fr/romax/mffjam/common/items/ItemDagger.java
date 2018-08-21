package fr.romax.mffjam.common.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class ItemDagger extends ItemSword{

	public ItemDagger() {
		super(ToolMaterial.IRON);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		if(!worldIn.isRemote) {
			if(handIn == EnumHand.MAIN_HAND) {
				if(playerIn.getHeldItemOffhand().getItem() instanceof ItemWritedPaper) {
					playerIn.sendMessage(new TextComponentString("Fusion de la dague et du papier."));
					playerIn.getHeldItemOffhand().setCount(0);
					playerIn.setItemStackToSlot(EntityEquipmentSlot.MAINHAND, ItemStack.EMPTY);
					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
				}
			}else {
				if(playerIn.getHeldItemMainhand().getItem() instanceof ItemWritedPaper) {
					playerIn.sendMessage(new TextComponentString("Fusion de la dague et du papier."));
					playerIn.getHeldItemMainhand().setCount(0);
					playerIn.setItemStackToSlot(EntityEquipmentSlot.OFFHAND, ItemStack.EMPTY);

					return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
				}
			}
		}
		
		return super.onItemRightClick(worldIn, playerIn, handIn);
	}

}
