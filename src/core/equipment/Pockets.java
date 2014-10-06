package core.equipment;

import java.util.ArrayList;

import core.entity.Actor;
import core.items.Item;
import core.items.Consumable;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class Pockets extends ItemSet {

	private int selected = 0;
	
	public Pockets() {
		items = new ArrayList<Item>();
		size = 6;
		
		for(int x = 0; x<size; x++)
			items.add(Item.loadItem("0"));
	}
	
	public PlainText useSelected(Actor source) {
		Textbox text = new PlainText(false, 1.5f, false);
		//text.setStill(true);
		text.setSource(source);
		if(items.get(selected).isConsumable()) {
			Consumable temp = Consumable.loadConsumable(items.get(selected).getID());
			temp.cycleEffects(source);
			text.buildText(temp.getFlavorText());
			if(items.get(selected).getAmount() > 1)
				items.get(selected).setAmount(items.get(selected).getAmount() - 1);
			else {
				if(selected < size - 1) {	// Rearrange selected position
					for(int x = selected + 1; x<size; x++) {
						items.set(x - 1, items.get(x));
					}
					items.set(size - 1, Item.loadItem("0"));
				} else {			
					items.set(selected, Item.loadItem("0"));
				}
				cycleSelected();
			}
		} else
			text.buildText("You can't find a way to use this.");
		
		return (PlainText) text;
	}
	
	public void cycleSelected() {
		if(selected < size - 1) {
			if(items.get(selected + 1).getID() != 0)
				selected++;
			else
				selected = 0;
		} else
			selected = 0;		
	}
	
	@Override
	public String save() {
		String save = "3;" + size;
		for(int x = 0; x<items.size(); x++) {
			save += ";" + items.get(x).getID();
			if(items.get(x).getAmount() > 0)
				save += ":" + items.get(x).getAmount();
		}
		
		return save;
	}

	@Override
	public String print() {
		String temp = "";
		for(int x = 0; x<items.size(); x++) {
			if(x == selected)
				temp += "E - ";
			temp += items.get(x).getNameColor() + items.get(x).getName() + ";";
			// Stackable items
			if(items.get(x).getAmount() > 1) {
				temp = temp.substring(0, temp.length() - 1);
				temp += " (" + items.get(x).getAmount() + ");";
			}
		}
		
		return temp;
	}

	@Override
	public void load(String[] temp) {
		items.clear();
		size = Integer.parseInt(temp[1]);
		for(int x = 0; x<size; x++) {
			if(temp[x+2] != null)
				items.add(Item.loadItem(temp[x+2]));
			else
				items.add(Item.loadItem("0"));
		}
	}

	@Override
	public SelectBox interactMenu(int slot) {
		Textbox text = new SelectBox(false);
		text.setStill(true);
		String temp = "Use;Drop;Hold";
		text.buildText(temp);
		
		return (SelectBox) text;
	}

	@Override
	public PlainText interact(int slot, int selection, Actor source) {
		Textbox text = new PlainText(false, 3.0f, false);
		text.setStill(true);
		switch(selection) {
		case(0):
			if(items.get(slot).isConsumable()) {
				Consumable temp = Consumable.loadConsumable(items.get(slot).getID());
				temp.cycleEffects(source);
				text.buildText(temp.getFlavorText());
				if(items.get(slot).getAmount() > 1)
					items.get(slot).setAmount(items.get(slot).getAmount() - 1);
				else
					items.set(slot, Item.loadItem("0"));
			} else
				text.buildText("You can't find a way to use this.");
			break;
		case(1):
			if(items.get(slot).getID() == 0)
				text.buildText("You have nothing to drop!");
			else {
				text.buildText("You dropped your " + items.get(slot).getName() + ".");
				items.set(slot, Item.loadItem("0"));
			}
			break;
		case(2):
			if(items.get(slot).getID() == 0)
				text.buildText("You have nothing to hold!");
			else if(source.getEquipment().getWeapons().getGear(0).getID() != 0 && source.getEquipment().getWeapons().getGear(1).getID() != 0)
				text.buildText("Your hands are already full!");
			else {
				text.buildText("Now holding " + items.get(slot).getName() + ".");
				if(source.getEquipment().getWeapons().getGear(0).getID() == 0)
					source.getEquipment().getWeapons().setGear(0, items.get(slot));
				else
					source.getEquipment().getWeapons().setGear(1, items.get(slot));
				items.set(slot, Item.loadItem("0"));
			}
			break;
		}
		
		return (PlainText) text;
	}
	
	public int getSelected() {
		return selected;
	}

}
