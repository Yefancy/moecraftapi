package net.moecraft.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
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

	public PlayerStatis(EntityPlayerMP player) {
		name = player.getName();
		uuid = player.getUniqueID().toString();
		StatisticsManagerServer f = player.getStatFile();
		minutesPlayed = f.readStat(StatList.PLAY_ONE_MINUTE);
		deaths = f.readStat(StatList.DEATHS);
		mobKills = f.readStat(StatList.MOB_KILLS);
		playerKills = f.readStat(StatList.PLAYER_KILLS);
		distanceWalked = f.readStat(StatList.WALK_ONE_CM);
		damageDealt = f.readStat(StatList.DAMAGE_DEALT);
		damageTaken = f.readStat(StatList.DAMAGE_TAKEN);
	}

	public PlayerStatis(String name, String uuid) {
		this.name = name;
		this.uuid = uuid;
		StatisticsManagerServer f = GetStatsManagerServer(uuid);
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

	private StatisticsManagerServer GetStatsManagerServer(String uuid) {
		StatisticsManagerServer statisticsmanagerserver = null;
		File file1 = new File(MoeCraftAPIMod.INSTANCE.getWorld(0).getSaveHandler().getWorldDirectory(), "stats");
		File file2 = new File(file1, uuid + ".json");

		if (!file2.exists()) {
			return null;
		}
		if (file2.isFile()) {
			try {
				statisticsmanagerserver = new StatisticsManagerServer(MoeCraftAPIMod.INSTANCE, file2);
				statisticsmanagerserver.readStatFile();
			} catch (JsonParseException jsonparseexception) {
				MoeCraftAPIMod.logger.error("Couldn\'t parse statistics file " + file2, jsonparseexception);
			}
		}
		return statisticsmanagerserver;
	}
}
