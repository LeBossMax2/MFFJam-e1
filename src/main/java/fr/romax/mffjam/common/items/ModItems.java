package fr.romax.mffjam.common.items;

import fr.romax.mffjam.common.registry.ItemsRegistry;
import net.minecraft.item.Item.ToolMaterial;

public class ModItems {

	public static ItemDagger dagger = new ItemDagger();
	public static ItemWritedPaper writted_paper = new ItemWritedPaper();

	
	public static void registerItems() {
		// A utiliser : ItemsRegistry.register(block, name);
		ItemsRegistry.register(dagger, "dagger");
		ItemsRegistry.register(writted_paper, "writted_paper");

	}
	
}
