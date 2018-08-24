package fr.romax.medievalcom;

import fr.romax.medievalcom.common.CommonProxy;
import fr.romax.medievalcom.common.tab.MedievalTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MedievalCommunications.MODID, name = MedievalCommunications.NAME, version = MedievalCommunications.VERSION)
public class MedievalCommunications {

	public static final String MODID = "medievalcommunications";
	public static final String NAME = "MedievalCommunications";
	public static final String VERSION = "1.0";
	
	public static final String CLIENT_PROXY = "fr.romax.medievalcom.client.ClientProxy";
	public static final String SERVER_PROXY = "fr.romax.medievalcom.server.ServerProxy";

	@Instance
	public static MedievalCommunications instance;
	
	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
	private static CommonProxy proxy;
	
	public static final CreativeTabs TAB = new MedievalTab("medievalcom");
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		this.proxy.preInit(event);
	}
	
	@EventHandler
	public void initnit(FMLInitializationEvent event) {
		this.proxy.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		this.proxy.postInit(event);
	}
	
}
