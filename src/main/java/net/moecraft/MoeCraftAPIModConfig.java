package net.moecraft;

import net.minecraftforge.common.config.Config;

@Config(modid = "moecraftapi")
public final class MoeCraftAPIModConfig {
	
	
	@Config.Comment("MoeServer binding port!")
    @Config.Name("BindPort")
    @Config.RangeInt(min = 0, max = 65535)
    @Config.RequiresMcRestart
    public static int bindPort = 23456;
}
