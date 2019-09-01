package me.wand555.Market;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

public class BannedItems {
	
	public static ArrayList<String> isBannedItem(UUID uuid, ItemStack has, ItemStack wants) {
		if(MarketCommandExecutor.getAdminMode().containsKey(uuid)) {
			if(MarketCommandExecutor.getAdminMode().get(uuid)) {
				return null;
			}
			else {

				@SuppressWarnings("unchecked")
				ArrayList<String> items = (ArrayList<String>) Market.getInstance().getConfig().getList("Banned Items");
				ArrayList<String> banned = new ArrayList<String>();
				for(String s : items) {
					if(Material.matchMaterial(s) != null) {
						Material mat = Material.matchMaterial(s);
						if(mat == has.getType()) {
							banned.add(mat.toString());
						}
						if(mat == wants.getType()) {
							banned.add(mat.toString());
						}	
					}			
				}
				return banned.isEmpty() ?  null : banned;
			}
		}
		else {

			@SuppressWarnings("unchecked")
			ArrayList<String> items = (ArrayList<String>) Market.getInstance().getConfig().getList("Banned Items");
			ArrayList<String> banned = new ArrayList<String>();
			for(String s : items) {
				if(Material.matchMaterial(s) != null) {
					Material mat = Material.matchMaterial(s);
					if(mat == has.getType()) {
						banned.add(mat.toString());
					}
					if(mat == wants.getType()) {
						banned.add(mat.toString());
					}	
				}			
			}
			return banned.isEmpty() ?  null : banned;
		}
		
		
	}

	
	//check if folder exists
	public static void checkOrdner() {
		File file = new File(Market.getInstance().getDataFolder()+"");
		if(!(file.exists())) {
			file.mkdir();
		}
	}
	
	//saves YML
	public static void saveCustomYml(FileConfiguration cfg, File file) {
	    try {
	    cfg.save(file);
	    } catch (IOException e) {
	    e.printStackTrace();
	    }
	}
	
	
}
