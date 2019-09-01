package me.wand555.Market;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class Market extends JavaPlugin {
	private static Market plugin;
	private MarketCommandExecutor myMarketCommandExecutor;
	private static Integer ID;
	private static ArrayList<String> guis = new ArrayList<String>();
	
	public void onEnable() {
		plugin = this;
		
		myMarketCommandExecutor = new MarketCommandExecutor(this);
		getCommand("market").setExecutor(myMarketCommandExecutor);
		getCommand("configrl").setExecutor(myMarketCommandExecutor);
		new GUIListener(this);
		new GUICloseListener(this);
		loadDefaults();
		
		loadIDDefault();
		loadID();
		loadConfig();
		loadList();
	}
	
	public void onDisable() {
		ConfigMethods.checkOrdner();
		File file = new File(this.getDataFolder()+"", "ID.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		cfg.set("ID", ID);
		ConfigMethods.saveCustomYml(cfg, file);
		
		
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			p.closeInventory();
		}
		
		disableConfig();
	}
	
	//get instance
	public static Market getInstance() {
		return plugin;
	}
	
	private static void loadList() {
		ArrayList<String> strings = getGuis();
		strings.add(ChatColor.AQUA + "Overview");
		strings.add(ChatColor.AQUA + "View Listings");
		strings.add(ChatColor.AQUA + "Buy Listing");
		strings.add(ChatColor.AQUA + "Create Listings");
		strings.add(ChatColor.AQUA + "Your Listings");
		strings.add(ChatColor.AQUA + "Edit your Listing");
		strings.add(ChatColor.AQUA + "Your Storage");
	}
	
	private void loadDefaults() {
		getConfig().options().copyDefaults(true);
		ArrayList<String> banned = new ArrayList<String>();
		banned.add("Add first item here, etc.");
		getConfig().addDefault("Banned Items", banned);
		saveConfig();
	}
	
	private void loadConfig() {
		ConfigMethods.checkOrdner();
		File file = new File(this.getDataFolder()+"", "listings.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		for(String s : cfg.getKeys(false)) {
			String id = s;
			UUID uuid = UUID.fromString(cfg.getString(id + ".uuid").trim());
			String type = cfg.getString(id + ".type");
			ItemStack has = cfg.getItemStack(id + ".has");
			ItemStack wants = cfg.getItemStack(id + ".wants");
			String date = cfg.getString(id + ".date");
			@SuppressWarnings("unchecked")
			//HIER ÄNDERN		
			ArrayList<ItemStack> storage = (ArrayList<ItemStack>) cfg.getList(id + ".storage content");
			CompleteItemAttribute comp = new CompleteItemAttribute(id, has, wants, has.getAmount(), type, uuid, date, storage);
			ConfigMethods.getListings().add(comp);
			cfg.set(s, null);
		}
		//ConfigClasses.saveCustomYml(cfg, file);
		file.delete();
		
		ConfigMethods.checkOrdner();
		File storageChestFile = new File(this.getDataFolder()+"", "profits.yml");
		FileConfiguration storageChestcfg = YamlConfiguration.loadConfiguration(storageChestFile);
		
		for(String s : storageChestcfg.getKeys(false)) {
			@SuppressWarnings("unchecked")
			ArrayList<ItemStack> items = (ArrayList<ItemStack>) storageChestcfg.getList(s + ".profits");
			ConfigMethods.getChestContents().put(UUID.fromString(s.trim()), items);
		}
		storageChestFile.delete();
		
	}
	
	private void disableConfig() {
		ConfigMethods.checkOrdner();
		File file = new File(this.getDataFolder()+"", "listings.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		for(CompleteItemAttribute s : ConfigMethods.getListings()) {
			if(s != null) {
				if(s.getType().equalsIgnoreCase("Admin Listing")) {
					cfg.set(s.getID() + ".uuid", s.getUUID().toString());
					cfg.set(s.getID() + ".name", Bukkit.getPlayer(s.getUUID()));
					cfg.set(s.getID() + ".type", s.getType());
					cfg.set(s.getID() + ".has", s.getHas());
					cfg.set(s.getID() + ".wants", s.getWants());
				}
				else {
					cfg.set(s.getID() + ".uuid", s.getUUID().toString());
					cfg.set(s.getID() + ".name", Bukkit.getPlayer(s.getUUID()));
					cfg.set(s.getID() + ".type", s.getType());
					cfg.set(s.getID() + ".has", s.getHas());
					cfg.set(s.getID() + ".wants", s.getWants());
					cfg.set(s.getID() + ".date", s.getDate());
					cfg.set(s.getID() + ".storage content", s.getStorageContents().toArray());
				}
				
				
			}
				
		}
		ConfigMethods.saveCustomYml(cfg, file);
		ConfigMethods.getListings().clear();
		
		
		ConfigMethods.checkOrdner();
		File storageChestFile = new File(this.getDataFolder()+"", "profits.yml");
		FileConfiguration storageChestcfg = YamlConfiguration.loadConfiguration(storageChestFile);
		
		for(Entry<UUID, ArrayList<ItemStack>> entry : ConfigMethods.getChestContents().entrySet()) {
			storageChestcfg.set(entry.getKey() + ".profits", entry.getValue().toArray());
		}
		ConfigMethods.saveCustomYml(storageChestcfg, storageChestFile);
		ConfigMethods.getChestContents().clear();
	}

	private void loadIDDefault() {
		ConfigMethods.checkOrdner();
		File file = new File(this.getDataFolder()+"", "ID.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		
		cfg.options().copyDefaults(true);
		cfg.addDefault("ID", 1);
		ConfigMethods.saveCustomYml(cfg, file);
	}
	
	//ID related
	private void loadID() {
		ConfigMethods.checkOrdner();
		File file = new File(this.getDataFolder()+"", "ID.yml");
		FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);
		Market.setID(cfg.getInt("ID"));
		
	}
	
	//get ID
	public static Integer getID() {
		return ID;
	}
	
	//set ID
	public static void setID(Integer number) {
		ID = number;
		
	}

	/**
	 * @return the guis
	 */
	public static ArrayList<String> getGuis() {
		return guis;
	}
	
}
