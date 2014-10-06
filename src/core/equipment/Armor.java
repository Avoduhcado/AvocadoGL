package core.equipment;

import core.entity.Actor;
import core.entity.weapons.MeleeWeapon;
import core.entity.weapons.MagicWeapon;
import core.entity.weapons.RangedWeapon;
import core.entity.weapons.Weapon;
import core.items.Consumable;
import core.items.Item;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class Armor extends GearSet {

	public Armor() {
		gear = new Item[18];
		slotNames = new String[]{"Crown  ", "Helmet ", "Cape   ", "L Shldr", "R Shldr", "Chest  ", "Robe   ", "Neck   ",
				"L Wrist", "R Wrist", "L Hand ", "R Hand ", "L Ring ", "R Ring ", "Belt   ", "Legs   ", "L Foot ", "R Foot "};
		
		for(int x = 0; x<gear.length; x++)
			gear[x] = Item.loadItem("0");
	}
	
	public float getArmorResist(Weapon weapon, int style) {
		float resist = 0.0f;
		if(weapon instanceof MeleeWeapon) {
			for(int x = 0; x<gear.length; x++) {
				switch(style) {
				case(0):
					resist += gear[x].getResistance().getThrust();
					break;
				case(1):
					resist += gear[x].getResistance().getCrush();
					break;
				case(2):
					resist += gear[x].getResistance().getSlash();
					break;
				}
			}
		} else if(weapon instanceof MagicWeapon) {
			for(int x = 0; x<gear.length; x++) {
				switch(style) {
				case(0):
				case(1):
				case(2):
					resist += gear[x].getResistance().getMagic();
					break;
				case(3):
					resist += gear[x].getResistance().getCrush();
					break;
				}
			}
		} else if(weapon instanceof RangedWeapon) {
			for(int x = 0; x<gear.length; x++) {
				switch(style) {
				case(0):
					resist += gear[x].getResistance().getBody();
					break;
				case(1):
					resist += gear[x].getResistance().getHead();
					break;
				case(2):
					resist += gear[x].getResistance().getCripple();
					break;
				case(3):
					resist += gear[x].getResistance().getCrush();
					break;
				}
			}
		} else {
			for(int x = 0; x<gear.length; x++) {
				switch(style) {
				case(0):
					resist += gear[x].getResistance().getCrush();
					break;
				}
			}
		}
		
		return resist;
	}
	
	public float getStability() {
		float stability = 0;
		for(int x = 0; x<gear.length; x++) {
			stability += gear[x].getStability();
		}
		stability = 1.25f - (stability / gear.length);
		System.out.println("Stability: " + stability);
		
		return stability;
	}
	
	@Override
	public String print() {
		String temp = "";
		for(int x = 0; x<gear.length; x++) {
			temp += getSlotName(x) + ": " + getGear(x).getNameColor() + getGear(x).getName() + ";";
		}
		
		return temp;
	}

	@Override
	public void load(String[] temp) {
		for(int x = 0; x<gear.length; x++) {
			setGear(x, Item.loadItem(temp[x+1]));
		}
	}

	@Override
	public String save() {
		String save = "1";
		for(int x = 0; x<gear.length; x++) {
			save += ";" + gear[x].getID();
		}
		
		return save;
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
			if(gear[slot].isUsable()) {
				Consumable temp = Consumable.loadConsumable(gear[slot].getID());
				temp.cycleEffects(source);
				text.buildText(temp.getFlavorText());
			} else
				text.buildText("You can't find a way to use this.");
			break;
		case(1):
			if(gear[slot].getID() == 0)
				text.buildText("You have nothing to drop!");
			else {
				text.buildText("You dropped your " + gear[slot].getName() + ".");
				source.getCostume().unequip();
				gear[slot] = Item.loadItem("0");
			}
			break;
		case(2):
			if(gear[slot].getID() == 0)
				text.buildText("You have nothing to hold!");
			else if(source.getEquipment().getWeapons().getGear(0).getID() != 0 && source.getEquipment().getWeapons().getGear(1).getID() != 0)
				text.buildText("Your hands are already full!");
			else {
				text.buildText("Now holding " + gear[slot].getName() + ".");
				if(source.getEquipment().getWeapons().getGear(0).getID() == 0)
					source.getEquipment().getWeapons().setGear(0, gear[slot]);
				else
					source.getEquipment().getWeapons().setGear(1, gear[slot]);
				gear[slot] = Item.loadItem("0");
			}
			break;
		}
		
		return (PlainText) text;
	}

}
