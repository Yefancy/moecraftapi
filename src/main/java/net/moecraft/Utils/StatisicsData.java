package net.moecraft.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.StatBase;
import net.moecraft.MoeCraftAPIMod;

public class StatisicsData{
	private Map<String, String> uuidMap = new HashMap<String,String>();
	private List<PlayerStatis> offlineStats = new ArrayList();
	
	public void AddUUID(String name, String uuid) {
		if(uuidMap.put(name, uuid)!=null)
			return;
		Save();
	}
	
	public void PlayerLogin(EntityPlayerMP player) {
		AddUUID(player.getName(), player.getUniqueID().toString());
		offlineStats.removeIf(p->{
			return p.name.equals(player.getName());
		});
	}
	
	public void PlayerLogout(EntityPlayerMP player) {
		offlineStats.removeIf(p->{
			return p.name.equals(player.getName());
		});
		offlineStats.add(new PlayerStatis(player));
	}
	
	public List<PlayerStatis> GetSortList(final StatBase sb, final boolean order){
		final List<PlayerStatis> back = new ArrayList();
		List<EntityPlayerMP> server = MoeCraftAPIMod.INSTANCE.getPlayerList().getPlayers();
		server.forEach(p->{
			back.add(new PlayerStatis(p));
		});
		offlineStats.forEach(p->{
			back.add(p);
		});
		back.sort((a,b)->{
			if(!order)
				return a.GetStat(sb)-b.GetStat(sb);
			return b.GetStat(sb)-a.GetStat(sb);
		});
		return back;
	}

	
	public void Load() {
		try{
			File file = new File(MoeCraftAPIMod.INSTANCE.getWorld(0).getSaveHandler().getWorldDirectory(), "moecraft_stats.dat");
	        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
	        Map<String, String> map = (Map<String, String>)ois.readObject();
	        ois.close();
	        MoeCraftAPIMod.logger.info("Loaded statistics data file");
	        uuidMap = map;
		}catch(Exception e) {
			MoeCraftAPIMod.logger.info("Created a new statistics data file");
		}
		uuidMap.keySet().forEach(new Consumer<String>() {
			@Override
			public void accept(String key) {
				offlineStats.add(new PlayerStatis(key, uuidMap.get(key)));
			}
		});
	}
	
	public void Save() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(new File(MoeCraftAPIMod.INSTANCE.getWorld(0).getSaveHandler().getWorldDirectory(), "moecraft_stats.dat")));
	        oos.writeObject(uuidMap);
	        oos.close();
	        MoeCraftAPIMod.logger.info("Saved statistics data file");
		}catch(Exception e) {
			MoeCraftAPIMod.logger.error("Saved statistics data file failed", e);
		}
	}
	
 	
}
