package core.items;

import core.entity.projectiles.ProjectileType;

public class AmmoItem extends Item {

	private float breakChance;
	private boolean weapon;
	private int weaponSlot;
	private ProjectileType ammoType;
	private String ammoName;
	private float speed;
	private boolean piercing;
	private float distance;
	private float damage;
	private float radius;
	private boolean multi;
	private boolean moving;
	private float duration;
	private String areaTexture;
	
	public AmmoItem(int ID, String name, boolean stackable, int amount, int price, boolean usable,
			int weaponType, float vigorCost, String weaponName, float weaponDamage, Resistance resistance,
			float breakChance, int weapon, ProjectileType ammoType, String ammoName) {
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
		this.breakChance = breakChance;
		if(weapon != 0) {
			this.weapon = true;
			this.weaponSlot = weapon;
		}
		this.ammoType = ammoType;
		this.ammoName = ammoName;
	}
	
	public void parseAmmo(String[] temp) {
		switch(ammoType) {
		case PROJECTILE:
			this.speed = Float.parseFloat(temp[15]);
			this.distance = Float.parseFloat(temp[16]);
			this.piercing = Boolean.parseBoolean(temp[17]);
			this.damage = Float.parseFloat(temp[18]);
			break;
		case HOMING:
			this.speed = Float.parseFloat(temp[15]);
			this.piercing = Boolean.parseBoolean(temp[16]);
			this.damage = Float.parseFloat(temp[17]);
			this.radius = Float.parseFloat(temp[18]);
			this.multi = Boolean.parseBoolean(temp[19]);
			this.areaTexture = temp[20];
			break;
		case AREA:
			this.damage = Float.parseFloat(temp[15]);
			this.radius = Float.parseFloat(temp[16]);
			this.moving = Boolean.parseBoolean(temp[17]);
			this.duration = Float.parseFloat(temp[18]);
			this.areaTexture = temp[19];
			break;
		case ENCHANT:
			break;
		}
	}
	
	public float getBreakChance() {
		return breakChance;
	}

	public int getWeaponSlot() {
		return weaponSlot;
	}

	@Override
	public float getSkill() {
		return 1;
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
	public boolean isConsumable() {
		return false;
	}

	@Override
	public String saveAttributes() {
		return "";
	}

	@Override
	public boolean isWeapon() {
		return weapon;
	}

	@Override
	public ProjectileType getAmmoType() {
		return ammoType;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public boolean getPiercing() {
		return piercing;
	}

	@Override
	public float getDistance() {
		return distance;
	}

	@Override
	public float getDamage() {
		return damage;
	}

	@Override
	public float getDiameter() {
		return radius;
	}

	@Override
	public boolean getMulti() {
		return multi;
	}

	@Override
	public boolean getMoving() {
		return moving;
	}

	@Override
	public float getDuration() {
		return duration;
	}
	
	public String getAreaTexture() {
		return areaTexture;
	}

	public String getAmmoName() {
		return ammoName;
	}

}
