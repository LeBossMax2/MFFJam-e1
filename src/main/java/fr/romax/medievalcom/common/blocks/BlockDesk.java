package fr.romax.medievalcom.common.blocks;

import java.util.Random;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.GuiHandler;
import fr.romax.medievalcom.common.utils.InventoryUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockDesk extends BlockHorizontal implements ITileEntityProvider
{
	public static final PropertyBool MAIN_PART = PropertyBool.create("part");
	private static final AxisAlignedBB[] AABB = new AxisAlignedBB[] {
		new AxisAlignedBB( 0, 0, 0, 1, 1, 2), //SOUTH
		new AxisAlignedBB(-1, 0, 0, 1, 1, 1), //WEST
		new AxisAlignedBB( 0, 0,-1, 1, 1, 1), //NORTH
		new AxisAlignedBB( 0, 0, 0, 2, 1, 1)};//EAST
	
	public BlockDesk()
	{
		super(Material.WOOD);
        this.hasTileEntity = true;
		this.setSoundType(SoundType.WOOD);
		this.setDefaultState(this.getBlockState().getBaseState().withProperty(FACING, EnumFacing.NORTH).withProperty(MAIN_PART, false));
	}
	
	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer)
	{
		// The second part of the model needs to be rendered in all layers except CUTOUT_MIPPED
		return state.getValue(MAIN_PART) ? layer == BlockRenderLayer.SOLID : layer != BlockRenderLayer.CUTOUT_MIPPED;
	}
	
	@Override
	public boolean isFullCube(IBlockState state)
	{
		return false;
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return false;
	}

	
	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos)
	{
		EnumFacing dir = state.getValue(FACING).rotateY();
		if (state.getValue(MAIN_PART)) dir = dir.getOpposite();
		return AABB[dir.getHorizontalIndex()];
	}
	
	@Override
    public Item getItemDropped(IBlockState state, Random rand, int fortune)
    {
        return state.getValue(MAIN_PART) ? Item.getItemFromBlock(this) : Items.AIR;
    }

	@Override
    public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos)
    {
        EnumFacing enumfacing = state.getValue(FACING).rotateY();

        if (!state.getValue(MAIN_PART))
        {
            if (worldIn.getBlockState(pos.offset(enumfacing)).getBlock() != this)
            {
                worldIn.setBlockToAir(pos);
            }
        }
        else if (worldIn.getBlockState(pos.offset(enumfacing.getOpposite())).getBlock() != this)
        {
            if (!worldIn.isRemote)
            {
                this.dropBlockAsItem(worldIn, pos, state, 0);
            }

            worldIn.setBlockToAir(pos);
        }
    }
	
	@Override
    public void onBlockHarvested(World worldIn, BlockPos pos, IBlockState state, EntityPlayer player)
    {
        if (player.capabilities.isCreativeMode && !state.getValue(MAIN_PART))
        {
            BlockPos blockpos = pos.offset(state.getValue(FACING).rotateY());

            if (worldIn.getBlockState(blockpos).getBlock() == this)
            {
                worldIn.setBlockToAir(blockpos);
            }
        }
    }
	
	@Override
    public EnumPushReaction getMobilityFlag(IBlockState state)
    {
        return EnumPushReaction.DESTROY;
    }
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta)
	{
		return TileEntityDesk.createTile(meta);
	}
	
	@Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
    {
        if (!worldIn.isRemote)
        {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            
            if (tileentity instanceof TileEntityDesk && ((TileEntityDesk)tileentity).getInventory() != null)
            {
                playerIn.openGui(MedievalCommunications.instance, GuiHandler.TILE_ENTITY, worldIn, pos.getX(), pos.getY(), pos.getZ());
            }

        }
        return true;
    }
	
	@Override
    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
    {
		if (state.getValue(MAIN_PART))
		{
			TileEntity tileentity = worldIn.getTileEntity(pos);
			
			if (tileentity instanceof TileEntityDesk)
			{
				InventoryUtils.dropInventoryItems(worldIn, pos, ((TileEntityDesk)tileentity).getInventory());
			}
		}

        super.breakBlock(worldIn, pos, state);
    }
	
	@Override
	public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(FACING).getHorizontalIndex() | (state.getValue(MAIN_PART) ? 4 : 0);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(MAIN_PART, meta > 3);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, FACING, MAIN_PART);
	}
	
}
