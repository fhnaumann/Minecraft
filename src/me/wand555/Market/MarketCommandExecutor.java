package me.wand555.Market;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MarketCommandExecutor implements CommandExecutor {
	private Market plugin;
	private static HashMap<UUID, Boolean> adminMode = new HashMap<UUID, Boolean>();
	
	public MarketCommandExecutor(Market plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		Player player = null;
		if(sender instanceof Player) {
			player = (Player) sender;
		}
		if(cmd.getName().equalsIgnoreCase("market")) {
			if(args.length == 0) {
				GUIListener.getClickedSlot().remove(player.getUniqueId());
				GUIListener.getCurrentPage().remove(player.getUniqueId());	
				
				GUIListener.getCurrentGUI().put(player.getUniqueId(), "Overview");
				CreateGUI.createGUI(player, "Overview");
			}
			else if(args.length == 1) {
				if(args[0].equalsIgnoreCase("admin")) {
					if(player.isOp()) {
						boolean isInMode = getAdminMode().containsKey(player.getUniqueId()) ? getAdminMode().get(player.getUniqueId()) : false;
						if(!isInMode) {
							player.sendMessage(ChatColor.GREEN + "You are now in admin mode!");
							getAdminMode().put(player.getUniqueId(), true);
						}
						else {
							player.sendMessage(ChatColor.GREEN + "You're no longer in admin mode!");
							getAdminMode().put(player.getUniqueId(), false);
						}
					}
					else {
						player.sendMessage(ChatColor.RED + "You need to be op to perform this command!");
					}
				}
				else {
					player.sendMessage(ChatColor.RED + "/market admin");
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("configrl")) {
			if(args.length == 0) {
				if(player.isOp()) {
					Market.getInstance().reloadConfig();
					player.sendMessage(ChatColor.GREEN + "Reloaded the config!");
				}
				else {
					player.sendMessage(ChatColor.RED + "You need to be op to perform this command!");
				}
			}
		}
		else if(cmd.getName().equalsIgnoreCase("market admin")) {
			
			
		}
		
		return true;
	}

	/**
	 * @return the adminMode
	 */
	public static HashMap<UUID, Boolean> getAdminMode() {
		return adminMode;
	}

}
