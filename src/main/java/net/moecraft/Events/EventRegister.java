package net.moecraft.Events;

import java.io.IOException;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.Net.MoeServer;
import net.moecraft.Utils.InfoProtocol;
import net.moecraft.Utils.StatisicsData;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public final class EventRegister {	
	
	@SubscribeEvent
	public static void OnWorldLoad(Load event) {
		if(MoeCraftAPIMod.STATISTICS_DATA == null) {
			MoeCraftAPIMod.logger.info("Loading statistics data.");
			MoeCraftAPIMod.STATISTICS_DATA = new StatisicsData();
			MoeCraftAPIMod.STATISTICS_DATA.Load();
		}
	}
	
	@SubscribeEvent
	public static void OnPlayerLoggedIn(PlayerLoggedInEvent event) {
		if(MoeServer.IsInit()) {
			String msg = "Player["+event.getPlayer().getName()+"] joins in!";
			String json = MoeServer.GSON.toJson(new InfoProtocol(2, msg)); //inform
			System.out.println(json);
			MoeServer.GetServer().BeginSend(json);
		}
		if(MoeCraftAPIMod.STATISTICS_DATA != null && event.getPlayer() instanceof ServerPlayerEntity) {
			MoeCraftAPIMod.STATISTICS_DATA.PlayerLogin((ServerPlayerEntity)event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void OnPlayerLoggedOut(PlayerLoggedOutEvent event) {
		if(MoeServer.IsInit()) {
			String msg = "Player ["+event.getPlayer().getName()+"] exits out!";
			String json = MoeServer.GSON.toJson(new InfoProtocol(2, msg)); //inform
			MoeServer.GetServer().BeginSend(json);
		}
		if(MoeCraftAPIMod.STATISTICS_DATA != null && event.getPlayer() instanceof ServerPlayerEntity) {
			MoeCraftAPIMod.STATISTICS_DATA.PlayerLogout((ServerPlayerEntity)event.getPlayer());
		}
	}

	@SubscribeEvent
	public static void OnServerChatEvent(ServerChatEvent event) throws ClassNotFoundException, IOException {
		if(MoeServer.IsInit()) {
			String msg = "<"+event.getUsername()+">:" + event.getMessage();
			String json = MoeServer.GSON.toJson(new InfoProtocol(3, msg)); //chat
			System.out.println(json);
			MoeServer.GetServer().BeginSend(json);
		}
	}
	
}
