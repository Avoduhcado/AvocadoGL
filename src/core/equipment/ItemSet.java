package core.equipment;

import java.util.ArrayList;

import core.entity.Actor;
import core.items.Item;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;

public abstract class ItemSet {

	protected ArrayList<Item> items;
	protected int size;
	
	public abstract String save();
	public abstract String print();
	public abstract void load(String[] temp);
	public abstract SelectBox interactMenu(int slot);
	public abstract PlainText interact(int slot, int selection, Actor source);
	
	public ArrayList<Item> getItems() {
		return items;
	}
	
	public Item getItem(int slot) {
		return items.get(slot);
	}
	
	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}
	
	public void addItem(Item item) {
		for(int x = 0; x<size; x++) {
			if(items.get(x).getID() == 0) {
				items.set(x, item);
				break;
			}
		}
	}
	
	public void removeItem(Item item) {
		items.remove(item);
		addItem(Item.loadItem("0"));
	}
	
	public int getSize() {
		return size;
	}
	
	public void setSize(int size) {
		this.size = size;
	}
	
}
