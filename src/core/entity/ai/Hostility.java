package core.entity.ai;

public enum Hostility {

	TIMID, 			// Will always run
	COWARD, 		// Will run when provoked
	NEUTRAL, 		// Will defend self but forgive
	ANGRY,			// Will attack back if provoked
	AGGRESSIVE,		// Will always attack
	CUSTOM;			// Custom settings
	
}
