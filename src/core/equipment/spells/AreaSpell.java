package core.equipment.spells;

import core.entity.projectiles.ProjectileType;

public class AreaSpell extends Spell {

	private float damage;
	private boolean moving;
	private float diameter;
	private float duration;
	private String areaTexture;
	
	public AreaSpell(int ID, String name, float cost,
			float damage, float diameter, boolean moving, float duration, String areaTexture) {
		this.ID = ID;
		this.name = name;
		this.spellType = ProjectileType.AREA;
		this.cost = cost;
		this.damage = damage;
		this.diameter = diameter;
		this.moving = moving;
		this.duration = duration;
		this.areaTexture = areaTexture;
	}

	public void cast() {
		
	}
	
	public float getDamage() {
		return damage;
	}

	public float getDiameter() {
		return diameter;
	}
	
	public boolean getMoving() {
		return moving;
	}
	
	public float getDuration() {
		return duration;
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
	public boolean getMulti() {
		return false;
	}

	@Override
	public String getAreaTexture() {
		return areaTexture;
	}

}
