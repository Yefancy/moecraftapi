package net.moecraft.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.server.MinecraftServer;
import net.minecraft.stats.StatList;
import net.minecraftforge.common.DimensionManager;
import net.moecraft.MoeCraftAPIMod;

public class ServerInfo {

	public static String GetCRList() {
		String list = "Crash Reports List: ";
		File pos = new File(new File("."), "crash-reports");
		if (!pos.exists())
			return list+"0";
		List<String> crs = getFiles(pos);
		crs.removeIf(new Predicate<String>() {
			@Override
			public boolean test(String cr) {
				return !cr.startsWith("crash-");
			}
		});
		list += crs.size();
		for (int i = crs.size()-1; i >= 0; i--)
			list += "\n" + crs.get(i);
		return list;
	}

	public static String GetCR(String time) {
		File file = new File(new File(".", "crash-reports"), "crash-" + time + "-server.txt");
		if (!file.exists()) {
			return "CR " + time + " can't be found";
		}
		try {
			FileReader reader = new FileReader(file);
			BufferedReader bReader = new BufferedReader(reader);
			StringBuilder sb = new StringBuilder();
			String s = "";
			while ((s = bReader.readLine()) != null) {
				sb.append(s + "\n");
			}
			bReader.close();
			String cr = sb.toString();
			return cr;

		} catch (Exception e) {
			return "Load CR " + time + " failed!";
		}
	}

	public static String GetTPS() {
		MinecraftServer server = MoeCraftAPIMod.INSTANCE;
		String tps = "";
		for (Integer dimId : DimensionManager.getIDs()) {
			double worldTickTime = mean(server.worldTickTimes.get(dimId)) * 1.0E-6D;
			double worldTPS = Math.min(1000.0 / worldTickTime, 20);
			tps += String.format(getDimensionPrefix(dimId) + ": Mean tick time: %.2fms. Mean TPS: %.2f\n",
					worldTickTime, worldTPS);
		}
		double meanTickTime = mean(server.tickTimeArray) * 1.0E-6D;
		double meanTPS = Math.min(1000.0 / meanTickTime, 20);
		tps += String.format("Overall: Mean tick time: %.2fms. Mean TPS: %.2f", meanTickTime, meanTPS);
		return tps;
	}

	public static String GetOnlineList() {
		MinecraftServer server = MoeCraftAPIMod.INSTANCE;
		String[] names = server.getAllUsernames();
		String list = "Online List: " + names.length;
		for (int i = 0; i < names.length; i++) {
			list += "\n   <" + names[i] + ">";
		}
		return list;
	}

	public static String GetPVEList(int top) {
		String list = "PVE Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.mobKillsStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %d", i + 1, p.name, p.mobKills);
		}
		return list;
	}

	public static String GetOnlineTimeList(int top) {
		String list = "Liver Emperor Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.minutesPlayedStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %.2fh", i + 1, p.name, (p.minutesPlayed * 1.0) / 3600 / 20);
		}
		return list;
	}

	public static String GetDeathList(int top) {
		String list = "Death Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.deathsStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %d", i + 1, p.name, p.deaths);
		}
		return list;
	}

	public static String GetPVPList(int top) {
		String list = "PVP Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.playerKillsStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %d", i + 1, p.name, p.playerKills);
		}
		return list;
	}

	public static String GetDistanceWalkedList(int top) {
		String list = "Walk Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.distanceWalkedStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %.2fm", i + 1, p.name, (p.distanceWalked * 1.0) / 100);
		}
		return list;
	}

	public static String GetDPSList(int top) {
		String list = "DPS Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.damageDealtStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %d", i + 1, p.name, p.damageDealt);
		}
		return list;
	}

	public static String GetTPSList(int top) {
		String list = "TPS Rank: ";
		List<PlayerStatis> sortList = MoeCraftAPIMod.STATISTICS_DATA.GetSortList(StatList.damageTakenStat, true);
		if (sortList.size() < top)
			top = sortList.size();
		list += "top" + top;
		for (int i = 0; i < top; i++) {
			PlayerStatis p = sortList.get(i);
			list += String.format("\n%d---%s %d", i + 1, p.name, p.damageTaken);
		}
		return list;
	}

	public static String GetTime() {
		long tmp = MoeCraftAPIMod.INSTANCE.getEntityWorld().getWorldTime();
		String time = (tmp + 6000) % 24000 / 1000 + ":" + ((tmp + 6000) % 1000) * 60 / 1000;
		return time;
	}

	private static long mean(long[] values) {
		long sum = 0L;
		for (long v : values) {
			sum += v;
		}
		return sum / values.length;
	}

	private static String getDimensionPrefix(int dimId) {
		String providerType = DimensionManager.getProvider(dimId).getDimensionName();
		return String.format("Dim %2d (%s)", dimId, providerType);
	}

	private static List<String> getFiles(File path) {
		List<String> files = new ArrayList<String>();
		File[] tempList = path.listFiles();

		for (int i = 0; i < tempList.length; i++) {
			if (tempList[i].isFile()) {
				files.add(tempList[i].getName());
			}
			if (tempList[i].isDirectory()) {
			}
		}
		return files;
	}
}
