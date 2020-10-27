package net.moecraft.Events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.Net.MoeServer;
import net.moecraft.Utils.InfoProtocol;
import net.moecraft.Utils.StatisicsData; 

public class EventRegister {	
	
	public EventRegister()
    {
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void OnWorldLoad(Load event) {
		if(MoeCraftAPIMod.STATISTICS_DATA == null) {
			MoeCraftAPIMod.logger.info("Loading statistics data.");
			MoeCraftAPIMod.STATISTICS_DATA = new StatisicsData();
			MoeCraftAPIMod.STATISTICS_DATA.Load();
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void OnPlayerLoggedIn(PlayerLoggedInEvent event) {
		if(MoeServer.IsInit()) {
			String msg = "Player ["+event.player.getDisplayName()+"] joins in!";
			String json = MoeServer.GSON.toJson(new InfoProtocol(2, msg)); //inform
			MoeServer.GetServer().BeginSend(json);
		}
		if(MoeCraftAPIMod.STATISTICS_DATA != null) {
			MoeCraftAPIMod.STATISTICS_DATA.PlayerLogin(event.player);
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void OnPlayerLoggedOut(PlayerLoggedOutEvent event) {
		if(MoeServer.IsInit()) {
			String msg = "Player ["+event.player.getDisplayName()+"] exits out!";
			String json = MoeServer.GSON.toJson(new InfoProtocol(2, msg)); //inform
			MoeServer.GetServer().BeginSend(json);
		}
		if(MoeCraftAPIMod.STATISTICS_DATA != null) {
			MoeCraftAPIMod.STATISTICS_DATA.PlayerLogout(event.player);
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public void OnServerChatEvent(ServerChatEvent event) {
		MoeCraftAPIMod.logger.info(event.message);
		if(MoeServer.IsInit()) {
			String msg = "<"+event.username+">:" + event.message;
			String json = MoeServer.GSON.toJson(new InfoProtocol(3, msg)); //chat
			MoeServer.GetServer().BeginSend(json);
		}
	}
	
}
