package core.equipment.spells;

import core.entity.projectiles.ProjectileType;

public class EnchantSpell extends Spell {

	//private Enchant enchant;
	
	public EnchantSpell(int ID, String name, float cost) {
		this.ID = ID;
		this.name = name;
		this.spellType = ProjectileType.ENCHANT;
		this.cost = cost;
	}

	public void cast() {
		
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

}
