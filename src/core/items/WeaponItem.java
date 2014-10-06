package core.items;

import core.entity.projectiles.ProjectileType;

public class WeaponItem extends Item {

	private int weaponSlot;
	private float skill;
	
	public WeaponItem(int ID, String name, boolean stackable, int amount, int price, boolean usable,
			int weaponType, float vigorCost, String weaponName, float weaponDamage, Resistance resistance,
			int weaponSlot, float skill) {
		this.ID = ID;
		this.name = name;
		this.stackable = stackable;
		this.amount = amount;
		this.price = price;
		this.usable = usable;
		this.weaponType = weaponType;
		this.vigorCost = vigorCost;
		this.weaponName = weaponName;
		this.weaponDamage = weaponDamage;
		this.resistance = resistance;
		this.weaponSlot = weaponSlot;
		this.skill = skill;
	}
	
	public String saveAttributes() {
		String data = "";
		if(amount > 1 || spells != null || skill > 1) {
			data += ":" + amount + "-" + skill + "-" + saveSpells();
		}
		
		return data;
	}

	public int getWeaponSlot() {
		return weaponSlot;
	}
	
	public float getSkill() {
		return skill;
	}

	@Override
	public int getArmorSlot() {
		return 0;
	}
	
	@Override
	public float getStability() {
		return 0;
	}

	@Override
	public float getBreakChance() {
		return 0;
	}
	
	@Override
	public boolean isConsumable() {
		return false;
	}

	@Override
	public boolean isWeapon() {
		return false;
	}

	@Override
	public ProjectileType getAmmoType() {
		return ProjectileType.PROJECTILE;
	}

	@Override
	public float getSpeed() {
		return 0;
	}

	@Override
	public boolean getPiercing() {
		return false;
	}

	@Override
	public float getDistance() {
		return 0;
	}

	@Override
	public float getDamage() {
		return 0;
	}

	@Override
	public float getDiameter() {
		return 0;
	}

	@Override
	public boolean getMulti() {
		return false;
	}

	@Override
	public boolean getMoving() {
		return false;
	}

	@Override
	public float getDuration() {
		return 0;
	}

	@Override
	public String getAreaTexture() {
		return null;
	}

	@Override
	public String getAmmoName() {
		return null;
	}

}
