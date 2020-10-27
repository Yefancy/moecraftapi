package net.moecraft.Utils;

import java.io.File;

import com.google.gson.JsonParseException;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.ServerStatisticsManager;
import net.minecraft.stats.Stat;
import net.minecraft.world.dimension.DimensionType;
import net.moecraft.MoeCraftAPIMod;

public class PlayerStatis {
	public String name = "";
	public String uuid = "";
	public int minutesPlayed = 0;
	public int deaths = 0;
	public int mobKills = 0;
	public int playerKills = 0;
	public int distanceWalked = 0;
	public int damageDealt = 0;
	public int damageTaken = 0;

	public PlayerStatis(String name, String uuid, int minutesPlayed, int deaths, int mobKills, int playerKills,
			int distanceWalked, int damageDealt, int damageTaken) {
		this.name = name;
		this.uuid = uuid;
		this.minutesPlayed = minutesPlayed;
		this.deaths = deaths;
		this.mobKills = mobKills;
		this.playerKills = playerKills;
		this.distanceWalked = distanceWalked;
		this.damageDealt = damageDealt;
		this.damageTaken = damageTaken;
	}

	public PlayerStatis(ServerPlayerEntity player) {
		name = player.getName().getString();
		uuid = player.getUniqueID().toString();
		ServerStatisticsManager f = player.getStats();
		minutesPlayed = f.getValue(Stat.PLAY_ONE_MINUTE);
		deaths = f.getValue(StatList.DEATHS);
		mobKills = f.getValue(StatList.MOB_KILLS);
		playerKills = f.getValue(StatList.PLAYER_KILLS);
		distanceWalked = f.getValue(StatList.WALK_ONE_CM);
		damageDealt = f.getValue(StatList.DAMAGE_DEALT);
		damageTaken = f.getValue(StatList.DAMAGE_TAKEN);
	}

	public PlayerStatis(String name, String uuid) {
		this.name = name;
		this.uuid = uuid;
		ServerStatisticsManager f = GetStatsManagerServer(uuid);
		if (f == null)
			return;
		minutesPlayed = f.readStat(StatList.PLAY_ONE_MINUTE);
		deaths = f.readStat(StatList.DEATHS);
		mobKills = f.readStat(StatList.MOB_KILLS);
		playerKills = f.readStat(StatList.PLAYER_KILLS);
		distanceWalked = f.readStat(StatList.WALK_ONE_CM);
		damageDealt = f.readStat(StatList.DAMAGE_DEALT);
		damageTaken = f.readStat(StatList.DAMAGE_TAKEN);
	}

	public int GetStat(StatBase sb) {
		if (sb == StatList.PLAY_ONE_MINUTE)
			return minutesPlayed;
		else if (sb == StatList.DEATHS)
			return deaths;
		else if (sb == StatList.MOB_KILLS)
			return mobKills;
		else if (sb == StatList.PLAYER_KILLS)
			return playerKills;
		else if (sb == StatList.WALK_ONE_CM)
			return distanceWalked;
		else if (sb == StatList.DAMAGE_DEALT)
			return damageDealt;
		else if (sb == StatList.DAMAGE_TAKEN)
			return damageTaken;
		return 0;
	}

	private ServerStatisticsManager GetStatsManagerServer(String uuid) {
		ServerStatisticsManager statisticsmanagerserver = null;
		File file1 = new File(MoeCraftAPIMod.INSTANCE.getWorld(DimensionType.OVERWORLD).getSaveHandler().getWorldDirectory(), "stats");
		File file2 = new File(file1, uuid + ".json");

		if (!file2.exists()) {
			return null;
		}
		if (file2.isFile()) {
			try {
				statisticsmanagerserver = new ServerStatisticsManager(MoeCraftAPIMod.INSTANCE, file2);
			} catch (JsonParseException jsonparseexception) {
				MoeCraftAPIMod.logger.error("Couldn\'t parse statistics file " + file2, jsonparseexception);
			}
		}
		return statisticsmanagerserver;
	}
}
