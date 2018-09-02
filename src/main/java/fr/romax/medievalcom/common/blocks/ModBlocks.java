package fr.romax.medievalcom.common.blocks;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.items.ItemDeskBlock;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ObjectHolder(MedievalCommunications.MODID)
@EventBusSubscriber(modid = MedievalCommunications.MODID)
public class ModBlocks
{
	public static final BlockDesk DESK = null;
	
	@SubscribeEvent
	public static void registerBlocks(RegistryEvent.Register<Block> event)
	{
		event.getRegistry().registerAll(init(new BlockDesk().setHardness(2.5F), "desk"));
	}

	public static void registerTiles()
	{
		registerTile(TileEntityDeskMain.class, "tile_desk_main");
		registerTile(TileEntityDeskSec.class, "tile_desk_sec");
	}

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(init(new ItemDeskBlock(DESK), DESK));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		registerItemRenders(DESK);
	}
	
	protected static Block init(Block block, String name)
	{
		block.setRegistryName(MedievalCommunications.MODID, name);
		block.setTranslationKey(name);
		block.setCreativeTab(MedievalCommunications.TAB);
		return block;
	}
	
	protected static void registerTile(Class<? extends TileEntity> tileClass, String name)
	{
		//Use this deprecated method for compatibility with older versions of forge
		GameRegistry.registerTileEntity(tileClass, MedievalCommunications.MODID + ":" + name);
	}
	
	protected static Item init(Item item, Block block)
	{
		item.setRegistryName(block.getRegistryName());
		item.setTranslationKey(block.getRegistryName().getPath());
		item.setCreativeTab(MedievalCommunications.TAB);
		return item;
	}

	@SideOnly(Side.CLIENT)
	protected static void registerItemRenders(Block... blocks)
	{
		for (Block block : blocks)
		{
			Item item = Item.getItemFromBlock(block);
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
}
