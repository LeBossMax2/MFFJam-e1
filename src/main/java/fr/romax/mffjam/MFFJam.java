package fr.romax.mffjam;

import fr.romax.mffjam.common.CommonProxy;
import fr.romax.mffjam.common.tab.MFFJamTab;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MFFJam.MODID, name = MFFJam.NAME, version = MFFJam.VERSION)
public class MFFJam {

	public static final String MODID = "mffjam1";
	public static final String NAME = "MFFJam1";
	public static final String VERSION = "Indev-1.0";
	
	public static final String CLIENT_PROXY = "fr.romax.mffjam.client.ClientProxy";
	public static final String SERVER_PROXY = "fr.romax.mffjam.server.ServerProxy";

	@Instance
	public static MFFJam instance;
	
	@SidedProxy(clientSide = CLIENT_PROXY, serverSide = SERVER_PROXY)
	private static CommonProxy proxy;
	
	public static CreativeTabs TAB = new MFFJamTab("mffjamtab");
	
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
