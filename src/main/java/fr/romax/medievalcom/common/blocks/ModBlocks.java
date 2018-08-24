package fr.romax.medievalcom.common.blocks;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.items.ItemDeskBlock;
import fr.romax.medievalcom.common.registry.BlocksRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModBlocks {

	public static void registerBlocks() {
		// A utiliser : BlocksRegistry.register(block, name);
		BlocksRegistry.register(new BlockDesk().setHardness(2.5F), ItemDeskBlock::new, "desk");
		GameRegistry.registerTileEntity(TileEntityDeskMain.class, new ResourceLocation(MedievalCommunications.MODID, "tile_desk_main"));
		GameRegistry.registerTileEntity(TileEntityDeskSec.class, new ResourceLocation(MedievalCommunications.MODID, "tile_desk_sec"));
	}
	
}
