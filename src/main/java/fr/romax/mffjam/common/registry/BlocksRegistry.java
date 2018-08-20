package fr.romax.mffjam.common.registry;

import java.util.LinkedHashMap;

import com.google.common.base.Function;

import fr.romax.mffjam.MFFJam;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = MFFJam.MODID)
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
		GameRegistry.registerTileEntity(tileEntity, new ResourceLocation(MFFJam.MODID, name));
	}

	public static void register(Block block, Function<Block, Item> itemBlockSupplier, String name) {
		Item item = itemBlockSupplier.apply(block);
		block.setRegistryName(new ResourceLocation(MFFJam.MODID, name));
		block.setUnlocalizedName(name);
		block.setCreativeTab(MFFJam.TAB);
		if (item != null) {
			item.setRegistryName(block.getRegistryName());
			BLOCKS.put(block, item);
		}
	}

}
