package core.equipment;

import core.entity.Actor;
import core.entity.weapons.Weapon;
import core.items.Consumable;
import core.items.Item;
import core.utilities.text.PlainText;
import core.utilities.text.SelectBox;
import core.utilities.text.Textbox;

public class Weapons extends GearSet {

	private Weapon heldRight;
	private Weapon heldLeft;
	private int equippedRight = 0;
	private int equippedLeft = 1;
	private boolean twoHand;
	
	public Weapons() {
		gear = new Item[8];
		slotNames = new String[]{"Main Hand", "Off Hand ", "Main Sheath", "Off Sheath", "Side Hand", "Main Back", "Off Back", "Back  "};
		
		for(int x = 0; x<gear.length; x++) {
			gear[x] = Item.loadItem("0");
		}
	}
	
	public float getWeaponCrit(Equipment equipment, Item shield) {
		// Resist value is equal to total armor resist towards current style
		float resist = equipment.getArmor().getArmorResist(heldRight.isSwinging() ? heldRight : heldLeft, 
				heldRight.isSwinging() ? heldRight.getCurrentStyle() : heldLeft.getCurrentStyle());
		if(shield != null) {
			resist += shield.getItemResist(heldRight.isSwinging() ? heldRight : heldLeft, 
				heldRight.isSwinging() ? heldRight.getCurrentStyle() : heldLeft.getCurrentStyle());
		}
		// Crit value based on weapon damage divided by armor resist
		float crit = (getSwingingItem().getWeaponDamage() - resist) / getSwingingItem().getWeaponDamage();
		
		return crit;
	}
	
	public float getWeaponDamage() {
		int weapon = heldRight.isSwinging() ? equippedRight : equippedLeft;
		
		float damage = gear[weapon].getWeaponDamage() * gear[weapon].getSkill();
		
		return damage;
	}
	
	/**
	 * Swap which weapon an entity is holding in either hand.
	 * @param slot The item slot to hold
	 * @param right If true hold in right hand, else hold in left
	 */
	public void holdWeapon(int slot, boolean right) {
		if(twoHand) {
			twoHand = false;
			if(equippedRight != 1)
				equippedLeft = 1;
			else
				equippedLeft = 0;
		}
		if(right) {
			if(equippedLeft == slot)
				equippedLeft = 1;
			equippedRight = slot;
			heldRight = Weapon.loadWeapon(getEquippedItemRight());
			heldRight.setCurrentStyle(0);
			if(getEquippedItemRight().getSpells() != null) {
				heldRight.bindSpells(getEquippedItemRight().getSpells());
			}
		} else {
			if(equippedRight == slot)
				equippedRight = 0;
			equippedLeft = slot;
			heldLeft = Weapon.loadWeapon(getEquippedItemLeft());
			heldLeft.setCurrentStyle(4);
			if(getEquippedItemLeft().getSpells() != null) {
				heldLeft.bindSpells(getEquippedItemLeft().getSpells());
			}
		}
	}
	
	/**
	 * Hold your right hand weapon with both hands
	 */
	public void holdTwoHanded() {
		if(equippedRight != -1) {
			if(twoHand) {
				twoHand = false;
				if(equippedRight != 1)
					equippedLeft = 1;
				else
					equippedLeft = 0;
			} else {
				twoHand = true;
				equippedLeft = -1;
			}
		}
	}
	
	public boolean isTwoHand() {
		return twoHand;
	}
	
	public int getEquippedRight() {
		return equippedRight;
	}
	
	public Item getEquippedItemRight() {
		return gear[equippedRight];
	}
	
	public Item getSwingingItem() {
		if(heldRight.isSwinging())
			return gear[equippedRight];
		else
			return gear[equippedLeft];
	}
	
	public Weapon getSwingingWeapon() {
		if(heldRight.isSwinging())
			return heldRight;
		else if(heldLeft.isSwinging())
			return heldLeft;
		
		return null;
	}
	
	public Weapon getHeldRight() {
		if(heldRight == null)
			holdWeapon(equippedRight, true);
		return heldRight;
	}
	
	public int getEquippedLeft() {
		return equippedLeft;
	}
	
	public Item getEquippedItemLeft() {
		return gear[equippedLeft];
	}
	
	public Weapon getHeldLeft() {
		// Left hand weapon may be un-equipped due to two handing
		if(equippedLeft != -1) {
			if(heldLeft == null)
				holdWeapon(equippedLeft, false);
			return heldLeft;
		} else {
			return heldRight;
		}
	}
	
	@Override
	public void setGear(Item[] gear) {
		this.gear = gear;
		heldRight = Weapon.loadWeapon(getEquippedItemRight());
		heldLeft = Weapon.loadWeapon(getEquippedItemLeft());
	}
	
	@Override
	public void setGear(int slot, Item item) {
		this.gear[slot] = item;
		if(slot == equippedRight)
			heldRight = Weapon.loadWeapon(item);
		else if(slot == equippedLeft)
			heldLeft = Weapon.loadWeapon(item);
	}
	
	@Override
	public String print() {
		String temp = "";
		for(int x = 0; x<gear.length; x++) {
			if(x == equippedRight) {
				if(twoHand) {
					if(gear[x].getWeaponType() == 1)
						temp += "SC - " + getSlotName(x) + ": " + getGear(x).getNameColor() + getGear(x).getName() + ";";
					else
						temp += "2H - " + getSlotName(x) + ": " + getGear(x).getNameColor() + getGear(x).getName() + ";";
				} else
					temp += "R - " + getSlotName(x) + ": " + getGear(x).getNameColor() + getGear(x).getName() + ";";
				// Items with magical binds on them
				if(gear[x].getWeaponType() == 1 && heldRight.hasSpellBinds()) {
					temp = temp.substring(0, temp.length() - 1);
					temp += " (B);";
				}
			} else if(x == equippedLeft) {
				temp += "L - " + getSlotName(x) + ": " + getGear(x).getNameColor() + getGear(x).getName() + ";";
				// Items with magical binds on them
				if(gear[x].getWeaponType() == 1 && heldLeft.hasSpellBinds()) {
					temp = temp.substring(0, temp.length() - 1);
					temp += " (B);";
				}
			}
			else
				temp += getSlotName(x) + ": " + getGear(x).getNameColor() + getGear(x).getName() + ";";
			// Stackable items
			if(gear[x].getAmount() > 1) {
				temp = temp.substring(0, temp.length() - 1);
				temp += " (" + gear[x].getAmount() + ");";
			}
		}
		
		return temp;
	}

	@Override
	public void load(String[] temp) {
		for(int x = 0; x<gear.length; x++) {
			setGear(x, Item.loadItem(temp[x+1]));
		}
		if(temp[gear.length + 1].startsWith("T")) {
			equippedRight = Integer.parseInt(temp[gear.length + 1].substring(1));
			holdTwoHanded();
		} else {
			equippedRight = Integer.parseInt(temp[gear.length + 1].substring(1));
			holdWeapon(equippedRight, true);
			equippedLeft = Integer.parseInt(temp[gear.length + 2].substring(1));
			holdWeapon(equippedLeft, false);
		}
	}

	@Override
	public String save() {
		String save = "2";
		for(int x = 0; x<gear.length; x++) {
			save += ";" + gear[x].getID();
			save += gear[x].saveAttributes();
		}
		if(twoHand) {
			save += ";T" + equippedRight;
		} else {
			save += ";R" + equippedRight + ";L" + equippedLeft;
		}
				
		return save;
	}

	@Override
	public SelectBox interactMenu(int slot) {
		Textbox text = new SelectBox(false);
		text.setStill(true);
		String temp = "";
		if(slot == 0 || slot == 1) {
			temp += "Equip;";
		}
		temp += "Use;Drop;Hold";
		text.buildText(temp);
		
		return (SelectBox) text;
	}

	@Override
	// TODO Add confirmation to dropping? Or make items appear on ground as lootable entities
	public PlainText interact(int slot, int selection, Actor source) {
		Textbox text = new PlainText(false, 3.0f, false);
		text.setStill(true);
		if(slot == 0 || slot == 1) {
			switch(selection) {
			case(0):
				if(source.getEquipment().canEquip(gear[slot], gear[slot].getWeaponSlot())) {
					source.getEquipment().equip(gear[slot]);
					text.buildText("Equipped " + gear[slot].getName() + ".");
					gear[slot] = Item.loadItem("0");
				} else if(gear[slot].getID() == 0){
					text.buildText("You can't equip nothing!");
				} else {
					text.buildText("You can't equip that!");
				}
				break;
			case(1):
				if(gear[slot].isUsable()) {
					Consumable temp = Consumable.loadConsumable(gear[slot].getID());
					temp.cycleEffects(source);
					text.buildText(temp.getFlavorText());
				} else
					text.buildText("You can't find a way to use this.");
				break;
			case(2):
				if(gear[slot].getID() == 0)
					text.buildText("You have nothing to drop!");
				else {
					text.buildText("You dropped your " + gear[slot].getName() + ".");
					gear[slot] = Item.loadItem("0");
				}
				break;
			case(3):
				if(gear[slot].getID() == 0)
					text.buildText("You have nothing to hold!");
				else if(gear[0].getID() != 0 && gear[1].getID() != 0)
					text.buildText("Your hands are already full!");
				else {
					text.buildText("Now holding " + gear[slot].getName() + ".");
					if(gear[0].getID() == 0)
						gear[0] = gear[slot];
					else
						gear[1] = gear[slot];
					gear[slot] = Item.loadItem("0");
				}
				break;
			}
		} else {
			switch(selection) {
			case(0):
				if(gear[slot].isUsable()) {
					text.buildText("You used " + gear[slot].getName());
				} else
					text.buildText("You can't find a way to use this.");
				break;
			case(1):
				if(gear[slot].getID() == 0)
					text.buildText("You have nothing to drop!");
				else {
					text.buildText("You dropped your " + gear[slot].getName() + ".");
					gear[slot] = Item.loadItem("0");
				}
				break;
			case(2):
				if(gear[slot].getID() == 0)
					text.buildText("You have nothing to hold!");
				else if(gear[0].getID() != 0 && gear[1].getID() != 0)
					text.buildText("Your hands are already full!");
				else {
					text.buildText("Now holding " + gear[slot].getName() + ".");
					if(gear[0].getID() == 0)
						gear[0] = gear[slot];
					else
						gear[1] = gear[slot];
					gear[slot] = Item.loadItem("0");
				}
				break;
			}
		}
		
		return (PlainText) text;
	}

}
