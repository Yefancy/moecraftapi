package net.moecraft.Proxy;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.MoeCraftAPIModConfig;
import net.moecraft.Events.EventRegister;
import net.moecraft.Net.MoeServer;


public class ServerProxy {
	
	public void preInit(FMLPreInitializationEvent e) {
		MoeCraftAPIMod.logger = e.getModLog();
	}

	public void init(FMLInitializationEvent e) {
		new EventRegister();
		MoeCraftAPIMod.INSTANCE = FMLCommonHandler.instance().getMinecraftServerInstance();
		if (!MoeServer.IsInit())
			MoeServer.CreateServer(MoeCraftAPIModConfig.bindPort);
	}

	public void postInit(FMLPostInitializationEvent e) {
	}
}