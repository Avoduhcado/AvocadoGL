package core.equipment.spells;

import core.entity.projectiles.ProjectileType;

public class ProjectileSpell extends Spell {

	protected float speed;
	protected boolean piercing;
	protected float distance;
	protected float damage;
	
	public ProjectileSpell(int ID, String name, float cost,
			float speed, boolean piercing, float distance, float damage) {
		this.ID = ID;
		this.name = name;
		this.spellType = ProjectileType.PROJECTILE;
		this.cost = cost;
		this.speed = speed;
		this.piercing = piercing;
		this.distance = distance;
		this.damage = damage;
	}

	public void cast() {
		
	}
	
	public float getSpeed() {
		return speed;
	}

	public boolean getPiercing() {
		return piercing;
	}

	public float getDistance() {
		return distance;
	}

	public float getDamage() {
		return damage;
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
	
}
