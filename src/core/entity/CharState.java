package core.entity;

public enum CharState {
	IDLE (""), 
	WALKING ("_walk"), 
	RUNNING ("_run"), 
	DEFEND ("_def"),
	ATTACKING ("_atk"), 
	HURT ("_hurt"), 
	DEAD ("_dead"), 
	DODGING ("_dodging");
	
	private String suffix;
	
	CharState(String suffix) {
		this.suffix = suffix;
	}
	
	public String getSuffix() {
		return this.suffix;
	}
}
