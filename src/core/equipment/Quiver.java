package core.equipment;

import java.util.ArrayList;

import core.entity.Actor;
import core.items.Consumable;
import core.items.Item;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class Quiver extends ItemSet {

	private int equipped = 0;
	
	public Quiver() {
		items = new ArrayList<Item>();
		size = 4;
		
		for(int x = 0; x<size; x++)
			items.add(Item.loadItem("0"));
	}
	
	public void changeAmmo() {
		if(equipped < size) {
			if(items.get(equipped + 1).getID() != 0) {
				equipped++;
			} else {
				equipped = 0;
			}
		} else
			equipped = 0;
	}
	
	public void useAmmo() {
		// Reduce ammo amount
		items.get(equipped).setAmount(items.get(equipped).getAmount() - 1);
		// Check if there's more ammo left
		if(items.get(equipped).getAmount() <= 0) {
			if(equipped < size - 1) {
				// Rearrange ammo's positions
				for(int x = equipped + 1; x<size; x++) {
					items.set(x - 1, items.get(x));
				}
				items.set(size - 1, Item.loadItem("0"));
			} else {			
				items.set(equipped, Item.loadItem("0"));
			}
			if(equipped > 0)
				equipped--;
		}
	}
	
	public Item getEquippedAmmo() {
		return items.get(equipped);
	}
	
	@Override
	public String save() {
		String save = "4;" + size;
		for(int x = 0; x<size; x++) {
			save += ";" + items.get(x).getID();
			if(items.get(x).getAmount() > 1)
				save += ":" + items.get(x).getAmount();
		}
		
		return save;
	}

	@Override
	public String print() {
		String temp = "";
		for(int x = 0; x<size; x++) {
			if(x == equipped) {
				temp += "E - " + items.get(x).getNameColor() + items.get(x).getName() + " (" + items.get(x).getAmount() + ");";
			} else
				temp += items.get(x).getNameColor() + items.get(x).getName() + " (" + items.get(x).getAmount() + ");";
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
		String temp = "Arm;Use;Drop;Hold";
		text.buildText(temp);
		
		return (SelectBox) text;
	}

	@Override
	public PlainText interact(int slot, int selection, Actor source) {
		Textbox text = new PlainText(false, 3.0f, false);
		text.setStill(true);
		switch(selection) {
		case(0):
			if(items.get(slot).getID() != 0) {
				equipped = slot;
				text.buildText("Armed " + items.get(slot).getName());
			} else {
				text.buildText("You have nothing to arm!");
			}
			break;
		case(1):
			if(items.get(slot).isUsable()) {
				Consumable temp = Consumable.loadConsumable(items.get(slot).getID());
				temp.cycleEffects(source);
				text.buildText(temp.getFlavorText());
			} else
				text.buildText("You can't find a way to use this.");
			break;
		case(2):
			if(items.get(slot).getID() == 0)
				text.buildText("You have nothing to drop!");
			else {
				text.buildText("You dropped your " + items.get(slot).getName() + ".");
				items.set(slot, Item.loadItem("0"));
			}
			break;
		case(3):
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

}
