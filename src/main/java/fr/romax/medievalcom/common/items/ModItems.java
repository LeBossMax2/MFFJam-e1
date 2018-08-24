package fr.romax.medievalcom.common.items;

import fr.romax.medievalcom.MedievalCommunications;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@ObjectHolder(MedievalCommunications.MODID)
@EventBusSubscriber(modid = MedievalCommunications.MODID)
public class ModItems
{
	public static final ItemDagger DAGGER = null;
	public static final ItemWrittenPaper WRITTEN_PAPER = null;
	public static final ItemMessageArrow MESSAGE_ARROW = null;
	public static final ItemSlimyPaper SLIMY_PAPER = null;

	@SubscribeEvent
	public static void registerItems(RegistryEvent.Register<Item> event)
	{
		event.getRegistry().registerAll(
			init(new ItemDagger(), "dagger"),
			init(new ItemWrittenPaper(), "written_paper"),
			init(new ItemMessageArrow(), "message_arrow"),
			init(new ItemSlimyPaper(), "slimy_paper"));
	}
	
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent event)
	{
		registerItemRenders(DAGGER, WRITTEN_PAPER, MESSAGE_ARROW, SLIMY_PAPER);
	}
	
	protected static Item init(Item item, String name)
	{
		item.setRegistryName(new ResourceLocation(MedievalCommunications.MODID, name));
		item.setUnlocalizedName(name);
		item.setCreativeTab(MedievalCommunications.TAB);
		return item;
	}

	@SideOnly(Side.CLIENT)
	protected static void registerItemRenders(Item... items)
	{
		for (Item item : items)
		{
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(item.getRegistryName(), "inventory"));
		}
	}
	
}
