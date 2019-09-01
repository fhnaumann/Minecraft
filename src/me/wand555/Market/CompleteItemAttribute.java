package me.wand555.Market;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import org.bukkit.inventory.ItemStack;

public class CompleteItemAttribute {

	String id;
	ItemStack has;
	ItemStack wants;
	int stock;
	String type;
	UUID uuid;
	String date;
	ArrayList<ItemStack> storagecontents;
	
	public CompleteItemAttribute(String id, ItemStack has, ItemStack wants, int stock, String type, UUID uuid, String date, ArrayList<ItemStack> storagecontents) {
		this.id = id;
		this.has = has;
		this.wants = wants;
		this.stock = stock;
		this.type = type;
		this.uuid = uuid;
		this.date = date;
		this.storagecontents = storagecontents;
	}
	
	public String getID() {
		return id;
	}
	
	public ItemStack getHas() {
		return has;
	}
	
	public ItemStack getWants() {
		return wants;
	}
	
	public int getStock() {
		return stock;
	}
	
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	public String getType() {
		return type;
	}
	
	public UUID getUUID() {
		return uuid;
	}
	
	public String getDate() {
		return date;
	}
	
	public ArrayList<ItemStack> getStorageContents() {
		return storagecontents;
	}
	
	public void setStorageContents(ArrayList<ItemStack> storage) {
		this.storagecontents = storage;
	}
}
