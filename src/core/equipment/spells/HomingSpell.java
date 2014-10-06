package core.equipment.spells;

import core.entity.projectiles.ProjectileType;

public class HomingSpell extends Spell {

	private float speed;
	private boolean piercing;
	private float damage;
	private float diameter;
	private boolean multi;
	private String areaTexture;
	
	public HomingSpell(int ID, String name, float cost,
			float speed, boolean piercing, float damage, float diameter, boolean multi, String areaTexture) {
		this.ID = ID;
		this.name = name;
		this.spellType = ProjectileType.HOMING;
		this.cost = cost;
		this.speed = speed;
		this.piercing = piercing;
		this.damage = damage;
		this.diameter = diameter;
		this.multi = multi;
		this.areaTexture = areaTexture;
	}

	public void cast() {
		
	}
	
	public float getSpeed() {
		return speed;
	}

	public boolean getPiercing() {
		return piercing;
	}

	public float getDamage() {
		return damage;
	}

	@Override
	public float getDistance() {
		return 0;
	}

	public float getDiameter() {
		return diameter;
	}

	public boolean getMulti() {
		return multi;
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
		return areaTexture;
	}

}
