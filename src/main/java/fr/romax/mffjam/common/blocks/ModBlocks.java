package fr.romax.mffjam.common.blocks;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.items.ItemDeskBlock;
import fr.romax.mffjam.common.registry.BlocksRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static void registerBlocks() {
		// A utiliser : BlocksRegistry.register(block, name);
		BlocksRegistry.register(new BlockDesk().setHardness(2.5F), ItemDeskBlock::new, "desk");
		GameRegistry.registerTileEntity(TileEntityDeskMain.class, new ResourceLocation(MFFJam.MODID, "tile_desk_main"));
		GameRegistry.registerTileEntity(TileEntityDeskSec.class, new ResourceLocation(MFFJam.MODID, "tile_desk_sec"));
	}
	
}
