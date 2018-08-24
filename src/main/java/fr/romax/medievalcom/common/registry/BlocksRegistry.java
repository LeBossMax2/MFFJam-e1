package fr.romax.medievalcom.common.registry;

import java.util.LinkedHashMap;

import com.google.common.base.Function;

import fr.romax.medievalcom.MedievalCommunications;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = MedievalCommunications.MODID)
public class BlocksRegistry {

	public static final LinkedHashMap<Block, Item> BLOCKS = new LinkedHashMap<>();

	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event) {
		BLOCKS.keySet().forEach(event.getRegistry()::register);
	}

	public static void register(Block block, String name) {
		register(block, ItemBlock::new, name);
	}

	public static void register(Block block, Class<? extends TileEntity> tileEntity, String name) {
		register(block, name);
		GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(MedievalCommunications.MODID, name));
	}

	public static void register(Block block, Function<Block, Item> itemBlockSupplier, String name) {
		Item item = itemBlockSupplier.apply(block);
		block.setRegistryName(new ResourceLocation(MedievalCommunications.MODID, name));
		block.setUnlocalizedName(name);
		block.setCreativeTab(MedievalCommunications.TAB);
		if (item != null) {
			item.setRegistryName(block.getRegistryName());
			BLOCKS.put(block, item);
		}
	}

}
