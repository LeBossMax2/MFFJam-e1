package fr.romax.mffjam.common.items;

import fr.romax.mffjam.common.registry.ItemsRegistry;

public class ModItems {

	public static ItemDagger dagger = new ItemDagger();
	public static ItemWritedPaper writted_paper = new ItemWritedPaper();
	public static ItemMessageArrow message_arrow = new ItemMessageArrow();

	
	public static void registerItems() {
		// A utiliser : ItemsRegistry.register(block, name);
		ItemsRegistry.register(dagger, "dagger");
		ItemsRegistry.register(writted_paper, "writted_paper");
		ItemsRegistry.register(message_arrow, "message_arrow");

	}
	
}
