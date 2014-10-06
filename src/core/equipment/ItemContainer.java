package core.equipment;

import java.util.ArrayList;

import core.entity.Actor;
import core.items.Item;

public class ItemContainer {

	private ArrayList<Item> items = new ArrayList<Item>();
	private boolean removeOnEmpty;
	
	public ItemContainer(Actor source) {
		if(source != null) {
			for(int x = 0; x<source.getEquipment().getPockets().getItems().size(); x++) {
				if(source.getEquipment().getPockets().getItems().get(x).getID() != 0)
					items.add(source.getEquipment().getPockets().getItems().get(x));
			}
			for(int x = 0; x<source.getEquipment().getArmor().getGear().length; x++) {
				if(source.getEquipment().getArmor().getGear(x).getID() != 0)
					items.add(source.getEquipment().getArmor().getGear(x));
			}
			for(int x = 0; x<source.getEquipment().getWeapons().getGear().length; x++) {
				if(source.getEquipment().getWeapons().getGear(x).getID() != 0)
					items.add(source.getEquipment().getWeapons().getGear(x));
			}
			for(int x = 0; x<source.getEquipment().getQuiver().getItems().size(); x++) {
				if(source.getEquipment().getQuiver().getItems().get(x).getID() != 0)
					items.add(source.getEquipment().getQuiver().getItems().get(x));
			}
			if(items.isEmpty()) {
				items.add(Item.loadItem("0"));
			}
		}
	}
	
	public boolean isEmpty() {
		if(items.isEmpty())
			return true;
		
		if(items.size() == 1 && items.get(0).getID() == 0)
			return true;
		
		return false;
	}
	
	public void fillItems(String[] temp) {
		removeOnEmpty = Boolean.parseBoolean(temp[0]);
		
		for(int x = 1; x<temp.length; x++) {
			if(!temp[x].startsWith("0"))
				items.add(Item.loadItem(temp[x]));
		}
		if(items.isEmpty()) {
			items.add(Item.loadItem("0"));
		}
	}
	
	public void addItem(Item item) {
		items.add(item);
	}
	
	public void addItem(String ID) {
		items.add(Item.loadItem(ID));
	}
	
	public void removeItem(Item item) {
		items.remove(item);
	}
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public void setRemoveOnEmpty(boolean remove) {
		this.removeOnEmpty = remove;
	}
	
	public boolean isRemoveOnEmpty() {
		return removeOnEmpty;
	}
	
	public String save() {
		String data = "";
		if(items.isEmpty()) {
			items.add(Item.loadItem("0"));
		}
		data += removeOnEmpty;
		for(int x = 0; x<items.size(); x++) {
			data += ";" + items.get(x).getID() + items.get(x).saveAttributes();
		}
		
		return data;
	}
	
}
