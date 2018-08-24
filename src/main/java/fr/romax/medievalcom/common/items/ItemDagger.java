package fr.romax.medievalcom.common.items;

import java.util.List;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import fr.romax.medievalcom.common.entities.EntityHangingDaggerMessage;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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
		
		if (!otherStack.isEmpty() && otherStack.hasTagCompound() && otherStack.getItem() == ModItems.WRITTEN_PAPER)
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
	
	@Override
	public float getAttackDamage()
	{
		return super.getAttackDamage() - 2.0F;
	}
	
	/**
	 * Gets a map of item attribute modifiers, used by ItemSword to increase hit damage.
	 */
	@Override
	public Multimap<String, AttributeModifier> getItemAttributeModifiers(EntityEquipmentSlot equipmentSlot)
	{
		Multimap<String, AttributeModifier> multimap = HashMultimap.<String, AttributeModifier> create();
		
		if (equipmentSlot == EntityEquipmentSlot.MAINHAND)
		{
			multimap.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Weapon modifier", this.getAttackDamage() + 3.0F, 0));
			multimap.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Weapon modifier", -2.4000000953674316D, 0));
		}
		
		return multimap;
	}
	
}
