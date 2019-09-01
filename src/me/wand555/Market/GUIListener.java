package me.wand555.Market;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Golem;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GUIListener implements Listener {

	public GUIListener(JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	private static HashMap<UUID, String> currentGUI = new HashMap<UUID, String>();
	private static HashMap<UUID, ItemStack> hasItem = new HashMap<UUID, ItemStack>();
	private static HashMap<UUID, ItemStack> wantsItem = new HashMap<UUID, ItemStack>();
	private static HashMap<UUID, Boolean> isGreen = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Boolean> isGreenBuy = new HashMap<UUID, Boolean>();
	private static HashMap<UUID, Integer> currentPage = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Integer> clickedSlot = new HashMap<UUID, Integer>();
	private static HashMap<UUID, Boolean> isFinished = new HashMap<UUID, Boolean>();
	
	@EventHandler
	public void onGUIClickEvent(InventoryClickEvent event) {
		InventoryAction action = event.getAction();
		Player player = (Player) event.getWhoClicked();
		ItemStack clicked = event.getCurrentItem();
		Inventory inv = player.getOpenInventory().getTopInventory();
		
		
		if(getCurrentGUI().containsKey(player.getUniqueId())) {
					
				if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Overview")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Overview")) {
						if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
							event.setCancelled(true);
						}
						
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							if(clicked != null) {
								event.setCancelled(true);
								
								//View Listings
								if(event.getRawSlot() == 1) {
									getCurrentPage().put(player.getUniqueId(), 1);
									getCurrentGUI().put(player.getUniqueId(), "View Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								//Create Listings
								else if(event.getRawSlot() == 3) {
									getIsGreen().put(player.getUniqueId(), false);
									getCurrentGUI().put(player.getUniqueId(), "Create Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
									getIsFinished().put(player.getUniqueId(), false);
								}
								//Your Listings
								else if(event.getRawSlot() == 5) {
									getCurrentPage().put(player.getUniqueId(), 1);
									getCurrentGUI().put(player.getUniqueId(), "Your Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								//Chest
								else if(event.getRawSlot() == 7) {
									getCurrentGUI().put(player.getUniqueId(), "Chest");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
							}
						}
					}
								
				}
				else if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("View Listings")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "View Listings")) {
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							for(InventoryAction c : InventoryAction.values()) {
								if(c.equals(action)) {
									event.setCancelled(true);
								}
							}
							
							//go back
							if(event.getRawSlot() == 49) {
								getCurrentPage().remove(player.getUniqueId());
								getCurrentGUI().put(player.getUniqueId(), "Overview");
								CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
							}
							//previous page
							else if(event.getRawSlot() == 45) {
								if(getCurrentPage().get(player.getUniqueId()) != 1) {
									getCurrentPage().put(player.getUniqueId(), getCurrentPage().get(player.getUniqueId()) - 1);
									getCurrentGUI().put(player.getUniqueId(), "View Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								else {
									player.sendMessage(ChatColor.RED + "You're on the first page!");
								}
							}
							//next page
							else if(event.getRawSlot() == 53) {
								if(ConfigMethods.determinePages(player.getUniqueId()) != 1) {
									getCurrentPage().put(player.getUniqueId(), getCurrentPage().get(player.getUniqueId()) + 1);
									getCurrentGUI().put(player.getUniqueId(), "View Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								else {
									player.sendMessage(ChatColor.RED + "Only 1 page exists!");
								}
							}
							else {
								if(clicked != null) {
									if(clicked.hasItemMeta()) {
										ItemMeta meta = clicked.getItemMeta();
										if(meta.hasLore()) {
											getClickedSlot().put(player.getUniqueId(), event.getRawSlot());
											
											CompleteItemAttribute comp = ConfigMethods.getListingAt(getClickedSlot().get(player.getUniqueId()));
											if(comp.getType().equalsIgnoreCase("Admin Listing")) {
												getIsGreen().put(player.getUniqueId(), false);	
												getCurrentGUI().put(player.getUniqueId(), "View Listings Buy");
												CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
											}
											else {
												if(ConfigMethods.clickerEqualsOwner(player.getUniqueId(), getClickedSlot().get(player.getUniqueId())) == false) {
													if(ConfigMethods.enoughStorage(getClickedSlot().get(player.getUniqueId()))) {
														if(ConfigMethods.enoughOwnerStorage(getClickedSlot().get(player.getUniqueId()))) {
															getIsGreenBuy().put(player.getUniqueId(), false);
															getIsGreen().put(player.getUniqueId(), false);	
															getCurrentGUI().put(player.getUniqueId(), "View Listings Buy");
															CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
															
														}
														else {
															player.sendMessage(ChatColor.RED + "The owner doesn't have enough storage space to store the receiving item!");
														}
													}
													else {
														player.sendMessage(ChatColor.RED + "The owner doesn't have enough in stock!");
													}
												}
												else {
													player.sendMessage(ChatColor.RED + "You cannot buy from your own listing!");
												}
											}
											
											
											
											
											
										}
									}
								}
								
							}
							
							
							
							
						}
					}
					
					
				}
				else if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("View Listings Buy")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Buy Listing")) {
						getIsFinished().put(player.getUniqueId(), false);
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							if(event.getRawSlot() != 25) {
								event.setCancelled(true);						
							}
						}
						if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
							event.setCancelled(true);
						}
						
						
						
						
						
						//go back
						if(event.getRawSlot() == 36) {
							ItemStack hasItem = getHasItem().containsKey(player.getUniqueId()) ? getHasItem().get(player.getUniqueId()) : null;
							if(hasItem == null) {
								getIsFinished().remove(player.getUniqueId());
								getClickedSlot().remove(player.getUniqueId());
								getIsGreen().remove(player.getUniqueId());
								getHasItem().remove(player.getUniqueId());
								getCurrentGUI().put(player.getUniqueId(), "View Listings");
								CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
							}
							else {
								player.sendMessage(ChatColor.RED + "Empty the slot first!");
							}
						}
						//confirm
						else if(event.getRawSlot()>=38 && event.getRawSlot()<=44) {
							if(getIsGreenBuy().get(player.getUniqueId())) {
								CompleteItemAttribute comp = ConfigMethods.getListingAt(getClickedSlot().get(player.getUniqueId()));
								
								if(comp.getType().equalsIgnoreCase("Admin Listing")) {
									
									getIsFinished().put(player.getUniqueId(), true);
									Inventory playerInv = player.getOpenInventory().getBottomInventory();
									if(!playerInv.addItem(comp.getHas()).isEmpty()) {
										player.sendMessage(ChatColor.GREEN + "Successfully bought from this listing!");
										player.sendMessage(ChatColor.RED + "Your inventory is full. The item has dropped on the ground!");
										for(ItemStack item : playerInv.addItem(comp.getHas()).values()) {
											player.getWorld().dropItemNaturally(player.getLocation(), item);								
										}
									}
									else {
										player.sendMessage(ChatColor.GREEN + "Successfully bought from this listing!");
									}
									
									new BukkitRunnable() {
										
										@Override
										public void run() {
											for(Player p : Bukkit.getOnlinePlayers()) {
												if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
													if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("Chest")) {
														GUIListener.getCurrentGUI().put(p.getUniqueId(), "Chest");
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
									}.runTaskLater(Market.getInstance(), 1L);
									
									getIsFinished().put(player.getUniqueId(), true);
									getClickedSlot().remove(player.getUniqueId());
									getIsGreen().remove(player.getUniqueId());
									getIsGreenBuy().remove(player.getUniqueId());
									getHasItem().remove(player.getUniqueId());
									getCurrentGUI().put(player.getUniqueId(), "View Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
									
								}
								else {
									//genügend Items in storage?
									if(ConfigMethods.enoughStorage(getClickedSlot().get(player.getUniqueId()))) {
										//hat owner genügend platz in Your Storage?
										if(ConfigMethods.enoughOwnerStorage(getClickedSlot().get(player.getUniqueId()))) {
											getIsFinished().put(player.getUniqueId(), true);
											ConfigMethods.onBought(getClickedSlot().get(player.getUniqueId()));
											Inventory playerInv = player.getOpenInventory().getBottomInventory();
											
											if(!playerInv.addItem(comp.getHas()).isEmpty()) {
												player.sendMessage(ChatColor.GREEN + "Successfully bought from this listing!");
												player.sendMessage(ChatColor.RED + "Your inventory is full. The item has dropped on the ground!");
												for(ItemStack item : playerInv.addItem(comp.getHas()).values()) {
													player.getWorld().dropItemNaturally(player.getLocation(), item);								
												}
											}
											else {
												player.sendMessage(ChatColor.GREEN + "Successfully bought from this listing!");
											}
											
											new BukkitRunnable() {
												
												@Override
												public void run() {
													for(Player p : Bukkit.getOnlinePlayers()) {
														if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
															if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("Chest")) {
																GUIListener.getCurrentGUI().put(p.getUniqueId(), "Chest");
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
											}.runTaskLater(Market.getInstance(), 1L);
											
											getIsFinished().put(player.getUniqueId(), true);
											getClickedSlot().remove(player.getUniqueId());
											getIsGreen().remove(player.getUniqueId());
											getHasItem().remove(player.getUniqueId());
											getCurrentGUI().put(player.getUniqueId(), "View Listings");
											CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
										}
										else {
											player.closeInventory();
											player.sendMessage(ChatColor.DARK_RED + "Looks like the owner of the storage changed his personal storage space!");
										}
									}
									else {
										player.closeInventory();
										player.sendMessage(ChatColor.DARK_RED + "Looks like the owner of the storage has removed the storage!");
									}
								}
								
								
							}
						}
						
						//store items upon entering
						for(InventoryAction c : InventoryAction.values()) {
							if(!c.equals(InventoryAction.CLONE_STACK)) {
								if(action.equals(c)) {
									new BukkitRunnable() {
										
										@Override
										public void run() {
											if(getIsGreen().containsKey(player.getUniqueId())) {
												ItemStack hasItem = inv.getItem(25);
												getHasItem().put(player.getUniqueId(), hasItem);
										
										
												if(hasItem == null) {
													getIsGreen().put(player.getUniqueId(), false);
													CreateGUI.changeInvContents(player, "View Listings Buy", "grey");
													
												}
												else if(hasItem != null) {
				
													CompleteItemAttribute comp = ConfigMethods.getListingAt(getClickedSlot().get(player.getUniqueId()));
													System.out.println(comp.getHas() + "has");
													System.out.println(comp.getWants() + "wants");
													if(hasItem.isSimilar(comp.getWants())) {
														if(hasItem.getAmount() == comp.getWants().getAmount()) {
															CreateGUI.changeInvContents(player, "View Listings Buy", "green");
															getIsGreen().put(player.getUniqueId(), true);
														}
														else {
															getIsGreen().put(player.getUniqueId(), false);
															CreateGUI.changeInvContents(player, "View Listings Buy", "grey");
														}												
													}
													else {
														getIsGreen().put(player.getUniqueId(), false);
														CreateGUI.changeInvContents(player, "View Listings Buy", "grey");
													}
												}
											}
											else {
												this.cancel();
											}
											
											
										}
									}.runTaskLater(Market.getInstance(), 1L);
									
								}	
							}
							
						}
					}
					
					
				}
				else if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Create Listings")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Create Listings")) {
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							if(event.getRawSlot() != 19 && event.getRawSlot() != 25) {
								event.setCancelled(true);						
							}
						}
						
						if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
							event.setCancelled(true);
						}
						
						//go back
						if(event.getRawSlot() == 36) {
							ItemStack hasItem = getHasItem().containsKey(player.getUniqueId()) ? getHasItem().get(player.getUniqueId()) : null;
							ItemStack wantsItem = getWantsItem().containsKey(player.getUniqueId()) ? getWantsItem().get(player.getUniqueId()) : null;
							if(hasItem == null && wantsItem == null) {
								getIsFinished().remove(player.getUniqueId());
								getIsGreen().remove(player.getUniqueId());
								getHasItem().remove(player.getUniqueId());
								getWantsItem().remove(player.getUniqueId());
								getCurrentGUI().put(player.getUniqueId(), "Overview");
								CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
							}
							else {
								player.sendMessage(ChatColor.RED + "Empty the slots first!");
							}
							
							
							
						}
						//confirm
						else if(event.getRawSlot()>=38 && event.getRawSlot()<=44) {
							if(getIsGreen().get(player.getUniqueId())) {
								
								if(BannedItems.isBannedItem(player.getUniqueId(), getHasItem().get(player.getUniqueId()), getWantsItem().get(player.getUniqueId())) == null) {
									getIsFinished().put(player.getUniqueId(), true);
									ConfigMethods.addListing(player.getUniqueId(), getHasItem().get(player.getUniqueId()), getWantsItem().get(player.getUniqueId()));
									player.sendMessage(ChatColor.GREEN + "Successfully created the listing!");
									getIsGreen().remove(player.getUniqueId());
									getHasItem().remove(player.getUniqueId());
									getWantsItem().remove(player.getUniqueId());
									getCurrentGUI().put(player.getUniqueId(), "Overview");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								else {
									ArrayList<String> string = BannedItems.isBannedItem(player.getUniqueId(), getHasItem().get(player.getUniqueId()), getWantsItem().get(player.getUniqueId()));
									for(String s : string) {
										player.sendMessage(ChatColor.RED + s + " is blacklisted!");
									}
									
								}
								
							}
							
						}
						
						//store items upon entering
						for(InventoryAction c : InventoryAction.values()) {
							if(!c.equals(InventoryAction.CLONE_STACK)) {
								if(action.equals(c)) {
									new BukkitRunnable() {
										
										@Override
										public void run() {
											if(getIsGreen().containsKey(player.getUniqueId())) {
												ItemStack hasItem = inv.getItem(19);
												getHasItem().put(player.getUniqueId(), hasItem);
												ItemStack wantsItem = inv.getItem(25);
												getWantsItem().put(player.getUniqueId(), wantsItem);
										
										
												if(hasItem == null && wantsItem == null) {
													getIsGreen().put(player.getUniqueId(), false);
													CreateGUI.changeInvContents(player, "Create Listings", "grey");
													
												}
												else if((hasItem != null && wantsItem == null) || (hasItem == null && wantsItem != null)) {
													getIsGreen().put(player.getUniqueId(), false);
													CreateGUI.changeInvContents(player, "Create Listings", "orange");
													
												}
												else if(hasItem != null && wantsItem != null) {
													CreateGUI.changeInvContents(player, "Create Listings", "green");
													getIsGreen().put(player.getUniqueId(), true);
													
												}
											}
											else {
												this.cancel();
											}
											
											
										}
									}.runTaskLater(Market.getInstance(), 1L);
									
								}	
							}
							
						}
					}
					
			
				}
				else if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Your Listings")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Your Listings")) {
						if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
							event.setCancelled(true);
						}
						
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							for(InventoryAction c : InventoryAction.values()) {
								if(c.equals(action)) {
									event.setCancelled(true);
								}
							}
							
							//go back
							if(event.getRawSlot() == 49) {
								ConfigMethods.getPersListings().remove(player.getUniqueId());
								getCurrentGUI().put(player.getUniqueId(), "Overview");
								CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));														
							}
							//previous page
							else if(event.getRawSlot() == 45) {
								if(getCurrentPage().get(player.getUniqueId()) != 1) {
									getCurrentPage().put(player.getUniqueId(), getCurrentPage().get(player.getUniqueId()) - 1);
									getCurrentGUI().put(player.getUniqueId(), "Your Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								else {
									player.sendMessage(ChatColor.RED + "You're on the first page!");
								}
							}
							//next page
							else if(event.getRawSlot() == 53) {
								if(ConfigMethods.determinePersonalPages(player.getUniqueId()) != 1) {
									getCurrentPage().put(player.getUniqueId(), getCurrentPage().get(player.getUniqueId()) + 1);
									getCurrentGUI().put(player.getUniqueId(), "Your Listings");
									CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
								}
								else {
									player.sendMessage(ChatColor.RED + "Only 1 page exists!");
								}
							}
							else {
								if(clicked != null) {
									if(clicked.hasItemMeta()) {
										ItemMeta meta = clicked.getItemMeta();
										if(meta.hasLore()) {
											getClickedSlot().put(player.getUniqueId(), event.getRawSlot());
											getCurrentGUI().put(player.getUniqueId(), "Your Listings Edit");
											CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
											
										}
									}
								}
								
							}
						}
					}
					
				}
				else if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Your Listings Edit")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Edit your Listing")) {
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							if(event.getRawSlot() >= 27 && event.getRawSlot() <= 44) {
								event.setCancelled(true);						
							}
						}
						
						if(action.equals(InventoryAction.COLLECT_TO_CURSOR)) {
							event.setCancelled(true);
						}
						
						//go back
						if(event.getRawSlot() == 36) {
							
							for(InventoryAction c : InventoryAction.values()) {
								if(action.equals(c)) {
									CompleteItemAttribute comp = ConfigMethods.getListingAt(getClickedSlot().get(player.getUniqueId()));
									if(comp.getType().equalsIgnoreCase("Admin Listing")) {
										event.setCancelled(true);
									}
									else {
										if(!ConfigMethods.getPersListings().isEmpty()) {
											if(getClickedSlot().containsKey(player.getUniqueId())) {
												ArrayList<ItemStack> storage = new ArrayList<ItemStack>();
												for(int i=0; i<27; i++) {
													if(inv.getItem(i) != null) {
														storage.add(inv.getItem(i));
													}
												}
												ConfigMethods.getPersonalListingAt(player.getUniqueId(), getClickedSlot().get(player.getUniqueId())).setStorageContents(storage);
												
												for(Player p : Bukkit.getOnlinePlayers()) {
													if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
														if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("View Listings")) {
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
									
										}				
									}

									
									
								}
							}
							
							
							getClickedSlot().remove(player.getUniqueId());
							getCurrentGUI().put(player.getUniqueId(), "Your Listings");
							CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
						}
						//delete listing
						else if(event.getRawSlot() == 44) {
							
							if(ConfigMethods.storageEmtpy(player.getUniqueId(), getClickedSlot().get(player.getUniqueId()))) {
								CompleteItemAttribute comp = ConfigMethods.getPersonalListingAt(player.getUniqueId(), getClickedSlot().get(player.getUniqueId()));
								player.getWorld().dropItemNaturally(player.getLocation(), comp.getWants());
								ConfigMethods.deleteListing(player.getUniqueId(), getClickedSlot().get(player.getUniqueId()));
								player.sendMessage(ChatColor.GREEN + "Successfully deleted this listing!");
								getClickedSlot().remove(player.getUniqueId());
								getCurrentGUI().put(player.getUniqueId(), "Your Listings");
								CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
							}
							else {
								player.sendMessage(ChatColor.RED + "Clear your storage before deleting it!");
							}
							
						}
						
						
						for(InventoryAction c : InventoryAction.values()) {
							if(action.equals(c)) {
								if(getClickedSlot().containsKey(player.getUniqueId())) {
									
									CompleteItemAttribute comp = ConfigMethods.getListingAt(getClickedSlot().get(player.getUniqueId()));
									if(comp.getType().equalsIgnoreCase("Admin Listing")) {
										event.setCancelled(true);
									}
									else {
										new BukkitRunnable() {
											
											@Override
											public void run() {
												if(!ConfigMethods.getPersListings().isEmpty()) {
													if(getClickedSlot().containsKey(player.getUniqueId())) {
														ArrayList<ItemStack> storage = new ArrayList<ItemStack>();
														for(int i=0; i<27; i++) {
															if(inv.getItem(i) != null) {
																storage.add(inv.getItem(i));
															}
														}
														ConfigMethods.getPersonalListingAt(player.getUniqueId(), getClickedSlot().get(player.getUniqueId())).setStorageContents(storage);
														
														for(Player p : Bukkit.getOnlinePlayers()) {
															if(GUIListener.getCurrentGUI().containsKey(p.getUniqueId())) {
																if(GUIListener.getCurrentGUI().get(p.getUniqueId()).equalsIgnoreCase("View Listings")) {
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
													else {
														this.cancel();
													}
													
												}
												else {
													this.cancel();
												}
												
											}
										}.runTaskLater(Market.getInstance(), 1L);
									}
								}
								
								
								
								
								
							}
						}
					}
					
					
					
					
					
				}
				else if(getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Chest")) {
					if(player.getOpenInventory().getTitle().equalsIgnoreCase(ChatColor.AQUA + "Your Storage")) {
						if(event.getRawSlot() < CreateGUI.getInv(player).getSize()) {
							if(event.getRawSlot() >= 27 && event.getRawSlot() <= 44) {
								event.setCancelled(true);						
							}
						}
						
						//go back
						if(event.getRawSlot() == 36) {
							
							for(InventoryAction c : InventoryAction.values()) {
								if(action.equals(c)) {
									ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
									
									for(int i=0; i<27; i++) {
										if(inv.getItem(i) != null) {
											contents.add(inv.getItem(i));
										}
									}		
									ConfigMethods.getChestContents().put(player.getUniqueId(), contents);
								}
							}
							
							getCurrentGUI().put(player.getUniqueId(), "Overview");
							CreateGUI.createGUI(player, getCurrentGUI().get(player.getUniqueId()));
						}
						
						for(InventoryAction c : InventoryAction.values()) {
							if(action.equals(c)) {
								new BukkitRunnable() {
									
									@Override
									public void run() {
										ArrayList<ItemStack> contents = new ArrayList<ItemStack>();
										
										for(int i=0; i<27; i++) {
											if(inv.getItem(i) != null) {
												contents.add(inv.getItem(i));
											}
										}		
										ConfigMethods.getChestContents().put(player.getUniqueId(), contents);
										
									}
								}.runTaskLater(Market.getInstance(), 1L);
							}
						}
					}
					
					
					
				}
		
		}
		
		
	}
	
	
	//get currentGUI
	public static HashMap<UUID, String> getCurrentGUI() {
		return currentGUI;
	}


	public static HashMap<UUID, ItemStack> getHasItem() {
		return hasItem;
	}


	public static HashMap<UUID, ItemStack> getWantsItem() {
		return wantsItem;
	}


	public static HashMap<UUID, Boolean> getIsGreen() {
		return isGreen;
	}


	public static HashMap<UUID, Integer> getCurrentPage() {
		return currentPage;
	}


	/**
	 * @return the clickedSlot
	 */
	public static HashMap<UUID, Integer> getClickedSlot() {
		return clickedSlot;
	}


	/**
	 * @param clickedSlot the clickedSlot to set
	 */
	public static void setClickedSlot(HashMap<UUID, Integer> clickedSlot) {
		GUIListener.clickedSlot = clickedSlot;
	}


	/**
	 * @return the isFinished
	 */
	public static HashMap<UUID, Boolean> getIsFinished() {
		return isFinished;
	}


	/**
	 * @param isFinished the isFinished to set
	 */
	public static void setIsFinished(HashMap<UUID, Boolean> isFinished) {
		GUIListener.isFinished = isFinished;
	}


	/**
	 * @return the isGreenBuy
	 */
	public static HashMap<UUID, Boolean> getIsGreenBuy() {
		return isGreenBuy;
	}
	
}
