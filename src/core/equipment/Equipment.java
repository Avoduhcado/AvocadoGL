package core.equipment;

import core.items.AmmoItem;
import core.items.ArmorItem;
import core.items.Item;
import core.items.PocketItem;
import core.items.WeaponItem;
import core.scene.Stage;
import core.utilities.text.HoldBox;
import core.utilities.text.PlainText;
import core.utilities.text.Textbox;

public class Equipment {

	private int gold;
	private Armor armor;
	private Weapons weapons;
	private Pockets pockets;
	private Quiver quiver;
	private Spellbook spellbook;
	
	public Equipment() {
		gold = 0;
		armor = new Armor();
		weapons = new Weapons();
		pockets = new Pockets();
		quiver = new Quiver();
		spellbook = new Spellbook();
	}
	
	public void equip(Item item) {
		if(item instanceof PocketItem) {
			pockets.addItem(item);
		} else if(item instanceof ArmorItem) {
			armor.setGear(item.getArmorSlot(), item);
		} else if(item instanceof WeaponItem) {
			weapons.setGear(item.getWeaponSlot(), item);
		} else if(item instanceof AmmoItem) {
			if(item.isWeapon()) {
				if(item.isStackable() && weapons.getGear(item.getWeaponSlot()).getID() == item.getID())
					weapons.getGear(item.getWeaponSlot()).setAmount(weapons.getGear(item.getWeaponSlot()).getAmount() + item.getAmount());
				else
					weapons.setGear(item.getWeaponSlot(), item);
			} else {
				if(item.isStackable() && quiver.getItem(item.getWeaponSlot()).getID() == item.getID())
					quiver.getItem(item.getWeaponSlot()).setAmount(quiver.getItem(item.getWeaponSlot()).getAmount() + item.getAmount());
				else
					quiver.addItem(item);
			}
		}
	}
	
	// Weapons with slot 0 will override currently equipped item
	public boolean canEquip(Item item, int slot) {
		if(item instanceof PocketItem) {
			for(int x = 0; x<pockets.size; x++) {
				if(pockets.getItem(x).getID() == 0 || (pockets.getItem(x).isStackable() && pockets.getItem(x).getID() == item.getID()))
					return true;
			}
			return false;
		} else if(item instanceof ArmorItem) {
			if(armor.getGear(slot).getID() != 0) {
				return false;
			}
		} else if(item instanceof WeaponItem) {
			if(weapons.getGear(slot).getID() != 0) {
				if(slot == 0 && weapons.getGear(1).getID() == 0)
					return true;
				return false;
			}
		} else if(item instanceof AmmoItem) {
			if(item.isWeapon()) {
				if(weapons.getGear(slot).getID() != 0 || (weapons.getGear(slot).isStackable() && weapons.getGear(slot).getID() == item.getID())) {
					if(slot == 0 && weapons.getGear(1).getID() == 0)
						return true;
					return false;
				}
			} else {
				for(int x = 0; x<quiver.size; x++) {
					if(quiver.getItem(x).getID() == 0 || (quiver.getItem(x).isStackable() && quiver.getItem(x).getID() == item.getID()))
						return true;
				}
				return false;
			}
		}
		
		return true;
	}
	
	public void unequip(Item item, Stage stage) {
		if(item instanceof PocketItem) {
			if(pockets.getItems().contains(item))
				pockets.removeItem(item);
			else if(weapons.getGear(0) == item) {
				weapons.setGear(0, Item.loadItem("0"));
			} else if(weapons.getGear(1) == item) {
				weapons.setGear(1, Item.loadItem("0"));
			}
		} else if(item instanceof ArmorItem) {
			if(armor.getGear(item.getArmorSlot()) == item)
				armor.setGear(item.getArmorSlot(), Item.loadItem("0"));
			else if(weapons.getGear(0) == item) {
				weapons.setGear(0, Item.loadItem("0"));
			} else if(weapons.getGear(1) == item) {
				weapons.setGear(1, Item.loadItem("0"));
			}
		} else if(item instanceof WeaponItem) {
			for(int x = 0; x<weapons.getGear().length; x++) {
				if(weapons.getGear(x) == item) {
					weapons.setGear(x, Item.loadItem("0"));
					break;
				}
			}
		} else if(item instanceof AmmoItem) {
			if(quiver.getItems().contains(item)) {
				quiver.removeItem(item);
			} else if(weapons.getGear(0) == item) {
				weapons.setGear(0, Item.loadItem("0"));
			} else if(weapons.getGear(1) == item) {
				weapons.setGear(1, Item.loadItem("0"));
			} else if(item.isWeapon() && weapons.getGear(item.getWeaponSlot()) == item) {
				weapons.setGear(item.getWeaponSlot(), Item.loadItem("0"));
			}
		}
		stage.menu.doUpdate();
	}
	
	public void holdItem(Item item) {
		if(weapons.getGear(0).getID() == 0)
			weapons.setGear(0, item);
		else if(weapons.getGear(1).getID() == 0)
			weapons.setGear(1, item);		
	}
	
	public Textbox holdItemPrompt(Item item, Stage stage) {
		String temp = "";
		if(item instanceof PocketItem) {
			temp = "Pockets are full!";
		} else if(item instanceof ArmorItem) {
			temp = "Armor slot is occupied!";
		} else if(item instanceof WeaponItem) {
			temp = "Weapon slot is occupied!";
		} else if(item instanceof AmmoItem) {
			temp = "Quiver is full!";
		}
		if(!canEquip(Item.loadItem("0"), 0)) {
			Textbox text = new PlainText(false, 3.0f, false);
			text.setSource(stage.player);
			text.buildText(temp + " And so are your hands!");
			return text;
		} else {
			HoldBox text = new HoldBox(true, temp, item);
			text.setSource(stage.player);
			return text;
		}
	}
	
	public boolean canHold() {
		if(weapons.getGear(0).getID() == 0 || weapons.getGear(1).getID() == 0)
			return true;
		
		return false;
	}
	
	public void weaponSpecial() {
		switch(weapons.getGear(weapons.getEquippedRight()).getWeaponType()) {
		case(0):
		case(1):
			weapons.holdTwoHanded();
			break;
		case(2):
			if(weapons.getGear(weapons.getEquippedRight()).isStackable()) {
				weapons.holdTwoHanded();
			} else {
				quiver.changeAmmo();
			}
			break;
		}
	}
	
	public String printSimple() {
		String equipment = gold + " <cwhite>Bone Splinters";
		if(weapons.isTwoHand()) {
			equipment += ";2H - " + weapons.getEquippedItemRight().getNameColor() + weapons.getEquippedItemRight().getName();
		} else {
			equipment += ";R - " + weapons.getEquippedItemRight().getNameColor() + weapons.getEquippedItemRight().getName() + 
					";L - " + weapons.getEquippedItemLeft().getNameColor() +  weapons.getEquippedItemLeft().getName();
		}
		
		if(quiver.getEquippedAmmo().getID() != 0) {
			equipment += ";Ammo - " + quiver.getEquippedAmmo().getNameColor() + quiver.getEquippedAmmo().getName();
		}
		
		if(pockets.getItem(pockets.getSelected()).getID() != 0) {
			equipment += ";Pocket - " + pockets.getItem(pockets.getSelected()).getNameColor() + pockets.getItem(pockets.getSelected()).getName();
		}
		
		return equipment;
	}
	
	public int getGold() {
		return gold;
	}
	
	public void setGold(int gold) {
		this.gold = gold;
	}
	
	public void addGold(int gold) {
		this.gold += gold;
		if(gold < 0)
			gold = 0;
	}
	
	public Armor getArmor() {
		return armor;
	}
	
	public void setArmor(Armor armor) {
		this.armor = armor;
	}
	
	public Weapons getWeapons() {
		return weapons;
	}
	
	public void setWeapons(Weapons weapons) {
		this.weapons = weapons;
	}
	
	public Pockets getPockets() {
		return pockets;
	}
	
	public void setPockets(Pockets pockets) {
		this.pockets = pockets;
	}
	
	public Quiver getQuiver() {
		return quiver;
	}
	
	public void setQuiver(Quiver quiver) {
		this.quiver = quiver;
	}
	
	public Spellbook getSpellbook() {
		return spellbook;
	}
	
	public void setSpellbook(Spellbook spellbook) {
		this.spellbook = spellbook;
	}

}
