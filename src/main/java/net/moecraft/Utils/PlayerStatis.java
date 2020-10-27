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
import net.minecraft.stats.StatBase;
import net.minecraft.stats.StatList;
import net.minecraft.stats.StatisticsFile;
import net.minecraft.util.IJsonSerializable;
import net.minecraft.util.TupleIntJsonSerializable;
import net.moecraft.MoeCraftAPIMod;

public class PlayerStatis {
	public String name;
	public String uuid;
	public int minutesPlayed;
	public int deaths;
	public int mobKills;
	public int playerKills;
	public int distanceWalked;
	public int damageDealt;
	public int damageTaken;
	
	public PlayerStatis(String name, String uuid, int minutesPlayed, int deaths, int mobKills, int playerKills, int distanceWalked, int damageDealt, int damageTaken) {
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
	
	public PlayerStatis(EntityPlayer player) {
		name = player.getDisplayName();
		uuid = player.getUniqueID().toString();
		StatisticsFile f = MoeCraftAPIMod.INSTANCE.getConfigurationManager().func_152602_a(player);			
		minutesPlayed = f.writeStat(StatList.minutesPlayedStat);
		deaths = f.writeStat(StatList.deathsStat);
		mobKills = f.writeStat(StatList.mobKillsStat);
		playerKills = f.writeStat(StatList.playerKillsStat);
		distanceWalked = f.writeStat(StatList.distanceWalkedStat);
		damageDealt = f.writeStat(StatList.damageDealtStat);
		damageTaken = f.writeStat(StatList.damageTakenStat);
	}
	
	public PlayerStatis(String name, String uuid) {		
		this.name = name;
		this.uuid = uuid;
		Map map = GetStatsMap(uuid);		
		minutesPlayed = GetStats(map, StatList.minutesPlayedStat);
		deaths = GetStats(map, StatList.deathsStat);
		mobKills = GetStats(map, StatList.mobKillsStat);
		playerKills = GetStats(map,StatList.playerKillsStat);
		distanceWalked = GetStats(map, StatList.distanceWalkedStat);		
		damageDealt = GetStats(map,StatList.damageDealtStat);
		damageTaken = GetStats(map, StatList.damageTakenStat);	
	}
	
	public int GetStat(StatBase sb) {
		if(sb == StatList.minutesPlayedStat)
			return minutesPlayed;
		else if(sb == StatList.deathsStat)
			return deaths;
		else if(sb == StatList.mobKillsStat)
			return mobKills;
		else if(sb == StatList.playerKillsStat)
			return playerKills;
		else if(sb == StatList.distanceWalkedStat)
			return distanceWalked;
		else if(sb == StatList.damageDealtStat)
			return damageDealt;
		else if(sb == StatList.damageTakenStat)
			return damageTaken;
		return 0;
	}
	
	private int GetStats(Map map, StatBase sb) {
		TupleIntJsonSerializable tupleintjsonserializable = (TupleIntJsonSerializable)map.get(sb);
        return tupleintjsonserializable == null ? 0 : tupleintjsonserializable.getIntegerValue();
	}
	
 	private Map GetStatsMap(String uuid)
    {
		Map map = Maps.newConcurrentMap();
        File file1 = new File(MoeCraftAPIMod.INSTANCE.worldServerForDimension(0).getSaveHandler().getWorldDirectory(), "stats");
        File file2 = new File(file1, uuid + ".json");

        if (!file2.exists())        {
            return null;
        }
        if (file2.isFile())        {
            try            {
            	map.clear();
            	map.putAll(DecodeStats(FileUtils.readFileToString(file2), file2));
            }
            catch (IOException ioexception)            {
            	MoeCraftAPIMod.logger.error("Couldn\'t read statistics file " + file2, ioexception);
            }
            catch (JsonParseException jsonparseexception)            {
            	MoeCraftAPIMod.logger.error("Couldn\'t parse statistics file " + file2, jsonparseexception);
            }
        }
        return map;
    } 
	
	private Map DecodeStats(String p_150881_1_, File field_150887_d)    {
        JsonElement jsonelement = (new JsonParser()).parse(p_150881_1_);

        if (!jsonelement.isJsonObject())        {
            return Maps.newHashMap();
        }
        else
        {
            JsonObject jsonobject = jsonelement.getAsJsonObject();
            HashMap hashmap = Maps.newHashMap();
            Iterator iterator = jsonobject.entrySet().iterator();

            while (iterator.hasNext())
            {
                Entry entry = (Entry)iterator.next();
                StatBase statbase = StatList.func_151177_a((String)entry.getKey());

                if (statbase != null)
                {
                    TupleIntJsonSerializable tupleintjsonserializable = new TupleIntJsonSerializable();

                    if (((JsonElement)entry.getValue()).isJsonPrimitive() && ((JsonElement)entry.getValue()).getAsJsonPrimitive().isNumber())
                    {
                        tupleintjsonserializable.setIntegerValue(((JsonElement)entry.getValue()).getAsInt());
                    }
                    else if (((JsonElement)entry.getValue()).isJsonObject())
                    {
                        JsonObject jsonobject1 = ((JsonElement)entry.getValue()).getAsJsonObject();

                        if (jsonobject1.has("value") && jsonobject1.get("value").isJsonPrimitive() && jsonobject1.get("value").getAsJsonPrimitive().isNumber())
                        {
                            tupleintjsonserializable.setIntegerValue(jsonobject1.getAsJsonPrimitive("value").getAsInt());
                        }

                        if (jsonobject1.has("progress") && statbase.func_150954_l() != null)
                        {
                            try
                            {
                                Constructor constructor = statbase.func_150954_l().getConstructor(new Class[0]);
                                IJsonSerializable ijsonserializable = (IJsonSerializable)constructor.newInstance(new Object[0]);
                                ijsonserializable.func_152753_a(jsonobject1.get("progress"));
                                tupleintjsonserializable.setJsonSerializableValue(ijsonserializable);
                            }
                            catch (Throwable throwable)
                            {
                                MoeCraftAPIMod.logger.warn("Invalid statistic progress in " + field_150887_d, throwable);
                            }
                        }
                    }

                    hashmap.put(statbase, tupleintjsonserializable);
                }
                else
                {
                	MoeCraftAPIMod.logger.warn("Invalid statistic in " + field_150887_d + ": Don\'t know what " + (String)entry.getKey() + " is");
                }
            }

            return hashmap;
        }
    }
}
