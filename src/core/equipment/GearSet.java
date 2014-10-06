package core.equipment;

import core.entity.Actor;
import core.items.Item;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;

public abstract class GearSet {

	protected Item[] gear;
	protected String[] slotNames;

	public abstract String print();
	public abstract void load(String[] temp);
	public abstract String save();
	public abstract SelectBox interactMenu(int slot);
	public abstract PlainText interact(int slot, int selection, Actor source);
	
	public Item[] getGear() {
		return gear;
	}
	
	public Item getGear(int slot) {
		return gear[slot];
	}
	
	public void setGear(Item[] gear) {
		this.gear = gear;
	}
	
	public void setGear(int slot, Item item) {
		this.gear[slot] = item;
	}
	
	public String[] getSlotNames() {
		return slotNames;
	}
	
	public String getSlotName(int slot) {
		return slotNames[slot];
	}
	
	public void setSlotNames(String[] slotNames) {
		this.slotNames = slotNames;
	}
	
}
