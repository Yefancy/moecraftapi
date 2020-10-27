package net.moecraft.Net;

import com.google.gson.Gson;

import net.minecraft.crash.CrashReport;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.text.TextComponentString;
import net.moecraft.MoeCraftAPIMod;
import net.moecraft.Utils.InfoProtocol;
import net.moecraft.Utils.ServerInfo;
import net.moecraft.Interfaces.IResult;

public class MoeServer {
	private static MoeSocket server;
	public static final Gson GSON = new Gson();

	public static void CreateServer(int port) {
		if (IsInit())
			return;
		server = new MoeSocket(port);
		if (!server.BlindPort()) {
			MoeCraftAPIMod.logger.info("MoeAPI faild to blind port: " + port + " please change a blind port!!");
			return;
		}
		System.out.println("MoeAPI blind port: " + port);
		server.BeginAccept(r -> {
			AcceptCallback(r);
		});
	}

	public static MoeSocket GetServer() {
		return server;
	}

	public static boolean IsInit() {
		return server != null;
	}
	
	public static void SendRuntimeCR(CrashReport cr) {
		MoeCraftAPIMod.logger.info("SendRTCrashReport");
		server.Send(GSON.toJson(new InfoProtocol(5, -1, cr.getCompleteReport())));
	}

	private static void AcceptCallback(IResult r) {
		if (!r.GetResult()) {
			MoeCraftAPIMod.logger.info(r.GetInfo());
			MoeCraftAPIMod.logger.info("MoeAPI accept error. If still error, please reboot or change a blind port!!");
			server.BeginAccept(r_ -> {
				AcceptCallback(r_);
			});
			return;
		}
		MoeCraftAPIMod.logger.info("MoeAPI accepted client IP:" + r.GetInfo());
		server.BeginRecive(r_ -> {
			ReciveCallback(r_);
		});
	}

	private static void ReciveCallback(IResult r) {
		if (!r.GetResult()) {
			MoeCraftAPIMod.logger.info("MoeAPI client " + r.GetInfo());
			server.BeginAccept(r_ -> {
				AcceptCallback(r_);
			});
			return;
		}
		MoeCraftAPIMod.logger.info("MoeAPI Recive msg from client:" + r.GetInfo());
		PackageDecoder(r.GetInfo());
	}

	private static void SendCallback(IResult r) {

	}

	private static void PackageDecoder(String json) {
		try {
			final InfoProtocol pkg = GSON.fromJson(json, InfoProtocol.class);
			switch (pkg.type) {
			case 0: //request
				if(pkg.info.equals("tps")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: tps");
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetTPS()))); //respond
				}
				else if(pkg.info.equals("onlineList")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: onlineList");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetOnlineList()))); //respond
				}
				else if(pkg.info.equals("pvpRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: pvpRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetPVPList(5)))); //respond
				}
				else if(pkg.info.equals("pveRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: pveRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetPVEList(5)))); //respond
				}
				else if(pkg.info.equals("onlineTimeRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: onlineTimeRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetOnlineTimeList(5)))); //respond
				}
				else if(pkg.info.equals("deathsRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: deathsRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetDeathList(5)))); //respond
				}
				else if(pkg.info.equals("walkRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: walkRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetDistanceWalkedList(5)))); //respond
				}
				else if(pkg.info.equals("DPSRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: DPSRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetDPSList(5)))); //respond
				}
				else if(pkg.info.equals("TPSRank")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: TPSRank");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetTPSList(5)))); //respond
				}
				else if(pkg.info.equals("Time")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: Time");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetTime()))); //respond
				}
				else if(pkg.info.equals("CRList")) {
					MoeCraftAPIMod.logger.info("MoeAPI Recive request: CRList");					
					server.BeginSend(GSON.toJson(new InfoProtocol(1, pkg.id, ServerInfo.GetCRList()))); //respond
				}
				break;
			case 1: //respond
				break;
			case 2: //inform
				break;	
			case 3: //chat
				MoeCraftAPIMod.INSTANCE.getPlayerList().sendMessage(new TextComponentString(pkg.info));
				break;
			case 4: //cmd
				String result = ((DedicatedServer)MoeCraftAPIMod.INSTANCE).handleRConCommand(pkg.info);
				server.BeginSend(GSON.toJson(new InfoProtocol(4, pkg.id, result))); //respond
				break;
			case 5: //CR
				String cr = ServerInfo.GetCR(pkg.info);
				server.BeginSend(GSON.toJson(new InfoProtocol(5, pkg.id, cr))); //respond
				break;
			default:
				MoeCraftAPIMod.logger.info("MoeAPI Recive undecode msg from client");
			}
		} catch (Exception e) {
			MoeCraftAPIMod.logger.info("MoeAPI json decoder exception");
		}
	}
}
