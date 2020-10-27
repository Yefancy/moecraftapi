package net.moecraft.Proxy;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.MoeCraftAPIModConfig;
import net.moecraft.Net.MoeServer;

@Mod.EventBusSubscriber
public class ServerProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		MoeCraftAPIMod.logger = e.getModLog();
	}

	public void init(FMLInitializationEvent e) {
		// MinecraftForge.EVENT_BUS.register(new EventHandler());
		MoeCraftAPIMod.INSTANCE = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (!MoeServer.IsInit())
			MoeServer.CreateServer(MoeCraftAPIModConfig.bindPort);
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
}