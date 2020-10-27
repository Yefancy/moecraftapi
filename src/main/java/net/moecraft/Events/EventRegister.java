package net.moecraft.Events;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.world.WorldEvent.Load;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.Net.MoeServer;
import net.moecraft.Net.MoeSocket;
import net.moecraft.Utils.InfoProtocol;
import net.moecraft.Utils.StatisicsData;

@Mod.EventBusSubscriber(modid = "moecraftapi")
public final class EventRegister {	
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void OnWorldLoad(Load event) {
		if(MoeCraftAPIMod.STATISTICS_DATA == null) {
			MoeCraftAPIMod.logger.info("Loading statistics data.");
			MoeCraftAPIMod.STATISTICS_DATA = new StatisicsData();
			MoeCraftAPIMod.STATISTICS_DATA.Load();
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void OnPlayerLoggedIn(PlayerLoggedInEvent event) {
		
		if(MoeServer.IsInit()) {
			String msg = "Player["+event.player.getName()+"] joins in!";
			String json = MoeServer.GSON.toJson(new InfoProtocol(2, msg)); //inform
			System.out.println(json);
			MoeServer.GetServer().BeginSend(json);
		}
		if(MoeCraftAPIMod.STATISTICS_DATA != null && event.player instanceof EntityPlayerMP) {
			MoeCraftAPIMod.STATISTICS_DATA.PlayerLogin((EntityPlayerMP)event.player);
		}
	}
	
	@SideOnly(Side.SERVER)
	@SubscribeEvent
	public static void OnPlayerLoggedOut(PlayerLoggedOutEvent event) {
		if(MoeServer.IsInit()) {
			String msg = "Player ["+event.player.getName()+"] exits out!";
			String json = MoeServer.GSON.toJson(new InfoProtocol(2, msg)); //inform
			MoeServer.GetServer().BeginSend(json);
		}
		if(MoeCraftAPIMod.STATISTICS_DATA != null && event.player instanceof EntityPlayerMP) {
			MoeCraftAPIMod.STATISTICS_DATA.PlayerLogout((EntityPlayerMP)event.player);
		}
	}
	
	@SideOnly(Side.SERVER)
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
