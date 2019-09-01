package me.wand555.Market;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class GUICloseListener implements Listener {
	
	public GUICloseListener (JavaPlugin plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
	public void onGUICloseEvent(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		if(GUIListener.getIsFinished().containsKey(player.getUniqueId())) {
			
			if(GUIListener.getIsFinished().get(player.getUniqueId()) == false) {
				if(GUIListener.getCurrentGUI().containsKey(player.getUniqueId())) {
					if(GUIListener.getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("View Listings Buy")) {
						if(GUIListener.getHasItem().get(player.getUniqueId()) != null) {
							player.getWorld().dropItemNaturally(player.getLocation(), GUIListener.getHasItem().get(player.getUniqueId()));
						}
						GUIListener.getCurrentGUI().remove(player.getUniqueId());
						GUIListener.getCurrentPage().remove(player.getUniqueId());
						GUIListener.getIsGreen().remove(player.getUniqueId());
						GUIListener.getHasItem().remove(player.getUniqueId());
						GUIListener.getIsFinished().remove(player.getUniqueId());
					}
					else if(GUIListener.getCurrentGUI().get(player.getUniqueId()).equalsIgnoreCase("Create Listings")) {
						
						if(GUIListener.getHasItem().get(player.getUniqueId()) != null) {
							player.getWorld().dropItemNaturally(player.getLocation(), GUIListener.getHasItem().get(player.getUniqueId()));
						}
						if(GUIListener.getWantsItem().get(player.getUniqueId()) != null) {
							player.getWorld().dropItemNaturally(player.getLocation(), GUIListener.getWantsItem().get(player.getUniqueId()));
						}
						
						GUIListener.getCurrentGUI().remove(player.getUniqueId());
						GUIListener.getCurrentPage().remove(player.getUniqueId());
						GUIListener.getIsGreen().remove(player.getUniqueId());
						GUIListener.getHasItem().remove(player.getUniqueId());
						GUIListener.getWantsItem().remove(player.getUniqueId());
						GUIListener.getIsFinished().remove(player.getUniqueId());
					}
				}
			}
		}
		if(GUIListener.getCurrentGUI().containsKey(player.getUniqueId())) {
			new BukkitRunnable() {
				
				@Override
				public void run() {
					if(!Market.getGuis().contains(player.getOpenInventory().getTitle())) {
						GUIListener.getCurrentGUI().remove(player.getUniqueId());
						GUIListener.getCurrentPage().remove(player.getUniqueId());
						GUIListener.getIsGreen().remove(player.getUniqueId());
						GUIListener.getHasItem().remove(player.getUniqueId());
						GUIListener.getWantsItem().remove(player.getUniqueId());
						GUIListener.getIsFinished().remove(player.getUniqueId());
					}
					
				}
			}.runTaskLater(Market.getInstance(), 2L);
			
		}
		
	}
}
