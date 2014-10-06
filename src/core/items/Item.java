package core.items;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import core.entity.projectiles.ProjectileType;
import core.entity.weapons.MagicWeapon;
import core.entity.weapons.MeleeWeapon;
import core.entity.weapons.RangedWeapon;
import core.entity.weapons.Weapon;
import core.equipment.spells.Spell;

public abstract class Item {

	protected int ID;
	protected String name;
	protected boolean stackable;
	protected int amount;
	protected int price;
	protected boolean usable;
	protected int weaponType;
	protected float vigorCost;
	protected String weaponName;
	protected float weaponDamage;
	protected Resistance resistance;
	protected Spell[] spells;
	
	public static Item loadItem(String ref) {
		BufferedReader reader;

		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/items"));

			String line;
			String[] ID = ref.split(":");
	    	String[] attributes = null;
	    	if(ID.length > 1) {
	    		attributes = ID[1].split("-");
	    	}

			while((line = reader.readLine()) != null) {
				String[] temp = line.split(";");
				
				if(temp[0].matches(ID[0])) {
					Item item = null;
					switch(Integer.parseInt(temp[0].charAt(0) + "")) {
					case(0):
					case(1):
						item = new PocketItem(Integer.parseInt(temp[0]), temp[1], Boolean.parseBoolean(temp[2]), 
		    					attributes != null ? Integer.parseInt(attributes[0]) : Integer.parseInt(temp[3]),
		    	    					Integer.parseInt(temp[4]), Boolean.parseBoolean(temp[5]), Integer.parseInt(temp[6]),
		    	    					Float.parseFloat(temp[7]), temp[8], Float.parseFloat(temp[9]), new Resistance(temp[10]),
		    	    					Boolean.parseBoolean(temp[11]));
						reader.close();
						return (PocketItem) item;
					case(2):
						item = new ArmorItem(Integer.parseInt(temp[0]), temp[1], Boolean.parseBoolean(temp[2]), 
		    					attributes != null ? Integer.parseInt(attributes[0]) : Integer.parseInt(temp[3]),
		    	    					Integer.parseInt(temp[4]), Boolean.parseBoolean(temp[5]), Integer.parseInt(temp[6]), 
		    	    					Float.parseFloat(temp[7]), temp[8], Float.parseFloat(temp[9]), new Resistance(temp[10]),
		    	    					Integer.parseInt(temp[11]), Float.parseFloat(temp[12]));
		    	    			
		    	    	reader.close();
		    	    	return (ArmorItem) item;
					case(3):
						item = new WeaponItem(Integer.parseInt(temp[0]), temp[1], Boolean.parseBoolean(temp[2]), 
		    					attributes != null ? Integer.parseInt(attributes[0]) : Integer.parseInt(temp[3]),
		    	    					Integer.parseInt(temp[4]), Boolean.parseBoolean(temp[5]), Integer.parseInt(temp[6]),
		    	    					Float.parseFloat(temp[7]), temp[8], Float.parseFloat(temp[9]), new Resistance(temp[10]),
		    	    					temp[0].charAt(0) == '3' ? Integer.parseInt(temp[11]) : 0,
		    	    					attributes != null ? Float.parseFloat(attributes[1]) : temp[0].charAt(0) == '2' ? Float.parseFloat(temp[12]) : 1f);
		    	    			
						if(attributes != null && attributes.length > 2) {
							item.parseSpells(attributes[2]);
						}
	
						reader.close();
						return (WeaponItem) item;
					case(4):
						item = new AmmoItem(Integer.parseInt(temp[0]), temp[1], Boolean.parseBoolean(temp[2]), 
		    					attributes != null ? Integer.parseInt(attributes[0]) : Integer.parseInt(temp[3]),
		    	    					Integer.parseInt(temp[4]), Boolean.parseBoolean(temp[5]), Integer.parseInt(temp[6]),
		    	    					Float.parseFloat(temp[7]), temp[8], Float.parseFloat(temp[9]), new Resistance(temp[10]),
		    	    					Float.parseFloat(temp[11]), Integer.parseInt(temp[12]), ProjectileType.parseType(temp[13]), temp[14]);
						
						((AmmoItem) item).parseAmmo(temp);
		    	    			
		    	    	reader.close();
		    	    	return (AmmoItem) item;
					}
				}
			}

			reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("The item database has been misplaced!");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("Item database failed to load!");
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static boolean validID(int ID) {
		BufferedReader reader;
		
		try {
			reader = new BufferedReader(new FileReader(System.getProperty("resources") + "/database/items"));

	    	String line;
	    	while((line = reader.readLine()) != null) {
	    		String[] temp = line.split(";");
	    		
	    		if(Integer.parseInt(temp[0]) == ID) {
		    		return true;
	    		}
	    	}

	    	reader.close();
	    } catch (FileNotFoundException e) {
	    	System.out.println("The item database has been misplaced!");
	    	e.printStackTrace();
	    } catch (IOException e) {
	    	System.out.println("Item database failed to load!");
	    	e.printStackTrace();
	    }
		
		return false;
	}
	
	public float getItemResist(Weapon weapon, int style) {
		float resist = 0.0f;
		if(weapon instanceof MeleeWeapon) {
			switch(style) {
			case(0):
				resist += getResistance().getThrust();
			break;
			case(1):
				resist += getResistance().getCrush();
			break;
			case(2):
				resist += getResistance().getSlash();
			break;
			}
		} else if(weapon instanceof MagicWeapon) {
			switch(style) {
			case(0):
			case(1):
			case(2):
				resist += getResistance().getMagic();
			break;
			case(3):
				resist += getResistance().getCrush();
			break;
			}
		} else if(weapon instanceof RangedWeapon) {
			switch(style) {
			case(0):
				resist += getResistance().getBody();
			break;
			case(1):
				resist += getResistance().getHead();
			break;
			case(2):
				resist += getResistance().getCripple();
			break;
			case(3):
				resist += getResistance().getCrush();
			break;
			}
		} else {
			switch(style) {
			case(0):
				resist += getResistance().getCrush();
			break;
			}
		}
		
		return resist;
	}
	
	public int getID() {
		return ID;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNameColor() {
		if(ID == 0) {
			return "<cdarkGray>";
		} else if(this instanceof WeaponItem) {
			return "<cgreen>";
		} else if(this instanceof ArmorItem) {
			return "<ccyan>";
		} else if(this instanceof PocketItem) {
			return "<clightGray>";
		} else if(this instanceof AmmoItem) {
			return "<cmagenta>";
		}
		
		return "<cgray>";
	}
	
	public boolean isStackable() {
		return stackable;
	}
	
	public int getAmount() {
		return amount;
	}
	
	public void setAmount(int amount) {
		this.amount = amount;
	}
	
	public int getPrice() {
		return price;
	}
	
	public boolean isUsable() {
		return usable;
	}
	
	public int getWeaponType() {
		return weaponType;
	}
	
	public float getAttackRange(int dir) {
		Weapon temp = Weapon.loadWeapon(this);
		temp.bindSpells(spells);
		
		return temp.getAttackRange(dir);
	}
	
	public float getVigorCost() {
		return vigorCost;
	}
	
	public String getWeaponName() {
		return weaponName;
	}
	
	public float getWeaponDamage() {
		return weaponDamage;
	}
	
	public Resistance getResistance() {
		return resistance;
	}
	
	public void parseSpells(String spells) {
		if(!spells.matches("0")) {
			String[] temp = spells.split(",");
			this.spells = new Spell[3];
			
			for(int x = 0; x<this.spells.length; x++) {
				this.spells[x] = Spell.loadSpell(temp[x]);
			}
		}
	}
	
	public String saveSpells() {
		String data = "";
		if(spells == null)
			data = "0";
		for(int x = 0; x<spells.length; x++) {
			data += spells[x].getID();
		}
		
		return data;
	}
	
	public int getFreeSpellSlot() {
		for(int x = 0; x<spells.length; x++) {
			if(spells[x].getID() == 0)
				return x;
		}
		
		return -1;
	}
	
	public Spell[] getSpells() {
		return spells;
	}
	
	public void setSpells(Spell[] spells) {
		this.spells = spells;
	}
	
	public void setSpell(int slot, Spell spell) {
		this.spells[slot] = spell;
	}
	
	public void addSpell(Spell spell) {
		for(int x = 0; x<spells.length; x++) {
			if(spells[x].getID() == 0)
				spells[x] = spell;
		}
	}
	
	// Weapons
	public abstract int getWeaponSlot();
	public abstract float getSkill();
	public abstract String saveAttributes();
	
	// Armor
	public abstract int getArmorSlot();
	public abstract float getStability();
	
	// Ammo
	public abstract boolean isWeapon();
	public abstract float getBreakChance();
	public abstract ProjectileType getAmmoType();
	public abstract String getAmmoName();
	public abstract float getSpeed();
	public abstract boolean getPiercing();
	public abstract float getDistance();
	public abstract float getDamage();
	public abstract float getDiameter();
	public abstract boolean getMulti();
	public abstract boolean getMoving();
	public abstract float getDuration();
	public abstract String getAreaTexture();
	
	// Pocket
	public abstract boolean isConsumable();
}
