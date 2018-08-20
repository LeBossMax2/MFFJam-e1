package fr.romax.mffjam.common.items;

import javax.annotation.Nullable;

import fr.romax.mffjam.common.blocks.BlockDesk;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemDeskBlock extends ItemBlock
{
	public ItemDeskBlock(Block block)
	{
		super(block);
	}
	
	@Override
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        IBlockState iblockstate = worldIn.getBlockState(pos);
        Block block = iblockstate.getBlock();

        if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(facing);
        }

        ItemStack itemstack = player.getHeldItem(hand);

        if (!itemstack.isEmpty())
        {
        	EnumFacing deskFacing = player.getHorizontalFacing();
        	
        	EnumFacing deskDir = deskFacing.rotateY();
        	float distToFace = getDistToFace(deskDir, hitX, hitY, hitZ);
        	
        	if (distToFace > 0.5f)
        	{
        		pos = pos.offset(deskDir.getOpposite());
        	}
        	
        	BlockPos secPos = pos.offset(deskDir);
        	
        	if (player.canPlayerEdit(pos, facing, itemstack) && player.canPlayerEdit(secPos, facing, itemstack))
    		{
        		int i = this.getMetadata(itemstack.getMetadata());
        		IBlockState state1 = this.block.getStateForPlacement(worldIn, pos, facing, hitX, hitY, hitZ, i, player, hand);
        		IBlockState state0 = state1.withProperty(BlockDesk.MAIN_PART, true);
        		if (this.mayPlace(worldIn, state0, pos, false, facing, (Entity)null) && this.mayPlace(worldIn, state1, secPos, false, facing, (Entity)null))
	        	{
	        		if (placeBlockAt(itemstack, player, worldIn, pos, secPos, facing, hitX, hitY, hitZ, state0, state1))
	        		{
	        			IBlockState newState = worldIn.getBlockState(pos);
	        			SoundType soundtype = newState.getBlock().getSoundType(newState, worldIn, pos, player);
	        			worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
	        			itemstack.shrink(1);
	        		}
	        		
	        		return EnumActionResult.SUCCESS;
	        	}
    		}
        }
        return EnumActionResult.FAIL;
    }

    public boolean mayPlace(World world, IBlockState state, BlockPos pos, boolean skipCollisionCheck, EnumFacing sidePlacedOn, @Nullable Entity placer)
    {
        IBlockState iblockstate1 = world.getBlockState(pos);
        AxisAlignedBB axisalignedbb = skipCollisionCheck ? null : state.getCollisionBoundingBox(world, pos);

        if (axisalignedbb != Block.NULL_AABB && !world.checkNoEntityCollision(axisalignedbb.offset(pos), placer))
        {
            return false;
        }
        else if (iblockstate1.getMaterial() == Material.CIRCUITS && state == Blocks.ANVIL)
        {
            return true;
        }
        else
        {
            return iblockstate1.getBlock().isReplaceable(world, pos) && state.getBlock().canPlaceBlockOnSide(world, pos, sidePlacedOn);
        }
    }
    

    @Override
    @SideOnly(Side.CLIENT)
    public boolean canPlaceBlockOnSide(World worldIn, BlockPos pos, EnumFacing side, EntityPlayer player, ItemStack stack)
    {
        Block block = worldIn.getBlockState(pos).getBlock();

        if (block == Blocks.SNOW_LAYER && block.isReplaceable(worldIn, pos))
        {
            side = EnumFacing.UP;
        }
        else if (!block.isReplaceable(worldIn, pos))
        {
            pos = pos.offset(side);
        }
        
        IBlockState iblockstate1 = worldIn.getBlockState(pos);

        if (!worldIn.checkNoEntityCollision(new AxisAlignedBB(pos), null))
        {
            return false;
        }
        else
        {
            return iblockstate1.getBlock().isReplaceable(worldIn, pos) && this.block.canPlaceBlockOnSide(worldIn, pos, side);
        }
    }
	
	
	private static float getDistToFace(EnumFacing dir, float hitX, float hitY, float hitZ)
	{
		switch (dir)
		{
		case DOWN:
			return hitY;
		case EAST:
			return 1.0f - hitX;
		case NORTH:
			return hitZ;
		case SOUTH:
			return 1.0f - hitZ;
		case UP:
			return 1.0f - hitY;
		case WEST:
			return hitX;
		default:
			return 1.0f;
		}
	}
	
	public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, BlockPos pos, BlockPos secPos, EnumFacing side, float hitX, float hitY, float hitZ, IBlockState newState0, IBlockState newState1)
    {
        if (!world.setBlockState(pos, newState0, 11)) return false;
        world.setBlockState(secPos, newState1, 11);

        IBlockState state = world.getBlockState(pos);
        if (state.getBlock() == this.block)
        {
            setTileEntityNBT(world, player, pos, stack);
            this.block.onBlockPlacedBy(world, pos, state, player, stack);

            if (player instanceof EntityPlayerMP)
                CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)player, pos, stack);
        }

        return true;
    }
	
}