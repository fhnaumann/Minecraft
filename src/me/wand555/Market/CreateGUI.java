package me.wand555.Market;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

public class CreateGUI {
	
	//get Inventory
	public static Inventory getInv(Player player) {
		return player.getOpenInventory().getTopInventory();
	}
	
	public static void createGUI(Player player, String invname) {
		Inventory gui = null;
		// /market
		if(invname.equalsIgnoreCase("Overview")) {
			gui = Market.getInstance().getServer().createInventory(null, 9, ChatColor.AQUA + "Overview");
			for(int i=0; i<gui.getSize(); i++) {
				if(i==1) {
					gui.setItem(i, createItem(Material.CHEST, 1, "View Listings", "View all listings currently on the market!"));
				}
				else if(i==3) {
					gui.setItem(i, createItem(Material.END_PORTAL_FRAME, 1, "Create Listing", "Create your own listing!"));
				}
				else if(i==5) {
					gui.setItem(i, createItem(Material.ENDER_CHEST, 1, "Your Listings", "Fill up your storage & edit/delete listings!"));
				}
				else if(i==7) {
					gui.setItem(i, createItem(Material.LIGHT_BLUE_SHULKER_BOX, 1, "Your Storage", "Your personal storage space!"));
				}
				else {
					gui.setItem(i, createGlass());
				}
			}
		}
		else if(invname.equalsIgnoreCase("View Listings")) {
			gui = Market.getInstance().getServer().createInventory(null, 54, ChatColor.AQUA + "View Listings");
			for(int i=0; i<gui.getSize(); i++) {
				if(i==45) {
					gui.setItem(i, createItem(Material.NETHER_STAR, GUIListener.getCurrentPage().get(player.getUniqueId()), "Previous Page", ""));
				}
				else if(i==49) {
					gui.setItem(i, goBackBarrier());
				}
				else if(i==53) {
					gui.setItem(i, createItem(Material.NETHER_STAR, GUIListener.getCurrentPage().get(player.getUniqueId()), "Next Page", ""));
				}
				else if(i>=36 && i<=53) {
					gui.setItem(i, createGlass());
				}
				else {
					gui.setItem(i, createGlass());
				}
			}
			
			int pages = ConfigMethods.determinePages(player.getUniqueId());
			int cpage = GUIListener.getCurrentPage().get(player.getUniqueId());
			ArrayList<ItemStack> items = ConfigMethods.createDisplayItemStackArrayList(player.getUniqueId());
			
			int index = cpage * 36 - 36;
			int endIndex = index >= items.size() ? items.size() - 1 : index + 36;
			
			int k=0;
			for(; index < endIndex; index++) {
				if(index < items.size()) {
					gui.setItem(k, items.get(index));
					k++;
				}
				else {
					break;
				}	
			}
			
		}
		else if(invname.equalsIgnoreCase("View Listings Buy")) {
			gui = Market.getInstance().getServer().createInventory(null, 45, ChatColor.AQUA + "Buy Listing");
			for(int i=0; i<gui.getSize(); i++) {
				if(i==1) {
					gui.setItem(i, createItem(Material.WRITABLE_BOOK, 1, "Item you get", "This is the item you will receive in this trade!"));
				}
				else if(i==7) {
					gui.setItem(i, createItem(Material.WRITABLE_BOOK, 1, "Item you have", "Place here the item the listing requires!"));
				}
				else if(i==19) {
					CompleteItemAttribute comp = ConfigMethods.getListingAt(GUIListener.getClickedSlot().get(player.getUniqueId()));
					gui.setItem(i, comp.getHas());
				}
				else if(i==23) {
					CompleteItemAttribute comp = ConfigMethods.getListingAt(GUIListener.getClickedSlot().get(player.getUniqueId()));
					gui.setItem(i, comp.getWants());
				}
				else if(i==25) {
					//load item
				}
				else if(i==36) {
					gui.setItem(i, goBackBarrier());
				}
				else if(i>=38 && i<=44) {
					gui.setItem(i, createItem(Material.LIGHT_GRAY_CONCRETE, 1, ChatColor.GRAY + "Enter item", "Place the item the listing requires!"));
				}
				else {
					gui.setItem(i, createGlass());
				}
			}
		}
		else if(invname.equalsIgnoreCase("Create Listings")) {
			gui = Market.getInstance().getServer().createInventory(null, 45, ChatColor.AQUA + "Create Listings");
			for(int i=0; i<gui.getSize(); i++) {		
				if(i==1) {
					gui.setItem(i, createItem(Material.WRITABLE_BOOK, 1, "Item you have", "Place here the item you wish to sell!"));
				}
				else if(i==7) {
					gui.setItem(i, createItem(Material.WRITABLE_BOOK, 1, "Item you want", "Place here the item you wish to buy!"));
				}
				else if(i==19) {
					//load item
				}
				else if(i==25) {
					//load item
				}
				else if(i==36) {
					gui.setItem(i, goBackBarrier());
				}
				else if(i>=38 && i<=44) {
					gui.setItem(i, createItem(Material.LIGHT_GRAY_CONCRETE, 1, ChatColor.GRAY + "Enter items", "Place the item you have and want first!"));
				}
				else {
					gui.setItem(i, createGlass());
				}
			}
		}
		else if(invname.equalsIgnoreCase("Your Listings")) {
			gui = Market.getInstance().getServer().createInventory(null, 54, ChatColor.AQUA + "Your Listings");	
			for(int i=0; i<gui.getSize(); i++) {
				if(i==45) {
					gui.setItem(i, createItem(Material.NETHER_STAR, GUIListener.getCurrentPage().get(player.getUniqueId()), "Previous Page", ""));
				}
				else if(i==49) {
					gui.setItem(i, goBackBarrier());
				}
				else if(i==53) {
					gui.setItem(i, createItem(Material.NETHER_STAR, GUIListener.getCurrentPage().get(player.getUniqueId()), "Next Page", ""));
				}
				else {
					gui.setItem(i, createGlass());
				}
				
				int cpage = GUIListener.getCurrentPage().get(player.getUniqueId());
				ArrayList<ItemStack> items = ConfigMethods.createPersonalDisplayItemStackArrayList(player.getUniqueId());
				
				int index = cpage * 36 - 36;
				int endIndex = index >= items.size() ? items.size() - 1 : index + 36;
				
				int k=0;
				for(; index < endIndex; index++) {
					if(index < items.size()) {
						gui.setItem(k, items.get(index));
						k++;
					}
					else {
						break;
					}	
				}
				
			}
		}
		else if(invname.equalsIgnoreCase("Your Listings Edit")) {
			gui = Market.getInstance().getServer().createInventory(null, 45, ChatColor.AQUA + "Edit your Listing");
			for(int i=0; i<gui.getSize(); i++) {
				if(i==36) {
					gui.setItem(i, goBackBarrier());
				}
				else if(i==44) {
					gui.setItem(i, createItem(Material.TNT, 1, ChatColor.DARK_RED + "Delete Listing", "Deletes this listing immediately!"));
				}
				else if(i>=27 && i<=44) {
					gui.setItem(i, createGlass());
				}
			}
			ConfigMethods.createPersonalDisplayItemStackArrayList(player.getUniqueId());
			CompleteItemAttribute comp = ConfigMethods.getPersonalListingAt(player.getUniqueId(), GUIListener.getClickedSlot().get(player.getUniqueId()));
			if(comp.getType().equalsIgnoreCase("Admin Listing")) {
				
			}
			else {
				ArrayList<ItemStack> storage = comp.getStorageContents();
				for(int i=0; i<storage.size(); i++) {
					if(storage.get(i) != null) {
						gui.setItem(i, storage.get(i));
					}
				}
			}
			
		}
		else if(invname.equalsIgnoreCase("Chest")) {
			gui = Market.getInstance().getServer().createInventory(null, 45, ChatColor.AQUA + "Your Storage");
			for(int i=0; i<gui.getSize(); i++) {
				if(i==36) {
					gui.setItem(i, goBackBarrier());
				}
				else if(i>=27 && i<=44) {
					gui.setItem(i, createGlass());
				}				
			}
			
			//Items in storage laden
			ArrayList<ItemStack> contents = ConfigMethods.getChestFrom(player.getUniqueId());
			for(int i=0; i<contents.size(); i++) {
				if(contents.get(i) != null) {
					gui.setItem(i, contents.get(i));
				}
			}
			
		}
		
		
		
		//open inv
		player.openInventory(gui);
		
	}
	
	
	//change live inventory
	public static void changeInvContents(Player player, String invname, String color) {
		Inventory gui = null;
		
		if(invname.equalsIgnoreCase("Create Listings")) {
			gui = player.getOpenInventory().getTopInventory();
			for(int i=38; i<gui.getSize(); i++) {
				gui.clear(i);
			}
			if(color.equalsIgnoreCase("grey")) {
				for(int i=38; i<gui.getSize(); i++) {
					gui.setItem(i, createItem(Material.LIGHT_GRAY_CONCRETE, 1, ChatColor.GRAY + "Enter items", "Place the item you have and want first!"));
				}
			}
			else if(color.equalsIgnoreCase("orange")) {
				for(int i=38; i<gui.getSize(); i++) {
					gui.setItem(i, createItem(Material.ORANGE_CONCRETE, 1, ChatColor.GOLD + "Enter items", "Place the second item!"));
				}				
			}
			else if(color.equalsIgnoreCase("green")) {
				for(int i=38; i<gui.getSize(); i++) {
					gui.setItem(i, createItem(Material.GREEN_CONCRETE, 1, ChatColor.GREEN + "Finalize listing", "Click here to confirm and finalize your listing!"));
				}
			}
		}
		else if(invname.equalsIgnoreCase("View Listings Buy")) {
			gui = player.getOpenInventory().getTopInventory();
			for(int i=38; i<gui.getSize(); i++) {
				gui.clear(i);
			}
			if(color.equalsIgnoreCase("grey")) {
				for(int i=38; i<gui.getSize(); i++) {
					gui.setItem(i, createItem(Material.LIGHT_GRAY_CONCRETE, 1, ChatColor.GRAY + "Enter item", "Place the item the listing requires!"));
				}
			}
			else if(color.equalsIgnoreCase("green")) {
				for(int i=38; i<gui.getSize(); i++) {
					gui.setItem(i, createItem(Material.GREEN_CONCRETE, 1, ChatColor.GREEN + "Finalize payment", "Click here to confirm and buy from this listing!"));
				}
			}
		}
		new BukkitRunnable() {
			
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				player.updateInventory();
				
			}
		}.runTaskLater(Market.getInstance(), 1L);
	}
	
	
	public static String goBack(Player player) {
		if(GUIListener.getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Overview")) {
			return "";
		}
		else if(GUIListener.getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Create Listings")) {
			return "Overview";
		}
		return "";
	}
	
	
	//create any item
	private static ItemStack createItem(Material mat, int amount, String displayname, String lore) {
		ItemStack item = new ItemStack(mat, amount);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(displayname);
		ArrayList<String> desc = new ArrayList<String>();
		desc.add(lore);
		meta.setLore(desc);
		item.setItemMeta(meta);
		return item;
	}
	
	//create Glass in inventory
	private static ItemStack createGlass() {
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(" ");
		item.setItemMeta(meta);
		return item;
	}
	
	//create Go-Back Item
	private static ItemStack goBackBarrier() {
		ItemStack item = new ItemStack(Material.BARRIER);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.RED + "Go Back");
		ArrayList<String> desc = new ArrayList<String>();
		desc.add("Takes you one step back!");
		meta.setLore(desc);
		item.setItemMeta(meta);
		return item;
	}
	
	//create Confirm in Inventory
	private static ItemStack createConfirm() {
		ItemStack item = new ItemStack(Material.GREEN_CONCRETE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(ChatColor.GREEN + "Confirm");
		ArrayList<String> desc = new ArrayList<String>();
		desc.add("Confirm and finalize your process!");
		meta.setLore(desc);
		item.setItemMeta(meta);
		return item;
	}
}
