package net.moecraft;


import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.moecraft.Utils.StatisicsData;

@Mod("moecraftapi")
public class MoeCraftAPIMod {
	// Directly reference a log4j logger.
    public static final Logger logger = LogManager.getLogger();
    public static MinecraftServer INSTANCE;
    public static StatisicsData STATISTICS_DATA;

    public MoeCraftAPIMod() {
    	ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        // some preinit code
    	logger.info("HELLO FROM PREINIT\n1\n1\n1\n1\n1\n1");
    }
}
