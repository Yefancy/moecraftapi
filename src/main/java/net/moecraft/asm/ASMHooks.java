package net.moecraft.asm;

import net.minecraft.crash.CrashReport;
import net.minecraft.world.WorldServer;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.Net.MoeServer;

@SuppressWarnings("unused")
public final class ASMHooks {

	public static void whenPlayersWake(WorldServer world) {
		MoeServer.GetServer().BeginSend("wake up test");
	}
	
	public static void sendCrashReport(CrashReport cr) {
		MoeServer.SendRuntimeCR(cr);
	}
}