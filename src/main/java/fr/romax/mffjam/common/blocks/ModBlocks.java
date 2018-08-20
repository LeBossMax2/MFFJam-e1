package fr.romax.mffjam.common.blocks;

import fr.romax.mffjam.common.items.ItemDeskBlock;
import fr.romax.mffjam.common.registry.BlocksRegistry;

public class ModBlocks {

	public static void registerBlocks() {
		// A utiliser : BlocksRegistry.register(block, name);
		BlocksRegistry.register(new BlockDesk(), ItemDeskBlock::new, "desk");
	}
	
}
