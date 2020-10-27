package net.moecraft;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.server.MinecraftServer;
import net.moecraft.Proxy.ServerProxy;
import net.moecraft.Utils.StatisicsData;


@Mod(modid = MoeCraftAPIMod.MODID, version = MoeCraftAPIMod.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*")
public class MoeCraftAPIMod {
	@SidedProxy(serverSide = "net.moecraft.Proxy.ServerProxy")
	public static ServerProxy proxy;
	public static final String MODID = "moecraftapi";
    public static final String VERSION = "2.0";
    public static MinecraftServer INSTANCE;
    public static Logger logger;
    public static StatisicsData STATISTICS_DATA;
    
    @EventHandler
    void preInit( FMLPreInitializationEvent event )
    {
        proxy.preInit(event);
    }
    
    @EventHandler
    void init( FMLInitializationEvent event )
    {
        proxy.init(event);
    }
    
    @EventHandler
    void postInit( FMLPostInitializationEvent event )
    {
        proxy.postInit(event);
    }
}
