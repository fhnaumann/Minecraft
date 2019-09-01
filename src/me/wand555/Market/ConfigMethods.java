package me.wand555.Market;

import java.io.File;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;


public class ConfigMethods {
	private static ArrayList<CompleteItemAttribute> listings = new ArrayList<CompleteItemAttribute>();
	private static HashMap<UUID, ArrayList<CompleteItemAttribute>> persListings = new HashMap<UUID, ArrayList<CompleteItemAttribute>>();
	
	private static HashMap<UUID, ArrayList<ItemStack>> chestContents = new HashMap<UUID, ArrayList<ItemStack>>();
	
	//when listing created
	public static void addListing(UUID uuid, ItemStack has, ItemStack wants) {
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		
		ItemStack clone = has.clone();
		ArrayList<ItemStack> storage = new ArrayList<ItemStack>();
		storage.add(clone);
		if(MarketCommandExecutor.getAdminMode().containsKey(uuid)) {
			if(MarketCommandExecutor.getAdminMode().get(uuid) == true) {
				CompleteItemAttribute comp = new CompleteItemAttribute(Market.getID().toString(), clone.clone(), wants, has.getAmount(), "Admin Listing", uuid, formatter.format(date), storage);
				getListings().add(comp);
			}
			else {
				CompleteItemAttribute comp = new CompleteItemAttribute(Market.getID().toString(), clone.clone(), wants, has.getAmount(), "User Listing", uuid, formatter.format(date), storage);
				getListings().add(comp);
			}
		}
		else {
			CompleteItemAttribute comp = new CompleteItemAttribute(Market.getID().toString(), clone.clone(), wants, has.getAmount(), "User Listing", uuid, formatter.format(date), storage);
			getListings().add(comp);
		}
		
		Market.setID(Market.getID() + 1);
		System.out.println(has + "addlisting has");
		System.out.println(wants + "addlisting wants");
		
		
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
				if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("View Listings")) {
					//GUIListener.getCurrentPage().put(p.getUniqueId(), 1);
					GUIListener.getCurrentGUI().put(p.getUniqueId(), "View Listings");
					CreateGUI.createGUI(p, GUIListener.getCurrentGUI().get(p.getUniqueId()));
					
					new BukkitRunnable() {
						
						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							p.updateInventory();
							
						}
					}.runTaskLater(Market.getInstance(), 1L);
				}
			}
		}
	}
	
	//when listing deleted
	public static void deleteListing(UUID uuid, int slotnumber) {
		CompleteItemAttribute comp = getPersonalListingAt(uuid, slotnumber);
		listings.remove(comp);
		persListings.get(uuid).remove(slotnumber);
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
				if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("View Listings")) {
					//GUIListener.getCurrentPage().put(p.getUniqueId(), 1);
					GUIListener.getCurrentGUI().put(p.getUniqueId(), "View Listings");
					CreateGUI.createGUI(p, GUIListener.getCurrentGUI().get(p.getUniqueId()));
					
					new BukkitRunnable() {
						
						@SuppressWarnings("deprecation")
						@Override
						public void run() {
							p.updateInventory();
							
						}
					}.runTaskLater(Market.getInstance(), 1L);
					
				}
				else if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("View Listings Buy")) {
					p.closeInventory();
					p.sendMessage(ChatColor.DARK_RED + "Looks like the owner of this listing has deleted this listing!");
				}
			}
		}
		
	}
	
	//when a player buys from a listing
	public static void onBought(int slotnumber) {
		CompleteItemAttribute comp = getListingAt(slotnumber);
		System.out.println(comp.getHas() + "has");
		System.out.println(comp.getWants() + "wants");
		if(comp.getType().equalsIgnoreCase("Admin Listing")) {
			
		}
		else {
			ArrayList<ItemStack> storage = comp.getStorageContents();
			Inventory storageInv  = Market.getInstance().getServer().createInventory(null, 27, "");
			for(int i=0; i<storage.size(); i++) {
				if(storage.get(i) != null) {
					storageInv.addItem(storage.get(i));
				}			
			}
			storage.clear();
			storageInv.removeItem(comp.getHas().clone());
			for(int i=0; i<storageInv.getSize(); i++) {
				if(storageInv.getItem(i) != null) {
					storage.add(storageInv.getItem(i));
				}			
			}
			@SuppressWarnings("unchecked")
			ArrayList<ItemStack> clonestorage = (ArrayList<ItemStack>) storage.clone();
			comp.setStorageContents(clonestorage);
			
			ArrayList<ItemStack> ownerChest = getChestFrom(comp.getUUID());
			ownerChest.add(comp.getWants().clone());
			getChestContents().put(comp.getUUID(), ownerChest);
			
			
			//wenn owner gerade in storage ist -> refreshen
			for(Player p : Bukkit.getOnlinePlayers()) {
				if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
					if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("Your Listings")) {
						//GUIListener.getCurrentPage().put(p.getUniqueId(), 1);
						GUIListener.getCurrentGUI().put(p.getUniqueId(), "Your Listings");
						CreateGUI.createGUI(p, GUIListener.getCurrentGUI().get(p.getUniqueId()));
						
						new BukkitRunnable() {
							
							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								p.updateInventory();
								
							}
						}.runTaskLater(Market.getInstance(), 1L);
					}
					else if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("Your Listings Edit")) {
						GUIListener.getCurrentGUI().put(p.getUniqueId(), "Your Listings Edit");
						CreateGUI.createGUI(p, GUIListener.getCurrentGUI().get(p.getUniqueId()));
						
						new BukkitRunnable() {
							
							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								p.updateInventory();
								
							}
						}.runTaskLater(Market.getInstance(), 1L);
					}
					else if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("Your Storage")) {
						GUIListener.getCurrentGUI().put(p.getUniqueId(), "Your Storage");
						CreateGUI.createGUI(p, GUIListener.getCurrentGUI().get(p.getUniqueId()));
						
						new BukkitRunnable() {
							
							@SuppressWarnings("deprecation")
							@Override
							public void run() {
								p.updateInventory();
								
							}
						}.runTaskLater(Market.getInstance(), 1L);
					}
				}
			}
		}
		
		
		
	}

	
	private static ArrayList<CompleteItemAttribute> order(ArrayList<CompleteItemAttribute> listings) {
		listings.trimToSize();
		ArrayList<CompleteItemAttribute> finished = listings;
		
		for(int i=0; i<listings.size(); i++) {
			if(i > 0) {
				if(listings.get(i).getType().equalsIgnoreCase("Admin Listing")) {
					
					
					for(int k=i; k>0; k--) {
						Collections.swap(finished, k, k-1);
						//finished.add(k-1, listings.get(k));
						if(k < finished.size()) {
							//finished.remove(k+1);
						}
						
					}
					
				}
				else {
					//System.out.println(finished.size() + "  " + i);
					finished.set(i, listings.get(i));
				}
			}
		}
		return finished;
	}
	
	//creates ItemStack to display
	public static ArrayList<ItemStack> createDisplayItemStackArrayList(UUID uuid) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		ArrayList<CompleteItemAttribute> clone = (ArrayList<CompleteItemAttribute>) getListings().clone();
		ArrayList<CompleteItemAttribute> finished = order(clone);
		//test = order(getListings(), finished);
		for(CompleteItemAttribute comp : finished) {
			getListings().set(finished.indexOf(comp), comp);
		}
		
		
		
		for(CompleteItemAttribute s : getListings()) {
			if(s.getType().equalsIgnoreCase("Admin Listing")) {
				ItemStack item = s.getHas().clone();
				ItemMeta meta = item.getItemMeta();
				ArrayList<String> desc = new ArrayList<String>();			
				desc.add(ChatColor.AQUA + "WANTS: " + ChatColor.RED + s.getWants());
				desc.add(ChatColor.AQUA + "Stock: " + ChatColor.RED + "Infinte");
				desc.add(ChatColor.AQUA + "Type: " + ChatColor.RED + s.getType());
				meta.setLore(desc);
				item.setItemMeta(meta);
				items.add(item);
			}
			else {
				ItemStack item = s.getHas().clone();
				ItemMeta meta = item.getItemMeta();
				s.setStock(getStorageAmount(s));
				
				ArrayList<String> desc = new ArrayList<String>();			
				desc.add(ChatColor.AQUA + "WANTS: " + ChatColor.RED + s.getWants());
				desc.add(ChatColor.AQUA + "Stock: " + ChatColor.RED + s.getStock());
				desc.add(ChatColor.AQUA + "Owner: " + ChatColor.RED + Bukkit.getPlayer(s.getUUID()).getName());
				desc.add(ChatColor.AQUA + "Type: " + ChatColor.RED + s.getType());
				desc.add(ChatColor.AQUA + "Created: " + ChatColor.RED + s.getDate());
				meta.setLore(desc);
				item.setItemMeta(meta);
				items.add(item);
			}
			
		}
			
		
		return items;
	}
	
	//creates ItemStack to display
	public static ArrayList<ItemStack> createPersonalDisplayItemStackArrayList(UUID uuid) {
		ArrayList<ItemStack> items = new ArrayList<ItemStack>();
		int i=0;
		//für ALLE Listings...
		ArrayList<CompleteItemAttribute> list = new ArrayList<CompleteItemAttribute>();
		for(CompleteItemAttribute s : getListings()) {
			if(s.getUUID().equals(uuid)) {
				if(s.getType().equalsIgnoreCase("Admin Listing")) {
					list.add(s);
					ItemStack item = s.getHas().clone();
					ItemMeta meta = item.getItemMeta();
					ArrayList<String> desc = new ArrayList<String>();
					desc.add(ChatColor.AQUA + "WANTS: " + ChatColor.RED + s.getWants());
					desc.add(ChatColor.AQUA + "Stock: " + ChatColor.RED + "Infinte");
					desc.add(ChatColor.AQUA + "Type: " + ChatColor.RED + s.getType());
					meta.setLore(desc);
					item.setItemMeta(meta);
					items.add(item);
					i++;
				}
				else {
					list.add(s);
					ItemStack item = s.getHas().clone();
					ItemMeta meta = item.getItemMeta();
					s.setStock(getStorageAmount(s));
					
					ArrayList<String> desc = new ArrayList<String>();			
					desc.add(ChatColor.AQUA + "WANTS: " + ChatColor.RED + s.getWants());
					desc.add(ChatColor.AQUA + "Stock: " + ChatColor.RED + s.getStock());
					desc.add(ChatColor.AQUA + "Owner: " + ChatColor.RED + Bukkit.getPlayer(s.getUUID()).getName());
					desc.add(ChatColor.AQUA + "Type: " + ChatColor.RED + s.getType());
					desc.add(ChatColor.AQUA + "Created: " + ChatColor.RED + s.getDate());
					meta.setLore(desc);
					item.setItemMeta(meta);
					items.add(item);
					i++;
				}
				
			}		
		}
		getPersListings().put(uuid, list);
		return items;
	}
	
	//does owner have enough storage space in "Your storage"?
	public static boolean enoughOwnerStorage(int slotnumber) {
		CompleteItemAttribute comp = getListingAt(slotnumber);
		ArrayList<ItemStack> storage = getChestFrom(comp.getUUID());
		Inventory storageInv = Market.getInstance().getServer().createInventory(null, 27, "");
		for(int i=0; i<storage.size(); i++) {
			if(storage.get(i) != null) {
				storageInv.addItem(storage.get(i));
			}			
		}
		
		boolean bol = storageInv.addItem(comp.getWants().clone()).isEmpty() ? true : false;
		return bol;
	}
	
	//does listing have enough storage?
	public static boolean enoughStorage(int slotnumber) {
		
		CompleteItemAttribute comp = getListingAt(slotnumber);
		ArrayList<ItemStack> storage = comp.getStorageContents();
		ItemStack has = comp.getHas().clone();
		Inventory storageInv = Market.getInstance().getServer().createInventory(null, 27, "");
		for(int i=0; i<storage.size(); i++) {
			if(storage.get(i) != null) {
				storageInv.addItem(storage.get(i));
			}			
		}
		boolean bol = storageInv.containsAtLeast(has, has.getAmount()) ? true : false;
		return bol;
	}
	
	private static int getStorageAmount(CompleteItemAttribute comp) {
		int counter = 0;
		ArrayList<ItemStack> storage = comp.getStorageContents();
		ItemStack has = comp.getHas().clone();
		for(int i=0; i<storage.size(); i++) {
			if(storage.get(i) != null) {
				if(storage.get(i).isSimilar(has)) {
					counter = counter + storage.get(i).getAmount();
				}
			}
		}
		return counter;
	}
	
	//is clicker = owner?
	public static boolean clickerEqualsOwner(UUID uuid, int slotnumber) {
		CompleteItemAttribute s = getListingAt(slotnumber);
		if(s.getUUID().equals(uuid)) {
			return true;
		}
		return false;
	}
	
	
	//is content in storage?
	public static boolean storageEmtpy(UUID uuid, int slotnumber) {
		for(CompleteItemAttribute s : getPersListings().get(uuid)) {
			if(getPersonalListingAt(uuid, slotnumber).equals(s)) {
				if(s.getType().equalsIgnoreCase("Admin Listing")) {
					return true;
				}
				else {
					ArrayList<ItemStack> storage = s.getStorageContents();
					if(storage.isEmpty()) {
						return true;
					}
					else {
						ItemStack has = s.getHas().clone();
						Inventory storageInv = Market.getInstance().getServer().createInventory(null, 27, "");
						for(int i=0; i<storage.size(); i++) {
							if(storage.get(i) != null) {
								storageInv.addItem(storage.get(i));
							}				
						}
						if(storageInv.containsAtLeast(has, 1)) {
							return false;
						}
						else {
							return true;
						}
						
					}
				
				}
			}
		}
		return false;
	}
	
	private static int getSize(UUID uuid) {
		int i=0;
		for(CompleteItemAttribute s : getListings()) {
			i++;
		}
		return i;
	}
	
	private static int getPersonalSize(UUID uuid) {
		int i=0;
		for(CompleteItemAttribute s : getListings()) {
			if(s.getUUID().equals(uuid)) {
				i++;
			}
			
		}
		return i;
	}
	
	public static CompleteItemAttribute getListingAt(Integer slotnumber) {
		return getListings().get(slotnumber);
	}
	
	public static CompleteItemAttribute getPersonalListingAt(UUID uuid, Integer slotnumber) {
		return getPersListings().get(uuid).get(slotnumber);
	}
	
	public static ArrayList<ItemStack> getChestFrom(UUID uuid) {		
		ArrayList<ItemStack> items = getChestContents().containsKey(uuid) ? getChestContents().get(uuid) : new ArrayList<ItemStack>();
		return items;
	}
	
	//determine amount of pages needed
	public static int determinePages(UUID uuid) {
		int i = (int) Math.ceil((double) getSize(uuid) / 36);
		if(i != 0) {
			return i;
		}
		else {
			return 1;
		}	
	}
	
	//determine personal amount of pages needed
		public static int determinePersonalPages(UUID uuid) {
			int i = (int) Math.ceil((double) getPersonalSize(uuid) / 36);
			if(i != 0) {
				return i;
			}
			else {
				return 1;
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




	public static ArrayList<CompleteItemAttribute> getListings() {
		return listings;
	}




	public static void setListings(ArrayList<CompleteItemAttribute> listings) {
		ConfigMethods.listings = listings;
	}
	
	public static HashMap<UUID, ArrayList<ItemStack>> getChestContents() {
		return chestContents;
	}

	/**
	 * @return the persListings
	 */
	public static HashMap<UUID, ArrayList<CompleteItemAttribute>> getPersListings() {
		return persListings;
	}

	
}
