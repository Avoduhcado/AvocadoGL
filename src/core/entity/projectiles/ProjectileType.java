package core.entity.projectiles;

public enum ProjectileType {

	PROJECTILE,
	HOMING,
	AREA,
	ENCHANT;
	
	public static ProjectileType parseType(String type) {
		if(type.matches("PROJECTILE")) {
			return PROJECTILE;
		} else if(type.matches("HOMING")) {
			return HOMING;
		} else if(type.matches("AREA")) {
			return AREA;
		} else if(type.matches("ENCHANT")) {
			return ENCHANT;
		}
		
		return null;
	}
	
}
