package net.moecraft;

import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.moecraft.Proxy.ServerProxy;
import net.moecraft.Utils.StatisicsData;

@Mod(modid = MoeCraftAPIMod.MODID, name = MoeCraftAPIMod.NAME, version = MoeCraftAPIMod.VERSION, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*")
public class MoeCraftAPIMod
{
	public static ServerProxy proxy = new ServerProxy();
    public static final String MODID = "moecraftapi";
    public static final String NAME = "MoeCraft API";
    public static final String VERSION = "2.0";
    public static MinecraftServer INSTANCE;
    public static StatisicsData STATISTICS_DATA;
    public static Logger logger;

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
