package fr.romax.mffjam.common.world.gen.structure;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockStoneSlab;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.BlockWoodSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraft.world.gen.structure.StructureBoundingBox;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraft.world.gen.structure.StructureVillagePieces.PieceWeight;
import net.minecraft.world.gen.structure.StructureVillagePieces.Start;
import net.minecraft.world.gen.structure.StructureVillagePieces.Village;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.registry.VillagerRegistry.IVillageCreationHandler;

public class SpyVillageHouse extends Village
{
	
	
	public SpyVillageHouse()
	{ }

    public SpyVillageHouse(StructureVillagePieces.Start start, int type, StructureBoundingBox box, EnumFacing facing)
    {
        super(start, type);
        this.setCoordBaseMode(facing);
        this.boundingBox = box;
    }
    
    
    @Override
    protected IBlockState getBiomeSpecificBlockState(IBlockState blockstateIn)
    {
    	if (blockstateIn.getBlock() == Blocks.WOODEN_SLAB)
    	{
    		BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(startPiece == null ? null : startPiece.biome, blockstateIn);
            MinecraftForge.TERRAIN_GEN_BUS.post(event);
            if (event.getResult() == net.minecraftforge.fml.common.eventhandler.Event.Result.DENY) return event.getReplacement();
            switch (this.structureType)
			{
			case 1: // Desert
				return Blocks.STONE_SLAB.getDefaultState().withProperty(BlockStoneSlab.VARIANT, BlockStoneSlab.EnumType.SAND).withProperty(BlockSlab.HALF, blockstateIn.getValue(BlockSlab.HALF));
			case 2: // Acacia
				return blockstateIn.withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.ACACIA);
			case 3: // Spruce
				return blockstateIn.withProperty(BlockWoodSlab.VARIANT, BlockPlanks.EnumType.SPRUCE);
			default:
				return blockstateIn;
			}
    	}
    	return super.getBiomeSpecificBlockState(blockstateIn);
    }

	@Override
	public boolean addComponentParts(World worldIn, Random randomIn, StructureBoundingBox structureBoundingBoxIn)
	{
        if (this.averageGroundLvl < 0)
        {
            this.averageGroundLvl = this.getAverageGroundLevel(worldIn, structureBoundingBoxIn);

            if (this.averageGroundLvl < 0)
            {
                return true;
            }

            this.boundingBox.offset(0, this.averageGroundLvl - this.boundingBox.maxY + 8 - 4, 0);
        }
        
        IBlockState cobble = this.getBiomeSpecificBlockState(Blocks.COBBLESTONE.getDefaultState());
        IBlockState stairs = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.NORTH));
        IBlockState stairs_seat = this.getBiomeSpecificBlockState(Blocks.OAK_STAIRS.getDefaultState().withProperty(BlockStairs.FACING, EnumFacing.SOUTH));
        IBlockState planks = this.getBiomeSpecificBlockState(Blocks.PLANKS.getDefaultState());
        IBlockState log = this.getBiomeSpecificBlockState(Blocks.LOG.getDefaultState());
        IBlockState slab = this.getBiomeSpecificBlockState(Blocks.WOODEN_SLAB.getDefaultState());
        IBlockState fence = this.getBiomeSpecificBlockState(Blocks.OAK_FENCE.getDefaultState());
        IBlockState ladders = Blocks.LADDER.getDefaultState().withProperty(BlockLadder.FACING, EnumFacing.SOUTH);
        IBlockState air = Blocks.AIR.getDefaultState();
        
        //Floor
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 3, 0, 4, 3, 4, cobble, cobble, false);
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, 1, 3, 6, 3, air, air, false);
        
        //Walls
        for (int x = 0; x < 5; x += 4)
        {
        	this.fillWithBlocks(worldIn, structureBoundingBoxIn, x, 6, 1, x, 6, 3, planks, planks, false);
    		this.fillWithBlocks(worldIn, structureBoundingBoxIn, x, 4, 1, x, 5, 1, log, log, false);
    		this.fillWithBlocks(worldIn, structureBoundingBoxIn, x, 4, 2, x, 5, 2, planks, planks, false);
    		this.fillWithBlocks(worldIn, structureBoundingBoxIn, x, 4, 3, x, 5, 3, log, log, false);
        }
        
        for (int z = 0; z < 5; z += 4)
        {
        	this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 6, z, 3, 6, z, planks, planks, false);
    		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 4, z, 1, 5, z, log, log, false);
    		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 3, 4, z, 3, 5, z, log, log, false);
        }
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 2, 4, 4, 2, 5, 4, planks, planks, false);
        this.createVillageDoor(worldIn, structureBoundingBoxIn, randomIn, 2, 4, 0, EnumFacing.NORTH);
        
        //Roof
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 7, 1, 3, 7, 3, slab, slab, false);

        this.setBlockState(worldIn, slab, 2, 7, 0, structureBoundingBoxIn);
        this.setBlockState(worldIn, slab, 0, 7, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, slab, 2, 7, 4, structureBoundingBoxIn);
        this.setBlockState(worldIn, slab, 4, 7, 2, structureBoundingBoxIn);
        
        for (int x = 0; x < 5; x += 4)
        {
        	for (int z = 0; z < 5; z += 4)
            {
                this.setBlockState(worldIn, slab, x, 6, z, structureBoundingBoxIn);
                this.fillWithBlocks(worldIn, structureBoundingBoxIn, x, 4, z, x, 5, z, fence, fence, false);
            }
        }
        
        //Underground layer
        this.fillWithBlocks(worldIn, structureBoundingBoxIn, 0, 0, 1, 3, 3, 4, cobble, air, false);
        
        this.setBlockState(worldIn, Blocks.NOTEBLOCK.getDefaultState(), 2, 1, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.REDSTONE_TORCH.getDefaultState(), 2, 1, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.HALF, BlockTrapDoor.DoorHalf.TOP), 1, 3, 3, structureBoundingBoxIn);
		this.fillWithBlocks(worldIn, structureBoundingBoxIn, 1, 1, 3, 1, 2, 3, ladders, ladders, false);
        
		//Furniture
        this.setBlockState(worldIn, stairs_seat, 1, 4, 2, structureBoundingBoxIn);
        this.setBlockState(worldIn, fence, 1, 4, 3, structureBoundingBoxIn);
        this.setBlockState(worldIn, Blocks.WOODEN_PRESSURE_PLATE.getDefaultState(), 1, 5, 3, structureBoundingBoxIn);
        this.placeTorch(worldIn, EnumFacing.NORTH, 2, 6, 1, structureBoundingBoxIn);
        
        this.spawnVillagers(worldIn, structureBoundingBoxIn, 2, 4, 2, 1);
        
		return true;
	}
	
	public static enum CreationHandler implements IVillageCreationHandler
	{
		INSTANCE;

		@Override
		public PieceWeight getVillagePieceWeight(Random random, int i)
		{
			return new PieceWeight(SpyVillageHouse.class, 2, 1);
		}

		@Override
		public Class<?> getComponentClass()
		{
			return SpyVillageHouse.class;
		}

		@Override
		public Village buildComponent(PieceWeight villagePiece, Start startPiece, List<StructureComponent> pieces, Random random, int structureMinX, int structureMinY, int structureMinZ, EnumFacing facing, int componentType)
		{
            StructureBoundingBox structureboundingbox = StructureBoundingBox.getComponentToAddBoundingBox(structureMinX, structureMinY, structureMinZ, 0, -3, 0, 5, 8, 5, facing);
            return canVillageGoDeeper(structureboundingbox) && StructureComponent.findIntersecting(pieces, structureboundingbox) == null ? new SpyVillageHouse(startPiece, componentType, structureboundingbox, facing) : null;
		}
		
	}
	
}
