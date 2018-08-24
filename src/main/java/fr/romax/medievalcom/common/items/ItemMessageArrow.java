package fr.romax.medievalcom.common.items;

import java.util.List;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.entities.EntityMessageArrow;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

public class ItemMessageArrow extends ItemArrow
{
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand)
	{
		EnumHand otherHand = hand == EnumHand.MAIN_HAND ? EnumHand.OFF_HAND : EnumHand.MAIN_HAND;
		ItemStack otherStack = player.getHeldItem(otherHand);
        ItemStack stack = player.getHeldItem(hand);
		
		if (otherStack.getItem() instanceof ItemBow)
		{
			return new ActionResult<>(EnumActionResult.PASS, stack);
		}

		if (stack.hasTagCompound())
		{
			ItemStack page = new ItemStack(ModItems.written_paper);
			ItemWrittenPaper.setContent(page, ItemWrittenPaper.getWritedText(stack), ItemWrittenPaper.getTitle(stack), ItemWrittenPaper.getAuthor(stack));
			ItemStack arrow = new ItemStack(Items.ARROW);
			stack.shrink(1);
			player.inventory.placeItemBackInInventory(world, page);
			player.inventory.placeItemBackInInventory(world, arrow);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, stack);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn)
	{
		if (stack.hasTagCompound())
		{
			tooltip.add(I18n.format("item.message_arrow.tooltip"));
		}
	}
	
	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter)
	{
		return new EntityMessageArrow(worldIn, shooter, stack.getTagCompound());
	}
	
	@Override
	public CreativeTabs getCreativeTab()
	{
		return CreativeTabs.COMBAT;
	}
	
	@Override
	public CreativeTabs[] getCreativeTabs()
	{
		return new CreativeTabs[] {CreativeTabs.COMBAT, MedievalCommunications.TAB};
	}
	
}
